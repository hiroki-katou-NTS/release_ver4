package nts.uk.ctx.at.schedule.app.find.budget.schedulevertical.personal;

import nts.arc.enums.EnumAdaptor;
import nts.arc.enums.EnumConstant;
import nts.uk.ctx.at.schedule.dom.shift.management.schedulecounter.PersonalCounter;
import nts.uk.ctx.at.schedule.dom.shift.management.schedulecounter.PersonalCounterCategory;
import nts.uk.ctx.at.schedule.dom.shift.management.schedulecounter.PersonalCounterRepo;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.infra.i18n.resource.I18NResourcesForUK;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * スケジュール個人計情報を取得する
 */
@Stateless
public class PersonalCounterFinder {
    @Inject
    private PersonalCounterRepo repository;

    @Inject
    I18NResourcesForUK ukResource;

    public List<PersonalCounterCategoryDto> findById() {

        Optional<PersonalCounter> personalCounter = repository.get(AppContexts.user().companyId());
        List<EnumConstant> listEnum = EnumAdaptor.convertToValueNameList(PersonalCounterCategory.class, ukResource);

        return PersonalCounterCategoryDto.setData(listEnum,personalCounter);
    }
}
