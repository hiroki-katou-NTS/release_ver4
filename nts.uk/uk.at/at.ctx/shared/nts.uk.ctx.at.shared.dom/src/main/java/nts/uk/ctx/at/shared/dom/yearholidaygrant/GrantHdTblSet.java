package nts.uk.ctx.at.shared.dom.yearholidaygrant;

import java.util.List;
import java.util.Optional;

import lombok.Getter;
import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.error.BusinessException;
import nts.arc.layer.dom.AggregateRoot;

/**
 * 年休付与テーブル設定
 * 
 * @author TanLV
 *
 */

@Getter
public class GrantHdTblSet extends AggregateRoot {
	/* 会社ID */
	private String companyId;

	/* コード */
	private YearHolidayCode yearHolidayCode;

	/* 名称 */
	private YearHolidayName yearHolidayName;

	/* 計算方法 */
	private CalculationMethod calculationMethod;

	/* 計算基準 */
	private StandardCalculation standardCalculation;

	/* 一斉付与を利用する */
	private UseSimultaneousGrant useSimultaneousGrant;

	/* 一斉付与日 */
	private Integer simultaneousGrandMonthDays;

	/* 備考 */
	private YearHolidayNote yearHolidayNote;

	private List<GrantCondition> grantConditions;

	@Override
	public void validate() {
		super.validate();

		// 一斉付与を利用する」がTRUEの場合は必ず一斉付与日を登録すること
		if (UseSimultaneousGrant.USE.equals(this.useSimultaneousGrant) && this.simultaneousGrandMonthDays == null) {
			throw new BusinessException("Msg_261");
		}

		for (int i = 0; i < this.grantConditions.size(); i++) {
			GrantCondition currentCondition = this.grantConditions.get(i);

			// 付与日数の計算対象」が「出勤率」の場合、条件値<=100
			if (CalculationMethod.WORKING_DAY.equals(this.calculationMethod)) {
				if (currentCondition.getConditionValue() != null && currentCondition.getConditionValue().v() > 100) {
					throw new BusinessException("Msg_262");
				}
			}
			
			// 付与日数の計算対象」が「労働日数」の場合、条件値<=366
			if (CalculationMethod.ATTENDENCE_RATE.equals(this.calculationMethod)) {
				if (currentCondition.getConditionValue() != null && currentCondition.getConditionValue().v() > 366) {
					throw new BusinessException("Msg_263");
				}
			}

			if (i == 0) {
				continue;
			}

			// 利用区分がTRUEの付与条件は、選択されている計算方法の条件値が入力されていること
			if (currentCondition.getUseConditionAtr() == UseConditionAtr.USE
					&& currentCondition.getConditionValue() == null) {
				throw new BusinessException("Msg_271");
			}

			// 条件NO：1、条件値 > 条件NO：2、条件値 > 条件NO：3、条件値 > 条件NO：4、条件値 > 条件NO：5、条件値
			if(this.grantConditions.get(i - 1).getConditionValue() != null) {
				Double firstValue = this.grantConditions.get(i - 1).getConditionValue().v();
				
				if(currentCondition.getConditionValue() != null) {
					Double secondValue = currentCondition.getConditionValue().v();
					
					if (firstValue <= secondValue && currentCondition.getUseConditionAtr() == UseConditionAtr.USE) {
						throw new BusinessException("Msg_264");
					}
				}				
			}
		}
	}

	public GrantHdTblSet(String companyId, YearHolidayCode yearHolidayCode, YearHolidayName yearHolidayName,
			CalculationMethod calculationMethod, StandardCalculation standardCalculation,
			UseSimultaneousGrant useSimultaneousGrant, int simultaneousGrandMonthDays, YearHolidayNote yearHolidayNote,
			List<GrantCondition> grantConditions) {

		this.companyId = companyId;
		this.yearHolidayCode = yearHolidayCode;
		this.yearHolidayName = yearHolidayName;
		this.calculationMethod = calculationMethod;
		this.standardCalculation = standardCalculation;
		this.useSimultaneousGrant = useSimultaneousGrant;
		this.simultaneousGrandMonthDays = simultaneousGrandMonthDays;
		this.yearHolidayNote = yearHolidayNote;
		this.grantConditions = grantConditions;
	}

	public static GrantHdTblSet createFromJavaType(String companyId, String yearHolidayCode, String yearHolidayName,
			int calculationMethod, int standardCalculation, int useSimultaneousGrant, int simultaneousGrandMonthDays,
			String yearHolidayNote, List<GrantCondition> grantConditions) {
		return new GrantHdTblSet(companyId, new YearHolidayCode(yearHolidayCode), new YearHolidayName(yearHolidayName),
				EnumAdaptor.valueOf(calculationMethod, CalculationMethod.class),
				EnumAdaptor.valueOf(standardCalculation, StandardCalculation.class),
				EnumAdaptor.valueOf(useSimultaneousGrant, UseSimultaneousGrant.class), simultaneousGrandMonthDays,
				new YearHolidayNote(yearHolidayNote), grantConditions);
	}
	
	/**
	 * 日数と出勤率から年休付与テーブルを取得する
	 * @param attendanceRate 出勤率
	 * @param prescribedDays 所定日数
	 * @param workingDays 労働日数
	 * @param deductedDays 控除日数
	 * @return 付与条件
	 */
	// 2018.7.27 add shuichi_ishida
	public Optional<GrantCondition> getGrantCondition(
			Double attendanceRate,
			Double prescribedDays,
			Double workingDays,
			Double deductedDays){
	
		// 計算方法をチェックする
		double criteria = attendanceRate;
		if (this.calculationMethod == CalculationMethod.WORKING_DAY){
			criteria = workingDays + deductedDays;
		}
		
		// 付与条件を取得
		GrantCondition target = null;
		this.grantConditions.sort((a, b) -> a.getConditionNo() - b.getConditionNo());
		// fix bug 113793
		List<GrantCondition> lstGrant = this.grantConditions;
		for (val grantCondition : lstGrant){
			if (grantCondition.getUseConditionAtr() != UseConditionAtr.USE) continue;
			if (grantCondition.getConditionValue() == null) break;
			
			// 基準値と付与条件．条件値を比較
			if (criteria >= grantCondition.getConditionValue().v().doubleValue()) {
				target = grantCondition;
				break;
			}
		}
		
		// 付与条件を返す
		return Optional.ofNullable(target);
	}
}
