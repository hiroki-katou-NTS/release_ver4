package nts.uk.ctx.pr.core.app.find.rule.employment.unitprice.personal;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import nts.uk.ctx.pr.core.app.find.rule.employment.unitprice.personal.dto.PersonalUnitPriceDto;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.personal.PersonalUnitPrice;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.personal.PersonalUnitPriceRepository;
import nts.uk.shr.com.context.AppContexts;

@RequestScoped
public class PersonalUnitPriceFinder {
	@Inject
	private PersonalUnitPriceRepository personalUnitPriceRepository;

	public List<PersonalUnitPriceDto> findAll() {
		String companyCode = AppContexts.user().companyCode();

		return personalUnitPriceRepository.findAll(companyCode).stream()
				.map(x -> new PersonalUnitPriceDto(x.getPersonalUnitPriceCode().v(), x.getPersonalUnitPriceName().v(),
						x.getPersonalUnitPriceAbName().v(), x.getDisplaySet().value, x.getUniteCode().v(),
						x.getPaymentSettingType().value, x.getFixPaymentAtr().value, x.getFixPaymentMonthly().value,
						x.getFixPaymentDayMonth().value, x.getFixPaymentDaily().value, x.getFixPaymentHoursly().value,
						x.getUnitPriceAtr().value, x.getMemo().v()))
				.collect(Collectors.toList());
	}

	public PersonalUnitPriceDto find(String personalUnitPriceCode) {
		String companyCode = AppContexts.user().companyCode();
		Optional<PersonalUnitPrice> unitPriceOp = personalUnitPriceRepository.find(companyCode, personalUnitPriceCode);
		if (!unitPriceOp.isPresent()) {
			return null;
		}

		PersonalUnitPrice unitPrice = unitPriceOp.get();

		return new PersonalUnitPriceDto(unitPrice.getPersonalUnitPriceCode().v(),
				unitPrice.getPersonalUnitPriceName().v(), unitPrice.getPersonalUnitPriceAbName().v(),
				unitPrice.getDisplaySet().value, unitPrice.getUniteCode().v(), unitPrice.getPaymentSettingType().value,
				unitPrice.getFixPaymentAtr().value, unitPrice.getFixPaymentMonthly().value,
				unitPrice.getFixPaymentDayMonth().value, unitPrice.getFixPaymentDaily().value,
				unitPrice.getFixPaymentHoursly().value, unitPrice.getUnitPriceAtr().value, unitPrice.getMemo().v());
	}

	/**
	 * Find all personal unit price
	 * 
	 * @param unitPriceCodeList
	 *            list of unit price code
	 * @return
	 */
	public List<PersonalUnitPriceDto> findAll(List<String> unitPriceCodeList) {
		String companyCode = AppContexts.user().companyCode();
		return personalUnitPriceRepository.findAll(companyCode, unitPriceCodeList).stream()
				.map(x -> new PersonalUnitPriceDto(x.getPersonalUnitPriceCode().v(), x.getPersonalUnitPriceName().v(),
						x.getPersonalUnitPriceAbName().v(), x.getDisplaySet().value, x.getUniteCode().v(),
						x.getPaymentSettingType().value, x.getFixPaymentAtr().value, x.getFixPaymentMonthly().value,
						x.getFixPaymentDayMonth().value, x.getFixPaymentDaily().value, x.getFixPaymentHoursly().value,
						x.getUnitPriceAtr().value, x.getMemo().v()))
				.collect(Collectors.toList());
	}
}
