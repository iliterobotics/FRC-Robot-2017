package org.usfirst.frc.team1885.display.ui;

import java.util.ArrayList;
import java.util.List;

import org.usfirst.frc.team1885.display.DisplayConfig;
import org.usfirst.frc.team1885.display.EGameMode;
import org.usfirst.frc.team1885.display.data.DriverStationDataStream.EDriverStationData;
import org.usfirst.frc.team1885.display.data.ERobotData;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory;
import de.jensd.fx.glyphs.icons525.Icons525;
import de.jensd.fx.glyphs.icons525.utils.Icon525Factory;
import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.Gauge.NeedleType;
import eu.hansolo.medusa.Section;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.Tile.SkinType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class ILITE2017RobotStatusDisplay extends AbstractTilePane{
  public ILITE2017RobotStatusDisplay() {
    Label title = new Label(" System Status ");
    title.setFont(Font.font(null, FontWeight.BOLD, 20));
    title.setTextFill(DisplayConfig.DEFAULT_TILE_TEXT_COLOR);
    getChildren().add(title);
    
    Text disconnected = FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.WARNING, DisplayConfig.ICON_FONT_AWESOME_TILE_SIZE);
    disconnected.setFill(DisplayConfig.ERROR_COLOR);
    Text connected = Icon525Factory.get().createIcon(Icons525.CIRCLESELECT, DisplayConfig.ICON_FONT_AWESOME_TILE_SIZE);
    connected.setFill(DisplayConfig.ILITE_GREEN);

    Tile connect = tile("Network Tables Status", SkinType.CUSTOM)
      .graphic(disconnected)
      .text("Disconnected")
      .textVisible(true)
      .build();
    rds.addListenerToData(DisplayConfig.NETWORK_TABLES_CONNECTION, Boolean.class, c -> {
      if(c) {
        connect.setGraphic(connected);
        connect.setText("Connected");
      } else {
        connect.setGraphic(disconnected);
        connect.setText("Disconnected");
      }
    });
    getChildren().add(connect);
    
    FlowPane modes = new FlowPane();
    modes.setPadding(new Insets(10));
    modes.setHgap(20);
    modes.setVgap(20);
    modes.setAlignment(Pos.CENTER);
    List<Text> texts = new ArrayList<>();
    for(int i = 0; i < EGameMode.values().length; i++) {
      EGameMode m = EGameMode.values()[i];
      Text t = FontAwesomeIconFactory.get().createIcon(m.icon, DisplayConfig.ICON_FONT_AWESOME_TILE_SIZE);
      t.setFill(m.color);
      modes.getChildren().add(t);
      texts.add(t);
    }
    
    Tile modeTile = tile("Game Mode", SkinType.CUSTOM)
      .textVisible(true)
      .text("Waiting for update...")
      .graphic(modes)
      .build();
    
    rds.bindOneWay(ERobotData.MATCH_PERIOD.comms, String.class, modeTile.textProperty());
    dsds.addListener(EDriverStationData.MATCH_MODE, newmode -> {
      texts.forEach(m -> m.setFill(DisplayConfig.TILE_BACKGROUND));
      EGameMode mm = EGameMode.valueOf(newmode);
      texts.get(mm.ordinal()).setFill(mm.color);
      modeTile.setText(mm.toString());
      modeTile.setTextColor(mm.color);
    });
    
    getChildren().add(modeTile);
    
    
    Tile matchTime = tile("Match Time", SkinType.NUMBER)
      .unit("seconds")
      .build();
    rds.bindOneWay(ERobotData.MATCH_TIME_MS.comms, Number.class, matchTime.valueProperty());
    getChildren().add(matchTime);

    
    Gauge batteryGauge = gauge(Gauge.SkinType.SIMPLE_SECTION)
      .unit("Volts")
      .minValue(0d)
      .maxValue(14d)
      .value(12.3d)
      .valueVisible(true)
      .sections(new Section(0d, 10.49999d, Color.RED),
                new Section(10.5d, 11.9999d, Color.YELLOW),
                new Section(12d, 14d, Color.GREEN))
      .needleType(NeedleType.AVIONIC)
      .build();
    
    Tile battery = tile("Battery Voltage", SkinType.CUSTOM)
      .graphic(batteryGauge)
      .text("Source: Driver's Station")
      .textVisible(true)
      .build();
    getChildren().add(battery);
    dsds.addListener(EDriverStationData.BATTERY_VOLTAGE, 
      voltage -> batteryGauge.setValue(Double.parseDouble(voltage)));
    
  }
}
