package nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.basicinfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.val;
import nts.arc.layer.app.cache.CacheCarrier;
import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.shared.dom.adapter.employee.EmployeeImport;
import nts.uk.ctx.at.shared.dom.bonuspay.enums.UseAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service.SpecialLeaveManagementService;
import nts.uk.ctx.at.shared.dom.specialholiday.export.NextSpecialLeaveGrant;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.GetClosureStartForEmployee;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.GrantHdTblSet;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.LengthServiceTbl;

/**
 * 次回特休付与を計算
 */
public class CalcNextSpecialLeaveGrantDate {

	/**
	 * 次回特休付与を計算
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param period 期間
	 * @return 次回特休付与リスト
	 */
	public static List<NextSpecialLeaveGrant> algorithm(
			SpecialLeaveManagementService.RequireM5 require, CacheCarrier cacheCarrier, 
			String companyId, String employeeId, int spLeaveCD, Optional<DatePeriod> period) {
		
		// ドメインモデル「特別休暇社員基本情報」を取得
		Optional<SpecialLeaveBasicInfo> specialLeaveBasicInfoOpt
			= require.specialLeaveBasicInfo(employeeId, spLeaveCD, UseAtr.USE);
		
		// 特別休暇使用区分をチェックする
		
			
		
		
		
			
		
		
		
		return algorithm(require, cacheCarrier, companyId, employeeId, period, 
				Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
	}

	/**
	 * 次回特休付与を計算
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param period 期間
	 * @param employee 社員
	 * @param annualLeaveEmpBasicInfo 特休社員基本情報
	 * @param grantHdTblSet 特休付与テーブル設定
	 * @param lengthServiceTbls 勤続年数テーブルリスト
	 * @return 次回特休付与リスト
	 */
	public static List<NextSpecialLeaveGrant> algorithm(RequireM2 require, CacheCarrier cacheCarrier,
			String companyId, String employeeId, Optional<DatePeriod> period,
			Optional<EmployeeImport> employeeOpt, Optional<AnnualLeaveEmpBasicInfo> annualLeaveEmpBasicInfoOpt,
			Optional<GrantHdTblSet> grantHdTblSetOpt, Optional<List<LengthServiceTbl>> lengthServiceTblsOpt) {
		
		List<NextSpecialLeaveGrant> nextAnnualLeaveGrantList = new ArrayList<>();

		// 「特休社員基本情報」を取得
		Optional<AnnualLeaveEmpBasicInfo> empBasicInfoOpt = Optional.empty();
		if (annualLeaveEmpBasicInfoOpt.isPresent()){
			empBasicInfoOpt = annualLeaveEmpBasicInfoOpt;
		}
		else {
			empBasicInfoOpt = require.employeeAnnualLeaveBasicInfo(employeeId);
		}
		if (!empBasicInfoOpt.isPresent()) return nextAnnualLeaveGrantList;
		val empBasicInfo = empBasicInfoOpt.get();
	
		// 「社員」を取得する
		EmployeeImport employee = null;
		if (employeeOpt.isPresent()){
			employee = employeeOpt.get();
		}
		else {
			employee = require.employee(cacheCarrier, employeeId);
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
			val closureStartOpt = GetClosureStartForEmployee.algorithm(require, cacheCarrier, employeeId);
			if (!closureStartOpt.isPresent()) return nextAnnualLeaveGrantList;
			targetPeriod = new DatePeriod(closureStartOpt.get().addDays(1), GeneralDate.max());
			
			isSingleDay = true;			// 単一日フラグ=true
		}
		
		// 特休付与テーブル設定コードを取得する
		val grantRule = empBasicInfo.getGrantRule();
		val grantTableCode = grantRule.getGrantTableCode().v();
		
		// 次回特休付与を取得する
		nextAnnualLeaveGrantList = GetNextSpecialLeaveGrant.algorithm(require, cacheCarrier,
				companyId, grantTableCode, employee.getEntryDate(), grantRule.getGrantStandardDate(),
				targetPeriod, isSingleDay, grantHdTblSetOpt, lengthServiceTblsOpt);
		
		// 次回特休付与を返す
		return nextAnnualLeaveGrantList;
	}
	
	/**
	 * 次回特休付与を計算
	 * @param repositoriesRequiredByRemNum 残数処理 キャッシュデータ
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param period 期間
	 * @param employee 社員
	 * @param annualLeaveEmpBasicInfo 特休社員基本情報
	 * @param grantHdTblSet 特休付与テーブル設定
	 * @param lengthServiceTbls 勤続年数テーブルリスト
	 * @return 次回特休付与リスト
	 */
	public static List<NextSpecialLeaveGrant> calNextHdGrantV2(RequireM1 require, CacheCarrier cacheCarrier, 
			String companyId, String employeeId, Optional<DatePeriod> period,
			Optional<EmployeeImport> empOp, Optional<AnnualLeaveEmpBasicInfo> annLeaEmpInfoOp,
			Optional<GrantHdTblSet> grantHdTblSetOpt, Optional<List<LengthServiceTbl>> lengthSvTblsOpt,
			Optional<GeneralDate> closureDate) {

		List<NextSpecialLeaveGrant> nextAnnualLeaveGrantList = new ArrayList<>();
		// 「特休社員基本情報」を取得
		Optional<AnnualLeaveEmpBasicInfo> empBasicInfoOpt = Optional.empty();
		if (annLeaEmpInfoOp.isPresent()){
			empBasicInfoOpt = annLeaEmpInfoOp;
		}
		else {
			empBasicInfoOpt = require.employeeAnnualLeaveBasicInfo(employeeId);
		}
		if (!empBasicInfoOpt.isPresent()) return nextAnnualLeaveGrantList;
		val empBasicInfo = empBasicInfoOpt.get();
	
		// 「社員」を取得する
		EmployeeImport employee = null;
		if (empOp.isPresent()){
			employee = empOp.get();
		}
		else {
			employee = require.employee(cacheCarrier, employeeId);
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
		
		// 特休付与テーブル設定コードを取得する
		val grantRule = empBasicInfo.getGrantRule();
		val grantTableCode = grantRule.getGrantTableCode().v();
		
		// 次回特休付与を取得する
		nextAnnualLeaveGrantList = GetNextSpecialLeaveGrant.algorithm(require, cacheCarrier,
				companyId, grantTableCode, employee.getEntryDate(), grantRule.getGrantStandardDate(),
				targetPeriod, isSingleDay, grantHdTblSetOpt, lengthSvTblsOpt);
		
		// 次回特休付与を返す
		return nextAnnualLeaveGrantList;
	}
	
	public static interface RequireM1 extends GetNextSpecialLeaveGrant.RequireM1 {

		Optional<AnnualLeaveEmpBasicInfo> employeeAnnualLeaveBasicInfo(String employeeId);
		
		EmployeeImport employee(CacheCarrier cacheCarrier, String empId);
	}
	
	public static interface RequireM2 extends RequireM1, GetClosureStartForEmployee.RequireM1, GetNextSpecialLeaveGrant.RequireM1 {
		
	}
}
