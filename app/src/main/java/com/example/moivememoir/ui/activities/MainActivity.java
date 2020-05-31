package com.example.moivememoir.ui.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;


import com.example.moivememoir.R;
import com.example.moivememoir.entities.Person;
import com.example.moivememoir.ui.fragments.HomeFragment;
import com.example.moivememoir.ui.fragments.MovieSearchFragment;
import com.example.moivememoir.ui.fragments.ReportFragment;
import com.example.moivememoir.ui.fragments.MapViewFragment;
import com.example.moivememoir.ui.fragments.WatchlistFragment;
import com.example.moivememoir.viewModel.WatchListViewModel;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {
    private TextView tvGreeting;
    private Person user = new Person(111, "", "");
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private WatchListViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //adding the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#26cbf0")));
        }

        viewModel = new
                ViewModelProvider(this ).get(WatchListViewModel .class);

        viewModel.initalizeVars(getApplication());


        Intent mIntent = getIntent();
        String userJson = mIntent.getExtras().getString("userObject");
        Gson g = new Gson();
        user= g.fromJson(userJson, Person.class);



        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nv);
        toggle = new ActionBarDrawerToggle(this,
                drawerLayout, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        //these two lines of code show the navicon drawer icon top left hand side
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);
        replaceFragmentFromMain(new HomeFragment());
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.home:
                replaceFragmentFromMain(new HomeFragment());
                break;
            case R.id.movieSearch:
                replaceFragmentFromMain(new MovieSearchFragment());
                break;
            case R.id.watchlist:
                replaceFragmentFromMain(new WatchlistFragment());
                break;
            case R.id.report:
                replaceFragmentFromMain(new ReportFragment());
                break;
            case R.id.maps:
                replaceFragmentFromMain(new MapViewFragment());
                break;

        }
        //this code closes the drawer after you selected an item from the menu,
//        otherwise stay open
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragmentFromMain(Fragment nextFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, nextFragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    public Person getUser() {
        return this.user;
    }

}
