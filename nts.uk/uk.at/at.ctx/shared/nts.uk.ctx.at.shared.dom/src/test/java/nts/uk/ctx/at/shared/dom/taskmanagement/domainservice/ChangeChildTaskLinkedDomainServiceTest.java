package nts.uk.ctx.at.shared.dom.taskmanagement.domainservice;


import lombok.val;
import mockit.Expectations;
import mockit.Injectable;
import mockit.integration.junit4.JMockit;
import nts.arc.task.tran.AtomTask;
import nts.arc.testing.assertion.NtsAssert;
import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.shared.dom.personallaborcondition.UseAtr;
import nts.uk.ctx.at.shared.dom.scherec.taskmanagement.taskframe.TaskFrameNo;
import nts.uk.ctx.at.shared.dom.scherec.taskmanagement.taskmaster.*;
import nts.uk.ctx.at.shared.dom.taskmanagement.aggregateroot.taskframe.TaskFrameName;
import nts.uk.ctx.at.shared.dom.taskmanagement.aggregateroot.taskframe.TaskFrameSetting;
import nts.uk.ctx.at.shared.dom.taskmanagement.aggregateroot.taskframe.TaskFrameUsageSetting;
import nts.uk.ctx.at.shared.dom.taskmanagement.aggregateroot.taskmaster.Tasks;
import nts.uk.shr.com.color.ColorCode;
import nts.uk.shr.com.context.AppContexts;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(JMockit.class)
public class ChangeChildTaskLinkedDomainServiceTest {
    @Injectable
    ChangeChildTaskLinkedDomainService.Require require;

    /**
     * case: 作業枠利用設定が設定されてない: Msg_1959
     */
    @Test
    public void testCaseWorkFrameUsageSettingsAreNotSet() {

        new Expectations(AppContexts.class) {
            {
                AppContexts.user().companyId();
                result = "cid";

                require.getWorkFrameUsageSetting("cid");
                result = null;

            }
        };
        NtsAssert.businessException("Msg_1959",
                () -> ChangeChildTaskLinkedDomainService.change(require, Helper.taskFrameNo
                        , Helper.code, Helper.childWorkList));

    }

    /**
     * Case : 子作業を紐付けることができません: Msg_2066
     */
    @Test
    public void testUnableToAssociateChildWork() {

        new Expectations(AppContexts.class) {
            {
                AppContexts.user().companyId();
                result = "cid";

                require.getWorkFrameUsageSetting("cid");
                result = new TaskFrameUsageSetting(Helper.frameSettingList());

            }
        };
        NtsAssert.businessException("Msg_2066",
                () -> ChangeChildTaskLinkedDomainService.change(require, new TaskFrameNo(5)
                        , Helper.code, Helper.childWorkList));
    }

    /**
     * Case: 下位枠が利用しないになっている: Msg_1957
     */
    @Test
    public void testTheLowerFrameIsNotUsed() {

        new Expectations(AppContexts.class) {
            {
                AppContexts.user().companyId();
                result = "cid";

                require.getWorkFrameUsageSetting("cid");
                result = new TaskFrameUsageSetting(Helper.frameSettingList());
            }
        };
        NtsAssert.businessException("Msg_1957",
                () -> ChangeChildTaskLinkedDomainService.change(require, new TaskFrameNo(4)
                        , Helper.code, Helper.childWorkList));

    }

    /**
     * Case: 作業マスタがない: Msg_2065
     */
    @Test
    public void testThereIsNoWorkMaster() {

        new Expectations(AppContexts.class) {
            {
                AppContexts.user().companyId();
                result = "cid";

                require.getWorkFrameUsageSetting("cid");
                result = new TaskFrameUsageSetting(Helper.frameSettingList02());

                require.getOptionalWork("cid", Helper.taskFrameNo, Helper.code);
                result = Optional.empty();

            }
        };
        NtsAssert.businessException("Msg_2065",
                () -> ChangeChildTaskLinkedDomainService.change(require, Helper.taskFrameNo
                        , Helper.code, Helper.childWorkList));

    }

    /**
     * Case: require.作業を取得する(親作業枠NO,親作業コード):  作業枠NO == 5
     * Throw BusinessException :  Msg_2065;
     */
    @Test
    public void testThrowBusinessExceptionWhenChangeWork_TaskFrameNo_is05() {
        val workOptional = Helper.createDomain();
        new Expectations(AppContexts.class) {
            {
                AppContexts.user().companyId();
                result = "cid";

                require.getWorkFrameUsageSetting("cid");
                result = new TaskFrameUsageSetting(Helper.frameSettingList02());

                require.getOptionalWork("cid", Helper.taskFrameNo, Helper.code);
                result = Optional.of(workOptional);

            }
        };
        NtsAssert.businessException("Msg_2066",
                () -> ChangeChildTaskLinkedDomainService.change(require, Helper.taskFrameNo
                        , Helper.code, Helper.childWorkList));

    }

    /**
     * Case: Success
     */
    @Test
    public void testNoThrowBusinessException() {
        val workOptional = Helper.createDomain();
        new Expectations(AppContexts.class, Tasks.class) {
            {
                AppContexts.user().companyId();
                result = "cid";

                require.getWorkFrameUsageSetting("cid");
                result = new TaskFrameUsageSetting(Helper.frameSettingList02());

                require.getOptionalWork("cid", Helper.taskFrameNo, Helper.code);
                result = Optional.of(workOptional);

                workOptional.changeChildTaskList(require, (List<TaskCode>) any);

            }
        };
        AtomTask atomTask = ChangeChildTaskLinkedDomainService.change(require, Helper.taskFrameNo
                , Helper.code, Helper.childWorkList);
        NtsAssert.atomTask(
                () -> atomTask,
                anySupplier -> require.update(anySupplier.get()));
    }

    public static class Helper

    {
        private static final String DATE_FORMAT = "yyyy/MM/dd";
        private static final TaskCode code = new TaskCode("CODE");
        private static final TaskFrameNo taskFrameNo = new TaskFrameNo(3);
        private static final ExternalCooperationInfo cooperationInfo = new ExternalCooperationInfo(
                Optional.of(new TaskExternalCode("externalCode1")),
                Optional.of(new TaskExternalCode("externalCode2")),
                Optional.of(new TaskExternalCode("externalCode3")),
                Optional.of(new TaskExternalCode("externalCode4")),
                Optional.of(new TaskExternalCode("externalCode5"))
        );

        private static final List<TaskCode> childWorkList = Arrays.asList(
                new TaskCode("CODE01"),
                new TaskCode("CODE02"),
                new TaskCode("CODE03"),
                new TaskCode("CODE04"),
                new TaskCode("CODE05")
        );
        private static final GeneralDate startDate = GeneralDate.fromString("2021/01/01", DATE_FORMAT);
        private static final GeneralDate endDate = GeneralDate.fromString("2021/05/15", DATE_FORMAT);
        private static final DatePeriod expirationDate = new DatePeriod(startDate, endDate);
        private static final TaskDisplayInfo displayInfo = new TaskDisplayInfo(
                new TaskName("TaskName"),
                new TaskAbName("TaskAbName"),
                Optional.of(new ColorCode("ColorCode")),
                Optional.of(new TaskNote("TaskNote"))

        );

        static List<TaskFrameSetting> frameSettingList() {
            return Arrays.asList(
                    new TaskFrameSetting(
                            new TaskFrameNo(1),
                            new TaskFrameName("Name01"),
                            UseAtr.USE
                    ),
                    new TaskFrameSetting(
                            new TaskFrameNo(5),
                            new TaskFrameName("Name05"),
                            UseAtr.NOTUSE

                    ), new TaskFrameSetting(
                            new TaskFrameNo(3),
                            new TaskFrameName("Name03"),
                            UseAtr.USE

                    ), new TaskFrameSetting(
                            new TaskFrameNo(4),
                            new TaskFrameName("Name04"),
                            UseAtr.USE

                    ), new TaskFrameSetting(
                            new TaskFrameNo(2),
                            new TaskFrameName("Name02"),
                            UseAtr.USE

                    )
            );
        }

        static List<TaskFrameSetting> frameSettingList02() {
            return Arrays.asList(
                    new TaskFrameSetting(
                            new TaskFrameNo(1),
                            new TaskFrameName("Name01"),
                            UseAtr.USE
                    ),
                    new TaskFrameSetting(
                            new TaskFrameNo(5),
                            new TaskFrameName("Name05"),
                            UseAtr.USE

                    ), new TaskFrameSetting(
                            new TaskFrameNo(3),
                            new TaskFrameName("Name03"),
                            UseAtr.USE

                    ), new TaskFrameSetting(
                            new TaskFrameNo(4),
                            new TaskFrameName("Name04"),
                            UseAtr.USE

                    ), new TaskFrameSetting(
                            new TaskFrameNo(2),
                            new TaskFrameName("Name02"),
                            UseAtr.USE

                    )
            );
        }

        static Tasks createDomain() {

            return new Tasks(code, new TaskFrameNo(5), cooperationInfo, childWorkList, expirationDate, displayInfo);
        }
    }
}
