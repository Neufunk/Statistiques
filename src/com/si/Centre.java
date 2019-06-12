package si;


import javafx.scene.control.Alert;

class Centre {

    final String[] CENTER_NAME = {"Philippeville", "Ciney", "Gedinne", "Eghezée", "Namur", "Province"};
    final int[] CENTER_NO = {902, 913, 923, 931, 961, 997};

    void showEmptyDialog() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("NullPointerException");
        alert.setHeaderText("Veuillez sélectionner un centre");
        alert.setContentText("avoid NullPointerException");
        alert.showAndWait();
    }
}
