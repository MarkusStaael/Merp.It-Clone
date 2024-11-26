package org.example.firstyearproject;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.transform.Affine;
import javafx.stage.Stage;

import org.example.firstyearproject.Algorithms.Dijkstra;
import org.example.firstyearproject.DataStructures.TernaryST;
import org.example.firstyearproject.DataTypes.ColorGroup;
import org.example.firstyearproject.MapObjects.Node;
import org.example.firstyearproject.MapObjects.Way;
import org.example.firstyearproject.Misc.Settings;
import org.example.firstyearproject.preloader.PreloaderController;
import org.example.firstyearproject.preloader.PreloaderView;

import java.io.IOException;
import java.util.ArrayList;

public class Controller {
    double lastX;
    double lastY;
    double zoom_level;

    public Controller(Model model, View view, Stage primarystage, Affine trans) {
        mousePressed(view, trans);
        mouseDragged(view, trans);
        mouseScrolled(view);
        zoomInButtonClicked(view);
        zoomOutButtonClicked(view);
        searchbarsSearched(view, model);
        kdTreeUpdates(view);
        loadnewButtonClicked(view, primarystage, model);
        exportToObjClicked(view, model);
        closeViewButtonClicked(view);
        pinDropped(view, model);
        windowResized(view);
        darkModeClicked(view);
        toggleGraphButtonClicked(view);
        toggleRouteButtonClicked(view);
        toggleBikeGraphButtonClicked(view);
        carButtonClicked(view, model);
        bikeButtonClicked(view, model);
    }

    private void mousePressed(View view, Affine trans) {
        view.canvas.setOnMousePressed(e -> {
            lastX = e.getX();
            lastY = e.getY();
        });
    }

    private void mouseDragged(View view, Affine trans) {
        view.canvas.setOnMouseDragged(e -> {
            if (e.isPrimaryButtonDown()) {
                double dx = e.getX() - lastX;
                double dy = e.getY() - lastY;
                view.pan(dx, dy);
            }
            lastX = e.getX();
            lastY = e.getY();
        });
    }

    private void mouseScrolled(View view) {
        view.canvas.setOnScroll(e -> {
//            if (!view.checkZoomOut() && e.getDeltaY() < 0) return;
//            if (!view.checkZoomIn() && e.getDeltaY() > 0) return;
            double factor = e.getDeltaY();
            zoom_level = zoom_level + factor;
            view.zoom(e.getX(), e.getY(), Math.pow(1.01, factor));
            updateZoom(view);
        });
    }

    private void zoomInButtonClicked(View view) {
        view.buttonplus.setOnAction(e -> {
//            if (!view.checkZoomIn()) return;
            view.zoom(view.canvas.getWidth() / 2, view.canvas.getHeight() / 2, Math.pow(1.01, 20));
            zoom_level = zoom_level + 20;
            updateZoom(view);
        });
    }

    private void zoomOutButtonClicked(View view) {
        view.buttonminus.setOnAction(e -> {
//            if (!view.checkZoomOut()) return;
            view.zoom(view.canvas.getWidth() / 2, view.canvas.getHeight() / 2, Math.pow(1.01, -20));
            zoom_level = zoom_level - 20;
            updateZoom(view);
        });
    }

    private void closeViewButtonClicked(View view) {
        view.close.setOnAction(e -> {
            view.primaryStage.close();
        });
    }

    private void loadnewButtonClicked(View view, Stage primarystage, Model model) {
        view.load.setOnAction(e -> {
            setModelNull(model);
            PreloaderView preloaderView = new PreloaderView(primarystage);
            new PreloaderController(preloaderView, primarystage);
        });
    }

    private void exportToObjClicked(View view, Model model) {
        view.save.setOnAction(e ->{
            try {
                model.save(view.url);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void kdTreeUpdates(View view) {
        view.scene.setOnKeyPressed(event -> {
            String codeString = event.getCode().toString();
            if(codeString.equals("K")){
                view.toggleMinMax();
                view.redraw();
            }

            //System.out.println(codeString);
        });
    }

    private void searchbarsSearched(View view, Model model){
        view.fromSearchbar.addEventHandler(KeyEvent.KEY_RELEASED, e ->{
            String input = view.fromSearchbar.getEditor().getText();
            if (input.length()>3){
                view.fromSearchbar.show();
            }
        });
        view.toSearchbar.addEventHandler(KeyEvent.KEY_RELEASED, e ->{
            String input = view.toSearchbar.getEditor().getText();
            if (input.length()>3){
                view.toSearchbar.show();
            }
        });
    }
    private void setModelNull(Model model) {
        model.kdTrees = null;
        model.TST = null;
        model.EWG = null;
        model.cycleEWG = null;
        model.tempnode = null;
        Model.counter = 0;
        Model.AddressList = new ArrayList<>();
        Model.tempInfo = new ArrayList<>();
        Model.tempCat = new ArrayList<>();
    }

    private void pinDropped(View view, Model model) {
        view.fromSearchbar.setOnKeyPressed(e -> {
            dropPin(view, model, e);

        });
        view.toSearchbar.setOnKeyPressed(e -> {
            dropPin(view, model, e);
        });
    }

    private void dropPin(View view, Model model, Event e) {
        if (!(e instanceof ActionEvent) && !(e instanceof KeyEvent)) return;
        if (e instanceof KeyEvent) {
            KeyEvent keyEvent = (KeyEvent) e;
            if (keyEvent.getCode() != KeyCode.ENTER) return;
        }

        String fromInput = view.fromSearchbar.getEditor().getText();
        String toInput = view.toSearchbar.getEditor().getText();

        if (!fromInput.isEmpty()) {
            Node node = model.TST.get(fromInput);
            view.pins.put("from", node);
        } else {
            view.pins.put("from", null);
            view.route = null;
        }
        if (!toInput.isEmpty()) {
            Node node = model.TST.get(toInput);
            view.pins.put("to", node);
        } else {
            view.pins.put("to", null);
            view.route = null;
        }
        view.redraw();
    }

    private void carButtonClicked(View view, Model model) {
        view.carButton.setOnAction(e -> {
            view.car = true;
            view.bike = false;
            dropPin(view, model, e);
            findPath(view, model);
        });
    }

    private void darkModeClicked(View view) {
        view.toggleColors.setOnAction(e -> {
            view.darkMode = !view.darkMode;
            if (view.darkMode) Settings.darkModeColor();
            else Settings.iniColor();
            view.redraw();
        });
    }

    private void bikeButtonClicked(View view, Model model) {
        view.bikeButton.setOnAction(e -> {
            view.bike = true;
            view.car = false;
            dropPin(view, model, e);
            findPath(view, model);
        });
    }

    private void findPath(View view, Model model) {
        if (view.pins.get("from") != null && view.pins.get("to") != null) {
            if (view.car) {
                Node from = model.EWG.findNearestWayNode(view.pins.get("from"), model.EWG);
                Node to = model.EWG.findNearestWayNode(view.pins.get("to"), model.EWG);
                Dijkstra dj = computePath(view, model, from);
                if (dj != null)
                    view.route = new Way(dj.pathTo(to), ColorGroup.HIGHWAY_ROUTE);

            }
            if (view.bike) {
                Node fromCycle = model.cycleEWG.findNearestWayNode(view.pins.get("from"), model.EWG);
                Node toCycle = model.cycleEWG.findNearestWayNode(view.pins.get("to"), model.EWG);
                Dijkstra cycleDJ = computePath(view, model, fromCycle);
                if (cycleDJ != null){
                    view.route = new Way(cycleDJ.pathTo(toCycle), ColorGroup.HIGHWAY_ROUTE);
                }
            }
        }else {
            view.route = null;
        }
        view.redraw();
    }

    private Dijkstra computePath(View view, Model model, Node from) {
        if (view.bike) {
            return new Dijkstra(model.cycleEWG, from.counter);
        }
        if (view.car) {
            return new Dijkstra(model.EWG, from.counter);
        }
        return null;
    }


    private void toggleGraphButtonClicked(View view) {
        view.toggleCarGraph.setOnAction(e -> {
            view.toDrawGraph = !view.toDrawGraph;
            view.redraw();
        });
    }

    private void toggleRouteButtonClicked(View view) {
        view.toggleRoute.setOnAction(e -> {
            view.toDrawRoute = !view.toDrawRoute;
            view.redraw();
        });
    }

    private void toggleBikeGraphButtonClicked(View view) {
        view.toggleBikeGraph.setOnAction(e -> {
            view.toDrawBikeGraph = !view.toDrawBikeGraph;
            view.redraw();
        });
    }

    private void windowResized(View view) {
        view.scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            view.redraw();
        });

        view.scene.heightProperty().addListener((observable, oldValue, newValue) -> {
            view.redraw();
        });
    }

    private void updateZoom(View view) {
        view.zoomzoom = view.trans.getMxx();
        String zoomlevel = String.format("%.2f", view.zoomzoom / view.maxZoom);
        view.zoomlevel.setText(zoomlevel + "%");
    }
}
