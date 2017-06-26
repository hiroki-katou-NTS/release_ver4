ko.bindingHandlers["ntsTable"] = {
    init: function(element, valueAccessor, allBindings, viewModel, bindingContext) {
        let $body = $(element).find('tbody');
        let value = ko.unwrap(valueAccessor());
        let ws = [];

        $(element).find('thead *[data-width]').each(function(i, el) {
            ws.push($(el).data('width'));
            $(el).css({
                width: $(el).data('width'),
                minWidth: $(el).data('width')
            });
        });

        $(value.rows).each(function(i, r) {
            let $row = $('<tr>').append($('<th>', { text: i + 1, css: { width: ws[0], minWidth: ws[0] } })).append($('<td>', { css: { width: ws[1], minWidth: ws[1] } })).append($('<td>', { css: { width: ws[2], minWidth: ws[2] } })).append($('<td>', { css: { width: ws[3], minWidth: ws[3] } }));

            $(element).find('tbody').append($row);
        });
    },
    update: function(element, valueAccessor, allBindings, viewModel, bindingContext) {

    }
};

__viewContext.ready(function() {
    let screenModel = new nts.uk.pr.view.qpp020.b.viewmodel.viewModel();
    this.bind(screenModel);
});