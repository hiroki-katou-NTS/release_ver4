package nts.uk.ctx.at.record.dom.monthlyprocess.aggr.export;

import lombok.Getter;
import nts.uk.ctx.at.record.dom.monthly.agreement.AgreementTimeOfMonthly;

/**
 * 36協定時間一覧
 * @author shuichu_ishida
 */
@Getter
public class AgreementTimeDetail {

	/** 社員ID */
	private String employeeId;
	/** 確定情報 */
	private AgreementTimeOfMonthly confirmed;
	/** 申請反映後情報 */
	private AgreementTimeOfMonthly afterAppReflect;
	
	/**
	 * コンストラクタ
	 * @param employeeId 社員ID
	 */
	public AgreementTimeDetail(String employeeId){
		
		this.employeeId = employeeId;
		this.confirmed = new AgreementTimeOfMonthly();
		this.afterAppReflect = new AgreementTimeOfMonthly();
	}
	
	/**
	 * ファクトリー
	 * @param employeeId 社員ID
	 * @param confirmed 確定情報
	 * @param afterAppReflect 申請反映後情報
	 * @return 36協定時間一覧
	 */
	public static AgreementTimeDetail of(
			String employeeId,
			AgreementTimeOfMonthly confirmed,
			AgreementTimeOfMonthly afterAppReflect){
		
		AgreementTimeDetail domain = new AgreementTimeDetail(employeeId);
		domain.confirmed = confirmed;
		domain.afterAppReflect = afterAppReflect;
		return domain;
	}
}
