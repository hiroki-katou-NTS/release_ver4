package nts.uk.ctx.sys.assist.dom.datarestoration;

import java.util.List;
import java.util.Optional;

/**
 * データ復旧の結果ログ
 */
public interface DataRecoveryLogRepository {

	int getMaxSeqId(String delId);
	
    List<DataRecoveryLog> getAllResultLogDeletion();

    Optional<DataRecoveryLog> getResultLogDeletionById(String delId);
    
    void add(DataRecoveryLog data);
	
}
