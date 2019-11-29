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

public class CallLogs implements IntializeSensorList{

	@Override
	public SensorCategoryDTO generateSensorCategory(List<String>activeList) {
		Map<Integer,String>map = new HashMap<>();
	
		Map<Integer,String>displayNameMap = new HashMap<>();
	
		Map<Integer,String>displayDescriptionMap = new HashMap<>();
		displayDescriptionMap.put( 1,"This sensor describes the ability to collect information related to the application bundle ID specific to the application category as reported by the Google Play Store.  At this time, these selection are only available for Android and will not have any affect on iOS user devices.");
		displayDescriptionMap.put( 2, "This sensor describes the ability to collect information related to the application bundle ID related to the foreground usage by the participant.  It can be used in conjunction with the app category classifiers to determine how long a participant is using a generalized social media application versus a specific application.  At this time, these selection are only available for Android and will not have any affect on iOS user devices.");
		displayDescriptionMap.put( 3,"This sensor describes the current battery state of the participant's mobile device.");
		displayDescriptionMap.put( 4, "This sensor describes the ability to collect information related to the screen on and off time of the participant's mobile device");
		displayDescriptionMap.put( 5,"This sensor describes the device information of the participant's mobile device.");
		displayDescriptionMap.put(6,"This sensor describes the ability to collect information related to the phone call and the time spent in by the participant in each phone call.");


		
		Set<SensorConfigurationDTO> scdto = new LinkedHashSet<SensorConfigurationDTO>();
		if(activeList.contains("6"))
		{
		SensorConfigurationDTO scdto1 = new SensorConfigurationDTO.SensorConfigurationBuilder("samplingTime", 0).setMin(0).setMax(1440).setDefaultValue(1).setSliderIncrement(1).setIcon("samplingTime").
				setHelpText("This describes the time that sensor is polled for new information. If this is set to 0, then the result will be event-based.  In some cases, only Android or iOS can be configured.  See the note below the selection for specific details related to which platform this is applicable.").
				setNote("Sampling time is event based for both Android and Ios platform.").setComponentType(ComponentType.TIME_SLIDER).setFrequencyType(FrequencyType.EVENT).setTitle("Sample Time").setSliderIncrement(1).build();

		SensorConfigurationDTO scdto2 = new SensorConfigurationDTO.SensorConfigurationBuilder("dutyCycle", 0).setMin(0).setMax(100).setDefaultValue(0).setSliderIncrement(1).setIcon("dutyCycle").
				setHelpText("This describes the percentage of time that sensor will be polled for before it is skipped for a period of time.  The total time the duty cycle is calculated against is specified by the duty cycle duration.  This field is only applicable to sensors and platforms that allow for polling. If Sampling Time is set to 0 or event-based, then this selection cannot be configured. In some cases, only Android or iOS can be configured.  See the note below the selection for specific details related to which platform this is applicable.").
				setNote("It is event based for both Ios and Android").setComponentType(ComponentType.PERCENTAGE_SLIDER).setFrequencyType(FrequencyType.EVENT).setTitle("Duty Cycle  - %").setSliderIncrement(1).build();

		SensorConfigurationDTO scdto3 = new SensorConfigurationDTO.SensorConfigurationBuilder("samplingDuration", 0).setMin(0).setMax(1440).setDefaultValue(125).setSliderIncrement(1).setIcon("samplingDuration").
				setHelpText("This describes the overall time that duty cycle for a sensor is calculated against.  This field is only applicable to sensors and platforms that allow for polling. If Sampling Time is set to 0 or event-based, then this selection cannot be configured. In some cases, only Android or iOS can be configured.  See the note below the selection for specific details related to which platform this is applicable.").
				setNote("It is event based for both Ios and Android").setComponentType(ComponentType.TIME_SLIDER).setFrequencyType(FrequencyType.EVENT).setTitle("Duty Cycle - Duration").setSliderIncrement(1).build();


		SensorConfigurationDTO scdto4 = new SensorConfigurationDTO.SensorConfigurationBuilder("uploadTime", 0).setMin(1).setMax(1440).setDefaultValue(120).setSliderIncrement(1).setIcon("UploadTime").
				setHelpText("This describes the time which the sensor data is uploaded to the server.  This is based on the Wi-Fi Prioritization selection specified in the common configuration section earlier. If Wi-Fi is required, then the upload time is observed only when Wi-Fi is connected to; otherwise the fallback timer will be used.").
				setNote(null).setComponentType(ComponentType.TIME_SLIDER).setTitle("Upload Time").setSliderIncrement(1).build();

		scdto.add(scdto1);
		scdto.add(scdto2);
		scdto.add(scdto3);
		scdto.add(scdto4);
		}
		else
		{
			SensorConfigurationDTO scdto1 = new SensorConfigurationDTO.SensorConfigurationBuilder("samplingTime", 0).setMin(0).setMax(1440).setDefaultValue(1).setSliderIncrement(1).setIcon("samplingTime").
					setHelpText("This describes the time that sensor is polled for new information. If this is set to 0, then the result will be event-based.  In some cases, only Android or iOS can be configured.  See the note below the selection for specific details related to which platform this is applicable.").
					setNote("Sampling time is event based for both Android and Ios platform.").setComponentType(ComponentType.TIME_SLIDER).setFrequencyType(FrequencyType.EVENT).setTitle("Sample Time").setSliderIncrement(1).build();

			SensorConfigurationDTO scdto2 = new SensorConfigurationDTO.SensorConfigurationBuilder("dutyCycle", 0).setMin(0).setMax(100).setDefaultValue(0).setSliderIncrement(1).setIcon("dutyCycle").
					setHelpText("This describes the percentage of time that sensor will be polled for before it is skipped for a period of time.  The total time the duty cycle is calculated against is specified by the duty cycle duration.  This field is only applicable to sensors and platforms that allow for polling. If Sampling Time is set to 0 or event-based, then this selection cannot be configured. In some cases, only Android or iOS can be configured.  See the note below the selection for specific details related to which platform this is applicable.").
					setNote("It is event based for both Ios and Android").setComponentType(ComponentType.PERCENTAGE_SLIDER).setFrequencyType(FrequencyType.EVENT).setTitle("Duty Cycle  - %").setSliderIncrement(1).build();

			SensorConfigurationDTO scdto3 = new SensorConfigurationDTO.SensorConfigurationBuilder("samplingDuration", 0).setMin(0).setMax(1440).setDefaultValue(125).setSliderIncrement(1).setIcon("samplingDuration").
					setHelpText("This describes the overall time that duty cycle for a sensor is calculated against.  This field is only applicable to sensors and platforms that allow for polling. If Sampling Time is set to 0 or event-based, then this selection cannot be configured. In some cases, only Android or iOS can be configured.  See the note below the selection for specific details related to which platform this is applicable.").
					setNote("It is event based for both Ios and Android").setComponentType(ComponentType.TIME_SLIDER).setFrequencyType(FrequencyType.EVENT).setTitle("Duty Cycle - Duration").setSliderIncrement(1).build();


			SensorConfigurationDTO scdto4 = new SensorConfigurationDTO.SensorConfigurationBuilder("uploadTime", 0).setMin(1).setMax(1440).setDefaultValue(120).setSliderIncrement(1).setIcon("UploadTime").
					setHelpText("This describes the time which the sensor data is uploaded to the server.  This is based on the Wi-Fi Prioritization selection specified in the common configuration section earlier. If Wi-Fi is required, then the upload time is observed only when Wi-Fi is connected to; otherwise the fallback timer will be used.").
					setNote(null).setComponentType(ComponentType.TIME_SLIDER).setTitle("Upload Time").setSliderIncrement(1).build();

			scdto.add(scdto1);
			scdto.add(scdto2);
			scdto.add(scdto3);
			scdto.add(scdto4);
		}
            SensorCategoryDTO sCategoryDTO = new SensorCategoryDTO.SensorCategoryBuilder(map.get(6), map.get(6)).setSensorId(Integer.parseInt("6")).setDisplayDescription(displayDescriptionMap.get(6)).setIcon(map.get(6)).setDisplayName(displayNameMap.get(6)).setConfiguration(scdto).build();
			return sCategoryDTO;

        }

}
