package com.example.demo.controllers;

import java.util.List;
import java.util.Optional;

import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.loggers.CSVLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;

@RestController
@RequestMapping("/api/item")
public class ItemController {

	@Autowired
	private CSVLogger csvLogger;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private UserRepository userRepository;
	
	@GetMapping
	public ResponseEntity<List<Item>> getItems() {
		csvLogger.logToCsv(null,"getItems", null, null, "Successfully fetched items", "success");
		return ResponseEntity.ok(itemRepository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Item> getItemById(@PathVariable Long id) {
		Optional<Item> item = itemRepository.findById(id);
		csvLogger.logToCsv(null,"getItemById", null, item.get().getId(), "Successfully fetched item by id " + id, "success");
		return ResponseEntity.of(item);
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<List<Item>> getItemsByName(@PathVariable String name) {
		List<Item> items = itemRepository.findByName(name);
		csvLogger.logToCsv(null,"getItemsByName", null, null, "Successfully fetched items by name " + name, "success");
		return items == null || items.isEmpty() ? ResponseEntity.notFound().build()
				: ResponseEntity.ok(items);
			
	}
	
}
