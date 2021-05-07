package javasec.urlconnection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class URLConnectionDemo {
    public static void main(String[] args) {
        try {
            URL url = new URL("https://www.badmonkey.site");
            URLConnection urlConnection = url.openConnection();
            urlConnection.setConnectTimeout(1000);
            urlConnection.setRequestProperty("user-agent","javasec");
            urlConnection.setReadTimeout(1000);
            urlConnection.connect();
            StringBuilder s = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null){
                s.append("\n").append(line);
            }
            System.out.println(s.toString());
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
