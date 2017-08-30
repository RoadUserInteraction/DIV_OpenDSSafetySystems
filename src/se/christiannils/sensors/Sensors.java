package se.christiannils.sensors;

import se.christiannils.sensors.*;

import eu.opends.main.Simulator;
import eu.opends.car.SteeringCar;

public class Sensors {
// at some point, there will be some sensor fusion
	private Simulator sim;
	private SteeringCar car;
	private ForwardRadar fRadar;
	
	public Sensors(Simulator sim, SteeringCar car){
		this.fRadar = new ForwardRadar(sim, car);
	}
	
	public void update(float tpf){
		this.fRadar.update(tpf);
	}
	
	public ForwardRadar getForwardRadar(){
		return fRadar;
	}
}
