module nts.uk.com.view.cmm040.a.viewmodel {
    import block = nts.uk.ui.block;
    import util = nts.uk.util;
    import errors = nts.uk.ui.errors;
    export class ScreenModel {

        tabs: KnockoutObservableArray<nts.uk.ui.NtsTabPanelModel>;
        selectedTab: KnockoutObservable<string>;
        workPlacesList: KnockoutObservableArray<any> = ko.observableArray([]);
        selectedWorkLocation: KnockoutObservable<string> = ko.observable(null);
        //selectedWorkLocation: KnockoutObservable<any>;
        //
        valueA5_2: KnockoutObservable<string> = ko.observable('');
        //
        currentBonusPaySetting: KnockoutObservable<BonusPaySetting>;
        //
        itemList: KnockoutObservableArray<ItemModel2>;
        radius: KnockoutObservable<number> = ko.observable(1);
        //
        // items: KnockoutObservableArray<ItemModel>;
        //
        workLocationCD: KnockoutObservable<string>;
        workLocationName: KnockoutObservable<string>;
        //
        latitude: KnockoutObservable<number> = ko.observable(null);
        longitude: KnockoutObservable<number> = ko.observable(null);
        //
        isCreate: KnockoutObservable<boolean>;

        workPlaceID: KnockoutObservable<string> = ko.observable('');

        listWorkPlaceIDs: any = [];
        LoginCompanyId: any = '';

        loginInfo: any;
        output: any;
        items: any = [];
        listSelectWorkplaceID :  any = [];

        cdl008data: KnockoutObservableArray<any> = ko.observableArray([]);

        radiusEnum: KnockoutObservableArray<Enum>;

        enableA53: KnockoutObservable<boolean>;

        option: any;

        constructor() {
            let self = this;

            self.option = new nts.uk.ui.option.NumberEditorOption({
                numberGroup: true,
                decimallength: 6,
                textalign: "left"
            }
            )



            self.workLocationCD = ko.observable('');
            self.workLocationName = ko.observable('');
            self.currentBonusPaySetting = ko.observable(new BonusPaySetting('', '', ''));
            //
            self.isCreate = ko.observable(null);
            self.enableA53 = ko.observable(false);
            //
            self.radiusEnum = ko.observableArray([]);
            self.itemList = ko.observableArray([]);
            //            self.itemList = ko.observableArray([
            //                new ItemModel('0', 'M_50'),
            //                new ItemModel('1', 'M_100'),
            //                new ItemModel('2', 'M_200'),
            //                new ItemModel('3', 'M_300'),
            //                new ItemModel('4', 'M_400'),
            //                new ItemModel('5', 'M_500'),
            //                new ItemModel('6', 'M_600'),
            //                new ItemModel('7', 'M_700'),
            //                new ItemModel('8', 'M_800'),
            //                new ItemModel('9', 'M_900'),
            //                new ItemModel('10', 'M_1000')
            //                     
            //            ]);
            self.tabs = ko.observableArray([
                { id: 'tab-1', title: nts.uk.resource.getText("CMM040_9"), content: '.tab-content-1', enable: ko.observable(true), visible: ko.observable(true) },
                { id: 'tab-2', title: nts.uk.resource.getText("CMM040_10"), content: '.tab-content-2', enable: ko.observable(true), visible: ko.observable(true) },
                { id: 'tab-3', title: nts.uk.resource.getText("CMM040_11"), content: '.tab-content-3', enable: ko.observable(true), visible: ko.observable(true) }
            ]);
            self.selectedTab = ko.observable('tab-1');
            self.selectedWorkLocation.subscribe(function(value) {
                //if (value == null || value == self.workLocationCD()) return;
                if (value == null || value == self.workLocationCD()) {
                    self.workLocationCD('');
                    self.workLocationName('');
                    self.isCreate(true);
                    errors.clearAll();
                    $("#focus").focus();

                };


                // if (value == null) return;
                self.items = [];
                self.selectWorkLocation(value);
 
            });
            self.valueA5_2.subscribe(function(value) {
                if (value == "") {
                    self.enableA53(false);
                }
                else {
                    self.enableA53(true);
                }
            });

            self.longitude.subscribe(function(value) {
                if (value.toString() == "" || (value > 180) || (value < -180)) {
                    let temp = _.find(nts.uk.ui.errors.getErrorList(), function(o) { return o.errorCode == "Msg_2161"; });
                    if (temp == null) {
                        $('#validatelong').ntsError('set', { messageId: "Msg_2161" });
                    }
                }
            });

            self.latitude.subscribe(function(value) {
                if (value.toString() == "" || (value > 90) || (value < -90)) {
                    let temp = _.find(nts.uk.ui.errors.getErrorList(), function(o) { return o.errorCode == "Msg_2162"; });
                    if (temp == null) {
                         $('#validatelat').ntsError('set', { messageId: "Msg_2162" });
                   }
                }
            });


//            self.isCreate.subscribe(function(value) {
//                if (value == true) {
//                    $("#focus").focus();
//                    errors.clearAll();
//                    return;
//
//                }
//                else {
//                    $("#focusName").focus();
//                    errors.clearAll();
//                    return;
//                }
//            });

        }

        startPage(): JQueryPromise<any> {
            var self = this;

            var dfd = $.Deferred();
            this.loadRadiusEnums().done(res => {
                console.log("getEnum");
            });
            this.reloadData().done(function() {
                $("#grid2").ntsGrid({
                    height: '400px',
                    dataSource: self.items,
                    primaryKey: 'companyCode',
                    virtualization: true,
                    virtualizationMode: 'continuous',
                    columns: [
                        { headerText: nts.uk.resource.getText('CMM040_25'), key: 'companyCode', dataType: 'string', width: '90px' },
                        { headerText: nts.uk.resource.getText('CMM040_26'), key: 'companyName', dataType: 'string', width: '140px' },
                        { headerText: nts.uk.resource.getText('CMM040_27'), key: 'open', dataType: 'string', width: '84px', unbound: true, ntsControl: 'Button' },
                        { headerText: nts.uk.resource.getText('CMM040_28'), key: 'workplaceCode', dataType: 'string', width: '160px' },
                        { headerText: nts.uk.resource.getText('CMM040_29'), key: 'workplaceName', dataType: 'string', width: '160px' }
                    ],
                    features: [{ name: 'Sorting', type: 'local' }],
                    ntsControls: [

                        { name: 'Button', text: nts.uk.resource.getText('CMM040_30'), click: function() { self.openDialogCDL008(); }, controlType: 'Button' }
                    ]
                });
                dfd.resolve();
            });


            return dfd.promise();
        }
        //tab3
        private reloadData(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            /** Get list TitleMenu*/
            service.getDataStart().done(function(data) {

                self.workPlacesList(data.listWorkLocationDto);
                self.LoginCompanyId = data.companyId;
                if (data.listWorkLocationDto.length) {
                    let i;
                    let listCidAndWorkplace = [];
                    let listWorkplace = [];
                    //let listWorkplaceS = data.listWorkLocationDto;
                    if (self.workLocationCD() == "") {

                        let result = data.listWorkLocationDto[0];
                        let getWplLogin = _.filter(result.listWorkplace, function(o) { return o.companyId == self.LoginCompanyId });
                        for (let i = 0; i < getWplLogin.length; i++) {
                            self.listWorkPlaceIDs.push({ companyId: self.LoginCompanyId, workpalceId: getWplLogin[i].workpalceId });
                        }
                        self.workLocationCD(result.workLocationCD);
                        self.workLocationName(result.workLocationName);
                        self.radius(result.radius);
                        self.latitude(result.latitude);
                        self.longitude(result.longitude);
                        self.selectedWorkLocation(self.workLocationCD());
                        self.isCreate(false);
                    
                    }
                    //
                    let listWorkplaceS = _.filter(data.listWorkLocationDto, function(o) { return o.workLocationCD == self.workLocationCD() });
                    for (i = 0; i < listWorkplaceS.length; i++) {
                        for (j = 0; j < listWorkplaceS[i].listWorkplace.length; j++) {
                            listWorkplace.push({
                                companyId: listWorkplaceS[i].listWorkplace[j].companyId,
                                workpalceId: listWorkplaceS[i].listWorkplace[j].workpalceId
                            }
                            );
                        }
                        console.log(listWorkplace);
                    }
                    service.getWorkPlace(listWorkplace).done(function(data) {
                        self.loginInfo = data;
                        let list = [];
                        let datagrid = [];
                        
                        console.log(data);
                        for (i = 0; i < data.listCompany.length; i++) {

                            let list1 = _.filter(data.listCidAndWorkplaceInfo, function(o) { return o.companyId == data.listCompany[i].companyId; });
                            let workPlaceCode = '';
                            let workPlaceName = '';

                            if (list1.length > 0) {
                                for (j = 0; j < list1[0].listWorkplaceInfoImport.length; j++) {
                                    workPlaceCode += list1[0].listWorkplaceInfoImport[j].workplaceCode + '</br>';
                                    workPlaceName += list1[0].listWorkplaceInfoImport[j].workplaceDisplayName + '</br>';
                                }
                            }
                            datagrid.push(new GridItem(data.listCompany[i].companyCode, data.listCompany[i].companyName, workPlaceCode, workPlaceName));
                        }
                         self.items = datagrid;
                        dfd.resolve();
                    });
                } else {
                    self.selectWorkLocation(null);
                    self.isCreate(true);
                    let listWorkplace = [{ companyId: __viewContext.user.companyId, workpalceId: '' }];
                    service.getWorkPlace(listWorkplace).done(function(data) {
                        self.loginInfo = data;
                        let list = [];
                        let datagrid = [];
                        console.log(data);
                        for (i = 0; i < data.listCompany.length; i++) {

                            let list1 = _.filter(data.listCidAndWorkplaceInfo, function(o) { return o.companyId == data.listCompany[i].companyId; });
                            let workPlaceCode = '';
                            let workPlaceName = '';

                            if (list1.length > 0) {
                                for (j = 0; j < list1[0].listWorkplaceInfoImport.length; j++) {
                                    workPlaceCode += list1[0].listWorkplaceInfoImport[j].workplaceCode + '</br>';
                                    workPlaceName += list1[0].listWorkplaceInfoImport[j].workplaceDisplayName + '</br>';
                                }
                            }
                          datagrid.push(new GridItem(data.listCompany[i].companyCode, data.listCompany[i].companyName, workPlaceCode, workPlaceName));
                        }
                        self.items = datagrid;
                        dfd.resolve();
                    });
                    $("#focus").focus();
                }
            }).fail(function(error) {
                dfd.fail();
                nts.uk.ui.dialog.alertError(error.message);
            });

            return dfd.promise();
        }

        private selectWorkLocation(workLocationCD: string) {
            errors.clearAll();
            let self = this;
            self.isCreate(false);
            let data = _.find(self.workPlacesList(), ['workLocationCD', workLocationCD]);
            $("#grid2").remove();
            $("#grid").append("<table id='grid2'></table>");
            self.listWorkPlaceIDs = [];
            if (workLocationCD != null && workLocationCD != "") {
                self.isCreate(false);
                self.workLocationCD(data.workLocationCD);
                self.workLocationName(data.workLocationName);
                self.radius(data.radius);
                self.latitude(data.latitude);
                self.longitude(data.longitude);
                let listWorkplace = [];
                let list1 = _.find(self.workPlacesList(), function(o) { return o.workLocationCD == workLocationCD; });
                if ( list1!=null) {
                    for (let j = 0; j < list1.listWorkplace.length; j++) {
                        listWorkplace.push({
                            companyId: list1.listWorkplace[j].companyId,
                            workpalceId: list1.listWorkplace[j].workpalceId
                        }
                        );
                    }
                }
                else {
                    listWorkplace.push({
                        companyId: __viewContext.user.companyId,
                        workpalceId: ''
                    });
                }
                if (listWorkplace.length > 0) {

                    let getWplLogin = _.filter(listWorkplace, function(o) { return o.companyId == self.LoginCompanyId });
                    for (let i = 0; i < getWplLogin.length; i++) {
                        self.listWorkPlaceIDs.push({ companyId: self.LoginCompanyId, workpalceId: getWplLogin[i].workpalceId });
                    }
                    service.getWorkPlace(listWorkplace).done(function(data) {
                        self.loginInfo = data;
                        let list = [];
                        let datagrid = [];
                        self.listSelectWorkplaceID = [];
                        console.log(data);
                        if(data.listCidAndWorkplaceInfo.length > 0){
                            for(let i = 0;i<data.listCidAndWorkplaceInfo.length;i++){
                                if(data.listCidAndWorkplaceInfo[i].companyId == __viewContext.user.companyId){
                                    for (let k = 0; k < data.listCidAndWorkplaceInfo[i].listWorkplaceInfoImport.length; k++) {
                                        self.listSelectWorkplaceID.push(data.listCidAndWorkplaceInfo[i].listWorkplaceInfoImport[k].workplaceId);
                                    }  
                                    break; 
                                }
                            }
                        }
                        for (let i = 0; i < data.listCompany.length; i++) {

                            let list1 = _.filter(data.listCidAndWorkplaceInfo, function(o) { return o.companyId == data.listCompany[i].companyId; });
                            let workPlaceCode = '';
                            let workPlaceName = '';

                            if (list1.length > 0) {
                                for (let j = 0; j < list1[0].listWorkplaceInfoImport.length; j++) {
                                    workPlaceCode += list1[0].listWorkplaceInfoImport[j].workplaceCode + '</br>';
                                    workPlaceName += list1[0].listWorkplaceInfoImport[j].workplaceDisplayName + '</br>';
                                }
                            }
                            datagrid.push(new GridItem(data.listCompany[i].companyCode, data.listCompany[i].companyName, workPlaceCode, workPlaceName));
                        }
                         self.items = datagrid;
                        $("#grid2").ntsGrid({
                            height: '400px',
                            dataSource: self.items,
                            primaryKey: 'companyCode',
                            virtualization: true,
                            virtualizationMode: 'continuous',
                            columns: [
                                { headerText: nts.uk.resource.getText('CMM040_25'), key: 'companyCode', dataType: 'string', width: '90px' },
                                { headerText: nts.uk.resource.getText('CMM040_26'), key: 'companyName', dataType: 'string', width: '140px' },
                                { headerText: nts.uk.resource.getText('CMM040_27'), key: 'open', dataType: 'string', width: '84px', unbound: true, ntsControl: 'Button' },
                                { headerText: nts.uk.resource.getText('CMM040_28'), key: 'workplaceCode', dataType: 'string', width: '160px' },
                                { headerText: nts.uk.resource.getText('CMM040_29'), key: 'workplaceName', dataType: 'string', width: '160px' }
                            ],
                            features: [{ name: 'Sorting', type: 'local' }],
                            ntsControls: [

                                { name: 'Button', text: nts.uk.resource.getText('CMM040_30'), click: function() { self.openDialogCDL008(); }, controlType: 'Button' }
                            ]
                        });
                    });
                       $("#focusName").focus();
                }
            }
            else {
                self.isCreate(true);
                self.workLocationCD('');
                self.workLocationName('');
                self.radius(0);
                self.longitude('0.000000');
                self.latitude('0.000000');
                self.selectedWorkLocation(null);
                 $("#focus").focus();
                errors.clearAll();
                
            }
        }
        //
        private findByIndex(index: number) {
            let self = this
            let data = _.nth(self.workPlacesList(), index);
            if (data !== undefined) {
                self.selectedWorkLocation(data.workLocationCD);
            }
            else {
                self.selectedWorkLocation(null);
            }
        }

        openB(): any {
            let self = this;
            let param = {
                workLocationCD: self.workLocationCD(),
                workLocationName: self.workLocationName()
            }
            nts.uk.ui.windows.setShared("CMM040B", param);
            nts.uk.ui.windows.sub.modal("/view/cmm/040/b/index.xhtml", { dialogClass: "no-close" }).onClosed(() => { });
        }

        buttonA5_3(): any {
            let self = this;
            let url = "https://www.google.co.jp/maps/place/" + self.valueA5_2();
            window.open(url);
        }

        newMode(): any {
            let self = this;
            errors.clearAll();
            self.isCreate(true);
            self.workLocationCD('');
            self.workLocationName('');
            self.valueA5_2('');
            self.radius(0);
            self.latitude('0.000000');
            self.longitude('0.000000');
            self.selectedWorkLocation(null);
            self.listSelectWorkplaceID = [];
            $("#grid2").remove();
            $("#grid").append("<table id='grid2'></table>");
            self.items = [];
            let list1 = _.filter(self.loginInfo.listCompany, function(o) { return o.companyId == __viewContext.user.companyId; });
            self.items.push(new GridItem(list1[0].companyCode, list1[0].companyName, '', ''));
            $("#grid2").ntsGrid({
                height: '400px',
                dataSource: self.items,
                primaryKey: 'companyCode',
                virtualization: true,
                virtualizationMode: 'continuous',
                columns: [
                    { headerText: nts.uk.resource.getText('CMM040_25'), key: 'companyCode', dataType: 'string', width: '90px' },
                    { headerText: nts.uk.resource.getText('CMM040_26'), key: 'companyName', dataType: 'string', width: '140px' },
                    { headerText: nts.uk.resource.getText('CMM040_27'), key: 'open', dataType: 'string', width: '84px', unbound: true, ntsControl: 'Button' },
                    { headerText: nts.uk.resource.getText('CMM040_28'), key: 'workplaceCode', dataType: 'string', width: '160px' },
                    { headerText: nts.uk.resource.getText('CMM040_29'), key: 'workplaceName', dataType: 'string', width: '160px' }
                ],
                features: [{ name: 'Sorting', type: 'local' }],
                ntsControls: [

                    { name: 'Button', text: nts.uk.resource.getText('CMM040_30'), click: function() { self.openDialogCDL008(); }, controlType: 'Button' }
                ]
            });
            $("#focus").focus();
        }

        add() {
            let self = this;
            $(".nts-input").trigger("validate");
            if (!$(".nts-input").ntsError("hasError")) {
                let listWorkplace = [];
                self.cdl008data();

                let param = {
                    workLocationCD: self.workLocationCD(),
                    workLocationName: self.workLocationName(),
                    radius: self.radius(),
                    latitude: self.latitude(),
                    longitude: self.longitude(),
                    listIPAddress: [],
                    listWorkplace: self.listWorkPlaceIDs
                }
           // let select = 
                if (self.isCreate() === true) {
                    service.insert(param).done((result) => {
                        nts.uk.ui.dialog.info({ messageId: "Msg_15" });
                        console.log(result);
                        self.reloadData().done(() => {
                            self.selectedWorkLocation(self.workLocationCD());
                            self.isCreate(false);
                        });
                        $("#focusName").focus();
                    }).fail((res: any) => {
                        nts.uk.ui.dialog.alert({ messageId: res.messageId }).then(() => {
                            self.startPage();
                        if(self.workPlacesList().length > 0){
                            self.findByIndex(0);
                        }
                            });
                        
                    }).always(() => {
                        block.clear();
                    });
                }
                else {
                    service.update(param).done((result) => {
                        nts.uk.ui.dialog.info({ messageId: "Msg_15" });
                        console.log(result);
                        self.reloadData().done(() => {
                            self.selectedWorkLocation(self.workLocationCD());
                        });
                        $("#focusName").focus();
                    }).fail((res: any) => {
                        nts.uk.ui.dialog.alert({ messageId: res.messageId }).then(() => {
                            let p = nts.uk.ui.errors.errorsViewModel();
                              p.option().show.subscribe(v => {
                                  if (v == false) {
                                      nts.uk.ui.errors.clearAll();
                                  }
                              });
                        self.startPage();
                        if(self.workPlacesList().length > 0){
                            self.findByIndex(0);
                        }
                        });
                    }).always(() => {
                        block.clear();
                    });
                }
            }
        }
        deleteWorkLocation(): any {
            let self = this;
            nts.uk.ui.dialog.confirm({ messageId: "Msg_18" }).ifYes(() => {
                service.deleteWorkLocation(self.workLocationCD()).done((result) => {
                    let index = _.findIndex(self.workPlacesList(), ['workLocationCD', self.workLocationCD()]);
                    index = _.min([self.workPlacesList().length - 2, index]);
                    nts.uk.ui.dialog.info({ messageId: "Msg_16" }).then(function () {
                             self.reloadData().done(() => {
                              if(index == -1){
                                  self.selectedWorkLocation(null);
                                  self.workPlacesList([]);
                                   errors.clearAll();
                                  }
                                 else{   
                        self.findByIndex(index);}
                    });
                    });
               
                }).fail((res: any) => {
                    nts.uk.ui.dialog.alert({ messageId: res.messageId }).then(function() {
                        let index = _.findIndex(self.workPlacesList(), ['workLocationCD', self.workLocationCD()]);
                        index = _.min([self.workPlacesList().length - 2, index]);
                        self.reloadData().done(() => {
                            self.findByIndex(0);
                        });
                    });
                }).always(() => {
                    block.clear();
                });

            });
        }
        buttonA5_16(): any {
            let self = this;
            $.ajax({
                url: "http://geoapi.heartrails.com/api/json?method=suggest&matching=like&keyword=%E6%96%B0%E5%AE%BF%E5%8C%BA",
                beforeSend: function(xhr) {
                    xhr.overrideMimeType("text/plain; charset=x-user-defined");
                }
            })
                .done(function(data) {
                    let result = JSON.parse(data).response.location[0];
                    self.latitude(result.y);
                    self.longitude(result.x);
                });
        }
        private loadRadiusEnums(): JQueryPromise<Array<Enum>> {
            let self = this;

            let dfd = $.Deferred();
            service.radiusEnum().done(function(res: Array<Enum>) {
                self.radiusEnum(res);
                self.itemList(res)
                dfd.resolve();
            }).fail(function(res) {
                nts.uk.ui.dialog.alertError(res.message);
            });

            return dfd.promise();
        }
        public openDialogCDL008(): void {
            let self = this;
            let workplaceIds = [];
            let listWorkplace = [];
            //let canSelected = self.canSelectWorkplaceIds() ? self.canSelectWorkplaceIds().split(',') : [];
          //  if(this.loginInfo == undefined ){
          //  let selectWorkPlace = this.loginInfo.listCidAndWorkplaceInfo[0].listWorkplaceInfoImport;
            
//            for (i = 0; i < self.listSelectWorkplaceID.length; i++) {
//                workplaceIds.push(listSelectWorkplaceID[i].workplaceId);
//
//            }
            //   }
            nts.uk.ui.windows.setShared('inputCDL008', {
                baseDate: moment(new Date()).toDate(),
                selectedCodes: self.listSelectWorkplaceID,//職場ID 選択済項目
                isMultiple: true,//選択モード
                selectedSystemType: 1,//システム区分（0：共通、1：就業、2：給与、3：人事）
                isrestrictionOfReferenceRange: true,//参照範囲の絞込
                isShowNoSelectRow: false, //未選択表示
                showNoSelection: false,//Unselection Item
            }, true);
            nts.uk.ui.windows.sub.modal('/view/cdl/008/a/index.xhtml').onClosed(function(): any {
                // Check is cancel.
                if (nts.uk.ui.windows.getShared('CDL008Cancel')) {
                    return;
                }
                //view all code of selected item 
                let output = nts.uk.ui.windows.getShared('workplaceInfor');
                self.listSelectWorkplaceID = [];
                self.listWorkPlaceIDs = [];
                for (let i = 0; i < output.length; i++) {
                    self.listWorkPlaceIDs.push({ companyId: self.LoginCompanyId, workpalceId: output[i].id });
                    self.listSelectWorkplaceID.push( output[i].id );
                }
                // self.cdl008data(output);
                let code = '';
                let name = '';
                for (i = 0; i < output.length; i++) {
                    code += output[i].code + '</br>';
                    name += output[i].name + '</br>';
                }
                //
                let row = $(document.activeElement).parents()[3].firstChild.innerText;
                $("#grid2").ntsGrid("updateRow", row, { workplaceCode: code, workplaceName: name });

            })
        }


    }
    export class WorkLocation {
        workLocationCD: string;
        workLocationName: string;
        constructor(workLocationCD: string, workLocationName: string) {
            this.workLocationCD = workLocationCD;
            this.workLocationName = workLocationName
        }
    }
    export class BonusPaySetting {
        companyId: KnockoutObservable<string>;
        name: KnockoutObservable<string>;
        code: KnockoutObservable<string>;
        constructor(companyId: string, name: string, code: string) {
            this.companyId = ko.observable(companyId);
            this.name = ko.observable(name);
            this.code = ko.observable(code);
        }
    }
    class ItemModel {
        value: string;
        fieldName: string;

        constructor(value: string, fieldName: string) {
            this.value = value;
            this.fieldName = fieldName;
        }
    }
    class GridItem {
        companyCode: string;
        companyName: string;
        workplaceCode: string;
        workplaceName: string;
        constructor(companyCode: string, companyName: string, workplaceCode: string, workplaceName: string) {
            this.companyCode = companyCode;
            this.companyName = companyName;
            this.workplaceCode = workplaceCode;
            this.workplaceName = workplaceName;
        }
    }


}