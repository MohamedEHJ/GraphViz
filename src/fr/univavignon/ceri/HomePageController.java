package fr.univavignon.ceri;

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
import java.util.ArrayList;
import java.util.List;

public class HomePageController {

    @FXML
    public Label fileName;

    @FXML
    public Button btn_back;

    @FXML
    public Pane visualisationWindow;

    File fileChoosen;
    Nodes n = new Nodes();
    Edge e = new Edge();

    /**
     * Receive the file choosen in the first window.
     *
     * @param graphML
     */
    public void receiveFile(File graphML) throws IOException, SAXException, ParserConfigurationException {
        fileChoosen = graphML;
        System.out.println(fileChoosen.getName());
        fileName.setText(fileChoosen.getName());
        xmlInit();
    }

    /**
     * read and init the graphML file by creating a list of edge and a list of node.
     */
    private void xmlInit() throws ParserConfigurationException, IOException, SAXException {
        //Get Document Builder
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        System.out.println(fileChoosen);
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

        n.nodeScrap(nList);
        e.edgeScrap(eList);

//        n.displayList();
//        e.displayList();

        drawNode();
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
        Parent first_page_parent = FXMLLoader.load(getClass().getResource("FXMLFirstPage.fxml"));
        Scene first_page_scene = new Scene(first_page_parent);
        Stage app_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        if (true) {
            // here we should verify the extension of the file. The button would be lock if the format is not good,
            // else we will be able to change scene
            app_stage.setScene(first_page_scene);
            app_stage.show();
        }
    }

    List<Circle> circleList = new ArrayList<>();

    void drawNode() {
        Circle c = new Circle(10, Color.BEIGE);
        visualisationWindow.getChildren().add(c);

        // 355
        // 544

        int paneCenterX = 544/2;
        int paneCenterY = 355/2;

        Rectangle r = new Rectangle(5,5,Color.RED);

        r.setX(paneCenterX);
        r.setY(paneCenterY);
        visualisationWindow.getChildren().add(r);
        for (int i = 1; i < n.nodesList.size(); i++) {
            Circle d = new Circle(5, Color.GREEN);
            visualisationWindow.getChildren().add(d);

            d.setCenterX(i*7+250);
            d.setCenterY(i*7+150);

//            System.out.println("cercle crée!");

            circleList.add(d);
        }


        c.setCenterX(2);
        c.setCenterY(100);
        System.out.println(circleList);

        System.out.println(n.nodesList);
        System.out.println(e.edgeList);

    }

    void drawEdge(){
        Line l = new Line();

    }


}
