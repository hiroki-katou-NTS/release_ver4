package nts.uk.ctx.bs.employee.dom.employee.history;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class AffCompanyHistService {
	@Inject
	private AffCompanyHistRepository affCompanyHistRepository;
	
	/** Add new affiliation history */
//	public void add(AffCompanyHistByEmployee domain, String pId){
//		// Insert last item
//		AffCompanyHistItem itemToBeAdded = domain.getLstAffCompanyHistoryItem().get(domain.getLstAffCompanyHistoryItem().size() -1);
//		affCompanyHistRepository.add(domain.getSId(), pId, itemToBeAdded);
//	}
	public void addToMerge(AffCompanyHistByEmployee domain, String pId, AffCompanyInfo newComInfo){
		// Insert last item
		AffCompanyHistItem itemToBeAdded = domain.getLstAffCompanyHistoryItem().get(domain.getLstAffCompanyHistoryItem().size() -1);
		affCompanyHistRepository.addToMerge(domain.getSId(), pId, itemToBeAdded, newComInfo);
	}
	/** Update one affiliation history */
	public void update(AffCompanyHistByEmployee domain, AffCompanyHistItem itemToBeUpdated){

		affCompanyHistRepository.update(itemToBeUpdated);
	}
	
	/** Delete one affiliation history */
	public void delete(AffCompanyHistByEmployee domain, AffCompanyHistItem itemToBeDelted, String pId){
		affCompanyHistRepository.remove(pId,domain.getSId(),itemToBeDelted.getHistoryId());
	}
}
