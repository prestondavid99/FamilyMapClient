package com.example.familymapclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements LoginFragment.Listener {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        // TODO : Gets disconnected here?
        Fragment fragment = new MapsFragment();
        fragmentManager.beginTransaction().replace(R.id.fragmentFrameLayout, fragment).commit();
    }

    @Override
    public void registered() {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}