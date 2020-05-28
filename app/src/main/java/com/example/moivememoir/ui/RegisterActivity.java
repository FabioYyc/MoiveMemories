package com.example.moivememoir.ui;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moivememoir.R;
import com.example.moivememoir.helpers.RestHelper;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private EditText etSurname;
    private EditText etFirstName;
    private TextView tvDOB;
    private EditText etAddress;
    private EditText etPostcode;
    private Spinner etState;
    private RadioGroup etGenders;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private String email;
    private String password;
    private String confirmPassword;
    private String passwordHash;
    private String surname;
    private String firstName;
    private String DOB;
    private String address;
    private String state;
    private String gender;
    private String postcode;
    private RestHelper restHelper;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        restHelper = new RestHelper();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.signUpEmail);
        etPassword = findViewById(R.id.signUpPassword);
        etConfirmPassword = findViewById(R.id.signUpPasswordConfirm);
        etSurname = findViewById(R.id.editSurname);
        etFirstName = findViewById(R.id.etFirstname);
        tvDOB = findViewById(R.id.tvDOB);
        etAddress = findViewById(R.id.etAddress);
        etState = findViewById(R.id.etState);
        etGenders = findViewById(R.id.signUpGender);
        etPostcode = findViewById(R.id.etPostcode);

        //radio group check, set the gender
        etGenders.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                // Check which radio button was clicked
                switch (checkedId) {
                    case R.id.radioFemale:
                        gender = "F";
                        break;
                    case R.id.radioMale:
                        gender = "M";
                        break;
                    case R.id.radioOther:
                        gender = "O";
                        break;
                }
            }
        });

        //Attach a datepicker dialog to the text view
        tvDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        RegisterActivity.this,
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
                if(dayStr.length() < 2) dayStr = "0"+dayStr;
                String monthStr = Integer.toString(month);
                if(monthStr.length() < 2) monthStr = "0"+monthStr;

                String date = year + "-" + monthStr + "-" + dayStr + "T00:00:00+10:00";
                String displayDate = year + "-" + month + "-" + day;
                DOB = date;
                tvDOB.setText(displayDate);
            }
        };
        //add spinner to state selection
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.states, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        etState.setAdapter(adapter);


        Button register = findViewById(R.id.registerSubmit);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Retrieve the data entered in the edit texts
                email = etUsername.getText().toString().toLowerCase().trim();
                password = etPassword.getText().toString().trim();
                confirmPassword = etConfirmPassword.getText().toString().trim();
                address = etAddress.getText().toString();
                state = etState.getSelectedItem().toString();
                firstName = etFirstName.getText().toString();
                surname = etSurname.getText().toString();
                postcode = etPostcode.getText().toString();

                //gender is already set


                if (validateInputs()) {
                    String[] details = new String[]{email, password, DOB, address, state, gender, firstName, surname, postcode};
                    RegisterActivity.RegisterTask registerTask = new RegisterTask();
                    displayLoader();
                    registerTask.execute(details);
                }
            }
        });
    }

    /**
     * Register, send two request, make new person object and credential object
     * Use uuid,
     *
     * @return Void, only set the register success
     */
    private class RegisterTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
//            String[] details = new String[]{email, password, firstName,surname,DOB, address, state, gender};


            return restHelper.register(params);
        }

        @Override
        protected void onPostExecute(String result) {

            Toast toast = Toast.makeText(getApplicationContext(), "message", Toast.LENGTH_LONG);
            Intent returnIntent = getIntent();
            if (result.equals("Success")) {
                pDialog.dismiss();
                toast.setText("Register successful");
                toast.show();

                returnIntent.putExtra("email", email);
                returnIntent.putExtra("password", password);
                setResult(RESULT_OK, returnIntent);
                finish();
            } else {
                pDialog.dismiss();
                toast.setText(result);
                toast.show();


            }
        }


    }


    /**
     * Validates inputs and shows error if any
     *
     * @return Boolean
     */
    private boolean validateInputs() {

        //validate email
        if ("".equals(email)) {
            etUsername.setError("Email cannot be empty");
            etUsername.requestFocus();
            return false;
        }

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (!email.matches(emailPattern)) {
            etUsername.setError("Invalid email");
            etUsername.requestFocus();
            return false;
        }

        //validate password

        if ("".equals(password) || password.length() < 5) {
            etPassword.setError("Password too short");
            etPassword.requestFocus();
            return false;
        }
        if (!confirmPassword.equals(password)) {
            etPassword.setError("Password does not match");
            etPassword.requestFocus();
            return false;
        }


        return true;
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(RegisterActivity.this);
        pDialog.setMessage("Signing up.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }
}
