package fr.univavignon.ceri.model;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Graph {
    ArrayList<Nodes> nodes;
    ArrayList<Edge> edges;

    public ArrayList<Nodes> getNodes() {
        return nodes;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    /**
     * Create a graph.
     *
     * @param fileChoosen GRAPHML file
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public Graph(File fileChoosen) throws IOException, SAXException, ParserConfigurationException {
        Document document = xmlInitialisation(fileChoosen);
        graphCreator(document);
    }

    /**
     * Read a GRAPHML file and create the document of this file
     *
     * @param fileChosen The graphml we are parsing.
     * @return document    Document of this file once normalize
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    Document xmlInitialisation(File fileChosen) throws ParserConfigurationException, IOException, SAXException {
        //Get Document Builder
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        //Build Document
        Document document = builder.parse(fileChosen);

        document.getDocumentElement().normalize();


        return document;

    }

    /**
     * Initialise the graph by creating a list of nodes and a list of edges.
     *
     * @param document creating from the graphml file, using to get nodes and edges
     */
    void graphCreator(Document document) {
        System.out.println("Creating graph...");


        NodeList nList = document.getElementsByTagName("node");
        NodeList eList = document.getElementsByTagName("edge");

        // take the node List from the DOM, and put the list of nodes in the "nodes" field of a graph
        this.nodes = (ArrayList<Nodes>) Nodes.nodeScrap(nList);

        // same as above for Edges
        this.edges = (ArrayList<Edge>) Edge.edgeScrap(eList);

        // Attribute for each edges a node for source and a node for target.
        for (Edge edge : edges) {
            int sourceInteger = edge.getSource();
            int targetInteger = edge.getTarget();

            for (Nodes node : nodes) {
                if (Integer.parseInt(node.getId()) == sourceInteger) {

                    edge.setSrc(node);
                }
                if (Integer.parseInt(node.getId()) == targetInteger) {
                    edge.setTrg(node);
                }
            }

        }

    }

    /**
     * Place node randomly by attributing random x and y position.
     * Also set the good edge position.
     */
    public void randomizeNodes() {
        System.out.println("Randomly place nodes");
        for (Nodes node : nodes) {
//            System.out.println("x = " + node.getPosX() + " y = " + node.getPosY());
            // Place node randomly.
            node.setPosX((int) (10 + Math.random() * (998 - 20)));
            node.setPosY((int) (10 + Math.random() * (658 - 20)));

            // Place edge to their source and target node.
            for (Edge edge : edges) {
                edge.setSourcePosX(edge.getSrc().getPosX());
                edge.setSourcePosY(edge.getSrc().getPosY());

                edge.setTargetPosX(edge.getTrg().getPosX());
                edge.setTargetPosY(edge.getTrg().getPosY());
            }
        }
    }


}
