package nts.uk.ctx.pr.core.app.find.rule.employment.allot;

import java.math.BigDecimal;

import lombok.Value;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.allot.ClassificationAllotSettingHeader;

@Value
public class ClassificationAllotSettingHeaderDto {

	String companyCode;
	String historyId;
	BigDecimal startDateYM;
	BigDecimal endDateYM;
	
	public static ClassificationAllotSettingHeaderDto fromDomain (ClassificationAllotSettingHeader classificationAllotSettingHeader) {
		return new ClassificationAllotSettingHeaderDto (classificationAllotSettingHeader.getCompanyCode().v(),
				classificationAllotSettingHeader.getHistoryId(), classificationAllotSettingHeader.getStartDateYM(),
				classificationAllotSettingHeader.getEndDateYM());
				
	} 
}
