/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.base.simplehistory;

import java.util.List;
import java.util.Optional;

import nts.arc.error.BusinessException;
import nts.arc.time.DateTimeConstraints;
import nts.arc.time.YearMonth;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class SimpleHistoryBaseService.
 */
public abstract class SimpleHistoryBaseService<M extends Master, H extends History<H>> {

	/**
	 * Delete history.
	 *
	 * @param uuid the uuid
	 */
	public void deleteHistory(String uuid) {
		SimpleHistoryRepository<H> repo = this.getRepository();
		Optional<H> hisOpt = repo.findHistoryByUuid(uuid);

		if (!hisOpt.isPresent()) {
			throw new RuntimeException("History not found.");

		}

		// Remove history.
		H history = hisOpt.get();

		// Check if current history is last.
		H lastHistory = repo.findLastestHistoryByMasterCode(AppContexts.user().companyCode(),
				history.getMasterCode().v()).get();

		// Latest history.
		if (!history.getUuid().equals(lastHistory.getUuid())) {
			throw new IllegalAccessError("Can not remove not latest history.");
		}

		// Delete current history.
		this.getRepository().deleteHistory(history.getUuid());

		// Update latest history.
		hisOpt = this.getRepository().findLastestHistoryByMasterCode(
				history.getCompanyCode(),
				history.getMasterCode().v());
		
		if (hisOpt.isPresent()) {
			history = hisOpt.get();
			history.setEnd(YearMonth.of(DateTimeConstraints.LIMIT_YEAR.max(), DateTimeConstraints.LIMIT_MONTH.max()));
			this.getRepository().updateHistory(history);
		}
	}

	/**
	 * For extended domain processing.
	 * @param uuid
	 */
	protected void onDeleteHistory(String uuid) {
		// Ret.
		return;
	}

	/**
	 * Copy history.
	 *
	 * @param copiedHistoryId the copied history id
	 * @param startYear the start year
	 */
	public H copyFromLasterHistory(String companyCode, String masterCode, YearMonth startYear) {
		Optional<H> historyOpt = this.getRepository().findLastestHistoryByMasterCode(companyCode, masterCode);
		if (!historyOpt.isPresent()) {
			throw new RuntimeException("History not found.");
		}
		H lastestHistory = historyOpt.get();

		// Validate start year.
		if (startYear.v() <= lastestHistory.getStart().v()) {
			throw new BusinessException("ER010");
		}

		// New history.
		H newHistory = lastestHistory.copyWithDate(startYear);
		this.getRepository().addHistory(newHistory);

		this.onCopyHistory(companyCode, masterCode, lastestHistory, newHistory);
		
		// Update old history.
		lastestHistory.setEnd(startYear.previousMonth());
		this.getRepository().updateHistory(lastestHistory);

		// Ret.
		return newHistory;
	}

	/**
	 * On copy history.
	 * @param companyCode
	 * @param masterCode
	 * @param copiedHistory
	 */
	protected void onCopyHistory(String companyCode, String masterCode, H copiedHistory, H newHistory) {
		// Just for override.
		return;
	}

	/**
	 * Create history.
	 *
	 * @param copiedHistoryId the copied history id
	 * @param startYear the start year
	 */
	public H createHistory(String companyCode, String masterCode, YearMonth startYear) {
		Optional<H> historyOpt = this.getRepository().findLastestHistoryByMasterCode(companyCode, masterCode);

		// New history.
		H newHistory = this.createInitalHistory(companyCode, masterCode, startYear);
		this.getRepository().addHistory(newHistory);

		// Update old history.
		if (historyOpt.isPresent()) {
			H lastestHistory = historyOpt.get();
			
			// Validate start year.
			if (startYear.v() <= lastestHistory.getStart().v()) {
				throw new BusinessException("ER010");
			}

			// Update latest history.
			lastestHistory.setEnd(startYear.previousMonth());
			this.getRepository().updateHistory(lastestHistory);
		}

		// Fire on create history.
		this.onCreateHistory(companyCode, masterCode, newHistory);

		// Ret.
		return newHistory;
	}

	/**
	 * On copy history.
	 * @param companyCode
	 * @param masterCode
	 * @param copiedHistory
	 */
	protected void onCreateHistory(String companyCode, String masterCode, H newHistory) {
		// Just for override.
		return;
	}

	/**
	 * Update history start.
	 *
	 * @param companyCode the company code
	 * @param masterCode the master code
	 * @param uuid the uuid
	 * @param newYearMonth the new year month
	 * @return the h
	 */
	public H updateHistoryStart(String uuid, YearMonth newYearMonth) {
		SimpleHistoryRepository<H> repo = this.getRepository();

		// Get updated uuid.
		Optional<H> optH = repo.findHistoryByUuid(uuid);
		
		// Check exist
		if (!optH.isPresent()) {
			throw new BusinessException("ER026");
		}

		H h = optH.get();
		List<H> historyList = repo.findAllHistoryByMasterCode(h.getCompanyCode(), h.getMasterCode().v());
		int indexOfH = historyList.indexOf(h);
		H afterH = indexOfH > 0 ? historyList.get(indexOfH -1) : null;
		H beforeH = indexOfH < (historyList.size() -1) ? historyList.get(indexOfH + 1) : null;

		// Validate new yearmonth.
		if (beforeH != null && newYearMonth.v() <= beforeH.getStart().v()) {
			// Error.
			throw new BusinessException("ER023");
		}

		if (afterH != null && newYearMonth.v() >= afterH.getStart().v()) {
			// Error.
			throw new BusinessException("ER023");
		}

		// Update h.
		h.setStart(newYearMonth);
		repo.updateHistory(h);

		// Update before h.
		if (beforeH != null) {
			beforeH.setEnd(newYearMonth.previousMonth());
			repo.updateHistory(beforeH);
		}
		
		// Ret.
		return h;
	}

	/**
	 * Gets the repository.
	 *
	 * @return the repository
	 */
	public abstract SimpleHistoryRepository<H> getRepository();

	/**
	 * Creates the inital history.
	 *
	 * @param masterCode the master code
	 * @return the m
	 */
	public abstract H createInitalHistory(String companyCode, String masterCode, YearMonth startTime);
}
