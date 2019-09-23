package nts.uk.ctx.at.record.pub.workrecord.workperfor.dailymonthlyprocessing;

import java.util.List;

public interface ErrMessageInfoPub {
	/**
	 * get all ErrMessageInfo by empCalAndSumExecLogID,executionContent
	 * 
	 * @param empCalAndSumExecLogID
	 * @return
	 */
	List<ErrMessageInfoEx> getAllErrMessageInfoByID(String empCalAndSumExecLogID, int executionContent);

	/**
	 * get all ErrMessageInfo by empCalAndSumExecLogID
	 * 
	 * @param empCalAndSumExecLogID
	 * @return
	 */
	List<ErrMessageInfoEx> getAllErrMessageInfoByEmpID(String empCalAndSumExecLogID);
	
	/** Get ErrMessageInfo by EmpCalAndSumExecLogID and ExecutionContent */
	List<ErrMessageInfoEx> getErrMessageInfoByExecutionContent(String empCalAndSumExecLogID, int executionContent);
	
	/** Add an ErrMessageInfo */
	void add(ErrMessageInfoEx errMessageInfo);

	/**
	 * For KIF 001
	 * 
	 * @param errMessageInfos
	 */
	void addList(List<ErrMessageInfoEx> errMessageInfos);
}
