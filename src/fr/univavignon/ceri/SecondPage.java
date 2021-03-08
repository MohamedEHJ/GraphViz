package fr.univavignon.ceri;

import fr.univavignon.ceri.model.Edge;
import fr.univavignon.ceri.model.Nodes;
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
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

public class SecondPage {


    public Button btn_back;
    public Label fileName;
    public Pane visualisationWindow;


    File fileChoosen;
    Nodes n = new Nodes();
    Edge e = new Edge();

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
    }

    /**
     * read and init the graphML file by creating a list of edge and a list of node.
     *
     */
    private void xmlInit() throws ParserConfigurationException, IOException, SAXException {
        System.out.println("PARSING THE GRAPHML FILE");

        //Get Document Builder
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        //Build Document
        Document document = builder.parse(fileChoosen);

        //Normalize the XML Structure; It's just too important !!
        document.getDocumentElement().normalize();

        //Here comes the root node
        Element root = document.getDocumentElement();
        System.out.println(root.getNodeName());

        //Get all node and edge
        NodeList nList = document.getElementsByTagName("node");
        NodeList eList = document.getElementsByTagName("edge");

        // ===================================
        /**
         * Here i put this 2 function in comment to allow the execution
         */
//        n.nodeScrap(nList);
//        e.edgeScrap(eList);

//        n.displayList();
//        e.displayList();

        drawEdge();
        drawANode();
    }


    /**
     * Draw a random graph in the frame visualisationWindow
     */
    void drawANode() {
//        Circle c = new Circle(10, Color.BEIGE);
//        visualisationWindow.getChildren().add(c);
//
//        int x = 300;
//        int y = 100;
//        c.setCenterX(100);
//
//        System.out.println(n.nodesList.size());
//
//        for (int i = 0; i < n.nodesList.size(); i++) {
//            Circle d = new Circle(10, Color.GREEN);
//            visualisationWindow.getChildren().add(d);
//
//            d.setCenterX(i * 10);
//            d.setCenterY(i + 3*2);
//
//            System.out.println("cercle crée!");
//        }
//
//
//        c.setCenterX(2);
//        c.setCenterY(100);

        Circle a = new Circle(15, Color.RED);
        visualisationWindow.getChildren().add(a);

        a.setCenterX(420);
        a.setCenterY(280);

        Circle b = new Circle(15, Color.BLACK);
        visualisationWindow.getChildren().add(b);

        b.setCenterX(420);
        b.setCenterY(380);

        Circle c = new Circle(15, Color.BLUE);
        visualisationWindow.getChildren().add(c);

        c.setCenterX(520);
        c.setCenterY(280);

        Circle d = new Circle(15, Color.GREEN);
        visualisationWindow.getChildren().add(d);

        d.setCenterX(520);
        d.setCenterY(380);

        Circle e = new Circle(10, Color.ORANGE);
        visualisationWindow.getChildren().add(e);

        e.setCenterX(620);
        e.setCenterY(330);


    }

    /**
     * Draw a small graph "by hand"
     */
    void drawEdge() {
        System.out.println("creation edge");
        Line redBlack = new Line();
        redBlack.setStartX(420);
        redBlack.setStartY(280);
        redBlack.setEndX(420);
        redBlack.setEndY(380);
        redBlack.setStrokeWidth(3);
        visualisationWindow.getChildren().add(redBlack);

        Line redBlue = new Line();
        redBlue.setStartX(420);
        redBlue.setStartY(280);
        redBlue.setEndX(520);
        redBlue.setEndY(280);
        redBlue.setStrokeWidth(3);
        visualisationWindow.getChildren().add(redBlue);

        Line blueGreen = new Line();
        blueGreen.setStartX(520);
        blueGreen.setStartY(280);
        blueGreen.setEndX(520);
        blueGreen.setEndY(380);
        visualisationWindow.getChildren().add(blueGreen);

        Line blackGreen = new Line();
        blackGreen.setStartX(420);
        blackGreen.setStartY(380);
        blackGreen.setEndX(520);
        blackGreen.setEndY(380);
        visualisationWindow.getChildren().add(blackGreen);

        Line blueYellow = new Line();
        blueYellow.setStartX(520);
        blueYellow.setStartY(280);
        blueYellow.setEndX(620);
        blueYellow.setEndY(330);
        visualisationWindow.getChildren().add(blueYellow);

        Line greenYellow = new Line();
        greenYellow.setStartX(520);
        greenYellow.setStartY(380);
        greenYellow.setEndX(620);
        greenYellow.setEndY(330);
        visualisationWindow.getChildren().add(greenYellow);
    }

}
