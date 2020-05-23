package com.example.moivememoir.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.moivememoir.R;
import com.example.moivememoir.entities.Person;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private TextView tvGreeting;
    private Person user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent i = getIntent();
        tvGreeting= findViewById(R.id.tvGreeting);
        user = (Person) i.getSerializableExtra("userObject");
        String greeting = "Hello " + user.getPersonName();
        tvGreeting.setText(greeting);
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        TextView tvDate = findViewById(R.id.tvDate);
        tvDate.setText(formattedDate);

    }
}
