/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.portal.app.find.toppage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.sys.portal.dom.enums.TopPagePartType;
import nts.uk.ctx.sys.portal.dom.flowmenu.FlowMenuRepository;
import nts.uk.ctx.sys.portal.dom.layout.LayoutNew;
import nts.uk.ctx.sys.portal.dom.layout.LayoutNewRepository;
import nts.uk.ctx.sys.portal.dom.layout.LayoutType;
import nts.uk.ctx.sys.portal.dom.toppage.TopPage;
import nts.uk.ctx.sys.portal.dom.toppage.TopPageRepository;
import nts.uk.ctx.sys.portal.dom.toppage.ToppageNew;
import nts.uk.ctx.sys.portal.dom.toppage.ToppageNewRepository;
import nts.uk.ctx.sys.portal.dom.toppagepart.createflowmenu.CreateFlowMenuRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class TopPageFinder.
 */
@Stateless
public class TopPageFinder {
	@Inject
	private TopPageRepository topPageRepository;
	@Inject
	private ToppageNewRepository toppageNewRepository;
	@Inject
	private FlowMenuRepository flowMenuRepository;
	@Inject
	private CreateFlowMenuRepository CFlowMenuRepo;
	@Inject
	private LayoutNewRepository layoutNewRepository;

	public List<TopPageItemDto> findAll(String companyId) {
		// 会社の「トップページ」を全て取得する
		List<ToppageNew> listTopPage = toppageNewRepository.getByCid(companyId);
		// convert from domain to dto
		List<TopPageItemDto> lstTopPageItemDto = listTopPage.stream()
				.map(item -> new TopPageItemDto(item.getTopPageCode().v(), item.getTopPageName().v()))
				.collect(Collectors.toList());
		return lstTopPageItemDto;
	}

	public TopPageDto findByCode(String companyId, String topPageCode, String languageType) {
		Optional<TopPage> topPage = topPageRepository.findByCode(companyId, topPageCode);
		if (topPage.isPresent()) {
			TopPage tp = topPage.get();
			return TopPageDto.fromDomain(tp);
		}
		return null;
	}

	public TopPageNewDto findByCode(String companyId, String topPageCode) {
		Optional<ToppageNew> topPage = toppageNewRepository.getByCidAndCode(companyId, topPageCode);
		if (topPage.isPresent()) {
			ToppageNew tp = topPage.get();
			return TopPageNewDto.fromDomain(tp);
		}
		return null;
	}

	public LayoutNewDto getLayout(String topPageCd, int layoutNo) {
		String companyId = AppContexts.user().companyId();
		// ドメインモデル「レイアウト」を取得する
		Optional<LayoutNew> layout1 = layoutNewRepository.getByCidAndCode(companyId, topPageCd,
				BigDecimal.valueOf(layoutNo));
		if (layout1.isPresent()) {
			LayoutNewDto layoutDto = toDto(layout1.get());
			return layoutDto;
		}
		return null;
	}

	public List<FlowMenuOutput> getFlowMenuOrFlowMenuUploadList(String cId, String topPageCd, int layoutType) {
		List<FlowMenuOutput> listFlow = new ArrayList<FlowMenuOutput>();
		// ドメインモデル「レイアウト」を取得する
		Optional<LayoutNew> layout1 = layoutNewRepository.getByCidAndCode(cId, topPageCd, BigDecimal.valueOf(0));
		if (!layout1.isPresent()) {
			return Collections.emptyList();
		}
		// アルゴリズム「フローメニューの作成リストを取得する」を実行する
		if (layoutType == LayoutType.FLOW_MENU.value) {
			// アルゴリズム「フローメニューの作成リストを取得する」を実行する
			// Inputフローコードが指定されている場合
			listFlow = CFlowMenuRepo.findByCid(cId).stream()
					.map(item -> FlowMenuOutput.builder()
							.flowCode(item.getFlowMenuCode().v())
							.flowName(item.getFlowMenuName().v())
							.fileId(item.getFlowMenuLayout().map(x -> x.getFileId()).orElse(""))
							.build())
					.collect(Collectors.toList());
			
		} else if (layoutType == LayoutType.FLOW_MENU_UPLOAD.value) {
			// アルゴリズム「フローメニュー（アップロード）リストを取得する」を実行する
			// Inputフローコードが指定されている場合
				listFlow = this.flowMenuRepository.findByType(cId, TopPagePartType.FlowMenu.value).stream()
						.map(item -> FlowMenuOutput.builder()
								.flowCode(item.getCode().v())
								.flowName(item.getName().v())
								.fileId(item.getFileID())
								.build())
						.collect(Collectors.toList());
	
		}
		return listFlow;
	}

	private LayoutNewDto toDto(LayoutNew domain) {
		LayoutNewDto dto = new LayoutNewDto();
		domain.setMemento(dto);
		return dto;
	}
}
