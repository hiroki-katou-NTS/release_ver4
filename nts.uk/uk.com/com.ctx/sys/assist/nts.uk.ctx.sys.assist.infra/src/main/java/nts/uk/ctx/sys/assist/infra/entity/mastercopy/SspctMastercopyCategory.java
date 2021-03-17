package nts.uk.ctx.sys.assist.infra.entity.mastercopy;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The persistent class for the SSPCT_MASTERCOPY_CATEGORY database table.
 * 
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="SSPCT_MASTERCOPY_CATEGORY")
public class SspctMastercopyCategory  implements Serializable {
	private static final long serialVersionUID = 1L;

	/** The category no. */
	@Id
	@Column(name="CATEGORY_NO")
	private Integer categoryNo;

	/** The category name. */
	@Column(name="CATEGORY_NAME")
	private String categoryName;

	/** The category order. */
	@Column(name="CATEGORY_ORDER")
	private BigDecimal categoryOrder;

	/** The system type. */
	@Column(name="SYSTEM_TYPE")
	private BigDecimal systemType;

}