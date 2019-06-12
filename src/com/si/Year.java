package si;


import javafx.scene.control.Alert;

class Year {

    void showEmptyDialog(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Champ requis");
        alert.setHeaderText("Veuillez sélectionner une année");
        alert.setContentText("Year.showEmptyDialog()");
        alert.showAndWait();
    }

}
