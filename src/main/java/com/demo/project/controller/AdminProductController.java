package com.demo.project.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.demo.project.entity.Category;
import com.demo.project.entity.Product;
import com.demo.project.repository.CategoryRepository;
import com.demo.project.repository.ProductRepository;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	private CategoryRepository categoryRepo;
	
	@GetMapping
	public String page(Model model, @RequestParam(value = "page", defaultValue = "0") Integer page) {
	    
		int perPage = 10;
		Pageable pageable = PageRequest.of(page, perPage);

		Page<Product> products = productRepo.findAll(pageable);
		
	    List<Category> categories = categoryRepo.findAll();

	    HashMap<Integer, String> cats = new HashMap<>();
	    for (Category cat : categories) {
	        cats.put(cat.getId(), cat.getName());
	    }

	    model.addAttribute("products", products);
		model.addAttribute("cats", cats);
		
		long count = productRepo.count(); //상품 갯수
		double pageCount = Math.ceil((double)count / (double)perPage); //표시할 페이지 숫자 (나머지가 있으면 + 1)
		
        model.addAttribute("pageCount", (int)pageCount);
        model.addAttribute("perPage", perPage);
        model.addAttribute("count", count);
        model.addAttribute("page", page);

	    return "/admin/products/page";
	}
	
	@GetMapping("/add")
	public String add(@ModelAttribute Product product, Model model) {
		
		List<Category> categories = categoryRepo.findAll();
		
		model.addAttribute("categories", categories);
				
		return "/admin/products/add"; 
	}
	
	@PostMapping("/add")
	public String add(@Valid Product product, BindingResult bindingResult, MultipartFile file,
			RedirectAttributes redirectAttributes, Model model) throws IOException {

		if (bindingResult.hasErrors()) {
			List<Category> categories = categoryRepo.findAll();
			model.addAttribute("categories",categories);
			return "/admin/products/add";
		}

		boolean fileOk = false;
		byte[] bytes = file.getBytes();				  // 업로드한 파일의 데이터
		String fileName = file.getOriginalFilename(); // 업로드한 파일의 이름
		Path path = Paths.get("src/main/resources/static/image/" + fileName); // 파일을 저장할 컨텍스트 안의 경로 

		if (fileName.endsWith("jpg") || fileName.endsWith("png")) { // 파일의 확장자 jpg , png
			fileOk = true;
		}

		// 성공적으로 product 추가 되는 경우
		redirectAttributes.addFlashAttribute("message", "상품이 추가됨");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");

		String slug = product.getName().toLowerCase().replace(" ", "-");

		Product productExists = productRepo.findByName(product.getName());

		if (!fileOk) { // file 업로드 안되거나 확장자가 틀림
			
			redirectAttributes.addFlashAttribute("message", "이미지는 jpg나 png를 사용해 주세요");
			redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
			redirectAttributes.addFlashAttribute("product",product);
			
		} else if (productExists != null) { //이미 등록된 상품 있음
			
			redirectAttributes.addFlashAttribute("message", "상품이 이미 있습니다. 다른것을 고르세요");
			redirectAttributes.addFlashAttribute("alertClass", "alert-danger");		
			redirectAttributes.addFlashAttribute("product",product);
			
		} else {  // 상품과 이미지 파일을 저장한다.
			
			product.setSlug(slug); // 슬러그를 - 형식으로 수저정해 저장
			product.setImage(fileName);
			productRepo.save(product);
			
			Files.write(path, bytes);
		}
		return "redirect:/admin/products/add";
	}
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable int id, Model model) {
		
		Product product = productRepo.findById(id);
		List<Category> categories = categoryRepo.findAll();
		
		model.addAttribute("categories", categories);
		model.addAttribute("product", product);
		return "admin/products/edit";
	}
	
	@PostMapping("/edit")
	public String edit(@Valid Product product, BindingResult bindingResult, MultipartFile file,
			RedirectAttributes redirectAttributes, Model model, @RequestParam int id) throws IOException {
        //우선 수정하기전의 상품의 객체를 DB에서 읽어오기 ( id 로 검색 )
		Product currentProduct = productRepo.findById(id);

		if (bindingResult.hasErrors()) {
			List<Category> categories = categoryRepo.findAll();
			model.addAttribute("categories", categories);
			return "/admin/products/edit";
		}

		boolean fileOk = false;
		byte[] bytes = file.getBytes(); // 업로드한 파일의 데이터
		String fileName = file.getOriginalFilename(); // 업로드한 파일의 이름
		Path path = Paths.get("src/main/resources/static/image/" + fileName); // 파일을 저장할 컨텍스트 안의 경로

		if (!file.isEmpty()) { // 새 이미지 파일이 있을경우
			if (fileName.endsWith("jpg") || fileName.endsWith("png")) { // 파일의 확장자 jpg , png
				fileOk = true;
			}
		} else { // 파일이 없을경우 ( 수정 이므로 이미지 파일이 없어도 OK )
			fileOk = true;
		}

		// 성공적으로 product 수정 되는 경우
		redirectAttributes.addFlashAttribute("message", "상품이 수정됨");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");

		String slug = product.getName().toLowerCase().replace(" ", "-");
                             //제품이름을 수정했을 경우에 slug가 다름 제품과 같지 않는지 검사
		Product productExists = productRepo.findBySlugAndIdNot(slug, product.getId());

		if (!fileOk) { // file 업로드 안되거나 확장자가 틀림

			redirectAttributes.addFlashAttribute("message", "이미지는 jpg나 png를 사용해 주세요");
			redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
			redirectAttributes.addFlashAttribute("product", product);

		} else if (productExists != null) { // 이미 등록된 상품 있음

			redirectAttributes.addFlashAttribute("message", "상품이 이미 있습니다. 다른것을 고르세요");
			redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
			redirectAttributes.addFlashAttribute("product", product);

		} else { // 상품과 이미지 파일을 저장한다.

			product.setSlug(slug); // 슬러그 저장
			
			if (!file.isEmpty()) { // 수정할 이미지 파일이 있을 경우에만 저장(이때 이전 파일 삭제)
				Path currentPath = Paths.get("src/main/resources/static/image/" + currentProduct.getImage());
				Files.delete(currentPath);
				product.setImage(fileName);				
				Files.write(path, bytes);	//Files 클래스를 사용해 파일을 저장		
			} else {
				product.setImage(currentProduct.getImage());	
			}
			
			productRepo.save(product);
			
		}
		return "redirect:/admin/products/edit/" + product.getId();
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable int id, RedirectAttributes redirectAttributes) throws IOException {
		// id로 상품을 삭제하기 전에 먼저 id로 제품객체를 불러와서 이미지 파일을 삭제한후 제품 삭제	
		Product currentProduct = productRepo.findById(id) ;
		Path currentPath = Paths.get("src/main/resources/static/image/" + currentProduct.getImage());

		Files.delete(currentPath);
		productRepo.deleteById(id);
		
		redirectAttributes.addFlashAttribute("message", "성공적으로 삭제 되었습니다.");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");		
		
		return "redirect:/admin/products";
	}
	
	
}
