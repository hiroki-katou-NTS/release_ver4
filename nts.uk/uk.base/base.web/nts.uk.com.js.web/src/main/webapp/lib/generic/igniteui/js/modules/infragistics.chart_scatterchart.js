/*!@license
* Infragistics.Web.ClientUI infragistics.chart_scatterchart.js 16.1.20161.2145
*
* Copyright (c) 2011-2016 Infragistics Inc.
*
* http://www.infragistics.com/
*
* Depends:
*     jquery-1.4.4.js
*     jquery.ui.core.js
*     jquery.ui.widget.js
*     infragistics.util.js
*/

(function ($) {
$.ig = $.ig || {};
var $$t = {}
$.ig.$currDefinitions = $$t;
$.ig.util.bulkDefine(["AbstractEnumerable:a", 
"Object:b", 
"Type:c", 
"Boolean:d", 
"ValueType:e", 
"Void:f", 
"IConvertible:g", 
"IFormatProvider:h", 
"Number:i", 
"String:j", 
"IComparable:k", 
"Number:l", 
"IComparable$1:m", 
"IEquatable$1:n", 
"Number:o", 
"Number:p", 
"Number:q", 
"NumberStyles:r", 
"Enum:s", 
"Array:t", 
"IList:u", 
"ICollection:v", 
"IEnumerable:w", 
"IEnumerator:x", 
"NotSupportedException:y", 
"Error:z", 
"Number:aa", 
"String:ab", 
"StringComparison:ac", 
"RegExp:ad", 
"CultureInfo:ae", 
"DateTimeFormatInfo:af", 
"Calendar:ag", 
"Date:ah", 
"Number:ai", 
"DayOfWeek:aj", 
"DateTimeKind:ak", 
"CalendarWeekRule:al", 
"NumberFormatInfo:am", 
"CompareInfo:an", 
"CompareOptions:ao", 
"IEnumerable$1:ap", 
"IEnumerator$1:aq", 
"IDisposable:ar", 
"StringSplitOptions:as", 
"Number:at", 
"Number:au", 
"Number:av", 
"Number:aw", 
"Number:ax", 
"Number:ay", 
"Assembly:az", 
"Stream:a0", 
"SeekOrigin:a1", 
"RuntimeTypeHandle:a2", 
"MethodInfo:a3", 
"MethodBase:a4", 
"MemberInfo:a5", 
"ParameterInfo:a6", 
"TypeCode:a7", 
"ConstructorInfo:a8", 
"PropertyInfo:a9", 
"Func$1:ba", 
"MulticastDelegate:bb", 
"IntPtr:bc", 
"AbstractEnumerator:bd", 
"Array:bo", 
"GenericEnumerable$1:cj", 
"GenericEnumerator$1:ck", 
"Monitor:es"]);


} (jQuery));



(function ($) {
$.ig = $.ig || {};
var $$t = {}
$.ig.$currDefinitions = $$t;
$.ig.util.bulkDefine(["Object:b", 
"Type:c", 
"Boolean:d", 
"ValueType:e", 
"Void:f", 
"IConvertible:g", 
"IFormatProvider:h", 
"Number:i", 
"String:j", 
"IComparable:k", 
"Number:l", 
"IComparable$1:m", 
"IEquatable$1:n", 
"Number:o", 
"Number:p", 
"Number:q", 
"NumberStyles:r", 
"Enum:s", 
"Array:t", 
"IList:u", 
"ICollection:v", 
"IEnumerable:w", 
"IEnumerator:x", 
"NotSupportedException:y", 
"Error:z", 
"Number:aa", 
"String:ab", 
"StringComparison:ac", 
"RegExp:ad", 
"CultureInfo:ae", 
"DateTimeFormatInfo:af", 
"Calendar:ag", 
"Date:ah", 
"Number:ai", 
"DayOfWeek:aj", 
"DateTimeKind:ak", 
"CalendarWeekRule:al", 
"NumberFormatInfo:am", 
"CompareInfo:an", 
"CompareOptions:ao", 
"IEnumerable$1:ap", 
"IEnumerator$1:aq", 
"IDisposable:ar", 
"StringSplitOptions:as", 
"Number:at", 
"Number:au", 
"Number:av", 
"Number:aw", 
"Number:ax", 
"Number:ay", 
"Assembly:az", 
"Stream:a0", 
"SeekOrigin:a1", 
"RuntimeTypeHandle:a2", 
"MethodInfo:a3", 
"MethodBase:a4", 
"MemberInfo:a5", 
"ParameterInfo:a6", 
"TypeCode:a7", 
"ConstructorInfo:a8", 
"PropertyInfo:a9", 
"Rect:ba", 
"Size:bb", 
"Point:bc", 
"Math:bd", 
"MulticastDelegate:bg", 
"IntPtr:bh", 
"Script:cc", 
"Action$1:ch", 
"EventArgs:cl", 
"UIElement:cm", 
"DependencyObject:cn", 
"Dictionary:co", 
"DependencyProperty:cp", 
"PropertyMetadata:cq", 
"PropertyChangedCallback:cr", 
"DependencyPropertyChangedEventArgs:cs", 
"DependencyPropertiesCollection:ct", 
"UnsetValue:cu", 
"Binding:cv", 
"PropertyPath:cw", 
"Transform:cx", 
"List$1:cz", 
"IList$1:c0", 
"ICollection$1:c1", 
"IArray:c2", 
"IArrayList:c3", 
"Array:c4", 
"CompareCallback:c5", 
"Func$3:c6", 
"Comparer$1:c7", 
"IComparer:c8", 
"IComparer$1:c9", 
"DefaultComparer$1:da", 
"Comparison$1:db", 
"ReadOnlyCollection$1:dc", 
"Predicate$1:dd", 
"NotImplementedException:de", 
"IFastItemColumnPropertyName:du", 
"IFastItemColumn$1:dv", 
"EventHandler$1:dy", 
"FrameworkElement:eu", 
"Visibility:ev", 
"Style:ew", 
"GeometryUtil:gf", 
"Tuple$2:gg", 
"RectUtil:g2", 
"MathUtil:g3", 
"RuntimeHelpers:g4", 
"RuntimeFieldHandle:g5", 
"ISmartPlaceable:hu", 
"SmartPosition:hv", 
"SmartPlaceableWrapper$1:hw", 
"SmartPlacer:hx", 
"Func$1:ib", 
"ErrorBarCalculatorReference:it", 
"ErrorBarCalculatorType:iu", 
"IErrorBarCalculator:iv", 
"AbstractEnumerable:kp", 
"AbstractEnumerator:kq", 
"GenericEnumerable$1:kr", 
"GenericEnumerator$1:ks"]);


$.ig.util.defType('ErrorBarCalculatorType', 'Enum', {
	init: function (v) {
		this._v = v;
	},
	$getName: function (v) {
		switch (v) {
			case 0: return "Fixed";
			case 1: return "Percentage";
			case 2: return "Data";
			case 3: return "StandardDeviation";
			case 4: return "StandardError";
			default: return v.toString();
		}
	},
	$value: function () {
		return this._v;
	},
	$type: new $.ig.Type('ErrorBarCalculatorType', $.ig.Enum.prototype.$type)
}, true);

$.ig.util.defType('ErrorBarCalculatorReference', 'Enum', {
	init: function (v) {
		this._v = v;
	},
	$getName: function (v) {
		switch (v) {
			case 0: return "X";
			case 1: return "Y";
			default: return v.toString();
		}
	},
	$value: function () {
		return this._v;
	},
	$type: new $.ig.Type('ErrorBarCalculatorReference', $.ig.Enum.prototype.$type)
}, true);

$.ig.util.defType('SmartPosition', 'Enum', {
	init: function (v) {
		this._v = v;
	},
	$getName: function (v) {
		switch (v) {
			case 0: return "LeftTop";
			case 1: return "CenterTop";
			case 2: return "RightTop";
			case 3: return "LeftCenter";
			case 4: return "CenterCenter";
			case 5: return "RightCenter";
			case 6: return "LeftBottom";
			case 7: return "CenterBottom";
			case 8: return "RightBottom";
			default: return v.toString();
		}
	},
	$value: function () {
		return this._v;
	},
	$type: new $.ig.Type('SmartPosition', $.ig.Enum.prototype.$type)
}, true);

$.ig.util.defType('ISmartPlaceable', 'Object', {
	$type: new $.ig.Type('ISmartPlaceable', null)
}, true);

$.ig.util.defType('SmartPlaceableWrapper$1', 'Object', {
	$t: null,
	init: function ($t) {
		this.$t = $t;
		this.$type = this.$type.specialize(this.$t);
		this._elementDesiredSize = new $.ig.Size();
		$.ig.Object.prototype.init.call(this);
		this.noWiggle(false);
	},
	_noWiggle: false,
	noWiggle: function (value) {
		if (arguments.length === 1) {
			this._noWiggle = value;
			return value;
		} else {
			return this._noWiggle;
		}
	}
	,
	__element: null,
	element: function (value) {
		if (arguments.length === 1) {
			this.__element = value;
			return value;
		} else {
			return this.__element;
		}
	}
	,
	_elementLocationResult: null,
	elementLocationResult: function (value) {
		if (arguments.length === 1) {
			this._elementLocationResult = value;
			return value;
		} else {
			return this._elementLocationResult;
		}
	}
	,
	_originalLocation: null,
	originalLocation: function (value) {
		if (arguments.length === 1) {
			this._originalLocation = value;
			return value;
		} else {
			return this._originalLocation;
		}
	}
	,
	getSmartPositions: function () {
		if (this.noWiggle()) {
			return this.$type.getStaticFields($.ig.SmartPlaceableWrapper$1.prototype.$type)._smartPositionDefault;
		} else {
			return this.$type.getStaticFields($.ig.SmartPlaceableWrapper$1.prototype.$type)._smartPositions;
		}
	}
	,
	_elementDesiredSize: null,
	elementDesiredSize: function (value) {
		if (arguments.length === 1) {
			this._elementDesiredSize = value;
			return value;
		} else {
			return this._elementDesiredSize;
		}
	}
	,
	getSmartElementSize: function () {
		return this.elementDesiredSize();
	}
	,
	getSmartBounds: function (position) {
		var s = this.getSmartElementSize();
		var w = s.width();
		var h = s.height();
		var d;
		{
			d = this.getOffset(position, w, h);
		}
		return new $.ig.Rect(0, this.originalLocation().__x + d.__x, this.originalLocation().__y + d.__y, w, h);
	}
	,
	opacity: function (value) {
		if (arguments.length === 1) {
			this.element().__opacity = value;
			return value;
		} else {
			return this.element().__opacity;
		}
	}
	,
	smartPosition: function (value) {
		if (arguments.length === 1) {
			this._smartPosition = value;
			var s = this.getSmartElementSize();
			var h = s.height();
			var w = s.width();
			var d;
			{
				d = this.getOffset(this._smartPosition, w, h);
			}
			this.elementLocationResult({ __x: this.originalLocation().__x + d.__x, __y: this.originalLocation().__y + d.__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			return value;
		} else {
			return this._smartPosition;
		}
	}
	,
	_smartPosition: 0,
	getOffset: function (position, w, h) {
		var c = 0.25;
		switch (position) {
			case $.ig.SmartPosition.prototype.leftTop: return { __x: -w * c, __y: -h * c, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			case $.ig.SmartPosition.prototype.centerTop: return { __x: 0, __y: -h * c, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			case $.ig.SmartPosition.prototype.rightTop: return { __x: w * c, __y: -h * c, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			case $.ig.SmartPosition.prototype.leftCenter: return { __x: -w * c, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			case $.ig.SmartPosition.prototype.centerCenter: return { __x: 0, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			case $.ig.SmartPosition.prototype.rightCenter: return { __x: w * c, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			case $.ig.SmartPosition.prototype.leftBottom: return { __x: -w * c, __y: h * c, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			case $.ig.SmartPosition.prototype.centerBottom: return { __x: 0, __y: h * c, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			default: return { __x: w * c, __y: h * c, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		}
	}
	,
	$type: new $.ig.Type('SmartPlaceableWrapper$1', $.ig.Object.prototype.$type, [$.ig.ISmartPlaceable.prototype.$type], function () {
		this._smartPositionDefault = [ $.ig.SmartPosition.prototype.centerCenter ];
		this._smartPositions = [ $.ig.SmartPosition.prototype.centerCenter, $.ig.SmartPosition.prototype.rightCenter, $.ig.SmartPosition.prototype.rightTop, $.ig.SmartPosition.prototype.centerTop, $.ig.SmartPosition.prototype.rightBottom, $.ig.SmartPosition.prototype.centerBottom, $.ig.SmartPosition.prototype.leftTop, $.ig.SmartPosition.prototype.leftCenter, $.ig.SmartPosition.prototype.leftBottom ];
	})
}, true);

$.ig.util.defType('SmartPlacer', 'Object', {
	init: function () {
		this._placed = new $.ig.List$1($.ig.Rect.prototype.$type, 0);
		$.ig.Object.prototype.init.call(this);
		this.bounds($.ig.Rect.prototype.empty());
		this.overlap(0.3);
		this.fade(2);
	},
	_bounds: null,
	bounds: function (value) {
		if (arguments.length === 1) {
			this._bounds = value;
			return value;
		} else {
			return this._bounds;
		}
	}
	,
	_overlap: 0,
	overlap: function (value) {
		if (arguments.length === 1) {
			this._overlap = value;
			return value;
		} else {
			return this._overlap;
		}
	}
	,
	_fade: 0,
	fade: function (value) {
		if (arguments.length === 1) {
			this._fade = value;
			return value;
		} else {
			return this._fade;
		}
	}
	,
	place: function (smartPlaceable) {
		if (smartPlaceable == null) {
			return;
		}
		var minScore = 1.7976931348623157E+308;
		var minBounds = $.ig.Rect.prototype.empty();
		var minPosition = $.ig.SmartPosition.prototype.centerBottom;
		var hasMinPosition = false;
		var $t = smartPlaceable.getSmartPositions();
		for (var i = 0; i < $t.length; i++) {
			var position = $t[i];
			var bounds = smartPlaceable.getSmartBounds(position);
			if (this.bounds().isEmpty() || this.bounds().containsRect(bounds)) {
				var score = 0;
				var en = this._placed.getEnumerator();
				while (en.moveNext()) {
					var rect = en.current();
					score += $.ig.RectUtil.prototype.intersectionArea(bounds, rect);
				}
				if (score == 0) {
					minScore = score;
					minPosition = position;
					minBounds = bounds;
					hasMinPosition = true;
					break;
				}
				if (score < minScore) {
					minScore = score;
					minPosition = position;
					minBounds = bounds;
					hasMinPosition = true;
				}
			}
		}
		var overlap = 0;
		if (hasMinPosition) {
			overlap = minScore / $.ig.RectUtil.prototype.getArea(minBounds);
		}
		if (!hasMinPosition || overlap > this.overlap()) {
			smartPlaceable.opacity(0);
		} else {
			if (minScore > 0) {
				smartPlaceable.opacity(Math.pow(1 - $.ig.MathUtil.prototype.clamp(0, overlap, 1), this.fade()));
			} else {
				smartPlaceable.opacity(1);
			}
			smartPlaceable.smartPosition(minPosition);
			this._placed.add(minBounds);
		}
	}
	,
	_placed: null,
	$type: new $.ig.Type('SmartPlacer', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('IErrorBarCalculator', 'Object', {
	$type: new $.ig.Type('IErrorBarCalculator', null)
}, true);

$.ig.ErrorBarCalculatorType.prototype.fixed = 0;
$.ig.ErrorBarCalculatorType.prototype.percentage = 1;
$.ig.ErrorBarCalculatorType.prototype.data = 2;
$.ig.ErrorBarCalculatorType.prototype.standardDeviation = 3;
$.ig.ErrorBarCalculatorType.prototype.standardError = 4;

$.ig.ErrorBarCalculatorReference.prototype.x = 0;
$.ig.ErrorBarCalculatorReference.prototype.y = 1;

$.ig.SmartPosition.prototype.leftTop = 0;
$.ig.SmartPosition.prototype.centerTop = 1;
$.ig.SmartPosition.prototype.rightTop = 2;
$.ig.SmartPosition.prototype.leftCenter = 3;
$.ig.SmartPosition.prototype.centerCenter = 4;
$.ig.SmartPosition.prototype.rightCenter = 5;
$.ig.SmartPosition.prototype.leftBottom = 6;
$.ig.SmartPosition.prototype.centerBottom = 7;
$.ig.SmartPosition.prototype.rightBottom = 8;

} (jQuery));



(function ($) {
$.ig = $.ig || {};
var $$t = {}
$.ig.$currDefinitions = $$t;
$.ig.util.bulkDefine(["IProvidesViewport:a", 
"Void:b", 
"ValueType:c", 
"Object:d", 
"Type:e", 
"Boolean:f", 
"IConvertible:g", 
"IFormatProvider:h", 
"Number:i", 
"String:j", 
"IComparable:k", 
"Number:l", 
"IComparable$1:m", 
"IEquatable$1:n", 
"Number:o", 
"Number:p", 
"Number:q", 
"NumberStyles:r", 
"Enum:s", 
"Array:t", 
"IList:u", 
"ICollection:v", 
"IEnumerable:w", 
"IEnumerator:x", 
"NotSupportedException:y", 
"Error:z", 
"Number:aa", 
"String:ab", 
"StringComparison:ac", 
"RegExp:ad", 
"CultureInfo:ae", 
"DateTimeFormatInfo:af", 
"Calendar:ag", 
"Date:ah", 
"Number:ai", 
"DayOfWeek:aj", 
"DateTimeKind:ak", 
"CalendarWeekRule:al", 
"NumberFormatInfo:am", 
"CompareInfo:an", 
"CompareOptions:ao", 
"IEnumerable$1:ap", 
"IEnumerator$1:aq", 
"IDisposable:ar", 
"StringSplitOptions:as", 
"Number:at", 
"Number:au", 
"Number:av", 
"Number:aw", 
"Number:ax", 
"Number:ay", 
"Assembly:az", 
"Stream:a0", 
"SeekOrigin:a1", 
"RuntimeTypeHandle:a2", 
"MethodInfo:a3", 
"MethodBase:a4", 
"MemberInfo:a5", 
"ParameterInfo:a6", 
"TypeCode:a7", 
"ConstructorInfo:a8", 
"PropertyInfo:a9", 
"Rect:ba", 
"Size:bb", 
"Point:bc", 
"Math:bd", 
"Series:be", 
"Control:bf", 
"FrameworkElement:bg", 
"UIElement:bh", 
"DependencyObject:bi", 
"Dictionary:bj", 
"DependencyProperty:bk", 
"PropertyMetadata:bl", 
"PropertyChangedCallback:bm", 
"MulticastDelegate:bn", 
"IntPtr:bo", 
"DependencyPropertyChangedEventArgs:bp", 
"DependencyPropertiesCollection:bq", 
"UnsetValue:br", 
"Script:bs", 
"Binding:bt", 
"PropertyPath:bu", 
"Transform:bv", 
"Visibility:bw", 
"Style:bx", 
"Thickness:by", 
"HorizontalAlignment:bz", 
"VerticalAlignment:b0", 
"INotifyPropertyChanged:b1", 
"PropertyChangedEventHandler:b2", 
"PropertyChangedEventArgs:b3", 
"SeriesView:b4", 
"ISchedulableRender:b5", 
"SeriesViewer:b6", 
"SeriesViewerView:b7", 
"CanvasRenderScheduler:b8", 
"List$1:b9", 
"IList$1:ca", 
"ICollection$1:cb", 
"IArray:cc", 
"IArrayList:cd", 
"Array:ce", 
"CompareCallback:cf", 
"Func$3:cg", 
"Action$1:ch", 
"Comparer$1:ci", 
"IComparer:cj", 
"IComparer$1:ck", 
"DefaultComparer$1:cl", 
"Comparison$1:cm", 
"ReadOnlyCollection$1:cn", 
"Predicate$1:co", 
"NotImplementedException:cp", 
"Callback:cq", 
"window:cr", 
"RenderingContext:cs", 
"IRenderer:ct", 
"Rectangle:cu", 
"Shape:cv", 
"Brush:cw", 
"Color:cx", 
"ArgumentException:cy", 
"DoubleCollection:cz", 
"Path:c0", 
"Geometry:c1", 
"GeometryType:c2", 
"TextBlock:c3", 
"Polygon:c4", 
"PointCollection:c5", 
"Polyline:c6", 
"DataTemplateRenderInfo:c7", 
"DataTemplatePassInfo:c8", 
"ContentControl:c9", 
"DataTemplate:da", 
"DataTemplateRenderHandler:db", 
"DataTemplateMeasureHandler:dc", 
"DataTemplateMeasureInfo:dd", 
"DataTemplatePassHandler:de", 
"Line:df", 
"FontInfo:dg", 
"XamOverviewPlusDetailPane:dh", 
"XamOverviewPlusDetailPaneView:di", 
"XamOverviewPlusDetailPaneViewManager:dj", 
"JQueryObject:dk", 
"Element:dl", 
"ElementAttributeCollection:dm", 
"ElementCollection:dn", 
"WebStyle:dp", 
"ElementNodeType:dq", 
"Document:dr", 
"EventListener:ds", 
"IElementEventHandler:dt", 
"ElementEventHandler:du", 
"ElementAttribute:dv", 
"JQueryPosition:dw", 
"JQueryCallback:dx", 
"JQueryEvent:dy", 
"JQueryUICallback:dz", 
"EventProxy:d0", 
"ModifierKeys:d1", 
"Func$2:d2", 
"MouseWheelHandler:d3", 
"Delegate:d4", 
"Interlocked:d5", 
"GestureHandler:d6", 
"ZoomGestureHandler:d7", 
"FlingGestureHandler:d8", 
"ContactHandler:d9", 
"TouchHandler:ea", 
"MouseOverHandler:eb", 
"MouseHandler:ec", 
"KeyHandler:ed", 
"Key:ee", 
"JQuery:ef", 
"JQueryDeferred:eg", 
"JQueryPromise:eh", 
"Action:ei", 
"CanvasViewRenderer:ej", 
"CanvasContext2D:ek", 
"CanvasContext:el", 
"TextMetrics:em", 
"ImageData:en", 
"CanvasElement:eo", 
"Gradient:ep", 
"LinearGradientBrush:eq", 
"GradientStop:er", 
"GeometryGroup:es", 
"GeometryCollection:et", 
"FillRule:eu", 
"PathGeometry:ev", 
"PathFigureCollection:ew", 
"LineGeometry:ex", 
"RectangleGeometry:ey", 
"EllipseGeometry:ez", 
"ArcSegment:e0", 
"PathSegment:e1", 
"PathSegmentType:e2", 
"SweepDirection:e3", 
"PathFigure:e4", 
"PathSegmentCollection:e5", 
"LineSegment:e6", 
"PolyLineSegment:e7", 
"BezierSegment:e8", 
"PolyBezierSegment:e9", 
"GeometryUtil:fa", 
"Tuple$2:fb", 
"TransformGroup:fc", 
"TransformCollection:fd", 
"TranslateTransform:fe", 
"RotateTransform:ff", 
"ScaleTransform:fg", 
"DivElement:fh", 
"BaseDOMEventProxy:fi", 
"DOMEventProxy:fj", 
"MSGesture:fk", 
"MouseEventArgs:fl", 
"EventArgs:fm", 
"DoubleAnimator:fn", 
"EasingFunctionHandler:fo", 
"ImageElement:fp", 
"RectUtil:fq", 
"MathUtil:fr", 
"RuntimeHelpers:fs", 
"RuntimeFieldHandle:ft", 
"PropertyChangedEventArgs$1:fu", 
"InteractionState:fv", 
"OverviewPlusDetailPaneMode:fw", 
"IOverviewPlusDetailControl:fx", 
"EventHandler$1:fy", 
"ArgumentNullException:fz", 
"OverviewPlusDetailViewportHost:f0", 
"XamDataChart:f1", 
"GridMode:f2", 
"BrushCollection:f3", 
"ObservableCollection$1:f4", 
"INotifyCollectionChanged:f5", 
"NotifyCollectionChangedEventHandler:f6", 
"NotifyCollectionChangedEventArgs:f7", 
"NotifyCollectionChangedAction:f8", 
"InterpolationMode:f9", 
"Random:ga", 
"ColorUtil:gb", 
"AxisCollection:gc", 
"XamDataChartView:gd", 
"SeriesViewerViewManager:ge", 
"AxisTitlePosition:gf", 
"PointerTooltipStyle:gg", 
"Dictionary$2:gh", 
"IDictionary$2:gi", 
"IDictionary:gj", 
"KeyValuePair$2:gk", 
"Enumerable:gl", 
"Thread:gm", 
"ThreadStart:gn", 
"IOrderedEnumerable$1:go", 
"SortedList$1:gp", 
"IEqualityComparer$1:gq", 
"EqualityComparer$1:gr", 
"IEqualityComparer:gs", 
"DefaultEqualityComparer$1:gt", 
"InvalidOperationException:gu", 
"CanvasGestureDOMEventProxy:gv", 
"TouchPointInfo:gw", 
"DOMExecutionContext:gx", 
"IExecutionContext:gy", 
"ExecutionContextExecuteCallback:gz", 
"TouchGestureRecognizer:g0", 
"TouchGestureState:g1", 
"TouchVelocityTracker:g2", 
"TouchHistoryItem:g3", 
"TouchVelocityReading:g4", 
"TouchGestureEventHandler:g5", 
"TouchGestureEventArgs:g6", 
"CancelableTouchGestureEventHandler:g7", 
"CssHelper:g8", 
"CssGradientUtil:g9", 
"FontUtil:ha", 
"TileZoomTile:hb", 
"TileZoomTileInfo:hc", 
"TileZoomTileCache:hd", 
"TileZoomManager:he", 
"RectChangedEventHandler:hf", 
"RectChangedEventArgs:hg", 
"Debug:hh", 
"TileZoomInfo:hi", 
"LinkedList$1:hj", 
"LinkedListNode$1:hk", 
"RenderSurface:hl", 
"FragmentBase:hm", 
"HorizontalAnchoredCategorySeries:hn", 
"AnchoredCategorySeries:ho", 
"CategorySeries:hp", 
"MarkerSeries:hq", 
"MarkerSeriesView:hr", 
"Marker:hs", 
"DataContext:ht", 
"MarkerTemplates:hu", 
"HashPool$2:hv", 
"IHashPool$2:hw", 
"IPool$1:hx", 
"Func$1:hy", 
"Pool$1:hz", 
"IIndexedPool$1:h0", 
"MarkerType:h1", 
"SeriesVisualData:h2", 
"PrimitiveVisualDataList:h3", 
"IVisualData:h4", 
"PrimitiveVisualData:h5", 
"PrimitiveAppearanceData:h6", 
"BrushAppearanceData:h7", 
"StringBuilder:h8", 
"Environment:h9", 
"AppearanceHelper:ia", 
"LinearGradientBrushAppearanceData:ib", 
"GradientStopAppearanceData:ic", 
"SolidBrushAppearanceData:id", 
"GeometryData:ie", 
"GetPointsSettings:ig", 
"EllipseGeometryData:ih", 
"RectangleGeometryData:ii", 
"LineGeometryData:ij", 
"PathGeometryData:ik", 
"PathFigureData:il", 
"SegmentData:im", 
"LineSegmentData:io", 
"PolylineSegmentData:ip", 
"ArcSegmentData:iq", 
"PolyBezierSegmentData:ir", 
"BezierSegmentData:is", 
"LabelAppearanceData:it", 
"ShapeTags:iu", 
"PointerTooltipVisualDataList:iv", 
"MarkerVisualDataList:iw", 
"MarkerVisualData:ix", 
"PointerTooltipVisualData:iy", 
"RectangleVisualData:iz", 
"PolygonVisualData:i0", 
"PolyLineVisualData:i1", 
"IFastItemsSource:i2", 
"IFastItemColumn$1:i3", 
"IFastItemColumnPropertyName:i4", 
"FastItemsSourceEventArgs:i5", 
"FastItemsSourceEventAction:i6", 
"IHasCategoryModePreference:i7", 
"IHasCategoryAxis:i8", 
"CategoryAxisBase:i9", 
"Axis:ja", 
"AxisView:jb", 
"StackedSeriesBase:jc", 
"IIsCategoryBased:jd", 
"CategoryMode:je", 
"ICategoryScaler:jf", 
"IScaler:jg", 
"ScalerParams:jh", 
"IBucketizer:ji", 
"IDetectsCollisions:jj", 
"StackedSeriesView:jk", 
"CategorySeriesView:jl", 
"ISupportsMarkers:jm", 
"CategoryBucketCalculator:jn", 
"ISortingAxis:jo", 
"CategoryFrame:jp", 
"Frame:jq", 
"BrushUtil:jr", 
"Canvas:js", 
"Panel:jt", 
"UIElementCollection:ju", 
"StackedBucketCalculator:jv", 
"StackedSeriesManager:jw", 
"StackedSeriesCollection:jx", 
"StackedFragmentSeries:jy", 
"PenLineCap:jz", 
"PropertyUpdatedEventHandler:j0", 
"PropertyUpdatedEventArgs:j1", 
"StackedAreaSeries:j2", 
"HorizontalStackedSeriesBase:j3", 
"NumericYAxis:j4", 
"StraightNumericAxisBase:j5", 
"NumericAxisBase:j6", 
"NumericAxisBaseView:j7", 
"NumericAxisRenderer:j8", 
"AxisRendererBase:j9", 
"ShouldRenderHandler:ka", 
"ScaleValueHandler:kb", 
"AxisRenderingParametersBase:kc", 
"RangeInfo:kd", 
"TickmarkValues:ke", 
"TickmarkValuesInitializationParameters:kf", 
"Func$4:kg", 
"GetGroupCenterHandler:kh", 
"GetUnscaledGroupCenterHandler:ki", 
"PathRenderingInfo:kj", 
"RenderStripHandler:kk", 
"RenderLineHandler:kl", 
"ShouldRenderLinesHandler:km", 
"ShouldRenderContentHandler:kn", 
"RenderAxisLineHandler:ko", 
"DetermineCrossingValueHandler:kp", 
"ShouldRenderLabelHandler:kq", 
"GetLabelLocationHandler:kr", 
"LabelPosition:ks", 
"TransformToLabelValueHandler:kt", 
"AxisLabelManager:ku", 
"AxisLabelPanelBase:kv", 
"AxisLabelPanelBaseView:kw", 
"AxisLabelSettings:kx", 
"AxisLabelsLocation:ky", 
"TitleSettings:kz", 
"GetLabelForItemHandler:k0", 
"CreateRenderingParamsHandler:k1", 
"SnapMajorValueHandler:k2", 
"AdjustMajorValueHandler:k3", 
"CategoryAxisRenderingParameters:k4", 
"LogarithmicTickmarkValues:k5", 
"LogarithmicNumericSnapper:k6", 
"Snapper:k7", 
"LinearTickmarkValues:k8", 
"LinearNumericSnapper:k9", 
"AxisRangeChangedEventArgs:la", 
"AxisRange:lb", 
"AutoRangeCalculator:lc", 
"NumericRadiusAxis:ld", 
"NumericRadiusAxisView:le", 
"NumericAngleAxis:lf", 
"IAngleScaler:lg", 
"NumericAngleAxisView:lh", 
"PolarAxisRenderingManager:li", 
"ViewportUtils:lj", 
"PolarAxisRenderingParameters:lk", 
"NumericAxisRenderingParameters:ll", 
"IPolarRadialRenderingParameters:lm", 
"RadialAxisRenderingParameters:ln", 
"AxisOrientation:lo", 
"AngleAxisLabelPanel:lp", 
"AngleAxisLabelPanelView:lq", 
"Extensions:lr", 
"CategoryAngleAxis:ls", 
"CategoryAngleAxisView:lt", 
"CategoryAxisBaseView:lu", 
"CategoryAxisRenderer:lv", 
"LinearCategorySnapper:lw", 
"CategoryTickmarkValues:lx", 
"RadialAxisLabelPanel:ly", 
"HorizontalAxisLabelPanelBase:lz", 
"HorizontalAxisLabelPanelBaseView:l0", 
"RadialAxisLabelPanelView:l1", 
"NumericScaler:l2", 
"StraightNumericAxisBaseView:l3", 
"NumericScaleMode:l4", 
"LogarithmicScaler:l5", 
"NumericXAxis:l6", 
"NumericXAxisView:l7", 
"HorizontalSmartAxisLabelPanel:l8", 
"AxisExtentType:l9", 
"SmartAxisLabelDisplayType:ma", 
"HorizontalSmartAxisLabelPanelView:mb", 
"FontMappingInfo:mc", 
"CategoryDateTimeXAxis:md", 
"CategoryDateTimeXAxisView:me", 
"TimeAxisDisplayType:mf", 
"FastItemDateTimeColumn:mg", 
"IFastItemColumnInternal:mh", 
"FastItemColumn:mi", 
"FastReflectionHelper:mj", 
"HorizontalAxisLabelPanel:mk", 
"CoercionInfo:ml", 
"SortedListView$1:mm", 
"ArrayUtil:mn", 
"HorizontalLogarithmicScaler:mo", 
"HorizontalLinearScaler:mp", 
"LinearScaler:mq", 
"NumericYAxisView:mr", 
"VerticalAxisLabelPanel:ms", 
"VerticalAxisLabelPanelView:mt", 
"VerticalLogarithmicScaler:mu", 
"VerticalLinearScaler:mv", 
"CategoryFramePreparerBase:mw", 
"FramePreparer:mx", 
"ISupportsErrorBars:my", 
"DefaultSupportsMarkers:mz", 
"DefaultProvidesViewport:m0", 
"DefaultSupportsErrorBars:m1", 
"PreparationParams:m2", 
"CategoryYAxis:m3", 
"CategoryYAxisView:m4", 
"SyncSettings:m5", 
"ValuesHolder:m6", 
"LineSeries:m7", 
"LineSeriesView:m8", 
"AnchoredCategorySeriesView:m9", 
"CategoryTrendLineManagerBase:na", 
"TrendLineManagerBase$1:nb", 
"TrendLineType:nc", 
"Clipper:nd", 
"EdgeClipper:ne", 
"LeftClipper:nf", 
"BottomClipper:ng", 
"RightClipper:nh", 
"TopClipper:ni", 
"TrendResolutionParams:nj", 
"Flattener:nk", 
"Stack$1:nl", 
"ReverseArrayEnumerator$1:nm", 
"SpiralTodo:nn", 
"FlattenerSettings:no", 
"IPreparesCategoryTrendline:np", 
"SortingTrendLineManager:nq", 
"TrendFitCalculator:nr", 
"LeastSquaresFit:ns", 
"Numeric:nt", 
"TrendAverageCalculator:nu", 
"CategoryTrendLineManager:nv", 
"AnchoredCategoryBucketCalculator:nw", 
"UnknownValuePlotting:nx", 
"CategoryLineRasterizer:ny", 
"Action$5:nz", 
"PathVisualData:n0", 
"CategorySeriesRenderManager:n1", 
"AssigningCategoryStyleEventArgs:n2", 
"AssigningCategoryStyleEventArgsBase:n3", 
"GetCategoryItemsHandler:n4", 
"HighlightingInfo:n5", 
"HighlightingState:n6", 
"AssigningCategoryMarkerStyleEventArgs:n7", 
"HighlightingManager:n8", 
"SplineSeriesBase:n9", 
"SplineSeriesBaseView:oa", 
"SplineType:ob", 
"CollisionAvoider:oc", 
"SafeSortedReadOnlyDoubleCollection:od", 
"SafeReadOnlyDoubleCollection:oe", 
"SafeEnumerable:of", 
"AreaSeries:og", 
"AreaSeriesView:oh", 
"LegendTemplates:oi", 
"PieChartBase:oj", 
"PieChartBaseView:ok", 
"PieChartViewManager:ol", 
"PieChartVisualData:om", 
"PieSliceVisualDataList:on", 
"PieSliceVisualData:oo", 
"PieSliceDataContext:op", 
"Slice:oq", 
"SliceView:or", 
"PieLabel:os", 
"LabelsPosition:ot", 
"MouseButtonEventArgs:ou", 
"FastItemsSource:ov", 
"ColumnReference:ow", 
"FastItemObjectColumn:ox", 
"FastItemIntColumn:oy", 
"LeaderLineType:oz", 
"OthersCategoryType:o0", 
"IndexCollection:o1", 
"LegendBase:o2", 
"LegendBaseView:o3", 
"LegendBaseViewManager:o4", 
"GradientData:o5", 
"GradientStopData:o6", 
"DataChartLegendMouseButtonEventArgs:o7", 
"DataChartMouseButtonEventArgs:o8", 
"ChartLegendMouseEventArgs:o9", 
"ChartMouseEventArgs:pa", 
"DataChartLegendMouseButtonEventHandler:pb", 
"DataChartLegendMouseEventHandler:pc", 
"LegendVisualData:pd", 
"LegendVisualDataList:pe", 
"LegendItemVisualData:pf", 
"FunnelSliceDataContext:pg", 
"PieChartFormatLabelHandler:ph", 
"LabelClickEventHandler:pi", 
"LabelClickEventArgs:pj", 
"SliceClickEventHandler:pk", 
"SliceClickEventArgs:pl", 
"ItemLegend:pm", 
"ItemLegendView:pn", 
"LegendItemInfo:po", 
"BubbleSeries:pp", 
"ScatterBase:pq", 
"ScatterBaseView:pr", 
"MarkerManagerBase:ps", 
"OwnedPoint:pt", 
"MarkerManagerBucket:pu", 
"ScatterTrendLineManager:pv", 
"NumericMarkerManager:pw", 
"CollisionAvoidanceType:px", 
"SmartPlacer:py", 
"ISmartPlaceable:pz", 
"SmartPosition:p0", 
"SmartPlaceableWrapper$1:p1", 
"ScatterAxisInfoCache:p2", 
"ScatterErrorBarSettings:p3", 
"ErrorBarSettingsBase:p4", 
"EnableErrorBars:p5", 
"ErrorBarCalculatorReference:p6", 
"IErrorBarCalculator:p7", 
"ErrorBarCalculatorType:p8", 
"ScatterFrame:p9", 
"ScatterFrameBase$1:qa", 
"DictInterpolator$3:qb", 
"Action$6:qc", 
"SeriesHitTestMode:qd", 
"SyncLink:qe", 
"IFastItemsSourceProvider:qf", 
"ChartCollection:qg", 
"FastItemsSourceReference:qh", 
"SyncManager:qi", 
"SyncLinkManager:qj", 
"ErrorBarsHelper:qk", 
"BubbleSeriesView:ql", 
"BubbleMarkerManager:qm", 
"SizeScale:qn", 
"BrushScale:qo", 
"ScaleLegend:qp", 
"ScaleLegendView:qq", 
"CustomPaletteBrushScale:qr", 
"BrushSelectionMode:qs", 
"ValueBrushScale:qt", 
"RingSeriesBase:qu", 
"XamDoughnutChart:qv", 
"RingCollection:qw", 
"Ring:qx", 
"RingControl:qy", 
"RingControlView:qz", 
"Arc:q0", 
"ArcView:q1", 
"ArcItem:q2", 
"SliceItem:q3", 
"RingSeriesBaseView:q4", 
"Nullable$1:q5", 
"RingSeriesCollection:q6", 
"SliceCollection:q7", 
"XamDoughnutChartView:q8", 
"Action$2:q9", 
"DoughnutChartVisualData:ra", 
"RingSeriesVisualDataList:rb", 
"RingSeriesVisualData:rc", 
"RingVisualDataList:rd", 
"RingVisualData:re", 
"ArcVisualDataList:rf", 
"ArcVisualData:rg", 
"SliceVisualDataList:rh", 
"SliceVisualData:ri", 
"DoughnutChartLabelVisualData:rj", 
"HoleDimensionsChangedEventHandler:rk", 
"HoleDimensionsChangedEventArgs:rl", 
"XamFunnelChart:rm", 
"IItemProvider:rn", 
"MessageHandler:ro", 
"MessageHandlerEventHandler:rp", 
"Message:rq", 
"ServiceProvider:rr", 
"MessageChannel:rs", 
"MessageEventHandler:rt", 
"Queue$1:ru", 
"XamFunnelConnector:rv", 
"XamFunnelController:rw", 
"SliceInfoList:rx", 
"SliceInfo:ry", 
"SliceAppearance:rz", 
"PointList:r0", 
"FunnelSliceVisualData:r1", 
"PointData:r2", 
"SliceInfoUnaryComparison:r3", 
"Bezier:r4", 
"BezierPoint:r5", 
"BezierOp:r6", 
"BezierPointComparison:r7", 
"DoubleColumn:r8", 
"ObjectColumn:r9", 
"XamFunnelView:sa", 
"IOuterLabelWidthDecider:sb", 
"IFunnelLabelSizeDecider:sc", 
"MouseLeaveMessage:sd", 
"InteractionMessage:se", 
"MouseMoveMessage:sf", 
"MouseButtonMessage:sg", 
"MouseButtonAction:sh", 
"MouseButtonType:si", 
"SetAreaSizeMessage:sj", 
"RenderingMessage:sk", 
"RenderSliceMessage:sl", 
"RenderOuterLabelMessage:sm", 
"TooltipValueChangedMessage:sn", 
"TooltipUpdateMessage:so", 
"FunnelDataContext:sp", 
"PropertyChangedMessage:sq", 
"ConfigurationMessage:sr", 
"ClearMessage:ss", 
"ClearTooltipMessage:st", 
"ContainerSizeChangedMessage:su", 
"ViewportChangedMessage:sv", 
"ViewPropertyChangedMessage:sw", 
"OuterLabelAlignment:sx", 
"FunnelSliceDisplay:sy", 
"SliceSelectionManager:sz", 
"DataUpdatedMessage:s0", 
"ItemsSourceAction:s1", 
"FunnelFrame:s2", 
"UserSelectedItemsChangedMessage:s3", 
"LabelSizeChangedMessage:s4", 
"FrameRenderCompleteMessage:s5", 
"IntColumn:s6", 
"IntColumnComparison:s7", 
"Convert:s8", 
"SelectedItemsChangedMessage:s9", 
"ModelUpdateMessage:ta", 
"SliceClickedMessage:tb", 
"FunnelSliceClickedEventHandler:tc", 
"FunnelSliceClickedEventArgs:td", 
"FunnelChartVisualData:te", 
"FunnelSliceVisualDataList:tf", 
"RingSeries:tg", 
"WaterfallSeries:th", 
"WaterfallSeriesView:ti", 
"CategoryTransitionInMode:tj", 
"FinancialSeries:tk", 
"FinancialSeriesView:tl", 
"FinancialBucketCalculator:tm", 
"CategoryTransitionSourceFramePreparer:tn", 
"TransitionInSpeedType:to", 
"FinancialCalculationDataSource:tp", 
"CalculatedColumn:tq", 
"FinancialEventArgs:tr", 
"FinancialCalculationSupportingCalculations:ts", 
"ColumnSupportingCalculation:tt", 
"SupportingCalculation$1:tu", 
"SupportingCalculationStrategy:tv", 
"DataSourceSupportingCalculation:tw", 
"ProvideColumnValuesStrategy:tx", 
"AssigningCategoryStyleEventHandler:ty", 
"FinancialValueList:tz", 
"CategoryXAxis:t0", 
"CategoryXAxisView:t1", 
"FinancialEventHandler:t2", 
"StepLineSeries:t3", 
"StepLineSeriesView:t4", 
"StepAreaSeries:t5", 
"StepAreaSeriesView:t6", 
"RangeAreaSeries:t7", 
"HorizontalRangeCategorySeries:t8", 
"RangeCategorySeries:t9", 
"IHasHighLowValueCategory:ua", 
"RangeCategorySeriesView:ub", 
"RangeCategoryBucketCalculator:uc", 
"RangeCategoryFramePreparer:ud", 
"IHasCategoryTrendline:ue", 
"IHasTrendline:uf", 
"DefaultCategoryTrendlineHost:ug", 
"DefaultCategoryTrendlinePreparer:uh", 
"DefaultHighLowValueProvider:ui", 
"HighLowValuesHolder:uj", 
"CategoryMarkerManager:uk", 
"RangeValueList:ul", 
"RangeAreaSeriesView:um", 
"LineFragment:un", 
"LineFragmentView:uo", 
"LineFragmentBucketCalculator:up", 
"IStacked100Series:uq", 
"AreaFragment:ur", 
"AreaFragmentView:us", 
"AreaFragmentBucketCalculator:ut", 
"StackedSplineAreaSeries:uu", 
"SplineAreaFragment:uv", 
"SplineFragmentBase:uw", 
"SplineAreaFragmentView:ux", 
"StackedColumnSeries:uy", 
"StackedColumnSeriesView:uz", 
"StackedColumnBucketCalculator:u0", 
"ColumnFragment:u1", 
"ColumnFragmentView:u2", 
"StackedBarSeries:u3", 
"VerticalStackedSeriesBase:u4", 
"IBarSeries:u5", 
"StackedBarSeriesView:u6", 
"StackedBarBucketCalculator:u7", 
"BarFragment:u8", 
"StackedLineSeries:u9", 
"StackedSplineSeries:va", 
"SplineFragment:vb", 
"SplineFragmentView:vc", 
"SplineFragmentBucketCalculator:vd", 
"StackedSeriesFramePreparer:ve", 
"CategoryFramePreparer:vf", 
"IHasSingleValueCategory:vg", 
"DefaultSingleValueProvider:vh", 
"SingleValuesHolder:vi", 
"StackedSeriesCreatedEventHandler:vj", 
"StackedSeriesCreatedEventArgs:vk", 
"StackedSeriesVisualData:vl", 
"SeriesVisualDataList:vm", 
"AxisComponentsForView:vn", 
"AxisComponentsFromView:vo", 
"AxisFormatLabelHandler:vp", 
"VisualExportHelper:vq", 
"ContentInfo:vr", 
"LabelFontHeuristics:vs", 
"AxisRangeChangedEventHandler:vt", 
"ChartContentManager:vu", 
"ChartContentType:vv", 
"RenderRequestedEventArgs:vw", 
"ChartTitleVisualData:vx", 
"VisualDataSerializer:vy", 
"AxisVisualData:vz", 
"AxisLabelVisualDataList:v0", 
"AxisLabelVisualData:v1", 
"AssigningCategoryMarkerStyleEventHandler:v2", 
"SeriesComponentsForView:v3", 
"CategorySeriesMarkerCollisionAvoidance:v4", 
"NonCollisionAvoider:v5", 
"DataChartAxisRangeChangedEventHandler:v6", 
"ChartAxisRangeChangedEventArgs:v7", 
"ChartVisualData:v8", 
"AxisVisualDataList:v9", 
"RadialBase:wa", 
"RadialBaseView:wb", 
"RadialBucketCalculator:wc", 
"SeriesRenderer$2:wd", 
"SeriesRenderingArguments:we", 
"RadialFrame:wf", 
"RadialAxes:wg", 
"PolarBase:wh", 
"PolarBaseView:wi", 
"PolarTrendLineManager:wj", 
"PolarLinePlanner:wk", 
"AngleRadiusPair:wl", 
"PolarAxisInfoCache:wm", 
"PolarFrame:wn", 
"PolarAxes:wo", 
"SeriesCollection:wp", 
"SeriesViewerComponentsFromView:wq", 
"SeriesViewerSurfaceViewer:wr", 
"LabelPanelArranger:ws", 
"LabelPanelsArrangeState:wt", 
"ChartHitTestMode:wu", 
"WindowResponse:wv", 
"ViewerSurfaceUsage:ww", 
"SeriesViewerComponentsForView:wx", 
"DataChartCursorEventHandler:wy", 
"ChartCursorEventArgs:wz", 
"DataChartMouseButtonEventHandler:w0", 
"DataChartMouseEventHandler:w1", 
"AnnotationLayer:w2", 
"AnnotationLayerView:w3", 
"RefreshCompletedEventHandler:w4", 
"SeriesComponentsFromView:w5", 
"EasingFunctions:w6", 
"PolygonUtil:w7", 
"TrendCalculators:w8", 
"HighDensityScatterSeries:xp", 
"HighDensityScatterSeriesView:xq", 
"KDTree2D:xr", 
"KDTreeNode2D:xs", 
"KDPointData:xt", 
"SearchData:xu", 
"Monitor:xv", 
"KDTreeThunk:xw", 
"KNearestResults:xx", 
"KNearestResult:xy", 
"SearchArgs:xz", 
"ProgressiveLoadStatusEventArgs:x0", 
"BarFramePreparer:yi", 
"BarTrendFitCalculator:yj", 
"BarTrendLineManager:yk", 
"VerticalAnchoredCategorySeries:yl", 
"BarSeries:ym", 
"BarSeriesView:yn", 
"BarBucketCalculator:yo", 
"ScatterLineSeries:ack", 
"ScatterLineSeriesView:acl", 
"ScatterSeries:acm", 
"ScatterSeriesView:acn", 
"ScatterSplineSeries:aco", 
"ScatterSplineSeriesView:acp", 
"Legend:ac1", 
"LegendView:ac2", 
"AbstractEnumerable:adb", 
"AbstractEnumerator:adc", 
"GenericEnumerable$1:add", 
"GenericEnumerator$1:ade"]);


$.ig.util.defType('EnableErrorBars', 'Enum', {
	init: function (v) {
		this._v = v;
	},
	$getName: function (v) {
		switch (v) {
			case 0: return "None";
			case 1: return "Both";
			case 2: return "Positive";
			case 3: return "Negative";
			default: return v.toString();
		}
	},
	$value: function () {
		return this._v;
	},
	$type: new $.ig.Type('EnableErrorBars', $.ig.Enum.prototype.$type)
}, true);

$.ig.util.defType('BrushSelectionMode', 'Enum', {
	init: function (v) {
		this._v = v;
	},
	$getName: function (v) {
		switch (v) {
			case 0: return "Select";
			case 1: return "Interpolate";
			default: return v.toString();
		}
	},
	$value: function () {
		return this._v;
	},
	$type: new $.ig.Type('BrushSelectionMode', $.ig.Enum.prototype.$type)
}, true);

$.ig.util.defType('CollisionAvoidanceType', 'Enum', {
	init: function (v) {
		this._v = v;
	},
	$getName: function (v) {
		switch (v) {
			case 0: return "None";
			case 1: return "Omit";
			case 2: return "Fade";
			case 3: return "OmitAndShift";
			case 4: return "FadeAndShift";
			default: return v.toString();
		}
	},
	$value: function () {
		return this._v;
	},
	$type: new $.ig.Type('CollisionAvoidanceType', $.ig.Enum.prototype.$type)
}, true);

$.ig.util.defType('HighDensityScatterSeries', 'Series', {
	init: function () {
		this.__minR = 0;
		this.__minG = 0;
		this.__minB = 0;
		this.__maxR = 255;
		this.__maxG = 0;
		this.__maxB = 0;
		this.__rangeR = 255;
		this.__rangeG = 0;
		this.__rangeB = 0;
		this.__tree = null;
		this.__expectedLevels = 0;
		this.__currentLevel = 0;
		this.__rendered = 0;
		this.__hasEffectiveViewport = false;
		this.__itemIndexes = null;
		this.__values = null;
		this.__alphas = null;
		$.ig.Series.prototype.init.call(this);
		this.defaultStyleKey($.ig.HighDensityScatterSeries.prototype.$type);
	},
	isScatter: function () {
		return true;
	}
	,
	xAxis: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.HighDensityScatterSeries.prototype.xAxisProperty, value);
			return value;
		} else {
			return this.getValue($.ig.HighDensityScatterSeries.prototype.xAxisProperty);
		}
	}
	,
	yAxis: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.HighDensityScatterSeries.prototype.yAxisProperty, value);
			return value;
		} else {
			return this.getValue($.ig.HighDensityScatterSeries.prototype.yAxisProperty);
		}
	}
	,
	xMemberPath: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.HighDensityScatterSeries.prototype.xMemberPathProperty, value);
			return value;
		} else {
			return this.getValue($.ig.HighDensityScatterSeries.prototype.xMemberPathProperty);
		}
	}
	,
	xColumn: function (value) {
		if (arguments.length === 1) {
			if (this._xColumn != value) {
				var oldXColumn = this.xColumn();
				this._xColumn = value;
				this.raisePropertyChanged($.ig.HighDensityScatterSeries.prototype.xColumnPropertyName, oldXColumn, this.xColumn());
			}
			return value;
		} else {
			return this._xColumn;
		}
	}
	,
	_xColumn: null,
	yMemberPath: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.HighDensityScatterSeries.prototype.yMemberPathProperty, value);
			return value;
		} else {
			return this.getValue($.ig.HighDensityScatterSeries.prototype.yMemberPathProperty);
		}
	}
	,
	yColumn: function (value) {
		if (arguments.length === 1) {
			if (this._yColumn != value) {
				var oldYColumn = this.yColumn();
				this._yColumn = value;
				this.raisePropertyChanged($.ig.HighDensityScatterSeries.prototype.yColumnPropertyName, oldYColumn, this.yColumn());
			}
			return value;
		} else {
			return this._yColumn;
		}
	}
	,
	_yColumn: null,
	useBruteForce: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.HighDensityScatterSeries.prototype.useBruteForceProperty, value);
			return value;
		} else {
			return this.getValue($.ig.HighDensityScatterSeries.prototype.useBruteForceProperty);
		}
	}
	,
	progressiveLoad: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.HighDensityScatterSeries.prototype.progressiveLoadProperty, value);
			return value;
		} else {
			return this.getValue($.ig.HighDensityScatterSeries.prototype.progressiveLoadProperty);
		}
	}
	,
	mouseOverEnabled: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.HighDensityScatterSeries.prototype.mouseOverEnabledProperty, value);
			return value;
		} else {
			return this.getValue($.ig.HighDensityScatterSeries.prototype.mouseOverEnabledProperty);
		}
	}
	,
	maxRenderDepth: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.HighDensityScatterSeries.prototype.maxRenderDepthProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.HighDensityScatterSeries.prototype.maxRenderDepthProperty));
		}
	}
	,
	heatMinimum: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.HighDensityScatterSeries.prototype.heatMinimumProperty, value);
			return value;
		} else {
			return this.getValue($.ig.HighDensityScatterSeries.prototype.heatMinimumProperty);
		}
	}
	,
	heatMaximum: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.HighDensityScatterSeries.prototype.heatMaximumProperty, value);
			return value;
		} else {
			return this.getValue($.ig.HighDensityScatterSeries.prototype.heatMaximumProperty);
		}
	}
	,
	heatMinimumColor: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.HighDensityScatterSeries.prototype.heatMinimumColorProperty, value);
			return value;
		} else {
			return this.getValue($.ig.HighDensityScatterSeries.prototype.heatMinimumColorProperty);
		}
	}
	,
	heatMaximumColor: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.HighDensityScatterSeries.prototype.heatMaximumColorProperty, value);
			return value;
		} else {
			return this.getValue($.ig.HighDensityScatterSeries.prototype.heatMaximumColorProperty);
		}
	}
	,
	pointExtent: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.HighDensityScatterSeries.prototype.pointExtentProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.HighDensityScatterSeries.prototype.pointExtentProperty));
		}
	}
	,
	resetTree: function () {
		if (this.__tree == null) {
			return;
		}
		this.__tree.cancelLoad();
		var $t = this.__tree;
		$t.progressiveThunkCompleted = $.ig.Delegate.prototype.remove($t.progressiveThunkCompleted, this._tree_ProgressiveThunkCompleted.runOn(this));
		this.__tree = null;
	}
	,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		$.ig.Series.prototype.propertyUpdatedOverride.call(this, sender, propertyName, oldValue, newValue);
		switch (propertyName) {
			case $.ig.Series.prototype.seriesViewerPropertyName:
				if (this.seriesViewer() != null) {
					var ev = this.getEffectiveViewport1(this.view());
					this.__hasEffectiveViewport = !ev.isEmpty();
				}
				break;
			case $.ig.Series.prototype.fastItemsSourcePropertyName:
				this.resetTree();
				if ($.ig.util.cast($.ig.IFastItemsSource.prototype.$type, oldValue) != null) {
					(oldValue).deregisterColumn(this.xColumn());
					(oldValue).deregisterColumn(this.yColumn());
					this.xColumn(null);
					this.yColumn(null);
				}
				if ($.ig.util.cast($.ig.IFastItemsSource.prototype.$type, newValue) != null) {
					this.xColumn(this.registerDoubleColumn(this.xMemberPath()));
					this.yColumn(this.registerDoubleColumn(this.yMemberPath()));
				}
				if ((this.yAxis() != null && !this.yAxis().updateRange()) || (this.xAxis() != null && !this.xAxis().updateRange())) {
					this.renderSeries(false);
				}
				this.notifyThumbnailDataChanged();
				break;
			case $.ig.HighDensityScatterSeries.prototype.xAxisPropertyName:
				var oldAxis = this.__xAxis;
				this.__xAxis = this.xAxis();
				this.resetTree();
				if (oldAxis != null) {
					oldAxis.rangeChanged = $.ig.Delegate.prototype.remove(oldAxis.rangeChanged, this.axis_RangeChanged.runOn(this));
				}
				if (this.__xAxis != null) {
					var $t = this.__xAxis;
					$t.rangeChanged = $.ig.Delegate.prototype.combine($t.rangeChanged, this.axis_RangeChanged.runOn(this));
				}
				if (oldValue != null) {
					(oldValue).deregisterSeries(this);
				}
				if (newValue != null) {
					(newValue).registerSeries(this);
				}
				if ((this.xAxis() != null && !this.xAxis().updateRange()) || (newValue == null && oldValue != null)) {
					this.renderSeries(false);
				}
				break;
			case $.ig.HighDensityScatterSeries.prototype.yAxisPropertyName:
				var oldYAxis = this.__yAxis;
				this.__yAxis = this.yAxis();
				this.resetTree();
				if (oldYAxis != null) {
					oldYAxis.rangeChanged = $.ig.Delegate.prototype.remove(oldYAxis.rangeChanged, this.axis_RangeChanged.runOn(this));
				}
				if (this.__yAxis != null) {
					var $t1 = this.__yAxis;
					$t1.rangeChanged = $.ig.Delegate.prototype.combine($t1.rangeChanged, this.axis_RangeChanged.runOn(this));
				}
				if (oldValue != null) {
					(oldValue).deregisterSeries(this);
				}
				if (newValue != null) {
					(newValue).registerSeries(this);
				}
				if ((this.yAxis() != null && !this.yAxis().updateRange()) || (newValue == null && oldValue != null)) {
					this.renderSeries(false);
				}
				break;
			case $.ig.HighDensityScatterSeries.prototype.mouseOverEnabledPropertyName:
				this.__mouseOverEnabled = this.mouseOverEnabled();
				this.renderSeries(false);
				break;
			case $.ig.HighDensityScatterSeries.prototype.xMemberPathPropertyName:
				if (this.fastItemsSource() != null) {
					this.fastItemsSource().deregisterColumn(this.xColumn());
					this.xColumn(this.registerDoubleColumn(this.xMemberPath()));
				}
				break;
			case $.ig.HighDensityScatterSeries.prototype.xColumnPropertyName:
				this.resetTree();
				if (this.xAxis() != null && !this.xAxis().updateRange()) {
					this.renderSeries(false);
				}
				this.notifyThumbnailDataChanged();
				break;
			case $.ig.HighDensityScatterSeries.prototype.yMemberPathPropertyName:
				if (this.fastItemsSource() != null) {
					this.fastItemsSource().deregisterColumn(this.yColumn());
					this.yColumn(this.registerDoubleColumn(this.yMemberPath()));
				}
				break;
			case $.ig.HighDensityScatterSeries.prototype.yColumnPropertyName:
				this.resetTree();
				if (this.yAxis() != null && !this.yAxis().updateRange()) {
					this.renderSeries(false);
				}
				this.notifyThumbnailDataChanged();
				break;
			case $.ig.HighDensityScatterSeries.prototype.useBruteForcePropertyName:
				this.resetTree();
				this.renderSeries(false);
				this.notifyThumbnailAppearanceChanged();
				break;
			case $.ig.HighDensityScatterSeries.prototype.heatMinimumPropertyName:
				this.renderSeries(false);
				this.notifyThumbnailAppearanceChanged();
				break;
			case $.ig.HighDensityScatterSeries.prototype.heatMaximumPropertyName:
				this.renderSeries(false);
				this.notifyThumbnailAppearanceChanged();
				break;
			case $.ig.HighDensityScatterSeries.prototype.maxRenderDepthPropertyName:
				this.renderSeries(false);
				break;
			case $.ig.HighDensityScatterSeries.prototype.heatMaximumColorPropertyName:
			case $.ig.HighDensityScatterSeries.prototype.heatMinimumColorPropertyName:
				if (!this.superView().colorScaleValid(this.heatMinimumColor(), this.heatMaximumColor())) {
					return;
				}
				this.__minR = this.heatMinimumColor().r();
				this.__minG = this.heatMinimumColor().g();
				this.__minB = this.heatMinimumColor().b();
				this.__maxR = this.heatMaximumColor().r();
				this.__maxG = this.heatMaximumColor().g();
				this.__maxB = this.heatMaximumColor().b();
				this.__rangeR = this.__maxR - this.__minR;
				this.__rangeG = this.__maxG - this.__minG;
				this.__rangeB = this.__maxB - this.__minB;
				this.renderSeries(false);
				this.notifyThumbnailAppearanceChanged();
				break;
			case $.ig.HighDensityScatterSeries.prototype.pointExtentPropertyName:
				this.__pointExtent = Math.max($.ig.util.getValue(newValue) - 1, 0);
				this.__drawExtent = this.__pointExtent > 0;
				this.renderSeries(false);
				this.notifyThumbnailAppearanceChanged();
				break;
			case $.ig.HighDensityScatterSeries.prototype.progressiveLoadPropertyName:
				this.renderSeries(false);
				this.notifyThumbnailAppearanceChanged();
				break;
		}
	}
	,
	canUseAsYAxis: function (axis) {
		if ($.ig.util.cast($.ig.NumericYAxis.prototype.$type, axis) !== null) {
			return true;
		}
		return false;
	}
	,
	canUseAsXAxis: function (axis) {
		if ($.ig.util.cast($.ig.NumericXAxis.prototype.$type, axis) !== null) {
			return true;
		}
		return false;
	}
	,
	axis_RangeChanged: function (sender, e) {
		this.resetTree();
		this.renderSeries(false);
	}
	,
	__pointExtent: 0,
	__drawExtent: false,
	__minR: 0,
	__minG: 0,
	__minB: 0,
	__maxR: 0,
	__maxG: 0,
	__maxB: 0,
	__rangeR: 0,
	__rangeG: 0,
	__rangeB: 0,
	__tree: null,
	__scalerParamsX: null,
	__scalerParamsY: null,
	__xAxis: null,
	__yAxis: null,
	__mouseOverEnabled: false,
	validateSeries: function (viewportRect, windowRect, view) {
		var ret = $.ig.Series.prototype.validateSeries.call(this, viewportRect, windowRect, view);
		if (this.yAxis() == null || this.xAxis() == null || this.xAxis().seriesViewer() == null || this.yAxis().seriesViewer() == null || this.yColumn() == null || this.xColumn() == null || this.yColumn().count() < 1 || this.xColumn().count() < 1 || this.yColumn().count() != this.xColumn().count() || this.xAxis().actualMinimumValue() == this.xAxis().actualMaximumValue() || this.yAxis().actualMinimumValue() == this.yAxis().actualMaximumValue() || this.viewport().isEmpty()) {
			ret = false;
		}
		return ret;
	}
	,
	__progressiveStatus: 0,
	progressiveStatus: function (value) {
		if (arguments.length === 1) {
			var oldValue = this.__progressiveStatus;
			this.__progressiveStatus = value;
			this.raisePropertyChanged("ProgressiveStatus", oldValue, this.__progressiveStatus);
			return value;
		} else {
			return this.__progressiveStatus;
		}
	}
	,
	renderSeriesOverride: function (animate) {
		$.ig.Series.prototype.renderSeriesOverride.call(this, animate);
		this.doRender(animate, this.view());
	}
	,
	doRender: function (animate, view) {
		if (this.clearAndAbortIfInvalid1(view)) {
			return;
		}
		if (this.__tree == null && !this.useBruteForce()) {
			var points = new $.ig.List$1($.ig.KDPointData.prototype.$type, 2, this.xColumn().count());
			var xAxis = this.xAxis();
			var yAxis = this.yAxis();
			var window = new $.ig.Rect(0, 0, 0, 1, 1);
			var viewport = new $.ig.Rect(0, 0, 0, 1, 1);
			var xParams = new $.ig.ScalerParams(1, window, viewport, xAxis.isInverted());
			var yParams = new $.ig.ScalerParams(1, window, viewport, yAxis.isInverted());
			var xVal;
			var yVal;
			for (var i = 0; i < this.xColumn().count(); i++) {
				xVal = xAxis.getScaledValue(this.xColumn().item(i), xParams);
				yVal = yAxis.getScaledValue(this.yColumn().item(i), yParams);
				if (!$.ig.util.isNaN(xVal) && !$.ig.util.isNaN(yVal) && !Number.isInfinity(xVal) && !Number.isInfinity(yVal)) {
					points.add((function () {
						var $ret = new $.ig.KDPointData();
						$ret._x = xVal;
						$ret._y = yVal;
						$ret._index = i;
						return $ret;
					}()));
				}
			}
			if (this.progressiveLoad()) {
				this.__currentLevel = 1;
				this.__expectedLevels = $.ig.truncate(Math.logBase(points.count(), 2)) + 3;
				this.progressiveStatus($.ig.truncate(((this.__currentLevel / this.__expectedLevels) * 100)));
				if (this.progressiveLoadStatusChanged != null) {
					this.progressiveLoadStatusChanged(this, new $.ig.ProgressiveLoadStatusEventArgs(this.progressiveStatus()));
				}
				this.__tree = $.ig.KDTree2D.prototype.getProgressive(points.toArray(), 1);
				var $t = this.__tree;
				$t.progressiveThunkCompleted = $.ig.Delegate.prototype.combine($t.progressiveThunkCompleted, this._tree_ProgressiveThunkCompleted.runOn(this));
				if (!this.__tree.progressiveStep()) {
					var $t1 = this.__tree;
					$t1.progressiveThunkCompleted = $.ig.Delegate.prototype.remove($t1.progressiveThunkCompleted, this._tree_ProgressiveThunkCompleted.runOn(this));
				}
				this.notifyThumbnailAppearanceChanged();
			} else {
				var before = $.ig.Date.prototype.now();
				this.__tree = new $.ig.KDTree2D(0, points.toArray(), 1);
				var after = $.ig.Date.prototype.now();
			}
		}
		if (this.progressiveLoad() && !this.useBruteForce()) {
			this.lockedRender(view);
		} else {
			this.renderBitmap(view);
		}
	}
	,
	__resolution: 0,
	__expectedLevels: 0,
	__currentLevel: 0,
	dataUpdatedOverride: function (action, position, count, propertyName) {
		$.ig.Series.prototype.dataUpdatedOverride.call(this, action, position, count, propertyName);
		this.resetTree();
		var refresh = false;
		if (this.xAxis() != null && !this.xAxis().updateRange()) {
			refresh = true;
		}
		if (this.yAxis() != null && !this.yAxis().updateRange()) {
			refresh = true;
		}
		if (refresh) {
			this.renderSeries(false);
		}
		this.notifyThumbnailDataChanged();
	}
	,
	assertMouseOver: function (view) {
		var hdView = view;
		var pixelCount = this.__imageWidth * this.__imageHeight;
		if (this.__mouseOverEnabled) {
			if (this.__itemIndexes == null || this.__itemIndexes.length != pixelCount) {
				this.__itemIndexes = new Array(pixelCount);
				for (var i = 0; i < pixelCount; i++) {
					this.__itemIndexes[i] = 0;
				}
			} else {
				for (var i1 = 0; i1 < pixelCount; i1++) {
					this.__itemIndexes[i1] = 0;
				}
			}
		}
	}
	,
	renderBitmap: function (view) {
		var window;
		var viewport;
		var oldIndexes = null;
		if (view.isThumbnailView()) {
			oldIndexes = this.__itemIndexes;
		}
		var $ret = view.getViewInfo(viewport, window);
		viewport = $ret.p0;
		window = $ret.p1;
		var superView = view;
		var effectiveViewport = this.getEffectiveViewport1(view);
		this.__scalerParamsX = new $.ig.ScalerParams(0, window, viewport, this.__xAxis.isInverted(), effectiveViewport);
		this.__scalerParamsY = new $.ig.ScalerParams(0, window, viewport, this.__yAxis.isInverted(), effectiveViewport);
		this.assertBitmap(view);
		this.assertMouseOver(view);
		var pixelCount = this.__imageWidth * this.__imageHeight;
		if (this.__values == null || this.__values.length != pixelCount) {
			this.__values = new Array(pixelCount);
			this.__alphas = new Array(pixelCount);
		}
		var valuesLength = this.__values.length;
		var values = this.__values;
		var alphas = this.__alphas;
		for (var i = 0; i < valuesLength; i++) {
			values[i] = 0;
			alphas[i] = 0;
		}
		this.__resolution = $.ig.truncate(Math.round(this.resolution()));
		this.__pixels = superView.getPixelBuffer();
		var pixelsLength = this.__pixels.length;
		var pixels = this.__pixels;
		for (var i1 = 0; i1 < pixelsLength; i1++) {
			pixels[i1] = 0;
		}
		var hmin = this.heatMinimum();
		var hmax = this.heatMaximum();
		if ($.ig.util.isNaN(hmin) || Number.isInfinity(hmin)) {
			hmin = 0;
		}
		if ($.ig.util.isNaN(hmax) || Number.isInfinity(hmax)) {
			hmax = 50;
		}
		this.__heatMinimum = Math.min(hmin, hmax);
		this.__heatMaximum = Math.max(hmin, hmax);
		if (this.__heatMinimum < 0) {
			this.__heatMinimum = 0;
		}
		if (this.__heatMaximum < 0) {
			this.__heatMaximum = 0;
		}
		this.__heatRange = this.__heatMaximum - this.__heatMinimum;
		if (this.useBruteForce()) {
			this.bruteForceRender(view);
		} else {
			this.useTree(view);
		}
		superView.updateBitmap();
		if (view.isThumbnailView()) {
			this.__itemIndexes = oldIndexes;
			this.superView().updateImageValues();
		}
	}
	,
	lockedRender: function (view) {
		if (this.__tree == null) {
			return;
		}
		this.renderBitmap(view);
	}
	,
	progressiveLoadStatusChanged: null,
	_tree_ProgressiveThunkCompleted: function (sender, e) {
		var $self = this;
		this.superView().defer(function () {
			if ($self.__currentLevel < $self.__expectedLevels - 1) {
				$self.__currentLevel++;
			}
			$self.progressiveStatus($.ig.truncate((($self.__currentLevel / $self.__expectedLevels) * 100)));
			if ($self.progressiveLoadStatusChanged != null) {
				$self.progressiveLoadStatusChanged($self, new $.ig.ProgressiveLoadStatusEventArgs($self.progressiveStatus()));
			}
			$self.lockedRender($self.view());
			$self.notifyThumbnailAppearanceChanged();
			if ($self.__tree != null) {
				if (!$self.__tree.progressiveStep()) {
					var $t = $self.__tree;
					$t.progressiveThunkCompleted = $.ig.Delegate.prototype.remove($t.progressiveThunkCompleted, $self._tree_ProgressiveThunkCompleted.runOn($self));
					$self.progressiveStatus(100);
					if ($self.progressiveLoadStatusChanged != null) {
						$self.progressiveLoadStatusChanged($self, new $.ig.ProgressiveLoadStatusEventArgs($self.progressiveStatus()));
					}
				}
			}
		});
	}
	,
	bruteForceRender: function (view) {
		var xValues = this.xColumn().asArray().clone();
		this.xAxis().getScaledValueList(xValues, 0, xValues.length, this.__scalerParamsX);
		var yValues = this.yColumn().asArray().clone();
		this.__rendered = 0;
		var pointExtent = this.__pointExtent;
		var drawExtent = this.__drawExtent;
		var valLength = this.__values.length;
		this.yAxis().getScaledValueList(yValues, 0, yValues.length, this.__scalerParamsY);
		var color = 255 << 24 | $.ig.truncate(this.__minR) << 16 | $.ig.truncate(this.__minG) << 8 | $.ig.truncate(this.__minB);
		for (var i = 0; i < xValues.length; i++) {
			var posX = $.ig.truncate(xValues[i]);
			var posY = $.ig.truncate(yValues[i]);
			posX = $.ig.truncate(Math.floor(posX));
			posY = $.ig.truncate(Math.floor(posY));
			if (posX < 0 || posX >= this.__imageWidth || posY < 0 || posY >= this.__imageHeight) {
				continue;
			}
			if (drawExtent) {
				var minX = posX - pointExtent;
				var maxX = posX + pointExtent;
				maxX = maxX > (this.__imageWidth - 1) ? (this.__imageWidth - 1) : maxX;
				minX = minX < 0 ? 0 : minX;
				var maxY = posY + pointExtent;
				var minY = posY - pointExtent;
				for (var x = minX; x <= maxX; x++) {
					for (var y = minY; y <= maxY; y++) {
						var pos = (y * this.__imageWidth) + x;
						if (pos < 0 || pos > valLength - 1) {
							continue;
						}
						this.__values[pos] = this.__values[pos] + 1;
						this.__alphas[pos] = 1;
						this.__rendered++;
						if (this.__mouseOverEnabled) {
							this.__itemIndexes[pos] = i + 1;
						}
					}
				}
			} else {
				var pos1 = (posY * this.__imageWidth) + posX;
				this.__values[pos1] = this.__values[pos1] + 1;
				this.__alphas[pos1] = 1;
				this.__rendered++;
				if (this.__mouseOverEnabled) {
					this.__itemIndexes[pos1] = i + 1;
				}
			}
		}
		this.renderImage();
	}
	,
	__nodes: null,
	useTree: function (view) {
		var $self = this;
		var viewport;
		var window;
		var $ret = view.getViewInfo(viewport, window);
		viewport = $ret.p0;
		window = $ret.p1;
		if (this.__tree == null) {
			return;
		}
		if (this.__nodes == null) {
			this.__nodes = new $.ig.List$1($.ig.KDTreeNode2D.prototype.$type, 2, $.ig.truncate(Math.round(viewport.width() * viewport.height())));
		} else {
			this.__nodes.clear();
		}
		var minX = window.left();
		var maxX = window.right();
		var minY = window.top();
		var maxY = window.bottom();
		this.__windowTop = window.top();
		this.__windowHeight = window.height();
		this.__windowLeft = window.left();
		this.__windowWidth = window.width();
		this.__viewportTop = viewport.top();
		this.__viewportHeight = viewport.height();
		this.__viewportLeft = viewport.left();
		this.__viewportWidth = viewport.width();
		var effectiveViewport = this.getEffectiveViewport1(view);
		this.__effectiveViewportLeft = (effectiveViewport.left() - this.__viewportLeft) / this.__viewportWidth;
		this.__effectiveViewportTop = (effectiveViewport.top() - this.__viewportTop) / this.__viewportHeight;
		var effectiveRight = (effectiveViewport.right() - this.__viewportLeft) / this.__viewportWidth;
		this.__effectiveViewportWidth = effectiveRight - this.__effectiveViewportLeft;
		var effectiveBottom = (effectiveViewport.bottom() - this.__viewportTop) / this.__viewportHeight;
		this.__effectiveViewportHeight = effectiveBottom - this.__effectiveViewportTop;
		if (this.__hasEffectiveViewport) {
			minX = (minX - this.__effectiveViewportLeft) / this.__effectiveViewportWidth;
			maxX = (maxX - this.__effectiveViewportLeft) / this.__effectiveViewportWidth;
			minY = (minY - this.__effectiveViewportTop) / this.__effectiveViewportHeight;
			maxY = (maxY - this.__effectiveViewportTop) / this.__effectiveViewportHeight;
		}
		var onePixelX = this.resolution() / viewport.width() * window.width();
		var onePixelY = this.resolution() / viewport.height() * window.height();
		var pizelSize = Math.min(onePixelX, onePixelY);
		var args = (function () {
			var $ret = new $.ig.SearchArgs();
			$ret._minX = minX;
			$ret._maxX = maxX;
			$ret._minY = minY;
			$ret._maxY = maxY;
			$ret._pixelSizeX = onePixelX;
			$ret._pixelSizeY = onePixelY;
			$ret._maxRenderDepth = $self.maxRenderDepth();
			return $ret;
		}());
		this.__tree.getVisible(this.__nodes, args, 0, 1, 0, 1);
		var current;
		this.__rendered = 0;
		for (var i = 0; i < this.__nodes.count(); i++) {
			current = this.__nodes.__inner[i];
			this.renderNode(current);
		}
		this.renderImage();
	}
	,
	__heatMinimum: 0,
	__heatMaximum: 0,
	__heatRange: 0,
	__viewportTop: 0,
	__viewportHeight: 0,
	__viewportLeft: 0,
	__viewportWidth: 0,
	__windowLeft: 0,
	__windowWidth: 0,
	__windowTop: 0,
	__windowHeight: 0,
	__effectiveViewportLeft: 0,
	__effectiveViewportTop: 0,
	__effectiveViewportWidth: 0,
	__effectiveViewportHeight: 0,
	renderNode: function (current) {
		if (current._unfinished) {
			return;
		}
		var pixelCutoff = current._searchData != null && current._searchData._isCutoff;
		var otherCount = current._otherPoints == null ? 0 : current._otherPoints.length;
		var val = (current._descendantCount - otherCount) + 1;
		if (pixelCutoff && val > 0) {
		} else {
			val = 1;
		}
		this.renderPointData(current._median, val, pixelCutoff, current._searchData);
		if (otherCount > 0 && !pixelCutoff) {
			var other;
			for (var i = 0; i < otherCount; i++) {
				other = current._otherPoints[i];
				this.renderPointData(other, val, false, current._searchData);
			}
		}
		if (current._searchData != null) {
			current._searchData._isCutoff = false;
		}
	}
	,
	scrollIntoView: function (item) {
		var windowRect = this.seriesViewer() != null ? this.seriesViewer().actualWindowRect() : $.ig.Rect.prototype.empty();
		var viewportRect = this.seriesViewer() != null ? this.seriesViewer().viewportRect() : $.ig.Rect.prototype.empty();
		var unitRect = new $.ig.Rect(0, 0, 0, 1, 1);
		var effectiveViewportRect = this.getEffectiveViewportForUnitViewport(this.view());
		var xParams = new $.ig.ScalerParams(0, unitRect, unitRect, this.xAxis().isInverted(), effectiveViewportRect);
		var yParams = new $.ig.ScalerParams(0, unitRect, unitRect, this.yAxis().isInverted(), effectiveViewportRect);
		var index = !windowRect.isEmpty() && !viewportRect.isEmpty() && this.fastItemsSource() != null ? this.fastItemsSource().indexOf(item) : -1;
		var cx = this.xAxis() != null && this.xColumn() != null && index < this.xColumn().count() ? this.xAxis().getScaledValue(this.xColumn().item(index), xParams) : NaN;
		var cy = this.yAxis() != null && this.yColumn() != null && index < this.yColumn().count() ? this.yAxis().getScaledValue(this.yColumn().item(index), yParams) : NaN;
		if (!$.ig.util.isNaN(cx)) {
			if (cx < windowRect.left() + 0.1 * windowRect.width()) {
				cx = cx + 0.4 * windowRect.width();
				windowRect.x(cx - 0.5 * windowRect.width());
			}
			if (cx > windowRect.right() - 0.1 * windowRect.width()) {
				cx = cx - 0.4 * windowRect.width();
				windowRect.x(cx - 0.5 * windowRect.width());
			}
		}
		if (!$.ig.util.isNaN(cy)) {
			if (cy < windowRect.top() + 0.1 * windowRect.height()) {
				cy = cy + 0.4 * windowRect.height();
				windowRect.y(cy - 0.5 * windowRect.height());
			}
			if (cy > windowRect.bottom() - 0.1 * windowRect.height()) {
				cy = cy - 0.4 * windowRect.height();
				windowRect.y(cy - 0.5 * windowRect.height());
			}
		}
		if (this.syncLink() != null) {
			this.syncLink().windowNotify(this.seriesViewer(), windowRect);
		}
		return index >= 0;
	}
	,
	renderPointData: function (pointData, p, isCutoff, searchData) {
		var color = this.getColorFromValue(p);
		var index = pointData._index;
		var pointExtent = this.__pointExtent;
		if (isCutoff) {
			var minXVal = searchData._minX;
			var maxXVal = searchData._maxX;
			var minYVal = searchData._minY;
			var maxYVal = searchData._maxY;
			if (this.__hasEffectiveViewport) {
				minXVal = this.__effectiveViewportLeft + this.__effectiveViewportWidth * minXVal;
				maxXVal = this.__effectiveViewportLeft + this.__effectiveViewportWidth * maxXVal;
				minYVal = this.__effectiveViewportTop + this.__effectiveViewportHeight * minYVal;
				maxYVal = this.__effectiveViewportTop + this.__effectiveViewportHeight * maxYVal;
			}
			var minX = $.ig.truncate((this.__viewportLeft + this.__viewportWidth * (minXVal - this.__windowLeft) / this.__windowWidth));
			var maxX = $.ig.truncate((this.__viewportLeft + this.__viewportWidth * (maxXVal - this.__windowLeft) / this.__windowWidth));
			var minY = $.ig.truncate((this.__viewportTop + this.__viewportHeight * (minYVal - this.__windowTop) / this.__windowHeight));
			var maxY = $.ig.truncate((this.__viewportTop + this.__viewportHeight * (maxYVal - this.__windowTop) / this.__windowHeight));
			minX = $.ig.truncate(Math.floor(minX));
			maxX = $.ig.truncate(Math.floor(maxX));
			minY = $.ig.truncate(Math.floor(minY));
			maxY = $.ig.truncate(Math.floor(maxY));
			if (this.__drawExtent) {
				minX -= pointExtent;
				maxX += pointExtent;
				minY -= pointExtent;
				maxY += pointExtent;
			}
			var area = ((maxX - minX) + 1) * ((maxY - minY) + 1);
			var dens = p / area;
			var alpha = dens;
			for (var i = minX; i <= maxX; i++) {
				for (var j = minY; j <= maxY; j++) {
					this.renderPixel(index, i, j, dens, alpha);
				}
			}
		} else {
			var xVal = pointData._x;
			var yVal = pointData._y;
			if (this.__hasEffectiveViewport) {
				xVal = this.__effectiveViewportLeft + this.__effectiveViewportWidth * xVal;
				yVal = this.__effectiveViewportTop + this.__effectiveViewportHeight * yVal;
			}
			var posX = $.ig.truncate((this.__viewportLeft + this.__viewportWidth * (xVal - this.__windowLeft) / this.__windowWidth));
			var posY = $.ig.truncate((this.__viewportTop + this.__viewportHeight * (yVal - this.__windowTop) / this.__windowHeight));
			posX = $.ig.truncate(Math.floor(posX));
			posY = $.ig.truncate(Math.floor(posY));
			if (this.__drawExtent) {
				var minX1 = posX - pointExtent;
				var maxX1 = posX + pointExtent;
				var maxY1 = posY + pointExtent;
				var minY1 = posY - pointExtent;
				var area1 = ((maxX1 - minX1) + 1) * ((maxY1 - minY1) + 1);
				var dens1 = p / area1;
				var alpha1 = dens1;
				for (var i1 = minX1; i1 <= maxX1; i1++) {
					for (var j1 = minY1; j1 <= maxY1; j1++) {
						this.renderPixel(index, i1, j1, dens1, alpha1);
					}
				}
			} else {
				this.renderPixel(index, posX, posY, p, 1);
			}
		}
	}
	,
	getAlphaColorFromValue: function (p, alpha) {
		return $.ig.truncate((Math.floor(255 * alpha))) << 24 | $.ig.truncate(Math.floor(alpha * (this.__minR + p * this.__rangeR))) << 16 | $.ig.truncate(Math.floor(alpha * (this.__minG + p * this.__rangeG))) << 8 | $.ig.truncate(Math.floor(alpha * (this.__minB + p * this.__rangeB)));
	}
	,
	getColorFromValue: function (p) {
		return 255 << 24 | $.ig.truncate(Math.floor((this.__minR + p * this.__rangeR))) << 16 | $.ig.truncate(Math.floor((this.__minG + p * this.__rangeG))) << 8 | $.ig.truncate(Math.floor((this.__minB + p * this.__rangeB)));
	}
	,
	getValueFromColor: function (color) {
		return (color >> 24 & 255) / 255;
	}
	,
	renderPixel: function (index, posX, posY, p, alpha) {
		if (posX < 0 || posX >= this.__imageWidth || posY < 0 || posY >= this.__imageHeight) {
			return;
		}
		var pos = (posY * this.__imageWidth) + posX;
		this.__values[pos] = this.__values[pos] + p;
		this.__alphas[pos] = this.__alphas[pos] + alpha;
		if (this.__mouseOverEnabled) {
			this.__itemIndexes[pos] = index + 1;
		}
		this.__rendered += $.ig.truncate(p);
	}
	,
	renderImage: function () {
		var val = 0;
		var alpha = 0;
		var heatRange = this.__heatRange;
		var heatMinimum = this.__heatMinimum;
		var heatMaximum = this.__heatMaximum;
		var valuesLength = this.__values.length;
		var values = this.__values;
		var alphas = this.__alphas;
		var pixels = this.__pixels;
		var lastVal = NaN;
		var lastAlpha = NaN;
		var color = 0;
		var pixelPos = 0;
		for (var i = 0; i < valuesLength; i++) {
			val = values[i];
			if (val != 0) {
				alpha = alphas[i];
				if (val >= heatMaximum) {
					val = 1;
				} else if (val <= heatMinimum) {
					val = 0;
				} else {
					val = (val - heatMinimum) / (heatRange);
				}
				if (alpha > 1) {
					alpha = 1;
				}
				if (alpha < 0.2) {
					alpha = 0.2;
				}
				if (lastVal != val || lastAlpha != alpha) {
					color = this.getAlphaColorFromValue(val, alpha);
					lastVal = val;
					lastAlpha = alpha;
				}
				pixels[pixelPos] = color >> 16 & 255;
				pixels[pixelPos + 1] = color >> 8 & 255;
				pixels[pixelPos + 2] = color & 255;
				pixels[pixelPos + 3] = color >> 24 & 255;
			}
			pixelPos += 4;
		}
	}
	,
	__imageWidth: 0,
	__imageHeight: 0,
	__pixels: null,
	__rendered: 0,
	assertBitmap: function (view) {
		var hdView = view;
		hdView.assertBitmap();
	}
	,
	createView: function () {
		return new $.ig.HighDensityScatterSeriesView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.Series.prototype.onViewCreated.call(this, view);
		this.superView(view);
	}
	,
	_superView: null,
	superView: function (value) {
		if (arguments.length === 1) {
			this._superView = value;
			return value;
		} else {
			return this._superView;
		}
	}
	,
	getRange: function (axis) {
		if (axis != null && axis == this.xAxis() && this.xColumn() != null) {
			return new $.ig.AxisRange(this.xColumn().minimum(), this.xColumn().maximum());
		}
		if (axis != null && axis == this.yAxis() && this.yColumn() != null) {
			return new $.ig.AxisRange(this.yColumn().minimum(), this.yColumn().maximum());
		}
		return null;
	}
	,
	__hasEffectiveViewport: false,
	viewportRectChangedOverride: function (oldViewportRect, newViewportRect) {
		if (this.seriesViewer() != null) {
			var ev = this.getEffectiveViewport1(this.view());
			this.__hasEffectiveViewport = !ev.isEmpty();
		}
		this.renderSeries(false);
	}
	,
	windowRectChangedOverride: function (oldWindowRect, newWindowRect) {
		this.renderSeries(false);
	}
	,
	__itemIndexes: null,
	__values: null,
	__alphas: null,
	getItem: function (world) {
		if (!this.__mouseOverEnabled || this.__itemIndexes == null || this.seriesViewer() == null || this.fastItemsSource() == null || this.__itemIndexes.length != (this.__imageWidth * this.__imageHeight)) {
			return null;
		}
		var windowRect = this.seriesViewer().actualWindowRect();
		var windowX = (world.__x - windowRect.left()) / windowRect.width();
		var windowY = (world.__y - windowRect.top()) / windowRect.height();
		var pixelX = $.ig.truncate(Math.round(this.viewport().left() + (this.viewport().width() * windowX)));
		var pixelY = $.ig.truncate(Math.round(this.viewport().top() + (this.viewport().height() * windowY)));
		var index = this.tryGetIndex(pixelX, pixelY);
		if (index < 0 || index > this.__itemIndexes.length - 1) {
			return null;
		}
		var itemIndex = this.__itemIndexes[index] - 1;
		if (itemIndex < 0 || itemIndex > this.fastItemsSource().count()) {
			return null;
		}
		return this.fastItemsSource().item(itemIndex);
	}
	,
	tryGetIndex: function (pixelX, pixelY) {
		var index = (this.__imageWidth * pixelY) + pixelX;
		if (index > 0 && index < this.__itemIndexes.length && this.__itemIndexes[index] > 0) {
			return index;
		}
		var dist = 1;
		index = (this.__imageWidth * (pixelY - dist)) + pixelX;
		if (index > 0 && index < this.__itemIndexes.length && this.__itemIndexes[index] > 0) {
			return index;
		}
		index = (this.__imageWidth * (pixelY + dist)) + pixelX;
		if (index > 0 && index < this.__itemIndexes.length && this.__itemIndexes[index] > 0) {
			return index;
		}
		index = (this.__imageWidth * (pixelY)) + pixelX - 1;
		if (index > 0 && index < this.__itemIndexes.length && this.__itemIndexes[index] > 0) {
			return index;
		}
		index = (this.__imageWidth * (pixelY)) + pixelX + 1;
		if (index > 0 && index < this.__itemIndexes.length && this.__itemIndexes[index] > 0) {
			return index;
		}
		index = (this.__imageWidth * (pixelY - dist)) + pixelX - 1;
		if (index > 0 && index < this.__itemIndexes.length && this.__itemIndexes[index] > 0) {
			return index;
		}
		index = (this.__imageWidth * (pixelY + dist)) + pixelX + 1;
		if (index > 0 && index < this.__itemIndexes.length && this.__itemIndexes[index] > 0) {
			return index;
		}
		index = (this.__imageWidth * (pixelY - dist)) + pixelX + 1;
		if (index > 0 && index < this.__itemIndexes.length && this.__itemIndexes[index] > 0) {
			return index;
		}
		index = (this.__imageWidth * (pixelY + dist)) + pixelX - 1;
		if (index > 0 && index < this.__itemIndexes.length && this.__itemIndexes[index] > 0) {
			return index;
		}
		return 0;
	}
	,
	updateImageValues: function (pixels, imageWidth, imageHeight) {
		this.__pixels = pixels;
		this.__imageWidth = imageWidth;
		this.__imageHeight = imageHeight;
	}
	,
	useDeferredMouseEnterAndLeave: function (value) {
		if (arguments.length === 1) {
			$.ig.Series.prototype.useDeferredMouseEnterAndLeave.call(this, value);
			return value;
		} else {
			return true;
		}
	}
	,
	clearRendering: function (wipeClean, view) {
		$.ig.Series.prototype.clearRendering.call(this, wipeClean, view);
		this.superView().clearBitmap();
		if (this.thumbnailView() != null) {
			(this.thumbnailView()).clearBitmap();
		}
		this.notifyThumbnailAppearanceChanged();
	}
	,
	renderThumbnail: function (viewportRect, surface) {
		$.ig.Series.prototype.renderThumbnail.call(this, viewportRect, surface);
		if (!this.thumbnailDirty()) {
			this.view().prepSurface(surface);
			return;
		}
		this.view().prepSurface(surface);
		if (this.clearAndAbortIfInvalid1(this.thumbnailView())) {
			return;
		}
		var thumbnailView = $.ig.util.cast($.ig.HighDensityScatterSeriesView.prototype.$type, this.thumbnailView());
		var frame = new $.ig.ScatterFrame();
		this.doRender(false, thumbnailView);
		this.thumbnailDirty(false);
	}
	,
	_alternateFrame: null,
	alternateFrame: function (value) {
		if (arguments.length === 1) {
			this._alternateFrame = value;
			return value;
		} else {
			return this._alternateFrame;
		}
	}
	,
	renderAlternateView: function (viewportRect, windowRect, surface, viewIdentifier, effectiveScalingRatio) {
		$.ig.Series.prototype.renderAlternateView.call(this, viewportRect, windowRect, surface, viewIdentifier, effectiveScalingRatio);
		var view = this.alternateViews().item(viewIdentifier);
		var highDensityScatterSeriesView = view;
		view.prepAltSurface(surface);
		if (this.clearAndAbortIfInvalid1(highDensityScatterSeriesView)) {
			return;
		}
		if (this.alternateFrame() == null) {
			this.alternateFrame(new $.ig.ScatterFrame());
		}
		this.doRender(false, view);
	}
	,
	$type: new $.ig.Type('HighDensityScatterSeries', $.ig.Series.prototype.$type)
}, true);

$.ig.util.defType('ProgressiveLoadStatusEventArgs', 'EventArgs', {
	_currentStatus: 0,
	currentStatus: function (value) {
		if (arguments.length === 1) {
			this._currentStatus = value;
			return value;
		} else {
			return this._currentStatus;
		}
	}
	,
	init: function (currentStatus) {
		$.ig.EventArgs.prototype.init.call(this);
		this.currentStatus(currentStatus);
	},
	$type: new $.ig.Type('ProgressiveLoadStatusEventArgs', $.ig.EventArgs.prototype.$type)
}, true);

$.ig.util.defType('HighDensityScatterSeriesView', 'SeriesView', {
	init: function (model) {
		$.ig.SeriesView.prototype.init.call(this, model);
		this.highDensityScatterModel(model);
	},
	onInit: function () {
		$.ig.SeriesView.prototype.onInit.call(this);
		if (!this.isThumbnailView()) {
			this.model().resolution(4);
			this.highDensityScatterModel().heatMinimumColor($.ig.Color.prototype.fromArgb(255, 0, 0, 0));
			this.highDensityScatterModel().heatMaximumColor($.ig.Color.prototype.fromArgb(255, 255, 0, 0));
		}
	}
	,
	updateImageValues: function () {
		this.highDensityScatterModel().updateImageValues(this.__pixels, this.__imageWidth, this.__imageHeight);
	}
	,
	_highDensityScatterModel: null,
	highDensityScatterModel: function (value) {
		if (arguments.length === 1) {
			this._highDensityScatterModel = value;
			return value;
		} else {
			return this._highDensityScatterModel;
		}
	}
	,
	__pixels: null,
	__imageWidth: 0,
	__imageHeight: 0,
	_offscreen: null,
	offscreen: function (value) {
		if (arguments.length === 1) {
			this._offscreen = value;
			return value;
		} else {
			return this._offscreen;
		}
	}
	,
	_imageData: null,
	imageData: function (value) {
		if (arguments.length === 1) {
			this._imageData = value;
			return value;
		} else {
			return this._imageData;
		}
	}
	,
	_offscreenContext: null,
	offscreenContext: function (value) {
		if (arguments.length === 1) {
			this._offscreenContext = value;
			return value;
		} else {
			return this._offscreenContext;
		}
	}
	,
	assertBitmap: function () {
		if (this.offscreen() == null) {
			this.offscreen($("<canvas></canvas>"));
			var cont = (this.offscreen()[0]).getContext("2d");
			this.offscreenContext(new $.ig.RenderingContext(new $.ig.CanvasViewRenderer(), cont));
		}
		var rect;
		if (this.isAlternateView()) {
			rect = this.viewport();
		} else {
			rect = this.model().seriesViewer().getContainerRect();
		}
		var viewportLeft = $.ig.truncate(Math.round(rect.left()));
		var viewportTop = $.ig.truncate(Math.round(rect.top()));
		var viewportWidth = $.ig.truncate(Math.round(rect.width()));
		var viewportHeight = $.ig.truncate(Math.round(rect.height()));
		var lastImageWidth = this.__imageWidth;
		var lastImageHeight = this.__imageHeight;
		this.__imageWidth = viewportWidth;
		this.__imageHeight = viewportHeight;
		if (lastImageWidth != this.__imageWidth || lastImageHeight != this.__imageHeight || this.__pixels == null) {
			this.offscreen().attr("width", viewportWidth.toString());
			this.offscreen().attr("height", viewportHeight.toString());
			this.imageData((this.offscreenContext().getUnderlyingContext()).getImageData(0, 0, viewportWidth, viewportHeight));
			this.__pixels = this.imageData().data;
		}
		this.highDensityScatterModel().updateImageValues(this.__pixels, this.__imageWidth, this.__imageHeight);
	}
	,
	getPixelBuffer: function () {
		return this.__pixels;
	}
	,
	updateBitmap: function () {
		var viewportLeft = $.ig.truncate(Math.round(this.viewport().left()));
		var viewportTop = $.ig.truncate(Math.round(this.viewport().left()));
		var viewportWidth = $.ig.truncate(Math.round(this.viewport().width()));
		var viewportHeight = $.ig.truncate(Math.round(this.viewport().height()));
		(this.offscreenContext().getUnderlyingContext()).putImageData(this.imageData(), 0, 0);
		this.makeDirty();
	}
	,
	defer: function (action) {
		window.setTimeout(action, 0);
	}
	,
	renderOverride: function (context, isHitContext) {
		$.ig.SeriesView.prototype.renderOverride.call(this, context, isHitContext);
		if (isHitContext) {
			if (!this.highDensityScatterModel().mouseOverEnabled()) {
				return;
			}
			var rect = new $.ig.Rectangle();
			rect.canvasLeft(this.viewport().left());
			rect.canvasTop(this.viewport().top());
			rect.width(this.viewport().width());
			rect.height(this.viewport().height());
			rect.__fill = this.getHitBrush();
			context.renderRectangle(rect);
			return;
		}
		if (this.offscreen() == null) {
			return;
		}
		var viewportLeft = $.ig.truncate(Math.round(this.viewport().left()));
		var viewportTop = $.ig.truncate(Math.round(this.viewport().top()));
		var viewportWidth = $.ig.truncate(Math.round(this.viewport().width()));
		var viewportHeight = $.ig.truncate(Math.round(this.viewport().height()));
		this.context().drawImage1(this.offscreen()[0], this.model().__opacity, viewportLeft, viewportTop, viewportWidth, viewportHeight, viewportLeft, viewportTop, viewportWidth, viewportHeight);
	}
	,
	colorScaleValid: function (HeatMinimumColor, HeatMaximumColor) {
		return $.ig.Color.prototype.l_op_Inequality_Lifted($.ig.util.toNullable($.ig.Color.prototype.$type, HeatMinimumColor), $.ig.util.toNullable($.ig.Color.prototype.$type, null)) && $.ig.Color.prototype.l_op_Inequality_Lifted($.ig.util.toNullable($.ig.Color.prototype.$type, HeatMaximumColor), $.ig.util.toNullable($.ig.Color.prototype.$type, null));
	}
	,
	clearBitmap: function () {
		if (this.__pixels != null) {
			for (var i = 0; i < this.__pixels.length; i++) {
				this.__pixels[i] = 0;
			}
			this.updateBitmap();
		}
	}
	,
	exportViewShapes: function (svd) {
		$.ig.SeriesView.prototype.exportViewShapes.call(this, svd);
		if (this.__pixels == null) {
			svd.pixels(null);
			return;
		}
		var packed = new Array($.ig.intDivide(this.__pixels.length, 4));
		var pos;
		for (var i = 0; i < $.ig.intDivide(this.__pixels.length, 4); i++) {
			pos = i * 4;
			packed[i] = this.__pixels[pos + 3] << 24 | this.__pixels[pos] << 16 | this.__pixels[pos + 1] << 8 | this.__pixels[pos + 2];
		}
		svd.pixels(packed);
		svd.pixelWidth(this.__imageWidth);
	}
	,
	$type: new $.ig.Type('HighDensityScatterSeriesView', $.ig.SeriesView.prototype.$type)
}, true);

$.ig.util.defType('KDTree2D', 'Object', {
	init: function (initNumber, points, maxLeafSize) {
		if (initNumber > 0) {
			switch (initNumber) {
				case 1:
					this.init1.apply(this, arguments);
					break;
			}
			return;
		}
		this.__currentProgressiveLevel = 0;
		this.__lock = {};
		this.__rand = new $.ig.Random(0);
		$.ig.Object.prototype.init.call(this);
		this.root(this.kDTreeHelper(points, 0, points.length - 1, 0, maxLeafSize));
	},
	init1: function (initNumber) {
		this.__currentProgressiveLevel = 0;
		this.__lock = {};
		this.__rand = new $.ig.Random(0);
		$.ig.Object.prototype.init.call(this);
	},
	__progressivePoints: null,
	__progressiveThunks: null,
	__toProcess: null,
	__currentProgressiveLevel: 0,
	__lock: null,
	syncLock: function () {
		return this.__lock;
	}
	,
	progressiveThunkCompleted: null,
	getProgressive: function (points, maxLeafSize) {
		var ret = new $.ig.KDTree2D(1);
		ret.root(new $.ig.KDTreeNode2D());
		ret.root()._unfinished = true;
		ret.__progressivePoints = points;
		ret.__progressiveThunks = new $.ig.Stack$1($.ig.KDTreeThunk.prototype.$type);
		ret.__toProcess = new $.ig.List$1($.ig.KDTreeThunk.prototype.$type, 0);
		var thunk = new $.ig.KDTreeThunk();
		thunk._startIndex = 0;
		thunk._endIndex = points.length - 1;
		thunk._level = 0;
		thunk._maxLeafSize = maxLeafSize;
		thunk._node = ret.root();
		ret.__progressiveThunks.push(thunk);
		return ret;
	}
	,
	progressiveStep: function () {
		if (this.__progressiveThunks.count() == 0 && this.__toProcess.count() == 0) {
			this.__progressivePoints = null;
			return false;
		}
		if (this.__progressiveThunks.count() == 0 && this.__toProcess.count() > 0) {
			return true;
		}
		this.__currentProgressiveLevel = this.__progressiveThunks.peek()._level;
		while (this.__progressiveThunks.count() > 0 && this.__progressiveThunks.peek()._level == this.__currentProgressiveLevel) {
			this.__toProcess.add(this.__progressiveThunks.pop());
		}
		window.setTimeout(this.processThunks.runOn(this), 0);
		return true;
	}
	,
	__isCancelled: false,
	cancelLoad: function () {
		this.__isCancelled = true;
	}
	,
	processThunks: function () {
		var t;
		for (var i = 0; i < this.__toProcess.count(); i++) {
			if (this.__isCancelled || this.__progressivePoints == null) {
				return;
			}
			if (this.__progressivePoints.length == 0) {
				continue;
			}
			t = this.__toProcess.__inner[i];
			this.kDTreeHelperProgressive(t._node, this.__progressivePoints, t._startIndex, t._endIndex, t._level, t._maxLeafSize);
		}
		this.__toProcess.clear();
		if (this.progressiveThunkCompleted != null) {
			this.progressiveThunkCompleted(this, new $.ig.EventArgs());
		}
	}
	,
	kDTreeHelperProgressive: function (node, points, startIndex, endIndex, level, maxLeafSize) {
		node._unfinished = false;
		node._isX = (level % 2) == 0;
		node._descendantCount = (endIndex - startIndex);
		if (startIndex == endIndex) {
			node._median = points[startIndex];
			return;
		}
		if (startIndex > endIndex) {
			node._unfinished = true;
			return;
		}
		if ((endIndex - startIndex) + 1 <= maxLeafSize) {
			node._median = points[startIndex];
			node._otherPoints = new Array((endIndex - startIndex) + 1);
			var j = 0;
			for (var i = startIndex; i <= endIndex; i++) {
				node._otherPoints[j++] = points[i];
			}
			return;
		}
		var k = Math.max($.ig.intDivide((endIndex - startIndex), 2), 1);
		var medianIndex = this.select(points, startIndex, endIndex, node._isX, k);
		node._median = points[medianIndex];
		if (startIndex <= medianIndex - 1) {
			node._leftChild = (function () {
				var $ret = new $.ig.KDTreeNode2D();
				$ret._unfinished = true;
				return $ret;
			}());
			node._leftChild._descendantCount = ((medianIndex - 1) - startIndex) + 1;
			this.__progressiveThunks.push((function () {
				var $ret = new $.ig.KDTreeThunk();
				$ret._startIndex = startIndex;
				$ret._endIndex = medianIndex - 1;
				$ret._level = level + 1;
				$ret._maxLeafSize = maxLeafSize;
				$ret._node = node._leftChild;
				return $ret;
			}()));
		} else {
			node._leftChild = null;
		}
		if (medianIndex + 1 <= endIndex) {
			node._rightChild = (function () {
				var $ret = new $.ig.KDTreeNode2D();
				$ret._unfinished = true;
				return $ret;
			}());
			node._rightChild._descendantCount = (endIndex - (medianIndex + 1)) + 1;
			this.__progressiveThunks.push((function () {
				var $ret = new $.ig.KDTreeThunk();
				$ret._startIndex = medianIndex + 1;
				$ret._endIndex = endIndex;
				$ret._level = level + 1;
				$ret._maxLeafSize = maxLeafSize;
				$ret._node = node._rightChild;
				return $ret;
			}()));
		} else {
			node._rightChild = null;
		}
	}
	,
	kDTreeHelper: function (points, startIndex, endIndex, level, maxLeafSize) {
		var node = new $.ig.KDTreeNode2D();
		node._isX = (level % 2) == 0;
		node._descendantCount = (endIndex - startIndex);
		if (startIndex == endIndex) {
			node._median = points[startIndex];
			return node;
		}
		if (startIndex > endIndex) {
			return null;
		}
		if ((endIndex - startIndex) + 1 <= maxLeafSize) {
			node._median = points[startIndex];
			node._otherPoints = new Array(endIndex - startIndex + 1);
			var j = 0;
			for (var i = startIndex; i <= endIndex; i++) {
				node._otherPoints[j++] = points[i];
			}
			return node;
		}
		var k = Math.max($.ig.intDivide((endIndex - startIndex), 2), 1);
		var medianIndex = this.select(points, startIndex, endIndex, node._isX, k);
		node._median = points[medianIndex];
		node._leftChild = this.kDTreeHelper(points, startIndex, medianIndex - 1, level + 1, maxLeafSize);
		node._rightChild = this.kDTreeHelper(points, medianIndex + 1, endIndex, level + 1, maxLeafSize);
		return node;
	}
	,
	partition: function (points, isX, startIndex, endIndex, pivotIndex) {
		var pivotValue = isX ? points[pivotIndex]._x : points[pivotIndex]._y;
		var temp = points[pivotIndex];
		points[pivotIndex] = points[endIndex];
		points[endIndex] = temp;
		var storeIndex = startIndex;
		for (var i = startIndex; i < endIndex; i++) {
			var val;
			if (isX) {
				val = points[i]._x;
			} else {
				val = points[i]._y;
			}
			if (val <= pivotValue) {
				temp = points[storeIndex];
				points[storeIndex] = points[i];
				points[i] = temp;
				storeIndex++;
			}
		}
		temp = points[endIndex];
		points[endIndex] = points[storeIndex];
		points[storeIndex] = temp;
		return storeIndex;
	}
	,
	__rand: null,
	select: function (points, startIndex, endIndex, isX, k) {
		if (startIndex == endIndex) {
			return startIndex;
		}
		var pivotIndex = this.__rand.next2(startIndex, endIndex);
		var newIndex = this.partition(points, isX, startIndex, endIndex, pivotIndex);
		var pivotDistance = newIndex - startIndex + 1;
		if (pivotDistance == k) {
			return newIndex;
		} else if (k < pivotDistance) {
			return this.select(points, startIndex, newIndex - 1, isX, k);
		} else {
			return this.select(points, newIndex + 1, endIndex, isX, k - pivotDistance);
		}
	}
	,
	kNearest: function (results, xValue, yValue, k) {
		this.kNearestHelper(results, xValue, yValue, k, this.root());
	}
	,
	kNearestHelper: function (results, xValue, yValue, k, current) {
		if (current == null || current._unfinished) {
			return;
		}
		if (current._leftChild == null && current._rightChild == null) {
			this.addNearest(results, xValue, yValue, current, current._median, true, 0, k);
			if (results._breakOut) {
				return;
			}
			if (current._otherPoints != null && current._otherPoints.length > 0) {
				for (var i = 0; i < current._otherPoints.length; i++) {
					this.addNearest(results, xValue, yValue, current, current._otherPoints[i], false, i, k);
					if (results._breakOut) {
						return;
					}
				}
			}
			return;
		}
		this.addNearest(results, xValue, yValue, current, current._median, true, 0, k);
		if (results._breakOut) {
			return;
		}
		if (current._isX) {
			if (xValue <= current._median._x) {
				this.kNearestHelper(results, xValue, yValue, k, current._leftChild);
				if (results._breakOut) {
					return;
				}
				if (this.dist(xValue, yValue, current._median._x, yValue) < results._currentFurthestDist) {
					this.kNearestHelper(results, xValue, yValue, k, current._rightChild);
				}
				if (results._breakOut) {
					return;
				}
			} else {
				this.kNearestHelper(results, xValue, yValue, k, current._rightChild);
				if (results._breakOut) {
					return;
				}
				if (this.dist(xValue, yValue, current._median._x, yValue) < results._currentFurthestDist) {
					this.kNearestHelper(results, xValue, yValue, k, current._leftChild);
				}
				if (results._breakOut) {
					return;
				}
			}
		} else {
			if (yValue <= current._median._y) {
				this.kNearestHelper(results, xValue, yValue, k, current._leftChild);
				if (results._breakOut) {
					return;
				}
				if (this.dist(xValue, yValue, xValue, current._median._y) < results._currentFurthestDist) {
					this.kNearestHelper(results, xValue, yValue, k, current._rightChild);
				}
				if (results._breakOut) {
					return;
				}
			} else {
				this.kNearestHelper(results, xValue, yValue, k, current._rightChild);
				if (results._breakOut) {
					return;
				}
				if (this.dist(xValue, yValue, xValue, current._median._y) < results._currentFurthestDist) {
					this.kNearestHelper(results, xValue, yValue, k, current._leftChild);
				}
				if (results._breakOut) {
					return;
				}
			}
		}
	}
	,
	addNearest: function (results, xValue, yValue, current, pointData, isMedian, index, k) {
		if (results._breakOut) {
			return;
		}
		if (results._consideredCount > results._consideredCutoff) {
			results._breakOut = true;
			return;
		}
		if (results._results.count() < k) {
			if ($.ig.util.isNaN(results._currentNearestDist)) {
				results._currentNearestDist = this.dist(xValue, yValue, pointData._x, pointData._y);
				results._currentFurthestDist = results._currentNearestDist;
				results._currentFurthestIndex = 0;
			}
			results._results.add((function () {
				var $ret = new $.ig.KNearestResult();
				$ret._isMedian = isMedian;
				$ret._index = index;
				$ret._node = current;
				$ret._x = pointData._x;
				$ret._y = pointData._y;
				return $ret;
			}()));
			results._consideredCount++;
			var dist = this.dist(xValue, yValue, pointData._x, pointData._y);
			if (dist < results._currentNearestDist) {
				results._currentNearestDist = dist;
			}
			if (dist > results._currentFurthestDist) {
				results._currentFurthestDist = dist;
				results._currentFurthestIndex = results._results.count() - 1;
			}
			return;
		}
		var newDist = 0;
		if (newDist < results._currentFurthestDist) {
			if (newDist < results._currentNearestDist) {
				results._currentNearestDist = newDist;
			}
			results._results.__inner[results._currentFurthestIndex] = (function () {
				var $ret = new $.ig.KNearestResult();
				$ret._isMedian = isMedian;
				$ret._index = index;
				$ret._node = current;
				$ret._x = pointData._x;
				$ret._y = pointData._y;
				return $ret;
			}());
			var maxDist = 0;
			var maxIndex = 0;
			for (var i = 0; i < results._results.count(); i++) {
				var currDist = this.dist(xValue, yValue, results._results.__inner[i]._x, results._results.__inner[i]._y);
				if (currDist > maxDist) {
					maxDist = currDist;
					maxIndex = i;
				}
			}
			results._consideredCount++;
		}
	}
	,
	dist: function (xValue, yValue, xValue2, yValue2) {
		return (xValue - xValue2) * (xValue - xValue2) + (yValue - yValue2) * (yValue - yValue2);
	}
	,
	getVisible: function (nodes, args, xMinimum, xMaximum, yMinimum, yMaximum) {
		this.getVisibleHelper(nodes, this.root(), args, xMinimum, xMaximum, yMinimum, yMaximum, false, 0);
	}
	,
	getVisibleHelper: function (nodes, currentNode, args, currentXMinimum, currentXMaximum, currentYMinimum, currentYMaximum, report, depth) {
		if (currentNode == null) {
			return;
		}
		var sd = currentNode._searchData;
		if (depth > args._maxRenderDepth || ((currentYMaximum - currentYMinimum) < args._pixelSizeY && (currentXMaximum - currentXMinimum) < args._pixelSizeX)) {
			if (currentNode._searchData == null) {
				currentNode._searchData = new $.ig.SearchData();
			}
			sd = currentNode._searchData;
			sd._isCutoff = true;
			sd._minX = currentXMinimum;
			sd._maxX = currentXMaximum;
			sd._minY = currentYMinimum;
			sd._maxY = currentYMaximum;
			nodes.add(currentNode);
			return;
		}
		if (sd != null) {
			sd._isCutoff = false;
		}
		if (currentNode._leftChild == null && currentNode._rightChild == null) {
			nodes.add(currentNode);
			return;
		}
		var leftXMinimum;
		var leftXMaximum;
		var leftYMinimum;
		var leftYMaximum;
		var rightXMinimum;
		var rightXMaximum;
		var rightYMinimum;
		var rightYMaximum;
		if (currentNode._isX) {
			leftXMinimum = currentXMinimum;
			leftXMaximum = currentNode._median._x;
			leftYMinimum = currentYMinimum;
			leftYMaximum = currentYMaximum;
			rightXMinimum = currentNode._median._x;
			rightXMaximum = currentXMaximum;
			rightYMinimum = currentYMinimum;
			rightYMaximum = currentYMaximum;
		} else {
			leftXMinimum = currentXMinimum;
			leftXMaximum = currentXMaximum;
			leftYMinimum = currentYMinimum;
			leftYMaximum = currentNode._median._y;
			rightXMinimum = currentXMinimum;
			rightXMaximum = currentXMaximum;
			rightYMinimum = currentNode._median._y;
			rightYMaximum = currentYMaximum;
		}
		if (report) {
			nodes.add(currentNode);
			this.getVisibleHelper(nodes, currentNode._leftChild, args, leftXMinimum, leftXMaximum, leftYMinimum, leftYMaximum, true, depth + 1);
			this.getVisibleHelper(nodes, currentNode._rightChild, args, rightXMinimum, rightXMaximum, rightYMinimum, rightYMaximum, true, depth + 1);
		} else {
			var addedCurrent = false;
			if (leftXMinimum >= args._minX && leftXMaximum <= args._maxX && leftYMinimum >= args._minY && leftYMaximum <= args._maxY) {
				addedCurrent = true;
				nodes.add(currentNode);
				this.getVisibleHelper(nodes, currentNode._leftChild, args, leftXMinimum, leftXMaximum, leftYMinimum, leftYMaximum, true, depth + 1);
			} else if (!(args._minX > leftXMaximum || args._maxX < leftXMinimum || args._minY > leftYMaximum || args._maxY < leftYMinimum)) {
				addedCurrent = true;
				nodes.add(currentNode);
				this.getVisibleHelper(nodes, currentNode._leftChild, args, leftXMinimum, leftXMaximum, leftYMinimum, leftYMaximum, false, depth + 1);
			}
			if (rightXMinimum >= args._minX && rightXMaximum <= args._maxX && rightYMinimum >= args._minY && rightYMaximum <= args._maxY) {
				if (!addedCurrent) {
					nodes.add(currentNode);
				}
				this.getVisibleHelper(nodes, currentNode._rightChild, args, rightXMinimum, rightXMaximum, rightYMinimum, rightYMaximum, true, depth + 1);
			} else if (!(args._minX > rightXMaximum || args._maxX < rightXMinimum || args._minY > rightYMaximum || args._maxY < rightYMinimum)) {
				if (!addedCurrent) {
					nodes.add(currentNode);
				}
				this.getVisibleHelper(nodes, currentNode._rightChild, args, rightXMinimum, rightXMaximum, rightYMinimum, rightYMaximum, false, depth + 1);
			}
		}
	}
	,
	_root: null,
	root: function (value) {
		if (arguments.length === 1) {
			this._root = value;
			return value;
		} else {
			return this._root;
		}
	}
	,
	validate: function () {
		this.validateHelper(this.root());
	}
	,
	validateHelper: function (Root) {
		if (Root == null) {
			return 0;
		}
		if (this.validateHelper(Root._leftChild) + this.validateHelper(Root._rightChild) != Root._descendantCount) {
			var a = 0;
		}
		return Root._descendantCount + 1;
	}
	,
	$type: new $.ig.Type('KDTree2D', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('KNearestResults', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	_consideredCount: 0,
	_consideredCutoff: 0,
	_results: null,
	_breakOut: false,
	_currentNearestDist: 0,
	_currentFurthestDist: 0,
	_currentFurthestIndex: 0,
	$type: new $.ig.Type('KNearestResults', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('KNearestResult', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	_index: 0,
	_isMedian: false,
	_x: 0,
	_y: 0,
	_node: null,
	$type: new $.ig.Type('KNearestResult', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('KDTreeThunk', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	_startIndex: 0,
	_endIndex: 0,
	_level: 0,
	_maxLeafSize: 0,
	_node: null,
	$type: new $.ig.Type('KDTreeThunk', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('SearchArgs', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	_minX: 0,
	_minY: 0,
	_maxX: 0,
	_maxY: 0,
	_pixelSizeX: 0,
	_pixelSizeY: 0,
	_maxRenderDepth: 0,
	$type: new $.ig.Type('SearchArgs', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('KDTreeNode2D', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	_unfinished: false,
	_isX: false,
	_descendantCount: 0,
	_median: null,
	_leftChild: null,
	_rightChild: null,
	_otherPoints: null,
	_searchData: null,
	$type: new $.ig.Type('KDTreeNode2D', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('SearchData', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	_isCutoff: false,
	_minX: 0,
	_maxX: 0,
	_minY: 0,
	_maxY: 0,
	$type: new $.ig.Type('SearchData', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('KDPointData', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	_x: 0,
	_y: 0,
	_index: 0,
	$type: new $.ig.Type('KDPointData', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('MarkerManagerBase', 'Object', {
	_getItemLocationsStrategy: null,
	getItemLocationsStrategy: function (value) {
		if (arguments.length === 1) {
			this._getItemLocationsStrategy = value;
			return value;
		} else {
			return this._getItemLocationsStrategy;
		}
	}
	,
	_provideMarkerStrategy: null,
	provideMarkerStrategy: function (value) {
		if (arguments.length === 1) {
			this._provideMarkerStrategy = value;
			return value;
		} else {
			return this._provideMarkerStrategy;
		}
	}
	,
	_removeUnusedMarkers: null,
	removeUnusedMarkers: function (value) {
		if (arguments.length === 1) {
			this._removeUnusedMarkers = value;
			return value;
		} else {
			return this._removeUnusedMarkers;
		}
	}
	,
	_provideItemStrategy: null,
	provideItemStrategy: function (value) {
		if (arguments.length === 1) {
			this._provideItemStrategy = value;
			return value;
		} else {
			return this._provideItemStrategy;
		}
	}
	,
	_activeMarkerIndexesStrategy: null,
	activeMarkerIndexesStrategy: function (value) {
		if (arguments.length === 1) {
			this._activeMarkerIndexesStrategy = value;
			return value;
		} else {
			return this._activeMarkerIndexesStrategy;
		}
	}
	,
	_useDeterministicSelection: false,
	useDeterministicSelection: function (value) {
		if (arguments.length === 1) {
			this._useDeterministicSelection = value;
			return value;
		} else {
			return this._useDeterministicSelection;
		}
	}
	,
	init: function (provideMarkerStrategy, provideItemStrategy, removeUnusedMarkers, getItemLocationsStrategy, activeMarkerIndexesStrategy) {
		$.ig.Object.prototype.init.call(this);
		this.provideMarkerStrategy(provideMarkerStrategy);
		this.provideItemStrategy(provideItemStrategy);
		this.removeUnusedMarkers(removeUnusedMarkers);
		this.getItemLocationsStrategy(getItemLocationsStrategy);
		this.activeMarkerIndexesStrategy(activeMarkerIndexesStrategy);
	},
	winnowMarkers: function (markers, maximumMarkers, windowRect, viewportRect, currentResolution) {
	}
	,
	render: function (markers, lightweight) {
	}
	,
	activeFirstKeys: function (buckets, keys) {
		var first = new $.ig.List$1($.ig.Number.prototype.$type, 0);
		var second = new $.ig.List$1($.ig.Number.prototype.$type, 0);
		var en = keys.getEnumerator();
		while (en.moveNext()) {
			var key = en.current();
			if (buckets.item(key).priorityItems().count() > 0) {
				first.add(key);
			} else {
				second.add(key);
			}
		}
		var ret = new $.ig.List$1($.ig.Number.prototype.$type, 0);
		ret.addRange(first);
		ret.addRange(second);
		return ret;
	}
	,
	selectMarkerItems: function (numToSelect, buckets, keys, markerItems) {
		while (numToSelect > 0) {
			if (numToSelect < keys.count()) {
				if (!$.ig.MarkerManagerBase.prototype.useDeterministicSelection()) {
					var ikeys = keys;
					$.ig.ArrayUtil.prototype.shuffle$1($.ig.Number.prototype.$type, ikeys);
				}
				keys = this.activeFirstKeys(buckets, keys);
				var count = numToSelect;
				for (var i = 0; i < count; i++) {
					var keyIndex = i;
					var bucket = buckets.item(keys.__inner[keyIndex]);
					var wasPriority;
					var index = (function () { var $ret = bucket.getItem(wasPriority); wasPriority = $ret.p0; return $ret.ret; }());
					markerItems.add(index);
					numToSelect--;
					if (bucket.isEmpty()) {
						buckets.remove(keys.__inner[keyIndex]);
					}
				}
			} else {
				var en = keys.getEnumerator();
				while (en.moveNext()) {
					var key = en.current();
					var bucket1 = buckets.item(key);
					var wasPriority1;
					var index1 = (function () { var $ret = bucket1.getItem(wasPriority1); wasPriority1 = $ret.p0; return $ret.ret; }());
					markerItems.add(index1);
					numToSelect--;
					if (bucket1.isEmpty()) {
						buckets.remove(key);
					}
				}
				keys = new $.ig.List$1($.ig.Number.prototype.$type, 1, buckets.keys());
			}
		}
	}
	,
	getVisibleItems: function (windowRect, viewportRect, itemLocations, visibleItems) {
		var left = viewportRect.left();
		var right = viewportRect.right();
		var top = viewportRect.top();
		var bottom = viewportRect.bottom();
		if (!windowRect.isEmpty() && !viewportRect.isEmpty()) {
			for (var i = 0; i < itemLocations.count(); ++i) {
				var x = itemLocations.item(i).__x;
				if ($.ig.util.isNaN(x)) {
					continue;
				}
				var y = itemLocations.item(i).__y;
				if ($.ig.util.isNaN(y)) {
					continue;
				}
				if (x < left || x > right) {
					continue;
				}
				if (y < top || y > bottom) {
					continue;
				}
				visibleItems.add(i);
			}
		}
	}
	,
	getBuckets: function (viewportRect, visibleItems, resolution, itemLocations) {
		var wasActive = new Array(itemLocations.count());
		var en = this.activeMarkerIndexesStrategy()().getEnumerator();
		while (en.moveNext()) {
			var index = en.current();
			if (index != -1) {
				wasActive[index] = true;
			}
		}
		var rowSize = $.ig.truncate(Math.floor(viewportRect.width() / resolution));
		var buckets = new $.ig.Dictionary$2($.ig.Number.prototype.$type, $.ig.MarkerManagerBucket.prototype.$type, 0);
		var en1 = visibleItems.getEnumerator();
		while (en1.moveNext()) {
			var index1 = en1.current();
			var xVal = itemLocations.item(index1).__x;
			var yVal = itemLocations.item(index1).__y;
			var rowNumber = $.ig.truncate(Math.floor(yVal / resolution));
			var colNumber = $.ig.truncate(Math.floor(xVal / resolution));
			var offset = (rowNumber * rowSize) + colNumber;
			var bucket;
			if (!(function () { var $ret = buckets.tryGetValue(offset, bucket); bucket = $ret.p1; return $ret.ret; }())) {
				bucket = new $.ig.MarkerManagerBucket();
				buckets.add(offset, bucket);
			}
			if (wasActive[index1]) {
				bucket.priorityItems().add(index1);
			} else {
				bucket.items().add(index1);
			}
		}
		return buckets;
	}
	,
	$type: new $.ig.Type('MarkerManagerBase', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('BubbleMarkerManager', 'MarkerManagerBase', {
	_radiusColumn: null,
	radiusColumn: function (value) {
		if (arguments.length === 1) {
			this._radiusColumn = value;
			return value;
		} else {
			return this._radiusColumn;
		}
	}
	,
	_actualRadiusColumn: null,
	actualRadiusColumn: function (value) {
		if (arguments.length === 1) {
			this._actualRadiusColumn = value;
			return value;
		} else {
			return this._actualRadiusColumn;
		}
	}
	,
	_actualMarkers: null,
	actualMarkers: function (value) {
		if (arguments.length === 1) {
			this._actualMarkers = value;
			return value;
		} else {
			return this._actualMarkers;
		}
	}
	,
	init: function (provideMarkerStrategy, provideItemStrategy, removeUnusedMarkers, getItemLocationsStrategy, activeMarkerIndexesStrategy) {
		$.ig.MarkerManagerBase.prototype.init.call(this, provideMarkerStrategy, provideItemStrategy, removeUnusedMarkers, getItemLocationsStrategy, activeMarkerIndexesStrategy);
		this.actualRadiusColumn(new $.ig.List$1(Number, 0));
		this.actualMarkers(new $.ig.List$1($.ig.Marker.prototype.$type, 0));
	},
	winnowMarkers: function (markers, maximumMarkers, windowRect, viewportRect, currentResolution) {
		var itemLocations = this.getItemLocationsStrategy()();
		markers.clear();
		this.actualRadiusColumn().clear();
		this.actualMarkers().clear();
		var visibleItems = new $.ig.List$1($.ig.Number.prototype.$type, 0);
		maximumMarkers = Math.max(0, maximumMarkers);
		var markerItems = null;
		this.getVisibleItems(windowRect, viewportRect, itemLocations, visibleItems);
		if (maximumMarkers >= visibleItems.count()) {
			markerItems = visibleItems;
		} else {
			markerItems = new $.ig.List$1($.ig.Number.prototype.$type, 0);
			var resolution = Math.max(8, currentResolution);
			var buckets = this.getBuckets(viewportRect, visibleItems, resolution, itemLocations);
			var keys = new $.ig.List$1($.ig.Number.prototype.$type, 1, buckets.keys());
			if ($.ig.MarkerManagerBase.prototype.useDeterministicSelection()) {
				keys.sort();
			}
			this.selectMarkerItems(maximumMarkers, buckets, keys, markerItems);
		}
		for (var i = 0; i < markerItems.count(); ++i) {
			var x = itemLocations[markerItems.__inner[i]].__x;
			var y = itemLocations[markerItems.__inner[i]].__y;
			var radius = this.radiusColumn().item(markerItems.__inner[i]);
			this.actualRadiusColumn().add(radius);
			var marker = this.provideMarkerStrategy()(this.provideItemStrategy()(markerItems.__inner[i]));
			(marker.content()).item(this.provideItemStrategy()(markerItems.__inner[i]));
			var mp = new $.ig.OwnedPoint();
			mp.ownerItem((marker.content()).item());
			mp.point({ __x: x, __y: y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			if (!markers.containsKey(mp.ownerItem())) {
				markers.add(mp.ownerItem(), mp);
				this.actualMarkers().add(marker);
			}
		}
	}
	,
	render: function (markers, lightweight) {
		var keys = new $.ig.List$1($.ig.Object.prototype.$type, 1, markers.keys());
		if ($.ig.MarkerManagerBase.prototype.useDeterministicSelection()) {
			keys.sort2(function (o1, o2) {
				var point1 = markers.item(o1);
				var point2 = markers.item(o2);
				var dist1 = Math.pow(point1.point().__x, 2) + Math.pow(point1.point().__y, 2);
				var dist2 = Math.pow(point2.point().__x, 2) + Math.pow(point2.point().__y, 2);
				return (dist1).compareTo(dist2);
			});
		}
		var en = keys.getEnumerator();
		while (en.moveNext()) {
			var key = en.current();
			var point = markers.item(key);
			var marker = this.provideMarkerStrategy()(point.ownerItem());
			marker.canvasZIndex(keys.indexOf1(key));
			marker.canvasLeft(point.point().__x);
			marker.canvasTop(point.point().__y);
		}
		this.removeUnusedMarkers()(markers);
	}
	,
	$type: new $.ig.Type('BubbleMarkerManager', $.ig.MarkerManagerBase.prototype.$type)
}, true);

$.ig.util.defType('ErrorBarSettingsBase', 'DependencyObject', {
	init: function () {
		$.ig.DependencyObject.prototype.init.call(this);
		this.defaultErrorBarStyle(new $.ig.Style());
	},
	defaultErrorBarStyle: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.ErrorBarSettingsBase.prototype.defaultErrorBarStyleProperty, value);
			return value;
		} else {
			return this.getValue($.ig.ErrorBarSettingsBase.prototype.defaultErrorBarStyleProperty);
		}
	}
	,
	propertyChanged: null,
	propertyUpdated: null,
	raisePropertyChanged: function (name, oldValue, newValue) {
		if (this.propertyChanged != null) {
			this.propertyChanged(this, new $.ig.PropertyChangedEventArgs(name));
		}
		if (this.propertyUpdated != null) {
			this.propertyUpdated(this, new $.ig.PropertyUpdatedEventArgs(name, oldValue, newValue));
		}
	}
	,
	$type: new $.ig.Type('ErrorBarSettingsBase', $.ig.DependencyObject.prototype.$type, [$.ig.INotifyPropertyChanged.prototype.$type])
}, true);

$.ig.util.defType('ScatterErrorBarSettings', 'ErrorBarSettingsBase', {
	init: function () {
		$.ig.ErrorBarSettingsBase.prototype.init.call(this);
		this.propertyUpdated = $.ig.Delegate.prototype.combine(this.propertyUpdated, this.scatterErrorBarSettings_PropertyUpdated.runOn(this));
	},
	enableErrorBarsHorizontal: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.ScatterErrorBarSettings.prototype.enableErrorBarsHorizontalProperty, $.ig.EnableErrorBars.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.ScatterErrorBarSettings.prototype.enableErrorBarsHorizontalProperty));
		}
	}
	,
	horizontalCalculatorReference: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.ScatterErrorBarSettings.prototype.horizontalCalculatorReferenceProperty, $.ig.ErrorBarCalculatorReference.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.ScatterErrorBarSettings.prototype.horizontalCalculatorReferenceProperty));
		}
	}
	,
	horizontalCalculator: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.ScatterErrorBarSettings.prototype.horizontalCalculatorProperty, value);
			return value;
		} else {
			return this.getValue($.ig.ScatterErrorBarSettings.prototype.horizontalCalculatorProperty);
		}
	}
	,
	horizontalErrorBarCapLength: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.ScatterErrorBarSettings.prototype.horizontalErrorBarCapLengthProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.ScatterErrorBarSettings.prototype.horizontalErrorBarCapLengthProperty));
		}
	}
	,
	horizontalStroke: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.ScatterErrorBarSettings.prototype.horizontalStrokeProperty, value);
			return value;
		} else {
			return this.getValue($.ig.ScatterErrorBarSettings.prototype.horizontalStrokeProperty);
		}
	}
	,
	horizontalStrokeThickness: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.ScatterErrorBarSettings.prototype.horizontalStrokeThicknessProperty, value);
			return value;
		} else {
			return this.getValue($.ig.ScatterErrorBarSettings.prototype.horizontalStrokeThicknessProperty);
		}
	}
	,
	horizontalErrorBarStyle: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.ScatterErrorBarSettings.prototype.horizontalErrorBarStyleProperty, value);
			return value;
		} else {
			return this.getValue($.ig.ScatterErrorBarSettings.prototype.horizontalErrorBarStyleProperty);
		}
	}
	,
	enableErrorBarsVertical: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.ScatterErrorBarSettings.prototype.enableErrorBarsVerticalProperty, $.ig.EnableErrorBars.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.ScatterErrorBarSettings.prototype.enableErrorBarsVerticalProperty));
		}
	}
	,
	verticalCalculatorReference: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.ScatterErrorBarSettings.prototype.verticalCalculatorReferenceProperty, $.ig.ErrorBarCalculatorReference.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.ScatterErrorBarSettings.prototype.verticalCalculatorReferenceProperty));
		}
	}
	,
	verticalCalculator: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.ScatterErrorBarSettings.prototype.verticalCalculatorProperty, value);
			return value;
		} else {
			return this.getValue($.ig.ScatterErrorBarSettings.prototype.verticalCalculatorProperty);
		}
	}
	,
	verticalErrorBarCapLength: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.ScatterErrorBarSettings.prototype.verticalErrorBarCapLengthProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.ScatterErrorBarSettings.prototype.verticalErrorBarCapLengthProperty));
		}
	}
	,
	verticalStroke: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.ScatterErrorBarSettings.prototype.verticalStrokeProperty, value);
			return value;
		} else {
			return this.getValue($.ig.ScatterErrorBarSettings.prototype.verticalStrokeProperty);
		}
	}
	,
	verticalStrokeThickness: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.ScatterErrorBarSettings.prototype.verticalStrokeThicknessProperty, value);
			return value;
		} else {
			return this.getValue($.ig.ScatterErrorBarSettings.prototype.verticalStrokeThicknessProperty);
		}
	}
	,
	verticalErrorBarStyle: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.ScatterErrorBarSettings.prototype.verticalErrorBarStyleProperty, value);
			return value;
		} else {
			return this.getValue($.ig.ScatterErrorBarSettings.prototype.verticalErrorBarStyleProperty);
		}
	}
	,
	__series: null,
	series: function (value) {
		if (arguments.length === 1) {
			this.__series = value;
			return value;
		} else {
			return this.__series;
		}
	}
	,
	scatterErrorBarSettings_PropertyUpdated: function (sender, e) {
		switch (e.propertyName()) {
			case $.ig.ScatterErrorBarSettings.prototype._horizontalCalculatorPropertyName:
			case $.ig.ScatterErrorBarSettings.prototype._verticalCalculatorPropertyName:
				var oldCalc = $.ig.util.cast($.ig.IErrorBarCalculator.prototype.$type, e.oldValue());
				if (oldCalc != null) {
					oldCalc.changed = $.ig.Delegate.prototype.remove(oldCalc.changed, this.calculator_Changed.runOn(this));
				}
				if (this.series() != null) {
					this.series().renderSeries(false);
					if (this.series().seriesViewer() != null) {
						this.series().notifyThumbnailAppearanceChanged();
					}
				}
				var newCalc = $.ig.util.cast($.ig.IErrorBarCalculator.prototype.$type, e.newValue());
				if (newCalc != null) {
					newCalc.changed = $.ig.Delegate.prototype.combine(newCalc.changed, this.calculator_Changed.runOn(this));
				}
				break;
			case $.ig.ScatterErrorBarSettings.prototype._enableErrorBarsHorizontalPropertyName:
			case $.ig.ScatterErrorBarSettings.prototype._enableErrorBarsVerticalPropertyName:
			case $.ig.ScatterErrorBarSettings.prototype._horizontalCalculatorReferencePropertyName:
			case $.ig.ScatterErrorBarSettings.prototype.horizontalErrorBarCapLengthPropertyName:
			case $.ig.ScatterErrorBarSettings.prototype.horizontalErrorBarStylePropertyName:
			case $.ig.ScatterErrorBarSettings.prototype._horizontalStrokePropertyName:
			case $.ig.ScatterErrorBarSettings.prototype._horizontalStrokeThicknessPropertyName:
			case $.ig.ScatterErrorBarSettings.prototype._verticalCalculatorReferencePropertyName:
			case $.ig.ScatterErrorBarSettings.prototype.verticalErrorBarCapLengthPropertyName:
			case $.ig.ScatterErrorBarSettings.prototype.verticalErrorBarStylePropertyName:
			case $.ig.ScatterErrorBarSettings.prototype._verticalStrokePropertyName:
			case $.ig.ScatterErrorBarSettings.prototype._verticalStrokeThicknessPropertyName:
				if (this.series() != null) {
					this.series().renderSeries(false);
					if (this.series().seriesViewer() != null) {
						this.series().notifyThumbnailAppearanceChanged();
					}
				}
				break;
		}
	}
	,
	calculator_Changed: function (sender, e) {
		var calculator = $.ig.util.cast($.ig.IErrorBarCalculator.prototype.$type, sender);
		if (calculator != null) {
			calculator.changed = $.ig.Delegate.prototype.remove(calculator.changed, this.calculator_Changed.runOn(this));
			if (this.series() != null) {
				this.series().renderSeries(false);
			}
			calculator.changed = $.ig.Delegate.prototype.combine(calculator.changed, this.calculator_Changed.runOn(this));
		}
	}
	,
	$type: new $.ig.Type('ScatterErrorBarSettings', $.ig.ErrorBarSettingsBase.prototype.$type)
}, true);

$.ig.util.defType('ScatterBase', 'MarkerSeries', {
	createView: function () {
		return new $.ig.ScatterBaseView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.MarkerSeries.prototype.onViewCreated.call(this, view);
		this.scatterView(view);
	}
	,
	_scatterView: null,
	scatterView: function (value) {
		if (arguments.length === 1) {
			this._scatterView = value;
			return value;
		} else {
			return this._scatterView;
		}
	}
	,
	isScatter: function () {
		return true;
	}
	,
	getResolvedHitTestMode: function () {
		if (this.hitTestMode() == $.ig.SeriesHitTestMode.prototype.auto) {
			if (this.maximumMarkers() <= 2000) {
				return $.ig.SeriesHitTestMode.prototype.computational;
			} else {
				return $.ig.MarkerSeries.prototype.getResolvedHitTestMode.call(this);
			}
		} else {
			return $.ig.MarkerSeries.prototype.getResolvedHitTestMode.call(this);
		}
	}
	,
	getSeriesValueMarkerBoundingBox: function (world) {
		if (!this.hasMarkers() || !this.shouldDisplayMarkers()) {
			return $.ig.Rect.prototype.empty();
		}
		var marker = this.markerView().getHitMarker(this.fromWorldPosition(world));
		var ret = null;
		if (marker != null) {
			return this.markerView().getBoundingBoxForMarker(marker);
		}
		return $.ig.Rect.prototype.empty();
	}
	,
	testHit: function (position, isFinger) {
		if (this.testMarkersOver(position, isFinger)) {
			return true;
		}
		return false;
	}
	,
	init: function () {
		$.ig.MarkerSeries.prototype.init.call(this);
		this.thumbnailFrame(new $.ig.ScatterFrame());
		this.__cachedWindowRect = $.ig.Rect.prototype.empty();
		this.__cachedViewportRect = $.ig.Rect.prototype.empty();
	},
	_axisInfoCache: null,
	axisInfoCache: function (value) {
		if (arguments.length === 1) {
			this._axisInfoCache = value;
			return value;
		} else {
			return this._axisInfoCache;
		}
	}
	,
	xAxis: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.ScatterBase.prototype.xAxisProperty, value);
			return value;
		} else {
			return this.getValue($.ig.ScatterBase.prototype.xAxisProperty);
		}
	}
	,
	yAxis: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.ScatterBase.prototype.yAxisProperty, value);
			return value;
		} else {
			return this.getValue($.ig.ScatterBase.prototype.yAxisProperty);
		}
	}
	,
	xMemberPath: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.ScatterBase.prototype.xMemberPathProperty, value);
			return value;
		} else {
			return this.getValue($.ig.ScatterBase.prototype.xMemberPathProperty);
		}
	}
	,
	xColumn: function (value) {
		if (arguments.length === 1) {
			if (this._xColumn != value) {
				var oldXColumn = this.xColumn();
				this._xColumn = value;
				this.raisePropertyChanged($.ig.ScatterBase.prototype.xColumnPropertyName, oldXColumn, this.xColumn());
			}
			return value;
		} else {
			return this._xColumn;
		}
	}
	,
	_xColumn: null,
	yMemberPath: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.ScatterBase.prototype.yMemberPathProperty, value);
			return value;
		} else {
			return this.getValue($.ig.ScatterBase.prototype.yMemberPathProperty);
		}
	}
	,
	yColumn: function (value) {
		if (arguments.length === 1) {
			if (this._yColumn != value) {
				var oldYColumn = this.yColumn();
				this._yColumn = value;
				this.raisePropertyChanged($.ig.ScatterBase.prototype.yColumnPropertyName, oldYColumn, this.yColumn());
			}
			return value;
		} else {
			return this._yColumn;
		}
	}
	,
	_yColumn: null,
	trendLineType: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.ScatterBase.prototype.trendLineTypeProperty, $.ig.TrendLineType.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.ScatterBase.prototype.trendLineTypeProperty));
		}
	}
	,
	trendLineBrush: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.ScatterBase.prototype.trendLineBrushProperty, value);
			return value;
		} else {
			return this.getValue($.ig.ScatterBase.prototype.trendLineBrushProperty);
		}
	}
	,
	actualTrendLineBrush: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.ScatterBase.prototype.actualTrendLineBrushProperty, value);
			return value;
		} else {
			return this.getValue($.ig.ScatterBase.prototype.actualTrendLineBrushProperty);
		}
	}
	,
	trendLineThickness: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.ScatterBase.prototype.trendLineThicknessProperty, value);
			return value;
		} else {
			return this.getValue($.ig.ScatterBase.prototype.trendLineThicknessProperty);
		}
	}
	,
	trendLineDashCap: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.ScatterBase.prototype.trendLineDashCapProperty, $.ig.PenLineCap.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.ScatterBase.prototype.trendLineDashCapProperty));
		}
	}
	,
	trendLineDashArray: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.ScatterBase.prototype.trendLineDashArrayProperty, value);
			return value;
		} else {
			return this.getValue($.ig.ScatterBase.prototype.trendLineDashArrayProperty);
		}
	}
	,
	trendLinePeriod: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.ScatterBase.prototype.trendLinePeriodProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.ScatterBase.prototype.trendLinePeriodProperty));
		}
	}
	,
	markerCollisionAvoidance: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.ScatterBase.prototype.markerCollisionAvoidanceProperty, $.ig.CollisionAvoidanceType.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.ScatterBase.prototype.markerCollisionAvoidanceProperty));
		}
	}
	,
	trendLineZIndex: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.ScatterBase.prototype.trendLineZIndexProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.ScatterBase.prototype.trendLineZIndexProperty));
		}
	}
	,
	maximumMarkers: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.ScatterBase.prototype.maximumMarkersProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.ScatterBase.prototype.maximumMarkersProperty));
		}
	}
	,
	invalidateAxes: function () {
		$.ig.MarkerSeries.prototype.invalidateAxes.call(this);
		if (this.xAxis() != null) {
			this.xAxis().renderAxis1(false);
		}
		if (this.yAxis() != null) {
			this.yAxis().renderAxis1(false);
		}
	}
	,
	errorBarSettings: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.ScatterBase.prototype.errorBarSettingsProperty, value);
			return value;
		} else {
			return this.getValue($.ig.ScatterBase.prototype.errorBarSettingsProperty);
		}
	}
	,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		$.ig.MarkerSeries.prototype.propertyUpdatedOverride.call(this, sender, propertyName, oldValue, newValue);
		if (this.scatterView().trendLineManager().propertyUpdated(sender, propertyName, oldValue, newValue)) {
			this.renderSeries(false);
			this.notifyThumbnailAppearanceChanged();
		}
		switch (propertyName) {
			case $.ig.Series.prototype.fastItemsSourcePropertyName:
				if ($.ig.util.cast($.ig.IFastItemsSource.prototype.$type, oldValue) != null) {
					(oldValue).deregisterColumn(this.xColumn());
					(oldValue).deregisterColumn(this.yColumn());
					this.xColumn(null);
					this.yColumn(null);
				}
				if ($.ig.util.cast($.ig.IFastItemsSource.prototype.$type, newValue) != null) {
					this.xColumn(this.registerDoubleColumn(this.xMemberPath()));
					this.yColumn(this.registerDoubleColumn(this.yMemberPath()));
				}
				if ((this.yAxis() != null && !this.yAxis().updateRange()) || (this.xAxis() != null && !this.xAxis().updateRange())) {
					this.renderSeries(false);
				}
				break;
			case $.ig.ScatterBase.prototype.xAxisPropertyName:
				if (oldValue != null) {
					(oldValue).deregisterSeries(this);
				}
				if (newValue != null) {
					(newValue).registerSeries(this);
				}
				if ((this.xAxis() != null && !this.xAxis().updateRange()) || (newValue == null && oldValue != null)) {
					this.renderSeries(false);
				}
				this.notifyThumbnailAppearanceChanged();
				break;
			case $.ig.ScatterBase.prototype.yAxisPropertyName:
				if (oldValue != null) {
					(oldValue).deregisterSeries(this);
				}
				if (newValue != null) {
					(newValue).registerSeries(this);
				}
				if ((this.yAxis() != null && !this.yAxis().updateRange()) || (newValue == null && oldValue != null)) {
					this.renderSeries(false);
				}
				this.notifyThumbnailAppearanceChanged();
				break;
			case $.ig.ScatterBase.prototype.xMemberPathPropertyName:
				if (this.fastItemsSource() != null) {
					this.fastItemsSource().deregisterColumn(this.xColumn());
					this.xColumn(this.registerDoubleColumn(this.xMemberPath()));
				}
				break;
			case $.ig.ScatterBase.prototype.xColumnPropertyName:
				this.scatterView().trendLineManager().reset();
				if (this.xAxis() != null && !this.xAxis().updateRange()) {
					this.renderSeries(false);
				}
				break;
			case $.ig.ScatterBase.prototype.yMemberPathPropertyName:
				if (this.fastItemsSource() != null) {
					this.fastItemsSource().deregisterColumn(this.yColumn());
					this.yColumn(this.registerDoubleColumn(this.yMemberPath()));
				}
				break;
			case $.ig.ScatterBase.prototype.yColumnPropertyName:
				this.scatterView().trendLineManager().reset();
				if (this.yAxis() != null && !this.yAxis().updateRange()) {
					this.renderSeries(false);
				}
				break;
			case $.ig.ScatterBase.prototype.markerCollisionAvoidancePropertyName:
				this.renderSeries(false);
				this.notifyThumbnailAppearanceChanged();
				break;
			case $.ig.ScatterBase.prototype.maximumMarkersPropertyName:
				this.renderSeries(false);
				this.notifyThumbnailAppearanceChanged();
				break;
			case $.ig.Series.prototype.transitionProgressPropertyName:
				this._transitionFrame.interpolate3(this.transitionProgress(), this._previousFrame, this._currentFrame);
				this.cacheViewInfo();
				try {
					if (this.clearAndAbortIfInvalid1(this.view())) {
						return;
					}
					if ((Math.round(this.transitionProgress() * 100000) / 100000) == 1) {
						this.renderFrame(this._currentFrame, this.scatterView());
					} else {
						this.renderFrame(this._transitionFrame, this.scatterView());
					}
				}
				finally {
					this.unCacheViewInfo();
				}
				break;
			case $.ig.Series.prototype.trendLineBrushPropertyName:
				this.updateIndexedProperties();
				break;
			case $.ig.ScatterBase.prototype._errorBarSettingsPropertyName:
				if (this.errorBarSettings() != null) {
					this.errorBarSettings().series(this);
				}
				this.renderSeries(false);
				this.notifyThumbnailAppearanceChanged();
				break;
			case $.ig.Series.prototype.trendLineTypePropertyName:
				this.notifyThumbnailAppearanceChanged();
				break;
		}
	}
	,
	canUseAsYAxis: function (axis) {
		if ($.ig.util.cast($.ig.NumericYAxis.prototype.$type, axis) !== null) {
			return true;
		}
		return false;
	}
	,
	canUseAsXAxis: function (axis) {
		if ($.ig.util.cast($.ig.NumericXAxis.prototype.$type, axis) !== null) {
			return true;
		}
		return false;
	}
	,
	unCacheViewInfo: function () {
		this.__cachedViewportRect = $.ig.Rect.prototype.empty();
		this.__cachedWindowRect = $.ig.Rect.prototype.empty();
	}
	,
	__cachedViewportRect: null,
	__cachedWindowRect: null,
	cacheViewInfo: function () {
		var $ret = this.getViewInfo(this.__cachedViewportRect, this.__cachedWindowRect);
		this.__cachedViewportRect = $ret.p0;
		this.__cachedWindowRect = $ret.p1;
	}
	,
	mustReact: function (propertyName, action) {
		if (action != $.ig.FastItemsSourceEventAction.prototype.change) {
			return true;
		}
		if (propertyName == null) {
			return true;
		}
		if (this.xMemberPath() == propertyName || this.yMemberPath() == propertyName) {
			return true;
		}
		return false;
	}
	,
	dataUpdatedOverride: function (action, position, count, propertyName) {
		var refresh = false;
		if (!this.mustReact(propertyName, action)) {
			return;
		}
		this.scatterView().trendLineManager().dataUpdated(action, position, count, propertyName);
		if (this.xAxis() != null && !this.xAxis().updateRange()) {
			refresh = true;
		}
		if (this.yAxis() != null && !this.yAxis().updateRange()) {
			refresh = true;
		}
		if (refresh) {
			this.renderSeries(true);
		}
	}
	,
	prepLinePoints: function (frame) {
		this.prepLinePoints1(frame, null);
	}
	,
	prepLinePoints1: function (frame, clipper) {
		var xCount = this.xColumn() != null ? this.xColumn().count() : 0;
		var yCount = this.yColumn() != null ? this.yColumn().count() : 0;
		var count = Math.min(xCount, yCount);
		if (count <= this.maximumMarkers()) {
			frame.points().clear();
			var linePoints = new $.ig.List$1($.ig.OwnedPoint.prototype.$type, 0);
			var en = frame.linePoints().values().getEnumerator();
			while (en.moveNext()) {
				var point = en.current();
				linePoints.add(point);
			}
			var fastItemsSource = this.fastItemsSource();
			linePoints.sort2(function (p1, p2) {
				var index1 = fastItemsSource.indexOf(p1.ownerItem());
				var index2 = fastItemsSource.indexOf(p2.ownerItem());
				if (index1 < index2) {
					return -1;
				}
				if (index1 > index2) {
					return 1;
				}
				return 0;
			});
			if (clipper != null) {
				clipper.target(frame.points());
			}
			var en1 = linePoints.getEnumerator();
			while (en1.moveNext()) {
				var point1 = en1.current();
				if (fastItemsSource.indexOf(point1.ownerItem()) >= 0) {
					if (clipper != null) {
						clipper.add(point1.point());
					} else {
						frame.points().add({ __x: point1.point().__x, __y: point1.point().__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
					}
				}
			}
		}
	}
	,
	getRange: function (axis) {
		if (axis != null && axis == this.xAxis() && this.xColumn() != null) {
			return new $.ig.AxisRange(this.xColumn().minimum(), this.xColumn().maximum());
		}
		if (axis != null && axis == this.yAxis() && this.yColumn() != null) {
			return new $.ig.AxisRange(this.yColumn().minimum(), this.yColumn().maximum());
		}
		return null;
	}
	,
	getItem: function (world) {
		return null;
	}
	,
	getItemIndex: function (world) {
		return -1;
	}
	,
	scrollIntoView: function (item) {
		var windowRect = this.seriesViewer() != null ? this.seriesViewer().actualWindowRect() : $.ig.Rect.prototype.empty();
		var viewportRect = this.seriesViewer() != null ? this.seriesViewer().viewportRect() : $.ig.Rect.prototype.empty();
		var unitRect = new $.ig.Rect(0, 0, 0, 1, 1);
		var effectiveViewportRect = this.getEffectiveViewportForUnitViewport(this.view());
		var xParams = new $.ig.ScalerParams(0, unitRect, unitRect, this.xAxis().isInverted(), effectiveViewportRect);
		var yParams = new $.ig.ScalerParams(0, unitRect, unitRect, this.yAxis().isInverted(), effectiveViewportRect);
		var index = !windowRect.isEmpty() && !viewportRect.isEmpty() && this.fastItemsSource() != null ? this.fastItemsSource().indexOf(item) : -1;
		var cx = this.xAxis() != null && this.xColumn() != null && index < this.xColumn().count() ? this.xAxis().getScaledValue(this.xColumn().item(index), xParams) : NaN;
		var cy = this.yAxis() != null && this.yColumn() != null && index < this.yColumn().count() ? this.yAxis().getScaledValue(this.yColumn().item(index), yParams) : NaN;
		if (!$.ig.util.isNaN(cx)) {
			if (cx < windowRect.left() + 0.1 * windowRect.width()) {
				cx = cx + 0.4 * windowRect.width();
				windowRect.x(cx - 0.5 * windowRect.width());
			}
			if (cx > windowRect.right() - 0.1 * windowRect.width()) {
				cx = cx - 0.4 * windowRect.width();
				windowRect.x(cx - 0.5 * windowRect.width());
			}
		}
		if (!$.ig.util.isNaN(cy)) {
			if (cy < windowRect.top() + 0.1 * windowRect.height()) {
				cy = cy + 0.4 * windowRect.height();
				windowRect.y(cy - 0.5 * windowRect.height());
			}
			if (cy > windowRect.bottom() - 0.1 * windowRect.height()) {
				cy = cy - 0.4 * windowRect.height();
				windowRect.y(cy - 0.5 * windowRect.height());
			}
		}
		if (this.syncLink() != null) {
			this.syncLink().windowNotify(this.seriesViewer(), windowRect);
		}
		return index >= 0;
	}
	,
	viewportRectChangedOverride: function (oldViewportRect, newViewportRect) {
		this.renderSeries(false);
	}
	,
	windowRectChangedOverride: function (oldWindowRect, newWindowRect) {
		this.renderSeries(false);
	}
	,
	_previousFrame: null,
	_transitionFrame: null,
	_currentFrame: null,
	calculateCachedPoints: function (view, frame, count, windowRect, viewportRect) {
		var $self = this;
		frame.cachedPoints(new $.ig.Dictionary$2($.ig.Object.prototype.$type, $.ig.OwnedPoint.prototype.$type, 1, count));
		var effectiveViewportRect = this.getEffectiveViewport1(view);
		var itemsSource = this.fastItemsSource();
		var x;
		var y;
		var xParams = (function () {
			var $ret = new $.ig.ScalerParams(0, windowRect, viewportRect, $self.axisInfoCache().xAxisIsInverted(), effectiveViewportRect);
			$ret._effectiveViewportRect = $self.getEffectiveViewport1(view);
			return $ret;
		}());
		var yParams = (function () {
			var $ret = new $.ig.ScalerParams(0, windowRect, viewportRect, $self.axisInfoCache().yAxisIsInverted(), effectiveViewportRect);
			$ret._effectiveViewportRect = $self.getEffectiveViewport1(view);
			return $ret;
		}());
		for (var i = 0; i < count; i++) {
			x = this.xColumn().item(i);
			y = this.yColumn().item(i);
			var point = { __x: this.axisInfoCache().xAxis().getScaledValue(x, xParams), __y: this.axisInfoCache().yAxis().getScaledValue(y, yParams), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			if (!Number.isInfinity(point.__x) && !Number.isInfinity(point.__y)) {
				var item = itemsSource.item(i);
				if (!frame.cachedPoints().containsKey(item)) {
					var columnValues = { __x: x, __y: y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
					var p = { __x: point.__x, __y: point.__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
					frame.cachedPoints().add(item, (function () {
						var $ret = new $.ig.OwnedPoint();
						$ret.ownerItem(item);
						$ret.columnValues(columnValues);
						$ret.point(p);
						return $ret;
					}()));
				}
			}
		}
	}
	,
	prepareFrame: function (frame, view) {
		var $self = this;
		frame.markers().clear();
		frame.trendLine().clear();
		frame.horizontalErrorBars().clear();
		frame.verticalErrorBars().clear();
		frame.horizontalErrorBarWidths().clear();
		frame.verticalErrorBarHeights().clear();
		var count = Math.min(this.xColumn() != null ? this.xColumn().count() : 0, this.yColumn() != null ? this.yColumn().count() : 0);
		var windowRect = view.windowRect();
		var viewportRect = view.viewport();
		var effectiveViewportRect = this.getEffectiveViewport1(view);
		var xParams = new $.ig.ScalerParams(0, windowRect, viewportRect, this.xAxis().isInverted(), effectiveViewportRect);
		xParams._effectiveViewportRect = this.getEffectiveViewport1(view);
		var yParams = new $.ig.ScalerParams(0, windowRect, viewportRect, this.yAxis().isInverted(), effectiveViewportRect);
		yParams._effectiveViewportRect = this.getEffectiveViewport1(view);
		if (count < 1) {
			return;
		}
		this.axisInfoCache((function () {
			var $ret = new $.ig.ScatterAxisInfoCache();
			$ret.xAxis($self.xAxis());
			$ret.yAxis($self.yAxis());
			$ret.xAxisIsInverted($self.xAxis().isInverted());
			$ret.yAxisIsInverted($self.yAxis().isInverted());
			$ret.fastItemsSource($self.fastItemsSource());
			return $ret;
		}()));
		var scatterView = view;
		if (count <= this.maximumMarkers()) {
			this.calculateCachedPoints(scatterView, frame, count, windowRect, viewportRect);
		}
		if (this.shouldDisplayMarkers()) {
			view.markerManager().winnowMarkers(frame.markers(), this.maximumMarkers(), windowRect, viewportRect, this.resolution());
		}
		var clipper = (function () {
			var $ret = new $.ig.Clipper(0, viewportRect, false);
			$ret.target(frame.trendLine());
			return $ret;
		}());
		var contentViewport = this.getContentViewport(view);
		var xmin = this.xAxis().getUnscaledValue(contentViewport.left(), xParams);
		var xmax = this.xAxis().getUnscaledValue(contentViewport.right(), xParams);
		view.trendLineManager().prepareLine(frame.trendLine(), this.trendLineType(), this.xColumn(), this.yColumn(), this.trendLinePeriod(), function (x) { return $self.xAxis().getScaledValue(x, xParams); }, function (y) { return $self.yAxis().getScaledValue(y, yParams); }, (function () {
			var $ret = new $.ig.TrendResolutionParams();
			$ret.resolution($self.resolution());
			$ret.viewport(viewportRect);
			$ret.window(windowRect);
			return $ret;
		}()), clipper, xmin, xmax);
		this.prepareErrorBars(frame, view);
	}
	,
	clearRendering: function (wipeClean, view) {
		$.ig.MarkerSeries.prototype.clearRendering.call(this, wipeClean, view);
		var scatterView = view;
		scatterView.clearRendering(wipeClean);
	}
	,
	renderFrame: function (frame, view) {
		var $self = this;
		var viewportRect = view.viewport();
		this.axisInfoCache((function () {
			var $ret = new $.ig.ScatterAxisInfoCache();
			$ret.xAxis($self.xAxis());
			$ret.yAxis($self.yAxis());
			$ret.xAxisIsInverted($self.xAxis().isInverted());
			$ret.yAxisIsInverted($self.yAxis().isInverted());
			return $ret;
		}()));
		if (this.shouldDisplayMarkers()) {
			view.markerManager().render(frame.markers(), this.useLightweightMarkers());
		}
		view.renderMarkers();
		var clipper = (function () {
			var $ret = new $.ig.Clipper(1, NaN, viewportRect.bottom(), NaN, viewportRect.top(), false);
			$ret.target(view.trendLineManager().trendPolyline().points());
			return $ret;
		}());
		view.trendLineManager().rasterizeTrendLine1(frame.trendLine(), clipper);
		this.renderErrorBars(frame, view);
	}
	,
	prepareErrorBars: function (frame, view) {
		var errorBarsHelper = new $.ig.ErrorBarsHelper(this, view);
		if (this.errorBarSettings() == null) {
			return;
		}
		var horizontalCalculator = this.errorBarSettings().horizontalCalculator();
		var verticalCalculator = this.errorBarSettings().verticalCalculator();
		var errorBarPositionX = 0;
		var errorBarPositionY = 0;
		var errorBarPositiveSize = 0;
		var errorBarNegativeSize = 0;
		var en = frame.markers().keys().getEnumerator();
		while (en.moveNext()) {
			var key = en.current();
			var point = frame.markers().item(key);
			if (horizontalCalculator != null) {
				switch (horizontalCalculator.getCalculatorType()) {
					case $.ig.ErrorBarCalculatorType.prototype.percentage:
						var $ret = this.preparePercentageErrorBarSize(point, errorBarsHelper, horizontalCalculator, errorBarPositiveSize, errorBarNegativeSize, true);
						errorBarPositiveSize = $ret.p3;
						errorBarNegativeSize = $ret.p4;
						break;
					case $.ig.ErrorBarCalculatorType.prototype.fixed:
						var $ret1 = this.prepareFixedErrorBarSize(point, errorBarsHelper, horizontalCalculator, errorBarPositiveSize, errorBarNegativeSize, true);
						errorBarPositiveSize = $ret1.p3;
						errorBarNegativeSize = $ret1.p4;
						break;
					case $.ig.ErrorBarCalculatorType.prototype.data:
						var $ret2 = this.prepareDataErrorBarSize(point, errorBarsHelper, horizontalCalculator, key, errorBarPositiveSize, errorBarNegativeSize, true);
						errorBarPositiveSize = $ret2.p4;
						errorBarNegativeSize = $ret2.p5;
						break;
					case $.ig.ErrorBarCalculatorType.prototype.standardDeviation:
						var $ret3 = this.prepareStandardDevErrorBarSize(errorBarsHelper, horizontalCalculator, errorBarPositionX, errorBarPositiveSize, errorBarNegativeSize, true);
						errorBarPositionX = $ret3.p2;
						errorBarPositiveSize = $ret3.p3;
						errorBarNegativeSize = $ret3.p4;
						break;
					case $.ig.ErrorBarCalculatorType.prototype.standardError:
						var $ret4 = this.prepareFixedErrorBarSize(point, errorBarsHelper, horizontalCalculator, errorBarPositiveSize, errorBarNegativeSize, true);
						errorBarPositiveSize = $ret4.p3;
						errorBarNegativeSize = $ret4.p4;
						break;
				}
				;
				var p = new $.ig.OwnedPoint();
				p.point(errorBarsHelper.calculateErrorBarCenterHorizontal(horizontalCalculator, this.axisInfoCache().xAxis(), point.point(), errorBarPositionX));
				;
				p.ownerItem(point.ownerItem());
				frame.horizontalErrorBars().add(key, p);
				frame.horizontalErrorBarWidths().add(key, [ errorBarPositiveSize, errorBarNegativeSize ]);
			}
			if (verticalCalculator != null) {
				switch (verticalCalculator.getCalculatorType()) {
					case $.ig.ErrorBarCalculatorType.prototype.percentage:
						var $ret5 = this.preparePercentageErrorBarSize(point, errorBarsHelper, verticalCalculator, errorBarPositiveSize, errorBarNegativeSize, false);
						errorBarPositiveSize = $ret5.p3;
						errorBarNegativeSize = $ret5.p4;
						break;
					case $.ig.ErrorBarCalculatorType.prototype.fixed:
						var $ret6 = this.prepareFixedErrorBarSize(point, errorBarsHelper, verticalCalculator, errorBarPositiveSize, errorBarNegativeSize, false);
						errorBarPositiveSize = $ret6.p3;
						errorBarNegativeSize = $ret6.p4;
						break;
					case $.ig.ErrorBarCalculatorType.prototype.data:
						var $ret7 = this.prepareDataErrorBarSize(point, errorBarsHelper, verticalCalculator, key, errorBarPositiveSize, errorBarNegativeSize, false);
						errorBarPositiveSize = $ret7.p4;
						errorBarNegativeSize = $ret7.p5;
						break;
					case $.ig.ErrorBarCalculatorType.prototype.standardDeviation:
						var $ret8 = this.prepareStandardDevErrorBarSize(errorBarsHelper, verticalCalculator, errorBarPositionY, errorBarPositiveSize, errorBarNegativeSize, false);
						errorBarPositionY = $ret8.p2;
						errorBarPositiveSize = $ret8.p3;
						errorBarNegativeSize = $ret8.p4;
						break;
					case $.ig.ErrorBarCalculatorType.prototype.standardError:
						var $ret9 = this.prepareFixedErrorBarSize(point, errorBarsHelper, verticalCalculator, errorBarPositiveSize, errorBarNegativeSize, false);
						errorBarPositiveSize = $ret9.p3;
						errorBarNegativeSize = $ret9.p4;
						break;
				}
				;
				var p1 = new $.ig.OwnedPoint();
				p1.point(errorBarsHelper.calculateErrorBarCenterVertical(verticalCalculator, this.axisInfoCache().yAxis(), point.point(), errorBarPositionY));
				;
				p1.ownerItem(point.ownerItem());
				frame.verticalErrorBars().add(key, p1);
				frame.verticalErrorBarHeights().add(key, [ errorBarPositiveSize, errorBarNegativeSize ]);
			}
		}
	}
	,
	preparePercentageErrorBarSize: function (point, helper, calculator, positiveSize, negativeSize, horizontal) {
		if (horizontal) {
			var value;
			var refAxis, targetAxis;
			targetAxis = this.axisInfoCache().xAxis();
			if (this.errorBarSettings().horizontalCalculatorReference() == $.ig.ErrorBarCalculatorReference.prototype.x) {
				value = point.point().__x;
				refAxis = this.axisInfoCache().xAxis();
			} else {
				value = point.point().__y;
				refAxis = this.axisInfoCache().yAxis();
			}
			var $ret = helper.calculateDependentErrorBarSize(value, calculator, refAxis, targetAxis, positiveSize, negativeSize);
			positiveSize = $ret.p4;
			negativeSize = $ret.p5;
		} else {
			var value1;
			var refAxis1, targetAxis1;
			targetAxis1 = this.axisInfoCache().yAxis();
			if (this.errorBarSettings().verticalCalculatorReference() == $.ig.ErrorBarCalculatorReference.prototype.x) {
				value1 = point.point().__x;
				refAxis1 = this.axisInfoCache().xAxis();
			} else {
				value1 = point.point().__y;
				refAxis1 = this.axisInfoCache().yAxis();
			}
			var $ret1 = helper.calculateDependentErrorBarSize(value1, calculator, refAxis1, targetAxis1, positiveSize, negativeSize);
			positiveSize = $ret1.p4;
			negativeSize = $ret1.p5;
		}
		return {
			p3: positiveSize,
			p4: negativeSize
		};
	}
	,
	prepareFixedErrorBarSize: function (point, helper, calculator, positiveSize, negativeSize, horizontal) {
		if (horizontal) {
			var $ret = helper.calculateIndependentErrorBarSize(point.point().__x, calculator, this.axisInfoCache().xAxis(), positiveSize, negativeSize);
			positiveSize = $ret.p3;
			negativeSize = $ret.p4;
		} else {
			var $ret1 = helper.calculateIndependentErrorBarSize(point.point().__y, calculator, this.axisInfoCache().yAxis(), positiveSize, negativeSize);
			positiveSize = $ret1.p3;
			negativeSize = $ret1.p4;
		}
		return {
			p3: positiveSize,
			p4: negativeSize
		};
	}
	,
	prepareDataErrorBarSize: function (point, helper, calculator, key, positiveSize, negativeSize, horizontal) {
		var errorColumn = calculator.getItemColumn();
		var index = this.fastItemsSource().indexOf(key);
		if (errorColumn != null && index < errorColumn.count()) {
			var unscaledValue = errorColumn.item(index);
			if (horizontal) {
				var $ret = helper.calculateFromUnscaledErrorBarValue(point.point().__x, unscaledValue, this.axisInfoCache().xAxis(), positiveSize, negativeSize);
				positiveSize = $ret.p3;
				negativeSize = $ret.p4;
			} else {
				var $ret1 = helper.calculateFromUnscaledErrorBarValue(point.point().__y, unscaledValue, this.axisInfoCache().yAxis(), positiveSize, negativeSize);
				positiveSize = $ret1.p3;
				negativeSize = $ret1.p4;
			}
		} else {
			positiveSize = negativeSize = NaN;
		}
		return {
			p4: positiveSize,
			p5: negativeSize
		};
	}
	,
	prepareStandardDevErrorBarSize: function (helper, calculator, position, positiveSize, negativeSize, horizontal) {
		var $ret = helper.calculateIndependentErrorBarPosition(calculator, position);
		position = $ret.p1;
		if (horizontal) {
			var $ret1 = helper.calculateStdDevErrorBarSize(position, calculator, this.axisInfoCache().xAxis(), positiveSize, negativeSize);
			positiveSize = $ret1.p3;
			negativeSize = $ret1.p4;
		} else {
			var $ret2 = helper.calculateStdDevErrorBarSize(position, calculator, this.axisInfoCache().yAxis(), positiveSize, negativeSize);
			positiveSize = $ret2.p3;
			negativeSize = $ret2.p4;
		}
		return {
			p2: position,
			p3: positiveSize,
			p4: negativeSize
		};
	}
	,
	renderErrorBars: function (frame, view) {
		if (!view.hasSurface() || this.errorBarSettings() == null) {
			view.hideErrorBars();
			return;
		}
		this.renderErrorBarsHorizontal(frame, view);
		this.renderErrorBarsVertical(frame, view);
	}
	,
	renderErrorBarsHorizontal: function (frame, view) {
		view.attachHorizontalErrorBars();
		var errorBarsHelper = new $.ig.ErrorBarsHelper(this, view);
		var horizontalErrorBarsGeometry = new $.ig.PathGeometry();
		var horizontalCalculator = this.errorBarSettings().horizontalCalculator();
		var en = frame.markers().keys().getEnumerator();
		while (en.moveNext()) {
			var key = en.current();
			if (horizontalCalculator != null && frame.horizontalErrorBarWidths().containsKey(key)) {
				var errorBarPositiveSize = frame.horizontalErrorBarWidths().item(key)[0];
				var errorBarNegativeSize = frame.horizontalErrorBarWidths().item(key)[1];
				if (!$.ig.util.isNaN(errorBarPositiveSize) && !$.ig.util.isNaN(errorBarNegativeSize)) {
					var centerHorizontal = frame.horizontalErrorBars().item(key).point();
					if (this.errorBarSettings().enableErrorBarsHorizontal() == $.ig.EnableErrorBars.prototype.both || this.errorBarSettings().enableErrorBarsHorizontal() == $.ig.EnableErrorBars.prototype.positive) {
						errorBarsHelper.addErrorBarHorizontal(horizontalErrorBarsGeometry, centerHorizontal, errorBarPositiveSize, true);
					}
					if (this.errorBarSettings().enableErrorBarsHorizontal() == $.ig.EnableErrorBars.prototype.both || this.errorBarSettings().enableErrorBarsHorizontal() == $.ig.EnableErrorBars.prototype.negative) {
						errorBarsHelper.addErrorBarHorizontal(horizontalErrorBarsGeometry, centerHorizontal, errorBarNegativeSize, false);
					}
				}
			}
		}
		view.provideHorizontalErrorBarGeometry(horizontalErrorBarsGeometry);
	}
	,
	renderErrorBarsVertical: function (frame, view) {
		view.attachVerticalErrorBars();
		var errorBarsHelper = new $.ig.ErrorBarsHelper(this, view);
		var verticalErrorBarsGeometry = new $.ig.PathGeometry();
		var verticalCalculator = this.errorBarSettings().verticalCalculator();
		var en = frame.markers().keys().getEnumerator();
		while (en.moveNext()) {
			var key = en.current();
			if (verticalCalculator != null && frame.verticalErrorBarHeights().containsKey(key)) {
				var errorBarPositiveSize = frame.verticalErrorBarHeights().item(key)[0];
				var errorBarNegativeSize = frame.verticalErrorBarHeights().item(key)[1];
				if (!$.ig.util.isNaN(errorBarPositiveSize) && !$.ig.util.isNaN(errorBarNegativeSize)) {
					var centerVertical = frame.verticalErrorBars().item(key).point();
					if (this.errorBarSettings().enableErrorBarsVertical() == $.ig.EnableErrorBars.prototype.both || this.errorBarSettings().enableErrorBarsVertical() == $.ig.EnableErrorBars.prototype.positive) {
						errorBarsHelper.addErrorBarVertical(verticalErrorBarsGeometry, centerVertical, errorBarPositiveSize, true);
					}
					if (this.errorBarSettings().enableErrorBarsVertical() == $.ig.EnableErrorBars.prototype.both || this.errorBarSettings().enableErrorBarsVertical() == $.ig.EnableErrorBars.prototype.negative) {
						errorBarsHelper.addErrorBarVertical(verticalErrorBarsGeometry, centerVertical, errorBarNegativeSize, false);
					}
				}
			}
		}
	}
	,
	validateSeries: function (viewportRect, windowRect, view) {
		var isValid = true;
		if (!$.ig.MarkerSeries.prototype.validateSeries.call(this, viewportRect, windowRect, view) || windowRect.isEmpty() || viewportRect.isEmpty() || this.xAxis() == null || this.yAxis() == null || this.xAxis().seriesViewer() == null || this.yAxis().seriesViewer() == null || this.xColumn() == null || this.yColumn() == null || this.xColumn().count() == 0 || this.yColumn().count() == 0 || this.fastItemsSource() == null || this.fastItemsSource().count() != this.xColumn().count() || this.fastItemsSource().count() != this.yColumn().count() || this.xAxis().seriesViewer() == null || this.yAxis().seriesViewer() == null || this.xAxis().actualMinimumValue() == this.xAxis().actualMaximumValue() || this.yAxis().actualMinimumValue() == this.yAxis().actualMaximumValue()) {
			isValid = false;
		}
		return isValid;
	}
	,
	getViewInfo: function (viewportRect, windowRect) {
		if (!this.__cachedViewportRect.isEmpty() && !this.__cachedWindowRect.isEmpty()) {
			viewportRect = this.__cachedViewportRect;
			windowRect = this.__cachedWindowRect;
			return {
				p0: viewportRect,
				p1: windowRect
			};
		}
		viewportRect = this.view().viewport();
		windowRect = this.view().windowRect();
		return {
			p0: viewportRect,
			p1: windowRect
		};
	}
	,
	renderSeriesOverride: function (animate) {
		this.cacheViewInfo();
		try {
			if (this.clearAndAbortIfInvalid1(this.view())) {
				return;
			}
			if (this.fastItemsSource() != null && this.fastItemsSource().count() > this.maximumMarkers()) {
				animate = false;
			}
			if (this.shouldAnimate(animate) && !this.skipPrepare()) {
				var previous = this._previousFrame;
				if (this.animationActive()) {
					if (this.animator().needsFlush()) {
						this.animator().flush();
					}
					this._previousFrame = this._transitionFrame;
					this._transitionFrame = previous;
				} else {
					this._previousFrame = this._currentFrame;
					this._currentFrame = previous;
				}
				this.prepareFrame(this._currentFrame, this.scatterView());
				this.startAnimation();
			} else {
				if (!this.skipPrepare()) {
					this.prepareFrame(this._currentFrame, this.scatterView());
				}
				this.renderFrame(this._currentFrame, this.scatterView());
			}
		}
		finally {
			this.unCacheViewInfo();
		}
	}
	,
	updateIndexedProperties: function () {
		$.ig.MarkerSeries.prototype.updateIndexedProperties.call(this);
		if (this.index() < 0) {
			return;
		}
		this.scatterView().updateTrendlineBrush();
	}
	,
	_thumbnailFrame: null,
	thumbnailFrame: function (value) {
		if (arguments.length === 1) {
			this._thumbnailFrame = value;
			return value;
		} else {
			return this._thumbnailFrame;
		}
	}
	,
	renderThumbnail: function (viewportRect, surface) {
		$.ig.MarkerSeries.prototype.renderThumbnail.call(this, viewportRect, surface);
		if (!this.thumbnailDirty()) {
			this.view().prepSurface(surface);
			return;
		}
		this.view().prepSurface(surface);
		if (this.clearAndAbortIfInvalid1(this.thumbnailView())) {
			return;
		}
		var thumbnailView = $.ig.util.cast($.ig.ScatterBaseView.prototype.$type, this.thumbnailView());
		if (!this.skipThumbnailPrepare()) {
			this.thumbnailFrame(new $.ig.ScatterFrame());
			this.prepareFrame(this.thumbnailFrame(), thumbnailView);
		}
		this.skipThumbnailPrepare(false);
		this.renderFrame(this.thumbnailFrame(), thumbnailView);
		this.thumbnailDirty(false);
	}
	,
	_alternateFrame: null,
	alternateFrame: function (value) {
		if (arguments.length === 1) {
			this._alternateFrame = value;
			return value;
		} else {
			return this._alternateFrame;
		}
	}
	,
	renderAlternateView: function (viewportRect, windowRect, surface, viewIdentifier, effectiveScalingRatio) {
		$.ig.MarkerSeries.prototype.renderAlternateView.call(this, viewportRect, windowRect, surface, viewIdentifier, effectiveScalingRatio);
		var view = this.alternateViews().item(viewIdentifier);
		var scatterSeriesView = view;
		view.prepAltSurface(surface);
		if (this.clearAndAbortIfInvalid1(scatterSeriesView)) {
			return;
		}
		if (this.alternateFrame() == null) {
			this.alternateFrame(new $.ig.ScatterFrame());
		}
		this.prepareFrame(this.alternateFrame(), scatterSeriesView);
		this.renderFrame(this.alternateFrame(), scatterSeriesView);
	}
	,
	removeUnusedMarkers: function (list, markers) {
		var remove = new $.ig.List$1($.ig.Object.prototype.$type, 0);
		var en = markers.activeKeys().getEnumerator();
		while (en.moveNext()) {
			var key = en.current();
			if (!list.containsKey(key)) {
				remove.add1(key);
			}
		}
		var en1 = remove.getEnumerator();
		while (en1.moveNext()) {
			var key1 = en1.current();
			markers.remove(key1);
		}
	}
	,
	getMarkerLocations: function (view, markers, locations, windowRect, viewportRect) {
		var $self = this;
		if (locations == null || locations.length != this.axisInfoCache().fastItemsSource().count()) {
			locations = new Array(this.axisInfoCache().fastItemsSource().count());
			for (var i = 0; i < this.axisInfoCache().fastItemsSource().count(); i++) {
				locations[i] = new $.ig.Point(0);
			}
		}
		var effectiveViewportRect = this.getEffectiveViewport1(view);
		var xParams = (function () {
			var $ret = new $.ig.ScalerParams(0, windowRect, viewportRect, $self.xAxis().isInverted(), effectiveViewportRect);
			$ret._effectiveViewportRect = $self.getEffectiveViewport1(view);
			return $ret;
		}());
		var yParams = (function () {
			var $ret = new $.ig.ScalerParams(0, windowRect, viewportRect, $self.yAxis().isInverted(), effectiveViewportRect);
			$ret._effectiveViewportRect = $self.getEffectiveViewport1(view);
			return $ret;
		}());
		var contentViewport = this.getContentViewport(view);
		var minX = this.axisInfoCache().xAxis().getUnscaledValue(contentViewport.left(), xParams);
		var maxX = this.axisInfoCache().xAxis().getUnscaledValue(contentViewport.right(), xParams);
		var minY = this.axisInfoCache().yAxis().getUnscaledValue(contentViewport.bottom(), yParams);
		var maxY = this.axisInfoCache().yAxis().getUnscaledValue(contentViewport.top(), yParams);
		if (this.axisInfoCache().xAxisIsInverted()) {
			var swap = minX;
			minX = maxX;
			maxX = swap;
		}
		if (this.axisInfoCache().yAxisIsInverted()) {
			var swap1 = minY;
			minY = maxY;
			maxY = swap1;
		}
		var cache = this.axisInfoCache();
		var xAxis = cache.xAxis();
		var yAxis = cache.yAxis();
		var x;
		var y;
		var xColumn = this.xColumn();
		var yColumn = this.yColumn();
		for (var i1 = 0; i1 < this.axisInfoCache().fastItemsSource().count(); i1++) {
			x = xColumn.item(i1);
			y = yColumn.item(i1);
			if (x >= minX && x <= maxX && y >= minY && y <= maxY) {
				locations[i1].__x = xAxis.getScaledValue(x, xParams);
				locations[i1].__y = yAxis.getScaledValue(y, yParams);
			} else {
				locations[i1].__x = NaN;
				locations[i1].__y = NaN;
			}
		}
		return locations;
	}
	,
	getActiveIndexes: function (markers, indexes) {
		if (indexes == null || indexes.length != markers.activeCount()) {
			indexes = new Array(markers.activeCount());
		}
		var i = 0;
		var source = this.fastItemsSource();
		var en = markers.activeKeys().getEnumerator();
		while (en.moveNext()) {
			var key = en.current();
			indexes[i] = source.indexOf(key);
			i++;
		}
		return indexes;
	}
	,
	exportVisualDataOverride: function (svd) {
		$.ig.MarkerSeries.prototype.exportVisualDataOverride.call(this, svd);
		var trendShape = new $.ig.PolyLineVisualData(1, "trendLine", this.scatterView().trendLineManager().trendPolyline());
		trendShape.tags().add("Trend");
		svd.shapes().add(trendShape);
	}
	,
	$type: new $.ig.Type('ScatterBase', $.ig.MarkerSeries.prototype.$type, [$.ig.ISupportsErrorBars.prototype.$type])
}, true);

$.ig.util.defType('SizeScale', 'DependencyObject', {
	init: function () {
		var $self = this;
		$.ig.DependencyObject.prototype.init.call(this);
		this.series(new $.ig.List$1($.ig.Series.prototype.$type, 0));
		this.propertyUpdated = $.ig.Delegate.prototype.combine(this.propertyUpdated, function (o, e) { $self.propertyUpdatedOverride(o, e.propertyName(), e.oldValue(), e.newValue()); });
	},
	_series: null,
	series: function (value) {
		if (arguments.length === 1) {
			this._series = value;
			return value;
		} else {
			return this._series;
		}
	}
	,
	minimumValue: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.SizeScale.prototype.minimumValueProperty, value);
			return value;
		} else {
			return this.getValue($.ig.SizeScale.prototype.minimumValueProperty);
		}
	}
	,
	maximumValue: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.SizeScale.prototype.maximumValueProperty, value);
			return value;
		} else {
			return this.getValue($.ig.SizeScale.prototype.maximumValueProperty);
		}
	}
	,
	isLogarithmic: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.SizeScale.prototype.isLogarithmicProperty, value);
			return value;
		} else {
			return this.getValue($.ig.SizeScale.prototype.isLogarithmicProperty);
		}
	}
	,
	logarithmBase: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.SizeScale.prototype.logarithmBaseProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.SizeScale.prototype.logarithmBaseProperty));
		}
	}
	,
	propertyChanged: null,
	propertyUpdated: null,
	raisePropertyChanged: function (name, oldValue, newValue) {
		if (this.propertyChanged != null) {
			this.propertyChanged(this, new $.ig.PropertyChangedEventArgs(name));
		}
		if (this.propertyUpdated != null) {
			this.propertyUpdated(this, new $.ig.PropertyUpdatedEventArgs(name, oldValue, newValue));
		}
	}
	,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		var en = this.series().getEnumerator();
		while (en.moveNext()) {
			var series = en.current();
			series.renderSeries(false);
			if (series.seriesViewer() != null) {
				series.notifyThumbnailAppearanceChanged();
				series.notifySizeScalesDirty();
			}
		}
	}
	,
	$type: new $.ig.Type('SizeScale', $.ig.DependencyObject.prototype.$type, [$.ig.INotifyPropertyChanged.prototype.$type])
}, true);

$.ig.util.defType('BrushScale', 'DependencyObject', {
	init: function () {
		var $self = this;
		this.__brushes = null;
		$.ig.DependencyObject.prototype.init.call(this);
		this.series(new $.ig.List$1($.ig.Series.prototype.$type, 0));
		this.brushes(new $.ig.BrushCollection());
		var $t = this.brushes();
		$t.collectionChanged = $.ig.Delegate.prototype.combine($t.collectionChanged, this.brushes_CollectionChanged.runOn(this));
		this.propertyUpdated = $.ig.Delegate.prototype.combine(this.propertyUpdated, function (o, e) { $self.propertyUpdatedOverride(o, e.propertyName(), e.oldValue(), e.newValue()); });
	},
	brushes_CollectionChanged: function (sender, e) {
		var en = this.series().getEnumerator();
		while (en.moveNext()) {
			var series = en.current();
			series.renderSeries(false);
			series.notifyBrushScalesDirty();
			series.notifyThumbnailAppearanceChanged();
		}
	}
	,
	brushes: function (value) {
		if (arguments.length === 1) {
			if (this.__brushes != null) {
				var $t = this.__brushes;
				$t.collectionChanged = $.ig.Delegate.prototype.remove($t.collectionChanged, this.brushes_CollectionChanged.runOn(this));
			}
			this.__brushes = value;
			if (this.__brushes != null) {
				var $t1 = this.__brushes;
				$t1.collectionChanged = $.ig.Delegate.prototype.combine($t1.collectionChanged, this.brushes_CollectionChanged.runOn(this));
			}
			var en = this.series().getEnumerator();
			while (en.moveNext()) {
				var series = en.current();
				series.renderSeries(false);
				series.notifyBrushScalesDirty();
				series.notifyThumbnailAppearanceChanged();
			}
			return value;
		} else {
			return this.__brushes;
		}
	}
	,
	__brushes: null,
	_series: null,
	series: function (value) {
		if (arguments.length === 1) {
			this._series = value;
			return value;
		} else {
			return this._series;
		}
	}
	,
	registerSeries: function (series) {
		var present = this.series().contains(series);
		if (!present) {
			this.series().add(series);
		}
	}
	,
	unregisterSeries: function (series) {
		var present = this.series().contains(series);
		if (present) {
			this.series().remove(series);
		}
	}
	,
	getBrush: function (index) {
		if (this.brushes() == null || index < 0 || index >= this.brushes().count()) {
			return null;
		}
		return this.brushes().item(index);
	}
	,
	isReady: function () {
		return true;
	}
	,
	getInterpolatedBrush: function (index) {
		if (this.brushes() == null || this.brushes().count() == 0 || index < 0) {
			return null;
		}
		return this.brushes().getInterpolatedBrush(index);
	}
	,
	propertyChanged: null,
	propertyUpdated: null,
	raisePropertyChanged: function (name, oldValue, newValue) {
		if (this.propertyChanged != null) {
			this.propertyChanged(this, new $.ig.PropertyChangedEventArgs(name));
		}
		if (this.propertyUpdated != null) {
			this.propertyUpdated(this, new $.ig.PropertyUpdatedEventArgs(name, oldValue, newValue));
		}
	}
	,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		var en = this.series().getEnumerator();
		while (en.moveNext()) {
			var series = en.current();
			series.renderSeries(false);
			series.notifyBrushScalesDirty();
			series.notifyThumbnailAppearanceChanged();
		}
	}
	,
	$type: new $.ig.Type('BrushScale', $.ig.DependencyObject.prototype.$type, [$.ig.INotifyPropertyChanged.prototype.$type])
}, true);

$.ig.util.defType('BubbleSeries', 'ScatterBase', {
	createView: function () {
		return new $.ig.BubbleSeriesView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.ScatterBase.prototype.onViewCreated.call(this, view);
		this.bubbleView(view);
	}
	,
	_bubbleView: null,
	bubbleView: function (value) {
		if (arguments.length === 1) {
			this._bubbleView = value;
			return value;
		} else {
			return this._bubbleView;
		}
	}
	,
	init: function () {
		$.ig.ScatterBase.prototype.init.call(this);
		this.defaultStyleKey($.ig.BubbleSeries.prototype.$type);
		this._previousFrame = new $.ig.ScatterFrame();
		this._transitionFrame = new $.ig.ScatterFrame();
		this._currentFrame = new $.ig.ScatterFrame();
	},
	_operatingWindowRect: null,
	operatingWindowRect: function (value) {
		if (arguments.length === 1) {
			this._operatingWindowRect = value;
			return value;
		} else {
			return this._operatingWindowRect;
		}
	}
	,
	_operatingViewportRect: null,
	operatingViewportRect: function (value) {
		if (arguments.length === 1) {
			this._operatingViewportRect = value;
			return value;
		} else {
			return this._operatingViewportRect;
		}
	}
	,
	internalRadiusColumn: function () {
		return this.radiusColumn();
	}
	,
	_legendItems: null,
	legendItems: function (value) {
		if (arguments.length === 1) {
			this._legendItems = value;
			return value;
		} else {
			return this._legendItems;
		}
	}
	,
	radiusMemberPath: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.BubbleSeries.prototype.radiusMemberPathProperty, value);
			return value;
		} else {
			return this.getValue($.ig.BubbleSeries.prototype.radiusMemberPathProperty);
		}
	}
	,
	radiusColumn: function (value) {
		if (arguments.length === 1) {
			if (this.__radiusColumn != value) {
				var oldZColumn = this.radiusColumn();
				this.__radiusColumn = value;
				this.raisePropertyChanged($.ig.BubbleSeries.prototype.radiusColumnPropertyName, oldZColumn, this.radiusColumn());
			}
			return value;
		} else {
			return this.__radiusColumn;
		}
	}
	,
	__radiusColumn: null,
	radiusScale: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.BubbleSeries.prototype.radiusScaleProperty, value);
			return value;
		} else {
			return this.getValue($.ig.BubbleSeries.prototype.radiusScaleProperty);
		}
	}
	,
	labelMemberPath: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.BubbleSeries.prototype.labelMemberPathProperty, value);
			return value;
		} else {
			return this.getValue($.ig.BubbleSeries.prototype.labelMemberPathProperty);
		}
	}
	,
	__labelColumn: null,
	labelColumn: function (value) {
		if (arguments.length === 1) {
			if (this.__labelColumn != value) {
				var oldColumn = this.labelColumn();
				this.__labelColumn = value;
				this.raisePropertyChanged($.ig.BubbleSeries.prototype.labelColumnPropertyName, oldColumn, this.labelColumn());
			}
			return value;
		} else {
			return this.__labelColumn;
		}
	}
	,
	fillMemberPath: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.BubbleSeries.prototype.fillMemberPathProperty, value);
			return value;
		} else {
			return this.getValue($.ig.BubbleSeries.prototype.fillMemberPathProperty);
		}
	}
	,
	fillScale: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.BubbleSeries.prototype.fillScaleProperty, value);
			return value;
		} else {
			return this.getValue($.ig.BubbleSeries.prototype.fillScaleProperty);
		}
	}
	,
	__fillColumn: null,
	fillColumn: function (value) {
		if (arguments.length === 1) {
			if (this.__fillColumn != value) {
				var oldZColumn = this.fillColumn();
				this.__fillColumn = value;
				this.raisePropertyChanged($.ig.BubbleSeries.prototype.fillColumnPropertyName, oldZColumn, this.fillColumn());
			}
			return value;
		} else {
			return this.__fillColumn;
		}
	}
	,
	calculateCachedPoints: function (view, frame, count, windowRect, viewportRect) {
		if (count <= this.maximumMarkers()) {
			frame.cachedPoints(new $.ig.Dictionary$2($.ig.Object.prototype.$type, $.ig.OwnedPoint.prototype.$type, 1, count));
		}
		var maximumMarkers = this.maximumMarkers();
		var itemsSource = this.fastItemsSource();
		var x;
		var y;
		var effectiveViewportRect = this.getEffectiveViewport1(view);
		var px = new $.ig.ScalerParams(0, windowRect, viewportRect, this.axisInfoCache().xAxisIsInverted(), effectiveViewportRect);
		var py = new $.ig.ScalerParams(0, windowRect, viewportRect, this.axisInfoCache().yAxisIsInverted(), effectiveViewportRect);
		for (var i = 0; i < count; i++) {
			x = this.xColumn().item(i);
			y = this.yColumn().item(i);
			var point = { __x: this.axisInfoCache().xAxis().getScaledValue(x, px), __y: this.axisInfoCache().yAxis().getScaledValue(y, py), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			if (!Number.isInfinity(point.__x) && !Number.isInfinity(point.__y)) {
				var item = itemsSource.item(i);
				if (count <= maximumMarkers) {
					if (!frame.cachedPoints().containsKey(item)) {
						var columnValues = { __x: x, __y: y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
						frame.cachedPoints().add(item, (function () {
							var $ret = new $.ig.OwnedPoint();
							$ret.ownerItem(item);
							$ret.columnValues(columnValues);
							$ret.point(point);
							return $ret;
						}()));
					}
				}
			}
		}
	}
	,
	renderFrame: function (frame, view) {
		var $self = this;
		this.axisInfoCache((function () {
			var $ret = new $.ig.ScatterAxisInfoCache();
			$ret.xAxis($self.xAxis());
			$ret.yAxis($self.yAxis());
			$ret.xAxisIsInverted($self.xAxis().isInverted());
			$ret.yAxisIsInverted($self.yAxis().isInverted());
			return $ret;
		}()));
		var bubbleView = $.ig.util.cast($.ig.BubbleSeriesView.prototype.$type, view);
		bubbleView.markerManager().render(frame.markers(), this.useLightweightMarkers());
		view.renderMarkers();
		var clipper = (function () {
			var $ret = new $.ig.Clipper(1, NaN, view.viewport().bottom(), NaN, view.viewport().top(), false);
			$ret.target(view.trendLineManager().trendPolyline().points());
			return $ret;
		}());
		view.trendLineManager().rasterizeTrendLine1(frame.trendLine(), clipper);
	}
	,
	prepareFrame: function (frame, view) {
		var $self = this;
		frame.markers().clear();
		frame.trendLine().clear();
		var bubbleView = $.ig.util.cast($.ig.BubbleSeriesView.prototype.$type, view);
		var bubbleManager = bubbleView.markerManager();
		bubbleManager.radiusColumn(this.radiusColumn());
		var count = Math.min(this.xColumn() != null ? this.xColumn().count() : 0, this.yColumn() != null ? this.yColumn().count() : 0);
		var windowRect = view.windowRect();
		var viewportRect = view.viewport();
		var effectiveViewportRect = this.getEffectiveViewport1(view);
		var contentViewport = this.getContentViewport(view);
		var xParams = new $.ig.ScalerParams(0, windowRect, viewportRect, this.xAxis().isInverted(), effectiveViewportRect);
		var yParams = new $.ig.ScalerParams(0, windowRect, viewportRect, this.yAxis().isInverted(), effectiveViewportRect);
		if (count < 1) {
			return;
		}
		this.axisInfoCache((function () {
			var $ret = new $.ig.ScatterAxisInfoCache();
			$ret.xAxis($self.xAxis());
			$ret.yAxis($self.yAxis());
			$ret.xAxisIsInverted($self.xAxis().isInverted());
			$ret.yAxisIsInverted($self.yAxis().isInverted());
			$ret.fastItemsSource($self.fastItemsSource());
			return $ret;
		}()));
		this.calculateCachedPoints(bubbleView, frame, count, windowRect, viewportRect);
		bubbleView.markerManager().winnowMarkers(frame.markers(), this.maximumMarkers(), windowRect, viewportRect, this.resolution());
		bubbleView.createMarkerSizes();
		bubbleView.setMarkerColors();
		var clipper = (function () {
			var $ret = new $.ig.Clipper(0, viewportRect, false);
			$ret.target(frame.trendLine());
			return $ret;
		}());
		var xmin = this.xAxis().getUnscaledValue(contentViewport.left(), xParams);
		var xmax = this.xAxis().getUnscaledValue(contentViewport.right(), xParams);
		bubbleView.trendLineManager().prepareLine(frame.trendLine(), this.trendLineType(), this.xColumn(), this.yColumn(), this.trendLinePeriod(), function (x) { return $self.xAxis().getScaledValue(x, xParams); }, function (y) { return $self.yAxis().getScaledValue(y, yParams); }, (function () {
			var $ret = new $.ig.TrendResolutionParams();
			$ret.resolution($self.resolution());
			$ret.viewport(viewportRect);
			$ret.window(windowRect);
			return $ret;
		}()), clipper, xmin, xmax);
	}
	,
	drawLegend: function () {
		if (this.seriesViewer() == null) {
			return;
		}
		var itemLegend = $.ig.util.cast($.ig.ItemLegend.prototype.$type, this.actualLegend());
		if (itemLegend != null) {
			itemLegend.clearLegendItems(this);
			this.createLegendItems();
			itemLegend.renderLegend(this);
		}
		var scaleLegend = $.ig.util.cast($.ig.ScaleLegend.prototype.$type, this.actualLegend());
		if (scaleLegend != null) {
			scaleLegend.restoreOriginalState();
			scaleLegend.initializeLegend(this);
		}
	}
	,
	getLinearSize: function (min, max, smallSize, largeSize, value) {
		if (value <= min || $.ig.util.isNaN(value) || Number.isInfinity(value)) {
			return smallSize;
		}
		if (value >= max) {
			return largeSize;
		}
		var result = smallSize + ((largeSize - smallSize) / (max - min)) * (value - min);
		return result;
	}
	,
	getLogarithmicSize: function (min, max, smallSize, largeSize, logBase, value) {
		var newValue = Math.logBase(value, logBase);
		var newMin = Math.logBase(min, logBase);
		var newMax = Math.logBase(max, logBase);
		return $.ig.BubbleSeries.prototype.getLinearSize(newMin, newMax, smallSize, largeSize, newValue);
	}
	,
	validateSeries: function (viewportRect, windowRect, view) {
		var isValid = $.ig.ScatterBase.prototype.validateSeries.call(this, viewportRect, windowRect, view);
		if (this.radiusColumn() == null || this.fastItemsSource() == null || this.radiusColumn().count() == 0 || this.fastItemsSource().count() != this.radiusColumn().count()) {
			isValid = false;
		}
		return isValid;
	}
	,
	mustReact: function (propertyName, action) {
		if (action != $.ig.FastItemsSourceEventAction.prototype.change) {
			return true;
		}
		if (propertyName == null) {
			return true;
		}
		if (this.xMemberPath() == propertyName || this.yMemberPath() == propertyName || this.radiusMemberPath() == propertyName) {
			return true;
		}
		return false;
	}
	,
	createLegendItems: function () {
		var $self = this;
		var itemLegend = $.ig.util.cast($.ig.ItemLegend.prototype.$type, this.actualLegend());
		if (itemLegend == null || this.fastItemsSource() == null) {
			return;
		}
		this.legendItems(new $.ig.List$1($.ig.UIElement.prototype.$type, 0));
		var fastItemsSource = this.fastItemsSource();
		for (var i = 0; i < fastItemsSource.count(); i++) {
			var brush = null;
			if ($.ig.util.cast($.ig.ValueBrushScale.prototype.$type, this.fillScale()) !== null && this.fillColumn() != null) {
				brush = (this.fillScale()).getBrushByIndex(i, this.fillColumn());
			} else if ($.ig.util.cast($.ig.CustomPaletteBrushScale.prototype.$type, this.fillScale()) !== null) {
				brush = (this.fillScale()).getBrush1(i, fastItemsSource.count());
			} else if (this.fillScale() != null) {
				brush = this.fillScale().getBrush(i);
			}
			var item = new $.ig.ContentControl();
			var itemLabel = this.labelColumn() != null && this.labelColumn().item(i) != null ? this.labelColumn().item(i).toString() : "";
			item.content((function () {
				var $ret = new $.ig.DataContext();
				$ret.series($self);
				$ret.item(fastItemsSource.item(i));
				$ret.itemBrush(brush);
				$ret.itemLabel(itemLabel);
				return $ret;
			}()));
			item.contentTemplate(this.discreteLegendItemTemplate());
			this.legendItems().add(item);
		}
	}
	,
	dataUpdatedOverride: function (action, position, count, propertyName) {
		$.ig.ScatterBase.prototype.dataUpdatedOverride.call(this, action, position, count, propertyName);
		this.drawLegend();
	}
	,
	notifyBrushScalesDirty: function () {
		$.ig.ScatterBase.prototype.notifyBrushScalesDirty.call(this);
		this.drawLegend();
	}
	,
	notifySizeScalesDirty: function () {
		$.ig.ScatterBase.prototype.notifySizeScalesDirty.call(this);
		this.drawLegend();
	}
	,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		$.ig.ScatterBase.prototype.propertyUpdatedOverride.call(this, sender, propertyName, oldValue, newValue);
		switch (propertyName) {
			case $.ig.Series.prototype.fastItemsSourcePropertyName:
				if ($.ig.util.cast($.ig.IFastItemsSource.prototype.$type, oldValue) != null) {
					(oldValue).deregisterColumn(this.radiusColumn());
					(oldValue).deregisterColumn(this.fillColumn());
					(oldValue).deregisterColumn(this.labelColumn());
					this.radiusColumn(null);
					this.fillColumn(null);
					this.labelColumn(null);
				}
				if ($.ig.util.cast($.ig.IFastItemsSource.prototype.$type, newValue) != null) {
					this.radiusColumn(this.registerDoubleColumn(this.radiusMemberPath()));
					if (!String.isNullOrEmpty(this.fillMemberPath())) {
						this.fillColumn(this.registerDoubleColumn(this.fillMemberPath()));
					}
					this.labelColumn(this.registerObjectColumn(this.labelMemberPath()));
				}
				this.renderSeries(false);
				this.drawLegend();
				break;
			case $.ig.BubbleSeries.prototype.radiusMemberPathPropertyName:
				if (this.fastItemsSource() != null) {
					this.fastItemsSource().deregisterColumn(this.radiusColumn());
					this.radiusColumn(this.registerDoubleColumn(this.radiusMemberPath()));
					this.drawLegend();
				}
				break;
			case $.ig.BubbleSeries.prototype.radiusColumnPropertyName:
				this.scatterView().trendLineManager().reset();
				this.renderSeries(false);
				this.notifyThumbnailDataChanged();
				break;
			case $.ig.BubbleSeries.prototype.radiusScalePropertyName:
				this.renderSeries(false);
				this.notifyThumbnailAppearanceChanged();
				break;
			case $.ig.Series.prototype.discreteLegendItemTemplatePropertyName:
				this.drawLegend();
				break;
			case $.ig.BubbleSeries.prototype.fillScalePropertyName:
				this.renderSeries(false);
				this.drawLegend();
				this.notifyThumbnailAppearanceChanged();
				break;
			case $.ig.BubbleSeries.prototype.fillColumnPropertyName:
				this.renderSeries(false);
				this.notifyThumbnailAppearanceChanged();
				break;
			case $.ig.BubbleSeries.prototype.fillMemberPathPropertyName:
				if (this.fastItemsSource() != null) {
					this.fastItemsSource().deregisterColumn(this.fillColumn());
					this.fillColumn(this.registerDoubleColumn(this.fillMemberPath()));
					this.drawLegend();
				}
				break;
			case $.ig.BubbleSeries.prototype.labelMemberPathPropertyName:
				if (this.fastItemsSource() != null) {
					this.fastItemsSource().deregisterColumn(this.labelColumn());
					this.labelColumn(this.registerObjectColumn(this.labelMemberPath()));
					this.drawLegend();
				}
				break;
			case $.ig.Series.prototype.actualLegendPropertyName:
				var legend = $.ig.util.cast($.ig.ItemLegend.prototype.$type, oldValue);
				if (legend != null) {
					legend.clearLegendItems(this);
				}
				var scaleLegend = $.ig.util.cast($.ig.ScaleLegend.prototype.$type, oldValue);
				if (scaleLegend != null) {
					var restoreLegend = true;
					var series = null;
					if (this.seriesViewer() != null) {
						var en = this.seriesViewer().series().getEnumerator();
						while (en.moveNext()) {
							var currentSeries = en.current();
							if (currentSeries.legend() == scaleLegend) {
								series = currentSeries;
								restoreLegend = false;
							}
						}
					}
					if (restoreLegend) {
						scaleLegend.restoreOriginalState();
					} else {
						scaleLegend.initializeLegend(series);
					}
				}
				this.drawLegend();
				break;
		}
	}
	,
	sizeBubbles: function (actualMarkers, actualRadiusColumn, viewportRect, isThumbnail) {
		var min = this.radiusColumn().minimum();
		var max = this.radiusColumn().maximum();
		if (this.radiusScale() != null) {
			var smallSize = this.radiusScale().minimumValue();
			var largeSize = this.radiusScale().maximumValue();
			var logBase = this.radiusScale().logarithmBase();
			if (!this.radiusScale().series().contains(this)) {
				this.radiusScale().series().add(this);
			}
			if (this.radiusScale().isLogarithmic()) {
				for (var i = 0; i < actualRadiusColumn.count(); i++) {
					actualRadiusColumn.__inner[i] = $.ig.BubbleSeries.prototype.getLogarithmicSize(min, max, smallSize, largeSize, logBase, actualRadiusColumn.__inner[i]);
				}
			} else {
				for (var i1 = 0; i1 < actualRadiusColumn.count(); i1++) {
					actualRadiusColumn.__inner[i1] = $.ig.BubbleSeries.prototype.getLinearSize(min, max, smallSize, largeSize, actualRadiusColumn.__inner[i1]);
				}
			}
		}
		if (isThumbnail) {
			var fullWidth = viewportRect.width();
			if (!this.view().viewport().isEmpty()) {
				fullWidth = this.view().viewport().width();
			} else if (this.seriesViewer() != null && !this.seriesViewer().viewportRect().isEmpty()) {
				fullWidth = this.seriesViewer().viewportRect().width();
			}
			var scale = viewportRect.width() / fullWidth;
			for (var ii = 0; ii < actualRadiusColumn.count(); ii++) {
				actualRadiusColumn.__inner[ii] = actualRadiusColumn.__inner[ii] * scale;
			}
		}
		for (var i2 = 0; i2 < actualMarkers.count(); i2++) {
			var marker = actualMarkers.__inner[i2];
			marker.width(Math.max(0, actualRadiusColumn.__inner[i2]));
			marker.height(Math.max(0, actualRadiusColumn.__inner[i2]));
		}
	}
	,
	setMarkerColors: function (actualMarkers) {
		if (this.fillScale() != null && !this.fillScale().series().contains(this)) {
			this.fillScale().series().add(this);
		}
		var customPaletteColorAxis = $.ig.util.cast($.ig.CustomPaletteBrushScale.prototype.$type, this.fillScale());
		var valueBrushScale = $.ig.util.cast($.ig.ValueBrushScale.prototype.$type, this.fillScale());
		var brushScale = this.fillScale();
		var clearMarkerBrushes = this.fillScale() == null || !this.fillScale().isReady() || (valueBrushScale != null && this.fillMemberPath() == null);
		if (clearMarkerBrushes) {
			this.bubbleView().clearMarkerBrushes();
			var bubbleThumbnailView = $.ig.util.cast($.ig.BubbleSeriesView.prototype.$type, this.thumbnailView());
			if (bubbleThumbnailView != null) {
				bubbleThumbnailView.clearMarkerBrushes();
			}
			return;
		}
		var markerCount = actualMarkers.count();
		var fastItemsSource = this.fastItemsSource();
		for (var i = 0; i < markerCount; i++) {
			var marker = actualMarkers.__inner[i];
			var markerContext = $.ig.util.cast($.ig.DataContext.prototype.$type, marker.content());
			if (markerContext != null) {
				var brush = null;
				var markerIndex = fastItemsSource.indexOf(markerContext.item());
				if (customPaletteColorAxis != null) {
					brush = customPaletteColorAxis.getBrush1(markerIndex, fastItemsSource.count());
				} else if (valueBrushScale != null) {
					brush = valueBrushScale.getBrushByIndex(markerIndex, this.fillColumn());
				} else if (brushScale != null) {
					brush = brushScale.getBrush(markerIndex);
				}
				markerContext.itemBrush(brush);
			}
		}
	}
	,
	$type: new $.ig.Type('BubbleSeries', $.ig.ScatterBase.prototype.$type)
}, true);

$.ig.util.defType('CustomPaletteBrushScale', 'BrushScale', {
	init: function () {
		$.ig.BrushScale.prototype.init.call(this);
	},
	brushSelectionMode: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.CustomPaletteBrushScale.prototype.brushSelectionModeProperty, $.ig.BrushSelectionMode.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.CustomPaletteBrushScale.prototype.brushSelectionModeProperty));
		}
	}
	,
	getBrush1: function (index, total) {
		if (this.brushes() == null || this.brushes().count() == 0) {
			return null;
		}
		if (this.brushSelectionMode() == $.ig.BrushSelectionMode.prototype.select) {
			return $.ig.BrushScale.prototype.getBrush.call(this, index % this.brushes().count());
		}
		var scaledIndex = $.ig.BubbleSeries.prototype.getLinearSize(0, total - 1, 0, this.brushes().count() - 1, index);
		return this.getInterpolatedBrush(scaledIndex);
	}
	,
	isReady: function () {
		return this.brushes() != null && this.brushes().count() > 0;
	}
	,
	$type: new $.ig.Type('CustomPaletteBrushScale', $.ig.BrushScale.prototype.$type)
}, true);

$.ig.util.defType('ValueBrushScale', 'BrushScale', {
	init: function () {
		this.__currentIsLogarithmic = false;
		this.__currentLogarithmBase = 10;
		$.ig.BrushScale.prototype.init.call(this);
	},
	minimumValue: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.ValueBrushScale.prototype.minimumValueProperty, value);
			return value;
		} else {
			return this.getValue($.ig.ValueBrushScale.prototype.minimumValueProperty);
		}
	}
	,
	maximumValue: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.ValueBrushScale.prototype.maximumValueProperty, value);
			return value;
		} else {
			return this.getValue($.ig.ValueBrushScale.prototype.maximumValueProperty);
		}
	}
	,
	isLogarithmic: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.ValueBrushScale.prototype.isLogarithmicProperty, value);
			return value;
		} else {
			return this.getValue($.ig.ValueBrushScale.prototype.isLogarithmicProperty);
		}
	}
	,
	__currentIsLogarithmic: false,
	logarithmBase: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.ValueBrushScale.prototype.logarithmBaseProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.ValueBrushScale.prototype.logarithmBaseProperty));
		}
	}
	,
	__currentLogarithmBase: 0,
	getBrushByIndex: function (index, FillColumn) {
		if (FillColumn == null || this.brushes() == null || this.brushes().count() == 0 || index < 0 || index >= FillColumn.count()) {
			return null;
		}
		if (FillColumn.count() == 0) {
			return this.brushes().item(0);
		}
		var min = $.ig.util.isNaN(this.minimumValue()) || Number.isInfinity(this.minimumValue()) ? FillColumn.minimum() : this.minimumValue();
		var max = $.ig.util.isNaN(this.maximumValue()) || Number.isInfinity(this.maximumValue()) ? FillColumn.maximum() : this.maximumValue();
		var value = FillColumn.item(index);
		if (min == max) {
			return value == min ? this.brushes().item(0) : null;
		}
		return this.getInterpolatedBrushLogarithmic(min, max, value);
	}
	,
	getBrushByValue: function (value, FillColumn) {
		if (FillColumn == null || this.brushes() == null || this.brushes().count() == 0) {
			return null;
		}
		if (FillColumn.count() <= 1) {
			return this.brushes().item(0);
		}
		var min = $.ig.util.isNaN(this.minimumValue()) || Number.isInfinity(this.minimumValue()) ? FillColumn.minimum() : this.minimumValue();
		var max = $.ig.util.isNaN(this.maximumValue()) || Number.isInfinity(this.maximumValue()) ? FillColumn.maximum() : this.maximumValue();
		if (value < min) {
			return null;
		}
		if (value > max) {
			return null;
		}
		return this.getInterpolatedBrushLogarithmic(min, max, value);
	}
	,
	getInterpolatedBrushLogarithmic: function (min, max, value) {
		if (this.__currentIsLogarithmic && this.__currentLogarithmBase > 0) {
			var newMin = Math.logBase(min, this.__currentLogarithmBase);
			var newMax = Math.logBase(max, this.__currentLogarithmBase);
			var newValue = Math.logBase(value, this.__currentLogarithmBase);
			return this.getInterpolatedBrushLinear(newMin, newMax, newValue);
		}
		return this.getInterpolatedBrushLinear(min, max, value);
	}
	,
	getInterpolatedBrushLinear: function (min, max, value) {
		if (value < min || value > max) {
			return null;
		}
		var scaledValue = (value - min) / (max - min);
		var scaledBrushIndex = scaledValue * (this.brushes().count() - 1);
		return this.getInterpolatedBrush(scaledBrushIndex);
	}
	,
	isReady: function () {
		return this.brushes() != null && this.brushes().count() > 0;
	}
	,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		switch (propertyName) {
			case $.ig.ValueBrushScale.prototype.logarithmBasePropertyName:
				this.__currentLogarithmBase = $.ig.util.getValue(newValue);
				break;
			case $.ig.ValueBrushScale.prototype.isLogarithmicPropertyName:
				this.__currentIsLogarithmic = newValue;
				break;
		}
		$.ig.BrushScale.prototype.propertyUpdatedOverride.call(this, sender, propertyName, oldValue, newValue);
	}
	,
	$type: new $.ig.Type('ValueBrushScale', $.ig.BrushScale.prototype.$type)
}, true);

$.ig.util.defType('NumericMarkerManager', 'MarkerManagerBase', {
	init: function (initNumber, provideMarkerStrategy, provideItemStrategy, removeUnusedMarkers, getItemLocationsStrategy, activeMarkerIndexesStrategy) {
		if (initNumber > 0) {
			switch (initNumber) {
				case 1:
					this.init1.apply(this, arguments);
					break;
			}
			return;
		}
		$.ig.NumericMarkerManager.prototype.init1.call(this, 1, provideMarkerStrategy, provideItemStrategy, removeUnusedMarkers, getItemLocationsStrategy, activeMarkerIndexesStrategy, function () {
			return $.ig.CollisionAvoidanceType.prototype.none;
		});
	},
	init1: function (initNumber, provideMarkerStrategy, provideItemStrategy, removeUnusedMarkers, getItemLocationsStrategy, activeMarkerIndexesStrategy, getCollisionAvoidanceStrategy) {
		$.ig.MarkerManagerBase.prototype.init.call(this, provideMarkerStrategy, provideItemStrategy, removeUnusedMarkers, getItemLocationsStrategy, activeMarkerIndexesStrategy);
		this.populateColumnValues(false);
		this.getColumnValues(function (i) { return { __x: 0, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }; });
		this.getCollisionAvoidanceStrategy(getCollisionAvoidanceStrategy);
	},
	_populateColumnValues: false,
	populateColumnValues: function (value) {
		if (arguments.length === 1) {
			this._populateColumnValues = value;
			return value;
		} else {
			return this._populateColumnValues;
		}
	}
	,
	_getColumnValues: null,
	getColumnValues: function (value) {
		if (arguments.length === 1) {
			this._getColumnValues = value;
			return value;
		} else {
			return this._getColumnValues;
		}
	}
	,
	_getCollisionAvoidanceStrategy: null,
	getCollisionAvoidanceStrategy: function (value) {
		if (arguments.length === 1) {
			this._getCollisionAvoidanceStrategy = value;
			return value;
		} else {
			return this._getCollisionAvoidanceStrategy;
		}
	}
	,
	_getMarkerDesiredSize: null,
	getMarkerDesiredSize: function (value) {
		if (arguments.length === 1) {
			this._getMarkerDesiredSize = value;
			return value;
		} else {
			return this._getMarkerDesiredSize;
		}
	}
	,
	winnowMarkers: function (markers, maximumMarkers, windowRect, viewportRect, currentResolution) {
		var itemLocations = this.getItemLocationsStrategy()();
		markers.clear();
		var visibleItems = new $.ig.List$1($.ig.Number.prototype.$type, 0);
		maximumMarkers = Math.max(0, maximumMarkers);
		var markerItems = null;
		this.getVisibleItems(windowRect, viewportRect, itemLocations, visibleItems);
		if (maximumMarkers >= visibleItems.count()) {
			markerItems = visibleItems;
		} else {
			markerItems = new $.ig.List$1($.ig.Number.prototype.$type, 0);
			var resolution = Math.max(8, currentResolution);
			var buckets = this.getBuckets(viewportRect, visibleItems, resolution, itemLocations);
			var keys = new $.ig.List$1($.ig.Number.prototype.$type, 1, buckets.keys());
			if ($.ig.MarkerManagerBase.prototype.useDeterministicSelection()) {
				keys.sort();
			}
			this.selectMarkerItems(maximumMarkers, buckets, keys, markerItems);
		}
		this.assignMarkers(markers, itemLocations, markerItems);
	}
	,
	assignMarkers: function (markers, itemLocations, markerItems) {
		for (var i = 0; i < markerItems.count(); ++i) {
			var index = markerItems.__inner[i];
			var point = itemLocations[index];
			var item = this.provideItemStrategy()(index);
			var marker = this.provideMarkerStrategy()(item);
			if (marker.content() != null) {
				(marker.content()).item(item);
			}
			var mp = new $.ig.OwnedPoint();
			if (this.populateColumnValues()) {
				mp.columnValues(this.getColumnValues()(index));
			}
			mp.ownerItem(item);
			mp.point({ __x: point.__x, __y: point.__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			if (!markers.containsKey(item)) {
				markers.add(item, mp);
			}
		}
	}
	,
	render: function (markers, lightweight) {
		var keys = markers.keys();
		if ($.ig.MarkerManagerBase.prototype.useDeterministicSelection()) {
			var keysList = new $.ig.List$1($.ig.Object.prototype.$type, 1, markers.keys());
			keysList.sort2(function (o1, o2) {
				var point1 = markers.item(o1);
				var point2 = markers.item(o2);
				var dist1 = Math.pow(point1.point().__x, 2) + Math.pow(point1.point().__y, 2);
				var dist2 = Math.pow(point2.point().__x, 2) + Math.pow(point2.point().__y, 2);
				return (dist1).compareTo(dist2);
			});
			keys = keysList;
		}
		var smartPlacer = null;
		var wrapper = null;
		switch (this.getCollisionAvoidanceStrategy()()) {
			case $.ig.CollisionAvoidanceType.prototype.none: break;
			case $.ig.CollisionAvoidanceType.prototype.omit:
				smartPlacer = (function () {
					var $ret = new $.ig.SmartPlacer();
					$ret.overlap(0.3);
					$ret.fade(0);
					return $ret;
				}());
				wrapper = new $.ig.SmartPlaceableWrapper$1($.ig.Marker.prototype.$type);
				wrapper.noWiggle(true);
				break;
			case $.ig.CollisionAvoidanceType.prototype.fade:
				smartPlacer = (function () {
					var $ret = new $.ig.SmartPlacer();
					$ret.overlap(0.6);
					$ret.fade(2);
					return $ret;
				}());
				wrapper = new $.ig.SmartPlaceableWrapper$1($.ig.Marker.prototype.$type);
				wrapper.noWiggle(true);
				break;
			case $.ig.CollisionAvoidanceType.prototype.omitAndShift:
				smartPlacer = (function () {
					var $ret = new $.ig.SmartPlacer();
					$ret.overlap(0.3);
					$ret.fade(0);
					return $ret;
				}());
				wrapper = new $.ig.SmartPlaceableWrapper$1($.ig.Marker.prototype.$type);
				break;
			case $.ig.CollisionAvoidanceType.prototype.fadeAndShift:
				smartPlacer = (function () {
					var $ret = new $.ig.SmartPlacer();
					$ret.overlap(0.6);
					$ret.fade(2);
					return $ret;
				}());
				wrapper = new $.ig.SmartPlaceableWrapper$1($.ig.Marker.prototype.$type);
				break;
		}
		var en = keys.getEnumerator();
		while (en.moveNext()) {
			var key = en.current();
			var point = markers.item(key);
			var marker = this.provideMarkerStrategy()(point.ownerItem());
			if (smartPlacer != null && wrapper != null) {
				wrapper.element(marker);
				wrapper.elementDesiredSize(this.getMarkerDesiredSize()(marker));
				wrapper.originalLocation(point.point());
				smartPlacer.place(wrapper);
				if (wrapper.opacity() == 0) {
					wrapper.smartPosition(wrapper.smartPosition());
				}
				point.point(wrapper.elementLocationResult());
			} else {
				marker.__opacity = 1;
				marker.__visibility = $.ig.Visibility.prototype.visible;
			}
			this.updateMarkerPosition(marker, point, lightweight);
		}
		this.removeUnusedMarkers()(markers);
	}
	,
	updateMarkerPosition: function (marker, point, lightweight) {
		marker.canvasLeft(point.point().__x);
		marker.canvasTop(point.point().__y);
	}
	,
	_series: null,
	series: function (value) {
		if (arguments.length === 1) {
			this._series = value;
			return value;
		} else {
			return this._series;
		}
	}
	,
	$type: new $.ig.Type('NumericMarkerManager', $.ig.MarkerManagerBase.prototype.$type)
}, true);

$.ig.util.defType('MarkerManagerBucket', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	__items: null,
	items: function () {
		if (this.__items == null) {
			this.__items = new $.ig.List$1($.ig.Number.prototype.$type, 0);
		}
		return this.__items;
	}
	,
	__priorityItems: null,
	priorityItems: function () {
		if (this.__priorityItems == null) {
			this.__priorityItems = new $.ig.List$1($.ig.Number.prototype.$type, 0);
		}
		return this.__priorityItems;
	}
	,
	getItem: function (wasPriority) {
		if (this.priorityItems().count() > 0) {
			var priorityIndex = this.priorityItems().__inner[this.priorityItems().count() - 1];
			this.priorityItems().removeAt(this.priorityItems().count() - 1);
			wasPriority = true;
			return {
				ret: priorityIndex,
				p0: wasPriority
			};
		}
		var index = this.items().__inner[this.items().count() - 1];
		this.items().removeAt(this.items().count() - 1);
		wasPriority = false;
		return {
			ret: index,
			p0: wasPriority
		};
	}
	,
	isEmpty: function () {
		return this.items().count() == 0 && this.priorityItems().count() == 0;
	}
	,
	$type: new $.ig.Type('MarkerManagerBucket', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('ScatterTrendLineManager', 'TrendLineManagerBase$1', {
	init: function () {
		$.ig.TrendLineManagerBase$1.prototype.init.call(this, $.ig.Point.prototype.$type);
	},
	prepareLine: function (flattenedPoints, trendLineType, XColumn, YColumn, period, GetScaledXValue, GetScaledYValue, trendResolutionParams, clipper, min, max) {
		var xmin = min;
		var xmax = max;
		var trend = new $.ig.List$1($.ig.Point.prototype.$type, 0);
		var count = 0;
		if (XColumn != null) {
			count = XColumn.count();
		}
		if (YColumn != null) {
			count = Math.min(count, YColumn.count());
		}
		if (!trendResolutionParams.window().isEmpty() && !trendResolutionParams.viewport().isEmpty()) {
			if (trendLineType == $.ig.TrendLineType.prototype.none) {
				this.trendCoefficients(null);
				this.trendColumn().clear();
			} else if (this.isFit(trendLineType)) {
				this.trendColumn().clear();
				this.trendCoefficients($.ig.TrendFitCalculator.prototype.calculateFit(trend, trendLineType, trendResolutionParams, this.trendCoefficients(), count, function (i) { return XColumn.item(i); }, function (i) { return YColumn.item(i); }, GetScaledXValue, GetScaledYValue, xmin, xmax));
			} else if (this.isAverage(trendLineType)) {
				this.trendCoefficients(null);
				this.trendColumn().clear();
				$.ig.TrendAverageCalculator.prototype.calculateXYAverage(trendLineType, this.trendColumn(), XColumn, YColumn, period);
				var en = this.trendColumn().getEnumerator();
				while (en.moveNext()) {
					var point = en.current();
					var xi = GetScaledXValue(point.__x);
					var yi = GetScaledYValue(point.__y);
					if (!$.ig.util.isNaN(xi) && !$.ig.util.isNaN(yi)) {
						trend.add({ __x: xi, __y: yi, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
					}
				}
			}
			this.flattenTrendLine1(trend, trendResolutionParams, flattenedPoints, clipper);
		}
	}
	,
	$type: new $.ig.Type('ScatterTrendLineManager', $.ig.TrendLineManagerBase$1.prototype.$type.specialize($.ig.Point.prototype.$type))
}, true);

$.ig.util.defType('ErrorBarsHelper', 'Object', {
	init: function (errorBarsHost, viewportHost) {
		$.ig.Object.prototype.init.call(this);
		this.errorBarsHost(errorBarsHost);
		this.viewportHost(viewportHost);
	},
	_errorBarsHost: null,
	errorBarsHost: function (value) {
		if (arguments.length === 1) {
			this._errorBarsHost = value;
			return value;
		} else {
			return this._errorBarsHost;
		}
	}
	,
	_viewportHost: null,
	viewportHost: function (value) {
		if (arguments.length === 1) {
			this._viewportHost = value;
			return value;
		} else {
			return this._viewportHost;
		}
	}
	,
	isCalculatorIndependent: function (calculator) {
		var type = calculator.getCalculatorType();
		if (type == $.ig.ErrorBarCalculatorType.prototype.percentage || type == $.ig.ErrorBarCalculatorType.prototype.data) {
			return false;
		} else {
			return true;
		}
	}
	,
	calculateIndependentErrorBarPosition: function (calculator, position) {
		if (calculator.hasConstantPosition()) {
			position = calculator.getPosition();
		}
		return {
			p1: position
		};
	}
	,
	calculateStdDevErrorBarSize: function (unscaledValue, calculator, axis, positiveSize, negativeSize) {
		var windowRect;
		var viewportRect;
		var effectiveViewportRect;
		var $ret = this.viewportHost().getViewInfo(viewportRect, windowRect);
		viewportRect = $ret.p0;
		windowRect = $ret.p1;
		effectiveViewportRect = this.viewportHost().getEffectiveViewport();
		var sParams = new $.ig.ScalerParams(0, windowRect, viewportRect, axis.isInverted(), effectiveViewportRect);
		var scaledValue = axis.getScaledValue(unscaledValue, sParams);
		var errorBarValue = calculator.getIndependentValue();
		positiveSize = Math.abs(Math.round(axis.getScaledValue(unscaledValue + errorBarValue, sParams) - scaledValue));
		negativeSize = Math.abs(Math.round(axis.getScaledValue(unscaledValue - errorBarValue, sParams) - scaledValue));
		return {
			p3: positiveSize,
			p4: negativeSize
		};
	}
	,
	calculateIndependentErrorBarSize: function (scaledValue, calculator, axis, positiveSize, negativeSize) {
		var windowRect;
		var viewportRect;
		var $ret = this.viewportHost().getViewInfo(viewportRect, windowRect);
		viewportRect = $ret.p0;
		windowRect = $ret.p1;
		var sParams = new $.ig.ScalerParams(1, windowRect, viewportRect, axis.isInverted());
		var unscaledValue = axis.getUnscaledValue(scaledValue, sParams);
		var errorBarValue = calculator.getIndependentValue();
		positiveSize = Math.abs(Math.round(axis.getScaledValue(unscaledValue + errorBarValue, sParams) - scaledValue));
		negativeSize = Math.abs(Math.round(axis.getScaledValue(unscaledValue - errorBarValue, sParams) - scaledValue));
		return {
			p3: positiveSize,
			p4: negativeSize
		};
	}
	,
	calculateDependentErrorBarSize1: function (scaledValue, calculator, axis, positiveSize, negativeSize) {
		var windowRect;
		var viewportRect;
		var $ret = this.viewportHost().getViewInfo(viewportRect, windowRect);
		viewportRect = $ret.p0;
		windowRect = $ret.p1;
		var sParams = new $.ig.ScalerParams(1, windowRect, viewportRect, axis.isInverted());
		var unscaledValue = axis.getUnscaledValue(scaledValue, sParams);
		var errorBarValue = calculator.getDependentValue(unscaledValue);
		positiveSize = Math.abs(Math.round(axis.getScaledValue(unscaledValue + errorBarValue, sParams) - scaledValue));
		negativeSize = Math.abs(Math.round(axis.getScaledValue(unscaledValue - errorBarValue, sParams) - scaledValue));
		return {
			p3: positiveSize,
			p4: negativeSize
		};
	}
	,
	calculateDependentErrorBarSize: function (scaledValue, calculator, refAxis, targetAxis, positiveSize, negativeSize) {
		var windowRect;
		var viewportRect;
		var $ret = this.viewportHost().getViewInfo(viewportRect, windowRect);
		viewportRect = $ret.p0;
		windowRect = $ret.p1;
		var refParams = new $.ig.ScalerParams(1, windowRect, viewportRect, refAxis.isInverted());
		var targetParams = new $.ig.ScalerParams(1, windowRect, viewportRect, targetAxis.isInverted());
		var unscaledValue = refAxis.getUnscaledValue(scaledValue, refParams);
		var errorBarValue = calculator.getDependentValue(unscaledValue);
		positiveSize = Math.abs(Math.round(refAxis.getScaledValue(unscaledValue + errorBarValue, targetParams) - scaledValue));
		negativeSize = Math.abs(Math.round(refAxis.getScaledValue(unscaledValue - errorBarValue, targetParams) - scaledValue));
		return {
			p4: positiveSize,
			p5: negativeSize
		};
	}
	,
	calculateFromUnscaledErrorBarValue: function (value, unscaledErrorBarValue, axis, positiveSize, negativeSize) {
		var windowRect;
		var viewportRect;
		var $ret = this.viewportHost().getViewInfo(viewportRect, windowRect);
		viewportRect = $ret.p0;
		windowRect = $ret.p1;
		var sParams = new $.ig.ScalerParams(1, windowRect, viewportRect, axis.isInverted());
		var unscaledValue = axis.getUnscaledValue(value, sParams);
		positiveSize = Math.abs(Math.round(axis.getScaledValue(unscaledValue + unscaledErrorBarValue, sParams) - value));
		negativeSize = Math.abs(Math.round(axis.getScaledValue(unscaledValue - unscaledErrorBarValue, sParams) - value));
		return {
			p3: positiveSize,
			p4: negativeSize
		};
	}
	,
	addErrorBarVertical: function (errorBarsGeometry, position, errorBarLength, positive) {
	}
	,
	addErrorBarHorizontal: function (errorBarsGeomety, position, errorBarLength, positive) {
	}
	,
	calculateErrorBarCenterHorizontal: function (calculator, axis, point, mean) {
		var center = new $.ig.Point(0);
		if (calculator.getCalculatorType() == $.ig.ErrorBarCalculatorType.prototype.standardDeviation) {
			var windowRect;
			var viewportRect;
			var effectiveViewportRect;
			var $ret = this.viewportHost().getViewInfo(viewportRect, windowRect);
			viewportRect = $ret.p0;
			windowRect = $ret.p1;
			effectiveViewportRect = this.viewportHost().getEffectiveViewport();
			var sParams = new $.ig.ScalerParams(0, windowRect, viewportRect, axis.isInverted(), effectiveViewportRect);
			center.__x = Math.round(axis.getScaledValue(mean, sParams));
			center.__y = Math.round(point.__y);
		} else {
			center.__x = Math.round(point.__x);
			center.__y = Math.round(point.__y);
		}
		return center;
	}
	,
	calculateErrorBarCenterVertical: function (calculator, axis, point, mean) {
		var center = new $.ig.Point(0);
		if (calculator.getCalculatorType() == $.ig.ErrorBarCalculatorType.prototype.standardDeviation) {
			var windowRect;
			var viewportRect;
			var effectiveViewportRect;
			var $ret = this.viewportHost().getViewInfo(viewportRect, windowRect);
			viewportRect = $ret.p0;
			windowRect = $ret.p1;
			effectiveViewportRect = this.viewportHost().getEffectiveViewport();
			var sParams = new $.ig.ScalerParams(0, windowRect, viewportRect, axis.isInverted(), effectiveViewportRect);
			center.__x = Math.round(point.__x);
			center.__y = Math.round(axis.getScaledValue(mean, sParams));
		} else {
			center.__x = Math.round(point.__x);
			center.__y = Math.round(point.__y);
		}
		return center;
	}
	,
	$type: new $.ig.Type('ErrorBarsHelper', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('ScaleLegend', 'LegendBase', {
	createView: function () {
		return new $.ig.ScaleLegendView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.LegendBase.prototype.onViewCreated.call(this, view);
		this.scaleView(view);
	}
	,
	_scaleView: null,
	scaleView: function (value) {
		if (arguments.length === 1) {
			this._scaleView = value;
			return value;
		} else {
			return this._scaleView;
		}
	}
	,
	legendScaleElement: function () {
		return this.scaleView().legendScaleElement();
	}
	,
	minText: function () {
		return this.scaleView().minText();
	}
	,
	maxText: function () {
		return this.scaleView().maxText();
	}
	,
	init: function () {
		$.ig.LegendBase.prototype.init.call(this);
		this.defaultStyleKey($.ig.ScaleLegend.prototype.$type);
	},
	parentVisibility: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.ScaleLegend.prototype.parentVisibilityProperty, $.ig.Visibility.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.ScaleLegend.prototype.parentVisibilityProperty));
		}
	}
	,
	seriesMarkerBrush: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.ScaleLegend.prototype.seriesMarkerBrushProperty, value);
			return value;
		} else {
			return this.getValue($.ig.ScaleLegend.prototype.seriesMarkerBrushProperty);
		}
	}
	,
	_minimumValue: 0,
	minimumValue: function (value) {
		if (arguments.length === 1) {
			this._minimumValue = value;
			return value;
		} else {
			return this._minimumValue;
		}
	}
	,
	_maximumValue: 0,
	maximumValue: function (value) {
		if (arguments.length === 1) {
			this._maximumValue = value;
			return value;
		} else {
			return this._maximumValue;
		}
	}
	,
	_brushes: null,
	brushes: function (value) {
		if (arguments.length === 1) {
			this._brushes = value;
			return value;
		} else {
			return this._brushes;
		}
	}
	,
	_sizeValueColumn: null,
	sizeValueColumn: function (value) {
		if (arguments.length === 1) {
			this._sizeValueColumn = value;
			return value;
		} else {
			return this._sizeValueColumn;
		}
	}
	,
	_brushValueColumn: null,
	brushValueColumn: function (value) {
		if (arguments.length === 1) {
			this._brushValueColumn = value;
			return value;
		} else {
			return this._brushValueColumn;
		}
	}
	,
	_brushScale: null,
	brushScale: function (value) {
		if (arguments.length === 1) {
			this._brushScale = value;
			return value;
		} else {
			return this._brushScale;
		}
	}
	,
	__series: null,
	series: function (value) {
		if (arguments.length === 1) {
			var $self = this;
			this.__series = value;
			this.setBinding($.ig.ScaleLegend.prototype.parentVisibilityProperty, (function () {
				var $ret = new $.ig.Binding(1, "Visibility");
				$ret.source($self.__series);
				return $ret;
			}()));
			this.setBinding($.ig.ScaleLegend.prototype.seriesMarkerBrushProperty, (function () {
				var $ret = new $.ig.Binding(1, $.ig.MarkerSeries.prototype.markerBrushPropertyName);
				$ret.source($self.__series);
				return $ret;
			}()));
			return value;
		} else {
			return this.__series;
		}
	}
	,
	_series_PropertyChanged: function (sender, e) {
		switch (e.propertyName()) {
			case "Visibility":
				this.parentVisibility(this.__series.__visibility);
				break;
			case $.ig.MarkerSeries.prototype.actualMarkerBrushPropertyName:
				this.seriesMarkerBrush(this.__series.actualMarkerBrush());
				break;
		}
	}
	,
	restoreOriginalState: function () {
		this.scaleView().restoreOriginalState();
	}
	,
	getBrush: function (index) {
		if (this.series() == null) {
			return null;
		}
		var customPaletteBrushScale = $.ig.util.cast($.ig.CustomPaletteBrushScale.prototype.$type, this.series().fillScale());
		var valueBrushScale = $.ig.util.cast($.ig.ValueBrushScale.prototype.$type, this.series().fillScale());
		var brushScale = this.series().fillScale();
		if (customPaletteBrushScale != null && this.series().fastItemsSource() != null) {
			return customPaletteBrushScale.getBrush1(index, this.series().fastItemsSource().count());
		} else if (valueBrushScale != null) {
			return valueBrushScale.getBrushByIndex(index, this.brushValueColumn());
		} else if (brushScale != null) {
			return brushScale.getBrush(index);
		}
		return null;
	}
	,
	getFirstColor: function (brush) {
		if (brush == null) {
			return this.scaleView().getTransparentBrush();
		}
		return $.ig.ColorUtil.prototype.getColor(brush);
	}
	,
	initializeLegend: function (series) {
		var bubbleSeries = $.ig.util.cast($.ig.BubbleSeries.prototype.$type, series);
		if (bubbleSeries == null || series.__visibility != $.ig.Visibility.prototype.visible) {
			return;
		}
		this.sizeValueColumn(bubbleSeries.internalRadiusColumn());
		this.brushScale(bubbleSeries.fillScale());
		this.brushValueColumn(bubbleSeries.fillColumn());
		this.series(bubbleSeries);
		this.brushes(new $.ig.ObservableCollection$1($.ig.Brush.prototype.$type, 0));
		this.renderLegend();
	}
	,
	renderLegend: function () {
		if (this.legendScaleElement() == null || this.sizeValueColumn() == null || this.sizeValueColumn().count() == 0) {
			return;
		}
		if (this.series() == null || this.series().actualLegend() != this) {
			return;
		}
		var useSeriesBrush = false;
		var legendScaleShapeElement = $.ig.util.cast($.ig.Shape.prototype.$type, this.legendScaleElement());
		if (legendScaleShapeElement != null) {
			var gradient = this.scaleView().buildGradient();
			for (var i = 0; i < this.sizeValueColumn().count(); i++) {
				if (this.brushScale() == null || !this.brushScale().isReady()) {
					if (this.series() != null) {
						useSeriesBrush = true;
					}
					break;
				}
				var scaledColorIndex = NaN;
				if (this.brushValueColumn() != null) {
					scaledColorIndex = (this.brushValueColumn().item(i) - this.brushValueColumn().minimum()) / (this.brushValueColumn().maximum() - this.brushValueColumn().minimum());
				}
				var colorOffset = (this.sizeValueColumn().item(i) - this.sizeValueColumn().minimum()) / (this.sizeValueColumn().maximum() - this.sizeValueColumn().minimum());
				if ($.ig.util.isNaN(scaledColorIndex)) {
					scaledColorIndex = 0;
				}
				if ($.ig.util.isNaN(colorOffset)) {
					colorOffset = scaledColorIndex;
				}
				var defaultColor = this.series() != null ? this.getFirstColor(this.series().actualMarkerBrush()) : this.scaleView().getTransparentBrush();
				var brush = this.getBrush(i);
				var color = brush != null ? this.getFirstColor(brush) : defaultColor;
				this.scaleView().addGradientStop(gradient, color, colorOffset);
			}
			this.scaleView().setScaleFill(legendScaleShapeElement, useSeriesBrush, gradient);
		}
		if (this.minText() != null) {
			this.minText().text((Math.round(this.sizeValueColumn().minimum() * 1000) / 1000).toString());
		}
		if (this.maxText() != null) {
			this.maxText().text((Math.round(this.sizeValueColumn().maximum() * 1000) / 1000).toString());
		}
	}
	,
	$type: new $.ig.Type('ScaleLegend', $.ig.LegendBase.prototype.$type)
}, true);

$.ig.util.defType('ScatterFrameBase$1', 'Frame', {
	$t: null,
	init: function ($t) {
		this.$t = $t;
		this.$type = this.$type.specialize(this.$t);
		$.ig.Frame.prototype.init.call(this);
		this.points(new $.ig.List$1($.ig.Point.prototype.$type, 0));
		this.cachedPoints(new $.ig.Dictionary$2($.ig.Object.prototype.$type, $.ig.OwnedPoint.prototype.$type, 0));
		this.markers(new $.ig.Dictionary$2($.ig.Object.prototype.$type, $.ig.OwnedPoint.prototype.$type, 0));
		this.trendLine(new $.ig.List$1($.ig.Point.prototype.$type, 0));
		this.horizontalErrorBars(new $.ig.Dictionary$2($.ig.Object.prototype.$type, $.ig.OwnedPoint.prototype.$type, 0));
		this.verticalErrorBars(new $.ig.Dictionary$2($.ig.Object.prototype.$type, $.ig.OwnedPoint.prototype.$type, 0));
		this.horizontalErrorBarWidths(new $.ig.Dictionary$2($.ig.Object.prototype.$type, Array, 0));
		this.verticalErrorBarHeights(new $.ig.Dictionary$2($.ig.Object.prototype.$type, Array, 0));
		this.getNewMinValue(function (maxPoint, minFrame, maxFrame) { return maxPoint; });
		this.ownedPointDictInterpolator(new $.ig.DictInterpolator$3($.ig.Object.prototype.$type, $.ig.OwnedPoint.prototype.$type, this.$t, this.interpolatePoint.runOn(this), function (p) { return p.ownerItem(); }, function (p) { return !$.ig.util.isNaN(p.point().__x) && !$.ig.util.isNaN(p.point().__y); }, function () { return new $.ig.OwnedPoint(); }));
	},
	_ownedPointDictInterpolator: null,
	ownedPointDictInterpolator: function (value) {
		if (arguments.length === 1) {
			this._ownedPointDictInterpolator = value;
			return value;
		} else {
			return this._ownedPointDictInterpolator;
		}
	}
	,
	interpolate3: function (p, minFrame, maxFrame) {
		var min = minFrame;
		var max = maxFrame;
		if ($.ig.util.getBoxIfEnum(this.$t, min) == null || $.ig.util.getBoxIfEnum(this.$t, max) == null) {
			return;
		}
		this.ownedPointDictInterpolator().interpolate(this.cachedPoints(), p, min.cachedPoints(), max.cachedPoints(), min, max);
		$.ig.Frame.prototype.interpolate(this.points(), p, min.points(), max.points());
		this.ownedPointDictInterpolator().interpolate(this.markers(), p, min.markers(), max.markers(), min, max);
		this.ownedPointDictInterpolator().interpolate(this.horizontalErrorBars(), p, min.horizontalErrorBars(), max.horizontalErrorBars(), min, max);
		this.ownedPointDictInterpolator().interpolate(this.verticalErrorBars(), p, min.verticalErrorBars(), max.verticalErrorBars(), min, max);
		this.addPointsThatSweepThroughTheView(this.markers(), p, min, max);
		$.ig.Frame.prototype.interpolate(this.trendLine(), p, min.trendLine(), max.trendLine());
		this.horizontalErrorBarWidths(max.horizontalErrorBarWidths());
		this.verticalErrorBarHeights(max.verticalErrorBarHeights());
		this.interpolateOverride(p, min, max);
	}
	,
	interpolateOverride: function (p, minFrame, maxFrame) {
	}
	,
	addPointsThatSweepThroughTheView: function (markers, p, minFrame, maxFrame) {
		var en = $.ig.Enumerable.prototype.where$1($.ig.OwnedPoint.prototype.$type, minFrame.cachedPoints().values(), function (changedPoint) { return !markers.containsKey(changedPoint.ownerItem()); }).getEnumerator();
		while (en.moveNext()) {
			var changedPoint = en.current();
			var maxPoint;
			if (!(function () { var $ret = maxFrame.cachedPoints().tryGetValue(changedPoint.ownerItem(), maxPoint); maxPoint = $ret.p1; return $ret.ret; }()) || (maxPoint.columnValues().__x == changedPoint.columnValues().__x && maxPoint.columnValues().__y == changedPoint.columnValues().__y)) {
				continue;
			}
			var newPoint = new $.ig.OwnedPoint();
			this.interpolatePoint(newPoint, p, changedPoint, maxPoint, minFrame, maxFrame);
			if ($.ig.util.isNaN(newPoint.point().__x) || $.ig.util.isNaN(newPoint.point().__y)) {
				continue;
			}
			markers.add(newPoint.ownerItem(), newPoint);
		}
	}
	,
	_getNewMinValue: null,
	getNewMinValue: function (value) {
		if (arguments.length === 1) {
			this._getNewMinValue = value;
			return value;
		} else {
			return this._getNewMinValue;
		}
	}
	,
	interpolateColumnValues: function (point, p, minPoint, maxPoint) {
		if (minPoint != null) {
			point.columnValues({ __x: minPoint.columnValues().__x, __y: minPoint.columnValues().__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		} else if (maxPoint != null) {
			point.columnValues({ __x: maxPoint.columnValues().__x, __y: maxPoint.columnValues().__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		}
	}
	,
	interpolatePoint: function (point, p, minPoint, maxPoint, minFrame, maxFrame) {
		var min;
		var max;
		if (minPoint == null) {
			if (maxPoint != null) {
				var minValue;
				if ((function () { var $ret = minFrame.cachedPoints().tryGetValue(maxPoint.ownerItem(), minValue); minValue = $ret.p1; return $ret.ret; }())) {
					min = minValue;
				} else {
					min = this.getNewMinValue()(maxPoint, minFrame, maxFrame);
				}
			} else {
				point.point({ __x: NaN, __y: NaN, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				return;
			}
		} else {
			min = minPoint;
			if (point.ownerItem() == null) {
				point.ownerItem(minPoint.ownerItem());
			}
		}
		if (maxPoint == null) {
			if (minPoint != null) {
				var maxValue;
				if ((function () { var $ret = maxFrame.cachedPoints().tryGetValue(minPoint.ownerItem(), maxValue); maxValue = $ret.p1; return $ret.ret; }())) {
					max = maxValue;
				} else {
					point.point({ __x: NaN, __y: NaN, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
					return;
				}
			} else {
				point.point({ __x: NaN, __y: NaN, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				return;
			}
		} else {
			max = maxPoint;
			if (point.ownerItem() == null) {
				point.ownerItem(maxPoint.ownerItem());
			}
		}
		this.interpolateColumnValues(point, p, min, max);
		if ($.ig.util.isNaN(min.point().__x) || $.ig.util.isNaN(min.point().__y)) {
			min = max;
		}
		this.interpolatePointOverride(point, p, min, max);
	}
	,
	interpolatePointOverride: function (point, p, min, max) {
		var q = 1 - p;
		point.point({ __x: min.point().__x * q + max.point().__x * p, __y: min.point().__y * q + max.point().__y * p, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
	}
	,
	_markers: null,
	markers: function (value) {
		if (arguments.length === 1) {
			this._markers = value;
			return value;
		} else {
			return this._markers;
		}
	}
	,
	_cachedPoints: null,
	cachedPoints: function (value) {
		if (arguments.length === 1) {
			this._cachedPoints = value;
			return value;
		} else {
			return this._cachedPoints;
		}
	}
	,
	_points: null,
	points: function (value) {
		if (arguments.length === 1) {
			this._points = value;
			return value;
		} else {
			return this._points;
		}
	}
	,
	_trendLine: null,
	trendLine: function (value) {
		if (arguments.length === 1) {
			this._trendLine = value;
			return value;
		} else {
			return this._trendLine;
		}
	}
	,
	_horizontalErrorBars: null,
	horizontalErrorBars: function (value) {
		if (arguments.length === 1) {
			this._horizontalErrorBars = value;
			return value;
		} else {
			return this._horizontalErrorBars;
		}
	}
	,
	_verticalErrorBars: null,
	verticalErrorBars: function (value) {
		if (arguments.length === 1) {
			this._verticalErrorBars = value;
			return value;
		} else {
			return this._verticalErrorBars;
		}
	}
	,
	_horizontalErrorBarWidths: null,
	horizontalErrorBarWidths: function (value) {
		if (arguments.length === 1) {
			this._horizontalErrorBarWidths = value;
			return value;
		} else {
			return this._horizontalErrorBarWidths;
		}
	}
	,
	_verticalErrorBarHeights: null,
	verticalErrorBarHeights: function (value) {
		if (arguments.length === 1) {
			this._verticalErrorBarHeights = value;
			return value;
		} else {
			return this._verticalErrorBarHeights;
		}
	}
	,
	$type: new $.ig.Type('ScatterFrameBase$1', $.ig.Frame.prototype.$type)
}, true);

$.ig.util.defType('ScatterAxisInfoCache', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	_xAxis: null,
	xAxis: function (value) {
		if (arguments.length === 1) {
			this._xAxis = value;
			return value;
		} else {
			return this._xAxis;
		}
	}
	,
	_yAxis: null,
	yAxis: function (value) {
		if (arguments.length === 1) {
			this._yAxis = value;
			return value;
		} else {
			return this._yAxis;
		}
	}
	,
	_xAxisIsInverted: false,
	xAxisIsInverted: function (value) {
		if (arguments.length === 1) {
			this._xAxisIsInverted = value;
			return value;
		} else {
			return this._xAxisIsInverted;
		}
	}
	,
	_yAxisIsInverted: false,
	yAxisIsInverted: function (value) {
		if (arguments.length === 1) {
			this._yAxisIsInverted = value;
			return value;
		} else {
			return this._yAxisIsInverted;
		}
	}
	,
	_fastItemsSource: null,
	fastItemsSource: function (value) {
		if (arguments.length === 1) {
			this._fastItemsSource = value;
			return value;
		} else {
			return this._fastItemsSource;
		}
	}
	,
	$type: new $.ig.Type('ScatterAxisInfoCache', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('DictInterpolator$3', 'Object', {
	$tKey: null,
	$tValue: null,
	$tFrame: null,
	init: function ($tKey, $tValue, $tFrame, interpolatePointStrat, getKeyStrat, validPointStrat, createValueStrat) {
		this.$tKey = $tKey;
		this.$tValue = $tValue;
		this.$tFrame = $tFrame;
		this.$type = this.$type.specialize(this.$tKey, this.$tValue, this.$tFrame);
		$.ig.Object.prototype.init.call(this);
		this.interpolatePointStrat(interpolatePointStrat);
		this.getKeyStrat(getKeyStrat);
		this.validPointStrat(validPointStrat);
		this.createValueStrat(createValueStrat);
	},
	_interpolatePointStrat: null,
	interpolatePointStrat: function (value) {
		if (arguments.length === 1) {
			this._interpolatePointStrat = value;
			return value;
		} else {
			return this._interpolatePointStrat;
		}
	}
	,
	_getKeyStrat: null,
	getKeyStrat: function (value) {
		if (arguments.length === 1) {
			this._getKeyStrat = value;
			return value;
		} else {
			return this._getKeyStrat;
		}
	}
	,
	_validPointStrat: null,
	validPointStrat: function (value) {
		if (arguments.length === 1) {
			this._validPointStrat = value;
			return value;
		} else {
			return this._validPointStrat;
		}
	}
	,
	_createValueStrat: null,
	createValueStrat: function (value) {
		if (arguments.length === 1) {
			this._createValueStrat = value;
			return value;
		} else {
			return this._createValueStrat;
		}
	}
	,
	interpolate: function (target, p, min, max, minFrame, maxFrame) {
		var removeFromTarget = new $.ig.List$1(this.$tKey, 0);
		var en = target.keys().getEnumerator();
		while (en.moveNext()) {
			var key = en.current();
			var minValue;
			var maxValue;
			var targetValue = target.item(key);
			var minContains = (function () { var $ret = min.tryGetValue(key, minValue); minValue = $ret.p1; return $ret.ret; }());
			var maxContains = (function () { var $ret = max.tryGetValue(key, maxValue); maxValue = $ret.p1; return $ret.ret; }());
			if (!minContains && !maxContains) {
				removeFromTarget.add(key);
			} else {
				this.interpolatePointStrat()(targetValue, p, minValue, maxValue, minFrame, maxFrame);
				if (!this.validPointStrat()(targetValue)) {
					removeFromTarget.add(key);
				}
			}
		}
		var en1 = removeFromTarget.getEnumerator();
		while (en1.moveNext()) {
			var key1 = en1.current();
			target.remove(key1);
		}
		var en2 = min.keys().getEnumerator();
		while (en2.moveNext()) {
			var key2 = en2.current();
			var minValue1 = min.item(key2);
			var maxValue1;
			var targetValue1;
			var $ret = max.tryGetValue(key2, maxValue1);
			maxValue1 = $ret.p1;
			var targetContains = (function () { var $ret = target.tryGetValue(key2, targetValue1); targetValue1 = $ret.p1; return $ret.ret; }());
			if (!targetContains) {
				targetValue1 = this.createValueStrat()();
				this.interpolatePointStrat()(targetValue1, p, minValue1, maxValue1, minFrame, maxFrame);
				if (!this.validPointStrat()(targetValue1)) {
					continue;
				}
				target.add(this.getKeyStrat()(targetValue1), targetValue1);
			}
		}
		var en3 = max.keys().getEnumerator();
		while (en3.moveNext()) {
			var key3 = en3.current();
			var maxValue2 = max.item(key3);
			var minValue2;
			var targetValue2;
			var minContains1 = (function () { var $ret = min.tryGetValue(key3, minValue2); minValue2 = $ret.p1; return $ret.ret; }());
			var targetContains1 = (function () { var $ret = target.tryGetValue(key3, targetValue2); targetValue2 = $ret.p1; return $ret.ret; }());
			if (!targetContains1 && !minContains1) {
				targetValue2 = this.createValueStrat()();
				this.interpolatePointStrat()(targetValue2, p, minValue2, maxValue2, minFrame, maxFrame);
				if (!this.validPointStrat()(targetValue2)) {
					continue;
				}
				target.add(this.getKeyStrat()(targetValue2), targetValue2);
			}
		}
	}
	,
	$type: new $.ig.Type('DictInterpolator$3', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('OwnedPoint', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
		this.columnValues({ __x: 0, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
	},
	_point: null,
	point: function (value) {
		if (arguments.length === 1) {
			this._point = value;
			return value;
		} else {
			return this._point;
		}
	}
	,
	_ownerItem: null,
	ownerItem: function (value) {
		if (arguments.length === 1) {
			this._ownerItem = value;
			return value;
		} else {
			return this._ownerItem;
		}
	}
	,
	_columnValues: null,
	columnValues: function (value) {
		if (arguments.length === 1) {
			this._columnValues = value;
			return value;
		} else {
			return this._columnValues;
		}
	}
	,
	$type: new $.ig.Type('OwnedPoint', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('ScatterFrame', 'ScatterFrameBase$1', {
	init: function () {
		$.ig.ScatterFrameBase$1.prototype.init.call(this, $.ig.ScatterFrame.prototype.$type);
		this.linePoints(new $.ig.Dictionary$2($.ig.Object.prototype.$type, $.ig.OwnedPoint.prototype.$type, 0));
	},
	_linePoints: null,
	linePoints: function (value) {
		if (arguments.length === 1) {
			this._linePoints = value;
			return value;
		} else {
			return this._linePoints;
		}
	}
	,
	interpolateOverride: function (p, minFrame, maxFrame) {
		$.ig.ScatterFrameBase$1.prototype.interpolateOverride.call(this, p, minFrame, maxFrame);
		var min = $.ig.util.cast($.ig.ScatterFrame.prototype.$type, minFrame);
		var max = $.ig.util.cast($.ig.ScatterFrame.prototype.$type, maxFrame);
		if (min == null || max == null) {
			return;
		}
		this.ownedPointDictInterpolator().interpolate(this.linePoints(), p, min.linePoints(), max.linePoints(), min, max);
	}
	,
	$type: new $.ig.Type('ScatterFrame', $.ig.ScatterFrameBase$1.prototype.$type.specialize(-1))
}, true);

$.ig.ScatterFrame.prototype.$type.initSelfReferences();

$.ig.util.defType('ScatterLineSeries', 'ScatterBase', {
	init: function () {
		var $self = this;
		$.ig.ScatterBase.prototype.init.call(this);
		this.defaultStyleKey($.ig.ScatterLineSeries.prototype.$type);
		this._previousFrame = new $.ig.ScatterFrame();
		this._transitionFrame = new $.ig.ScatterFrame();
		this._currentFrame = new $.ig.ScatterFrame();
		var getMinValue = function (maxPoint, minFrame, maxFrame) {
			var index = $self.fastItemsSource().indexOf(maxPoint.ownerItem());
			if (index == -1 || index == 0) {
				return maxPoint;
			}
			var prev = $self.fastItemsSource().item(index - 1);
			var prevPoint;
			if (!(function () { var $ret = minFrame.cachedPoints().tryGetValue(prev, prevPoint); prevPoint = $ret.p1; return $ret.ret; }())) {
				return maxPoint;
			}
			return prevPoint;
		};
		this._previousFrame.getNewMinValue(getMinValue);
		this._transitionFrame.getNewMinValue(getMinValue);
		this._currentFrame.getNewMinValue(getMinValue);
	},
	createView: function () {
		return new $.ig.ScatterLineSeriesView(this);
	}
	,
	testHit: function (position, isFinger) {
		if (this.testMarkersOver(position, isFinger)) {
			return true;
		}
		if (this.testScatterStrokeOver(position, isFinger)) {
			return true;
		}
		return false;
	}
	,
	testScatterStrokeOver: function (position, isFinger) {
		var scatterLineView = this.view();
		var path = scatterLineView._polyline;
		return this.testNearStroke(position, isFinger, path.data());
	}
	,
	prepareFrame: function (frame, view) {
		var $self = this;
		$.ig.ScatterBase.prototype.prepareFrame.call(this, frame, view);
		frame.points().clear();
		frame.linePoints().clear();
		var windowRect = view.windowRect();
		var viewportRect = view.viewport();
		var effectiveViewportRect = this.getEffectiveViewport1(view);
		var measure = this.resolution() * this.resolution();
		var count = 0;
		if (this.xColumn() != null) {
			count = this.xColumn().count();
		}
		if (this.yColumn() != null) {
			count = Math.min(this.yColumn().count(), count);
		}
		var px = new $.ig.ScalerParams(0, windowRect, viewportRect, this.axisInfoCache().xAxisIsInverted(), effectiveViewportRect);
		var py = new $.ig.ScalerParams(0, windowRect, viewportRect, this.axisInfoCache().yAxisIsInverted(), effectiveViewportRect);
		var X = function (i) { return $self.axisInfoCache().xAxis().getScaledValue($self.xColumn().item(i), px); };
		var Y = function (i) { return $self.axisInfoCache().yAxis().getScaledValue($self.yColumn().item(i), py); };
		var top = viewportRect.top() - 10;
		var bottom = viewportRect.bottom() + 10;
		var left = viewportRect.left() - 10;
		var right = viewportRect.right() + 10;
		var clipper = (function () {
			var $ret = new $.ig.Clipper(1, left, bottom, right, top, false);
			$ret.target(frame.points());
			return $ret;
		}());
		for (var i = 0; i < count; ) {
			var j = i;
			++i;
			if (count > this.maximumMarkers()) {
				while (i < count && this.measure(X, Y, j, i) < measure) {
					++i;
				}
				clipper.add(this.centroid(X, Y, j, i - 1));
			} else {
				var newPoint = new $.ig.OwnedPoint();
				newPoint.point({ __x: X(j), __y: Y(j), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				newPoint.ownerItem(this.fastItemsSource().item(j));
				if (!frame.linePoints().containsKey(newPoint.ownerItem())) {
					frame.linePoints().add(newPoint.ownerItem(), newPoint);
				}
			}
		}
		if (count > this.maximumMarkers()) {
			clipper.target(null);
		}
	}
	,
	centroid: function (X, Y, a, b) {
		if (a == b) {
			return { __x: X(a), __y: Y(a), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		}
		var cx = 0;
		var cy = 0;
		var weight = (b - a + 1);
		for (var i = a; i <= b; ++i) {
			cx += X(i);
			cy += Y(i);
		}
		return { __x: cx / weight, __y: cy / weight, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
	}
	,
	measure: function (X, Y, a, b) {
		var x = X(b) - X(a);
		var y = Y(b) - Y(a);
		return x * x + y * y;
	}
	,
	renderFrame: function (frame, view) {
		view.clearRendering(false);
		$.ig.ScatterBase.prototype.renderFrame.call(this, frame, view);
		var clipRect = new $.ig.Rect(0, view.viewport().left(), view.viewport().top(), view.viewport().width(), view.viewport().height());
		clipRect = $.ig.RectUtil.prototype.inflateBy(clipRect, this.thickness(), this.thickness());
		this.prepLinePoints1(frame, new $.ig.Clipper(0, clipRect, false));
		var scatterLineView = $.ig.util.cast($.ig.ScatterLineSeriesView.prototype.$type, view);
		this.polylineSegments1(scatterLineView._polyline, frame.points().count(), function (j) { return frame.points().__inner[j].__x; }, function (j) { return frame.points().__inner[j].__y; }, this.unknownValuePlotting(), this.resolution());
	}
	,
	polylineSegments1: function (polylines, count, x0, y0, unknownValuePlotting, resolution) {
		var polylineData = new $.ig.PathGeometry();
		polylines.data(polylineData);
		polylineData.figures(new $.ig.PathFigureCollection());
		var polylineSegments = new $.ig.List$1($.ig.PolyLineSegment.prototype.$type, 0);
		var currentLineStartIndex = 0;
		for (var i = 0; i < count; i++) {
			if ($.ig.util.isNaN(x0(i)) || $.ig.util.isNaN(y0(i))) {
				var pointsInCurrentLine = i - currentLineStartIndex;
				if (pointsInCurrentLine > 0) {
					if (unknownValuePlotting == $.ig.UnknownValuePlotting.prototype.dontPlot || polylineSegments.count() == 0) {
						var currentPolylineSegment = new $.ig.PolyLineSegment();
						polylineSegments.add(currentPolylineSegment);
					}
					this.polylineSegments(polylineSegments.__inner[polylineSegments.count() - 1].__points, currentLineStartIndex, i - 1, x0, y0, resolution);
				}
				currentLineStartIndex = i + 1;
			}
		}
		if (unknownValuePlotting == $.ig.UnknownValuePlotting.prototype.dontPlot || polylineSegments.count() == 0) {
			var lastPolylineSegment = new $.ig.PolyLineSegment();
			polylineSegments.add(lastPolylineSegment);
		}
		this.polylineSegments(polylineSegments.__inner[polylineSegments.count() - 1].__points, currentLineStartIndex, count - 1, x0, y0, resolution);
		for (var current = 0; current < polylineSegments.count(); current++) {
			var polylineSegment = polylineSegments.__inner[current];
			if (polylineSegment.__points.count() > 0) {
				var polylineFigure = (function () {
					var $ret = new $.ig.PathFigure();
					$ret.startPoint(polylineSegment.__points.__inner[0]);
					return $ret;
				}());
				polylineFigure.__segments.add(polylineSegment);
				polylineData.figures().add(polylineFigure);
			}
		}
	}
	,
	flattenHelper: function (result, X, Y, b, e, E) {
		var indices = new $.ig.List$1($.ig.Number.prototype.$type, 0);
		var start = b;
		var end = e;
		var toFlatten = end - start + 1;
		while (toFlatten > 0) {
			if (toFlatten <= $.ig.ScatterLineSeries.prototype._fLATTENER_CHUNKING) {
				$.ig.Flattener.prototype.flatten2(indices, X, Y, start, end, E);
				start = end + 1;
			} else {
				var currentEnd = start + $.ig.ScatterLineSeries.prototype._fLATTENER_CHUNKING - 1;
				$.ig.Flattener.prototype.flatten2(indices, X, Y, start, currentEnd, E);
				start = currentEnd + 1;
			}
			toFlatten = end - start + 1;
		}
		return indices;
	}
	,
	polylineSegments: function (polylinePoints, startIndex, endIndex, x0, y0, resolution) {
		if (endIndex > -1) {
			var res = this.resolution();
			var indices = this.flattenHelper(new $.ig.List$1($.ig.Number.prototype.$type, 0), x0, y0, startIndex, endIndex, res);
			var index;
			for (var i = 0; i < indices.count(); i++) {
				index = indices.item(i);
				polylinePoints.add({ __x: x0(index), __y: y0(index), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			}
		}
	}
	,
	unknownValuePlotting: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.ScatterLineSeries.prototype.unknownValuePlottingProperty, $.ig.UnknownValuePlotting.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.ScatterLineSeries.prototype.unknownValuePlottingProperty));
		}
	}
	,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		$.ig.ScatterBase.prototype.propertyUpdatedOverride.call(this, sender, propertyName, oldValue, newValue);
		switch (propertyName) {
			case $.ig.ScatterLineSeries.prototype._unknownValuePlottingPropertyName:
				this.renderSeries(false);
				this.notifyThumbnailAppearanceChanged();
				break;
		}
	}
	,
	$type: new $.ig.Type('ScatterLineSeries', $.ig.ScatterBase.prototype.$type)
}, true);

$.ig.util.defType('ScatterSeries', 'ScatterBase', {
	createView: function () {
		return new $.ig.ScatterSeriesView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.ScatterBase.prototype.onViewCreated.call(this, view);
		this.scatterSeriesView(view);
	}
	,
	_scatterSeriesView: null,
	scatterSeriesView: function (value) {
		if (arguments.length === 1) {
			this._scatterSeriesView = value;
			return value;
		} else {
			return this._scatterSeriesView;
		}
	}
	,
	init: function () {
		$.ig.ScatterBase.prototype.init.call(this);
		this.defaultStyleKey($.ig.ScatterSeries.prototype.$type);
		this._previousFrame = new $.ig.ScatterFrame();
		this._transitionFrame = new $.ig.ScatterFrame();
		this._currentFrame = new $.ig.ScatterFrame();
	},
	$type: new $.ig.Type('ScatterSeries', $.ig.ScatterBase.prototype.$type)
}, true);

$.ig.util.defType('ScatterSplineSeries', 'ScatterBase', {
	init: function () {
		$.ig.ScatterBase.prototype.init.call(this);
		this.defaultStyleKey($.ig.ScatterSplineSeries.prototype.$type);
		this._previousFrame = new $.ig.ScatterFrame();
		this._transitionFrame = new $.ig.ScatterFrame();
		this._currentFrame = new $.ig.ScatterFrame();
	},
	createView: function () {
		return new $.ig.ScatterSplineSeriesView(this);
	}
	,
	stiffness: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.ScatterSplineSeries.prototype.stiffnessProperty, value);
			return value;
		} else {
			return this.getValue($.ig.ScatterSplineSeries.prototype.stiffnessProperty);
		}
	}
	,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		$.ig.ScatterBase.prototype.propertyUpdatedOverride.call(this, sender, propertyName, oldValue, newValue);
		switch (propertyName) {
			case $.ig.ScatterSplineSeries.prototype.stiffnessPropertyName:
				this.renderSeries(false);
				this.notifyThumbnailAppearanceChanged();
				break;
		}
	}
	,
	getResolvedHitTestMode: function () {
		if (this.hitTestMode() == $.ig.SeriesHitTestMode.prototype.auto) {
			return $.ig.SeriesHitTestMode.prototype.colorEncoded;
		} else {
			return $.ig.ScatterBase.prototype.getResolvedHitTestMode.call(this);
		}
	}
	,
	prepareFrame: function (frame, view) {
		var $self = this;
		$.ig.ScatterBase.prototype.prepareFrame.call(this, frame, view);
		frame.points().clear();
		frame.linePoints().clear();
		var windowRect = view.windowRect();
		var viewportRect = view.viewport();
		var effectiveViewportRect = this.getEffectiveViewport1(view);
		var measure = this.resolution() * this.resolution();
		var count = Math.min(this.xColumn() != null ? this.xColumn().count() : 0, this.yColumn() != null ? this.yColumn().count() : 0);
		var px = new $.ig.ScalerParams(0, windowRect, viewportRect, this.axisInfoCache().xAxisIsInverted(), effectiveViewportRect);
		var py = new $.ig.ScalerParams(0, windowRect, viewportRect, this.axisInfoCache().yAxisIsInverted(), effectiveViewportRect);
		var X = function (i) { return $self.axisInfoCache().xAxis().getScaledValue($self.xColumn().item(i), px); };
		var Y = function (i) { return $self.axisInfoCache().yAxis().getScaledValue($self.yColumn().item(i), py); };
		var clipper = (function () {
			var $ret = new $.ig.Clipper(0, viewportRect, false);
			$ret.target(frame.points());
			return $ret;
		}());
		for (var i = 0; i < count; ) {
			var j = i;
			++i;
			while (i < count && this.measure(X, Y, j, i) < measure) {
				++i;
			}
			if (count > this.maximumMarkers()) {
				clipper.add(this.centroid(X, Y, j, i - 1));
			} else {
				var newPoint = new $.ig.OwnedPoint();
				newPoint.point({ __x: X(j), __y: Y(j), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				newPoint.ownerItem(this.fastItemsSource().item(j));
				if (!frame.linePoints().containsKey(newPoint.ownerItem())) {
					frame.linePoints().add(newPoint.ownerItem(), newPoint);
				}
			}
		}
		if (count > this.maximumMarkers()) {
			clipper.target(null);
		}
	}
	,
	centroid: function (X, Y, a, b) {
		if (a == b) {
			return { __x: X(a), __y: Y(a), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		}
		var cx = 0;
		var cy = 0;
		var weight = (b - a + 1);
		for (var i = a; i <= b; ++i) {
			cx += X(i);
			cy += Y(i);
		}
		return { __x: cx / weight, __y: cy / weight, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
	}
	,
	measure: function (X, Y, a, b) {
		var x = X(b) - X(a);
		var y = Y(b) - Y(a);
		return x * x + y * y;
	}
	,
	clearRendering: function (wipeClean, view) {
		$.ig.ScatterBase.prototype.clearRendering.call(this, wipeClean, view);
		var scatterSplineView = $.ig.util.cast($.ig.ScatterSplineSeriesView.prototype.$type, view);
		scatterSplineView.clearRendering(false);
	}
	,
	renderFrame: function (frame, view) {
		this.clearRendering(false, view);
		$.ig.ScatterBase.prototype.renderFrame.call(this, frame, view);
		this.prepLinePoints(frame);
		var scatterSplineView = $.ig.util.cast($.ig.ScatterSplineSeriesView.prototype.$type, view);
		scatterSplineView._polyline.data(new $.ig.PathGeometry());
		(scatterSplineView._polyline.data()).figures($.ig.Numeric.prototype.spline2D1(frame.points().count(), function (i) { return frame.points().__inner[i].__x; }, function (i) { return frame.points().__inner[i].__y; }, this.stiffness()));
	}
	,
	$type: new $.ig.Type('ScatterSplineSeries', $.ig.ScatterBase.prototype.$type)
}, true);

$.ig.util.defType('Legend', 'LegendBase', {
	createView: function () {
		return new $.ig.LegendView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.LegendBase.prototype.onViewCreated.call(this, view);
		this.legendView(view);
	}
	,
	_legendView: null,
	legendView: function (value) {
		if (arguments.length === 1) {
			this._legendView = value;
			return value;
		} else {
			return this._legendView;
		}
	}
	,
	init: function () {
		var $self = this;
		$.ig.LegendBase.prototype.init.call(this);
		this.defaultStyleKey($.ig.Legend.prototype.$type);
		var $t = this.children();
		$t.collectionChanged = $.ig.Delegate.prototype.combine($t.collectionChanged, function (o, e) {
			if (e.oldItems() != null) {
				var en = e.oldItems().getEnumerator();
				while (en.moveNext()) {
					var item = en.current();
					$self.legendView().removeItemVisual(item);
				}
			}
			if (e.newItems() != null) {
				var en1 = e.newItems().getEnumerator();
				while (en1.moveNext()) {
					var item1 = en1.current();
					$self.legendView().addItemVisual(item1);
				}
			}
		});
	},
	getSeriesIndex: function (series) {
		if ($.ig.util.cast($.ig.FragmentBase.prototype.$type, series) !== null || $.ig.util.cast($.ig.SplineFragmentBase.prototype.$type, series) !== null) {
			var parentSeries = $.ig.util.cast($.ig.FragmentBase.prototype.$type, series) !== null ? (series).parentSeries() : (series).parentSeries();
			if (parentSeries.reverseLegendOrder()) {
				return -1 + parentSeries.index() + parentSeries.actualSeries().count() - parentSeries.stackedSeriesManager().seriesVisual().indexOf($.ig.util.cast($.ig.AnchoredCategorySeries.prototype.$type, series));
			}
		}
		return series.index();
	}
	,
	addChildInOrder: function (legendItem, series) {
		if ($.ig.util.cast($.ig.StackedSeriesBase.prototype.$type, series) !== null) {
			return;
		}
		if (!series.isUsableInLegend()) {
			return;
		}
		var index = 0;
		var en = this.children().getEnumerator();
		while (en.moveNext()) {
			var item = en.current();
			var itemChart;
			var itemSeries;
			var itemItem;
			var $ret = this.view().fetchLegendEnvironment(item, itemChart, itemSeries, itemItem);
			itemChart = $ret.p1;
			itemSeries = $ret.p2;
			itemItem = $ret.p3;
			if (series.seriesViewer() != null && itemChart != null && (this.getSortOrder(series.seriesViewer()) < this.getSortOrder(itemChart) || (this.getSortOrder(series.seriesViewer()) == -1 && this.getSortOrder(itemChart) == -1 && series.seriesViewer().getHashCode() < itemChart.getHashCode()))) {
				break;
			}
			if (series.seriesViewer() != null && itemChart != null && series.seriesViewer() == itemChart && itemSeries != null) {
				var indexOfSeries = this.getSeriesIndex(series);
				var indexOfItemSeries = this.getSeriesIndex(itemSeries);
				var sortOrderSeries = this.getSortOrder(series);
				var sortOrderItemSeries = this.getSortOrder(itemSeries);
				if ($.ig.util.cast($.ig.BarSeries.prototype.$type, itemSeries) !== null) {
					if (sortOrderItemSeries == -1 && sortOrderSeries == -1) {
						index = 0;
						break;
					}
					if (sortOrderSeries < sortOrderItemSeries || sortOrderItemSeries == -1) {
						break;
					}
				}
				if (indexOfSeries <= indexOfItemSeries) {
					break;
				}
			}
			index++;
		}
		this.children().insert(index, legendItem);
		var info = new $.ig.LegendItemInfo();
		info.legendItem(legendItem);
		info.series(series);
		var contentControl = $.ig.util.cast($.ig.ContentControl.prototype.$type, legendItem);
		if (contentControl != null && contentControl.content() != null) {
			var context = $.ig.util.cast($.ig.DataContext.prototype.$type, contentControl.content());
			if (context != null) {
				info.dataContext(context);
				info.text(context.itemLabel());
			}
		}
	}
	,
	getSortOrder: function (target) {
		return -1;
	}
	,
	$type: new $.ig.Type('Legend', $.ig.LegendBase.prototype.$type)
}, true);

$.ig.util.defType('ScatterBaseView', 'MarkerSeriesView', {
	_markerManager: null,
	markerManager: function (value) {
		if (arguments.length === 1) {
			this._markerManager = value;
			return value;
		} else {
			return this._markerManager;
		}
	}
	,
	_locations: null,
	locations: function (value) {
		if (arguments.length === 1) {
			this._locations = value;
			return value;
		} else {
			return this._locations;
		}
	}
	,
	_scatterModel: null,
	scatterModel: function (value) {
		if (arguments.length === 1) {
			this._scatterModel = value;
			return value;
		} else {
			return this._scatterModel;
		}
	}
	,
	_indexes: null,
	indexes: function (value) {
		if (arguments.length === 1) {
			this._indexes = value;
			return value;
		} else {
			return this._indexes;
		}
	}
	,
	init: function (model) {
		this.__markerMeasureInfo = null;
		$.ig.MarkerSeriesView.prototype.init.call(this, model);
		this.scatterModel(model);
		this.markers(new $.ig.HashPool$2($.ig.Object.prototype.$type, $.ig.Marker.prototype.$type));
		this.initMarkers(this.markers());
		this.trendLineManager(new $.ig.ScatterTrendLineManager());
	},
	_horizontalErrorBarsPath: null,
	horizontalErrorBarsPath: function (value) {
		if (arguments.length === 1) {
			this._horizontalErrorBarsPath = value;
			return value;
		} else {
			return this._horizontalErrorBarsPath;
		}
	}
	,
	_verticalErrorBarsPath: null,
	verticalErrorBarsPath: function (value) {
		if (arguments.length === 1) {
			this._verticalErrorBarsPath = value;
			return value;
		} else {
			return this._verticalErrorBarsPath;
		}
	}
	,
	_trendLineManager: null,
	trendLineManager: function (value) {
		if (arguments.length === 1) {
			this._trendLineManager = value;
			return value;
		} else {
			return this._trendLineManager;
		}
	}
	,
	onInit: function () {
		$.ig.MarkerSeriesView.prototype.onInit.call(this);
		this.markerManager(this.createMarkerManager());
		this.horizontalErrorBarsPath(new $.ig.Path());
		this.verticalErrorBarsPath(new $.ig.Path());
		if (!this.isThumbnailView()) {
			this.scatterModel().maximumMarkers(2000);
		}
	}
	,
	__markerMeasureInfo: null,
	getMarkerDesiredSize: function (marker) {
		if (this.__markerMeasureInfo == null) {
			this.__markerMeasureInfo = new $.ig.DataTemplateMeasureInfo();
			this.__markerMeasureInfo.context = this.context().getUnderlyingContext();
		}
		this.__markerMeasureInfo.width = marker.width();
		this.__markerMeasureInfo.height = marker.height();
		this.__markerMeasureInfo.data = marker.content();
		var template = marker.contentTemplate();
		if (template.measure() != null) {
			template.measure()(this.__markerMeasureInfo);
		}
		return new $.ig.Size(1, this.__markerMeasureInfo.width, this.__markerMeasureInfo.height);
	}
	,
	createMarkerManager: function () {
		var $self = this;
		var markerManager = new $.ig.NumericMarkerManager(1, function (o) { return $self.markers().item(o); }, function (i) { return $self.scatterModel().axisInfoCache().fastItemsSource().item(i); }, this.removeUnusedMarkers.runOn(this), this.getMarkerLocations.runOn(this), this.getActiveIndexes.runOn(this), function () { return $self.scatterModel().markerCollisionAvoidance(); });
		markerManager.getMarkerDesiredSize(this.getMarkerDesiredSize.runOn(this));
		return markerManager;
	}
	,
	removeUnusedMarkers: function (list) {
		this.scatterModel().removeUnusedMarkers(list, this.markers());
	}
	,
	getVisibleMarkersInternal: function () {
		return this.visibleMarkers();
	}
	,
	getMarkerLocationsInternal: function () {
		return this.locations();
	}
	,
	getMarkerLocations: function () {
		this.locations(this.scatterModel().getMarkerLocations(this, this.markers(), this.locations(), this.windowRect(), this.viewport()));
		return this.locations();
	}
	,
	getActiveIndexes: function () {
		this.indexes(this.scatterModel().getActiveIndexes(this.markers(), this.indexes()));
		return this.indexes();
	}
	,
	attachHorizontalErrorBars: function () {
	}
	,
	provideHorizontalErrorBarGeometry: function (horizontalErrorBarsGeometry) {
	}
	,
	attachVerticalErrorBars: function () {
	}
	,
	provideVerticalErrorBarGeometry: function (verticalErrorBarsGeometry) {
	}
	,
	updateTrendlineBrush: function () {
		if (this.scatterModel().trendLineBrush() != null) {
			this.scatterModel().actualTrendLineBrush(this.scatterModel().trendLineBrush());
		} else {
			this.scatterModel().actualTrendLineBrush(this.scatterModel().actualBrush());
		}
	}
	,
	_markers: null,
	markers: function (value) {
		if (arguments.length === 1) {
			this._markers = value;
			return value;
		} else {
			return this._markers;
		}
	}
	,
	doToAllMarkers: function (action) {
		this.markers().doToAll(action);
	}
	,
	hideErrorBars: function () {
	}
	,
	renderMarkersOverride: function (context, isHitContext) {
		$.ig.MarkerSeriesView.prototype.renderMarkersOverride.call(this, context, isHitContext);
		if (context.shouldRender()) {
			if (this.scatterModel().trendLineType() != $.ig.TrendLineType.prototype.none && !isHitContext) {
				var polyline = this.trendLineManager().trendPolyline();
				polyline.strokeThickness(this.scatterModel().trendLineThickness());
				polyline.__stroke = this.scatterModel().actualTrendLineBrush();
				polyline.strokeDashArray(this.scatterModel().trendLineDashArray());
				polyline.strokeDashCap(this.scatterModel().trendLineDashCap());
				context.renderPolyline(polyline);
			}
		}
	}
	,
	clearRendering: function (wipeClean) {
		if (wipeClean) {
			this.hideErrorBars();
			this.markers().clear();
		}
		this.trendLineManager().clearPoints();
		this.makeDirty();
	}
	,
	getDefaultTooltipTemplate: function () {
		var tooltipTemplate = "<div class='ui-chart-default-tooltip-content'><span";
		var nonTransparentOutline = this.model().actualOutline() != null && $.ig.Color.prototype.l_op_Inequality_Lifted($.ig.util.toNullable($.ig.Color.prototype.$type, this.model().actualOutline().color()), $.ig.util.toNullable($.ig.Color.prototype.$type, null)) && this.model().actualOutline().color().a() > 0;
		if (nonTransparentOutline) {
			tooltipTemplate += " style='color:" + this.model().actualOutline().__fill + "'";
		}
		tooltipTemplate += ">" + this.scatterModel().title() + "</span><br/><span>" + "x: </span><span class='ui-priority-primary'>${item." + this.scatterModel().xMemberPath() + "}</span><br/><span>" + "y: </span><span class='ui-priority-primary'>${item." + this.scatterModel().yMemberPath() + "}</span></div>";
		return tooltipTemplate;
	}
	,
	$type: new $.ig.Type('ScatterBaseView', $.ig.MarkerSeriesView.prototype.$type)
}, true);

$.ig.util.defType('BubbleSeriesView', 'ScatterBaseView', {
	_bubbleModel: null,
	bubbleModel: function (value) {
		if (arguments.length === 1) {
			this._bubbleModel = value;
			return value;
		} else {
			return this._bubbleModel;
		}
	}
	,
	init: function (model) {
		$.ig.ScatterBaseView.prototype.init.call(this, model);
		this.bubbleModel(model);
		this.trendLineManager(new $.ig.ScatterTrendLineManager());
	},
	onInit: function () {
		$.ig.ScatterBaseView.prototype.onInit.call(this);
		if (!this.isThumbnailView()) {
			this.markerModel().markerType($.ig.MarkerType.prototype.automatic);
			this.model().legendItemBadgeTemplate((function () {
				var $ret = new $.ig.DataTemplate();
				$ret.render($.ig.LegendTemplates.prototype.pointBadgeTemplate);
				$ret.measure($.ig.LegendTemplates.prototype.legendItemBadgeMeasure);
				return $ret;
			}()));
		}
	}
	,
	createMarkerManager: function () {
		var $self = this;
		return new $.ig.BubbleMarkerManager(function (o) { return $self.markers().item(o); }, function (i) { return $self.scatterModel().axisInfoCache().fastItemsSource().item(i); }, this.removeUnusedMarkers.runOn(this), this.getMarkerLocations.runOn(this), this.getActiveIndexes.runOn(this));
	}
	,
	createMarkerSizes: function () {
		var bubbleManager = this.markerManager();
		this.bubbleModel().sizeBubbles(bubbleManager.actualMarkers(), bubbleManager.actualRadiusColumn(), this.viewport(), this == this.model().thumbnailView());
		this.makeDirty();
	}
	,
	setMarkerColors: function () {
		var bubbleManager = this.markerManager();
		this.bubbleModel().setMarkerColors(bubbleManager.actualMarkers());
	}
	,
	clearMarkerBrushes: function () {
		var bubbleManager = this.markerManager();
		var en = bubbleManager.actualMarkers().getEnumerator();
		while (en.moveNext()) {
			var marker = en.current();
			var markerContext = $.ig.util.cast($.ig.DataContext.prototype.$type, marker.content());
			if (markerContext != null) {
				markerContext.itemBrush(null);
			}
		}
	}
	,
	getDefaultTooltipTemplate: function () {
		var tooltipTemplate = "<div class='ui-chart-default-tooltip-content'><span";
		var nonTransparentOutline = this.model().actualOutline() != null && $.ig.Color.prototype.l_op_Inequality_Lifted($.ig.util.toNullable($.ig.Color.prototype.$type, this.model().actualOutline().color()), $.ig.util.toNullable($.ig.Color.prototype.$type, null)) && this.model().actualOutline().color().a() > 0;
		if (nonTransparentOutline) {
			tooltipTemplate += " style='color:" + this.model().actualOutline().__fill + "'";
		}
		tooltipTemplate += ">" + this.bubbleModel().title() + "</span><br/><span>" + "(${item." + this.bubbleModel().xMemberPath() + "}, ${item." + this.bubbleModel().yMemberPath() + "})</span>";
		if (!String.isNullOrEmpty(this.bubbleModel().radiusMemberPath())) {
			tooltipTemplate += "<span>, Radius: ${item." + this.bubbleModel().radiusMemberPath() + "}</span>";
		}
		tooltipTemplate += "</div>";
		return tooltipTemplate;
	}
	,
	$type: new $.ig.Type('BubbleSeriesView', $.ig.ScatterBaseView.prototype.$type)
}, true);

$.ig.util.defType('LegendView', 'LegendBaseView', {
	init: function (model) {
		$.ig.LegendBaseView.prototype.init.call(this, model);
		this.legendModel(model);
	},
	_legendModel: null,
	legendModel: function (value) {
		if (arguments.length === 1) {
			this._legendModel = value;
			return value;
		} else {
			return this._legendModel;
		}
	}
	,
	onInit: function () {
		$.ig.LegendBaseView.prototype.onInit.call(this);
	}
	,
	$type: new $.ig.Type('LegendView', $.ig.LegendBaseView.prototype.$type)
}, true);

$.ig.util.defType('ScaleLegendView', 'LegendBaseView', {
	init: function (model) {
		this._scaleSize = new $.ig.Size();
		this._isDirty = false;
		$.ig.LegendBaseView.prototype.init.call(this, model);
		this.scaleModel(model);
		this.minText(new $.ig.TextBlock());
		this.maxText(new $.ig.TextBlock());
		this.legendScaleElement(new $.ig.Polygon());
	},
	_scaleModel: null,
	scaleModel: function (value) {
		if (arguments.length === 1) {
			this._scaleModel = value;
			return value;
		} else {
			return this._scaleModel;
		}
	}
	,
	_legendScaleElement: null,
	legendScaleElement: function (value) {
		if (arguments.length === 1) {
			this._legendScaleElement = value;
			return value;
		} else {
			return this._legendScaleElement;
		}
	}
	,
	_minText: null,
	minText: function (value) {
		if (arguments.length === 1) {
			this._minText = value;
			return value;
		} else {
			return this._minText;
		}
	}
	,
	_maxText: null,
	maxText: function (value) {
		if (arguments.length === 1) {
			this._maxText = value;
			return value;
		} else {
			return this._maxText;
		}
	}
	,
	restoreOriginalState: function () {
	}
	,
	detachContent: function () {
	}
	,
	getTransparentBrush: function () {
		return $.ig.Color.prototype.fromArgb(0, 0, 0, 0);
	}
	,
	buildGradient: function () {
		return new $.ig.GradientData();
	}
	,
	addGradientStop: function (gradient, color, colorOffset) {
		var data = gradient;
		data.gradientStops().add((function () {
			var $ret = new $.ig.GradientStopData();
			$ret.brush((function () {
				var $ret = new $.ig.Brush();
				$ret.color(color);
				return $ret;
			}()));
			$ret.offset(colorOffset);
			return $ret;
		}()));
	}
	,
	setScaleFill: function (legendScaleShapeElement, useSeriesBrush, gradient) {
		if (useSeriesBrush) {
			this.currBrush(this.scaleModel().series().actualMarkerBrush());
			this.currGradient(null);
		} else {
			this.currBrush(null);
			this.currGradient(gradient);
		}
		this.makeDirty();
	}
	,
	_currGradient: null,
	currGradient: function (value) {
		if (arguments.length === 1) {
			this._currGradient = value;
			return value;
		} else {
			return this._currGradient;
		}
	}
	,
	_scaleContext: null,
	scaleContext: function (value) {
		if (arguments.length === 1) {
			this._scaleContext = value;
			return value;
		} else {
			return this._scaleContext;
		}
	}
	,
	_scaleSize: null,
	scaleSize: function (value) {
		if (arguments.length === 1) {
			this._scaleSize = value;
			return value;
		} else {
			return this._scaleSize;
		}
	}
	,
	onContainerProvided: function (container) {
		$.ig.LegendBaseView.prototype.onContainerProvided.call(this, container);
		this.scaleContext(this.viewManager().getScaleContext(container));
		this.scaleSize(this.viewManager().getScaleContainerSize());
		this.makeDirty();
	}
	,
	_isDirty: false,
	makeDirty: function () {
		if (!this._isDirty) {
			this._isDirty = true;
			window.setTimeout(this.undirty.runOn(this), 0);
		}
	}
	,
	undirty: function () {
		if (this._isDirty) {
			this._isDirty = false;
			this.render();
		}
	}
	,
	render: function () {
		if (this.scaleContext() == null) {
			return;
		}
		this.refreshScaleShape();
	}
	,
	getDesiredWidth: function (element) {
		var tb = $.ig.util.cast($.ig.TextBlock.prototype.$type, element);
		if (tb != null && tb.text() != null) {
			return this.scaleContext().measureTextWidth(tb.text()) + $.ig.ScaleLegendView.prototype.tEXT_MARGIN;
		}
		return 0;
	}
	,
	getDesiredHeight: function (element) {
		return this.fontHeight() + $.ig.ScaleLegendView.prototype.tEXT_MARGIN;
	}
	,
	_fontHeight: 0,
	fontHeight: function (value) {
		if (arguments.length === 1) {
			this._fontHeight = value;
			return value;
		} else {
			return this._fontHeight;
		}
	}
	,
	_fontBrush: null,
	fontBrush: function (value) {
		if (arguments.length === 1) {
			this._fontBrush = value;
			return value;
		} else {
			return this._fontBrush;
		}
	}
	,
	refreshScaleShape: function () {
		if (this.scaleModel().series() == null || this.scaleModel().series().seriesViewer() == null || (this.currGradient() == null && this.currBrush() == null)) {
			return;
		}
		if (this.scaleContext().shouldRender()) {
			this.scaleContext().setFontInfo(this.scaleModel().series().seriesViewer().getFontInfo());
			this.fontHeight(this.scaleModel().series().seriesViewer().view().fontHeight());
			this.fontBrush(this.scaleModel().series().seriesViewer().getFontBrush());
			var minWidth = this.getDesiredWidth(this.minText());
			var maxWidth = this.getDesiredWidth(this.maxText());
			var textWidth = Math.max(minWidth, maxWidth) + 4;
			if (textWidth >= this.scaleSize().width()) {
				textWidth = 0;
			}
			var scaleWidth = this.scaleSize().width() - textWidth;
			var left = 2;
			var top = 2;
			scaleWidth = scaleWidth - 4;
			var scaleHeight = this.scaleSize().height() - 4;
			var textLeft = left + scaleWidth + 4;
			var textTop = top;
			var textHeight = scaleHeight;
			var point1 = { __x: left + (3 / 5) * scaleWidth, __y: top, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			var point2 = { __x: left + (5 / 5) * scaleWidth, __y: top, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			var point3 = { __x: left + (5 / 5) * scaleWidth, __y: scaleHeight, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			var point4 = { __x: left, __y: scaleHeight, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			var p = this.legendScaleElement();
			p.points().clear();
			p.points().add(point1);
			p.points().add(point2);
			p.points().add(point3);
			p.points().add(point4);
			this.minText().canvasLeft(textLeft);
			this.minText().canvasTop(textTop);
			this.minText().fill(this.fontBrush());
			this.maxText().canvasLeft(textLeft);
			this.maxText().canvasTop(textTop + textHeight - (this.getDesiredHeight(this.minText())));
			this.maxText().fill(this.fontBrush());
			this.scaleContext().clearRectangle(0, 0, this.scaleSize().width(), this.scaleSize().height());
			if (this.currGradient() == null && this.currBrush() != null) {
				p.__fill = this.currBrush();
				this.scaleContext().renderPolygon(p);
			} else {
				this.viewManager().renderGradientShape(this.scaleContext(), p, this.currGradient(), new $.ig.Rect(0, top, left, scaleWidth, scaleHeight));
			}
			if (textWidth > 0) {
				this.scaleContext().renderTextBlock(this.minText());
				this.scaleContext().renderTextBlock(this.maxText());
			}
		}
	}
	,
	_currBrush: null,
	currBrush: function (value) {
		if (arguments.length === 1) {
			this._currBrush = value;
			return value;
		} else {
			return this._currBrush;
		}
	}
	,
	onSizeChanged: function () {
		$.ig.LegendBaseView.prototype.onSizeChanged.call(this);
	}
	,
	$type: new $.ig.Type('ScaleLegendView', $.ig.LegendBaseView.prototype.$type)
}, true);

$.ig.util.defType('ScatterLineSeriesView', 'ScatterBaseView', {
	init: function (model) {
		this._polyline = new $.ig.Path();
		$.ig.ScatterBaseView.prototype.init.call(this, model);
	},
	_polyline: null,
	setupAppearanceOverride: function () {
		$.ig.ScatterBaseView.prototype.setupAppearanceOverride.call(this);
		this._polyline.__stroke = this.model().actualBrush();
		this._polyline.strokeThickness(this.model().thickness());
	}
	,
	setupHitAppearanceOverride: function () {
		$.ig.ScatterBaseView.prototype.setupHitAppearanceOverride.call(this);
		var hitBrush = this.getHitBrush();
		this._polyline.__stroke = hitBrush;
		this._polyline.strokeThickness(this.model().thickness() + $.ig.SeriesView.prototype.hIT_THICKNESS_AUGMENT);
	}
	,
	renderOverride: function (context, isHitContext) {
		$.ig.ScatterBaseView.prototype.renderOverride.call(this, context, isHitContext);
		if (context.shouldRender()) {
			context.renderPath(this._polyline);
		}
	}
	,
	clearRendering: function (wipeClean) {
		$.ig.ScatterBaseView.prototype.clearRendering.call(this, wipeClean);
		this._polyline.data(null);
	}
	,
	exportViewShapes: function (svd) {
		$.ig.ScatterBaseView.prototype.exportViewShapes.call(this, svd);
		var shape = new $.ig.PathVisualData(1, "shape", this._polyline);
		shape.tags().add("Main");
		svd.shapes().add(shape);
	}
	,
	applyDropShadowDefaultSettings: function () {
		var color = new $.ig.Color();
		color.colorString("rgba(95,95,95,0.5)");
		this.model().shadowColor(color);
		this.model().shadowBlur(3);
		this.model().shadowOffsetX(1);
		this.model().shadowOffsetY(4);
		this.model().useSingleShadow(false);
	}
	,
	$type: new $.ig.Type('ScatterLineSeriesView', $.ig.ScatterBaseView.prototype.$type)
}, true);

$.ig.util.defType('ScatterSeriesView', 'ScatterBaseView', {
	_scatterSeriesModel: null,
	scatterSeriesModel: function (value) {
		if (arguments.length === 1) {
			this._scatterSeriesModel = value;
			return value;
		} else {
			return this._scatterSeriesModel;
		}
	}
	,
	init: function (model) {
		$.ig.ScatterBaseView.prototype.init.call(this, model);
		this.scatterSeriesModel(model);
	},
	onInit: function () {
		$.ig.ScatterBaseView.prototype.onInit.call(this);
		if (!this.isThumbnailView()) {
			this.markerModel().markerType($.ig.MarkerType.prototype.automatic);
			this.model().legendItemBadgeTemplate((function () {
				var $ret = new $.ig.DataTemplate();
				$ret.render($.ig.LegendTemplates.prototype.pointBadgeTemplate);
				$ret.measure($.ig.LegendTemplates.prototype.legendItemBadgeMeasure);
				return $ret;
			}()));
		}
	}
	,
	applyDropShadowDefaultSettings: function () {
		var color = new $.ig.Color();
		color.colorString("rgba(95,95,95,0.5)");
		this.model().shadowColor(color);
		this.model().shadowBlur(3);
		this.model().shadowOffsetX(2);
		this.model().shadowOffsetY(2);
		this.model().useSingleShadow(false);
	}
	,
	$type: new $.ig.Type('ScatterSeriesView', $.ig.ScatterBaseView.prototype.$type)
}, true);

$.ig.util.defType('ScatterSplineSeriesView', 'ScatterBaseView', {
	init: function (model) {
		this._polyline = new $.ig.Path();
		$.ig.ScatterBaseView.prototype.init.call(this, model);
	},
	_polyline: null,
	setupAppearanceOverride: function () {
		$.ig.ScatterBaseView.prototype.setupAppearanceOverride.call(this);
		this._polyline.__stroke = this.model().actualBrush();
		this._polyline.strokeThickness(this.model().thickness());
	}
	,
	setupHitAppearanceOverride: function () {
		$.ig.ScatterBaseView.prototype.setupHitAppearanceOverride.call(this);
		var hitBrush = this.getHitBrush();
		this._polyline.__stroke = hitBrush;
		this._polyline.strokeThickness(this.model().thickness() + $.ig.SeriesView.prototype.hIT_THICKNESS_AUGMENT);
	}
	,
	renderOverride: function (context, isHitContext) {
		$.ig.ScatterBaseView.prototype.renderOverride.call(this, context, isHitContext);
		if (context.shouldRender()) {
			context.renderPath(this._polyline);
		}
	}
	,
	clearRendering: function (wipeClean) {
		$.ig.ScatterBaseView.prototype.clearRendering.call(this, wipeClean);
		this._polyline.data(null);
	}
	,
	exportViewShapes: function (svd) {
		$.ig.ScatterBaseView.prototype.exportViewShapes.call(this, svd);
		var shape = new $.ig.PathVisualData(1, "shape", this._polyline);
		shape.tags().add("Main");
		svd.shapes().add(shape);
	}
	,
	applyDropShadowDefaultSettings: function () {
		var color = new $.ig.Color();
		color.colorString("rgba(95,95,95,0.5)");
		this.model().shadowColor(color);
		this.model().shadowBlur(3);
		this.model().shadowOffsetX(1);
		this.model().shadowOffsetY(4);
		this.model().useSingleShadow(false);
	}
	,
	$type: new $.ig.Type('ScatterSplineSeriesView', $.ig.ScatterBaseView.prototype.$type)
}, true);

$.ig.EnableErrorBars.prototype.none = 0;
$.ig.EnableErrorBars.prototype.both = 1;
$.ig.EnableErrorBars.prototype.positive = 2;
$.ig.EnableErrorBars.prototype.negative = 3;

$.ig.BrushSelectionMode.prototype.select = 0;
$.ig.BrushSelectionMode.prototype.interpolate = 1;

$.ig.CollisionAvoidanceType.prototype.none = 0;
$.ig.CollisionAvoidanceType.prototype.omit = 1;
$.ig.CollisionAvoidanceType.prototype.fade = 2;
$.ig.CollisionAvoidanceType.prototype.omitAndShift = 3;
$.ig.CollisionAvoidanceType.prototype.fadeAndShift = 4;

$.ig.HighDensityScatterSeries.prototype.xAxisPropertyName = "XAxis";
$.ig.HighDensityScatterSeries.prototype.yAxisPropertyName = "YAxis";
$.ig.HighDensityScatterSeries.prototype.xMemberPathPropertyName = "XMemberPath";
$.ig.HighDensityScatterSeries.prototype.xColumnPropertyName = "XColumn";
$.ig.HighDensityScatterSeries.prototype.yMemberPathPropertyName = "YMemberPath";
$.ig.HighDensityScatterSeries.prototype.yColumnPropertyName = "YColumn";
$.ig.HighDensityScatterSeries.prototype.useBruteForcePropertyName = "UseBruteForce";
$.ig.HighDensityScatterSeries.prototype.progressiveLoadPropertyName = "ProgressiveLoad";
$.ig.HighDensityScatterSeries.prototype.mouseOverEnabledPropertyName = "MouseOverEnabled";
$.ig.HighDensityScatterSeries.prototype.maxRenderDepthPropertyName = "MaxRenderDepth";
$.ig.HighDensityScatterSeries.prototype.heatMinimumPropertyName = "HeatMinimum";
$.ig.HighDensityScatterSeries.prototype.heatMaximumPropertyName = "HeatMaximum";
$.ig.HighDensityScatterSeries.prototype.heatMinimumColorPropertyName = "HeatMinimumColor";
$.ig.HighDensityScatterSeries.prototype.heatMaximumColorPropertyName = "HeatMaximumColor";
$.ig.HighDensityScatterSeries.prototype.pointExtentPropertyName = "PointExtent";
$.ig.HighDensityScatterSeries.prototype.xAxisProperty = $.ig.DependencyProperty.prototype.register($.ig.HighDensityScatterSeries.prototype.xAxisPropertyName, $.ig.NumericXAxis.prototype.$type, $.ig.HighDensityScatterSeries.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.HighDensityScatterSeries.prototype.xAxisPropertyName, e.oldValue(), e.newValue());
}));
$.ig.HighDensityScatterSeries.prototype.yAxisProperty = $.ig.DependencyProperty.prototype.register($.ig.HighDensityScatterSeries.prototype.yAxisPropertyName, $.ig.NumericYAxis.prototype.$type, $.ig.HighDensityScatterSeries.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.HighDensityScatterSeries.prototype.yAxisPropertyName, e.oldValue(), e.newValue());
}));
$.ig.HighDensityScatterSeries.prototype.xMemberPathProperty = $.ig.DependencyProperty.prototype.register($.ig.HighDensityScatterSeries.prototype.xMemberPathPropertyName, String, $.ig.HighDensityScatterSeries.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.HighDensityScatterSeries.prototype.xMemberPathPropertyName, e.oldValue(), e.newValue());
}));
$.ig.HighDensityScatterSeries.prototype.yMemberPathProperty = $.ig.DependencyProperty.prototype.register($.ig.HighDensityScatterSeries.prototype.yMemberPathPropertyName, String, $.ig.HighDensityScatterSeries.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.HighDensityScatterSeries.prototype.yMemberPathPropertyName, e.oldValue(), e.newValue());
}));
$.ig.HighDensityScatterSeries.prototype.useBruteForceProperty = $.ig.DependencyProperty.prototype.register($.ig.HighDensityScatterSeries.prototype.useBruteForcePropertyName, $.ig.Boolean.prototype.$type, $.ig.HighDensityScatterSeries.prototype.$type, new $.ig.PropertyMetadata(2, false, function (sender, e) {
	(sender).raisePropertyChanged($.ig.HighDensityScatterSeries.prototype.useBruteForcePropertyName, e.oldValue(), e.newValue());
}));
$.ig.HighDensityScatterSeries.prototype.progressiveLoadProperty = $.ig.DependencyProperty.prototype.register($.ig.HighDensityScatterSeries.prototype.progressiveLoadPropertyName, $.ig.Boolean.prototype.$type, $.ig.HighDensityScatterSeries.prototype.$type, new $.ig.PropertyMetadata(2, true, function (sender, e) {
	(sender).raisePropertyChanged($.ig.HighDensityScatterSeries.prototype.progressiveLoadPropertyName, e.oldValue(), e.newValue());
}));
$.ig.HighDensityScatterSeries.prototype.mouseOverEnabledProperty = $.ig.DependencyProperty.prototype.register($.ig.HighDensityScatterSeries.prototype.mouseOverEnabledPropertyName, $.ig.Boolean.prototype.$type, $.ig.HighDensityScatterSeries.prototype.$type, new $.ig.PropertyMetadata(2, false, function (sender, e) {
	(sender).raisePropertyChanged($.ig.HighDensityScatterSeries.prototype.mouseOverEnabledPropertyName, e.oldValue(), e.newValue());
}));
$.ig.HighDensityScatterSeries.prototype.maxRenderDepthProperty = $.ig.DependencyProperty.prototype.register($.ig.HighDensityScatterSeries.prototype.maxRenderDepthPropertyName, $.ig.Number.prototype.$type, $.ig.HighDensityScatterSeries.prototype.$type, new $.ig.PropertyMetadata(2, 0x7FFFFFFF, function (sender, e) {
	(sender).raisePropertyChanged($.ig.HighDensityScatterSeries.prototype.maxRenderDepthPropertyName, e.oldValue(), e.newValue());
}));
$.ig.HighDensityScatterSeries.prototype.heatMinimumProperty = $.ig.DependencyProperty.prototype.register($.ig.HighDensityScatterSeries.prototype.heatMinimumPropertyName, Number, $.ig.HighDensityScatterSeries.prototype.$type, new $.ig.PropertyMetadata(2, 0, function (sender, e) {
	(sender).raisePropertyChanged($.ig.HighDensityScatterSeries.prototype.heatMinimumPropertyName, e.oldValue(), e.newValue());
}));
$.ig.HighDensityScatterSeries.prototype.heatMaximumProperty = $.ig.DependencyProperty.prototype.register($.ig.HighDensityScatterSeries.prototype.heatMaximumPropertyName, Number, $.ig.HighDensityScatterSeries.prototype.$type, new $.ig.PropertyMetadata(2, 50, function (sender, e) {
	(sender).raisePropertyChanged($.ig.HighDensityScatterSeries.prototype.heatMaximumPropertyName, e.oldValue(), e.newValue());
}));
$.ig.HighDensityScatterSeries.prototype.heatMinimumColorProperty = $.ig.DependencyProperty.prototype.register($.ig.HighDensityScatterSeries.prototype.heatMinimumColorPropertyName, $.ig.Color.prototype.$type, $.ig.HighDensityScatterSeries.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.HighDensityScatterSeries.prototype.heatMinimumColorPropertyName, e.oldValue(), e.newValue());
}));
$.ig.HighDensityScatterSeries.prototype.heatMaximumColorProperty = $.ig.DependencyProperty.prototype.register($.ig.HighDensityScatterSeries.prototype.heatMaximumColorPropertyName, $.ig.Color.prototype.$type, $.ig.HighDensityScatterSeries.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.HighDensityScatterSeries.prototype.heatMaximumColorPropertyName, e.oldValue(), e.newValue());
}));
$.ig.HighDensityScatterSeries.prototype.pointExtentProperty = $.ig.DependencyProperty.prototype.register($.ig.HighDensityScatterSeries.prototype.pointExtentPropertyName, $.ig.Number.prototype.$type, $.ig.HighDensityScatterSeries.prototype.$type, new $.ig.PropertyMetadata(2, 1, function (sender, e) {
	(sender).raisePropertyChanged($.ig.HighDensityScatterSeries.prototype.pointExtentPropertyName, e.oldValue(), e.newValue());
}));

$.ig.ErrorBarSettingsBase.prototype.defaultErrorBarStylePropertyName = "DefaultErrorBarStyle";
$.ig.ErrorBarSettingsBase.prototype.defaultErrorBarStyleProperty = $.ig.DependencyProperty.prototype.register($.ig.ErrorBarSettingsBase.prototype.defaultErrorBarStylePropertyName, $.ig.Style.prototype.$type, $.ig.ErrorBarSettingsBase.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.ErrorBarSettingsBase.prototype.defaultErrorBarStylePropertyName, e.oldValue(), e.newValue());
}));

$.ig.ScatterErrorBarSettings.prototype._enableErrorBarsHorizontalPropertyName = "EnableErrorBarsHorizontal";
$.ig.ScatterErrorBarSettings.prototype._horizontalCalculatorReferencePropertyName = "HorizontalCalculatorReference";
$.ig.ScatterErrorBarSettings.prototype._horizontalCalculatorPropertyName = "HorizontalCalculator";
$.ig.ScatterErrorBarSettings.prototype.horizontalErrorBarCapLengthPropertyName = "HorizontalErrorBarCapLength";
$.ig.ScatterErrorBarSettings.prototype._horizontalStrokePropertyName = "HorizontalStroke";
$.ig.ScatterErrorBarSettings.prototype._horizontalStrokeThicknessPropertyName = "HorizontalStrokeThickness";
$.ig.ScatterErrorBarSettings.prototype.horizontalErrorBarStylePropertyName = "HorizontalErrorBarStyle";
$.ig.ScatterErrorBarSettings.prototype._enableErrorBarsVerticalPropertyName = "EnableErrorBarsVertical";
$.ig.ScatterErrorBarSettings.prototype._verticalCalculatorReferencePropertyName = "VerticalCalculatorReference";
$.ig.ScatterErrorBarSettings.prototype._verticalCalculatorPropertyName = "VerticalCalculator";
$.ig.ScatterErrorBarSettings.prototype.verticalErrorBarCapLengthPropertyName = "VerticalErrorBarCapLength";
$.ig.ScatterErrorBarSettings.prototype._verticalStrokePropertyName = "VerticalStroke";
$.ig.ScatterErrorBarSettings.prototype._verticalStrokeThicknessPropertyName = "VerticalStrokeThickness";
$.ig.ScatterErrorBarSettings.prototype.verticalErrorBarStylePropertyName = "VerticalErrorBarStyle";
$.ig.ScatterErrorBarSettings.prototype.enableErrorBarsHorizontalProperty = $.ig.DependencyProperty.prototype.register($.ig.ScatterErrorBarSettings.prototype._enableErrorBarsHorizontalPropertyName, $.ig.EnableErrorBars.prototype.$type, $.ig.ScatterErrorBarSettings.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.EnableErrorBars.prototype.getBox($.ig.EnableErrorBars.prototype.none), function (sender, e) {
	(sender).raisePropertyChanged($.ig.ScatterErrorBarSettings.prototype._enableErrorBarsHorizontalPropertyName, e.oldValue(), e.newValue());
}));
$.ig.ScatterErrorBarSettings.prototype.horizontalCalculatorReferenceProperty = $.ig.DependencyProperty.prototype.register($.ig.ScatterErrorBarSettings.prototype._horizontalCalculatorReferencePropertyName, $.ig.ErrorBarCalculatorReference.prototype.$type, $.ig.ScatterErrorBarSettings.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.ErrorBarCalculatorReference.prototype.getBox($.ig.ErrorBarCalculatorReference.prototype.x), function (sender, e) {
	(sender).raisePropertyChanged($.ig.ScatterErrorBarSettings.prototype._horizontalCalculatorReferencePropertyName, e.oldValue(), e.newValue());
}));
$.ig.ScatterErrorBarSettings.prototype.horizontalCalculatorProperty = $.ig.DependencyProperty.prototype.register($.ig.ScatterErrorBarSettings.prototype._horizontalCalculatorPropertyName, $.ig.IErrorBarCalculator.prototype.$type, $.ig.ScatterErrorBarSettings.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.ScatterErrorBarSettings.prototype._horizontalCalculatorPropertyName, e.oldValue(), e.newValue());
}));
$.ig.ScatterErrorBarSettings.prototype.horizontalErrorBarCapLengthProperty = $.ig.DependencyProperty.prototype.register($.ig.ScatterErrorBarSettings.prototype.horizontalErrorBarCapLengthPropertyName, $.ig.Number.prototype.$type, $.ig.ScatterErrorBarSettings.prototype.$type, new $.ig.PropertyMetadata(2, 6, function (sender, e) {
	(sender).raisePropertyChanged($.ig.ScatterErrorBarSettings.prototype.horizontalErrorBarCapLengthPropertyName, e.oldValue(), e.newValue());
}));
$.ig.ScatterErrorBarSettings.prototype.horizontalStrokeProperty = $.ig.DependencyProperty.prototype.register($.ig.ScatterErrorBarSettings.prototype._horizontalStrokePropertyName, $.ig.Brush.prototype.$type, $.ig.ScatterErrorBarSettings.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.ScatterErrorBarSettings.prototype._horizontalStrokePropertyName, e.oldValue(), e.newValue());
}));
$.ig.ScatterErrorBarSettings.prototype.horizontalStrokeThicknessProperty = $.ig.DependencyProperty.prototype.register($.ig.ScatterErrorBarSettings.prototype._horizontalStrokeThicknessPropertyName, Number, $.ig.ScatterErrorBarSettings.prototype.$type, new $.ig.PropertyMetadata(2, 1, function (sender, e) {
	(sender).raisePropertyChanged($.ig.ScatterErrorBarSettings.prototype._horizontalStrokeThicknessPropertyName, e.oldValue(), e.newValue());
}));
$.ig.ScatterErrorBarSettings.prototype.horizontalErrorBarStyleProperty = $.ig.DependencyProperty.prototype.register($.ig.ScatterErrorBarSettings.prototype.horizontalErrorBarStylePropertyName, $.ig.Style.prototype.$type, $.ig.ScatterErrorBarSettings.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.ScatterErrorBarSettings.prototype.horizontalErrorBarStylePropertyName, e.oldValue(), e.newValue());
}));
$.ig.ScatterErrorBarSettings.prototype.enableErrorBarsVerticalProperty = $.ig.DependencyProperty.prototype.register($.ig.ScatterErrorBarSettings.prototype._enableErrorBarsVerticalPropertyName, $.ig.EnableErrorBars.prototype.$type, $.ig.ScatterErrorBarSettings.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.EnableErrorBars.prototype.getBox($.ig.EnableErrorBars.prototype.none), function (sender, e) {
	(sender).raisePropertyChanged($.ig.ScatterErrorBarSettings.prototype._enableErrorBarsVerticalPropertyName, e.oldValue(), e.newValue());
}));
$.ig.ScatterErrorBarSettings.prototype.verticalCalculatorReferenceProperty = $.ig.DependencyProperty.prototype.register($.ig.ScatterErrorBarSettings.prototype._verticalCalculatorReferencePropertyName, $.ig.ErrorBarCalculatorReference.prototype.$type, $.ig.ScatterErrorBarSettings.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.ErrorBarCalculatorReference.prototype.getBox($.ig.ErrorBarCalculatorReference.prototype.y), function (sender, e) {
	(sender).raisePropertyChanged($.ig.ScatterErrorBarSettings.prototype._verticalCalculatorReferencePropertyName, e.oldValue(), e.newValue());
}));
$.ig.ScatterErrorBarSettings.prototype.verticalCalculatorProperty = $.ig.DependencyProperty.prototype.register($.ig.ScatterErrorBarSettings.prototype._verticalCalculatorPropertyName, $.ig.IErrorBarCalculator.prototype.$type, $.ig.ScatterErrorBarSettings.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.ScatterErrorBarSettings.prototype._verticalCalculatorPropertyName, e.oldValue(), e.newValue());
}));
$.ig.ScatterErrorBarSettings.prototype.verticalErrorBarCapLengthProperty = $.ig.DependencyProperty.prototype.register($.ig.ScatterErrorBarSettings.prototype.verticalErrorBarCapLengthPropertyName, $.ig.Number.prototype.$type, $.ig.ScatterErrorBarSettings.prototype.$type, new $.ig.PropertyMetadata(2, 6, function (sender, e) {
	(sender).raisePropertyChanged($.ig.ScatterErrorBarSettings.prototype.verticalErrorBarCapLengthPropertyName, e.oldValue(), e.newValue());
}));
$.ig.ScatterErrorBarSettings.prototype.verticalStrokeProperty = $.ig.DependencyProperty.prototype.register($.ig.ScatterErrorBarSettings.prototype._verticalStrokePropertyName, $.ig.Brush.prototype.$type, $.ig.ScatterErrorBarSettings.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.ScatterErrorBarSettings.prototype._verticalStrokePropertyName, e.oldValue(), e.newValue());
}));
$.ig.ScatterErrorBarSettings.prototype.verticalStrokeThicknessProperty = $.ig.DependencyProperty.prototype.register($.ig.ScatterErrorBarSettings.prototype._verticalStrokeThicknessPropertyName, Number, $.ig.ScatterErrorBarSettings.prototype.$type, new $.ig.PropertyMetadata(2, 1, function (sender, e) {
	(sender).raisePropertyChanged($.ig.ScatterErrorBarSettings.prototype._verticalStrokeThicknessPropertyName, e.oldValue(), e.newValue());
}));
$.ig.ScatterErrorBarSettings.prototype.verticalErrorBarStyleProperty = $.ig.DependencyProperty.prototype.register($.ig.ScatterErrorBarSettings.prototype.verticalErrorBarStylePropertyName, $.ig.Style.prototype.$type, $.ig.ScatterErrorBarSettings.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.ScatterErrorBarSettings.prototype.verticalErrorBarStylePropertyName, e.oldValue(), e.newValue());
}));

$.ig.ScatterBase.prototype.xAxisPropertyName = "XAxis";
$.ig.ScatterBase.prototype.yAxisPropertyName = "YAxis";
$.ig.ScatterBase.prototype.xMemberPathPropertyName = "XMemberPath";
$.ig.ScatterBase.prototype.xColumnPropertyName = "XColumn";
$.ig.ScatterBase.prototype.yMemberPathPropertyName = "YMemberPath";
$.ig.ScatterBase.prototype.yColumnPropertyName = "YColumn";
$.ig.ScatterBase.prototype.markerCollisionAvoidancePropertyName = "MarkerCollisionAvoidance";
$.ig.ScatterBase.prototype.maximumMarkersPropertyName = "MaximumMarkers";
$.ig.ScatterBase.prototype._errorBarSettingsPropertyName = "ErrorBarSettings";
$.ig.ScatterBase.prototype.xAxisProperty = $.ig.DependencyProperty.prototype.register($.ig.ScatterBase.prototype.xAxisPropertyName, $.ig.NumericXAxis.prototype.$type, $.ig.ScatterBase.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.ScatterBase.prototype.xAxisPropertyName, e.oldValue(), e.newValue());
}));
$.ig.ScatterBase.prototype.yAxisProperty = $.ig.DependencyProperty.prototype.register($.ig.ScatterBase.prototype.yAxisPropertyName, $.ig.NumericYAxis.prototype.$type, $.ig.ScatterBase.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.ScatterBase.prototype.yAxisPropertyName, e.oldValue(), e.newValue());
}));
$.ig.ScatterBase.prototype.xMemberPathProperty = $.ig.DependencyProperty.prototype.register($.ig.ScatterBase.prototype.xMemberPathPropertyName, String, $.ig.ScatterBase.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.ScatterBase.prototype.xMemberPathPropertyName, e.oldValue(), e.newValue());
}));
$.ig.ScatterBase.prototype.yMemberPathProperty = $.ig.DependencyProperty.prototype.register($.ig.ScatterBase.prototype.yMemberPathPropertyName, String, $.ig.ScatterBase.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.ScatterBase.prototype.yMemberPathPropertyName, e.oldValue(), e.newValue());
}));
$.ig.ScatterBase.prototype.trendLineTypeProperty = $.ig.DependencyProperty.prototype.register($.ig.Series.prototype.trendLineTypePropertyName, $.ig.TrendLineType.prototype.$type, $.ig.ScatterBase.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.TrendLineType.prototype.getBox($.ig.TrendLineType.prototype.none), function (sender, e) {
	(sender).raisePropertyChanged($.ig.Series.prototype.trendLineTypePropertyName, e.oldValue(), e.newValue());
}));
$.ig.ScatterBase.prototype.trendLineBrushProperty = $.ig.DependencyProperty.prototype.register($.ig.Series.prototype.trendLineBrushPropertyName, $.ig.Brush.prototype.$type, $.ig.ScatterBase.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.Series.prototype.trendLineBrushPropertyName, e.oldValue(), e.newValue());
}));
$.ig.ScatterBase.prototype.actualTrendLineBrushProperty = $.ig.DependencyProperty.prototype.register($.ig.Series.prototype.trendLineActualBrushPropertyName, $.ig.Brush.prototype.$type, $.ig.ScatterBase.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.Series.prototype.trendLineActualBrushPropertyName, e.oldValue(), e.newValue());
}));
$.ig.ScatterBase.prototype.trendLineThicknessProperty = $.ig.DependencyProperty.prototype.register($.ig.Series.prototype.trendLineThicknessPropertyName, Number, $.ig.ScatterBase.prototype.$type, new $.ig.PropertyMetadata(2, 1.5, function (sender, e) {
	(sender).raisePropertyChanged($.ig.Series.prototype.trendLineThicknessPropertyName, e.oldValue(), e.newValue());
}));
$.ig.ScatterBase.prototype.trendLineDashCapProperty = $.ig.DependencyProperty.prototype.register($.ig.Series.prototype.trendLineDashCapPropertyName, $.ig.PenLineCap.prototype.$type, $.ig.ScatterBase.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.PenLineCap.prototype.getBox($.ig.PenLineCap.prototype.flat), function (sender, e) {
	(sender).raisePropertyChanged($.ig.Series.prototype.trendLineDashCapPropertyName, e.oldValue(), e.newValue());
}));
$.ig.ScatterBase.prototype.trendLineDashArrayProperty = $.ig.DependencyProperty.prototype.register($.ig.Series.prototype.trendLineDashArrayPropertyName, $.ig.DoubleCollection.prototype.$type, $.ig.ScatterBase.prototype.$type, new $.ig.PropertyMetadata(1, function (sender, e) {
	(sender).raisePropertyChanged($.ig.Series.prototype.trendLineDashArrayPropertyName, e.oldValue(), e.newValue());
}));
$.ig.ScatterBase.prototype.trendLinePeriodProperty = $.ig.DependencyProperty.prototype.register($.ig.Series.prototype.trendLinePeriodPropertyName, $.ig.Number.prototype.$type, $.ig.ScatterBase.prototype.$type, new $.ig.PropertyMetadata(2, 7, function (sender, e) {
	(sender).raisePropertyChanged($.ig.Series.prototype.trendLinePeriodPropertyName, e.oldValue(), e.newValue());
}));
$.ig.ScatterBase.prototype.markerCollisionAvoidanceProperty = $.ig.DependencyProperty.prototype.register($.ig.ScatterBase.prototype.markerCollisionAvoidancePropertyName, $.ig.CollisionAvoidanceType.prototype.$type, $.ig.ScatterBase.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.CollisionAvoidanceType.prototype.getBox($.ig.CollisionAvoidanceType.prototype.none), function (sender, e) {
	(sender).raisePropertyChanged($.ig.ScatterBase.prototype.markerCollisionAvoidancePropertyName, e.oldValue(), e.newValue());
}));
$.ig.ScatterBase.prototype.trendLineZIndexProperty = $.ig.DependencyProperty.prototype.register($.ig.Series.prototype.trendLineZIndexPropertyName, $.ig.Number.prototype.$type, $.ig.ScatterBase.prototype.$type, new $.ig.PropertyMetadata(2, 1001, function (sender, e) {
	(sender).raisePropertyChanged($.ig.Series.prototype.trendLineZIndexPropertyName, e.oldValue(), e.newValue());
}));
$.ig.ScatterBase.prototype.maximumMarkersProperty = $.ig.DependencyProperty.prototype.register($.ig.ScatterBase.prototype.maximumMarkersPropertyName, $.ig.Number.prototype.$type, $.ig.ScatterBase.prototype.$type, new $.ig.PropertyMetadata(2, 400, function (sender, e) {
	(sender).raisePropertyChanged($.ig.ScatterBase.prototype.maximumMarkersPropertyName, e.oldValue(), e.newValue());
}));
$.ig.ScatterBase.prototype.errorBarSettingsProperty = $.ig.DependencyProperty.prototype.register($.ig.ScatterBase.prototype._errorBarSettingsPropertyName, $.ig.ScatterErrorBarSettings.prototype.$type, $.ig.ScatterBase.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.ScatterBase.prototype._errorBarSettingsPropertyName, e.oldValue(), e.newValue());
}));

$.ig.SizeScale.prototype.minimumValuePropertyName = "MinimumValue";
$.ig.SizeScale.prototype.maximumValuePropertyName = "MaximumValue";
$.ig.SizeScale.prototype.isLogarithmicPropertyName = "IsLogarithmic";
$.ig.SizeScale.prototype.logarithmBasePropertyName = "LogarithmBase";
$.ig.SizeScale.prototype.minimumValueProperty = $.ig.DependencyProperty.prototype.register($.ig.SizeScale.prototype.minimumValuePropertyName, Number, $.ig.SizeScale.prototype.$type, new $.ig.PropertyMetadata(2, NaN, function (o, e) {
	(o).raisePropertyChanged($.ig.SizeScale.prototype.minimumValuePropertyName, e.oldValue(), e.newValue());
}));
$.ig.SizeScale.prototype.maximumValueProperty = $.ig.DependencyProperty.prototype.register($.ig.SizeScale.prototype.maximumValuePropertyName, Number, $.ig.SizeScale.prototype.$type, new $.ig.PropertyMetadata(2, NaN, function (o, e) {
	(o).raisePropertyChanged($.ig.SizeScale.prototype.maximumValuePropertyName, e.oldValue(), e.newValue());
}));
$.ig.SizeScale.prototype.isLogarithmicProperty = $.ig.DependencyProperty.prototype.register($.ig.SizeScale.prototype.isLogarithmicPropertyName, $.ig.Boolean.prototype.$type, $.ig.SizeScale.prototype.$type, new $.ig.PropertyMetadata(2, false, function (o, e) {
	(o).raisePropertyChanged($.ig.SizeScale.prototype.isLogarithmicPropertyName, e.oldValue(), e.newValue());
}));
$.ig.SizeScale.prototype.logarithmBaseProperty = $.ig.DependencyProperty.prototype.register($.ig.SizeScale.prototype.logarithmBasePropertyName, $.ig.Number.prototype.$type, $.ig.SizeScale.prototype.$type, new $.ig.PropertyMetadata(2, 10, function (o, e) {
	(o).raisePropertyChanged($.ig.SizeScale.prototype.logarithmBasePropertyName, e.oldValue(), e.newValue());
}));

$.ig.BubbleSeries.prototype.radiusMemberPathPropertyName = "RadiusMemberPath";
$.ig.BubbleSeries.prototype.radiusColumnPropertyName = "RadiusColumn";
$.ig.BubbleSeries.prototype.radiusScalePropertyName = "RadiusScale";
$.ig.BubbleSeries.prototype.labelMemberPathPropertyName = "LabelMemberPath";
$.ig.BubbleSeries.prototype.labelColumnPropertyName = "LabelColumn";
$.ig.BubbleSeries.prototype.fillMemberPathPropertyName = "FillMemberPath";
$.ig.BubbleSeries.prototype.fillScalePropertyName = "FillScale";
$.ig.BubbleSeries.prototype.fillColumnPropertyName = "FillColumn";
$.ig.BubbleSeries.prototype.radiusMemberPathProperty = $.ig.DependencyProperty.prototype.register($.ig.BubbleSeries.prototype.radiusMemberPathPropertyName, String, $.ig.BubbleSeries.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.BubbleSeries.prototype.radiusMemberPathPropertyName, e.oldValue(), e.newValue());
}));
$.ig.BubbleSeries.prototype.radiusScaleProperty = $.ig.DependencyProperty.prototype.register($.ig.BubbleSeries.prototype.radiusScalePropertyName, $.ig.SizeScale.prototype.$type, $.ig.BubbleSeries.prototype.$type, new $.ig.PropertyMetadata(1, function (sender, e) {
	(sender).raisePropertyChanged($.ig.BubbleSeries.prototype.radiusScalePropertyName, e.oldValue(), e.newValue());
}));
$.ig.BubbleSeries.prototype.labelMemberPathProperty = $.ig.DependencyProperty.prototype.register($.ig.BubbleSeries.prototype.labelMemberPathPropertyName, String, $.ig.BubbleSeries.prototype.$type, new $.ig.PropertyMetadata(1, function (sender, e) {
	(sender).raisePropertyChanged($.ig.BubbleSeries.prototype.labelMemberPathPropertyName, e.oldValue(), e.newValue());
}));
$.ig.BubbleSeries.prototype.fillMemberPathProperty = $.ig.DependencyProperty.prototype.register($.ig.BubbleSeries.prototype.fillMemberPathPropertyName, String, $.ig.BubbleSeries.prototype.$type, new $.ig.PropertyMetadata(1, function (sender, e) {
	(sender).raisePropertyChanged($.ig.BubbleSeries.prototype.fillMemberPathPropertyName, e.oldValue(), e.newValue());
}));
$.ig.BubbleSeries.prototype.fillScaleProperty = $.ig.DependencyProperty.prototype.register($.ig.BubbleSeries.prototype.fillScalePropertyName, $.ig.BrushScale.prototype.$type, $.ig.BubbleSeries.prototype.$type, new $.ig.PropertyMetadata(1, function (sender, e) {
	(sender).raisePropertyChanged($.ig.BubbleSeries.prototype.fillScalePropertyName, e.oldValue(), e.newValue());
}));

$.ig.CustomPaletteBrushScale.prototype.brushSelectionModePropertyName = "BrushSelectionMode";
$.ig.CustomPaletteBrushScale.prototype.brushSelectionModeProperty = $.ig.DependencyProperty.prototype.register($.ig.CustomPaletteBrushScale.prototype.brushSelectionModePropertyName, $.ig.BrushSelectionMode.prototype.$type, $.ig.CustomPaletteBrushScale.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.BrushSelectionMode.prototype.getBox($.ig.BrushSelectionMode.prototype.select), function (o, e) {
	(o).raisePropertyChanged($.ig.CustomPaletteBrushScale.prototype.brushSelectionModePropertyName, e.oldValue(), e.newValue());
}));

$.ig.ValueBrushScale.prototype.minimumValuePropertyName = "MinimumValue";
$.ig.ValueBrushScale.prototype.maximumValuePropertyName = "MaximumValue";
$.ig.ValueBrushScale.prototype.isLogarithmicPropertyName = "IsLogarithmic";
$.ig.ValueBrushScale.prototype.logarithmBasePropertyName = "LogarithmBase";
$.ig.ValueBrushScale.prototype.minimumValueProperty = $.ig.DependencyProperty.prototype.register($.ig.ValueBrushScale.prototype.minimumValuePropertyName, Number, $.ig.ValueBrushScale.prototype.$type, new $.ig.PropertyMetadata(2, NaN, function (o, e) {
	(o).raisePropertyChanged($.ig.ValueBrushScale.prototype.minimumValuePropertyName, e.oldValue(), e.newValue());
}));
$.ig.ValueBrushScale.prototype.maximumValueProperty = $.ig.DependencyProperty.prototype.register($.ig.ValueBrushScale.prototype.maximumValuePropertyName, Number, $.ig.ValueBrushScale.prototype.$type, new $.ig.PropertyMetadata(2, NaN, function (o, e) {
	(o).raisePropertyChanged($.ig.ValueBrushScale.prototype.maximumValuePropertyName, e.oldValue(), e.newValue());
}));
$.ig.ValueBrushScale.prototype.isLogarithmicProperty = $.ig.DependencyProperty.prototype.register($.ig.ValueBrushScale.prototype.isLogarithmicPropertyName, $.ig.Boolean.prototype.$type, $.ig.ValueBrushScale.prototype.$type, new $.ig.PropertyMetadata(2, false, function (o, e) {
	(o).raisePropertyChanged($.ig.ValueBrushScale.prototype.isLogarithmicPropertyName, e.oldValue(), e.newValue());
}));
$.ig.ValueBrushScale.prototype.logarithmBaseProperty = $.ig.DependencyProperty.prototype.register($.ig.ValueBrushScale.prototype.logarithmBasePropertyName, $.ig.Number.prototype.$type, $.ig.ValueBrushScale.prototype.$type, new $.ig.PropertyMetadata(2, 10, function (o, e) {
	(o).raisePropertyChanged($.ig.ValueBrushScale.prototype.logarithmBasePropertyName, e.oldValue(), e.newValue());
}));

$.ig.ScaleLegend.prototype.parentVisibilityPropertyName = "ParentVisibility";
$.ig.ScaleLegend.prototype.seriesMarkerBrushPropertyName = "SeriesMarkerBrush";
$.ig.ScaleLegend.prototype.parentVisibilityProperty = $.ig.DependencyProperty.prototype.register($.ig.ScaleLegend.prototype.parentVisibilityPropertyName, $.ig.Visibility.prototype.$type, $.ig.ScaleLegend.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.Visibility.prototype.getBox($.ig.Visibility.prototype.visible), function (o, e) {
	if ($.ig.util.getEnumValue(e.newValue()) != $.ig.Visibility.prototype.visible) {
		(o).restoreOriginalState();
	} else {
		(o).renderLegend();
	}
}));
$.ig.ScaleLegend.prototype.seriesMarkerBrushProperty = $.ig.DependencyProperty.prototype.register($.ig.ScaleLegend.prototype.seriesMarkerBrushPropertyName, $.ig.Brush.prototype.$type, $.ig.ScaleLegend.prototype.$type, new $.ig.PropertyMetadata(1, function (o, e) {
	(o).renderLegend();
}));

$.ig.ScatterLineSeries.prototype._fLATTENER_CHUNKING = 512;
$.ig.ScatterLineSeries.prototype._unknownValuePlottingPropertyName = "UnknownValuePlotting";
$.ig.ScatterLineSeries.prototype.unknownValuePlottingProperty = $.ig.DependencyProperty.prototype.register($.ig.ScatterLineSeries.prototype._unknownValuePlottingPropertyName, $.ig.UnknownValuePlotting.prototype.$type, $.ig.ScatterLineSeries.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.UnknownValuePlotting.prototype.getBox($.ig.UnknownValuePlotting.prototype.dontPlot), function (sender, e) {
	(sender).raisePropertyChanged($.ig.ScatterLineSeries.prototype._unknownValuePlottingPropertyName, e.oldValue(), e.newValue());
}));

$.ig.ScatterSplineSeries.prototype.stiffnessPropertyName = "Stiffness";
$.ig.ScatterSplineSeries.prototype.stiffnessProperty = $.ig.DependencyProperty.prototype.register($.ig.ScatterSplineSeries.prototype.stiffnessPropertyName, Number, $.ig.ScatterSplineSeries.prototype.$type, new $.ig.PropertyMetadata(2, 0.5, function (sender, e) {
	(sender).raisePropertyChanged($.ig.ScatterSplineSeries.prototype.stiffnessPropertyName, e.oldValue(), e.newValue());
}));

$.ig.Legend.prototype.orientationPropertyName = "Orientation";

$.ig.ScaleLegendView.prototype._legendScaleElementName = "LegendScale";
$.ig.ScaleLegendView.prototype.tEXT_MARGIN = 0;

} (jQuery));


