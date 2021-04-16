package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserControllerTest {
	private  UserController userController;
	private UserRepository userRepository = Mockito.mock(UserRepository.class);
	private CartRepository cartRepository = Mockito.mock(CartRepository.class);
	private BCryptPasswordEncoder bCryptPasswordEncoder = Mockito.mock(BCryptPasswordEncoder.class);

	@Before
	public void setUp() {
		userController = new UserController();
		TestUtils.injectObjects(userController, "userRepository", userRepository);
		TestUtils.injectObjects(userController, "cartRepository", cartRepository);
		TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
	}

	@Test
	public void should_return_user_by_id() {
		User user = mockUser();
		when(userRepository.findById(any(Long.class))).thenReturn(java.util.Optional.of(user));
		ResponseEntity<User> response = userController.findById(0L);
		Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
		User responseUser = response.getBody();
		Assert.assertNotNull(responseUser);
	}

	@Test
	public void should_return_user_by_name() {
		User user = mockUser();
		when(userRepository.findByUsername(any(String.class))).thenReturn(user);
		ResponseEntity<User> response = userController.findByUserName("testUser");
		Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
		User responseUser = response.getBody();
		Assert.assertNotNull(responseUser);
	}

	@Test
	public void should_be_able_to_create_user() {
		CreateUserRequest createUserRequest = new CreateUserRequest();
		createUserRequest.setUsername("testUser");
		createUserRequest.setPassword("testPassword");
		createUserRequest.setConfirmPassword("testPassword");
		ResponseEntity<User> response = userController.createUser(createUserRequest);
		Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
		User responseUser = response.getBody();
		Assert.assertNotNull(responseUser);
	}


	private User mockUser() {
		Cart cart = new Cart();
		User user = new User();
		user.setId(0);
		user.setUsername("testUser");
		user.setPassword("testPassword");
		user.setCart(cart);
		return user;
	}

}
