/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.at.infra.schedule.monthly;

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

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
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
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.function.dom.monthlyworkschedule.MonthlyAttendanceItemsDisplay;
import nts.uk.ctx.at.function.dom.monthlyworkschedule.OutputItemMonthlyWorkSchedule;
import nts.uk.ctx.at.function.dom.monthlyworkschedule.OutputItemMonthlyWorkScheduleRepository;
import nts.uk.ctx.at.function.dom.monthlyworkschedule.PrintSettingRemarksColumn;
import nts.uk.ctx.at.record.app.service.attendanceitem.value.AttendanceItemValueService;
import nts.uk.ctx.at.record.app.service.attendanceitem.value.AttendanceItemValueService.MonthlyAttendanceItemValueResult;
import nts.uk.ctx.at.request.dom.application.common.adapter.workplace.WkpHistImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.workplace.WorkplaceAdapter;
import nts.uk.ctx.at.schedule.dom.adapter.employment.EmploymentHistoryImported;
import nts.uk.ctx.at.schedule.dom.adapter.employment.ScEmploymentAdapter;
import nts.uk.ctx.at.schedule.dom.adapter.executionlog.SCEmployeeAdapter;
import nts.uk.ctx.at.schedule.dom.adapter.executionlog.dto.EmployeeDto;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ItemValue;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ValueType;
import nts.uk.ctx.at.shared.dom.monthlyattditem.MonthlyAttendanceItemAtr;
import nts.uk.ctx.at.shared.dom.scherec.dailyattendanceitem.adapter.attendanceitemname.AttItemName;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattendanceitem.service.CompanyMonthlyItemService;
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
import nts.uk.file.at.app.export.dailyschedule.FileOutputType;
import nts.uk.file.at.app.export.dailyschedule.TotalWorkplaceHierachy;
import nts.uk.file.at.app.export.dailyschedule.WorkScheduleSettingTotalOutput;
import nts.uk.file.at.app.export.dailyschedule.data.EmployeeReportData;
import nts.uk.file.at.app.export.dailyschedule.data.OutputItemSetting;
import nts.uk.file.at.app.export.dailyschedule.data.WorkplaceReportData;
import nts.uk.file.at.app.export.dailyschedule.totalsum.TotalSumRowOfDailySchedule;
import nts.uk.file.at.app.export.dailyschedule.totalsum.TotalValue;
import nts.uk.file.at.app.export.dailyschedule.totalsum.WorkplaceTotal;
import nts.uk.file.at.app.export.employee.jobtitle.EmployeeJobHistExport;
import nts.uk.file.at.app.export.employee.jobtitle.JobTitleImportAdapter;
import nts.uk.file.at.app.export.monthlyschedule.DetailedMonthlyPerformanceReportData;
import nts.uk.file.at.app.export.monthlyschedule.MonthlyRecordValuesExport;
import nts.uk.file.at.app.export.monthlyschedule.MonthlyWorkScheduleCondition;
import nts.uk.file.at.app.export.monthlyschedule.MonthlyWorkScheduleGenerator;
import nts.uk.file.at.app.export.monthlyschedule.MonthlyWorkScheduleQuery;
import nts.uk.file.at.infra.schedule.RowPageTracker;
import nts.uk.file.at.infra.schedule.daily.TimeDurationFormatExtend;
import nts.uk.file.at.infra.schedule.daily.WorkScheOutputConstants;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
import nts.uk.shr.com.time.calendar.period.YearMonthPeriod;
import nts.uk.shr.infra.file.report.aspose.cells.AsposeCellsReportContext;
import nts.uk.shr.infra.file.report.aspose.cells.AsposeCellsReportGenerator;

/**
 * The Class AsposeMonthlyWorkScheduleGenerator.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class AsposeMonthlyWorkScheduleGenerator extends AsposeCellsReportGenerator
		implements MonthlyWorkScheduleGenerator {

	/** The company repository. */
	@Inject
	private CompanyRepository companyRepository;

	@Inject
	private AttendanceItemValueService  attendanceItemValueService;
	
	/** The workplace adapter. */
	@Inject
	private WorkplaceAdapter workplaceAdapter;

	/** The workplace config repository. */
	@Inject
	private WorkplaceConfigInfoRepository workplaceConfigRepository;

	/** The employee adapter. */
	@Inject
	private SCEmployeeAdapter employeeAdapter;
	
	/** The workplace info repository. */
	@Inject
	private WorkplaceInfoRepository workplaceInfoRepository;

	/**  The employment adapter. */
	@Inject
	private ScEmploymentAdapter employmentAdapter;

	/** The job title adapter. */
	@Inject
	private JobTitleImportAdapter jobTitleAdapter;

	/** The finder. */
	@Inject
	private EmploymentRepository employmentRepository;
	
	/** The output item repo. */
	@Inject
	private OutputItemMonthlyWorkScheduleRepository outputItemRepo;
	
	/** The company monthly item service. */
	@Inject
	private CompanyMonthlyItemService companyMonthlyItemService;
	
	@Inject
	private ManagedParallelWithContext parallel;

	/** The Constant TEMPLATE_DATE. */
	private static final String TEMPLATE_DATE= "report/KWR006_Date.xlsx";

	/** The Constant TEMPLATE_EMPLOYEE. */
	private static final String TEMPLATE_EMPLOYEE = "report/KWR006_Employee.xlsx";
	
	/** The Constant CHUNK_SIZE. */
	private static final int CHUNK_SIZE = 16;

	/** The Constant DATA_COLUMN_INDEX. */
	private static final int[] DATA_COLUMN_INDEX = {3, 9, 11, 15, 17, 39};

	/** The font family. */
	private final String FONT_FAMILY = "ＭＳ ゴシック";

	/** The font size. */
	private final double FONT_SIZE = 6.5;
	
	/** The Constant DATA_PREFIX. */
	private static final String DATA_PREFIX = "DATA_";
	
	/** The Constant DATA_PREFIX_NO_WORKPLACE. */
	private static final String DATA_PREFIX_NO_WORKPLACE = "NOWPK_";
	
	private static final int CHUNK_SIZE_ERROR = 5;
	
	private static final int REMARK_CELL_WIDTH = 4;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.file.at.app.export.dailyschedule.WorkScheduleOutputGenerator#
	 * generate(nts.uk.file.at.app.export.dailyschedule.
	 * WorkScheduleOutputCondition,
	 * nts.uk.ctx.bs.employee.dom.workplace.config.info.WorkplaceConfigInfo,
	 * nts.uk.file.at.app.export.dailyschedule.WorkScheduleOutputQuery)
	 */
	@Override
	public void generate(FileGeneratorContext generatorContext, TaskDataSetter setter, MonthlyWorkScheduleQuery query) {
		MonthlyWorkScheduleCondition condition = query.getCondition();
		AsposeCellsReportContext reportContext = null;
		if (condition.getOutputType() == MonthlyWorkScheduleCondition.EXPORT_BY_EMPLOYEE) {
			reportContext = this.createContext(TEMPLATE_EMPLOYEE);
		} else {
			reportContext = this.createContext(TEMPLATE_DATE);
		}
		Optional<OutputItemMonthlyWorkSchedule> optOutputItemMonthlyWork = outputItemRepo.findByCidAndCode(AppContexts.user().companyId(), query.getCode());
		if (!optOutputItemMonthlyWork.isPresent()) {
			throw new BusinessException(new RawErrorMessage("Msg_1141"));
		}
		OutputItemMonthlyWorkSchedule outputItemMonthlyWork = optOutputItemMonthlyWork.get();
		
		Workbook workbook;
		try {
			workbook = reportContext.getWorkbook();
			
			WorkbookDesigner designer = new WorkbookDesigner();
			designer.setWorkbook(workbook);
			
			TotalSumRowOfDailySchedule sum = new TotalSumRowOfDailySchedule();
			sum.setPersonalTotal(new ArrayList<>());
			
			// Calculate row size and get sheet
			List<MonthlyAttendanceItemsDisplay> lstAttendanceItemDisplay = outputItemMonthlyWork.getLstDisplayedAttendance();
			int nListOutputCode = lstAttendanceItemDisplay.size();
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
			MonthlyPerformanceReportData reportData = new MonthlyPerformanceReportData();
			collectData(reportData, query, condition, outputItemMonthlyWork, setter);
			
			Worksheet sheet;
			Integer currentRow, dateRow;
			
//			if (condition.getOutputType() == MonthlyWorkScheduleCondition.PAGE_BREAK_EMPLOYEE) {
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
				
//			} else {
//				switch (nSize) {
//				case 1:
//					sheet = workbook.getWorksheets().get(3);
//					break;
//				case 2:
//					sheet = workbook.getWorksheets().get(4);
//					break;
//				case 3:
//				default:
//					sheet = workbook.getWorksheets().get(5);
//					
//				}
//			}
			currentRow = 2;
			dateRow = 0;
			
			WorksheetCollection sheetCollection = workbook.getWorksheets();
			
			// Write header data
			writeHeaderData(query, outputItemMonthlyWork, sheet, reportData, dateRow);
			
			// Copy footer
			//copyFooter(sheet, sheetCollection.get(6));
			 	
			// Set all fixed cell
			setFixedData(condition, outputItemMonthlyWork, sheet, reportData, currentRow);
			
			// Write display map
			writeDisplayMap(sheet.getCells(),reportData, currentRow, nSize);
			
			currentRow+=nSize*2;
			
			// Back up start row
//			int startRow = currentRow;
			
			// Create row page tracker
			RowPageTracker rowPageTracker = new RowPageTracker();
			rowPageTracker.initMaxRowAllowedMonthly(nSize, condition.getOutputType());
			
			// Write detailed work schedule
			if (condition.getOutputType() == MonthlyWorkScheduleCondition.EXPORT_BY_EMPLOYEE)
				currentRow = writeDetailedWorkSchedule(query, currentRow, sheetCollection, sheet, reportData.getWorkplaceReportData(), nSize, condition, rowPageTracker);
			else {
				MonthlyReportData dailyReportData = reportData.getMonthlyReportData();
				currentRow = writeDetailedMonthlySchedule(currentRow, sheetCollection, sheet, dailyReportData, nSize, condition, rowPageTracker);
			}

			//hide footer
			hideFooter(sheet);
			// Delete remark content column if this option is disabled
			//always print remark 
			/*if (outputItemMonthlyWork.getPrintSettingRemarksColumn() == PrintSettingRemarksColumn.NOT_PRINT_REMARK) {
				Cells cells = sheet.getCells();
				cells.deleteColumns(29, 4, true);
				Range lastColumn = cells.createRange(startRow, 0, currentRow - 1 - startRow, 29);
				lastColumn.setOutlineBorder(BorderType.RIGHT_BORDER, CellBorderType.NONE, Color.getBlack());
				
				// Insert 3 columns to make the table at center area 
				// it should be 3 columns at both sides
				cells.insertColumns(0, 2);
			}*/
			
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
			createPrintArea(sheet);
			
			// Save workbook
			if (query.getFileType() == FileOutputType.FILE_TYPE_EXCEL)
				reportContext.saveAsExcel(this.createNewFile(generatorContext, WorkScheOutputConstants.FILE_NAME_MONTHLY + "_" + currentFormattedDate + ".xlsx"));
			else {
				reportContext.saveAsPdf(this.createNewFile(generatorContext, WorkScheOutputConstants.FILE_NAME_MONTHLY + "_" + currentFormattedDate + ".pdf"));
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Collect header data.
	 *
	 * @param headerData the header data
	 * @param condition the condition
	 */
	public void collectHeaderData(MonthlyPerformanceHeaderData headerData, MonthlyWorkScheduleCondition condition) {
		Optional<Company> optCompany = companyRepository.find(AppContexts.user().companyId());
		headerData.setFixedHeaderData(new ArrayList<>());
		if (optCompany.isPresent())
			headerData.companyName = optCompany.get().getCompanyName().v();
		else
			headerData.companyName = "";
		if (condition.getOutputType() == MonthlyWorkScheduleCondition.EXPORT_BY_EMPLOYEE) {
			headerData.fixedHeaderData.add(WorkScheOutputConstants.YEARMONTH);
		} else {
			headerData.fixedHeaderData.add(WorkScheOutputConstants.PERSONAL_NAME);
		}
		headerData.fixedHeaderData.add(WorkScheOutputConstants.CLOSURE_DATE);
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
	public void collectDisplayMap(MonthlyPerformanceHeaderData headerData, OutputItemMonthlyWorkSchedule condition, String companyId, String roleId) {
		List<MonthlyAttendanceItemsDisplay> lstItem = condition.getLstDisplayedAttendance();
		headerData.setLstOutputItemSettingCode(new ArrayList<>());
		
		List<Integer> lstAttendanceId = lstItem.stream().sorted((o1, o2) -> (o1.getOrderNo() - o2.getOrderNo())).map(x -> x.getAttendanceDisplay()).collect(Collectors.toList());
		List<AttItemName> lstAttendanceDto = companyMonthlyItemService.getMonthlyItems(companyId, Optional.of(roleId), lstAttendanceId, Arrays.asList(MonthlyAttendanceItemAtr.values()));
		if (lstAttendanceDto.isEmpty())
			throw new BusinessException(new RawErrorMessage("Msg_1417"));
//		List<AttdItemDto> lstMonthlyAttendanceItem = monthlyAttendanceItemFinder.findAll();
		condition.setLstDisplayedAttendance(lstItem.stream().filter(x -> lstAttendanceId.contains(x.getAttendanceDisplay())).collect(Collectors.toList()));
		
		lstAttendanceId.stream().forEach(x -> {
			Optional<AttItemName> opAttendanceItem = lstAttendanceDto.stream()
					.filter(item -> item.getAttendanceItemId() == x).findFirst();
			OutputItemSetting setting = new OutputItemSetting();
			// setting.setItemCode(attendanceItem.getAttendanceItemDisplayNumber());
			if (opAttendanceItem.isPresent()) {
				setting.setItemCode(opAttendanceItem.get().getAttendanceItemId());
				setting.setItemName(opAttendanceItem.get().getAttendanceItemName());
			} else {
				setting.setItemCode(null);
				setting.setItemName("");
			}
			headerData.lstOutputItemSettingCode.add(setting);
		});
	}

	/**
	 * Collect workplace hierarchy.
	 *
	 * @param workplaceId the workplace id
	 * @param baseDate the base date
	 * @param lstWorkplaceConfigInfo the lst workplace config info
	 * @return the map
	 */
	public Map<String, WorkplaceInfo> collectWorkplaceHierarchy(String workplaceId, GeneralDate baseDate, List<WorkplaceConfigInfo> lstWorkplaceConfigInfo) {
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
	 * Get lowest workplace hierarchy level.
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
	 * Find workplace by hierarchy code.
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
	 * Analyze info (export by employee).
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
					isFoundParent = true;
					break;
				}
			}

			// Not found, then add to root
			if (!isFoundParent) {
				wrp.parent = parent;
				parent.lstChildWorkplaceReportData.put(k, wrp);
			}
		}
	}

	/**
	 * Find workplace (export by employee).
	 *
	 * @param employeeId the employee id
	 * @param rootWorkplace the root workplace
	 * @param baseDate the base date
	 * @param lstWorkplaceHistImport the lst workplace hist import
	 * @param lstWorkplaceConfigInfo the lst workplace config info
	 * @return the workplace report data
	 */
	public WorkplaceReportData findWorkplace(String employeeId, WorkplaceReportData rootWorkplace, GeneralDate baseDate,
			List<WkpHistImport> lstWorkplaceHistImport, List<WorkplaceConfigInfo> lstWorkplaceConfigInfo) {
		Map<String, WorkplaceReportData> mapWorkplaceInfo = rootWorkplace.getLstChildWorkplaceReportData();
		WkpHistImport workplaceImport = lstWorkplaceHistImport.stream().filter(hist -> StringUtils.equalsIgnoreCase(hist.getEmployeeId(), employeeId) ).findFirst().get();
		WorkplaceHierarchy code = lstWorkplaceConfigInfo.stream().filter(x -> StringUtils.equalsIgnoreCase(x.getLstWkpHierarchy().get(0).getWorkplaceId(), workplaceImport.getWorkplaceId())).findFirst().get().getLstWkpHierarchy().get(0);
		HierarchyCode hierarchyCode = code.getHierarchyCode();
		if (mapWorkplaceInfo.containsKey(hierarchyCode.v())) {
			return mapWorkplaceInfo.get(hierarchyCode.v());
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
	 * Find workplace (export by date).
	 *
	 * @param employeeId the employee id
	 * @param rootWorkplace the root workplace
	 * @param baseDate the base date
	 * @param lstWkpHistImport the lst wkp hist import
	 * @param lstWorkplaceConfigInfo the lst workplace config info
	 * @return the workplace report data
	 */
	public MonthlyWorkplaceData findWorkplace(String employeeId, MonthlyWorkplaceData rootWorkplace, YearMonth baseDate, 
			List<WkpHistImport> lstWkpHistImport, List<WorkplaceConfigInfo> lstWorkplaceConfigInfo) {
		
		Map<String, MonthlyWorkplaceData> mapWorkplaceInfo = rootWorkplace.getLstChildWorkplaceData();
		WkpHistImport workplaceImport = lstWkpHistImport.stream().filter(hist -> StringUtils.equalsIgnoreCase(hist.getEmployeeId(), employeeId) ).findFirst().get();
		WorkplaceHierarchy code = lstWorkplaceConfigInfo.stream().filter(x -> StringUtils.equalsIgnoreCase(x.getLstWkpHierarchy().get(0).getWorkplaceId(), workplaceImport.getWorkplaceId())).findFirst().get().getLstWkpHierarchy().get(0);
		HierarchyCode hierarchyCode = code.getHierarchyCode();
		if (mapWorkplaceInfo.containsKey(hierarchyCode.v())) {
			return mapWorkplaceInfo.get(hierarchyCode.v());
		}
		else {
			for (Map.Entry<String, MonthlyWorkplaceData> keySet : mapWorkplaceInfo.entrySet()) {
				MonthlyWorkplaceData data = findWorkplace(employeeId, keySet.getValue(), baseDate, lstWkpHistImport, lstWorkplaceConfigInfo);
				if (data != null)
					return data;
			}
		}
		return null;
	}
	
	/**
	 * Collect data.
	 *
	 * @param reportData the report data
	 * @param query the query
	 * @param condition the condition
	 * @param outputItem the output item
	 * @param setter the setter
	 */
	public void collectData(MonthlyPerformanceReportData reportData, MonthlyWorkScheduleQuery query, MonthlyWorkScheduleCondition condition, OutputItemMonthlyWorkSchedule outputItem, TaskDataSetter setter) {
		String companyId = AppContexts.user().companyId();
		String roleId = AppContexts.user().roles().forAttendance();
		reportData.setHeaderData(new MonthlyPerformanceHeaderData());
		collectHeaderData(reportData.getHeaderData(), condition);
		collectDisplayMap(reportData.getHeaderData(), outputItem, companyId, roleId);
		YearMonth endDate = query.getEndYearMonth();
		
		WorkScheduleMonthlyQueryData queryData = new WorkScheduleMonthlyQueryData();
		queryData.setQuery(query);
		
		List<YearMonth> lstDate = new YearMonthPeriod(query.getStartYearMonth(), endDate).yearMonthsBetween();
		queryData.setMonthPeriod(lstDate);
		
		Map<String, WorkplaceInfo> lstWorkplace = new TreeMap<>(); // Automatically sort by code, will need to check hierarchy later
		List<String> lstWorkplaceId = new ArrayList<>();
		List<String> lstEmployeeNoWorkplace = new ArrayList<>();
		
		GeneralDate finalDate = query.getBaseDate();
		
		for (String employeeId: query.getEmployeeId()) {
			WkpHistImport workplaceImport = workplaceAdapter.findWkpBySid(employeeId, finalDate);
			if (workplaceImport == null) {
				lstEmployeeNoWorkplace.add(employeeId);
				continue;
			}
			lstWorkplaceId.add(workplaceImport.getWorkplaceId());
			queryData.getLstWorkplaceImport().add(workplaceImport);
		}
		
		if (!lstEmployeeNoWorkplace.isEmpty()) {
			List<EmployeeDto> lstEmployeeDto = employeeAdapter.findByEmployeeIds(lstEmployeeNoWorkplace);
			int numOfChunks = (int)Math.ceil((double)lstEmployeeDto.size() / CHUNK_SIZE_ERROR);
			int start, length;
			List<EmployeeDto> lstSplitEmployeeDto;
			for(int i = 0; i < numOfChunks; i++) {
				start = i * CHUNK_SIZE_ERROR;
	            length = Math.min(lstEmployeeDto.size() - start, CHUNK_SIZE_ERROR);

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
		
		List<WorkplaceConfigInfo> lstWorkplaceConfigInfo = workplaceConfigRepository.findByWkpIdsAtTime(companyId, finalDate, lstWorkplaceId);
		queryData.setLstWorkplaceConfigInfo(lstWorkplaceConfigInfo);
		
		// Collect child workplace, automatically sort into tree map
		for (String entry: lstWorkplaceId) {
			lstWorkplace.putAll(collectWorkplaceHierarchy(entry, finalDate, lstWorkplaceConfigInfo));
		}
		
		List<Integer> listAttendanceId = outputItem.getLstDisplayedAttendance().stream().map(item -> {
			return item.getAttendanceDisplay();
		}).collect(Collectors.toList());
		if (outputItem.getPrintSettingRemarksColumn() == PrintSettingRemarksColumn.PRINT_REMARK) {
			listAttendanceId.add(outputItem.getRemarkInputNo().value + 1283);
		}
		
		List<MonthlyAttendanceItemValueResult> itemValues;
		{
			List<MonthlyAttendanceItemValueResult> itemValuesSync = Collections.synchronizedList(new ArrayList<>());
			this.parallel.forEach(query.getEmployeeId(), subEmployeeIds -> {
				List<MonthlyAttendanceItemValueResult> subValues = attendanceItemValueService.getMonthlyValueOf(
						subEmployeeIds, new YearMonthPeriod(query.getStartYearMonth(), endDate), listAttendanceId);
				itemValuesSync.addAll(subValues);
			});
			
			// synchronizedListはこの並列部分でしか必要ないので、ここで普通のArrayListに変える。
			itemValues = new ArrayList<>(itemValuesSync);
		}
		
		List<MonthlyRecordValuesExport> lstMonthlyRecordValueExport = itemValues.stream()
				.map(item -> new MonthlyRecordValuesExport(item.getYearMonth(), item.getClosureId(),
						new ClosureDate(item.getClouseDate(), item.isLastDayOfMonth()), item.getAttendanceItems(),
						item.getEmployeeId()))
				.collect(Collectors.toList());

		queryData.setLstAttendanceResultImport(lstMonthlyRecordValueExport);
		
		// Extract list employeeId from attendance result list -> List employee won't have those w/o data
		List<String> lstEmployeeWithData = lstMonthlyRecordValueExport.stream().map(attendanceData -> {
			String employeeId = attendanceData.getEmployeeId();
			return employeeId;
		}).collect(Collectors.toList());
		
		// Extract list employeeId from attendance result list -> List employee won't have those w/o data
		// From list employeeId above -> Find back their workplace hierachy code
		//Set<String> lstEmployeeWithData = new HashSet<>();
		Set<String> lstWorkplaceIdWithData = new HashSet<>();
		lstWorkplaceIdWithData = lstMonthlyRecordValueExport.stream().map(attendanceData -> {
			String employeeId = attendanceData.getEmployeeId();
			WkpHistImport workplaceImport = queryData.getLstWorkplaceImport().stream().filter(hist -> StringUtils.equalsIgnoreCase(hist.getEmployeeId(), employeeId) ).findFirst().get();
			WorkplaceHierarchy code = lstWorkplaceConfigInfo.stream().filter(x -> StringUtils.equalsIgnoreCase(x.getLstWkpHierarchy().get(0).getWorkplaceId(), workplaceImport.getWorkplaceId())).findFirst().get().getLstWkpHierarchy().get(0);
			return code.getHierarchyCode().v();
		}).collect(Collectors.toSet());
		
		// This employee list with data, find out all other employees who don't have data.
		List<String> lstEmployeeIdNoData = query.getEmployeeId().stream().filter(x -> !lstEmployeeWithData.contains(x)).collect(Collectors.toList());
		if (!lstEmployeeIdNoData.isEmpty()) {
			List<EmployeeDto> lstEmployeeDto = employeeAdapter.findByEmployeeIds(lstEmployeeIdNoData);
			int numOfChunks = (int)Math.ceil((double)lstEmployeeDto.size() / CHUNK_SIZE_ERROR);
			int start, length;
			List<EmployeeDto> lstSplitEmployeeDto;
			for(int i = 0; i < numOfChunks; i++) {
				start = i * CHUNK_SIZE_ERROR;
	            length = Math.min(lstEmployeeDto.size() - start, CHUNK_SIZE_ERROR);

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
		WorkScheduleSettingTotalOutput totalOutputSetting = condition.getTotalOutputSetting();
		TotalWorkplaceHierachy outputSetting = totalOutputSetting.getWorkplaceHierarchyTotal();
		int highestOutputLevel = outputSetting.getHighestLevelEnabled();
		if ((lowestEmployeeLevel < highestOutputLevel && !totalOutputSetting.isDetails()
				&& !totalOutputSetting.isGrossTotal() && !totalOutputSetting.isPersonalTotal() && !totalOutputSetting.isTotalNumberDay()
				&& !totalOutputSetting.isWorkplaceTotal()) || lstMonthlyRecordValueExport.isEmpty()) {
			throw new BusinessException(new RawErrorMessage("Msg_37"));
		}
		
		// Sort attendance display
		List<MonthlyAttendanceItemsDisplay> lstAttendanceItemsDisplay = outputItem.getLstDisplayedAttendance().stream().sorted((o1,o2) -> o1.getOrderNo() - o2.getOrderNo()).collect(Collectors.toList());
		queryData.setLstDisplayItem(lstAttendanceItemsDisplay);
		
		if (condition.getOutputType() == MonthlyWorkScheduleCondition.EXPORT_BY_EMPLOYEE) {
			WorkplaceReportData data = new WorkplaceReportData();
			data.workplaceCode = "";
			reportData.setWorkplaceReportData(data);
			
			analyzeInfoExportByEmployee(lstWorkplace, data);
		
			List<EmployeeDto> lstEmloyeeDto = employeeAdapter.findByEmployeeIds(lstEmployeeWithData);
			
			lstEmloyeeDto = lstEmloyeeDto.stream().sorted((a,b)-> (a.getEmployeeCode().compareTo(b.getEmployeeCode()))).collect(Collectors.toList());
			for (EmployeeDto dto: lstEmloyeeDto) {
				collectEmployeePerformanceDataByEmployee(reportData, queryData, dto);
			}
			
			calculateTotalExportByEmployee(data, lstAttendanceItemsDisplay);
		
		}
		else {
			MonthlyReportData monthlyReportData = new MonthlyReportData();
			reportData.setMonthlyReportData(monthlyReportData);
			YearMonthPeriod period = new YearMonthPeriod(query.getStartYearMonth(), query.getEndYearMonth());
			List<WorkplaceMonthlyReportData> lstReportData = period.yearMonthsBetween().stream().map(x -> {
				WorkplaceMonthlyReportData dailyWorkplaceReportData = new WorkplaceMonthlyReportData();
				dailyWorkplaceReportData.lstWorkplaceData = new MonthlyWorkplaceData();
				dailyWorkplaceReportData.lstWorkplaceData.workplaceCode = "";
				dailyWorkplaceReportData.yearMonth = x;
				return dailyWorkplaceReportData;
			}).collect(Collectors.toList());
			monthlyReportData.setLstMonthlyReportData(lstReportData);
			
			final Map<String, WorkplaceInfo> lstWorkplaceTemp = lstWorkplace;
			List<String> lstAddedCode = new ArrayList<>();
			lstReportData.forEach(x -> {
				analyzeInfoExportByDate(lstWorkplaceTemp, x.getLstWorkplaceData(), lstAddedCode);
			});
			
			collectEmployeePerformanceDataByDate(reportData, queryData);
			// Calculate workplace total
			lstReportData.forEach(dailyData -> {
				calculateTotalExportByDate(dailyData.getLstWorkplaceData());
			});
			
			// Calculate gross total
			calculateGrossTotalByDate(monthlyReportData);
		}
	}

	/**
	 * Collect employee performance data by employee.
	 *
	 * @param reportData the report data
	 * @param queryData the query data
	 * @param employeeDto the employee dto
	 * @return the employee report data
	 */
	private EmployeeReportData collectEmployeePerformanceDataByEmployee(MonthlyPerformanceReportData reportData, WorkScheduleMonthlyQueryData queryData, EmployeeDto employeeDto) {
		String companyId = AppContexts.user().companyId();
		MonthlyWorkScheduleQuery query = queryData.getQuery();
		//YearMonth endMonth = query.getEndYearMonth();
		GeneralDate endDate = query.getBaseDate();
		//MonthlyWorkScheduleCondition condition = query.getCondition();
		
		// Always has item because this has passed error check
		OutputItemMonthlyWorkSchedule outSche = outputItemRepo.findByCidAndCode(companyId, query.getCode()).get();
		
		// Get all data from query data container
		List<MonthlyRecordValuesExport> lstAttendanceResultImport = queryData.getLstAttendanceResultImport();
		List<MonthlyAttendanceItemsDisplay> lstDisplayItem = queryData.getLstDisplayItem();
		List<WkpHistImport> lstWorkplaceImport = queryData.getLstWorkplaceImport();
		List<WorkplaceConfigInfo> lstWorkplaceConfigInfo = queryData.getLstWorkplaceConfigInfo();
		String employeeId = employeeDto.getEmployeeId();
		
		WorkplaceReportData workplaceData = findWorkplace(employeeId, reportData.getWorkplaceReportData(), endDate, lstWorkplaceImport, lstWorkplaceConfigInfo);
		EmployeeReportData employeeData = new EmployeeReportData();
		
		// Employee code and name
		employeeData.employeeName = employeeDto.getEmployeeName();
		employeeData.employeeCode = employeeDto.getEmployeeCode();
		
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
		
		employeeData.lstDetailedMonthlyPerformance = new ArrayList<>();
		workplaceData.lstEmployeeReportData.add(employeeData);
		lstAttendanceResultImport.stream().filter(x -> x.getEmployeeId().equals(employeeId)).sorted((o1,o2) -> o1.getYearMonth().compareTo(o2.getYearMonth())).forEach(x -> {
			YearMonth workingDate = x.getYearMonth();
			
			DetailedMonthlyPerformanceReportData detailedDate = new DetailedMonthlyPerformanceReportData();
			detailedDate.setYearMonth(workingDate);
			employeeData.lstDetailedMonthlyPerformance.add(detailedDate);
			
			// Closure date
			ClosureDate closureDate = x.getClosureDate();
			if (closureDate.getLastDayOfMonth()) {
				detailedDate.closureDate = WorkScheOutputConstants.CLOSURE_DATE_LAST_DAY;
			}
			else {
				detailedDate.closureDate = String.format(WorkScheOutputConstants.CLOSURE_DATE_NON_LAST_DAY, closureDate.getClosureDay().v());
			}
			
			
			// Remark content, it's full detail remark but will be processed on printing
			detailedDate.errorDetail = "";
			if (outSche.getPrintSettingRemarksColumn() == PrintSettingRemarksColumn.PRINT_REMARK) {
				Optional<ItemValue> optRemarkRecord = x.getItemValues().stream().filter(att -> att.getItemId() == outSche.getRemarkInputNo().value + 1283).findFirst();
				optRemarkRecord.ifPresent(remark -> {
					detailedDate.errorDetail += (remark.value() == null? "" : remark.value());
				});
			}
			
			detailedDate.actualValue = new ArrayList<>();
			lstDisplayItem.stream().forEach(item -> {
				Optional<ItemValue> optItemValue = x.getItemValues().stream().filter(att -> att.getItemId() == item.getAttendanceDisplay()).findFirst();
				if (optItemValue.isPresent()) {
					ItemValue itemValue = optItemValue.get();
					detailedDate.actualValue.add(new ActualValue(itemValue.getItemId(), itemValue.getValue(), itemValue.getValueType().value));
				}
				else {
					detailedDate.actualValue.add(new ActualValue(item.getAttendanceDisplay(), "", ActualValue.STRING));
				}
				// Enable data presentation
				workplaceData.setHasData(true);
			});
		});
		return employeeData;
	}
	

	/**
	 * Collect employee performance data by date.
	 *
	 * @param reportData the report data
	 * @param queryData the query data
	 */
	private void collectEmployeePerformanceDataByDate(MonthlyPerformanceReportData reportData, WorkScheduleMonthlyQueryData queryData) {
		MonthlyWorkScheduleQuery query = queryData.getQuery();
		YearMonth endDate = query.getEndYearMonth();
		// Always has item because this has passed error check
		OutputItemMonthlyWorkSchedule outSche = outputItemRepo
				.findByCidAndCode(AppContexts.user().companyId(), query.getCode()).get();
		
		// Get all data from query data container
		List<YearMonth> datePeriod = queryData.getMonthPeriod();
		List<MonthlyRecordValuesExport> lstAttendanceResultImport = queryData.getLstAttendanceResultImport();
		List<MonthlyAttendanceItemsDisplay> lstDisplayItem = queryData.getLstDisplayItem();
		List<WkpHistImport> lstWorkplaceHistImport = queryData.getLstWorkplaceImport();
		
		List<String> lstEmployeeId = query.getEmployeeId();
		List<EmployeeDto> lstEmployeeDto = employeeAdapter.findByEmployeeIds(lstEmployeeId);
		
		for (String employeeId: lstEmployeeId) {
			Optional<EmployeeDto> optEmployeeDto = lstEmployeeDto.stream().filter(employee -> StringUtils.equalsAnyIgnoreCase(employee.getEmployeeId(),employeeId)).findFirst();
			
			datePeriod.stream().forEach(date -> {
				Optional<WorkplaceMonthlyReportData> optDailyWorkplaceData = reportData.getMonthlyReportData().getLstMonthlyReportData().stream().filter(x -> x.getYearMonth().compareTo(date) == 0).findFirst();
				MonthlyWorkplaceData dailyWorkplaceData = findWorkplace(employeeId,optDailyWorkplaceData.get().getLstWorkplaceData(), endDate, lstWorkplaceHistImport, queryData.getLstWorkplaceConfigInfo());
				if (dailyWorkplaceData != null) {
					
					
					lstAttendanceResultImport.stream().filter(x -> (x.getEmployeeId().equals(employeeId) && x.getYearMonth().compareTo(date) == 0) && 
							StringUtils.equalsAnyIgnoreCase(x.getEmployeeId(), employeeId)).forEach(x -> {
						MonthlyPersonalPerformanceData personalPerformanceDate = new MonthlyPersonalPerformanceData();
								if (optEmployeeDto.isPresent()) {
									personalPerformanceDate.setEmployeeName(optEmployeeDto.get().getEmployeeName());
									personalPerformanceDate.setEmployeeCode(optEmployeeDto.get().getEmployeeCode());
								}
						dailyWorkplaceData.getLstDailyPersonalData().add(personalPerformanceDate);
						
						// Closure date
						ClosureDate closureDate = x.getClosureDate();
						if (closureDate.getLastDayOfMonth()) {
							personalPerformanceDate.closureDate = WorkScheOutputConstants.CLOSURE_DATE_LAST_DAY;
						}
						else {
							personalPerformanceDate.closureDate = String.format(WorkScheOutputConstants.CLOSURE_DATE_NON_LAST_DAY, closureDate.getClosureDay().v());
						}
						
						// Remark content, it's full detail remark but will be processed on printing
						personalPerformanceDate.detailedErrorData = "";
						if (outSche.getPrintSettingRemarksColumn() == PrintSettingRemarksColumn.PRINT_REMARK) {
							Optional<ItemValue> optRemarkRecord = x.getItemValues().stream().filter(att -> att.getItemId() == outSche.getRemarkInputNo().value + 1283).findFirst();
							optRemarkRecord.ifPresent(remark -> {
								personalPerformanceDate.detailedErrorData += (remark.value() == null? "" : remark.value());
							});
						}
						
						personalPerformanceDate.actualValue = new ArrayList<>();
						lstDisplayItem.stream().forEach(item -> {
							Optional<ItemValue> optItemValue = x.getItemValues().stream().filter(att -> att.getItemId() == item.getAttendanceDisplay()).findFirst();
							if (optItemValue.isPresent()) {
								ItemValue itemValue = optItemValue.get();
								personalPerformanceDate.actualValue.add(new ActualValue(itemValue.getItemId(), itemValue.getValue(), itemValue.getValueType().value));
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
	 * Find workplace by hierarchy code export date.
	 *
	 * @param rootWorkplace the root workplace
	 * @param hierarchyCode the hierarchy code
	 * @return the monthly workplace data
	 */
	private MonthlyWorkplaceData findWorkplaceByHierarchyCodeExportDate(MonthlyWorkplaceData rootWorkplace, String hierarchyCode) {
		MonthlyWorkplaceData workplaceReportData = null;
		
		for (Map.Entry<String, MonthlyWorkplaceData> entry : rootWorkplace.lstChildWorkplaceData.entrySet()) {
			if (workplaceReportData != null) {
				break;
			}
			String key = entry.getKey();
			MonthlyWorkplaceData childWorkplace = entry.getValue();
			if (StringUtils.equalsIgnoreCase(key, hierarchyCode)) {
				workplaceReportData = childWorkplace;
				break;
			}
			workplaceReportData = findWorkplaceByHierarchyCodeExportDate(childWorkplace, hierarchyCode);
		}
		
		return workplaceReportData;
	}

	/**
	 * Analyze info (export by date).
	 *
	 * @param lstWorkplace the lst workplace
	 * @param parent the parent
	 * @param lstAddedWorkplace the lst added workplace
	 */
	private void analyzeInfoExportByDate(Map<String, WorkplaceInfo> lstWorkplace, MonthlyWorkplaceData parent, List<String> lstAddedWorkplace) {
		parent.lstChildWorkplaceData = new TreeMap<>();
		
		boolean isFoundParent;
		// Add workplace into respective parent
		for (Map.Entry<String, WorkplaceInfo> entry : lstWorkplace.entrySet()) {
			String k = entry.getKey();
			WorkplaceInfo wpi = entry.getValue();
			
			isFoundParent = false;
			
			// Init an workplace report data
			MonthlyWorkplaceData wrp = new MonthlyWorkplaceData();
			wrp.workplaceCode = wpi.getWorkplaceCode().v();
			wrp.workplaceName = wpi.getWorkplaceName().v();
			wrp.level = k.length() / 3;
			
			// Break down code
			String breakDownCode;
			for (int level = k.length(); level >= 3; level -= 3) {
				breakDownCode = k.substring(0, level);
				
				// Find workplace based on breakdown code
				MonthlyWorkplaceData targetWorkplace = findWorkplaceByHierarchyCodeExportDate(parent, breakDownCode);
				
				// Find parent workplace, add into its child
				if (targetWorkplace != null) {
					wrp.parent = targetWorkplace;
					targetWorkplace.lstChildWorkplaceData.put(k, wrp);
					isFoundParent = true;
					break;
				}
			}

			// Not found, then add to root
			if (!isFoundParent) {
				wrp.parent = parent;
				parent.lstChildWorkplaceData.put(k, wrp);
			}
		}
	}

	/**
	 * Calculate total (export by employee).
	 *
	 * @param workplaceData the workplace data
	 * @param lstAttendanceId the lst attendance id
	 */
	public void calculateTotalExportByEmployee(WorkplaceReportData workplaceData, List<MonthlyAttendanceItemsDisplay> lstAttendanceId) {
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
				employeeData.getLstDetailedMonthlyPerformance().stream().forEach(detail -> {
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
	 * Calculate total (export by date).
	 *
	 * @param workplaceData the workplace data
	 */
	public void calculateTotalExportByDate(MonthlyWorkplaceData workplaceData) {
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
		
		List<MonthlyPersonalPerformanceData> performanceData = workplaceData.getLstDailyPersonalData();
		
		// Employee
		if (performanceData != null && performanceData.size() > 0) {
			for (MonthlyPersonalPerformanceData data: performanceData) {
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
							totalWorkplaceValue.setValue(String.valueOf((int) totalWorkplaceValue.value() + (int) actualValue.value()));
						}
						if (valueTypeEnum.isDoubleCountable()) {
							totalWorkplaceValue.setValue(String.valueOf((double) totalWorkplaceValue.value() + (double) actualValue.value()));
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
		Map<String, MonthlyWorkplaceData> lstReportData = workplaceData.getLstChildWorkplaceData();
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
	public void calculateGrossTotalByDate(MonthlyReportData dailyReportData) {
		List<WorkplaceMonthlyReportData> lstWorkplaceDailyReportData = dailyReportData.getLstMonthlyReportData();
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
	 * Write detail work schedule.
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
	public int writeDetailedWorkSchedule(MonthlyWorkScheduleQuery query, int currentRow, WorksheetCollection templateSheetCollection, Worksheet sheet, WorkplaceReportData workplaceReportData, 
			int dataRowCount, MonthlyWorkScheduleCondition condition, RowPageTracker rowPageTracker) throws Exception {
		Cells cells = sheet.getCells();
		
		WorkScheduleSettingTotalOutput totalOutput = condition.getTotalOutputSetting();
		List<EmployeeReportData> lstEmployeeReportData = workplaceReportData.getLstEmployeeReportData();
		
		// Root workplace won't have employee
		if (lstEmployeeReportData != null && lstEmployeeReportData.size() > 0) {
			Iterator<EmployeeReportData> iteratorEmployee = lstEmployeeReportData.iterator();
			while (iteratorEmployee.hasNext()) {
				EmployeeReportData employeeReportData = iteratorEmployee.next();
				
				// Calculate used row for workplace, employee, 1st record
				int usedRow = 0;
				//count month for get data
//				int countPeriodMonth = 12*(query.getEndYearMonth().year() - query.getStartYearMonth().year()) + query.getEndYearMonth().month() - query.getStartYearMonth().month() + 1;
				if (condition.isShowWorkplace()) usedRow++;
				if (condition.isShowPersonal())  usedRow++;
				if (totalOutput.isDetails() && !employeeReportData.getLstDetailedMonthlyPerformance().isEmpty()) {
					int countItem = employeeReportData.countItem();
					usedRow += (countItem % CHUNK_SIZE) != 0 ? countItem / CHUNK_SIZE + 1 : countItem / CHUNK_SIZE;
				}
				if (rowPageTracker.checkRemainingRowSufficient(usedRow) <= 0) {
					sheet.getHorizontalPageBreaks().add(currentRow);
					rowPageTracker.resetRemainingRow();
				}
				
				if (condition.isShowWorkplace()) {
					Range workplaceRangeTemp = templateSheetCollection.getRangeByName(WorkScheOutputConstants.RANGE_WORKPLACE_ROW);
					Range workplaceRange = cells.createRange(currentRow, 0, 1, DATA_COLUMN_INDEX[5]);
					workplaceRange.copy(workplaceRangeTemp);
					rowPageTracker.useOneRowAndCheckResetRemainingRow(sheet, currentRow);
					// A3_1
					Cell workplaceTagCell = cells.get(currentRow, 0);
					workplaceTagCell.setValue(WorkScheOutputConstants.WORKPLACE + "　"
							+ workplaceReportData.getWorkplaceCode() + " " + workplaceReportData.getWorkplaceName());
					currentRow++;
				} 
				
				// A3_2
//				Cell workplaceInfo = cells.get(currentRow, DATA_COLUMN_INDEX[0]);
//				workplaceInfo.setValue(workplaceReportData.getWorkplaceCode() + " " + workplaceReportData.getWorkplaceName());
				
				if (condition.isShowPersonal()) {
					Range employeeRangeTemp = templateSheetCollection.getRangeByName(WorkScheOutputConstants.RANGE_EMPLOYEE_ROW);
					Range employeeRange = cells.createRange(currentRow, 0, 1, DATA_COLUMN_INDEX[5]);
					employeeRange.copy(employeeRangeTemp);
					rowPageTracker.useOneRowAndCheckResetRemainingRow(sheet, currentRow);
					// A4_1
					Cell employeeTagCell = cells.get(currentRow, 0);
					employeeTagCell.setValue(WorkScheOutputConstants.EMPLOYEE + "　" + employeeReportData.employeeCode
							+ "　" + employeeReportData.employeeName + "　" + WorkScheOutputConstants.EMPLOYMENT + "　"
							+ employeeReportData.employmentName + "　" + WorkScheOutputConstants.POSITION + "　"
							+ employeeReportData.position);
					currentRow++;
				} 
				
				// A4_2
//				Cell employeeCell = cells.get(currentRow, DATA_COLUMN_INDEX[0]);
//				employeeCell.setValue(employeeReportData.employeeCode + " " + employeeReportData.employeeName);
//				
//				// A4_3
//				Cell employmentTagCell = cells.get(currentRow, DATA_COLUMN_INDEX[1]);
//				employmentTagCell.setValue(WorkScheOutputConstants.EMPLOYMENT);
//				
//				// A4_5
//				Cell employmentCell = cells.get(currentRow, DATA_COLUMN_INDEX[2]);
//				employmentCell.setValue(employeeReportData.employmentName);
//				
//				// A4_6
//				Cell jobTitleTagCell = cells.get(currentRow, DATA_COLUMN_INDEX[3]);
//				jobTitleTagCell.setValue(WorkScheOutputConstants.POSITION);
//
//				// A4_7
//				Cell jobTitleCell = cells.get(currentRow, DATA_COLUMN_INDEX[4]);
//				jobTitleCell.setValue(employeeReportData.position);
				
				boolean colorWhite = true; // true = white, false = light blue, start with white row
				
				// Detail personal performance
				if (totalOutput.isDetails()) {
					List<DetailedMonthlyPerformanceReportData> lstDetailedDailyPerformance = employeeReportData.getLstDetailedMonthlyPerformance();
					for (DetailedMonthlyPerformanceReportData detailedDailyPerformanceReportData: lstDetailedDailyPerformance) {
						Range dateRangeTemp;
						
						if (rowPageTracker.checkRemainingRowSufficient(dataRowCount) < 0) {
							sheet.getHorizontalPageBreaks().add(currentRow);
							rowPageTracker.resetRemainingRow();
						}
						
						// A5_1
						Cell dateCell = cells.get(currentRow, 0);
						YearMonth yearMonth = detailedDailyPerformanceReportData.getYearMonth();
						String yearMonthStr = yearMonth.year() + "/" + (yearMonth.month() < 10? "0" + yearMonth.month() : yearMonth.month());
						Cell prevYearMonthCell = cells.get(currentRow - dataRowCount, 0);
						if (prevYearMonthCell.getValue() != null && yearMonthStr.equals(prevYearMonthCell.getValue())) {
							colorWhite = !colorWhite;
						}
						
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
						
						if (prevYearMonthCell.getValue() != null && yearMonthStr.equals(prevYearMonthCell.getValue())) {
							Range yearMonthRange = cells.createRange(currentRow, 0, dataRowCount, 2);
							yearMonthRange.setOutlineBorder(BorderType.TOP_BORDER, CellBorderType.NONE, Color.getBlack());
						}
						else {
							dateCell.setValue(yearMonthStr);
						}
						
						Range dateRange2 = cells.createRange(currentRow, 0, dataRowCount, 2);
						dateRange2.merge();
						
						// A5_2
						List<ActualValue> lstItem = detailedDailyPerformanceReportData.getActualValue();
						
						// A5_4
						String closureDate = detailedDailyPerformanceReportData.getClosureDate();
						Cell closureCell = cells.get(currentRow, 2);
						closureCell.putValue(closureDate);
						
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
				            	Style style = cell.getStyle();
				            	ValueType valueTypeEnum = EnumAdaptor.valueOf(actualValue.getValueType(), ValueType.class);
				            	String value = actualValue.getValue();
				            	if (valueTypeEnum.isTime()) {
									if (value != null) {
//										if (valueTypeEnum == ValueType.TIME) {
//											cell.setValue(getTimeAttr(value, false));
//										} else {
//											cell.setValue(getTimeAttr(value, true));
//										}
										cell.setValue(getTimeAttr(value, false));
									}
									else{
										cell.setValue(getTimeAttr("0", true));
									}	
									style.setHorizontalAlignment(TextAlignmentType.RIGHT);
				            	}
				            	else if (valueTypeEnum.isDouble() || valueTypeEnum.isInteger()) {
				            		cell.putValue(value, true);
									style.setHorizontalAlignment(TextAlignmentType.RIGHT);
								}
				            	else {
				            		cell.setValue(value);
									style.setHorizontalAlignment(TextAlignmentType.LEFT);
				            	}
				            	setFontStyle(style);
				            	cell.setStyle(style);
				            }
				            
				            curRow++;
				        }
				        
				        // A5_5
				        Cell remarkCell = cells.get(currentRow, DATA_COLUMN_INDEX[5]-REMARK_CELL_WIDTH);
				        String errorDetail = detailedDailyPerformanceReportData.getErrorDetail();
				        
				        int numOfChunksRemark = (int)Math.ceil((double)errorDetail.length() / 10);
						int curRowRemark = currentRow;
						String remarkContentRow;
						
						int maxPossibleRow = Math.min(numOfChunksRemark, dataRowCount);
						
			        	for(int i = 0; i < maxPossibleRow; i++) {
				            start = i * 10;
				            length = Math.min(errorDetail.length() - start, 10);
				            
				            remarkContentRow = errorDetail.substring(start, start + length);
				            
				            for (int j = 0; j < length; j++) {
				            	// Column 4, 6, 8,...
				            	remarkCell = cells.get(curRowRemark, DATA_COLUMN_INDEX[5]-REMARK_CELL_WIDTH); 
				            	Style style = remarkCell.getStyle();
				            	remarkCell.setValue(remarkContentRow);
								style.setHorizontalAlignment(TextAlignmentType.LEFT);
				            	setFontStyle(style);
				            	remarkCell.setStyle(style);
				            }
				            
				            curRowRemark++;
				        }
				        
				        //remarkCell.setValue(detailedDailyPerformanceReportData.getErrorDetail());
				        
				        currentRow += dataRowCount;
				        colorWhite = !colorWhite; // Change to other color
					}
				}
				
				// Personal total
				if (totalOutput.isPersonalTotal()) {
					if (rowPageTracker.checkRemainingRowSufficient(dataRowCount) < 0) {
						sheet.getHorizontalPageBreaks().add(currentRow);
						rowPageTracker.resetRemainingRow();
					}
					
					Range personalTotalRangeTemp = templateSheetCollection.getRangeByName(WorkScheOutputConstants.RANGE_TOTAL_ROW + dataRowCount);
					Range personalTotalRange = cells.createRange(currentRow, 0, dataRowCount, DATA_COLUMN_INDEX[5]);
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
			            int valueType;
			            
			            for (int j = 0; j < length; j++) {
			            	TotalValue totalValue = lstItemRow.get(j);
			            	String value = totalValue.getValue();
			            	valueType = totalValue.getValueType();

			            	Cell cell = cells.get(currentRow, DATA_COLUMN_INDEX[0] + j * 2); 
			            	Style style = cell.getStyle();
			            	ValueType valueTypeEnum = EnumAdaptor.valueOf(valueType, ValueType.class);
			            	if (valueTypeEnum.isIntegerCountable()) {
			            		if ((valueTypeEnum == ValueType.COUNT || valueTypeEnum == ValueType.AMOUNT) && value != null) {
			            			cell.putValue(value, true);
			            		}
			            		else {
			            			if (value != null)
										cell.setValue(getTimeAttr(value,false));
									else
										cell.setValue(getTimeAttr("0",false));
			            		}
								style.setHorizontalAlignment(TextAlignmentType.RIGHT);
							}
			            	else if (valueTypeEnum.isDoubleCountable() && value != null) {
			            		cell.putValue(value, true);
			            	}
			            	if (valueTypeEnum.isDouble() || valueTypeEnum.isInteger()){
			            		style.setHorizontalAlignment(TextAlignmentType.RIGHT);
			            	}
			            	setFontStyle(style);
			            	cell.setStyle(style);
			            }
			            currentRow++;
			        }
				}
		        
		        // Page break by employee
				if (condition.getPageBreakIndicator() == MonthlyWorkScheduleCondition.PAGE_BREAK_EMPLOYEE && iteratorEmployee.hasNext()) {
					rowPageTracker.resetRemainingRow();
					sheet.getHorizontalPageBreaks().add(currentRow);
				}
			}
		}
		
		// Skip writing total for root, use gross total instead
		if (lstEmployeeReportData != null && lstEmployeeReportData.size() > 0 && totalOutput.isWorkplaceTotal()) {
			if (rowPageTracker.checkRemainingRowSufficient(dataRowCount) < 0) {
				rowPageTracker.resetRemainingRow();
				sheet.getHorizontalPageBreaks().add(currentRow);
			}
			
			Range workplaceTotalRangeTemp = templateSheetCollection.getRangeByName(WorkScheOutputConstants.RANGE_TOTAL_ROW + dataRowCount);
			Range workplaceTotalRange = cells.createRange(currentRow, 0, dataRowCount, DATA_COLUMN_INDEX[5]);
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
	            	String value = totalValue.getValue();
	            	Cell cell = cells.get(currentRow, DATA_COLUMN_INDEX[0] + j * 2); 
	            	Style style = cell.getStyle();
	            	ValueType valueTypeEnum = EnumAdaptor.valueOf(totalValue.getValueType(), ValueType.class);
	            	if (valueTypeEnum.isIntegerCountable()) {
	            		if ((valueTypeEnum == ValueType.COUNT || valueTypeEnum == ValueType.AMOUNT) && value != null) {
	            			cell.putValue(value, true);
	            		}
	            		else {
	            			if (value != null)
								cell.setValue(getTimeAttr(value,false));
							else
								cell.setValue(getTimeAttr("0",false));
	            		}
						style.setHorizontalAlignment(TextAlignmentType.RIGHT);
					}
	            	else if (valueTypeEnum.isDoubleCountable() && value != null) {
	            		cell.putValue(value, true);
	            	}
	            	if (valueTypeEnum.isDouble() || valueTypeEnum.isInteger()) {
						style.setHorizontalAlignment(TextAlignmentType.RIGHT);
					}
	            	setFontStyle(style);
	            	cell.setStyle(style);
	            }
	            currentRow++;
	        }
		}
		
		Map<String, WorkplaceReportData> mapChildWorkplace = workplaceReportData.getLstChildWorkplaceReportData();
		// Check if any child workplace has data, page break when child workplace also has data (need to recursive into child workplace)
		int childHasDataCount = (int) mapChildWorkplace.values().stream().map(child -> child.isHasData()).filter(child -> child).count();
		if (((condition.getPageBreakIndicator() == MonthlyWorkScheduleCondition.PAGE_BREAK_WORKPLACE || 
				condition.getPageBreakIndicator() == MonthlyWorkScheduleCondition.PAGE_BREAK_EMPLOYEE) &&
				mapChildWorkplace.size() > 0) && workplaceReportData.level != 0 && childHasDataCount > 0) {
			Range lastRowRange = cells.createRange(currentRow - 1, 0, 1, DATA_COLUMN_INDEX[5]);
        	lastRowRange.setOutlineBorder(BorderType.BOTTOM_BORDER, CellBorderType.THIN, Color.getBlack());
        	rowPageTracker.resetRemainingRow();
			sheet.getHorizontalPageBreaks().add(currentRow);
		}
		// Process to child workplace
		Iterator<Map.Entry<String, WorkplaceReportData>> it = mapChildWorkplace.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, WorkplaceReportData> entry = it.next();
			
			// Remove page break when current workplace has no data
			for (int pageBreakIndex = 0; pageBreakIndex < sheet.getHorizontalPageBreaks().getCount(); pageBreakIndex++) {
				int ridx = sheet.getHorizontalPageBreaks().get(pageBreakIndex).getRow();
				if (ridx == currentRow && !entry.getValue().isHasData()) {
					sheet.getHorizontalPageBreaks().removeAt(pageBreakIndex);
					break;
				}
			}
			
			currentRow = writeDetailedWorkSchedule(query, currentRow, templateSheetCollection, sheet, entry.getValue(), dataRowCount, condition, rowPageTracker);
			
			// Page break by workplace
			if ((condition.getPageBreakIndicator() == MonthlyWorkScheduleCondition.PAGE_BREAK_WORKPLACE || condition.getPageBreakIndicator() == MonthlyWorkScheduleCondition.PAGE_BREAK_EMPLOYEE)
					&& it.hasNext()) {
				rowPageTracker.resetRemainingRow();
				sheet.getHorizontalPageBreaks().add(currentRow);
			}
		}
		
		/**
		 * A9 - A13
		 */
		TotalWorkplaceHierachy totalHierarchyOption = totalOutput.getWorkplaceHierarchyTotal();
		if (totalOutput.isGrossTotal() || totalOutput.isCumulativeWorkplace()) {
			
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
						if (rowPageTracker.checkRemainingRowSufficient(dataRowCount) < 0) {
							sheet.getHorizontalPageBreaks().add(currentRow);
							rowPageTracker.resetRemainingRow();
						}
						
						Range wkpHierTotalRangeTemp = templateSheetCollection.getRangeByName(WorkScheOutputConstants.RANGE_TOTAL_ROW + dataRowCount);
						Range wkpHierTotalRange = cells.createRange(currentRow, 0, dataRowCount, DATA_COLUMN_INDEX[5]);
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
				            	String value = totalValue.getValue();
				            	Cell cell = cells.get(currentRow + i, DATA_COLUMN_INDEX[0] + j * 2); 
				            	Style style = cell.getStyle();
				            	ValueType valueTypeEnum = EnumAdaptor.valueOf(totalValue.getValueType(), ValueType.class);
				            	if (valueTypeEnum.isIntegerCountable()) {
				            		if ((valueTypeEnum == ValueType.COUNT || valueTypeEnum == ValueType.AMOUNT) && value != null) {
				            			cell.putValue(value, true);
				            		}
				            		else {
				            			if (value != null)
											cell.setValue(getTimeAttr(value,false));
										else
											cell.setValue(getTimeAttr("0",false));
				            		}
									style.setHorizontalAlignment(TextAlignmentType.RIGHT);
								}
				            	else if (valueTypeEnum.isDoubleCountable() && value != null) {
				            		cell.putValue(value, true);
				            	}
				            	if (valueTypeEnum.isDouble() || valueTypeEnum.isInteger()) {
									style.setHorizontalAlignment(TextAlignmentType.RIGHT);
								}
				            	setFontStyle(style);
				            	cell.setStyle(style);
				            }
				        }
				        currentRow += dataRowCount;
					} else {
						if (levelIterator != null && levelIterator.hasNext()) {
							levelIterator.next();
						}
					}
				} while (levelIterator != null && levelIterator.hasNext());
			}
		}
		
		return currentRow;
	}
	
	/**
	 * Find detailed data.
	 *
	 * @param workplaceData the workplace data
	 * @return true, if successful
	 */
	private boolean findDetailedData(MonthlyWorkplaceData workplaceData) {
		List<MonthlyPersonalPerformanceData> lstMonthlyPersonalData = workplaceData.getLstDailyPersonalData();
		if (lstMonthlyPersonalData != null && !lstMonthlyPersonalData.isEmpty())
		for (MonthlyPersonalPerformanceData personalData : lstMonthlyPersonalData) {
			List<ActualValue> lstActualValue = personalData.getActualValue();
			if (lstActualValue != null && !lstActualValue.isEmpty())
				return true;
		}
		Map<String, MonthlyWorkplaceData> mapChildWorkplaceData = workplaceData.getLstChildWorkplaceData();
		for (Map.Entry<String, MonthlyWorkplaceData> entrySet: mapChildWorkplaceData.entrySet()) {
			MonthlyWorkplaceData childWorkplace = entrySet.getValue();
			if (findDetailedData(childWorkplace))
				return true;
		}
		return false;
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
	public int writeDetailedMonthlySchedule(int currentRow, WorksheetCollection templateSheetCollection, Worksheet sheet, MonthlyReportData dailyReport,
			int dataRowCount, MonthlyWorkScheduleCondition condition, RowPageTracker rowPageTracker) throws Exception {
		Cells cells = sheet.getCells();
		List<WorkplaceMonthlyReportData> lstMonthlyReportData = dailyReport.getLstMonthlyReportData();
		
		// Remove all monthly workplace data w/o any data inside it
		for (int i = lstMonthlyReportData.size() - 1; i >=0 ; i--) {
			WorkplaceMonthlyReportData monthlyReportData = lstMonthlyReportData.get(i);
			if (!findDetailedData(monthlyReportData.getLstWorkplaceData()))
				lstMonthlyReportData.remove(i);
		}
		
		Iterator<WorkplaceMonthlyReportData> iteratorWorkplaceData  = lstMonthlyReportData.iterator();
		
		while(iteratorWorkplaceData.hasNext()) {
			WorkplaceMonthlyReportData monthlyReportData = iteratorWorkplaceData.next();
			MonthlyWorkplaceData rootWorkplace = monthlyReportData.getLstWorkplaceData();
//			if (!findDetailedData(rootWorkplace))
//				continue;
			Range dateRangeTemp = templateSheetCollection.getRangeByName(WorkScheOutputConstants.RANGE_DATE_ROW);
			Range dateRange = cells.createRange(currentRow, 0, 1, DATA_COLUMN_INDEX[5]);
			dateRange.copy(dateRangeTemp);
			rowPageTracker.useOneRowAndCheckResetRemainingRow(sheet, currentRow);
			//dateRange.setOutlineBorder(BorderType.TOP_BORDER, CellBorderType.THIN, Color.getBlack());
			
			// B3_1
			int month = monthlyReportData.getYearMonth().month();
			String date = monthlyReportData.getYearMonth().year() + "/" + (month < 10 ? "0" + month : month);
			Cell dateTagCell = cells.get(currentRow, 0);
			dateTagCell.setValue(WorkScheOutputConstants.DATE_BRACKET + "　" +date);
			
//			// B3_2
//			int month = monthlyReportData.getYearMonth().month();
//			String date = monthlyReportData.getYearMonth().year() + "/" + (month < 10 ? "0" + month : month);
//			Cell dateCell = cells.get(currentRow, 2);
//			dateCell.setValue(date);
			
			currentRow++;
			
			
			currentRow = writeDailyDetailedPerformanceDataOnWorkplace(currentRow, sheet, templateSheetCollection, rootWorkplace, dataRowCount, condition, rowPageTracker);
		
		}
		
		if (condition.getTotalOutputSetting().isGrossTotal()) {
			// Remove page break after the very last workplace, previous iterator should always generate a page break after every workplace
			int ridx = 0;
			for (int pageBreakIndex = 0; pageBreakIndex < sheet.getHorizontalPageBreaks().getCount(); pageBreakIndex++) {
				ridx = sheet.getHorizontalPageBreaks().get(pageBreakIndex).getRow();
				if (ridx == currentRow && condition.getPageBreakIndicator() != MonthlyWorkScheduleCondition.PAGE_BREAK_NOT_USE) {
					sheet.getHorizontalPageBreaks().removeAt(pageBreakIndex);
					break;
				}
			}
			
			// Gross total after all the rest of the data
			if (rowPageTracker.checkRemainingRowSufficient(dataRowCount) < 0) {
				sheet.getHorizontalPageBreaks().add(currentRow);
				rowPageTracker.resetRemainingRow();
			}
			Range wkpGrossTotalRangeTemp = templateSheetCollection.getRangeByName(WorkScheOutputConstants.RANGE_TOTAL_ROW + dataRowCount);
			Range wkpGrossTotalRange = cells.createRange(currentRow, 0, dataRowCount, DATA_COLUMN_INDEX[5]);
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
			
			currentRow = writeGrossTotal(currentRow, dailyReport.getListTotalValue(), sheet, dataRowCount, rowPageTracker, condition);
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
	private int writeDailyDetailedPerformanceDataOnWorkplace(int currentRow, Worksheet sheet, WorksheetCollection templateSheetCollection, MonthlyWorkplaceData rootWorkplace, int dataRowCount, MonthlyWorkScheduleCondition condition, RowPageTracker rowPageTracker) throws Exception {
		Cells cells = sheet.getCells();
		if (!findDetailedData(rootWorkplace))
			return currentRow;
		Range workplaceRangeTemp = templateSheetCollection.getRangeByName(WorkScheOutputConstants.RANGE_DATE_ROW);
		Range workplaceRange = cells.createRange(currentRow, 0, 1, DATA_COLUMN_INDEX[5]);
		workplaceRange.copy(workplaceRangeTemp);
		
		boolean colorWhite = true; // true = white, false = light blue, start with white row
		
		List<MonthlyPersonalPerformanceData> employeeReportData = rootWorkplace.getLstDailyPersonalData();
		if (employeeReportData != null && !employeeReportData.isEmpty()) {
			rowPageTracker.useOneRowAndCheckResetRemainingRow(sheet, currentRow);
			if (condition.getTotalOutputSetting().isDetails() || condition.getTotalOutputSetting().isWorkplaceTotal()
					|| condition.getTotalOutputSetting().isCumulativeWorkplace()) {
				// B4_1
				Cell workplaceTagCell = cells.get(currentRow, 0);
				workplaceTagCell.setValue(WorkScheOutputConstants.WORKPLACE + "　" + rootWorkplace.getWorkplaceCode()
						+ "　" + rootWorkplace.getWorkplaceName());
				currentRow++;
			}
//			// B4_2
//			Cell workplaceCell = cells.get(currentRow, 2);
//			workplaceCell.setValue(rootWorkplace.getWorkplaceCode() + " " + rootWorkplace.getWorkplaceName());
			
			Iterator<MonthlyPersonalPerformanceData> dataIterator = employeeReportData.iterator();
			
			// Employee data
			while (dataIterator.hasNext() && condition.getTotalOutputSetting().isDetails()){
				MonthlyPersonalPerformanceData employee = dataIterator.next();
				
				List<ActualValue> lstItem = employee.getActualValue();
				// Skip to next employee when there is no actual value
				if (lstItem == null || lstItem.isEmpty()) continue;
				
				if (rowPageTracker.checkRemainingRowSufficient(dataRowCount) < 0) {
					sheet.getHorizontalPageBreaks().add(currentRow);
					rowPageTracker.resetRemainingRow();
				}
				
				// B5_1
				Cell employeeCodeCell = cells.get(currentRow, 0);
				Cell employeeNameCell = cells.get(currentRow, 1);
				
				Cell prevEmployeeCell = cells.get(currentRow - dataRowCount, 0);
				if (prevEmployeeCell.getValue() != null && prevEmployeeCell.getValue().toString().equals(employee.getEmployeeName()) && condition.getPageBreakIndicator() == MonthlyWorkScheduleCondition.PAGE_BREAK_EMPLOYEE) {
					colorWhite = !colorWhite;
					
					// Loop until getting the correct row, then remove page break
					for (int pageBreakIndex = 0; ; pageBreakIndex++) {
						int ridx = sheet.getHorizontalPageBreaks().get(pageBreakIndex).getRow();
						if (ridx == currentRow) {
							sheet.getHorizontalPageBreaks().removeAt(pageBreakIndex);
							break;
						}
					}
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
				
				if (prevEmployeeCell.getValue() != null && prevEmployeeCell.getValue().toString().equals(employee.getEmployeeName())) {
					Range employeeRange = cells.createRange(currentRow, 0, dataRowCount, 2);
					employeeRange.setOutlineBorder(BorderType.TOP_BORDER, CellBorderType.NONE, Color.getBlack());
				}
				else {
					employeeCodeCell.setValue(employee.getEmployeeCode());
					employeeNameCell.setValue(employee.getEmployeeName());
				}
				
				// B5_3
				Cell closureDateCell = cells.get(currentRow, 2);
				String closureDate = employee.getClosureDate();
				closureDateCell.putValue(closureDate);
				
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
			            	Style style = cell.getStyle();
			            	ValueType valueTypeEnum = EnumAdaptor.valueOf(actualValue.getValueType(), ValueType.class);
			            	String value = actualValue.getValue();
			            	if (valueTypeEnum.isTime()) {
								if (value != null)
									cell.setValue(getTimeAttr(value,false));
								else
									cell.setValue(getTimeAttr("0",false));
								style.setHorizontalAlignment(TextAlignmentType.RIGHT);
			            	}
			            	else if (valueTypeEnum.isDouble() || valueTypeEnum.isInteger()) {
			            		cell.putValue(value, true);	
			            		style.setHorizontalAlignment(TextAlignmentType.RIGHT);
			            	}
			            	else {
								cell.setValue(value);
								style.setHorizontalAlignment(TextAlignmentType.LEFT);
							}
			            	setFontStyle(style);
			            	cell.setStyle(style);
			            }
			            
			            curRow++;
			        }
			        
			        // B5_4
			        Cell remarkCell = cells.get(currentRow, DATA_COLUMN_INDEX[5]);
			        String errorDetail = employee.getDetailedErrorData();
			        
			        int numOfChunksRemark = (int)Math.ceil((double)errorDetail.length() / 10);
					int curRowRemark = currentRow;
					String remarkContentRow;
					
					int maxPossibleRow = Math.min(numOfChunksRemark, dataRowCount);
					
		        	for(int i = 0; i < maxPossibleRow; i++) {
			            start = i * 10;
			            length = Math.min(errorDetail.length() - start, 10);

			            remarkContentRow = errorDetail.substring(start, start + length);
			            
			            for (int j = 0; j < length; j++) {
			            	// Column 4, 6, 8,...
			            	remarkCell = cells.get(curRowRemark, DATA_COLUMN_INDEX[5]-REMARK_CELL_WIDTH); 
			            	Style style = remarkCell.getStyle();
			            	remarkCell.setValue(remarkContentRow);
							style.setHorizontalAlignment(TextAlignmentType.LEFT);
			            	setFontStyle(style);
			            	remarkCell.setStyle(style);
			            }
			            
			            curRowRemark++;
			        }
			        //remarkCell.setValue(employee.getDetailedErrorData());
				}
		        currentRow += dataRowCount;
		        colorWhite = !colorWhite; // Change to other color
		        
		        // Only break when has next iterator
		        if (dataIterator.hasNext() && condition.getPageBreakIndicator() == MonthlyWorkScheduleCondition.PAGE_BREAK_EMPLOYEE) {
		        	// Thin border for last row of page
		        	Range lastRowRange = cells.createRange(currentRow - 1, 0, 1, DATA_COLUMN_INDEX[5]);
		        	lastRowRange.setOutlineBorder(BorderType.BOTTOM_BORDER, CellBorderType.THIN, Color.getBlack());
		        	
		        	// Page break by employee
					sheet.getHorizontalPageBreaks().add(currentRow);
					rowPageTracker.resetRemainingRow();
		        }
			}
		}
		
		// Workplace total, root workplace use gross total instead
		if (condition.getTotalOutputSetting().isWorkplaceTotal() && rootWorkplace.getLevel() != 0 && employeeReportData != null && !employeeReportData.isEmpty()) {
			if (rowPageTracker.checkRemainingRowSufficient(dataRowCount) < 0) {
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
		
		Map<String, MonthlyWorkplaceData> mapChildWorkplace = rootWorkplace.getLstChildWorkplaceData();
		// Check if any child workplace has data, page break when child workplace also has data (need to recursive into child workplace)
		int childHasDataCount = (int) mapChildWorkplace.values().stream().map(child -> child.isHasData()).filter(child -> child).count();
		if (((condition.getPageBreakIndicator() == MonthlyWorkScheduleCondition.PAGE_BREAK_WORKPLACE || 
				condition.getPageBreakIndicator() == MonthlyWorkScheduleCondition.PAGE_BREAK_EMPLOYEE) &&
				mapChildWorkplace.size() > 0) && rootWorkplace.level != 0 && childHasDataCount > 0
				&& employeeReportData != null && !employeeReportData.isEmpty()) {
			Range lastRowRange = cells.createRange(currentRow - 1, 0, 1, DATA_COLUMN_INDEX[5]);
        	lastRowRange.setOutlineBorder(BorderType.BOTTOM_BORDER, CellBorderType.THIN, Color.getBlack());
        	rowPageTracker.resetRemainingRow();
			sheet.getHorizontalPageBreaks().add(currentRow);
		}
		// Child workplace
		Iterator<Map.Entry<String, MonthlyWorkplaceData>> workplaceIterator = mapChildWorkplace.entrySet().iterator();
		while (workplaceIterator.hasNext()) {
			Map.Entry<String, MonthlyWorkplaceData> entry = workplaceIterator.next();
			currentRow = writeDailyDetailedPerformanceDataOnWorkplace(currentRow, sheet, templateSheetCollection, entry.getValue(), dataRowCount, condition, rowPageTracker);
			
			// Page break by workplace
			if ((condition.getPageBreakIndicator() == MonthlyWorkScheduleCondition.PAGE_BREAK_WORKPLACE || 
					condition.getPageBreakIndicator() == MonthlyWorkScheduleCondition.PAGE_BREAK_EMPLOYEE) && rootWorkplace.hasData 
					&& (workplaceIterator.hasNext() || !workplaceIterator.hasNext() && rootWorkplace.level != 0)) {
				Range lastRowRange = cells.createRange(currentRow - 1, 0, 1, DATA_COLUMN_INDEX[5]);
	        	lastRowRange.setOutlineBorder(BorderType.BOTTOM_BORDER, CellBorderType.THIN, Color.getBlack());
	        	rowPageTracker.resetRemainingRow();
				sheet.getHorizontalPageBreaks().add(currentRow);
			}
		}
		
		// Workplace hierarchy total
		int level = rootWorkplace.getLevel();
		TotalWorkplaceHierachy settingTotalHierarchy = condition.getTotalOutputSetting().getWorkplaceHierarchyTotal();
		List<Integer> lstBetweenLevel = findEnabledLevelBetweenWorkplaces(rootWorkplace, settingTotalHierarchy);
		Iterator<Integer> levelIterator = null;
		if (!lstBetweenLevel.isEmpty()) {
			levelIterator = lstBetweenLevel.iterator();
		}
		if (level != 0 && level >= settingTotalHierarchy.getHighestLevelEnabled() 
				&& condition.getTotalOutputSetting().isCumulativeWorkplace()) {
			do {
				if (levelIterator != null && levelIterator.hasNext()) 
					level = (int) levelIterator.next();
				if (!settingTotalHierarchy.checkLevelEnabled(level)) break;
				if (rowPageTracker.checkRemainingRowSufficient(dataRowCount) < 0) {
					sheet.getHorizontalPageBreaks().add(currentRow);
					rowPageTracker.resetRemainingRow();
				}
				Range wkpHierTotalRangeTemp = templateSheetCollection.getRangeByName(WorkScheOutputConstants.RANGE_TOTAL_ROW + dataRowCount);
				Range wkpHierTotalRange = cells.createRange(currentRow, 0, dataRowCount, DATA_COLUMN_INDEX[5]);
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
	 * Write workplace total for export type by date.
	 *
	 * @param currentRow the current row
	 * @param rootWorkplace the root workplace
	 * @param sheet the sheet
	 * @param dataRowCount the data row count
	 * @param totalType the total type
	 * @return the int
	 */
	private int writeWorkplaceTotal(int currentRow, MonthlyWorkplaceData rootWorkplace, Worksheet sheet, int dataRowCount, boolean totalType) {
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
			    	String value = totalValue.getValue();
		        	Cell cell = cells.get(currentRow, DATA_COLUMN_INDEX[0] + j * 2); 
		        	Style style = cell.getStyle();
		        	ValueType valueTypeEnum = EnumAdaptor.valueOf(totalValue.getValueType(), ValueType.class);
		        	if (valueTypeEnum.isIntegerCountable()) {
	            		if ((valueTypeEnum == ValueType.COUNT || valueTypeEnum == ValueType.AMOUNT) && value != null) {
	            			cell.putValue(value, true);
	            		}
	            		else {
	            			if (value != null)
								cell.setValue(getTimeAttr(value,false));
							else
								cell.setValue(getTimeAttr("0",false));
	            		}
						style.setHorizontalAlignment(TextAlignmentType.RIGHT);
					}
	            	else if (valueTypeEnum.isDoubleCountable() && value != null) {
	            		cell.putValue(value, true);
	            	}
					if (valueTypeEnum.isDouble() || valueTypeEnum.isInteger()) {
						style.setHorizontalAlignment(TextAlignmentType.RIGHT);
					}
	            	setFontStyle(style);
	            	cell.setStyle(style);
			    }
			    currentRow++;
			}
			
			if (numOfChunks == 0) currentRow++;
		}
		
		return currentRow;
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
	private int writeGrossTotal(int currentRow, List<TotalValue> lstGrossTotal, Worksheet sheet, int dataRowCount, RowPageTracker rowPageTracker, MonthlyWorkScheduleCondition condition) {
		
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
			    	String value = totalValue.getValue();
		        	Cell cell = cells.get(currentRow, DATA_COLUMN_INDEX[0] + j * 2); 
		        	Style style = cell.getStyle();
		        	ValueType valueTypeEnum = EnumAdaptor.valueOf(totalValue.getValueType(), ValueType.class);
		        	if (valueTypeEnum.isIntegerCountable()) {
	            		if ((valueTypeEnum == ValueType.COUNT || valueTypeEnum == ValueType.AMOUNT) && value != null) {
	            			cell.putValue(value, true);
	            		}
	            		else {
	            			if (value != null)
								cell.setValue(getTimeAttr(value,false));
							else
								cell.setValue(getTimeAttr("0",false));
	            		}
						style.setHorizontalAlignment(TextAlignmentType.RIGHT);
					}
	            	else if (valueTypeEnum.isDoubleCountable() && value != null) {
	            		cell.putValue(value, true);
	            	}
		        	if (valueTypeEnum.isDouble() || valueTypeEnum.isInteger()) {
						style.setHorizontalAlignment(TextAlignmentType.RIGHT);
					}
	            	setFontStyle(style);
	            	cell.setStyle(style);
			    }
			    currentRow++;
			}
			
			if (numOfChunks == 0) currentRow++;
		}
		
		return currentRow;
	}

	/**
	 * Find workplace higher enabled level.
	 *
	 * @param workplaceReportData the workplace report data
	 * @param hierarchy the hierarchy
	 * @return the int
	 */
	public int findWorkplaceHigherEnabledLevel(WorkplaceReportData workplaceReportData, TotalWorkplaceHierachy hierarchy) {
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
	 * Gets the time attr.
	 *
	 * @param rawValue the raw value
	 * @return the time attr
	 */
	public String getTimeAttr(String rawValue, boolean isConvertAttr) {
		int value = Integer.parseInt(rawValue);
		TimeDurationFormatExtend timeFormat = new TimeDurationFormatExtend(value);
		if (isConvertAttr && value != 0) {
			//AttendanceTimeOfExistMinus time = new AttendanceTimeOfExistMinus(value);
			return timeFormat.getFullText();
		}
		else {
			return timeFormat.getTimeText();
		}
	}

	/**
	 * Sets the font style.
	 *
	 * @param style the new font style
	 */
	private void setFontStyle(Style style) {
		Font font = style.getFont();
		font.setDoubleSize(FONT_SIZE);
		font.setName(FONT_FAMILY);
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
	private void writeHeaderData(MonthlyWorkScheduleQuery query, OutputItemMonthlyWorkSchedule outputItem, Worksheet sheet, MonthlyPerformanceReportData reportData, int dateRow) {
		// Company name
		PageSetup pageSetup = sheet.getPageSetup();
		pageSetup.setHeader(0, "&8&\"MS ゴシック\" " + reportData.getHeaderData().companyName);
		
		// Output item name
		pageSetup.setHeader(1, "&16&\"MS ゴシック\"" + outputItem.getItemName().v());
		
		// Set header date
		DateTimeFormatter fullDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/M/d  H:mm", Locale.JAPAN);
		pageSetup.setHeader(2, "&8&\"MS ゴシック\" " + LocalDateTime.now().format(fullDateTimeFormatter) + "\npage &P ");
		
		Cells cells = sheet.getCells();
		Cell periodCell = cells.get(dateRow,0);
		
		YearMonth startMonth = query.getStartYearMonth();
		YearMonth endMonth = query.getEndYearMonth();
		
		StringBuilder builder = new StringBuilder();
		builder.append(WorkScheOutputConstants.PERIOD);
		builder.append(" ");
		builder.append(startMonth.year() + "/" + startMonth.month());
		builder.append(" ～ ");
		builder.append(endMonth.year() + "/" + endMonth.month());
		
		periodCell.setValue(builder.toString());
	}
	
	/**
	 * Sets the fixed data.
	 *
	 * @param condition the condition
	 * @param outputMonthlySchedule the output monthly schedule
	 * @param sheet the sheet
	 * @param reportData the report data
	 * @param currentRow the current row
	 */
	private void setFixedData(MonthlyWorkScheduleCondition condition, OutputItemMonthlyWorkSchedule outputMonthlySchedule, Worksheet sheet, MonthlyPerformanceReportData reportData, int currentRow) {
		MonthlyPerformanceHeaderData headerData = reportData.getHeaderData();
		// Set fixed value
		Cells cells = sheet.getCells();
		
//		if (condition.getOutputType() == MonthlyWorkScheduleCondition.EXPORT_BY_EMPLOYEE) {
			// A2_1
			Cell dateCell = cells.get(currentRow, 0);
			dateCell.setValue(headerData.getFixedHeaderData().get(0));
			
			// A2_1
			Cell closureCell = cells.get(currentRow, 2);
			closureCell.setValue(headerData.getFixedHeaderData().get(1));
			
			// A2_4
			Cell remarkCell = cells.get(currentRow, 35);
			remarkCell.setValue(headerData.getFixedHeaderData().get(2));
//		}
//		else {
//			// B2_1
//			Cell erAlCell = cells.get(currentRow, 0);
//			erAlCell.setValue(headerData.getFixedHeaderData().get(0));
//			
//			// B2_4
//			Cell dateCell = cells.get(currentRow, 31);
//			dateCell.setValue(headerData.getFixedHeaderData().get(1));
//		}
	}
	
	/**
	 * Write display map.
	 *
	 * @param cells the cells
	 * @param reportData the report data
	 * @param currentRow the current row
	 * @param nSize the n size
	 */
	private void writeDisplayMap(Cells cells, MonthlyPerformanceReportData reportData, int currentRow, int nSize) {
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
            	
//            	cell = cells.get(currentRow + i*2 + 1, DATA_COLUMN_INDEX[0] + j * 2); 
//            	cell.setValue(outputItem.getItemCode());
            }
        }
	}
	
	/**
	 * Get all enabled level between child and its parent workplace.
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
	private List<Integer> findEnabledLevelBetweenWorkplaces(MonthlyWorkplaceData workplaceReportData, TotalWorkplaceHierachy hierarchy) {
		List<Integer> lstEnabledLevel = new ArrayList<>();
		
		if (workplaceReportData.getLevel() == 0)
			return lstEnabledLevel;
		
		MonthlyWorkplaceData parentWorkplace = workplaceReportData.getParent();
		for (int i = workplaceReportData.getLevel(); i > parentWorkplace.getLevel(); i--) {
			if (hierarchy.checkLevelEnabled(i))
				lstEnabledLevel.add(i);
		}
		return lstEnabledLevel;
	}
	
	/**
	 * Create print area of the entire report, important for exporting to PDF.
	 *
	 * @param currentRow the current row
	 * @param sheet the sheet
	 */
	private void createPrintArea(Worksheet sheet) {
		// Get the last column and row name
		Cells cells = sheet.getCells();
		int maxDataRow = cells.getMaxDataRow();
		Cell lastCell = cells.get(maxDataRow, 38);
		String lastCellName = lastCell.getName();
		
		PageSetup pageSetup = sheet.getPageSetup();
		pageSetup.setPrintArea("A1:" + lastCellName);
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
	
}