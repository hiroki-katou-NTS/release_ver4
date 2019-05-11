package nts.uk.ctx.at.request.app.find.application.overtime.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Value;
import nts.uk.ctx.at.request.dom.application.overtime.service.CaculationTime;

@Value
public class ParamChangeAppDate {
	/**
	 * appDate
	 */
	private String appDate;
	/**
	 * prePostAtr
	 */
	private int prePostAtr;
	/**
	 * siftCD
	 */
	private String siftCD;
	
	/**
	 * overtimeHours
	 */
	private List<CaculationTime> overtimeHours;
	private String workTypeCode;
	private Integer startTime;
	private Integer endTime;
	private List<Integer> startTimeRests;
	private List<Integer> endTimeRests;
	private String selectedEmplCodes;
	
	
	private int overtimeAtr;
	
	public List<Integer> getStartTimeRests() {

		List<Integer> result = new ArrayList<>();
		startTimeRests.forEach(x -> {
			if (x != null) {
				result.add(x);
			}
		});

		return result;
	}
	
	public List<Integer> getEndTimeRests() {

		List<Integer> result = new ArrayList<>();
		endTimeRests.forEach(x -> {
			if (x != null) {
				result.add(x);
			}
		});

		return result;
	}

}
