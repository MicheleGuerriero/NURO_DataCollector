package dataCollector;

import java.util.HashSet;
import java.util.Set;

import org.codehaus.jackson.JsonNode;

import exception.MetricNotAvailableException;

public class MetricManager {

	private static final int TEN_SECONDS = 10;
	private static final int MINUTE = 60;
	
	public Set<String> getApplicationMetrics() {

		Set<String> metrics = new HashSet<String>();
		metrics.add("NUROServerLastTenSecondsAverageRunTime");
		metrics.add("NUROServerLastMinuteAverageRunTime");
		metrics.add("NUROServerLastTenSecondsPlayerCount");
		metrics.add("NUROServerLastMinutePlayerCount");
		metrics.add("NUROServerLastTenSecondsRequestCount");
		metrics.add("NUROServerLastMinuteRequestCount");
		metrics.add("NUROServerLastTenSecondsAverageThroughput");
		metrics.add("NUROServerLastMinuteAverageThroughput");

		return metrics;
	}

	public Object getMetricValue(String metric, JsonNode actualObj)
			throws MetricNotAvailableException {

		String value;
		System.out.println("start looking for the value of the metric: "
				+ metric);

		switch (metric) {
		case "NUROServerLastTenSecondsAverageRunTime":
			value = actualObj.get("request_analytics").get("10seconds")
					.get("avg_run_time").toString();
			return Double.parseDouble(value.substring(1, value.length() - 1));
		case "NUROServerLastMinuteAverageRunTime":
			value = actualObj.get("request_analytics").get("minute")
					.get("avg_run_time").toString();
			return Double.parseDouble(value.substring(1, value.length() - 1));
		case "NUROServerLastTenSecondsPlayerCount":
			value = actualObj.get("request_analytics").get("10seconds")
					.get("player_count").toString();
			return Integer.parseInt(value.substring(1, value.length() - 1));
		case "NUROServerLastMinutePlayerCount":
			value = actualObj.get("request_analytics").get("minute")
					.get("player_count").toString();
			return Integer.parseInt(value.substring(1, value.length() - 1));
		case "NUROServerLastTenSecondsRequestCount":
			value = actualObj.get("request_analytics").get("10seconds")
					.get("request_count").toString();
			return Integer.parseInt(value.substring(1, value.length() - 1));
		case "NUROServerLastMinuteRequestCount":
			value = actualObj.get("request_analytics").get("minute")
					.get("request_count").toString();
			return Integer.parseInt(value.substring(1, value.length() - 1));
		case "NUROServerLastTenSecondsAverageThroughput":
			value = actualObj.get("request_analytics").get("10seconds")
					.get("request_count").toString();
			return Double.parseDouble(value.substring(1, value.length() - 1))
					/ TEN_SECONDS;
		case "NUROServerLastMinuteAverageThroughput":
			value = actualObj.get("request_analytics").get("minute")
					.get("request_count").toString();
			return Double.parseDouble(value.substring(1, value.length() - 1))
					/ MINUTE;
		default:
			throw new MetricNotAvailableException(
					"The specified metric is not available from NURO sensors");

		}

	}
}
