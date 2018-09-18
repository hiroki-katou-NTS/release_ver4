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

import org.apache.commons.lang3.tuple.Pair;

import nts.arc.task.AsyncTask;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.app.find.monthly.finder.MonthlyRecordWorkFinder;
import nts.uk.ctx.at.record.app.find.monthly.root.MonthlyRecordWorkDto;
import nts.uk.ctx.at.record.app.find.monthly.root.common.ClosureDateDto;
import nts.uk.ctx.at.record.dom.approvalmanagement.dailyperformance.algorithm.RegisterDayApproval;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.month.algorithm.ParamRegisterConfirmMonth;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.month.algorithm.RegisterConfirmationMonth;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.month.algorithm.SelfConfirm;
import nts.uk.ctx.at.shared.dom.attendance.util.AttendanceItemIdContainer;
import nts.uk.ctx.at.shared.dom.attendance.util.AttendanceItemUtil;
import nts.uk.ctx.at.shared.dom.attendance.util.AttendanceItemUtil.AttendanceItemType;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ItemValue;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ValueType;
import nts.uk.screen.at.app.monthlyperformance.audittrail.MonthlyCorrectionLogCommand;
import nts.uk.screen.at.app.monthlyperformance.audittrail.MonthlyCorrectionLogCommandHandler;
import nts.uk.screen.at.app.monthlyperformance.correction.dto.MPItemCheckBox;
import nts.uk.screen.at.app.monthlyperformance.correction.dto.MPItemDetail;
import nts.uk.screen.at.app.monthlyperformance.correction.dto.MPItemParent;
import nts.uk.screen.at.app.monthlyperformance.correction.query.MonthlyModifyQuery;
import nts.uk.shr.com.context.AppContexts;
/**
 * 
 * @author sonnh1
 *
 */
@Stateless
public class MonModifyCommandFacade {
	
	@Inject
	private MonthModifyCommandFacade monthModifyCommandFacade;

//	@Inject
//	private MonthlyPerformanceCorrectionUpdateCommand monthlyPerformanceCorrectionUpdateCommand;
	
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
		// new
		mapItemDetail.entrySet().forEach(item -> {
			List<MPItemDetail> rowDatas = item.getValue();
			listQuery.add(new MonthlyModifyQuery(rowDatas.stream().map(x -> {
				return ItemValue.builder().itemId(x.getItemId()).layout(x.getLayoutCode()).value(x.getValue())
						.valueType(ValueType.valueOf(x.getValueType())).withPath("");
			}).collect(Collectors.toList()), dataParent.getYearMonth(), item.getKey(), dataParent.getClosureId(),
					dataParent.getClosureDate()));
		});
		List<MonthlyRecordWorkDto> oldDtos = getDtoFromQuery(listQuery);
		monthModifyCommandFacade.handleUpdate(listQuery, oldDtos);

		// old
//		dataParent.getMPItemDetails().forEach(item -> {
//			ClosureDateDto closureDate = dataParent.getClosureDate();
//			EditStateOfMonthlyPerformanceDto editStateOfMonthlyPerformanceDto = new EditStateOfMonthlyPerformanceDto(
//					item.getEmployeeId(), new Integer(item.getItemId()),
//					new DatePeriod(dataParent.getStartDate(), dataParent.getEndDate()),
//					dataParent.getYearMonth().intValue(), dataParent.getClosureId(),
//					new nts.uk.screen.at.app.monthlyperformance.correction.dto.ClosureDateDto(
//							closureDate.getClosureDay().intValue(),
//							closureDate.getLastDayOfMonth().booleanValue() ? 1 : 0),
//					new Integer(0));
//			this.monthlyPerformanceCorrectionUpdateCommand.handleAddOrUpdate(editStateOfMonthlyPerformanceDto);
//		});
		
		// insert sign
		this.insertSign(dataParent);
		
		List<MPItemCheckBox> listRegister = new ArrayList<>();
		List<MPItemCheckBox> listRemove = new ArrayList<>();
		for(MPItemCheckBox mpi :dataParent.getDataCheckApproval()) {
			if(mpi.isValue()) {
				listRegister.add(mpi);
			}else {
				listRemove.add(mpi);
			}
		}
		
		// insert approval
		this.insertApproval(listRegister,dataParent.getEndDate());
		// remove approval		
		this.removeMonApproval(listRemove,dataParent.getEndDate());

		// add correction log
		ExecutorService executorService = Executors.newFixedThreadPool(1);
		AsyncTask task = AsyncTask.builder().withContexts().keepsTrack(false).threadName(this.getClass().getName())
				.build(() -> {
					List<MonthlyRecordWorkDto> newDtos = getDtoFromQuery(listQuery);
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
				mPItemParent.getClosureId(), closureDate.getLastDayOfMonth() ? ym.lastDateInMonth() : closureDate.getClosureDay(), GeneralDate.today());
		
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
	
	private void insertApproval(List<MPItemCheckBox> dataCheckApprovals,GeneralDate endDate) {
		if(dataCheckApprovals.isEmpty()) return;
		Set<Pair<String, GeneralDate>> empAndDates = new HashSet<>();
		for(MPItemCheckBox dataCheckApproval : dataCheckApprovals) {
			empAndDates.add(Pair.of(dataCheckApproval.getEmployeeId(), endDate));
		}
		registerDayApproval.registerMonApproval(AppContexts.user().userId(), 
				new ArrayList<>(empAndDates), 2, AppContexts.user().companyId());
	}

	public void removeMonApproval(List<MPItemCheckBox> dataCheckApprovals,GeneralDate endDate) {
		if(dataCheckApprovals.isEmpty()) return;
		Set<Pair<String, GeneralDate>> empAndDates = new HashSet<>();
		for(MPItemCheckBox dataCheckApproval : dataCheckApprovals) {
			empAndDates.add(Pair.of(dataCheckApproval.getEmployeeId(), endDate));
		}
		registerDayApproval.removeMonApproval(AppContexts.user().userId(), 
				new ArrayList<>(empAndDates), 2, AppContexts.user().companyId());
	}
	
}
