package org.usfirst.frc.team1885.display.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.usfirst.frc.team1885.display.IUpdate;
import org.usfirst.frc.team1885.display.data.DriverStationDataStream.EDriverStationData;

import edu.wpi.first.wpilibj.DriverStation;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;

public class DriverStationDataStream {
  
  public enum EDriverStationData {
    BATTERY_VOLTAGE,
    ALLIANCE,
    STATION_ID,
    MATCH_MODE,
    DS_CONN_STATUS
  }
  
  private Timer mPollingTimer = new Timer("Driver Station Poller");
  private ConnectToDSTask mDSConnectTask = new ConnectToDSTask();
  private DriverStation mDSInstance;
  private Map<EDriverStationData, Property<String>> mData = new HashMap<>();
  
  public void addListener(EDriverStationData pData, IUpdate<String> pListener) {
    mData.get(pData).addListener((obs, old, mew) -> pListener.update(mew));
  }
  
  private DriverStationDataStream() {
    for(EDriverStationData data : EDriverStationData.values()) {
      mData.put(data, new SimpleStringProperty());
    }
    
    // TODO create start/stop/restart method
    // TODO implement transfer from trying to connect to polling for data
  }
  
  private class DriverStationPollTask extends TimerTask {
    @Override
    public void run() {
      //TODO 
    }
  }
  
  private class ConnectToDSTask extends TimerTask {
    @Override
    public void run() {
      //TODO
    }
    
  }

  private static DriverStationDataStream inst;
  public static DriverStationDataStream inst() {
    if(inst == null) {
      inst = new DriverStationDataStream();
    }
    return inst;
  }

  public void setValueForTesting(EDriverStationData pData, String pValue) {
    mData.get(pData).setValue(pValue);
  }

}
