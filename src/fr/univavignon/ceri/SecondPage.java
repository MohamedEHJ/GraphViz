package fr.univavignon.ceri;

import fr.univavignon.ceri.model.Edge;
import fr.univavignon.ceri.model.Graph;
import fr.univavignon.ceri.model.Nodes;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
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
import javafx.util.Duration;
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

    int frameWidth = 978; // Y
    int frameLength = 638; // X

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
        G.randomizeNodesWithSeed();

        drawEdges(G);
        drawNodes(G);
    }


    /**
     * Draw nodes from graph G using node list.
     *
     * @param g
     */
    void drawNodes(Graph g) {
        // width = 1003
        // height = 668

//        visualisationWindow.getChildren().clear();
        int i = 0;
        for (Nodes node : g.getNodes()) {
            Color clr;
            if (node.getId().equals("6") || node.getId().equals("0")) {
                clr = Color.RED;
            } else {
                clr = Color.ORANGE;
            }

            Circle c = new Circle(5, clr);
            nodes.add(c);
            c.setCenterX(node.getPosX());
            c.setCenterY(node.getPosY());

            visualisationWindow.getChildren().add(c);
            i++;
        }


    }

    /**
     * Draw graph using edge list from the graph G.
     *
     * @param g
     */
    void drawEdges(Graph g) {
        /**
         * Initalisation pour test.
         */
        visualisationWindow.getChildren().clear();

        for (Edge edge : g.getEdges()) {
            Line line = new Line();
            edges.add(line);
            line.setStartX(edge.getSrc().getPosX());
            line.setStartY(edge.getSrc().getPosY());

            line.setEndX(edge.getTrg().getPosX());
            line.setEndY(edge.getTrg().getPosY());

            if (edge.getPoids() != 1) {
                line.setStrokeWidth(edge.getPoids());
            } else {
                line.setStrokeWidth(edge.getPoids());
            }

            visualisationWindow.getChildren().add(line);
        }

    }

    void fruchtermanReingold(Graph g) {
        // Parameter : Graph(node list, edge list), Frame Width, Frame Length, temperature, iteration
        int iteration = 100;
        float temperature = frameWidth / 10;
        float dt = temperature / iteration;

        // Initialisation
//        G.randomizeNodes();  // Randomly place Node in the frame.

        //TODO Remove the randomize with seed

        // Optimal distance between node
        float optimalDistance = (float) Math.sqrt(((frameLength) * (frameWidth)) / G.getNodes().size());

        System.out.println("Distance optimal = " + optimalDistance);


        // Great loop
        for (int i = 0; i < iteration; i++) {
//            System.out.println(g.getEdges());
            System.out.println(g.getNodes());

            // Repulsive forces
            for (Nodes node1 : g.getNodes()) {
                node1.setDisplacementX(0);
                node1.setDisplacementY(0);
                for (Nodes node2 : g.getNodes()) {
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

                        if (distance != 0) {
                            node1.setDisplacementX(node1.getDisplacementX() + (distanceX / (distance)) * repulsion);
                            node1.setDisplacementY(node1.getDisplacementY() + (distanceY / (distance)) * repulsion);

                        }
                    }
                }
            }

            //Attractive forces with edges
            for (Edge edge : g.getEdges()) {
                // get the node associated to the edge.
                Nodes sourceNode = edge.src;
                Nodes targetNode = edge.trg;

                float distanceX = (sourceNode.getPosX() - targetNode.getPosX());
                float distanceY = (sourceNode.getPosY() - targetNode.getPosY());

                float distance = (float) Math.sqrt(distanceX * distanceX + distanceY * distanceY);

                float attraction = (distance * distance) / optimalDistance;
                System.out.println("attraction : " + attraction);

                if (distance != 0) {
                    sourceNode.setDisplacementX(sourceNode.getDisplacementX() - (distanceX / (distance)) * attraction);
                    sourceNode.setDisplacementY(sourceNode.getDisplacementY() - (distanceY / (distance)) * attraction);

                    targetNode.setDisplacementX(targetNode.getDisplacementX() + (distanceX / (distance)) * attraction);
                    targetNode.setDisplacementY(targetNode.getDisplacementY() + (distanceY / (distance)) * attraction);
                }

            }

            for (Nodes node : g.getNodes()) {
                float dispX = node.getDisplacementX();
                float dispY = node.getDisplacementY();

                float disp = (float) Math.sqrt(dispX * dispX + dispY * dispY);

                if (disp != 0) {
//                    System.out.println(node.toString());

                    node.setPosX(node.getPosX() + (dispX / disp) * Math.min(disp, temperature));
                    node.setPosY(node.getPosY() + (dispY / disp) * Math.min(disp, temperature));

                    node.setPosX(Math.min(frameWidth, Math.max(20, node.getPosX())));
                    node.setPosY(Math.min(frameLength, Math.max(20, node.getPosY())));

                    if (node.getPosX() > frameWidth || node.getPosY() > frameLength) {
                        System.out.println("ERROR");
                    }
                }
            }

//            temperature -= temperature / iteration;
            temperature -= dt;

            System.out.println(g.getNodes());
            System.out.println("température : " + temperature);
            System.out.println("------------------------------");
        }


        System.out.println("\n" + G.getNodes());
        drawEdges(g);
        drawNodes(g);

    }


    /**
     * Animation via Button.
     *
     * @param actionEvent
     */
    public void fruchtermanReingoldButton(ActionEvent actionEvent) {
//        fruchtermanReingold(G);
        animation();
//        fruchtermanReingoldAnimation();
    }

    /**
     * Setup timeline for the animation.
     * We can change the duration of a frame.
     */
    private void animation() {
        final Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new EventHandler() {
                    @Override
                    public void handle(Event event) {
                        fruchtermanReingoldAnimation();
                    }
                }),
                new KeyFrame(Duration.millis(150))
        );

        timeline.setCycleCount(iteration);

        timeline.play();
    }

    /**
     * Variable initialisation
     */
    float temperature = frameWidth / 10;
    int iteration = 100;
    float dt = temperature / iteration;

    private void fruchtermanReingoldAnimation() {

        drawEdges(G);
        drawNodes(G);

        //TODO Remove the randomize with seed

        // Optimal distance between node
        float optimalDistance = (float) Math.sqrt(((frameLength) * (frameWidth)) / G.getNodes().size());


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

                    if (distance != 0) {
                        node1.setDisplacementX(node1.getDisplacementX() + (distanceX / (distance)) * repulsion);
                        node1.setDisplacementY(node1.getDisplacementY() + (distanceY / (distance)) * repulsion);

                    }
                }
            }
        }

        // Attractive forces with edges
        for (Edge edge : G.getEdges()) {
            // get the node associated to the edge.
            Nodes sourceNode = edge.src;
            Nodes targetNode = edge.trg;

            float distanceX = (sourceNode.getPosX() - targetNode.getPosX());
            float distanceY = (sourceNode.getPosY() - targetNode.getPosY());

            float distance = (float) Math.sqrt(distanceX * distanceX + distanceY * distanceY);

            float attraction = (distance * distance) / optimalDistance;

            if (distance != 0) {
                sourceNode.setDisplacementX(sourceNode.getDisplacementX() - (distanceX / (distance)) * attraction);
                sourceNode.setDisplacementY(sourceNode.getDisplacementY() - (distanceY / (distance)) * attraction);

                targetNode.setDisplacementX(targetNode.getDisplacementX() + (distanceX / (distance)) * attraction);
                targetNode.setDisplacementY(targetNode.getDisplacementY() + (distanceY / (distance)) * attraction);
            }

        }

        // Apply position
        for (Nodes node : G.getNodes()) {
            float dispX = node.getDisplacementX();
            float dispY = node.getDisplacementY();

            float disp = (float) Math.sqrt(dispX * dispX + dispY * dispY);

            if (disp != 0) {
//                    System.out.println(node.toString());

                node.setPosX(node.getPosX() + (dispX / disp) * Math.min(disp, temperature));
                node.setPosY(node.getPosY() + (dispY / disp) * Math.min(disp, temperature));

                node.setPosX(Math.min(frameWidth - 20, Math.max(20, node.getPosX())));
                node.setPosY(Math.min(frameLength - 20, Math.max(20, node.getPosY())));

                if (node.getPosX() > frameWidth || node.getPosY() > frameLength) {
                    System.out.println("ERROR");
                }
            }
        }

//            temperature -= temperature / iteration;
        temperature -= dt;
        System.out.println("température " + temperature);
        drawEdges(G);
        drawNodes(G);


    }


}
