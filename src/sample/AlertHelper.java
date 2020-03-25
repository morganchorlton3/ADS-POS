package sample;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

import java.text.DecimalFormat;
import java.util.Optional;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class AlertHelper {

    private static DecimalFormat df2 = new DecimalFormat("#.##");

    public static void itemNotFound(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Item Not Found");
        alert.setHeaderText(null);
        alert.setContentText("Item Not Found please try again");

        alert.showAndWait();
    }
}
