package nts.uk.ctx.pr.core.dom.rule.employment.averagepay;


import lombok.Getter;
import nts.arc.error.BusinessException;
import nts.arc.layer.dom.AggregateRoot;

/**
 * 平均賃金計算設定マスタ
 * Average wage calculation setting master
 * @author Doan Duy Hung
 *
 */
public class AveragePay extends AggregateRoot {
	@Getter
	private String companyCode;
	
	@Getter
	private RoundTimingSet roundTimingSet;
	
	@Getter
	private AttendDayGettingSet attendDayGettingSet;
	
	@Getter
	private RoundDigitSet roundDigitSet;
	
	@Getter
	private ExceptionPayRate exceptionPayRate;
	
	@Override
	public void validate() {
		if(this.exceptionPayRate.v()==null) {
			throw new BusinessException("ER001");
		}
		super.validate();
	} 
	
	public AveragePay(String companyCode, RoundTimingSet roundTimingSet, AttendDayGettingSet attendDayGettingSet,
			RoundDigitSet roundDigitSet, ExceptionPayRate exceptionPayRate) {
		super();
		this.companyCode = companyCode;
		this.roundTimingSet = roundTimingSet;
		this.attendDayGettingSet = attendDayGettingSet;
		this.roundDigitSet = roundDigitSet;
		this.exceptionPayRate = exceptionPayRate;
	}
	
	/**
	 * check attend day select
	 * @return attend day statement
	 */
	public boolean isAttenDayStatementItem() {
		 return AttendDayGettingSet.SELECT_FROM_STATEMENT_ITEM.equals(this.attendDayGettingSet);
	}
}
