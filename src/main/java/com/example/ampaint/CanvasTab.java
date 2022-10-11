package com.example.ampaint;

import javafx.event.Event;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;

import java.io.File;

public class CanvasTab extends Tab {
    private static FileChooser fileChooser;

    public File savedFile;
    public Image thisImage;
    public ScrollPane scroll;
    public StackPane stack;
    public String title;
    public boolean isNotChanged;
    public NewCanvas currentCanvas;
    public Pane pane;

    /** Tab constructor. */
    public CanvasTab() {         // a new tab with canvas
        this.isNotChanged = true;
        this.savedFile = null;
        this.title = "Untitled Canvas";
        this.currentCanvas = new NewCanvas();
        setup();
    }

    /** Tab constructor with file.
     * @param saved imported image file from computer
     */
    public CanvasTab(File saved) {         // a new tab with canvas
        this.isNotChanged = false;
        this.savedFile = saved;
        this.title = savedFile.getName();
        this.currentCanvas = new NewCanvas();
        setup();
    }

    private void setup() {
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files (*png,*jpg,*jpeg,*bmp)", "*.png", "*.jpg", "*.jpeg", "*.bmp"));

        this.pane = new Pane(currentCanvas);
        this.stack = new StackPane();
        this.stack.getChildren().addAll(pane);   //canvas -> canvasPane -> stackPane -> scroll
        this.scroll = new ScrollPane(this.stack);

        this.setContent(scroll);
        this.setText((isNotChanged ? "*" : "") + this.title);
        this.setOnCloseRequest((Event e) -> {
            e.consume();            // consumes the normal event call
            if(this.isNotChanged) {} // if there are unsaved changes, give a warning
            // paintController.onSaveAs();
            else
                paintController.removeCurrentTab();
        });
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
    public Boolean getChanges(){return isNotChanged;}
    public void setChanges(Boolean is){isNotChanged = is;}
    public NewCanvas getCanvas(){return currentCanvas;}
}
