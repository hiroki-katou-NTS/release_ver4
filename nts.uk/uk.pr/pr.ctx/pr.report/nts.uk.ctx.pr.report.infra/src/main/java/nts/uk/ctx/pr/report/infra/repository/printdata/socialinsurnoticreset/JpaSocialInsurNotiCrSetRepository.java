package nts.uk.ctx.pr.report.infra.repository.printdata.socialinsurnoticreset;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.report.dom.printdata.socinsurnoticreset.SocialInsurNotiCrSetRepository;
import nts.uk.ctx.pr.report.dom.printdata.socinsurnoticreset.SocialInsurNotiCreateSet;
import nts.uk.ctx.pr.report.infra.entity.printdata.socialinsurnoticreset.QqsmtSocInsuNotiSet;
import nts.uk.ctx.pr.report.infra.entity.printdata.socialinsurnoticreset.QqsmtSocInsuNotiSetPk;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JpaSocialInsurNotiCrSetRepository extends JpaRepository implements SocialInsurNotiCrSetRepository {

    private static final String SELECT_ALL_QUERY_STRING = "SELECT f FROM QqsmtSocInsuNotiSet f";
    private static final String SELECT_BY_KEY_STRING = SELECT_ALL_QUERY_STRING + " WHERE  f.socInsuNotiSetPk.userId =:userId AND  f.socInsuNotiSetPk.cid =:cid ";

    @Override
    public Optional<SocialInsurNotiCreateSet> getSocialInsurNotiCreateSetById(String userId, String cid) {
        return this.queryProxy().query(SELECT_BY_KEY_STRING, QqsmtSocInsuNotiSet.class)
                .setParameter("userId", userId)
                .setParameter("cid", cid)
                .getSingle(c->c.toDomain());
    }

    @Override
    public void add(SocialInsurNotiCreateSet domain) {
        this.commandProxy().insert(QqsmtSocInsuNotiSet.toEntity(domain));
    }

    @Override
    public void update(SocialInsurNotiCreateSet domain) {
        this.commandProxy().update(QqsmtSocInsuNotiSet.toEntity(domain));
    }

    @Override
    public void remove(String userId, String cid) {
        this.commandProxy().remove(QqsmtSocInsuNotiSet.class, new QqsmtSocInsuNotiSetPk(userId, cid));
    }
}
