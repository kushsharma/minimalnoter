package com.softnuke.noter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

public class ConfirmDeleteDialog extends DialogFragment {
	private DeleteCommunicator callBack;	
	
	// Container Activity must implement this interface
    public interface DeleteCommunicator {
        public void deleteNoteCallBack(boolean confirm,int id);
    }
	
    //constructor kindof
    public static ConfirmDeleteDialog newInstance(int num){    	
    	
    	ConfirmDeleteDialog f = new ConfirmDeleteDialog();
    	
    	// Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("noteid", num);
        f.setArguments(args);

        return f;
    } 
    
    @Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub    	
    	
        super.onAttach(activity);       
        
        
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
        	callBack = (DeleteCommunicator) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement DeleteCommunicator");
        }
	}

	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		final int noteid = getArguments().getInt("noteid");
		
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        
        builder.setMessage("Are you sure?");
        builder.setTitle("Delete note");
        
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   
                	   // Send the event to the host activity
                	   callBack.deleteNoteCallBack(true, noteid);
                       
                	   Log.i("Dialog","Delete");
                	                          	                       
                   }
               });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   
                       Log.i("Dialog","Cancel");
                   }
               });
        
        
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
