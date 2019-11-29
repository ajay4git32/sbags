package com.vibrent.sense.service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibrent.sense.domain.ComponentMain;
import com.vibrent.sense.domain.ComponentType;
import com.vibrent.sense.domain.ConfigurationMain;
import com.vibrent.sense.domain.SensorCategory;
import com.vibrent.sense.domain.SensorConfiguration;
import com.vibrent.sense.domain.SuperConfiguration;
import com.vibrent.sense.dto.ComponentMainDTO;
import com.vibrent.sense.dto.ConfigurationMainDTO;
import com.vibrent.sense.dto.SensorCategoryDTO;
import com.vibrent.sense.dto.SensorConfigurationDTO;
import com.vibrent.sense.dto.SuperConfigurationDTO;
import com.vibrent.sense.mapper.ComponentMainMapper;
import com.vibrent.sense.mapper.ConfigurationMainMapper;
import com.vibrent.sense.mapper.SensorCategoryMapper;
import com.vibrent.sense.mapper.SuperConfigurationMapper;
import com.vibrent.sense.repository.SuperConfigurationRepository;

@Service
public class JsonCreateService {

	@Autowired
	ConfigurationMainService configurationMainService;;
	@Autowired
	SuperConfigurationService superConfigurationService;
	@Autowired
	SuperConfigurationMapper superConfiguratioMapper;
	@Autowired
	ConfigurationMainMapper configurationMainMapper;
	@Autowired
	SensorCategoryMapper sensorCategoryMapper;
	@Autowired
	ComponentMainMapper componentMainMapper;
	@Autowired
	SuperConfigurationRepository supRep;

	public SuperConfigurationDTO createNewJSON(String siteId, String participantId, List<String> activeSensorLst) {
		SuperConfigurationDTO superConfigurationDTO = new SuperConfigurationDTO();


		Set<ComponentMainDTO> components= new LinkedHashSet<ComponentMainDTO>();
		ComponentMainDTO comDtofUT = new ComponentMainDTO.ComponentMainBuilder("fileUpdateTime", 0).setMin(5).setMax(1440).setDefaultValue(125).setSliderIncrement(1).setIcon("fileUpdateTime").
				setHelpText("The next synchronization time is the time that the sensor configuration will periodically check for and apply new updates on the participant's mobile device.").
				setNote("Changing this value to a longer time will delay the time that updates will be applied to the participant's mobile device.").setComponentType(ComponentType.TIME_SLIDER).setTitle("Next Synchronization Time").build();
		components.add(comDtofUT);
		ComponentMainDTO comDtoMUT = new ComponentMainDTO.ComponentMainBuilder("minimumUploadTime", 0).setMin(5).setMax(1440).setDefaultValue(1440).setSliderIncrement(1).setIcon("minimumUploadTime").
				setHelpText("Sensor uploads can be managed independently; however, when used in conjunction with the upload mode, this is the timer at which uploads will occur over cellular regardless of Wi-Fi availability.").
				setNote("Reducing this time may negatively affect cellular data plans.").setComponentType(ComponentType.TIME_SLIDER).setTitle("Sensor Upload Fallback Timer").build();
		components.add(comDtoMUT);
		ComponentMainDTO comDtoDUM =new ComponentMainDTO.ComponentMainBuilder("dataUploadMode", 0).setMin(0).setMax(0).setDefaultValue(1).setSliderIncrement(1).setIcon("dataUploadMode").
				setHelpText("Sensor uploads can be managed independently; however, when used in conjunction with the upload mode, this is the timer at which uploads will occur over cellular regardless of Wi-Fi availability.").
				setNote("Disabling this may negatively affect cellular data plans.").setComponentType(ComponentType.TOGGLE).setTitle("Wi-Fi Prioritization").setAdditionalText("If Wifi mode is selected then data shall be uploaded via Wifi").setToggleHeader("WiFi Setting").build();
		components.add(comDtoDUM);
		ConfigurationMainDTO configurationMainDTO= new ConfigurationMainDTO.ConfigurationMainBuilder("Common").setDisplayDescription("The following are common across all platforms and all sensors of the participant's mobile device.").setIcon("Common").setComponents(components).build();
		if(!activeSensorLst.isEmpty()) {
			
			Set<SensorCategoryDTO> sdto = new LinkedHashSet<SensorCategoryDTO>();
			SensorCategoryDTO appCategorySensor=   new SensorDecorator(new AppCategory()).generateSensorCategory(activeSensorLst);
			sdto.add(appCategorySensor);
			SensorCategoryDTO appUsageSensor=   new SensorDecorator(new AppUsage()).generateSensorCategory(activeSensorLst);
			sdto.add(appUsageSensor);
			SensorCategoryDTO batteryLevelSensor=   new SensorDecorator(new BatteryLevel()).generateSensorCategory(activeSensorLst);
			sdto.add(batteryLevelSensor);
			SensorCategoryDTO screenSensor=   new SensorDecorator(new Screen()).generateSensorCategory(activeSensorLst);
			sdto.add(screenSensor);
			SensorCategoryDTO deviceInfoSensor=   new SensorDecorator(new DeviceInfo()).generateSensorCategory(activeSensorLst);
			sdto.add(deviceInfoSensor);
			SensorCategoryDTO callLogs=   new SensorDecorator(new CallLogs()).generateSensorCategory(activeSensorLst);
			sdto.add(callLogs);
	

			superConfigurationDTO.setSensorList(sdto);
		}

		superConfigurationDTO.setParticipantId(participantId);
		superConfigurationDTO.setSiteId(Integer.parseInt(siteId));
		superConfigurationDTO.setConfiguration(configurationMainDTO);

		return superConfigurationDTO;
	}
	public  SuperConfigurationDTO extracted(String siteId, String participantId) {

		List<SuperConfiguration> superConfigurationLst =superConfigurationService.findBySiteIdAndParticipantId(Integer.parseInt(siteId),participantId);
		if(superConfigurationLst!=null && !superConfigurationLst.isEmpty())
		{
			SuperConfiguration sensorsuperConfiguration=superConfigurationLst.get(0);
			Long configId= sensorsuperConfiguration.getConfigId();
			Optional<SuperConfiguration> sensorsuperConfigurationOtional= superConfigurationService.findById(configId);

			if(sensorsuperConfigurationOtional.isPresent())
			{

				SuperConfigurationDTO superConfigurationDTO =superConfiguratioMapper.toSuperConfigurationDTO(sensorsuperConfigurationOtional.get());
				ConfigurationMainDTO configurationMainDTO = new ConfigurationMainDTO.ConfigurationMainBuilder(sensorsuperConfigurationOtional.get().getConfiguration().getDisplayName()).
						setDisplayDescription(sensorsuperConfigurationOtional.get().getConfiguration().getDisplayDescription()).setIcon(sensorsuperConfigurationOtional.get().getConfiguration().getIcon()).build();
				Set<ComponentMainDTO>componentMainDTOs = new LinkedHashSet<ComponentMainDTO>();
				for(ComponentMain comMain:sensorsuperConfigurationOtional.get().getConfiguration().getComponents())
				{
					ComponentMainDTO cmd= new ComponentMainDTO.ComponentMainBuilder(comMain.getComponentName(), comMain.getData()).setMin(comMain.getMin())
							.setMax(comMain.getMax()).setDefaultValue(comMain.getDefaultValue()).setNote(comMain.getNote()).setSliderIncrement(comMain.getSliderIncrement()).setHelpText(comMain.getHelpText()).setIcon(comMain.getIcon()).setComponentType(comMain.getComponentType()).
							setAdditionalText(comMain.getAdditionalText()).setFrequencyType(comMain.getFrequencyType()).build();

					componentMainDTOs.add(cmd);

				}

				Set<ComponentMainDTO> cmpDestination = new LinkedHashSet<>();
				ComponentMainDTO cmdOne =componentMainDTOs.stream().filter(s->s.getComponentName().equalsIgnoreCase("fileUpdateTime")).findFirst().get();
				ComponentMainDTO cmdTwo =componentMainDTOs.stream().filter(s->s.getComponentName().equalsIgnoreCase("minimumUploadTime")).findFirst().get();
				ComponentMainDTO cmdThree =componentMainDTOs.stream().filter(s->s.getComponentName().equalsIgnoreCase("dataUploadMode")).findFirst().get();

				cmpDestination.add(cmdOne);
				cmpDestination.add(cmdTwo);
				cmpDestination.add(cmdThree);

				ConfigurationMainDTO  configurationMainRes=new ConfigurationMainDTO.ConfigurationMainBuilder(configurationMainDTO.getDisplayName()).setDisplayDescription(configurationMainDTO.getDisplayDescription())
						.setIcon(configurationMainDTO.getIcon()).setComponents(cmpDestination).build();

				Set<SensorCategory> sensorCategories =sensorsuperConfigurationOtional.get().getSensorMain();
				Set<SensorCategoryDTO> sensorCategoryDTOs = new LinkedHashSet<SensorCategoryDTO>();
				SensorConfigurationDTO mos=	null;

				for(SensorCategory sensorCategory:sensorCategories)
				{

					Set<SensorConfiguration> sensorsuperConfiguration1 =sensorCategory.getConfiguration();
					Set<SensorConfigurationDTO> sensorConfigurations = new LinkedHashSet<SensorConfigurationDTO>();

					for(SensorConfiguration superConfiguration:sensorsuperConfiguration1)
					{
						mos =	new SensorConfigurationDTO.SensorConfigurationBuilder(superConfiguration.getComponentName(), superConfiguration.getData()).
								setComponentType(superConfiguration.getComponentType()).setDefaultValue(superConfiguration.getDefaultValue()).setIcon(superConfiguration.getIcon())
								.setMax(superConfiguration.getMax()).setMin(superConfiguration.getMin()).setHelpText(superConfiguration.getHelpText()).setFrequencyType(superConfiguration.getFrequencyType()).setTitle(superConfiguration.getTitle()).setSliderIncrement(superConfiguration.getSliderIncrement()).build();
						sensorConfigurations.add(mos);

					}

					Set<SensorConfigurationDTO> scConfiguration = new LinkedHashSet<>();
					SensorConfigurationDTO scOne = sensorConfigurations.stream().filter(s->s.getComponentName().equalsIgnoreCase("samplingTime")).findFirst().get();
					SensorConfigurationDTO scTwo = sensorConfigurations.stream().filter(s->s.getComponentName().equalsIgnoreCase("dutyCycle")).findFirst().get();
					SensorConfigurationDTO scThree = sensorConfigurations.stream().filter(s->s.getComponentName().equalsIgnoreCase("samplingDuration")).findFirst().get();
					SensorConfigurationDTO scFour=sensorConfigurations.stream().filter(s->s.getComponentName().equalsIgnoreCase("uploadTime")).findFirst().get();

					scConfiguration.add(scOne);
					scConfiguration.add(scTwo);
					scConfiguration.add(scThree);
					scConfiguration.add(scFour);




					SensorCategoryDTO newSensorCategoryDTO =new SensorCategoryDTO.SensorCategoryBuilder(sensorCategory.getCategoryName(), sensorCategory.getDisplayName()).setDisplayDescription(sensorCategory.getDisplayDescription())
							.setIcon(sensorCategory.getIcon()).setConfiguration(scConfiguration).build();
					sensorCategoryDTOs.add(newSensorCategoryDTO);
				}

				superConfigurationDTO.setParticipantId(participantId);
				superConfigurationDTO.setSiteId(Integer.parseInt(siteId));
				superConfigurationDTO.setConfiguration(configurationMainRes);
				superConfigurationDTO.setSensorList(sensorCategoryDTOs);
				return superConfigurationDTO;
			}
		}
		return new SuperConfigurationDTO();
	}
	public  SuperConfigurationDTO saveConfiguration(SuperConfigurationDTO superConfigurationDTO) {
		boolean isDeleted =false;

		List<SuperConfiguration> superConfigurationLst =superConfigurationService.findBySiteIdAndParticipantId(superConfigurationDTO.getSiteId(),superConfigurationDTO.getParticipantId());
		List<SuperConfiguration> superConfigurationWithSite =superConfigurationService.findByParticipantId(superConfigurationDTO.getParticipantId());
		Optional<SuperConfiguration> filteredsuperConfiguration =superConfigurationWithSite.stream().filter(s->s.getParticipantId().trim().equalsIgnoreCase(superConfigurationDTO.getParticipantId().trim())).findFirst();
		if(filteredsuperConfiguration!=null && superConfigurationWithSite!=null && !superConfigurationWithSite.isEmpty())
		{
			if(superConfigurationWithSite.get(0).getParticipantId().equalsIgnoreCase(superConfigurationDTO.getParticipantId()))
			{
				long configId = filteredsuperConfiguration.get().getConfigId();
				superConfigurationService.deleteById(configId);
				isDeleted=true;
			}
		}


		if(superConfigurationLst!=null && !superConfigurationLst.isEmpty() && !isDeleted)
		{
			isDeleted=false;
			SuperConfiguration sensorsuperConfiguration=superConfigurationLst.get(0);
			Long configId= sensorsuperConfiguration.getConfigId();
			Optional<SuperConfiguration> sensorsuperConfigurationOtional= superConfigurationService.findById(configId);

			if(sensorsuperConfigurationOtional!=null && sensorsuperConfigurationOtional.get()!=null)
			{
				ConfigurationMainDTO configurationMaindto =superConfigurationDTO.getConfiguration();

				Set<ComponentMainDTO> componentMainDTOs=configurationMaindto.getComponents();


				Set<ComponentMain> cMain = new LinkedHashSet<ComponentMain>();
				for(ComponentMainDTO componentMainDTO:componentMainDTOs)
				{
					ComponentMain cm=componentMainMapper.toComponentMain(componentMainDTO);
					cMain.add(cm);

				}

				ConfigurationMain cmr=configurationMainMapper.toConfigurationMain(new ConfigurationMainDTO.ConfigurationMainBuilder(configurationMaindto.getDisplayName())
						.setDisplayDescription(configurationMaindto.getDisplayDescription()).build());
				cmr.setComponents(cMain);


				Set<SensorCategoryDTO> scCategoryDTOs =superConfigurationDTO.getSensorList();




				Set<SensorCategory> scCategorys = new LinkedHashSet<SensorCategory>();
				for(SensorCategoryDTO sensorCategoryDTO:scCategoryDTOs)
				{
					SensorCategory sc=sensorCategoryMapper.toSensorCategory(sensorCategoryDTO);
					scCategorys.add(sc);

				}
				SuperConfiguration superConfiguration = sensorsuperConfigurationOtional.get();
				superConfiguration.setParticipantId(superConfigurationDTO.getParticipantId());
				superConfiguration.setSiteId(superConfigurationDTO.getSiteId());
				configurationMainService.save(cmr);
				superConfiguration.setConfiguration(cmr);
				superConfiguration.setSensorMain(scCategorys);

				SuperConfiguration sconConfiguration=superConfigurationService.save(superConfiguration);
				return superConfiguratioMapper.toSuperConfigurationDTO(sconConfiguration);
			}
		}
		else
		{
			ConfigurationMainDTO configurationMaindto =superConfigurationDTO.getConfiguration();

			Set<ComponentMainDTO> componentMainDTOs=configurationMaindto.getComponents();


			Set<ComponentMain> cMain = new LinkedHashSet<ComponentMain>();
			for(ComponentMainDTO componentMainDTO:componentMainDTOs)
			{
				ComponentMain cm=componentMainMapper.toComponentMain(componentMainDTO);
				cMain.add(cm);

			}

			ConfigurationMain cmr=configurationMainMapper.toConfigurationMain(configurationMaindto);
			cmr.setComponents(cMain);


			Set<SensorCategoryDTO> scCategoryDTOs =superConfigurationDTO.getSensorList();




			Set<SensorCategory> scCategorys = new LinkedHashSet<SensorCategory>();
			for(SensorCategoryDTO sensorCategoryDTO:scCategoryDTOs)
			{
				SensorCategory sc=sensorCategoryMapper.toSensorCategory(sensorCategoryDTO);
				scCategorys.add(sc);

			}
			SuperConfiguration superConfiguration = new SuperConfiguration();
			superConfiguration.setParticipantId(superConfigurationDTO.getParticipantId());
			superConfiguration.setSiteId(superConfigurationDTO.getSiteId());
			configurationMainService.save(cmr);
			superConfiguration.setConfiguration(cmr);
			superConfiguration.setSensorMain(scCategorys);

			SuperConfiguration sconConfiguration=superConfigurationService.save(superConfiguration);
			return superConfiguratioMapper.toSuperConfigurationDTO(sconConfiguration);
		}
		return superConfigurationDTO;
	}

	public  SuperConfigurationDTO saveConfiguration(int siteId,SuperConfigurationDTO superConfigurationDTO) {

		List<SuperConfiguration> superConfigurationLst =superConfigurationService.findBySiteIdAndParticipantId(superConfigurationDTO.getSiteId(),superConfigurationDTO.getParticipantId());
		List<SuperConfiguration> superConfigurationWithSite =superConfigurationService.findByParticipantId(superConfigurationDTO.getParticipantId());
		Optional<SuperConfiguration> filteredsuperConfiguration =superConfigurationWithSite.stream().filter(s->s.getParticipantId().trim().equalsIgnoreCase(superConfigurationDTO.getParticipantId().trim())).findFirst();
		if(filteredsuperConfiguration!=null && superConfigurationWithSite!=null && !superConfigurationWithSite.isEmpty())
		{
			if(superConfigurationWithSite.get(0).getParticipantId().equalsIgnoreCase(superConfigurationDTO.getParticipantId()))
			{
				long configId = filteredsuperConfiguration.get().getConfigId();

			}
		}


		if(!superConfigurationLst.isEmpty())
		{
			SuperConfiguration sensorsuperConfiguration=superConfigurationLst.get(0);
			Long configId= sensorsuperConfiguration.getConfigId();
			Optional<SuperConfiguration> sensorsuperConfigurationOtional= superConfigurationService.findById(configId);

			if(sensorsuperConfigurationOtional.get()!=null)
			{
				ConfigurationMainDTO configurationMaindto =superConfigurationDTO.getConfiguration();

				Set<ComponentMainDTO> componentMainDTOs=configurationMaindto.getComponents();


				Set<ComponentMain> cMain = new LinkedHashSet<ComponentMain>();
				for(ComponentMainDTO componentMainDTO:componentMainDTOs)
				{
					ComponentMain cm=componentMainMapper.toComponentMain(componentMainDTO);
					cMain.add(cm);

				}

				ConfigurationMain cmr=configurationMainMapper.toConfigurationMain(new ConfigurationMainDTO.ConfigurationMainBuilder(configurationMaindto.getDisplayName())
						.setDisplayDescription(configurationMaindto.getDisplayDescription()).build());
				cmr.setComponents(cMain);


				Set<SensorCategoryDTO> scCategoryDTOs =superConfigurationDTO.getSensorList();




				Set<SensorCategory> scCategorys = new LinkedHashSet<SensorCategory>();
				for(SensorCategoryDTO sensorCategoryDTO:scCategoryDTOs)
				{
					SensorCategory sc=sensorCategoryMapper.toSensorCategory(sensorCategoryDTO);
					scCategorys.add(sc);

				}
				SuperConfiguration superConfiguration = sensorsuperConfigurationOtional.get();
				superConfiguration.setParticipantId(superConfigurationDTO.getParticipantId());
				superConfiguration.setSiteId(superConfigurationDTO.getSiteId());
				configurationMainService.save(cmr);
				superConfiguration.setConfiguration(cmr);
				superConfiguration.setSensorMain(scCategorys);

				SuperConfiguration sconConfiguration=superConfigurationService.save(superConfiguration);
				return superConfiguratioMapper.toSuperConfigurationDTO(sconConfiguration);
			}
		}
		else
		{
			ConfigurationMainDTO configurationMaindto =superConfigurationDTO.getConfiguration();

			Set<ComponentMainDTO> componentMainDTOs=configurationMaindto.getComponents();


			Set<ComponentMain> cMain = new LinkedHashSet<ComponentMain>();
			for(ComponentMainDTO componentMainDTO:componentMainDTOs)
			{
				ComponentMain cm=componentMainMapper.toComponentMain(componentMainDTO);
				cMain.add(cm);

			}

			ConfigurationMain cmr=configurationMainMapper.toConfigurationMain(configurationMaindto);
			cmr.setComponents(cMain);


			Set<SensorCategoryDTO> scCategoryDTOs =superConfigurationDTO.getSensorList();




			Set<SensorCategory> scCategorys = new LinkedHashSet<SensorCategory>();
			for(SensorCategoryDTO sensorCategoryDTO:scCategoryDTOs)
			{
				SensorCategory sc=sensorCategoryMapper.toSensorCategory(sensorCategoryDTO);
				scCategorys.add(sc);

			}
			SuperConfiguration superConfiguration = new SuperConfiguration();
			superConfiguration.setParticipantId(superConfigurationDTO.getParticipantId());
			superConfiguration.setSiteId(superConfigurationDTO.getSiteId());
			configurationMainService.save(cmr);
			superConfiguration.setConfiguration(cmr);
			superConfiguration.setSensorMain(scCategorys);

			SuperConfiguration sconConfiguration=superConfigurationService.save(superConfiguration);
			return superConfiguratioMapper.toSuperConfigurationDTO(sconConfiguration);
		}
		return superConfigurationDTO;
	}


}
