package controllers;

import com.example.demo.TestUtils;
import com.example.demo.controllers.ItemController;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private UserRepository userRepository = mock(UserRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);
    private ItemController itemController;
    @Before
    public void setup(){
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "userRepository", userRepository);
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void getItemByName(){
        String itemName = "ramen";
        Item item = new Item();
        item.setName(itemName);
        List<Item> itemList = new ArrayList<Item>();
        itemList.add(item);

        when(itemRepository.findByName(itemName)).thenReturn(itemList);
        final ResponseEntity<List<Item>> res =  itemController.getItemsByName(itemName);

        assertNotNull(res);
        assertEquals(res.getStatusCodeValue(), 200);

        List<Item> resItems = res.getBody();

        assertEquals(resItems.get(0).getName(), itemName);
        assertEquals(resItems.size(), 1);
    }

    @Test
    public void itemNotFoundByName(){
        final ResponseEntity<List<Item>> res =  itemController.getItemsByName("cheese");

        assertNotNull(res);
        assertEquals(res.getStatusCodeValue(), 404);
    }

    @Test
    public void getItemById(){
        Long itemID = (long)1;
        Item item = new Item();
        item.setName("cookie");

        when(itemRepository.findById(itemID)).thenReturn(java.util.Optional.of(item));

        final ResponseEntity<Item> res =  itemController.getItemById(itemID);

        assertNotNull(res);
        assertEquals(res.getStatusCodeValue(), 200);
        Item resItem = res.getBody();

        assertEquals(resItem.getName(), "cookie");
    }

    @Test
    public void itemNotFoundById(){
        Long itemID = (long)1;
        Item item = new Item();
        item.setName("cheeto puffs");

        final ResponseEntity<Item> res =  itemController.getItemById(itemID);

        assertNotNull(res);
        assertEquals(res.getStatusCodeValue(), 404);
    }
}
