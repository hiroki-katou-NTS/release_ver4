package nts.uk.file.pr.app.export.retirementpayment;

import java.util.List;

import nts.arc.layer.infra.file.export.FileGeneratorContext;
import nts.uk.file.pr.app.export.retirementpayment.data.RetirePayItemDto;
import nts.uk.file.pr.app.export.retirementpayment.data.RetirementPaymentReportData;

public interface RetirementPaymentReportGenerator {
	/**
	 * Generate.
	 *
	 * @param generatorContext the generator context
	 * @param dataSource the data source
	 */
	void generate(FileGeneratorContext generatorContext, List<RetirementPaymentReportData> dataSource, List<RetirePayItemDto> lstRetirePayItemDto);	
}
