/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.ws.wagetable.inittable.dto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Data;
import nts.uk.ctx.pr.core.dom.wagetable.certification.Certification;
import nts.uk.ctx.pr.core.dom.wagetable.certification.CertifyGroup;
import nts.uk.ctx.pr.core.dom.wagetable.certification.CertifyGroupCode;
import nts.uk.ctx.pr.core.dom.wagetable.certification.CertifyGroupName;
import nts.uk.ctx.pr.core.dom.wagetable.certification.CertifyGroupSetMemento;
import nts.uk.ctx.pr.core.dom.wagetable.certification.MultipleTargetSetting;

/**
 * The Class CodeItem.
 */
@Data
public class CertifyGroupDto {

	/** The code. */
	private String code;

	/** The name. */
	private String name;

	/** The memo. */
	private Integer multiApplySet;

	/** The certify items. */
	private List<CertifyItemDto> certifyItems;

	/**
	 * From domain.
	 *
	 * @param wtHistory
	 *            the wt history
	 * @return the wt history dto
	 */
	public CertifyGroupDto fromDomain(CertifyGroup certifyGroup) {
		CertifyGroupDto dto = this;

		certifyGroup.saveToMemento(new CgSetMemento(dto));

		return dto;
	}

	/**
	 * The Class CgSetMemento.
	 */
	private class CgSetMemento implements CertifyGroupSetMemento {

		/** The dto. */
		private CertifyGroupDto dto;

		/**
		 * Instantiates a new cg set memento.
		 *
		 * @param dto
		 *            the dto
		 */
		public CgSetMemento(CertifyGroupDto dto) {
			this.dto = dto;
		}

		@Override
		public void setCompanyCode(String companyCode) {
			// Do nothing.
		}

		@Override
		public void setCode(CertifyGroupCode code) {
			this.dto.code = code.v();
		}

		@Override
		public void setName(CertifyGroupName name) {
			this.dto.name = name.v();
		}

		@Override
		public void setMultiApplySet(MultipleTargetSetting multiApplySet) {
			this.dto.multiApplySet = multiApplySet.value;
		}

		@Override
		public void setCertifies(Set<Certification> certifies) {
			this.dto.certifyItems = certifies.stream().map(item -> {
				CertifyItemDto certifyItemDto = new CertifyItemDto();
				item.saveToMemento(certifyItemDto);
				return certifyItemDto;
			}).collect(Collectors.toList());
		}
	}

}
