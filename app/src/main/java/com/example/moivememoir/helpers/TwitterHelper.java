package com.example.moivememoir.helpers;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class TwitterHelper {
    HashMap<String, Integer> positiveWords = new HashMap<String, Integer>();
    HashMap<String, Integer> negativeWords = new HashMap<String, Integer>();
    Context context;

    public TwitterHelper(Context context) {
        this.context = context;
        positiveWords = readFileReturnMap("positive-words.txt");
        negativeWords = readFileReturnMap("negative-words.txt");
    }


    public String calculateCategory(ArrayList<String> tweetText){
        int negative =0;
        int positive =0 ;

        for(int i =0; i<tweetText.size(); i++){
            for(String word : tweetText.get(i).split( "[\\s,]+")) {
                if (positiveWords.containsKey(word.toLowerCase().trim())) positive += 1;
                if (negativeWords.containsKey(word.toLowerCase().trim())) negative += 1;
            }
        }

        if(negative>positive) return "Negative";
        if(negative<positive) return "Positive";
        if(negative==positive) return "Neutral";

        return null;
    };

    public HashMap<String, Integer> readFileReturnMap(String filename){
        HashMap<String, Integer> returnWords = new HashMap<String, Integer>();

        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader
                    (context.getAssets().open(filename)));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if(!line.startsWith(";")){
                    returnWords.put(line, 1);
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return returnWords;

    }
}
