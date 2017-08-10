package com.approsoft.internalstorage;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    EditText etInputBox;
    Button btnWriteToFile, btnReadFile;
    TextView tvFileData, tvToastMessage;
    FileOutputStream fileOutputStream;
    FileInputStream fileInputStream;
    String fileName = "InternalStorage.txt";
    Toast myToast;
    View myToastView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etInputBox = (EditText) findViewById(R.id.etInputBox);
        btnWriteToFile = (Button) findViewById(R.id.btnWriteToFile);
        btnReadFile = (Button) findViewById(R.id.btnReadFile);
        tvFileData = (TextView) findViewById(R.id.tvFileData);

        LayoutInflater layoutInflater = getLayoutInflater();
        myToastView = layoutInflater.inflate(R.layout.my_toast,null);
        tvToastMessage = (TextView) myToastView.findViewById(R.id.tvMessage);
        myToast  = new Toast(MainActivity.this);
        myToast.setView(myToastView);

        btnWriteToFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(etInputBox.getText())){
                    etInputBox.setError("Input something first");
                    etInputBox.requestFocus();
                }else{
                    String dataToWrite = etInputBox.getText().toString();
                    dataToWrite = dataToWrite+"\n";
                    byte[] bytesOfData = dataToWrite.getBytes();
                    try {
                        fileOutputStream = openFileOutput(fileName, Context.MODE_APPEND);
                        fileOutputStream.write(bytesOfData);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        try {
                            fileOutputStream.close();
                            etInputBox.setText("");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        btnReadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    fileInputStream = openFileInput(fileName);
                    StringBuffer stringBuffer = new StringBuffer();
                    int read = -1;
                    while ((read = fileInputStream.read()) != -1) {
                        stringBuffer.append((char) read);
                    }
                    tvFileData.setText(stringBuffer.toString());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    tvToastMessage.setText("File not found.");
                    myToast.show();
                } catch (IOException e) {
                    e.printStackTrace();
                    tvToastMessage.setText("Error reading data.");
                    myToast.show();
                }finally{
                    try {
                        if(fileInputStream!=null) {
                            fileInputStream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
