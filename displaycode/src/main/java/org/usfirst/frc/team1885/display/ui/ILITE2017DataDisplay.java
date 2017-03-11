package org.usfirst.frc.team1885.display.ui;

import org.usfirst.frc.team1885.display.DisplayConfig;
import org.usfirst.frc.team1885.display.IUpdate;
import org.usfirst.frc.team1885.display.data.ERobotData;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.Section;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.Tile.SkinType;
import eu.hansolo.tilesfx.skins.BarChartItem;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;

public class ILITE2017DataDisplay extends AbstractTilePane{
  
  
  public ILITE2017DataDisplay() {
    Gauge pressureGauge = gauge(Gauge.SkinType.SIMPLE_SECTION)
      .unit("psi")
      .minValue(0d)
      .maxValue(140d)
      .value(81.2d)
      .sections(new Section(0d, 34.9999d, Color.RED),
                new Section(35d, 59.9999d, Color.ORANGE),
                new Section(60d, 89.9999d, Color.YELLOW),
                new Section(90d, 114.9999d, Color.GREEN),
                new Section(115d, 124.9999d, Color.YELLOW),
                new Section(125d, 140d, Color.RED))
      .minMeasuredValueVisible(true)
      .build();
    Tile pressure = tile("Pressure", SkinType.CUSTOM)
      .graphic(pressureGauge)
      .text("TODO - Implement   Min: N/A")
      .textVisible(true)
      .build();
    // TODO - bind to rds
    // TODO - bind text to min value
    getChildren().add(pressure);
    
    Tile pressureRaw = tile("Analog Pressure Readout", SkinType.GAUGE)
      .minValue(0d)
      .maxValue(5d)
      .value(4d)
      .unit("Volts")
      .text("TODO - Implement")
      .textVisible(true)
      .build();
    // TODO - bind to rds
    getChildren().add(pressureRaw);
    
    BarChartItem leftDist = new BarChartItem("Left", 1.5d, DisplayConfig.ILITE_GREEN);
    rds.bindOneWay(ERobotData.LEFT_ENCODER_POS.comms, Number.class, leftDist.valueProperty());
    BarChartItem rightDist = new BarChartItem("Right", 1.5d, DisplayConfig.ILITE_GREEN);
    rds.bindOneWay(ERobotData.RIGHT_ENCODER_POS.comms, Number.class, rightDist.valueProperty());
    
    Tile distances = tile("Encoder Distance (ft)", SkinType.BAR_CHART)
      .barChartItems(leftDist, rightDist)
      .maxValue(100d)
      .build();
    getChildren().add(distances);
    
    BarChartItem leftVelocity = new BarChartItem("Left", 9.5d, DisplayConfig.ILITE_GREEN);
    rds.bindOneWay(ERobotData.LEFT_ENCODER_VELOCITY.comms, Number.class, leftVelocity.valueProperty());
    BarChartItem rightVelocity = new BarChartItem("Right", 9.7d, DisplayConfig.ILITE_GREEN);
    rds.bindOneWay(ERobotData.RIGHT_ENCODER_VELOCITY.comms, Number.class, rightVelocity.valueProperty());
    
    Tile encoderVelocityChart = tile("Encoder Speed (ft/s)", SkinType.BAR_CHART)
      .barChartItems(leftVelocity, rightVelocity)
      .text("Peak: 0.00")
      .textVisible(true)
      .maxValue(13d)
      .build();
    getChildren().add(encoderVelocityChart);
    
    IUpdate<Number> updater = update -> {
      encoderVelocityChart.setMaxValue(Math.max((Double)update, encoderVelocityChart.getMaxValue()));
      encoderVelocityChart.setDescription("Peak: " + encoderVelocityChart.getMaxValue());
    };
    
    rds.addListenerToData(ERobotData.LEFT_ENCODER_VELOCITY.comms, Number.class, updater);
    rds.addListenerToData(ERobotData.RIGHT_ENCODER_VELOCITY.comms, Number.class, updater);
    rightVelocity.setValue(1d);
    
    Gauge gyroGauge = gauge(Gauge.SkinType.FLAT)
      .minValue(-180d)
      .barColor(DisplayConfig.ILITE_GREEN)
      .maxValue(180d)
      .value(0d)
      .value(-60)
      .unit("degrees")
      .build();
    
    Tile gyroTile = tile("Gyro Reading", SkinType.CUSTOM)
      .graphic(gyroGauge)
      .text("TODO - Implement")
      .textVisible(true)
      .build();
    rds.bindOneWay(ERobotData.GYRO_DEG.comms, Number.class, gyroTile.valueProperty());
    getChildren().add(gyroTile);
    
    getChildren().add(createBlankTile());
    
    Tile dtCurrent = tile("Drive Train Current", SkinType.SPARK_LINE)
      .unit("Amps")
      .maxValue(160d)
      .build();
    getChildren().add(dtCurrent);
    rds.bindOneWay(ERobotData.DRIVETRAIN_CURRENT.comms, Number.class, dtCurrent.valueProperty());

    
    Tile intakeCurrent = tile("Intake Current", SkinType.SPARK_LINE)
      .unit("Amps")
      .maxValue(40d)
      .build();
    getChildren().add(intakeCurrent);
    rds.bindOneWay(ERobotData.INTAKE_CURRENT.comms, Number.class, intakeCurrent.valueProperty());
    
    Tile climbCurrent = tile("Climber Current", SkinType.SPARK_LINE)
      .unit("Amps")
      .maxValue(80d)
      .build();
    getChildren().add(climbCurrent);
    rds.bindOneWay(ERobotData.CLIMBER_CURRENT.comms, Number.class, climbCurrent.valueProperty());
  }

  XYChart.Series<Number, Number> dtseries = new XYChart.Series<>();
}
