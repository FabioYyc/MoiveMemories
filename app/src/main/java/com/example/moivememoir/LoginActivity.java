package com.example.moivememoir;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.moivememoir.rest.RestHelper;

public class LoginActivity  extends AppCompatActivity{
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private EditText etUsername;
    private EditText etPassword;
    private String login_url = "http://192.168.0.101/member/login.php";
    private String username;
    private String password;
    private String passwordHash;
    private RestHelper restHelper;
    private Boolean loginSuccess;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        restHelper = new RestHelper();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etUsername = findViewById(R.id.editEmail);
        etPassword = findViewById(R.id.editPassword);
        Button login = findViewById(R.id.loginButton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Retrieve the data entered in the edit texts
                username = etUsername.getText().toString().toLowerCase().trim();
                password = etPassword.getText().toString().trim();
                String[] details = new String[]{username, password};
                if (validateInputs()) {
                LoginTask loginTask = new LoginTask();
                displayLoader();
                loginTask.execute(details);
                }
            }
        });
    }

    /**
     * Validates inputs and shows error if any
     * @return Boolean
     */
    private boolean validateInputs() {
        if("".equals(username)){
            etUsername.setError("Username cannot be empty");
            etUsername.requestFocus();
            return false;
        }
        if("".equals(password)){
            etPassword.setError("Password cannot be empty");
            etPassword.requestFocus();
            return false;
        }
        return true;
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("login.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    /**
     * Async login using restful method
     *
     */

    private class LoginTask extends AsyncTask<String, Void, Boolean>{

        @Override
        protected Boolean doInBackground(String... params) {

            String username = params[0];
            String password = params[1];
            return restHelper.login(username, password);
        }
        @Override
        protected void onPostExecute(Boolean result) {
            loginSuccess = result;
            Toast toast = Toast.makeText(getApplicationContext(), "message", Toast.LENGTH_LONG);
            if(loginSuccess) {
                pDialog.dismiss();
                toast.setText("Login successful");
                toast.show();
                Intent intent = new Intent(LoginActivity.this,
                        MainActivity.class);
                startActivity(intent);
            }
            else{
                toast.setText("Incorrect Credentials");
                toast.show();
            }
        }


    }

}
