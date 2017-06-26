package nts.uk.ctx.basic.app.find.system.bank.linebank;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.uk.ctx.basic.dom.system.bank.linebank.LineBankRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

@Stateless
@Transactional
public class LineBankFinder {
	@Inject
	private LineBankRepository lineBankRepository;

	/**
	 * find all lineBank
	 * 
	 * @return
	 */
	public List<LineBankDto> findAll() {
		LoginUserContext login = AppContexts.user();
		List<LineBankDto> result = this.lineBankRepository.findAll(login.companyCode()).stream().map(x -> {
			return LineBankDto.fromDomain(x);
		}).collect(Collectors.toList());

		return result;
	}

	/**
	 * find one lineBank base on lineBankCode
	 * 
	 * @param lineBankCode
	 * @return
	 */
	public Optional<LineBankDto> find(String lineBankCode) {
		LoginUserContext login = AppContexts.user();
		return this.lineBankRepository.find(login.companyCode(), lineBankCode).map(x -> LineBankDto.fromDomain(x));
	}
}
