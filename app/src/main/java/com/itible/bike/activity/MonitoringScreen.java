package com.itible.bike.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.itible.bike.R;
import com.itible.bike.enums.Places;
import com.itible.bike.util.ActivityHelper;
import com.itible.bike.util.Util;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MonitoringScreen extends Activity {

    private static final String TAG = "MonitoringScreen";
    private final boolean mIsUserInitiatedDisconnect = false;
    private int mMaxChars = 50000;//Default
    private UUID mDeviceUUID;
    private BluetoothSocket mBTSocket;
    private ReadInput mReadThread = null;
    //    private TextView mTxtReceive;
//    private Button mBtnClearInput;
//    private ScrollView scrollView;
//    private CheckBox chkScroll;
//    private CheckBox chkReceiveText;
    private boolean mIsBluetoothConnected = false;
    private BluetoothDevice mDevice;
    private ProgressDialog progressDialog;
    private Instant lastGpsCoordsTime;
    private Button btnStart, btnRight, btnLeft, btnFront, btnSave;
    private WebView webView;
    private String currentLatitude, currentLongitude, finishLatitude, finishLongitude, finalUrl;
    private List<String> latitudes, longitudes;
    private Spinner spinnerStart, spinnerFinish;
    private TextView txtResults;
    private long startTime, duration;
    private int movements;
    public static final String FINAL_URL = "final_url";
    public static final String FINAL_DURATION = "final_duration";
    public static final String FINAL_MOVEMENTS = "final_movements";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_monitoring_screen);
        setContentView(R.layout.activity_map);
        ActivityHelper.initialize(this);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        mDevice = b.getParcelable(MainActivity.DEVICE_EXTRA);
        mDeviceUUID = UUID.fromString(b.getString(MainActivity.DEVICE_UUID));
        mMaxChars = b.getInt(MainActivity.BUFFER_SIZE);
        Log.d(TAG, "Ready");
        lastGpsCoordsTime = Instant.now();
//        mTxtReceive = findViewById(R.id.txtReceive);
//        chkScroll = findViewById(R.id.chkScroll);
//        chkReceiveText = findViewById(R.id.chkReceiveText);
//        scrollView = findViewById(R.id.viewScroll);
//        mBtnClearInput = findViewById(R.id.btnClearInput);
//        mTxtReceive.setMovementMethod(new ScrollingMovementMethod());
//
//        mBtnClearInput.setOnClickListener(arg0 -> mTxtReceive.setText(""));


        btnRight = findViewById(R.id.btnRight);
        btnLeft = findViewById(R.id.btnLeft);
        btnFront = findViewById(R.id.btnFront);
        btnSave = findViewById(R.id.btnSave);
        txtResults = findViewById(R.id.txtResults);

        btnStart = findViewById(R.id.btnStart);
        webView = findViewById(R.id.wv_browser);
        spinnerStart = findViewById(R.id.spinnerStart);
        spinnerFinish = findViewById(R.id.spinnerFinish);

        latitudes = new ArrayList<>();
        longitudes = new ArrayList<>();
        setupElements();

    }

    /**
     * Setup UI elements
     */
    private void setupElements() {
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new HelloWebViewClient());

        List<Places> places = Arrays.asList(Places.values());
        ArrayAdapter<Places> adapter = new ArrayAdapter<>(MonitoringScreen.this, android.R.layout.simple_spinner_item, places);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerStart.setAdapter(adapter);
        spinnerStart.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                        if (places.get(position).equals(Places.SELECT_ONE)) {
                            Toast.makeText(getApplicationContext(), "Please pick some place!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Start set to " + places.get(position).name(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                }
        );

        spinnerFinish.setAdapter(adapter);
        spinnerFinish.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                        Places pickedPlace = places.get(position);
                        if (pickedPlace.equals(Places.SELECT_ONE)) {
                            Toast.makeText(getApplicationContext(), "Please pick some place!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Finish set to " + pickedPlace.name(), Toast.LENGTH_SHORT).show();
                            finishLatitude = pickedPlace.getLatitude();
                            finishLongitude = pickedPlace.getLongitude();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                }
        );


        btnStart.setOnClickListener(v -> {
            if (spinnerStart.getSelectedItem().equals(Places.SELECT_ONE)) {
                Toast.makeText(getApplicationContext(), "Please select the starting place!", Toast.LENGTH_LONG).show();
            } else if (spinnerFinish.getSelectedItem().equals(Places.SELECT_ONE)) {
                Toast.makeText(getApplicationContext(), "Please select the finish place!", Toast.LENGTH_LONG).show();
            } else {
                webView.setVisibility(View.VISIBLE);
                txtResults.setVisibility(View.GONE);
                webView.loadUrl(Places.valueOf(spinnerStart.getSelectedItem().toString()).getUrl());
                startTime = System.nanoTime();
            }
        });

        btnFront.setOnClickListener(v -> simulateClick(550, 1800));
        btnSave.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SaveTrainingActivity.class);
            intent.putExtra(FINAL_URL, finalUrl);
            intent.putExtra(FINAL_DURATION, duration);
            intent.putExtra(FINAL_MOVEMENTS, movements);
            startActivity(intent);
        });
        btnRight.setOnClickListener(v -> generateSwipeGesture("right"));
        btnLeft.setOnClickListener(v -> generateSwipeGesture("left"));
    }

    private void simulateClick(float x, float y) {
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();
        MotionEvent.PointerProperties[] properties = new MotionEvent.PointerProperties[1];
        MotionEvent.PointerProperties pp1 = new MotionEvent.PointerProperties();
        pp1.id = 0;
        pp1.toolType = MotionEvent.TOOL_TYPE_FINGER;
        properties[0] = pp1;
        MotionEvent.PointerCoords[] pointerCoords = new MotionEvent.PointerCoords[1];
        MotionEvent.PointerCoords pc1 = new MotionEvent.PointerCoords();
        pc1.x = x;
        pc1.y = y;
        pc1.pressure = 1;
        pc1.size = 1;
        pointerCoords[0] = pc1;
        MotionEvent motionEvent = MotionEvent.obtain(downTime, eventTime,
                MotionEvent.ACTION_DOWN, 1, properties,
                pointerCoords, 0, 0, 1, 1, 0, 0, 0, 0);
        dispatchTouchEvent(motionEvent);

        motionEvent = MotionEvent.obtain(downTime, eventTime,
                MotionEvent.ACTION_UP, 1, properties,
                pointerCoords, 0, 0, 1, 1, 0, 0, 0, 0);
        dispatchTouchEvent(motionEvent);
        movements++;
    }

    private void getUrl() {
        Log.i(TAG, "latitude = " + currentLatitude);
        Log.i(TAG, "longitude = " + currentLongitude);
    }

    public void generateSwipeGesture(String direction) {
        float xx1 = 0, xx2 = 0;
        if (direction.equals("left")) {
            xx1 = 600;
            xx2 = 1000;
        } else if (direction.equals("right")) {
            xx1 = 600;
            xx2 = 200;
        }
        PointF startPoint1 = new PointF(xx1, 1400);
        PointF startPoint2 = new PointF(xx1, 1400);
        PointF endPoint1 = new PointF(xx2, 1400);
        PointF endPoint2 = new PointF(xx2, 1400);
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();

        float startX1 = startPoint1.x;
        float startY1 = startPoint1.y;
        float startX2 = startPoint2.x;
        float startY2 = startPoint2.y;

        float endX1 = endPoint1.x;
        float endY1 = endPoint1.y;
        float endX2 = endPoint2.x;
        float endY2 = endPoint2.y;

        // pointer 1
        float x1 = startX1;
        float y1 = startY1;

        // pointer 2
        float x2 = startX2;
        float y2 = startY2;

        MotionEvent.PointerCoords[] pointerCoords = new MotionEvent.PointerCoords[2];
        MotionEvent.PointerCoords pc1 = new MotionEvent.PointerCoords();
        MotionEvent.PointerCoords pc2 = new MotionEvent.PointerCoords();
        pc1.x = x1;
        pc1.y = y1;
        pc1.pressure = 1;
        pc1.size = 1;
        pc2.x = x2;
        pc2.y = y2;
        pc2.pressure = 1;
        pc2.size = 1;
        pointerCoords[0] = pc1;
        pointerCoords[1] = pc2;

        MotionEvent.PointerProperties[] pointerProperties = new MotionEvent.PointerProperties[2];
        MotionEvent.PointerProperties pp1 = new MotionEvent.PointerProperties();
        MotionEvent.PointerProperties pp2 = new MotionEvent.PointerProperties();
        pp1.id = 0;
        pp1.toolType = MotionEvent.TOOL_TYPE_FINGER;
        pp2.id = 1;
        pp2.toolType = MotionEvent.TOOL_TYPE_FINGER;
        pointerProperties[0] = pp1;
        pointerProperties[1] = pp2;

        MotionEvent event;
        // send the initial touches
        event = MotionEvent.obtain(downTime, eventTime,
                MotionEvent.ACTION_DOWN, 1, pointerProperties, pointerCoords,
                0, 0, // metaState, buttonState
                1, // x precision
                1, // y precision
                0, 0, 0, 0); // deviceId, edgeFlags, source, flags
        dispatchTouchEvent(event);


        event = MotionEvent.obtain(downTime, eventTime,
                MotionEvent.ACTION_POINTER_DOWN
                        + (pp2.id << MotionEvent.ACTION_POINTER_INDEX_SHIFT),
                2, pointerProperties, pointerCoords, 0, 0, 1, 1, 0, 0, 0, 0);
        dispatchTouchEvent(event);

        int numMoves = 1;

        float stepX1 = (endX1 - startX1) / numMoves;
        float stepY1 = (endY1 - startY1) / numMoves;
        float stepX2 = (endX2 - startX2) / numMoves;
        float stepY2 = (endY2 - startY2) / numMoves;

        // send the zoom
        for (int i = 0; i < numMoves; i++) {
            eventTime += 5;
            pointerCoords[0].x += stepX1;
            pointerCoords[0].y += stepY1;
            pointerCoords[1].x += stepX2;
            pointerCoords[1].y += stepY2;

            event = MotionEvent.obtain(downTime, eventTime,
                    MotionEvent.ACTION_MOVE, 2, pointerProperties,
                    pointerCoords, 0, 0, 1, 1, 0, 0, 0, 0);
            dispatchTouchEvent(event);
        }

        event = MotionEvent.obtain(downTime, eventTime,
                MotionEvent.ACTION_UP, 2, pointerProperties,
                pointerCoords, 0, 0, 1, 1, 0, 0, 0, 0);
        dispatchTouchEvent(event);
    }

    private void msg(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        if (mBTSocket != null && mIsBluetoothConnected) {
            new DisConnectBT().execute();
        }
        Log.d(TAG, "Paused");
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (mBTSocket == null || !mIsBluetoothConnected) {
            new ConnectBT().execute();
        }
        Log.d(TAG, "Resumed");
        super.onResume();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "Stopped");
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * Reading the current GPS position by parsing streetview map URL during loading of resources
     */
    private class HelloWebViewClient extends WebViewClient {
        @Override
        public void onLoadResource(WebView view, String url) {
            Pattern p = Pattern.compile("(\\d{2}\\.\\d{7})!3d(\\d{2}\\.\\d{7})");
            Matcher m = p.matcher(url);
            if (m.find()) { // set the coordinates
                Instant now = Instant.now();
                Log.d(TAG, "Duration: " + Duration.between(lastGpsCoordsTime, now).toMillis());
                if (Duration.between(lastGpsCoordsTime, now).toMillis() > 6000) {
                    lastGpsCoordsTime = now;
                    if (longitudes.size() == 0) { // first time insert when empty
                        longitudes.add(m.group(1));
                        latitudes.add(m.group(2));
                    } else { // check for getting rid off bugy GPS value.. next value should be closer than 5,5km.. -> 0.05
                        if (shouldSaveGpsPosition(m)) {
                            longitudes.add(m.group(1));
                            latitudes.add(m.group(2));
                        }
                    }
                }

                currentLongitude = m.group(1);
                currentLatitude = m.group(2);
                if (currentLatitude.equals(finishLatitude) && currentLongitude.equals(finishLongitude)) {
                    longitudes.add(currentLongitude);
                    latitudes.add(currentLatitude);
//                    webView.setVisibility(View.GONE);
                    finalUrl = Util.createRouteUrl(longitudes, latitudes);
                    webView.loadUrl(finalUrl);
                    txtResults.setVisibility(View.VISIBLE);
                    duration = (System.nanoTime() - startTime) / 1_000_000_000;
                    txtResults.setText("You have successfully finish the track! \n " +
                            "Duration = " + Util.formatDuration(duration) + "\n " +
                            "Number of movements = " + movements
                    );
                }
            }
        }

    }

    private boolean shouldSaveGpsPosition(Matcher m) {
        return new BigDecimal(longitudes.get(longitudes.size() - 1)).subtract(new BigDecimal(m.group(1))).abs().compareTo(new BigDecimal("0.05")) < 0 &&
                new BigDecimal(latitudes.get(latitudes.size() - 1)).subtract(new BigDecimal(m.group(2))).abs().compareTo(new BigDecimal("0.05")) < 0 && !longitudes.get(longitudes.size() - 1).equals(m.group(1)) && !latitudes.get(latitudes.size() - 1).equals(m.group(2));
    }

    private class ReadInput implements Runnable {

        private final Thread t;
        private boolean bStop = false;

        public ReadInput() {
            t = new Thread(this, "Input Thread");
            t.start();
        }

        public boolean isRunning() {
            return t.isAlive();
        }

        @Override
        public void run() {
            InputStream inputStream;

            try {
                inputStream = mBTSocket.getInputStream();
                while (!bStop) {
                    byte[] buffer = new byte[256];
                    if (inputStream.available() > 0) {
                        inputStream.read(buffer);
                        int i;
                        for (i = 0; i < buffer.length && buffer[i] != 0; i++) {
                        }
                        final String strInput = new String(buffer, 0, i);
                        Log.i(TAG, strInput);
                        if (strInput.trim().equals("5") || strInput.trim().equals("6")) {
                            simulateClick(600, 1400);
                        }
                    }
                    Thread.sleep(500);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

        }

        public void stop() {
            bStop = true;
        }

    }

    private class DisConnectBT extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {

            if (mReadThread != null) {
                mReadThread.stop();
                while (mReadThread.isRunning())
                    ; // Wait until it stops
                mReadThread = null;

            }

            try {
                mBTSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            mIsBluetoothConnected = false;
            if (mIsUserInitiatedDisconnect) {
                finish();
            }
        }

    }

    private class ConnectBT extends AsyncTask<Void, Void, Void> {
        private boolean mConnectSuccessful = true;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MonitoringScreen.this, "Hold on", "Connecting");// http://stackoverflow.com/a/11130220/1287554
        }

        @Override
        protected Void doInBackground(Void... devices) {

            try {
                if (mBTSocket == null || !mIsBluetoothConnected) {
                    mBTSocket = mDevice.createInsecureRfcommSocketToServiceRecord(mDeviceUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    mBTSocket.connect();
                }
            } catch (IOException e) {
// Unable to connect to device
                e.printStackTrace();
                mConnectSuccessful = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

//            if (!mConnectSuccessful) {  //TODO odkomentuj
            if (false) {
                Toast.makeText(getApplicationContext(), "Could not connect to device. Is it a Serial device? Also check if the UUID is correct in the settings", Toast.LENGTH_LONG).show();
                finish();
            } else {
                msg("Connected to device");
                mIsBluetoothConnected = true;
                mReadThread = new ReadInput(); // Kick off input reader
            }

            progressDialog.dismiss();
        }

    }
}
