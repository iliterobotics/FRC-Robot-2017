package org.usfirst.frc.team1885.display.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.usfirst.frc.team1885.display.EGameMode;
import org.usfirst.frc.team1885.display.IUpdate;
import org.usfirst.frc.team1885.display.ObservableFX;

import edu.wpi.first.wpilibj.DriverStation;

public class DriverStationDataStream {
  
  public enum EDriverStationData {
    BATTERY_VOLTAGE,
    ALLIANCE,
    STATION_ID,
    MATCH_MODE,
    MATCH_TIME,
    DS_CONN_STATUS
  }
  
  private Timer mPollingTimer = new Timer("Driver Station Poller");
  private ConnectToDSTask mDSConnectTask;
  private DriverStation mDSInstance;
  private final Executor mPool = Executors.newFixedThreadPool(1);
  private Map<EDriverStationData, ObservableFX<String>> mData = new HashMap<>();
  
  public void addListener(EDriverStationData pData, IUpdate<String> pListener) {
    mData.get(pData).addListener(pListener);
  }
  
  private DriverStationDataStream() {
    for(EDriverStationData data : EDriverStationData.values()) {
      mData.put(data, new ObservableFX<>());
    }
    
    // TODO create start/stop/restart method
    // TODO implement transfer from trying to connect to polling for data
    mDSConnectTask = new ConnectToDSTask();
    
    
  }
  
  private class DriverStationWaitTask implements Runnable {
    @Override
    public void run() {
      DriverStation.getInstance().waitForData();  // Maybe this blows up ... maybe it doesn't?
    }
  }
  
  private class DriverStationPollTask extends TimerTask {
    @Override
    public void run() {
      DriverStation ds = DriverStation.getInstance();
      mData.get(EDriverStationData.ALLIANCE).setValue(ds.getAlliance().name());
      mData.get(EDriverStationData.BATTERY_VOLTAGE).setValue(Double.toString(ds.getBatteryVoltage()));
      mData.get(EDriverStationData.MATCH_TIME).setValue(Double.toString(ds.getMatchTime()));
      
      EGameMode mode = EGameMode.DISABLED;
      if(ds.isAutonomous()) {
        mode = EGameMode.AUTONOMOUS;
      } else if (ds.isBrownedOut()) {
        mode = EGameMode.BROWNOUT;
      } else if (ds.isDisabled()) {
        mode = EGameMode.DISABLED;
      } else if (ds.isOperatorControl()) {
        mode = EGameMode.TELEOP;
      }
      mData.get(EDriverStationData.MATCH_MODE).setValue(mode.name());
    }
  }
  
  private final Object connectionLock = new Object();
  private class ConnectToDSTask extends TimerTask {
    @Override
    public void run() {
      DriverStation.getInstance().waitForData();
      // May be able to test this direction via wpilib.Utility.getFPGATime()
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
