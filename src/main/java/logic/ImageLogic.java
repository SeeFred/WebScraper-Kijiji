/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import dal.ImageDAL;
import entity.Image;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 * @author wufangfang
 */
public class ImageLogic extends GenericLogic<Image,ImageDAL>{
    public static final String PATH = "path";
    public static final String NAME = "name";
    public static final String URL = "url";
    public static final String ID = "id";
    
    public ImageLogic(){
        super(new ImageDAL());
    }
    @Override
    public List<Image> getAll(){
        return get(()->dao().findAll());
    }
    @Override
    public Image getWithId( int id){
        return get(()->dao().findById(id));
    }
    @Override
    public Image createEntity(Map<String, String[]> parameterMap) {
        Image image = new Image();
        if(parameterMap.containsKey(ID)){
            image.setId(Integer.parseInt(parameterMap.get(ID)[0]));
        }
        image.setPath(validateInput(false, PATH, parameterMap.get(PATH)[0], 255));
        image.setName(validateInput(false, NAME, parameterMap.get(NAME)[0], 255));
        image.setUrl(validateInput(false, URL, parameterMap.get(URL)[0], 255));
        return image;
    } 
    @Override
    public List<String> getColumnNames() {
        return Arrays.asList("ID", "URL", "Name", "Path");
    }

    @Override
    public List<String> getColumnCodes() {
        return Arrays.asList(ID, URL, NAME, PATH);
    }

    @Override
    public List<?> extractDataAsList(Image e) {
        return Arrays.asList(e.getId(), e.getUrl(), e.getName(), e.getPath());
    }
    public List<Image> getWithUrl(String url){
        return get(()->dao().findByUrl(url));
    }
    public Image getWithPath(String path){
        return get(()->dao().findByPath(path));
    }

    public List<Image> getWithName(String name) { return get(()->dao().findByName(name)); }
}
