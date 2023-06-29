package com.demo.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.project.entity.Page;

public interface PageRepository extends JpaRepository<Page, Integer> {

	Page findBySlug(String slug);
	
	Page findBySlugAndIdNot(String slug, int id);
	
	List<Page> findAllByOrderBySortingAsc();

	Page findById(int[] id);
}
