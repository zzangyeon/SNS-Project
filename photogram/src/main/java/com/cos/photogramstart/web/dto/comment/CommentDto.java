package com.cos.photogramstart.web.dto.comment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class CommentDto {

	@NotBlank//빈값 , 빈 공백(space), null 체크
	private String content;
	@NotNull //-null 체크  /@Notempty - 빈값,null 체크
	private int imageId;//int는 나머지 안되고 @NotNull만 됨!
	
	//toEntity가 필요없다.
}
