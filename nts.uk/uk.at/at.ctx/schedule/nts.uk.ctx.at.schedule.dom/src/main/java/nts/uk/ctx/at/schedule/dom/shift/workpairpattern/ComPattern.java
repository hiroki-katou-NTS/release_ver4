package nts.uk.ctx.at.schedule.dom.shift.workpairpattern;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;

/**
 * 会社勤務ペアパターングループ
 * 
 * @author sonnh1
 *
 */
@Getter
@AllArgsConstructor
public class ComPattern extends AggregateRoot {
	private String companyId;
	private int groupNo;
	private WorkPairPatternGroupName groupName;
	private GroupUsageAtr groupUsageAtr;
	private String note;

	public static ComPattern convertFromJavaType(String companyId, int groupNo, String groupName, int groupUsageAtr,
			String note) {
		return new ComPattern(companyId, groupNo, new WorkPairPatternGroupName(groupName),
				EnumAdaptor.valueOf(groupUsageAtr, GroupUsageAtr.class), note);
	}
}
