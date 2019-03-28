package nts.uk.ctx.at.shared.dom.worktype.service;

public interface JudgmentOneDayHoliday {
	/**
	 * 1日休日の判定
	 * @param companyID
	 * @param workTypeCD
	 * @return
	 */
	public boolean judgmentOneDayHoliday(String companyID, String workTypeCD);
	
}
