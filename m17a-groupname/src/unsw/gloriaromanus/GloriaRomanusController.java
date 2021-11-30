package unsw.gloriaromanus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.FeatureTable;
import com.esri.arcgisruntime.data.GeoPackage;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.IdentifyLayerResult;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol.HorizontalAlignment;
import com.esri.arcgisruntime.symbology.TextSymbol.VerticalAlignment;
import com.esri.arcgisruntime.data.Feature;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.geojson.FeatureCollection;
import org.geojson.LngLatAlt;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.util.Pair;

public class GloriaRomanusController implements Battle_observer {

  @FXML
  private MapView mapView;

  @FXML
  private StackPane stackPaneMain;

  // could use ControllerFactory?
  private ArrayList<Pair<MenuController, VBox>> controllerParentPairs;
  private HashMap<String, Integer> fxmlIndex;
  private String currFXML;

  private ArcGISMap map;

  private Map<String, Integer> provinceToNumberTroopsMap;

  private String humanFaction;

  private Feature currentlySelectedHumanProvince;
  private Feature currentlySelectedEnemyProvince;

  private FeatureLayer featureLayer_provinces;

  private GloriaRomanus game;
  private saveAndLoad game_save;

  @FXML
  private TextField treasury;

  @FXML
  private CheckBox treasuryGoal;

  @FXML
  private CheckBox conquestGoal;

  @FXML
  private CheckBox wealthGoal;

  @FXML
  private CheckBox twoPlayers;

  @FXML
  private CheckBox threePlayers;

  @FXML
  private CheckBox fourPlayers;

  @FXML
  private ComboBox<String> savedgames;

  @FXML
  private Label Victroy_text;

  @FXML
  private void initialize() throws JsonParseException, JsonMappingException, IOException, InterruptedException {
    game = new GloriaRomanus(2);

    provinceToNumberTroopsMap = new HashMap<String, Integer>();
    for (String provinceName : game.getProvinceOwning().keySet()) {
      provinceToNumberTroopsMap.put(provinceName, 0);
    }

    humanFaction = game.getCurrFaction().getName();
    currentlySelectedHumanProvince = null;
    currentlySelectedEnemyProvince = null;

    fxmlIndex = new HashMap<String, Integer>();

    controllerParentPairs = new ArrayList<Pair<MenuController, VBox>>();
    game_save = new saveAndLoad();

  }

  public void clickedInvadeButton(ActionEvent e) throws IOException {
    if (currentlySelectedHumanProvince != null && currentlySelectedEnemyProvince != null) {
      String humanProvince = (String) currentlySelectedHumanProvince.getAttributes().get("name");
      String enemyProvince = (String) currentlySelectedEnemyProvince.getAttributes().get("name");
      if (game.isConnected(humanProvince, enemyProvince)) {
        Province p1 = game.getProvinceOfAllFaction(humanProvince);
        Province p2 = game.getProvinceOfAllFaction(enemyProvince);

        Battle_resolver b = new Battle_resolver(p1, p2);
        b.attach(this);
        b.startSkirmish();

        provinceToNumberTroopsMap.put(humanProvince, p1.getNumofTroops());
        provinceToNumberTroopsMap.put(enemyProvince, p2.getNumofTroops());
        if (p2.getFaction().getName().equals(p1.getFaction().getName())) {
          game.getProvinceOwning().put(enemyProvince, humanFaction);
        }

        resetSelections(); // reset selections in UI
        addAllPointGraphics(); // reset graphics
      } else {
        printMessageToTerminal("Provinces not adjacent, cannot invade!");
      }

    }
  }

  public void resetInvadeMenuOutput() {
    int currIndex = getFXMLIndex(currFXML);
    if (controllerParentPairs.get(currIndex).getKey() instanceof InvasionMenuController) {
      ((InvasionMenuController) controllerParentPairs.get(currIndex).getKey()).resetTerminal();
    }
  }

  public void EndTurn(ActionEvent e) throws JsonParseException, JsonMappingException, IOException {
    if (game.checkVictory()){
      changeMenu(e, "victory_menu.fxml");
      Victroy_text.setText(game.getCurrFaction().getName() + " Faction Win!");
      terminate();
      return;
    }
    game.endTurn();
    humanFaction = game.getCurrFaction().getName();
    resetSelections();
    updateTreasury();
    displayInfo("");
    displayInfrastructure("");
    displayTrain("");
    updateTroopNum();
    setFaction();
  }

  /**
   * run this initially to update province owner, change feature in each
   * FeatureLayer to be visible/invisible depending on owner. Can also update
   * graphics initially
   */
  private void initializeProvinceLayers() throws JsonParseException, JsonMappingException, IOException {

    Basemap myBasemap = Basemap.createImagery();
    // myBasemap.getReferenceLayers().remove(0);
    map = new ArcGISMap(myBasemap);
    mapView.setMap(map);

    // note - tried having different FeatureLayers for AI and human provinces to
    // allow different selection colors, but deprecated setSelectionColor method
    // does nothing
    // so forced to only have 1 selection color (unless construct graphics overlays
    // to give color highlighting)
    GeoPackage gpkg_provinces = new GeoPackage("src/unsw/gloriaromanus/provinces_right_hand_fixed.gpkg");
    gpkg_provinces.loadAsync();
    gpkg_provinces.addDoneLoadingListener(() -> {
      if (gpkg_provinces.getLoadStatus() == LoadStatus.LOADED) {
        // create province border feature
        featureLayer_provinces = createFeatureLayer(gpkg_provinces);
        map.getOperationalLayers().add(featureLayer_provinces);

      } else {
        System.out.println("load failure");
      }
    });

    addAllPointGraphics();
  }

  private void addAllPointGraphics() throws JsonParseException, JsonMappingException, IOException {
    mapView.getGraphicsOverlays().clear();

    InputStream inputStream = new FileInputStream(new File("src/unsw/gloriaromanus/provinces_label.geojson"));
    FeatureCollection fc = new ObjectMapper().readValue(inputStream, FeatureCollection.class);

    GraphicsOverlay graphicsOverlay = new GraphicsOverlay();

    for (org.geojson.Feature f : fc.getFeatures()) {
      if (f.getGeometry() instanceof org.geojson.Point) {
        org.geojson.Point p = (org.geojson.Point) f.getGeometry();
        LngLatAlt coor = p.getCoordinates();
        Point curPoint = new Point(coor.getLongitude(), coor.getLatitude(), SpatialReferences.getWgs84());
        PictureMarkerSymbol s = null;
        String province = (String) f.getProperty("name");
        String faction = game.getProvinceOwning().get(province);

        TextSymbol t = new TextSymbol(10, faction + "\n" + province + "\n" + provinceToNumberTroopsMap.get(province),
            0xFFFF0000, HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM);

        switch (faction) {
          case "Gaul":
            // note can instantiate a PictureMarkerSymbol using the JavaFX Image class - so
            // could
            // construct it with custom-produced BufferedImages stored in Ram
            // http://jens-na.github.io/2013/11/06/java-how-to-concat-buffered-images/
            // then you could convert it to JavaFX image
            // https://stackoverflow.com/a/30970114

            // you can pass in a filename to create a PictureMarkerSymbol...
            s = new PictureMarkerSymbol(new Image((new File("images/Celtic_Druid.png")).toURI().toString()));
            break;
          case "Rome":
            // you can also pass in a javafx Image to create a PictureMarkerSymbol
            // (different to BufferedImage)
            s = new PictureMarkerSymbol("images/legionary.png");
            break;
          case "Spanish":
            s = new PictureMarkerSymbol(new Image((new File("images/gold_pile.png")).toURI().toString()));
            break;
          case "Italia":
            Image tmp = new Image((new File("images/CS2511Sprites_No_Background/Camel/Alone/CamelAlone_NB.png")).toURI().toString(), 40, 40, false, false);
            s = new PictureMarkerSymbol(tmp);
            break;
          // TODO = handle all faction names, and find a better structure...
        }
        t.setHaloColor(0xFFFFFFFF);
        t.setHaloWidth(2);
        Graphic gPic = new Graphic(curPoint, s);
        Graphic gText = new Graphic(curPoint, t);
        graphicsOverlay.getGraphics().add(gPic);
        graphicsOverlay.getGraphics().add(gText);
      } else {
        System.out.println("Non-point geo json object in file");
      }

    }

    inputStream.close();
    mapView.getGraphicsOverlays().add(graphicsOverlay);
  }

  private FeatureLayer createFeatureLayer(GeoPackage gpkg_provinces) {
    FeatureTable geoPackageTable_provinces = gpkg_provinces.getGeoPackageFeatureTables().get(0);

    // Make sure a feature table was found in the package
    if (geoPackageTable_provinces == null) {
      System.out.println("no geoPackageTable found");
      return null;
    }

    // Create a layer to show the feature table
    FeatureLayer flp = new FeatureLayer(geoPackageTable_provinces);

    // https://developers.arcgis.com/java/latest/guide/identify-features.htm
    // listen to the mouse clicked event on the map view
    mapView.setOnMouseClicked(e -> {
      // was the main button pressed?
      if (e.getButton() == MouseButton.PRIMARY) {
        // get the screen point where the user clicked or tapped
        Point2D screenPoint = new Point2D(e.getX(), e.getY());

        // specifying the layer to identify, where to identify, tolerance around point,
        // to return pop-ups only, and
        // maximum results
        // note - if select right on border, even with 0 tolerance, can select multiple
        // features - so have to check length of result when handling it
        final ListenableFuture<IdentifyLayerResult> identifyFuture = mapView.identifyLayerAsync(flp, screenPoint, 0,
            false, 25);

        // add a listener to the future
        identifyFuture.addDoneListener(() -> {
          try {
            // get the identify results from the future - returns when the operation is
            // complete
            IdentifyLayerResult identifyLayerResult = identifyFuture.get();
            // a reference to the feature layer can be used, for example, to select
            // identified features
            if (identifyLayerResult.getLayerContent() instanceof FeatureLayer) {
              FeatureLayer featureLayer = (FeatureLayer) identifyLayerResult.getLayerContent();
              // select all features that were identified
              List<Feature> features = identifyLayerResult.getElements().stream().map(f -> (Feature) f)
                  .collect(Collectors.toList());

              if (features.size() > 1) {
                printMessageToTerminal("Have more than 1 element - you might have clicked on boundary!");
              } else if (features.size() == 1) {
                // note maybe best to track whether selected...
                Feature f = features.get(0);
                String province = (String) f.getAttributes().get("name");
                int currIndex = getFXMLIndex(currFXML);
                if (game.getProvinceOwning().get(province).equals(humanFaction)) {
                  // province owned by human
                  if (currentlySelectedHumanProvince != null) {
                    featureLayer.unselectFeature(currentlySelectedHumanProvince);
                  }
                  currentlySelectedHumanProvince = f;
                  displayInfrastructure(game.getInfra(province));
                  displayInfo(game.getInfo(province));
                  displayTrain(game.getTrain(province));
                  displaySoldier();
                  if (controllerParentPairs.get(currIndex).getKey() instanceof InvasionMenuController) {
                    ((InvasionMenuController) controllerParentPairs.get(currIndex).getKey())
                        .setInvadingProvince(province);
                  }

                } else {
                  if (currentlySelectedEnemyProvince != null) {
                    featureLayer.unselectFeature(currentlySelectedEnemyProvince);
                  }
                  currentlySelectedEnemyProvince = f;
                  if (controllerParentPairs.get(currIndex).getKey() instanceof InvasionMenuController) {
                    ((InvasionMenuController) controllerParentPairs.get(currIndex).getKey())
                        .setOpponentProvince(province);
                  }
                }

                featureLayer.selectFeature(f);
              }

            }
          } catch (InterruptedException | ExecutionException | FileNotFoundException ex) {
            // ... must deal with checked exceptions thrown from the async identify
            // operation
            System.out.println("InterruptedException occurred");
          }
        });
      }
    });
    return flp;
  }

  private ArrayList<String> getHumanProvincesList() throws IOException {
    // https://developers.arcgis.com/labs/java/query-a-feature-layer/

    String content = null;
    if (twoPlayers.isSelected()) {
      content = Files.readString(Paths.get("src/unsw/gloriaromanus/initial_province_ownership.json"));
    } else if (threePlayers.isSelected()) {
      content = Files.readString(Paths.get("src/unsw/gloriaromanus/3_player_provinces.json"));
    } else if (fourPlayers.isSelected()) {
      content = Files.readString(Paths.get("src/unsw/gloriaromanus/4_player_provinces.json"));
    }

    JSONObject ownership = new JSONObject(content);
    return ArrayUtil.convert(ownership.getJSONArray(humanFaction));
  }

  /**
   * returns query for arcgis to get features representing human provinces can
   * apply this to FeatureTable.queryFeaturesAsync() pass string to
   * QueryParameters.setWhereClause() as the query string
   */
  private String getHumanProvincesQuery() throws IOException {
    LinkedList<String> l = new LinkedList<String>();
    for (String hp : getHumanProvincesList()) {
      l.add("name='" + hp + "'");
    }
    return "(" + String.join(" OR ", l) + ")";
  }

  // private boolean confirmIfProvincesConnected(String province1, String
  // province2) throws IOException {
  // String content =
  // Files.readString(Paths.get("src/unsw/gloriaromanus/province_adjacency_matrix_fully_connected.json"));
  // JSONObject provinceAdjacencyMatrix = new JSONObject(content);
  // return
  // provinceAdjacencyMatrix.getJSONObject(province1).getBoolean(province2);
  // }

  private void resetSelections() {
    List<Feature> selecteds = new ArrayList<>();
    if (currentlySelectedEnemyProvince != null)
      selecteds.add(currentlySelectedEnemyProvince);
    if (currentlySelectedHumanProvince != null)
      selecteds.add(currentlySelectedHumanProvince);
    featureLayer_provinces.unselectFeatures(selecteds);
    currentlySelectedEnemyProvince = null;
    currentlySelectedHumanProvince = null;
    int currIndex = getFXMLIndex(currFXML);
    if (controllerParentPairs.get(currIndex).getKey() instanceof InvasionMenuController) {
      ((InvasionMenuController) controllerParentPairs.get(currIndex).getKey()).setInvadingProvince("");
      ((InvasionMenuController) controllerParentPairs.get(currIndex).getKey()).setOpponentProvince("");
    }
  }

  private void printMessageToTerminal(String message) {
    int currIndex = getFXMLIndex(currFXML);
    if (controllerParentPairs.get(currIndex).getKey() instanceof InvasionMenuController) {
      ((InvasionMenuController) controllerParentPairs.get(currIndex).getKey()).appendToTerminal(message);
    }
  }

  private void displayInfo(String message) {
    int currIndex = getFXMLIndex(currFXML);
    if (controllerParentPairs.get(currIndex).getKey() instanceof TaxMenuController) {
      ((TaxMenuController) controllerParentPairs.get(currIndex).getKey()).appendToTerminal(message);
    }
  }

  /**
   * Stops and releases all resources used in application.
   */
  void terminate() {

    if (mapView != null) {
      mapView.dispose();
    }
  }

  public void displayInfoFromBasicMenu() {
    if (currentlySelectedHumanProvince != null) {
      String province = (String) currentlySelectedHumanProvince.getAttributes().get("name");
      displayInfo(game.getInfo(province));
    }

  }

  public void displayTrainFromBasicMenu() {
    if (currentlySelectedHumanProvince != null) {
      String province = (String) currentlySelectedHumanProvince.getAttributes().get("name");
      displayTrain(game.getTrain(province));
    }

  }

  public void displayInfrastructureFromBasicMenu() {
    if (currentlySelectedHumanProvince != null) {
      String province = (String) currentlySelectedHumanProvince.getAttributes().get("name");
      displayInfrastructure(game.getInfra(province));
    }

  }

  public void displayInfrastructure(String message) {
    int currIndex = getFXMLIndex(currFXML);
    if (controllerParentPairs.get(currIndex).getKey() instanceof InfrastructureMenuController) {
      ((InfrastructureMenuController) controllerParentPairs.get(currIndex).getKey()).appendToTerminal(message);
    }
  }

  public void displayTrain(String message) {
    int currIndex = getFXMLIndex(currFXML);
    if (controllerParentPairs.get(currIndex).getKey() instanceof TrainMenuController) {
      ((TrainMenuController) controllerParentPairs.get(currIndex).getKey()).setMessage(message);
    }
  }

  public void switchMenu(String dest) {
    stackPaneMain.getChildren().remove(controllerParentPairs.get(getFXMLIndex(currFXML)).getValue());
    stackPaneMain.getChildren().add(controllerParentPairs.get(getFXMLIndex(dest)).getValue());
    currFXML = dest;
    updateTreasury();
  }

  public void updateTreasury() {
    ((BasicMenu2Controller) controllerParentPairs.get(getFXMLIndex("basic_menu2.fxml")).getKey())
        .setTreasury(game.getCurrFaction().getTreasury());
  }

  /**
   * 
   * @param FXML file name
   * @return index of the FXML file in arraylist fxmlIndex
   */
  public int getFXMLIndex(String name) {
    return fxmlIndex.get(name);
  }

  public void setTax(String tax) {
    if (currentlySelectedHumanProvince != null) {
      String name = (String) currentlySelectedHumanProvince.getAttributes().get("name");
      game.getProvince(name).changeTaxRate(tax);
      displayInfo(game.getInfo(name));
    }
  }

  public void build(String production) {
    if (currentlySelectedHumanProvince != null) {
      String name = (String) currentlySelectedHumanProvince.getAttributes().get("name");
      game.build(name, production);
      updateTreasury();
      displayInfrastructure(game.getProvince(name).getInfra());
    }
  }

  public void upgrade(String production) {
    if (currentlySelectedHumanProvince != null) {
      String name = (String) currentlySelectedHumanProvince.getAttributes().get("name");
      game.upgrade(name, production);
      updateTreasury();
      displayInfrastructure(game.getProvince(name).getInfra());
    }
  }

  public void cancelInfra() {
    if (currentlySelectedHumanProvince != null) {
      String name = (String) currentlySelectedHumanProvince.getAttributes().get("name");
      game.getProvince(name).cancel();
      updateTreasury();
      displayInfrastructure(game.getProvince(name).getInfra());
    }
  }

  public void cancelTrain(int index) {
    if (currentlySelectedHumanProvince != null) {
      String name = (String) currentlySelectedHumanProvince.getAttributes().get("name");
      game.getProvince(name).getTroopProductionBuilding().cancel(index);
      ;
      updateTreasury();
      displayTrain(game.getProvince(name).getTrain());
    }
  }

  public void train(String type) throws IOException {
    if (currentlySelectedHumanProvince != null) {
      String name = (String) currentlySelectedHumanProvince.getAttributes().get("name");
      game.train(name, type);
      displayTrain(game.getProvince(name).getTrain());
    }
  }

  public void updateTroopNum() throws JsonParseException, JsonMappingException, IOException {
    for (String provinceName : game.getProvinceOwning().keySet()) {
      provinceToNumberTroopsMap.put(provinceName, game.getFromAllProvince(provinceName).getNumofTroops());
    }
    addAllPointGraphics();
  }

  public List<Province> getProvinces() {
    return game.getCurrFaction().getProvinces();
  }

  public void displaySoldier() throws FileNotFoundException {
    if (currentlySelectedHumanProvince != null) {
      String humanProvince = (String)currentlySelectedHumanProvince.getAttributes().get("name");
      int currIndex = getFXMLIndex(currFXML);
      if (controllerParentPairs.get(currIndex).getKey() instanceof SoldierMenuController) {
        ((SoldierMenuController)controllerParentPairs.get(currIndex).getKey()).showSoldiers(game.getFromAllProvince(humanProvince).getSoldiers());
      }

    }
  }

  public void setFaction() {
    ((BasicMenu2Controller)controllerParentPairs.get(getFXMLIndex("basic_menu2.fxml")).getKey()).setFaction(game.getCurrFaction().getName());
  }

  @FXML
  public void unselectCheckBox(ActionEvent e) {
    String tmp = ((CheckBox) e.getSource()).getText();
    if (tmp.equals("Two players")){
      threePlayers.setSelected(false); 
      fourPlayers.setSelected(false); 
    }
    if (tmp.equals("Three players")){
      twoPlayers.setSelected(false); 
      fourPlayers.setSelected(false); 
    }
    if (tmp.equals("Four players")){
      twoPlayers.setSelected(false); 
      threePlayers.setSelected(false); 
    }
    
  }

  @FXML
  public void StartGameFromSetGoal(ActionEvent e) throws JsonParseException, JsonMappingException, IOException, InterruptedException {
    if (!treasuryGoal.isSelected() && ! conquestGoal.isSelected() && !wealthGoal.isSelected()){
      System.out.println("haven't select goal!");
      return;
    }
    if (!twoPlayers.isSelected() && ! threePlayers.isSelected() && !fourPlayers.isSelected()){
      System.out.println("haven't select number of players!");
      return;
    }

    String res_string = "";
    if (treasuryGoal.isSelected()){
      res_string += " TREASURY " ;
    }
    if (conquestGoal.isSelected()){
      res_string += " CONQUEST ";
    }
    if (wealthGoal.isSelected()){
      res_string += " WEALTH " ;
    }
    

    StartGame(e);


    
    if (twoPlayers.isSelected()){
      game = new GloriaRomanus(2);
    } else if (threePlayers.isSelected()){
      game = new GloriaRomanus(3);
    } else if (fourPlayers.isSelected()){
      game = new GloriaRomanus(4);
    }

    game.setGoal(res_string);
    updateTreasury();
    updateTroopNum();
    setFaction();
    initializeProvinceLayers();
  }

  public void StartGame(ActionEvent e) throws IOException {
    changeMenu(e, "main.fxml");

    FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));

    String []menus = {"basic_menu.fxml", "invasion_menu.fxml", "tax_menu.fxml", "basic_menu2.fxml", "train_menu.fxml", "Infrastructure_menu.fxml", "soldier_menu.fxml","save_menu.fxml"};
    controllerParentPairs = new ArrayList<Pair<MenuController, VBox>>();
    int ct = 0;
    for (String fxmlName: menus){
      fxmlIndex.put(fxmlName, ct);
      ct++;
      System.out.println(fxmlName);
      loader = new FXMLLoader(getClass().getResource(fxmlName));
      VBox root = (VBox)loader.load();
      MenuController menuController = (MenuController)loader.getController();
      menuController.setParent(this);
      controllerParentPairs.add(new Pair<MenuController, VBox>(menuController, root));
    }
    currFXML = "basic_menu.fxml";
    stackPaneMain.getChildren().add(controllerParentPairs.get(getFXMLIndex(currFXML)).getValue());
    stackPaneMain.getChildren().add(controllerParentPairs.get(getFXMLIndex("basic_menu2.fxml")).getValue());
  }


  @FXML
  public void changeToSteGoalMenu(ActionEvent e) throws IOException {
    changeMenu(e, "SetGoalMenu.fxml");
  }


  @FXML
  public void changeToLoadGameMenu(ActionEvent e) throws IOException {
    changeMenu(e, "LoadGameMenu.fxml");
    File[] saved_games =  new File("src/savedGame").listFiles();
    for (File file : saved_games) {
      savedgames.getItems().add(file.getName());
    }

  }

  @FXML
  public void LoadGame(ActionEvent e) throws IOException, ClassNotFoundException {
    if (savedgames.getValue() == null){
      System.out.println("Haven't select game to load");
      return;
    }
    String filename = savedgames.getValue();
    StartGame(e);
    this.game = game_save.loadGame(filename);
    updateTreasury();
    updateTroopNum();
    setFaction();
    initializeProvinceLayers();
    
  }

  @FXML
  public void changeToStartGameMenu(ActionEvent e) throws IOException {
    changeMenu(e, "StartMenu.fxml");

  }


  public void changeMenu(ActionEvent e,String menu) throws IOException  {
    Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();

    FXMLLoader loader = new FXMLLoader(getClass().getResource(menu));
    loader.setController(this);
    Parent root = loader.load();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  public void clickedSaveButton(ActionEvent e, String savename) throws ClassNotFoundException, IOException {
    game_save.saveGame(savename, game);
  }


  @Override
  public void update(String message) {
    printMessageToTerminal(message);
  }



}

