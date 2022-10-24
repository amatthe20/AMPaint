package com.example.ampaint;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
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
public class PaintController implements Initializable {

    @FXML private Label pixelColor;              // used for displaying a pixel's color (in hex)
    @FXML private Label canvasCd;
    @FXML private ScrollPane scroll;
    @FXML protected StackPane stack;
    @FXML private NewCanvas canvas;
    @FXML protected ColorPicker colorPicker;
    @FXML protected CheckBox eraser;
    @FXML protected TextField brushSize;         // used for getting the line thickness
    @FXML private TextField width;               // used for getting the canvas width
    @FXML private TextField height;              // used for getting the canvas height
    @FXML private static TabPane tabPane;
    @FXML private static CanvasTab tab;
    @FXML private Button newTabButton;         // used for generating new canvases
    @FXML private Button resize;
    @FXML protected ToggleButton colorGrabber;   // used for getting the color of a pixel
    @FXML private RadioMenuItem mode;            // used for toggling between dark and light mode
    @FXML private MenuItem open;
    @FXML private MenuItem save;
    @FXML private MenuItem saveAs;
    @FXML private MenuItem close;
    @FXML private MenuItem pencil;
    @FXML private MenuItem straight;
    @FXML private MenuItem dashed;
    @FXML private MenuItem undo;
    @FXML private MenuItem redo;
    @FXML private MenuItem copy;
    @FXML private MenuItem cut;
    @FXML private MenuItem paste;
    @FXML private RadioMenuItem square;
    @FXML private RadioMenuItem rectangle;
    @FXML private RadioMenuItem circle;
    @FXML private RadioMenuItem ellipse;
    @FXML private RadioMenuItem triangle;
    @FXML private RadioMenuItem shape;

    private static double x1, y1, x2, y2;       // used for getting the x and y coordinates from the mouse
    private File saved;
    private File file;
    private double zoomStartVal = 5;
    private double zoomScale = zoomStartVal;
    public static Stage stage;                         // used for getting the stage from the parent class to the controller

    /**
     * Opens a file from a directory on user's computer. Files supported are png, jpeg, jpg, and bmp.
     */
    public void onOpenFile() {   // Open
        FileChooser openedFile = new FileChooser();
        openedFile.setTitle("Open Image");
        openedFile.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files (*png,*jpg,*jpeg,*bmp)", "*.png", "*.jpg", "*.jpeg", "*.bmp"),
                new FileChooser.ExtensionFilter("All Files","*.*"));

        file = openedFile.showOpenDialog(stage);     // shows the file dialog
        Image image = new Image(file.toURI().toString());
        canvas.drawImage(file);   // canvas file
        canvas.drawImage(image);    // new canvas
        EditMenu.updateUndoStack(canvas);
        LoggerHandle.getLoggerHandle().writeToLog(true,"Opened " + file.getName());
    }

    /**
     * Saves the canvas to the application.
     * @throws IOException to make sure the canvas is a screenshot and saved properly
     */
    public void onSave() throws IOException {        // Save
        String fileName = saved.getAbsolutePath();
        int dot = saved.getName().lastIndexOf(".");
        String fileExtension = saved.getName().substring(dot + 1);      // gets the file extension
        Image image = stack.snapshot(null, null);   // if there is a rotation of the canvas, this will capture it
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), fileExtension, new File(fileName));   // saves to file path
        LoggerHandle.getLoggerHandle().writeToLog(true,"Save");
    }

    /**
     * Saves the canvas to a directory on user's desktop or pc. It can save as any image file supported.
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
                stack.snapshot(null, writImage);
                RenderedImage rendImage = SwingFXUtils.fromFXImage(writImage, null);
                ImageIO.write(rendImage, "png", saved);
                ImageIO.write(rendImage, "jpeg", saved);
                ImageIO.write(rendImage, "jpg", saved);
                ImageIO.write(rendImage, "bmp", saved);
                LoggerHandle.getLoggerHandle().writeToLog(true,"SaveAs");
            }
            catch (IOException ex) {
                System.out.println("Failed to save");
            }
        }
        LoggerHandle.getLoggerHandle().writeToLog(true,"SaveAs");
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
            else if (result.get() == dontSave) {System.exit(0);}
        }
        else {Platform.exit(); System.exit(0);}
    }

    public StackPane getStackPane() {return stack;}

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
    public void onAbout() {HelpMenu.about();}    // activates about window

    /**
     * Calls help() from HelpMenu.java to be displayed
     * @see HelpMenu#help()
     */
    public void onHelp() {HelpMenu.help();}      // activates help window

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
            canvasCd.setText(e.getX() + ", " + e.getY() + "px");
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
        LoggerHandle.getLoggerHandle().writeToLog(true,"Square selected");
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
            canvasCd.setText(e.getX() + ", " + e.getY() + "px");
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
        LoggerHandle.getLoggerHandle().writeToLog(true,"Circle selected");
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
            canvasCd.setText(e.getX() + ", " + e.getY() + "px");
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
        LoggerHandle.getLoggerHandle().writeToLog(true,"Rectangle selected");
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
            canvasCd.setText(e.getX() + ", " + e.getY() + "px");
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
        LoggerHandle.getLoggerHandle().writeToLog(true,"Ellipse selected");
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
            canvasCd.setText(e.getX() + ", " + e.getY() + "px");
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
        LoggerHandle.getLoggerHandle().writeToLog(true,"Triangle selected");
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
            canvasCd.setText(e.getX() + ", " + e.getY() + "px");
            EditMenu.updateUndoStack(canvas);
        });

        canvas.setOnMouseReleased(e-> {
            x2 = e.getX();
            y2 = e.getY();
            canvas.setLineWidth(Double.parseDouble(brushSize.getText()));
            canvas.setLineColor(colorPicker.getValue());
            canvas.drawShape(canvas.getShapeAngle(), canvas.getShapeSides(),x1,y1,x2,y2);
            EditMenu.updateUndoStack(canvas);
        });
        LoggerHandle.getLoggerHandle().writeToLog(true,"New Shape selected");
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
            EditMenu.updateUndoStack(canvas);
        });

        canvas.setOnMouseDragged(e-> {
            canvas.getApp().lineTo(e.getX(), e.getY());
            canvas.getApp().stroke();
            canvasCd.setText(e.getX() + ", " + e.getY() + "px");
            EditMenu.updateUndoStack(canvas);
        });

        canvas.setOnMouseReleased(e-> {
            canvas.getApp().lineTo(e.getX(), e.getY());
            canvas.getApp().stroke();
            canvas.getApp().closePath();
            EditMenu.updateUndoStack(canvas);
        });
        LoggerHandle.getLoggerHandle().writeToLog(true,"Straight Line tool activated");
    }

    /**
     * Draws a dashed line from mouse events (pressed, dragged, released) and updates undo stack for undo.
     * Updates color and size from NewCanvas.java methods.
     */
    public void dashed() {      // draws a dashed line
        canvas.setOnMousePressed(e -> {
            canvas.getApp().beginPath();
            canvas.setLineWidth(Double.parseDouble(brushSize.getText()));      // set width of the stroke
            canvas.setLineColor(colorPicker.getValue());                       // set color of the stroke
            canvas.drawDashedLine(Double.parseDouble(brushSize.getText()), e.getX(), e.getY());
            EditMenu.updateUndoStack(canvas);
        });

        canvas.setOnMouseDragged(e -> {
            canvas.drawDashedLine(Double.parseDouble(brushSize.getText()), e.getX(), e.getY());
            canvas.getApp().stroke();
            EditMenu.updateUndoStack(canvas);
            canvasCd.setText(e.getX() + ", " + e.getY() + "px");
        });

        canvas.setOnMouseReleased(e -> {
            canvas.drawDashedLine(Double.parseDouble(brushSize.getText()), e.getX(), e.getY());
            canvas.getApp().stroke();
            canvas.getApp().closePath();
            EditMenu.updateUndoStack(canvas);
            canvasCd.setText(e.getX() + ", " + e.getY() + "px");
        });
        LoggerHandle.getLoggerHandle().writeToLog(true,"Dashed Line tool activated");
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
            canvasCd.setText(e.getX() + ", " + e.getY() + "px");
            canvas.getApp().lineTo(e.getX(), e.getY());
            canvas.getApp().stroke();       // draws continuous line (no unwanted spaces)
            EditMenu.updateUndoStack(canvas);
        });

        canvas.setOnMouseReleased(e-> {
            canvas.getApp().lineTo(e.getX(), e.getY());
            canvas.getApp().stroke();
            canvas.getApp().closePath();
            EditMenu.updateUndoStack(canvas);
        });
        LoggerHandle.getLoggerHandle().writeToLog(true,"Pencil tool activated");
    }

    /* buttons on the toolbar */

    /**
     * Calls clearCanvas() from NewCanvas.java to be executed.
     * @see NewCanvas#clearCanvas()
     */
    public void onClearCanvas() {
        canvas.clearCanvas();
        EditMenu.updateUndoStack(canvas);
        LoggerHandle.getLoggerHandle().writeToLog(true,"Canvas cleared");
    }

    /**
     * Erases strokes and updates undo stack for undo.
     */
    public void erase() {     // if the eraser checkbox gets checked
        canvas.setOnMousePressed(e-> {
            canvas.getApp().beginPath();
            canvas.setLineWidth(Double.parseDouble(brushSize.getText()));
            canvas.setLineColor(Color.WHITE);
            canvas.getApp().moveTo(e.getX(), e.getY());
            canvas.getApp().stroke();
            EditMenu.updateUndoStack(canvas);
        });

        canvas.setOnMouseDragged(e-> {
            canvasCd.setText(e.getX() + ", " + e.getY() + "px");
            canvas.getApp().lineTo(e.getX(), e.getY());
            canvas.getApp().stroke();       // draws continuous line (no unwanted spaces)
            EditMenu.updateUndoStack(canvas);
        });

        canvas.setOnMouseReleased(e-> {
            canvasCd.setText(e.getX() + ", " + e.getY() + "px");
            canvas.getApp().lineTo(e.getX(), e.getY());
            canvas.getApp().stroke();
            canvas.getApp().closePath();
            EditMenu.updateUndoStack(canvas);
        });
        LoggerHandle.getLoggerHandle().writeToLog(true,"Eraser checked");

        if(!eraser.isSelected()) {
            pencil();      // goes to default tool
            LoggerHandle.getLoggerHandle().writeToLog(true,"Eraser unchecked");
        }
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
                LoggerHandle.getLoggerHandle().writeToLog(true, "Color Grabber toggled on");
            });
        }
        else {
            colorGrabber.setSelected(false);
            canvas.setOnMouseClicked(e-> pixelColor.setText("No Pixel Selected"));     // revert to no pixel
            LoggerHandle.getLoggerHandle().writeToLog(true, "Color Grabber toggled off");
        }
    }

    /**
     * Resizes canvas to user's dimensions input.
     */
    public void onResizeCanvas() {
        int cWidth = Integer.parseInt(width.getText());
        int cHeight = Integer.parseInt(height.getText());
        canvas.setDimensions(cWidth,cHeight);
        EditMenu.updateUndoStack(canvas);
        LoggerHandle.getLoggerHandle().writeToLog(true, "Canvas resized to " + cWidth + " x " + cHeight);
    }

    /**
     * Rotates canvas right 90 degrees.
     */
    public void onRotateRight90() {
        stack.setRotate(stack.getRotate() + 90);
        EditMenu.updateUndoStack(canvas);
        LoggerHandle.getLoggerHandle().writeToLog(true, "Canvas rotated right 90 degrees");
    }

    /**
     * Rotates canvas left 90 degrees.
     */
    public void onRotateLeft90() {
        stack.setRotate(stack.getRotate() - 90);
        EditMenu.updateUndoStack(canvas);
        LoggerHandle.getLoggerHandle().writeToLog(true, "Canvas rotated left 90 degrees");
    }

    /**
     * Rotates canvas right 180 degrees.
     */
    public void onRotateRight180() {
        stack.setRotate(stack.getRotate() + 180);
        EditMenu.updateUndoStack(canvas);
        LoggerHandle.getLoggerHandle().writeToLog(true, "Canvas rotated 180 degrees");
    }

    /**
     * Rotates canvas left 180 degrees.
     */
    public void onRotateLeft180() {
        stack.setRotate(stack.getRotate() - 180);
        EditMenu.updateUndoStack(canvas);
        LoggerHandle.getLoggerHandle().writeToLog(true, "Canvas rotated 180 degrees");
    }

    /**
     * Rotates canvas right 270 degrees.
     */
    public void onRotateRight270() {
        stack.setRotate(stack.getRotate() + 270);
        EditMenu.updateUndoStack(canvas);
        LoggerHandle.getLoggerHandle().writeToLog(true, "Canvas rotated 270 degrees");
    }

    /**
     * Rotates canvas left 270 degrees.
     */
    public void onRotateLeft270() {
        stack.setRotate(stack.getRotate() - 270);
        EditMenu.updateUndoStack(canvas);
        LoggerHandle.getLoggerHandle().writeToLog(true, "Canvas rotated 270 degrees");
    }

    /**
     * Flips canvas horizontally.
     */
    public void flipHorizontal() {
        canvas.setScaleX(-canvas.getScaleX());
        EditMenu.updateUndoStack(canvas);
        LoggerHandle.getLoggerHandle().writeToLog(true, "Canvas flipped vertically");
    }

    /**
     * Flips canvas vertically.
     */
    public void flipVertical() {
        canvas.setScaleY(-canvas.getScaleY());
        EditMenu.updateUndoStack(canvas);
        LoggerHandle.getLoggerHandle().writeToLog(true, "Canvas flipped horizontally");
    }

    /**
     * Displays the mouse coordinates from the canvas.
     */
    public void coordinates() {
        canvas.setOnMouseMoved(e-> canvasCd.setText(e.getX() + ", " + e.getY() + "px"));
        canvas.setOnMouseExited(e-> canvasCd.setText("No Canvas Detected"));
    }

    public void zoomIn() {
        zoomScale++;
        zoomScale = Math.max(1, zoomScale);
        stack.setScaleX(zoomScale/zoomStartVal);
        stack.setScaleY(zoomScale/zoomStartVal);
        scroll.setHvalue(0.5);
        scroll.setVvalue(0.5);
    }

    public void zoomOut() {
        zoomScale--;
        zoomScale = Math.max(1, zoomScale);
        stack.setScaleX(zoomScale/zoomStartVal);
        stack.setScaleY(zoomScale/zoomStartVal);
        scroll.setHvalue(0.5);
        scroll.setVvalue(0.5);
    }

    public void setStage(Stage stage) {PaintController.stage = stage;}

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
        new ImageIcons(close, new Image(AMPaint.class.getResourceAsStream("/menu/close.png")));
        new ImageIcons(pencil, new Image(AMPaint.class.getResourceAsStream("/menu/pencil.png")));
        new ImageIcons(straight, new Image(AMPaint.class.getResourceAsStream("/menu/straight.png")));
        new ImageIcons(dashed, new Image(AMPaint.class.getResourceAsStream("/menu/dashed.png")));
        new ImageIcons(undo, new Image(AMPaint.class.getResourceAsStream("/menu/undo.jpg")));
        new ImageIcons(redo, new Image(AMPaint.class.getResourceAsStream("/menu/redo.png")));
        new ImageIcons(cut, new Image(AMPaint.class.getResourceAsStream("/menu/cut.png")));
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
        tabPane = new TabPane();
        getCurrentTab();
        tab = new CanvasTab();
    }

    public void addNewTab() {
        CanvasTab tab = new CanvasTab();
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
    }

    /**
     * Gets the current tab from mouse click.
     * @return the tab
     */
    public static CanvasTab getCurrentTab() {return (CanvasTab)tabPane.getSelectionModel().getSelectedItem();}

    /**
     * Deletes the tab from mouse click.
     */
    public static void removeCurrentTab() {tabPane.getTabs().remove(PaintController.getCurrentTab());}
}