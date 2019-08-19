package nts.uk.ctx.pr.report.dom.printdata.socinsurnoticreset;

import lombok.Getter;
import lombok.Setter;
import nts.arc.enums.EnumAdaptor;

import java.util.Optional;

/**
 * 社会保険届作成設定
 */
@Getter
@Setter
public class SocialInsurNotiCreateSet {
    /**
     * 会社ID
     */
    private String cid;
    /**
     * ユーザID
     */
    private String userId;

    /**
     * 事業所情報
     */
    private BusinessDivision officeInformation;

    /**
     * 事業所整理記号
     */
    private BussEsimateClass businessArrSymbol;

    /**
     * 出力順
     */
    private SocialInsurOutOrder outputOrder;
    /**
     * 印刷個人番号
     */
    private PersonalNumClass printPersonNumber;
    /**
     * 提出氏名区分
     */
    private SubNameClass submittedName;

    /**
     * 被保険者整理番号
     */
    private InsurPersonNumDivision insuredNumber;

    /**
     * FD番号
     */
    private Optional<FdNumber> fdNumber;

    /**
     * テキスト個人番号
     */
    private Optional<TextPerNumberClass> textPersonNumber;

    /**
     * 出力形式
     */
    private Optional<OutputFormatClass> outputFormat;

    /**
     * 改行コード
     */
    private Optional<LineFeedCode> lineFeedCode;

    public SocialInsurNotiCreateSet(String userId, String cid, int officeInformation, int businessArrSymbol, int outputOrder, int printPersonNumber, int submittedName, int insuredNumber, String fdNumber, Integer textPersonNumber, Integer outputFormat, Integer lineFeedCode) {
        this.cid = cid;
        this.userId = userId;
        this.officeInformation = EnumAdaptor.valueOf(officeInformation, BusinessDivision.class);
        this.businessArrSymbol = EnumAdaptor.valueOf(businessArrSymbol, BussEsimateClass.class);
        this.outputOrder = EnumAdaptor.valueOf(outputOrder, SocialInsurOutOrder.class);
        this.printPersonNumber = EnumAdaptor.valueOf(printPersonNumber, PersonalNumClass.class);
        this.submittedName = EnumAdaptor.valueOf(submittedName, SubNameClass.class);
        this.insuredNumber = EnumAdaptor.valueOf(insuredNumber, InsurPersonNumDivision.class);
        this.fdNumber = fdNumber == null ? Optional.empty() : Optional.of(new FdNumber(fdNumber));
        this.textPersonNumber = textPersonNumber == null ? Optional.empty() : Optional.of(EnumAdaptor.valueOf(textPersonNumber, TextPerNumberClass.class));
        this.outputFormat = outputFormat == null ? Optional.empty() : Optional.of(EnumAdaptor.valueOf(outputFormat, OutputFormatClass.class));
        this.lineFeedCode = lineFeedCode == null ? Optional.empty() : Optional.of(EnumAdaptor.valueOf(lineFeedCode, LineFeedCode.class));
    }
}
