package org.example.firstyearproject;

import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.stage.Stage;

import java.awt.*;
import java.util.*;

import org.example.firstyearproject.DataTypes.Address;
import org.example.firstyearproject.DataTypes.DrawManager;
import org.example.firstyearproject.DataTypes.ZoomGroup;
import org.example.firstyearproject.MapObjects.MapObject;
import org.example.firstyearproject.MapObjects.Node;
import org.example.firstyearproject.MapObjects.Way;
import org.example.firstyearproject.Misc.*;

public class View {
    Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    double resX = screenSize.getWidth();
    double resY = screenSize.getHeight();
    Canvas canvas = new ResizableCanvas(resX/2, resY/2);
    GraphicsContext gc = canvas.getGraphicsContext2D();
    Affine trans = new Affine();
    Button buttonplus, buttonminus, go, carButton, bikeButton, darkModeButton;
    MenuItem load, close, save;
    CheckMenuItem toggleCarGraph,toggleBikeGraph, toggleRoute, toggleColors;
    TextField zoomlevel, graphToggleInfo, routeToggleInfo, bikeGraphToggleInfo;
    ComboBox<String> fromSearchbar, toSearchbar;
    Model model;
    Stage primaryStage;
    boolean updateMinMax;
    float minX,maxX,minY,maxY;
    Scene scene;
    Map<String, Node> pins = new HashMap<>();
    Way route;
    boolean toDrawGraph, toDrawRoute, toDrawBikeGraph, car, bike, darkMode;
    double maxZoom, zoomzoom;
    String url;

    public View(Model model, Stage primaryStage) {
        this.model = model;
        this.primaryStage = primaryStage;
        this.updateMinMax = true;
        this.buttonplus = new Button("+");
        this.buttonminus = new Button("-");
        this.carButton = new Button("Car");
        this.bikeButton = new Button("Bike");
        this.go = new Button("Go");
        this.fromSearchbar = new ComboBox<>();
        this.toSearchbar = new ComboBox<>();
        this.zoomlevel = new TextField();
        this.close = new MenuItem("Exit");
        this.load = new MenuItem("Load new map");
        this.save = new MenuItem("Export to .obj");
        this.toggleBikeGraph = new CheckMenuItem("Show Bike Graph");
        this.toggleRoute = new CheckMenuItem("Hide Route");
        this.toggleCarGraph = new CheckMenuItem("Show Car Graph");
        this.toggleColors = new CheckMenuItem("Toggle Colour Mode");
        this.go = new Button("Go");
        this.toDrawGraph = false;
        this.toDrawRoute = true;
        this.toDrawBikeGraph = false;
        this.car = true;
        this.bike = false;
        this.darkMode = false;

        Settings.iniColor();
        setupUI();
    }

    public void setUrl(String url){
        this.url = url;
    }

    public boolean checkZoomOut(){
        return zoomzoom > maxZoom;

    }

    public boolean checkZoomIn(){
        return true;
        //return zoomzoom < maxZoom*100;
    }

    void redraw() {
        clearCanvas();
        //drawAxis(gc);
        gc.setLineWidth(0.00025);


        drawMap();
        drawGraphs();
        drawRoute();
        drawPins();
        setMaxZoom();
        drawMisc();
    }

    private void drawMap(){
        DrawManager drawManager = new DrawManager();
        if(updateMinMax) updateMinMax();
        Box searchArea = new Box(minX,maxX,minY,maxY);

        int size = ZoomGroup.zoomLevelThreshHolds.length;
        for(int i = 0; i < size; i++){
            if(trans.getMxx()>ZoomGroup.zoomLevelThreshHolds[i])
                drawManager = model.kdTrees.get(i).rangesearch(searchArea,drawManager);
        }
        for(MapObject mo : drawManager) mo.draw(gc);
    }

    private Box createBox(){
        float minX = (float) (  (-trans.getTx()       /   trans.getMxx())   /   Settings.getLonOffset()   );
        float minY = (float) (  (-trans.getTy()       /   trans.getMyy())   /   Settings.getLatOffset()   );
        float maxX = (float) (  ((canvas.getWidth()    /   trans.getMxx())   /   Settings.getLonOffset()) + minX);
        float maxY = (float) (  ((canvas.getHeight()   /   trans.getMyy())   /   Settings.getLatOffset()) + minY);

        if(minX>maxX) {
            float temp = maxX;
            maxX = minX;
            minX = temp;
        }
        if(minY>maxY) {
            float temp = maxY;
            maxY = minY;
            minY = temp;
        }

        return new Box(minX,maxX,minY,maxY);
    }

    private void panToMap(){
        float midLon = model.minlon+(model.maxlon-model.minlon)/2;
        float midLat = model.minlat+(model.maxlat-model.minlat)/2;

        trans.setTx(    -midLon /   trans.getMxx()  *   Settings.getLonOffset());
        trans.setTy(    -midLat /   trans.getMyy()  *   Settings.getLatOffset());

        zoom(0, 0, canvas.getWidth() / (model.maxlon - model.minlon));
        pan(canvas.getWidth()/2,canvas.getHeight()/2);
    }

    private void drawRoute(){
        if (route != null && toDrawRoute){
            if(!route.nodes.isEmpty()){
                route.simpleDraw(gc, route.colorGroup);
            }
        }
    }

    private void drawPins(){
        for(String m: pins.keySet()){
            if(m.equals("from")){
                drawPin(pins.get(m), Color.RED);
            }else if(m.equals("to")){
                drawPin(pins.get(m), Color.GREEN);
            }
        }
    }

    private void drawPin(Node node, Color color){
        if(node == null){
            return;
        }

        double x = node.getLon() * Settings.getLonOffset();
        double y = node.getLat() * Settings.getLatOffset();

        //draw pin
        gc.setLineWidth(0.00005);
        gc.setStroke(Color.BLACK);
        gc.beginPath();
        gc.moveTo(x, y);
        gc.lineTo(x, y-0.0003333333333);
        gc.stroke();

        //draw pinhed
        y -= 0.00006+0.0003333333333;
        gc.setFill(Color.YELLOW);
        gc.fillOval(x-0.0001,y-0.0002, 0.00015,0.00015);
        gc.setLineWidth(0.0001);
        gc.setStroke(color);
        gc.beginPath();
        gc.moveTo(x, y);
        gc.lineTo(x-0.0001, y-0.0001);
        gc.lineTo(x, y-0.0002);
        gc.lineTo(x+0.0001, y-0.0001);
        gc.lineTo(x,y);
        gc.stroke();
    }

    private void drawGraphs(){
        if(toDrawBikeGraph){
            for(Way way: model.cycleEWG.getWays()){
                way.simpleDraw(gc, way.colorGroup);
            }
        }
        if(toDrawGraph){
            for(Way way: model.EWG.getWays()){
                way.simpleDraw(gc, way.colorGroup);
            }
        }
    }

    private void drawMisc() {
        if(!updateMinMax) {
            drawBox(minX, minY, maxX, maxY);
        }
    }

    private void clearCanvas() {
        gc.setTransform(new Affine());
        //gc.setFill(Settings.getColor(ColorGroup.WATER_GENERIC));
        if (darkMode) gc.setFill(Color.web("1D4DA7"));
        else gc.setFill(Color.LIGHTBLUE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setTransform(trans);
        //gc.setLineWidth(0.00005);
    }

    private void setMaxZoom(){
        if(this.maxZoom == 0.0 || this.maxZoom == 1.0){
            this.maxZoom = trans.getMxx();
        }
    }


    private void setupUI(){
        primaryStage.setTitle("Map Drawer");

        StackPane pane = new StackPane();
        HBox topbar = new HBox();
        HBox bottombar = new HBox();
        MenuBar navbar = new MenuBar();
        BorderPane map = new BorderPane(canvas);
        BorderPane overlay = new BorderPane();

        HBox choiceBox = createChoiceBox();
        VBox searchbars = createSearchBars(choiceBox);
        VBox zoombuttons = createZoomButtons();
        VBox zoomLevelBox = createZoomLevel();
        createFileButton(navbar);
        createSettingsButton(navbar);

        topbar.getChildren().addAll(navbar);
        bottombar.getChildren().addAll(zoombuttons, zoomLevelBox, searchbars);

        overlay.setTop(topbar);
        overlay.setBottom(bottombar);
        overlay.setPickOnBounds(false);
        pane.getChildren().addAll(map,overlay);
        pane.setPickOnBounds(true);

        this.scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.show();

        redraw();
        panToMap();
    }

    private HBox createChoiceBox(){
        HBox box = new HBox();
        box.getChildren().addAll(carButton, bikeButton);
        return box;
    }


    private void createFileButton(MenuBar navbar){
        Menu file = new Menu("File");
        file.getItems().addAll(load, save, close);
        navbar.getMenus().addAll(file);
    }

    private void createSettingsButton(MenuBar navbar){
        Menu settings = new Menu("Settings");
        settings.getItems().addAll(toggleCarGraph, toggleBikeGraph, toggleRoute, toggleColors);
        navbar.getMenus().add(settings);
    }

    private VBox createZoomButtons(){
        buttonplus.setPrefWidth(35);
        buttonminus.setPrefWidth(35);

        TextField filler = new TextField();
        filler.setDisable(true);
        filler.setPrefWidth(buttonminus.getWidth());

        VBox zoombuttons = new VBox();
        zoombuttons.getChildren().addAll(filler, buttonplus, buttonminus);
        return zoombuttons;
    }

    private VBox createSearchBars(HBox choiceBox){
        VBox searchbars = new VBox();

        fromSearchbar.setEditable(true);
        toSearchbar.setEditable(true);

        fromSearchbar.setPromptText("From...");
        toSearchbar.setPromptText("To...");

        fromSearchbar.setPrefWidth(300);
        toSearchbar.setPrefWidth(300);

        ArrayList<String> addrNames = new ArrayList<>();
        HashMap<String, Address> name2addr = new HashMap<>();
        SortAddress(addrNames,name2addr);

        fromSearchbar.setItems(FXCollections.observableList(addrNames));
        toSearchbar.setItems(FXCollections.observableList(addrNames));

        new AutoCompleteComboBoxListener<>(fromSearchbar);
        new AutoCompleteComboBoxListener<>(toSearchbar);

        searchbars.getChildren().addAll(choiceBox, fromSearchbar, toSearchbar);

        return searchbars;
    }

    private VBox createZoomLevel(){
        VBox zoomlevelfield = new VBox();

        TextField filler2 = new TextField();
        filler2.setDisable(true);
        filler2.setPrefWidth(zoomlevel.getWidth());

        TextField filler = new TextField("Zoom Level:");
        filler.setEditable(false);

        zoomlevelfield.getChildren().addAll(filler2, filler, zoomlevel);

        zoomlevel.setEditable(false);
        zoomlevel.setMouseTransparent(true);
        zoomlevel.setPrefWidth(70);
        zoomlevel.setText("0.0");

        return zoomlevelfield;
    }

    void drawBox(float minX,float minY,float maxX,float maxY) {
        gc.setLineWidth(5/trans.getMxx());
        minX *= Settings.getLonOffset();
        maxX *= Settings.getLonOffset();
        minY *= Settings.getLatOffset();
        maxY *= Settings.getLatOffset();


        //gc.setLineWidth(1);
        //System.out.println("Attempting to draw box");
        gc.setStroke(Color.BLACK);
        gc.beginPath();
        gc.moveTo(minX, minY);
        gc.lineTo(maxX, minY);
        gc.lineTo(maxX, maxY);
        gc.lineTo(minX, maxY);
        gc.lineTo(minX, minY);
        gc.stroke();
    }

    public void toggleMinMax() {
        if(updateMinMax)    updateMinMax = false;
        else                updateMinMax = true;
    }

    public float screenToCoordinate(double arg,boolean isLon) {
        if(isLon)   return (float) ((arg/trans.getMxx())/Settings.getLonOffset());
        else        return (float) ((arg/trans.getMxx())/Settings.getLatOffset());
    }

    public float realCoordinateToShown(float arg, boolean isLon){
        if(isLon)   return arg*Settings.getLonOffset();
        else        return arg*Settings.getLatOffset();
    }

    public void updateMinMax() {
        minX = (float) screenToCoordinate(-trans.getTx(),true);
        minY = (float) screenToCoordinate(-trans.getTy(),false);
        maxX = (float) screenToCoordinate(canvas.getWidth(),true) + minX;
        maxY = (float) screenToCoordinate(canvas.getHeight(),false) + minY;

        if(minX>maxX) {
            float temp = maxX;
            maxX = minX;
            minX = temp;
        }
        if(minY>maxY) {
            float temp = maxY;
            maxY = minY;
            minY = temp;
        }
    }

    private void drawAxis(GraphicsContext gc) {
        gc.setLineWidth(5/trans.getMxx());

        gc.setStroke(Color.BLACK);
        gc.beginPath();
        gc.moveTo(0, 0);
        gc.lineTo(model.maxlon*Settings.getLonOffset(), 0);
        gc.stroke();

        gc.beginPath();
        gc.moveTo(0, 0);
        gc.lineTo(0, model.maxlat*Settings.getLatOffset());
        gc.stroke();

        // test
         /*
        double x = (canvas.getWidth()/2) / trans.getMxx() + (trans.getTx()*-1/trans.getMxx());
        double y = (canvas.getHeight()/2) / trans.getMyy() + (trans.getTy()*-1/trans.getMyy());
        gc.moveTo(x, y);
        gc.lineTo(0, 0);
        gc.stroke();*/
    }

    void pan(double dx, double dy) {
        trans.prependTranslation(dx, dy);
        redraw();
    }


    void zoom(double dx, double dy, double factor) {
        pan(-dx, -dy);
        trans.prependScale(factor, factor);
        pan(dx, dy);
        redraw();
    }

    void SortAddress(ArrayList<String> addrNames, HashMap<String, Address> name2addr){
        for(int i =1;i<Model.AddressList.size();i++ ){
            for(int k = i; k > 0 && Address.isGreater(Model.AddressList.get(k-1),Model.AddressList.get(k)); k--){
                Collections.swap(Model.AddressList, k-1, k);
            }
        }
        for(Address address : Model.AddressList){
            addrNames.add(address.toString());
            name2addr.put(address.toString(),address);
        }
    }
}