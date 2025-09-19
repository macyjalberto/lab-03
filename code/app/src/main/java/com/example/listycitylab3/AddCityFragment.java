package com.example.listycitylab3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddCityFragment extends DialogFragment {
    
    // instance variable from hint
    private City city;
    
    // empty constructor to use when adding city
    public AddCityFragment() {
    }
    
    /* 
    constructor with city as its parameter as recommended by hint
    makes it so it remembers current city when selecting existing city
    and "remembers" no city when making a new one
    without it, the input bars will appear completely blank when clicking existing cities as if user is adding
    a new city
    */
    public void checkCity(City city) {
        this.city = city;
    }
    
    interface AddCityDialogListener {
        void addCity(City city);
    }
    
    private AddCityDialogListener listener;
    
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        
        if (context instanceof AddCityDialogListener) {
            listener = (AddCityDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement AddCityDialogListener");
        }
    }
    
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_city, null);
        
        EditText editCityText = view.findViewById(R.id.edit_text_city_text);
        EditText editProvinceText = view.findViewById(R.id.edit_text_province_text);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        
        // if user is selecting existing city, gets its city/province so it can be put on the input bars
        if (city != null) {
            editCityText.setText(city.getName());
            editProvinceText.setText(city.getProvince());
        }
        
        return builder
                .setView(view)
                /* formats it so if you're adding a new city, then it says "add a city"
                otherwise, it will say "editing [city], [province]" */
                .setTitle(city == null ? "Add a city" : String.format("You are editing:\n%s, %s", editCityText.getText().toString(), editProvinceText.getText().toString()))
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Confirm", (dialog, which) -> {
                    
                    String cityName = editCityText.getText().toString();
                    String provinceName = editProvinceText.getText().toString();
                    
                    // sees if user is entering a new city/province, or editing an already existing one
                    if (city == null) {
                        listener.addCity(new City(cityName, provinceName));
                    } else {
                        // changes existing city and province to new one
                        city.setName(cityName);
                        city.setProvince(provinceName);
                    }
                })
                .create();
        
    }
}