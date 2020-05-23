package ui;

import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import model.Maze;

//helper class to easily create various elements of the UI
public class SceneCreator {

    //EFFECTS: produces a button with the given parameters
    public static Button createButton(String text, double w, double h) {
        Button btn = new Button();
        btn.setText(text);
        btn.setMaxWidth(w);
        btn.setMinWidth(w);
        btn.setMinHeight(h);
        btn.setMaxHeight(h);
        return btn;
    }

    //EFFECTS: produces a rectangle with the given parameters
    public static Rectangle createRectangle(Paint fill, double x, double y, double w, double h) {
        Rectangle rect = new Rectangle();
        rect.setFill(fill);
        rect.setX(x);
        rect.setY(y);
        rect.setWidth(w);
        rect.setHeight(h);
        return rect;
    }

    //EFFECTS: produces a rectangle with the given parameters
    public static Rectangle createRectangle(double x, double y, double w, double h) {
        Rectangle rect = new Rectangle();
        rect.setFill(Color.WHITE);
        rect.setX(x);
        rect.setY(y);
        rect.setWidth(w);
        rect.setHeight(h);
        return rect;
    }

    //EFFECTS: produces a rectangle with the given parameters
    public static Rectangle createRectangle(Paint fill, double w, double h) {
        Rectangle rect = new Rectangle();
        rect.setFill(fill);
        rect.setX(0);
        rect.setY(0);
        rect.setWidth(w);
        rect.setHeight(h);
        return rect;
    }

    //EFFECTS: produces a rectangle with the given parameters
    public static Rectangle createRectangle(double w, double h) {
        Rectangle rect = new Rectangle();
        rect.setFill(Color.WHITE);
        rect.setX(0);
        rect.setY(0);
        rect.setWidth(w);
        rect.setHeight(h);
        return rect;
    }

    //EFFECTS: produces a grid with the given parameters
    public static Grid createGrid(double w, double h, int r, int c) {
        Grid grid = new Grid(w, h, r, c);
        return grid;
    }

    //EFFECTS: produces a grid with the given parameters
    public static Grid createGrid(double w, double h, Maze maze) {
        Grid grid = new Grid(w, h, maze);
        return grid;
    }

    //EFFECTS: produces a circle with the given parameters
    public static Circle createCircle(int r, Paint color) {
        Circle circle = new Circle();
        circle.setRadius(r);
        circle.setFill(color);

        return circle;
    }

    //EFFECTS: produces a slider with the given parameters
    public static Slider createSlider(float min, float max, float defaultVal, double w, double h) {
        Slider slider = new Slider(min, max, defaultVal);
        slider.setMaxWidth(w);
        slider.setMinWidth(w);
        slider.setMaxHeight(h);
        slider.setMinHeight(h);
        return slider;
    }

    //EFFECTS: produces a text with the given parameters
    public static Text createText(String text) {
        Text textNode = new Text(text);

        return textNode;
    }

    //EFFECTS: produces a text field with the given parameters
    public static TextField createTextField(String hintText) {
        TextField textField = new TextField();
        textField.setPromptText(hintText);
        textField.setMaxWidth(45);
        textField.setMinWidth(45);
        return textField;
    }
}
