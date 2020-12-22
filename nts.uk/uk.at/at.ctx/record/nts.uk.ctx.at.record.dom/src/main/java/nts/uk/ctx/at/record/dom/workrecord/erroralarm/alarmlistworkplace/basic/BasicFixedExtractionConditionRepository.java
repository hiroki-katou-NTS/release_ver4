package nts.uk.ctx.at.record.dom.workrecord.erroralarm.alarmlistworkplace.basic;

import java.util.List;

public interface BasicFixedExtractionConditionRepository {

    List<BasicFixedExtractionCondition> getByID(String id);

    List<BasicFixedExtractionCondition> getByIDs(List<String> ids);

    List<BasicFixedExtractionCondition> getBy(List<String> ids, boolean useAtr);

    void register(BasicFixedExtractionCondition domain);

    void registerAll(List<BasicFixedExtractionCondition> domain);

    void update(BasicFixedExtractionCondition domain);

    void delete(String id);

}
