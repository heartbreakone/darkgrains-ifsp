/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dg.core.states;

import com.dg.core.ClientApplication;
import com.dg.nifty.LoginScreenController;
import com.dg.nw.ConnectionHandler;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.audio.AudioNode;
import de.lessvoid.nifty.Nifty;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Victor
 */
public class LoginAppState extends AbstractAppState {

    //referencias da classe mãe
    private ClientApplication application;
    private AppStateManager stateManager;
    //controladores da Nifty
    private LoginScreenController loginController;
    //backtrack
    private AudioNode bt;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.stateManager = stateManager;
        this.application = (ClientApplication) app;

        Nifty nifty = application.getNifty();

        //controladores de tela do Nifty
        loginController = new LoginScreenController(this);
        nifty.registerScreenController(loginController);
        //adiciona as telas em xml ao Nifty
        nifty.addXml("Interface/nifty/login.xml");

        //inicia som
        bt = new AudioNode(app.getAssetManager(), "Sounds/MúsicasConvertidasParaOgg/Músicas/MainMenu.ogg", false);
        bt.setLooping(true);
        bt.setVolume(0.6f);
        bt.setPositional(false);
        bt.setDirectional(false);
        bt.play();

        //vai para a tela de login
        application.getNifty().gotoScreen("login");
        
        //log
        Logger.getGlobal().log(Level.INFO, "LoginAppState Iniciado!");
    }

    @Override
    public void update(float tpf) {
    }

    @Override
    public void cleanup() {
        super.cleanup();
    }

    /**
     * Remove este estado e sai da aplicação
     */
    public void quit() {
        //para o update() e chama o cleanup()
        stateManager.detach(this);
        //sai da aplicação
        application.stop();
    }
    
    /**
     * Abre uma sessão no servidor e passa ao estado de logado
     * @param username Nome de Usuário
     * @param password Senha
     * @throws RuntimeException Caso algum erro, ele é passado à classe controladora nifty 
     */
    public void loginUser(String username, String password) throws RuntimeException {
        //tratamento de strings
        //todo
        
        //tenta criar uma sessão do usuário no servidor
        ConnectionHandler connection = new ConnectionHandler(ConnectionHandler.DEFAULT_URL, username, password);
        
        //caso a conexão não pode abrir sessão por algum problema, ele é passado como excessão
        if (!connection.isConnected()) {
            throw new RuntimeException(connection.getLastResponse().getMessage());
        }
        
        //para este estado e inicia os estados de conectado ao servidor e do menu
        stateManager.detach(this);
        stateManager.attach(new LoggedAppState(connection));
        stateManager.attach(new MenuAppState());
        
        //log
        Logger.getGlobal().log(Level.INFO, "Usuário {0} logado e MenuAppState iniciado!", username);
    }

    /**
     * Seta as configurações do gerais do jogo
     *
     * @throws RuntimeException Caso algum erro, é passado à classe controladora
     * Nifty
     */
    public void setSettings() throws RuntimeException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Cria um novo usuário pelo servidor Método a ser chamado pela
     * ScreenController
     *
     * @param username Nome de usuário
     * @param password Senha
     * @param email Email
     * @throws RuntimeException Caso algum erro no servidor ele é passado à
     * controladora Nifty
     */
    public void createUser(String username, String password, String email) throws RuntimeException {
        //TODO
    }
}
