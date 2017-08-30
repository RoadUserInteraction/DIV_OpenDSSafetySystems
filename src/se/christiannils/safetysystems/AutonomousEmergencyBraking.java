package se.christiannils.safetysystems;

import com.mathworks.toolbox.javabuilder.MWException;
import com.mathworks.toolbox.javabuilder.MWLogicalArray;
import com.mathworks.toolbox.javabuilder.MWNumericArray;

import SafetySystem.*;
import eu.opends.car.SteeringCar;
import se.christiannils.sensors.ForwardRadar;
import se.christiannils.sensors.RadarTargetObject;
import se.christiannils.sensors.Sensors;

public class AutonomousEmergencyBraking {
	boolean enable = false;
	float braking = 0f;
	private SteeringCar car;
	private ForwardRadar fRadar;
	private RadarTargetObject radarTargets;
	private float tpf;
	
	// Load java class from the MATLAB function, comment in if not used
	SafetySystem ss = null;
	Object[] aebResult = null;
	
	public AutonomousEmergencyBraking(SteeringCar car, Sensors sensors, boolean enable){
		this.enable = enable; 
		this.car = car;
		this.fRadar = sensors.getForwardRadar();
		
		// To comment in if the function from Matlab is not used
		try {
			this.ss = new SafetySystem();
		} catch (MWException e) {
			System.out.println("Could not create the AEB object from Matlab function");
		}
	}
	
	public boolean isEnable(){
		return enable;
	}
	
	public void update(Float tpf){		
		// Threat assessment 
		this.tpf = tpf;
		this.braking = this.ThreatAssessment();
	}
	
	public float getBraking(){
		return braking;
	}
	
	public float ThreatAssessment(){
		// Initialize variables
		float braking = 0f, rho, theta, previousRho, rangeRate;
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
				
//				// Either via JAVA 
				
				// Or via compiling MATLAB's function									
				try {
					aebResult = ss.AEB(1, this.braking, rho, theta, rangeRate, car.getCurrentSpeedMs());
				} catch (MWException e) {
					System.out.println("Exception: " + e.toString()); // if there is an error when evaluating the function
				}
				
				if (braking<((MWNumericArray)aebResult[0]).getFloat(1)){
					braking = ((MWNumericArray)aebResult[0]).getFloat(1); // convert MATLAB type to java's Float
				}
				
//***************************************************************************************************************//
			}			
		}
		
		// store the radarTargets object for the next iteration used to calculate range rate
		this.radarTargets = radarTargets; 
		
		return braking;
	}
}
