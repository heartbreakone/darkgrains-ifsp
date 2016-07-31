/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dg.nifty;

import com.dg.core.states.MenuAppState;
import com.dg.game.World;
import com.dg.nw.ConnectionHandler;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Victor
 */
public class WorldSelectionScreenController extends AbstractScreenController {

    protected MenuAppState appState;
    private int dificult = 0;
    //private WorldModel created;
    private ListBox<World> worldList;
    private ConnectionHandler connection;

    public boolean create() {
        if (dificult > 0) {
            String nome = screen.getTopMostPopup().findNiftyControl("name-create", TextField.class).getRealText();
            if (!nome.equals("")) {
                try {
                    World w = appState.createWorld(nome, dificult);
                    worldList.addItem(w);
                    worldList.setFocusItem(w);
                    worldList.selectItem(w);
                } catch (RuntimeException e) {
                    erroMessage = e.getMessage();
                    popup("erro");
                    return false;
                }
                popdown();
                return true;
            }
            alertMessage = "Insira um nome ao mundo";
            popup("alert");
            return false;
        }
        alertMessage = "Selecione uma dificuldade.";
        popup("alert");
        return false;
    }

    public WorldSelectionScreenController(MenuAppState appState) {
        this.appState = appState;
    }

    public WorldSelectionScreenController(MenuAppState appState, ConnectionHandler ch) {
        this.appState = appState;
        this.connection = ch;
    }

    @Override
    public void onStartScreen() {
        super.onStartScreen();
        fillWorldList();
    }

    /**
     * Fill the listbox with items. In this case with JustAnExampleModelClass.
     */
    public void fillWorldList() {
        worldList = (ListBox<World>) screen.findNiftyControl("worldList", ListBox.class);
        worldList.removeAllItems(worldList.getItems());
        worldList.addAllItems(this.connection.getWorldList());
    }

    @NiftyEventSubscriber(id = "worldList")
    public void onListBoxSelectionChanged(final String id, final ListBoxSelectionChangedEvent<World> event) {
        List<World> selection = event.getSelection();
        World w = selection.get(0);
        updateWorldInfo(w);
    }

    private void updateWorldInfo(World w) {
        TextRenderer render;
        render = screen.findElementByName("id").getRenderer(TextRenderer.class);
        render.setText("" + w.getId());
        render = screen.findElementByName("name").getRenderer(TextRenderer.class);
        render.setText("" + w.toString());
        render = screen.findElementByName("cr").getRenderer(TextRenderer.class);
        render.setText("" + w.getCrimerate());
        render = screen.findElementByName("date").getRenderer(TextRenderer.class);
        render.setText("" + new SimpleDateFormat("dd/MM/yy:HH:mm").format(w.getCreated()));
        render = screen.findElementByName("dif").getRenderer(TextRenderer.class);
        render.setText("" + w.getDifficulty());

    }

    public void sair() {
        try {
            appState.quit();
        } catch (Exception e) {
            erroMessage = e.getMessage();
            popup("erro");
        }
    }

    public void deletarWorld() {
        try {
            if (worldList.getSelection().size() > 0) {
                World w = worldList.getSelection().get(0);
                connection.deleteWorld(w.getId());
                fillWorldList();
            } else {
                alertMessage = "Selecione um mundo para deletar";
                popup("alert");
            }
        } catch (Exception e) {
            erroMessage = e.getMessage();
            popup("erro");
        }
    }

    public void jogarWorld() {
        List<World> model = worldList.getSelection();
        if (model.size() > 0) {
            try {
//                appState.playWorld(model.get(0));
                //todo
                appState.playWorld(null);
            } catch (RuntimeException e) {
                erroMessage = e.getMessage();
                popup("erro");
            }
        } else {
            alertMessage = "selecione um mundo para jogar";
            popup("alert");
        }
    }

    public void voltar() {
        nifty.gotoScreen("menu");
    }

    public void selectDificult(String dif) {
        int i = Integer.parseInt(dif);
        Element element;
        switch (i) {
            case 1:
                element = screen.getTopMostPopup().findElementByName("facil");
                element.startEffect(EffectEventId.onCustom, null, "select");
                break;
            case 2:
                element = screen.getTopMostPopup().findElementByName("medio");
                element.startEffect(EffectEventId.onCustom, null, "select");
                break;
            case 3:
                element = screen.getTopMostPopup().findElementByName("dificil");
                element.startEffect(EffectEventId.onCustom, null, "select");
                break;
            default:
                break;
        }
        dificult = i;
    }

    public void createAndPlay() {
        if (create()) {
            jogarWorld();
        }
    }

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
}
