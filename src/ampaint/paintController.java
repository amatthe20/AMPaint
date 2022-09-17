package ampaint;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

public class paintController implements Initializable {
    
    /* variables for the layout */
    @FXML private Canvas canvas;

    @FXML private ColorPicker colorPicker;

    @FXML private CheckBox eraser;

    @FXML private TextField brushSize;

    private double size;

    GraphicsContext app;      // for activating the canvas

/********************************************************************************/
    /* methods for the file menu items */
    public void onOpenFile() {   // Open
        app = canvas.getGraphicsContext2D();
    
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));

        File fileObj = fileChooser.showOpenDialog(null);     // file dialog opener
        Image image = new Image(fileObj.toURI().toString());
        canvas.setWidth(image.getWidth());            // resizes canvas to fit image
        canvas.setHeight(image.getHeight());
        app.drawImage(image, 0, 0);                  // new canvas
    }
    
    public void onSave() throws IOException {        // Save
        Image image = canvas.snapshot(null, null);
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", new File("newPaint.png"));
    }

    public void onSaveAs() {       // Save As
        FileChooser savedfile = new FileChooser();
            savedfile.setTitle("Save Image");
            
            File saved = savedfile.showSaveDialog(null);       // shows the file dialog but in save mode
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
        Platform.exit();        // closes the app entirely
    }

/********************************************************************************/

    /* methods for the tools */
    public void Line() {         // if the user wants to draw a straight line
        size = Double.parseDouble(brushSize.getText());
        canvas.setOnMousePressed(e -> {
            if(eraser.isSelected()) erase(e);  // checks if eraser is selected
            else {
                app.beginPath();
                app.lineTo(e.getX(), e.getY());    // goes to mouse coordinates
                app.setLineWidth(size);
            }
        });

        canvas.setOnMouseDragged(e -> {
            if(eraser.isSelected()) erase(e);
            else {
                app.setLineWidth(size);
                app.stroke();                     // draws
            }
        });

        canvas.setOnMouseReleased(e -> {
            if(eraser.isSelected()) erase(e);
            else {
                app.lineTo(e.getX(), e.getY());   // goes to mouse coordinates
                app.setLineWidth(size);
                app.stroke();
                app.closePath();
            }
        });

        colorPicker.setOnAction(e->app.setStroke(colorPicker.getValue()));
    }

    public void erase(MouseEvent e) {     // if the eraser checkbox gets checked
        app = canvas.getGraphicsContext2D();
        app.beginPath();
        app.clearRect(e.getX(), e.getY(), 30, 30);
        app.closePath();
    }


    public void FreeDraw() {            // default drawing tool
        canvas.setOnMousePressed(e -> {            // When the mouse is clicked
            size = Double.parseDouble(brushSize.getText());
            if(eraser.isSelected()) erase(e);     
            else {
                app.beginPath();
                app.setLineWidth(size);
                app.moveTo(e.getX(), e.getY());
                app.stroke();
            }
        });

        canvas.setOnMouseDragged(e -> {              // When the mouse moves 
            size = Double.parseDouble(brushSize.getText());
            if(eraser.isSelected()) erase(e);
            else {
                app.lineTo(e.getX(), e.getY());
                app.setLineWidth(size);
                app.stroke();       // draws
            }
        });

        canvas.setOnMouseReleased(e -> {                      // When the mouse is released
            size = Double.parseDouble(brushSize.getText());
            if(eraser.isSelected()) erase(e);
            else {
                app.lineTo(e.getX(), e.getY());
                app.setLineWidth(size);
                app.stroke();
                app.closePath();
            }
        });

        colorPicker.setOnAction(e->app.setStroke(colorPicker.getValue()));   // draws with the selected color
    }

/********************************************************************************/
    /* methods for the help menu items */

    public void onAbout() {             // About
        Stage dialog = new Stage();                        // opens new window
        dialog.initModality(Modality.NONE);
        dialog.initOwner(null);
        VBox about = new VBox();
        about.getChildren().add(new Text("Welcome to AMPaint!\n"      // user can learn about the app and see the version
        + "This is a Paint application where you can draw.\n\n\n"
        +  "Features:\n\n" + "Import Image as Canvas\n" + "Save Canvas in Application\n" 
        + "Save Canvas to Computer\n" + "Free Draw and Line Tools\n" + "ToolBar for Thickness, Color, Eraser, and Clearing Canvas\n"
        + "Scroll Bars on the Canvas\n\n" + "Stay tuned for more features!\n\n\n\n" + "Version 1.0.5"));     
        Scene aboutBox = new Scene(about, 400, 300);                  // size of the window
        dialog.setTitle("About");
        dialog.setScene(aboutBox);
        dialog.show();
    }



/********************************************************************************/

    /* buttons on the toolbar */
    public void onClearCanvas() {
        app.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());   // blank canvas
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        app = canvas.getGraphicsContext2D();
        FreeDraw();
    } 
    
}
