package nts.uk.ctx.sys.portal.dom.layout.service;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.gul.text.IdentifierUtil;
import nts.gul.text.StringUtil;
import nts.uk.ctx.sys.portal.dom.layout.Layout;
import nts.uk.ctx.sys.portal.dom.layout.LayoutRepository;
import nts.uk.ctx.sys.portal.dom.placement.service.PlacementService;
import nts.uk.shr.com.context.AppContexts;

/**
 * @author LamDT
 */
@Stateless
public class DefaultLayoutService implements LayoutService {

	@Inject
	private LayoutRepository layoutRepository;
		
	@Inject
	private PlacementService placementService;
	
	@Override
	public boolean isExist(String layoutID) {
		Optional<Layout> layout = layoutRepository.find(layoutID);
		return layout.isPresent();
	}

	@Override
	public void deleteLayout(String companyID, String layoutID) {
		if (isExist(layoutID)) {
			// Remove old data
			layoutRepository.remove(companyID, layoutID);
			placementService.deletePlacementByLayout(companyID, layoutID);
		}
	}
	
	@Override
	public String copyTopPageLayout(String layoutID) {
		return copyLayout(layoutID, 0);
	}

	@Override
	public String copyTitleMenuLayout(String layoutID) {
		return copyLayout(layoutID, 1);
	}
	
	@Override
	public String copyMyPageLayout(String layoutID) {
		return copyLayout(layoutID, 2);
	}
	
	private String copyLayout(String layoutID, int pgType) {
		if (StringUtil.isNullOrEmpty(layoutID, true) || !isExist(layoutID))
			return null;
		
		String companyID = AppContexts.user().companyId();
		String newLayoutID = IdentifierUtil.randomUniqueId();
		
		// Copy Layout
		Layout newLayout = Layout.createFromJavaType(companyID, newLayoutID, pgType);
		layoutRepository.add(newLayout);
		// Copy all Layout's Placement
		placementService.copyPlacementByLayout(layoutID, newLayoutID);
		
		return newLayoutID;
	}

}
