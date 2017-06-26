package nts.uk.shr.infra.data.entity;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.dom.AggregateRoot;

/**
 * Base class of table entity as aggregate root.
 */
@Getter
@Setter
@MappedSuperclass
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class AggregateTableEntity extends UkJpaEntity {
    
    /**
     * version
     */
    //@Version
	@Transient 
    protected long version;
    
    /**
     * Copy common fields of AggregateRoot to domain from this entity.
     * 
     * @param domain AggregateRoot
     */
    public void toDomain(AggregateRoot domain) {
    	domain.setVersion(this.version);
    }
    
    /**
     * Copy common fields of AggregateRoot to this entity from domain.
     * 
     * @param domain AggregateRoot
     */
    public void fromDomain(AggregateRoot domain) {
    	this.setVersion(domain.getVersion());
    }
}
