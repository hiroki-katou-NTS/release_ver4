package nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing;

import java.util.List;

public interface ErrMessageInfoRepository {

	/**
	 * get all ErrMessageInfo by empCalAndSumExecLogID,executionContent
	 * 
	 * @param empCalAndSumExecLogID
	 * @return
	 */
	List<ErrMessageInfo> getAllErrMessageInfoByID(String empCalAndSumExecLogID, int executionContent);

	/**
	 * get all ErrMessageInfo by empCalAndSumExecLogID
	 * 
	 * @param empCalAndSumExecLogID
	 * @return
	 */
	List<ErrMessageInfo> getAllErrMessageInfoByEmpID(String empCalAndSumExecLogID);
	
	/** Get ErrMessageInfo by EmpCalAndSumExecLogID and ExecutionContent */
	List<ErrMessageInfo> getErrMessageInfoByExecutionContent(String empCalAndSumExecLogID, int executionContent);
	
	/** Add an ErrMessageInfo */
	void add(ErrMessageInfo errMessageInfo);

	/**
	 * For KIF 001
	 * 
	 * @param errMessageInfos
	 */
	void addList(List<ErrMessageInfo> errMessageInfos);

}
