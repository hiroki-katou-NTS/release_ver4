package nts.uk.ctx.pr.core.dom.personalinfo.employmentcontract;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;
import nts.gul.util.Range;
import nts.uk.ctx.core.dom.company.CompanyCode;
import nts.uk.shr.com.primitive.PersonId;

/**
 * Aggregate Root: Personal employment contract
 *
 */
public class PersonalEmploymentContract extends AggregateRoot {

	/**
	 * Payroll system.
	 */
	@Getter
	private PayrollSystem payrollSystem;

	/**
	 * Personal Id
	 */
	@Getter
	private PersonId personId;

	/**
	 * Company Code
	 */
	@Getter
	private CompanyCode companyCode;
	
	/**
	 * Range date: from - to
	 */
	@Getter
	private Range<GeneralDate> rangeDate;
	
	public PersonalEmploymentContract(PayrollSystem payrollSystem, PersonId personId, CompanyCode companyCode,
			GeneralDate startDate, GeneralDate endDate) {
		super();
		this.payrollSystem = payrollSystem;
		this.personId = personId;
		this.companyCode = companyCode;
		rangeDate = Range.between(startDate, endDate);
	}

	/**
	 * Create instance using Java type parameters.
	 * 
	 * @return PersonalEmploymentContract
	 */
	public static PersonalEmploymentContract createFromJavaType(int payrollSystem, String personId, String companyCode,
			GeneralDate startDate, GeneralDate endDate) {
		return new PersonalEmploymentContract(EnumAdaptor.valueOf(payrollSystem, PayrollSystem.class),
				new PersonId(personId), new CompanyCode(companyCode), startDate, endDate);
	}

	/**
	 * Payroll system by DAY || HOURS
	 * 
	 * @return true if payroll system = 2(DAY) || 3(HOURS) else false
	 */
	public boolean isPayrollSystemDailyOrDay() {
		return this.payrollSystem == PayrollSystem.DAY || this.payrollSystem == PayrollSystem.HOURS;
	}
	
	/**
	 * Payroll system by MONTHLY || DAILY
	 * 
	 * @return true if payroll system = 0(MONTHLY) || 1(DAILY) else false
	 */
	public boolean isPayrollSystemDailyOrMonthly() {
		return this.payrollSystem == PayrollSystem.MONTHLY || this.payrollSystem == PayrollSystem.DAILY;
	}
}
