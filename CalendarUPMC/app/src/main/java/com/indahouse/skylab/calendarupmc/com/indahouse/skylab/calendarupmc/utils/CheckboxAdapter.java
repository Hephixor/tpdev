package com.indahouse.skylab.calendarupmc.com.indahouse.skylab.calendarupmc.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.indahouse.skylab.calendarupmc.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckboxAdapter extends ArrayAdapter {
    Context context;
    List<Boolean> checkboxState;
    List<String> checkboxItems;
    SharedPreferences sharedPreferences;

    public CheckboxAdapter(Context context, List<String> resource) {
        super(context, R.layout.form_checkbox_item, resource);
        this.context = context;
        this.checkboxItems = resource;
        sharedPreferences = context.getApplicationContext().getSharedPreferences(context.getResources().getString(R.string.shared_pref),Context.MODE_PRIVATE);
        this.checkboxState = stateFromPrefs(resource);

    }

    private ArrayList<Boolean> stateFromPrefs(List<String> resource){
        ArrayList<Boolean> tmpState = new ArrayList<Boolean>(Collections.nCopies(resource.size(), false));
        HashMap<String,?> map = (HashMap)sharedPreferences.getAll();
        for (String str : map.keySet()) {
            if(map.get(str).equals("true")){tmpState.set(Integer.valueOf(str),true); }
        }
        return tmpState;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView==null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.form_checkbox_item, parent, false);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.textView1);
        final CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox1);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setChecked(position, !checkboxState.get(position));
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(String.valueOf(position),String.valueOf(checkboxState.get(position)));
                editor.commit();

                String key = String.valueOf(position);
                String val = sharedPreferences.getString(key,"defaultValue");
            }
        });
        textView.setText(checkboxItems.get(position));
        cb.setChecked(checkboxState.get(position));
        return convertView;
    }

    private void setChecked( int position, boolean state)
    {
        checkboxState.set(position, state);
        notifyDataSetChanged();
    }

    public void printSharedPref(){
        HashMap<String, String> hm = new HashMap<>();
        hm = (HashMap<String, String>) sharedPreferences.getAll();
        //Log.e("SharedPrefMap", hm.toString());

    }

    public List<Boolean> getBools(){
        return checkboxState;
    }

    public List<String> getCheckboxItems() { return checkboxItems; }

    public boolean hasCheck(){
        for (Boolean b: checkboxState) {
            if(b.equals(true)){
                return true;
            }
        }
        return false;
    }
}