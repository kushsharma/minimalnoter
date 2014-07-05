package com.softnuke.noter;

import java.util.ArrayList;


import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CustomAdaptor extends BaseAdapter {
	private final Activity context;
	private ArrayList<Integer> colors;
	public ArrayList<String> color_list;
	private int color_index = 0;
	
	private final ArrayList<HashMap<String, String>> dataMaper;
	//private final ArrayList<ArrayList<String>> dataMaper;
	
	public CustomAdaptor(Activity context,int textViewResourceId, ArrayList<HashMap<String, String>> dataMaper) {
		//super(context, R.layout.list_item, dataMaper);
		
		this.context = context;
	    this.dataMaper = dataMaper;
	    
	    this.colors = new ArrayList<Integer>();
	    this.color_list = new ArrayList<String>();
	    
	    //no use
	    /*this.color_list.add(0, "#7A6A53");
	    this.color_list.add(1, "#948C75");
	    this.color_list.add(2, "#D9CEB2");
	    this.color_list.add(3, "#D5DED9");
	    this.color_list.add(4, "#99B2B7");
	    
	    this.colors.add(224);
	    this.colors.add(228);
	    this.colors.add(204);
	    */
	}
	
	@Override
	  public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		SuperViewHolder holder = null;
		if(rowView == null){
		//calling inflater to access layout view
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    
		//creating custom view
	    rowView = inflater.inflate( R.layout.list_item, parent, false);
	    
	    holder = new SuperViewHolder(rowView);
	    
	    rowView.setTag(holder);
		}
		else{
			holder = (SuperViewHolder) rowView.getTag();	
		}
	    
	    
	    HashMap<String, String> val = new HashMap<String,String>();
	    val.putAll(dataMaper.get(position));
	    
	    //ArrayList<String> val = dataMaper.get(position);
	    
	    //Log.i("list val", val.get(1).toString());
	    Log.i("list val", val.get("name").toString());
	    
	    //a snippet to iterate through map
	    /*for (Map.Entry<String, String> entry : val.entrySet())
	    {
	    	//setting value for each field
	    	id.setText(entry.getKey().toString());
	    	
	    	name.setText(entry.getKey().toString());
		    
		    data.setText(entry.getValue().toString());
		    
		    //System.out.println(entry.getKey() + "/" + entry.getValue());
	    }*/	
	    
	  //setting value for each field
	    
	    holder.id.setText(val.get("id").toString());
    	
    	holder.name.setText(val.get("name").toString());
	    
    	holder.data.setText(val.get("data").toString());
	    
    	holder.time.setText(val.get("timeformated").toString());
	    
    	String current_color = val.get("color").toString();
    	
    	/*holder.id.setText(val.get(0).toString());
    	
    	holder.name.setText(val.get(1).toString());
	    
    	holder.data.setText(val.get(2).toString());
	    
    	holder.time.setText(val.get(3).toString());*/
	    
    	//updating row background color
    	/*int red,green,blue;
	    red = this.colors.get(0);green = this.colors.get(1);blue=this.colors.get(2);
	    this.colors.clear();	    
	    
	    this.colors.add(red-3);
	    this.colors.add(green-3);
	    this.colors.add(blue-3);
	    
	    //stoping them to completely exhaust
	    if(this.colors.get(0)<24)
	    	this.colors.add(0, 224);
	    if(this.colors.get(1)<28)
	    	this.colors.add(1, 224);
	    if(this.colors.get(2)<4)
	    	this.colors.add(2, 224);    	
	    
	    rowView.setBackgroundColor(Color.rgb(red, green, blue));*/
    	
    	//String current_color = color_list.get(color_index);
    	
    	try{    		
    		rowView.setBackgroundColor(Color.parseColor(current_color));
    	}
    	catch(Exception e){
    		Log.d("COLOR ERROR", "Unable to parse, setting default");
    		
    		rowView.setBackgroundColor(Color.parseColor("#EB8736"));
    	}
    	
    	/*if(color_list.size() - 1 > color_index){
    		color_index++;
    	}
    	else{
    		color_index = 0;
    	}*/
    	
    	
	    
	    return rowView;
	}	
	
	class SuperViewHolder{
		TextView id , name, data, time;
		
		SuperViewHolder(View row){
			this.id= (TextView) row.findViewById(R.id.tvListId);
		    this.name = (TextView) row.findViewById(R.id.tvListNoteName);
		    this.data = (TextView) row.findViewById(R.id.tvListNoteLine);
		    this.time = (TextView) row.findViewById(R.id.tvListTime);
		}
	}
	
	
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataMaper.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}	
	
}
