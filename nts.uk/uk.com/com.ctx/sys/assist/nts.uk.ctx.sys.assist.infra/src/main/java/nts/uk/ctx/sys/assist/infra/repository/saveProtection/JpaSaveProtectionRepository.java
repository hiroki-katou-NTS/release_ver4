package nts.uk.ctx.sys.assist.infra.repository.saveProtection;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.sys.assist.dom.saveProtetion.SaveProtetion;
import nts.uk.ctx.sys.assist.dom.saveProtetion.SaveProtetionRepository;
import nts.uk.ctx.sys.assist.infra.entity.saveProtection.SspmtSaveProtection;
import nts.uk.ctx.sys.assist.infra.entity.saveProtection.SspmtSaveProtectionPk;

@Stateless
public class JpaSaveProtectionRepository extends JpaRepository implements SaveProtetionRepository
{

    private static final String SELECT_ALL_QUERY_STRING = "SELECT f FROM SspmtSaveProtection f";
    private static final String SELECT_BY_KEY_STRING = SELECT_ALL_QUERY_STRING + " WHERE ";

    @Override
    public List<SaveProtetion> getAllSaveProtection(){
        /*return this.queryProxy().query(SELECT_ALL_QUERY_STRING, SspmtSaveProtection.class)
                .getList(item -> item.toDomain());*/
    	return null;
    }

    @Override
    public Optional<SaveProtetion> getSaveProtectionById(){
        /*return this.queryProxy().query(SELECT_BY_KEY_STRING, SspmtSaveProtection.class)
        .getSingle(c->c.toDomain());*/
    	return null;
    }

    @Override
    public void add(SaveProtetion domain){
        //this.commandProxy().insert(SspmtSaveProtection.toEntity(domain));
    }

    @Override
    public void update(SaveProtetion domain){
        //this.commandProxy().update(SspmtSaveProtection.toEntity(domain));
    }

    @Override
    public void remove(){
        this.commandProxy().remove(SspmtSaveProtection.class, new SspmtSaveProtectionPk()); 
    }
}
