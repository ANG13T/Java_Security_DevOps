package controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);
    @Before
    public void setup(){
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }
    @Test
    public void create_user_happy_path() throws Exception {
        when(bCryptPasswordEncoder.encode("pass2468")).thenReturn("thisIsHashed");

        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("janeDoe");
        r.setPassword("pass2468");
        r.setConfirmPassword("pass2468");

        final ResponseEntity<User> response =  userController.createUser(r);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();
        assertNotNull(u);
        assertEquals(u.getId(), 0);
        assertEquals(u.getUsername(), "janeDoe");
        assertEquals(u.getPassword(), "thisIsHashed");
    }

    @Test
    public void find_by_id(){
        User user = new User();
        user.setUsername("mark");
        user.setPassword("dadada");
        when(userRepository.findById(0L)).thenReturn(java.util.Optional.of(user));
        ResponseEntity<User> userResponseEntity = userController.findById(0L);
        assertTrue(userResponseEntity.getStatusCode().is2xxSuccessful());
        assertEquals("mark", userResponseEntity.getBody().getUsername());
        assertEquals("dadada", userResponseEntity.getBody().getPassword());
    }
    @Test
    public void find_by_username(){
        User user = new User();
        user.setUsername("mark");
        user.setPassword("dadada");
        when(userRepository.findByUsername("mark")).thenReturn(user);
        ResponseEntity<User> userResponseEntity = userController.findByUserName("mark");
        assertTrue(userResponseEntity.getStatusCode().is2xxSuccessful());
        assertEquals("mark", userResponseEntity.getBody().getUsername());
        assertEquals("dadada", userResponseEntity.getBody().getPassword());
    }

    @Test
    public void password_length_error(){
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("johnDoe");
        r.setPassword("123");
        r.setConfirmPassword("123");

        final ResponseEntity<User> response =  userController.createUser(r);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());

    }

    @Test
    public void passwordNotMatchConfirm(){
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("johnDoe");
        r.setPassword("password248");
        r.setConfirmPassword("password1234");

        final ResponseEntity<User> response =  userController.createUser(r);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }


}
