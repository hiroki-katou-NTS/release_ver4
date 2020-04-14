module nts.uk.at.view.kdp002.a {

    export module viewmodel {

        export class ScreenModel {
            stampSetting: KnockoutObservable<StampSetting> = ko.observable({});
            stampClock: StampClock = new StampClock();
            stampTab: KnockoutObservable<StampTab> = ko.observable(new StampTab());
            stampGrid: KnockoutObservable<EmbossGridInfo> = ko.observable({});
            stampToSuppress: KnockoutObservable<StampToSuppress> = ko.observable({});
            stampResultDisplay: KnockoutObservable<IStampResultDisplay> = ko.observable({});
           
            constructor() {
                let self = this;
            }

            public startPage(): JQueryPromise<void> {
                let self = this;
                let dfd = $.Deferred<void>();
                service.startPage()
                    .done((res: IStartPage) => {
                        self.stampSetting(res.stampSetting);
                        self.stampTab().bindData(res.stampSetting.pageLayouts);
                        self.stampGrid(new EmbossGridInfo(res));
                        self.stampGrid().yearMonth.subscribe((val) => {
                           self.getTimeCardData();
                        });
                        let stampToSuppress = res.stampToSuppress;
                        stampToSuppress.isUse = res.stampSetting.buttonEmphasisArt;
                        self.stampToSuppress(stampToSuppress);
                        self.stampResultDisplay(res.stampResultDisplay);
                        dfd.resolve();
                    }).fail((res) => {
                        nts.uk.ui.dialog.alertError({ messageId: res.messageId }).then(() => {
                            nts.uk.request.jump("com", "/view/ccg/008/a/index.xhtml");
                        });
                    });

                return dfd.promise();
            }

            public getTimeCardData() {
                nts.uk.ui.errors.clearAll();
                $(".nts-input").trigger("validate");
                if (nts.uk.ui.errors.hasError()) {
                    return;
                }

                let self = this;
                nts.uk.ui.block.grayout();
                let data = {
                    date: self.stampGrid().yearMonth() + '/15'
                };
                service.getTimeCardData(data).done((timeCard) => {
                    self.stampGrid().bindItemData(timeCard.listAttendances);
                }).fail((res) => {
                    nts.uk.ui.dialog.alertError({ messageId: res.messageId });
                }).always(() => {
                    nts.uk.ui.block.clear();
                });
            }

            public getStampData() {
                nts.uk.ui.errors.clearAll();
                $(".nts-input").trigger("validate");
                if (nts.uk.ui.errors.hasError()) {
                    return;
                }

                let self = this;
                nts.uk.ui.block.grayout();
                service.getStampData(self.stampGrid().dateValue()).done((stampDatas) => {
                    self.stampGrid().bindItemData(stampDatas);
                }).fail((res) => {
                    nts.uk.ui.dialog.alertError({ messageId: res.messageId });
                }).always(() => {
                    nts.uk.ui.block.clear();
                });
            }

            public getPageLayout(pageNo: number) {
                let self = this;
                let layout = _.find(self.stampTab().layouts(), (ly) => { return ly.pageNo === pageNo }); 
        
                if(layout) {
                    let btnSettings = layout.buttonSettings;
                    btnSettings.forEach(btn => {
                        btn.onClick = self.clickBtn1;
                    });
                    layout.buttonSettings = btnSettings;
                }
        
                return layout;
            }
        
            public clickBtn1(vm, layout) {
                let button = this;
                let data = {
                    datetime: moment().format('YYYY/MM/DD HH:mm:ss'),
                    authcMethod:0,
                    stampMeans:3,
                    reservationArt: button.btnReservationArt,
                    changeHalfDay: button.changeHalfDay,
                    goOutArt: button.goOutArt,
                    setPreClockArt: button.setPreClockArt,
                    changeClockArt: button.changeClockArt,
                    changeCalArt: button.changeCalArt
                };
                service.stampInput(data).done((res) => {
                    if(vm.stampResultDisplay().notUseAttr == 1 && button.changeClockArt == 1) {
                        vm.openScreenC(button, layout);
                    } else {
                        vm.openScreenB(button, layout);
                    }
                }).fail((res) => {
                    nts.uk.ui.dialog.alertError({ messageId: res.messageId });
                });
            }

            public openScreenB(button, layout) {
                let self = this;
                nts.uk.ui.windows.setShared("resultDisplayTime",  self.stampSetting().resultDisplayTime);
                nts.uk.ui.windows.sub.modal('/view/kdp/002/b/index.xhtml').onClosed(() => {
                    self.getStampData();
                    self.openKDP002T(button, layout);
                }); 
            }

            public openScreenC(button, layout) {
                let self = this;
                nts.uk.ui.windows.setShared('KDP010_2C', self.stampResultDisplay().displayItemId, true);
                nts.uk.ui.windows.sub.modal('/view/kdp/002/c/index.xhtml').onClosed(function (): any {
                    self.getStampData();
                    self.openKDP002T(button, layout);
                });
            }

            public openKDP002T(button: ButtonSetting, layout) {
                let data = {
                    pageNo: layout.pageNo,
                    buttonDisNo: button.btnPositionNo
                }
                service.getError(data).done((res) => {
                    if(res && res.dailyAttdErrorInfos && res.dailyAttdErrorInfos.length > 0) {
                        nts.uk.ui.windows.setShared('KDP010_2T', res, true);
                        nts.uk.ui.windows.sub.modal('/view/kdp/002/t/index.xhtml').onClosed(function (): any {
                            
                        });
                    }
                });
            }
        
        }

    }
}