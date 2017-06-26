package nts.uk.ctx.basic.app.find.organization.payclassification;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.basic.dom.organization.payclassification.PayClassification;
import nts.uk.ctx.basic.dom.organization.payclassification.PayClassificationRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class PayClassificationFinder {

	@Inject
	private PayClassificationRepository payClassificationRepository;

	public List<PayClassificationDto> init() {
		String companyCode = AppContexts.user().companyCode();
		return payClassificationRepository.findAll(companyCode)
				.stream().map(e->{return convertToDto(e);}).collect(Collectors.toList());
	}

	private PayClassificationDto convertToDto(PayClassification payClassification) {
		PayClassificationDto payClassificationDto = new PayClassificationDto();
		payClassificationDto.setPayClassificationCode(payClassification.getPayClassificationCode().v());
		payClassificationDto.setPayClassificationName(payClassification.getPayClassificationName().v());
		payClassificationDto.setMemo(payClassification.getMemo().v());
		return payClassificationDto;
	}

}
