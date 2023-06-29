package com.demo.project.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.demo.project.entity.Category;
import com.demo.project.repository.CategoryRepository;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {

	@Autowired
	private CategoryRepository categoryRepo;
	
	@GetMapping
	public String page(Model model) {
		
		List<Category> categories = categoryRepo.findAllByOrderBySortingAsc(); //카테고리 전체 리스트
		
		model.addAttribute("categories",categories);
		
		return "/admin/categories/page";
		
	}
	
	@GetMapping("/add")
	public String add(@ModelAttribute Category category) {
		return "/admin/categories/add";
	}
	
	@PostMapping("/add")
	public String add(@Valid Category category, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

		if(bindingResult.hasErrors()) {
			return "/admin/categories/add";
		}
		// 성공적으로 cartegory 추가 되는 경우
		redirectAttributes.addFlashAttribute("message", "카테고리 추가됨");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");
		
		String slug = category.getName().toLowerCase().replace(" ", "-");
		
		Category categoryExists = categoryRepo.findByName(category.getName());
		
		if(categoryExists != null) {
			redirectAttributes.addFlashAttribute("message", "카테고리가 이미 있습니다. 다른것을 고르세요");
			redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
			redirectAttributes.addFlashAttribute("category", category); //입력내용을 보여주기위함
		} else {
			category.setSlug(slug);  //슬러그를 - 형식으로 수정해 저장
			category.setSorting(100); //기본 솔팅값
			
			categoryRepo.save(category);
		}
		return "redirect:/admin/categories/add";
	}
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable int id, Model model) {
		Category category = categoryRepo.findById(id).orElse(null);
		model.addAttribute("category",category);
		return "admin/categories/edit";
	}
	
	@PostMapping("/edit")
	public String edit(@Valid Category category, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

		if(bindingResult.hasErrors()) {
			return "/admin/categories/edit";
		}
		// 성공적으로 page 수정 되는 경우
		redirectAttributes.addFlashAttribute("message", "카테고리 수정됨");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");
		
		String slug = category.getName().toLowerCase().replace(" ", "-");
		
		Category categoryExists = categoryRepo.findByName(category.getName()); //같은 카테고리가 있는지 검사
		
		if(categoryExists != null) {
			redirectAttributes.addFlashAttribute("message", "카테고리가 이미 있습니다. 다른것을 고르세요");
			redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
			redirectAttributes.addFlashAttribute("category", category); //입력내용을 보여주기위함
		} else {
			category.setSlug(slug);  //슬러그를 - 형식으로 수정해 저장
			category.setSorting(100); //기본 솔팅값
			
			categoryRepo.save(category);
		}
		return "redirect:/admin/categories/edit/" + category.getId();
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable int id, RedirectAttributes redirectAttributes) {

		categoryRepo.deleteById(id);

		redirectAttributes.addFlashAttribute("message", "성공적으로 삭제되었습니다.");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");

		return "redirect:/admin/categories";
	}
	
	@PostMapping("/reorder")
	public @ResponseBody String reorder(@RequestParam("id[]") int[] id) {

	    int count = 1;
        Category category;

        for (int categoryId : id) {
            Optional<Category> optionalPage = categoryRepo.findById(categoryId);
            category = optionalPage.get();
            category.setSorting(count);
            categoryRepo.save(category);
            count++;
        }

        return "ok";
    }
	
}
