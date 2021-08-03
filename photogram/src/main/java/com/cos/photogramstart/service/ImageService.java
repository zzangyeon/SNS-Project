package com.cos.photogramstart.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.image.ImageRepository;
import com.cos.photogramstart.web.dto.image.ImageUploadDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ImageService {
	
	private final ImageRepository imageRepository;
	
	@Value("${file.path}")//application.yml 의 file path를 불러오는것!! 전역적으로 쓸 수 있기 때문에 좋다! 문법 알자!
	private String uploadFolder;
	
	@Transactional//서비스단에서 db변형할 시 꼭 이 transactioanl 걸어줘야함
	public void 사진업로드(ImageUploadDto imageUploadDto,PrincipalDetails principalDetails) {
		UUID uuid = UUID.randomUUID();//UUID -네트워크 상에서 고유성과 유일성이 보장되는 id를 만들기위한 표준 규약(universally unique identifier)
		String imagefileName = uuid+"_"+imageUploadDto.getFile().getOriginalFilename();//1.jpg
		//System.out.println("이미지파일이름:"+imagefileName);
		
		Path imageFilePath = Paths.get(uploadFolder+imagefileName);
		
		//통신, I/O(하드디스크를 읽거나 뭐 할때)시에는 -> 예외가 발생할 수 있다! so,예외처리해줘야함
		try {
			Files.write(imageFilePath, imageUploadDto.getFile().getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//image 테이블에 저장
		Image image = imageUploadDto.toEntity(imagefileName, principalDetails.getUser());
		imageRepository.save(image);
 		//System.out.println(imageEntity); //주의할 것 !!!! 객체를 sysout할떄 tostring이 발동하고. 객체내부의 멤버변수들을 출력하는데
 		//image객체 내부의 user객체가 있어서 user객치를 또 출력하려고 멤버변수 List<Image> image에서 또 image객체를 호출하여 출력하고 
 		//무한 참조가 일어난다!! 그래서 toStirng()함수를 override해서 image출력시 User객체를 빼고!! 출력하도록 수정한다.1!!
 		//so JPA 사용 시에는 sysout 객체 를 조심해서 써야 한다!
 		//controller에서 객체를 return할때 무한참조를 또 조심해야한다!!
	}
	
	@Transactional(readOnly = true)  //Transactional 붙이면 session을 컨트롤러 단까지 끌고 오게 된다.
	public List<Image> 이미지스토리(int principalId,Pageable pageable){
		List<Image> images = imageRepository.mStory(principalId,pageable);
		
		//2(cos)로 로그인
		//images에 좋아요 상태 담기
		images.forEach((image)->{
			
			image.setLikeCount(image.getLikes().size());
			
			image.getLikes().forEach((like)->{
				if(like.getUser().getId() == principalId) {//해당 이미지에 좋아요한 사람들을 찾아서 현재 로그인한 사람이 좋아요 한 것인지 비교.
					image.setLikeState(true);			
				}
			});
			
		});
		
		return images;
	}
	
	@Transactional(readOnly = true)
	public List<Image> 인기사진() {
		return imageRepository.mPopular();
	}
	
	
}
