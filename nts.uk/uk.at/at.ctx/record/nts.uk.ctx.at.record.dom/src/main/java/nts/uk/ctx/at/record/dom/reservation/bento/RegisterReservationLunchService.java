package nts.uk.ctx.at.record.dom.reservation.bento;

import nts.arc.task.tran.AtomTask;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.reservation.bentomenu.BentoMenu;
import nts.uk.ctx.at.record.dom.reservation.bentomenu.closingtime.BentoReservationClosingTime;
import nts.uk.ctx.at.record.dom.reservation.reservationsetting.Achievements;
import nts.uk.ctx.at.record.dom.reservation.reservationsetting.BentoReservationSetting;
import nts.uk.ctx.at.record.dom.reservation.reservationsetting.CorrectionContent;
import nts.uk.ctx.at.record.dom.reservation.reservationsetting.OperationDistinction;
import nts.uk.shr.com.context.AppContexts;

/**
 * 弁当予約設定を登録する
 * @author Nguyen Huy Quang
 */
public class RegisterReservationLunchService {

    /**
     * 登録する
     */
    public static AtomTask register(
            Require require, OperationDistinction operationDistinction,Achievements arAchievements,
            CorrectionContent correctionContent, BentoReservationClosingTime bentoReservationClosingTime) {

        String companyId = AppContexts.user().companyId();
        GeneralDate date = GeneralDate.max();

        // 1: get(会社ID)
        BentoReservationSetting bentoReservationSetting = require.getReservationSettings(companyId);

        // 3: get(会社ID,’9999/12/31’)
        BentoMenu bentoMenu = require.getBentoMenu(companyId, date);
        String historyID = null;
        if (bentoMenu != null){
            historyID = bentoMenu.getHistoryID();
        }
        String finalHistoryID = historyID;

        return AtomTask.of(() -> {
            require.registerBentoMenu(finalHistoryID,bentoReservationClosingTime);

            if (bentoReservationSetting == null){
                BentoReservationSetting newSetting = new BentoReservationSetting(companyId,operationDistinction,correctionContent,arAchievements);
                require.inSert(newSetting);
            }else {
                require.update(bentoReservationSetting);
            }
        });
    }

    public static interface Require {

        /**
         * 弁当予約設定を取得する
         */
        BentoReservationSetting getReservationSettings(String cid);

        /**
         * 弁当メニュを取得する
         */
        BentoMenu getBentoMenu(String cid,GeneralDate date);

        /**
         * 弁当メニューを登録する
         */
        void registerBentoMenu(String historyID,BentoReservationClosingTime bentoReservationClosingTime);

        /**
         * Insert（弁当予約設定）
         */
        void inSert(BentoReservationSetting bentoReservationSetting);

        /**
         * Update（弁当予約設定）
         */
        void update(BentoReservationSetting bentoReservationSetting);

    }

}
