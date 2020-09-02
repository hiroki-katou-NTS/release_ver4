package nts.uk.screen.at.app.ksm005.find;

import nts.uk.ctx.at.schedule.dom.shift.weeklywrkday.WeeklyWorkDayPattern;
import nts.uk.ctx.at.schedule.dom.shift.weeklywrkday.WeeklyWorkDayRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.ctx.at.shared.pub.worktime.worktimeset.WorkTimeSettingPub;
import nts.uk.shr.com.context.AppContexts;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

@Stateless
public class WeeklyWorkScreenProcessor {

    @Inject
    private WorkTypeRepository workTypeRepository;

    @Inject
    private WeeklyWorkDayRepository weeklyWorkDayRepository;

    @Inject
    private WorkTimeSettingPub workTimeSettingPub;

    public WeeklyWorkDto findDataBentoMenu(RequestPrams requestPrams) {

        String cid = AppContexts.user().companyId();
        WeeklyWorkDayPattern weeklyWorkDayPattern = weeklyWorkDayRepository.getWeeklyWorkDayPatternByCompanyId(cid);

        //get WorkdayPatternDto
        List<WorkdayPatternDto> workdayPatternDtos = new ArrayList<>();
        weeklyWorkDayPattern.getListWorkdayPatternItem().forEach(x -> {
            workdayPatternDtos.add(new WorkdayPatternDto(
                    x.getDayOfWeek().description,
                    x.getWorkdayDivision().description,
                    x.getWorkdayDivision().value
            ));
        });

        // get workTypeCodes
        List<WorkType> workTypes = workTypeRepository.getAcquiredHolidayWorkTypes(cid);
        List<String> workTypeCodes = new ArrayList<>();
        workTypes.forEach(x ->workTypeCodes.add(x.getWorkTypeCode().v()));
        List<String> finalworkTypeCodes = workTypeCodes.stream().distinct().collect(Collectors.toList());
        //get workTypeName
        List<workTypeDto> workTypeDtos = new ArrayList<>();

        List<String> listWorkTypeCode = requestPrams.listWorkTypeCd.stream().filter(Objects::nonNull).collect(Collectors.toList());
        Map<String, String> listWorkTypeCodeName = listWorkTypeCode.size() > 0
                ? workTypeRepository.getCodeNameWorkType(cid, listWorkTypeCode) : new HashMap<>();

        listWorkTypeCodeName.forEach((k,v) -> workTypeDtos.add(new workTypeDto(k, v)));

        // get WorkTimeSettingName
        String WorkTimeSettingName = workTimeSettingPub.getWorkTimeSettingName(cid, requestPrams.worktimeCode);

        return new WeeklyWorkDto(workdayPatternDtos,workTypeDtos,finalworkTypeCodes,WorkTimeSettingName);
    }

}
