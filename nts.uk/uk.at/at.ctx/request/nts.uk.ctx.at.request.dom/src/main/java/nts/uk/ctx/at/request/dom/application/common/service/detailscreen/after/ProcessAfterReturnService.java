package nts.uk.ctx.at.request.dom.application.common.service.detailscreen.after;

import nts.uk.ctx.at.request.dom.application.common.Application;

/**
 * 11-2.詳細画面差し戻し後の処理
 * @author tutk
 *
 */
public interface ProcessAfterReturnService {
	public void detailScreenProcessAfterReturn(Application application, boolean checkApplicant,int orderPhase);
}
