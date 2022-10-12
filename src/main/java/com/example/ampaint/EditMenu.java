package com.example.ampaint;

import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.Stack;

/**
 * Contains methods for the edit menu.
 */
public class EditMenu {
    public static Stack<Image> undoStack = new Stack();
    public static Stack<Image> redoStack = new Stack();

    private static Image clipboard = null;

    /**
     * Undoes current edit by "popping" the edited canvas back on the undo stack. This canvas will get pushed down the redo stack.
     * If it's the first edit, the canvas will be pushed down the undo stack.
     * @param canvas the current canvas
     */
    public static void undo(NewCanvas canvas) {
        Image im = undoStack.pop();
        if (!undoStack.empty()) {
            redoStack.push(im);
            canvas.drawImage(undoStack.peek());
        } else {   // puts image back because in this case it's the only one in stack
            canvas.drawImage(im);
            undoStack.push(im);
        }
    }

    /**
     * Redoes current edit by "popping" the old canvas back on the undo stack. This canvas will get pushed down again.
     * @param canvas the current canvas
     */
    public static void redo(NewCanvas canvas) {
        if(!redoStack.isEmpty()) {
            Image im = redoStack.pop();
            undoStack.push(im);
            canvas.drawImage(im);
        }
    }

    /**
     * Gets the edits from the canvas by taking a snapshot of the canvas and pushing that image unto the stack.
     * @param canvas the current canvas
     */
    public static void updateUndoStack(NewCanvas canvas) {
        double x = canvas.getScaleX(); // stores original x and y scales to reset after save
        double y = canvas.getScaleY();
        canvas.setScaleX(1);            // briefly sets canvas scale to default to avoid saving errors
        canvas.setScaleY(1);
        WritableImage writ = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
        SnapshotParameters para = new SnapshotParameters();
        para.setFill(Color.TRANSPARENT);  // clean background
        Image im = canvas.snapshot(para, writ);
        canvas.setScaleX(x);            // set back to original scale
        canvas.setScaleY(y);
        undoStack.push(im);      // push to undo stack
    }

    /**
     * Takes snapshot of selected canvas and copies to clipboard
     */
    public static void copy(NewCanvas canvas) {
        canvas.setOnMousePressed(e-> {
            canvas.setLineColor(Color.RED);
            canvas.setLineWidth(5);
            canvas.drawRectangle(e.getX(),e.getY(),e.getX(),e.getY());
            updateUndoStack(canvas);
        });

        canvas.setOnMouseDragged(e-> {
            //undo(canvas);
            canvas.drawRectangle(e.getX(),e.getY(),e.getX(),e.getY());
            updateUndoStack(canvas);
        });

        canvas.setOnMouseReleased(e-> {
            //undo(canvas);
            clipboard = canvas.getRegion(0,0,e.getX(),e.getY());
            updateUndoStack(canvas);
        });

    }

    /**
     * Displays copied canvas snapshot unto canvas
     */
    public static void paste(NewCanvas canvas) {
        canvas.setOnMouseClicked(e-> {
            try {
                canvas.drawImageAt(clipboard, e.getX(), e.getY());
            }
            catch(Exception p) {
                System.out.println(e);
            }
            updateUndoStack(canvas);
        });
    }

    /*public static void cut(NewCanvas canvas) {      // TODO
        canvas.setOnMousePressed(e-> {

        });

        canvas.setOnMouseDragged(e-> {

        });

        canvas.setOnMouseReleased(e-> {

        });
    }*/
}
