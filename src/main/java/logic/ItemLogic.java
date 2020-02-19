/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;


import dal.ItemDAL;
import entity.Category;
import entity.Image;
import entity.Item;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author wufan
 */
public class ItemLogic extends GenericLogic<Item, ItemDAL>{
    public static final String DESCRIPTION = "description";
    public static final String CATEGORY_ID = "categoryId";
    public static final String IMAGE_ID = "imageId";
    public static final String LOCATION = "location";
    public static final String PRICE = "price";
    public static final String TITLE = "title";
    public static final String DATE = "date";
    public static final String URL = "url";
    public static final String ID = "id";
    
    public ItemLogic() {
        super(new ItemDAL());
    }
  
    @Override
    public List<Item> getAll(){
        return get(()->dao().findAll());
    }
    @Override
    public Item getWithId( int id){
        return get(()->dao().findById(id));
    }
   

    @Override
    public Item createEntity(Map<String, String[]> parameterMap) {
        Item item = new Item();
        if(parameterMap.containsKey(ID)){
            item.setId(Integer.parseInt(parameterMap.get(ID)[0]));
        }
        item.setDescription(validateInput(false, DESCRIPTION, parameterMap.get(DESCRIPTION)[0], 65535 ));
        item.setUrl(validateInput(false, URL, parameterMap.get(URL)[0], 255));
        if(parameterMap.containsKey(CATEGORY_ID)){
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
            BigDecimal price = null;
            try {
                price = new BigDecimal(numberFormat.parse(parameterMap.get(PRICE)[0]).toString());
            } catch (ParseException e) {
                Logger.getLogger(ItemLogic.class.getName()).log(Level.SEVERE, null, e);
            }
            item.setPrice(price);
        }
        item.setTitle(validateInput(false, TITLE, parameterMap.get(TITLE)[0], 255));
        item.setLocation(validateInput(true, LOCATION, parameterMap.get(LOCATION)[0], 45));
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date date;
        try {
            date = format.parse(parameterMap.get(DATE)[0]);
        } catch (ParseException ex) {
            date = new Date();
            Logger.getLogger(ItemLogic.class.getName()).log(Level.SEVERE, null, ex);
        }
        item.setDate(date);
        if(parameterMap.containsKey(CATEGORY_ID)){
            Integer categoryId = Integer.valueOf(parameterMap.get(CATEGORY_ID)[0]);
            item.setCategory(new Category(categoryId));
        }
        if(parameterMap.containsKey(IMAGE_ID)){
            Integer imageId = Integer.valueOf(parameterMap.get(IMAGE_ID)[0]);
            item.setImage(new Image(imageId));
        }

        return item;
    } 

    @Override
    public List<String> getColumnNames() {
        return Arrays.asList("Description", "Category Id", "Image Id", "Location","Price","Title","Date","URL","ID");
    }

    @Override
    public List<String> getColumnCodes() {
        return Arrays.asList(DESCRIPTION, CATEGORY_ID, IMAGE_ID, LOCATION,PRICE,TITLE,DATE,URL,ID);
    }

    @Override
    public List<?> extractDataAsList(Item e) {
        return Arrays.asList(e.getId(), e.getUrl(), e.getPrice(), e.getTitle(),e.getDate(),e.getLocation(),e.getDescription(),e.getCategory().getId(),e.getImage().getId());
    }

    public List<Item> getWithCategory(int categoryId){
        return get(()->dao().findByCategory(categoryId));
    }
    public List<Item> getWithPrice(BigDecimal price){
        return get(() -> dao().findByPrice(price));
    }
    public List<Item> getWithTitle(String title){
        return get(() ->dao().findByTitle(title));
    }
    public List<Item> getWithDate(Date date){
        return get(() ->dao().findByDate(date));
    }
    public List<Item> getWithLocation(String location){
        return get(() ->dao().findByLocation(location));
    }
    public List<Item> getWithDescription(String description){
        return get(() ->dao().findByDescription(description));
    }
    public Item getWithUrl(String url){
        return get(() ->dao().findByUrl(url));
    }
    
}
