﻿/*!@license
* Infragistics.Web.ClientUI common DV widget localization resources 16.1.20161.2145
*
* Copyright (c) 2011-2016 Infragistics Inc.
*
* http://www.infragistics.com/
*
*/

/*global jQuery */
(function ($) {
    $.ig = $.ig || {};

    if (!$.ig.Chart) {
	    $.ig.Chart = {};

	    $.extend($.ig.Chart, {

		    locale: {
			    seriesName: "オプションを設定するときに、シリーズ名のオプションを指定する必要があります。",
			    axisName: "オプションを設定するときに、軸名のオプションを指定する必要があります。",
			    invalidLabelBinding: "ラベルにバインドする値はありません。",
			    invalidSeriesAxisCombination: "シリーズおよび軸型の組み合わせは無効です: ",
			    close: "閉じる",
			    overview: "概要",
			    zoomOut: "ズームアウト",
			    zoomIn: "ズームイン",
			    resetZoom: "ズームのリセット",
			    seriesUnsupportedOption: "現在のシリーズ タイプで次のオプションはサポートされません: ",
			    seriesTypeNotLoaded: "要求されたシリーズ タイプを含む JavaScript ファイルが読み込まれていない、またはシリーズ タイプが無効です: ",
			    axisTypeNotLoaded: "要求された軸タイプを含む JavaScript ファイルが読み込まれていない、または軸タイプが無効です: ",
			    axisUnsupportedOption: "現在の軸タイプで次のオプションはサポートされません: "
		    }
	    });

    }
})(jQuery);

/*!@license
 * Infragistics.Web.ClientUI charting and map common widget 16.1.20161.2145
 *
 * Copyright (c) 2011-2016 Infragistics Inc.
 *
 * http://www.infragistics.com/
 *
 * Depends on:
 *	jquery-1.9.1.js
 *	jquery.ui.core.js
 *	jquery.ui.widget.js
 *	infragistics.dataSource.js
 *	infragistics.templating.js (Optional)
 *	infragistics.util.js
 */
/*global jQuery, Class, window */
(function ($) {

    $.ig.dvCommonWidget = $.ig.dvCommonWidget || Class.extend({
        init: function (widget) {
            this.widget = widget;
        },

        _createWidget: function (options, element, widget) {
            var self = this;
            this.widget = widget;
            this.widget._duringInit = true;

            options = $.extend(false, {}, options);
            this.widget._creationOptions = options;
            this.widget._defaultOptions = this.widget.options;
            this.widget.options = {};

            if ((options.dataSource && $.type(options.dataSource) === "array") ||
            ($.type(options.dataSource) === "object" &&
            (typeof options.dataSource._xmlToArray !== "function" ||
            typeof options.dataSource._encodePkParams !== "function"))) {
                this.widget._initialDataSource = options.dataSource;
                options.dataSource = null;
            }
            this.widget._initialSeriesDataSource = {};
            this.widget._initialAxesDataSource = {};

            if (options.series) {
                options.series = options.series.slice(0);
                $.each(options.series, function (i, val) {
                    if ((val.name && val.dataSource && $.type(val.dataSource) === "array") ||
                    ($.type(val.dataSource) === "object" &&
                    (typeof val.dataSource._xmlToArray !== "function" ||
                    typeof val.dataSource._encodePkParams !== "function"))) {
                        self.widget._initialSeriesDataSource[ val.name ] = val.dataSource;
                        val.dataSource = null;
                    }
                });
            }
            if (options.axes) {
                options.axes = options.axes.slice(0);
                $.each(options.axes, function (i, val) {
                    if ((val.name && val.dataSource && $.type(val.dataSource) === "array") ||
                    ($.type(val.dataSource) === "object" &&
                    (typeof val.dataSource._xmlToArray !== "function" ||
                    typeof val.dataSource._encodePkParams !== "function"))) {
                        self.widget._initialAxesDataSource[ val.name ] = val.dataSource;
                        val.dataSource = null;
                    }
                });
            }

            switch (this.widget.widgetName) {
            case "igDataChart":
                this.widget._axisTemplate = $.extend(false, {}, {});
                this.widget._seriesTemplate = $.extend(false, {}, {});

                //this.widget.options.legend = null;
                //this.widget.options.series[ 0 ].legend = null;
                if (options.series === undefined) {
                    options.series = [];
                }
                if (options.axes === undefined) {
                    options.axes = [];
                }
                break;
            case "igPieChart":
                break;
            case "igMap":
                this.widget._seriesTemplate = $.extend(false, {}, {});
                if (options.series === undefined) {
                    options.series = [];
                }
                this.widget.options.backgroundContent = null;
                break;
            }

            $.Widget.prototype._createWidget.apply(this.widget, [ options, element ]);

            /* VS 03/14/2013 Bug 135879 */

            // note: that block can not be before base call: objects (legend) of multi-stacked series can be broken
            //var a = widget.options.series;
            //if (a && a.length === 1 && !a[ 0 ].name && !a[ 0 ].type) {
            //    a.pop();
            //}
            //a = widget.options.axis;
            //if (a && a.length === 1 && !a[ 0 ].name && !a[ 0 ].type) {
            //    a.pop();
            //}
        },

        option: function (key, value) {
            var val = $.Widget.prototype.option.apply(this.widget, arguments);
            if (val === undefined) {
                if (typeof key === "string") {
                    if (value === undefined) {
                        return this.widget._defaultOptions[ key ];
                    }
                }
            }
            return val;
        },

        _create: function () {
            var widget = this.widget;
            if (!$.ig.util._isCanvasSupported()) {
                $.ig.util._renderUnsupportedBrowser(widget);
            } else {
                this.widget._notInitialized = true;
                if (widget._initialDataSource) {
                    widget._creationOptions.dataSource = this.widget._initialDataSource;
                    widget.options.dataSource = this.widget._initialDataSource;
                }

                if (widget.options.series) {
                    $.each(widget.options.series, function (i, val) {
                        if (val.name && widget._initialSeriesDataSource[ val.name ] !== undefined) {
                            val.dataSource = widget._initialSeriesDataSource[ val.name ];
                        }
                    });
                }
                if (widget.options.axes) {
                    $.each(widget.options.axes, function (i, val) {
                        if (val.name && widget._initialAxesDataSource[ val.name ] !== undefined) {
                            val.dataSource = widget._initialAxesDataSource[ val.name ];
                        }
                    });
                }

                widget._pendingCrossingAxes = [];
                widget._tooltipTemplate = null;
                widget._tooltipTtemplates = null;
                widget._tooltip = {};
                this.widget._tooltipTemplates = {};

                switch (this.widget.widgetName) {
                case "igDataChart":
                    widget._chart = new $.ig.XamDataChart();
                    widget._chart.manageDataSources(true);
                    widget._axes = {};
                    widget._series = {};
                    widget._axisOpt = {};
                    widget._seriesOpt = {};
                    widget._seriesSubOpt = {};
                    widget._seriesSub = {};
                    this._bindDataChartEvents(widget._chart);
                    this._renderChartContainer(this.widget);
                    this._initialDataBind();
                    break;
                case "igPieChart":
                    widget._chart = new $.ig.XamPieChart();

                    // widget._chart.manageDataSources(true);
                    this._bindPieChartEvents(widget._chart);
                    this._renderChartContainer(this.widget);
                    this._initialDataBind();
                    break;
                case "igMap":
                    widget._chart = new $.ig.XamGeographicMap();
                    widget._chart.manageDataSources(true);
                    widget._axes = {};
                    widget._series = {};
                    widget._axisOpt = {};
                    widget._seriesOpt = {};
                    widget._seriesSubOpt = {};
                    widget._seriesSub = {};
                    this._bindMapEvents(widget._chart);
                    this._setBackgroundContent(widget.options.backgroundContent);
                    break;
                }

                if (this.widget._creationOptions.name) {
                    this.widget._chart.name(this.widget._creationOptions.name);
                }

                if (this.widget.dsCount === 0 && this.widget._notInitialized) {
                    this._initializeWidget(this.widget);
                }
            }
        },
        _converterCallback: function () {
			var context = this.settings.callee;
            context.widget.dsCount--;
            if (context.widget.dsCount === 0) {
                context._initializeWidget(context.widget);
            }
        },
        _initDataOptions: function (options, callBack) {
            var widget = this.widget, dataOptions;
            if (options.dataSourceUrl) {
                options.dataSource = options.dataSourceUrl;
            }
            widget._containerSourceID = widget.id();
            dataOptions = {
                id: options.name || widget._containerSourceID,
                rowAdded: this._itemAdded,
                rowDeleted: this._itemRemoved,
                rowUpdated: this._itemUpdated,
                rowInserted: this._itemInserted,
                callback: callBack,
                callee: this,
                responseDataKey: options.responseDataKey,
                primaryKey: options.primaryKey,
                responseTotalRecCountKey: options.responseTotalRecCountKey,
                dataSource: options.dataSource
            };
            if (options.dataSourceType !== null) {
                dataOptions.type = options.dataSourceType;
            }
            return dataOptions;
        },

        _setupDataSource: function (options, callback) {
            var dataOptions = this._initDataOptions(options, callback);
            if (this.widget.dataSources === undefined) {
                this.widget.dataSources = {};
            }
            if (!dataOptions.dataSource ||
				typeof dataOptions.dataSource._xmlToArray !== "function" ||
				typeof dataOptions.dataSource._encodePkParams !== "function") {

                // fix for JSONP
                if ($.type(dataOptions.dataSource) === "string" &&
                dataOptions.dataSource.indexOf("$callback=?") !== -1) {
                    this.widget.dataSources[ dataOptions.id ] =
                    new $.ig.JSONPDataSource(dataOptions);
                } else {
                    this.widget.dataSources[ dataOptions.id ] = new $.ig.DataSource(dataOptions);
                }
            } else {
                this.widget.dataSources[ dataOptions.id ] = dataOptions.dataSource;
                dataOptions.dataSource =
                this.widget.dataSources[ dataOptions.id ].settings.dataSource;

                if (this.widget.dataSources[ dataOptions.id ].settings.responseDataKey !== null) {
                    delete dataOptions.responseDataKey;
                    if (dataOptions.schema) {
                        dataOptions.schema.searchField =
                        this.widget.dataSource.settings.responseDataKey;
                    }
                }

                this.widget.dataSources[ dataOptions.id ].settings =
                this._mergeDataSourceSettings(
                this.widget.dataSources[ dataOptions.id ].settings, dataOptions);
                if (dataOptions.schema) {
                    this.widget.dataSources[ dataOptions.id ]._initSchema();
                }
            }
        },
        _initCallback: function (success, error, dataSource) {
			var context = dataSource.settings.callee;
            context.widget.dsCount--;
            if (context.widget.dsCount === 0 && success) {
                context._initializeWidget(context.widget);
            }
        },
        _initializeWidget: function (widget) {
            this.widget = widget;

            if (this.widget.dataSources[ this.widget._containerSourceID ]) {
                this._setItemsSource(
                this.widget, this.widget._chart, this.widget._containerSourceID);
            }
            this._setWidgetOptions(this.widget._creationOptions, widget._chart);
            this.widget._duringInit = false;

            if (widget._creationOptions.series) {
                this._setCoreWidgetOption(
                widget._chart, "series", this.widget._creationOptions.series);
            }

            widget._notInitialized = false;
        },

        _getNotifyTarget: function (targetName) {
            var target;
            if (this.widget.widgetName === "igPieChart") {
                target = this.widget._chart;
            } else {
                if (targetName === this.widget.id()) {
                    target = this.widget._chart;
                } else {
                    target = this._getSeriesByName(targetName);
                    if (!target) {
                        target = this._getAxisByName(targetName);
                        if (!target) {
                            target = this._getSubSeriesByName(null, targetName);
                        }
                    }
                }
            }
			return target !== undefined ? target : null;
        },

        _notifyItemAdded: function (dataSource, newItem, index) {
            this.widget._chart.notifyInsertItem(dataSource, index, newItem.row);
        },

        _itemAdded: function (item, dataSource) {
            this._notifyItemAdded(dataSource, item, dataSource.dataView().length - 1);
        },

        _itemInserted: function (item, dataSource) {
            this._notifyItemAdded(dataSource, item, item.rowIndex);
        },

        _itemUpdated: function (item, dataSource) {
            this.widget._chart.notifySetItem(dataSource, item.rowIndex, item.oldRow, item.newRow);
        },

        _itemRemoved: function (item, dataSource) {
            this.widget._chart.notifyRemoveItem(dataSource, item.rowIndex, item.row);
        },
        _setWidgetOptions: function (options, chart) {
            var self = this;

            $.each(options, function (key, value) {
                if (!self._setWidgetOption(chart, key, value)) {
                    self._setCoreWidgetOption(chart, key, value);
                } else {

                    //still propagate the value so that it can be read back by the user.
                    self.widget.options[ key ] = value;
                }
            });
        },

        _createBrushFromValue: function (val) {
            var b, stops, colorStop, currOffset;
            if (!val) {
                return null;
            }

            if (typeof val === "string") {
                if ($.ig.CssGradientUtil.prototype.isGradient(val)) {
                    b = $.ig.CssGradientUtil.prototype.brushFromGradientString(val);
                } else {
                    b = new $.ig.Brush();
                    b.fill(val);
                }
            } else if (val.type === "linearGradient") {
                b = new $.ig.LinearGradientBrush();
                if (val.startPoint && val.endPoint) {
                    b._useCustomDirection = true;
                    b._startX = val.startPoint.x;
                    b._startY = val.startPoint.y;
                    b._endX = val.endPoint.x;
                    b._endY = val.endPoint.y;
                }

                if (val.colorStops) {
                    stops = [];
                    for (var i = 0; i < val.colorStops.length; i++) {
                        colorStop = new $.ig.GradientStop();
                        currOffset = 0;
                        if (val.colorStops[ i ].offset) {
                            currOffset = val.colorStops[ i ].offset;
                        }
                        colorStop._offset = currOffset;
                        colorStop.__fill = val.colorStops[ i ].color;
                        stops.push(colorStop);
                    }
                    b._gradientStops = stops;
                }

            }

            return b;
        },

		_getValueFromBrush: function (brush) {
			var ret = {}, currStop, newStop;
			if (brush._isGradient) {
				ret.type = "linearGradient";
				if (brush._useCustomDirection) {
					ret.startPoint = {};
					ret.startPoint.x = brush._startX;
					ret.startPoint.y = brush._startY;
					ret.endPoint = {};
					ret.endPoint.x = brush._endX;
					ret.endPoint.y = brush._endY;
				}
				if (brush._gradientStops) {
					ret.colorStops = [];
					for (var i = 0; i < brush._gradientStops.length; i++) {
						currStop = brush._gradientStops[ i ];
						newStop = {};
						newStop.offset = currStop._offset;
						newStop.color = currStop.__fill;
						ret.colorStops.push(newStop);
					}
				}
				return ret;
			} else {
				return brush.fill();
			}
		},
        _setWidgetOption: function (chart, key, value) {
            var $tempBrushCollection, isRGB, i;

            switch (key) {
            case "crosshairPoint":
                chart.crosshairPoint($.ig.APIFactory.prototype.createPoint(value.x, value.y));
                return true;
            case "windowRect":
                chart.windowRect(
                $.ig.APIFactory.prototype.createRect(
                value.left, value.top, value.width, value.height));
                return true;
            case "horizontalZoomable":
                chart.horizontalZoomable(value);
                return true;
			case "isPagePanningAllowed":
				chart.isPagePanningAllowed(value);
				return true;
            case "useTiledZooming":
                chart.useTiledZooming(value);
                return true;
            case "preferHigherResolutionTiles":
                chart.preferHigherResolutionTiles(value);
                return true;
            case "zoomTileCacheSize":
                chart.zoomTileCacheSize(value);
                return true;
            case "verticalZoomable":
                chart.verticalZoomable(value);
                return true;
			case "zoomable":
				chart.zoomable(value);
				return true;
            case "windowResponse":
                switch (value) {
                case "deferred":
                    chart.windowResponse(0);
                    break;
                case "immediate":
                    chart.windowResponse(1);
                    break;
                }
                return true;
            case "windowRectMinWidth":
                chart.windowRectMinWidth(value);
                return true;
            case "overviewPlusDetailPaneVisibility":
                switch (value) {
                case "visible":
                    chart.overviewPlusDetailPaneVisibility(0);
                    break;
                case "collapsed":
                    chart.overviewPlusDetailPaneVisibility(1);
                    break;
                }
                return true;
            case "crosshairVisibility":
                switch (value) {
                case "visible":
                    chart.crosshairVisibility(0);
                    break;
                case "collapsed":
                    chart.crosshairVisibility(1);
                    break;
                }
                return true;
            case "plotAreaBackground":
                if (value === null) {
                    chart.plotAreaBackground(null);
                } else {
                        chart.plotAreaBackground(this._createBrushFromValue(value));
                }
                return true;
            case "plotAreaMarginLeft":
                chart.plotAreaMarginLeft(value);
                return true;
            case "plotAreaMarginTop":
                chart.plotAreaMarginTop(value);
                return true;
            case "plotAreaMarginBottom":
                chart.plotAreaMarginBottom(value);
                return true;
            case "plotAreaMarginRight":
                chart.plotAreaMarginRight(value);
                return true;
            case "defaultInteraction":
                switch (value) {
                case "none":
                    chart.defaultInteraction(0);
                    break;
                case "dragZoom":
                    chart.defaultInteraction(1);
                    break;
                case "dragPan":
                    chart.defaultInteraction(2);
                    break;
                }
                return true;
            case "dragModifier":
                switch (value) {
                case "none":
                    chart.dragModifier(0);
                    break;
                case "alt":
                    chart.dragModifier(1);
                    break;
                case "control":
                    chart.dragModifier(2);
                    break;
                case "shift":
                    chart.dragModifier(4);
                    break;
                case "windows":
                    chart.dragModifier(8);
                    break;
                case "apple":
                    chart.dragModifier(8);
                    break;
                }
                return true;
            case "panModifier":
                switch (value) {
                case "none":
                    chart.panModifier(0);
                    break;
                case "alt":
                    chart.panModifier(1);
                    break;
                case "control":
                    chart.panModifier(2);
                    break;
                case "shift":
                    chart.panModifier(4);
                    break;
                case "windows":
                    chart.panModifier(8);
                    break;
                case "apple":
                    chart.panModifier(8);
                    break;
                }

                return true;
            case "previewRect":
                chart.previewRect(
                $.ig.APIFactory.prototype.createRect(
                value.left, value.top, value.width, value.height));
                return true;
            case "windowPositionHorizontal":
                chart.windowPositionHorizontal(value);
                return true;
            case "windowPositionVertical":
                chart.windowPositionVertical(value);
                return true;
            case "windowScaleHorizontal":
                chart.windowScaleHorizontal(value);
                return true;
            case "windowScaleVertical":
                chart.windowScaleVertical(value);
                return true;
			case "windowScale":
				chart.windowScale(value);
				return true;
            case "triangleMarkerTemplate":
            case "circleMarkerTemplate":
            case "pyramidMarkerTemplate":
            case "squareMarkerTemplate":
            case "diamondMarkerTemplate":
            case "pentagonMarkerTemplate":
            case "hexagonMarkerTemplate":
            case "tetragramMarkerTemplate":
            case "pentagramMarkerTemplate":
            case "hexagramMarkerTemplate":
                this._setDataTemplate(chart, key, value);
                return true;
            case "topMargin":
                chart.topMargin(value);
                return true;
            case "leftMargin":
                chart.leftMargin(value);
                return true;
            case "rightMargin":
                chart.rightMargin(value);
                return true;
            case "bottomMargin":
                chart.bottomMargin(value);
                return true;
            case "autoMarginWidth":
                chart.autoMarginWidth(value);
                return true;
            case "autoMarginHeight":
                chart.autoMarginHeight(value);
                return true;
            case "worldRect":
                chart.worldRect(
                $.ig.APIFactory.prototype.createRect(
                value.left, value.top, value.width, value.height));
                return true;
                /*case "backgroundContent":
                chart.backgroundContent(value);
                return true; */
            case "xAxis":
                chart.xAxis(this._getAxisByName(value));
                return true;
            case "yAxis":
                chart.yAxis(this._getAxisByName(value));
                return true;
            case "isSquare":
                chart.isSquare(value);
                return true;
            case "gridMode":
                switch (value) {
                case "none":
                    chart.gridMode(0);
                    break;
                case "beforeSeries":
                    chart.gridMode(1);
                    break;
                case "behindSeries":
                    chart.gridMode(2);
                    break;
                }
                return true;
            case "alignsGridLinesToPixels":
                    chart.alignsGridLinesToPixels(value);
                    return true;
            case "brushes":
                isRGB = true;
                if (
                (typeof value[ 0 ] === "string" && value[ 0 ] === "HSV") || value[ 0 ] === "RGB")
                {
                    if (value[ 0 ] === "HSV") { isRGB = false; } value = value.slice(1);
                }
                $tempBrushCollection = new $.ig.BrushCollection();
                for (i = 0; i < value.length; i++) {
                        $tempBrushCollection.add(this._createBrushFromValue(value[ i ]));
                }
                chart.brushes($tempBrushCollection);
                return true;
            case "markerBrushes":
                isRGB = true;
                if (
                (typeof value[ 0 ] === "string" && value[ 0 ] === "HSV") || value[ 0 ] === "RGB")
                {
                    if (value[ 0 ] === "HSV") { isRGB = false; } value = value.slice(1);
                }
                $tempBrushCollection = new $.ig.BrushCollection();
                for (i = 0; i < value.length; i++) {
                        $tempBrushCollection.add(this._createBrushFromValue(value[ i ]));
                }
                chart.markerBrushes($tempBrushCollection);
                return true;
            case "outlines":
                isRGB = true;
                if (
                (typeof value[ 0 ] === "string" && value[ 0 ] === "HSV") || value[ 0 ] === "RGB")
                {
                    if (value[ 0 ] === "HSV") { isRGB = false; } value = value.slice(1);
                }
                $tempBrushCollection = new $.ig.BrushCollection();
                for (i = 0; i < value.length; i++) {
                        $tempBrushCollection.add(this._createBrushFromValue(value[ i ]));
                }
                chart.outlines($tempBrushCollection);
                return true;
            case "markerOutlines":
                isRGB = true;
                if (
                (typeof value[ 0 ] === "string" && value[ 0 ] === "HSV") || value[ 0 ] === "RGB")
                {
                    if (value[ 0 ] === "HSV") { isRGB = false; } value = value.slice(1);
                }
                $tempBrushCollection = new $.ig.BrushCollection();
                for (i = 0; i < value.length; i++) {
                        $tempBrushCollection.add(this._createBrushFromValue(value[ i ]));
                }
                chart.markerOutlines($tempBrushCollection);
                return true;

                //pie chart
            case "valueMemberPath":
                chart.valueMemberPath(value);
                return true;
            case "labelMemberPath":
                chart.labelMemberPath(value);
                return true;
            case "labelsPosition":
                switch (value) {
                case "none":
                    chart.labelsPosition(0);
                    break;
                case "center":
                    chart.labelsPosition(1);
                    break;
                case "insideEnd":
                    chart.labelsPosition(2);
                    break;
                case "outsideEnd":
                    chart.labelsPosition(3);
                    break;
                case "bestFit":
                    chart.labelsPosition(4);
                    break;
                }
                return true;
            case "leaderLineVisibility":
                switch (value) {
                case "visible":
                    chart.leaderLineVisibility(0);
                    break;
                case "collapsed":
                    chart.leaderLineVisibility(1);
                    break;
                }
                return true;
                case "leaderLineType":
                    switch (value) {
                        case "straight":
                            chart.leaderLineType(0);
                            break;
                        case "arc":
                            chart.leaderLineType(1);
                            break;
                        case "spline":
                            chart.leaderLineType(2);
                            break;
                    }
                return true;
                case "leaderLineMargin":
                    chart.leaderLineMargin(value);
                    return true;
                case "leaderLineStyle":
                    chart.leaderLineStyle(value);
                    return true;
            case "othersCategoryThreshold":
                chart.othersCategoryThreshold(value);
                return true;
			case "formatLabel":
				chart.formatLabel(value);
				return true;
			case "othersCategoryStyle":
				chart.othersCategoryStyle(value);
				return true;
            case "othersCategoryType":
                switch (value) {
                case "number":
                    chart.othersCategoryType(0);
                    break;
                case "percent":
                    chart.othersCategoryType(1);
                    break;
                }
                return true;
            case "othersCategoryText":
                chart.othersCategoryText(value);
                return true;
            case "explodedRadius":
                chart.explodedRadius(value);
                return true;
            case "radiusFactor":
                chart.radiusFactor(value);
                return true;
            case "allowSliceSelection":
                chart.allowSliceSelection(value);
                return true;
            case "allowSliceExplosion":
                chart.allowSliceExplosion(value);
                return true;
            case "labelExtent":
                chart.labelExtent(value);
                return true;
            case "startAngle":
                chart.startAngle(value);
                return true;
            case "sweepDirection":
                switch (value) {
                case "counterclockwise":
                    chart.sweepDirection(0);
                    break;
                case "clockwise":
                    chart.sweepDirection(1);
                    break;
                }
                return true;
            case "selectedStyle":
                chart.selectedStyle(value);
                return true;
            case "legendItemTemplate":
                this._setDataTemplate(chart, key, value);
                return true;
            case "legendItemBadgeTemplate":
                this._setDataTemplate(chart, key, value);
                return true;
            case "textStyle":
                chart.textStyle(value);
                return true;
			case "overviewPlusDetailPaneBackgroundImageUri":
				chart.overviewPlusDetailPaneBackgroundImageUri(value);
				return true;
			case "isSurfaceInteractionDisabled":
				chart.isSurfaceInteractionDisabled(value);
				return true;
			case "animateSeriesWhenAxisRangeChanges":
				chart.animateSeriesWhenAxisRangeChanges(value);
				return true;
			case "innerExtent":
				chart.innerExtent(value);
				return true;
                case "title":
                    chart.title(value);
                    return true;
                case "subtitle":
                    chart.subtitle(value);
                    return true;
                case "titleTextStyle":
                    chart.titleTextStyle(value);
                    return true;
				case "titleTopMargin":
					chart.titleTopMargin(value);
					return true;
				case "titleLeftMargin":
					chart.titleLeftMargin(value);
					return true;
				case "titleRightMargin":
					chart.titleRightMargin(value);
					return true;
				case "titleBottomMargin":
					chart.titleBottomMargin(value);
					return true;
				case "subtitleTopMargin":
					chart.subtitleTopMargin(value);
					return true;
				case "subtitleLeftMargin":
					chart.subtitleLeftMargin(value);
					return true;
				case "subtitleRightMargin":
					chart.subtitleRightMargin(value);
					return true;
				case "subtitleBottomMargin":
					chart.subtitleBottomMargin(value);
					return true;
                case "subtitleTextStyle":
                    chart.subtitleTextStyle(value);
                    return true;
                case "titleTextColor":
					if (value === null || value === undefined) {
                        chart.titleTextColor(null);
                    } else {
                        chart.titleTextColor(this._createBrushFromValue(value));
            }
                    return true;
                case "subtitleTextColor":
					if (value === null || value === undefined) {
                        chart.subtitleTextColor(null);
                    } else {
                        chart.subtitleTextColor(this._createBrushFromValue(value));
                    }
                    return true;
				case "titleHorizontalAlignment":
                    switch (value) {
                        case "left":
                            chart.titleHorizontalAlignment(0);
                            break;
                        case "center":
                            chart.titleHorizontalAlignment(1);
                            break;
                        case "right":
                            chart.titleHorizontalAlignment(2);
                            break;
                        default:
                            break;
                    }
                    break;
				case "subtitleHorizontalAlignment":
                    switch (value) {
                        case "left":
                            chart.subtitleHorizontalAlignment(0);
                            break;
                        case "center":
                            chart.subtitleHorizontalAlignment(1);
                            break;
                        case "right":
                            chart.subtitleHorizontalAlignment(2);
                            break;
                        default:
                            break;
            }
                    break;
				case "highlightingTransitionDuration":
                    chart.highlightingTransitionDuration(value);
                    return true;
				case "pixelScalingRatio":
					chart.pixelScalingRatio(value);
					return true;
            }

            return false;
        },
        _createAxisFromType: function (axisType) {
            var axis;
            try {
                axis = this._createAxisFromTypeHelper(axisType);
            } catch (err) {
                throw new Error($.ig.Chart.locale.axisTypeNotLoaded + axisType);
            }

            if (axis.name === undefined) {
                throw new Error($.ig.Chart.locale.axisTypeNotLoaded + axisType);
            }
            return axis;
        },
        _createAxisFromTypeHelper: function (axisType) {
            switch (axisType) {
            case "numericX":
                return new $.ig.NumericXAxis();
            case "numericY":
                return new $.ig.NumericYAxis();
            case "categoryX":
                return new $.ig.CategoryXAxis();
            case "categoryDateTimeX":
                return new $.ig.CategoryDateTimeXAxis();
            case "categoryY":
                return new $.ig.CategoryYAxis();
            case "categoryAngle":
                return new $.ig.CategoryAngleAxis();
            case "numericAngle":
                return new $.ig.NumericAngleAxis();
            case "numericRadius":
                return new $.ig.NumericRadiusAxis();
            default:
                break;
            }
        },
        _createSeriesFromType: function (seriesType) {
            var series;
            try {
                series = this._createSeriesFromTypeHelper(seriesType);
            } catch (err) {
                throw new Error($.ig.Chart.locale.seriesTypeNotLoaded + seriesType);
            }

            if (series.name === undefined) {
                throw new Error($.ig.Chart.locale.seriesTypeNotLoaded + seriesType);
            }

            return series;
        },
        _createSeriesFromTypeHelper: function (seriesType) {
            switch (seriesType) {
            case "area":
                return new $.ig.AreaSeries();
            case "bar":
                return new $.ig.BarSeries();
            case "column":
                return new $.ig.ColumnSeries();
            case "line":
                return new $.ig.LineSeries();
            case "rangeArea":
                return new $.ig.RangeAreaSeries();
            case "rangeColumn":
                return new $.ig.RangeColumnSeries();
            case "splineArea":
                return new $.ig.SplineAreaSeries();
            case "spline":
                return new $.ig.SplineSeries();
            case "stepArea":
                return new $.ig.StepAreaSeries();
            case "stepLine":
                return new $.ig.StepLineSeries();
            case "waterfall":
                return new $.ig.WaterfallSeries();
            case "financial":
                return new $.ig.FinancialPriceSeries();
            case "typicalPriceIndicator":
                return new $.ig.TypicalPriceIndicator();
            case "polarArea":
                return new $.ig.PolarAreaSeries();
            case "polarLine":
                return new $.ig.PolarLineSeries();
			case "polarSpline":
                return new $.ig.PolarSplineSeries();
			case "polarSplineArea":
                return new $.ig.PolarSplineAreaSeries();
            case "polarScatter":
                return new $.ig.PolarScatterSeries();
            case "radialColumn":
                return new $.ig.RadialColumnSeries();
            case "radialLine":
                return new $.ig.RadialLineSeries();
            case "radialPie":
                return new $.ig.RadialPieSeries();
            case "radialArea":
                return new $.ig.RadialAreaSeries();
            case "scatter":
                return new $.ig.ScatterSeries();
            case "highDensityScatter":
                return new $.ig.HighDensityScatterSeries();
            case "scatterLine":
                return new $.ig.ScatterLineSeries();
			case "scatterSpline":
				return new $.ig.ScatterSplineSeries();
            case "bubble":
                return new $.ig.BubbleSeries();
            case "absoluteVolumeOscillatorIndicator":
                return new $.ig.AbsoluteVolumeOscillatorIndicator();
            case "averageTrueRangeIndicator":
                return new $.ig.AverageTrueRangeIndicator();
            case "accumulationDistributionIndicator":
                return new $.ig.AccumulationDistributionIndicator();
            case "averageDirectionalIndexIndicator":
                return new $.ig.AverageDirectionalIndexIndicator();
            case "geographicShape":
                return new $.ig.GeographicShapeSeries();
            case "geographicSymbol":
                return new $.ig.GeographicSymbolSeries();
            case "geographicScatterArea":
				return new $.ig.GeographicScatterAreaSeries();
            case "geographicContourLine":
				return new $.ig.GeographicContourLineSeries();
			case "geographicHighDensityScatter":
				return new $.ig.GeographicHighDensityScatterSeries();
			case "geographicProportionalSymbol":
				return new $.ig.GeographicProportionalSymbolSeries();
			case "geographicTileSeries":
				return new $.ig.GeographicTileSeries();
            case "geographicPolyline":
                return new $.ig.GeographicPolylineSeries();
			case "bollingerBandWidthIndicator":
				return new $.ig.BollingerBandWidthIndicator();
			case "chaikinOscillatorIndicator":
				return new $.ig.ChaikinOscillatorIndicator();
			case "chaikinVolatilityIndicator":
				return new $.ig.ChaikinVolatilityIndicator();
			case "commodityChannelIndexIndicator":
				return new $.ig.CommodityChannelIndexIndicator();
			case "detrendedPriceOscillatorIndicator":
				return new $.ig.DetrendedPriceOscillatorIndicator();
			case "easeOfMovementIndicator":
				return new $.ig.EaseOfMovementIndicator();
			case "fastStochasticOscillatorIndicator":
				return new $.ig.FastStochasticOscillatorIndicator();
			case "forceIndexIndicator":
				return new $.ig.ForceIndexIndicator();
			case "fullStochasticOscillatorIndicator":
				return new $.ig.FullStochasticOscillatorIndicator();
			case "marketFacilitationIndexIndicator":
				return new $.ig.MarketFacilitationIndexIndicator();
			case "massIndexIndicator":
				return new $.ig.MassIndexIndicator();
			case "medianPriceIndicator":
				return new $.ig.MedianPriceIndicator();
			case "moneyFlowIndexIndicator":
				return new $.ig.MoneyFlowIndexIndicator();
			case "movingAverageConvergenceDivergenceIndicator":
				return new $.ig.MovingAverageConvergenceDivergenceIndicator();
			case "negativeVolumeIndexIndicator":
				return new $.ig.NegativeVolumeIndexIndicator();
			case "onBalanceVolumeIndicator":
				return new $.ig.OnBalanceVolumeIndicator();
			case "percentagePriceOscillatorIndicator":
				return new $.ig.PercentagePriceOscillatorIndicator();
			case "percentageVolumeOscillatorIndicator":
				return new $.ig.PercentageVolumeOscillatorIndicator();
			case "positiveVolumeIndexIndicator":
				return new $.ig.PositiveVolumeIndexIndicator();
			case "priceVolumeTrendIndicator":
				return new $.ig.PriceVolumeTrendIndicator();
			case "rateOfChangeAndMomentumIndicator":
				return new $.ig.RateOfChangeAndMomentumIndicator();
			case "relativeStrengthIndexIndicator":
				return new $.ig.RelativeStrengthIndexIndicator();
			case "slowStochasticOscillatorIndicator":
				return new $.ig.SlowStochasticOscillatorIndicator();
			case "standardDeviationIndicator":
				return new $.ig.StandardDeviationIndicator();
			case "stochRSIIndicator":
				return new $.ig.StochRSIIndicator();
			case "trixIndicator":
				return new $.ig.TRIXIndicator();
			case "ultimateOscillatorIndicator":
				return new $.ig.UltimateOscillatorIndicator();
			case "weightedCloseIndicator":
				return new $.ig.WeightedCloseIndicator();
			case "williamsPercentRIndicator":
				return new $.ig.WilliamsPercentRIndicator();
			case "bollingerBandsOverlay":
				return new $.ig.BollingerBandsOverlay();
			case "priceChannelOverlay":
				return new $.ig.PriceChannelOverlay();
			case "customIndicator":
				return new $.ig.CustomIndicator();
			case "point":
				return new $.ig.PointSeries();
			case "stackedColumn":
				return new $.ig.StackedColumnSeries();
			case "stacked100Column":
				return new $.ig.Stacked100ColumnSeries();
			case "stackedArea":
				return new $.ig.StackedAreaSeries();
			case "stacked100Area":
				return new $.ig.Stacked100AreaSeries();
			case "stackedBar":
				return new $.ig.StackedBarSeries();
			case "stacked100Bar":
				return new $.ig.Stacked100BarSeries();
			case "stackedLine":
				return new $.ig.StackedLineSeries();
			case "stacked100Line":
				return new $.ig.Stacked100LineSeries();
			case "stackedSpline":
				return new $.ig.StackedSplineSeries();
			case "stacked100Spline":
				return new $.ig.Stacked100SplineSeries();
			case "stackedSplineArea":
				return new $.ig.StackedSplineAreaSeries();
			case "stacked100SplineArea":
				return new $.ig.Stacked100SplineAreaSeries();
			case "stackedFragment":
				return new $.ig.StackedFragmentSeries();
                case "crosshairLayer":
                    return new $.ig.CrosshairLayer();
                case "categoryHighlightLayer":
                    return new $.ig.CategoryHighlightLayer();
                case "categoryItemHighlightLayer":
                    return new $.ig.CategoryItemHighlightLayer();
                case "itemToolTipLayer":
                    return new $.ig.ItemToolTipLayer();
                case "categoryToolTipLayer":
                    return new $.ig.CategoryToolTipLayer();

            default:
                break;
            }
        },

		_clearAxes: function (series) {
			if (series.xAxis) {
				series.xAxis(null);
			}
			if (series.yAxis) {
				series.yAxis(null);
			}
			if (series.angleAxis) {
				series.angleAxis(null);
			}
			if (series.valueAxis) {
				series.valueAxis(null);
			}
			if (series.radiusAxis) {
				series.radiusAxis(null);
			}
		},

		// replace or append new object obj to array a
		// if item has same name as obj, then replace, otherwise append
		_setForName: function (a, obj) {
			var i = a.length;
			while (i-- > 0) {
				if (a[ i ] && a[ i ].name === obj.name) {
					a[ i ] = obj;
					return;
				}
			}
			a.push(obj);
		},

        _setCoreWidgetOption: function (chart, key, value) {
            var self = this, widget = this.widget, axis = null, axisOpt = null, axisIsNew = false,
            merged, settings, newLink, oldLink, seriesIsNew, series = null, seriesOpt = null,
            exploded, templ;
            switch (key) {
            case "axes":

                //set axis
                if (this.widget.widgetName === "igMap") {
                    return;
                }
                $.each(value, function (i, val) {
                    if (!val.name) {
                        throw new Error($.ig.Chart.locale.axisName);
                    }
                    if (self.widget._series[ val.name ]) {
                        throw new Error($.ig.Chart.locale.nameInUse);
                    }
                    axis = self._getAxisByName(val.name);
                    if (axis && val.remove) {
                        delete widget._axes[ val.name ];
                        delete widget._axisOpt[ val.name ];
                        var ind =
                            self._getIndexByName(
                            widget.options.axes,
                            val.name);
                        widget.options.axes.splice(ind, 1);
                        widget._chart.axes().remove(axis);
                        if (widget.dataSources[ val.name ]) {
                            delete widget.dataSources[ val.name ].settings;
                            delete widget.dataSources[ val.name ];
                        }
                        if (widget._target && widget._target.name() === val.name) {
                            widget._target = null;
                        }
                        return;
                    }

                    if (axis) {
                        axisOpt = widget._axisOpt[ val.name ];
                    }

                    if (!axis && !val.remove) {
                        axis = self._createAxisFromType(val.type);
                        axis.name(val.name);
                        axisIsNew = true;
                    }

                    if (axis) {
                        if (axisIsNew) {
                            widget._axes[ val.name ] = axis;
                            merged = self._mergeIntoNewWithDataSource(widget._axisTemplate, val);

                            /* VS 03/14/2013 Bug 135879 */
                            /* VS 04/02/2013 Bug 138644 */
                            self._setForName(widget.options.axes, merged);
                            widget._axisOpt[ val.name ] = merged;
                        }
                        if (val.coercionMethods) {
                            axis.coercionMethods(val.coercionMethods);
                        }
                            if (widget.options.expectFunctions !== undefined &&
                                widget.options.expectFunctions) {
                                axis.expectFunctions(widget.options.expectFunctions);
                            }
                            if (val.expectFunctions !== undefined) {
                                axis.expectFunctions(val.expectFunctions);
                            }

                        self._setAxisOptions(val, axis);
                        if (axisIsNew) {
                            if (widget.dataSources[ axis.name() ]) {
                                self._setItemsSource(self.widget, axis, axis.name());

                                //axis.itemsSource(widget.dataSources[axis.name()].dataView());
                            }
                        } else {
                            self._mergeIntoWithDataSource(axisOpt, val);
                        }
                        if (axisIsNew) {
                            widget._chart.axes().add(axis);
                        }
                    }
                });

                if (widget._pendingCrossingAxes && widget._pendingCrossingAxes.length > 0) {
                    $.each(widget._pendingCrossingAxes, function (i, val) {
                        val();
                    });
                    widget._pendingCrossingAxes.length = 0;
                }
                return;
            case "series":
                if (!widget._duringInit) {
                    seriesIsNew = false;

                    //set series
                    $.each(value, function (i, val) {
                        if (!val.name) {
                            throw new Error($.ig.Chart.locale.seriesName);
                        }
                        if (self.widget._axes[ val.name ]) {
                            throw new Error($.ig.Chart.locale.nameInUse);
                        }
                        series = self._getSeriesByName(val.name);
                        if (series && val.remove) {
                            delete widget._series[ val.name ];
                            delete widget._seriesOpt[ val.name ];
							delete widget._seriesSubOpt[ val.name ];
							delete widget._seriesSub[ val.name ];

                            if (widget.dataSources[ val.name ]) {
                                delete widget.dataSources[ val.name ].settings;
                                delete widget.dataSources[ val.name ];
                            }
                            var ind = self._getIndexByName(
                                widget.options.series,
                                val.name);
                            widget.options.series.splice(ind, 1);
                            widget._chart.series().remove(series);
							self._clearAxes(series);
                            return;
                        }
                        if (!series && !val.remove) {
                            series = self._createSeriesFromType(val.type);
							self._bindSeriesEvents(series);
                            series.name(val.name);
                            seriesIsNew = true;
                        }
                        if (series) {
                            seriesOpt = widget._seriesOpt[ val.name ];
                        }
                        if (series) {
                            if (seriesIsNew) {
                                widget._series[ val.name ] = series;
								widget._seriesSub[ val.name ] = {};
								merged =
                                    self._mergeIntoNewWithDataSource(widget._seriesTemplate, val);

                                /* VS 03/14/2013 Bug 135879 */
                                /* VS 04/02/2013 Bug 138644 */
                                self._setForName(widget.options.series, merged);
                                widget._seriesOpt[ val.name ] = merged;
								widget._seriesSubOpt[ val.name ] = {};
                            }
                            if (val.coercionMethods) {
                                series.coercionMethods(val.coercionMethods);
                            }
                                if (widget.options.expectFunctions !== undefined &&
                                    widget.options.expectFunctions) {
                                    series.expectFunctions(widget.options.expectFunctions);
                                }
                                if (val.expectFunctions !== undefined) {
                                    series.expectFunctions(val.expectFunctions);
                                }
                            self._setSeriesOptions(val, series, widget);
                            if (seriesIsNew) {
                                if (widget.dataSources[ series.name() ]) {
                                    self._setItemsSource(self.widget, series, series.name());
                                }
                            } else {
                                self._mergeIntoWithDataSource(seriesOpt, val);
                            }
                            if (seriesIsNew) {
                                widget._chart.series().add(series);
                            }
                        }
                    });
                }
                return;
            case "syncChannel":
                newLink = null;
                if (value && value.length > 0) {
                    newLink = $.ig.SyncLinkManager.prototype.instance().getLink(value);
                }
                oldLink = widget._chart.actualSyncLink();
                if (oldLink) {
                    $.ig.SyncLinkManager.prototype.instance().releaseLink(oldLink);
                }
                if (newLink) {
                    widget._chart.actualSyncLink(newLink);
                } else {
                    widget._chart.actualSyncLink(new $.ig.SyncLink());
                }
                break;
            case "synchronizeVertically":
                settings = widget._chart.syncSettings();
                settings.synchronizeVertically(value);
                break;
            case "synchronizeHorizontally":
                settings = widget._chart.syncSettings();
                settings.synchronizeHorizontally(value);
                break;
            case "legend":
                this._setLegend(widget._chart, value);
                break;
            case "width":
            case "height":
                this._setSize(widget._chart, key, value);
                break;
            case "size":
                if (value.width) {
                    this._setSize(widget._chart, "width", value.width);

                    /* VS 03/14/2013 Bug 135879 */
                    widget.options.width = value.width;
                }
                if (value.height) {
                    this._setSize(widget._chart, "height", value.height);

                    /* VS 03/14/2013 Bug 135879 */
                    widget.options.height = value.height;
                }
                return;

            //pie chart	specific
            case "explodedSlices":
                if (value.length) {
                    exploded = new $.ig.IndexCollection();
                    $.each(value, function (i, val) {
                        exploded.add(val);
                    });
                    widget._chart.explodedSlices(exploded);
                }
                break;
           case "selectedSlices":
                if (value.length) {
                   var selected = new $.ig.IndexCollection();
                    $.each(value, function (i, val) {
                        selected.add(val);
                    });
                    widget._chart.selectedSlices(selected);
                }
                break;
            case "showTooltip":
                if (value === true) {
                    this._addTooltip(widget, widget._chart, widget.css.tooltip);
                }
                if (value === false) {
                    this._removeTooltip(widget, widget._chart);
                }
                break;
            case "tooltipTemplate":
                if ($.ig.tmpl) {
                    if (this._htmlCheckExpr.test(value)) {
                        templ = value;
                    } else {
                        if ($("#" + value).length > 0) {
                            templ = $("#" + value).text();
                        } else if ($(value).length > 0) {
                            templ = $(value).text();
                        } else {
                            templ = value;
                        }
                    }

                    //templ = (typeof $("#" + value).data().tmpl === "undefined") ? $.template(widget.id() + "TooltipTemplate", value) : value;
                    widget._tooltipTemplate = templ;
					if (widget._chart.tooltipTemplate) {
						widget._chart.tooltipTemplate(templ);
                }
                }
                break;

            //			case "dataSource":
            //				this._setItemsSource(this.widget, this.widget._chart, this.widget._containerSourceID);
            //				break;
            //map specific
            case "backgroundContent":
				if (value === null) {
					widget._chart.backgroundContent(null);
				}
                if (!widget._duringInit) {
                    this._setBackgroundContent(value);
                }
                break;
            default:
					return;
            }
            /* VS 03/14/2013 Bug 135879 */
            widget.options[ key ] = value;
        },

        _htmlCheckExpr: /^[^<]*(<[\w\W]+>)[^>]*$/,

        _getAxisByName: function (name) {
            if (this.widget._axes[ name ] === undefined) {
				if (this.widget.widgetName === "igMap") {
					if (name === "xAxis") {
						return this.widget._chart.xAxis();
					}
					if (name === "yAxis") {
						return this.widget._chart.yAxis();
					}
				}
                return null;
            }
            return this.widget._axes[ name ];
        },

        _getSeriesByName: function (name) {
            if (this.widget._series[ name ] === undefined) {
                return null;
            }
            return this.widget._series[ name ];
        },

        _setAxisOptions: function (options, axis) {
            var self = this;
            $.each(options, function (key, value) {
                if (!self._setAxisOption(axis, key, value)) {
                    self._setCoreAxisOption(axis, key, value);
                }
                /* Graham 03/14/2013 Bug 135879 */

                //still propagate the axis option so the user can read it back.
                self.widget._axisOpt[ axis.name() ][ key ] = value;
            });
        },

        _setSeriesOptions: function (options, series) {
            var self = this;
            $.each(options, function (key, value) {
                if (!self._setCoreSeriesOption(series, key, value)) {
                    self._seriesSetOption(series, key, value);
                }
                /* Graham 03/14/2013 Bug 135879 */
                if (key !== "series") {

						//still propagate the series option so the user can read it back.
						self.widget._seriesOpt[ series.name() ][ key ] = value;
                }
            });
        },

         _setSubSeriesOptions: function (options, parentSeries, series) {
            var self = this;
            $.each(options, function (key, value) {
                if (!self._setCoreSeriesOption(series, key, value)) {
                    self._seriesSetOption(series, key, value);
                }
                /* Graham 03/14/2013 Bug 135879 */

                //still propagate the series option so the user can read it back.
                self.widget._seriesSubOpt[ parentSeries.name() ][ series.name() ][ key ] = value;
            });
        },

        _setCoreSeriesOption: function (series, key, value) {
            var templ;
            switch (key) {
            case "legend":
                this._setLegend(series, value);
                return true;
            case "showTooltip":
                if (value === true) {
                    this._addTooltip(this.widget, series, this.widget.css.tooltip);
                    this._setCoreSeriesOption(series, "tooltipTemplate", "default");
                } else {
                    this._removeTooltip(this.widget, series);
                }
                return true;
            case "tooltipTemplate":
                if ($.ig.tmpl) {
                    if (this._htmlCheckExpr.test(value)) {
                        templ = value;
                    } else {
                        if ($("#" + value).length > 0) {
                            templ = $("#" + value).text();
                        } else if ($(value).length > 0) {
                            templ = $(value).text();
                        } else {
                            templ = value;
                        }
                    }

                    //templ = (typeof $("#" + value).data().tmpl === "undefined") ? $.template(series.name() + "TooltipTemplate", value) : value;
                    this.widget._tooltipTemplates[ series.name() ] = templ;
                        if (series.tooltipTemplate) {
                            series.tooltipTemplate(templ);
                }
                        if (series.flattenEventArgs) {
                            series.flattenEventArgs($.proxy(this._getChartEvt, this));
                        }
                    }
                return true;
            case "displayType":
                switch (value) {
                case "candlestick":
                    series.displayType(0);
                    break;
                case "ohlc":
                    series.displayType(1);
                    break;
                case "line":
                    series.displayType(0);
                    break;
                case "area":
                    series.displayType(1);
                    break;
                case "column":
                    series.displayType(2);
                    break;
                }
                return true;
			case "tileImagery":
				if (value === null) {
					series.backgroundContent(null);
				}
                this._setSeriesBackgroundContent(series, value);
                return true;
            }
            return false;
        },
        _initMap: function () {
            if (this.widget._duringInit) {
                this._renderChartContainer(this.widget);
                this._initialDataBind();

				if (this.widget.dsCount === 0 && this.widget._notInitialized) {
                    this._initializeWidget(this.widget);
                }
            }
        },
        _setSeriesBackgroundContent: function (series, bgOptions) {
			var seriesOpt = this.widget._seriesOpt[ series.name() ];
			seriesOpt.tileImagery = bgOptions;

			switch (bgOptions.type) {
			case "cloudMade":
				series.tileImagery(this._setBackgroundOptions(new $.ig.CloudMadeMapImagery(), bgOptions));
				break;
			case "bing":
				this._initializeBingMapsImageryForSeries(series, bgOptions);
				break;
			case "openStreet":
				series.tileImagery(this._setBackgroundOptions(new $.ig.OpenStreetMapImagery(), bgOptions));
				break;
			default:
				return;
			}
		},
        _setBackgroundContent: function (bgOptions) {
            if (!bgOptions) {
                this._initMap();
            } else {
				this.widget.options.backgroundContent = bgOptions;

				switch (bgOptions.type) {
				case "cloudMade":
				    this.widget._chart.backgroundContent(
                    this._setBackgroundOptions(new $.ig.CloudMadeMapImagery(), bgOptions));
					this._initMap();
					break;
				case "bing":
					this._initializeBingMapsImagery(bgOptions);
					break;
				case "openStreet":
				    this.widget._chart.backgroundContent(
                    this._setBackgroundOptions(new $.ig.OpenStreetMapImagery(), bgOptions));
					this._initMap();
					break;
				default:
					return;
				}
            }
        },
        _initializeBingMapsImagery: function (bgOptions) {
            var bingUrl =
            bgOptions.bingUrl || "http://dev.virtualearth.net/REST/v1/Imagery/Metadata/",
            imagerySet = bgOptions.imagerySet || "AerialWithLabels", bingKey = bgOptions.key;
            $.ajax({
                url: bingUrl + imagerySet,
                data: "key=" + bingKey + "&include=ImageryProviders",
                success: $.proxy(this._setBingBackground, this),
                dataType: "jsonp",
                jsonp: "jsonp"
            });
        },
		_initializeBingMapsImageryForSeries: function (series, bgOptions) {
		    var bingUrl = bgOptions.bingUrl || "http://dev.virtualearth.net/REST/v1/Imagery/Metadata/",
            imagerySet =
            bgOptions.imagerySet || "AerialWithLabels", bingKey = bgOptions.key, self = this;
            $.ajax({
                url: bingUrl + imagerySet,
                data: "key=" + bingKey + "&include=ImageryProviders",
                success: function (res) { self._setBingBackgroundForSeries(series, res); },
                dataType: "jsonp",
                jsonp: "jsonp"
            });
        },
		_setBingBackgroundForSeries: function (series, res) {
		    var i, bg = new $.ig.BingMapsMapImagery(), meta = res.resourceSets[ 0 ].resources[ 0 ],
            culture = this.widget.options.backgroundContent.cultureName || "en-US";
            bg.tilePath(meta.imageUrl);
            bg.cultureName(culture);
            if (this.widget.options.backgroundContent.downloadingImage !== undefined) {
                bg.downloadingImage = this._getDownloadingImageHandler(
                this.widget.options.backgroundContent.downloadingImage);
            }
            if (this.widget.options.backgroundContent.opacity !== undefined) {
                bg.opacity(this.widget.options.opacity);
            }
            for (i = 0; i < meta.imageUrlSubdomains.length; i++) {
                bg.subDomains().add(meta.imageUrlSubdomains[ i ]);
            }
            series.tileImagery(bg);
        },
        _setBingBackground: function (res) {
            var i, bg = new $.ig.BingMapsMapImagery(), meta,
            culture = this.widget.options.backgroundContent.cultureName || "en-US";
            if (!res || !res.resourceSets || res.resourceSets.length < 1 ||
            !res.resourceSets[ 0 ].resources || res.resourceSets[ 0 ].resources.length < 1) {
                return;
            }
			meta = res.resourceSets[ 0 ].resources[ 0 ];
            bg.tilePath(meta.imageUrl);
            bg.cultureName(culture);
            if (this.widget.options.backgroundContent.downloadingImage !== undefined) {
                bg.downloadingImage = this._getDownloadingImageHandler(
                this.widget.options.backgroundContent.downloadingImage);
            }
            if (this.widget.options.backgroundContent.opacity !== undefined) {
                bg.opacity(this.widget.options.opacity);
            }
            for (i = 0; i < meta.imageUrlSubdomains.length; i++) {
                bg.subDomains().add(meta.imageUrlSubdomains[ i ]);
            }
            this.widget._chart.backgroundContent(bg);
            this._initMap();
        },
        _setBackgroundOptions: function (background, options) {
            var self = this;
            $.each(options, function (key, val) {
                self._backgroundContextSetOption(background, key, val);
            });
            return background;
        },
        _backgroundContextSetOption: function (background, key, value) {
            switch (key) {
            case "key":
                background.key(value);
                break;
            case "parameter":
                background.parameter(value);
                break;
            case "opacity":
                background.opacity(value);
                break;
            case "downloadingImage":
                background.downloadingImage = this._getDownloadingImageHandler(value);
                break;
            default:
                break;
            }
        },
        _getDownloadingImageHandler: function(outerFunc) {
            return function (sender, args) {
                var uri = args.uri();
                var image = args.image();

                var outerArgs =  { uri: uri, image: image };
                outerFunc(outerArgs);

                args.uri(outerArgs.uri);
            };
        },
        _setDataTemplate: function (series, key, value) {
            var tempTemplate = new $.ig.DataTemplate(), requireThis = false;
            if (!value) {
                if (series[ key ]) {
                    series[ key ](null);
                }
                return;
            }

            if (value.requireThis) {
                requireThis = true;
            }

            if (value.render) {
                if (requireThis) {
                    tempTemplate.render($.proxy(value.render, value));
                } else {
                    tempTemplate.render(value.render);
                }
            } else {
                tempTemplate.render(value);
            }
            if (value.measure) {
                if (requireThis) {
                    tempTemplate.measure($.proxy(value.measure, value));
                } else {
                    tempTemplate.measure(value.measure);
                }
            }
            if (value.passStarting) {
                if (requireThis) {
                    tempTemplate.passStarting($.proxy(value.passStarting, value));
                } else {
                    tempTemplate.passStarting(value.passStarting);
                }
            }
            if (value.passCompleted) {
                if (requireThis) {
                    tempTemplate.passCompleted($.proxy(value.passCompleted, value));
                } else {
                    tempTemplate.passCompleted(value.passCompleted);
                }
            }
            if (series[ key ]) {
                series[ key ](tempTemplate);
            }
        },
        _seriesSetOption: function (series, key, value) {
            try {
                return this._seriesSetOptionHelper(series, key, value);
            } catch (err) {
                if (series[ key ] === undefined) {
                    throw new Error($.ig.Chart.locale.seriesUnsupportedOption + key);
                } else {
                    throw err;
                }
            }
        },
        _seriesSetOptionHelper: function (series, key, value) {
            var $tempColor, $old, axis, axisType, seriesType;

            switch (key) {
            case "legendItemVisibility":
                switch (value) {
                case "visible":
                    series.legendItemVisibility(0);
                    break;
                case "collapsed":
                    series.legendItemVisibility(1);
                    break;
                }
                return true;
            case "legendItemBadgeTemplate":
            case "legendItemTemplate":
            case "discreteLegendItemTemplate":
            case "markerTemplate":
                this._setDataTemplate(series, key, value);
                return true;
            case "transitionDuration":
                series.transitionDuration(value);
                return true;
			case "transitionEasingFunction":
				series.transitionEasingFunction($.ig.util.getEasingFunction(value));
				return true;
			case "transitionInEasingFunction":
					series.transitionInEasingFunction($.ig.util.getEasingFunction(value));
					return true;
            case "resolution":
                series.resolution(value);
                return true;
            case "title":
                series.title(value);
                return true;
            case "brush":
                if (value === null) {
                    series.brush(null);
                } else {
                        series.brush(this._createBrushFromValue(value));
                }
                return true;
            case "outline":
                if (value === null) {
                    series.outline(null);
                } else {
                        series.outline(this._createBrushFromValue(value));
                }
                return true;
            case "thickness":
                series.thickness(value);
                return true;
                case "isDropShadowEnabled":
                    series.isDropShadowEnabled(value);
                    return true;
                case "useSingleShadow":
                    series.useSingleShadow(value);
                    return true;
                case "shadowColor":
                    if (value === null) {
                        series.shadowColor(null);
                    } else {
                        $tempColor = $.ig.APIFactory.prototype.createColor(value);
                        series.shadowColor($tempColor);
                    }
                    return true;
                case "shadowBlur":
                    series.shadowBlur(value);
                    return true;
                case "shadowOffsetX":
                    series.shadowOffsetX(value);
                    return true;
                case "shadowOffsetY":
                    series.shadowOffsetY(value);
                    return true;
            case "markerType":
                switch (value) {
                case "unset":
                    series.markerType(0);
                    break;
                case "none":
                    series.markerType(1);
                    break;
                case "automatic":
                    series.markerType(2);
                    break;
                case "circle":
                    series.markerType(3);
                    break;
                case "triangle":
                    series.markerType(4);
                    break;
                case "pyramid":
                    series.markerType(5);
                    break;
                case "square":
                    series.markerType(6);
                    break;
                case "diamond":
                    series.markerType(7);
                    break;
                case "pentagon":
                    series.markerType(8);
                    break;
                case "hexagon":
                    series.markerType(9);
                    break;
                case "tetragram":
                    series.markerType(10);
                    break;
                case "pentagram":
                    series.markerType(11);
                    break;
                case "hexagram":
                    series.markerType(12);
                    break;
                }
                return true;
            case "shapeMemberPath":
                series.shapeMemberPath(value);
                return true;
            case "shapeStyleSelector":
                series.shapeStyleSelector(value);
                return true;
            case "shapeStyle":
                series.shapeStyle(value);
                return true;
            case "markerBrush":
                if (value === null) {
                    series.markerBrush(null);
                } else {
                        series.markerBrush(this._createBrushFromValue(value));
                }
                return true;
            case "markerOutline":
                if (value === null) {
                    series.markerOutline(null);
                } else {
                        series.markerOutline(this._createBrushFromValue(value));
                }
                return true;
            case "markerCollisionAvoidance":
                switch (value) {
                case "none":
                    series.markerCollisionAvoidance(0);
                    break;
                case "omit":
                    series.markerCollisionAvoidance(1);
                    break;
                case "fade":
                    series.markerCollisionAvoidance(2);
                    break;
                case "omitAndShift":
                    series.markerCollisionAvoidance(3);
                    break;
                case "fadeAndShift":
                    series.markerCollisionAvoidance(4);
                    break;
                }
                return true;
            case "xAxis":
                axis = this._getAxisByName(value);
                if (value !== null && (series.canUseAsXAxis === undefined ||
                    !series.canUseAsXAxis(axis))) {
                    seriesType = series.getType() ? series.getType().name : null;
                    axisType = axis.getType() ? axis.getType().name : null;
                    throw new Error($.ig.Chart.locale.invalidSeriesAxisCombination +
                        seriesType + ", xAxis: " + axisType);
                    }
                series.xAxis(this._getAxisByName(value));
                return true;
            case "yAxis":
               axis = this._getAxisByName(value);
               if (value !== null && (series.canUseAsYAxis === undefined ||
                   !series.canUseAsYAxis(axis))) {
                   seriesType = series.getType() ? series.getType().name : null;
                   axisType = axis.getType() ? axis.getType().name : null;
                   throw new Error($.ig.Chart.locale.invalidSeriesAxisCombination +
                       seriesType + ", yAxis: " + axisType);
                    }
                series.yAxis(this._getAxisByName(value));
                return true;
            case "xMemberPath":
                series.xMemberPath(value);
                return true;
            case "yMemberPath":
                series.yMemberPath(value);
                return true;
            case "trendLineType":
                switch (value) {
                case "none":
                    series.trendLineType(0);
                    break;
                case "linearFit":
                    series.trendLineType(1);
                    break;
                case "quadraticFit":
                    series.trendLineType(2);
                    break;
                case "cubicFit":
                    series.trendLineType(3);
                    break;
                case "quarticFit":
                    series.trendLineType(4);
                    break;
                case "quinticFit":
                    series.trendLineType(5);
                    break;
                case "logarithmicFit":
                    series.trendLineType(6);
                    break;
                case "exponentialFit":
                    series.trendLineType(7);
                    break;
                case "powerLawFit":
                    series.trendLineType(8);
                    break;
                case "simpleAverage":
                    series.trendLineType(9);
                    break;
                case "exponentialAverage":
                    series.trendLineType(10);
                    break;
                case "modifiedAverage":
                    series.trendLineType(11);
                    break;
                case "cumulativeAverage":
                    series.trendLineType(12);
                    break;
                case "weightedAverage":
                    series.trendLineType(13);
                    break;
                }
                return true;
            case "trendLineBrush":
                if (value === null) {
                    series.trendLineBrush(null);
                } else {
                        series.trendLineBrush(this._createBrushFromValue(value));
                }
                return true;
            case "trendLineThickness":
                series.trendLineThickness(value);
                return true;
            case "trendLinePeriod":
                series.trendLinePeriod(value);
                return true;
            case "trendLineZIndex":
                series.trendLineZIndex(value);
                return true;
				case "isTransitionInEnabled":
					series.isTransitionInEnabled(value);
					return true;
				case "transitionInSpeedType":
                    switch (value) {
						case "auto":
							series.transitionInSpeedType(0);
							break;
						case "normal":
							series.transitionInSpeedType(1);
							break;
						case "valueScaled":
							series.transitionInSpeedType(2);
							break;
						case "indexScaled":
							series.transitionInSpeedType(3);
							break;
						case "random":
							series.transitionInSpeedType(4);
							break;
					}
					return true;
				case "transitionInMode":
                    switch (value) {
						case "auto":
							series.transitionInMode(0);
							break;
						case "fromZero":
							series.transitionInMode(1);
							break;
						case "sweepFromLeft":
							series.transitionInMode(2);
							break;
						case "sweepFromRight":
							series.transitionInMode(3);
							break;
						case "sweepFromTop":
							series.transitionInMode(4);
							break;
						case "sweepFromBottom":
							series.transitionInMode(5);
							break;
						case "sweepFromCenter":
							series.transitionInMode(6);
							break;
						case "accordionFromLeft":
							series.transitionInMode(7);
							break;
						case "accordionFromRight":
							series.transitionInMode(8);
							break;
						case "accordionFromTop":
							series.transitionInMode(9);
							break;
						case "accordionFromBottom":
							series.transitionInMode(10);
							break;
						case "expand":
							series.transitionInMode(11);
							break;
						case "sweepFromCategoryAxisMinimum":
							series.transitionInMode(12);
							break;
						case "sweepFromCategoryAxisMaximum":
							series.transitionInMode(13);
							break;
						case "sweepFromValueAxisMinimum":
							series.transitionInMode(14);
							break;
						case "sweepFromValueAxisMaximum":
							series.transitionInMode(15);
							break;
						case "accordionFromCategoryAxisMinimum":
							series.transitionInMode(16);
							break;
						case "accordionFromCategoryAxisMaximum":
							series.transitionInMode(17);
							break;
						case "accordionFromValueAxisMinimum":
							series.transitionInMode(18);
							break;
						case "accordionFromValueAxisMaximum":
							series.transitionInMode(19);
							break;
                    }
					return true;
				case "transitionInDuration":
					series.transitionInDuration(value);
					return true;
            case "maximumMarkers":
                series.maximumMarkers(value);
                return true;
            case "radiusMemberPath":
                series.radiusMemberPath(value);
                return true;
            case "radiusScale":
                series.radiusScale(this._getSizeScale(value));
                return true;
            case "labelMemberPath":
                series.labelMemberPath(value);
                return true;
            case "fillMemberPath":
                series.fillMemberPath(value);
                return true;
            case "fillScale":
                series.fillScale(this._getBrushScale(value));
                return true;
            case "angleAxis":
                axis = this._getAxisByName(value);
                if (value !== null && (series.canUseAsAngleAxis === undefined ||
                    !series.canUseAsAngleAxis(axis))) {
                    seriesType = series.getType() ? series.getType().name : null;
                    axisType = axis.getType() ? axis.getType().name : null;
                    throw new Error($.ig.Chart.locale.invalidSeriesAxisCombination +
                        seriesType + ", angleAxis: " + axisType);
                    }
                series.angleAxis(this._getAxisByName(value));
                return true;
            case "valueAxis":
                    axis = this._getAxisByName(value);
                    if (value !== null && (series.canUseAsValueAxis === undefined ||
                        !series.canUseAsValueAxis(axis))) {
                        seriesType = series.getType() ? series.getType().name : null;
                        axisType = axis.getType() ? axis.getType().name : null;
                        throw new Error($.ig.Chart.locale.invalidSeriesAxisCombination +
                            seriesType + ", valueAxis: " + axisType);
                    }
                series.valueAxis(this._getAxisByName(value));
                return true;
            case "clipSeriesToBounds":
                series.clipSeriesToBounds(value);
                return true;
            case "valueMemberPath":
                series.valueMemberPath(value);
                return true;
            case "unknownValuePlotting":
                switch (value) {
                case "linearInterpolate":
                    series.unknownValuePlotting(0);
                    break;
                case "dontPlot":
                    series.unknownValuePlotting(1);
                    break;
                }
                return true;
			case "radiusX":
                series.radiusX(value);
                return true;
			case "radiusY":
                series.radiusY(value);
                return true;
			case "radius":
				series.radiusX(value);
                series.radiusY(value);
                return true;
            case "reverseLegendOrder":
                series.reverseLegendOrder(value);
                return true;
            case "angleMemberPath":
                series.angleMemberPath(value);
                return true;
            case "radiusAxis":
                axis = this._getAxisByName(value);
                if (value !== null && (
                    series.canUseAsRadiusAxis === undefined || !series.canUseAsRadiusAxis(axis))) {
                        seriesType = series.getType() ? series.getType().name : null;
                        axisType = axis.getType() ? axis.getType().name : null;
                        throw new Error($.ig.Chart.locale.invalidSeriesAxisCombination +
                            seriesType + ", radiusAxis: " + axisType);
                    }
                series.radiusAxis(this._getAxisByName(value));
                return true;
            case "useCartesianInterpolation":
                series.useCartesianInterpolation(value);
                return true;
            case "negativeBrush":
                if (value === null) {
                    series.negativeBrush(null);
                } else {
                        series.negativeBrush(this._createBrushFromValue(value));
                }
                return true;
            case "splineType":
                switch (value) {
                case "natural":
                    series.splineType(0);
                    break;
                case "clamped":
                    series.splineType(1);
                    break;
                }
                return true;
            case "lowMemberPath":
                series.lowMemberPath(value);
                return true;
            case "highMemberPath":
                series.highMemberPath(value);
                return true;
            case "openMemberPath":
                series.openMemberPath(value);
                return true;
            case "closeMemberPath":
                series.closeMemberPath(value);
                return true;
            case "volumeMemberPath":
                series.volumeMemberPath(value);
                return true;
            case "ignoreFirst":
                series.ignoreFirst(value);
                return true;
            case "period":
                series.period(value);
                return true;
            case "shortPeriod":
                series.shortPeriod(value);
                return true;
            case "longPeriod":
                series.longPeriod(value);
                return true;
            case "trianglesSource":
                series.trianglesSource(value);
                return true;
            case "triangleVertexMemberPath1":
                series.triangleVertexMemberPath1(value);
                return true;
            case "triangleVertexMemberPath2":
                series.triangleVertexMemberPath2(value);
                return true;
            case "triangleVertexMemberPath3":
                series.triangleVertexMemberPath3(value);
                return true;
            case "colorScale":
                series.colorScale(this._getColorScale(value));
                return true;
            case "colorMemberPath":
                series.colorMemberPath(value);
                return true;
            case "visibleFromScale":
                series.visibleFromScale(value);
                return true;
            case "longitudeMemberPath":
                series.longitudeMemberPath(value);
                return true;
            case "latitudeMemberPath":
                series.latitudeMemberPath(value);
                return true;
			case "valueResolver":
                series.valueResolver(this._getValueResolver(value));
                return true;
			case "shapeFilterResolution":
                series.shapeFilterResolution(value);
                return true;
			case "useBruteForce":
                series.useBruteForce(value);
                return true;
			case "progressiveLoad":
                series.progressiveLoad(value);
                return true;
                case "heatMinimumColor":
                    if (value === null) {
                        series.heatMinimumColor(null);
                    } else {
                        $tempColor = $.ig.APIFactory.prototype.createColor(value);
                        series.heatMinimumColor($tempColor);
                    }
                    return true;
                case "heatMaximumColor":
                    if (value === null) {
                        series.heatMaximumColor(null);
                    } else {
                        $tempColor = $.ig.APIFactory.prototype.createColor(value);
                        series.heatMaximumColor($tempColor);
                    }
                    return true;
			case "mouseOverEnabled":
                series.mouseOverEnabled(value);
                return true;
			case "pointExtent":
				series.pointExtent(value);
				return true;
			case "heatMinimum":
                series.heatMinimum(value);
                return true;
			case "heatMaximum":
                series.heatMaximum(value);
                return true;
			case "multiplier":
				series.multiplier(value);
				return true;
			case "smoothingPeriod":
				series.smoothingPeriod(value);
				return true;
			case "triggerPeriod":
				series.triggerPeriod(value);
				return true;
			case "signalPeriod":
				series.signalPeriod(value);
				return true;
			case "useHighMarkerFidelity":
				series.useHighMarkerFidelity(value);
				return true;
			case "series":
				this._setSubSeries(series, value);
				return true;
			case "opacity":
                    $old = series.opacity();
				series.opacity(value);
                    if (series.raisePropertyChanged) {
                        series.raisePropertyChanged("Opacity", $old, value);
                    }
				if (series.renderSeries) {
					series.renderSeries(false);
				}
				return true;
                case "areaFillOpacity":
                    series.areaFillOpacity(value);
                    return true;
                case "expectFunctions":
                    series.expectFunctions(value);
                    return true;
                case "useInterpolation":
                    series.useInterpolation(value);
                    return true;
                case "skipUnknownValues":
                    series.skipUnknownValues(value);
                    return true;
                case "verticalLineVisibility":
                    switch (value) {
                        case "visible":
                            series.verticalLineVisibility(0);
                            break;
                        case "collapsed":
                            series.verticalLineVisibility(1);
                            break;
                        default:
                            break;
            }
                    return true;
                case "horizontalLineVisibility":
                    switch (value) {
                        case "visible":
                            series.horizontalLineVisibility(0);
                            break;
                        case "collapsed":
                            series.horizontalLineVisibility(1);
                            break;
                        default:
                            break;
                    }
                    return true;
                case "targetSeries":
					if (value === undefined || value === null || this._getNotifyTarget(value) === null) {
                        series.targetSeries(null);
                    } else {
                        series.targetSeries(this._getNotifyTarget(value));
                    }
                    return true;
                case "targetAxis":
					if (value === undefined || value === null || this._getNotifyTarget(value) === null) {
                        series.targetAxis(null);
                    } else {
                        series.targetAxis(this._getNotifyTarget(value));
                    }
                    return true;
				case "cursorPosition":
					series.cursorPosition($.ig.APIFactory.prototype.createPoint(value.x, value.y));
					return true;
				case "bandHighlightWidth":
					series.bandHighlightWidth(value);
					return true;
				case "toolTipPosition":
					switch (value) {
                        case "auto":
                            series.toolTipPosition(0);
                            break;
                        case "outsideStart":
                            series.toolTipPosition(1);
                            break;
						case "insideStart":
                            series.toolTipPosition(2);
                            break;
						case "insideEnd":
                            series.toolTipPosition(3);
                            break;
						case "outsideEnd":
                            series.toolTipPosition(4);
                            break;
                        default:
                            break;
                    }
                    return true;
				case "highlightType":
					switch (value) {
                        case "auto":
                            series.highlightType(0);
                            break;
                        case "marker":
                            series.highlightType(1);
                            break;
						case "shape":
                            series.highlightType(2);
                            break;
                        default:
                            break;
                    }
                    return true;
				case "useIndex":
                    series.useIndex(value);
                    return true;
				case "useLegend":
                    series.useLegend(value);
                    return true;
                case "isCustomCategoryStyleAllowed":
                    series.isCustomCategoryStyleAllowed(value);
                    return true;
				case "isCustomCategoryMarkerStyleAllowed":
                    series.isCustomCategoryMarkerStyleAllowed(value);
                    return true;
                case "isHighlightingEnabled":
                    series.isHighlightingEnabled(value);
                    return true;
				case "consolidatedColumnVerticalPosition":
					switch (value) {
						case "minimum":
							series.consolidatedColumnVerticalPosition(0);
							break;
						case "maximum":
							series.consolidatedColumnVerticalPosition(1);
							break;
						case "median":
							series.consolidatedColumnVerticalPosition(2);
							break;
						case "relativeMinimum":
							series.consolidatedColumnVerticalPosition(3);
							break;
						case "relativeMaximum":
							series.consolidatedColumnVerticalPosition(4);
							break;
					}
					return true;
            }

            return false;
        },
        _getSubSeriesByName: function (parentSeries, name) {
            var subSeries = null;

			if (parentSeries === null) {
				for (var s in this.widget._seriesSub) {
					parentSeries = this._getSeriesByName(s);
					if (parentSeries !== null) {
						subSeries = this._getSubSeriesByName(parentSeries, name);
						if (subSeries !== null) {
							return subSeries;
						}
					}
				}
			}
			if (!parentSeries) {
				return null;
			}
			var parentName = parentSeries.name();

			if (this.widget._seriesSub[ parentName ][ name ] === undefined) {
                return null;
			}

			return this.widget._seriesSub[ parentName ][ name ];
		},
		_getIndexByName: function (fromArray, name) {
		    for (var i = 0; i < fromArray.length; i++) {
		        if (fromArray[ i ].name &&
                    fromArray[ i ].name === name) {
		            return i;
		        }
		    }
		    return -1;
		},
		_setSubSeries: function (parentSeries, subSeries) {
            var self = this, series = null, widget = this.widget, seriesIsNew = false,
            subSeriesOpt, parentInd, i, parentName =
            parentSeries.name(), seriesOpt, checkParent;
			parentInd = -1;
			for (i = 0; i < widget.options.series.length; i++) {
				checkParent = widget.options.series[ i ];
				if (checkParent && checkParent.name === parentName) {
					parentInd = i;
					break;
				}
			}

			//set series
			$.each(subSeries, function (i, val) {
				if (!val.name) {
					throw new Error($.ig.Chart.locale.seriesName);
				}

				if (self._getSeriesByName(val.name)) {
					throw new Error($.ig.Chart.locale.nameInUse);
				}

				series = self._getSubSeriesByName(parentSeries, val.name);
				if (series && val.remove) {
					if (widget._seriesSub[ parentName ]) {
						delete widget._seriesSub[ parentName ][ val.name ];
					}

					if (widget._seriesSubOpt[ parentName ]) {
						subSeriesOpt = widget._seriesSubOpt[ parentName ][ val.name ];
						delete widget._seriesSubOpt[ parentName ][ val.name ];
					}

					if (parentInd !== -1) {
					    var ind =
                            self._getIndexByName(
                            widget.options.series[ parentInd ].series,
                            subSeriesOpt.name);
						widget.options.series[ parentInd ].series.splice(ind, 1);
					}

					parentSeries.series().remove(series);
					self._clearAxes(series);

					return;
				}
				if (!series && !val.remove) {
					series = self._createSeriesFromType(val.type);
					self._bindSeriesEvents(series);
					series.name(val.name);
					seriesIsNew = true;
				}
				if (series) {
					seriesOpt = widget._seriesSubOpt[ parentName ][ val.name ];
					if (seriesIsNew) {
						widget._seriesSub[ parentName ][ val.name ] = series;
						if (parentInd !== -1) {
							/* Graham 03/14/2013 Bug 135879 */
							/* VS 04/02/2013 Bug 138644 */
							self._setForName(widget.options.series, val);
						}
						/* Graham 03/14/2013 Bug 135879 */
						widget._seriesSubOpt[ parentName ][ val.name ] = val;
					}
					if (val.coercionMethods && series.coercionMethods) {
						series.coercionMethods(val.coercionMethods);
					}
                    if (widget.options.expectFunctions !== undefined &&
						widget.options.expectFunctions && series.expectFunctions) {
                        series.expectFunctions(widget.options.expectFunctions);
                    }
                    if (val.expectFunctions !== undefined && series.expectFunctions) {
                        series.expectFunctions(val.expectFunctions);
                    }
                    self._setSubSeriesOptions(val, parentSeries, series, widget);

                    if (seriesIsNew) {
						parentSeries.series().add(series);
					}
				}
			});
		},
		_getValueResolver: function (value) {
			var valueResolver;
			if (value.type === undefined || value.type === "linear") {
				valueResolver = new $.ig.LinearContourValueResolver();
			}
			if (value.valueCount) {
				valueResolver.valueCount(value.valueCount);
			}
			return valueResolver;
		},
        _getSizeScale: function (value) {
		    if (value === null) {
		        return value;
		    }

            var scale = new $.ig.SizeScale();
            if (value.minimumValue !== undefined) {
                scale.minimumValue(value.minimumValue);
            }
            if (value.maximumValue !== undefined) {
                scale.maximumValue(value.maximumValue);
            }
            if (value.isLogarithmic !== undefined) {
                scale.isLogarithmic(value.isLogarithmic);
            }
            if (value.logarithmBase !== undefined) {
                scale.logarithmBase(value.logarithmBase);
            }
            return scale;
        },
        _getColorScale: function (value) {
            var type = "customPalette", scale = null;

		    if (value === null) {
		        return value;
		    }

            if (value.type) {
                type = value.type;
            }

            switch (type) {
            case "customPalette":
                scale = new $.ig.CustomPaletteColorScale();
                break;
            }

            if (scale) {
                if (value.minimumValue) {
                    scale.minimumValue(value.minimumValue);
                }
                if (value.maximumValue) {
                    scale.maximumValue(value.maximumValue);
                }

                if (value.interpolationMode) {
                    switch (value.interpolationMode) {
                    case "select":
                        scale.interpolationMode(0);
                        break;
                    case "interpolateRGB":
                        scale.interpolationMode(1);
                        break;
                    case "interpolateHSV":
                        scale.interpolationMode(2);
                        break;
                    }
                }

                if (value.palette) {
                    scale.providePalette(value.palette);
                }
            }

            return scale;
        },
        _getBrushScale: function (value) {
            var type = "value", scale = null, isRGB, i, brushes, $tempBrushCollection;

		    if (value === null) {
		        return null;
		    }

            if (value.type) {
                type = value.type;
            }

            switch (type) {
            case "value":
                scale = new $.ig.ValueBrushScale();
                break;
            case "customPalette":
                scale = new $.ig.CustomPaletteBrushScale();
                break;
            }

            if (scale) {
                if (value.brushes) {
                    isRGB = true;
                    brushes = value.brushes;
                    if ((typeof brushes[ 0 ] === "string" && brushes[ 0 ] === "HSV") ||
                    brushes[ 0 ] === "RGB")
                    {
                        if (brushes[ 0 ] === "HSV") { isRGB = false; } brushes = brushes.slice(1);
                    }
                    $tempBrushCollection = new $.ig.BrushCollection();
                    for (i = 0; i < brushes.length; i++) {
                        $tempBrushCollection.add(this._createBrushFromValue(brushes[ i ]));
                    }
                    scale.brushes($tempBrushCollection);
                }
                if (value.brushSelectionMode) {
                    switch (value.brushSelectionMode) {
                    case "select":
                        scale.brushSelectionMode(0);
                        break;
                    case "interpolate":
                        scale.brushSelectionMode(1);
                        break;
                    }
                }
                if (value.minimumValue) {
                    scale.minimumValue(value.minimumValue);
                }
                if (value.maximumValue) {
                    scale.maximumValue(value.maximumValue);
                }
                if (value.isLogarithmic) {
                    scale.isLogarithmic(value.isLogarithmic);
                }
                if (value.logarithmBase) {
                    scale.logarithmBase(value.logarithmBase);
                }
            }

			return scale;
		},
		_setAxisOption: function (axis, key, value) {
			try {
				return this._setAxisOptionHelper(axis, key, value);
			} catch (err) {
				if (axis[ key ] === undefined) {
					throw new Error($.ig.Chart.locale.axisUnsupportedOption + key);
				} else {
					throw err;
				}
			}
		},
		_setAxisOptionHelper: function (axis, key, value) {
			switch (key) {
			case "strokeThickness":
			case "majorStrokeThickness":
			case "minorStrokeThickness":
			case "isInverted":
			case "crossingValue":
			case "label":
			case "gap":
			case "overlap":
			case "interval":
			case "minorInterval":
			case "useEnhancedIntervalManagement":
			case "enhancedIntervalMinimumCharacters":
			case "startAngleOffset":
			case "minimumValue":
			case "maximumValue":
			case "referenceValue":
			case "isLogarithmic":
			case "logarithmBase":
			case "radiusExtentScale":
			case "innerRadiusExtentScale":
			case "dateTimeMemberPath":
			case "formatLabel":
			case "title":
			case "useClusteringMode":
                if (axis[ key ]) {
                    axis[ key ](value);
                }
                return true;
            case "useSmartAxis":
                axis.useSmartAxis(value);
                break;
            case "smartAxisExtent":
                axis.smartAxisExtent(value);
                break;
            case "smartAxisMinimumExtent":
                axis.smartAxisMinimumExtent(value);
                break;
            case "smartAxisMaximumExtent":
                axis.smartAxisMaximumExtent(value);
                break;
            case "smartAxisExtentType":
                axis.smartAxisExtentType(value);
                break;
            case "smartAxisAngle":
                axis.smartAxisAngle(value);
                break;
            case "smartAxisMinimumAngle":
                axis.smartAxisMinimumAngle(value);
                break;
            case "smartAxisMaximumAngle":
                axis.smartAxisMaximumAngle(value);
                break;
            case "smartAxisFont":
                axis.smartAxisFont(value);
                break;
            case "smartAxisFontSize":
                axis.smartAxisFontSize(value);
                break;
            case "smartAxisMinimumFontSize":
                axis.smartAxisMinimumFontSize(value);
                break;
            case "smartAxisMaximumFontSize":
                axis.smartAxisMaximumFontSize(value);
                break;
            case "smartAxisNumberOfStaggerLevels":
                axis.smartAxisNumberOfStaggerLevels(value);
                break;
            case "smartAxisMinimumStaggerLevels":
                axis.smartAxisMinimumStaggerLevels(value);
                break;
            case "smartAxisMaximumStaggerLevels":
                axis.smartAxisMaximumStaggerLevels(value);
                break;
            case "smartAxisVerticalAlignment":
                axis.smartAxisVerticalAlignment(value);
                break;
            case "smartAxisTopMargin":
                axis.smartAxisTopMargin(value);
                break;
            case "smartAxisProximityMargin":
                axis.smartAxisProximityMargin(value);
                break;
            case "stroke":
                    axis.stroke(this._createBrushFromValue(value));
                return true;
            case "strip":
                    axis.strip(this._createBrushFromValue(value));
                return true;
            case "majorStroke":
                    axis.majorStroke(this._createBrushFromValue(value));
                return true;
            case "minorStroke":
                    axis.minorStroke(this._createBrushFromValue(value));
                return true;
            case "scaleMode":
                switch (value) {
                case "linear":
                    axis.scaleMode(0);
                    break;
                case "logarithmic":
                    axis.scaleMode(1);
                    break;
                }
                return true;
                case "expectFunctions":
                    axis.expectFunctions(value);
                    return true;
                case "displayType":
                    switch (value) {
                        case "continuous":
                            axis.displayType(0);
                            break;
                        case "discrete":
                            axis.displayType(1);
                            break;
                    }
                    return true;
			    case "isDataPreSorted":
			        axis.isDataPreSorted(value);
			        return true;
            }
        },
        _setCoreAxisOption: function (axis, key, value) {
            var labelSettings = null, titleSettings = null, tempAxis, self = this;

            switch (key) {
            case "crossingAxis":
                if (value) {
                    tempAxis = this._getAxisByName(value);
                    if (tempAxis) {
                        axis.crossingAxis(tempAxis);
                    } else {
                        this.widget._pendingCrossingAxes.push(function () {
                            var tempAxis = self._getAxisByName(value);
                            if (tempAxis) {
                                axis.crossingAxis(tempAxis);
                            }
                        });
                    }
                } else {
                    axis.crossingAxis(null);
                }
                break;
            case "labelLocation":
                labelSettings = axis.labelSettings();
                if (labelSettings === null) {
                    labelSettings = new $.ig.AxisLabelSettings();
                }
                switch (value) {
                case "outsideTop":
                    labelSettings.location(0);
                    break;
                case "outsideBottom":
                    labelSettings.location(1);
                    break;
                case "outsideLeft":
                    labelSettings.location(2);
                    break;
                case "outsideRight":
                    labelSettings.location(3);
                    break;
                case "insideTop":
                    labelSettings.location(4);
                    break;
                case "insideBottom":
                    labelSettings.location(5);
                    break;
                case "insideLeft":
                    labelSettings.location(6);
                    break;
                case "insideRight":
                    labelSettings.location(7);
                    break;
                default:
                    break;
                }
                if (!axis.labelSettings()) {
                    axis.labelSettings(labelSettings);
                }
                break;
            case "labelVisibility":
                labelSettings = axis.labelSettings();
                if (labelSettings === null) {
                    labelSettings = new $.ig.AxisLabelSettings();
                }
                switch (value) {
                case "visible":
                    labelSettings.visibility(0);
                    break;
                case "collapsed":
                    labelSettings.visibility(1);
                    break;
                default:
                    break;
                }
                if (!axis.labelSettings()) {
                    axis.labelSettings(labelSettings);
                }
                break;
            case "labelExtent":
                if (value === null) {
                    value = NaN;
                }
                labelSettings = axis.labelSettings();
                if (labelSettings === null) {
                    labelSettings = new $.ig.AxisLabelSettings();
                }
                labelSettings.extent(value);
                if (!axis.labelSettings()) {
                    axis.labelSettings(labelSettings);
                }
                break;
                case "labelHorizontalAlignment":
                    labelSettings = axis.labelSettings();
                    if (labelSettings === null) {
                        labelSettings = new $.ig.AxisLabelSettings();
                    }
                    switch (value) {
                        case "left":
                            labelSettings.horizontalAlignment(0);
                            break;
                        case "center":
                            labelSettings.horizontalAlignment(1);
                            break;
                        case "right":
                            labelSettings.horizontalAlignment(2);
                            break;
                        case "stretch":
                            labelSettings.horizontalAlignment(1);
                            break;
                        default:
                            break;
                    }
                    if (!axis.labelSettings()) {
                        axis.labelSettings(labelSettings);
                    }
                    break;
                case "labelVerticalAlignment":
                    labelSettings = axis.labelSettings();
                    if (labelSettings === null) {
                        labelSettings = new $.ig.AxisLabelSettings();
                    }
                    switch (value) {
                        case "top":
                            labelSettings.verticalAlignment(0);
                            break;
                        case "center":
                            labelSettings.verticalAlignment(1);
                            break;
                        case "bottom":
                            labelSettings.verticalAlignment(2);
                            break;
                        case "stretch":
                            labelSettings.verticalAlignment(1);
                            break;
                        default:
                            break;
                    }
                    if (!axis.labelSettings()) {
                        axis.labelSettings(labelSettings);
                    }
                    break;
                case "labelMargin":
                    labelSettings = axis.labelSettings();
                    if (labelSettings === null) {
                        labelSettings = new $.ig.AxisLabelSettings();
                    }
                    labelSettings.margin(value);
                    if (!axis.labelSettings()) {
                        axis.labelSettings(labelSettings);
                    }
                    break;
                case "labelTopMargin":
                    labelSettings = axis.labelSettings();
                    if (labelSettings === null) {
                        labelSettings = new $.ig.AxisLabelSettings();
                    }
                    labelSettings.topMargin(value);
                    if (!axis.labelSettings()) {
                        axis.labelSettings(labelSettings);
                    }
                    break;
                case "labelRightMargin":
                    labelSettings = axis.labelSettings();
                    if (labelSettings === null) {
                        labelSettings = new $.ig.AxisLabelSettings();
                    }
                    labelSettings.rightMargin(value);
                    if (!axis.labelSettings()) {
                        axis.labelSettings(labelSettings);
                    }
                    break;
                case "labelBottomMargin":
                    labelSettings = axis.labelSettings();
                    if (labelSettings === null) {
                        labelSettings = new $.ig.AxisLabelSettings();
                    }
                    labelSettings.bottomMargin(value);
                    if (!axis.labelSettings()) {
                        axis.labelSettings(labelSettings);
                    }
                    break;
                case "labelLeftMargin":
                    labelSettings = axis.labelSettings();
                    if (labelSettings === null) {
                        labelSettings = new $.ig.AxisLabelSettings();
                    }
                    labelSettings.leftMargin(value);
                    if (!axis.labelSettings()) {
                        axis.labelSettings(labelSettings);
                    }
                    break;
                case "showFirstLabel":
                    labelSettings = axis.labelSettings();
                    if (labelSettings === null) {
                        labelSettings = new $.ig.AxisLabelSettings();
                    }
                    labelSettings.showFirstLabel(value);
                    if (!axis.labelSettings()) {
                        axis.labelSettings(labelSettings);
                    }
                    break;
            case "labelAngle":
                if (value === null) {
                    value = 0;
                }
                labelSettings = axis.labelSettings();
                if (labelSettings === null) {
                    labelSettings = new $.ig.AxisLabelSettings();
                }
                labelSettings.angle(value);
                if (!axis.labelSettings()) {
                    axis.labelSettings(labelSettings);
                }
                break;
            case "labelTextStyle":
                labelSettings = axis.labelSettings();
                if (labelSettings === null) {
                    labelSettings = new $.ig.AxisLabelSettings();
                }
                labelSettings.textStyle(value);
                if (!axis.labelSettings()) {
                    axis.labelSettings(labelSettings);
                }
                break;
            case "labelTextColor":
                labelSettings = axis.labelSettings();
                if (labelSettings === null) {
                    labelSettings = new $.ig.AxisLabelSettings();
                }
                    labelSettings.textColor(this._createBrushFromValue(value));
                if (!axis.labelSettings()) {
                    axis.labelSettings(labelSettings);
                }
                    break;
                case "titleAngle":
                    if (value === null) {
                        value = 0;
                    }
                    titleSettings = axis.titleSettings();
                    if (titleSettings === null) {
                        titleSettings = new $.ig.TitleSettings();
                    }
                    titleSettings.angle(value);
                    if (!axis.titleSettings()) {
                        axis.titleSettings(titleSettings);
                    }
                    break;
                case "titleTextStyle":
                    titleSettings = axis.titleSettings();
                    if (titleSettings === null) {
                        titleSettings = new $.ig.TitleSettings();
                    }
                    titleSettings.textStyle(value);
                    if (!axis.titleSettings()) {
                        axis.titleSettings(titleSettings);
                    }
                    break;
                case "titleMargin":
                    titleSettings = axis.titleSettings();
                    if (titleSettings === null) {
                        titleSettings = new $.ig.TitleSettings();
                    }
                    titleSettings.margin(value);
                    if (!axis.titleSettings()) {
                        axis.titleSettings(titleSettings);
                    }
                    break;
                case "titleTopMargin":
                    titleSettings = axis.titleSettings();
                    if (titleSettings === null) {
                        titleSettings = new $.ig.TitleSettings();
                    }
                    titleSettings.topMargin(value);
                    if (!axis.titleSettings()) {
                        axis.titleSettings(titleSettings);
                    }
                    break;
                case "titleRightMargin":
                    titleSettings = axis.titleSettings();
                    if (titleSettings === null) {
                        titleSettings = new $.ig.TitleSettings();
                    }
                    titleSettings.rightMargin(value);
                    if (!axis.titleSettings()) {
                        axis.titleSettings(titleSettings);
                    }
                break;
                case "titleBottomMargin":
                    titleSettings = axis.titleSettings();
                    if (titleSettings === null) {
                        titleSettings = new $.ig.TitleSettings();
                    }
                    titleSettings.bottomMargin(value);
                    if (!axis.titleSettings()) {
                        axis.titleSettings(titleSettings);
            }
                    break;
                case "titleLeftMargin":
                    titleSettings = axis.titleSettings();
                    if (titleSettings === null) {
                        titleSettings = new $.ig.TitleSettings();
                    }
                    titleSettings.leftMargin(value);
                    if (!axis.titleSettings()) {
                        axis.titleSettings(titleSettings);
                    }
                    break;
                case "titleTextColor":
                    titleSettings = axis.titleSettings();
                    if (titleSettings === null) {
                        titleSettings = new $.ig.TitleSettings();
                    }
                    titleSettings.textColor(this._createBrushFromValue(value));
                    if (!axis.titleSettings()) {
                        axis.titleSettings(titleSettings);
                    }
                    break;

                case "titleVerticalAlignment":
                    titleSettings = axis.titleSettings();
                    if (titleSettings === null) {
                        titleSettings = new $.ig.TitleSettings();
                    }
                    switch (value) {
                        case "top":
                            titleSettings.verticalAlignment(0);
                            break;
                        case "center":
                            titleSettings.verticalAlignment(1);
                            break;
                        case "bottom":
                            titleSettings.verticalAlignment(2);
                            break;
                        default:
                            break;
                    }
                    if (!axis.titleSettings()) {
                        axis.titleSettings(titleSettings);
                    }
                    break;
                case "titleHorizontalAlignment":
                    titleSettings = axis.titleSettings();
                    if (titleSettings === null) {
                        titleSettings = new $.ig.TitleSettings();
                    }

                    switch (value) {
                        case "left":
                            titleSettings.horizontalAlignment(0);
                            break;
                        case "center":
                            titleSettings.horizontalAlignment(1);
                            break;
                        case "right":
                            titleSettings.horizontalAlignment(2);
                            break;
                        default:
                            break;
                    }
                    if (!axis.titleSettings()) {
                        axis.titleSettings(titleSettings);
                    }
                    break;
                case "titlePosition":
                    titleSettings = axis.titleSettings();
                    if (titleSettings === null) {
                        titleSettings = new $.ig.TitleSettings();
                    }
                    switch (value) {
                        case "auto":
                            titleSettings.position(0);
                            break;
                        case "left":
                            titleSettings.position(1);
                            break;
                        case "right":
                            titleSettings.position(2);
                            break;
                        case "top":
                            titleSettings.position(3);
                            break;
                        case "bottom":
                            titleSettings.position(4);
                            break;
                        default:
                            break;
                    }
                    if (!axis.titleSettings()) {
                        axis.titleSettings(titleSettings);
                    }
                break;
                case "tickLength":
                    axis.tickLength(value);
                    break;
                case "tickStrokeThickness":
                    axis.tickStrokeThickness(value);
                    break;
                case "tickStroke":
                    axis.tickStroke(this._createBrushFromValue(value));
                    break;
					}
        },
        _checkObjectForDataSourceProperty: function (value) {
            if (value && Object.prototype.toString.call(value) === "[object Array]") {
                var ret = [];
                for (var index = 0; index < value.length; index++) {
                    if (value[ index ].dataSource !== undefined ||
                    value[ index ].dataSourceUrl !== undefined) {
                        ret[ ret.length ] = value[ index ];
                    }
                }

                if (ret.length === 0) {
                    return null;
                }
                return ret;
            }

            return null;
        },

        _checkObjectForShapeDataSourceProperty: function (value) {
            if (value && Object.prototype.toString.call(value) === "[object Array]") {
                var ret = [];
                for (var index = 0; index < value.length; index++) {
                    if (value[ index ].shapeDataSource !== undefined || value[ index ].
                    triangulatedDataSource !== undefined) {
                        ret[ ret.length ] = value[ index ];
                    }
                }

                if (ret.length === 0) {
                    return null;
                }
                return ret;
            }

            return null;
        },
        _setOption: function (key, value) {
            var arbitraryDataSource = this._checkObjectForDataSourceProperty(value);
            var arbitraryShapeDataSource = this._checkObjectForShapeDataSourceProperty(value);
            var dataOptions = {}, id, index;

            if (key === "dataSource" || key === "dataSourceUrl" || key === "responseDataKey") {
                if (key === "dataSource" || key === "dataSourceUrl") {
                    dataOptions.dataSource = value;
                    if (this.widget.options.responseDataKey) {
                        dataOptions.responseDataKey = this.widget.options.responseDataKey;
                    }
                    id = this.widget._containerSourceID;
                } else if (key === "responseDataKey") {
                    dataOptions.responseDataKey = value;
                    if (this.widget.options.dataSource) {
                        dataOptions.dataSource = this.widget.options.dataSource;
                    } else if (this.widget.options.dataSourceUrl) {
                        dataOptions.dataSource = this.widget.options.dataSourceUrl;
                    }
                    id = this.widget._containerSourceID;
                }

                this.widget._setCoreCallbackOptions = { key: key, value: value };
                this._setupDataSource(dataOptions, this._chartDataCallBack);
                if (id === undefined || id === null) {
                    id = this.widget._containerSourceID;
                }
                this.widget.dataSources[ id ].dataBind();
                if (key === "dataSource") {
                    this.widget.options.dataSource = value;
                }
                if (key === "dataSourceUrl") {
                    this.widget.options.dataSourceUrl = value;
                }
                if (key === "responseDataKey") {
                    this.widget.options.responseDataKey = value;
                }
            } else if (arbitraryDataSource) {
                for (index = 0; index < arbitraryDataSource.length; index++) {
                    dataOptions = arbitraryDataSource[ index ];
                    id = arbitraryDataSource[ index ].name;
                    if (this.widget._target && this.widget._target.name() === id) {
                        this.widget._target = null;
                    }

                    this.widget._setCoreCallbackOptions = { key: key, value: value };
                    this._setupDataSource(dataOptions, this._chartDataCallBack);
                    this.widget.dataSources[ id ].dataBind();
            }
            } else if (arbitraryShapeDataSource) {
                for (index = 0; index < arbitraryShapeDataSource.length; index++) {
                    var obj = value[ index ];
                    this.widget._initConverter(obj, this._convDataCallback);
                    this.widget._setCoreCallbackOptions = { key: key, value: value };
                }
            } else {
                this._setOptionAfterInit(key, value);
            }
        },
        _chartDataCallBack: function (success, e, dataSource) {
            var key = this.widget._setCoreCallbackOptions.key,
            value = this.widget._setCoreCallbackOptions.value;
            if (!success) {
                return false;
            }
            this._setItemsSource(this.widget, this._getNotifyTarget(dataSource.settings.id),
            dataSource.settings.id);
			if (key === "series" || key === "axes") {
				this._setOptionAfterInit(key, value);
			}
        },
        _convDataCallback: function () {
            var context = this.settings.callee, id = this.settings.id,
            key = context.widget._setCoreCallbackOptions.key,
            value = context.widget._setCoreCallbackOptions.value;
            context._setItemsSource(context.widget, context._getNotifyTarget(id), id);
			if (key === "series") {
				context._setOptionAfterInit(key, value);
			}
        },
		_setOptionAfterInit: function (key, value) {
			if (!this._setWidgetOption(this.widget._chart, key, value)) {
                this._setCoreWidgetOption(this.widget._chart, key, value);
            } else {

                //still propagate a value into the options so that it can be examined by the user.
                this.widget.options[ key ] = value;
            }
		},
        _setItemsSource: function (widget, target, id) {
            var ds = widget.dataSources[ id ];
            if (target === widget._chart) {
                if (ds !== null) {
                    widget._chart.setWidgetLevelDataSource(ds);
                } else {
                    widget._chart.removeWidgetLevelDataSource();
                }
            } else {
                if (ds !== null) {
                    widget._chart.setSpecificDataSource(id, ds);
                } else {
                    widget._chart.removeSpecificDataSource(id, false);
                }
            }
        },
        _initialDataBind: function () {
            var ds = this._initDataSources(), self = this;
            $.each(ds, function (i) {
                if (typeof self.widget.dataSources[ i ].dataBind === "function") {
                    self.widget.dataSources[ i ].dataBind();
                }
            });
        },
        _initDataSources: function () {
            var self = this, widget = this.widget, options = this.widget.options, axes, series;
            widget.dataSources = {};
            widget.dsCount = 0;
            if (options.dataSource || options.dataSourceUrl) {
                widget.dsCount++;
                self._setupDataSource(options, this._initCallback);
            }
            if (options.shapeDataSource || options.triangulationDataSource) {
                widget.dsCount++;
                self.widget._initConverter(options, self._converterCallback);
            }
            if (options.axes) {
                axes = options.axes;
                $.each(axes, function (i, val) {
                    if (val.dataSource || val.dataSourceUrl) {
                        widget.dsCount++;
                        self._setupDataSource(val, self._initCallback);
                    }
                });
            }
            if (options.series) {
                series = options.series;
                $.each(series, function (i, val) {
                    if (val.dataSource || val.dataSourceUrl) {
                        widget.dsCount++;
                        self._setupDataSource(val, self._initCallback);
                    }
                    if (val.shapeDataSource || val.triangulationDataSource) {
                        widget.dsCount++;
                        self.widget._initConverter(val, self._converterCallback);
                    }
                });
            }
            return widget.dataSources;
        },
        _drawCanvas: function (canvasElemnts, iWidth, iHeight) {
            var oSaveCanvas = document.createElement("canvas"), oSaveCtx;

            oSaveCanvas.width = iWidth;
            oSaveCanvas.height = iHeight;
            oSaveCanvas.style.width = iWidth + "px";
            oSaveCanvas.style.height = iHeight + "px";

            oSaveCtx = oSaveCanvas.getContext("2d");
            $.each(canvasElemnts, function (i, canvas) {
                oSaveCtx.drawImage(canvas, 0, 0, iWidth, iHeight);
            });
            return oSaveCanvas;
        },
        _getLegendElements: function () {
            var options = this.widget.options, elements = [];
            if (this.widget.options.legend) {
                elements.push($("#" + this.widget.options.legend.element));
            }
            if (options.series) {
                $.each(options.series, function (i, ser) {
                    if (ser && ser.legend) {
                        elements.push($("#" + ser.legend.element));
                    }
                });
            }
            return elements;
        },
        _print: function () {
            var doc = document,
				widget = this.widget,
				legends = this._getLegendElements(),
				chartWrapper = $("#" + this.widget.id()),
				origDisplay = [],
				origWrappers = [],
				body = doc.body,
				childNodes = body.childNodes;

            if (widget._isPrinting) {
                return;
            }

            widget._isPrinting = true;

            $.each(childNodes, function (i, node) {
                if (node.nodeType === 1) {
                    origDisplay[ i ] = node.style.display;
                    var matched = false;
                    $.each(chartWrapper, function (i, chartNode) {
                        if (node === chartNode) {
                            matched = true;
                            return;
                        }
                    });
                    $.each(legends, function (i, subLegend) {
                        if (subLegend === node) {
                            matched = true;
                            return;
                        }
                    });
                    if (matched) {
                        return;
                    }
                    node.style.display = "none";
                }
            });

            origWrappers[ 0 ] = (chartWrapper.parent());
            $(body).append(chartWrapper);

            $.each(legends, function (i, legend) {
                origWrappers[ i + 1 ] = legend.parent();
                $(body).append(legend);
            });

            window.print();

            //restore
            setTimeout(function () {
                widget._isPrinting = false;
                origWrappers[ 0 ].prepend(chartWrapper);
                $.each(legends, function (i, legend) {
                    origWrappers[ i + 1 ].prepend(legend);
                });

                $.each(childNodes, function (i, node) {
                    if (node.nodeType === 1) {
                        node.style.display = origDisplay[ i ];
                    }
                });

            }, 1000);
        },
        _getImage: function (width, height, chart) {
            var expCanvas, imgElement = document.createElement("img");
            width = width || $("#" + chart.id() + "_chart_container").width();
            height = height || $("#" + chart.id() + "_chart_container").height();
            expCanvas = this._drawCanvas($("#" + chart.id() + " canvas"), width, height);
            imgElement.src = expCanvas.toDataURL("image/png");
            return imgElement;
        },
		_initSize: function (widget, o) {
			var v, key, size, i = -1,
				chart = widget._chart,
				elem = widget.element[ 0 ];
			this._oldProp = { width: elem.style.width, height: elem.style.height };
			while (i++ < 1) {
				key = i === 0 ? "width" : "height";
				if (o[ key ]) {
				    size = key;
				} else {
					v = elem.style[ key ];
					if (!v || (v && v.indexOf("%") > 0)) {
						this._setSize(chart, size = key, v || 500);
					}
				}
			}

			// _setSize should be called at least once: support for initially invisible container of char
			if (!size) {
				this._setSize(chart, "width");
			}
		},
		_setSize: function (chart, key, val) {
			$.ig.util.setSize(this.widget.element, key, val, chart, "notifyContainerResized");
		},
        _renderChartContainer: function (chart) {
            var opt = chart.options, chartElement;

            if (!chart._isRendered) {
                chartElement =
                $("<div id='" + chart.id() +
                    "_chart_container' style='width:100%;height:100%'></div>").
                    appendTo(chart.element);
                chartElement.addClass(chart.css.chart || chart.css.map);
                this._chartElement = chartElement;
                chart._chart.provideContainer(chartElement);
                this._initSize(chart, opt);
            }
        },

        _setLegend: function (item, value) {
			var legend;
			if (value !== null) {
				value.owner = this.widget;
				if (value.type === undefined) {
					value.type = (this.widget.widgetName === "igPieChart") ? "item" : "legend";
				}
				if (item.legend() === null) {

					//check if the widget is created already
				    if ($("#" + value.element).data("igChartLegend") ||
                        (value.data && value.data("igChartLegend"))) {
					    legend = (!value.element) ?
                        value.data("igChartLegend") : $("#" + value.element).data("igChartLegend");
						legend.options.owner = this.widget.options;
						legend._owner = this.widget;
					} else {
						legend = $("#" + value.element).igChartLegend(value).data("igChartLegend");
					}
					item.legend(legend.legend);
				} else {
					$("#" + item.legend().name()).igChartLegend(value);
				}
			} else {
				if (item.legend() !== null && $("#" + item.legend().name()).length > 0) {
					$("#" + item.legend().name()).igChartLegend("destroy");
				}
			}
        },

        _mergeDataSourceSettings: function (s1, s2) {
            if (!s1) {
                return s2;
            }
            if (!s2) {
                return s1;
            }
            var source1 = s1.dataSource, source2 = s2.dataSource, newSettings;
            if (source1 && ($.type(source1) === "array" || $.type(source1) === "object")) {
                s1.dataSource = null;
            }
            if (source2 && ($.type(source2) === "array" || $.type(source2) === "object")) {
                s2.dataSource = null;
            }
            newSettings = $.extend(true, {}, s1, s2);
            if (source2 && ($.type(source2) === "array" || $.type(source2) === "object")) {
                s2.dataSource = source2;
                newSettings.dataSource = source2;
            } else if (source1 && ($.type(source1) === "array" || $.type(source1) === "object")) {
                s1.dataSource = source1;
            }
            if (source2 === null) {
                newSettings.dataSource = null;
            }
            return newSettings;
        },

        _mergeIntoWithDataSource: function (o1, o2) {
            var ds1, ds2, setToNull, s1 = null, s2 = null;
            if (o1.dataSource) {
                ds1 = o1.dataSource;
            }
            if (o2.dataSource) {
                ds2 = o2.dataSource;
            }
            setToNull = false;
            if (o2.dataSource === null) {
                setToNull = true;
            }

            if (o1.dataSource) {
                o1.dataSource = null;
            }
            if (o2.dataSource) {
                o2.dataSource = null;
            }

            $.extend(true, o1, o2);
            if (ds1) {
                o1.dataSource = ds1;
            }
            if (ds2) {
                o2.dataSource = ds2;
                o1.dataSource = ds2;
            }
            if (o1.dataSource) {
                if (o1.dataSource && o1.dataSource.settings) {
                    s1 = o1.dataSource.settings;
                }
                if (o2.dataSource && o2.dataSource.settings) {
                    s2 = o2.dataSource.settings;
                }
                o1.dataSource.settings = this._mergeDataSourceSettings(s1, s2);
            }
        },

        _mergeIntoNewWithDataSource: function (o1, o2) {
            var ds1, ds2, setToNull, newObj, s1 = null, s2 = null;
            if (o1.dataSource) {
                ds1 = o1.dataSource;
            }
            if (o2.dataSource) {
                ds2 = o2.dataSource;
            }
            setToNull = false;
            if (o2.dataSource === null) {
                setToNull = true;
            }

            if (o1.dataSource) {
                o1.dataSource = null;
            }
            if (o2.dataSource) {
                o2.dataSource = null;
            }

            newObj = $.extend(true, {}, o1, o2);

            if (ds1) {
                o1.dataSource = ds1;
                newObj.dataSource = ds1;
            }
            if (ds2) {
                o2.dataSource = ds2;
                newObj.dataSource = ds2;
            }
            if (newObj.dataSource) {
                if (o1.dataSource && o1.dataSource.settings) {
                    s1 = o1.dataSource.settings;
                }
                if (o2.dataSource && o2.dataSource.settings) {
                    s2 = o2.dataSource.settings;
                }
                newObj.dataSource.settings = this._mergeDataSourceSettings(s1, s2);
            }

            return newObj;
        },

        _addTooltip: function (widget, series, clss) {
            if (typeof widget._chart.toolTip === "function") {
                if (widget._tooltip[ widget.id() ] === undefined) {
                    widget._tooltip[ widget.id() ] =
                    $("<div id=\"" + widget.id() + "_tooltip\" class=\"" + clss + "\"></div>");
                }
                this._bindTooltipEvents(widget, widget._tooltip[ widget.id() ]);
                widget._chart.toolTip(widget._tooltip[ widget.id() ]);
            } else {
                if (widget._tooltip[ series.name() ] === undefined) {
                    widget._tooltip[ series.name() ] =
                    $("<div id=\"" + series.name() + "_tooltip\" class=\"" + clss + "\"></div>");
                }
                this._bindTooltipEvents(widget, widget._tooltip[ series.name() ]);
                series.toolTip(widget._tooltip[ series.name() ]);
            }
        },

        _removeTooltip: function (widget, series) {
            if (typeof widget._chart.toolTip === "function") {
                if (widget._tooltip[ widget.id() ] !== undefined) {
                    this._removeTooltipEvents(widget, widget._tooltip[ widget.id() ]);
                    delete widget._tooltip[ widget.id() ];
                    widget._chart.toolTip(null);
                }
            } else {
                if (widget._tooltip[ series.name() ] !== undefined) {
                    this._removeTooltipEvents(widget, widget._tooltip[ series.name() ]);
                    delete widget._tooltip[ series.name() ];
                    series.toolTip(null);
                }
            }
        },

        _bindTooltipEvents: function (chart, tooltip) {
            tooltip.updateToolTip = $.ig.Delegate.prototype.combine(tooltip.updateToolTip,
            jQuery.proxy(this._fireToolTipUpdateToolTip, this));
            tooltip.hideToolTip = $.ig.Delegate.prototype.combine(tooltip.hideToolTip,
            jQuery.proxy(this._fireToolTipHideToolTip, this));
        },

        _removeTooltipEvents: function (chart, tooltip) {
            delete tooltip.updateToolTip;
            delete tooltip.hideToolTip;
        },

        _bindMapEvents: function (chart) {
            chart.seriesCursorMouseMove = $.ig.Delegate.prototype.combine(
            chart.seriesCursorMouseMove, jQuery.proxy(this._fireChartSeriesCursorMouseMove, this));
            chart.seriesMouseLeftButtonDown = $.ig.Delegate.prototype.combine(
            chart.seriesMouseLeftButtonDown,
            jQuery.proxy(this._fireChartSeriesMouseLeftButtonDown, this));
            chart.seriesMouseLeftButtonUp = $.ig.Delegate.prototype.combine(
            chart.seriesMouseLeftButtonUp,
            jQuery.proxy(this._fireChartSeriesMouseLeftButtonUp, this));
            chart.seriesMouseMove = $.ig.Delegate.prototype.combine(
            chart.seriesMouseMove, jQuery.proxy(this._fireChartSeriesMouseMove, this));
            chart.seriesMouseEnter = $.ig.Delegate.prototype.combine(
            chart.seriesMouseEnter, jQuery.proxy(this._fireChartSeriesMouseEnter, this));
            chart.seriesMouseLeave = $.ig.Delegate.prototype.combine(
            chart.seriesMouseLeave, jQuery.proxy(this._fireChartSeriesMouseLeave, this));
            chart.windowRectChanged = $.ig.Delegate.prototype.combine(
            chart.windowRectChanged, jQuery.proxy(this._fireChartWindowRectChanged, this));
            chart.actualWindowRectChanged =
            $.ig.Delegate.prototype.combine(chart.actualWindowRectChanged,
            jQuery.proxy(this._fireChartActualWindowRectChanged, this));
            chart.gridAreaRectChanged = $.ig.Delegate.prototype.combine(
            chart.gridAreaRectChanged, jQuery.proxy(this._fireChartGridAreaRectChanged, this));
            chart.refreshCompleted = $.ig.Delegate.prototype.combine(
            chart.refreshCompleted, jQuery.proxy(this._fireChartRefreshCompleted, this));
            chart.imageTilesReady = $.ig.Delegate.prototype.combine(
            chart.imageTilesReady, jQuery.proxy(this._fireChartImageTilesReady, this));
			chart.notifyCrosshairUpdate = $.ig.Delegate.prototype.combine(
            chart.notifyCrosshairUpdate, jQuery.proxy(this._notifyCrosshairUpdate, this));
        },

		_bindSeriesEvents: function (series) {
			if (series.basedOnColumns !== undefined) {
			    series.basedOnColumns($.ig.Delegate.prototype.combine(series.basedOnColumns(),
                jQuery.proxy(this._fireChartBasedOnColumns, this)));
			}
			if (series.indicator !== undefined) {
			    series.indicator($.ig.Delegate.prototype.combine(series.indicator(),
                jQuery.proxy(this._fireChartIndicator, this)));
			}
			if (series.typical !== undefined) {
			    series.typical = $.ig.Delegate.prototype.combine(
                series.typical, jQuery.proxy(this._fireChartTypical, this));
			}
			if (series.typicalBasedOn !== undefined) {
			    series.typicalBasedOn =
                $.ig.Delegate.prototype.combine(series.typicalBasedOn,
                jQuery.proxy(this._fireChartTypicalBasedOn, this));
			}
			if (series.triangulationStatusChanged !== undefined) {
			    series.triangulationStatusChanged =
                $.ig.Delegate.prototype.combine(series.triangulationStatusChanged,
                jQuery.proxy(this._fireMapTriangulationStatusChanged, this));
			}
			if (series.progressiveLoadStatusChanged !== undefined) {
			    series.progressiveLoadStatusChanged =
                $.ig.Delegate.prototype.combine(series.progressiveLoadStatusChanged,
                jQuery.proxy(this._fireChartProgressiveLoadStatusChanged, this));
			}
            if (series.assigningCategoryStyle !== undefined) {
                series.assigningCategoryStyle =
                $.ig.Delegate.prototype.combine(series.assigningCategoryStyle,
                jQuery.proxy(this._fireChartAssigningCategoryStyle, this));
            }
			if (series.assigningCategoryMarkerStyle !== undefined) {
			    series.assigningCategoryMarkerStyle =
                $.ig.Delegate.prototype.combine(series.assigningCategoryMarkerStyle,
                jQuery.proxy(this._fireChartAssigningCategoryMarkerStyle, this));
			}

            //if (series.scatterMouseOver !== undefined) {
            //	series.scatterMouseOver = $.ig.Delegate.prototype.combine(series.scatterMouseOver, jQuery.proxy(this._fireChart_scatterMouseOver, this));
            //}
		},
		_getWidgetName: function () {
			switch (this.widget.widgetName) {
			case "igPieChart":
			case "igDataChart":
				return "chart";
			case "igMap":
				return "map";
			}
		},
		_fireChartProgressiveLoadStatusChanged: function (sender, evtArgs) {
		    var e = {}, widget = this._getWidgetName(),
            seriesOpt = this.widget._seriesOpt[ sender.name() ];
			e.currentStatus = evtArgs.currentStatus();
			e[ widget ] = this.widget.options;
			e.series = seriesOpt;
            seriesOpt.progressiveStatus = e.currentStatus;
			this.widget._trigger("progressiveLoadStatusChanged", null, e);
		},
		_fireChartScatterMouseOver: function (sender, evtArgs) {
			var e = {}, widget = this._getWidgetName();
			e[ widget ] = this.widget.options;
			e.args = evtArgs;
			e.series = sender;
			this.widget._trigger("scatterMouseOver", null, e);
		},
		_fireChartTypicalBasedOn: function (sender, evtArgs) {
		    var e = {}, widget = this._getWidgetName(),
            seriesOpt = this.widget._seriesOpt[ sender.name() ], i,
            intSeries = this.widget._series[ sender.name() ], self = this;
			e[ widget ] = this.widget.options;
			e.series = seriesOpt;
			e.position = evtArgs.position();
			e.count = evtArgs.count();
			e.getDataSource = function () {
				return self._flattenDataSource(intSeries, evtArgs.dataSource());
			};
			e.basedOn = [];

		    //evtArgs.basedOn();
			e.minimumValue = evtArgs.dataSource().minimumValue();
			e.maximumValue = evtArgs.dataSource().maximumValue();
			this.widget._trigger("typicalBasedOn", null, e);
			if (e.basedOn !== null && e.basedOn.length && e.basedOn.length > 0) {
				for (i = 0; i < e.basedOn.length; i++) {
					evtArgs.basedOn().add(e.basedOn[ i ]);
				}
			}
			if (!isNaN(e.minimum)) {
				evtArgs.dataSource().minimumValue(e.minimumValue);
			}
			if (!isNaN(e.maximum)) {
				evtArgs.dataSource().maximumValue(e.maximumValue);
			}
		},
		_fireChartBasedOnColumns: function (sender, evtArgs) {
		    var e = {}, widget = this._getWidgetName(),
            seriesOpt = this.widget._seriesOpt[ sender.name() ], i,
            intSeries = this.widget._series[ sender.name() ], self = this;
			e[ widget ] = this.widget.options;
			e.series = seriesOpt;
			e.position = evtArgs.position();
			e.count = evtArgs.count();
			e.getDataSource = function () {
				return self._flattenDataSource(intSeries, evtArgs.dataSource());
			};
			e.basedOn = [];

		    //evtArgs.basedOn();
			e.minimumValue = evtArgs.dataSource().minimumValue();
			e.maximumValue = evtArgs.dataSource().maximumValue();
			this.widget._trigger("basedOn", null, e);
			if (e.basedOn !== null && e.basedOn.length && e.basedOn.length > 0) {
				for (i = 0; i < e.basedOn.length; i++) {
					evtArgs.basedOn().add(e.basedOn[ i ]);
				}
			}
			if (!isNaN(e.minimum)) {
				evtArgs.dataSource().minimumValue(e.minimumValue);
			}
			if (!isNaN(e.maximum)) {
				evtArgs.dataSource().maximumValue(e.maximumValue);
			}
		},
		_fireChartIndicator: function (sender, evtArgs) {
		    var e = {}, widget = this._getWidgetName(),
            seriesOpt = this.widget._seriesOpt[ sender.name() ],
            intSeries = this.widget._series[ sender.name() ], self = this;
			e[ widget ] = this.widget.options;
			e.series = seriesOpt;
			e.position = evtArgs.position();
			e.count = evtArgs.count();
			e.getDataSource = function () {
				return self._flattenDataSource(intSeries, evtArgs.dataSource());
			};
			e.basedOn = evtArgs.basedOn();
			e.minimumValue = evtArgs.dataSource().minimumValue();
			e.maximumValue = evtArgs.dataSource().maximumValue();
			this.widget._trigger("indicator", null, e);
			if (!isNaN(e.minimum)) {
				evtArgs.dataSource().minimumValue(e.minimumValue);
			}
			if (!isNaN(e.maximum)) {
				evtArgs.dataSource().maximumValue(e.maximumValue);
			}
		},
		_fireChartTypical: function (sender, evtArgs) {
			var e = {}, widget = this._getWidgetName(), seriesOpt = this.widget._seriesOpt[ sender.name() ],
				intSeries = this.widget._series[ sender.name() ], self = this,
				typicalRet, tBasedOn;
            e[ widget ] = this.widget.options;
            e.series = seriesOpt;
            e.position = evtArgs.position();
            e.count = evtArgs.count();
            e.getDataSource = function () {
                var ds = self._flattenDataSource(intSeries, evtArgs.dataSource());
				return ds;
            };
            e.basedOn = evtArgs.basedOn();
            e.minimumValue = evtArgs.dataSource().minimumValue();
            e.maximumValue = evtArgs.dataSource().maximumValue();
            e.typicalColumn = [];
			tBasedOn = evtArgs.dataSource().typicalColumn().basedOn();
            this.widget._trigger("typical", null, e);
            if (e.typicalColumn !== null && e.typicalColumn.length > 0) {
                typicalRet = new $.ig.List$1(Number, 1, e.typicalColumn);
				evtArgs.dataSource().typicalColumn(new $.ig.CalculatedColumn(1, typicalRet, tBasedOn));
            } else {
                evtArgs.dataSource().typicalColumn(null);
            }

            if (!isNaN(e.minimum)) {
                evtArgs.dataSource().minimumValue(e.minimumValue);
            }
            if (!isNaN(e.maximum)) {
                evtArgs.dataSource().maximumValue(e.maximumValue);
            }
		},
		_flattenHighlightingInfo: function (info) {
			var ret = null;
			if (info === null) {
				return ret;
			}

			ret = {};
			ret.startIndex = info.startIndex();
			ret.endIndex = info.endIndex();
			switch (info.state()) {
				case 0:
					ret.state = "out";
					break;
				case 1:
					ret.state = "in";
					break;
				case 2:
					ret.state = "static";
					break;
			}
			ret.progress = info.progress();
			ret.isMarker = info.isMarker();
			return ret;
		},
		_fireChartAssigningCategoryMarkerStyle: function (sender, evtArgs) {
            var e = {}, widget = this._getWidgetName(),
            seriesOpt = this.widget._seriesOpt[ sender.name() ], inFill,
			inStroke;
            if (!sender.isCustomCategoryMarkerStyleAllowed) {
                return;
            }

			e[ widget ] = this.widget.options;
            e.series = seriesOpt;
            e.startIndex = evtArgs.startIndex();
            e.endIndex = evtArgs.endIndex();
			e.hasDateRange = evtArgs.hasDateRange();
			if (e.hasDateRange) {
				e.startDate = evtArgs.startDate();
				e.endDate = evtArgs.endDate();
			}
            e.getItems = $.proxy(evtArgs.getItems(), sender);
            e.fill = inFill = this._getValueFromBrush(evtArgs.fill());
            e.stroke = inStroke = this._getValueFromBrush(evtArgs.stroke());
            e.opacity = evtArgs.opacity();
			e.highlightingHandled = evtArgs.highlightingHandled();
			e.maxAllSeriesHighlightingProgress = evtArgs.maxAllSeriesHighlightingProgress();
			e.sumAllSeriesHighlightingProgress = evtArgs.sumAllSeriesHighlightingProgress();
			e.highlightingInfo = this._flattenHighlightingInfo(evtArgs.highlightingInfo());
			e.isNegativeShape = evtArgs.isNegativeShape();
			e.isThumbnail = evtArgs.isThumbnail();

            this.widget._trigger("assigningCategoryMarkerStyle", null, e);

            if (e.fill !== inFill) {
                evtArgs.fill(this._createBrushFromValue(e.fill));
            }
            if (e.stroke !== inStroke) {
                evtArgs.stroke(this._createBrushFromValue(e.stroke));
            }
            evtArgs.opacity(e.opacity);
			evtArgs.highlightingHandled(e.highlightingHandled);
        },
		_fireChartAssigningCategoryStyle: function (sender, evtArgs) {
		    var e = {}, widget = this._getWidgetName(),
            seriesOpt = this.widget._seriesOpt[ sender.name() ], inFill,
			inStroke, inStrokeDashArray, outStrokeDashArray, i;
            if (!sender.isCustomCategoryStyleAllowed) {
                return;
            }

            e[ widget ] = this.widget.options;
            e.series = seriesOpt;
            e.startIndex = evtArgs.startIndex();
            e.endIndex = evtArgs.endIndex();
			e.hasDateRange = evtArgs.hasDateRange();
			if (e.hasDateRange) {
				e.startDate = evtArgs.startDate();
				e.endDate = evtArgs.endDate();
			}
            e.getItems = $.proxy(evtArgs.getItems(), sender);
            e.fill = inFill = this._getValueFromBrush(evtArgs.fill());
            e.stroke = inStroke = this._getValueFromBrush(evtArgs.stroke());
            e.strokeThickness = evtArgs.strokeThickness();
			var sda = evtArgs.strokeDashArray();
			if (sda !== undefined && sda !== null) {
				e.strokeDashArray = inStrokeDashArray = sda.toArray();
            }
            e.strokeDashCap = evtArgs.strokeDashCap();
            e.radiusX = evtArgs.radiusX();
            e.radiusY = evtArgs.radiusY();
			e.opacity = evtArgs.opacity();
			e.highlightingHandled = evtArgs.highlightingHandled();
			e.maxAllSeriesHighlightingProgress = evtArgs.maxAllSeriesHighlightingProgress();
			e.sumAllSeriesHighlightingProgress = evtArgs.sumAllSeriesHighlightingProgress();
			e.highlightingInfo = this._flattenHighlightingInfo(evtArgs.highlightingInfo());
			e.isNegativeShape = evtArgs.isNegativeShape();
			e.isThumbnail = evtArgs.isThumbnail();

            this.widget._trigger("assigningCategoryStyle", null, e);

            if (e.fill !== inFill) {
                evtArgs.fill(this._createBrushFromValue(e.fill));
            }
            if (e.stroke !== inStroke) {
                evtArgs.stroke(this._createBrushFromValue(e.stroke));
            }
            evtArgs.strokeThickness(e.strokeThickness);
            if (e.strokeDashArray !== inStrokeDashArray) {
                outStrokeDashArray = new $.ig.DoubleCollection();
                for (i = 0; i < e.strokeDashArray.length; i++) {
                    outStrokeDashArray.add(e.strokeDashArray[ i ]);
                }
                evtArgs.strokeDashArray(outStrokeDashArray);
            }
            evtArgs.strokeDashCap(e.strokeDashCap);
            evtArgs.radiusX(e.radiusX);
            evtArgs.radiusY(e.radiusY);
			evtArgs.opacity(e.opacity);
			evtArgs.highlightingHandled(e.highlightingHandled);
		},
		_flattenDataSource: function (series, ds) {
			var ret = {},
				openColumn = series.getOpenColumnAsArray(),
				highColumn = series.getHighColumnAsArray(),
				lowColumn = series.getLowColumnAsArray(),
				closeColumn = series.getCloseColumnAsArray(),
				volumeColumn = series.getVolumeColumnAsArray();

			ret.indicatorColumn = ds.indicatorColumn().asArrayList();
			ret.openColumn = openColumn;
			ret.highColumn = highColumn;
			ret.lowColumn = lowColumn;
			ret.closeColumn = closeColumn;
			ret.volumeColumn = volumeColumn;
			return ret;
		},
		_fireMapTriangulationStatusChanged: function (sender, evtArgs) {
		    var e = {}, widget = this._getWidgetName(),
            seriesOpt = this.widget._seriesOpt[ sender.name() ];
			e.currentStatus = evtArgs.currentStatus();
			e[ widget ] = this.widget.options;
			e.series = seriesOpt;
			this.widget._trigger("triangulationStatusChanged", null, e);
		},
        _bindPieChartEvents: function (chart) {
            chart.sliceClick = $.ig.Delegate.prototype.combine(chart.sliceClick,
            jQuery.proxy(this._firePieChartSliceClick, this));
            chart.labelClick = $.ig.Delegate.prototype.combine(chart.labelClick,
            jQuery.proxy(this._firePieChartLabelClick, this));
        },

		_firePieChartLabelClick: function (sender, evtArgs) {
		    var e = {};
		    e.item = evtArgs.slice().dataContext();
		    e.allowSliceClick = evtArgs.allowSliceClick();
		    this.widget._trigger("labelClick", null, e);
		    evtArgs.allowSliceClick(e.allowSliceClick);
		},

        _firePieChartSliceClick: function (sender, evtArgs) {
            var e = {}, isExploded, isSelected;
            e.slice = {};
            e.slice.item = evtArgs.slice().dataContext();
            isExploded = evtArgs.slice().isExploded();
            isSelected = evtArgs.slice().isSelected();
            e.slice.isExploded = isExploded;
            e.slice.isSelected = isSelected;
            e.chart = this.widget.options;
            e.originalEvent = evtArgs.originalEvent();
            this.widget._trigger("sliceClick", null, e);
            if (e.slice.isExploded !== isExploded) {
                evtArgs.slice().isExploded(e.slice.isExploded);
            }
            if (e.slice.isSelected !== isSelected) {
                evtArgs.slice().isSelected(e.slice.isSelected);
            }
        },
        _bindDataChartEvents: function (chart) {
            chart.seriesCursorMouseMove =
            $.ig.Delegate.prototype.combine(chart.seriesCursorMouseMove,
            jQuery.proxy(this._fireChartSeriesCursorMouseMove, this));
            chart.seriesMouseLeftButtonDown =
            $.ig.Delegate.prototype.combine(chart.seriesMouseLeftButtonDown,
            jQuery.proxy(this._fireChartSeriesMouseLeftButtonDown, this));
            chart.seriesMouseLeftButtonUp =
            $.ig.Delegate.prototype.combine(chart.seriesMouseLeftButtonUp,
            jQuery.proxy(this._fireChartSeriesMouseLeftButtonUp, this));
            chart.seriesMouseMove = $.ig.Delegate.prototype.combine(chart.seriesMouseMove,
            jQuery.proxy(this._fireChartSeriesMouseMove, this));
            chart.seriesMouseEnter = $.ig.Delegate.prototype.combine(chart.seriesMouseEnter,
            jQuery.proxy(this._fireChartSeriesMouseEnter, this));
            chart.seriesMouseLeave = $.ig.Delegate.prototype.combine(chart.seriesMouseLeave,
            jQuery.proxy(this._fireChartSeriesMouseLeave, this));
            chart.windowRectChanged = $.ig.Delegate.prototype.combine(chart.windowRectChanged,
            jQuery.proxy(this._fireChartWindowRectChanged, this));
            chart.actualWindowRectChanged =
            $.ig.Delegate.prototype.combine(chart.actualWindowRectChanged,
            jQuery.proxy(this._fireChartActualWindowRectChanged, this));
            chart.gridAreaRectChanged = $.ig.Delegate.prototype.combine(chart.gridAreaRectChanged,
            jQuery.proxy(this._fireChartGridAreaRectChanged, this));
            chart.refreshCompleted = $.ig.Delegate.prototype.combine(chart.refreshCompleted,
            jQuery.proxy(this._fireChartRefreshCompleted, this));
            chart.axisRangeChanged =
            $.ig.Delegate.prototype.combine(chart.axisRangeChanged,
            jQuery.proxy(this._fireChartAxisRangeChanged, this));
            chart.notifyCrosshairUpdate =
            $.ig.Delegate.prototype.combine(chart.notifyCrosshairUpdate,
            jQuery.proxy(this._notifyCrosshairUpdate, this));
        },
        _getSeriesOpt: function (evtArgs) {
            var widget = this.widget, parentSeries, logicalSeries;

            if (!widget._seriesOpt) {
					return widget.options;
				}

				if (widget._seriesOpt[ evtArgs.series().name() ]) {
					return widget._seriesOpt[ evtArgs.series().name() ];
                }

                if (!evtArgs.series().parentSeries) {
					return widget.options;
				}

				parentSeries = evtArgs.series().parentSeries();
				logicalSeries = evtArgs.series().logicalSeriesLink();

				if (widget._seriesSubOpt[ parentSeries.name() ][ logicalSeries.name() ]) {
					return widget._seriesSubOpt[ parentSeries.name() ][ logicalSeries.name() ];
				}

				return widget.options;
        },
        _getChartEvt: function (evtArgs) {
            var e = {}, seriesOpt = this._getSeriesOpt(evtArgs), pos,
            widget = this._getWidgetName(), intSeries = null;

            e[ widget ] = this.widget.options;
            e.series = seriesOpt;
            if (e.series && !e.series.title) {
                e.series.title = "Series Title";
            }
            e.item = evtArgs.item();

            if (evtArgs.series && evtArgs.series() !== null) {
                intSeries = evtArgs.series();
            }
            if (intSeries !== null && intSeries.hostedSeries && intSeries.hostedSeries() !== null) {
                intSeries = intSeries.hostedSeries();
            }

            if (evtArgs.actualItemBrush && evtArgs.actualItemBrush() !== null) {
                e.actualItemBrush = this._getValueFromBrush(evtArgs.actualItemBrush());
            } else if (intSeries !== null && intSeries.actualMarkerBrush &&
                intSeries.actualMarkerBrush() !== null) {
                e.actualItemBrush = this._getValueFromBrush(intSeries.actualMarkerBrush());
            }
            if (intSeries !== null && intSeries.actualBrush && intSeries.actualBrush() !== null) {
                e.actualSeriesBrush = this._getValueFromBrush(intSeries.actualBrush());
            }

            if (evtArgs.originalEvent && evtArgs.originalEvent() !== null &&
                evtArgs.originalEvent().position && evtArgs.originalEvent().position() !== null) {
                pos = evtArgs.originalEvent().position();
                e.positionX = pos.__x;
                e.positionY = pos.__y;
            }
            return e;
        },
        _notifyCrosshairUpdate: function () {
			if (this.widget._chart &&
				this.widget._chart.crosshairPoint) {
				this.widget.options.crosshairPoint = {
					x: this.widget._chart.crosshairPoint().__x,
					y: this.widget._chart.crosshairPoint().__y
				};
			}
		},
        _fireChartSeriesCursorMouseMove: function (sender, evtArgs) {
            var e = this._getChartEvt(evtArgs);
            this.widget._trigger("seriesCursorMouseMove", null, e);
        },
        _fireChartSeriesMouseLeftButtonDown: function (sender, evtArgs) {
            var e = this._getChartEvt(evtArgs);
            this.widget._trigger("seriesMouseLeftButtonDown", null, e);
        },
        _fireChartSeriesMouseLeftButtonUp: function (sender, evtArgs) {
            var e = this._getChartEvt(evtArgs);
            this.widget._trigger("seriesMouseLeftButtonUp", null, e);
        },
        _fireChartSeriesMouseMove: function (sender, evtArgs) {
            var e = this._getChartEvt(evtArgs);
            this.widget._trigger("seriesMouseMove", null, e);
        },
        _fireChartSeriesMouseEnter: function (sender, evtArgs) {
            var e = this._getChartEvt(evtArgs);
            this.widget._trigger("seriesMouseEnter", null, e);
        },
        _fireChartSeriesMouseLeave: function (sender, evtArgs) {
            var e = this._getChartEvt(evtArgs);
            this.widget._trigger("seriesMouseLeave", null, e);
        },
        _fireChartWindowRectChanged: function (sender, evtArgs) {
            var e = {}, oldRect = evtArgs.oldRect(), newRect = evtArgs.newRect(),
            widget = this._getWidgetName();
            if (oldRect) {
                e.oldTop = oldRect.top();
                e.oldLeft = oldRect.left();
                e.oldWidth = oldRect.width();
                e.oldHeight = oldRect.height();
            }

            e.newTop = newRect.top();
            e.newLeft = newRect.left();
            e.newWidth = newRect.width();
            e.newHeight = newRect.height();
            e[ widget ] = this.widget.options;

            this.widget.options.windowRect = {
                top: e.newTop,
                left: e.newLeft,
                width: e.newWidth,
                height: e.newHeight
            };

            this.widget._trigger("windowRectChanged", null, e);
        },
        _fireChartActualWindowRectChanged: function (sender, evtArgs) {
            var e = {}, oldRect = evtArgs.oldRect(),
            newRect = evtArgs.newRect(), widget = this._getWidgetName();
            if (oldRect) {
                e.oldTop = oldRect.top();
                e.oldLeft = oldRect.left();
                e.oldWidth = oldRect.width();
                e.oldHeight = oldRect.height();
            }

            e.newTop = newRect.top();
            e.newLeft = newRect.left();
            e.newWidth = newRect.width();
            e.newHeight = newRect.height();
            e[ widget ] = this.widget.options;

            this.widget.options.actualWindowRect = {
                top: e.newTop,
                left: e.newLeft,
                width: e.newWidth,
                height: e.newHeight
            };

            this.widget._trigger("actualWindowRectChanged", null, e);
        },
        _fireChartGridAreaRectChanged: function (sender, evtArgs) {
            var e = {}, oldRect =
            evtArgs.oldRect(), newRect = evtArgs.newRect(), widget = this._getWidgetName();
            if (oldRect) {
                e.oldTop = oldRect.top();
                e.oldLeft = oldRect.left();
                e.oldWidth = oldRect.width();
                e.oldHeight = oldRect.height();
            }

            e.newTop = newRect.top();
            e.newLeft = newRect.left();
            e.newWidth = newRect.width();
            e.newHeight = newRect.height();
            e[ widget ] = this.widget.options;

            this.widget.options.gridAreaRect = {
                top: e.newTop,
                left: e.newLeft,
                width: e.newWidth,
                height: e.newHeight
            };

            this.widget._trigger("gridAreaRectChanged", null, e);
        },
        _fireChartRefreshCompleted: function () {
            var e = {}, widget = this._getWidgetName();
            e[ widget ] = this.widget.options;

            this.widget._trigger("refreshCompleted", null, e);
        },
		_fireChartImageTilesReady: function () {
            var e = {}, widget = this._getWidgetName();
            e[ widget ] = this.widget.options;

            this.widget._trigger("imageTilesReady", null, e);
        },
        _fireChartAxisRangeChanged: function (sender, evtArgs) {
            var e = {};
            e.chart = this.widget.options;
            e.axis = this.widget._axisOpt[ evtArgs.axis().name() ];
            e.oldMinimumValue = evtArgs.oldMinimumValue();
            e.oldMaximumValue = evtArgs.oldMaximumValue();
            e.newMinimumValue = evtArgs.minimumValue();
            e.newMaximumValue = evtArgs.maximumValue();

            this.widget._trigger("axisRangeChanged", null, e);
        },
        _fireToolTipUpdateToolTip: function (args) {
            var e = {}, noCancel, template;
            e = this._getChartEvt(args);
            e.tempId = (this.widget._seriesOpt) ? e.series.name : this.widget.id();
            e.element = null;
            if (e.series !== null) {
                e.element = this.widget._tooltip[ e.tempId ];
            }

            noCancel = this.widget._trigger(this.widget.events.tooltipShowing, null, e);

            if (e === null) {
                noCancel = false;
            }
            if (noCancel) {
                template = this.widget._tooltipTemplate;
                if (e.series !== null && this.widget._tooltipTemplates[ e.tempId ] !== undefined) {
                    template = this.widget._tooltipTemplates[ e.tempId ];
                }

                if (template === "default") {
                    if (args.series().view) {
                        template = args.series().view().getDefaultTooltipTemplate();
                        this.widget._tooltipTemplates[ e.tempId ] = template;
						if (args.series() && args.series().isDefaultToolTipSelected) {
							args.series().isDefaultToolTipSelected(true);
						}
                    }
                } else {
					if (args.series() && args.series().isDefaultToolTipSelected) {
						args.series().isDefaultToolTipSelected(false);
					}
				}

                if (template) {
                    this.widget._tooltip[ e.tempId ].children().remove();
                    if (e.item === null) {
                        noCancel = false;
                    }

                    if (noCancel) {
                        this.widget._tooltip[ e.tempId ].html($.ig.tmpl(template, e));
                    }
                }
                if (args.hideOthers) {
                    $.each(this.widget._tooltip, function (i, tip) {
                        tip.hide();
                    });
                }

                if (noCancel) {
                    this.widget._tooltip[ e.tempId ].show();
                    this.widget._trigger(this.widget.events.tooltipShown, null, e);
                }
            }
            return noCancel;
        },
        _fireToolTipHideToolTip: function (args) {
            var e = {}, noCancel;
            e = (this.widget._seriesOpt) ? this._getChartEvt(args) : args;
            e.tempId = (this.widget._seriesOpt) ? e.series.name : this.widget.id();
            e.element = null;
            if (e.series !== null) {
                e.element = this.widget._tooltip[ e.tempId ];
            }

            noCancel = this.widget._trigger(this.widget.events.tooltipHiding, null, e);
            if (noCancel) {
                $.each(this.widget._tooltip, function (i, tip) {
                    tip.hide();
                });
                this.widget._trigger(this.widget.events.tooltipHidden, null, e);
            }
        },
        _destroy: function (widget) {
            var elem = this._chartElement, old = this._oldProp;
            if (elem) {
                delete this._chartElement;
                elem.remove();

                // stop possible timer
                this._setSize(widget._chart);

                // restore original width/height
                elem = widget.element;
                elem.css("width", old.width);
                elem.css("height", old.height);
            }
            widget._axes = null;
            widget._series = null;
            widget._axisOpt = null;
            widget._seriesOpt = null;
            widget._seriesSub = null;
            widget._seriesSubOpt = null;
            widget.dataSources = null;

			$.each(this.widget._tooltip, function (i, tip) {
                 tip.hide();
				 tip.remove();
			});

			widget._tooltip = null;
            widget._tooltipTemplates = null;
        }
    });

    $.ig.SimpleTextMarkerTemplate = $.ig.TextMarkerTemplate || Class.extend({
        requireThis: true,

        settings: {
            padding: 5,
            getText: null,
            backgroundColor: "rgba(255,255,255,.6)",
            borderColor: "rgba(20, 20, 20, .6)",
            borderThickness: 1,
            textColor: "black",
            font: null
        },

        init: function (options) {
            if (options !== undefined) {
                this.settings =
                $.extend(true, {}, $.ig.SimpleTextMarkerTemplate.prototype.settings, options);
            }
            return this;
        },

        getText: function (item, textDelegate) {
            if (textDelegate === null) {
                return "";
            }

            return textDelegate(item);
        },

        measure: function (measureInfo) {
            var s = this.settings, data, text, cont = measureInfo.context, height, width;

            if (s.font !== null) {
                cont.font = s.font;
            }
            if (s.textColor !== null) {
                cont.fillStyle = s.textColor;
            }

            data = measureInfo.data;

            text = "null";
            if (data.item() !== null) {
                text = this.getText(data.item(), s.getText);
            }
            height = cont.measureText("M").width;
            width = cont.measureText(text).width;
            measureInfo.width = width + s.padding * 2.0;
            measureInfo.height = height + s.padding * 2.0;
        },

        render: function (renderInfo) {
            var s = this.settings, ctx =
            renderInfo.context, data, text, halfWidth, halfHeight, x, y;

            if (renderInfo.isHitTestRender) {
                ctx.globalAlpha = 1;
                ctx.fillStyle = renderInfo.data.actualItemBrush().fill();
            } else {
                ctx.fillStyle = s.backgroundColor;
                ctx.strokeStyle = s.borderColor;
                ctx.lineWidth = s.borderThickness;
            }

            data = renderInfo.data;
            text = this.getText(data.item(), s.getText);

            halfWidth = renderInfo.availableWidth / 2.0;
            halfHeight = renderInfo.availableHeight / 2.0;
            x = renderInfo.xPosition - halfWidth;
            y = renderInfo.yPosition - halfHeight;

            if (renderInfo.isHitTestRender) {
                ctx.fillRect(x, y, renderInfo.availableWidth, renderInfo.availableHeight);
            } else {
                ctx.fillRect(x, y, renderInfo.availableWidth, renderInfo.availableHeight);
                ctx.strokeRect(x, y, renderInfo.availableWidth, renderInfo.availableHeight);

                ctx.fillStyle = s.textColor;
                ctx.textBaseline = "top";
                ctx.fillText(text, x + s.padding, y + s.padding);
            }
        }

    });
}(jQuery));



