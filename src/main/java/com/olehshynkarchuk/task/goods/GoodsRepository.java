package com.olehshynkarchuk.task.goods;

import java.util.HashMap;
import java.util.Map;

public class GoodsRepository {

    private final Map<Integer, Goods> items;

    public GoodsRepository() {
        Map<Integer, Goods> temp = Map.of(1, new Goods(1, "Product#1", 25.3),
                2, new Goods(2, "Product#2", 35.1),
                3, new Goods(3, "Product#3", 4.31),
                4, new Goods(4, "Product#4", 215),
                5, new Goods(5, "Product#5", 2.9),
                6, new Goods(6, "Product#6", 1.31),
                7, new Goods(7, "Product#7", 123.13),
                8, new Goods(8, "Product#8", 12),
                9, new Goods(9, "Product#9", 9.78),
                10, new Goods(10, "Product#10", 1.34));
        this.items = new HashMap<>(temp);
    }

    public Map<String, Integer> getCount() {
        return Map.of("count", items.size());
    }

    public Goods getItem(Integer ID) {
        return items.get(ID);
    }

    public Goods putItem(Goods newGoods) {
        return items.put(items.size() + 1, new Goods(items.size() + 1, newGoods.name(), newGoods.price()));
    }

    public Map<Integer, Goods> getAllGoodsTable() {
        return items;
    }

    public Goods deleteGoodsByID(int ID) {
        return items.remove(ID);
    }

    public Goods updateGoodsByID(int ID, Goods newGoods) {
        return items.replace(ID, new Goods(items.size() + 1, newGoods.name(), newGoods.price()));
    }
}
