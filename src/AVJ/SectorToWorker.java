package AVJ;

import com.LoadProperties;
import java.io.IOException;
import java.util.Properties;

public class SectorToWorker {

    String secteur = "";
    LoadProperties loadProperties = new LoadProperties();
    Properties properties = new Properties();

    public void secteurToWorker(String selectedSecteur){
        loadFile();
        this.secteur = selectedSecteur;
        switch (selectedSecteur){
            case "AM APE" :
                secteur = properties.getProperty("travailleur.AMAPE");
                break;
            case "AM APE Philippeville" :
                secteur = properties.getProperty("travailleur.AMAPEPhilippeville");
                break;
            case "AM Jambes" :
                secteur = properties.getProperty("travailleur.AMJambes");
                break;
            case "AM Philippeville" :
                secteur = properties.getProperty("travailleur.AMPhilippeville");
                break;
            case "Andenne" :
                secteur = properties.getProperty("travailleur.Andenne");
                break;
            case "Bois de Villers" :
                secteur = properties.getProperty("travailleur.BoisDeVillers");
                break;
            case "Bouge" :
                secteur = properties.getProperty("travailleur.Bouge");
                break;
            case "Cerfontaine" :
                secteur = properties.getProperty("travailleur.Cerfontaine");
                break;
            case "Couvin" :
                secteur = properties.getProperty("travailleur.Couvin");
                break;
            case "Eghezée" :
                secteur = properties.getProperty("travailleur.Eghezée");
                break;
            case "Florennes" :
                secteur = properties.getProperty("travailleur.Florennes");
                break;
            case "Fosses-Mettet" :
                secteur = properties.getProperty("travailleur.FossesMettet");
                break;
            case "Gembloux" :
                secteur = properties.getProperty("travailleur.Gembloux");
                break;
            case "Gesves" :
                secteur = properties.getProperty("travailleur.Gesves");
                break;
            case "Jambes" :
                secteur = properties.getProperty("travailleur.Jambes");
                break;
            case "Jambes extérieur" :
                secteur = properties.getProperty("travailleur.JambesExterieur");
                break;
            case "Namur" :
                secteur = properties.getProperty("travailleur.Namur");
                break;
            case "Namur Gare" :
                secteur = properties.getProperty("travailleur.NamurGare");
                break;
            case "Philippeville" :
                secteur = properties.getProperty("travailleur.Philippeville");
                break;
            case "Saint-Servais" :
                secteur = properties.getProperty("travailleur.SaintServais");
                break;
            case "Salzinnes" :
                secteur = properties.getProperty("travailleur.Salzinnes");
                break;
            case "Sambreville" :
                secteur = properties.getProperty("travailleur.Sambreville");
                break;
            case "Viroinval" :
                secteur = properties.getProperty("travailleur.Viroinval");
                break;
            case "Volantes" :
                secteur = properties.getProperty("travailleur.Volantes");
                break;
            case "Volantes Namur" :
                secteur = properties.getProperty("travailleur.VolantesNamur");
                break;
            case "Walcourt" :
                secteur = properties.getProperty("travailleur.Walcourt");
                break;
        }
    }

    public String getSecteur(){
        return secteur;
    }

    private void loadFile(){
        try {
            properties = loadProperties.load("C:\\Users\\johnathanv\\IdeaProjects\\Statistiques_FX\\src\\resources\\properties\\Contingent.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
