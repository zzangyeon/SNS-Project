package com.cos.photogramstart.handler.aop;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.handler.ex.CustomValidationException;

@Component//RestController, Service 모든것들이 Component를 상속해서 만들어져 있음.
@Aspect
public class ValidationAdvice {//aop 공통적인 전처리를 함!
	
	@Around("execution(* com.cos.photogramstart.web.api.*Controller.*(..))")//@before @after 도 있음.
	public Object apiAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		
		//System.out.println("web api controller!-----------------------------");
		Object[] args = proceedingJoinPoint.getArgs();
		for(Object arg:args) {
			if(arg instanceof BindingResult) {
				BindingResult bindingResult = (BindingResult)arg;//다운 캐스팅
				
				if(bindingResult.hasErrors()) {
					Map<String, String> errorMap = new HashMap<String, String>();
					for(FieldError error:bindingResult.getFieldErrors()) {
						errorMap.put(error.getField(),error.getDefaultMessage());
					}
					throw new CustomValidationApiException("유효성검사 실패함",errorMap); //발생시 밑 코드는 다 무효화 됨.
				}
			}
		}
		//proceedingJoinPoint - profile 함수의 모든 곳에 접근할 수 있는 변수
		//controller 내의 함수보다 먼저 실행됨
		
		return proceedingJoinPoint.proceed();//profile() 함수가 이때 실행됨.
	}
	@Around("execution(* com.cos.photogramstart.web.*Controller.*(..))")
	public Object advice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		//System.out.println("web controller!------------------------------------");
		Object[] args = proceedingJoinPoint.getArgs();
		for(Object arg:args) {
			if(arg instanceof BindingResult) {
				BindingResult bindingResult = (BindingResult)arg;
				
				if(bindingResult.hasErrors()) {
					Map<String, String> errorMap = new HashMap<String, String>();
					for(FieldError error:bindingResult.getFieldErrors()) {
						errorMap.put(error.getField(),error.getDefaultMessage());
					}
					throw new CustomValidationException("유효성검사 실패함",errorMap);//예외 발생시 controllerexceptionhandler에 의해...?
				}
				
			}
		}
		return proceedingJoinPoint.proceed();
	}

}
