package nts.uk.ctx.sys.gateway.dom.securitypolicy.password;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.val;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.sys.gateway.dom.loginold.ContractCode;
import nts.uk.ctx.sys.gateway.dom.securitypolicy.password.changelog.PasswordChangeLog;
import nts.uk.ctx.sys.gateway.dom.securitypolicy.password.complexity.PasswordComplexityRequirement;
import nts.uk.ctx.sys.gateway.dom.securitypolicy.password.validate.ValidationResultOnLogin;
import nts.uk.ctx.sys.shared.dom.user.password.PassStatus;

@Getter
/**
 * パスワードポリシー
 */
public class PasswordPolicy extends AggregateRoot {
	
	// テナントコード
	private final ContractCode contractCode;
	
	// 利用する
	private boolean isUse;
	
	// 複雑さ
	private PasswordComplexityRequirement complexityRequirement;
	
	// 変更履歴回数
	private PasswordHistoryCount historyCount;
	
	// 有効期限
	private PasswordValidityPeriod validityPeriod;
	
	// 期限切れ逼迫通知
	private NotificationPasswordChange notificationPasswordChange;
	
	// 初回ログイン時パスワード変更
	private boolean initialPasswordChange;
	
	// ログイン時にポリシーチェック実施
	private boolean loginCheck;

	public PasswordPolicy(ContractCode contractCode, NotificationPasswordChange notificationPasswordChange,
			boolean loginCheck, boolean initialPasswordChange, boolean isUse, PasswordHistoryCount historyCount,
			PasswordValidityPeriod validityPeriod, PasswordComplexityRequirement complexityRequirement) {
		super();
		this.contractCode = contractCode;
		this.notificationPasswordChange = notificationPasswordChange;
		this.loginCheck = loginCheck;
		this.initialPasswordChange = initialPasswordChange;
		this.isUse = isUse;
		this.historyCount = historyCount;
		this.validityPeriod = validityPeriod;
		this.complexityRequirement = complexityRequirement;
	}

	public static PasswordPolicy createFromJavaType(String contractCode, int notificationPasswordChange,
			boolean loginCheck, boolean initialPasswordChange, boolean isUse, int historyCount, 
			int validityPeriod, PasswordComplexityRequirement complexityRequirement) {
		return new PasswordPolicy(new ContractCode(contractCode),
				new NotificationPasswordChange(new BigDecimal(notificationPasswordChange)), loginCheck,
				initialPasswordChange, isUse, new PasswordHistoryCount(new BigDecimal(historyCount)),
				new PasswordValidityPeriod(new BigDecimal(validityPeriod)),
				complexityRequirement);

	}

	/**
	 * ログイン時にポリシー違反してないか
	 */
	public ValidationResultOnLogin violatedOnLogin(ValidateOnLoginRequire require,
			String userId,
			String password,
			PassStatus passwordStatus) {
		
		// ポリシー利用しない
		if (!isUse) {
			return ValidationResultOnLogin.ok();
		}

		// パスワードリセット
		if (passwordStatus.equals(PassStatus.Reset)) {
			return ValidationResultOnLogin.reset();
		}
		
		// 初期パスワード
		if (initialPasswordChange && passwordStatus.equals(PassStatus.InitPassword)) {
			return ValidationResultOnLogin.initial();
		}
		
		// 文字構成をチェック
		val errorList = complexityRequirement.validatePassword(password);
		if (loginCheck && errorList.size() > 0) {
			return ValidationResultOnLogin.complexityError(errorList);
		}
		
		// 有効期限をチェック
		if (!validityPeriod.isUnlimited()) {
			int remainingDays = calculateRemainingDays(require, userId);
			
			// 有効期限切れ
			if (remainingDays < 0) {
				return ValidationResultOnLogin.expired();
			}
			
			// 期限切れが近い
			if (notificationPasswordChange.needsNotify(remainingDays)) {
				return ValidationResultOnLogin.expiresSoon(remainingDays);
			}
		}
		// 問題なし
		return ValidationResultOnLogin.ok();
	}
	
	/**
	 * 有効期限が切れるまでの残日数を求める
	 * @return 有効期限までの日数
	 */
	private int calculateRemainingDays(ValidateOnLoginRequire require, String userId) {
		
		val changeLog = require.getPasswordChangeLog(userId);
		if(changeLog.latestLog().isPresent()) {
			// 前回変更してからの日数
			int ageInDays = changeLog.latestLog().get().ageInDays();
			// 有効日数から上の日数を引く
			return validityPeriod.v().intValue() - ageInDays;
		}
		// TODO 「初期パスワードに変更履歴が作成されない問題」のため変更履歴がない場合はチェックを回避
		// ※通知するかどうかの日数がMAX99のため100で回避
		return 100;
	}
	
	public static interface ValidateOnLoginRequire {
		
		PasswordChangeLog getPasswordChangeLog(String userId);
	}
}
