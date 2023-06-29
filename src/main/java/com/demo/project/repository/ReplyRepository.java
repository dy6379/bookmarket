package com.demo.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.project.entity.Book;
import com.demo.project.entity.Product;
import com.demo.project.entity.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Integer> {

	List<Reply> findByBook(Book book);

	List<Reply> findByProduct(Product product);

}
