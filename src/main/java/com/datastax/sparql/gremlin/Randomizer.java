package com.datastax.sparql.gremlin;

import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

public class Randomizer {

    final private static HashSet<Integer> usedVarNames;
    private static String duplicatedVariableSufix = "";

    static{
        usedVarNames = new HashSet<>();
    }

    private static int getRandomNumber(){
        int randomNum;
        do {
            randomNum = ThreadLocalRandom.current().nextInt(10000, 99999 + 1);
        } while (usedVarNames.contains(randomNum)); // fast lookup, to ensure not collisions
        usedVarNames.add(randomNum);
        return randomNum;
    }

    public static String getRandomVarName() {
        int randomNum = getRandomNumber();
        return "?r" + String.valueOf(randomNum);
    }

    public static String dup(){
        return  duplicatedVariableSufix;
    }



    public static void setDuplicatedVariable() {
        int randomNum = getRandomNumber();
        duplicatedVariableSufix = "_" + String.valueOf(randomNum);
    }
}
