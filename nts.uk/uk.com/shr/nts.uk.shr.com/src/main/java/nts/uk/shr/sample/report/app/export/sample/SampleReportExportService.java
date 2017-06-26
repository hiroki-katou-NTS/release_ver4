package nts.uk.shr.sample.report.app.export.sample;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.layer.app.file.export.ExportService;
import nts.arc.layer.app.file.export.ExportServiceContext;

@Stateless
public class SampleReportExportService extends ExportService<SampleReportQuery> {
	
	@Inject
	private SampleReportDataRepository repository;

	@Inject
	private SampleReportGenerator generator;

	@Override
	protected void handle(ExportServiceContext<SampleReportQuery> context) {

		// get query parameters
		String value = context.getQuery().getValue();
		value.toString();
		
		// create data source
		val items = this.repository.getItems();
		val dataSource = new SampleReportDataSource(items);
		
		// invoke generator
		this.generator.generate(context.getGeneratorContext(), dataSource);
	}
}
