package com.olehshynkarchuk.task.repo;

import java.util.HashMap;
import java.util.Map;

public class Repository {
    private static Repository instance;

    public static Repository getInstance() {
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
    }

    public Map<Integer, Goods> items;

    public Repository() {
        Map<Integer, Goods> temp = Map.of(1, new Goods("Product#1", 25.3),
                2, new Goods("Product#2", 35.1),
                3, new Goods("Product#3", 4.31),
                4, new Goods("Product#4", 215),
                5, new Goods("Product#5", 2.9),
                6, new Goods("Product#6", 1.31),
                7, new Goods("Product#7", 123.13),
                8, new Goods("Product#8", 12),
                9, new Goods("Product#9", 9.78),
                10, new Goods("Product#10", 1.34));
        this.items = new HashMap<>(temp);
    }

    public Map<String, Integer> getCount() {
        return Map.of("count", items.size());
    }

    public Goods getItem(Integer ID) {
        return items.get(ID);
    }

    public void putItem(Goods newGoods) {
        items.put(items.size() + 1, newGoods);
    }

    public Map<Integer, Goods> getAllGoodsTable() {
        return items;
    }
}
