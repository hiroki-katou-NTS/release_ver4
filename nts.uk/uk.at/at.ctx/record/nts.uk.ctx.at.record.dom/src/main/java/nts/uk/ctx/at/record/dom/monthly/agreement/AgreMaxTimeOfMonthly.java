package nts.uk.ctx.at.record.dom.monthly.agreement;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.RepositoriesRequiredByMonthlyAggr;
import nts.uk.ctx.at.record.dom.standardtime.primitivevalue.LimitOneMonth;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.ctx.at.shared.dom.monthly.agreement.AgreMaxTimeStatusOfMonthly;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;

/**
 * 月別実績の36協定上限時間
 * @author shuichi_ishida
 */
@Getter
public class AgreMaxTimeOfMonthly {

	/** 36協定時間 */
	@Setter
	private AttendanceTimeMonth agreementTime;
	/** 上限時間 */
	private LimitOneMonth maxTime;
	/** 状態 */
	private AgreMaxTimeStatusOfMonthly status;

	/**
	 * コンストラクタ
	 */
	public AgreMaxTimeOfMonthly(){
		
		this.agreementTime = new AttendanceTimeMonth(0);
		this.maxTime = new LimitOneMonth(0);
		this.status = AgreMaxTimeStatusOfMonthly.NORMAL;
	}
	
	/**
	 * ファクトリー
	 * @param agreementTime 36協定時間
	 * @param maxTime 上限時間
	 * @param status 状態
	 * @return 月別実績の36協定上限時間
	 */
	public static AgreMaxTimeOfMonthly of(
			AttendanceTimeMonth agreementTime,
			LimitOneMonth maxTime,
			AgreMaxTimeStatusOfMonthly status){

		AgreMaxTimeOfMonthly domain = new AgreMaxTimeOfMonthly();
		domain.agreementTime = agreementTime;
		domain.maxTime = maxTime;
		domain.status = status;
		return domain;
	}
	
	/**
	 * 上限時間の取得
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param criteriaDate 基準日
	 * @param workingSystem 労働制
	 * @param repositories 月次集計が必要とするリポジトリ
	 */
	public void setMaxTime(
			String companyId,
			String employeeId,
			GeneralDate criteriaDate,
			WorkingSystem workingSystem,
			RepositoriesRequiredByMonthlyAggr repositories){
		
		// 初期設定
		this.maxTime = new LimitOneMonth(0);
		
		// 「36協定基本設定」を取得する
		val basicAgreementSet = repositories.getAgreementDomainService().getBasicSet(
				companyId, employeeId, criteriaDate, workingSystem).getBasicAgreementSetting();
		
		// 月間の値を取得し、上限時間とする
		this.maxTime = new LimitOneMonth(basicAgreementSet.getLimitOneMonth().v());
	}
	
	/**
	 * エラーチェック
	 */
	public void errorCheck(){
		
		// チェック処理
		if (this.agreementTime.v() >= this.maxTime.v()) {
			this.status = AgreMaxTimeStatusOfMonthly.EXCESS_MAXTIME;
			return;
		}
		
		this.status = AgreMaxTimeStatusOfMonthly.NORMAL;
	}
	
	/**
	 * 合算する
	 * @param target 加算対象
	 */
	public void sum(AgreMaxTimeOfMonthly target){

		this.agreementTime = this.agreementTime.addMinutes(target.agreementTime.v());
	}
}
