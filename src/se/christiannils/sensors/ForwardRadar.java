package se.christiannils.sensors;

import eu.opends.main.Simulator;
import eu.opends.traffic.PhysicalTraffic;
import eu.opends.traffic.TrafficObject;
import eu.opends.traffic.TrafficCar;
import eu.opends.traffic.TrafficCarData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.vecmath.Tuple3f;

import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.PhysicsRayTestResult;
import com.jme3.bullet.collision.PhysicsSweepTestResult;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.CompoundCollisionShape;
import com.jme3.bullet.collision.shapes.ConeCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.collision.Collidable ;
import com.jme3.collision.CollisionResult;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.bullet.objects.PhysicsCharacter;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.bullet.objects.PhysicsVehicle;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Dome;
import com.jme3.util.TempVars;

import eu.opends.car.Car;
import eu.opends.car.SteeringCar;

public class ForwardRadar {
	private Simulator sim;
	private SteeringCar car;
	private Vector3f radarPosition = new Vector3f(0f, 0.4f, -2.6f);
	private RadarTargetObject radarTargets;
	
	public ForwardRadar(Simulator sim, SteeringCar car){
		this.sim = sim;
		this.car = car;		
		// We implement this radar here: http://www.delphi.com/manufacturers/auto/safety/active/electronically-scanning-radar
		// There are two cones
	}
	
	public void update(float tpf){	
		// function to update the radar's targets
		updateTargets(tpf);
	}
	
	private Node getFirstLevelParent(Geometry geo){
		Node parent = geo.getParent();
		
		if (parent.equals(sim.getSceneNode())){
			return new Node();
		}
		
		while (!parent.getParent().equals(sim.getSceneNode())){
			parent = parent.getParent();
		}

		return parent;
	}
	
	public void updateTargets(float tpf){
		// Method to update the radarTargetObjects map
		float limit;
		String targetObjectKey;		
		
		radarTargets = new RadarTargetObject("Car");
		
		for (float angle = -45f; angle<=45; angle+=0.1f){
			if (Math.abs(angle)>10){
				limit = 60f; // mid range limit
			} else {
				limit = 174f; // long range limit
			}
			
			CollisionResults results = new CollisionResults();
			Ray ray = new Ray(car.getPosition().add(car.getRotation().mult(radarPosition)), car.getRotation().mult(new Quaternion(0,1,0,(float)Math.toRadians(angle))).getRotationColumn(2));
			ray.setLimit(limit);
			sim.getSceneNode().collideWith(ray, results);
			
			if (results.size() > 0) {
			  	CollisionResults onlyVehicleResults = new CollisionResults();
			  	for (CollisionResult result : results){
			  		if (getFirstLevelParent(result.getGeometry()).getControl(VehicleControl.class)!=null){ //store only the collision results with the vehicles
			  			onlyVehicleResults.addCollision(result);
			  		}
			  	}
			  	if (onlyVehicleResults.size()>0){
				    CollisionResult closest  = onlyVehicleResults.getClosestCollision();
				    targetObjectKey = Integer.toHexString(getFirstLevelParent(closest.getGeometry()).hashCode());
				    radarTargets.add(targetObjectKey, closest.getDistance(), angle);
			  	}
			  }
		}
		
		radarTargets.updateAllCentroids(); // update all centroids
		return;
	}


	public RadarTargetObject getRadarTargets() {
		return radarTargets;
	}
	
}
