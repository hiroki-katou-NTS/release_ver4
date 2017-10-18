package nts.uk.file.com.infra.generate;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import com.aspose.cells.BackgroundType;
import com.aspose.cells.BorderType;
import com.aspose.cells.Cell;
import com.aspose.cells.CellBorderType;
import com.aspose.cells.Cells;
import com.aspose.cells.Color;
import com.aspose.cells.Font;
import com.aspose.cells.HorizontalPageBreakCollection;
import com.aspose.cells.PageSetup;
import com.aspose.cells.Style;
import com.aspose.cells.VerticalPageBreakCollection;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;
import com.aspose.cells.WorksheetCollection;

import approve.employee.EmployeeApproverDataSource;
import approve.employee.EmployeeApproverRootOutputGenerator;
import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.layer.infra.file.export.FileGeneratorContext;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.workflow.dom.adapter.workplace.WorkplaceImport;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApplicationType;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.service.output.ApproverAsApplicationInforOutput;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.service.output.EmployeeApproverAsApplicationOutput;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.service.output.EmployeeOrderApproverAsAppOutput;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.service.output.WpApproverAsAppOutput;
import nts.uk.shr.infra.file.report.aspose.cells.AsposeCellsReportGenerator;

@Stateless
public class AsposeEmployeeApproverReportGenerator extends AsposeCellsReportGenerator
		implements EmployeeApproverRootOutputGenerator {

	private static final String TEMPLATE_FILE = "report/申請者として承認ルートのEXCEL出力.xlsx";

	private static final String REPORT_FILE_NAME = "申請者として承認ルートのEXCEL出力.xlsx";

	private static final int[] COLUMN_INDEX = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 };

	private static final int maxOfPhrase = 5;

	@Override
	public void generate(FileGeneratorContext generatorContext, EmployeeApproverDataSource dataSource) {
		try (val reportContext = this.createContext(TEMPLATE_FILE)) {

			val designer = this.createContext(TEMPLATE_FILE);
			Workbook workbook = designer.getWorkbook();
			WorksheetCollection worksheets = workbook.getWorksheets();
			Worksheet worksheet = worksheets.get(0);
			// set up page prepare print
			this.printPage(worksheet);
			this.printWorkplace(worksheets, dataSource);

			designer.getDesigner().setWorkbook(workbook);
			designer.processDesigner();

			designer.saveAsExcel(this.createNewFile(generatorContext, this.getReportName(REPORT_FILE_NAME)));

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * PRINT PAGE
	 * 
	 * @param worksheet
	 * @param lstWorkplace
	 */
	private void printPage(Worksheet worksheet) {
		// Set print page
		PageSetup pageSetup = worksheet.getPageSetup();
		pageSetup.setFirstPageNumber(1);
		pageSetup.setPrintArea("A1:P");
	}

	/**
	 * Sets the title style.
	 *
	 * @param cell
	 *            the new title style
	 */
	private void setTitleStyle(Cell cell) {
		Style style = cell.getStyle();
		style.setPattern(BackgroundType.SOLID);
		style.setBorder(BorderType.TOP_BORDER, CellBorderType.THIN, Color.getGray());
		style.setBorder(BorderType.BOTTOM_BORDER, CellBorderType.THIN, Color.getGray());
		style.setBorder(BorderType.RIGHT_BORDER, CellBorderType.DOTTED, Color.getGray());
		cell.setStyle(style);
	}

	/**
	 * Sets the title style for the mergerd row .
	 *
	 * @param cell
	 *            the new title style
	 */
	private void setTitleStyleMerge(Cell cell) {
		Style style = cell.getStyle();
		style.setPattern(BackgroundType.SOLID);
		style.setBorder(BorderType.TOP_BORDER, CellBorderType.THIN, Color.getGray());
		style.setBorder(BorderType.RIGHT_BORDER, CellBorderType.DOTTED, Color.getGray());
		cell.setStyle(style);
	}

	/**
	 * PRINT ALL WORKPLACE
	 * 
	 * @param worksheets
	 * @param dataSource
	 */
	private void printWorkplace(WorksheetCollection worksheets, EmployeeApproverDataSource dataSource) {
		Worksheet worksheet = worksheets.get(0);
		Cells cells = worksheet.getCells();
		Map<String, WpApproverAsAppOutput> lstWorkplace = dataSource.getWpApprover();
		if (lstWorkplace.size() == 0) {
			throw new BusinessException("Msg_7");

		}

		int firstRow = 1;
		for (Map.Entry m : lstWorkplace.entrySet()) {
			WpApproverAsAppOutput workplace = (WpApproverAsAppOutput) m.getValue();
			firstRow = this.printEachWorkplace(worksheets, cells, firstRow, workplace);

		}

	}

	/**
	 * PRINT EACH WORKPLACE
	 * 
	 * @param worksheets
	 * @param cells
	 * @param firstRow
	 * @param workplace
	 * @return
	 */
	private int printEachWorkplace(WorksheetCollection worksheets, Cells cells, int firstRow,
			WpApproverAsAppOutput workplace) {

		WorkplaceImport wpInfor = workplace.getWpInfor();
		Map<String, EmployeeApproverAsApplicationOutput> employee = workplace.getMapEmpRootInfo();

		int numberOfPage = (firstRow + 1) / 52;

		int numberOfRowMerge = (52 * numberOfPage) - firstRow;
		// set "【職場】"
		Cell workPlace = cells.get(firstRow, COLUMN_INDEX[1]);
		workPlace.setValue("【職場】");

		// set worplace Name, workplace Code
		Cell workPlaceCode = cells.get(firstRow, COLUMN_INDEX[2]);
		workPlaceCode.setValue(wpInfor.getWkpCode() + " " + wpInfor.getWkpName());

		// tăng rowIndex lên 1 theo template
		firstRow = firstRow + 1;

		// nếu tổng số dòng trên một trang bị vượt qua > 52*x
		// thì sẽ ngắt trang
		if (numberOfRowMerge >= 0) {
			HorizontalPageBreakCollection hPageBreaks = worksheets.get(0).getHorizontalPageBreaks();
			hPageBreaks.add("P" + firstRow);
			VerticalPageBreakCollection vPageBreaks = worksheets.get(0).getVerticalPageBreaks();
			vPageBreaks.add("P" + firstRow);
		}

		// in ra các employee trong cùng một workplace
		for (Map.Entry m : employee.entrySet()) {
			EmployeeApproverAsApplicationOutput output = (EmployeeApproverAsApplicationOutput) m.getValue();
			int maxRow = 0;
			int totalMerger = 0;
			for (Map.Entry m1 : output.getMapAppTypeAsApprover().entrySet()) {

				// LIST CÁC FORM CỦA MỘT NGƯỜI
				List<ApproverAsApplicationInforOutput> appLst = (List<ApproverAsApplicationInforOutput>) m1.getValue();

				// TÍNH MAX ĐỂ MERGER CELL
				if (appLst != null || !CollectionUtil.isEmpty(appLst)) {
					maxRow = this.findMax(appLst);
				}
				totalMerger = totalMerger + maxRow;

			}

			int pages = (firstRow + totalMerger) / 52;

			int rowMergered = (52 * pages) - firstRow;

			firstRow = printEmployee(worksheets, totalMerger, cells, firstRow, output, false, pages);

			// if (rowMergered > 0) {
			//
			// firstRow = printEmployee(worksheets, rowMergered, cells,
			// firstRow, output,
			// true, pages);
			//
			// HorizontalPageBreakCollection hPageBreaks =
			// worksheets.get(0).getHorizontalPageBreaks();
			// hPageBreaks.add("P" + firstRow);
			// VerticalPageBreakCollection vPageBreaks =
			// worksheets.get(0).getVerticalPageBreaks();
			// vPageBreaks.add("P" + firstRow);
			//
			// firstRow = printEmployee(worksheets, totalMerger - rowMergered,
			// cells,
			// firstRow, output, false, pages);
			//
			// } else {
			//
			// firstRow = printEmployee(worksheets, totalMerger, cells,
			// firstRow, output,
			// false, pages);
			//
			// }

		}

		return firstRow;

	}

	/*
	 * find number of person
	 */

	private int findMax(List<ApproverAsApplicationInforOutput> approval) {
		int max = 1;
		for (ApproverAsApplicationInforOutput appRoot : approval) {
			if (appRoot.getLstEmpInfo() != null || !CollectionUtil.isEmpty(appRoot.getLstEmpInfo())) {
				if (max < appRoot.getLstEmpInfo().size()) {
					max = appRoot.getLstEmpInfo().size();
				}
			}
		}
		return max;
	}

	private int printEmployee(WorksheetCollection worksheets, int totalMerger, Cells cells, int firstRow,
			EmployeeApproverAsApplicationOutput output, boolean isBreak, int pages) {

		// bắt đầu của employee
		pages = (firstRow + totalMerger) / 52;

		int rowMergered = (52 * pages) - firstRow;
		if (rowMergered > 0) {
			// set employee code - CỘT 1
			if (rowMergered > 1) {
				cells.merge(firstRow, 1, rowMergered, 1, true);
			}

			Cell em_Code = cells.get(firstRow, COLUMN_INDEX[1]);
			em_Code.setValue(output.getEmployeeInfor().getEId());

			// set employee name - CỘT 2
			if (rowMergered > 1) {
				cells.merge(firstRow, 2, rowMergered, 1, true);
			}

			Cell em_Name = cells.get(firstRow, COLUMN_INDEX[2]);
			em_Name.setValue(output.getEmployeeInfor().getEName());

			for (int i = 0; i < rowMergered; i++) {
				Cell style_Code = cells.get(firstRow + i, COLUMN_INDEX[1]);
				Cell style_Name = cells.get(firstRow + i, COLUMN_INDEX[2]);
				if (i == (totalMerger - 1)) {
					setTitleStyle(style_Code);
					setTitleStyle(style_Name);
				} else {
					setTitleStyleMerge(style_Code);
					setTitleStyleMerge(style_Name);
				}
			}

			// IN RA TỪ CỘT THỨ 3 TRỞ RA
			for (Map.Entry m1 : output.getMapAppTypeAsApprover().entrySet()) {

				// LIST CÁC FORM CỦA MỘT NGƯỜI
				List<ApproverAsApplicationInforOutput> appLst = (List<ApproverAsApplicationInforOutput>) m1.getValue();

				// TÍNH MAX ĐỂ MERGER CELL
				int max = this.findMax(appLst);

				firstRow = printColumns3(cells, max, m1, firstRow, appLst, rowMergered);

			}

		} else {

			// set employee code - CỘT 1
			if (totalMerger > 1) {
				cells.merge(firstRow, 1, totalMerger, 1, true);
			}

			Cell em_Code = cells.get(firstRow, COLUMN_INDEX[1]);
			em_Code.setValue(output.getEmployeeInfor().getEId());

			// set employee name - CỘT 2
			if (totalMerger > 1) {
				cells.merge(firstRow, 2, totalMerger, 1, true);
			}

			Cell em_Name = cells.get(firstRow, COLUMN_INDEX[2]);
			em_Name.setValue(output.getEmployeeInfor().getEName());

			for (int i = 0; i < totalMerger; i++) {
				Cell style_Code = cells.get(firstRow + i, COLUMN_INDEX[1]);
				Cell style_Name = cells.get(firstRow + i, COLUMN_INDEX[2]);
				if (i == (totalMerger - 1)) {
					setTitleStyle(style_Code);
					setTitleStyle(style_Name);
				} else {
					setTitleStyleMerge(style_Code);
					setTitleStyleMerge(style_Name);
				}
			}

			// IN RA TỪ CỘT THỨ 3 TRỞ RA
			for (Map.Entry m1 : output.getMapAppTypeAsApprover().entrySet()) {

				// LIST CÁC FORM CỦA MỘT NGƯỜI
				List<ApproverAsApplicationInforOutput> appLst = (List<ApproverAsApplicationInforOutput>) m1.getValue();

				// TÍNH MAX ĐỂ MERGER CELL
				int max = this.findMax(appLst);
				firstRow = printColumns3(cells, max, m1, firstRow, appLst, 0);

			}
		}
		return firstRow;
	}

	private int printColumns3(Cells cells, int max, Map.Entry m1, int firstRow,
			List<ApproverAsApplicationInforOutput> appLst, int rowMergered) {
		if (rowMergered > 0) {

			// IN RA CỘT THỨ 3
			if (max > 1) {
				cells.merge(firstRow, 3, max, 1, true);
			}
			Cell em_Form = cells.get(firstRow, COLUMN_INDEX[3]);
			if (m1.getKey().toString().equals("99")) {
				em_Form.setValue("共通");
			} else {
				em_Form.setValue(
						EnumAdaptor.valueOf(Integer.valueOf(m1.getKey().toString()), ApplicationType.class).nameId);
			}

			// SET STYLE CHO CỘT THỨ 3
			for (int i = 0; i < max; i++) {
				Cell style_Form = cells.get(firstRow + i, COLUMN_INDEX[3]);
				if (i == (max - 1)) {
					setTitleStyle(style_Form);
				} else {
					setTitleStyleMerge(style_Form);
				}
			}

			// KIỂM TRA XEM ĐỘ DÀI CÁC PHRASE
			// NẾU BẰNG 0 THÌ IN RA CỘT THỨ 13
			// NGƯỢC LẠI THÌ KHÔNG IN HAY GÌ ĐÓ
			if (appLst.size() == 0) {
				Cell notice = cells.get(firstRow, COLUMN_INDEX[14]);
				notice.setValue("マスタなし");
				Color color = Color.getRed();
				Font font = notice.getStyle().getFont();
				font.setColor(color);
				setTitleStyle(notice);
				notice.setStyle(notice.getStyle());
				firstRow = firstRow + 1;
			} else {

				// TÌM RA SỐ DÒNG MAX ĐỂ MERGE CELL CHO CỘT 3, 4

				int j = 4;

				// IN RA CỘT THỨ 4 CỦA PHRASE I

				if (appLst.size() == maxOfPhrase) {
					for (int k = 0; k < appLst.size(); k++) {

						ApproverAsApplicationInforOutput app = appLst.get(k);
						if (app.getPhaseNumber() == 0) {

							printEachPhrase(cells, firstRow, max, app, j);
							if ((j + 2) < 14) {
								j = j + 2;
							}

						} else if (app.getPhaseNumber() == 1) {

							printEachPhrase(cells, firstRow, max, app, j);
							if ((j + 2) < 14) {
								j = j + 2;
							}

						} else if (app.getPhaseNumber() == 2) {

							printEachPhrase(cells, firstRow, max, app, j);
							if ((j + 2) < 14) {
								j = j + 2;
							}

						} else if (app.getPhaseNumber() == 3) {

							printEachPhrase(cells, firstRow, max, app, j);
							if ((j + 2) < 14) {
								j = j + 2;
							}

						} else if (app.getPhaseNumber() == 4) {

							printEachPhrase(cells, firstRow, max, app, j);
							if ((j + 2) < 14) {
								j = j + 2;
							}

						} else if (app.getPhaseNumber() == 5) {

							printEachPhrase(cells, firstRow, max, app, j);
							if ((j + 2) < 14) {
								j = j + 2;
							}

						}

					}

					if (max > 1) {
						cells.merge(firstRow, 14, max, 1);
					}
					Cell notice = cells.get(firstRow, COLUMN_INDEX[14]);
					notice.setValue("");
					for (int i = 0; i < max; i++) {
						Cell style_Form = cells.get(firstRow + i, COLUMN_INDEX[14]);
						if (i == (max - 1)) {
							setTitleStyle(style_Form);
						} else {
							setTitleStyleMerge(style_Form);
						}
					}
					firstRow = firstRow + max;

				} else {

					for (int k = 0; k < appLst.size(); k++) {

						ApproverAsApplicationInforOutput app = appLst.get(k);
						if (app.getPhaseNumber() == 0) {

							printEachPhrase(cells, firstRow, max, app, j);
							if ((j + 2) < 14) {
								j = j + 2;
							}

						} else if (app.getPhaseNumber() == 1) {

							printEachPhrase(cells, firstRow, max, app, j);
							if ((j + 2) < 14) {
								j = j + 2;
							}

						} else if (app.getPhaseNumber() == 2) {

							printEachPhrase(cells, firstRow, max, app, j);
							if ((j + 2) < 14) {
								j = j + 2;
							}

						} else if (app.getPhaseNumber() == 3) {

							printEachPhrase(cells, firstRow, max, app, j);
							if ((j + 2) < 14) {
								j = j + 2;
							}

						} else if (app.getPhaseNumber() == 4) {

							printEachPhrase(cells, firstRow, max, app, j);
							if ((j + 2) < 14) {
								j = j + 2;
							}

						} else if (app.getPhaseNumber() == 5) {

							printEachPhrase(cells, firstRow, max, app, j);
							if ((j + 2) < 14) {
								j = j + 2;
							}

						}

					}

					for (int k = appLst.size(); k < maxOfPhrase; k++) {
						// SET STYLE
						for (int i = 0; i < max; i++) {
							Cell style_Form = cells.get(firstRow + i, COLUMN_INDEX[j]);
							if (i == (max - 1)) {
								setTitleStyle(style_Form);
							} else {
								setTitleStyleMerge(style_Form);
							}
						}

						for (int a = 0; a < 5; a++) {
							Cell em_name = cells.get(firstRow + a, COLUMN_INDEX[j + 1]);
							setTitleStyle(em_name);
							a++;

						}
						
						if(j <= 12){
							j = j + 2;
						}

					}

					if (max > 1) {
						cells.merge(firstRow, 14, max, 1);
					}
					Cell notice = cells.get(firstRow, COLUMN_INDEX[14]);
					notice.setValue("");
					for (int i = 0; i < max; i++) {
						Cell style_Form = cells.get(firstRow + i, COLUMN_INDEX[14]);
						if (i == (max - 1)) {
							setTitleStyle(style_Form);
						} else {
							setTitleStyleMerge(style_Form);
						}
					}
					firstRow = firstRow + max;

				}

			}

		} else {

			// IN RA CỘT THỨ 3
			if (max > 1) {
				cells.merge(firstRow, COLUMN_INDEX[3], max, 1, true);
			}
			Cell em_Form = cells.get(firstRow, COLUMN_INDEX[3]);
			if (m1.getKey().toString().equals("99")) {
				em_Form.setValue("共通");
			} else {
				em_Form.setValue(
						EnumAdaptor.valueOf(Integer.valueOf(m1.getKey().toString()), ApplicationType.class).nameId);
			}

			// SET STYLE CHO CỘT THỨ 3
			for (int i = 0; i < max; i++) {
				Cell style_Form = cells.get(firstRow + i, COLUMN_INDEX[3]);
				if (i == (max - 1)) {
					setTitleStyle(style_Form);
				} else {
					setTitleStyleMerge(style_Form);
				}
			}

			// KIỂM TRA XEM ĐỘ DÀI CÁC PHRASE
			// NẾU BẰNG 0 THÌ IN RA CỘT THỨ 13
			// NGƯỢC LẠI THÌ KHÔNG IN HAY GÌ ĐÓ
			if (appLst.size() == 0) {
				Cell notice = cells.get(firstRow, COLUMN_INDEX[14]);
				notice.setValue("マスタなし");
				Color color = Color.getRed();
				Font font = notice.getStyle().getFont();
				font.setColor(color);
				setTitleStyle(notice);
				notice.setStyle(notice.getStyle());
				firstRow = firstRow + 1;
			} else {

				// TÌM RA SỐ DÒNG MAX ĐỂ MERGE CELL CHO CỘT 3, 4

				int j = 4;

				// IN RA CỘT THỨ 4 CỦA PHRASE I
				for (ApproverAsApplicationInforOutput app : appLst) {

					if (app.getPhaseNumber() == 0) {

						printEachPhrase(cells, firstRow, max, app, j);
						if ((j + 2) < 14) {
							j = j + 2;
						}

					} else if (app.getPhaseNumber() == 1) {

						printEachPhrase(cells, firstRow, max, app, j);
						if ((j + 2) < 14) {
							j = j + 2;
						}

					} else if (app.getPhaseNumber() == 2) {

						printEachPhrase(cells, firstRow, max, app, j);
						if ((j + 2) < 14) {
							j = j + 2;
						}

					} else if (app.getPhaseNumber() == 3) {

						printEachPhrase(cells, firstRow, max, app, j);
						if ((j + 2) < 14) {
							j = j + 2;
						}

					} else if (app.getPhaseNumber() == 4) {

						printEachPhrase(cells, firstRow, max, app, j);
						if ((j + 2) < 14) {
							j = j + 2;
						}

					} else if (app.getPhaseNumber() == 5) {

						printEachPhrase(cells, firstRow, max, app, j);
						if ((j + 2) < 14) {
							j = j + 2;
						}

					}

				}

				if (max > 1) {
					cells.merge(firstRow, 14, max, 1);
				}
				Cell notice = cells.get(firstRow, COLUMN_INDEX[14]);
				notice.setValue("");
				for (int i = 0; i < max; i++) {
					Cell style_Form = cells.get(firstRow + i, COLUMN_INDEX[14]);
					if (i == (max - 1)) {
						setTitleStyle(style_Form);
					} else {
						setTitleStyleMerge(style_Form);
					}
				}
				firstRow = firstRow + max;

			}
		}

		return firstRow;
	}

	private void printEachPhrase(Cells cells, int firstRow, int max, ApproverAsApplicationInforOutput app, int j) {
		if (max > 1) {
			cells.merge(firstRow, j, max, 1, true);
		}
		Cell appPhrase = cells.get(firstRow, COLUMN_INDEX[j]);
		appPhrase.setValue(app.getApprovalForm());
		// SET STYLE
		for (int i = 0; i < max; i++) {
			Cell style_Form = cells.get(firstRow + i, COLUMN_INDEX[j]);
			if (i == (max - 1)) {
				setTitleStyle(style_Form);
			} else {
				setTitleStyleMerge(style_Form);
			}
		}
		// IN RA CỘT THỨ 5 CỦA PHRASE I
		List<EmployeeOrderApproverAsAppOutput> employeelst = app.getLstEmpInfo();
		int i = 0;
		for (EmployeeOrderApproverAsAppOutput eName : employeelst) {
			Cell em_name = cells.get(firstRow + i, COLUMN_INDEX[j + 1]);
			em_name.setValue(eName.getEmployeeName());
			setTitleStyle(em_name);
			i++;

		}

	}

}
