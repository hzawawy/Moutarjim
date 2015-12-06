package com.example.messenger;

import java.net.DatagramSocket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.example.messenger.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
	Button signupButton, signinButton, closeButton;
	boolean keepListening = true;
	DatagramSocket serverSocket;
	ConnectionManager connectionMgr = new ConnectionManager();
	boolean running = false;
	Process pr;
	final int SIGNUP_CODE = 1;
	final int SIGNIN_CODE = 2;
	@Override
	   public void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.activity_main);
//	      getActionBar().setDisplayHomeAsUpEnabled(true);
	      
//	      TextView msgTextView = (TextView) findViewById(R.id.textView2);
//	      msgTextView.setText("Enter your first name: ");

	      // Adding the signup button
	      this.signupButton = (Button)this.findViewById(R.id.signup);
	      OnClickListener signuplistener = new OnClickListener() {
		        @Override
		        public void onClick(View v) {
		          signup();
		        }
		      };
	      this.signupButton.setOnClickListener(signuplistener);

	      // Adding the signin button
	      this.signinButton = (Button)this.findViewById(R.id.signin);
	      OnClickListener signinlistener = new OnClickListener() {
		        @Override
		        public void onClick(View v) {
		          signin();
		        }
		      };
	      this.signinButton.setOnClickListener(signinlistener);

	      // Adding the close button
	      this.closeButton = (Button)this.findViewById(R.id.close);
	      OnClickListener listener = new OnClickListener() {
		        @Override
		        public void onClick(View v) {
		        	finish();
		        }
		      };
	      this.closeButton.setOnClickListener(listener);
	   }

	@Override
	   public boolean onCreateOptionsMenu(Menu menu) {
		   super.onCreateOptionsMenu(menu);
		   getMenuInflater().inflate(R.menu.main, menu);
		   getMenuInflater().inflate(R.menu.search, menu);
		   return super.onCreateOptionsMenu(menu);
	   }
	   
	   @Override
	   public boolean onOptionsItemSelected(MenuItem item) {
	       // Handle item selection
	       switch (item.getItemId()) {
	           case R.id.search:
	               // do nothing for now
	               return true;
	           case R.id.signup:
	        	   signup();
	               return true;
	           case R.id.signin:
	        	   signin();
	               return true;
	           case R.id.close:
	        	   finish();
	               return true;
	           default:
	               return super.onOptionsItemSelected(item);
	       }
	   }
	   
	   private void signup(){
		   Intent intent = new Intent(this, SignupActivity.class);
		   startActivityForResult(intent, SIGNUP_CODE);
	   }
	   
	   private void signin(){
		   Intent intent = new Intent(this, SigninActivity.class);
		   startActivityForResult(intent, SIGNIN_CODE);
	   }
	   
	   @Override 
	   public void onActivityResult(int requestCode, int resultCode, Intent data) {     
//			super.onActivityResult(requestCode, resultCode, data);
//			if (requestCode == SIGNUP_CODE) {
//			     if(resultCode == RESULT_OK){
//			      String stredittext=data.getStringExtra("edittextvalue");
//			}
		   
			super.onActivityResult(requestCode, resultCode, data);
		     switch(requestCode) { 
		       case (SIGNUP_CODE): { 
		    	   if (resultCode == RESULT_OK) { 
		    		   String signupData = data.getStringExtra("signup");
		    		   connectionMgr.setAction("signup");
		    		   connectionMgr.setSendData(signupData.getBytes());
			   			try {
			   				connectionMgr.execute().get(3000, TimeUnit.MILLISECONDS);
			   			} catch(TimeoutException te){
				   			signinComplete(getString(R.string.timeoutexception));
			   			} catch(ExecutionException ee){
				   			signinComplete(getString(R.string.executionexecption));			   				
			   			} catch(InterruptedException ie){
				   			signinComplete(getString(R.string.interruptionexecption));			   				
			   			}
		    		   signupComplete(new String(connectionMgr.getReceivedData()));
		    	   } 
		    	   break; 
		       } 
		       case (SIGNIN_CODE): {
			   		if (resultCode == RESULT_OK) { 
			   			String signinData = data.getStringExtra("signin");
			   			connectionMgr.setAction("signin");
			   			connectionMgr.setSendData(signinData.getBytes());
			   			try {
			   				connectionMgr.execute().get(3000, TimeUnit.MILLISECONDS);
			   			} catch(TimeoutException te){
				   			signinComplete(getString(R.string.timeoutexception));
			   			} catch(ExecutionException ee){
				   			signinComplete(getString(R.string.executionexecption));			   				
			   			} catch(InterruptedException ie){
				   			signinComplete(getString(R.string.interruptionexecption));			   				
			   			}
			   			signinComplete(new String(connectionMgr.getReceivedData()));
				        // TODO Update your TextView.
			         } 
			         break; 
		       }
		     } 
	   	}
	   private void signupComplete(String signupResult){
		   if (signupResult.contains(getString(R.string.successful_signup))){
			   
		   }
	   }
	   private void signinComplete(String signinResult){
		   if (signinResult.contains(getString(R.string.successful_signin))){
			   
		   }
	   }
}
