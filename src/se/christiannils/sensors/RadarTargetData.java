package se.christiannils.sensors;

public class RadarTargetData {
	private float rho; // distance to point
	private float theta; // bearing to point	
	
	public RadarTargetData(float rho, float theta) {
		this.rho = rho;
		this.theta = theta;
	}

	public float getRho() {
		return rho;
	}

	public void setRho(float rho) {
		this.rho = rho;
	}

	public float getTheta() {
		return theta;
	}

	public void setTheta(float theta) {
		this.theta = theta;
	}
}
