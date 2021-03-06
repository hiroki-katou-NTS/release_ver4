package nts.uk.ctx.pr.transfer.app.command.emppaymentinfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.pr.transfer.dom.adapter.employment.EmploymentHistImport;
import nts.uk.ctx.pr.transfer.dom.adapter.employment.TransEmploymentHistAdapter;
import nts.uk.ctx.pr.transfer.dom.adapter.wageprovision.processdatecls.CurrProcessYmAdapter;
import nts.uk.ctx.pr.transfer.dom.adapter.wageprovision.processdatecls.CurrProcessYmImport;
import nts.uk.ctx.pr.transfer.dom.adapter.wageprovision.processdatecls.EmploymentTiedProcYmAdapter;
import nts.uk.ctx.pr.transfer.dom.adapter.wageprovision.processdatecls.EmploymentTiedProcYmImport;
import nts.uk.ctx.pr.transfer.dom.adapter.wageprovision.processdatecls.ProcessInformationAdapter;
import nts.uk.ctx.pr.transfer.dom.adapter.wageprovision.processdatecls.ProcessInformationImport;
import nts.uk.ctx.pr.transfer.dom.emppaymentinfo.EmployeeBonusPaymentInfor;
import nts.uk.ctx.pr.transfer.dom.emppaymentinfo.EmployeeBonusPaymentMethod;
import nts.uk.ctx.pr.transfer.dom.emppaymentinfo.EmployeePaymentInforRepository;
import nts.uk.ctx.pr.transfer.dom.emppaymentinfo.EmployeePaymentMethodRepository;
import nts.uk.ctx.pr.transfer.dom.emppaymentinfo.EmployeeSalaryPaymentInfor;
import nts.uk.ctx.pr.transfer.dom.emppaymentinfo.EmployeeSalaryPaymentMethod;
import nts.uk.ctx.pr.transfer.dom.emppaymentinfo.IndividualSettingAtr;
import nts.uk.ctx.pr.transfer.dom.emppaymentinfo.PaymentMethodDetail;
import nts.uk.ctx.pr.transfer.dom.sourcebank.SourceBankCode;
import nts.uk.shr.com.context.AppContexts;

/**
 * 
 * @author HungTT
 *
 */

@Stateless
public class SourceBankIntegrationCommandHandler extends CommandHandler<SourceBankIntegrationCommand> {

	@Inject
	private ProcessInformationAdapter procInfoAdapter;

	@Inject
	private CurrProcessYmAdapter currentProcYmAdapter;

	@Inject
	private EmploymentTiedProcYmAdapter empTiedProcYmAdapter;

	@Inject
	private EmployeePaymentInforRepository payInfoRepo;

	@Inject
	private EmployeePaymentMethodRepository payMethodRepo;

	@Inject
	private TransEmploymentHistAdapter employmentAdapter;

	@Override
	protected void handle(CommandHandlerContext<SourceBankIntegrationCommand> context) {
		String companyId = AppContexts.user().companyId();
		String sourceCode = context.getCommand().getSourceCode();
		String destinationCode = context.getCommand().getDestinationCode();

		// ??????ID = ??????????????????ID
		// ???????????? = ???????????????
		List<ProcessInformationImport> listProcInfor = procInfoAdapter
				.getProcessInformationByDeprecatedCategory(companyId, 0);

		// get thang nam xu ly hien tai
		Set<Integer> setProcCateNo = listProcInfor.stream().map(i -> i.getProcessCateNo()).collect(Collectors.toSet());
		List<CurrProcessYmImport> lstCurrProcYm = currentProcYmAdapter.getCurrentSalaryProcessYm(companyId,
				new ArrayList<>(setProcCateNo));

		// get EmploymentTiedProcYm
		List<EmploymentTiedProcYmImport> lstEmpTiedProcYm = empTiedProcYmAdapter.getByListProcCateNo(companyId,
				new ArrayList<>(setProcCateNo));

		// get lich su tuyen dung
		List<EmploymentHistImport> lstEmpHist = employmentAdapter.findByCidAndDate(companyId, GeneralDate.today());

		for (EmploymentHistImport empHist : lstEmpHist) {
			empIntegrationProcess(empHist.getEmployeeId(), empHist.getEmploymentCode(), lstEmpTiedProcYm, lstCurrProcYm,
					sourceCode, destinationCode);
		}
	}

	private void empIntegrationProcess(String employeeId, String employmentCode,
			List<EmploymentTiedProcYmImport> lstEmpTiedProcYm, List<CurrProcessYmImport> lstCurrProcYm,
			String sourceCode, String destinationCode) {
		val optEmpTiedProcYm = lstEmpTiedProcYm.stream().filter(t -> t.getEmploymentCodes().contains(employmentCode))
				.findFirst();
		// salary payment
		val optSalPayInfor = payInfoRepo.getEmpSalPaymentInfo(employeeId);
		if (optSalPayInfor.isPresent()) {
			String salHistId = getSalaryHistId(optSalPayInfor.get(), optEmpTiedProcYm, lstCurrProcYm);
			val salPayMethod = payMethodRepo.getSalPayMethodByHistoryId(salHistId);
			if (salPayMethod.isPresent()) {
				EmployeeSalaryPaymentMethod domain = salPayMethod.get();
				for (PaymentMethodDetail pmd : domain.getListPaymentMethod()) {
					if (pmd.getTransferInfor().isPresent()
							&& sourceCode.equals(pmd.getTransferInfor().get().getSourceBankBranchInfor().v())) {
						pmd.getTransferInfor().get().setSourceBankBranchInfor(new SourceBankCode(destinationCode));
					}
				}
				payMethodRepo.updateEmpSalaryPayMethod(domain);
			}
		}
		// bonus payment
		val optBonusPayInfor = payInfoRepo.getEmpBonusPaymentInfo(employeeId);
		if (optBonusPayInfor.isPresent()) {
			String bonHistId = getBonusHistId(optBonusPayInfor.get(), optEmpTiedProcYm, lstCurrProcYm);
			val bonPayMethod = payMethodRepo.getBonusPayMethodByHistoryId(bonHistId);
			if (bonPayMethod.isPresent()) {
				EmployeeBonusPaymentMethod domain = bonPayMethod.get();
				if (domain.getSettingAtr() == IndividualSettingAtr.PAY_WITH_INDIVIDUAL_SETTING
						&& domain.getListPaymentMethod().isPresent()) {
					for (PaymentMethodDetail pmd : domain.getListPaymentMethod().get()) {
						if (pmd.getTransferInfor().isPresent()
								&& sourceCode.equals(pmd.getTransferInfor().get().getSourceBankBranchInfor().v())) {
							pmd.getTransferInfor().get().setSourceBankBranchInfor(new SourceBankCode(destinationCode));
						}
					}
					payMethodRepo.updateEmpBonusPayMethod(domain);
				}

			}
		}
	}

	private String getSalaryHistId(EmployeeSalaryPaymentInfor salPayInfor,
			Optional<EmploymentTiedProcYmImport> empTiedProcYm, List<CurrProcessYmImport> lstCurrProcYm) {
		if (empTiedProcYm.isPresent()) {
			val optCurrProcYm = lstCurrProcYm.stream()
					.filter(p -> p.getProcessCateNo() == empTiedProcYm.get().getProcessCateNo()).findFirst();
			if (optCurrProcYm.isPresent()) {
				val optSalInfor = salPayInfor.items().stream()
						.filter(h -> h.end().greaterThanOrEqualTo(optCurrProcYm.get().getCurrentYm())).findFirst();
				return optSalInfor.isPresent() ? optSalInfor.get().identifier() : "";
			} else
				return "";

		} else {
			val optCurrProcYm = lstCurrProcYm.stream().filter(p -> p.getProcessCateNo() == 1).findFirst();
			if (optCurrProcYm.isPresent()) {
				val optSalInfor = salPayInfor.items().stream()
						.filter(h -> h.end().greaterThanOrEqualTo(optCurrProcYm.get().getCurrentYm())).findFirst();
				return optSalInfor.isPresent() ? optSalInfor.get().identifier() : "";
			} else
				return "";
		}
	}

	private String getBonusHistId(EmployeeBonusPaymentInfor bonPayInfor,
			Optional<EmploymentTiedProcYmImport> empTiedProcYm, List<CurrProcessYmImport> lstCurrProcYm) {
		if (empTiedProcYm.isPresent()) {
			val optCurrProcYm = lstCurrProcYm.stream()
					.filter(p -> p.getProcessCateNo() == empTiedProcYm.get().getProcessCateNo()).findFirst();
			if (optCurrProcYm.isPresent()) {
				val optBonInfor = bonPayInfor.items().stream()
						.filter(h -> h.end().greaterThanOrEqualTo(optCurrProcYm.get().getCurrentYm())).findFirst();
				return optBonInfor.isPresent() ? optBonInfor.get().identifier() : "";
			}
			return "";
		} else {
			val optCurrProcYm = lstCurrProcYm.stream().filter(p -> p.getProcessCateNo() == 1).findFirst();
			if (optCurrProcYm.isPresent()) {
				val optBonInfor = bonPayInfor.items().stream()
						.filter(h -> h.end().greaterThanOrEqualTo(optCurrProcYm.get().getCurrentYm())).findFirst();
				return optBonInfor.isPresent() ? optBonInfor.get().identifier() : "";
			}
			return "";
		}
	}

}
