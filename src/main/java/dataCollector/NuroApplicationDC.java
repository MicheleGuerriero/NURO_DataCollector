package dataCollector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import config.EnvironmentReader;
import exception.ConfigurationException;
import exception.MetricNotAvailableException;
import it.polimi.tower4clouds.data_collector_library.DCAgent;
import it.polimi.tower4clouds.manager.api.ManagerAPI;
import it.polimi.tower4clouds.model.data_collectors.DCDescriptor;
import it.polimi.tower4clouds.model.ontology.CloudProvider;
import it.polimi.tower4clouds.model.ontology.ExternalComponent;
import it.polimi.tower4clouds.model.ontology.InternalComponent;
import it.polimi.tower4clouds.model.ontology.Location;
import it.polimi.tower4clouds.model.ontology.PaaSService;
import it.polimi.tower4clouds.model.ontology.Resource;
import it.polimi.tower4clouds.model.ontology.VM;

public class NuroApplicationDC implements Observer {



	public void startMonitor(EnvironmentReader config) throws ConfigurationException {

		MetricManager manager=new MetricManager();
		
		DCAgent dcAgent = new DCAgent(new ManagerAPI(config.getMmIP(),
				config.getMmPort()));
		
		DCDescriptor dcDescriptor = new DCDescriptor();
		
		if (config.getInternalComponentId() != null) {
			dcDescriptor.addResource(buildInternalComponent(config));
			dcDescriptor.addMonitoredResource(manager.getApplicationMetrics(),
					buildInternalComponent(config));
		}
		if (config.getVmId() != null) {
			dcDescriptor.addResource(buildExternalComponent(config));
		}
		dcDescriptor.addResources(buildRelatedResources(config));
		dcDescriptor.setConfigSyncPeriod(config.getDcSyncPeriod());
		dcDescriptor.setKeepAlive(config.getResourcesKeepAlivePeriod());
		dcAgent.setDCDescriptor(dcDescriptor);
		dcAgent.addObserver(this);
		dcAgent.start();

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		HttpClient httpClient = HttpClientBuilder.create().build();
		String username;
		String password;
		String sensorUrl;

		while (true) {
			try {

				for (String metric : manager.getApplicationMetrics()) {
					if (dcAgent.shouldMonitor(new InternalComponent(
							EnvironmentReader.getInstance()
									.getInternalComponentType(), EnvironmentReader
									.getInstance().getInternalComponentId()),
							metric)) {

						username = dcAgent.getParameters(metric)
								.get("userName");
						password = dcAgent.getParameters(metric)
								.get("password");
						sensorUrl =dcAgent.getParameters(metric).get("sensorUrl");
						String auth = username + ":" + password;
						String encodedAuth = Base64.encodeBase64String(auth
								.getBytes());
						HttpGet httpget = new HttpGet(sensorUrl);
						httpget.addHeader("Authorization", "Basic "
								+ encodedAuth);
						HttpResponse response = httpClient.execute(httpget);
						HttpEntity responseEntity = response.getEntity();
						InputStream stream = responseEntity.getContent();
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(stream));
						StringBuilder out = new StringBuilder();
						String line;
						while ((line = reader.readLine()) != null) {
							out.append(line);
						}
						reader.close();

						ObjectMapper mapper = new ObjectMapper();
						JsonNode actualObj = mapper.readTree(out.toString());

						System.out.println("sending value "
								+ manager.getMetricValue(metric, actualObj)
								+ " for metric " + metric);
						dcAgent.send(new InternalComponent(EnvironmentReader
								.getInstance().getInternalComponentType(),
								EnvironmentReader.getInstance()
										.getInternalComponentId()), metric,
										manager.getMetricValue(metric, actualObj));
						Thread.sleep(Integer.parseInt(dcAgent.getParameters(
								metric).get("samplingTime")) * 1000);
					}

				}

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MetricNotAvailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private static Set<Resource> buildRelatedResources(EnvironmentReader config) {
		Set<Resource> relatedResources = new HashSet<Resource>();
		if (config.getCloudProviderId() != null) {
			relatedResources.add(new CloudProvider(config
					.getCloudProviderType(), config.getCloudProviderId()));
		}
		if (config.getLocationId() != null) {
			relatedResources.add(new Location(config.getLocationtype(), config
					.getLocationId()));
		}
		return relatedResources;
	}

	private static Resource buildExternalComponent(EnvironmentReader config)
			throws ConfigurationException {
		ExternalComponent externalComponent;
		if (config.getVmId() != null) {
			externalComponent = new VM();
			externalComponent.setId(config.getVmId());
			externalComponent.setType(config.getVmType());
		} else if (config.getPaasServiceId() != null) {
			externalComponent = new PaaSService();
			externalComponent.setId(config.getPaasServiceId());
			externalComponent.setType(config.getPaasServiceType());
		} else {
			throw new ConfigurationException(
					"Neither VM nor PaaS service were specified");
		}
		if (config.getCloudProviderId() != null)
			externalComponent.setCloudProvider(config.getCloudProviderId());
		if (config.getLocationId() != null)
			externalComponent.setLocation(config.getLocationId());
		return externalComponent;
	}

	private static Resource buildInternalComponent(EnvironmentReader config) {
		InternalComponent internalComponent = new InternalComponent(
				config.getInternalComponentType(),
				config.getInternalComponentId());
		if (config.getVmId() != null)
			internalComponent.addRequiredComponent(config.getVmId());
		return internalComponent;
	}

	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		// not user

	}

}
