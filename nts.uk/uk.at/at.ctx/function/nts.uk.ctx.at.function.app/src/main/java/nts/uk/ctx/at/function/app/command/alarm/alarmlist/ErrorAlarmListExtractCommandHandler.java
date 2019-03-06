package nts.uk.ctx.at.function.app.command.alarm.alarmlist;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.val;
import nts.arc.layer.app.command.AsyncCommandHandler;
import nts.arc.layer.app.command.AsyncCommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.task.data.TaskDataSetter;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.function.dom.alarm.AlarmPatternSetting;
import nts.uk.ctx.at.function.dom.alarm.AlarmPatternSettingRepository;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.EmployeeSearchDto;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.ExtractAlarmListService;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.ExtractedAlarmDto;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.AlarmCheckConditionByCategory;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.AlarmCheckConditionByCategoryRepository;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.CheckCondition;
import nts.uk.ctx.at.function.dom.alarm.extraprocessstatus.AlarmListExtraProcessStatus;
import nts.uk.ctx.at.function.dom.alarm.extraprocessstatus.AlarmListExtraProcessStatusRepository;
import nts.uk.ctx.at.function.dom.alarm.extraprocessstatus.ExtractionState;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class ErrorAlarmListExtractCommandHandler extends AsyncCommandHandler<ErrorAlarmListCommand> {

//	@Inject
//	private ExtractAlarmListFinder extractAlarmFinder;
	
	@Inject
	private AlarmListExtraProcessStatusRepository repo;

	@Inject
	private AlarmCheckConditionByCategoryRepository erAlByCateRepo;
	
	@Inject
	private AlarmPatternSettingRepository alPatternSettingRepo;
	
	@Inject
	private ExtractAlarmListService extractAlarmListService;
	
//	private static final List<Integer> CHECK_CATEGORY = Arrays.asList(AlarmCategory.DAILY.value, AlarmCategory.SCHEDULE_4WEEK.value, 
//			AlarmCategory.AGREEMENT.value, AlarmCategory.MONTHLY.value, AlarmCategory.MULTIPLE_MONTH.value);
	
	@Override
	protected void handle(CommandHandlerContext<ErrorAlarmListCommand> context) {
		val asyncContext = context.asAsync();
		String comId = AppContexts.user().companyId();
		ErrorAlarmListCommand command = context.getCommand();
		
		// パラメータ．パターンコードをもとにドメインモデル「アラームリストパターン設定」を取得する
		// パラメータ．パターンコードから「アラームリストパターン設定」を取得する
		Optional<AlarmPatternSetting> alarmPatternSetting = this.alPatternSettingRepo.findByAlarmPatternCode(comId, command.getAlarmCode());		
		if(!alarmPatternSetting.isPresent())
			throw new RuntimeException("「アラームリストパターン設定 」が見つかりません！");
		
		List<EmployeeSearchDto> listEmpId = command.getListEmployee();

		TaskDataSetter dataSetter = asyncContext.getDataSetter();
		AtomicInteger counter = new AtomicInteger(0);

		ObjectMapper mapper = new ObjectMapper();
		dataSetter.setData("extracting", false);
		
		dataSetter.setData("empCount", counter.get());
		List<Integer> listCategory = command.getListPeriodByCategory().stream().map(x -> x.getCategory()).collect(Collectors.toList());
		List<CheckCondition> checkConList = alarmPatternSetting.get().getCheckConList().stream()
				.filter(e -> listCategory.contains(e.getAlarmCategory().value)).collect(Collectors.toList());
		
		List<AlarmCheckConditionByCategory> eralCate = erAlByCateRepo.findByCategoryAndCode(comId, listCategory,
				checkConList.stream().map(c -> c.getCheckConditionList()).flatMap(List::stream).collect(Collectors.toList()));
		int max = listEmpId.size() * eralCate.size();
		
		ExtractedAlarmDto dto = this.extractAlarmListService.extractAlarmV2(listEmpId, command.getListPeriodByCategory(), 
				eralCate, checkConList, finished -> {
			counter.set(counter.get() + finished);
			int completed = calcCompletedEmp(listEmpId, counter, max, finished).intValue();
			dataSetter.updateData("empCount", completed >= max ? max - 1 : completed);
		}, () -> {
			return shouldStop(context, asyncContext);
		});
		dataSetter.updateData("extracting", dto.isExtracting());
		dataSetter.setData("dataWriting", true);
		try {
			if(CollectionUtil.isEmpty(dto.getExtractedAlarmData())){
				dataSetter.setData("nullData", true);
			} else {
				for (int i = 0; i < dto.getExtractedAlarmData().size(); i++) {
					/** Convert to json string */
					dataSetter.setData("dataNo" + i, mapper.writeValueAsString(dto.getExtractedAlarmData().get(i)));
				}
			}
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
		
		dataSetter.updateData("empCount", listEmpId.size());
	}

	private Double calcCompletedEmp(List<EmployeeSearchDto> listEmpId, AtomicInteger counter, int max, Integer finished) {
		double completedPercent = Double.valueOf(counter.get()) / max;
		return Math.floor(completedPercent * listEmpId.size());
	}

	private Boolean shouldStop(CommandHandlerContext<ErrorAlarmListCommand> context,
			AsyncCommandHandlerContext<ErrorAlarmListCommand> asyncContext) {
		Optional<AlarmListExtraProcessStatus> alarmListExtraProcessStatus= this.repo.getAlListExtaProcessByID(context.getCommand().getStatusProcessId());
		if (alarmListExtraProcessStatus.isPresent()
				&& alarmListExtraProcessStatus.get().getStatus() == ExtractionState.INTERRUPTION) {
			asyncContext.finishedAsCancelled();
			return true;
		}
		return false;
	}
}