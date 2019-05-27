package si;

import javafx.scene.control.Alert;

class Periode {

    private String column;

    void showEmptyDialog(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("NullPointerException");
        alert.setHeaderText("Veuillez sélectionner une période");
        alert.setContentText("avoid NullPointerException");
        alert.showAndWait();
    }
}
