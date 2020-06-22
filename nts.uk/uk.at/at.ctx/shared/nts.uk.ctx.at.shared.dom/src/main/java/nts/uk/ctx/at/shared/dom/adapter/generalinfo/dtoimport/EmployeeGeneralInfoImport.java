package nts.uk.ctx.at.shared.dom.adapter.generalinfo.dtoimport;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EmployeeGeneralInfoImport {
	
	List<ExEmploymentHistoryImport> employmentHistoryImports;
	
	List<ExClassificationHistoryImport> exClassificationHistoryImports;
	
	List<ExJobTitleHistoryImport> exJobTitleHistoryImports;
	
	List<ExWorkPlaceHistoryImport> exWorkPlaceHistoryImports;

}
