package nts.uk.ctx.at.record.dom.divergencetime.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.divergencetime.DivergenceReason;
import nts.uk.ctx.at.record.dom.divergencetime.DivergenceTimeRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class CheckSelectReason implements DivergenceReasonService{
	@Inject
	private DivergenceTimeRepository divTimeRepo;
	
	String companyId = AppContexts.user().companyId();

	@Override
	public boolean isExit(int selectUseSet, int divTimeId) {
		if(selectUseSet==0){
			return true;
		}else{
			List<DivergenceReason> lst = this.divTimeRepo.getDivReasonByCode(companyId, Integer.valueOf(divTimeId));
			if(lst.isEmpty()){
				return false;
			}else{
				return true;
			}
		}
	}
}
