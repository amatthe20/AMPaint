package ampaint;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javax.imageio.ImageIO;

public class paintController implements Initializable {
    
    /* variables for the layout */
    @FXML
    private Canvas canvas;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private CheckBox eraser;

    @FXML
    private TextField brushSize;

    @FXML
    private File file;

    GraphicsContext app;      // for activating the canvas


    /* methods for when user clicks on the menuitem */
    public void onSave() throws IOException {        // Save
        Image image = canvas.snapshot(null, null);
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", new File("newPaint.png"));
    }

    public void onOpenFile() {   // Open Image File
        app = canvas.getGraphicsContext2D();

        FileChooser fileChooser = new FileChooser();     
        fileChooser.setTitle("Open Image");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));

        File fileObj = fileChooser.showOpenDialog(null);     // file dialog opener
        Image image = new Image(fileObj.toURI().toString());
        if (fileObj != null) {
            try {
                InputStream i = new FileInputStream(fileObj);     // take the selected image 
                ImageView imageView = new ImageView();            // save to the canvas and then clear it
                imageView.setImage(image);
                app.drawImage(image, 0, 0);
            } 
            catch (IOException ex) {
                System.out.println("Error!");
            }
        }  
    }

    public void onSaveAs() {       // Save As
        FileChooser savedfile = new FileChooser();
            savedfile.setTitle("Save Image");
            
            File saved = savedfile.showSaveDialog(null);       // shows the file dialog but in save mode
            if (saved != null) {
                try {
                    WritableImage writImage = new WritableImage(900, 900);     // saves image in the width and height then
                    canvas.snapshot(null, writImage);
                    RenderedImage rendImage = SwingFXUtils.fromFXImage(writImage, null);
                    ImageIO.write(rendImage, "png", saved);
                } 
                catch (IOException ex) {
                    System.out.println("Failed to show");
                }
            }
    }

    public void onClose() {     // Close
        Platform.exit();        // closes the app entirely
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
    app = canvas.getGraphicsContext2D();

        canvas.setOnMouseDragged(e -> {              // When the mouse moves 
            double size = Double.parseDouble(brushSize.getText());
            double x = e.getX() - size / 2;
            double y = e.getY() - size / 2;

            if(eraser.isSelected()) {          // if user clicks on the eraser checkbox
                app.clearRect(x, y, size, size);
            }
            else {
                app.setFill(colorPicker.getValue());   // the chosen color
                app.fillRect(x, y, size, size);        // draws
            }
        });

        canvas.setOnMousePressed(e -> {            // When the mouse is clicked
            double size = Double.parseDouble(brushSize.getText());
            double x = e.getX() - size / 2;
            double y = e.getY() - size / 2;

            if(eraser.isSelected()) {          // if user clicks on the eraser checkbox
                app.clearRect(x, y, size, size);
            }
            else {
                app.setFill(colorPicker.getValue());   // the chosen color
                app.fillRect(x, y, size, size);        // draws
            }
        });
    } 
    
}
