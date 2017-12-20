package nts.uk.ctx.at.shared.dom.calculation.holiday.time;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.uk.ctx.at.shared.dom.calculation.holiday.TransferFrameName;
import nts.uk.ctx.at.shared.dom.calculation.holiday.WorkdayFrameName;

@AllArgsConstructor
@Getter
public class WorkdayFrame {
	/** 会社ID */
	private String companyId;

	/** NO */
	private int workdayFrameNo;

	/** 休出枠名称 */
	private WorkdayFrameName workdayFrameName;

	/** 使用区分 */
	private int useAtr;

	/** 振替枠名称 */
	private TransferFrameName transferFrameName;

	public static WorkdayFrame createFromJavaType(String companyId, int workdayFrameNo, String workdayFrameName,
			int useAtr, String transferFrameName) {
		return new WorkdayFrame(companyId, workdayFrameNo, new WorkdayFrameName(workdayFrameName), useAtr,
				new TransferFrameName(transferFrameName));
	}
}
