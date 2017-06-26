package nts.uk.pr.file.infra.banktransfer;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.file.pr.app.export.banktransfer.BankTransferReportRepository;
import nts.uk.file.pr.app.export.banktransfer.data.BankDto;
import nts.uk.file.pr.app.export.banktransfer.data.BankTransferParamRpDto;
import nts.uk.file.pr.app.export.banktransfer.data.BankTransferRpDto;
import nts.uk.file.pr.app.export.banktransfer.data.BranchDto;
import nts.uk.file.pr.app.export.banktransfer.data.CalledDto;

@Stateless
public class JpaBankTransferReportRepository extends JpaRepository implements BankTransferReportRepository {

	private String SEL_1_BANKTRANSFER = "SELECT NEW " + BankTransferRpDto.class.getName() + ""
			+ "(b.qbkdtBankTransferPK.ccd, b.cnameKana, b.qbkdtBankTransferPK.pid, b.qbkdtBankTransferPK.fromBranchId, b.fromBankKnName, b.fromBranchKnName, b.qbkdtBankTransferPK.fromAccountAtr,"
			+ " b.qbkdtBankTransferPK.fromAccountNo, b.qbkdtBankTransferPK.toBranchId, b.toBankKnName, b.toBranchKnName, b.qbkdtBankTransferPK.toAccountAtr, b.qbkdtBankTransferPK.toAccountNo, "
			+ "b.toAccountKnName, b.depcd, b.paymentMny, b.qbkdtBankTransferPK.payBonusAtr, b.qbkdtBankTransferPK.processingNo, b.processingYm, b.qbkdtBankTransferPK.payDate, b.qbkdtBankTransferPK.sparePayAtr)"
			+ "FROM QbkdtBankTransfer b " + "WHERE b.qbkdtBankTransferPK.ccd = :companyCode "
			+ "AND b.qbkdtBankTransferPK.fromBranchId = :fromBranchId "
			+ "AND b.qbkdtBankTransferPK.processingNo = :processingNo "
			+ "AND b.qbkdtBankTransferPK.payBonusAtr = :payBonusAtr " + "AND b.processingYm = :processingYm "
			+ "AND b.qbkdtBankTransferPK.payDate = :payDate " + "AND b.qbkdtBankTransferPK.sparePayAtr = :sparePayAtr";

	private String SEL_1_1_BANKTRANSFER = "SELECT NEW " + BankTransferRpDto.class.getName() + ""
			+ "(b.qbkdtBankTransferPK.ccd, b.cnameKana, b.qbkdtBankTransferPK.pid, b.qbkdtBankTransferPK.fromBranchId, b.fromBankKnName, b.fromBranchKnName, b.qbkdtBankTransferPK.fromAccountAtr,"
			+ " b.qbkdtBankTransferPK.fromAccountNo, b.qbkdtBankTransferPK.toBranchId, b.toBankKnName, b.toBranchKnName, b.qbkdtBankTransferPK.toAccountAtr, b.qbkdtBankTransferPK.toAccountNo, "
			+ "b.toAccountKnName, b.depcd, b.paymentMny, b.qbkdtBankTransferPK.payBonusAtr, b.qbkdtBankTransferPK.processingNo, b.processingYm, b.qbkdtBankTransferPK.payDate, b.qbkdtBankTransferPK.sparePayAtr)"
			+ "FROM QbkdtBankTransfer b " + "WHERE b.qbkdtBankTransferPK.ccd = :companyCode "
			+ "AND b.qbkdtBankTransferPK.fromBranchId = :fromBranchId "
			+ "AND b.qbkdtBankTransferPK.processingNo = :processingNo "
			+ "AND b.qbkdtBankTransferPK.payBonusAtr = :payBonusAtr " + "AND b.processingYm = :processingYm "
			+ "AND b.qbkdtBankTransferPK.payDate = :payDate ";

	private String SEL_2_BRANCH = "SELECT NEW " + BranchDto.class.getName() + ""
			+ "(c.branchName, c.branchCode, c.bankCode)"
			+ "FROM CbkmtBranch c WHERE c.ckbmtBranchPK.companyCode = :companyCode AND c.ckbmtBranchPK.branchId = :branchId";

	private String SEL_2_BANK = "SELECT NEW " + BankDto.class.getName() + "" + "(d.bankName, d.cbkmtBankPK.bankCode)"
			+ "FROM CbkmtBank d WHERE d.cbkmtBankPK.companyCode = :companyCode AND d.cbkmtBankPK.bankCode = :bankCode";

	private String SEL_CALLED = "SELECT NEW " + CalledDto.class.getName() + "" + "(e.person)"
			+ "FROM CmnmtCalled e WHERE e.ccd = :companyCode";

	@Override
	public List<BankTransferRpDto> findBySEL1(BankTransferParamRpDto param) {
		return this.queryProxy().query(SEL_1_BANKTRANSFER, BankTransferRpDto.class)
				.setParameter("companyCode", param.getCompanyCode())
				.setParameter("fromBranchId", param.getFromBranchId())
				.setParameter("payBonusAtr", param.getPayBonusAtr()).setParameter("processingNo", param.getProcessNo())
				.setParameter("processingYm", param.getProcessingYM()).setParameter("payDate", param.getPayDate())
				.setParameter("sparePayAtr", param.getSparePayAtr()).getList();
	}

	@Override
	public List<BankTransferRpDto> findBySEL1_1(BankTransferParamRpDto param) {
		return this.queryProxy().query(SEL_1_1_BANKTRANSFER, BankTransferRpDto.class)
				.setParameter("companyCode", param.getCompanyCode())
				.setParameter("fromBranchId", param.getFromBranchId())
				.setParameter("payBonusAtr", param.getPayBonusAtr()).setParameter("processingNo", param.getProcessNo())
				.setParameter("processingYm", param.getProcessingYM()).setParameter("payDate", param.getPayDate())
				.getList();
	}

	@Override
	public Optional<BranchDto> findAllBranch(String companyCode, String branchId) {
		return this.queryProxy().query(SEL_2_BRANCH, BranchDto.class).setParameter("companyCode", companyCode)
				.setParameter("branchId", branchId).getSingle();
	}

	@Override
	public Optional<BankDto> findAllBank(String companyCode, String bankCode) {
		return this.queryProxy().query(SEL_2_BANK, BankDto.class).setParameter("companyCode", companyCode)
				.setParameter("bankCode", bankCode).getSingle();
	}

	@Override
	public Optional<CalledDto> findAllCalled(String companyCode) {
		return this.queryProxy().query(SEL_CALLED, CalledDto.class).setParameter("companyCode", companyCode).getSingle();
	}

}
