package main;

import javafx.scene.control.Alert;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionHandler {

    public static void switchException(Exception exception, Class classe) {
        String errorName = exception.getClass().getName();
        String className = classe.getName();
        System.out.println("Erreur " + errorName + " renvoyée par la classe " + className);
        switch (errorName) {
            case "java.sql.SQLException":
                displayError("Erreur SQL", exception, className);
                break;
            case "java.sql.SQLSyntaxErrorException":
                displayError("Erreur de syntaxe SQL", exception, className);
                break;
            case "java.lang.NullPointerException":
                displayError("Null Pointer Exception", exception, className);
                break;
            case "java.lang.ClassNotFoundException":
                displayError("Classe non trouvée", exception, className);
                break;
            case "java.io.FileNotFoundException":
                displayError("Fichier occupé ou introuvable", exception, className);
                break;
            case "java.lang.IllegalStateException":
                displayError("Illegal State", exception, className);
                break;
            default:
                displayError("Erreur inconnue", exception, className);
        }
    }

    private static void displayError(String title, Exception e, String className) {
        StringWriter error = new StringWriter();
        e.printStackTrace(new PrintWriter(error));
        e.printStackTrace();
        String e1 = e.toString();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title + " - " + className);
        alert.setHeaderText("ERREUR " + e1 + "\nRENVOYÉ PAR " + className);
        alert.setContentText("STACKTRACE \n" + error.toString() + "\n");
        alert.showAndWait();
    }
}
