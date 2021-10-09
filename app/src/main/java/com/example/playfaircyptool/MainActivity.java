package com.example.playfaircyptool;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    Spinner methodSpinner;
    EditText edtKey;
    EditText edtInput;
    TextView tvOutput;
    Button bntStart; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        methodSpinner = findViewById(R.id.method_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.method_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        methodSpinner.setAdapter(adapter);

        edtKey = findViewById(R.id.key_edt);
        edtInput= findViewById(R.id.input_edt);
        tvOutput = findViewById(R.id.output_textview);
        bntStart = findViewById(R.id.start_btn);
        bntStart.setOnClickListener(view -> clickStart());

    }

    private void clickStart() {

        String strkey = edtKey.getText().toString().trim();
        String strInput = edtInput.getText().toString().trim();
        String strMethod = methodSpinner.getSelectedItem().toString();
        Playfair playfair = new Playfair(strkey, strInput);

        if(playfair.isValidInput() == true && playfair.isValidKey() == true){

            if(strMethod.equals("Encryption")) {
                tvOutput.setText(playfair.cipher());
            } else {
                tvOutput.setText(playfair.decode());
            }
        }
    }
}