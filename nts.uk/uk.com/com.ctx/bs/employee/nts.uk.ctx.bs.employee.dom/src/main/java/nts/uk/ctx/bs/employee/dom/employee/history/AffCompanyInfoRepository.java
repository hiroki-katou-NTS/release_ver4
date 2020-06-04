package nts.uk.ctx.bs.employee.dom.employee.history;

public interface AffCompanyInfoRepository {

//	 Merge BSYMT_AFF_COM_HIST To BSYMT_AFF_COM_INFO  because response
//	 new Insert Method ↓
//	         ClassName  : AffCompanyHistRepositoryImp
//	         MethodName : addToMerge 
//	public void add(AffCompanyInfo domain);
	
	public void update(AffCompanyInfo domain);
	
	public void remove(AffCompanyInfo domain);
	public void remove(String histId);
	
	public AffCompanyInfo getAffCompanyInfoByHistId(String histId);

}
