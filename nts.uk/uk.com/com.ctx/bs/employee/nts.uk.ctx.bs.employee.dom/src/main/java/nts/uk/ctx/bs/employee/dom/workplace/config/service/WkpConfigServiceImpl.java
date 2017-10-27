/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.dom.workplace.config.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.dom.workplace.Workplace;
import nts.uk.ctx.bs.employee.dom.workplace.WorkplaceHistory;
import nts.uk.ctx.bs.employee.dom.workplace.WorkplaceRepository;
import nts.uk.ctx.bs.employee.dom.workplace.config.WorkplaceConfig;
import nts.uk.ctx.bs.employee.dom.workplace.config.WorkplaceConfigHistory;
import nts.uk.ctx.bs.employee.dom.workplace.config.WorkplaceConfigRepository;
import nts.uk.ctx.bs.employee.dom.workplace.config.info.WorkplaceConfigInfo;
import nts.uk.ctx.bs.employee.dom.workplace.config.info.WorkplaceConfigInfoRepository;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * The Class WkpConfigServiceImpl.
 */
@Stateless
public class WkpConfigServiceImpl implements WkpConfigService {

    /** The workplace config repository. */
    @Inject
    private WorkplaceConfigRepository workplaceConfigRepository;

    /** The wkp config info repo. */
    @Inject
    private WorkplaceConfigInfoRepository wkpConfigInfoRepo;

    /** The workplace repo. */
    @Inject
    private WorkplaceRepository workplaceRepo;

    /** The Constant ELEMENT_FIRST. */
    private static final Integer ELEMENT_FIRST = 0;
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * nts.uk.ctx.bs.employee.dom.workplace.config.service.WkpConfigService#
     * updatePrevHistory(java.lang.String, nts.arc.time.GeneralDate)
     */
    @Override
    public void updatePrevHistory(String companyId, String prevHistId, GeneralDate endĐate) {
        Optional<WorkplaceConfig> optional = workplaceConfigRepository.findByHistId(companyId, prevHistId);
        if (!optional.isPresent()) {
            throw new RuntimeException(String.format("History id %s didn't existed.", prevHistId));
        }
        WorkplaceConfig wkpConfig = optional.get();
        // set end date of previous history
        DatePeriod period = wkpConfig.getWkpConfigHistory().get(ELEMENT_FIRST).getPeriod();
        wkpConfig.getWkpConfigHistory().get(ELEMENT_FIRST).setPeriod(period.newSpan(period.start(), endĐate));
        workplaceConfigRepository.update(wkpConfig);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * nts.uk.ctx.bs.employee.dom.workplace.config.service.WkpConfigService#
     * updateWkpHistoryIfNeed(java.lang.String,
     * nts.uk.ctx.bs.employee.dom.workplace.config.WorkplaceConfigHistory,
     * nts.arc.time.GeneralDate)
     */
    @Override
    public void updateWkpHistoryIfNeed(String companyId, WorkplaceConfigHistory latestWkpConfigHist,
            GeneralDate newHistStartDate) {
        String historyId = latestWkpConfigHist.getHistoryId();
        Optional<WorkplaceConfigInfo> optionalWkpConfigInfo = this.wkpConfigInfoRepo.find(companyId, historyId);
        if (!optionalWkpConfigInfo.isPresent()) {
            return;
        }
        // find all workplace by historyId
        List<String> lstWkpId = optionalWkpConfigInfo.get().getLstWkpHierarchy().stream()
                .map(item -> item.getWorkplaceId()).collect(Collectors.toList());

        // check date of workplace
        List<Workplace> lstWorkplace = this.workplaceRepo.findByWkpIds(lstWkpId);
        
        lstWorkplace.forEach(workplace -> {
            this.processRemoveWKp(workplace, latestWkpConfigHist.getPeriod().start(), newHistStartDate);
        });
    }

    /**
     * Process remove W kp.
     *
     * @param workplace the workplace
     * @param startDWkpConfigHistCurrent the start D wkp config hist current
     * @param newStartDWkpConfigHist the new start D wkp config hist
     */
	private void processRemoveWKp(Workplace workplace, GeneralDate startDWkpConfigHistCurrent,
			GeneralDate newStartDWkpConfigHist) {
		final List<String> lstHistIdRemove = new ArrayList<>();
		Iterator<WorkplaceHistory> itWkpHistory = workplace.getWorkplaceHistory().iterator();
		Boolean isChangedEndDate = false;

		while (itWkpHistory.hasNext()) {
			WorkplaceHistory wkpHistoryCurrent = itWkpHistory.next();
			GeneralDate startDateWkpHist = wkpHistoryCurrent.getPeriod().start();
			
			// not equal
			if (!startDateWkpHist.equals(startDWkpConfigHistCurrent)) {
				
				// update start date, end date of previous
				if (isChangedEndDate && !lstHistIdRemove.contains(wkpHistoryCurrent.getHistoryId())) {
					DatePeriod period = wkpHistoryCurrent.getPeriod();
					int dayOfAgo = -1;
					wkpHistoryCurrent.setPeriod(period.newSpan(period.start(),
							newStartDWkpConfigHist.addDays(dayOfAgo)));
					break;
				}
				continue;
			}
			// find date range need to remove
			lstHistIdRemove.addAll(this.findExistedDateInRange(workplace.getWorkplaceHistory(), newStartDWkpConfigHist,
					startDateWkpHist));
			
			// remove workplace infor in date range
			this.removeWkpHistory(workplace, lstHistIdRemove);
			
			// update start date
			DatePeriod period = wkpHistoryCurrent.getPeriod();
			wkpHistoryCurrent.setPeriod(period.newSpan(newStartDWkpConfigHist, period.end()));

			// set flag changed end date of previous workplace history
			isChangedEndDate = true;
		}
		
		// remove workplace history
     	workplace.getWorkplaceHistory().removeIf(item -> lstHistIdRemove.contains(item.getHistoryId()));
		
		// update workplace
		this.workplaceRepo.update(workplace);
    }

    /**
     * Find existed date in range.
     *
     * @param lstWkpHistory
     *            the lst wkp history
     * @param startDate
     *            the start date
     * @param endDate
     *            the end date
     * @return the list
     */
    private List<String> findExistedDateInRange(List<WorkplaceHistory> lstWkpHistory, GeneralDate startDate,
            GeneralDate endDate) {
        return lstWkpHistory.stream()
                .filter(item -> item.getPeriod().start().after(startDate)
                        && item.getPeriod().start().before(endDate))
                .map(item -> item.getHistoryId())
                .collect(Collectors.toList());
    }

    /**
     * Removes the workplace infor.
     *
     * @param companyId
     *            the company id
     * @param workplaceId
     *            the workplace id
     * @param lstHistoryIdRemove
     *            the lst history id remove
     */
    private void removeWkpHistory(Workplace workplace, List<String> lstHistoryIdRemove) {
        lstHistoryIdRemove.forEach(historyId -> {
            // remove workplace history and workplace infor
            this.workplaceRepo.removeWkpHistory(workplace.getCompanyId(), workplace.getWorkplaceId(), historyId);
        });
    }
}
