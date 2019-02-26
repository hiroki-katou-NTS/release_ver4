package nts.uk.ctx.at.shared.dom.workrule.closure.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.adapter.employee.EmpEmployeeAdapter;
import nts.uk.ctx.at.shared.dom.adapter.employment.ShareEmploymentAdapter;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureInfo;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

/**
 * 処理：社員に対応する締め開始日を取得する
 * @author shuichu_ishida
 */
public class GetClosureStartForEmployeeProc {

	/** 締めアルゴリズム */
	private ClosureService closureService;
	/** 社員の取得 */
	private EmpEmployeeAdapter empEmployee;
	/** 所属雇用履歴の取得 */
	private ShareEmploymentAdapter shareEmploymentAdapter;
	/** 雇用に紐づく就業締めの取得 */
	private ClosureEmploymentRepository closureEmploymentRepo;
	
	/** 社員ID */
	private String employeeId;
	/** 締め情報リスト */
	private List<ClosureInfo> closureInfos;
	/** チェック開始日 */
	private GeneralDate checkStart;
	/** チェック終了日 */
	private GeneralDate checkEnd;

	public GetClosureStartForEmployeeProc(
			ClosureService closureService,
			EmpEmployeeAdapter empEmployee,
			ShareEmploymentAdapter shareEmploymentAdapter,
			ClosureEmploymentRepository closureEmploymentRepo) {
		
		this.closureService = closureService;
		this.empEmployee = empEmployee;
		this.shareEmploymentAdapter = shareEmploymentAdapter;
		this.closureEmploymentRepo = closureEmploymentRepo;
	}
	
	/**
	 * 社員に対応する締め開始日を取得する
	 * @param employeeId 社員ID
	 * @return 締め開始日
	 */
	public Optional<GeneralDate> algorithm(String employeeId){
		
		this.employeeId = employeeId;
		
		// ログインしている会社ID　取得
		LoginUserContext loginUserContext = AppContexts.user();
		String companyId = loginUserContext.companyId();
		
		// 全締めの当月と期間を取得する
		this.closureInfos = this.closureService.getAllClosureInfo();
		
		// チェック開始日・終了日を計算
		this.calcCheckStartAndEnd();
		if (this.checkStart == null || this.checkEnd == null) return Optional.empty();

		// 「検索開始日」←「チェック開始日」
		GeneralDate searchStart = this.checkStart;
		while (true){

			// 「所属雇用履歴」を取得する
			val bsEmploymentHistOpt = this.shareEmploymentAdapter.findEmploymentHistory(
					companyId, employeeId, searchStart);
			if (!bsEmploymentHistOpt.isPresent()) break;
			val bsEmploymentHist = bsEmploymentHistOpt.get();
			val employmentCd = bsEmploymentHist.getEmploymentCode();
			
			// 雇用に紐づく締めを取得する
			Integer closureId = 1;
			val closureEmploymentOpt = this.closureEmploymentRepo.findByEmploymentCD(companyId, employmentCd);
			if (closureEmploymentOpt.isPresent()){
				val closureEmployment = closureEmploymentOpt.get();
				closureId = closureEmployment.getClosureId();
			}
			
			// 締め情報．期間を取得
			ClosureInfo targetClosureInfo = null;
			for (val closureInfo : this.closureInfos){
				if (closureInfo.getClosureId().value == closureId){
					targetClosureInfo = closureInfo;
					break;
				}
			}
			if (targetClosureInfo == null) break;
			
			// 既に締められた雇用履歴かチェック
			if (bsEmploymentHist.getPeriod().end().afterOrEquals(targetClosureInfo.getPeriod().start())){
				// 雇用履歴の途中まで締められたかチェック
				if (bsEmploymentHist.getPeriod().start().before(targetClosureInfo.getPeriod().start())){
					// チェック開始日　←　「締め情報．期間．開始日」
					this.checkStart = targetClosureInfo.getPeriod().start();
				}
			}
			else {
				// チェック開始日　←　「所属雇用履歴．期間．終了日」の翌日
				this.checkStart = bsEmploymentHist.getPeriod().end().addDays(1);
			}
			
			// チェック終了日までチェックが完了したかチェック
			if (this.checkEnd.beforeOrEquals(bsEmploymentHist.getPeriod().end())) break;
			
			// 検索開始日　←　所属雇用履歴．期間．終了日の翌日
			searchStart = bsEmploymentHist.getPeriod().end().addDays(1);
		}
		
		// チェック開始日を返す
		return Optional.ofNullable(this.checkStart);
	}
	
	/**
	 * チェック開始日・終了日を計算
	 */
	private void calcCheckStartAndEnd(){

		this.checkStart = null;
		this.checkEnd = null;
		for (val closureInfo : this.closureInfos){
			
			// 締め情報のうち、一番早い「期間．開始日」を取得
			val targetPeriod = closureInfo.getPeriod();
			if (this.checkStart == null){
				this.checkStart = targetPeriod.start();
			}
			else if (this.checkStart.after(targetPeriod.start())){
				this.checkStart = targetPeriod.start();
			}
			
			// 締め情報のうち、一番遅い「期間．終了日」を取得
			if (this.checkEnd == null){
				this.checkEnd = closureInfo.getPeriod().end();
			}
			else if (this.checkEnd.before(targetPeriod.end())){
				this.checkEnd = closureInfo.getPeriod().end();
			}
		}
		
		// 「社員」を取得する
		val employee = this.empEmployee.findByEmpId(this.employeeId);
		if (employee != null){
			
			// 「チェック開始日」と「入社年月日」を比較
			if (this.checkStart.before(employee.getEntryDate())) this.checkStart = employee.getEntryDate();
		}
	}
}
