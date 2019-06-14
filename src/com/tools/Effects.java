package tools;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.scene.effect.BoxBlur;
import javafx.util.Duration;

public class Effects {

    public void setFadeTransition(Node node, double duration, double fromValue, double toValue){
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(duration), node);
        fadeTransition.setAutoReverse(false);
        fadeTransition.setFromValue(fromValue);
        fadeTransition.setToValue(toValue);
        fadeTransition.play();
    }

    public void setFadeIn(Node node, double duration) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(duration), node);
        fadeTransition.setAutoReverse(false);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
    }

    public void setFadeOut(Node node, double duration) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(duration), node);
        fadeTransition.setAutoReverse(false);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();
    }

    public void setBoxBlur(Node node){
        BoxBlur boxBlur = new BoxBlur();
        boxBlur.setWidth(5);
        boxBlur.setHeight(5);
        boxBlur.setIterations(10);
        node.setEffect(boxBlur);
    }

    public void unsetBoxBlur(Node node){
        BoxBlur boxBlur = new BoxBlur();
        boxBlur.setWidth(0);
        boxBlur.setHeight(0);
        boxBlur.setIterations(0);
        node.setEffect(boxBlur);
    }

    public void setScaleTransition(Node node, int duration, double fromValue, double toValue){
        ScaleTransition scaleTransition = new ScaleTransition();
        scaleTransition.setDuration(Duration.millis(duration));
        scaleTransition.setNode(node);
        scaleTransition.setFromX(fromValue);
        scaleTransition.setFromY(fromValue);
        scaleTransition.setToX(toValue);
        scaleTransition.setToY(toValue);
        scaleTransition.setCycleCount(1);
        scaleTransition.setAutoReverse(false);
        scaleTransition.play();
    }
}
