package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
	public static final String USERNAME = "testUser";
	public static final String ITEM_NAME = "Square Widget";
	private OrderController orderController;
	private UserRepository userRepository = Mockito.mock(UserRepository.class);
	private OrderRepository orderRepository = Mockito.mock(OrderRepository.class);

	@Before
	public void setUp() {
		orderController = new OrderController();
		Cart cart = new Cart();
		User user = new User();
		user.setId(0);
		user.setUsername(USERNAME);
		user.setPassword("testPassword");
		user.setCart(cart);
		when(userRepository.findByUsername(USERNAME)).thenReturn(user);
		List<UserOrder> orderList = new ArrayList<>();
		Item item = createItem();
		cart.addItem(item);
		orderList.add(UserOrder.createFromCart(cart));
		when(orderRepository.findByUser(any(User.class))).thenReturn(orderList);

		TestUtils.injectObjects(orderController, "userRepository", userRepository);
		TestUtils.injectObjects(orderController, "orderRepository",orderRepository);

	}


	@Test
	public void should_be_able_to_submit_cart() {
		ResponseEntity<UserOrder> userOrderResponse = orderController.submit(USERNAME);
		Assert.assertEquals(HttpStatus.OK.value(), userOrderResponse.getStatusCodeValue());
		UserOrder userOrder = userOrderResponse.getBody();
		Assert.assertNotNull(userOrder);
		Assert.assertEquals(1, userOrder.getItems().size());
		Item item = createItem();
		Item responseItem = userOrder.getItems().get(0);
		Assert.assertEquals(item.getName(), responseItem.getName());
	}

	@Test
	public void should_be_able_to_get_history() {
		ResponseEntity<List<UserOrder>> userOrderResponse = orderController.getOrdersForUser(USERNAME);
		Assert.assertEquals(HttpStatus.OK.value(), userOrderResponse.getStatusCodeValue());
		List<UserOrder> userOrderList = userOrderResponse.getBody();
		Assert.assertNotNull(userOrderList);
		Assert.assertEquals(1, userOrderList.size());
	}

	private Item createItem() {
		Item item = new Item();
		item.setId(0L);
		item.setName(ITEM_NAME);
		item.setPrice(BigDecimal.valueOf(1.99));
		item.setDescription("A widget that is square");
		return item;
	}
}
