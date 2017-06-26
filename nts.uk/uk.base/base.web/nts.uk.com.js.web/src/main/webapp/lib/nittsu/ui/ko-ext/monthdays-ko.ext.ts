/// <reference path="../../reference.ts"/>

module nts.uk.ui.koExtentions {
    
    /**
     * Dialog binding handler
     */
    class NtsMonthDaysBindingHandler implements KnockoutBindingHandler {

        /**
         * Init. 
         */
        init(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            let data = valueAccessor();
            let $container = $(element);
            var self = this;
            
            let value = ko.unwrap(data.value);
            let dataName = ko.unwrap(data.name);
            let enable = data.enable === undefined ? true : ko.unwrap(data.enable);
            
            $container.addClass("ntsControl ntsMonthDays_Container");
            $container.append("<div class='ntsMonthDays'/>");
            let $control = $container.find(".ntsMonthDays");
            $control.append("<div class='ntsMonthPicker ntsComboBox ntsMonthDays_Component'/><div class='ntsMonthLabel ntsLabel ntsMonthDays_Component'/><div class='ntsDayPicker ntsComboBox ntsMonthDays_Component'/><div class='ntsDayLabel ntsLabel ntsMonthDays_Component'/>");
            
            let $monthPicker = $control.find(".ntsMonthPicker");
            let $dayPicker = $control.find(".ntsDayPicker");
            let $monthLabel = $control.find(".ntsMonthLabel");
            let $dayLabel = $control.find(".ntsDayLabel");
            $monthLabel.append("<label>月</label>");
            $dayLabel.append("<label>日</label>");
            
            $monthPicker.igCombo({
                    dataSource: NtsMonthDaysBindingHandler.getMonths(),
                    textKey: "text",
                    valueKey: "value",
                    width: "60px",
                    height: "30px",
                    mode : "dropdown",
                    selectionChanged: function (evt, ui) {
                        let currentMonth = ui.items[0].data.value;
                        let currentDay = $dayPicker.igCombo( "selectedItems");
                        let days = NtsMonthDaysBindingHandler.getDaysInMonth(currentMonth);
                        
                        let value = currentDay[0].data.value > days.length ? days.length: currentDay[0].data.value;
                        $dayPicker.igCombo("option", "dataSource", days);
                        data.value(currentMonth*100 + value);
                    }
              });
            
            $dayPicker.igCombo({
                    dataSource: NtsMonthDaysBindingHandler.getDaysInMonth(1),
                    textKey: "text",
                    valueKey: "value",
                    width: "60px",
                    height: "30px",
                    mode : "dropdown",
                    selectionChanged: function (evt, ui) {
                        let currentDay = ui.items[0].data.value;
                        let currentMonth = $monthPicker.igCombo( "selectedItems")[0].data.value;
                        
                        data.value(currentMonth*100 + currentDay);
                    }
              });
        }

        /**
         * Update
         */
        update(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            let data = valueAccessor();
            let $container = $(element);
            
            let value = ko.unwrap(data.value);
            let enable = data.enable === undefined ? true : ko.unwrap(data.enable);
            
            let $monthPicker = $container.find(".ntsMonthPicker");
            let $dayPicker = $container.find(".ntsDayPicker");
            if(enable !== false){
                $monthPicker.igCombo('option', 'disabled', false);
                $dayPicker.igCombo('option', 'disabled', false);            
            } else {
                $monthPicker.igCombo('option', 'disabled', true);
                $dayPicker.igCombo('option', 'disabled', true); 
            }
            
            if(!nts.uk.util.isNullOrUndefined(value) && nts.uk.ntsNumber.isNumber(value)){
                let month = nts.uk.ntsNumber.trunc(parseInt(value) / 100);
                let day = parseInt(value) % 100;    
                $monthPicker.igCombo("value", month);
                $dayPicker.igCombo("value", day);     
            }   
            
            let currentDay = $dayPicker.igCombo( "selectedItems")[0].data.value;
            let currentMonth = $monthPicker.igCombo( "selectedItems")[0].data.value;
            data.value(currentMonth*100 + currentDay);
            
        }
        
        static getMonths(): Array<any> {
            
            let monthSource = []; 
            while (monthSource.length < 12){
                monthSource.push({text: monthSource.length + 1, value: monthSource.length + 1});        
            }
            
            return monthSource;
        } 
        
        static getDaysInMonth(month: number): Array<any> {
            
            let daysInMonth = moment(month, "MM").daysInMonth();
            if (daysInMonth !== NaN) { 
                if (month === 2){
                    daysInMonth++;        
                }
                let days = [];
                while (days.length < daysInMonth){
                    days.push({text: days.length + 1, value: days.length + 1});        
                }            
                return days;
            }
            
            return [];
        }
    }

    ko.bindingHandlers['ntsMonthDays'] = new NtsMonthDaysBindingHandler();
}