package nts.uk.ctx.at.record.dom.adapter.generalinfo.dtoimport;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ExEmploymentHistoryImport {

	private String employeeId;
	
	private List<ExEmploymentHistItemImport> employmentItems;
}
