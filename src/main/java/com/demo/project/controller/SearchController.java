package com.demo.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.demo.project.entity.Book;
import com.demo.project.entity.Product;
import com.demo.project.repository.BookRepository;
import com.demo.project.repository.ProductRepository;

@Controller
public class SearchController {
	
	@Autowired
	private BookRepository bookRepo;
	
	@Autowired
	private ProductRepository productRepo;
	
	@GetMapping("/search")
	public String search(@RequestParam("keyword") String name, Model model) {
		if (name.length() >= 2) {
			// 제품 검색
			List<Product> productSearchResult = productRepo.findAllByNameContaining(name);
			model.addAttribute("productSearchResult", productSearchResult);
	        
			// 책 검색
			List<Book> bookSearchResult = bookRepo.findAllByNameContaining(name);
			model.addAttribute("bookSearchResult", bookSearchResult);
		} else {
			model.addAttribute("productSearchResult", null);
			model.addAttribute("bookSearchResult", null);
		}
	    
	    return "search"; // 검색 결과를 보여주는 HTML 템플릿으로 이동합니다.
	}

	
}
