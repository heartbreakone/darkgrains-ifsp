/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dg.nifty;

import com.dg.core.states.LoginAppState;
import com.dg.core.states.MenuAppState;
import com.jme3.app.state.AbstractAppState;

/**
 *
 * @author Victor
 */
public class MainMenuScreenController extends AbstractScreenController {
    
    protected MenuAppState appState;
    
    public MainMenuScreenController(AbstractAppState appState) {
        this.appState = (MenuAppState) appState;
    }

    /**
     * seta configurações alteradas
     */
    public void setConfigs() {
        //get settings from controls
        try {
            popdown();
            appState.setSettings();
        } catch (RuntimeException e) {
            erroMessage = e.getMessage();
            popup("erro");
        }
    }

    /**
     * sai da aplicação
     */
    public void sair() {
        
        try {
            appState.quit();
        } catch (Exception e) {
            erroMessage = e.getMessage();
            popup("erro");
        }
    }

    /**
     * vai para a tela de seleção de mundos
     */
    public void jogar() {
        nifty.gotoScreen("world-selection");
    }

    /**
     * desloga e volta a tela de login
     */
    public void logout() {
        //desloga
        appState.logout();
    }

    /**
     * abre pagina no navegador para o rank
     */    
    public void rank() {
        try {
            appState.openRank();
        } catch (RuntimeException e) {
            erroMessage = e.getMessage();
            popup("erro");
        }
    }
}
