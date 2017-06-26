package nts.uk.ctx.at.record.app.command.divergencetime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeItemSet {
	/*会社ID*/
	private String companyId;
	/*乖離時間ID*/
	private int divTimeId;
	/*勤怠項目ID*/
	private int attendanceId;
}
