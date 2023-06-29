package com.demo.project;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.demo.project.entity.Cart;
import com.demo.project.entity.Category;
import com.demo.project.entity.Page;
import com.demo.project.repository.CategoryRepository;
import com.demo.project.repository.PageRepository;

@ControllerAdvice
public class Common {

	@Autowired
	private PageRepository pageRepo;
	
	@Autowired
	private CategoryRepository categoryRepo;
	
	@ModelAttribute
	@SuppressWarnings("unchecked")
	public void sharedData(Model model, HttpSession session, Principal principal) {
		
		if(principal != null) {
			model.addAttribute("principal", principal.getName());
		}
		
		List<Page> cpages = pageRepo.findAllByOrderBySortingAsc();
		List<Category> categories = categoryRepo.findAllByOrderBySortingAsc();
		
		boolean cartActive = false;
		
		if (session.getAttribute("cart") != null) {
			
			HashMap<Integer, Cart> cart = (HashMap<Integer, Cart>) session.getAttribute("cart");
			
			int size = 0;
			int total = 0;
			
			for(Cart item : cart.values()) {
				size += item.getQuantity();
				total += item.getQuantity() * Integer.parseInt(item.getPrice());
			}
			model.addAttribute("csize", size);
			model.addAttribute("ctotal", total);
			cartActive = true;
		}
		
		model.addAttribute("cpages", cpages);
		model.addAttribute("categories", categories);
		model.addAttribute("cartActive", cartActive);
		
	}
}
