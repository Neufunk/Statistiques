package si;


import javafx.scene.control.Alert;

class Centre {

    private int sheet;
    final String[] CENTER_NAME = {"Philippeville", "Ciney", "Gedinne", "Eghezée", "Namur", "Province"};
    final int[] CENTER_NO = {902, 913, 923, 931, 961, 997};

    void toExcelSheet(String centre) {
        switch (centre) {
            case "Global":
                sheet = 5;
                break;
            case "Namur":
                sheet = 4;
                break;
            case "Philippeville":
                sheet = 0;
                break;
            case "Ciney":
                sheet = 1;
                break;
            case "Gedinne":
                sheet = 2;
                break;
            case "Eghezée":
                sheet = 3;
                break;
            default:
                System.out.println("Le centre n'existe pas");
                break;
        }
    }



    void showEmptyDialog() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("NullPointerException");
        alert.setHeaderText("Veuillez sélectionner un centre");
        alert.setContentText("avoid NullPointerException");
        alert.showAndWait();
    }

    int getSheet() {
        return sheet;
    }

}