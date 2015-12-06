package com.example.messenger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SigninActivity extends AppCompatActivity {
	EditText usernameText, passwordText;
	
	@Override
	   public void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.activity_sign_in);

	      TextView msgTextView = (TextView) findViewById(R.id.username);
	      msgTextView.setText("Enter your username name: ");
	      
	      usernameText = (EditText) findViewById(R.id.usernameEdit);
	      
	      TextView passwordTextView = (TextView) findViewById(R.id.password);
	      passwordTextView.setText("Enter your password: ");

	      passwordText = (EditText) findViewById(R.id.passwordEdit);
	      
	      // Adding the OK button
	      Button okButton = (Button)this.findViewById(R.id.okButton);
	      okButton.setText("Ok");
	      OnClickListener okButtonListener = new OnClickListener() {
		        @Override
		        public void onClick(View v) {
		        	if (usernameText.getText()!=null && passwordText.getText()!=null){
		        		StringBuilder builder = new StringBuilder();
		        		builder.append(usernameText.getText().toString()+",");
		        		builder.append("signin,");
		        		builder.append(passwordText.getText().toString());
		        		serverSignin(builder.toString());
		        	}
		        }
		      };
	      okButton.setOnClickListener(okButtonListener);

	      // Adding the Cancel button
	      Button cancelButton = (Button)this.findViewById(R.id.cancelButton);
	      cancelButton.setText("Cancel");
	      OnClickListener cancelButtonListener = new OnClickListener() {
		        @Override
		        public void onClick(View v) {
		        	finish();
		        }
		      };
		  cancelButton.setOnClickListener(cancelButtonListener);

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	private void serverSignin(String signinText){
		Intent intent=new Intent();  
        intent.putExtra("signin",signinText);  
        setResult(Activity.RESULT_OK, intent);  
        finish();
	}
}
