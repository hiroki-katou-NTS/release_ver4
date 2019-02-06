package nts.uk.shr.sample.report.ws.sample;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.app.file.export.ExportServiceResult;
import nts.uk.shr.infra.file.csv.CSVExportService;
import nts.uk.shr.infra.file.csv.CSVFileData;
import nts.uk.shr.sample.aspose.SampleAsposeExportService;
import nts.uk.shr.sample.report.app.export.sample.SampleReportExportService;
import nts.uk.shr.sample.report.app.export.sample.SampleReportQuery;

@Path("/sample/report")
@Produces("application/json")
public class SampleReportWebService {

	@Inject
	private SampleReportExportService exportService;
	
	@Inject
	private SampleAsposeExportService asposeService;
	
	@Inject
	private CSVExportService csvService;
	
	@POST
	@Path("generate")
	public ExportServiceResult generate(SampleReportQuery query) {
		return this.exportService.start(query);
	}
	
	@POST
	@Path("stripe")
	public ExportServiceResult stripe() {
		return this.asposeService.start(null);
	}
	
	@POST
	@Path("csv")
	public ExportServiceResult csv() {
		CSVFileData  query = new CSVFileData();
		List<String> headers = IntStream.range(1,  10).mapToObj(c -> "Column " + c).collect(Collectors.toList());
		query.setFileName("Sample.CSV");
		query.setHeaders(headers);
		List<Map<String, Object>> data = IntStream.range(1, 1000).mapToObj(r -> {
			return headers.stream().collect(Collectors.toMap(h -> h, h -> (Object) (h + " Value at row " + r)));
		}).collect(Collectors.toList());
		query.setDatas(data);
		return this.csvService.start(query);
	}
}
