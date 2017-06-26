/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.pr.app.export.wageledger;

import java.util.Arrays;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.layer.app.file.export.ExportService;
import nts.arc.layer.app.file.export.ExportServiceContext;
import nts.arc.time.GeneralDate;
import nts.uk.file.pr.app.export.wageledger.WageLedgerReportQuery.LayoutType;
import nts.uk.file.pr.app.export.wageledger.data.WLNewLayoutReportData;
import nts.uk.file.pr.app.export.wageledger.data.WLOldLayoutReportData;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class WageLedgerReportSevice.
 */
@Stateless
public class WageLedgerReportSevice extends ExportService<WageLedgerReportQuery>{
	
	/** The repository. */
	@Inject
	private WageLedgerDataRepository repository;
	
	/** The generator. */
	@Inject
	private WLOldLayoutReportGenerator oldGenerator;
	
	/** The new generator. */
	@Inject
	private WLNewLayoutReportGenerator newGenerator;

	/* (non-Javadoc)
	 * @see nts.arc.layer.app.file.export.ExportService#handle(nts.arc.layer.app.file.export.ExportServiceContext)
	 */
	@Override
	protected void handle(ExportServiceContext<WageLedgerReportQuery> context) {
		WageLedgerReportQuery query = context.getQuery();
		String companyCode = AppContexts.user().companyCode();
		
		// TODO: Validate query employee ids and base date.
		query.baseDate = GeneralDate.today();
		query.employeeIds = Arrays.asList("99900000-0000-0000-0000-000000000002",
				"99900000-0000-0000-0000-000000000003");
		if (!this.repository.hasReportData(companyCode, query)) {
			throw new BusinessException(new RawErrorMessage("対象データがありません。"));
		}
		
		// Generate report.
		if (query.layoutType == LayoutType.NewLayout) {
			// Query Data.
			query.employeeIds = Arrays.asList("99900000-0000-0000-0000-000000000002");
			List<WLNewLayoutReportData> reportData = this.repository.findReportDatas(companyCode, query,
					WLNewLayoutReportData.class);
			this.newGenerator.generate(context.getGeneratorContext(), reportData, query);
			return;
		}
		// Query Data.
		List<WLOldLayoutReportData> reportData = this.repository.findReportDatas(companyCode, query,
				WLOldLayoutReportData.class);
		this.oldGenerator.generate(context.getGeneratorContext(), reportData, query);
	}
}
