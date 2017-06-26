package nts.uk.ctx.pr.core.app.command.rule.employment.processing.yearmonth;

import lombok.Getter;
import nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.paydayprocessing.PaydayProcessing;


@Getter
public class PaydayProcessingCommand {
	
	private int processingNo;
	
	private String processingName;
	
	private int dispSet;
	
	private int currentProcessingYm;
	
	private int bonusAtr;
	
	private int bcurrentProcessingYm;

	public PaydayProcessing toDomain(String companyCode) {
		return PaydayProcessing.createSimpleFromJavaType(companyCode, getProcessingNo(), getProcessingName(),
				getDispSet(), getCurrentProcessingYm(), getBonusAtr(), getBcurrentProcessingYm());
	}

}
