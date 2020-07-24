package nts.uk.ctx.at.request.app.find.application.lateleaveearly;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.applicationlatearrival.LateEarlyCancelAppSet;

@Getter
@AllArgsConstructor
public class LateEarlyCancelAppSetDto {
	
	private String companyId;
	
	private int cancelAtr;
	
	public static LateEarlyCancelAppSetDto fromDomain(LateEarlyCancelAppSet lateEarlyCancelAppSet) {
		return new LateEarlyCancelAppSetDto(lateEarlyCancelAppSet.getCompanyID(), lateEarlyCancelAppSet.getCancelAtr().value);
	}
}
