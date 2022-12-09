package com.example.ampaint;

import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Contains methods for the help menu.
 */
public class HelpMenu {
    /**
     * Opens a window that displays the features and version.
     */
    public static void about() {             // About
        Stage dialog = new Stage();                        // opens new window
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(PaintController.getStage());
        dialog.getIcons().add(new Image(AMPaint.class.getResourceAsStream("/other/about.png")));
        VBox about = new VBox();
        ScrollPane scroll = new ScrollPane(about);
        about.getChildren().add(new Text("Welcome to AMPaint!\n" // user can learn about the app and see the version
                + "This is a Paint application where you can draw for fun.\n\n\n"
                + "Version 1.5.0"));
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        Scene aboutBox = new Scene(scroll, 400, 300);                  // size of the window
        dialog.setTitle("About");
        dialog.setScene(aboutBox);
        dialog.show();
    }

    /**
     * Opens a window that describes how to use a feature from its menu or toolbar.
     */
    public static void help() {            // Help
        Stage dialog = new Stage();                        // opens new window
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(PaintController.getStage());
        dialog.getIcons().add(new Image(AMPaint.class.getResourceAsStream("/other/help.png")));
        VBox help = new VBox();
        ScrollPane scroll = new ScrollPane(help);
        help.getChildren().add(new Text("How to Navigate AMPaint App\n\n"
                + "File Menu:\n"
                + "Open - Import an image from your desktop as the canvas\n"
                + "Save - Save current canvas into the application\n"
                + "Save As - Save current canvas to your desktop as a png\n"
                + "Close - Exit application\n\n"
                + "Edit Menu:\n" + "Undo - Undoes current edit on canvas\n"
                + "Redo - Redoes current edit on canvas\n"
                + "Copy - Gets selected image and copies it to clipboard\n"
                + "Paste - Places selected image from clipboard on canvas\n\n"
                + "Shapes Menu:\n"
                + "Square - Draw a square from mouse drag\n"
                + "Rectangle - Draw a rectangle from mouse drag\n"
                + "Circle - Draw a circle from mouse drag\n"
                + "Ellipse - Draw a ellipse from mouse drag\n"
                + "Triangle - Draw a triangle from mouse drag\n"
                + "New Shape - Draw a custom polygon from angle and sides input\n\n"
                + "Rotate Menu:\n"
                + "Right 90 - Rotates canvas 90 degrees right\n"
                + "Left 90 - Rotates canvas 90 degrees left\n"
                + "Right 180 - Rotates canvas 180 degrees right\n"
                + "Left 180 - Rotates canvas 180 degrees left\n"
                + "Right 270 - Rotates canvas 270 degrees right\n"
                + "Left 270 - Rotates canvas 270 degrees left\n"
                + "Flip horizontal - Mirrors canvas over the x-axis\n"
                + "Flip vertical - Mirrors canvas over the y-axis\n\n"
                + "Extras Menu:\n"
                + "Dark and Light Mode toggling\n\n"
                + "ToolBar:\n"
                + "New Canvas button for opening a new canvas in a different tab\n"
                + "TextField for line thickness\n" + "Color Chooser for changing the color of the stroke\n"
                + "Pencil checkbox for activating the pencil\n"
                + "Straight line checkbox for activating the straight line tool\n"
                + "Dashed line checkbox for activating the dashed line tool\n"
                + "Eraser checkbox for activating the eraser\n"
                + "Clear Canvas button to erase the image but user can still draw on the blank canvas\n"
                + "Color Grabber button to get the color on a pixel with a mouse click\n"
                + "TextFields for canvas resizing (width and height) with its button"));
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        Scene aboutBox = new Scene(scroll, 400, 300);                  // size of the window
        dialog.setTitle("Help");
        dialog.setScene(aboutBox);
        dialog.show();
    }
}