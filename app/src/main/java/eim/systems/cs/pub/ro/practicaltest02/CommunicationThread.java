package eim.systems.cs.pub.ro.practicaltest02;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.protocol.HTTP;
import cz.msebera.android.httpclient.util.EntityUtils;
import general.Constants;
import general.Utilities;



public class CommunicationThread extends Thread {

    private ServerThread serverThread;
    private Socket socket;
    private Socket utcSocket;

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    @Override
    public void run() {
        if (socket == null) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
            return;
        }
        try {
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Buffered Reader / Print Writer are null!");
                return;
            }
            Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client (city / information type!");



            String money = bufferedReader.readLine();
            Log.i(Constants.TAG, "[COMMUNICATION THREAD] Am primit " + money +" de la "  + socket.getInetAddress().getHostAddress());

            if (money == null || money.isEmpty()) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error receiving parameters from client (city / information type!");
                return;
            }

            HashMap<String, Moneda> mon = serverThread.getMoneda();

        //    String clientAddress = socket.getInetAddress().getHostAddress();
             HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("https://api.coindesk.com/v1/bpi/currentprice/EUR.json");
            HttpResponse httpGetResponse = httpClient.execute(httpGet);
            HttpEntity httpGetEntity = httpGetResponse.getEntity();

            Document document = Jsoup.parse(EntityUtils.toString(httpGetEntity));
            Element element = document.child(0);
            Elements elements = element.getElementsByTag("body");
            Log.e("ANDREIIIII", String.valueOf(elements));

            String line = String.valueOf(elements);
            String a = line.substring(6, line.length());
            a = a.substring(0, a.length()-7);

            JSONObject content = new JSONObject(a);
            JSONObject currentObservation = content.getJSONObject("time");
            String time = currentObservation.getString("updated");
            Log.e("ANDREIIIII", time);

            currentObservation = content.getJSONObject("bpi");
            String USD = currentObservation.getString("USD");
            Log.e("ANDREIIIII", USD);

            currentObservation = content.getJSONObject("bpi");
            String EUR = currentObservation.getString("EUR");
            Log.e("ANDREIIIII", EUR);
            String result = time;

            if(money.equals("EUR"))
                result = time + EUR;
            if(money.equals("USD"))
                result = time + EUR;

            printWriter.println(result);
            printWriter.flush();

        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
            if (utcSocket != null) {
                try {
                    utcSocket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }

}
