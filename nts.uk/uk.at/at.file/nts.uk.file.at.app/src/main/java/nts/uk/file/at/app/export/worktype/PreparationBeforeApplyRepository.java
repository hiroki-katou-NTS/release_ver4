package nts.uk.file.at.app.export.worktype;

import java.util.List;

public interface PreparationBeforeApplyRepository{
	
	List<Object[]> getChangePerInforDefinitionToExport(String cid);
	List<Object[]> getExtraData(String cid);
	List<Object[]> getJob(String cid, String baseDate);
}