package ampaint;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

public class paintController implements Initializable {

    @FXML private Label pixelColor;              // used for displaying a pixel's color (in hex)

    @FXML protected NewCanvas canvas;

    @FXML protected ColorPicker colorPicker;

    @FXML protected CheckBox eraser;

    @FXML protected TextField brushSize;

    @FXML public static TabPane tabPane;

    @FXML public static Tab tab;

    @FXML protected Button newTabButton;

    @FXML protected ToggleButton colorGrabber;

    @FXML private ToggleButton mode;            // used for toggling between dark and light mode

    @FXML private MenuItem undo;

    @FXML private MenuItem redo;

    @FXML private MenuItem copy;

    @FXML private MenuItem paste;

    private double size;                        // used for converting textfield input into a double
    private static double x1, y1, x2, y2;
    private File saved;
    public Stage stage;

    public void onOpenFile() {   // Open
        canvas.getApp();
    
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files (*png,*jpg,*jpeg,*bmp)", "*.png", "*.jpg", "*.jpeg", "*.bmp"));

        File fileObj = fileChooser.showOpenDialog(stage);     // file dialog opener
        Image image = new Image(fileObj.toURI().toString());
        canvas.drawImage(fileObj);   // canvas file
        canvas.drawImage(image);    // new canvas
        editMenu.updateUndoStack(canvas);
    }
    
    public void onSave() throws IOException {        // Save
        Image image = canvas.snapshot(null, null);
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", new File("newPaint.png"));   // saves in application
    }

    public void onSaveAs() {       // Save As
        FileChooser savedfile = new FileChooser();
        savedfile.setTitle("Save Image");

        saved = savedfile.showSaveDialog(stage);       // shows the file dialog but in save mode
        if (saved != null) {
            try {
                WritableImage writImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getWidth());     // saves image in the width and height then
                canvas.snapshot(null, writImage);
                RenderedImage rendImage = SwingFXUtils.fromFXImage(writImage, null);
                ImageIO.write(rendImage, "png", saved);
            } 
            catch (IOException ex) {
                System.out.println("Failed to save");
            }
        }
    }

    public void onClose() {     // Close
        if(saved == null) {
            Alert close = new Alert(Alert.AlertType.WARNING);    // Popup regardless if user saved or not but might change to when the file isn't saved
            close.setTitle("Warning");
            close.setHeaderText("Hold on!");
            close.setContentText("Want to save your files before you leave?");  

            ButtonType save = new ButtonType("Yes");
            ButtonType dontSave = new ButtonType("No");
            ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

            close.getButtonTypes().setAll(save, dontSave, cancel);    // 3 options

            Optional<ButtonType> result = close.showAndWait();
            if (result.get() == save) {
                onSaveAs(); Platform.exit();     // opens the save dialog and 
            }
            else if (result.get() == dontSave) {Platform.exit(); System.exit(0);}
        }
        else {Platform.exit(); System.exit(0);}

    }
    

    /* methods for the help menu items */

    public void onAbout() {
        helpMenu about = new helpMenu();      // references help menu
        about.about();                        // activates about window
    }

    public void onHelp() {
        helpMenu help = new helpMenu();       
        help.help();                         // activates help window
    }


    /* methods for the shapes menu */

    public void aSquare() {     // draws a square
        size = Double.parseDouble(brushSize.getText());
        canvas.setOnMousePressed(e-> {
            x1 = e.getX();
            y1 = e.getY();
        });

        canvas.setOnMouseDragged(e-> {
            undo.setOnAction(ee->editMenu.undo(canvas));
            x2 = e.getX();
            y2 = e.getY();
            editMenu.updateUndoStack(canvas);
        });

        canvas.setOnMouseReleased(e-> {
            undo.setOnAction(ee-> editMenu.undo(canvas));
            x2 = e.getX();
            y2 = e.getY();
            canvas.setLineWidth(size);
            canvas.setLineColor(colorPicker.getValue());
            canvas.drawSquare(x1, y1, x2, y2);
            editMenu.updateUndoStack(canvas);
        });
    }

    public void aCircle() {                  // draws a filled circle
        size = Double.parseDouble(brushSize.getText());
        canvas.setOnMousePressed(e-> {
            x1 = e.getX();
            y1 = e.getY();
        });

        canvas.setOnMouseDragged(e-> {
            undo.setOnAction(ee->editMenu.undo(canvas));
            x2 = e.getX();
            y2 = e.getY();
            editMenu.updateUndoStack(canvas);
        });

        canvas.setOnMouseReleased(e-> {
            undo.setOnAction(ee->editMenu.undo(canvas));
            x2 = e.getX();
            y2 = e.getY();
            canvas.setLineWidth(size);
            canvas.setLineColor(colorPicker.getValue());
            canvas.drawCircle(x1, y1, x2, y2);
            editMenu.updateUndoStack(canvas);
        });
        
    }

    public void aRectangle() {         // draws a filled rectangle   
        size = Double.parseDouble(brushSize.getText());
        canvas.setOnMousePressed(e-> {
            x1 = e.getX();
            y1 = e.getY();
        });

        canvas.setOnMouseDragged(e-> {
            undo.setOnAction(ee->editMenu.undo(canvas));
            x2 = e.getX();
            y2 = e.getY();
            editMenu.updateUndoStack(canvas);
        });

        canvas.setOnMouseReleased(e-> {
            undo.setOnAction(ee->editMenu.undo(canvas));
            x2 = e.getX();
            y2 = e.getY();
            canvas.setLineWidth(size);
            canvas.setLineColor(colorPicker.getValue());
            canvas.drawRectangle(x1,y1,x2,y2);
            editMenu.updateUndoStack(canvas);
        });

    }

    public void anEllipse() {          // draws a filled ellipse
        size = Double.parseDouble(brushSize.getText());
        canvas.setOnMousePressed(e-> {
            x1 = e.getX();
            y1 = e.getY();
        });

        canvas.setOnMouseDragged(e-> {
            undo.setOnAction(ee->editMenu.undo(canvas));
            x2 = e.getX();
            y2 = e.getY();
            editMenu.updateUndoStack(canvas);
        });

        canvas.setOnMouseReleased(e-> {
            undo.setOnAction(ee->editMenu.undo(canvas));
            x2 = e.getX();
            y2 = e.getY();
            canvas.setLineWidth(size);
            canvas.setLineColor(colorPicker.getValue());
            canvas.drawEllipse(x1,y1,x2,y2);
            editMenu.updateUndoStack(canvas);
        });
    }


    public void aTriangle() {          // draws a triangle pointing upwards
        size = Double.parseDouble(brushSize.getText());
        canvas.setOnMousePressed(e-> {
            x1 = e.getX();
            y1 = e.getY();
        });

        canvas.setOnMouseDragged(e-> {
            undo.setOnAction(ee->editMenu.undo(canvas));
            x2 = e.getX();
            y2 = e.getY();
            editMenu.updateUndoStack(canvas);
        });

        canvas.setOnMouseReleased(e-> {
            undo.setOnAction(ee->editMenu.undo(canvas));
            x2 = e.getX();
            y2 = e.getY();
            canvas.setLineWidth(size);
            canvas.setLineColor(colorPicker.getValue());
            canvas.drawTriangle(x1,y1,x2,y2);
            editMenu.updateUndoStack(canvas);
        });
    }

    
    public void aShape() {          // draws a shape using specifed number of sides and the angle it draws on
        size = Double.parseDouble(brushSize.getText());
        canvas.drawShapeAngle();
        canvas.drawShapeSides();
        canvas.setOnMousePressed(e-> {
            x1 = e.getX();
            y1 = e.getY();
        });

        canvas.setOnMouseDragged(e-> {
            undo.setOnAction(ee->editMenu.undo(canvas));
            x2 = e.getX();
            y2 = e.getY();
            editMenu.updateUndoStack(canvas);
        });

        canvas.setOnMouseReleased(e-> {
            undo.setOnAction(ee->editMenu.undo(canvas));
            x2 = e.getX();
            y2 = e.getY();
            canvas.setLineWidth(size);
            canvas.setLineColor(colorPicker.getValue());
            canvas.drawShape(canvas.getDrawShapeAngle(),canvas.getDrawShapeSides(),x1,y1,x2,y2);
            editMenu.updateUndoStack(canvas);
        });
    }
    
    

    public void line() {        // draws a straight line
        size = Double.parseDouble(brushSize.getText());
        canvas.setOnMousePressed(e-> {
            undo.setOnAction(ee->editMenu.undo(canvas));
            canvas.getApp().beginPath();
            canvas.setLineColor(colorPicker.getValue());
            canvas.setLineWidth(size);
            canvas.getApp().lineTo(e.getX(), e.getY());
            canvas.getApp().stroke();
            editMenu.updateUndoStack(canvas);
        });

        canvas.setOnMouseDragged(e-> {
            undo.setOnAction(ee->editMenu.undo(canvas));
            canvas.setLineWidth(size);
            canvas.getApp().lineTo(e.getX(), e.getY());
            canvas.getApp().stroke();
            editMenu.updateUndoStack(canvas);
        });

        canvas.setOnMouseReleased(e-> {
            undo.setOnAction(ee->editMenu.undo(canvas));
            canvas.setLineColor(colorPicker.getValue());
            canvas.setLineWidth(size);
            canvas.getApp().lineTo(e.getX(), e.getY());
            canvas.getApp().stroke();
            canvas.getApp().closePath();
            editMenu.updateUndoStack(canvas);
        });
    }

    public void dashed() {      // draws a dashed line
        size = Double.parseDouble(brushSize.getText());
        canvas.setOnMousePressed(e -> {
            undo.setOnAction(ee->editMenu.undo(canvas));
            canvas.getApp().beginPath();
            canvas.getApp().setLineWidth(size);
            canvas.setLineColor(colorPicker.getValue());
            canvas.drawDashedLine(size, e.getX(), e.getY());
            canvas.getApp().stroke();
            editMenu.updateUndoStack(canvas);
        });

        canvas.setOnMouseDragged(e -> {
            undo.setOnAction(ee->editMenu.undo(canvas));
            canvas.getApp().setLineWidth(size);
            canvas.setLineColor(colorPicker.getValue());
            canvas.drawDashedLine(size, e.getX(), e.getY());
            canvas.getApp().stroke();
            editMenu.updateUndoStack(canvas);
        });

        canvas.setOnMouseReleased(e -> {
            undo.setOnAction(ee->editMenu.undo(canvas));
            canvas.setLineWidth(size);
            canvas.setLineColor(colorPicker.getValue());
            canvas.drawDashedLine(size, e.getX(), e.getY());
            canvas.getApp().stroke();
            canvas.getApp().closePath();
            editMenu.updateUndoStack(canvas);
        });
    }

    public void pencil() {            // default drawing tool
        size = Double.parseDouble(brushSize.getText());
        canvas.setOnMousePressed(e -> {
            undo.setOnAction(ee->editMenu.undo(canvas));    
            canvas.getApp().beginPath();
            canvas.setLineWidth(size);
            canvas.setLineColor(colorPicker.getValue());
            canvas.getApp().moveTo(e.getX(), e.getY());
            canvas.getApp().stroke();
            editMenu.updateUndoStack(canvas);
        });

        canvas.setOnMouseDragged(e -> {
            undo.setOnAction(ee->editMenu.undo(canvas));    
            canvas.getApp().lineTo(e.getX(), e.getY());
            canvas.setLineWidth(size);
            canvas.setLineColor(colorPicker.getValue());
            canvas.getApp().stroke();       // draws continous line (no unwanted spaces)
            editMenu.updateUndoStack(canvas);
        });

        canvas.setOnMouseReleased(e -> {   
            undo.setOnAction(ee->editMenu.undo(canvas));    
            canvas.getApp().lineTo(e.getX(), e.getY());
            canvas.setLineWidth(size);
            canvas.getApp().stroke();
            canvas.getApp().closePath();
            editMenu.updateUndoStack(canvas);
        });
    }    


    /* buttons on the toolbar */
    public void addNewTab() {
        tabPane.getTabs().add(new CanvasTab());
    }


    public void onClearCanvas() {
        undo.setOnAction(ee->editMenu.undo(canvas));
        canvas.clearCanvas();
        editMenu.updateUndoStack(canvas);
    }

    public void erase() {     // if the eraser checkbox gets checked
        size = Double.parseDouble(brushSize.getText());
        canvas.setOnMousePressed(e -> {
            undo.setOnAction(ee->editMenu.undo(canvas));
            canvas.getApp().beginPath();
            canvas.getApp().moveTo(e.getX(), e.getY());
            canvas.getApp().clearRect(e.getX(), e.getY(), size, size);
            editMenu.updateUndoStack(canvas);
        });
        canvas.setOnMouseDragged(e -> {
            undo.setOnAction(ee->editMenu.undo(canvas));
            canvas.getApp().lineTo(e.getX(), e.getY());
            canvas.getApp().clearRect(e.getX(), e.getY(), size, size);
            editMenu.updateUndoStack(canvas);
        });
        canvas.setOnMouseReleased(e -> {
            undo.setOnAction(ee->editMenu.undo(canvas));
            canvas.getApp().lineTo(e.getX(), e.getY());
            canvas.getApp().closePath();
            editMenu.updateUndoStack(canvas);
        });

        if(!eraser.isSelected()) {pencil();}    // goes to default tool
    }

    public void onColorGrab() {      // gets a color of a pixel with mouse click
        pixelColor.setText("No Pixel Selected");
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
            canvas.setOnMouseClicked(e-> {pixelColor.setText("No Pixel Selected");});
        }
    }

    public void toggleDarkMode() {    // for toggling between dark and light mode
        if(mode.isSelected()) {
            mode.setSelected(true);
            stage = (Stage) mode.getScene().getWindow();    // gets scene from AMPaint class
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

    public void setStage(Stage stage) {this.stage = stage;}

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        canvas.getApp();

        paintController.tabPane = new TabPane();
        
        //stage.setOnCloseRequest((Window w)-> {
            //onClose();
        //});
    }

    public static CanvasTab getCurrentTab(){
        return (CanvasTab)tabPane.getSelectionModel().getSelectedItem();
    }

    public static void removeCurrentTab() {
        paintController.tabPane.getTabs().remove(paintController.getCurrentTab());
    }
    
}
