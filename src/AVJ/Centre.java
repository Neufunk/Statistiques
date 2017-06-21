package AVJ;

import javafx.collections.ObservableList;

public class Centre {

    Data data = new Data();
    private ObservableList secteur;

    public void setCentre(String centre){
        switch (centre) {
            case "ASD":
                secteur = data.secteursASD;
                break;
            case "Namur" :
                secteur = data.secteursNamur;
                break;
            case "Philippeville" :
                secteur = data.secteursPhilippeville;
        }
    }

    public ObservableList getSecteur(){
        return secteur;
    }
}

