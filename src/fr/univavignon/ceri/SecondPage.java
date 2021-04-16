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
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;

public class SecondPage {

    // JAVAFX
    public Button btn_back;
    public Label fileName;
    public Pane visualisationWindow;
    public Button fruchterman_reingold;
    public Text nbIteration;
    public AnchorPane ap;
    public Button btn_stop;
    public Text step;


    // Properties
    int frameWidth = 978; // Y
    int frameLength = 638; // X
    float temperature = frameWidth / 10f;
    int iteration;
    float dt;
    int x = 1;
    File fileChoosen;


    ArrayList<Circle> nodes = new ArrayList<>();
    ArrayList<Line> edges = new ArrayList<>();


    public void setIteration(int iteration) {
        this.iteration = iteration;
    }


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
            timeline.stop();

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
        btn_stop.setDisable(true);

        xmlInit();

    }

    /**
     * init value of decrementation for temperature, init label "number of iteration" and launch spatialisation.
     */
    public void initialisationBeforeLaunch() {
        nbIteration.setText(String.valueOf(iteration));
        dt = temperature / iteration;
        btn_stop.setDisable(false);
        btn_back.setDisable(true);
        animation();
        fruchterman_reingold.setDisable(true);
    }


    /**
     * read and init the graphML file by creating a list of edge and a list of node.
     */
    Graph G;
    private void xmlInit() throws ParserConfigurationException, IOException, SAXException {
        G = new Graph(fileChoosen);
        G.randomizeNodesWithSeed();

        nbIteration.setText(String.valueOf(iteration));

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

        for (Nodes node : g.getNodes()) {
            Color clr;
            if (node.getId().equals("6") || node.getId().equals("0")) {
                clr = Color.RED;
            } else {
                clr = Color.ORANGE;
            }

            Circle c = new Circle(2, clr);
            nodes.add(c);
            c.setCenterX(node.getPosX());
            c.setCenterY(node.getPosY());

            visualisationWindow.getChildren().add(c);

        }


    }

    /**
     * Draw graph using edge list from the graph G.
     *
     * @param g
     */
    void drawEdges(Graph g) {
        visualisationWindow.getChildren().clear();

        for (Edge edge : g.getEdges()) {
            Line line = new Line();
            edges.add(line);
            line.setStartX(edge.getSrc().getPosX());
            line.setStartY(edge.getSrc().getPosY());

            line.setEndX(edge.getTrg().getPosX());
            line.setEndY(edge.getTrg().getPosY());

            line.setStrokeWidth(edge.getPoids() * 0.1);

            visualisationWindow.getChildren().add(line);
        }

    }



    /**
     * Open a window to choose number of iteration.
     *
     * @param actionEvent
     */
    public void fruchtermanReingoldButton(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("configurationSpatialisation.fxml"));
        AnchorPane newWindow = (AnchorPane) loader.load();
        IterationChoose controller = loader.getController();
        controller.setMainWindow(this);
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(fruchterman_reingold.getScene().getWindow());
        Scene scene = new Scene(newWindow);
        stage.setScene(scene);
        stage.show();
    }


    /**
     * Setup timeline for the animation.
     * We can change the duration of a frame by changing argument on line "new KeyFrame(...)".
     */
    Timeline timeline;
    private void animation() {
        timeline = new Timeline(
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
     * Fruchterman reingold implementation
     */
    private void fruchtermanReingoldAnimation() {
        System.out.println("itération : " + iteration);
        System.out.println("température : " + temperature);
        System.out.println("dt = " + dt);
        drawEdges(G);
        drawNodes(G);

        // Optimal distance between node
        float optimalDistance = (float) Math.sqrt(((frameLength) * (frameWidth)) / (float) G.getNodes().size());


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

/*        // Gravity
        for (Nodes node : G.getNodes()) {
            float d = (float) Math.sqrt(node.displacementX * node.displacementX + node.getDisplacementY() * node.getDisplacementY());
            float gf = 0.01f * optimalDistance * 0.5f * d;
            node.displacementX -= gf * node.getPosX() / d;
            node.displacementY -= gf * node.getPosY() / d;
        }*/


        // Apply position
        for (Nodes node : G.getNodes()) {
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
        System.out.println("température " + temperature);
        drawEdges(G);
        drawNodes(G);
//        System.out.println("itération numéro : " + x++);
        step.setText(String.valueOf(x++));


    }

    public void stopTimeline(ActionEvent actionEvent) {
        timeline.stop();
        btn_stop.setDisable(true);
        btn_back.setDisable(false);
    }
}
