/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dg.core.path;

import com.jme3.math.Vector2f;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.alg.FloydWarshallShortestPaths;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.GraphPathImpl;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Luís
 */
public class StreetMesh {

    private String fileName;
    DefaultDirectedGraph<Node, DefaultEdge> graph;
    private List<Node> nodes;
    private List<Connection> connections;
    private FloydWarshallShortestPaths paths;

    public StreetMesh(JSONObject model) {
        this.nodes = new ArrayList<>();
        this.connections = new ArrayList<>();

        this.graph = new DefaultDirectedGraph<>(DefaultEdge.class);
                
        JSONArray jNodes = (JSONArray) model.get("nodes");
        for (Object o : jNodes) {
            JSONObject jo = (JSONObject) o;

            float x = ((Double) jo.get("x")).floatValue();
            float y = ((Double) jo.get("y")).floatValue();
            int id = ((Long) jo.get("id")).intValue();

            Node n = new Node(new Vector2f(x, y), id);
            nodes.add(n);
            graph.addVertex(n);
        }

        JSONArray jConnections = (JSONArray) model.get("connections");
        for (Object o : jConnections) {
            JSONObject jo = (JSONObject) o;

            Node a = getNodeById(((Long) jo.get("a")).intValue());
            Node b = getNodeById(((Long) jo.get("b")).intValue());
            int id = ((Long) jo.get("id")).intValue();

            Connection c = new Connection(a, b, id);
            connections.add(c);

            DefaultEdge e = graph.addEdge(a, b);
//            graph.setEdgeWeight(e, a.distanceTo(b));

        }
        paths = new FloydWarshallShortestPaths(graph);
        graph = (DefaultDirectedGraph<Node, DefaultEdge>) paths.getGraph();
//      
    }

    public Node getBaseNode() {
        return nodes.get(132);
    }

    public List<Node> getShortestPath(Node from, Node to) {
        List<Node> list = new ArrayList<>();
        List<DefaultEdge> shortest_path;
        GraphPath g = paths.getShortestPath(from, to);
        
        if (g != null) {
            shortest_path = g.getEdgeList();
            Logger.getGlobal().log(Level.INFO, "Menor rota calculada. Distância: {3}\n\tDe {1} a {2}\n\t {0}", new Object[]{shortest_path, from, to, paths.shortestDistance(from, to)});
            for (DefaultEdge e : shortest_path) {
                list.add(graph.getEdgeTarget(e));
            }
        } else {
            Logger.getGlobal().log(Level.SEVERE, "CAMINHO NÃO ENCONTRADO. de {0} a {1}", new Object[]{from, to});
            throw new RuntimeException("Erro ao obter shortest path");
        }
        return list;
    }

    public Vector2f getRandPos() {
        return getRandNode().getPosition();
    }

    private Node getNodeById(int id) {
        for (Node n : nodes) {
            if (n.getId() == id) {
                return n;
            }
        }
        return null;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public DefaultDirectedGraph<Node, DefaultEdge> getGraph() {
        return graph;
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public Node getRandNode() {
        return (Node) nodes.toArray()[new Random().nextInt(nodes.size())];
    }
}
