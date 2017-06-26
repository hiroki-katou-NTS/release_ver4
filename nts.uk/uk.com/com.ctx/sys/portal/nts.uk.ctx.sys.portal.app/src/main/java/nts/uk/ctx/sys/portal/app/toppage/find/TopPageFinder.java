package nts.uk.ctx.sys.portal.app.toppage.find;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.sys.portal.dom.toppage.TopPage;
import nts.uk.ctx.sys.portal.dom.toppage.TopPageRepository;


/**
 * The Class TopPageFinder.
 */
@Stateless
public class TopPageFinder {
	@Inject
	TopPageRepository topPageRepository;

	public List<TopPageItemDto> findAll(String companyId) {
		List<TopPage> listTopPage = topPageRepository.findAll(companyId);
		// convert from domain to dto
		List<TopPageItemDto> lstTopPageItemDto = listTopPage.stream()
				.map(item -> new TopPageItemDto(item.getTopPageCode().v(), item.getTopPageName().v()))
				.collect(Collectors.toList());
		return lstTopPageItemDto;
	}

	public TopPageDto findByCode(String companyId, String topPageCode, String languageType) {
		Optional<TopPage> topPage = topPageRepository.findByCode(companyId, topPageCode);
		// convert toppage domain to dto
		if (topPage.isPresent()) {
			TopPage tp = topPage.get(); 
			return new TopPageDto(tp.getTopPageCode().v(), tp.getTopPageName().v());
		}
		return null;
	}
}
