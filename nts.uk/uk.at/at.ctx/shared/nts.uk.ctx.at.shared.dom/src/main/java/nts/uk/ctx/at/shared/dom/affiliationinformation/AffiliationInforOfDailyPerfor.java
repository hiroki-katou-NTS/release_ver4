package nts.uk.ctx.at.shared.dom.affiliationinformation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.bonuspay.primitives.BonusPaySettingCode;
import nts.uk.ctx.at.shared.dom.dailyattdcal.dailyattendance.affiliationinfor.AffiliationInforOfDailyAttd;
import nts.uk.ctx.at.shared.dom.dailyattdcal.dailyattendance.affiliationinfor.ClassificationCode;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.EmploymentCode;

/**
 * 
 * @author nampt
 * 日別実績の所属情報 - root
 *
 */
@Getter
@NoArgsConstructor
public class AffiliationInforOfDailyPerfor extends AggregateRoot {
	//社員ID
	private String employeeId;
	//年月日
	private GeneralDate ymd;
	//所属情報
	private AffiliationInforOfDailyAttd  affiliationInfor;

	public AffiliationInforOfDailyPerfor(EmploymentCode employmentCode, String employeeId, String jobTitleID,
			String wplID, GeneralDate ymd, ClassificationCode clsCode, BonusPaySettingCode bonusPaySettingCode) {
		super();
		this.employeeId = employeeId;
		this.ymd = ymd;
		this.affiliationInfor = new AffiliationInforOfDailyAttd(employmentCode, jobTitleID, wplID, clsCode,
				bonusPaySettingCode);
	}

	public AffiliationInforOfDailyPerfor(String employeeId, GeneralDate ymd,
			AffiliationInforOfDailyAttd affiliationInfor) {
		super();
		this.employeeId = employeeId;
		this.ymd = ymd;
		this.affiliationInfor = affiliationInfor;
	}
	

}
