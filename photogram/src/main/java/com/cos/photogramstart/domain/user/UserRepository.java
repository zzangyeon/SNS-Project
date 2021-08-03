package com.cos.photogramstart.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

//어노테이션이 없어도 IOC등록이 자동으로 된다. jparepository를 상속하였기 때문.

public interface UserRepository extends JpaRepository<User, Integer>{
	//JPA query creation from method names(query method)
	
	User findByUsername(String username);
}
