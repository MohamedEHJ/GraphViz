package fr.univavignon.ceri;

import fr.univavignon.ceri.model.Edge;
import fr.univavignon.ceri.model.Graph;
import fr.univavignon.ceri.model.Nodes;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SecondPage {


    public Button btn_back;
    public Label fileName;
    public Pane visualisationWindow;
    public Button fruchterman_reingold;

    int frameLength = 978;
    int frameWidth = 638;

    File fileChoosen;

    ArrayList<Circle> nodes = new ArrayList<>();
    ArrayList<Line> edges = new ArrayList<>();


    /**
     * Manage the button "retour" to get back to the first window.
     *
     * @param actionEvent
     * @throws IOException
     */
    @FXML
    public void retour(ActionEvent actionEvent) throws IOException {
        System.out.println("You clicked 'Retour' Button!");
        Parent first_page_parent = FXMLLoader.load(getClass().getResource("main_page.fxml"));
        Scene first_page_scene = new Scene(first_page_parent);
        Stage app_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        if (true) {
            // here we should verify the extension of the file. The button would be lock if the format is not good,
            // else we will be able to change scene
            app_stage.setScene(first_page_scene);
            app_stage.show();
        }
    }

    /**
     * Receive the file choosen in the first window.
     *
     * @param graphML
     */
    Task task;

    public void receiveFile(File graphML) throws IOException, SAXException, ParserConfigurationException {
        fileChoosen = graphML;
        System.out.println("file choosen: " + fileChoosen.getName());
        fileName.setText(fileChoosen.getName());

        xmlInit();

//        fruchtermanReingold();

    }


    /**
     * read and init the graphML file by creating a list of edge and a list of node.
     */
    Graph G;

    private void xmlInit() throws ParserConfigurationException, IOException, SAXException {
        G = new Graph(fileChoosen);
        G.randomizeNodes();

        drawEdge();
        drawANode();
    }


    /**
     * Draw nodes from graph G using node list.
     */
    void drawANode() {
        // width = 1003
        // height = 668

//        visualisationWindow.getChildren().clear();
        Circle d = new Circle(10, Color.RED);
        d.setCenterX(1000);
        d.setCenterY(660);
        visualisationWindow.getChildren().add(d);

        for (Nodes node : G.getNodes()) {
            Circle c = new Circle(10, Color.TURQUOISE);
            nodes.add(c);
            c.setCenterX(node.getPosX());
            c.setCenterY(node.getPosY());
            visualisationWindow.getChildren().add(c);
        }


    }

    /**
     * Draw graph using edge list from the graph G.
     */
    void drawEdge() {

        visualisationWindow.getChildren().clear();

        for (Edge edge : G.getEdges()) {
            Line line = new Line();
            edges.add(line);
            line.setStartX(edge.getSrc().getPosX());
            line.setStartY(edge.getSrc().getPosY());

            line.setEndX(edge.getTrg().getPosX());
            line.setEndY(edge.getTrg().getPosY());

            line.setStrokeWidth(edge.getPoids());

            visualisationWindow.getChildren().add(line);
        }

    }

    float attraction(float d, float optimalDistance) {
        return (d * d) / optimalDistance;
    }

    float repulsion(float d, float optimalDistance) {
        return -(optimalDistance * optimalDistance) / d;
    }

    void fruchtermanReingold() {

        ArrayList<Nodes> toDisplay = G.getNodes();
        System.out.println("*********" + G.getNodes());
        // Parameter : Graph(node list, edge list), Frame Width, Frame Length, temperature, iteration
        int iteration = 10;
        int temperature = frameWidth / 10;
        int dt = temperature / iteration + 1;

        // Initialisation
        G.randomizeNodes();  // Randomly place Node in the frame.

        // Optimal distance between node
        float optimalDistance = (float) Math.sqrt((frameLength * frameWidth) / G.getNodes().size());

        System.out.println("Distance optimal = " + optimalDistance);

        float deplacementX = 0;
        float deplacementY = 0;

        float sourceDeplacementX = 0;
        float sourceDeplacementY = 0;

        float targetDeplacementX = 0;
        float targetDeplacementY = 0;

        float max = (float) Math.sqrt((frameWidth * frameLength) / 10f);


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
                            node1.setDisplacementX(node2.getDisplacementX() + (distanceX / Math.abs(distance)) * repulsion);
                            node1.setDisplacementY(node2.getDisplacementY() + (distanceY / Math.abs(distance)) * repulsion);
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

                    sourceNode.setDisplacementX(sourceNode.getDisplacementX() - distanceX / Math.abs(distance) * attraction);
                    sourceNode.setDisplacementY(sourceNode.getDisplacementY() - distanceY / Math.abs(distance) * attraction);

                    targetNode.setDisplacementX(targetNode.getDisplacementX() - distanceX / Math.abs(distance) * attraction);
                    targetNode.setDisplacementY(targetNode.getDisplacementY() - distanceY / Math.abs(distance) * attraction);


                }


                // We now apply the calculated displacements
            }

            for (Nodes node : G.getNodes()) {
                float dispX = node.getDisplacementX();
                float dispY = node.getDisplacementY();

                float disp = (float) Math.sqrt(dispX * dispX + dispY * dispY);

                if (disp > 0) {
//                    System.out.println(node.toString());

                    float posX = (node.getPosX() + dispX / disp * Math.min(dispX, temperature));
                    float posY = (node.getPosY() + dispY / disp * Math.min(dispY, temperature));

                    node.setPosX(Math.min(frameWidth / 2, Math.max((-frameWidth) / 2, posX)));
                    node.setPosY(Math.min(frameLength / 2, Math.max((-frameLength) / 2, posY)));

//                    System.out.println("posX = " + posX);
//                    System.out.println("posY = " + posY);
//                    System.out.println("température = " + temperature);
//                    System.out.println();
                }
                System.out.println(G.getNodes());
            }

            temperature -= temperature / iteration;

//            System.out.println("température : " + temperature);

//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
        System.out.println("end of loop");

        drawEdge();
        drawANode();

    }


    public void fruchtermanReingoldButton(ActionEvent actionEvent) {
        fruchtermanReingold();
    }


}
