package nts.uk.file.at.app.export.attendancerecord;

import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.error.BundledBusinessException;
import nts.arc.layer.app.file.export.ExportService;
import nts.arc.layer.app.file.export.ExportServiceContext;
import nts.arc.task.data.TaskDataSetter;
import nts.arc.task.parallel.ManagedParallelWithContext;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.function.dom.attendancerecord.export.AttendanceRecordExport;
import nts.uk.ctx.at.function.dom.attendancerecord.export.AttendanceRecordExportRepository;
import nts.uk.ctx.at.function.dom.attendancerecord.export.setting.AttendanceRecordExportSetting;
import nts.uk.ctx.at.function.dom.attendancerecord.export.setting.AttendanceRecordExportSettingRepository;
import nts.uk.ctx.at.function.dom.attendancerecord.export.setting.NameUseAtr;
import nts.uk.ctx.at.function.dom.attendancerecord.item.CalculateAttendanceRecord;
import nts.uk.ctx.at.function.dom.attendancerecord.item.CalculateAttendanceRecordRepositoty;
import nts.uk.ctx.at.function.dom.attendancerecord.item.SingleAttendanceRecordRepository;
import nts.uk.ctx.at.function.dom.attendancetype.AttendanceType;
import nts.uk.ctx.at.function.dom.attendancetype.AttendanceTypeRepository;
import nts.uk.ctx.at.function.dom.attendancetype.ScreenUseAtr;
import nts.uk.ctx.at.record.app.service.attendanceitem.value.AttendanceItemValueService;
import nts.uk.ctx.at.record.app.service.attendanceitem.value.AttendanceItemValueService.AttendanceItemValueResult;
import nts.uk.ctx.at.record.app.service.attendanceitem.value.AttendanceItemValueService.MonthlyAttendanceItemValueResult;
import nts.uk.ctx.at.request.dom.application.common.adapter.workplace.WkpHistImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.workplace.WorkplaceAdapter;
import nts.uk.ctx.at.shared.app.service.workrule.closure.ClosureEmploymentService;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ItemValue;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureHistory;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.ctx.bs.company.dom.company.Company;
import nts.uk.ctx.bs.company.dom.company.CompanyRepository;
import nts.uk.ctx.bs.employee.dom.workplace.config.info.WorkplaceConfigInfo;
import nts.uk.ctx.bs.employee.dom.workplace.config.info.WorkplaceConfigInfoRepository;
import nts.uk.ctx.bs.employee.dom.workplace.config.info.WorkplaceHierarchy;
import nts.uk.file.at.app.export.attendancerecord.data.AttendanceRecordReportColumnData;
import nts.uk.file.at.app.export.attendancerecord.data.AttendanceRecordReportDailyData;
import nts.uk.file.at.app.export.attendancerecord.data.AttendanceRecordReportData;
import nts.uk.file.at.app.export.attendancerecord.data.AttendanceRecordReportEmployeeData;
import nts.uk.file.at.app.export.attendancerecord.data.AttendanceRecordReportWeeklyData;
import nts.uk.file.at.app.export.attendancerecord.data.AttendanceRecordReportWeeklySumaryData;
import nts.uk.query.pub.employee.EmployeeInformationExport;
import nts.uk.query.pub.employee.EmployeeInformationPub;
import nts.uk.query.pub.employee.EmployeeInformationQueryDto;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.com.time.calendar.period.YearMonthPeriod;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class AttendanceRecordExportService extends ExportService<AttendanceRecordRequest> {

	final static long UPPER_POSITION = 1;
	final static long LOWER_POSITION = 2;
	final static int PDF_MODE = 1;
	final static String ZERO = "0";

	@Inject
	private ClosureEmploymentService closureEmploymentService;

	@Inject
	private SingleAttendanceRecordRepository singleAttendanceRepo;

	@Inject
	private CalculateAttendanceRecordRepositoty calculateAttendanceRepo;

	@Inject
	private AttendanceItemValueService attendanceService;

	@Inject
	private AttendanceRecordExportSettingRepository attendanceRecExpSetRepo;

	@Inject
	private AttendanceRecordExportRepository attendanceRecExpRepo;

	@Inject
	private AttendanceRecordReportGenerator reportGenerator;

	@Inject
	private CompanyRepository companyRepo;

	@Inject
	private EmployeeInformationPub employeePub;

	@Inject
	private WorkplaceConfigInfoRepository wplConfigInfoRepo;

	@Inject
	private WorkTypeRepository workTypeRepo;

	@Inject
	private WorkTimeSettingRepository workTimeRepo;

	@Inject
	private AttendanceTypeRepository attendanceRepo;

	@Inject
	private WorkplaceAdapter workplaceAdapter;
	
	@Inject
	private ManagedParallelWithContext parallel;

	@Override
	protected void handle(ExportServiceContext<AttendanceRecordRequest> context) {

		// get Dto
		AttendanceRecordRequest request = context.getQuery();
		String companyId = AppContexts.user().companyId();
		Map<String, List<AttendanceRecordReportEmployeeData>> reportData = new LinkedHashMap<>();
		List<AttendanceRecordReportEmployeeData> attendanceRecRepEmpDataList = new ArrayList<AttendanceRecordReportEmployeeData>();
		BundledBusinessException exceptions = BundledBusinessException.newInstance();
		TaskDataSetter setter = context.getDataSetter();
		// Get layout info
		Optional<AttendanceRecordExportSetting> optionalAttendanceRecExpSet = attendanceRecExpSetRepo
				.getAttendanceRecExpSet(companyId, request.getLayout());

		List<Employee> unknownEmployeeList = new ArrayList<>();
		List<Employee> nullDataEmployeeList = new ArrayList<>();
		List<Employee> employeeListAfterSort = new ArrayList<>();
		List<Employee> distinctEmployeeListAfterSort = new ArrayList<>();
		// Get workType info
		List<WorkType> workTypeList = workTypeRepo.findByCompanyId(companyId);

		// Get workTime info
		List<WorkTimeSetting> workTimeList = workTimeRepo.findByCompanyId(companyId);

		List<String> wplIds = new ArrayList<>();
		String invidual = "";
		List<String> empIDs = request.getEmployeeList().stream().map(e -> e.getEmployeeId()).collect(Collectors.toList());
		
		GeneralDate endByClosure = GeneralDate.ymd(request.getEndDate().year(), request.getEndDate().month(), request.getEndDate().lastDateInMonth());
		GeneralDate startTime = request.getStartDate().addMonths(-1);
		GeneralDate startByClosure = GeneralDate.ymd(startTime.year(), startTime.month(), 1);
		
		DatePeriod period = new DatePeriod(startByClosure, endByClosure);
		List<WkpHistImport> wkps = workplaceAdapter.findWkpBySid(empIDs, startByClosure);
		// Get workplace history
		for (Employee e : request.getEmployeeList()) {
			WkpHistImport hist = wkps.stream().filter(w -> w.getEmployeeId().equals(e.getEmployeeId())).findFirst().orElse(null);
			if (hist == null) {
				unknownEmployeeList.add(e);
			} else {
				e.setWorkplaceId(hist.getWorkplaceId());
				e.setWorkplaceCode(hist.getWorkplaceCode());
				wplIds.add(hist.getWorkplaceId());
			}
		}

		if (!wplIds.isEmpty()) {

			// Get Workplace config info
			List<WorkplaceConfigInfo> wplConfigInfoList = wplConfigInfoRepo
					.findByWkpIdsAtTime(AppContexts.user().companyId(), GeneralDate.localDate(LocalDate.now()), wplIds);
			List<WorkplaceHierarchy> hierarchyList = new ArrayList<>();

			// Find heirarchy for each workplace
			wplIds.forEach(item -> {

				for (WorkplaceConfigInfo info : wplConfigInfoList) {
					for (WorkplaceHierarchy hiearachy : info.getLstWkpHierarchy()) {
						if (item.equals(hiearachy.getWorkplaceId()))
							hierarchyList.add(hiearachy);
					}
				}
			});

			// Sort by heirarchy - sort by workplace
			hierarchyList.sort(Comparator.comparing(WorkplaceHierarchy::getHierarchyCode));

			wplIds = hierarchyList.stream().map(item -> item.getWorkplaceId()).distinct().collect(Collectors.toList());

			List<Employee> employeeListSortByCD = request.getEmployeeList().stream()
					.sorted(Comparator.comparing(Employee::getEmployeeCode)).collect(Collectors.toList());
			// sort employee by heirarchy
			wplIds.forEach(id -> {
				for (Employee employee : employeeListSortByCD) {
					if (id.equals(employee.getWorkplaceId()))
						employeeListAfterSort.add(employee);
				}

			});
		}
		// unknown employee put at the end of list
		if (!unknownEmployeeList.isEmpty()) {
			employeeListAfterSort.addAll(unknownEmployeeList);
		}
		distinctEmployeeListAfterSort = employeeListAfterSort.stream().distinct().collect(Collectors.toList());

		List<Integer> attendanceItemList = new ArrayList<>();
		// get upper-daily-singleItem list
		List<Integer> singleIdUpper = this.singleAttendanceRepo.getIdSingleAttendanceRecordByPosition(companyId,
				request.getLayout(), UPPER_POSITION);
		attendanceItemList.addAll(singleIdUpper);
		// get upper-daily-calculateItem list

		List<CalculateAttendanceRecord> calculateUpperDaily = this.calculateAttendanceRepo
				.getIdCalculateAttendanceRecordDailyByPosition(companyId, request.getLayout(), UPPER_POSITION);

		// get lower-daily-singleItem list
		List<Integer> singleIdLower = this.singleAttendanceRepo.getIdSingleAttendanceRecordByPosition(companyId,
				request.getLayout(), LOWER_POSITION);

		attendanceItemList.addAll(singleIdLower);
		// get lower-daily-CalculateItem list

		List<CalculateAttendanceRecord> calculateLowerDaily = this.calculateAttendanceRepo
				.getIdCalculateAttendanceRecordDailyByPosition(companyId, request.getLayout(), LOWER_POSITION);

		// get upper-monthly-Item list
		List<CalculateAttendanceRecord> calculateUpperMonthly = this.calculateAttendanceRepo
				.getIdCalculateAttendanceRecordMonthlyByPosition(companyId, request.getLayout(), UPPER_POSITION);

		// get lower-monthly-Item list
		List<CalculateAttendanceRecord> calculateLowerMonthly = this.calculateAttendanceRepo
				.getIdCalculateAttendanceRecordMonthlyByPosition(companyId, request.getLayout(), LOWER_POSITION);

		List<ScreenUseAtr> screenUseAtrList = Arrays.asList(ScreenUseAtr.ATTENDANCE_TYPE_OF_DERVICETYPE,
				ScreenUseAtr.EMPLOYEE_BOOKING_HOURS);

		List<AttendanceType> attendanceTypeList = new ArrayList<>();
		List<EmployeeInformationExport> employeeInfoList = new ArrayList<>();
		Set<Integer> singleId = new HashSet<>();
		Set<Integer> monthlyId = new HashSet<>();

		// Get all Daily attendance item
		singleId.addAll(singleIdUpper);
		singleId.addAll(singleIdLower);
		Stream.concat(calculateUpperDaily.stream(), calculateLowerDaily.stream()).forEach(item -> {
			if (item.getAddedItem() != null)
				singleId.addAll(item.getAddedItem());
			if (item.getSubtractedItem() != null)
				singleId.addAll(item.getSubtractedItem());
		});
		
		// Get all Monthly Attendanceitem
		Stream.concat(calculateUpperMonthly.stream(), calculateLowerMonthly.stream()).forEach(item -> {
			if (item.getAddedItem() != null)
				monthlyId.addAll(item.getAddedItem());
			if (item.getSubtractedItem() != null)
				singleId.addAll(item.getSubtractedItem());
		});
		
		YearMonthPeriod periodMonthly = new YearMonthPeriod(request.getStartDate().yearMonth(), request.getEndDate().yearMonth());
		List<MonthlyAttendanceItemValueResult> monthlyValues = attendanceService.getMonthlyValueOf(empIDs, periodMonthly, monthlyId);
		
		List<AttendanceItemValueResult> dailyValues;
		{
			List<AttendanceItemValueResult> syncResults = Collections.synchronizedList(new ArrayList<>());
			this.parallel.forEach(empIDs, empId -> {
				syncResults.addAll(attendanceService.getValueOf(Arrays.asList(empId), period, singleId));
			});
			dailyValues = new ArrayList<>(syncResults);
		}

		for (Employee employee : distinctEmployeeListAfterSort) {

			// Number of real data
			Integer realDataOfEmployee = 0;

			// get Closure
			Optional<Closure> optionalClosure = closureEmploymentService.findClosureByEmployee(employee.getEmployeeId(),
					request.getEndDate());
			ClosureDate closureDate = new ClosureDate(1, false);
			if (optionalClosure.isPresent()) {
				Closure closure = optionalClosure.get();

				// get closure history
				List<ClosureHistory> closureHistory = closure.getClosureHistories();

				// find closure history
				for (ClosureHistory history : closureHistory) {
					if (history.getStartYearMonth().lessThanOrEqualTo(request.getEndDate().yearMonth())
							&& history.getEndYearMonth().greaterThanOrEqualTo(request.getEndDate().yearMonth())) {
						closureDate = history.getClosureDate();

					}
				}

				// check if closure is found

				if (closureDate.getClosureDay().v() != 0 || closureDate.getLastDayOfMonth()) {

					// Get start time - end time
					YearMonth startYearMonth = closureDate.getLastDayOfMonth() ? request.getStartDate().yearMonth()
							: request.getStartDate().yearMonth().previousMonth();
					YearMonth endYearMonth = closureDate.getLastDayOfMonth() ? request.getEndDate().yearMonth()
							: request.getEndDate().yearMonth().previousMonth();
					YearMonth yearMonth = startYearMonth;

					if (attendanceTypeList.isEmpty()) {
						attendanceTypeList.addAll(attendanceRepo.getItemByAtrandType(AppContexts.user().companyId(),
								screenUseAtrList, 1));
					}

					while (yearMonth.lessThanOrEqualTo(endYearMonth)) {

						Integer realData = 0;

						GeneralDate startDateByClosure;
						GeneralDate endDateByClosure;

						// Get start - end date to export
						if (closureDate.getLastDayOfMonth()) {
							startDateByClosure = GeneralDate.ymd(yearMonth.year(), yearMonth.month(), 1);
							endDateByClosure = GeneralDate.ymd(yearMonth.year(), yearMonth.month(),
									yearMonth.lastDateInMonth());
						} else {
							startDateByClosure = GeneralDate.ymd(yearMonth.year(), yearMonth.month(),
									closureDate.getClosureDay().v() + 1);
							endDateByClosure = GeneralDate.ymd(yearMonth.addMonths(1).year(),
									yearMonth.addMonths(1).month(), closureDate.getClosureDay().v());
						}

						// amount day in month
						int flag = 0;

						// List DailyData
						List<AttendanceRecordReportDailyData> dailyDataList = new ArrayList<>();

						// Weekly Data
						List<AttendanceRecordReportWeeklyData> weeklyDataList = new ArrayList<>();

						// Report by Month
						while (startDateByClosure.beforeOrEquals(endDateByClosure)) {
							flag++;
							List<AttendanceRecordResponse> upperDailyRespond = new ArrayList<>();
							List<AttendanceRecordResponse> lowerDailyRespond = new ArrayList<>();
							// return result upper-daily-singleItems
							AttendanceItemValueService.AttendanceItemValueResult valueSingleUpper = null;
							GeneralDate closureDateTemp = startDateByClosure;

							AttendanceItemValueResult itemValueResult = AttendanceItemValueResult.builder()
									.employeeId(null).workingDate(null).attendanceItems(new ArrayList<ItemValue>())
									.build();
							// Get all daily result in Date
							if (!singleIdUpper.isEmpty() || !singleIdLower.isEmpty()) {
								for (AttendanceItemValueResult item : dailyValues) {
									if (item.getWorkingDate().equals(startDateByClosure) && item.getEmployeeId().equals(employee.employeeId)) {
										itemValueResult = item;
										break;
									}
								}
							}

							// Fill in upper single item
							if (!singleIdUpper.isEmpty()) {
								valueSingleUpper = AttendanceItemValueResult.builder()
										.employeeId(employee.getEmployeeId()).workingDate(startDateByClosure)
										.attendanceItems(new ArrayList<ItemValue>()).build();
								for (Integer id : singleIdUpper) {
									ItemValue value = new ItemValue();
									for (ItemValue item : itemValueResult.getAttendanceItems()) {
										if (item.getItemId() == id) {
											if (item.getValue() != null && !ZERO.equals(item.getValue())
													&& !item.getValue().isEmpty()) {
												realData++;
											}
											value = item;
											break;
										}

									}
									valueSingleUpper.getAttendanceItems().add(value);
								}

							} else {
								valueSingleUpper = AttendanceItemValueResult.builder()
										.employeeId(employee.getEmployeeId()).workingDate(startDateByClosure)
										.attendanceItems(new ArrayList<ItemValue>()).build();
								for (int i = 1; i <= 6; i++) {
									valueSingleUpper.getAttendanceItems().add(new ItemValue());

								}
							}

							// convert data to show
							if (valueSingleUpper != null) {

								valueSingleUpper.getAttendanceItems().forEach(item -> {
									if (item != null)
										upperDailyRespond.add(new AttendanceRecordResponse(employee.getEmployeeId(),
												employee.getEmployeeName(), closureDateTemp, "",
												this.convertString(item, workTypeList, workTimeList, attendanceTypeList,
														optionalAttendanceRecExpSet.get().getNameUseAtr())));

								});
							}
							// return result upper-daily-calculateItems
							for (CalculateAttendanceRecord item : calculateUpperDaily) {

								AttendanceItemValueService.AttendanceItemValueResult addValueCalUpper = AttendanceItemValueResult
										.builder().employeeId(employee.getEmployeeId()).workingDate(startDateByClosure)
										.attendanceItems(new ArrayList<ItemValue>()).build();
								// Fill in upper calculate daily ADD item
								if (item.getAddedItem() != null && !item.getAddedItem().isEmpty())
									for (Integer id : item.getAddedItem()) {
										for (ItemValue e : itemValueResult.getAttendanceItems()) {
											if (e.getItemId() == id) {
												if (e.getValue() != null && !ZERO.equals(e.getValue())
														&& !e.getValue().isEmpty()) {
													realData++;
												}
												addValueCalUpper.getAttendanceItems().add(e);
												break;
											}

										}

									}

								AttendanceItemValueService.AttendanceItemValueResult subValueCalUpper = AttendanceItemValueResult
										.builder().employeeId(employee.getEmployeeId()).workingDate(startDateByClosure)
										.attendanceItems(new ArrayList<ItemValue>()).build();
								// Fill in upper calculate daily SUBTRACT item
								if (item.getSubtractedItem() != null && !item.getSubtractedItem().isEmpty())
									for (Integer id : item.getSubtractedItem()) {

										for (ItemValue e : itemValueResult.getAttendanceItems()) {
											if (e.getItemId() == id) {
												if (e.getValue() != null && !ZERO.equals(e.getValue())
														&& !e.getValue().isEmpty()) {
													realData++;
												}
												subValueCalUpper.getAttendanceItems().add(e);
												break;
											}

										}

									}

								// get result upper calculate
								String result = "";
								if (!addValueCalUpper.getAttendanceItems().isEmpty()
										|| !subValueCalUpper.getAttendanceItems().isEmpty()) {
									result = this.getSumCalculateAttendanceItem(addValueCalUpper.getAttendanceItems(),
											subValueCalUpper.getAttendanceItems());
								}
								upperDailyRespond.add(new AttendanceRecordResponse(employee.getEmployeeId(),
										employee.getEmployeeName(), closureDateTemp, "", result));

							}

							// return result lower-daily-singleItems
							AttendanceItemValueService.AttendanceItemValueResult valueSingleLower = AttendanceItemValueResult
									.builder().employeeId(employee.getEmployeeId()).workingDate(startDateByClosure)
									.attendanceItems(new ArrayList<ItemValue>()).build();
							// Fill in lower single item
							if (!singleIdLower.isEmpty()) {

								valueSingleLower = AttendanceItemValueResult.builder()
										.employeeId(employee.getEmployeeId()).workingDate(startDateByClosure)
										.attendanceItems(new ArrayList<ItemValue>()).build();
								for (Integer id : singleIdLower) {
									ItemValue value = new ItemValue();
									for (ItemValue item : itemValueResult.getAttendanceItems()) {
										if (item.getItemId() == id) {
											if (item.getValue() != null && !ZERO.equals(item.getValue())
													&& !item.getValue().isEmpty()) {
												realData++;
											}
											value = item;
											break;
										}

									}
									valueSingleLower.getAttendanceItems().add(value);
								}

							} else {
								valueSingleLower = AttendanceItemValueResult.builder()
										.employeeId(employee.getEmployeeId()).workingDate(startDateByClosure)
										.attendanceItems(new ArrayList<ItemValue>()).build();
								for (int i = 1; i <= 6; i++) {
									valueSingleLower.getAttendanceItems().add(new ItemValue());

								}
							}
							// convert data to show
							if (valueSingleLower != null)
								valueSingleLower.getAttendanceItems().forEach(item -> {
									if (item != null)
										lowerDailyRespond.add(new AttendanceRecordResponse(employee.getEmployeeId(),
												employee.getEmployeeName(), closureDateTemp, "",
												this.convertString(item, workTypeList, workTimeList, attendanceTypeList,
														optionalAttendanceRecExpSet.get().getNameUseAtr())));

								});

							for (CalculateAttendanceRecord item : calculateLowerDaily) {

								AttendanceItemValueService.AttendanceItemValueResult addValueCalUpper = AttendanceItemValueResult
										.builder().attendanceItems(new ArrayList<>()).build();
								// Fill in lower calculate daily ADD item
								if (item.getAddedItem() != null && !item.getAddedItem().isEmpty())
									for (Integer id : item.getAddedItem()) {
										for (ItemValue e : itemValueResult.getAttendanceItems()) {
											if (e.getItemId() == id) {
												if (e.getValue() != null && !ZERO.equals(e.getValue())
														&& !e.getValue().isEmpty()) {
													realData++;
												}
												addValueCalUpper.getAttendanceItems().add(e);
												break;
											}

										}
									}

								AttendanceItemValueService.AttendanceItemValueResult subValueCalUpper = AttendanceItemValueResult
										.builder().attendanceItems(new ArrayList<>()).build();
								// Fill in lower calculate daily SUBTRACT item
								if (item.getSubtractedItem() != null && !item.getSubtractedItem().isEmpty())
									for (Integer id : item.getSubtractedItem()) {
										for (ItemValue e : itemValueResult.getAttendanceItems()) {
											if (e.getItemId() == id) {
												if (e.getValue() != null && !ZERO.equals(e.getValue())
														&& !e.getValue().isEmpty()) {
													realData++;
												}
												subValueCalUpper.getAttendanceItems().add(e);
												break;
											}

										}

									}

								// get result lower calculate daily
								String result = "";
								if (!addValueCalUpper.getAttendanceItems().isEmpty()
										|| !subValueCalUpper.getAttendanceItems().isEmpty()) {
									result = this.getSumCalculateAttendanceItem(addValueCalUpper.getAttendanceItems(),
											subValueCalUpper.getAttendanceItems());
								}
								lowerDailyRespond.add(new AttendanceRecordResponse(employee.getEmployeeId(),
										employee.getEmployeeName(), closureDateTemp, "", result));
							}

							AttendanceRecordReportDailyData dailyData = new AttendanceRecordReportDailyData();
							// Set data daily
							dailyData.setDate(String.valueOf(startDateByClosure.day()));
							dailyData.setDayOfWeek(DayOfWeekJP
									.getValue(startDateByClosure.localDate().getDayOfWeek().toString()).japanese);
							AttendanceRecordReportColumnData[] columnDatasArray = new AttendanceRecordReportColumnData[9];
							int index = 0;
							for (AttendanceRecordResponse item : upperDailyRespond) {
								columnDatasArray[index] = new AttendanceRecordReportColumnData("", "");
								if (item.getValue() != null)
									columnDatasArray[index].setUper(item.getValue());
								index++;
							}
							index = 0;
							for (AttendanceRecordResponse item : lowerDailyRespond) {
								if (item.getValue() != null)
									columnDatasArray[index].setLower(item.getValue());
								index++;

							}
							List<AttendanceRecordReportColumnData> columnDatas = new ArrayList<>();
							for (int i = 0; i < columnDatasArray.length; i++) {
								if (columnDatasArray[i] == null)
									columnDatasArray[i] = new AttendanceRecordReportColumnData("", "");
								columnDatas.add(columnDatasArray[i]);
							}

							dailyData.setColumnDatas(columnDatas);
							dailyData.setSecondCol(flag <= 15 ? false : true);
							dailyDataList.add(dailyData);
							// Check end of week
							if (startDateByClosure.localDate().getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
								AttendanceRecordReportWeeklyData weeklyData = new AttendanceRecordReportWeeklyData();
								// Set weekly data
								weeklyData.setDailyDatas(dailyDataList);
								// Set total result in week
								AttendanceRecordReportWeeklySumaryData summaryWeeklyData = new AttendanceRecordReportWeeklySumaryData();
								summaryWeeklyData = this.getSumWeeklyValue(dailyDataList);

								weeklyData.setWeeklySumaryData(summaryWeeklyData);

								weeklyDataList.add(weeklyData);
								// empty daily data list
								dailyDataList = new ArrayList<>();

							}
							// Next day
							startDateByClosure = startDateByClosure.addDays(1);

						}
						// Day of the last week in month
						if (dailyDataList.size() > 0) {
							AttendanceRecordReportWeeklyData weeklyData = new AttendanceRecordReportWeeklyData();
							weeklyData.setDailyDatas(dailyDataList);
							AttendanceRecordReportWeeklySumaryData summaryWeeklyData = new AttendanceRecordReportWeeklySumaryData();
							summaryWeeklyData = this.getSumWeeklyValue(dailyDataList);

							weeklyData.setWeeklySumaryData(summaryWeeklyData);

							weeklyDataList.add(weeklyData);
							// empty daily data list
							dailyDataList = new ArrayList<>();
						}

						List<String> upperResult = new ArrayList<>();
						List<String> lowerResult = new ArrayList<>();
						AttendanceItemValueService.MonthlyAttendanceItemValueResult itemValueResult = MonthlyAttendanceItemValueResult
								.builder().attendanceItems(new ArrayList<>()).build();

						if (!calculateUpperMonthly.isEmpty() || !calculateLowerMonthly.isEmpty()) {

							// Get montnly result
							for (MonthlyAttendanceItemValueResult item : monthlyValues) {
								if (item.getYearMonth()
										.equals(closureDate.getLastDayOfMonth() ? yearMonth : yearMonth.addMonths(1))
										&& item.getClouseDate() == closureDate.getClosureDay().v()) {
									itemValueResult = item;
									break;
								}
							}
						}

						for (CalculateAttendanceRecord item : calculateUpperMonthly) {
							AttendanceItemValueService.MonthlyAttendanceItemValueResult monthlyUpperAddResult = MonthlyAttendanceItemValueResult
									.builder().attendanceItems(new ArrayList<>()).build();
							AttendanceItemValueService.MonthlyAttendanceItemValueResult monthlyUpperSubResult = MonthlyAttendanceItemValueResult
									.builder().attendanceItems(new ArrayList<>()).build();

							// Fill in upper calculate monthly ADD item
							if (item.getAddedItem() != null && !item.getAddedItem().isEmpty()) {
								for (Integer id : item.getAddedItem()) {
									for (ItemValue e : itemValueResult.getAttendanceItems()) {
										if (id == e.getItemId()) {
											if (e.getValue() != null && !ZERO.equals(e.getValue())
													&& !e.getValue().isEmpty()) {
												realData++;
											}
											monthlyUpperAddResult.getAttendanceItems().add(e);
											break;
										}

									}

								}
							}

							// Fill in upper calculate monthly SUBTRACT item
							if (item.getSubtractedItem() != null && !item.getSubtractedItem().isEmpty()) {
								for (Integer id : item.getSubtractedItem()) {
									for (ItemValue e : itemValueResult.getAttendanceItems()) {
										if (id == e.getItemId()) {
											if (e.getValue() != null && !ZERO.equals(e.getValue())
													&& !e.getValue().isEmpty()) {
												realData++;
											}
											monthlyUpperSubResult.getAttendanceItems().add(e);
											break;
										}

									}

								}
							}

							// Get upper monthly result
							String result = "";
							if (!monthlyUpperAddResult.getAttendanceItems().isEmpty()
									|| !monthlyUpperSubResult.getAttendanceItems().isEmpty()) {
								result = this.getSumCalculateAttendanceItem(monthlyUpperAddResult.getAttendanceItems(),
										monthlyUpperSubResult.getAttendanceItems());
							}
							upperResult.add(result);

						}

						for (CalculateAttendanceRecord item : calculateLowerMonthly) {
							AttendanceItemValueService.MonthlyAttendanceItemValueResult monthlyLowerAddResult = MonthlyAttendanceItemValueResult
									.builder().attendanceItems(new ArrayList<>()).build();

							AttendanceItemValueService.MonthlyAttendanceItemValueResult monthlyLowerSubResult = MonthlyAttendanceItemValueResult
									.builder().attendanceItems(new ArrayList<>()).build();
							// Fill in lower calculate monthly ADD item
							if (item.getAddedItem() != null && !item.getAddedItem().isEmpty()) {
								for (Integer id : item.getAddedItem()) {
									for (ItemValue e : itemValueResult.getAttendanceItems()) {
										if (id == e.getItemId()) {
											if (e.getValue() != null && !ZERO.equals(e.getValue())
													&& !e.getValue().isEmpty()) {
												realData++;
											}
											monthlyLowerAddResult.getAttendanceItems().add(e);
											break;
										}

									}

								}
							}

							// Fill in lower calculate monthly SUBTRACT item
							if (item.getSubtractedItem() != null && !item.getSubtractedItem().isEmpty()) {
								for (Integer id : item.getSubtractedItem()) {
									for (ItemValue e : itemValueResult.getAttendanceItems()) {
										if (id == e.getItemId()) {
											if (e.getValue() != null && !ZERO.equals(e.getValue())
													&& !e.getValue().isEmpty()) {
												realData++;
											}
											monthlyLowerSubResult.getAttendanceItems().add(e);
											break;
										}

									}

								}
							}

							// Get lower monthly result
							String result = new String("");
							if (!monthlyLowerAddResult.getAttendanceItems().isEmpty()
									|| !monthlyLowerSubResult.getAttendanceItems().isEmpty()) {
								result = this.getSumCalculateAttendanceItem(monthlyLowerAddResult.getAttendanceItems(),
										monthlyLowerSubResult.getAttendanceItems());
							}
							lowerResult.add(result);

						}

						// Convert to AttendanceRecordReportColumnData
						List<AttendanceRecordReportColumnData> employeeMonthlyData = new ArrayList<>();

						AttendanceRecordReportColumnData[] columnDataMonthlyArray = new AttendanceRecordReportColumnData[12];
						int index = 0;
						for (String item : upperResult) {
							columnDataMonthlyArray[index] = new AttendanceRecordReportColumnData("", "");
							if (item != null)
								columnDataMonthlyArray[index].setUper(item);
							index++;
						}
						index = 0;
						for (String item : lowerResult) {
							if (item != null) {
								if (columnDataMonthlyArray[index] == null) {
									columnDataMonthlyArray[index] = new AttendanceRecordReportColumnData("", "");
								}
								columnDataMonthlyArray[index].setLower(item);
							}
							index++;

						}

						for (int i = 0; i < columnDataMonthlyArray.length; i++) {
							if (columnDataMonthlyArray[i] == null)
								columnDataMonthlyArray[i] = new AttendanceRecordReportColumnData("", "");
							employeeMonthlyData.add(columnDataMonthlyArray[i]);
						}

						if (realData > 0) {
							// Get AttendanceRecordReportEmployeeData
							AttendanceRecordReportEmployeeData attendanceRecRepEmpData = new AttendanceRecordReportEmployeeData();

							attendanceRecRepEmpData.setEmployeeMonthlyData(employeeMonthlyData);
							attendanceRecRepEmpData.setWeeklyDatas(weeklyDataList);
							YearMonth yearMonthExport = closureDate.getLastDayOfMonth() ? yearMonth
									: yearMonth.addMonths(1);
							attendanceRecRepEmpData.setReportYearMonth(yearMonthExport.toString());

							/**
							 * Need information
							 * 
							 * The invidual. The workplace. The employment The
							 * title. The work type The year month
							 **/

							// build param

							// Get Employee information
							if(employeeInfoList.isEmpty()){
								EmployeeInformationQueryDto param = EmployeeInformationQueryDto.builder()
										.employeeIds(empIDs).referenceDate(endByClosure).toGetWorkplace(true)
										.toGetDepartment(false).toGetPosition(true).toGetEmployment(true)
										.toGetClassification(false).toGetEmploymentCls(true).build();
								employeeInfoList = employeePub.find(param);
							}
							EmployeeInformationExport result = employeeInfoList.stream()
																				.filter(e -> e.getEmployeeId().equals(employee.getEmployeeId()))
																				.findFirst().get();

							attendanceRecRepEmpData
									.setEmployment(result.getEmployment().getEmploymentName().toString());
							attendanceRecRepEmpData
									.setInvidual(employee.getEmployeeCode() + " " + employee.getEmployeeName());
							attendanceRecRepEmpData.setTitle(result.getPosition() == null ? ""
									: result.getPosition().getPositionName().toString());
							attendanceRecRepEmpData.setWorkplace(result.getWorkplace() == null ? ""
									: result.getWorkplace().getWorkplaceName().toString());
							attendanceRecRepEmpData.setWorkType(result.getEmploymentCls() == null ? ""
									: TextResource.localize(EnumAdaptor.valueOf(result.getEmploymentCls(),
											WorkingSystem.class).nameId));
							attendanceRecRepEmpData
									.setYearMonth(yearMonthExport.year() + "/" + yearMonthExport.month());
							attendanceRecRepEmpDataList.add(attendanceRecRepEmpData);
							realDataOfEmployee++;
						}
						// Next monthly
						yearMonth = yearMonth.addMonths(1);

					}

				} else {
					// If closure not found
					String info = "\n " + employee.employeeCode + " " + employee.employeeName;
					if (info.length() + invidual.length() > 164) {
						if (!invidual.contains("\n..."))
							invidual = invidual.concat("\n...");
					} else {
						invidual = invidual.concat(info);
					}

				}

			} else {

				// If closure is wrong
				String info = "\n " + employee.employeeCode + " " + employee.employeeName;
				if (info.length() + invidual.length() > 164) {
					if (!invidual.contains("\n..."))
						invidual = invidual.concat("\n...");
				} else {
					invidual = invidual.concat(info);
				}

			}

			if (realDataOfEmployee == 0) {
				nullDataEmployeeList.add(employee);
			}
			
			List<AttendanceRecordReportEmployeeData> attendanceRecRepEmpDataByMonthList = new ArrayList<>();
			for (AttendanceRecordReportEmployeeData item : attendanceRecRepEmpDataList) {

				if (this.getCodeFromInvidual(item.getInvidual()).equals(employee.getEmployeeCode().trim())) {
					attendanceRecRepEmpDataByMonthList.add(item);
				}

			}
			// Fill in export Data of employee
			reportData.put(employee.getEmployeeCode().trim(), attendanceRecRepEmpDataByMonthList);
		}

		// set invidual to client
		if (!invidual.isEmpty()) {
			setter.setData("invidual", invidual);
		}
		if (distinctEmployeeListAfterSort.size() <= nullDataEmployeeList.size()) {
			// If real data of employee isn't exist
			if (!invidual.isEmpty()) {
				exceptions.addMessage("Msg_1269", invidual);
			}
			exceptions.throwExceptions();

		} else {
			distinctEmployeeListAfterSort.removeAll(nullDataEmployeeList);

		}

		// get seal stamp
		List<String> sealStamp = attendanceRecExpSetRepo.getSealStamp(companyId, request.getLayout());

		// Get daily header info
		List<AttendanceRecordExport> dailyRecord = attendanceRecExpRepo.getAllAttendanceRecordExportDaily(companyId,
				request.getLayout());
		List<AttendanceRecordExport> dailyRecordTotal = new ArrayList<>();

		for (int i = 1; i <= 9; i++) {
			if (this.findIndexInList(i, dailyRecord) == null) {
				AttendanceRecordExport item = new AttendanceRecordExport();
				item.setLowerPosition(null);
				item.setUpperPosition(null);
				dailyRecordTotal.add(item);
			} else {
				dailyRecordTotal.add(this.findIndexInList(i, dailyRecord));
			}
		}

		// get monthly header info
		List<AttendanceRecordExport> monthlyRecord = attendanceRecExpRepo.getAllAttendanceRecordExportMonthly(companyId,
				request.getLayout());
		List<AttendanceRecordExport> monthlyRecordTotal = new ArrayList<>();
		for (int i = 1; i <= 12; i++) {
			if (this.findIndexInList(i, monthlyRecord) == null) {
				AttendanceRecordExport item = new AttendanceRecordExport();
				item.setLowerPosition(null);
				item.setUpperPosition(null);
				monthlyRecordTotal.add(item);
			} else {
				monthlyRecordTotal.add(this.findIndexInList(i, monthlyRecord));
			}
		}

		// get header
		List<AttendanceRecordReportColumnData> dailyHeader = new ArrayList<AttendanceRecordReportColumnData>();
		List<AttendanceRecordReportColumnData> monthlyHeader = new ArrayList<AttendanceRecordReportColumnData>();

		for (AttendanceRecordExport item : dailyRecordTotal) {

			String upperheader = "";
			String lowerheader = "";
			if (item.getUpperPosition() != null && item.getUpperPosition().isPresent())
				upperheader = item.getUpperPosition().get().getNameDisplay();
			if (item.getLowerPosition() != null && item.getLowerPosition().isPresent())
				lowerheader = item.getLowerPosition().get().getNameDisplay();
			AttendanceRecordReportColumnData temp = (new AttendanceRecordReportColumnData(upperheader, lowerheader));
			dailyHeader.add(temp);

		}

		for (AttendanceRecordExport item : monthlyRecordTotal) {

			String upperheader = "";
			String lowerheader = "";
			if (item.getUpperPosition() != null && item.getUpperPosition().isPresent())
				upperheader = item.getUpperPosition().get().getNameDisplay();
			if (item.getLowerPosition() != null && item.getLowerPosition().isPresent())
				lowerheader = item.getLowerPosition().get().getNameDisplay();
			monthlyHeader.add(new AttendanceRecordReportColumnData(upperheader, lowerheader));
		}

		// check error List
		if (!exceptions.cloneExceptions().isEmpty()) {
			throw exceptions;
		}

		// Get info is showed on template
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

		LocalDateTime presentDate = LocalDateTime.now();
		String exportDate = presentDate.format(format).toString();

		AttendanceRecordReportData recordReportData = new AttendanceRecordReportData();
		Optional<Company> optionalCompany = companyRepo.find(companyId);

		recordReportData.setCompanyName(optionalCompany.get().getCompanyName().toString());
		recordReportData.setDailyHeader(dailyHeader);
		recordReportData.setExportDateTime(exportDate);
		recordReportData.setMonthlyHeader(monthlyHeader);
		recordReportData.setReportData(reportData);
		recordReportData.setReportName(optionalAttendanceRecExpSet.get().getName().v());
		recordReportData.setSealColName(
				optionalAttendanceRecExpSet.get().getSealUseAtr() ? sealStamp : new ArrayList<String>());

		AttendanceRecordReportDatasource recordReportDataSource = new AttendanceRecordReportDatasource(recordReportData,
				request.getMode());

		// Generate file
		reportGenerator.generate(context.getGeneratorContext(), recordReportDataSource);

	}

	/**
	 * Gets the number from string.
	 *
	 * @param string
	 *            the string
	 * @return the number from string
	 */
	Integer getNumberFromString(String string) {

		String result = string.replaceAll("\\D+", "");
		if (!result.equals(""))
			return Integer.parseInt(result);
		return 0;
	}

	/**
	 * Gets the period from string.
	 *
	 * @param list
	 *            the list
	 * @return the period from string
	 */
	String getPeriodFromString(List<ItemValue> list) {
		for (ItemValue i : list) {
			if (i.value() != null) {
				String tmp = this.getNumberFromString(i.value().toString()).toString();
				int index = i.value().toString().indexOf(tmp);
				return i.value().toString().substring(index);
			}
		}
		return null;

	}

	/**
	 * Gets the name from invidual.
	 *
	 * @param invidual
	 *            the invidual
	 * @return the name from invidual
	 */
	String getNameFromInvidual(String invidual) {
		int index = invidual.indexOf(" ");
		return invidual.substring(index + 1).trim();
	}

	/**
	 * Gets the code from invidual.
	 *
	 * @param invidual
	 *            the invidual
	 * @return the code from invidual
	 */
	String getCodeFromInvidual(String invidual) {
		int index = invidual.indexOf(" ");
		return invidual.substring(0, index + 1).trim();
	}

	/**
	 * Gets the sum calculate attendance item.
	 *
	 * @param addValueCalUpper
	 *            the add value cal upper
	 * @param subValueCalUpper
	 *            the sub value cal upper
	 * @return the sum calculate attendance item
	 */
	String getSumCalculateAttendanceItem(List<ItemValue> addValueCalUpper, List<ItemValue> subValueCalUpper) {

		Double sum = new Double(0);
		if (!addValueCalUpper.isEmpty()
				&& (addValueCalUpper.get(0).getValueType().isInteger()
						|| addValueCalUpper.get(0).getValueType().isDouble())
				|| !subValueCalUpper.isEmpty() && (subValueCalUpper.get(0).getValueType().isInteger()
						|| subValueCalUpper.get(0).getValueType().isDouble())) {

			// calculate add
			if (!addValueCalUpper.isEmpty()) {
				for (ItemValue i : addValueCalUpper) {
					if (i.getValue() != null && !i.getValue().isEmpty())
						sum = Double.parseDouble(sum.toString()) + Double.parseDouble(i.value().toString());
				}
			}
			// calculate sub
			if (!subValueCalUpper.isEmpty()) {
				for (ItemValue i : subValueCalUpper) {
					if (i.getValue() != null && !i.getValue().isEmpty())
						sum = Double.parseDouble(sum.toString()) - Double.parseDouble(i.value().toString());
				}
			}
			if (sum.equals(0.0))
				return "";

			Integer sumInt;
			List<ItemValue> list = new ArrayList<>();
			if (!addValueCalUpper.isEmpty()) {
				list.addAll(addValueCalUpper);
			} else {
				list.addAll(subValueCalUpper);
			}
			switch (list.get(0).getValueType().value) {

			case 1:
			case 2:

				sumInt = sum.intValue();
				return this.convertMinutesToHours(sumInt.toString());
			case 7:
			case 8:
				sumInt = sum.intValue();
				return sumInt.toString() + " 回";
			case 13:
				sumInt = sum.intValue();
				DecimalFormat format = new DecimalFormat("###,###,###");
				return format.format(sum.intValue());
			default:
				break;

			}

		}
		return sum.toString();
	}

	/**
	 * Gets the sum weekly value.
	 *
	 * @param list
	 *            the list
	 * @return the sum weekly value
	 */
	public AttendanceRecordReportWeeklySumaryData getSumWeeklyValue(List<AttendanceRecordReportDailyData> list) {
		AttendanceRecordReportWeeklySumaryData result = new AttendanceRecordReportWeeklySumaryData();

		String upperValue7th = "";
		String lowerValue7th = "";
		String upperValue8th = "";
		String lowerValue8th = "";
		String upperValue9th = "";
		String lowerValue9th = "";

		for (AttendanceRecordReportDailyData item : list) {
			if (item.getColumnDatas().get(6) != null) {
				upperValue7th = this.add(upperValue7th, item.getColumnDatas().get(6).getUper());
				lowerValue7th = this.add(lowerValue7th, item.getColumnDatas().get(6).getLower());
			}
			if (item.getColumnDatas().get(7) != null) {
				upperValue8th = this.add(upperValue8th, item.getColumnDatas().get(7).getUper());
				lowerValue8th = this.add(lowerValue8th, item.getColumnDatas().get(7).getLower());
			}
			if (item.getColumnDatas().get(8) != null) {
				upperValue9th = this.add(upperValue9th, item.getColumnDatas().get(8).getUper());
				lowerValue9th = this.add(lowerValue9th, item.getColumnDatas().get(8).getLower());
			}

		}
		List<AttendanceRecordReportColumnData> columnDatas = new ArrayList<>();
		AttendanceRecordReportColumnData columnData7 = new AttendanceRecordReportColumnData(upperValue7th,
				lowerValue7th);
		columnDatas.add(columnData7);
		AttendanceRecordReportColumnData columnData8 = new AttendanceRecordReportColumnData(upperValue8th,
				lowerValue8th);
		columnDatas.add(columnData8);
		AttendanceRecordReportColumnData columnData9 = new AttendanceRecordReportColumnData(upperValue9th,
				lowerValue9th);
		columnDatas.add(columnData9);

		result.setDateRange(list.get(0).getDate() + "-" + list.get(list.size() - 1).getDate());
		result.setColumnDatas(columnDatas);
		result.setSecondCol(list.get(list.size() - 1).isSecondCol());

		return result;

	}

	/**
	 * Adds the.
	 *
	 * @param a
	 *            the a
	 * @param b
	 *            the b
	 * @return the string
	 */
	public String add(String a, String b) {
		if (a.equals(""))
			return b;
		if (b.equals(""))
			return a;
		int indexA = a.indexOf(":");
		int indexB = b.indexOf(":");

		int subtrA = a.indexOf("-");
		int subtrB = b.indexOf("-");

		if (indexA >= 0 && indexB >= 0) {
			Integer hourA;
			Integer hourB;
			Integer minuteA;
			Integer minuteB;
			hourA = Integer.parseInt(a.substring(0, indexA));
			if (subtrA == 0) {
				minuteA = Integer.parseInt(a.substring(indexA + 1)) * (-1);
			} else {
				minuteA = Integer.parseInt(a.substring(indexA + 1));
			}
			hourB = Integer.parseInt(b.substring(0, indexB));
			if (subtrB == 0) {
				minuteB = Integer.parseInt(b.substring(indexB + 1)) * (-1);
			} else {

				minuteB = Integer.parseInt(b.substring(indexB + 1));
			}

			Integer totalMinute = (hourA * 60 + minuteA) + (hourB * 60 + minuteB);

			return this.convertMinutesToHours(totalMinute.toString());
		} else {
			indexA = a.indexOf("回");
			indexB = b.indexOf("回");

			if (indexA >= 0 && indexB >= 0) {
				Integer countA = Integer.parseInt(a.substring(0, indexA - 1));
				Integer countB = Integer.parseInt(b.substring(0, indexB - 1));

				Integer totalCount = countA + countB;

				return totalCount + " 回";
			} else {
				String stringAmountA = a.replaceAll(",", "");
				String stringAmountB = b.replaceAll(",", "");

				Integer amountA = Integer.parseInt(stringAmountA);
				Integer amountB = Integer.parseInt(stringAmountB);

				Integer totalAmount = amountA + amountB;
				DecimalFormat format = new DecimalFormat("###,###,###");
				return format.format(totalAmount);

			}

		}

	}

	/**
	 * Convert string.
	 *
	 * @param item
	 *            the item
	 * @param workTypeList
	 *            the work type list
	 * @param workTimeSettingList
	 *            the work time setting list
	 * @param screenUseAtrList
	 *            the screen use atr list
	 * @param nameUseAtr
	 *            the name use atr
	 * @return the string
	 */
	private String convertString(ItemValue item, List<WorkType> workTypeList, List<WorkTimeSetting> workTimeSettingList,
			List<AttendanceType> attendanceTypeList, NameUseAtr nameUseAtr) {
		final String value = item.getValue();
		if (item.getValueType() == null || item.getValue() == null)
			return "";
		switch (item.getValueType()) {

		case TIME:
		case CLOCK:
		case TIME_WITH_DAY:

			if (Integer.parseInt(item.getValue()) == 0 || item.getValue().isEmpty())
				return "";
			return this.convertMinutesToHours(value.toString());
		case COUNT:
		case COUNT_WITH_DECIMAL:
			if (Integer.parseInt(item.getValue()) == 0 || item.getValue().isEmpty())
				return "";
			return value.toString() + " 回";
		case AMOUNT:
			if (Integer.parseInt(item.getValue()) == 0 || item.getValue().isEmpty())
				return "";
			DecimalFormat format = new DecimalFormat("###,###,###");
			return format.format(Integer.parseInt(value));

		case CODE:

			if (!attendanceTypeList.isEmpty()) {
				AttendanceType attendance = attendanceTypeList.stream()
						.filter(e -> e.getAttendanceItemId() == item.getItemId()).collect(Collectors.toList()).get(0);
				if (attendance.getScreenUseAtr().equals(ScreenUseAtr.ATTENDANCE_TYPE_OF_DERVICETYPE)) {
					List<WorkType> worktype = workTypeList.stream()
							.filter(ite -> ite.getWorkTypeCode().v().equals(value)).collect(Collectors.toList());
					if (!worktype.isEmpty())

						return nameUseAtr.equals(NameUseAtr.FORMAL_NAME) ? worktype.get(0).getName().v()
								: worktype.get(0).getAbbreviationName().v();
					return value;
				} else {

					List<WorkTimeSetting> workTime = workTimeSettingList.stream()
							.filter(e -> e.getWorktimeCode().v().equals(value)).collect(Collectors.toList());
					if (!workTime.isEmpty())
						return nameUseAtr.equals(NameUseAtr.FORMAL_NAME)
								? workTime.get(0).getWorkTimeDisplayName().getWorkTimeName().v()
								: workTime.get(0).getWorkTimeDisplayName().getWorkTimeAbName().v();
					return value;
				}
			}
			return value;

		default:
			return value;

		}
	}

	/**
	 * Find index in list.
	 *
	 * @param i
	 *            the i
	 * @param list
	 *            the list
	 * @return the attendance record export
	 */
	private AttendanceRecordExport findIndexInList(int i, List<AttendanceRecordExport> list) {
		for (AttendanceRecordExport item : list) {
			if (item.getColumnIndex() == i)
				return item;
		}

		return null;
	}

	/**
	 * Convert minutes to hours.
	 *
	 * @param minutes
	 *            the minutes
	 * @return the string
	 */
	private String convertMinutesToHours(String minutes) {
		if (minutes.equals("0") || minutes.equals("")) {
			return "0:00";
		}
		String FORMAT = "%d:%02d";
		Integer minuteInt = Integer.parseInt(minutes);
		if (minuteInt < 0) {
			minuteInt *= -1;
			Integer hourInt = minuteInt / 60;
			minuteInt = minuteInt % 60;
			return "-" + String.format(FORMAT, hourInt, minuteInt);
		} else {
			Integer hourInt = minuteInt / 60;
			minuteInt = minuteInt % 60;

			return String.format(FORMAT, hourInt, minuteInt);
		}
	}
}
