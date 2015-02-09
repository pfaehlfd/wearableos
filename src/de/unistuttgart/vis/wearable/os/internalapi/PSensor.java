/*
 * This file is part of the Garment OS Project. For any details concerning use 
 * of this project in source or binary form please refer to the provided license
 * file.
 * 
 * (c) 2014-2015 pfaehlfd, roehrdor, roehrlls
 */
package de.unistuttgart.vis.wearable.os.internalapi;

import de.unistuttgart.vis.wearable.os.graph.GraphType;
import de.unistuttgart.vis.wearable.os.sensors.MeasurementSystems;
import de.unistuttgart.vis.wearable.os.sensors.MeasurementUnits;
import de.unistuttgart.vis.wearable.os.sensors.SensorData;
import de.unistuttgart.vis.wearable.os.sensors.SensorType;
import de.unistuttgart.vis.wearable.os.utils.Constants;
import de.unistuttgart.vis.wearable.os.utils.Utils;

/**
 * Sensor object that can be used on the client side
 * 
 * @author roehrdor
 */
public class PSensor implements android.os.Parcelable {
	//
	// The ID, name and bluetooth id of the sensor
	//
	private final int ID;
	private String displayedSensorName = null;
	private String bluetoothID = "";
	
	//
	// Settings for the Sensor
	//		
	private boolean isInternalSensor = false;
	private boolean isEnabled = true;
    private int sampleRate = 0;
    private int savePeriod = 0;
    private float smoothness = 0.0f;
    private SensorType sensorType = null;    
    private GraphType graphType = null;
    private MeasurementUnits rawDataMeasurementUnit = null;
    private MeasurementSystems rawDataMeasurementSystem = null;
    private MeasurementUnits displayedMeasurementUnit = null;
    private MeasurementSystems displayedMeasurementSystem = null;
	
	//
	// The gained sensor data
	//
	private java.util.Vector<SensorData> rawData = new java.util.Vector<SensorData>();
	
	
	
	// =============================================================
	//
	// Constructors
	//
	// =============================================================
	/**
	 * Private constructor, this one shall never be used
	 * 
	 * @throws IllegalAccessError
	 *             if this constructor is called
	 */
	@SuppressWarnings({"unused"})
	private PSensor() {
		throw new IllegalAccessError("This constructor shall never be used");
	}
	
	//<nsdk>
	/**
	 * Create a new Sensor object with the given name and the given ID
	 * 
	 * @param ID
	 *            the given ID
	 * @param displayedSensorName
	 *            the displayed SensorName
	 */
	public PSensor(int ID, String displayedSensorName) {
		this.ID = ID;
		this.displayedSensorName = displayedSensorName;
	}

	/**
	 * Create a new Sensor object with the given parameters
	 * 
	 * @param ID
	 *            sensor id
	 * @param displayedSensorName
	 *            the displayed sensor name
	 * @param bluetoothID
	 *            the bluetooth id
	 * @param sampleRate
	 *            the sample rate
	 * @param savePeriod
	 *            the save period
	 * @param smoothness
	 *            the smoothness
	 * @param sensorType
	 *            the sensor type
	 * @param graphType
	 *            the graph type
	 * @param rawDataMeasurementUnits
	 *            the raw data measurement unit
	 * @param rawDataMeasurementSystems
	 *            the raw data measurement system
	 * @param displayedMeasurementUnits
	 *            the displayed data measurement unit
	 * @param displayedMeasurementSystems
	 *            the displayed measurement system
	 */
	public PSensor(int ID, String displayedSensorName, String bluetoothID,
			int sampleRate, int savePeriod, float smoothness,
			SensorType sensorType, GraphType graphType,
			MeasurementUnits rawDataMeasurementUnits,
			MeasurementSystems rawDataMeasurementSystems,
			MeasurementUnits displayedMeasurementUnits,
			MeasurementSystems displayedMeasurementSystems) {
		this.ID = ID;
		this.displayedSensorName = displayedSensorName;
		this.bluetoothID = bluetoothID;
		this.sampleRate = sampleRate;
		this.savePeriod = savePeriod;
		this.smoothness = smoothness;
		this.sensorType = sensorType;
		this.graphType = graphType;
		this.rawDataMeasurementUnit = rawDataMeasurementUnits;
		this.rawDataMeasurementSystem = rawDataMeasurementSystems;
		this.displayedMeasurementUnit = displayedMeasurementUnits;
		this.displayedMeasurementSystem = displayedMeasurementSystems;
	}
	//</nsdk>
	
	
	
	// =============================================================
	//
	// Functions
	//
	// =============================================================
	/**
	 * Get the unique Sensor ID
	 * 
	 * @return the unique Sensor ID
	 */
	public int getID() {
		return this.ID;
	}
	
	/**
	 * Check whether the given Sensor is an internal sensor
	 * 
	 * @return true if the sensor is an internal sensor
	 */
	public boolean isInternalSensor() {
		return this.isInternalSensor;		
	}	
	
	/**
	 * Get the bluetooth ID tag from the sensor
	 * 
	 * @return the bluetooth id
	 */
	public String getBluetoothID() {
		return this.bluetoothID;
	}

    /**
     * Get the last requested raw data again. This function works without refreshing anything and
     * shall be used it the user wants to access the same data twice
     *
     * @return the cached list of sensor data
     */
    public java.util.Vector<SensorData> getLastRawData() {
        return this.rawData;
    }

    /**
     * Get the current available raw data from this Sensor
     *
     * @return the current available rawData
     */
    public java.util.Vector<SensorData> getRawData() {
        PSensorData pd = APIFunctions.SENSORS_SENSOR_getRawData(this.ID);
        if(pd != null) {
            this.rawData = pd.toSensorDataList();
            return this.rawData;
        } else
            return null;
    }

    /**
     * Get raw data fro the given time stamp. If data one second before and after the given time
     * shall be included as well the plusMinusOneSecond flag shall be set. Note to easily convert
     * between a unix time stamp and the {@link java.util.Date} object one can use the provided
     * {@link de.unistuttgart.vis.wearable.os.utils.Utils#unixToDate(int)} function.
     *
     * @param time               the time to search raw data for
     * @param plusMinusOneSecond in case this flag is set the data recorded one second earlier and
     *                           later will be included as well
     * @return a vector containing the data sets that meet the requirements
     */
    public java.util.Vector<SensorData> getRawData(java.util.Date time, boolean plusMinusOneSecond) {
        PSensorData pd = APIFunctions.SENSORS_SENSOR_getRawDataIB(this.ID, Utils.dateToUnix(time), plusMinusOneSecond);
        if(pd != null) {
            this.rawData = pd.toSensorDataList();
            return this.rawData;
        } else
            return null;
    }

    /**
     * Get all data that has been recorded from the begin time stamp on but before the end time stamp
     *
     * @param begin the begin time stamp
     * @param end   the end time stamp
     * @return a vector containing the data sets that meet the requirements
     */
    public java.util.Vector<SensorData> getRawData(java.util.Date begin, java.util.Date end) {
        PSensorData pd = APIFunctions.SENSORS_SENSOR_getRawDataII(this.ID, Utils.dateToUnix(begin), Utils.dateToUnix(end));
        if(pd != null) {
            this.rawData = pd.toSensorDataList();
            return this.rawData;
        } else
            return null;
    }

	
	// =====================================================================
	//
	// Getter and Setter functions	
	// 
	// =====================================================================
	/**
	 * @return the isEnabled
	 */
	public boolean isEnabled() {
		this.isEnabled = APIFunctions.SENSORS_SENSOR_isEnabled(this.ID); 
		return this.isEnabled;
	}

	/**
	 * @param isEnabled the isEnabled to set
	 */
	public void setEnabled(boolean isEnabled) {		
		this.isEnabled = isEnabled;
		APIFunctions.SENSORS_SENSOR_setEnabled(this.ID, isEnabled);
	}

	/**
	 * @return the displayedSensorName
	 */
	public String getDisplayedSensorName() {
		this.displayedSensorName = APIFunctions.SENSORS_SENSOR_getDisplayedSensorName(this.ID);
		return this.displayedSensorName;
	}

	/**
	 * @param displayedSensorName the displayedSensorName to set
	 */
	public void setDisplayedSensorName(String displayedSensorName) {
		this.displayedSensorName = displayedSensorName;
		APIFunctions.SENSORS_SENSOR_setDisplayedSensorName(this.ID, displayedSensorName);
	}

	/**
	 * @return the sampleRate
	 */
	public int getSampleRate() {
		this.sampleRate = APIFunctions.SENSORS_SENSOR_getSampleRate(this.ID);
		return this.sampleRate;
	}

	/**
	 * @param sampleRate the sampleRate to set
	 */
	public void setSampleRate(int sampleRate) {
		this.sampleRate = sampleRate;
		APIFunctions.SENSORS_SENSOR_setSampleRate(this.ID, sampleRate);
	}

	/**
	 * @return the savePeriod
	 */
	public int getSavePeriod() {
		this.savePeriod = APIFunctions.SENSORS_SENSOR_getSavePeriod(this.ID);
		return savePeriod;
	}

	/**
	 * @param savePeriod the savePeriod to set
	 */
	public void setSavePeriod(int savePeriod) {
		this.savePeriod = savePeriod;
		APIFunctions.SENSORS_SENSOR_setSavePeriod(this.ID, savePeriod);
	}

	/**
	 * @return the smoothness
	 */
	public float getSmoothness() {
		this.smoothness = APIFunctions.SENSORS_SENSOR_getSmoothness(this.ID);
		return smoothness;
	}

	/**
	 * @param smoothness the smoothness to set
	 */
	public void setSmoothness(float smoothness) {		
		this.smoothness = smoothness;
		APIFunctions.SENSORS_SENSOR_setSmoothness(this.ID, smoothness);
	}	
	
	/**
	 * @return the sensorType
	 */
	public SensorType getSensorType() {
		int ordinal = APIFunctions.SENSORS_SENSOR_getSensorType(this.ID);
		if(ordinal == Constants.ENUMERATION_NULL)
			this.sensorType = null;
		else
			this.sensorType = SensorType.values()[ordinal];
		return sensorType;
	}

	/**
	 * @param sensorType the sensorType to set
	 */
	public void setSensorType(SensorType sensorType) {
		this.sensorType = sensorType;
		int ordinal = sensorType != null ? sensorType.ordinal() : Constants.ENUMERATION_NULL;
		APIFunctions.SENSORS_SENSOR_setSensorType(this.ID, ordinal);
	}

	/**
	 * @return the graphType
	 */
	public GraphType getGraphType() {
		int ordinal = APIFunctions.SENSORS_SENSOR_getGraphType(this.ID);
		if(ordinal == Constants.ENUMERATION_NULL)
			this.graphType = null;
		else
			this.graphType = GraphType.values()[ordinal];
		return graphType;
	}

	/**
	 * @param graphType the graphType to set
	 */
	public void setGraphType(GraphType graphType) {
		this.graphType = graphType;
		int ordinal = graphType != null ? graphType.ordinal() : Constants.ENUMERATION_NULL;
		APIFunctions.SENSORS_SENSOR_setGraphType(this.ID, ordinal);
	}

	/**
	 * @return the rawDataMeasurementUnit
	 */
	public MeasurementUnits getRawDataMeasurementUnit() {
		int ordinal = APIFunctions.SENSORS_SENSOR_getRawDataMeasurementUnit(this.ID);
		if(ordinal == Constants.ENUMERATION_NULL)
			this.rawDataMeasurementUnit = null;
		else
			this.rawDataMeasurementUnit = MeasurementUnits.values()[ordinal];
		return rawDataMeasurementUnit;
	}

	/**
	 * @param rawDataMeasurementUnit the rawDataMeasurementUnit to set
	 */
	public void setRawDataMeasurementUnit(MeasurementUnits rawDataMeasurementUnit) {
		this.rawDataMeasurementUnit = rawDataMeasurementUnit;
		int ordinal = rawDataMeasurementUnit != null ? rawDataMeasurementUnit.ordinal() : Constants.ENUMERATION_NULL;
		APIFunctions.SENSORS_SENSOR_setRawDataMeasurementUnit(this.ID, ordinal);
	}

	/**
	 * @return the rawDataMeasurementSystem
	 */
	public MeasurementSystems getRawDataMeasurementSystem() {
		int ordinal = APIFunctions.SENSORS_SENSOR_getRawDataMeasurementSystem(this.ID);
		if(ordinal == Constants.ENUMERATION_NULL)
			this.rawDataMeasurementSystem = null;
		else
			this.rawDataMeasurementSystem = MeasurementSystems.values()[ordinal];
		return rawDataMeasurementSystem;
	}

	/**
	 * @param rawDataMeasurementSystem the rawDataMeasurementSystem to set
	 */
	public void setRawDataMeasurementSystem(MeasurementSystems rawDataMeasurementSystem) {
		this.rawDataMeasurementSystem = rawDataMeasurementSystem;
		int ordinal = rawDataMeasurementSystem != null ? rawDataMeasurementSystem.ordinal() : Constants.ENUMERATION_NULL;
		APIFunctions.SENSORS_SENSOR_setRawDataMeasurementSystem(this.ID, ordinal);
	}

	/**
	 * @return the displayedMeasurementUnit
	 */
	public MeasurementUnits getDisplayedMeasurementUnit() {
		int ordinal = APIFunctions.SENSORS_SENSOR_getDisplayedMeasurementUnit(this.ID);
		if(ordinal == Constants.ENUMERATION_NULL)
			this.displayedMeasurementUnit = null;
		else
			this.displayedMeasurementUnit = MeasurementUnits.values()[ordinal];
		return displayedMeasurementUnit;
	}

	/**
	 * @param displayedMeasurementUnit the displayedMeasurementUnit to set
	 */
	public void setDisplayedMeasurementUnit(MeasurementUnits displayedMeasurementUnit) {
		this.displayedMeasurementUnit = displayedMeasurementUnit;
		int ordinal = displayedMeasurementUnit != null ? displayedMeasurementUnit.ordinal() : Constants.ENUMERATION_NULL;
		APIFunctions.SENSORS_SENSOR_setDisplayedMeasurementUnit(this.ID, ordinal);
	}

	/**
	 * @return the displayedMeasurementSystem
	 */
	public MeasurementSystems getDisplayedMeasurementSystem() {
		int ordinal = APIFunctions.SENSORS_SENSOR_getDisplayedMeasurementSystem(this.ID);
		if(ordinal == Constants.ENUMERATION_NULL)
			this.displayedMeasurementSystem = null;
		else
			this.displayedMeasurementSystem = MeasurementSystems.values()[ordinal];
		return displayedMeasurementSystem;
	}

	/**
	 * @param displayedMeasurementSystem the displayedMeasurementSystem to set
	 */
	public void setDisplayedMeasurementSystem(MeasurementSystems displayedMeasurementSystem) {
		this.displayedMeasurementSystem = displayedMeasurementSystem;
		int ordinal = displayedMeasurementSystem != null ? displayedMeasurementSystem.ordinal() : Constants.ENUMERATION_NULL;
		APIFunctions.SENSORS_SENSOR_setDisplayedMeasurementSystem(this.ID, ordinal);
	}


	
	// =====================================================================
	//
	// Parcel procedures	
	// 
	// =====================================================================
	//
	// Creator Object that is used to transmit objects
	//
	public static final android.os.Parcelable.Creator<PSensor> CREATOR = new android.os.Parcelable.Creator<PSensor>() {
		@Override
		public PSensor createFromParcel(android.os.Parcel source) {
			PSensor ret = new PSensor(source.readInt(), source.readString());
			byte b = source.readByte();
			int o = 0;;
			ret.isInternalSensor = (b & 0x1) != 0x0;
			ret.isEnabled = (b & 0x2) != 0x0;
			ret.sampleRate = source.readInt();
			ret.savePeriod = source.readInt();
			ret.smoothness = source.readInt();
			ret.sensorType = (o = source.readInt()) != Constants.ENUMERATION_NULL ? SensorType.values()[o] : null;
			ret.graphType = (o = source.readInt()) != Constants.ENUMERATION_NULL ? GraphType.values()[o] : null;
			ret.rawDataMeasurementUnit = (o = source.readInt()) != Constants.ENUMERATION_NULL ? MeasurementUnits.values()[o] : null;
			ret.rawDataMeasurementSystem = (o = source.readInt()) != Constants.ENUMERATION_NULL ? MeasurementSystems.values()[o] : null;
			ret.displayedMeasurementUnit = (o = source.readInt()) != Constants.ENUMERATION_NULL ? MeasurementUnits.values()[o] : null;
			ret.displayedMeasurementSystem = (o = source.readInt()) != Constants.ENUMERATION_NULL ? MeasurementSystems.values()[o] : null;
			return ret;
		}

		@Override
		public PSensor[] newArray(int size) {
			return new PSensor[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(android.os.Parcel dest, int flags) {
		dest.writeInt(this.ID);
		dest.writeString(this.displayedSensorName);		
		byte compressedInternalEnabled = 0x0;
		compressedInternalEnabled |= this.isInternalSensor ? (byte)0x1 : (byte)0x0;
		compressedInternalEnabled |= this.isEnabled ? (byte)0x2 : (byte)0x0;
		dest.writeByte(compressedInternalEnabled);
		dest.writeInt(this.sampleRate);
		dest.writeInt(this.savePeriod);
		dest.writeFloat(this.smoothness);
		dest.writeInt(this.sensorType == null ? Constants.ENUMERATION_NULL : this.sensorType.ordinal());
		dest.writeInt(this.graphType == null ? Constants.ENUMERATION_NULL : this.graphType.ordinal());
		dest.writeInt(this.rawDataMeasurementUnit == null ? Constants.ENUMERATION_NULL : this.rawDataMeasurementUnit.ordinal());
		dest.writeInt(this.rawDataMeasurementSystem == null ? Constants.ENUMERATION_NULL : this.rawDataMeasurementSystem.ordinal());
		dest.writeInt(this.displayedMeasurementUnit == null ? Constants.ENUMERATION_NULL : this.displayedMeasurementUnit.ordinal());
		dest.writeInt(this.displayedMeasurementSystem == null ? Constants.ENUMERATION_NULL : this.displayedMeasurementSystem.ordinal());
	}
}