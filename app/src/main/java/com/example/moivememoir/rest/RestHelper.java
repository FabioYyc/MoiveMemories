package com.example.moivememoir.rest;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

public class RestHelper {
    private OkHttpClient client = null;
    private String results;

    public RestHelper() {
        client = new OkHttpClient();
    }

    private static final String BASE_URL =
            "http://192.168.1.102:16377/MovieMemoir/webresources/";


    public Boolean login(String username, String password){
        String methodPath = "memoir.credentials/login";
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        if(username.equals("test")) return true;
        return false;
    }

    public void register(){

    }

    public  String findByCourse(int courseid) {
        final String methodPath = "student.student/findByCourse/" + courseid;
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results=response.body().string();
        }catch (Exception e){
            e.printStackTrace();
        }
        return results;
    }

}
