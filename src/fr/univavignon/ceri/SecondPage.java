package fr.univavignon.ceri;

import fr.univavignon.ceri.model.Edge;
import fr.univavignon.ceri.model.Graph;
import fr.univavignon.ceri.model.Nodes;
import fr.univavignon.ceri.model.PageRank;
import javafx.animation.PauseTransition;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class SecondPage {


    public Button btn_back;
    public Label fileName;
    public Pane visualisationWindow;
    public Button fruchterman_reingold;

    public Text ALogCentrNom;
    public Text AlgoCentrPremier;
    public Text AlgoCentrDernier;

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
    Task task;

    public void receiveFile(File graphML) throws IOException, SAXException, ParserConfigurationException {
        fileChoosen = graphML;
        System.out.println("file choosen: " + fileChoosen.getName());
        fileName.setText(fileChoosen.getName());

        xmlInit();
        displayWeightedPageRank();
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
        d.setCenterY(0);
        visualisationWindow.getChildren().add(d);
        ArrayList<Color> color = new ArrayList<>();

        color.add(Color.RED);
        color.add(Color.GREEN);
        color.add(Color.BLUE);
        color.add(Color.ORANGE);
        color.add(Color.YELLOW);

        Map<Color, String> colors = new LinkedHashMap<>();
        colors.put(Color.RED, "RED");
        colors.put(Color.GREEN, "GREEN");
        colors.put(Color.BLUE, "BLUE");
        colors.put(Color.YELLOW, "YELLOW");
        colors.put(Color.ORANGE, "ORANGE");

        int i = 0;
        for (Nodes node : G.getNodes()) {
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
            System.out.println();
            i++;
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

            if (edge.getPoids() != 1) {
                line.setStrokeWidth(edge.getPoids());
            } else {
                line.setStrokeWidth(edge.getPoids());
            }

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
        int iteration = 20;
        int temperature = 5;
        int dt = temperature / iteration + 1;

        // Initialisation
        G.randomizeNodes();  // Randomly place Node in the frame.

        // Optimal distance between node
        float optimalDistance = (float) Math.sqrt(((frameLength - 20) * (frameWidth - 20)) / G.getNodes().size());

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
                            node1.setDisplacementX(node2.getDisplacementX() + (distanceX / (distance)) * repulsion);
                            node1.setDisplacementY(node2.getDisplacementY() + (distanceY / (distance)) * repulsion);
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
                // We now apply the calculated displacements
            }

            for (Nodes node : G.getNodes()) {
                float dispX = node.getDisplacementX();
                float dispY = node.getDisplacementY();

                float disp = (float) Math.sqrt(dispX * dispX + dispY * dispY);

                if (disp > 0) {
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
//            temperature -= dt;


        }
        drawEdge();
        drawANode();
        System.out.println("\n" + G.getNodes());


    }


    public void fruchtermanReingoldButton(ActionEvent actionEvent) {
        fruchtermanReingold();
    }

    void displayWeightedPageRank(){
        PageRank tmp = new PageRank();
        tmp.fillListeES(G.getEdges(), G.getNodes());
        ArrayList<String> links = tmp.sortPR();

        ALogCentrNom.setText("WeightedPageRank");
        AlgoCentrPremier.setText(links.get(0));
        AlgoCentrDernier.setText(links.get(links.size() - 1));

    }


}
