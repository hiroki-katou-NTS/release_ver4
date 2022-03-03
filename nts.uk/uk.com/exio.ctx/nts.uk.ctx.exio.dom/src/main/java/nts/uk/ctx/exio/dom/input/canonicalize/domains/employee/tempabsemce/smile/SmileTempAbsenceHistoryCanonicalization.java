package nts.uk.ctx.exio.dom.input.canonicalize.domains.employee.tempabsemce.smile;

import java.util.ArrayList;
import java.util.List;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.exio.dom.input.canonicalize.domains.ItemNoMap;
import nts.uk.ctx.exio.dom.input.canonicalize.domains.employee.tempabsemce.TempAbsenceHistoryCanonicalization;
import nts.uk.ctx.exio.dom.input.canonicalize.domains.employee.tempabsemce.smile.pv.SmileTempAbsenceDataEndDate;
import nts.uk.ctx.exio.dom.input.canonicalize.result.CanonicalItem;
import nts.uk.ctx.exio.dom.input.canonicalize.result.IntermediateResult;

public class SmileTempAbsenceHistoryCanonicalization extends TempAbsenceHistoryCanonicalization{
	
	@Override
	public ItemNoMap getItemNoMap() {
		return ItemNoMap.reflection(Items.class);
	}

	public static class Items {
		public static final int 社員コード = 1;
		public static final int 開始日 = 2;
		public static final int 終了日 = 3;
		public static final int 休職休業枠NO = 4;
		public static final int 備考 = 5;
		public static final int SID = 101;
		public static final int HIST_ID = 102;
		public static final int 社会保険支給対象区分 = 103;
		public static final int 家族メンバーID = 104;
		public static final int 多胎妊娠区分 = 105;
		public static final int 同一家族の休業有無 = 106;
		public static final int 子の区分 = 107;
		public static final int 縁組成立の年月日 = 108;
		public static final int 配偶者が休業しているか = 109;
		public static final int 同一家族の短時間勤務日数 = 110;
		public static final int 開始年月日 = 200;
		public static final int 終了年月日 = 201;
		public static final int 理由コード = 202;
		public static final int 休職理由 = 203;
	}
	
	@Override
	protected List<IntermediateResult> preCanonicalize(List<IntermediateResult> interm) {
		
		List<IntermediateResult> preCannicalizedData = new ArrayList<>();
		
		interm.forEach(t ->{
			preCannicalizedData.add(create(t));

		});
		return interm;
	}
	
	
	private IntermediateResult create(IntermediateResult t) {
		
		if(t.isImporting(Items.休職理由)) {
			t = t.addCanonicalized(new CanonicalItem(Items.備考, t.getItemByNo(Items.休職理由).get().getString()));
		}
		
		return t.addCanonicalized(new CanonicalItem(Items.開始日, getStartDate(t)))
					 .addCanonicalized(new CanonicalItem(Items.終了日, getEndDate(t)));
//					 .addCanonicalized(new CanonicalItem(Items.休職休業区分, getEndDate(t)));
	}


//	private SmileTempAbsenceDataReasonCode getReasonCode(IntermediateResult t) {
////		new SmileTempAbsenceDataReasonCode(t.getItemByNo(Items.理由コード).get().getString());
//	}

	private GeneralDate getStartDate(IntermediateResult t) { 
		return t.getItemByNo(Items.開始年月日).get().getDate();
	}
	
	private GeneralDate getEndDate(IntermediateResult t) { 
		return new SmileTempAbsenceDataEndDate(t.getItemByNo(Items.終了年月日).get().getString())
						.getGeneralDate();
	}

}
