package com.example.touch;

import java.awt.event.KeyEvent;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.desktop.R;

public class SetActivity extends Activity{

	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.set_layout);
	    preferences = this.getApplicationContext().getSharedPreferences("configInfo", Context.MODE_PRIVATE);
	    editor = preferences.edit();
		Button save = (Button) findViewById(R.id.save);
		save.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View view) {
				
				
	        	editor.putInt("button1",getUnicode(((EditText)findViewById(R.id.up_edit)).getText().toString()));
	        	editor.putInt("button2",getUnicode(((EditText)findViewById(R.id.left_edit)).getText().toString()));
	        	editor.putInt("button3",getUnicode(((EditText)findViewById(R.id.right_edit)).getText().toString()));
	        	editor.putInt("button4",getUnicode(((EditText)findViewById(R.id.down_edit)).getText().toString()));
	        	editor.putInt("buttonR1C1",getUnicode(((EditText)findViewById(R.id.purple_edit)).getText().toString()));
	        	editor.putInt("buttonR1C2",getUnicode(((EditText)findViewById(R.id.green_edit)).getText().toString()));
	        	editor.putInt("buttonR1C3",getUnicode(((EditText)findViewById(R.id.orange_edit)).getText().toString()));
	        	editor.putInt("buttonR2C1",getUnicode(((EditText)findViewById(R.id.red_edit)).getText().toString()));
	        	editor.putInt("buttonR2C2",getUnicode(((EditText)findViewById(R.id.blue_edit)).getText().toString()));
	        	editor.putInt("buttonR2C3",getUnicode(((EditText)findViewById(R.id.white_edit)).getText().toString()));
	        	editor.commit();
	        	Toast.makeText(SetActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
	        
			}
		});
		
		Button cancel = (Button) findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View view) {
				
				
			}
		});
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		this.onDestroy();
	}

	private int getUnicode(String str)
	{
		if("".equals(str) || str.trim().equals("") || str == null)
		{
			return 0;
		}
		int unicode = 0;
		switch(str.toUpperCase().toCharArray()[0])
		{
		case 'A':
			unicode = KeyEvent.VK_A;
			break;
		case 'B':
			unicode = KeyEvent.VK_B;
			break;
		case 'C':
			unicode = KeyEvent.VK_C;
			break;
		case 'D':
			unicode = KeyEvent.VK_D;
			break;
		case 'E':
			unicode = KeyEvent.VK_E;
			break;
		case 'F':
			unicode = KeyEvent.VK_F;
			break;
		case 'J':
			unicode = KeyEvent.VK_J;
			break;
		case 'H':
			unicode = KeyEvent.VK_H;
			break;
		case 'I':
			unicode = KeyEvent.VK_I;
			break;
		case 'G':
			unicode = KeyEvent.VK_G;
			break;
		case 'K':
			unicode = KeyEvent.VK_K;
			break;
		case 'L':
			unicode = KeyEvent.VK_L;
			break;
		case 'M':
			unicode = KeyEvent.VK_M;
			break;
		case 'N':
			unicode = KeyEvent.VK_N;
			break;
		case 'O':
			unicode = KeyEvent.VK_O;
			break;
		case 'P':
			unicode = KeyEvent.VK_P;
			break;
		case 'Q':
			unicode = KeyEvent.VK_Q;
			break;
		case 'R':
			unicode = KeyEvent.VK_R;
			break;
		case 'S':
			unicode = KeyEvent.VK_S;
			break;
		case 'T':
			unicode = KeyEvent.VK_T;
			break;
		case 'U':
			unicode = KeyEvent.VK_U;
			break;
		case 'V':
			unicode = KeyEvent.VK_V;
			break;
		case 'W':
			unicode = KeyEvent.VK_W;
			break;
		case 'X':
			unicode = KeyEvent.VK_X;
			break;
		case 'Y':
			unicode = KeyEvent.VK_Y;
			break;
		case 'Z':
			unicode = KeyEvent.VK_Z;
			break;
		}
		return unicode;
	}

	
}
