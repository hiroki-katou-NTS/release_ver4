package nts.uk.ctx.pr.core.dom.paymentdata.service;

import java.util.List;

import lombok.Value;
import nts.arc.time.YearMonth;
import nts.uk.ctx.pr.core.dom.allot.PersonalAllotSetting;
import nts.uk.ctx.pr.core.dom.paymentdata.PaymentCalculationBasicInformation;
import nts.uk.ctx.pr.core.dom.paymentdata.paymentdatemaster.PaymentDateMaster;
import nts.uk.ctx.pr.core.dom.personalinfo.employmentcontract.PersonalEmploymentContract;
import nts.uk.ctx.pr.core.dom.personalinfo.holiday.HolidayPaid;
import nts.uk.ctx.pr.core.dom.personalinfo.wage.PersonalWage;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.detail.LayoutMasterDetail;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.line.LayoutMasterLine;
import nts.uk.shr.com.primitive.PersonId;

@Value
public class PaymentDetailParam {
	String companyCode;
	PersonId personId;
	YearMonth baseYearMonth;
	YearMonth currentProcessingYearMonth;
	HolidayPaid holiday;
	PersonalEmploymentContract employmentContract;
	PaymentCalculationBasicInformation payCalBasicInfo;
	PaymentDateMaster paymentDateMaster;
	PersonalAllotSetting personalAllotSetting;
	List<LayoutMasterDetail> layoutMasterDetailList;
	List<LayoutMasterLine> lineList;
	List<PersonalWage> personalWageList;
	
	public int getStartYearMonth() {
		return personalAllotSetting.getStartDate().v();
	}
}
