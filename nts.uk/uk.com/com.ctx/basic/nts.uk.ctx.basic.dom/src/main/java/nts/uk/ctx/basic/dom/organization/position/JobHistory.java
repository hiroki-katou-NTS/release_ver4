package nts.uk.ctx.basic.dom.organization.position;

import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;

@Setter
@Getter
public class JobHistory extends AggregateRoot {
	
	private String companyCode;
	
	private String historyId;

	private GeneralDate startDate;
	
	private GeneralDate endDate;
	
	public JobHistory( String companyCode,String historyId,GeneralDate startDate,GeneralDate endDate) {
		super();
		this.companyCode = companyCode;
		this.historyId = historyId;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public static JobHistory createFromJavaType( String companyCode,String historyId,GeneralDate startDate,GeneralDate endDate) {
		return new JobHistory(companyCode,historyId,startDate,endDate);

	}
}
