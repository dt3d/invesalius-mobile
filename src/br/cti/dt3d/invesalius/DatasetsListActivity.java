package br.cti.dt3d.invesalius;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class DatasetsListActivity extends Activity implements OnItemClickListener, OnItemLongClickListener, OnClickListener{
	
	static ListView lv1;
	static EditText et1;
	ArrayList<String> array = new ArrayList<String>();
	File f = new File(InVesaliusMobileActivity.diretorio);
	static ProgressDialog dialog;
	static CharSequence toDelete = "NULL";
	public static Context c;
	public static Activity a;
	
	ArrayAdapter<String> aa;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.list);
		c = this;
		a = this;
		
		if(f.isDirectory()){
			File[] files = f.listFiles();
			
			for(int i=0; i<files.length;i++){
				if(files[i].isDirectory() && !(files[i].isHidden())) array.add(files[i].getName());
			}
		}
		
		// Ordenar upper e lowercase juntos, porque o Collections.sort(), utilizado anteriormente, ordena maiúsculas e depois minúsculas.
		int i = 0;
		String[] arrayAux = new String[array.size()];
		for (String a : array) {
			arrayAux[i] = a;
			i++;
		}
		Arrays.sort(arrayAux, new AlphabeticComparator());
		array = new ArrayList<String>();
		for (int j = 0; j < i; j++){
			array.add(j, arrayAux[j]);
		}
		// *1 (tem um adendo no final do código para essa parte)

		final int array_size = array.size();
		
		lv1=(ListView)findViewById(R.id.listView1);
		et1 = (EditText)findViewById(R.id.editText1);
	
	    et1.addTextChangedListener(new TextWatcher(){
	        public void afterTextChanged(Editable s) {
	        	String[] busca = DatasetsListActivity.et1.getText().toString().toLowerCase().split(" ");
	        	ArrayList<String> lista = new ArrayList<String>();
	        	for (int i = 0; i < array_size; i++) {
	        		if (!array.get(i).toLowerCase().equalsIgnoreCase("download demos...")){
		        		if (busca.length == 0 || (busca.length == 1 && busca[0].equalsIgnoreCase(""))){ 
		        			lista.add(array.get(i));
		        		}else{
			        		Integer[] encontrados = new Integer[busca.length];
			        		for (int m = 0; m < busca.length; m++) encontrados[m] = 0;
			        		String[] opcao = array.get(i).toLowerCase().split(" ");
			        		for (int j = 0; j < opcao.length; j++){
			        			for (int k = 0; k < busca.length; k++){
					        	   	if (opcao[j].contains(busca[k])){ 
					        	   		encontrados[k]++;
					        	   	}
			        			}
			        		}
			        		Boolean verifica = true;
			        		for (int l = 0; l < busca.length; l++){
			        			if (encontrados[l] == 0)
			        				verifica = false;
			        		}
			        		if (verifica)
			        			lista.add(array.get(i));
		        		}
		        	}
        		}
	        	if (!lista.contains("Download demos..."))
	        		lista.add("Download demos...");
	        	aa =  new ArrayAdapter<String>(DatasetsListActivity.this,android.R.layout.simple_list_item_1, lista);
	        	lv1=(ListView)findViewById(R.id.listView1);
	            lv1.setAdapter(aa);       	
	        }
	        
			public void beforeTextChanged(CharSequence s, int start, int count, int after){}
	        public void onTextChanged(CharSequence s, int start, int before, int count){}
	    }); 
		
		// By using setAdpater method in listview we an add string array in list.
	    aa = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1 , array);
		lv1.setAdapter(aa);
		aa.notifyDataSetChanged();
		lv1.setOnItemClickListener(this);
		lv1.setOnItemLongClickListener(this);

	}
	
	@Override	
    protected void onResume() {
        super.onResume();

        if (f != null && f.exists()){
        	if (!array.contains("Download demos..."))
        		array.add("Download demos...");
        }
    }
	
	public void onItemClick(AdapterView<?> parent, View view,	
	        int position, long id) {
			if(((TextView) view).getText().equals("Download demos...")){
				Intent intent = new Intent(this, ExamplesListActivity.class);
		    	startActivity(intent);
			}
			else{
				Intent intent = new Intent(this, OrientacaoActivity.class);
		    	intent.putExtra("DIR",InVesaliusMobileActivity.diretorio+"/"+((TextView) view).getText());
		    	startActivity(intent);
			}	    	
	}
	
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
		if(!((TextView) view).getText().equals("Download demos...")){
			toDelete = ((TextView) view).getText();
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Are you sure ?");
			builder.setCancelable(false);
			builder.setPositiveButton("Yes", this);
			builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			           }
			       });
			builder.setTitle("Delete Files");
			AlertDialog alert = builder.create();
			alert.show();
		}
		return true;
	}
	
	public void onClick(DialogInterface dialog, int id){
		delete();
	}
	
	public void delete(){
		new DeleteDirTask().execute(InVesaliusMobileActivity.diretorio+"/"+toDelete);
		array.remove(toDelete);
		lv1.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1 , array));
	}
	
}

// *1 - Comparator utilizado na ordenação.
class AlphabeticComparator implements Comparator<Object> {
	public int compare(Object o1, Object o2) {
		String s1 = (String) o1;
		String s2 = (String) o2;
		return s1.toLowerCase().compareTo(s2.toLowerCase());
	}
}