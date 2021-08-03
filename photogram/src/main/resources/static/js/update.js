// (1) 회원정보 수정
function update(userId,event) {
	
	event.preventDefault();//폼태그의 action을 막는것!!
	let data = $("#profileUpdate").serialize();
	console.log(data);
	
	$.ajax({
		type:"put",
		url:'/api/user/'+userId,
		data:data,
		contentType:"application/x-www-form-urlencoded; charset=utf-8",
		dataType:"json"
	}).done(res=>{//json으로 파싱해서 res변수로 받는다.  //HttpStatus 상태코드 200번대면 done  //브라우저끼리 ajax통신할때는 http상태코드를 같이 보내주는것이 좋다.
		console.log("성공",res);
		location.href = '/user/'+userId;
	}).fail(error=>{//HttpStatus 상태코드 200번대가 아니면 error
		if(error.responseJSON.data==null){
			alert(error.responseJSON.message);   //errorMap 있을떄, 없을때를 분기해 놓은것. 유효성검사할때 프론트,전처리,후처리중에 전처리 후처리할때 errorMap여부가 다르기 때문에 이렇게 분기로 받아준다.
		}else{
			alert(JSON.stringify(error.responseJSON.data));//JSON.stringify() - js object를 문자열로 나타내줌.
		}
	});
}