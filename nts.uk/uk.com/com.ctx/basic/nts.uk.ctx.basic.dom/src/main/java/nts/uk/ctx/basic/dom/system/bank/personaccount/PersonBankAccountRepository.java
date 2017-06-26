package nts.uk.ctx.basic.dom.system.bank.personaccount;

import java.util.List;
import java.util.Optional;

public interface PersonBankAccountRepository {

	/**
	 * 
	 * @param companyCode
	 * @return
	 */
	List<PersonBankAccount> findAll(String companyCode);

	/**
	 * 
	 * @param companyCode
	 * @param personId
	 * @param baseYM
	 * @return
	 */
	Optional<PersonBankAccount> findBasePIdAndBaseYM(String companyCode, String personId, int baseYM);

	/**
	 * 
	 * @param conpanyCode
	 * @param personId
	 * @param historyID
	 * @return
	 */
	Optional<PersonBankAccount> find(String conpanyCode, String personId, String historyID);

	/**
	 * Get all person bank account by bank
	 * 
	 * @param companyCode
	 *            company code
	 * @param bankCode
	 *            bank code
	 * @return list person bank account
	 */
	List<PersonBankAccount> findAll(String companyCode, String branchId);

	/**
	 * Get all person bank account base on line bank code
	 * 
	 * @param companyCode
	 * @param lineBankCode
	 * @return
	 */
	List<PersonBankAccount> findAllLineBank(String companyCode, String lineBankCode);

	/**
	 * Check all person branch account by bank
	 * 
	 * @param companyCode
	 * @param bankCode
	 * @param branchCode
	 * @return
	 */
	List<PersonBankAccount> findAllBranchCode(String companyCode, String branchId);

	/**
	 * Check exists branch account
	 * 
	 * @param companyCode
	 * @param branchId
	 * @return
	 */
	boolean checkExistsBranchAccount(String companyCode, List<String> branchId);

	/**
	 * Check exists line bank account
	 * 
	 * @param companyCode
	 * @param lineBankCode
	 * @return
	 */
	boolean checkExistsLineBankAccount(String companyCode, List<String> lineBankCode);

	/**
	 * 
	 * @param personBankAccount
	 */
	void update(PersonBankAccount personBankAccount);

	/**
	 * Find all personal bank account
	 * 
	 * @param companyCode
	 *            company code
	 * @param branchId
	 *            branch id
	 * @return list of personal bank account
	 */
	List<PersonBankAccount> findAllBranch(String companyCode, List<String> branchId);
}
