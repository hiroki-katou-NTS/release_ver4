module nts.uk.at.view.kaf018.b.viewmodel {
    import getText = nts.uk.resource.getText;
    import getShared = nts.uk.ui.windows.getShared;
    import formatDate = nts.uk.time.formatDate;
    import info = nts.uk.ui.dialog.info;
    import error = nts.uk.ui.dialog.alertError;
    import shareModel = kaf018.share.model;
    import confirm = nts.uk.ui.dialog.confirm;
    import block = nts.uk.ui.block;

    export class ScreenModel {
        tempData: Array<model.ConfStatusDta> = [];
        enable: KnockoutObservable<boolean> = ko.observable(true);
        listWorkplaceId: KnockoutObservableArray<string> = ko.observableArray([]);
        closureId: number;
        closureName: string;
        processingYm: string;
        startDate: Date;
        endDate: Date;
        isDailyComfirm: boolean;
        listEmployeeCode: Array<any>;
        listWorkplace: Array<model.WorkplaceInfor>;
        inputContent: any;
        isCheckedAll: KnockoutObservable<boolean> = ko.observable(false);
        constructor() { }

        startPage(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            block.invisible();
            let params: model.IParam = nts.uk.ui.windows.getShared('KAF018BInput');
            self.closureId = params.closureId;
            self.closureName = params.closureName;
            self.processingYm = params.processingYm;
            self.startDate = params.startDate;
            self.endDate = params.endDate;
            self.listEmployeeCode = params.listEmployeeCode;
            self.isDailyComfirm = params.isConfirmData;
            self.listWorkplace = params.listWorkplace;
            self.inputContent = params.inputContent;
            
            let obj = {
                startDate: self.startDate,
                endDate: self.endDate,
                isConfirmData: self.isDailyComfirm,
                listWorkplace: self.listWorkplace,
                listEmpCd: self.listEmployeeCode
            };
            service.getAppSttByWorkpace(obj).done(function(data: any) {
                _.forEach(data, function(item) {
                    self.tempData.push(new model.ConfStatusDta(item.workplaceId, item.workplaceName,item.enabled, item.checked, 
                            self.getRecord(item.approvedNumOfCase ? item.approvedNumOfCase : 0),
                            self.getRecord1(item.numOfApp ? item.numOfApp : 0, item.numOfUnreflected ? item.numOfUnreflected : 0),
                            self.getRecord(item.numOfUnapproval ? item.numOfUnapproval : 0),
                            self.getRecord(item.numOfDenials ? item.numOfDenials : 0)));
                })
                self.initNtsGrid(self.listHidden());
                dfd.resolve();
                block.clear();
            }).fail(function() {
                block.clear();
            })
            return dfd.promise();
        }

        sendMails() {
            var self = this;
            block.invisible();
            let confirmStatus: Array<model.UnApprovalSendMail> = [];
            _.forEach(self.tempData, function(item) {
                if (item.isChecked) {
                    confirmStatus.push(new model.UnApprovalSendMail(item.workplaceId, item.isChecked));
                }
            });

            service.getCheckSendMail(confirmStatus).done(function() {
                confirm({ messageId: "Msg_795" }).ifYes(() => {
                    block.invisible();
                    let listWkpId = [];
                    _.forEach(confirmStatus, function(item) {
                        listWkpId.push(item.workplaceId);
                    });
                    let obj = {
                        listWkpId: listWkpId,
                        closureStart: self.startDate,
                        closureEnd: self.endDate,
                        listEmpCd: self.listEmployeeCode
                    };
                    service.exeSendUnconfirmedMail(obj).done(function(result: any) {
                        shareModel.showMsgSendEmail(result);
                    }).fail(function(err) {
                        error({ messageId: err.messageId });
                    }).always(function() {
                        block.clear();
                    });
                })
            }).fail(function(err) {
                error({ messageId: err.messageId });
            }).always(function() {
                block.clear();
            });
        }

        getRecord1(value1: number, value2: number): string {
            let val2: string =  value2 > 0 ? value2 : "";
            let val1: string = value1 > 0 ? value1 + "件" : "";
            let symb = (val1 != "" && val2 != "") ? "/" : "";
            return val2 + symb + val1;
        }

        getRecord(value?: number) {
            return value ? value + "件" : "";
        }

        getTargetDate(): string {
            var self = this;
            let startDate = nts.uk.time.formatDate(new Date(self.startDate), 'yyyy/MM/dd');
            let endDate = nts.uk.time.formatDate(new Date(self.endDate), 'yyyy/MM/dd');
            return self.processingYm + "(" + startDate + " ～ " + endDate + ")";
        }

        goBackA() {
            var self = this;
            let params = {
                inputContent: self.inputContent
            };
             nts.uk.request.jump('/view/kaf/018/a/index.xhtml', params);    
        }

        gotoC(id) {
            var self = this;
            let listWorkplace = [];
            let indexs = null;
            _.each(self.tempData, function(item, index) {
                listWorkplace.push(new shareModel.ItemModel(item.workplaceId, item.workplaceName));
                if(item.workplaceId == id){
                    indexs = index;
                }
            });
            let params = {
                closureId: self.closureId,
                closureName: self.closureName,
                processingYm: self.processingYm,
                startDate: self.startDate,
                endDate: self.endDate,
                listWorkplace: listWorkplace,
                selectedWplIndex: indexs,
                listEmployeeCode: self.listEmployeeCode,
                inputContent: self.inputContent
            };
            nts.uk.request.jump('/view/kaf/018/c/index.xhtml', params);
        }
        listHidden(): Array<any>{
            let self = this;
            let lstHidden = [];
             _.each(self.tempData, function(item, index) {
                if(item.isEnabled == false){
                    lstHidden.push(item.workplaceId);
                }
            });
            return lstHidden;
        }
        initNtsGrid(lstHidden: Array<any>) {
            var self = this;
            $("#gridB").ntsGrid({
                width: '750px',
                height: '174px',
                dataSource: self.tempData,
                primaryKey: 'workplaceId',
                rowVirtualization: true,
                virtualization: true,
                hidePrimaryKey: true,
                virtualizationMode: 'continuous',
                columns: [
                    { headerText: getText('KAF018_20'), key: 'workplaceName', dataType: 'string', width: '210px', ntsControl: 'LinkLabel' },
                    { headerText: getText('KAF018_21'), key: 'numOfUnreflected', dataType: 'string', width: '100px' },
                    { headerText: getText('KAF018_22'), key: 'numOfUnapproval', dataType: 'string', width: '100px'},
                    { headerText: getText('KAF018_23'), key: 'approvedNumOfCase', dataType: 'string', width: '100px'},
                    { headerText: getText('KAF018_24'), key: 'numOfDenials', dataType: 'string', width: '100px'},
                    { headerText: 'ID', key: 'workplaceId', dataType: 'string', width: '0px', ntsControl: 'Label'},
                    { headerText: getText('KAF018_25'), key: 'isChecked', dataType: 'boolean', width: '120px', 
                            showHeaderCheckbox: true, ntsControl: 'Checkbox',  hiddenRows: lstHidden}
                ],
                features: [
                    {
                        name: 'Selection',
                        mode: 'row',
                        multipleSelection: true
                    }
                ],
                ntsControls: [{ name: 'Checkbox', options: { value: 1, text: '' }, optionsValue: 'value', optionsText: 'text', controlType: 'CheckBox' },
                    { name: 'LinkLabel' ,click: function(rowId){self.gotoC(rowId)}, controlType: 'LinkLabel' }],
            });
            $("#gridB").setupSearchScroll("igGrid", true);
            $("#gridB").focus();
        }
    }

    export module model {

        export class UnApprovalSendMail {
            workplaceId: string;
            isChecked: boolean;
            constructor(workplaceId: string, isChecked: boolean) {
                this.workplaceId = workplaceId;
                this.isChecked = isChecked;
            }
        }

        export class IParam {
            closureId: number;

            /** The closure name. */
            closureName: string;

            /** The start date. */
            startDate: Date;

            /** The end date. */
            endDate: Date;

            /** The closure date. */
            //処理年月
            closureDate: number;

            processingYm: string;

            isConfirmData: boolean;

            listEmployeeCode: Array<any>;

            listWorkplace: Array<WorkplaceInfor>;
        }

        export class WorkplaceInfor {
            // 職場ID
            code: string;

            name: string;
            constructor(code: string, name: string) {
                this.code = code;
                this.name = name;
            }
        }
        
        export class ConfStatusDta {
            workplaceId: string;
            workplaceName: string;
            isEnabled: boolean;
            isChecked: boolean;
            /**承認済件数*/
            approvedNumOfCase: string;
            /**未反映件数*/
            numOfUnreflected: string;
            /**未承認件数*/
            numOfUnapproval: string;
            /**否認件数*/
            numOfDenials: string;
            constructor(workplaceId: string, workplaceName: string,isEnabled: boolean, isChecked: boolean,
                approvedNumOfCase: any, numOfUnreflected: any, numOfUnapproval: any, numOfDenials: any) {
                this.workplaceId = workplaceId;
                this.workplaceName = workplaceName;
                this.isEnabled = isEnabled;
                this.approvedNumOfCase = approvedNumOfCase;
                this.numOfUnreflected = numOfUnreflected;
                this.numOfUnapproval = numOfUnapproval;
                this.numOfDenials = numOfDenials;
                this.isChecked = isChecked;
            }
        }
    }
}