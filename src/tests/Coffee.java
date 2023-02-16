package tests;

import java.util.HashMap;

public class Coffee extends CaffeineBeverage {
    @Override
    void brew() {
        System.out.println("Dripping Coffee through filter");
    }

    @Override
    void addCondiments() {
        System.out.println("Adding condiments to coffee");
        HashMap<String, String> coffee = new HashMap<>();
    }
}
