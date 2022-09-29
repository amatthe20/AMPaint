package ampaint;

import java.io.File;
import java.util.Optional;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class NewCanvas extends Canvas {

    protected GraphicsContext app;
    private boolean isClickedFirst = true;
    //public Image image;

    public NewCanvas() {
        super();
        this.app = this.getGraphicsContext2D();
        this.setWidth(400);
        this.setHeight(400);
    }
    
    public void drawImageAt(Image can, double x, double y){this.app.drawImage(can, x, y);}

    public void drawImage(Image im){   // for setting an image to the canvas
        this.setWidth(im.getWidth());
        this.setHeight(im.getHeight());
        this.app.drawImage(im, 0, 0);
    }

    public void drawImage(File file) {     // for opening a file from the user's desktop
        if (file != null) {
            Image im = new Image(file.toURI().toString());
            this.drawImage(im);
        }
    }

    public Image getImage() {
            SnapshotParameters para = new SnapshotParameters();
            WritableImage wi = this.snapshot(para, null);
            ImageView iv = new ImageView(wi);
            return iv.getImage();
        }


    public void setDimensions(int width, int height) {     // for resizing canvas
        this.setWidth(width);
        this.setHeight(height);
    }

    public void updateDimensions() {     // sets image in current tab
        if(paintController.getCurrentTab().thisImage != null) {
            this.setHeight(paintController.getCurrentTab().thisImage.getHeight());
            this.setWidth(paintController.getCurrentTab().thisImage.getWidth());
        } else {
            this.setHeight(0);
            this.setWidth(0);
        }
    }

    public void updateDimensions(Image i) {
        setDimensions((int) i.getWidth(), (int) i.getHeight());
    }

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

    public void drawStraightLine(double x, double y) {      // drawing a straight line
        this.app.lineTo(x, y);
    }


    public void drawDashedLine(double size, double x, double y) {     // drawing a dashed line
        this.app.setLineDashes(3*size);
        this.app.setLineDashOffset(4);
        this.app.lineTo(x, y);
    }


    public void drawRectangle(double x1, double y1, double x2, double y2) {
        double x = Math.min(x1, x2);
        double y = Math.min(y1, y2);
        double w = Math.abs(x1 - x2);
        double h = Math.abs(y1 - y2);
        this.app.strokeRect(x, y, w, h);
    }

    public void drawSquare(double x1, double y1, double x2, double y2) {
        final double ANGLE_45 = Math.PI / 4.0;    // a 45 degree angle makes it a square
        final int SIDES = 4;
        double[] xPoints = new double[SIDES];   // a square has 4 sides
        double[] yPoints = new double[SIDES];
        double radius = Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
        for (int i = 0; i < SIDES; i++) {
            xPoints[i] = x1 + (radius * Math.cos(((2 * Math.PI * i) / 4) + ANGLE_45));
            yPoints[i] = y1 + (radius * Math.sin(((2 * Math.PI * i) / 4) + ANGLE_45));
        }
        this.app.strokePolygon(xPoints, yPoints, SIDES);
    }

    public void drawCircle(double x1, double y1, double x2, double y2) {
        double x = Math.min(x1, x2);
        double y = Math.min(y1, y2);
        double s = Math.abs(x1 - x2);
        double t = Math.abs(y1 - y2);
        this.app.strokeOval(x, y, s, s);
    }

    public void drawEllipse(double x1, double y1, double x2, double y2) {
        double x = Math.min(x1, x2);
        double y = Math.min(y1, y2);
        double s = Math.abs(x1 - x2);
        double t = Math.abs(y1 - y2);
        this.app.strokeOval(x, y, s, t);
    }

    public void setLineColor(Color color){this.app.setStroke(color);}      // color setter 

    public Color getLineColor(){return (Color)this.app.getStroke();}       // color getter

    public void setLineWidth(double width){this.app.setLineWidth(width);}  // width setter

    public double getLineWidth(){return this.app.getLineWidth();}          // width getter
    
    public GraphicsContext getApp() {
        return app;
    }

}
