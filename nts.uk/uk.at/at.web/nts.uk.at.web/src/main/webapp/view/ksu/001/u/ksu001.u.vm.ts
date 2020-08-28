// Refercen definition of any library
/// <reference path="../../../../lib/nittsu/viewcontext.d.ts" />
module nts.uk.at.view.ksu001.u {
    const Paths = {
        GET_PUBLIC_INFO_ORG: 'screen/at/shiftmanagement/shifttable/getPublicInfoOrg',
        REGISTER: 'ctx/schedule/shiftmanagement/shifttable/register'
    };
    const Ksu001u = {
        PUBLIC: '公開',
        EDIT: '編集中',
        TEXT_COLOR_PUB: '#4f6228',
        BG_COLOR_PUB: '#92d050',
        TEXT_COLOR_EDIT: '#974706',
        BG_COLOR_PRE_PUB: '#d8e4bc'
    };

    @bean()
    class Ksu001UViewModel extends ko.ViewModel {
        calendarData: KnockoutObservable<any>;
        yearMonthPicked: KnockoutObservable<number> = ko.observable();
        cssRangerYM: any = ko.observable();
        optionDates: KnockoutObservableArray<any> = ko.observableArray([]);
        firstDay: KnockoutObservable<number>;
        yearMonth: KnockoutObservable<number>;
        startDate: number;
        endDate: number;
        workplaceId: KnockoutObservable<string> = ko.observable();
        eventDisplay: KnockoutObservable<boolean> = ko.observable(false);
        eventUpdatable: KnockoutObservable<boolean>;
        holidayDisplay: KnockoutObservable<boolean>;
        cellButtonDisplay: KnockoutObservable<boolean> = ko.observable(false);
        showCalendarHeader: KnockoutObservable<boolean>;
        workplaceName: KnockoutObservable<string> = ko.observable('');
        lstStep: KnockoutObservableArray<any>;
        stepSelected: any;
        publicDate: KnockoutObservable<string> = ko.observable("");
        editDate: KnockoutObservable<string> = ko.observable("");
        newPublicDate: KnockoutObservable<string> = ko.observable("");
        newEditDate: KnockoutObservable<string> = ko.observable("");
        unit: KnockoutObservable<number> = ko.observable();
        workplaceGroupId: KnockoutObservable<string> = ko.observable('');        
        params: any;
        isBtnClick: KnockoutObservable<boolean> = ko.observable(false);
        oldValue: number = 0;
        constructor(params: any) {
            super();
            const self = this;       
            self.firstDay = ko.observable(0);
            self.eventUpdatable = ko.observable(false);
            self.holidayDisplay = ko.observable(false);
            self.showCalendarHeader = ko.observable(true);

            self.lstStep = ko.observableArray([
                { code: '0', name: nts.uk.resource.getText('KSU001_4010') },
                { code: '1', name: nts.uk.resource.getText('KSU001_4011') }
            ]);
            self.stepSelected = ko.observable("0");
            self.startDate = 1;
            self.endDate = 31;            
            self.params = params;

            self.yearMonthPicked.subscribe(function(value) {
                if(self.oldValue == value)
                {
                    return;
                }
                if(self.oldValue != value)
                {
                    self.oldValue = value;
                }
                if(self.isBtnClick()) {
                    self.isBtnClick(false);
                    return;
                }
                if(!self.isBtnClick()){
                    self.getDataToOneMonth(value);
                }                             
            })
        }

        created(){
            const self = this;
            self.loadPubDateInfo();
            self.clickCalendar();
        }

        getDataToOneMonth(yearMonth : number): void {            
            let self = this;
            let dates = self.optionDates();
            let year = parseInt(yearMonth.toString().substr(0,4));
            let month = parseInt(yearMonth.toString().substr(5,2));
            if(self.publicDate()){
                let publicDateSplit = self.publicDate().split('-');
                if(year == parseInt(publicDateSplit[0])){
                    if(month < parseInt(publicDateSplit[1])){
                        let numberDayOfMonth = self.getNumberOfDays(year, month);
                        for(let i = 1; i<= numberDayOfMonth; i ++){
                            let date = self.formatDate(new Date(year, month - 1, i));
                            let existDate = self.checkExistDate(date);
                            if (existDate) {
                                self.removeExistDate(existDate);
                            }
                            dates.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_PUB, Ksu001u.BG_COLOR_PUB, [Ksu001u.PUBLIC]));             
                        }
                    }
                } else if(year < parseInt(publicDateSplit[0])){
                    let numberDayOfMonth = self.getNumberOfDays(year, month);
                        for(let i = 1; i<= numberDayOfMonth; i ++){
                            let date = self.formatDate(new Date(year, month - 1, i));
                            let existDate = self.checkExistDate(date);
                            if (existDate) {
                                self.removeExistDate(existDate);
                            }
                            dates.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_PUB, Ksu001u.BG_COLOR_PUB, [Ksu001u.PUBLIC]));             
                        }
                } else {

                }
                self.optionDates(dates);
            }
        }

        loadPubDateInfo(): void {
            const self = this;
            let request = nts.uk.ui.windows.getShared('dataShareDialogU');
            self.$ajax(Paths.GET_PUBLIC_INFO_ORG, request).then((data: IPublicInfoOrg) => {
                self.unit(data.unit);
                self.workplaceId(data.workplaceId);
                self.workplaceGroupId(data.workplaceGroupId);
                self.workplaceName(data.displayName);
                self.publicDate(data.publicDate);
                self.editDate(data.editDate);
                self.newPublicDate(data.publicDate);
                self.newEditDate(data.editDate);

                let a = [];                
                let publicDate = self.publicDate();
                let editDate = self.editDate();
                let publicDateSplit = [];
                let year;
                let month;
                if(self.publicDate()){
                    publicDateSplit = publicDate.split('/');
                    year = parseInt(publicDateSplit[0]);
                    month =parseInt(publicDateSplit[1]);                   
                    self.publicDate(self.formatDate(new Date(parseInt(publicDateSplit[0]), parseInt(publicDateSplit[1]) - 1, parseInt(publicDateSplit[2]))));
                    self.newPublicDate(self.formatDate(new Date(parseInt(publicDateSplit[0]), parseInt(publicDateSplit[1]) - 1, parseInt(publicDateSplit[2]))));                    
                } else {                   
                    let today = new Date();     
                    year = today.getFullYear();
                    month = today.getMonth() + 1; 
                    self.optionDates([]);
                }     
                let editDateSplit = [];
                let publicDateInt = parseInt(publicDateSplit[2]);
                let endEditDateInt = 0;
                if (editDate) {
                    editDateSplit = editDate.split('/');
                    endEditDateInt = parseInt(editDateSplit[2]);
                    self.editDate(self.formatDate(new Date(parseInt(editDateSplit[0]), parseInt(editDateSplit[1]) - 1, parseInt(editDateSplit[2]))));
                    self.newEditDate(self.formatDate(new Date(parseInt(editDateSplit[0]), parseInt(editDateSplit[1]) - 1, parseInt(editDateSplit[2]))));
                    for (let i = 1; i <= publicDateInt; i++) {
                        let date = self.formatDate(new Date(parseInt(publicDateSplit[0]), parseInt(publicDateSplit[1]) - 1, i));
                        let existDate = self.checkExistDate(date);
                        if (existDate) {
                            self.removeExistDate(existDate);
                        }
                        if (date >= self.editDate() && date <= self.publicDate()) {
	                        a.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_EDIT, Ksu001u.BG_COLOR_PUB,[Ksu001u.EDIT]));                            
                        } else {                        
                            a.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_PUB, Ksu001u.BG_COLOR_PUB, [Ksu001u.PUBLIC]));                            
                        }
                    }
                } else {
                    for (let i = 1; i <= publicDateInt; i++) {
                        let date = self.formatDate(new Date(parseInt(publicDateSplit[0]), parseInt(publicDateSplit[1]) - 1, i));
                        let existDate = self.checkExistDate(date);
                        if (existDate) {
                            self.removeExistDate(existDate);
                        }
                        a.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_PUB, Ksu001u.BG_COLOR_PUB, [Ksu001u.PUBLIC]));                        
                    }
                }
                self.optionDates(a); 
                self.yearMonthPicked(parseInt(year +""+ month ));
                $('#prev-btn').focus();
            }).fail((res) => {
                self.$dialog.error({ messageId: res.messageId });               
            }).always(()=>{
                self.$blockui('clear');                
            });
            
        }

        public clickCalendar(): void {
            const self = this;
            $("#calendar").ntsCalendar("init", {
                cellClick: function (dateClick) {
                    let basePubDate = self.publicDate();
                    let baseEditDate = self.editDate();
                    let baseEditDateSplit =[];

                    let publicDate = self.newPublicDate();
                    let publicDateSplit = [];
                    let editDate = self.newEditDate();
                    let editDateSplit = [];
                    let dateClickSplit = dateClick.split('-');
                    let offset = 0;
                    if (self.publicDate() == self.editDate()) {
                        offset = 1;
                    }
                    // check cell date click is inner or outer period public date
                    let existDate = self.checkExistDate(dateClick);
                    // if date click on between period of public date
                    if(!self.publicDate()){
                        for (let i = 0; i <= parseInt(dateClickSplit[2]); i++) {
                            let date = self.formatDate(new Date(parseInt(dateClickSplit[0]), parseInt(dateClickSplit[1]) - 1, parseInt(dateClickSplit[2]) - i));
                            if (self.checkExistDate(date)) {
                                self.removeExistDate(self.checkExistDate(date));
                            }
                            self.optionDates.push({
                                start: date,
                                textColor: Ksu001u.TEXT_COLOR_PUB,
                                backgroundColor: Ksu001u.BG_COLOR_PRE_PUB,
                                listText: [Ksu001u.PUBLIC]
                            });
                        }
                    } else {
                        publicDateSplit = publicDate.split('-');
                    }
                    if (baseEditDate) {
                        baseEditDateSplit = baseEditDate.split('-');
                        editDateSplit = editDate.split('-');
                        if (existDate) {
                            // set new edit date is date click cell calendar
                            self.newEditDate(self.formatDate(new Date(parseInt(dateClickSplit[0]), parseInt(dateClickSplit[1]) - 1, parseInt(dateClickSplit[2]))));
                            // check cell date is public or edit
                            if (existDate.textColor === Ksu001u.TEXT_COLOR_PUB && existDate.backgroundColor === Ksu001u.BG_COLOR_PUB) {
                                for (let i = 0; i <= parseInt(publicDateSplit[2]) - parseInt(dateClickSplit[2]); i++) {
                                    let date = self.formatDate(new Date(parseInt(publicDateSplit[0]), parseInt(publicDateSplit[1]) - 1, parseInt(publicDateSplit[2]) - i));
                                    if (self.checkExistDate(date)) {
                                        self.removeExistDate(self.checkExistDate(date));
                                    }
                                    self.optionDates.push({
                                        start: date,
                                        textColor: Ksu001u.TEXT_COLOR_EDIT,
                                        backgroundColor: Ksu001u.BG_COLOR_PRE_PUB,
                                        listText: [Ksu001u.EDIT]
                                    });
                                }
                            } else if (existDate.textColor === Ksu001u.TEXT_COLOR_EDIT) {
                                self.newEditDate(self.formatDate(new Date(parseInt(dateClickSplit[0]), parseInt(dateClickSplit[1]) - 1, parseInt(dateClickSplit[2]) + 1)));
                                for (let i = 0; i <= parseInt(dateClickSplit[2]) - parseInt(editDateSplit[2]); i++) {
                                    let date = self.formatDate(new Date(parseInt(dateClickSplit[0]), parseInt(dateClickSplit[1]) - 1, parseInt(dateClickSplit[2]) - i));
                                    if (self.checkExistDate(date)) {
                                        self.removeExistDate(self.checkExistDate(date));
                                    }
                                    self.optionDates.push({
                                        start: date,
                                        textColor: Ksu001u.TEXT_COLOR_PUB,
                                        backgroundColor: Ksu001u.BG_COLOR_PRE_PUB,
                                        listText: [Ksu001u.PUBLIC]
                                    });
                                }
                            }
                        } else {
                            let periodPub = parseInt(dateClickSplit[2]) - parseInt(publicDateSplit[2]);
                            let periodEdit = 0;
                            if (self.editDate() != "") {
                                periodEdit = parseInt(dateClickSplit[2]) - (parseInt(baseEditDateSplit[2]) <= parseInt(editDateSplit[2]) ? parseInt(baseEditDateSplit[2]) :parseInt(editDateSplit[2]));
                            }
                            if (periodPub <= periodEdit) {
                                self.newEditDate("");
                            }

                            self.newPublicDate(self.formatDate(new Date(parseInt(dateClickSplit[0]), parseInt(dateClickSplit[1]) - 1, parseInt(dateClickSplit[2]))));
                            for (let i = 0; i <= (periodPub > periodEdit ? periodPub : periodEdit) + offset; i++) {
                                let date = self.formatDate(new Date(parseInt(dateClickSplit[0]), parseInt(dateClickSplit[1]) - 1, parseInt(dateClickSplit[2]) - i));
                                if (self.checkExistDate(date)) {
                                    self.removeExistDate(self.checkExistDate(date));
                                }
                                self.optionDates.push({
                                    start: date,
                                    textColor: Ksu001u.TEXT_COLOR_PUB,
                                    backgroundColor: Ksu001u.BG_COLOR_PRE_PUB,
                                    listText: [Ksu001u.PUBLIC]
                                });
                            }
                        }

                    } else {
                        if (existDate) {
                            // set new edit date is date click cell calendar
                            self.newEditDate(self.formatDate(new Date(parseInt(dateClickSplit[0]), parseInt(dateClickSplit[1]) - 1, parseInt(dateClickSplit[2]))));

                            for (let i = 0; i <= parseInt(publicDateSplit[2]) - parseInt(dateClickSplit[2]); i++) {
                                let date = self.formatDate(new Date(parseInt(publicDateSplit[0]), parseInt(publicDateSplit[1]) - 1, parseInt(publicDateSplit[2]) - i));
                                if (self.checkExistDate(date)) {
                                    self.removeExistDate(self.checkExistDate(date));
                                }
                                self.optionDates.push({
                                    start: date,
                                    textColor: Ksu001u.TEXT_COLOR_EDIT,
                                    backgroundColor: Ksu001u.BG_COLOR_PRE_PUB,
                                    listText: [Ksu001u.EDIT]
                                });
                            }

                        } else {
                            let periodPub = parseInt(dateClickSplit[2]) - parseInt(publicDateSplit[2]);
                            self.newPublicDate(self.formatDate(new Date(parseInt(dateClickSplit[0]), parseInt(dateClickSplit[1]) - 1, parseInt(dateClickSplit[2]))));
                            for (let i = 0; i < periodPub; i++) {
                                let date = self.formatDate(new Date(parseInt(dateClickSplit[0]), parseInt(dateClickSplit[1]) - 1, parseInt(dateClickSplit[2]) - i));
                                if (self.checkExistDate(date)) {
                                    self.removeExistDate(self.checkExistDate(date));
                                }
                                self.optionDates.push({
                                    start: date,
                                    textColor: Ksu001u.TEXT_COLOR_PUB,
                                    backgroundColor: Ksu001u.BG_COLOR_PRE_PUB,
                                    listText: [Ksu001u.PUBLIC]
                                });
                            }
                        }
                    }
                }
            });
        }
        public Prev(): void {
            const self = this;

            let tmp = self.stepSelected();
            if (self.stepSelected() == "0") {
                self.weekPrev();
            } else {
                self.monthPrev();
            }
        }

        public Forward(): void {
            const self = this;
            if (self.stepSelected() == "0") {
                self.weekForward();
            } else {
                self.monthForward();
            }
        }

        public weekForward(): void {
            const self = this;
            let dates = self.optionDates();
            let basePubDate = self.publicDate();
            let basePubDateSplit = [];
            let publicDate = self.newPublicDate();
            let publicDateSplit = [];
            let offset = 0;
            if (self.newPublicDate() == self.editDate()) {
                offset = 1;
            }

            // let editDate = self.editDate();            
            if(!self.publicDate()){
                let today = new Date();
                for (let i = 1; i <= 7 ; i++) {
                    let date = self.formatDate(new Date(today.getFullYear(), today.getMonth(), i));
                    if (self.checkExistDate(date)) {
                        self.removeExistDate(self.checkExistDate(date));
                    }
                    self.optionDates.push({
                        start: date,
                        textColor: Ksu001u.TEXT_COLOR_PUB,
                        backgroundColor: Ksu001u.BG_COLOR_PRE_PUB,
                        listText: [Ksu001u.PUBLIC]
                    });
                }
                self.newPublicDate(self.formatDate(new Date(today.getFullYear(), today.getMonth(), 7)));
            } else {
                basePubDateSplit = basePubDate.split('-');
                publicDateSplit = publicDate.split('-');
            }

            let forwardWeek = self.formatDate(new Date(parseInt(publicDateSplit[0]), parseInt(publicDateSplit[1]) - 1, parseInt(publicDateSplit[2]) + 7));
            let forwardWeekSplit = forwardWeek.split('-');
            let forwardWeekPublicDate = self.formatDate(new Date(parseInt(forwardWeekSplit[0]), parseInt(forwardWeekSplit[1]) - 1, parseInt(forwardWeekSplit[2])));

            let numberDayOfMonth = self.getNumberOfDays(parseInt(forwardWeekSplit[0]), parseInt(forwardWeekSplit[1]));
            if (parseInt(forwardWeekSplit[0]) == parseInt(basePubDateSplit[0])) {
                if (parseInt(forwardWeekSplit[1]) == parseInt(basePubDateSplit[1])) {
                    if(self.editDate()){                                              
                        let size = self.daysDifference(self.editDate(), forwardWeek) >= 7 ? self.daysDifference(self.editDate(), forwardWeek) : 7 ;
                        for (let i = 0; i <= size; i ++) {
                            let date = self.formatDate(new Date(parseInt(forwardWeekSplit[0]), parseInt(forwardWeekSplit[1]) - 1, parseInt(forwardWeekSplit[2]) - i));
                            if (self.checkExistDate(date)) {
                                self.removeExistDate(self.checkExistDate(date));
                            }
                            if(forwardWeek <= self.publicDate()){
                                if (date < self.editDate()) {
                                    dates.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_PUB, Ksu001u.BG_COLOR_PUB, [Ksu001u.PUBLIC]));                                                                  
                                } else if(date >= self.editDate()){
                                    dates.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_EDIT, Ksu001u.BG_COLOR_PUB, [Ksu001u.EDIT]));
                                }                               
                            } else {
                                dates.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_PUB, Ksu001u.BG_COLOR_PRE_PUB, [Ksu001u.PUBLIC]));
                            }                            
                        }
                    } else {
                        for (let i = 0; i < 7; i++) {
                            let date = self.formatDate(new Date(parseInt(forwardWeekSplit[0]), parseInt(forwardWeekSplit[1]) - 1, parseInt(forwardWeekSplit[2]) - i));
                            if (self.checkExistDate(date)) {
                                self.removeExistDate(self.checkExistDate(date));
                            }
                            if (forwardWeek > self.publicDate()) {
                                dates.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_PUB, Ksu001u.BG_COLOR_PRE_PUB, [Ksu001u.PUBLIC]));
                            } else {
                                dates.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_PUB, Ksu001u.BG_COLOR_PUB, [Ksu001u.PUBLIC])); 
                            }
                        }
                    }                    
                } else if (parseInt(forwardWeekSplit[1]) < parseInt(basePubDateSplit[1])) {
                    for (let i = 0; i <= 7; i++) {
                        let date = self.formatDate(new Date(parseInt(forwardWeekSplit[0]), parseInt(forwardWeekSplit[1]) - 1, parseInt(forwardWeekSplit[2]) - i));
                        if (self.checkExistDate(date)) {
                            self.removeExistDate(self.checkExistDate(date));
                        }
                        dates.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_PUB, Ksu001u.BG_COLOR_PUB, [Ksu001u.PUBLIC])); 
                    }
                } else if (parseInt(forwardWeekSplit[1]) > parseInt(basePubDateSplit[1])) {
                    for (let i = 0; i < 7; i++) {
                        let date = self.formatDate(new Date(parseInt(forwardWeekSplit[0]), parseInt(forwardWeekSplit[1]) - 1, parseInt(forwardWeekSplit[2]) - i));
                        if (self.checkExistDate(date)) {
                            self.removeExistDate(self.checkExistDate(date));
                        }
                        dates.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_PUB, Ksu001u.BG_COLOR_PRE_PUB, [Ksu001u.PUBLIC])); 
                    }
                }
            } else if(parseInt(forwardWeekSplit[0]) < parseInt(basePubDateSplit[0])){
                for (let i = 0; i < numberDayOfMonth; i++) {
                    let date = self.formatDate(new Date(parseInt(forwardWeekSplit[0]), parseInt(forwardWeekSplit[1]) - 1, numberDayOfMonth - i));
                    if (self.checkExistDate(date)) {
                        self.removeExistDate(self.checkExistDate(date));
                    }
                    if(date <=  forwardWeek){
                        dates.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_PUB, Ksu001u.BG_COLOR_PUB, [Ksu001u.PUBLIC])); 
                    } else {
                        dates.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_EDIT, Ksu001u.BG_COLOR_PRE_PUB, [Ksu001u.EDIT]));
                    }                   
                }
            } else if(parseInt(forwardWeekSplit[0]) > parseInt(basePubDateSplit[0])){
                for (let i = 0; i < parseInt(forwardWeekSplit[2]); i++) {
                    let date = self.formatDate(new Date(parseInt(forwardWeekSplit[0]), parseInt(forwardWeekSplit[1]) - 1, parseInt(forwardWeekSplit[2]) - i));
                    if (self.checkExistDate(date)) {
                        self.removeExistDate(self.checkExistDate(date));
                    }                    
                    dates.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_PUB, Ksu001u.BG_COLOR_PRE_PUB, [Ksu001u.PUBLIC])); 
                }
            }
            self.optionDates(dates);
            self.newPublicDate(forwardWeekPublicDate);
            self.yearMonthPicked(parseInt(forwardWeekSplit[0] + forwardWeekSplit[1]));            
        }

         public weekPrev(): void {
            const self = this;
            let dates = self.optionDates();
            let basePubDate = self.publicDate();
            let basePubDateSplit = basePubDate.split('-');
            let publicDate = self.newPublicDate();
            let publicDateSplit = publicDate.split('-');
            let editDate = self.newEditDate();
            let editDateSplit = [];

            let offset = 0;
            if (self.publicDate() == self.editDate()) {
                offset = 1;
            }                
            let prevWeek = "";
            let prevWeekEditDate = "";
            if(self.newPublicDate() == self.publicDate() && self.newEditDate()){
                editDateSplit = editDate.split("-");
                prevWeek = self.formatDate(new Date(parseInt(editDateSplit[0]), parseInt(editDateSplit[1]) - 1, parseInt(editDateSplit[2]) - 7));  
                prevWeekEditDate = self.formatDate(new Date(parseInt(editDateSplit[0]), parseInt(editDateSplit[1]) - 1, parseInt(editDateSplit[2]) - 6)); 
            } else {
                prevWeek = self.formatDate(new Date(parseInt(publicDateSplit[0]), parseInt(publicDateSplit[1]) - 1, parseInt(publicDateSplit[2]) - 7));  
                prevWeekEditDate = self.formatDate(new Date(parseInt(publicDateSplit[0]), parseInt(publicDateSplit[1]) - 1, parseInt(publicDateSplit[2]) - 6)); 
            }            
            
            let prevWeekSplit = prevWeek.split('-');
            let prevWeekPublicDate = self.formatDate(new Date(parseInt(prevWeekSplit[0]), parseInt(prevWeekSplit[1]) - 1, parseInt(prevWeekSplit[2])))

            let numberDayOfMonth = self.getNumberOfDays(parseInt(prevWeekSplit[0]), parseInt(prevWeekSplit[1]));
            // check year vs year current
            if(parseInt(prevWeekSplit[0]) == parseInt(basePubDateSplit[0])){
                // check month vs month current
                if (parseInt(prevWeekSplit[1]) == parseInt(basePubDateSplit[1])) {
                    for (let i = 1; i <= 7; i++) {
                        let date = self.formatDate(new Date(parseInt(prevWeekSplit[0]), parseInt(prevWeekSplit[1]) - 1, parseInt(prevWeekSplit[2]) + i));
                        if (self.checkExistDate(date)) {
                            self.removeExistDate(self.checkExistDate(date));
                        }
                        if (prevWeek < self.publicDate()) {
                            if(date <= self.publicDate()){
                                dates.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_EDIT, Ksu001u.BG_COLOR_PRE_PUB, [Ksu001u.EDIT]));
                            }                            
                        } 
                    }                    
                } else if(parseInt(prevWeekSplit[1]) < parseInt(basePubDateSplit[1])){
                    let dayOfMonth = self.getNumberOfDays(parseInt(prevWeekSplit[0]), parseInt(prevWeekSplit[1]));
                    for (let i = 1; i <= dayOfMonth; i++) {
                        let date = self.formatDate(new Date(parseInt(prevWeekSplit[0]), parseInt(prevWeekSplit[1]) - 1, i));
                        if (self.checkExistDate(date)) {
                            self.removeExistDate(self.checkExistDate(date));
                        }
                        if (date <= prevWeek) {
                            dates.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_PUB, Ksu001u.BG_COLOR_PUB, [Ksu001u.PUBLIC]));
                        } else {
                            dates.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_EDIT, Ksu001u.BG_COLOR_PRE_PUB, [Ksu001u.EDIT]));
                        }
                    }
                } else if(parseInt(prevWeekSplit[1]) > parseInt(basePubDateSplit[1])){
                    for (let i = 1; i <= 7; i++ ){
                        let date = self.formatDate(new Date(parseInt(prevWeekSplit[0]), parseInt(prevWeekSplit[1]) - 1, parseInt(prevWeekSplit[2]) + i));                            
                        if (self.checkExistDate(date)) {
                            self.removeExistDate(self.checkExistDate(date));
                        }
                    }
                }
            } else if(parseInt(prevWeekSplit[0]) < parseInt(basePubDateSplit[0])){
                for (let i = 0; i < numberDayOfMonth; i++) {
                    let date = self.formatDate(new Date(parseInt(prevWeekSplit[0]), parseInt(prevWeekSplit[1]) - 1, numberDayOfMonth - i));
                    if (self.checkExistDate(date)) {
                        self.removeExistDate(self.checkExistDate(date));
                    }
                    if(date <=  prevWeek){
                        dates.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_PUB, Ksu001u.BG_COLOR_PUB, [Ksu001u.PUBLIC]));
                    } else {
                        dates.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_EDIT, Ksu001u.BG_COLOR_PRE_PUB, [Ksu001u.EDIT]));                       
                    }                   
                }
            } else if(parseInt(prevWeekSplit[0]) > parseInt(basePubDateSplit[0])){
                for (let i = 0; i < numberDayOfMonth; i++) {
                    let date = self.formatDate(new Date(parseInt(prevWeekSplit[0]), parseInt(prevWeekSplit[1]) - 1, numberDayOfMonth - i));
                    if (self.checkExistDate(date)) {
                        self.removeExistDate(self.checkExistDate(date));
                    }
                    if(date <=  prevWeek){
                        dates.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_PUB, Ksu001u.BG_COLOR_PRE_PUB, [Ksu001u.PUBLIC]));
                    }       
                }
            }
            self.optionDates(dates);
            if(prevWeek > self.publicDate() || (prevWeek > self.publicDate()&& prevWeek >= self.editDate())){
                self.newPublicDate(prevWeekPublicDate);
            } else {
                self.newPublicDate(self.publicDate());
                self.newEditDate(prevWeekEditDate);
            }            
            self.isBtnClick(true);
            self.yearMonthPicked(parseInt(prevWeekSplit[0] + prevWeekSplit[1]));
                    
            // self.yearMonthPicked.valueHasMutated();
        }
 
        public monthPrev(): void {
            const self = this;
            let dates = self.optionDates();
            let basePubDate = self.publicDate();
            let basePubDateSplit = basePubDate.split('-');
            let publicDate = self.newPublicDate();
            let publicDateSplit = publicDate.split('-');
            let prevMonthPublicDate = "";
            let editDate = self.editDate();
            let editDateSplit = [];
            if (parseInt(publicDateSplit[1]) == parseInt(editDateSplit[1]) && self.publicDate() <= self.editDate()) {
                self.editDate("");
                editDate = "";
            }

            let prevMonth = self.formatDate(new Date(parseInt(publicDateSplit[0]), parseInt(publicDateSplit[1]) - 2, parseInt(publicDateSplit[2])));
            let prevMonthSplit = prevMonth.split('-');
            let numberDayOfPrevMonth = self.getNumberOfDays(parseInt(prevMonthSplit[0]), parseInt(prevMonthSplit[1]));
            if (parseInt(prevMonthSplit[2]) < numberDayOfPrevMonth) {
                prevMonthPublicDate = self.formatDate(new Date(parseInt(prevMonthSplit[0]), parseInt(prevMonthSplit[1]) - 1, parseInt(prevMonthSplit[2])));
            } else {
                prevMonthPublicDate = self.formatDate(new Date(parseInt(prevMonthSplit[0]), parseInt(prevMonthSplit[1]) - 1, numberDayOfPrevMonth));
            }
            let prevMonthPublicDateSplit = prevMonthPublicDate.split('-');
            // reset public date and edit    
            if (editDate && parseInt(basePubDateSplit[0]) == parseInt(prevMonthPublicDateSplit[0])) {
                if (parseInt(basePubDateSplit[1]) == parseInt(prevMonthPublicDateSplit[1])) {
                    // self.loadPubDateInfo();
                    for (let i = 0; i < numberDayOfPrevMonth; i++) {
                        let date = self.formatDate(new Date(parseInt(basePubDateSplit[0]), parseInt(basePubDateSplit[1]) - 1, numberDayOfPrevMonth - i));
                        let existDate = self.checkExistDate(date);
                        if (existDate) {
                            self.removeExistDate(existDate);
                        }
                        if (date >= self.editDate() && date <= self.publicDate()) {
                            dates.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_EDIT, Ksu001u.BG_COLOR_PUB, [Ksu001u.EDIT]));
                        } else if(date < self.editDate()) {
                            dates.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_PUB, Ksu001u.BG_COLOR_PUB, [Ksu001u.PUBLIC]));
                        }
                    }
                } else if (parseInt(prevMonthPublicDateSplit[1]) < parseInt(basePubDateSplit[1])) {
                    for (let i = 0; i < numberDayOfPrevMonth; i++) {
                        let date = self.formatDate(new Date(parseInt(prevMonthPublicDateSplit[0]), parseInt(prevMonthPublicDateSplit[1]) - 1, numberDayOfPrevMonth - i));
                        let existDate = self.checkExistDate(date);
                        if (i < numberDayOfPrevMonth - parseInt(prevMonthPublicDateSplit[2])) {
                            self.removeExistDate(existDate);
                            dates.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_EDIT, Ksu001u.BG_COLOR_PRE_PUB, [Ksu001u.EDIT]));
                        } else {
                            self.removeExistDate(existDate);
                            dates.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_PUB, Ksu001u.BG_COLOR_PUB, [Ksu001u.PUBLIC]));
                        }
                    }
                } else if (parseInt(prevMonthPublicDateSplit[1]) > parseInt(basePubDateSplit[1])) {
                    for (let i = 0; i < parseInt(prevMonthPublicDateSplit[2]); i++) {
                        let date = self.formatDate(new Date(parseInt(prevMonthPublicDateSplit[0]), parseInt(prevMonthPublicDateSplit[1]) - 1, parseInt(prevMonthPublicDateSplit[2]) - i));
                        let existDate = self.checkExistDate(date);
                        self.removeExistDate(existDate);
                        dates.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_PUB, Ksu001u.BG_COLOR_PRE_PUB, [Ksu001u.PUBLIC]));
                    }
                }
            } else if (editDate && parseInt(basePubDateSplit[0]) > parseInt(prevMonthPublicDateSplit[0])) {
                for (let i = 0; i < numberDayOfPrevMonth; i++) {
                    let date = self.formatDate(new Date(parseInt(prevMonthPublicDateSplit[0]), parseInt(prevMonthPublicDateSplit[1]) - 1, numberDayOfPrevMonth - i));
                    let existDate = self.checkExistDate(date);
                    if (i < numberDayOfPrevMonth - parseInt(prevMonthPublicDateSplit[2])) {
                        self.removeExistDate(existDate);
                        dates.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_EDIT, Ksu001u.BG_COLOR_PRE_PUB, [Ksu001u.EDIT]));
                    } else {
                        self.removeExistDate(existDate);
                        dates.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_PUB, Ksu001u.BG_COLOR_PUB, [Ksu001u.PUBLIC]));
                    }
                }
            } else {
                if(parseInt(basePubDateSplit[0]) == parseInt(prevMonthPublicDateSplit[0])){
                    if (parseInt(basePubDateSplit[1]) == parseInt(prevMonthPublicDateSplit[1])) {
                        for (let i = 0; i < numberDayOfPrevMonth; i++) {
                            let date = self.formatDate(new Date(parseInt(basePubDateSplit[0]), parseInt(basePubDateSplit[1]) - 1, numberDayOfPrevMonth - i));
                            let existDate = self.checkExistDate(date);
                            if (existDate) {
                                self.removeExistDate(existDate);
                            }
                            if (date <= self.publicDate()) {
                                dates.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_PUB, Ksu001u.BG_COLOR_PUB, [Ksu001u.PUBLIC]));
                            }
                        }
                    } else if (parseInt(prevMonthPublicDateSplit[1]) < parseInt(basePubDateSplit[1])) {
                        for (let i = 0; i < numberDayOfPrevMonth; i++) {
                            let date = self.formatDate(new Date(parseInt(prevMonthPublicDateSplit[0]), parseInt(prevMonthPublicDateSplit[1]) - 1, numberDayOfPrevMonth - i));
                            let existDate = self.checkExistDate(date);
                            if (i < numberDayOfPrevMonth - parseInt(prevMonthPublicDateSplit[2])) {
                                self.removeExistDate(existDate);
                                dates.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_EDIT, Ksu001u.BG_COLOR_PRE_PUB, [Ksu001u.EDIT]));
                            } else {
                                self.removeExistDate(existDate);
                                dates.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_PUB, Ksu001u.BG_COLOR_PUB, [Ksu001u.PUBLIC]));
                            }
                        }
                    } else if (parseInt(prevMonthPublicDateSplit[1]) > parseInt(basePubDateSplit[1])) {
                        for (let i = 0; i < parseInt(prevMonthPublicDateSplit[2]); i++) {
                            let date = self.formatDate(new Date(parseInt(prevMonthPublicDateSplit[0]), parseInt(prevMonthPublicDateSplit[1]) - 1, parseInt(prevMonthPublicDateSplit[2]) - i));
                            let existDate = self.checkExistDate(date);
                            self.removeExistDate(existDate);
                            dates.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_PUB, Ksu001u.BG_COLOR_PRE_PUB, [Ksu001u.PUBLIC]));
                        }
                    }

                } else if (parseInt(basePubDateSplit[0]) > parseInt(prevMonthPublicDateSplit[0])) {
                    for (let i = 0; i < numberDayOfPrevMonth; i++) {
                        let date = self.formatDate(new Date(parseInt(prevMonthPublicDateSplit[0]), parseInt(prevMonthPublicDateSplit[1]) - 1, numberDayOfPrevMonth - i));
                        let existDate = self.checkExistDate(date);
                        if (i < numberDayOfPrevMonth - parseInt(prevMonthPublicDateSplit[2])) {
                            self.removeExistDate(existDate);
                            dates.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_EDIT, Ksu001u.BG_COLOR_PRE_PUB, [Ksu001u.EDIT]));
                        } else {
                            self.removeExistDate(existDate);
                            dates.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_PUB, Ksu001u.BG_COLOR_PUB, [Ksu001u.PUBLIC]));
                        }
                    }
                } else if (parseInt(basePubDateSplit[0]) < parseInt(prevMonthPublicDateSplit[0])) {
                    for (let i = 0; i < parseInt(prevMonthPublicDateSplit[2]); i++) {
                        let date = self.formatDate(new Date(parseInt(prevMonthPublicDateSplit[0]), parseInt(prevMonthPublicDateSplit[1]) - 1, parseInt(prevMonthPublicDateSplit[2]) - i));
                        let existDate = self.checkExistDate(date);
                        self.removeExistDate(existDate);
                        dates.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_PUB, Ksu001u.BG_COLOR_PRE_PUB, [Ksu001u.PUBLIC]));
                    }
                }
            }
            self.optionDates(dates);
            self.newPublicDate(prevMonthPublicDate);
            self.yearMonthPicked(parseInt(prevMonthPublicDateSplit[0] + prevMonthPublicDateSplit[1]));
        }

        public monthForward(): void {
            const self = this;
            let dates = self.optionDates();
            let basePubDate = self.publicDate();
            let basePubDateSplit = basePubDate.split('-');
            let publicDate = self.newPublicDate();
            let publicDateSplit = publicDate.split('-');
            let nextMonthPublicDate = "";
            let nextMonth = "";
            let nextMonthPublicDateSplit = [];
            let offset = 0;
            let editDate = self.editDate();            
            if (self.publicDate() == self.editDate()) {
                offset = 1;
            }
            
            let numberDayOfNextMonth = 1;
            if(parseInt(publicDateSplit[1]) + 1 > 12){
                numberDayOfNextMonth = self.getNumberOfDays(parseInt(publicDateSplit[0]) + 1, 1);
            } else {
                numberDayOfNextMonth = self.getNumberOfDays(parseInt(publicDateSplit[0]), parseInt(publicDateSplit[1]) + 1);
            }
            if(numberDayOfNextMonth >= parseInt(publicDateSplit[2])){
                nextMonth = self.formatDate(new Date(parseInt(publicDateSplit[0]), parseInt(publicDateSplit[1]), parseInt(publicDateSplit[2])));
            } else {
                nextMonth = self.formatDate(new Date(parseInt(publicDateSplit[0]), parseInt(publicDateSplit[1]), numberDayOfNextMonth));
            }
            let nextMonthSplit = nextMonth.split('-');
            nextMonthPublicDate = self.formatDate(new Date(parseInt(nextMonthSplit[0]), parseInt(nextMonthSplit[1]) - 1, parseInt(nextMonthSplit[2])));
            nextMonthPublicDateSplit = nextMonthPublicDate.split('-');           
            if (editDate && parseInt(basePubDateSplit[0]) == parseInt(nextMonthPublicDateSplit[0])) {  
                if (parseInt(basePubDateSplit[1]) == parseInt(nextMonthPublicDateSplit[1])) {
                    for(let i = 0; i < numberDayOfNextMonth ; i ++) {
                        let date = self.formatDate(new Date(parseInt(basePubDateSplit[0]), parseInt(basePubDateSplit[1]) - 1, numberDayOfNextMonth - i));
                        let existDate = self.checkExistDate(date);    
                        if(existDate){
                            self.removeExistDate(existDate);
                        }
                        if (date >= self.editDate() && date <= self.publicDate()) {
                            dates.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_EDIT, Ksu001u.BG_COLOR_PUB, [Ksu001u.EDIT]));
                        } else if(date < self.editDate()) {
                            dates.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_PUB, Ksu001u.BG_COLOR_PUB, [Ksu001u.PUBLIC]));
                        }
                    }
                } else if (parseInt(basePubDateSplit[1]) > parseInt(nextMonthPublicDateSplit[1])) {
                    for (let i = 1; i <= parseInt(nextMonthPublicDateSplit[2]) + offset; i++) {
                        let date = self.formatDate(new Date(parseInt(nextMonthPublicDateSplit[0]), parseInt(nextMonthPublicDateSplit[1]) - 1, parseInt(nextMonthPublicDateSplit[2]) - i));
                        let existDate = self.checkExistDate(date);
                        if (i <= parseInt(nextMonthPublicDateSplit[2]) - parseInt(publicDateSplit[2])) {
                            self.removeExistDate(existDate);
                            dates.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_EDIT, Ksu001u.BG_COLOR_PUB, [Ksu001u.EDIT]));
                        } else {
                            self.removeExistDate(existDate);
                            dates.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_PUB, Ksu001u.BG_COLOR_PUB, [Ksu001u.PUBLIC]));
                        }
                    }
                } else if (parseInt(basePubDateSplit[1]) < parseInt(nextMonthPublicDateSplit[1])){
                    for (let i = 0; i < parseInt(nextMonthPublicDateSplit[2]) + offset; i++) {
                        let date = self.formatDate(new Date(parseInt(nextMonthPublicDateSplit[0]), parseInt(nextMonthPublicDateSplit[1]) - 1, parseInt(nextMonthPublicDateSplit[2]) - i));
                        let existDate = self.checkExistDate(date);
                        self.removeExistDate(existDate);
                        dates.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_PUB, Ksu001u.BG_COLOR_PRE_PUB, [Ksu001u.PUBLIC]));
                    }
                }
            } else if (editDate && parseInt(basePubDateSplit[0]) > parseInt(nextMonthPublicDateSplit[0])) {
                for (let i = 1; i <= parseInt(nextMonthPublicDateSplit[2]) + offset; i++) {
                    let date = self.formatDate(new Date(parseInt(nextMonthPublicDateSplit[0]), parseInt(nextMonthPublicDateSplit[1]) - 1, parseInt(nextMonthPublicDateSplit[2]) - i));
                    let existDate = self.checkExistDate(date);
                    if (i <= parseInt(nextMonthPublicDateSplit[2]) - parseInt(publicDateSplit[2])) {
                        self.removeExistDate(existDate);
                        dates.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_EDIT, Ksu001u.BG_COLOR_PUB, [Ksu001u.EDIT]));
                    } else {
                        self.removeExistDate(existDate);
                        dates.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_PUB, Ksu001u.BG_COLOR_PUB, [Ksu001u.PUBLIC]));
                    }
                }
            }else if (editDate && parseInt(basePubDateSplit[0]) < parseInt(nextMonthPublicDateSplit[0])) {
                for (let i = 0; i < parseInt(nextMonthPublicDateSplit[2]) + offset; i++) {
                    let date = self.formatDate(new Date(parseInt(nextMonthPublicDateSplit[0]), parseInt(nextMonthPublicDateSplit[1]) - 1, parseInt(nextMonthPublicDateSplit[2]) - i));
                    let existDate = self.checkExistDate(date);
                    self.removeExistDate(existDate);
                    dates.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_PUB, Ksu001u.BG_COLOR_PRE_PUB, [Ksu001u.PUBLIC]));
                }
            } else {
                if (parseInt(basePubDateSplit[0]) == parseInt(nextMonthPublicDateSplit[0])) {
                    if (parseInt(basePubDateSplit[1]) == parseInt(nextMonthPublicDateSplit[1])) {
                        for(let i = 0; i < numberDayOfNextMonth ; i ++) {
                            let date = self.formatDate(new Date(parseInt(basePubDateSplit[0]), parseInt(basePubDateSplit[1]) - 1, numberDayOfNextMonth - i));
                            let existDate = self.checkExistDate(date);    
                            if(existDate){
                                self.removeExistDate(existDate);
                            }
                            if(date <= self.publicDate()){
                                dates.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_PUB, Ksu001u.BG_COLOR_PUB, [Ksu001u.PUBLIC]));
                            }
                        }
                    } else if (parseInt(basePubDateSplit[1]) > parseInt(nextMonthPublicDateSplit[1])) {
                        for (let i = 1; i <= parseInt(nextMonthPublicDateSplit[2]) + offset; i++) {
                            let date = self.formatDate(new Date(parseInt(nextMonthPublicDateSplit[0]), parseInt(nextMonthPublicDateSplit[1]) - 1, parseInt(nextMonthPublicDateSplit[2]) - i));
                            let existDate = self.checkExistDate(date);
                            if (i <= parseInt(nextMonthPublicDateSplit[2]) - parseInt(publicDateSplit[2])) {
                                self.removeExistDate(existDate);
                                dates.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_EDIT, Ksu001u.BG_COLOR_PUB, [Ksu001u.EDIT]));
                            } else {
                                self.removeExistDate(existDate);
                                dates.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_PUB, Ksu001u.BG_COLOR_PUB, [Ksu001u.PUBLIC]));
                            }
                        }
                    } else  if(parseInt(basePubDateSplit[1]) < parseInt(nextMonthPublicDateSplit[1])) {
                        for (let i = 0; i < parseInt(nextMonthPublicDateSplit[2]) + offset; i++) {
                            let date = self.formatDate(new Date(parseInt(nextMonthPublicDateSplit[0]), parseInt(nextMonthPublicDateSplit[1]) - 1, parseInt(nextMonthPublicDateSplit[2]) - i));
                            let existDate = self.checkExistDate(date);
                            self.removeExistDate(existDate);
                            dates.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_PUB, Ksu001u.BG_COLOR_PRE_PUB, [Ksu001u.PUBLIC]));
                        }
                    }

                } else if (parseInt(basePubDateSplit[0]) > parseInt(nextMonthPublicDateSplit[0])){
                    for (let i = 1; i <= parseInt(nextMonthPublicDateSplit[2]) + offset; i++) {
                        let date = self.formatDate(new Date(parseInt(nextMonthPublicDateSplit[0]), parseInt(nextMonthPublicDateSplit[1]) - 1, parseInt(nextMonthPublicDateSplit[2]) - i));
                        let existDate = self.checkExistDate(date);
                        if (i <= parseInt(nextMonthPublicDateSplit[2]) - parseInt(publicDateSplit[2])) {
                            self.removeExistDate(existDate);
                            dates.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_EDIT, Ksu001u.BG_COLOR_PUB, [Ksu001u.EDIT]));
                        } else {
                            self.removeExistDate(existDate);
                            dates.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_PUB, Ksu001u.BG_COLOR_PUB, [Ksu001u.PUBLIC]));
                        }
                    }
                } else if(parseInt(basePubDateSplit[0]) < parseInt(nextMonthPublicDateSplit[0])){
                    for (let i = 0; i < parseInt(nextMonthPublicDateSplit[2]) + offset; i++) {
                        let date = self.formatDate(new Date(parseInt(nextMonthPublicDateSplit[0]), parseInt(nextMonthPublicDateSplit[1]) - 1, parseInt(nextMonthPublicDateSplit[2]) - i));
                        let existDate = self.checkExistDate(date);
                        self.removeExistDate(existDate);
                        dates.push(new CalendarItem(date, Ksu001u.TEXT_COLOR_PUB, Ksu001u.BG_COLOR_PRE_PUB, [Ksu001u.PUBLIC]));
                    }
                }               
            }
            self.optionDates(dates);
            self.newPublicDate(nextMonthPublicDate);
            self.yearMonthPicked(parseInt(nextMonthPublicDateSplit[0] + nextMonthPublicDateSplit[1]));
        }

        public registerShiftTable(): void {
            const self = this;
            let command: any = {
                unit: self.unit(),
                workplaceId: self.workplaceId(),
                workplaceGroupId: self.workplaceGroupId()
            };
            if (self.newEditDate()) {
                let editDateStr = self.newEditDate();
                command.editDate = editDateStr.replace('-', '/').replace('-', '/')
            }
            if (self.newPublicDate()) {
                let publicDateStr = self.newPublicDate();
                command.publicDate = publicDateStr.replace('-', '/').replace('-', '/');
            }

            self.$blockui("invisible");
            self.$ajax(Paths.REGISTER, command).done(() => {
                _.remove(self.optionDates());
                self.$blockui("clear");  
                self.$dialog.info({ messageId: "Msg_15" }).then(function() {
                    $('#prev-btn').focus();		
                });
                self.loadPubDateInfo();                               
            }).fail((res) => {
                self.$dialog.error({ messageId: res.messageId });
            }).always(() => {
                self.$blockui("clear");
            });            	
        }        

        public clearBtn(): void {
            const self = this;
            _.remove(self.optionDates());
            self.loadPubDateInfo();

        }

        closeDialog(): void {
            const vm = this;
            vm.$window.close();
        }

        private checkExistDate(date): any {
            const self = this;
            return _.find(self.optionDates(), x => x.start == date);
        }

        private removeExistDate(date): void {
            const self = this;
            for (let i = 0; i < self.optionDates().length; i++) {
                if (self.optionDates()[i] == date) {
                    self.optionDates().splice(i, 1);
                }
            }
        }

        /**
         * 
         * @param year number
         * @param month number
         */
        getNumberOfDays(year, month) {
            let isLeap = ((year % 4) == 0 && ((year % 100) != 0 || (year % 400) == 0));
            return [31, (isLeap ? 29 : 28), 31, 30, 31, 30, 31, 31, 30, 31, 30, 31][month - 1];
        }

        /**
         * 
         * @param date Convert Date to String with format yyyy-MM-dd
         */
        formatDate(date) {
            var d = new Date(date),
                month = '' + (d.getMonth() + 1),
                day = '' + d.getDate(),
                year = d.getFullYear();

            if (month.length < 2)
                month = '0' + month;
            if (day.length < 2)
                day = '0' + day;

            return [year, month, day].join('-');
        }
        /**
         * 
         * @param firstDate (String yyyy-MM-dd)
         * @param secondDate (String yyyy-MM-dd)
         */

        daysDifference(firstDate, secondDate) {
            var startDay = new Date(firstDate);
            var endDay = new Date(secondDate);

            var millisBetween = startDay.getTime() - endDay.getTime();
            var days = millisBetween / (1000 * 3600 * 24);

            return Math.round(Math.abs(days));
        }


    }
	
	 class CalendarItem {
        start: string;
        textColor: string;
        backgroundColor: string;
        listText: string[];
        constructor(start: string, textColor: string, backgroundColor: string, listText: string[]) {
            this.start = start;
            this.backgroundColor = backgroundColor;
            this.textColor = textColor;
            this.listText = listText;
        }
        changeListText(value: string[]){
            this.listText = value;     
        }
    }

    interface IPublicInfoOrg {
        unit: number;
        workplaceId: string;
        workplaceGroupId: string;
        displayName: string;
        publicDate: string;
        editDate: string;
    }

    class PublicInfoOrg {
        unit: KnockoutObservable<number> = ko.observable();
        workplaceId: KnockoutObservable<string> = ko.observable('');
        workplaceGroupId: KnockoutObservable<string> = ko.observable('');
        displayName: KnockoutObservable<string> = ko.observable('');
        publicDate: KnockoutObservable<string> = ko.observable('');
        editDate: KnockoutObservable<string> = ko.observable('');

        constructor(params?: IPublicInfoOrg) {
            const self = this;
            if (params) {
                self.unit(params.unit);
                self.workplaceId(params.workplaceId);
                self.workplaceGroupId(params.workplaceGroupId);
                self.displayName(params.displayName);
                self.publicDate(params.publicDate);
                if (params.editDate) {
                    self.editDate(params.editDate);
                }
            }
        }
    }
}
