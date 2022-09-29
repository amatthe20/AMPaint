package ampaint;

import java.io.File;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class CanvasTab extends Tab {

    public File savedFile;
    public Image thisImage;
    public ScrollPane scroll;
    public boolean isChanged;
    public NewCanvas currentCanvas = new NewCanvas();
    public Pane pane = new Pane();
    
    public CanvasTab(String label) {         // a new tab with canvas
        super();
        this.scroll = new ScrollPane();
        this.pane.getChildren().add(this.currentCanvas);
        this.scroll.setContent(this.pane);
        this.setContent(this.scroll);

    }

    public void setImage(Image image) {       // for creating the canvas
        try {
            this.thisImage = image;     // sets thisImage pointer to image
            this.currentCanvas.updateDimensions(); // update the canvas dimensions
            this.currentCanvas.app.drawImage(thisImage,0,0);
        } catch (Exception e) {
            System.out.println("Failed to setImage:" + e);
        }
    }



    public Pane getPane() {return pane;}
    public Boolean getChanges(){return isChanged;}
    public void setChanges(Boolean is){isChanged = is;}
    public NewCanvas getCanvas(){return currentCanvas;}


}
