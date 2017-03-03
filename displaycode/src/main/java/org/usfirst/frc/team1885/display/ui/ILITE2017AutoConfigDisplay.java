package org.usfirst.frc.team1885.display.ui;

import org.usfirst.frc.team1885.display.DisplayConfig;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.Tile.SkinType;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class ILITE2017AutoConfigDisplay extends AbstractTilePane{

  public ILITE2017AutoConfigDisplay() {
    Text title = new Text("  Autonomous  \nConfiguration");
    title.setFont(Font.font(null, FontWeight.BOLD, 20));
    title.setTextAlignment(TextAlignment.CENTER);
    title.setFill(DisplayConfig.DEFAULT_TILE_TEXT_COLOR);
    getChildren().add(title);
    
    Tile doAnythingYesNo = tile("Do Anything?", SkinType.SWITCH)
      .selected(true)
      .text("Yes")
      .textVisible(true)
      .build();
    getChildren().add(doAnythingYesNo);
    // TODO - send to robot
    
    Tile autonDelay = tile("Delay", SkinType.SLIDER)
      .minValue(0d)
      .maxValue(7d)
      .increment(0.5d)
      .unit("s")
      .build();
    getChildren().add(autonDelay);
    
    Tile autonGearYesNo = tile("Place Gear?", SkinType.SWITCH)
      .selected(true)
      .text("Yes")
      .textVisible(true)
      .build();
    getChildren().add(autonGearYesNo);
    // TODO - send to robot
    
    Tile autonModeConfig = tile("Side of Airship", SkinType.SWITCH)
      .selected(false)
      .text("Left Side")
      .textVisible(true)
      .build();
    autonModeConfig.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
      if(isSelected) {
        autonModeConfig.setText("Right Side");
      } else {
        autonModeConfig.setText("Left Side");
      }
    });
    getChildren().add(autonModeConfig);
    // TODO - bind property in robot data sender

    Tile autonDistanceConfig = tile("Extra Travel", SkinType.PLUS_MINUS)
      .unit("ft")
      .increment(1)
      .value(0)
      .minValue(0)
      .maxValue(15)
      .text("Distance after baseline")
      .textVisible(true)
      .build();
    getChildren().add(autonDistanceConfig);
    //TODO bind property in a robot data sender to the value property of this
  }
}
