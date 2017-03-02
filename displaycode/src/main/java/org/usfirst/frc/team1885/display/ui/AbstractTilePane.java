package org.usfirst.frc.team1885.display.ui;

import org.usfirst.frc.team1885.display.DisplayConfig;

import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.Tile.TextSize;
import javafx.geometry.Insets;
import javafx.scene.layout.FlowPane;

public abstract class AbstractTilePane extends FlowPane{
  
  public AbstractTilePane() {
    setPadding(new Insets(10));
    setHgap(10);
    setVgap(10);
    setBackground(DisplayConfig.PANEL_BACKGROUND);
  }

  protected final TileBuilder<?> tile(String title) {
    return TileBuilder.create()
      .backgroundColor(DisplayConfig.TILE_BACKGROUND)
      .title(title)
      .textSize(TextSize.BIGGER)
      .valueColor(DisplayConfig.DEFAULT_TILE_TEXT_COLOR)
      .unitColor(DisplayConfig.DEFAULT_TILE_TEXT_COLOR)
      .roundedCorners(true)
      .prefSize(DisplayConfig.DEFAULT_TILE_WIDTH, DisplayConfig.DEFAULT_TILE_WIDTH);
  }
}
