

module qmm020.b.viewmodel {
    export class ScreenModel {
        //content:  KnockoutObservable<any>;
        itemList: KnockoutObservableArray<ComHistItem>;
        currentItem: KnockoutObservable<ComHistItem>;
        maxItem: KnockoutObservable<service.model.CompanyAllotSettingDto>;
        companyAllots: Array<service.model.CompanyAllotSettingDto>;
        isInsert: KnockoutObservable<boolean>;
        constructor() {
            var self = this;
            self.isInsert = ko.observable(false);
            self.itemList = ko.observableArray([]);
            self.currentItem = ko.observable(new ComHistItem({
                historyId: '',
                startYm: '',
                endYm: '',
                payCode: '',
                bonusCode: ''
            }));
            self.maxItem = ko.observable(new service.model.CompanyAllotSettingDto());
            self.start();
        }
        // start function
        start(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred<any>();
            //Get list startDate, endDate of History 
            //get allot Company
            service.getAllotCompanyList().done(function(companyAllots: Array<service.model.CompanyAllotSettingDto>) {
                
                if (companyAllots.length > 0) {
                    let _items: Array<ComHistItem> = [];
                    //push data to listItem of hist List
                    for (let i in companyAllots) {
                        let item = companyAllots[i];
                        if (item) {
                            _items.push(new ComHistItem({
                                historyId: (item.historyId || "").toString(),
                                startYm: (item.startDate || "").toString(),
                                endYm: (item.endDate || "").toString(),
                                payCode: (item.paymentDetailCode || "").toString(),
                                bonusCode: (item.bonusDetailCode || "").toString()
                            }));
                        }
                    }
                    self.itemList(_items);
                    self.companyAllots = companyAllots;
                    //console.log(self.companyAllots);
                    self.currentItem().setSource(self.companyAllots);
                    //console.log(self.companyAllots);
                    self.currentItem().historyId(companyAllots[0].historyId);
                } else {
                    //self.allowClick(false);
                    dfd.resolve();
                }
            }).fail(function(res) {
                // Alert message
                alert(res);
            });
            //get Row with max Date 
            service.getAllotCompanyMaxDate().done(function(itemMax: service.model.CompanyAllotSettingDto) {
                self.maxItem(itemMax);
            }).fail(function(res) {
                alert(res);
            });

            return dfd.promise();
        }

        //Event of Save Button Clicking
        //Insert or Update Data
        register() {
            var self = this;
            //debugger;
            //Insert
            if (self.isInsert()) {
                service.insertComAllot(self.currentItem()).done(function() {
                }).fail(function(res) {
                    alert(res);
                });
                //Update    
            } else {
                service.updateComAllot(self.currentItem()).done(function() {
                }).fail(function(res) {
                    alert(res);
                });
            }
        }

        //Open dialog Add History
        openJDialog() {
            var self = this;
            //getMaxDate
            var historyScreenType = "1";
            //Get value TabCode + value of selected Name in History List
            let valueShareJDialog = historyScreenType + "~" + self.currentItem().startYm();

            nts.uk.ui.windows.setShared('valJDialog', valueShareJDialog);

            nts.uk.ui.windows.sub.modal('/view/qmm/020/j/index.xhtml', { title: '譏守ｴｰ譖ｸ縺ｮ邏舌★縺托ｼ槫ｱ･豁ｴ霑ｽ蜉�' })
                .onClosed(function() {
                    let returnJDialog: string = nts.uk.ui.windows.getShared('returnJDialog');
                    if (returnJDialog != undefined) {
                        var modeRadio = returnJDialog.split("~")[0];
                        var returnValue = returnJDialog.split("~")[1];
                        if (returnValue != '') {
                            var items = [];
                            var addItem = new ComHistItem({
                                historyId: new Date().getTime().toString(),
                                startYm: returnValue,
                                endYm: '999912',
                                payCode: '',
                                bonusCode: ''
                            });
                            // them thang cuoi cung tren man hinh
                            items.push(addItem);
                            //them cac thang da co
                            for (let i in self.itemList()) {
                                items.push(self.itemList()[i]);
                            }
                            //UPDATE ENDATE MAX
                            var lastValue = _.find(self.companyAllots, function(item) { return item.endDate == 999912; });
                            if (lastValue != undefined) {
                                lastValue.endDate = previousYM(returnValue);
                                var updateValue = _.find(items, function(item) { return item.endYm() == "999912" && item.startYm() != returnValue; });
                                if (updateValue != undefined) {
                                    updateValue.endYm(lastValue.endDate.toString());
                                }
                            }
                            // Goi cap nhat vao currentItem
                            // Trong truong hop them moi NHANH, copy payCode, bonusCode tu Previous Item
                            if (modeRadio === "1") {
                                //the new items
                                //update payCode, bonus Code cua thang vua them
                                //Update Current item tren man hinh 
                                self.currentItem().historyId(addItem.historyId());
                                self.currentItem().startYm(returnValue);
                                self.currentItem().endYm('999912');

                                self.currentItem().payCode(self.maxItem().paymentDetailCode);
                                self.currentItem().bonusCode(self.maxItem().bonusDetailCode);
                                //get Payment Name
                                if (self.currentItem().payCode() != '') {
                                    service.getAllotLayoutName(self.currentItem().payCode()).done(function(stmtName: string) {
                                        self.currentItem().payName(stmtName);
                                    }).fail(function(res) {
                                        self.currentItem().payName('');
                                    });
                                } else {
                                    self.currentItem().payName('');
                                }
                                //get Bonus Name
                                if (self.currentItem().bonusCode() != '') {
                                    service.getAllotLayoutName(self.currentItem().bonusCode()).done(function(stmtName: string) {
                                        self.currentItem().bonusName(stmtName);
                                    }).fail(function(res) {
                                        self.currentItem().bonusName('');
                                    });
                                } else {
                                    self.currentItem().bonusName('');
                                }
                            } else {
                                self.currentItem().historyId(addItem.historyId());
                                self.currentItem().startYm(returnValue);
                                self.currentItem().endYm('999912');

                                self.currentItem().payCode('');
                                self.currentItem().bonusCode('');
                                self.currentItem().payName('');
                                self.currentItem().bonusName('');
                            }
                        }
                        //console.log(self.companyAllots);
                        self.itemList([]);
                        self.itemList(items);
                        self.isInsert = ko.observable(true);
                        //debugger;
                    }
                });
        }

        //Open dialog Edit History
        openKDialog() {
            var self = this;
            nts.uk.ui.windows.setShared("endYM", self.currentItem().endYm());
            nts.uk.ui.windows.setShared('scrType', '1');
            nts.uk.ui.windows.setShared('startYM', self.currentItem().startYm());
            var current = _.find(self.companyAllots, function(item) { return item.historyId == self.currentItem().historyId(); });
            var previousItem = _.find(self.companyAllots, function(item) { return item.endDate == parseInt(self.currentItem().startYm()) - 1; });
            if (current) {
                nts.uk.ui.windows.setShared('currentItem', current);
            }
            if (previousItem) {
                nts.uk.ui.windows.setShared('previousItem', previousItem);
            }
            nts.uk.ui.windows.sub.modal('/view/qmm/020/k/index.xhtml', { title: '譏守ｴｰ譖ｸ縺ｮ邏舌★縺托ｼ槫ｱ･豁ｴ邱ｨ髮�' }).onClosed(function(): any {
                self.start();
            });
        }
        //Open L Dialog
        openLDialog() {
            alert('2017');
        }
        //Click to button Select Payment
        openPaymentMDialog() {
            var self = this;
            var valueShareMDialog = self.currentItem().startYm();
            //debugger;
            nts.uk.ui.windows.setShared('valMDialog', valueShareMDialog);
            nts.uk.ui.windows.sub.modal('/view/qmm/020/m/index.xhtml', { title: '譏守ｴｰ譖ｸ縺ｮ驕ｸ謚�' }).onClosed(function(): any {
                //get selected code from M dialog
                var stmtCodeSelected = nts.uk.ui.windows.getShared('stmtCodeSelected');

                self.currentItem().payCode(stmtCodeSelected);
                //get Name payment Name
                service.getAllotLayoutName(self.currentItem().payCode()).done(function(stmtName: string) {
                    self.currentItem().payName(stmtName);
                }).fail(function(res) {
                    alert(res);
                });
            });
        }

        //Click to button Select Bonus
        openBonusMDialog() {
            var self = this;
            var valueShareMDialog = self.currentItem().startYm();
            //debugger;
            nts.uk.ui.windows.setShared('valMDialog', valueShareMDialog);
            nts.uk.ui.windows.sub.modal('/view/qmm/020/m/index.xhtml', { title: '譏守ｴｰ譖ｸ縺ｮ驕ｸ謚�' }).onClosed(function(): any {
                //get selected code from M dialog
                var stmtCodeSelected = nts.uk.ui.windows.getShared('stmtCodeSelected');

                self.currentItem().bonusCode(stmtCodeSelected);
                //get Name payment Name
                service.getAllotLayoutName(self.currentItem().bonusCode()).done(function(stmtName: string) {
                    self.currentItem().bonusName(stmtName);
                }).fail(function(res) {
                    alert(res);
                });
            });
        }

    }
    //Previous Month 
    function previousYM(sYm: string) {
        var preYm: number = 0;
        if (sYm.length == 6) {
            let sYear: string = sYm.substr(0, 4);
            let sMonth: string = sYm.substr(4, 2);
            //Trong truong hop thang 1 thi thang truoc la thang 12
            if (sMonth == "01") {
                preYm = parseInt((parseInt(sYear) - 1).toString() + "12");
                //Truong hop con lai thi tru di 1      
            } else {
                preYm = parseInt(sYm) - 1;
            }
        }
        return preYm;
    }

    interface IComHistItem {
        historyId: string;
        startYm: string;
        endYm: string;
        payCode: string;
        bonusCode: string;
        startEnd?: string;
        payName?: string;
        bonusName?: string;
    }

    class ComHistItem {
        id: string;
        historyId: KnockoutObservable<string>;
        startYm: KnockoutObservable<string>;
        endYm: KnockoutObservable<string>;
        payCode: KnockoutObservable<string>;
        bonusCode: KnockoutObservable<string>;
        startEnd: string;
        payName: KnockoutObservable<string>;
        bonusName: KnockoutObservable<string>;
        listSource: Array<any>;
        constructor(param: IComHistItem) {
            let self = this;
            self.id = param.historyId;
            self.historyId = ko.observable(param.historyId);
            self.startYm = ko.observable(param.startYm);
            self.endYm = ko.observable(param.endYm);
            self.payCode = ko.observable(param.payCode);
            self.bonusCode = ko.observable(param.bonusCode);
            self.startEnd = param.startYm + '~' + param.endYm;
            self.payName = ko.observable(param.payName || '');
            self.bonusName = ko.observable(param.bonusName || '');

            self.historyId.subscribe(function(newValue) {
                if (typeof newValue != 'string') {
                    return
                }
                var current = _.find(self.listSource, function(item) { return item.historyId == newValue; });
                if (current) {
                    self.startYm(current.startDate);
                    self.endYm(current.endDate);
                    self.payCode(current.paymentDetailCode);
                    self.bonusCode(current.bonusDetailCode);
                    self.startEnd = self.startYm() + '~' + self.endYm();

                    if (current.paymentDetailCode != '') {
                        service.getAllotLayoutName(current.paymentDetailCode).done(function(stmtName: string) {
                            self.payName(stmtName);
                        }).fail(function(res) {
                            self.payName('');
                        });
                    } else {
                        self.payName('');
                    }
                    if (current.bonusDetailCode != '') {
                        service.getAllotLayoutName(current.bonusDetailCode).done(function(stmtName: string) {
                            self.bonusName(stmtName);
                        }).fail(function(res) {
                            self.bonusName('');
                        });
                    } else {
                        self.bonusName('');
                    }

                }
                else { // Them moi
                    var newItem: service.model.CompanyAllotSettingDto = {
                        paymentDetailCode: '',
                        bonusDetailCode: '',
                        startDate: 0,
                        endDate: 0,
                        historyId: self.historyId()
                    };
                    self.listSource.push(newItem);
                    self.payName('');
                    self.bonusName('');
                }
            });

            self.payCode.subscribe(function(newValue) {
                //console.log(self.listSource);
                var current = _.find(self.listSource, function(item) { return item.historyId == self.historyId(); });
                if (current) {
                    current.paymentDetailCode = newValue;
                }
            });
            self.bonusCode.subscribe(function(newValue) {
                var current = _.find(self.listSource, function(item) { return item.historyId == self.historyId(); });
                if (current) {
                    current.bonusDetailCode = newValue;
                }
            });


            self.startYm.subscribe(function(newValue) {
                self.startEnd = self.startYm() + '~' + self.endYm();
                var current = _.find(self.listSource, function(item) { return item.historyId == self.historyId(); });
                if (current) {
                    current.startDate = newValue;
                }
            });

            self.endYm.subscribe(function(newValue) {
                self.startEnd = self.startYm() + '~' + self.endYm();
                var current = _.find(self.listSource, function(item) { return item.historyId == self.historyId(); });
                if (current) {
                    current.endDate = newValue;
                }
            });
        }

        setSource(list: Array<any>) {
            this.listSource = list || [];
        }
    }
}
