package org.usfirst.frc.team1885.display;

import java.util.HashMap;
import java.util.Map;

import org.usfirst.frc.team1885.display.data.RobotDataStream;
import org.usfirst.frc.team1885.display.ui.AbstractTilePane;
import org.usfirst.frc.team1885.display.ui.ILITE2017AutoConfigDisplay;
import org.usfirst.frc.team1885.display.ui.ILITE2017DataDisplay;
import org.usfirst.frc.team1885.display.ui.ILITE2017RobotLogDisplay;
import org.usfirst.frc.team1885.display.ui.ILITE2017RobotStatusDisplay;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Test extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) throws Exception {
    
    BorderPane pane = new BorderPane();
    pane.setCenterShape(true);
    pane.setPrefSize(1270, 800);
    pane.setTop(new ILITE2017AutoConfigDisplay());
    pane.setBottom(new ILITE2017RobotStatusDisplay());
    
    StackPane center = new StackPane();
    pane.setCenter(center);
    
    // Using a map of integers to keep a proper order
    Map<Integer, Region> panes = new HashMap<>();
    Map<Integer, Button> buttons = new HashMap<>();
    
    buttons.put(0, new Button("Robot Telemetry"));
    panes.put(0, new ILITE2017DataDisplay());
    
    buttons.put(1, new Button("Log (testing)"));
    panes.put(1, new ILITE2017RobotLogDisplay());
    
    VBox selectorPane = new VBox();
    AbstractTilePane.decorate(selectorPane);
    selectorPane.setSpacing(20d);
    pane.setRight(selectorPane);

    // This sets up the Stack Pane 'on top' functionality.
    for(int i = 0; i < panes.size(); i++) {
      Button b = buttons.get(i);
      Region p = panes.get(i);
      b.setPrefWidth(DisplayConfig.DEFAULT_TILE_WIDTH-10);
      b.setFont(Font.font(null, FontWeight.BOLD, 20d));
      b.setStyle("-fx-border-color: #00FF00");
      selectorPane.getChildren().add(b);
      center.getChildren().add(p);
      AbstractTilePane.decorate(p);
      b.setOnAction(event -> {
        panes.values().forEach(pp -> pp.setVisible(false));
        buttons.values().forEach(bu -> {
          bu.setTextFill(DisplayConfig.ILITE_GREEN);
          bu.setBackground(DisplayConfig.PANEL_BACKGROUND);
        });
        b.setTextFill(DisplayConfig.ILITE_PURPLE);
        b.setBackground(DisplayConfig.ILITE_GREEN_BACKGROUND);
        p.setVisible(true);
      });
    }
    buttons.get(0).fire();
    
    Scene scene = new Scene(pane);
    stage.setTitle("ILITE Dashboard");
    stage.setScene(scene);
    stage.show();
    
    stage.setOnCloseRequest(e -> {
      System.out.println("Goodbye!");
      Platform.exit();
      System.exit(0);
    });
    
    RobotDataStream.inst().log("TEST DATA");
    RobotDataStream.inst().log("TEST DATA");
    RobotDataStream.inst().log("TEST DATA");
    RobotDataStream.inst().log("TEST DATA");
  }

}
