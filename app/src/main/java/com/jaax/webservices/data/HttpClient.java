package com.jaax.webservices.data;

import com.jaax.webservices.utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {

    public String getWeatherData( String lugar ){
        HttpURLConnection connection = null;
        InputStream inputStream = null;

        try {
            connection = (HttpURLConnection) ( new URL( Utils.INSTANCE.getBASE_URL()+lugar ) ).openConnection();
            connection.setRequestMethod( "GET" );
            connection.setDoInput( true );
            connection.connect();

            StringBuilder sb = new StringBuilder();
            inputStream = connection.getInputStream();
            BufferedReader bf = new BufferedReader( new InputStreamReader(inputStream));
            String line = null;

            while( (line = bf.readLine()) != null ) {
                sb.append(line).append("\r\n");
            }
            inputStream.close();
            connection.disconnect();

            return sb.toString();
        } catch( IOException ioe ) {
            ioe.printStackTrace();
        }
        return null;
    }
}
