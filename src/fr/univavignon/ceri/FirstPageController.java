package fr.univavignon.ceri;

import com.sun.glass.ui.CommonDialogs;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class FirstPageController {

    @FXML
    public Button visualisation_btn;

    @FXML
    public void sceneChange(ActionEvent actionEvent) throws IOException, ParserConfigurationException, SAXException {
        System.out.println("You clicked on 'Visualisation' button!");


        // A ENLEVER A ENLEVER A ENLEVER A ENLEVER A ENLEVER A ENLEVER A ENLEVER
        fileChoosen = new File("C:\\Users\\AZ\\Documents\\mongraph.graphml");
        // A ENLEVER A ENLEVER A ENLEVER A ENLEVER A ENLEVER A ENLEVER A ENLEVER A ENLEVER


        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLHomePage.fxml"));
        Parent home_page_parent = loader.load();
        Scene home_page_scene = new Scene(home_page_parent);
        Stage app_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        if (true) {
            // here we should verify the extension of the file. The button would be lock if the format is not good,
            // else we will be able to change scene
            /**
             * pass the information to the home page controller.
             */
            HomePageController hpController = loader.getController();
            hpController.receiveFile(fileChoosen);

            app_stage.setScene(home_page_scene);
            app_stage.show();
        }

    }


    File fileChoosen;

    @FXML
    public void FileChooser(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Graphml file", "*.graphml"));
        fc.setInitialDirectory(new File("C:\\Users\\AZ\\Documents"));
        fileChoosen = fc.showOpenDialog(null);


    }
}
