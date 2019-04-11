package nts.uk.screen.at.app.monthlyperformance.correction.dto;

import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.monthly.root.common.ClosureDateDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MPItemParent {
	
	/** 年月: 年月 */
	private Integer yearMonth;

	/** 締めID: 締めID */
	private int closureId;

	/** 締め日: 日付 */
	private ClosureDateDto closureDate;
	
	/**
	 * data has been changed
	 */
	private List<MPItemDetail> mPItemDetails;
	
	private GeneralDate startDate;
	
	private GeneralDate endDate;
	
	private List<MPItemCheckBox> dataCheckSign;
	
	private List<MPItemCheckBox> dataCheckApproval;

	private Set<MPItemVersion> dataLock;
}
