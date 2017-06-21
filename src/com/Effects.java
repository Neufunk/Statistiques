package com;

import javafx.animation.FadeTransition;
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

    public void setBoxBlur(Node node){
        BoxBlur boxBlur = new BoxBlur();
        boxBlur.setWidth(2);
        boxBlur.setHeight(2);
        boxBlur.setIterations(13);
        node.setEffect(boxBlur);
    }
}
