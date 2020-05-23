package ui.util;

import javafx.animation.FillTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

//helper class which allows for multiple colors to be animated through during a single transition
public class MultiFillTransition {

    private List<Color> fillColors;
    private Shape shape;
    private Duration duration;

    //EFFECTS: create a new multi-fill transition given the effected shape, colors, and a duration
    public MultiFillTransition(Duration duration, Shape shape, List<Color> colors) {
        this.fillColors = colors;
        this.shape = shape;
        this.duration = duration;
    }

    //EFFECTS: get the sequential transition resulted from combining all the colors in a single transition
    public SequentialTransition getValue() {
        if (fillColors.size() == 0) {
            return null;
        }

        double timePerTransition = duration.toMillis() / fillColors.size();
        Duration durationPerTransition = new Duration(timePerTransition);

        List<FillTransition> fillTransitions = new ArrayList<>(fillColors.size());

        for (Color color : fillColors) {
            FillTransition transition = new FillTransition(durationPerTransition, shape);
            transition.setToValue(color);
            fillTransitions.add(transition);
        }

        SequentialTransition result = new SequentialTransition();
        result.getChildren().addAll(fillTransitions);

        return result;
    }
}
