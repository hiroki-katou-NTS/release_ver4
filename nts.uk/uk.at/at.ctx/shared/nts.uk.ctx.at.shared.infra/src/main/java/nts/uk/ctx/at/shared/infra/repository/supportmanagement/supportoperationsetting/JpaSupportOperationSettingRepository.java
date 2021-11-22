package nts.uk.ctx.at.shared.infra.repository.supportmanagement.supportoperationsetting;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.shared.dom.supportmanagement.supportoperationsetting.MaximumNumberOfSupport;
import nts.uk.ctx.at.shared.dom.supportmanagement.supportoperationsetting.SupportOperationSetting;
import nts.uk.ctx.at.shared.dom.supportmanagement.supportoperationsetting.SupportOperationSettingRepository;
import nts.uk.ctx.at.shared.infra.entity.supportmanagement.supportoperationsetting.KshmtSupportOperationSetting;

import javax.ejb.Stateless;
import java.util.Optional;

@Stateless
public class JpaSupportOperationSettingRepository extends JpaRepository implements SupportOperationSettingRepository {

    @Override
    public void update(String cid, SupportOperationSetting supportOperationSetting) {
        Optional<KshmtSupportOperationSetting> op = this.queryProxy().find(cid, KshmtSupportOperationSetting.class);
        if(op.isPresent()){
            KshmtSupportOperationSetting entity = op.get();
            this.toEntity(supportOperationSetting, entity);
            this.commandProxy().update(entity);
        }
    }

    @Override
    public SupportOperationSetting get(String cid) {
        Optional<KshmtSupportOperationSetting> op = this.queryProxy().find(cid, KshmtSupportOperationSetting.class);
        if(op.isPresent()){
            KshmtSupportOperationSetting entity = op.get();
            SupportOperationSetting domain = new SupportOperationSetting(entity.isAvailable(), entity.isCanRecipientChooseSupporter(), new MaximumNumberOfSupport(entity.getMaxTimesPerDayOfSupport()));
            return domain;
        }
        return null;
    }

    private void toEntity(SupportOperationSetting domain, KshmtSupportOperationSetting entity){
        entity.setAvailable(domain.isUsed());
        entity.setCanRecipientChooseSupporter(domain.isSupportDestinationCanSpecifySupporter());
        entity.setMaxTimesPerDayOfSupport(domain.getMaxNumberOfSupportOfDay().v());
    }


}
