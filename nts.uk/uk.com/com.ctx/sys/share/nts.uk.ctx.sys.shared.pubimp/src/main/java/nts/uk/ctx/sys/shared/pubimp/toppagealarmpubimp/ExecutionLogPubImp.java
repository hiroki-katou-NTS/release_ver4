package nts.uk.ctx.sys.shared.pubimp.toppagealarmpubimp;

import java.util.Optional;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.sys.shared.dom.toppagealarm.TopPageAlarmDetail;
import nts.uk.ctx.sys.shared.dom.toppagealarm.TopPageAlarmRepository;
import nts.uk.ctx.sys.shared.dom.toppagealarmset.TopPageAlarmSet;
import nts.uk.ctx.sys.shared.dom.toppagealarmset.TopPageAlarmSetRepository;
import nts.uk.ctx.sys.shared.pub.toppagealarmpub.ExecutionLogErrorDetail;
import nts.uk.ctx.sys.shared.pub.toppagealarmpub.ExecutionLogImport;
import nts.uk.ctx.sys.shared.pub.toppagealarmpub.ExecutionLogPub;

@Stateless
public class ExecutionLogPubImp implements ExecutionLogPub{
	@Inject
	private TopPageAlarmRepository topPageAlarmRepository;
	
	@Inject 
	private TopPageAlarmSetRepository topPageAlarmSetRepository;
	
	@Override
	public void updateExecuteLog(ExecutionLogImport param) {
		boolean check = this.checkTopPage(param);
		// Nếu hàm checkTopPage trả ra false => return
		if(!check){
			return;
		}
		// Nếu không có errors thì insert vào toppage
		if(param.getExistenceError() == 0){
			for(String item : param.getManagerId()){
				String executionLogId = UUID.randomUUID().toString();
				topPageAlarmRepository.insertTopPage(executionLogId, item, param.getExecutionContent(), param.getIsCancelled(), param.getExistenceError());
			}
		}else{
			int countUp = 1;
			for(String item : param.getManagerId()){
				String executionLogId = UUID.randomUUID().toString();
				topPageAlarmRepository.insertTopPage(executionLogId, item, param.getExecutionContent(), param.getIsCancelled(), param.getExistenceError());
				for(ExecutionLogErrorDetail obj: param.getTargerEmployee()){
					TopPageAlarmDetail domainInsert = TopPageAlarmDetail.createFromJavaType(executionLogId, countUp, obj.getErrorMessage(), obj.getTargerEmployee());
					topPageAlarmRepository.insertDetail(domainInsert);
					countUp = countUp + 1;
				}
			}
		}
	}
	
	public boolean checkTopPage(ExecutionLogImport param){
		// get top page set by companyId and execution content
		Optional<TopPageAlarmSet> getTopPageSet = topPageAlarmSetRepository.getByAlarmCategory(param.getCompanyId(), param.getExecutionContent());
		// Nếu lấy được toppage nhưng trường use của option này trong domain toppage set = not use => return false;
		if(getTopPageSet.isPresent() && getTopPageSet.get().getUseAtr().value == 0){
			return false;
		}
		return true;
	}
}
