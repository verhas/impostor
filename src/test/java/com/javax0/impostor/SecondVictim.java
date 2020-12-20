package com.javax0.impostor;

public class SecondVictim {

    public String sound(String animal){
        if( animal.equals("horse"))return "neight";
        if( animal.equals("duck"))return "quack";
        if( animal.equals("dog"))return "wuff";
        return "broaf";
    }

    public void run() {
        System.out.println("Victim run");
    }
}
