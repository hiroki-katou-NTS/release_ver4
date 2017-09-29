/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.dom.workplace.config.service.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.bs.employee.dom.workplace.Workplace;
import nts.uk.ctx.bs.employee.dom.workplace.WorkplaceHistory;
import nts.uk.ctx.bs.employee.dom.workplace.WorkplaceRepository;
import nts.uk.ctx.bs.employee.dom.workplace.config.WorkplaceConfig;
import nts.uk.ctx.bs.employee.dom.workplace.config.WorkplaceConfigHistory;
import nts.uk.ctx.bs.employee.dom.workplace.config.WorkplaceConfigRepository;
import nts.uk.ctx.bs.employee.dom.workplace.config.info.WorkplaceConfigInfo;
import nts.uk.ctx.bs.employee.dom.workplace.config.info.WorkplaceConfigInfoRepository;
import nts.uk.ctx.bs.employee.dom.workplace.config.service.WkpConfigService;
import nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfoRepository;

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

    /** The workplace info repo. */
    @Inject
    private WorkplaceInfoRepository workplaceInfoRepo;

    /*
     * (non-Javadoc)
     * 
     * @see
     * nts.uk.ctx.bs.employee.dom.workplace.config.service.WkpConfigService#
     * updatePrevHistory(java.lang.String, nts.arc.time.GeneralDate)
     */
    @Override
    public void updatePrevHistory(String companyId, String prevHistId, GeneralDate endĐate) {
        Optional<WorkplaceConfig> wkpConfig = workplaceConfigRepository.findByHistId(companyId, prevHistId);
        if (wkpConfig.isPresent()) {
            workplaceConfigRepository.update(wkpConfig.get(), endĐate);
        }
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
            throw new RuntimeException("Not existed history id: " + historyId);
        }
        // find all workplace by historyId
        List<String> lstWkpId = optionalWkpConfigInfo.get().getWkpHierarchy().stream()
                .map(item -> item.getWorkplaceId().v()).collect(Collectors.toList());

        // check date of workplace
        List<Workplace> lstWorkplace = this.workplaceRepo.findByWkpIds(lstWkpId);
        for (Workplace workplace : lstWorkplace) {
            this.progress(workplace, latestWkpConfigHist.getPeriod().getStartDate(), newHistStartDate);
        }
    }

    /**
     * Progress.
     *
     * @param workplace
     *            the workplace
     * @param startDateHistCurrent
     *            the start date hist current
     * @param newStartDateHist
     *            the new start date hist
     */
    private void progress(Workplace workplace, GeneralDate startDateHistCurrent, GeneralDate newStartDateHist) {
        List<WorkplaceHistory> lstNewWkpHistory = new ArrayList<>();
        for (WorkplaceHistory wkpHistory : workplace.getWorkplaceHistory()) {
            GeneralDate startDateWkpHist = wkpHistory.getPeriod().getStartDate();
            // TODO: case newStartDateHist > startDateWkpHist ???
            // not equal
            if (!startDateWkpHist.equals(startDateHistCurrent)) {
                continue;
            }
            // find date range need to remove
            List<String> lstHistoryIdRemove = this.findExistedDateInRange(workplace.getWorkplaceHistory(),
                    newStartDateHist, startDateWkpHist);
            if (!CollectionUtil.isEmpty(lstHistoryIdRemove)) {
                // remove workplace infor in date range
                this.removeWorkplaceInfor(workplace.getCompanyId(), workplace.getWorkplaceId().v(), lstHistoryIdRemove);
            }
            startDateWkpHist = newStartDateHist;

            lstNewWkpHistory = this.subListWkpHistory(workplace.getWorkplaceHistory(), newStartDateHist);
            lstNewWkpHistory.add(wkpHistory);
            break;
        }
        // empty list workplace history
        workplace.getWorkplaceHistory().clear();

        // add all new list workplace history
        workplace.getWorkplaceHistory().addAll(lstNewWkpHistory);

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
                .filter(item -> item.getPeriod().getStartDate().afterOrEquals(startDate)
                        && item.getPeriod().getStartDate().beforeOrEquals(endDate))
                .map(item -> item.getHistoryId().v())
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
    private void removeWorkplaceInfor(String companyId, String workplaceId, List<String> lstHistoryIdRemove) {
        lstHistoryIdRemove.forEach(historyId -> {
            this.workplaceInfoRepo.remove(companyId, workplaceId, historyId);
        });
    }

    /**
     * Sub list wkp history.
     *
     * @param lstWorkplaceHistory
     *            the lst workplace history
     * @param newStartDateHist
     *            the new start date hist
     * @return the list
     */
    private List<WorkplaceHistory> subListWkpHistory(List<WorkplaceHistory> lstWorkplaceHistory,
            GeneralDate newStartDateHist) {
        List<WorkplaceHistory> subListWkpHistory = lstWorkplaceHistory.stream()
                .filter(item -> item.getPeriod().getStartDate().before(newStartDateHist)).collect(Collectors.toList());

        if (CollectionUtil.isEmpty(subListWkpHistory)) {
            return new ArrayList<>();
        }
        // set previous a day for end date
        subListWkpHistory.get(0).getPeriod().setEndDate(newStartDateHist.addDays(-1));

        return subListWkpHistory;
    }
}
