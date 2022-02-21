package com.codegym.task.task30.task3008;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleHelper {
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));


    public static void writeMessage(String message){
        System.out.println(message);
    }

    public static String readString(){
        String s = "";

        while (true){
            try {
                s = br.readLine();
                return s;
            } catch (IOException io){
                System.out.println("An error while trying to enter a number. Try again.");
            }
        }
    }

    public static int readInt(){
        int i = 0;

        while (true){
            try {
                i = Integer.parseInt(readString());
                return i;
            } catch (NumberFormatException ne) {
                System.out.println("An error while trying to enter a number. Try again.");
            }
        }
    }


}

