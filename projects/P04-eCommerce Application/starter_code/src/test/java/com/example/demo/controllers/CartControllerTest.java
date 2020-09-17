package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.controllers.CartController;
import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    private CartController cartController;
    private CartRepository cartRepository = mock(CartRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);
    @Before
    public void setup(){
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
    }

    @Test
    public void userNotFoundError(){
        ModifyCartRequest cartRequest = new ModifyCartRequest();
        final ResponseEntity<Cart> res = cartController.addTocart(cartRequest);
        assertEquals(res.getStatusCodeValue(), 404);
    }

    @Test
    public void itemNotFoundError(){
        String username = "john";
        User user = new User();
        user.setUsername(username);
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        when(userRepository.findByUsername(username)).thenReturn(user);

        final ResponseEntity<Cart> res = cartController.addTocart(modifyCartRequest);

        assertEquals(res.getStatusCodeValue(), 404);
    }

    @Test
    public void addItemToCart(){
        String username = "john";
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(2);
        modifyCartRequest.setUsername(username);

        User user = new User();
        user.setUsername(username);
        Cart cart = new Cart();
        cart.setItems(new ArrayList<>());
        user.setCart(cart);

        when(userRepository.findByUsername(username)).thenReturn(user);

        Item item = new Item();
        item.setName("cream");
        item.setId((long)2);
        item.setPrice(BigDecimal.valueOf(5));
        Optional<Item> optItems = Optional.of(item);

        when(itemRepository.findById(modifyCartRequest.getItemId())).thenReturn(optItems);

        final ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        assertNotNull(response);

        Cart resCart = response.getBody();

        assertEquals(resCart.getItems().size(), 2);
    }




}
