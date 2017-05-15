/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.portal.app.find.toppage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;
import nts.uk.ctx.sys.portal.dom.placement.Placement;
import nts.uk.ctx.sys.portal.dom.toppage.TopPage;
import nts.uk.ctx.sys.portal.dom.toppagepart.TopPagePart;

/**
 * The Class TopPageDto.
 */
@Data
public class TopPageDto {

	/** The top page code. */
	private String topPageCode;

	/** The top page name. */
	private String topPageName;

	/** The Language number. */
	private Integer languageNumber;
	
	/** The layout id. */
	private String layoutId;
	
	/** The placement dto. */
	private List<PlacementDto> placementDto;

	/**
	 * From domain.
	 *
	 * @param topPage the top page
	 * @param lstPlacement the lst placement
	 * @param lstTopPagePart the lst top page part
	 * @return the top page dto
	 */
	public static TopPageDto fromDomain(TopPage topPage, List<Placement> lstPlacement,
			List<TopPagePart> lstTopPagePart) {
		TopPageDto topPageDto = new TopPageDto();
		topPageDto.topPageCode = topPage.getTopPageCode().v();
		topPageDto.topPageName = topPage.getTopPageName().v();
		topPageDto.languageNumber = topPage.getLanguageNumber();
		topPageDto.layoutId = topPage.getLayoutId();
		if (lstPlacement.isEmpty()) {
			topPageDto.placementDto = new ArrayList<>();
		} else {
			topPageDto.placementDto = lstPlacement.stream().map(item -> {
				return PlacementDto.fromDomain(item, lstTopPagePart.stream().filter(t -> {
					return t.getToppagePartID().equals(item.getToppagePartID());
				}).findFirst().get());
			}).collect(Collectors.toList());
		}
		return topPageDto;
	}
}
