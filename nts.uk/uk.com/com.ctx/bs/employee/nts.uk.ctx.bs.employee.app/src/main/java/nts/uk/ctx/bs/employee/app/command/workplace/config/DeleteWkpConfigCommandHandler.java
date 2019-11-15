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

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.bs.employee.app.command.workplace.config.service.WkpConfigService;
import nts.uk.ctx.bs.employee.dom.workplace.WorkplaceRepository;
import nts.uk.ctx.bs.employee.dom.workplace.config.WorkplaceConfig;
import nts.uk.ctx.bs.employee.dom.workplace.config.WorkplaceConfigRepository;
import nts.uk.ctx.bs.employee.dom.workplace.config.info.WorkplaceConfigInfo;
import nts.uk.ctx.bs.employee.dom.workplace.config.info.WorkplaceConfigInfoRepository;
import nts.uk.ctx.bs.employee.dom.workplace.policy.HistoryPolicy;
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
    
    /** The wkp service. */
//    @Inject
//    private WorkplaceService wkpService;
    
    /** The wkp config service. */
    @Inject
    private WkpConfigService wkpConfigService;
    
    @Inject
    private WorkplaceRepository repoWkp;
    
    /* (non-Javadoc)
     * @see nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command.CommandHandlerContext)
     */
    @Override
    protected void handle(CommandHandlerContext<DeleteWkpConfigCommand> context) {
        String companyId = AppContexts.user().companyId();
        
        // find all workplace configure by companyId
        Optional<WorkplaceConfig> optionalWkpConfig = wkpConfigRepo.findAllByCompanyId(companyId);
        if (!optionalWkpConfig.isPresent()) {
            throw new RuntimeException("Didn't exist workplce configure.");
        }
        WorkplaceConfig wkpConfig = optionalWkpConfig.get();
        
        DeleteWkpConfigCommand command = context.getCommand();
        
        // valid history latest
        List<String> lstHistoryId = wkpConfig.items().stream()
                .map(item -> item.identifier())
                .collect(Collectors.toList());
        HistoryPolicy.validHistoryLatest(lstHistoryId, command.getHistoryId());
        
        // remove workplace config history
        this.wkpConfigRepo.removeWkpConfigHist(companyId, command.getHistoryId());
        
        // update end date of previous history (below history that is removed)
        int idxPrevHistLatest = 1;
        String prevHistIdLatest = wkpConfig.items().get(idxPrevHistLatest).identifier();
        this.wkpConfigService.updatePrevHistory(companyId, prevHistIdLatest, HistoryPolicy.getMaxDate());
        
        // find all workplace of history that is removed
        Optional<WorkplaceConfigInfo> optionalWkpConfigInfo = this.wkpConfigInfoRepo.find(companyId,
                command.getHistoryId());
        if (!optionalWkpConfigInfo.isPresent()) {
            return;
        }
        List<String> lstWkpId = optionalWkpConfigInfo.get().getLstWkpHierarchy().stream()
                .map(item -> item.getWorkplaceId())
                .collect(Collectors.toList());
        
        // remove workplace of history
        lstWkpId.forEach((wkpId) -> {
        	repoWkp.deleteWorkplacesBySDate(companyId, wkpId, wkpConfig.getWkpConfigHistoryLatest().span().start());
        });
    }

}
