/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.at.infra.statement;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.aspose.cells.Cell;
import com.aspose.cells.Cells;
import com.aspose.cells.PageSetup;
import com.aspose.cells.Range;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;

import lombok.val;
import nts.arc.layer.infra.file.export.FileGeneratorContext;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.function.app.find.statement.export.DataExport;
import nts.uk.ctx.at.function.app.find.statement.export.StatementList;
import nts.uk.ctx.at.function.dom.statement.StampingOutputItemSet;
import nts.uk.ctx.at.function.dom.statement.StampingOutputItemSetRepository;
import nts.uk.ctx.bs.company.dom.company.CompanyRepository;
import nts.uk.file.at.app.export.statement.OutputConditionOfEmbossingGenerator;
import nts.uk.file.at.app.export.statement.OutputConditionOfEmbossingQuery;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.infra.file.report.aspose.cells.AsposeCellsReportGenerator;

/**
 * The Class AsposeOutputConditionOfEmbossingGenerator.
 */
@Stateless
public class AsposeOutputConditionOfEmbossingGenerator extends AsposeCellsReportGenerator implements OutputConditionOfEmbossingGenerator{

	/** The stamping output item set repository. */
	@Inject
	private StampingOutputItemSetRepository stampingOutputItemSetRepository;
	
	@Inject
	private CompanyRepository companyRepository;
	
	@Inject
	private DataExport dataExport;
	
	/** The Constant filename. */
	private static final String filename = "report/KDP003.xlsx";
	
	/** The Constant yyyyMMdd. */
	private static final String yyyyMMdd = "yyyy/MM/dd";
	
	/** The Constant yyyyMd. */
	private static final String yyyyMd = "yyyy/M/d";
	
	private static final String[] ATTANDANCE_CLASSIFICATION_COLUMN = new String[]{"AM3", "AQ3"}; // 出退勤区分
	private static final String[] WORKING_HOURS_COLUMN = new String[]{"AR3", "AV3"}; // 就業時間帯
	private static final String[] INSTALL_LOCATION_COLUMN =  new String[]{"AW3", "AZ3"}; // 設置場所
	private static final String[] LOCATION_INFORM_COLUMN =  new String[]{"BA3", "BD3"}; // 位置情報
	private static final String[] OT_HOURS_COLUMN =  new String[]{"BE3", "BH3"}; // 残業時間
	private static final String[] LATE_NIGHT_TIME_COLUMN =  new String[]{"BI3", "BL3"}; // 深夜時間
	private static final String[] SUPPORT_CARD_COLUMN =  new String[]{"BM3", "BQ3"}; // 応援カード
	
	/* (non-Javadoc)
	 * @see nts.uk.file.at.app.export.statement.OutputConditionOfEmbossingGenerator#generate(nts.arc.layer.infra.file.export.FileGeneratorContext, nts.uk.file.at.app.export.statement.OutputConditionOfEmbossingQuery)
	 */
	@Override
	public void generate(FileGeneratorContext fileGeneratorContext, OutputConditionOfEmbossingQuery query) {
		String companyId = AppContexts.user().companyId();
		String companyName = companyRepository.find(companyId).get().getCompanyName().v();
		String employeeCd = AppContexts.user().employeeCode();
		
		// ドメインモデル「打刻一覧出力項目設定」を取得する(get domain model 「打刻一覧出力項目設定」)
		StampingOutputItemSet stampingOutputItemSet = stampingOutputItemSetRepository.getByCidAndCode(companyId, query.getOutputSetCode()).get();
		
		GeneralDate startDate = convertToDate(query.getStartDate(), yyyyMMdd);
		GeneralDate endDate = convertToDate(query.getEndDate(), yyyyMMdd);
		
		List<StatementList> dataPreExport = dataExport.getTargetData(query.getLstEmployee(), 
																	 startDate, 
																	 endDate, 
																	 query.isCardNumNotRegister());
		exportExcel(fileGeneratorContext, dataPreExport, stampingOutputItemSet, startDate, endDate, companyName, query.isCardNumNotRegister(), employeeCd);
	}
	
	/**
	 * Sets the template.
	 *
	 * @param ws the new template
	 */
	private void setTemplate(Worksheet ws, GeneralDate startDate, GeneralDate endDate, String companyName) {
		PageSetup pageSetup = ws.getPageSetup();
		/*A1_1*/
		pageSetup.setHeader(0, "&9 " + companyName);
		/*A1_2*/
		pageSetup.setHeader(1, "&16&\"ＭＳ ゴシック\" " + TextResource.localize("KDP003_1"));
		/*A1_3, A1_4*/
		DateTimeFormatter fullDateTimeFormatter = DateTimeFormatter.ofPattern("yy/MM/dd  h:mm a", Locale.US);
		pageSetup.setHeader(2, "&8 " + LocalDateTime.now().format(fullDateTimeFormatter).toLowerCase() + "\n&P ページ");
		
		Cells cells = ws.getCells();
		/*B1_1, B1_2*/
		cells.get("A1").setValue(TextResource.localize("KDP003_57") + " " + startDate.toString() + "　～　" + endDate.toString());
		/*C1_1*/
		cells.get("A2").setValue(TextResource.localize("KDP003_40"));
		/*C1_2*/
		cells.get("F2").setValue(TextResource.localize("KDP003_41"));
		/*C1_3*/
		cells.get("M2").setValue(TextResource.localize("KDP003_42"));
		/*C1_4*/
		cells.get("R2").setValue(TextResource.localize("KDP003_43"));
		/*C1_5*/
		cells.get("X2").setValue(TextResource.localize("KDP003_44"));
		/*C1_6*/
		cells.get("AE2").setValue(TextResource.localize("KDP003_45"));
		/*C1_7*/
		cells.get("AJ2").setValue(TextResource.localize("KDP003_46"));
		/*C1_8*/
		cells.get("AM2").setValue(TextResource.localize("KDP003_47"));
		/*C1_9*/
		cells.get("AR2").setValue(TextResource.localize("KDP003_48"));
		/*C1_10*/
		cells.get("AW2").setValue(TextResource.localize("KDP003_49"));
		/*C1_11*/
		cells.get("BA2").setValue(TextResource.localize("KDP003_50"));
		/*C1_12*/
		cells.get("BE2").setValue(TextResource.localize("KDP003_51"));
		/*C1_13*/
		cells.get("BI2").setValue(TextResource.localize("KDP003_52"));
		/*C1_14*/
		cells.get("BM2").setValue(TextResource.localize("KDP003_53"));
	}
	
	/**
	 * Export excel.
	 *
	 * @param fileGeneratorContext the file generator context
	 * @param dataPreExport the data pre export
	 */
	private void exportExcel(FileGeneratorContext fileGeneratorContext, List<StatementList> dataPreExport, 
							StampingOutputItemSet stampingOutputItemSet, GeneralDate startDate, GeneralDate endDate, 
							String companyName, boolean isCardNumNotRegister, String employeeCd) {
		
		val reportContext = this.createContext(filename);
		
		// Instantiating a Workbook object
		Workbook workbook = reportContext.getWorkbook();

		// Accessing the added worksheet in the Excel file
		Worksheet worksheet = workbook.getWorksheets().get("帳票レイアウト");
		setTemplate(worksheet, startDate, endDate, companyName);
		Worksheet worksheetCopy = workbook.getWorksheets().get("copy");
		setTemplate(worksheetCopy, startDate, endDate, companyName);
		Cells cells = worksheet.getCells();

//		// copy page template 1 -> 2
		Range range1 = worksheetCopy.getCells().createRange("A3", "BQ34");
		int countLinePage = 3;
		while (countLinePage <= dataPreExport.size() && dataPreExport.size() > 34) {
			countLinePage += 31;
			Range range2 = worksheet.getCells().createRange("A" + (countLinePage+1), "BQ" + (countLinePage + 34));
			try {
				range2.copy(range1);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			countLinePage += 1;
		}
		
		Integer count = 3;
		// process export with data
		if (isCardNumNotRegister) {
			for (int i = 0; i < dataPreExport.size(); i++) {
				StatementList dto = dataPreExport.get(i);
				String date = dto.getDate().toString(yyyyMd);
				
				if (i == 0) {
					cells.get("X"+count).putValue(dto.getCardNo(), false);
					cells.get("AE"+count).setValue(date);
				} else {
					if (dto.getCardNo().compareTo(dataPreExport.get(i-1).getCardNo()) != 0) {
						cells.get("X"+count).putValue(dto.getCardNo(), false);
					}
					if (date.compareTo(dataPreExport.get(i-1).getDate().toString(yyyyMd)) != 0) {
						cells.get("AE"+count).setValue(date);
					}
				}
				cells.get("AJ"+count).setValue(dto.getTime());
				cells.get("AM"+count).setValue(dto.getAtdType());
				cells.get("AR"+count).setValue(dto.getWorkTimeZone());
				count++;
			}
		} else {
			for (int i = 0; i < dataPreExport.size(); i++) {
				StatementList dto = dataPreExport.get(i);
				String date = dto.getDate().toString(yyyyMd);
				if (i == 0) {
					cells.get("A"+count).setValue(dto.getWkpCode());
					cells.get("F"+count).setValue(dto.getWkpName());
					cells.get("M"+count).setValue(dto.getEmpCode());
					cells.get("R"+count).setValue(dto.getEmpName());
					cells.get("X"+count).putValue(dto.getCardNo(), false);
					cells.get("AE"+count).setValue(date);
				} else {
					if ( dto.getWkpCode().compareTo(dataPreExport.get(i-1).getWkpCode()) != 0
							&& dto.getWkpName().compareTo(dataPreExport.get(i-1).getWkpName()) != 0
							&& dto.getEmpCode().compareTo(dataPreExport.get(i-1).getEmpCode()) != 0
							&& dto.getEmpName().compareTo(dataPreExport.get(i-1).getEmpName()) != 0
							&& dto.getCardNo().compareTo(dataPreExport.get(i-1).getCardNo()) != 0) {
						cells.get("A"+count).setValue(dto.getWkpCode());
						cells.get("F"+count).setValue(dto.getWkpName());
						cells.get("M"+count).setValue(dto.getEmpCode());
						cells.get("R"+count).setValue(dto.getEmpName());
						cells.get("X"+count).putValue(dto.getCardNo(), false);
					}
					if ( date.compareTo(dataPreExport.get(i-1).getDate().toString(yyyyMd)) != 0) {
						cells.get("AE"+count).setValue(date);
					}
				} 
				cells.get("AJ"+count).setValue(dto.getTime());
				cells.get("AM"+count).setValue(dto.getAtdType());
				cells.get("AR"+count).setValue(dto.getWorkTimeZone());
				count++;
			}
		}
		
		// delete row and column 
		int col1Start = cells.get(ATTANDANCE_CLASSIFICATION_COLUMN[0]).getColumn();
		int col1End = cells.get(ATTANDANCE_CLASSIFICATION_COLUMN[1]).getColumn();
		int col2Start = cells.get(WORKING_HOURS_COLUMN[0]).getColumn();
		int col2End = cells.get(WORKING_HOURS_COLUMN[1]).getColumn();
		int col3Start = cells.get(INSTALL_LOCATION_COLUMN[0]).getColumn();
		int col3End = cells.get(INSTALL_LOCATION_COLUMN[1]).getColumn();
		int col4Start = cells.get(LOCATION_INFORM_COLUMN[0]).getColumn();
		int col4End = cells.get(LOCATION_INFORM_COLUMN[1]).getColumn();
		int col5Start = cells.get(OT_HOURS_COLUMN[0]).getColumn();
		int col5End = cells.get(OT_HOURS_COLUMN[1]).getColumn();
		int col6Start = cells.get(LATE_NIGHT_TIME_COLUMN[0]).getColumn();
		int col6End = cells.get(LATE_NIGHT_TIME_COLUMN[1]).getColumn();
		int col7Start = cells.get(SUPPORT_CARD_COLUMN[0]).getColumn();
		int col7End = cells.get(SUPPORT_CARD_COLUMN[1]).getColumn();
		
		if (!stampingOutputItemSet.isOutputSupportCard()) {
			deleteCell(worksheet, col7Start, col7End);
		}
		
		if (!stampingOutputItemSet.isOutputNightTime()) {
			deleteCell(worksheet, col6Start, col6End);
		}
		
		if (!stampingOutputItemSet.isOutputOT()) {
			deleteCell(worksheet, col5Start, col5End);
		}
		
		if (!stampingOutputItemSet.isOutputPosInfor()) {
			deleteCell(worksheet, col4Start, col4End);
		}
		
		if (!stampingOutputItemSet.isOutputSetLocation()) {
			deleteCell(worksheet, col3Start, col3End);
		}
		
		if (!stampingOutputItemSet.isOutputWorkHours()) {
			deleteCell(worksheet, col2Start, col2End);
		}
		
		if (!stampingOutputItemSet.isOutputEmbossMethod()) {
			deleteCell(worksheet, col1Start, col1End);
		}
		
		int col8Start = cells.get("BR2").getColumn();
		if (!stampingOutputItemSet.isOutputEmbossMethod()) {
			deleteCell(worksheet, col8Start, 9999);
		}
		
		worksheet.getCells().deleteRows(count-1, worksheet.getCells().getMaxRow(), true);
		
		// Saving the Excel file
		workbook.getWorksheets().removeAt("copy");
		
		DateTimeFormatter jpFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss", Locale.JAPAN);
		String currentFormattedDate = LocalDateTime.now().format(jpFormatter);
		
		reportContext.saveAsExcel(this.createNewFile(fileGeneratorContext, "KDP003_" + employeeCd + "_ " + currentFormattedDate + ".xlsx"));
	}
	
	/**
	 * Delete cell.
	 *
	 * @param worksheet the worksheet
	 * @param colStart the col start
	 * @param colEnd the col end
	 */
	private void deleteCell(Worksheet worksheet, int colStart, int colEnd) {
		worksheet.getCells().deleteColumns(colStart, colEnd - colStart + 1, true);
	}
	
	/**
	 * Convert to date.
	 *
	 * @param date the date
	 * @param format the format
	 * @return the general date
	 */
	private GeneralDate convertToDate(String date, String format) {
		return GeneralDate.fromString(date, format);
	}
}
