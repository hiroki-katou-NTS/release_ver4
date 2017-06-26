package nts.uk.ctx.pr.core.infra.repository.payment.banktransfer;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.pr.core.dom.payment.banktransfer.BankTransfer;
import nts.uk.ctx.pr.core.dom.payment.banktransfer.BankTransferParam;
import nts.uk.ctx.pr.core.dom.payment.banktransfer.BankTransferRepository;
import nts.uk.ctx.pr.core.infra.entity.payment.banktransfer.QbkdtBankTransfer;
import nts.uk.ctx.pr.core.infra.entity.payment.banktransfer.QbkdtBankTransferPK;

@Stateless
@Transactional
public class JpaBankTransferRepository extends JpaRepository implements BankTransferRepository {

	private final String SEL = "SELECT b FROM QbkdtBankTransfer b ";

	private final String SEL_1 = SEL + "WHERE b.qbkdtBankTransferPK.ccd = :companyCode "
			+ "AND b.qbkdtBankTransferPK.fromBranchId = :fromBranchId "
			+ "AND b.qbkdtBankTransferPK.processingNo = :processingNo "
			+ "AND b.qbkdtBankTransferPK.payBonusAtr = :payBonusAtr " + "AND b.processingYm = :processingYm "
			+ "AND b.qbkdtBankTransferPK.payDate = :payDate " + "AND b.qbkdtBankTransferPK.sparePayAtr = :sparePayAtr";
	
	private final String SEL_1_1 = SEL + "WHERE b.qbkdtBankTransferPK.ccd = :companyCode "
			+ "AND b.qbkdtBankTransferPK.fromBranchId = :fromBranchId "
			+ "AND b.qbkdtBankTransferPK.processingNo = :processingNo "
			+ "AND b.qbkdtBankTransferPK.payBonusAtr = :payBonusAtr " + "AND b.processingYm = :processingYm "
			+ "AND b.qbkdtBankTransferPK.payDate = :payDate";

	private final String SEL_2 = SEL + "WHERE b.qbkdtBankTransferPK.ccd = :companyCode "
			+ "AND b.qbkdtBankTransferPK.pid = :personId " + "AND b.qbkdtBankTransferPK.fromBranchId = :fromBranchId "
			+ "AND b.qbkdtBankTransferPK.fromAccountAtr = :fromAccountAtr "
			+ "AND b.qbkdtBankTransferPK.fromAccountNo = :fromAccountNo "
			+ "AND b.qbkdtBankTransferPK.toBranchId = :toBranchId "
			+ "AND b.qbkdtBankTransferPK.toAccountAtr = :toAccountAtr "
			+ "AND b.qbkdtBankTransferPK.toAccountNo = :toAccountNo "
			+ "AND b.qbkdtBankTransferPK.payBonusAtr = :payBonusAtr " + "AND b.qbkdtBankTransferPK.payDate = :payDate "
			+ "AND b.qbkdtBankTransferPK.sparePayAtr = :sparePayAtr";

	private final String DEL_1 = "DELETE FROM QbkdtBankTransfer b " + "WHERE b.qbkdtBankTransferPK.ccd = :companyCode "
			+ "AND b.qbkdtBankTransferPK.payBonusAtr = :payBonusAtr "
			+ "AND b.qbkdtBankTransferPK.processingNo = :processingNo "
			+ "AND b.qbkdtBankTransferPK.payDate = :payDate " + "AND b.qbkdtBankTransferPK.sparePayAtr = :sparePayAtr";

	private final String DEL_ALL = "DELETE FROM QbkdtBankTransfer b "
			+ "WHERE b.qbkdtBankTransferPK.ccd = :companyCode "
			+ "AND b.qbkdtBankTransferPK.processingNo = :processingNo "
			+ "AND b.qbkdtBankTransferPK.payDate = :payDate";

	@Override
	public void add(BankTransfer root) {
		this.commandProxy().insert(toEntity(root));
	}

	@Override
	public void update(BankTransfer root) {
		this.commandProxy().update(toEntity(root));
	}

	@Override
	public Optional<BankTransfer> find(BankTransferParam param) {
		QbkdtBankTransferPK key = new QbkdtBankTransferPK(param.getCompanyCode(), param.getPersonId(),
				param.getFromBranchId(), param.getFromAccountAtr(), param.getFromAccountNo(), param.getToBranchId(),
				param.getToAccountAtr(), param.getToAccountNo(), param.getPayBonusAtr(), param.getProcessNo(),
				param.getPayDate(), param.getSparePayAtr());

		Optional<BankTransfer> entityOpt = this.queryProxy().find(key, QbkdtBankTransfer.class).map(x -> toDomain(x));

		return entityOpt;
	}

	@Override
	public List<BankTransfer> findBySEL1(BankTransferParam param) {
		return this.queryProxy().query(SEL_1, QbkdtBankTransfer.class)
				.setParameter("companyCode", param.getCompanyCode())
				.setParameter("fromBranchId", param.getFromBranchId())
				.setParameter("payBonusAtr", param.getPayBonusAtr()).setParameter("processingNo", param.getProcessNo())
				.setParameter("processingYm", param.getProcessYearMonth()).setParameter("payDate", param.getPayDate())
				.setParameter("sparePayAtr", param.getSparePayAtr()).getList(x -> toDomain(x));
	}
	
	@Override
	public List<BankTransfer> findBySEL1_1(BankTransferParam param) {
		return this.queryProxy().query(SEL_1_1, QbkdtBankTransfer.class)
				.setParameter("companyCode", param.getCompanyCode())
				.setParameter("fromBranchId", param.getFromBranchId())
				.setParameter("payBonusAtr", param.getPayBonusAtr()).setParameter("processingNo", param.getProcessNo())
				.setParameter("processingYm", param.getProcessYearMonth()).setParameter("payDate", param.getPayDate())
				.getList(x -> toDomain(x));
	}

	@Override
	public List<BankTransfer> findBySEL2(BankTransferParam param) {
		return this.queryProxy().query(SEL_2, QbkdtBankTransfer.class)
				.setParameter("companyCode", param.getCompanyCode()).setParameter("personId", param.getPersonId())
				.setParameter("fromBranchId", param.getFromBranchId())
				.setParameter("fromAccountAtr", param.getFromAccountAtr())
				.setParameter("fromAccountNo", param.getFromAccountNo())
				.setParameter("toBranchId", param.getToBranchId()).setParameter("toAccountAtr", param.getToAccountAtr())
				.setParameter("toAccountNo", param.getToAccountNo()).setParameter("payBonusAtr", param.getPayBonusAtr())
				.setParameter("payDate", param.getPayDate()).setParameter("sparePayAtr", param.getSparePayAtr())
				.getList(x -> toDomain(x));
	}

	@Override
	public void remove(String companyCode, int payBonusAtr, int processingNo, GeneralDate payDate, int sparePayAtr) {
		this.getEntityManager().createQuery(DEL_1, QbkdtBankTransfer.class).setParameter("companyCode", companyCode)
				.setParameter("payBonusAtr", payBonusAtr).setParameter("processingNo", processingNo)
				.setParameter("payDate", payDate).setParameter("sparePayAtr", sparePayAtr).executeUpdate();
	}

	@Override
	public void removeAll(String companyCode, int processingNo, GeneralDate payDate) {
		this.getEntityManager().createQuery(DEL_ALL, QbkdtBankTransfer.class).setParameter("companyCode", companyCode)
				.setParameter("processingNo", processingNo).setParameter("payDate", payDate).executeUpdate();
	}

	/**
	 * Convert to domain
	 * 
	 * @param x
	 *            entity
	 * @return BankTransfer
	 */
	private BankTransfer toDomain(QbkdtBankTransfer x) {
		BankTransfer bankTransfer = BankTransfer.createFromJavaType(x.qbkdtBankTransferPK.ccd, x.cnameKana,
				x.qbkdtBankTransferPK.pid, x.depcd, x.qbkdtBankTransferPK.payDate, x.qbkdtBankTransferPK.payBonusAtr,
				x.paymentMny, x.qbkdtBankTransferPK.processingNo, x.processingYm, x.qbkdtBankTransferPK.sparePayAtr);

		bankTransfer.fromBank(x.qbkdtBankTransferPK.fromBranchId, x.fromBankKnName, x.fromBranchKnName,
				x.qbkdtBankTransferPK.fromAccountAtr, x.qbkdtBankTransferPK.fromAccountNo);
		bankTransfer.toBank(x.qbkdtBankTransferPK.toBranchId, x.toBankKnName, x.toBranchKnName,
				x.qbkdtBankTransferPK.toAccountAtr, x.qbkdtBankTransferPK.toAccountNo, x.toAccountKnName);

		return bankTransfer;
	}

	/**
	 * Convert to entity
	 * 
	 * @param root
	 * @return QbkdtBankTransfer
	 */
	private QbkdtBankTransfer toEntity(BankTransfer root) {
		QbkdtBankTransferPK key = toPK(root);

		QbkdtBankTransfer entity = new QbkdtBankTransfer(key);
		entity.cnameKana = root.getCompanyNameKana();
		entity.fromBankKnName = root.getFromBank().getBankNameKana();
		entity.fromBranchKnName = root.getFromBank().getBranchNameKana();
		entity.toBankKnName = root.getToBank().getBankNameKana();
		entity.toBranchKnName = root.getToBank().getBranchNameKana();
		entity.toAccountKnName = root.getToBank().getAccountNameKana();
		entity.depcd = root.getDepartmentCode();
		entity.paymentMny = root.getPaymentMoney().v();
		entity.processingYm = root.getProcessingYM().v();

		return entity;
	}

	/**
	 * Convert to pk object
	 * 
	 * @param root
	 * @return QbkdtBankTransferPK
	 */
	private QbkdtBankTransferPK toPK(BankTransfer root) {
		return new QbkdtBankTransferPK(root.getCompanyCode(), root.getPersonId().v(), root.getFromBank().getBranchId(),
				root.getFromBank().getAccountAtr(), root.getFromBank().getAccountNo(), root.getToBank().getBranchId(),
				root.getToBank().getAccountAtr(), root.getToBank().getAccountNo(), root.getPaymentBonusAtr().value,
				root.getProcessingNo(), root.getPaymentDate(), root.getSparePaymentAtr().value);
	}
}
