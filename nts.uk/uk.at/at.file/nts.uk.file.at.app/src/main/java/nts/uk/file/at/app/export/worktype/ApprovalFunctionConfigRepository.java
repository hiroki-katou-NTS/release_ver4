package nts.uk.file.at.app.export.worktype;

import java.util.List;

import nts.uk.shr.infra.file.report.masterlist.data.MasterData;

public interface ApprovalFunctionConfigRepository {
	List<MasterData> getAllApprovalFunctionConfig(String cid);
	List<MasterData> getAllEmploymentApprovalSetting(String cid);
}
