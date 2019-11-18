package nts.uk.ctx.pr.shared.infra.repository.empinsqualifiinfo.employmentinsqualifiinfo;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.shared.dom.empinsqualifiinfo.employmentinsqualifiinfo.EmpInsGetInfo;
import nts.uk.ctx.pr.shared.dom.empinsqualifiinfo.employmentinsqualifiinfo.EmpInsGetInfoRepository;
import nts.uk.ctx.pr.shared.infra.entity.empinsqualifiinfo.employmentinsqualifiinfo.QqsmtEmpInsGetInfo;

import javax.ejb.Stateless;
import java.util.List;
import java.util.Optional;

@Stateless
public class JpaEmpInsGetInfoRepository extends JpaRepository implements EmpInsGetInfoRepository {

    private static final String SELECT_ALL_QUERY_STRING = "SELECT e FROM QqsmtEmpInsGetInfo e";
    private static final String SELECT_BY_KEY_STRING = SELECT_ALL_QUERY_STRING + " WHERE e.empInsGetInfoPk.sId =:sId";
    private static final String SELECT_BY_EMP_IDS = SELECT_ALL_QUERY_STRING + " WHERE e.empInsGetInfoPk.sid IN :sids";


    @Override
    public List<EmpInsGetInfo> getAllEmpInsGetInfo() {
        return null;
    }

    @Override
    public Optional<EmpInsGetInfo> getEmpInsGetInfoById(String sId) {
        return this.queryProxy().query(SELECT_BY_KEY_STRING, QqsmtEmpInsGetInfo.class)
                .setParameter("sId", sId)
                .getSingle(e -> e.toDomain());
    }

    @Override
    public void add(EmpInsGetInfo domain) {
        this.commandProxy().insert(QqsmtEmpInsGetInfo.toEntity(domain));
    }

    @Override
    public void update(EmpInsGetInfo domain) {
        this.commandProxy().update(QqsmtEmpInsGetInfo.toEntity(domain));
    }

    @Override
    public List<EmpInsGetInfo> getByEmpIds(List<String> empIds) {
        return this.queryProxy().query(SELECT_BY_EMP_IDS, QqsmtEmpInsGetInfo.class)
                .setParameter("sids", empIds)
                .getList(QqsmtEmpInsGetInfo::toDomain);
    }

}
