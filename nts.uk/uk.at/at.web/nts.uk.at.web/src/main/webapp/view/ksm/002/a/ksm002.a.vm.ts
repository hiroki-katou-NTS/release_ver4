module ksm002.a.viewmodel {
    import getShared = nts.uk.ui.windows.getShared;
    import setShared = nts.uk.ui.windows.setShared;
    export class ScreenModel {
        //MODE
        isNew: KnockoutObservable<boolean>;
        //PANE
        boxItemList: KnockoutObservableArray<BoxModel>;
        fullBoxItemList: KnockoutObservableArray<BoxModel>;
        selectedIds: KnockoutObservableArray<number>;
        enable: KnockoutObservable<boolean>;
        //current Date
        currentYear: KnockoutObservable<String>;
        currentMonth: KnockoutObservable<String>;
        endDateOfMonth: KnockoutObservable<number>;
        //Calendar
        calendarData: KnockoutObservable<any>;
        yearMonthPicked: KnockoutObservable<number>;
        cssRangerYM: any;
        optionDates: KnockoutObservableArray<OptionalDate>;
        firstDay: KnockoutObservable<number>;
        yearMonth: KnockoutObservable<number>;
        startDate: number;
        endDate: number;
        workplaceId: KnockoutObservable<string>;
        eventDisplay: KnockoutObservable<boolean>;
        eventUpdatable: KnockoutObservable<boolean>;
        holidayDisplay: KnockoutObservable<boolean>;
        cellButtonDisplay: KnockoutObservable<boolean>;
        workplaceName: KnockoutObservable<string>;

        constructor() {
            var self = this;
            self.isNew = ko.observable(true);
            self.boxItemList = ko.observableArray([]);
            self.fullBoxItemList = ko.observableArray([]);
            self.selectedIds = ko.observableArray([]);
            self.enable = ko.observable(true);
            //Calendar
            self.yearMonthPicked = ko.observable(moment(new Date()).format("YYYYMM"));
            self.cssRangerYM = {};
            self.optionDates = ko.observableArray([]);

            self.firstDay = ko.observable(0);
            self.startDate = 1;
            self.endDate = 31;
            self.workplaceId = ko.observable("0");
            self.workplaceName = ko.observable("");
            self.eventDisplay = ko.observable(true);
            self.eventUpdatable = ko.observable(true);
            self.holidayDisplay = ko.observable(true);
            self.cellButtonDisplay = ko.observable(true);

            $("#calendar").ntsCalendar("init", {
                buttonClick: function(date) {
                    self.openKsm002EDialog(date);
                },
                cellClick: function(date) {
                    let param: IData = { date: date, selectable: _.map(self.boxItemList(), 'id'), selecteds: self.selectedIds() };
                    self.setSpecificItemToSelectedDate(param);
                }
            });
            //Side bar tab change
            $("#sidebar").ntsSideBar("init", {
                active: 0,
                activate: (event, info) => {
                    if (info.newIndex == 1) {
                        nts.uk.ui._viewModel.content.viewModelB.start(true);
                    } else {
                        self.start();
                    }
                }
            });
            //Change Month 
            self.yearMonthPicked.subscribe(function(value) {
                let arrOptionaDates: Array<OptionalDate> = [];
                self.getDataToOneMonth(value).done(function(arrOptionaDates) {
                    self.optionDates(arrOptionaDates);
                    if (arrOptionaDates.length > 0) {
                        self.isNew(false);
                    } else {
                        self.isNew(true);
                    }
                })
            })
        }

        /** 
         *Start page 
         */
        start(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred<any>();
            let isUse: number = 1;
            let arrOptionaDates: Array<OptionalDate> = [];
            service.getSpecificDateByIsUse(isUse).done(function(lstSpecifiDate: any) {
                if (lstSpecifiDate.length > 0) {
                    //getAll SpecDate
                    self.getAllSpecDate();
                    //Set Start Day of Company
                    self.getComStartDay().done(function(startDay: number) {
                        self.firstDay(startDay);
                    });
                    //set parameter to calendar
                    let lstBoxCheck: Array<BoxModel> = [];
                    _.forEach(lstSpecifiDate, function(item) {
                        lstBoxCheck.push(new BoxModel(item.specificDateItemNo, item.specificName));
                    });
                    _.orderBy(lstBoxCheck, ['id'], ['desc']);
                    self.boxItemList(lstBoxCheck);
                    //Set data to calendar
                    self.getDataToOneMonth(self.yearMonthPicked()).done(function(arrOptionaDates) {
                        if (arrOptionaDates.length > 0) {
                            self.optionDates(arrOptionaDates);
                            self.isNew(false);
                        }
                    })
                } else {
                    //In Case no Data, openCDialog                    self.openKsm002CDialog();
                };
                dfd.resolve();
            }).fail(function(res) {
                nts.uk.ui.dialog.alertError(res.message).then(function() { nts.uk.ui.block.clear(); });
                dfd.reject();
            });
            return dfd.promise();
        }

        /**Fill data to each month*/
        getDataToOneMonth(processMonth: string): JQueryPromise<Array<OptionalDate>> {
            var self = this;
            let dfd = $.Deferred<any>();
            let endOfMonth: number = moment(processMonth,"YYYYMM").endOf('month').date();
            let isUse: number = 1;
            let arrOptionaDates: Array<OptionalDate> = [];
            //Array Name to fill on  one Date
            let arrName: Array<string> = [];
            let arrId: Array<string> = [];
            service.getCompanySpecificDateByCompanyDateWithName(processMonth).done(function(lstComSpecDate: any) {
                if (lstComSpecDate.length > 0) {
                    self.isNew(false);
                    for (let j = 1; j < endOfMonth + 1; j++) {
                        let processDay: string = processMonth + _.padStart(j, 2, '0');
                        arrName = [];
                        arrId = [];
                        //Loop in each Day
                        _.forEach(lstComSpecDate, function(comItem) {
                            if (comItem.specificDate == processDay) {
                                arrName.push(comItem.specificDateItemName);
                                arrId.push(comItem.specificDateItemNo);
                            };
                        });
                        arrOptionaDates.push(new OptionalDate(moment(processDay).format('YYYY-MM-DD'), arrName, arrId));
                    };
                }
                //Return Array of Data in Month
                dfd.resolve(arrOptionaDates);
            }).fail(function(res) {
                nts.uk.ui.dialog.alertError(res.message).then(function() { nts.uk.ui.block.clear(); });
                dfd.reject();
            });
            return dfd.promise();
        }
        /** get Start Day of Company*/
        getComStartDay(): JQueryPromise<number> {
            let dfd = $.Deferred<any>();
            //get Company Start Day
            service.getCompanyStartDay().done(function(startDayComapny: number) {
                //self.firstDay();
                dfd.resolve(startDayComapny.startDay);
            }).fail(function(res) {
                nts.uk.ui.dialog.alertError(res.message).then(function() { nts.uk.ui.block.clear(); });
                dfd.reject();
            });
            return dfd.promise();
        }

        /**
         * get full selectable item
         */
        getAllSpecDate() {
            var self = this;
            service.getAllSpecificDate().done(data => {
                data.forEach(item => {
                    self.fullBoxItemList.push(new BoxModel(item.specificDateItemNo, item.specificName));
                });
            }).fail(res => {
                nts.uk.ui.dialog.alertError(res.message).then(function() { nts.uk.ui.block.clear(); });
            });
        }

        /** set Specific Item To Select*/
        setSpecificItemToSelectedDate(param: IData) {
            var self = this;
            //get process date
            let selectedDate: string = moment(param.date).format("YYYY-MM-DD");            
            let selectedOptionalDate: OptionalDate = _.find(self.optionDates(), function(o) { return o.start == selectedDate; });
            
            if (selectedOptionalDate === undefined) { selectedOptionalDate = new OptionalDate(); };
            //get list id selectable
            let arrSelectable: Array<string> = _.map(self.boxItemList(), 'id');
            if (self.optionDates().length > 0) {
                self.optionDates.remove(selectedOptionalDate);
                selectedOptionalDate.start = selectedDate;
                selectedOptionalDate.listId = param.selecteds;
                selectedOptionalDate.listText = _.chain(self.boxItemList()).filter((item) => {
                    return (param.selecteds.indexOf(item.id) > -1);
                }).map('name').value();
                self.optionDates.push(selectedOptionalDate);
            } else {
                let lstText: Array<string> = [];
                lstText = _.chain(self.boxItemList()).filter((item) => {
                    return (param.selecteds.indexOf(item.id) > -1);
                }).map('name').value();
                self.optionDates.push(new OptionalDate(selectedDate, lstText, param.selecteds));
            }
        }
        /**Insert Calendar data*/
        Insert(lstComSpecificDateCommand: Array<ICompanySpecificDateCommand>) {
            var self = this;
            service.insertComSpecificDate(lstComSpecificDateCommand).done(function(res: Array<any>) {
                nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(function() {
                    self.start();
                });
            }).fail(function(res) {
                nts.uk.ui.dialog.alertError(res.message).then(function() { nts.uk.ui.block.clear(); });
            });
        }

        /**Delete Calendar data*/
        DeleteOneMonth() {
            let dfd = $.Deferred<any>();
            var self = this;
            //delete
            service.deleteComSpecificDate({ yearMonth: self.yearMonthPicked().toString() }).done(function(res: any) {
                nts.uk.ui.dialog.info({ messageId: "Msg_16" }).then(function() {
                    //Set dataSource to Null
                    self.optionDates([]);
                    self.isNew(true);
                });
                dfd.resolve();
            }).fail(function(res) {
                nts.uk.ui.dialog.alertError(res.message).then(function() { nts.uk.ui.block.clear(); });
                dfd.reject();
            });
            return dfd.promise();
        }

        /**Register Process*/
        Register() {
            var self = this;
            //Delete before Insert            
            let dfd = $.Deferred<any>();
            if (self.hasItemSpecNotUse()) {
                nts.uk.ui.dialog.alertError({ messageId: "Msg_139" });
            } else {
                if (self.isNew()) {
                    self.Insert(self.convertToCommand())
                } else {
                    if (_.flattenDepth(_.map(self.optionDates(), 'listId')).length > 0) {
                        //Update
                        service.updateComSpecificDate(self.convertToCommand()).done(function(res: Array<any>) {
                            nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(function() {
                                self.start();
                            });
                        }).fail(function(res) {
                            nts.uk.ui.dialog.alertError(res.message).then(function() { nts.uk.ui.block.clear(); });
                        });
                    } else {
                        //Delete all OLD data
                        service.deleteComSpecificDate({ yearMonth: self.yearMonthPicked().toString() }).done(function(res: any) {
                            nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(function() {
                                self.isNew(true);
                            };
                        }).fail(function(res) {
                            nts.uk.ui.dialog.alertError(res.message).then(function() { nts.uk.ui.block.clear(); });
                        });
                    }
                }
                return dfd.promise();
            }
        }

        /**convert data to command (delete, insert)*/
        // where : self.optionDates().length > 0 
        convertToCommand() {
            var self = this;
            let lstComSpecificDateCommand: Array<ICompanySpecificDateCommand> = [];
            _.forEach(self.optionDates(), function(processDay) {
                if (processDay.listId.length > 0) {
                    _.forEach(processDay.listId, function(id) {
                        lstComSpecificDateCommand.push({ specificDate: Number(moment(processDay.start).format('YYYYMMDD')), specificDateNo: Number(id) });
                    });
                };
            });
            return lstComSpecificDateCommand;
        };
        /**
         * check spec item is Use
         */
        hasItemSpecNotUse(): boolean {
            var self = this;
            let allCode = [];
            let isUseCode = _.map(self.boxItemList(), o => o.id);
            _.forEach(self.optionDates(), function(k) {
                allCode = _.concat(allCode, self.getSpecIdfromName(k.listText));
            });
            allCode = _.uniq(allCode);
            let arrDiff: Array<string> = _.difference(allCode, isUseCode);
            if (arrDiff.length > 0)
                return true;
            else
                return false;
        }

        /**get List ID from List NAME*/
        getSpecIdfromName(arrSpecDateName: string[]): number[] {
            var self = this;
            let arrSpecId = [];
            arrSpecDateName.forEach(item => {
                let specDateItem = _.find(self.fullBoxItemList(), o => { return o.name == item });
                arrSpecId.push(specDateItem.id);
            });
            return arrSpecId;
        }

        /** Open Dialog Specific Date Setting*/
        openKsm002CDialog() {
            var self = this;
            nts.uk.ui.windows.sub.modal('/view/ksm/002/c/index.xhtml', { title: '乖離時間の登録＞対象項目', }).onClosed(function(): any {
                self.start();
            })
        }
        /**
         * open dialog D event
         */
        openKsm002DDialog() {
            var self = this;
            nts.uk.ui.windows.setShared('KSM002_D_PARAM',
                {
                    util: 1,
                    startDate: Number(moment(self.yearMonthPicked().toString(), 'YYYYMM').startOf('month').format('YYYYMMDD')),
                    endDate: Number(moment(self.yearMonthPicked().toString(), 'YYYYMM').endOf('month').format('YYYYMMDD'))
                });
            nts.uk.ui.windows.sub.modal("/view/ksm/002/d/index.xhtml", { title: "割増項目の設定", dialogClass: "no-close" }).onClosed(function() {

            });
        }
        /**Process open E Dialog*/
        openKsm002EDialog(selectedDate: string) {
            var self = this;
            //get data in process date
            let selectedOptionalDate: OptionalDate = _.find(self.optionDates(), function(o) { return o.start == selectedDate; });
            //get list id selected OptinonalDate
            let arrSelecteds: Array<string> = [];
            if (selectedOptionalDate !== undefined) {
                arrSelecteds = selectedOptionalDate.listId;
            } else {
                selectedOptionalDate = new OptionalDate();
            }
            //get list id selectable
            let arrSelectable: Array<string> = _.map(self.boxItemList(), 'id');

            setShared('KSM002_E_PARAM', { date: moment(selectedDate).format('YYYY/MM/DD'), selectable: arrSelectable, selecteds: arrSelecteds });
            nts.uk.ui.windows.sub.modal('/view/ksm/002/e/index.xhtml', { title: '乖離時間の登録＞対象項目', }).onClosed(function() {
                let param: IData = getShared('KSM002E_VALUES');
                //console.log(param);
                if (param !== undefined) {
                    self.setSpecificItemToSelectedDate(param);
                }
            });
        }
    }

    interface IData {
        date: any,
        selectable: Array<any>,
        selecteds: Array<any>
    }

    class BoxModel {
        id: number;
        name: string;
        constructor(id: number, name: string) {
            var self = this;
            self.id = id;
            self.name = name;
        }
    }

    class SpecItem {
        specItemNo: number;
        specItemName: string;
        constructor(specItemNo: string, specItemName: string) {
            var self = this;
            self.specItemNo = specItemNo;
            self.specItemName = specItemName;
        }
    }

    class OptionalDate {
        start: string;
        textColor: string;
        backgroundColor: string;
        listText: Array<string>;
        listId: Array<string>;
        constructor(start: string, listText: Array<string>, listId: Array<string>) {
            var self = this;
            self.start = start;
            this.backgroundColor = 'white';
            this.textColor = '#31859C';
            self.listText = listText;
            self.listId = listId;
        }
    }

    export module model {
        export class ItemModel2 {
            code: string;
            name: string;
            constructor(code: string, name: string) {
                this.code = code;
                this.name = name;
            }
        }
    }
    export class ICompanySpecificDateCommand {
        specificDate: number,
        specificDateNo: number,
        isUpdate?: boolean;
        //        constructor(specificDate: number, specificDateNo: number) {
        //            this.specificDate = specificDate;
        //            this.specificDateNo = specificDateNo;
        //        }
    }

}