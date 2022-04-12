package com.example.familymapclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements LoginFragment.Listener {

    private MenuItem searchIcon;
    private MenuItem settingsIcon;
    boolean menuSettings;
    boolean settingsClicked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        menuSettings = false;
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentFrameLayout);

        if (fragment == null) {
            fragment = createLoginFragment();

            fragmentManager.beginTransaction().add(R.id.fragmentFrameLayout, fragment).commit();
        } else {
            if (fragment instanceof LoginFragment) {
                ((LoginFragment) fragment).registerListener(this);
            }
        }
    }

    private Fragment createLoginFragment() {
        LoginFragment fragment = new LoginFragment();
        fragment.registerListener(this);
        return fragment;
    }

    @Override
    public void signedIn() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment fragment = new MapsFragment();
        menuSettings = true;
        fragmentManager.beginTransaction().replace(R.id.fragmentFrameLayout, fragment).commit();
    }

    @Override
    public void registered() {

    }

    public void settings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void search() {

    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        searchIcon = menu.findItem(R.id.search_button);
        settingsIcon = menu.findItem(R.id.settings_button);
        if (menuSettings) {
            searchIcon.setVisible(true);
            settingsIcon.setVisible(true);
        } else if (settingsClicked) {
            searchIcon.setVisible(false);
            settingsIcon.setVisible(false);
        } else {
            searchIcon.setVisible(false);
            settingsIcon.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_button:
                settings();
                return true;
            case R.id.search_button:
                search();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}