package jp.tetra2000.proximity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;

import java.util.HashMap;
import java.util.Map;

public class ProximitySensorModule extends ReactContextBaseJavaModule {
    private ReactApplicationContext reactContext;

    private SensorManager sensorManager;
    private Sensor proximitySensor;

    public ProximitySensorModule(ReactApplicationContext reactContext) {
        super(reactContext);

        this.reactContext = reactContext;

        sensorManager = (SensorManager) reactContext.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager == null) return;

        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
    }

    @Override
    public String getName() {
        return "ProximitySensor";
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();

        return constants;
    }

    @ReactMethod
    public void startMonitor() {
        if (sensorManager == null || proximitySensor == null) return;

        sensorManager.registerListener(sensorEventListener, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @ReactMethod
    public void stopMonitor() {
        if (sensorManager == null || proximitySensor == null) return;

        sensorManager.unregisterListener(sensorEventListener);
    }

    private final SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            WritableMap map = Arguments.createMap();
            map.putDouble("proximity", event.values[0]);
            reactContext
                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit("sensorChanged",map);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
}
