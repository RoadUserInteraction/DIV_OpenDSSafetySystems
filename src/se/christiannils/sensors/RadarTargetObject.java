package se.christiannils.sensors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RadarTargetObject {

	private String type;
	private Map<String, Integer> centroid = new HashMap<String, Integer>();
	private Map<String, List<RadarTargetData>> dataArray = new HashMap<String, List<RadarTargetData>>();
	
	public RadarTargetObject (String type){
		this.type = type;		
	}
	
	public void add (String key, float rho, float theta){
		
		RadarTargetData data = new RadarTargetData(rho, theta);
		
		if (dataArray.containsKey(key)){
			List<RadarTargetData> oldData = dataArray.get(key);
			if (oldData.add(data)){
				dataArray.replace(key, oldData);
			}
		} else {
			List<RadarTargetData> dataList = new ArrayList<RadarTargetData>();
			if (dataList.add(data)){
				dataArray.put(key, dataList);
			}
		}
				
		return;
	}
	
	public void updateAllCentroids() {
		
		for (String key : this.getAllKeys()){
			updateCentroid(key);
		}
		
		return;
	}
	private void updateCentroid(String key){
		int size = dataArray.get(key).size();
		if ( (size & 1) == 0 ) {
			centroid.put(key, (size)/2-1);
		} else {
			centroid.put(key, (size-1)/2); 
		}
	}
	
	public List<RadarTargetData> getAllData(String key){
		return dataArray.get(key);
	}
	
	public RadarTargetData getData(String key){
		return dataArray.get(key).get(centroid.get(key)); // return data from centroid		
	}
	
	public String getType(){
		return type;
	}
	
	public Set<String> getAllKeys(){
		return dataArray.keySet();
	}
}
