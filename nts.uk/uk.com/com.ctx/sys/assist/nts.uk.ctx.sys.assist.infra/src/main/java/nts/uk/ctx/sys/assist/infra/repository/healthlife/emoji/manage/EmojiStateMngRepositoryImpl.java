package nts.uk.ctx.sys.assist.infra.repository.healthlife.emoji.manage;

import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.sys.assist.dom.healthlife.emoji.manage.EmojiStateMng;
import nts.uk.ctx.sys.assist.dom.healthlife.emoji.manage.EmojiStateMngRepository;
import nts.uk.ctx.sys.assist.infra.entity.healthlife.emoji.manage.EmojiStateMngEntity;
import nts.uk.ctx.sys.assist.infra.entity.healthlife.emoji.manage.EmojiStateMngEntityPK;
import nts.uk.shr.com.context.AppContexts;

/*
 * Repository Implements UKDesign.ドメインモデル.NittsuSystem.UniversalK.NittsuSystem.Common (NtsCommons).ヘルスライフ.感情状態管理.感情状態管理.感情状態管理
 * 感情状態管理Repository Implements					
 */
@Stateless
public class EmojiStateMngRepositoryImpl extends JpaRepository implements EmojiStateMngRepository {

	private static EmojiStateMngEntity toEntity(EmojiStateMng domain) {
		EmojiStateMngEntity entity = new EmojiStateMngEntity();
		domain.setMemento(entity);
		return entity;
	}

	@Override
	public void insert(EmojiStateMng domain) {
		EmojiStateMngEntity entity = EmojiStateMngRepositoryImpl.toEntity(domain);
		entity.setContractCd(AppContexts.user().contractCode());
		this.commandProxy().insert(entity);

	}

	@Override
	public void update(EmojiStateMng domain) {
		EmojiStateMngEntity entity = EmojiStateMngRepositoryImpl.toEntity(domain);
		Optional<EmojiStateMngEntity> oldEntity = this.queryProxy().find(entity.getPk(), EmojiStateMngEntity.class);
		oldEntity.ifPresent(updateEntity -> {
			updateEntity.setManageEmojiState(entity.getManageEmojiState());
			updateEntity.setWearyMoodName(entity.getWearyMoodName());
			updateEntity.setSadMoodName(entity.getSadMoodName());
			updateEntity.setAverageMoodName(entity.getAverageMoodName());
			updateEntity.setGoodMoodName(entity.getGoodMoodName());
			updateEntity.setHappyMoodName(entity.getHappyMoodName());
			this.commandProxy().update(updateEntity);
		});
	}

	@Override
	public Optional<EmojiStateMng> getByCid(String cid) {
		return this.queryProxy().find(new EmojiStateMngEntityPK(cid), EmojiStateMngEntity.class)
				.map(EmojiStateMng::createFromMemento);
	}
}
