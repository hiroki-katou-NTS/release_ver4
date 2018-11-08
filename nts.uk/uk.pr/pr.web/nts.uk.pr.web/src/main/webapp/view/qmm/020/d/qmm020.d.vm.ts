module nts.uk.pr.view.qmm020.d.viewmodel {

    import block = nts.uk.ui.block;
    import dialog = nts.uk.ui.dialog;
    import getText = nts.uk.resource.getText;
    import modal = nts.uk.ui.windows.sub.modal;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import model = qmm020.share.model;

    export class ScreenModel {

        listStateCorrelationHis: KnockoutObservableArray<ItemModel> =  ko.observableArray([]);
        to : KnockoutObservable<string> = ko.observable(' ～ ');
        items: KnockoutObservableArray<ItemModel> = ko.observableArray([]);
        currentSelectedHis: KnockoutObservable<any> = ko.observable();
        index: number;
        currentSelectedDep:  any;
        singleSelectedCode: any;
        columns: any;
        selectedCodes2: any;
        list: any;
        startYearMonth: KnockoutObservable<string> = ko.observable();
        mode: KnockoutObservable<number> = ko.observable(0);
        shared: KnockoutObservable<any> = ko.observable();
        setShared: KnockoutObservable<any> = ko.observable();
        getShared: KnockoutObservable<any> = ko.observable();
        modal: KnockoutObservable<any> = ko.observable();

        transferMode: KnockoutObservable<number> = ko.observable();
        enableAddHisButton:  KnockoutObservable<boolean> = ko.observable(true);
        enableEditHisButton:  KnockoutObservable<boolean> = ko.observable(true);
        isModeAddHistory: KnockoutObservable<boolean> = ko.observable();
        constructor(){
            block.invisible();
            let self = this;
            let firstHistory;
            self.shared(model);
            self.setShared(setShared);
            self.getShared(getShared);
            self.modal(modal);
            self.list = [
                {
                    CID: '000000000000-0001',
                    HIST_ID: 1,
                    DEP_ID: 1,
                    CD: 0,
                    NAME: 'Ngon Ngu Lap Trinh'
                },
                {
                    CID: '000000000000-0001',
                    HIST_ID: 2,
                    DEP_ID: 2,
                    CD: 0,
                    NAME: 'PHP'
                },
                {
                    CID: '000000000000-0001',
                    HIST_ID: 3,
                    DEP_ID: 3,
                    CD: 0,
                    NAME: 'Java'
                },
                {
                    CID: '000000000000-0001',
                    HIST_ID: 4,
                    DEP_ID: 4,
                    CD: 2,
                    NAME: 'C#'
                },

            ]
            service.getStateCorrelationHisDeparmentById().done((data)=>{
                if(data.length > 0){
                    _.forEach(data,(o)=>{
                        self.listStateCorrelationHis.push(new ItemModel(o.historyID, '', o.startYearMonth , o.endYearMonth,self.to()));
                    });
                    firstHistory = _.head(self.listStateCorrelationHis());
                    self.currentSelectedHis(firstHistory.hisId);
                }
            }).fail((err)=>{
                if(err) dialog.alertError(err);
            }).always(()=>{
                block.clear();
            });


            self.items = ko.observableArray([]);
            self.createList();
            self.currentSelectedDep = ko.observableArray([]);
            self.currentSelectedHis.subscribe((hisId)=>{
               console.dir(hisId);
            });

            self.selectedCodes2 = ko.observable([]);
            self.index = 0;
            let template = '<button class="setting" onclick="openScreenM(\'${departmentID}\')" >設定</button>${salaryCode} ';
            let template1 = '<button class="setting" onclick="openScreenM1(\'${departmentID}\')" >設定</button>${bonusCode} ';

            self.columns = ko.observableArray([{ headerText: getText('QMM020_33'), width: "450px", key: 'departmentID', dataType: "string" },
                /*{ headerText: getText('QMM020_20'), key: 'nodeText', width: "250px", dataType: "string", ntsControl: 'Button' },
                { headerText: getText('QMM020_23'), key: 'custom', width: "250px", dataType: "string", ntsControl: 'Button' }]),*/
                { headerText: getText('QMM020_23'), key: 'salaryCode', width: '100px', template: template},
                { headerText: '', key: 'salaryName', width: '100px'},
                { headerText: getText('QMM020_23'), key: 'bonusCode', width: '100px', template: template1},
                { headerText: '', key: 'bonusName', width: '100px'},
                ]);

        }
        openScreenL(){
            let self = this;
            modal("/view/qmm/020/l/index.xhtml").onClosed(()=>{
                location.reload();
            });
        }

        openScreenJ(){
            let self = this;
            let hisId,startYearMonth, endYearMonth,temp;
            let rs = _.find(self.listStateCorrelationHis(),{hisId: self.currentSelectedHis()});
            setShared(model.PARAMETERS_SCREEN_J.INPUT, {
                startYearMonth : rs.startYearMonth,
                isPerson: false,
                modeScreen: model.MODE_SCREEN.EMPLOYEE,
            });
            modal("/view/qmm/020/j/index.xhtml").onClosed(()=>{
                let params = getShared(model.PARAMETERS_SCREEN_J.OUTPUT);
                if(params){
                    hisId = HISTORY.NEW;
                    startYearMonth = params.start;
                    self.startYearMonth(startYearMonth);
                    endYearMonth = Number(startYearMonth.toString().slice(4, 6)) == 1 ? (startYearMonth - 89) : (startYearMonth - 1);
                    if(self.listStateCorrelationHis().length > 0){
                        temp = _.head(self.listStateCorrelationHis());
                        temp.endYearMonth = endYearMonth;
                        temp.changeDisplay();
                        self.listStateCorrelationHis.unshift(new ItemModel(hisId, '', startYearMonth , 999912,self.to()));
                        self.isModeAddHistory(true);
                        self.listStateCorrelationHis(self.listStateCorrelationHis());
                    }else{
                        self.listStateCorrelationHis.push(new ItemModel(hisId, '', startYearMonth, 999912,self.to()));
                        self.isModeAddHistory(true);
                        self.listStateCorrelationHis(self.listStateCorrelationHis());
                    }

                    self.transferMode(params.transferMethod);
                    self.currentSelectedHis(HISTORY.NEW);
                }
            });
        }

        openScreenK(){
            modal("/view/qmm/020/k/index.xhtml");
        }
        createList(){
            let self = this;
            let node = new Node('0000' + 0, 'TaiTT', '01','aaaaaaa','02','ffffffff',[]);
            self.createGrisList(self.list,0,node);
            self.items.push(node);
        }
        createGrisList(data:any, parrent_id:number, node: Node){
            let self = this;
            _.forEach(data,(o)=>{
                if(parrent_id === o.CD){
                    let level = new Node('0000' + o.DEP_ID, 'Tai',o.DEP_ID, null,null,null,[]);
                    node.childs.push(level);
                    self.createGrisList(self.list,o.DEP_ID,level);
                }
            });
        }

        registerStateCorrelationHisDeparment(){
            block.invisible();
            let self = this;
            let historyID;
            if(self.mode() === MODE.NEW){
                historyID = nts.uk.util.randomId();
            }else if(self.mode() === MODE.UPDATE){
                historyID = self.currentSelectedHis();
            }
            let list1: Array<IStateLinkSettingMaster> = [];
            self.convertToCommand(historyID,self.items(),list1);

            let data = {
                listStateLinkSettingMasterCommand : list1,
                stateLinkSettingDateCommand : {
                    historyID: historyID ,
                    date: '2018/03/15'
                },
                stateCorrelationHisDeparmentCommand :{
                    cid: '',
                    historyID: historyID,
                    startYearMonth: self.startYearMonth(),
                    endYearMonth: 999912                },
                mode: self.mode()
            };
            service.registerStateCorrelationHisDeparment(data).done(()=>{
                dialog.info({ messageId: "Msg_15" }).then(() => {

                });
            }).fail((err)=>{
                if(err) dialog.alertError(err);
            }).always(()=>{
                block.clear();
            });

        }
        convertToCommand(hisID:string,data:any,list: Array<IStateLinkSettingMaster>){
           let self = this;
            _.forEach(data,(o) =>{

               let i = {
                   hisId: hisID,
                    masterCode: o.departmentID,
                    salaryCode: '01',
                    bonusCode: '02',
               };
                let temp = new StateLinkSettingMaster(i);
                list.push(temp);
                if(o.childs.length == 0) return;
                self.convertToCommand(hisID,o.childs,list);
            });
        }

        searchByDepID(data:any,depID:string,salaryCode:string,bonusCode:string){
            let self = this;
            _.forEach(data,(o)=>{
                if(o.departmentID === depID){
                    o.salaryCode = salaryCode;
                    o.bonusCode = bonusCode;
                    return;
                }
                self.searchByDepID(o.childs,depID,salaryCode,bonusCode);
            });
        }

        convertYearMonthToDisplayYearMonth(yearMonth) {
            return nts.uk.time.formatYearMonth(yearMonth);
        }



    }

    export class ItemModel {
        hisId: string;
        name: string;
        display: string;
        startYearMonth: number;
        endYearMonth: number;
        to: string;
        constructor(hisId: string, name: string, startYearMonth: number,endYearMonth: number, to: string) {
            this.hisId = hisId;
            this.name = name;
            this.startYearMonth = startYearMonth;
            this.endYearMonth = endYearMonth;
            this.to = to;
            this.display = this.convertYearMonthToDisplayYearMonth(startYearMonth)+ this.to + this.convertYearMonthToDisplayYearMonth(endYearMonth);
        }
        changeDisplay(){
            this.display = this.convertYearMonthToDisplayYearMonth(this.startYearMonth)+ this.to + this.convertYearMonthToDisplayYearMonth(this.endYearMonth);
        }

        convertYearMonthToDisplayYearMonth(yearMonth) {
            return nts.uk.time.formatYearMonth(yearMonth);
        }
    }

    class Node {
        departmentID: string;
        departmentName: string;
        display: string;
        salaryCode: string;
        salaryName: string;
        bonusCode: string;
        bonusName: string;
        childs: Array<Node>;
        constructor(departmentID: string,
                    departmentName: string,
                    salaryCode:string,
                    salaryName: string,
                    bonusCode:string,
                    bonusName:string,
                    childs: Array<Node>) {
            this.departmentID = departmentID;
            this.departmentName = departmentName;
            this.salaryCode = salaryCode;
            this.salaryName = salaryName;
            this.bonusCode = bonusCode;
            this.bonusName = bonusName;
            this.childs = childs;
        }
    }

    export interface IStateLinkSettingMaster {
        hisId: string;
        masterCode: string;
        salaryCode: number;
        bonusCode: number;
    }
    export class StateLinkSettingMaster {
        hisId: string;
        masterCode: string;
        salaryCode: number;
        bonusCode: number;
        constructor(params: IStateLinkSettingMaster) {
            this.hisId = params ? params.hisId : null;
            this.masterCode = params ? params.masterCode : null;
            this.salaryCode = params ? params.salaryCode : null;
            this.bonusCode = params ? params.bonusCode : null;
        }
    }
    export enum MODE{
        NEW = 0,
        UPDATE = 1,
    }

    export enum HISTORY{
        NEW = '0'
    }
}



let openScreenM = function(id: string) {

    let model = __viewContext.viewModel.viewmodelD;
    let rs = _.find(model.listStateCorrelationHis(),{hisId: model.currentSelectedHis()});

    nts.uk.ui.windows.setShared('PARAM_INPUT_SCREEN_M',{
        startYearMonth: rs.startYearMonth,
    });
    nts.uk.ui.windows.sub.modal("/view/qmm/020/m/index.xhtml").onClosed(()=>{
        let params = nts.uk.ui.windows.getShared('PARAM_OUTPUT_SCREEN_M');
        if(params){
            model.searchByDepID(model.items(),id,params.statementCode,params.statementCode);
            model.items(model.items());
        }
    });

};

let openScreenM1 = function(id: string) {
    let model = __viewContext.viewModel.viewmodelD;
    let rs = _.find(model.listStateCorrelationHis(),{hisId: model.currentSelectedHis()});
    model.setShared('PARAM_INPUT_SCREEN_M',{
        startYearMonth: rs.startYearMonth,
    });
    model.modal("/view/qmm/020/m/index.xhtml").onClosed(()=>{
        let params = model.getShared('PARAM_OUTPUT_SCREEN_M');
        if(params){
            model.searchByDepID(model.items(),id,params.statementCode,params.statementCode);
            model.items(model.items());
        }
    });
};