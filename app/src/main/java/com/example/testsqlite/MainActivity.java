package com.example.testsqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText edtMaSP, edtTenSp, edtSiLuong;
    Button btnInsert, btnDelete, btnUpdate, btnQuery, btnClear;
    ListView lv;
    ArrayList<String> myList;
    ArrayAdapter<String> myAdapter;
    SQLiteDatabase myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        edtMaSP = findViewById(R.id.edtId);
        edtTenSp = findViewById(R.id.edtName);
        edtSiLuong = findViewById(R.id.edtSoLuong);
        btnDelete = findViewById(R.id.btn_delete);
        btnUpdate = findViewById(R.id.btn_update);
        btnQuery = findViewById(R.id.btn_query);
        btnInsert = findViewById(R.id.btn_insert);
        // topa list view
        lv = findViewById(R.id.lv);
        myList = new ArrayList<>();
        myAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, myList);
        lv.setAdapter(myAdapter);
//         Toa va mo Co so du lieu
        myDatabase = openOrCreateDatabase("qlSanPham.db", MODE_PRIVATE, null);
        // Tao table để chứa dữ liệu
        try {
            String SQL = "CREATE TABLE tblSanPham( maSanPham TEXT primary key , tenSanPham TEXT , soLuong INTEGER )";
            myDatabase.execSQL(SQL);
        } catch (Exception e) {
            Log.e("Error", "Table đã tồn tại");

        }

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String maSanPham = edtMaSP.getText().toString();
                String tenSanPham = edtTenSp.getText().toString();
                int soLuong = Integer.parseInt(edtSiLuong.getText().toString());
                ContentValues myvalue = new ContentValues();
                myvalue.put("maSanPham", maSanPham);
                myvalue.put("tenSanPham", tenSanPham);
                myvalue.put("soLuong", soLuong);
                String msg = "";
                if (myDatabase.insert("tblSanPham", null, myvalue) == -1) {
                    msg = "fail to insert Record";
                } else {
                    msg = "Insert record successfully";
                }
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String maSanPham = edtMaSP.getText().toString();
                int n = myDatabase.delete("tblSanPham", "maSanPham = ?", new String[]{maSanPham});
                String msg = "";
                if (n == 0) {
                    msg = "No Record to Delete";
                } else {
                    msg = n + " Record is Delete";

                }
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();

            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int soLuong = Integer.parseInt(edtSiLuong.getText().toString());
                String maSanPham = edtMaSP.getText().toString();
                ContentValues myValue = new ContentValues();
                myValue.put("soLuong", soLuong);
                int n = myDatabase.update("tblSanPham", myValue, "maSanPham = ?", new String[]{maSanPham});
                String msg = "";
                if (n == 0) {
                    msg = "No Record to Update";
                } else {
                    msg = n + " Record is Update";

                }
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();

            }
        });
        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myList.clear();
                // lấy toàn b dữ lieu
                // 1
                // Lấy dữ liệu by con trỏ
                Cursor c = myDatabase.query("tblSanPham", null, null, null, null, null, null, null);
                // 2
                // Đọc dữ liệu
                c.moveToNext();
                String data = "";
                while (c.isAfterLast() == false) {
                    data = c.getString(0) + " - " + c.getString(1) + " - " + c.getString(2) + ".";
                    c.moveToNext();
                    myList.add(data);
                }
                c.close();
                myAdapter.notifyDataSetChanged();
            }
        });
        btnClear = findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtMaSP.setText("");
                edtSiLuong.setText("");
                edtTenSp.setText("");
            }
        });
    }
}