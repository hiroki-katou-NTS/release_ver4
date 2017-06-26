package nts.uk.shr.infra.file.report.aspose.cells;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.enterprise.inject.spi.CDI;

import com.aspose.cells.ICellsDataTable;
import com.aspose.cells.SaveFormat;
import com.aspose.cells.Workbook;
import com.aspose.cells.WorkbookDesigner;

import lombok.Getter;
import nts.arc.i18n.custom.IInternationalization;

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
		
		IInternationalization i18n = CDI.current().select(IInternationalization.class).get();
		Map<String, Object> items = i18n.getReportItems(reportId);
		if (!items.isEmpty()) this.setDataSource("I18N", new SingleMapDataSource(items));
	}
	
	public void setDataSource(String nameOfVariable, ICellsDataTable dataTable) {
		this.designer.setDataSource(nameOfVariable, dataTable);
	}
	
	public void setDataSource(String nameOfVariable, Object data) {
		this.designer.setDataSource(nameOfVariable, data);
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

	@Override
	public void close() throws Exception {
		this.templateFile.close();
	}
	
}
