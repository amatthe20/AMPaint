package com.example.ampaint;

import javafx.scene.layout.StackPane;

/**
 * Contains methods for the rotate menu.
 */
public class RotateMenu {
    /**
     * Rotates canvas 90 degrees right.
     * @param stack stackpane used for canvas
     * @param canvas current canvas
     */
    public static void right90(StackPane stack, NewCanvas canvas) {
        stack.setRotate(stack.getRotate() + 90);
        EditMenu.updateUndoStack(canvas);
        LoggerHandle.getLoggerHandle().writeToLog(true, "Canvas rotated right 90 degrees");
    }

    /**
     * Rotates canvas 90 degrees left.
     * @param stack stackpane used for canvas
     * @param canvas current canvas
     */
    public static void left90(StackPane stack, NewCanvas canvas) {
        stack.setRotate(stack.getRotate() - 90);
        EditMenu.updateUndoStack(canvas);
        LoggerHandle.getLoggerHandle().writeToLog(true, "Canvas rotated left 90 degrees");
    }

    /**
     * Rotates canvas 180 degrees right.
     * @param stack stackpane used for canvas
     * @param canvas current canvas
     */
    public static void right180(StackPane stack, NewCanvas canvas) {
        stack.setRotate(stack.getRotate() + 180);
        EditMenu.updateUndoStack(canvas);
        LoggerHandle.getLoggerHandle().writeToLog(true, "Canvas rotated 180 degrees");
    }

    /**
     * Rotates canvas 180 degrees left.
     * @param stack stackpane used for canvas
     * @param canvas current canvas
     */
    public static void left180(StackPane stack, NewCanvas canvas) {
        stack.setRotate(stack.getRotate() - 180);
        EditMenu.updateUndoStack(canvas);
        LoggerHandle.getLoggerHandle().writeToLog(true, "Canvas rotated 180 degrees");
    }

    /**
     * Rotates canvas 270 degrees left.
     * @param stack stackpane used for canvas
     * @param canvas current canvas
     */
    public static void right270(StackPane stack, NewCanvas canvas) {
        stack.setRotate(stack.getRotate() + 270);
        EditMenu.updateUndoStack(canvas);
        LoggerHandle.getLoggerHandle().writeToLog(true, "Canvas rotated 270 degrees");
    }

    /**
     * Rotates canvas 270 degrees left.
     * @param stack stackpane used for canvas
     * @param canvas current canvas
     */
    public static void left270(StackPane stack, NewCanvas canvas) {
        stack.setRotate(stack.getRotate() - 270);
        EditMenu.updateUndoStack(canvas);
        LoggerHandle.getLoggerHandle().writeToLog(true, "Canvas rotated 270 degrees");
    }

    /**
     * Flips canvas horizontally.
     * @param canvas current canvas
     */
    public static void horizontal(NewCanvas canvas) {
        canvas.setScaleX(-canvas.getScaleX());
        EditMenu.updateUndoStack(canvas);
        LoggerHandle.getLoggerHandle().writeToLog(true, "Canvas flipped vertically");
    }

    /**
     * Flips canvas vertically.
     * @param canvas current canvas
     */
    public static void vertical(NewCanvas canvas) {
        canvas.setScaleY(-canvas.getScaleY());
        EditMenu.updateUndoStack(canvas);
        LoggerHandle.getLoggerHandle().writeToLog(true, "Canvas flipped horizontally");
    }
}
