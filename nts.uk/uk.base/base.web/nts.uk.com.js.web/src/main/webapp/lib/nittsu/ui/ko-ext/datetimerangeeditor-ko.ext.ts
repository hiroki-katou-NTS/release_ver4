/// <reference path="../../reference.ts"/>

module nts.uk.ui.koExtentions {

    /**
     * Dialog binding handler
     */
    class NtsDateTimePairRangeEditorBindingHandler implements KnockoutBindingHandler {

        /**
         * Init. 
         */
        init(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext) {
            let data = valueAccessor(),
                $element = $(element);
            let editable = nts.uk.util.isNullOrUndefined(data.editable) ? false : ko.unwrap(data.editable);
                        
            let construct: EditorConstructSite = new EditorConstructSite($element);
            
            construct.build(data, allBindingsAccessor, viewModel, bindingContext);
            
            $element.data("construct", construct);
            
            return { 'controlsDescendantBindings': true };
        }

        /**
         * Update
         */
        update(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            let data = valueAccessor(),
                $element = $(element),
                construct: EditorConstructSite = $element.data("construct");
            
            ko.bindingHandlers["ntsDateTimePairEditor"].update(construct.$start[0], function() {
                return construct.createStartData(data);
            }, allBindingsAccessor, viewModel, bindingContext);
            ko.bindingHandlers["ntsDateTimePairEditor"].update(construct.$end[0], function() {
                return construct.createEndData(data);
            }, allBindingsAccessor, viewModel, bindingContext);
    
        }
    }
    
    class EditorConstructSite {
        $root: JQuery;
        $start: JQuery;
        $end: JQuery;
        startValue: KnockoutObservable<string>;
        endValue: KnockoutObservable<string>;
        startValueBind: KnockoutComputed<string>;
        endValueBind: KnockoutComputed<string>;
        format: string = "YYYY/MM/DD H:mm:ss";
        rangeUnit: string;
        maxRange: number;
        name: string;
        startName: string;
        endName: string;
        
        constructor($root: JQuery) {
            this.$root = $root;
        }
        
        validate(start, end): boolean {
            let self = this;
            self.$root.find(".control-container").ntsError('clearKibanError');
            let mStart = moment(start, self.format);
            let mEnd = moment(end, self.format);
            if(!mEnd.isValid() || !mStart.isValid()){
                return false;
            }
            if(mEnd.isBefore(mStart)){
                self.$root.find(".datetimepairrange-container")
                        .ntsError('set', nts.uk.resource.getMessage('FND_E_SPAN_REVERSED', [self.name]) , 'FND_E_SPAN_REVERSED', false);
                return false;
            }
            if(self.maxRange > 0){
                let maxEnd = mStart.add(self.maxRange, self.rangeUnit);
                if(maxEnd.isBefore(mEnd)){
                    self.$root.find(".datetimepairrange-container")
                            .ntsError('set', "Max range is " + self.maxRange + " " + self.rangeUnit, 'Not defined code', false);
                    return false;
                }
            }
            return true;
        }
        
        initVal(allBindData){
            let self = this;
            self.rangeUnit = _.isNil(allBindData.rangeUnit) ? "years" : ko.unwrap(allBindData.rangeUnit);
            self.maxRange = _.isNil(allBindData.maxRange) ? 0 : ko.unwrap(allBindData.maxRange);
            self.name = _.isNil(allBindData.name) ? "Input" : ko.unwrap(allBindData.name);
            self.startName = _.isNil(allBindData.startName) ? "Start Date" : ko.unwrap(allBindData.startName);
            self.endName = _.isNil(allBindData.endName) ? "End Date" : ko.unwrap(allBindData.endName);
            self.startValueBind = ko.observable();
            self.endValueBind = ko.observable();
            self.startValue = ko.computed({
                read: function() {
                    let value = allBindData.value().start();
                    
                    self.startValueBind(value);
                    
                    return value;
                }, write: function(val) {
                    let endVal = self.endValueBind();
                    
                    allBindData.value().start(val);
                    
                    self.validate(val, endVal);
//                        allBindData.value.valueHasMutated();  
                },
                owner: this  
            });
            self.endValue = ko.computed({
                read: function() {
                    let value = allBindData.value().end();
                    
                    self.endValueBind(value);
                    
                    return value;
                }, write: function(val) {
                    let startVal = self.startValueBind();
                    
                    self.validate(startVal, val);
                    
                    allBindData.value().end(val);
//                        allBindData.value.valueHasMutated();        
                    
                },
                owner: this  
            });  
            self.startValueBind.subscribe((v) => {
                self.startValue(v);    
            });
            self.endValueBind.subscribe((v) => {
                self.endValue(v);    
            });     
        }
        
        build(allBindData, allBindingsAccessor, viewModel, bindingContext){
            let self = this;
            
            self.initVal(allBindData);
            
            let $container = $("<div>", { "class": "datetimerange-editor datetimepairrange-container ntsControl control-container" }),
                this.$start = $("<div>", { "class": "start-datetime-editor datetimepairrange-component ntsControl" }),
                $seperator = $("<div>", { "class": "seperator datetimepairrange-component" }),
                this.$end = $("<div>", { "class": "end-datetime-editor datetimepairrange-component ntsControl" });  
            $container.append(this.$start);
            $container.append($seperator);
            $container.append(this.$end);
            self.$root.addClass("ntsControl");
            self.$root.append($container);
            $seperator.append($("<span>", { "class": "seperator-span", text: "~" }))
            
            ko.bindingHandlers["ntsDateTimePairEditor"].init(this.$start[0], function() {
                return self.createStartData(allBindData);
            }, allBindingsAccessor, viewModel, bindingContext);
            ko.bindingHandlers["ntsDateTimePairEditor"].init(this.$end[0], function() {
                return self.createEndData(allBindData);
            }, allBindingsAccessor, viewModel, bindingContext);  
        }
        
        createStartData(allBindData: any): any {
            let self = this;
            return { required: allBindData.required, 
                     name: allBindData.startName, 
                     value: self.startValueBind, 
                     enable: allBindData.enable,  
                     disabled: allBindData.disabled, 
                     startDate: allBindData.startDate, 
                     endDate: allBindData.endDate };    
        }
        
        createEndData(allBindData: any): any {
            let self = this;
            return { required: allBindData.required, 
                     name: allBindData.endName, 
                     value: self.endValueBind, 
                     enable: allBindData.enable, 
                     disabled: allBindData.disabled, 
                     startDate: allBindData.startDate, 
                     endDate: allBindData.endDate };     
        }
    }


    ko.bindingHandlers['ntsDateTimePairRangeEditor'] = new NtsDateTimePairRangeEditorBindingHandler();
}
