package nts.uk.ctx.at.shared.dom.workrule.closure.service;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.adapter.employee.EmpEmployeeAdapter;
import nts.uk.ctx.at.shared.dom.adapter.employment.ShareEmploymentAdapter;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository;

/**
 * 実装：社員に対応する締め開始日を取得する
 * @author shuichi_ishida
 */
@Stateless
public class GetClosureStartForEmployeeImpl implements GetClosureStartForEmployee {

	/** 締めアルゴリズム */
	@Inject
	private ClosureService closureService;
	/** 社員の取得 */
	@Inject
	private EmpEmployeeAdapter empEmployee;
	/** 所属雇用履歴の取得 */
	@Inject
	private ShareEmploymentAdapter shareEmploymentAdapter;
	/** 雇用に紐づく就業締めの取得 */
	@Inject
	private ClosureEmploymentRepository closureEmploymentRepo;

	/** 社員に対応する締め開始日を取得する */
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Optional<GeneralDate> algorithm(String employeeId) {
		
		GetClosureStartForEmployeeProc proc = new GetClosureStartForEmployeeProc(
				this.closureService,
				this.empEmployee,
				this.shareEmploymentAdapter,
				this.closureEmploymentRepo);
		return proc.algorithm(employeeId);
	}

}
