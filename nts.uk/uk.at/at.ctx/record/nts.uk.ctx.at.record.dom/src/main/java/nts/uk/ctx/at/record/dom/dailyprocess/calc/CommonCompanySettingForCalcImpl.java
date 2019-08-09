package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeRepository;
import nts.uk.ctx.at.record.dom.optitem.OptionalItemRepository;
import nts.uk.ctx.at.record.dom.optitem.applicable.EmpConditionRepository;
import nts.uk.ctx.at.record.dom.optitem.calculation.FormulaRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.ErrorAlarmWorkRecord;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.ErrorAlarmWorkRecordRepository;
import nts.uk.ctx.at.record.dom.workrule.specific.SpecificWorkRuleRepository;
import nts.uk.ctx.at.shared.dom.bonuspay.repository.BPUnitUseSettingRepository;
import nts.uk.ctx.at.shared.dom.calculation.holiday.HolidayAddtionRepository;
import nts.uk.ctx.at.shared.dom.ot.zerotime.ZeroTimeRepository;
import nts.uk.ctx.at.shared.dom.statutory.worktime.UsageUnitSetting;
import nts.uk.ctx.at.shared.dom.statutory.worktime.UsageUnitSettingRepository;
import nts.uk.ctx.at.shared.dom.statutory.worktime.employee.EmployeeWtSetting;
//import nts.uk.ctx.at.shared.dom.statutory.worktime.employee.EmployeeWtSettingRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensLeaveComSetRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * 計算に必要なパラメータを取得する
 * @author keisuke_hoshina
 *
 */
@Stateless
public class CommonCompanySettingForCalcImpl implements CommonCompanySettingForCalc{

	//休暇加算設定
	@Inject
	private HolidayAddtionRepository holidayAddtionRepository;
	//総拘束時間
	@Inject
	private SpecificWorkRuleRepository specificWorkRuleRepository;
	//会社ごとの代休設定
	@Inject
	private CompensLeaveComSetRepository compensLeaveComSetRepository;
	//乖離
	@Inject 
	private DivergenceTimeRepository divergenceTimeRepository;
	//エラーアラーム設定
	@Inject
	private ErrorAlarmWorkRecordRepository errorAlarmWorkRecordRepository;
	//加給設定の利用単位
	@Inject
	private BPUnitUseSettingRepository bPUnitUseSettingRepository; 
	//0時跨ぎ
	@Inject
	private ZeroTimeRepository zeroTimeRepository;
	//任意項目
	@Inject
	private OptionalItemRepository optionalItemRepository;
	//計算式
	@Inject
	private FormulaRepository formulaRepository;
	//労働条件
	@Inject
	private EmpConditionRepository empConditionRepository;
	
	@Inject
	//労働時間と日数の設定の利用単位の設定
	private UsageUnitSettingRepository usageUnitSettingRepository;
	
//	@Inject
//	private EmployeeWtSettingRepository employeeWtSettingRepository;
	
	@Override
	public ManagePerCompanySet getCompanySetting(CalculateOption calcOption) {

		String companyId = AppContexts.user().companyId();
		
		List<ErrorAlarmWorkRecord> errorAlerms = Collections.emptyList();
		if (!calcOption.isMasterTime()) {
			errorAlerms = errorAlarmWorkRecordRepository.getAllErAlCompanyAndUseAtr(companyId, true);
		}
		
		val optionalItems = optionalItemRepository.findAll(companyId);
		val usageSetting = usageUnitSettingRepository.findByCompany(companyId);
		return new ManagePerCompanySet(holidayAddtionRepository.findByCompanyId(companyId),
									  holidayAddtionRepository.findByCId(companyId),
									  specificWorkRuleRepository.findCalcMethodByCid(companyId),
									  compensLeaveComSetRepository.find(companyId),
									  divergenceTimeRepository.getAllDivTime(companyId),
									  errorAlerms,
									  bPUnitUseSettingRepository.getSetting(companyId),
									  optionalItems,
									  formulaRepository.find(companyId),
									  empConditionRepository.findAll(companyId, optionalItems.stream().map(oi -> oi.getOptionalItemNo().v()).collect(Collectors.toList())),
									  zeroTimeRepository.findByCId(companyId),
									  specificWorkRuleRepository.findUpperLimitWkHourByCid(companyId),
									  usageSetting,
									  getEmployeeWTSetting(usageSetting, companyId));
	}
	
	private Optional<EmployeeWtSetting> getEmployeeWTSetting(Optional<UsageUnitSetting> usageSetting, String compID){
		/** Comment by: employeeWtSettingRepository is disable */
//		if(usageSetting.isPresent() && usageSetting.get().isEmployee()){
//			return Optional.ofNullable(employeeWtSettingRepository.find(compID));
//		}
		return Optional.empty();
	}

}
