package com.cz.app;

/**
 * Created by cz on 16/3/7.
 */
public class Item {
    public String name;
    public String info;
    public String className;

    public Item(String name, String className, String info) {
        this.name = name;
        this.className = className;
        this.info = info;
    }

    @Override
    public String toString() {
        return name;
    }
}
