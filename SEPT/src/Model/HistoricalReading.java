package Model;

import org.joda.time.LocalDate;

/**
 * Data structure for storing historical BOM daily station readings.
 */
public final class HistoricalReading
{
	public final LocalDate date;

	public final Double min;
	public final Double max;

	public final Double temp9AM;
	public final Double temp3PM;

	public HistoricalReading(String date, Double min, Double max, Double temp9AM, Double temp3PM) {
		
		this.date = new LocalDate(date);

		this.min = min;
		this.max = max;

		this.temp9AM = temp9AM;
		this.temp3PM = temp3PM;
	}
}
