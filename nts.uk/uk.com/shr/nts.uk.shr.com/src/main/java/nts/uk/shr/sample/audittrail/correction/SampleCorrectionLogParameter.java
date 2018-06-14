package nts.uk.shr.sample.audittrail.correction;

import java.io.Serializable;
import java.util.List;

import lombok.Value;
import nts.arc.time.GeneralDate;
import nts.uk.shr.com.security.audittrail.correction.content.DataValueAttribute;
import nts.uk.shr.com.security.audittrail.correction.content.ItemInfo;

@Value
public class SampleCorrectionLogParameter implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	private final List<SampleCorrectionTarget> targets;
	
	@Value
	public static class SampleCorrectionTarget {
		private final String employeeId;
		private final GeneralDate date;
		private final List<SampleCorrectedItem> correctedItems;
	}
	
	@Value
	public static class SampleCorrectedItem {
		private final String itemName;
		private final int itemNo;
		private final int before;
		private final int after;
		
		public ItemInfo toItemInfo() {
			return ItemInfo.create(
					this.itemName,
					DataValueAttribute.COUNT,
					this.before,
					this.after);
		}
	}
	
}
