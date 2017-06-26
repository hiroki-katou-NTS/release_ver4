/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.wagetable.certification.find;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.pr.core.app.wagetable.certification.find.dto.CertifyGroupFindDto;
import nts.uk.ctx.pr.core.app.wagetable.certification.find.dto.CertifyGroupFindOutDto;
import nts.uk.ctx.pr.core.dom.wagetable.certification.Certification;
import nts.uk.ctx.pr.core.dom.wagetable.certification.CertificationRepository;
import nts.uk.ctx.pr.core.dom.wagetable.certification.CertifyGroup;
import nts.uk.ctx.pr.core.dom.wagetable.certification.CertifyGroupCode;
import nts.uk.ctx.pr.core.dom.wagetable.certification.CertifyGroupGetMemento;
import nts.uk.ctx.pr.core.dom.wagetable.certification.CertifyGroupName;
import nts.uk.ctx.pr.core.dom.wagetable.certification.CertifyGroupRepository;
import nts.uk.ctx.pr.core.dom.wagetable.certification.MultipleTargetSetting;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

/**
 * The Class CertifyGroupFinder.
 */
@Stateless
public class CertifyGroupFinder {

	/** The certify repository. */
	@Inject
	private CertifyGroupRepository certifyRepository;

	/** The certifi repository. */
	@Inject
	private CertificationRepository certifiRepository;

	/**
	 * Inits the all.
	 *
	 * @return the list
	 */
	public List<CertifyGroup> initAll() {

		// Get the company login
		String companyCode = AppContexts.user().companyCode();

		// Get all
		List<CertifyGroup> certifyGroups = this.certifyRepository.findAll(companyCode);

		List<Certification> certifyNoneGroupItems = certifiRepository
			.findAllNoneOfGroup(companyCode);

		// Check exist none group item.
		if (!CollectionUtil.isEmpty(certifyNoneGroupItems)) {
			// Add group of none group items.
			certifyGroups.add(
				new CertifyGroup(new CertifyGroupDefaultImpl(companyCode, certifyNoneGroupItems)));
		}
		return certifyGroups;
	}

	/**
	 * Find all.
	 *
	 * @return the list
	 */
	public List<CertifyGroupFindOutDto> findAll() {

		// get info login
		LoginUserContext loginUserContext = AppContexts.user();

		// call findAll
		List<CertifyGroup> data = this.certifyRepository.findAll(loginUserContext.companyCode());

		// to Dto
		return data.stream().map(certifyGroup -> {
			CertifyGroupFindOutDto dto = new CertifyGroupFindOutDto();
			certifyGroup.saveToMemento(dto);
			return dto;
		}).collect(Collectors.toList());
	}

	/**
	 * Find.
	 *
	 * @param code
	 *            the code
	 * @return the certify group find dto
	 */
	public CertifyGroupFindDto find(String code) {

		// get info login
		LoginUserContext loginUserContext = AppContexts.user();
		CertifyGroupFindDto dataOutput = new CertifyGroupFindDto();

		// call findById
		Optional<CertifyGroup> data = this.certifyRepository
			.findById(loginUserContext.companyCode(), code);

		// not value find
		if (!data.isPresent()) {
			return null;
		}

		// to output
		data.get().saveToMemento(dataOutput);
		return dataOutput;
	}

	/**
	 * The Class CertifyGroupDefaultImpl.
	 */
	class CertifyGroupDefaultImpl implements CertifyGroupGetMemento {

		/** The company code. */
		private String companyCode;

		/** The certify none group items. */
		List<Certification> certifyNoneGroupItems;

		/**
		 * Instantiates a new certify group default impl.
		 *
		 * @param companyCode the company code
		 * @param certifyNoneGroupItems the certify none group items
		 */
		public CertifyGroupDefaultImpl(String companyCode,
			List<Certification> certifyNoneGroupItems) {
			this.companyCode = companyCode;
			this.certifyNoneGroupItems = certifyNoneGroupItems;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.pr.core.dom.wagetable.certification.CertifyGroupGetMemento
		 * #getCompanyCode()
		 */
		@Override
		public String getCompanyCode() {
			return this.companyCode;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.pr.core.dom.wagetable.certification.CertifyGroupGetMemento
		 * #getCode()
		 */
		@Override
		public CertifyGroupCode getCode() {
			return new CertifyGroupCode("000");
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.pr.core.dom.wagetable.certification.CertifyGroupGetMemento
		 * #getName()
		 */
		@Override
		public CertifyGroupName getName() {
			return new CertifyGroupName("グループ なし");
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.pr.core.dom.wagetable.certification.CertifyGroupGetMemento
		 * #getMultiApplySet()
		 */
		@Override
		public MultipleTargetSetting getMultiApplySet() {
			return MultipleTargetSetting.TotalMethod;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.pr.core.dom.wagetable.certification.CertifyGroupGetMemento
		 * #getCertifies()
		 */
		@Override
		public Set<Certification> getCertifies() {
			return certifyNoneGroupItems.stream().collect(Collectors.toSet());
		}

	}

}
