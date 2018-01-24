package nts.uk.ctx.at.request.dom.setting.workplace;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.at.request.dom.application.UseAtr;
/**
 * 指示利用設定
 * @author dudt
 *
 */
@Getter
@AllArgsConstructor
public class InstructionUseSetting extends DomainObject{
	/**
	 * 指示区分
	 */
	public UseAtr instructionAtr;
	/**
	 * 備考
	 */
	public Memo instructionRemarks;
	/**
	 * 利用区分
	 */
	public UseAtr instructionUseDivision;
}
