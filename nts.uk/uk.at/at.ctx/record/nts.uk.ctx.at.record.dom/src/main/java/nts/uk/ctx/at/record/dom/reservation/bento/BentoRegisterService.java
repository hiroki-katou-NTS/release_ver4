package nts.uk.ctx.at.record.dom.reservation.bento;

import lombok.val;
import nts.arc.task.tran.AtomTask;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.reservation.bentomenu.Bento;
import nts.uk.ctx.at.record.dom.reservation.bentomenu.BentoMenu;
import nts.uk.shr.com.context.AppContexts;

import javax.ejb.Stateless;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 弁当を登録する
 */
@Stateless
public class BentoRegisterService {

    /**
     * 登録する
     */
    public static AtomTask register(Require require, Bento bento) {
        String cid = AppContexts.user().companyId();
        GeneralDate date = GeneralDate.max();

        BentoMenu bentoMenu = require.getBentoMenu(cid, date);
        bentoMenu.getMenu().add(bento);

        return AtomTask.of(() -> {
            require.register(bentoMenu);
        });
    }

    public static interface Require {
        // 弁当メニューを取得する
        BentoMenu getBentoMenu(String cid, GeneralDate date);

        // 弁当を追加する
        void register(BentoMenu bentoMenu);
    }

}
