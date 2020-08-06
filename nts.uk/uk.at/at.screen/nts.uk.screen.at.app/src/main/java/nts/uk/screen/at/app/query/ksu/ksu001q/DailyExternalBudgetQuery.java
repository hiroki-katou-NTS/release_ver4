package nts.uk.screen.at.app.query.ksu.ksu001q;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.schedule.dom.workschedule.budgetcontrol.budgetperformance.ExtBudgetActItemCode;
import nts.uk.ctx.at.schedule.dom.workschedule.budgetcontrol.budgetperformance.ExtBudgetDaily;
import nts.uk.ctx.at.schedule.dom.workschedule.budgetcontrol.budgetperformance.ExtBudgetDailyRepository;
import nts.uk.ctx.at.shared.dom.workrule.organizationmanagement.workplace.TargetOrgIdenInfor;
import nts.uk.ctx.at.shared.dom.workrule.organizationmanagement.workplace.TargetOrganizationUnit;

/**
 * 期間中の外部予算日次を取得する
 * 
 * @author thanhlv
 *
 */
@Stateless
public class DailyExternalBudgetQuery {

	/** 日次の外部予算実績Repository */
	@Inject
	private ExtBudgetDailyRepository extBudgetDailyRepository;

	/**
	 * 期間中の外部予算日次を取得する
	 * 
	 * @param dailyExternal
	 * @return
	 */
	public List<DailyExternalBudgetDto> getDailyExternalBudget(DailyExternalBudget dailyExternal) {

		String workplaceId = null;
		String workplaceGroupId = null;
		if (dailyExternal.getUnit().equals("1")) {
			workplaceId = null;
			workplaceGroupId = dailyExternal.getId();
		}
		if (dailyExternal.getUnit().equals("0")) {
			workplaceId = dailyExternal.getId();
			workplaceGroupId = null;
		}

		TargetOrgIdenInfor targetOrg = new TargetOrgIdenInfor(
				TargetOrganizationUnit.valueOf(Integer.parseInt(dailyExternal.getUnit())), workplaceId,
				workplaceGroupId);

		DatePeriod datePeriod = new DatePeriod(GeneralDate.fromString(dailyExternal.getStartDate(), "yyyy/MM/dd"),
				GeneralDate.fromString(dailyExternal.getEndDate(), "yyyy/MM/dd"));

		// 期間の日次の外部予算実績を取得する
		List<ExtBudgetDaily> extBudgetDailies = extBudgetDailyRepository.getDailyExtBudgetResultsForPeriod(targetOrg,
				datePeriod, new ExtBudgetActItemCode(dailyExternal.getItemCode()));

		// 値（項目）
		return extBudgetDailies.stream().map(x -> {
			DailyExternalBudgetDto budgetDto = new DailyExternalBudgetDto();
			// TODO: interface ExtBudgetActualValue chua viet nen chua lam tiep duoc
			budgetDto.setValue(x.getActualValue().toString());
			budgetDto.setDate(x.getYmd().toString());
			return budgetDto;
		}).collect(Collectors.toList());
	}
}
