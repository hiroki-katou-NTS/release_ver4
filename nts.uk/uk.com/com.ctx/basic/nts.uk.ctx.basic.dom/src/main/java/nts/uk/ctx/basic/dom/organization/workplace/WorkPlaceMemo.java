package nts.uk.ctx.basic.dom.organization.workplace;

import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.shr.com.primitive.Memo;

@Getter
public class WorkPlaceMemo extends AggregateRoot{

	private final String companyCode;

	private final String historyId;

	private Memo memo;

	public WorkPlaceMemo(String companyCode, String historyId, Memo memo) {
		this.companyCode = companyCode;
		this.historyId = historyId;
		this.memo = memo;
	}

}
