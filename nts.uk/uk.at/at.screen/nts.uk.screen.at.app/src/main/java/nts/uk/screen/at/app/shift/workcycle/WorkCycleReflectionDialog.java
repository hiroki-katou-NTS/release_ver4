package nts.uk.screen.at.app.shift.workcycle;

import lombok.AllArgsConstructor;
import lombok.val;
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
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.WorkStyle;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.internal.PredetermineTimeSetForCalc;
import nts.uk.ctx.at.shared.dom.worktype.HolidayAtr;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeSet;
import nts.uk.ctx.at.shared.dom.worktype.algorithm.HolidayWorkTypeService;
import nts.uk.screen.at.app.ksm003.find.GetWorkCycle;
import nts.uk.screen.at.app.ksm003.find.WorkCycleDto;
import nts.uk.screen.at.app.ksm003.find.WorkCycleQueryRepository;
import nts.uk.shr.com.context.AppContexts;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * 勤務サイクル反映ダイアログ
 * @author khai.dh
 */
@Stateless
public class WorkCycleReflectionDialog {
	@Inject private HolidayWorkTypeService holidayWorkTypeService;
	@Inject private CreateWorkCycleAppImage createWorkCycleAppImage;
	@Inject private WeeklyWorkSettingRepository weeklyWorkDayRepository;
	@Inject private PublicHolidayRepository publicHolidayRepository;
	@Inject private WorkCycleQueryRepository workCycleRepository;
	@Inject private BasicScheduleService basicScheduleService;
	@Inject private GetWorkCycle getWorkCycle;
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
			workCycleCode = workCycleDtoList.get(0).getCode();
		}
		dto.setWorkCycleList(workCycleDtoList);

		// 2. 休日系の勤務種類を取得する [Get holiday type of work]
		List<WorkType> workTypes = holidayWorkTypeService.acquiredHolidayWorkType();

		// 3. 作成する(Require, 期間, 勤務サイクルの反映設定)
		Iterator<WorkType> it = workTypes.iterator();
		dto.setPubHoliday(handle(it, HolidayAtr.PUBLIC_HOLIDAY));
		dto.setSatHoliday(handle(it, HolidayAtr.STATUTORY_HOLIDAYS));
		dto.setNonSatHoliday(handle(it, HolidayAtr.NON_STATUTORY_HOLIDAYS));

		String nonSatHoliday = CollectionUtil.isEmpty(dto.getNonSatHoliday()) ? null : dto.getNonSatHoliday().get(0).getWorkTypeCode();
		String satHoliday = CollectionUtil.isEmpty(dto.getSatHoliday()) ? null : dto.getSatHoliday().get(0).getWorkTypeCode();
		String pubHoliday = CollectionUtil.isEmpty(dto.getPubHoliday()) ? null : dto.getPubHoliday().get(0).getWorkTypeCode();

		val config = new WorkCycleRefSetting(
				workCycleCode,
				refOrder,
				numOfSlideDays,
				nonSatHoliday,
				satHoliday,
				pubHoliday
		);
		val cRequire = new CreateWorkCycleAppImageRequire(
				weeklyWorkDayRepository,
				publicHolidayRepository,
				workCycleRepository);
		List<RefImageEachDay> refImageEachDayList = CreateWorkCycleAppImage.create(cRequire, creationPeriod, config);
		List<WorkCycleReflectionDto.RefImageEachDayDto> refImageEachDayDtos = new ArrayList<>();
		val wRequire = new WorkInformationRequire(basicScheduleService);
        for (RefImageEachDay ref : refImageEachDayList)
            refImageEachDayDtos.add(WorkCycleReflectionDto.RefImageEachDayDto.fromDomain(ref, wRequire));


		dto.setReflectionImage(refImageEachDayDtos); // 反映イメージ
		return dto;
	}

	public List<WorkCycleReflectionDto.WorkTypeDto> handle(Iterator<WorkType> iterator, HolidayAtr holidayAtr){
        List<WorkCycleReflectionDto.WorkTypeDto> result = new ArrayList<>();
	    while (iterator.hasNext()){
	        WorkType workType = iterator.next();
	        Iterator<WorkTypeSet> it = workType.getWorkTypeSetList().iterator();
	        while(it.hasNext()){
				WorkTypeSet workTypeSet = it.next();
				if(workTypeSet.getHolidayAtr().equals(holidayAtr)){
					result.add(WorkCycleReflectionDto.WorkTypeDto.fromDomain(workType));
					it.remove();
				}
			}
			if(!it.hasNext())
				iterator.remove();
        }
        return result;
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
		val wRequire = new WorkInformationRequire(basicScheduleService);
		List<RefImageEachDay> refImageEachDayList = CreateWorkCycleAppImage.create(cRequire, creationPeriod, workCycleRefSetting);
		List<WorkCycleReflectionDto.RefImageEachDayDto> reflectionImage = new ArrayList<>();
		refImageEachDayList.forEach(ref -> reflectionImage.add(WorkCycleReflectionDto.RefImageEachDayDto.fromDomain(ref, wRequire)));

		return reflectionImage;
	}

	@AllArgsConstructor
	private static class CreateWorkCycleAppImageRequire implements CreateWorkCycleAppImage.Require {
		private final String cid = AppContexts.user().companyId();

		private WeeklyWorkSettingRepository weeklyWorkDayRepository;
		private PublicHolidayRepository publicHolidayRepository;
		private WorkCycleQueryRepository workCycleRepository;

		@Override
		public Optional<WeeklyWorkDayPattern> getWeeklyWorkSetting() {
			return Optional.of(weeklyWorkDayRepository.getWeeklyWorkDayPatternByCompanyId(cid));
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
		private BasicScheduleService basicScheduleService;

		@Override
		public Optional<WorkType> findByPK(String workTypeCd) {
			return Optional.empty();
		}

		@Override
		public Optional<WorkTimeSetting> findByCode(String workTimeCode) {
			return Optional.empty();
		}

		@Override
		public SetupType checkNeededOfWorkTimeSetting(String workTypeCode) {
			return null;
		}

		@Override
		public PredetermineTimeSetForCalc getPredeterminedTimezone(String workTimeCd, String workTypeCd, Integer workNo) {
			return null;
		}

		@Override
		public WorkStyle checkWorkDay(String workTypeCode) {
			return basicScheduleService.checkWorkDay(workTypeCode);
		}
	}
}