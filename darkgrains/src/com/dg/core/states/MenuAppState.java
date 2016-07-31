/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dg.core.states;

import com.dg.core.ClientApplication;
import com.dg.nifty.MainMenuScreenController;
import com.dg.nifty.WorldSelectionScreenController;
import com.dg.game.World;
import com.dg.nifty.LoadScreenController;
import com.dg.nw.NetworkListener;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import de.lessvoid.nifty.Nifty;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Estado do menu. CRUD de Mundos, abre o rank, e joga.
 * @author Victor
 */
public class MenuAppState extends AbstractAppState implements NetworkListener {

    private ClientApplication app;
    private AppStateManager stateManager;
    //controladores de tela Nifty
    private MainMenuScreenController menuController;
    private WorldSelectionScreenController worldController;
    private LoadScreenController loadController;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);

        this.stateManager = stateManager;
        this.app = (ClientApplication) app;

        Nifty nifty = this.app.getNifty();

        //inicia e registra os controladores Nifty e XMLs
        menuController = new MainMenuScreenController(this);
        nifty.registerScreenController(menuController);
        worldController = new WorldSelectionScreenController(this,
                stateManager.getState(LoggedAppState.class).getConnection());
        nifty.registerScreenController(worldController);
        try {
            nifty.validateXml("Interface/nifty/menu.xml");
            nifty.validateXml("Interface/nifty/world-selection.xml");
        } catch (Exception ex) {
            Logger.getLogger(MenuAppState.class.getName()).log(Level.SEVERE, "Erro ao validar XML:\n\t{0}", ex);
        }
        nifty.addXml("Interface/nifty/menu.xml");
        nifty.addXml("Interface/nifty/world-selection.xml");

        //vai para tela do menu principal
        nifty.gotoScreen("menu");


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
        try {
            logout();
            app.stop();
        } catch (RuntimeException e) {
            Logger.getGlobal().log(Level.SEVERE, null, e);
        }
    }

    /**
     * Seta as configurações do gerais do jogo
     *
     * @throws RuntimeException Caso algum erro, é passado à classe controladora
     * Nifty
     */
    public void setSettings() throws RuntimeException {
        //todo
    }

    /**
     * Deleta mundo do usuário
     *
     * @param id o id do mundo
     * @throws RuntimeException caso algum erro ocorra passa para a controladora
     * Nifty
     */
    public void deleteWorld(int id) throws RuntimeException {
        //todo
    }

    /**
     * Começa um jogo (world) Baixa os dados do mundo do servidor Para o estado
     * atual Vai para o estado GameAppState
     *
     * @param world o mundo a ser jogado
     * @throws RuntimeException caso algum erro ocorra passa para a controladora
     * Nifty
     */
    public void playWorld(World world) throws RuntimeException {
        //TODO load world
        this.setEnabled(false);
        stateManager.attach(new GameAppState());
    }

    /**
     * Cria um mundo para o usuário
     *
     * @param nome o nome do jogo
     * @param dificult a dificuldade do jogo
     * @return A referência do mundo criado
     * @throws RuntimeException caso algum erro ocorra passa para a controladora
     * Nifty
     */
    public World createWorld(String nome, int dificult) throws RuntimeException {
        //todo
        World world = new World(0, dificult, nome, new Date(System.currentTimeMillis()));
        return world;
    }

    /**
     * Abre uma pagina na web para o rank
     *
     * @throws RuntimeException caso algum erro ocorra passa para a controladora
     * Nifty
     */
    public void openRank() throws RuntimeException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Encerra conexão Volta ao LoginAppState
     *
     * @throws RuntimeException caso algum erro ocorra passa para a controladora
     * Nifty
     */
    public void logout() throws RuntimeException {
        //do logout stuff
        LoggedAppState laState = stateManager.getState(LoggedAppState.class);
        laState.getConnection().endClient();

        stateManager.detach(laState);
        stateManager.detach(this);
        stateManager.attach(new LoginAppState());
    }

    @Override
    public void onTimeout() {
        stateManager.detach(this);
        logout();
    }
}
