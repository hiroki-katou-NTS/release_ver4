module nts.uk.at.view.ksu001.test1.viewmodel {
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import getText = nts.uk.resource.getText;
    import flat = nts.uk.util.flatArray;

    export class ScreenModel {
        selectedTab: KnockoutObservable<string> = ko.observable('company');
        contextMenu: Array<any>;
        dataSourceCompany: KnockoutObservableArray<any> = ko.observableArray([null, null, null, null, null, null, null, null, null, null]);
        dataSourceWorkplace: KnockoutObservableArray<any> = ko.observableArray([null, null, null, null, null, null, null, null, null, null]);
        dataSourceWorkplaceGroup: KnockoutObservableArray<any> = ko.observableArray([null, null, null, null, null, null, null, null, null, null]);
        sourceCompany: KnockoutObservableArray<any> = ko.observableArray([]);
        sourceWorkplace: KnockoutObservableArray<any> = ko.observableArray([]);
        sourceWorkplaceGroup: KnockoutObservableArray<any> = ko.observableArray([]);
        checked: KnockoutObservable<boolean> = ko.observable(false);
        textName: KnockoutObservable<string> = ko.observable(null);
        tooltip: KnockoutObservable<string> = ko.observable(null);
        selectedLinkButtonCom: KnockoutObservable<number> = ko.observable(0);
        selectedLinkButtonWkp: KnockoutObservable<number> = ko.observable(0);
        selectedLinkButtonWkpGroup: KnockoutObservable<number> = ko.observable(0);
        listComPattern: KnockoutObservableArray<any> = ko.observableArray([]);
        listWkpPattern: KnockoutObservableArray<any> = ko.observableArray([]);
        listWkpGroupPattern: KnockoutObservableArray<any> = ko.observableArray([]);
        flag: boolean = true;
        indexLinkButtonCom: number = null;
        indexLinkButtonWkp: number = null;
        indexLinkButtonWkpGroup: number = null;
        dataToStick: any = null;
        selectedButtonTableCompany: KnockoutObservable<any> = ko.observable({});
        selectedButtonTableWorkplace: KnockoutObservable<any> = ko.observable({});
        selectedButtonTableWorkplaceGroup: KnockoutObservable<any> = ko.observable({});
        indexBtnSelected: number = 0;
        treeGrid: any;
        baseDate: KnockoutObservable<Date>;
        selectedWorkplaceId: KnockoutObservable<string>;
        workplaceGridList: KnockoutObservableArray<any> = ko.observableArray([]);
        options: Option;
        workplaceGroupList: KnockoutObservable<any> = ko.observable([]);
        currentIds: KnockoutObservable<any> = ko.observable(null);
        currentCodes: KnockoutObservable<any> = ko.observable([]);
        currentNames: KnockoutObservable<any> = ko.observable([]);

        tabs: KnockoutObservableArray<nts.uk.ui.NtsTabPanelModel> = ko.observableArray([
            { id: 'company', title: getText("Com_Company"), content: '.tab-content-1', enable: ko.observable(true), visible: ko.observable(true) },
            { id: 'workplace', title: getText("Com_Workplace"), content: '.tab-content-2', enable: ko.observable(true), visible: ko.observable(true) },
            { id: 'workplaceGroup', title: "????????????", content: '.tab-content-3', enable: ko.observable(true), visible: ko.observable(true) }
        ]);

        textButtonArrComPattern: KnockoutObservableArray<any> = ko.observableArray([
            { name: ko.observable(getText("KSU001_1603", ['???'])), id: 0 },
            { name: ko.observable(getText("KSU001_1603", ['???'])), id: 1 },
            { name: ko.observable(getText("KSU001_1603", ['???'])), id: 2 },
            { name: ko.observable(getText("KSU001_1603", ['???'])), id: 3 },
            { name: ko.observable(getText("KSU001_1603", ['???'])), id: 4 },
            { name: ko.observable(getText("KSU001_1603", ['???'])), id: 5 },
            { name: ko.observable(getText("KSU001_1603", ['???'])), id: 6 },
            { name: ko.observable(getText("KSU001_1603", ['???'])), id: 7 },
            { name: ko.observable(getText("KSU001_1603", ['???'])), id: 8 },
            { name: ko.observable(getText("KSU001_1603", ['??????'])), id: 9 },
        ]);

        textButtonArrWkpPattern: KnockoutObservableArray<any> = ko.observableArray([
            { name: ko.observable(getText("KSU001_1603", ['???'])), id: 0 },
            { name: ko.observable(getText("KSU001_1603", ['???'])), id: 1 },
            { name: ko.observable(getText("KSU001_1603", ['???'])), id: 2 },
            { name: ko.observable(getText("KSU001_1603", ['???'])), id: 3 },
            { name: ko.observable(getText("KSU001_1603", ['???'])), id: 4 },
            { name: ko.observable(getText("KSU001_1603", ['???'])), id: 5 },
            { name: ko.observable(getText("KSU001_1603", ['???'])), id: 6 },
            { name: ko.observable(getText("KSU001_1603", ['???'])), id: 7 },
            { name: ko.observable(getText("KSU001_1603", ['???'])), id: 8 },
            { name: ko.observable(getText("KSU001_1603", ['??????'])), id: 9 },
        ]);

        textButtonArrWkpGroupPattern: KnockoutObservableArray<any> = ko.observableArray([
            { name: ko.observable(getText("KSU001_1603", ['???'])), id: 0 },
            { name: ko.observable(getText("KSU001_1603", ['???'])), id: 1 },
            { name: ko.observable(getText("KSU001_1603", ['???'])), id: 2 },
            { name: ko.observable(getText("KSU001_1603", ['???'])), id: 3 },
            { name: ko.observable(getText("KSU001_1603", ['???'])), id: 4 },
            { name: ko.observable(getText("KSU001_1603", ['???'])), id: 5 },
            { name: ko.observable(getText("KSU001_1603", ['???'])), id: 6 },
            { name: ko.observable(getText("KSU001_1603", ['???'])), id: 7 },
            { name: ko.observable(getText("KSU001_1603", ['???'])), id: 8 },
            { name: ko.observable(getText("KSU001_1603", ['??????'])), id: 9 },
        ]);

        constructor() {
            let self = this;

            self.baseDate = ko.observable(new Date());
            self.selectedWorkplaceId = ko.observable("");
            self.treeGrid = {
                isShowAlreadySet: false, // is show already setting column.
                isMultiSelect: false, // is multiselect.
                isShowSelectButton: false, // Show button select all and selected sub parent
                treeType: 1, // workplace tree.
                selectType: 3, // select first item.
                maxRows: 12, // maximum rows can be displayed.
                selectedId: self.selectedWorkplaceId,
                baseDate: self.baseDate,
                isDialog: false,
                systemType: 2
            };

            self.options = {
                itemList: self.workplaceGroupList,
                currentCodes: self.currentCodes,
                currentNames: self.currentNames,
                currentIds: self.currentIds,
                multiple: false,
                tabindex: 2,
                isAlreadySetting: false,
                showEmptyItem: false,
                reloadData: ko.observable(''),
                height: 373,
                selectedMode: 1
            };

            self.contextMenu = [
                { id: "openDialog", text: getText("KSU001_1705"), action: self.openDialogJB.bind(self) },
                { id: "openPopup", text: getText("KSU001_1706"), action: self.openPopup.bind(self) },
                { id: "delete", text: getText("KSU001_1707"), action: self.remove.bind(self) }
            ];
            $("#workplaceGroup").hide();
            self.selectedTab.subscribe((newValue) => {
                if (newValue === 'workplaceGroup') {
                    $("#tree-grid-screen-e").hide();
                    $("#workplaceGroup").show();
                } else {
                    $("#workplaceGroup").hide();
                    $("#tree-grid-screen-e").show();
                }
                if (newValue === 'workplace' && self.flag) {
                    self.initScreenQ();
                    self.flag = false;
                }
            });

            self.selectedButtonTableCompany.subscribe(function() {
                self.dataToStick = $("#test1").ntsButtonTable("getSelectedCells")[0] ? $("#test1").ntsButtonTable("getSelectedCells")[0].data.data : null;
                let arrDataToStick: any[] = _.map(self.dataToStick, 'data');
                $("#extable").exTable("stickData", arrDataToStick);
                self.indexBtnSelected = self.selectedButtonTableCompany().column + self.selectedButtonTableCompany().row * 10;
            });

            self.selectedButtonTableWorkplace.subscribe(function() {
                self.dataToStick = $("#test2").ntsButtonTable("getSelectedCells")[0] ? $("#test2").ntsButtonTable("getSelectedCells")[0].data.data : null;
                let arrDataToStick: any[] = _.map(self.dataToStick, 'data');
                $("#extable").exTable("stickData", arrDataToStick);
                self.indexBtnSelected = self.selectedButtonTableWorkplace().column + self.selectedButtonTableWorkplace().row * 10;
            });

            self.selectedButtonTableWorkplaceGroup.subscribe(function() {
                self.dataToStick = $("#test2").ntsButtonTable("getSelectedCells")[0] ? $("#test2").ntsButtonTable("getSelectedCells")[0].data.data : null;
                let arrDataToStick: any[] = _.map(self.dataToStick, 'data');
                $("#extable").exTable("stickData", arrDataToStick);
                self.indexBtnSelected = self.selectedButtonTableWorkplaceGroup().column + self.selectedButtonTableWorkplaceGroup().row * 10;
            });

            $("#test1").bind("getdatabutton", function(evt, data) {
                self.textName(data.text);
                self.tooltip(data.tooltip);
            });

            $("#test2").bind("getdatabutton", function(evt, data) {
                self.textName(data.text);
                self.tooltip(data.tooltip);
            });

            $("#test3").bind("getdatabutton", function(evt, data) {
                self.textName(data.text);
                self.tooltip(data.tooltip);
            });
        }

        /**
         * get content of link button
         */
        initScreenQ(): void {
            let self = this;
            $('#tree-grid-screen-e').ntsTreeComponent(self.treeGrid).done(function() {
                self.workplaceGridList($('#tree-grid-screen-e').getDataList());
                if (self.workplaceGridList().length > 0) {
                    self.selectedWorkplaceId(self.workplaceGridList()[0].workplaceId);
                }
            });
            if (self.selectedTab() === 'company') {
                //self.handleInit(self.listComPattern(), self.textButtonArrComPattern, self.dataSourceCompany, ko.observable(0));
                self.handleInit(self.listWkpPattern(), self.textButtonArrComPattern, self.dataSourceCompany, ko.observable(0));
            }
            if (self.selectedTab() === 'workplace') {
                self.handleInit(self.listWkpPattern(), self.textButtonArrWkpPattern, self.dataSourceWorkplace, ko.observable(0));
            }
            if (self.selectedTab() === 'workplaceGroup') {
                self.handleInit(self.listWkpGroupPattern(), self.textButtonArrWkpGroupPattern, self.dataSourceWorkplaceGroup, ko.observable(0));
            }
        }

        /**
         * handle init
         * change text of linkbutton
         * set data for datasource
         */
        handleInit(listPattern: any, listTextButton: any, dataSource: any, index: any): any {
            let self = this;
            //set default for listTextButton and dataSource
            listTextButton([
                { name: ko.observable(getText("KSU001_1603", ['???'])), id: 0 },
                { name: ko.observable(getText("KSU001_1603", ['???'])), id: 1 },
                { name: ko.observable(getText("KSU001_1603", ['???'])), id: 2 },
                { name: ko.observable(getText("KSU001_1603", ['???'])), id: 3 },
                { name: ko.observable(getText("KSU001_1603", ['???'])), id: 4 },
                { name: ko.observable(getText("KSU001_1603", ['???'])), id: 5 },
                { name: ko.observable(getText("KSU001_1603", ['???'])), id: 6 },
                { name: ko.observable(getText("KSU001_1603", ['???'])), id: 7 },
                { name: ko.observable(getText("KSU001_1603", ['???'])), id: 8 },
                { name: ko.observable(getText("KSU001_1603", ['??????'])), id: 9 },
            ]);
            dataSource([null, null, null, null, null, null, null, null, null, null]);

            for (let i = 0; i < listPattern.length; i++) {
                let source: any[] = [{}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}];
                //change text of linkbutton
                listTextButton()[listPattern[i].groupNo - 1].name(nts.uk.text.padRight(listPattern[i].groupName, ' ', 6));
                //set data for dataSource
                _.each(listPattern[i].patternItem, (pattItem) => {
                    let text = pattItem.patternName;
                    let arrPairShortName = [], arrPairObject: any = [];
                    _.forEach(pattItem.workPairSet, (wPSet) => {
                        let workType = null, workTime = null, pairShortName = null;
                        workType = _.find(__viewContext.viewModel.viewO.listWorkType(), { 'workTypeCode': wPSet.workTypeCode });
                        let workTypeShortName = workType.abbreviationName;
                        workTime = _.find(__viewContext.viewModel.viewO.listWorkTime(), { 'workTimeCode': wPSet.workTimeCode });
                        let workTimeShortName = workTime ? workTime.abName : null;
                        pairShortName = workTimeShortName ? '[' + workTypeShortName + '/' + workTimeShortName + ']' : '[' + workTypeShortName + ']';
                        arrPairShortName.push(pairShortName);
                        arrPairObject.push({
                            index: wPSet.pairNo,
                            data: {
                                workTypeCode: workType.workTypeCode,
                                workTypeName: workType.name,
                                workTimeCode: workTime ? workTime.workTimeCode : null,
                                workTimeName: workTime ? workTime.name : null,
                                startTime: (workTime && workTime.timeNumberCnt == 1) ? workTime.startTime : '',
                                endTime: (workTime && workTime.timeNumberCnt == 1) ? workTime.endTime : '',
                                symbolName: null
                            }
                        });
                    });
                    let arrDataOfArrPairObject: any = [];
                    _.each(arrPairObject, (data) => {
                        arrDataOfArrPairObject.push(data.data);
                    });
                    //set symbol for arrPairObject
                    //                    $.when(__viewContext.viewModel.viewA.setDataToDisplaySymbol(arrDataOfArrPairObject)).done(() => {
                    __viewContext.viewModel.viewA.setDataToDisplaySymbol(arrDataOfArrPairObject)
                    // set tooltip
                    let arrTooltipClone = _.clone(arrPairShortName);
                    for (let i = 7; i < arrTooltipClone.length; i += 7) {
                        arrPairShortName.splice(i, 0, 'lb');
                        i++;
                    }
                    let tooltip: string = arrPairShortName.join('???');
                    tooltip = tooltip.replace(/???lb/g, '\n');

                    //insert data to source
                    source.splice(pattItem.patternNo - 1, 1, { text: text, tooltip: tooltip, data: arrPairObject });
                    //                });
                });
                dataSource().splice(listPattern[i].groupNo - 1, 1, source);
            }

            self.clickLinkButton(null, index);
        }

        /**
         * Click to link button
         */
        clickLinkButton(element: any, index?: any): void {
            let self = this,
                source: any[] = [{}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}];

            if (self.selectedTab() === 'company') {
                self.indexLinkButtonCom = index();
                // link button has color gray when clicked
                _.each($('#part-1-1 a.hyperlink'), (a) => {
                    $(a).removeClass('color-gray');
                });
                $($('#part-1-1 a.hyperlink')[self.indexLinkButtonCom]).addClass('color-gray');
                self.selectedLinkButtonCom(self.indexLinkButtonCom);
                //set sourceCompany
                self.sourceCompany(self.dataSourceCompany()[self.indexLinkButtonCom] || source);
            }
            if (self.selectedTab() === 'workplace') {
                self.indexLinkButtonWkp = index();
                // link button has color gray when clicked
                _.each($('#part-1-2 a.hyperlink'), (a) => {
                    $(a).removeClass('color-gray');
                });
                $($('#part-1-2 a.hyperlink')[self.indexLinkButtonWkp]).addClass('color-gray');
                self.selectedLinkButtonWkp(self.indexLinkButtonWkp);
                //set sourceWorkplace
                self.sourceWorkplace(self.dataSourceWorkplace()[self.indexLinkButtonWkp] || source);
            }
            if (self.selectedTab() === 'workplaceGroup') {
                self.indexLinkButtonWkpGroup = index();
                // link button has color gray when clicked
                _.each($('#part-1-3 a.hyperlink'), (a) => {
                    $(a).removeClass('color-gray');
                });
                $($('#part-1-3 a.hyperlink')[self.indexLinkButtonWkpGroup]).addClass('color-gray');
                self.selectedLinkButtonWkpGroup(self.indexLinkButtonWkpGroup);
                //set sourceWorkplaceGroup
                self.sourceWorkplaceGroup(self.dataSourceWorkplaceGroup()[self.indexLinkButtonWkpGroup] || source);
            }
        }

        /**
         * Open popup to change name button
         */
        openPopup(button): JQueryPromise<any> {
            let self = this, dfd = $.Deferred();
            $("#test2").trigger("getdatabutton", { text: button[0].innerText });
            $("#popup-area").css('visibility', 'visible');
            let buttonWidth = button.outerWidth(true) - 30;
            $("#popup-area").position({ "of": button, my: "left+" + buttonWidth + " top", at: "left+" + buttonWidth + " top" });
            $("#test2").bind("namechanged", function(evt, data) {
                $("#test2").unbind("namechanged");
                if (!nts.uk.util.isNullOrUndefined(data)) {
                    dfd.resolve(data);
                } else {
                    dfd.resolve(button.parent().data("cell-data"));
                }
                self.refreshDataSource();
            });
            return dfd.promise();
        }

        /**
         * decision change name button
         */
        decision(): void {
            let self = this;
            $("#popup-area").css('visibility', 'hidden');
            $("#test2").trigger("namechanged", { text: self.textName(), tooltip: self.tooltip() });
        }

        /**
         * Close popup
         */
        closePopup(): void {
            nts.uk.ui.errors.clearAll()
            $("#popup-area").css('visibility', 'hidden');
            $("#test2").trigger("namechanged", undefined);
        }

        /**
         * Open dialog JA
         */
        openDialogJA(): JQueryPromise<any> {
            let self = this, dfd = $.Deferred();
            if ((self.selectedTab() === 'workplace' || self.selectedTab() === 'company') && self.selectedWorkplaceId() == null) {
                nts.uk.ui.dialog.alertError({ messageId: 'Msg_1197' });
                return;
            }
            var lwps = $('#tree-grid-screen-e').getDataList();
            var rstd_1 = $('#tree-grid-screen-e').getRowSelected();
            var flwps = flat(_.cloneDeep(lwps), "children");
            var wkp;
            if (self.selectedTab() !== 'workplaceGroup') {
                wkp = _.find(flwps, function(wkp) { return wkp.id == _.head(rstd_1).id; });
            }
            setShared('dataForJB', {
                selectedTab: self.selectedTab(),
                workplaceCode: self.selectedTab() === 'workplaceGroup' ? self.currentCodes() : rstd_1[0].code,
                workplaceName: self.selectedTab() === 'workplaceGroup' ? self.currentNames() : wkp ? wkp.name : '',
                workplaceId: self.selectedTab() === 'company' ? null : self.selectedTab() === 'workplace' ? self.selectedWorkplaceId() : self.currentIds(),
                listWorkType: __viewContext.viewModel.viewO.listWorkType(),
                listWorkTime: __viewContext.viewModel.viewO.listWorkTime(),
                selectedLinkButton: self.selectedTab() === 'company' ? self.selectedLinkButtonCom()
                    : self.selectedTab() === 'workplace' ? self.selectedLinkButtonWkp() : self.selectedLinkButtonWkpGroup(),
                // listCheckNeededOfWorkTime for JA to JA send to JB
                listCheckNeededOfWorkTime: __viewContext.viewModel.viewA.listCheckNeededOfWorkTime()
            });
            nts.uk.ui.windows.sub.modal("/view/ksu/001/jb/index.xhtml").onClosed(() => {
                let selectedLB: any = ko.observable(getShared("dataFromJA").selectedLinkButton);
                if (self.selectedTab() == 'company') {
                    $.when(__viewContext.viewModel.viewA.getDataComPattern()).done(() => {
                        //self.handleInit(self.listComPattern(), self.textButtonArrComPattern, self.dataSourceCompany, selectedLB);
                        self.handleInit(self.listWkpPattern(), self.textButtonArrComPattern, self.dataSourceCompany, selectedLB);
                    });
                }
                if (self.selectedTab() == 'workplace') {
                    $.when(__viewContext.viewModel.viewA.getDataWkpPattern()).done(() => {
                        self.handleInit(self.listWkpPattern(), self.textButtonArrWkpPattern, self.dataSourceWorkplace, selectedLB);
                    });
                }
                if (self.selectedTab() == 'workplaceGroup') {
                    $.when(__viewContext.viewModel.viewA.getDataWkpPattern()).done(() => {
                        self.handleInit(self.listWkpGroupPattern(), self.textButtonArrWkpGroupPattern, self.dataSourceWorkplaceGroup, selectedLB);
                    });
                }

                dfd.resolve(undefined);
            });
            return dfd.promise();
        }

        /**
         * Open dialog JB
         */
        openDialogJB(evt, data): JQueryPromise<any> {
            let self = this, dfd = $.Deferred();

            self.textName(data ? data.text : null);
            self.tooltip(data ? data.tooltip : null);
            setShared("dataForJB", {
                text: self.textName(),
                tooltip: self.tooltip(),
                data: data ? data.data : null,
                textDecision: getText("KSU001_924"),
                listCheckNeededOfWorkTime: __viewContext.viewModel.viewA.listCheckNeededOfWorkTime()
            });
            nts.uk.ui.windows.sub.modal("/view/ksu/001/jb/index.xhtml").onClosed(() => {
                let data = getShared("dataFromJB");
                if (data) {
                    self.textName(data.text);
                    self.tooltip(data.tooltip);
                    let dataBasicSchedule = _.map(data.data, 'data');
                    //set symbol for object
                    $.when(__viewContext.viewModel.viewA.setDataToDisplaySymbol(dataBasicSchedule)).done(() => {
                        dfd.resolve({ text: self.textName(), tooltip: self.tooltip(), data: data.data });
                        self.refreshDataSource();
                        // neu buttonTable do dang dc select, set lai data cho dataToStick 
                        if (self.indexBtnSelected == $(evt).attr('data-idx')) {
                            $("#extable").exTable("stickData", dataBasicSchedule);
                        }
                    });
                }
            });
            return dfd.promise();
        }

        /**
         * remove button on table
         */
        remove(): JQueryPromise<any> {
            let self = this, dfd = $.Deferred();

            setTimeout(function() {
                dfd.resolve(undefined);
                self.refreshDataSource();
            }, 10);

            return dfd.promise();
        }

        /**
         * refresh dataSource
         */
        refreshDataSource(): void {
            let self = this;
            if (self.selectedTab() === 'company') {
                self.dataSourceCompany()[self.indexLinkButtonCom] = self.sourceCompany();
            }
            if (self.selectedTab() === 'workplace') {
                self.dataSourceWorkplace()[self.indexLinkButtonWkp] = self.sourceWorkplace();
            }
            if (self.selectedTab() === 'workplaceGroup') {
                self.dataSourceWorkplaceGroup()[self.indexLinkButtonWkpGroup] = self.sourceWorkplaceGroup();
            }
        }
    }
}