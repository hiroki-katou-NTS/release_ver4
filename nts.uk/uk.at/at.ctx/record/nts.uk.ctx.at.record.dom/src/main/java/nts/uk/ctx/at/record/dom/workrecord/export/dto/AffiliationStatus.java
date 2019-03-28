package nts.uk.ctx.at.record.dom.workrecord.export.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AffiliationStatus {

	private String employeeID;
	
	private List<PeriodInformation> periodInformation;
	
	private boolean noEmployment;
}
