/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.pr.file.infra.wageledger;

import java.util.ArrayList;
import java.util.List;

import com.aspose.cells.BackgroundType;
import com.aspose.cells.BorderType;
import com.aspose.cells.Cell;
import com.aspose.cells.CellBorderType;
import com.aspose.cells.Color;
import com.aspose.cells.FindOptions;
import com.aspose.cells.LookInType;
import com.aspose.cells.Style;
import com.aspose.cells.TextAlignmentType;
import com.aspose.cells.Worksheet;

import nts.uk.file.pr.app.export.wageledger.data.share.HeaderReportData;
import nts.uk.shr.infra.file.report.aspose.cells.AsposeCellsReportContext;
import nts.uk.shr.infra.file.report.aspose.cells.AsposeCellsReportGenerator;

public class WageLedgerBaseGenerator extends AsposeCellsReportGenerator{
	/** The Constant REPORT_FILE_NAME. */
	protected static final String REPORT_FILE_NAME = "賃金台帳.pdf";
	
	/** The Constant BLUE_COLOR. */
	protected static final Color BLUE_COLOR = Color.fromArgb(197, 241, 247);
	
	/** The Constant GREEN_COLOR. */
	protected static final Color GREEN_COLOR = Color.fromArgb(199, 243, 145);
	
	/**
	 * Safe sub list.
	 *
	 * @param <T> the generic type
	 * @param list the list
	 * @param fromIndex the from index
	 * @param toIndex the to index
	 * @return the list
	 */
	protected <T> List<T> safeSubList(List<T> list, int fromIndex, int toIndex) {
		int size = list.size();
		if (fromIndex >= size || toIndex <= 0 || fromIndex >= toIndex) {
			return new ArrayList<>();
		}

		fromIndex = Math.max(0, fromIndex);
		toIndex = Math.min(size, toIndex);
		return list.subList(fromIndex, toIndex);
	}
	
	/**
	 * Find cell with content.
	 *
	 * @param ws the ws
	 * @param content the content
	 * @return the cell
	 */
	protected Cell findCellWithContent(Worksheet ws, String content) {
		// Cell find option.
		FindOptions findOptions = new FindOptions();
		findOptions.setLookInType(LookInType.VALUES);
		findOptions.setCaseSensitive(false);
		
		Cell cell = ws.getCells().find(content, null, findOptions);
		
		return cell;
	}
	
	
	/**
	 * Fill header data.
	 *
	 * @param reportContext the report context
	 * @param headerData the header data
	 */
	protected void fillHeaderData(AsposeCellsReportContext reportContext, HeaderReportData headerData) {
		Worksheet ws = reportContext.getDesigner().getWorkbook().getWorksheets().get(0);
		String infoPadding = "        ";
		
		// Fill target period.
		Cell targetPeriodCell = this.findCellWithContent(ws, "TargetPeriod");
		if (targetPeriodCell != null) {
			targetPeriodCell.setValue(String.format("%d年1月～%d年12月", headerData.targetYear,
					headerData.targetYear));
		}
		
		// Fill Department Label.
		Cell depLabelCell = this.findCellWithContent(ws, "DepartmentLabel");
		depLabelCell.setValue("【部門】");
		
		// Fill Department info.
		Cell depInfo = this.findCellWithContent(ws, "DepartmentInfo");
		depInfo.setValue(headerData.departmentCode + infoPadding + headerData.departmentName);
		
		// Fill Employee label.
		Cell empLabel = this.findCellWithContent(ws, "EmployeeLabel");
		empLabel.setValue("【社員】");
		
		// Fill Employee info.
		Cell empInfo = this.findCellWithContent(ws, "EmployeeInfo");
		empInfo.setValue(headerData.employeeCode + infoPadding + headerData.departmentName);
		
		// Fill Sex label.
		Cell sexLabelCell = this.findCellWithContent(ws, "SexLabel");
		sexLabelCell.setValue("【性別】");
		
		// Fill Sex Info.
		Cell sexInfoCell = this.findCellWithContent(ws, "SexInfo");
		sexInfoCell.setValue(headerData.sex);
	}
	
	/**
	 * Sets the style cell.
	 *
	 * @param cell the cell
	 * @param styleModel the style model
	 */
	protected void setStyleCell(Cell cell, StyleModel styleModel) {
		Style style = cell.getStyle();
		
		// Background Color
		if (styleModel.backgroundColor != null) {
			style.setForegroundColor(styleModel.backgroundColor);
			style.setPattern(BackgroundType.SOLID);
		}
		
		// Border
		switch (styleModel.borderType) {
		case None:
			break;
		case Outside:
			// Setting the line style of the top border
			style.setBorder(BorderType.TOP_BORDER, CellBorderType.THIN, Color.getBlack());
			// Setting the line style of the bottom border
			style.setBorder(BorderType.BOTTOM_BORDER, CellBorderType.THIN, Color.getBlack());
			// Setting the line style of the left border
			style.setBorder(BorderType.LEFT_BORDER, CellBorderType.THIN, Color.getBlack());
			// Setting the line style of the right border
			style.setBorder(BorderType.RIGHT_BORDER, CellBorderType.THIN, Color.getBlack());
			break;
		case ItemCellBorder:
			style.setBorder(BorderType.RIGHT_BORDER, CellBorderType.DOTTED, Color.getBlack());
			break;
		case ToTalCellBorder:
			// Setting the line style of the left border
			style.setBorder(BorderType.LEFT_BORDER, CellBorderType.DOUBLE, Color.getBlack());
			// Setting the line style of the right border
			style.setBorder(BorderType.RIGHT_BORDER, CellBorderType.THIN, Color.getBlack());
			break;
		case TotalHeaderBorder:
			// Setting the line style of the top border
			style.setBorder(BorderType.TOP_BORDER, CellBorderType.THIN, Color.getBlack());
			// Setting the line style of the bottom border
			style.setBorder(BorderType.BOTTOM_BORDER, CellBorderType.THIN, Color.getBlack());
			// Setting the line style of the left border
			style.setBorder(BorderType.LEFT_BORDER, CellBorderType.DOUBLE, Color.getBlack());
			// Setting the line style of the right border
			style.setBorder(BorderType.RIGHT_BORDER, CellBorderType.THIN, Color.getBlack());
			break;
		}
		
		// Alignment.
		if (styleModel.isCenterText) {
			style.setVerticalAlignment(TextAlignmentType.CENTER);
			style.setHorizontalAlignment(TextAlignmentType.CENTER);
		} else {
			style.setHorizontalAlignment(TextAlignmentType.RIGHT);
		}
		if (styleModel.isWrapText) {
			style.setTextWrapped(true);
		}
		
		// Number formart.
		if (styleModel.isNummeric) {
			style.setCustom("#,##0;-#,##0");
		}
		cell.setStyle(style);
	}
	
	/**
	 * The Enum WLBorderType.
	 */
	protected enum WLBorderType {
		
		/** The None. */
		None,
		
		/** The Outside. */
		Outside,
		
		/** The Item cell border. */
		ItemCellBorder,
		
		/** The To tal cell border. */
		ToTalCellBorder,
		
		/** The Total header border. */
		TotalHeaderBorder
	}
	
	/**
	 * The Class StyleModel.
	 */
	public static class StyleModel {
		
		/** The background color. */
		public Color backgroundColor;
		
		/** The border type. */
		public WLBorderType borderType;
		
		/** The is center text. */
		public boolean isCenterText;
		
		/** The is wrap text. */
		public boolean isWrapText;
		
		/** The is nummeric. */
		public boolean isNummeric;
		
		/**
		 * Creates the header cell style.
		 *
		 * @return the style model
		 */
		public static StyleModel createHeaderCellStyle(boolean isTotalCell) {
			StyleModel style = new StyleModel();
			style.backgroundColor = BLUE_COLOR;
			style.borderType = isTotalCell ? WLBorderType.TotalHeaderBorder : WLBorderType.Outside;
			style.isCenterText = true;
			return style;
		}
		
		/**
		 * Creates the total cell style.
		 *
		 * @param backgroundColor the background color
		 * @return the style model
		 */
		public static StyleModel createTotalCellStyle(Color backgroundColor) {
			StyleModel style = new StyleModel();
			style.backgroundColor = backgroundColor;
			style.borderType = WLBorderType.ToTalCellBorder;
			style.isNummeric = true;
			return style;
		}
		
		/**
		 * Creates the item cell style.
		 *
		 * @param backgroundColor the background color
		 * @param isSetBorder the is set border
		 * @return the style model
		 */
		public static StyleModel createItemCellStyle(Color backgroundColor, boolean isSetBorder) {
			StyleModel style = new StyleModel();
			style.backgroundColor = backgroundColor;
			style.borderType = isSetBorder ? WLBorderType.ItemCellBorder : WLBorderType.None;
			style.isNummeric = true;
			return style;
		}
		
		/**
		 * Creates the name cell style.
		 *
		 * @param backgroundColor the background color
		 * @return the style model
		 */
		public static StyleModel createNameCellStyle(Color backgroundColor) {
			StyleModel style = new StyleModel();
			style.backgroundColor = backgroundColor;
			style.borderType = WLBorderType.Outside;
			style.isNummeric = true;
			return style;
		}
	}
}
