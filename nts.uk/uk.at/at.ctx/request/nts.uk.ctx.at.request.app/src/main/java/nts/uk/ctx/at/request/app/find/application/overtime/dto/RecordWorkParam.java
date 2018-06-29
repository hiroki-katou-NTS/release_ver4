package nts.uk.ctx.at.request.app.find.application.overtime.dto;

import java.util.List;

import lombok.Data;
import nts.uk.ctx.at.request.dom.application.overtime.service.CaculationTime;

@Data
public class RecordWorkParam {
	public String employeeID; 
	public String appDate;
	public String siftCD;
	public int prePostAtr;
	public List<CaculationTime> overtimeHours;
	private String workTypeCode;
	private Integer startTimeRest;
	private Integer endTimeRest;
}
