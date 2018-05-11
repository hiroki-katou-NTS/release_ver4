package nts.uk.ctx.at.request.app.command.application.holidayshipment;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.Setter;
import nts.arc.time.GeneralDate;

@Getter
@Setter
public class AbsenceLeaveAppCommand {
	private String appDate;
	private String wkTypeCD;
	private int changeWorkHoursType;
	private WkTimeCommand wkTime1;
	private WkTimeCommand wkTime2;
	private String appID;
	private String wkTimeCD;
	private List<SubDigestionCmd> SubDigestions;
	private List<SubTargetDigestionCmd> subTargetDigestions;

	public GeneralDate getAppDate() {
		return !StringUtils.isEmpty(appDate) ? GeneralDate.fromString(appDate, "yyyy/MM/dd") : null;
	}

	public List<SubDigestionCmd> getSubDigestions() {
		return this.SubDigestions == null ? Collections.emptyList() : this.SubDigestions;
	}

	public List<SubTargetDigestionCmd> getSubTargetDigestions() {
		return this.subTargetDigestions == null ? Collections.emptyList() : this.subTargetDigestions;
	}
}
