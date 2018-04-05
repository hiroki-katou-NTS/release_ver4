package nts.uk.ctx.at.request.infra.repository.mail;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.request.dom.mail.UrlTaskIncre;
import nts.uk.ctx.at.request.dom.mail.UrlTaskIncreRepository;
import nts.uk.ctx.at.request.infra.entity.mail.SgwmtUrlTaskIncre;
import nts.uk.ctx.at.request.infra.entity.mail.SgwmtUrlTaskIncrePk;

@Stateless
public class JpaUrlTaskIncreRepository extends JpaRepository implements UrlTaskIncreRepository
{

    private static final String SELECT_ALL_QUERY_STRING = "SELECT f FROM SgwmtUrlTaskIncre f";
    private static final String SELECT_BY_KEY_STRING = SELECT_ALL_QUERY_STRING + " WHERE  f.urlTaskIncrePk.embeddedId =:embeddedId AND  f.urlTaskIncrePk.cid =:cid AND  f.urlTaskIncrePk.taskIncreId =:taskIncreId ";

    @Override
    public List<UrlTaskIncre> getAllUrlTaskIncre(){
        return this.queryProxy().query(SELECT_ALL_QUERY_STRING, SgwmtUrlTaskIncre.class)
                .getList(item -> item.toDomain());
    }

    @Override
    public Optional<UrlTaskIncre> getUrlTaskIncreById(String embeddedId, String cid, String taskIncreId){
        return this.queryProxy().query(SELECT_BY_KEY_STRING, SgwmtUrlTaskIncre.class)
        .setParameter("embeddedId", embeddedId)
        .setParameter("cid", cid)
        .setParameter("taskIncreId", taskIncreId)
        .getSingle(c->c.toDomain());
    }

    @Override
    public void add(UrlTaskIncre domain){
        this.commandProxy().insert(SgwmtUrlTaskIncre.toEntity(domain));
    }

    @Override
    public void update(UrlTaskIncre domain){
        this.commandProxy().update(SgwmtUrlTaskIncre.toEntity(domain));
    }

    @Override
    public void remove(String embeddedId, String cid, String taskIncreId){
        this.commandProxy().remove(SgwmtUrlTaskIncre.class, new SgwmtUrlTaskIncrePk(embeddedId, cid, taskIncreId)); 
    }
}
