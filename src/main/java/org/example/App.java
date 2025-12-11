package org.example;

import java.util.Random;

public class App
{
    private static MyHashMap<String, String> hashMap = new MyHashMap<>();

    public static void main( String[] args )
    {
        Random random = new Random();

        for (int i = 0; i < 91; i++){
            int rnd = random.nextInt(100);
            hashMap.add("Key#" + rnd, "Value#" + rnd);
        }

        System.out.println(hashMap);
    }
}
