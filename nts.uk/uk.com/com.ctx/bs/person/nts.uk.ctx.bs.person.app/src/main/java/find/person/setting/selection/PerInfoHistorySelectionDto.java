package find.person.setting.selection;

import lombok.Value;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.person.dom.person.setting.selection.PerInfoHistorySelection;

@Value
public class PerInfoHistorySelectionDto {
	private String histId;
	private String selectionItemId;
	private String companyCode;
	private GeneralDate startDate;
	private GeneralDate endDate;

	public static PerInfoHistorySelectionDto fromDomainHistorySelection(PerInfoHistorySelection domain) {
		return new PerInfoHistorySelectionDto(
				domain.getHistId(), 
				domain.getSelectionItemId(), 
				domain.getCompanyCode(),
				domain.getStartDate(), 
				domain.getEndDate());
	}
}
