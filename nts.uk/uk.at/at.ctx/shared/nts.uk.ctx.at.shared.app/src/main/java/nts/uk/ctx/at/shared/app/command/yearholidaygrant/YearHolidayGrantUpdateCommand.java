package nts.uk.ctx.at.shared.app.command.yearholidaygrant;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Value;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.ConditionValue;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.GrantCondition;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.GrantHdTblSet;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.YearHolidayCode;
import nts.uk.shr.com.context.AppContexts;

@Value
public class YearHolidayGrantUpdateCommand {
	/* 会社ID */
	private String companyId;

	/* コード */
	private String yearHolidayCode;

	/* 名称 */
	private String yearHolidayName;

	/* 計算方法 */
	private int calculationMethod;

	/* 計算基準 */
	private int standardCalculation;

	/* 一斉付与を利用する */
	private int useSimultaneousGrant;

	/* 一斉付与日 */
	private int simultaneousGrandMonthDays;

	/* 備考 */
	private String yearHolidayNote;
	
	private List<GrantConditionCommand> grantConditions;

	/**
	 * Convert to domain object
	 * @return
	 */
	public GrantHdTblSet toDomain() {
		String companyId = AppContexts.user().companyId();
		
		List<GrantCondition> grantConditionList = this.grantConditions.stream().map(x-> {
			return new GrantCondition(companyId, new YearHolidayCode(yearHolidayCode), x.getConditionNo(), new ConditionValue(x.getConditionValue()), x.getUseConditionAtr());
		}).collect(Collectors.toList());
		
		return  GrantHdTblSet.createFromJavaType(companyId, yearHolidayCode, yearHolidayName, calculationMethod, standardCalculation,
				useSimultaneousGrant, simultaneousGrandMonthDays, yearHolidayNote, grantConditionList);
	}
}
