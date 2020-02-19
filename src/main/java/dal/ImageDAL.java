/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dal;

import entity.Image;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author wufan
 */
public class ImageDAL extends GenericDAL<Image>{
    
    public ImageDAL(Class<Image> entityClass) {
        super(entityClass);
    }
    public ImageDAL() {
        super(Image.class);
    }

    public List<Image> findAll(){
        return findResults( "Image.findAll", null);
    }
    public Image findById(int id){
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        return findResult( "Image.findById", map);
    }
    public List<Image> findByUrl(String url){
        Map<String, Object> map = new HashMap<>();
        map.put("url", url);
        return findResults( "Image.findByUrl", map);
    }
    public Image findByPath(String path){
        Map<String, Object> map = new HashMap<>();
        map.put("path", path);
        return findResult("Image.findByPath", map);
    }
    public List<Image> findByName(String name){
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        return findResults("Image.findByName", map);
        
        
    }
}
