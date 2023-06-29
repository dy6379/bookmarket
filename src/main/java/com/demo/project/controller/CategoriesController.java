package com.demo.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.demo.project.entity.Product;
import com.demo.project.repository.ProductRepository;
import com.demo.project.entity.Book;
import com.demo.project.entity.Category;
import com.demo.project.repository.BookRepository;
import com.demo.project.repository.CategoryRepository;

@Controller
@RequestMapping("/category")
public class CategoriesController {

	@Autowired
	private CategoryRepository categoryRepo;
	
	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	private BookRepository bookRepo;
	
	@GetMapping("/{slug}")
	public String category(@PathVariable String slug, Model model, @RequestParam(value = "page", defaultValue = "0") Integer page) {

	    int perPage = 6; // 한 페이지에 6개
	    Pageable pageable = PageRequest.of(page, perPage);
	    long count = 0; // 상품 갯수 초기값 0

	    if (slug.equals("all")) {
	        Page<Product> products = productRepo.findAll(pageable);
	        Page<Book> books = bookRepo.findAll(pageable);
	        count = productRepo.count(); // 상품 갯수
	        count = bookRepo.count(); // 도서 갯수

	        model.addAttribute("products", products);
	        model.addAttribute("books", books);

	    } else {

	        Category category = categoryRepo.findBySlug(slug);
	        if (category == null) {
	            return "redirect:/";
	        }
	        String categoryId = Integer.toString(category.getId());
	        String categoryName = category.getName();
	        List<Product> products = productRepo.findAllByCategoryId(categoryId, pageable);
	        List<Book> books = bookRepo.findAllByCategoryId(categoryId, pageable);
	        count = productRepo.countByCategoryId(categoryId);
	        count = bookRepo.countByCategoryId(categoryId);

	        model.addAttribute("products", products);
	        model.addAttribute("books",books);
	        model.addAttribute("categoryName", categoryName);
	    }

	    double pageCount = Math.ceil((double) count / (double) perPage); // 표시할 페이지 숫자 (나머지가 있으면 + 1)

	    model.addAttribute("pageCount", (int) pageCount);
	    model.addAttribute("perPage", perPage);
	    model.addAttribute("count", count);
	    model.addAttribute("page", page);

	    return "products";
	}
	
}

