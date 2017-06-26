package nts.uk.ctx.basic.dom.organization.department;

import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.shr.com.primitive.Memo;

@Getter
public class DepartmentMemo extends AggregateRoot{

	private final String companyCode;

	private final String historyId;

	private Memo memo;

	public DepartmentMemo(String companyCode, String historyId, Memo memo) {
		this.companyCode = companyCode;
		this.historyId = historyId;
		this.memo = memo;
	}

	public static DepartmentMemo createFromJavaType(String companyCode, String historyId, String memo) {
		return new DepartmentMemo(companyCode, companyCode, new Memo(memo));
	}
}
