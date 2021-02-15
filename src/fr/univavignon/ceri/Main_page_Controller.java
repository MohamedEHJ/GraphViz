package fr.univavignon.ceri;

import com.sun.glass.ui.CommonDialogs;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class Main_page_Controller {

    @FXML
    public Button visualisation_btn;

    @FXML
    public Button search_btn;

    public void visualisation(ActionEvent actionEvent) throws IOException {
        AnchorPane a = FXMLLoader.load(getClass().getResource("second_page.fxml"));
    }

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

        File selectedFile = fc.showOpenDialog(null);

        if ((selectedFile != null)) {
            search_btn.setText(selectedFile.getName());
            visualisation_btn.setDisable(false);
        } else {
            // else it's not
            System.out.println("file is not valid");
        }
    }


}
