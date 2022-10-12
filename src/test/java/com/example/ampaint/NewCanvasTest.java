package com.example.ampaint;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Consists of test cases for certain NewCanvas methods.
 */
class NewCanvasTest {
    NewCanvas canvas = new NewCanvas();

    /**
     * Test method for drawRectangle
     */
    @Test
    void drawRectangle() {
        System.out.println("draw rectangle");
        Image im = (Image) new WritableImage(100, 100);
        canvas.drawImage(im);
        canvas.setLineColor(Color.DARKGREEN);
        canvas.setLineWidth(10);
        canvas.drawRectangle(10, 30, 20, 10);
        assertEquals(im, canvas.getImage());
    }

    /**
     * Test method for drawSquare
     */
    @Test
    void drawSquare() {
        System.out.println("draw square");
        Image im = (Image) new WritableImage(100, 100);
        canvas.drawImage(im);
        canvas.setLineColor(Color.AQUAMARINE);
        canvas.setLineWidth(10);
        canvas.drawSquare(10, 20, 20, 10);
        assertEquals(im, canvas.getImage());
    }

    /**
     * Test method for drawTriangle
     */
    @Test
    void drawTriangle() {
        System.out.println("draw triangle");
        Image im = (Image) new WritableImage(100, 100);
        canvas.drawImage(im);
        canvas.setLineColor(Color.PEACHPUFF);
        canvas.setLineWidth(10);
        canvas.drawTriangle(10, 30, 20, 10);
        assertEquals(im, canvas.getImage());
    }

    /**
     * Test method for drawCircle
     */
    @Test
    void drawCircle() {
        System.out.println("draw triangle");
        Image im = (Image) new WritableImage(100, 100);
        canvas.drawImage(im);
        canvas.setLineColor(Color.LAVENDER);
        canvas.setLineWidth(10);
        canvas.drawCircle(10, 10, 20, 10);
        assertEquals(im, canvas.getImage());
    }

    /**
     * Test method for drawEllipse
     */
    @Test
    void drawEllipse() {
        System.out.println("draw ellipse");
        Image im = (Image) new WritableImage(100, 100);
        canvas.drawImage(im);
        canvas.setLineColor(Color.GOLD);
        canvas.setLineWidth(10);
        canvas.drawCircle(10, 10, 20, 10);
        assertEquals(im, canvas.getImage());
    }

    /**
     * Test method for setLineColor
     */
    @Test
    void setLineColor() {
        System.out.println("set line color");
        canvas.setLineColor(Color.LAVENDER);
        assertEquals(Color.LAVENDER, canvas.getLineColor());
    }

    /**
     * Test method for setLineWidth
     */
    @Test
    void setLineWidth() {
        System.out.println("set line width");
        canvas.setLineWidth(10);
        assertEquals(10, canvas.getLineWidth());
    }
}