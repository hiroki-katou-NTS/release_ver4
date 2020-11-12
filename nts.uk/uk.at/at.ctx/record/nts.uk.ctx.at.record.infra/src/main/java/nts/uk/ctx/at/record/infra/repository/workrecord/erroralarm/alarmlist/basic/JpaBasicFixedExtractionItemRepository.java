package nts.uk.ctx.at.record.infra.repository.workrecord.erroralarm.alarmlist.basic;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.alarmlist.basic.BasicFixedExtractionCondition;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.alarmlist.basic.BasicFixedExtractionItem;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.alarmlist.basic.BasicFixedExtractionItemRepository;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.List;
import java.util.Optional;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class JpaBasicFixedExtractionItemRepository extends JpaRepository implements BasicFixedExtractionItemRepository {
    @Override
    public Optional<BasicFixedExtractionItem> getByID(String id) {
        return Optional.empty();
    }

    @Override
    public List<BasicFixedExtractionItem> getAll() {
        return null;
    }

    @Override
    public void register(BasicFixedExtractionCondition domain) {

    }

    @Override
    public void update(BasicFixedExtractionCondition domain) {

    }

    @Override
    public void delete(String id) {

    }
}
