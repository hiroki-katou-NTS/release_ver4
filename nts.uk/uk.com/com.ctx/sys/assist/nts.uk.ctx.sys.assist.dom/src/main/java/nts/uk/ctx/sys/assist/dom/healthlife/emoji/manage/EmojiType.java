package nts.uk.ctx.sys.assist.dom.healthlife.emoji.manage;

/*
 * UKDesign.ドメインモデル.NittsuSystem.UniversalK.NittsuSystem.Common (NtsCommons).ヘルスライフ.感情状態管理.感情種類
 */
public enum EmojiType {

	// どんより
	WEARY(0),
	// ゆううつ
	SAD(1),
	// 普通
	AVERAGE(2),
	// ぼちぼち
	GOOD(3),
	// いい感じ
	HAPPY(4);								

	public int value;

	EmojiType(int val) {
		this.value = val;
	}
}
