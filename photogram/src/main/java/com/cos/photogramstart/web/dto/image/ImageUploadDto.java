package com.cos.photogramstart.web.dto.image;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotBlank;

import org.springframework.web.multipart.MultipartFile;

import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.user.User;

import lombok.Data;

@Data
public class ImageUploadDto {
	
	private MultipartFile file;//@NotBlank - multipartfile타입에서는 이 validation이 먹히지 않는다!! 하지만 전처리를 해줘야하기 때문에!~ 다른방법을 쓰자!
	private String caption;
	
	
	public Image toEntity(String postImageUrl,User user) {
		return Image.builder()
				.caption(caption)
				.postImageUrl(postImageUrl)
				.user(user)
				.build();
	}

}
