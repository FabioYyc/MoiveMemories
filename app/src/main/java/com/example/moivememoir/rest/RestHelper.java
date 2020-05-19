package com.example.moivememoir.rest;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RestHelper {
    private OkHttpClient client = null;
    private String results;

    public RestHelper() {
        client = new OkHttpClient();
    }

    private static final String BASE_URL =
            "http://192.168.1.102:16377/NamedQueryTutorial/webresources/";

    public String getAllStudents() {
        final String methodPath = "student.student/";
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    public Boolean login(String username, String password){
        if(username.equals("test")) return true;
        return false;
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
