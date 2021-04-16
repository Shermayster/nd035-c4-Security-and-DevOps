package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

	private CartController cartController;
	private UserRepository userRepository = mock(UserRepository.class);
	private CartRepository cartRepository = mock(CartRepository.class);
	private ItemRepository itemRepository = mock(ItemRepository.class);

	@Before public void setUp() {
		cartController = new CartController();
		TestUtils.injectObjects(cartController, "userRepository", userRepository);
		TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
		TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
	}

	@Test
	public void should_be_able_add_to_cart() {
		int QUANTITY = 3;
		User testUser = mockUser();
		Item item = mockItem();
		ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
		modifyCartRequest.setUsername(testUser.getUsername());
		modifyCartRequest.setItemId(item.getId());
		modifyCartRequest.setQuantity(QUANTITY);
		ResponseEntity<Cart> cartResponseEntity = cartController.addTocart(modifyCartRequest);
		Assert.assertEquals(HttpStatus.OK, cartResponseEntity.getStatusCode());
		Cart responseCart = cartResponseEntity.getBody();
		Assert.assertNotNull(responseCart);
		Item respItem = responseCart.getItems().get(0);
		Assert.assertEquals(respItem.getId(), item.getId());
		Assert.assertEquals(respItem.getPrice(), item.getPrice());
		Assert.assertEquals(responseCart.getTotal(), item.getPrice().multiply(BigDecimal.valueOf(QUANTITY)));
	}

	@Test
	public void should_return_error_on_empty_user() {
		mockUserNotFount();
		ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
		ResponseEntity<Cart> cartResponseEntity = cartController.addTocart(modifyCartRequest);
		Assert.assertEquals(HttpStatus.NOT_FOUND, cartResponseEntity.getStatusCode());
	}

	@Test
	public void should_return_error_on_empty_item() {
		mockUser();
		mockEmptyItem();
		ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
		ResponseEntity<Cart> cartResponseEntity = cartController.addTocart(modifyCartRequest);
		Assert.assertEquals(HttpStatus.NOT_FOUND, cartResponseEntity.getStatusCode());
	}

	@Test
	public void should_be_able_to_remove_item() {
		int initialQuantity = 3;
		int removeQuantity = 2;
		int remainingQuantity = 1;
		User testUser = mockUser();
		Item item = mockItem();
		ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
		modifyCartRequest.setUsername(testUser.getUsername());
		modifyCartRequest.setItemId(item.getId());
		modifyCartRequest.setQuantity(initialQuantity);
		cartController.addTocart(modifyCartRequest);
		modifyCartRequest.setQuantity(removeQuantity);
		ResponseEntity<Cart> cartResponseEntity = cartController.removeFromcart(modifyCartRequest);
		Assert.assertEquals(HttpStatus.OK, cartResponseEntity.getStatusCode());
		Cart responseCart = cartResponseEntity.getBody();
		Assert.assertNotNull(responseCart);
		Item respItem = responseCart.getItems().get(0);
		Assert.assertEquals(respItem.getId(), item.getId());
		Assert.assertEquals(respItem.getPrice(), item.getPrice());
		Assert.assertEquals(responseCart.getTotal(), item.getPrice().multiply(BigDecimal.valueOf(remainingQuantity)));
	}


	private User mockUser() {
		Cart cart = new Cart();
		User user = new User();
		user.setId(0);
		user.setUsername("testUser");
		user.setPassword("testPassword");
		user.setCart(cart);
		when(userRepository.findByUsername("testUser")).thenReturn(user);
		return user;
	}

	private void mockUserNotFount() {
		when(userRepository.findByUsername("testUser")).thenReturn(null);
	}

	private Item mockItem() {
		Item item = new Item();
		item.setId(0L);
		item.setName("Square Widget");
		item.setPrice(BigDecimal.valueOf(1.99));
		item.setDescription("A widget that is square");
		when(itemRepository.findById(0L)).thenReturn(java.util.Optional.of(item));
		when(itemRepository.findByName("Square Widget")).thenReturn(Collections.singletonList(item));
		return item;
	}

	private void mockEmptyItem() {
		when(itemRepository.findById(0L)).thenReturn(java.util.Optional.empty());
		when(itemRepository.findByName("Square Widget")).thenReturn(Collections.singletonList(null));
	}
}
