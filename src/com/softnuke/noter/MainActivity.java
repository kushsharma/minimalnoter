package com.softnuke.noter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements ConfirmDeleteDialog.DeleteCommunicator{	
	
	final static String FILENAME="dataNotes.txt";
	private CustomAdaptor adapter = null;
	float historicX = Float.NaN, historicY = Float.NaN;
	static final int DELTA = 50;
	enum Direction {LEFT, RIGHT;}	
	
	ListView listview;
	
	public ArrayList<HashMap<String,String>> DataMaper = new ArrayList<HashMap<String, String>>();
	//public ArrayList<ArrayList<String>> DataMaper = new ArrayList<ArrayList<String>>();
	
	static final int EDIT_REQUEST = 0;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); 
    	
    	listview = (ListView) findViewById(R.id.lvNotes);
    	//registering to add context menu for all items
        registerForContextMenu(listview);        
        
        
    	//set button and list view listeners like swiping!
        setListeners();
        //loadTextFromFile();
        
        //loadNotesFromDb();
        
        //Insert DataMaper values form db
        arrayFill();
        //setup listeners for Listview and adpater
        fillupList();
        
    }

    private void populateListView() {
    	arrayFill();
    	
    	//updating adapter view
    	adapter.notifyDataSetChanged();		
	}

	private void arrayFill() { 
    	//access DB
    	DbHandler db = new DbHandler(this);
    	
    	
        //clearing any predefined old data
    	DataMaper.clear();
    	
    	// Reading all contacts
        Log.d("Reading: ", "Reading all contacts.."); 
        List<Notes> notes = db.getAllNotes();   
        
        int i = 0;
        
        
        
        for (Notes note : notes) { 
            String log = "Id: "+note.getID()+" ,Name: " + note.getName() + " ,Data: " + note.getData();            
            // Writing Contacts to log
            Log.d("Name: ", log);
            
            HashMap<String, String> value = new HashMap<String, String>();
            //ArrayList<String> value= new ArrayList<String>();
            
            //inserting into hashmap
            value.put("id", ""+note.getID());
            value.put("name", note.getName());
            value.put("data", note.getData());
            value.put("time", note.getTime());
            
            /*value.add(""+note.getID());//note id
            value.add(""+note.getName());//note name
            value.add(""+note.getData());//note raw date
*/            
            //convert time to look pretty            
            String _Date = note.getTime(); //"2010-09-29 08:45:22"
            
            SimpleDateFormat fmtInput = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat fmtOutput = new SimpleDateFormat("dd MMM, EEEE");
            
         	Date date = null;
			try {
				date = fmtInput.parse(_Date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
            String formated = fmtOutput.format(date);
            
            value.put("timeformated", formated);
            value.put("color", note.getColor());
            
           /* value.add(""+formated);//formated date
            value.add(""+note.getColor());//color
*/
            //just an example
            //value.put("note3", "Wash your clothes");
            
            //Arraylist values are
            // index 0 - id
            // index 1 - name
            // index 2 - note data
            // index 3 - date - raw
            // index 4 - date - formatted
            // index 5 - color
            
            DataMaper.add(value);          
            
            
        } 
        
        //just an example
        //DataMaper.put("1", value);
        
        for(i = 0; i<DataMaper.size();i++){
        	Log.i("DataMaper values",DataMaper.get(i).toString());
        }
        
        Log.i("data filled","success");
        
		
	}

	private void fillupList() {    	
    	
       /* String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
            "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
            "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
            "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
            "Android", "iPhone", "WindowsMobile" };

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
          list.add(values[i]);
        }*/
        
        //calling customadaptor for user defined view using list_item.xml
        //final CustomAdaptor adapter = new CustomAdaptor(this, android.R.layout.simple_list_item_1, values);
    	
        Log.d("filling", "list start");
        adapter = new CustomAdaptor(this, android.R.layout.simple_list_item_1, DataMaper);
        
        //setting data adapter
        listview.setAdapter(adapter);        
        
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		      @Override
		      public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
		    	  
		        //final String item = (String) parent.getItemAtPosition(position);
		    	  
		    	  //gives an pulsing effect when clicked
		    	  Animation pulse = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_pulse);
		    	  view.startAnimation(pulse);
		    	  
		    	  
		    	  	//counting numbre of items
		    	    int count = parent.getChildCount();		    	    
		    	    
		    	    TextView t1 = (TextView) view.findViewById(R.id.tvListId);
					
		    	    String data[] = new String[6];		    	    
		    	    data[0] = t1.getText().toString();// fetching id		    	    
		    	    
		    	    for(int i = 0;i<DataMaper.size();i++){

		    	    	//filling hash map 
		    			HashMap<String,String> temp = new HashMap<String,String>();
		    			temp.putAll(DataMaper.get(i));
		    			
		    			if(temp.get("id") == data[0]){	//matching id
		    				
		    				data[1] = temp.get("name").toString();//name
		 		    		data[2] = temp.get("data").toString();//data
		 		    		data[3] = temp.get("time").toString();//time formated
		 		    		data[4] = temp.get("timeformated").toString(); //color
		 		    		data[5] = temp.get("color").toString(); //color 
		    			}
		    		}
		    	    
		    	    /*TextView t2 = (TextView) view.findViewById(R.id.tvListNoteName);
		    	    TextView t3 = (TextView) view.findViewById(R.id.tvListNoteLine);
		    	    TextView t4 = (TextView) view.findViewById(R.id.tvListTime);*/			    	   
		        
			    Log.i("item","ittemmm"+position+",id:"+count+data[1]);
		        
		        Intent i = new Intent(MainActivity.this, NotesEditor.class);
			    i.putExtra("Note", data); //note details
			    
				MainActivity.this.startActivityForResult(i, EDIT_REQUEST);
				
				/* view.animate().setDuration(2000).alpha(0).withEndAction(new Runnable() {
	              @Override
	              public void run() {
	                list.remove(item);
	                adapter.notifyDataSetChanged();
	                view.setAlpha(1);
	              }
	            });*/
		        
		        //Separate thread for ui interaction
			        /*runOnUiThread(new Runnable()
			        {
			            public void run()
			            {
			            	Toast.makeText(MainActivity.this,"Clicked", Toast.LENGTH_SHORT).show();
			            }
			        });*/
		      }

		    });
        
        
        
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.item_slide_out);
        
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				//Toast.makeText(getApplicationContext(), "Holded", Toast.LENGTH_SHORT).show();
				//view.startAnimation(animation);
				return false;
			}
        	
		});        
        
        
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                
            }
        });
        
        
		
	}
	
	protected void onActivityResult(int requestCode, int resultCode,
            Intent data) {
        if (requestCode == EDIT_REQUEST) {
            if (resultCode == RESULT_OK) {
                // A note was picked.  Here we will just display it
                // to the user.
                //startActivity(new Intent(Intent.ACTION_VIEW, data));
            	Log.i("activity","received back");
            	
            	//refilling list
            	populateListView();
            	
            }
        }
    }
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {		
		super.onCreateContextMenu(menu, v, menuInfo);
		
		//filling context Menu
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.context_main, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
	
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		View v = info.targetView;
		
				
		TextView t1 = (TextView) v.findViewById(R.id.tvListId);
		
	    String data[] = new String[6];		    	    
	    data[0] = t1.getText().toString();// fetching id		    	    
	    
	    for(int i = 0;i<DataMaper.size();i++){

	    	//filling hash map 
			HashMap<String,String> temp = new HashMap<String,String>();
			temp.putAll(DataMaper.get(i));
			
			if(temp.get("id") == data[0]){	//matching id
				
				data[1] = temp.get("name").toString();//name
		    	data[2] = temp.get("data").toString();//data
		    	data[3] = temp.get("time").toString();//raw date
		    	data[4] = temp.get("timeformated").toString(); //time formated
		    	data[5] = temp.get("color").toString(); //color 
			}
		}
	    
	    int noteid = Integer.parseInt(data[0]);   
	    		
	    switch (item.getItemId()) {
	        case R.id.itemDelete:
	            Log.i("Context","Delete");
	            
	            DbHandler db = new DbHandler(this);
	            
	            //creating note
	        	Notes note = new Notes(noteid);
	            
		    	int res = db.deleteNote(note);
		    	
	            if(res == 1){
	            	Toast.makeText(getApplicationContext(), "Deleted.", Toast.LENGTH_SHORT).show();
	            	
	            	//resetting list
	            	populateListView();
	            }
	            else
	            	Toast.makeText(getApplicationContext(), "Unable to delete.", Toast.LENGTH_SHORT).show();
	            
	            db.close();
	            
	            return true;
	        case R.id.itemProperties:
	        	
	        	Log.i("Context","Properties");
	        	Toast.makeText(getApplicationContext(), "Created on: "+data[3], Toast.LENGTH_LONG).show();	        	
	        		        	
	            return true;
	        default:
	            return super.onContextItemSelected(item);
	    }
	    
	    
		
	}

	private void setListeners() {	
		
		SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(listview,
                        new SwipeDismissListViewTouchListener.OnDismissCallback() {
                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    
                                	View rowV = adapter.getView(position, null, null);
		
									TextView id = (TextView) rowV.findViewById(R.id.tvListId);
									int noteid = Integer.parseInt(id.getText().toString());
									
									Log.d("Remove item of id:",""+noteid);
									
									//calling confirm dialog box
							    	ConfirmDeleteDialog dialog = ConfirmDeleteDialog.newInstance(noteid);
							    	dialog.show(getSupportFragmentManager(), null);
							    	
                                }
                                
                            }
                        });
        listview.setOnTouchListener(touchListener);
        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
        listview.setOnScrollListener(touchListener.makeScrollListener());
        
				
		/*save.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//if(v.getId()==R.id.bSave){
					String usertext = mtextInput.getText().toString();
					
					mtextInput.setText("");
					
					if(!usertext.contentEquals(""))
					{	
						//String currentText = mtextOutput.getText().toString();
						//mtextOutput.setText(usertext+"\n"+currentText);
					
						try {
							FileOutputStream fo = openFileOutput(FILENAME,Context.MODE_APPEND);
							fo.write(usertext.getBytes());
							fo.write("\n".getBytes());
						} catch (FileNotFoundException e) {
							
							e.printStackTrace();
						} catch (IOException e) {
							
							e.printStackTrace();
						}
					}
					
				//}
			}
		});		
		
		Log.i("Savebutton", "Set");*/		
				
		
	}//set Listeners end
	
	private void AddNewNote(String noteName) {
		
		Log.i("name", noteName);
		
		Intent edi = new Intent(MainActivity.this, NotesEditor.class);
		String data[] = new String[3];
		
		//fetch time to act as a id				
		Calendar c = Calendar.getInstance();
        Log.i("Current time => ",""+c.getTime());
        
        //generate time
        //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //String formattedDate = df.format(c.getTime());
        //generate id
        SimpleDateFormat dfId = new SimpleDateFormat("ddHHmmss");
        String formatedDateforId = dfId.format(c.getTime());
		
										
		data[0] = formatedDateforId; //id 
		data[1] = noteName;
		data[2] = "";		
		
		Log.i("name",""+data[1]);
		
		edi.putExtra("Note", data); //Optional parameters
		
		//reset input field
		//mtextInput.setText("");
		
		MainActivity.this.startActivityForResult(edi, EDIT_REQUEST);
		
	}
	
	public static class CreateNoteDialog extends DialogFragment {
		EditText name;
		Button create,skip;
		private MainActivity MainAct;
		
		@Override
		public void onAttach(Activity activity) {
			// TODO Auto-generated method stub
	    	
	    	//attaching activity to call delNote non static method
	    	if (activity instanceof MainActivity)
	        {
	            MainAct = (MainActivity) activity;
	        }
	        super.onAttach(activity);
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			
			//setting note title
			getDialog().setTitle("Note Title?");
			
			View v = inflater.inflate(R.layout.dialog_new_note, null);
			
			name = (EditText) v.findViewById(R.id.dialogNoteName);
			create = (Button) v.findViewById(R.id.dialogCreateButton);
			skip = (Button) v.findViewById(R.id.dialogSkipButton);
			
			create.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String text = name.getText().toString();
					
					if(text.trim().length() < 1){
						Toast.makeText(getActivity(), "Insert a name please!", Toast.LENGTH_SHORT).show();
					}						
					else{
						Toast.makeText(getActivity(), "Creating new note: "+text, Toast.LENGTH_SHORT).show();
						
						//calling add new note method
						MainAct.AddNewNote(text);
						
						//close dialog
						dismiss();
					}
					
					
				}
			});
			
			skip.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {					
					Toast.makeText(getActivity(), "Creating new note", Toast.LENGTH_SHORT).show();
					
					//calling add new note method with default text:New Note
					MainAct.AddNewNote("New Note");
					
					//close dialog
					dismiss();					
				}
			});
			return v;			
			
		}
	    
	}
	
	private void loadNotesFromDb() {
    	DbHandler db = new DbHandler(this);
        
        /**
         * CRUD Operations
         * */
        // Inserting Contacts
    	
        Log.d("Insert: ", "Inserting .."); 
        //db.addNote(new Notes("Superman", "9100666600"));        
        //db.addNote(new Notes("Batman", "9199333399"));
         
        // Reading all contacts
        Log.d("Reading: ", "Reading all contacts.."); 
        List<Notes> notes = db.getAllNotes();       
         
        for (Notes cn : notes) {
            String log = "Id: "+cn.getID()+" ,Name: " + cn.getName() + " ,Data: " + cn.getData();
            
            // Writing Contacts to log
            Log.d("Name: ", log);
        }   
            
        
		
	}
    
    //this will load text from saved file
    private void loadTextFromFile() {
		File f = new File(getFilesDir(),FILENAME);
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			
			String line;
			while((line=br.readLine())!=null){
				//mtextOutput.setText(line+"\n"+mtextOutput.getText());
				
			}
			
			br.close();
			
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		switch (item.getItemId()) {
		    case R.id.menu_itemAbout:
		    	//notifying user who is the master mind
		    	Toast.makeText(getApplicationContext(), "App Developer: Kush Kumar Sharma", Toast.LENGTH_LONG).show();
		      
		      return true;
		    case R.id.menu_new_note:
		    	
		    	//calling dialog for asking new note name that will handle new notes
				CreateNoteDialog dialog = new CreateNoteDialog();
				dialog.show(getSupportFragmentManager(), "NewNotedDialog");
				
		    	return true;
		    default:
	            return super.onContextItemSelected(item);
		}
		
	}
	
	//this is called when confirm delete dialog will select accept and send it
	@Override
	public void deleteNoteCallBack(boolean confirm,int noteid) {
		// TODO Auto-generated method stub
		
		if(!confirm)
			return;	    	
    	
    	// deleting from database too			
		DbHandler db = new DbHandler(this);
        
    	//creating note
    	Notes note = new Notes(noteid);
    	
    	int res = db.deleteNote(note);
    	
        if(res == 1){
        	//removing note from DataMaper!
        	for(int i = 0;i<DataMaper.size();i++){

    	    	//filling hash map 
    			HashMap<String,String> temp = new HashMap<String,String>();
    			temp.putAll(DataMaper.get(i));
    			
    			String Stringnoteid = ""+noteid;
    			
    			if(temp.get("id").equals(Stringnoteid)){	//matching id
    				DataMaper.remove(i);
    				break;
    			}
    		}
        	//updating adapter view
        	adapter.notifyDataSetChanged();
        	
        	Toast.makeText(this, "Deleted.", Toast.LENGTH_SHORT).show();	            	
        }
        else{
        	Toast.makeText(this, "Unable to delete.", Toast.LENGTH_SHORT).show();
        }        	
        
        db.close();
        
	}	
    
}
