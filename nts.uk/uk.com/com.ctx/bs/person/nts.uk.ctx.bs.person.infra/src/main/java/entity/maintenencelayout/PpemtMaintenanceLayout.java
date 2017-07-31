/**
 * 
 */
package entity.maintenencelayout;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import entity.roles.PpemtPersonRolePk;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * @author laitv
 *
 */

@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PPEMT_MAINTENANCE_LAYOUT")
@Entity
public class PpemtMaintenanceLayout extends UkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@EmbeddedId
	public PpemtMaintenanceLayoutPk ppemtMaintenanceLayoutPk;
	
	@Basic(optional = false)
	@Column(name = "CID")
	public String companyId;
	
	@Basic(optional = false)
	@Column(name = "LAYOUT_CD")
	public String layoutCode;
	
	@Basic(optional = false)
	@Column(name = "LAYOUT_NAME")
	public String layoutName;

	@Override
	protected Object getKey() {
		return this.ppemtMaintenanceLayoutPk;
	}
}
