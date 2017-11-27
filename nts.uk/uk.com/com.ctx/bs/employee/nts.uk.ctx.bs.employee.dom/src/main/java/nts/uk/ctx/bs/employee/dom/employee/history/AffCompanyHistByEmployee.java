package nts.uk.ctx.bs.employee.dom.employee.history;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.layer.dom.DomainObject;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
/** 所属会社履歴（社員別） */
public class AffCompanyHistByEmployee extends DomainObject {
	/** 社員ID */
	private String sId;

	/** 履歴 */
	private List<AffCompanyHistItem> lstAffCompanyHistoryItem;

	public AffCompanyHistItem getAffCompanyHistItem(String historyId) {
		if (lstAffCompanyHistoryItem == null) {
			lstAffCompanyHistoryItem = new ArrayList<AffCompanyHistItem>();
		}
		
		List<AffCompanyHistItem> filter = lstAffCompanyHistoryItem.stream()
				.filter(m -> m.getHistoryId().equals(historyId)).collect(Collectors.toList());

		if (!filter.isEmpty()) {
			return filter.get(0);
		}

		return null;
	}

	public void addAffCompanyHistItem(AffCompanyHistItem domain) {
		if (lstAffCompanyHistoryItem == null) {
			lstAffCompanyHistoryItem = new ArrayList<AffCompanyHistItem>();
		}
		
		lstAffCompanyHistoryItem.add(domain);
	}
}
