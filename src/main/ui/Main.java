//TODO: EDGE-CASE HANDLING FOR TEXTFIELD INPUTS
//TODO: FIX INITIAL VISUALISATION SPEED TO MATCH INITIAL SLIDER VALUE
//TODO: DON'T ALLOW USER TO DRAG OUTSIDE GRIDPANE
//TODO: REMEMBER TYPE BEFORE DRAG TO RESTORE


package ui;

import javafx.application.Application;
import javafx.stage.Stage;

//serves as the entry point of the application
public class Main extends Application {
    public static Grid grid;
    public static Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Main.primaryStage = primaryStage;
        Main.grid = SceneAssembler.assembleScene(primaryStage);
    }
}
