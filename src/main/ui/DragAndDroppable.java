package ui;

import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

//describes a stackPane which has drag-and-drop functionality
public abstract class DragAndDroppable extends StackPane {
    //EFFECTS: constructs new DragAndDroppable object
    public DragAndDroppable() {
        this.initializeInteractionHandling();
    }

    //MODIFIES: this
    //EFFECTS: initializes event handlers related to dragging for this object
    private void initializeInteractionHandling() {
        this.setOnMouseClicked(this::handleMouseClick);
        this.setOnDragDetected(this::handleDragDetected);
        this.setOnMouseDragEntered(this::handleMouseDragEntered);
        this.setOnDragOver(this::handleDragOver);
        this.setOnDragDropped(this::handleMouseDragDropped);
        this.setOnDragEntered(this::handleDragEntered);
        this.setOnDragExited(this::handleDragExited);
    }

    //EFFECTS: defines functionality for onMouseClick
    protected abstract void handleMouseClick(MouseEvent event);

    //EFFECTS: defines functionality for onDragDetected
    protected abstract void handleDragDetected(MouseEvent event);

    //EFFECTS: defines functionality for onMouseDragEntered
    protected abstract void handleMouseDragEntered(MouseEvent event);

    //EFFECTS: defines functionality for onDragOver
    protected abstract void handleDragOver(DragEvent event);

    //EFFECTS: defines functionality for onDragDropped
    protected abstract void handleMouseDragDropped(DragEvent event);

    //EFFECTS: defines functionality for onDragEntered
    protected abstract void handleDragEntered(DragEvent event);

    //EFFECTS: defines functionality for onDragExited
    protected abstract void handleDragExited(DragEvent event);
}
