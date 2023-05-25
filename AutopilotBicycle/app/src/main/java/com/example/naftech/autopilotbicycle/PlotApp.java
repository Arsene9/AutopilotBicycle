package com.example.naftech.autopilotbicycle;

/**
 * Dynamic XYPlot
 * Based off the example of Dynamic Plot in the AndroidPlot web site (http://androidplot.com/docs/a-dynamic-xy-plot/)
 * and the Dynamically plotted sensor data example codes (http://androidplot.com/docs/dynamically-plotting-sensor-data/)
 */

import static com.example.naftech.autopilotbicycle.MasterApp.size;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidplot.util.PixelUtils;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYStepMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicBoolean;


public class PlotApp extends AppCompatActivity {

	private final static String TAG = ">==< ArduinoYun >==<";    //Label for the log 
	private final static String ARDUINO_IP_ADDRESS = "192.168.240.1";//"10.0.132.208";   //IP Address of the Arduino yun
	private final static int PORT = 4444;   //Port through which socket communication occurs
	
	public float[] InData = new float[6];	//Contains the various data entered by the user [K1 K2 K3 K4 Vel T]
//	public float[] K = new float[4];u
//	public float Vel;
//	public float T;
	
 
    private static final int Size = 200; //Defines the number of points to be plotted
    private XYPlot RollPlot = null;
    private XYPlot SteerPlot = null;
	private XYPlot RollRatePlot = null;
	private XYPlot SteerRatePlot = null;
    private SimpleXYSeries Roll = null;
    private SimpleXYSeries Steer = null;
	private SimpleXYSeries RollRate = null;
	private SimpleXYSeries SteerRate = null;

	public Button StopBtn, AbortBtn;
	public TextView StatText;
	public boolean Stop = false,
				   Abort = false;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
 
        // android boilerplate stuff
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plotapp);//dynamicxyplot);
        
        //Populate the InputData array with entry from user on Activity 1
        InData = getIntent().getFloatArrayExtra(MasterApp.InputData);

		// get handles to our View defined in layout.xml:
		Toolbar mytoolbar = (Toolbar) findViewById(R.id.MainToolBar);
		setSupportActionBar(mytoolbar);

		StopBtn = (Button) findViewById(R.id.StopButton);
		AbortBtn = (Button) findViewById(R.id.AbortButton);

		StatText = (TextView) findViewById(R.id.StatusTextView);

        //Roll and Steer plots setup
        RollPlot = (XYPlot) findViewById(R.id.dynamicXYPlot1);
        Roll = new SimpleXYSeries("Roll Angle");
        Roll.useImplicitXVals();

        SteerPlot = (XYPlot) findViewById(R.id.dynamicXYPlot2);
        Steer = new SimpleXYSeries("Steer Angle");
        Steer.useImplicitXVals();

		RollRatePlot = (XYPlot) findViewById(R.id.dynamicXYPlot3);
		RollRate = new SimpleXYSeries("Roll Angle Rate");
		RollRate.useImplicitXVals();

		SteerRatePlot = (XYPlot) findViewById(R.id.dynamicXYPlot4);
		SteerRate = new SimpleXYSeries("Steer Angle Rate");
		SteerRate.useImplicitXVals();

		//y-axis setup
        RollPlot.setRangeStepMode(XYStepMode.INCREMENT_BY_VAL);
        SteerPlot.setRangeStepMode(XYStepMode.INCREMENT_BY_VAL);
		RollRatePlot.setRangeStepMode(XYStepMode.INCREMENT_BY_VAL);
		SteerRatePlot.setRangeStepMode(XYStepMode.INCREMENT_BY_VAL);
        RollPlot.setRangeBoundaries(-1, 1, BoundaryMode.FIXED);
        SteerPlot.setRangeBoundaries(-1, 1, BoundaryMode.FIXED);
		RollRatePlot.setRangeBoundaries(-2, 2, BoundaryMode.FIXED);
		SteerRatePlot.setRangeBoundaries(-2, 2, BoundaryMode.FIXED);
        //x-axis setup
//        RollPlot.setDomainStepMode(XYStepMode.INCREMENT_BY_VAL);//(XYStepMode.SUBDIVIDE, time.length);
//        SteerPlot.setDomainStepMode(XYStepMode.INCREMENT_BY_VAL);
        RollPlot.setDomainBoundaries(0, Size, BoundaryMode.FIXED);
        SteerPlot.setDomainBoundaries(0, Size, BoundaryMode.FIXED);
		RollRatePlot.setDomainBoundaries(0, Size, BoundaryMode.FIXED);
		SteerRatePlot.setDomainBoundaries(0, Size, BoundaryMode.FIXED);

		//X and Y Spacing
		RollPlot.setDomainStepValue(5);
		SteerPlot.setDomainStepValue(5);
		RollRatePlot.setDomainStepValue(5);
		SteerRatePlot.setDomainStepValue(5);
		RollPlot.setRangeStepValue(0.2);//.setTicksPerRangeLabel(3);
		SteerPlot.setRangeStepValue(0.2);//.setTicksPerRangeLabel(3);
		RollRatePlot.setRangeStepValue(0.2);//.setTicksPerRangeLabel(3);
		SteerRatePlot.setRangeStepValue(0.2);//.setTicksPerRangeLabel(3);

        //line and point setup
        LineAndPointFormatter formatter1 = new LineAndPointFormatter(Color.GREEN, null, null, null);
        LineAndPointFormatter formatter2 = new LineAndPointFormatter(Color.BLUE, null, null, null);
		LineAndPointFormatter formatter3 = new LineAndPointFormatter(Color.RED, null, null, null);
		LineAndPointFormatter formatter4 = new LineAndPointFormatter(Color.YELLOW, null, null, null);
        RollPlot.addSeries(Roll, formatter1);
        SteerPlot.addSeries(Steer, formatter2);
		RollRatePlot.addSeries(RollRate, formatter3);
		SteerRatePlot.addSeries(SteerRate, formatter4);
        formatter1.getLinePaint().setStrokeJoin(Paint.Join.ROUND);
        formatter1.getLinePaint().setStrokeWidth(2);  //Defines the thickness of the line
        formatter2.getLinePaint().setStrokeJoin(Paint.Join.ROUND);
        formatter2.getLinePaint().setStrokeWidth(2);  //Defines the thickness of the line
		formatter3.getLinePaint().setStrokeJoin(Paint.Join.ROUND);
		formatter3.getLinePaint().setStrokeWidth(2);  //Defines the thickness of the line
		formatter4.getLinePaint().setStrokeJoin(Paint.Join.ROUND);
		formatter4.getLinePaint().setStrokeWidth(2);  //Defines the thickness of the line

		//Labels
        RollPlot.setDomainLabel("Samples");
        //RollPlot.getDomainLabelWidget().pack();
        RollPlot.setRangeLabel("Angle(rad)");
        //RollPlot.getDomainLabelWidget().pack();
        SteerPlot.setDomainLabel("Samples");
        //SteerPlot.getDomainLabelWidget().pack();
        SteerPlot.setRangeLabel("Angle(rad)");
        //SteerPlot.getDomainLabelWidget().pack();
		RollRatePlot.setDomainLabel("Samples");
		//RollPlot.getDomainLabelWidget().pack();
		RollRatePlot.setRangeLabel("Angular rate(rad/sec)");
		//RollPlot.getDomainLabelWidget().pack();
		SteerRatePlot.setDomainLabel("Samples");
		//SteerPlot.getDomainLabelWidget().pack();
		SteerRatePlot.setRangeLabel("Angular rate(rad/sec)");
		//SteerPlot.getDomainLabelWidget().pack();
        
        RollPlot.setRangeValueFormat(new DecimalFormat("###.#"));
        SteerPlot.setRangeValueFormat(new DecimalFormat("###.#"));
		RollRatePlot.setRangeValueFormat(new DecimalFormat("###.#"));
		SteerRatePlot.setRangeValueFormat(new DecimalFormat("###.#"));
        
     // create a dash effect for domain and range grid lines:
        DashPathEffect dashFx = new DashPathEffect(
                new float[] {PixelUtils.dpToPix(3), PixelUtils.dpToPix(3)}, 0);
        RollPlot.getGraphWidget().getDomainGridLinePaint().setPathEffect(dashFx);
        SteerPlot.getGraphWidget().getDomainGridLinePaint().setPathEffect(dashFx);
		RollRatePlot.getGraphWidget().getDomainGridLinePaint().setPathEffect(dashFx);
		SteerRatePlot.getGraphWidget().getDomainGridLinePaint().setPathEffect(dashFx);
        RollPlot.getGraphWidget().getRangeGridLinePaint().setPathEffect(dashFx);
        SteerPlot.getGraphWidget().getRangeGridLinePaint().setPathEffect(dashFx);
		RollRatePlot.getGraphWidget().getRangeGridLinePaint().setPathEffect(dashFx);
		SteerRatePlot.getGraphWidget().getRangeGridLinePaint().setPathEffect(dashFx);

		//When Stop button is clicked
		StopBtn.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Stop = true;
			}
		});

		//When Abort button is clicked
		AbortBtn.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Abort = true;
			}
		});


	}//End OnCreat
    
	@Override
	protected void onResume() {
		mStop.set(false);
		if(sNetworkThread == null){
			sNetworkThread = new Thread(smNetworkRunnable);
			sNetworkThread.start();
		}
		super.onResume();
	}

	@Override
	protected void onPause() {
		mStop.set(true);
		if(sNetworkThread != null) sNetworkThread.interrupt();
		super.onPause();
	}

	///////////////////////////////////////    Menu Selection and Action Control    /////////////////////////
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater mMenuInflater = getMenuInflater();
		mMenuInflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem Item){
		if(Item.getItemId() == R.id.DataEntry){
			Intent intent = new Intent(getApplicationContext(), MasterApp.class);
			startActivity(intent);
		}/*else{
			Intent intent = new Intent(getApplicationContext(), TorqueTestApp.class);
			startActivity(intent);
		}*/
		return true;
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private AtomicBoolean mStop = new AtomicBoolean(false);

	private OutputStream mOutputStream = null;
	private InputStream mInputStream = null;
	
	private Socket mSocket = null;

	///Toaster generator
	public void showToast(final String toast)
	{
		runOnUiThread(new Runnable() {
			public void run()
			{
				Toast.makeText(PlotApp.this, toast, Toast.LENGTH_SHORT).show();
			}
		});
	}

	///Adjust Text in a text box
	public void DisplayText(final String text, final TextView TextBox)
	{
		TextBox.clearComposingText();
		TextBox.setText(text);
	}

	///********** Thread that sends and receives data to and from Arduino yun  ***************///
	private static Thread sNetworkThread = null;
	String prev ="";
	private final Runnable smNetworkRunnable = new Runnable() {

		@Override
		public void run() {
			log("starting snetwork thread");
			//Will make 9 attempts to connect to the Yun
			int attempts;
			for(attempts = 0; attempts < 3000; attempts++) {
				//Attempting to connect to the Arduino
				try {
					mSocket = new Socket(ARDUINO_IP_ADDRESS, PORT);
					mInputStream = mSocket.getInputStream();
					mOutputStream = mSocket.getOutputStream();
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
					mStop.set(true);
				} catch (IOException e1) {
					e1.printStackTrace();
					mStop.set(true);
				}

				try {
					log("Checking mstop");
					while(!mStop.get()){
						attempts = 0;  //Reset the number of attempts once one succeeds
						if(MasterApp.state.equals("start")){
							if(size == 2)
								mOutputStream.write(("T").getBytes());
							else
								mOutputStream.write(("S").getBytes());
							for(int i=0; i<size; i++){
								mOutputStream.write((MasterApp.InData[i]+"\n").getBytes());
								log("sent value: "+ MasterApp.InData[i]);
							}
							MasterApp.state = "";
							log("System Started");
							showToast("You just started the bike.\n" +
									"           (START)          ");
						} else if(Stop){
							mOutputStream.write(("s").getBytes());
							Stop = false;
							log("System Stopped");
							showToast("You just stopped the bike gracefully.\n" +
									"                (STOP)             ");
						} else if(Abort){
							mOutputStream.write(("A").getBytes());
							Abort = false;
							log("System Aborted");
							showToast("You just stop the bike abruptly.\n" +
									"             (ABORT)           ");
						}/*else if(MasterApp.state == "TPGen"){
							mOutputStream.write(("G").getBytes());
							MasterApp.state = "";
							log("Torque Profile Generator Initiated");
							showToast("You are now in Torque Profile Generation mode.\n" +
									"                  (TPGen)                    ");
						}*/
						///////////////////////////////////////////////////////////////////////////////
						BufferedReader r = new BufferedReader(new InputStreamReader(mInputStream));
						String Info = r.readLine();

						if(Info.equals("MSG")) {
							Info = r.readLine();
							if (!prev.equals(Info) && !Info.equals("")) {
								//showToast(Info);
								DisplayText("", StatText);
								DisplayText(Info, StatText);
								log("Received: " + Info);
								prev = Info;
							}
						}else {
							String[] data = Info.split("[ ]");
							for (int i = 1; i < 5; i++) {
								PlotUpdate(i, Double.parseDouble(data[i]));
							}
						}
						///////////////////////////////////////////////////////////////////////////////
					}//end !mStop.get()
				} catch (IOException e) {
					e.printStackTrace();
				}catch (Exception e) {
					e.printStackTrace();
				} finally{
					try {
						mStop.set(true);
						if(mOutputStream != null) mOutputStream.close();
						if(mInputStream != null) mInputStream.close();
						if(mSocket != null) mSocket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}//End of 9 Attempts to connect to Arduino
			log("returning from snetwork thread");
			attempts = 0;
			sNetworkThread = null;
		}
	};

	public void log(String s){
		Log.d(TAG, s);
	}
	
	/**Graph function
	 * This function Updates the Plots with the new values from the sensors
	 * @author Arsene
	 * When Type= 1 => Roll plot is updated
	 * When Type= 2 => Steer plot is updated
	 * When Type= 3 => RollRate plot is updated
	 * When Type= 4 => SteerRate plot is updated
	 * Data = Angle obtained from Sensor
	 */
	// Called whenever a new Sensor reading is taken.
    public void PlotUpdate(int Type, double data) {
        //Updates the plot
		if (Type == 1){
			// get rid the oldest sample in history:
			if (Roll.size() > Size) {
				Roll.removeFirst();
			}
			// add the latest history sample:
			Roll.addLast(null, data);
			// redraw the Plots:
			RollPlot.redraw();
			//log("Received Roll angle: " + data);
		}else if(Type == 2){
        	// get rid the oldest sample in history:
        	if (Steer.size() > Size){
            	Steer.removeFirst();
            }
        	// add the latest history sample:
        	Steer.addLast(null, data);
        	// redraw the Plots:
        	SteerPlot.redraw();
        	//log("Received Steer angle: " + data);
        }else if (Type == 3){
			// get rid the oldest sample in history:
			if (RollRate.size() > Size) {
				RollRate.removeFirst();
			}
			// add the latest history sample:
			RollRate.addLast(null, data);
			// redraw the Plots:
			RollRatePlot.redraw();
			//log("Received Roll angle rate: " + data);
		}else if(Type == 4){
			// get rid the oldest sample in history:
			if (SteerRate.size() > Size){
				SteerRate.removeFirst();
			}
			// add the latest history sample:
			SteerRate.addLast(null, data);
			// redraw the Plots:
			SteerRatePlot.redraw();
			//log("Received Steer angle rate: " + data);
		}else{
			log("Something Seems to have been wrong here. You are not supposed to be here");
			log("You are not supposed to be here!");
		}
    }//end PlotUpdate
}//end class PlotApp
