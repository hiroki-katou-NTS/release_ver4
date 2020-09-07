package nts.uk.screen.at.app.reservation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BentoMenuJoinBentoSettingDto {

    //BentoSetting
    //予約の運用区別
    public int operationDistinction;

    //基準時間
    public int referenceTime;

    //予約済みの内容変更期限内容
    public int contentChangeDeadline;

    //予約済みの内容変更期限日数
    public int contentChangeDeadlineDay;

    //注文済み期限方法
    public int orderDeadline;

    //月別実績の集計
    public int monthlyResults;

    //日別実績の集計
    public int dailyResults;

    //注文済みデータ
    private int orderedData;

    //BentoMenu

    //名前
    public String reservationFrameName1;

    //開始
    public Integer reservationStartTime1;

    //終了
    public int reservationEndTime1;

    //名前
    public String reservationFrameName2;

    //開始
    public Integer reservationStartTime2;

    //終了
    public Integer reservationEndTime2;


    public static BentoMenuJoinBentoSettingDto setData(BentoMenuDto bentoMenuDto, BentoReservationSettingDto bentoReservationSettingDto){

        if (bentoMenuDto == null){
            return null;
        }
        return new BentoMenuJoinBentoSettingDto(
                bentoReservationSettingDto.operationDistinction,
                bentoReservationSettingDto.referenceTime,
                bentoReservationSettingDto.contentChangeDeadline,
                bentoReservationSettingDto.contentChangeDeadlineDay,
                bentoReservationSettingDto.orderDeadline,
                bentoReservationSettingDto.monthlyResults,
                bentoReservationSettingDto.dailyResults,
                bentoReservationSettingDto.orderedData,
                bentoMenuDto.reservationFrameName1,
                bentoMenuDto.reservationStartTime1,
                bentoMenuDto.reservationEndTime1,
                bentoMenuDto.reservationFrameName2,
                bentoMenuDto.reservationStartTime2,
                bentoMenuDto.reservationEndTime2
        );
    }
}
