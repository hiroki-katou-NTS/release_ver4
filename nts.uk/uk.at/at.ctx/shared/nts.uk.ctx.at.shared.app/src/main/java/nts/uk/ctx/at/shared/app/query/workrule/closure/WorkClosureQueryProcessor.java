/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.app.query.workrule.closure;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmployment;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureHistory;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.UseClassification;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class WorkClosureQueryProcessor.
 */
@Stateless
public class WorkClosureQueryProcessor {
	
	/** The closure repo. */
	@Inject
	private ClosureRepository closureRepo;
	
	/** The closure emp repo. */
	@Inject
	private ClosureEmploymentRepository closureEmpRepo;

	/**
	 * Find closure by reference date.
	 *
	 * @param refDate the ref date
	 * @return the list
	 */
	// request 140: 会社の締めを取得する
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<ClosureResultModel> findClosureByReferenceDate(GeneralDate refDate) {
		// find all active closure
		List<Closure> closures = this.closureRepo.findAllActive(AppContexts.user().companyId(),
				UseClassification.UseClass_Use);

		// get result list
		List<ClosureResultModel> resultList = new ArrayList<>();
		closures.forEach(clo -> {
			ClosureHistory history = clo.getHistoryByBaseDate(refDate);
			ClosureResultModel res = ClosureResultModel.builder()
					.closureId(history.getClosureId().value)
					.closureName(history.getClosureName().v())
					.build();
			resultList.add(res);
		});

		return resultList;
	}

	/**
	 * Find closure by employment code.
	 *
	 * @param empCode the emp code
	 * @return the integer
	 */
	// request 142: 雇用に紐づく締めを取得する
	public Integer findClosureByEmploymentCode(String empCode) {
		Optional<ClosureEmployment> closureEmp = this.closureEmpRepo.findByEmploymentCD(AppContexts.user().companyId(),
				empCode);
		if (closureEmp.isPresent()) {
			return closureEmp.get().getClosureId();
		}
		return ClosureId.RegularEmployee.value;
	}
}
