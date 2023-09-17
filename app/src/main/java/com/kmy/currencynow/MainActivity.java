package com.kmy.currencynow;


import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView convertFromDropdownTextView ,convertToDropdownTextView ,conversionRateText ;

    String covertFromValue ,covertToValue ,conversionValue ;

    Dialog fromDialog ,toDialog ;

    ArrayList<String> arrayList ;

    String[] country = {"AFN","EUR","ALL","DZD","USD","AOA","XCD","ARS","AMD","AWG","AUD","AZN","BSD","BHD","BDT","BBD","BYN","BZD","XOF","BMD","INR","BTN","BOB","BOV","BAM","BWP","NDK","BRL","BNO","BGN","EGP"} ;

    EditText amountToConvert ;

    Button convertBtn ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inflate();

        arrayList = new ArrayList<>();
        for (String i : country)
        {
            arrayList.add(i);
        }
        convertFromDropdownTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDialog = new Dialog(MainActivity.this);
                fromDialog.setContentView(R.layout.spinner);
                fromDialog.getWindow().setLayout(650 ,800);
                fromDialog.show();

                EditText editText = fromDialog.findViewById(R.id.select_country);
                ListView  listView = fromDialog.findViewById(R.id.list_view);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this , android.R.layout.simple_list_item_1 ,arrayList);
                listView.setAdapter(adapter);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        convertFromDropdownTextView.setText(adapter.getItem(position));
                        fromDialog.dismiss();
                        covertFromValue = adapter.getItem(position);

                    }
                });

            }
        });

        convertToDropdownTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                toDialog = new Dialog(MainActivity.this);
                toDialog.setContentView(R.layout.spinner);
                toDialog.getWindow().setLayout(650 ,800);
                toDialog.show();

                EditText editText = toDialog.findViewById(R.id.select_country);
                ListView  listView = toDialog.findViewById(R.id.list_view);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this , android.R.layout.simple_list_item_1 ,arrayList);
                listView.setAdapter(adapter);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        convertToDropdownTextView.setText(adapter.getItem(position));
                        toDialog.dismiss();
                        covertToValue = adapter.getItem(position);

                    }
                });

            }
        });

        convertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Double amountTOConvert = Double.valueOf(MainActivity.this.amountToConvert.getText().toString());
                    getConversionRate(covertFromValue,covertToValue,amountTOConvert);
                }
                catch (Exception e)
                {

                }
            }
        });
    }

    public void inflate()
    {
        convertFromDropdownTextView = findViewById(R.id.convert_from_dropdown_menu);
        convertToDropdownTextView = findViewById(R.id.convert_to_dropdown_menu);
        conversionRateText = findViewById(R.id.conversionRateText);
        amountToConvert = findViewById(R.id.amountToConvertValueEditText);
        convertBtn = findViewById(R.id.result_btn);
    }

    public String getConversionRate(String covertFrom ,String covertTo , Double amountToConvert)
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://free.currconv.com/api/v7/convert?q="+covertFrom+"_"+covertTo+"&compact=ultra&apiKey=092be5c2edca15cb5fae";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    Double conversionRateValue = round(((Double) jsonObject.get(covertFrom + "_" + covertTo)), 2);
                    conversionValue = "" + round((conversionRateValue * amountToConvert), 2);
                    conversionRateText.setText(conversionValue);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
        return null;
    }

    public static double round(double value ,int places)
    {
        if(places<0) throw new IllegalArgumentException();
        BigDecimal bigDecimal = BigDecimal.valueOf(value);
        bigDecimal=bigDecimal.setScale(places, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

}