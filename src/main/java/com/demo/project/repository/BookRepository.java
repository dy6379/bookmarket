package com.demo.project.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.project.entity.Book;

public interface BookRepository extends JpaRepository<Book, Integer> {

	Book findByName(String name);
	
	Book findById(int id);
	
	Book findBySlugAndIdNot(String slug, int id);
	
	Page<Book> findAll(Pageable pageable);

	List<Book> findAllByCategoryId(String categoryId, Pageable pageable);
	
	List<Book> findAllByCategoryId(String categoryId);

	long countByCategoryId(String categoryId);

	List<Book> findAllByName(String name);

	List<Book> findAllByNameContaining(String name);
}
