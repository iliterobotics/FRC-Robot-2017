package org.usfirst.frc.team1885.display.data;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.usfirst.frc.team1885.display.DisplayConfig;
import org.usfirst.frc.team1885.display.IUpdate;
import org.usfirst.frc.team1885.display.ObservableFX;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.IRemote;
import edu.wpi.first.wpilibj.tables.IRemoteConnectionListener;
import edu.wpi.first.wpilibj.tables.ITableListener;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

/**
 * Converts from the NetworkTables generic data components into 
 * <code>javafx.beans.property.Property</code> types. This allows JavaFX display code
 * to automagically update widgets when the data changes via the Java8 binding framework.
 */
public class RobotDataStream {
  private NetworkTable mTable;
  private ConnectionListener mConnectionListener;
  private ITableListener mListener = (source, key, value, isNew) -> updateData(key);
  protected static final String NO_DATA = "NO_DATA";
  
  protected Map<String, ESupportedTypes> mDataToCollect = new HashMap<>();
  
  protected final Map<String, ObservableFX<Number>> mLatestNumbers = new HashMap<>();
  protected final Map<String, ObservableFX<String>> mLatestStrings = new HashMap<>();
  protected final Map<String, ObservableFX<Boolean>> mLatestBooleans = new HashMap<>();
  protected final Map<String, ObservableFX<Object>> mLatestUnknowns = new HashMap<>();
  
  protected final ObservableList<String> mRobotLog = FXCollections.observableArrayList();
  private final Queue<String> mRobotLogDump = new LinkedList<>();
  
  protected RobotDataStream() {
    mRobotLog.add("CTOR LOG");

    // Initial known set of data
    for(ERobotData data : ERobotData.values()) {
      addDataToCollect(data.comms, data.type);
    }
    
    for(EDriverStationData data : EDriverStationData.values()) {
      addDataToCollect(data.name(), ESupportedTypes.STRING);
    }
    
    // Setup conversion from integer to 8 booleans for the PCM
    for(int i = 0; i < 8; i++) {
      createProperty(ERobotData.PCM.name() + i, ESupportedTypes.BOOLEAN);
    }
    mLatestNumbers.get(ERobotData.PCM.comms).addListener(data -> {
      // We could do reverse bit testing on the integer.
      // But why, when BigInteger lets us cheat!?
      BigInteger bi = BigInteger.valueOf((int)data);
      for(int i = 0; i < 8; i++) {
        mLatestBooleans.get(ERobotData.PCM.name() + i).setValue(bi.testBit(i));
      }
    });
    
    // Setup NetworkTables Connection property
    createProperty(DisplayConfig.NETWORK_TABLES_CONNECTION, ESupportedTypes.STRING);
    mConnectionListener = new ConnectionListener(mLatestBooleans.get(DisplayConfig.NETWORK_TABLES_CONNECTION));
    
    // Setup tables AFTER data is created...
    NetworkTable.setClientMode();
    DisplayConfig.ROBOT_IP_ADDRESS.addListener((obs, old, mew) -> updateIp());
    DisplayConfig.ROBOT_TELEMETRY_TABLE.addListener((obs, old, mew) -> updateTable());
    updateIp();
  }
  
  public void sendDataToRobot(String pData, ESupportedTypes pType, String pValue) {
    if(pValue == null) { 
      return;
    }
    switch(pType) {
    case BOOLEAN:
      mTable.putBoolean(pData, Boolean.parseBoolean(pValue));
      break;
    case DOUBLE:
    case INTEGER:
    case LONG:
      mTable.putNumber(pData, Double.parseDouble(pValue));
      break;
    case STRING:
    case UNSUPPORTED:
    default:
      mTable.putString(pData, pValue);
      break;
    }
  }
  
  /**
   * Quick method to add a listener for any String data from the robot.  No need to mess
   * with multiple calls, multiple data types, wonky listeners, etc.  Just call this method
   * with an appropriate Comms Id and Listener to start receiving data updates.
   * <br><br>
   * This is particularly useful for dynamically adding JSON updates from the robot without
   * needing to update the display's configs.
   * <br><br>
   * Under the covers, this method calls the <code>addDataToCollect</code> method and then
   * adds a listener. 
   */
  public void addDataWithGenericListener(String pCommsId, IUpdate<String> pListener) {
    addDataToCollect(pCommsId, ESupportedTypes.STRING);
    mLatestStrings.get(pCommsId).addListener(pListener);
  }
  
  /**
   * Adds a data and its type to the polling method of this data stream. If the
   * data was already added to this stream, then this will update the data type
   * to the input type.
   * 
   * @return Whether or not the data was already being collected.  If TRUE, it
   * is likely the calling method needs to re-bind properties to this data stream
   * using <code>listenForLatest(...)</code>
   */
  public final boolean addDataToCollect(String pCommsId, ESupportedTypes pType) {
    boolean exists = mDataToCollect.containsKey(pCommsId);
    mDataToCollect.put(pCommsId, pType);
    createProperty(pCommsId, pType);
    return exists;
  }

  /**
   * Adds a one-way data binding to the input property.  When the polling task retrieves the data from
   * the comms interface, the input property will be updated.
   * 
   * Note that this a "mostly" type-safe cast.  If a calling method tries to bind a boolean property
   * to what is an integer in the stream, this will throw a ClassCastException.
   * 
   * If a property was not added to the data stream prior to this method being called, no
   * property will be created - therefore no data will be retrieved for <code>pData</code>.
   * 
   * @param pType - This is the class representing the type of the display component property
   * @param pProperty - this is the property usually associated to a display component or a data
   * model for a display component.
   * @return the current value of the property - usually this is null on display startup.
   */
  public <T> T bindOneWay(String pCommsId, Class<T> pType, Property<T> pProperty) {
    ObservableFX<?> p = getProperty(pCommsId, ESupportedTypes.fromType(pType));
    if(p != null) {
      // JIT compiler trick - this ChangeListener will be the same 'type' as T
      p.addListener(data -> pProperty.setValue(pType.cast(data)));
      if(p.getValue() != null) {
        return pType.cast(p.getValue());
      }
    }
    return null;
  }
  
  /**
   * Adds a basic listener to a value
   * @return current value of the data
   */
  public <T> T addListenerToData(String pCommsId, Class<T> pType, IUpdate<T> pListener) {
    ObservableFX<?> p = getProperty(pCommsId, ESupportedTypes.fromType(pType));
    if(p != null) {
      // JIT compiler trick - this ChangeListener will be the same 'type' as T
      p.addListener(data -> pListener.update(pType.cast(data)));
      if(p.getValue() != null) {
        return pType.cast(p.getValue());
      }
    }
    return null;
  }

  public void bindListToRobotLog(ListView<String> list) {
    list.setItems(mRobotLog); 
  }

  public void log(String string) {
    Platform.runLater(() -> {
      mRobotLogDump.offer(string);
      mRobotLog.add(string);
    });
  }
  
  private void updateRobotLog() {
    String[] newlogs = mTable.getStringArray(ERobotData.ROBOT_LOG.comms, new String[]{""});
    if(newlogs != null && newlogs.length > 0) {
      int toRemove = Math.max(0, mRobotLogDump.size() + newlogs.length - DisplayConfig.MAX_ROBOT_LOG_SIZE);
      for(int i = 0; i < toRemove; i++) {
        mRobotLogDump.remove();
      }
      for(int i = 0; i < newlogs.length; i++) {
        mRobotLogDump.offer(newlogs[i]);
      }
      
      Platform.runLater(() -> {
        mRobotLog.clear();
        mRobotLog.addAll(mRobotLogDump);
      });
    }
  }
  
  private void updateIp() {
    NetworkTable.setIPAddress(DisplayConfig.ROBOT_IP_ADDRESS.getValue());
    updateTable();
  }
  
  private void updateTable() {
    if(mTable != null) {
      mTable.removeTableListener(mListener);
      mTable.removeConnectionListener(mConnectionListener);
    }
    mTable = NetworkTable.getTable(DisplayConfig.ROBOT_TELEMETRY_TABLE.getValue());
    mTable.addTableListener(mListener);
    mTable.addConnectionListener(mConnectionListener, true);
  }

  /**
   * Creates a property in the appropriate map.
   */
  private void createProperty(String pCommsId, ESupportedTypes pType) {

    if(getProperty(pCommsId, pType) != null) {
      deleteProperty(pCommsId);
    }
    
    switch(pType) {
    case BOOLEAN:
      mLatestBooleans.put(pCommsId, new ObservableFX<>());
      break;
    case INTEGER:
    case LONG:
    case DOUBLE:
      mLatestNumbers.put(pCommsId, new ObservableFX<>());
      break;
    case STRING:
      mLatestStrings.put(pCommsId, new ObservableFX<>());
      break;
    case UNSUPPORTED:
    default:
      mLatestUnknowns.put(pCommsId, new ObservableFX<>());
      break;
    
    }
  }

  private void deleteProperty(String pCommsId) {
    mLatestBooleans.remove(pCommsId);
    mLatestNumbers.remove(pCommsId);
    mLatestStrings.remove(pCommsId);
    mLatestUnknowns.remove(pCommsId);
  }
  
  /**
   * Retrieves the data from the network table and converts it to the appropriate type.
   * @param pCommsId
   */
  private void updateData(String pCommsId) {
    
    if(pCommsId.equals(ERobotData.ROBOT_LOG.comms)) {
      updateRobotLog();
    } else {
      if(!mDataToCollect.containsKey(pCommsId)) {
        addDataToCollect(pCommsId, ESupportedTypes.UNSUPPORTED);
      }
      
      switch(mDataToCollect.get(pCommsId)) {
      case BOOLEAN:
     // Bang. Head. On. Desk.  Should be allowed to pass null as the default to signify "no data"...
        mLatestBooleans.get(pCommsId).setValue(mTable.getBoolean(pCommsId, Boolean.FALSE));
        break;
      case INTEGER:
      case LONG:
      case DOUBLE:
        mLatestNumbers.get(pCommsId).setValue(mTable.getNumber(pCommsId, Double.NaN));
        break;
      case STRING:
        mLatestStrings.get(pCommsId).setValue(mTable.getString(pCommsId, NO_DATA));
        break;
      default:
        mLatestUnknowns.get(pCommsId).setValue(mTable.getString(pCommsId, NO_DATA));
      }
    }
  }
  
  private ObservableFX<?> getProperty(String pCommsId, ESupportedTypes pType) {
    switch(pType) {
    case BOOLEAN:
      return mLatestBooleans.get(pCommsId);
    case DOUBLE:
    case INTEGER:
    case LONG:
      return mLatestNumbers.get(pCommsId);
    case STRING:
      return mLatestStrings.get(pCommsId);
    case UNSUPPORTED:
      return mLatestUnknowns.get(pCommsId);
    }
    
    return null;
  }
  
  private class ConnectionListener implements IRemoteConnectionListener {
    private final ObservableFX<Boolean> mProperty;
    ConnectionListener(ObservableFX<Boolean> pProperty) {
      mProperty = pProperty;
    }

    @Override
    public void connected(IRemote remote) {
      System.out.println("Network tables Connected");
      mProperty.setValue(true);
    }

    @Override
    public void disconnected(IRemote remote) {
      System.out.println("Network tables Disconnected");
      mProperty.setValue(false);
    }
  }

  
  /*
   * Singleton junk
   */
  private static RobotDataStream inst;
  public static RobotDataStream inst() {
    if(inst == null) {
      inst = new RobotDataStream();
    }
    return inst;
  }
}
