package nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.basicinfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.adapter.employee.EmpEmployeeAdapter;
import nts.uk.ctx.at.shared.dom.adapter.employee.EmployeeImport;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.GetClosureStartForEmployee;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.GrantHdTblSet;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.LengthServiceTbl;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.export.GetNextAnnualLeaveGrant;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.export.NextAnnualLeaveGrant;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 実装：次回年休付与日を計算
 * @author shuichu_ishida
 */
@Stateless
public class CalcNextAnnualLeaveGrantDateImpl implements CalcNextAnnualLeaveGrantDate {

	/** 年休社員基本情報 */
	@Inject
	private AnnLeaEmpBasicInfoRepository annLeaEmpBasicInfoRepo;
	/** 社員の取得 */
	@Inject
	private EmpEmployeeAdapter empEmployee;
	/** 社員に対応する締め開始日を取得する */
	@Inject
	private GetClosureStartForEmployee getClosureStartForEmployee;
	/** 次回年休付与を取得する */
	@Inject
	private GetNextAnnualLeaveGrant getNextAnnualLeaveGrant;
	
	/** 次回年休付与を計算 */
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<NextAnnualLeaveGrant> algorithm(String companyId, String employeeId, Optional<DatePeriod> period){
		return this.algorithm(companyId, employeeId, period,
				Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
	}
	
	/** 次回年休付与を計算 */
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<NextAnnualLeaveGrant> algorithm(String companyId, String employeeId, Optional<DatePeriod> period,
			Optional<EmployeeImport> employeeOpt, Optional<AnnualLeaveEmpBasicInfo> annualLeaveEmpBasicInfoOpt,
			Optional<GrantHdTblSet> grantHdTblSetOpt, Optional<List<LengthServiceTbl>> lengthServiceTblsOpt) {
		
		List<NextAnnualLeaveGrant> nextAnnualLeaveGrantList = new ArrayList<>();

		// 「年休社員基本情報」を取得
		Optional<AnnualLeaveEmpBasicInfo> empBasicInfoOpt = Optional.empty();
		if (annualLeaveEmpBasicInfoOpt.isPresent()){
			empBasicInfoOpt = annualLeaveEmpBasicInfoOpt;
		}
		else {
			empBasicInfoOpt = this.annLeaEmpBasicInfoRepo.get(employeeId);
		}
		if (!empBasicInfoOpt.isPresent()) return nextAnnualLeaveGrantList;
		val empBasicInfo = empBasicInfoOpt.get();
	
		// 「社員」を取得する
		EmployeeImport employee = null;
		if (employeeOpt.isPresent()){
			employee = employeeOpt.get();
		}
		else {
			employee = this.empEmployee.findByEmpId(employeeId);
		}
		if (employee == null) return nextAnnualLeaveGrantList;
		
		// 「期間」をチェック
		DatePeriod targetPeriod = null;
		boolean isSingleDay = false;	// 単一日フラグ=false
		if (period.isPresent()){
			
			// 開始日、終了日を１日後にずらした期間
			val paramPeriod = period.get();
			int addEnd = 0;
			if (paramPeriod.end().before(GeneralDate.max())) addEnd = 1;
			targetPeriod = new DatePeriod(paramPeriod.start().addDays(1), paramPeriod.end().addDays(addEnd));
		}
		else {
			
			// 社員に対応する締め開始日を取得する
			val closureStartOpt = this.getClosureStartForEmployee.algorithm(employeeId);
			if (!closureStartOpt.isPresent()) return nextAnnualLeaveGrantList;
			targetPeriod = new DatePeriod(closureStartOpt.get().addDays(1), GeneralDate.max());
			
			isSingleDay = true;			// 単一日フラグ=true
		}
		
		// 年休付与テーブル設定コードを取得する
		val grantRule = empBasicInfo.getGrantRule();
		val grantTableCode = grantRule.getGrantTableCode().v();
		
		// 次回年休付与を取得する
		nextAnnualLeaveGrantList = this.getNextAnnualLeaveGrant.algorithm(
				companyId, grantTableCode, employee.getEntryDate(), grantRule.getGrantStandardDate(),
				targetPeriod, isSingleDay, grantHdTblSetOpt, lengthServiceTblsOpt);
		
		// 次回年休付与を返す
		return nextAnnualLeaveGrantList;
	}

	@Override
	public List<NextAnnualLeaveGrant> calNextHdGrantV2(String companyId, String employeeId, Optional<DatePeriod> period,
			Optional<EmployeeImport> empOp, Optional<AnnualLeaveEmpBasicInfo> annLeaEmpInfoOp,
			Optional<GrantHdTblSet> grantHdTblSetOpt, Optional<List<LengthServiceTbl>> lengthSvTblsOpt,
			Optional<GeneralDate> closureDate) {

		List<NextAnnualLeaveGrant> nextAnnualLeaveGrantList = new ArrayList<>();
		// 「年休社員基本情報」を取得
		Optional<AnnualLeaveEmpBasicInfo> empBasicInfoOpt = Optional.empty();
		if (annLeaEmpInfoOp.isPresent()){
			empBasicInfoOpt = annLeaEmpInfoOp;
		}
		else {
			empBasicInfoOpt = this.annLeaEmpBasicInfoRepo.get(employeeId);
		}
		if (!empBasicInfoOpt.isPresent()) return nextAnnualLeaveGrantList;
		val empBasicInfo = empBasicInfoOpt.get();
	
		// 「社員」を取得する
		EmployeeImport employee = null;
		if (empOp.isPresent()){
			employee = empOp.get();
		}
		else {
			employee = this.empEmployee.findByEmpId(employeeId);
		}
		if (employee == null) return nextAnnualLeaveGrantList;
		
		// 「期間」をチェック
		DatePeriod targetPeriod = null;
		boolean isSingleDay = false;	// 単一日フラグ=false
		if (period.isPresent()){
			
			// 開始日、終了日を１日後にずらした期間
			val paramPeriod = period.get();
			int addEnd = 0;
			if (paramPeriod.end().before(GeneralDate.max())) addEnd = 1;
			targetPeriod = new DatePeriod(paramPeriod.start().addDays(1), paramPeriod.end().addDays(addEnd));
		}
		else {
			
			// 社員に対応する締め開始日を取得する
			if (!closureDate.isPresent()) return nextAnnualLeaveGrantList;
			targetPeriod = new DatePeriod(closureDate.get().addDays(1), GeneralDate.max());
			isSingleDay = true;			// 単一日フラグ=true
		}
		
		// 年休付与テーブル設定コードを取得する
		val grantRule = empBasicInfo.getGrantRule();
		val grantTableCode = grantRule.getGrantTableCode().v();
		
		// 次回年休付与を取得する
		nextAnnualLeaveGrantList = this.getNextAnnualLeaveGrant.algorithm(
				companyId, grantTableCode, employee.getEntryDate(), grantRule.getGrantStandardDate(),
				targetPeriod, isSingleDay, grantHdTblSetOpt, lengthSvTblsOpt);
		
		// 次回年休付与を返す
		return nextAnnualLeaveGrantList;
	}
}
