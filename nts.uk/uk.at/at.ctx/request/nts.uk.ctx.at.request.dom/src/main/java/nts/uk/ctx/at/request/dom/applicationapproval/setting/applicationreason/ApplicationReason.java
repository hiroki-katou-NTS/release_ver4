package nts.uk.ctx.at.request.dom.applicationapproval.setting.applicationreason;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.at.request.dom.applicationapproval.application.ApplicationType;

/**
 * 申請定型理由
 * @author dudt
 *
 */
@Getter
@AllArgsConstructor
public class ApplicationReason extends AggregateRoot {
	/**
	 * 会社Iｄ
	 */
	public String companyId;
	/**
	 * 申請種類
	 */
	public ApplicationType appType;
	
	/** 理由ID */
	public String reasonID;
	/**
	 * 表示順
	 */
	public int dispOrder;
	
	/** 定型理由 */
	public String reasonTemp;
	/**
	 * 既定
	 */
	public DefaultFlg defaultFlg;
	
	public static ApplicationReason createSimpleFromJavaType(String companyId,
			int appType, String reasonID, 
			int dispOrder, String reasonTemp,
			int defaultFlg) {
				return new ApplicationReason(companyId, 
						EnumAdaptor.valueOf(appType, ApplicationType.class), 
						reasonID,
						dispOrder,
						reasonTemp,
						EnumAdaptor.valueOf(defaultFlg, DefaultFlg.class));
		
	}
}
