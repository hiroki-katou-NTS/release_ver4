package nts.uk.screen.at.app.dailymodify.command;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.uk.ctx.at.record.app.command.dailyperform.DailyRecordWorkCommand;
import nts.uk.ctx.at.record.app.command.dailyperform.DailyRecordWorkCommandHandler;
import nts.uk.ctx.at.record.app.command.dailyperform.checkdata.DPItemValueRC;
import nts.uk.ctx.at.record.app.find.dailyperform.DailyRecordDto;
import nts.uk.ctx.at.record.app.find.dailyperform.DailyRecordWorkFinder;
import nts.uk.ctx.at.record.dom.approvalmanagement.dailyperformance.algorithm.ContentApproval;
import nts.uk.ctx.at.record.dom.approvalmanagement.dailyperformance.algorithm.ParamDayApproval;
import nts.uk.ctx.at.record.dom.approvalmanagement.dailyperformance.algorithm.RegisterDayApproval;
import nts.uk.ctx.at.record.dom.editstate.EditStateOfDailyPerformance;
import nts.uk.ctx.at.record.dom.editstate.enums.EditStateSetting;
import nts.uk.ctx.at.record.dom.optitem.OptionalItemAtr;
import nts.uk.ctx.at.record.dom.optitem.OptionalItemRepository;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.algorithm.ParamIdentityConfirmDay;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.algorithm.RegisterIdentityConfirmDay;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.algorithm.SelfConfirmDay;
import nts.uk.ctx.at.shared.dom.attendance.util.AttendanceItemUtil;
import nts.uk.screen.at.app.dailymodify.query.DailyModifyQuery;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPItemCheckBox;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPItemValue;
import nts.uk.shr.com.context.AppContexts;

@Stateless
@Transactional
/** 日別修正CommandFacade */
public class DailyModifyCommandFacade {

	/** finder */
	@Inject
	private DailyRecordWorkFinder finder;

	@Inject
	private DailyRecordWorkCommandHandler handler;

//	@Inject
//	private EditStateColorOfDailyPerformCommandAddHandler editStateHandler;

	@Inject
	private RegisterIdentityConfirmDay registerIdentityConfirmDay;
	
	@Inject
	private RegisterDayApproval registerDayApproval;

	@Inject
	private OptionalItemRepository optionalMasterRepo;

	public void handleAdd(DailyModifyQuery query) {
//		handleAdd(createCommand(AppContexts.user().employeeId(), toDto(query), query));
		handleAdd(Arrays.asList(query));
	}

	public void handleUpdate(DailyModifyQuery query) {
//		this.handler.handleUpdate(createCommand(AppContexts.user().employeeId(), toDto(query), query));
		handleUpdate(Arrays.asList(query));
	}
	
	public void handleAdd(List<DailyModifyQuery> querys) {
//		String sid = AppContexts.user().employeeId();
//		List<DailyRecordDto> dto = toDto(querys);
//		this.handler.handleAdd(dto.stream().map(o -> {
//			DailyModifyQuery query = querys.stream().filter(q -> q.getBaseDate().equals(o.workingDate()) 
//										&& q.getEmployeeId().equals(o.employeeId()))
//					.findFirst().get();
//			return createCommand(sid, o, query);
//		}).collect(Collectors.toList()));
		this.handler.handleAdd(createCommands(querys, AppContexts.user().employeeId()));
	}

	public List<DPItemValueRC> handleUpdate(List<DailyModifyQuery> querys) {
//		String sid = AppContexts.user().employeeId();
//		List<DailyRecordDto> dto = toDto(querys);
//		List<DailyRecordWorkCommand> command = dto.stream().map(o -> {
//						DailyModifyQuery query = querys.stream().filter(q -> q.getBaseDate().equals(o.workingDate()) 
//								&& q.getEmployeeId().equals(o.employeeId()))
//			.findFirst().get();
//			return createCommand(sid, o, query);
//			}).collect(Collectors.toList());
		return this.handler.handleUpdate(createCommands(querys, AppContexts.user().employeeId()));
	}

	private List<EditStateOfDailyPerformance> convertTo(String sid, DailyModifyQuery query) {
		return query.getItemValues().stream().map(x -> {
			return new EditStateOfDailyPerformance(query.getEmployeeId(), x.getItemId(), query.getBaseDate(),
					sid.equals(query.getEmployeeId()) ? EditStateSetting.HAND_CORRECTION_MYSELF
							: EditStateSetting.HAND_CORRECTION_OTHER);
		}).collect(Collectors.toList());
	}
	
	private List<DailyRecordWorkCommand> createCommands(List<DailyModifyQuery> query, String sID) {
		Map<Integer, OptionalItemAtr> optionalMaster = optionalMasterRepo
				.findAll(AppContexts.user().companyId())
				.stream().collect(Collectors.toMap(c -> c.getOptionalItemNo().v(), c -> c.getOptionalItemAtr()));
		List<DailyRecordDto> oldValues = finder.find(query.stream()
									.collect(Collectors.groupingBy(c -> c.getEmployeeId(), 
												Collectors.collectingAndThen(Collectors.toList(), 
													c -> c.stream().map(q -> q.getBaseDate())
																	.collect(Collectors.toList())))));
		return oldValues.stream().map(o -> {
			DailyModifyQuery dq = query.stream().filter(q -> q.getBaseDate().equals(o.workingDate()) 
																&& q.getEmployeeId().equals(o.employeeId()))
									.findFirst().get();
			AttendanceItemUtil.fromItemValues(o, dq.getItemValues());
			o.getOptionalItem().ifPresent(optional -> {
				optional.correctItemsWith(optionalMaster);
			});
			o.getTimeLeaving().ifPresent(dto -> {
				if(dto.getWorkAndLeave() != null) 
					dto.getWorkAndLeave().removeIf(tl -> tl.getWorking() == null && tl.getLeave() == null);
			});
			return createCommand(sID, o, dq);
		}).collect(Collectors.toList());
	}

	private DailyRecordWorkCommand createCommand(String sid, DailyRecordDto dto, DailyModifyQuery query) {
		DailyRecordWorkCommand command = DailyRecordWorkCommand.open()
														.forEmployeeId(query.getEmployeeId())
														.withWokingDate(query.getBaseDate())
														.withData(dto)
														.fromItems(query.getItemValues());
		command.getEditState().updateDatas(convertTo(sid, query));
		return command;
	}

	public void handleEditCell(List<DPItemValue> data) {
//		String sid = AppContexts.user().employeeId();
//		List<EditStateOfDailyPerformance> editData = data.stream().map(x -> {
//			return new EditStateOfDailyPerformance(x.getEmployeeId(), x.getItemId(), x.getDate(),
//					sid.equals(x.getEmployeeId()) ? EditStateSetting.HAND_CORRECTION_MYSELF
//							: EditStateSetting.HAND_CORRECTION_OTHER);
//		}).collect(Collectors.toList());
//		EditStateColorOfDailyPerformCommand command = new EditStateColorOfDailyPerformCommand();
//		command.getData().addAll(editData);
//		editStateHandler.handle(command);
	}

//	public void insertSign(List<DPItemCheckBox> dataCheckSign) {
//		ParamIdentityConfirmDay day = new ParamIdentityConfirmDay(AppContexts.user().employeeId(), dataCheckSign
//				.stream().map(x -> new SelfConfirmDay(x.getDate(), x.isValue())).collect(Collectors.toList()));
//		registerIdentityConfirmDay.registerIdentity(day);
//	}
}
