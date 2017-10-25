package nts.uk.ctx.at.request.dom.setting.company.divergencereason;

import lombok.AllArgsConstructor;
import lombok.Data;
import nts.uk.ctx.at.request.dom.setting.applicationreason.DefaultFlg;

@Data
@AllArgsConstructor
public class ReasonTypeItem {
	/** 理由ID */
	public String reasonID;
	/**
	 * 表示順
	 */
	public int dispOrder;
	
	/** 定型理由 */
	public ReasonTempPrimitive reasonTemp;
	/**
	 * 既定
	 */
	public DefaultFlg defaultFlg;

}
