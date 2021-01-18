package nts.uk.ctx.at.request.infra.repository.application;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.logging.log4j.util.Strings;

import com.aspose.cells.Cell;
import com.aspose.cells.Cells;
import com.aspose.cells.PageSetup;
import com.aspose.cells.ShapeCollection;
import com.aspose.cells.TextBox;
import com.aspose.cells.TextBoxCollection;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;
import com.aspose.cells.WorksheetCollection;

import nts.arc.i18n.I18NText;
import nts.arc.layer.infra.file.export.FileGeneratorContext;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApprovalBehaviorAtrImport_New;
import nts.uk.ctx.at.request.dom.application.common.service.print.ApplicationGenerator;
import nts.uk.ctx.at.request.dom.application.common.service.print.ApproverPrintDetails;
import nts.uk.ctx.at.request.dom.application.common.service.print.PrintContentOfApp;
import nts.uk.ctx.at.request.dom.application.stamp.StampRequestMode;
import nts.uk.ctx.at.request.dom.setting.company.appreasonstandard.AppStandardReasonCode;
import nts.uk.ctx.at.request.infra.repository.application.appabsence.AsposeApplyForLeave;
import nts.uk.ctx.at.request.infra.repository.application.businesstrip.AposeBusinessTrip;
import nts.uk.ctx.at.request.infra.repository.application.gobackdirectly.AsposeGoReturnDirectly;
import nts.uk.ctx.at.request.infra.repository.application.holidayshipment.print.AsposeHolidayShipment;
import nts.uk.ctx.at.request.infra.repository.application.holidaywork.AsposeAppHolidayWork;
import nts.uk.ctx.at.request.infra.repository.application.lateleaveearly.AsposeLateLeaveEarly;
import nts.uk.ctx.at.request.infra.repository.application.optional.AposeOptionalItem;
import nts.uk.ctx.at.request.infra.repository.application.overtime.AsposeAppOverTime;
import nts.uk.ctx.at.request.infra.repository.application.stamp.AsposeAppStamp;
import nts.uk.ctx.at.request.infra.repository.application.workchange.AsposeWorkChange;
import nts.uk.shr.infra.file.report.aspose.cells.AsposeCellsReportContext;
import nts.uk.shr.infra.file.report.aspose.cells.AsposeCellsReportGenerator;

/**
 * refactor 4
 * @author Doan Duy Hung
 *
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class AsposeApplication extends AsposeCellsReportGenerator implements ApplicationGenerator {

	@Inject
	private AsposeWorkChange asposeWorkChange;

	@Inject
	private AsposeLateLeaveEarly asposeLateLeaveEarly;

	@Inject
	private AsposeAppStamp asposeAppStamp;

	@Inject
	private AposeBusinessTrip aposeBusinessTrip;

	@Inject
	private AposeOptionalItem aposeOptionalItem;
	
	@Inject
	private AsposeGoReturnDirectly asposeGoReturnDirectly;
	
	@Inject
	private AsposeAppOverTime asposeAppOverTime;
	
	@Inject
	private AsposeAppHolidayWork asposeAppHolidayWork;
	
	@Inject
	private AsposeApplyForLeave asposeApplyForLeave;
	
	@Inject
	private AsposeHolidayShipment asposeHolidayShipment;
	
	private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy年 MM月 dd日 (E)", Locale.JAPAN);

	@Override
	public void generate(FileGeneratorContext generatorContext, PrintContentOfApp printContentOfApp, ApplicationType appType) {
		try {
			AsposeCellsReportContext designer;
			if (appType == ApplicationType.STAMP_APPLICATION) {
				if (printContentOfApp.getOpAppStampOutput().isPresent()
						&& printContentOfApp.getOpAppStampOutput().get().getAppDispInfoStartupOutput()
								.getAppDetailScreenInfo().isPresent()
						&& printContentOfApp.getOpAppStampOutput().get().getAppDispInfoStartupOutput()
								.getAppDetailScreenInfo().get().getApplication().getOpStampRequestMode().isPresent()) {
					// AnhNM
					// get file template for KAF002
					designer = this.createContext(this.getFileTemplateStamp(
							printContentOfApp.getOpAppStampOutput().get().getAppDispInfoStartupOutput()
									.getAppDetailScreenInfo().get().getApplication().getOpStampRequestMode().get()));
				} else {
					throw new RuntimeException("No data to print");
				}
			} else {
				designer = this.createContext(this.getFileTemplate(appType));
			}
			Workbook workbook = designer.getWorkbook();
			WorksheetCollection worksheets = workbook.getWorksheets();
			Worksheet worksheet = worksheets.get(0);

			// AnhNM
			// copy cell 7th to 8th before import value to header
			if (appType == ApplicationType.STAMP_APPLICATION) {
				this.asposeAppStamp.copyCells(worksheet);
			}

			printPageHeader(worksheet, printContentOfApp);
			printTopKAF000(worksheet, printContentOfApp);
			
			if (!appType.equals(ApplicationType.COMPLEMENT_LEAVE_APPLICATION)) {
			    printTopKAF000AppDate(worksheet, printContentOfApp);
			}
			
			printTopKAF000PrePost(worksheet, printContentOfApp);

			// AnhNM
			// condition for mode: KAF002
			if (appType == ApplicationType.STAMP_APPLICATION) {
				printEachAppContent(worksheet, printContentOfApp, appType,
						printContentOfApp.getOpAppStampOutput().get().getAppDispInfoStartupOutput()
								.getAppDetailScreenInfo().get().getApplication().getOpStampRequestMode().get());
			} else {
				printEachAppContent(worksheet, printContentOfApp, appType, null);
			}

			designer.getDesigner().setWorkbook(workbook);
			designer.processDesigner();

			designer.saveAsPdf(this.createNewFile(generatorContext, this.getReportName(printContentOfApp.getApplicationName() + ".pdf")));
//			designer.saveAsExcel(this.createNewFile(generatorContext, this.getReportName(printContentOfApp.getApplicationName() + ".xlsx")));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	// AnhNM
	private String getFileTemplateStamp(StampRequestMode stampRequestMode) {
		if (stampRequestMode.value == 0) {
			return "application/KAF002_打刻申請_template.xlsx";
		} else {
			return "application/KAF002_レコーダーイメージ申請_template.xlsx";
		}
	}

	private void printEachAppContent(Worksheet worksheet, PrintContentOfApp printContentOfApp, ApplicationType appType,
			StampRequestMode mode) {
		Cell reasonLabel;
		Cell remarkLabel;
		Cell reasonContent;

		switch (appType) {
		case OVER_TIME_APPLICATION:
			AsposeAppOverTime.CalRange result = asposeAppOverTime.printAppOverTimeContent(worksheet, printContentOfApp);
			int startReasonCommon = result.getStartReasonCommon();
			int startReasonLabel = result.getStartReasonLabel();
			reasonLabel = worksheet.getCells().get("B" + (27 - startReasonCommon));
			remarkLabel = worksheet.getCells().get("B" + (27 - startReasonCommon + 9 - startReasonLabel));
			reasonContent = worksheet.getCells().get("D" + (27 - startReasonCommon));
			printBottomKAF000(reasonLabel, remarkLabel, reasonContent, printContentOfApp, appType);
			break;
		case ABSENCE_APPLICATION:
		    int deleteCntAbs = asposeApplyForLeave.printApplyForLeaveContent(worksheet, printContentOfApp);
		    reasonLabel = worksheet.getCells().get("B" + (17- deleteCntAbs));
            remarkLabel = worksheet.getCells().get("B" + (20- deleteCntAbs));
            reasonContent = worksheet.getCells().get("D" + (17- deleteCntAbs));
            printBottomKAF000(reasonLabel, remarkLabel, reasonContent, printContentOfApp, appType);
			break;
		case WORK_CHANGE_APPLICATION:
			int deleteCntWC = asposeWorkChange.printWorkChangeContent(worksheet, printContentOfApp);
			reasonLabel = worksheet.getCells().get("B" + (15- deleteCntWC));
			remarkLabel = worksheet.getCells().get("B" + (18- deleteCntWC));
			reasonContent = worksheet.getCells().get("D" + (15- deleteCntWC));
			printBottomKAF000(reasonLabel, remarkLabel, reasonContent, printContentOfApp, appType);
			break;
		case BUSINESS_TRIP_APPLICATION:
			aposeBusinessTrip.printBusinessTrip(worksheet, printContentOfApp);
			reasonLabel = worksheet.getCells().get("B27");
			remarkLabel = worksheet.getCells().get("B30");
			reasonContent = worksheet.getCells().get("D27");
			printBottomKAF000(reasonLabel, remarkLabel, reasonContent, printContentOfApp, appType);
			break;
		case GO_RETURN_DIRECTLY_APPLICATION:
		    int deleteCnt = asposeGoReturnDirectly.printContentGoReturn(worksheet, printContentOfApp);
		    reasonLabel = worksheet.getCells().get("B" + (13 - deleteCnt));
            remarkLabel = worksheet.getCells().get("B" + (16 - deleteCnt));
            reasonContent = worksheet.getCells().get("D" + (13 - deleteCnt));
            printBottomKAF000(reasonLabel, remarkLabel, reasonContent, printContentOfApp, appType);
			break;
		case HOLIDAY_WORK_APPLICATION:
			List<Integer> deleteRowsList = asposeAppHolidayWork.printAppHolidayWorkContent(worksheet, printContentOfApp);
			reasonLabel = worksheet.getCells().get("B" + (27 - deleteRowsList.get(0)));
			reasonContent = worksheet.getCells().get("D" + (27 - deleteRowsList.get(0)));
			remarkLabel = worksheet.getCells().get("B" + (33 - deleteRowsList.get(0) - deleteRowsList.get(1)));
			printBottomKAF000(reasonLabel, remarkLabel, reasonContent, printContentOfApp, appType);
			break;
		case STAMP_APPLICATION:
			if (mode.value == 0) {
				asposeAppStamp.printAppStampContent(worksheet, printContentOfApp);
			} else {
				asposeAppStamp.printAppStampContent(worksheet, printContentOfApp);
				reasonLabel = worksheet.getCells().get("B11");
				remarkLabel = worksheet.getCells().get("B14");
				reasonContent = worksheet.getCells().get("D11");
				printBottomKAF000(reasonLabel, remarkLabel, reasonContent, printContentOfApp, appType);
			}
			break;
		case ANNUAL_HOLIDAY_APPLICATION:
			break;
		case EARLY_LEAVE_CANCEL_APPLICATION:
			asposeLateLeaveEarly.printLateEarlyContent(worksheet, printContentOfApp);
			reasonLabel = worksheet.getCells().get("B13");
			remarkLabel = worksheet.getCells().get("B16");
			reasonContent = worksheet.getCells().get("D13");
			printBottomKAF000(reasonLabel, remarkLabel, reasonContent, printContentOfApp, appType);
			asposeLateLeaveEarly.deleteEmptyRow(worksheet);
			break;
		case COMPLEMENT_LEAVE_APPLICATION:
		    int deleteCntHS = asposeHolidayShipment.printHolidayShipmentContent(worksheet, printContentOfApp);
		    reasonLabel = worksheet.getCells().get("B" + (18 - deleteCntHS));
            remarkLabel = worksheet.getCells().get("B" + (21 - deleteCntHS));
            reasonContent = worksheet.getCells().get("D" + (18 - deleteCntHS));
            printBottomKAF000(reasonLabel, remarkLabel, reasonContent, printContentOfApp, appType);
			break;
		case OPTIONAL_ITEM_APPLICATION:
			aposeOptionalItem.printOptionalItem(worksheet, printContentOfApp);
			reasonLabel = worksheet.getCells().get("B20");
			remarkLabel = worksheet.getCells().get("B23");
			reasonContent = worksheet.getCells().get("D20");
			printBottomKAF000(reasonLabel, remarkLabel, reasonContent, printContentOfApp, appType);
			aposeOptionalItem.deleteEmptyRow(worksheet);
			break;
		default:
			break;
		}
	}

	private String getFileTemplate(ApplicationType appType) {
		switch (appType) {
		case OVER_TIME_APPLICATION:
			return "application/KAF005_template.xlsx";
		case ABSENCE_APPLICATION:
			return "application/KAF006_template.xlsx";
		case WORK_CHANGE_APPLICATION:
			return "application/KAF007_template.xlsx";
		case BUSINESS_TRIP_APPLICATION:
			return "application/KAF008_template.xlsx";
		case GO_RETURN_DIRECTLY_APPLICATION:
			return "application/KAF009_template.xlsx";
		case HOLIDAY_WORK_APPLICATION:
			return "application/KAF010_template.xlsx";
		case STAMP_APPLICATION:
			return "";
		case ANNUAL_HOLIDAY_APPLICATION:
			return "";
		case EARLY_LEAVE_CANCEL_APPLICATION:
			return "application/KAF004_template.xlsx";
		case COMPLEMENT_LEAVE_APPLICATION:
			return "";
		case OPTIONAL_ITEM_APPLICATION:
			return "application/KAF020_template.xlsx";
		default:
			return "testAppTemplate";
		}
	}

	private void printPageHeader(Worksheet worksheet, PrintContentOfApp printContentOfApp) {
		PageSetup pageSetup = worksheet.getPageSetup();
		pageSetup.setFirstPageNumber(1);
		pageSetup.setHeader(0, "&9&\"ＭＳ ゴシック\"" + printContentOfApp.getCompanyName());
		pageSetup.setHeader(1, "&16&\"ＭＳ ゴシック\"" + printContentOfApp.getApplicationName());
		pageSetup.setHeader(2, "&9&\"ＭＳ ゴシック\"" + GeneralDateTime.now().toString());
	}

	private void printTopKAF000(Worksheet worksheet, PrintContentOfApp printContentOfApp) {
		Cells cells = worksheet.getCells();
		TextBoxCollection textBoxCollection = worksheet.getTextBoxes();
		// resource text
		Cell cellB3 = cells.get("B3");
		cellB3.setValue(I18NText.getText("Com_Workplace"));
		Cell cellB4 = cells.get("B4");
		cellB4.setValue(I18NText.getText("Com_Person"));
//		Cell cellB6 = cells.get("B6");
//		cellB6.setValue(I18NText.getText("KAF000_49"));
//		Cell cellB7 = cells.get("B7");
//		cellB7.setValue(I18NText.getText("KAF000_46"));
		// value
		Cell cellC3 = cells.get("C3");
		cellC3.setValue(printContentOfApp.getWorkPlaceName());
		Cell cellC4 = cells.get("C4");
		cellC4.setValue(printContentOfApp.getEmployeeInfoLst().get(0).getBussinessName());
		ShapeCollection sc = worksheet.getShapes();
		if(printContentOfApp.getApproverColumnContents().getApproverPrintDetailsLst().size() > 0) {
			ApproverPrintDetails approverPrintDetails1 = printContentOfApp.getApproverColumnContents().getApproverPrintDetailsLst().get(0);
			if (approverPrintDetails1.getApprovalBehaviorAtr() == ApprovalBehaviorAtrImport_New.APPROVED ||
					approverPrintDetails1.getApprovalBehaviorAtr() == ApprovalBehaviorAtrImport_New.DENIAL) {
				sc.get("APPORVAL1").setPrintable(true);
				Cell cellG1 = cells.get("G1");
				cellG1.setValue(approverPrintDetails1.getAffJobTitleHistoryImport().getJobTitleName());
				TextBox textBoxName1 = textBoxCollection.get("NAME1");
				textBoxName1.setText(approverPrintDetails1.getEmployeeInfoImport().getBussinessName().substring(0, 3));
				TextBox textBoxDate1 = textBoxCollection.get("DATE1");
				textBoxDate1.setText(approverPrintDetails1.getOpApprovalDate().map(x -> x.toString()).orElse(null));
				TextBox textBoxStatus1 = textBoxCollection.get("STATUS1");
				if(approverPrintDetails1.getApprovalBehaviorAtr() == ApprovalBehaviorAtrImport_New.APPROVED) {
					textBoxStatus1.setText(I18NText.getText("KAF000_15"));
				} else {
					textBoxStatus1.setText(I18NText.getText("KAF000_16"));
				}
			} else {
				sc.get("APPORVAL1").setPrintable(false);

			}
		} else {
			sc.get("APPORVAL1").setPrintable(false);
		}
		if(printContentOfApp.getApproverColumnContents().getApproverPrintDetailsLst().size() > 1) {
			ApproverPrintDetails approverPrintDetails2 = printContentOfApp.getApproverColumnContents().getApproverPrintDetailsLst().get(1);
			if (approverPrintDetails2.getApprovalBehaviorAtr() == ApprovalBehaviorAtrImport_New.APPROVED ||
					approverPrintDetails2.getApprovalBehaviorAtr() == ApprovalBehaviorAtrImport_New.DENIAL) {
				sc.get("APPORVAL2").setPrintable(true);
				Cell cellH1 = cells.get("H1");
				cellH1.setValue(approverPrintDetails2.getAffJobTitleHistoryImport().getJobTitleName());
				TextBox textBoxName2 = textBoxCollection.get("NAME2");
				textBoxName2.setText(approverPrintDetails2.getEmployeeInfoImport().getBussinessName().substring(0, 3));
				TextBox textBoxDate2 = textBoxCollection.get("DATE2");
				textBoxDate2.setText(approverPrintDetails2.getOpApprovalDate().map(x -> x.toString()).orElse(null));
				TextBox textBoxStatus2 = textBoxCollection.get("STATUS2");
				if(approverPrintDetails2.getApprovalBehaviorAtr() == ApprovalBehaviorAtrImport_New.APPROVED) {
					textBoxStatus2.setText(I18NText.getText("KAF000_15"));
				} else {
					textBoxStatus2.setText(I18NText.getText("KAF000_16"));
				}
			} else {
				sc.get("APPORVAL2").setPrintable(false);
			}
		} else {
			sc.get("APPORVAL2").setPrintable(false);
		}
		if(printContentOfApp.getApproverColumnContents().getApproverPrintDetailsLst().size() > 2) {
			ApproverPrintDetails approverPrintDetails3 = printContentOfApp.getApproverColumnContents().getApproverPrintDetailsLst().get(2);
			if (approverPrintDetails3.getApprovalBehaviorAtr() == ApprovalBehaviorAtrImport_New.APPROVED ||
					approverPrintDetails3.getApprovalBehaviorAtr() == ApprovalBehaviorAtrImport_New.DENIAL) {
				sc.get("APPORVAL3").setPrintable(true);
				Cell cellI1 = cells.get("I1");
				cellI1.setValue(approverPrintDetails3.getAffJobTitleHistoryImport().getJobTitleName());
				TextBox textBoxName3 = textBoxCollection.get("NAME3");
				textBoxName3.setText(approverPrintDetails3.getEmployeeInfoImport().getBussinessName().substring(0, 3));
				TextBox textBoxDate3 = textBoxCollection.get("DATE3");
				textBoxDate3.setText(approverPrintDetails3.getOpApprovalDate().map(x -> x.toString()).orElse(null));
				TextBox textBoxStatus3 = textBoxCollection.get("STATUS3");
				if(approverPrintDetails3.getApprovalBehaviorAtr() == ApprovalBehaviorAtrImport_New.APPROVED) {
					textBoxStatus3.setText(I18NText.getText("KAF000_15"));
				} else {
					textBoxStatus3.setText(I18NText.getText("KAF000_16"));
				}
			} else {
				sc.get("APPORVAL3").setPrintable(false);
			}
		} else {
			sc.get("APPORVAL3").setPrintable(false);
		}
		if(printContentOfApp.getApproverColumnContents().getApproverPrintDetailsLst().size() > 3) {
			ApproverPrintDetails approverPrintDetails4 = printContentOfApp.getApproverColumnContents().getApproverPrintDetailsLst().get(3);
			if (approverPrintDetails4.getApprovalBehaviorAtr() == ApprovalBehaviorAtrImport_New.APPROVED ||
					approverPrintDetails4.getApprovalBehaviorAtr() == ApprovalBehaviorAtrImport_New.DENIAL) {
				sc.get("APPORVAL4").setPrintable(true);
				Cell cellJ1 = cells.get("J1");
				cellJ1.setValue(approverPrintDetails4.getAffJobTitleHistoryImport().getJobTitleName());
				TextBox textBoxName4 = textBoxCollection.get("NAME4");
				textBoxName4.setText(approverPrintDetails4.getEmployeeInfoImport().getBussinessName().substring(0, 3));
				TextBox textBoxDate4 = textBoxCollection.get("DATE4");
				textBoxDate4.setText(approverPrintDetails4.getOpApprovalDate().map(x -> x.toString()).orElse(null));
				TextBox textBoxStatus4 = textBoxCollection.get("STATUS4");
				if(approverPrintDetails4.getApprovalBehaviorAtr() == ApprovalBehaviorAtrImport_New.APPROVED) {
					textBoxStatus4.setText(I18NText.getText("KAF000_15"));
				} else {
					textBoxStatus4.setText(I18NText.getText("KAF000_16"));
				}
			} else {
				sc.get("APPORVAL4").setPrintable(false);
			}
		} else {
			sc.get("APPORVAL4").setPrintable(false);
		}
		if(printContentOfApp.getApproverColumnContents().getApproverPrintDetailsLst().size() > 4) {
			ApproverPrintDetails approverPrintDetails5 = printContentOfApp.getApproverColumnContents().getApproverPrintDetailsLst().get(4);
			if (approverPrintDetails5.getApprovalBehaviorAtr() == ApprovalBehaviorAtrImport_New.APPROVED ||
					approverPrintDetails5.getApprovalBehaviorAtr() == ApprovalBehaviorAtrImport_New.DENIAL) {
				sc.get("APPORVAL5").setPrintable(true);
				Cell cellK1 = cells.get("K1");
				cellK1.setValue(approverPrintDetails5.getAffJobTitleHistoryImport().getJobTitleName());
				TextBox textBoxName5 = textBoxCollection.get("NAME5");
				textBoxName5.setText(approverPrintDetails5.getEmployeeInfoImport().getBussinessName().substring(0, 3));
				TextBox textBoxDate5 = textBoxCollection.get("DATE5");
				textBoxDate5.setText(approverPrintDetails5.getOpApprovalDate().map(x -> x.toString()).orElse(null));
				TextBox textBoxStatus5 = textBoxCollection.get("STATUS5");
				if(approverPrintDetails5.getApprovalBehaviorAtr() == ApprovalBehaviorAtrImport_New.APPROVED) {
					textBoxStatus5.setText(I18NText.getText("KAF000_15"));
				} else {
					textBoxStatus5.setText(I18NText.getText("KAF000_16"));
				}
			} else {
				sc.get("APPORVAL5").setPrintable(false);
			}
		} else {
			sc.get("APPORVAL5").setPrintable(false);
		}

//		Cell cellD6 = cells.get("D6");
//		GeneralDate startDate = printContentOfApp.getAppStartDate();
//		GeneralDate endDate = printContentOfApp.getAppEndDate();
//		if(startDate != null && endDate != null) {
//			if(startDate.equals(endDate)) {
//				cellD6.setValue(printContentOfApp.getAppDate().toString("yyyy年　MM月　dd日　(E)"));
//			} else {
//				String text = startDate.toString("yyyy年　MM月　dd日　(E)") + "～" + endDate.toString("yyyy年　MM月　dd日　(E)");
//				cellD6.setValue(text);
//			}
//		} else {
//			cellD6.setValue(printContentOfApp.getAppDate().toString("yyyy年　MM月　dd日　(E)"));
//		}

//		Cell cellD7 = cells.get("D7");
//		cellD7.setValue(printContentOfApp.getPrePostAtr().name);
	}
	
	private void printTopKAF000AppDate(Worksheet worksheet, PrintContentOfApp printContentOfApp) {
        Cells cells = worksheet.getCells();
        
        Cell cellB6 = cells.get("B6");
        cellB6.setValue(I18NText.getText("KAF000_49"));
        
        Cell cellD6 = cells.get("D6");
        GeneralDate startDate = printContentOfApp.getAppStartDate();
        GeneralDate endDate = printContentOfApp.getAppEndDate();
        if(startDate != null && endDate != null) {
            if(startDate.equals(endDate)) {
                cellD6.setValue(dateTimeFormatter.format(printContentOfApp.getAppDate().localDate()));
            } else {
                String text = dateTimeFormatter.format(startDate.localDate()) + "～" + dateTimeFormatter.format(endDate.localDate());
                cellD6.setValue(text);
            }
        } else {
            cellD6.setValue(dateTimeFormatter.format(printContentOfApp.getAppDate().localDate()));
        }
    }
	
	private void printTopKAF000PrePost(Worksheet worksheet, PrintContentOfApp printContentOfApp) {
	    Cells cells = worksheet.getCells();
	    
	    Cell cellB7 = cells.get("B7");
        cellB7.setValue(I18NText.getText("KAF000_46"));
        
        Cell cellD7 = cells.get("D7");
        cellD7.setValue(printContentOfApp.getPrePostAtr().name);
	}

	private void printBottomKAF000(Cell reasonLabel, Cell remarkLabel, Cell reasonContent, PrintContentOfApp printContentOfApp, ApplicationType appType) {
		reasonLabel.setValue(I18NText.getText("KAF000_52"));
		remarkLabel.setValue(I18NText.getText("KAF000_59"));
		String appReasonStandard = Strings.EMPTY;
		
		if (appType.equals(ApplicationType.ABSENCE_APPLICATION)) {		  
		    Optional<AppStandardReasonCode> reasonCD = printContentOfApp.getOpPrintContentApplyForLeave().get()
                    .getAppAbsenceStartInfoOutput().getAppDispInfoStartupOutput().getAppDetailScreenInfo()
                    .get().getApplication().getOpAppStandardReasonCD();
		    
		    if (reasonCD.isPresent()) {
		        appReasonStandard = printContentOfApp.getOpPrintContentApplyForLeave().get()
		                .getAppAbsenceStartInfoOutput().getAppDispInfoStartupOutput()
		                .getAppDispInfoNoDateOutput().getReasonTypeItemLst().stream().filter(x -> x.getAppStandardReasonCD().equals(reasonCD.get()))
		                .findFirst().map(x -> x.getReasonForFixedForm().v()).orElse(null);
		    }
		} else {
		    if(printContentOfApp.getAppReasonStandard() != null) {
	            appReasonStandard = printContentOfApp.getAppReasonStandard().getReasonTypeItemLst().stream().findFirst()
	                .map(x -> x.getReasonForFixedForm().v()).orElse(null);
	        }
		}
//		if(printContentOfApp.getAppReasonStandard() != null) {
//			appReasonStandard = printContentOfApp.getAppReasonStandard().getReasonTypeItemLst().stream().findFirst()
//				.map(x -> x.getReasonForFixedForm().v()).orElse(null);
//		}
		String appReason = Strings.EMPTY;
		if(printContentOfApp.getOpAppReason() != null) {
			appReason = printContentOfApp.getOpAppReason().v();
		}
		reasonContent.setValue(appReasonStandard + "\n" + appReason);
	}

}
