package ui;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Maze;
import model.PathInformation;
import model.PathfinderManager;
import persistence.Reader;
import persistence.Writer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

//responsible for creating the action handling or listening of all the buttons and elements of UI
//essentially a database of methods
public class ActionManager {

    //EFFECTS: produces event handle responsible for clicking visualize button
    public static EventHandler<ActionEvent> createVisualizeButtonAction() {
        return e -> {
            if (!Main.grid.getStatus().equals(Grid.VisualisationStatus.IN_PROGRESS)) {
                if (Main.grid.getStatus().equals(Grid.VisualisationStatus.DONE)) {
                    Main.grid.unanimateCells();
                }

                PathfinderManager pathfinderManager = new PathfinderManager(Main.grid.mazeSource);
                PathInformation pathInfo = pathfinderManager.runAStar();
                AnimationPlayer player = new AnimationPlayer(pathInfo.animations);
                StatisticsDisplayer statDisplayer = new StatisticsDisplayer(pathInfo);
                player.playAnimations(statDisplayer);
                Main.grid.setStatus(Grid.VisualisationStatus.IN_PROGRESS);
                Main.grid.setMouseTransparent(true);
            }
        };
    }

    //EFFECTS: produces event handle responsible for clicking save button
    public static EventHandler<ActionEvent> createSaveButtonAction() {
        return e -> {
            final Stage popup = new Stage();
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.initOwner(Main.primaryStage);
            AnchorPane canvas = new AnchorPane();
            Text dialogue = new Text("What name would you like to give this maze?");
            TextField textField = new TextField();
            Button cancelButton = SceneCreator.createButton("Cancel", 150, 40);
            Button doneButton = SceneCreator.createButton("Done", 150, 40);
            AnchorPane.setLeftAnchor(dialogue, 10.0);
            AnchorPane.setTopAnchor(dialogue, 10.0);
            AnchorPane.setLeftAnchor(textField, 10.0);
            AnchorPane.setTopAnchor(textField, 30.0);
            AnchorPane.setLeftAnchor(cancelButton, 0.0);
            AnchorPane.setBottomAnchor(cancelButton, 0.0);
            AnchorPane.setRightAnchor(doneButton, 0.0);
            AnchorPane.setBottomAnchor(doneButton, 0.0);
            cancelButton.setOnAction(createCancelButtonAction(popup));
            doneButton.setOnAction(createDoneButtonAction(popup, textField, Main.grid.mazeSource));
            canvas.getChildren().addAll(dialogue, textField, cancelButton, doneButton);
            popup.setScene(new Scene(canvas, 300, 150));
            popup.show();
        };
    }

    //EFFECTS: produces event handle responsible for clicking cancel button
    public static EventHandler<ActionEvent> createCancelButtonAction(Stage stage) {
        return e -> stage.close();
    }

    //EFFECTS: produces event handle responsible for clicking done button in save popup
    public static EventHandler<ActionEvent> createDoneButtonAction(Stage stage, TextField textField, Maze maze) {
        return e -> {
            String name = textField.getText();
            File path = new File("./data");
            if (path.exists()) {
                File newFile = new File(path, name + ".txt");
                try {
                    newFile.createNewFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                Writer writer = null;
                try {
                    writer = new Writer(newFile);
                } catch (FileNotFoundException | UnsupportedEncodingException exe) {
                    exe.printStackTrace();
                }
                writer.write(maze);
                System.out.println("Saved maze successfully!");
            }
            stage.close();
        };
    }

    //EFFECTS: produces event handle responsible for clicking load button
    public static EventHandler<ActionEvent> createLoadButtonAction() {
        return e -> {
            final Stage popup = new Stage();
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.initOwner(Main.primaryStage);
            Text dialogue = new Text("Which maze would you like to load?");
            ComboBox<String> dropDownList = new ComboBox<String>();
            dropDownList.getItems().addAll(getFileNamesFromFolder(new File("./data")));
            Button cancelButton = SceneCreator.createButton("Cancel", 150, 40);
            Button confirmButton = SceneCreator.createButton("Confirm", 150, 40);
            AnchorPane.setLeftAnchor(dialogue, 10.0);
            AnchorPane.setTopAnchor(dialogue, 10.0);
            AnchorPane.setLeftAnchor(dropDownList, 10.0);
            AnchorPane.setTopAnchor(dropDownList, 30.0);
            AnchorPane.setLeftAnchor(cancelButton, 0.0);
            AnchorPane.setBottomAnchor(cancelButton, 0.0);
            AnchorPane.setRightAnchor(confirmButton, 0.0);
            AnchorPane.setBottomAnchor(confirmButton, 0.0);
            cancelButton.setOnAction(createCancelButtonAction(popup));
            confirmButton.setOnAction(createConfirmButtonAction(popup, dropDownList));
            AnchorPane canvas = new AnchorPane(dialogue, dropDownList, cancelButton, confirmButton);
            popup.setScene(new Scene(canvas, 300, 150));
            popup.show();
        };
    }

    //EFFECTS: produces event handle responsible for clicking confirm button in load popup
    public static EventHandler<ActionEvent> createConfirmButtonAction(Stage stage, ComboBox<String> dropDownList) {
        return e -> {
            String selection = dropDownList.getValue();
            ArrayList<String> currentSaves = getFileNamesFromFolder(new File("./data"));
            if (currentSaves.contains(selection)) {
                File dataSource = new File("./data/" + selection + ".txt");
                Reader reader = new Reader();
                try {
                    Maze newMaze = reader.readMaze(dataSource);
                    Main.grid.changeMaze(newMaze);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                stage.close();
            }
        };
    }

    //EFFECTS: returns name of all files (without .txt) in given folder directory
    private static ArrayList<String> getFileNamesFromFolder(final File folder) {
        //taken from https://stackoverflow.com/questions/1844688/how-to-read-all-files-in-a-folder-from-java
        ArrayList<String> fileNames = new ArrayList<>();

        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                getFileNamesFromFolder(fileEntry);
            } else {
                String rawName = fileEntry.getName();
                //remove .txt at end of file name
                String name = rawName.substring(0, rawName.length() - 4);
                fileNames.add(name);
            }
        }

        return fileNames;
    }

    //EFFECTS: produces event handle responsible for changing value of visualize slider
    public static ChangeListener<Number> createSliderListener() {
        return (ov, oldVal, newVal) -> {
            AnimationPlayer.VISUALIZATION_SPEED = 0.05 + (double) newVal * (double) newVal;
        };
    }

    //EFFECTS: produces event handle responsible for clicking ok button in statistics popup
    public static EventHandler<ActionEvent> createOkButtonAction(Stage stage) {
        return e -> stage.close();
    }

    //EFFECTS: produces event handle responsible for clicking remove all obstacles button
    public static EventHandler<ActionEvent> createRemoveObstaclesButtonAction() {
        return e -> {
            if (!Main.grid.getStatus().equals(Grid.VisualisationStatus.IN_PROGRESS)) {
                Main.grid.removeAllObstacles();
            }
        };
    }

    //EFFECTS: produces event handle responsible for clicking apply (dimensions) button
    public static EventHandler<ActionEvent> createApplyDimensionsButtonAction(TextField textFieldX,
                                                                              TextField textFieldY) {
        return e -> {
            if (!Main.grid.getStatus().equals(Grid.VisualisationStatus.IN_PROGRESS)) {
                int x = Integer.parseInt(textFieldX.getText());
                int y = Integer.parseInt(textFieldY.getText());
                Maze newMaze = new Maze(x, y);
                Main.grid.changeMaze(newMaze);
            }
        };
    }
}
