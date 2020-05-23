package ui;

import javafx.animation.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import model.Tile;
import ui.util.MultiFillTransition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CellAnimator {
    private static final double ACCELERATION_FACTOR = 1.05;
    private static final double MAX_ACCELERATION = 99;
    public static int failuresSoFar = 0;

    //EFFECTS: produces a Transition which portrays this cell's animation, to be rendered when the
    //         pathfinding is visualized by the user on grid
    public static Transition animateCell(Cell cell, Tile.AnimationState newState) {
        if (!newState.equals(cell.animationState)) {
            cell.animationState = newState;
            double durationInMillis = AnimationPlayer.VISUALIZATION_SPEED * 1000 * 3;

            if (newState.equals(Tile.AnimationState.CURRENT)) {
                if (cell.type.equals(Tile.TileType.TARGET)) {
                    return CellAnimator.createAnimationTransition(cell, durationInMillis,
                            new ArrayList<>(Collections.singleton(Color.BLUEVIOLET)));
                }
            } else {
                List<Color> animationColors = CellAnimator.getAnimationColorsGivenState(newState);

                if (animationColors.size() == 0) {
                    return null;
                }

                cell.animatingCircle.setRadius(1);
                if (newState.equals(Tile.AnimationState.FAILURE)) {
                    durationInMillis = handleOnFailure();
                }

                return createAnimationTransition(cell, durationInMillis, animationColors);
            }
        }

        return null;
    }

    //EFFECTS: if the animation status is FAIL for this animation frame, set an accelerated animation time
    private static double handleOnFailure() {
        CellAnimator.failuresSoFar++;
        double accelerateBy = Math.max(1 / MAX_ACCELERATION,
                1 / Math.pow(ACCELERATION_FACTOR, CellAnimator.failuresSoFar));
        double durationInMillis = AnimationPlayer.VISUALIZATION_SPEED * 1000 * accelerateBy * 3 / 2;
        return Math.max(1, durationInMillis);
    }

    //EFFECTS: generate the transition effects relating to the change in color and size
    private static ParallelTransition createAnimationTransition(Cell cell, double durationInMillis,
                                                         List<Color> animationColors) {
        Duration duration = new Duration(durationInMillis);

        MultiFillTransition circleFillTransition = new MultiFillTransition(duration,
                cell.animatingCircle, animationColors);

        FillTransition rectangleFillTransition = new FillTransition(duration, cell.fill);
        rectangleFillTransition.setToValue(animationColors.get(animationColors.size() - 1));

        ScaleTransition scaleClipTransition = new ScaleTransition(new Duration(durationInMillis / 2), cell);
        scaleClipTransition.setByX(0.1);
        scaleClipTransition.setByY(0.1);
        scaleClipTransition.setAutoReverse(true);
        scaleClipTransition.setCycleCount(2);
        scaleClipTransition.setDelay(new Duration(durationInMillis / 2));
        ScaleTransition scaleTransition = new ScaleTransition(duration, cell.animatingCircle);
        scaleTransition.setFromX(0);
        scaleTransition.setFromY(0);
        scaleTransition.setToY(cell.getHeight() / Math.sqrt(2) + 1);
        scaleTransition.setToX(cell.getWidth() / Math.sqrt(2) + 1);

        ParallelTransition cellTransitions = new ParallelTransition(scaleClipTransition, scaleTransition);
        SequentialTransition fillTransitions = new SequentialTransition();

        fillTransitions.getChildren().addAll(circleFillTransition.getValue(),
                rectangleFillTransition);

        return new ParallelTransition(fillTransitions, cellTransitions);
    }

    //EFFECTS: produces a list of colors to be animated through given a particular animation state
    private static List<Color> getAnimationColorsGivenState(Tile.AnimationState state) {
        ArrayList<Color> animationColors = new ArrayList<>();

        switch (state) {
            case CURRENT:
                break;
            case VISITED:
                animationColors.add(Color.LIGHTPINK);
                animationColors.add(Color.PALEVIOLETRED);
                animationColors.add(Color.BLUEVIOLET);
                break;
            case PATH:
                animationColors.add(Color.LIGHTGOLDENRODYELLOW);
                animationColors.add(Color.YELLOW);
                animationColors.add(Color.PALETURQUOISE);
                animationColors.add(Color.TURQUOISE);
                break;
            case FAILURE:
                animationColors.add(Color.DARKGREY);
        }

        return animationColors;
    }
}
