package cypher.test;


/**
 * Listener for Bluetooth events involving byte arrays in addition to Strings.
 *
 * @author Macro Yau
 */
public interface BluetoothSerialRawListener extends cypher.test.BluetoothSerialListener {

    /**
     * Specified message is read from the serial port.
     *
     * @param bytes The byte array read.
     */
    void onBluetoothSerialReadRaw(byte[] bytes);

    /**
     * Specified message is written to the serial port.
     *
     * @param bytes The byte array written.
     */
    void onBluetoothSerialWriteRaw(byte[] bytes);

}
