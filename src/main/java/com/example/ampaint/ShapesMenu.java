package com.example.ampaint;

import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Contains methods for the shapes menu.
 */
public class ShapesMenu {
    private static double x1, y1, x2, y2;

    /**
     * Draws a square using mouse events and logs to user's home directory.
     * @param canvas current canvas
     * @param brushSize stroke thickness
     * @param cp current color
     * @param canvasCd mouse coordinates on the canvas
     */
    public static void square(NewCanvas canvas, TextField brushSize, ColorPicker cp, Label canvasCd) {
        canvas.setOnMousePressed(e-> {
            x1 = e.getX();
            y1 = e.getY();
        });

        canvas.setOnMouseDragged(e-> {
            x2 = e.getX();
            y2 = e.getY();
            canvasCd.setText(e.getX() + ", " + e.getY() + "px");
            EditMenu.updateUndoStack(canvas);
        });

        canvas.setOnMouseReleased(e-> {
            x2 = e.getX();
            y2 = e.getY();
            canvas.setLineWidth(Double.parseDouble(brushSize.getText()));
            canvas.setLineColor(cp.getValue());
            canvas.drawSquare(x1, y1, x2, y2);
            EditMenu.updateUndoStack(canvas);
        });
        LoggerHandle.getLoggerHandle().writeToLog(true,"Square selected");
    }

    /**
     * Draws a circle using mouse events and logs to user's home directory.
     * @param canvas current canvas
     * @param brushSize stroke thickness
     * @param cp current color
     * @param canvasCd mouse coordinates on the canvas
     */
    public static void circle(NewCanvas canvas, TextField brushSize, ColorPicker cp, Label canvasCd) {
        canvas.setOnMousePressed(e-> {
            x1 = e.getX();
            y1 = e.getY();
        });

        canvas.setOnMouseDragged(e-> {
            x2 = e.getX();
            y2 = e.getY();
            canvasCd.setText(e.getX() + ", " + e.getY() + "px");
            EditMenu.updateUndoStack(canvas);
        });

        canvas.setOnMouseReleased(e-> {
            x2 = e.getX();
            y2 = e.getY();
            canvas.setLineWidth(Double.parseDouble(brushSize.getText()));
            canvas.setLineColor(cp.getValue());
            canvas.drawCircle(x1, y1, x2, y2);
            EditMenu.updateUndoStack(canvas);
        });
        LoggerHandle.getLoggerHandle().writeToLog(true,"Circle selected");
    }

    /**
     * Draws a rectangle using mouse events and logs to user's home directory.
     * @param canvas current canvas
     * @param brushSize stroke thickness
     * @param cp current color
     * @param canvasCd mouse coordinates on the canvas
     */
    public static void rectangle(NewCanvas canvas, TextField brushSize, ColorPicker cp, Label canvasCd) {
        canvas.setOnMousePressed(e-> {
            x1 = e.getX();
            y1 = e.getY();
        });

        canvas.setOnMouseDragged(e-> {
            x2 = e.getX();
            y2 = e.getY();
            canvasCd.setText(e.getX() + ", " + e.getY() + "px");
            EditMenu.updateUndoStack(canvas);
        });

        canvas.setOnMouseReleased(e-> {
            x2 = e.getX();
            y2 = e.getY();
            canvas.setLineWidth(Double.parseDouble(brushSize.getText()));
            canvas.setLineColor(cp.getValue());
            canvas.drawRectangle(x1,y1,x2,y2);
            EditMenu.updateUndoStack(canvas);
        });
        LoggerHandle.getLoggerHandle().writeToLog(true,"Rectangle selected");
    }

    /**
     * Draws an ellipse using mouse events and logs to user's home directory.
     * @param canvas current canvas
     * @param brushSize stroke thickness
     * @param cp current color
     * @param canvasCd mouse coordinates on the canvas
     */
    public static void ellipse(NewCanvas canvas, TextField brushSize, ColorPicker cp, Label canvasCd) {
        canvas.setOnMousePressed(e-> {
            x1 = e.getX();
            y1 = e.getY();
        });

        canvas.setOnMouseDragged(e-> {
            x2 = e.getX();
            y2 = e.getY();
            canvasCd.setText(e.getX() + ", " + e.getY() + "px");
            EditMenu.updateUndoStack(canvas);
        });

        canvas.setOnMouseReleased(e-> {
            x2 = e.getX();
            y2 = e.getY();
            canvas.setLineWidth(Double.parseDouble(brushSize.getText()));
            canvas.setLineColor(cp.getValue());
            canvas.drawEllipse(x1,y1,x2,y2);
            EditMenu.updateUndoStack(canvas);
        });
        LoggerHandle.getLoggerHandle().writeToLog(true,"Ellipse selected");
    }

    /**
     * Draws a triangle using mouse events and logs to user's home directory.
     * @param canvas current canvas
     * @param brushSize stroke thickness
     * @param cp current color
     * @param canvasCd mouse coordinates on the canvas
     */
    public static void triangle(NewCanvas canvas, TextField brushSize, ColorPicker cp, Label canvasCd) {
        canvas.setOnMousePressed(e-> {
            x1 = e.getX();
            y1 = e.getY();
        });

        canvas.setOnMouseDragged(e-> {
            x2 = e.getX();
            y2 = e.getY();
            canvasCd.setText(e.getX() + ", " + e.getY() + "px");
            EditMenu.updateUndoStack(canvas);
        });

        canvas.setOnMouseReleased(e-> {
            x2 = e.getX();
            y2 = e.getY();
            canvas.setLineWidth(Double.parseDouble(brushSize.getText()));
            canvas.setLineColor(cp.getValue());
            canvas.drawTriangle(x1,y1,x2,y2);
            EditMenu.updateUndoStack(canvas);
        });
        LoggerHandle.getLoggerHandle().writeToLog(true,"Triangle selected");
    }

    /**
     * Draws a shape using user's shape and angle input and logs to user's home directory.
     * @param canvas current canvas
     * @param brushSize stroke thickness
     * @param cp current color
     * @param canvasCd mouse coordinates on the canvas
     */
    public static void shape(NewCanvas canvas, TextField brushSize, ColorPicker cp, Label canvasCd) {
        canvas.setShapeAngle();
        canvas.setShapeSides();
        canvas.setOnMousePressed(e-> {
            x1 = e.getX();
            y1 = e.getY();
        });

        canvas.setOnMouseDragged(e-> {
            x2 = e.getX();
            y2 = e.getY();
            canvasCd.setText(e.getX() + ", " + e.getY() + "px");
            EditMenu.updateUndoStack(canvas);
        });

        canvas.setOnMouseReleased(e-> {
            x2 = e.getX();
            y2 = e.getY();
            canvas.setLineWidth(Double.parseDouble(brushSize.getText()));
            canvas.setLineColor(cp.getValue());
            canvas.drawShape(canvas.getShapeAngle(), canvas.getShapeSides(),x1,y1,x2,y2);
            EditMenu.updateUndoStack(canvas);
        });
        LoggerHandle.getLoggerHandle().writeToLog(true,"New Shape selected");
    }
}
