package nts.uk.shr.infra.file.report.aspose.cells;

import java.io.InputStream;
import java.io.OutputStream;

import com.aspose.cells.ICellsDataTable;
import com.aspose.cells.SaveFormat;
import com.aspose.cells.Workbook;
import com.aspose.cells.WorkbookDesigner;

import lombok.Getter;

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
