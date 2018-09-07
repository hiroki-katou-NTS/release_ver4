package nts.uk.ctx.pr.core.app.find.wageprovision.processdatecls;

import lombok.AllArgsConstructor;
import lombok.Value;
import nts.uk.ctx.pr.core.dom.wageprovision.processdatecls.ValPayDateSet;

/**
 * 支払日の設定の規定値
 */
@AllArgsConstructor
@Value
public class ValPayDateSetDto {

	/**
	 * 会社ID
	 */
	private String cid;

	/**
	 * 処理区分NO
	 */
	private int processCateNo;

	public static ValPayDateSetDto fromDomain(ValPayDateSet domain) {
		return new ValPayDateSetDto(domain.getCid(), domain.getProcessCateNo());
	}

}
