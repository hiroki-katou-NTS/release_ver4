package nts.uk.ctx.basic.dom.system.bank.branch;

import java.util.List;
import java.util.Optional;

import nts.uk.ctx.basic.dom.system.bank.BankCode;

public interface BankBranchRepository {
	
	/**
	 * Find bank branch
	 * @param companyCode
	 * @param branchId
	 * @return
	 */
	Optional<BankBranch> find(String companyCode, String branchId);
	
    /**
     * Get list of bank code
     * @param bankCode
     * @return
     */
	List<BankBranch> findAll(String companyCode,BankCode bankCode);
	
	/**
     * Get list 
     * @param bankCode
     * @return
     */
	List<BankBranch> findAll(String companyCode);
	
	/**
	 * Add new bank branch
	 * @param bank branch
	 */
    
	void add(BankBranch bank);
	
    /**
     * Update bank branch information	
     * @param branchId branch
     */
	void update(BankBranch bank);
	
	void update(String bankNewCode,  String companyCode, String branchId);
	
	/**
	 * Remove a bank branch
	 * @param branchId branch
	 */
	void remove(String companyCode, String branchId);
	
	/**
	 * Remove a list bank branch
	 * @param companyCode
	 * @param branchId
	 */
	void removeAll(String companyCode, List<String> branchIdList);

	/**
	 * Check exist bank
	 * @param companyCode
	 * @param bankCode
	 * @param branchCode
	 * @return
	 */
	boolean checkExists(String companyCode, String bankCode, String branchCode);
}
