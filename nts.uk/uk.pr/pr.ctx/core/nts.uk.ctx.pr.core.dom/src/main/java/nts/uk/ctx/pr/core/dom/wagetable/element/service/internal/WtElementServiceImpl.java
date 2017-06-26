/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.wagetable.element.service.internal;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.core.dom.wagetable.ElementType;
import nts.uk.ctx.pr.core.dom.wagetable.element.service.WtElementService;
import nts.uk.ctx.pr.core.dom.wagetable.reference.WtCodeRef;
import nts.uk.ctx.pr.core.dom.wagetable.reference.WtCodeRefRepository;
import nts.uk.ctx.pr.core.dom.wagetable.reference.WtMasterRef;
import nts.uk.ctx.pr.core.dom.wagetable.reference.WtMasterRefRepository;

/**
 * The Class WtElementServiceImpl.
 */
@Stateless
public class WtElementServiceImpl implements WtElementService {

	/** The wt code ref repo. */
	@Inject
	private WtCodeRefRepository wtCodeRefRepo;

	/** The wt master ref repo. */
	@Inject
	private WtMasterRefRepository wtMasterRefRepo;

	/**
	 * Gets the demension name.
	 *
	 * @param companyCode
	 *            the company code
	 * @param referenceCode
	 *            the reference code
	 * @param type
	 *            the type
	 * @return the demension name
	 */
	public String getDemensionName(String companyCode, String referenceCode, ElementType type) {
		// Check element type.
		switch (type) {
			case MASTER_REF: {
				// Get master ref by ref code.
				Optional<WtMasterRef> optWtMasterRef = this.wtMasterRefRepo.findByCode(companyCode,
						referenceCode);
	
				// Check present
				if (!optWtMasterRef.isPresent()) {
					return null;
				}
	
				// Return ref name.
				return optWtMasterRef.get().getRefName();
			}
	
			/** The code ref. */
			case CODE_REF: {
				// Get code ref by ref code.
				Optional<WtCodeRef> optWtCodeRef = this.wtCodeRefRepo.findByCode(companyCode,
						referenceCode);
	
				// Check present
				if (!optWtCodeRef.isPresent()) {
					return null;
				}
	
				// Return ref name.
				return optWtCodeRef.get().getRefName();
			}
	
			/** The item data ref. */
			case ITEM_DATA_REF:
				return ElementType.ITEM_DATA_REF.displayName;
	
			/** The experience fix. */
			case EXPERIENCE_FIX:
				return ElementType.EXPERIENCE_FIX.displayName;
	
			/** The age fix. */
			case AGE_FIX:
				return ElementType.AGE_FIX.displayName;
	
			/** The family mem fix. */
			case FAMILY_MEM_FIX:
				return ElementType.FAMILY_MEM_FIX.displayName;
	
			// Extend element type
			/** The certification. */
			case CERTIFICATION:
				return ElementType.CERTIFICATION.displayName;
	
			/** The working day. */
			case WITHOUT_WORKING_DAY:
				return ElementType.WITHOUT_WORKING_DAY.displayName;
	
			/** The come late. */
			case COME_LATE:
				return ElementType.COME_LATE.displayName;
	
			/** The level. */
			case LEVEL:
				return ElementType.LEVEL.displayName;
	
			default:
				return null;
		}
	}

}
