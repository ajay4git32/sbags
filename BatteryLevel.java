package com.vibrent.sense.service;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vibrent.sense.domain.ComponentType;
import com.vibrent.sense.domain.FrequencyType;
import com.vibrent.sense.dto.SensorCategoryDTO;
import com.vibrent.sense.dto.SensorConfigurationDTO;

public class BatteryLevel implements IntializeSensorList{


	@Override
	public SensorCategoryDTO generateSensorCategory(List<String>activeList) {
		Map<Integer,String>map = new HashMap<>();
		map.put( 1,"APP_CATEGORY");
		map.put( 2,"APP_USAGE");
		map.put( 3,"BATTERY_LEVEL");
		map.put( 4,"SCREEN");
		map.put( 5,"DEVICE_INFO");
		map.put(6, "CALL_LOGS");

		Map<Integer,String>displayNameMap = new HashMap<>();

        }
}
