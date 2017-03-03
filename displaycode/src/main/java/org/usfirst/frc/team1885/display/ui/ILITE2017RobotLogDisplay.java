package org.usfirst.frc.team1885.display.ui;

import org.usfirst.frc.team1885.display.DisplayConfig;
import org.usfirst.frc.team1885.display.data.RobotDataStream;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Font;

public class ILITE2017RobotLogDisplay extends ScrollPane{
  public ILITE2017RobotLogDisplay() {
    final int fontSize = 16;
    
    ListView<String> list = new ListView<>();
    setContent(list);
    getChildren().add(list);
    RobotDataStream.inst().bindListToRobotLog(list);
    setVbarPolicy(ScrollBarPolicy.ALWAYS);
    list.setBackground(DisplayConfig.PANEL_BACKGROUND);
    list.setPrefWidth(5 * DisplayConfig.DEFAULT_TILE_WIDTH + 20);
    list.setPrefHeight(fontSize * DisplayConfig.MAX_ROBOT_LOG_SIZE);
    
    ListCell<String> decorator = new ListCell<String>() {
      @Override 
      protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        setTextFill(DisplayConfig.ILITE_GREEN);
        setText(item);
        setBackground(DisplayConfig.PANEL_BACKGROUND);
        setFont(Font.font(fontSize));
      }
    };
    list.setCellFactory(l -> decorator ); // This effectively is "return decorator"
  }
}
