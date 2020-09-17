package com.example.demo.controllers;

import java.util.List;

import com.example.demo.logging.CsvLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;

@RestController
@RequestMapping("/api/order")
public class OrderController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private CsvLogger csvLogger;
	
	
	@PostMapping("/submit/{username}")
	public ResponseEntity<UserOrder> submit(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		if(user == null) {
			csvLogger.logToCsv(null,"submit", null, null, "Couldn't find user with username  " + username , "notFound");
			return ResponseEntity.notFound().build();
		}
		UserOrder order = UserOrder.createFromCart(user.getCart());
		orderRepository.save(order);
		csvLogger.logToCsv(order.getId(),"submit", null, null, "Successfully submitted order" + order.getId() , "success");
		return ResponseEntity.ok(order);
	}
	
	@GetMapping("/history/{username}")
	public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		if(user == null) {
			csvLogger.logToCsv(null,"getOrdersForUser", null, null, "Couldn't get orders for  " + username , "notFound");
			return ResponseEntity.notFound().build();
		}
		csvLogger.logToCsv(user.getId(),"getOrdersForUser", null, null, "Successfully got orders for " + username , "success");
		return ResponseEntity.ok(orderRepository.findByUser(user));
	}
}
