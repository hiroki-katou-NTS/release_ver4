package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import java.util.Map;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.layer.app.cache.CacheCarrier;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.require.RecordDomRequireService;
import nts.uk.ctx.at.shared.dom.attendance.MasterShareBus;
import nts.uk.ctx.at.shared.dom.attendance.MasterShareBus.MasterShareContainer;
import nts.uk.ctx.at.shared.dom.common.TimeOfDay;
import nts.uk.ctx.at.shared.dom.dailyprocess.calc.FactoryManagePerPersonDailySet;
import nts.uk.ctx.at.shared.dom.scherec.addsettingofworktime.AddSetting;
import nts.uk.ctx.at.shared.dom.scherec.addsettingofworktime.HolidayAddtionRepository;
import nts.uk.ctx.at.shared.dom.scherec.addsettingofworktime.HolidayCalcMethodSet;
import nts.uk.ctx.at.shared.dom.scherec.addsettingofworktime.WorkDeformedLaborAdditionSet;
import nts.uk.ctx.at.shared.dom.scherec.addsettingofworktime.WorkFlexAdditionSet;
import nts.uk.ctx.at.shared.dom.scherec.addsettingofworktime.WorkRegularAdditionSet;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.bonuspay.primitives.BonusPaySettingCode;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.bonuspay.repository.BPSettingRepository;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.bonuspay.setting.BonusPaySetting;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.dailyattendancework.IntegrationOfDaily;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailycalprocess.calculation.ManagePerCompanySet;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailycalprocess.calculation.ManagePerPersonDailySet;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailycalprocess.calculation.PredetermineTimeSetForCalc;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.personcostcalc.employeeunitpricehistory.EmployeeUnitPriceHistoryItem;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.personcostcalc.employeeunitpricehistory.EmployeeUnitPriceHistoryRepositoly;
import nts.uk.ctx.at.shared.dom.scherec.statutory.worktime.UsageUnitSetting;
import nts.uk.ctx.at.shared.dom.scherec.statutory.worktime.algorithm.DailyStatutoryLaborTime;
import nts.uk.ctx.at.shared.dom.scherec.statutory.worktime.week.DailyUnit;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.shr.com.context.AppContexts;


/**
 * @author kazuki_watanabe
 *
 */
@Stateless
public class FactoryManagePerPersonDailySetImpl implements FactoryManagePerPersonDailySet {

	/*加給設定*/
	@Inject
	private BPSettingRepository bPSettingRepository;
	
	/* 所定時間帯 */
	@Inject
	private PredetemineTimeSettingRepository predetemineTimeSetRepository;
	
	/* 休暇加算設定 */
	@Inject
	private HolidayAddtionRepository hollidayAdditonRepository;

	/* 社員単価履歴 */
	@Inject
	private EmployeeUnitPriceHistoryRepositoly employeeUnitPriceHistoryRepositoly;
	
	@Inject
	private RecordDomRequireService requireService;
	
	
	@Override
	public Optional<ManagePerPersonDailySet> create(String companyId, ManagePerCompanySet companySetting, IntegrationOfDaily daily, WorkingConditionItem nowWorkingItem ) {
		return internalCreate(companyId, companySetting.getUsageSetting(), daily, nowWorkingItem,
				companySetting.getShareContainer());

	}


	private Optional<ManagePerPersonDailySet> internalCreate(String companyId, 
			Optional<UsageUnitSetting> usageSetting, 
			IntegrationOfDaily daily, WorkingConditionItem nowWorkingItem,
			MasterShareContainer<String> shareContainer) {
		try {
			val require = requireService.createRequire();
			/*法定労働時間*/
			DailyUnit dailyUnit = DailyStatutoryLaborTime.getDailyUnit(
					require,
					new CacheCarrier(),
					companyId,
					daily.getAffiliationInfor().getEmploymentCode().toString(),
					daily.getEmployeeId(),
					daily.getYmd(),
					nowWorkingItem.getLaborSystem(),
					usageSetting);

			if(dailyUnit == null || dailyUnit.getDailyTime() == null)
				dailyUnit = new DailyUnit(new TimeOfDay(0));
			
			/*加算設定*/
			AddSetting addSetting = this.getAddSetting(
					companyId,
					hollidayAdditonRepository.findByCompanyId(companyId),
					nowWorkingItem.getLaborSystem());
	
			/*加給*/
			Optional<BonusPaySettingCode> bpCode = daily.getAffiliationInfor().getBonusPaySettingCode();
			Optional<BonusPaySetting> bonusPaySetting = Optional.empty();
			if(bpCode.isPresent() && bpCode.get() != null ) {
				bonusPaySetting = this.bPSettingRepository.getBonusPaySetting(companyId, bpCode.get());
			}
			
			/**　勤務種類 */
			val workType = require.workType(companyId, nowWorkingItem.getWorkCategory().getWeekdayTime().getWorkTypeCode().get().v())
					.orElseThrow(() -> new RuntimeException("No WorkType"));
		
			/*平日時*/
			PredetermineTimeSetForCalc predetermineTimeSetByPersonWeekDay = this.getPredByPersonInfo(
					nowWorkingItem.getWorkCategory().getWeekdayTime().getWorkTimeCode().get(), shareContainer, workType);
			
			/*社員単価履歴*/
			Optional<EmployeeUnitPriceHistoryItem> unitPrice = employeeUnitPriceHistoryRepositoly.get(daily.getEmployeeId(), daily.getYmd());
			
			return Optional.of(
					new ManagePerPersonDailySet(
					nowWorkingItem,
					dailyUnit,
					addSetting,
					bonusPaySetting,
					predetermineTimeSetByPersonWeekDay,
					unitPrice)
				);
		}
		catch(RuntimeException e) {
			return Optional.empty();
		}
	}


	/**
	 * @param map 各加算設定
	 * @param workingSystem 労働制
	 * @return 加算設定
	 */
	private AddSetting getAddSetting(String companyID, Map<String, AggregateRoot> map, WorkingSystem workingSystem) {
		
		switch(workingSystem) {
		case REGULAR_WORK:
			AggregateRoot workRegularAdditionSet = map.get("regularWork");
			return workRegularAdditionSet != null
					?(WorkRegularAdditionSet) workRegularAdditionSet
					: new WorkRegularAdditionSet(companyID, HolidayCalcMethodSet.emptyHolidayCalcMethodSet());
		
		case FLEX_TIME_WORK:
			AggregateRoot workFlexAdditionSet = map.get("flexWork");
			return workFlexAdditionSet != null
					?(WorkFlexAdditionSet) workFlexAdditionSet
					: new WorkFlexAdditionSet(companyID, HolidayCalcMethodSet.emptyHolidayCalcMethodSet());
			
		case VARIABLE_WORKING_TIME_WORK:
			AggregateRoot workDeformedLaborAdditionSet = map.get("irregularWork");
			return workDeformedLaborAdditionSet != null
					? (WorkDeformedLaborAdditionSet) workDeformedLaborAdditionSet
					: new WorkDeformedLaborAdditionSet(companyID, HolidayCalcMethodSet.emptyHolidayCalcMethodSet());
		
		default:
			return new WorkDeformedLaborAdditionSet(companyID, HolidayCalcMethodSet.emptyHolidayCalcMethodSet());
		}
	}
	
	
	/**
	 * 就業時間帯の所定時間帯を取得する
	 * （所定時間帯を計算用就業時間帯に変換して返してくれる計算処理専用メソッド）
	 * 
	 * @param workTimeCode
	 * @param shareContainer
	 * @return
	 */
	private PredetermineTimeSetForCalc getPredByPersonInfo(WorkTimeCode workTimeCode,
			MasterShareContainer<String> shareContainer, WorkType workType) {

		val predSetting = getPredetermineTimeSetFromShareContainer(shareContainer, AppContexts.user().companyId(),
				workTimeCode.toString());
		if (!predSetting.isPresent())
			throw new RuntimeException("predetermineedSetting is null");
		return PredetermineTimeSetForCalc.convertFromAggregatePremiumTime(predSetting.get(), workType);

	}
	
	/**
	 * 共有コンテナを使った所定時間帯設定の取得
	 * 
	 * @param shareContainer
	 * @param companyId
	 * @param workTimeCode
	 * @return
	 */
	private Optional<PredetemineTimeSetting> getPredetermineTimeSetFromShareContainer(
			MasterShareContainer<String> shareContainer, String companyId, String workTimeCode) {
		val predSet = shareContainer.getShared("PredetemineSet" + workTimeCode,
				() -> predetemineTimeSetRepository.findByWorkTimeCode(companyId, workTimeCode));
		if (predSet.isPresent()) {
			return Optional.of(predSet.get().clone());
		}
		return Optional.empty();
	}


	@Override
	public Optional<ManagePerPersonDailySet> create(String companyId, String sid, GeneralDate ymd,
			IntegrationOfDaily daily) {
		
		val require = requireService.createRequire();
		val workingItem = require.workingConditionItem(sid, ymd);
		if(!workingItem.isPresent()) {
			return Optional.empty();
		}
		val usageSetting = require.usageUnitSetting(companyId);
		MasterShareContainer<String> shareContainer = MasterShareBus.open();
		
		val personDailySet = internalCreate(companyId, usageSetting, daily, workingItem.get(), shareContainer);
		
		shareContainer.clearAll();
		
		return personDailySet;
	}
	
	
}
