/// <reference path="../../reference.ts"/>

module nts.uk.ui.koExtentions {

    /**
     * FormLabel
     */
    class NtsFormLabelBindingHandler implements KnockoutBindingHandler {

        /**
         * Init.
         */
        init(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {

            var data = valueAccessor();
            var primitiveValue = ko.unwrap(data.constraint);
            var isRequired = ko.unwrap(data.required) === true;
            var isInline = ko.unwrap(data.inline) === true;
            var isEnable = ko.unwrap(data.enable) !== false;
            var $formLabel = $(element).addClass('form-label');

            $('<label/>').html($formLabel.html()).appendTo($formLabel.empty());
            if (!isEnable) {
                $formLabel.addClass('disabled');
            } else {
                $formLabel.removeClass('disabled');
            }
            if (isRequired) {
                $formLabel.addClass('required');
            }

            if (primitiveValue !== undefined) {
                $formLabel.addClass(isInline ? 'inline' : 'broken');

                var constraintText = NtsFormLabelBindingHandler.buildConstraintText(primitiveValue);
                $('<i/>').text(constraintText).appendTo($formLabel);
            }
        }

        /**
         * Update
         */
        update(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
        }

        static buildConstraintText(primitiveValues: any) {
            if (!Array.isArray(primitiveValues))
                primitiveValues = [primitiveValues];
            let constraintText: string = "";
            _.forEach(primitiveValues, function(primitiveValue) {
                let constraint = __viewContext.primitiveValueConstraints[primitiveValue];
                switch (constraint.valueType) {
                    case 'String':
                        constraintText += (constraintText.length > 0) ? "/" : "";
                        constraintText += uk.text.getCharType(primitiveValue).buildConstraintText(constraint.maxLength);
                        break;
                    case 'Decimal':
                        constraintText += (constraintText.length > 0) ? "/" : "";
                        constraintText += constraint.min + "～" + constraint.max; 
                        break;
                    case 'Integer':
                        constraintText += (constraintText.length > 0) ? "/" : "";
                        constraintText += constraint.min + "～" + constraint.max; 
                        break;
                    default:
                        constraintText += 'ERROR';
                        break;
                }
            });
            return constraintText;
        }
    }
    
    ko.bindingHandlers['ntsFormLabel'] = new NtsFormLabelBindingHandler();
}