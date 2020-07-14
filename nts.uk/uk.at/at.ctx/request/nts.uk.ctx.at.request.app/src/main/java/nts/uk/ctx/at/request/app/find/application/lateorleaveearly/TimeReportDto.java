package nts.uk.ctx.at.request.app.find.application.lateorleaveearly;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.at.request.dom.application.lateorleaveearly.LateOrEarlyClassification;
import nts.uk.ctx.at.request.dom.application.lateorleaveearly.TimeReport;
import nts.uk.shr.com.time.TimeWithDayAttr;

@Data
@AllArgsConstructor
@NoArgsConstructor
// 遅刻早退時刻報告
public class TimeReportDto {
	// 勤務NO
	private int workNo;
	// 区分
	private int lateOrEarlyClassification;
	// 時刻
	// wait handle
	private int timeWithDayAttr;

	public static TimeReportDto fromDomain(TimeReport value) {
		return new TimeReportDto(value.getWorkNo(), value.getLateOrEarlyClassification().value, 1);
	}

	public TimeReport toDomain() {
		return new TimeReport(workNo, EnumAdaptor.valueOf(lateOrEarlyClassification, LateOrEarlyClassification.class),
				new TimeWithDayAttr(timeWithDayAttr));
	}
}
