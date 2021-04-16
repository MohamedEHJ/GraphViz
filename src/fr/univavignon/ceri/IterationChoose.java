package fr.univavignon.ceri;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

import java.io.IOException;

public class IterationChoose {


    public Spinner<Integer> spinnerIteration;

    private SecondPage mainWindow;


    public void setMainWindow(SecondPage mainWindow) {
        this.mainWindow = mainWindow;
    }

    @FXML
    void initialize() {
        SpinnerValueFactory<Integer> valueFactory = //
                new SpinnerValueFactory.IntegerSpinnerValueFactory(25, 10000, 100);

        spinnerIteration.setValueFactory(valueFactory);

    }

    public void startAnimation(ActionEvent actionEvent) throws IOException {
        mainWindow.setIteration(spinnerIteration.getValue());
        ((Button) actionEvent.getSource()).getScene().getWindow().hide();
        mainWindow.initialisationBeforeLaunch();


    }
}
