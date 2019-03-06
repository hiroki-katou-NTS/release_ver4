package nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.fixedcheckitem.checkadminunverified;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.FixedConditionDataRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.fixedcheckitem.checkadminunverified.checkbossconfirmed.CheckBossConfirmedService;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.fixedcheckitem.checkprincipalunconfirm.ValueExtractAlarmWR;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.fixedcheckitem.checkprincipalunconfirm.checkconfirm.StateConfirm;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class CheckAdminUnverifiedDefault implements CheckAdminUnverifiedService {

	@Inject
	private CheckBossConfirmedService checkBossConfirmedService;

	@Inject
	private FixedConditionDataRepository fixedConditionDataRepository;

	@Override
	public List<ValueExtractAlarmWR> checkAdminUnverified(String workplaceID, String employeeID, GeneralDate startDate,
			GeneralDate endDate) {
		List<ValueExtractAlarmWR> listValueExtractAlarmWR = new ArrayList<>();
		// 管理者の確認が完了しているかチェックする
		List<StateConfirm> listState = checkBossConfirmedService.checkBossConfirmed(employeeID, startDate, endDate);
		if (listState.isEmpty()) {
			return Collections.emptyList();
		}
		// 返り値をもとにアラーム値メッセージを生成する
		// 勤務実績のアラームデータを生成する
		String comment = fixedConditionDataRepository.getFixedByNO(4).get().getMessage().v();
		for (StateConfirm stateConfirm : listState) {
			if (!stateConfirm.isState()) {
				listValueExtractAlarmWR.add(new ValueExtractAlarmWR(workplaceID, employeeID, stateConfirm.getDate(),
						TextResource.localize("KAL010_1"), TextResource.localize("KAL010_44"),
						TextResource.localize("KAL010_45"), comment));
			}

		}

		return listValueExtractAlarmWR;
	}

	@Override
	public List<ValueExtractAlarmWR> checkAdminUnverified(String workplaceID, String employeeID,
			DatePeriod datePeriod) {
		List<ValueExtractAlarmWR> listValueExtractAlarmWR = new ArrayList<>();
		// 管理者の確認が完了しているかチェックする
		List<StateConfirm> listState = new ArrayList<>();
		try {
			listState = checkBossConfirmedService.checkBossConfirmed(employeeID, datePeriod);
		} catch (BusinessException e) {
			throw new BusinessException("Msg_1430", "承認者");
		}
		// List<StateConfirm> listState =
		// checkBossConfirmedService.checkBossConfirmed(employeeID, datePeriod);
		if (listState.isEmpty()) {
			return Collections.emptyList();
		}
		// 返り値をもとにアラーム値メッセージを生成する
		// 勤務実績のアラームデータを生成する
		String comment = fixedConditionDataRepository.getFixedByNO(4).get().getMessage().v();
		for (StateConfirm stateConfirm : listState) {
			if (!stateConfirm.isState()) {
				listValueExtractAlarmWR.add(new ValueExtractAlarmWR(workplaceID, employeeID, stateConfirm.getDate(),
						TextResource.localize("KAL010_1"), TextResource.localize("KAL010_44"),
						TextResource.localize("KAL010_45"), comment));
			}

		}

		return listValueExtractAlarmWR;
	}

	public Map<String, List<GeneralDate>> checkAdminUnverified(List<String> employeeID, DatePeriod datePeriod) {
		List<StateConfirm> listState = checkBossConfirmedService.checkBossConfirmed(employeeID, datePeriod);
		
		return listState.stream().collect(Collectors.groupingBy(c -> c.getEmployeeId(),
																Collectors.collectingAndThen(Collectors.toList(),
																			list -> list.stream().filter(c -> !c.isState()).map(c -> c.getDate()).collect(Collectors.toList()))));
	}

}
