package com.vibrent.sense.service;

import java.util.List;

import com.vibrent.sense.dto.SensorCategoryDTO;

public interface IntializeSensorList {
	public  SensorCategoryDTO generateSensorCategory(List<String>activeSensor);

}
