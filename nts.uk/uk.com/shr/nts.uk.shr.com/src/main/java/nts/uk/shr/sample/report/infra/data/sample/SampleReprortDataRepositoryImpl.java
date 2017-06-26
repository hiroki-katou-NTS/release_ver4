package nts.uk.shr.sample.report.infra.data.sample;

import java.util.Arrays;
import java.util.List;

import javax.ejb.Stateless;

import nts.uk.shr.sample.report.app.export.sample.SampleReportDataItem;
import nts.uk.shr.sample.report.app.export.sample.SampleReportDataRepository;

@Stateless
public class SampleReprortDataRepositoryImpl implements SampleReportDataRepository {
	public List<SampleReportDataItem> getItems() {
		return Arrays.asList(
				new SampleReportDataItem("001", "test"),
				new SampleReportDataItem("002", "帳票データ"),
				new SampleReportDataItem("003", "aaaaaaaaaaaaaaaaaaaaaaaaa"));
	}
	
}
