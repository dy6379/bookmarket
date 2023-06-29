package com.demo.project.entity;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.project.DataNotFoundException;
import com.demo.project.repository.ReplyRepository;

@Service
public class ReplyService {
	
	@Autowired
	private ReplyRepository replyRepo;
	
	public void create(Product product, Book book, User user, String content, String rating) {
	    Reply reply = new Reply();
	    reply.setProduct(product);
	    reply.setBook(book);
	    reply.setUser(user);
	    reply.setContent(content);
	    reply.setRating(rating);
	    reply.setCreatedAt(LocalDateTime.now());
	    this.replyRepo.save(reply);
	}
	
	public Reply getReply(int replyId) {
	    Optional<Reply> reply = this.replyRepo.findById(replyId);
	    if (reply.isPresent()) {
	        return reply.get();
	    } else {
	        throw new DataNotFoundException("Reply not found");
	    }
	}
	
	public void modify(Reply reply, String content, String rating) {
		reply.setContent(content);
		reply.setRating(rating);
		reply.setModifyDate(LocalDateTime.now());
        this.replyRepo.save(reply);
    }
	
	public void delete(Reply reply) {
		this.replyRepo.delete(reply);
	}

}
