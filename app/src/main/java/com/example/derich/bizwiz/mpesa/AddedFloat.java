package com.example.derich.bizwiz.mpesa;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.sql.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_ADDED_FLOAT;
import static com.example.derich.bizwiz.sql.DatabaseHelper.DATE_MILLIS;
import static com.example.derich.bizwiz.sql.DatabaseHelper.TABLE_MPESA;

public class AddedFloat extends AppCompatActivity {
    EditText amount;
    DatabaseHelper myDb;
    Button btn_insert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_added_float);
        btn_insert = findViewById(R.id.button_added_float);
        amount = findViewById(R.id.editText_added_float);
        addedFloat();
    }

    private void addedFloat() {
        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                String addedAmount = amount.getText().toString().trim();
                if (!(addedAmount.isEmpty()))
                {
                    Integer addedCash = Integer.valueOf(addedAmount);
                long timeMillis = System.currentTimeMillis();
                insert( getDate(timeMillis),addedCash);
                amount.setText("");
                    Toast.makeText(AddedFloat.this,"Added successfully",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(AddedFloat.this,"Amount cannot be empty",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public static String getDate(long milliseconds){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Calendar vCalendar = Calendar.getInstance();
        vCalendar.setTimeInMillis(milliseconds);
        return sdf.format(vCalendar.getTime());
    }

    public void insert(String date,Integer addedCash) {
        //Your DB Helper
        SQLiteOpenHelper dbHelper = new DatabaseHelper(AddedFloat.this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        contentValue.put(DATE_MILLIS, date);
        contentValue.put(COLUMN_ADDED_FLOAT, addedCash);

        //insert data in DB
        database.insert(TABLE_MPESA,null,contentValue);

        //Close the DB connection.
        database.close();

    }
}