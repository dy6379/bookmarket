package com.demo.project.entity;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Entity
@Data
@Table(name = "books")
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@OneToMany(mappedBy = "book", cascade = CascadeType.REMOVE)
    private List<Reply> replyList;
	
	@Size(min = 2, max = 20 , message = "도서명은 2자 이상 20자 이하 입력해 주세요")
	private String name;
	
	private String slug;
	
	@Size(min = 5, message = "도서설명은 5자 이상 입력해주세요")
	private String description;
	
	private String image;
	
	@Pattern(regexp = "^[1-9][0-9]*") //숫자만 1~99999999999
	private String price;  //문자열로 하고 변환해서 사용
	
	@Size(min = 2, max = 20, message = "저자는 2~20자까지 입력 가능합니다")
	private String author;
	
	@Size(min = 2, max = 12, message = "출판사는 2~12자까지 입력 가능합니다")
	private String publisher;
	
	private Date publicdate;
	
	private String bookCondition;
	
	@Pattern(regexp = "^[1-9][0-9]*", message = "카테고리를 선택해 주세요")
	@Column(name = "category_id")
	private String categoryId;
	
    @Column(name = "created_at", updatable = false)//처음 생숭후 업데이트 안됨
    @UpdateTimestamp							//새 데이터가 생성될때의 날짜 시간으로 생성(insert)
	private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    @UpdateTimestamp				// 수정될때마다 자동생성(update)
	private LocalDateTime updatedAt;
    
    private int sorting;
}
