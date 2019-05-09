package main;

import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class HoveredNode extends StackPane {
    public HoveredNode(double value, int colorCounter) {
        setPrefSize(11, 11);

        final Label label = createDataThresholdLabel(value, colorCounter);

        setOnMouseEntered(mouseEvent -> {
            getChildren().setAll(label);
            setCursor(Cursor.NONE);
            toFront();
        });
        setOnMouseExited(mouseEvent -> getChildren().clear());
    }

    private Label createDataThresholdLabel(double value, int colorCounter) {
        final Label label;
        if (value - Math.floor(value) == 0){
            label = new Label(String.format("%,.0f", value));
        } else {
            label = new Label(String.format("%,.2f", value));
        }
        label.getStyleClass().addAll("chart-line-symbol", "chart-series-line", "default-color" + colorCounter);
        label.setTextFill(Color.BLACK);
        label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
        return label;
    }
}
