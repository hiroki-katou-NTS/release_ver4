package nts.uk.screen.at.app.ksm008.find;

import nts.uk.ctx.at.schedulealarm.dom.alarmcheck.AlarmCheckConditionScheduleCode;
import nts.uk.ctx.at.schedulealarm.dom.alarmcheck.AlarmCheckConditionScheduleRepository;
import nts.uk.shr.com.context.AppContexts;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class AlarmCheckConditionsFinder {

    @Inject
    private AlarmCheckConditionScheduleRepository alarmCheckConditionScheduleRepository;

    public List<AlarmCheckDto> getItems() {
        // Convert data to SampleDto
        // Return converted DTO list.
        String contractCd = AppContexts.user().contractCode();
        String companyId = AppContexts.user().companyId();
        return alarmCheckConditionScheduleRepository.getAll(contractCd, companyId)
                .stream().map(alarmCheckConditionSchedule -> AlarmCheckDto.toDto(alarmCheckConditionSchedule)).collect(Collectors.toList());
    }

    public AlarmCheckDto getMsg(String alarmCode) {
        String contractCd = AppContexts.user().contractCode();
        String cid = AppContexts.user().companyId();
        return AlarmCheckDto.toDto(alarmCheckConditionScheduleRepository.get(contractCd, cid, new AlarmCheckConditionScheduleCode(alarmCode)));
    }
}