package nts.uk.screen.at.app.monthlyperformance.correction.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.task.AsyncTask;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.app.find.monthly.finder.MonthlyRecordWorkFinder;
import nts.uk.ctx.at.record.app.find.monthly.root.MonthlyRecordWorkDto;
import nts.uk.ctx.at.record.app.find.monthly.root.common.ClosureDateDto;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.EmpPerformMonthParamImport;
import nts.uk.ctx.at.record.dom.approvalmanagement.dailyperformance.algorithm.RegisterDayApproval;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.IntegrationOfMonthly;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.month.algorithm.ParamRegisterConfirmMonth;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.month.algorithm.RegisterConfirmationMonth;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.month.algorithm.SelfConfirm;
import nts.uk.ctx.at.shared.dom.attendance.util.AttendanceItemUtil;
import nts.uk.ctx.at.shared.dom.attendance.util.AttendanceItemUtil.AttendanceItemType;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ItemValue;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ValueType;
import nts.uk.screen.at.app.monthlyperformance.audittrail.MonthlyCorrectionLogCommand;
import nts.uk.screen.at.app.monthlyperformance.audittrail.MonthlyCorrectionLogCommandHandler;
import nts.uk.screen.at.app.monthlyperformance.correction.dto.EditStateOfMonthlyPerformanceDto;
import nts.uk.screen.at.app.monthlyperformance.correction.dto.MPItemCheckBox;
import nts.uk.screen.at.app.monthlyperformance.correction.dto.MPItemDetail;
import nts.uk.screen.at.app.monthlyperformance.correction.dto.MPItemParent;
import nts.uk.screen.at.app.monthlyperformance.correction.query.MonthlyModifyQuery;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
/**
 * 
 * @author sonnh1
 *
 */
@Stateless
public class MonModifyCommandFacade {
	
	@Inject
	private MonthModifyCommandFacade monthModifyCommandFacade;

	@Inject
	private MonthlyPerformanceCorrectionUpdateCommand monthlyPerformanceCorrectionUpdateCommand;
	
	@Inject
	private RegisterConfirmationMonth registerConfirmationMonth;
	
	@Inject
	private MonthlyCorrectionLogCommandHandler handlerLog;
	
	@Inject
	private MonthlyRecordWorkFinder finder;

	@Inject
	private RegisterDayApproval registerDayApproval;
	
	public Map<Integer, List<MPItemParent>> insertItemDomain(MPItemParent dataParent) {
		Map<String, List<MPItemDetail>> mapItemDetail = dataParent.getMPItemDetails().stream()
				.collect(Collectors.groupingBy(x -> x.getEmployeeId()));
		List<MonthlyModifyQuery> listQuery = new ArrayList<>();
		// insert value
		mapItemDetail.entrySet().forEach(item -> {
			List<MPItemDetail> rowDatas = item.getValue();
			listQuery.add(new MonthlyModifyQuery(rowDatas.stream().map(x -> {
				return ItemValue.builder().itemId(x.getItemId()).layout(x.getLayoutCode()).value(x.getValue())
						.valueType(ValueType.valueOf(x.getValueType())).withPath("");
			}).collect(Collectors.toList()), dataParent.getYearMonth(), item.getKey(), dataParent.getClosureId(),
					dataParent.getClosureDate()));
		});
		List<MonthlyRecordWorkDto> oldDtos = getDtoFromQuery(listQuery); // lay data truoc khi update de so sanh voi data sau khi update
		monthModifyCommandFacade.handleUpdate(listQuery,oldDtos);

		// insert edit state
		dataParent.getMPItemDetails().forEach(item -> {
			ClosureDateDto closureDate = dataParent.getClosureDate();
			EditStateOfMonthlyPerformanceDto editStateOfMonthlyPerformanceDto = new EditStateOfMonthlyPerformanceDto(
					item.getEmployeeId(), new Integer(item.getItemId()),
					new DatePeriod(dataParent.getStartDate(), dataParent.getEndDate()),
					dataParent.getYearMonth().intValue(), dataParent.getClosureId(),
					new nts.uk.screen.at.app.monthlyperformance.correction.dto.ClosureDateDto(
							closureDate.getClosureDay().intValue(),
							closureDate.getLastDayOfMonth().booleanValue() ? 1 : 0),
					new Integer(0));
			this.monthlyPerformanceCorrectionUpdateCommand.handleAddOrUpdate(editStateOfMonthlyPerformanceDto);
		});
		
		// dang ki xac nhan ban than
		this.insertSign(dataParent);
		
		List<EmpPerformMonthParamImport> listRegister = new ArrayList<>();
		List<EmpPerformMonthParamImport> listRemove = new ArrayList<>();
		for(MPItemCheckBox mpi :dataParent.getDataCheckApproval()) { //loc trang thai approve de dang ki
			EmpPerformMonthParamImport p = new EmpPerformMonthParamImport(new YearMonth(dataParent.getYearMonth()), dataParent.getClosureId(),
					dataParent.getClosureDate().toDomain(), dataParent.getEndDate(), mpi.getEmployeeId());
			if(mpi.isValue()) {
				listRegister.add(p);
			}else {
				listRemove.add(p);
			}
		}
		
		// remove approval	- no. 529	
		this.removeMonApproval(listRemove);
		// insert approval - no. 528
		this.insertApproval(listRegister);
		
		// add correction log
		ExecutorService executorService = Executors.newFixedThreadPool(1);
		AsyncTask task = AsyncTask.builder().withContexts().keepsTrack(false).threadName(this.getClass().getName())
				.build(() -> {
					List<MonthlyRecordWorkDto> newDtos = createDtoNews(listQuery, oldDtos);
					//List<MonthlyRecordWorkDto> newDtos = getDtoFromQuery(listQuery); // lay lai data sau khi update de so sanh voi data truoc khi update
					handlerLog.handle(new MonthlyCorrectionLogCommand(oldDtos, newDtos, listQuery, dataParent.getEndDate()));
				});
		executorService.submit(task);
		return Collections.emptyMap();
	}
	
	private void insertSign(MPItemParent mPItemParent) {
		List<MPItemCheckBox> dataCheckSign = mPItemParent.getDataCheckSign();
		if(dataCheckSign.isEmpty()) return;
		List<SelfConfirm> selfConfirm = new ArrayList<>();
		ClosureDateDto closureDate = mPItemParent.getClosureDate();
		YearMonth ym = new YearMonth(mPItemParent.getYearMonth());
		dataCheckSign.stream().forEach(x -> {
			selfConfirm.add(new SelfConfirm(x.getEmployeeId(), x.isValue()));
		});
		ParamRegisterConfirmMonth param = new ParamRegisterConfirmMonth(ym, selfConfirm,
				mPItemParent.getClosureId(), new ClosureDate(closureDate.getClosureDay(),
						closureDate.getLastDayOfMonth()), GeneralDate.today());
		
		registerConfirmationMonth.registerConfirmationMonth(param);
	}

//	private void insertApproval(List<MPItemCheckBox> dataCheckApproval) {
//		if(dataCheckApproval.isEmpty()) return;
//		ParamDayApproval param = new ParamDayApproval(AppContexts.user().employeeId(),
//				dataCheckApproval.stream()
//						.map(x -> new ContentApproval(x.getDate(), x.isValue(), x.getEmployeeId(), x.isFlagRemoveAll()))
//						.collect(Collectors.toList()));
//		registerDayApproval.registerDayApproval(param);
//	}
	
	private List<MonthlyRecordWorkDto> getDtoFromQuery(List<MonthlyModifyQuery> query) {
		Set<String> emps = new HashSet<>();
		Set<YearMonth> yearmonth = new HashSet<>();
		query.stream().forEach(q -> {
			emps.add(q.getEmployeeId());
			yearmonth.add(new YearMonth(q.getYearMonth()));
		});
		List<MonthlyRecordWorkDto> values = finder.find(emps, yearmonth);
		List<MonthlyRecordWorkDto> listDtos = new ArrayList<>();
		values.forEach(v -> {
			MonthlyModifyQuery q = query.stream().filter(qr -> {
				return qr.getClosureId() == v.getClosureID() && qr.getEmployeeId().equals(v.getEmployeeId())
						&& v.yearMonth().compareTo(qr.getYearMonth()) == 0 && v.getClosureDate().equals(qr.getClosureDate());
			}).findFirst().orElse(null);
			if (q != null) {
				listDtos.add(v);
			}
		});
		return listDtos;
	}
	
	private void insertApproval(List<EmpPerformMonthParamImport> dataCheckApprovals) {
		if(dataCheckApprovals.isEmpty()) return;
		registerDayApproval.registerMonApproval(AppContexts.user().employeeId(), dataCheckApprovals);
	}

	public void removeMonApproval(List<EmpPerformMonthParamImport> dataCheckApprovals) {
		if(dataCheckApprovals.isEmpty()) return;
		registerDayApproval.removeMonApproval(AppContexts.user().employeeId(), dataCheckApprovals);
	}
	
	private List<MonthlyRecordWorkDto> createDtoNews(List<MonthlyModifyQuery> query,List<MonthlyRecordWorkDto> values) {
		Set<String> emps = new HashSet<>();
		Set<YearMonth> yearmonth = new HashSet<>();
		query.stream().forEach(q -> {
			emps.add(q.getEmployeeId());
			yearmonth.add(new YearMonth(q.getYearMonth()));
		});
		//List<MonthlyRecordWorkDto> oldValues = finder.find(emps, yearmonth);
		return values.stream().map(v -> {
			MonthlyModifyQuery q = query.stream().filter(qr -> {
				return qr.getClosureId() == v.getClosureID() && qr.getEmployeeId().equals(v.getEmployeeId())
						&& v.yearMonth().compareTo(qr.getYearMonth()) == 0 && v.getClosureDate().equals(qr.getClosureDate());
			}).findFirst().orElse(null);
			if(q == null){
				return null;
			}
			IntegrationOfMonthly domain = v.toDomain(v.employeeId(), v.yearMonth(), v.getClosureID(), v.getClosureDate());
			MonthlyRecordWorkDto dtoNew = MonthlyRecordWorkDto.fromOnlyAttTime(domain);
			MonthlyRecordWorkDto dto = AttendanceItemUtil.fromItemValues(dtoNew, q.getItems(), AttendanceItemType.MONTHLY_ITEM);
			return dto;
		}).filter(v -> v != null).collect(Collectors.toList());
	}
	
}
