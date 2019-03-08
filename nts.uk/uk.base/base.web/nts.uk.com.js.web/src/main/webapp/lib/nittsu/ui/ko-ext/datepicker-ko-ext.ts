/// <reference path="../../reference.ts"/>

module nts.uk.ui.koExtentions {

    class DatePickerBindingHandler implements KnockoutBindingHandler {
        /**
         * Constructor.
         */
        constructor() {
        }

        /**
         * Init.
         */
        init(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            var data = valueAccessor();
            var value = data.value;
            var name = data.name !== undefined ? ko.unwrap(data.name) : "";
            var constraintName = (data.constraint !== undefined) ? ko.unwrap(data.constraint) : "";
            let pickOnly = !util.isNullOrUndefined(data.pickOnly) ? ko.unwrap(data.pickOnly) : false;
            var dateFormat: string = (data.dateFormat !== undefined) ? ko.unwrap(data.dateFormat) : "YYYY/MM/DD";
            var ISOFormat = text.getISOFormat(dateFormat);
            var hasDayofWeek: boolean = (ISOFormat.indexOf("ddd") !== -1);
            var dayofWeekFormat: string = ISOFormat.replace(/[^d]/g, "");
            ISOFormat = ISOFormat.replace(/d/g,"").trim();
            var valueFormat: string = (data.valueFormat !== undefined) ? ko.unwrap(data.valueFormat) : "";
            var required: boolean = (data.required !== undefined) ? ko.unwrap(data.required) : false;
            var button: boolean = (data.button !== undefined) ? ko.unwrap(data.button) : false;
            var startDate: any = (data.startDate !== undefined) ? ko.unwrap(data.startDate) : null;
            var endDate: any = (data.endDate !== undefined) ? ko.unwrap(data.endDate) : null;
            var focus: any = (data.focus !== undefined) ? ko.unwrap(data.focus) : false;
            var autoHide: boolean = (data.autoHide !== undefined) ? ko.unwrap(data.autoHide) : true;
            let acceptJapaneseCalendar: boolean = (data.acceptJapaneseCalendar !== undefined) ? ko.unwrap(data.acceptJapaneseCalendar) : true;
            var valueType:string = typeof value();
            
//            value.extend({ notify: 'always' });
            
            if (valueType === "string") {
                valueFormat = (valueFormat) ? valueFormat : text.getISOFormat("ISO");
            }
            else if (valueType === "number") {
                valueFormat = (valueFormat) ? valueFormat : ISOFormat;
            }
            else if (valueType === "object") {
                if (moment.isDate(value())) {
                    valueType = "date";
                }
                else if (moment.isMoment(value())) {
                    valueType = "moment";
                }
            }

            var container = $(element);
            let idString;
            if (!container.attr("id")) {
                idString = nts.uk.util.randomId();
            } else {
                idString = container.attr("id");
                container.removeAttr("id");    
            }
            
            let tabIndex = nts.uk.util.isNullOrEmpty(container.attr("tabindex")) ? "0" : container.attr("tabindex");
            container.removeAttr("tabindex");
            
            let containerClass = container.attr('class');
            container.removeClass(containerClass);
            container.addClass("ntsControl nts-datepicker-wrapper").data("init", true);
            var inputClass: string = (ISOFormat.length < 10) ? "yearmonth-picker" : "";
            var $input: any = $("<input id='" + container.attr("id") + "' class='ntsDatepicker nts-input reset-element' tabindex='" + tabIndex + "'/>").addClass(inputClass);
            $input.addClass(containerClass).attr("id", idString).attr("data-name", container.data("name"));
            container.append($input);
            $input.data("required", required);
            let jumpButtonsDisplay = data.showJumpButtons !== undefined ? ko.unwrap(data.showJumpButtons) : false; 
            let fiscalYear = data.fiscalYear !== undefined ? ko.unwrap(data.fiscalYear) : false;
            let $prevButton, $nextButton;
            if (jumpButtonsDisplay) {
                $prevButton = $("<button/>").addClass("ntsDateNextButton ntsButton ntsDatePickerButton ntsDatePicker_Component auto-height")
                                .text("◀").css("margin-right", "3px").attr("tabIndex", tabIndex);
                $nextButton = $("<button/>").addClass("ntsDatePrevButton ntsButton ntsDatePickerButton ntsDatePicker_Component auto-height")
                                .text("▶").css("margin-left", "3px").attr("tabIndex", tabIndex);
                $input.before($prevButton).after($nextButton);
            }
            if (data.dateFormat === "YYYY") {                
                let $yearType = $("<label/>").attr("for", idString)
                                                .css({ "position": "absolute",
                                                      "line-height": "30px",
                                                      "right": jumpButtonsDisplay ? "40px" : "5px"});
                let labelText = fiscalYear ? "年度" : "年"; 
                $yearType.text(labelText);
                container.append($yearType);
            }
            
            if (hasDayofWeek) {
                var lengthClass: string = (dayofWeekFormat.length > 3) ? "long-day" : "short-day";
                var $label: any = $("<label id='" + idString + "-label' for='" + idString + "' class='dayofweek-label' />");
                $input.addClass(lengthClass);
                container.append($label);
            }
            
            // Init Datepicker
            $input.datepicker({
                language: 'ja-JP',
                format: ISOFormat,
                startDate: startDate,
                endDate: endDate,
                autoHide: autoHide,
                weekStart: 0,
                zIndex:　11000
            }).data("dateNormalizer", DatePickerNormalizer.getInstance($input, $prevButton, $nextButton).setCssRanger(data.cssRanger)
                                .fiscalMonthsMode(data.fiscalMonthsMode)
                                .setDefaultCss(data.defaultClass || ""));

            name = nts.uk.resource.getControlName(name);
            
            if (pickOnly) {
                $input.attr("readonly", true);
                $input.css("cursor", "default");
            }
            
            $input.on("change", (e) => {
//                var onChanging = container.data("changed");
//                if(onChanging === true){
//                    return;
//                }
                
                if ($input.data("change")) {
                    $input.data("change", false);
                    return;
                }
                
                var newText = $input.val();
                var validator = new validation.TimeValidator(name, constraintName, {required: $input.data("required"), 
                                                    outputFormat: nts.uk.util.isNullOrEmpty(valueFormat) ? ISOFormat : valueFormat, 
                                                    valueType: valueType, acceptJapaneseCalendar: acceptJapaneseCalendar,
                                                    inputFormat: ISOFormat});
                var result = validator.validate(newText);
                $input.ntsError('clear');
                if (result.isValid) {
                    //if(!validateMinMax(result.parsedValue)){
                    //   return; 
                    //}
                    validateMinMax(result.parsedValue);
                    // Day of Week
                    if (hasDayofWeek) {
                        if (util.isNullOrEmpty(result.parsedValue))
                            $label.text("");
                        else
                            $label.text("(" + time.formatPattern(newText, "", dayofWeekFormat) + ")");
                    }
//                    container.data("changed", true);
                    if (typeof result.parsedValue === "string") {
                        $input.data("change", true);
                        if (_.has(data, "type") && ko.toJS(data.type) === "date") {
                            let momentDate = moment(result.parsedValue);
                            value(new Date(Date.UTC(momentDate.year(), momentDate.month(), momentDate.date())));
                        } else {
                            value(result.parsedValue);
                        }
                        $input.data("change", false);
                        let dateFormatValue = (value() !== "") ? text.removeFromStart(time.formatPattern(value(), valueFormat, ISOFormat), "0") : "";
                        if (dateFormatValue !== "" && dateFormatValue !== "Invalid date") {
                            $input.data("change", true);
                            $input.datepicker('setDate', new Date(dateFormatValue.replace(/\//g, "-")));
                        }
                    } else {
                        value(result.parsedValue);
                    }
                    value.valueWillMutate();
                }
                else {                    
                    $input.ntsError('set', result.errorMessage, result.errorCode, false);
//                    container.data("changed", true);
                    value(newText);
                }
                //$input.focus();
            });
            
            $input.on("blur", () => {
                var newText = $input.val();
                var validator = new validation.TimeValidator(name, constraintName, {required: $input.data("required"), 
                                                    outputFormat: nts.uk.util.isNullOrEmpty(valueFormat) ? ISOFormat : valueFormat, 
                                                    valueType: valueType, acceptJapaneseCalendar: acceptJapaneseCalendar,
                                                    inputFormat: ISOFormat});
                var result = validator.validate(newText);
                if (!result.isValid) {
                    $input.ntsError('set', result.errorMessage, result.errorCode, false);
                } else if (acceptJapaneseCalendar){
                    // Day of Week
                    if (hasDayofWeek) {
                        if (util.isNullOrEmpty(result.parsedValue))
                            $label.text("");
                        else
                            $label.text("(" + time.formatPattern(newText, "", dayofWeekFormat) + ")");
                    }
                    
//                    if(!util.isNullOrEmpty(result.parsedValue)){
//                        $input.val(moment(result.parsedValue).format(ISOFormat));    
//                    }
                }
            });
            
            var validateMinMax = function(parsedValue){
                if(nts.uk.util.isNullOrEmpty(parsedValue)){
                    return true;
                }
                var mmRs = new nts.uk.time.MomentResult();
                var otFormat = nts.uk.util.isNullOrEmpty(valueFormat) ? ISOFormat : valueFormat;
                var minDate = !nts.uk.util.isNullOrUndefined($input.data('startDate')) ? moment($input.data('startDate'), otFormat) : mmRs.systemMin();
                var maxDate = !nts.uk.util.isNullOrUndefined($input.data('endDate')) ? moment($input.data('endDate'), otFormat) : mmRs.systemMax();
                var momentCurrent = moment(parsedValue, otFormat);
                var error = false;
                if(momentCurrent.isBefore(minDate, 'day')){
                    error = true;
                } else if(momentCurrent.isAfter(maxDate, 'day')){
                    error = true;
                }    
                if(error){
                    var isHasYear = (nts.uk.util.isNullOrEmpty(otFormat) ? false : otFormat.indexOf("Y") >= 0) || otFormat.indexOf("Y") >= 0;
                    var isHasMonth = (nts.uk.util.isNullOrEmpty(otFormat) ? false : otFormat.indexOf("M") >= 0) || otFormat.indexOf("M") >= 0;
                    var isHasDay = (nts.uk.util.isNullOrEmpty(otFormat) ? false : otFormat.indexOf("D") >= 0) || otFormat.indexOf("D") >= 0;
                    var mesId = "FND_E_DATE_Y";
                    var fm = "YYYY";
                    if (isHasDay && isHasMonth && isHasYear) {
                        mesId = "FND_E_DATE_YMD", fm = "YYYY/MM/DD";
                    } else if (isHasMonth && isHasYear) {
                        mesId = "FND_E_DATE_YM", fm = "YYYY/MM";
                    } 
                    $input.ntsError('set', { messageId: mesId, messageParams: [ name, minDate.format(fm), maxDate.format(fm) ] }, mesId, false); 
                    if (hasDayofWeek) {
                        $label.text(""); 
                    }
                    // value(null);
                    return false;
                }
                return true
            };


            $input.on('validate', (function(e: Event) {
                var newText = $input.val();
                var validator = new validation.TimeValidator(name, constraintName, {required: $input.data("required"), 
                                                    outputFormat: nts.uk.util.isNullOrEmpty(valueFormat) ? ISOFormat : valueFormat, 
                                                    valueType: valueType, acceptJapaneseCalendar: acceptJapaneseCalendar,
                                                    inputFormat: ISOFormat});
                var result = validator.validate(newText);
                $input.ntsError('clearKibanError');
                if (!result.isValid) {
                    $input.ntsError('set', result.errorMessage, result.errorCode, false);
                } else if (acceptJapaneseCalendar){
                    // Day of Week
                    if (hasDayofWeek) {
                        if (util.isNullOrEmpty(result.parsedValue))
                            $label.text("");
                        else
                            $label.text("(" + time.formatPattern(newText, "", dayofWeekFormat) + ")");
                    }
                    
//                    if(!util.isNullOrEmpty(result.parsedValue)){
//                        $input.val(moment(result.parsedValue).format(ISOFormat));    
//                    }
                }
            }));
            
            new nts.uk.util.value.DefaultValue().onReset($input, data.value);
            container.data("init", false);
            
            $input.ntsDatepicker("bindFlip");
            $input.data('startDate', startDate);
            $input.data('endDate', endDate);
        }

        /**
         * Update
         */
        update(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            var data = valueAccessor();
            var value = data.value;
            var dateFormat: string = (data.dateFormat !== undefined) ? ko.unwrap(data.dateFormat) : "YYYY/MM/DD";
            var ISOFormat = text.getISOFormat(dateFormat);
            var hasDayofWeek: boolean = (ISOFormat.indexOf("ddd") !== -1);
            var dayofWeekFormat: string = ISOFormat.replace(/[^d]/g, "");
            ISOFormat = ISOFormat.replace(/d/g,"").trim();
            var valueFormat: string = (data.valueFormat !== undefined) ? ko.unwrap(data.valueFormat) : ISOFormat;
            var disabled: boolean = (data.disabled !== undefined) ? ko.unwrap(data.disabled) : false;
            var enable: boolean = (data.enable !== undefined) ? ko.unwrap(data.enable) : undefined;
            var startDate: any = (data.startDate !== undefined) ? ko.unwrap(data.startDate) : null;
            var endDate: any = (data.endDate !== undefined) ? ko.unwrap(data.endDate) : null;
            var required: boolean = (data.required !== undefined) ? ko.unwrap(data.required) : false;
            var focus: any = (data.focus !== undefined) ? ko.unwrap(data.focus) : false;
            var container = $(element); 
            let dateNormalizer = container.find("input").data("dateNormalizer");
            if (dateNormalizer) {
                if (data.cssRanger) {
                    dateNormalizer.setCssRanger(ko.unwrap(data.cssRanger));
                }
            }
            var init = container.data("init");
            var $input: any = container.find(".nts-input");
            var $label: any = container.find(".dayofweek-label");

            // Properties Binding
            $input.datepicker('setStartDate', startDate);
            $input.datepicker('setEndDate', endDate);
            $input.data('startDate', startDate);
            $input.data('endDate', endDate);

            // Value Binding
            if (value() !== $input.val()){
                var dateFormatValue = (value() !== "") ? text.removeFromStart(time.formatPattern(value(), valueFormat, ISOFormat), "0") : "";
                if (dateFormatValue !== "" && dateFormatValue !== "Invalid date") {
                    // Check equals to avoid multi datepicker with same value
                    $input.datepicker('setDate', new Date(dateFormatValue.replace(/\//g, "-")));
                    $label.text("(" + time.formatPattern(value(), valueFormat, dayofWeekFormat) + ")");
                } else if (dateFormatValue === "Invalid date" && (typeof value() === "string" || value() instanceof String)) {
                    $input.val(value());
                    $label.text("");
                    $input.trigger("validate");
                }
                else {
                    $input.val("");
                    $label.text("");
                }        
            }
//            container.data("changed", false);
            
            $input.data("required", required);
            
            if (enable !== undefined) {
               $input.prop("disabled", !enable);
               container.find(".ntsDatePickerButton").prop("disabled", !enable);
            } else{
                $input.prop("disabled", disabled);
                container.find(".ntsDatePickerButton").prop("disabled", disabled);
            }
            if($input.prop("disabled") === true){
                new nts.uk.util.value.DefaultValue().applyReset($input, value);
            }
            if (data.button)
                container.find('.datepicker-btn').prop("disabled", disabled);
            if(focus){
                $input.focus();
            }
        }
    }

    ko.bindingHandlers['ntsDatePicker'] = new DatePickerBindingHandler();
    
    enum ViewLocation {
        PREV,
        CURRENT,
        NEXT
    }
    
    class DatePickerNormalizer {
        private $input: JQuery;
        private $prev: JQuery;
        private $next: JQuery;
        // Body contents of picker
        private $view: JQuery;
        // Header part contains current year or month
        private $currentView: JQuery;
        private options: any;
        // Year or month or dates (the body contents)
        private selectedView: string; 
        // Current year that's displayed
        private viewYear: number;
        // Current month that's displayed
        private viewMonth: number;
        // Picked year displaying on input
        private year: number;
        // Picked month displaying on input
        private month: number;
        // Picked date displaying on input
        private date: number;
        private cssRanger: any;
        // Lowest level (months or days)
        private colorLevel: string;
        private defaultCss: string;
        private fiscalMonth: number = 1; 
        private defaultMonths: Array<string>;
        
        // Constants
        NAMESPACE: string = "datepicker";
        YEARS: string = "years";
        MONTHS: string = "months";
        DAYS: string = "days";
        WEEK: string = "week";
        PICKER: string = " picker";
        YEAR: string = "year";
        MONTH: string = "month";
        DAY: string = "day";
        YEAR_TEXT: string = "年";
        MONTH_TEXT: string = "月";
        PERIOD_TEXT: string = "度";
        structure = { 0: this.YEARS, 1: this.MONTHS, 2: this.DAYS };
        EVENT_SHOW: string = "show." + this.NAMESPACE; 
        EVENT_KEYUP: string = "keyup." + this.NAMESPACE;
        EVENT_PICK: string = "pick." + this.NAMESPACE;
        EVENT_CLICK: string = "click";
        Y_FORMAT: string = "YYYY";
        YM_FORMAT: string = "YYYY/MM";
        YMD_FORMAT: string = "YYYY/MM/DD";
        DATE_SPLITTER: string = "/";
        
        // Use this method to get an instance.
        static getInstance($input: JQuery, $prev?: JQuery, $next?: JQuery): DatePickerNormalizer {
            var instance = new DatePickerNormalizer();
            instance.$input = $input;
            instance.$prev = $prev;
            instance.$next = $next;
            return instance.onShow().onKeyup().onPick().onJump();
        }
        setCssRanger(range: any) {
            this.cssRanger = range;
            return this;
        }
        setFiscalMonth(month: number) {
            this.fiscalMonth = month;
            return this;
        }
        setDefaultCss(clazz: string) {
            this.defaultCss = clazz;
            return this;
        }
        fiscalMonthsMode(value: boolean) {
            if (value === true) this.setFiscalMonth(4);
            return this;
        }
        
        private getPicker(): JQuery {
            return this.$input.data(this.NAMESPACE).$picker;
        }
        private getYearsPicker(): JQuery {
            return this.$input.data(this.NAMESPACE).$yearsPicker;
        }
        private getMonthsPicker(): JQuery {
            return this.$input.data(this.NAMESPACE).$monthsPicker;
        }
        private getYearsBoard(): JQuery {
            return this.$input.data(this.NAMESPACE).$years;
        }
        private getMonthsBoard(): JQuery {
            return this.$input.data(this.NAMESPACE).$months;
        }
        private getCurrentYear(): JQuery {
            return this.$input.data(this.NAMESPACE).$yearCurrent;
        } 
        
        private getView(view: number, isCurrentView?: boolean): JQuery {
            var pickerView, viewPart, currentViewPart;
            var viewName = this.structure[view];
            switch (viewName) {
                case this.YEARS:
                    pickerView = this.YEARS + this.PICKER;
                    viewPart = this.YEARS;
                    currentViewPart = "current year";
                    break;
                case this.MONTHS:
                    pickerView = this.MONTHS + this.PICKER;
                    viewPart = this.MONTHS;
                    currentViewPart = "current month";
                    break;
                case this.DAYS:
                    pickerView = this.DAYS + this.PICKER;
                    viewPart = this.DAYS;
                    break;
                case this.WEEK:
                    pickerView = this.DAYS + this.PICKER;  
                    viewPart = this.WEEK;
                    break;
            }
            return $(this.getPicker()[0]).children().filter(function(idx, elm) { 
                        return $(elm).data("view") === pickerView;
                    }).find("ul").filter(function(idx, elm) {
                        if (isCurrentView === true) return idx === 0;
                        else return $(elm).data("view") === viewPart;
                    });
        }
        
        private getMutedClass(): string {
            return this.options !== undefined ? this.options.mutedClass : ""; 
        }
        
        private getPickedClass(): string {
            return this.options !== undefined ? this.options.pickedClass : "";
        }
        
        private setColorLevel(): void {
            if (this.options.format === this.Y_FORMAT) this.colorLevel = this.YEARS;
            else if (this.options.format === this.YM_FORMAT) this.colorLevel = this.MONTHS;
            else if (this.options.format === this.YMD_FORMAT) this.colorLevel = this.DAYS;
            // Only set to colorLevel in initialization.
            if (this.selectedView === undefined) this.selectedView = this.colorLevel;
        }
        
        color(): void {
            if (this.cssRanger === undefined) return;
            // Year only picker
            if (this.cssRanger.constructor === Array) {
                _.each(this.cssRanger, (cell) => this.colorCell(cell, ViewLocation.CURRENT, -1));
                return;
            }
            this.colorNode(this.cssRanger, ViewLocation.CURRENT, 0);
            this.colorNode(this.cssRanger, ViewLocation.NEXT, 0);
            this.colorNode(this.cssRanger, ViewLocation.PREV, 0);
        }
        
        private colorNode(holders: any, location: ViewLocation, currentLayer?: number): void {
            var holder;
            var handledYear = location === ViewLocation.CURRENT ? this.viewYear : this.viewYear + 1;
            // If processing layer is 1 level above leaf layer
            if (this.colorLevel === this.structure[currentLayer + 1]) {
                switch (currentLayer) {
                    case 0:
                        holder = handledYear;
                        break;
                    case 1:
                        if (location === ViewLocation.CURRENT) holder = this.viewMonth;
                        else if (location === ViewLocation.PREV) holder = this.viewMonth - 1;
                        else holder = this.viewMonth + 1;
                        break;
                    case 2:
                        holder = this.date;
                        break; 
                    default:
                        holder = handledYear;
                        currentLayer = 0;
                        break;
                }
            } else {
                switch (currentLayer) {
                    case 0:
                        holder = this.viewYear;
                        break;
                    case 1:
                        holder = this.viewMonth;
                        break;
                    case 2:
                        holder = this.date;
                        break; 
                    default:
                        holder = this.viewYear;
                        currentLayer = 0;
                        break;
                }
            }
            
            if (holders.hasOwnProperty(holder)) {
                if (holders[holder].constructor === Array) {
                    _.each(holders[holder], (cell) => this.colorCell(cell, location, currentLayer));
                    return;
                }
                currentLayer++;
                this.colorNode(holders[holder], location, currentLayer);
            }
        }
        
        private colorCell(cell: any, location: ViewLocation, layer: number): void {
            var self = this;
            var data: any = typeof cell === "object" ? Object.keys(cell)[0] : cell;
            var $target = this.$view.children().filter(function(idx, elm) {
                if (self.structure[layer] === self.YEARS) {
                    return $(elm).text() === self.defaultMonths[data - 1]
                        && ((location === ViewLocation.PREV && $(elm).data("view").indexOf("prev") !== -1) 
                        || (location === ViewLocation.NEXT && $(elm).data("view").indexOf("next") !== -1)
                        || location === ViewLocation.CURRENT && $(elm).data("view").indexOf("prev") === -1 
                            && $(elm).data("view").indexOf("next") === -1);
                } else if (self.structure[layer] === self.MONTHS) {
                    return $(elm).text() === data.toString()
                        && ((location === ViewLocation.PREV && $(elm).data("view").indexOf("prev") !== -1) 
                        || (location === ViewLocation.NEXT && $(elm).data("view").indexOf("next") !== -1)
                        || location === ViewLocation.CURRENT && $(elm).data("view").indexOf("prev") === -1 
                            && $(elm).data("view").indexOf("next") === -1);
                } else if (layer === -1) {
                    return $(elm).text() === data.toString();
                }
            });
            if ($target.length > 0) {
                $target.addClass((typeof cell === "object" && cell[data] !== undefined) ? cell[data] : this.defaultCss);
            }
        }
        
        fillFiscalMonthsInYear(): void {
            var self = this;
            if (this.fiscalMonth === 1) return;
            var nextYearMonths = this.defaultMonths.slice(0, this.fiscalMonth - 1);
            var currentYearMonths = this.defaultMonths.slice(this.fiscalMonth - 1);
            var newMonths = $.merge(currentYearMonths, nextYearMonths);
            var nextYearMark = 12 - this.fiscalMonth;
            this.getMonthsBoard().children().each((idx: number, elm: any) => {
                $(elm).text(newMonths[idx]); 
                if (idx > nextYearMark) 
                    $(elm).addClass(self.getMutedClass()).attr("data-view", "fiscalMonth next")
                           .data("view", "fiscalMonth next").css("font-size", "inherit");
            });
            
            var $currentYear = this.getCurrentYear();
            if ($currentYear.length > 0) $currentYear.text(this.viewYear + this.yearText());
        }
        
        allowPickMonth(): boolean {
            return (this.viewMonth < this.fiscalMonth && this.viewYear === this.year - 1)
                || (this.viewMonth >= this.fiscalMonth && this.viewYear === this.year);
        }
        
        allowPickDate(): boolean {
            return this.viewYear === this.year && this.viewMonth === this.month;
        }
        
        pickMonth(): void {
            var self = this; 
            if (self.fiscalMonth === 1) return;
            var month = self.month + self.MONTH_TEXT;
            this.getMonthsBoard().children().each((idx, elm) => {
                var view;
                if ($(elm).text() === month.toString()) {
                    view = "month picked";
                    $(elm).addClass(self.getPickedClass()).attr("data-view", view).data("view", view);
                } else if ($(elm).hasClass(self.getPickedClass())) {
                    view = $(elm).data("view").split(" ")[0];
                    $(elm).removeClass(self.getPickedClass()).attr("data-view", view).data("view", view);
                }
            });
        }
        
        pickDate(): void {
            var self = this;
            if (self.colorLevel !== self.DAYS || self.fiscalMonth === 1) return;
            var date = self.date;
            this.$view.children().each((idx, elm) => {
                if ($(elm).text() === date.toString() && $(elm).data("view").indexOf("prev") === -1 
                    && $(elm).data("view").indexOf("next") === -1) {
                    $(elm).addClass(self.getPickedClass()).attr("data-view", "day picked").data("view", "day picked");
                } else if ($(elm).hasClass(self.getPickedClass())) {
                    $(elm).removeClass(self.getPickedClass()).attr("data-view", "day").data("view", "day");
                }
            });
        }
        
        clearPicked(): void {
            var self = this;
            var view = self.colorLevel === self.MONTHS ? "month" : "day";
            var $selectedBoard;
            if (this.selectedView === this.MONTHS) {
                $selectedBoard = this.getMonthsBoard();
            } else if (this.selectedView === this.DAYS) {
                $selectedBoard = this.getYearsBoard();
            }
            if ($selectedBoard === undefined) return;
            $selectedBoard.children().filter((idx, elm) => {
                return $(elm).data("view").indexOf("picked") !== -1;
            }).removeClass(self.getPickedClass()).attr("data-view", view).data("view", view);
        }
        
        yearText(): string {
            return this.fiscalMonth !== 1 ? this.YEAR_TEXT + this.PERIOD_TEXT : this.YEAR_TEXT;
        }
        
        onClick(): void {
            var self = this;
            var picker = this.getPicker();
            picker.off("click", this._click);
            picker.on("click", $.proxy(this._click, this));
        } 
        
        _click(evt: any): void {
            var $target = $(evt.target);
            var view = $target.data("view");
            switch (view) {
                case "years prev":
                case "years next":
                    this.updateYearsView();
                    break;
                case "year prev":
                    this.viewYear--;
                    this.updateMonthsView();
                    break;
                case "year next":
                    this.viewYear++;
                    this.updateMonthsView();
                    break;
                case "month prev":
                    if (this.viewMonth == 1) {
                        this.viewMonth = 12;
                        this.viewYear--;
                    } else this.viewMonth--;
                    this.updateDaysView();
                    break;
                case "month next":
                    if (this.viewMonth == 12) { 
                        this.viewMonth = 1;
                        this.viewYear++;
                    } else this.viewMonth++;
                    this.updateDaysView();
                    break;
                case "day prev":
                    this.updateDaysView();
                    break;
                case "day next":
                    this.updateDaysView();
                    break;
                case "fiscalMonth next":
                    if ($target.hasClass(this.getPickedClass())) return;
                    var pickedMonth = this.defaultMonths.indexOf($target.text());
                    this._clickFiscalNextMonth(pickedMonth); 
                    this.$input.datepicker("hide");
                    if (this.colorLevel === this.DAYS) {
                        this.$input.datepicker("show");
                    }
                    break;
                case "year current":
                    this.selectedView = this.YEARS;
                    break;
                case "month current":
                    this.selectedView = this.MONTHS;
                    if (this.viewMonth < this.fiscalMonth) this.viewYear--;
                    this.updateMonthsView();
                    break;
            }
        }
        
        updateYearsView(): void {
            this.color();
        }
        
        updateMonthsView(): void {
            if (this.fiscalMonth !== 1) {
                this.fillFiscalMonthsInYear();
            } 
            if (this.colorLevel === this.MONTHS) {
                this.color();
            }
            if (this.allowPickMonth()) this.pickMonth();
            
            if (this.viewMonth < this.fiscalMonth && this.viewYear === this.year) this.clearPicked();
        }
        
        updateDaysView(): void {
            if (this.colorLevel === this.DAYS) {
                this.color();
            }
            if (this.allowPickDate()) this.pickDate();
        }
        
        _beforeShow(): void {
            this.options = this.$input.data(this.NAMESPACE).options;
            this.setColorLevel();
            this.defaultMonths = this.options.monthsShort;
            var text = this.$input.val();
            var parsedTextTime = this.parseDate(text);
            if (parsedTextTime !== undefined && parsedTextTime.month === 2) {
                this.viewYear = this.year = parsedTextTime.year;
                this.viewMonth = this.month = parsedTextTime.month;
                this.date = parsedTextTime.date;
            } else {
                var initValue = this.$input.datepicker("getDate", true);
                var viewTime = this.$input.data(this.NAMESPACE).viewDate;
                this.viewYear = viewTime.getFullYear();
                this.viewMonth = viewTime.getMonth() + 1;
                var parsedTime = this.parseDate(initValue);
                if (parsedTime !== undefined) {
                    this.year = parsedTime.year;
                    this.month = parsedTime.month;
                    this.date = parsedTime.date;
                } else return;
            }
            
            var colorLevel = this.colorLevel;
            var layer;
            if (colorLevel === this.YEARS) {
                layer = 0;
            } else if (colorLevel === this.MONTHS) {
                 layer = 1;
            } else if (colorLevel === this.DAYS) {
                layer = 2;
            }
            this.$view = this.getView(layer);
            this.$currentView = this.getView(layer, true);
            // Body contents of picker is showing months
            if (this.selectedView === this.MONTHS) {
                if (this.viewMonth < this.fiscalMonth) this.viewYear--;
                this.fillFiscalMonthsInYear();
            }
            this.color();
            // Pick time
            if (this.selectedView === this.MONTHS && this.allowPickMonth()) {
                if (this.viewMonth < this.fiscalMonth && this.viewYear === this.year) this.clearPicked();
                this.pickMonth();
            } else if (this.selectedView === this.DAYS && this.allowPickDate()) {
                this.pickDate();
            }
        }
        
        parseDate(date: string): any {
            var exp = /\d+(\/\d+)?(\/\d+)?/;
            if (exp.test(date) === false) return;
            var dateParts = date.split(this.DATE_SPLITTER);
            return {
                        year: parseInt(dateParts[0]),
                        month: parseInt(dateParts[1]),
                        date: dateParts[2] !== undefined ? parseInt(dateParts[2]) : undefined
                   };
        }
        
        onShow(): DatePickerNormalizer {
            var self = this;
            this.$input.on(this.EVENT_SHOW, function(evt) {
                var _self = self;
                setTimeout(function() {
                    if (!_self.validDate()) return; 
                    _self._beforeShow.call(_self);
                    _self.onClick.call(_self);
                }, 0);
            });
            return self;
        }
        
        onKeyup(): DatePickerNormalizer {
//            this.$input.off(this.EVENT_KEYUP, this._beforeShow);
//            this.$input.on(this.EVENT_KEYUP, $.proxy(this._beforeShow, this));
            let self = this;
            this.$input.on(this.EVENT_KEYUP, () => {
                self.validDate();
            });
            
            return this;
        }
        
        validDate() {
            let self = this;
            let current, prev, next, b, ns = self.$input.data(self.NAMESPACE);
            if (!isNaN(ns.date)) return true;
            if (!self.colorLevel) self.setColorLevel();
            if (self.colorLevel === self.YEARS) {
                current = ns.$yearsCurrent;
                prev = ns.$yearsPrev;
                next = ns.$yearsNext;
                b = ns.$years;
            } else if (self.colorLevel === self.MONTHS) {
                current = ns.$yearCurrent;
                prev = ns.$yearPrev;
                next = ns.$yearNext;
                b = ns.$months;
            } else if (self.colorLevel === self.DAYS) {
                current = ns.$monthCurrent;
                prev = ns.$monthPrev;
                next = ns.$monthNext;
                b = ns.$days;
            }
            
            current.html("");
            current.addClass(ns.options.disabledClass);
            prev.addClass(ns.options.disabledClass);
            next.addClass(ns.options.disabledClass);
            b.find("li").each(function() {
                let e = $(this); 
                e.addClass(ns.options.disabledClass);
                if (self.colorLevel === self.YEARS) {
                    e.html("");
                }
            });
        }
        
        onPick(): DatePickerNormalizer {
            var self = this;
            this.$input.on(this.EVENT_PICK, function(evt: any) {
                var view = evt.view; // month | year
                if (view === self.DAY) {
                    self.date = evt.date.getDate();
                    self.month = evt.date.getMonth() + 1;
                    self.viewMonth = self.month;
                    self.year = evt.date.getFullYear();
                    self.viewYear = self.year;
                } else if (view === self.MONTH) {
                    self._clickFiscalNextMonth.call(self, evt.date.getMonth());
                } else if (view === self.YEAR) {
                    var _self = self;
                    setTimeout(function() {
                        _self.year = evt.date.getFullYear();
                        _self.viewYear = _self.year;
                        _self.month = _self.viewMonth;
                        if (_self.viewMonth < _self.fiscalMonth) _self.viewYear--;
                        _self.updateMonthsView.call(_self);
                    }, 0);
                }
                
                if (!_.isNil(view) && view !== "") {
                    self.$input.focus();
                }
            });
            return self;
        }
        
        _clickFiscalNextMonth(pickedMonth: number) {
            var self = this;
            self.month = pickedMonth + 1;
            self.viewMonth = self.month;
            // Fiscal month mode
            if (self.fiscalMonth !== 1) {
                self.year = self.month >= self.fiscalMonth ? self.viewYear : (self.viewYear + 1);
                self.viewYear = self.year;
                self.$input.datepicker("setDate", new Date(self.year, self.month - 1, self.date || 1));
            }  
        }
        
        onJump(): DatePickerNormalizer {
            var self = this;
            if (util.isNullOrUndefined(self.$prev) || util.isNullOrUndefined(self.$next)) return self; 
            this.$prev.on(this.EVENT_CLICK, function(evt: any) {
                self.addTime(-1);
            });
            
            this.$next.on(this.EVENT_CLICK, function(evt: any) {
                self.addTime(1);
            });
            return self;
        }
        
        addTime(value: number) {
            let self = this;
            let year, month, date;
            if (self.options === undefined) self.options = self.$input.data(self.NAMESPACE).options;
            
            let time = self.$input.datepicker("getDate", true);
            let parsedTime = self.parseDate(time);
            if (parsedTime !== undefined) {
                if (self.options.format === self.YMD_FORMAT) {
                    year = parsedTime.year;
                    month = parsedTime.month - 1;
                    date = parsedTime.date + value;
                } else if (self.options.format === self.YM_FORMAT) {
                    let postCalcVal = parsedTime.month + value;
                    date = 1;
                    if (postCalcVal < 1) {
                        year = parsedTime.year - 1;
                        month = 11;
                    } else if (postCalcVal > 12) {
                        year = parsedTime.year + 1;
                        month = postCalcVal - 13;
                    } else {
                        year = parsedTime.year;
                        month = postCalcVal - 1;
                    }
                } else if (self.options.format === self.Y_FORMAT) {
                    let postCalcVal = parsedTime.year + value;
                    if (postCalcVal < 1900) {
                        year = 9999;
                    } else if (postCalcVal > 9999) {
                        year = 1900;
                    } else year = postCalcVal;
                    month = 1;
                    date = 1;
                }
            }
            self.$input.datepicker("setDate", new Date(year, month, date));
        }
    }
}
