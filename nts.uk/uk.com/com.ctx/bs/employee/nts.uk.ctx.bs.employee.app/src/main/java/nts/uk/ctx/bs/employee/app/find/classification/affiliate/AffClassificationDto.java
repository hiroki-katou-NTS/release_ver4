/**
 * 
 */
package nts.uk.ctx.bs.employee.app.find.classification.affiliate;

import lombok.Setter;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.dom.classification.affiliate_ver1.AffClassHistItem_ver1;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.pereg.app.PeregItem;
import nts.uk.shr.pereg.app.find.dto.PeregDomainDto;

/**
 * @author danpv
 *
 */
@Setter
public class AffClassificationDto extends PeregDomainDto {

	@PeregItem("IS00025")
	/**
	 * 期間
	 */
	private String periodName;

	/**
	 * 開始日
	 */
	@PeregItem("IS00026")
	private GeneralDate startDate;

	/**
	 * 終了日
	 */
	@PeregItem("IS00027")
	private GeneralDate endDate;

	/**
	 * 分類CD
	 */
	@PeregItem("IS00028")
	private String classificationCode;

	public AffClassificationDto() {

	}

	public AffClassificationDto(String recordId, String employeeId) {
		super(recordId, employeeId, null);
	}

	public static AffClassificationDto createFromDomain(AffClassHistItem_ver1 histItem, DateHistoryItem dateHistoryItem) {
		AffClassificationDto dto = new AffClassificationDto(histItem.getHistoryId(), histItem.getEmployeeId());
		dto.setStartDate(dateHistoryItem.start());
		dto.setEndDate(dateHistoryItem.end());
		dto.setClassificationCode(histItem.getClassificationCode().v());
		return dto;
	}

}
