package nts.uk.ctx.basic.dom.organization.position;

import lombok.Getter;
import lombok.Setter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.shr.com.primitive.Memo;

@Setter
@Getter
public class JobTitle extends AggregateRoot {

	private String companyCode;

	private String historyId;

	private JobCode jobCode;

	private JobName jobName;

	private JobCode jobOutCode;
	
	private PresenceCheckScopeSet presenceCheckScopeSet;

	private Memo memo;

	private HiterarchyOrderCode hiterarchyOrderCode;

	public JobTitle(
			String companyCode, 
			String historyId, 
			JobCode jobCode, 
			JobName jobName, 
			PresenceCheckScopeSet presenceCheckScopeSet, 
			JobCode jobOutCode,
			Memo memo, 
			HiterarchyOrderCode hierarchyOrderCode) {
				super();
				this.companyCode = companyCode;
				this.historyId = historyId;
				this.jobCode = jobCode;
				this.jobName = jobName;
				this.presenceCheckScopeSet = presenceCheckScopeSet;
				this.jobOutCode = jobOutCode;
				this.memo = memo;
				this.hiterarchyOrderCode = hierarchyOrderCode;
	}

	public static JobTitle createFromJavaType(
			String companyCode, 
			String historyId, 
			String jobCode, 
			String jobName, 
			int presenceCheckScopeSet,
			String jobOutCode, 
			String memo, 
			String hierarchyOrderCode) {
		return new JobTitle(
				companyCode, 
				historyId, 
				new JobCode(jobCode), 
				new JobName(jobName), 
				EnumAdaptor.valueOf(presenceCheckScopeSet, PresenceCheckScopeSet.class),
				new JobCode(jobOutCode),
				new Memo(memo), 
				new HiterarchyOrderCode(hierarchyOrderCode));
	}

}