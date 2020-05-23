package ui;

import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.Tile;
import model.Tile.AnimationState;
import java.io.File;

//represents one node in the maze which can animate, we interacted with, and which conveys information from Tiles
public class Cell extends DragAndDroppable {
    ImageView imageView;
    Rectangle fill;
    Circle animatingCircle;
    Tile.TileType type;
    AnimationState animationState = AnimationState.DEFAULT;
    Rectangle clip;

    //EFFECTS: constructs a new cell object with the given width and height
    public Cell(double w, double h) {
        super();
        this.type = Tile.TileType.EMPTY;
        this.animatingCircle = SceneCreator.createCircle(0, Color.WHITE);
        this.imageView = new ImageView();
        imageView.fitWidthProperty().bind(this.widthProperty());
        imageView.fitHeightProperty().bind(this.heightProperty());
        fill = new Rectangle();
        fill.setFill(Color.TRANSPARENT);
        fill.heightProperty().bind(this.heightProperty());
        fill.widthProperty().bind(this.widthProperty());
        this.getChildren().addAll(fill, animatingCircle, imageView);
        this.setMinHeight(h);
        this.setMaxHeight(h);
        this.setMinWidth(w);
        this.setMaxWidth(w);
        clip = new Rectangle(w, h);
        this.setClip(clip);
        this.setStyle("-fx-background-color: transparent;");
    }

    public void setIcon(Image icon) {
        this.imageView.setImage(icon);
    }

    public enum Icon {
        START("start.png"),
        TARGET("target.png"),
        OBSTACLE("obstacle.png");

        private String value;

        public String getValue() {
            String path = "./assets/";
            File file = new File(path + this.value);
            return file.toURI().toString();
        }

        Icon(String value) {
            this.value = value;
        }
    }

    //MODIFIES: this
    //EFFECTS: make this cell an obstacle cell
    public void setToObstacle() {
        this.setIcon(new Image(Icon.OBSTACLE.getValue()));
        this.type = Tile.TileType.OBSTACLE;
    }

    //MODIFIES: this
    //EFFECTS: make this cell a start cell
    public void setToStart() {
        this.setIcon(new Image(Icon.START.getValue()));
        this.type = Tile.TileType.START;
    }

    //MODIFIES: this
    //EFFECTS: make this cell a target cell
    public void setToTarget() {
        this.setIcon(new Image(Icon.TARGET.getValue()));
        this.type = Tile.TileType.TARGET;
    }

    //MODIFIES: this
    //EFFECTS: make this cell an empty cell
    public void setToEmpty() {
        this.setIcon(null);
        this.type = Tile.TileType.EMPTY;
    }

    //MODIFIES: this
    //EFFECTS: handle the DragDetected event
    protected void handleDragDetected(MouseEvent event) {
        if (this.type.equals(Tile.TileType.START) || this.type.equals(Tile.TileType.TARGET)) {
            Dragboard db = this.startDragAndDrop(TransferMode.ANY);
            db.setDragView(new Image(new File("./assets/transparent.png").toURI().toString()));
            setCursor(Cursor.CLOSED_HAND);
            ClipboardContent content = new ClipboardContent();
            content.putString(this.type.toString());
            db.setContent(content);
            this.type = Tile.TileType.EMPTY;
            event.consume();

        } else {
            startFullDrag();
        }

    }

    //MODIFIES: this
    //EFFECTS: handle the MouseClicked event
    protected void handleMouseClick(MouseEvent event) {
        becomeObstacle();
    }

    protected void handleMouseDragEntered(MouseEvent event) {
        if (this.type.equals(Tile.TileType.START) || this.type.equals(Tile.TileType.TARGET)) {
            return;
        }
        becomeObstacle();
    }

    //EFFECTS: handle the DragOver event
    protected void handleDragOver(DragEvent event) {
        if (event.getDragboard().hasString()) {
            event.acceptTransferModes(TransferMode.ANY);
        }

        event.consume();
    }

    //MODIFIES: this
    //EFFECTS: handle the DragDropped event
    protected void handleMouseDragDropped(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;

        if (this.isNotSpecialCell()) {
            if (db.hasString()) {
                editCellGivenDragboard(this, db);
                success = true;
            }
        } else {
            editCellGivenDragboard((Cell) event.getGestureSource(), db);
        }

        event.setDropCompleted(success);

        event.consume();
    }

    //EFFECTS: produces true if this cell is NOT a START cell or a TARGET cell
    protected boolean isNotSpecialCell() {
        return !this.type.equals(Tile.TileType.START) && !this.type.equals(Tile.TileType.TARGET);
    }

    //MODIFIES: this
    //EFFECTS: modifies appearence of cell during a dragging event
    protected void editCellGivenDragboard(Cell cell, Dragboard db) {
        switch (db.getString()) {
            case "START":
                Main.grid.setStart(cell);
                break;
            case "TARGET":
                Main.grid.setTarget(cell);
                break;
            //TODO: add portal
        }
    }

    //MODIFIES: this
    //EFFECTS: handle the DragEntered event
    protected void handleDragEntered(DragEvent event) {
        if (this.isNotSpecialCell()) {
            if (event.getDragboard().hasString()) {
                Image image;
                switch (event.getDragboard().getString()) {
                    case "START":
                        image = new Image(Icon.START.getValue());
                        break;
                    case "TARGET":
                        image = new Image(Icon.TARGET.getValue());
                        break;
                    //TODO: add portal later
                    default:
                        image = null;
                }
                this.setIcon(image);
            }
        }
        event.consume();
    }

    //MODIFIES: this
    //EFFECTS: handle the DragExited event
    protected void handleDragExited(DragEvent event) {
        if (this.isNotSpecialCell()) {
            if (event.getDragboard().hasString()) {
                this.setIcon(null);
            }
        }
        event.consume();
    }

    //MODIFIES: this
    //EFFECTS: make this an obstacle cell and change the maze to reflect this change
    private void becomeObstacle() {
        if (this.isNotSpecialCell()) {
            int col = GridPane.getColumnIndex(this);
            int row = GridPane.getRowIndex(this);

            if (this.type == Tile.TileType.OBSTACLE) {
                Main.grid.removeObstacle(col, row);
            } else {
                Main.grid.addObstacle(col, row);
            }
        }
    }

    //MODIFIES: this
    //EFFECTS: reset cell to default appearance
    public void resetToDefaultState() {
        this.animationState = AnimationState.DEFAULT;
        this.fill.setFill(Color.WHITE);
        this.animatingCircle.setRadius(0);
        this.animatingCircle.setFill(Color.WHITE);
    }
}


