module nts.uk.com.view.cmf002.d.viewmodel {
    import close = nts.uk.ui.windows.close;
    import getText = nts.uk.resource.getText;
    import model = cmf002.share.model;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import block = nts.uk.ui.block;
    import alertError = nts.uk.ui.dialog.alertError;
    import shareModel = cmf002.share.model;
    import info = nts.uk.ui.dialog.info;
    import validation = nts.uk.ui.validation;

    export class ScreenModel {
        selectedSearchTable: KnockoutObservable<any> = ko.observable(null);
        tableItemList: KnockoutObservableArray<TableItem> = ko.observableArray([]);
        selectedTable: KnockoutObservable<any> = ko.observable(null);

        selectedSearchItem: KnockoutObservable<any> = ko.observable(null);
        itemList: KnockoutObservableArray<CtgItemDataDto> = ko.observableArray([]);
        selectedItem: KnockoutObservable<any> = ko.observable(null);

        ctgItemDataList: KnockoutObservableArray<CtgItemDataDto> = ko.observableArray([]);
        cndDetai: KnockoutObservable<OutCndDetailDto> = ko.observable(null);

        selectedSeriNum: KnockoutObservable<any> = ko.observable(null);

        // declare var of params screen B
        categoryName: string = '';
        categoryId: string = '';
        cndSetCd: string = '';
        cndSetName: string = '';

        standardAtr: shareModel.STANDARD_ATR;
        registerMode: shareModel.SCREEN_MODE;

        /**
        * Constructor.
        */
        constructor() {
            let self = this;
            self.standardAtr = shareModel.STANDARD_ATR.STANDARD;
            self.registerMode = shareModel.SCREEN_MODE.NEW
            self.selectedTable.subscribe(table => {
                let items = _.filter(self.ctgItemDataList(), { "tableName": table });
                self.itemList(items);
                self.focusFirstRowD5_4();
                $('#D5_4').focus();
            })
        }

        startPage(): JQueryPromise<any> {
            let self = this;
            var dfd = $.Deferred();
            block.invisible();
            // get data from screen B
            let params = getShared('CMF002_D_PARAMS');
            if (params) {
                self.categoryName = params.categoryName;
                self.categoryId = params.categoryId;
                self.cndSetCd = params.cndSetCd;
                self.cndSetName = params.cndSetName;
                service.getListCtgItems(params.cndSetCd, params.categoryId).done(res => {
                    //get data return from server
                    let outputItemList: CtgItemDataCndDetailDto = res;
                    if (res.cndDetai == null) {
                        self.registerMode = shareModel.SCREEN_MODE.NEW
                        self.cndDetai(new OutCndDetailDto(self.cndSetCd, "", []));
                    } else {
                        self.registerMode = shareModel.SCREEN_MODE.UPDATE
                        self.cndDetai(OutCndDetailDto.fromApp(res.cndDetai));
                    }
                    self.ctgItemDataList(res.ctgItemDataList);
                    self.loadDetaiItemGrid();
                    $('#D5_2').focus();
                    block.clear();
                    dfd.resolve();
                }).fail(res => {
                    alertError(res);
                    block.clear();
                    dfd.reject();
                });
            }
            return dfd.promise();
        }

        setTableItemList() {
            let self = this;
            self.tableItemList.removeAll();
            let tableUniq = _.uniqBy(self.ctgItemDataList(), 'tableName');
            _.each(tableUniq, item => {
                self.tableItemList.push(new TableItem(item.tableName, item.displayTableName));
            })
            self.focusFirstRowD5_2();
        }

        focusFirstRowD5_2() {
            let self = this;
            if (_.isEmpty(self.tableItemList())) {
                self.selectedTable(null);
            } else {
                self.selectedTable(_.head(self.tableItemList()).tableName);
            }
        }

        focusFirstRowD5_4() {
            let self = this;
            if (_.isEmpty(self.itemList())) {
                self.selectedItem(null);
            } else {
                self.selectedItem(_.head(self.itemList()).itemNo);
            }
        }

        setDataInitDetailItem() {
            let self = this;
            _.each(self.cndDetai().listOutCndDetailItem(), function(item: OutCndDetailItemDto) {
                let ctgItem: CtgItemDataDto = self.getCtgItem(item.categoryItemNo());
                if (ctgItem) {
                    item.dataType = ctgItem.dataType;
                    item.subscribeCondBetween(item.conditionSymbol());
                    item.searchValueCd = ctgItem.searchValueCd;
                    item.switchView(OutCndDetailItemDto.getSwitchView(item.dataType, item.conditionSymbol()));
                    item.clearData();
                } else {
                    item.dataType = null;
                }
            })
        }

        loadDetaiItemGrid() {
            let self = this;
            self.setTableItemList();
            self.setDataInitDetailItem();
            $("#fixed-table").ntsFixedTable({ height: 328 });
        }

        getCtgItem(itemNo) {
            let self = this;
            return _.find(self.ctgItemDataList(), { 'itemNo': itemNo });
        }

        getItemName(itemNo) {
            let self = this;
            let item = self.getCtgItem(itemNo);
            if (item) {
                return item.itemName;
            }
            return "";
        }

        getComboboxSource(searchValueCd, dataType) {
            let self = this;
            if (dataType != null) {
                return shareModel.getConditonSymbol(searchValueCd, dataType);
            }
            return [];
        }

        register() {
            let self = this;
            block.invisible();
            _.each(self.cndDetai().listOutCndDetailItem(), function(item: OutCndDetailItemDto) {
                item.validate();
            })
            if (nts.uk.ui.errors.hasError()) {
                block.clear();
                return;
            }

            _.each(self.cndDetai().listOutCndDetailItem(), function(item: OutCndDetailItemDto) {
                let listSearchCodeList = [];
                if (item.switchView() != SWITCH_VIEW.SEARCH_CODE_LIST) return;
                let listSearchCode = _.split(item.joinedSearchCodeList(), ',');
                _.each(listSearchCode, searchCode => {
                    let newSearchCode: SearchCodeListDto = new SearchCodeListDto(item.conditionSettingCd(), item.categoryId(),
                        item.categoryItemNo(), item.seriNum(), _.trim(searchCode), self.getItemName(item.categoryItemNo()));
                    listSearchCodeList.push(newSearchCode);
                })
                item.listSearchCodeList = listSearchCodeList
            })
            let command: OutCndDetailInfoCommand = new OutCndDetailInfoCommand(OutCndDetailCommand.fromDto(self.cndDetai()),
                self.standardAtr, self.registerMode);
            service.register(command).done(() => {
                info({ messageId: "Msg_15" }).then(() => {
                    self.focusFirstRowD5_2();
                    $('#D5_2').focus();
                });
            }).fail(res => {
                alertError(res);
            }).always(() => {
                block.clear();
            });
        }

        //終了する
        closeDialog() {
            close();
        }

        btnRightClick() {
            let self = this;
            if (self.selectedItem() == null) {
                return;
            }
            let seriNum = 1;
            let item = self.getCtgItem(parseInt(self.selectedItem()));
            let itemDetail = _.maxBy(ko.toJS(self.cndDetai().listOutCndDetailItem()), 'seriNum');
            if (itemDetail) {
                seriNum = itemDetail.seriNum + 1;
            }
            let newItemDetail = new OutCndDetailItemDto(self.categoryId, item.itemNo, seriNum,
                self.cndSetCd, null);
            newItemDetail.dataType = item.dataType;
            newItemDetail.subscribeCondBetween(newItemDetail.conditionSymbol());
            newItemDetail.searchValueCd = item.searchValueCd;
            self.cndDetai().listOutCndDetailItem.push(newItemDetail);
        }

        btnLeftClick() {
            let self = this;
            if (self.selectedSeriNum() == null) {
                return;
            }
            self.cndDetai().listOutCndDetailItem.remove(function(item: OutCndDetailItemDto) {
                if (item.seriNum() == self.selectedSeriNum()) {
                    item.removeValidate();
                    return true;
                }
                return false;
            });
            self.selectedSeriNum(null);
        }

        selectDetailItem(seriNum) {
            let self = this;
            self.selectedSeriNum(seriNum);
            $("#fixed-table tr").removeClass("my-active-row");
            $("#fixed-table tr[data-id='" + seriNum + "']").addClass("my-active-row");
        }
    }

    class TableItem {
        tableName: string;
        displayTableName: string;
        constructor(tableName: string, displayTableName: string) {
            this.tableName = tableName;
            this.displayTableName = displayTableName;
        }
    }

    class CtgItemDataCndDetailDto {
        ctgItemDataList: Array<CtgItemDataDto>;
        cndDetai: OutCndDetailDto;

        constructor(ctgItemDataList: Array<CtgItemDataDto>, cndDetaiList: OutCndDetailDto) {
            this.ctgItemDataList = ctgItemDataList;
            this.cndDetai = cndDetaiList;
        }
    }
    class CtgItemDataDto {
        categoryId: number;
        itemNo: number;
        tableName: string;
        displayTableName: string;
        itemName: string;
        dataType: shareModel.ITEM_TYPE;
        searchValueCd: string;

        constructor(categoryId: number, itemNo: number,
            tableName: string, displayTableName: string, itemName: string,
            dataType: number, searchValueCd: string) {
            this.categoryId = categoryId;
            this.itemNo = itemNo;
            this.tableName = tableName;
            this.displayTableName = displayTableName;
            this.itemName = itemName;
            this.dataType = dataType;
            this.searchValueCd = searchValueCd;
        }
    }

    class OutCndDetailDto {
        conditionSettingCd: string;
        exterOutCdnSql: string;
        listOutCndDetailItem: KnockoutObservableArray<OutCndDetailItemDto>;
        constructor(conditionSettingCd: string,
            exterOutCdnSql: string, listOutCndDetailItem: Array<OutCndDetailItemDto>) {
            this.conditionSettingCd = conditionSettingCd;
            this.exterOutCdnSql = exterOutCdnSql;
            this.listOutCndDetailItem = ko.observableArray(listOutCndDetailItem);
        }

        static fromApp(app) {
            let listOutCndDetailItem = [];
            _.each(app.listOutCndDetailItem, item => {
                listOutCndDetailItem.push(OutCndDetailItemDto.fromApp(item));
            })
            return new OutCndDetailDto(app.conditionSettingCd, app.exterOutCdnSql, listOutCndDetailItem);
        }
    }
    class OutCndDetailItemDto {
        categoryId: KnockoutObservable<string>;
        categoryItemNo: KnockoutObservable<number>;
        seriNum: KnockoutObservable<number>;
        conditionSettingCd: KnockoutObservable<string>;
        conditionSymbol: KnockoutObservable<number>;
        searchNum: KnockoutObservable<number>;
        searchNumEndVal: KnockoutObservable<number>;
        searchNumStartVal: KnockoutObservable<number>;
        searchChar: KnockoutObservable<string>;
        searchCharEndVal: KnockoutObservable<string>;
        searchCharStartVal: KnockoutObservable<string>;
        searchDate: KnockoutObservable<string>;
        searchDateEnd: KnockoutObservable<string>;
        searchDateStart: KnockoutObservable<string>;
        searchClock: KnockoutObservable<number>;
        searchClockEndVal: KnockoutObservable<number>;
        searchClockStartVal: KnockoutObservable<number>;
        searchTime: KnockoutObservable<number>;
        searchTimeEndVal: KnockoutObservable<number>;
        searchTimeStartVal: KnockoutObservable<number>;
        listSearchCodeList: Array<SearchCodeListDto>;
        joinedSearchCodeList: KnockoutObservable<string>;

        itemName: string;
        dataType: shareModel.ITEM_TYPE;
        searchValueCd: string;
        switchView: KnockoutObservable<SWITCH_VIEW>;

        charValidator = new validation.StringValidator("", "OutCndCharVal", { required: true });
        numberValidator = new validation.NumberValidator("", "OutCndNumVal", { required: true });
        timeValidator = new validation.TimeValidator("", "AttendanceTime", { required: true, valueType: "Clock", inputFormat: "hh:mm", outputFormat: "time", mode: "time" });
        clockValidator = new validation.TimeValidator("", "AttendanceClock", { required: true, valueType: "TimeWithDay", inputFormat: "hh:mm", outputFormat: "time", mode: "time" });
        searchCdValidator = new validation.StringValidator("", "ExtOutCndSearchCd", { required: true });

        subCharStart: any;
        subCharEnd: any;
        subNumStart: any;
        subNumEnd: any;
        subDateStart: any;
        subDateEnd: any;
        subTimeStart: any;
        subTimeEnd: any;
        subClockStart: any;
        subClockEnd: any;
        
        constructor(categoryId: string, categoryItemNo: number, seriNum: number,
            conditionSettingCd: string, conditionSymbol: number) {
            let self = this;
            self.categoryId = ko.observable(categoryId);
            self.categoryItemNo = ko.observable(categoryItemNo);
            self.seriNum = ko.observable(seriNum);
            self.conditionSettingCd = ko.observable(conditionSettingCd);
            self.conditionSymbol = ko.observable(conditionSymbol);
            self.searchNum = ko.observable(null);
            self.searchNumEndVal = ko.observable(null);
            self.searchNumStartVal = ko.observable(null);
            self.searchChar = ko.observable(null);
            self.searchCharEndVal = ko.observable(null);
            self.searchCharStartVal = ko.observable(null);
            self.searchDate = ko.observable(null);
            self.searchDateEnd = ko.observable(null);
            self.searchDateStart = ko.observable(null);
            self.searchClock = ko.observable(null);
            self.searchClockEndVal = ko.observable(null);
            self.searchClockStartVal = ko.observable(null);
            self.searchTime = ko.observable(null);
            self.searchTimeEndVal = ko.observable(null);
            self.searchTimeStartVal = ko.observable(null);
            self.listSearchCodeList = [];
            self.joinedSearchCodeList = ko.observable(null);

            self.switchView = ko.observable(SWITCH_VIEW.NONE);
            self.searchValueCd = "";
            self.conditionSymbol.subscribe(condSymbol => {
                self.switchView(OutCndDetailItemDto.getSwitchView(self.dataType, condSymbol));
                self.subscribeCondBetween(condSymbol);
            })
            self.conditionSymbol.subscribe(condSymbol => {
                self.disposeCondBetween(condSymbol);
            }, null, "beforeChange")
            self.switchView.subscribe(condSymbol => {
                self.clearData();
            })
        }

        subscribeCondBetween(condSymbol) {
            let self = this;
            if (shareModel.CONDITION_SYMBOL.BETWEEN != condSymbol) {
                return;
            }
            switch (self.dataType) {
                case shareModel.ITEM_TYPE.CHARACTER:
                    self.subCharStart = self.searchCharStartVal.subscribe(value => {
                        this.clearError("D6_C4_3");
                    })
                    self.subCharEnd = self.searchCharEndVal.subscribe(value => {
                        this.clearError("D6_C4_3");
                    })
                    break;
                case shareModel.ITEM_TYPE.NUMERIC:
                    self.subNumStart = self.searchNumStartVal.subscribe(value => {
                        this.clearError("D6_C4_6");
                    })
                    self.subNumEnd = self.searchNumEndVal.subscribe(value => {
                        this.clearError("D6_C4_6");
                    })
                    break;
                case shareModel.ITEM_TYPE.DATE:
                    self.subDateStart = self.searchDateStart.subscribe(value => {
                        this.clearError("D6_C4_9");
                    })
                    self.subDateEnd = self.searchDateEnd.subscribe(value => {
                        this.clearError("D6_C4_9");
                    })
                    break;
                case shareModel.ITEM_TYPE.TIME:
                    self.subTimeStart = self.searchTimeStartVal.subscribe(value => {
                        this.clearError("D6_C4_12");
                    })
                    self.subTimeEnd = self.searchTimeEndVal.subscribe(value => {
                        this.clearError("D6_C4_12");
                    })
                    break;
                case shareModel.ITEM_TYPE.INS_TIME:
                    self.subClockStart = self.searchClockStartVal.subscribe(value => {
                        this.clearError("D6_C4_15");
                    })
                    self.subClockEnd = self.searchClockEndVal.subscribe(value => {
                        this.clearError("D6_C4_15");
                    })
                    break;
            }
        }

        disposeCondBetween(condSymbol) {
            let self = this;
            if (shareModel.CONDITION_SYMBOL.BETWEEN != condSymbol) {
                return;
            }
            switch (self.dataType) {
                case shareModel.ITEM_TYPE.CHARACTER:
                    self.subCharStart.dispose();
                    self.subCharEnd.dispose();
                    break;
                case shareModel.ITEM_TYPE.NUMERIC:
                    self.subNumStart.dispose();
                    self.subNumEnd.dispose();
                    break;
                case shareModel.ITEM_TYPE.DATE:
                    self.subDateStart.dispose();
                    self.subDateEnd.dispose();
                    break;
                case shareModel.ITEM_TYPE.TIME:
                    self.subTimeStart.dispose();
                    self.subTimeEnd.dispose();
                    break;
                case shareModel.ITEM_TYPE.INS_TIME:
                    self.subClockStart.dispose();
                    self.subClockEnd.dispose();
                    break;
            }
        }

        clearError(control) {
            let self = this;
            $("#fixed-table tr[data-id='" + self.seriNum() + "'] ." + control).ntsError('clear');
        }

        checkError(control) {
            let self = this;
            $("#fixed-table tr[data-id='" + self.seriNum() + "'] ." + control).ntsError('check');
        }

        setError(control, messageId) {
            let self = this;
            $("#fixed-table tr[data-id='" + self.seriNum() + "'] ." + control).ntsError('set', { messageId: messageId });
        }

        setErrorCompare(control1, control2) {
            let self = this;
            $("#fixed-table tr[data-id='" + self.seriNum() + "'] ." + control2)
                .ntsError('set', { messageId: 'Msg_1401', messageParams: [getText(self.getControlName(control1)), getText(self.getControlName(control2))] });
        }

        getControlName(control) {
            switch (control) {
                case 'D6_C4_1': return 'CMF002_106';
                case 'D6_C4_2': return 'CMF002_107';
                case 'D6_C4_3': return 'CMF002_108';
                case 'D6_C4_4': return 'CMF002_106';
                case 'D6_C4_5': return 'CMF002_107';
                case 'D6_C4_6': return 'CMF002_108';
                case 'D6_C4_7': return 'CMF002_106';
                case 'D6_C4_8': return 'CMF002_106';
                case 'D6_C4_9': return 'CMF002_106';
                case 'D6_C4_10': return 'CMF002_106';
                case 'D6_C4_11': return 'CMF002_107';
                case 'D6_C4_12': return 'CMF002_108';
                case 'D6_C4_13': return 'CMF002_106';
                case 'D6_C4_14': return 'CMF002_107';
                case 'D6_C4_15': return 'CMF002_108';
                case 'D6_C4_16': return 'CMF002_355';
            }
        }

        clearData() {
            let self = this;
            // 文字型
            if (self.switchView() != SWITCH_VIEW.CHARACTER_NORMAL) {
                self.searchChar(null);
                self.clearError("D6_C4_1");
            }
            if (self.switchView() != SWITCH_VIEW.CHARACTER_PERIOD) {
                self.searchCharStartVal(null);
                self.searchCharEndVal(null);
                self.clearError("D6_C4_2");
                self.clearError("D6_C4_3");
            }

            // 数値型
            if (self.switchView() != SWITCH_VIEW.NUMERIC_NORMAL) {
                self.searchNum(null);
                self.clearError("D6_C4_4");
            }
            if (self.switchView() != SWITCH_VIEW.NUMERIC_PERIOD) {
                self.searchNumStartVal(null);
                self.searchNumEndVal(null);
                self.clearError("D6_C4_5");
                self.clearError("D6_C4_6");
            }

            // 日付型
            if (self.switchView() != SWITCH_VIEW.DATE_NORMAL) {
                self.searchDate(null);
                self.clearError("D6_C4_7");
            }
            if (self.switchView() != SWITCH_VIEW.DATE_PERIOD) {
                self.searchDateStart(null);
                self.searchDateEnd(null);
                self.clearError("D6_C4_8");
                self.clearError("D6_C4_9");
            }

            // 時間型
            if (self.switchView() != SWITCH_VIEW.TIME_NORMAL) {
                self.searchTime(null);
                self.clearError("D6_C4_10");
            }
            if (self.switchView() != SWITCH_VIEW.TIME_PERIOD) {
                self.searchTimeStartVal(null);
                self.searchTimeEndVal(null);
                self.clearError("D6_C4_11");
                self.clearError("D6_C4_12");
            }

            // 時刻型
            if (self.switchView() != SWITCH_VIEW.INS_TIME_NORMAL) {
                self.searchClock(null);
                self.clearError("D6_C4_13");
            }
            if (self.switchView() != SWITCH_VIEW.INS_TIME_PERIOD) {
                self.searchClockStartVal(null);
                self.searchClockEndVal(null);
                self.clearError("D6_C4_14");
                self.clearError("D6_C4_15");
            }

            if (self.switchView() != SWITCH_VIEW.SEARCH_CODE_LIST) {
                self.joinedSearchCodeList(null);
                self.clearError("D6_C4_16");
            }
        }

        validate() {
            let self = this;
            let checkRequired = false;
            let checkCompare = false;
            // 項目名が存在し、記号が指定されていない項目が1件以上ある場合
            if (_.isNil(self.conditionSymbol())) {
                self.setError("D6_C3_1", "Msg_656");
                return;
            }

            // 検索する値が入力されていない項目が１件以上ある場合（複数の場合は全ての項目が入力されていること）
            switch (self.switchView()) {
                case SWITCH_VIEW.CHARACTER_NORMAL:
                    if (_.isEmpty(self.searchChar())) {
                        self.setError("D6_C4_1", "Msg_656");
                        checkRequired = true;
                    }
                    break;
                case SWITCH_VIEW.CHARACTER_PERIOD:
                    if (_.isEmpty(self.searchCharStartVal())) {
                        self.setError("D6_C4_2", "Msg_656");
                        checkRequired = true;
                    }
                    if (_.isEmpty(self.searchCharEndVal())) {
                        self.setError("D6_C4_3", "Msg_656");
                        checkRequired = true;
                    }
                    break;
                case SWITCH_VIEW.NUMERIC_NORMAL:
                    if (_.isEmpty(self.searchNum())) {
                        self.setError("D6_C4_4", "Msg_656");
                        checkRequired = true;
                    }
                    break;
                case SWITCH_VIEW.NUMERIC_PERIOD:
                    if (_.isNil(self.searchNumStartVal())) {
                        self.setError("D6_C4_5", "Msg_656");
                        checkRequired = true;
                    }
                    if (_.isNil(self.searchNumEndVal())) {
                        self.setError("D6_C4_6", "Msg_656");
                        checkRequired = true;
                    }
                    break;
                case SWITCH_VIEW.DATE_NORMAL:
                    if (_.isEmpty(self.searchDate())) {
                        self.setError("D6_C4_7", "Msg_656");
                        checkRequired = true;
                    }
                    break;
                case SWITCH_VIEW.DATE_PERIOD:
                    if (_.isEmpty(self.searchDateStart())) {
                        self.setError("D6_C4_8", "Msg_656");
                        checkRequired = true;
                    }
                    if (_.isEmpty(self.searchDateEnd())) {
                        self.setError("D6_C4_9", "Msg_656");
                        checkRequired = true;
                    }
                    break;
                case SWITCH_VIEW.TIME_NORMAL:
                    if (_.isNil(self.searchTime())) {
                        self.setError("D6_C4_10", "Msg_656");
                        checkRequired = true;
                    }
                    break;
                case SWITCH_VIEW.TIME_PERIOD:
                    if (_.isNil(self.searchTimeStartVal())) {
                        self.setError("D6_C4_11", "Msg_656");
                        checkRequired = true;
                    }
                    if (_.isNil(self.searchTimeEndVal())) {
                        self.setError("D6_C4_12", "Msg_656");
                        checkRequired = true;
                    }
                    break;
                case SWITCH_VIEW.INS_TIME_NORMAL:
                    if (_.isNil(self.searchClock())) {
                        self.setError("D6_C4_1", "Msg_656");
                        checkRequired = true;
                    }
                    break;
                case SWITCH_VIEW.INS_TIME_PERIOD:
                    if (_.isNil(self.searchClockStartVal())) {
                        self.setError("D6_C4_14", "Msg_656");
                        checkRequired = true;
                    }
                    if (_.isNil(self.searchClockEndVal())) {
                        self.setError("D6_C4_15", "Msg_656");
                        checkRequired = true;
                    }
                    break;
                case SWITCH_VIEW.SEARCH_CODE_LIST:
                    if (_.isEmpty(self.joinedSearchCodeList())) {
                        self.setError("D6_C4_16", "Msg_656");
                        checkRequired = true;
                    }
                    break;
            }
            if (checkRequired) {
                return
            }

            // 記号に「範囲内」が選択されている場合
            switch (self.switchView()) {
                case SWITCH_VIEW.CHARACTER_PERIOD:
                    if (self.searchCharStartVal() > self.searchCharEndVal()) {
                        self.setErrorCompare("D6_C4_2", "D6_C4_3");
                        checkCompare = true;
                    }
                    break;
                case SWITCH_VIEW.NUMERIC_PERIOD:
                    if (self.searchNumStartVal() > self.searchNumEndVal()) {
                        self.setErrorCompare("D6_C4_5", "D6_C4_6");
                        checkCompare = true;
                    }
                    break;
                case SWITCH_VIEW.DATE_PERIOD:
                    if (moment.utc(self.searchDateStart()).diff(moment.utc(self.searchDateEnd()), 'days') > 0) {
                        self.setErrorCompare("D6_C4_8", "D6_C4_9");
                        checkCompare = true;
                    }
                    break;
                case SWITCH_VIEW.TIME_PERIOD:
                    if (self.searchTimeStartVal() > self.searchTimeEndVal()) {
                        self.setErrorCompare("D6_C4_11", "D6_C4_12");
                        checkCompare = true;
                    }
                    break;
                case SWITCH_VIEW.INS_TIME_PERIOD:
                    if (self.searchClockStartVal() > self.searchClockEndVal()) {
                        self.setErrorCompare("D6_C4_14", "D6_C4_15");
                        checkCompare = true;
                    }
                    break;
            }
            if (checkCompare) {
                return;
            }

            // 記号に「同じ(複数)」「同じでない(複数)」が選択されている場合
            if (self.switchView() == SWITCH_VIEW.SEARCH_CODE_LIST) {
                self.validateSearchCodeList("D6_C4_16");
                return;
            }
        }

        /**
         * 外部出力条件登録チェック複数タイプ
         */
        validateSearchCodeList(control) {
            let self = this;
            let listSearchCode = _.split(self.joinedSearchCodeList(), ',')
            _.each(listSearchCode, item => {
                let searchCode = _.trim(item);

                // 検索コードがカテゴリ項目の型と同じ場合
                switch (self.dataType) {
                    case shareModel.ITEM_TYPE.CHARACTER:
                        if (!self.charValidator.validate(searchCode).isValid) {
                            self.setError(control, "Msg_760");
                            return false;
                        }
                        break;
                    case shareModel.ITEM_TYPE.NUMERIC:
                        if (!self.numberValidator.validate(searchCode).isValid) {
                            self.setError(control, "Msg_760");
                            return false;
                        }
                        break;
                    case shareModel.ITEM_TYPE.DATE:
                        if (!moment(searchCode, "YYYY/MM/DD", true).isValid()) {
                            self.setError(control, "Msg_760");
                            return false;
                        }
                        break;
                    case shareModel.ITEM_TYPE.TIME:
                        if (!self.timeValidator.validate(searchCode).isValid) {
                            self.setError(control, "Msg_760");
                            return false;
                        }
                        break;
                    case shareModel.ITEM_TYPE.INS_TIME:
                        if (!self.clockValidator.validate(searchCode).isValid) {
                            self.setError(control, "Msg_760");
                            return false;
                        }
                        break;
                }
                // 対象の値の桁数が「検索コード」の桁数より大きい場合
                if (!self.searchCdValidator.validate(searchCode).isValid) {
                    self.setError(control, "Msg_1346");
                    return false;
                }
            })
        }

        removeValidate() {
            let self = this;
            switch (self.switchView()) {
                case SWITCH_VIEW.CHARACTER_NORMAL:
                    self.clearError("D6_C4_1");
                    break;
                case SWITCH_VIEW.CHARACTER_PERIOD:
                    self.clearError("D6_C4_2");
                    self.clearError("D6_C4_3");
                    break;
                case SWITCH_VIEW.NUMERIC_NORMAL:
                    self.clearError("D6_C4_4");
                    break;
                case SWITCH_VIEW.NUMERIC_PERIOD:
                    self.clearError("D6_C4_5");
                    self.clearError("D6_C4_6");
                    break;
                case SWITCH_VIEW.DATE_NORMAL:
                    self.clearError("D6_C4_7");
                    break;
                case SWITCH_VIEW.DATE_PERIOD:
                    self.clearError("D6_C4_8");
                    self.clearError("D6_C4_9");
                    break;
                case SWITCH_VIEW.TIME_NORMAL:
                    self.clearError("D6_C4_10");
                case SWITCH_VIEW.TIME_PERIOD:
                    self.clearError("D6_C4_11");
                    self.clearError("D6_C4_12");
                    break;
                case SWITCH_VIEW.INS_TIME_NORMAL:
                    self.clearError("D6_C4_13");
                    break;
                case SWITCH_VIEW.INS_TIME_PERIOD:
                    self.clearError("D6_C4_14");
                    self.clearError("D6_C4_15");
                    break;
                case SWITCH_VIEW.SEARCH_CODE_LIST:
                    self.clearError("D6_C4_16");
                    break;
            }
        }

        static getSwitchView(dataType: shareModel.ITEM_TYPE, conditionSymbol: shareModel.CONDITION_SYMBOL): SWITCH_VIEW {
            switch (dataType) {
                case shareModel.ITEM_TYPE.CHARACTER:
                    switch (conditionSymbol) {
                        case shareModel.CONDITION_SYMBOL.BETWEEN: return SWITCH_VIEW.CHARACTER_PERIOD;
                        case shareModel.CONDITION_SYMBOL.IN:
                        case shareModel.CONDITION_SYMBOL.NOT_IN: return SWITCH_VIEW.SEARCH_CODE_LIST;
                        default: return SWITCH_VIEW.CHARACTER_NORMAL;
                    }
                case shareModel.ITEM_TYPE.NUMERIC:
                    switch (conditionSymbol) {
                        case shareModel.CONDITION_SYMBOL.BETWEEN: return SWITCH_VIEW.NUMERIC_PERIOD;
                        case shareModel.CONDITION_SYMBOL.IN:
                        case shareModel.CONDITION_SYMBOL.NOT_IN: return SWITCH_VIEW.SEARCH_CODE_LIST;
                        default: return SWITCH_VIEW.NUMERIC_NORMAL;
                    }
                case shareModel.ITEM_TYPE.DATE:
                    switch (conditionSymbol) {
                        case shareModel.CONDITION_SYMBOL.BETWEEN: return SWITCH_VIEW.DATE_PERIOD;
                        case shareModel.CONDITION_SYMBOL.IN:
                        case shareModel.CONDITION_SYMBOL.NOT_IN: return SWITCH_VIEW.SEARCH_CODE_LIST;
                        default: return SWITCH_VIEW.DATE_NORMAL;
                    }
                case shareModel.ITEM_TYPE.TIME:
                    switch (conditionSymbol) {
                        case shareModel.CONDITION_SYMBOL.BETWEEN: return SWITCH_VIEW.TIME_PERIOD;
                        case shareModel.CONDITION_SYMBOL.IN:
                        case shareModel.CONDITION_SYMBOL.NOT_IN: return SWITCH_VIEW.SEARCH_CODE_LIST;
                        default: return SWITCH_VIEW.TIME_NORMAL;
                    }
                case shareModel.ITEM_TYPE.INS_TIME:
                    switch (conditionSymbol) {
                        case shareModel.CONDITION_SYMBOL.BETWEEN: return SWITCH_VIEW.INS_TIME_PERIOD;
                        case shareModel.CONDITION_SYMBOL.IN:
                        case shareModel.CONDITION_SYMBOL.NOT_IN: return SWITCH_VIEW.SEARCH_CODE_LIST;
                        default: return SWITCH_VIEW.INS_TIME_NORMAL;
                    }
            }
        }

        static fromApp(app) {
            let dto = new OutCndDetailItemDto(app.categoryId, app.categoryItemNo, app.seriNum,
                app.conditionSettingCd, app.conditionSymbol);
            dto.searchNum(app.searchNum);
            dto.searchNumEndVal(app.searchNumEndVal);
            dto.searchNumStartVal(app.searchNumStartVal);
            dto.searchChar(app.searchChar);
            dto.searchCharEndVal(app.searchCharEndVal);
            dto.searchCharStartVal(app.searchCharStartVal);
            dto.searchDate(app.searchDate);
            dto.searchDateEnd(app.searchDateEnd);
            dto.searchDateStart(app.searchDateStart);
            dto.searchClock(app.searchClock);
            dto.searchClockEndVal(app.searchClockEndVal);
            dto.searchClockStartVal(app.searchClockStartVal);
            dto.searchTime(app.searchTime);
            dto.searchTimeEndVal(app.searchTimeEndVal);
            dto.searchTimeStartVal(app.searchTimeStartVal);
            dto.joinedSearchCodeList(app.joinedSearchCodeList);
            return dto;
        }
    }
    class SearchCodeListDto {
        conditionSetCode: string;
        categoryId: number;
        categoryItemNo: number;
        seriNum: number;
        searchCode: string;
        searchItemName: string;
        constructor(conditionSetCode: string, categoryId: number, categoryItemNo: number,
            seriNum: number, searchCode: string, searchItemName: string) {
            let self = this;
            self.conditionSetCode = conditionSetCode;
            self.categoryId = categoryId;
            self.categoryItemNo = categoryItemNo;
            self.seriNum = seriNum;
            self.searchCode = searchCode;
            self.searchItemName = searchItemName;
        }
    }

    export enum SWITCH_VIEW {
        NONE = 0,
        CHARACTER_NORMAL = 1,
        CHARACTER_PERIOD = 2,
        NUMERIC_NORMAL = 3,
        NUMERIC_PERIOD = 4,
        DATE_NORMAL = 5,
        DATE_PERIOD = 6,
        TIME_NORMAL = 7,
        TIME_PERIOD = 8,
        INS_TIME_NORMAL = 9,
        INS_TIME_PERIOD = 10,
        SEARCH_CODE_LIST = 11
    }

    class OutCndDetailInfoCommand {
        outCndDetail: OutCndDetailDto;
        standardAtr: number;
        registerMode: number;
        constructor(outCndDetail: OutCndDetailDto, standardAtr: number, registerMode: number) {
            this.outCndDetail = outCndDetail;
            this.standardAtr = standardAtr;
            this.registerMode = registerMode;
        }
    }

    class OutCndDetailCommand {
        conditionSettingCd: string;
        exterOutCdnSql: string;
        listOutCndDetailItem: Array<OutCndDetailItemCommand>;
        constructor() {

        }
        static fromDto(dto: OutCndDetailDto) {
            let cmd = new OutCndDetailCommand();
            let listOutCndDetailItem = [];
            _.each(dto.listOutCndDetailItem(), function(item: OutCndDetailItemDto) {
                listOutCndDetailItem.push(OutCndDetailItemCommand.fromDto(item));
            })
            cmd.conditionSettingCd = dto.conditionSettingCd;
            cmd.exterOutCdnSql = dto.exterOutCdnSql;
            cmd.listOutCndDetailItem = listOutCndDetailItem
            return cmd;
        }
    }

    class OutCndDetailItemCommand {
        categoryId: string;
        categoryItemNo: number;
        seriNum: number;
        conditionSettingCd: string;
        conditionSymbol: number;
        searchNum: number;
        searchNumEndVal: number;
        searchNumStartVal: number;
        searchChar: string;
        searchCharEndVal: string;
        searchCharStartVal: string;
        searchDate: string;
        searchDateEnd: string;
        searchDateStart: string;
        searchClock: number;
        searchClockEndVal: number;
        searchClockStartVal: number;
        searchTime: number;
        searchTimeEndVal: number;
        searchTimeStartVal: number;
        listSearchCodeList: Array<SearchCodeListDto>;
        constructor() {

        }
        static fromDto(dto: OutCndDetailItemDto) {
            let cmd = new OutCndDetailItemCommand();
            cmd.categoryId = dto.categoryId();
            cmd.categoryItemNo = dto.categoryItemNo();
            cmd.seriNum = dto.seriNum();
            cmd.conditionSettingCd = dto.conditionSettingCd();
            cmd.conditionSymbol = dto.conditionSymbol();
            cmd.searchNum = dto.searchNum();
            cmd.searchNumEndVal = dto.searchNumEndVal();
            cmd.searchNumStartVal = dto.searchNumStartVal();
            cmd.searchChar = dto.searchChar();
            cmd.searchCharEndVal = dto.searchCharEndVal();
            cmd.searchCharStartVal = dto.searchCharStartVal();
            if (dto.searchDate() != null) {
                cmd.searchDate = moment.utc(dto.searchDate(), "YYYY/MM/DD")._d
            }
            if (dto.searchDateEnd() != null) {
                cmd.searchDateEnd = moment.utc(dto.searchDateEnd(), "YYYY/MM/DD")._d
            }
            if (dto.searchDateStart() != null) {
                cmd.searchDateStart = moment.utc(dto.searchDateStart(), "YYYY/MM/DD")._d
            }
            cmd.searchClock = dto.searchClock();
            cmd.searchClockEndVal = dto.searchClockEndVal();
            cmd.searchClockStartVal = dto.searchClockStartVal();
            cmd.searchTime = dto.searchTime();
            cmd.searchTimeEndVal = dto.searchTimeEndVal();
            cmd.searchTimeStartVal = dto.searchTimeStartVal();
            cmd.listSearchCodeList = dto.listSearchCodeList;
            return cmd;
        }
    }
}