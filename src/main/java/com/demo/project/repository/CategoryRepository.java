package com.demo.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.project.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

	Category findByName(String name);

	List<Category> findAllByOrderBySortingAsc();

	Category findBySlug(String slug);
}
