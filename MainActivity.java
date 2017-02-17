package com.example.yourprojectname;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.EventLogTags;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataChangeSet;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;


import com.example.intuition.humanity.ApiClientAsyncTask;

import static android.os.Build.VERSION_CODES.M;
import static com.example.intuition.humanity.ApiClientAsyncTask.getGoogleApiClient;

public class MainActivity extends AppCompatActivity  {
    public static final String PACKAGE_NAME = "com.example.intuition.helpinghand";
    public static final String DATABASE_NAME = "orgs_db";
    public static final String TABLE_NAME = "orgs_table";
    public SQLiteDatabase db = null;
    private static final String DATABASE_PATH = "/data/data/" + PACKAGE_NAME + "/databases/" + DATABASE_NAME;
    private static final File DATA_DIRECTORY_DATABASE =
            new File(Environment.getDataDirectory() + "/data/" + PACKAGE_NAME + "/databases/" + DATABASE_NAME);

    ListView orgs_list1;
    private CloudBackup mCloud;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            db = openOrCreateDatabase("orgs_db", Context.MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS orgs_table(_id integer primary key autoincrement unique, orgs_img blob, orgs_name text)");
            Cursor cursor = db.rawQuery("SELECT * FROM orgs_table", null);
            if (cursor.moveToFirst()){
                Toast.makeText(MainActivity.this, "Data is there", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(MainActivity.this, "Data is not there", Toast.LENGTH_SHORT ).show();

                ContentValues contentValues = new ContentValues();
                contentValues.put("orgs_img", R.drawable.image1);
                contentValues.put("orgs_name", "Pizza");
                db.insert("orgs_table", null, contentValues);

                ContentValues contentValues1 = new ContentValues();
                contentValues1.put("orgs_img", R.drawable.image2);
                contentValues1.put("orgs_name", "cheese");
                db.insert("orgs_table", null, contentValues1);

                ContentValues contentValues2 = new ContentValues();
                contentValues2.put("orgs_img", R.drawable.image3);
                contentValues2.put("orgs_name", "Cake");
                db.insert("orgs_table", null, contentValues2);

                ContentValues contentValues3 = new ContentValues();
                contentValues3.put("orgs_img", R.drawable.image4);
                contentValues3.put("orgs_name", "Ice_cream");
                db.insert("orgs_table", null, contentValues3);

                ContentValues contentValues4 = new ContentValues();
                contentValues4.put("orgs_img", R.drawable.image5);
                contentValues4.put("orgs_name", "Coffee");
                db.insert("orgs_table", null, contentValues4);

                ContentValues contentValues5 = new ContentValues();
                contentValues5.put("orgs_img", R.drawable.image6);
                contentValues5.put("orgs_name", "coffee1");
                db.insert("orgs_table", null, contentValues5);

                ContentValues contentValues6 = new ContentValues();
                contentValues6.put("orgs_img", R.drawable.image7);
                contentValues6.put("orgs_name", "Butter");
                db.insert("orgs_table", null, contentValues6);

                ContentValues contentValues7 = new ContentValues();
                contentValues7.put("orgs_img", R.drawable.image8);
                contentValues7.put("orgs_name", "Chicken");
                db.insert("orgs_table", null, contentValues7);

                ContentValues contentValues8 = new ContentValues();
                contentValues8.put("orgs_img", R.drawable.image9);
                contentValues8.put("orgs_name", "Tikka");
                db.insert("orgs_table", null, contentValues8);

                ContentValues contentValues9 = new ContentValues();
                contentValues9.put("orgs_img", R.drawable.image10);
                contentValues9.put("orgs_name", "Curry");
                db.insert("orgs_table", null, contentValues9);

                ContentValues contentValues10 = new ContentValues();
                contentValues10.put("orgs_img", R.drawable.image11);
                contentValues10.put("orgs_name", "Shake");
                db.insert("orgs_table", null, contentValues10);

                ContentValues contentValues11 = new ContentValues();
                contentValues11.put("orgs_img", R.drawable.image12);
                contentValues11.put("orgs_name", "Chocolate");
                db.insert("orgs_table", null, contentValues11);
            }

            orgs_list1 = (ListView) findViewById(R.id.orgs_list);
            final Cursor cursor1 = db.rawQuery("SELECT * FROM orgs_table", null);
            String[] from = {"orgs_img", "orgs_name"};
            int[] to = {R.id.orgs_img, R.id.orgs_name};
            SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(MainActivity.this, R.layout.list_arrangement, cursor1, from, to, 0);
            orgs_list1.setAdapter(simpleCursorAdapter);

        orgs_list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                TextView tv = (TextView) v.findViewById(R.id.orgs_name);
                if (tv.getText().toString().equals("Pizza")) {
                    Intent intent = new Intent(MainActivity.this, Org1.class);
                    startActivity(intent);
                }
                if (tv.getText().toString().equals("cheese")) {
                    Intent intent = new Intent(MainActivity.this, Org2.class);
                    startActivity(intent);
                }if (tv.getText().toString().equals("Cake")) {
                    Intent intent = new Intent(MainActivity.this, Org3.class);
                    startActivity(intent);
                }if (tv.getText().toString().equals("Ice_cream")) {
                    Intent intent = new Intent(MainActivity.this, Org4.class);
                    startActivity(intent);
                }if (tv.getText().toString().equals("Coffee")) {
                    Intent intent = new Intent(MainActivity.this, Org5.class);
                    startActivity(intent);
                }if (tv.getText().toString().equals("coffee1")) {
                    Intent intent = new Intent(MainActivity.this, Org6.class);
                    startActivity(intent);
//                }if (tv.getText().toString().equals("Butter")) {
//                    Intent intent = new Intent(MainActivity.this, Org7.class);
//                    startActivity(intent);
//                }if (tv.getText().toString().equals("Chicken")) {
//                    Intent intent = new Intent(MainActivity.this, Org8.class);
//                    startActivity(intent);
//                }if (tv.getText().toString().equals("Tikka")) {
//                    Intent intent = new Intent(MainActivity.this, Org9.class);
//                    startActivity(intent);
//                }if (tv.getText().toString().equals("Curry")) {
//                    Intent intent = new Intent(MainActivity.this, Org10.class);
//                    startActivity(intent);
//                }if (tv.getText().toString().equals("Shake")) {
//                    Intent intent = new Intent(MainActivity.this, Org11.class);
//                    startActivity(intent);
//                }if (tv.getText().toString().equals("Chocolate")) {
//                    Intent intent = new Intent(MainActivity.this, Org12.class);
//                    startActivity(intent);
                }


            }
        });

        Button b1 = (Button) findViewById(R.id.cloud_backup);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Crash time...", Toast.LENGTH_SHORT).show();
//                        mCloud.saveFileToDrive();
                Intent i = new Intent(MainActivity.this, CloudBackup.class);
                startActivity(i);
            }
        });

        /////////////////////////////////////////////////////////
        ////////////For OnClick Webview Open below code///////////

//        orgs_list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View v,
//                                    int position, long id) {
//                WebView webView = new WebView(v.getContext());
//                String[] urls = getResources().getStringArray(R.array.bookmark_urls);
//                webView.loadUrl(urls[position]);
//            }
//        });
        //////////////////////////////////////////////////////////////////


    }




}
