package org.usfirst.frc.team1885.display;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.usfirst.frc.team1885.display.DisplayConfig;
import org.usfirst.frc.team1885.display.ERobotData;
import org.usfirst.frc.team1885.display.ESupportedTypes;

import com.sun.xml.internal.ws.api.addressing.AddressingPropertySet;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.IRemote;
import edu.wpi.first.wpilibj.tables.IRemoteConnectionListener;
import edu.wpi.first.wpilibj.tables.ITableListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;

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
  
  protected final Map<String, DoubleProperty> mLatestDoubles = new HashMap<>();
  protected final Map<String, IntegerProperty> mLatestIntegers = new HashMap<>();
  protected final Map<String, LongProperty> mLatestLongs = new HashMap<>();
  protected final Map<String, Property<String>> mLatestStrings = new HashMap<>();
  protected final Map<String, Property<Boolean>> mLatestBooleans = new HashMap<>();
  protected final Map<String, Property<Object>> mLatestUnknowns = new HashMap<>();
  
  protected RobotDataStream() {
    // Initial known set of data
    for(ERobotData data : ERobotData.values()) {
      addDataToCollect(data.comms, data.type);
    }
    
    // Setup conversion from integer to 8 booleans for the PCM
    for(int i = 0; i < 8; i++) {
      createProperty(ERobotData.PCM.name() + i, ESupportedTypes.BOOLEAN);
    }
    mLatestIntegers.get(ERobotData.PCM.comms).addListener((obs, old, mew) -> {
      // We could do reverse bit testing on the integer.
      // But why, when BigInteger lets us cheat!?
      BigInteger bi = BigInteger.valueOf((Integer)mew);
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
    mLatestStrings.get(pCommsId).addListener((obs, old, mew) -> pListener.update(mew));
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
    Property<?> p = getProperty(pCommsId, ESupportedTypes.fromType(pType));
    if(p != null) {
      p.addListener((obs, old, mew) -> pProperty.setValue(pType.cast(mew)));
      return pType.cast(p.getValue());
    }
    return null;
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
      mLatestBooleans.put(pCommsId, new SimpleBooleanProperty());
      break;
    case DOUBLE:
      mLatestDoubles.put(pCommsId, new SimpleDoubleProperty());
      break;
    case INTEGER:
      mLatestIntegers.put(pCommsId, new SimpleIntegerProperty());
      break;
    case LONG:
      mLatestLongs.put(pCommsId, new SimpleLongProperty());
      break;
    case STRING:
      mLatestStrings.put(pCommsId, new SimpleStringProperty());
      break;
    case UNSUPPORTED:
    default:
      mLatestUnknowns.put(pCommsId, new SimpleObjectProperty<Object>());
      break;
    
    }
  }

  private void deleteProperty(String pCommsId) {
    mLatestBooleans.remove(pCommsId);
    mLatestDoubles.remove(pCommsId);
    mLatestIntegers.remove(pCommsId);
    mLatestLongs.remove(pCommsId);
    mLatestStrings.remove(pCommsId);
    mLatestUnknowns.remove(pCommsId);
  }
  
  /**
   * Retrieves the data from the network table and converts it to the appropriate type.
   * @param pCommsId
   */
  private void updateData(String pCommsId) {
    if(!mDataToCollect.containsKey(pCommsId)) {
      addDataToCollect(pCommsId, ESupportedTypes.UNSUPPORTED);
    }
    
    switch(mDataToCollect.get(pCommsId)) {
    case BOOLEAN:
   // Bang. Head. On. Desk.  Should be allowed to pass null as the default to signify "no data"...
      mLatestBooleans.get(pCommsId).setValue(mTable.getBoolean(pCommsId, Boolean.FALSE));
      break;
    case INTEGER:
      mLatestIntegers.get(pCommsId).setValue((int)mTable.getNumber(pCommsId, Integer.MAX_VALUE));
      break;
    case LONG:
      mLatestLongs.get(pCommsId).setValue((long)mTable.getNumber(pCommsId, Long.MAX_VALUE));
      break;
    case DOUBLE:
      mLatestDoubles.get(pCommsId).setValue(mTable.getNumber(pCommsId, Double.NaN));
      break;
    case STRING:
      mLatestStrings.get(pCommsId).setValue(mTable.getString(pCommsId, NO_DATA));
      break;
    default:
      mLatestUnknowns.get(pCommsId).setValue(mTable.getString(pCommsId, NO_DATA));
    }
  }
  
  private Property<?> getProperty(String pCommsId, ESupportedTypes pType) {
    switch(pType) {
    case BOOLEAN:
      return mLatestBooleans.get(pCommsId);
    case DOUBLE:
      return mLatestDoubles.get(pCommsId);
    case INTEGER:
      return mLatestIntegers.get(pCommsId);
    case LONG:
      return mLatestLongs.get(pCommsId);
    case STRING:
      return mLatestStrings.get(pCommsId);
    case UNSUPPORTED:
      return mLatestUnknowns.get(pCommsId);
    }
    
    return null;
  }
  
  private class ConnectionListener implements IRemoteConnectionListener {
    private final Property<Boolean> mProperty;
    ConnectionListener(Property<Boolean> pProperty) {
      mProperty = pProperty;
    }

    @Override
    public void connected(IRemote remote) {
      mProperty.setValue(true);
    }

    @Override
    public void disconnected(IRemote remote) {
      mProperty.setValue(false);
    }
  }

  
  /*
   * Singleton junk
   */
  private static final RobotDataStream inst = new RobotDataStream();
  public static RobotDataStream inst() { return inst; }
}
