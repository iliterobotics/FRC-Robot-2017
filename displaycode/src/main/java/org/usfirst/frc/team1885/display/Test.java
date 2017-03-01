package org.usfirst.frc.team1885.display;

import eu.hansolo.tilesfx.Tile;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class Test extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) throws Exception {
    
    BorderPane pane = new BorderPane();
    pane.setPadding(new Insets(10));
    pane.setCenterShape(true);
    pane.setPrefSize(1270, 800);
    pane.setBackground(new Background(new BackgroundFill(Tile.BACKGROUND.darker(), CornerRadii.EMPTY, Insets.EMPTY)));
    pane.setCenter(new ILITE2017DataDisplay().getDisplayComponent());
    
    Scene scene = new Scene(pane);
    stage.setTitle("ILITE Dashboard");
    stage.setScene(scene);
    stage.show();
  }

}
