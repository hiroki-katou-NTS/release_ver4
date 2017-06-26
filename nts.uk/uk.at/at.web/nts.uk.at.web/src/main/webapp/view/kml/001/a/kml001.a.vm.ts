module kml001.a.viewmodel {
    import servicebase = kml001.shr.servicebase;
    import vmbase = kml001.shr.vmbase;
    export class ScreenModel {
        gridPersonCostList: KnockoutObservableArray<vmbase.GridPersonCostCalculation>;
        currentGridPersonCost: KnockoutObservable<string>;
        personCostList: KnockoutObservableArray<vmbase.PersonCostCalculation>;
        currentPersonCost: KnockoutObservable<vmbase.PersonCostCalculation>;
        premiumItems: KnockoutObservableArray<vmbase.PremiumItem>;
        lastStartDate: string;
        isInsert: KnockoutObservable<Boolean>;
        newStartDate: KnockoutObservable<string>;
        constructor() {
            $('#formula-child-1').html(nts.uk.resource.getText('KML001_7'));
            var self = this;
            self.personCostList = ko.observableArray([]);
            self.currentPersonCost = ko.observable(new vmbase.PersonCostCalculation('', '', "", "9999/12/31", 0, '', []));
            self.newStartDate = ko.observable(null);
            self.gridPersonCostList = ko.observableArray([]);
            self.currentGridPersonCost = ko.observable(null);
            self.premiumItems = ko.observableArray([]);
            self.isInsert = ko.observable(true);
        }
        
        /**
         * get data on start page
         */
        startPage(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            var dfdPremiumItemSelect = servicebase.premiumItemSelect();
            var dfdPersonCostCalculationSelect = servicebase.personCostCalculationSelect();
            $.when(dfdPremiumItemSelect, dfdPersonCostCalculationSelect).done((premiumItemSelectData, dfdPersonCostCalculationSelectData) => {
                // Premium Item Select: Done
                premiumItemSelectData.forEach(function(item) {
                    self.premiumItems.push(
                        new vmbase.PremiumItem(
                            item.companyID,
                            item.id,
                            item.attendanceID,
                            item.name,
                            item.displayNumber,
                            item.useAtr
                        ));
                });
                let sum = 0;
                self.premiumItems().forEach(function(item){
                    sum+=item.useAtr();    
                });
                if(sum==0){ // open premiumItem dialog when no premium is used
                    self.premiumDialog();        
                }
                // PersonCostCalculationSelect: Done
                if (dfdPersonCostCalculationSelectData.length) {
                    self.loadData(dfdPersonCostCalculationSelectData, 0);
                    self.currentGridPersonCost.subscribe(function(value) { // change current personCostCalculation when grid is selected
                        self.currentPersonCost(_.find(self.personCostList(), function(o) { return o.startDate() == _.split(value, ' ', 1)[0]; }));
                        self.newStartDate(self.currentPersonCost().startDate());
                        $("#startDateInput").ntsError('clear');
                        $(".premiumInput").ntsError('clear');
                        nts.uk.ui.errors.clearAll();
                        ko.utils.arrayForEach(self.currentPersonCost().premiumSets(), function(premiumSet, index) {
                            let iDList = [];
                            self.currentPersonCost().premiumSets()[index].attendanceItems().forEach(function(item) {
                                iDList.push(item.shortAttendanceID);
                            });
                            self.getItem(iDList, index);
                        });
                        self.isInsert(false);
                    });

                    self.isInsert(false);
                }
                self.newStartDate.subscribe(function(value) {
                    if (self.isInsert()) { // check startDate input when insert
                        if (_.size(self.personCostList()) != 0) { // when list personCostCalculation not empty
                            if ((self.newStartDate() == "") || vmbase.ProcessHandler.validateDateInput(self.newStartDate(), _.last(self.personCostList()).startDate())) {
                                $("#startDateInput").ntsError('set', {messageId:"Msg_65"});
                            } else { 
                                $("#startDateInput").ntsError('clear'); 
                            }
                        } else { // when list personCostCalculation empty
                            if ((self.newStartDate() == "") || vmbase.ProcessHandler.validateDateInput(self.newStartDate(), "1900/01/01")) {
                                $("#startDateInput").ntsError('set', {messageId:"Msg_65"});
                            } else { 
                                $("#startDateInput").ntsError('clear'); 
                            }
                        }
                    }
                });
                dfd.resolve();
            }).fail((res1, res2) => {
                nts.uk.ui.dialog.alert(res1.message+'\n'+res2.message);
                dfd.reject();
            });
            return dfd.promise();
        }
        
        /**
         * set new data to element on screen
         */
        private loadData(res: Array<any>, index: number) {
            var self = this;
            
            // set data to currrent PersonCostCalculation
            res.forEach(function(personCostCalc) {
                self.personCostList.push(vmbase.ProcessHandler.fromObjectPerconCost(personCostCalc));
            });
            self.currentPersonCost((_.first(self.personCostList()) == null) ? new vmbase.PersonCostCalculation('', '', "", "9999/12/31", 0, '', []) : self.personCostList()[index]);
            self.newStartDate(self.currentPersonCost().startDate());
            
            // set data to grid list
            self.personCostList().forEach(function(item) { self.gridPersonCostList.push(new vmbase.GridPersonCostCalculation(item.startDate() + " ~ " + item.endDate())) });
            self.currentGridPersonCost(self.currentPersonCost().startDate() + " ~ " + self.currentPersonCost().endDate());
            ko.utils.arrayForEach(self.currentPersonCost().premiumSets(), function(premiumSet, index) {
                let iDList = [];
                self.currentPersonCost().premiumSets()[index].attendanceItems().forEach(function(item) {
                    iDList.push(item.shortAttendanceID);
                });
                self.getItem(iDList, index);
            });
            if (_.size(self.personCostList()) == 0) {
                self.lastStartDate = "1900/01/01";
            } else {
                self.lastStartDate = _.last(self.personCostList()).startDate();
            } 
        }
        
        /**
         * get list item for each premium setting
         */
        private getItem(iDList: Array<number>, index: number) {
            var self = this;
            var dfd = $.Deferred();
            if (iDList.length != 0) {
                self.currentPersonCost().premiumSets()[index].attendanceItems.removeAll();
                servicebase.getAttendanceItems(iDList)
                    .done(function(res: Array<any>) {
                        let newList = [];
                        res.forEach(function(item) {
                            newList.push(new vmbase.AttendanceItem(item.attendanceItemId, item.attendanceItemName));
                        });
                        self.currentPersonCost().premiumSets()[index].attendanceItems(newList);
                    })
                    .fail(function(res) {
                        nts.uk.ui.dialog.alert(res.message);
                    });
            }
        }
        
        /**
         * insert/update new person cost calculation 
         */
        saveData(): void {
            var self = this;
            if (self.isInsert()) {
                let index = _.findLastIndex(self.personCostList()) + 1;
                if ((self.newStartDate() != "") && !vmbase.ProcessHandler.validateDateInput(self.currentPersonCost().startDate(), self.lastStartDate)) {
                    // insert new data if startDate have no error
                    self.currentPersonCost().startDate(self.newStartDate());
                    servicebase.personCostCalculationInsert(vmbase.ProcessHandler.toObjectPersonCost(self.currentPersonCost()))
                        .done(function(res: Array<any>) {
                            servicebase.personCostCalculationSelect()
                                .done(function(newData: Array<any>) {
                                    // refresh data list
                                    self.personCostList.removeAll();
                                    self.gridPersonCostList.removeAll();
                                    self.loadData(newData, index);
                                    self.isInsert(false);
                                    nts.uk.ui.dialog.alert(vmbase.MSG.MSG015);
                                }).fail(function(res) {
                                    nts.uk.ui.dialog.alert(res.message);
                                });
                        }).fail(function(res) {
                            nts.uk.ui.dialog.alert(res.message);
                        });
                } else {
                    $("#startDateInput").ntsError('set', {messageId:"Msg_65"});
                }
            } else {
                // update new data for current personCostCalculation
                let index = _.findIndex(self.personCostList(), function(item){return item.historyID() == self.currentPersonCost().historyID()});
                servicebase.personCostCalculationUpdate(vmbase.ProcessHandler.toObjectPersonCost(self.currentPersonCost()))
                    .done(function(res: Array<any>) {
                        servicebase.personCostCalculationSelect()
                            .done(function(newData: Array<any>) {
                                // refresh data list
                                self.personCostList.removeAll();
                                self.gridPersonCostList.removeAll();
                                self.loadData(newData, index);
                                nts.uk.ui.dialog.alert(vmbase.MSG.MSG015);
                            }).fail(function(res) {
                                nts.uk.ui.dialog.alert(res.message);
                            });
                    }).fail(function(res) {
                        nts.uk.ui.dialog.alert(res.message);
                    });
            }
        }

        /**
         * open premium dialog
         */
        premiumDialog(): void {
            var self = this;
            let index = _.findIndex(self.personCostList(), self.currentPersonCost())
            nts.uk.ui.windows.setShared('isInsert', self.isInsert());
            nts.uk.ui.windows.sub.modal("/view/kml/001/b/index.xhtml", { title: "割増項目の設定", dialogClass: "no-close" }).onClosed(function() {
                if (nts.uk.ui.windows.getShared('updatePremiumSeting') == true) {
                    // refresh and set new data if premiumSetting change
                    servicebase.personCostCalculationSelect()
                        .done(function(res: Array<any>) {
                            let processItem;
                            if (self.isInsert()) {
                                processItem = self.currentPersonCost();
                            }
                            self.personCostList.removeAll();
                            self.gridPersonCostList.removeAll();
                            self.loadData(res, index);
                            if (self.isInsert()) { // set data when create new data
                                self.currentPersonCost(processItem);
                                let lastItem = _.last(self.personCostList());
                                self.currentPersonCost().premiumSets().forEach(function(item, index) {
                                    item.useAtr(lastItem.premiumSets()[index].useAtr());
                                });
                            }
                        }).fail(function(res) {
                            nts.uk.ui.dialog.alert(res.message);
                        });
                    if (_.size(self.personCostList()) == 0) { // set data when PersonCostCalculation list empty
                        servicebase.premiumItemSelect()
                            .done(function(res: Array<any>) {
                                self.premiumItems.removeAll();
                                res.forEach(function(item) {
                                    self.premiumItems.push(
                                        new vmbase.PremiumItem(
                                            item.companyID,
                                            item.id,
                                            item.attendanceID,
                                            item.name,
                                            item.displayNumber,
                                            item.useAtr));
                                });
                            }).fail(function(res) {
                                nts.uk.ui.dialog.alert(res.message);
                            });
                    }
                }
            });
        }

        /**
         * open create dialog
         */
        createDialog(): void {
            var self = this;
            let lastestHistory = _.last(self.personCostList());
            nts.uk.ui.windows.setShared('lastestStartDate', lastestHistory == null ? "1900/01/01" : lastestHistory.startDate());
            nts.uk.ui.windows.setShared('size', _.size(self.personCostList()));
            nts.uk.ui.windows.sub.modal("/view/kml/001/c/index.xhtml", { title: "履歴の追加", dialogClass: "no-close" }).onClosed(function() {
                let newStartDate: string = nts.uk.ui.windows.getShared('newStartDate');
                if (newStartDate != null) {
                    let copyDataFlag: boolean = nts.uk.ui.windows.getShared('copyDataFlag');
                    if (_.size(self.personCostList()) != 0) { // when PersonCostCalculation list not empty
                        if (copyDataFlag) { // when new data is copy
                            let lastItem = self.clonePersonCostCalculation(_.last(self.personCostList()));
                            self.currentPersonCost(
                                new vmbase.PersonCostCalculation(
                                    lastItem.companyID(),
                                    '',
                                    newStartDate,
                                    "9999/12/31",
                                    lastItem.unitPrice(),
                                    lastItem.memo(),
                                    []));
                            self.currentPersonCost().premiumSets(lastItem.premiumSets());
                            self.newStartDate(self.currentPersonCost().startDate());
                        } else { 
                            self.currentPersonCost(
                                new vmbase.PersonCostCalculation(
                                    '',
                                    '',
                                    newStartDate,
                                    "9999/12/31",
                                    0,
                                    '',
                                    []));
                            let newPremiumSets = [];
                            for (let i = 1; i <= 10; i++) {
                                newPremiumSets.push(new vmbase.PremiumSetting("", "", i, 0, i, "", i, self.premiumItems()[i - 1].useAtr(), []));
                            }
                            self.currentPersonCost().premiumSets(newPremiumSets);
                            self.newStartDate(self.currentPersonCost().startDate());
                        }
                        self.isInsert(true);
                    } else { // when PersonCostCalculation list empty
                        self.currentPersonCost(
                            new vmbase.PersonCostCalculation(
                                '',
                                '',
                                newStartDate,
                                "9999/12/31",
                                0,
                                '',
                                []));
                        let newPremiumSets = [];
                        for (let i = 1; i <= 10; i++) {
                            newPremiumSets.push(new vmbase.PremiumSetting("", "", i, 0, i, "", i, self.premiumItems()[i - 1].useAtr(), []));
                        }
                        self.currentPersonCost().premiumSets(newPremiumSets);
                        self.newStartDate(self.currentPersonCost().startDate());
                        self.isInsert(true);
                    }
                }
            });
        }

        /**
         * open edit dialog
         */
        editDialog(): void {
            var self = this;
            let index = _.findIndex(self.personCostList(), self.currentPersonCost())
            nts.uk.ui.windows.setShared('personCostList', self.personCostList());
            nts.uk.ui.windows.setShared('currentPersonCost', self.currentPersonCost());
            nts.uk.ui.windows.sub.modal("/view/kml/001/d/index.xhtml", { title: "履歴の編集", dialogClass: "no-close" }).onClosed(function() {
                let editedIndex = nts.uk.ui.windows.getShared('isEdited');
                if (editedIndex != null) { // when data is edited
                    if (editedIndex == 1) index -= 1; // when edit is delete, set index to last item
                    servicebase.personCostCalculationSelect()
                        .done(function(res: Array<any>) {
                            // refresh data list
                            self.personCostList.removeAll();
                            self.gridPersonCostList.removeAll();
                            self.loadData(res, index);
                        }).fail(function(res) {
                            nts.uk.ui.dialog.alert(res.message);
                        });
                }
            });;
        }
        
        /**
         * open select item dialog
         */
        selectDialog(data, index): void {
            var self = this;
            let currentList = [];
            ko.utils.arrayForEach(data.attendanceItems(), function(attendanceItem: vmbase.AttendanceItem) {
                currentList.push(attendanceItem.shortAttendanceID);
            });
            nts.uk.ui.windows.setShared('SelectedAttendanceId', currentList,true);
            nts.uk.ui.windows.setShared('Multiple', true);
            servicebase.getAttendanceItemByType(0)
                .done(function(res: Array<any>) {
                    nts.uk.ui.windows.setShared('AllAttendanceObj', _.map(res, function(item) { return item.attendanceItemId }));
                    nts.uk.ui.windows.sub.modal("/view/kdl/021/a/index.xhtml", { title: "割増項目の設定", dialogClass: "no-close" }).onClosed(function() {
                        let newList = nts.uk.ui.windows.getShared('selectedChildAttendace');
                        if (newList != null) {
                            if (newList.length != 0) {
                                if (!_.isEqual(newList, currentList)) {
                                    //clone Knockout Object
                                    self.currentPersonCost(self.clonePersonCostCalculation(self.currentPersonCost()));
                                    self.getItem(newList,index);
                                }
                            } else {
                                self.currentPersonCost().premiumSets()[index].attendanceItems([]);
                            }
                        }
                    });
                }).fail(function(res) {
                    nts.uk.ui.dialog.alert(res.message);         
                });

        }
        
        /**
         * clone PersonCostCalculation Object
         */
        private clonePersonCostCalculation(object: vmbase.PersonCostCalculation): vmbase.PersonCostCalculation {
            return vmbase.ProcessHandler.fromObjectPerconCost(
                                            _.cloneDeep(
                                                vmbase.ProcessHandler.toObjectPersonCost(object)));
        }
    }
}