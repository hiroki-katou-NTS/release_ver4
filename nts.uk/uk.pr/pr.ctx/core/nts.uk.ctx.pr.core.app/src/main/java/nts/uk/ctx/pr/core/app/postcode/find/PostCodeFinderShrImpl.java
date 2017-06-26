package nts.uk.ctx.pr.core.app.postcode.find;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.shr.find.postcode.IPostCodeFinder;
import nts.uk.shr.find.postcode.PostCode;
@Stateless
public class PostCodeFinderShrImpl implements IPostCodeFinder {

	/** The finder. */
	@Inject
	private PostCodeFinder finder;
	@Override
	public List<PostCode> findPostCodeList(String zipCode) {
		return this.finder.findPostCodeList(zipCode).stream().map(c-> new PostCode(c.getId(), c.getLocalGovCode(), c.getPostcode(),
				c.getPrefectureNameKn(), c.getMunicipalityNameKn(), 
				c.getTownNameKn(), c.getPrefectureName(), c.getMunicipalityName(), c.getTownName())).collect(Collectors.toList());
	}

}
