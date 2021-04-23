package nts.uk.ctx.sys.gateway.dom.tenantlogin;

import lombok.Getter;
import lombok.val;
import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.gul.security.hash.password.PasswordHash;

/**
 * テナント認証
 */
public class TenantAuthenticate {
	
	/** テナントコード（契約コード） */
	@Getter
	private final String tenantCode;
	
	/** 認証用パスワード（テナントコードをsaltとしてハッシュ化済み） */
	@Getter
	private String hashedPassword;
	
	/** 利用期間 */
	@Getter
	private DatePeriod availablePeriod;

	public TenantAuthenticate(String tenantCode, String hashedPassword, DatePeriod availablePeriod) {
		this.tenantCode = tenantCode;
		this.hashedPassword = hashedPassword;
		this.availablePeriod = availablePeriod;
	}
	
	/**
	 * 作成する
	 * @param tenantCode
	 * @param passwordPlainText
	 * @return
	 */
	public static TenantAuthenticate create(String tenantCode, String passwordPlainText, GeneralDate startDate) {
		
		String hashedPassword = PasswordHash.generate(passwordPlainText, tenantCode);
		val availablePeriod = new DatePeriod(startDate, GeneralDate.max());
		
		return new TenantAuthenticate(tenantCode, hashedPassword, availablePeriod);
	}
	
	/**
	 * パスワードを検証する
	 * @param String password
	 * @return
	 */
	public boolean verifyPassword(String passwordPlainText) {
		return PasswordHash.verifyThat(passwordPlainText, tenantCode)
				.isEqualTo(hashedPassword);
	}
	
	/**
	 * 有効期限をチェックする
	 * @param GeneralDate
	 * @return
	 */
	public boolean isAvailableAt(GeneralDate date) {
		return this.availablePeriod.contains(date);
	}
}
