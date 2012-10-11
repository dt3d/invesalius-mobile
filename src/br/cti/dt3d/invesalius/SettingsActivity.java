package br.cti.dt3d.invesalius;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends Activity implements OnClickListener{

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.settings);
        Button download = (Button)findViewById(R.id.downloadDemos);
        download.setOnClickListener(this);
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
				validadePath(dir, editor, settings);
				break;
			case R.id.cancel:
	    		editor.putString("DIR", InVesaliusMobileActivity.diretorio);
	    		editor.commit();
	    		Log.v("ivm","3:"+settings.getString("DIR","0"));
	    		finish();
	    		break;
			case R.id.downloadDemos:
				if (validadePath(dir, editor, settings)){
					Intent intent = new Intent(this, ExamplesListActivity.class);
					startActivity(intent);
				}
				break;
		}
	}
	
	public boolean validadePath(EditText dir, SharedPreferences.Editor editor, SharedPreferences settings){
		File f = new File(dir.getText().toString());
		if (f.isDirectory()){
			InVesaliusMobileActivity.diretorio = dir.getText().toString();
    		editor.putString("DIR", InVesaliusMobileActivity.diretorio);
    		editor.commit();
    		Log.v("ivm","3:"+settings.getString("DIR","0"));
    		finish();
    		return true;
		}else{
			AlertDialog builder = new AlertDialog.Builder(this).create();
			builder.setTitle("Error");
			builder.setMessage("Path not found.");
			builder.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
				}
			});
			builder.show();
			return false;
        }
	}
}
