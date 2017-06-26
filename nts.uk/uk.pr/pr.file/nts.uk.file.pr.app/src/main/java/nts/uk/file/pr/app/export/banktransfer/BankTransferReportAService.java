package nts.uk.file.pr.app.export.banktransfer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.file.export.ExportService;
import nts.arc.layer.app.file.export.ExportServiceContext;
import nts.uk.ctx.pr.core.dom.enums.PayBonusAtr;
import nts.uk.ctx.pr.core.dom.enums.SparePayAtr;
import nts.uk.file.pr.app.export.banktransfer.data.BankDto;
import nts.uk.file.pr.app.export.banktransfer.data.BankTransferAReport;
import nts.uk.file.pr.app.export.banktransfer.data.BankTransferARpData;
import nts.uk.file.pr.app.export.banktransfer.data.BankTransferARpHeader;
import nts.uk.file.pr.app.export.banktransfer.data.BankTransferParamRpDto;
import nts.uk.file.pr.app.export.banktransfer.data.BankTransferRpDto;
import nts.uk.file.pr.app.export.banktransfer.data.BranchDto;
import nts.uk.file.pr.app.export.banktransfer.query.BankTransferReportQuery;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class BankTransferReportAService extends ExportService<BankTransferReportQuery> {

	@Inject
	private BankTransferRpAGenerator generator;

	@Inject
	private BankTransferReportRepository bankTransferReportRepo;

	@Override
	protected void handle(ExportServiceContext<BankTransferReportQuery> context) {
		String companyCode = AppContexts.user().companyCode();
		BankTransferReportQuery query = context.getQuery();
		if (query.getSparePayAtr().equals("1")) {
			process(query, companyCode, context, SparePayAtr.NORMAL.value);
		} else if (query.getSparePayAtr().equals("2")) {
			process(query, companyCode, context, SparePayAtr.PRELIMINARY.value);
		} else {
			process(query, companyCode, context, 0);
		}
	}

	private void process(BankTransferReportQuery query, String companyCode,
			ExportServiceContext<BankTransferReportQuery> context, int sparePayAtr) {
		List<BankTransferARpData> rpDataList = new ArrayList<BankTransferARpData>();
		for (String fromBranchId : query.getFromBranchId()) {
			BankTransferParamRpDto bankTransferParamRp = new BankTransferParamRpDto(companyCode, fromBranchId,
					PayBonusAtr.SALARY.value, query.getProcessingNo(), query.getProcessingYm(), query.getPayDate(),
					sparePayAtr);
			// BANK_TRANSFER SEL_1
			List<BankTransferRpDto> bankTransfer = new ArrayList<BankTransferRpDto>();
			if (query.getSparePayAtr().equals("3")) {
				bankTransfer = bankTransferReportRepo.findBySEL1_1(bankTransferParamRp);
			} else {
				bankTransfer = bankTransferReportRepo.findBySEL1(bankTransferParamRp);
			}

			for (BankTransferRpDto bankTrans : bankTransfer) {
				BankTransferARpData rpData = new BankTransferARpData();
				// A_DBD_008 (PERSON_COM)
				rpData.setScd("0000000001");
				// A_DBD_009 (PERSON_BASE)
				rpData.setName("山");
				if (query.getSelectedId_J_SEL_001() == 1) {
					// PERSON_BASE SEL_1
				}

				Optional<BranchDto> branchDto = bankTransferReportRepo.findAllBranch(companyCode,
						bankTrans.getToBranchId());
				Optional<BankDto> bankDto = bankTransferReportRepo.findAllBank(companyCode,
						branchDto.get().getBankCode());
				if (!branchDto.isPresent() || !bankDto.isPresent()) {
					throw new BusinessException("ER010");
				} else {
					// A_DBD_002
					rpData.setBankCode(bankDto.get().getBankCode());
					// A_DBD_003
					rpData.setBankName(bankDto.get().getBankName());
					// A_DBD_004
					rpData.setBranchCode(branchDto.get().getBranchCode());
					// A_DBD_005
					rpData.setBranchName(branchDto.get().getBranchName());
					// A_DBD_006
					if (bankTrans.getToAccountAtr() == 0) {
						rpData.setToAccountAtr("普通");
					} else {
						rpData.setToAccountAtr("当座");
					}
					// A_DBD_007
					rpData.setToAccountNo(bankTrans.getToAccountNo());
					// A_DBD_010
					rpData.setPaymentMyn(bankTrans.getPaymentMoney());
					rpData.setUnit("円");

					rpDataList.add(rpData);
				}
			}
		}
		BankTransferARpData rpDataSum = new BankTransferARpData();
		rpDataSum.setBankCode("総合計");
		// A_CTR_002 (totalPaymentMny)
		BigDecimal sum = BigDecimal.valueOf(0);
		for (BankTransferARpData bankTransferARpData : rpDataList) {
			sum = sum.add(bankTransferARpData.getPaymentMyn());
		}
		rpDataSum.setPaymentMyn(sum);
		rpDataSum.setUnit("円");
		// A_CTR_001 (totalPerson)
		rpDataSum.setName(String.valueOf(rpDataList.size()) + "人");

		rpDataList.add(rpDataSum);

		BankTransferARpHeader header = new BankTransferARpHeader();
		header.setCompanyName("【日通システム株式会社】");
		header.setCode("0001 - 100 	給与分  	給与予備月 】");
		header.setDate(query.getTransferDate()+"】");
		// A_DBD_001
		header.setPerson(bankTransferReportRepo.findAllCalled(companyCode).get().getPerson());

		if (query.getSparePayAtr().equals("3")) {
			if (query.getCurrentCode_J_SEL_004() == 2) {
				// A_CTR_003
				header.setState("【給与分】");
				// A_CTR_004
				// header.setState("【給与予備月】");
			} else {
				header.setState("");
			}
		} else {
			header.setState("");
		}

		BankTransferAReport reportA = new BankTransferAReport();
		reportA.setData(rpDataList);
		reportA.setHeader(header);

		this.generator.generator(context.getGeneratorContext(), reportA);
	}
}
