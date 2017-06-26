/// <reference path="../../reference.ts"/>

module nts.uk.ui.koExtentions {

    /**
     * LinkButton
     */
    class NtsLinkButtonBindingHandler implements KnockoutBindingHandler {

        /**
         * Init.
         */
        init(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {

            var data = valueAccessor();
            var jump = ko.unwrap(data.jump);
            var action = data.action;

            var linkText = $(element).text();
            var $linkButton = $(element).wrap('<div class="ntsControl"/>')
                .addClass('link-button')
                .click(function() {
                    event.preventDefault();
                    if (!nts.uk.util.isNullOrUndefined(action))
                        action.call(viewModel);
                    else if (!nts.uk.util.isNullOrUndefined(jump))
                        nts.uk.request.jump(jump);
                });
        }

        /**
         * Update
         */
        update(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
        }
    }
    
    ko.bindingHandlers['ntsLinkButton'] = new NtsLinkButtonBindingHandler();
}