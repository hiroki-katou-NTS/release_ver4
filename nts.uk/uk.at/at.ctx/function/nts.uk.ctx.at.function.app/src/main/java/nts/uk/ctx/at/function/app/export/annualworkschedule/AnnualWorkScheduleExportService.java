package nts.uk.ctx.at.function.app.export.annualworkschedule;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.app.file.export.ExportService;
import nts.arc.layer.app.file.export.ExportServiceContext;
import nts.uk.ctx.at.function.dom.adapter.standardtime.AgreementOperationSettingAdapter;
import nts.uk.ctx.at.function.dom.adapter.standardtime.AgreementOperationSettingImport;
import nts.uk.ctx.at.function.dom.adapter.standardtime.TimeOverLimitTypeImport;
import nts.uk.ctx.at.function.dom.annualworkschedule.Employee;
import nts.uk.ctx.at.function.dom.annualworkschedule.export.AnnualWorkScheduleGenerator;
import nts.uk.ctx.at.function.dom.annualworkschedule.export.AnnualWorkScheduleRepository;
import nts.uk.ctx.at.function.dom.annualworkschedule.export.ExcludeEmp;
import nts.uk.ctx.at.function.dom.annualworkschedule.export.ExportData;
import nts.uk.ctx.at.function.dom.annualworkschedule.export.PrintFormat;
import nts.uk.ctx.at.shared.dom.common.Year;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class AnnualWorkScheduleExportService extends ExportService<AnnualWorkScheduleExportQuery> {
	@Inject
	private AnnualWorkScheduleRepository repostory;
	@Inject
	private AnnualWorkScheduleGenerator generator;
	@Inject
	private AgreementOperationSettingAdapter agreementOperationSettingAdapter;

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	protected void handle(ExportServiceContext<AnnualWorkScheduleExportQuery> context) {
		String companyId = AppContexts.user().companyId();
		AnnualWorkScheduleExportQuery query = context.getQuery();
		PrintFormat printFormat = EnumAdaptor.valueOf(query.getPrintFormat(), PrintFormat.class);
		ExcludeEmp excludeEmp = EnumAdaptor.valueOf(query.getExcludeEmp(), ExcludeEmp.class);
		List<Employee> employees = query.getEmployees().stream()
				.map(m -> new Employee(m.getEmployeeId(), m.getCode(), m.getName(), m.getWorkplaceName()))
				.collect(Collectors.toList());
		Year fiscalYear = null;
		YearMonth startYm = null;
		YearMonth endYm = null;
		Integer monthLimit;
		// ドメインモデル「３６協定運用設定」を取得する
		Optional<AgreementOperationSettingImport> agreementSetObj = agreementOperationSettingAdapter.find(companyId);
		if (PrintFormat.AGREEMENT_36.equals(printFormat)) {
			fiscalYear = new Year(Integer.parseInt(query.getFiscalYear()));
			startYm = this.getStartYearMonth(agreementSetObj, fiscalYear);
			endYm = startYm.plusMonths(11);
		} else {
			startYm = YearMonth.parse(query.getStartYearMonth(), DateTimeFormatter.ofPattern("uuuu/MM"));
			endYm = YearMonth.parse(query.getEndYearMonth(), DateTimeFormatter.ofPattern("uuuu/MM"));
		}

		// get ３６協定超過上限回数
		if (agreementSetObj.isPresent()) {
			monthLimit = agreementSetObj.get().getNumberTimesOverLimitType().value;
		} else {
			monthLimit = TimeOverLimitTypeImport.ZERO_TIMES.value;
		}

		ExportData data = this.repostory.outputProcess(companyId, query.getSetItemsOutputCd(), fiscalYear, startYm,
				endYm, employees, printFormat, query.getBreakPage(), excludeEmp, monthLimit);
		val dataSetter = context.getDataSetter();
		List<String> employeeError = data.getEmployeeError();
		if (!employeeError.isEmpty()) {
			dataSetter.setData("messageId", "Msg_1344");
			dataSetter.setData("totalEmpErr", employeeError.size());
			for (int i = 0; i < employeeError.size(); i++) {
				dataSetter.setData("empErr" + i, employeeError.get(i));
			}
		}
		// invoke generator
		this.generator.generate(context.getGeneratorContext(), data);
	}

	/**
	 * Get startYm, endYm
	 * 
	 * @param startYm
	 *            output
	 * @param endYm
	 *            output
	 */
	private YearMonth getStartYearMonth(Optional<AgreementOperationSettingImport> agreementSetObj, Year fiscalYear) {
		String month = "01";
		// 「36協定運用設定」．起算月から年度の期間を求める
		if (agreementSetObj.isPresent()) {
			switch (agreementSetObj.get().getStartingMonth()) {
			case JANUARY:
				month = "01";
				break;
			case FEBRUARY:
				month = "02";
				break;
			case MARCH:
				month = "03";
				break;
			case APRIL:
				month = "04";
				break;
			case MAY:
				month = "05";
				break;
			case JUNE:
				month = "06";
				break;
			case JULY:
				month = "07";
				break;
			case AUGUST:
				month = "08";
				break;
			case SEPTEMBER:
				month = "09";
				break;
			case OCTOBER:
				month = "10";
				break;
			case NOVEMBER:
				month = "11";
				break;
			case DECEMBER:
				month = "12";
				break;
			}
		}
		StringBuilder ym = new StringBuilder();
		ym.append(fiscalYear.toString());
		ym.append('/');
		ym.append(month);
		return YearMonth.parse(ym.toString(), DateTimeFormatter.ofPattern("uuuu/MM"));
	}
}
