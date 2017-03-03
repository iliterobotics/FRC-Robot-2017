package org.usfirst.frc.team1885.display;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.scene.paint.Color;

public enum EGameMode {
  AUTONOMOUS(Color.DEEPSKYBLUE, FontAwesomeIcon.ANDROID),
  TELEOP(DisplayConfig.ILITE_GREEN, FontAwesomeIcon.GAMEPAD),
  DISABLED(Color.YELLOW, FontAwesomeIcon.HAND_STOP_ALT),
  BROWNOUT(DisplayConfig.ERROR_COLOR, FontAwesomeIcon.FREE_CODE_CAMP);
  
  public final Color color;
  public final FontAwesomeIcon icon;
  
  private EGameMode(Color pColor, FontAwesomeIcon pIcon) {
    color = pColor;
    icon = pIcon;
  }
}
