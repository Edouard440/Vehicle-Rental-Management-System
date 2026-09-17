package vrms.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import vrms.contract.Identifiable;
import java.util.ArrayList;

public class Repository <T extends Identifiable>{

    private final Map<String, T> stockage = new HashMap<>();

    public void save(T item){
        String key = item.getId();
        stockage.put(key, item);
    }

    public T findById(String id){
        return stockage.get(id);
    }

    public List<T> findAll(){
        List<T> copyStockage = new ArrayList<>(stockage.values());
        return  copyStockage;
    }

    public void saveAll(List<? extends T> items){
        for(T item : items){
            save(item);
        }
    }

}