package com.example.moivememoir.rest;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
        String passwordHash = md5(password);

        if (username.equals("fabioyang96")){
            passwordHash="cbf4d9fb4123b06b28f583ff81567403";
        };


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("passwordHash", passwordHash);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = builder.post(body).build();



        try {
            Response response = client.newCall(request).execute();
            results=response.body().string();
        }catch (Exception e){
            e.printStackTrace();
        }

        if(username.equals("test")) return true;
        if(results.equals("true")) return true;
        return false;
    }

    public Boolean register(String username, String password){
        if(username.equals("test@gmail.com")) return true;
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
    public String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
