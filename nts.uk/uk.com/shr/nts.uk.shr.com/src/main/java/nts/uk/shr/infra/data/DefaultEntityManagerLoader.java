package nts.uk.shr.infra.data;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import nts.arc.layer.infra.data.EntityManagerLoader;

/**
 * DefaultEntityManagerLoader
 */
@Stateless
public class DefaultEntityManagerLoader implements EntityManagerLoader {

    @PersistenceContext(unitName = "UK")
    private EntityManager entityManager;
    
    @Override
    public EntityManager getEntityManager() {
        return this.entityManager;
    }
}
