package logic;

import common.TomcatStartUp;
import entity.Category;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CategoryLogicTest {
    private CategoryLogic logic;
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
        logic = new CategoryLogic();
        /*HashMap implements interface Map. Map is generic, it has two parameters, first is the Key (in our case String) and second is Value (in our case String[])*/
        sampleMap = new HashMap<>();
        /*Map stores date based on the idea of dictionaries. Each value is associated with a key. Key can be used to get a value very quickly*/
        sampleMap.put(CategoryLogic.TITLE, new String[]{"Junit 5 Test"});
        /*Map::put is used to store a key and a value inside of a map and Map::get is used to retrieve a value using a key.*/
        sampleMap.put(CategoryLogic.URL, new String[]{"junit"});
    }

    @AfterEach
    final void tearDown() throws Exception {
    }

    @Test
    void testGetAll() {
        //get all the accounts from the DB
        List<Category> list = logic.getAll();
        //store the size of list/ this way we know how many accounts exits in DB
        int originalSize = list.size();
//        logic.delete(list.get(0));
        Category testCategory = logic.createEntity(sampleMap);
        //add the newly created account to DB
        logic.add(testCategory);

        list = logic.getAll();
        //the new size of accounts must be 1 larger than original size
        assertEquals(originalSize + 1, list.size());
        logic.delete(testCategory);

        //get all accounts again
        list = logic.getAll();
        //the new size of accounts must be same as original size
        assertEquals(originalSize, list.size());

    }

    @Test
    void testGetWithId() {
        //create a new category and save it so we can delete later
        Category category = logic.createEntity(sampleMap);
        //add the newly created category to DB
        logic.add(category);
        List<Category> list = logic.getAll();
        //use the first image in the list as test image
        Category testCategory = list.get(0);
        //using the id of test image get another image from logic
        Category returnedCategory = logic.getWithId(testCategory.getId());
        //the two accounts (testImage and returnedImage) must be the same
        //assert all field to guarantee they are the same
        assertEquals(testCategory.getId(), returnedCategory.getId());
        assertEquals(testCategory.getUrl(), returnedCategory.getUrl());
        assertEquals(testCategory.getTitle(), returnedCategory.getTitle());
        logic.delete(category);

    }

    @Test
    void testGetWithUrl() {
        Category category = logic.createEntity(sampleMap);
        logic.add(category);

        Category returnedCategory = logic.getWithUrl(sampleMap.get(CategoryLogic.URL)[0]);

        assertEquals(sampleMap.get(CategoryLogic.URL)[0], returnedCategory.getUrl());

        logic.delete(category);
    }

    @Test
    void testGetWithTitle() {
        Category category = logic.createEntity(sampleMap);
        logic.add(category);

        Category returnedCategory = logic.getWithTitle(sampleMap.get(CategoryLogic.TITLE)[0]);

        assertEquals(sampleMap.get(CategoryLogic.TITLE)[0], returnedCategory.getTitle());

        logic.delete(category);
    }

    @Test
    void testCreateEntity() {
        List<Category> list = logic.getAll();
        int originalSize = list.size();
        Category testCategory = logic.createEntity(sampleMap);
        logic.add(testCategory);
        list = logic.getAll();
        assertEquals(originalSize + 1, list.size());
        logic.delete(testCategory);
        list = logic.getAll();
        assertEquals(originalSize, list.size());
    }

    @Test
    void testGetColumnNames() {
        List<String> categoryList = logic.getColumnNames();
        assertEquals(categoryList.get(0), "ID");
        assertEquals(categoryList.get(1), "URL");
        assertEquals(categoryList.get(2), "Title");
    }

    @Test
    void testGetColumnCodes() {
        List<String> categoryList = logic.getColumnCodes();
        List<String> categoryListExpected = Arrays.asList(CategoryLogic.ID, CategoryLogic.URL, CategoryLogic.TITLE);
        for (int i = 0; i < categoryListExpected.size(); i++) {
            assertEquals(categoryList.get(i), categoryListExpected.get(i));
        }
    }

    @Test
    void testExtractDataAsListWithConstructorNoParameter() {
        Category category = new Category();
        List<?> categoryList = logic.extractDataAsList(category);
        assertNull(categoryList.get(0));
        assertNull(categoryList.get(1));
        assertNull(categoryList.get(2));
    }

    @Test
    void testExtractDataAsListWithConstructorWithParameter() {
        Category category = new Category(1, "https://www.kijiji.ca/b-jobs", "jobs");
        List<?> categoryList = logic.extractDataAsList(category);
        assertEquals(1, categoryList.get(0));
        assertEquals("https://www.kijiji.ca/b-jobs", categoryList.get(1));
        assertEquals("jobs", categoryList.get(2));
    }
}