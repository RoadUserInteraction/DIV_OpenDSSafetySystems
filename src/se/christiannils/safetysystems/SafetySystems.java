/*
*  This file is part of the safety systems package for OpenDS (Open Source Driving Simulator).
*  Copyright (C) 2017 Christian-Nils Åkerberg Boda
*
*/

package se.christiannils.safetysystems;

import se.christiannils.sensors.Sensors;

import eu.opends.main.Simulator;
import eu.opends.car.SteeringCar;

public class SafetySystems {
	boolean warning = false;
	float steeringAngle, brakePedal;
	private ForwardCollisionWarning FCW;
	private AutonomousEmergencyBraking AEB;
	private Simulator sim;
	private SteeringCar car;
	private Sensors sensors;
	
	public SafetySystems(Simulator sim, SteeringCar car, boolean FCW, boolean AEB){
		this.sim = sim;
		this.car = car;
		this.sensors = new Sensors(sim, car);
		this.FCW = new ForwardCollisionWarning(car, sensors, FCW);
		this.AEB = new AutonomousEmergencyBraking(car, sensors, FCW);
		// Initiate the safety systems
	}
	
	public SafetySystemOutput update(float tpf){
		
		// Update sensors
		this.sensors.update(tpf);
		// Update the systems
		this.FCW.update(tpf);
		this.AEB.update(tpf);
		
		this.warning = this.FCW.isWarning();
		this.steeringAngle = 0;
		this.brakePedal = this.AEB.getBraking();
		
		return new SafetySystemOutput(warning, steeringAngle, brakePedal);
	}
	

}
