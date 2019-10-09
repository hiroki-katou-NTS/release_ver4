/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.pubimp.optitem;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.optitem.OptionalItemRepository;
import optitem.OptionalItemExport;
import optitem.OptionalItemPub;

/**
 * The Class OptionalItemPubImp.
 */
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Stateless
public class OptionalItemPubImp implements OptionalItemPub {

	/** The opt item repo. */
	@Inject
	private OptionalItemRepository optItemRepo;

	/*
	 * (non-Javadoc)
	 * 
	 * @see optitem.OptionalItemPub#getOptionalItems(java.lang.String,
	 * java.util.List)
	 */
	@Override
	public List<OptionalItemExport> getOptionalItems(String companyId, List<Integer> optionalItemNos) {
		return this.optItemRepo.findByListNos(companyId, optionalItemNos).stream()
				.map(optItem -> OptionalItemExport.builder().optionalItemName(optItem.getOptionalItemName().v())
						.optionalItemNo(optItem.getOptionalItemNo().v()).optionalItemUnit(optItem.getUnit().v()).build())
				.collect(Collectors.toList());
	}

}
