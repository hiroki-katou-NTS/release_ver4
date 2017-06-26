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
import nts.uk.file.pr.app.export.banktransfer.data.BankTransferBReport;
import nts.uk.file.pr.app.export.banktransfer.data.BankTransferBRpData;
import nts.uk.file.pr.app.export.banktransfer.data.BankTransferBRpHeader;
import nts.uk.file.pr.app.export.banktransfer.data.BankTransferParamRpDto;
import nts.uk.file.pr.app.export.banktransfer.data.BankTransferRpDto;
import nts.uk.file.pr.app.export.banktransfer.data.BranchDto;
import nts.uk.file.pr.app.export.banktransfer.query.BankTransferReportQuery;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class BankTransferReportBService extends ExportService<BankTransferReportQuery> {

	@Inject
	private BankTransferRpBGenerator generator;

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

		List<BankTransferBRpData> rpDataList = new ArrayList<BankTransferBRpData>();
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
				BankTransferBRpData rpData = new BankTransferBRpData();
				// PERSON_BASE SEL_1
				// to-do : sau khi select trong personBase can check dieu kien
				// ton tai hay khong(throw ER010)
				Optional<BranchDto> branchDto = bankTransferReportRepo.findAllBranch(companyCode,
						bankTrans.getToBranchId());
				Optional<BankDto> bankDto = bankTransferReportRepo.findAllBank(companyCode,
						branchDto.get().getBankCode());
				if (!branchDto.isPresent() || !bankDto.isPresent()) {
					throw new BusinessException("ER010");
				} else {
					// B_DBD_002
					rpData.setBankCode(bankDto.get().getBankCode());
					// B_DBD_003
					rpData.setBankName(bankDto.get().getBankName());
					// B_DBD_004
					rpData.setBranchCode(branchDto.get().getBranchCode());
					// B_DBD_005
					rpData.setBranchName(branchDto.get().getBranchName());

					// B_DBD_006
					if (bankTrans.getFromAccountAtr() == 0) {
						rpData.setFromAccountAtr("普通");
					} else {
						rpData.setFromAccountAtr("当座");
					}
					// B_DBD_007
					rpData.setFromAccountNo(bankTrans.getFromAccountNo());
					// B_DBD_008
					rpData.setNumPerSameType(15);
					// A_DBD_009
					rpData.setPaymentMyn(bankTrans.getPaymentMoney());
					rpData.setUnit("円");
					rpData.setUnitPerson("人");

					rpDataList.add(rpData);
				}
			}
		}
		BankTransferBRpData rpDataSum = new BankTransferBRpData();
		rpDataSum.setBankCode("総合計");
		// B_CTR_002 (totalPaymentMny)
		BigDecimal sum = BigDecimal.valueOf(0);
		int totalPerson = 0;
		for (BankTransferBRpData bankTransferBRpData : rpDataList) {
			sum = sum.add(bankTransferBRpData.getPaymentMyn());
			totalPerson += bankTransferBRpData.getNumPerSameType();
		}
		rpDataSum.setPaymentMyn(sum);
		rpDataSum.setUnit("円");
		// B_CTR_001 (totalPerson)
		rpDataSum.setNumPerSameType(totalPerson);
		rpDataSum.setUnitPerson("人");

		rpDataList.add(rpDataSum);

		BankTransferBRpHeader header = new BankTransferBRpHeader();
		header.setCompanyName("【日通システム株式会社】");
		header.setStartCode("0001 - 100 	給与分  	給与予備月 ");
		header.setEndCode("9999 - 999	総合計	給与予備月 】");
		header.setDate("平成 29 年 5 月12日  】");
		// B_DBD_001
		header.setPerson(bankTransferReportRepo.findAllCalled(companyCode).get().getPerson());

		BankTransferBReport reportB = new BankTransferBReport();
		reportB.setData(rpDataList);
		reportB.setHeader(header);

		this.generator.generator(context.getGeneratorContext(), reportB);
	}

}