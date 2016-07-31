/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dg.nw;

import com.dg.game.World;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Luís
 */
public class ConnectionHandler {

    private URL serverUrl;
    private Client clientInfo;
    private boolean connected = false;
    private final String USER_AGENT = "Mozilla/5.0";
    public static final String DEFAULT_URL = "http://localhost:8084/darkgrains-web/gamedata";
    private Response lastResponse;

    public ConnectionHandler(String serverUrl, String username, String password) throws RuntimeException {
        try {
            this.serverUrl = new URL(serverUrl);
            HttpURLConnection con = (HttpURLConnection) this.serverUrl.openConnection();

            Response r = sendRequest(ClientRequest.buildLoginAction(username, password));

            if (r.getSuccess()) {
                this.clientInfo = new Client(username, r.getMessage());
                this.connected = true;
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private Response sendRequest(String request) {
        try {
            HttpURLConnection con = (HttpURLConnection) this.serverUrl.openConnection();

            //add reuqest header
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(request);
            wr.flush();
            wr.close();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //System.out.println(response.toString());
            lastResponse = new Response(response.toString());
            return lastResponse;
        } catch (IOException ex) {
            Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean updateClient() {
        if (!connected) {
            throw new RuntimeException("Client is not connected to the server.");
        }

        Response r = sendRequest(ClientRequest.updateAction(clientInfo.getClientHash()));
        if (!r.getSuccess()) {
            this.connected = false;
            return false;
        }
        return true;
    }

    public boolean endClient() {
        if (!connected) {
            throw new RuntimeException("Client is not connected to the server.");
        }

        Response r = sendRequest(ClientRequest.endAction(clientInfo.getClientHash()));
        return true;
    }

    public List<World> getWorldList() {
        if (!connected) {
            throw new RuntimeException("Client is not connected to the server.");
        }

        Response r = sendRequest(ClientRequest.getWorldListAction(clientInfo.getClientHash()));

        JSONParser jp = new JSONParser();
        try {
            JSONArray ja = (JSONArray) jp.parse(r.getMessage());
            List<World> lw = new ArrayList<>();

            for (int i = 0; i < ja.size(); i++) {
                lw.add(new World((JSONObject) ja.get(i)));
            }

            return lw;
        } catch (ParseException ex) {
            Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public boolean deleteWorld(int id) {
        if (!connected) {
            throw new RuntimeException("Client is not connected to the server.");
        }

        Response r = sendRequest(ClientRequest.deleteWorldAction(clientInfo.getClientHash(), id));
        return true;
    }

    public Response getLastResponse() {
        return lastResponse;
    }
}
