/**
 * 
 */
package nts.uk.screen.at.app.ksu001.getaggregatedInfo;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.DateInMonth;
import nts.arc.time.calendar.period.DatePeriod;
import nts.gul.text.StringUtil;
import nts.uk.ctx.at.aggregation.dom.schedulecounter.tally.PersonalCounterCategory;
import nts.uk.ctx.at.aggregation.dom.schedulecounter.tally.WorkplaceCounterCategory;
import nts.uk.ctx.at.schedule.app.find.budget.external.ExternalBudgetDto;
import nts.uk.ctx.at.shared.dom.workrule.organizationmanagement.workplace.TargetOrgIdenInfor;
import nts.uk.ctx.at.shared.dom.workrule.organizationmanagement.workplace.TargetOrganizationUnit;
import nts.uk.screen.at.app.ksu001.aggreratedinformation.AggregatedInformationDto;
import nts.uk.screen.at.app.ksu001.aggreratedinformation.ScreenQueryAggreratedInformation;
import nts.uk.screen.at.app.ksu001.start.AggregatePersonalMapDto;
import nts.uk.screen.at.app.ksu001.start.AggregateWorkplaceMapDto;
import nts.uk.screen.at.app.ksu001.start.ExternalBudgetMapDto;
import nts.uk.screen.at.app.ksu001.start.ExternalBudgetMapDtoList;

/**
 * @author laitv
 *
 */
@Stateless
public class GetAggregatedInfoFinder {

	@Inject
	private ScreenQueryAggreratedInformation screenQAggreratedInfo;
	
	private static final String DATE_FORMAT = "yyyy/MM/dd";

	public AggregatedInformationRs getData(AggregatedInfoParam param) {
		DatePeriod datePeriod = new DatePeriod(GeneralDate.fromString(param.startDate, DATE_FORMAT), GeneralDate.fromString(param.endDate, DATE_FORMAT));
		DateInMonth closeDate = new DateInMonth(param.day, param.isLastDay);
		Boolean isAchievement = param.getActualData;
		Optional<PersonalCounterCategory> personalCounterOp = StringUtil.isNullOrEmpty(param.personTotalSelected, true) ? Optional.empty() : Optional.of(PersonalCounterCategory.of(Integer.valueOf(param.personTotalSelected)));
		Optional<WorkplaceCounterCategory> workplaceCounterOp = StringUtil.isNullOrEmpty(param.workplaceSelected, true) ? Optional.empty() : Optional.of(WorkplaceCounterCategory.of(Integer.valueOf(param.workplaceSelected)));
		TargetOrgIdenInfor targetOrgIdenInfor = null;
		if (param.unit == TargetOrganizationUnit.WORKPLACE.value) {
			targetOrgIdenInfor = new TargetOrgIdenInfor(TargetOrganizationUnit.WORKPLACE,Optional.of(param.workplaceId == null ? param.workplaceId : param.workplaceId), Optional.empty());
		} else {
			targetOrgIdenInfor = new TargetOrgIdenInfor(TargetOrganizationUnit.WORKPLACE_GROUP, Optional.empty(),Optional.of(param.workplaceGroupId == null ? param.workplaceGroupId : param.workplaceGroupId));
		}
		
		AggregatedInformationDto dto = screenQAggreratedInfo.get(param.listSid, datePeriod, closeDate, isAchievement, targetOrgIdenInfor, personalCounterOp, workplaceCounterOp, param.isShiftMode);
		return convertData(dto);
		
		

	}
	
	private AggregatedInformationRs convertData(AggregatedInformationDto dto) {
		return new AggregatedInformationRs(
				convertExternalBudget(dto.externalBudget), 
				AggregatePersonalMapDto.convertMap(dto.aggrerateSchedule.aggreratePersonal), 
				AggregateWorkplaceMapDto.convertMap(dto.aggrerateSchedule.aggrerateWorkplace));
		
	}

	private List<ExternalBudgetMapDtoList> convertExternalBudget(
			Map<GeneralDate, Map<ExternalBudgetDto, String>> externalBudget) {

		return externalBudget.entrySet().stream()
				.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().entrySet().stream()
						.map(x -> new ExternalBudgetMapDto(x.getKey().getExternalBudgetCode(),
								x.getKey().getExternalBudgetCode(), x.getValue()))
						.collect(Collectors.toList())))
				.entrySet().stream().map(x -> new ExternalBudgetMapDtoList(x.getKey(), x.getValue()))
				.collect(Collectors.toList());
	}
}
