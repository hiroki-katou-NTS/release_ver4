package nts.uk.ctx.at.record.dom.workrecord.export.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

//社員の会社所属情報(年月)
@AllArgsConstructor
@Getter
public class EmpAffInfoExport {
	
	private List<AffiliationStatus> affiliationStatus;
}
