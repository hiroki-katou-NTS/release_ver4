/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.app.vacation.setting.nursingleave.command;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.shared.app.vacation.setting.nursingleave.command.dto.NursingLeaveSettingDto;
import nts.uk.ctx.at.shared.dom.vacation.setting.ManageDistinct;
import nts.uk.ctx.at.shared.dom.vacation.setting.nursingleave.NursingLeaveSetting;
import nts.uk.ctx.at.shared.dom.vacation.setting.nursingleave.NursingLeaveSettingRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class NursingLeaveCommandHandler.
 */
@Stateless
public class NursingLeaveCommandHandler extends CommandHandler<NursingLeaveCommand> {
    
    /** The Constant INDEX_NURSING. */
    private static final int INDEX_NURSING = 0;
    
    /** The Constant INDEX_CHILD_NURSING. */
    private static final int INDEX_CHILD_NURSING = 1;
    
    /** The nursing leave repo. */
    @Inject
    private NursingLeaveSettingRepository nursingLeaveRepo;
    
    /* (non-Javadoc)
     * @see nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command.CommandHandlerContext)
     */
    @Override
    protected void handle(CommandHandlerContext<NursingLeaveCommand> context) {
        String companyId = AppContexts.user().companyId();
        NursingLeaveCommand command = context.getCommand();
        List<NursingLeaveSetting> result = this.nursingLeaveRepo.findByCompanyId(companyId);
        
        // Check fields enable/disable.
        this.validateField(command, result);
        
        NursingLeaveSetting nursingSetting = command.getNursingSetting().toDomain(companyId);
        NursingLeaveSetting childNursingSetting = command.getChildNursingSetting().toDomain(companyId);
        if (CollectionUtil.isEmpty(result)) {
            this.nursingLeaveRepo.add(nursingSetting, childNursingSetting);
        } else {
            this.nursingLeaveRepo.update(nursingSetting, childNursingSetting);
        }
    }

    /**
     * Validate field.
     *
     * @param command the command
     * @param result the result
     */
    private void validateField(NursingLeaveCommand command, List<NursingLeaveSetting> result) {
        boolean isNoSetting = CollectionUtil.isEmpty(result);
        this.checkField(command.getNursingSetting(), isNoSetting ? null : result.get(INDEX_NURSING));
        this.checkField(command.getChildNursingSetting(), isNoSetting ? null : result.get(INDEX_CHILD_NURSING));
    }
    
    /**
     * Check field.
     *
     * @param command the command
     * @param settingDb the setting db
     */
    private void checkField(NursingLeaveSettingDto command, NursingLeaveSetting settingDb) {
        // Manage
        if (command.getManageType() == ManageDistinct.YES.value) {
            return;
        }
        // No setting existed
        if (settingDb == null) {
            this.initValue(command);
            return;
        }
        command.setStartMonthDay(settingDb.getStartMonthDay());
        command.setNursingNumberLeaveDay(settingDb.getMaxPersonSetting().getNursingNumberLeaveDay().v());
        command.setNursingNumberPerson(settingDb.getMaxPersonSetting().getNursingNumberPerson().v());
    }
    
    /**
     * Inits the value.
     *
     * @param setting the setting
     */
    private void initValue(NursingLeaveSettingDto setting) {
        setting.setStartMonthDay(null);
        setting.setNursingNumberLeaveDay(null);
        setting.setNursingNumberPerson(null);
        setting.setWorkTypeCodes(new ArrayList<>());
    }
}
