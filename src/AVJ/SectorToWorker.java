package AVJ;

import com.LoadProperties;
import com.Version;
import javafx.scene.control.Alert;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class SectorToWorker {

    String travailleur = "";
    String key = "";
    LoadProperties loadProperties = new LoadProperties();
    Properties properties = new Properties();


    public String setKey(String selectedSecteur) {
        loadFile();
        switch (selectedSecteur) {
            case "AM APE":
                key = "travailleur.AMAPE";
                break;
            case "AM APE Philippeville":
                key = "travailleur.AMAPEPhilippeville";
                break;
            case "AM Jambes":
                key = "travailleur.AMJambes";
                break;
            case "AM Philippeville":
                key = "travailleur.AMPhilippeville";
                break;
            case "Andenne":
                key = "travailleur.Andenne";
                break;
            case "Bois de Villers":
                key = "travailleur.BoisDeVillers";
                break;
            case "Bouge":
                key = "travailleur.Bouge";
                break;
            case "Cerfontaine":
                key = "travailleur.Cerfontaine";
                break;
            case "Couvin":
                key = "travailleur.Couvin";
                break;
            case "Eghezée":
                key = "travailleur.Eghezée";
                break;
            case "Florennes":
                key = "travailleur.Florennes";
                break;
            case "Fosses-Mettet":
                key = "travailleur.FossesMettet";
                break;
            case "Gembloux":
                key = "travailleur.Gembloux";
                break;
            case "Gesves":
                key = "travailleur.Gesves";
                break;
            case "Jambes":
                key = "travailleur.Jambes";
                break;
            case "Jambes extérieur":
                key = "travailleur.JambesExterieur";
                break;
            case "Namur":
                key = "travailleur.Namur";
                break;
            case "Namur Gare":
                key = "travailleur.NamurGare";
                break;
            case "Philippeville":
                key = "travailleur.Philippeville";
                break;
            case "Saint-Servais":
                key = "travailleur.SaintServais";
                break;
            case "Salzinnes":
                key = "travailleur.Salzinnes";
                break;
            case "Sambreville":
                key = "travailleur.Sambreville";
                break;
            case "Viroinval":
                key = "travailleur.Viroinval";
                break;
            case "Volantes":
                key = "travailleur.Volantes";
                break;
            case "Volantes Namur":
                key = "travailleur.VolantesNamur";
                break;
            case "Walcourt":
                key = "travailleur.Walcourt";
                break;
        }
        return key;
    }

    public String getKey(){
        return key;
    }

    public void secteurToWorker() {
        travailleur = properties.getProperty(key);
    }

    public String getTravailleur() {
        return travailleur;
    }

    public void changeWorker(String newWorker){
        properties.setProperty(key, newWorker);
        loadProperties.save(properties);
    }

    private void loadFile() {
        try {
            properties = loadProperties.load("C:\\Users\\johnathanv\\IdeaProjects\\Statistiques_FX\\src\\resources\\properties\\Contingent.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
