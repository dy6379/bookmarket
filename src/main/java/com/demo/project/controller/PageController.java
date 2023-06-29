package com.demo.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.demo.project.entity.Page;
import com.demo.project.repository.PageRepository;

@Controller
@RequestMapping("/")
public class PageController {

	@Autowired
	private PageRepository pageRepo;
	
//	@GetMapping
//	public String home(Model model) {
//		
//		Page page = pageRepo.findBySlug("home");
//		model.addAttribute("page",page);
//		
//		return "page";
//	}
	@GetMapping
	private String index(Model model) {
		return "index";
	}
	
	@GetMapping("/login")
	public String login(Model model) {
		return("login");
	}

	@GetMapping("/{slug}")
    public String page(@PathVariable String slug, Model model) {
        
        Page page = pageRepo.findBySlug(slug);

        if (page == null) {
            return "redirect:/";
        }
        
        model.addAttribute("page", page);
        
        return "page";
    }
	
}
