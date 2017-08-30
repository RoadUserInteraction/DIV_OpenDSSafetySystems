package se.christiannils.safetysystems;

public class SafetySystemOutput {
	boolean warning = false;
	float steeringAngle = 0f;
	float brakePedal = 0f;
	
	public SafetySystemOutput (boolean warning, float steeringAngle, float brakePedal){
		this.warning = warning;
		this.steeringAngle = steeringAngle;
		this.brakePedal = brakePedal;
	}
	
	public boolean isWarning() 
	{
		return warning;
	}


	public float getSteeringAngle()
	{
		return steeringAngle;
	}


	public float getBrakePedal() 
	{
		return brakePedal;
	}

}
