/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dg.core;

import com.dg.core.states.LoggedAppState;
import com.dg.core.states.LoginAppState;
import com.dg.core.states.MenuAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;

/**
 * Aplicação cliente dá acesso à renderização, input, aos estados do jogo, e ao
 * Nifty.
 *
 * @author Victor
 */
public class ClientApplication extends SimpleApplication {

    //Nifty
    private NiftyJmeDisplay niftyDisplay;
    private Nifty nifty;

    @Override
    public void simpleInitApp() {
        //remove opções de debug    
        setDisplayFps(false);
        setDisplayStatView(false);
        flyCam.setEnabled(false);

        //inicia o nifty
        niftyDisplay = new NiftyJmeDisplay(assetManager,
                inputManager,
                audioRenderer,
                viewPort);
        guiViewPort.addProcessor(niftyDisplay);
        nifty = niftyDisplay.getNifty();
        nifty.loadStyleFile("Interface/nifty/nifty-styles.xml");
        nifty.addXml("Interface/nifty/popup.xml");
        
        //inicia o estado para login
        stateManager.attach(new LoginAppState());
    }

    @Override
    public void simpleUpdate(float tpf) {
        //loop inicial
    }

    /**
     * Retorna o objeto Nifty da aplicação
     * @return a instância do Nifty na aplicação
     */
    public Nifty getNifty() {
        return nifty;
    }

    public void timeout() {
        // O jogo perdeu conexão, deve ir pra tela de login e mostrar mensagem de timeout
    }
    
}
