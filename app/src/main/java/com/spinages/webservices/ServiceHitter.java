package com.spinages.webservices;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by monu on 7/21/2015.
 */

public class  ServiceHitter extends AsyncTask<String, Void, String> {
    private String url;
    private JSONObject jsonObject = null;

   public ServiceHitter(String url)
    {
        this.url = url;

    }
   public ServiceHitter(String url,JSONObject jsonObject)
    {
        this.url = url;
        this.jsonObject= jsonObject;
    }

    @Override
    protected void onPreExecute() {
        //super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            return loadFromNetwork(url);
        } catch (IOException e) {
            String msg = "connection error";
            return msg;
        }

    }

    @Override
    protected void onPostExecute(String result) {

        Log.i("service output", result);

    }


    private InputStream downloadUrl(String urlString) throws IOException {
        // BEGIN_INCLUDE(get_inputstream)
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        String encoded = Base64.encodeToString(("harkomal" + ":" + "harkomal").getBytes("UTF-8"), Base64.NO_WRAP);
        conn.setRequestProperty("Authorization", "Basic " + encoded);
        conn.setRequestProperty("Authorization", "Basic " + encoded);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept-Language", "en-US");
        conn.setRequestProperty("Accept-Encoding", "gzip");
        conn.setRequestProperty("Accept", "*/*");
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        if(jsonObject==null) {// Start the query

        }else
        {
            conn.setDoOutput(true);
            JSONObject authCredential = new JSONObject();
            JSONObject values = new JSONObject();
            try {

                values.accumulate("password", "harkomal");
                values.accumulate("username", "harkomal");
                authCredential.accumulate("authCredential", values);
            } catch (Exception e) {
                Log.e("error", "jsonerror");
            }




            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(jsonObject.toString());
            wr.flush();
            wr.close();
            Log.e("jsoninput", jsonObject.toString());
        }
        conn.connect();
        InputStream stream = conn.getInputStream();
        Log.e("monu",stream.toString());
        return stream;

        // END_INCLUDE(get_inputstream)
    }


    private String readIt(InputStream stream) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder sb = new StringBuilder();
        String newLine = System.getProperty("line.separator");
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line + newLine);
        }
        String result = sb.toString();
        Log.e("Output",stream.toString());
        return result;

    }

    private String loadFromNetwork(String urlString) throws IOException {
        InputStream stream = null;
        String str = "";

        try {
            stream = downloadUrl(urlString);
           str = readIt(stream);

        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        return str;
    }
}
