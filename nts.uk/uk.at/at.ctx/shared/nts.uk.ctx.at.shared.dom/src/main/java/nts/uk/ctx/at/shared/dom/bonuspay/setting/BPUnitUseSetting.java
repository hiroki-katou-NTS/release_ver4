/**
 * 9:59:33 AM Jun 6, 2017
 */
package nts.uk.ctx.at.shared.dom.bonuspay.setting;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.at.shared.dom.bonuspay.enums.UseAtr;
import nts.uk.ctx.at.shared.dom.common.CompanyId;

/**
 * @author hungnm
 *
 */
@Getter
public class BPUnitUseSetting extends AggregateRoot {

	private CompanyId companyId;

	private UseAtr workplaceUseAtr;

	private UseAtr personalUseAtr;

	private UseAtr workingTimesheetUseAtr;

	private BPUnitUseSetting(CompanyId companyId, UseAtr workplaceUseAtr, UseAtr personalUseAtr,
			UseAtr workingTimesheetUseAtr) {
		super();
		this.companyId = companyId;
		this.workplaceUseAtr = workplaceUseAtr;
		this.personalUseAtr = personalUseAtr;
		this.workingTimesheetUseAtr = workingTimesheetUseAtr;
	}

	private BPUnitUseSetting() {
		super();
	}

	public static BPUnitUseSetting createFromJavaType(String companyId, int workplaceUseAtr, int personalUseAtr,
			int workingTimesheetUseAtr) {
		return new BPUnitUseSetting(new CompanyId(companyId), EnumAdaptor.valueOf(workplaceUseAtr, UseAtr.class),
				EnumAdaptor.valueOf(personalUseAtr, UseAtr.class),
				EnumAdaptor.valueOf(workingTimesheetUseAtr, UseAtr.class));
	}
}
