package com.demo.project.controller;

import java.security.Principal;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.demo.project.ReplyForm;
import com.demo.project.entity.Book;
import com.demo.project.entity.Product;
import com.demo.project.entity.Reply;
import com.demo.project.entity.ReplyService;
import com.demo.project.entity.User;
import com.demo.project.repository.BookRepository;
import com.demo.project.repository.ProductRepository;
import com.demo.project.repository.UserRepository;

@Controller
@RequestMapping("/replies")
public class ReplyController {

    @Autowired
    private ReplyService replyService;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/product/{id}")
    public String createReply(@PathVariable("id") int id, @Valid ReplyForm replyform,BindingResult result,
            Model model,Principal principal, RedirectAttributes redirectAttributes) {
        Product product = productRepository.findById(id);
        
        User user = userRepository.findByUsername(principal.getName());
        
        if(result.hasErrors()) {
			model.addAttribute("product", product);
			model.addAttribute("replyForm", new ReplyForm());

			return "product_detail";
		}
        
        String rating = replyform.getRating();
        replyService.create(product, null, user, replyform.getContent(), rating);
        redirectAttributes.addFlashAttribute("message", "Reply added successfully");

        return String.format("redirect:/product/%d", id);
    }
    
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/book/{id}")
    public String createBookReply(@PathVariable("id") int id, @Valid ReplyForm replyform,BindingResult result,
            Model model,Principal principal, RedirectAttributes redirectAttributes) {
        
        Book book = bookRepository.findById(id);
        
        User user = userRepository.findByUsername(principal.getName());
        
        if(result.hasErrors()) {
			model.addAttribute("book", book);
			model.addAttribute("replyForm", new ReplyForm());

			return "book_detail";
		}
        
        String rating = replyform.getRating();
        replyService.create(null, book, user, replyform.getContent(), rating);
        redirectAttributes.addFlashAttribute("message", "Reply added successfully");

        return String.format("redirect:/book/%d", id);
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String replyModify(ReplyForm replyform, @PathVariable("id") int id, Principal principal) {
    	
    	Reply reply = this.replyService.getReply(id);
    	if(!reply.getUser().getUsername().equals(principal.getName())) {
    		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
    	}
    	replyform.setContent(reply.getContent());
    	replyform.setRating(reply.getRating()); // 별점 추가
    	return "reply_form";
    }
    
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String replyModify(@Valid ReplyForm replyform,BindingResult bindingResult,
    			@PathVariable("id") int id, Principal principal) {
    	
    	if (bindingResult.hasErrors()) {
    	    Reply reply = this.replyService.getReply(id);
    	    if (reply.getProduct() != null) {
    	        return "product_detail";
    	    } else if (reply.getBook() != null) {
    	        return "book_detail";
    	    } 
    	}
    	
    	Reply reply = this.replyService.getReply(id);
    	if(!reply.getUser().getUsername().equals(principal.getName())) {
    		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
    	}
    	this.replyService.modify(reply, replyform.getContent(), replyform.getRating());
    	if (reply.getProduct() != null) {
            return String.format("redirect:/product/%d", reply.getProduct().getId());
        } else if (reply.getBook() != null) {
            return String.format("redirect:/book/%d", reply.getBook().getId());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "댓글이 올바르지 않습니다.");
        }
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String answerDelete(Principal principal, @PathVariable("id") Integer id
    		, @RequestHeader("Referer") String referer) {
        Reply reply = this.replyService.getReply(id);
        if (!reply.getUser().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.replyService.delete(reply);
        
        return "redirect:" + referer;
    }
    
    
}
