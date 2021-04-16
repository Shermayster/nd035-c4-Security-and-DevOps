package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
	public static final String ITEM_NAME = "Square Widget";
	public static final String ITEM_DESC = "A widget that is square";
	private ItemController itemController;
	private ItemRepository itemRepository = mock(ItemRepository.class);

	@Before
	public void setUp() {
		itemController = new ItemController();
		TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
		Item item = new Item();
		item.setId(0L);
		item.setName(ITEM_NAME);
		item.setPrice(BigDecimal.valueOf(1.99));
		item.setDescription(ITEM_DESC);
		List<Item> itemList = new ArrayList<>();
		itemList.add(item);
		when(itemRepository.findAll()).thenReturn(itemList);
		when(itemRepository.findById(0L)).thenReturn(java.util.Optional.of(item));
		when(itemRepository.findByName("Square Widget")).thenReturn(Collections.singletonList(item));
	}

	@Test
	public void should_return_all_items() {
		ResponseEntity<List<Item>> itemListResponse = itemController.getItems();
		Assert.assertEquals(HttpStatus.OK.value(), itemListResponse.getStatusCodeValue());
		Assert.assertEquals(1, Objects.requireNonNull(itemListResponse.getBody()).size());
		Item responseItem =  itemListResponse.getBody().get(0);
		Assert.assertEquals(java.util.Optional.of(0L), java.util.Optional.of(responseItem.getId()));
	}

	@Test
	public void should_return_item_by_id() {
		ResponseEntity<Item> itemListResponse = itemController.getItemById(0L);
		Assert.assertEquals(HttpStatus.OK.value(), itemListResponse.getStatusCodeValue());
		Item responseItem = itemListResponse.getBody();
		Assert.assertNotNull(responseItem);
		Assert.assertEquals(ITEM_NAME, responseItem.getName());
		Assert.assertEquals(ITEM_DESC, responseItem.getDescription());
	}
	@Test
	public void should_return_item_by_name() {
		ResponseEntity<List<Item>> itemListResponse = itemController.getItemsByName(ITEM_NAME);
		Assert.assertEquals(HttpStatus.OK.value(), itemListResponse.getStatusCodeValue());
		Assert.assertEquals(1, Objects.requireNonNull(itemListResponse.getBody()).size());
		Item responseItem =  itemListResponse.getBody().get(0);
		Assert.assertEquals(java.util.Optional.of(0L), java.util.Optional.of(responseItem.getId()));
	}
}
