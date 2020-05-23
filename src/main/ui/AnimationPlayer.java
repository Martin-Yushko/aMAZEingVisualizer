package ui;

import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import model.Animation;
import java.util.LinkedList;
import java.util.List;

import model.Tile.*;
import ui.util.StaggeredTransition;

// Serves as the manager class for displaying the visualisation
public class AnimationPlayer {
    public static double VISUALIZATION_SPEED = 0.2;
    public LinkedList<Animation> animations;
    private List<Transition> transitions;
    private int mazeX;
    private int mazeY;

    //EFFECTS: constructs a new AnimationPlayer class which is able to play the passed-in animations
    public AnimationPlayer(LinkedList<Animation> animations) {
        this.animations = animations;
        this.transitions = new LinkedList<>();
        this.mazeX = animations.get(0).getMazeX();
        this.mazeY = animations.get(0).getMazeY();
    }

    //EFFECTS: Renders the path-finding onto the screen and signals its beginning and end
    public void playAnimations(Runnable onFinish) {
        for (Animation animation : animations) {
            this.renderAnimation(animation);
        }
        SequentialTransition seqTransition = new SequentialTransition();
        seqTransition.getChildren().addAll(transitions);
        javafx.animation.Animation lastTransition =
                seqTransition.getChildren().get(seqTransition.getChildren().size() - 1);

        lastTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onFinish.run();
                Main.grid.setStatus(Grid.VisualisationStatus.DONE);
                Main.grid.setMouseTransparent(false);
                CellAnimator.failuresSoFar = 0;
            }
        });

        StaggeredTransition staggeredTransition = new StaggeredTransition(seqTransition, 0);
        double offset = VISUALIZATION_SPEED;
        offset /= Math.pow(Math.cbrt(animations.size()), 2) / 3;
        staggeredTransition.setStaggerOffset((float) offset);

        staggeredTransition.play();
    }

    //EFFECTS: Renders the path-finding onto the screen
    private void renderAnimation(Animation animation) {
        for (int row = 0; row < mazeY; row++) {
            for (int col = 0; col < mazeX; col++) {
                if (animation.getDuration() != -1) {
                    PauseTransition pauseTransition = new PauseTransition(new Duration(animation.getDuration()));
                    this.transitions.add(pauseTransition);
                    return;
                } else {
                    int index = row * mazeY + col;
                    AnimationState animationState = animation.animationStates.get(index);
                    Cell cell = Main.grid.getCell(col, row);
                    Transition newTransition = CellAnimator.animateCell(cell, animationState);
                    if (newTransition != null) {
                        this.transitions.add(newTransition);
                    }
                }

            }
        }
    }
}
