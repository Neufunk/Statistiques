package main;

import javafx.application.Preloader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FirstPreloader extends Preloader {
    private ProgressBar bar;
    private Stage stage;

    private Scene createPreloaderScene() {
        ImageView imageView = new ImageView("img/asd_logo.png");
        imageView.setScaleX(0.5);
        imageView.setScaleY(0.5);
        bar = new ProgressBar();
        bar.setStyle("-fx-accent: #96bb37");
        GridPane gp = new GridPane();
        gp.setAlignment(Pos.CENTER);
        gp.setStyle("-fx-background-color: WHITE");
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(imageView);
        vBox.getChildren().add(bar);
        gp.add(vBox, 0, 0);
        return new Scene(gp, 500, 350);
    }

    public void start(Stage stage) {
        this.stage = stage;
        stage.setScene(createPreloaderScene());
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    @Override
    public void handleProgressNotification(ProgressNotification pn) {
        bar.setProgress(pn.getProgress());
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification evt) {
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (evt.getType() == StateChangeNotification.Type.BEFORE_START) {
            stage.hide();
        }
    }
}
