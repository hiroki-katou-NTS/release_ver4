package nts.uk.ctx.at.record.dom.divergencetime.service;

public interface DivergenceReasonService {
	/**
	 * check data is exit with companyId, divTimeId
	 * @param selectUseSet
	 * @param divTimeId
	 * @return
	 * true: is data
	 * false: no data
	 */
	boolean isExit(int selectUseSet, int divTimeId);
}
