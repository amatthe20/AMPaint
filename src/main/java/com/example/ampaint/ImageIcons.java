package com.example.ampaint;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Contains methods for setting the graphics for various objects.
 */
public class ImageIcons {
    /**
     * Sets the graphic for a MenuItem.
     * @param item menu item
     * @param icon imported image
     */
    public ImageIcons(MenuItem item, Image icon) {
        ImageView image = new ImageView(icon);
        image.setFitWidth(15);
        image.setFitHeight(15);
        image.setPreserveRatio(true);
        item.setGraphic(image);
    }

    /**
     * Sets the graphic for a RadioMenuItem.
     * @param item radio menu item
     * @param icon imported image
     */
    public ImageIcons(RadioMenuItem item, Image icon) {
        ImageView image = new ImageView(icon);
        image.setFitWidth(15);
        image.setFitHeight(15);
        image.setPreserveRatio(true);
        item.setGraphic(image);
    }

    /**
     * Sets the graphic for a Button.
     * @param button button
     * @param icon imported image
     */
    public ImageIcons(Button button, Image icon) {
        ImageView image = new ImageView(icon);
        image.setFitWidth(15);
        image.setFitHeight(15);
        image.setPreserveRatio(true);
        button.setGraphic(image);
    }

    /**
     * Sets the graphic for a ToggleButton.
     * @param button toggle button
     * @param icon imported image
     */
    public ImageIcons(ToggleButton button, Image icon) {
        ImageView image = new ImageView(icon);
        image.setFitWidth(15);
        image.setFitHeight(15);
        image.setPreserveRatio(true);
        button.setGraphic(image);
    }

    /**
     * Sets the graphic for a CheckBox.
     * @param box checkbox
     * @param icon imported image
     */
    public ImageIcons(CheckBox box, Image icon) {
        ImageView image = new ImageView(icon);
        image.setFitWidth(15);
        image.setFitHeight(15);
        image.setPreserveRatio(true);
        box.setGraphic(image);
    }

    /**
     * Sets the graphic for a Label.
     * @param label label
     * @param icon imported image
     */
    public ImageIcons(Label label, Image icon) {
        ImageView image = new ImageView(icon);
        image.setFitWidth(15);
        image.setFitHeight(15);
        label.setGraphic(image);
    }
}
