package nts.uk.ctx.at.request.infra.entity.setting.requestofearch;

import java.io.Serializable;

import javax.persistence.Column;

public class KrqstWpAppConfigDetailPK  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**会社ID*/
	@Column(name = "CID")
	public String companyId;
	/**
	 * 職場ID
	 */
	@Column(name = "WKP_ID")
	public String workplaceId;
	/**申請種類*/
	@Column(name = "APP_TYPE")
	public int appType;
}
