package org.usfirst.frc.team1885.display.ui;

import org.usfirst.frc.team1885.display.DisplayConfig;
import org.usfirst.frc.team1885.display.data.RobotDataStream;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.Tile.SkinType;
import eu.hansolo.tilesfx.Tile.TextSize;
import eu.hansolo.tilesfx.TileBuilder;
import javafx.geometry.Insets;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;

public abstract class AbstractTilePane extends FlowPane{

  protected final RobotDataStream rds = RobotDataStream.inst();
  
  public static void decorate(Region pRegion) {
    pRegion.setPadding(new Insets(10));
    pRegion.setBackground(DisplayConfig.PANEL_BACKGROUND);
  }
  
  public AbstractTilePane() {
    decorate(this);
    setHgap(10);
    setVgap(10);
  }
  
  protected final Tile createBlankTile() {
    return tile("",SkinType.CUSTOM).backgroundColor(DisplayConfig.PANEL_BACKGROUND_COLOR).build();
  }

  protected final TileBuilder<?> tile(String title, SkinType pSkinType) {
    return TileBuilder.create()
      .skinType(pSkinType)
      .backgroundColor(DisplayConfig.TILE_BACKGROUND)
      .title(title)
      .textSize(TextSize.BIGGER)
      .valueColor(DisplayConfig.DEFAULT_TILE_TEXT_COLOR)
      .unitColor(DisplayConfig.DEFAULT_TILE_TEXT_COLOR)
      .roundedCorners(true)
      .prefSize(DisplayConfig.DEFAULT_TILE_WIDTH, DisplayConfig.DEFAULT_TILE_WIDTH);
  }


  protected final GaugeBuilder<?> gauge(final Gauge.SkinType TYPE) {
    return GaugeBuilder.create()
      .skinType(TYPE)
      .prefSize(DisplayConfig.DEFAULT_TILE_WIDTH/1.62, DisplayConfig.DEFAULT_TILE_HEIGHT/1.62)
      .animated(true)
      .valueColor(DisplayConfig.DEFAULT_TILE_TEXT_COLOR)
      .titleColor(DisplayConfig.DEFAULT_TILE_TEXT_COLOR)
      .unitColor(DisplayConfig.DEFAULT_TILE_TEXT_COLOR)
      .barColor(DisplayConfig.ILITE_PURPLE)
      .needleColor(DisplayConfig.DEFAULT_TILE_TEXT_COLOR)
      .barBackgroundColor(Tile.BACKGROUND.darker())
      .tickLabelColor(DisplayConfig.DEFAULT_TILE_TEXT_COLOR)
      .majorTickMarkColor(DisplayConfig.DEFAULT_TILE_TEXT_COLOR)
      .minorTickMarkColor(DisplayConfig.DEFAULT_TILE_TEXT_COLOR)
      .mediumTickMarkColor(DisplayConfig.DEFAULT_TILE_TEXT_COLOR);
  }
}
