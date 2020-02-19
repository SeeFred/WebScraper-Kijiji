package logic;

import common.TomcatStartUp;
import entity.Category;
import entity.Image;
import entity.Item;
import org.junit.jupiter.api.*;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ItemLogicTest {
    private ItemLogic logic;
    private ImageLogic imageLogic;
    private CategoryLogic categoryLogic;
    private Image image;
    private Category category;
    private Map<String, String[]> sampleMap;

    @BeforeAll
    final static void setUpBeforeClass() throws Exception {
        TomcatStartUp.createTomcat();
    }

    @AfterAll
    final static void tearDownAfterClass() throws Exception {
        TomcatStartUp.stopAndDestroyTomcat();
    }

    @BeforeEach
    final void setUp() throws Exception {
        logic = new ItemLogic();
        imageLogic = new ImageLogic();
        categoryLogic = new CategoryLogic();

        Map<String, String[]> imageMap = new HashMap<>();
        imageMap.put(ImageLogic.URL, new String[]{"https://www.kijiji.ca/b-jobs"});
        imageMap.put(ImageLogic.NAME, new String[]{"fang"});
        imageMap.put(ImageLogic.PATH, new String[]{"/temp/download"});
        image = imageLogic.createEntity(imageMap);
        imageLogic.add(image);

        Map<String, String[]> categoryMap = new HashMap<>();
        categoryMap.put(CategoryLogic.TITLE, new String[]{"Junit 5 Test"});
        categoryMap.put(CategoryLogic.URL, new String[]{"junit"});
        category = categoryLogic.createEntity(categoryMap);
        categoryLogic.add(category);

        sampleMap = new HashMap<>();
        sampleMap.put(ItemLogic.DESCRIPTION, new String[]{"Junit 5 Test"});
        sampleMap.put(ItemLogic.IMAGE_ID, new String[]{image.getId().toString()});
        sampleMap.put(ItemLogic.CATEGORY_ID, new String[]{category.getId().toString()});
        sampleMap.put(ItemLogic.LOCATION, new String[]{"/temp/item"});
        sampleMap.put(ItemLogic.PRICE, new String[]{"$30"});
        sampleMap.put(ItemLogic.TITLE, new String[]{"junit5"});
        sampleMap.put(ItemLogic.DATE, new String[]{"02/02/2020"});
        sampleMap.put(ItemLogic.URL, new String[]{"https://www.google.com"});
        sampleMap.put(ItemLogic.ID, new String[]{"2"});
    }

    @AfterEach
    final void tearDown() {
        imageLogic.delete(image);
        categoryLogic.delete(category);
    }

    @Test
    void testGetAll() {
        //get all the item from the DB
        List<Item> list = logic.getAll();
        //store the size of list/ this way we know how many item exits in DB
        int originalSize = list.size();
        //create a new item and save it so we can delete later
        Item testItem = logic.createEntity(sampleMap);
        //add the newly created item to DB
        logic.add(testItem);
        //get all the item again
        list = logic.getAll();
        //the new size of item must be 1 larger than original size
        assertEquals(originalSize + 1, list.size());
        //delete the new item, so DB is reverted back to it original form
        logic.delete(testItem);
        //get all item again
        list = logic.getAll();
        //the new size of item must be same as original size
        assertEquals(originalSize, list.size());

    }

    @Test
    void testGetWithId() {
        Item item = logic.createEntity(sampleMap);
        logic.add(item);
        List<Item> list = logic.getAll();
        Item testItem = list.get(0);
        //using the id of test image get another image from logic
        Item returnedItem = logic.getWithId(testItem.getId());
        //the two accounts (testImage and returnedImage) must be the same
        //assert all field to guarantee they are the same
        assertEquals(testItem.getId(), returnedItem.getId());
        assertEquals(testItem.getUrl(), returnedItem.getUrl());
        assertEquals(testItem.getPrice(), returnedItem.getPrice());
        assertEquals(testItem.getTitle(), returnedItem.getTitle());
        assertEquals(testItem.getDate(), returnedItem.getDate());
        assertEquals(testItem.getLocation(), returnedItem.getLocation());
        assertEquals(testItem.getDescription(), returnedItem.getDescription());
        assertEquals(testItem.getCategory(), returnedItem.getCategory());
        assertEquals(testItem.getImage(), returnedItem.getImage());

        logic.delete(item);
    }

    @Test
    void testCreateEntity() {
        List<Item> list = logic.getAll();
        int originalSize = list.size();
        Item testItems = logic.createEntity(sampleMap);
        logic.add(testItems);
        list = logic.getAll();
        assertEquals(originalSize + 1, list.size());
        logic.delete(testItems);
        list = logic.getAll();
        assertEquals(originalSize, list.size());
    }

    @Test
    void testGetColumnNames() {
        List<String> itemList = logic.getColumnNames();
        List<String> itemListExpected = Arrays.asList("Description", "Category Id", "Image Id", "Location", "Price", "Title", "Date", "URL", "ID");
        for (int i = 0; i < itemListExpected.size(); i++) {
            assertEquals(itemListExpected.get(i), itemListExpected.get(i));
        }
    }

    @Test
    void testGetColumnCodes() {
        List<String> itemList = logic.getColumnNames();
        List<String> itemListExpected = Arrays.asList(ItemLogic.DESCRIPTION, ItemLogic.CATEGORY_ID, ItemLogic.IMAGE_ID, ItemLogic.LOCATION, ItemLogic.PRICE, ItemLogic.TITLE, ItemLogic.DATE, ItemLogic.URL, ItemLogic.ID);
        for (int i = 0; i < itemListExpected.size(); i++) {
            assertEquals(itemListExpected.get(i), itemListExpected.get(i));
        }
    }

    @Test
    void testExtractDataAsList() throws ParseException {
        Item item = logic.createEntity(sampleMap);
        List<?> list = logic.extractDataAsList(item);
        assertEquals(sampleMap.get(ItemLogic.ID)[0], list.get(0).toString());
        assertEquals(sampleMap.get(ItemLogic.URL)[0], list.get(1));
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        assertEquals(numberFormat.parse(sampleMap.get(ItemLogic.PRICE)[0]).toString(), list.get(2).toString());
        assertEquals(sampleMap.get(ItemLogic.TITLE)[0], list.get(3));
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        assertEquals(format.parse(sampleMap.get(ItemLogic.DATE)[0]), list.get(4));
        assertEquals(sampleMap.get(ItemLogic.LOCATION)[0], list.get(5));
        assertEquals(sampleMap.get(ItemLogic.DESCRIPTION)[0], list.get(6));
        assertEquals(sampleMap.get(ItemLogic.CATEGORY_ID)[0], list.get(7).toString());
        assertEquals(sampleMap.get(ItemLogic.IMAGE_ID)[0], list.get(8).toString());
    }

    @Test
    void testGetWithCategory() {
        Item item = logic.createEntity(sampleMap);
        item.setImage(image);
        item.setCategory(category);
        logic.add(item);
        List<Item> list = logic.getWithCategory(item.getCategory().getId());
        assertTrue(list.size() >= 1);
        for (Item itemloop : list) {
            assertEquals(itemloop.getCategory().getId(), item.getCategory().getId());
        }
        logic.delete(item);

    }

    @Test
    void testGetWithPrice() {
        Item item = logic.createEntity(sampleMap);
        item.setImage(image);
        item.setCategory(category);
        logic.add(item);
        List<Item> list = logic.getWithPrice(item.getPrice());
        assertTrue(list.size() >= 1);
        for (Item itemloop : list) {
            assertEquals(itemloop.getPrice().compareTo(item.getPrice()), 0);
        }
        logic.delete(item);
    }

    @Test
    void testGetWithTitle() {
        Item item = logic.createEntity(sampleMap);
        item.setImage(image);
        item.setCategory(category);
        logic.add(item);
        List<Item> list = logic.getWithTitle(item.getTitle());
        assertTrue(list.size() >= 1);
        for (Item itemloop : list) {
            assertEquals(itemloop.getTitle(), item.getTitle());
        }
        logic.delete(item);
    }

    @Test
    void testGetWithDate() {
        Item item = logic.createEntity(sampleMap);
        item.setImage(image);
        item.setCategory(category);
        logic.add(item);
        List<Item> list = logic.getWithDate(item.getDate());
        assertTrue(list.size() >= 1);
        for (Item itemloop : list) {
            assertEquals(itemloop.getDate(), item.getDate());
        }
        logic.delete(item);
    }

    @Test
    void testGetWithLocation() {
        Item item = logic.createEntity(sampleMap);
        item.setImage(image);
        item.setCategory(category);
        logic.add(item);
        List<Item> list = logic.getWithLocation(item.getLocation());
        assertTrue(list.size() >= 1);
        for (Item itemloop : list) {
            assertEquals(itemloop.getLocation(), item.getLocation());
        }
        logic.delete(item);
    }

    @Test
    void testGetWithDescription() {
        Item item = logic.createEntity(sampleMap);
        item.setImage(image);
        item.setCategory(category);
        logic.add(item);
        List<Item> list = logic.getWithDescription(item.getDescription());
        assertTrue(list.size() >= 1);
        for (Item itemloop : list) {
            assertEquals(itemloop.getDescription(), item.getDescription());
        }
        logic.delete(item);
    }

    @Test
    void testGetWithUrl() {
        Item item = logic.createEntity(sampleMap);
        item.setImage(image);
        item.setCategory(category);
        logic.add(item);
        Item testItem = logic.getWithUrl(item.getUrl());
        assertEquals(item.getUrl(),testItem.getUrl());
        logic.delete(item);
    }
}