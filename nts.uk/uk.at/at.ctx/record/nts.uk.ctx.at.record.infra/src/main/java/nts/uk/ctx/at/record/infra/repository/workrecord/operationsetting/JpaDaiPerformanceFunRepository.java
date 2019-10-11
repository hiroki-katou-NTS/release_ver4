package nts.uk.ctx.at.record.infra.repository.workrecord.operationsetting;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.DaiPerformanceFun;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.DaiPerformanceFunRepository;
import nts.uk.ctx.at.record.infra.entity.workrecord.operationsetting.KrcmtDaiPerformEdFun;
import nts.uk.ctx.at.record.infra.entity.workrecord.operationsetting.KrcmtDaiPerformEdFunPk;

@Stateless
public class JpaDaiPerformanceFunRepository extends JpaRepository implements DaiPerformanceFunRepository
{

    private static final String SELECT_ALL_QUERY_STRING = "SELECT f FROM KrcmtDaiPerformEdFun f";
    private static final String SELECT_BY_KEY_STRING = SELECT_ALL_QUERY_STRING + " WHERE  f.daiPerformanceFunPk.cid =:cid ";

    @Override
    public List<DaiPerformanceFun> getAllDaiPerformanceFun(){
        return this.queryProxy().query(SELECT_ALL_QUERY_STRING, KrcmtDaiPerformEdFun.class)
                .getList(item -> item.toDomain());
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    @Override
    public Optional<DaiPerformanceFun> getDaiPerformanceFunById(String cid){
        return this.queryProxy().query(SELECT_BY_KEY_STRING, KrcmtDaiPerformEdFun.class)
        .setParameter("cid", cid)
        .getSingle(c->c.toDomain());
    }

    @Override
    public void add(DaiPerformanceFun domain){
        this.commandProxy().insert(KrcmtDaiPerformEdFun.toEntity(domain));
    }

    @Override
    public void update(DaiPerformanceFun domain){
        KrcmtDaiPerformEdFun newDaiPerformanceFun = KrcmtDaiPerformEdFun.toEntity(domain);
        KrcmtDaiPerformEdFun updateDaiPerformanceFun = this.queryProxy().find(newDaiPerformanceFun.daiPerformanceFunPk, KrcmtDaiPerformEdFun.class).get();
        if (null == updateDaiPerformanceFun) {
            return;
        }
        updateDaiPerformanceFun.comment = newDaiPerformanceFun.comment;
        updateDaiPerformanceFun.monthChkMsgAtr = newDaiPerformanceFun.monthChkMsgAtr;
        updateDaiPerformanceFun.disp36Atr = newDaiPerformanceFun.disp36Atr;
        updateDaiPerformanceFun.clearManuAtr = newDaiPerformanceFun.clearManuAtr;
        updateDaiPerformanceFun.flexDispAtr = newDaiPerformanceFun.flexDispAtr;
        updateDaiPerformanceFun.breakCalcUpdAtr = newDaiPerformanceFun.breakCalcUpdAtr;
        updateDaiPerformanceFun.breakTimeAutoAtr = newDaiPerformanceFun.breakTimeAutoAtr;
        updateDaiPerformanceFun.ealyCalcUpdAtr = newDaiPerformanceFun.ealyCalcUpdAtr;
        updateDaiPerformanceFun.overtimeCalcUpdAtr = newDaiPerformanceFun.overtimeCalcUpdAtr;
        updateDaiPerformanceFun.lawOverCalcUpdAtr = newDaiPerformanceFun.lawOverCalcUpdAtr;
        updateDaiPerformanceFun.manualFixAutoSetAtr = newDaiPerformanceFun.manualFixAutoSetAtr;
        updateDaiPerformanceFun.checkErrRefDisp = newDaiPerformanceFun.checkErrRefDisp;
        this.commandProxy().update(updateDaiPerformanceFun);
    }

    @Override
    public void remove(String cid){
        this.commandProxy().remove(KrcmtDaiPerformEdFun.class, new KrcmtDaiPerformEdFunPk(cid)); 
    }
}
