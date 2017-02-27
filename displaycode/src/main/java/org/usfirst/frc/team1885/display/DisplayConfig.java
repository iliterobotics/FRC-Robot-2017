package org.usfirst.frc.team1885.display;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;

public class DisplayConfig {
  public static final Property<String> ROBOT_IP_ADDRESS = new SimpleStringProperty("10.18.85.2");
  
  public static final Property<String> ROBOT_TELEMETRY_TABLE = new SimpleStringProperty("RobotTelemetry");
  
  public static final String NETWORK_TABLES_CONNECTION = "Network Tables Connected";
}
