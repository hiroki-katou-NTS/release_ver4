/// <reference path="../../reference.ts"/>

module nts.uk.ui.koExtentions {
    import validation = nts.uk.ui.validation;

    /**
     * BaseEditor Processor
     */
    class EditorProcessor {
        editorOption: any;

        init($input: JQuery, data: any) {
            var self = this;
            var value: (newText: string) => {} = data.value;
            var constraintName = (data.constraint !== undefined) ? ko.unwrap(data.constraint) : "";
            var constraint = validation.getConstraint(constraintName);
            var immediate: boolean = ko.unwrap(data.immediate !== undefined ? data.immediate : 'false');
            var readonly: boolean = (data.readonly !== undefined) ? ko.unwrap(data.readonly) : false;
            var valueUpdate: string = (immediate === true) ? 'input' : 'change';
            var option: any = (data.option !== undefined) ? ko.mapping.toJS(data.option) : {};
            this.editorOption = $.extend(this.getDefaultOption(), option);
            var characterWidth: number = 9;
            if (constraint && constraint.maxLength && !$input.is("textarea")) {
                var autoWidth = constraint.maxLength * characterWidth;
                $input.width(autoWidth);
            }
            $input.addClass('nts-editor nts-input');
            $input.wrap("<span class= 'nts-editor-wrapped ntsControl'/>");

            $input.on(valueUpdate, (e) => {
                var newText = $input.val();
                let validator = this.getValidator(data);
                var result = validator.validate(newText);
                if (result.isValid) {
                    $input.ntsError('clear');
                    value(result.parsedValue);
                } else {
                    let error = $input.ntsError('getError');
                    if (nts.uk.util.isNullOrUndefined(error) || error.messageText !== result.errorMessage) {
                        $input.ntsError('clear');
                        $input.ntsError('set', result.errorMessage);
                    }
                    value(newText);
                }
            });

            // Format on blur
            $input.blur(() => {
                if (!$input.attr('readonly')) {
                    var formatter = self.getFormatter(data);
                    var newText = $input.val();
                    let validator = self.getValidator(data);
                    var result = validator.validate(newText);
                    if (result.isValid) {
                        $input.ntsError('clear');
                        $input.val(formatter.format(result.parsedValue));
                    }
                    else {
                        let error = $input.ntsError('getError');
                        if (nts.uk.util.isNullOrUndefined(error) || error.messageText !== result.errorMessage) {
                            $input.ntsError('clear');
                            $input.ntsError('set', result.errorMessage);
                        }
                        value(newText);
                    }
                }
            });

            $input.on('validate', (function(e: Event) {
                var newText = $input.val();
                let validator = self.getValidator(data);
                var result = validator.validate(newText);
                $input.ntsError('clear');
                if (!result.isValid) {
                    $input.ntsError('set', result.errorMessage);
                }
            }));
               
            new nts.uk.util.value.DefaultValue().onReset($input, data.value);
        }

        update($input: JQuery, data: any) {
            var value: (val: any) => string = data.value;
            var required: boolean = (data.required !== undefined) ? ko.unwrap(data.required) : false;
            var enable: boolean = (data.enable !== undefined) ? ko.unwrap(data.enable) : true;
            var readonly: boolean = (data.readonly !== undefined) ? ko.unwrap(data.readonly) : false;
            var option: any = (data.option !== undefined) ? ko.mapping.toJS(data.option) : {};
            this.editorOption = $.extend(this.getDefaultOption(), option);
            var placeholder: string = this.editorOption.placeholder;
            var textalign: string = this.editorOption.textalign;
            var width: string = this.editorOption.width;
            // Properties
            if (enable !== false) {
               $input.removeAttr('disabled');
            } else {
               $input.attr('disabled', 'disabled');
               new nts.uk.util.value.DefaultValue().applyReset($input, value);
            }
            (readonly === false) ? $input.removeAttr('readonly') : $input.attr('readonly', 'readonly');
            $input.attr('placeholder', placeholder);
            $input.css('text-align', textalign);
            if (width.trim() != "")
                $input.width(width);
            // Format value
            var formatted = $input.ntsError('hasError') ? value() : this.getFormatter(data).format(value());
            $input.val(formatted);
        }

        getDefaultOption(): any {
            return {};
        }

        getFormatter(data: any): format.IFormatter {
            return new format.NoFormatter();
        }

        getValidator(data: any): validation.IValidator {
            return new validation.NoValidator();
        }
    }

    /**
     * TextEditor Processor
     */
    class TextEditorProcessor extends EditorProcessor {

        init($input: JQuery, data: any) {
            var value: KnockoutObservable<string> = data.value;
            var constraintName = (data.constraint !== undefined) ? ko.unwrap(data.constraint) : "";
            var constraint = validation.getConstraint(constraintName);
            var readonly: boolean = (data.readonly !== undefined) ? ko.unwrap(data.readonly) : false;
            var characterWidth: number = 9;
            if (constraint && constraint.maxLength && !$input.is("textarea")) {
                var autoWidth = constraint.maxLength * characterWidth;
                $input.width(autoWidth);
            }
            $input.addClass('nts-editor nts-input');
            $input.wrap("<span class= 'nts-editor-wrapped ntsControl'/>");

            let validator = this.getValidator(data);
            $input.on("keyup", (e) => {
                var code = e.keyCode || e.which;
                if (!readonly && code.toString() !== '9') {
                    var newText = $input.val();
                    var result = validator.validate(newText);
                    $input.ntsError('clear');
                    if (!result.isValid) {
                        $input.ntsError('set', result.errorMessage);
                    }
                }
            });

            $input.on("change", (e) => {
                if (!$input.attr('readonly')) {
                    var newText = $input.val();
                    var result = validator.validate(newText, { isCheckExpression: true });
                    $input.ntsError('clear');
                    if (result.isValid) {
                        if (value() === result.parsedValue) {
                            $input.val(result.parsedValue);
                        } else {
                            value(result.parsedValue);
                        }
                    } else {
                        $input.ntsError('set', result.errorMessage);
                        value(newText);
                    }
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

        update($input: JQuery, data: any) {
            super.update($input, data);
            var textmode: string = this.editorOption.textmode;
            $input.attr('type', textmode);
        }

        getDefaultOption(): any {
            return new nts.uk.ui.option.TextEditorOption();
        }

        getFormatter(data: any): format.IFormatter {
            var constraintName = (data.constraint !== undefined) ? ko.unwrap(data.constraint) : "";
            var constraint = validation.getConstraint(constraintName);
            return new text.StringFormatter({ constraintName: constraintName, constraint: constraint, editorOption: this.editorOption });
        }

        getValidator(data: any): validation.IValidator {
            var name: string = data.name !== undefined ? ko.unwrap(data.name) : "";
            name = nts.uk.resource.getControlName(name);
            var required: boolean = (data.required !== undefined) ? ko.unwrap(data.required) : false;
            var constraintName = (data.constraint !== undefined) ? ko.unwrap(data.constraint) : "";
            return new validation.StringValidator(name, constraintName, { required: required });
        }
    }

    /**
     * MultilineEditor Processor
     */
    class MultilineEditorProcessor extends EditorProcessor {

        update($input: JQuery, data: any) {
            super.update($input, data);
            var resizeable: string = this.editorOption.resizeable;
            $input.css('resize', (resizeable) ? "both" : "none");
        }

        getDefaultOption(): any {
            return new option.MultilineEditorOption();
        }

        getFormatter(data: any): format.IFormatter {
            var constraintName = (data.constraint !== undefined) ? ko.unwrap(data.constraint) : "";
            var constraint = validation.getConstraint(constraintName);
            return new text.StringFormatter({ constraintName: constraintName, constraint: constraint, editorOption: this.editorOption });
        }

        getValidator(data: any): validation.IValidator {
            var name: string = data.name !== undefined ? ko.unwrap(data.name) : "";
            name = nts.uk.resource.getControlName(name);
            var required: boolean = (data.required !== undefined) ? ko.unwrap(data.required) : false;
            var constraintName = (data.constraint !== undefined) ? ko.unwrap(data.constraint) : "";
            return new validation.StringValidator(name, constraintName, { required: required });
        }
    }

    /**
     * NumberEditor Processor
     */
    class NumberEditorProcessor extends EditorProcessor {

        init($input: JQuery, data: any) {
            super.init($input, data);
            $input.focus(() => {
                var selectionType = document.getSelection().type;
                // Remove separator (comma)
                $input.val(data.value());
                // If focusing is caused by Tab key, select text
                // this code is needed because removing separator deselects.
                if (selectionType === 'Range') {
                    $input.select();
                }
            });
        }

        update($input: JQuery, data: any) {
            super.update($input, data);
            var $parent = $input.parent();
            var width: string = this.editorOption.width;
            var parentTag = $parent.parent().prop("tagName").toLowerCase();
            if (parentTag === "td" || parentTag === "th" || parentTag === "a" || width === "100%") {
                $parent.css({ 'width': '100%' });
            }
            $input.css("box-sizing", "border-box");
            if (width.trim() != "")
                $input.width(width);
            if (this.editorOption.currencyformat !== undefined && this.editorOption.currencyformat !== null) {
                $parent.addClass("symbol").addClass(this.editorOption.currencyposition === 'left' ? 'symbol-left' : 'symbol-right');
                var format = this.editorOption.currencyformat === "JPY" ? "\u00A5" : '$';
                $parent.attr("data-content", format);
            } else if (this.editorOption.symbolChar !== undefined && this.editorOption.symbolChar !== "" && this.editorOption.symbolPosition !== undefined) {
                $parent.addClass("symbol").addClass(this.editorOption.symbolPosition === 'right' ? 'symbol-right' : 'symbol-left');
                $parent.attr("data-content", this.editorOption.symbolChar);
            }
            if(!nts.uk.util.isNullOrEmpty(this.editorOption.defaultValue) 
                && nts.uk.util.isNullOrEmpty(data.value())){
                data.value(this.editorOption.defaultValue);        
            }
        }

        getDefaultOption(): any {
            return new nts.uk.ui.option.NumberEditorOption();
        }

        getFormatter(data: any): format.IFormatter {
            return new text.NumberFormatter({ option: this.editorOption });
        }

        getValidator(data: any): validation.IValidator {
            var name = data.name !== undefined ? ko.unwrap(data.name) : "";
            name = nts.uk.resource.getControlName(name);
            var constraintName = (data.constraint !== undefined) ? ko.unwrap(data.constraint) : "";
            var required = (data.required !== undefined) ? ko.unwrap(data.required) : false;
            this.editorOption['required'] = required;   
            
            return new validation.NumberValidator(name, constraintName, this.editorOption);
        }
    }

    /**
     * TimeEditor Processor
     */
    class TimeEditorProcessor extends EditorProcessor {

        update($input: JQuery, data: any) {
            super.update($input, data);
            var option: any = (data.option !== undefined) ? ko.mapping.toJS(data.option) : this.getDefaultOption();
            var width: string = option.width;
            var parent = $input.parent();
            var parentTag = parent.parent().prop("tagName").toLowerCase();
            if (parentTag === "td" || parentTag === "th" || parentTag === "a" || width === "100%") {
                parent.css({ 'width': '100%' });
            }
        }

        getDefaultOption(): any {
            return new nts.uk.ui.option.TimeEditorOption();
        }

        getFormatter(data: any): format.IFormatter {
            var option = (data.option !== undefined) ? ko.mapping.toJS(data.option) : this.getDefaultOption();
            var inputFormat: string = (data.inputFormat !== undefined) ? ko.unwrap(data.inputFormat) : option.inputFormat;
            return new text.TimeFormatter({ inputFormat: inputFormat });
        }

        getValidator(data: any): validation.IValidator {
            var name = data.name !== undefined ? ko.unwrap(data.name) : "";
            name = nts.uk.resource.getControlName(name);
            var constraintName = (data.constraint !== undefined) ? ko.unwrap(data.constraint) : "";
            var option = (data.option !== undefined) ? ko.mapping.toJS(data.option) : this.getDefaultOption();
            var required: boolean = (data.required !== undefined) ? ko.unwrap(data.required) : false;
            var inputFormat: string = (data.inputFormat !== undefined) ? ko.unwrap(data.inputFormat) : option.inputFormat;
            var mode: string = (data.mode !== undefined) ? ko.unwrap(data.mode) : "";
            return new validation.TimeValidator(name, constraintName, { required: required, outputFormat: inputFormat, mode: mode });
        }
    }

    /**
     * Base Editor
     */
    class NtsEditorBindingHandler implements KnockoutBindingHandler {

        /**
         * Init.
         */
        init(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            new EditorProcessor().init($(element), valueAccessor());
        }

        /**
         * Update
         */
        update(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            new EditorProcessor().update($(element), valueAccessor());
        }
    }

    /**
     * TextEditor
     */
    class NtsTextEditorBindingHandler extends NtsEditorBindingHandler {

        /**
         * Init.
         */
        init(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            new TextEditorProcessor().init($(element), valueAccessor());
        }

        /**
         * Update
         */
        update(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            new TextEditorProcessor().update($(element), valueAccessor());
        }
    }

    /**
     * NumberEditor
     */
    class NtsNumberEditorBindingHandler implements KnockoutBindingHandler {

        /**
         * Init.
         */
        init(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            new NumberEditorProcessor().init($(element), valueAccessor());
        }

        /**
         * Update
         */
        update(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            new NumberEditorProcessor().update($(element), valueAccessor());
        }
    }

    /**
     * TimeEditor
     */
    class NtsTimeEditorBindingHandler implements KnockoutBindingHandler {

        /**
         * Init.
         */
        init(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            new TimeEditorProcessor().init($(element), valueAccessor());
        }

        /**
         * Update
         */
        update(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            // Get data
            new TimeEditorProcessor().update($(element), valueAccessor());
        }
    }

    /**
     * MultilineEditor
     */
    class NtsMultilineEditorBindingHandler extends NtsEditorBindingHandler {

        /**
         * Init.
         */
        init(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            new MultilineEditorProcessor().init($(element), valueAccessor());
        }

        /**
         * Update
         */
        update(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            new MultilineEditorProcessor().update($(element), valueAccessor());
        }
    }

    ko.bindingHandlers['ntsTextEditor'] = new NtsTextEditorBindingHandler();
    ko.bindingHandlers['ntsNumberEditor'] = new NtsNumberEditorBindingHandler();
    ko.bindingHandlers['ntsTimeEditor'] = new NtsTimeEditorBindingHandler();
    ko.bindingHandlers['ntsMultilineEditor'] = new NtsMultilineEditorBindingHandler();
}