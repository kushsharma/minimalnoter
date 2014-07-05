package com.softnuke.noter;


import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.support.v4.app.DialogFragment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class NotesEditor extends ActionBarActivity implements ConfirmDeleteDialog.DeleteCommunicator{
	
	private String Noted[], note_color="#EB8736", note_time = null, note_name = null ;
	private TextView nName,nData;
	private Button quick ;
	
	private int noteid = 0;
	private static boolean confirmDel = false;
	
	private static final int ID_COLORblue  = 1, ID_COLORgreen = 2, ID_COLORsky = 3, ID_COLORred = 4, ID_COLORzombie = 5;	
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editor);
		
		//calling actionbar from support
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
		
		nName = (TextView) findViewById(R.id.tvNoteName);
		nData = (TextView) findViewById(R.id.etNotes);
		quick = (Button) findViewById(R.id.bQuickAction);
		
		Intent intent = getIntent();
		Noted= intent.getStringArrayExtra("Note"); //if it's a string you stored.
		
		noteid = Integer.parseInt(Noted[0]);	
		note_name = Noted[1].toString() ;
		
		if(Noted.length > 3)
			note_time = Noted[3].toString();	//note time
		if(Noted.length > 5)
			note_color = Noted[5];	//note color
		
		nName.setText(note_name + " id :"+noteid);
		nData.setText(Noted[2].toString());	
		
		//call QuickActionBar setup
		SetupQuickColorChooser();
	}
	
	private void SetupQuickColorChooser() {
		
		// quick action menu procedure started ----------------------
		ActionItem item_color1 	= new ActionItem(ID_COLORblue, "Color Blue", getResources().getDrawable(R.drawable.menu_ok));
		ActionItem item_color2  = new ActionItem(ID_COLORgreen, "Color Green", getResources().getDrawable(R.drawable.menu_ok));
		ActionItem item_color3 	= new ActionItem(ID_COLORsky, "Color Sky", getResources().getDrawable(R.drawable.menu_ok));
		ActionItem item_color4  = new ActionItem(ID_COLORred, "Color Red", getResources().getDrawable(R.drawable.menu_ok));
		ActionItem item_color5  = new ActionItem(ID_COLORzombie, "Color Zombie", getResources().getDrawable(R.drawable.menu_ok));
		
		final QuickAction quickAction = new QuickAction(this, QuickAction.VERTICAL);
		
		//add action items into QuickAction
        quickAction.addActionItem(item_color1);
		quickAction.addActionItem(item_color2);
		quickAction.addActionItem(item_color3);
		quickAction.addActionItem(item_color4);
		quickAction.addActionItem(item_color5);		
		
		 //Set listener for action item clicked
		quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {			
			@Override
			public void onItemClick(QuickAction source, int pos, int actionId) {				
				ActionItem actionItem = quickAction.getActionItem(pos);
                 
				//here we can filter which action item was clicked with pos or actionId parameter
				switch(actionId){
					case ID_COLORblue:
						note_color = ""+getResources().getString(R.color.list_blue);
						Toast.makeText(getApplicationContext(), "Color Blue selected", Toast.LENGTH_SHORT).show();					
						break;
					
					case ID_COLORgreen: 
						note_color = ""+getResources().getString(R.color.list_green);
						Toast.makeText(getApplicationContext(), "Color Green selected", Toast.LENGTH_SHORT).show();					
						break;
					case ID_COLORsky: 
						note_color = ""+getResources().getString(R.color.list_sky_blue);
						Toast.makeText(getApplicationContext(), "Color Sky Blue selected", Toast.LENGTH_SHORT).show();					
						break;
					case ID_COLORred:  
						note_color = ""+getResources().getString(R.color.list_red);
						Toast.makeText(getApplicationContext(), "Color Red selected", Toast.LENGTH_SHORT).show();					
						break;
					case ID_COLORzombie :  
						note_color = ""+getResources().getString(R.color.list_blue_zombie);
						Toast.makeText(getApplicationContext(), "Color Zombie selected", Toast.LENGTH_SHORT).show();					
						break;
					
					default : Toast.makeText(getApplicationContext(), actionItem.getTitle() + " selected", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		
		//set listnener for on dismiss event, this listener will be called only if QuickAction dialog was dismissed
		//by clicking the area outside the dialog.
		quickAction.setOnDismissListener(new QuickAction.OnDismissListener() {			
			@Override
			public void onDismiss() {
				//Toast.makeText(getApplicationContext(), "Dismissed", Toast.LENGTH_SHORT).show();
			}
		});
		
		//Listeners
		quick.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				quickAction.setAnimStyle(QuickAction.ANIM_REFLECT);
				quickAction.show(v);
				
			}
		});
		
	}

	private void saveNote() {
		
		//fetch time			
		Calendar c = Calendar.getInstance(); 
        //generate time
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
    	
    	//creating updated note
    	Notes note = new Notes(noteid,note_name.trim(),nData.getText().toString(),formattedDate,note_color);
    	
    	Log.i("timestamp on note", ""+note.getTime());
    	
    	DbHandler db = new DbHandler(this);
    	int res = 0;//to check if update or add query executed
    	
    	//check if note exists already otherwise create new
    	Notes n = db.getNote(noteid);	    	
    	Notes last = null;
    	
    	if(n == null){
    		//adding new
    		db.addNote(note);
    		
        	//updating noteid which is dynamically assigned to newly note created
    		 last = db.getLastAddedNote();
    		 
    		 noteid = last.getID();
    		 Log.d("Last note", "Id : "+ noteid);
    	}
    	else{
    		//updating note
	    	res = db.updateNote(note);	 
    	}  
    	

    	
    	if(res > 0)
    		Toast.makeText(getApplicationContext(), "Updated.", Toast.LENGTH_SHORT).show();
    	else
    		Toast.makeText(getApplicationContext(), "Added.", Toast.LENGTH_SHORT).show();    	
    	
    	db.close();
	}
	
	private void delNote(){
		
		DbHandler db = new DbHandler(this);
        
    	//creating updated note
    	Notes note = new Notes(noteid);
    	
    	int res = db.deleteNote(note);
    	
        if(res == 1){
        	Toast.makeText(getApplicationContext(), "Deleted.", Toast.LENGTH_SHORT).show();	            	
        }
        else{
        	Toast.makeText(getApplicationContext(), "Unable to delete.", Toast.LENGTH_SHORT).show();
        }        	
        
        db.close();
        
        //closing editor
        Intent i = new Intent();
    	setResult(RESULT_OK,i);			    	
		finish();
    }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (keyCode == KeyEvent.KEYCODE_BACK ) {
	    	Log.i("Back", "Hit");
	    	
	    	Intent i = new Intent();
	    	
	    	//save or update note
	    	saveNote();
	    	
	    	setResult(RESULT_OK,i);	
			finish();
	        return true;
	    }

	    return super.onKeyDown(keyCode, event);
	}

	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_editor, menu);
        return true;
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		switch (item.getItemId()) {
		    case R.id.item_editor_delete:
		    	//Delete the current note
		    	Log.i("Menu", "Delete");		    	
		    			    	
		    	//calling confirm dialog box
		    	ConfirmDeleteDialog dialog = ConfirmDeleteDialog.newInstance(noteid);
		    	dialog.show(getSupportFragmentManager(), null);
		    	
		      return true;
		    case R.id.menu_editor_save:
		    	//exit without save
		    	Log.i("Menu", "Save");		    	
		    	
		    	//save or update note
		    	saveNote();
		    	
		      return true;  
		    default:
	            return super.onContextItemSelected(item);
		}
		
	}

	@Override
	public void deleteNoteCallBack(boolean confirm, int id) {
		// TODO Auto-generated method stub
		if(!confirm)
			return;
		
		//delete note method called
		delNote();
		
	}
	
	/*public static class ConfirmDeleteDialog extends DialogFragment {
		private NotesEditor EditorAct;
		
	    @Override
		public void onAttach(Activity activity) {
			// TODO Auto-generated method stub
	    	
	    	//attaching activity to call delNote non static method
	    	if (activity instanceof NotesEditor)
	        {
	            EditorAct = (NotesEditor) activity;
	        }
	        super.onAttach(activity);
		}

		@Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the Builder class for convenient dialog construction
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        
	        builder.setMessage("Are you sure?");
	        builder.setTitle("Delete note");
	        
	        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                	   confirmDel = true;
	                	   
	                	   Log.i("Dialog","Delete");
	                	   
	                	   //calling delete note method	       		    		
	                	   EditorAct.delNote();	                       	                       
	                   }
	               });
	        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                	   confirmDel = false;
	                       Log.i("Dialog","Cancel");
	                   }
	               });
	        
	        
	        // Create the AlertDialog object and return it
	        return builder.create();
	    }
	}*/

}
