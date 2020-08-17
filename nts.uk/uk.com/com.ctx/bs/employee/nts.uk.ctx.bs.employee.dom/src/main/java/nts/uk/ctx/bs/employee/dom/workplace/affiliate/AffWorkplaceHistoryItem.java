package nts.uk.ctx.bs.employee.dom.workplace.affiliate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

/**
 * The Class AffWorkplaceHistoryItem.
 */
// 所属職場履歴項目
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AffWorkplaceHistoryItem {

	/** The history Id. */
	// 履歴ID
	private String historyId;
	
	/** The Employee Id. */
	// 社員ID
	private String employeeId;
	
	/** The workplaceId. */
	// 職場ID
	private String  workplaceId;
	
	/** The normalWorkplaceCode. */
	// 通常職場コード
	private String  normalWorkplaceId;

	private Optional<String>  workLocationCode;
	
	public static AffWorkplaceHistoryItem createFromJavaType(String histId, String employeeId, String workplaceId, String normalWorkplaceId){
		return new AffWorkplaceHistoryItem(histId,employeeId, workplaceId, normalWorkplaceId);

	}

	public AffWorkplaceHistoryItem(String historyId, String employeeId, String workplaceId, String normalWorkplaceId) {
		this.historyId = historyId;
		this.employeeId = employeeId;
		this.workplaceId = workplaceId;
		this.normalWorkplaceId = normalWorkplaceId;
		this.workLocationCode = Optional.empty();
	}
}
