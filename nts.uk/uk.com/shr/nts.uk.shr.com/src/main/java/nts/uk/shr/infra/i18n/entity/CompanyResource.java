package nts.uk.shr.infra.i18n.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "CMNMT_RESOURCE")
public class CompanyResource {
	@EmbeddedId
	private CompanyResourcePK primaryKey;
	@Column(name = "ITEM_CONTENT")
	private String content;
	
}
