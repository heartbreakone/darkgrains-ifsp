/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dg.core.states;

import com.dg.nw.ConnectionHandler;
import com.dg.nw.NetworkListener;
import com.dg.nw.UpdateTask;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import java.util.Date;
import java.util.List;
import java.util.Timer;

/**
 *
 * @author Luís
 */
public class LoggedAppState extends AbstractAppState {
    private AppStateManager stateManager;
    private ConnectionHandler connection;
    private List<NetworkListener> states;

    public LoggedAppState(ConnectionHandler connection) {
        if(!connection.isConnected()) {
            throw new RuntimeException("Not connected");
        }
        this.connection = connection;
        
    }
   
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.stateManager = stateManager;
        
        Timer timer = new Timer();
        timer.schedule(new UpdateTask(this), 0, 5000);
        
    }

    @Override
    public void update(float tpf) { 
        
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
    }
    
    private void addListener(NetworkListener state) {
        states.add(state);
    }
    
    private void sendTimeoutMessage() {
        for(NetworkListener nl : states) {
            nl.onTimeout();
        }
    }

    public ConnectionHandler getConnection() {
        return connection;
    }
    
    public void update() {
        System.out.println("Teste " + new Date());
        if(connection.isConnected() == false) {
            sendTimeoutMessage();
            stateManager.detach(this);
        }

        try {
            connection.updateClient();
        } catch(RuntimeException e) {
            sendTimeoutMessage();
            stateManager.detach(this);
        }
    }    
}
