package nts.uk.ctx.pr.core.ac.bank.linebank;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.basic.pub.system.bank.linebank.LineBankPub;
import nts.uk.ctx.pr.core.dom.bank.linebank.adapter.BasicConsignorDto;
import nts.uk.ctx.pr.core.dom.bank.linebank.adapter.BasicLineBankDto;
import nts.uk.ctx.pr.core.dom.bank.linebank.adapter.LineBankAdapter;

@Stateless
public class LineBankAdapterImpl implements LineBankAdapter {

	@Inject
	private LineBankPub lineBankPub;

	@Override
	public Optional<BasicLineBankDto> find(String companyCode, String lineBankCode) {
		return lineBankPub.find(companyCode, lineBankCode).map(x -> {
			List<BasicConsignorDto> consignor = x.getConsignor().stream().map(c -> {
				return new BasicConsignorDto(c.getConsignorCode(), c.getConsignorName());
			}).collect(Collectors.toList());
			return new BasicLineBankDto(companyCode, x.getAccountAtr(), x.getAccountNo(), x.getBranchId(), consignor,
					x.getLineBankCode(), x.getLineBankName(), x.getMemo(), x.getRequesterName());
		});
	}
}
