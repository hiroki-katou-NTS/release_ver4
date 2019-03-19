package nts.uk.ctx.at.record.dom.workrecord.identificationstatus;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.enums.SelfConfirmError;
import nts.uk.ctx.at.shared.dom.common.CompanyId;

/**
 * 
 * @author thanhnx 本人確認処理の利用設定
 *
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class IdentityProcessUseSet extends AggregateRoot {
	/**
	 * 会社ID
	 */
	private CompanyId companyId;
	
	/**
	 * 日の本人確認を利用する
	 */
	private boolean useConfirmByYourself;
	
	/**
	 * 月の本人確認を利用する
	 */
	private boolean useIdentityOfMonth;
	
	/**
	 * エラーがある場合の日の本人確認
	 */
	private Optional<SelfConfirmError> yourSelfConfirmError;
	
	public static IdentityProcessUseSet createFromJavaType(CompanyId cid, boolean useConfirmByYourself, boolean useIdentityOfMonth,
			Optional<SelfConfirmError> yourSelfConfirmError) {
		return new IdentityProcessUseSet(cid, useConfirmByYourself, useIdentityOfMonth, yourSelfConfirmError);
	}
}
