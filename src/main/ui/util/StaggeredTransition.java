package ui.util;

import javafx.animation.*;
import javafx.util.Duration;

import java.util.LinkedList;
import java.util.List;

//a class whose purpose is to change a sequential transition into one which occurs in parallax given a particular offset
public class StaggeredTransition {

    private float staggerOffset;
    private SequentialTransition sourceTransitions;

    //EFFECTS: consturcts a new staggered transition given a sequence of transitions and an offset
    public StaggeredTransition(SequentialTransition transitions, float offset) {
        this.staggerOffset = offset;
        this.sourceTransitions = transitions;
    }

    public void setStaggerOffset(float offset) {
        this.staggerOffset = offset;
    }

    //EFFECTS: play the resulting staggered transition
    public void play() {
        double delayAdditive = 0;
        List<Animation> transitions = sourceTransitions.getChildren();
        List<Animation> outputTransitions = new LinkedList<>();
        int counter = 0;
        for (Animation transition : transitions) {
            transition.setDelay(durationForStagger(counter, delayAdditive));
            outputTransitions.add(transition);
            counter++;
            if (transition instanceof PauseTransition) {
                delayAdditive += ((PauseTransition) transition).getDuration().toMillis();
            }
        }

        ParallelTransition resultTransition = new ParallelTransition();
        resultTransition.getChildren().addAll(outputTransitions);
        resultTransition.play();
    }

    //EFFECTS: produces the duration of the offset of every subsequent animation
    private Duration durationForStagger(int counter, double delayAdditive) {
        return new Duration(staggerOffset * counter * 1000 + delayAdditive);
    }
}
