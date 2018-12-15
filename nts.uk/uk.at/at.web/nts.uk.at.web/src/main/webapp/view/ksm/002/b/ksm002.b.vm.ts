module ksm002.b.viewmodel {
    import flat = nts.uk.util.flatArray;
    import bService = nts.uk.at.view.ksm002.b.service;
    export class ScreenModel {
        checkBoxList: KnockoutObservableArray<CheckBoxItem> = ko.observableArray([]); 
        selectedIds: KnockoutObservableArray<number> = ko.observableArray([]); 
        yearMonthPicked: KnockoutObservable<number> = ko.observable(Number(moment(new Date()).format('YYYYMM')));
        workPlaceText: KnockoutObservable<string> = ko.observable(nts.uk.resource.getText('KSM002_61', [nts.uk.resource.getText('Com_Workplace')]));
        currentWorkPlace: KnockoutObservable<WorkPlaceObject> = ko.observable(new WorkPlaceObject('',''));
        rootList: Array<IWorkPlaceDto> = []; // list data from server
        isUpdate: KnockoutObservableArray<boolean> = ko.observableArray(false);
        fullCheckBoxItem: Array<CheckBoxItem> = [];
        firstDay: KnockoutObservable<number> = ko.observable(0);
        calendarPanel: ICalendarPanel = {
            optionDates: ko.observableArray([]),
            yearMonth: this.yearMonthPicked,
            firstDay: this.firstDay,
            startDate: 1,
            endDate: 31,
            workplaceId: this.currentWorkPlace().id,
            workplaceName: this.currentWorkPlace().name,
            eventDisplay: ko.observable(true),
            eventUpdatable: ko.observable(false),
            holidayDisplay: ko.observable(true),
            cellButtonDisplay: ko.observable(true)
        }
        kcpTreeGrid: ITreeGrid = {
            treeType: 1,
            selectType: 3,
            isDialog: false,
            isMultiSelect: false,
            isShowAlreadySet: false,
            isShowSelectButton: false,
            baseDate: ko.observable(new Date()),
            selectedWorkplaceId: this.currentWorkPlace().id,
            alreadySettingList: ko.observableArray([])
        };
        
        constructor() {
            var self = this;
            
            // get new data when year month change
            self.yearMonthPicked.subscribe(value => {
                if(!nts.uk.util.isNullOrEmpty(value)){
                    nts.uk.ui.block.invisible();
                    self.getCalendarWorkPlaceByCode()
                    .done(()=>{ nts.uk.ui.block.clear(); })
                    .fail((res) => {
                        nts.uk.ui.dialog.alertError(res.message).then(()=>{nts.uk.ui.block.clear();});
                    });
                }        
            });
            
            // calendar event handler 
            $("#calendar1").ntsCalendar("init", {
                cellClick: function(date) {
                    nts.uk.ui._viewModel.content.viewModelB.setListText(date, self.convertNumberToName(self.selectedIds()));
                },
                buttonClick: function(date) {
                    let item = _.find(self.calendarPanel.optionDates(), o => o.start == date);
                    if(nts.uk.util.isNullOrUndefined(item)){
                        nts.uk.ui._viewModel.content.viewModelB.openDialogE({
                            start: date,
                            listText: []        
                        });       
                    } else {
                        nts.uk.ui._viewModel.content.viewModelB.openDialogE(item);
                    }
                }
            });
            $('#tree-grid').ntsTreeComponent(self.kcpTreeGrid).done(()=>{
                if(!nts.uk.util.isNullOrUndefined(self.currentWorkPlace().id())&&!nts.uk.util.isNullOrEmpty(self.currentWorkPlace().id())){
                    self.currentWorkPlace().name(_.first($('#tree-grid')['getDataList']()).name);  
                }
                      
                // get new data when Work Place Code change
                self.currentWorkPlace().id.subscribe(value => {
                    nts.uk.ui.block.invisible();
                    let data: Array<any> = flat($('#tree-grid')['getDataList'](), 'childs');
                    let item = _.find(data, m => m.workplaceId == value);
                    if (item) {
                        self.currentWorkPlace().name(item.name);
                    } else {
                        self.currentWorkPlace().name('');
                    }    
                    self.getCalendarWorkPlaceByCode()
                    .done(()=>{ nts.uk.ui.block.clear(); })
                    .fail((res) => {
                        nts.uk.ui.dialog.alertError(res.message).then(()=>{nts.uk.ui.block.clear();});
                    });
                });
            });
        }
        
        /**
         * get required data
         */
        start(value: boolean){
            var self = this;  
            $('#tree-grid').focusTreeGridComponent();
            nts.uk.ui.block.invisible();
            $.when(
                self.getAllSpecDate(), 
                nts.uk.characteristics.restore("IndividualStartDay"),
                bService.getCompanyStartDay(),
                self.getSpecDateByIsUse(),
                self.getCalendarWorkPlaceByCode()
            ).done((data1, data2, data3, data4, data5)=>{            
                if(!nts.uk.util.isNullOrUndefined(data3)) { 
                    self.firstDay(data3.startDay); 
                }
                if(nts.uk.util.isNullOrEmpty(self.checkBoxList())){
                    self.openDialogC();
                }
                nts.uk.ui.block.clear(); 
            }).fail((res1,res2,res3,res4,res5) => {
                nts.uk.ui.dialog.alertError(res1.message+res2.message+res3.message+res4.message+res4.message).then(()=>{nts.uk.ui.block.clear();});
            });
        }
        
        /**
         * register button event handler
         */
        submitEventHandler(){
            var self = this;
            if(nts.uk.util.isNullOrUndefined(self.currentWorkPlace().id())||nts.uk.util.isNullOrEmpty(self.currentWorkPlace().id())){
                nts.uk.ui.dialog.alertError({ messageId: "Msg_339" }).then(()=>{nts.uk.ui.block.clear();});      
            } else {
                $(".yearMonthPicker").trigger("validate");
                if (!nts.uk.ui.errors.hasError()) {
                    if(nts.uk.util.isNullOrEmpty(self.selectedIds())){
                        nts.uk.ui.dialog.alertError({ messageId: "Msg_339" });     
                    } else {
                        if(!self.checkItemUse()) {
                            nts.uk.ui.dialog.alertError({ messageId: "Msg_139" });       
                        } else {
                            nts.uk.ui.block.invisible();
                            if(self.isUpdate()){
                                self.updateCalendarWorkPlace().done(()=>{
                                    nts.uk.ui.block.clear();        
                                }).fail((res)=>{
                                    nts.uk.ui.dialog.alertError(res.message).then(()=>{nts.uk.ui.block.clear();});  
                                }); 
                            } else {
                                self.insertCalendarWorkPlace().done(()=>{
                                    nts.uk.ui.block.clear();        
                                }).fail((res)=>{
                                    nts.uk.ui.dialog.alertError(res.message).then(()=>{nts.uk.ui.block.clear();});  
                                }); 
                            }    
                        }
                    }
                }
            }
        }
        
        /**
         * delete button event handler
         */
        removeEventHandler(){
            var self = this;
            $(".yearMonthPicker").trigger("validate");
            if (!nts.uk.ui.errors.hasError()) {
                nts.uk.ui.dialog.confirm({ messageId: 'Msg_18' }).ifYes(function(){
                    nts.uk.ui.block.invisible();
                    self.deleteCalendarWorkPlace().done(()=>{
                        nts.uk.ui.block.clear();        
                    }).fail((res)=>{
                        nts.uk.ui.dialog.alertError(res.message).then(()=>{nts.uk.ui.block.clear();});  
                    });        
                }).ifNo(function(){
                    // do nothing           
                });
            }
        }
        
        /**
         * get full selectable item
         */
        getAllSpecDate(): JQueryPromise<any>{
            var self = this;
            var dfd = $.Deferred();
            bService.getAllSpecDate().done(data=>{
                data.forEach(item => {
                    self.fullCheckBoxItem.push(new CheckBoxItem(item.specificDateItemNo, item.specificName));    
                });   
                dfd.resolve();
            }).fail(res => {
                dfd.reject(res);
            }); 
            return dfd.promise();           
        }
        
        /**
         * get selectable item
         */
        getSpecDateByIsUse(): JQueryPromise<any>{
            var self = this;
            var dfd = $.Deferred();
            bService.getSpecificDateByIsUse(1).done(data=>{
                if(!nts.uk.util.isNullOrEmpty(data)){
                    self.checkBoxList.removeAll();
                    let sortData = _.sortBy(data, o => o.specificDateItemNo);
                    let a = []
                    sortData.forEach(item => {
                        a.push(new CheckBoxItem(item.specificDateItemNo, item.specificName));    
                    });   
                    self.checkBoxList(a);
                }
                dfd.resolve();
            }).fail(res => {
                dfd.reject(res);
            });   
            return dfd.promise();          
        }
        
        /**
         * get calendar work place spec date by work place id and year month
         */
        getCalendarWorkPlaceByCode(): JQueryPromise<any>{
            var self = this;
            var dfd = $.Deferred();
//            if(!nts.uk.util.isNullOrUndefined(self.currentWorkPlace().id())&&!nts.uk.util.isNullOrEmpty(self.currentWorkPlace().id())){
                let workplaceParam = {
                    workPlaceId: '69607aa0-e0dc-4deb-92e7-5e7b965bce43',
                    workPlaceDate: moment(self.yearMonthPicked(), "YYYY/MM/01").format("YYYY/MM/DD")
                }
                bService.getCalendarWorkPlaceByCode(workplaceParam).done(data=>{
                    self.rootList = data;
                    self.calendarPanel.optionDates.removeAll();
                    let a = [];
                    if(!nts.uk.util.isNullOrEmpty(data)) {
                        data.forEach(item => {
                            let sortItemNumber = _.sortBy(item.specificDateItemNo, o => o);
                            a.push(new CalendarItem(item.specificDate, self.convertNumberToName(sortItemNumber)))                    
                        });   
                        self.isUpdate(true);
                    } else {
                        self.isUpdate(false);
                    }
                    self.calendarPanel.optionDates(a);
                    self.calendarPanel.optionDates.valueHasMutated();
                    dfd.resolve();
                }).fail(res => {
                    dfd.reject(res);
                });
//            } else dfd.resolve();
            return dfd.promise();
        }
        
        /**
         * insert calendar work place spec date
         */
        insertCalendarWorkPlace(): JQueryPromise<any>{
            var self = this;
            var dfd = $.Deferred();
            bService.insertCalendarWorkPlace(self.createCommand()).done(data=>{
                nts.uk.ui.dialog.info({ messageId: "Msg_15" });
                self.getCalendarWorkPlaceByCode().done(()=>{dfd.resolve();}).fail((res)=>{dfd.reject(res);});
            }).fail(res => {
                dfd.reject(res);
            });
            return dfd.promise();
        }
        
        /**
         * update calendar work place spec date
         */
        updateCalendarWorkPlace(): JQueryPromise<any>{
            var self = this;
            var dfd = $.Deferred();
            bService.updateCalendarWorkPlace(self.createCommand()).done(data=>{
                nts.uk.ui.dialog.info({ messageId: "Msg_15" });
                self.getCalendarWorkPlaceByCode().done(()=>{dfd.resolve();}).fail((res)=>{dfd.reject(res);});   
            }).fail(res => {
                dfd.reject(res);
            });
            return dfd.promise();
        }
        
        /**
         * delete calendar work place spec date
         */
        deleteCalendarWorkPlace(): JQueryPromise<any>{
            var self = this;
            var dfd = $.Deferred();
            bService.deleteCalendarWorkPlace({
                workPlaceId: self.currentWorkPlace().id(),
                startDate:moment(self.yearMonthPicked(), "YYYYMM").startOf('month').format('YYYY/MM/DD'), 
                endDate:moment(self.yearMonthPicked(), "YYYYMM").endOf('month').format('YYYY/MM/DD')   
            }).done(data=>{
                nts.uk.ui.dialog.info({ messageId: "Msg_16" });
                self.getCalendarWorkPlaceByCode().done(()=>{dfd.resolve();}).fail((res)=>{dfd.reject(res);});  
            }).fail(res => {
                dfd.reject(res);
            });
            return dfd.promise();
        }
        
        /**
         * open dialog C event
         */
        openDialogC() {
            var self = this;
            if(nts.uk.util.isNullOrUndefined(self.currentWorkPlace().id())){
                nts.uk.ui.dialog.alertError({ messageId: "Msg_339" }).then(()=>{nts.uk.ui.block.clear();});      
            } else {
                nts.uk.ui.windows.sub.modal("/view/ksm/002/c/index.xhtml", { title: "割増項目の設定", dialogClass: "no-close" }).onClosed(function() {
                    self.selectedIds([]);
                    nts.uk.ui.block.invisible();
                    self.getSpecDateByIsUse().done(()=>{
                        nts.uk.ui.block.clear();        
                    }).fail((res)=>{
                        nts.uk.ui.dialog.alertError(res.message).then(()=>{nts.uk.ui.block.clear();});  
                    });    
                }); 
            } 
        }
        
        /**
         * open dialog D event
         */
        openDialogD() {
            var self = this;
            if(nts.uk.util.isNullOrUndefined(self.currentWorkPlace().id())){
                nts.uk.ui.dialog.alertError({ messageId: "Msg_339" }).then(()=>{nts.uk.ui.block.clear();});      
            } else {
                nts.uk.ui.windows.setShared('KSM002_D_PARAM', 
                {
                    util: 2,
                    workplaceObj: ko.mapping.toJS(self.currentWorkPlace()),
                    startDate: Number(moment(self.yearMonthPicked().toString(),'YYYYMM').startOf('month').format('YYYYMMDD')),
                    endDate: Number(moment(self.yearMonthPicked().toString(),'YYYYMM').endOf('month').format('YYYYMMDD'))
                });
                nts.uk.ui.windows.sub.modal("/view/ksm/002/d/index.xhtml", { title: "割増項目の設定", dialogClass: "no-close" }).onClosed(function() {
                    nts.uk.ui.block.invisible();
                    self.getCalendarWorkPlaceByCode().done(()=>{
                        nts.uk.ui.block.clear();        
                    }).fail((res)=>{
                        nts.uk.ui.dialog.alertError(res.message).then(()=>{nts.uk.ui.block.clear();});  
                    });  
                });  
            }
        }
        
        /**
         * open dialog E event
         */
        openDialogE(item) {
            var self = this;
            if(nts.uk.util.isNullOrUndefined(self.currentWorkPlace().id())){
                nts.uk.ui.dialog.alertError({ messageId: "Msg_339" }).then(()=>{nts.uk.ui.block.clear();});      
            } else {
                nts.uk.ui.windows.setShared('KSM002_E_PARAM', 
                {
                    date: Number(moment(item.start).format("YYYYMMDD")),
                    selectable: _.map(self.checkBoxList(), o => o.id),
                    selecteds: self.convertNameToNumber(item.listText)
                });
                nts.uk.ui.windows.sub.modal("/view/ksm/002/e/index.xhtml", { title: "割増項目の設定", dialogClass: "no-close" }).onClosed(function() {
                    let param = nts.uk.ui.windows.getShared('KSM002E_VALUES');
                    if(param != undefined){
                        self.setListText(
                            moment(param.date.toString()).format('YYYY-MM-DD'),
                            self.convertNumberToName(param.selecteds)    
                        );
                    }  
                });  
            }
        }
        
        /**
         * setting item list event
         */
        setListText(date, data){
            var self = this;
        
            let dateData = self.calendarPanel.optionDates();
            let existItem = _.find(dateData, item => item.start == date);   
            if(existItem != undefined) {
                existItem.changeListText(data);   
            }else{
                if(!nts.uk.util.isNullOrEmpty(data)){
                    dateData.push(new CalendarItem(date, data));
                }
            } 
            self.calendarPanel.optionDates.valueHasMutated();
           
        }
        
        /**
         * check selected item is selectable
         */
        checkItemUse(): boolean {
            var self = this;
            let selectedUniqueCode = [];
            let selectableUniqueCode = _.map(self.checkBoxList(), o => o.id);
            self.calendarPanel.optionDates().forEach(item => {
                selectedUniqueCode = _.concat(selectedUniqueCode, self.convertNameToNumber(item.listText));  
            });        
            selectedUniqueCode = _.uniq(selectedUniqueCode);
            let result = 1;
            selectedUniqueCode.forEach(item => {
                if(_.includes(selectableUniqueCode,item)){
                    result*=1;   
                } else {
                    result*=0;    
                }    
            });
            if(result == 0) return false;
            else return true;
        }
        
        /**
         * create command data for insert/update
         */
        createCommand(){
            var self = this;
            let a = [];
            if(self.isUpdate()){
                // update case
                self.calendarPanel.optionDates().forEach(item => {
                    let before = _.find(self.rootList, o => o.specificDate == moment(item.start).format('YYYY/MM/DD')); 
                    if(nts.uk.util.isNullOrUndefined(before)){
                        a.push({
                            workPlaceId: self.currentWorkPlace().id(),
                            specificDate: moment(item.start).format('YYYY/MM/DD'),
                            specificDateItemNo: self.convertNameToNumber(item.listText),
                            isUpdate: false
                        });
                    } else {
                        let current = {
                            workPlaceId: self.currentWorkPlace().id(),
                            specificDate: moment(item.start).format('YYYY/MM/DD'),
                            specificDateItemNo: self.convertNameToNumber(item.listText)
                        };   
                        if(!_.isEqual(ko.mapping.toJSON(before),ko.mapping.toJSON(current))) {
                            current["isUpdate"] = true;
                            a.push(current);    
                        }
                    }
                });
            } else {
                // insert case
                self.calendarPanel.optionDates().forEach(item => {
                    a.push({
                        workPlaceId: self.currentWorkPlace().id(),
                        specificDate: moment(item.start).format('YYYY/MM/DD'),
                        specificDateItemNo: self.convertNameToNumber(item.listText)
                    })    
                });  
            }
            return a;
        }
        
        /**
         * convert list item selected from number to string
         */
        convertNumberToName(inputArray: number[]): string[]{
            var self = this;   
            let a = [];
            inputArray.forEach(item => {
                let rs = _.find(self.fullCheckBoxItem, o => {return o.id == item});
                a.push(rs.name);       
            });
            return a; 
        }
        
        /**
         * convert list item selected from string to number
         */
        convertNameToNumber(inputArray: string[]): number[]{
            var self = this;   
            let a = [];
            inputArray.forEach(item => {
                let rs = _.find(self.fullCheckBoxItem, o => {return o.name == item});
                a.push(rs.id);       
            });
            return a; 
        }
        
        /**
         * Print file excel
         */
        exportExcel() : void {
            var self = this;
            nts.uk.ui.block.grayout();
            service.saveAsExcel().done(function() {
            }).fail(function(error) {
                nts.uk.ui.dialog.alertError({ messageId: error.messageId });
            }).always(function() {
                nts.uk.ui.block.clear();
            });
        }
    }
    
    interface IWorkPlaceDto {
        workPlaceId: string;
        specificDate: number;
        specificDateItemNo: number[];
    }
    
    class WorkPlaceObject {
        id: KnockoutObservable<string>;
        name: KnockoutObservable<string>;
        constructor(id: string, name: string) {
            this.id = ko.observable(id);
            this.name = ko.observable(name);   
        }      
    }
    
    class CheckBoxItem {
        id: number;
        name: string; 
        constructor(id: number, name: string) {
            this.id = id;
            this.name = name;
        } 
    }
    
    class CalendarItem {
        start: string;
        textColor: string;
        backgroundColor: string;
        listText: string[];
        constructor(start: number, listText: string[]) {
            this.start = moment(start.toString()).format('YYYY-MM-DD');
            this.backgroundColor = 'white';
            this.textColor = '#31859C';
            this.listText = listText;
        }
        changeListText(value: string[]){
            this.listText = value;     
        }
    }
    
    export enum WorkingDayAtr {
        WorkingDayAtr_Company = '稼働日',
        WorkingDayAtr_WorkPlace = '非稼働日（法内）',
        WorkingDayAtr_Class = '非稼働日（法外）'
    }
    
}