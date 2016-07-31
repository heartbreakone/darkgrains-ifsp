/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dg.mapparser;

import com.dg.mapparser.model.Pair;
import com.dg.mapparser.model.Block;
import com.dg.mapparser.model.Connection;
import com.dg.mapparser.model.Spot;
import com.dg.mapparser.model.StreetArray;
import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Random;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * Quick static application for SVG -> Raw map conversion
 * @author Luís
 */
public final class Application {
    public Application() {
        try {
            String filename = JOptionPane.showInputDialog("Insira o nome do arquivo, ou caminho absoluto do arquivo SVG contendo os dados para gerar o nível:");
        
            if(filename == null) {
                JOptionPane.showMessageDialog(null, "Você selecionou sair.");
                return;
            }

            File file = new File(filename);

            // Criação do documento local do nosso arquivo XML
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            
            // Normalização do documento (multi-line strings)
            doc.getDocumentElement().normalize();
            
            
            NodeList g = doc.getDocumentElement().getElementsByTagName("g");
            if(g.getLength() == 0) {
                JOptionPane.showMessageDialog(null, "O mapa não contém nenhum grupo.");
                return;
            }
            
            StreetArray sa = new StreetArray();

            for(int i = 0; i < g.getLength(); i++) {
                Element e = (Element) g.item(i);
                if(e.getAttribute("id").equals("dg-paths")) {
                    processStreet(sa, e);
                }
            }
            
            for(int j = 0; j < g.getLength(); j++) {
                Element e = (Element) g.item(j);
                if(e.getAttribute("id").equals("dg-blocks")) {
                    processBlock(sa, e);
                }
            } 
       
            String out = filename + "-" + new Date().getTime() / 1000 + ".map";
            PrintWriter writer = new PrintWriter(out, "UTF-8");
            writer.println(sa.toJSON().toJSONString());
            writer.close();

        } catch (HeadlessException | ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Application application = new Application();
    }
    
    /**
     * Retrieves current node reference for supplied (x,y) 
     * OR instantiates a new Pair containing those values and returns it.
     * @param st current StreetArray object. This may be altered until the end of the code
     * @param x x value
     * @param y y value
     * @return An value-unique Pair, contained in the referenced StreetArrau
     */
    private int currNodeId = 0;
    public Spot getNode(StreetArray st, Double x, Double y) {
        Pair<Double, Double> p = new Pair<> (x, y);
        
        for(Spot spot : st.nodes) {
            if(spot.equals(p)) {
                return spot;
            }
        }
        
        Spot r = new Spot(x, y, ++currNodeId);
        st.nodes.add(r);
        return r;
    }
    
    /**
     * Inserts Connection into Street Array
     * @param st
     * @param p1
     * @param p2
     * @return
     */
    public void insertConnection(StreetArray st, Spot p1, Spot p2) {
        st.connections.add(new Connection(p1, p2));
    }
    
    public void insertBlock(StreetArray st, Double x, Double y, Double width, Double height, Double color) {
        st.blocks.add(new Block(x, y, width, height, color));
    }
    
    public Double rgbToDouble(String in) {
        in = in.replace("#", "");
        int r = Integer.parseInt(in.substring(0, 2), 16),
            g = Integer.parseInt(in.substring(2, 4), 16),
            b = Integer.parseInt(in.substring(4, 6), 16);
        
        int gray = (r + g + b) / 3;
        return 1 - gray / 255.0;
    }
    
    public void processStreet(StreetArray st, Element e) {
        NodeList nl = e.getElementsByTagName("line");
        for(int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);
            Element z = (Element) n;
            
            NamedNodeMap nnm = n.getAttributes();
            Spot p1, p2;
            
            p1 = getNode(st, Double.parseDouble(nnm.getNamedItem("x1").getNodeValue()), Double.parseDouble(nnm.getNamedItem("y1").getNodeValue()));
            p2 = getNode(st, Double.parseDouble(nnm.getNamedItem("x2").getNodeValue()), Double.parseDouble(nnm.getNamedItem("y2").getNodeValue()));
            System.out.println(p1.toString() + p2.toString());
            
            insertConnection(st, p1, p2);
        }    
    }
    
    public void processBlock(StreetArray st, Element e) {
        NodeList nl = e.getElementsByTagName("rect");
        for(int i = 0; i < nl.getLength(); i++) {
            Node s = nl.item(i);
            NamedNodeMap nnm = s.getAttributes();

            Node fill = nnm.getNamedItem("fill");
            Double color = fill == null ? 1.0 : rgbToDouble(fill.getNodeValue());
            
            insertBlock(
                    st, 
                    Double.parseDouble(nnm.getNamedItem("x").getNodeValue()),
                    Double.parseDouble(nnm.getNamedItem("y").getNodeValue()),
                    Double.parseDouble(nnm.getNamedItem("width").getNodeValue()),
                    Double.parseDouble(nnm.getNamedItem("height").getNodeValue()),
                    color
            );
        }
    }
}
