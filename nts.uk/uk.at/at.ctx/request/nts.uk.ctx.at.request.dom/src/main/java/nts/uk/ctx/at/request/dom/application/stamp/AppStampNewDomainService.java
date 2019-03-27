package nts.uk.ctx.at.request.dom.application.stamp;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.ProcessResult;
import nts.uk.ctx.at.request.dom.application.stamp.output.AppStampNewPreOutput;
/**
 * 
 * @author Doan Duy Hung
 *
 */
public interface AppStampNewDomainService {
	
	// 打刻申請（新規）起動前処理
	public AppStampNewPreOutput appStampPreProcess(String companyID, String employeeID, GeneralDate appDate);
	
	// 外出／育児／介護の申請の新規登録
	public ProcessResult appStampRegister(String applicationReason, AppStamp appStamp, boolean checkOver1Year);
}
