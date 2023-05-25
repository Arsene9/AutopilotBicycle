package com.example.naftech.autopilotbicycle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.naftech.autopilotbicycle.MasterApp.InData;
import static com.example.naftech.autopilotbicycle.MasterApp.size;
import static com.example.naftech.autopilotbicycle.MasterApp.state;

/**
 * Created by admin on 12/21/2016.
 */

public class TorqueTestApp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.torquetestapp);

        Toolbar mytoolbar = (Toolbar) findViewById(R.id.MainToolBar);
        setSupportActionBar(mytoolbar);
        final EditText SampleT = (EditText)findViewById(R.id.SampleValueEditText);
        final EditText Torque = (EditText)findViewById(R.id.Torque_Value);

        Button startbtn = (Button)findViewById(R.id.StartButton);
        Button TPGen = (Button) findViewById(R.id.TPGenButton);

        ///Launches the plotapp once a button is clicked
        TPGen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(getApplicationContext(), PlotApp.class);
                state = "TPGen";
                startActivity(intent);
            }
        });
        //When Start button is clicked
        startbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(getApplicationContext(), PlotApp.class);
                InData[0] = Torque.getText().toString();
                InData[1] = SampleT.getText().toString();
                state = "start";
                size = 2;
                //intent.putExtra(InputData, InData);
                startActivity(intent);
            }
        });
    }//end onCreat`

    ///////////////////////////////////////    Menu Selection and Action Control    /////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater mMenuInflater = getMenuInflater();
        mMenuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

   /* @Override
    public boolean onOptionsItemSelected(MenuItem Item){
        if(Item.getItemId() == R.id.TorqueTest){
            Toast.makeText(TorqueTestApp.this, "You are in the Torque Test page Already", Toast.LENGTH_SHORT)
                    .show();
        }else{
            Intent intent = new Intent(getApplicationContext(), MasterApp.class);
            startActivity(intent);
        }
        return true;
    }*/
    //////////////////////////////////////////////////////////////////////////////////////////////////////////

}//End TorqueTestApp
