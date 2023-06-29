package com.demo.project.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import lombok.Data;

@Entity
@Table(name="pages")
@Data
public class Page {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Size(min=2, message = "제목은 2자 이상 입력해주세요")
	private String title;
	
	private String slug;
	
	@Size(min=5, message = "내용은 5자 이상 입력해주세요")
	private String content;
	
	private int sorting;
}
