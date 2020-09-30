package nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.ctx.at.shared.dom.remainingnumber.common.empinfo.grantremainingdata.daynumber.LeaveNumberInfo;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.grantnumber.SpecialLeaveGrantNumber;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.remainingnumber.SpecialLeaveRemainingNumber;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.usenumber.SpecialLeaveUsedNumber;

@Getter
@Setter

//特別休暇数情報
public class SpecialLeaveNumberInfo extends LeaveNumberInfo implements Serializable {

//	//付与数
//	private SpecialLeaveGrantNumber grantNumber;
//	//使用数
//	private SpecialLeaveUsedNumber usedNumber;
//	//残数
//	private SpecialLeaveRemainingNumber remainingNumber;
	
	public SpecialLeaveNumberInfo(
			double dayNumberOfGrant, 
			Integer timeOfGrant, 
			double dayNumberOfUse, 
			Integer timeOfUse,
			double useSavingDays, 
			double numberOverDays, 
			Integer timeOver, 
			double dayNumberOfRemain, 
			Integer timeOfRemain) {
		
		this.grantNumber = SpecialLeaveGrantNumber.createFromJavaType(dayNumberOfGrant, timeOfGrant);
		this.usedNumber = SpecialLeaveUsedNumber.createFromJavaType(
				dayNumberOfUse, timeOfUse, useSavingDays, numberOverDays, timeOver);
		this.remainingNumber = SpecialLeaveRemainingNumber.createFromJavaType(dayNumberOfRemain, timeOfRemain);
	}
	
	public SpecialLeaveNumberInfo(double dayNumberOfGrant, Integer timeOfGrant,double dayNumberOfUse, Integer timeOfUse,
			Double useSavingDays, double numberOverDays, Integer timeOver, double dayNumberOfRemain, Integer timeOfRemain) {
		this.grantNumber = SpecialLeaveGrantNumber.createFromJavaType(dayNumberOfGrant, timeOfGrant);
		this.usedNumber = SpecialLeaveUsedNumber.createFromJavaType(dayNumberOfUse,timeOfUse,useSavingDays,numberOverDays,timeOver);
		this.remainingNumber = SpecialLeaveRemainingNumber.createFromJavaType(dayNumberOfRemain, timeOfRemain);
	}
	
	

}
