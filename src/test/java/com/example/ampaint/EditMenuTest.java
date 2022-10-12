package com.example.ampaint;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Consists of test cases for certain EditMenu methods.
 */
class EditMenuTest {
    NewCanvas canvas = new NewCanvas();

    /**
     * Test method for undo
     */
    @Test
    void undo() {
        System.out.println("undo");
        Image im = (Image) new WritableImage(100, 100);
        canvas.drawImage(im);
        canvas.setLineColor(Color.CORAL);
        canvas.setLineWidth(10);
        canvas.drawRectangle(10, 30, 20, 10);
        EditMenu.updateUndoStack(canvas);
        EditMenu.undo(canvas);
        assertEquals(im, canvas.getImage());
    }

    /**
     * Test method for redo
     */
    @Test
    void redo() {
        System.out.println("redo");
        Image im = (Image) new WritableImage(100, 100);
        canvas.drawImage(im);
        canvas.drawRectangle(10, 30, 20, 10);
        EditMenu.updateUndoStack(canvas);
        Image im2 = canvas.getImage();
        EditMenu.undo(canvas);
        EditMenu.redo(canvas);
        assertEquals(im2, canvas.getImage());
    }

    /**
     * Test method for updateUndoStack
     */
    @Test
    void updateUndoStack() {
        System.out.println("update undo stacks");
        Image im = (Image) new WritableImage(100, 100);
        canvas.drawImage(im);
        canvas.drawRectangle(10, 30, 20, 10);
        EditMenu.updateUndoStack(canvas);
        canvas.drawSquare(5, 10, 10, 5);
        EditMenu.updateUndoStack(canvas);
        EditMenu.undo(canvas);
        EditMenu.undo(canvas);
        EditMenu.redo(canvas);
        assertEquals(im, canvas.getImage());
    }
}