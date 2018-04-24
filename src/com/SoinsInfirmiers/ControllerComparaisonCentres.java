package SoinsInfirmiers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSpinner;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerComparaisonCentres implements Initializable {

    @FXML
    private JFXComboBox<Integer> comboYear;
    @FXML
    private JFXComboBox<String> comboCentre1;
    @FXML
    private JFXComboBox<String> comboCentre2;
    @FXML
    private JFXComboBox<String> comboCentre3;
    @FXML
    private JFXComboBox<String> comboCentre4;
    @FXML
    private JFXComboBox<String> comboCentre5;
    @FXML
    private JFXComboBox<String> comboIndic;
    @FXML
    private JFXButton generateButton;
    @FXML
    private JFXButton clearButton;
    @FXML
    private JFXComboBox<String> comboCategorie;
    @FXML
    private ImageView redCross1;
    @FXML
    private ImageView redCross2;
    @FXML
    private ImageView redCross3;
    @FXML
    private ImageView redCross4;
    @FXML
    private ImageView redCross5;
    @FXML
    private Label noGraphicLabel;
    @FXML
    private JFXSpinner idleSpinner;
    @FXML
    private VBox menuPane;
    @FXML
    private LineChart<?, ?> lineChart;
    @FXML
    private NumberAxis yAxis;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        main.Menu menu = new main.Menu();
        menu.loadMenuBar(menuPane);
        initializeCombo();
    }

    private void initializeCombo() {
        Data data = new Data();
        comboYear.setItems(data.yearList);
        JFXComboBox[] comboCenterArray = {comboCentre1, comboCentre2, comboCentre3, comboCentre4, comboCentre5};
        for (JFXComboBox aComboCenterArray : comboCenterArray) {
            aComboCenterArray.setItems(data.centerList);
        }
        comboCategorie.setItems(data.categorieList);
    }

    public void setIndicateursInCombo() {
        if (comboCategorie.getValue() != null) {
            Category category = new Category();
            category.setCategorie(comboCategorie.getValue());
            comboIndic.setItems(category.getCategorie());
        }
    }
}
