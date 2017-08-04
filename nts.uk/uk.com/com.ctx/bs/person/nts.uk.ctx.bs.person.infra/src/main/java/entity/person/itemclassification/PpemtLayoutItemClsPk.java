/**
 * 
 */
package entity.person.itemclassification;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author laitv
 *
 */

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class PpemtLayoutItemClsPk implements Serializable{
	
	private static final long serialVersionUID = 1L;
	@Basic(optional = false)
    @Column(name = "LAYOUT_ID")
	public String layoutId;
	
	
	@Basic(optional = false)
    @Column(name = "DISPORDER ")
	public String dispOrder;
	
}
