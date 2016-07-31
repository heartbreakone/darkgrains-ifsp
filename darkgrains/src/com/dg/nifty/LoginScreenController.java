package com.dg.nifty;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import com.dg.core.states.LoginAppState;
import com.jme3.app.state.AbstractAppState;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.TextField;

/**
 * Handdle login screen
 *
 * @author Victor
 */
public class LoginScreenController extends AbstractScreenController {

    //referência da classe que a cuida
    private LoginAppState appState;

    public LoginScreenController(AbstractAppState appState) {
        this.appState = (LoginAppState) appState;
    }

    /**
     * Tenta logar, se não exibe popup de erro
     *
     * @param username Nome de usuário
     * @param password Senha
     * @return true se o login foi feito, se não, false
     */
    private boolean loginUser(String username, String password) {
        //todo tratamento de string?
        try {
            appState.loginUser(username, password);
            return true;
        } catch (RuntimeException e) {
            erroMessage = e.getMessage();
            return false;
        }
    }

    //Nifty events
    
    /**
     * seta configurações alteradas
     */
    //@NiftyEventSubscriber(id = "")
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
    //@NiftyEventSubscriber(id = "")
    public void sair() {
        try {
            appState.quit();
        } catch (RuntimeException e) {
            erroMessage = e.getMessage();
            popup("erro");
        }
    }

    /**
     * tenta abrir sessão do user no servidor
     */
    //@NiftyEventSubscriber(id = "")
    public void login() {
        String username = screen.findNiftyControl("username", TextField.class).getRealText();
        String password = screen.findNiftyControl("password", TextField.class).getRealText();

        //todo tratamento de strings?

        if (!loginUser(username, password)) {
            popup("erro");
        }
    }

    /**
     * Tenta cadastrar um novo cliente
     */
    //@NiftyEventSubscriber(id = "")
    public void cadastra() {
        String username = screen.findNiftyControl("usenameCadastro", TextField.class).getRealText();
        String password = screen.findNiftyControl("passwordCadastro", TextField.class).getRealText();
        String email = screen.findNiftyControl("usenameCadastro", TextField.class).getRealText();

        //todo tratamento de strings?

        try {
            appState.createUser(username, password, email);
        } catch (RuntimeException e) {
            erroMessage = e.getMessage();
            popup("erro");
        }
    }
}
