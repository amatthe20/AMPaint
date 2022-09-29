package ampaint;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author amatthe1
 */
public class helpMenu {

    public void about() {             // About
        Stage dialog = new Stage();                        // opens new window
        dialog.initModality(Modality.NONE);
        dialog.initOwner(null);
        VBox about = new VBox();
        about.getChildren().add(new Text("Welcome to AMPaint!\n" // user can learn about the app and see the version
                + "This is a Paint application where you can draw.\n\n\n"
                + "Features:\n\n" + "Import Image as Canvas\n" + "Save Canvas in Application\n"
                + "Save Canvas to Computer\n" + "Keyboard Shortcuts for the File Menu" 
                + "Tabs (open multiple canvases at once)" + "Pencil and Line Tools\n" 
                + "Square, Rectangle, Circle, and Ellipse Shapes\n" + "Smart Save"
                + "ToolBar for Tabs, Thickness, Color, Eraser, Clearing Canvas, and Color Grabber\n"
                + "Scroll Bars on the Canvas\n\n" + "Stay tuned for more features!\n\n\n\n" + "Version 1.1.0"));
        Scene aboutBox = new Scene(about, 400, 300);                  // size of the window
        dialog.setTitle("About");
        dialog.setScene(aboutBox);
        dialog.show();
    }

    public void help() {            // Help
        Stage dialog = new Stage();                        // opens new window
        dialog.initModality(Modality.NONE);
        dialog.initOwner(null);
        VBox help = new VBox();
        help.getChildren().add(new Text("How to Navigate AMPaint App\n\n" 
                + "File Menu:\n"
                + "Open - Import an image from your desktop as the canvas\n"
                + "Save - Save current canvas into the application\n"
                + "Save As - Save current canvas to your desktop as a png\n"
                + "Close - Exit application\n\n" 
                + "In Tools Menu:\n" + "Free Draw - Draw on canvas\n"
                + "Line - Draws a kind of line as specified (straight or dashed)\n\n"
                + "Shapes Menu:\n"
                + "Square - Draw a filled square from mouse drag\n"
                + "Rectangle - Draw a filled rectangle from mouse drag\n"
                + "Circle - Draw a filled circle from mouse drag\n"
                + "Ellipse - Draw a filled ellipse from mouse drag\n\n" 
                + "ToolBar:\n"
                + "New Canvas button for opening a new canvas in a different tab\n"
                + "Textfield for line thickness\n" + "Color Chooser for changing the color of the stroke\n"
                + "Eraser checkbox for activating the eraser\n" 
                + "Clear Canvas button to erase the image but user can still draw on the blank canvas\n"
                + "Color Grabber button to get the color on a pixel with a mouse click\n"));
        Scene aboutBox = new Scene(help, 400, 300);                  // size of the window
        dialog.setTitle("Help");
        dialog.setScene(aboutBox);
        dialog.show();
    }
}
