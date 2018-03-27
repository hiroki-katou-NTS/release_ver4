package nts.uk.shr.infra.file.report.aspose.cells;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.enterprise.inject.spi.CDI;

import com.aspose.cells.Encoding;
import com.aspose.cells.ICellsDataTable;
import com.aspose.cells.PageSetup;
import com.aspose.cells.SaveFormat;
import com.aspose.cells.SaveOptions;
import com.aspose.cells.TxtSaveOptions;
import com.aspose.cells.TxtValueQuoteType;
import com.aspose.cells.Workbook;
import com.aspose.cells.WorkbookDesigner;
import com.aspose.cells.WorksheetCollection;

import lombok.Getter;
import nts.uk.shr.infra.i18n.resource.I18NResourceType;
import nts.uk.shr.infra.i18n.resource.I18NResourcesForUK;

public class AsposeCellsReportContext implements AutoCloseable {
	
	@Getter
	private final InputStream templateFile;

	@Getter
	private final Workbook workbook;
	
	@Getter
	private final WorkbookDesigner designer;
	
	public AsposeCellsReportContext(InputStream templateFile) {
		
		try {
			this.templateFile = templateFile;
			this.workbook = new Workbook(this.templateFile);
			this.designer = new WorkbookDesigner(this.workbook);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		
		// TODO: set localized texts
	}
	
	public AsposeCellsReportContext(InputStream templateFile, String reportId) {
		
		try {
			this.templateFile = templateFile;
			this.workbook = new Workbook(this.templateFile);
			this.designer = new WorkbookDesigner(this.workbook);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		
		I18NResourcesForUK i18n = CDI.current().select(I18NResourcesForUK.class).get();
		Map<String, ?> items = i18n.loadForUserByResourceType(I18NResourceType.ITEM_NAME);
		if (!items.isEmpty()) this.setDataSource("I18N", new SingleMapDataSource(items));
	}
	
	public AsposeCellsReportContext(String reportId) {
		
		try {
			this.templateFile = null;
			this.workbook = new Workbook();
			this.designer = new WorkbookDesigner(this.workbook);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		
		I18NResourcesForUK i18n = CDI.current().select(I18NResourcesForUK.class).get();
		Map<String, ?> items = i18n.loadForUserByResourceType(I18NResourceType.ITEM_NAME);
		if (!items.isEmpty()) this.setDataSource("I18N", new SingleMapDataSource(items));
	}
	
	public void setDataSource(String nameOfVariable, ICellsDataTable dataTable) {
		this.designer.setDataSource(nameOfVariable, dataTable);
	}
	
	public void setDataSource(String nameOfVariable, Object data) {
		this.designer.setDataSource(nameOfVariable, data);
	}
	
	public void setHeader(int section, String content) {
		WorksheetCollection sheets = workbook.getWorksheets();
		if (sheets.getCount() == 0) return;
		PageSetup pageSetup = sheets.get(0).getPageSetup();
		Pattern pattern = Pattern.compile("(.*)\\#\\{([\\w_\\d]+)\\}(.*)", Pattern.DOTALL);
		Matcher matcher = pattern.matcher(content);
		if (matcher.matches()) {
			String resourceId = matcher.group(2);
			I18NResourcesForUK i18n = CDI.current().select(I18NResourcesForUK.class).get();
			StringBuffer sb = new StringBuffer(matcher.group(1));
			i18n.getRawContent(resourceId).ifPresent(resource -> {
				sb.append(resource).append(matcher.group(3));
				pageSetup.setHeader(section, sb.toString());
			});
			return;
		}
		pageSetup.setHeader(section, content);
	}
	
	public void processDesigner() {
		try {
			this.designer.process();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void saveAsPdf(OutputStream outputStream) {
		try {
			this.workbook.save(outputStream, SaveFormat.PDF);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void saveAsExcel(OutputStream outputStream) {
		try {
			this.workbook.save(outputStream, SaveFormat.XLSX);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void saveAsCSV(OutputStream outputStream) {
		try {
			TxtSaveOptions opts = new TxtSaveOptions(SaveFormat.CSV);
			opts.setQuoteType(TxtValueQuoteType.ALWAYS);
			opts.setEncoding(Encoding.getUTF8());
			this.workbook.save(outputStream, opts);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void saveWithOtherOption(OutputStream outputStream, SaveOptions saveOptions) {
		try {
			this.workbook.save(outputStream, saveOptions);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void close() throws Exception {
		if(this.templateFile != null){
			this.templateFile.close();	
		}
	}
	
}
