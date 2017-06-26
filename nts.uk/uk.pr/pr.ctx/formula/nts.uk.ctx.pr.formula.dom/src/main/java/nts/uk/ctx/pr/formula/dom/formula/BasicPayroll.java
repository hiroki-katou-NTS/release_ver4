package nts.uk.ctx.pr.formula.dom.formula;

import java.math.BigDecimal;

import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.pr.formula.dom.primitive.BaseDay;
import nts.uk.ctx.pr.formula.dom.primitive.BaseHour;
import nts.uk.ctx.pr.formula.dom.primitive.TimeNotationSetting;

@Getter
public class BasicPayroll extends AggregateRoot {

	private String companyCode;

	private TimeNotationSetting timeNotationSetting;

	private BaseDay baseDays;

	private BaseHour baseHours;


	/**
	 * @param companyCode
	 * @param timeNotationSetting
	 * @param baseDays
	 * @param baseHours
	 */
	public BasicPayroll(String companyCode, TimeNotationSetting timeNotationSetting, BaseDay baseDays,
			BaseHour baseHours) {
		super();
		this.companyCode = companyCode;
		this.timeNotationSetting = timeNotationSetting;
		this.baseDays = baseDays;
		this.baseHours = baseHours;
	}

	public static BasicPayroll createFromJavaType(String companyCode, BigDecimal timeNotationSetting,
			BigDecimal baseDays, BigDecimal baseHours) {
		return new BasicPayroll(companyCode, new TimeNotationSetting(timeNotationSetting), new BaseDay(baseDays),
				new BaseHour(baseHours));
	}

}
