package nts.uk.ctx.at.record.infra.repository.bonuspay;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.record.infra.entity.bonuspay.KbpstBPTimeItemSetting;
import nts.uk.ctx.at.record.infra.entity.bonuspay.KbpstBPTimeItemSettingPK;
import nts.uk.ctx.at.shared.dom.bonuspay.repository.BPTimeItemSettingRepository;
import nts.uk.ctx.at.shared.dom.bonuspay.timeitem.BPTimeItemSetting;

@Stateless
public class JpaBPTimeItemSettingRepository extends JpaRepository implements BPTimeItemSettingRepository {
	private final String SELECT_BPTIMEITEMSET_BY_COMPANYID = "SELECT c " + " FROM KbpstBPTimeItemSetting c "
			+ " JOIN  KbpstBonusPayTimeItem k "
			+ " ON c.kbpstBPTimeItemSettingPK.timeItemNo = k.kbpstBonusPayTimeItemPK.timeItemNo "
			+ " AND c.kbpstBPTimeItemSettingPK.timeItemTypeAtr = k.kbpstBonusPayTimeItemPK.timeItemTypeAtr "
			+ " WHERE c.kbpstBPTimeItemSettingPK.companyId = :companyId "
			+ " AND c.kbpstBPTimeItemSettingPK.timeItemTypeAtr = 0 " + " AND k.useAtr = 1 "
			+ " ORDER BY k.kbpstBonusPayTimeItemPK.timeItemNo";

	private final String SELECT_SPEC_BPTIMEITEMSET_BY_COMPANYID = "SELECT c " + " FROM KbpstBPTimeItemSetting c "
			+ " JOIN KbpstBonusPayTimeItem k "
			+ " ON c.kbpstBPTimeItemSettingPK.timeItemNo = k.kbpstBonusPayTimeItemPK.timeItemNo "
			+ " AND c.kbpstBPTimeItemSettingPK.timeItemTypeAtr = k.kbpstBonusPayTimeItemPK.timeItemTypeAtr "
			+ " WHERE c.kbpstBPTimeItemSettingPK.companyId = :companyId "
			+ " AND k.kbpstBonusPayTimeItemPK.timeItemTypeAtr = 1 " + " AND k.useAtr = 1 "
			+ " ORDER BY k.kbpstBonusPayTimeItemPK.timeItemNo";

	@Override
	public List<BPTimeItemSetting> getListSetting(String companyId) {
		return this.queryProxy().query(SELECT_BPTIMEITEMSET_BY_COMPANYID, KbpstBPTimeItemSetting.class)
				.setParameter("companyId", companyId).getList(x -> this.toBPTimeItemSettingDomain(x));
	}

	@Override
	public List<BPTimeItemSetting> getListSpecialSetting(String companyId) {
		return this.queryProxy().query(SELECT_SPEC_BPTIMEITEMSET_BY_COMPANYID, KbpstBPTimeItemSetting.class)
				.setParameter("companyId", companyId).getList(x -> this.toBPTimeItemSettingDomain(x));
	}

	@Override
	public void addListSetting(List<BPTimeItemSetting> lstSetting) {
		List<KbpstBPTimeItemSetting> lstKbpstBPTimeItemSetting = lstSetting.stream()
				.map(c -> toBPTimeItemSettingEntity(c)).collect(Collectors.toList());
		this.commandProxy().insertAll(lstKbpstBPTimeItemSetting);
	}

	@Override
	public void updateListSetting(List<BPTimeItemSetting> lstSetting) {
		lstSetting.forEach(c -> {
			Optional<KbpstBPTimeItemSetting> kbpstBPTimeItemSettingOptional = this.queryProxy()
					.find(new KbpstBPTimeItemSettingPK(c.getCompanyId().toString(), new BigDecimal(c.getTimeItemNo()), new BigDecimal(c.getTimeItemTypeAtr().value)),
							KbpstBPTimeItemSetting.class);
			if (kbpstBPTimeItemSettingOptional.isPresent()) {
				KbpstBPTimeItemSetting kbpstBPTimeItemSetting = kbpstBPTimeItemSettingOptional.get();
				kbpstBPTimeItemSetting.holidayCalSettingAtr = new BigDecimal(c.getHolidayCalSettingAtr().value);
				kbpstBPTimeItemSetting.overtimeCalSettingAtr = new BigDecimal(c.getOvertimeCalSettingAtr().value);
				kbpstBPTimeItemSetting.worktimeCalSettingAtr = new BigDecimal(c.getWorktimeCalSettingAtr().value);
				this.commandProxy().update(kbpstBPTimeItemSetting);
			} else {
				this.commandProxy().insert(c);
			}
		});
	}

	private BPTimeItemSetting toBPTimeItemSettingDomain(KbpstBPTimeItemSetting kbpstBPTimeItemSetting) {
		return BPTimeItemSetting.createFromJavaType(kbpstBPTimeItemSetting.kbpstBPTimeItemSettingPK.companyId,
				kbpstBPTimeItemSetting.kbpstBPTimeItemSettingPK.timeItemNo.intValue(),
				kbpstBPTimeItemSetting.holidayCalSettingAtr.intValue(),
				kbpstBPTimeItemSetting.overtimeCalSettingAtr.intValue(),
				kbpstBPTimeItemSetting.worktimeCalSettingAtr.intValue(),
				kbpstBPTimeItemSetting.kbpstBPTimeItemSettingPK.timeItemTypeAtr.intValue());

	}

	private KbpstBPTimeItemSetting toBPTimeItemSettingEntity(BPTimeItemSetting bpTimeItemSetting) {

		return new KbpstBPTimeItemSetting(
				new KbpstBPTimeItemSettingPK(bpTimeItemSetting.getCompanyId().toString(),
						new BigDecimal(bpTimeItemSetting.getTimeItemNo()),
						new BigDecimal(bpTimeItemSetting.getTimeItemTypeAtr().value)),
				new BigDecimal(bpTimeItemSetting.getHolidayCalSettingAtr().value),
				new BigDecimal(bpTimeItemSetting.getOvertimeCalSettingAtr().value),
				new BigDecimal(bpTimeItemSetting.getWorktimeCalSettingAtr().value));
	}

}
