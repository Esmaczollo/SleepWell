package com.example.maciek.sleepwell.MainActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.maciek.sleepwell.DataBase.DataBase;
import com.example.maciek.sleepwell.DataBase.Tables.AllDreamsTable;
import com.example.maciek.sleepwell.DataBase.Tables.UserTable;
import com.example.maciek.sleepwell.MainActivity.Fragments.AlarmFragment;
import com.example.maciek.sleepwell.MainActivity.Fragments.SettingsFragment;
import com.example.maciek.sleepwell.MainActivity.Fragments.StatisticsFragment;
import com.example.maciek.sleepwell.R;

import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private Button buttonUserTable;
    private Button buttonAllDreamsTableTable;
    private Button buttonCurrentDreamTable;
    private Button buttonCreateDataBase;

    private DataBase dataBase;
    private UserTable userTable;
    private AllDreamsTable allDreamsTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*  TEST    TEST    TEST    TEST    TEST    TEST    TEST    TEST    TEST*/

        buttonUserTable = (Button) findViewById(R.id.buttonUserTable);
        buttonAllDreamsTableTable = (Button) findViewById(R.id.buttonAllDreamsTableTable);
        buttonCurrentDreamTable = (Button) findViewById(R.id.buttonCurrentDreamTable);
        buttonCreateDataBase = (Button) findViewById(R.id.buttonCreateDataBase);

        dataBase = new DataBase(this);
        userTable = new UserTable(dataBase);
        allDreamsTable = new AllDreamsTable(dataBase);

        buttonUserTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Kliknelo buttonUserTable");
                Cursor cursor = userTable.getAllData();

                if(cursor.getCount() == 0){
                    Toast.makeText(MainActivity.this, "There is no data", Toast.LENGTH_SHORT).show();
                }

                StringBuffer buffer = new StringBuffer();

                while(cursor.moveToNext())
                {
                    buffer.append("ID :" + cursor.getString(0) + "\n");
                    buffer.append("USER_NAME :" + cursor.getString(1) + "\n\n");
                }

                System.out.println(buffer.toString());

                System.out.println("|||||||||||||||||||||||||||||||||||||||||\n\n");

                cursor = allDreamsTable.getAllData();

                buffer = new StringBuffer();

                while(cursor.moveToNext())
                {
                    buffer.append("SLEEP_ID :" + cursor.getString(0) + "\n");
                    buffer.append("AVERANGE_REM_TIME :" + cursor.getString(1) + "\n");
                    buffer.append("AVERANGE_NREM_TIME :" + cursor.getString(2) + "\n");
                    buffer.append("AVERANGE_DEEP_TIME :" + cursor.getString(3) + "\n");
                    buffer.append("AVERANGE_SHALLOW_TIME :" + cursor.getString(4) + "\n");
                    buffer.append("EVALUATION_OF_SLEEP_QUALITY :" + cursor.getString(5) + "\n");
                    buffer.append("TIME_OF_SLEEP :" + cursor.getString(6) + "\n");
                    buffer.append("SLEEP_START_DATE :" + cursor.getString(7) + "\n");
                    buffer.append("SLEEP_STOP_DATE :" + cursor.getString(8) + "\n");
                    buffer.append("PHASE_OF_AWAKENING :" + cursor.getString(9) + "\n");
                }
                System.out.println(buffer.toString());
            }
        });

        buttonAllDreamsTableTable.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                boolean isUpdated = userTable.updateDate(1, "Maciek - UPDATE");
                if(isUpdated == true)
                {
                    Toast.makeText(MainActivity.this, "Data updated correctly", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Error while updating data", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonCurrentDreamTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer deletedRows = userTable.deleteData(1);
                if(deletedRows > 0)
                {
                    Toast.makeText(MainActivity.this, "Some row was deleted", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "No row deleted", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonCreateDataBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Kliknelo buttonCreateDataBase");
                Random rand = new Random();
                int randInt = rand.nextInt(100);
                String rowContent = "Maciek - " + randInt;
                boolean isInsertedUserTable = userTable.insertData(rowContent);
                boolean isInsertedAllDreamsTable = allDreamsTable.insertData(21.65, 2312.12312, 2.0, 0.666666, 9, 1856, "201708102245","201708110633", "REM");

                if(isInsertedUserTable == true && isInsertedAllDreamsTable == true){
                    Toast.makeText(MainActivity.this, "Data inserted correctly", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this, "Error while inserting data", Toast.LENGTH_SHORT).show();
                }

            }
        });
        /*  TEST    TEST    TEST    TEST    TEST    TEST    TEST    TEST    TEST*/


        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        final AlarmFragment alarmFragment = new AlarmFragment();
        final StatisticsFragment statisticsFragment = new StatisticsFragment();
        final SettingsFragment settingsFragment = new SettingsFragment();

        getFragmentManager().beginTransaction().replace(R.id.frameLayout, alarmFragment).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_alarm:
                        getFragmentManager().beginTransaction().replace(R.id.frameLayout, alarmFragment).commit();
                        break;
                    case R.id.ic_statistics:
                        getFragmentManager().beginTransaction().replace(R.id.frameLayout, statisticsFragment).commit();
                        break;
                    case R.id.ic_settings:
                        getFragmentManager().beginTransaction().replace(R.id.frameLayout, settingsFragment).commit();
                        break;
                }
                return true;
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_navigation_main,menu);
        return true;
    }



    private void SetScreen() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
}
