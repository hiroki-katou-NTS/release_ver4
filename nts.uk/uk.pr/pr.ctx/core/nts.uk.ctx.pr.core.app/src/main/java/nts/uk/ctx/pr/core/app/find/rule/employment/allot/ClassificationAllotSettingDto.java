package nts.uk.ctx.pr.core.app.find.rule.employment.allot;

import lombok.AllArgsConstructor;
import lombok.Data;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.allot.ClassificationAllotSetting;

@Data
@AllArgsConstructor
public class ClassificationAllotSettingDto {

	String companyCode;
	String historyId;
	String bonusDetailCode;
	String paymentDetailCode;
	String classificationCode;
	String classificationName;


	
	public static ClassificationAllotSettingDto fromDomain(ClassificationAllotSetting classificationAllotSetting) {
		return new ClassificationAllotSettingDto(classificationAllotSetting.getCompanyCode().v(),
				classificationAllotSetting.getHistotyId(), classificationAllotSetting.getBonusDetailCode().v(),
				classificationAllotSetting.getPaymentDetailCode().v(),
				classificationAllotSetting.getClassificationCode().v(), null);
	}

}
