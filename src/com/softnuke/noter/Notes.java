package com.softnuke.noter;

public class Notes {
	//private variables
    int _id = 0;
    String _name = null;
    String _data = null;
    String _time = null;
    String _color = "#EB8736"; //default light orange
     
    // Empty constructor
    public Notes(){
         
    }
    
 // constructor
    public Notes(int id){
        this._id = id;               
    }
    
    // constructor
    public Notes(int id, String name, String data){
        this._id = id;
        this._name = name;
        this._data = data;        
    }
    
    // constructor
    public Notes(int id, String name, String data, String time){
        this._id = id;
        this._name = name;
        this._data = data;
        this._time = time;        
    }
    
    // constructor with color
    public Notes(int id, String name, String data, String time, String color){
        this._id = id;
        this._name = name;
        this._data = data;
        this._time = time;
        this._color = color;        
    }
     
    // constructor
    public Notes(String name, String data){
        this._name = name;
        this._data = data;
    }
    
    // getting ID
    public int getID(){
        return this._id;
    }
     
    // setting id
    public void setID(int id){
        this._id = id;
    }
     
    // getting name
    public String getName(){
        return this._name;
    }
     
    // setting name
    public void setName(String name){
        this._name = name;
    }
     
    // getting data
    public String getData(){
        return this._data;
    }
     
    // setting data
    public void setData(String data){
        this._data = data;
    }
    
    //get time
    public String getTime(){
        return this._time;
    }
    
    //set time
    public void setTime(String time){
        this._time = time;
    }
    
    //get color
    public String getColor(){
        return this._color;
    }
    
    //set color
    public String setColor(String color){
        return this._color = color;
    }
}
