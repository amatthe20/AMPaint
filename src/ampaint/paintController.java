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
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
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


    protected double size;

    private static double x1, y1, x2, y2;

    private boolean isToggled = false;
    
    private File saved;

    //GraphicsContext app;      // for activating the canvas

    
    public void onOpenFile() {   // Open
        canvas.getApp();
    
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files (*png,*jpg,*jpeg,*bmp)", "*.png", "*.jpg", "*.jpeg", "*.bmp"));

        File fileObj = fileChooser.showOpenDialog(null);     // file dialog opener
        Image image = new Image(fileObj.toURI().toString());
        canvas.drawImage(fileObj);   // canvas file
        canvas.drawImage(image);    // new canvas
        //canvas.setWidth(image.getWidth());            // resizes canvas to fit image
        //canvas.setHeight(image.getHeight());
        //canvas.getApp().drawImage(image, 0, 0);                  // new canvas
    }
    
    public void onSave() throws IOException {        // Save
        Image image = canvas.snapshot(null, null);
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", new File("newPaint.png"));   // saves in application
    }

    public void onSaveAs() {       // Save As
        FileChooser savedfile = new FileChooser();
        savedfile.setTitle("Save Image");

        saved = savedfile.showSaveDialog(null);       // shows the file dialog but in save mode
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

    public void aSquare() {     // draws a filled square
        size = Double.parseDouble(brushSize.getText());
        
        canvas.setOnMousePressed(e-> {
            if(eraser.isSelected()) erase(e);
            x1 = e.getX();
            y1 = e.getY();
        });

        canvas.setOnMouseDragged(e-> {
            if(eraser.isSelected()) erase(e);
            x2 = e.getX();
            y2 = e.getY();
            canvas.setLineWidth(size);
            canvas.setLineColor(colorPicker.getValue());
            canvas.drawSquare(x1, y1, x2, y2);
        
        });

        canvas.setOnMouseReleased(e-> {
            if(eraser.isSelected()) erase(e);
            x2 = e.getX();
            y2 = e.getY();
            canvas.setLineWidth(size);
            canvas.setLineColor(colorPicker.getValue());
            canvas.drawSquare(x1, y1, x2, y2);
        });
    }

    public void aCircle() {                  // draws a filled circle
        size = Double.parseDouble(brushSize.getText());

        canvas.setOnMousePressed(e-> {
            if(eraser.isSelected()) erase(e);
            x1 = e.getX();
            y1 = e.getY();
        });

        canvas.setOnMouseDragged(e-> {
            if(eraser.isSelected()) erase(e);
            x2 = e.getX();
            y2 = e.getY();
            canvas.setLineWidth(size);
            canvas.setLineColor(colorPicker.getValue());
            canvas.drawCircle(x1, y1, x2, y2);
        });

        canvas.setOnMouseReleased(e-> {
            if(eraser.isSelected()) erase(e);
            x2 = e.getX();
            y2 = e.getY();
            canvas.setLineWidth(size);
            canvas.setLineColor(colorPicker.getValue());
            canvas.drawCircle(x1, y1, x2, y2);
        });
        
    }

    public void aRectangle() {         // draws a filled rectangle   
        size = Double.parseDouble(brushSize.getText());

        canvas.setOnMousePressed(e-> {
            if(eraser.isSelected()) erase(e);
            x1 = e.getX();
            y1 = e.getY();
        });

        canvas.setOnMouseDragged(e-> {
            if(eraser.isSelected()) erase(e);
            x2 = e.getX();
            y2 = e.getY();
            canvas.setLineWidth(size);
            canvas.setLineColor(colorPicker.getValue());
            canvas.drawRectangle(x1,y1,x2,y2);
        });

        canvas.setOnMouseReleased(e-> {
            if(eraser.isSelected()) erase(e);
            x2 = e.getX();
            y2 = e.getY();
            canvas.setLineWidth(size);
            canvas.setLineColor(colorPicker.getValue());
            canvas.drawRectangle(x1,y1,x2,y2);
        });

    }

    public void anEllipse() {          // draws a filled ellipse
        size = Double.parseDouble(brushSize.getText());

        canvas.setOnMousePressed(e-> {
           if(eraser.isSelected()) erase(e);
            x1 = e.getX();
            y1 = e.getY();
        });

        canvas.setOnMouseDragged(e-> {
            if(eraser.isSelected()) erase(e);
            x2 = e.getX();
            y2 = e.getY();
            canvas.setLineWidth(size);
            canvas.setLineColor(colorPicker.getValue());
            canvas.drawEllipse(x1,y1,x2,y2);
        });

        canvas.setOnMouseReleased(e-> {
            if(eraser.isSelected()) erase(e);
            x2 = e.getX();
            y2 = e.getY();
            canvas.setLineWidth(size);
            canvas.setLineColor(colorPicker.getValue());
            canvas.drawEllipse(x1,y1,x2,y2);
        });
    }




    public void line() {                    // draws a line
        size = Double.parseDouble(brushSize.getText());
        canvas.setOnMousePressed(e-> {
            if(eraser.isSelected()) erase(e);
            else{
                canvas.getApp().beginPath();
                canvas.setLineColor(colorPicker.getValue());
                canvas.setLineWidth(size);
                canvas.drawStraightLine(e.getX(), e.getY());
            }
        });

        canvas.setOnMouseDragged(e-> {
            if(eraser.isSelected()) erase(e);
            canvas.setLineWidth(size);
            canvas.drawStraightLine(e.getX(), e.getY());
            canvas.getApp().stroke();
        });

        canvas.setOnMouseReleased(e-> {
            if(eraser.isSelected()) erase(e);
            canvas.setLineColor(colorPicker.getValue());
            canvas.setLineWidth(size);
            canvas.drawStraightLine(e.getX(), e.getY());
            canvas.getApp().stroke();
            canvas.getApp().closePath();
        });
    }


    public void dashed() {      // draws a dashed line
        size = Double.parseDouble(brushSize.getText());
        canvas.setOnMousePressed(e -> {
            if(eraser.isSelected()) erase(e);  // checks if eraser is selected
            else {
                canvas.getApp().beginPath();
                canvas.getApp().setLineWidth(size);
                canvas.setLineColor(colorPicker.getValue());
                canvas.drawDashedLine(size, e.getX(), e.getY());
            }
        });

        canvas.setOnMouseDragged(e -> {
            if(eraser.isSelected()) erase(e);
            else {
                canvas.getApp().setLineWidth(size);
                canvas.setLineColor(colorPicker.getValue());
                canvas.drawDashedLine(size, e.getX(), e.getY());
                canvas.getApp().stroke();
            }
        });

        canvas.setOnMouseReleased(e -> {
            if(eraser.isSelected()) erase(e);
            else {
                canvas.setLineWidth(size);
                canvas.setLineColor(colorPicker.getValue());
                canvas.drawDashedLine(size, e.getX(), e.getY());
                canvas.getApp().stroke();
                canvas.getApp().closePath();
            }
        });
    }

    public void pencil() {            // default drawing tool
        size = Double.parseDouble(brushSize.getText());
        canvas.setOnMousePressed(e -> {            
            if(eraser.isSelected()) erase(e);     
            else {
                canvas.getApp().beginPath();
                canvas.getApp().setLineWidth(size);
                canvas.setLineColor(colorPicker.getValue());
                canvas.getApp().moveTo(e.getX(), e.getY());
                canvas.getApp().stroke();
            }
        });

        canvas.setOnMouseDragged(e -> {               
            if(eraser.isSelected()) erase(e);
            else {
                canvas.getApp().lineTo(e.getX(), e.getY());
                canvas.getApp().setLineWidth(size);
                canvas.setLineColor(colorPicker.getValue());
                canvas.getApp().stroke();       // draws continous line (no unwanted spaces)
            }
        });

        canvas.setOnMouseReleased(e -> {                      
            if(eraser.isSelected()) erase(e);
            else {
                canvas.getApp().lineTo(e.getX(), e.getY());
                canvas.getApp().setLineWidth(size);
                canvas.getApp().stroke();
                canvas.getApp().closePath();
            }
        });
    }    


    /* buttons on the toolbar */
    /*public void addNewTab(String lbl, Image i) {
        CanvasTab t = new CanvasTab(lbl);
        paintController.tab.getTabs().add(t);
        paintController.tab.getSelectionModel().select(t);
        t.setImage(i);
    }

    public void addNewTab(File f) throws FileNotFoundException {
        CanvasTab t = new CanvasTab(f.getName());
        t.savedFile = f;
        paintController.tab.getTabs().add(t);
        paintController.tab.getSelectionModel().select(t);
        t.setImage(new Image(new FileInputStream(f)));
    }*/


    public void onClearCanvas() {
        canvas.clearCanvas();
    }

    public void erase(MouseEvent e) {     // if the eraser checkbox gets checked
        canvas.getApp();
        size = Double.parseDouble(brushSize.getText());
        canvas.getApp().beginPath();
        canvas.getApp().clearRect(e.getX(), e.getY(), size, size);
        canvas.getApp().closePath();
    }

    public void onColorGrab() {      // gets a color of a pixel with mouse click
        pixelColor.setText("");
        if(colorGrabber.isSelected()) {
            isToggled = true;
            colorGrabber.setSelected(isToggled);
            this.canvas.setOnMouseClicked(e-> {
                WritableImage writ = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
                canvas.snapshot(null, writ);     // takes screenshot of canvas
                PixelReader p = writ.getPixelReader();
                Color pixel = p.getColor((int)e.getX(), (int)e.getY());     // gets the color from the screenshot
                pixelColor.setText(pixel.toString().replace("0x", "#"));    // displays on the label as a hex value
            });
        }
        else {
            isToggled = false;
            colorGrabber.setSelected(isToggled);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        canvas.getApp();

        paintController.tabPane = new TabPane();
        
        
    }

    public static CanvasTab getCurrentTab(){
        return (CanvasTab)tabPane.getSelectionModel().getSelectedItem();
    }
    
}
