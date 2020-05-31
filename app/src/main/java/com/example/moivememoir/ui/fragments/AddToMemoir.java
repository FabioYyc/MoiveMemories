package com.example.moivememoir.ui.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.moivememoir.R;
import com.example.moivememoir.entities.Cinema;
import com.example.moivememoir.entities.Memoir;
import com.example.moivememoir.entities.Movie;
import com.example.moivememoir.entities.Person;
import com.example.moivememoir.helpers.RestHelper;
import com.example.moivememoir.ui.activities.MainActivity;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddToMemoir extends Fragment {

    Spinner cinemaSpinner;
    List<String> cinemaList = new ArrayList<String>();
    List<Cinema> cinemas = new ArrayList<>();
    Person user;
    RestHelper restHelper;
    TextView watchedDatePicker;
    TextView watchedTimePicker;
    TextView etComment;
    TextView addMemoirName;
    TextView addMemoirYear;
    EditText etCinemaName;
    EditText etCinemaPostcode;
    RatingBar memoirRatingBar;
    Button submitMemoir;
    ImageView addMemoirPoster;
    Boolean newCinema = false;
    Switch createNewCinema;
    String newCinemaName;
    String newCinemaPostcode;


    DatePickerDialog.OnDateSetListener onDateSetListener;
    TimePickerDialog timePickerDialog;
    String watchedDate;
    String timeStr;
    String comment;
    float rating;
    Cinema selectedCinema;
    Movie movie;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_to_memoir, container, false);
        cinemaSpinner = view.findViewById(R.id.cinemaSpinner);
        etComment = view.findViewById(R.id.etComment);
        memoirRatingBar = view.findViewById(R.id.addMemoirRatingBar);
        submitMemoir = view.findViewById(R.id.submitMemoir);
        createNewCinema = view.findViewById(R.id.createNewCinema);
        etCinemaName = view.findViewById(R.id.etCinemaName);
        etCinemaPostcode = view.findViewById(R.id.etCinemaPostcode);
        addMemoirPoster = view.findViewById(R.id.addMemoirPoster);
        addMemoirName = view.findViewById(R.id.addMemoirName);
        addMemoirYear = view.findViewById(R.id.addMemoirYear);
        //So user cannot edit without selecting to add new cinema
        etCinemaName.setFocusable(false);
        etCinemaPostcode.setFocusable(false);

        createNewCinema.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //if create new cinema, set the fields editable
                    newCinema = true;
                    etCinemaName.setFocusable(true);
                    etCinemaPostcode.setFocusable(true);
                }
            }
        });

        Bundle bundle = this.getArguments();
        String movieJson = "";
        if (bundle != null) {
            Gson gson = new Gson();
            movieJson = bundle.getString("movieJson");
            movie = gson.fromJson(movieJson, Movie.class);
            addMemoirName.setText(movie.getName());
            DateFormat dateFormat = new SimpleDateFormat("yyyy");
            String dateStr = dateFormat.format(movie.getReleaseDate());
            addMemoirYear.setText(dateStr);
            Picasso.get().load(movie.getImageLink()).into(addMemoirPoster);

        }



        MainActivity activity = (MainActivity) getActivity();
        user = activity.getUser();
        GetCinemas getCinemas = new GetCinemas();
        getCinemas.execute();

        watchedDatePicker = view.findViewById(R.id.watchDatePicker);


        //Attach a datepicker dialog to the text view
        watchedDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        onDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // cool so I can set date format here, nice
                month = month + 1;
//                Log.d("RegisterActivity", "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);
                String dayStr = Integer.toString(day);
                if (dayStr.length() < 2) dayStr = "0" + dayStr;
                String monthStr = Integer.toString(month);
                if (monthStr.length() < 2) monthStr = "0" + monthStr;

                String date = year + "-" + monthStr + "-" + dayStr;
                String displayDate = year + "-" + month + "-" + day;
                watchedDate = date;
                watchedDatePicker.setText(displayDate);
            }
        };

        watchedTimePicker = view.findViewById(R.id.watchTimePicker);
        watchedTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {

                                timeStr = "T"+convertDate(sHour) + ":" + convertDate(sMinute) +":" +"00" +"+10:00";
                            }
                        }, hour, minutes, true);
                timePickerDialog.show();
            }
        });


        submitMemoir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo: set all the fields to the correct values
                rating = memoirRatingBar.getRating();
                comment = etComment.getText().toString();
                selectedCinema = cinemas.get(cinemaSpinner.getSelectedItemPosition());
                newCinemaName = etCinemaName.getText().toString();
                newCinemaPostcode = etCinemaPostcode.getText().toString();
                if(validateInputs()) {
                    PostToMemoir postToMemoir = new PostToMemoir();
                    postToMemoir.execute();
                    GetCinemas getCinemas = new GetCinemas();
                    getCinemas.execute();
                }

            }
        });


        return view;
    }

    private class GetCinemas extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            restHelper = new RestHelper();
            String result = restHelper.getCinemas();
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (!result.equals("failed")) {

                JSONArray array = null;
                try {
                    array = new JSONArray(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < array.length(); i++) {
                    //String[] colHEAD = new String[]{"NAME", "RELEASE YEAR", "RATING"};
                    JSONObject obj = null;

                    try {
                        obj = array.getJSONObject(i);
                        String addressStr = null;
                        addressStr = obj.getString("cinemaName") + " " + obj.getString("cinemaPostcode");
                        cinemaList.add(addressStr);
                        Cinema cinema = new Cinema(obj.getInt("cinemaId"), obj.getString("cinemaName"), obj.getString("cinemaPostcode"));
                        cinemas.add(cinema);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, cinemaList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cinemaSpinner.setAdapter(adapter);

            }
        }
    }
    public String convertDate(int input) {
        if (input >= 10) {
            return String.valueOf(input);
        } else {
            return "0" + String.valueOf(input);
        }
    }

    private boolean validateInputs() {

        //validate email
        if(newCinema) {
            if ("".equals(etCinemaName.getText().toString())) {
                etCinemaName.setError("Cinema name");
                etCinemaName.requestFocus();
                return false;
            }

            String postcodePattern = "[0-9]";
            if (!etCinemaPostcode.getText().toString().matches(postcodePattern)) {
                etCinemaPostcode.setError("Cinema postcode cannot be empty");
                etCinemaPostcode.requestFocus();
                return false;
            }

            //validate password

        }
        if(comment.isEmpty()){
            etComment.setError("Comment cannot be empty");
            etComment.requestFocus();

        }

        return true;
    }

    private class PostToMemoir extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            //todo: create the memoir json here
            String returnStr = "false";
            String dateTimeStr = watchedDate +timeStr;
            Memoir memoir = new Memoir();
            memoir.setMemoirRating(rating);
            memoir.setMemoirWatchDatetime(dateTimeStr);
            memoir.setMemoirComment(comment);
            memoir.setPersonId(user);
            memoir.setMemoirMovieName(movie.getName());
            DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'+10:00'");
           String dateStr = outputFormat.format(movie.getReleaseDate());
            memoir.setMemoirMovieReleaseDate(dateStr);
            if(!newCinema) memoir.setCinemaId(selectedCinema);

            else{
                Cinema cinema = new Cinema(cinemaList.size()+1,
                        newCinemaName, newCinemaPostcode);
                memoir.setCinemaId(cinema);

            }

                returnStr =restHelper.addMemoir(memoir, newCinema);

            return returnStr;
        }

        @Override
        protected void onPostExecute(String result) {
            if(result.equals("success")){
                Toast toast=Toast.makeText(getActivity(),"Movie added to memoir",Toast.LENGTH_SHORT);
                toast.show();
                submitMemoir.setEnabled(false);
            }
            else{
                Toast toast=Toast.makeText(getActivity(),"Adding to memoir failed",Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}


