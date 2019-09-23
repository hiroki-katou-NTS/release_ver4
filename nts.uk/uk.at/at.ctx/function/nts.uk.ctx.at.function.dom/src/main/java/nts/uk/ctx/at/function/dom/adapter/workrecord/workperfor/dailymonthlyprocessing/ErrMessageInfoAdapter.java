package nts.uk.ctx.at.function.dom.adapter.workrecord.workperfor.dailymonthlyprocessing;

import java.util.List;

public interface ErrMessageInfoAdapter {
	/**
	 * get all ErrMessageInfo by empCalAndSumExecLogID,executionContent
	 * 
	 * @param empCalAndSumExecLogID
	 * @return
	 */
	List<ErrMessageInfoImport> getAllErrMessageInfoByID(String empCalAndSumExecLogID, int executionContent);

	/**
	 * get all ErrMessageInfo by empCalAndSumExecLogID
	 * 
	 * @param empCalAndSumExecLogID
	 * @return
	 */
	List<ErrMessageInfoImport> getAllErrMessageInfoByEmpID(String empCalAndSumExecLogID);

	/** Get ErrMessageInfo by EmpCalAndSumExecLogID and ExecutionContent */
	List<ErrMessageInfoImport> getErrMessageInfoByExecutionContent(String empCalAndSumExecLogID, int executionContent);

	/** Add an ErrMessageInfo */
	void add(ErrMessageInfoImport errMessageInfo);

	/**
	 * For KIF 001
	 * 
	 * @param errMessageInfos
	 */
	void addList(List<ErrMessageInfoImport> errMessageInfos);
}
