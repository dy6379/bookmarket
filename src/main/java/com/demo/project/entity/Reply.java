package com.demo.project.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Entity
@Table(name = "reply")
@Data
public class Reply  {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

	@ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
	
	@Size(min = 5, message = "댓글은 5자 이상 입력해주세요")
	private String content;
	
	@Pattern(regexp = "[1-5]", message = "별점은 1부터 5까지만 입력 가능합니다.")
	private String rating;  //문자열로 하고 변환해서 사용
	
	@Column(name = "createdAt", updatable = false)//처음 생숭후 업데이트 안됨
    @UpdateTimestamp							//새 데이터가 생성될때의 날짜 시간으로 생성(insert)
	private LocalDateTime createdAt;
	
	@Column(name = "modifyDate")//처음 생숭후 업데이트 안됨
    @UpdateTimestamp		
	private LocalDateTime modifyDate;

}
