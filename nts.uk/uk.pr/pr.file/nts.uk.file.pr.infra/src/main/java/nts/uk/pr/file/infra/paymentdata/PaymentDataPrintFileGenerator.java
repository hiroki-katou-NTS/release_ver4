package nts.uk.pr.file.infra.paymentdata;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import com.aspose.cells.Cells;
import com.aspose.cells.PageOrientationType;
import com.aspose.cells.Range;
import com.aspose.cells.SaveFormat;
import com.aspose.cells.Style;
import com.aspose.cells.Workbook;
import com.aspose.cells.WorkbookDesigner;
import com.aspose.cells.Worksheet;

import nts.arc.error.BusinessException;
import nts.arc.layer.infra.file.export.FileGenerator;
import nts.arc.layer.infra.file.export.FileGeneratorContext;
import nts.uk.pr.file.infra.paymentdata.result.DetailItemDto;
import nts.uk.pr.file.infra.paymentdata.result.LayoutMasterCategoryDto;
import nts.uk.pr.file.infra.paymentdata.result.LineDto;
import nts.uk.pr.file.infra.paymentdata.result.PaymentDataResult;

@Stateless
public class PaymentDataPrintFileGenerator extends FileGenerator {

	private final String fileName = "report/qpp021.xlsx";

	protected void generate(FileGeneratorContext context) {
		try {
			// get list PaymentDataResult
			List<PaymentDataResult> results = null;//context.getParameterAt(0);
			// create workbook
			WorkbookDesigner designer = new WorkbookDesigner();
			String saveFile = File.createTempFile("ukrp", "給与支給明細書.pdf").getPath();
			OutputStream output = this.createNewFile(context, saveFile);
			Workbook workbook = null;
			try {
				workbook = new Workbook(Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName));
			} catch (Exception e) {
				throw new RuntimeException("Report template not found");
			}
			designer.setWorkbook(workbook);
			// create table
			if (!results.isEmpty()) {
				drawTable(designer, results);
				// save to file
				designer.getWorkbook().save(output, SaveFormat.PDF);
			} else {
				throw new BusinessException("更新対象のデータが存在しません。");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void drawTable(WorkbookDesigner designer, List<PaymentDataResult> results) throws Exception {
		Style styleValue = designer.getWorkbook().getWorksheets().get(0).getCells().getCellStyle(0, 0);
		Style styleHeader = designer.getWorkbook().getWorksheets().get(0).getCells().getCellStyle(0, 1);
		Style styleFooter = designer.getWorkbook().getWorksheets().get(0).getCells().getCellStyle(0, 3);
		Style styleLable = designer.getWorkbook().getWorksheets().get(0).getCells().getCellStyle(0, 5);
		for (int i = 0; i < results.size(); i++) {
			Worksheet sheet = designer.getWorkbook().getWorksheets().get(i);
			Cells cells = sheet.getCells();
			// check empty data
			if (results.get(i).getCategories() == null) {
				// bind header
				designer.setDataSource("PaymentDataHeader", results.get(i).getPaymentHeader());
				Range error = cells.createRange(8, 11, 1, 5);
				error.merge();
				error.setValue("更新対象のデータが存在しません。");
				// bind processing Yearmonth
				String year = results.get(i).getPaymentHeader().getProcessingYM().toString().substring(0, 4);
				String month = results.get(i).getPaymentHeader().getProcessingYM().toString().substring(4);
				String processingYm = year + "年" + month + "月";
				designer.setDataSource("ProcessingYm", processingYm);
				// remove sample style
				sheet.getCells().get(0, 0).setStyle(null);
				sheet.getCells().get(0, 1).setStyle(null);
				sheet.getCells().get(0, 3).setStyle(null);
				sheet.getCells().get(0, 5).setStyle(null);
				// check dirty
				Range delete = cells.createRange(9, 0, 50, 50);
				delete.setStyle(null);
				delete.setValue(null);
				// fit wide
				designer.getWorkbook().getWorksheets().get(i).getPageSetup().setFitToPagesWide(1);
				// set landscape
				designer.getWorkbook().getWorksheets().get(i).getPageSetup()
						.setOrientation(PageOrientationType.LANDSCAPE);
				// add another page
				if (i < results.size() - 1) {
					designer.getWorkbook().getWorksheets().addCopy(i);
				}
				// approve process
				designer.process(i, true);
			} else {
				List<LayoutMasterCategoryDto> categories = results.get(i).getCategories();
				int lineCountCate1 = 0;
				int lineCountCate2 = 0;
				int lineCountCate3 = 0;
				int lineCountCate4 = 0;
				for (int j = 0; j < categories.size(); j++) {
					if (categories.get(j).getCategoryAttribute() == 0) {
						lineCountCate1 = categories.get(j).getLines().stream()
								.filter(line -> line.getLineDispayAttribute() == 1).collect(Collectors.toList()).size();
					} else if (categories.get(j).getCategoryAttribute() == 1) {
						lineCountCate2 = categories.get(j).getLines().stream()
								.filter(line -> line.getLineDispayAttribute() == 1).collect(Collectors.toList()).size();
					} else if (categories.get(j).getCategoryAttribute() == 2) {
						lineCountCate3 = categories.get(j).getLines().stream()
								.filter(line -> line.getLineDispayAttribute() == 1).collect(Collectors.toList()).size();
					} else if (categories.get(j).getCategoryAttribute() == 3) {
						lineCountCate4 = categories.get(j).getLines().stream()
								.filter(line -> line.getLineDispayAttribute() == 1).collect(Collectors.toList()).size();
					}
				}
				// check dirty
				Range delete = cells.createRange(9, 0, 50, 50);
				delete.setStyle(null);
				delete.setValue(null);
				delete.merge();
				delete.unMerge();
				// start drawing
				Range lblCate1 = cells.createRange(9, 1, lineCountCate1 * 2, 1);
				lblCate1.merge();
				lblCate1.setStyle(styleLable);
				lblCate1.setValue("支給");
				int startRowItemCate1 = 9;
				int startColItem = 2;
				for (int j = 1; j <= lineCountCate1; j++) {
					for (int k = 0; k < 9; k++) {
						Range headerItem1 = cells.createRange(startRowItemCate1, startColItem, 1, 3);
						headerItem1.merge();
						headerItem1.setStyle(styleHeader);
						headerItem1.setValue(String.format("&=$ItemNameCat1Line%s_%s", j, k));
						Range valueItem1 = cells.createRange(startRowItemCate1 + 1, startColItem, 1, 3);
						valueItem1.merge();
						valueItem1.setStyle(styleValue);
						valueItem1.setValue(String.format("&=$ItemValueCat1Line%s_%s", j, k));
						startColItem += 3;
					}
					startColItem = 2;
					startRowItemCate1 += 2;
				}
				int startRowItemCate2 = startRowItemCate1;
				Range lblCate2 = cells.createRange(startRowItemCate2, 1, lineCountCate2 * 2, 1);
				lblCate2.merge();
				lblCate2.setStyle(styleLable);
				lblCate2.setValue("控除");
				for (int j = 1; j <= lineCountCate2; j++) {
					for (int k = 0; k < 9; k++) {
						Range headerItem1 = cells.createRange(startRowItemCate2, startColItem, 1, 3);
						headerItem1.merge();
						headerItem1.setStyle(styleHeader);
						headerItem1.setValue(String.format("&=$ItemNameCat2Line%s_%s", j, k));
						Range valueItem1 = cells.createRange(startRowItemCate2 + 1, startColItem, 1, 3);
						valueItem1.merge();
						valueItem1.setStyle(styleValue);
						valueItem1.setValue(String.format("&=$ItemValueCat2Line%s_%s", j, k));
						startColItem += 3;
					}
					startColItem = 2;
					startRowItemCate2 += 2;
				}
				int startRowItemCate3 = startRowItemCate2;
				if (lineCountCate3 != 0) {
					Range lblCate3 = cells.createRange(startRowItemCate3, 1, lineCountCate3 * 2, 1);
					lblCate3.merge();
					lblCate3.setStyle(styleLable);
					lblCate3.setValue("勤怠");
					for (int j = 1; j <= lineCountCate3; j++) {
						for (int k = 0; k < 9; k++) {
							Range headerItem1 = cells.createRange(startRowItemCate3, startColItem, 1, 3);
							headerItem1.merge();
							headerItem1.setStyle(styleHeader);
							headerItem1.setValue(String.format("&=$ItemNameCat3Line%s_%s", j, k));
							Range valueItem1 = cells.createRange(startRowItemCate3 + 1, startColItem, 1, 3);
							valueItem1.merge();
							valueItem1.setStyle(styleValue);
							valueItem1.setValue(String.format("&=$ItemValueCat3Line%s_%s", j, k));
							startColItem += 3;
						}
						startColItem = 2;
						startRowItemCate3 += 2;
					}
				}
				int startRowItemCate4 = startRowItemCate3;
				if (lineCountCate4 != 0) {
					Range lblCate4 = cells.createRange(startRowItemCate4, 1, lineCountCate4 * 2, 1);
					lblCate4.merge();
					lblCate4.setStyle(styleLable);
					lblCate4.setValue("記事");
					for (int j = 1; j <= lineCountCate4; j++) {
						for (int k = 0; k < 9; k++) {
							Range headerItem1 = cells.createRange(startRowItemCate4, startColItem, 1, 3);
							headerItem1.merge();
							headerItem1.setStyle(styleHeader);
							headerItem1.setValue(String.format("&=$ItemNameCat4Line%s_%s", j, k));
							Range valueItem1 = cells.createRange(startRowItemCate4 + 1, startColItem, 1, 3);
							valueItem1.merge();
							valueItem1.setStyle(styleValue);
							valueItem1.setValue(String.format("&=$ItemValueCat4Line%s_%s", j, k));
							startColItem += 3;
						}
						startColItem = 2;
						startRowItemCate4 += 2;
					}
				}
				Range companyCode = cells.createRange(startRowItemCate4 + 1, 0, 1, 7);
				companyCode.merge();
				companyCode.setStyle(styleFooter);
				companyCode.setValue("&=PaymentDataHeader.companyCode(bean)");
				Range companyName = cells.createRange(startRowItemCate4 + 1, 11, 1, 7);
				companyName.merge();
				companyName.setStyle(styleFooter);
				companyName.setValue("&=PaymentDataHeader.companyName(bean)");
				// remove sample style
				sheet.getCells().get(0, 0).setStyle(null);
				sheet.getCells().get(0, 1).setStyle(null);
				sheet.getCells().get(0, 3).setStyle(null);
				sheet.getCells().get(0, 5).setStyle(null);
				// bind data
				bindingData(designer, results.get(i));
				// fit
				designer.getWorkbook().getWorksheets().get(i).getPageSetup().setFitToPagesWide(1);
				// set landscape
				designer.getWorkbook().getWorksheets().get(i).getPageSetup()
						.setOrientation(PageOrientationType.LANDSCAPE);
				// add another page
				if (i < results.size() - 1) {
					designer.getWorkbook().getWorksheets().addCopy(i);
				}
				// approve process
				designer.process(i, true);
			}
		}

	}

	private void bindingData(WorkbookDesigner designer, PaymentDataResult result) {
		// bind header
		designer.setDataSource("PaymentDataHeader", result.getPaymentHeader());
		designer.setDataSource("Department", result.getPaymentHeader().getDepartmentCode() + " " + result.getPaymentHeader().getDepartmentName());
		// bind categories data to datasource
		List<LayoutMasterCategoryDto> categories = result.getCategories();
		for (int j = 0; j < categories.size(); j++) {
			if (categories.get(j).getCategoryAttribute() == 0) {
				createValue(designer, categories.get(j), 1);
			} else if (categories.get(j).getCategoryAttribute() == 1) {
				createValue(designer, categories.get(j), 2);
			} else if (categories.get(j).getCategoryAttribute() == 2) {
				createValue(designer, categories.get(j), 3);
			} else if (categories.get(j).getCategoryAttribute() == 3) {
				createValue(designer, categories.get(j), 4);
			}
		}
		// bind processing Yearmonth
		String year = result.getPaymentHeader().getProcessingYM().toString().substring(0, 4);
		String month = result.getPaymentHeader().getProcessingYM().toString().substring(4);
		String processingYm = year + "年" + month + "月";
		designer.setDataSource("ProcessingYm", processingYm);
	}

	// calculate lines and items position
	private void createValue(WorkbookDesigner designer, LayoutMasterCategoryDto category, int cateIndex) {
		List<LineDto> lines = category.getLines().stream().filter(line -> line.getLineDispayAttribute() == 1)
				.collect(Collectors.toList());
		for (LineDto line : lines) {
			int i = 0;
			for (DetailItemDto detailItem : line.getDetails()) {
				String dataSourceName = String.format("ItemNameCat%sLine%s_%s", cateIndex, line.getLinePosition(), i);
				String dataSourceValue = String.format("ItemValueCat%sLine%s_%s", cateIndex, line.getLinePosition(), i);
				designer.setDataSource(dataSourceName, detailItem.getItemName());
				if (detailItem.getValue() != null) {
					if (detailItem.getCategoryAtr() == 2) {
						if (detailItem.getItemCode().equals("F203")||detailItem.getItemCode().equals("0100")) {
							int t = detailItem.getValue().intValue();
							int hours = t / 60;
							int minutes = t % 60;
							String timeFormat = String.format("%d:%02d", hours, minutes);
							designer.setDataSource(dataSourceValue, timeFormat);
						} else
							designer.setDataSource(dataSourceValue, detailItem.getValue());
					} else {
						designer.setDataSource(dataSourceValue, String.format("%,.0f", detailItem.getValue()));
					}
				} else {
					designer.setDataSource(dataSourceValue, "");
				}
				i++;
			}

		}
	}

}
