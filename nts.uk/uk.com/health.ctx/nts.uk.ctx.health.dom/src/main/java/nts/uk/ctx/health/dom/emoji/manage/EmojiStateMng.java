package nts.uk.ctx.health.dom.emoji.manage;

import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.shr.com.enumcommon.NotUseAtr;

/*
 * UKDesign.ドメインモデル.NittsuSystem.UniversalK.NittsuSystem.Common (NtsCommons).ヘルスライフ.感情状態管理.感情状態管理.感情状態管理
 */
public class EmojiStateMng extends AggregateRoot {
	// 会社ID
	private String cid;

	// 感情状態を管理する
	private NotUseAtr manageEmojiState;

	// 感情状態設定
	private EmojiStateDetail emojiStateSetting;

	private EmojiStateMng() {
	}

	public static EmojiStateMng createFromMemento(MementoGetter memento) {
		EmojiStateMng domain = new EmojiStateMng();
		domain.getMemento(memento);
		return domain;
	}

	public void getMemento(MementoGetter memento) {
		this.cid = memento.getCid();
		this.manageEmojiState = EnumAdaptor.valueOf(memento.getManageEmojiState(), NotUseAtr.class);
		this.emojiStateSetting = memento.getEmojiStateSetting();
	}

	public void setMemento(MementoSetter memento) {
		memento.setCid(this.cid);
		memento.setManageEmojiState(this.manageEmojiState.value);
		memento.setEmojiStateSetting(this.emojiStateSetting);
	}

	public interface MementoSetter {
		void setCid(String cid);

		void setManageEmojiState(Integer manageEmojiState);

		void setEmojiStateSetting(EmojiStateDetail emojiStateSetting);
	}

	public interface MementoGetter {
		String getCid();

		Integer getManageEmojiState();

		EmojiStateDetail getEmojiStateSetting();
	}
}
