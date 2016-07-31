/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dg.nifty;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.batch.spi.BatchRenderBackend.Image;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.Screen;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * handdle load screen
 *
 * @author Victor
 */
public class LoadScreenController extends AbstractScreenController {

    private List<String> coolMessages = new ArrayList<>();
    private boolean inLoad = false;
    private Element dotsElement, coolElement;
    private TextRenderer coolRenderer;

    public boolean isRunnig() {
        return inLoad;
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        super.bind(nifty, screen);

        
        coolMessages.add("BOM DIA SENHORES!");
        coolMessages.add("Cavando o cenario.");


    }

    public void startLoad() {
        inLoad = true;
        coolThread.start();
        dotsThread.start();
    }

    private Element createDotElement(Element parent) {
        NiftyImage image = nifty.createImage("Interface/img/menu/btnLoading.png", true);
        ImageBuilder bd = new ImageBuilder();
        Element e = bd.build(nifty, screen, parent);
        e.getRenderer(ImageRenderer.class).setImage(image);
        return e;
    }
    private Thread dotsThread = new Thread(new Runnable() {
        @Override
        public void run() {
            dotsElement = screen.findElementByName("loadbar");
            while (inLoad) {
                try {
                    dotsElement.add(createDotElement(dotsElement));
                    Thread.sleep(1000);
                    dotsElement.add(createDotElement(dotsElement));
                    Thread.sleep(1000);
                    dotsElement.add(createDotElement(dotsElement));
                    Thread.sleep(1000);
                    dotsElement.add(createDotElement(dotsElement));
                    Thread.sleep(1000);
                    dotsElement.add(createDotElement(dotsElement));
                    Thread.sleep(1000);
                    dotsElement.add(createDotElement(dotsElement));
                    Thread.sleep(1000);
                    for (Element e : dotsElement.getElements()) {
                        e.markForRemoval();
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(LoadScreenController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }, "LOADING Dots Thread");
    private Thread coolThread = new Thread(new Runnable() {
        @Override
        public void run() {
            Collections.shuffle(coolMessages);
            coolElement = screen.findElementByName("message");
            coolRenderer = coolElement.getRenderer(TextRenderer.class);
            for (int i = 0; inLoad && i < coolMessages.size(); i++) {
                try {
                    coolRenderer.setText(coolMessages.get(i));
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(LoadScreenController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }, "LOADING Cool Messages Thread");

    public void finishLoading() {
        inLoad = false;
    }
}
