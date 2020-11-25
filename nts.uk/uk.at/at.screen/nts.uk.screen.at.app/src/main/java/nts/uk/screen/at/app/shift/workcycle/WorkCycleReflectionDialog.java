package nts.uk.screen.at.app.shift.workcycle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.AllArgsConstructor;
import lombok.val;
import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.schedule.dom.shift.businesscalendar.holiday.PublicHoliday;
import nts.uk.ctx.at.schedule.dom.shift.businesscalendar.holiday.PublicHolidayRepository;
import nts.uk.ctx.at.schedule.dom.shift.pattern.work.WeeklyWorkSettingRepository;
import nts.uk.ctx.at.schedule.dom.shift.weeklywrkday.WeeklyWorkDayPattern;
import nts.uk.ctx.at.schedule.dom.shift.workcycle.WorkCycle;
import nts.uk.ctx.at.schedule.dom.shift.workcycle.domainservice.CreateWorkCycleAppImage;
import nts.uk.ctx.at.schedule.dom.shift.workcycle.domainservice.RefImageEachDay;
import nts.uk.ctx.at.schedule.dom.shift.workcycle.domainservice.WorkCreateMethod;
import nts.uk.ctx.at.schedule.dom.shift.workcycle.domainservice.WorkCycleRefSetting;
import nts.uk.ctx.at.shared.dom.WorkInformation;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.BasicScheduleService;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.SetupType;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixedWorkSetting;
import nts.uk.ctx.at.shared.dom.worktime.flexset.FlexWorkSetting;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkSetting;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.internal.PredetermineTimeSetForCalc;
import nts.uk.ctx.at.shared.dom.worktype.HolidayAtr;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.ctx.at.shared.dom.worktype.algorithm.HolidayWorkTypeService;
import nts.uk.screen.at.app.ksm003.find.GetWorkCycle;
import nts.uk.screen.at.app.ksm003.find.WorkCycleDto;
import nts.uk.screen.at.app.ksm003.find.WorkCycleQueryRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * 勤務サイクル反映ダイアログ
 * @author khai.dh
 */
@Stateless
public class WorkCycleReflectionDialog {
	@Inject private HolidayWorkTypeService holidayWorkTypeService;
	@Inject private WeeklyWorkSettingRepository weeklyWorkDayRepository;
	@Inject private PublicHolidayRepository publicHolidayRepository;
	@Inject private WorkCycleQueryRepository workCycleRepository;
	@Inject private BasicScheduleService basicScheduleService;
	@Inject private GetWorkCycle getWorkCycle;

	@Inject
	private	WorkTypeRepository workTypeRepository;

    @Inject
    private WorkTimeSettingRepository workTimeSettingRepository;

 	/**
	 * 起動情報を取得する
	 * @param bootMode 起動モード
	 * @param creationPeriod 作成期間
	 * @param workCycleCode 勤務サイクルコード
	 * @param refOrder パターン反映順序
	 * @param numOfSlideDays スライド日数
	 * @return start up info
	 */
	public WorkCycleReflectionDto getStartupInfo(
			BootMode bootMode,
			DatePeriod creationPeriod,
			String workCycleCode,
			List<WorkCreateMethod> refOrder,
			int numOfSlideDays){
		val dto = new WorkCycleReflectionDto();
		List<WorkCycleDto> workCycleDtoList = new ArrayList<>();
		// 1. 勤務サイクル一覧を取得する [Get a list of work cycles]
		if (bootMode == BootMode.EXEC_MODE){
			workCycleDtoList = getWorkCycle.getDataStartScreen();
			if(CollectionUtil.isEmpty(workCycleDtoList))
				throw new BusinessException("Msg_37");
			workCycleCode = workCycleDtoList.get(0).getCode();
		}
		dto.setWorkCycleList(workCycleDtoList);
		// 2. 休日系の勤務種類を取得する [Get holiday type of work]
		List<WorkType> workTypes = holidayWorkTypeService.acquiredHolidayWorkType();
        Map<Optional<HolidayAtr>, List<WorkType>> map = workTypes.stream().collect(
                Collectors.groupingBy(WorkType::getHolidayAtr)
        );
		// 3. 作成する(Require, 期間, 勤務サイクルの反映設定)
		dto.setPubHoliday(convertToDomain(map.get(Optional.of(HolidayAtr.PUBLIC_HOLIDAY))));
		dto.setSatHoliday(convertToDomain(map.get(Optional.of(HolidayAtr.STATUTORY_HOLIDAYS))));
		dto.setNonSatHoliday(convertToDomain(map.get(Optional.of(HolidayAtr.NON_STATUTORY_HOLIDAYS))));

		String satHoliday = CollectionUtil.isEmpty(dto.getSatHoliday()) ? null : dto.getSatHoliday().get(0).getWorkTypeCode();
		String nonSatHoliday = CollectionUtil.isEmpty(dto.getNonSatHoliday()) ? null : dto.getNonSatHoliday().get(0).getWorkTypeCode();
		String pubHoliday = CollectionUtil.isEmpty(dto.getPubHoliday()) ? null : dto.getPubHoliday().get(0).getWorkTypeCode();

		val config = new WorkCycleRefSetting(
				workCycleCode,
				refOrder,
				numOfSlideDays,
				satHoliday,
				nonSatHoliday,
				pubHoliday
		);
		val cRequire = new CreateWorkCycleAppImageRequire(
				weeklyWorkDayRepository,
				publicHolidayRepository,
				workCycleRepository);
		List<RefImageEachDay> refImageEachDayList = CreateWorkCycleAppImage.create(cRequire, creationPeriod, config);
		List<WorkCycleReflectionDto.RefImageEachDayDto> refImageEachDayDtos = new ArrayList<>();
		val wRequire = new WorkInformationRequire(workTypeRepository);

		Map<String, String> workTypeCodeNameMap = getWorkTypeCodeNameMap(refImageEachDayList);
        refImageEachDayList.forEach(ref -> refImageEachDayDtos.add(
        		WorkCycleReflectionDto.RefImageEachDayDto
						.fromDomain(ref, wRequire, workTimeSettingRepository, workTypeCodeNameMap
				)
		));
		dto.setReflectionImage(refImageEachDayDtos); // 反映イメージ
		return dto;
	}

	public List<WorkCycleReflectionDto.WorkTypeDto> convertToDomain(List<WorkType> list){
		if(CollectionUtil.isEmpty(list)){
			return Collections.emptyList();
		}

        Set<WorkCycleReflectionDto.WorkTypeDto> result = new HashSet<>();
        list.forEach( i -> result.add(WorkCycleReflectionDto.WorkTypeDto.fromDomain(i)));
        return new ArrayList<>(result).stream()
                .sorted(Comparator.comparing(WorkCycleReflectionDto.WorkTypeDto::getWorkTypeCode))
                .collect(Collectors.toList());
    }

	/**
	 * 勤務サイクルの適用イメージを取得する
	 * @param creationPeriod 作成期間
	 * @param workCycleRefSetting 設定
	 * @return 反映イメージ
	 */
	public List<WorkCycleReflectionDto.RefImageEachDayDto> getWorkCycleAppImage(
			DatePeriod creationPeriod,
			WorkCycleRefSetting workCycleRefSetting){
		val cRequire = new CreateWorkCycleAppImageRequire(
				weeklyWorkDayRepository,
				publicHolidayRepository,
				workCycleRepository);
		val wRequire = new WorkInformationRequire(workTypeRepository);
		List<RefImageEachDay> refImageEachDayList = CreateWorkCycleAppImage.create(cRequire, creationPeriod, workCycleRefSetting);
		List<WorkCycleReflectionDto.RefImageEachDayDto> reflectionImage = new ArrayList<>();
		Map<String, String> workTypeCodeNameMap = getWorkTypeCodeNameMap(refImageEachDayList);
		refImageEachDayList.forEach(ref -> reflectionImage.add(
				WorkCycleReflectionDto.RefImageEachDayDto
						.fromDomain(ref, wRequire, workTimeSettingRepository, workTypeCodeNameMap)
		));

		return reflectionImage;
	}

	private Map<String, String> getWorkTypeCodeNameMap(List<RefImageEachDay> refImageEachDayList) {
		List<String> workTypeCodeList = new ArrayList<>();
		refImageEachDayList.forEach(ref -> {
			WorkTypeCode workTypeCode = ref.getWorkInformation().getWorkTypeCode();
			workTypeCodeList.add(workTypeCode == null ? "" : workTypeCode.v());
		});
		return workTypeRepository
				.getCodeNameWorkType(AppContexts.user().companyId(), workTypeCodeList);
	}

	@AllArgsConstructor
	private static class CreateWorkCycleAppImageRequire implements CreateWorkCycleAppImage.Require {
		private final String cid = AppContexts.user().companyId();

		private WeeklyWorkSettingRepository weeklyWorkDayRepository;
		private PublicHolidayRepository publicHolidayRepository;
		private WorkCycleQueryRepository workCycleRepository;

		@Override
		public Optional<WeeklyWorkDayPattern> getWeeklyWorkSetting() {
			return Optional.ofNullable(weeklyWorkDayRepository.getWeeklyWorkDayPatternByCompanyId(cid));
		}

		@Override
		public List<PublicHoliday> getpHolidayWhileDate(GeneralDate strDate, GeneralDate endDate) {
			return publicHolidayRepository.getpHolidayWhileDate(cid, strDate, endDate);
		}

		@Override
		public Optional<WorkCycle> getWorkCycle(String code) {
			return workCycleRepository.getByCidAndCode(cid, code);
		}
	}

	@AllArgsConstructor
	private static class WorkInformationRequire implements WorkInformation.Require {

		private final String companyId = AppContexts.user().companyId();

		private WorkTypeRepository workTypeRepository;

		@Override
		public Optional<WorkType> getWorkType(String workTypeCd) {
            return workTypeRepository.findByPK(companyId, workTypeCd);
		}

		@Override
		public Optional<WorkTimeSetting> getWorkTime(String workTimeCode) {
			return Optional.empty();
		}

		@Override
		public SetupType checkNeededOfWorkTimeSetting(String workTypeCode) {
			return null;
		}

		@Override
		public PredetermineTimeSetForCalc getPredeterminedTimezone(String workTypeCd, String workTimeCd, Integer workNo) {
			return null;
		}

		@Override
		public FixedWorkSetting getWorkSettingForFixedWork(WorkTimeCode code) {
			// TODO 自動生成されたメソッド・スタブ
			return null;
		}

		@Override
		public FlowWorkSetting getWorkSettingForFlowWork(WorkTimeCode code) {
			// TODO 自動生成されたメソッド・スタブ
			return null;
		}

		@Override
		public FlexWorkSetting getWorkSettingForFlexWork(WorkTimeCode code) {
			// TODO 自動生成されたメソッド・スタブ
			return null;
		}

		@Override
		public PredetemineTimeSetting getPredetermineTimeSetting(WorkTimeCode wktmCd) {
			// TODO 自動生成されたメソッド・スタブ
			return null;
		}
	}
}