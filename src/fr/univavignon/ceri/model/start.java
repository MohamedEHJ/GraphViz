package fr.univavignon.ceri.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class start  {


    public static void main(String args[]) throws ParserConfigurationException, IOException, SAXException {
        System.out.println("Fonction scrapping GRAPHML");
        File fileChoosen = new File("C:\\Users\\AZ\\Documents\\graphs\\crawl40.graphml");

        //Get Document Builder
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        System.out.println(fileChoosen);
        //Build Document
        Document document = builder.parse(fileChoosen);
        Nodes n = new Nodes();
        Edge e = new Edge();


        //Normalize the XML Structure; It's just too important !!
        document.getDocumentElement().normalize();

        //Here comes the root node
        Element root = document.getDocumentElement();
        System.out.println(root.getNodeName());

        //Get all node and edge
        NodeList nList = document.getElementsByTagName("node");
        NodeList eList = document.getElementsByTagName("edge");

        // ===================================

        //Scrap it from NodeList
        n.nodeScrap(nList);
        e.edgeScrap(eList);

//        n.displayList();
//        e.displayList();
//
//        drawEdge();
//        drawANode();

        Graph G = new Graph();

        // take the node List from the DOM, and put the list of nodes in the "nodes" field of a graph
        G.nodes = (ArrayList<Nodes>) n.nodeScrap(nList);

        // same as above for Edges
        G.edges = (ArrayList<Edge>) e.edgeScrap(eList);

        System.out.println(G.nodes);
        System.out.println(G.edges);
    }
}
