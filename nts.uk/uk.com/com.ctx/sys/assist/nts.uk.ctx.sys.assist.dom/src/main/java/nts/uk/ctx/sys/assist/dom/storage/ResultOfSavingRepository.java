package nts.uk.ctx.sys.assist.dom.storage;

import java.util.List;
import java.util.Optional;

import nts.uk.shr.com.enumcommon.NotUseAtr;

/**
 * データ保存の保存結果
 */
public interface ResultOfSavingRepository {

	List<ResultOfSaving> getAllResultOfSaving();

	Optional<ResultOfSaving> getResultOfSavingById(String storeProcessingId);

	void add(ResultOfSaving data);

	void update(String storeProcessingId, int targetNumberPeople, SaveStatus saveStatus, String fileId,
			NotUseAtr deletedFiles);

	void update(String storeProcessingId, int targetNumberPeople, SaveStatus saveStatus);
	
	void update(ResultOfSaving data);
}
