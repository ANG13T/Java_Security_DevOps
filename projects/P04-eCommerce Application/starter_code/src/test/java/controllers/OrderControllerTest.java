package controllers;

import com.example.demo.TestUtils;
import com.example.demo.controllers.OrderController;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private OrderController orderContoller;
    private OrderRepository orderRepository = mock(OrderRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);
    @Before
    public void setup(){
        orderContoller = new OrderController();
        TestUtils.injectObjects(orderContoller, "orderRepository", orderRepository);
        TestUtils.injectObjects(orderContoller, "userRepository", userRepository);
    }

    @Test
    public void submitOrder(){
        String username = "johnDoe";

        User u = new User();
        u.setUsername(username);

        Cart c = new Cart();
        c.setId((long)1);
        List<Item> itemList = new ArrayList<Item>();
        itemList.add(new Item());
        c.setTotal(BigDecimal.valueOf(32.12));
        c.setItems(itemList);

        u.setCart(c);

        when(userRepository.findByUsername(username)).thenReturn(u);

        final ResponseEntity<UserOrder> response = orderContoller.submit(username);

        assertEquals(response.getStatusCodeValue(), 200);

        UserOrder order = response.getBody();

        assertEquals(order.getItems().size(), itemList.size());
    }

    @Test
    public void getOrdersForUser(){
        User u = new User();
        u.setUsername("johnDoe");

        when(userRepository.findByUsername("johnDoe")).thenReturn(u);

        List<UserOrder> userOrders = new ArrayList<>();
        UserOrder userOrder = new UserOrder();
        userOrders.add(userOrder);

        when(orderRepository.findByUser(u)).thenReturn(userOrders);

        final ResponseEntity<List<UserOrder>> response = orderContoller.getOrdersForUser("johnDoe");

        assertEquals(response.getStatusCodeValue(), 200);

        List<UserOrder> responseUserOrders = response.getBody();

        assertNotNull(responseUserOrders);
        assertEquals(responseUserOrders.get(0), userOrders.get(0));
        assertEquals(responseUserOrders.size(), userOrders.size());

    }

    @Test
    public void userOrderHistoryNotFound(){
        User u = new User();
        u.setUsername("johnDoe246");

        final ResponseEntity<List<UserOrder>> response = orderContoller.getOrdersForUser("johnDoe246");

        assertNotNull(response);
        assertEquals(response.getStatusCodeValue(), 404);
    }
}
