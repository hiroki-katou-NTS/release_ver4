package nts.uk.ctx.at.record.app.find.standardtime;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class AgreementYearSettingDto {

	private int yearValue;
	
	private BigDecimal errorOneYear;
	
	private BigDecimal alarmOneYear;
}
