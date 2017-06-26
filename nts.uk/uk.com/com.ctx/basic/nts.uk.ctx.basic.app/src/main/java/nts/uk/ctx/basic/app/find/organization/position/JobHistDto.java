package nts.uk.ctx.basic.app.find.organization.position;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.basic.dom.organization.position.JobHistory;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JobHistDto {

	
	private String companyCode;
	private String historyId;
	private GeneralDate startDate;
	private GeneralDate endDate;

	
	public static JobHistDto fromDomain(JobHistory domain) {
		return new JobHistDto(
				domain.getCompanyCode(),
				domain.getHistoryId(),
				domain.getStartDate(),
				domain.getEndDate());
	}
}
