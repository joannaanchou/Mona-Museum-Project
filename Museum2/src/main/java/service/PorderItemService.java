package service;

import java.sql.Connection;
import java.util.List;

import model.PorderItem;




public interface PorderItemService {
	
	
    void batchInsert(List<PorderItem> items, Connection conn);
    
    
    List<PorderItem> getItemsByPorderId(int porderId);
    
    
    void update(PorderItem item);
    
    
  
    void deleteById(int id);
}