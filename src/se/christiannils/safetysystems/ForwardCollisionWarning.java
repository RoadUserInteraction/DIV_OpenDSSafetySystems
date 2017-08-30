package se.christiannils.safetysystems;
import se.christiannils.sensors.ForwardRadar;
import se.christiannils.sensors.RadarTargetObject;
import se.christiannils.sensors.Sensors;

import eu.opends.car.SteeringCar;

// the two following imports are used to make the java compiled matlab function work
import com.mathworks.toolbox.javabuilder.*;
import SafetySystem.*;

public class ForwardCollisionWarning {
	boolean warning = false, enable = false;
	private SteeringCar car;
	private ForwardRadar fRadar;
	private RadarTargetObject radarTargets;
	private float tpf;
	
	// Load java class from the MATLAB function, comment in if not used
	SafetySystem ss = null;
	Object[] fcwResult = null;
	
	public ForwardCollisionWarning(SteeringCar car, Sensors sensors, boolean enable){
		this.enable = enable; 
		this.car = car;
		this.fRadar = sensors.getForwardRadar();
		
		// To comment in if the function from Matlab is not used
		try {
			this.ss = new SafetySystem();
		} catch (MWException e) {
			System.out.println("Could not create the FCW object from Matlab function");
		}
	}
	
	public boolean isEnable(){
		return enable;
	}
	
	public void update(Float tpf){		
		// Threat assessment 
		this.tpf = tpf;
		this.warning = this.ThreatAssessment();
	}
	
	public boolean isWarning(){
		return warning;
	}
	
	public boolean ThreatAssessment(){
		// Initialize variables
		boolean warning = false;
		float rho, theta, previousRho, rangeRate;
		RadarTargetObject radarTargets = fRadar.getRadarTargets();
		RadarTargetObject previousRadarTargets = this.radarTargets;
		
		for (String key: radarTargets.getAllKeys()){
			rho = radarTargets.getData(key).getRho();
			theta = radarTargets.getData(key).getTheta();
			if (previousRadarTargets!=null){
				if (previousRadarTargets.getAllKeys().contains(key)){
					previousRho = previousRadarTargets.getData(key).getRho();
					rangeRate = (rho-previousRho)/tpf;
				} else {
					rangeRate = Float.NaN;
				}
				
//******************************************* to be changed by the students******************************************//
				
//				// Either via JAVA (See below example)
//				// assign the TTC (positive) if the range decreases otherwise (+)infinity				
//				float TTC = (rho/rangeRate<0) ? -rho/rangeRate : Float.POSITIVE_INFINITY; 
//
//				// Threat assessment based on TTC
//				if (TTC < 2){ // Under 2 seconds
//					// Check if the steering car is moving (do not have to warn if not moving)
//					if (car.getCurrentSpeedMs()>1){
//						// Check if opponent car is on the trajectory of the car (here on longitudinal trajectory within a 4-m wide corridor)
//						
//						if (Math.abs(rho*Math.sin(Math.toRadians(theta)))<1){
//							warning = true;
//						}
//					}
//				}
				
				// Or via compiling MATLAB's function									
				try {
					fcwResult = ss.FCW(1, rho, theta, rangeRate, car.getCurrentSpeedMs());
				} catch (MWException e) {
					System.out.println("Exception: " + e.toString()); // if there is an error when evaluating the function
				}
				warning = ((MWLogicalArray)fcwResult[0]).getBoolean(1); // get the first object in fcwResult variables. 
				// cast it to MWLogicalArray, which is the class of the output variable. Then convert it in java primitive boolean.
				// see (http://soliton.ae.gatech.edu/classes/ae6382/documents/matlab/mathworks/javabuilder.pdf)
				
//***************************************************************************************************************//
			}			
		}
		
		// store the radarTargets object for the next iteration used to calculate range rate
		this.radarTargets = radarTargets; 
		
		return warning;
	}
}
