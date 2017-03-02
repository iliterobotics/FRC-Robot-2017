package org.usfirst.frc.team1885.display.ui;

import org.usfirst.frc.team1885.display.DisplayConfig;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.Tile.SkinType;
import eu.hansolo.tilesfx.events.TileEvent;
import eu.hansolo.tilesfx.events.TileEventListener;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ILITE2017AutoConfigDisplay extends AbstractTilePane{

  public ILITE2017AutoConfigDisplay() {
    Label title = new Label("Autonomous Configuration");
    title.setFont(Font.font(null, FontWeight.BOLD, 20));
    title.setTextFill(DisplayConfig.DEFAULT_TILE_TEXT_COLOR);
    getChildren().add(title);

    Tile autonDistanceConfig = tile("Extra Autonomous Distance")
      .skinType(SkinType.PLUS_MINUS)
      .unit("ft")
      .increment(1)
      .value(0)
      .minValue(0)
      .maxValue(15)
      .description("Dist. after baseline")
      .build();
    getChildren().add(autonDistanceConfig);
    //TODO bind property in a robot data sender to the value property of this
    
    Tile autonModeConfig = tile("TODO - Auton Side")
      .skinType(SkinType.SWITCH)
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
  }
}
