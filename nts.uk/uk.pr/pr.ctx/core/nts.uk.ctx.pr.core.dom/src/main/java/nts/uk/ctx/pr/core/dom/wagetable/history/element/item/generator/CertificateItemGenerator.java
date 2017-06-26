/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.wagetable.history.element.item.generator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.gul.collection.CollectionUtil;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.pr.core.dom.wagetable.ElementId;
import nts.uk.ctx.pr.core.dom.wagetable.ElementType;
import nts.uk.ctx.pr.core.dom.wagetable.certification.Certification;
import nts.uk.ctx.pr.core.dom.wagetable.certification.CertificationRepository;
import nts.uk.ctx.pr.core.dom.wagetable.history.element.ElementSetting;
import nts.uk.ctx.pr.core.dom.wagetable.history.element.item.CodeItem;
import nts.uk.ctx.pr.core.dom.wagetable.history.element.item.Item;

/**
 * The Class CertificateItemGenerator.
 */
@Stateless
public class CertificateItemGenerator implements ItemGenerator {

	/** The certification repo. */
	@Inject
	private CertificationRepository certificationRepo;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.wagetable.history.element.item.generator.
	 * ItemGenerator#generate(nts.uk.ctx.pr.core.dom.wagetable.history.element.
	 * ElementSetting)
	 */
	@Override
	public List<? extends Item> generate(String companyCode, String historyId,
			ElementSetting elementSetting) {
		// Get all certification.
		List<Certification> certifications = this.certificationRepo.findAll(companyCode);

		// Check has items.
		if (CollectionUtil.isEmpty(certifications)) {
			throw new BusinessException(new RawErrorMessage(
					"Have not any items on demension  " + elementSetting.getDemensionNo().value
							+ ": " + elementSetting.getType().displayName));
		}

		// Create map: unique code - old uuid.
		@SuppressWarnings("unchecked")
		List<CodeItem> codeItems = (List<CodeItem>) elementSetting.getItemList();
		Map<String, ElementId> mapCodeItems = codeItems.stream()
				.collect(Collectors.toMap(CodeItem::getReferenceCode, CodeItem::getUuid));

		// Generate uuid of code items.
		return certifications.stream().map(item -> {
			// Create code item.
			CodeItem codeItem = new CodeItem(item.getCode(), mapCodeItems
					.getOrDefault(item.getCode(), new ElementId(IdentifierUtil.randomUniqueId())));
			codeItem.setDisplayName(item.getName());

			// Return
			return codeItem;
		}).collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.wagetable.history.element.item.generator.
	 * ItemGenerator#canHandle(nts.uk.ctx.pr.core.dom.wagetable.ElementType)
	 */
	@Override
	public boolean canHandle(ElementType type) {
		return type == ElementType.CERTIFICATION;
	}

}
