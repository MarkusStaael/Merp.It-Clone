package org.example.firstyearproject.Misc;

import javafx.scene.canvas.Canvas;

import java.io.Serializable;

public class ResizableCanvas extends Canvas implements Serializable {
    //credit to https://stackoverflow.com/questions/24533556/how-to-make-canvas-resizable-in-javafx
    public ResizableCanvas(double v1, double v2){
        super(v1,v2);
    }
    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double maxHeight(double width) {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public double maxWidth(double height) {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public double minWidth(double height) {
        return 1D;
    }

    @Override
    public double minHeight(double width) {
        return 1D;
    }

    @Override
    public void resize(double width, double height) {
        this.setWidth(width);
        this.setHeight(height);
    }
}