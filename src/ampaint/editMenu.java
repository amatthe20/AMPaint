package ampaint;

import java.util.Stack;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class editMenu {
    public static Stack<Image> undoStack = new Stack();
    public static Stack<Image> redoStack = new Stack();
    
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

    public static void redo(NewCanvas canvas) {
        if(!redoStack.isEmpty()) {
            Image im = redoStack.pop();
            undoStack.push(im);
            canvas.drawImage(im);
        }
    }

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
}
