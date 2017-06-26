package nts.uk.ctx.sys.portal.infra.repository.toppage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.uk.ctx.sys.portal.dom.primitive.TopPageCode;
import nts.uk.ctx.sys.portal.dom.primitive.TopPageName;
import nts.uk.ctx.sys.portal.dom.toppage.TopPage;
import nts.uk.ctx.sys.portal.dom.toppage.TopPageRepository;

@Stateless
public class JpaTopPageRepository implements TopPageRepository {

	@Override
	public List<TopPage> findAll(String companyId) {
		// Mock data
		List<TopPage> lstTopPage = new ArrayList<TopPage>();
		lstTopPage.add(new TopPage(new TopPageCode("001"), null, new TopPageName("no"), null));
		lstTopPage.add(new TopPage(new TopPageCode("002"), null, new TopPageName("2"), null));
		lstTopPage.add(new TopPage(new TopPageCode("003"), null, new TopPageName("4"), null));
		lstTopPage.add(new TopPage(new TopPageCode("004"), null, new TopPageName("5"), null));
		return lstTopPage;
	}

	@Override
	public Optional<TopPage> findByCode(String companyId, String topPageCode) {
		List<TopPage> lstTopPage = new ArrayList<TopPage>();
		lstTopPage.add(new TopPage(new TopPageCode("001"), null, new TopPageName("no"), null));
		lstTopPage.add(new TopPage(new TopPageCode("002"), null, new TopPageName("2"), null));
		lstTopPage.add(new TopPage(new TopPageCode("003"), null, new TopPageName("4"), null));
		lstTopPage.add(new TopPage(new TopPageCode("004"), null, new TopPageName("5"), null));
		TopPage tp = lstTopPage.stream().filter(item -> {
			return item.getTopPageCode().v().equals(topPageCode);
		}).findAny().orElse(null);
		;
		return Optional.of(tp);
	}

	@Override
	public void add(String CompanyId, TopPage topPage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(String CompanyId, TopPage topPage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(String CompanyId, String topPageCode) {
		// TODO Auto-generated method stub
		
	}
}
