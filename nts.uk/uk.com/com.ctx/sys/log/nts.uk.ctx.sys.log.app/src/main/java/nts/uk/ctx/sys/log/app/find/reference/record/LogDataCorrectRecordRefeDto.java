package nts.uk.ctx.sys.log.app.find.reference.record;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.time.GeneralDate;
import nts.gul.text.IdentifierUtil;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.com.security.audittrail.correction.content.CorrectionAttr;
import nts.uk.shr.com.security.audittrail.correction.content.DataCorrectionLog;
import nts.uk.shr.com.security.audittrail.correction.content.TargetDataType;

/**
 * 
 * @author Thuongtv
 *
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LogDataCorrectRecordRefeDto {
	private String parentKey;
	private String childrentKey;
	private String operationId;
	private String targetDate;
	private int targetDataType;
	private String itemName;
	private String valueBefore;
	private String valueAfter;
	private String remarks;
	private String correctionAttr;
	private String userNameTaget;
	private String employeeIdtaget;
	private int showOrder;

	public static LogDataCorrectRecordRefeDto fromDomain(DataCorrectionLog domain) {
		String childrentKey = IdentifierUtil.randomUniqueId();
		String targetDateStr = "";
		GeneralDate targetDate = domain.getTargetDataKey().getDateKey();
		TargetDataType tagetData = TargetDataType.of(domain.getTargetDataType().value);
		switch (tagetData) {
		case SCHEDULE:
		case DAILY_RECORD:	
			targetDateStr = targetDate.toString("yyyy/MM/dd");
			break;
		case MONTHLY_RECORD:
		case ANY_PERIOD_SUMMARY:
		case SALARY_DETAIL:
		case BONUS_DETAIL:
			targetDateStr = targetDate.toString("yyyy/MM");
			break;
		case YEAR_END_ADJUSTMENT:
		case MONTHLY_CALCULATION:
		case RISING_SALARY_BACK:
			targetDateStr = targetDate.toString("yyyy");
			break;
		default:
			targetDateStr = targetDate.toString("yyyy/MM/dd");
			break;
		}
		
		return new LogDataCorrectRecordRefeDto("",
				childrentKey,
				domain.getOperationId(),
				targetDateStr,
				domain.getTargetDataType().value,
				domain.getCorrectedItem().getName(),
				domain.getCorrectedItem().getValueBefore().getViewValue(),
				domain.getCorrectedItem().getValueAfter().getViewValue(),
				domain.getRemark(),getCorrectionAttr(domain.getCorrectionAttr().value),
				domain.getTargetUser().getUserName(),domain.getTargetUser().getEmployeeId(),domain.getShowOrder()
				);
		
	}
	
	private static String getCorrectionAttr(int attr) {
		CorrectionAttr correctionAttr = CorrectionAttr.of(attr);
		return correctionAttr.localize();
	}
	
}
