module nts.uk.com.view.cmm007.a {
    import viewModelTabB = nts.uk.com.view.cmm007.b.viewmodel;
    import viewModelTabC = nts.uk.com.view.cmm007.c.viewmodel;
    import viewModelTabD = nts.uk.com.view.cmm007.d.viewmodel;
    import viewModelTabE = nts.uk.com.view.cmm007.e.viewmodel;
    import viewModelTabG = nts.uk.com.view.cmm007.g.viewmodel;
    
    export module viewmodel {
        export class ScreenModel {
            
            systemDefine: KnockoutObservable<any>;
            temporaryAbsenceFr: KnockoutObservable<any>;
            planYearHolidayFr: KnockoutObservable<any>;
            overtimeWorkFr: KnockoutObservable<any>;
            workdayoffFr: KnockoutObservable<any>;
            
            constructor(){
                let _self = this;
                _self.systemDefine = ko.observable(new viewModelTabB.ScreenModel());
                _self.temporaryAbsenceFr = ko.observable(new viewModelTabC.ScreenModel());
                _self.planYearHolidayFr = ko.observable(new viewModelTabD.ScreenModel());
                _self.overtimeWorkFr = ko.observable(new viewModelTabE.ScreenModel());
                _self.workdayoffFr = ko.observable(new viewModelTabG.ScreenModel());
            }
            
            public start_page(): JQueryPromise<any> {
                
                var dfd = $.Deferred<any>();
                
                let _self = this;
                
                $.when(_self.systemDefine().start_page(),
                    _self.temporaryAbsenceFr().start_page(),
                    _self.planYearHolidayFr().start_page(),
                    _self.overtimeWorkFr().start_page(),
                    _self.workdayoffFr().start_page()).done(function() {
                        dfd.resolve(_self);
                    });
                
                return dfd.promise();
            }
            
            /**
             * on select tab handle
             */
            public onSelectTabB(): void {
                $('#com_person').focus();
                $("#sidebar").ntsSideBar("init", {
                    active: SideBarTabIndex.FIRST,
                    activate: (event, info) => {
                        $('#com_person').focus();
                        let _self = this;
                        _self.start_page();
                        _self.removeErrorMonitor();
                    }
                });
            }
            
             /**
             * on select tab handle
             */
            public onSelectTabC(): void {
                $("#sidebar").ntsSideBar("init", {
                    active: SideBarTabIndex.SECOND,
                    activate: (event, info) => {
                        $('#tempAbsenceNo7').focus();
                        let _self = this;
                        _self.start_page();
                        _self.removeErrorMonitor();
                    }
                });
            }
            
             /**
             * on select tab handle
             */
            public onSelectTabD(): void {
                 $("#sidebar").ntsSideBar("init", {
                    active: SideBarTabIndex.THIRD,
                    activate: (event, info) => {
                        $('#plan_year_hd_frame1').focus();
                        let _self = this;
                        _self.start_page();
                        _self.removeErrorMonitor();
                    }
                });   
            }
            
             /**
             * on select tab handle
             */
            public onSelectTabE(): void {
                 $("#sidebar").ntsSideBar("init", {
                    active: SideBarTabIndex.FOURTH,
                    activate: (event, info) => {
                        $('#overtime_work_name1').focus();
                        let _self = this;
                        _self.start_page();
                        _self.removeErrorMonitor();
                    }
                }); 
            }
            
             /**
             * on select tab handle
             */
            public onSelectTabG(): void {
                 $("#sidebar").ntsSideBar("init", {
                    active: SideBarTabIndex.FIFTH,
                    activate: (event, info) => {
                        $('#work_day_off_name1').focus();
                        let _self = this;
                        _self.start_page();
                        _self.removeErrorMonitor();
                    }
                });
            }
            
            /**
            *   remove all alert error all tab
            **/
            public removeErrorMonitor(): void {
                $('.nts-input').ntsError('clear');    
            }
       }    
    }
    
    module SideBarTabIndex {
        export const FIRST = 0;                        
        export const SECOND = 1;
        export const THIRD = 2;
        export const FOURTH = 3;
        export const FIFTH = 4;
    }
}