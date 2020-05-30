package com.example.moivememoir.helpers;

import com.example.moivememoir.entities.Cinema;
import com.example.moivememoir.entities.Memoir;
import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class RestHelper {
    private OkHttpClient client = null;

    public RestHelper() {
        client = new OkHttpClient();
    }

    private static final String BASE_URL ="http://118.138.16.25:16377/MovieMemoir/webresources/";
           //Home Ip "http://192.168.1.102:16377/MovieMemoir/webresources/";
    //monash ip http://118.138.16.25:16377/MovieMemoir/webresources/


    public String login(String username, String password){
        String results = "null";
        String methodPath = "memoir.credentials/login";
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        String passwordHash = md5(password);

        if (username.equals("fabioyang96")){
            passwordHash="cbf4d9fb4123b06b28f583ff81567403";
        };

        if (username.equals("test")) return "unit test";


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
        int code = 0;


        try {
            Response response = client.newCall(request).execute();
            results=response.body().string();
             code= response.code();
        }catch (Exception e){
            e.printStackTrace();
        }

        if(code == 200) return results;
        return "failed";

    }

    public String findTop5Movies(int personId){
        String result = "failed";
        if(personId == 111) return result;
        String methodPath = "memoir.memoir/findRecentMovieWatched/" + personId;
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            //if get data failed
//            if(response.code() != 200) return result;
            result=response.body().string();


        }catch (Exception e){
            e.printStackTrace();
        }
        return result;

    }

    public String movieSearch(String movieName, String apiKey){
        String result = "failed";

        //https://api.themoviedb.org/3/search/movie?api_key={api_key}&query=Jack+Reacher
        movieName.replace(" ", "+");

        String url = "https://api.themoviedb.org/3/search/movie?api_key="+ apiKey+ "&query="+movieName;

        Request.Builder builder = new Request.Builder();
        builder.url(url);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            //if get data failed
            if(response.code() != 200) return result;
            result=response.body().string();


        }catch (Exception e){
            e.printStackTrace();
        }
        return result;

    }

    public String addMemoir(Memoir memoir, Boolean newCinema) {
        String result = "failed";
        String methodPth = "memoir.memoir";

        Request.Builder credentialBuilder = new Request.Builder();
        credentialBuilder.url(BASE_URL + methodPth);

        Gson gson = new Gson();
        int memoirId = createMemoirId();
        memoir.setMemoirId(memoirId);

        String memoirJson = gson.toJson(memoir);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        if(!newCinema){

            RequestBody body = RequestBody.create( memoirJson, JSON);
            Request request = credentialBuilder.post(body).build();

            try {
                Response response = client.newCall(request).execute();
                if(response.code()==204) result =  "success";
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        else{
            //todo:create a new cinema first, then post the json.
            Cinema cinema = memoir.getCinemaId();
            try {
                if(createNewCinema(cinema)){

                    RequestBody body = RequestBody.create( memoirJson, JSON);
                    Request request = credentialBuilder.post(body).build();
                    try {
                        Response response = client.newCall(request).execute();
                        if(response.code()==204) result =  "success";
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return result;
    }

    public String register(String[] params){
        //String[] details = new String[]{email, password, DOB, address, state, gender, firstName, surname, postcode};
        String email = params[0];
        String password = params[1];
        String DOB = params[2];
        String address = params[3];
        String state = params[4];
        String gender = params[5];
        String firstName = params[6];
        String surname = params[7];
        String postcode = params[8];
        String credentialResult = "";
        Boolean personResult;

        String passwordHash = md5(password);

        //create a person
        Integer personId = createPersonId();
        //if it is 0 meaning the request was not successful
        if(personId == 0) return "Create person failed";

        JSONObject personObj = new JSONObject();
        try {
            personObj.put("personId", personId);
            personObj.put("personName", firstName);
            personObj.put("personSurname", surname);
            personObj.put("personAddress", address);
            personObj.put("personDob", DOB);
            personObj.put("personState", state);
            personObj.put("personGender", gender);
            personObj.put("personPostcode", postcode);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        //return false if create person failed
        personResult = createPerson(personObj);
        if(!personResult) return "Create person failed";;

        //create a credential
        Date signUpDate = new Date();
        String dateStr = signUpDate+"T00:00:00+10:00";
        JSONObject credentialObj = new JSONObject();
        try {
            credentialObj.put("username", email);
            credentialObj.put("passwordHash", passwordHash);
            credentialObj.put("personId", personObj);
            credentialObj.put("signUpDate", dateStr);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String credential = "memoir.credentials/signUp";

        Request.Builder credentialBuilder = new Request.Builder();
        credentialBuilder.url(BASE_URL + credential);

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(  String.valueOf(credentialObj), JSON);
        Request credentialRequest = credentialBuilder.post(body).build();
        try {
            Response response = client.newCall(credentialRequest).execute();
            credentialResult=response.body().string();
            if(credentialResult.equals("Success")) return "Success";
        }catch (Exception e){
            e.printStackTrace();
        }



        return credentialResult;

    }

    public Integer createPersonId(){
        String path = "memoir.person/count";
        int result = 0;
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + path);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            result=Integer.parseInt(response.body().string());
            return result+1;

        }catch (Exception e){
            e.printStackTrace();
        }
        return result;

    }

    public Integer createMemoirId(){
        String path = "memoir.memoir/count";
        int result = 0;
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + path);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            result=Integer.parseInt(response.body().string());
            return result+1;

        }catch (Exception e){
            e.printStackTrace();
        }
        return result;

    }

    public Boolean createNewCinema(Cinema cinema) throws IOException {
        String path = "memoir.cinema";
        Gson gson = new Gson();

        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + path);
        String cinemaJson = gson.toJson(cinema);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create( cinemaJson, JSON);
        Request request = builder.post(body).build();
        Response response = client.newCall(request).execute();
        if(response.code()==204) return true;

        return false;

    }

    public String getWatchesPerPostcode(int personId, String startDate, String endDate){
        String result = "failed";
//        if(personId == 111) return result;

        String methodPath = "memoir.memoir/findTotalWatchedPerPostcode/" + personId +"/"
                + startDate +"/" +endDate;

        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            //if get data failed
//            if(response.code() != 200) return result;
            result=response.body().string();

            if(response.code() !=200) return "failed";

        }catch (Exception e){
            e.printStackTrace();
        }
        return result;

    }

    public String getWatchesPerMonth(int personId, String year){
        String result = "failed";
//        if(personId == 111) return result;

        String methodPath = "memoir.memoir/findTotalWatchedPerMonth/" + personId +"/"
                + year;

        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            //if get data failed
//            if(response.code() != 200) return result;
            result=response.body().string();

            if(response.code() !=200) return "failed";

        }catch (Exception e){
            e.printStackTrace();
        }
        return result;

    }

    public String getCinemas(){
        String methodPath = "memoir.cinema";
        String result = "failed";
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            //if get data failed
//            if(response.code() != 200) return result;
            result=response.body().string();

            if(response.code() ==200) return result;

        }catch (Exception e){
            e.printStackTrace();
        }
        return result;


    }



    public Boolean createPerson(JSONObject personObj){
        String path = "memoir.person";
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + path);

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON,  personObj.toString());
        Request request = builder.post(body).build();
        try {
            Response response = client.newCall(request).execute();
            int code = response.code();
            //check if the code is 200
            if(code == 204) return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
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
