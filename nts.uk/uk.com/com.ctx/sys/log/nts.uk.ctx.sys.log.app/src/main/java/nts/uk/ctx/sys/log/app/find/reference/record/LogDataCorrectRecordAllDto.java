package nts.uk.ctx.sys.log.app.find.reference.record;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.arc.time.GeneralDate;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.com.security.audittrail.correction.content.CorrectionAttr;
import nts.uk.shr.com.security.audittrail.correction.content.DataCorrectionLog;

/**
 * 
 * @author huannv
 *
 */

@Getter
@Setter
@AllArgsConstructor
public class LogDataCorrectRecordAllDto {
	private String operationId;
	private GeneralDate targetDate;
	private int targetDataType;
	private String itemName;
	private String valueBefore;
	private String valueAfter;
	private String remarks;
	private String correctionAttr;
	private String userNameTaget;
	private String employeeIdtaget;
	private String userIdtaget;
	//
	private String tarGetYmd;
	private String tarGetYm;
	private String tarGetY;
	private String keyString;
	private String catagoryCorection;
	private String itemContentValueBefor;
	private String itemContentValueAppter;

	public static LogDataCorrectRecordAllDto fromDomain(DataCorrectionLog domain) {

		return new LogDataCorrectRecordAllDto(domain.getOperationId(), domain.getTargetDataKey().getDateKey(),
				domain.getTargetDataType().value, domain.getCorrectedItem().getName(),
				domain.getCorrectedItem().getValueBefore().getRawValue() == null ? ""
						: domain.getCorrectedItem().getValueBefore().getRawValue().getValue() == null ? ""
								: domain.getCorrectedItem().getValueBefore().getRawValue().getValue().toString(),
				domain.getCorrectedItem().getValueAfter().getRawValue() == null ? ""
						: domain.getCorrectedItem().getValueAfter().getRawValue().getValue() == null ? ""
								: domain.getCorrectedItem().getValueAfter().getRawValue().getValue().toString(),
				domain.getRemark(), getCorrectionAttr(domain.getCorrectionAttr().value),
				domain.getTargetUser().getUserName(), domain.getTargetUser().getEmployeeId(),
				domain.getTargetUser().getUserId(), getTarGetYmd(domain), getTarGetYm(domain), getarGetY(domain),
				domain.getTargetDataKey().getStringKey().isPresent() ? domain.getTargetDataKey().getStringKey().get()
						: "",
				getCorrectionAttr(domain.getCorrectionAttr().value),
				domain.getCorrectedItem().getValueBefore().getViewValue(),
				domain.getCorrectedItem().getValueAfter().getViewValue());

	}

	private static String getCorrectionAttr(int attr) {
		CorrectionAttr correctionAttr = CorrectionAttr.of(attr);
		return correctionAttr.localize();
	}

	private static String getTarGetYm(DataCorrectionLog domain) {

		// if (domain.getTargetDataKey().getDateKey().isPresent()) {
		GeneralDate targetDate = domain.getTargetDataKey().getDateKey();
		return String.valueOf(targetDate.yearMonth());
		// }

		// return null;
	}

	private static String getarGetY(DataCorrectionLog domain) {

		// if (domain.getTargetDataKey().getDateKey().isPresent()) {
		GeneralDate targetDate = domain.getTargetDataKey().getDateKey();
		return String.valueOf(targetDate.year());
		// }

		// return null;
	}

	private static String getTarGetYmd(DataCorrectionLog domain) {

		// if (domain.getTargetDataKey().getDateKey().isPresent()) {
		GeneralDate targetDate = domain.getTargetDataKey().getDateKey();
		return targetDate.toString();
		// }

		// return null;
	}

}
