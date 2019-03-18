package nts.uk.ctx.at.function.dom.alarm.alarmlist;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.transaction.TransactionSynchronizationRegistry;

import org.apache.log4j.Logger;

import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.aggregationprocess.AggregationProcessService;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.AlarmCheckConditionByCategory;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.CheckCondition;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class ExtractAlarmListService {

	@Inject
	private AggregationProcessService aggregationProcessService;
	
	public ExtractedAlarmDto extractAlarm(List<EmployeeSearchDto> listEmployee, String checkPatternCode,
			List<PeriodByAlarmCategory> periodByCategory) {		
		String companyID = AppContexts.user().companyId();
		
		// チェック条件に当てはまらない場合(When it does not fit the check condition)
		// 条件を満たしていない
		if (listEmployee.isEmpty()) {
			// エラーメッセージ(#Msg_834)を表示する
			throw new BusinessException("Msg_834");	
		}
		
		// ドメインモデル「アラームリスト抽出処理状況」を作成する
//		GeneralDateTime now1 = GeneralDateTime.now();
		GeneralDate today = GeneralDate.today();

		
		// 勤務実績のアラームリストの集計処理を行う
		List<AlarmExtraValueWkReDto> listAlarmExtraValueWR = aggregationProcessService.processAlarmListWorkRecord(today, companyID, listEmployee,
				checkPatternCode, periodByCategory);

		// 集計結果を確認する sort list
		Comparator<AlarmExtraValueWkReDto> comparator = Comparator.comparing(AlarmExtraValueWkReDto::getHierarchyCd)
																	.thenComparing(Comparator.comparing(AlarmExtraValueWkReDto::getEmployeeCode))
																	.thenComparing(Comparator.comparing(AlarmExtraValueWkReDto::getAlarmValueDate))
																	.thenComparing(Comparator.comparing(AlarmExtraValueWkReDto::getCategory));
		List<AlarmExtraValueWkReDto> sortedAlarmExtraValue = listAlarmExtraValueWR.stream().sorted(comparator).collect(Collectors.toList());
		// 集計データが無い場合
		if (listAlarmExtraValueWR.isEmpty()) {
			// 情報メッセージ(#Msg_835) を表示する
			return  new ExtractedAlarmDto(new ArrayList<>(), false, true);
		}
		// 集計データがある場合
		// B画面 ダイアログ「アラームリスト」を起動する
		return new ExtractedAlarmDto(sortedAlarmExtraValue, false, false);		

	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public ExtractedAlarmDto extractAlarmV2(List<EmployeeSearchDto> listEmployee,
			List<PeriodByAlarmCategory> periodByCategory, List<AlarmCheckConditionByCategory> eralCate,
			List<CheckCondition> checkConList, Consumer<Integer> counter, Supplier<Boolean> shouldStop) {
		
		String companyID = AppContexts.user().companyId();
		
		// チェック条件に当てはまらない場合(When it does not fit the check condition)
		// 条件を満たしていない
		if (listEmployee.isEmpty()) {
			// エラーメッセージ(#Msg_834)を表示する
			throw new BusinessException("Msg_834");	
		}
		
		// ドメインモデル「アラームリスト抽出処理状況」を作成する
//		GeneralDateTime now1 = GeneralDateTime.now();
		GeneralDate today = GeneralDate.today();
		
		// 勤務実績のアラームリストの集計処理を行う
		List<AlarmExtraValueWkReDto> listAlarmExtraValueWR = aggregationProcessService.processAlarmListWorkRecordV2(today, companyID, listEmployee,
				periodByCategory, eralCate, checkConList, counter, shouldStop);
		
		
		// 集計結果を確認する sort list
		Comparator<AlarmExtraValueWkReDto> comparator = Comparator.comparing(AlarmExtraValueWkReDto::getHierarchyCd)
																	.thenComparing(Comparator.comparing(AlarmExtraValueWkReDto::getEmployeeCode))
																	.thenComparing(Comparator.comparing(AlarmExtraValueWkReDto::getAlarmValueDate))
																	.thenComparing(Comparator.comparing(AlarmExtraValueWkReDto::getCategory));
		List<AlarmExtraValueWkReDto> sortedAlarmExtraValue = listAlarmExtraValueWR.stream().sorted(comparator).collect(Collectors.toList());
		// 集計データが無い場合
		if (listAlarmExtraValueWR.isEmpty()) {
			// 情報メッセージ(#Msg_835) を表示する
			return  new ExtractedAlarmDto(new ArrayList<>(), false, true);
		}
		// 集計データがある場合
		// B画面 ダイアログ「アラームリスト」を起動する
		return new ExtractedAlarmDto(sortedAlarmExtraValue, false, false);		

	}

}
