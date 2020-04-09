module nts.uk.at.view.kdp010.h {
    export module viewmodel {
        export class ScreenModel {

            // H1_2
            optionHighlight: KnockoutObservableArray<any> = ko.observableArray([
                { id: 1, name: nts.uk.resource.getText("KDP010_108") },
                { id: 0, name: nts.uk.resource.getText("KDP010_109") }
            ]);
            selectedHighlight: KnockoutObservable<number> = ko.observable(1);

            // H2_2
            contentsStampType: KnockoutObservableArray<any> = ko.observableArray(__viewContext.enums.ContentsStampType);
            selectedDay: KnockoutObservable<number> = ko.observable(1);

            // H3_2
            optionStamping: KnockoutObservableArray<any> = ko.observableArray(__viewContext.enums.GoingOutReason);
            selectedStamping: KnockoutObservable<number> = ko.observable(0);

            // H4_2
            simpleValue: KnockoutObservable<string> = ko.observable("");

            // H5_2
            letterColors: KnockoutObservable<string> = ko.observable('#0033cc');

            // H6_2
            backgroundColors: KnockoutObservable<string> = ko.observable('#ccccff');

            // H7_2
            optionAudio: KnockoutObservableArray<any> = ko.observableArray(__viewContext.enums.AudioType);
            selectedAudio: KnockoutObservable<number> = ko.observable(0);
            
            defaultData: KnockoutObservable<number> = ko.observable(null);

            buttonPositionNo: KnockoutObservable<number> = ko.observable('');
            dataStampPage: KnockoutObservableArray<StampPageCommentCommand> = ko.observable(new StampPageCommentCommand({}));
            dataShare: KnockoutObservableArray<any> = ko.observableArray([]);

            lstChangeClock: KnockoutObservableArray<any> = ko.observableArray(__viewContext.enums.ChangeClockArt);
            lstChangeCalArt: KnockoutObservableArray<any> = ko.observableArray(__viewContext.enums.ChangeCalArt);
            lstContents: KnockoutObservableArray<any> = ko.observableArray(__viewContext.enums.ContentsStampType);
            lstDataShare: KnockoutObservableArray<any> = ko.observableArray();
            lstData: KnockoutObservableArray<StampTypeCommand> = ko.observable(new StampTypeCommand({}));
            isFocus: KnockoutObservable<boolean> = ko.observable(true);
            constructor() {
                let self = this;
                self.selectedDay.subscribe((newValue) => {
                    self.getDataFromContents(newValue);
                })
                
                self.simpleValue.subscribe(function(codeChanged: string) {
                        self.simpleValue($.trim(self.simpleValue()));
                    });

                self.selectedHighlight.subscribe((newValue) => {
                    if (self.selectedHighlight() == 1)
                        nts.uk.ui.errors.clearAll();
                })
                $('.ntsRadioBox').focus();
            }
            /**
             * start page  
             */
            public startPage(): JQueryPromise<any> {
                let self = this,
                    dfd = $.Deferred();
                self.getDataStamp();
                self.getDataFromContents(1);
                dfd.resolve();
                return dfd.promise();
            }

            public getDataStamp() {

                let self = this;
                self.dataShare = nts.uk.ui.windows.getShared('KDP010_G');
                self.buttonPositionNo(self.dataShare.buttonPositionNo);
                if (self.dataShare.dataShare != undefined) {
                    let data = self.dataShare.dataShare.lstButtonSet ? self.dataShare.dataShare.lstButtonSet.filter(x => x.buttonPositionNo == self.dataShare.buttonPositionNo)[0] : self.dataShare.dataShare;
                    if (data ) {
                            self.letterColors(data.buttonDisSet.buttonNameSet.textColor);
                            self.simpleValue(data.buttonDisSet.buttonNameSet.buttonName);
                            self.backgroundColors(data.buttonDisSet.backGroundColor);
                            self.selectedStamping(data.buttonType.stampType.goOutArt);
                            self.selectedAudio(data.audioType);
                            self.selectedHighlight(data.usrArt); 
                    }
                }
                $(document).ready(function() {
                    $('#highlight-radio').focus();
                });
            }

            /**
             * Pass Data to A
             */
            public passData(): void {
                let self = this;
                $('#correc').trigger("validate");
                if (nts.uk.ui.errors.hasError() && self.selectedHighlight() == 1) {
                    return;
                }
                self.dataStampPage = ({
                    buttonPositionNo: self.buttonPositionNo(),
                    buttonDisSet: ({
                        buttonNameSet: ({
                            textColor: self.letterColors(),
                            buttonName: self.simpleValue()
                        }),
                        backGroundColor: self.backgroundColors()
                    }),
                    buttonType: ({
                        reservationArt: self.lstData.reservationArt,
                        stampType: ({
                            changeHalfDay: self.lstData.changeHalfDay,
                            goOutArt: self.selectedStamping(),
                            setPreClockArt: self.lstData.setPreClockArt,
                            changeClockArt: self.lstData.changeClockArt,
                            changeCalArt: self.lstData.changeCalArt
                        })
                    }),
                    usrArt: self.selectedHighlight(),
                    audioType: self.selectedAudio()

                });

                if (self.dataShare.dataShare == undefined || self.dataShare.dataShare.lstButtonSet == undefined) {
                    self.dataShare = self.dataStampPage;
                }

                if (self.dataShare.dataShare != undefined) {
                    let selectedIndex = _.findIndex(self.dataShare.dataShare.lstButtonSet, (obj) => { return obj.buttonPositionNo == self.buttonPositionNo(); });
                    if (selectedIndex >= 0) {
                        self.dataShare.dataShare.lstButtonSet[selectedIndex] = self.dataStampPage;
                    }
                    else {
                        self.dataShare.dataShare.lstButtonSet.push(self.dataStampPage);
                    }
                }

                let shareG = self.dataShare;
                nts.uk.ui.windows.setShared('KDP010_H', shareG);
                nts.uk.ui.windows.close();
            }

            public getDataFromContents(number: value): void {
                let self = this;
                switch (self.selectedDay()) {
                    case 1:
                        self.lstData = { changeClockArt: self.lstChangeClock()[0].value, changeCalArt: 0, setPreClockArt: 0, changeHalfDay: 0, reservationArt: 0 };
                        break;
                    case 2:
                        self.lstData = { changeClockArt: self.lstChangeClock()[0].value, changeCalArt: 0, setPreClockArt: 1, changeHalfDay: 0, reservationArt: 0 };
                        break;
                    case 3:
                        self.lstData = { changeClockArt: self.lstChangeClock()[0].value, changeCalArt: self.lstChangeCalArt()[1].value, setPreClockArt: 0, changeHalfDay: 0, reservationArt: 0 };
                        break;
                    case 4:
                        self.lstData = { changeClockArt: self.lstChangeClock()[0].value, changeCalArt: self.lstChangeCalArt()[3].value, setPreClockArt: 0, changeHalfDay: 0, reservationArt: 0 };
                        break;
                    case 5:
                        self.lstData = { changeClockArt: self.lstChangeClock()[1].value, changeCalArt: 0, setPreClockArt: 0, changeHalfDay: 0, reservationArt: 0 };
                        break;
                    case 6:
                        self.lstData = { changeClockArt: self.lstChangeClock()[1].value, changeCalArt: 0, setPreClockArt: 2, changeHalfDay: 0, reservationArt: 0 };
                        break;
                    case 7:
                        self.lstData = { changeClockArt: self.lstChangeClock()[1].value, changeCalArt: self.lstChangeCalArt()[2].value, setPreClockArt: 0, changeHalfDay: 0, reservationArt: 0 };
                        break;
                    case 8:
                        self.lstData = { changeClockArt: self.lstChangeClock()[4].value, changeCalArt: 0, setPreClockArt: 0, changeHalfDay: 0, reservationArt: 0 };
                        break;
                    case 9:
                        self.lstData = { changeClockArt: self.lstChangeClock()[5].value, changeCalArt: 0, setPreClockArt: 0, changeHalfDay: 0, reservationArt: 0 };
                        break;
                    case 10:
                        self.lstData = { changeClockArt: self.lstChangeClock()[2].value, changeCalArt: 0, setPreClockArt: 0, changeHalfDay: 0, reservationArt: 0 };
                        break;
                    case 11:
                        self.lstData = { changeClockArt: self.lstChangeClock()[3].value, changeCalArt: 0, setPreClockArt: 0, changeHalfDay: 0, reservationArt: 0 };
                        break;
                    case 12:
                        self.lstData = { changeClockArt: self.lstChangeClock()[7].value, changeCalArt: 0, setPreClockArt: 0, changeHalfDay: 0, reservationArt: 0 };
                        break;
                    case 13:
                        self.lstData = { changeClockArt: self.lstChangeClock()[9].value, changeCalArt: 0, setPreClockArt: 0, changeHalfDay: 0, reservationArt: 0 };
                        break;
                    case 14:
                        self.lstData = { changeClockArt: self.lstChangeClock()[6].value, changeCalArt: 0, setPreClockArt: 0, changeHalfDay: 0, reservationArt: 0 };
                        break;
                    case 15:
                        self.lstData = { changeClockArt: self.lstChangeClock()[8].value, changeCalArt: 0, setPreClockArt: 0, changeHalfDay: 0, reservationArt: 0 };
                        break;
                    case 16:
                        self.lstData = { changeClockArt: self.lstChangeClock()[12].value, changeCalArt: 0, setPreClockArt: 0, changeHalfDay: 0, reservationArt: 0 };
                        break;
                    case 17:
                        self.lstData = { changeClockArt: self.lstChangeClock()[6].value, changeCalArt: self.lstChangeCalArt()[1].value, setPreClockArt: 0, changeHalfDay: 0, reservationArt: 0 };
                        break;
                    case 18:
                        self.lstData = { changeClockArt: self.lstChangeClock()[6].value, changeCalArt: self.lstChangeCalArt()[3].value, setPreClockArt: 0, changeHalfDay: 0, reservationArt: 0 };
                        break;
                    case 19:
                        self.lstData = { changeClockArt: null, changeCalArt: 0, setPreClockArt: 0, changeHalfDay: 0, reservationArt: 1 };
                        break;
                    case 20:
                        self.lstData = { changeClockArt: null, changeCalArt: 0, setPreClockArt: 0, changeHalfDay: 0, reservationArt: 2 };
                        break;
                }
            }

            /**
             * Close dialog
             */
            public closeDialog(): void {
                let self = this;
                nts.uk.ui.windows.close();
            }

        }

    }

    // StampPageCommentCommand
    export class StampPageCommentCommand {

        /** ページNO */
        pageComment: string;
        /** ページ名 */
        commentColor: string;

        constructor(param: IStampPageCommentCommand) {
            this.pageComment = param.pageComment;
            this.commentColor = param.commentColor;
        }
    }

    interface IStampPageCommentCommand {
        pageComment: number;
        commentColor: number;
    }

    // ButtonSettingsCommand
    export class StampPageCommentCommand {
        /** ボタン位置NO */
        buttonPositionNo: number;
        /** ボタンの表示設定 */
        buttonDisSet: ButtonDisSetCommand;
        /** ボタン種類 */
        buttonType: ButtonTypeCommand;
        /** 使用区分 */
        usrArt: number;
        /** 音声使用方法 */
        audioType: number;

        constructor(param: IStampPageCommentCommand) {
            this.buttonPositionNo = param.buttonPositionNo;
            this.buttonDisSet = param.buttonDisSet;
            this.buttonType = param.buttonType;
            this.usrArt = param.usrArt;
            this.audioType = param.audioType;
        }
    }

    interface IStampPageCommentCommand {
        /** ボタン位置NO */
        buttonPositionNo: number;
        /** ボタンの表示設定 */
        buttonDisSet: ButtonDisSetCommand;
        /** ボタン種類 */
        buttonType: ButtonTypeCommand;
        /** 使用区分 */
        usrArt: number;
        /** 音声使用方法 */
        audioType: number;
    }

    // ButtonDisSetCommand
    export class ButtonDisSetCommand {

        /** ボタン名称設定 */
        buttonNameSet: ButtonNameSetCommand;
        /** 背景色 */
        backGroundColor: string;

        constructor(param: IButtonDisSetCommand) {
            this.buttonNameSet = param.buttonNameSet;
            this.backGroundColor = param.backGroundColor;
        }
    }

    interface IButtonDisSetCommand {
        buttonNameSet: ButtonNameSetCommand;
        backGroundColor: string;
    }

    // ButtonNameSetCommand
    export class ButtonNameSetCommand {

        /** ボタン名称設定 */
        textColor: string;
        /** 背景色 */
        buttonName: string;

        constructor(param: IButtonNameSetCommand) {
            this.textColor = param.textColor;
            this.buttonName = param.buttonName;
        }
    }

    interface IButtonNameSetCommand {
        textColor: string;
        buttonName: string;
    }

    // ButtonTypeCommand
    export class ButtonTypeCommand {

        /** 予約区分 */
        reservationArt: number;
        /** 打刻種類 */
        stampType: StampTypeCommand;

        constructor(param: IButtonTypeCommand) {
            this.reservationArt = param.reservationArt;
            this.stampType = param.stampType;
        }
    }

    interface IButtonTypeCommand {
        reservationArt: number;
        stampType: StampTypeCommand;
    }

    // StampTypeCommand
    export class StampTypeCommand {

        /** 勤務種類を半休に変更する */
        changeHalfDay: boolean;
        /** 外出区分 */
        goOutArt: number;
        /** 所定時刻セット区分 */
        setPreClockArt: number;
        /** 時刻変更区分 */
        changeClockArt: number;
        /** 計算区分変更対象 */
        changeCalArt: number;

        constructor(param: IStampTypeCommand) {
            this.changeHalfDay = param.changeHalfDay || null;
            this.goOutArt = param.goOutArt || null;
            this.setPreClockArt = param.setPreClockArt|| null;
            this.changeClockArt = param.changeClockArt|| null;
            this.changeCalArt = param.changeCalArt|| null;
        }
    }

    interface IStampTypeCommand {
        changeHalfDay?: boolean;
        goOutArt?: number ;
        setPreClockArt?: number;
        changeClockArt?: number;
        changeCalArt?: number;
    }
}