package nts.uk.ctx.at.aggregation.app.find.schedulecounter.wkptimezone;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.aggregation.dom.schedulecounter.timezonepeople.WorkplaceCounterTimeZonePeopleNumber;
import nts.uk.ctx.at.aggregation.dom.schedulecounter.timezonepeople.WorkplaceCounterTimeZonePeopleNumberRepo;
import nts.uk.shr.com.context.AppContexts;

/**
 * 時間帯人数情報を取得する
 */
@Stateless
public class WkpTimeZonePeopleNumberFinder {

    @Inject
    private WorkplaceCounterTimeZonePeopleNumberRepo repository;

    public List<WkpCounterStartTimeDto> findById() {
        Optional<WorkplaceCounterTimeZonePeopleNumber> timeZonePeopleNumber = repository.get(AppContexts.user().companyId());

        return timeZonePeopleNumber.isPresent() ?
            timeZonePeopleNumber.get().getTimeZoneList().stream().map(x -> new WkpCounterStartTimeDto(x.v())).collect(Collectors.toList()) :
            new ArrayList<>();
    }
}
