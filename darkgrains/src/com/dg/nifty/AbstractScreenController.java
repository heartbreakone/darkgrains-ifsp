/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dg.nifty;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe que generaliza os popups para as ScreenControllers
 * @author Victor
 */
public class AbstractScreenController implements ScreenController {

    protected Nifty nifty;
    protected Screen screen;
    protected String erroMessage = "erro", alertMessage = "alerta";
    protected Element loadPopup;

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
        //TODO \/
        loadPopup = nifty.createPopup("load");
    }

    @Override
    public void onStartScreen() {
    }

    @Override
    public void onEndScreen() {
    }

    /**
     * Sobe um popup na tela atual
     *
     * @param name id da popup no Nifty XML
     */
    public void popup(final String name) {
        Element popupElement = nifty.createPopup(name);
        nifty.showPopup(nifty.getCurrentScreen(), popupElement.getId(), null);
        Logger.getGlobal().log(Level.INFO, "POPUP: {0}", name);
    }

    /**
     * Fecha o popup mais alto disponível
     */
    public void popdown() {
        Element top = screen.getTopMostPopup();
        nifty.closePopup(top.getId());
        Logger.getGlobal().log(Level.INFO, "POPDOWN: {0}", top.getId());
    }

    //médodos para chamada no Nifty XML
    public String getErroMessage() {
        return this.erroMessage;
    }

    public String getAlertMessage() {
        return this.alertMessage;
    }

    //TODO \/
    public void startLoadPop() {
        nifty.showPopup(screen, loadPopup.getId(), null);
        Logger.getGlobal().log(Level.INFO, "POPUP: {0}", loadPopup.getId());
    }

    public void closeLoadPop() {
        nifty.closePopup(loadPopup.getId());
        Logger.getGlobal().log(Level.INFO, "POPDOWN: {0}", loadPopup.getId());
    }
}
