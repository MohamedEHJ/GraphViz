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

public class start {


    private static Graph G = null;

    public static void main(String args[]) throws ParserConfigurationException, IOException, SAXException {
        System.out.println("Fonction scrapping GRAPHML");
        File fileChoosen = new File("C:\\Users\\AZ\\Documents\\graphs\\apetit graph.graphml");
        File fileChoosen2 = new File("C:\\Users\\AZ\\Documents\\graphs\\crawl40.graphml");
        G = new Graph(fileChoosen);

        if (G.getNodes().size() == 3) {
            G.getNodes().get(0).setPosX(10);
            G.getNodes().get(0).setPosY(20);

            G.getNodes().get(1).setPosX(10f);
            G.getNodes().get(1).setPosY(30f);

            G.getNodes().get(2).setPosX(15f);
            G.getNodes().get(2).setPosY(30f);

        }

        fruchtermanReingold();

    }


    static void fruchtermanReingold() {

        // Parameter : Graph(node list, edge list), Frame Width, Frame Length, temperature, iteration
        int iteration = 3;
        int frameWidth = 978; // Y
        int frameLength = 638; // X

        int temperature = frameWidth / 10;
        int dt = temperature / iteration;

        ArrayList<Edge> fruchtermanEdge = G.getEdges();

        //TODO Remove the randomize with seed

        // Optimal distance between node
        float optimalDistance = (float) Math.sqrt(((frameLength) * (frameWidth)) / G.getNodes().size());

        System.out.println("Distance optimal = " + optimalDistance);


        // Great loop
        for (int i = 0; i < iteration; i++) {

            // Repulsive forces
            for (Nodes node1 : G.getNodes()) {
                node1.setDisplacementX(0);
                node1.setDisplacementY(0);
                for (Nodes node2 : G.getNodes()) {
                    if (node1 != node2) {

                        // distance between node
                        float distanceX = (node1.getPosX() - node2.getPosX());
                        float distanceY = (node1.getPosY() - node2.getPosY());

                        /**
                         * Problems encountered here with the function "pow(base,exponent)" not to be used
                         * because not compatible with float.
                         */
                        // Distance between node1 and node2
                        float distance = (float) Math.sqrt(distanceX * distanceX + distanceY * distanceY);

                        float repulsion = (optimalDistance * optimalDistance / (distance));    // Repulsion

                        if (distance > 0) {
                            node1.setDisplacementX(node1.getDisplacementX() + (distanceX / (distance)) * repulsion);
                            node1.setDisplacementY(node1.getDisplacementY() + (distanceY / (distance)) * repulsion);
                        }
                    }
                }
            }

            //Attractive forces with edges
            for (Edge edge : G.getEdges()) {
                // get the node associated to the edge.
                Nodes sourceNode = edge.getSrc();
                Nodes targetNode = edge.getTrg();

                float distanceX = (sourceNode.getPosX() - targetNode.getPosX());
                float distanceY = (sourceNode.getPosY() - targetNode.getPosY());

                float distance = (float) Math.sqrt(distanceX * distanceX + distanceY * distanceY);

                float attraction = (distance * distance) / optimalDistance;

                if (distance > 0) {

                    sourceNode.setDisplacementX(sourceNode.getDisplacementX() - (distanceX / (distance)) * attraction);
                    sourceNode.setDisplacementY(sourceNode.getDisplacementY() - (distanceY / (distance)) * attraction);

                    targetNode.setDisplacementX(targetNode.getDisplacementX() - (distanceX / (distance)) * attraction);
                    targetNode.setDisplacementY(targetNode.getDisplacementY() - (distanceY / (distance)) * attraction);
                }

            }
            // We now apply the calculated displacements
            System.out.println("----- FIN ITERATION " + i + " -----");
//            for (Nodes node : G.getNodes()) {
//                float dispX = node.getDisplacementX();
//                float dispY = node.getDisplacementY();
//
//                float disp = (float) Math.sqrt(dispX * dispX + dispY * dispY);
//
//                if (disp > 0) {
////                    System.out.println(node.toString());
//
//                    node.setPosX(node.getPosX() + (dispX / disp) * Math.min(disp, temperature));
//                    node.setPosY(node.getPosY() + (dispY / disp) * Math.min(disp, temperature));
//
//                    node.setPosX(Math.min(frameWidth - 20, Math.max(20, node.getPosX())));
//                    node.setPosY(Math.min(frameLength - 20, Math.max(20, node.getPosY())));
//
//                    if (node.getPosX() > frameWidth || node.getPosY() > frameLength) {
//                        System.out.println("ERROR");
//                    }
//                }
//            }

//            temperature -= temperature / iteration;
            temperature -= dt;


            System.out.println("Itération = " + i);


        }


        System.out.println("\n" + G.getNodes());
        System.out.println();

    }


}
