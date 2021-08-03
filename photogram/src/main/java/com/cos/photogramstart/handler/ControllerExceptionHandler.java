package com.cos.photogramstart.handler;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.handler.ex.CustomException;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.util.Script;
import com.cos.photogramstart.web.dto.CMRespDto;

@RestController//데이터 리턴(파일이 아닌)
@ControllerAdvice//모든 예외 발생시 이 클래스가 예외를 다 낚아챈다
public class ControllerExceptionHandler {
	

	 @ExceptionHandler(CustomValidationException.class)//RuntimeException은 매개변수로 string만 받는다.. so,customvalidationexception을 만들어서 쓴다.
	 public String vallidationException(CustomValidationException e) { //제네릭타입 리턴시 <?>로 리턴하는게 제일편함. 
	 //CMRespDto, Script 비교(아래)
	 //1. 클라이언트에게 응답할 때는 Script가 좋음 [UX를 높이기 위해]
	 //2. Ajax통신,Android통신 - CMRespDto 사용 [응답을 개발자가 받을대 코드로 받는 것이 더 좋음]
		 if(e.getErrorMap() == null) {
			 return Script.back(e.getMessage());
		 }else {
			 return Script.back(e.getErrorMap().toString());
		 }
     }
	
	//위에것은 스크립트로 반환, 아래것은 api(데이터 즉 오브젝트(cmrespdto)로 반환
	
	@ExceptionHandler(CustomValidationApiException.class)//RuntimeException은 매개변수로 string만 받는다.. so,customvalidationexception을 만들어서 쓴다.
	public ResponseEntity<CMRespDto<?>> vallidationApiException(CustomValidationApiException e) {
		return new ResponseEntity<>(new CMRespDto<>(-1,e.getMessage(),e.getErrorMap()), HttpStatus.BAD_REQUEST);
		//http상태코드와 보낼메세지를 함게 보낼 수 있다. Responseentity
	}
	
	@ExceptionHandler(CustomApiException.class)
	public ResponseEntity<CMRespDto<?>> ApiException(CustomApiException e) {
		return new ResponseEntity<>(new CMRespDto<>(-1,e.getMessage(),null), HttpStatus.BAD_REQUEST);
	}	
	
	 @ExceptionHandler(CustomException.class)
	 public String Exception(CustomException e) { 
			 return Script.back(e.getMessage());
		
     }

}
