package com.spinages.shakeMotion;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.spinages.app.R;
import com.spinages.webservices.ServiceHitter;

public class MainAccelerometer extends Activity implements AccelerometerListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.);
		
		// Check onResume Method to start accelerometer listener
	}
   
	public void onAccelerationChanged(float x, float y, float z) {
		// TODO Auto-generated method stub
		
	}

	public void onShake(float force) {
		
		// Called when Motion Detected


		
	}

	@Override
    public void onResume() {
            super.onResume();

            
            //Check device supported Accelerometer senssor or not
            if (AccelerometerManager.isSupported(this)) {
            	
            	//Start Accelerometer Listening
    			AccelerometerManager.startListening(this);
            }
    }
	
	@Override
    public void onStop() {
            super.onStop();
            
            //Check device supported Accelerometer senssor or not
            if (AccelerometerManager.isListening()) {
            	
            	//Start Accelerometer Listening
    			AccelerometerManager.stopListening();
    			

            }
           
    }
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i("Sensor", "Service  distroy");
		
		//Check device supported Accelerometer senssor or not
		if (AccelerometerManager.isListening()) {
			
			//Start Accelerometer Listening
			AccelerometerManager.stopListening();
			

        }
			
	}

}
