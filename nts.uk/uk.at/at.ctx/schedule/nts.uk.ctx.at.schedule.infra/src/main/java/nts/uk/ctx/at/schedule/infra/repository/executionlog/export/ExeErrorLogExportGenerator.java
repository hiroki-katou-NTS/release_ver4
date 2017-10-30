package nts.uk.ctx.at.schedule.infra.repository.executionlog.export;

import java.util.Arrays;
import java.util.List;

import javax.ejb.Stateless;

import com.aspose.cells.BackgroundType;
import com.aspose.cells.BorderType;
import com.aspose.cells.Cell;
import com.aspose.cells.CellBorderType;
import com.aspose.cells.Cells;
import com.aspose.cells.Color;
import com.aspose.cells.PageSetup;
import com.aspose.cells.Style;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;
import com.aspose.cells.WorksheetCollection;

import lombok.val;
import nts.arc.layer.infra.file.export.FileGeneratorContext;
import nts.uk.ctx.at.schedule.app.find.executionlog.dto.ScheduleErrorLogDto;
import nts.uk.ctx.at.schedule.app.find.executionlog.export.ExeErrorLogGenerator;
import nts.uk.ctx.at.schedule.app.find.executionlog.export.ExportData;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.infra.file.report.aspose.cells.AsposeCellsReportGenerator;

@Stateless
public class ExeErrorLogExportGenerator extends AsposeCellsReportGenerator implements ExeErrorLogGenerator {

	/** The Constant TEMPLATE_FILE. */
	private final String REPORT_ID = "CSV_GENERATOR";

	/** The Constant EXPORT_FILE_NAME. */
	private final String EXPORT_FILE_NAME = "KSC001.csv";

	/** The Constant EXTENSION_FILE. */
	private final String EXTENSION_FILE = ".csv";

	/** The Constant SHEET_NAME. */
	private final String SHEET_NAME = "Sheet 1";

	/** The Constant PRINT_AREA. */
	private final String PRINT_AREA = "A1:F";

	/** The Constant DEFAULT_VALUE. */
	private final int DEFAULT_VALUE = 0;

	/** The Constant INDEX_HEADER. */
	private final int INDEX_HEADER = 0;

	/** The Constant INDEX_CONTENT. */
	private final int INDEX_CONTENT = 1;

	@Override
	public void generate(FileGeneratorContext fileContext, ExportData exportData) {
		try (val reportContext = this.createEmptyContext(REPORT_ID)) {
			Workbook workbook = reportContext.getWorkbook();
			WorksheetCollection worksheets = workbook.getWorksheets();

			// create new sheet
			createNewSheet(worksheets, exportData);

			reportContext.getDesigner().setWorkbook(workbook);
			reportContext.processDesigner();

			// save csv file
			reportContext.saveAsCSV(this.createNewFile(fileContext, this.getFileName()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Creates the new sheet.
	 *
	 * @param worksheets
	 *            the worksheets
	 * @param exportData
	 *            the export data
	 */
	private void createNewSheet(WorksheetCollection worksheets, ExportData exportData) {
		// get worksheet
		Worksheet worksheet = worksheets.get(DEFAULT_VALUE);

		// set sheet name
		worksheet.setName(SHEET_NAME);

		// begin write data
		this.writeData(worksheet, exportData);

		// setting page
		this.settingPage(worksheet, exportData.lstError.size());
	}

	/**
	 * Setting page.
	 *
	 * @param worksheet
	 *            the worksheet
	 * @param totalRow
	 *            the total row
	 */
	private void settingPage(Worksheet worksheet, int totalRow) {
		PageSetup pageSetup = worksheet.getPageSetup();
		int idxHeader = 1;
		String printArea = PRINT_AREA + (idxHeader + totalRow);
		pageSetup.setPrintArea(printArea);
		// pageSetup.setHeader(0,"&\"IPAPGothic\"&11 " +
		// header.getNameCompany());
	}

	/**
	 * Write data.
	 *
	 * @param worksheet
	 *            the worksheet
	 * @param exportData
	 *            the export data
	 */
	private void writeData(Worksheet worksheet, ExportData exportData) {
		// write header
		this.writeHeader(worksheet, exportData.lstHeader);

		// write content error
		this.writeContent(worksheet, exportData.lstError);
	}

	/**
	 * Write header.
	 *
	 * @param worksheet
	 *            the worksheet
	 * @param lstHeader
	 *            the lst header
	 */
	private void writeHeader(Worksheet worksheet, List<String> lstHeader) {
		Cells cells = worksheet.getCells();
		for (int i = 0; i < lstHeader.size(); i++) {
			Cell cell = cells.get(INDEX_HEADER, i);
			this.setStyleCell(cell);
			cell.setValue(lstHeader.get(i));
		}
	}

	/**
	 * Write content.
	 *
	 * @param worksheet
	 *            the worksheet
	 * @param lstError
	 *            the lst error
	 */
	private void writeContent(Worksheet worksheet, List<ScheduleErrorLogDto> lstError) {
		Cells cells = worksheet.getCells();
		int indexRow = INDEX_CONTENT;
		for (ScheduleErrorLogDto record : lstError) {
			this.setDataRecord(cells, indexRow, record);
			indexRow++;
		}
	}

	/**
	 * Sets the data record.
	 *
	 * @param cells
	 *            the cells
	 * @param indexRow
	 *            the index row
	 * @param data
	 *            the data
	 */
	@SuppressWarnings("rawtypes")
	private void setDataRecord(Cells cells, int indexRow, ScheduleErrorLogDto data) {
		List lstValueColumn = this.convertObjectToList(data);
		for (int i = 0; i < lstValueColumn.size(); i++) {
			Cell cell = cells.get(indexRow, i);
			this.setStyleCell(cell);
			cell.setValue(lstValueColumn.get(i));
		}
	}

	/**
	 * Sets the style cell.
	 *
	 * @param cell
	 *            the new style cell
	 */
	private void setStyleCell(Cell cell) {
		// new style
		Style style = cell.getStyle();
		style.setPattern(BackgroundType.SOLID);
		style.setBorder(BorderType.TOP_BORDER, CellBorderType.THIN, Color.getBlack());
		style.setBorder(BorderType.BOTTOM_BORDER, CellBorderType.THIN, Color.getBlack());
		style.setBorder(BorderType.LEFT_BORDER, CellBorderType.THIN, Color.getBlack());
		style.setBorder(BorderType.RIGHT_BORDER, CellBorderType.THIN, Color.getBlack());

		// set style for cell
		cell.setStyle(style);
	}

	/**
	 * Gets the file name.
	 *
	 * @param employeeId
	 *            the employee id
	 * @return the file name
	 */
	private String getFileName() {
		String loginEmployeeCode = AppContexts.user().employeeCode();
		String rawFileName = this.getReportName(EXPORT_FILE_NAME);
		String fileName = rawFileName.substring(DEFAULT_VALUE, rawFileName.indexOf(EXTENSION_FILE));
		return String.format("%s_%s%s", fileName, loginEmployeeCode, EXTENSION_FILE);
	}

	/**
	 * Convert object to list.
	 *
	 * @param data
	 *            the data
	 * @return the list
	 */
	@SuppressWarnings({ "rawtypes" })
	private List convertObjectToList(ScheduleErrorLogDto data) {
		return Arrays.asList(data.employeeCode, data.employeeName, data.date, data.errorContent);
	}

}
