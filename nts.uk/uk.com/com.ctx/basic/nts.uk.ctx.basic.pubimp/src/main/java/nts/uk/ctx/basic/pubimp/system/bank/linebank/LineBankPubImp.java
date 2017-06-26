package nts.uk.ctx.basic.pubimp.system.bank.linebank;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.basic.dom.system.bank.linebank.LineBankRepository;
import nts.uk.ctx.basic.pub.system.bank.linebank.ConsignorDto;
import nts.uk.ctx.basic.pub.system.bank.linebank.LineBankDto;
import nts.uk.ctx.basic.pub.system.bank.linebank.LineBankPub;

@Stateless
public class LineBankPubImp implements LineBankPub {
	@Inject
	private LineBankRepository lineBankRepository;

	@Override
	public Optional<LineBankDto> find(String companyCode, String lineBankCode) {
		return lineBankRepository.find(companyCode, lineBankCode).map(x -> {
			List<ConsignorDto> consignor = x.getConsignor().stream().map(c -> {
				return new ConsignorDto(c.getConsignorCode().toString(), c.getConsignorMemo().toString());
			}).collect(Collectors.toList());
			return new LineBankDto(companyCode, x.getAccountAtr().value, x.getAccountNo().toString(),
					x.getBranchId().toString(), consignor, x.getLineBankCode().toString(),
					x.getLineBankName().toString(), x.getMemo().toString(), x.getRequesterName().toString());
		});
	}
}
