/// <reference path="../../reference.ts"/>

module nts.uk.ui.koExtentions {
    
    /**
     * Dialog binding handler
     */
    class NtsDateRangePickerBindingHandler implements KnockoutBindingHandler {

        /**
         * Init. sssss 
         */
        init(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            let data = valueAccessor(),
                $container = $(element),
                construct: DateRangeHelper = new DateRangeHelper($container),
                value = ko.unwrap(data.value);
            
            construct.bindInit(data, allBindingsAccessor, viewModel, bindingContext);
            
            $container.data("construct", construct);
            
            return { 'controlsDescendantBindings': true };
        }

        /**
         * Update
         */
        update(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            let data = valueAccessor(),
                $container = $(element),
                enable = data.enable === undefined ? true : ko.unwrap(data.enable),
                required = ko.unwrap(data.required),
                construct: DateRangeHelper = $container.data("construct"),
                value = ko.unwrap(data.value);
            
            if(!nts.uk.util.isNullOrUndefined(value)){
                construct.startValue(nts.uk.util.isNullOrUndefined(value.startDate) ? "" : value.startDate);
                construct.endValue(nts.uk.util.isNullOrUndefined(value.endDate) ? "" : value.endDate);    
            }
            ko.bindingHandlers["ntsDatePicker"].update(construct.$start[0], function() {
                return construct.createStartBinding(data);
            }, allBindingsAccessor, viewModel, bindingContext);
            ko.bindingHandlers["ntsDatePicker"].update(construct.$end[0], function() {
                return construct.createEndBinding(data);
            }, allBindingsAccessor, viewModel, bindingContext);
            
            if(enable === false){
                $container.find(".ntsDateRange_Component").removeAttr("tabindex");    
            } else {
                $container.find(".ntsDateRange_Component").attr("tabindex", $container.data("tabindex"));         
            } 
            $container.find(".ntsDateRangeButton").prop("disabled", !enable);
            let $datePickerArea = $container.find(".ntsDateRange_Container"); 
            $datePickerArea.data("required", required);
        }
    }

    ko.bindingHandlers['ntsDateRangePicker'] = new NtsDateRangePickerBindingHandler();
    
    class DateRangeHelper {
        $container: JQuery;
        startValue: KnockoutObservable<string>;
        endValue: KnockoutObservable<string>;
        value: KnockoutObservable<any>;
        getMessage: any;
        $datePickerArea: JQuery;
        dateFormat: string;
        startName: string;
        endName: string;
        rangeName: string;
        maxRange: string;
        $ntsDateRange: JQuery;
        $start: JQuery;
        $end: JQuery;
        
        constructor($element: JQuery){
            this.$container = $element;        
        }
        
        public bindInit(parentBinding: any, allBindingsAccessor, viewModel, bindingContext){
            let self = this;
            self.value = parentBinding.value;
            self.startValue = ko.observable(nts.uk.util.isNullOrUndefined(self.value().startDate) ? "" : self.value().startDate);
            self.endValue = ko.observable(nts.uk.util.isNullOrUndefined(self.value().endDate) ? "" : self.value().endDate);
            
            self.startValue.subscribe((v) => {
                let oldValue = self.value();
                
                oldValue.startDate = v;  
                
                self.validateProcess(v, oldValue);
                self.value(oldValue);
            });
            
            self.endValue.subscribe((v) => {
                let oldValue = self.value();
                
                oldValue.endDate = v;  
                
                self.validateProcess(v, oldValue);
                self.value(oldValue);
            });
            
            self.bindControl(parentBinding, allBindingsAccessor, viewModel, bindingContext);
        }
        
        private bindControl(data: any, allBindingsAccessor, viewModel, bindingContext){
            let self = this, dateType = ko.unwrap(data.type), maxRange = ko.unwrap(data.maxRange), rangeName = ko.unwrap(data.name),
                startName = ko.unwrap(data.startName), endName = ko.unwrap(data.endName), 
                showNextPrevious = data.showNextPrevious === undefined ? false : ko.unwrap(data.showNextPrevious),
                jumpUnit = data.jumpUnit === undefined ? "month" : ko.unwrap(data.jumpUnit),
                id = nts.uk.util.randomId(), required = ko.unwrap(data.required),
                tabIndex = nts.uk.util.isNullOrEmpty(self.$container.attr("tabindex")) ? "0" : self.$container.attr("tabindex");
            
            self.maxRange = maxRange;
            self.$container.data("tabindex", tabIndex);
            self.$container.removeAttr("tabindex");
            
            self.$container.append("<div class='ntsDateRange_Container' id='"+ id +"' />");
            
            self.$datePickerArea = self.$container.find(".ntsDateRange_Container"); 
            
            self.$datePickerArea.append("<div class='ntsDateRangeComponent ntsControl ntsDateRange'>" + 
                "<div class='ntsDateRangeComponent ntsStartDate ntsControl nts-datepicker-wrapper'/><div class='ntsDateRangeComponent ntsRangeLabel'><label>～</label></div>" +
                "<div class='ntsDateRangeComponent ntsEndDate ntsControl nts-datepicker-wrapper' /></div>");
            
            self.$datePickerArea.data("required", required);
            
            if(dateType === 'year') {
                self.dateFormat = 'YYYY'; 
            } else if(dateType === 'yearmonth') {
                self.dateFormat = 'YYYY/MM'; 
            } else {
                self.dateFormat = 'YYYY/MM/DD'; 
            }
            var ISOFormat = text.getISOFormat(self.dateFormat);
            ISOFormat = ISOFormat.replace(/d/g,"").trim();
            
            if (showNextPrevious === true) {
                self.bindJump(jumpUnit);
            }
              
            let $startDateArea = self.$datePickerArea.find(".ntsStartDate");
            let $endDateArea = self.$datePickerArea.find(".ntsEndDate");
            
            $startDateArea.append("<div id='" + id + "-startInput'  class='ntsDatepicker nts-input ntsStartDatePicker ntsDateRangeComponent ntsDateRange_Component' />");
            $endDateArea.append("<div id='" + id + "-endInput' class='ntsDatepicker nts-input ntsEndDatePicker ntsDateRangeComponent ntsDateRange_Component' />");
            self.$start = $startDateArea.find(".ntsStartDatePicker");
            self.$end = $endDateArea.find(".ntsEndDatePicker");
            let $input = self.$container.find(".nts-input");
            // Init Datepicker
//            $input.datepicker({
//                language: 'ja-JP',
//                format: ISOFormat,
//                autoHide: true,
//                weekStart: 0
//            });
            
            self.rangeName = nts.uk.util.isNullOrUndefined(rangeName) ? "期間入力フォーム" : nts.uk.resource.getControlName(rangeName);
            self.startName = nts.uk.util.isNullOrUndefined(startName) ? "期間入力フォーム開始" : nts.uk.resource.getControlName(startName);
            self.endName = nts.uk.util.isNullOrUndefined(endName) ? "期間入力フォーム終了" : nts.uk.resource.getControlName(endName);
            self.getMessage = nts.uk.resource.getMessage;
            
            ko.bindingHandlers["ntsDatePicker"].init(self.$start[0], function() {
                return self.createStartBinding(data);
            }, allBindingsAccessor, viewModel, bindingContext);
            ko.bindingHandlers["ntsDatePicker"].init(self.$end[0], function() {
                return self.createEndBinding(data);
            }, allBindingsAccessor, viewModel, bindingContext);  
            
            self.$ntsDateRange = self.$container.find(".ntsRangeLabel");
            
            $input.on('validate', (function(e: Event) {
                let $target = $(e.target);
                var newText = $target.val(); 
                let oldValue = self.value();
                
//                $target.ntsError('clear');
//                self.$ntsDateRange.ntsError("clear");
                if(!$target.ntsError('hasError')){
                    self.$ntsDateRange.ntsError("clear"); 
                    self.validateProcess(newText, oldValue);
                }
            }));
            
            self.$container.find(".ntsDateRange_Component").attr("tabindex", tabIndex);
        }
        
        private bindJump(jumpUnit: string){
            let self = this;
            self.$datePickerArea.append("<div class= 'ntsDateRangeComponent ntsDateNextButton_Container ntsRangeButton_Container'>" + 
                "<button class = 'ntsDateNextButton ntsButton ntsDateRangeButton ntsDateRange_Component auto-height'/></div>");        
            self.$datePickerArea.prepend("<div class='ntsDateRangeComponent ntsDatePreviousButton_Container ntsRangeButton_Container'>" + 
                "<button class = 'ntsDatePrevButton ntsButton ntsDateRangeButton ntsDateRange_Component auto-height'/></div>"); 
            
            let $nextButton = self.$container.find(".ntsDateNextButton").text("▶").css("margin-left", "3px");
            let $prevButton = self.$container.find(".ntsDatePrevButton").text("◀").css("margin-right", "3px");
            
            $nextButton.click(function (evt, ui){
                self.jump(true, jumpUnit);      
            });
            
            $prevButton.click(function (evt, ui){
                self.jump(false, jumpUnit);     
            });
        }
        
        private jump(isNext: boolean, jumpUnit: string){
            let self = this, $startDate = self.$container.find(".ntsStartDatePicker"),
                $endDate = self.$container.find(".ntsEndDatePicker"), oldValue = self.value(),
                currentStart = $startDate.val(),
                currentEnd = $endDate.val();   
            
            if (!nts.uk.util.isNullOrEmpty(currentStart)){
                let startDate = moment(currentStart, self.dateFormat);   
                if(startDate.isValid()) {
                    if(jumpUnit === "year"){
                        startDate.year(startDate.year() + (isNext ? 1 : -1));   
                    } else {
                        let isEndOfMonth = startDate.daysInMonth() === startDate.date();
                        startDate.month(startDate.month() + (isNext ? 1 : -1));    
                        if(isEndOfMonth){
                            startDate.endOf("month");           
                        }      
                    } 
                    oldValue.startDate = startDate.format(self.dateFormat);         
                }     
            }    
            
            if (!nts.uk.util.isNullOrEmpty(currentEnd)){
                let endDate = moment(currentEnd, self.dateFormat);   
                if(endDate.isValid()) {
                    if(jumpUnit === "year"){
                        endDate.year(endDate.year() + (isNext ? 1 : -1));   
                    } else {
                        let isEndOfMonth = endDate.daysInMonth() === endDate.date();
                        endDate.month(endDate.month() + (isNext ? 1 : -1));    
                        if(isEndOfMonth){
                            endDate.endOf("month");           
                        }   
                    }
                    oldValue.endDate = endDate.format(self.dateFormat);         
                }     
            } 
            self.value(oldValue);      
        }
        
        private validateProcess (newText: string, oldValue: any){
            let self = this;
            if(self.$start.find("input").ntsError("hasError") || self.$end.find("input").ntsError("hasError")) {
                return;       
            }
            self.$ntsDateRange.ntsError("clear");
            
            let startDate = moment(oldValue.startDate, self.dateFormat);
            let endDate = moment(oldValue.endDate, self.dateFormat);
            if (endDate.isBefore(startDate)) {
                self.$ntsDateRange.ntsError('set', self.getMessage("FND_E_SPAN_REVERSED", [self.rangeName]), "FND_E_SPAN_REVERSED");    
            } else if(self.dateFormat === "YYYY/MM/DD" && self.maxRange === "oneMonth"){
                let maxDate = startDate.add(31, "days");
                if(endDate.isSameOrAfter(maxDate)){
                    self.$ntsDateRange.ntsError('set', self.getMessage("FND_E_SPAN_OVER_MONTH", [self.rangeName]), "FND_E_SPAN_OVER_MONTH");         
                }
            } else if (self.maxRange === "oneYear"){
                let maxDate = _.cloneDeep(startDate);
                if(self.dateFormat === "YYYY/MM/DD"){
                    let currentDate = startDate.date();
                    let isEndMonth = currentDate === startDate.endOf("months").date();
                    let isStartMonth = currentDate === 1;
                    maxDate = maxDate.date(1).add(1, 'year');
                    if(isStartMonth){
                        maxDate = maxDate.month(maxDate.month() - 1).endOf("months");
                    } else if(isEndMonth){
                        maxDate = maxDate.endOf("months").add(-1, "days");        
                    } else {
                        maxDate = maxDate.date(currentDate - 1);    
                    }    
                } else if(self.dateFormat === "YYYY/MM"){
                    maxDate = maxDate.add(1, 'year').add(-1, "months");   
                } else {
                    maxDate = maxDate.add(1, 'year');
                }
                if (endDate.isAfter(maxDate)) {
                    self.$ntsDateRange.ntsError('set', self.getMessage("FND_E_SPAN_OVER_YEAR", [self.rangeName]), "FND_E_SPAN_OVER_YEAR");        
                }
            }  
        }
        
        public createStartBinding(parentBinding: any, name, format: string): any {
            let self = this;
            return { required: parentBinding.required, 
                     name: parentBinding.startName ? self.startName : parentBinding.name, 
                     value: self.startValue, 
                     dateFormat: self.dateFormat, 
                     valueFormat: self.dateFormat, 
                     enable: parentBinding.enable, 
                     disabled: parentBinding.disabled 
                     //,endDate: self.endValue 
                   };
        }
        
        public createEndBinding(parentBinding: any, name: string): any {
            let self = this;
            return { required: parentBinding.required, 
                     name: parentBinding.endName ? self.endName : parentBinding.name, 
                     value: self.endValue, 
                     dateFormat: self.dateFormat, 
                     valueFormat: self.dateFormat, 
                     enable: parentBinding.enable, 
                     disabled: parentBinding.disabled 
                     //,startDate: self.startValue 
                   };
        }
    }
}
