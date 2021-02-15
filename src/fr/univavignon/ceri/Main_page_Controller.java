package fr.univavignon.ceri;

import com.sun.glass.ui.CommonDialogs;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class Main_page_Controller {

    @FXML
    public Button visualisation_btn;

    @FXML
    public Button search_btn;

    /**
     * Manage the scene change when clicking on "visualisation" button.
     * Send the file in argument to the second scene too.
     *
     * @param actionEvent
     * @throws IOException
     */
    public void visualisation(ActionEvent actionEvent) throws IOException, ParserConfigurationException, SAXException {
        System.out.println("You clicked on 'Visualisation' button!");


        // A ENLEVER A ENLEVER A ENLEVER A ENLEVER A ENLEVER A ENLEVER A ENLEVER
//        fileChoosen = new File("C:\\Users\\AZ\\Documents\\graphs\\crawl40.graphml");
        // A ENLEVER A ENLEVER A ENLEVER A ENLEVER A ENLEVER A ENLEVER A ENLEVER A ENLEVER


        FXMLLoader loader = new FXMLLoader(getClass().getResource("second_page.fxml"));
        Parent home_page_parent = loader.load();
        Scene home_page_scene = new Scene(home_page_parent);
        Stage app_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        if (true) {
            // here we should verify the extension of the file. The button would be lock if the format is not good,
            // else we will be able to change scene
            /**
             * pass the information to the home page controller.
             */
            SecondPage secondPage = loader.getController();
            secondPage.receiveFile(selectedFile);

            app_stage.setScene(home_page_scene);
            app_stage.show();
        }
    }

    File selectedFile;

    /**
     * Manage the filechooser button.
     *
     * @param actionEvent
     */
    public void fileChooser(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File("C:\\Users\\mohamed\\Desktop\\s6\\projet prog\\graphs(1)")); // to comment before commit
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Graphml Files", "*.graphml")
        );

        selectedFile = fc.showOpenDialog(null);

        if ((selectedFile != null)) {
            search_btn.setText(selectedFile.getName());
            visualisation_btn.setDisable(false);
        } else {
            // else it's not
            System.out.println("file is not valid");
        }
    }


}
