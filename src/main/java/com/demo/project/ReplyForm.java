package com.demo.project;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReplyForm {

	@NotBlank(message = "내용은 필수항목입니다.")
	private String content;//답변
	
	@Pattern(regexp = "[1-5]", message = "별점은 1부터 5까지만 입력 가능합니다.")
	private String rating;  //문자열로 하고 변환해서 사용
}
