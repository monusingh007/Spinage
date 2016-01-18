package com.spinages.webservices;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.spinages.app.AppConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.MalformedInputException;

/**
 * Created by monu on 8/19/2015.
 */
public class URLconnectionService {


    public static JSONObject getWebServiceData(String urlString, String payload, String contentType, String email, String password) throws IOException {
        // BEGIN_INCLUDE(get_inputstream)
        InputStream stream = null;
        JSONObject output_json_obj = null;
        JSONObject input_json = null;
        String jsonString = " ";
        try {
            String encoded;
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            encoded = Base64.encodeToString((email + ":" + password).getBytes("UTF-8"), Base64.NO_WRAP);
            if(email!=""&&password!="")
            conn.setRequestProperty("Authorization", "Basic " + encoded);
            conn.setRequestProperty("Accept", "*/*");
            conn.setRequestProperty("Accept-Language", "en-US");
            conn.setRequestProperty("Accept-Encoding", "gzip");


            if (contentType.equals(AppConstants.POST_JSON_REQUEST)) {// Start the query
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");

                try {
                    Log.e("payload",payload);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(payload);
                wr.flush();
                wr.close();
            }
            if (contentType.equals(AppConstants.POST_TEXT_REQUEST)) {
                    conn.setRequestProperty("Content-Type", "text/plain");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");

                try {
                    Log.e("payload", payload);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(payload);
                writer.flush();
                writer.close();
            }


            if (contentType.equals(AppConstants.GET_REQUEST)) {
                conn.setDoInput(true);
                conn.setRequestMethod("GET");

            }

            conn.connect();

            stream = conn.getInputStream();

            try {
                Log.e("server response", stream.toString());
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        } catch (SocketTimeoutException e) {
            e.printStackTrace();

        } catch (MalformedInputException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    stream));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            stream.close();
            jsonString = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            output_json_obj = new JSONObject(jsonString);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        Log.e("jsoninput", jsonString);
        // return JSON String
        return output_json_obj;

    }

}
