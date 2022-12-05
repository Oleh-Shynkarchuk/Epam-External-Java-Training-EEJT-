package com.olehshynkarchuk.task.goods;

import java.util.HashMap;
import java.util.Map;

public class GoodsRepository {

    private final Map<Integer, Goods> items;

    public GoodsRepository() {
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

    public Goods putItem(Goods newGoods) {
        Goods goods = items.put(items.size() + 1, newGoods);
        return goods == null ? newGoods : goods;
    }

    public Map<Integer, Goods> getAllGoodsTable() {
        return items;
    }

    public Goods deleteGoodsByID(int ID) {
        return items.remove(ID);
    }

    public Goods replaceGoodsByID(int ID, Goods newGoods) {
        return items.replace(ID, newGoods);
    }
}
