package ui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.PathInformation;

import static ui.ActionManager.createOkButtonAction;

//class whose purpose is to display information regarding the path that the algorithm followed.
public class StatisticsDisplayer implements Runnable {

    private PathInformation pathInformation;

    //EFFECTs: constructs a StatisticsDisplayer object given the pertinent path information
    public StatisticsDisplayer(PathInformation pathInfo) {
        this.pathInformation = pathInfo;
    }

    @Override
    public void run() {
        final Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.initOwner(Main.primaryStage);
        AnchorPane canvas = new AnchorPane();
        Text possibleDialogue = new Text(pathInformation.possible ? "A path was found!" : "This maze is impossible...");
        Text timeDialogue = new Text("That took " + (int) pathInformation.timeTaken + " milliseconds.");
        Text visitedDialogue = new Text(pathInformation.numberNodesVisited + " nodes were visited.");
        Button okButton = SceneCreator.createButton("Ok", 150, 40);

        AnchorPane.setLeftAnchor(possibleDialogue, 10.0);
        AnchorPane.setTopAnchor(possibleDialogue, 10.0);

        AnchorPane.setLeftAnchor(timeDialogue, 10.0);
        AnchorPane.setTopAnchor(timeDialogue, 40.0);

        AnchorPane.setLeftAnchor(visitedDialogue, 10.0);
        AnchorPane.setTopAnchor(visitedDialogue, 60.0);

        AnchorPane.setRightAnchor(okButton, 0.0);
        AnchorPane.setRightAnchor(okButton, 0.0);
        AnchorPane.setBottomAnchor(okButton, 0.0);

        okButton.setOnAction(createOkButtonAction(popup));
        canvas.getChildren().addAll(possibleDialogue, okButton, timeDialogue, visitedDialogue);
        Scene dialogScene = new Scene(canvas, 300, 150);
        popup.setScene(dialogScene);
        popup.show();
    }
}
