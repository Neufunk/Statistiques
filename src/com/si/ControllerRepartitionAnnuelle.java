package si;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSpinner;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import tools.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import static si.Database.Query.*;

public class ControllerRepartitionAnnuelle implements Initializable {

    @FXML
    private JFXComboBox<String> comboCentre, comboIndic;
    @FXML
    private JFXComboBox<Integer> comboYear;
    @FXML
    private PieChart roundGraph;
    @FXML
    private JFXSpinner spinner;
    @FXML
    private JFXComboBox<String> comboCategorie;
    @FXML
    private VBox vboxIndic, vboxData;
    @FXML
    private JFXButton backButton, nextButton;
    @FXML
    private AnchorPane menuPane;

    private final Centre centre = new Centre();
    private final Effects effects = new Effects();
    private Database database = new Database();

    private final String[][] COMBO_INDICATEURS = {
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
        SUPPLEMENTS EN JOURS
     */

    private final Database.Query[][][] INDICATEUR_ARRAY = {
            // TARIFICATION
            {
                    {RECETTE_TOTALE, TARIFICATION_OA, TICKETS_MODERATEURS, SOINS_DIVERS, CONVENTIONS}
            },
            // VISITES
            {
                    {NOMBRE_DE_VISITES, VISITES_PAR_J_AV_SOINS, VISITES_PAR_J_PRESTES}
            },
            // PATIENTS
            {
                    {NOMBRE_DE_PATIENTS, NOMBRE_DE_PATIENTS_FFA, NOMBRE_DE_PATIENTS_FFB, NOMBRE_DE_PATIENTS_FFC, NOMBRE_DE_PATIENTS_NOMENCLATURE}
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
        navigateThroughYears();
    }

    private void initializeCombo() {
        comboCentre.getItems().addAll(centre.CENTER_NAME);
        comboCentre.setValue(centre.CENTER_NAME[5]);
        int[] yearList = Date.getYearList();
        for (int value : yearList) {
            comboYear.getItems().addAll(value);
        }
        comboYear.setValue(Date.getCurrentYearInt());
        comboCategorie.getItems().addAll(database.CATEGORIE);
    }

    public void setIndicateursInCombo() {
        if (comboCategorie.getValue() != null) {
            comboIndic.getItems().clear();
            int index = comboCategorie.getSelectionModel().getSelectedIndex();
            for (int i = 0; i < COMBO_INDICATEURS[index].length; i++) {
                comboIndic.getItems().add(COMBO_INDICATEURS[index][i].replace("_", " "));
            }
        }
    }

    @FXML
    private void onGenerateButtonClick() {
        if (EmptyChecker.checkCombo(comboCategorie, comboIndic, comboYear)) {
            disableButton();
            resetAll();
            new Thread(this::generate).start();
        }
    }

    private void resetAll() {
        effects.setFadeOut(roundGraph, 100);
        effects.setFadeOut(vboxData, 300);
        effects.setFadeOut(vboxIndic, 300);
        roundGraph.getData().clear();
        spinner.setVisible(true);
        vboxData.getChildren().clear();
        vboxIndic.getChildren().clear();
    }

    private void generate() {
        Connection conn = database.connect();
        PreparedStatement ps = null;
        try {
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            final int CENTRE_NO = centre.CENTER_NO[comboCentre.getSelectionModel().getSelectedIndex()];
            for (int i = 0; i < INDICATEUR_ARRAY[comboCategorie.getSelectionModel().getSelectedIndex()][comboIndic.getSelectionModel().getSelectedIndex()].length; i++) {
                double total = 0;
                Database.Query currentIndicateur = INDICATEUR_ARRAY[comboCategorie.getSelectionModel().getSelectedIndex()][comboIndic.getSelectionModel().getSelectedIndex()][i];
                String query = database.selectQuery(currentIndicateur);
                ps = conn.prepareStatement(query);
                ResultSet rs = database.setQuery(currentIndicateur, ps, comboYear.getValue(), CENTRE_NO);
                while (rs.next()) {
                    System.out.println(rs.getDouble("TOTAL"));
                    Console.appendln("" + rs.getDouble("TOTAL"));
                    total += rs.getDouble("TOTAL");
                }
                System.out.println(currentIndicateur + ": " + total);
                Console.appendln(currentIndicateur + " : " + total);
                double finalTotal = total;
                // Avoid the first indic. in the pie
                if (i < 1) {
                    final Label label = new Label(currentIndicateur.toString().replace("_", " "));
                    label.setFont(Font.font(18));
                    label.setStyle("-fx-font-weight: bold;");
                    Platform.runLater(() -> vboxIndic.getChildren().add(label));

                    final Label label2 = new Label(Formatter.formatDouble(finalTotal));
                    label2.setFont(Font.font(18));
                    label2.setStyle("-fx-font-weight: bold;");
                    Platform.runLater(() -> vboxData.getChildren().add(label2));
                } else {
                    pieChartData.add(new PieChart.Data(Formatter.formatString(currentIndicateur.toString()), total));
                    Platform.runLater(() -> vboxIndic.getChildren().add(new Label(currentIndicateur.toString().replace("_", " "))));
                    Platform.runLater(() -> vboxData.getChildren().add(new Label(Formatter.formatDouble(finalTotal))));
                }
            }
            Platform.runLater(() -> roundGraph.setData(pieChartData));
            Platform.runLater(this::buildGraphic);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.close(ps);
            database.close(conn);
        }
    }

    private void buildGraphic() {
        roundGraph.setVisible(true);
        roundGraph.setTitle(comboIndic.getValue() + " - " + comboYear.getValue());
        spinner.setVisible(false);
        effects.setFadeIn(vboxData, 300);
        effects.setFadeIn(vboxIndic, 300);
        effects.setFadeIn(roundGraph, 900);
        for (final PieChart.Data data : roundGraph.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, (e1) ->
                    Tooltip.install(data.getNode(), new Tooltip(data.getName() + " - " + Formatter.formatDouble(data.getPieValue()))));
            if(data.getPieValue() == 0) {
                roundGraph.setVisible(false);
            }
        }
        for (Node node : roundGraph.lookupAll(".chart-legend-item")) {
            if (node instanceof Label) {
                ((Label) node).setWrapText(true);
                ((Label) node).setAlignment(Pos.CENTER);
                node.setManaged(true);
                ((Label) node).setMinWidth(150);
                ((Label) node).setMaxWidth(2000);
            }
        }

    }

    private void navigateThroughYears() {
        backButton.addEventHandler(MouseEvent.MOUSE_RELEASED, (event) -> {
            comboYear.getSelectionModel().selectPrevious();
            resetAll();
            onGenerateButtonClick();
        });
        nextButton.addEventHandler(MouseEvent.MOUSE_RELEASED, (event) -> {
            comboYear.getSelectionModel().selectNext();
            resetAll();
            onGenerateButtonClick();
        });
    }

    private void disableButton() {
        if (comboYear.getSelectionModel().getSelectedIndex() == 0) {
            backButton.setDisable(true);
        } else {
            backButton.setDisable(false);
        }
        if (comboYear.getSelectionModel().getSelectedIndex() == 4) {
            nextButton.setDisable(true);
        } else {
            nextButton.setDisable(false);
        }
    }
}