package org.usfirst.frc.team1885.display;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class DisplayConfig {
  public static final Property<String> ROBOT_IP_ADDRESS = new SimpleStringProperty("10.18.85.2");
  
  public static final Property<String> ROBOT_TELEMETRY_TABLE = new SimpleStringProperty("RobotTelemetry");
  
  public static final String NETWORK_TABLES_CONNECTION = "Network Tables Connected";
  
  public static final double DEFAULT_TILE_WIDTH = 200;
  public static final double DEFAULT_TILE_HEIGHT = 200;
  
  public static final Background PANEL_BACKGROUND = new Background(
    new BackgroundFill(Color.PURPLE, CornerRadii.EMPTY, Insets.EMPTY));

  public static final Color TILE_BACKGROUND = Color.BLACK;

  public static final Color DEFAULT_TILE_TEXT_COLOR = Color.valueOf("#00FF00");
}
