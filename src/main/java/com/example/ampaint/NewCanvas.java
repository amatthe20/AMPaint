package com.example.ampaint;

import javafx.event.ActionEvent;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.io.File;
import java.util.Optional;

/**
 * Extends Canvas class to allow functionality for the canvas.
 */
public class NewCanvas extends Canvas {
    protected GraphicsContext app;
    private boolean isClickedFirst = true;
    private double angle;
    private int sides;
    private TextInputDialog shape1, shape2;

    /** Canvas constructor. */
    public NewCanvas() {
        super();
        this.app = this.getGraphicsContext2D();
        this.setWidth(400);
        this.setHeight(400);
        EditMenu.updateUndoStack(this);
    }

    public void select(Scene scene) {
        scene.setCursor(Cursor.CROSSHAIR);
    }

    /**
     * Places image on canvas using x,y coordinates.
     * @param can canvas
     * @param x x coordinate of mouse position
     * @param y y coordinate of mouse position
     */
    public void drawImageAt(Image can, double x, double y){this.app.drawImage(can, x, y);}

    /**
     * Places image on canvas.
     * @param im imported image from computer
     */
    public void drawImage(Image im){   // for setting an image to the canvas
        this.setWidth(im.getWidth());
        this.setHeight(im.getHeight());
        this.app.drawImage(im, 0, 0);
    }

    /**
     * Gets image from file on user's computer.
     * @param file desktop image file
     */
    public void drawImage(File file) {     // for opening a file from the user's desktop
        if (file != null) {
            Image im = new Image(file.toURI().toString());
            this.drawImage(im);
        }
    }

    /**
     * Gets the current image by taking a snapshot of the canvas.
     * @return the canvas snapshot
     */
    public Image getImage() {
        SnapshotParameters para = new SnapshotParameters();
        WritableImage wi = this.snapshot(para, null);
        ImageView iv = new ImageView(wi);
        return iv.getImage();
    }

    public Image getRegion(double x1, double y1, double x2, double y2) {
        SnapshotParameters snap = new SnapshotParameters();
        WritableImage writ = new WritableImage((int)Math.abs(x1-x2), (int)Math.abs(y1-y2));

        snap.setViewport(new Rectangle2D((x1 < x2 ? x1: x2), (y1 < y2 ? y1: y2), Math.abs(x1-x2), Math.abs(x1-x2)));
        this.snapshot(snap,writ);
        return writ;
    }

    /**
     * Sets the canvas's dimensions for resizing.
     * @param width width of the new canvas
     * @param height height of the new canvas
     */
    public void setDimensions(int width, int height) {     // for resizing canvas
        this.setWidth(width);
        this.setHeight(height);
    }

    /**
     * Sets the canvas's dimensions from the current tab.
     */
    public void updateDimensions() {     // sets image in current tab
        if(paintController.getCurrentTab().thisImage != null) {
            this.setHeight(paintController.getCurrentTab().thisImage.getHeight());
            this.setWidth(paintController.getCurrentTab().thisImage.getWidth());
        } else {
            this.setHeight(0);
            this.setWidth(0);
        }
    }

    /**
     * Sets the canvas's dimensions from the imported image from user's computer.
     * @param i imported image from computer
     */
    public void updateDimensions(Image i) {
        setDimensions((int) i.getWidth(), (int) i.getHeight());
    }

    /**
     * Displays alert when user clicks its button that will clear the canvas.
     */
    public void clearCanvas() {       // when the user wants to start over
        Alert clear = new Alert(Alert.AlertType.CONFIRMATION);
        clear.setTitle("Confirmation");
        clear.setHeaderText("Clear this canvas?");
        clear.setContentText("Are you sure?");

        ButtonType yes = new ButtonType("Yes");
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        clear.getButtonTypes().setAll(yes, no);    // 2 options
        Optional<ButtonType> result = clear.showAndWait();
        if (result.get() == yes) {
            this.app.clearRect(0, 0, this.getWidth(), this.getHeight());
        } else if (result.get() == no) {
            // nothing
        }
    }

    /**
     * Draws a dashed line from mouse coordinates.
     * @param size thickness of the line
     * @param x x coordinate of the mouse
     * @param y y coordinate of the mouse
     */
    public void drawDashedLine(double size, double x, double y) {     // drawing a dashed line
        this.app.setLineDashes(3*size);
        this.app.setLineDashOffset(4);
        this.app.lineTo(x, y);
    }

    /**
     * Draws a rectangle from mouse coordinates.
     * @param x1 initial x coordinate of the mouse
     * @param y1 initial y coordinate of the mouse
     * @param x2 final x coordinate of the mouse
     * @param y2 final y coordinate of the mouse
     */
    public void drawRectangle(double x1, double y1, double x2, double y2) {
        double x = Math.min(x1, x2);
        double y = Math.min(y1, y2);
        double w = Math.abs(x1 - x2);
        double h = Math.abs(y1 - y2);
        this.app.strokeRect(x, y, w, h);
    }

    /**
     * Draws a square from mouse coordinates.
     * @param x1 initial x coordinate of the mouse
     * @param y1 initial y coordinate of the mouse
     * @param x2 final x coordinate of the mouse
     * @param y2 final y coordinate of the mouse
     */
    public void drawSquare(double x1, double y1, double x2, double y2) {
        drawShape(4.0,4,x1,y1,x2,y2);
    }

    /**
     * Draws a triangle from mouse coordinates.
     * @param x1 initial x coordinate of the mouse
     * @param y1 initial y coordinate of the mouse
     * @param x2 final x coordinate of the mouse
     * @param y2 final y coordinate of the mouse
     */
    public void drawTriangle(double x1, double y1, double x2, double y2) {
        drawShape(6.0,3,x1,y1,x2,y2);
    }

    /**
     * Draws a regular sided polygon from mouse coordinates.
     * @param angle orientation of the polygon
     * @param sides number of sides for the polygon
     * @param x1 initial x coordinate of the mouse
     * @param y1 initial y coordinate of the mouse
     * @param x2 final x coordinate of the mouse
     * @param y2 final y coordinate of the mouse
     */
    public void drawShape(double angle, int sides, double x1, double y1, double x2, double y2) {
        final double ANGLE = Math.PI / angle;    // for placing the shape in an orientation
        final int SIDES = sides;
        double[] xPoints = new double[SIDES];    // gets the sides
        double[] yPoints = new double[SIDES];
        double radius = Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));      // calculates the radius
        for (int i = 0; i < SIDES; i++) {        // for each side
            xPoints[i] = x1 + (radius * Math.cos(((2 * Math.PI * i) / sides) + ANGLE));  // draw shape
            yPoints[i] = y1 + (radius * Math.sin(((2 * Math.PI * i) / sides) + ANGLE));
        }
        this.app.strokePolygon(xPoints, yPoints, SIDES);
    }

    /**
     * Sets the polygon's angle from user input.
     */
    public void setShapeAngle() {    // for drawShape function (gets the angle)
        this.shape1 = new TextInputDialog("Enter number as double (i.e. 5.0)");
        this.shape1.setTitle("New Shape");
        this.shape1.setHeaderText("Enter the preferred angle over pi");
        final Button ok = (Button) this.shape1.getDialogPane().lookupButton(ButtonType.OK);
        ok.addEventFilter(ActionEvent.ACTION, event ->
                System.out.println("OK was pressed from shape angle dialog")
        );

        final Button cancel = (Button) this.shape1.getDialogPane().lookupButton(ButtonType.CANCEL);
        cancel.addEventFilter(ActionEvent.ACTION, event ->
                System.out.println("Cancel was pressed from shape angle dialog")
        );

        Optional<String> result = this.shape1.showAndWait();
        if (result.isPresent()) {
            try {     // if the input is a double
                this.angle = Double.parseDouble(shape1.getEditor().getText());
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        } else {} // close dialog
    }

    /**
     * Gets the polygon's angle from user input.
     * @return orientation of the polygon
     */
    public double getShapeAngle() {return this.angle;}

    /**
     * Sets the polygon's sides from user input.
     */
    public void setShapeSides() {       // for drawShape function (gets the sides)
        shape2 = new TextInputDialog("Enter number as an integer (i.e. 5)");
        shape2.setTitle("New Shape");
        shape2.setHeaderText("Enter the number of sides");
        final Button ok = (Button) this.shape2.getDialogPane().lookupButton(ButtonType.OK);
        ok.addEventFilter(ActionEvent.ACTION, event ->
                System.out.println("OK was pressed from shape sides dialog")
        );

        final Button cancel = (Button) this.shape2.getDialogPane().lookupButton(ButtonType.CANCEL);
        cancel.addEventFilter(ActionEvent.ACTION, event ->
                System.out.println("Cancel was pressed from shape sides dialog")
        );

        Optional<String> result = this.shape2.showAndWait();
        if (result.isPresent()) {
            try {     // if the input is a double
                this.sides = Integer.parseInt(shape2.getEditor().getText());
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        } else {} // close dialog
    }

    /**
     * Gets the polygon's sides from user input.
     * @return number of sides
     */
    public int getShapeSides() {return sides;}

    /**
     * Draws a circle from mouse coordinates.
     * @param x1 initial x coordinate of the mouse
     * @param y1 initial y coordinate of the mouse
     * @param x2 final x coordinate of the mouse
     * @param y2 final y coordinate of the mouse
     */
    public void drawCircle(double x1, double y1, double x2, double y2) {
        double x = Math.min(x1, x2);     // gets x
        double y = Math.min(y1, y2);     // gets y
        double s = Math.abs(x1 - x2);
        double t = Math.abs(y1 - y2);
        this.app.strokeOval(x, y, s, s);
    }

    /**
     * Draws an ellipse from mouse coordinates.
     * @param x1 initial x coordinate of the mouse
     * @param y1 initial y coordinate of the mouse
     * @param x2 final x coordinate of the mouse
     * @param y2 final y coordinate of the mouse
     */
    public void drawEllipse(double x1, double y1, double x2, double y2) {
        double x = Math.min(x1, x2);
        double y = Math.min(y1, y2);
        double s = Math.abs(x1 - x2);
        double t = Math.abs(y1 - y2);
        this.app.strokeOval(x, y, s, t);
    }

    /**
     * Sets the color of the stroke.
     * @param color color value
     */
    public void setLineColor(Color color){this.app.setStroke(color);}      // color setter

    /**
     * Gets the current color of the stroke.
     * @return color value
     */
    public Color getLineColor(){return (Color)this.app.getStroke();}       // color getter

    /**
     * Sets the thickness of the stroke.
     * @param width width value
     */
    public void setLineWidth(double width){this.app.setLineWidth(width);}  // width setter

    /**
     * Gets the current thickness of the stroke.
     * @return width value
     */
    public double getLineWidth(){return this.app.getLineWidth();}          // width getter

    /**
     * Gets the canvas's GraphicsContext.
     * @return the app
     */
    public GraphicsContext getApp() {return app;}

    @Override
    public boolean isResizable() {return true;}
}

