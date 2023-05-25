package com.example.naftech.autopilotbicycle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Created by admin on 12/28/2016.
 */

public class MainMenu extends AppCompatActivity {
    MainMenu(){
        Toolbar mytoolbar = (Toolbar) findViewById(R.id.MainToolBar);
        setSupportActionBar(mytoolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater mMenuInflater = getMenuInflater();
        mMenuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem Item){
        if(Item.getItemId() == R.id.DataEntry){
            Toast.makeText(MainMenu.this, "You are in the DataEntry page Already", Toast.LENGTH_SHORT)
                    .show();
//            Intent intent = new Intent(getApplicationContext(), MasterApp.class);
//            startActivity(intent);
        }/*else{
            Toast.makeText(MainMenu.this, "Torque Test page is on the way", Toast.LENGTH_SHORT)
                    .show();
//            Intent intent = new Intent(getApplicationContext(), TorqueTestApp.class);
//            startActivity(intent);
        }*/
        return true;
    }
}
