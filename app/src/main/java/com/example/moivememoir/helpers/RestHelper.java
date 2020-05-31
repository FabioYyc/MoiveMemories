package com.example.moivememoir.helpers;

import com.example.moivememoir.entities.Cinema;
import com.example.moivememoir.entities.Memoir;
import com.example.moivememoir.entities.Movie;
import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RestHelper {
    private OkHttpClient client = null;

    public RestHelper() {
        client = new OkHttpClient();
    }

    private static final String BASE_URL = "http://192.168.1.102:16377/MovieMemoir/webresources/";
    //Home Ip "http://192.168.1.102:16377/MovieMemoir/webresources/";
    //monash ip http://118.138.16.25:16377/MovieMemoir/webresources/


    public String login(String username, String password) {
        String results = "null";
        String methodPath = "memoir.credentials/login";
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        String passwordHash = md5(password);

        //my test account so I dont have to remember the password
        // as it was added in assignment 1 and i forgot the password already
        if (username.equals("fabioyang96")) {
            passwordHash = "cbf4d9fb4123b06b28f583ff81567403";
        }
        ;




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
            results = response.body().string();
            code = response.code();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (code == 200) return results;
        return "failed";

    }

    public String findTop5Movies(int personId) {
        String result = "failed";
        if (personId == 111) return result;
        String methodPath = "memoir.memoir/findRecentMovieWatched/" + personId;
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            //if get data failed
//            if(response.code() != 200) return result;
            result = response.body().string();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    public List<Movie> movieSearch(String movieName, String apiKey) {
        List<Movie> movieList = new ArrayList<>();
        String result;
        //https://api.themoviedb.org/3/search/movie?api_key={api_key}&query=Jack+Reacher
        movieName.replace(" ", "+");

        String url = "https://api.themoviedb.org/3/search/movie?api_key=" + apiKey + "&query=" + movieName;

        Request.Builder builder = new Request.Builder();
        builder.url(url);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            //if get data failed
            if (response.code() != 200) return movieList;
            result = response.body().string();
            try {

                JSONObject jsonObj = new JSONObject(result);

                JSONArray array = jsonObj.getJSONArray("results");
                for (int i = 0; i < array.length(); i++) {
//                    {
//                        "poster_path": "/IfB9hy4JH1eH6HEfIgIGORXi5h.jpg",
//                            "adult": false,
//                            "overview": "Jack Reacher must uncover the truth behind a major government conspiracy in order to clear his name. On the run as a fugitive from the law, Reacher uncovers a potential secret from his past that could change his life forever.",
//                            "release_date": "2016-10-19",
//                            "genre_ids": [
//                        53,
//                                28,
//                                80,
//                                18,
//                                9648
//  ],
//                        "id": 343611,
//                            "original_title": "Jack Reacher: Never Go Back",
//                            "original_language": "en",
//                            "title": "Jack Reacher: Never Go Back",
//                            "backdrop_path": "/4ynQYtSEuU5hyipcGkfD6ncwtwz.jpg",
//                            "popularity": 26.818468,
//                            "vote_count": 201,
//                            "video": false,
//                            "vote_average": 4.19
//                    }
                    JSONObject obj = array.getJSONObject(i);
                    Movie movie = new Movie();
                    String posterBasePath = "https://image.tmdb.org/t/p/w500/";
                    String posterPath = posterBasePath + obj.getString("poster_path");
                    movie.setImageLink(posterPath);
                    movie.setDetail(obj.getString("overview"));
                    Date releaseDate = new SimpleDateFormat("yyyy-MM-dd").parse(obj.getString("release_date"));
                    movie.setReleaseDate(releaseDate);
                    movie.setName(obj.getString("title"));
                    movie.setId(obj.getInt("id"));
                    float myFloatValue = BigDecimal.valueOf(obj.getDouble("vote_average")).floatValue();
                    movie.setRating(myFloatValue / 2);

                    movieList.add(movie);

                }
            } catch (JSONException | ParseException e) {
                e.printStackTrace();

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return movieList;

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

        if (!newCinema) {

            RequestBody body = RequestBody.create(memoirJson, JSON);
            Request request = credentialBuilder.post(body).build();

            try {
                Response response = client.newCall(request).execute();
                if (response.code() == 204) result = "success";
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            //todo:create a new cinema first, then post the json.
            Cinema cinema = memoir.getCinemaId();
            try {
                if (createNewCinema(cinema)) {

                    RequestBody body = RequestBody.create(memoirJson, JSON);
                    Request request = credentialBuilder.post(body).build();
                    try {
                        Response response = client.newCall(request).execute();
                        if (response.code() == 204) result = "success";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return result;
    }

    public String register(String[] params) {
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
        if (personId == 0) return "Create person failed";

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
        if (!personResult) return "Create person failed";
        ;

        //create a credential
        Date signUpDate = new Date();
        String dateStr = signUpDate + "T00:00:00+10:00";
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
        RequestBody body = RequestBody.create(String.valueOf(credentialObj), JSON);
        Request credentialRequest = credentialBuilder.post(body).build();
        try {
            Response response = client.newCall(credentialRequest).execute();
            credentialResult = response.body().string();
            if (credentialResult.equals("Success")) return "Success";
        } catch (Exception e) {
            e.printStackTrace();
        }


        return credentialResult;

    }

    public Integer createPersonId() {
        String path = "memoir.person/count";
        int result = 0;
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + path);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            result = Integer.parseInt(response.body().string());
            return result + 1;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    public Integer createMemoirId() {
        String path = "memoir.memoir/count";
        int result = 0;
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + path);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            result = Integer.parseInt(response.body().string());
            return result + 1;

        } catch (Exception e) {
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

        RequestBody body = RequestBody.create(cinemaJson, JSON);
        Request request = builder.post(body).build();
        Response response = client.newCall(request).execute();
        if (response.code() == 204) return true;

        return false;

    }

    public String getWatchesPerPostcode(int personId, String startDate, String endDate) {
        String result = "failed";
//        if(personId == 111) return result;

        String methodPath = "memoir.memoir/findTotalWatchedPerPostcode/" + personId + "/"
                + startDate + "/" + endDate;

        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            //if get data failed
//            if(response.code() != 200) return result;
            result = response.body().string();

            if (response.code() != 200) return "failed";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    public String getWatchesPerMonth(int personId, String year) {
        String result = "failed";
//        if(personId == 111) return result;

        String methodPath = "memoir.memoir/findTotalWatchedPerMonth/" + personId + "/"
                + year;

        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            //if get data failed
//            if(response.code() != 200) return result;
            result = response.body().string();

            if (response.code() != 200) return "failed";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    public String getCinemas() {
        String methodPath = "memoir.cinema";
        String result = "failed";
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            //if get data failed
//            if(response.code() != 200) return result;
            result = response.body().string();

            if (response.code() == 200) return result;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;


    }

    public String getMemoirs() {
        String methodPath = "memoir.memoir";
        String result = "failed";
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            //if get data failed
//            if(response.code() != 200) return result;
            result = response.body().string();

            if (response.code() == 200) return result;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;


    }


    public Boolean createPerson(JSONObject personObj) {
        String path = "memoir.person";
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + path);

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, personObj.toString());
        Request request = builder.post(body).build();
        try {
            Response response = client.newCall(request).execute();
            int code = response.code();
            //check if the code is 200
            if (code == 204) return true;
        } catch (Exception e) {
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
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
