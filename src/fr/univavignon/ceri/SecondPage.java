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

        drawANode();
    }


    /**
     * Draw node in a pane.
     */
    void drawANode() {
        Circle c = new Circle(10, Color.BEIGE);
        visualisationWindow.getChildren().add(c);

        int x = 300;
        int y = 100;
        c.setCenterX(100);

        System.out.println(n.nodesList.size());

        for (int i = 0; i < n.nodesList.size(); i++) {
            Circle d = new Circle(10, Color.GREEN);
            visualisationWindow.getChildren().add(d);

            d.setCenterX(i * 10);
            d.setCenterY(i * 10 + 3);

            System.out.println("cercle crée!");
        }



        c.setCenterX(2);
        c.setCenterY(100);
    }

}
