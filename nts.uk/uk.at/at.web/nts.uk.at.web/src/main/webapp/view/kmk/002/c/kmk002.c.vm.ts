module nts.uk.at.view.kmk002.c {
    import AttdItemDto = service.model.AttendanceItemDto;
    import AttdItemLinkRequest = service.model.AttdItemLinkRequest;
    import AttendanceItemDto = nts.uk.at.view.kmk002.a.service.model.AttendanceItemDto;
    import ItemSelectionDto = nts.uk.at.view.kmk002.a.service.model.ItemSelectionDto;
    import ParamToC = nts.uk.at.view.kmk002.a.viewmodel.ParamToC;

    export module viewmodel {
        export class ScreenModel {

            leftItems: KnockoutObservableArray<AttdItemDto>;
            rightItems: KnockoutObservableArray<AttendanceItemDto>;
            selectedLeftItem: KnockoutObservableArray<number>;
            selectedRightItem: KnockoutObservableArray<number>;
            minusSegment: KnockoutObservable<boolean>;
            columnsLeft: any;
            columnsRight: any;
            formulaAtr: string;
            formulaName: string;

            constructor() {
                let self = this;
                self.selectedRightItem = ko.observableArray([]);
                self.leftItems = ko.observableArray([]);
                self.selectedLeftItem = ko.observableArray([]);
                self.rightItems = ko.observableArray([]);
                self.minusSegment = ko.observable(false);

                // data source
                self.columnsLeft = ko.observableArray([
                    { headerText: '', key: 'attendanceItemId', hidden: true },
                    { headerText: nts.uk.resource.getText('KMK002_7'), key: 'attendanceItemDisplayNumber', width: 50 },
                    { headerText: nts.uk.resource.getText('KMK002_8'), key: 'attendanceItemName', width: 100 }
                ]);
                self.columnsRight = ko.observableArray([
                    { headerText: '', key: 'attendanceItemId', hidden: true },
                    { headerText: nts.uk.resource.getText('KMK002_58'), key: 'operatorText', width: 50 },
                    { headerText: nts.uk.resource.getText('KMK002_7'), key: 'attendanceItemDisplayNumber', width: 50 },
                    { headerText: nts.uk.resource.getText('KMK002_8'), key: 'attendanceItemName', width: 100 }
                ]);

            }

            /**
             * Start page.
             */
            public startPage(): JQueryPromise<void> {
                let self = this;
                let dfd = $.Deferred<void>();

                // block ui.
                nts.uk.ui.block.invisible();

                // load attendance items
                self.loadAttendanceItems()
                    .done(() => dfd.resolve())
                    .always(() => nts.uk.ui.block.clear()); // clear block ui.;

                return dfd.promise();
            }

            /**
             * Load attendance items.
             */
            private loadAttendanceItems(): JQueryPromise<void> {
                let self = this;
                let dfd = $.Deferred<void>();

                // Get param from parent screen
                let param: ParamToC = nts.uk.ui.windows.getShared('paramToC');

                // create request object
                let request = <AttdItemLinkRequest>{};
                request.anyItemNos = param.excludedOptItemNos;
                request.formulaAtr = param.formulaAtr;
                request.performanceAtr = param.performanceAtr;

                // get list attendance item
                if (param.performanceAtr == 1) {
                    // get list daily attendance item
                    service.findDailyAttdItem(request).done(data => {
                        self.setDatasource(param, data);

                        dfd.resolve();
                    });
                } else {
                    // get list monthly attendance item
                    service.findMonthlyAttdItem(request).done(data => {
                        self.setDatasource(param, data);

                        dfd.resolve();
                    });
                }

                return dfd.promise();
            }

            /**
             * Set datasource for viewmodel
             */
            private setDatasource(param: ParamToC, data: Array<AttdItemDto>): void {
                let self = this;

                // set datasource for left table
                self.leftItems(data);
                self.sortLeftTable();

                // set displayNumber for right table
                param.itemSelection.attendanceItems.forEach(item => {
                    let vl = _.find(data, it => it.attendanceItemId == item.attendanceItemId);
                    if (!_.isUndefined(vl)) {
                        item.attendanceItemDisplayNumber = vl.attendanceItemDisplayNumber;    
                    }
                });
                let sortedRightTable = _.sortBy(param.itemSelection.attendanceItems,item => item.attendanceItemDisplayNumber);
                // Set param to view model.
                self.fromDto(param);

                // remove selected items from left table.
                self.removeItemFromLeftTable(param.itemSelection.attendanceItems);
                self.rightItems(sortedRightTable);

            }

            /**
             * Submit and close dialog.
             */
            public submit(): void {
                let self = this;

                // set return value
                let dto = <ItemSelectionDto>{};
                dto.minusSegment = self.minusSegment() == true ? 1 : 0;
                dto.attendanceItems = self.rightItems();
                nts.uk.ui.windows.setShared('returnFromC', dto);

                // close dialog.
                self.close();
            }

            /**
             * Close dialog.
             */
            public close(): void {
                nts.uk.ui.windows.close();
            }

            /**
             * Remove selected items from left table.
             */
            private removeItemFromLeftTable(listItem: Array<AttendanceItemDto>): void {
                let self = this;
                _.forEach(listItem, item => {
                    self.leftItems.remove(i => i.attendanceItemId == item.attendanceItemId);
                });
            }

            /**
             * Event on click add button
             */
            public onClickAdd(): void {
                let self = this;
                self.toRightTable(AddSubOperator.ADD);
            }

            /**
             * Event on click subtract button
             */
            public onClickSubtract(): void {
                let self = this;
                self.toRightTable(AddSubOperator.SUBTRACT);
            }

            /**
             * Push items of left to right table.
             */
            private toRightTable(method: AddSubOperator): void {
                let self = this;
                if (!nts.uk.util.isNullOrEmpty(self.selectedLeftItem())) {
                    let rightItems = [];
                    let removedItems = [];

                    _.forEach(self.selectedLeftItem(), id => {
                        // get selected item by selected id
                        let item = _.find(self.leftItems(), item => item.attendanceItemId == id);
                        removedItems.push(item);

                        // push item to right table
                        if (method == AddSubOperator.ADD) {
                            rightItems.push(self.toSelectDto(item, AddSubOperator.ADD));
                        } else {
                            rightItems.push(self.toSelectDto(item, AddSubOperator.SUBTRACT));
                        }

                    });

                    // remove from left table
                    self.leftItems.removeAll(removedItems);

                    // set right items.
                    self.rightItems(self.rightItems().concat(rightItems));

                    // sort right table
                    self.sortRightTable();

                    // clear selected item
                    self.selectedLeftItem([]);
                }
            }

            /**
             * Event on click back button
             */
            private onClickBack(): void {
                let self = this;
                if (!nts.uk.util.isNullOrEmpty(self.selectedRightItem())) {
                    let leftItems = [];
                    let removedItems = [];

                    _.forEach(self.selectedRightItem(), id => {
                        // get selected item by selected id
                        let item = _.find(self.rightItems(), item => item.attendanceItemId == id);
                        removedItems.push(item);

                        // push item to left table
                        leftItems.push(self.toBackDto(item));
                    });

                    // remove from left table
                    self.rightItems.removeAll(removedItems);

                    // set left items
                    self.leftItems(self.leftItems().concat(leftItems));

                    // sort left table
                    self.sortLeftTable();

                    // clear selected item.
                    self.selectedRightItem([]);
                }
            }

            /**
             * Sort list attendance item by attendance item Id.
             */
            private sortLeftTable(): void {
                let self = this;
                let sortedList = _.sortBy(self.leftItems(), item => item.attendanceItemDisplayNumber);
                self.leftItems(sortedList);
            }

            /**
             * Sort right table.
             */
            private sortRightTable(): void {
                let self = this;
                let sortedList = _.sortBy(self.rightItems(), item => item.attendanceItemDisplayNumber);
                self.rightItems(sortedList);
            }

            /**
             * Convert dto to view model.
             */
            private fromDto(dto: ParamToC): void {
                let self = this;
                self.formulaAtr = dto.formulaAtrName;
                self.formulaName = dto.formulaName;
                self.rightItems(dto.itemSelection.attendanceItems);
                self.minusSegment(dto.itemSelection.minusSegment == 1 ? true : false);
            }

            /**
             * to select data object
             */
            private toSelectDto(data: AttdItemDto, method: number): AttendanceItemDto {
                let operatorText: string = '';
                if (method == AddSubOperator.ADD) {
                    operatorText = nts.uk.resource.getText('Enum_OperatorAtr_ADD');
                }
                else {
                    operatorText = nts.uk.resource.getText('Enum_OperatorAtr_SUBTRACT');
                }
                let dto: AttendanceItemDto = {
                    operator: method,
                    operatorText: operatorText,
                    attendanceItemId: data.attendanceItemId,
                    attendanceItemName: data.attendanceItemName,
                    attendanceItemDisplayNumber: data.attendanceItemDisplayNumber
                };
                return dto;
            }

            /**
             * to back data object
             */
            private toBackDto(data: AttendanceItemDto): AttdItemDto {
                var dto: AttdItemDto = {
                    attendanceItemId: data.attendanceItemId,
                    attendanceItemName: data.attendanceItemName,
                    attendanceItemDisplayNumber: data.attendanceItemDisplayNumber
                };
                return dto;
            }

        }

        export enum AddSubOperator {
            ADD = 0,
            SUBTRACT = 1
        }

    }
}