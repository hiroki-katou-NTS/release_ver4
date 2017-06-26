package nts.uk.ctx.pr.core.app.find.paymentdata;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.core.dom.paymentdata.PaymentDatePersonal;
import nts.uk.ctx.pr.core.dom.paymentdata.paymentdatemaster.PaymentDateProcessingMaster;
import nts.uk.ctx.pr.core.dom.paymentdata.repository.PaymentDatePersonalRepository;
import nts.uk.ctx.pr.core.dom.paymentdata.repository.PaymentDateProcessingMasterRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

@Stateless
public class PaymentDateProcessingMasterFinder {
	/**
	 * PaymentDateProcessingMasterRepository
	 */
	@Inject
	private PaymentDateProcessingMasterRepository repository;
	
	@Inject
	private PaymentDatePersonalRepository datePersonalRepo;

	/**
	 * 
	 * @param companyCode
	 * @param paymentBonusAtribute
	 * @param processingNo
	 * @return PaymentDateProcessingMaster
	 */
	public Optional<PaymentDateProcessingMasterDto> find(int paymentBonusAtribute,
			int processingNo) {
		LoginUserContext login = AppContexts.user();
		return this.repository.find(login.companyCode(), paymentBonusAtribute, processingNo)
				.map(d -> PaymentDateProcessingMasterDto.fromDomain(d));
	}

	/**
	 * 
	 * @param companyCode
	 * @param paymentBonusAtribute
	 * @return PaymentDateProcessingMasterDto
	 */
	public List<PaymentDateProcessingMasterDto> findAll(int paymentBonusAtribute) {
		LoginUserContext login = AppContexts.user();
		return this.repository.findAll(login.companyCode(), paymentBonusAtribute).stream()
				.map(d -> { return PaymentDateProcessingMasterDto.fromDomain(d); })
				.collect(Collectors.toList());
	}
	
	/**
	 * Find date processing master by personal
	 * @param personId personId
	 * @return PaymentDateProcessingMasterDto
	 */
	public PaymentDateProcessingMasterDto findByLogin() {
		LoginUserContext login = AppContexts.user();
		String companyCode = login.companyCode();
		String personId = login.personId();
		
		Optional<PaymentDatePersonal> datePersonOp = this.datePersonalRepo.find(companyCode, personId);
		if (!datePersonOp.isPresent()) {
			throw new RuntimeException("Error system");
		}
		
		Optional<PaymentDateProcessingMaster> dateMasterOp = this.repository.find(companyCode, datePersonOp.get().getProcessingNo());
		if (!dateMasterOp.isPresent()) {
			throw new RuntimeException("Error system");
		}
		
		return PaymentDateProcessingMasterDto.fromDomain(dateMasterOp.get());
	}
}
