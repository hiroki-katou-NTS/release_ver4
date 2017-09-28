/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.app.command.workplace.config;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.dom.workplace.WorkplaceRepository;
import nts.uk.ctx.bs.employee.dom.workplace.config.WorkplaceConfig;
import nts.uk.ctx.bs.employee.dom.workplace.config.WorkplaceConfigRepository;
import nts.uk.ctx.bs.employee.dom.workplace.config.info.WorkplaceConfigInfo;
import nts.uk.ctx.bs.employee.dom.workplace.config.info.WorkplaceConfigInfoRepository;
import nts.uk.ctx.bs.employee.dom.workplace.config.service.WkpConfigService;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class DeleteWkpConfigCommandHandler.
 */
@Stateless
@Transactional
public class DeleteWkpConfigCommandHandler extends CommandHandler<DeleteWkpConfigCommand> {

    /** The wkp config repo. */
    @Inject
    private WorkplaceConfigRepository wkpConfigRepo;
    
    /** The wkp config info repo. */
    @Inject
    private WorkplaceConfigInfoRepository wkpConfigInfoRepo;
    
    /** The workplace repo. */
    @Inject
    private WorkplaceRepository workplaceRepo;
    
    /** The wkp config service. */
    @Inject
    private WkpConfigService wkpConfigService;
    
    /** The Constant NUMBER_ELEMENT_MIN. */
    private static final Integer NUMBER_ELEMENT_MIN = 1;
    
    /** The Constant DATE_FORMAT. */
    private static final String DATE_FORMAT = "yyyy/MM/dd";
    
    /** The Constant MAX_DATE. */
    private static final String MAX_DATE = "9999/12/31";
    
    /* (non-Javadoc)
     * @see nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command.CommandHandlerContext)
     */
    @Override
    protected void handle(CommandHandlerContext<DeleteWkpConfigCommand> context) {
        String companyId = AppContexts.user().companyId();
        
        // find all workplace configure by companyId
        Optional<WorkplaceConfig> optionalWkpConfig = wkpConfigRepo.findWorkplaceByCompanyId(companyId);
        if (!optionalWkpConfig.isPresent()) {
            throw new RuntimeException("Didn't exist workplce configure.");
        }
        WorkplaceConfig wkpConfig = optionalWkpConfig.get();
        
        // Don't remove when only has 1 history
        if (wkpConfig.getWkpConfigHistory().size() == NUMBER_ELEMENT_MIN) {
            throw new BusinessException("Msg_57");
        }
        DeleteWkpConfigCommand command = context.getCommand();
        
        // check history remove latest ?
        if (!command.getHistoryId().equals(wkpConfig.getWkpConfigHistoryLatest().getHistoryId())) {
            throw new BusinessException("Msg_55");
        }
        
        // update end date of previous history (below history that is removed)
        int idxprevHistLatest = 1;
        String prevHistIdLatest = wkpConfig.getWkpConfigHistory().get(idxprevHistLatest).getHistoryId();
        this.wkpConfigService.updatePrevHistory(companyId, prevHistIdLatest, GeneralDate.fromString(MAX_DATE, DATE_FORMAT));
        
        // remove workplace config history
        this.wkpConfigRepo.removeWkpConfigHist(companyId, command.getHistoryId());
        
        // find all workplace of history that is removed
        Optional<WorkplaceConfigInfo> optionalWkpConfigInfo = this.wkpConfigInfoRepo.find(companyId,
                command.getHistoryId());
        if (!optionalWkpConfigInfo.isPresent()) {
            return;
        }
        List<String> lstWkpId = optionalWkpConfigInfo.get().getWkpHierarchy().stream()
                .map(item -> item.getWorkplaceId().v())
                .collect(Collectors.toList());
        
        // remove workplace of history
        lstWkpId.forEach((wkpId) -> {
            this.workplaceRepo.removeByWkpId(companyId, wkpId);
        });
    }

}
