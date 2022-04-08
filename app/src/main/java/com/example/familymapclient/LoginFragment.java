package com.example.familymapclient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.GnssAntennaInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
    private EditText serverHost;
    private EditText serverPort;
    private EditText username;
    private EditText password;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private Button signInButton;
    private Button registerButton;
    private boolean radioButtonClicked;
    private RadioButton radioButton;
    private String gender;

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

    public void validate() {
        if (serverHost.getText().toString().equals("") || serverPort.getText().toString().equals("") || username.getText().toString().equals("")
                || password.getText().toString().equals("")) {
            signInButton.setEnabled(false);
        } else {
            signInButton.setEnabled(true);
        }
    }

    public void validateRegister() {
        if (serverHost.getText().toString().equals("") || serverPort.getText().toString().equals("") || username.getText().toString().equals("")
                || password.getText().toString().equals("") || firstName.getText().toString().equals("") || lastName.getText().toString().equals("")
                || email.getText().toString().equals("") || !radioButtonClicked) {
            registerButton.setEnabled(false);
        } else {
            registerButton.setEnabled(true);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        /* EditText */
        serverHost = view.findViewById(R.id.serverHost);
        serverPort = view.findViewById(R.id.serverPort);
        username = view.findViewById(R.id.username);
        password = view.findViewById(R.id.password);
        firstName = view.findViewById(R.id.firstName);
        lastName = view.findViewById(R.id.lastName);
        email = view.findViewById(R.id.email);

        serverHost.setText("10.0.2.2");
        serverPort.setText("8080");
        username.setText("thisUser");
        password.setText("password");
        firstName.setText("firsName");
        lastName.setText("lastName");
        email.setText("email");

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validate();
                validateRegister();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        serverHost.addTextChangedListener(textWatcher);
        serverPort.addTextChangedListener(textWatcher);
        username.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);
        firstName.addTextChangedListener(textWatcher);
        lastName.addTextChangedListener(textWatcher);
        email.addTextChangedListener(textWatcher);

        /* Buttons */
        signInButton = view.findViewById(R.id.signInButton);




        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    LoginRequest loginRequest = new LoginRequest(username.getText().toString(), password.getText().toString());
                    @SuppressLint("HandlerLeak") Handler threadHandler = new Handler() {
                        @SuppressLint("HandlerLeak")
                        @Override
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
                            if (listener != null) {
                                listener.signedIn();
                            }
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
        registerButton = view.findViewById(R.id.registerButton);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButtonClicked = true;
                radioButton = (RadioButton) view.findViewById(checkedId);
                gender = radioButton.getText().toString();
                validateRegister();
            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                try {
                    /* Radio Buttons */

                    RegisterRequest registerRequest = new RegisterRequest(username.getText().toString(), password.getText().toString(),
                            email.getText().toString(), firstName.getText().toString(), lastName.getText().toString(), gender);


                    @SuppressLint("HandlerLeak") Handler threadHandler = new Handler() {
                        @Override
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

                            if (listener != null) {
                                listener.registered();
                            }
                        }
                    };
                    DownloadTask task = new DownloadTask(threadHandler, serverHost.getText().toString(), serverPort.getText().toString(), registerRequest);
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    executor.submit(task);

                } catch (Exception e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                }
            }
        });
        validate();
        validateRegister();
        return view;
    }
}