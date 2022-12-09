package com.example.ampaint;

import javafx.scene.control.*;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * Contains methods for the toolbar.
 */
public class Toolbar {
    private static double zoomStartVal = 5;
    private static double zoomScale = zoomStartVal;

    /**
     * Acts as the freehand tool and logs to user's home directory.
     * @param canvas current canvas
     * @param brushSize stroke thickness
     * @param cp current color
     * @param canvasCd mouse coordinates on the canvas
     */
    public static void pencil(NewCanvas canvas, TextField brushSize, ColorPicker cp, Label canvasCd) {      // default tool
        canvas.setOnMousePressed(e-> {
            canvas.getApp().beginPath();
            canvas.setLineWidth(Double.parseDouble(brushSize.getText()));
            canvas.setLineColor(cp.getValue());
            canvas.unDashLine(e.getX(), e.getY());
            canvas.getApp().moveTo(e.getX(), e.getY());
            canvas.getApp().stroke();
            EditMenu.updateUndoStack(canvas);    // updates undo stack
        });

        canvas.setOnMouseDragged(e-> {
            canvas.unDashLine(e.getX(), e.getY());
            canvasCd.setText(e.getX() + ", " + e.getY() + "px");
            canvas.getApp().lineTo(e.getX(), e.getY());
            canvas.getApp().stroke();       // draws continuous line (no unwanted spaces)
            EditMenu.updateUndoStack(canvas);
        });

        canvas.setOnMouseReleased(e-> {
            canvas.unDashLine(e.getX(), e.getY());
            canvas.getApp().lineTo(e.getX(), e.getY());
            canvas.getApp().stroke();
            canvas.getApp().closePath();
            EditMenu.updateUndoStack(canvas);
        });
        LoggerHandle.getLoggerHandle().writeToLog(true,"Pencil tool activated");
    }

    /**
     * Draws a dashed line using mouse events and logs to user's home directory.
     * @param canvas current canvas
     * @param brushSize stroke thickness
     * @param cp current color
     * @param canvasCd mouse coordinates on the canvas
     */
    public static void dLine(NewCanvas canvas, TextField brushSize, ColorPicker cp, Label canvasCd) {   // draws a dashed line
        canvas.setOnMousePressed(e -> {
            canvas.getApp().beginPath();
            canvas.setLineWidth(Double.parseDouble(brushSize.getText()));      // set width of the stroke
            canvas.setLineColor(cp.getValue());                                // set color of the stroke
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
     * Draws a straight line using mouse events and logs to user's home directory.
     * @param canvas current canvas
     * @param brushSize stroke thickness
     * @param cp current color
     * @param canvasCd mouse coordinates on the canvas
     */
    public static void sLine(NewCanvas canvas, TextField brushSize, ColorPicker cp, Label canvasCd) {     // draws a straight line
        canvas.setOnMousePressed(e-> {
            canvas.getApp().beginPath();
            canvas.setLineColor(cp.getValue());
            canvas.setLineWidth(Double.parseDouble(brushSize.getText()));
            canvas.unDashLine(e.getX(), e.getY());
            canvas.getApp().lineTo(e.getX(), e.getY());
            EditMenu.updateUndoStack(canvas);
        });

        canvas.setOnMouseDragged(e-> {
            canvas.unDashLine(e.getX(), e.getY());
            canvas.getApp().lineTo(e.getX(), e.getY());
            canvas.getApp().stroke();
            canvasCd.setText(e.getX() + ", " + e.getY() + "px");
            EditMenu.updateUndoStack(canvas);
        });

        canvas.setOnMouseReleased(e-> {
            canvas.unDashLine(e.getX(), e.getY());
            canvas.getApp().lineTo(e.getX(), e.getY());
            canvas.getApp().stroke();
            canvas.getApp().closePath();
            EditMenu.updateUndoStack(canvas);
        });
        LoggerHandle.getLoggerHandle().writeToLog(true,"Straight Line tool activated");
    }

    /**
     * Clears the current stroke from the canvas and logs to user's home directory
     * @param canvas    current canvas
     * @param brushSize stroke thickness
     * @param cp        current color
     * @param canvasCd  mouse coordinates on the canvas
     */
    public static void eraser(NewCanvas canvas, CheckBox eraser, TextField brushSize, ColorPicker cp, Label canvasCd) {
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
            pencil(canvas, brushSize, cp, canvasCd);      // goes to default tool
            LoggerHandle.getLoggerHandle().writeToLog(true,"Eraser unchecked");
        }
    }

    /**
     * Erases the entire canvas after a warning.
     * @param canvas current canvas
     */
    public static void clear(NewCanvas canvas) {
        canvas.clearCanvas();
        EditMenu.updateUndoStack(canvas);
        LoggerHandle.getLoggerHandle().writeToLog(true, "Canvas cleared");
    }

    /**
     * Displays the hex value of a selected pixel.
     * @param canvas current canvas
     * @param pixelColor hex value of pixel
     * @param colorGrabber colorGrabber button
     */
    public static void colorGrab(NewCanvas canvas, Label pixelColor, ToggleButton colorGrabber) {
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
     * @param canvas current canvas
     * @param width new canvas width
     * @param height new canvas height
     */
    public static void resize(NewCanvas canvas, TextField width, TextField height) {
        int cWidth = Integer.parseInt(width.getText());
        int cHeight = Integer.parseInt(height.getText());
        canvas.setDimensions(cWidth,cHeight);
        EditMenu.updateUndoStack(canvas);
        LoggerHandle.getLoggerHandle().writeToLog(true, "Canvas resized to " + cWidth + " x " + cHeight);
    }

    /**
     * Zooms in on the canvas.
     * @param stack stackpane used for canvas
     * @param scroll scrollpane used on canvas
     */
    public static void zoomIn(StackPane stack, ScrollPane scroll) {
        zoomScale++;    // in
        zoomScale = Math.max(1, zoomScale);
        stack.setScaleX(zoomScale/zoomStartVal);
        stack.setScaleY(zoomScale/zoomStartVal);
        scroll.setHvalue(0.5);
        scroll.setVvalue(0.5);
    }

    /**
     * Zooms out on the canvas.
     * @param stack stackpane used for canvas
     * @param scroll scrollpane used on canvas
     */
    public static void zoomOut(StackPane stack, ScrollPane scroll) {
        zoomScale--;   // out
        zoomScale = Math.max(1, zoomScale);
        stack.setScaleX(zoomScale/zoomStartVal);
        stack.setScaleY(zoomScale/zoomStartVal);
        scroll.setHvalue(0.5);
        scroll.setVvalue(0.5);
    }
}
