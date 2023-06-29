package com.demo.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.demo.project.ReplyForm;
import com.demo.project.entity.Book;
import com.demo.project.entity.Product;
import com.demo.project.entity.Reply;
import com.demo.project.repository.BookRepository;
import com.demo.project.repository.ProductRepository;
import com.demo.project.repository.ReplyRepository;

@Controller
public class DetailController {

	@Autowired
	private BookRepository bookRepo;
	
	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	private ReplyRepository replyRepo;

	@GetMapping("/book/{id}")
    public String showBookDetail(@PathVariable int id, Model model, ReplyForm replyForm) {
        Book book = bookRepo.findById(id);
        if (book == null) {
            return "redirect:/"; 
        }
        model.addAttribute("book", book);
        
        List<Reply> replies = replyRepo.findByBook(book);
        model.addAttribute("replies",replies);
        
        List<Book> books = bookRepo.findAll();
        model.addAttribute("books",books);
        
     // 'reply' 객체를 생성하여 모델에 추가
        Reply reply = new Reply();
        model.addAttribute("reply", reply);
        
        return "book_detail";
    }
	
	@GetMapping("/product/{id}")
    public String showProductDetail(@PathVariable int id, Model model, ReplyForm replyForm) {
        Product product = productRepo.findById(id);
        if (product == null) {
            return "redirect:/"; 
        }
        model.addAttribute("product", product);
        
        List<Reply> replies = replyRepo.findByProduct(product);
        model.addAttribute("replies",replies);
        
        List<Product> products = productRepo.findAll();
        model.addAttribute("products",products);
        
     // 'reply' 객체를 생성하여 모델에 추가
        Reply reply = new Reply();
        model.addAttribute("reply", reply);
        
        return "product_detail";
    }
	
	
}
