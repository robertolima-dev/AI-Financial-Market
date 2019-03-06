package com.crypfy.api;

import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class IndexProcessor {

    private static String FILENAME;

    public void writeIndex(String value,String name){

        FILENAME = name.equals("linea") ? "/crypfy/linea.txt" : "/crypfy/audace.txt";
        //do it
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILENAME))) {

            String content = value;
            bw.write(content);
            System.out.println("Done");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readIndex(String name){

        FILENAME = name.equals("linea") ? "/crypfy/linea.txt" : "/crypfy/audace.txt";
        String theValue = null;
        try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {
            theValue = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return theValue;
    }

}
