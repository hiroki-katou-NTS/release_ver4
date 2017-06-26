package nts.uk.ctx.basic.infra.repository.system.bank.personaccount;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.basic.dom.system.bank.personaccount.PersonBankAccount;
import nts.uk.ctx.basic.dom.system.bank.personaccount.PersonBankAccountRepository;
import nts.uk.ctx.basic.dom.system.bank.personaccount.PersonUseSetting;
import nts.uk.ctx.basic.infra.entity.system.bank.personaccount.PbamtPersonBankAccount;
import nts.uk.ctx.basic.infra.entity.system.bank.personaccount.PbamtPersonBankAccountPK;

@Stateless
@Transactional
public class JpaPersonBankAccountRepository extends JpaRepository implements PersonBankAccountRepository {

	private final String SEL_1 = "SELECT a FROM PbamtPersonBankAccount a"
			+ " WHERE a.pbamtPersonBankAccountPK.companyCode = :companyCode" + " AND (a.toBranchId1 = :branchId"
			+ " OR a.toBranchId2 = :branchId" + " OR a.toBranchId3 = :branchId" + " OR a.toBranchId4 = :branchId"
			+ " OR a.toBranchId5 = :branchId)";

	private final String SEL_1_1 = "SELECT a FROM PbamtPersonBankAccount a"
			+ " WHERE a.pbamtPersonBankAccountPK.companyCode = :companyCode" + " AND (a.toBranchId1 IN :branchIdList"
			+ " OR a.toBranchId2 IN :branchIdList" + " OR a.toBranchId3 IN :branchIdList"
			+ " OR a.toBranchId4 IN :branchIdList" + " OR a.toBranchId5 IN :branchIdList)";

	private final String SEL_1_2 = "SELECT COUNT(a) FROM PbamtPersonBankAccount a"
			+ " WHERE a.pbamtPersonBankAccountPK.companyCode = :companyCode" + " AND (a.toBranchId1 IN :branchId"
			+ " OR a.toBranchId2 IN :branchId" + " OR a.toBranchId3 IN :branchId" + " OR a.toBranchId4 IN :branchId"
			+ " OR a.toBranchId5 IN :branchId)";

	private final String SEL_6 = "SELECT a FROM PbamtPersonBankAccount a"
			+ " WHERE a.pbamtPersonBankAccountPK.companyCode = :companyCode" + " AND (a.fromLineBankCd1 = :lineBankCode"
			+ " OR a.fromLineBankCd2 = :lineBankCode" + " OR a.fromLineBankCd3 = :lineBankCode"
			+ " OR a.fromLineBankCd4 = :lineBankCode" + " OR a.fromLineBankCd5 = :lineBankCode)";

	private final String SEL_6_1 = "SELECT COUNT(a) FROM PbamtPersonBankAccount a"
			+ " WHERE a.pbamtPersonBankAccountPK.companyCode = :companyCode"
			+ " AND (a.fromLineBankCd1 IN :lineBankCode" + " OR a.fromLineBankCd2 IN :lineBankCode"
			+ " OR a.fromLineBankCd3 IN :lineBankCode" + " OR a.fromLineBankCd4 IN :lineBankCode"
			+ " OR a.fromLineBankCd5 IN :lineBankCode)";

	private final String SEL_BY_BANKANDBRANCH = "SELECT a FROM PbamtPersonBankAccount a"
			+ " WHERE a.pbamtPersonBankAccountPK.companyCode = :companyCode" + " AND (a.toBranchId1 = :branchId"
			+ " OR a.toBranchId2 = :branchId" + " OR a.toBranchId3 = :branchId" + " OR a.toBranchId4 = :branchId"
			+ " OR a.toBranchId5 = :branchId)";

	private final String SEL_7 = "SELECT a FROM PbamtPersonBankAccount a"
			+ " WHERE a.pbamtPersonBankAccountPK.companyCode = :companyCode"
			+ " AND a.pbamtPersonBankAccountPK.personId = :personId" + " AND a.startYearMonth <= :baseYM"
			+ " AND a.endYearMonth >= :baseYM" + " AND ((a.useSet1 = 1" + " AND a.paymentMethod1 = 0)"
			+ " OR (a.useSet3 = 1" + " AND a.paymentMethod3 = 0)" + " OR (a.useSet4 = 1" + " AND a.paymentMethod4 = 0)"
			+ " OR (a.useSet5 = 1" + " AND a.paymentMethod5 = 0))";

	@Override 
	public List<PersonBankAccount> findAll(String companyCode) {
		return null;
	}

	@Override
	public Optional<PersonBankAccount> findBasePIdAndBaseYM(String companyCode, String personId, int baseYM) {
		return this.queryProxy().query(SEL_7, PbamtPersonBankAccount.class).setParameter("companyCode", companyCode)
				.setParameter("personId", personId).setParameter("baseYM", baseYM).getSingle(x -> toDomain(x));
	}

	@Override
	public Optional<PersonBankAccount> find(String conpanyCode, String personId, String historyID) {
		PbamtPersonBankAccountPK key = new PbamtPersonBankAccountPK(conpanyCode, personId, historyID);
		return this.queryProxy().find(key, PbamtPersonBankAccount.class).map(x -> toDomain(x));
	}

	@Override
	public List<PersonBankAccount> findAll(String companyCode, String branchId) {
		return this.queryProxy().query(SEL_1, PbamtPersonBankAccount.class).setParameter("companyCode", companyCode)
				.setParameter("branchId", branchId).getList(x -> toDomain(x));
	}

	@Override
	public List<PersonBankAccount> findAllBranch(String companyCode, List<String> branchIdList) {
		return this.queryProxy().query(SEL_1_1, PbamtPersonBankAccount.class).setParameter("companyCode", companyCode)
				.setParameter("branchIdList", branchIdList).getList(x -> toDomain(x));
	}

	@Override
	public List<PersonBankAccount> findAllLineBank(String companyCode, String lineBankCode) {
		return this.queryProxy().query(SEL_6, PbamtPersonBankAccount.class).setParameter("companyCode", companyCode)
				.setParameter("lineBankCode", lineBankCode).getList(x -> toDomain(x));
	}

	/**
	 * Convert data entity to domain object
	 * 
	 * @param x
	 *            entity object
	 * @return domain object
	 */
	private PersonBankAccount toDomain(PbamtPersonBankAccount x) {
		return new PersonBankAccount(x.pbamtPersonBankAccountPK.companyCode, x.pbamtPersonBankAccountPK.personId,
				x.pbamtPersonBankAccountPK.historyId, x.startYearMonth, x.endYearMonth,
				new PersonUseSetting(x.useSet1, x.priorityOrder1, x.paymentMethod1, x.partialPaySet1, x.fixAmountMny1,
						x.fixRate1, x.fromLineBankCd1, x.toBranchId1, x.accountAtr1, x.accountNo1,
						x.accountHolderKnName1, x.accountHolderName1),
				new PersonUseSetting(x.useSet2, x.priorityOrder2, x.paymentMethod2, x.partialPaySet2, x.fixAmountMny2,
						x.fixRate2, x.fromLineBankCd2, x.toBranchId2, x.accountAtr2, x.accountNo2,
						x.accountHolderKnName2, x.accountHolderName2),
				new PersonUseSetting(x.useSet3, x.priorityOrder3, x.paymentMethod3, x.partialPaySet3, x.fixAmountMny3,
						x.fixRate3, x.fromLineBankCd3, x.toBranchId3, x.accountAtr3, x.accountNo3,
						x.accountHolderKnName3, x.accountHolderName3),
				new PersonUseSetting(x.useSet4, x.priorityOrder4, x.paymentMethod4, x.partialPaySet4, x.fixAmountMny4,
						x.fixRate4, x.fromLineBankCd4, x.toBranchId4, x.accountAtr4, x.accountNo4,
						x.accountHolderKnName4, x.accountHolderName4),
				new PersonUseSetting(x.useSet5, x.priorityOrder5, x.paymentMethod5, x.partialPaySet5, x.fixAmountMny5,
						x.fixRate5, x.fromLineBankCd5, x.toBranchId5, x.accountAtr5, x.accountNo5,
						x.accountHolderKnName5, x.accountHolderName5));
	}

	@Override
	public boolean checkExistsBranchAccount(String companyCode, List<String> branchId) {
		Optional<Long> numberOfPerson = this.queryProxy().query(SEL_1_2, Long.class)
				.setParameter("companyCode", companyCode).setParameter("branchId", branchId).getSingle();
		return numberOfPerson.get() > 0;
	}

	@Override
	public boolean checkExistsLineBankAccount(String companyCode, List<String> lineBankCode) {
		Optional<Long> numberOfPerson = this.queryProxy().query(SEL_6_1, Long.class)
				.setParameter("companyCode", companyCode).setParameter("lineBankCode", lineBankCode).getSingle();
		return numberOfPerson.get() > 0;
	}

	@Override
	public void update(PersonBankAccount personBankAccount) {
		this.commandProxy().update(toEntity(personBankAccount));
	}

	private static PbamtPersonBankAccount toEntity(PersonBankAccount domain) {
		PbamtPersonBankAccountPK key = new PbamtPersonBankAccountPK(domain.getCompanyCode(), domain.getPersonID(),
				domain.getHistId());
		PbamtPersonBankAccount entity = new PbamtPersonBankAccount(key, domain.getStartYearMonth(),
				domain.getEndYearMonth(), domain.getUseSet1().getUseSet(), domain.getUseSet1().getPriorityOrder(),
				domain.getUseSet1().getPaymentMethod(), domain.getUseSet1().getPartialPaySet(),
				domain.getUseSet1().getFixAmountMny(), domain.getUseSet1().getFixRate(),
				domain.getUseSet1().getFromLineBankCd(), domain.getUseSet1().getToBranchId(),
				domain.getUseSet1().getAccountAtr(), domain.getUseSet1().getAccountNo(),
				domain.getUseSet1().getAccountHolderKnName(), domain.getUseSet1().getAccountHolderName(),
				domain.getUseSet2().getUseSet(), domain.getUseSet2().getPriorityOrder(),
				domain.getUseSet2().getPaymentMethod(), domain.getUseSet2().getPartialPaySet(),
				domain.getUseSet2().getFixAmountMny(), domain.getUseSet2().getFixRate(),
				domain.getUseSet2().getFromLineBankCd(), domain.getUseSet2().getToBranchId(),
				domain.getUseSet2().getAccountAtr(), domain.getUseSet2().getAccountNo(),
				domain.getUseSet2().getAccountHolderKnName(), domain.getUseSet2().getAccountHolderName(),
				domain.getUseSet3().getUseSet(), domain.getUseSet3().getPriorityOrder(),
				domain.getUseSet3().getPaymentMethod(), domain.getUseSet3().getPartialPaySet(),
				domain.getUseSet3().getFixAmountMny(), domain.getUseSet3().getFixRate(),
				domain.getUseSet3().getFromLineBankCd(), domain.getUseSet3().getToBranchId(),
				domain.getUseSet3().getAccountAtr(), domain.getUseSet3().getAccountNo(),
				domain.getUseSet3().getAccountHolderKnName(), domain.getUseSet3().getAccountHolderName(),
				domain.getUseSet4().getUseSet(), domain.getUseSet4().getPriorityOrder(),
				domain.getUseSet4().getPaymentMethod(), domain.getUseSet4().getPartialPaySet(),
				domain.getUseSet4().getFixAmountMny(), domain.getUseSet4().getFixRate(),
				domain.getUseSet4().getFromLineBankCd(), domain.getUseSet4().getToBranchId(),
				domain.getUseSet4().getAccountAtr(), domain.getUseSet4().getAccountNo(),
				domain.getUseSet4().getAccountHolderKnName(), domain.getUseSet4().getAccountHolderName(),
				domain.getUseSet5().getUseSet(), domain.getUseSet5().getPriorityOrder(),
				domain.getUseSet5().getPaymentMethod(), domain.getUseSet5().getPartialPaySet(),
				domain.getUseSet5().getFixAmountMny(), domain.getUseSet5().getFixRate(),
				domain.getUseSet5().getFromLineBankCd(), domain.getUseSet5().getToBranchId(),
				domain.getUseSet5().getAccountAtr(), domain.getUseSet5().getAccountNo(),
				domain.getUseSet5().getAccountHolderKnName(), domain.getUseSet5().getAccountHolderName());
		return entity;
	}

	@Override
	public List<PersonBankAccount> findAllBranchCode(String companyCode, String branchId) {
		return this.queryProxy().query(SEL_BY_BANKANDBRANCH, PbamtPersonBankAccount.class)
				.setParameter("companyCode", companyCode).setParameter("branchId", branchId).getList(x -> toDomain(x));
	}
}
