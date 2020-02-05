package nts.uk.ctx.at.request.dom.setting.workplace;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.UseAtr;

/**
 * 申請利用設定
 * @author Doan Duy Hung
 *
 */
@Getter
@AllArgsConstructor
public class ApplicationUseSetting extends DomainObject {
	
	/**
	 * 備考
	 */
	private AppUseSetRemark memo;
	
	/**
	 * 利用区分
	 */
	private UseAtr userAtr;
	
	/**
	 * 申請種類
	 */
	private ApplicationType appType;
}
