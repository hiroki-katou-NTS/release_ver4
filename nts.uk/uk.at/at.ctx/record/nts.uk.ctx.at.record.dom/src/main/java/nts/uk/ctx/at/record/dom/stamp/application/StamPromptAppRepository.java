package nts.uk.ctx.at.record.dom.stamp.application;

import java.util.List;
import java.util.Optional;

public interface StamPromptAppRepository {

	void insert(StamPromptApplication application);
	
	void update(StamPromptApplication application);
	
	Optional<StampResultDisplay> getStampSet (String companyId);

	List<StampRecordDis> getAllStampSetPage(String companyId);
}
