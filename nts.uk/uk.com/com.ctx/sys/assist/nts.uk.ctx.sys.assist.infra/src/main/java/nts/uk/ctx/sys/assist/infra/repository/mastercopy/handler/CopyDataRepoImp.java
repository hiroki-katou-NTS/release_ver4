package nts.uk.ctx.sys.assist.infra.repository.mastercopy.handler;

import nts.uk.ctx.sys.assist.dom.mastercopy.*;
import nts.uk.ctx.sys.assist.dom.mastercopy.handler.CopyDataRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

/**
 * @author locph
 */
@Stateless
@Transactional(value = Transactional.TxType.REQUIRES_NEW)
public class CopyDataRepoImp implements CopyDataRepository {

    @Inject
    MasterCopyDataRepository repository;

    @Inject
    PersonalInfoDataCopyAdapter personalInfoDataCopyAdapter;

    @Inject
    ErAlWorkRecordCopyAdapter erAlWorkRecordCopyAdapter;

    @Override
    public void copy(String companyId, TargetTableInfo targetTableInfo, Integer copyMethod) {
        String tableName = targetTableInfo.getTableName().v().trim();
        switch (targetTableInfo.getCopyAttribute()) {
            case COPY_WITH_COMPANY_ID:
            case COPY_MORE_COMPANY_ID:
                repository.doCopy(tableName, CopyMethod.valueOf(copyMethod), companyId);
                break;
            case COPY_OTHER:
                //using adapter
                switch (tableName) {
                    case "KRCMT_ERAL_SET":
                        erAlWorkRecordCopyAdapter.copy(companyId, copyMethod);
                        break;
                    case "PPEMT_PER_INFO_CTG":
                    case "PPEMT_PER_INFO_CTG_ORDER":
                    case "PPEMT_PER_INFO_ITEM_CM":
                    case "PPEMT_PER_INFO_ITEM":
                    case "PPEMT_DATE_RANGE_ITEM":
                        break;
                    case "PPEMT_PER_INFO_ITEM_ORDER":
                        personalInfoDataCopyAdapter.personalInfoDefEvent(companyId, copyMethod);
                        break;
                    case "PPEMT_HISTORY_SELECTION":
                    case "PPEMT_SELECTION":
                        break;
                    case "PPEMT_SEL_ITEM_ORDER":
                        //do event Event：個人情報選択項目の初期値コピー
                        personalInfoDataCopyAdapter.personalInfoSelectItemEvent(companyId, copyMethod);
                        break;
                    case "PPEMT_NEW_LAYOUT":
                    case "PPEMT_LAYOUT_ITEM_CLS_DF":
                        break;
                    case "PPEMT_LAYOUT_ITEM_CLS":
                        //Event：新規レイアウトの初期値コピー
                        personalInfoDataCopyAdapter.newLayoutEvent(companyId, copyMethod);
                        break;
                    case "PPEMT_PINFO_ITEM_GROUP":
                        break;
                    case "PPEMT_PINFO_ITEM_DF_GROUP":
                        //Event：個人情報項目グループの初期値コピー
                        personalInfoDataCopyAdapter.personalInfoItemGroupEvent(companyId, copyMethod);
                        break;
                }
        }
    }
}
