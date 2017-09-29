/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.app.command.workplace.config;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.dom.workplace.config.WorkplaceConfig;
import nts.uk.ctx.bs.employee.dom.workplace.config.WorkplaceConfigHistory;
import nts.uk.ctx.bs.employee.dom.workplace.config.WorkplaceConfigRepository;
import nts.uk.ctx.bs.employee.dom.workplace.config.info.service.WkpConfigInfoService;
import nts.uk.ctx.bs.employee.dom.workplace.config.service.WkpConfigService;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class SaveWorkplaceConfigCommandHandler.
 */
@Stateless
@Transactional
public class SaveWkpConfigCommandHandler extends CommandHandler<SaveWkpConfigCommand> {

    /** The workplace config repository. */
    @Inject
    private WorkplaceConfigRepository workplaceConfigRepository;

    /** The wkp config info service. */
    @Inject
    private WkpConfigInfoService wkpConfigInfoService;

    /** The wkp config service. */
    @Inject
    private WkpConfigService wkpConfigService;

    /*
     * (non-Javadoc)
     * 
     * @see
     * nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command
     * .CommandHandlerContext)
     */
    @Override
    protected void handle(CommandHandlerContext<SaveWkpConfigCommand> context) {
        SaveWkpConfigCommand command = context.getCommand();
        String companyId = AppContexts.user().companyId();
        
        // workplace config history is sorted mode DESC by start date
        Optional<WorkplaceConfig> optional = workplaceConfigRepository.findWorkplaceByCompanyId(companyId);
        // new mode
        if (!optional.isPresent()) {
            workplaceConfigRepository.add(command.toDomain(companyId));
            return;
        }
        // add mode
        if (command.getIsAddMode()) {
            this.addHistory(companyId, command, optional.get());
        } else {
            this.updateHistory(companyId, command, optional.get());
        }
        
    }

    /**
     * Adds the history.
     *
     * @param companyId the company id
     * @param command the command
     * @param latestWkpConfigHist the latest wkp config hist
     */
    private void addHistory(String companyId, SaveWkpConfigCommand command, WorkplaceConfig wkpConfig) {
        // get start date of add new hist
        GeneralDate newStartDateHist = command.getWkpConfigHistory().getPeriod().getStartDate();
        WorkplaceConfigHistory latestWkpConfigHist = wkpConfig.getWkpConfigHistoryLatest();
        // validate add hist and return first histId
        if (this.isInValidStartDateHistory(latestWkpConfigHist.getPeriod().getStartDate(),
                newStartDateHist)) {
            throw new BusinessException("Msg_102");
        }
        String latestHistIdCurrent = latestWkpConfigHist.getHistoryId();
        
        // convert command to domain
        WorkplaceConfig workplaceConfig = command.toDomain(companyId);
        
        // add workplace config and return add new historyId
        workplaceConfigRepository.add(workplaceConfig);
        
        // previous day
        int dayOfAgo = -1;
        // update previous history
        wkpConfigService.updatePrevHistory(companyId, latestHistIdCurrent, newStartDateHist.addDays(dayOfAgo));
        
        String newHistoryId = workplaceConfig.getWkpConfigHistoryLatest().getHistoryId();
        // copy latest ConfigInfoHist from fisrtHistId
        wkpConfigInfoService.copyWkpConfigInfoHist(companyId, latestHistIdCurrent, newHistoryId);
    }
    
    /**
     * Update history.
     *
     * @param companyId the company id
     * @param command the command
     */
    private void updateHistory(String companyId, SaveWkpConfigCommand command, WorkplaceConfig wkpConfigDatabase) {
        // get start date of add new hist
        GeneralDate newStartDateHist = command.getWkpConfigHistory().getPeriod().getStartDate();
        
        // get previous history (below history latest)
        int idxPrevLatestHist = 1;
        WorkplaceConfigHistory prevHistLatest = wkpConfigDatabase.getWkpConfigHistory().get(idxPrevLatestHist);
        
        // validate new start date with previous of latest history.
        if (this.isInValidStartDateHistory(prevHistLatest.getPeriod().getStartDate(), newStartDateHist)) {
            throw new BusinessException("Msg_127");
        }
        // update workplace by new start date history if need
        this.wkpConfigService.updateWkpHistoryIfNeed(companyId, wkpConfigDatabase.getWkpConfigHistoryLatest(), newStartDateHist);
        
        // convert to domain
        WorkplaceConfig wkpConfigCommand = command.toDomain(companyId);
        
        // set end date of previous history (below of history latest)
        int dayOfAgo = -1;
        prevHistLatest.getPeriod().setEndDate(newStartDateHist.addDays(dayOfAgo));
        
        // update previous history latest
        wkpConfigCommand.getWkpConfigHistory().add(prevHistLatest);
        
        // update history
        this.workplaceConfigRepository.update(wkpConfigCommand);
    }

    /**
     * Checks if is in valid start date history.
     *
     * @param beforeDate the before date
     * @param afterDate the after date
     * @return true, if is in valid start date history
     */
    private boolean isInValidStartDateHistory(GeneralDate beforeDate, GeneralDate afterDate) {
        return beforeDate.afterOrEquals(afterDate);
    }
}
