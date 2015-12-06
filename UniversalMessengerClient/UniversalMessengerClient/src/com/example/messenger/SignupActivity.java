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

public class SignupActivity extends AppCompatActivity {
	EditText firstnameEdit, lastnameEdit, languageEdit, usernameEdit, passwordEdit, repasswordEdit;
	
	@Override
	   public void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.activity_sign_up);
	      
	      TextView firstnameView = (TextView) findViewById(R.id.firstname);
	      firstnameView.setText("Enter your first name: ");
	      
	      firstnameEdit = (EditText) findViewById(R.id.firstnameEdit);
	      
	      TextView lastnameView = (TextView) findViewById(R.id.lastname);
	      lastnameView.setText("Enter your last name: ");

	      lastnameEdit = (EditText) findViewById(R.id.lastnameEdit);
	      
	      TextView languageView = (TextView) findViewById(R.id.language);
	      languageView.setText("Enter your preferred language: ");

	      languageEdit = (EditText) findViewById(R.id.languageEdit);
	      
	      TextView usernameView = (TextView) findViewById(R.id.username);
	      usernameView.setText("Enter your username: ");

	      usernameEdit = (EditText) findViewById(R.id.usernameEdit);
	      
	      TextView passwordView = (TextView) findViewById(R.id.password);
	      passwordView.setText("Enter your password: ");

	      passwordEdit = (EditText) findViewById(R.id.passwordEdit);
	      
	      TextView repasswordView = (TextView) findViewById(R.id.repassword);
	      repasswordView.setText("Re-enter your password: ");

	      repasswordEdit = (EditText) findViewById(R.id.repasswordEdit);

	      // Adding the OK button
	      Button okButton = (Button)this.findViewById(R.id.okButton);
	      okButton.setText("Ok");
	      OnClickListener okButtonListener = new OnClickListener() {
		        @Override
		        public void onClick(View v) {
		        	if (firstnameEdit.getText()!=null && lastnameEdit.getText()!=null && 
		        			languageEdit.getText()!=null && usernameEdit.getText()!=null &&
		        			passwordEdit.getText()!=null && repasswordEdit.getText()!=null){
		        		if (!passwordEdit.getText().toString().equals(repasswordEdit.getText().toString())){
		        			// send error message that password and re-password should match
		        		}
		        		StringBuilder builder = new StringBuilder();
		        		builder.append(usernameEdit.getText().toString()+",");
		        		builder.append("signup,");
		        		builder.append(firstnameEdit.getText().toString()+",");
		        		builder.append(lastnameEdit.getText().toString()+",");
		        		builder.append(languageEdit.getText().toString()+",");
		        		builder.append(passwordEdit.getText().toString());
		        		serverSignup(builder.toString());
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
	
	private void serverSignup(String signupText){
		Intent resultIntent = new Intent();
		resultIntent.putExtra("signup",signupText);
		setResult(Activity.RESULT_OK, resultIntent);
		finish();
	}
}
