package si;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSpinner;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import tools.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class ControllerComparaisonCentres implements Initializable {

    @FXML
    private JFXComboBox<Integer> comboYear;
    @FXML
    private JFXComboBox<String> comboCentre1, comboCentre2, comboCentre3, comboCentre4, comboCentre5, comboIndic, comboCategorie;
    @FXML
    private ImageView redCross1, redCross2, redCross3, redCross4, redCross5;
    @FXML
    private JFXSpinner idleSpinner;
    @FXML
    private AnchorPane menuPane;
    @FXML
    private LineChart<String, Float> lineChart;
    @FXML
    private NumberAxis yAxis;

    private final Centre centre = new Centre();
    private final Queries queries = new Queries();
    private final Effects effects = new Effects();
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private int colorCounter = 0;
    private Connection conn;
    private DatabaseConnection databaseConnection = new DatabaseConnection();
    private Identification id = new Identification();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuPane.getChildren().get(0).getStyleClass().add("brown");
        initializeCombo();
        onRedCrossClick();
    }

    @SuppressWarnings("unchecked")
    private void initializeCombo() {
        int[] yearList = Date.getYearList();
        for (int value : yearList) {
            comboYear.getItems().addAll(value);
        }

        comboYear.setValue(Date.getCurrentYearInt());
        JFXComboBox[] comboCenterArray = {comboCentre1, comboCentre2, comboCentre3, comboCentre4, comboCentre5};

        for (JFXComboBox aComboCenterArray : comboCenterArray) {
            aComboCenterArray.getItems().addAll(centre.CENTER_NAME);
        }
        for (int i = 0; i < queries.CATEGORIE.length; i++) {
            comboCategorie.getItems().addAll(queries.CATEGORIE[i]);
        }
    }

    public void setIndicateursInCombo() {
        if (comboCategorie.getValue() != null) {
            comboIndic.getItems().clear();
            int index = comboCategorie.getSelectionModel().getSelectedIndex();
            for (int i = 0; i < queries.COMBO_INDICATEUR_ARRAY[index].length; i++) {
                System.out.println(queries.COMBO_INDICATEUR_ARRAY[index][i].toString());
                comboIndic.getItems().addAll(queries.COMBO_INDICATEUR_ARRAY[index][i].toString().replace("_", " "));
            }
        }
    }

    public void onGenerateButtonClick() {
        effects.setFadeTransition(lineChart, 200, 1, 0);
        lineChart.getData().clear();
        if (checkEmpty()) {
            idleSpinner.setVisible(true);
            new Thread(() -> {
                try {
                    generateAll();
                } catch (Exception e) {
                    ExceptionHandler.switchException(e, this.getClass());
                } finally {
                    databaseConnection.close(rs);
                    databaseConnection.close(ps);
                    databaseConnection.close(conn);
                }
            }).start();
        }
    }

    private boolean checkEmpty() {
        if (checkComboCentre()) {
            EmptyChecker.showEmptyCentreDialog();
            return false;
        } else if (comboYear.getValue() == null) {
            EmptyChecker.showEmptyYearDialog();
            return false;
        } else if (comboCategorie.getValue() == null) {
            EmptyChecker.showEmptyCategoryDialog();
            return false;
        } else if (comboIndic.getValue() == null) {
            EmptyChecker.showEmptyIndicDialog();
            return false;
        } else {
            return true;
        }
    }

    private boolean checkComboCentre() {
        return comboCentre1.getValue() == null && comboCentre2.getValue() == null && comboCentre3.getValue() == null &&
                comboCentre4.getValue() == null && comboCentre5.getValue() == null;
    }

    public void onClearButtonClick() {
        clearCombos();
        lineChart.setTitle("");
        lineChart.getData().clear();
        effects.setFadeTransition(lineChart, 200, 1, 0);
    }

    private void onRedCrossClick() {
        redCross1.addEventHandler(MouseEvent.MOUSE_RELEASED, (e) -> {
            comboCentre1.getSelectionModel().clearSelection();
            onGenerateButtonClick();
        });
        redCross2.addEventHandler(MouseEvent.MOUSE_RELEASED, (e) -> {
            comboCentre2.getSelectionModel().clearSelection();
            onGenerateButtonClick();
        });
        redCross3.addEventHandler(MouseEvent.MOUSE_RELEASED, (e) -> {
            comboCentre3.getSelectionModel().clearSelection();
            onGenerateButtonClick();
        });
        redCross4.addEventHandler(MouseEvent.MOUSE_RELEASED, (e) -> {
            comboCentre4.getSelectionModel().clearSelection();
            onGenerateButtonClick();
        });
        redCross5.addEventHandler(MouseEvent.MOUSE_RELEASED, (e) -> {
            comboCentre5.getSelectionModel().clearSelection();
            onGenerateButtonClick();
        });
    }

    public void onAllCenterButtonClick() {
        comboCentre1.getSelectionModel().select(0);
        comboCentre2.getSelectionModel().select(1);
        comboCentre3.getSelectionModel().select(2);
        comboCentre4.getSelectionModel().select(3);
        comboCentre5.getSelectionModel().select(4);
    }

    private void clearCombos() {
        comboCentre1.getSelectionModel().clearSelection();
        comboCentre2.getSelectionModel().clearSelection();
        comboCentre3.getSelectionModel().clearSelection();
        comboCentre4.getSelectionModel().clearSelection();
        comboCentre5.getSelectionModel().clearSelection();
    }

    private void generateAll() throws Exception {
        colorCounter = 0;
        Queries.Query currentIndicateur = queries.COMBO_INDICATEUR_ARRAY[comboCategorie.getSelectionModel().getSelectedIndex()][comboIndic.getSelectionModel().getSelectedIndex()];
        int year = comboYear.getValue();
        String query = queries.selectQuery(currentIndicateur);
        conn = databaseConnection.connect(
                id.set(Identification.info.D615_URL),
                id.set(Identification.info.D615_USER),
                id.set(Identification.info.D615_PASSWD),
                id.set(Identification.info.D615_DRIVER)
        );
        ps = conn.prepareStatement(query);
        if (comboCentre1.getValue() != null) {
            rs = queries.setQuery(currentIndicateur, ps, year, centre.CENTER_NO[comboCentre1.getSelectionModel().getSelectedIndex()]);
            addDataToGraphic(rs, comboCentre1.getValue());
        }
        if (comboCentre2.getValue() != null) {
            rs = queries.setQuery(currentIndicateur, ps, year, centre.CENTER_NO[comboCentre2.getSelectionModel().getSelectedIndex()]);
            addDataToGraphic(rs, comboCentre2.getValue());
        }
        if (comboCentre3.getValue() != null) {
            rs = queries.setQuery(currentIndicateur, ps, year, centre.CENTER_NO[comboCentre3.getSelectionModel().getSelectedIndex()]);
            addDataToGraphic(rs, comboCentre3.getValue());
        }
        if (comboCentre4.getValue() != null) {
            rs = queries.setQuery(currentIndicateur, ps, year, centre.CENTER_NO[comboCentre4.getSelectionModel().getSelectedIndex()]);
            addDataToGraphic(rs, comboCentre4.getValue());
        }
        if (comboCentre5.getValue() != null) {
            rs = queries.setQuery(currentIndicateur, ps, year, centre.CENTER_NO[comboCentre5.getSelectionModel().getSelectedIndex()]);
            addDataToGraphic(rs, comboCentre5.getValue());
        }
        Platform.runLater(this::buildGraphic);
    }

    @SuppressWarnings("unchecked")
    private void addDataToGraphic(ResultSet rs, String centre) {
        XYChart.Series series = new XYChart.Series();
        try {
            yAxis.setForceZeroInRange(false); // Important for chart scale
            lineChart.setVisible(true);
            idleSpinner.setVisible(false);
            String[] MONTH = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet",
                    "Août", "Septembre", "Octobre", "Novembre", "Décembre"};
            int i = 0;
            while (rs.next()) {
                series.setName(centre);
                XYChart.Data<String, Double> data = new XYChart.Data<>(MONTH[i], rs.getDouble("TOTAL"));
                data.setNode(new HoveredNode(rs.getDouble("TOTAL"), colorCounter));
                series.getData().add(data);
                System.out.println(MONTH[i]);
                i++;
            }
            int finalI = i;
            Platform.runLater(() -> Console.appendln(finalI + "mois ajoutés au graphique avec succès"));
            Platform.runLater(() -> lineChart.getData().add(series));
            colorCounter++;
        } catch (Exception e) {
            ExceptionHandler.switchException(e, this.getClass());
        }
    }

    private void buildGraphic(){
        lineChart.setTitle(comboIndic.getValue());
        effects.setFadeTransition(lineChart, 200, 0, 1);
        idleSpinner.setVisible(false);
        for (Node node : lineChart.lookupAll(".chart-legend-item")) {
            if (node instanceof Label) {
                ((Label) node).setWrapText(true);
                ((Label) node).setAlignment(Pos.CENTER);
                node.setManaged(true);
                ((Label) node).setMinWidth(200);
                ((Label) node).setMaxWidth(2000);
            }
        }
    }
}
