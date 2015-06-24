package Config;

import Exception.ConfigurationException;


public class Configuration {
	

		private static Configuration _instance = null;
		private String mmIP;
		private int mmPort;
		private int dcSyncPeriod = 30;
		private int resourcesKeepAlivePeriod = 60;
		private String cloudProviderId;
		private String cloudProviderType;
		private String paasServiceId;
		private String paasServiceType;
		private String vmId;
		private String vmType;
		private String locationId;
		private String locationtype;
		private String internalComponentId;
		private String internalComponentType;

		public static Configuration getInstance() throws ConfigurationException {
			if (_instance == null)
				_instance = new Configuration();
			return _instance;
		}

		private Configuration() throws ConfigurationException {
			try {
				mmIP = getMandatoryEnvVar(Environment.MODACLOUDS_TOWER4CLOUDS_MANAGER_IP);
				String mmPortString = getMandatoryEnvVar(Environment.MODACLOUDS_TOWER4CLOUDS_MANAGER_PORT);
				mmPort = Integer.parseInt(mmPortString);

				String dcSyncPeriodString = getOptionalEnvVar(
						Environment.MODACLOUDS_TOWER4CLOUDS_DC_SYNC_PERIOD,
						Integer.toString(dcSyncPeriod));
				dcSyncPeriod = Integer.parseInt(dcSyncPeriodString);
				
				String resourcesKeepAlivePeriodString = getOptionalEnvVar(
						Environment.MODACLOUDS_TOWER4CLOUDS_RESOURCES_KEEP_ALIVE_PERIOD,
						Integer.toString(resourcesKeepAlivePeriod));
				resourcesKeepAlivePeriod = Integer.parseInt(resourcesKeepAlivePeriodString);
				
				cloudProviderId = getOptionalEnvVar(
						Environment.MODACLOUDS_TOWER4CLOUDS_CLOUD_PROVIDER_ID);
				cloudProviderType = getOptionalEnvVar(Environment.MODACLOUDS_TOWER4CLOUDS_CLOUD_PROVIDER_TYPE);
				paasServiceId = getOptionalEnvVar(Environment.MODACLOUDS_TOWER4CLOUDS_PAAS_SERVICE_ID);
				paasServiceType = getOptionalEnvVar(Environment.MODACLOUDS_TOWER4CLOUDS_PAAS_SERVICE_TYPE);
				vmId = getOptionalEnvVar(Environment.MODACLOUDS_TOWER4CLOUDS_VM_ID);
				vmType = getOptionalEnvVar(Environment.MODACLOUDS_TOWER4CLOUDS_VM_TYPE);
				locationId = getOptionalEnvVar(Environment.MODACLOUDS_TOWER4CLOUDS_LOCATION_ID);
				locationtype = getOptionalEnvVar(Environment.MODACLOUDS_TOWER4CLOUDS_LOCATION_TYPE);
				internalComponentId = getOptionalEnvVar(Environment.MODACLOUDS_TOWER4CLOUDS_INTERNAL_COMPONENT_ID);
				internalComponentType = getOptionalEnvVar(Environment.MODACLOUDS_TOWER4CLOUDS_INTERNAL_COMPONENT_TYPE);
				

			} catch (Exception e) {
				throw new ConfigurationException(
						"Could not configure properly the data collector");
			}
		}

		private String getMandatoryEnvVar(String varName)
				throws ConfigurationException {
			String var = System.getenv(varName);
			if (var == null)
				throw new ConfigurationException(varName
						+ " variable was not defined");
			return var;
		}

		private String getOptionalEnvVar(String varName, String defaultValue) {
			String var = getOptionalEnvVar(varName);
			if (var == null) {
				var = defaultValue;
			}
			return var;
		}

		private String getOptionalEnvVar(String varName) {
			return System.getenv(varName);
		}
		
		public String getCloudProviderId() {
			return cloudProviderId;
		}
		
		public String getCloudProviderType() {
			return cloudProviderType;
		}
		
		public String getInternalComponentId() {
			return internalComponentId;
		}
		
		public int getDcSyncPeriod() {
			return dcSyncPeriod;
		}
		
		public String getInternalComponentType() {
			return internalComponentType;
		}
		
		public String getLocationId() {
			return locationId;
		}
		
		public String getLocationtype() {
			return locationtype;
		}
		
		public String getMmIP() {
			return mmIP;
		}
		
		public int getMmPort() {
			return mmPort;
		}
		
		public String getPaasServiceId() {
			return paasServiceId;
		}
		
		public String getPaasServiceType() {
			return paasServiceType;
		}
		
		public int getResourcesKeepAlivePeriod() {
			return resourcesKeepAlivePeriod;
		}
		
		public String getVmId() {
			return vmId;
		}
		
		public String getVmType() {
			return vmType;
		}

}
