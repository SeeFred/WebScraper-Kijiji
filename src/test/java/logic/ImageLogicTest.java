package logic;

import common.TomcatStartUp;
import entity.Account;
import entity.Image;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ImageLogicTest {
    private ImageLogic logic;
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
        logic = new ImageLogic();
        /*HashMap implements interface Map. Map is generic, it has two parameters, first is the Key (in our case String) and second is Value (in our case String[])*/
        sampleMap = new HashMap<>();
        /*Map stores date based on the idea of dictionaries. Each value is associated with a key. Key can be used to get a value very quickly*/
//        sampleMap.put(ImageLogic.ID, new String[]{"5"});
        /*Map::put is used to store a key and a value inside of a map and Map::get is used to retrieve a value using a key.*/
        sampleMap.put(ImageLogic.URL, new String[]{"https://www.kijiji.ca/b-jobs"});
        /*In this case we are using static values stored in AccoundLogic which represent general names for Account Columns in DB to store values in Map*/
        sampleMap.put(ImageLogic.NAME, new String[]{"fang"});
        /*This account has Display Name: "Junit 5 Test", User: "junit", and Password: "junit5"*/
        sampleMap.put(ImageLogic.PATH, new String[]{"/temp/download"});

    }

    @AfterEach
    final void tearDown() {
    }

    @Test
    void testGetAll() {
        //get all the image from the DB
        List<Image> list = logic.getAll();
        //store the size of list/ this way we know how many accounts exits in DB
        int originalSize = list.size();

        //create a new Image and save it so we can delete later
        Image testImage = logic.createEntity(sampleMap);
        //add the newly created image to DB
        logic.add(testImage);

        //get all the image again
        list = logic.getAll();
        //the new size of images must be 1 larger than original size
        assertEquals(originalSize + 1, list.size());

        //delete the new image, so DB is reverted back to it original form
        logic.delete(testImage);

        //get all image again
        list = logic.getAll();
        //the new size of image must be same as original size
        assertEquals(originalSize, list.size());
    }

    @Test
    void testGetWithId() {
        //create a new Image and save it so we can delete later
        Image image = logic.createEntity(sampleMap);
        //add the newly created image to DB
        logic.add(image);
        List<Image> list = logic.getAll();
        //use the first image in the list as test image
        Image testImage = list.get(0);
        //using the id of test image get another image from logic
        Image returnedImage = logic.getWithId(testImage.getId());
        //the two accounts (testImage and returnedImage) must be the same
        //assert all field to guarantee they are the same
        assertEquals(testImage.getId(), returnedImage.getId());
        assertEquals(testImage.getUrl(), returnedImage.getUrl());
        assertEquals(testImage.getPath(), returnedImage.getPath());
        assertEquals(testImage.getName(), returnedImage.getName());
        logic.delete(image);
    }

    @Test
    void testCreateEntity() {
        //get all the image from the DB
        List<Image> list = logic.getAll();
        //store the size of list/ this way we know how many image exits in DB
        int originalSize = list.size();

        //create a new Image and save it so we can delete later
        Image testImage = logic.createEntity(sampleMap);
        //add the newly created image to DB
        logic.add(testImage);

        //get all the image again
        list = logic.getAll();
        //the new size of images must be 1 larger than original size
        assertEquals(originalSize + 1, list.size());

        //delete the new image, so DB is reverted back to it original form
        logic.delete(testImage);

        //get all image again
        list = logic.getAll();
        //the new size of image must be same as original size
        assertEquals(originalSize, list.size());


    }

    @Test
    void testGetColumnNames() {
        List<String> imageList = logic.getColumnNames();
        List<String> imageListExpected = Arrays.asList("ID", "URL", "Name", "Path");
        for (int i = 0; i < imageListExpected.size(); i++) {
            assertEquals(imageListExpected.get(i), imageListExpected.get(i));
        }
    }

    @Test
    void testGetColumnCodes() {
        List<String> imageListWithCodes = logic.getColumnCodes();
        List<String> imageListWithCodesExpected = Arrays.asList(ImageLogic.ID, ImageLogic.URL, ImageLogic.NAME, ImageLogic.PATH);
        for (int i = 0; i < imageListWithCodesExpected.size(); i++) {
            assertEquals(imageListWithCodesExpected.get(i), imageListWithCodesExpected.get(i));
        }
    }

    @Test
    void testExtractDataAsList() {
        Image image= logic.createEntity(sampleMap);
        List<?> imageList = logic.extractDataAsList(image);
        assertEquals(sampleMap.get(ImageLogic.URL)[0],imageList.get(1));
        assertEquals(sampleMap.get(ImageLogic.NAME)[0],imageList.get(2));
        assertEquals(sampleMap.get(ImageLogic.PATH)[0],imageList.get(3));

    }


    @Test
    void testGetWithUrl() {
        Image image = logic.createEntity(sampleMap);
        logic.add(image);

        List<Image> list = logic.getWithUrl(sampleMap.get(ImageLogic.URL)[0]);
        for (Image testImage : list) {
            assertEquals(sampleMap.get(ImageLogic.URL)[0], testImage.getUrl());
        }

        logic.delete(image);
    }

    @Test
    void testGetWithPath() {
        Image image =logic.createEntity(sampleMap);
        logic.add(image);

        Image testImage = logic.getWithPath(sampleMap.get(ImageLogic.PATH)[0]);
//        assertEquals(sampleMap.get(ImageLogic.URL)[0], testImage.getUrl());
        assertEquals(sampleMap.get(ImageLogic.PATH)[0], testImage.getPath());
//        assertEquals(sampleMap.get(ImageLogic.NAME)[0], testImage.getName());

        logic.delete(image);
    }

    @Test
    void testGetWithName() {
        Image image =logic.createEntity(sampleMap);
        logic.add(image);

        List<Image> list = logic.getWithName(sampleMap.get(ImageLogic.NAME)[0]);
        for (Image testImage : list) {
            assertEquals(sampleMap.get(ImageLogic.NAME)[0], testImage.getName());
        }

        logic.delete(image);
    }
}