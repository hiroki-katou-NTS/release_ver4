package nts.uk.ctx.sys.assist.infra.repository.storage;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.sys.assist.dom.storage.TargetEmployees;
import nts.uk.ctx.sys.assist.dom.storage.TargetEmployeesRepository;
import nts.uk.ctx.sys.assist.infra.entity.storage.SspmtTargetEmployees;
import nts.uk.ctx.sys.assist.infra.entity.storage.SspmtTargetEmployeesPk;

@Stateless
public class JpaTargetEmployeesRepository extends JpaRepository implements TargetEmployeesRepository
{

    private static final String SELECT_ALL_QUERY_STRING = "SELECT f FROM SspmtTargetEmployees f";
    private static final String SELECT_BY_KEY_STRING = SELECT_ALL_QUERY_STRING + " WHERE  f.targetEmployeesPk.storeProcessingId =:storeProcessingId AND  f.targetEmployeesPk.employeeId =:employeeId ";

    @Override
    public List<TargetEmployees> getAllTargetEmployees(){
        return this.queryProxy().query(SELECT_ALL_QUERY_STRING, SspmtTargetEmployees.class)
                .getList(item -> item.toDomain());
    }

    @Override
    public Optional<TargetEmployees> getTargetEmployeesById(String storeProcessingId, String employeeId){
        return this.queryProxy().query(SELECT_BY_KEY_STRING, SspmtTargetEmployees.class)
        .setParameter("storeProcessingId", storeProcessingId)
        .setParameter("employeeId", employeeId)
        .getSingle(c->c.toDomain());
    }

    @Override
    public void add(TargetEmployees domain){
        this.commandProxy().insert(SspmtTargetEmployees.toEntity(domain));
    }

    @Override
    public void update(TargetEmployees domain){
        this.commandProxy().update(SspmtTargetEmployees.toEntity(domain));
    }

    @Override
    public void remove(String storeProcessingId, String employeeId){
        this.commandProxy().remove(SspmtTargetEmployees.class, new SspmtTargetEmployeesPk(storeProcessingId, employeeId)); 
    }
}
