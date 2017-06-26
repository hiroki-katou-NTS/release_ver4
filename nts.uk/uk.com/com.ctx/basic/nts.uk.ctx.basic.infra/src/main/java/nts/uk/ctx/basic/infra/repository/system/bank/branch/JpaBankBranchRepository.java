package nts.uk.ctx.basic.infra.repository.system.bank.branch;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.basic.dom.system.bank.BankCode;
import nts.uk.ctx.basic.dom.system.bank.branch.BankBranch;
import nts.uk.ctx.basic.dom.system.bank.branch.BankBranchRepository;
import nts.uk.ctx.basic.infra.entity.system.bank.branch.CbkmtBranch;
import nts.uk.ctx.basic.infra.entity.system.bank.branch.CbkmtBranchPK;

@Stateless
public class JpaBankBranchRepository extends JpaRepository implements BankBranchRepository{
	public static String SEL_1 = "SELECT br FROM CbkmtBranch br WHERE br.ckbmtBranchPK.companyCode = :companyCode AND br.bankCode = :bankCode AND br.branchCode = :branchCode";
    public static String SEL_2 = "SELECT br FROM CbkmtBranch br WHERE br.ckbmtBranchPK.companyCode = :companyCode AND br.bankCode = :bankCode";
    public static String SEL_3 = "SELECT br FROM CbkmtBranch br WHERE br.ckbmtBranchPK.companyCode = :companyCode ORDER BY br.branchCode";
	
    @Override
    public Optional<BankBranch> find(String companyCode, String branchId) {
    	CbkmtBranchPK key = new CbkmtBranchPK(companyCode, branchId);
    	return this.queryProxy().find(key, CbkmtBranch.class)
    			.map(x -> BankBranch.createFromJavaType(companyCode, branchId, x.bankCode, x.branchCode, x.branchName, x.branchKnName, x.memo));
    }
    
    @Override
	public List<BankBranch> findAll(String companyCode,BankCode bankCode) {
         return this.queryProxy().query(SEL_2, CbkmtBranch.class)
        		 .setParameter("companyCode", companyCode)
        		 .setParameter("bankCode", bankCode)
        		 .getList(x -> BankBranch.createFromJavaType(
        				 x.ckbmtBranchPK.companyCode,
        				 x.ckbmtBranchPK.branchId,
        				 x.bankCode,
        				 x.branchCode, 
        				 x.branchName, 
        				 x.branchKnName, 
        				 x.memo));
	}

	@Override
	public void add(BankBranch bank) {
		this.commandProxy().insert(toEntity(bank));
		
	}

	@Override
	public void update(BankBranch bank) {
		this.commandProxy().update(toEntity(bank));
		
	}
	
	@Override
	public void update(String bankNewCode, String companyCode, String branchId) {
		CbkmtBranchPK key = new CbkmtBranchPK(companyCode, branchId);
		CbkmtBranch entity = this.getEntityManager().find(CbkmtBranch.class, key);
		entity.bankCode = bankNewCode;
		this.commandProxy().update(entity);
	}

	@Override
	public void remove(String companyCode, String branchId) {
		CbkmtBranchPK key = new CbkmtBranchPK(companyCode, branchId);
		this.commandProxy().remove(CbkmtBranch.class, key);
		
	}
	
	@Override
	public void removeAll(String companyCode, List<String> branchIdList) {
		List<CbkmtBranchPK> lstCbkmtBranchPK =  branchIdList.stream().map(x -> new CbkmtBranchPK(companyCode, x)).collect(Collectors.toList());
		this.commandProxy().removeAll(CbkmtBranch.class, lstCbkmtBranchPK);
		this.getEntityManager().flush();
	}
	
	@Override
	public boolean checkExists(String companyCode, String bankCode, String branchCode) {
		List<CbkmtBranch> branchs = this.queryProxy().query(SEL_1, CbkmtBranch.class)
       		 .setParameter("companyCode", companyCode)
       		 .setParameter("bankCode", bankCode)
       		 .setParameter("branchCode", branchCode)
       		 .getList();
		
		return !branchs.isEmpty();
	}
	
	/**
	 * Convert domain to entity
	 * @param domain  Convert domain to entity
	 * @return CbkmtBranch
	 */
	private static CbkmtBranch toEntity(BankBranch domain){
		CbkmtBranchPK key = new CbkmtBranchPK(domain.getCompanyCode(), domain.getBranchId().toString());
		CbkmtBranch entity = new CbkmtBranch(key, domain.getBankCode(), domain.getBankBranchCode().v(), domain.getBankBranchName().v(), domain.getBankBranchNameKana().v(), domain.getMemo().v());		
		return entity;
	}

	@Override
	public List<BankBranch> findAll(String companyCode) {
		return this.queryProxy().query(SEL_3, CbkmtBranch.class)
       		 .setParameter("companyCode", companyCode)
       		 .getList(x -> BankBranch.createFromJavaType(
       				 x.ckbmtBranchPK.companyCode,
       				 x.ckbmtBranchPK.branchId,
       				 x.bankCode,
       				 x.branchCode, 
       				 x.branchName, 
       				 x.branchKnName, 
       				 x.memo));
	}

}
