package com.example.mpfinal;

import java.util.ArrayList;

public class Display_Fish {
    private static ArrayList<Boolean> own;
    private static int MAX_FISH = 7;
    private static String[] names= new String[]{"채소꾸러미", "랜덤박스", "구글카드", "랜덤상자", "문화상품권", "햄버거", "화장품"};;

    public static String[] getNames() {
        return names;
    }

    public void setNames(String[] names) {
        this.names = names;
    }

    public static ArrayList<Boolean> getOwn() {
        return own;
    }

    public static void setOwn(ArrayList<Boolean> own) {
        Display_Fish.own = own;
    }

    public static int getMaxFish() {
        return MAX_FISH;
    }
}
