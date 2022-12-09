package com.example.ampaint;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Where methods are called from different classes to make the paint program work.
 */
public class PaintController implements Initializable {

    @FXML private Label pixelColor;              // used for displaying a pixel's color (in hex)
    @FXML private Label canvasCd;
    @FXML private ScrollPane scroll;
    @FXML protected StackPane stack;
    @FXML private NewCanvas canvas;
    @FXML protected ColorPicker colorPicker;
    @FXML private CheckBox pencil;
    @FXML private CheckBox straight;
    @FXML private CheckBox dashed;
    @FXML protected CheckBox eraser;
    @FXML protected TextField brushSize;         // used for getting the line thickness
    @FXML private TextField width;               // used for getting the canvas width
    @FXML private TextField height;              // used for getting the canvas height
    @FXML private Button in;
    @FXML private Button out;
    @FXML private Button resize;
    @FXML protected ToggleButton colorGrabber;   // used for getting the color of a pixel
    @FXML private RadioMenuItem mode;            // used for toggling between dark and light mode
    @FXML private MenuItem open;
    @FXML private MenuItem save;
    @FXML private MenuItem saveAs;
    @FXML private MenuItem stop;
    @FXML private MenuItem close;
    @FXML private MenuItem undo;
    @FXML private MenuItem redo;
    @FXML private MenuItem copy;
    @FXML private MenuItem paste;
    @FXML private RadioMenuItem square;
    @FXML private RadioMenuItem rectangle;
    @FXML private RadioMenuItem circle;
    @FXML private RadioMenuItem ellipse;
    @FXML private RadioMenuItem triangle;
    @FXML private RadioMenuItem shape;

    public static Stage stage;                         // used for getting the stage from the parent class to the controller

    /**
     * Opens a file from a directory on user's computer. Files supported are png, jpeg, jpg, and bmp.
     * @see FileMenu#open(NewCanvas)
     */
    public void onOpenFile() {FileMenu.open(canvas);}                    // Open

    /**
     * Saves the canvas to a set directory.
     * @throws IOException to make sure the canvas is a screenshot and saved properly
     * @see FileMenu#save(StackPane)
     */
    public void onSave() throws IOException {FileMenu.save(stack);}      // Save

    /**
     * Saves the canvas to a directory on user's desktop or pc. It can save as any image file supported.
     * @see FileMenu#saveAs(NewCanvas, StackPane)
     */
    public void onSaveAs() {FileMenu.saveAs(canvas, stack);}             // Save As

    /**
     * Displays alert to warn user about saving the canvas before they leave. It pops up regardless if they saved it or not.
     * @see FileMenu#close(NewCanvas, StackPane)
     */
    public void onClose() {FileMenu.close(canvas, stack);}               // Close

    /**
     * Stops current thread.
     * @see LoggerHandle#cease()
     */
    public void onStop() {FileMenu.stop();}                              // Stop Current Thread

    /**
     * Undoes current canvas edit from undo stack.
     * @see EditMenu#undo(NewCanvas)
     */
    public void undo() {undo.setOnAction(e->EditMenu.undo(canvas));}      // Undo

    /**
     * Redoes current canvas edit from undo stack.
     * @see EditMenu#redo(NewCanvas)
     */
    public void redo() {redo.setOnAction(e->EditMenu.redo(canvas));}     // Redo

    /**
     * Copies a selected region of the canvas to clipboard.
     * @see EditMenu#copy(NewCanvas)
     */
    public void copy() {copy.setOnAction(e->EditMenu.copy(canvas));}    // Copy

    /**
     * Displays the region of the canvas on the canvas.
     * @see EditMenu#paste(NewCanvas)
     */
    public void paste() {paste.setOnAction(e->EditMenu.paste(canvas));}   // Paste

    /* methods for the help menu items */
    /**
     * Activates about window
     * @see HelpMenu#about()
     */
    public void onAbout() {HelpMenu.about();}    // About

    /**
     * Activates help window
     * @see HelpMenu#help()
     */
    public void onHelp() {HelpMenu.help();}      // Help

    /* methods for the extras menu */
    /**
     * Toggles between dark and light mode for canvas window.
     */
    public void toggleDarkMode() {    // for toggling between dark and light mode
        if(mode.isSelected()) {
            mode.setSelected(true);
            stage = (Stage) colorGrabber.getScene().getWindow();    // gets scene from AMPaint class
            stage.getScene().getRoot().setStyle("-fx-accent: #1e74c6;" +     // goes to dark mode
                    "       -fx-focus-color: -fx-accent;" +
                    "       -fx-base: #373e43;" +
                    "       -fx-control-inner-background: derive(-fx-base, 35%);" +
                    "       -fx-control-inner-background-alt: -fx-control-inner-background;");
            mode.setText("Light Mode");
            LoggerHandle.getLoggerHandle().writeToLog(true,"Dark Mode toggled on");
        }
        else {  // reset
            mode.setSelected(false);
            mode.setText("Dark Mode");
            stage.getScene().getRoot().setStyle("");    // goes back to light or "default" mode
            LoggerHandle.getLoggerHandle().writeToLog(true,"Dark Mode toggled off");
        }
    }

    /* methods for the shapes menu */
    /**
     * Draws a square from mouse events (pressed, dragged, released) and updates undo stack for undo.
     * Updates color and size from NewCanvas.java methods.
     * @see ShapesMenu#square(NewCanvas, TextField, ColorPicker, Label)
     */
    public void aSquare() {ShapesMenu.square(canvas, brushSize, colorPicker, canvasCd);}     // Square

    /**
     * Draws a circle from mouse events (pressed, dragged, released) and updates undo stack for undo.
     * Updates color and size from NewCanvas.java methods.
     * @see ShapesMenu#circle(NewCanvas, TextField, ColorPicker, Label)
     */
    public void aCircle() {ShapesMenu.circle(canvas, brushSize, colorPicker, canvasCd);}     // Circle

    /**
     * Draws a rectangle from mouse events (pressed, dragged, released) and updates undo stack for undo.
     * Updates color and size from NewCanvas.java methods.
     * @see ShapesMenu#rectangle(NewCanvas, TextField, ColorPicker, Label) 
     */
    public void aRectangle() {ShapesMenu.rectangle(canvas, brushSize, colorPicker, canvasCd);}   // Rectangle

    /**
     * Draws an ellipse from mouse events (pressed, dragged, released) and updates undo stack for undo.
     * Updates color and size from NewCanvas.java methods.
     * @see ShapesMenu#ellipse(NewCanvas, TextField, ColorPicker, Label) 
     */
    public void anEllipse() {ShapesMenu.ellipse(canvas, brushSize, colorPicker, canvasCd);}      // Ellipse

    /**
     * Draws a triangle from mouse events (pressed, dragged, released) and updates undo stack for undo.
     * Updates color and size from NewCanvas.java methods.
     * @see ShapesMenu#triangle(NewCanvas, TextField, ColorPicker, Label) 
     */
    public void aTriangle() {ShapesMenu.triangle(canvas, brushSize, colorPicker, canvasCd);}     // Triangle

    /**
     * Draws a shape from the user's input on angle and sides and updates undo stack for undo.
     * Updates color and size from NewCanvas.java methods.
     * @see ShapesMenu#shape(NewCanvas, TextField, ColorPicker, Label) 
     * @see NewCanvas#setShapeAngle()
     * @see NewCanvas#setShapeSides() 
     */
    public void aShape() {ShapesMenu.shape(canvas, brushSize, colorPicker, canvasCd);}         // Shape

    /* buttons on the toolbar */
    /**
     * Draws line stroke from mouse events (pressed, dragged, released) and updates undo stack for undo.
     * Updates color and size from NewCanvas.java methods.
     */
    public void pencil() {Toolbar.pencil(canvas, brushSize, colorPicker, canvasCd);}   // Pencil (default tool)

    /**
     * Draws a straight line from mouse events (pressed, dragged, released) and updates undo stack for undo.
     * Updates color and size from NewCanvas.java methods.
     */
    public void line() {Toolbar.sLine(canvas, brushSize, colorPicker, canvasCd);}     // Straight Line

    /**
     * Draws a dashed line from mouse events (pressed, dragged, released) and updates undo stack for undo.
     * Updates color and size from NewCanvas.java methods.
     */
    public void dashed() {Toolbar.dLine(canvas, brushSize, colorPicker, canvasCd);}   // Dashed Line

    /**
     * Erases strokes and updates undo stack for undo.
     * @see Toolbar#eraser(NewCanvas, CheckBox, TextField, ColorPicker, Label)
     */
    public void erase() {Toolbar.eraser(canvas, eraser, brushSize, colorPicker, canvasCd);}    // Eraser

    /**
     * Calls clearCanvas() from NewCanvas.java to be executed.
     * @see NewCanvas#clearCanvas()
     */
    public void onClearCanvas() {Toolbar.clear(canvas);}    // Clear

    /**
     * Displays color from a pixel on mouse click.
     */
    public void onColorGrab() {Toolbar.colorGrab(canvas, pixelColor, colorGrabber);}   // gets a color of a pixel with mouse click

    /**
     * Resizes canvas to user's dimensions input.
     * @see Toolbar#resize(NewCanvas, TextField, TextField) 
     */
    public void onResizeCanvas() {Toolbar.resize(canvas, width, height);}     // Resize

    /**
     * Zooms in on the canvas.
     * @see Toolbar#zoomIn(StackPane, ScrollPane)
     */
    public void zoomIn() {Toolbar.zoomIn(stack, scroll);}        // +

    /**
     * Zooms out on the canvas.
     * @see Toolbar#zoomOut(StackPane, ScrollPane)
     */
    public void zoomOut() {Toolbar.zoomOut(stack, scroll);}      // -

    /* methods for the rotate menu */
    /**
     * Rotates canvas right 90 degrees.
     */
    public void onRotateRight90() {RotateMenu.right90(stack, canvas);}      // Right 90

    /**
     * Rotates canvas left 90 degrees.
     */
    public void onRotateLeft90() {RotateMenu.left90(stack, canvas);}       // Left 90

    /**
     * Rotates canvas right 180 degrees.
     */
    public void onRotateRight180() {RotateMenu.right180(stack, canvas);}    // Right 180

    /**
     * Rotates canvas left 180 degrees.
     */
    public void onRotateLeft180() {RotateMenu.left180(stack, canvas);}       // Left 180

    /**
     * Rotates canvas right 270 degrees.
     */
    public void onRotateRight270() {RotateMenu.right270(stack, canvas);}     // Right 270

    /**
     * Rotates canvas left 270 degrees.
     */
    public void onRotateLeft270() {RotateMenu.left270(stack, canvas);}      // Left 270

    /**
     * Flips canvas horizontally.
     */
    public void flipHorizontal() {RotateMenu.horizontal(canvas);}           // Horizontal

    /**
     * Flips canvas vertically.
     */
    public void flipVertical() {RotateMenu.vertical(canvas);}               // Vertical

    /**
     * Displays the mouse coordinates from the canvas.
     */
    public void coordinates() {
        canvas.setOnMouseMoved(e-> canvasCd.setText(e.getX() + ", " + e.getY() + "px"));
        canvas.setOnMouseExited(e-> canvasCd.setText("No Canvas Detected"));
    }

    /**
     * Sets the stage for this class (from primary class).
     * @param stage main stage
     */
    public void setStage(Stage stage) {PaintController.stage = stage;}

    /**
     * Gets the stage for this class.
     * @return stage
     */
    public static Stage getStage() {return stage;}


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String string[] = {""};
        LoggerHandle.main(string);
        LoggerHandle.getLoggerHandle().writeToLog(false," AMPaint loaded successfully");
        canvas.getApp();
        pencil();   // loads with default tool
        coordinates();   // automatically gets the canvas's coordinates

        new ImageIcons(open, new Image(AMPaint.class.getResourceAsStream("/menu/open.png")));
        new ImageIcons(save, new Image(AMPaint.class.getResourceAsStream("/menu/save.png")));
        new ImageIcons(saveAs, new Image(AMPaint.class.getResourceAsStream("/menu/saveAs.png")));
        new ImageIcons(stop, new Image(AMPaint.class.getResourceAsStream("/menu/stop.png")));
        new ImageIcons(close, new Image(AMPaint.class.getResourceAsStream("/menu/close.png")));
        new ImageIcons(pencil, new Image(AMPaint.class.getResourceAsStream("/menu/pencil.png")));
        new ImageIcons(straight, new Image(AMPaint.class.getResourceAsStream("/menu/straight.png")));
        new ImageIcons(dashed, new Image(AMPaint.class.getResourceAsStream("/menu/dashed.png")));
        new ImageIcons(undo, new Image(AMPaint.class.getResourceAsStream("/menu/undo.jpg")));
        new ImageIcons(redo, new Image(AMPaint.class.getResourceAsStream("/menu/redo.png")));
        new ImageIcons(copy, new Image(AMPaint.class.getResourceAsStream("/menu/copy.png")));
        new ImageIcons(paste, new Image(AMPaint.class.getResourceAsStream("/menu/paste.jpg")));
        new ImageIcons(square, new Image(AMPaint.class.getResourceAsStream("/menu/square.png")));
        new ImageIcons(rectangle, new Image(AMPaint.class.getResourceAsStream("/menu/rectangle.png")));
        new ImageIcons(circle, new Image(AMPaint.class.getResourceAsStream("/menu/circle.png")));
        new ImageIcons(ellipse, new Image(AMPaint.class.getResourceAsStream("/menu/ellipse.png")));
        new ImageIcons(triangle, new Image(AMPaint.class.getResourceAsStream("/menu/triangle.jpg")));
        new ImageIcons(shape, new Image(AMPaint.class.getResourceAsStream("/menu/new.png")));
        new ImageIcons(eraser, new Image(AMPaint.class.getResourceAsStream("/toolbar/eraser.png")));
        new ImageIcons(colorGrabber, new Image(AMPaint.class.getResourceAsStream("/toolbar/colorPick.png")));
        new ImageIcons(resize, new Image(AMPaint.class.getResourceAsStream("/toolbar/resize.png")));
        new ImageIcons(in, new Image(AMPaint.class.getResourceAsStream("/toolbar/plus.png")));
        new ImageIcons(out, new Image(AMPaint.class.getResourceAsStream("/toolbar/minus.png")));
        new ImageIcons(canvasCd, new Image(AMPaint.class.getResourceAsStream("/other/movecursor.png")));
    }
}