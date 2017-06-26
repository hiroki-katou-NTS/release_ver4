package nts.uk.file.pr.app.export.banktransfer;

import java.util.List;
import java.util.Optional;

import nts.uk.file.pr.app.export.banktransfer.data.BankDto;
import nts.uk.file.pr.app.export.banktransfer.data.BankTransferParamRpDto;
import nts.uk.file.pr.app.export.banktransfer.data.BankTransferRpDto;
import nts.uk.file.pr.app.export.banktransfer.data.BranchDto;
import nts.uk.file.pr.app.export.banktransfer.data.CalledDto;

public interface BankTransferReportRepository {
	/**
	 * Find list bank transfer (SEL_1_BANKTRANSFER)
	 * 
	 * @param param
	 *            parameter information
	 * @return list BankTransferRpDto
	 */
	List<BankTransferRpDto> findBySEL1(BankTransferParamRpDto param);

	/**
	 * 
	 * Find list bank transfer (SEL_1_1_BANKTRANSFER: not include property
	 * "sparePatAtr")
	 * 
	 * @param param
	 * @return list BankTransferRpDto
	 */
	List<BankTransferRpDto> findBySEL1_1(BankTransferParamRpDto param);

	/**
	 * 
	 * @param companyCode
	 * @param branchId
	 * @return branchDto
	 */
	Optional<BranchDto> findAllBranch(String companyCode, String branchId);

	/**
	 * 
	 * @param companyCode
	 * @param bankCode
	 * @return bankDto
	 */
	Optional<BankDto> findAllBank(String companyCode, String bankCode);

	/**
	 * 
	 * @param companyCode
	 * @return
	 */
	Optional<CalledDto> findAllCalled(String companyCode);
}
