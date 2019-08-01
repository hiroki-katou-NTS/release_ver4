package nts.uk.ctx.sys.assist.dom.deletedata;

import java.util.List;
import java.util.Optional;

/**
* データ削除の保存結果
*/
public interface ResultDeletionRepository
{

    List<ResultDeletion> getAllResultDeletion();

    Optional<ResultDeletion> getResultDeletionById(String delId);
    void add(ResultDeletion data);
    /**
	 * 
	 * @param data
	 */
	void update(ResultDeletion data);
	
	void update(ResultDeletion resultDel, ManualSetDeletion manualSetDel);
}
