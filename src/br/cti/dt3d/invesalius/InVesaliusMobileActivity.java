package br.cti.dt3d.invesalius;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class InVesaliusMobileActivity extends Activity implements OnClickListener{
    /** Called when the activity is first created. */

	static String diretorio = "/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);

        SharedPreferences settings = getSharedPreferences("general",0);
        SharedPreferences.Editor editor = settings.edit();
        diretorio = settings.getString("DIR", "0");
        if(diretorio.equals("0")){
        	diretorio = Environment.getExternalStorageDirectory() + "/invesalius";
        	editor.putString("DIR", diretorio);
        	editor.commit();
        }

        ImageView logoInvesalius = (ImageView)findViewById(R.id.logoInvesalius);
        Button datasets = (Button)findViewById(R.id.datasets);
        datasets.setOnClickListener(this);
        Button config = (Button)findViewById(R.id.config);
        config.setOnClickListener(this);
        Button help = (Button)findViewById(R.id.help);
        help.setOnClickListener(this);
        ImageView logo = (ImageView)findViewById(R.id.logo2);
        logo.setOnClickListener(this);
    }

    public void onClick(View v){
    	Intent intent;
    	// Verifica qual botão foi pressionado
		File f = new File(InVesaliusMobileActivity.diretorio);
		if (!f.exists()){
			f.mkdir();
		}
    	switch(v.getId()){
    		case R.id.datasets:
    			if (f.list() != null && f.list().length > 0){
    				intent = new Intent(this, DatasetsListActivity.class);
    			}else{
    				AlertDialog.Builder builder = new AlertDialog.Builder(this);
    				builder.setMessage("No datasets found. Do you want to download demo images?");
    				builder.setCancelable(false);
    				builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			        	public void onClick(DialogInterface dialog, int id) {
			            	dialog.cancel();
			        	}
    				});
    				builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
    					public void onClick(DialogInterface dialog, int id) {
    						Intent intent = new Intent(InVesaliusMobileActivity.this, ExamplesListActivity.class);
    						startActivity(intent);
    					}
    				});
    				builder.setTitle("No Datasets");
    				AlertDialog alert = builder.create();
    				alert.show();
    				break;
    			}
	    		startActivity(intent);
	    		break;
	       	case R.id.config:
	       		intent = new Intent(this, SettingsActivity.class);
	       		startActivity(intent);
	       		break;
	       	case R.id.logo2:
	       		MostraSobre(this);
	       		break;
	       	case R.id.help:
	       		String url = "http://www.cti.gov.br/dt3d/invesalius-mobile/help.html";
	    		Intent i = new Intent(Intent.ACTION_VIEW);
	    		i.setData(Uri.parse(url));
	    		startActivity(i);
	    }
    }

    public void mudaDiretorio(String dir){
    	SharedPreferences settings = getSharedPreferences("general",0);
        Log.v("ivm","2:"+settings.getString("DIR","0"));
        SharedPreferences.Editor editor = settings.edit();
        InVesaliusMobileActivity.diretorio = dir;
		editor.putString("DIR", InVesaliusMobileActivity.diretorio);
		editor.commit();
    }

    public static void MostraSobre(Context context){
    	AlertDialog.Builder builder = new AlertDialog.Builder(context);
		// Mostra a dialog box "Sobre"
    	builder.setMessage("Versão 1.0\nCentro de Tecnologia da Informação Renato Archer\n(www.cti.gov.br)\n\nAuthors:\nGuilherme H. P. da Silva\nGuilherme C. S. Ruppert\nWesley T. S. T. Ide\n");
		builder.setCancelable(true);
		builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.show();
    }
}
