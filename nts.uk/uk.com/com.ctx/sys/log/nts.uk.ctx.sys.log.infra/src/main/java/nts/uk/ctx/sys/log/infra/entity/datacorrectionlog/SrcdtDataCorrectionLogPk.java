package nts.uk.ctx.sys.log.infra.entity.datacorrectionlog;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 
 * @author HungTT
 *
 */

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class SrcdtDataCorrectionLogPk {

	@Column(name = "OPERATION_ID")
	@Basic(optional = false)
	String operationId;

	@Column(name = "USER_ID")
	@Basic(optional = false)
	String userId;

	@Column(name = "TARGET_DATA_TYPE")
	@Basic(optional = false)
	int targetDataType;
	
	@Column(name = "ITEM_ID")
	@Basic(optional = false)
	String itemId;

}
