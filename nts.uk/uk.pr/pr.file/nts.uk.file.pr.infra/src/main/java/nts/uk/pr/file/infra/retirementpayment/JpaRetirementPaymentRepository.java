/**
 * 
 */
package nts.uk.pr.file.infra.retirementpayment;

import java.util.List;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.file.pr.app.export.retirementpayment.RetirementPaymentRepository;
import nts.uk.file.pr.app.export.retirementpayment.data.RetirePayItemDto;
import nts.uk.file.pr.app.export.retirementpayment.data.RetirementPaymentDto;

/**
 * @author hungnm
 *
 */
@Stateless
public class JpaRetirementPaymentRepository extends JpaRepository implements RetirementPaymentRepository {

	private static final String GET_ALL_BY_BASE_RANGE_DATE;

	private static final String GET_ALL_RETIRE_PAY_ITEMS;

	private static final String RETIREMENT_PAYMENT_DTO = RetirementPaymentDto.class.getName();

	private static final String RETIRE_PAY_ITEM_DTO = RetirePayItemDto.class.getName();

	static {
		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT NEW ");
		builderString.append(RETIREMENT_PAYMENT_DTO);
		builderString.append(
				"(a.reportQredtRetirementPaymentPK.pid, a.trialPeriodSet, a.exclusionYears, a.totalPaymentMoney, a.incomeTaxMoney, a.cityTaxMoney, a.prefectureTaxMoney, a.deductionMoney1, a.deductionMoney2, a.deductionMoney3, a.totalDeductionMoney, a.actualRecieveMoney, a.statementMemo)");
		builderString.append("FROM ReportQredtRetirementPayment a ");
		builderString.append("WHERE a.reportQredtRetirementPaymentPK.ccd = :companyCode ");
		builderString.append("AND a.reportQredtRetirementPaymentPK.pid IN :personId ");
		builderString.append("AND a.reportQredtRetirementPaymentPK.payDate >= :startYmd ");
		builderString.append("AND a.reportQredtRetirementPaymentPK.payDate <= :endYmd ");
		GET_ALL_BY_BASE_RANGE_DATE = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT NEW ");
		builderString.append(RETIRE_PAY_ITEM_DTO);
		builderString.append("(a.reportQremtRetirePayItemPK.itemCode, a.itemName, a.itemAbName)");
		builderString.append("FROM ReportQremtRetirePayItem a ");
		builderString.append("WHERE a.reportQremtRetirePayItemPK.ccd = :companyCode ");
		GET_ALL_RETIRE_PAY_ITEMS = builderString.toString();
	}

	@Override
	public List<RetirementPaymentDto> getRetirementPayment(String companyCode, List<String> personId, GeneralDate startDate,
			GeneralDate endDate) {
		List<RetirementPaymentDto> reportQredtRetirementPayment = this.queryProxy()
				.query(GET_ALL_BY_BASE_RANGE_DATE, RetirementPaymentDto.class).setParameter("companyCode", companyCode)
				.setParameter("personId", personId).setParameter("startYmd", startDate).setParameter("endYmd", endDate)
				.getList();
		return reportQredtRetirementPayment;
	}

	@Override
	public List<RetirePayItemDto> getListRetirePayItem(String companyCode) {
		return this.queryProxy().query(GET_ALL_RETIRE_PAY_ITEMS, RetirePayItemDto.class)
				.setParameter("companyCode", companyCode).getList();
	}

}
