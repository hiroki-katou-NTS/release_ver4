package nts.uk.ctx.at.request.infra.entity.setting.requestofearch;

import java.io.Serializable;

import javax.persistence.Column;

public class KrqstComAppConfigDetailPK implements Serializable{
	/**会社ID*/
	@Column(name = "CID")
	public String companyId;
	/**申請種類*/
	@Column(name = "APP_TYPE")
	public int appType;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
