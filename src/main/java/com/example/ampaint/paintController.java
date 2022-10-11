package com.example.ampaint;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Where methods are called from different classes to make the paint program work.
 */
public class paintController implements Initializable {

    @FXML private Label pixelColor;              // used for displaying a pixel's color (in hex)
    @FXML protected NewCanvas canvas;
    @FXML protected ColorPicker colorPicker;
    @FXML protected CheckBox eraser;
    @FXML protected TextField brushSize;         // used for getting the line thickness
    @FXML private TextField width;               // used for getting the canvas width
    @FXML private TextField height;              // used for getting the canvas height
    @FXML public static TabPane tabPane;
    @FXML public static Tab tab;
    @FXML protected Button newTabButton;         // used for generating new canvases
    @FXML protected ToggleButton colorGrabber;   // used for getting the color of a pixel
    @FXML private RadioMenuItem mode;            // used for toggling between dark and light mode
    @FXML private MenuItem undo;
    @FXML private MenuItem redo;
    @FXML private MenuItem copy;
    @FXML private MenuItem paste;

    private int cWidth;
    private int cHeight;
    private static double x1, y1, x2, y2;       // used for getting the x and y coordinates from the mouse
    private File saved;
    public Stage stage;                         // used for getting the stage from the parent class to the controller

    /**
     * Opens a file from a directory on user's computer. Files supported are png, jpeg, jpg, and bmp.
     */
    public void onOpenFile() {   // Open
        FileChooser openedFile = new FileChooser();
        openedFile.setTitle("Open Image");
        openedFile.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files (*png,*jpg,*jpeg,*bmp)", "*.png", "*.jpg", "*.jpeg", "*.bmp"),
                new FileChooser.ExtensionFilter("All Files","*.*"));

        File fileObj = openedFile.showOpenDialog(stage);     // shows the file dialog
        Image image = new Image(fileObj.toURI().toString());
        canvas.drawImage(fileObj);   // canvas file
        canvas.drawImage(image);    // new canvas
        EditMenu.updateUndoStack(canvas);
    }

    /**
     * Saves the canvas to the application.
     * @throws IOException to make sure the canvas is a screenshot and saved properly
     */
    public void onSave() throws IOException {        // Save
        Image image = canvas.snapshot(null, null);
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", new File("newPaint.png"));   // saves in application
    }

    /**
     * Saves the canvas to a directory on user's desktop or pc. It can save as any image file.
     */
    public void onSaveAs() {       // Save As
        FileChooser savedFile = new FileChooser();
        savedFile.setTitle("Save Image");
        savedFile.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PNG", "*.png"),    // the different file types the user can save in
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("JPEG","*.jpeg"),
                new FileChooser.ExtensionFilter("BMP","*.bmp"));
        saved = savedFile.showSaveDialog(stage);       // shows the file dialog but in save mode
        if (saved != null) {
            try {
                WritableImage writImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getWidth());     // saves image in the width and height then
                canvas.snapshot(null, writImage);
                RenderedImage rendImage = SwingFXUtils.fromFXImage(writImage, null);
                ImageIO.write(rendImage, "png", saved);
                ImageIO.write(rendImage, "jpeg", saved);
                ImageIO.write(rendImage, "jpg", saved);
                ImageIO.write(rendImage, "bmp", saved);
            }
            catch (IOException ex) {
                System.out.println("Failed to save");
            }
        }
    }

    /**
     * Displays alert to warn user about saving the canvas before they leave. It pops up regardless if they saved it or not.
     */
    public void onClose() {     // Close
        if(saved == null) {
            Alert close = new Alert(Alert.AlertType.WARNING);    // Popup regardless if user saved or not but might change to when the file isn't saved
            close.setTitle("Warning");
            close.setHeaderText("Hold on!");
            close.setContentText("Want to save your files before you leave?");

            ButtonType save = new ButtonType("Yes");
            ButtonType dontSave = new ButtonType("No");
            ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            close.getButtonTypes().setAll(save, dontSave, cancel);    // 3 options
            Optional<ButtonType> result = close.showAndWait();
            if (result.get() == save) {onSaveAs(); Platform.exit();}    // opens the save dialog and exits after saved
            else if (result.get() == dontSave) {Platform.exit(); System.exit(0);}
        }
        else {Platform.exit(); System.exit(0);}
    }

    /**
     * Undo current canvas edit from undo stack.
     * @see EditMenu#undo(NewCanvas)
     */
    public void undo() {undo.setOnAction(e->EditMenu.undo(canvas));}

    /**
     * Redoes current canvas edit from undo stack.
     * @see EditMenu#redo(NewCanvas)
     */
    public void redo() {redo.setOnAction(e->EditMenu.redo(canvas));}

    /**
     * Copies a selected region of the canvas to clipboard.
     * @see EditMenu#copy(NewCanvas)
     */
    public void copy() {copy.setOnAction(e->EditMenu.copy(canvas));}

    /**
     * Displays the region of the canvas on the canvas.
     * @see EditMenu#paste(NewCanvas)
     */
    public void paste() {paste.setOnAction(e->EditMenu.paste(canvas));}

    /* methods for the help menu items */
    /**
     * Calls about() from HelpMenu.java to be displayed
     * @see HelpMenu#about()
     */
    public void onAbout() {
        HelpMenu about = new HelpMenu();      // references help menu
        about.about();                        // activates about window
    }

    /**
     * Calls help() from HelpMenu.java to be displayed
     * @see HelpMenu#help()
     */
    public void onHelp() {
        HelpMenu help = new HelpMenu();
        help.help();                         // activates help window
    }

    /* methods for the shapes menu */
    /**
     * Draws a square from mouse events (pressed, dragged, released) and updates undo stack for undo.
     * Calls drawSquare() from NewCanvas.java to execute square.
     * Updates color and size from NewCanvas.java methods.
     */
    public void aSquare() {     // draws a square
        canvas.setOnMousePressed(e-> {
            x1 = e.getX();
            y1 = e.getY();
        });

        canvas.setOnMouseDragged(e-> {
            x2 = e.getX();
            y2 = e.getY();
            EditMenu.updateUndoStack(canvas);
        });

        canvas.setOnMouseReleased(e-> {
            x2 = e.getX();
            y2 = e.getY();
            canvas.setLineWidth(Double.parseDouble(brushSize.getText()));
            canvas.setLineColor(colorPicker.getValue());
            canvas.drawSquare(x1, y1, x2, y2);
            EditMenu.updateUndoStack(canvas);
        });
    }

    /**
     * Draws a circle from mouse events (pressed, dragged, released) and updates undo stack for undo.
     * Calls drawCircle() from NewCanvas.java to execute circle.
     * Updates color and size from NewCanvas.java methods.
     */
    public void aCircle() {                  // draws a circle
        canvas.setOnMousePressed(e-> {
            x1 = e.getX();
            y1 = e.getY();
        });

        canvas.setOnMouseDragged(e-> {
            x2 = e.getX();
            y2 = e.getY();
            EditMenu.updateUndoStack(canvas);
        });

        canvas.setOnMouseReleased(e-> {
            x2 = e.getX();
            y2 = e.getY();
            canvas.setLineWidth(Double.parseDouble(brushSize.getText()));
            canvas.setLineColor(colorPicker.getValue());
            canvas.drawCircle(x1, y1, x2, y2);
            EditMenu.updateUndoStack(canvas);
        });
    }

    /**
     * Draws a rectangle from mouse events (pressed, dragged, released) and updates undo stack for undo.
     * Calls drawRectangle() from NewCanvas.java to execute rectangle.
     * Updates color and size from NewCanvas.java methods.
     */
    public void aRectangle() {         // draws a rectangle
        canvas.setOnMousePressed(e-> {
            x1 = e.getX();
            y1 = e.getY();
        });

        canvas.setOnMouseDragged(e-> {
            x2 = e.getX();
            y2 = e.getY();
            EditMenu.updateUndoStack(canvas);
        });

        canvas.setOnMouseReleased(e-> {
            x2 = e.getX();
            y2 = e.getY();
            canvas.setLineWidth(Double.parseDouble(brushSize.getText()));
            canvas.setLineColor(colorPicker.getValue());
            canvas.drawRectangle(x1,y1,x2,y2);
            EditMenu.updateUndoStack(canvas);
        });
    }

    /**
     * Draws an ellipse from mouse events (pressed, dragged, released) and updates undo stack for undo.
     * Calls drawEllipse() from NewCanvas.java to execute ellipse.
     * Updates color and size from NewCanvas.java methods.
     */
    public void anEllipse() {          // draws a filled ellipse
        canvas.setOnMousePressed(e-> {
            x1 = e.getX();
            y1 = e.getY();
        });

        canvas.setOnMouseDragged(e-> {
            x2 = e.getX();
            y2 = e.getY();
            EditMenu.updateUndoStack(canvas);
        });

        canvas.setOnMouseReleased(e-> {
            x2 = e.getX();
            y2 = e.getY();
            canvas.setLineWidth(Double.parseDouble(brushSize.getText()));
            canvas.setLineColor(colorPicker.getValue());
            canvas.drawEllipse(x1,y1,x2,y2);
            EditMenu.updateUndoStack(canvas);
        });
    }

    /**
     * Draws a triangle from mouse events (pressed, dragged, released) and updates undo stack for undo.
     * Calls drawTriangle() from NewCanvas.java to execute triangle.
     * Updates color and size from NewCanvas.java methods.
     */
    public void aTriangle() {          // draws a triangle pointing upwards
        canvas.setOnMousePressed(e-> {
            x1 = e.getX();
            y1 = e.getY();
        });

        canvas.setOnMouseDragged(e-> {
            x2 = e.getX();
            y2 = e.getY();
            EditMenu.updateUndoStack(canvas);
        });

        canvas.setOnMouseReleased(e-> {
            x2 = e.getX();
            y2 = e.getY();
            canvas.setLineWidth(Double.parseDouble(brushSize.getText()));
            canvas.setLineColor(colorPicker.getValue());
            canvas.drawTriangle(x1,y1,x2,y2);
            EditMenu.updateUndoStack(canvas);
        });
    }

    /**
     * Draws a shape from the user's input on angle and sides and updates undo stack for undo.
     * Calls drawShapeAngle() and drawShapeSides() from NewCanvas.java to get input.
     * Updates color and size from NewCanvas.java methods.
     */
    public void aShape() {          // draws a shape using specified number of sides and the angle it draws on
        canvas.setShapeAngle();
        canvas.setShapeSides();
        canvas.setOnMousePressed(e-> {
            x1 = e.getX();
            y1 = e.getY();
        });

        canvas.setOnMouseDragged(e-> {
            x2 = e.getX();
            y2 = e.getY();
            EditMenu.updateUndoStack(canvas);
        });

        canvas.setOnMouseReleased(e-> {
            x2 = e.getX();
            y2 = e.getY();
            canvas.setLineWidth(Double.parseDouble(brushSize.getText()));
            canvas.setLineColor(colorPicker.getValue());
            canvas.drawShape(canvas.getShapeAngle(),canvas.getShapeSides(),x1,y1,x2,y2);
            EditMenu.updateUndoStack(canvas);
        });
    }

    /**
     * Draws a straight line from mouse events (pressed, dragged, released) and updates undo stack for undo.
     * Updates color and size from NewCanvas.java methods.
     */
    public void line() {        // draws a straight line
        canvas.setOnMousePressed(e-> {
            canvas.getApp().beginPath();
            canvas.setLineColor(colorPicker.getValue());
            canvas.setLineWidth(Double.parseDouble(brushSize.getText()));
            canvas.getApp().lineTo(e.getX(), e.getY());
            canvas.getApp().stroke();
            EditMenu.updateUndoStack(canvas);
        });

        canvas.setOnMouseDragged(e-> {
            canvas.setLineWidth(Double.parseDouble(brushSize.getText()));
            canvas.getApp().lineTo(e.getX(), e.getY());
            canvas.getApp().stroke();
            EditMenu.updateUndoStack(canvas);
        });

        canvas.setOnMouseReleased(e-> {
            canvas.setLineColor(colorPicker.getValue());
            canvas.setLineWidth(Double.parseDouble(brushSize.getText()));
            canvas.getApp().lineTo(e.getX(), e.getY());
            canvas.getApp().stroke();
            canvas.getApp().closePath();
            EditMenu.updateUndoStack(canvas);
        });
    }

    /**
     * Draws a dashed line from mouse events (pressed, dragged, released) and updates undo stack for undo.
     * Updates color and size from NewCanvas.java methods.
     */
    public void dashed() {      // draws a dashed line
        canvas.setOnMousePressed(e -> {
            canvas.getApp().beginPath();
            canvas.getApp().setLineWidth(Double.parseDouble(brushSize.getText()));
            canvas.setLineColor(colorPicker.getValue());
            canvas.drawDashedLine(Double.parseDouble(brushSize.getText()), e.getX(), e.getY());
            canvas.getApp().stroke();
            EditMenu.updateUndoStack(canvas);
        });

        canvas.setOnMouseDragged(e -> {
            canvas.getApp().setLineWidth(Double.parseDouble(brushSize.getText()));
            canvas.setLineColor(colorPicker.getValue());
            canvas.drawDashedLine(Double.parseDouble(brushSize.getText()), e.getX(), e.getY());
            canvas.getApp().stroke();
            EditMenu.updateUndoStack(canvas);
        });

        canvas.setOnMouseReleased(e -> {
            canvas.setLineWidth(Double.parseDouble(brushSize.getText()));
            canvas.setLineColor(colorPicker.getValue());
            canvas.drawDashedLine(Double.parseDouble(brushSize.getText()), e.getX(), e.getY());
            canvas.getApp().stroke();
            canvas.getApp().closePath();
            EditMenu.updateUndoStack(canvas);
        });
    }

    /**
     * Draws line stroke from mouse events (pressed, dragged, released) and updates undo stack for undo.
     * Updates color and size from NewCanvas.java methods.
     */
    public void pencil() {            // default drawing tool
        canvas.setOnMousePressed(e-> {
            canvas.getApp().beginPath();
            canvas.setLineWidth(Double.parseDouble(brushSize.getText()));
            canvas.setLineColor(colorPicker.getValue());
            canvas.getApp().moveTo(e.getX(), e.getY());
            canvas.getApp().stroke();
            EditMenu.updateUndoStack(canvas);
        });

        canvas.setOnMouseDragged(e-> {
            canvas.getApp().lineTo(e.getX(), e.getY());
            canvas.setLineWidth(Double.parseDouble(brushSize.getText()));
            canvas.setLineColor(colorPicker.getValue());
            canvas.getApp().stroke();       // draws continuous line (no unwanted spaces)
            EditMenu.updateUndoStack(canvas);
        });

        canvas.setOnMouseReleased(e-> {
            canvas.getApp().lineTo(e.getX(), e.getY());
            canvas.setLineWidth(Double.parseDouble(brushSize.getText()));
            canvas.getApp().stroke();
            canvas.getApp().closePath();
            EditMenu.updateUndoStack(canvas);
        });
    }

    /**
     * Toggles between dark and light mode for canvas window.
     */
    public void toggleDarkMode() {    // for toggling between dark and light mode
        if(mode.isSelected()) {
            mode.setSelected(true);
            stage = (Stage) colorGrabber.getScene().getWindow();    // gets scene from AMPaint class
            stage.getScene().getRoot().setStyle("-fx-accent: #1e74c6;" +     // goes to dark mode
                    "    -fx-focus-color: -fx-accent;" +
                    "    -fx-base: #373e43;" +
                    "    -fx-control-inner-background: derive(-fx-base, 35%);" +
                    "    -fx-control-inner-background-alt: -fx-control-inner-background;");
            mode.setText("Light Mode");
        }
        else {  // reset
            mode.setSelected(false);
            mode.setText("Dark Mode");
            stage.getScene().getRoot().setStyle("");    // goes back to light or "default" mode
        }
    }

    /* buttons on the toolbar */

    /**
     */
    public void addNewTab() {
        tabPane.getTabs().add(new CanvasTab());
    }

    /**
     * Calls clearCanvas() from NewCanvas.java to be executed.
     * @see NewCanvas#clearCanvas()
     */
    public void onClearCanvas() {
        canvas.clearCanvas();
        EditMenu.updateUndoStack(canvas);
    }

    /**
     * Erases strokes and updates undo stack for undo.
     */
    public void erase() {     // if the eraser checkbox gets checked
        canvas.setOnMousePressed(e -> {
            canvas.getApp().beginPath();
            canvas.getApp().moveTo(e.getX(), e.getY());
            canvas.getApp().clearRect(e.getX(), e.getY(), Double.parseDouble(brushSize.getText()), Double.parseDouble(brushSize.getText()));
            EditMenu.updateUndoStack(canvas);
        });
        canvas.setOnMouseDragged(e -> {
            canvas.getApp().lineTo(e.getX(), e.getY());
            canvas.getApp().clearRect(e.getX(), e.getY(), Double.parseDouble(brushSize.getText()), Double.parseDouble(brushSize.getText()));
            EditMenu.updateUndoStack(canvas);
        });
        canvas.setOnMouseReleased(e -> {
            canvas.getApp().lineTo(e.getX(), e.getY());
            canvas.getApp().closePath();
            EditMenu.updateUndoStack(canvas);
        });
        if(!eraser.isSelected()) {pencil();}    // goes to default tool
    }

    /**
     * Displays color from a pixel on mouse click.
     */
    public void onColorGrab() {      // gets a color of a pixel with mouse click
        if(colorGrabber.isSelected()) {
            colorGrabber.setSelected(true);
            canvas.setOnMouseClicked(e-> {
                WritableImage writ = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
                canvas.snapshot(null, writ);     // takes screenshot of canvas
                PixelReader p = writ.getPixelReader();
                Color pixel = p.getColor((int)e.getX(), (int)e.getY());     // gets the color from the screenshot
                pixelColor.setText(pixel.toString().replace("0x", "#"));    // displays on the label as a hex value
            });
        }
        else {
            colorGrabber.setSelected(false);
            canvas.setOnMouseClicked(e->{pixelColor.setText("No Pixel Selected");});     // revert to no pixel
        }
    }

    /**
     * Resizes canvas to user's dimensions input.
     */
    public void onResizeCanvas() {
        cWidth = Integer.parseInt(width.getText());
        cHeight = Integer.parseInt(height.getText());
        canvas.setDimensions(cWidth,cHeight);
        EditMenu.updateUndoStack(canvas);
    }

    /**
     * Sets the stage for this class.
     * @param stage stage getter
     */
    public void setStage(Stage stage) {this.stage = stage;}
    public Scene getScene() {return this.stage.getScene();}


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        canvas.getApp();
        pencil();   // loads with default tool

        paintController.tabPane = new TabPane();
    }

    /**
     * Gets the current tab from mouse click.
     * @return the tab
     */
    public static CanvasTab getCurrentTab(){
        return (CanvasTab)tabPane.getSelectionModel().getSelectedItem();
    }

    /**
     * Deletes the tab from mouse click.
     */
    public static void removeCurrentTab() {
        paintController.tabPane.getTabs().remove(paintController.getCurrentTab());
    }
}
