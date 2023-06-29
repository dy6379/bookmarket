package com.demo.project.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.project.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

	Product findByName(String name);
	
	List<Product> findAllByName(String name);

	Product findById(int id);

	Product findBySlugAndIdNot(String slug, int id);

	Page<Product> findAll(Pageable pageable);

	List<Product> findAllByCategoryId(String categoryId, Pageable pageable);

	long countByCategoryId(String categoryId);

	List<Product> findAllByNameContaining(String name);
}
