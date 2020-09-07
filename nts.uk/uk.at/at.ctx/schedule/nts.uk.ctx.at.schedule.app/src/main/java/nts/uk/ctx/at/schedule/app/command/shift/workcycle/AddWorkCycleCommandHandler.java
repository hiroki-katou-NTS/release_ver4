package nts.uk.ctx.at.schedule.app.command.shift.workcycle;

import lombok.AllArgsConstructor;
import lombok.val;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.arc.task.tran.AtomTask;
import nts.uk.ctx.at.schedule.app.command.shift.workcycle.command.AddWorkCycleCommand;
import nts.uk.ctx.at.schedule.dom.shift.workcycle.WorkCycle;
import nts.uk.ctx.at.schedule.dom.shift.workcycle.WorkCycleRepository;
import nts.uk.ctx.at.schedule.dom.shift.workcycle.domainservice.RegisterWorkCycleService;
import nts.uk.ctx.at.schedule.dom.shift.workcycle.domainservice.WorkCycleCreateResult;
import nts.uk.ctx.at.shared.dom.WorkInformation;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.BasicScheduleService;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.SetupType;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.WorkStyle;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingService;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.internal.PredetermineTimeSetForCalc;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.shr.com.context.AppContexts;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Optional;

/**
 * 勤務サイクルを新規登録する
 */
@Stateless
public class AddWorkCycleCommandHandler extends CommandHandlerWithResult<AddWorkCycleCommand, WorkCycleCreateResult> {

    @Inject
    private WorkCycleRepository workCycleRepository;

    @Inject
    private BasicScheduleService basicScheduleService;

    @Inject
    private WorkTypeRepository workTypeRepo;

    @Inject
    private WorkTimeSettingRepository workTimeSettingRepository;

    @Inject
    private WorkTimeSettingService workTimeSettingService;

    @Override
    protected WorkCycleCreateResult handle(CommandHandlerContext<AddWorkCycleCommand> context) {

        val command = context.getCommand();
        String cid = AppContexts.user().companyId();
        RegisterWorkCycleServiceImlp require = new RegisterWorkCycleServiceImlp(workCycleRepository,basicScheduleService, workTypeRepo,workTimeSettingRepository,workTimeSettingService, basicScheduleService);
        WorkCycleCreateResult result = RegisterWorkCycleService.register(require, AddWorkCycleCommand.createFromCommand(command, cid), true);
        if (!result.isHasError()) {
            AtomTask atomTask = result.getAtomTask().get();
            transaction.execute(() ->{
                atomTask.run();
            });
        }
        return result;
    }

    @AllArgsConstructor
    private class RegisterWorkCycleServiceImlp implements RegisterWorkCycleService.Require {
        private final String companyId = AppContexts.user().companyId();

        WorkCycleRepository workCycleRepository;

        private BasicScheduleService service;

        private WorkTypeRepository workTypeRepo;

        private WorkTimeSettingRepository workTimeSettingRepository;

        private WorkTimeSettingService workTimeSettingService;

        private BasicScheduleService basicScheduleService;

        @Override
        public SetupType checkNeededOfWorkTimeSetting(String workTypeCode) {
            return service.checkNeededOfWorkTimeSetting(workTypeCode);
        }

        @Override
        public Optional<WorkType> findByPK(String workTypeCd) {
            return workTypeRepo.findByPK(companyId, workTypeCd);
        }

        @Override
        public Optional<WorkTimeSetting> findByCode(String workTimeCode) {
            return workTimeSettingRepository.findByCode(companyId, workTimeCode);
        }

        @Override
        public PredetermineTimeSetForCalc getPredeterminedTimezone(String workTimeCd,
                                                                   String workTypeCd, Integer workNo) {
            return workTimeSettingService .getPredeterminedTimezone(companyId, workTimeCd, workTypeCd, workNo);
        }

        @Override
        public WorkStyle checkWorkDay(String workTypeCode) {
            return basicScheduleService.checkWorkDay(workTypeCode);
        }

        @Override
        public boolean exists(String cid, String code) {
            return workCycleRepository.exists(cid, code);
        }

        @Override
        public void insert(WorkCycle item) {
            this.workCycleRepository.add(item);
        }

        @Override
        public void update(WorkCycle item) {
            this.workCycleRepository.update(item);
        }

    }

}
