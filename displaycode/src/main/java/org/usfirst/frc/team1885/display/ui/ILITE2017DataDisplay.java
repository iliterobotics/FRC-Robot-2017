package org.usfirst.frc.team1885.display.ui;

import org.usfirst.frc.team1885.display.DisplayConfig;
import org.usfirst.frc.team1885.display.ERobotData;
import org.usfirst.frc.team1885.display.RobotDataStream;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.Tile.SkinType;

public class ILITE2017DataDisplay extends AbstractTilePane{
  
  private final RobotDataStream rds = RobotDataStream.inst();
  
  public ILITE2017DataDisplay() {
    
    Tile sample = tile("Sample Title")
      .skinType(SkinType.SPARK_LINE)
      .text("Text sample")
      .textVisible(true)
      .alertMessage("Alert Message sample")
      .description("Description Text Sample")
      .unit("ft")
      .increment(1)
      .minValue(1)
      .maxValue(15)
      .build();
    getChildren().add(sample);
    
    Tile mode = tile("Game Mode")
      .skinType(SkinType.TEXT)
      .description("DISABLED")
      .descriptionColor(DisplayConfig.DEFAULT_TILE_TEXT_COLOR)
      .build();
    rds.bindOneWay(ERobotData.MATCH_PERIOD.comms, String.class, mode.descriptionProperty());
    getChildren().add(mode);
    
    Tile matchTime = tile("Match Time")
      .skinType(SkinType.NUMBER)
      .unit("s")
      .build();
    rds.bindOneWay(ERobotData.MATCH_TIME_MS.comms, Number.class, matchTime.valueProperty());
    getChildren().add(matchTime);
    
    Tile leftEncoder = tile("Left Encoder Distance")
      .skinType(SkinType.NUMBER)
      .title("Left Encoder Distance")
      .unit("ft")
      .build();
    rds.bindOneWay(ERobotData.LEFT_ENCODER_POS.comms, Number.class, leftEncoder.valueProperty());
    getChildren().add(leftEncoder);
    
    Tile rightEncoderPos = tile("Right Encoder Distance")
      .skinType(SkinType.NUMBER)
      .unit("ft")
      .build();
    rds.bindOneWay(ERobotData.RIGHT_ENCODER_POS.comms, Number.class, rightEncoderPos.valueProperty());
    getChildren().add(rightEncoderPos);
    
    Tile leftEncoderVelocity = tile("Left Encoder Velocity")
      .skinType(SkinType.SPARK_LINE)
      .minValue(0)
      .maxValue(0.1d)
      .description("Peak: N/A")
      .textVisible(true)
      .unit("ft/s")
      .build();
    rds.bindOneWay(ERobotData.LEFT_ENCODER_VELOCITY.comms, Number.class, leftEncoderVelocity.valueProperty());
    getChildren().add(leftEncoderVelocity);
    rds.addListenerToData(ERobotData.LEFT_ENCODER_VELOCITY.comms, Number.class, update -> {
      leftEncoderVelocity.setMaxValue(Math.max((Double)update, leftEncoderVelocity.getMaxValue()));
      leftEncoderVelocity.setDescription("Peak: " + leftEncoderVelocity.getMaxValue());
    });
    
    Tile rightEncoderVelocity = tile("Right Encoder Velocity")
      .skinType(SkinType.SPARK_LINE)
      .minValue(0)
      .maxValue(0.1d)
      .description("Peak: N/A")
      .textVisible(true)
      .unit("ft/s")
      .build();
    rds.bindOneWay(ERobotData.RIGHT_ENCODER_VELOCITY.comms, Number.class, rightEncoderVelocity.valueProperty());
    getChildren().add(rightEncoderVelocity);
    rds.addListenerToData(ERobotData.RIGHT_ENCODER_VELOCITY.comms, Number.class, update -> {
      rightEncoderVelocity.setMaxValue(Math.max((Double)update, rightEncoderVelocity.getMaxValue()));
      rightEncoderVelocity.setDescription("Peak: " + rightEncoderVelocity.getMaxValue());
    });
    
  }
  
}
