package nts.uk.ctx.pr.core.dom.rule.law.tax.residential.service;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lanlt
 *
 */
public class JapanLocationService {

	/**
	 * Return list regions and prefectures of Japan follow the 
	 * define sheet of DBLayout file of QMM003 function
	 * @return
	 */
	public static List<RegionObject> getJapanLocation(){
		List<RegionObject> result = new ArrayList<>();
		List<PrefectureObject> region;
		region = new ArrayList<>();
		region.add(new PrefectureObject("01", "北海道"));
		result.add(new RegionObject("1", "北海道",region));
		region = new ArrayList<>();
		region.add(new PrefectureObject("02", "青森県"));
		region.add(new PrefectureObject("03", "岩手県"));
		region.add(new PrefectureObject("04", "宮城県"));
		region.add(new PrefectureObject("05", "秋田県"));
		region.add(new PrefectureObject("06", "山形県"));
		region.add(new PrefectureObject("07", "福島県"));
		result.add(new RegionObject("2", "東北",region));
		region = new ArrayList<>();
		region.add(new PrefectureObject("08", "茨城県"));
		region.add(new PrefectureObject("09", "栃木県"));
		region.add(new PrefectureObject("10", "群馬県"));
		region.add(new PrefectureObject("11", "埼玉県"));
		region.add(new PrefectureObject("12", "千葉県"));
		region.add(new PrefectureObject("13", "東京都"));
		region.add(new PrefectureObject("14", "神奈川県"));
		result.add(new RegionObject("3", "関東",region));
		region = new ArrayList<>();
		region.add(new PrefectureObject("15", "新潟県"));
		region.add(new PrefectureObject("16", "富山県"));
		region.add(new PrefectureObject("17", "石川県"));
		region.add(new PrefectureObject("18", "福井県"));
		region.add(new PrefectureObject("19", "山梨県"));
		region.add(new PrefectureObject("20", "長野県"));
		region.add(new PrefectureObject("21", "岐阜県"));
		region.add(new PrefectureObject("22", "静岡県"));
		region.add(new PrefectureObject("23", "愛知県"));
		result.add(new RegionObject("4", "中部",region));
		region = new ArrayList<>();
		region.add(new PrefectureObject("24", "三重県"));
		region.add(new PrefectureObject("25", "滋賀県"));
		region.add(new PrefectureObject("26", "京都府"));
		region.add(new PrefectureObject("27", "大阪府"));
		region.add(new PrefectureObject("28", "兵庫県"));
		region.add(new PrefectureObject("29", "奈良県"));
		region.add(new PrefectureObject("30", "和歌山県"));
		result.add(new RegionObject("5", "近畿",region));
		region = new ArrayList<>();
		region.add(new PrefectureObject("31", "鳥取県"));
		region.add(new PrefectureObject("32", "島根県"));
		region.add(new PrefectureObject("33", "岡山県"));
		region.add(new PrefectureObject("34", "広島県"));
		region.add(new PrefectureObject("35", "山口県"));
		result.add(new RegionObject("6", "中国",region));
		region = new ArrayList<>();
		region.add(new PrefectureObject("36", "徳島県"));
		region.add(new PrefectureObject("37", "香川県"));
		region.add(new PrefectureObject("38", "愛媛県"));
		region.add(new PrefectureObject("39", "高知県"));
		result.add(new RegionObject("7", "四国",region));
		region = new ArrayList<>();
		region.add(new PrefectureObject("40", "福岡県"));
		region.add(new PrefectureObject("41", "佐賀県"));
		region.add(new PrefectureObject("42", "長崎県"));
		region.add(new PrefectureObject("43", "熊本県"));
		region.add(new PrefectureObject("44", "大分県"));
		region.add(new PrefectureObject("45", "宮崎県"));
		region.add(new PrefectureObject("46", "鹿児島県"));
		region.add(new PrefectureObject("47", "沖縄県"));
		result.add(new RegionObject("8", "四国",region));
		return result;
	}
}