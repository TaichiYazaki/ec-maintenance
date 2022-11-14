package jp.co.example.repository;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jp.co.example.ecommerce_b.repository.UserRepository;

@SpringBootTest
class UserRepositoryTest {

	
	@Autowired
	private UserRepository userRepository;
	
	
	@Test
	void test() {
		fail("Not yet implemented");
	}
	
	
	@BeforeEach
	
	@Test
    public void test_emailCheck() {
		
	//DBに存在するメールアドレスを用意
	String email = "abc@sample.com";
	
	
	//assertThat(userRepository.emailCheck(email).isEmpty(),is(true));
	
	
	}
	
	
	//@Test
	//public void test_registerUser() {
	//	try {
	//		userRepository.registerUser(null)
	//	}
	//}

	
	
}
