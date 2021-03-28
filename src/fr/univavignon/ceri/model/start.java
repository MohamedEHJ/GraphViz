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
        File fileChoosen = new File("C:\\Users\\AZ\\Documents\\graphs\\web532.graphml");

        Graph G = new Graph(fileChoosen);

        System.out.println(G.nodes);
        System.out.println(G.edges);
    }
}
