package nts.uk.ctx.basic.dom.system.bank;

import java.util.List;
import java.util.Optional;

public interface BankRepository {

	/**
	 * Get list of bank
	 * @param companyCode
	 * @return
	 */
	List<Bank> findAll(String companyCode);
	
	/**
	 * Find bank
	 * @param companyCode
	 * @param bankCode
	 * @return
	 */
	Optional<Bank> find(String companyCode, String bankCode);

	/**
	 * Add new Bank
	 * @param bank
	 */
	void add(Bank bank);
	
	/**
	 * Update Bank information
	 * @param bank
	 */
	void update(Bank bank);
	
	/**
	 * Remove a bank
	 * @param companyCode
	 * @param bankCode
	 */
	void remove(String companyCode, String bankCode);
}
