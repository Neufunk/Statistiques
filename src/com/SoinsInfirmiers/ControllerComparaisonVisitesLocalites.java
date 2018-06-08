package SoinsInfirmiers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import main.Menu;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ControllerComparaisonVisitesLocalites implements Initializable {

    @FXML
    private TableView<ObservableList> tableView;
    @FXML
    private VBox menuBox;
    @FXML
    private BarChart barChart;

    final private Menu menu = new Menu();
    private Connection conn;
    private String query = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menu.loadMenuBar(menuBox);
        startQuery();
    }

    private void connect() {
        try {
            System.out.println("\n---------------------------------- ");
            System.out.println("Test du driver...");
            Class.forName("oracle.jdbc.driver.OracleDriver");
            System.out.println("Driver O.K.");

            String url = "jdbc:oracle:thin:@192.168.46.12:1521:D615";
            String user = "***";
            String passwd = "***";

            System.out.println("Connexion en cours...");
            conn = DriverManager.getConnection(url, user, passwd);
            System.out.println("Connexion effective !");
            System.out.println("---------------------------------- \n");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startQuery() {
        int c = 1;
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        connect();
        setQuery();
        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        XYChart.Series series = new XYChart.Series();
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);

            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                final int j = i - 1;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i));
                col.setCellFactory(TextFieldTableCell.forTableColumn());
                col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(j).toString()));
                tableView.getColumns().addAll(col);
                System.out.println("Column [" + i + "] ");
            }

            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    row.add(rs.getString(i));
                }
                System.out.println("Row [" + c + "]" + row);
                c++;
                data.add(row);
                series.getData().add(new XYChart.Data<>(rs.getString(1), rs.getDouble(19)));
            }
            tableView.setItems(data);
            generateBarChart(series);
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateBarChart(XYChart.Series series){
        barChart.getData().add(series);
    }

    private void setQuery(){
        query = "SELECT COALESCE( TO_CHAR( Name ), 'ALL' ) AS Localité,\n" +
                "                POSTAL_CODE AS Code_Postal,  \n" +
                "                NVL(Janvier,0) AS Janvier,\n" +
                "                NVL(Février,0) AS Février,\n" +
                "                NVL(Mars,0) AS Mars,\n" +
                "                (NVL(Janvier,0) + NVL(Février,0) + NVL(Mars,0)) AS Trimestre_1, \n" +
                "                NVL(Avril,0) AS Avril, \n" +
                "                NVL(Mai,0) AS Mai, \n" +
                "                NVL(Juin,0) AS Juin,\n" +
                "                (NVL(Avril,0) + NVL(Mai,0) + NVL(Juin,0)) AS Trimestre_2,\n" +
                "                NVL(Juillet,0) AS Juillet, \n" +
                "                NVL(Aout,0) AS Aout,\n" +
                "                NVL(Septembre,0) AS Septembre, \n" +
                "                (NVL(Juillet,0) + NVL(Aout,0) + NVL(Septembre,0)) AS Trimestre_3,\n" +
                "                NVL(Octobre,0) AS Octobre,  \n" +
                "                NVL(Novembre,0) AS Novembre, \n" +
                "                NVL(Décembre,0) AS Décembre,  \n" +
                "                (NVL(Octobre,0) + NVL(Novembre,0) + NVL(Décembre,0)) AS Trimestre_4,\n" +
                "                (NVL(Janvier,0) + NVL(Février,0) + NVL(Mars,0) + NVL(Avril,0) + NVL(Mai,0) + NVL(Juin,0) + NVL(Juillet,0) + NVL(Aout,0) + NVL(Septembre,0) + NVL(Octobre,0) + NVL(Novembre,0) + NVL(Décembre,0)) AS Total\n" +
                "                FROM ( \n" +
                "                SELECT COUNT(*) AS cnt, ioi.center, mu.name, mu.POSTAL_CODE, EXTRACT( MONTH FROM ioi.achiev_dat ) AS month\n" +
                "                    FROM   INVOICE_OUT_ITEMS ioi, INVOICES_OUT ino, CONV_HC.CARE_TARIFFS ctf, MUNICIPALITIES mu, STREETS st, FAMS fa, FAM_membs fmr \n" +
                "                    WHERE  ioi.center IN ( 961, 931, 923, 913, 902 ) \n" +
                "                   \n" +
                "                    AND ioi.ctf_seq_no = ctf.seq_no\n" +
                "                    AND ioi.ino_seq_no = ino.seq_no\n" +
                "                    AND fmr.fay_seq_no = INO.fmr_fay_seq_no_2\n" +
                "                    AND fmr.ref_no = INO.fmr_ref_no_2\n" +
                "                    AND fa.seq_no = fmr.fay_seq_no\n" +
                "                    AND fa.stt_seq_no = st.seq_no\n" +
                "                    AND st.mun_seq_no = mu.seq_no\n" +
                "                    AND    ioi.ACHIEV_DAT >= DATE '2018-01-01' \n" +
                "                    AND    ioi.ACHIEV_DAT <  DATE '2019-01-01' \n" +
                "                    AND    ioi.REINVOICING = '1' \n" +
                "                    AND    ino.INVOICE_TYPE = 'HM'\n" +
                "                    AND    ctf.class IN ('B1','B2','B3','P1','P2','P3','P4','P5')\n" +
                "                    AND    mu.postal_code BETWEEN '5000' AND '9999' \n" +
                "\n" +
                "                    GROUP BY ROLLUP( mu.name ), mu.POSTAL_CODE, EXTRACT( MONTH FROM ioi.achiev_dat ), substr(ino.seq_no, 1, 6), ioi.center\n" +
                "                 ) \n" +
                "\n" +
                "                 PIVOT ( MAX(cnt) FOR month IN ( \n" +
                "                    1 AS Janvier, \n" +
                "                    2 AS Février, \n" +
                "                    3 AS Mars, \n" +
                "                    4 AS Avril, \n" +
                "                    5 AS Mai, \n" +
                "                    6 AS Juin, \n" +
                "                    7 AS Juillet, \n" +
                "                    8 AS Aout, \n" +
                "                    9 AS Septembre, \n" +
                "                   10 AS Octobre, \n" +
                "                   11 AS Novembre, \n" +
                "                   12 AS Décembre\n" +
                "                 ) )\n" +
                "                 WHERE Name NOT LIKE ('ALL') \n" +
                "                 ORDER BY Localité NULLS LAST ";
    }
}
