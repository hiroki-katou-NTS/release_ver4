package nts.uk.shr.infra.i18n.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.arc.i18n.custom.ResourceType;

@Entity
@Table(name = "CISMT_SYSTEM_RESOURCE")
@Getter
@Setter
public class SystemResource {
	@EmbeddedId
	private SystemResourcePK pk;

	@Column(name = "ITEM_CONTENT")
	private String content;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "RESOURCE_TYPE")
	private ResourceType type;

	// use for 予約
	@Column
	private boolean customizable;
}
