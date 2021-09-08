package nts.uk.ctx.at.function.infra.repository.outputitemsofannualworkledger;

import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.JpaRepository;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.function.dom.dailyworkschedule.OutputItemSettingCode;
import nts.uk.ctx.at.function.dom.dailyworkschedule.OutputItemSettingName;
import nts.uk.ctx.at.function.dom.outputitemsofannualworkledger.*;
import nts.uk.ctx.at.function.dom.outputitemsofworkstatustable.FormOutputItemName;
import nts.uk.ctx.at.function.dom.outputitemsofworkstatustable.OutputItem;
import nts.uk.ctx.at.function.dom.outputitemsofworkstatustable.OutputItemDetailAttItem;
import nts.uk.ctx.at.function.dom.outputitemsofworkstatustable.OutputItemWorkLedger;
import nts.uk.ctx.at.function.dom.outputitemsofworkstatustable.enums.*;
import nts.uk.ctx.at.function.infra.entity.outputitemsofannualworkledger.*;

import javax.ejb.Stateless;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Stateless
public class JpaAnnualWorkLedgerOutputSettingRepository extends JpaRepository implements AnnualWorkLedgerOutputSettingRepository {
    private static final String FIND_LIST_OUT_PUT_STATUS;

    private static final String FIND_LIST_FREE_SETTING_ITEM;

    private static final String FIND_WORK_SETTING;

    private static final String FIND_WORK_ITEM;

    private static final String FIND_WORK_CONST;

    private static final String FIND_DELETE_WORK_CONST;

    private static final String FIND_DELETE_WORK_ITEM;

    private static final String FIND_WORK_ITEM_BY_CODE;

    private static final String FIND_WORK_ITEM_BY_CODE_EMPLOYEE;


    static {
        StringBuilder builderString = new StringBuilder();
        builderString.append("SELECT a ");
        builderString.append("FROM KfnmtRptYrRecSetting a ");
        builderString.append("WHERE a.companyId  =:cid ");
        builderString.append(" AND  a.settingType  =:settingType ");
        builderString.append(" ORDER BY  a.displayCode ");
        FIND_LIST_OUT_PUT_STATUS = builderString.toString();

        builderString = new StringBuilder();
        builderString.append("SELECT a ");
        builderString.append("FROM KfnmtRptYrRecSetting a ");
        builderString.append("WHERE a.companyId  =:cid ");
        builderString.append(" AND  a.settingType  =:settingType ");
        builderString.append(" AND  a.employeeId  =:employeeId ");
        builderString.append(" ORDER BY  a.displayCode ");
        FIND_LIST_FREE_SETTING_ITEM = builderString.toString();

        builderString = new StringBuilder();
        builderString.append("SELECT a ");
        builderString.append("FROM KfnmtRptYrRecSetting a ");
        builderString.append("WHERE a.companyId  =:cid ");
        builderString.append(" AND  a.iD  =:settingId ");
        builderString.append(" ORDER BY  a.displayCode ");
        FIND_WORK_SETTING = builderString.toString();

        builderString = new StringBuilder();
        builderString.append("SELECT a ");
        builderString.append("FROM KfnmtRptYrRecItem a ");
        builderString.append("WHERE a.companyId  =:cid ");
        builderString.append(" AND  a.pk.iD  =:settingId ");
        builderString.append(" ORDER BY  a.pk.itemPos ");
        FIND_WORK_ITEM = builderString.toString();

        builderString = new StringBuilder();
        builderString.append("SELECT a ");
        builderString.append("FROM KfnmtRptYrRecDispCont a ");
        builderString.append("WHERE a.companyId  =:cid ");
        builderString.append(" AND  a.pk.iD  =:settingId ");
        builderString.append(" ORDER BY   a.pk.iD, a.pk.itemPos, a.pk.attendanceId ");
        FIND_WORK_CONST = builderString.toString();

        builderString = new StringBuilder();
        builderString.append("SELECT a  ");
        builderString.append("FROM KfnmtRptYrRecDispCont a ");
        builderString.append(" WHERE  a.pk.iD  =:settingId ");
        FIND_DELETE_WORK_CONST = builderString.toString();

        builderString = new StringBuilder();
        builderString.append("SELECT a  ");
        builderString.append("FROM KfnmtRptYrRecItem a ");
        builderString.append(" WHERE  a.pk.iD  =:settingId ");
        FIND_DELETE_WORK_ITEM = builderString.toString();

        builderString = new StringBuilder();
        builderString.append("SELECT a ");
        builderString.append("FROM KfnmtRptYrRecSetting a ");
        builderString.append("WHERE a.companyId  =:cid ");
        builderString.append(" AND  a.displayCode  =:displayCode ");
        builderString.append(" AND  a.settingType  =:settingType ");
        FIND_WORK_ITEM_BY_CODE = builderString.toString();

        builderString = new StringBuilder();
        builderString.append("SELECT a ");
        builderString.append("FROM KfnmtRptYrRecSetting a ");
        builderString.append(" WHERE a.companyId  =:cid ");
        builderString.append(" AND  a.employeeId  =:employeeId ");
        builderString.append(" AND  a.displayCode  =:displayCode ");
        builderString.append(" AND  a.settingType  =:settingType ");
        FIND_WORK_ITEM_BY_CODE_EMPLOYEE = builderString.toString();
    }

    @Override
    public List<AnnualWorkLedgerOutputSetting> getAListOfOutputSettings(String cid, SettingClassificationCommon classificationCommon) {
        return this.queryProxy().query(FIND_LIST_OUT_PUT_STATUS, KfnmtRptYrRecSetting.class)
                .setParameter("cid", cid)
                .setParameter("settingType", classificationCommon.value)
                .getList(JpaAnnualWorkLedgerOutputSettingRepository::toDomain);
    }

    @Override
    public List<AnnualWorkLedgerOutputSetting> getTheFreeSettingOutputItemList(String cid, SettingClassificationCommon classificationCommon, String employeeId) {
        return this.queryProxy().query(FIND_LIST_FREE_SETTING_ITEM, KfnmtRptYrRecSetting.class)
                .setParameter("cid", cid)
                .setParameter("settingType", classificationCommon.value)
                .setParameter("employeeId", employeeId)
                .getList(JpaAnnualWorkLedgerOutputSettingRepository::toDomain);
    }

    @Override
    public Optional<AnnualWorkLedgerOutputSetting> getDetailsOfTheOutputSettings(String cid, String settingId) {
        val itemList = this.queryProxy().query(FIND_WORK_CONST, KfnmtRptYrRecDispCont.class)
                .setParameter("cid", cid)
                .setParameter("settingId", settingId).getList();

        val outputItem = this.queryProxy().query(FIND_WORK_ITEM, KfnmtRptYrRecItem.class)
                .setParameter("cid", cid)
                .setParameter("settingId", settingId).getList(JpaAnnualWorkLedgerOutputSettingRepository::toDomain);

        outputItem.forEach(e -> {
            e.setSelectedAttendanceItemList(itemList.stream().filter(i -> i.pk.itemPos == e.getRank())
                    .map(JpaAnnualWorkLedgerOutputSettingRepository::toDomain).collect(Collectors.toList()));
        });

        return this.queryProxy().query(FIND_WORK_SETTING, KfnmtRptYrRecSetting.class)
                .setParameter("cid", cid)
                .setParameter("settingId", settingId)
                .getSingle(e -> JpaAnnualWorkLedgerOutputSettingRepository.toDomain(e, outputItem));

    }

    @Override
    public void createNew(String cid, AnnualWorkLedgerOutputSetting outputSetting) {
        val entitySetting = KfnmtRptYrRecSetting.fromDomain(cid, outputSetting);
        this.commandProxy().insert(entitySetting);

        val listEntityItems = KfnmtRptYrRecItem.fromDomain(outputSetting);
        if (!listEntityItems.isEmpty()) {
            this.commandProxy().insertAll(listEntityItems);
        }
        val listEntityConst = KfnmtRptYrRecDispCont.fromDomain(outputSetting);
        if (!listEntityConst.isEmpty()) {
            this.commandProxy().insertAll(listEntityConst);
        }
    }

    @Override
    public void update(String cid, String settingId, AnnualWorkLedgerOutputSetting outputSetting) {
        this.commandProxy().update(KfnmtRptYrRecSetting.fromDomain(cid, outputSetting));
        val entityItem = this.queryProxy().query(FIND_DELETE_WORK_ITEM, KfnmtRptYrRecItem.class)
                .setParameter("settingId", settingId).getList();
        if (!CollectionUtil.isEmpty(entityItem)) {
            this.commandProxy().removeAll(entityItem);
            this.getEntityManager().flush();

        }
        this.commandProxy().insertAll(KfnmtRptYrRecItem.fromDomain(outputSetting));
        val entityCont = this.queryProxy().query(FIND_DELETE_WORK_CONST, KfnmtRptYrRecDispCont.class)
                .setParameter("settingId", settingId).getList();
        if (!CollectionUtil.isEmpty(entityCont)) {
            this.commandProxy().removeAll(entityCont);
            this.getEntityManager().flush();
        }
        this.commandProxy().insertAll(KfnmtRptYrRecDispCont.fromDomain(outputSetting));
    }

    @Override
    public void deleteSettingDetail(String settingId) {
        this.commandProxy().remove(KfnmtRptYrRecSetting.class, (settingId));

        val entityCont = this.queryProxy().query(FIND_DELETE_WORK_CONST, KfnmtRptYrRecDispCont.class)
                .setParameter("settingId", settingId).getList();
        if (!entityCont.isEmpty()) {
            this.commandProxy().removeAll(entityCont);
        }

        val entityItem = this.queryProxy().query(FIND_DELETE_WORK_ITEM, KfnmtRptYrRecItem.class)
                .setParameter("settingId", settingId).getList();
        if (!entityItem.isEmpty()) {
            this.commandProxy().removeAll(entityItem);
        }
    }

    @Override
    public void duplicateConfigurationDetail(String cid, String replicationSourceSettingsId,
                                             String destinationSettingId, OutputItemSettingCode outputItemSettingCode,
                                             OutputItemSettingName outputItemSettingName) {
        val optEntitySetting = this.queryProxy().query(FIND_WORK_SETTING, KfnmtRptYrRecSetting.class)
                .setParameter("cid", cid)
                .setParameter("settingId", replicationSourceSettingsId).getSingle();
        if (optEntitySetting.isPresent()) {
            KfnmtRptYrRecSetting entitySetting = optEntitySetting.get();
            val entity = new KfnmtRptYrRecSetting(
                    destinationSettingId,
                    entitySetting.contractCode,
                    entitySetting.companyId,
                    Integer.parseInt(outputItemSettingCode.v()),
                    outputItemSettingName.v(),
                    entitySetting.employeeId,
                    entitySetting.settingType
            );
            this.commandProxy().insert(entity);
        }
        val optEntityItem = this.queryProxy().query(FIND_WORK_ITEM, KfnmtRptYrRecItem.class)
                .setParameter("cid", cid)
                .setParameter("settingId", replicationSourceSettingsId).getList();
        if (!optEntityItem.isEmpty()) {
            val listItem = optEntityItem.stream().map(e -> new KfnmtRptYrRecItem(
                    new KfnmtRptYrRecItemPk(destinationSettingId, e.pk.itemPos),
                    e.contractCode,
                    e.companyId,
                    e.itemName,
                    e.itemIsPrintEd,
                    e.itemCalculatorType,
                    e.itemAttendanceType,
                    e.itemAttribute
            )).collect(Collectors.toList());
            this.commandProxy().insertAll(listItem);
        }
        val optEntityConst = this.queryProxy().query(FIND_WORK_CONST, KfnmtRptYrRecDispCont.class)
                .setParameter("cid", cid)
                .setParameter("settingId", replicationSourceSettingsId).getList();
        if (!optEntityConst.isEmpty()) {
            val listItem = optEntityConst.stream().map(e -> new KfnmtRptYrRecDispCont(
                    new KfnmtRptYrRecDispContPk(destinationSettingId, e.pk.itemPos, e.pk.attendanceId),
                    e.contractCode,
                    e.companyId,
                    e.operator
            )).collect(Collectors.toList());
            this.commandProxy().insertAll(listItem);
        }
    }

    @Override
    public boolean exist(OutputItemSettingCode code, String cid) {
        val displayCode = Integer.parseInt(code.v());
        val settingType = SettingClassificationCommon.STANDARD_SELECTION;
        val rs = this.queryProxy().query(FIND_WORK_ITEM_BY_CODE, KfnmtRptYrRecSetting.class)
                .setParameter("cid", cid)
                .setParameter("displayCode", displayCode)
                .setParameter("settingType", settingType.value)
                .getList(JpaAnnualWorkLedgerOutputSettingRepository::toDomain);
        return rs != null && rs.size() != 0;
    }

    @Override
    public boolean exist(OutputItemSettingCode code, String cid, String employeeId) {
        val displayCode = Integer.parseInt(code.v());
        val settingType = SettingClassificationCommon.FREE_SETTING;
        val rs = this.queryProxy().query(FIND_WORK_ITEM_BY_CODE_EMPLOYEE, KfnmtRptYrRecSetting.class)
                .setParameter("cid", cid)
                .setParameter("employeeId", employeeId)
                .setParameter("displayCode", displayCode)
                .setParameter("settingType", settingType.value)
                .getList(JpaAnnualWorkLedgerOutputSettingRepository::toDomain);
        return rs != null && rs.size() != 0;
    }

    private static AnnualWorkLedgerOutputSetting toDomain(KfnmtRptYrRecSetting entity) {

        return new AnnualWorkLedgerOutputSetting(
                entity.iD,
                new OutputItemSettingCode(Integer.toString(entity.displayCode)),
                new OutputItemSettingName(entity.name),
                EnumAdaptor.valueOf(entity.settingType, SettingClassificationCommon.class),
                null,
                entity.employeeId,
                null
        );
    }

    private static AnnualWorkLedgerOutputSetting toDomain(KfnmtRptYrRecSetting entity, List<OutputItemWorkLedger> itemList) {
        val monthlys = itemList.stream().filter(e -> e.getDailyMonthlyClassification()
                .equals(DailyMonthlyClassification.MONTHLY)).collect(Collectors.toList());
        val dailys = itemList.stream().filter(e -> e.getDailyMonthlyClassification()
                .equals(DailyMonthlyClassification.DAILY)).map(i -> new DailyOutputItemsAnnualWorkLedger(
                i.getRank(),
                new OutputItemNameOfAnnualWorkLedgerDaily(i.getName().v()),
                i.isPrintTargetFlag(),
                i.getIndependentCalcClassic(),
                i.getDailyMonthlyClassification(),
                i.getItemDetailAttributes(),
                i.getSelectedAttendanceItemList()
        ))
                .collect(Collectors.toList());
        return new AnnualWorkLedgerOutputSetting(
                entity.iD,
                new OutputItemSettingCode(Integer.toString(entity.displayCode)),
                new OutputItemSettingName(entity.name),
                EnumAdaptor.valueOf(entity.settingType, SettingClassificationCommon.class),
                dailys,
                entity.employeeId,
                monthlys
        );
    }

    private static OutputItemWorkLedger toDomain(KfnmtRptYrRecItem entity) {
        return new OutputItemWorkLedger(
                entity.pk.itemPos,
                new OutputItemNameOfAnnualWorkLedger(entity.itemName),
                entity.itemIsPrintEd,
                EnumAdaptor.valueOf(entity.itemCalculatorType, IndependentCalcClassic.class),
                EnumAdaptor.valueOf(entity.itemAttendanceType, DailyMonthlyClassification.class),
                EnumAdaptor.valueOf(entity.itemAttribute, CommonAttributesOfForms.class),
                null
        );
    }

    private static OutputItemDetailAttItem toDomain(KfnmtRptYrRecDispCont entity) {
        return new OutputItemDetailAttItem(
                EnumAdaptor.valueOf(entity.operator, OperatorsCommonToForms.class),
                entity.pk.attendanceId
        );

    }
}
