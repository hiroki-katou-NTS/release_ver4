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
            var autoHide: boolean = (data.autoHide !== undefined) ? ko.unwrap(data.autoHide) : true;
            var valueType:string = typeof value();
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
            let containerClass = container.attr('class');
            container.removeClass(containerClass);
            container.addClass("ntsControl nts-datepicker-wrapper").data("init", true);
            var inputClass: string = (ISOFormat.length < 10) ? "yearmonth-picker" : "";
            var $input: any = $("<input id='" + container.attr("id") + "' class='ntsDatepicker nts-input reset-element' />").addClass(inputClass);
            $input.addClass(containerClass).attr("id", idString).attr("data-name", container.data("name"));
            container.append($input);
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
            });
            
            DatePickerNormalizer.getInstance($input).setCssRanger(data.cssRanger)
                                .fiscalMonthsMode(data.fiscalMonthsMode)
                                .setDefaultCss(data.defaultClass || "");

            name = nts.uk.resource.getControlName(name);
            var validator = new validation.TimeValidator(name, constraintName, {required: required, 
                outputFormat: nts.uk.util.isNullOrEmpty(valueFormat) ? ISOFormat : valueFormat, valueType: valueType});
            $input.on("change", (e) => {
                var newText = $input.val();
                var result = validator.validate(newText);
                $input.ntsError('clear');
                if (result.isValid) {
                    // Day of Week
                    if (hasDayofWeek) {
                        if (util.isNullOrEmpty(result.parsedValue))
                            $label.text("");
                        else
                            $label.text("(" + time.formatPattern(newText, "", dayofWeekFormat) + ")");
                    }
                    value(result.parsedValue);
                }
                else {
                    $input.ntsError('set', result.errorMessage);
                    value(newText);
                }
            });
            
            $input.on("blur", () => {
                var newText = $input.val();
                var result = validator.validate(newText);
                if (!result.isValid) {
                    $input.ntsError('set', result.errorMessage);
                }
            });


            $input.on('validate', (function(e: Event) {
                var newText = $input.val();
                var result = validator.validate(newText);
                $input.ntsError('clear');
                if (!result.isValid) {
                    $input.ntsError('set', result.errorMessage);
                }
            }));
            
            new nts.uk.util.value.DefaultValue().onReset($input, data.value);
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

            var container = $(element);
            var init = container.data("init");
            var $input: any = container.find(".nts-input");
            var $label: any = container.find(".dayofweek-label");
            
            // Value Binding
            var dateFormatValue = (value() !== "") ? time.formatPattern(value(), valueFormat, ISOFormat) : "";
            if (dateFormatValue !== "" && dateFormatValue !== "Invalid date") {
                // Check equals to avoid multi datepicker with same value
                $input.datepicker('setDate', dateFormatValue);
                $label.text("(" + time.formatPattern(value(), valueFormat, dayofWeekFormat) + ")");
            }
            else {
                $input.val("");
                $label.text("");
            }
            container.data("init", false);
            // Properties Binding
            $input.datepicker('setStartDate', startDate);
            $input.datepicker('setEndDate', endDate);
            if (enable !== undefined)
               $input.prop("disabled", !enable);
            else
                $input.prop("disabled", disabled);
            if($input.prop("disabled") === true){
                new nts.uk.util.value.DefaultValue().applyReset($input, value);
            }
            if (data.button)
                container.find('.datepicker-btn').prop("disabled", disabled);
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
        YM_FORMAT: string = "YYYY/MM";
        YMD_FORMAT: string = "YYYY/MM/DD";
        DATE_SPLITTER: string = "/";
        
        // Use this method to get an instance.
        static getInstance($input: JQuery): DatePickerNormalizer {
            var instance = new DatePickerNormalizer();
            instance.$input = $input;
            return instance.onShow().onKeyup().onPick();
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
            if (this.options.format === this.YM_FORMAT) this.colorLevel = this.MONTHS;
            else if (this.options.format === this.YMD_FORMAT) this.colorLevel = this.DAYS;
            // Only set to colorLevel in initialization.
            if (this.selectedView === undefined) this.selectedView = this.colorLevel;
        }
        
        color(): void {
            if (this.cssRanger === undefined) return;
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
            
            if (this.viewMonth < this.fiscalMonth) {
                var self = this;
                var $currentYear = this.getCurrentYear();
                if ($currentYear.length > 0) $currentYear.text(this.viewYear + this.yearText());
            }   
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
            var initValue = this.$input.datepicker("getDate", true);
            var viewTime = this.$input.data(this.NAMESPACE).viewDate;
            this.viewYear = viewTime.getFullYear();
            this.viewMonth = viewTime.getMonth() + 1;
            this.defaultMonths = this.options.months;
            var parsedTime;
            if (this.options.format === this.YMD_FORMAT) parsedTime = this.parseDate(initValue);
            else if (this.options.format === this.YM_FORMAT) parsedTime = this.parseDate(initValue); 
            if (parsedTime !== undefined) {
                this.year = parsedTime.year;
                this.month = parsedTime.month;
                this.date = parsedTime.date;
            } else return;
            
            var colorLevel = this.colorLevel;
            var layer = colorLevel === this.MONTHS ? 1 : 2;
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
            var exp = new RegExp(/\d+\/\d+(\/\d+)?/);
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
                    _self._beforeShow.call(_self);
                    _self.onClick.call(_self);
                }, 0);
            });
            return self;
        }
        
        onKeyup(): DatePickerNormalizer {
            this.$input.off(this.EVENT_KEYUP, this._beforeShow);
            this.$input.on(this.EVENT_KEYUP, $.proxy(this._beforeShow, this));
            return this;
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
    }
}