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
            let data = valueAccessor();
            let $container = $(element);
                        
            let dateType = ko.unwrap(data.type);
            let maxRange = ko.unwrap(data.maxRange);
            let value = data.value; 
            let dataName = ko.unwrap(data.name);
            let enable = data.enable === undefined ? true : ko.unwrap(data.enable);
            let showNextPrevious = data.showNextPrevious === undefined ? false : ko.unwrap(data.showNextPrevious);
            let required = ko.unwrap(data.required);
            
            let id = nts.uk.util.randomId();
            
            $container.append("<div class='ntsDateRange_Container' id='"+ id +"' />");
            
            let $datePickerArea = $container.find(".ntsDateRange_Container"); 
            
            $datePickerArea.append("<div class='ntsDateRangeComponent ntsControl ntsDateRange'>" + 
                "<div class='ntsDateRangeComponent ntsStartDate ntsControl nts-datepicker-wrapper'/><div class='ntsDateRangeComponent ntsRangeLabel'><label>～</label></div>" +
                "<div class='ntsDateRangeComponent ntsEndDate ntsControl nts-datepicker-wrapper' /></div>");
            
            $datePickerArea.data("required", required);
            
            let dateFormat: string = (dateType !== 'yearmonth') ? "YYYY/MM/DD" : 'YYYY/MM';
            var ISOFormat = text.getISOFormat(dateFormat);
            ISOFormat = ISOFormat.replace(/d/g,"").trim();
            
            if (showNextPrevious === true) {
                $datePickerArea.append("<div class= 'ntsDateRangeComponent ntsDateNextButton_Container ntsRangeButton_Container'><button class = 'ntsDateNextButton ntsButton ntsDateRangeButton auto-height'/></div>");        
                $datePickerArea.prepend("<div class='ntsDateRangeComponent ntsDatePreviousButton_Container ntsRangeButton_Container'><button class = 'ntsDatePrevButton ntsButton ntsDateRangeButton auto-height'/></div>"); 
                
                let $nextButton = $container.find(".ntsDateNextButton");
                let $prevButton = $container.find(".ntsDatePrevButton");
                
                $nextButton.text("next").click(function (evt, ui){
                    let $startDate = $container.find(".ntsStartDatePicker");
                    let $endDate = $container.find(".ntsEndDatePicker");
                    
                    let oldValue = value(); 
                    let currentStart = $startDate.val();
                    let currentEnd = $endDate.val();   
                    if (!nts.uk.util.isNullOrEmpty(currentStart)){
                        let startDate = moment(currentStart, dateFormat);   
                        if(startDate.isValid()) {
                            let isEndOfMonth = startDate.daysInMonth() === startDate.date();
                            startDate.month(startDate.month() + 1);   
                            if(isEndOfMonth){
                                startDate.endOf("month");           
                            }
                            oldValue.startDate = startDate.format(dateFormat);         
                        }     
                    }    
                    
                    if (!nts.uk.util.isNullOrEmpty(currentEnd)){
                        let endDate = moment(currentEnd, dateFormat);   
                        if(endDate.isValid()) { 
                            let isEndOfMonth = endDate.daysInMonth() === endDate.date();
                            endDate.month(endDate.month() + 1);   
                            if(isEndOfMonth){
                                endDate.endOf("month");           
                            }
                            oldValue.endDate = endDate.format(dateFormat);         
                        }     
                    } 
                    value(oldValue);       
                });
                
                $prevButton.text("prev").click(function (evt, ui){
                    let $startDate = $container.find(".ntsStartDatePicker");
                    let $endDate = $container.find(".ntsEndDatePicker");
                    
                    let oldValue = value(); 
                    let currentStart = $startDate.val();
                    let currentEnd = $endDate.val();   
                    if (!nts.uk.util.isNullOrEmpty(currentStart)){
                        let startDate = moment(currentStart, dateFormat);   
                        if(startDate.isValid()) {
                            let isEndOfMonth = startDate.daysInMonth() === startDate.date();
                            startDate.month(startDate.month() - 1);   
                            if(isEndOfMonth){
                                startDate.endOf("month");           
                            }
                            oldValue.startDate = startDate.format(dateFormat);         
                        }     
                    }    
                    
                    if (!nts.uk.util.isNullOrEmpty(currentEnd)){
                        let endDate = moment(currentEnd, dateFormat);   
                        if(endDate.isValid()) {
                            let isEndOfMonth = endDate.daysInMonth() === endDate.date();
                            endDate.month(endDate.month() - 1);   
                            if(isEndOfMonth){
                                endDate.endOf("month");           
                            }
                            oldValue.endDate = endDate.format(dateFormat);         
                        }     
                    } 
                    value(oldValue);      
                });
            }
              
            let $startDateArea = $datePickerArea.find(".ntsStartDate");
            let $endDateArea = $datePickerArea.find(".ntsEndDate");
            
            $startDateArea.append("<input id='" + id + "-startInput'  class='ntsDatepicker nts-input ntsStartDatePicker' />");
            $endDateArea.append("<input id='" + id + "-endInput' class='ntsDatepicker nts-input ntsEndDatePicker' />");
            
            let $input = $container.find(".ntsDatepicker");
            // Init Datepicker
            $input.datepicker({
                language: 'ja-JP',
                format: ISOFormat,
                autoHide: true, 
            });
            
            dataName = nts.uk.util.isNullOrUndefined(dataName) ? "月日入力フォーム" : nts.uk.resource.getControlName(dataName);
            var validator = new validation.TimeValidator(dataName, "", {required: false, outputFormat: dateFormat, valueType: "string"});
            
            let $ntsDateRange = $container.find(".ntsRangeLabel");
            $input.on("change", (e) => {
                let $target = $(e.target);
                var newText = $target.val();
                $target.ntsError('clear');
                $ntsDateRange.ntsError("clear");
                var result = validator.validate(newText);
                
                let oldValue = value();
                if($target.hasClass("ntsStartDatePicker")){
                    oldValue.startDate = result.isValid ? result.parsedValue : newText;            
                } else {
                    oldValue.endDate = result.isValid ? result.parsedValue : newText;    
                }
                if(nts.uk.util.isNullOrEmpty(newText) && $datePickerArea.data("required") === true){
                    $target.ntsError('set', nts.uk.resource.getMessage('FND_E_REQ_INPUT', [ dataName ])); 
                } else if (!result.isValid) {
                    $target.ntsError('set', result.errorMessage);
                } else if (!nts.uk.util.isNullOrEmpty(newText)) {
                    let startDate = moment(oldValue.startDate, dateFormat);
                    let endDate = moment(oldValue.endDate, dateFormat);
                    if (endDate.isBefore(startDate)) {
                        $ntsDateRange.ntsError('set', "期間誤り");    
                    } else if(dateFormat === "YYYY/MM/DD" && maxRange === "oneMonth"){
                        let start = parseInt(startDate.format("YYYYMMDD"));
                        let end = parseInt(endDate.format("YYYYMMDD"));    
                        if(end - start > 31 || end - start < 0){
                            $ntsDateRange.ntsError('set', "最長期間違反");         
                        }
                    }  
                }
                
                value(oldValue);
            });
            
            $input.on("blur", (e) => {
                var newText = $(e.target).val();
                if(nts.uk.util.isNullOrEmpty(newText) && $datePickerArea.data("required") === true){
                    $(e.target).ntsError('set', nts.uk.resource.getMessage('FND_E_REQ_INPUT', [ dataName ])); 
                } else {
                    var result = validator.validate(newText);
                    if (!result.isValid) {
                        $(e.target).ntsError('set', result.errorMessage);
                    }    
                }
            });


            $input.on('validate', (function(e: Event) {
                let $target = $(e.target);
                var newText = $target.val(); 
                var result = validator.validate(newText);
                $target.ntsError('clear');
                $ntsDateRange.ntsError("clear");
                if(nts.uk.util.isNullOrEmpty(newText) && $datePickerArea.data("required") === true){
                    $target.ntsError('set', nts.uk.resource.getMessage('FND_E_REQ_INPUT', [ dataName ])); 
                } else if (!result.isValid) {
                    $target.ntsError('set', result.errorMessage);
                } else if (!nts.uk.util.isNullOrEmpty(newText)) {
                    $ntsDateRange.ntsError("clear");
                    let startDate = moment(oldValue.startDate, dateFormat);
                    let endDate = moment(oldValue.endDate, dateFormat);
                    if (endDate.isBefore(startDate)) {
                        $ntsDateRange.ntsError('set', "期間誤り");    
                    } else if(dateFormat === "YYYY/MM/DD" && maxRange === "oneMonth"){
                        let start = parseInt(startDate.format("YYYYMMDD"));
                        let end = parseInt(endDate.format("YYYYMMDD"));    
                        if(end - start > 31 || end - start < 0){
                            $ntsDateRange.ntsError('set', "最長期間違反");         
                        }
                    }  
                }
            }));
        }

        /**
         * Update
         */
        update(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            let data = valueAccessor();
            let $container = $(element);
            let dateType = ko.unwrap(data.type);
            let maxRange = ko.unwrap(data.maxRange);
            let dataName = ko.unwrap(data.name);
            let enable = data.enable === undefined ? true : ko.unwrap(data.enable);
            let required = ko.unwrap(data.required);
            
            let dateFormat: string = (dateType !== 'yearmonth') ? "YYYY/MM/DD" : 'YYYY/MM';
            var ISOFormat = text.getISOFormat(dateFormat);
            ISOFormat = ISOFormat.replace(/d/g,"").trim();
            
            let $input = $container.find(".ntsDatepicker");
            let $startDate = $container.find(".ntsStartDatePicker");
            let $endDate = $container.find(".ntsEndDatePicker");
            
            if(!nts.uk.util.isNullOrUndefined(data.value())){
                let startDate = (data.value().startDate !== "") ? time.formatPattern(data.value().startDate, dateFormat, ISOFormat) : "";
                let oldStart = $startDate.val(); 
                if(startDate !== oldStart){
                    if (startDate !== "" && startDate !== "Invalid date") {
                    // Check equals to avoid multi datepicker with same value
                        $startDate.datepicker('setDate', startDate);
                    } else {
                        $startDate.val("");
                    }     
                }   
                var endDate = (data.value().endDate !== "") ? time.formatPattern(data.value().endDate, dateFormat, ISOFormat) : "";
                let oldEnd = $endDate.val();
                if (endDate !== oldEnd){
                    if (endDate !== "" && endDate !== "Invalid date") {
                        // Check equals to avoid multi datepicker with same value
                        $endDate.datepicker('setDate', endDate);
                    } else {
                        $endDate.val("");
                    }       
                } 
            }
            
            $input.prop("disabled", !enable);
            $container.find(".ntsDateRangeButton").prop("disabled", !enable);
            let $datePickerArea = $container.find(".ntsDateRange_Container"); 
            $datePickerArea.data("required", required);
        }
    }

    ko.bindingHandlers['ntsDateRangePicker'] = new NtsDateRangePickerBindingHandler();
}