package com.example.familymapclient;

import android.content.Context;
import android.location.GnssAntennaInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import requestresult.LoginRequest;
import requestresult.RegisterRequest;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Listener listener;
    private static final String LOG_TAG = "MainActivity";
    private static final String TOTAL_SIZE_KEY = "TotalSizeKey";
    private TextView totalSizeTextView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {

    }

    public interface Listener {
        void signedIn();
        void registered();
    }

    public void registerListener(Listener listener) { this.listener = listener; }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        /* EditText */
        EditText serverHost = view.findViewById(R.id.serverHost);
        EditText serverPort = view.findViewById(R.id.serverPort);
        EditText username = view.findViewById(R.id.username);
        EditText password = view.findViewById(R.id.password);
        EditText firstName = view.findViewById(R.id.firstName);
        EditText lastName = view.findViewById(R.id.lastName);
        EditText email = view.findViewById(R.id.email);




        /* Buttons */
        Button signInButton = view.findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    LoginRequest loginRequest = new LoginRequest(username.getText().toString(), password.getText().toString());


                    Handler threadHandler = new Handler(Looper.getMainLooper()) {
                        public void handleMessage(Message message) {
                            Bundle bundle = message.getData();
                            boolean isSuccess = bundle.getBoolean("Success");
                            Context context = getActivity();
                            CharSequence text;
                            int duration = Toast.LENGTH_SHORT;
                            if (isSuccess) {
                                text = "Login Successful";
                                String firstLastName = bundle.getString("firstLastName");
                                Toast toast = Toast.makeText(context, firstLastName, duration);
                                toast.show();
                            } else {
                                text = "Login Failed";
                            }

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
//                            if (listener != null) {
//                                listener.signedIn();
//                            }
                        }
                    };
                    DownloadTask task = new DownloadTask(threadHandler, serverHost.getText().toString(), serverPort.getText().toString(), loginRequest);
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    executor.submit(task);


                } catch (Exception e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                }

            }



        });

        RadioGroup radioGroup;

        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        Button registerButton = view.findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    /* Radio Buttons */
                    int selectedId = radioGroup.getCheckedRadioButtonId();

                    RadioButton radioButton = (RadioButton) getActivity().findViewById(selectedId);
                    String gender = radioButton.getText().toString();
                    RegisterRequest registerRequest = new RegisterRequest(username.getText().toString(), password.getText().toString(),
                        email.getText().toString(), firstName.getText().toString(), lastName.getText().toString(), gender);

                    if(listener != null) {
                        listener.signedIn();

                        Handler threadHandler = new Handler(Looper.getMainLooper()) {
                            public void handleMessage(Message message) {
                                Bundle bundle = message.getData();
                                boolean isSuccess = bundle.getBoolean("Success");
                                Context context = getActivity();
                                CharSequence text;
                                int duration = Toast.LENGTH_SHORT;
                                if (isSuccess) {
                                    text = "Register Successful";
                                    String firstLastName = bundle.getString("firstLastName");
                                    Toast toast = Toast.makeText(context, firstLastName, duration);
                                    toast.show();
                                } else {
                                    text = "Register Failed";
                                }

                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            }
                        };
                        DownloadTask task = new DownloadTask(threadHandler, serverHost.getText().toString(), serverPort.getText().toString(), registerRequest);
                        ExecutorService executor = Executors.newSingleThreadExecutor();
                        executor.submit(task);

                    }
                } catch (Exception e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                }
            }
        });
        return view;
    }
}