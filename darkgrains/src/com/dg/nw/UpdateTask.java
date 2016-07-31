/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dg.nw;

import com.dg.core.states.LoggedAppState;
import java.util.TimerTask;

/**
 *
 * @author Luís
 */
public class UpdateTask extends TimerTask {
    private LoggedAppState laState;

    public UpdateTask(LoggedAppState laState) {
        this.laState = laState;
    }
    
    @Override
    public void run() {
        //laState.update();
    }
    
}
