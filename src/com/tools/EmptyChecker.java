package tools;

import com.jfoenix.controls.JFXComboBox;
import javafx.scene.control.Alert;

public class EmptyChecker {

    static public boolean checkCombo(JFXComboBox... combo) {
        boolean answer = false;
        for (JFXComboBox combobox : combo) {
            if (combobox.getValue() == null) {
                showEmptyDialog();
                return false;
            } else {
                answer = true;
            }
        }
        return answer;
    }

    private static void showEmptyDialog() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Champ requis");
        alert.setHeaderText("Un champ requis est manquant");
        alert.showAndWait();
    }

    public static void showEmptyYearDialog() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Champ requis");
        alert.setHeaderText("Veuillez sélectionner une année");
        alert.showAndWait();
    }

    public static void showEmptyIndicDialog() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Champ requis");
        alert.setHeaderText("Veuillez sélectionner un indicateur");
        alert.showAndWait();
    }

    public static void showEmptyCategoryDialog() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Champ requis");
        alert.setHeaderText("Veuillez sélectionner une catégorie");
        alert.showAndWait();
    }

    public static void showEmptyCentreDialog() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Champ requis");
        alert.setHeaderText("Veuillez sélectionner un centre");
        alert.showAndWait();
    }
}
