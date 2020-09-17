package com.example.demo.controllers;

import java.util.Optional;
import java.util.stream.IntStream;

import com.example.demo.logging.CsvLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;

@RestController
@RequestMapping("/api/cart")
public class CartController {

	@Autowired
	private CsvLogger csvLogger;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private ItemRepository itemRepository;
	
	@PostMapping("/addToCart")
	public ResponseEntity<Cart> addTocart(@RequestBody ModifyCartRequest request) {
		User user = userRepository.findByUsername(request.getUsername());
		if(user == null) {
			System.out.println("logger");
			System.out.println(csvLogger);
			csvLogger.logToCsv(user.getId(),"addTocart", "items", request.getItemId(), "Couldn't find user" , "notFound");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Optional<Item> item = itemRepository.findById(request.getItemId());
		if(!item.isPresent()) {
			csvLogger.logToCsv(user.getId(),"addTocart", null, null, "Could not find specific item" + request.getItemId() , "notFound");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Cart cart = user.getCart();
		System.out.println("Adding to cart....");
		IntStream.range(0, request.getQuantity())
			.forEach(i -> {
				cart.addItem(item.get());
				csvLogger.logToCsv(user.getId(),"addTocart", item.get().toString(), item.get().getId(), "Saved item to cart " + item.get().getName(), "success");
			});
		cartRepository.save(cart);
		return ResponseEntity.ok(cart);
	}
	
	@PostMapping("/removeFromCart")
	public ResponseEntity<Cart> removeFromcart(@RequestBody ModifyCartRequest request) {
		User user = userRepository.findByUsername(request.getUsername());
		if(user == null) {
			csvLogger.logToCsv(user.getId(),"removeFromcart", null, null, "Couldn't find user  " , "notFound");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Optional<Item> item = itemRepository.findById(request.getItemId());
		if(!item.isPresent()) {
			csvLogger.logToCsv(user.getId(),"removeFromcart", null, null, "Could not find specific item" + request.getItemId() , "notFound");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Cart cart = user.getCart();
		IntStream.range(0, request.getQuantity())
			.forEach(i -> {
				cart.removeItem(item.get());
				csvLogger.logToCsv(user.getId(),"removeFromcart", item.get().toString(), item.get().getId(), "Removed item from cart " + item.get().getName(), "success");
			});
		cartRepository.save(cart);

		return ResponseEntity.ok(cart);
	}
		
}
