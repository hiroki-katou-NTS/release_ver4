package nts.uk.ctx.at.record.repository.workrecord.operationsetting;

import java.util.Optional;
import java.util.List;

import javax.ejb.Stateless;

import nts.uk.ctx.at.record.infra.entity.workrecord.operationsetting.KrcmtIdentityProcess;
import nts.uk.ctx.at.record.infra.entity.workrecord.operationsetting.KrcmtIdentityProcessPk;
import nts.uk.ctx.at.record..dom.workrecord.operationsetting.IdentityProcessRepository;
import nts.uk.ctx.at.record..dom.workrecord.operationsetting.IdentityProcess;
import nts.arc.layer.infra.data.JpaRepository;

@Stateless
public class JpaIdentityProcessRepository extends JpaRepository implements IdentityProcessRepository
{

    private static final String SELECT_ALL_QUERY_STRING = "SELECT f FROM KrcmtIdentityProcess f";
    private static final String SELECT_BY_KEY_STRING = SELECT_ALL_QUERY_STRING + " WHERE  f.identityProcessPk.cid =:cid ";

    @Override
    public List<IdentityProcess> getAllIdentityProcess(){
        return this.queryProxy().query(SELECT_ALL_QUERY_STRING, KrcmtIdentityProcess.class)
                .getList(item -> toDomain(item));
    }

    @Override
    public Optional<IdentityProcess> getIdentityProcessById(String cid){
        return this.queryProxy().query(SELECT_BY_KEY_STRING, KrcmtIdentityProcess.class)
        .setParameter("cid", cid)
        .getSingle(c->toDomain(c));
    }

    @Override
    public void add(IdentityProcess domain){
        this.commandProxy().insert(toEntity(domain));
    }

    @Override
    public void update(IdentityProcess domain){
        KrcmtIdentityProcess newIdentityProcess = toEntity(domain);
        KrcmtIdentityProcess updateIdentityProcess = this.queryProxy().find(newIdentityProcess.identityProcessPk, KrcmtIdentityProcess.class).get();
        if (null == updateIdentityProcess) {
            return;
        }
        updateIdentityProcess.useConfirmByYourself = newIdentityProcess.useConfirmByYourself;
        updateIdentityProcess.useIdentityOfMonth = newIdentityProcess.useIdentityOfMonth;
        updateIdentityProcess.yourselfConfirmError = newIdentityProcess.yourselfConfirmError;
        this.commandProxy().update(updateIdentityProcess);
    }

    @Override
    public void remove(String cid){
        this.commandProxy().remove(KrcmtIdentityProcess.class, new KrcmtIdentityProcessPk(cid)); 
    }
}
