/// <reference path="../../../../lib/nittsu/viewcontext.d.ts" />

module nts.uk.at.kmr003.a {

    import block = nts.uk.ui.block;
    import getText = nts.uk.resource.getText;
    import validation = nts.uk.ui.validation;
    import parseTime = nts.uk.time.parseTime;

    const API = {
        BENTO_RESERVATTIONS: 'screen/at/record/reservation/bento_modify/getReservations',
        BENTO_UPDATE: 'at/record/reservation/bento/force-update',
        BENTO_DELETE: 'at/record/reservation/bento/force-delete'
    };

    // Grid cell errors
    const dialogOptions: any = {
        forGrid: true,
        headers: [
            new nts.uk.ui.errors.ErrorHeader("rowId", getText("KMR003_21"), "auto", true),
            new nts.uk.ui.errors.ErrorHeader("columnKey", "弁当ヘッダー", "auto", true),
            new nts.uk.ui.errors.ErrorHeader("message", "エラー内容", "auto", true)
        ]
    };

    @bean(dialogOptions)
    export class KMR003AViewModel extends ko.ViewModel {
        date: KnockoutObservable<string> = ko.observable((new Date()).toISOString().substr(0, 10) + 'T00:00:00.000Z');

        //A2_5 A2_6
        searchConditions: KnockoutObservableArray<any> = ko.observableArray(__viewContext.enums.BentoReservationSearchConditionDto);
        searchConditionValue: KnockoutObservable<number> = ko.observable(4);

        //A2_7 A2_8
        closingTimeFrames: KnockoutObservableArray<ClosingTimeFrame> = ko.observableArray();
        closingTimeFrameValue: KnockoutObservable<number> = ko.observable(1);
        closingTimeTime: KnockoutObservable<any> = ko.observable();

        ccg001ComponentOption: GroupOption = null;

        dynamicColumns = [];
        datas: Array<ReservationModifyEmployeeDto> = [];
        listBento = [];
        headerInfos: Array<HeaderInfoDto> = [];
        empSearchItems: Array<EmployeeSearchDto> = [];

        canDelete: KnockoutObservable<boolean> = ko.observable(false);
        deleteItems: KnockoutObservableArray<string> = ko.observableArray([]);

        constructor() {
            super();
            let vm = this;
            vm.ccg001ComponentOption = <GroupOption>{
                /** Common properties */
                systemType: 2,
                showEmployeeSelection: true,
                showQuickSearchTab: true,
                showAdvancedSearchTab: true,
                showBaseDate: true,
                showClosure: false,
                showAllClosure: false,
                showPeriod: false,
                periodFormatYM: null,

                /** Required parameter */
                baseDate: moment.utc().toISOString(),
                periodStartDate: null,
                periodEndDate: null,
                inService: true,
                leaveOfAbsence: true,
                closed: true,
                retirement: false,

                /** Quick search tab options */
                showAllReferableEmployee: true,
                showOnlyMe: true,
                showSameWorkplace: true,
                showSameWorkplaceAndChild: true,

                /** Advanced search properties */
                showEmployment: true,
                showDepartment: false,
                showWorkplace: true,
                showClassification: true,
                showJobTitle: true,
                showWorktype: true,
                isMutipleCheck: null,
                tabindex: 6,
                showOnStart: true,

                /**
                 * Self-defined function: Return data from CCG001
                 * @param: data: the data return from CCG001
                 */
                returnDataFromCcg001: function (data: Ccg001ReturnedData) {
                    vm.empSearchItems = data.listEmployee;
                    vm.initData();
                }
            }
        }

        created() {
            const vm = this;
            $('#com-ccg001').ntsGroupComponent(vm.ccg001ComponentOption);
            vm.loadMGrid();
            vm.initData();

            vm.date.subscribe((value: string) => {
                let momentDate = moment(value);
                if (momentDate instanceof moment && !momentDate.isValid()) {
                    return;
                }

                vm.initData();
            })
            vm.searchConditionValue.subscribe(() => {
                vm.initData();
            })
            vm.closingTimeFrameValue.subscribe(value => {
                vm.setClosingTimeTime(value);
                vm.initData();
            })

            _.extend(window, { vm });
        }

        isNewMode() {
            let self = this;
            return self.searchConditionValue() == 3
        }

        getFixedColumns(): Array<any> {
            let self = this;

            let isNewMode = self.isNewMode();
            var fixedColumns = [];
            fixedColumns.push({ headerText: "key", key: 'key', dataType: 'string', hidden: true });
            fixedColumns.push({
                headerText: getText("KMR003_21"), key: 'reservationMemberCode', dataType: 'number', width: '115px', ntsControl: "Label"
            });
            fixedColumns.push({ headerText: getText("KMR003_22"), key: 'reservationMemberName', dataType: 'string', width: '150px', ntsControl: "Label" });
            if (!isNewMode) {
                fixedColumns.push({
                    headerText: getText("KMR003_23"),
                    group: [
                        { headerText: '', key: 'isDelete', dataType: 'boolean', width: '60px', checkbox: true, ntsControl: "isDelCheckBox" }
                    ]
                });
            }

            fixedColumns.push({ headerText: getText("KMR003_24"), key: 'reservationTime', dataType: 'string', width: '65px', ntsControl: "Label", columnCssClass: "halign-right" });

            if (isNewMode) {
                fixedColumns.push({ headerText: getText("KMR003_25"), key: 'ordered', dataType: 'boolean', width: '60px', ntsControl: "Label" });
            } else {
                fixedColumns.push({
                    headerText: getText("KMR003_25"),
                    group: [
                        { headerText: '', key: 'ordered', dataType: 'boolean', width: '60px', checkbox: true, ntsControl: "isOrderCheckBox" }
                    ]
                });
            }

            return fixedColumns;
        }

        loadMGrid() {
            let self = this;
            let height = $(window).height() - 90 - 290;
            let width = $(window).width() + 20 - 1170;

            let cellStates: Array<CellState> = [];
            _.forEach(self.datas, (data: ReservationModifyEmployeeDto) => {
                if (!self.isNewMode()) {
                    cellStates.push(new CellState(data.key, "isDelete", ["center-align"]));
                }

                cellStates.push(new CellState(data.key, "ordered", ["center-align"]));
            });

            new nts.uk.ui.mgrid.MGrid($("#grid")[0], {
                width: "1170px",
                height: "200px",
                subWidth: width + "px",
                subHeight: height + "px",
                headerHeight: '60px',
                dataSource: self.datas,
                primaryKey: 'key',
                primaryKeyDataType: 'string',
                rowVirtualization: true,
                virtualization: true,
                virtualizationMode: 'continuous',
                enter: 'right',
                autoFitWindow: false,
                hidePrimaryKey: true,
                // errorColumns: [ "ruleCode" ],
                errorsOnPage: true,
                columns: self.getFixedColumns().concat(self.dynamicColumns),
                ntsControls: [
                    {
                        name: 'isDelCheckBox', options: { value: 1, text: '' }, optionsValue: 'value',
                        optionsText: 'text', controlType: 'CheckBox', enable: true,
                        onChange: function (rowId, columnKey, value, rowData) {
                            self.checkDelete(rowId, value);
                        }
                    },
                    {
                        name: 'isOrderCheckBox', options: { value: 1, text: '' }, optionsValue: 'value',
                        optionsText: 'text', controlType: 'CheckBox', enable: true,
                        onChange: function (rowId, columnKey, value, rowData) {
                            self.setBentoInput(rowId, value);
                        }
                    }
                ],
                features: [
                    {
                        name: "ColumnFixing",
                        showFixButtons: false,
                        fixingDirection: 'left',
                        columnSettings: [
                            {
                                columnKey: "reservationMemberCode",
                                isFixed: true
                            },
                            {
                                columnKey: "reservationMemberName",
                                isFixed: true
                            },
                            {
                                columnKey: "isDelete",
                                isFixed: true
                            },
                            {
                                columnKey: "reservationTime",
                                isFixed: true
                            },
                            {
                                columnKey: "ordered",
                                isFixed: true
                            }
                        ]
                    },
                    {
                        name: 'CellStyles',
                        states: cellStates
                    }
                ]
            }).create();
            self.setPageStatus();
        }

        setPageStatus() {
            let self = this;
            let data = $("#grid").mGrid("dataSource");
            _.each(data, (item: ReservationModifyEmployeeDto) => {
                if (!item.activity) {
                    // self.disableControl(item.key, "reservationMemberCode", true);
                    // self.disableControl(item.key, "reservationMemberName", true);
                    self.disableControl(item.key, "isDelete", true);
                    // self.disableControl(item.key, "reservationTime", true);
                    self.disableControl(item.key, "ordered", true);

                    self.setBentoInput(item.key, true);
                } else {
                    if (self.isNewMode()) {
                        self.disableControl(item.key, "ordered", true);
                    }
                    self.setBentoInput(item.key, item.ordered);
                }
            })
        }

        checkDelete(rowId: any, check: any) {
            let self = this;
            if (check) {
                self.deleteItems().push(rowId);
            } else {
                const index = self.deleteItems().indexOf(rowId);
                if (index > -1) {
                    self.deleteItems().splice(index, 1);
                }
            }

            self.canDelete(self.deleteItems().length > 0);
        }

        setBentoInput(rowId: any, isDisable: any) {
            let self = this;
            _.forEach(self.headerInfos, (bento: HeaderInfoDto) => {
                self.disableControl(rowId, bento.key, isDisable);
            })
        }

        disableControl(rowId, columnKey, isDisable) {
            if (isDisable) {
                $("#grid").mGrid("disableNtsControlAt", rowId, columnKey)
            } else {
                $("#grid").mGrid("enableNtsControlAt", rowId, columnKey)
            }
        }

        initData(): JQueryPromise<any> {
            let self = this,
                dfd = $.Deferred();
            self.$blockui("invisible");
            let param = self.createParamGet();
            self.datas = [];
            self.dynamicColumns = [];
            self.deleteItems.removeAll();
            self.canDelete(false);
            self.$ajax(API.BENTO_RESERVATTIONS, param).done((res: IReservationModifyDto) => {
                if (!_.isEmpty(res.bentoClosingTimes)) {
                    self.closingTimeFrames.removeAll();
                }

                _.forEach(res.bentoClosingTimes, (item: IClosingTimeDto) => {
                    //add A2_8 A2_9 A2_10
                    self.closingTimeFrames.push(new ClosingTimeFrame(item));
                })
                self.setClosingTimeTime(self.closingTimeFrameValue());
                self.headerInfos = _.map(res.bentos, (item: IHeaderInfoDto) => { return new HeaderInfoDto(item); });
                _.forEach(self.headerInfos, (item: HeaderInfoDto) => {
                    self.dynamicColumns.push({
                        headerText: item.bentoName,
                        group: [
                            {
                                headerText: item.unitLabel,
                                key: item.key,
                                dataType: 'number',
                                width: '80px',
                                columnCssClass: 'halign-right',
                                constraint: {
                                    primitiveValue: 'BentoReservationCount',
                                    required: false
                                }
                            }
                        ]
                    });
                });

                self.datas = _.map(res.reservationModifyEmps, (item: IReservationModifyEmployeeDto) => {
                    let dto = new ReservationModifyEmployeeDto(item);
                    dto.convertData(self.headerInfos);
                    return dto;
                });

                if (!_.isEmpty(res.errors)) {
                    let errors = [];
                    _.forEach(res.errors, error => {
                        errors.push({
                            message: error.message,
                            messageId: error.messageId,
                            supplements: {}
                        })
                    });

                    nts.uk.ui.dialog.bundledErrors({ errors: errors });
                }
            }).fail(err => {
                self.$dialog.error(err);
                self.$blockui("clear");
            }).always(() => {
                $("#grid").mGrid("destroy");
                self.loadMGrid();
                self.$blockui("clear");

                // check focus
                if (_.isEmpty(self.datas)) {
                    $("#ccg001-btn-search-drawer").focus()
                } else {
                    $("#A1_2").focus();
                }
                dfd.resolve();
            })
            return dfd.promise();
        }

        createParamGet() {
            let self = this;
            let empIds = _.map(self.empSearchItems, (item: EmployeeSearchDto) => {
                return item.employeeId;
            });
            let param = {
                empIds: empIds,
                date: self.date(),
                closingTimeFrame: self.closingTimeFrameValue(),
                searchCondition: self.searchConditionValue()
            };
            return param;
        }

        setClosingTimeTime(frameId: number) {
            let self = this;
            let frame = _.find(self.closingTimeFrames(), (item: ClosingTimeFrame) => { return item.id == frameId });
            if (frame) {
                self.closingTimeTime(parseTime(frame.startTime, true).format() + "～" + parseTime(frame.endTime, true).format())
            } else {
                self.closingTimeTime("");
            }
        }

        updateReservation() {
            let self = this;
            self.$blockui("invisible");
            let reservations: Array<ReservationModifyEmployeeDto> = $("#grid").mGrid("dataSource", true);

            let commandUpdate = new ForceUpdateBentoReserveCommand(self.date(), self.isNewMode(), self.closingTimeFrameValue());
            commandUpdate.setReservationInfos(reservations, self.headerInfos);
            self.$ajax(API.BENTO_UPDATE, commandUpdate).done(() => {
                self.$dialog.info({ messageId: "Msg_15" }).then(function () {
                    self.$blockui("clear");
                    if (self.isNewMode()) {
                        self.searchConditionValue(4);
                    } else {
                        self.initData();
                    }
                });
            }).always(() => self.$blockui("clear"));
        }

        deleteReservation() {
            let self = this;
            self.$dialog.confirm({ messageId: 'Msg_18' }).then(res => {
                if (res == "yes") {
                    self.$blockui("invisible");
                    let reservations: Array<ReservationModifyEmployeeDto> = $("#grid").mGrid("dataSource", true);
                    let reservationDeletes = _.filter(reservations, (reservation: ReservationModifyEmployeeDto) => { return reservation.isDelete; });

                    let commandDelete = new ForceDeleteBentoReserveCommand(self.date(), self.closingTimeFrameValue());
                    commandDelete.setReservationInfos(reservationDeletes);
                    self.$ajax(API.BENTO_DELETE, commandDelete).done(() => {
                        self.$dialog.info({ messageId: "Msg_16" }).then(function () {
                            self.$blockui("clear");
                            self.initData();
                        });
                    }).always(() => self.$blockui("clear"));
                }
            });
        }
    }

    interface GroupOption {
        /** Common properties */
        showEmployeeSelection?: boolean; // 検索タイプ
        systemType: number; // システム区分
        showQuickSearchTab?: boolean; // クイック検索
        showAdvancedSearchTab?: boolean; // 詳細検索
        showBaseDate?: boolean; // 基準日利用
        showClosure?: boolean; // 就業締め日利用
        showAllClosure?: boolean; // 全締め表示
        showPeriod?: boolean; // 対象期間利用
        periodFormatYM?: boolean; // 対象期間精度
        isInDialog?: boolean;

        /** Required parameter */
        baseDate?: string; // 基準日
        periodStartDate?: string; // 対象期間開始日
        periodEndDate?: string; // 対象期間終了日
        inService: boolean; // 在職区分
        leaveOfAbsence: boolean; // 休職区分
        closed: boolean; // 休業区分
        retirement: boolean; // 退職区分

        /** Quick search tab options */
        showAllReferableEmployee?: boolean; // 参照可能な社員すべて
        showOnlyMe?: boolean; // 自分だけ
        showSameWorkplace?: boolean; // 同じ職場の社員
        showSameWorkplaceAndChild?: boolean; // 同じ職場とその配下の社員

        /** Advanced search properties */
        showEmployment?: boolean; // 雇用条件
        showWorkplace?: boolean; // 職場条件
        showClassification?: boolean; // 分類条件
        showJobTitle?: boolean; // 職位条件
        showWorktype?: boolean; // 勤種条件
        isMutipleCheck?: boolean; // 選択モード
        isTab2Lazy?: boolean;

        /** Data returned */
        returnDataFromCcg001: (data: Ccg001ReturnedData) => void;
    }

    interface Ccg001ReturnedData {
        baseDate: string; // 基準日
        closureId?: number; // 締めID
        periodStart: string; // 対象期間（開始)
        periodEnd: string; // 対象期間（終了）
        listEmployee: Array<EmployeeSearchDto>; // 検索結果
    }

    interface EmployeeSearchDto {
        employeeId: string;
        employeeCode: string;
        employeeName: string;
        affiliationCode: string;
        affiliationId: string;
        affiliationName: string;
    }

    class CellState {
        rowId: any;
        columnKey: string;
        state: Array<any>
        constructor(rowId: any, columnKey: string, state: Array<any>) {
            this.rowId = rowId;
            this.columnKey = columnKey;
            this.state = state;
        }
    }

    class ClosingTimeFrame {
        id: number;
        name: string;
        endTime: number;
        startTime: number;

        constructor(item: IClosingTimeDto) {
            this.id = item.closingTimeFrame;
            this.name = item.name;
            this.startTime = item.startTime;
            this.endTime = item.endTime;
        }
    }

    // 予約の修正起動情報
    class IReservationModifyDto {
        bentos: Array<IHeaderInfoDto>;

        bentoClosingTimes: Array<IClosingTimeDto>;

        empFinishs: Array<IEmployeeInfoMonthFinishDto>;

        reservationModifyEmps: Array<IReservationModifyEmployeeDto>;

        errors: Array<any>;
    }

    // 弁当ヘッダー
    interface IHeaderInfoDto {
        frameNo: number;
        bentoName: string;
        unit: string;
    }

    class HeaderInfoDto {
        frameNo: number;
        bentoName: string;
        unit: string;

        key: string;
        unitLabel: string;
        constructor(item: IHeaderInfoDto) {
            this.frameNo = item.frameNo;
            this.bentoName = item.bentoName;
            this.unit = item.unit;

            this.key = item.frameNo + "_" + item.bentoName;
            this.unitLabel = "(" + item.unit + ")";
        }
    }

    // 弁当メニューの締め時刻
    interface IClosingTimeDto {
        closingTimeFrame: number;
        name: string;
        endTime: number;
        startTime: number;
    }

    // 月締め処理が済んでいる社員情報
    interface IEmployeeInfoMonthFinishDto {
        employeeCode: string;
        employeeName: string;
    }

    // 社員の予約情報
    interface IReservationModifyEmployeeDto {
        reservationDetails: Array<IReservationModifyDetailDto>;
        reservationCardNo: string;
        reservationMemberId: string;
        reservationMemberCode: string;
        reservationMemberName: string;
        reservationDate: Date;
        reservationTime: number;
        activity: boolean;
        ordered: boolean;
        closingTimeFrame: number;
    }

    // 社員の予約情報
    interface IReservationModifyDetailDto {
        bentoCount: number;
        frameNo: number;
    }


    // 社員の予約情報
    class ReservationModifyEmployeeDto {
        reservationDetails: Array<ReservationModifyDetailDto>;
        reservationCardNo: string;
        reservationMemberId: string;
        reservationMemberCode: string;
        reservationMemberName: string;
        reservationDate: Date;
        reservationTime: number;
        activity: boolean;
        ordered: boolean;
        closingTimeFrame: number;

        key: string;
        isDelete: boolean;

        constructor(item: IReservationModifyEmployeeDto) {
            this.reservationDetails = _.map(item.reservationDetails, (x: IReservationModifyDetailDto) => {
                return new ReservationModifyDetailDto(x);
            })

            this.reservationCardNo = item.reservationCardNo;
            this.reservationMemberId = item.reservationMemberId;
            this.reservationMemberCode = item.reservationMemberCode;
            this.reservationMemberName = item.reservationMemberName;
            this.reservationDate = item.reservationDate;
            this.reservationTime = item.reservationTime;
            this.activity = item.activity;
            this.ordered = item.ordered;
            this.closingTimeFrame = item.closingTimeFrame;

            this.key = item.reservationMemberCode;
            this.isDelete = false;
        }

        convertData(headerInfos: Array<HeaderInfoDto>) {
            let self = this;
            _.forEach(this.reservationDetails, (item: ReservationModifyDetailDto) => {
                let header = _.find(headerInfos, (x: HeaderInfoDto) => { return x.frameNo == item.frameNo; });
                if (header) {
                    self[header.key] = item.bentoCount;
                }
            })
        }
    }

    // 社員の予約情報
    class ReservationModifyDetailDto {
        bentoCount: number;
        frameNo: number;
        constructor(item: IReservationModifyDetailDto) {
            this.bentoCount = item.bentoCount;
            this.frameNo = item.frameNo;
        }
    }

    class ForceUpdateBentoReserveCommand {
        reservationInfos: Array<BentoReserveInfoCommand>;
        date: any;
        isNew: boolean;
        closingTimeFrame: number;

        constructor(date: any, isNew: boolean, closingTimeFrame: number) {
            this.reservationInfos = [];
            this.date = date;
            this.isNew = isNew;
            this.closingTimeFrame = closingTimeFrame;
        }

        setReservationInfos(reservations: Array<ReservationModifyEmployeeDto>, bentos: Array<HeaderInfoDto>) {
            this.reservationInfos = _.map(reservations, (reservation: ReservationModifyEmployeeDto) => {
                return new BentoReserveInfoCommand(reservation, bentos);
            });
        }
    }

    class BentoReserveInfoCommand {
        reservationCardNo: String;
        ordered: boolean;
        details: Array<BentoReserveDetailCommand>
        constructor(reservation: ReservationModifyEmployeeDto, bentos: Array<HeaderInfoDto>) {
            let self = this;
            self.reservationCardNo = reservation.reservationCardNo;
            self.ordered = reservation.ordered;
            self.details = [];
            _.forEach(bentos, (bento: HeaderInfoDto) => {
                let bentoCount = reservation[bento.key];
                if (!isNaN(bentoCount) && bentoCount != null) {
                    self.details.push(new BentoReserveDetailCommand(bento.frameNo, bentoCount));
                }
            })
        }
    }

    class BentoReserveDetailCommand {
        frameNo: number;
        bentoCount: number;

        constructor(frameNo: number, bentoCount: number) {
            this.frameNo = frameNo;
            this.bentoCount = bentoCount;
        }
    }

    class ForceDeleteBentoReserveCommand {
        reservationInfos: Array<ReservationInfoCommand>;
        date: any;
        closingTimeFrame: number;

        constructor(date: any, closingTimeFrame: number) {
            this.date = date;
            this.closingTimeFrame = closingTimeFrame;
        }

        setReservationInfos(reservations: Array<ReservationModifyEmployeeDto>) {
            this.reservationInfos = _.map(reservations, (reservation: ReservationModifyEmployeeDto) => {
                return new ReservationInfoCommand(reservation.reservationCardNo, reservation.reservationMemberId);
            });
        }
    }

    class ReservationInfoCommand {
        reservationCardNo: String;
        empployeeId: String;

        constructor(reservationCardNo: String, empployeeId: String) {
            this.reservationCardNo = reservationCardNo;
            this.empployeeId = empployeeId;
        }
    }
}
