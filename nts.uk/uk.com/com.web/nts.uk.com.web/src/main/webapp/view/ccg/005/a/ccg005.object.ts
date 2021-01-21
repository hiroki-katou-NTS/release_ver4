module nts.uk.at.view.ccg005.a.object {
  export interface DisplayAttendanceDataDto {
    favoriteSpecifyDto: FavoriteSpecifyDto[];               //お気に入りの指定
    attendanceInformationDtos: AttendanceInformationDto[];  //在席情報DTO
    emojiUsage: number;                                     //感情状態を利用する
    isInCharge: boolean;                                    //担当者か
    applicationNameDtos: ApplicationNameDto[];              //申請名
    bussinessName: string;                                  //自分のビジネスネーム
  }

  export interface FavoriteSpecifyDto {
    favoriteName: string;                                   // お気に入り名
    creatorId: string;                                      // 作��D
    inputDate: any;                                         // 入力日;
    targetSelection: number;                                // 対象選�
    workplaceId: string[];                                  // 職場ID
    order: number;                                          // 頺
  }

  export interface AttendanceInformationDto {
    applicationDtos: any[];                                 //申請
    sid: string;                                            //社員ID
    attendanceDetailDto: any;                               //詳細出退勤
    avatarDto: UserAvatarDto;                               //個人の顔写真
    activityStatusDto: ActivityStatusDto;                   //在席のステータス
    commentDto: any;                                        //社員のコメント情報
    goOutDto: GoOutEmployeeInformationDto;                  //社員の外出情報
    emojiDto: EmployeeEmojiStateDto;                        //社員の感情状態
  }

  export interface ApplicationNameDto {
    appName: string;                                        //申請名
    appType: number;                                        //申請種類
  }

  export interface UserAvatarDto {
    personalId: string;                                     //個人ID
    fileId: string;                                         //顔写真ファイルID
  }

  export interface ActivityStatusDto {
    activity: number;                                       //ステータス分類
    date: any;                                              //年月日
    sid: string;                                            //社員ID
  }

  export interface GoOutEmployeeInformationDto {
    goOutTime: number;                                      //外出時刻
    goOutReason: string;                                    //外出理由
    goOutDate: any;                                         //年月日
    comebackTime: number;                                   //戻り時刻
    sid: string;                                            //社員ID
  }

  export interface EmployeeEmojiStateDto {
    date: any;                                              //年月日
    emojiType: number;                                      //感情種類
    sid: string;                                            //社員ID
  }
}