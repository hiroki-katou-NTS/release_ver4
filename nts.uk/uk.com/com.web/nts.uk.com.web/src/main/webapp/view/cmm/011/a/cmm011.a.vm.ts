module cmm011.a.viewmodel {


    export class ScreenModel {

        //  list box
        itemHistId: KnockoutObservableArray<any>;
        historyId: KnockoutObservable<any>;
        selectedCodes_His: KnockoutObservable<any>;
        itemHist: KnockoutObservable<any>;
        arr: any;
        enablebtnDelete: KnockoutObservable<boolean>;
        enableDDialog: KnockoutObservable<boolean>;
        enablebtnupdown: KnockoutObservable<boolean>;

        // treegrid
        dataSource: KnockoutObservableArray<any>;
        // dataSourceToInsert : chứa list workplace để insert vào csdl trong trường hợp thêm mới lịch sử
        dataSourceToInsert: KnockoutObservableArray<any>;
        currentItem_treegrid: KnockoutObservable<any>;
        singleSelectedCode: KnockoutObservable<string>;
        selectedCodes_treegrid: any;
        headers: any;
        lengthTreeBegin: KnockoutObservable<number>;
        lengthTreeCurrent: KnockoutObservable<number>;

        allowClick: KnockoutObservable<boolean> = ko.observable(true);
        checknull: KnockoutObservable<string>; // biến để check trường hợp HistoryList có null hay không?
        dataSourceFlat: any; // dataSource sau khi Flat
        itemaddHist: any;
        numberItemNew: KnockoutObservable<number>;
        // listDtoUpdateHierachy : chứa list workplace bị thay đổi hirachy khi thêm mới workplace.
        // gửi list này lên để update hirachy.
        listDtoUpdateHierachy: KnockoutObservable<any>;
        dtoAdd: KnockoutObservable<any>;
        checkAddHist1: KnockoutObservable<string>;
        newEndDate: KnockoutObservable<string>;
        // arrayItemEdit : chứa list workplace để update hirachy khi click button updaown
        arrayItemEdit: KnockoutObservableArray<any>;

        A_INP_CODE: KnockoutObservable<string>;
        A_INP_CODE_enable: KnockoutObservable<boolean>;
        A_INP_NAME: KnockoutObservable<string>;
        A_INP_FULLNAME: KnockoutObservable<string>;
        A_INP_OUTCODE: KnockoutObservable<string>;
        A_INP_MEMO: KnockoutObservable<string>;

        constructor() {
            var self = this;
            self.itemHistId = ko.observableArray([]);
            self.historyId = ko.observable('');
            self.enablebtnDelete = ko.observable(true);
            self.enableDDialog = ko.observable(true);
            self.enablebtnupdown = ko.observable(true);
            self.selectedCodes_His = ko.observable('');
            self.itemHist = ko.observable(null);
            self.arr = ko.observableArray([]);
            self.A_INP_CODE = ko.observable(null);
            self.A_INP_NAME = ko.observable(null);
            self.A_INP_FULLNAME = ko.observable(null);
            self.A_INP_OUTCODE = ko.observable(null);
            self.A_INP_CODE_enable = ko.observable(false);
            self.A_INP_MEMO = ko.observable(null);

            self.dataSource = ko.observableArray([]);
            self.dataSourceToInsert = ko.observableArray([]);
            self.singleSelectedCode = ko.observable(null);
            self.selectedCodes_treegrid = ko.observableArray([]);
            self.headers = ko.observableArray(["", ""]);
            self.lengthTreeCurrent = ko.observable(null);
            self.lengthTreeBegin = ko.observable(null);
            self.numberItemNew = ko.observable(0);

            self.currentItem_treegrid = ko.observable(null);
            self.checknull = ko.observable(null);
            self.listDtoUpdateHierachy = ko.observable(null);
            self.dtoAdd = ko.observable(null);
            self.checkAddHist1 = ko.observable('');
            self.newEndDate = ko.observable(null);
            self.arrayItemEdit = ko.observableArray([]);

            self.singleSelectedCode.subscribe(function(codeChangeds) {
                var _dt = self.dataSource();
                var _code = self.singleSelectedCode();
                var current = self.findHira(_code, _dt);
                if (current.historyId == "") {
                    self.A_INP_CODE_enable(true);
                    self.A_INP_CODE("");
                    self.A_INP_NAME("");
                    self.A_INP_FULLNAME("");
                    self.A_INP_OUTCODE("");
                    $("#A_INP_CODE").focus();
                }
                else {
                    self.A_INP_CODE(current.departmentCode);
                    self.A_INP_NAME(current.name);
                    self.A_INP_FULLNAME(current.fullName);
                    self.A_INP_OUTCODE(current.externalCode);
                    self.A_INP_CODE_enable(false);
                }

            });

            self.selectedCodes_His.subscribe((function(codeChanged) {
                self.findHist_Dep(self.itemHistId(), codeChanged);
                if (self.itemHist() != null) {
                    if (self.itemHist().historyId != "") {
                        self.enableBtn();
                        for (var i = 0; i < self.itemHistId().length; i++) {
                            if (self.itemHistId()[i].historyId == "") {
                                let item = self.itemHistId()[i];
                                self.itemHistId.remove(item);
                                let _dt = self.itemHistId();
                                self.itemHistId([]);
                                _dt[0].endDate = "9999/12/31";
                                self.itemHistId(_dt);
                            }
                        }
                        if (self.numberItemNew() == 1) {
                            self.numberItemNew(0);
                        }
                        self.historyId(self.itemHist().historyId);
                        //get position by historyId
                        var dfd = $.Deferred();
                        service.getAllWorkPLaceByHistId(self.historyId())
                            .done(function(department_arr: Array<viewmodel.model.DtoWKP>) {
                                self.dataSource(department_arr);
                                if (self.dataSource().length > 0) {
                                    self.dataSourceFlat = ko.observableArray(nts.uk.util.flatArray(self.dataSource(), "children"));
                                    self.singleSelectedCode(self.dataSource()[0].departmentCode);
                                }
                            }).fail(function(error) {
                                alert(error.messageId);
                            })
                        service.getMemoWorkPLaceByHistId(self.historyId())
                            .done(function(memo: viewmodel.model.MemoDto) {
                                if (memo != null) {
                                    self.A_INP_MEMO(memo.memo);
                                }
                            }).fail(function(error) {
                                alert(error.messageId);
                            })
                        dfd.resolve();
                        return dfd.promise();
                    } else {
                    }
                }
            }));
            // event khi click button up
            $(document).delegate("#tree-up-down-up", "click", function() {
                self.checkAddHist1("clickbtnupdown");
                self.updateHirechyOfBtnUpDown();
            });

            // event khi click button down
            $(document).delegate("#tree-up-down-down", "click", function() {
                self.checkAddHist1("clickbtnupdown");
                self.updateHirechyOfBtnUpDown();
            });
        }

        register() {
            var self = this;
            self.enableBtn();
            /*case add item lần đầu khi history == null*/
            if (self.checknull() === "landau" && self.itemHistId().length == 1 && self.checkInput()) {
                let dto = new model.AddWorkplaceDto(self.A_INP_CODE(), null, "9999/12/31", self.A_INP_OUTCODE(), self.A_INP_FULLNAME(), "001", self.A_INP_NAME(), self.itemaddHist.startDate, self.A_INP_MEMO(), self.A_INP_NAME(), "1", "1", null, null, null);
                var dfd = $.Deferred();
                let arr = new Array;
                arr.push(dto);
                service.addWorkPlace(arr)
                    .done(function(mess: any) {
                        location.reload();
                    }).fail(function(error) {
                        if (error.messageId == "ER005") {
                            alert("入力したコードは既に存在しています。\r\n コードを確認してください。 ");
                            $("#A_INP_CODE").focus();
                        }
                    })
                dfd.resolve();
                return dfd.promise();
            }
            /*case update item*/
            if (self.A_INP_CODE_enable() == false && self.checkInput() && self.checkAddHist1() == '') {
                var dfd = $.Deferred();
                let hisdto = self.findHist_Dep(self.itemHistId(), self.selectedCodes_His());
                var current:any = self.findHira(self.singleSelectedCode(), self.dataSource());
                let dto = new model.AddWorkplaceDto(self.A_INP_CODE(), hisdto.historyId, hisdto.endDate, self.A_INP_OUTCODE(), self.A_INP_FULLNAME(), current.hierarchyCode, self.A_INP_NAME(), hisdto.startDate, self.A_INP_MEMO(), current.shortName, current.parentChildAttribute1, current.parentChildAttribute2, null, null, null);
                let wkpCodeItemUpdate = self.A_INP_CODE();
                let arr = new Array;
                arr.push(dto);
                service.upDateListWorkplace(arr)
                    .done(function(mess: any) {
                        self.getAllWorkPlaceByHistId(hisdto.historyId, wkpCodeItemUpdate);
                        self.numberItemNew(0);
                    }).fail(function(error) {
                        if (error.messageId == "ER06") {
                            alert("対象データがありません。");
                        }
                    })
                dfd.resolve();
                return dfd.promise();
            }
            /*case add item trong trường hợp histtory không thay đổi*/
            if (self.numberItemNew() == 1 && self.checkInput()) {
                var self = this;
                var dfd = $.Deferred();
                let hisdto = self.findHist_Dep(self.itemHistId(), self.selectedCodes_His());
                let _dto = new model.AddWorkplaceDto(self.A_INP_CODE(), hisdto.historyId, hisdto.endDate, self.A_INP_OUTCODE(), self.A_INP_FULLNAME(), self.dtoAdd().hierarchyCode, self.A_INP_NAME(), hisdto.startDate, self.A_INP_MEMO(), self.A_INP_FULLNAME(), "1", "1", null, null, null);
                let wkpCodeItemUpdate = self.A_INP_CODE();
                let data = self.listDtoUpdateHierachy();
                let arr = new Array;
                arr.push(_dto);
                if (data != null) {
                    service.upDateListWorkplace(data)
                        .done(function(mess) {
                            var dfd2 = $.Deferred();
                            service.addWorkPlace(arr)
                                .done(function(mess: any) {
                                    self.getAllWorkPlaceByHistId(hisdto.historyId, wkpCodeItemUpdate);
                                    self.numberItemNew(0);
                                })
                                .fail(function(error) {
                                    if (error.messageId == "ER005") {
                                        alert("入力したコードは既に存在しています。\r\n コードを確認してください。 ");
                                        $("#A_INP_CODE").focus();
                                    }
                                })
                            dfd2.resolve();
                            return dfd2.promise();
                        }).fail(function(error) { })
                    dfd.resolve();
                    return dfd.promise();
                } else {
                    var dfd2 = $.Deferred();
                    service.addWorkPlace(arr)
                        .done(function(mess: any) {
                            self.getAllWorkPlaceByHistId(hisdto.historyId, wkpCodeItemUpdate);
                            self.numberItemNew(0);
                        })
                        .fail(function(error) {
                            if (error.messageId == "ER005") {
                                alert("入力したコードは既に存在しています。\r\n コードを確認してください。 ");
                                $("#A_INP_CODE").focus();
                            }
                        })
                    dfd2.resolve();
                    return dfd2.promise();
                }
            }
            /*case add list workplace trong trường hợp thêm mới lịch sử từ lịch sử mới nhất*/
            if (self.checkAddHist1() == "AddhistoryFromLatest") {
                let _dt = self.dataSourceToInsert();
                if (_dt.length > 0) {
                    _dt[0].memo = self.A_INP_MEMO();
                }
                self.dataSourceToInsert(_dt);
                var dfd2 = $.Deferred();
                service.addListWorkPlace(self.dataSourceToInsert())
                    .done(function(mess: any) {
                        var dfd2 = $.Deferred();
                        let _dto = new model.AddWorkplaceDto("", self.itemHistId()[1].historyId, self.itemHistId()[1].endDate, null, null, null, null, null, "addhistoryfromlatest", null, null, null, null, null, null);
                        let arr = new Array;
                        arr.push(_dto);
                        service.upDateEndDateWkp(arr)
                            .done(function() {
                                location.reload();
                            })
                            .fail(function() { })
                        self.start();
                    })
                    .fail(function(error) { })
                dfd2.resolve();
                return dfd2.promise();
            }
            /*case add  workplace trong trường hợp thêm mới lịch sử , listworkplace = null*/
            if (self.checkAddHist1() == "AddhistoryFromBeggin") {
                if (self.checkInput()) {
                    let _dto = new model.AddWorkplaceDto(self.A_INP_CODE(), null, self.itemHistId()[0].endDate, self.A_INP_OUTCODE(), self.A_INP_FULLNAME(), "001", self.A_INP_NAME(), self.itemHistId()[0].startDate, self.A_INP_MEMO(), null, "1", "1", null, null, null);
                    let arr1 = new Array;
                    arr1.push(_dto);
                    var dfd2 = $.Deferred();
                    service.addListWorkPlace(arr1)
                        .done(function(mess: any) {
                            var dfd2 = $.Deferred();
                            let _dto = new model.AddWorkplaceDto("", self.itemHistId()[1].historyId, self.itemHistId()[1].endDate, null, null, null, null, null, "addhistoryfromlatest", null, null, null, null, null, null);
                            let arr = new Array;
                            arr.push(_dto);
                            service.upDateEndDateWkp(arr)
                                .done(function() {
                                    location.reload();
                                })
                                .fail(function() { })
                            self.start();
                        })
                        .fail(function(error) { })
                    dfd2.resolve();
                    return dfd2.promise();
                }
            }
            /*case khi click up down buron ==> *update lại hierachy của các item*/
            if (self.checkAddHist1() == "clickbtnupdown") {
                let _dt = self.arrayItemEdit();
                let hisdto = self.findHist_Dep(self.itemHistId(), self.selectedCodes_His());
                var dfd = $.Deferred();
                if (self.arrayItemEdit().length > 1) {
                    service.upDateListWorkplace(_dt)
                        .done(function(done) {
                            self.getAllWorkPlaceByHistId(hisdto.historyId, self.singleSelectedCode());
                        }).fail(function(error) {
                            alert(error.messageId);
                        })
                    dfd.resolve();
                    return dfd.promise();
                }
            }
        }

        enableBtn() {
            var self = this;
            self.enablebtnDelete(true);
            self.enableDDialog(true);
            self.enablebtnupdown(true);
        }
        disableBtn() {
            var self = this;
            self.enablebtnDelete(false);
            self.enableDDialog(false);
            self.enablebtnupdown(false);
        }

        getAllWorkPlaceByHistId(historyId:any, workplaceCode:any) {
            var self = this;
            var dfd = $.Deferred();
            service.getAllWorkPLaceByHistId(historyId)
                .done(function(department_arr: Array<viewmodel.model.DtoWKP>) {
                    self.dataSource(department_arr);
                    if (self.dataSource().length > 0) {
                        self.dataSourceFlat = ko.observableArray(nts.uk.util.flatArray(self.dataSource(), "children"));
                        self.singleSelectedCode(workplaceCode);
                    }
                }).fail(function(error) {
                    alert(error.messageId);
                })
        }

        // update hirachy khi click btn up down
        updateHirechyOfBtnUpDown() {
            var self = this;
            var _dt = self.dataSource();
            var _code = self.singleSelectedCode();
            var current = self.findHira(_code, _dt);
            var parrent = self.findParent(_code, _dt);
            let hisdto = self.findHist_Dep(self.itemHistId(), self.selectedCodes_His());
            if (parrent) {
                //Parent hirachy code
                var phc = parrent.hierarchyCode;
                var changeIndexChild = _.filter(parrent['children'], function(item) {
                    return item;
                });
                for (var i in changeIndexChild) {
                    var item:any = changeIndexChild[i];
                    var itemHierachy = item.hierarchyCode;
                    var j = parseInt(i) + 1;
                    item.hierarchyCode = item.hierarchyCode.substr(0, item.hierarchyCode.length - 1) + j;
                    item.startDate = hisdto.startDate;
                    item.endDate = hisdto.endDate;
                    item.workPlaceCode = item.departmentCode;
                    item.memo = self.A_INP_MEMO();
                    if (self.arrayItemEdit().length > 0) {
                        let _dt2 = self.arrayItemEdit();
                        var isDuplicateItem = _.filter(_dt2, function(item1) {
                            return item1.departmentCode == item.departmentCode;
                        });
                        if (isDuplicateItem.length > 0) {
                            // xóa isDuplicateItem
                            _dt2 = jQuery.grep(_dt2, function(value) {
                                return value.departmentCode != isDuplicateItem[0].departmentCode;
                            });

                            self.arrayItemEdit(_dt2);
                            self.arrayItemEdit().push(item);
                        } else {
                            self.arrayItemEdit().push(item);
                        }
                    } else {
                        self.arrayItemEdit().push(item);
                    }
                    if (item.children.length > 0) {
                        self.updateHierachyWhenclickUpdownBtn(item);
                    }
                }
            } else {
                //curtent hirachy code
                var chc = current.hierarchyCode;
                var changeIndexChilds = _.filter(_dt, function(item) {
                    return item;
                });
                for (var i in changeIndexChilds) {
                    var item:any = changeIndexChilds[i];
                    var itemHierachy = item.hierarchyCode;
                    var j = parseInt(i) + 1;
                    item.hierarchyCode = item.hierarchyCode.substr(0, item.hierarchyCode.length - 1) + j;
                    item.startDate = hisdto.startDate;
                    item.endDate = hisdto.endDate;
                    item.workPlaceCode = item.departmentCode;
                    item.memo = self.A_INP_MEMO();
                    if (self.arrayItemEdit().length > 0) {
                        let _dt2 = self.arrayItemEdit();
                        var isDuplicateItem = _.filter(_dt2, function(item1) {
                            return item1.departmentCode == item.departmentCode;
                        });
                        if (isDuplicateItem.length > 0) {
                            // xóa isDuplicateItem
                            _dt2 = jQuery.grep(_dt2, function(value) {
                                return value.departmentCode != isDuplicateItem[0].departmentCode;
                            });

                            self.arrayItemEdit(_dt2);
                            self.arrayItemEdit().push(item);
                        } else {
                            self.arrayItemEdit().push(item);
                        }
                    } else {
                        self.arrayItemEdit().push(item);
                    }
                    if (item.children.length > 0) {
                        self.updateHierachyWhenclickUpdownBtn(item);
                    }
                }
            }
        }

        deletebtn() {
            var self = this;
            let hisdto = self.findHist_Dep(self.itemHistId(), self.selectedCodes_His());
            var _dt = self.dataSource();
            var _dtflat = nts.uk.util.flatArray(_dt, 'children');
            var _code = self.singleSelectedCode();
            var current = self.findHira(_code, _dt);
            let deleteobj = new model.WorkPlaceDeleteDto(current.departmentCode, current.historyId, current.hierarchyCode);
            if (_dtflat.length < 2) {
                return;
            } else if (_dt.length < 2 && current.hierarchyCode.length == 3) {
                return;
            }
            nts.uk.ui.dialog.confirm("データを削除します。\r\nよろしいですか？").ifYes(function() {
                var dfd2 = $.Deferred();
                service.deleteWorkPalce(deleteobj)
                    .done(function() {
                        var _dt = self.dataSource();
                        var _code = self.singleSelectedCode();
                        var current = self.findHira(_code, _dt);
                        var parrent = self.findParent(_code, _dt);
                        if (parrent) {
                            var index = parrent.children.indexOf(current);
                            //Parent hirachy code
                            var phc = parrent.hierarchyCode;
                            var chc = parseInt(current.hierarchyCode.substr(current.hierarchyCode.length - 3, 3));

                            // Thay đổi hirachiCode của các object bên dưới
                            var changeIndexChild = _.filter(parrent['children'], function(item:any) {
                                return item.hierarchyCode.length == current.hierarchyCode.length && parseInt(item.hierarchyCode.substr(item.hierarchyCode.length - 3, 3)) > chc;
                            });
                            for (var i in changeIndexChild) {
                                var item1 = changeIndexChild[i];
                                var itemAddH = (parseInt(item1.hierarchyCode.substr(item1.hierarchyCode.length - 3, 3)) - 1) + "";
                                while ((itemAddH + "").length < 3)
                                    itemAddH = "0" + itemAddH;
                                item1.hierarchyCode = phc + itemAddH;
                                item1.editIndex = true;
                                if (item1.children.length > 0) {
                                    self.updateHierachyWhenInsertItem(item1, phc + itemAddH);
                                }
                            }
                            var editObjs = _.filter(nts.uk.util.flatArray(self.dataSource(), 'children'), function(item:any) { return item.editIndex; });
                            if (editObjs.length > 0) {
                                let currentHis = self.itemHist();
                                for (var k = 0; k < editObjs.length; k++) {
                                    editObjs[k].startDate = currentHis.startDate;
                                    editObjs[k].endDate = currentHis.endDate;
                                    editObjs[k].memo = self.A_INP_MEMO();
                                    editObjs[k].workPlaceCode = editObjs[k].departmentCode;
                                }
                            }
                            self.listDtoUpdateHierachy(editObjs);
                        } else {
                            var index:any = _dt.indexOf(current);
                            //Parent hirachy code
                            var phc:any = current.hierarchyCode;
                            var chc = parseInt(current.hierarchyCode.substr(current.hierarchyCode.length - 3, 3));

                            // Thay đổi hirachiCode của các object bên dưới
                            var changeIndexChild2 = _.filter(_dt, function(item) {
                                return item.hierarchyCode.length == current.hierarchyCode.length && parseInt(item.hierarchyCode.substr(item.hierarchyCode.length - 3, 3)) > chc;
                            });

                            for (var i in changeIndexChild2) {
                                var item2 = changeIndexChild2[i];
                                var itemAddH = (parseInt(item2.hierarchyCode.substr(item2.hierarchyCode.length - 3, 3)) - 1) + "";
                                while ((itemAddH + "").length < 3)
                                    itemAddH = "0" + itemAddH;
                                item2.hierarchyCode = itemAddH;
                                item2.editIndex = true;
                                if (item2.children.length > 0) {
                                    self.updateHierachyWhenInsertItem(item2, itemAddH);
                                }
                            }
                            var editObjs = _.filter(nts.uk.util.flatArray(self.dataSource(), 'children'), function(item:any) { return item.editIndex; });
                            if (editObjs.length > 0) {
                                let currentHis = self.itemHist();
                                for (var k = 0; k < editObjs.length; k++) {
                                    editObjs[k].startDate = currentHis.startDate;
                                    editObjs[k].endDate = currentHis.endDate;
                                    editObjs[k].memo = self.A_INP_MEMO();
                                    editObjs[k].workPlaceCode = editObjs[k].departmentCode;
                                }
                            }
                            self.listDtoUpdateHierachy(editObjs);
                        }
                        let data = self.listDtoUpdateHierachy();
                        if (data != null) {
                            var dfd = $.Deferred();
                            service.upDateListWorkplace(data)
                                .done(function(mess) {
                                    var current = self.findHira(deleteobj.workplaceCode, self.dataSource());
                                    let _dt = nts.uk.util.flatArray(self.dataSource(), 'children');
                                    let selectedcode = "";
                                    let indexOfItemDelete = _.findIndex(_dt, function(o:any) { return o.departmentCode == deleteobj.workplaceCode; });
                                    if (indexOfItemDelete === _dt.length - 1 || current.children.length > 0) {
                                        selectedcode = (_dt[indexOfItemDelete - 1].departmentCode);
                                    } else {
                                        selectedcode = (_dt[indexOfItemDelete + 1].departmentCode);
                                    }
                                    self.getAllWorkPlaceByHistId(hisdto.historyId, selectedcode);
                                }).fail(function(error) { })
                            dfd.resolve();
                            return dfd.promise();
                        }
                    })
                    .fail(function(error) {
                        if (error.messageId == "ER06") {
                            alert("対象データがありません。");
                        }
                    })
                dfd2.resolve();
                return dfd2.promise();
            }).ifNo(function() { });
        }

        findHira(value: string, sources:any) {
            let self = this;

            if (!sources || !sources.length) {
                return undefined;
            }

            sources = nts.uk.util.flatArray(sources, 'children');
            self.lengthTreeCurrent(sources.length + 1);
            return _.find(sources, function(item: model.Dto) { return item.departmentCode == value; });
        }

        findParent(value: string, sources:any) :any{
            let self = this, node:any;

            if (!sources || !sources.length) {
                return undefined;
            }

            sources = nts.uk.util.flatArray(sources, 'children');
            self.lengthTreeCurrent(sources.length + 1);
            return _.find(sources, function(item: model.Dto) { return _.find(item.children, function(child) { return child.departmentCode == value; }); });
        }

        findHist_Dep(items: Array<viewmodel.model.HistoryDto>, newValue: string): viewmodel.model.HistoryDto {
            let self = this;
            let node: viewmodel.model.HistoryDto;
            _.find(items, function(obj: viewmodel.model.HistoryDto) {
                if (!node) {
                    if (obj.startDate == newValue) {
                        node = obj;
                        self.itemHist(node);
                    }
                }
            });
            return node;
        };

        //find history object
        findHist(value: string): viewmodel.model.HistoryDto {
            let self = this;
            var itemModel:any = null;
            _.find(self.itemHistId, function(obj: viewmodel.model.HistoryDto) {
                if (obj.startDate == value) {
                    itemModel = obj;
                }
            })
            return itemModel;
        }

        checkInput(): boolean {
            var self = this;
            if (self.A_INP_CODE() == "") {
                alert("コードが入力されていません。");
                $("#A_INP_CODE").focus();
                return false;
            } else if (self.A_INP_NAME() == "") {
                alert("名称 が入力されていません。");
                $("#A_INP_NAME").focus();
                return false;
            }
            return true
        }


        openCDialog() {
            var self = this;
            if (self.checknull() == "landau") {
                nts.uk.ui.windows.setShared('datanull', "datanull");
                nts.uk.ui.windows.sub.modal('/view/cmm/011/c/index.xhtml', { title: '明細レイアウトの作成＞履歴追加' }).onClosed(function(): any {
                    let itemAddHistory = nts.uk.ui.windows.getShared('itemHistory');
                    if (itemAddHistory) {
                        self.disableBtn();
                        let itemadd = new viewmodel.model.HistoryDto(itemAddHistory.startYearMonth, "9999/12/31", "");
                        self.itemaddHist = itemadd;
                        self.itemHistId().push(self.itemaddHist);
                        self.selectedCodes_His(self.itemaddHist.startDate);
                        self.resetInput();
                        if (itemAddHistory.memo !== null) {
                            self.A_INP_MEMO(itemAddHistory.memo);
                        } else {
                            self.A_INP_MEMO("");
                        }
                    }
                });
            } else {
                if (self.selectedCodes_His() == null)
                    return false;
                nts.uk.ui.windows.setShared('datanull', "notnull");
                nts.uk.ui.windows.setShared('startDateOfHis', self.itemHistId()[0].startDate);
                nts.uk.ui.windows.sub.modal('/view/cmm/011/c/index.xhtml', { title: '明細レイアウトの作成＞履歴追加' }).onClosed(function(): any {
                    let itemAddHistory = nts.uk.ui.windows.getShared('itemHistory');
                    if (itemAddHistory) {
                        self.disableBtn();
                        if (itemAddHistory.checked == true) {
                            let add = new viewmodel.model.HistoryDto(itemAddHistory.startYearMonth, "9999/12/31", "");
                            let arr = self.itemHistId();
                            arr.unshift(add);
                            let startDate = new Date(itemAddHistory.startYearMonth);
                            startDate.setDate(startDate.getDate() - 1);
                            let strStartDate = startDate.getFullYear() + '/' + (startDate.getMonth() + 1) + '/' + startDate.getDate();
                            arr[1].endDate = strStartDate;
                            self.itemHistId(arr);
                            self.selectedCodes_His(itemAddHistory.startYearMonth);
                            self.A_INP_MEMO(itemAddHistory.memo);
                            var _dt = self.dataSource();
                            let hisdto = self.findHist_Dep(self.itemHistId(), self.selectedCodes_His());
                            var _dt2 = _.forEach(nts.uk.util.flatArray(self.dataSource(), 'children'), function(item) {
                                item.historyId = null;
                                item.startDate = hisdto.startDate;
                                item.endDate = hisdto.endDate;
                                item.workPlaceCode = item.departmentCode;
                            });
                            self.checkAddHist1("AddhistoryFromLatest");
                            self.dataSourceToInsert(_dt2);
                        } else {
                            let add = new viewmodel.model.HistoryDto(itemAddHistory.startYearMonth, "9999/12/31", "");
                            let arr = self.itemHistId();
                            arr.unshift(add);
                            //self.itemHistId.unshift(add);
                            let startDate = new Date(itemAddHistory.startYearMonth);
                            startDate.setDate(startDate.getDate() - 1);
                            let strStartDate = startDate.getFullYear() + '/' + (startDate.getMonth() + 1) + '/' + startDate.getDate();
                            arr[1].endDate = strStartDate;
                            self.itemHistId(arr);
                            self.A_INP_MEMO(itemAddHistory.memo);
                            self.selectedCodes_His(self.itemHistId()[0].startDate);
                            self.dataSource(null);
                            self.resetInput();
                            self.checkAddHist1("AddhistoryFromBeggin");
                        }
                    }
                });
            }
        }

        openDDialog() {
            var self = this;
            if (self.selectedCodes_His() == null)
                return false;
            let hisdto:any = self.findHist_Dep(self.itemHistId(), self.selectedCodes_His());
            let index = _.findIndex(self.itemHistId(), function(obj) { return obj == hisdto; });
            hisdto.index = index;
            nts.uk.ui.windows.setShared('itemHist', hisdto);
            nts.uk.ui.windows.sub.modal('/view/cmm/011/d/index.xhtml', { title: '明細レイアウトの作成＞履歴の編集' }).onClosed(function(): any {
                let newstartDate = nts.uk.ui.windows.getShared('newstartDate');
                let isRadiocheck = nts.uk.ui.windows.getShared('isradio');
                if (isRadiocheck == "1") {
                    // delete thang his dau tien + delete memo
                    var dfd = $.Deferred();
                    service.deleteHistory(self.itemHistId()[0].historyId)
                        .done(function() {
                            // cap nhat endate thang sau --> 9999/12/31
                            var dfd = $.Deferred();
                            if (self.itemHistId().length < 2) {
                                location.reload();
                            }
                            service.updateEndDateByHistoryId(self.itemHistId()[1].historyId)
                                .done(function() {
                                    location.reload();
                                })
                                .fail(function() {

                                })
                            dfd.resolve();
                            return dfd.promise();
                        })
                        .fail(function(error) {
                            if (error.messageId == "ER06") {
                                alert("対象データがありません。");
                            }
                        })
                    dfd.resolve();
                    return dfd.promise();

                } else if (isRadiocheck == "2") {
                    let hisdto = self.findHist_Dep(self.itemHistId(), self.selectedCodes_His());
                    let indexItemHist = _.findIndex(self.itemHistId(), function(obj) { return obj == hisdto; });
                    let his2 = "";
                    let endDate = "";
                    let newEndDateRep = "";
                    if (indexItemHist == self.itemHistId().length - 1) {
                        his2 = null;
                    } else {
                        his2 = self.itemHistId()[indexItemHist + 1].historyId;
                        let newEndDate = new Date(newstartDate);
                        newEndDate.setDate(newEndDate.getDate() - 1);
                        newEndDateRep = newEndDate.getFullYear() + '/' + (newEndDate.getMonth() + 1) + '/' + newEndDate.getDate();
                    }
                    let obj = new model.updateDateMY(hisdto.historyId, his2, newstartDate, newEndDateRep);
                    var dfd = $.Deferred();
                    service.upDateStartDateandEndDate(obj)
                        .done(function() {
                            var dfd2 = $.Deferred();
                            service.getAllHistory()
                                .done(function(histories: any) {
                                    self.itemHistId(histories);
                                    self.selectedCodes_His(obj.newStartDate);
                                })
                                .fail(function() { })
                            dfd2.resolve();
                            return dfd2.promise();
                        })
                        .fail(function() { })
                    dfd.resolve();
                    return dfd.promise();
                }
            });
        }


        insertItemUp() {
            var self = this;
            if (self.lengthTreeCurrent() < 889) {
                if (self.numberItemNew() == 0) {
                    var _dt = self.dataSource();
                    var _code = self.singleSelectedCode();
                    var current = self.findHira(_code, _dt);
                    var i = current.hierarchyCode.substr(current.hierarchyCode.length - 3, current.hierarchyCode.length);
                    var hierachyItemadd = (parseInt(i)) + "";
                    while ((hierachyItemadd + "").length < 3)
                        hierachyItemadd = "0" + hierachyItemadd;
                    var parrent = self.findParent(_code, _dt);
                    var newObj = new model.Dto('', "999", "", "",
                        "", "", hierachyItemadd,
                        "情報を登録してください", "情報を登録してください",
                        current.startDate,
                        []);
                    if (parrent) {
                        var index = parrent.children.indexOf(current);
                        //Parent hirachy code
                        var phc = parrent.hierarchyCode;
                        var chc = parseInt(current.hierarchyCode.substr(current.hierarchyCode.length - 3, 3));

                        // Thay đổi hirachiCode của các object bên dưới
                        var changeIndexChild = _.filter(parrent['children'], function(item:any) {
                            return item.hierarchyCode.length == current.hierarchyCode.length && parseInt(item.hierarchyCode.substr(item.hierarchyCode.length - 3, 3)) >= chc;
                        });
                        for (var i in changeIndexChild) {
                            var item1 = changeIndexChild[i];
                            var itemAddH = (parseInt(item1.hierarchyCode.substr(item1.hierarchyCode.length - 3, 3)) + 1) + "";
                            while ((itemAddH + "").length < 3)
                                itemAddH = "0" + itemAddH;
                            item1.hierarchyCode = phc + itemAddH;
                            item1.editIndex = true;
                            if (item1.children.length > 0) {
                                self.updateHierachyWhenInsertItem(item1, phc + itemAddH);
                            }
                        }
                        newObj.hierarchyCode = phc + hierachyItemadd;
                        parrent.children.splice(index, 0, newObj);
                        var editObjs = _.filter(nts.uk.util.flatArray(self.dataSource(), 'children'), function(item:any) { return item.editIndex; });
                        if (editObjs.length > 0) {
                            let currentHis = self.itemHist();
                            for (var k = 0; k < editObjs.length; k++) {
                                editObjs[k].startDate = currentHis.startDate;
                                editObjs[k].endDate = currentHis.endDate;
                                editObjs[k].memo = self.A_INP_MEMO();
                                editObjs[k].workPlaceCode = editObjs[k].departmentCode;
                            }
                        }

                        self.dtoAdd(newObj);
                        self.listDtoUpdateHierachy(editObjs);
                    } else {
                        var index:any = _dt.indexOf(current);
                        //Parent hirachy code
                        var phc:any = current.hierarchyCode;
                        var chc = parseInt(current.hierarchyCode.substr(current.hierarchyCode.length - 3, 3));
                        // Thay đổi hirachiCode của các object bên dưới
                        var changeIndexChild2 = _.filter(_dt, function(item) {
                            return item.hierarchyCode.length == current.hierarchyCode.length && parseInt(item.hierarchyCode.substr(item.hierarchyCode.length - 3, 3)) >= chc;
                        });

                        for (var i in changeIndexChild2) {
                            var item2 = changeIndexChild2[i];
                            var itemAddH = (parseInt(item2.hierarchyCode.substr(item2.hierarchyCode.length - 3, 3)) + 1) + "";
                            while ((itemAddH + "").length < 3)
                                itemAddH = "0" + itemAddH;
                            item2.hierarchyCode = itemAddH;
                            item2.editIndex = true;
                            if (item2.children.length > 0) {
                                self.updateHierachyWhenInsertItem(item2, itemAddH);
                            }
                        }
                        newObj.hierarchyCode = hierachyItemadd;
                        var editObjs = _.filter(nts.uk.util.flatArray(self.dataSource(), 'children'), function(item:any) { return item.editIndex; });
                        if (editObjs.length > 0) {
                            let currentHis = self.itemHist();
                            for (var k = 0; k < editObjs.length; k++) {
                                editObjs[k].startDate = currentHis.startDate;
                                editObjs[k].endDate = currentHis.endDate;
                                editObjs[k].memo = self.A_INP_MEMO();
                                editObjs[k].workPlaceCode = editObjs[k].departmentCode;
                            }
                        }
                        self.dtoAdd(newObj);
                        self.listDtoUpdateHierachy(editObjs);
                        _dt.splice(index, 0, newObj);
                    }
                    self.dataSource(_dt);
                    self.numberItemNew(1);
                    self.singleSelectedCode(newObj.departmentCode);
                    self.resetInput();
                    self.disableBtn();
                } else if (self.numberItemNew() == 1) {
                    $("#A_INP_CODE").focus();
                    self.disableBtn();
                }
            } else {
                alert("maximum 889 item");
            }

        }

        resetInput() {
            var self = this;
            self.A_INP_CODE("");
            self.A_INP_CODE_enable(true);
            self.A_INP_NAME("");
            self.A_INP_FULLNAME("");
            self.A_INP_OUTCODE("");
            $("#A_INP_CODE").focus();
        }

        // update hierachy when click button up down
        updateHierachyWhenclickUpdownBtn(item: any) {
            var self = this;
            let hisdto = self.findHist_Dep(self.itemHistId(), self.selectedCodes_His());
            for (var i in item.children) {
                var itemCon = item.children[i];
                var j:any = parseInt(i) + 1;
                while ((j + "").length < 3)
                    j = "0" + j;
                itemCon.hierarchyCode = item.hierarchyCode.substr(0, item.hierarchyCode.length) + j;
                itemCon.startDate = hisdto.startDate;
                itemCon.endDate = hisdto.endDate;
                itemCon.workPlaceCode = itemCon.departmentCode;
                if (self.arrayItemEdit().length > 0) {
                    let _dt2 = self.arrayItemEdit();
                    var isDuplicateItem = _.filter(_dt2, function(item1) {
                        return item1.departmentCode == itemCon.departmentCode;
                    });
                    if (isDuplicateItem.length > 0) {
                        // xóa isDuplicateItem
                        _dt2 = jQuery.grep(_dt2, function(value) {
                            return value.departmentCode != isDuplicateItem[0].departmentCode;
                        });

                        self.arrayItemEdit(_dt2);
                        self.arrayItemEdit().push(itemCon);
                    } else {
                        self.arrayItemEdit().push(itemCon);
                    }
                } else {
                    self.arrayItemEdit().push(itemCon);
                }
                if (itemCon.children.length > 0) {
                    self.updateHierachyWhenclickUpdownBtn(itemCon);
                }
            }
        }
        
        // update Hierachy when insert item to tree
        updateHierachyWhenInsertItem(item: any, hierarchyCode: any) {
            var self = this;
            for (var i in item.children) {
                var con = item.children[i];
                var hierachy = con.hierarchyCode.substr(0, hierarchyCode.length);
                var ii = con.hierarchyCode.replace(hierachy, hierarchyCode);
                con.hierarchyCode = ii;
                con.editIndex = true;
                if (con.children.length > 0) {
                    self.updateHierachyWhenInsertItem(con, hierarchyCode);
                }
            }
        }

        insertItemDown() {
            var self = this;
            if (self.lengthTreeCurrent() < 889) {
                if (self.numberItemNew() == 0) {
                    var _dt = self.dataSource();
                    var _code = self.singleSelectedCode();
                    var current = self.findHira(_code, _dt);
                    var i = current.hierarchyCode.substr(current.hierarchyCode.length - 3, current.hierarchyCode.length);
                    var hierachyItemadd = (parseInt(i) + 1) + "";
                    while ((hierachyItemadd + "").length < 3)
                        hierachyItemadd = "0" + hierachyItemadd;
                    var parrent = self.findParent(_code, _dt);
                    var newObj = new model.Dto('', "999", "", "",
                        "", "", hierachyItemadd,
                        "情報を登録してください", "情報を登録してください",
                        current.startDate,
                        []);
                    if (parrent) {
                        var index = parrent.children.indexOf(current);
                        //Parent hirachy code
                        var phc = parrent.hierarchyCode;
                        var chc = parseInt(current.hierarchyCode.substr(current.hierarchyCode.length - 3, 3));
                        // Thay đổi hirachiCode của các object bên dưới
                        var changeIndexChild = _.filter(parrent['children'], function(item:any) {
                            return item.hierarchyCode.length == current.hierarchyCode.length && parseInt(item.hierarchyCode.substr(item.hierarchyCode.length - 3, 3)) > chc;
                        });
                        for (var i in changeIndexChild) {
                            var item = changeIndexChild[i];
                            var itemAddH = (parseInt(item.hierarchyCode.substr(item.hierarchyCode.length - 3, 3)) + 1) + "";
                            while ((itemAddH + "").length < 3)
                                itemAddH = "0" + itemAddH;
                            item.hierarchyCode = phc + itemAddH;
                            item.editIndex = true;
                            if (item.children.length > 0) {
                                self.updateHierachyWhenInsertItem(item, phc + itemAddH);
                            }
                        }
                        newObj.hierarchyCode = phc + hierachyItemadd;
                        parrent.children.splice(index + 1, 0, newObj);
                        var editObjs = _.filter(nts.uk.util.flatArray(self.dataSource(), 'children'), function(item:any) { return item.editIndex; });
                        if (editObjs.length > 0) {
                            let currentHis = self.itemHist();
                            for (var k = 0; k < editObjs.length; k++) {
                                editObjs[k].startDate = currentHis.startDate;
                                editObjs[k].endDate = currentHis.endDate;
                                editObjs[k].memo = self.A_INP_MEMO();
                                editObjs[k].workPlaceCode = editObjs[k].departmentCode;
                            }
                        }
                        self.dtoAdd(newObj);
                        if (editObjs.length > 0) {
                            self.listDtoUpdateHierachy(editObjs);
                        } else {
                            self.listDtoUpdateHierachy();
                        }
                    }
                    else {
                        var index:any = _dt.indexOf(current);
                        //Parent hirachy code
                        var phc:any = current.hierarchyCode;
                        var chc = parseInt(current.hierarchyCode.substr(current.hierarchyCode.length - 3, 3));
                        // Thay đổi hirachiCode của các object bên dưới
                        var changeIndexChild2 = _.filter(_dt, function(item) {
                            return item.hierarchyCode.length == current.hierarchyCode.length && parseInt(item.hierarchyCode.substr(item.hierarchyCode.length - 3, 3)) > chc;
                        });
                        for (var i in changeIndexChild2) {
                            var item = changeIndexChild2[i];
                            var itemAddH = (parseInt(item.hierarchyCode.substr(item.hierarchyCode.length - 3, 3)) + 1) + "";
                            while ((itemAddH + "").length < 3)
                                itemAddH = "0" + itemAddH;
                            item.hierarchyCode = itemAddH;
                            item.editIndex = true;
                            if (item.children.length > 0) {
                                self.updateHierachyWhenInsertItem(item, itemAddH);
                            }
                        }
                        newObj.hierarchyCode = hierachyItemadd;
                        var editObjs = _.filter(nts.uk.util.flatArray(self.dataSource(), 'children'), function(item:any) { return item.editIndex; });
                        if (editObjs.length > 0) {
                            let currentHis = self.itemHist();
                            for (var k = 0; k < editObjs.length; k++) {
                                editObjs[k].startDate = currentHis.startDate;
                                editObjs[k].endDate = currentHis.endDate;
                                editObjs[k].memo = self.A_INP_MEMO();
                                editObjs[k].workPlaceCode = editObjs[k].departmentCode;
                            }
                        }
                        self.dtoAdd(newObj);
                        if (editObjs.length > 0) {
                            self.listDtoUpdateHierachy(editObjs);
                        } else {
                            self.listDtoUpdateHierachy();
                        }
                        _dt.splice(index + 1, 0, newObj);
                    }
                    self.dataSource(_dt);
                    self.numberItemNew(1);
                    self.singleSelectedCode(newObj.departmentCode);
                    self.resetInput();
                    self.disableBtn();
                } else if (self.numberItemNew() == 1) {
                    $("#A_INP_CODE").focus();
                    self.disableBtn();
                } else {
                    alert("maximum 889 item");
                }
            }
        }
        insertItemEnd() {
            var self = this;
            if (self.lengthTreeCurrent() < 889) {
                if (self.numberItemNew() == 0) {
                    var _dt = self.dataSource();
                    var _code = self.singleSelectedCode();
                    var current = self.findHira(_code, _dt);
                    if (current.hierarchyCode.length < 30) {
                        var hierachy_current = current.hierarchyCode;
                        var length = current.children.length + 1;
                        var hierachyItemadd = length + "";
                        while ((hierachyItemadd + "").length < 3)
                            hierachyItemadd = "0" + hierachyItemadd;
                        var newObj:any = new model.Dto('', "999", "", "",
                            "", "", hierachy_current + hierachyItemadd,
                            "情報を登録してください", "情報を登録してください",
                            current.startDate,
                            []);
                        current.children.push(newObj);
                        let currentHis = self.itemHist();
                        newObj.startDate = currentHis.startDate;
                        newObj.endDate = currentHis.endDate;
                        newObj.memo = self.A_INP_MEMO();
                        self.dtoAdd(newObj);
                        self.listDtoUpdateHierachy();
                        self.dataSource(_dt);
                        self.numberItemNew(1);
                        self.singleSelectedCode(newObj.departmentCode);
                        self.disableBtn();
                    } else {
                        alert("hierarchy item current is 10 ,not push item child to tree");
                    }
                } else if (self.numberItemNew() == 1) {
                    $("#A_INP_CODE").focus();
                    self.disableBtn();
                }
            } else {
                alert("maximum 889 item");
            }
        }


        start(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred<any>();
            // get init data workplace
            service.getAllWorkplace().done(function(workplaceQueryResult: viewmodel.model.WorkPlaceQueryResult) {
                var workplaceQueryResult = workplaceQueryResult;
                if (workplaceQueryResult.histories == null) {
                    nts.uk.ui.windows.setShared('datanull', "datanull");
                    self.checknull("landau");
                    self.openCDialog();
                } else {
                    if (workplaceQueryResult.workPlaces.length > 0) {
                        self.dataSource(workplaceQueryResult.workPlaces);
                    }
                    if (workplaceQueryResult.memo) {
                        self.A_INP_MEMO(workplaceQueryResult.memo.memo);
                    }
                    if (workplaceQueryResult.histories.length > 0) {
                        self.itemHistId(workplaceQueryResult.histories);
                        if (self.dataSource().length > 0) {
                            self.dataSourceFlat = ko.observableArray(nts.uk.util.flatArray(self.dataSource(), "children"));
                            self.singleSelectedCode(workplaceQueryResult.workPlaces[0].departmentCode);
                            self.selectedCodes_His(self.itemHistId()[0].startDate);
                            self.numberItemNew(0);
                        }
                    }
                }
                dfd.resolve();
            }).fail(function(error) {
            })
            dfd.resolve();
            return dfd.promise();
        }
    }
    /**
          * Model namespace.
       */
    export module model {

        export class DepartmentQueryResult {
            histories: Array<HistoryDto>;
            departments: Array<Dto>;
            memo: MemoDto;
        }

        export class WorkPlaceQueryResult {
            histories: Array<HistoryDto>;
            workPlaces: Array<DtoWKP>;
            memo: MemoDto;
        }

        export class MemoDto {
            historyId: string;
            memo: string;
        }

        export class HistoryDto {
            endDate: string;
            historyId: string;
            startDate: string;
            constructor(startDate: string, endDate: string, historyId: string) {
                var self = this;
                self.endDate = endDate;
                self.startDate = startDate;
                self.historyId = historyId;
            }
        }
        //    Department     
        export class DtoWKP {
            companyCode: string;
            departmentCode: string;
            historyId: string;
            endDate: string;
            externalCode: string;
            fullName: string;
            hierarchyCode: string;
            name: string;
            parentChildAttribute1: string;
            parentChildAttribute2: string;
            parentWorkCode1: string;
            parentWorkCode2: string;
            shortName: string;
            startDate: string;
            children: Array<Dto>;
            constructor(companyCode: string,
                departmentCode: string,
                historyId: string,
                endDate: string,
                externalCode: string,
                fullName: string,
                hierarchyCode: string,
                name: string,
                parentChildAttribute1: string,
                parentChildAttribute2: string,
                parentWorkCode1: string,
                parentWorkCode2: string,
                shortName: string,
                startDate: string,
                children: Array<Dto>
            ) {
                var self = this;
                self.companyCode = companyCode;
                self.departmentCode = departmentCode;
                self.historyId = historyId;
                self.endDate = endDate;
                self.externalCode = externalCode;
                self.fullName = fullName;
                self.hierarchyCode = hierarchyCode;
                self.name = name;
                self.parentChildAttribute1 = parentChildAttribute1;
                self.parentChildAttribute2 = parentChildAttribute2;
                self.parentWorkCode1 = parentWorkCode1;
                self.parentWorkCode2 = parentWorkCode2;
                self.shortName = shortName;
                self.startDate = startDate;
                self.children = children;
            }
        }

        // WOrkPLace Dto
        export class Dto {
            companyCode: string;
            departmentCode: string;
            historyId: string;
            endDate: string;
            externalCode: string;
            fullName: string;
            hierarchyCode: string;
            name: string;
            display: string;
            startDate: string;
            children: Array<Dto>;
            constructor(companyCode: string,
                departmentCode: string,
                historyId: string,
                endDate: string,
                externalCode: string,
                fullName: string,
                hierarchyCode: string,
                name: string,
                display: string,
                startDate: string,
                children: Array<Dto>
            ) {
                var self = this;
                self.companyCode = companyCode;
                self.departmentCode = departmentCode;
                self.historyId = historyId;
                self.endDate = endDate;
                self.externalCode = externalCode;
                self.fullName = fullName;
                self.hierarchyCode = hierarchyCode;
                self.name = name;
                self.display = display;
                self.startDate = startDate;
                self.children = children;
            }
        }
        export class AddWorkplaceDto {
            workPlaceCode: string;
            historyId: string;
            endDate: string;
            externalCode: string;
            fullName: string;
            hierarchyCode: string;
            name: string;
            startDate: string;
            memo: string;
            shortName: string;
            parentChildAttribute1: string;
            parentChildAttribute2: string;
            parentWorkCode1: string;
            parentWorkCode2: string;
            children: Array<AddWorkplaceDto>;
            constructor(
                workPlaceCode: string,
                historyId: string,
                endDate: string,
                externalCode: string,
                fullName: string,
                hierarchyCode: string,
                name: string,
                startDate: string,
                memo: string,
                shortName: string,
                parentChildAttribute1: string,
                parentChildAttribute2: string,
                parentWorkCode1: string,
                parentWorkCode2: string,
                children: Array<AddWorkplaceDto>) {
                var self = this;
                self.memo = memo;
                self.workPlaceCode = workPlaceCode;
                self.historyId = historyId;
                self.endDate = endDate;
                self.externalCode = externalCode;
                self.fullName = fullName;
                self.hierarchyCode = hierarchyCode;
                self.name = name;
                self.shortName = shortName;
                self.startDate = startDate;
                self.parentChildAttribute1 = parentChildAttribute1;
                self.parentChildAttribute2 = parentChildAttribute2;
                self.parentWorkCode1 = parentWorkCode1;
                self.parentWorkCode2 = parentWorkCode2;
                self.children = children;
            }
        }

        export class WorkPlaceDeleteDto {
            workplaceCode: string;
            hierarchyCode: string;
            historyId: string;
            constructor(workplaceCode: string, historyId: string, hierarchyCode: string) {
                var self = this;
                self.workplaceCode = workplaceCode;
                self.hierarchyCode = hierarchyCode;
                self.historyId = historyId;
            }
        }

        export class updateDateMY {
            historyId1: string;
            historyId2: string;
            newStartDate: string;
            newEndDate: string;
            constructor(historyId1: string, historyId2: string, newStartDate: string, newEndDate: string) {
                var self = this;
                self.historyId1 = historyId1;
                self.historyId2 = historyId2;
                self.newStartDate = newStartDate;
                self.newEndDate = newEndDate;
            }
        }

    }

}
