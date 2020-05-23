package ui;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Maze;
import util.Pair;

import java.util.*;

//responsible for creating and layouting out all elements of the UI
public class SceneAssembler {
    private static final double height = 750;
    private static final double width = 1000;
    private static Maze myMaze = generateMaze();
    private static Map<String, Node> elements = new LinkedHashMap<>();

    //MODIFIES: this, primarySage
    //EFFECTS: assembles all the UI elements
    public static Grid assembleScene(Stage primaryStage) {
        elements.put("menuCanvas",  SceneCreator.createRectangle(Color.LIGHTCORAL, 0, 0, 300, height));
        elements.put("grid", SceneCreator.createGrid(width - 300, height, myMaze));
        elements.put("visualizeButton", SceneCreator.createButton("VISUALIZE!", 295, 95));
        elements.put("saveButton", SceneCreator.createButton("Save Maze!", 295, 95));
        elements.put("loadButton", SceneCreator.createButton("Load Maze!", 295, 95));
        elements.put("removeObstaclesButton", SceneCreator.createButton("Clear all obstacles", 250, 85));
        elements.put("dimensionText", SceneCreator.createText("Set custom dimensions to the maze:"));
        elements.put("textFieldX", SceneCreator.createTextField("X"));
        elements.put("textFieldY", SceneCreator.createTextField("Y"));
        elements.put("applyDimensionsButton", SceneCreator.createButton("Apply", 115, 30));
        elements.put("sliderText", SceneCreator.createText("Visualization speed:"));
        elements.put("slider", SceneCreator.createSlider(0, (float) Math.sqrt(2), 0.25f, 285, 25));

        anchorAllElements();
        assignActionsToAllElements();

        setStage(primaryStage);

        return (Grid) elements.get("grid");
    }

    //MODIFIES: this
    //EFFECTS: adds constraints to all the UI elements
    private static void anchorAllElements() {
        anchorElement(elements.get("menuCanvas"), 0.0, null, 0.0, 0.0);
        anchorElement(elements.get("grid"), null, 5.0, 5.0, 5.0);
        anchorElement(elements.get("visualizeButton"), 0.0, null, 0.0, null);
        anchorElement(elements.get("saveButton"), 0.0, null, 125.0, null);
        anchorElement(elements.get("loadButton"), 0.0, null, 225.0, null);
        anchorElement(elements.get("slider"), 10.0, null, null, 10.0);
        anchorElement(elements.get("sliderText"), 10.0, null, null, 10.0 + 25.0 + 5.0);
        anchorElement(elements.get("removeObstaclesButton"), 25.0, null, null, 325.0);
        anchorElement(elements.get("applyDimensionsButton"), 15.0, null, 565.0, null);
        anchorElement(elements.get("dimensionText"), 10.0, null, 500.0, null);
        anchorElement(elements.get("textFieldX"), 15.0, null, 525.0, null);
        anchorElement(elements.get("textFieldY"), 80.0, null, 525.0, null);
    }

    //MODIFIES: this
    //EFFECTS: assigns action or listener handlers to all relevant UI elements
    private static void assignActionsToAllElements() {
        Button visualizeButton = (Button) elements.get("visualizeButton");
        Button saveButton = (Button) elements.get("saveButton");
        Button loadButton = (Button) elements.get("loadButton");
        Slider slider = (Slider) elements.get("slider");
        Button removeObstaclesButton = (Button) elements.get("removeObstaclesButton");
        Button applyDimensionsButton = (Button) elements.get("applyDimensionsButton");
        TextField textFieldX = (TextField) elements.get("textFieldX");
        TextField textFieldY = (TextField) elements.get("textFieldY");

        visualizeButton.setOnAction(ActionManager.createVisualizeButtonAction());
        saveButton.setOnAction(ActionManager.createSaveButtonAction());
        loadButton.setOnAction(ActionManager.createLoadButtonAction());
        slider.valueProperty().addListener(ActionManager.createSliderListener());
        removeObstaclesButton.setOnAction(ActionManager.createRemoveObstaclesButtonAction());
        applyDimensionsButton.setOnAction(ActionManager.createApplyDimensionsButtonAction(textFieldX, textFieldY));
    }

    //MODIFIES: primaryStage
    //EFFECTS: assembles the stage; final step in the process
    private static void setStage(Stage primaryStage) {
        Pane root = new AnchorPane();
        root.getChildren().addAll(elements.values());
        Scene scene = new Scene(root, width, height);
        primaryStage.setTitle("aMAZEing Visualizer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //EFFECTS: creates a new maze with arbitrary modifications such as obstacles and start/end positions
    public static Maze generateMaze() {
        Maze maze = new Maze();
        maze.changeStart(1, 1);
        ArrayList<Pair<Integer, Integer>> obstacleCoords = new ArrayList<>();
        obstacleCoords.add(new Pair<>(0, 1));
        obstacleCoords.add(new Pair<>(1, 5));
        obstacleCoords.add(new Pair<>(2, 4));
        obstacleCoords.add(new Pair<>(3, 3));
        obstacleCoords.add(new Pair<>(4, 2));
        obstacleCoords.add(new Pair<>(4, 1));

        for (Pair<Integer, Integer> coord : obstacleCoords) {
            maze.addObstacle(coord.first, coord.second);
        }

        return maze;
    }

    //MODIFIES: element
    //EFFECTS: constrains the given node by the given parameters
    //         if a parameter is null, then element will not be constrained in that direction
    private static void anchorElement(Node element, Double left, Double right, Double up, Double down) {
        if (element == null) {
            return;
        }
        if (left != null) {
            AnchorPane.setLeftAnchor(element, left);
        }
        if (right != null) {
            AnchorPane.setRightAnchor(element, right);
        }
        if (up != null) {
            AnchorPane.setTopAnchor(element, up);
        }
        if (down != null) {
            AnchorPane.setBottomAnchor(element, down);
        }
    }
}
