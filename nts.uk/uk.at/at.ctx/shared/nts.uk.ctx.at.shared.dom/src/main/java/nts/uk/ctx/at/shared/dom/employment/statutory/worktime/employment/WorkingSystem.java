package nts.uk.ctx.at.shared.dom.employment.statutory.worktime.employment;

/**
 * 労働制
 * @author keisuke_hoshina
 *
 */
public enum WorkingSystem {
	RegularWork,
	FlexTimeWork,
	VariableWorkingTimeWork,
	ExcludedWorkingCalculate,;


	/**
	 * 就業計算対象外であるか判定する
	 * @return　就業計算対象外である
	 */
	public boolean isExcludedWorkingCalculate() {
		return this.equals(ExcludedWorkingCalculate);
	}
	
	/**
	 * 変形労働であるか判定する
	 * @return　変形労働である
	 */
	public boolean isVariableWorkingTimeWork() {
		return this.equals(VariableWorkingTimeWork);
	}
}
