package com.example.naftech.autopilotbicycle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;


public class MasterApp extends AppCompatActivity {

    public static String state = "";
    public static String InputData = "InputData";
    public static String[] InData = new String[8];
    public static int size = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.master_app);

        Toolbar mytoolbar = (Toolbar) findViewById(R.id.MainToolBar);
        setSupportActionBar(mytoolbar);

        final EditText K1 = (EditText) findViewById(R.id.K1EditText);
        final EditText K2 = (EditText) findViewById(R.id.K2EditText);
        final EditText K3 = (EditText) findViewById(R.id.K3EditText);
        final EditText K4 = (EditText) findViewById(R.id.K4EditText);
        final EditText Velocity = (EditText) findViewById(R.id.VelocityEditText);
        final EditText SampleT = (EditText) findViewById(R.id.SampleValueEditText);

        final Switch DMS = (Switch) findViewById(R.id.DMSwitch); //Drive motor switch
        final Switch SMS = (Switch) findViewById(R.id.SMSwitch);  //Steer motor switch

        Button startbtn = (Button) findViewById(R.id.StartButton);

        ///Launches the plotapp once a button is clicked
        //When Start button is clicked
        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(getApplicationContext(), PlotApp.class);
                InData[0] = K1.getText().toString();//K1.getText().toString();//Double.parseDouble(K1.getText().toString());
                InData[1] = K2.getText().toString();//Double.parseDouble(K2.getText().toString());
                InData[2] = K3.getText().toString();//Double.parseDouble(K3.getText().toString());
                InData[3] = K4.getText().toString();//Double.parseDouble(K4.getText().toString());
                InData[4] = Velocity.getText().toString();//Double.parseDouble(Velocity.getText().toString());//To be converted to the appropriate 12bits value that
                //gives the desired voltage before sending to Ardiuno yun
                InData[5] = SampleT.getText().toString();//Double.parseDouble(SampleT.getText().toString());
                InData[6] = DMS.getText().toString();
                InData[7] = SMS.getText().toString();
//				intent.putExtra( sK1, Float.parseFloat(K1.getText().toString()) );
//				intent.putExtra( sK2, Float.parseFloat(K2.getText().toString()) );
//				intent.putExtra( sK3, Float.parseFloat(K3.getText().toString()) );
//				intent.putExtra( sK4, Float.parseFloat(K4.getText().toString()) );
//
//				intent.putExtra( sVel, Float.parseFloat(Velocity.getText().toString()) );
//
//				intent.putExtra( sT, Float.parseFloat(SampleT.getText().toString()) );
                state = "start";
                size = 8;
                //intent.putExtra(InputData, InData);
                startActivity(intent);
            }
        });
    }//End of onCreate

    //////////////////////////    Saving Activity State Temporarily   /////////////////////////////
//    @Override
//    public void onSaveInstanceState(Bundle savedInstanceState)
//    {
//        // Store UI state to the savedInstanceState.
//        // This bundle will be passed to onCreate on next call.  EditText txtName = (EditText)findViewById(R.id.txtName);
//        String strName = txtName.getText().toString();
//
//        EditText txtEmail = (EditText)findViewById(R.id.txtEmail);
//        String strEmail = txtEmail.getText().toString();
//
//        CheckBox chkTandC = (CheckBox)findViewById(R.id.chkTandC);
//        boolean blnTandC = chkTandC.isChecked();
//
//        savedInstanceState.putString(“Name”, strName);
//        savedInstanceState.putString(“Email”, strEmail);
//        savedInstanceState.putBoolean(“TandC”, blnTandC);
//
//        super.onSaveInstanceState(savedInstanceState);
//    }
    ///////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////    Menu Options andActions   ///////////////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mMenuInflater = getMenuInflater();
        mMenuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem Item) {
        if (Item.getItemId() == R.id.DataEntry) {
            Toast.makeText(MasterApp.this, "You are in the DataEntry page Already", Toast.LENGTH_SHORT)
                    .show();
//            Intent intent = new Intent(getApplicationContext(), MasterApp.class);
//            startActivity(intent);
        } /*else {
//            Toast.makeText(MasterApp.this, "Torque Test page is on the way", Toast.LENGTH_SHORT)
//                    .show();
            Intent intent = new Intent(getApplicationContext(), TorqueTestApp.class);
            startActivity(intent);
        }*/
        return true;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////
}
