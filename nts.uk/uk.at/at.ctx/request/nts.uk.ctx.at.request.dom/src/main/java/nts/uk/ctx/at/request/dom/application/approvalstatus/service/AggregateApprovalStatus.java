package nts.uk.ctx.at.request.dom.application.approvalstatus.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder.Case;

import lombok.val;
import lombok.extern.slf4j.Slf4j;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository_New;
import nts.uk.ctx.at.request.dom.application.ReflectedState_New;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.output.ApprovalStatusEmployeeOutput;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.output.ApprovalSttAppOutput;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.output.WorkplaceInfor;

/**
 * 承認状況を集計する
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class AggregateApprovalStatus {
	
	@Inject
	private ApplicationRepository_New applicationRepository;

	public ApprovalSttAppOutput aggregate(String companyId, WorkplaceInfor workplace, List<ApprovalStatusEmployeeOutput> appSttEmployees) {
		
		val states = appSttEmployees.stream()
				.map(e -> aggregate(companyId, e))
				.flatMap(l -> l.stream())
				.collect(Collectors.toList());
		
		return aggregateStates(workplace, states);
	}

	
	private List<ReflectedState_New> aggregate(String companyId, ApprovalStatusEmployeeOutput appSttEmployee) {

		val applications = this.applicationRepository.getListAppBySID(
				companyId, appSttEmployee.getSid(),
				appSttEmployee.getStartDate(),
				appSttEmployee.getEndDate());
		
		return applications.stream()
				.map(a -> a.getReflectionInformation().getStateReflectionReal())
				.collect(Collectors.toList());
	}


	private ApprovalSttAppOutput aggregateStates(WorkplaceInfor workplace, List<ReflectedState_New> states) {
		
		int numApproved = 0;		// 承認済み
		int numNotApproved = 0;		// 未承認
		int numNotReflected = 0;	// 未反映
		int numDenial = 0;			// 否認
		int numCancel = 0;			// 取り消し（集計対象から除外する）
		
		for (val state : states) {
			switch (state) {
			// 未反映 or 差し戻し
			case NOTREFLECTED:
			case REMAND:
				numNotApproved++;
				numNotReflected++;
				break;
			// 反映待ち
			case WAITREFLECTION:
				numApproved++;
				numNotReflected++;
				break;
			// 否認
			case DENIAL:
				numDenial++;
				break;
			// 反映済み
			case REFLECTED:
				numApproved++;
				break;
			// 取り消し
			case CANCELED:
			case WAITCANCEL:
				numCancel++;
				break;
			}
		}
		
		return new ApprovalSttAppOutput(
				workplace.getCode(),
				workplace.getName(),
				numNotReflected != 0,
				false,
				states.size() - numCancel,
				numApproved,
				numNotReflected,
				numNotApproved,
				numDenial);
	}
}
