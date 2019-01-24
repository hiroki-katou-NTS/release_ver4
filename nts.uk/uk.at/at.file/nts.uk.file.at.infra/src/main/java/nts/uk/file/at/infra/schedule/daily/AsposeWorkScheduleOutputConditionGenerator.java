/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.at.infra.schedule.daily;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;

import org.apache.commons.lang3.StringUtils;

import com.aspose.cells.BorderType;
import com.aspose.cells.Cell;
import com.aspose.cells.CellBorderType;
import com.aspose.cells.Cells;
import com.aspose.cells.Color;
import com.aspose.cells.Font;
import com.aspose.cells.PageSetup;
import com.aspose.cells.Range;
import com.aspose.cells.Style;
import com.aspose.cells.TextAlignmentType;
import com.aspose.cells.Workbook;
import com.aspose.cells.WorkbookDesigner;
import com.aspose.cells.Worksheet;
import com.aspose.cells.WorksheetCollection;

import nts.arc.enums.EnumAdaptor;
import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.layer.infra.file.export.FileGeneratorContext;
import nts.arc.task.data.TaskDataSetter;
import nts.arc.task.parallel.ManagedParallelWithContext;
import nts.arc.time.GeneralDate;
import nts.gul.text.StringLength;
import nts.uk.ctx.at.function.dom.adapter.dailyattendanceitem.AttendanceItemValueImport;
import nts.uk.ctx.at.function.dom.adapter.dailyattendanceitem.AttendanceResultImport;
import nts.uk.ctx.at.function.dom.dailyworkschedule.AttendanceItemsDisplay;
import nts.uk.ctx.at.function.dom.dailyworkschedule.NameWorkTypeOrHourZone;
import nts.uk.ctx.at.function.dom.dailyworkschedule.OutputItemDailyWorkSchedule;
import nts.uk.ctx.at.function.dom.dailyworkschedule.OutputItemDailyWorkScheduleRepository;
import nts.uk.ctx.at.function.dom.dailyworkschedule.PrintRemarksContent;
import nts.uk.ctx.at.function.dom.dailyworkschedule.RemarksContentChoice;
import nts.uk.ctx.at.record.app.find.dailyperform.editstate.EditStateOfDailyPerformanceDto;
import nts.uk.ctx.at.record.app.find.dailyperform.editstate.EditStateOfDailyPerformanceFinder;
import nts.uk.ctx.at.record.dom.editstate.enums.EditStateSetting;
import nts.uk.ctx.at.record.dom.optitem.OptionalItem;
import nts.uk.ctx.at.record.dom.optitem.OptionalItemRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerErrorRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.ErrorAlarmWorkRecord;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.ErrorAlarmWorkRecordRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.ErrorAlarmWorkRecordCode;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.SystemFixedErrorAlarm;
import nts.uk.ctx.at.record.dom.workrecord.goout.GoingOutReason;
import nts.uk.ctx.at.request.dom.application.common.adapter.workplace.WkpHistImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.workplace.WorkplaceAdapter;
import nts.uk.ctx.at.schedule.dom.adapter.employment.EmploymentHistoryImported;
import nts.uk.ctx.at.schedule.dom.adapter.employment.ScEmploymentAdapter;
import nts.uk.ctx.at.schedule.dom.adapter.executionlog.SCEmployeeAdapter;
import nts.uk.ctx.at.schedule.dom.adapter.executionlog.dto.EmployeeDto;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ValueType;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalAtrOvertime;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.TimeLimitUpperLimitSetting;
import nts.uk.ctx.at.shared.dom.scherec.dailyattendanceitem.adapter.attendanceitemname.AttItemName;
import nts.uk.ctx.at.shared.dom.scherec.dailyattendanceitem.enums.DailyAttendanceAtr;
import nts.uk.ctx.at.shared.dom.scherec.dailyattendanceitem.service.CompanyDailyItemService;
import nts.uk.ctx.at.shared.dom.workingcondition.ManageAtr;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.ctx.bs.company.dom.company.Company;
import nts.uk.ctx.bs.company.dom.company.CompanyRepository;
import nts.uk.ctx.bs.employee.dom.employment.Employment;
import nts.uk.ctx.bs.employee.dom.employment.EmploymentRepository;
import nts.uk.ctx.bs.employee.dom.workplace.config.info.HierarchyCode;
import nts.uk.ctx.bs.employee.dom.workplace.config.info.WorkplaceConfigInfo;
import nts.uk.ctx.bs.employee.dom.workplace.config.info.WorkplaceConfigInfoRepository;
import nts.uk.ctx.bs.employee.dom.workplace.config.info.WorkplaceHierarchy;
import nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfo;
import nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfoRepository;
import nts.uk.file.at.app.export.dailyschedule.ActualValue;
import nts.uk.file.at.app.export.dailyschedule.AttendanceResultImportAdapter;
import nts.uk.file.at.app.export.dailyschedule.FileOutputType;
import nts.uk.file.at.app.export.dailyschedule.FormOutputType;
import nts.uk.file.at.app.export.dailyschedule.OutputConditionSetting;
import nts.uk.file.at.app.export.dailyschedule.PageBreakIndicator;
import nts.uk.file.at.app.export.dailyschedule.TotalDayCountWs;
import nts.uk.file.at.app.export.dailyschedule.TotalWorkplaceHierachy;
import nts.uk.file.at.app.export.dailyschedule.WorkScheduleOutputCondition;
import nts.uk.file.at.app.export.dailyschedule.WorkScheduleOutputGenerator;
import nts.uk.file.at.app.export.dailyschedule.WorkScheduleOutputQuery;
import nts.uk.file.at.app.export.dailyschedule.WorkScheduleSettingTotalOutput;
import nts.uk.file.at.app.export.dailyschedule.data.DailyPerformanceHeaderData;
import nts.uk.file.at.app.export.dailyschedule.data.DailyPerformanceReportData;
import nts.uk.file.at.app.export.dailyschedule.data.DailyPersonalPerformanceData;
import nts.uk.file.at.app.export.dailyschedule.data.DailyReportData;
import nts.uk.file.at.app.export.dailyschedule.data.DailyWorkplaceData;
import nts.uk.file.at.app.export.dailyschedule.data.DetailedDailyPerformanceReportData;
import nts.uk.file.at.app.export.dailyschedule.data.EmployeeReportData;
import nts.uk.file.at.app.export.dailyschedule.data.OutputItemSetting;
import nts.uk.file.at.app.export.dailyschedule.data.WorkplaceDailyReportData;
import nts.uk.file.at.app.export.dailyschedule.data.WorkplaceReportData;
import nts.uk.file.at.app.export.dailyschedule.totalsum.TotalCountDay;
import nts.uk.file.at.app.export.dailyschedule.totalsum.TotalSumRowOfDailySchedule;
import nts.uk.file.at.app.export.dailyschedule.totalsum.TotalValue;
import nts.uk.file.at.app.export.dailyschedule.totalsum.WorkplaceTotal;
import nts.uk.file.at.app.export.employee.jobtitle.EmployeeJobHistExport;
import nts.uk.file.at.app.export.employee.jobtitle.JobTitleImportAdapter;
import nts.uk.file.at.infra.schedule.EmployeePrintOrder;
import nts.uk.file.at.infra.schedule.RowPageTracker;
import nts.uk.screen.at.app.dailyperformance.correction.DailyPerformanceScreenRepo;
import nts.uk.screen.at.app.dailyperformance.correction.datadialog.CodeName;
import nts.uk.screen.at.app.dailyperformance.correction.datadialog.DataDialogWithTypeProcessor;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DateRange;
import nts.uk.screen.at.app.dailyperformance.correction.dto.workinfomation.CalculationStateDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.workinfomation.WorkInfoOfDailyPerformanceDetailDto;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.infra.file.report.aspose.cells.AsposeCellsReportContext;
import nts.uk.shr.infra.file.report.aspose.cells.AsposeCellsReportGenerator;

/**
 * The Class AsposeWorkScheduleOutputConditionGenerator.
 */
@Stateless
public class AsposeWorkScheduleOutputConditionGenerator extends AsposeCellsReportGenerator implements WorkScheduleOutputGenerator{

	/** The workplace config repository. */
	@Inject
	private WorkplaceConfigInfoRepository workplaceConfigRepository;
	
	/** The workplace info repository. */
	@Inject
	private WorkplaceInfoRepository workplaceInfoRepository;
	
	/** The error alarm repository. */
	@Inject
	private EmployeeDailyPerErrorRepository errorAlarmRepository;
	
	/** The attendace adapter. */
	@Inject
	private AttendanceResultImportAdapter attendaceAdapter;
	
	/** The total day count ws. */
	@Inject
	private TotalDayCountWs totalDayCountWs;
	
	/** The error alarm work record repo. */
	@Inject
	private ErrorAlarmWorkRecordRepository errorAlarmWorkRecordRepo;
	
	/** The edit state finder. */
	@Inject
	private EditStateOfDailyPerformanceFinder editStateFinder;
	
	/** The workplace adapter. */
	@Inject
	private WorkplaceAdapter workplaceAdapter;
	
	/** The employment adapter. */
	@Inject
	private ScEmploymentAdapter employmentAdapter;
	
	/** The output item repo. */
	@Inject
	private OutputItemDailyWorkScheduleRepository outputItemRepo;
	
	/** The employee adapter. */
	@Inject
	private SCEmployeeAdapter employeeAdapter;
	
	/** The employment repository. */
	@Inject
	private EmploymentRepository employmentRepository;
	
	/** The job title adapter. */
//	@Inject
//	private DailyAttendanceItemNameDomainService attendanceNameService;
	
	/** The job title adapter. */
	@Inject
	private JobTitleImportAdapter jobTitleAdapter;
	
	/** The company repository. */
	@Inject
	private CompanyRepository companyRepository;
	
	/** The worktype repo. */
	@Inject
	private WorkTypeRepository worktypeRepo;
	
	/** The work time repo. */
	@Inject
	private WorkTimeSettingRepository workTimeRepo;
	
	/** The daily performance repo. */
	@Inject
	private DailyPerformanceScreenRepo dailyPerformanceRepo;
	
	/** The data processor. */
	@Inject
	private DataDialogWithTypeProcessor dataProcessor;
	
//	@Inject
//	private DailyAttdItemAuthRepository daiAttItemAuthRepo;
	
	/** The company daily item service. */
	@Inject
	private CompanyDailyItemService companyDailyItemService;
	
	@Inject
	private ManagedParallelWithContext parallel;
	
	/** The optional item repo. */
	@Inject
	private OptionalItemRepository optionalItemRepo;
	
	/** The Constant filename. */
	private static final String TEMPLATE_DATE = "report/KWR001_Date.xlsx";
	
	/** The Constant filename. */
	private static final String TEMPLATE_EMPLOYEE = "report/KWR001_Employee.xlsx";
	
	/** The Constant DATA_PREFIX. */
	private static final String DATA_PREFIX = "DATA_";
	
	/** The Constant DATA_PREFIX_NO_WORKPLACE. */
	private static final String DATA_PREFIX_NO_WORKPLACE = "NOWPK_";
	
	/** The Constant CHUNK_SIZE. */
	private static final int CHUNK_SIZE = 16;
	
	/** The Constant LIMIT_DATA_PACK. */
	private static final int LIMIT_DATA_PACK = 5;
	
	/** The Constant LIMIT_REMARK_INPUT. */
	private static final int LIMIT_REMARK_INPUT = 19;
	
	/** The Constant DATA_COLUMN_INDEX. */
	private static final int[] DATA_COLUMN_INDEX = {3, 8, 10, 14, 16, 39};
	
	/** The Constant MASTER_UNREGISTERED. */
	private static final String MASTER_UNREGISTERED = "マスタ未登録";
	
	/** The Constant ATTENDANCE_ID_WORK_TYPE. */
	// All attendance ID which has value type = CODE
	private static final int[] ATTENDANCE_ID_WORK_TYPE = {1, 28};
	
	/** The Constant ATTENDANCE_ID_WORK_TIME. */
	private static final int[] ATTENDANCE_ID_WORK_TIME = {2, 29};
	
	/** The Constant ATTENDANCE_ID_WORK_LOCATION. */
	private static final int[] ATTENDANCE_ID_WORK_LOCATION = {30, 33, 36, 38, 40, 43, 46, 48, 50, 52, 54, 56, 58, 60, 62, 64, 66, 68, 70, 72, 74, 76, 78, 80, 82, 84, 87, 90, 94, 97, 
			101, 104, 108, 111, 115, 118, 122, 125, 129, 132, 136, 139, 143, 146, 150, 153, 156, 158, 162, 164, 168, 170, 174, 176, 180, 182, 186, 188, 192, 194, 198, 200, 204, 206, 210, 212};
	
	/** The Constant ATTENDANCE_ID_REASON. */
	private static final int[] ATTENDANCE_ID_REASON = {438, 443, 448, 453, 458, 801, 806, 811, 816, 821};
	
	/** The Constant ATTENDANCE_ID_WORKPLACE. */
	private static final int ATTENDANCE_ID_WORKPLACE = 623;
	
	/** The Constant ATTENDANCE_ID_CLASSIFICATION. */
	private static final int ATTENDANCE_ID_CLASSIFICATION = 624;
	
	/** The Constant ATTENDANCE_ID_POSITION. */
	private static final int ATTENDANCE_ID_POSITION = 625;
	
	/** The Constant ATTENDANCE_ID_EMPLOYMENT. */
	private static final int ATTENDANCE_ID_EMPLOYMENT = 626;
	
	/** The Constant ATTENDANCE_ID_USE_MAP. */
	// All attendance ID which has value type = ATTR
	private static final int[] ATTENDANCE_ID_USE_MAP = {};
	
	/** The Constant ATTENDANCE_ID_CALCULATION_MAP. */
	private static final int[] ATTENDANCE_ID_CALCULATION_MAP = {627, 628, 629, 630, 631, 632, 633, 634, 635, 636, 637, 638, 639, 640};
	
	/** The Constant ATTENDANCE_ID_OVERTIME_MAP. */
	private static final int[] ATTENDANCE_ID_OVERTIME_MAP = {824, 825, 826, 827, 828, 829, 830, 831, 832};
	
	/** The Constant ATTENDANCE_ID_OUTSIDE_MAP. */
	private static final int[] ATTENDANCE_ID_OUTSIDE_MAP = {86, 93, 100, 107, 114, 121, 128, 135, 142, 149};
	
	/** The Constant ATTENDANCE_ID_OPTIONAL_START. */
	// Attendance id with optional item from KMK002, for workaround
	private static final int ATTENDANCE_ID_OPTIONAL_START = 641;
	
	/** The Constant ATTENDANCE_ID_OPTIONAL_END. */
	private static final int ATTENDANCE_ID_OPTIONAL_END = 740;
	
	/** The Constant FONT_FAMILY. */
	private static final String FONT_FAMILY = "ＭＳ ゴシック";
	
	/** The Constant FONT_SIZE. */
	private static final int FONT_SIZE = 9;
	
	/* (non-Javadoc)
	 * @see nts.uk.file.at.app.export.dailyschedule.WorkScheduleOutputGenerator#generate(nts.uk.file.at.app.export.dailyschedule.WorkScheduleOutputCondition, nts.uk.ctx.bs.employee.dom.workplace.config.info.WorkplaceConfigInfo, nts.uk.file.at.app.export.dailyschedule.WorkScheduleOutputQuery)
	 */
	@Override
	public void generate(FileGeneratorContext generatorContext, TaskDataSetter setter, WorkScheduleOutputQuery query) {
		AsposeCellsReportContext reportContext = null;
		WorkScheduleOutputCondition condition = query.getCondition();
		if (condition.getOutputType() == FormOutputType.BY_EMPLOYEE) {
			reportContext = this.createContext(TEMPLATE_EMPLOYEE);
		} else {
			reportContext = this.createContext(TEMPLATE_DATE);
		}
		
		// ドメインモデル「日別勤務表の出力項目」を取得する
		Optional<OutputItemDailyWorkSchedule> optOutputItemDailyWork = outputItemRepo.findByCidAndCode(AppContexts.user().companyId(), query.getCondition().getCode().v());
		if (!optOutputItemDailyWork.isPresent()) {
			throw new BusinessException(new RawErrorMessage("Msg_1141"));
		}
		
		OutputItemDailyWorkSchedule outputItemDailyWork = optOutputItemDailyWork.get();
		
		Workbook workbook;
		try {
			workbook = reportContext.getWorkbook();
			
			WorkbookDesigner designer = new WorkbookDesigner();
			designer.setWorkbook(workbook);
			
			TotalSumRowOfDailySchedule sum = new TotalSumRowOfDailySchedule();
			sum.setPersonalTotal(new ArrayList<>());
			
			// Calculate row size and get sheet
			List<AttendanceItemsDisplay> lstAttendanceItemDisplay = outputItemDailyWork.getLstDisplayedAttendance();
			List<Integer> lstAttendanceId = lstAttendanceItemDisplay.stream().sorted((o1, o2) -> (o1.getOrderNo() - o2.getOrderNo()))
					.map(x -> x.getAttendanceDisplay()).collect(Collectors.toList());
			String companyID = AppContexts.user().companyId();
			String roleId = AppContexts.user().roles().forAttendance();
			List<AttItemName> lstDailyAttendanceItem = companyDailyItemService.getDailyItems(companyID, Optional.of(roleId),
					lstAttendanceId, Arrays.asList(DailyAttendanceAtr.values()));
			int nListOutputCode = lstDailyAttendanceItem.size();
			int nSize;
			if (nListOutputCode % CHUNK_SIZE == 0) {
				nSize = nListOutputCode / CHUNK_SIZE;
			}
			else {
				nSize = nListOutputCode / CHUNK_SIZE + 1;
			}
			
			/**
			 * Collect data
			 */
			DailyPerformanceReportData reportData = new DailyPerformanceReportData();
			collectData(reportData, query, condition, optOutputItemDailyWork.get(), nSize, setter);
			
			Worksheet sheet;
			Integer currentRow, dateRow;
			
			if (condition.getOutputType() == FormOutputType.BY_EMPLOYEE) {
				switch (nSize) {
				case 1:
					sheet = workbook.getWorksheets().get(0);
					break;
				case 2:
					sheet = workbook.getWorksheets().get(1);
					break;
				case 3:
				default:
					sheet = workbook.getWorksheets().get(2);
				}
				
			} else {
				switch (nSize) {
				case 1:
					sheet = workbook.getWorksheets().get(3);
					break;
				case 2:
					sheet = workbook.getWorksheets().get(4);
					break;
				case 3:
				default:
					sheet = workbook.getWorksheets().get(5);
					
				}
			}
			currentRow = 2;
			dateRow = 0;
			
			WorksheetCollection sheetCollection = workbook.getWorksheets();
			
			// Write header data
			writeHeaderData(query, outputItemDailyWork, sheet, reportData, dateRow);
			
			// Copy footer
			//copyFooter(sheet, sheetCollection.get(6));
			 	
			// Set all fixed cell
			setFixedData(condition, sheet, reportData, currentRow);
			
			// Write display map
			writeDisplayMap(sheet.getCells(),reportData, currentRow, nSize);
			
			currentRow+=nSize*2;
			
			// Create row page tracker
			DatePeriod period = new DatePeriod(query.getStartDate(), query.getEndDate());
			RowPageTracker rowPageTracker = new RowPageTracker();
			if (nSize == 1 && RowPageTracker.MAX_ROW_PER_PAGE_EMPLOYEE_1 / period.datesBetween().size() > 1) {
				rowPageTracker.initMaxRowAllowed(nSize, condition.getOutputType());
			} else {
				rowPageTracker.initMaxRowAllowed(nSize, condition.getOutputType(), period.datesBetween().size() + 4);
			}
			
			// Write detailed work schedule
			if (condition.getOutputType() == FormOutputType.BY_EMPLOYEE)
				currentRow = writeDetailedWorkSchedule(currentRow, sheetCollection, sheet, reportData.getWorkplaceReportData(), nSize, condition, rowPageTracker);
			else {
				DailyReportData dailyReportData = reportData.getDailyReportData();
				currentRow = writeDetailedDailySchedule(currentRow, sheetCollection, sheet, dailyReportData, nSize, condition, rowPageTracker);
			}
			
			// Delete footer if user doesn't set remark content
			if (!outputItemDailyWork.getLstRemarkContent().stream().anyMatch(remark -> remark.isUsedClassification())) {
				hideFooter(sheet);
			}
			
			alignTopCotent(sheet);
			
			// Rename sheet
			sheet.setName(WorkScheOutputConstants.SHEET_NAME_MONTHLY);
			
			// Move to first position
			sheet.moveTo(0);
			
			// Delete the rest of workbook
			int sheetCount = sheetCollection.getCount();
			for (int i = sheetCount - 1; i > 0; i--) {
				sheetCollection.removeAt(i);
			}
			
			// Process designer
			reportContext.processDesigner();
			
			// Get current date and format it
			DateTimeFormatter jpFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss", Locale.JAPAN);
			String currentFormattedDate = LocalDateTime.now().format(jpFormatter);
			
			// Create print area
			createPrintArea(currentRow, sheet);
			
			// Save workbook
			if (query.getFileType() == FileOutputType.FILE_TYPE_EXCEL) {
				reportContext.saveAsExcel(this.createNewFile(generatorContext, WorkScheOutputConstants.SHEET_FILE_NAME + "_" + currentFormattedDate + ".xlsx"));
			} else {
				reportContext.saveAsPdf(this.createNewFile(generatorContext, WorkScheOutputConstants.SHEET_FILE_NAME + "_" + currentFormattedDate + ".pdf"));
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	
	/**
	 * Sets the font style.
	 *
	 * @param style the new font style
	 */
	private void setFontStyle (Style style){
		
		Font font = style.getFont();
		font.setSize(FONT_SIZE);
		font.setName(FONT_FAMILY);
	}
	
	/**
	 * Collect data.
	 *
	 * @param reportData the report data
	 * @param query the query
	 * @param condition the condition
	 * @param outputItem the output item
	 * @param dataRowCount the data row count
	 * @param setter the setter
	 */
	private void collectData(DailyPerformanceReportData reportData, WorkScheduleOutputQuery query, WorkScheduleOutputCondition condition, OutputItemDailyWorkSchedule outputItem, int dataRowCount, TaskDataSetter setter) {
		String companyId = AppContexts.user().companyId();
		String roleId = AppContexts.user().roles().forAttendance();
		
		reportData.setHeaderData(new DailyPerformanceHeaderData());
		collectHeaderData(reportData.getHeaderData(), condition);
		List<Integer> lstHaveName = collectDisplayMap(reportData.getHeaderData(), outputItem, companyId, roleId);
		GeneralDate endDate = query.getEndDate();
		GeneralDate baseDate = query.getBaseDate();
		
		WorkScheduleQueryData queryData = new WorkScheduleQueryData();
		queryData.setQuery(query);
		
		DatePeriod period = new DatePeriod(query.getStartDate(), endDate);
		
		// Init remark query data container
		RemarkQueryDataContainer remarkQueryDataContainer = new RemarkQueryDataContainer();
		remarkQueryDataContainer.setDailyPerformanceRepo(dailyPerformanceRepo);
		remarkQueryDataContainer.setEditStateFinder(editStateFinder);
		remarkQueryDataContainer.setLstRemarkContent(outputItem.getLstRemarkContent());
		remarkQueryDataContainer.initData(query.getEmployeeId(), period);
		queryData.setRemarkDataContainter(remarkQueryDataContainer);		
		
		List<GeneralDate> lstDate = new DateRange(query.getStartDate(), endDate).toListDate();
		queryData.setDatePeriod(lstDate);
		
		Map<String, WorkplaceInfo> lstWorkplace = new TreeMap<>(); // Automatically sort by code, will need to check hierarchy later
		List<String> lstWorkplaceId = new ArrayList<>();
		List<String> lstEmployeeNoWorkplace = new ArrayList<>();
		
		// Push all employeeId into print order
		List<EmployeePrintOrder> lstEmployeePrintOrder = new ArrayList<>();
		int order = 0;
		
		// Get all workplace of selected employees within given period
		for (String employeeId: query.getEmployeeId()) {
			WkpHistImport workplaceHist = workplaceAdapter.findWkpBySid(employeeId, baseDate);
			if (workplaceHist == null) {
				lstEmployeeNoWorkplace.add(employeeId);
				continue;
			}
			lstWorkplaceId.add(workplaceHist.getWorkplaceId());
			queryData.getLstWorkplaceImport().add(workplaceHist);
			
			// Also add into print order list
			lstEmployeePrintOrder.add(new EmployeePrintOrder(order++, employeeId));
		}
		
		if (!lstEmployeeNoWorkplace.isEmpty()) {
			List<EmployeeDto> lstEmployeeDto = employeeAdapter.findByEmployeeIds(lstEmployeeNoWorkplace);
			int numOfChunks = (int)Math.ceil((double)lstEmployeeDto.size() / LIMIT_DATA_PACK);
			int start, length;
			List<EmployeeDto> lstSplitEmployeeDto;
			for(int i = 0; i < numOfChunks; i++) {
				start = i * LIMIT_DATA_PACK;
	            length = Math.min(lstEmployeeDto.size() - start, LIMIT_DATA_PACK);

	            lstSplitEmployeeDto = lstEmployeeDto.subList(start, start + length);
	            
	            // Convert to json array
	            JsonArrayBuilder arr = Json.createArrayBuilder();
	    		
	    		for (EmployeeDto employee : lstSplitEmployeeDto) {
	    			arr.add(employee.buildJsonObject());
	    		}
	            
	            setter.setData(DATA_PREFIX_NO_WORKPLACE + i, arr.build().toString());
			}
			
			// Remove all of these employees from original list
			lstEmployeeNoWorkplace.stream().forEach(employee -> {
				query.getEmployeeId().remove(employee);
			});
			
			// Stop sequence if no employee left
			if (query.getEmployeeId().isEmpty())
				throw new BusinessException(new RawErrorMessage("Msg_1396"));
		}
		
		List<WorkplaceConfigInfo> lstWorkplaceConfigInfo = workplaceConfigRepository.findByWkpIdsAtTime(companyId, baseDate, lstWorkplaceId);
		queryData.setLstWorkplaceConfigInfo(lstWorkplaceConfigInfo);
		
		// Collect child workplace, automatically sort into tree map
		for (String entry: lstWorkplaceId) {
			lstWorkplace.putAll(collectWorkplaceHierarchy(entry, baseDate, lstWorkplaceConfigInfo));
		}
		
//		List<Integer> itemsId = AttendanceResultImportAdapter.convertList(outputItem.getLstDisplayedAttendance(), x -> x.getAttendanceDisplay());
		List<Integer> itemsId = lstHaveName;
		// Add additional item from remark input
		List<RemarksContentChoice> lstRemarkEnable = outputItem.getLstRemarkContent().stream().filter(x -> x.isUsedClassification()).map(x -> {
			return x.getPrintItem();
		}).collect(Collectors.toList());
		if (lstRemarkEnable.contains(RemarksContentChoice.REMARKS_INPUT))
			itemsId.add(outputItem.getRemarkInputNo().value + 833); // Remark no (0~4) -> attendanceId (833~837)
		
		List<AttendanceResultImport> lstAttendanceResultImport;
		{
			List<AttendanceResultImport> resultsSync = Collections.synchronizedList(new ArrayList<>());
			this.parallel.forEach(query.getEmployeeId(), employeeId -> {
				// Request list #332
				List<AttendanceResultImport> result = attendaceAdapter.getValueOf(Arrays.asList(employeeId), new DatePeriod(query.getStartDate(), query.getEndDate()), itemsId);
				resultsSync.addAll(result);
			});
			lstAttendanceResultImport = new ArrayList<>();
			lstAttendanceResultImport.addAll(resultsSync);
		}
		
		queryData.setLstAttendanceResultImport(lstAttendanceResultImport);
		
		// Extract list employeeId from attendance result list -> List employee won't have those w/o data
		List<String> lstEmployeeWithData = lstAttendanceResultImport.stream().map(attendanceData -> {
			String employeeId = attendanceData.getEmployeeId();
			return employeeId;
		}).collect(Collectors.toList());
		
		// From list employeeId above -> Find back their workplace hierachy code
		Set<String> lstWorkplaceIdWithData = new HashSet<>();
		lstWorkplaceIdWithData = lstEmployeeWithData.stream().map(employeeId -> {
			WkpHistImport workplaceImport = queryData.getLstWorkplaceImport().stream().filter(hist -> StringUtils.equalsIgnoreCase(hist.getEmployeeId(), employeeId) ).findFirst().get();
			WorkplaceHierarchy code = lstWorkplaceConfigInfo.stream().filter(x -> StringUtils.equalsIgnoreCase(x.getLstWkpHierarchy().get(0).getWorkplaceId(), workplaceImport.getWorkplaceId())).findFirst().get().getLstWkpHierarchy().get(0);
			return code.getHierarchyCode().v();
		}).collect(Collectors.toSet());
		
		// This employee list with data, find out all other employees who don't have data.
		List<String> lstEmployeeIdNoData = query.getEmployeeId().stream().filter(x -> !lstEmployeeWithData.contains(x)).collect(Collectors.toList());
		if (!lstEmployeeIdNoData.isEmpty()) {
			List<EmployeeDto> lstEmployeeDto = employeeAdapter.findByEmployeeIds(lstEmployeeIdNoData);
			int numOfChunks = (int)Math.ceil((double)lstEmployeeDto.size() / LIMIT_DATA_PACK);
			int start, length;
			List<EmployeeDto> lstSplitEmployeeDto;
			for(int i = 0; i < numOfChunks; i++) {
				start = i * LIMIT_DATA_PACK;
	            length = Math.min(lstEmployeeDto.size() - start, LIMIT_DATA_PACK);

	            lstSplitEmployeeDto = lstEmployeeDto.subList(start, start + length);
	            
	            // Convert to json array
	            JsonArrayBuilder arr = Json.createArrayBuilder();
	    		
	    		for (EmployeeDto employee : lstSplitEmployeeDto) {
	    			arr.add(employee.buildJsonObject());
	    		}
	            
	            setter.setData(DATA_PREFIX + i, arr.build().toString());
			}
		}
		
		
		// Check lowest level of employee and highest level of output setting, and attendance result count is 0
		// 階層累計行のみ出力する設定の場合、データ取得件数は0件として扱い、エラーメッセージを表示(#Msg_37#)
		int lowestEmployeeLevel = checkLowestWorkplaceLevel(lstWorkplaceIdWithData); // Get lowest possible workplace level -> lowestEmployeeLevel
		WorkScheduleSettingTotalOutput totalOutputCondition = condition.getSettingDetailTotalOutput();
		TotalWorkplaceHierachy outputSetting = condition.getSettingDetailTotalOutput().getWorkplaceHierarchyTotal();
		int highestOutputLevel = outputSetting.getHighestLevelEnabled();
		if ((lowestEmployeeLevel < highestOutputLevel && !totalOutputCondition.isDetails() && !totalOutputCondition.isGrossTotal()
				&& !totalOutputCondition.isPersonalTotal() && !totalOutputCondition.isPersonalTotal() && !totalOutputCondition.isTotalNumberDay()
				&& !totalOutputCondition.isWorkplaceTotal()) || lstAttendanceResultImport.size() == 0) {
			throw new BusinessException(new RawErrorMessage("Msg_37"));
		}
		
		/*
		 *  Collect all data which uses value type = code
		 */
		// 勤務種類を取得する
		if (itemsId.stream().filter(x -> IntStream.of(ATTENDANCE_ID_WORK_TYPE).anyMatch(y -> x == y)).count() > 0) {
			List<WorkType> lstWorkType = worktypeRepo.findByCompanyId(companyId);
			queryData.setLstWorkType(lstWorkType);
		}
		// 就業時間帯を取得する
		if (itemsId.stream().filter(x -> IntStream.of(ATTENDANCE_ID_WORK_TIME).anyMatch(y -> x == y)).count() > 0) {
			List<WorkTimeSetting> lstWorkTime = workTimeRepo.findByCompanyId(companyId);
			queryData.setLstWorkTime(lstWorkTime);
		}
		// 職場を取得する
		if (itemsId.stream().filter(x -> ATTENDANCE_ID_WORKPLACE == x).count() > 0) {
			List<CodeName> lstWorkplaceInfo = dataProcessor.getWorkPlace(companyId, endDate).getCodeNames();
			queryData.setLstWorkplaceInfo(lstWorkplaceInfo);
		}
		// 勤務場所を取得する
		if (itemsId.stream().filter(x -> IntStream.of(ATTENDANCE_ID_WORK_LOCATION).anyMatch(y -> x == y)).count() > 0) {
			List<CodeName> lstWorkLocation = dataProcessor.getServicePlace(companyId).getCodeNames();
			queryData.setLstWorkLocation(lstWorkLocation);
		}
		// 乖離理由を取得する
		if (itemsId.stream().filter(x -> IntStream.of(ATTENDANCE_ID_REASON).anyMatch(y -> x == y)).count() > 0) {
			List<CodeName> lstReason = dataProcessor.getReason(companyId).getCodeNames();
			queryData.setLstReason(lstReason);
		}
		// 分類を取得する
		if (itemsId.stream().filter(x -> IntStream.of(ATTENDANCE_ID_CLASSIFICATION).anyMatch(y -> x == y)).count() > 0) {
			List<CodeName> lstClassification = dataProcessor.getClassification(companyId).getCodeNames();
			queryData.setLstClassification(lstClassification);
		}
		// 職位を取得する
		if (itemsId.stream().filter(x -> ATTENDANCE_ID_POSITION == x).count() > 0) {
			List<CodeName> lstPosition = dataProcessor.getPossition(companyId, endDate).getCodeNames();
			queryData.setLstPosition(lstPosition);
		}
		// 雇用を取得する
		if (itemsId.stream().filter(x -> ATTENDANCE_ID_EMPLOYMENT == x).count() > 0) {
			List<CodeName> lstEmployment = dataProcessor.getEmployment(companyId).getCodeNames();
			queryData.setLstEmployment(lstEmployment);
		}
		
		// Collect optional item from KMK002 if there is at least 1 attendance id fall into id 641 -> 740
		List<Integer> lstOptionalAttendanceId = itemsId.stream()
				.filter(x -> x >= ATTENDANCE_ID_OPTIONAL_START && x <= ATTENDANCE_ID_OPTIONAL_END)
				.map(x -> x - 640)
				.collect(Collectors.toList());
		if (!lstOptionalAttendanceId.isEmpty()) {
			List<OptionalItem> lstOptionalItem = optionalItemRepo.findByListNos(companyId, lstOptionalAttendanceId);
			queryData.setLstOptionalItem(lstOptionalItem);
		}
		
		// Sort attendance display
		List<AttendanceItemsDisplay> lstAttendanceItemsDisplay = outputItem.getLstDisplayedAttendance().stream()
				.filter(item -> itemsId.contains(item.getAttendanceDisplay()))
				.sorted((o1,o2) -> o1.getOrderNo() - o2.getOrderNo()).collect(Collectors.toList());
		queryData.setLstDisplayItem(lstAttendanceItemsDisplay);
		
		if (condition.getOutputType() == FormOutputType.BY_EMPLOYEE) {
			WorkplaceReportData data = new WorkplaceReportData();
			data.workplaceCode = "";
			reportData.setWorkplaceReportData(data);
			
			analyzeInfoExportByEmployee(lstWorkplace, data);
			
			List<EmployeeDto> lstEmloyeeDto = employeeAdapter.findByEmployeeIds(lstEmployeeWithData).stream().sorted((o1, o2)->o1.getEmployeeCode().
                    compareTo(o2.getEmployeeCode())).
                    collect(Collectors.toList());
			
//			// Put these dto into print order list
//			lstEmloyeeDto.stream().forEach(dto -> {
//				lstEmployeePrintOrder.stream().filter(employee -> StringUtils.equals(employee.getEmployeeId(), dto.getEmployeeId())).findFirst().get().setEmployeeDto(Optional.of(dto));
//			});
//			
//			// Filter out all employee w/o data, workplace (no employee dto)
//			List<EmployeePrintOrder> lstEmployeePrintOrderWithData = lstEmployeePrintOrder.stream().filter(employee -> employee.getEmployeeDto().isPresent()).collect(Collectors.toList());
//			
//			// Convert print order list back into list employee dto so we can keep the correct printing order as inputed
//			lstEmloyeeDto = lstEmployeePrintOrderWithData.stream().map(employee -> employee.getEmployeeDto().get()).collect(Collectors.toList());
			
			for (EmployeeDto dto: lstEmloyeeDto) {
				EmployeeReportData employeeReportData = collectEmployeePerformanceDataByEmployee(reportData, queryData, dto, dataRowCount);
				
				// Calculate total count day
				if (condition.getSettingDetailTotalOutput().isTotalNumberDay()) {
					employeeReportData.totalCountDay = totalDayCountWs.calculateAllDayCount(dto.getEmployeeId(), new DateRange(query.getStartDate(), query.getEndDate()), employeeReportData.totalCountDay);
				}
			}
			
			calculateTotalExportByEmployee(data, lstAttendanceItemsDisplay);
		}
		else {
			DailyReportData dailyReportData = new DailyReportData();
			reportData.setDailyReportData(dailyReportData);
			DateRange dateRange = new DateRange(query.getStartDate(), query.getEndDate());
			List<WorkplaceDailyReportData> lstReportData = dateRange.toListDate().stream().map(x -> {
				WorkplaceDailyReportData dailyWorkplaceReportData = new WorkplaceDailyReportData();
				dailyWorkplaceReportData.lstWorkplaceData = new DailyWorkplaceData();
				dailyWorkplaceReportData.lstWorkplaceData.workplaceCode = "";
				dailyWorkplaceReportData.date = x;
				return dailyWorkplaceReportData;
			}).collect(Collectors.toList());
			dailyReportData.setLstDailyReportData(lstReportData);
			
			final Map<String, WorkplaceInfo> lstWorkplaceTemp = lstWorkplace;
			List<String> lstAddedCode = new ArrayList<>();
			lstReportData.forEach(x -> {
				analyzeInfoExportByDate(lstWorkplaceTemp, x.getLstWorkplaceData(), lstAddedCode);
			});
			
			collectEmployeePerformanceDataByDate(reportData, queryData, dataRowCount);
			// Calculate workplace total
			lstReportData.forEach(dailyData -> {
				calculateTotalExportByDate(dailyData.getLstWorkplaceData());
			});
			
			// Calculate gross total
			calculateGrossTotalByDate(dailyReportData);
		}
	}
	
	/**
	 * Check lowest workplace level.
	 *
	 * @param lstWorkplaceId the lst workplace id
	 * @return the int
	 */
	private int checkLowestWorkplaceLevel(Set<String> lstWorkplaceId) {
		int lowestLevel = 0;
		
		for (String workplaceId: lstWorkplaceId) {
			int level = workplaceId.length() / 3;
			if (lowestLevel < level) {
				lowestLevel = level;
			}
		}
		
		return lowestLevel;
	}
	
	/**
	 * Collect employee performance data by date.
	 *
	 * @param reportData the report data
	 * @param queryData the query data
	 * @param dataRowCount the data row count
	 */
	private void collectEmployeePerformanceDataByDate(DailyPerformanceReportData reportData, WorkScheduleQueryData queryData, int dataRowCount) {
		String companyId = AppContexts.user().companyId();
		WorkScheduleOutputQuery query = queryData.getQuery();
		GeneralDate endDate = query.getEndDate();
		WorkScheduleOutputCondition condition = query.getCondition();
		// Always has item because this has passed error check
		OutputItemDailyWorkSchedule outSche = outputItemRepo.findByCidAndCode(companyId, condition.getCode().v()).get();
		
		// Get all data from query data container
		List<GeneralDate> datePeriod = queryData.getDatePeriod();
		List<AttendanceResultImport> lstAttendanceResultImport = queryData.getLstAttendanceResultImport();
		List<AttendanceItemsDisplay> lstDisplayItem = queryData.getLstDisplayItem();
		List<WkpHistImport> lstWorkplaceHistImport = queryData.getLstWorkplaceImport();
		List<OptionalItem> lstOptionalItem = queryData.getLstOptionalItem();
		
		// Get all error alarm code
		List<ErrorAlarmWorkRecordCode> lstErAlCode = condition.getErrorAlarmCode().get();
		
		List<String> lstEmployeeId = query.getEmployeeId();
		List<EmployeeDto> lstEmployeeDto = employeeAdapter.findByEmployeeIds(lstEmployeeId);
		
		List<EmployeeDailyPerError> errorListAllEmployee = errorAlarmRepository.finds(lstEmployeeId, new DatePeriod(query.getStartDate(), endDate));
		// Get list remark check box from screen C (UI)
		List<PrintRemarksContent> lstRemarkContent = outSche.getLstRemarkContent();
		
		Set<String> lstAllErrorCode = errorListAllEmployee.stream().map(x -> x.getErrorAlarmWorkRecordCode().v()).collect(Collectors.toSet());
		List<String> tempErrorCode = new ArrayList<>();
		tempErrorCode.addAll(lstAllErrorCode);
		List<ErrorAlarmWorkRecord> lstAllErrorRecord = errorAlarmWorkRecordRepo.getListErAlByListCode(companyId, tempErrorCode);
		
		for (String employeeId: lstEmployeeId) {
			Optional<EmployeeDto> optEmployeeDto = lstEmployeeDto.stream().filter(employee -> StringUtils.equalsAnyIgnoreCase(employee.getEmployeeId(),employeeId)).findFirst();
			
			datePeriod.stream().forEach(date -> {
				Optional<WorkplaceDailyReportData> optDailyWorkplaceData = reportData.getDailyReportData().getLstDailyReportData().stream().filter(x -> x.getDate().compareTo(date) == 0).findFirst();
				DailyWorkplaceData dailyWorkplaceData = findWorkplace(employeeId,optDailyWorkplaceData.get().getLstWorkplaceData(), query.getBaseDate(), lstWorkplaceHistImport, queryData.getLstWorkplaceConfigInfo());
				if (dailyWorkplaceData != null) {
					DailyPersonalPerformanceData personalPerformanceDate = new DailyPersonalPerformanceData();
					if (optEmployeeDto.isPresent())
					{
						personalPerformanceDate.setEmployeeName(optEmployeeDto.get().getEmployeeName());
						personalPerformanceDate.setEmployeeCode(optEmployeeDto.get().getEmployeeCode());
					}
					dailyWorkplaceData.getLstDailyPersonalData().add(personalPerformanceDate);
					
					lstAttendanceResultImport.stream().filter(x -> (x.getEmployeeId().equals(employeeId) && x.getWorkingDate().compareTo(date) == 0)).forEach(x -> {
						GeneralDate workingDate = x.getWorkingDate();
						
						// ドメインモデル「日別勤務表の出力項目」を取得する
						List<EmployeeDailyPerError> errorList = errorListAllEmployee.stream()
								.filter(error -> StringUtils.equalsAnyIgnoreCase(error.getEmployeeID(), employeeId) && error.getDate().compareTo(workingDate) == 0).collect(Collectors.toList());
						// Manually set error list into remark query data container
						queryData.getRemarkDataContainter().setErrorList(errorList);
						
						List<String> lstRemarkContentStr = new ArrayList<>();
						if (query.getCondition().getErrorAlarmCode().isPresent()) {
							personalPerformanceDate.detailedErrorData = "";
							
							lstRemarkContent.stream().filter(remark -> remark.isUsedClassification()).forEach(remark -> {
								
								// Append 備考入力
								if (remark.getPrintItem() == RemarksContentChoice.REMARKS_INPUT) {
									Optional<AttendanceItemValueImport> optRemarkInput = x.getAttendanceItems().stream().filter(att -> att.getItemId() == outSche.getRemarkInputNo().value + 833).findFirst();
									if (optRemarkInput.isPresent()) {
										String value = optRemarkInput.get().getValue();
//										value = StringUtils.substring(value, 0, 9); // Already dealt with null possibility
										if(value != null) {
											value = StringLength.cutOffAsLengthHalf(value, LIMIT_REMARK_INPUT);
										}
										personalPerformanceDate.detailedErrorData += (value == null? "" : value + " ");
									}
								}
								// Append マスタ未登録
								else if (remark.getPrintItem() == RemarksContentChoice.MASTER_UNREGISTERED) {
									List<AttendanceItemValueImport> lstItemMasterUnregistered = x.getAttendanceItems().stream().
											filter(att -> EnumAdaptor.valueOf(att.getValueType(), ValueType.class) == ValueType.CODE).collect(Collectors.toList());
									
									List<String> names = lstItemMasterUnregistered.stream()
											.map(item -> this.getNameFromCode(item.getItemId(),
													item.getValue(), queryData, outSche))
											.collect(Collectors.toList());

									boolean masterUnregistedFlag = names.stream()
											.anyMatch(item -> StringUtils.equalsIgnoreCase(item,
													MASTER_UNREGISTERED));
										
									if (masterUnregistedFlag) {
										lstRemarkContentStr.add(TextResource.localize(RemarksContentChoice.MASTER_UNREGISTERED.shortName));
									}
								}
								else {
									// Get possible content based on remark choice
									Optional<PrintRemarksContent> optContent = getRemarkContent(employeeId, workingDate, remark.getPrintItem(), lstErAlCode, queryData.getRemarkDataContainter());
									if (optContent.isPresent()) {
										// Add to remark
										lstRemarkContentStr.add(TextResource.localize(optContent.get().getPrintItem().shortName));
									}
								}
							});
							
							// Analyze remark content size and append remark content list
							for (int i = 0; i < lstRemarkContentStr.size(); i++) {
								String remarkContentStr = lstRemarkContentStr.get(i);
								int bufferredLength = personalPerformanceDate.detailedErrorData .length() + 5;
								if (bufferredLength >= 35 && dataRowCount == 1) {
									personalPerformanceDate.detailedErrorData  += " 他" + (lstRemarkContentStr.size() - i - 1) + "件";
								}
								else {
									personalPerformanceDate.detailedErrorData  += " " + remarkContentStr;
								}
							}
						}
						
						// ER/AL
						if (query.getCondition().getConditionSetting() == OutputConditionSetting.USE_CONDITION) {
							boolean erMark = false, alMark = false;
							
							List<String> lstErrorCode = errorList.stream().map(error -> error.getErrorAlarmWorkRecordCode().v()).collect(Collectors.toList());
							// ドメインモデル「勤務実績のエラーアラーム」を取得する
							
							List<ErrorAlarmWorkRecord> lstErrorRecord = lstAllErrorRecord.stream().filter(err -> {
								return  lstErrorCode.contains(err.getCode().v());
							}).collect(Collectors.toList());
							
							for (ErrorAlarmWorkRecord error : lstErrorRecord) {
								// コードから区分を取得する
								switch (error.getTypeAtr()) {
								case ALARM: // 区分　＝　エラー　のとき　AL
									alMark = true; 
									break;
								case ERROR: // 区分　=　アラーム　のとき　ER
									erMark = true;
									break;
								case OTHER:
									break;
								}
								if (erMark && alMark) break;
							}
							if (erMark && alMark) {
								personalPerformanceDate.errorAlarmCode = WorkScheOutputConstants.ER_AL;
							}
							else if (erMark) {
								personalPerformanceDate.errorAlarmCode = WorkScheOutputConstants.ER;
							}
							else if (alMark) {
								personalPerformanceDate.errorAlarmCode = WorkScheOutputConstants.AL;
							}
						}
						personalPerformanceDate.actualValue = new ArrayList<>();
						lstDisplayItem.stream().forEach(item -> {
							Optional<AttendanceItemValueImport> optItemValue = x.getAttendanceItems().stream().filter(att -> att.getItemId() == item.getAttendanceDisplay()).findFirst();
							if (optItemValue.isPresent()) {
								AttendanceItemValueImport itemValue = optItemValue.get();
								ValueType valueTypeEnum = EnumAdaptor.valueOf(itemValue.getValueType(), ValueType.class);
								int attendanceId = itemValue.getItemId();
								if ((valueTypeEnum == ValueType.CODE || valueTypeEnum == ValueType.ATTR) && itemValue.getValue() != null) {
									String value = this.getNameFromCode(attendanceId, itemValue.getValue(), queryData, outSche);
									itemValue.setValue(value);
								}
								// Workaround for optional attendance item from KMK002
								if (attendanceId >= ATTENDANCE_ID_OPTIONAL_START && attendanceId <= ATTENDANCE_ID_OPTIONAL_END) {
									Optional<OptionalItem> optOptionalItem = lstOptionalItem.stream().filter(opt -> opt.getOptionalItemNo().v() == attendanceId - 640).findFirst();
									optOptionalItem.ifPresent(optionalItem -> {
										switch(optionalItem.getOptionalItemAtr()) {
										case TIME:
											//int val = Integer.parseInt(itemValue.getValue());
											itemValue.setValueType(ValueType.TIME.value);
											break;
										case NUMBER:
											itemValue.setValueType(ValueType.COUNT_WITH_DECIMAL.value);
											break;
										case AMOUNT:
											itemValue.setValueType(ValueType.AMOUNT.value);
											break;
										}
									});
									
//									try {
//										int val = Integer.parseInt(itemValue.getValue());
//										itemValue.setValueType(ValueType.TIME.value);
//									} catch (Exception e) {
//										itemValue.setValueType(ValueType.AMOUNT.value);
//									}
								}
								personalPerformanceDate.actualValue.add(new ActualValue(itemValue.getItemId(), itemValue.getValue(), itemValue.getValueType()));
							}
							else {
								personalPerformanceDate.actualValue.add(new ActualValue(item.getAttendanceDisplay(), "", ActualValue.STRING));
							}
							dailyWorkplaceData.hasData = true;
						});
					});
				}
			});
			
			
		}
	}

	/**
	 * Collect employee performance data by employee.
	 *
	 * @param reportData the report data
	 * @param queryData the query data
	 * @param employeeDto the employee dto
	 * @param dataRowCount the data row count
	 * @return the employee report data
	 */
	private EmployeeReportData collectEmployeePerformanceDataByEmployee(DailyPerformanceReportData reportData, WorkScheduleQueryData queryData, EmployeeDto employeeDto, int dataRowCount) {
		String companyId = AppContexts.user().companyId();
		WorkScheduleOutputQuery query = queryData.getQuery();
		GeneralDate endDate = query.getEndDate();
		WorkScheduleOutputCondition condition = query.getCondition();
		
		// Always has item because this has passed error check
		OutputItemDailyWorkSchedule outSche = outputItemRepo.findByCidAndCode(companyId, condition.getCode().v()).get();
		
		// Get all data from query data container
		List<AttendanceResultImport> lstAttendanceResultImport = queryData.getLstAttendanceResultImport();
		List<AttendanceItemsDisplay> lstDisplayItem = queryData.getLstDisplayItem();
		List<WkpHistImport> lstWorkplaceImport = queryData.getLstWorkplaceImport();
		List<WorkplaceConfigInfo> lstWorkplaceConfigInfo = queryData.getLstWorkplaceConfigInfo();
		List<OptionalItem> lstOptionalItem = queryData.getLstOptionalItem();
		String employeeId = employeeDto.getEmployeeId();
		
		List<ErrorAlarmWorkRecordCode> lstErAlCode = condition.getErrorAlarmCode().get();
		
		EmployeeReportData employeeData = new EmployeeReportData();
		
		// Employee code and name
		employeeData.employeeName = employeeDto.getEmployeeName();
		employeeData.employeeCode = employeeDto.getEmployeeCode();
		employeeData.employeeId = employeeId;
		
		// Employment
		Optional<EmploymentHistoryImported> optEmploymentImport = employmentAdapter.getEmpHistBySid(companyId, employeeId, endDate);
		if (optEmploymentImport.isPresent()) {
			String employmentCode = optEmploymentImport.get().getEmploymentCode();
			Optional<Employment> optEmployment = employmentRepository.findEmployment(companyId, employmentCode);
			if (optEmployment.isPresent()) {
				Employment employment = optEmployment.get();
				employeeData.employmentName = employment.getEmploymentName().v();
			}
		}
		
		// Job title
		Optional<EmployeeJobHistExport> optJobTitle = jobTitleAdapter.findBySid(employeeId, endDate);
		if (optJobTitle.isPresent())
			employeeData.position = optJobTitle.get().getJobTitleName();
		else
			employeeData.position = "";
		
		employeeData.lstDetailedPerformance = new ArrayList<>();
		
		WorkplaceReportData workplaceData = findWorkplace(employeeId, reportData.getWorkplaceReportData(), query.getBaseDate(), lstWorkplaceImport, lstWorkplaceConfigInfo);
		workplaceData.lstEmployeeReportData.add(employeeData);
		
		lstAttendanceResultImport.stream().filter(x -> x.getEmployeeId().equals(employeeId)).sorted((o1,o2) -> o1.getWorkingDate().compareTo(o2.getWorkingDate())).forEach(x -> {
			GeneralDate workingDate = x.getWorkingDate();
			
			DetailedDailyPerformanceReportData detailedDate = new DetailedDailyPerformanceReportData();
			detailedDate.setDate(workingDate);
			detailedDate.setDayOfWeek(String.valueOf(workingDate.dayOfWeek()));
			employeeData.lstDetailedPerformance.add(detailedDate);
			
			// ドメインモデル「社員の日別実績エラー一覧」を取得する
			List<EmployeeDailyPerError> errorList = errorAlarmRepository.find(employeeId, workingDate);
			// Manually set error list into remark data container
			queryData.getRemarkDataContainter().setErrorList(errorList);
			
			List<String> lstRemarkContentStr = new ArrayList<>();
			detailedDate.errorDetail = "";
			if (query.getCondition().getErrorAlarmCode().isPresent()) {
				// Get list remark check box from screen C (UI)
				List<PrintRemarksContent> lstRemarkContent = outSche.getLstRemarkContent();
				lstRemarkContent.stream().filter(remark -> remark.isUsedClassification()).forEach(remark -> {
					// Append 備考入力
					if (remark.getPrintItem() == RemarksContentChoice.REMARKS_INPUT) {
						Optional<AttendanceItemValueImport> optRemarkInput = x.getAttendanceItems().stream().filter(att -> att.getItemId() == outSche.getRemarkInputNo().value + 833).findFirst();
						if (optRemarkInput.isPresent()) {
							String value = optRemarkInput.get().getValue();
//							value = StringUtils.substring(value, 0, 9); // Already dealt with null possibility
							if(value != null) {
								value = StringLength.cutOffAsLengthHalf(value, LIMIT_REMARK_INPUT);
							}
							detailedDate.errorDetail += (value == null ? "" : value + " ");
						}
					}
					// Append マスタ未登録
					else if (remark.getPrintItem() == RemarksContentChoice.MASTER_UNREGISTERED) {
						List<AttendanceItemValueImport> lstItemMasterUnregistered = x.getAttendanceItems().stream().
								filter(att -> EnumAdaptor.valueOf(att.getValueType(), ValueType.class) == ValueType.CODE).collect(Collectors.toList());
						
						List<String> names = lstItemMasterUnregistered.stream()
								.map(item -> this.getNameFromCode(item.getItemId(),
										item.getValue(), queryData, outSche))
								.collect(Collectors.toList());

						boolean masterUnregistedFlag = names.stream()
								.anyMatch(item -> StringUtils.equalsIgnoreCase(item,
										MASTER_UNREGISTERED));
							
						if (masterUnregistedFlag) {
							lstRemarkContentStr.add(TextResource.localize(RemarksContentChoice.MASTER_UNREGISTERED.shortName));
						}
					}
					else {
						// Get other possible content based on remark choice
						Optional<PrintRemarksContent> optContent = getRemarkContent(employeeId, workingDate, remark.getPrintItem(), lstErAlCode, queryData.getRemarkDataContainter());
						if (optContent.isPresent()) {
							// Add to remark
							lstRemarkContentStr.add(TextResource.localize(optContent.get().getPrintItem().shortName));
						}
					}
				});
				
				// Analyze remark content size and append remark content list
				for (int i = 0; i < lstRemarkContentStr.size(); i++) {
					String remarkContentStr = lstRemarkContentStr.get(i);
					int bufferredLength = detailedDate.errorDetail.length() + 5;
					if (bufferredLength >= 35 && dataRowCount == 1) {
						detailedDate.errorDetail += " 他" + (lstRemarkContentStr.size() - i - 1) + "件";
					}
					else {
						detailedDate.errorDetail += " " + remarkContentStr;
					}
				}
			}
			
			// ER/AL
			//if (query.getCondition().getConditionSetting() == OutputConditionSetting.USE_CONDITION) {
				boolean erMark = false, alMark = false;
				List<String> lstErrorCode = errorList.stream().map(error -> error.getErrorAlarmWorkRecordCode().v()).collect(Collectors.toList());
				// ドメインモデル「勤務実績のエラーアラーム」を取得する
				List<ErrorAlarmWorkRecord> lstErrorRecord = errorAlarmWorkRecordRepo.getListErAlByListCode(companyId, lstErrorCode);
				
				for (ErrorAlarmWorkRecord error : lstErrorRecord) {
					// コードから区分を取得する
					switch (error.getTypeAtr()) {
					case ALARM: // 区分　＝　エラー　のとき　AL
						alMark = true; 
						break;
					case ERROR: // 区分　=　アラーム　のとき　ER
						erMark = true;
						break;
					case OTHER:
						break;
					}
					if (erMark && alMark) break;
				}
				if (erMark && alMark) {
					detailedDate.errorAlarmMark = WorkScheOutputConstants.ER_AL;
				}
				else if (erMark) {
					detailedDate.errorAlarmMark = WorkScheOutputConstants.ER;
				}
				else if (alMark) {
					detailedDate.errorAlarmMark = WorkScheOutputConstants.AL;
				}
			//}
			
			detailedDate.actualValue = new ArrayList<>();
			lstDisplayItem.stream().forEach(item -> {
				Optional<AttendanceItemValueImport> optItemValue = x.getAttendanceItems().stream().filter(att -> att.getItemId() == item.getAttendanceDisplay()).findFirst();
				if (optItemValue.isPresent()) {
					AttendanceItemValueImport itemValue = optItemValue.get();
					ValueType valueTypeEnum = EnumAdaptor.valueOf(itemValue.getValueType(), ValueType.class);
					int attendanceId = itemValue.getItemId();
					if ((valueTypeEnum == ValueType.CODE || valueTypeEnum == ValueType.ATTR) && itemValue.getValue() != null) {
						String value = this.getNameFromCode(attendanceId, itemValue.getValue(), queryData, outSche);
						itemValue.setValue(value);
					}
					// Workaround for optional attendance item from KMK002
					if (attendanceId >= ATTENDANCE_ID_OPTIONAL_START && attendanceId <= ATTENDANCE_ID_OPTIONAL_END) {
						Optional<OptionalItem> optOptionalItem = lstOptionalItem.stream().filter(opt -> opt.getOptionalItemNo().v() == attendanceId - 640).findFirst();
						optOptionalItem.ifPresent(optionalItem -> {
							switch(optionalItem.getOptionalItemAtr()) {
							case TIME:
								itemValue.setValueType(ValueType.TIME.value);
								break;
							case NUMBER:
								itemValue.setValueType(ValueType.COUNT_WITH_DECIMAL.value);
								break;
							case AMOUNT:
								itemValue.setValueType(ValueType.AMOUNT.value);
								break;
							}
						}); 
					}
					
					detailedDate.actualValue.add(new ActualValue(attendanceId, itemValue.getValue(), itemValue.getValueType()));
				}
				else {
					detailedDate.actualValue.add(new ActualValue(item.getAttendanceDisplay(), "", ActualValue.STRING));
				}
				workplaceData.setHasData(true);
			});
		});
		return employeeData;
	}
	
	/**
	 * Calculate total export by employee.
	 *
	 * @param workplaceData the workplace data
	 * @param lstAttendanceId the lst attendance id
	 */
	private void calculateTotalExportByEmployee(WorkplaceReportData workplaceData, List<AttendanceItemsDisplay> lstAttendanceId) {
		// Recursive
		workplaceData.getLstChildWorkplaceReportData().values().forEach(x -> {
			calculateTotalExportByEmployee(x, lstAttendanceId);
			workplaceData.setHasData(workplaceData.isHasData() || x.isHasData());
		});
		// Workplace
		workplaceData.workplaceTotal = new WorkplaceTotal();
		workplaceData.workplaceTotal.setWorkplaceId(workplaceData.getWorkplaceCode());
		List<TotalValue> lstTotalValue = new ArrayList<>();
		workplaceData.workplaceTotal.setTotalWorkplaceValue(lstTotalValue);
		
		// Calculate total of child workplaces
		final List<TotalValue> lstWorkplaceGrossTotal = lstAttendanceId.stream().map(item -> {
			TotalValue totalVal = new TotalValue();
			totalVal.setAttendanceId(item.getAttendanceDisplay());
			totalVal.setValue("0");
			totalVal.setValueType(TotalValue.STRING);
			return totalVal;
		}).collect(Collectors.toList());
		workplaceData.setGrossTotal(lstWorkplaceGrossTotal);
		workplaceData.getLstChildWorkplaceReportData().values().forEach(child -> {
			child.getGrossTotal().stream().forEach(val -> {
				if (val.value() == null) return;
				int attendanceId = val.getAttendanceId();
				TotalValue totalVal = lstWorkplaceGrossTotal.stream().filter(attendance -> attendance.getAttendanceId() == attendanceId).findFirst().get();
				ValueType valueTypeEnum = EnumAdaptor.valueOf(val.getValueType(), ValueType.class);
				if (valueTypeEnum.isIntegerCountable()) {
					int currentValue = (int) val.value();
					totalVal.setValue(String.valueOf(Integer.parseInt(totalVal.getValue()) + currentValue));
					totalVal.setValueType(val.getValueType());
				}
				if (valueTypeEnum.isDoubleCountable()) {
					double currentValueDouble = (double) val.value();
					totalVal.setValue(String.valueOf(Double.parseDouble(totalVal.getValue()) + currentValueDouble));
					totalVal.setValueType(val.getValueType());
				}
			});
		});
		
		// Calculate total of employees of current level (after calculating total of child workplace)
		List<EmployeeReportData> lstReportData = workplaceData.getLstEmployeeReportData();
		if (lstReportData != null && lstReportData.size() > 0) {
			workplaceData.getLstEmployeeReportData().stream().forEach(employeeData -> {
				// Put attendanceId into map
				lstAttendanceId.stream().forEach(attendanceId -> {
					int attendanceDisplay = attendanceId.getAttendanceDisplay();
					if (!employeeData.mapPersonalTotal.containsKey(attendanceDisplay)) {
						employeeData.mapPersonalTotal.put(attendanceDisplay, new TotalValue(attendanceDisplay, "0", TotalValue.STRING));
						TotalValue totalVal = new TotalValue();
						totalVal.setAttendanceId(attendanceDisplay);
						totalVal.setValue("0");
						totalVal.setValueType(TotalValue.STRING);
						Optional<TotalValue> optWorkplaceTotalValue = lstTotalValue.stream().filter(x -> x.getAttendanceId() == attendanceDisplay).findFirst();
						if (!optWorkplaceTotalValue.isPresent()) {
							lstTotalValue.add(totalVal);
						}
					}
				});
				
				// Add into total by attendanceId
				employeeData.getLstDetailedPerformance().stream().forEach(detail -> {
					detail.actualValue.stream().forEach((aVal) -> {
						int attdId = aVal.getAttendanceId();
						int valueType = aVal.getValueType();
						
						// Get all total
						ValueType valueTypeEnum = EnumAdaptor.valueOf(valueType, ValueType.class);
						TotalValue personalTotal = employeeData.mapPersonalTotal.get(attdId);
						TotalValue totalVal = lstTotalValue.stream().filter(attendance -> attendance.getAttendanceId() == attdId).findFirst().get();
						TotalValue totalGrossVal = lstWorkplaceGrossTotal.stream().filter(attendance -> attendance.getAttendanceId() == attdId).findFirst().get();
						
						// Change value type
						personalTotal.setValueType(valueType);
						totalVal.setValueType(valueType);
						totalGrossVal.setValueType(valueType);
						
						if (aVal.value() == null) return;
						
						if (valueTypeEnum.isIntegerCountable()) {
							int currentValue = (int) aVal.value();
							personalTotal.setValue(String.valueOf(Integer.parseInt(personalTotal.getValue()) + currentValue));
							employeeData.mapPersonalTotal.put(attdId, personalTotal);
							totalVal.setValue(String.valueOf(Integer.parseInt(totalVal.getValue()) + currentValue));
							totalGrossVal.setValue(String.valueOf(Integer.parseInt(totalGrossVal.getValue()) + currentValue));
							employeeData.mapPersonalTotal.put(attdId, personalTotal);
						}
						if (valueTypeEnum.isDoubleCountable()) {
							double currentValueDouble = (double) aVal.value();
							personalTotal.setValue(String.valueOf(Double.parseDouble(personalTotal.getValue()) + currentValueDouble));
							employeeData.mapPersonalTotal.put(attdId, personalTotal);
							totalVal.setValue(String.valueOf(Double.parseDouble(totalVal.getValue()) + currentValueDouble));
							totalGrossVal.setValue(String.valueOf(Double.parseDouble(totalGrossVal.getValue()) + currentValueDouble));
							employeeData.mapPersonalTotal.put(attdId, personalTotal);
						}
					});
				});
			});
		}
		
		// Calculate pure gross total of root workplace
		if (workplaceData.level == 0) {
			List<TotalValue> lstTotalVal = new ArrayList<>();
			workplaceData.setGrossTotal(lstTotalVal);
			
			// Sum of all workplace total and workplace hierarchy total
			workplaceData.lstChildWorkplaceReportData.forEach((k,child) -> {
				child.getGrossTotal().forEach(item -> {
					if (item.value() == null) return;
					Optional<TotalValue> optTotalVal = lstTotalVal.stream().filter(x -> x.getAttendanceId() == item.getAttendanceId()).findFirst();
					if (optTotalVal.isPresent()) {
						TotalValue totalVal = optTotalVal.get();
						int valueType = item.getValueType();
						totalVal.setValueType(valueType);
						ValueType valueTypeEnum = EnumAdaptor.valueOf(valueType, ValueType.class);
						if (valueTypeEnum.isIntegerCountable()) {
							totalVal.setValue(String.valueOf((int) totalVal.value() + Integer.parseInt(item.getValue())));
						}
						if (valueTypeEnum.isDoubleCountable()) {
							totalVal.setValue(String.valueOf((double) totalVal.value() + Double.parseDouble(item.getValue())));
						}
					}
					else {
						TotalValue totalVal = new TotalValue();
						totalVal.setAttendanceId(item.getAttendanceId());
						totalVal.setValue(item.getValue());
						totalVal.setValueType(item.getValueType());
						lstTotalVal.add(totalVal);
					}
				});
			});
		}
	}
	
	/**
	 * Calculate total export by date.
	 *
	 * @param workplaceData the workplace data
	 */
	private void calculateTotalExportByDate(DailyWorkplaceData workplaceData) {
		// Recursive
		workplaceData.getLstChildWorkplaceData().values().forEach(x -> {
			calculateTotalExportByDate(x);
			workplaceData.setHasData(workplaceData.isHasData() || x.isHasData());
		});
		// Workplace
		workplaceData.workplaceTotal = new WorkplaceTotal();
		workplaceData.workplaceTotal.setWorkplaceId(workplaceData.getWorkplaceCode());
		List<TotalValue> lstTotalValue = new ArrayList<>();
		workplaceData.workplaceTotal.setTotalWorkplaceValue(lstTotalValue);
		workplaceData.workplaceHierarchyTotal = new WorkplaceTotal();
		workplaceData.workplaceHierarchyTotal.setWorkplaceId(workplaceData.getWorkplaceCode());
		List<TotalValue> lstTotalHierarchyValue = new ArrayList<>();
		workplaceData.workplaceHierarchyTotal.setTotalWorkplaceValue(lstTotalHierarchyValue);
		
		List<DailyPersonalPerformanceData> performanceData = workplaceData.getLstDailyPersonalData();
		
		// Employee
		if (performanceData != null && performanceData.size() > 0) {
			for (DailyPersonalPerformanceData data: performanceData) {
				List<ActualValue> lstActualValue = data.getActualValue();
				if (lstActualValue == null) continue;
				lstActualValue.forEach(actualValue -> {
					int valueType = actualValue.getValueType();
					Optional<TotalValue> optTotalVal = lstTotalValue.stream().filter(x -> x.getAttendanceId() == actualValue.getAttendanceId()).findFirst();
					TotalValue totalValue;
					if (optTotalVal.isPresent()) {
						if (actualValue.value() == null) return;
						totalValue = optTotalVal.get();
						int totalValueType = totalValue.getValueType();
						ValueType valueTypeEnum = EnumAdaptor.valueOf(totalValueType, ValueType.class);
						if (valueTypeEnum.isIntegerCountable()) {
							totalValue.setValue(String.valueOf((int) totalValue.value() + (int) actualValue.value()));
						}
						if (valueTypeEnum.isDoubleCountable()) {
							totalValue.setValue(String.valueOf((double) totalValue.value() + (double) actualValue.value()));
						}
					}
					else {
						totalValue = new TotalValue();
						totalValue.setAttendanceId(actualValue.getAttendanceId());
						ValueType valueTypeEnum = EnumAdaptor.valueOf(actualValue.getValueType(), ValueType.class);
						if (valueTypeEnum.isIntegerCountable() || valueTypeEnum.isDoubleCountable()) {
							if (actualValue.getValue() != null) {
								totalValue.setValue(actualValue.getValue());
							} else {
								totalValue.setValue("0");
							}
						}
						else { // This case also deals with null value
							totalValue.setValue(actualValue.getValue());
						}
						totalValue.setValueType(valueType);
						lstTotalValue.add(totalValue);
					}
					
					Optional<TotalValue> optTotalWorkplaceVal = lstTotalHierarchyValue.stream().filter(x -> x.getAttendanceId() == actualValue.getAttendanceId()).findFirst();
					TotalValue totalWorkplaceValue;
					if (optTotalWorkplaceVal.isPresent()) {
						totalWorkplaceValue = optTotalWorkplaceVal.get();
						int totalValueType = totalValue.getValueType();
						ValueType valueTypeEnum = EnumAdaptor.valueOf(totalValueType, ValueType.class);
						if (valueTypeEnum.isIntegerCountable()) {
							totalValue.setValue(String.valueOf((int) totalWorkplaceValue.value() + (int) actualValue.value()));
						}
						if (valueTypeEnum.isDoubleCountable()) {
							totalValue.setValue(String.valueOf((double) totalWorkplaceValue.value() + (double) actualValue.value()));
						}
					}
					else {
						totalWorkplaceValue = new TotalValue();
						totalWorkplaceValue.setAttendanceId(actualValue.getAttendanceId());
						ValueType valueTypeEnum = EnumAdaptor.valueOf(actualValue.getValueType(), ValueType.class);
						if (valueTypeEnum.isIntegerCountable() || valueTypeEnum.isDoubleCountable()) {
							if (actualValue.getValue() != null) {
								totalWorkplaceValue.setValue(actualValue.getValue());
							} else {
								totalWorkplaceValue.setValue("0");
							}
						}
						else { // This case also deals with null value
							totalWorkplaceValue.setValue(actualValue.getValue());
						}
						totalWorkplaceValue.setValueType(valueType);
						lstTotalHierarchyValue.add(totalWorkplaceValue);
					}
				});
			}
		}
		
		// Workplace
		Map<String, DailyWorkplaceData> lstReportData = workplaceData.getLstChildWorkplaceData();
		lstReportData.values().forEach((value) -> {
			List<TotalValue> lstActualValue = value.getWorkplaceTotal().getTotalWorkplaceValue();
			lstActualValue.forEach(actualValue -> {
				int valueType = actualValue.getValueType();
				Optional<TotalValue> optTotalVal = lstTotalValue.stream().filter(x -> x.getAttendanceId() == actualValue.getAttendanceId()).findFirst();
				TotalValue totalValue;
				if (optTotalVal.isPresent()) {
					if (actualValue.value() == null) return;
					totalValue = optTotalVal.get();
					int totalValueType = totalValue.getValueType();
					ValueType valueTypeEnum = EnumAdaptor.valueOf(totalValueType, ValueType.class);
					if (valueTypeEnum.isIntegerCountable()) {
						totalValue.setValue(String.valueOf((int) totalValue.value() + (int) actualValue.value()));
					}
					if (valueTypeEnum.isDoubleCountable()) {
						totalValue.setValue(String.valueOf((double) totalValue.value() + (double) actualValue.value()));
					}
				}
				else {
					totalValue = new TotalValue();
					totalValue.setAttendanceId(actualValue.getAttendanceId());
					ValueType valueTypeEnum = EnumAdaptor.valueOf(actualValue.getValueType(), ValueType.class);
					if (valueTypeEnum.isIntegerCountable() || valueTypeEnum.isDoubleCountable()) {
						if (actualValue.getValue() != null) {
							totalValue.setValue(actualValue.getValue());
						} else {
							totalValue.setValue("0");
						}
					}
					else { // This case also deals with null value
						totalValue.setValue(actualValue.getValue());
					}
					totalValue.setValueType(valueType);
					lstTotalValue.add(totalValue);
				}
			});
		});
	}
	
	/**
	 * Calculate gross total by date.
	 *
	 * @param dailyReportData the daily report data
	 */
	private void calculateGrossTotalByDate(DailyReportData dailyReportData) {
		List<WorkplaceDailyReportData> lstWorkplaceDailyReportData = dailyReportData.getLstDailyReportData();
		List<TotalValue> lstGrossTotal = new ArrayList<>();
		dailyReportData.setListTotalValue(lstGrossTotal);
		
		lstWorkplaceDailyReportData.stream().forEach(workplaceDaily -> {
			WorkplaceTotal workplaceTotal = workplaceDaily.getLstWorkplaceData().getWorkplaceTotal();
			List<TotalValue> lstTotalVal = workplaceTotal.getTotalWorkplaceValue();
			lstTotalVal.stream().forEach(totalVal -> {
				// Literally 0-1 result in list
				Optional<TotalValue> optGrossTotal = lstGrossTotal.stream().filter(x -> x.getAttendanceId() == totalVal.getAttendanceId()).findFirst();
				TotalValue totalValue;
				if (optGrossTotal.isPresent()) {
					if (totalVal.value() == null) return;
					totalValue = optGrossTotal.get();
					int totalValueType = totalValue.getValueType();
					ValueType valueTypeEnum = EnumAdaptor.valueOf(totalValueType, ValueType.class);
					if (valueTypeEnum.isIntegerCountable()) {
						totalValue.setValue(String.valueOf((int) totalValue.value() + (int) totalVal.value()));
					}
					if (valueTypeEnum.isDoubleCountable()) {
						totalValue.setValue(String.valueOf((double) totalValue.value() + (double) totalVal.value()));
					}
				}
				else {
					totalValue = new TotalValue();
					totalValue.setAttendanceId(totalVal.getAttendanceId());
					ValueType valueTypeEnum = EnumAdaptor.valueOf(totalVal.getValueType(), ValueType.class);
					if (valueTypeEnum.isIntegerCountable() || valueTypeEnum.isDoubleCountable()) {
						totalValue.setValue(totalVal.getValue());
					}
					totalValue.setValueType(totalVal.getValueType());
					lstGrossTotal.add(totalValue);
				}
			});
		});
	}
	
	/**
	 * Find workplace.
	 *
	 * @param employeeId the employee id
	 * @param rootWorkplace the root workplace
	 * @param baseDate the base date
	 * @param lstWorkplaceHistImport the lst workplace hist import
	 * @param lstWorkplaceConfigInfo the lst workplace config info
	 * @return the workplace report data
	 */
	private WorkplaceReportData findWorkplace(String employeeId, WorkplaceReportData rootWorkplace, GeneralDate baseDate,
			List<WkpHistImport> lstWorkplaceHistImport, List<WorkplaceConfigInfo> lstWorkplaceConfigInfo) {
		Map<String, WorkplaceReportData> mapWorkplaceInfo = rootWorkplace.getLstChildWorkplaceReportData();
		WkpHistImport workplaceImport = lstWorkplaceHistImport.stream().filter(hist -> StringUtils.equalsIgnoreCase(hist.getEmployeeId(), employeeId) ).findFirst().get();
		//WkpInfo workplaceInfo = workplaceImport.getLstWkpInfo().stream().filter(x -> baseDate.compareTo(x.getDatePeriod().start()) >= 0 && baseDate.compareTo(x.getDatePeriod().end()) <= 0).findFirst().get();
		WorkplaceHierarchy code = lstWorkplaceConfigInfo.stream().filter(x -> StringUtils.equalsIgnoreCase(x.getLstWkpHierarchy().get(0).getWorkplaceId(), workplaceImport.getWorkplaceId())).findFirst().get().getLstWkpHierarchy().get(0);
		HierarchyCode hierarchyCode = code.getHierarchyCode();
		if (mapWorkplaceInfo.containsKey(hierarchyCode.v())) {
			WorkplaceReportData reportData = mapWorkplaceInfo.get(hierarchyCode.v());
			//reportData.period = workplaceInfo.getDatePeriod();
			return reportData;
		}
		else {
			for (Map.Entry<String, WorkplaceReportData> keySet : mapWorkplaceInfo.entrySet()) {
				WorkplaceReportData data = findWorkplace(employeeId, keySet.getValue(), baseDate, lstWorkplaceHistImport, lstWorkplaceConfigInfo);
				if (data != null)
					return data;
			}
		}
		return null;
	}
	
	/**
	 * Find workplace.
	 *
	 * @param employeeId the employee id
	 * @param rootWorkplace the root workplace
	 * @param baseDate the base date
	 * @param lstWkpHistImport the lst wkp hist import
	 * @param lstWorkplaceConfigInfo the lst workplace config info
	 * @return the daily workplace data
	 */
	private DailyWorkplaceData findWorkplace(String employeeId, DailyWorkplaceData rootWorkplace, GeneralDate baseDate, 
			List<WkpHistImport> lstWkpHistImport, List<WorkplaceConfigInfo> lstWorkplaceConfigInfo) {
		
		Map<String, DailyWorkplaceData> mapWorkplaceInfo = rootWorkplace.getLstChildWorkplaceData();
		WkpHistImport workplaceImport = lstWkpHistImport.stream().filter(hist -> StringUtils.equalsIgnoreCase(hist.getEmployeeId(), employeeId) ).findFirst().get();
		//WkpInfo workplaceInfo = workplaceImport.getLstWkpInfo().stream().filter(x -> baseDate.compareTo(x.getDatePeriod().start()) >= 0 && baseDate.compareTo(x.getDatePeriod().end()) <= 0).findFirst().get();
		WorkplaceHierarchy code = lstWorkplaceConfigInfo.stream().filter(x -> StringUtils.equalsIgnoreCase(x.getLstWkpHierarchy().get(0).getWorkplaceId(), workplaceImport.getWorkplaceId())).findFirst().get().getLstWkpHierarchy().get(0);
		HierarchyCode hierarchyCode = code.getHierarchyCode();
		if (mapWorkplaceInfo.containsKey(hierarchyCode.v())) {
			DailyWorkplaceData workplaceData = mapWorkplaceInfo.get(hierarchyCode.v());
			//workplaceData.period = workplaceInfo.getDatePeriod();
			return workplaceData;
		}
		else {
			for (Map.Entry<String, DailyWorkplaceData> keySet : mapWorkplaceInfo.entrySet()) {
				DailyWorkplaceData data = findWorkplace(employeeId, keySet.getValue(), baseDate, lstWkpHistImport, lstWorkplaceConfigInfo);
				if (data != null)
					return data;
			}
		}
		return null;
	}
	
	/**
	 * Collect workplace hierarchy.
	 *
	 * @param workplaceId the workplace id
	 * @param baseDate the base date
	 * @param lstWorkplaceConfigInfo the lst workplace config info
	 * @return the map
	 */
	private Map<String, WorkplaceInfo> collectWorkplaceHierarchy(String workplaceId, GeneralDate baseDate, List<WorkplaceConfigInfo> lstWorkplaceConfigInfo) {
		Map<String, WorkplaceInfo> lstWorkplace = new TreeMap<>();
		Optional<WorkplaceConfigInfo> optHierachyInfo = lstWorkplaceConfigInfo.stream().filter(x -> StringUtils.equalsIgnoreCase(x.getLstWkpHierarchy().get(0).getWorkplaceId(), workplaceId)).findFirst();
		optHierachyInfo.ifPresent(info -> {
			List<WorkplaceHierarchy> lstHierarchy = info.getLstWkpHierarchy();
			for (WorkplaceHierarchy hierarchy: lstHierarchy) {
				WorkplaceInfo workplace = workplaceInfoRepository.findByWkpId(hierarchy.getWorkplaceId(), baseDate).get();
				lstWorkplace.put(hierarchy.getHierarchyCode().v(), workplace);
				
				// Recursive
				if (!StringUtils.equals(workplace.getWorkplaceId(), workplaceId)) {
					collectWorkplaceHierarchy(workplace.getWorkplaceId(), baseDate, lstWorkplaceConfigInfo);
				}
			}
		});
		return lstWorkplace;
	}
	
	/**
	 * Analyze info export by employee.
	 *
	 * @param lstWorkplace the lst workplace
	 * @param parent the parent
	 */
	private void analyzeInfoExportByEmployee(Map<String, WorkplaceInfo> lstWorkplace, WorkplaceReportData parent) {
		boolean isFoundParent;
		// Add workplace into respective parent
		for (Map.Entry<String, WorkplaceInfo> entry : lstWorkplace.entrySet()) {
			String k = entry.getKey();
			WorkplaceInfo wpi = entry.getValue();
			
			isFoundParent = false;
			
			// Init an workplace report data
			WorkplaceReportData wrp = new WorkplaceReportData();
			wrp.workplaceCode = wpi.getWorkplaceCode().v();
			wrp.workplaceName = wpi.getWorkplaceName().v();
			wrp.lstEmployeeReportData = new ArrayList<>();
			wrp.level = k.length() / 3;
			
			// Break down code
			String breakDownCode;
			for (int level = k.length(); level >= 3; level -= 3) {
				breakDownCode = k.substring(0, level);
				
				// Find workplace based on breakdown code
				WorkplaceReportData targetWorkplace = findWorkplaceByHierarchyCodeExportEmployee(parent, breakDownCode);
				
				// Find parent workplace, add into its child
				if (targetWorkplace != null) {
					wrp.parent = targetWorkplace;
					targetWorkplace.lstChildWorkplaceReportData.put(k, wrp);
					//targetWorkplace.lstChildWorkplaceReportData = sortByWorkplaceCodeExportByEmployee(targetWorkplace.lstChildWorkplaceReportData);
					isFoundParent = true;
					break;
				}
			}

			// Not found, then add to root
			if (!isFoundParent) {
				wrp.parent = parent;
				parent.lstChildWorkplaceReportData.put(k, wrp);
				//parent.lstChildWorkplaceReportData = sortByWorkplaceCodeExportByEmployee(parent.lstChildWorkplaceReportData);
			}
		}
	}
	
	/**
	 * Find workplace by hierarchy code export employee.
	 *
	 * @param rootWorkplace the root workplace
	 * @param hierarchyCode the hierarchy code
	 * @return the workplace report data
	 */
	private WorkplaceReportData findWorkplaceByHierarchyCodeExportEmployee(WorkplaceReportData rootWorkplace, String hierarchyCode) {
		WorkplaceReportData workplaceReportData = null;
		
		for (Map.Entry<String, WorkplaceReportData> entry : rootWorkplace.lstChildWorkplaceReportData.entrySet()) {
			if (workplaceReportData != null) {
				break;
			}
			String key = entry.getKey();
			WorkplaceReportData childWorkplace = entry.getValue();
			if (StringUtils.equalsIgnoreCase(key, hierarchyCode)) {
				workplaceReportData = childWorkplace;
				break;
			}
			workplaceReportData = findWorkplaceByHierarchyCodeExportEmployee(childWorkplace, hierarchyCode);
		}
		
		return workplaceReportData;
	}
	
	
	/**
	 * Analyze info export by date.
	 *
	 * @param lstWorkplace the lst workplace
	 * @param parent the parent
	 * @param lstAddedWorkplace the lst added workplace
	 */
	private void analyzeInfoExportByDate(Map<String, WorkplaceInfo> lstWorkplace, DailyWorkplaceData parent, List<String> lstAddedWorkplace) {
		parent.lstChildWorkplaceData = new TreeMap<>();
		
		boolean isFoundParent;
		// Add workplace into respective parent
		for (Map.Entry<String, WorkplaceInfo> entry : lstWorkplace.entrySet()) {
			String k = entry.getKey();
			WorkplaceInfo wpi = entry.getValue();
			
			isFoundParent = false;
			
			// Init an workplace report data
			DailyWorkplaceData wrp = new DailyWorkplaceData();
			wrp.workplaceCode = wpi.getWorkplaceCode().v();
			wrp.workplaceName = wpi.getWorkplaceName().v();
			wrp.level = k.length() / 3;
			
			// Break down code
			String breakDownCode;
			for (int level = k.length(); level >= 3; level -= 3) {
				breakDownCode = k.substring(0, level);
				
				// Find workplace based on breakdown code
				DailyWorkplaceData targetWorkplace = findWorkplaceByHierarchyCodeExportDate(parent, breakDownCode);
				
				// Find parent workplace, add into its child
				if (targetWorkplace != null) {
					wrp.parent = targetWorkplace;
					targetWorkplace.lstChildWorkplaceData.put(k, wrp);
					//sortByWorkplaceCodeExportByDate(targetWorkplace.lstChildWorkplaceData);
					isFoundParent = true;
					break;
				}
			}

			// Not found, then add to root
			if (!isFoundParent) {
				wrp.parent = parent;
				parent.lstChildWorkplaceData.put(k, wrp);
				//sortByWorkplaceCodeExportByDate(parent.lstChildWorkplaceData);
			}
		}
	}
	
	/**
	 * Find workplace by hierarchy code export date.
	 *
	 * @param rootWorkplace the root workplace
	 * @param hierarchyCode the hierarchy code
	 * @return the daily workplace data
	 */
	private DailyWorkplaceData findWorkplaceByHierarchyCodeExportDate(DailyWorkplaceData rootWorkplace, String hierarchyCode) {
		DailyWorkplaceData workplaceReportData = null;
		
		for (Map.Entry<String, DailyWorkplaceData> entry : rootWorkplace.lstChildWorkplaceData.entrySet()) {
			if (workplaceReportData != null) {
				break;
			}
			String key = entry.getKey();
			DailyWorkplaceData childWorkplace = entry.getValue();
			if (StringUtils.equalsIgnoreCase(key, hierarchyCode)) {
				workplaceReportData = childWorkplace;
				break;
			}
			workplaceReportData = findWorkplaceByHierarchyCodeExportDate(childWorkplace, hierarchyCode);
		}
		
		return workplaceReportData;
	}
	
	/**
	 * Collect header data.
	 *
	 * @param headerData the header data
	 * @param condition the condition
	 */
	private void collectHeaderData(DailyPerformanceHeaderData headerData, WorkScheduleOutputCondition condition) {
		Optional<Company> optCompany = companyRepository.find(AppContexts.user().companyId());
		if (optCompany.isPresent())
			headerData.companyName = optCompany.get().getCompanyName().v();
		else
			headerData.companyName = "";
		headerData.setFixedHeaderData(new ArrayList<>());
		headerData.fixedHeaderData.add(WorkScheOutputConstants.ERAL);
		if (condition.getOutputType() == FormOutputType.BY_EMPLOYEE) {
			headerData.fixedHeaderData.add(WorkScheOutputConstants.DATE);
			headerData.fixedHeaderData.add(WorkScheOutputConstants.DAYMONTH);
			headerData.fixedHeaderData.add(WorkScheOutputConstants.DAY);
		} else {
			headerData.fixedHeaderData.add(WorkScheOutputConstants.PERSONAL_NAME);
		}
		headerData.fixedHeaderData.add(WorkScheOutputConstants.REMARK);
	}
	
	/**
	 * Collect display map.
	 *
	 * @param headerData the header data
	 * @param condition the condition
	 * @param companyId the company id
	 * @param roleId the role id
	 */
	private List<Integer> collectDisplayMap(DailyPerformanceHeaderData headerData,
			OutputItemDailyWorkSchedule condition, String companyId, String roleId) {
		List<Integer> lstHaveNameIds = new ArrayList<>();
		List<AttendanceItemsDisplay> lstItem = condition.getLstDisplayedAttendance();
		headerData.setLstOutputItemSettingCode(new ArrayList<>());

		List<Integer> lstAttendanceId = lstItem.stream().sorted((o1, o2) -> (o1.getOrderNo() - o2.getOrderNo()))
				.map(x -> x.getAttendanceDisplay()).collect(Collectors.toList());
		List<AttItemName> lstDailyAttendanceItem = companyDailyItemService.getDailyItems(companyId, Optional.of(roleId),
				lstAttendanceId, Arrays.asList(DailyAttendanceAtr.values()));
		if (lstDailyAttendanceItem.isEmpty())
			throw new BusinessException(new RawErrorMessage("Msg_1417"));
		//condition.setLstDisplayedAttendance(lstItem.stream().filter(x -> lstAttendanceId.contains(x.getAttendanceDisplay())).collect(Collectors.toList()));
		
		lstAttendanceId.stream().forEach(x -> {
			Optional<AttItemName> opAttendanceItem = lstDailyAttendanceItem.stream()
					.filter(item -> item.getAttendanceItemId() == x).findFirst();
			OutputItemSetting setting = new OutputItemSetting();
			if (opAttendanceItem.isPresent()) {
				setting.setItemCode(opAttendanceItem.get().getAttendanceItemDisplayNumber());
				setting.setItemName(opAttendanceItem.get().getAttendanceItemName());
				lstHaveNameIds.add(x);
				headerData.lstOutputItemSettingCode.add(setting);
			} 
//			else {
//				setting.setItemCode(null);
//				setting.setItemName("");
//			}
//			headerData.lstOutputItemSettingCode.add(setting);
		});
		return lstHaveNameIds;
	}
	
	/**
	 * Write header data.
	 *
	 * @param query the query
	 * @param outputItem the output item
	 * @param sheet the sheet
	 * @param reportData the report data
	 * @param dateRow the date row
	 */
	private void writeHeaderData(WorkScheduleOutputQuery query, OutputItemDailyWorkSchedule outputItem, Worksheet sheet, DailyPerformanceReportData reportData, int dateRow) {
		// Company name
		PageSetup pageSetup = sheet.getPageSetup();
		pageSetup.setHeader(0, "&8&\"MS ゴシック\"" + reportData.getHeaderData().companyName);
		
		// Output item name
		pageSetup.setHeader(1, "&16&\"MS ゴシック\"" + outputItem.getItemName().v());
		
		// Set header date
		DateTimeFormatter fullDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/M/d  H:mm", Locale.JAPAN);
		pageSetup.setHeader(2, "&8&\"MS ゴシック\" " + LocalDateTime.now().format(fullDateTimeFormatter) + "\npage &P ");
		
		Cells cells = sheet.getCells();
		Cell periodCell = cells.get(dateRow,0);
		
		DateTimeFormatter jpFormatter = DateTimeFormatter.ofPattern("yyyy/M/d (E)", Locale.JAPAN);
		String periodStr = WorkScheOutputConstants.PERIOD + " " + query.getStartDate().toLocalDate().format(jpFormatter) + " " +WorkScheOutputConstants.PERIOD_SYMBOL + " " + query.getEndDate().toLocalDate().format(jpFormatter);
		periodCell.setValue(periodStr);
	}
	
	/**
	 * Sets the fixed data.
	 *
	 * @param condition the condition
	 * @param sheet the sheet
	 * @param reportData the report data
	 * @param currentRow the current row
	 */
	private void setFixedData(WorkScheduleOutputCondition condition, Worksheet sheet, DailyPerformanceReportData reportData, int currentRow) {
		DailyPerformanceHeaderData headerData = reportData.getHeaderData();
		// Set fixed value
		Cells cells = sheet.getCells();
		
		if (condition.getOutputType() == FormOutputType.BY_EMPLOYEE) {
			// A2_1
			Cell erAlCell = cells.get(currentRow, 0);
			erAlCell.setValue(headerData.getFixedHeaderData().get(0));
			
			Cell dateCell = cells.get(currentRow, 1);
			// A2_2
			dateCell.setValue(headerData.getFixedHeaderData().get(1));
			
//			// A2_3
//			Cell dayMonCell = cells.get(currentRow + 1, 1);
//			dayMonCell.setValue(headerData.getFixedHeaderData().get(2));
//			
//			// A2_4
//			Cell dayCell = cells.get(currentRow + 1, 2);
//			dayCell.setValue(headerData.getFixedHeaderData().get(3));
			
			// A2_6
			Cell remarkCell = cells.get(currentRow, 35);
			remarkCell.setValue(headerData.getFixedHeaderData().get(4));
		}
		else {
			// B2_1
			Cell erAlCell = cells.get(currentRow, 0);
			erAlCell.setValue(headerData.getFixedHeaderData().get(0));
			
			// B2_2
			Cell dateCell = cells.get(currentRow, 1);
			dateCell.setValue(headerData.getFixedHeaderData().get(1));
			
			// B2_4
			Cell remarkCell = cells.get(currentRow, 35);
			remarkCell.setValue(headerData.getFixedHeaderData().get(2));
		}
		
	}
	
	/**
	 * Write display map.
	 *
	 * @param cells the cells
	 * @param reportData the report data
	 * @param currentRow the current row
	 * @param nSize the n size
	 */
	private void writeDisplayMap(Cells cells, DailyPerformanceReportData reportData, int currentRow, int nSize) {
		List<OutputItemSetting> lstItem = reportData.getHeaderData().getLstOutputItemSettingCode();
		
		// Divide list into smaller lists (max 16 items)
		int numOfChunks = (int)Math.ceil((double)lstItem.size() / CHUNK_SIZE);

		int start, length;
		List<OutputItemSetting> lstItemRow;
		
        for(int i = 0; i < numOfChunks; i++) {
            start = i * CHUNK_SIZE; 
            length = Math.min(lstItem.size() - start, CHUNK_SIZE);

            lstItemRow = lstItem.subList(start, start + length);
            
            OutputItemSetting outputItem;
            
            for (int j = 0; j < length; j++) {
            	outputItem = lstItemRow.get(j);
            	// Column 4, 6, 8,...
            	// Row 3, 4, 5
            	Cell cell = cells.get(currentRow + i*2, DATA_COLUMN_INDEX[0] + j * 2); 
            	cell.setValue(outputItem.getItemName());
            	
            	cell = cells.get(currentRow + i*2 + 1, DATA_COLUMN_INDEX[0] + j * 2); 
//            	cell.setValue(outputItem.getItemCode());
            }
        }
	}
	
	/**
	 * Write detailed work schedule.
	 *
	 * @param currentRow the current row
	 * @param templateSheetCollection the template sheet collection
	 * @param sheet the sheet
	 * @param workplaceReportData the workplace report data
	 * @param dataRowCount the data row count
	 * @param condition the condition
	 * @param rowPageTracker the row page tracker
	 * @return the int
	 * @throws Exception the exception
	 */
	private int writeDetailedWorkSchedule(int currentRow, WorksheetCollection templateSheetCollection, Worksheet sheet, WorkplaceReportData workplaceReportData, 
			int dataRowCount, WorkScheduleOutputCondition condition, RowPageTracker rowPageTracker) throws Exception {
		Cells cells = sheet.getCells();
		
		WorkScheduleSettingTotalOutput totalOutput = condition.getSettingDetailTotalOutput();
		List<EmployeeReportData> lstEmployeeReportData = workplaceReportData.getLstEmployeeReportData();
		
		// Root workplace won't have employee
		if (lstEmployeeReportData != null && lstEmployeeReportData.size() > 0) {
			Iterator<EmployeeReportData> iteratorEmployee = lstEmployeeReportData.iterator();
			while (iteratorEmployee.hasNext()) {
				EmployeeReportData employeeReportData = iteratorEmployee.next();
				
				if (rowPageTracker.checkRemainingRowSufficient(dataRowCount) < 0 || (rowPageTracker.checkRemainingRowSufficient(dataRowCount) == 0 && dataRowCount == 1)) {
					sheet.getHorizontalPageBreaks().add(currentRow);
					rowPageTracker.resetRemainingRow();
				}
				Range workplaceRangeTemp = templateSheetCollection.getRangeByName(WorkScheOutputConstants.RANGE_WORKPLACE_ROW);
				Range workplaceRange = cells.createRange(currentRow, 0, 1, 39);
				workplaceRange.copy(workplaceRangeTemp);
				if (rowPageTracker.checkRemainingRowSufficient(dataRowCount) == 0) {
					rowPageTracker.useRemainingRow(dataRowCount);
					rowPageTracker.resetRemainingRow();
				}
				else {
					rowPageTracker.useRemainingRow(dataRowCount);
				}
				
				// A3_1
				Cell workplaceTagCell = cells.get(currentRow, 0);
				workplaceTagCell.setValue(WorkScheOutputConstants.WORKPLACE +"　"+ workplaceReportData.getWorkplaceCode() +"　"+ workplaceReportData.getWorkplaceName());
				
				// A3_2
//				Cell workplaceInfo = cells.get(currentRow, DATA_COLUMN_INDEX[0]);
//				workplaceInfo.setValue(workplaceReportData.getWorkplaceCode() + " " + workplaceReportData.getWorkplaceName());
				
				currentRow++;

				if (rowPageTracker.checkRemainingRowSufficient(dataRowCount) < 0 || (rowPageTracker.checkRemainingRowSufficient(dataRowCount) == 0 && dataRowCount == 1)) {
					sheet.getHorizontalPageBreaks().add(currentRow);
					rowPageTracker.resetRemainingRow();
				}
				Range employeeRangeTemp = templateSheetCollection.getRangeByName(WorkScheOutputConstants.RANGE_EMPLOYEE_ROW);
				Range employeeRange = cells.createRange(currentRow, 0, 1, 39);
				employeeRange.copy(employeeRangeTemp);
				if (rowPageTracker.checkRemainingRowSufficient(dataRowCount) == 0) {
					rowPageTracker.useRemainingRow(dataRowCount);
					rowPageTracker.resetRemainingRow();
				}
				else {
					rowPageTracker.useRemainingRow(dataRowCount);
				}
				
				// A4_1
				Cell employeeTagCell = cells.get(currentRow, 0);
				employeeTagCell.setValue(WorkScheOutputConstants.EMPLOYEE + "　" + employeeReportData.employeeCode + "　"
						+ employeeReportData.employeeName + "　" + WorkScheOutputConstants.EMPLOYMENT + "　"
						+ employeeReportData.employmentName + "　" + WorkScheOutputConstants.POSITION + "　"
						+ employeeReportData.position);

//				// A4_2
//				Cell employeeCell = cells.get(currentRow, 3);
//				employeeCell.setValue(employeeReportData.employeeCode + " " + employeeReportData.employeeName);
//				
//				// A4_3
//				Cell employmentTagCell = cells.get(currentRow, 9);
//				employmentTagCell.setValue(WorkScheOutputConstants.EMPLOYMENT);
//				
//				// A4_5
//				Cell employmentCell = cells.get(currentRow, 11);
//				employmentCell.setValue(employeeReportData.employmentName);
//				
//				// A4_6
//				Cell jobTitleTagCell = cells.get(currentRow, 15);
//				jobTitleTagCell.setValue(WorkScheOutputConstants.POSITION);
//
//				// A4_7
//				Cell jobTitleCell = cells.get(currentRow, 17);
//				jobTitleCell.setValue(employeeReportData.position);
				
				currentRow++;
				boolean colorWhite = true; // true = white, false = light blue, start with white row
				
				// Detail personal performance
				if (totalOutput.isDetails()) {
					List<DetailedDailyPerformanceReportData> lstDetailedDailyPerformance = employeeReportData.getLstDetailedPerformance();
					for (DetailedDailyPerformanceReportData detailedDailyPerformanceReportData: lstDetailedDailyPerformance) {
						Range dateRangeTemp;
						
						if (rowPageTracker.checkRemainingRowSufficient(dataRowCount) < 0 || (rowPageTracker.checkRemainingRowSufficient(dataRowCount) == 0 && dataRowCount == 1)) {
							sheet.getHorizontalPageBreaks().add(currentRow);
							rowPageTracker.resetRemainingRow();
						}
						if (colorWhite) // White row
							dateRangeTemp = templateSheetCollection.getRangeByName(WorkScheOutputConstants.RANGE_WHITE_ROW + dataRowCount);
						else // Light blue row
							dateRangeTemp = templateSheetCollection.getRangeByName(WorkScheOutputConstants.RANGE_LIGHTBLUE_ROW + dataRowCount);
						Range dateRange = cells.createRange(currentRow, 0, dataRowCount, 39);
						dateRange.copy(dateRangeTemp);
						dateRange.setOutlineBorder(BorderType.BOTTOM_BORDER, CellBorderType.THIN, Color.getBlack());
						if (rowPageTracker.checkRemainingRowSufficient(dataRowCount) == 0) {
							rowPageTracker.useRemainingRow(dataRowCount);
							rowPageTracker.resetRemainingRow();
						}
						else {
							rowPageTracker.useRemainingRow(dataRowCount);
						}
						
						// A5_1
						Cell erAlMark = cells.get(currentRow, 0);
						erAlMark.setValue(detailedDailyPerformanceReportData.getErrorAlarmMark());
						
						// A5_2
						Cell dateCell = cells.get(currentRow, 1);
						dateCell.setValue(detailedDailyPerformanceReportData.getDate().toString("MM/dd"));
						
						// A5_3
						DateTimeFormatter jpFormatter = DateTimeFormatter.ofPattern("E", Locale.JAPAN);
						String jp = detailedDailyPerformanceReportData.getDate().toLocalDate().format(jpFormatter);
						Cell dayOfWeekCell = cells.get(currentRow, 2);
						dayOfWeekCell.setValue(jp);
						
						// A5_4
						List<ActualValue> lstItem = detailedDailyPerformanceReportData.getActualValue();
						
						// Divide list into smaller lists (max 16 items)
						int numOfChunks = (int)Math.ceil((double)lstItem.size() / CHUNK_SIZE);
	
						int curRow = currentRow;
						int start, length;
						List<ActualValue> lstItemRow;
						
				        for(int i = 0; i < numOfChunks; i++) {
				            start = i * CHUNK_SIZE;
				            length = Math.min(lstItem.size() - start, CHUNK_SIZE);
	
				            lstItemRow = lstItem.subList(start, start + length);
				            
				            for (int j = 0; j < length; j++) {
				            	// Column 4, 6, 8,...
				            	ActualValue actualValue = lstItemRow.get(j);
				            	Cell cell = cells.get(curRow, DATA_COLUMN_INDEX[0] + j * 2); 
				            	writeDetailValue(actualValue, cell);
				            }
				            
				            curRow++;
				        }
				        
				        // A5_5
				        Cell remarkCell;
				        String errorDetail = detailedDailyPerformanceReportData.getErrorDetail();
				        if (errorDetail.length() >= 35 && dataRowCount == 1) {
				        	int numOfChunksRemark = (int)Math.ceil((double)errorDetail.length() / 35);
							int curRowRemark = currentRow;
							String remarkContentRow;
							
				        	for(int i = 0; i < numOfChunksRemark; i++) {
					            start = i * 35;
					            length = Math.min(errorDetail.length() - start, 35);
		
					            remarkContentRow = errorDetail.substring(start, start + length);
					            
					            for (int j = 0; j < length; j++) {
					            	// Column 4, 6, 8,...
					            	remarkCell = cells.get(curRowRemark, 35); 
					            	Style style = remarkCell.getStyle();
					            	remarkCell.setValue(remarkContentRow);
									style.setHorizontalAlignment(TextAlignmentType.LEFT);
					            	setFontStyle(style);
					            	remarkCell.setStyle(style);
					            }
					            
					            curRowRemark++;
					        }
				        }
				        else {
				        	remarkCell = cells.get(currentRow,35);
				        	remarkCell.setValue(errorDetail);
				        }
				        
				        currentRow += dataRowCount;
				        colorWhite = !colorWhite; // Change to other color
					}
				}
				
				// Personal total
				if (totalOutput.isPersonalTotal()) {
					if (rowPageTracker.checkRemainingRowSufficient(dataRowCount) < 0 || (rowPageTracker.checkRemainingRowSufficient(dataRowCount) == 0 && dataRowCount == 1)) {
						sheet.getHorizontalPageBreaks().add(currentRow);
						rowPageTracker.resetRemainingRow();
					}
					
					Range personalTotalRangeTemp = templateSheetCollection.getRangeByName(WorkScheOutputConstants.RANGE_TOTAL_ROW + dataRowCount);
					Range personalTotalRange = cells.createRange(currentRow, 0, dataRowCount, 39);
					personalTotalRange.copy(personalTotalRangeTemp);
					personalTotalRange.setOutlineBorder(BorderType.BOTTOM_BORDER, CellBorderType.THIN, Color.getBlack());
					if (rowPageTracker.checkRemainingRowSufficient(dataRowCount) == 0) {
						rowPageTracker.useRemainingRow(dataRowCount);
						rowPageTracker.resetRemainingRow();
					}
					else {
						rowPageTracker.useRemainingRow(dataRowCount);
					}
					
					// A6_1
					Cell personalTotalCellTag = cells.get(currentRow, 0);
					personalTotalCellTag.setValue(WorkScheOutputConstants.PERSONAL_TOTAL);
					
					// A6_2
					Map<Integer, TotalValue> mapPersonalTotal = employeeReportData.getMapPersonalTotal();
					int numOfChunks = (int) Math.ceil( (double) mapPersonalTotal.size() / CHUNK_SIZE);
					int start, length;
					List<TotalValue> lstItemRow;
					
			        for(int i = 0; i < numOfChunks; i++) {
			            start = i * CHUNK_SIZE;
			            length = Math.min(mapPersonalTotal.size() - start, CHUNK_SIZE);
			            
			            lstItemRow = mapPersonalTotal.values().stream().collect(Collectors.toList()).subList(start, start + length);
			            
			            for (int j = 0; j < length; j++) {
			            	TotalValue totalValue = lstItemRow.get(j);
			            	Cell cell = cells.get(currentRow, DATA_COLUMN_INDEX[0] + j * 2); 
			            	writeTotalValue(totalValue, cell);
			            }
			            currentRow++;
			        }
				}
		        
				// Total count day
		        if (condition.getSettingDetailTotalOutput().isTotalNumberDay()) {
		        	// Use fixed 2 rows
		        	if (rowPageTracker.checkRemainingRowSufficient(2) < 0) {
						sheet.getHorizontalPageBreaks().add(currentRow);
						rowPageTracker.resetRemainingRow();
					}
			        
			        Range dayCountRangeTemp = templateSheetCollection.getRangeByName(WorkScheOutputConstants.RANGE_DAYCOUNT_ROW);
					Range dayCountRange = cells.createRange(currentRow, 0, 2, 39);
					dayCountRange.copy(dayCountRangeTemp);
					dayCountRange.setOutlineBorder(BorderType.BOTTOM_BORDER, CellBorderType.THIN, Color.getBlack());
					if (rowPageTracker.checkRemainingRowSufficient(2) == 0) {
						rowPageTracker.useRemainingRow(2);
						rowPageTracker.resetRemainingRow();
					}
					else {
						rowPageTracker.useRemainingRow(2);
					}
					
					// A7_1
					Cell dayCountTag = cells.get(currentRow, 0);
					dayCountTag.setValue(WorkScheOutputConstants.DAY_COUNT);
					
					// A7_2 -> A7_10
					for (int i = 0; i < WorkScheOutputConstants.DAY_COUNT_TITLES.length; i++) {
						Cell dayTypeTag = cells.get(currentRow, i*2 + 3);
						dayTypeTag.setValue(WorkScheOutputConstants.DAY_COUNT_TITLES[i]);
					}
					
					currentRow++;
				
					// A7_11 -> A7_19
					TotalCountDay totalCountDay = employeeReportData.getTotalCountDay();
					totalCountDay.initAllDayCount();
					
					for (int i = 0; i < WorkScheOutputConstants.DAY_COUNT_TITLES.length; i++) {
						Cell dayTypeTag = cells.get(currentRow, i*2 + 3);
						dayTypeTag.setValue(totalCountDay.getAllDayCount().get(i));
					}
					
					currentRow++;
		        }
		        
		        // Page break by employee
				if (condition.getPageBreakIndicator() == PageBreakIndicator.EMPLOYEE && iteratorEmployee.hasNext()) {
					rowPageTracker.resetRemainingRow();
					sheet.getHorizontalPageBreaks().add(currentRow);
				}
			}
		}
		
		// Skip writing total for root, use gross total instead
		if (lstEmployeeReportData != null && lstEmployeeReportData.size() > 0 && totalOutput.isWorkplaceTotal()) {
			if (rowPageTracker.checkRemainingRowSufficient(dataRowCount) < 0 || (rowPageTracker.checkRemainingRowSufficient(dataRowCount) == 0 && dataRowCount == 1)) {
				rowPageTracker.resetRemainingRow();
				sheet.getHorizontalPageBreaks().add(currentRow);
			}
			
			Range workplaceTotalRangeTemp = templateSheetCollection.getRangeByName(WorkScheOutputConstants.RANGE_TOTAL_ROW + dataRowCount);
			Range workplaceTotalRange = cells.createRange(currentRow, 0, dataRowCount, 39);
			if (rowPageTracker.checkRemainingRowSufficient(dataRowCount) == 0) {
				rowPageTracker.useRemainingRow(dataRowCount);
				rowPageTracker.resetRemainingRow();
			}
			else {
				rowPageTracker.useRemainingRow(dataRowCount);
			}
			
			workplaceTotalRange.copy(workplaceTotalRangeTemp);
			workplaceTotalRange.setOutlineBorder(BorderType.BOTTOM_BORDER, CellBorderType.THIN, Color.getBlack());
			
			// A8_1
			Cell workplaceTotalCellTag = cells.get(currentRow, 0);
			workplaceTotalCellTag.setValue(WorkScheOutputConstants.WORKPLACE_TOTAL);
			
			// A8_2
			WorkplaceTotal workplaceTotal = workplaceReportData.getWorkplaceTotal();
			int numOfChunks = (int) Math.ceil( (double) workplaceTotal.getTotalWorkplaceValue().size() / CHUNK_SIZE);
			int start, length;
			List<TotalValue> lstItemRow;
			
	        for(int i = 0; i < numOfChunks; i++) {
	            start = i * CHUNK_SIZE;
	            length = Math.min(workplaceTotal.getTotalWorkplaceValue().size() - start, CHUNK_SIZE);
	
	            lstItemRow = workplaceTotal.getTotalWorkplaceValue().subList(start, start + length);
	            
	            for (int j = 0; j < length; j++) {
	            	TotalValue totalValue = lstItemRow.get(j);
	            	Cell cell = cells.get(currentRow, DATA_COLUMN_INDEX[0] + j * 2); 
	            	writeTotalValue(totalValue, cell);
	            }
	            currentRow++;
	        }
		}
		
		Map<String, WorkplaceReportData> mapChildWorkplace = workplaceReportData.getLstChildWorkplaceReportData();
		int childHasDataCount = (int)mapChildWorkplace.values().stream().map(child -> child.isHasData()).filter(child -> child).count();
		if (((condition.getPageBreakIndicator() == PageBreakIndicator.WORKPLACE || 
				condition.getPageBreakIndicator() == PageBreakIndicator.EMPLOYEE) &&
				mapChildWorkplace.size() > 0 ) && workplaceReportData.level != 0 && childHasDataCount > 0) { /* Trick to not page break between detailed data and total data */
			Range lastRowRange = cells.createRange(currentRow - 1, 0, 1, 39);
        	lastRowRange.setOutlineBorder(BorderType.BOTTOM_BORDER, CellBorderType.THIN, Color.getBlack());
        	rowPageTracker.resetRemainingRow();
			sheet.getHorizontalPageBreaks().add(currentRow);
		}
		// Process to child workplace9
		Iterator<Map.Entry<String, WorkplaceReportData>> it = mapChildWorkplace.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, WorkplaceReportData> entry = it.next();
			WorkplaceReportData childWorkplaceReportData = entry.getValue();
			currentRow = writeDetailedWorkSchedule(currentRow, templateSheetCollection, sheet, childWorkplaceReportData, dataRowCount, condition, rowPageTracker);
			
			// Page break by workplace
			if ((condition.getPageBreakIndicator() == PageBreakIndicator.WORKPLACE || condition.getPageBreakIndicator() == PageBreakIndicator.EMPLOYEE)
					&& it.hasNext() && childWorkplaceReportData.isHasData()) {
				rowPageTracker.resetRemainingRow();
				sheet.getHorizontalPageBreaks().add(currentRow);
			}
		}
		
		/**
		 * A9 - A13
		 */
		TotalWorkplaceHierachy totalHierarchyOption = totalOutput.getWorkplaceHierarchyTotal();
		if ((totalOutput.isGrossTotal() || totalOutput.isCumulativeWorkplace())
				&& !(workplaceReportData.getLstChildWorkplaceReportData().isEmpty()
				&& workplaceReportData.getLstEmployeeReportData().isEmpty())) {
			
			int level = workplaceReportData.getLevel();
			
			if (findWorkplaceHigherEnabledLevel(workplaceReportData, totalHierarchyOption) <= level && workplaceReportData.isHasData()) {
				String tagStr;
				
				WorkplaceReportData parentWorkplace = workplaceReportData.getParent();
				
				List<Integer> lstBetweenLevel = findEnabledLevelBetweenWorkplaces(workplaceReportData, totalHierarchyOption);
				Iterator<Integer> levelIterator = null;
				if (!lstBetweenLevel.isEmpty()) {
					levelIterator = lstBetweenLevel.iterator();
				}
				
				do {
					//if (level != 0 && level >= totalHierarchyOption.getHighestLevelEnabled() && totalOutput.isCumulativeWorkplace() && totalHierarchyOption.checkLevelEnabled(level)) {
					if (level != 0 && level >= totalHierarchyOption.getHighestLevelEnabled() && totalOutput.isCumulativeWorkplace()) {
						// Condition: not root workplace (lvl = 0), level within picked hierarchy zone, enable cumulative workplace.
						int parentHigherEnabledLevel = findWorkplaceHigherEnabledLevel(parentWorkplace, totalHierarchyOption);
						if (parentHigherEnabledLevel <= level) {
							if (levelIterator != null && levelIterator.hasNext()) {
								tagStr = WorkScheOutputConstants.WORKPLACE_HIERARCHY_TOTAL + levelIterator.next();
							}
							else {
								tagStr = null;
							}
						}
						else if (totalHierarchyOption.checkLevelEnabled(level)) {
							tagStr = WorkScheOutputConstants.WORKPLACE_HIERARCHY_TOTAL + workplaceReportData.getLevel();
						}
						else {
							tagStr = null;
						}
					} else if (totalOutput.isGrossTotal() && level == 0)
						// Condition: enable gross total, also implicitly root workplace
						tagStr = WorkScheOutputConstants.GROSS_TOTAL;
					else
						tagStr = null;
					
					if (tagStr != null) {
						if (rowPageTracker.checkRemainingRowSufficient(dataRowCount) < 0 || (rowPageTracker.checkRemainingRowSufficient(dataRowCount) == 0 && dataRowCount == 1)) {
							sheet.getHorizontalPageBreaks().add(currentRow);
							rowPageTracker.resetRemainingRow();
						}
						
						Range wkpHierTotalRangeTemp = templateSheetCollection.getRangeByName(WorkScheOutputConstants.RANGE_TOTAL_ROW + dataRowCount);
						Range wkpHierTotalRange = cells.createRange(currentRow, 0, dataRowCount, 39);
						wkpHierTotalRange.copy(wkpHierTotalRangeTemp);
						wkpHierTotalRange.setOutlineBorder(BorderType.BOTTOM_BORDER, CellBorderType.THIN, Color.getBlack());
						if (rowPageTracker.checkRemainingRowSufficient(dataRowCount) == 0) {
							rowPageTracker.useRemainingRow(dataRowCount);
							rowPageTracker.resetRemainingRow();
						}
						else {
							rowPageTracker.useRemainingRow(dataRowCount);
						}
						
						// A9_1 - A13_1
						Cell workplaceTotalCellTag = cells.get(currentRow, 0);
						workplaceTotalCellTag.setValue(tagStr);
						
						// A9_2 - A13_2
						int numOfChunks = (int) Math.ceil( (double) workplaceReportData.getGrossTotal().size() / CHUNK_SIZE);
						int start, length;
						List<TotalValue> lstItemRow;
						
				        for(int i = 0; i < numOfChunks; i++) {
				            start = i * CHUNK_SIZE;
				            length = Math.min(workplaceReportData.getGrossTotal().size() - start, CHUNK_SIZE);
				
				            lstItemRow = workplaceReportData.getGrossTotal().subList(start, start + length);
				            
				            for (int j = 0; j < length; j++) {
				            	TotalValue totalValue = lstItemRow.get(j);
				            	Cell cell = cells.get(currentRow + i, DATA_COLUMN_INDEX[0] + j * 2); 
				            	writeTotalValue(totalValue, cell);
				            }
				        }
				        currentRow += dataRowCount;
					}
				} while (levelIterator != null && levelIterator.hasNext());
			}
		}
		
		return currentRow;
	}

	/**
	 * Write detail value.
	 *
	 * @param actualValue the actual value
	 * @param cell the cell
	 */
	private void writeDetailValue(ActualValue actualValue, Cell cell) {
		Style style = cell.getStyle();
		ValueType valueTypeEnum = EnumAdaptor.valueOf(actualValue.getValueType(), ValueType.class);
		if (valueTypeEnum.isTime()) {
			String value = actualValue.getValue();
			if (value != null) {
				if (valueTypeEnum == ValueType.TIME) {
					cell.setValue(getTimeAttr(value, false));
				} else {
					cell.setValue(getTimeAttr(value, true));
				}
			}
			style.setHorizontalAlignment(TextAlignmentType.RIGHT);
		} else if (valueTypeEnum.isDouble() || valueTypeEnum.isInteger()) {
			cell.setValue(actualValue.getValue());
			style.setHorizontalAlignment(TextAlignmentType.RIGHT);
		}
		else {
			cell.setValue(actualValue.getValue());
			style.setHorizontalAlignment(TextAlignmentType.LEFT);
		}
		setFontStyle(style);
		cell.setStyle(style);
	}
	
	/**
	 * Write detailed daily schedule.
	 *
	 * @param currentRow the current row
	 * @param templateSheetCollection the template sheet collection
	 * @param sheet the sheet
	 * @param dailyReport the daily report
	 * @param dataRowCount the data row count
	 * @param condition the condition
	 * @param rowPageTracker the row page tracker
	 * @return the int
	 * @throws Exception the exception
	 */
	private int writeDetailedDailySchedule(int currentRow, WorksheetCollection templateSheetCollection, Worksheet sheet, DailyReportData dailyReport,
			int dataRowCount, WorkScheduleOutputCondition condition, RowPageTracker rowPageTracker) throws Exception {
		Cells cells = sheet.getCells();
		List<WorkplaceDailyReportData> lstDailyReportData = dailyReport.getLstDailyReportData();
		
		Iterator<WorkplaceDailyReportData> iteratorWorkplaceData  = lstDailyReportData.iterator();
		
		while(iteratorWorkplaceData.hasNext()) {
			WorkplaceDailyReportData dailyReportData = iteratorWorkplaceData.next();
			Range dateRangeTemp = templateSheetCollection.getRangeByName(WorkScheOutputConstants.RANGE_DATE_ROW);
			Range dateRange = cells.createRange(currentRow, 0, 1, DATA_COLUMN_INDEX[5]);
			dateRange.copy(dateRangeTemp);
			rowPageTracker.useOneRowAndCheckResetRemainingRow(sheet, currentRow);
			//dateRange.setOutlineBorder(BorderType.TOP_BORDER, CellBorderType.THIN, Color.getBlack());
			
			DailyWorkplaceData rootWorkplace = dailyReportData.getLstWorkplaceData();
			if (!rootWorkplace.isHasData()) continue; // Skip to next day when none of workplace has data
			
			// B3_1
			Cell dateTagCell = cells.get(currentRow, 0);
			DateTimeFormatter jpFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd (E)", Locale.JAPAN);
			String date = dailyReportData.getDate().toLocalDate().format(jpFormatter);
			dateTagCell.setValue(WorkScheOutputConstants.DATE_BRACKET +"　"+ date);
			
//			// B3_2
//			Cell dateCell = cells.get(currentRow, 2);
//			dateCell.setValue(date);
			
			currentRow++;
			
			currentRow = writeDailyDetailedPerformanceDataOnWorkplace(currentRow, sheet, templateSheetCollection, rootWorkplace, dataRowCount, condition, rowPageTracker);
		
			if (iteratorWorkplaceData.hasNext()) {
				// Page break (regardless of setting, see example template sheet ★ 日別勤務表-日別3行-1)
				rowPageTracker.resetRemainingRow();
				sheet.getHorizontalPageBreaks().add(currentRow);
			}
		}
		
		if (condition.getSettingDetailTotalOutput().isGrossTotal()) {
			// Gross total after all the rest of the data
			if (rowPageTracker.checkRemainingRowSufficient(dataRowCount) < 0 || (rowPageTracker.checkRemainingRowSufficient(dataRowCount) == 0 && dataRowCount == 1)) {
				sheet.getHorizontalPageBreaks().add(currentRow);
				rowPageTracker.resetRemainingRow();
			}
			Range wkpGrossTotalRangeTemp = templateSheetCollection.getRangeByName(WorkScheOutputConstants.RANGE_TOTAL_ROW + dataRowCount);
			Range wkpGrossTotalRange = cells.createRange(currentRow, 0, dataRowCount, 39);
			wkpGrossTotalRange.copy(wkpGrossTotalRangeTemp);
			wkpGrossTotalRange.setOutlineBorder(BorderType.BOTTOM_BORDER, CellBorderType.THIN, Color.getBlack());
			if (rowPageTracker.checkRemainingRowSufficient(dataRowCount) == 0) {
				rowPageTracker.useRemainingRow(dataRowCount);
				rowPageTracker.resetRemainingRow();
			}
			else {
				rowPageTracker.useRemainingRow(dataRowCount);
			}
			
			Cell grossTotalCellTag = cells.get(currentRow, 0);
			grossTotalCellTag.setValue(WorkScheOutputConstants.GROSS_TOTAL);
			
			currentRow = writeGrossTotal(currentRow, dailyReport.getListTotalValue(), sheet, dataRowCount, rowPageTracker);
		}
		
		return currentRow;
	}
	
	/**
	 * Write daily detailed performance data on workplace.
	 *
	 * @param currentRow the current row
	 * @param sheet the sheet
	 * @param templateSheetCollection the template sheet collection
	 * @param rootWorkplace the root workplace
	 * @param dataRowCount the data row count
	 * @param condition the condition
	 * @param rowPageTracker the row page tracker
	 * @return the int
	 * @throws Exception the exception
	 */
	private int writeDailyDetailedPerformanceDataOnWorkplace(int currentRow, Worksheet sheet, WorksheetCollection templateSheetCollection, DailyWorkplaceData rootWorkplace, int dataRowCount, WorkScheduleOutputCondition condition, RowPageTracker rowPageTracker) throws Exception {
		Cells cells = sheet.getCells();
		Range workplaceRangeTemp = templateSheetCollection.getRangeByName(WorkScheOutputConstants.RANGE_DATE_ROW);
		Range workplaceRange = cells.createRange(currentRow, 0, 1, DATA_COLUMN_INDEX[5]);
		workplaceRange.copy(workplaceRangeTemp);
		
		boolean colorWhite = true; // true = white, false = light blue, start with white row
		
		List<DailyPersonalPerformanceData> employeeReportData = rootWorkplace.getLstDailyPersonalData();
		if (employeeReportData != null && !employeeReportData.isEmpty() && rootWorkplace.isHasData()) {
			rowPageTracker.useOneRowAndCheckResetRemainingRow(sheet, currentRow);
			// B4_1
			Cell workplaceTagCell = cells.get(currentRow, 0);
			workplaceTagCell.setValue(WorkScheOutputConstants.WORKPLACE +"　"+ rootWorkplace.getWorkplaceCode() +"　"+ rootWorkplace.getWorkplaceName());
			
//			// B4_2
//			Cell workplaceCell = cells.get(currentRow, 2);
//			workplaceCell.setValue(rootWorkplace.getWorkplaceCode() + " " + rootWorkplace.getWorkplaceName());
			
			currentRow++;
			
			Iterator<DailyPersonalPerformanceData> dataIterator = employeeReportData.iterator();
			
			// Employee data
			while (dataIterator.hasNext() && condition.getSettingDetailTotalOutput().isDetails()){
				DailyPersonalPerformanceData employee = dataIterator.next();
				
				List<ActualValue> lstItem = employee.getActualValue();
				// Skip to next employee when there is no actual value
				if (lstItem == null || lstItem.isEmpty()) continue;
				
				if (rowPageTracker.checkRemainingRowSufficient(dataRowCount) < 0 || (rowPageTracker.checkRemainingRowSufficient(dataRowCount) == 0 && dataRowCount == 1)) {
					sheet.getHorizontalPageBreaks().add(currentRow);
					rowPageTracker.resetRemainingRow();
				}
				
				Range dateRangeTemp;
				if (colorWhite) // White row
					dateRangeTemp = templateSheetCollection.getRangeByName(WorkScheOutputConstants.RANGE_WHITE_ROW + dataRowCount);
				else // Light blue row
					dateRangeTemp = templateSheetCollection.getRangeByName(WorkScheOutputConstants.RANGE_LIGHTBLUE_ROW + dataRowCount);
				Range dateRange = cells.createRange(currentRow, 0, dataRowCount, DATA_COLUMN_INDEX[5]);
				dateRange.copy(dateRangeTemp);
				dateRange.setOutlineBorder(BorderType.BOTTOM_BORDER, CellBorderType.THIN, Color.getBlack());
				if (rowPageTracker.checkRemainingRowSufficient(dataRowCount) == 0) {
					rowPageTracker.useRemainingRow(dataRowCount);
					rowPageTracker.resetRemainingRow();
				}
				else {
					rowPageTracker.useRemainingRow(dataRowCount);
				}
				
				// B5_1
				Cell erAlMark = cells.get(currentRow, 0);
				erAlMark.setValue(employee.getErrorAlarmCode());
				
				// B5_2
				Cell employeeCodeCell = cells.get(currentRow, 1);
				employeeCodeCell.setValue(employee.getEmployeeCode());
				
				
				Cell employeeNameCell = cells.get(currentRow, 2);
				employeeNameCell.setValue(employee.getEmployeeName());
				
				
//				Range employeeRange = cells.createRange(currentRow, 1, dataRowCount, 2);
//				employeeRange.merge();
				
				
				if (lstItem != null) {
					// B5_3
					// Divide list into smaller lists (max 16 items)
					int size = lstItem.size();
					int numOfChunks = (int)Math.ceil((double)size / CHUNK_SIZE);
					int start, length;
					List<ActualValue> lstItemRow;
					
					int curRow = currentRow;
		
			        for(int i = 0; i < numOfChunks; i++) {
			            start = i * CHUNK_SIZE;
			            length = Math.min(size - start, CHUNK_SIZE);
		
			            lstItemRow = lstItem.subList(start, start + length);
			            for (int j = 0; j < length; j++) {
			            	// Column 4, 6, 8,...
			            	ActualValue actualValue = lstItemRow.get(j);
			            	Cell cell = cells.get(curRow, DATA_COLUMN_INDEX[0] + j * 2); 
			            	writeDetailValue(actualValue, cell);
			            }
			            
			            curRow++;
			        }
			        
			        // B5_4
			        Cell remarkCell;
			        String errorDetail = employee.getDetailedErrorData();
			        if (errorDetail.length() >= 35 && dataRowCount == 1) {
			        	int numOfChunksRemark = (int)Math.ceil((double)errorDetail.length() / 35);
						int curRowRemark = currentRow;
						String remarkContentRow;
						
			        	for(int i = 0; i < numOfChunksRemark; i++) {
				            start = i * 35;
				            length = Math.min(errorDetail.length() - start, 35);
	
				            remarkContentRow = errorDetail.substring(start, start + length);
				            
				            for (int j = 0; j < length; j++) {
				            	// Column 4, 6, 8,...
				            	remarkCell = cells.get(curRowRemark, 35); 
				            	Style style = remarkCell.getStyle();
				            	remarkCell.setValue(remarkContentRow);
								style.setHorizontalAlignment(TextAlignmentType.LEFT);
				            	setFontStyle(style);
				            	remarkCell.setStyle(style);
				            }
				            
				            curRowRemark++;
				        }
			        }
			        else {
			        	remarkCell = cells.get(currentRow,35);
			        	remarkCell.setValue(errorDetail);
			        }
				}
		        currentRow += dataRowCount;
		        colorWhite = !colorWhite; // Change to other color
		        
		        // Only break when has next iterator
		        if (dataIterator.hasNext() && condition.getPageBreakIndicator() == PageBreakIndicator.EMPLOYEE) {
		        	// Thin border for last row of page
		        	Range lastRowRange = cells.createRange(currentRow - 1, 0, 1, 39);
		        	lastRowRange.setOutlineBorder(BorderType.BOTTOM_BORDER, CellBorderType.THIN, Color.getBlack());
		        	
		        	// Page break by employee
					sheet.getHorizontalPageBreaks().add(currentRow);
					rowPageTracker.resetRemainingRow();
		        }
			}
		}
		
		// Workplace total, root workplace use gross total instead
		if (condition.getSettingDetailTotalOutput().isWorkplaceTotal() && rootWorkplace.getLevel() != 0) {
			if (rowPageTracker.checkRemainingRowSufficient(dataRowCount) < 0 || (rowPageTracker.checkRemainingRowSufficient(dataRowCount) == 0 && dataRowCount == 1)) {
				sheet.getHorizontalPageBreaks().add(currentRow);
				rowPageTracker.resetRemainingRow();
			}
			Range workplaceTotalTemp = templateSheetCollection.getRangeByName(WorkScheOutputConstants.RANGE_TOTAL_ROW + dataRowCount);
			Range workplaceTotal = cells.createRange(currentRow, 0, dataRowCount, DATA_COLUMN_INDEX[5]);
			workplaceTotal.copy(workplaceTotalTemp);
			workplaceTotal.setOutlineBorder(BorderType.TOP_BORDER, CellBorderType.DOUBLE, Color.getBlack());
			workplaceTotal.setOutlineBorder(BorderType.BOTTOM_BORDER, CellBorderType.THIN, Color.getBlack());
			if (rowPageTracker.checkRemainingRowSufficient(dataRowCount) == 0) {
				rowPageTracker.useRemainingRow(dataRowCount);
				rowPageTracker.resetRemainingRow();
			}
			else {
				rowPageTracker.useRemainingRow(dataRowCount);
			}
			
			// B6_1
			Cell workplaceTotalCellTag = cells.get(currentRow, 0);
			workplaceTotalCellTag.setValue(WorkScheOutputConstants.WORKPLACE_TOTAL);
			
			// B6_2
			if (rootWorkplace.hasData)
			currentRow = writeWorkplaceTotal(currentRow, rootWorkplace, sheet, dataRowCount, true);
		}
		
		boolean firstWorkplace = true;

		
		Map<String, DailyWorkplaceData> mapChildWorkplace = rootWorkplace.getLstChildWorkplaceData();
		int childHasDataCount = (int) mapChildWorkplace.values().stream().map(child -> child.isHasData()).filter(child -> child).count();
		if (((condition.getPageBreakIndicator() == PageBreakIndicator.WORKPLACE || 
				condition.getPageBreakIndicator() == PageBreakIndicator.EMPLOYEE) &&
				mapChildWorkplace.size() > 0) && rootWorkplace.level != 0 && childHasDataCount>0 && rootWorkplace.isHasData()) {
			Range lastRowRange = cells.createRange(currentRow - 1, 0, 1, 39);
        	lastRowRange.setOutlineBorder(BorderType.BOTTOM_BORDER, CellBorderType.THIN, Color.getBlack());
        	rowPageTracker.resetRemainingRow();
			sheet.getHorizontalPageBreaks().add(currentRow);
		}
		// Child workplace
		for (Map.Entry<String, DailyWorkplaceData> entry: mapChildWorkplace.entrySet()) {
			// Page break by workplace
			DailyWorkplaceData childWorkplaceData = entry.getValue();
			//&& childWorkplaceData.is
			if ((condition.getPageBreakIndicator() == PageBreakIndicator.WORKPLACE || 
					condition.getPageBreakIndicator() == PageBreakIndicator.EMPLOYEE) && !firstWorkplace  && childWorkplaceData.isHasData()) {
				Range lastRowRange = cells.createRange(currentRow - 1, 0, 1, 39);
	        	lastRowRange.setOutlineBorder(BorderType.BOTTOM_BORDER, CellBorderType.THIN, Color.getBlack());
	        	rowPageTracker.resetRemainingRow();
				sheet.getHorizontalPageBreaks().add(currentRow);
			}
			firstWorkplace = firstWorkplace && !childWorkplaceData.isHasData();
			
			currentRow = writeDailyDetailedPerformanceDataOnWorkplace(currentRow, sheet, templateSheetCollection, childWorkplaceData, dataRowCount, condition, rowPageTracker);
		}
		
		// Workplace hierarchy total
		int level = rootWorkplace.getLevel();
		TotalWorkplaceHierachy settingTotalHierarchy = condition.getSettingDetailTotalOutput().getWorkplaceHierarchyTotal();
		
		List<Integer> lstBetweenLevel = findEnabledLevelBetweenWorkplaces(rootWorkplace, settingTotalHierarchy);
		Iterator<Integer> levelIterator = null;
		if (!lstBetweenLevel.isEmpty()) {
			levelIterator = lstBetweenLevel.iterator();
		}
		
		if (level != 0 && level >= settingTotalHierarchy.getHighestLevelEnabled() 
				&& condition.getSettingDetailTotalOutput().isCumulativeWorkplace()) {
			do {
				if (levelIterator != null && levelIterator.hasNext()) 
					level = (int) levelIterator.next();
				if (!settingTotalHierarchy.checkLevelEnabled(level)) break;
				if (rowPageTracker.checkRemainingRowSufficient(dataRowCount) < 0 || (rowPageTracker.checkRemainingRowSufficient(dataRowCount) == 0 && dataRowCount == 1)) {
					sheet.getHorizontalPageBreaks().add(currentRow);
					rowPageTracker.resetRemainingRow();
				}
				Range wkpHierTotalRangeTemp = templateSheetCollection.getRangeByName(WorkScheOutputConstants.RANGE_TOTAL_ROW + dataRowCount);
				Range wkpHierTotalRange = cells.createRange(currentRow, 0, dataRowCount, 39);
				wkpHierTotalRange.copy(wkpHierTotalRangeTemp);
				wkpHierTotalRange.setOutlineBorder(BorderType.BOTTOM_BORDER, CellBorderType.THIN, Color.getBlack());
				if (rowPageTracker.checkRemainingRowSufficient(dataRowCount) == 0) {
					rowPageTracker.useRemainingRow(dataRowCount);
					rowPageTracker.resetRemainingRow();
				}
				else {
					rowPageTracker.useRemainingRow(dataRowCount);
				}
				
				// B7_1 - B11_1
				Cell workplaceTotalCellTag = cells.get(currentRow, 0);
				workplaceTotalCellTag.setValue(WorkScheOutputConstants.WORKPLACE_HIERARCHY_TOTAL + level);
				
				// B7_2 - B11_2
				currentRow = writeWorkplaceTotal(currentRow, rootWorkplace, sheet, dataRowCount, false);
			} while (levelIterator != null && levelIterator.hasNext());
		}
		
		return currentRow;
	}

	/**
	 * Write workplace total.
	 *
	 * @param currentRow the current row
	 * @param rootWorkplace the root workplace
	 * @param sheet the sheet
	 * @param dataRowCount the data row count
	 * @param totalType the total type
	 * @return the int
	 */
	private int writeWorkplaceTotal(int currentRow, DailyWorkplaceData rootWorkplace, Worksheet sheet, int dataRowCount, boolean totalType) {
		List<TotalValue> totalWorkplaceValue;
		if (!totalType)
			totalWorkplaceValue = rootWorkplace.getWorkplaceTotal().getTotalWorkplaceValue();
		else
			totalWorkplaceValue = rootWorkplace.getWorkplaceHierarchyTotal().getTotalWorkplaceValue();
		int size = totalWorkplaceValue.size();
		if (size == 0) {
			currentRow += dataRowCount;
		}
		else {
			
			Cells cells = sheet.getCells(); 	
			
			int numOfChunks = (int) Math.ceil( (double) size / CHUNK_SIZE);
			int start, length;
			List<TotalValue> lstItemRow;
			
			for(int i = 0; i < numOfChunks; i++) {
			    start = i * CHUNK_SIZE;
			    length = Math.min(size - start, CHUNK_SIZE);

			    lstItemRow = totalWorkplaceValue.stream().collect(Collectors.toList()).subList(start, start + length);
			    TotalValue totalValue;
			    
			    for (int j = 0; j < length; j++) {
			    	totalValue = lstItemRow.get(j);
		        	Cell cell = cells.get(currentRow, DATA_COLUMN_INDEX[0] + j * 2); 
		        	writeTotalValue(totalValue, cell);
			    }
			    currentRow++;
			}
			
			if (numOfChunks == 0) currentRow++;
		}
		
		return currentRow;
	}

	/**
	 * Write total value.
	 *
	 * @param totalValue the total value
	 * @param cell the cell
	 */
	private void writeTotalValue(TotalValue totalValue, Cell cell) {
		Style style = cell.getStyle();
		String value = totalValue.getValue();
    	ValueType valueTypeEnum = EnumAdaptor.valueOf(totalValue.getValueType(), ValueType.class);
    	if (valueTypeEnum.isIntegerCountable()) {
    		if ((valueTypeEnum == ValueType.COUNT) && value != null) {
    			cell.putValue(value, true);
    		}
    		else {
    			if (value != null)
					cell.setValue(getTimeAttr(value, false));
				else
					cell.setValue(getTimeAttr("0", false));
    		}
			style.setHorizontalAlignment(TextAlignmentType.RIGHT);
		}
    	else if ((valueTypeEnum == ValueType.COUNT_WITH_DECIMAL || valueTypeEnum == ValueType.AMOUNT) && value != null) {
    		cell.putValue(value, true);
    	}
    	setFontStyle(style);
    	cell.setStyle(style);
	}
	
	/**
	 * Write gross total.
	 *
	 * @param currentRow the current row
	 * @param lstGrossTotal the lst gross total
	 * @param sheet the sheet
	 * @param dataRowCount the data row count
	 * @param rowPageTracker the row page tracker
	 * @return the int
	 */
	private int writeGrossTotal(int currentRow, List<TotalValue> lstGrossTotal, Worksheet sheet, int dataRowCount, RowPageTracker rowPageTracker) {
		int size = lstGrossTotal.size();
		if (size == 0) {
			currentRow += dataRowCount;
		}
		else {
			Cells cells = sheet.getCells();
			
			int numOfChunks = (int) Math.ceil( (double) size / CHUNK_SIZE);
			int start, length;
			List<TotalValue> lstItemRow;
			
			for(int i = 0; i < numOfChunks; i++) {
			    start = i * CHUNK_SIZE;
			    length = Math.min(lstGrossTotal.size() - start, CHUNK_SIZE);

			    lstItemRow = lstGrossTotal.stream().collect(Collectors.toList()).subList(start, start + length);
			    
			    TotalValue totalValue;
			    
			    for (int j = 0; j < length; j++) {
			    	totalValue =lstItemRow.get(j);
		        	Cell cell = cells.get(currentRow, DATA_COLUMN_INDEX[0] + j * 2); 
		        	writeTotalValue(totalValue, cell);
			    }
			    currentRow++;
			}
			
			if (numOfChunks == 0) currentRow++;
		}
		
		return currentRow;
	}
	
	/**
	 * Hide footer.
	 *
	 * @param sheet the sheet
	 */
	private void hideFooter(Worksheet sheet) {
		PageSetup pageSetup = sheet.getPageSetup();
		pageSetup.setFooter(0, "");
	}
	
	/**
	 * Creates the print area.
	 *
	 * @param currentRow the current row
	 * @param sheet the sheet
	 */
	private void createPrintArea(int currentRow, Worksheet sheet) {
		// Get the last column and row name
		Cells cells = sheet.getCells();
		Cell lastCell = cells.get(currentRow - 1, 38);
		String lastCellName = lastCell.getName();
		
		PageSetup pageSetup = sheet.getPageSetup();
		pageSetup.setPrintArea("A1:" + lastCellName);
	}
	
	/**
	 * Gets the time attr.
	 *
	 * @param rawValue the raw value
	 * @param isConvertAttr the is convert attr
	 * @return the time attr
	 */
	private String getTimeAttr(String rawValue, boolean isConvertAttr) {
		int value = Integer.parseInt(rawValue);
		TimeDurationFormatExtend timeFormat = new TimeDurationFormatExtend(value);
		if (isConvertAttr) {
			//AttendanceTimeOfExistMinus time = new AttendanceTimeOfExistMinus(value);
			return timeFormat.getFullText();
		}
		else {
			return timeFormat.getTimeText();
		}
	}
	
	/**
	 * Find workplace higher enabled level.
	 *
	 * @param workplaceReportData the workplace report data
	 * @param hierarchy the hierarchy
	 * @return the int
	 */
	private int findWorkplaceHigherEnabledLevel(WorkplaceReportData workplaceReportData, TotalWorkplaceHierachy hierarchy) {
		int currentLevel = workplaceReportData.getLevel();
		if (currentLevel != 0) {
			int lowerEnabledLevel = hierarchy.getLowerClosestSelectedHierarchyLevel(currentLevel);
			if (lowerEnabledLevel == 0) {
				WorkplaceReportData parentWorkplace = workplaceReportData.getParent();
				return findWorkplaceHigherEnabledLevel(parentWorkplace, hierarchy);
			}
			return lowerEnabledLevel;
		}
		else {
			return 0;
		}
	}
	
	/**
	 * Find enabled level between workplaces.
	 *
	 * @param workplaceReportData the workplace report data
	 * @param hierarchy the hierarchy
	 * @return the list
	 */
	private List<Integer> findEnabledLevelBetweenWorkplaces(WorkplaceReportData workplaceReportData, TotalWorkplaceHierachy hierarchy) {
		List<Integer> lstEnabledLevel = new ArrayList<>();
		
		if (workplaceReportData.getLevel() == 0)
			return lstEnabledLevel;
		
		WorkplaceReportData parentWorkplace = workplaceReportData.getParent();
		for (int i = workplaceReportData.getLevel(); i > parentWorkplace.getLevel(); i--) {
			if (hierarchy.checkLevelEnabled(i))
				lstEnabledLevel.add(i);
		}
		return lstEnabledLevel;
	}
	
	/**
	 * Find enabled level between workplaces.
	 *
	 * @param workplaceReportData the workplace report data
	 * @param hierarchy the hierarchy
	 * @return the list
	 */
	private List<Integer> findEnabledLevelBetweenWorkplaces(DailyWorkplaceData workplaceReportData, TotalWorkplaceHierachy hierarchy) {
		List<Integer> lstEnabledLevel = new ArrayList<>();
		
		if (workplaceReportData.getLevel() == 0)
			return lstEnabledLevel;
		
		DailyWorkplaceData parentWorkplace = workplaceReportData.getParent();
		for (int i = workplaceReportData.getLevel(); i > parentWorkplace.getLevel(); i--) {
			if (hierarchy.checkLevelEnabled(i))
				lstEnabledLevel.add(i);
		}
		return lstEnabledLevel;
	}
	
	
	/**
	 * Align top cotent.
	 *
	 * @param sheet the sheet
	 */
	private void alignTopCotent(Worksheet sheet) {
		PageSetup pageSetup = sheet.getPageSetup();
		pageSetup.setCenterHorizontally(true);
		pageSetup.setCenterVertically(false); 
	}
	
	/**
	 * Gets the remark content.
	 *
	 * @param employeeId the employee id
	 * @param currentDate the current date
	 * @param choice the choice
	 * @param lstOutputErrorCode the lst output error code
	 * @param remarkDataContainer the remark data container
	 * @return the remark content
	 */
	public Optional<PrintRemarksContent> getRemarkContent(String employeeId, GeneralDate currentDate,
			RemarksContentChoice choice, List<ErrorAlarmWorkRecordCode> lstOutputErrorCode, RemarkQueryDataContainer remarkDataContainer) {
		PrintRemarksContent printRemarksContent = null;
		List<EmployeeDailyPerError> errorList = remarkDataContainer.getErrorList();
		//List<String> errorCodeList = lstOutputErrorCode.stream().map(x -> x.v()).collect(Collectors.toList());
		
		// 遅刻早退
		if (errorList.size() > 0) {
			// Late come
			boolean isLateCome = false, isEarlyLeave = false;
			Optional<EmployeeDailyPerError> optErrorLateCome = errorList.stream()
					.filter(x -> x.getErrorAlarmWorkRecordCode().v().contains(SystemFixedErrorAlarm.LATE.value)).findFirst();
			if (optErrorLateCome.isPresent() && (choice == RemarksContentChoice.LEAVING_EARLY)) {
				isLateCome = true;
			}
			
			// Early leave
			Optional<EmployeeDailyPerError> optErrorEarlyLeave = errorList.stream()
					.filter(x -> x.getErrorAlarmWorkRecordCode().v().contains(SystemFixedErrorAlarm.LEAVE_EARLY.value)).findFirst();
			if (optErrorEarlyLeave.isPresent() && (choice == RemarksContentChoice.LEAVING_EARLY)) {
				isEarlyLeave = true;
			}
			
			// Both case
			if (isLateCome && isEarlyLeave) {
				printRemarksContent = new PrintRemarksContent(1, RemarksContentChoice.LATE_COME_EARLY_LEAVE.value);
			}
			// Late come
			else if (isLateCome) {
				printRemarksContent = new PrintRemarksContent(1, RemarksContentChoice.LATE_COME.value);
			}
			// Early leave
			else if (isEarlyLeave) {
				printRemarksContent = new PrintRemarksContent(1, RemarksContentChoice.LEAVING_EARLY.value);
			}
		}
		
		if (choice == RemarksContentChoice.MANUAL_INPUT || choice == RemarksContentChoice.ACKNOWLEDGMENT) {
			// 手入力
			List<EditStateOfDailyPerformanceDto> editStateDto = remarkDataContainer.getEditStateDto();
			List<GeneralDate> lstEditStateDate = new ArrayList<>();
			// Likely lstEditStateDate only has 0-1 element
			editStateDto.stream().filter(x -> (x.getEditStateSetting() == EditStateSetting.HAND_CORRECTION_MYSELF.value
										   || x.getEditStateSetting() == EditStateSetting.HAND_CORRECTION_OTHER.value)
										   && x.getYmd().compareTo(currentDate) == 0).forEach(x -> lstEditStateDate.add(x.getYmd()));
			
			if (lstEditStateDate.size() > 0 && choice == RemarksContentChoice.MANUAL_INPUT) {
				printRemarksContent = new PrintRemarksContent(1, RemarksContentChoice.MANUAL_INPUT.value);
			}
			
			// 承認反映
			Boolean isApprovalDate = editStateDto.stream().filter(x -> x.getEditStateSetting() == EditStateSetting.REFLECT_APPLICATION.value).map(x -> {return x.getYmd();}).anyMatch(y-> y.equals(currentDate));
			if (isApprovalDate && choice == RemarksContentChoice.ACKNOWLEDGMENT) {
				printRemarksContent = new PrintRemarksContent(1, RemarksContentChoice.ACKNOWLEDGMENT.value);
			}
		}
		
		// 未計算
		List<WorkInfoOfDailyPerformanceDetailDto> dailyPerformanceList = remarkDataContainer.getDailyPerformanceList();
		if (dailyPerformanceList != null && !dailyPerformanceList.isEmpty() && choice == RemarksContentChoice.NOT_CALCULATED) {
			Optional<WorkInfoOfDailyPerformanceDetailDto> optDailyPerformanceWorkInfo = dailyPerformanceList.stream().filter(info -> info.getYmd().compareTo(currentDate) == 0
					&& info.getCalculationState() == CalculationStateDto.No_Calculated
					&& StringUtils.equals(info.getEmployeeId(), employeeId)).findFirst();
			if (optDailyPerformanceWorkInfo.isPresent()) {
				printRemarksContent = new PrintRemarksContent(1, RemarksContentChoice.NOT_CALCULATED.value);
			}
		}
		
		// 事前申請超過
		Optional<EmployeeDailyPerError> optErrorExceedByApplication = errorList.stream()
				.filter(x -> x.getErrorAlarmWorkRecordCode().v().contains(SystemFixedErrorAlarm.PRE_OVERTIME_APP_EXCESS.value)
						  || x.getErrorAlarmWorkRecordCode().v().contains(SystemFixedErrorAlarm.PRE_HOLIDAYWORK_APP_EXCESS.value)
						  || x.getErrorAlarmWorkRecordCode().v().contains(SystemFixedErrorAlarm.PRE_FLEX_APP_EXCESS.value)
						  || x.getErrorAlarmWorkRecordCode().v().contains(SystemFixedErrorAlarm.PRE_MIDNIGHT_EXCESS.value)).findFirst();
		if (optErrorExceedByApplication.isPresent() && (choice == RemarksContentChoice.EXCEED_BY_APPLICATION)) {
			printRemarksContent = new PrintRemarksContent(1, RemarksContentChoice.EXCEED_BY_APPLICATION.value);
		}
		
		// 打刻順序不正
		Optional<EmployeeDailyPerError> optErrorIncorrectStamp = errorList.stream()
				.filter(x -> x.getErrorAlarmWorkRecordCode().v().contains(SystemFixedErrorAlarm.INCORRECT_STAMP.value)).findFirst();
		if (optErrorIncorrectStamp.isPresent() && (choice == RemarksContentChoice.IMPRINTING_ORDER_NOT_CORRECT)) {
			printRemarksContent = new PrintRemarksContent(1, RemarksContentChoice.IMPRINTING_ORDER_NOT_CORRECT.value);
		}
		
		// 打刻漏れ
		Optional<EmployeeDailyPerError> optErrorEngraving = errorList.stream()
				.filter(x -> x.getErrorAlarmWorkRecordCode().v().contains(SystemFixedErrorAlarm.TIME_LEAVING_STAMP_LEAKAGE.value)).findFirst();
		if (optErrorEngraving.isPresent() && (choice == RemarksContentChoice.ENGRAVING)) {
			printRemarksContent = new PrintRemarksContent(1, RemarksContentChoice.ENGRAVING.value);
		}
		
		// 二重打刻
		Optional<EmployeeDailyPerError> optErrorDoubleStamp = errorList.stream()
				.filter(x -> x.getErrorAlarmWorkRecordCode().v().contains(SystemFixedErrorAlarm.DOUBLE_STAMP.value)).findFirst();
		if (optErrorDoubleStamp.isPresent() && (choice == RemarksContentChoice.DOUBLE_ENGRAVED)) {
			printRemarksContent = new PrintRemarksContent(1, RemarksContentChoice.DOUBLE_ENGRAVED.value);
		}
		
		// 休日打刻
		Optional<EmployeeDailyPerError> optErrorHolidayStamp = errorList.stream()
				.filter(x -> x.getErrorAlarmWorkRecordCode().v().contains(SystemFixedErrorAlarm.HOLIDAY_STAMP.value)).findFirst();
		if (optErrorHolidayStamp.isPresent() && (choice == RemarksContentChoice.HOLIDAY_STAMP)) {
			printRemarksContent = new PrintRemarksContent(1, RemarksContentChoice.HOLIDAY_STAMP.value);
		}
		
		return Optional.ofNullable(printRemarksContent);
	}
	
	/**
	 * Gets the name from code.
	 *
	 * @param attendanceId the attendance id
	 * @param code the code
	 * @param queryData the query data
	 * @param outSche the out sche
	 * @return the name from code
	 */
	private String getNameFromCode(int attendanceId, String code, WorkScheduleQueryData queryData, OutputItemDailyWorkSchedule outSche) {
		// Get all data
		List<WorkType> lstWorkType = queryData.getLstWorkType();
		List<WorkTimeSetting> lstWorkTime = queryData.getLstWorkTime();
		List<CodeName> lstWorkplaceInfo = queryData.getLstWorkplaceInfo();
		List<CodeName> lstWorkLocation = queryData.getLstWorkLocation();
		List<CodeName> lstReason = queryData.getLstReason();
		List<CodeName> lstClassification = queryData.getLstClassification();
		List<CodeName> lstPosition = queryData.getLstPosition();
		List<CodeName> lstEmployment = queryData.getLstEmployment();
		
		// Not set -> won't check master unregistered
		if (StringUtils.isEmpty(code)) {
			return "";
		}
		
		if (IntStream.of(ATTENDANCE_ID_WORK_TYPE).anyMatch(id -> id == attendanceId)) {
			Optional<WorkType> optWorkType = lstWorkType.stream()
					.filter(type -> type.getWorkTypeCode().v().equalsIgnoreCase(code)).findFirst();

			if (!optWorkType.isPresent()) {
				return MASTER_UNREGISTERED;
			}

			WorkType workType = optWorkType.get();
			if (outSche.getWorkTypeNameDisplay() == NameWorkTypeOrHourZone.OFFICIAL_NAME) {
				return workType.getName().v();
			}

			return workType.getAbbreviationName().v();
		}
		
		if (IntStream.of(ATTENDANCE_ID_WORK_TIME).anyMatch(id -> id == attendanceId)) {
			Optional<WorkTimeSetting> optWorkTime = lstWorkTime.stream()
					.filter(type -> type.getWorktimeCode().v().equalsIgnoreCase(code)).findFirst();
			
			if (!optWorkTime.isPresent()) {
				return MASTER_UNREGISTERED;
			}

			WorkTimeSetting workTime = optWorkTime.get();
			if (outSche.getWorkTypeNameDisplay() == NameWorkTypeOrHourZone.OFFICIAL_NAME) {
				return workTime.getWorkTimeDisplayName().getWorkTimeName().v();
			}

			return workTime.getWorkTimeDisplayName().getWorkTimeAbName().v();
		}
		
		if (IntStream.of(ATTENDANCE_ID_WORK_LOCATION).anyMatch(id -> id == attendanceId)) {
			Optional<CodeName> optWorkLocation = lstWorkLocation.stream()
					.filter(location -> location.getCode().equalsIgnoreCase(code)).findFirst();
			
			if (!optWorkLocation.isPresent()) {
				return MASTER_UNREGISTERED;
			}
			
			CodeName workLocation = optWorkLocation.get();
			return workLocation.getName();
		}
		
		if (attendanceId == ATTENDANCE_ID_WORKPLACE) {
			Optional<CodeName> optWorkplace = lstWorkplaceInfo.stream()
					.filter(workplace -> workplace.getId().equalsIgnoreCase(code)).findFirst();
			if (!optWorkplace.isPresent()) {
				return MASTER_UNREGISTERED;
			}

			CodeName workplace = optWorkplace.get();
			return workplace.getName();
		}

		if (IntStream.of(ATTENDANCE_ID_REASON).anyMatch(id -> id == attendanceId)) {
			String itemId = this.getIdFromAttendanceId(attendanceId);
			List<CodeName> optReason = lstReason.stream().filter(reason -> reason.getCode().equalsIgnoreCase(code))
					.filter(item -> item.getId().equalsIgnoreCase(itemId)).collect(Collectors.toList());
			if (optReason.isEmpty()) {
				return MASTER_UNREGISTERED;
			}
			return optReason.get(0).getName();
		}
		
		if (attendanceId == ATTENDANCE_ID_CLASSIFICATION) {
			Optional<CodeName> optClassification = lstClassification.stream()
					.filter(classification -> classification.getCode().equalsIgnoreCase(code))
					.findFirst();

			if (!optClassification.isPresent()) {
				return MASTER_UNREGISTERED;
			}

			CodeName classification = optClassification.get();
			return classification.getName();
		}
		
		if (attendanceId == ATTENDANCE_ID_POSITION) {
			Optional<CodeName> optPosition = lstPosition.stream()
					.filter(position -> position.getId().equalsIgnoreCase(code)).findFirst();

			if (!optPosition.isPresent()) {
				return MASTER_UNREGISTERED;
			}

			CodeName position = optPosition.get();
			return position.getName();
		}
		
		if (attendanceId == ATTENDANCE_ID_EMPLOYMENT) {
			Optional<CodeName> optEmployment = lstEmployment.stream()
					.filter(employment -> employment.getCode().equalsIgnoreCase(code)).findFirst();
			if (!optEmployment.isPresent()) {
				return MASTER_UNREGISTERED;
			}

			CodeName employment = optEmployment.get();
			return employment.getName();
		}

		if (IntStream.of(ATTENDANCE_ID_CALCULATION_MAP).anyMatch(id -> id == attendanceId)) {
			return EnumAdaptor.valueOf(Integer.valueOf(code), AutoCalAtrOvertime.class).description;
		}

		if (IntStream.of(ATTENDANCE_ID_OVERTIME_MAP).anyMatch(id -> id == attendanceId)) {
			return EnumAdaptor.valueOf(Integer.valueOf(code),
					TimeLimitUpperLimitSetting.class).description;
		}

		if (IntStream.of(ATTENDANCE_ID_OUTSIDE_MAP).anyMatch(id -> id == attendanceId)) {
			return TextResource.localize(
					EnumAdaptor.valueOf(Integer.valueOf(code), GoingOutReason.class).nameId);
		}

		if (IntStream.of(ATTENDANCE_ID_USE_MAP).anyMatch(id -> id == attendanceId)) {
			return EnumAdaptor.valueOf(Integer.valueOf(code), ManageAtr.class).description;
		}

		return "";
	}

	//convert from attendanceId to reasonId
	private String getIdFromAttendanceId(int attendanceId) {
		int indexNumber = 0;
		for (int i = 0; i < ATTENDANCE_ID_REASON.length; i++)
			if (ATTENDANCE_ID_REASON[i] == attendanceId) {
				indexNumber = i;
			}
		//because index start from 0
		return String.valueOf(indexNumber + 1);
	}
	
}
