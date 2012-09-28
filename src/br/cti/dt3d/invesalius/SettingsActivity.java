package br.cti.dt3d.invesalius;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends Activity implements OnClickListener{

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        Button set = (Button)findViewById(R.id.ok2);
        set.setOnClickListener(this);
        Button cancel = (Button)findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        EditText dir = (EditText)findViewById(R.id.dir);
        dir.setText(InVesaliusMobileActivity.diretorio);
	}

	public void onClick(View v){
		EditText dir = (EditText)findViewById(R.id.dir);
        SharedPreferences settings = getSharedPreferences("general",0);
        Log.v("ivm","2:"+settings.getString("DIR","0"));
        SharedPreferences.Editor editor = settings.edit();
        System.out.println(v.getId());
        System.out.println(R.id.ok2);
		switch(v.getId()){
			case R.id.ok2:
				File f = new File(dir.getText().toString());
				if (f.isDirectory()){
					InVesaliusMobileActivity.diretorio = dir.getText().toString();
		    		editor.putString("DIR", InVesaliusMobileActivity.diretorio);
		    		editor.commit();
		    		Log.v("ivm","3:"+settings.getString("DIR","0"));
		    		finish();
				}else{
					AlertDialog builder = new AlertDialog.Builder(this).create();
					builder.setTitle("Error");
					builder.setMessage("Path not found.");
					builder.setButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
						}
					});
					builder.show();
		        }
				break;
			case R.id.cancel:
	    		editor.putString("DIR", InVesaliusMobileActivity.diretorio);
	    		editor.commit();
	    		Log.v("ivm","3:"+settings.getString("DIR","0"));
	    		finish();
	    		break;
		}
	}
}
