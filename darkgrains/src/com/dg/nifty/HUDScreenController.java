/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dg.nifty;

import com.dg.core.states.GameAppState;
import com.dg.game.EventEnum;
import com.dg.game.units.Occurrence;
import com.dg.game.units.OccurrenceTypeEnum;
import com.dg.game.units.internal.CopStateEnum;
import com.dg.game.units.internal.CopTypeEnum;
import com.jme3.app.state.AbstractAppState;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.DroppableDroppedEvent;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.label.LabelControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyInputMapping;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.screen.Screen;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Victor
 */
public class HUDScreenController extends AbstractScreenController implements NiftyInputMapping {

    private GameAppState appState;
    private Element occInfo;

    public HUDScreenController(AbstractAppState appState) {
        this.appState = (GameAppState) appState;
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        super.bind(nifty, screen);
        occInfo = screen.findElementByName("occEvent");
        occInfo.hideWithoutEffect();
        atualizaCopCount(0, 0, 0);
    }

    @Override
    public void onStartScreen() {
    }

    @Override
    public void onEndScreen() {
    }

    public void setTexts(List<String[]> values) {
        for (Iterator<String[]> it = values.iterator(); it.hasNext();) {
            String[] strings = it.next();
            setText(strings[0], strings[1]);
        }
    }
    
    public void setImages(List<String[]> values) {
        for (Iterator<String[]> it = values.iterator(); it.hasNext();) {
            String[] strings = it.next();
            if(strings.length == 2)
                setImage(strings[0], strings[1]);
        }
    }
    
    public void setImage(String id, String local) {
        ImageRenderer imr = screen.findElementByName(id).getRenderer(ImageRenderer.class);
        imr.setImage(nifty.createImage(local, false));
    }
    
    public void openOccurence(Occurrence occ, boolean show) {
        if (show) {
            occInfo.hide();
            
            ArrayList<String[]> list = new ArrayList();
            if (occ.isLetal()) {
                list.add(new String[] {"info","tipo: " + occ.getType().name() + " e letal"});
            } else {
                list.add(new String[] {"info","tipo: " + occ.getType().name() + " e nao letal"});
            }
            list.add(new String[] {"bandidos","Bandidos: " + occ.getBanditQuantity()});
            list.add(new String[] {"pericles","Periculosidade: " + occ.getPericulosidade()});
            setTexts(list);
            
            if (occ.getType() == OccurrenceTypeEnum.DIFICIL) {
                setImage("bandit", "Interface/img/characters/chefebandido.png");
            } else if(occ.getType() == OccurrenceTypeEnum.FACIL) {
                setImage("bandit", "Interface/img/characters/bandido.png");
            } else {
                setImage("bandit", "Interface/img/characters/vOVA.png");
            }
            
            occInfo.show();
        } else {
            if (occInfo.isVisible()) {
                occInfo.hide();
            }
        }
    }

    public void deploy() {
    }
    //botões de menu

    public void tutorialButton() {
        //pause
        appState.setEnabled(false);
        popup("tutorial");
    }

    public void listButton() {
        //pause
        appState.setEnabled(false);
        popup("pause-menu");
    }

    //util
    /**
     * set text for a text element
     *
     * @param id
     * @param value
     */
    public void setText(String id, String value) {
        TextRenderer rd = screen.findElementByName(id).getRenderer(TextRenderer.class);
        rd.setText(value);
    }

    @Override
    public NiftyInputEvent convert(KeyboardInputEvent inputEvent) {
        if (inputEvent.isKeyDown()) {
            if (inputEvent.getKey() == KeyboardInputEvent.KEY_ESCAPE) {
                listButton();
                return NiftyInputEvent.Escape;
            } else if (inputEvent.getKey() == KeyboardInputEvent.KEY_1) {
                //compraCabo();
                return NiftyInputEvent.Activate;
            } else if (inputEvent.getKey() == KeyboardInputEvent.KEY_2) {
                //compraTenente();
                return NiftyInputEvent.Activate;
            } else if (inputEvent.getKey() == KeyboardInputEvent.KEY_3) {
                // compraCoronel();
            } else if (inputEvent.getKey() == KeyboardInputEvent.KEY_F10) {
                tutorialButton();
            } else if (inputEvent.getKey() == KeyboardInputEvent.KEY_F11) {
                listButton();
            }
        }
        return null;
    }

    public void atualizaCopCount(int q1, int q2, int q3) {
        TextRenderer rend;
        rend = screen.findElementByName("cabCount").getRenderer(TextRenderer.class);
        rend.setText("Cabo " + q1);

        rend = screen.findElementByName("tenCount").getRenderer(TextRenderer.class);
        rend.setText("Tenente " + q2);

        rend = screen.findElementByName("corCount").getRenderer(TextRenderer.class);
        rend.setText("Coronel " + q3);
    }

    public void atualizaGrains(int grains) {
        TextRenderer rend;
        rend = screen.findElementByName("grains").getRenderer(TextRenderer.class);
        rend.setText("\\#C1272D#Grains " + grains);
    }

    public void buy(String type) {
        switch (type) {
            case "cab":
                appState.buyCop(CopTypeEnum.CABO);
                break;
            case "ten":
                appState.buyCop(CopTypeEnum.TENENTE);
                break;
            case "cor":
                appState.buyCop(CopTypeEnum.CORONEL);
                break;
        }
    }

    public void sell(String type) {
        switch (type) {
            case "cab":
                appState.sellCop(CopTypeEnum.CABO);
                break;
            case "ten":
                appState.sellCop(CopTypeEnum.TENENTE);
                break;
            case "cor":
                appState.sellCop(CopTypeEnum.CORONEL);
                break;
        }
    }

    public void sendBat(String type) {
        switch (type) {
            case "cab":
                appState.sendBattalion(CopTypeEnum.CABO);
                break;
            case "ten":
                appState.sendBattalion(CopTypeEnum.TENENTE);
                break;
            case "cor":
                appState.sendBattalion(CopTypeEnum.CORONEL);
                break;
        }
    }

    public void jogar() {
        appState.setEnabled(true);
        nifty.closePopup(screen.getTopMostPopup().getId());
    }

    public void sair() {
        appState.quit();
    }
    
    public void showEventMessage(EventEnum type, String[] args) {
        //todo
    }
    
}
