package si;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSpinner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import tools.Date;
import tools.Effects;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import static si.Database.Query.*;

public class ControllerRepartition implements Initializable {

    @FXML
    private JFXComboBox<String> comboCentre, comboIndic;
    @FXML
    private JFXComboBox<Integer> comboYear;
    @FXML
    private PieChart roundGraph;
    @FXML
    private Label graphicTitle, noGraphicLabel, labelMasterIndic, labelMasterValue, labelIndicA, labelIndicB, labelIndicC, labelIndicD;
    @FXML
    private Label labelIndicE, labelValueA, labelValueB, labelValueC, labelValueD, labelValueE, monthLabel;
    @FXML
    private JFXSpinner spinner;
    @FXML
    private JFXComboBox<String> comboCategorie;
    @FXML
    private GridPane labelPane, valuePane;
    @FXML
    private JFXButton backButton, nextButton;
    @FXML
    private AnchorPane menuPane;

    private final Year year = new Year();
    private final Centre centre = new Centre();
    private final Periode periode = new Periode();
    private final Indicateur indicateur = new Indicateur();
    private final Effects effects = new Effects();
    private Database database = new Database();
    private Connection conn;

    private final String[][] COMBO_ARRAY = {
            // TARIFICATION
            {"RECETTE_TOTALE"},
            // VISITES
            {"NOMBRE_DE_VISITES"},
            // PATIENTS
            {"NOMBRE_DE_PATIENTS"},
            // SOINS
            {},
            // SUIVI DU PERSONNEL
            {"TOTAL_JOURS_PAYES", "TOTAL_JOURS_PRESTES_INF.", "TOTAL_PRESTE"}
    };

    /* TODO :
        REPARTITION DES BLOCS
        SUPPLEMENT EN JOURS
     */

    private final Database.Query[][][] INDICATEUR_ARRAY = {
            // TARIFICATION
            {
                    {RECETTE_TOTALE, TARIFICATION_OA, TICKETS_MODERATEURS, SOINS_DIVERS, CONVENTIONS}
            },
            // VISITES
            {
                    {VISITES_PAR_J_AV_SOINS, VISITES_PAR_J_PRESTES}
            },
            // PATIENTS
            {
                    {NOMBRE_DE_PATIENTS_FFA, NOMBRE_DE_PATIENTS_FFB, NOMBRE_DE_PATIENTS_FFC, NOMBRE_DE_PATIENTS_PALLIA}
            },
            // SOINS
            {

            },
            // SUIVI DU PERSONNEL
            {

            }
    };


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuPane.getChildren().get(0).getStyleClass().add("indigo");
        initializeCombo();
        navigateThroughMonths();
    }

    private void initializeCombo() {
        comboCentre.getItems().addAll(centre.CENTER_NAME);
        comboCentre.setValue(centre.CENTER_NAME[5]);
        int[] yearList = Date.getYearList();
        for (int value : yearList) {
            comboYear.getItems().addAll(value);
        }
        comboCategorie.getItems().addAll(database.CATEGORIE);
    }

    public void setIndicateursInCombo() {
        if (comboCategorie.getValue() != null) {
            comboIndic.getItems().clear();
            int index = comboCategorie.getSelectionModel().getSelectedIndex();
            for (int i = 0; i < COMBO_ARRAY[index].length; i++) {
                comboIndic.getItems().add(COMBO_ARRAY[index][i].replace("_", " "));
            }
        }
    }

    @FXML
    private void onGenerateButtonClick() {
        generate();
    }

    private void generate() {
        conn = database.connect();
        try {
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            PreparedStatement ps;
            final int CENTRE_NO = centre.CENTER_NO[comboCentre.getSelectionModel().getSelectedIndex()];
            for (int i = 0; i < INDICATEUR_ARRAY[comboCategorie.getSelectionModel().getSelectedIndex()][comboIndic.getSelectionModel().getSelectedIndex()].length; i++) {
                double total = 0;
                Database.Query currentIndicateur = INDICATEUR_ARRAY[comboCategorie.getSelectionModel().getSelectedIndex()][comboIndic.getSelectionModel().getSelectedIndex()][i];
                String query = database.selectQuery(currentIndicateur);
                ps = conn.prepareStatement(query);
                ResultSet rs = database.setQuery(currentIndicateur, ps, comboYear.getValue(), CENTRE_NO);
                while (rs.next()) {
                    System.out.println(rs.getDouble("TOTAL"));
                    total += rs.getDouble("TOTAL");
                }
                System.out.println(currentIndicateur + ": " + total);
                if (i > 0) {
                    pieChartData.add(new PieChart.Data(currentIndicateur.toString(), total));
                }
            }

            roundGraph.setData(pieChartData);
            for (final PieChart.Data data : roundGraph.getData()) {
                data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, (e1) ->
                        Tooltip.install(data.getNode(), new Tooltip(String.format("%,.2f", data.getPieValue()))));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.close(conn);
        }
    }

    private void navigateThroughMonths() {
        backButton.addEventHandler(MouseEvent.MOUSE_RELEASED, (event) -> {

        });
        nextButton.addEventHandler(MouseEvent.MOUSE_RELEASED, (event) -> {

        });
    }
}