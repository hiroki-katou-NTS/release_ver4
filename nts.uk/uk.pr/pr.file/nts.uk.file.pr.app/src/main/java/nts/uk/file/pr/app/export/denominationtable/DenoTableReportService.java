/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.pr.app.export.denominationtable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.layer.app.file.export.ExportService;
import nts.arc.layer.app.file.export.ExportServiceContext;
import nts.arc.time.GeneralDate;
import nts.uk.file.pr.app.export.denominationtable.data.DenoTableHeaderData;
import nts.uk.file.pr.app.export.denominationtable.data.Denomination;
import nts.uk.file.pr.app.export.denominationtable.data.DenominationTableData;
import nts.uk.file.pr.app.export.denominationtable.data.EmployeeData;
import nts.uk.file.pr.app.export.denominationtable.query.DenoTableReportQuery;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.japanese.JapaneseDate;
import nts.uk.shr.com.time.japanese.JapaneseErasProvider;

/**
 * The Class DenoTableReportService.
 */
@Stateless
public class DenoTableReportService extends ExportService<DenoTableReportQuery> {

	/** The generator. */
	@Inject
	private DenoTableReportGenerator generator;

	/** The repository. */
	@Inject
	private DenoTableRepository repository;
	
	/** The Constant TWO_THOUSANDS. */
	private static final int TWO_THOUSANDS = 2000;
	
	/** The japanese provider. */
    @Inject
    private JapaneseErasProvider japaneseProvider;

	/*
	 * (non-Javadoc)
	 * @see
	 * nts.arc.layer.app.file.export.ExportService#handle(nts.arc.layer.app.file
	 * .export.ExportServiceContext)
	 */
	@Override
	protected void handle(ExportServiceContext<DenoTableReportQuery> context) {

		// GET QUERY
		DenoTableReportQuery query = context.getQuery();

		// Validate.
		this.validateData(query);
		
		String[] personIds = { "99900000-0000-0000-0000-000000000001", "99900000-0000-0000-0000-000000000002",
				"99900000-0000-0000-0000-000000000003", "99900000-0000-0000-0000-000000000004",
				"99900000-0000-0000-0000-000000000005", "99900000-0000-0000-0000-000000000006",
				"99900000-0000-0000-0000-000000000007", "99900000-0000-0000-0000-000000000008",
				"99900000-0000-0000-0000-000000000009", "99900000-0000-0000-0000-000000000010"};
			query.setPIdList(Arrays.asList(personIds));

		// GET ITEM DATA LIST
		List<EmployeeData> items = this.repository.getItems(AppContexts.user().companyCode(), query);
		
		// DIVISION DENOMINATION
		items.stream().forEach(emp -> {
			emp.setDenomination(this.divisionDeno(emp.getPaymentAmount(), context.getQuery()));
		});

		//  CONVERT YEARMONTH JAPANESE 
		StringBuilder japanYM = new StringBuilder("【処理年月：");
		japanYM.append(convertYearMonthJP(query.getYearMonth()));

		// CREATE HEADER OBJECT
		DenoTableHeaderData headerData = DenoTableHeaderData.builder()
				.departmentInfo("【部門： A部門～C部門（3部門）】")
				.categoryInfo("【分類： 正社員～アルバイト（５分類）】")
				.positionInfo("【職位： 部長～一般（12職位）】")
				.targetYearMonth(japanYM.toString())
				.build();

		// CREATE DATA SOURCE
		val dataSource = DenominationTableData.builder()
				.salaryChartHeader(headerData)
				.employeeList(items)
				.build();

		// GENERATE REPORT
		this.generator.generate(context.getGeneratorContext(), dataSource, query);
	}
	
	private void validateData(DenoTableReportQuery query) {
		if (query.getYearMonth() == null) {
			throw new RuntimeException("Target Year is Empty");
		}
		if (query.getYearMonth() <= 190001 || query.getYearMonth() >= 999912) {
			throw new RuntimeException("Target Year is not in range");
		}
		if (query.getIsPrintDepHierarchy() && query.getSelectedLevels().isEmpty()) {
			throw new RuntimeException("1~9階層 が選択されていません。");
		}
		if (query.getIsPrintDepHierarchy() && query.getSelectedLevels().size() > 5) {
			throw new RuntimeException("部門階層が正しく指定されていません。");
		}
		if (!query.getIsPrintDepHierarchy() && query.getSelectedBreakPageCode() == 4) {
			throw new RuntimeException("設定が正しくありません。");
		}
		if (query.getIsPrintDepHierarchy() && query.getSelectedBreakPageCode() == 4
				&& query.getSelectedLevels().indexOf(query.getSelectedBreakPageHierarchyCode()) < 0) {
			throw new RuntimeException("設定が正しくありません。");
		}
	}
	
	/**
	 * Convert year month JP.
	 *
	 * @param yearMonth the year month
	 * @return the string
	 */
	private String convertYearMonthJP(Integer yearMonth) {
		String firstDay = "01";
		String tmpDate = yearMonth.toString().concat(firstDay);
		String dateFormat = "yyyyMMdd";
		GeneralDate generalDate = GeneralDate.fromString(tmpDate, dateFormat);
		JapaneseDate japaneseDate = this.japaneseProvider.toJapaneseDate(generalDate);
		return japaneseDate.era() + japaneseDate.year() + "年 " + japaneseDate.month() + "月度給与】";
	}
	
	/**
	 * Division deno.
	 *
	 * @param paymentAmount the payment amount
	 * @param query the query
	 * @return the map
	 */
	private Map<Denomination, Long> divisionDeno(Double paymentAmount, DenoTableReportQuery query) {
		Map<Denomination, Long> deno = new HashMap<>();
		Double amount = paymentAmount;
		for (Denomination d : Denomination.values()) {
			if (amount >= d.deno) {
				if((query.getSelectedUse2000yen() == 0) && (d.deno == TWO_THOUSANDS)){
					deno.put(d, 0L);
					continue;
				}
				Long quantity = (long) (amount / d.deno);
				deno.put(d, quantity);
				amount = amount % d.deno;
			} 
			else {
				deno.put(d, 0L);
			}
		}
		return deno;
	}
}
