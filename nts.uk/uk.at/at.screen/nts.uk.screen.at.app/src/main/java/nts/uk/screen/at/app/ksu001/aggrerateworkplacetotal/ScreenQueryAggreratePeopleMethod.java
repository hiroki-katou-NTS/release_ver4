package nts.uk.screen.at.app.ksu001.aggrerateworkplacetotal;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.AllArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.aggregation.dom.schedulecounter.aggregationprocess.workplacecounter.CountNumberOfPeopleByEachWorkMethodService;
import nts.uk.ctx.at.aggregation.dom.schedulecounter.aggregationprocess.workplacecounter.NumberOfPeopleByEachWorkMethod;
import nts.uk.ctx.at.shared.app.find.workrule.shiftmaster.WorkTimeSettingDto;
import nts.uk.ctx.at.shared.app.query.worktime.GetWorkingHoursInformationQuery;
import nts.uk.ctx.at.shared.dom.WorkInformation;
import nts.uk.ctx.at.shared.dom.scherec.aggregation.perdaily.AggregationUnitOfWorkMethod;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.dailyattendancework.IntegrationOfDaily;
import nts.uk.ctx.at.shared.dom.workrule.organizationmanagement.workplace.TargetOrgIdenInfor;
import nts.uk.ctx.at.shared.dom.workrule.shiftmaster.ShiftMaster;
import nts.uk.ctx.at.shared.dom.workrule.shiftmaster.ShiftMasterCode;
import nts.uk.ctx.at.shared.dom.workrule.shiftmaster.ShiftMasterRepository;
import nts.uk.screen.at.app.ksu001.getshiftpalette.ShiftMasterDto;
import nts.uk.shr.com.context.AppContexts;

/**
 * 勤務方法ごとに人数を集計する
 * @author hoangnd
 *
 */
@Stateless 
public class ScreenQueryAggreratePeopleMethod {

//	@Inject
//	private CountNumberOfPeopleByEachWorkMethodService.Require require;
	
	@Inject
	private GetWorkingHoursInformationQuery getWorkingHoursInformationQuery;
	
	@Inject
	private ShiftMasterRepository shiftMasterRepository;
	
	public AggreratePeopleMethodDto get(
			TargetOrgIdenInfor targetOrg,
			DatePeriod period,
			List<IntegrationOfDaily> scheduleList,
			List<IntegrationOfDaily> actualList,
			boolean isShiftDisplay
			) {
		Require require = new Require(shiftMasterRepository);
		
		AggreratePeopleMethodDto output = new AggreratePeopleMethodDto();
		String companyId = AppContexts.user().companyId();
		// 1: シフト表示か == false
		if (!isShiftDisplay) {
			// 1.1: 勤務方法別に集計する(Require, 対象組織識別情報, 期間, List<日別勤怠(Work)>, List<日別勤怠(Work)>, 勤務方法の集計単位, 関数( ( String ) -> T ))
			Map<GeneralDate, List<NumberOfPeopleByEachWorkMethod<String>>> countWork = 
						CountNumberOfPeopleByEachWorkMethodService.countByWorkMethod(
								require,
								targetOrg,
								period,
								scheduleList,
								actualList,
								AggregationUnitOfWorkMethod.WORK_TIME,
								workMethod -> new String(workMethod));
			
			// 1.2: 就業時間帯情報リストを取得する(会社ID, List<就業時間帯コード>)
			List<WorkTimeSettingDto> workTimeSetting =
					getWorkingHoursInformationQuery.getListWorkTimeSetting(
							companyId,
							countWork.entrySet()
									 .stream()
									 .map(x -> x.getValue())
									 .flatMap(x -> x.stream())
									 .map(x -> x.getWorkMethod())
									 .distinct()
									 .collect(Collectors.toList()))
					.stream()
					.map(WorkTimeSettingDto::new)
					.collect(Collectors.toList())
					;
			
			Map<GeneralDate, List<NumberOfPeopleByEachWorkMethod<WorkTimeSettingDto>>> countWorkOutput = 
					countWork.entrySet()
							 .stream()
							 .collect(Collectors.toMap(
									 e -> e.getKey(),
									 
									 e -> e.getValue().stream()
													 .map(x -> new NumberOfPeopleByEachWorkMethod<WorkTimeSettingDto>(
															 workTimeSetting.stream()
																	 .filter(y -> y.getWorkTimeCode() == x.getWorkMethod())
																	 .findFirst()
																	 .orElse(null),
															 x.getPlanNumber(),
															 x.getScheduleNumber(),
															 x.getActualNumber()))
													 .collect(Collectors.toList())
									 
							 ));
			output.setCountWork(countWorkOutput);
			
		} else { // 2: シフト表示か == true
			
			// 2.1 シフト別人数を取得する(Require, 対象組織識別情報, 期間, List<日別勤怠(Work)>, List<日別勤怠(Work)>)
			Map<GeneralDate, List<NumberOfPeopleByEachWorkMethod<ShiftMasterCode>>> shift = 
					CountNumberOfPeopleByEachWorkMethodService.getByShift(
							require,
							targetOrg,
							period,
							scheduleList,
							actualList);
			
			// 2.2: *get
			List<ShiftMasterDto> shirftMasters = 
					shiftMasterRepository.getByListShiftMaterCd2(
							companyId,
							shift.entrySet()
							 .stream()
							 .map(x -> x.getValue())
							 .flatMap(x -> x.stream())
							 .map(x -> x.getWorkMethod().v())
							 .distinct()
							 .collect(Collectors.toList()))
					.stream()
					.map(ShiftMasterDto::new)
					.collect(Collectors.toList());
					
			
			Map<GeneralDate, List<NumberOfPeopleByEachWorkMethod<ShiftMasterDto>>> shiftOutput =
				shift.entrySet()
					.stream()
					.collect(Collectors.toMap(
							e -> e.getKey(),
							e -> e.getValue().stream()
											 .map(x -> new NumberOfPeopleByEachWorkMethod<ShiftMasterDto>(
													 shirftMasters.stream()
															 .filter(y -> y.shiftMasterCode == x.getWorkMethod().v())
															 .findFirst()
															 .orElse(null),
													 x.getPlanNumber(),
													 x.getScheduleNumber(),
													 x.getActualNumber()))
											 .collect(Collectors.toList())
					 ));
			
			
			output.setShift(shiftOutput);
			
		}
		
		
		
		
		return output;
		
		
	}
	
	@AllArgsConstructor
	private static class Require implements CountNumberOfPeopleByEachWorkMethodService.Require {
		
		@Inject
		private ShiftMasterRepository shiftMasterRepository;
		
		@Override
		public Optional<ShiftMaster> getShiftMaster(WorkInformation workInformation) {
			return shiftMasterRepository.getByWorkTypeAndWorkTime(
					AppContexts.user().companyId(),
					workInformation.getWorkTimeCode().v(),
					workInformation.getWorkTimeCodeNotNull().map(x -> x.v()).orElse(null));
		}
		
	}
}
