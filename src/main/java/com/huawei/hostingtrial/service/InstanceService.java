package com.huawei.hostingtrial.service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.huawei.hostingtrial.domain.Instance;
import com.huawei.hostingtrial.domain.InstanceTypeEnum;
import com.huawei.hostingtrial.repository.InstanceRepository;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;


public class InstanceService {
	
	private static final Logger logger = LoggerFactory.getLogger(InstanceService.class);
	
	private DatabaseReader reader;
	
	private Resource geoipResource =new ClassPathResource("geoip/GeoLite2-City.mmdb");

	@Autowired
	private InstanceRepository instanceRepository;
	
	private List<Instance> gateways, mediaServers, turnServers;
	
	private HashMap<String,List<Instance>> closestServers = new HashMap<String, List<Instance>>();
	
	public void readInstancesFromDatabase(){
		if (gateways==null){
			gateways = instanceRepository.findByInstanceType(InstanceTypeEnum.GATEWAY);
			mediaServers = instanceRepository.findByInstanceType(InstanceTypeEnum.MEDIA);
			turnServers = instanceRepository.findByInstanceType(InstanceTypeEnum.TURN);			
		}		
	}
	
	public HashMap<String,List<Instance>> getClosestServers(String ipAddress){
		readInstancesFromDatabase();
		calculateDistances(ipAddress, gateways);
		calculateDistances(ipAddress, mediaServers);
		calculateDistances(ipAddress, turnServers);
		closestServers.put("Gateway", new LinkedList<Instance>());
		closestServers.get("Gateway").add(gateways.get(0));
		closestServers.get("Gateway").add(gateways.get(1));
		closestServers.put("Media", new LinkedList<Instance>());
		closestServers.get("Media").add(mediaServers.get(0));
		closestServers.put("Turn", new LinkedList<Instance>());
		closestServers.get("Turn").add(turnServers.get(0));
		return closestServers;
	}
		
	public void setGeoipResource(Resource resource){
		this.geoipResource = resource;
		try {
			reader = new DatabaseReader.Builder(this.geoipResource.getFile()).build();		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//get the request ip address, this it the client ip
	public String getIpAddress(HttpServletRequest httpServletRequest){
		String ipAddress = httpServletRequest.getHeader("X-FORWARDED-FOR");  
		if (ipAddress == null) {  
			ipAddress = httpServletRequest.getRemoteAddr();  
		}
		if(ipAddress.contains("127.0.0")){
			ipAddress="107.1.141.74";
		}
		logger.error("the incoming request ip address is "+ipAddress);
		return ipAddress;
	}
	
	public void calculateDistances(String clientIp, List<Instance> gwInstances){
		//create a tempare instance for client
		Instance clientTemp = new Instance();
		clientTemp.setIpAddress(clientIp);
		populateInstanceFromIP(clientTemp);
		//calculate the distance between instance and client
		for(Instance instance : gwInstances){
			double distance = haversine(clientTemp.getLatitude(), clientTemp.getLongitude(), instance.getLatitude(), instance.getLongitude());
			instance.distance = distance;
		}
		//sort by distance 
		Collections.sort(gwInstances, new InstanceCompator());
	}
	
	//populate instance when saving, the ip address comes from input
	public Instance populateInstanceFromIP(Instance instance){
		String ipAddress = instance.getIpAddress();
		CityResponse response;
		try {
			response = reader.city(InetAddress.getByName(ipAddress));

			instance.setCountryCode(response.getCountry().getIsoCode());
			instance.setCountryName(response.getCountry().getName());
			instance.setSubDivisionCode(response.getMostSpecificSubdivision().getIsoCode());
			instance.setSubDivisonName(response.getMostSpecificSubdivision().getName());
			instance.setCity(response.getCity().getName());
			instance.setZipCode(response.getPostal().getCode());
			instance.setLatitude(response.getLocation().getLatitude());
			instance.setLongitude(response.getLocation().getLongitude());
		
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GeoIp2Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
		return instance;
	}

	protected double haversine(double lat1, double lng1, double lat2, double lng2){
		double earthRadius = 3958.75;
		double dLat = Math.toRadians(lat2-lat1);
		double dLng = Math.toRadians(lng2-lng1);
		double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
		           Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
		           Math.sin(dLng/2) * Math.sin(dLng/2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		return   earthRadius * c;		
	}
	
	protected class InstanceCompator implements Comparator<Instance> {
		public int compare(Instance i1, Instance i2){
			if (i1.distance < i2.distance) return -1;
			else if (i1.distance==i2.distance) return 0;
			else return 1;
		}
	}

}
