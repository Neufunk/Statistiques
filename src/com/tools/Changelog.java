package tools;

import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Changelog {

    private static Stage console = new Stage();
    private static javafx.scene.control.TextArea text = new TextArea();

    static public void buildChangelog(){
        StackPane stackPane = new StackPane();
        Scene scene = new Scene(stackPane);
        stackPane.getChildren().add(text);
        text.positionCaret(0);
        String style = "-fx-control-inner-background:#FFF;" +
                " -fx-highlight-fill: #333;" +
                " -fx-highlight-text-fill: #fff; " +
                " -fx-text-fill: #000; ";
        text.setStyle(style);
        text.setEditable(false);

        String title = "CHANGELOGS";
        console.setTitle(title);
        console.setX(100);
        console.setY(150);
        console.setScene(scene);
        console.setHeight(520);
        console.setWidth(680);
        console.show();
    }

    static public void append(String string) {
        text.appendText(string + "\n");
    }
}
