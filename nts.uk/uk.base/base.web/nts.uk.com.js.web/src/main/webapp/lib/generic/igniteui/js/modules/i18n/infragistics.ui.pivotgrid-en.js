/*!@license
* Infragistics.Web.ClientUI Pivot Grid localization resources 16.2.20162.2040
*
* Copyright (c) 2011-2016 Infragistics Inc.
*
* http://www.infragistics.com/
*
*/

/*global define, jQuery */
(function (factory) {
	if (typeof define === "function" && define.amd) {
		define( ["jquery"], factory );
	} else {
		factory(jQuery);
	}
}
(function ($) {
    $.ig = $.ig || {};

    if (!$.ig.PivotGrid) {
        $.ig.PivotGrid = {};

        $.extend($.ig.PivotGrid, {
            locale: {
                filtersHeader: "Drop Filter Fields Here",
                measuresHeader: "Drop Data Items Here",
                rowsHeader: "Drop Row Fields Here",
                columnsHeader: "Drop Column Fields Here",
                disabledFiltersHeader: "Filter Fields",
                disabledMeasuresHeader: "Data Items",
                disabledRowsHeader: "Row Fields",
                disabledColumnsHeader: "Column Fields",
                noSuchAxis: "No such axis"
            }
        });
    }
}));// REMOVE_FROM_COMBINED_FILES