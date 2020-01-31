module jhn001.a.viewmodel {
    import modal = nts.uk.ui.windows.sub.modal;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import showDialog = nts.uk.ui.dialog;
    import text = nts.uk.resource.getText;
    import lv = nts.layout.validate;
    import format = nts.uk.text.format;
    import vc = nts.layout.validation;
    import subModal = nts.uk.ui.windows.sub.modal;
    import hasError = nts.uk.ui.errors.hasError;
    import info = nts.uk.ui.dialog.info;
    import alert = nts.uk.ui.dialog.alert;
    import confirm = nts.uk.ui.dialog.confirm;

    const __viewContext: any = window['__viewContext'] || {},
        block = window["nts"]["uk"]["ui"]["block"]["grayout"],
        unblock = window["nts"]["uk"]["ui"]["block"]["clear"],
        invisible = window["nts"]["uk"]["ui"]["block"]["invisible"];


    export class ViewModel {
        layouts: KnockoutObservableArray<ILayout> = ko.observableArray([]);
        layout: KnockoutObservable<Layout> = ko.observable(new Layout()); 
        reportClsId : KnockoutObservable<string> = ko.observable('');
        
        enaGoBack : KnockoutObservable<boolean> = ko.observable(false);
        enaSave : KnockoutObservable<boolean> = ko.observable(true);
        enaSaveDraft : KnockoutObservable<boolean> = ko.observable(true);
        enaAttachedFile : KnockoutObservable<boolean> = ko.observable(true);
        enaRemove : KnockoutObservable<boolean> = ko.observable(true);
        
        listItemDf = [];
        missingDocName = '';
        reportIdFromJhn003 = null;
        
        reportColums: KnockoutObservableArray<any> = ko.observableArray([
            { headerText: '', key: 'id', width: 0, hidden: true },
            { headerText: text('JHN001_A221_4_1'), key: 'reportCode', width: 80, hidden: false },
            { headerText: text('JHN001_A221_4_2'), key: 'reportName', width: 260, hidden: false, formatter: _.escape }
        ]);
        
        constructor(dataShareJhn003) {
            let self = this,
                layout = self.layout(),
                layouts = self.layouts;
            
            if (dataShareJhn003) {
                self.reportIdFromJhn003 = dataShareJhn003.reportId;
            }
            
            self.reportClsId.subscribe(id => {
                self.listItemDf = [];
                if (id) {
                    block();
                    let objReport = _.find(self.layouts(), function(o) { return o.id == id; })

                    if (objReport == undefined || objReport == null) {
                        return;
                    }
                    
                    // A1.3
                    if(objReport.regStatus != null && objReport.regStatus == 2){
                        self.enaSaveDraft(false);
                    }else{
                        self.enaSaveDraft(true);
                    }
                    
                    // A1.6
                    if(objReport.reportId == null || objReport.reportId == '' || objReport.reportId == undefined || (objReport.aprStatus != null && objReport.aprStatus != 0)){
                        self.enaRemove(false);
                    }else{
                        self.enaRemove(true);
                    }

                    let query = {
                        reportId: string = objReport.reportId,
                        reportLayoutId: number = objReport.reportClsId
                    };

                    service.getReportDetails(query).done((data: any) => {
                        if (data) {

                            lv.removeDoubleLine(data.classificationItems);
                            self.layout().listItemCls(data.classificationItems || []);
                            
                            if( data.classificationItems.length > 0 ){
                                self.setListItemDf(data.classificationItems);
                            }

                            // set sendBackComment header A222_2_1
                            layout.sendBackComment(text('JHN001_A222_2_1') + ' : ' + objReport.sendBackComment);

                            // set message header A222_1_1
                            layout.message(text('JHN001_A222_1_1') + ' : ' + data.message);

                            // set list file document
                            self.setListDocument(data.documentSampleDto);

                            _.defer(() => {
                                new vc(self.layout().listItemCls());
                            });
                            unblock();
                        } else {
                            self.layout().listItemCls.removeAll();
                            unblock();
                        }
                    }).fail(mgs => {
                        self.layout().showColor(true);
                        self.layout().listItemCls.removeAll();
                        unblock();
                    });
                }
            });
            
            self.start(self.reportIdFromJhn003);
        }
        
        setListItemDf(clsItems: any) {
            let self = this,
                itemDfs = [];
            for (let i = 0; i < clsItems.length; i++) {
                if (clsItems[i].items != undefined || clsItems[i].layoutItemType != "SeparatorLine"){
                    for (let j = 0; j < clsItems[i].items.length; j++) {
                        let item = clsItems[i].items[j];
                        let obj = {
                            categoryId: clsItems[i].personInfoCategoryID,
                            categoryCode: clsItems[i].categoryCode,
                            categoryName: clsItems[i].categoryName,
                            ctgType: clsItems[i].ctgType,
                            layoutItemType: clsItems[i].layoutItemType,
                            layoutDisOrder: clsItems[i].dispOrder,
                            dispOrder: item.dispOrder,
                            itemDefId: item.itemDefId,
                            itemCode: item.itemCode,
                            itemName: item.itemName
                        }
                        self.listItemDf.push(obj);
                    }
                }
            }
        }
        
        getListDocument(param): JQueryPromise<any> {
            let self = this,
                dfd = $.Deferred();
            var dfdGetData = service.getListDoc(param);

            block();
            $.when(dfdGetData).done((listdatafile: any) => {
                if (listdatafile) {
                    self.setListDocument(listdatafile);
                }
                unblock();
                dfd.resolve();
            });
            return dfd.promise();
        }

        setListDocument(listdatafile: any) {
            let self = this;
            var lstDoc = [];
            var missingDocName = '';
            for (var i = 0; i < listdatafile.length; i++) {
                
                if(listdatafile[i].fileName == null){
                    missingDocName = missingDocName + listdatafile[i].sampleFileName + ' 、';
                }
                 
                let obj = {
                    docName: listdatafile[i].docName,
                    ngoactruoc: '(',
                    sampleFileName: listdatafile[i].sampleFileName == null ? '' : '<a href="/shr/infra/file/storage/infor/' + listdatafile[i].fileName + '" target="_blank">' + listdatafile[i].sampleFileName + '</a>',
                    ngoacsau: ')',
                    fileName: listdatafile[i].fileName == null ? '' : '<a href="/shr/infra/file/storage/infor/' + listdatafile[i].fileName + '" target="_blank">' + listdatafile[i].fileName + '</a>',
                    cid: listdatafile[i].cid,
                    reportLayoutID: listdatafile[i].reportLayoutID,
                    docID: listdatafile[i].docID,
                    dispOrder: listdatafile[i].dispOrder,
                    requiredDoc: listdatafile[i].requiredDoc,
                    docRemarks: listdatafile[i].docRemarks,
                    sampleFileId: listdatafile[i].sampleFileId,
                    reportID: listdatafile[i].reportID,
                    fileId: listdatafile[i].fileId,
                    fileSize: listdatafile[i].fileSize
                }
                lstDoc.push(obj);
            }
            if (missingDocName != '') {
                self.missingDocName = text('JHN001_B2_3_7_1') + missingDocName.substring(0, missingDocName.length - 1);
            } else {
                self.missingDocName = text('JHN001_B2_3_7_1');
            }
            self.layout().listDocument(lstDoc);
        }
        
        getMissingDocName() {
            let self = this;
            var lstDoc = self.layout().listDocument();
            let missingDocName = '';
            
            
            
        }
        
        getListReportSaveDraft(): JQueryPromise<any> {
            let self = this,
                dfd = $.Deferred();
            var dfdGetData = service.getListReportSaveDraft();

            block();
            $.when(dfdGetData).done((listReportDarft: any) => {
                if (listReportDarft.length > 0) {
                    subModal('/view/jhn/001/b/index.xhtml', { title: '' }).onClosed(() => {
                        dataShare = getShared('CPS001B_PARAMS');
                    });
                }
                unblock();
                dfd.resolve();
            });
            return dfd.promise();
        }
        
        
        newMode(){
            let self = this;
        }

        start(reportIdFromJhn003): JQueryPromise<any> {
            let self = this,
                layout = self.layout,
                layouts = self.layouts,
                dfd = $.Deferred();
            // get all layout
            layouts.removeAll();
            service.getAll(false).done((data: Array<any>) => {
                if (data && data.length) {
                    let _data: Array<ILayout> = _.map(data, x => {
                        return {
                            id          : x.clsDto.reportClsId,
                            reportCode  : x.clsDto.reportCode,
                            reportName  : x.clsDto.reportName,
                            reportClsId : x.clsDto.reportClsId,
                            displayOrder: x.clsDto.displayOrder,
                            remark      : x.clsDto.remark,
                            memo        : x.clsDto.remark,
                            message     : x.clsDto.message,
                            reportId    : x.reportID,
                            sendBackComment : x.sendBackComment,
                            rootSateId  : x.rootSateId,
                            reportType  : x.clsDto.reportType,
                            regStatus   : x.regStatus, // Save_Draft(1) , Registration(2)
                            aprStatus   : x.aprStatus, // Not_Started(0)
                            workId : null
                            
                        }
                    });
                    _.each(_.orderBy(_data, ['displayOrder'], ['asc']), d => layouts.push(d));
                    if (_data) {
                        if (reportIdFromJhn003 == undefined || reportIdFromJhn003 == null) {
                            if(self.reportClsId() == "" || self.reportClsId() == null){
                                self.reportClsId(_data[0].reportClsId);
                            }else{
                                self.reportClsId(self.reportClsId());
                            }
                            
                        } else {
                            let objReport = _.find(_data, function(o) { return o.reportId == reportIdFromJhn003; })

                            if (objReport == undefined || objReport == null) {
                                self.reportClsId(_data[0].reportClsId);
                            }

                            self.reportClsId(objReport.reportClsId);
                        }
                    }
                } else {
                    self.createNewLayout();
                }
                
                self.getListReportSaveDraft().done(() => {});
                
                dfd.resolve();
            });
            return dfd.promise();
        }
        
        createNewLayout() {
            let self = this,
                layout = self.layout,
                layouts = self.layouts;
        }
        
        save() {
            let self = this,
                layout = self.layout,
                layouts = self.layouts,
                controls = self.layout().listItemCls();
            
            // refresh data from layout
            self.layout().outData.refresh();
            let inputs = self.layout().outData();
            
            let reportLayoutId = self.reportClsId();
            if( reportLayoutId == '' || reportLayoutId == null || reportLayoutId == undefined)
                return;
            
            let objReport = _.find(self.layouts(), function(o) { return o.id == reportLayoutId; })

            if (objReport == undefined || objReport == null) {
                return;
            }
            
            let command = { 
                inputs : inputs ,
                listItemDf: self.listItemDf,
                reportID : objReport.reportId ,
                reportLayoutID : reportLayoutId ,
                reportCode : objReport.reportCode ,
                reportName : objReport.reportName ,
                reportType : objReport.reportType ,
                sendBackComment : objReport.sendBackComment ,
                workId : objReport.workId == null ? 0 : objReport.workId,
                rootSateId: objReport.rootSateId,
                isSaveDraft : 0,
                missingDocName: self.missingDocName
            };

             // trigger change of all control in layout
            lv.checkError(controls);
            
            setTimeout(() => {
                if (hasError()) {
                    $('#func-notifier-errors').trigger('click');
                    return;
                }

                // push data layout to webservice
                block();
                service.saveData(command).done(() => {
                    info({ messageId: "Msg_15" }).then(function() {
                        self.start(null);
                    });
                }).fail((mes: any) => {
                    unblock();
                });
            }, 50);

        }
        
        saveDraft() {
            let self = this,
                layout = self.layout,
                layouts = self.layouts,
                controls = self.layout().listItemCls();
            
            // refresh data from layout
            self.layout().outData.refresh();
            let inputs = self.layout().outData();
            
            let reportLayoutId = self.reportClsId();
            if( reportLayoutId == '' || reportLayoutId == null || reportLayoutId == undefined)
                return;

            let objReport = _.find(self.layouts(), function(o) { return o.id == reportLayoutId; })

            if (objReport == undefined || objReport == null) {
                return;
            }
            
            let command = { 
                inputs : inputs ,
                listItemDf: self.listItemDf,
                reportID : objReport.reportId ,
                reportLayoutID : reportLayoutId ,
                reportCode : objReport.reportCode ,
                reportName : objReport.reportName ,
                reportType : objReport.reportType ,
                sendBackComment : objReport.sendBackComment ,
                workId : objReport.workId == null ? 0 : objReport.workId,
                rootSateId: objReport.rootSateId,
                isSaveDraft: 1,
                missingDocName: self.missingDocName
            };

             // trigger change of all control in layout
            lv.checkError(controls);
            
            setTimeout(() => {
                if (hasError()) {
                    $('#func-notifier-errors').trigger('click');
                    return;
                }

                // push data layout to webservice
                block();
                service.saveDraftData(command).done(() => {
                    info({ messageId: "Msg_15" }).then(function() {
                        self.start(null);
                    });
                }).fail((mes: any) => {
                    unblock();
                });
            }, 50);
        }
        
        attachedFile() {
            let self = this,
                layout = self.layout,
                layouts = self.layouts,
                controls = self.layout().listItemCls();
            
            // refresh data from layout
            self.layout().outData.refresh();
            let inputs = self.layout().outData();
            
            let reportLayoutId = self.reportClsId();
            if( reportLayoutId == '' || reportLayoutId == null || reportLayoutId == undefined)
                return;

            let objReport = _.find(self.layouts(), function(o) { return o.id == reportLayoutId; })

            if (objReport == undefined || objReport == null) {
                return;
            }
            
            let command = {
                inputs: inputs,
                listItemDf: self.listItemDf,
                reportID: objReport.reportId,
                reportLayoutID: reportLayoutId,
                reportCode: objReport.reportCode,
                reportName: objReport.reportName,
                reportType: objReport.reportType,
                sendBackComment: objReport.sendBackComment,
                workId: objReport.workId == null ? 0 : objReport.workId,
                rootSateId: objReport.rootSateId,
                isSaveDraft: 1,
                missingDocName: self.missingDocName
            };

            // trigger change of all control in layout
            lv.checkError(controls);
            
            let param = {
                reportId: string = objReport.reportId,
                layoutReportId: string = reportLayoutId,
                command : command
            };
            
            setShared("JHN001F_PARAMS", param );
            
            subModal('/view/jhn/001/f/index.xhtml', { title: '' }).onClosed(() => {
                console.log('test open dialog f');
                // get lại list file document
                self.getListDocument(param).done(() => {
                    unblock();
                });
            });
        }
        
        remove() {
            let self = this,
                layout = self.layout,
                layouts = self.layouts;

            if (nts.uk.ui.errors.hasError()) {
                return;
            }

            nts.uk.ui.dialog.confirm({ messageId: "Msg_18" }).ifYes(() => {
                block();
                let reportLayoutId = self.reportClsId();
                if (reportLayoutId == '' || reportLayoutId == null || reportLayoutId == undefined)
                    return;

                let objReport = _.find(self.layouts(), function(o) { return o.id == reportLayoutId; })

                if (objReport == undefined || objReport == null) {
                    return;
                }

                let objRemove = {
                    reportId: string = objReport.reportId
                };

                service.removeData(objRemove).done(() => {
                    info({ messageId: "Msg_40" }).then(function() {
                        self.reportClsId(null);
                        self.start(null);
                    });
                }).fail((mes: any) => {
                    unblock();
                });
            }).ifNo(() => { });
        }
        
        public backTopScreenTopReport(): void {
            let self = this;
            nts.uk.request.jump("hr", "/view/jhc/002/a/index.xhtml");
        }
    }

    class Layout {
        id: KnockoutObservable<string> = ko.observable('');
        mode: KnockoutObservable<TABS> = ko.observable(TABS.LAYOUT);
        showColor: KnockoutObservable<boolean> = ko.observable(false);
        outData: KnockoutObservableArray<any> = ko.observableArray([]);
        listItemCls: KnockoutObservableArray<any> = ko.observableArray([]);
        // standardDate of layout
        standardDate: KnockoutObservable<string> = ko.observable(moment.utc().format("YYYY/MM/DD"));
        
        message: KnockoutObservable<string> = ko.observable('');
        sendBackComment: KnockoutObservable<string> = ko.observable('');
        
        approvalRootState : any = ko.observableArray([]);
        listDocument : any = ko.observableArray([]);
        
        constructor() {
            let self = this;

            }
        
        clickSampleFileName() {
            let rowData: any = this;
            if (rowData.sampleFileId) {
                nts.uk.request.ajax("/shr/infra/file/storage/infor/" + rowData.sampleFileId).done(function(res) {
                    nts.uk.request.specials.donwloadFile(rowData.sampleFileId);
                });
            }
        }

        clickFileName() {
            let rowData: any = this;
            if (rowData.fileId) {
                nts.uk.request.ajax("/shr/infra/file/storage/infor/" + rowData.fileId).done(function(res) {
                    
//                    nts.uk.request.ajax("/shr/infra/file/storage/infor/" + rowData.originalName).done(function(res) {
//                        console.log(res);
//                    }).fail(function(error) {
//                       console.log(error);
//                    });
                    nts.uk.request.specials.donwloadFile(rowData.fileId);
                });
            }
        }
    }
    
    interface IItemDf {
        categoryId: string;
        categoryCode: string;
        categoryName: string;
        ctgType: number;
        layoutItemType: number;
        layoutDisOrder: number;
        dispOrder : number;
        itemDefId: string;
        itemCode : string;
        itemName: string;
    }
    
    interface ICategory {
        id: string;
        categoryCode?: string;
        categoryName?: string;
        categoryType?: IT_CAT_TYPE;
    }                       

    export enum TABS {
        LAYOUT = <any>"layout",
        CATEGORY = <any>"category"
    }

    export interface IPeregQuery {
        ctgId: string;
        ctgCd?: string;
        empId: string;
        standardDate: Date;
        infoId?: string;
    }

    export interface ILayoutQuery {
        layoutId: string;
        browsingEmpId: string;
        standardDate: Date;
    }

    export interface IPeregCommand {
        personId: string;
        employeeId: string;
        inputs: Array<IPeregItemCommand>;
    }

    export interface IPeregItemCommand {
        /** category code */
        categoryCd: string;
        /** Record Id, but this is null when new record */
        recordId: string;
        /** input items */
        items: Array<IPeregItemValueCommand>;
    }

    export interface IPeregItemValueCommand {
        definitionId: string;
        itemCode: string;
        value: string;
        'type': number;
    }

    export interface IParam {
        showAll?: boolean;
        employeeId: string;
        categoryId?: string;
    }

    export interface IEventData {
        id: string;
        iid?: string;
        tab: TABS;
        act?: string;
        ccode?: string;
        ctype?: IT_CAT_TYPE;
    }

    // define ITEM_CATEGORY_TYPE
    export enum IT_CAT_TYPE {
        SINGLE = 1, // Single info
        MULTI = 2, // Multi info
        CONTINU = 3, // Continuos history
        NODUPLICATE = 4, //No duplicate history
        DUPLICATE = 5, // Duplicate history,
        CONTINUWED = 6 // Continuos history with end date
    }

    export enum ITEM_SINGLE_TYPE {
        STRING = 1,
        NUMERIC = 2,
        DATE = 3,
        TIME = 4,
        TIMEPOINT = 5,
        SELECTION = 6
    }

    interface IPersonAuth {
        functionNo: number;
        functionName: string;
        available: boolean;
        description: string;
        orderNumber: number;
    }

    enum FunctionNo {
        No1_Allow_DelEmp = 1, // có thể delete employee ở đăng ký thông tin cá nhân
        No2_Allow_UploadAva = 2, // có thể upload ảnh chân dung employee ở đăng ký thông tin cá nhân
        No3_Allow_RefAva = 3,// có thể xem ảnh chân dung employee ở đăng ký thông tin cá nhân
        No4_Allow_UploadMap = 4, // có thể upload file bản đồ ở đăng ký thông tin cá nhân
        No5_Allow_RefMap = 5, // có thể xem file bản đồ ở đăng ký thông tin cá nhân
        No6_Allow_UploadDoc = 6,// có thể upload file điện tử employee ở đăng ký thông tin cá nhân
        No7_Allow_RefDoc = 7,// có thể xem file điện tử employee ở đăng ký thông tin cá nhân
        No8_Allow_Print = 8,  // có thể in biểu mẫu của employee ở đăng ký thông tin cá nhân
        No9_Allow_SetCoppy = 9,// có thể setting copy target item khi tạo nhân viên mới ở đăng ký mới thông tin cá nhân
        No10_Allow_SetInit = 10, // có thể setting giá trị ban đầu nhập vào khi tạo nhân viên mới ở đăng ký mới thông tin cá nhân
        No11_Allow_SwitchWpl = 11  // Lọc chọn lựa phòng ban trực thuộc/workplace trực tiếp theo bộ phận liên kết cấp dưới tại đăng ký thông tin cá nhân
    }

    interface ILicensenCheck {
        display: boolean;
        registered: number;
        canBeRegistered: number;
        maxRegistered: number;
        message: string;
        licenseKey: string;
        status: string;
    }

   
}