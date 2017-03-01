package org.usfirst.frc.team1885.display;

import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.Tile.SkinType;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;

public class ILITE2017DataDisplay {
  
  private final FlowPane mDisplay = new FlowPane();
  
  public Region getDisplayComponent() {
    return mDisplay;
  }
  
  public ILITE2017DataDisplay() {
    RobotDataStream rds = RobotDataStream.inst();
    Tile mode = TileBuilder.create()
      .prefSize(DisplayConfig.DEFAULT_TILE_WIDTH, DisplayConfig.DEFAULT_TILE_HEIGHT)
      .skinType(SkinType.TEXT)
      .title("Game Mode")
      .text("Waiting To Begin...")
      .textVisible(true)
      .build();
    rds.bindOneWay(ERobotData.MATCH_PERIOD.comms, String.class, mode.textProperty());
    mDisplay.getChildren().add(mode);
    
    Tile matchTime = TileBuilder.create()
      .prefSize(DisplayConfig.DEFAULT_TILE_WIDTH, DisplayConfig.DEFAULT_TILE_HEIGHT)
      .skinType(SkinType.NUMBER)
      .title("Match Time")
      .build();
    rds.bindOneWay(ERobotData.MATCH_TIME_MS.comms, matchTime.valueProperty());
    mDisplay.getChildren().add(matchTime);
  }
}
