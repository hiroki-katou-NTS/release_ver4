package nts.uk.screen.at.app.kmk.kmk017.worktimeworkplace;

import lombok.AllArgsConstructor;
import nts.uk.ctx.at.shared.dom.worktime.workplace.WorkTimeWorkplace;
import nts.uk.ctx.at.shared.dom.worktime.workplace.WorkTimeWorkplaceRepository;
import nts.uk.ctx.at.shared.dom.worktime.workplace.service.WorkTimeWorkplaceService;
import nts.uk.shr.com.context.AppContexts;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 職場リストを表示する
 */
@Stateless
public class WorkTimeWorkplaceProcessor {

    @Inject
    private WorkTimeWorkplaceService workTimeWorkplaceService;

    @Inject
    private WorkTimeWorkplaceRepository repository;

    public List<WorkTimeWorkplaceDto> findWorkTimeWorkplace() {

        RequireImpl require = new RequireImpl(repository);
        List<WorkTimeWorkplace> workTimeWorkplaces = workTimeWorkplaceService.getByCid(require);
        return workTimeWorkplaces.stream().map(WorkTimeWorkplaceDto::setData).collect(Collectors.toList());
    }

    @AllArgsConstructor
    private class RequireImpl implements WorkTimeWorkplaceService.Require {
        private WorkTimeWorkplaceRepository repository;

        @Override
        public List<WorkTimeWorkplace> getByCId() {
            return repository.getByCId(AppContexts.user().companyId());
        }
    }
}
