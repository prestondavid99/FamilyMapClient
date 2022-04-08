package com.example.familymapclient;
import com.google.gson.Gson;

import requestresult.EventResult;
import requestresult.LoginRequest;
import requestresult.LoginResult;
import requestresult.PersonResult;
import requestresult.RegisterRequest;
import requestresult.RegisterResult;


import java.io.*;
import java.net.*;


public class ServerProxy {

    private String serverHost;
    private String serverPort;

    public ServerProxy(String serverHost, String serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    public LoginResult login(LoginRequest r) {

        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/login");

            // Start constructing our HTTP request
            HttpURLConnection http = (HttpURLConnection)url.openConnection();


            // Specify that we are sending an HTTP POST request
            http.setRequestMethod("POST");

            // Indicate that this request will contain an HTTP request body
            http.setDoOutput(true);

            // Connect to the server and send the HTTP request
            http.connect();

            Gson gson = new Gson();
            String reqData = gson.toJson(r);
            // Get the output stream containing the HTTP request body
            OutputStream reqBody = http.getOutputStream();

            // Write the JSON data to the request body
            writeString(reqData, reqBody);

            // Close the request body output stream, indicating that the
            // request is complete
            reqBody.close();

            // By the time we get here, the HTTP response has been received from the server.
            // Check to make sure that the HTTP response from the server contains a 200
            // status code, which means "success".  Treat anything else as a failure.
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Get the input stream containing the HTTP response body
                InputStream respBody = http.getInputStream();

                // Extract JSON data from the HTTP response body
                String respData = readString(respBody);
                LoginResult result = gson.fromJson(respData, LoginResult.class);
                // Display the JSON data returned from the server
                System.out.println(respData);
                result.setSuccess(true);
                return result;
            }
            else {
                // The HTTP response status code indicates an error
                // occurred, so print out the message from the HTTP response
                System.out.println("ERROR: " + http.getResponseMessage());

                // Get the error stream containing the HTTP response body (if any)
                InputStream respBody = http.getErrorStream();

                // Extract data from the HTTP response body
                String respData = readString(respBody);
                LoginResult result = gson.fromJson(respData, LoginResult.class);

                // Display the data returned from the server
                System.out.println(respData);
                result.setSuccess(false);
                return result;
            }
        }
        catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
        }
        return null;
    }

    public PersonResult getPeople(String authToken) {

        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/person");

            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("GET");

            http.setDoOutput(false);

            http.addRequestProperty("Authorization", authToken);

            http.addRequestProperty("Accept", "application/json");

            http.connect();

            Gson gson = new Gson();
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Get the input stream containing the HTTP response body
                InputStream respBody = http.getInputStream();

                // Extract JSON data from the HTTP response body
                String respData = readString(respBody);
                PersonResult result = gson.fromJson(respData, PersonResult.class);
                // Display the JSON data returned from the server
                System.out.println(respData);
                result.setSuccess(true);
                return result;
            }
            else {
                // The HTTP response status code indicates an error
                // occurred, so print out the message from the HTTP response
                System.out.println("ERROR: " + http.getResponseMessage());

                // Get the error stream containing the HTTP response body (if any)
                InputStream respBody = http.getErrorStream();

                // Extract data from the HTTP response body
                String respData = readString(respBody);
                PersonResult result = gson.fromJson(respData, PersonResult.class);

                // Display the data returned from the server
                System.out.println(respData);
                result.setSuccess(false);
                return result;
            }
        }
        catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
        }
        return null;
    }

    public EventResult getEvents(String authToken) {

        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/event");

            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("GET");

            http.setDoOutput(false);

            http.addRequestProperty("Authorization", authToken);

            http.addRequestProperty("Accept", "application/json");

            http.connect();

            Gson gson = new Gson();
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Get the input stream containing the HTTP response body
                InputStream respBody = http.getInputStream();

                // Extract JSON data from the HTTP response body
                String respData = readString(respBody);
                EventResult result = gson.fromJson(respData, EventResult.class);
                // Display the JSON data returned from the server
                System.out.println(respData);
                result.setSuccess(true);
                return result;
            }
            else {
                // The HTTP response status code indicates an error
                // occurred, so print out the message from the HTTP response
                System.out.println("ERROR: " + http.getResponseMessage());

                // Get the error stream containing the HTTP response body (if any)
                InputStream respBody = http.getErrorStream();

                // Extract data from the HTTP response body
                String respData = readString(respBody);
                EventResult result = gson.fromJson(respData, EventResult.class);

                // Display the data returned from the server
                System.out.println(respData);
                result.setSuccess(false);
                return result;
            }
        }
        catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
        }
        return null;
    }

    public RegisterResult register(RegisterRequest r) {
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/register");

            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("POST");

            http.setDoOutput(true);

            http.connect();

            Gson gson = new Gson();
            String reqData = gson.toJson(r);
            // Get the output stream containing the HTTP request body
            OutputStream reqBody = http.getOutputStream();

            // Write the JSON data to the request body
            writeString(reqData, reqBody);

            reqBody.close();
            // TODO : it should be ok, but it's not
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Get the input stream containing the HTTP response body
                InputStream respBody = http.getInputStream();

                // Extract JSON data from the HTTP response body
                String respData = readString(respBody);
                RegisterResult result = gson.fromJson(respData, RegisterResult.class);
                // Display the JSON data returned from the server
                System.out.println(respData);
                result.setSuccess(true);
                return result;
            }
            else {

                // The HTTP response status code indicates an error
                // occurred, so print out the message from the HTTP response
                System.out.println("ERROR: " + http.getResponseMessage());

                // Get the error stream containing the HTTP response body (if any)
                InputStream respBody = http.getErrorStream();

                // Extract data from the HTTP response body
                String respData = readString(respBody);
                RegisterResult result = gson.fromJson(respData, RegisterResult.class);

                // Display the data returned from the server
                result.setSuccess(false);
                System.out.println(respData);
                return result;
            }
        }
        catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
        }
        return null;
    }



    /*
        The readString method shows how to read a String from an InputStream.
    */
    private static String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    /*
        The writeString method shows how to write a String to an OutputStream.
    */
    private static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

}