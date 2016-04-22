package Model;

import Utils.BOMUtils;
import org.joda.time.LocalDateTime;

/**
 * Data structure for storing recent BOM station readings.
 */
public final class LatestReading
{
	private final LocalDateTime localDateTime;
	private final LocalDateTime utcDateTime;

	private final Double latitude;
	private final Double longitude;

	private final Double airTemp;
	private final Double apparentTemp;
	private final Double dewPt;
	private final Integer relativeHumidity;
	private final Double deltaTemp;

	private final String cloud;
	private final String cloudType;

	private final String windDir;
	private final Integer windSpdKmH;
	private final Integer windSpdKts;
	private final Integer windGustKmH;
	private final Integer windGustKts;

	private final Double pressureQNH;
	private final Double pressureMSL;

	private final String rainTrace;

	public LatestReading(String localDateTime, String utcDateTime, Double latitude, Double longitude, Double airTemp,
						 Double apparentTemp, Double dewPt, Integer relativeHumidity, Double deltaTemp, String cloud,
						 String cloudType, String windDir, Integer windSpdKmH, Integer windSpdKts, Integer windGustKmH,
						 Integer windGustKts, Double pressureQNH, Double pressureMSL, String rainTrace) {
		
		this.localDateTime = BOMUtils.toDateTime(localDateTime);
		this.utcDateTime = BOMUtils.toDateTime(utcDateTime);

		this.latitude = latitude;
		this.longitude = longitude;

		this.airTemp = airTemp;
		this.apparentTemp = apparentTemp;
		this.dewPt = dewPt;
		this.relativeHumidity = relativeHumidity;
		this.deltaTemp = deltaTemp;

		this.cloud = cloud;
		this.cloudType = cloudType;

		this.windDir = windDir;
		this.windSpdKmH = windSpdKmH;
		this.windSpdKts = windSpdKts;
		this.windGustKmH = windGustKmH;
		this.windGustKts = windGustKts;

		this.pressureQNH = pressureQNH;
		this.pressureMSL = pressureMSL;

		this.rainTrace = rainTrace;
	}

	public final LocalDateTime getLocalDateTime() {
		return localDateTime;
	}
	public final LocalDateTime getUTCDateTime() {
		return utcDateTime;
	}
	public final Double getLatitude() {
		return latitude;
	}
	public final Double getLongitude() {
		return longitude;
	}
	public final Double getAirTemp() {
		return airTemp;
	}
	public final Double getApparentTemp() {
		return apparentTemp;
	}
	public final Double getDewPt() {
		return dewPt;
	}
	public final Integer getRelativeHumidity() {
		return relativeHumidity;
	}
	public final Double getDeltaTemp() {
		return deltaTemp;
	}
	public final String getCloud() {
		return cloud;
	}
	public final String getCloudType() {
		return cloudType;
	}
	public final String getWindDir() {
		return windDir;
	}
	public final Integer getWindSpdKmH() {
		return windSpdKmH;
	}
	public final Integer getWindSpdKts() {
		return windSpdKts;
	}
	public final Integer getWindGustKmH() {
		return windGustKmH;
	}
	public final Integer getWindGustKts() {
		return windGustKts;
	}
	public final Double getPressureQNH() {
		return pressureQNH;
	}
	public final Double getPressureMSL() {
		return pressureMSL;
	}
	public final String getRainTrace() {
		return rainTrace;
	}

	public final String toString() {
		return this.latitude + "," + this.longitude + "-" + this.cloudType + "-" + this.windDir + "-"
				+ this.windSpdKmH+"-"+this.rainTrace;
	}
}
