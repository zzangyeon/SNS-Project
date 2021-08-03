package com.cos.photogramstart.web.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.service.SubscribeService;
import com.cos.photogramstart.service.UserService;
import com.cos.photogramstart.web.dto.CMRespDto;
import com.cos.photogramstart.web.dto.subscribe.SubscribeDto;
import com.cos.photogramstart.web.dto.user.UserUpdateDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserApiController {
	
	private final UserService userService;
	private final SubscribeService subscribeService;
	
	@PutMapping("/api/user/{principalId}/profileImageUrl")
	public ResponseEntity<?> profileImageUrlUpdate(@PathVariable int principalId,MultipartFile profileImageFile,@AuthenticationPrincipal PrincipalDetails principalDetails){//변수명이 name과 같아야 받을 수 있다.
		User userEntity = userService.회원프로필사진병경(principalId,profileImageFile);
		principalDetails.setUser(userEntity);//세션값 변경 안해도 작동이 잘 되긴하는데... 이유가 있을까!?
		return new ResponseEntity<>(new CMRespDto<>(1,"회원프로필 변경 성공",null), HttpStatus.OK);
	}
	
	@GetMapping("/api/user/{pageUserId}/subscribe")
	public ResponseEntity<?> subscribeList(@PathVariable int pageUserId, @AuthenticationPrincipal PrincipalDetails principalDetails){
		List<SubscribeDto>subscribeDto = subscribeService.구독리스트(principalDetails.getUser().getId(),pageUserId);
		return new ResponseEntity<>(new CMRespDto<>(1,"구독자 정보 리스트 불러오기 성공",subscribeDto), HttpStatus.OK);
	}
	
	@PutMapping("/api/user/{id}")
	public CMRespDto<?> update(
			@PathVariable int id,
			@Valid UserUpdateDto userUpdateDto,
			BindingResult bindingResult,//꼭 @Valid 가 적혀있는 다음 파라미터에 적어야한다!!!
			@AuthenticationPrincipal PrincipalDetails principalDetails){//CMRespDto이용해 api로 ajax응답...
	
			User userEntity = userService.회원수정(id, userUpdateDto.toEntity());
			principalDetails.setUser(userEntity);//세션정보수정 - 세션에도 바뀐 userEntity정보를 저장해 주어야 다시 회원정보수정에 들어갔을때 반영이 된다.
			return new CMRespDto<>(1,"회원수정완료",userEntity);//응답시 userEntity의 모든 getter함수가 호출되고 JSON으로 파싱하여 응답한다.JSON으로 파싱시 메세지 컨버터가 모든 getter함수를 호출 실행한다.
			//userEntity 리턴시 getter함수로 호출하고 그것을 JSON으로 바꾸는 과정에서 User 의 List<Image> images가 getter로 인해 계속 list에 데이터가 담기고 그것을 JSON으로 바꾸고 하는 과정에서 무한반복이 일어난다!			
	}
	
}
