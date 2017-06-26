/*!@license
* Infragistics.Web.ClientUI infragistics.chart_financialchart.js 16.1.20161.2145
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
"GenericEnumerator$1:ck"]);


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
"Func$2:bf", 
"MulticastDelegate:bg", 
"IntPtr:bh", 
"Delegate:bj", 
"Interlocked:bk", 
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
"NotifyCollectionChangedEventArgs:d1", 
"NotifyCollectionChangedAction:d2", 
"Dictionary$2:d8", 
"IDictionary$2:d9", 
"IDictionary:ea", 
"KeyValuePair$2:eb", 
"Enumerable:ec", 
"Thread:ed", 
"ThreadStart:ee", 
"IOrderedEnumerable$1:ef", 
"SortedList$1:eg", 
"ArgumentNullException:eh", 
"IEqualityComparer$1:ei", 
"EqualityComparer$1:ej", 
"IEqualityComparer:ek", 
"DefaultEqualityComparer$1:el", 
"InvalidOperationException:em", 
"ArgumentException:en", 
"FrameworkElement:eu", 
"Visibility:ev", 
"Style:ew", 
"Geometry:e1", 
"GeometryType:e2", 
"PointCollection:e5", 
"DataTemplateRenderInfo:e7", 
"DataTemplatePassInfo:e8", 
"ContentControl:e9", 
"Control:fa", 
"Thickness:fb", 
"HorizontalAlignment:fc", 
"VerticalAlignment:fd", 
"DataTemplate:fe", 
"DataTemplateRenderHandler:ff", 
"DataTemplateMeasureHandler:fg", 
"DataTemplateMeasureInfo:fh", 
"DataTemplatePassHandler:fi", 
"GeometryGroup:fx", 
"GeometryCollection:fy", 
"FillRule:fz", 
"PathGeometry:f0", 
"PathFigureCollection:f1", 
"ArcSegment:f5", 
"PathSegment:f6", 
"PathSegmentType:f7", 
"SweepDirection:f8", 
"PathFigure:f9", 
"PathSegmentCollection:ga", 
"LineSegment:gb", 
"PolyLineSegment:gc", 
"INotifyPropertyChanged:gn", 
"PropertyChangedEventHandler:go", 
"PropertyChangedEventArgs:gp", 
"ObservableCollection$1:hb", 
"INotifyCollectionChanged:hc", 
"NotifyCollectionChangedEventHandler:hd", 
"Func$1:ib", 
"Extensions:jf", 
"Panel:jg", 
"UIElementCollection:jh", 
"PathFigureUtil:jn", 
"AbstractEnumerable:kp", 
"AbstractEnumerator:kq", 
"GenericEnumerable$1:kr", 
"GenericEnumerator$1:ks"]);


$.ig.util.defType('Extensions', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	reset1: function (geom) {
		geom.figures().clear();
		geom.figures().add(new $.ig.PathFigure());
		geom.figures().removeAt(0);
	}
	,
	reset: function (geom) {
		geom.children().clear();
		geom.children().add(new $.ig.PathGeometry());
		geom.children().removeAt(0);
	}
	,
	detach: function (child) {
		if (child == null) {
			return;
		}
		var parent = $.ig.util.cast($.ig.Panel.prototype.$type, child.parent());
		if (parent != null) {
			parent.children().remove(child);
			return;
		}
		var cont = $.ig.util.cast($.ig.ContentControl.prototype.$type, child.parent());
		if (cont != null) {
			cont.content(null);
			return;
		}
	}
	,
	transferChildrenTo: function (from, to) {
		var transfer = new $.ig.List$1($.ig.UIElement.prototype.$type, 0);
		var en = $.ig.Enumerable.prototype.ofType$1($.ig.UIElement.prototype.$type, from.children()).getEnumerator();
		while (en.moveNext()) {
			var child = en.current();
			transfer.add(child);
		}
		var en1 = transfer.getEnumerator();
		while (en1.moveNext()) {
			var child1 = en1.current();
			from.children().remove(child1);
			to.children().add(child1);
		}
	}
	,
	isPlottable: function (point) {
		return !$.ig.util.isNaN(point.__x) && !$.ig.util.isNaN(point.__y) && !Number.isInfinity(point.__x) && !Number.isInfinity(point.__y);
	}
	,
	isPlottable1: function (rect) {
		return !$.ig.util.isNaN(rect.left()) && !$.ig.util.isNaN(rect.right()) && !$.ig.util.isNaN(rect.top()) && !$.ig.util.isNaN(rect.bottom()) && !Number.isInfinity(rect.left()) && !Number.isInfinity(rect.right()) && !Number.isInfinity(rect.top()) && !Number.isInfinity(rect.bottom());
	}
	,
	$type: new $.ig.Type('Extensions', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('PathFigureUtil', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	duplicate1: function (pathFigureCollection) {
		var dup = new $.ig.PathFigureCollection();
		var en = pathFigureCollection.getEnumerator();
		while (en.moveNext()) {
			var pathFigure = en.current();
			dup.add($.ig.PathFigureUtil.prototype.duplicate(pathFigure));
		}
		return dup;
	}
	,
	duplicate: function (pathFigure) {
		if (pathFigure == null) {
			return null;
		}
		var segments = new $.ig.PathSegmentCollection();
		var en = pathFigure.__segments.getEnumerator();
		while (en.moveNext()) {
			var pathSegment = en.current();
			switch (pathSegment.type()) {
				case $.ig.PathSegmentType.prototype.arc:
					var arcSeg = pathSegment;
					var newArcSeg = new $.ig.ArcSegment();
					newArcSeg.isLargeArc(arcSeg.isLargeArc());
					newArcSeg.point(arcSeg.point());
					newArcSeg.rotationAngle(arcSeg.rotationAngle());
					newArcSeg.size(arcSeg.size());
					newArcSeg.sweepDirection(arcSeg.sweepDirection());
					segments.add(newArcSeg);
					break;
				case $.ig.PathSegmentType.prototype.line:
					var lineSeg = pathSegment;
					var newLineSeg = new $.ig.LineSegment(1);
					newLineSeg.point(lineSeg.point());
					segments.add(newLineSeg);
					break;
				case $.ig.PathSegmentType.prototype.polyLine:
					var polyLineSeg = pathSegment;
					var newPolyLineSeg = new $.ig.PolyLineSegment();
					var en1 = polyLineSeg.__points.getEnumerator();
					while (en1.moveNext()) {
						var p = en1.current();
						newPolyLineSeg.__points.add(p);
					}
					segments.add(newPolyLineSeg);
					break;
			}
		}
		return (function () {
			var $ret = new $.ig.PathFigure();
			$ret.isClosed(pathFigure.__isClosed);
			$ret.isFilled(pathFigure.__isFilled);
			$ret.startPoint(pathFigure.__startPoint);
			$ret.segments(segments);
			return $ret;
		}());
	}
	,
	$type: new $.ig.Type('PathFigureUtil', $.ig.Object.prototype.$type)
}, true);

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
"BarFramePreparer:yi", 
"BarTrendFitCalculator:yj", 
"BarTrendLineManager:yk", 
"VerticalAnchoredCategorySeries:yl", 
"BarSeries:ym", 
"BarSeriesView:yn", 
"BarBucketCalculator:yo", 
"IndicatorRenderer:yr", 
"PathFigureUtil:ys", 
"IndicatorDisplayType:zc", 
"PriceDisplayType:zd", 
"FinancialIndicator:ze", 
"FinancialIndicatorView:zf", 
"FinancialIndicatorBucketCalculator:zg", 
"StrategyBasedIndicator:zh", 
"IndicatorCalculationStrategy:zi", 
"AbsoluteVolumeOscillatorIndicator:zj", 
"AbsoluteVolumeOscillatorIndicatorStrategy:zk", 
"AccumulationDistributionIndicator:zl", 
"AccumulationDistributionIndicatorStrategy:zm", 
"StreamingIndicatorCalculationStrategy:zn", 
"AverageDirectionalIndexIndicator:zo", 
"AverageDirectionalIndexIndicatorStrategy:zp", 
"AverageTrueRangeIndicator:zq", 
"AverageTrueRangeIndicatorStrategy:zr", 
"FinancialOverlay:zs", 
"BollingerBandsOverlay:zt", 
"BollingerBandsOverlayView:zu", 
"BollingerBandsBucketCalculator:zv", 
"BollingerBandWidthIndicator:zw", 
"BollingerBandWidthIndicatorStrategy:zx", 
"ChaikinOscillatorIndicator:zy", 
"ChaikinOscillatorIndicatorStrategy:zz", 
"ChaikinVolatilityIndicator:z0", 
"ChaikinVolatilityIndicatorStrategy:z1", 
"CommodityChannelIndexIndicator:z2", 
"CommodityChannelIndexIndicatorStrategy:z3", 
"CustomIndicator:z4", 
"CustomIndicatorStrategy:z5", 
"DetrendedPriceOscillatorIndicator:z6", 
"DetrendedPriceOscillatorIndicatorStrategy:z7", 
"EaseOfMovementIndicator:z8", 
"EaseOfMovementIndicatorStrategy:z9", 
"FastStochasticOscillatorIndicator:aaa", 
"FastStochasticOscillatorIndicatorStrategy:aab", 
"PercentKCalculationStrategy:aac", 
"FinancialPriceSeries:aad", 
"FinancialPriceSeriesView:aae", 
"FinancialPriceBucketCalculator:aaf", 
"ForceIndexIndicator:aag", 
"ForceIndexIndicatorStrategy:aah", 
"FullStochasticOscillatorIndicator:aai", 
"FullStochasticOscillatorIndicatorStrategy:aaj", 
"PositiveVolumeIndexIndicator:aak", 
"PositiveVolumeIndexIndicatorStrategy:aal", 
"ItemwiseIndicatorCalculationStrategy:aam", 
"ItemwiseStrategyBasedIndicator:aan", 
"ItemwiseStrategyCalculationStrategy:aao", 
"MassIndexIndicator:aap", 
"MassIndexIndicatorStrategy:aaq", 
"MedianPriceIndicator:aar", 
"MedianPriceIndicatorStrategy:aas", 
"MarketFacilitationIndexIndicator:aat", 
"MarketFacilitationIndexIndicatorStrategy:aau", 
"MoneyFlowIndexIndicator:aav", 
"MoneyFlowIndexIndicatorStrategy:aaw", 
"MovingAverageConvergenceDivergenceIndicator:aax", 
"MovingAverageConvergenceDivergenceIndicatorStrategy:aay", 
"NegativeVolumeIndexIndicator:aaz", 
"NegativeVolumeIndexIndicatorStrategy:aa0", 
"OnBalanceVolumeIndicator:aa1", 
"OnBalanceVolumeIndicatorStrategy:aa2", 
"PercentagePriceOscillatorIndicator:aa3", 
"PercentagePriceOscillatorIndicatorStrategy:aa4", 
"PercentageVolumeOscillatorIndicator:aa5", 
"PercentageVolumeOscillatorIndicatorStrategy:aa6", 
"PriceChannelOverlay:aa7", 
"PriceChannelOverlayView:aa8", 
"PriceChannelBucketCalculator:aa9", 
"PriceVolumeTrendIndicator:aba", 
"PriceVolumeTrendIndicatorStrategy:abb", 
"RateOfChangeAndMomentumIndicator:abc", 
"RateOfChangeAndMomentumIndicatorStrategy:abd", 
"RelativeStrengthIndexIndicator:abe", 
"RelativeStrengthIndexIndicatorStrategy:abf", 
"SlowStochasticOscillatorIndicator:abg", 
"SlowStochasticOscillatorIndicatorStrategy:abh", 
"StandardDeviationIndicator:abi", 
"StandardDeviationIndicatorStrategy:abj", 
"StochRSIIndicator:abk", 
"StochRSIIndicatorStrategy:abl", 
"TRIXIndicator:abm", 
"TRIXIndicatorStrategy:abn", 
"TypicalPriceIndicator:abo", 
"TypicalPriceIndicatorStrategy:abp", 
"UltimateOscillatorIndicator:abq", 
"UltimateOscillatorIndicatorCalculationStrategy:abr", 
"WeightedCloseIndicator:abs", 
"WeightedCloseIndicatorStrategy:abt", 
"WilliamsPercentRIndicator:abu", 
"WilliamsPercentRIndicatorStrategy:abv", 
"Legend:ac1", 
"LegendView:ac2", 
"AbstractEnumerable:adb", 
"AbstractEnumerator:adc", 
"GenericEnumerable$1:add", 
"GenericEnumerator$1:ade"]);


$.ig.util.defType('PriceDisplayType', 'Enum', {
	init: function (v) {
		this._v = v;
	},
	$getName: function (v) {
		switch (v) {
			case 0: return "Candlestick";
			case 1: return "OHLC";
			default: return v.toString();
		}
	},
	$value: function () {
		return this._v;
	},
	$type: new $.ig.Type('PriceDisplayType', $.ig.Enum.prototype.$type)
}, true);

$.ig.util.defType('IndicatorDisplayType', 'Enum', {
	init: function (v) {
		this._v = v;
	},
	$getName: function (v) {
		switch (v) {
			case 0: return "Line";
			case 1: return "Area";
			case 2: return "Column";
			default: return v.toString();
		}
	},
	$value: function () {
		return this._v;
	},
	$type: new $.ig.Type('IndicatorDisplayType', $.ig.Enum.prototype.$type)
}, true);

$.ig.util.defType('IndicatorRenderer', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	output: function (segments, x0, y0, resolution) {
		var pathFigure = new $.ig.PathFigure();
		var flattenedIndices = $.ig.Flattener.prototype.flatten(new $.ig.List$1($.ig.Number.prototype.$type, 0), segments, x0, y0, 0, segments.count() - 1, resolution);
		for (var j = 0; j < flattenedIndices.count(); j++) {
			var k = flattenedIndices.item(j);
			pathFigure.__segments.add((function () {
				var $ret = new $.ig.LineSegment(1);
				$ret.point({ __x: x0(k), __y: y0(k), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				return $ret;
			}()));
		}
		pathFigure.__startPoint = (pathFigure.__segments.__inner[0]).point();
		return pathFigure;
	}
	,
	rasterizeLine: function (count, x0, y0, x1, y1, colorByGradient, windowRect, viewportRect, positivePath0, positivePath01, positivePath1, negativePath0, negativePath01, negativePath1, bucketSize, resolution) {
		var positiveFigures0 = (positivePath0.data()).figures();
		var negativeFigures0 = (negativePath0.data()).figures();
		if (bucketSize == 1) {
			var currentSegment = new $.ig.List$1($.ig.Number.prototype.$type, 0);
			var currentFigures0 = positiveFigures0;
			var currentType = 0;
			currentSegment.add(0);
			for (var i = 0, j = 1; j < count; i = j++) {
				var type = currentType;
				var valueDelegateResult = y0(j) - y0(i);
				if (colorByGradient && !$.ig.util.isNaN(valueDelegateResult)) {
					type = Math.sign(valueDelegateResult);
				}
				if (type != 0 && type != currentType) {
					currentFigures0.add($.ig.IndicatorRenderer.prototype.output(currentSegment, x0, y0, resolution));
					currentType = type;
					currentFigures0 = currentType == 1 ? negativeFigures0 : positiveFigures0;
					currentSegment.clear();
					currentSegment.add(i);
				}
				currentSegment.add(j);
			}
			currentFigures0.add($.ig.IndicatorRenderer.prototype.output(currentSegment, x0, y0, resolution));
		} else {
			var currentSegment1 = new $.ig.List$1($.ig.Number.prototype.$type, 0);
			var currentFigures01 = positiveFigures0;
			var currentType1 = 0;
			currentSegment1.add(0);
			for (var i1 = 0, j1 = 1; j1 < count; i1 = j1++) {
				var type1 = currentType1;
				var valueDelegateResult1 = y0(j1) - y0(i1);
				if (colorByGradient && !$.ig.util.isNaN(valueDelegateResult1)) {
					type1 = Math.sign(valueDelegateResult1);
				}
				if (type1 != 0 && type1 != currentType1) {
					if (currentSegment1.count() > 0) {
						currentFigures01.add($.ig.IndicatorRenderer.prototype.output(currentSegment1, x0, y0, resolution));
					}
					currentType1 = type1;
					currentFigures01 = currentType1 == 1 ? negativeFigures0 : positiveFigures0;
					currentSegment1.clear();
					currentSegment1.add(i1);
				}
				currentSegment1.add(j1);
			}
			if (currentSegment1.count() > 0) {
				currentFigures01.add($.ig.IndicatorRenderer.prototype.output(currentSegment1, x0, y0, resolution));
			}
		}
	}
	,
	rasterizeArea: function (count, x0, y0, x1, y1, colorByGradient, windowRect, viewportRect, positivePath0, positivePath01, positivePath1, negativePath0, negativePath01, negativePath1, worldZero, bucketSize, resolution) {
		var positiveFigures0 = (positivePath0.data()).figures();
		var positiveFigures01 = (positivePath01.data()).figures();
		var negativeFigures0 = (negativePath0.data()).figures();
		var negativeFigures01 = (negativePath01.data()).figures();
		if (bucketSize == 1) {
			var currentSegment = new $.ig.List$1($.ig.Number.prototype.$type, 0);
			var currentFigures0 = positiveFigures0;
			var currentFigures01 = positiveFigures01;
			var currentType = 0;
			currentSegment.add(0);
			for (var i = 0, j = 1; j < count; i = j++) {
				var type = currentType;
				var valueDelegateResult = y0(j) - y0(i);
				if (colorByGradient && !$.ig.util.isNaN(valueDelegateResult)) {
					type = Math.sign(valueDelegateResult);
				}
				if (type != 0 && type != currentType) {
					if (currentSegment.count() > 0) {
						var figure0 = $.ig.IndicatorRenderer.prototype.output(currentSegment, x0, y0, resolution);
						var figure01 = $.ig.PathFigureUtil.prototype.duplicate(figure0);
						figure01.__segments.add((function () {
							var $ret = new $.ig.LineSegment(1);
							$ret.point({ __x: (figure0.__segments.__inner[figure0.__segments.count() - 1]).point().__x, __y: worldZero, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
							return $ret;
						}()));
						figure01.__segments.add((function () {
							var $ret = new $.ig.LineSegment(1);
							$ret.point({ __x: (figure0.__segments.__inner[0]).point().__x, __y: worldZero, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
							return $ret;
						}()));
						currentFigures0.add(figure0);
						currentFigures01.add(figure01);
					}
					currentType = type;
					currentFigures0 = currentType == 1 ? negativeFigures0 : positiveFigures0;
					currentFigures01 = currentType == 1 ? negativeFigures01 : positiveFigures01;
					currentSegment.clear();
					currentSegment.add(i);
				}
				currentSegment.add(j);
			}
			{
				var figure02 = $.ig.IndicatorRenderer.prototype.output(currentSegment, x0, y0, resolution);
				var figure011 = $.ig.PathFigureUtil.prototype.duplicate(figure02);
				figure011.__segments.add((function () {
					var $ret = new $.ig.LineSegment(1);
					$ret.point({ __x: (figure02.__segments.__inner[figure02.__segments.count() - 1]).point().__x, __y: worldZero, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
					return $ret;
				}()));
				figure011.__segments.add((function () {
					var $ret = new $.ig.LineSegment(1);
					$ret.point({ __x: (figure02.__segments.__inner[0]).point().__x, __y: worldZero, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
					return $ret;
				}()));
				currentFigures0.add(figure02);
				currentFigures01.add(figure011);
			}
		} else {
			var currentSegment1 = new $.ig.List$1($.ig.Number.prototype.$type, 0);
			var currentFigures02 = positiveFigures0;
			var currentFigures011 = positiveFigures01;
			var currentType1 = 0;
			currentSegment1.add(0);
			for (var i1 = 0, j1 = 1; j1 < count; i1 = j1++) {
				var type1 = currentType1;
				var valueDelegateResult1 = (y0(j1) + y1(j1)) - (y0(i1) + y1(i1));
				if (colorByGradient && !$.ig.util.isNaN(valueDelegateResult1)) {
					type1 = Math.sign(valueDelegateResult1);
				}
				if (type1 != 0 && type1 != currentType1) {
					if (currentSegment1.count() > 0) {
						var figure03 = $.ig.IndicatorRenderer.prototype.output(currentSegment1, x0, y0, resolution);
						var figure012 = $.ig.PathFigureUtil.prototype.duplicate(figure03);
						figure012.__segments.add((function () {
							var $ret = new $.ig.LineSegment(1);
							$ret.point({ __x: (figure03.__segments.__inner[figure03.__segments.count() - 1]).point().__x, __y: worldZero, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
							return $ret;
						}()));
						figure012.__segments.add((function () {
							var $ret = new $.ig.LineSegment(1);
							$ret.point({ __x: (figure03.__segments.__inner[0]).point().__x, __y: worldZero, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
							return $ret;
						}()));
						currentFigures02.add(figure03);
						currentFigures011.add(figure012);
					}
					currentType1 = type1;
					currentFigures02 = currentType1 == 1 ? negativeFigures0 : positiveFigures0;
					currentFigures011 = currentType1 == 1 ? negativeFigures01 : positiveFigures01;
					currentSegment1.clear();
					currentSegment1.add(i1);
				}
				currentSegment1.add(j1);
			}
			{
				var figure04 = $.ig.IndicatorRenderer.prototype.output(currentSegment1, x0, y0, resolution);
				var figure013 = $.ig.PathFigureUtil.prototype.duplicate(figure04);
				figure013.__segments.add((function () {
					var $ret = new $.ig.LineSegment(1);
					$ret.point({ __x: (figure04.__segments.__inner[figure04.__segments.count() - 1]).point().__x, __y: worldZero, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
					return $ret;
				}()));
				figure013.__segments.add((function () {
					var $ret = new $.ig.LineSegment(1);
					$ret.point({ __x: (figure04.__segments.__inner[0]).point().__x, __y: worldZero, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
					return $ret;
				}()));
				currentFigures02.add(figure04);
				currentFigures011.add(figure013);
			}
		}
	}
	,
	rasterizeColumns: function (count, x0, y0, x1, y1, colorByGradient, worldZero, Columns, positiveColumns, negativeColumns) {
		var positiveGeometryGroup = $.ig.util.cast($.ig.GeometryGroup.prototype.$type, positiveColumns.data());
		var negativeGeometryGroup = $.ig.util.cast($.ig.GeometryGroup.prototype.$type, negativeColumns.data());
		for (var i = 0; i < count; ++i) {
			var column = Columns.item(i);
			column.startPoint({ __x: x0(i), __y: worldZero, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			column.endPoint({ __x: x0(i), __y: y0(i), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			var pos = false;
			if (i > 0) {
				if (y0(i) <= y0(i - 1)) {
					pos = true;
				}
			} else {
				if (count > 1) {
					if (y0(i + 1) <= y0(i)) {
						pos = true;
					}
				}
			}
			if (pos) {
				positiveGeometryGroup.children().add(column);
			} else {
				negativeGeometryGroup.children().add(column);
			}
		}
		Columns.count(count);
	}
	,
	$type: new $.ig.Type('IndicatorRenderer', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('RangeValueList', 'Object', {
	__highColumn: null,
	__lowColumn: null,
	init: function (highColumn, lowColumn) {
		$.ig.Object.prototype.init.call(this);
		this.__highColumn = highColumn;
		this.__lowColumn = lowColumn;
	},
	indexOf: function (item) {
		throw new $.ig.NotImplementedException(0);
	}
	,
	insert: function (index, item) {
		throw new $.ig.NotImplementedException(0);
	}
	,
	removeAt: function (index) {
		throw new $.ig.NotImplementedException(0);
	}
	,
	item: function (index, value) {
		if (arguments.length === 2) {
			throw new $.ig.NotImplementedException(0);
			return value;
		} else {
			var high = NaN;
			var low = NaN;
			if (this.__highColumn != null && index >= 0 && index < this.__highColumn.count()) {
				high = this.__highColumn.item(index);
			}
			if (this.__lowColumn != null && index >= 0 && index < this.__lowColumn.count()) {
				low = this.__lowColumn.item(index);
			}
			var highIsNaN = $.ig.util.isNaN(high);
			var lowIsNaN = $.ig.util.isNaN(low);
			if (!highIsNaN && !lowIsNaN) {
				return (high + low) / 2;
			}
			if (!highIsNaN) {
				return high;
			}
			if (!lowIsNaN) {
				return low;
			}
			return NaN;
		}
	}
	,
	add: function (item) {
		throw new $.ig.NotImplementedException(0);
	}
	,
	clear: function () {
		throw new $.ig.NotImplementedException(0);
	}
	,
	contains: function (item) {
		throw new $.ig.NotImplementedException(0);
	}
	,
	copyTo: function (array, arrayIndex) {
		throw new $.ig.NotImplementedException(0);
	}
	,
	count: function () {
		var highCount = 0;
		var lowCount = 0;
		if (this.__highColumn != null) {
			highCount = this.__highColumn.count();
		}
		if (this.__lowColumn != null) {
			lowCount = this.__lowColumn.count();
		}
		var count = 0;
		count = Math.max(count, highCount);
		count = Math.max(count, lowCount);
		return count;
	}
	,
	isReadOnly: function () {
		return true;
	}
	,
	remove: function (item) {
		throw new $.ig.NotImplementedException(0);
	}
	,
	getEnumerator: function () {
		throw new $.ig.NotImplementedException(0);
	}
	,
	getEnumerator: function () {
		throw new $.ig.NotImplementedException(0);
	}
	,
	$type: new $.ig.Type('RangeValueList', $.ig.Object.prototype.$type, [$.ig.IList$1.prototype.$type.specialize(Number)])
}, true);

$.ig.util.defType('FinancialEventArgs', 'EventArgs', {
	init: function (position, count, dataSource, supportingCalculations) {
		$.ig.EventArgs.prototype.init.call(this);
		this.position(position);
		this.count(count);
		this.dataSource(dataSource);
		this.supportingCalculations(supportingCalculations);
	},
	_position: 0,
	position: function (value) {
		if (arguments.length === 1) {
			this._position = value;
			return value;
		} else {
			return this._position;
		}
	}
	,
	_count: 0,
	count: function (value) {
		if (arguments.length === 1) {
			this._count = value;
			return value;
		} else {
			return this._count;
		}
	}
	,
	_dataSource: null,
	dataSource: function (value) {
		if (arguments.length === 1) {
			this._dataSource = value;
			return value;
		} else {
			return this._dataSource;
		}
	}
	,
	_supportingCalculations: null,
	supportingCalculations: function (value) {
		if (arguments.length === 1) {
			this._supportingCalculations = value;
			return value;
		} else {
			return this._supportingCalculations;
		}
	}
	,
	_basedOn: null,
	basedOn: function (value) {
		if (arguments.length === 1) {
			this._basedOn = value;
			return value;
		} else {
			return this._basedOn;
		}
	}
	,
	$type: new $.ig.Type('FinancialEventArgs', $.ig.EventArgs.prototype.$type)
}, true);

$.ig.util.defType('FinancialSeries', 'Series', {
	onViewCreated: function (view) {
		$.ig.Series.prototype.onViewCreated.call(this, view);
		this.financialView(view);
	}
	,
	_financialView: null,
	financialView: function (value) {
		if (arguments.length === 1) {
			this._financialView = value;
			return value;
		} else {
			return this._financialView;
		}
	}
	,
	isFinancial: function () {
		return true;
	}
	,
	getHighStrokeSegments: function (position) {
		var matching = this.getMatchingBuckets(this.xAxis(), this._currentFrame._buckets, this.getBucketSize(this.view()), this.getFirstBucket(this.view()), this.toWorldPosition(position), true);
		if (matching == null) {
			return null;
		}
		var prevBucketValue = matching[0];
		var nextBucketValue = matching[1];
		var ret = new Array(1);
		var seg = new Array(2);
		seg[0] = { __x: prevBucketValue[0], __y: prevBucketValue[2], $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		seg[1] = { __x: nextBucketValue[0], __y: nextBucketValue[2], $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		ret[0] = seg;
		return ret;
	}
	,
	getLowStrokeSegments: function (position) {
		var matching = this.getMatchingBuckets(this.xAxis(), this._currentFrame._buckets, this.getBucketSize(this.view()), this.getFirstBucket(this.view()), this.toWorldPosition(position), true);
		if (matching == null) {
			return null;
		}
		var prevBucketValue = matching[0];
		var nextBucketValue = matching[1];
		var ret = new Array(1);
		var seg = new Array(2);
		seg[0] = { __x: prevBucketValue[0], __y: prevBucketValue[1], $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		seg[1] = { __x: nextBucketValue[0], __y: nextBucketValue[1], $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		ret[0] = seg;
		return ret;
	}
	,
	init: function () {
		this._columnToMapping = new $.ig.Dictionary$2($.ig.IFastItemColumn$1.prototype.$type.specialize(Number), String, 0);
		this._mappingToColumnName = new $.ig.Dictionary$2(String, String, 0);
		this.__ignoreColumnChanges = false;
		this.__dontRenderTypical = false;
		$.ig.Series.prototype.init.call(this);
		this._renderManager = new $.ig.CategorySeriesRenderManager();
		this.actualIsCustomCategoryStyleAllowed(false);
		this.sourceFramePreparer(new $.ig.CategoryTransitionSourceFramePreparer());
		this.thumbnailFrame(new $.ig.CategoryFrame(3));
		this.defaultStyleKey($.ig.FinancialSeries.prototype.$type);
	},
	negativeBrush: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.FinancialSeries.prototype.negativeBrushProperty, value);
			return value;
		} else {
			return this.getValue($.ig.FinancialSeries.prototype.negativeBrushProperty);
		}
	}
	,
	xAxis: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.FinancialSeries.prototype.xAxisProperty, value);
			return value;
		} else {
			return this.getValue($.ig.FinancialSeries.prototype.xAxisProperty);
		}
	}
	,
	yAxis: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.FinancialSeries.prototype.yAxisProperty, value);
			return value;
		} else {
			return this.getValue($.ig.FinancialSeries.prototype.yAxisProperty);
		}
	}
	,
	openMemberPath: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.FinancialSeries.prototype.openMemberPathProperty, value);
			return value;
		} else {
			return this.getValue($.ig.FinancialSeries.prototype.openMemberPathProperty);
		}
	}
	,
	openColumn: function (value) {
		if (arguments.length === 1) {
			if (this._openColumn != value) {
				var oldOpenColumn = this._openColumn;
				this._openColumn = value;
				this.raisePropertyChanged($.ig.FinancialSeries.prototype.openColumnPropertyName, oldOpenColumn, this._openColumn);
			}
			return value;
		} else {
			return this._openColumn;
		}
	}
	,
	_openColumn: null,
	highMemberPath: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.FinancialSeries.prototype.highMemberPathProperty, value);
			return value;
		} else {
			return this.getValue($.ig.FinancialSeries.prototype.highMemberPathProperty);
		}
	}
	,
	highColumn: function (value) {
		if (arguments.length === 1) {
			if (this._highColumn != value) {
				var oldHighColumn = this._highColumn;
				this._highColumn = value;
				this.raisePropertyChanged($.ig.FinancialSeries.prototype.highColumnPropertyName, oldHighColumn, this._highColumn);
			}
			return value;
		} else {
			return this._highColumn;
		}
	}
	,
	_highColumn: null,
	lowMemberPath: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.FinancialSeries.prototype.lowMemberPathProperty, value);
			return value;
		} else {
			return this.getValue($.ig.FinancialSeries.prototype.lowMemberPathProperty);
		}
	}
	,
	lowColumn: function (value) {
		if (arguments.length === 1) {
			if (this._lowColumn != value) {
				var oldLowColumn = this._lowColumn;
				this._lowColumn = value;
				this.raisePropertyChanged($.ig.FinancialSeries.prototype.lowColumnPropertyName, oldLowColumn, this._lowColumn);
			}
			return value;
		} else {
			return this._lowColumn;
		}
	}
	,
	_lowColumn: null,
	closeMemberPath: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.FinancialSeries.prototype.closeMemberPathProperty, value);
			return value;
		} else {
			return this.getValue($.ig.FinancialSeries.prototype.closeMemberPathProperty);
		}
	}
	,
	closeColumn: function (value) {
		if (arguments.length === 1) {
			if (this._closeColumn != value) {
				var oldCloseColumn = this._closeColumn;
				this._closeColumn = value;
				this.raisePropertyChanged($.ig.FinancialSeries.prototype.closeColumnPropertyName, oldCloseColumn, this._closeColumn);
			}
			return value;
		} else {
			return this._closeColumn;
		}
	}
	,
	_closeColumn: null,
	volumeMemberPath: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.FinancialSeries.prototype.volumeMemberPathProperty, value);
			return value;
		} else {
			return this.getValue($.ig.FinancialSeries.prototype.volumeMemberPathProperty);
		}
	}
	,
	volumeColumn: function (value) {
		if (arguments.length === 1) {
			if (this._volumeColumn != value) {
				var oldVolumeColumn = this._volumeColumn;
				this._volumeColumn = value;
				this.raisePropertyChanged($.ig.FinancialSeries.prototype.volumeColumnPropertyName, oldVolumeColumn, this._volumeColumn);
			}
			return value;
		} else {
			return this._volumeColumn;
		}
	}
	,
	_volumeColumn: null,
	isCustomCategoryStyleAllowed: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.FinancialSeries.prototype.isCustomCategoryStyleAllowedProperty, value);
			return value;
		} else {
			return this.getValue($.ig.FinancialSeries.prototype.isCustomCategoryStyleAllowedProperty);
		}
	}
	,
	_actualIsCustomCategoryStyleAllowed: false,
	actualIsCustomCategoryStyleAllowed: function (value) {
		if (arguments.length === 1) {
			this._actualIsCustomCategoryStyleAllowed = value;
			return value;
		} else {
			return this._actualIsCustomCategoryStyleAllowed;
		}
	}
	,
	assigningCategoryStyle: null,
	shouldOverrideCategoryStyle: function () {
		return (this.assigningCategoryStyle != null && this.actualIsCustomCategoryStyleAllowed()) || this.isHighlightingEnabled();
	}
	,
	raiseAssigningCategoryStyles: function (args) {
		if (this.assigningCategoryStyle != null && this.actualIsCustomCategoryStyleAllowed()) {
			this.assigningCategoryStyle(this, args);
		}
	}
	,
	_renderManager: null,
	performCategoryStyleOverride: function (buckets, currentIndex, valueCount, categoryAxis, axisParams, isThumbnail) {
		var isHighlightingEnabled = this.actualIsHighlightingEnabled();
		this._renderManager.prePerformCategoryStyleOverride(buckets, currentIndex, valueCount, categoryAxis, axisParams, isThumbnail, isHighlightingEnabled);
		var args = this._renderManager.categoryOverrideArgs();
		var info = null;
		var itemsSource = this.fastItemsSource();
		if (isHighlightingEnabled && this.seriesViewer() != null) {
			info = this.seriesViewer().highlightingManager().getHighlightingInfo(this, itemsSource, categoryAxis, args.startIndex(), args.endIndex(), false);
			args.highlightingInfo(info);
		}
		args.isThumbnail(isThumbnail);
		this.raiseAssigningCategoryStyles(args);
		this._renderManager.postPerformCategoryStyleOverride(info, isThumbnail, isHighlightingEnabled);
	}
	,
	getExactUnsortedItemIndex: function (world) {
		return this.getExactUnsortedItemIndexHelper(world, this.xAxis());
	}
	,
	getPreviousOrExactIndex: function (world, skipUnknowns) {
		return this.getPreviousOrExactIndexHelper(world, skipUnknowns, this.xAxis(), this.getExactUnsortedItemIndex.runOn(this), new $.ig.FinancialValueList(this.openColumn(), this.highColumn(), this.lowColumn(), this.closeColumn(), this.volumeColumn()));
	}
	,
	getNextOrExactIndex: function (world, skipUnknowns) {
		return this.getNextOrExactIndexHelper(world, skipUnknowns, this.xAxis(), this.getExactUnsortedItemIndex.runOn(this), new $.ig.FinancialValueList(this.openColumn(), this.highColumn(), this.lowColumn(), this.closeColumn(), this.volumeColumn()));
	}
	,
	getDistanceToIndex: function (world, index, axis, p, offset) {
		if (axis == null) {
			return Number.POSITIVE_INFINITY;
		}
		var count = this.xAxis()._cachedItemsCount;
		return this.getDistanceToIndexHelper(world, index, this.xAxis(), p, offset, count, this.getExactUnsortedItemIndex.runOn(this));
	}
	,
	getOffsetValue: function () {
		return this.getOffset(this.view().windowRect(), this.view().viewport(), this.getEffectiveViewport1(this.view()));
	}
	,
	getCategoryWidth: function () {
		return this.xAxis().getCategorySize(this.view().windowRect(), this.view().viewport(), this.getEffectiveViewport1(this.view()));
	}
	,
	getSeriesValuePosition: function (world, useInterpolation, skipUnknowns) {
		return this.getSeriesValuePositionHelper(world, useInterpolation, skipUnknowns, this.getOffset(this.view().windowRect(), this.view().viewport(), this.getEffectiveViewport1(this.view())), this.yAxis(), this.xAxis(), null, null, null);
	}
	,
	getSeriesValue: function (world, useInterpolation, skipUnknowns) {
		var effectiveViewportRect = this.getEffectiveViewport1(this.view());
		var offset = this.getOffset(this.view().windowRect(), this.view().viewport(), effectiveViewportRect);
		var xParams = new $.ig.ScalerParams(0, this.view().windowRect(), this.view().viewport(), this.xAxis().isInverted(), effectiveViewportRect);
		return this.getSeriesValueHelper(new $.ig.FinancialValueList(this.openColumn(), this.highColumn(), this.lowColumn(), this.closeColumn(), this.volumeColumn()), world, this.xAxis(), xParams, offset, this.getExactUnsortedItemIndex.runOn(this), useInterpolation, skipUnknowns);
	}
	,
	getSeriesHighValue: function (world, useInterpolation, skipUnknowns) {
		var effectiveViewportRect = this.getEffectiveViewport1(this.view());
		var offset = this.getOffset(this.view().windowRect(), this.view().viewport(), effectiveViewportRect);
		var xParams = new $.ig.ScalerParams(0, this.view().windowRect(), this.view().viewport(), this.xAxis().isInverted(), effectiveViewportRect);
		return this.getSeriesValueHelper(this.highColumn(), world, this.xAxis(), xParams, offset, this.getExactUnsortedItemIndex.runOn(this), useInterpolation, skipUnknowns);
	}
	,
	getSeriesLowValue: function (world, useInterpolation, skipUnknowns) {
		var effectiveViewportRect = this.getEffectiveViewport1(this.view());
		var offset = this.getOffset(this.view().windowRect(), this.view().viewport(), effectiveViewportRect);
		var xParams = new $.ig.ScalerParams(0, this.view().windowRect(), this.view().viewport(), this.xAxis().isInverted(), effectiveViewportRect);
		return this.getSeriesValueHelper(this.lowColumn(), world, this.xAxis(), xParams, offset, this.getExactUnsortedItemIndex.runOn(this), useInterpolation, skipUnknowns);
	}
	,
	getSeriesCloseValue: function (world, useInterpolation, skipUnknowns) {
		var effectiveViewportRect = this.getEffectiveViewport1(this.view());
		var offset = this.getOffset(this.view().windowRect(), this.view().viewport(), effectiveViewportRect);
		var xParams = new $.ig.ScalerParams(0, this.view().windowRect(), this.view().viewport(), this.xAxis().isInverted(), effectiveViewportRect);
		return this.getSeriesValueHelper(this.closeColumn(), world, this.xAxis(), xParams, offset, this.getExactUnsortedItemIndex.runOn(this), useInterpolation, skipUnknowns);
	}
	,
	getSeriesOpenValue: function (world, useInterpolation, skipUnknowns) {
		var effectiveViewportRect = this.getEffectiveViewport1(this.view());
		var offset = this.getOffset(this.view().windowRect(), this.view().viewport(), effectiveViewportRect);
		var xParams = new $.ig.ScalerParams(0, this.view().windowRect(), this.view().viewport(), this.xAxis().isInverted(), effectiveViewportRect);
		return this.getSeriesValueHelper(this.openColumn(), world, this.xAxis(), xParams, offset, this.getExactUnsortedItemIndex.runOn(this), useInterpolation, skipUnknowns);
	}
	,
	getSeriesVolumeValue: function (world, useInterpolation, skipUnknowns) {
		var effectiveViewportRect = this.getEffectiveViewport1(this.view());
		var offset = this.getOffset(this.view().windowRect(), this.view().viewport(), effectiveViewportRect);
		var xParams = new $.ig.ScalerParams(0, this.view().windowRect(), this.view().viewport(), this.xAxis().isInverted(), effectiveViewportRect);
		return this.getSeriesValueHelper(this.volumeColumn(), world, this.xAxis(), xParams, offset, this.getExactUnsortedItemIndex.runOn(this), useInterpolation, skipUnknowns);
	}
	,
	getSeriesHighValuePosition: function (world, useInterpolation, skipUnknowns) {
		var $self = this;
		return this.getSeriesValuePositionHelper(world, useInterpolation, skipUnknowns, this.getOffset(this.view().windowRect(), this.view().viewport(), this.getEffectiveViewport1(this.view())), this.yAxis(), this.xAxis(), this.getSeriesHighValue.runOn(this), function (w, skip) { return $self.getPreviousOrExactIndexHelper(w, skip, $self.xAxis(), $self.getExactUnsortedItemIndex.runOn($self), $self.highColumn()); }, function (w, skip) { return $self.getNextOrExactIndexHelper(w, skip, $self.xAxis(), $self.getExactUnsortedItemIndex.runOn($self), $self.highColumn()); });
	}
	,
	getSeriesLowValuePosition: function (world, useInterpolation, skipUnknowns) {
		var $self = this;
		return this.getSeriesValuePositionHelper(world, useInterpolation, skipUnknowns, this.getOffset(this.view().windowRect(), this.view().viewport(), this.getEffectiveViewport1(this.view())), this.yAxis(), this.xAxis(), this.getSeriesLowValue.runOn(this), function (w, skip) { return $self.getPreviousOrExactIndexHelper(w, skip, $self.xAxis(), $self.getExactUnsortedItemIndex.runOn($self), $self.lowColumn()); }, function (w, skip) { return $self.getNextOrExactIndexHelper(w, skip, $self.xAxis(), $self.getExactUnsortedItemIndex.runOn($self), $self.lowColumn()); });
	}
	,
	getSeriesOpenValuePosition: function (world, useInterpolation, skipUnknowns) {
		var $self = this;
		return this.getSeriesValuePositionHelper(world, useInterpolation, skipUnknowns, this.getOffset(this.view().windowRect(), this.view().viewport(), this.getEffectiveViewport1(this.view())), this.yAxis(), this.xAxis(), this.getSeriesOpenValue.runOn(this), function (w, skip) { return $self.getPreviousOrExactIndexHelper(w, skip, $self.xAxis(), $self.getExactUnsortedItemIndex.runOn($self), $self.openColumn()); }, function (w, skip) { return $self.getNextOrExactIndexHelper(w, skip, $self.xAxis(), $self.getExactUnsortedItemIndex.runOn($self), $self.openColumn()); });
	}
	,
	getSeriesCloseValuePosition: function (world, useInterpolation, skipUnknowns) {
		var $self = this;
		return this.getSeriesValuePositionHelper(world, useInterpolation, skipUnknowns, this.getOffset(this.view().windowRect(), this.view().viewport(), this.getEffectiveViewport1(this.view())), this.yAxis(), this.xAxis(), this.getSeriesCloseValue.runOn(this), function (w, skip) { return $self.getPreviousOrExactIndexHelper(w, skip, $self.xAxis(), $self.getExactUnsortedItemIndex.runOn($self), $self.closeColumn()); }, function (w, skip) { return $self.getNextOrExactIndexHelper(w, skip, $self.xAxis(), $self.getExactUnsortedItemIndex.runOn($self), $self.closeColumn()); });
	}
	,
	getSeriesVolumeValuePosition: function (world, useInterpolation, skipUnknowns) {
		var $self = this;
		return this.getSeriesValuePositionHelper(world, useInterpolation, skipUnknowns, this.getOffset(this.view().windowRect(), this.view().viewport(), this.getEffectiveViewport1(this.view())), this.yAxis(), this.xAxis(), this.getSeriesVolumeValue.runOn(this), function (w, skip) { return $self.getPreviousOrExactIndexHelper(w, skip, $self.xAxis(), $self.getExactUnsortedItemIndex.runOn($self), $self.volumeColumn()); }, function (w, skip) { return $self.getNextOrExactIndexHelper(w, skip, $self.xAxis(), $self.getExactUnsortedItemIndex.runOn($self), $self.volumeColumn()); });
	}
	,
	invalidateAxes: function () {
		$.ig.Series.prototype.invalidateAxes.call(this);
		if (this.xAxis() != null) {
			this.xAxis().renderAxis1(false);
		}
		if (this.yAxis() != null) {
			this.yAxis().renderAxis1(false);
		}
	}
	,
	windowRectChangedOverride: function (oldWindowRect, newWindowRect) {
		this.financialView().bucketCalculator().calculateBuckets(this.resolution());
		this.renderSeries(false);
	}
	,
	viewportRectChangedOverride: function (oldViewportRect, newViewportRect) {
		if (this.transitionInIsInProgress() && this.transitionProgress() < 0.05) {
			this.transitionInViable(true);
		}
		this.financialView().bucketCalculator().calculateBuckets(this.resolution());
		this.renderSeries(false);
	}
	,
	_columnToMapping: null,
	_mappingToColumnName: null,
	registerColumn: function (itemsSource, mapping, propertyName) {
		var column = this.registerDoubleColumn(mapping);
		this._columnToMapping.add(column, mapping);
		this._mappingToColumnName.add(mapping, propertyName);
		return column;
	}
	,
	deRegisterColumn: function (itemsSource, column) {
		if (column == null) {
			return;
		}
		itemsSource.deregisterColumn(column);
		var mapping = this._columnToMapping.item(column);
		this._mappingToColumnName.remove(mapping);
		this._columnToMapping.remove(column);
	}
	,
	__ignoreColumnChanges: false,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		$.ig.Series.prototype.propertyUpdatedOverride.call(this, sender, propertyName, oldValue, newValue);
		switch (propertyName) {
			case $.ig.Series.prototype.syncLinkPropertyName:
				if (this.syncLink() != null && this.seriesViewer() != null) {
					this.financialView().bucketCalculator().calculateBuckets(this.resolution());
					this.renderSeries(false);
				}
				break;
			case $.ig.Series.prototype.seriesViewerPropertyName:
				if (oldValue != null && newValue == null) {
					if (this.xAxis() != null) {
						this.xAxis().deregisterSeries(this);
					}
					if (this.yAxis() != null) {
						this.yAxis().deregisterSeries(this);
					}
				}
				if (oldValue == null && newValue != null) {
					if (this.xAxis() != null) {
						this.xAxis().registerSeries(this);
					}
					if (this.yAxis() != null) {
						this.yAxis().registerSeries(this);
					}
				}
				this.financialView().bucketCalculator().calculateBuckets(this.resolution());
				this.renderSeries(false);
				break;
			case $.ig.Series.prototype.fastItemsSourcePropertyName:
				this.__ignoreColumnChanges = true;
				if ($.ig.util.cast($.ig.IFastItemsSource.prototype.$type, oldValue) != null) {
					this.deRegisterColumn($.ig.util.cast($.ig.IFastItemsSource.prototype.$type, oldValue), this.openColumn());
					this.deRegisterColumn($.ig.util.cast($.ig.IFastItemsSource.prototype.$type, oldValue), this.highColumn());
					this.deRegisterColumn($.ig.util.cast($.ig.IFastItemsSource.prototype.$type, oldValue), this.lowColumn());
					this.deRegisterColumn($.ig.util.cast($.ig.IFastItemsSource.prototype.$type, oldValue), this.closeColumn());
					this.deRegisterColumn($.ig.util.cast($.ig.IFastItemsSource.prototype.$type, oldValue), this.volumeColumn());
					this.openColumn(null);
					this.highColumn(null);
					this.lowColumn(null);
					this.closeColumn(null);
					this.volumeColumn(null);
				}
				if ($.ig.util.cast($.ig.IFastItemsSource.prototype.$type, newValue) != null) {
					if (this.openMemberPath() != null) {
						this.openColumn(this.registerColumn($.ig.util.cast($.ig.IFastItemsSource.prototype.$type, newValue), this.openMemberPath(), $.ig.FinancialSeries.prototype.openColumnPropertyName));
					}
					if (this.highMemberPath() != null) {
						this.highColumn(this.registerColumn($.ig.util.cast($.ig.IFastItemsSource.prototype.$type, newValue), this.highMemberPath(), $.ig.FinancialSeries.prototype.highColumnPropertyName));
					}
					if (this.lowMemberPath() != null) {
						this.lowColumn(this.registerColumn($.ig.util.cast($.ig.IFastItemsSource.prototype.$type, newValue), this.lowMemberPath(), $.ig.FinancialSeries.prototype.lowColumnPropertyName));
					}
					if (this.closeMemberPath() != null) {
						this.closeColumn(this.registerColumn($.ig.util.cast($.ig.IFastItemsSource.prototype.$type, newValue), this.closeMemberPath(), $.ig.FinancialSeries.prototype.closeColumnPropertyName));
					}
					if (this.volumeMemberPath() != null) {
						this.volumeColumn(this.registerColumn($.ig.util.cast($.ig.IFastItemsSource.prototype.$type, newValue), this.volumeMemberPath(), $.ig.FinancialSeries.prototype.volumeColumnPropertyName));
					}
				}
				this.__ignoreColumnChanges = false;
				if (this.yAxis() != null && !this.yAxis().updateRange()) {
					this.financialView().bucketCalculator().calculateBuckets(this.resolution());
					this.renderSeries(false);
				}
				break;
			case $.ig.FinancialSeries.prototype.openMemberPathPropertyName:
				if (this.fastItemsSource() != null) {
					this.deRegisterColumn(this.fastItemsSource(), this.openColumn());
					this.openColumn(this.registerColumn(this.fastItemsSource(), this.openMemberPath(), $.ig.FinancialSeries.prototype.openColumnPropertyName));
				}
				break;
			case $.ig.FinancialSeries.prototype.openColumnPropertyName:
				if (this.yAxis() != null && !this.yAxis().updateRange() && !this.__ignoreColumnChanges) {
					this.financialView().bucketCalculator().calculateBuckets(this.resolution());
					this.renderSeries(false);
				}
				break;
			case $.ig.FinancialSeries.prototype.highMemberPathPropertyName:
				if (this.fastItemsSource() != null) {
					this.deRegisterColumn(this.fastItemsSource(), this.highColumn());
					this.highColumn(this.registerColumn(this.fastItemsSource(), this.highMemberPath(), $.ig.FinancialSeries.prototype.highColumnPropertyName));
				}
				break;
			case $.ig.FinancialSeries.prototype.highColumnPropertyName:
				if (this.yAxis() != null && !this.yAxis().updateRange() && !this.__ignoreColumnChanges) {
					this.financialView().bucketCalculator().calculateBuckets(this.resolution());
					this.renderSeries(false);
				}
				break;
			case $.ig.FinancialSeries.prototype.lowMemberPathPropertyName:
				if (this.fastItemsSource() != null) {
					this.deRegisterColumn(this.fastItemsSource(), this.lowColumn());
					this.lowColumn(this.registerColumn(this.fastItemsSource(), this.lowMemberPath(), $.ig.FinancialSeries.prototype.lowColumnPropertyName));
				}
				break;
			case $.ig.FinancialSeries.prototype.lowColumnPropertyName:
				if (this.yAxis() != null && !this.yAxis().updateRange() && !this.__ignoreColumnChanges) {
					this.financialView().bucketCalculator().calculateBuckets(this.resolution());
					this.renderSeries(false);
				}
				break;
			case $.ig.FinancialSeries.prototype.closeMemberPathPropertyName:
				if (this.fastItemsSource() != null) {
					this.deRegisterColumn(this.fastItemsSource(), this.closeColumn());
					this.closeColumn(this.registerColumn(this.fastItemsSource(), this.closeMemberPath(), $.ig.FinancialSeries.prototype.closeColumnPropertyName));
				}
				break;
			case $.ig.FinancialSeries.prototype.closeColumnPropertyName:
				if (this.yAxis() != null && !this.yAxis().updateRange() && !this.__ignoreColumnChanges) {
					this.financialView().bucketCalculator().calculateBuckets(this.resolution());
					this.renderSeries(false);
				}
				break;
			case $.ig.FinancialSeries.prototype.volumeMemberPathPropertyName:
				if (this.fastItemsSource() != null) {
					this.deRegisterColumn(this.fastItemsSource(), this.volumeColumn());
					this.volumeColumn(this.registerColumn(this.fastItemsSource(), this.volumeMemberPath(), $.ig.FinancialSeries.prototype.volumeColumnPropertyName));
				}
				break;
			case $.ig.FinancialSeries.prototype.volumeColumnPropertyName:
				if (this.yAxis() != null && !this.yAxis().updateRange() && !this.__ignoreColumnChanges) {
					this.financialView().bucketCalculator().calculateBuckets(this.resolution());
					this.renderSeries(false);
				}
				break;
			case $.ig.FinancialSeries.prototype.xAxisPropertyName:
				if (oldValue != null) {
					(oldValue).deregisterSeries(this);
				}
				if (newValue != null) {
					(newValue).registerSeries(this);
				}
				this.financialView().bucketCalculator().calculateBuckets(this.resolution());
				this.renderSeries(false);
				this.notifyThumbnailAppearanceChanged();
				break;
			case $.ig.FinancialSeries.prototype.yAxisPropertyName:
				if (oldValue != null) {
					(oldValue).deregisterSeries(this);
				}
				if (newValue != null) {
					(newValue).registerSeries(this);
				}
				this.financialView().bucketCalculator().calculateBuckets(this.resolution());
				if (this.yAxis() != null) {
					this.yAxis().updateRange();
				}
				{
					this.renderSeries(false);
				}
				this.notifyThumbnailAppearanceChanged();
				break;
			case $.ig.Series.prototype.transitionProgressPropertyName:
				this._transitionFrame.interpolate3(this.transitionProgress(), this._previousFrame, this._currentFrame);
				if (this.clearAndAbortIfInvalid1(this.view())) {
					return;
				}
				if (this.transitionProgress() == 1) {
					this._currentFrame.clearSpeedModifiers();
					this.renderFrame(this._currentFrame, this.financialView());
					if (this.transitionInIsInProgress()) {
						this.transitionInIsInProgress(false);
						this.clearSpeedModifiers();
						this.animator().intervalMilliseconds($.ig.truncate(this.getTotalMilliseconds()));
						this.animator().easingFunction(this.transitionEasingFunction());
					}
				} else {
					this.renderFrame(this._transitionFrame, this.financialView());
				}
				break;
			case $.ig.FinancialSeries.prototype.negativeBrushPropertyName:
				this.renderSeries(false);
				break;
			case $.ig.CategorySeries.prototype.isCustomCategoryStyleAllowedPropertyName:
				this.actualIsCustomCategoryStyleAllowed(this.getIsCustomCategoryStyleAllowed());
				this.renderSeries(false);
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
		if ($.ig.util.cast($.ig.CategoryXAxis.prototype.$type, axis) !== null || $.ig.util.cast($.ig.CategoryDateTimeXAxis.prototype.$type, axis) !== null) {
			return true;
		}
		return false;
	}
	,
	getCategoryItemsHelper: function (orderedStartIndex, orderedEndIndex, axis) {
		if (orderedEndIndex < orderedStartIndex || orderedStartIndex < 0 || orderedStartIndex > this.fastItemsSource().count() || orderedEndIndex < 0 || orderedEndIndex > this.fastItemsSource().count() || axis == null) {
			return null;
		}
		var ret = new Array((orderedEndIndex - orderedStartIndex) + 1);
		var isSorting = axis.isSorting();
		var sortedIndices = null;
		if (isSorting) {
			sortedIndices = (axis).sortedIndices();
		}
		for (var i = orderedStartIndex; i <= orderedEndIndex; i++) {
			var ind = i;
			if (isSorting) {
				ind = sortedIndices.__inner[ind];
			}
			ret[i - orderedStartIndex] = this.fastItemsSource().item(i);
		}
		return ret;
	}
	,
	getBucketSize: function (view) {
		return (view).bucketCalculator().bucketSize();
	}
	,
	getFirstBucket: function (view) {
		return (view).bucketCalculator().firstBucket();
	}
	,
	getCategoryItems: function (orderedStartIndex, orderedEndIndex) {
		return this.getCategoryItemsHelper(orderedStartIndex, orderedEndIndex, this.yAxis());
	}
	,
	_prevHighlightingInfo: null,
	prevHighlightingInfo: function (value) {
		if (arguments.length === 1) {
			this._prevHighlightingInfo = value;
			return value;
		} else {
			return this._prevHighlightingInfo;
		}
	}
	,
	hasIndividualElements: function () {
		return false;
	}
	,
	isHighlightingSupported: function () {
		return true;
	}
	,
	getHighlightingInfo: function (item, world) {
		if (this.hasIndividualElements()) {
			var info = this.getSpecificHighlightingInfo(item, world, this.prevHighlightingInfo());
			this.prevHighlightingInfo(info);
			return info;
		} else {
			var info1 = this.getFullSeriesInfo(item, world, this.prevHighlightingInfo());
			this.prevHighlightingInfo(info1);
			return info1;
		}
	}
	,
	getSortingCategoryBucketIndex: function (world) {
		var current = this._currentFrame;
		if (this.animationActive()) {
			current = this._transitionFrame;
		}
		var viewportRect = this.view().viewport();
		var windowRect = this.view().windowRect();
		var indexAxis = this.xAxis();
		var screenPos = 0;
		if (indexAxis.isVertical()) {
			var windowY = (world.__y - windowRect.top()) / windowRect.height();
			screenPos = windowY * viewportRect.height() + viewportRect.top();
		} else {
			var windowX = (world.__x - windowRect.left()) / windowRect.width();
			screenPos = windowX * viewportRect.width() + viewportRect.left();
		}
		var count = current._buckets.count();
		var i = 0;
		for (i = 0; i < count; i++) {
			var bucket = current._buckets.__inner[i];
			var nextBucket = null;
			if (i < count - 1) {
				nextBucket = current._buckets.__inner[i + 1];
			}
			if (bucket[0] <= screenPos && nextBucket == null || nextBucket[0] >= screenPos) {
				if (nextBucket != null) {
					if (Math.abs(bucket[0] - screenPos) < Math.abs(nextBucket[0] - screenPos)) {
						return i;
					} else {
						return i + 1;
					}
				} else {
					return i;
				}
			}
		}
		return i;
	}
	,
	getSpecificHighlightingInfo: function (item, world, prevValue) {
		var count = this.fastItemsSource().count();
		var firstBucket = this.financialView().bucketCalculator().firstBucket();
		var lastBucket = this.financialView().bucketCalculator().lastBucket();
		var bucketSize = this.financialView().bucketCalculator().bucketSize();
		var axis = this.xAxis();
		var bucketStart = -1;
		var bucketEnd = -1;
		if (axis.isSorting()) {
			var current = this._currentFrame;
			if (this.animationActive()) {
				current = this._transitionFrame;
			}
			var bucket = this.getSortingCategoryBucketIndex(world);
			bucketStart = bucket;
			bucketEnd = bucket;
		} else {
			var index = this.getItemIndex(world);
			bucketStart = $.ig.truncate(Math.floor(($.ig.intDivide(index, bucketSize)))) * bucketSize;
			bucketEnd = bucketStart + (bucketSize - 1);
		}
		if (prevValue != null && prevValue.startIndex() == bucketStart && prevValue.endIndex() == bucketEnd) {
			return prevValue;
		}
		var info = new $.ig.HighlightingInfo();
		info.series(this);
		info.startIndex(bucketStart);
		info.endIndex(bucketEnd);
		return info;
	}
	,
	getFullSeriesInfo: function (item, world, prevValue) {
		var info = new $.ig.HighlightingInfo();
		info.series(this);
		info.startIndex(0);
		info.endIndex(this.fastItemsSource().count() - 1);
		if (prevValue != null && prevValue.startIndex() == info.startIndex() && prevValue.endIndex() == info.endIndex()) {
			return prevValue;
		}
		return info;
	}
	,
	getIsCustomCategoryStyleAllowed: function () {
		return this.isCustomCategoryStyleAllowed();
	}
	,
	getExactItemIndex: function (world) {
		var windowRect = this.view().windowRect();
		var viewportRect = this.view().viewport();
		var rowIndex = -1;
		if (!windowRect.isEmpty() && !viewportRect.isEmpty() & this.xAxis() != null) {
			var effectiveViewportRect = this.getEffectiveViewport1(this.view());
			var p = new $.ig.ScalerParams(0, windowRect, viewportRect, this.xAxis().isInverted(), effectiveViewportRect);
			var contentViewport = this.getContentViewport(this.view());
			var left = this.xAxis().getUnscaledValue(contentViewport.left(), p);
			var right = this.xAxis().getUnscaledValue(contentViewport.right(), p);
			var windowX = (world.__x - windowRect.left()) / windowRect.width();
			var bucket = left + (windowX * (right - left));
			if (this.xAxis().categoryMode() != $.ig.CategoryMode.prototype.mode0) {
				bucket -= 0.5;
			}
			rowIndex = bucket;
		}
		return rowIndex;
	}
	,
	getItemIndex: function (world) {
		return $.ig.truncate(Math.round(this.getExactItemIndex(world)));
	}
	,
	getItemIndexSorted: function (world) {
		var windowRect = this.view().windowRect();
		var viewportRect = this.view().viewport();
		var effectiveViewportRect = this.getEffectiveViewport1(this.view());
		if (windowRect.isEmpty() || viewportRect.isEmpty()) {
			return -1;
		}
		var xParams = new $.ig.ScalerParams(0, windowRect, viewportRect, this.xAxis().isInverted(), effectiveViewportRect);
		var sorting = $.ig.util.cast($.ig.ISortingAxis.prototype.$type, this.xAxis());
		var contentViewport = this.getContentViewport(this.view());
		var left = this.xAxis().getUnscaledValue(contentViewport.left(), xParams);
		var right = this.xAxis().getUnscaledValue(contentViewport.right(), xParams);
		var windowX = (world.__x - windowRect.left()) / windowRect.width();
		var axisValue = left + ((right - left) * windowX);
		var itemIndex = sorting.getIndexClosestToUnscaledValue(axisValue);
		return itemIndex;
	}
	,
	getItem: function (world) {
		var index = 0;
		if ($.ig.util.cast($.ig.ISortingAxis.prototype.$type, this.xAxis()) !== null) {
			index = this.getItemIndexSorted(world);
			if (index == -1) {
				return null;
			}
		} else {
			index = this.getItemIndex(world);
		}
		return index >= 0 && this.fastItemsSource() != null && index < this.fastItemsSource().count() ? this.fastItemsSource().item(index) : null;
	}
	,
	_previousFrame: null,
	_transitionFrame: null,
	_currentFrame: null,
	getOffset: function (windowRect, viewportRect, effectiveViewportRect) {
		var offset = 0;
		if (this.xAxis() == null) {
			return offset;
		}
		var categoryMode = this.xAxis().categoryMode();
		switch (categoryMode) {
			case $.ig.CategoryMode.prototype.mode0:
				offset = 0;
				break;
			case $.ig.CategoryMode.prototype.mode1:
				offset = 0.5 * this.xAxis().getCategorySize(windowRect, viewportRect, effectiveViewportRect);
				break;
			case $.ig.CategoryMode.prototype.mode2:
				offset = this.xAxis().getGroupCenter(this.index(), windowRect, viewportRect, effectiveViewportRect);
				break;
		}
		if (this.xAxis().isInverted()) {
			offset = -offset;
		}
		return offset;
	}
	,
	transitionInMode: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.FinancialSeries.prototype.transitionInModeProperty, $.ig.CategoryTransitionInMode.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.FinancialSeries.prototype.transitionInModeProperty));
		}
	}
	,
	isTransitionInEnabled: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.FinancialSeries.prototype.isTransitionInEnabledProperty, value);
			return value;
		} else {
			return this.getValue($.ig.FinancialSeries.prototype.isTransitionInEnabledProperty);
		}
	}
	,
	shouldTransitionIn: function () {
		return this.isTransitionInEnabled();
	}
	,
	getDefaultTransitionInMode: function () {
		return $.ig.CategoryTransitionInMode.prototype.expand;
	}
	,
	getDefaultTransitionInSpeedType: function () {
		return $.ig.TransitionInSpeedType.prototype.indexScaled;
	}
	,
	_transitionInIsInProgress: false,
	transitionInIsInProgress: function (value) {
		if (arguments.length === 1) {
			this._transitionInIsInProgress = value;
			return value;
		} else {
			return this._transitionInIsInProgress;
		}
	}
	,
	renderFrame: function (frame, view) {
		this.customClipRect(frame.customClip());
		view.onRenderFrame();
	}
	,
	__fullClip: null,
	fullClip: function () {
		return this.__fullClip;
	}
	,
	prepareFrame: function (frame, view) {
		frame.clearFrame();
	}
	,
	validateSeries: function (viewportRect, windowRect, view) {
		var isValid = $.ig.Series.prototype.validateSeries.call(this, viewportRect, windowRect, view);
		var financialView = view;
		if (this.fastItemsSource() == null || this.fastItemsSource().count() == 0 || !view.hasSurface() || windowRect.isEmpty() || viewportRect.isEmpty() || this.xAxis() == null || this.yAxis() == null || financialView.bucketCalculator().bucketSize() < 1 || this.xAxis().seriesViewer() == null || this.yAxis().seriesViewer() == null || this.yAxis().actualMinimumValue() == this.yAxis().actualMaximumValue() || this.xAxis().itemsSource() == null || this.xAxis()._cachedItemsCount < 1) {
			financialView.bucketCalculator().bucketSize(0);
			isValid = false;
		}
		return isValid;
	}
	,
	getViewInfo: function (viewportRect, windowRect) {
		windowRect = this.view().windowRect();
		viewportRect = this.view().viewport();
		return {
			p0: viewportRect,
			p1: windowRect
		};
	}
	,
	_sourceFramePreparer: null,
	sourceFramePreparer: function (value) {
		if (arguments.length === 1) {
			this._sourceFramePreparer = value;
			return value;
		} else {
			return this._sourceFramePreparer;
		}
	}
	,
	getWorldZeroValue: function (view) {
		var value = 0;
		var windowRect = view.windowRect();
		var viewportRect = view.viewport();
		var effectiveViewportRect = this.getEffectiveViewport1(this.view());
		if (!windowRect.isEmpty() && !viewportRect.isEmpty() && this.yAxis() != null) {
			var yParams = new $.ig.ScalerParams(0, windowRect, viewportRect, this.yAxis().isInverted(), effectiveViewportRect);
			value = this.yAxis().getScaledValue(this.yAxis().referenceValue(), yParams);
		}
		return value;
	}
	,
	clearSpeedModifiers: function () {
		$.ig.Series.prototype.clearSpeedModifiers.call(this);
		this._previousFrame.clearSpeedModifiers();
		this._currentFrame.clearSpeedModifiers();
		this._transitionFrame.clearSpeedModifiers();
	}
	,
	renderSeriesOverride: function (animate) {
		var $self = this;
		$.ig.Series.prototype.renderSeriesOverride.call(this, animate);
		this.financialView().bucketCalculator().calculateBuckets(this.resolution());
		if (this.clearAndAbortIfInvalid1(this.view())) {
			return;
		}
		if (this.skipPrepare()) {
			if (this.animationActive()) {
				this.renderFrame(this._transitionFrame, this.financialView());
			} else {
				this.renderFrame(this._currentFrame, this.financialView());
			}
			return;
		}
		if (this.shouldAnimate(animate)) {
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
			this.prepareFrame(this._currentFrame, this.financialView());
			if (this.transitionInViable()) {
				this.animator().stop();
				this.animator().intervalMilliseconds($.ig.truncate(this.getTotalTransitionInMilliseconds()));
				this.animator().easingFunction(this.transitionInEasingFunction() != null ? this.transitionInEasingFunction() : this.transitionEasingFunction());
				this.sourceFramePreparer().prepareSourceFrame(this._previousFrame, this._currentFrame, this.isVertical(), this.xAxis(), this.yAxis(), this.transitionInMode(), this.getDefaultTransitionInMode(), this.transitionInSpeedType(), this.getDefaultTransitionInSpeedType(), function () { return $self.getWorldZeroValue($self.financialView()); }, this.financialView().viewport());
			}
			this.checkTransitionInterrupted();
			this.startAnimation();
			if (this.transitionInViable()) {
				this.transitionInViable(false);
				this.transitionInIsInProgress(true);
			}
		} else {
			this.prepareFrame(this._currentFrame, this.financialView());
			this.renderFrame(this._currentFrame, this.financialView());
		}
	}
	,
	typical: null,
	typicalBasedOn: null,
	validateBasedOn: function (basedOn) {
		var en = basedOn.getEnumerator();
		while (en.moveNext()) {
			var col = en.current();
			switch (col) {
				case "HighColumn":
					if (this.highColumn() == null) {
						return false;
					}
					break;
				case "LowColumn":
					if (this.lowColumn() == null) {
						return false;
					}
					break;
				case "OpenColumn":
					if (this.openColumn() == null) {
						return false;
					}
					break;
				case "CloseColumn":
					if (this.closeColumn() == null) {
						return false;
					}
					break;
				case "VolumeColumn":
					if (this.volumeColumn() == null) {
						return false;
					}
					break;
			}
		}
		return true;
	}
	,
	xAxisSortRequired: function () {
		return this.xAxis() != null && $.ig.util.cast($.ig.ISortingAxis.prototype.$type, this.xAxis()) !== null;
	}
	,
	hasTypical: function () {
		return this.typical != null;
	}
	,
	callTypical: function (args) {
		this.typical(this, args);
	}
	,
	typicalColumn: function () {
		var d__ = new $.ig.FinancialSeries___get_TypicalColumn__IteratorClass(-2);
		d__.__4__this = this;
		return d__;
	}
	,
	tR: function () {
		var d__ = new $.ig.FinancialSeries___get_TR__IteratorClass(-2);
		d__.__4__this = this;
		return d__;
	}
	,
	tL: function () {
		var d__ = new $.ig.FinancialSeries___get_TL__IteratorClass(-2);
		d__.__4__this = this;
		return d__;
	}
	,
	makeReadOnlyAndEnsureSorted: function (column) {
		if (column == null) {
			return null;
		}
		if (this.xAxisSortRequired() && (this.xAxis()).sortedIndices() != null) {
			return new $.ig.SafeSortedReadOnlyDoubleCollection(0, column, (this.xAxis()).sortedIndices());
		}
		return new $.ig.SafeReadOnlyDoubleCollection(0, column);
	}
	,
	__dontRenderTypical: false,
	getTypicalBasedOn: function () {
		var ret = new $.ig.List$1(String, 0);
		ret.add("HighColumn");
		ret.add("LowColumn");
		ret.add("CloseColumn");
		if (this.typicalBasedOn != null && this.fastItemsSource() != null && !this.__dontRenderTypical) {
			this.__dontRenderTypical = true;
			var dataSource = this.provideDataSource(0, this.fastItemsSource().count());
			this.__dontRenderTypical = false;
			var args = new $.ig.FinancialEventArgs(0, this.fastItemsSource().count(), dataSource, this.provideSupportingCalculations(dataSource));
			this.typicalBasedOn(this, args);
			if (args.basedOn() != null && args.basedOn().count() > 0) {
				return args.basedOn();
			}
		}
		return ret;
	}
	,
	__openArray: null,
	getOpenColumnAsArray: function () {
		if (this.openColumn() == null) {
			return null;
		}
		if (this.xAxis() != null && $.ig.util.cast($.ig.ISortingAxis.prototype.$type, this.xAxis()) !== null) {
			if (this.__openArray != null) {
				return this.__openArray.asArray();
			}
			var _arr = new $.ig.List$1(Number, 2, this.openColumn().count());
			var indices = (this.xAxis()).sortedIndices();
			var _colArr = this.openColumn().asArray();
			for (var i = 0; i < indices.count(); i++) {
				_arr.add(_colArr[indices.__inner[i]]);
			}
			this.__openArray = _arr;
			return this.__openArray.asArray();
		} else {
			return this.openColumn().asArray();
		}
	}
	,
	__highArray: null,
	getHighColumnAsArray: function () {
		if (this.highColumn() == null) {
			return null;
		}
		if (this.xAxis() != null && $.ig.util.cast($.ig.ISortingAxis.prototype.$type, this.xAxis()) !== null) {
			if (this.__highArray != null) {
				return this.__highArray.asArray();
			}
			var _arr = new $.ig.List$1(Number, 2, this.highColumn().count());
			var indices = (this.xAxis()).sortedIndices();
			var _colArr = this.highColumn().asArray();
			for (var i = 0; i < indices.count(); i++) {
				_arr.add(_colArr[indices.__inner[i]]);
			}
			this.__highArray = _arr;
			return this.__highArray.asArray();
		} else {
			return this.highColumn().asArray();
		}
	}
	,
	__lowArray: null,
	getLowColumnAsArray: function () {
		if (this.lowColumn() == null) {
			return null;
		}
		if (this.xAxis() != null && $.ig.util.cast($.ig.ISortingAxis.prototype.$type, this.xAxis()) !== null) {
			if (this.__lowArray != null) {
				return this.__lowArray.asArray();
			}
			var _arr = new $.ig.List$1(Number, 2, this.lowColumn().count());
			var indices = (this.xAxis()).sortedIndices();
			var _colArr = this.lowColumn().asArray();
			for (var i = 0; i < indices.count(); i++) {
				_arr.add(_colArr[indices.__inner[i]]);
			}
			this.__lowArray = _arr;
			return this.__lowArray.asArray();
		} else {
			return this.lowColumn().asArray();
		}
	}
	,
	__closeArray: null,
	getCloseColumnAsArray: function () {
		if (this.closeColumn() == null) {
			return null;
		}
		if (this.xAxis() != null && $.ig.util.cast($.ig.ISortingAxis.prototype.$type, this.xAxis()) !== null) {
			if (this.__closeArray != null) {
				return this.__closeArray.asArray();
			}
			var _arr = new $.ig.List$1(Number, 2, this.closeColumn().count());
			var indices = (this.xAxis()).sortedIndices();
			var _colArr = this.closeColumn().asArray();
			for (var i = 0; i < indices.count(); i++) {
				_arr.add(_colArr[indices.__inner[i]]);
			}
			this.__closeArray = _arr;
			return this.__closeArray.asArray();
		} else {
			return this.closeColumn().asArray();
		}
	}
	,
	__volumeArray: null,
	getVolumeColumnAsArray: function () {
		if (this.volumeColumn() == null) {
			return null;
		}
		if (this.xAxis() != null && $.ig.util.cast($.ig.ISortingAxis.prototype.$type, this.xAxis()) !== null) {
			if (this.__volumeArray != null) {
				return this.__volumeArray.asArray();
			}
			var _arr = new $.ig.List$1(Number, 2, this.volumeColumn().count());
			var indices = (this.xAxis()).sortedIndices();
			var _colArr = this.volumeColumn().asArray();
			for (var i = 0; i < indices.count(); i++) {
				_arr.add(_colArr[indices.__inner[i]]);
			}
			this.__volumeArray = _arr;
			return this.__volumeArray.asArray();
		} else {
			return this.volumeColumn().asArray();
		}
	}
	,
	dataUpdatedOverride: function (action, position, count, propertyName) {
		$.ig.Series.prototype.dataUpdatedOverride.call(this, action, position, count, propertyName);
		if (this.xAxis() == null || !($.ig.util.cast($.ig.ISortingAxis.prototype.$type, this.xAxis()) !== null)) {
			return;
		}
		var indices = (this.xAxis()).sortedIndices();
		switch (action) {
			case $.ig.FastItemsSourceEventAction.prototype.insert:
				for (var i = position; i < count; i++) {
					var index = indices.__inner[i];
					if (this.__openArray != null) {
						this.__openArray.insert(i, this.openColumn().item(index));
					}
					if (this.__highArray != null) {
						this.__highArray.insert(i, this.highColumn().item(index));
					}
					if (this.__lowArray != null) {
						this.__lowArray.insert(i, this.lowColumn().item(index));
					}
					if (this.__closeArray != null) {
						this.__closeArray.insert(i, this.closeColumn().item(index));
					}
					if (this.__volumeArray != null) {
						this.__volumeArray.insert(i, this.volumeColumn().item(index));
					}
				}
				break;
			case $.ig.FastItemsSourceEventAction.prototype.remove:
				if (this.__openArray != null) {
					this.__openArray.removeRange(position, count);
				}
				if (this.__highArray != null) {
					this.__highArray.removeRange(position, count);
				}
				if (this.__lowArray != null) {
					this.__lowArray.removeRange(position, count);
				}
				if (this.__closeArray != null) {
					this.__closeArray.removeRange(position, count);
				}
				if (this.__volumeArray != null) {
					this.__volumeArray.removeRange(position, count);
				}
				break;
			case $.ig.FastItemsSourceEventAction.prototype.reset:
				if (this.__openArray != null) {
					this.__openArray = new $.ig.List$1(Number, 0);
				}
				if (this.__highArray != null) {
					this.__highArray = new $.ig.List$1(Number, 0);
				}
				if (this.__lowArray != null) {
					this.__lowArray = new $.ig.List$1(Number, 0);
				}
				if (this.__closeArray != null) {
					this.__closeArray = new $.ig.List$1(Number, 0);
				}
				if (this.__volumeArray != null) {
					this.__volumeArray = new $.ig.List$1(Number, 0);
				}
				break;
			case $.ig.FastItemsSourceEventAction.prototype.change:
			case $.ig.FastItemsSourceEventAction.prototype.replace:
				for (var i1 = position; i1 < count; i1++) {
					var index1 = indices.__inner[i1];
					if (this.__openArray != null) {
						this.__openArray.__inner[i1] = this.openColumn().item(index1);
					}
					if (this.__highArray != null) {
						this.__highArray.__inner[i1] = this.highColumn().item(index1);
					}
					if (this.__lowArray != null) {
						this.__lowArray.__inner[i1] = this.lowColumn().item(index1);
					}
					if (this.__closeArray != null) {
						this.__closeArray.__inner[i1] = this.closeColumn().item(index1);
					}
					if (this.__volumeArray != null) {
						this.__volumeArray.__inner[i1] = this.volumeColumn().item(index1);
					}
				}
				break;
		}
	}
	,
	provideDataSource: function (position, count) {
		var $self = this;
		var readOnlyOpenColumn = this.makeReadOnlyAndEnsureSorted(this.openColumn());
		var readOnlyCloseColumn = this.makeReadOnlyAndEnsureSorted(this.closeColumn());
		var readOnlyHighColumn = this.makeReadOnlyAndEnsureSorted(this.highColumn());
		var readOnlyLowColumn = this.makeReadOnlyAndEnsureSorted(this.lowColumn());
		var readOnlyVolumeColumn = this.makeReadOnlyAndEnsureSorted(this.volumeColumn());
		var dataSource = (function () {
			var $ret = new $.ig.FinancialCalculationDataSource();
			$ret.typicalColumn(new $.ig.CalculatedColumn(1, new $.ig.SafeEnumerable($self.typicalColumn()), $self.getTypicalBasedOn()));
			$ret.trueRange(new $.ig.CalculatedColumn(1, new $.ig.SafeEnumerable($self.tR()), (function () {
				var $ret = new $.ig.List$1(String, 0);
				$ret.add("HighColumn");
				$ret.add("LowColumn");
				$ret.add("CloseColumn");
				return $ret;
			}())));
			$ret.trueLow(new $.ig.CalculatedColumn(1, new $.ig.SafeEnumerable($self.tL()), (function () {
				var $ret = new $.ig.List$1(String, 0);
				$ret.add("LowColumn");
				$ret.add("CloseColumn");
				return $ret;
			}())));
			$ret.openColumn(readOnlyOpenColumn);
			$ret.closeColumn(readOnlyCloseColumn);
			$ret.highColumn(readOnlyHighColumn);
			$ret.lowColumn(readOnlyLowColumn);
			$ret.volumeColumn(readOnlyVolumeColumn);
			$ret.calculateFrom(position);
			$ret.calculateCount(count);
			$ret.minimumValue(NaN);
			$ret.maximumValue(NaN);
			$ret.count($self.fastItemsSource() != null ? $self.fastItemsSource().count() : 0);
			return $ret;
		}());
		return dataSource;
	}
	,
	makeSafe: function (value) {
		if (Number.isInfinity(value) || $.ig.util.isNaN(value)) {
			return 0;
		}
		return value;
	}
	,
	provideSupportingCalculations: function (dataSource) {
		var $self = this;
		return (function () {
			var $ret = new $.ig.FinancialCalculationSupportingCalculations();
			$ret.eMA(new $.ig.ColumnSupportingCalculation(1, $.ig.Series.prototype.eMA, new $.ig.List$1(String, 0)));
			$ret.sMA(new $.ig.ColumnSupportingCalculation(1, $.ig.Series.prototype.sMA, new $.ig.List$1(String, 0)));
			$ret.sTDEV(new $.ig.ColumnSupportingCalculation(1, $.ig.Series.prototype.sTDEV, new $.ig.List$1(String, 0)));
			$ret.movingSum(new $.ig.ColumnSupportingCalculation(1, $.ig.Series.prototype.movingSum, new $.ig.List$1(String, 0)));
			$ret.shortVolumeOscillatorAverage(new $.ig.DataSourceSupportingCalculation(1, function (ds1) { return $.ig.Series.prototype.eMA(ds1.volumeColumn(), ds1.shortPeriod()); }, (function () {
				var $ret = new $.ig.List$1(String, 0);
				$ret.add($.ig.FinancialSeries.prototype.volumeColumnPropertyName);
				return $ret;
			}())));
			$ret.longVolumeOscillatorAverage(new $.ig.DataSourceSupportingCalculation(1, function (ds2) { return $.ig.Series.prototype.eMA(ds2.volumeColumn(), ds2.longPeriod()); }, (function () {
				var $ret = new $.ig.List$1(String, 0);
				$ret.add($.ig.FinancialSeries.prototype.volumeColumnPropertyName);
				return $ret;
			}())));
			$ret.shortPriceOscillatorAverage(new $.ig.DataSourceSupportingCalculation(1, function (ds3) { return $.ig.Series.prototype.eMA(ds3.typicalColumn(), ds3.shortPeriod()); }, dataSource.typicalColumn().basedOn()));
			$ret.longPriceOscillatorAverage(new $.ig.DataSourceSupportingCalculation(1, function (ds4) { return $.ig.Series.prototype.eMA(ds4.typicalColumn(), ds4.longPeriod()); }, dataSource.typicalColumn().basedOn()));
			$ret.toEnumerableRange($.ig.Series.prototype.toEnumerableRange);
			$ret.toEnumerable($.ig.Series.prototype.toEnumerable);
			$ret.makeSafe($self.makeSafe.runOn($self));
			return $ret;
		}());
	}
	,
	categoryAxis: function () {
		return this.xAxis();
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
		$.ig.Series.prototype.renderThumbnail.call(this, viewportRect, surface);
		if (!this.thumbnailDirty()) {
			this.view().prepSurface(surface);
			return;
		}
		var thumbnailView = $.ig.util.cast($.ig.FinancialSeriesView.prototype.$type, this.thumbnailView());
		this.view().prepSurface(surface);
		thumbnailView.bucketCalculator().calculateBuckets(this.resolution());
		if (this.clearAndAbortIfInvalid1(this.thumbnailView())) {
			return;
		}
		if (!this.skipThumbnailPrepare()) {
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
		$.ig.Series.prototype.renderAlternateView.call(this, viewportRect, windowRect, surface, viewIdentifier, effectiveScalingRatio);
		var view = this.alternateViews().item(viewIdentifier);
		var financialSeriesView = view;
		financialSeriesView.bucketCalculator().calculateBuckets(this.resolution());
		view.prepAltSurface(surface);
		if (this.clearAndAbortIfInvalid1(financialSeriesView)) {
			return;
		}
		if (this.alternateFrame() == null) {
			this.alternateFrame(new $.ig.CategoryFrame(3));
		}
		this.alternateFrame().clearFrame();
		this.prepareFrame(this.alternateFrame(), financialSeriesView);
		this.renderFrame(this.alternateFrame(), financialSeriesView);
	}
	,
	getMatchingAxis: function (axis) {
		if (this.xAxis() == axis) {
			return this.yAxis();
		}
		if (this.yAxis() == axis) {
			return this.xAxis();
		}
		return null;
	}
	,
	$type: new $.ig.Type('FinancialSeries', $.ig.Series.prototype.$type, [$.ig.IHasCategoryAxis.prototype.$type])
}, true);

$.ig.util.defType('FinancialIndicator', 'FinancialSeries', {
	createView: function () {
		return new $.ig.FinancialIndicatorView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.FinancialSeries.prototype.onViewCreated.call(this, view);
		this.indicatorView(view);
	}
	,
	_indicatorView: null,
	indicatorView: function (value) {
		if (arguments.length === 1) {
			this._indicatorView = value;
			return value;
		} else {
			return this._indicatorView;
		}
	}
	,
	init: function () {
		$.ig.FinancialSeries.prototype.init.call(this);
		this._previousFrame = new $.ig.CategoryFrame(3);
		this._transitionFrame = new $.ig.CategoryFrame(3);
		this._currentFrame = new $.ig.CategoryFrame(3);
		this.indicatorColumn(new $.ig.List$1(Number, 0));
		this.indicatorRange(new $.ig.AxisRange(-100, 100));
	},
	ensureYRangeThenRender: function (animate) {
		if (this.yAxis() != null && !this.yAxis().updateRange()) {
			this.renderSeries(true);
		}
	}
	,
	testAreaOver: function (position, snapPosition, isFinger) {
		if ($.ig.util.isNaN(snapPosition.__x) || $.ig.util.isNaN(snapPosition.__y)) {
			return false;
		}
		var valueAxis = this.yAxis();
		var referencePosition = 0;
		if (valueAxis != null) {
			referencePosition = valueAxis.scaleValue(valueAxis.referenceValue());
		}
		if ((position.__y >= referencePosition && position.__y <= snapPosition.__y) || (position.__y <= referencePosition && position.__y >= snapPosition.__y)) {
			return true;
		}
		return false;
	}
	,
	getResolvedHitTestMode: function () {
		if (this.hitTestMode() == $.ig.SeriesHitTestMode.prototype.auto) {
			return $.ig.SeriesHitTestMode.prototype.computational;
		} else {
			return $.ig.FinancialSeries.prototype.getResolvedHitTestMode.call(this);
		}
	}
	,
	getSeriesValueBoundingBox: function (world) {
		var pos = this.fromWorldPosition(world);
		var windowRect = this.view().windowRect();
		var viewportRect = this.view().viewport();
		var effectiveViewportRect = this.getEffectiveViewport1(this.view());
		var yParams = new $.ig.ScalerParams(0, windowRect, viewportRect, this.yAxis().isInverted(), effectiveViewportRect);
		var matching = this.getMatchingBuckets(this.xAxis(), this._currentFrame._buckets, this.getBucketSize(this.view()), this.getFirstBucket(this.view()), world, true);
		if (matching == null) {
			return $.ig.Rect.prototype.empty();
		}
		var prevBucketValue = matching[0];
		var nextBucketValue = matching[1];
		var prevDist = Math.abs(pos.__x - prevBucketValue[0]);
		var nextDist = Math.abs(pos.__x - nextBucketValue[0]);
		var zero = this.yAxis().getScaledValue(this.yAxis().referenceValue(), yParams);
		var groupWidth = 1;
		if (prevDist < nextDist) {
			var left = prevBucketValue[0] - 0.5 * groupWidth;
			var top = prevBucketValue[1];
			var bottom = zero;
			return new $.ig.Rect(0, left, Math.min(top, bottom), groupWidth, Math.max(top, bottom) - Math.min(top, bottom));
		} else {
			var left1 = nextBucketValue[0] - 0.5 * groupWidth;
			var top1 = nextBucketValue[1];
			var bottom1 = zero;
			return new $.ig.Rect(0, left1, Math.min(top1, bottom1), groupWidth, Math.max(top1, bottom1) - Math.min(top1, bottom1));
		}
	}
	,
	testHit: function (position, isFinger) {
		var snapPosition = this.getSeriesValuePosition(this.toWorldPosition(position), true, false);
		if (this.displayType() == $.ig.IndicatorDisplayType.prototype.area) {
			if (this.testAreaOver(position, snapPosition, isFinger)) {
				return true;
			}
		}
		if (this.displayType() == $.ig.IndicatorDisplayType.prototype.area || this.displayType() == $.ig.IndicatorDisplayType.prototype.line) {
			if (this.testHighLowStrokeOver(position, isFinger)) {
				return true;
			}
		}
		if (this.displayType() == $.ig.IndicatorDisplayType.prototype.column) {
			if (this.testShapesOver(position, isFinger)) {
				return true;
			}
		}
		return false;
	}
	,
	dataUpdatedOverride: function (action, position, count, propertyName) {
		if (this.xAxis() != null && $.ig.util.cast($.ig.ISortingAxis.prototype.$type, this.xAxis()) !== null) {
			(this.xAxis()).notifyDataChanged();
		}
		this.indicatorView().trendLineManager().dataUpdated(action, position, count, propertyName);
		if (propertyName != null && this._mappingToColumnName.containsKey(propertyName)) {
			var $ret = this._mappingToColumnName.tryGetValue(propertyName, propertyName);
			propertyName = $ret.p1;
		}
		if (this.xAxis() != null && $.ig.util.cast($.ig.ISortingAxis.prototype.$type, this.xAxis()) !== null) {
			action = $.ig.FastItemsSourceEventAction.prototype.reset;
			position = 0;
			count = this.fastItemsSource().count();
		}
		switch (action) {
			case $.ig.FastItemsSourceEventAction.prototype.change:
				if (this.shouldUpdateIndicator(position, count, propertyName)) {
					this.updateIndicator(position, count, propertyName);
					this.ensureYRangeThenRender(true);
				}
				break;
			case $.ig.FastItemsSourceEventAction.prototype.replace:
				if (this.shouldUpdateIndicator(position, this.fastItemsSource().count() - position, propertyName)) {
					this.updateIndicator(position, this.fastItemsSource().count() - position, propertyName);
					this.ensureYRangeThenRender(true);
				}
				break;
			case $.ig.FastItemsSourceEventAction.prototype.insert:
				if (this.shouldUpdateIndicator(position, this.fastItemsSource().count() - position, propertyName)) {
					var newVals = new Array(count);
					for (var j = 0; j < count; j++) {
						newVals[j] = 0;
					}
					this.indicatorColumn().insertRange(position, newVals);
					this.updateIndicator(position, this.fastItemsSource().count() - position, propertyName);
					this.ensureYRangeThenRender(true);
				}
				break;
			case $.ig.FastItemsSourceEventAction.prototype.remove:
				if (this.shouldUpdateIndicator(position, this.fastItemsSource().count() - position, propertyName)) {
					this.indicatorColumn().removeRange(position, count);
					this.updateIndicator(position, this.fastItemsSource().count() - position, propertyName);
					this.ensureYRangeThenRender(true);
				}
				break;
			case $.ig.FastItemsSourceEventAction.prototype.reset:
				if (this.shouldUpdateIndicator(position, this.fastItemsSource().count() - position, propertyName)) {
					this.indicatorColumn(new $.ig.List$1(Number, 2, this.fastItemsSource().count()));
					var newVals1 = new Array(count);
					for (var j1 = 0; j1 < count; j1++) {
						newVals1[j1] = 0;
					}
					this.indicatorColumn().insertRange(0, newVals1);
					this.updateIndicator(position, this.fastItemsSource().count() - position, propertyName);
					this.ensureYRangeThenRender(true);
				}
				break;
		}
	}
	,
	displayType: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.FinancialIndicator.prototype.displayTypeProperty, $.ig.IndicatorDisplayType.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.FinancialIndicator.prototype.displayTypeProperty));
		}
	}
	,
	ignoreFirst: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.FinancialIndicator.prototype.ignoreFirstProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.FinancialIndicator.prototype.ignoreFirstProperty));
		}
	}
	,
	trendLineType: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.FinancialIndicator.prototype.trendLineTypeProperty, $.ig.TrendLineType.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.FinancialIndicator.prototype.trendLineTypeProperty));
		}
	}
	,
	trendLineBrush: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.FinancialIndicator.prototype.trendLineBrushProperty, value);
			return value;
		} else {
			return this.getValue($.ig.FinancialIndicator.prototype.trendLineBrushProperty);
		}
	}
	,
	actualTrendLineBrush: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.FinancialIndicator.prototype.actualTrendLineBrushProperty, value);
			return value;
		} else {
			return this.getValue($.ig.FinancialIndicator.prototype.actualTrendLineBrushProperty);
		}
	}
	,
	trendLineThickness: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.FinancialIndicator.prototype.trendLineThicknessProperty, value);
			return value;
		} else {
			return this.getValue($.ig.FinancialIndicator.prototype.trendLineThicknessProperty);
		}
	}
	,
	trendLineDashCap: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.FinancialIndicator.prototype.trendLineDashCapProperty, $.ig.PenLineCap.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.FinancialIndicator.prototype.trendLineDashCapProperty));
		}
	}
	,
	trendLineDashArray: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.FinancialIndicator.prototype.trendLineDashArrayProperty, value);
			return value;
		} else {
			return this.getValue($.ig.FinancialIndicator.prototype.trendLineDashArrayProperty);
		}
	}
	,
	trendLinePeriod: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.FinancialIndicator.prototype.trendLinePeriodProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.FinancialIndicator.prototype.trendLinePeriodProperty));
		}
	}
	,
	trendPeriodOverride: function () {
		return -1;
	}
	,
	trendLineZIndex: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.FinancialIndicator.prototype.trendLineZIndexProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.FinancialIndicator.prototype.trendLineZIndexProperty));
		}
	}
	,
	_indicatorColumn: null,
	indicatorColumn: function (value) {
		if (arguments.length === 1) {
			this._indicatorColumn = value;
			return value;
		} else {
			return this._indicatorColumn;
		}
	}
	,
	_indicatorRange: null,
	indicatorRange: function (value) {
		if (arguments.length === 1) {
			this._indicatorRange = value;
			return value;
		} else {
			return this._indicatorRange;
		}
	}
	,
	indicatorOverride: function (position, count) {
	}
	,
	basedOn: function (position, count) {
	}
	,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		switch (propertyName) {
			case $.ig.FinancialSeries.prototype.xAxisPropertyName:
				this.indicatorView().selectTrendlineManager();
				break;
		}
		$.ig.FinancialSeries.prototype.propertyUpdatedOverride.call(this, sender, propertyName, oldValue, newValue);
		if (this.indicatorView().trendLineManager().propertyUpdated(sender, propertyName, oldValue, newValue)) {
			this.renderSeries(false);
			this.notifyThumbnailAppearanceChanged();
		}
		switch (propertyName) {
			case $.ig.Series.prototype.fastItemsSourcePropertyName:
				if (this.fastItemsSource() != null) {
					this.indicatorColumn(new $.ig.List$1(Number, 2, this.fastItemsSource().count()));
					var newVals = new Array(this.fastItemsSource().count());
					for (var j = 0; j < this.fastItemsSource().count(); j++) {
						newVals[j] = 0;
					}
					this.indicatorColumn().insertRange(0, newVals);
					this.updateIndicator(0, this.fastItemsSource().count(), null);
					if (this.yAxis() != null && !this.yAxis().updateRange()) {
						this.financialView().bucketCalculator().calculateBuckets(this.resolution());
						this.renderSeries(false);
					}
				}
				break;
			case $.ig.Series.prototype.trendLineBrushPropertyName:
				this.updateIndexedProperties();
				break;
			case $.ig.FinancialIndicator.prototype.displayTypePropertyName:
				this.clearRendering(true, this.view());
				this.renderSeries(false);
				this.notifyThumbnailAppearanceChanged();
				break;
			case $.ig.FinancialSeries.prototype.openColumnPropertyName:
			case $.ig.FinancialSeries.prototype.highColumnPropertyName:
			case $.ig.FinancialSeries.prototype.lowColumnPropertyName:
			case $.ig.FinancialSeries.prototype.closeColumnPropertyName:
			case $.ig.FinancialSeries.prototype.volumeColumnPropertyName:
				if (this.fastItemsSource() != null) {
					if (this.indicatorColumn().count() != this.fastItemsSource().count()) {
						this.indicatorColumn(new $.ig.List$1(Number, 2, this.fastItemsSource().count()));
						var newVals1 = new Array(this.fastItemsSource().count());
						for (var j1 = 0; j1 < this.fastItemsSource().count(); j1++) {
							newVals1[j1] = 0;
						}
						this.indicatorColumn().insertRange(0, newVals1);
					}
					if (this.shouldUpdateIndicator(0, this.fastItemsSource().count() - 1, propertyName)) {
						this.fullIndicatorRefresh();
					}
				}
				break;
			case $.ig.FinancialSeries.prototype.xAxisPropertyName:
				if (this.xAxis() != null && ($.ig.util.cast($.ig.ISortingAxis.prototype.$type, this.xAxis()) !== null || $.ig.util.cast($.ig.ISortingAxis.prototype.$type, oldValue) !== null)) {
					this.fullIndicatorRefresh();
				}
				break;
			case $.ig.FinancialSeries.prototype.yAxisPropertyName:
			case $.ig.FinancialIndicator.prototype.ignoreFirstPropertyName:
				this.fullIndicatorRefresh();
				this.notifyThumbnailAppearanceChanged();
				break;
			case $.ig.Series.prototype.trendLineTypePropertyName:
				this.notifyThumbnailAppearanceChanged();
				break;
		}
	}
	,
	getSeriesValue: function (world, useInterpolation, skipUnknowns) {
		if (this.indicatorColumn() == null) {
			return $.ig.FinancialSeries.prototype.getSeriesValue.call(this, world, useInterpolation, skipUnknowns);
		}
		var effectiveViewportRect = this.getEffectiveViewport1(this.view());
		var offset = this.getOffset(this.view().windowRect(), this.view().viewport(), effectiveViewportRect);
		var xParams = new $.ig.ScalerParams(0, this.view().windowRect(), this.view().viewport(), this.xAxis().isInverted(), effectiveViewportRect);
		xParams._effectiveViewportRect = this.seriesViewer().viewportRect();
		return this.getSeriesValueHelper(this.indicatorColumn(), world, this.xAxis(), xParams, offset, this.getExactUnsortedItemIndex.runOn(this), useInterpolation, skipUnknowns);
	}
	,
	getPreviousOrExactIndex: function (world, skipUnknowns) {
		if (this.indicatorColumn() == null) {
			return $.ig.FinancialSeries.prototype.getPreviousOrExactIndex.call(this, world, skipUnknowns);
		}
		return this.getPreviousOrExactIndexHelper(world, skipUnknowns, this.xAxis(), this.getExactUnsortedItemIndex.runOn(this), this.indicatorColumn());
	}
	,
	getNextOrExactIndex: function (world, skipUnknowns) {
		if (this.indicatorColumn() == null) {
			return $.ig.FinancialSeries.prototype.getNextOrExactIndex.call(this, world, skipUnknowns);
		}
		return this.getNextOrExactIndexHelper(world, skipUnknowns, this.xAxis(), this.getExactUnsortedItemIndex.runOn(this), this.indicatorColumn());
	}
	,
	usesPresortedValueColumn: function () {
		return true;
	}
	,
	shouldUpdateIndicator: function (position, count, updatedPropertyName) {
		if (updatedPropertyName == null) {
			return true;
		}
		if (this.basedOn(position, count).contains(updatedPropertyName)) {
			return true;
		}
		return false;
	}
	,
	updateIndicator: function (position, count, updatedPropertyName) {
		this.indicatorOverride(position, count);
	}
	,
	getRange: function (axis) {
		if (this.fastItemsSource() == null) {
			return null;
		}
		if (axis != null && axis == this.yAxis()) {
			return this.indicatorRange();
		}
		return null;
	}
	,
	scrollIntoView: function (item) {
		var index = this.fastItemsSource() != null ? this.fastItemsSource().indexOf(item) : -1;
		var windowRect = this.view().windowRect();
		var viewportRect = this.view().viewport();
		var unitRect = new $.ig.Rect(0, 0, 0, 1, 1);
		var effectiveViewportRect = this.getEffectiveViewportForUnitViewport(this.view());
		if (index >= 0 && $.ig.Rect.prototype.l_op_Inequality(windowRect, null) && $.ig.Rect.prototype.l_op_Inequality(viewportRect, null)) {
			if (this.xAxis() != null) {
				var xParams = new $.ig.ScalerParams(0, unitRect, unitRect, this.xAxis().isInverted(), effectiveViewportRect);
				var cx = this.xAxis().getScaledValue(index, xParams);
				if (cx < windowRect.left() + 0.1 * windowRect.width()) {
					cx = cx + 0.4 * windowRect.width();
				}
				if (cx > windowRect.right() - 0.1 * windowRect.width()) {
					cx = cx - 0.4 * windowRect.width();
				}
				windowRect.x(cx - 0.5 * windowRect.width());
			}
			if (this.yAxis() != null && this.indicatorColumn() != null && index < this.indicatorColumn().count()) {
				var yParams = new $.ig.ScalerParams(0, unitRect, unitRect, this.yAxis().isInverted(), effectiveViewportRect);
				var cy = this.yAxis().getScaledValue(this.indicatorColumn().__inner[index], yParams);
				if (cy < windowRect.top() + 0.1 * windowRect.height()) {
					cy = cy + 0.4 * windowRect.height();
				}
				if (cy > windowRect.bottom() - 0.1 * windowRect.height()) {
					cy = cy - 0.4 * windowRect.height();
				}
				windowRect.y(cy - 0.5 * windowRect.height());
			}
			this.syncLink().windowNotify(this.seriesViewer(), windowRect);
		}
		return index >= 0;
	}
	,
	validateSeries: function (viewportRect, windowRect, view) {
		var isValid = $.ig.FinancialSeries.prototype.validateSeries.call(this, viewportRect, windowRect, view);
		if (this.indicatorColumn() == null || this.indicatorColumn().count() == 0) {
			isValid = false;
		}
		return isValid;
	}
	,
	prepareFrame: function (frame, view) {
		var $self = this;
		$.ig.FinancialSeries.prototype.prepareFrame.call(this, frame, view);
		var windowRect = view.windowRect();
		var viewportRect = view.viewport();
		var effectiveViewportRect = this.getEffectiveViewport1(view);
		var xaxis = this.xAxis();
		var yaxis = this.yAxis();
		var xParams = new $.ig.ScalerParams(0, windowRect, viewportRect, xaxis.isInverted(), effectiveViewportRect);
		var yParams = new $.ig.ScalerParams(0, windowRect, viewportRect, yaxis.isInverted(), effectiveViewportRect);
		frame._buckets.clear();
		frame._markers.clear();
		frame._trend.clear();
		var offset = 0;
		var sortingXAxis = $.ig.util.cast($.ig.ISortingAxis.prototype.$type, this.xAxis());
		if (sortingXAxis != null && sortingXAxis.sortedIndices().count() != this.fastItemsSource().count()) {
			return;
		}
		offset = this.getOffset(windowRect, viewportRect, effectiveViewportRect);
		var _trendPeriod = this.trendPeriodOverride();
		if (_trendPeriod == -1) {
			_trendPeriod = this.trendLinePeriod();
		}
		var indicatorView = $.ig.util.cast($.ig.FinancialIndicatorView.prototype.$type, view);
		this.indicatorView().trendLineManager().prepareLine(frame._trend, this.trendLineType(), this.indicatorColumn(), _trendPeriod, function (x) { return $self.xAxis().getScaledValue(x, xParams); }, function (y) { return $self.yAxis().getScaledValue(y, yParams); }, (function () {
			var $ret = new $.ig.TrendResolutionParams();
			$ret.bucketSize(view.bucketCalculator().bucketSize());
			$ret.firstBucket(view.bucketCalculator().firstBucket());
			$ret.lastBucket(view.bucketCalculator().lastBucket());
			$ret.offset(offset);
			$ret.resolution($self.resolution());
			$ret.viewport(viewportRect);
			$ret.window(windowRect);
			return $ret;
		}()));
		var singlePixelSpan = this.convertToSingle(this.xAxis().getUnscaledValue(2, xParams) - this.xAxis().getUnscaledValue(1, xParams));
		for (var i = view.bucketCalculator().firstBucket(); i <= view.bucketCalculator().lastBucket(); ++i) {
			var bucket;
			if (sortingXAxis == null) {
				bucket = view.bucketCalculator().getBucket(i);
			} else {
				var index = sortingXAxis.sortedIndices().__inner[i];
				var bucketX = sortingXAxis.getUnscaledValueAt(index);
				var bucketY0 = this.convertToSingle(this.indicatorColumn().__inner[i]);
				var bucketY1 = bucketY0;
				var currentX = bucketX;
				while (i < view.bucketCalculator().lastBucket()) {
					index = sortingXAxis.sortedIndices().__inner[i + 1];
					currentX = sortingXAxis.getUnscaledValueAt(index);
					if (currentX - bucketX > singlePixelSpan) {
						break;
					}
					i++;
					var y = this.convertToSingle(this.indicatorColumn().__inner[i]);
					bucketY0 = Math.min(bucketY0, y);
					bucketY1 = Math.max(bucketY1, y);
				}
				var xVal = NaN;
				if (!$.ig.util.isNaN(bucketX)) {
					xVal = this.xAxis().getScaledValue(bucketX, xParams);
				}
				bucket = [ this.convertToSingle(xVal), bucketY0, bucketY1 ];
			}
			if (!isNaN(bucket[0])) {
				if (this.xAxis() != null && $.ig.util.cast($.ig.ISortingAxis.prototype.$type, this.xAxis()) !== null) {
					bucket[0] = (bucket[0] + offset);
				} else {
					bucket[0] = (xaxis.getScaledValue(bucket[0], xParams) + offset);
				}
				bucket[1] = yaxis.getScaledValue(bucket[1], yParams);
				if (view.bucketCalculator().bucketSize() > 1 || sortingXAxis != null) {
					bucket[2] = yaxis.getScaledValue(bucket[2], yParams);
				} else {
					bucket[2] = bucket[1];
				}
				frame._buckets.add(bucket);
			}
		}
		return;
	}
	,
	convertToSingle: function (p) {
		return p;
	}
	,
	clearRendering: function (wipeClean, view) {
		$.ig.FinancialSeries.prototype.clearRendering.call(this, wipeClean, view);
		var indicatorView = view;
		indicatorView.clearIndicatorVisual(wipeClean);
		indicatorView.trendLineManager().clearPoints();
	}
	,
	getDefaultTransitionInMode: function () {
		switch (this.displayType()) {
			case $.ig.IndicatorDisplayType.prototype.area: return $.ig.CategoryTransitionInMode.prototype.fromZero;
			case $.ig.IndicatorDisplayType.prototype.column: return $.ig.CategoryTransitionInMode.prototype.fromZero;
			case $.ig.IndicatorDisplayType.prototype.line: return $.ig.CategoryTransitionInMode.prototype.sweepFromCategoryAxisMinimum;
		}
		return $.ig.CategoryTransitionInMode.prototype.fromZero;
	}
	,
	renderFrame: function (frame, view) {
		$.ig.FinancialSeries.prototype.renderFrame.call(this, frame, view);
		var indicatorView = $.ig.util.cast($.ig.FinancialIndicatorView.prototype.$type, view);
		var x0 = function (i) {
			return frame._buckets.__inner[i][0];
		};
		var y0 = function (i) {
			return frame._buckets.__inner[i][1];
		};
		var x1 = function (i) {
			return frame._buckets.__inner[i][0];
		};
		var y1 = function (i) {
			return frame._buckets.__inner[i][2];
		};
		indicatorView.trendLineManager().clearPoints();
		indicatorView.trendLineManager().rasterizeTrendLine(frame._trend);
		var viewportRect = view.viewport();
		var effectiveViewportRect = this.getEffectiveViewport1(view);
		var zero = 0;
		var positiveBrush = this.actualBrush();
		var negativeBrush = this.negativeBrush();
		if (view.checkFrameDirty(frame)) {
			indicatorView.clearIndicatorVisual(false);
			if (frame._buckets.count() > 0) {
				switch (this.displayType()) {
					case $.ig.IndicatorDisplayType.prototype.line:
						indicatorView.rasterizeLine(frame._buckets.count(), x0, y0, x1, y1, true);
						break;
					case $.ig.IndicatorDisplayType.prototype.area:
						if (this.yAxis() != null) {
							zero = this.getWorldZeroValue(view);
						} else {
							zero = 0.5 * (viewportRect.top() + viewportRect.bottom());
						}
						indicatorView.rasterizeArea(frame._buckets.count(), x0, y0, x1, y1, true, zero);
						break;
					case $.ig.IndicatorDisplayType.prototype.column:
						zero = this.getWorldZeroValue(view);
						indicatorView.rasterizeColumns(frame._buckets.count(), x0, y0, x1, y1, true, zero);
						break;
					default: throw new $.ig.NotImplementedException(0);
				}
			}
			view.updateFrameVersion(frame);
		}
		var buckets = frame._buckets;
		var valueCount = this.fastItemsSource().count();
		this._renderManager.initCategoryRenderSettings(this, this.shouldOverrideCategoryStyle(), this.xAxis(), this.getCategoryItems.runOn(this), this.getBucketSize(view), this.getFirstBucket(view));
		var areStylesOverriden = false;
		var args = this._renderManager.categoryOverrideArgs();
		if (args != null) {
			areStylesOverriden = true;
		}
		this._renderManager._initialRenderFill = positiveBrush;
		this._renderManager._actualRenderFill = positiveBrush;
		if (areStylesOverriden) {
			var xParams = new $.ig.ScalerParams(0, view.windowRect(), view.viewport(), this.xAxis().isInverted(), effectiveViewportRect);
			this._renderManager._actualNegativeShape = false;
			this.performCategoryStyleOverride(buckets, -1, valueCount, this.xAxis(), xParams, view.isThumbnailView());
		}
		this._renderManager.setCategoryShapeAppearance(indicatorView._positivePath0, true, false, true, false);
		this._renderManager.setCategoryShapeAppearance(indicatorView._positivePath1, true, false, true, false);
		this._renderManager.setCategoryShapeAppearance(indicatorView._positivePath01, false, true, false, false);
		indicatorView._positivePath01.__opacity = 0.8 * this._renderManager._actualRenderOpacity * this.actualAreaFillOpacity();
		this._renderManager.setCategoryShapeAppearance(indicatorView._positiveColumns, true, false, false, false);
		this._renderManager._initialRenderFill = negativeBrush;
		this._renderManager._actualRenderFill = negativeBrush;
		if (areStylesOverriden) {
			var xParams1 = new $.ig.ScalerParams(0, view.windowRect(), view.viewport(), this.xAxis().isInverted(), effectiveViewportRect);
			this._renderManager._actualNegativeShape = true;
			this.performCategoryStyleOverride(buckets, -1, valueCount, this.xAxis(), xParams1, view.isThumbnailView());
		}
		this._renderManager.setCategoryShapeAppearance(indicatorView._negativePath0, true, false, true, false);
		this._renderManager.setCategoryShapeAppearance(indicatorView._negativePath1, true, false, true, false);
		this._renderManager.setCategoryShapeAppearance(indicatorView._negativePath01, false, true, false, false);
		indicatorView._negativePath01.__opacity = 0.8 * this._renderManager._actualRenderOpacity * this.actualAreaFillOpacity();
		this._renderManager.setCategoryShapeAppearance(indicatorView._negativeColumns, true, false, false, false);
		indicatorView.updateHitTests();
	}
	,
	updateIndexedProperties: function () {
		$.ig.FinancialSeries.prototype.updateIndexedProperties.call(this);
		if (this.index() < 0) {
			return;
		}
		this.indicatorView().updateTrendlineBrush();
	}
	,
	fullIndicatorRefresh: function () {
		this.indicatorView().trendLineManager().reset();
		this.indicatorOverride(0, this.indicatorColumn().count());
		if (this.yAxis() != null && !this.yAxis().updateRange()) {
			this.renderSeries(false);
		}
	}
	,
	exportVisualDataOverride: function (svd) {
		$.ig.FinancialSeries.prototype.exportVisualDataOverride.call(this, svd);
		var trendShape = new $.ig.PolyLineVisualData(1, "trendLine", this.indicatorView().trendLineManager().trendPolyline());
		trendShape.tags().add("Trend");
		svd.shapes().add(trendShape);
	}
	,
	$type: new $.ig.Type('FinancialIndicator', $.ig.FinancialSeries.prototype.$type)
}, true);

$.ig.util.defType('StrategyBasedIndicator', 'FinancialIndicator', {
	init: function () {
		$.ig.FinancialIndicator.prototype.init.call(this);
		this.actualCalculationStrategy(this.calculationStrategy());
		this.defaultStyleKey(this.styleKeyType());
	},
	_actualCalculationStrategy: null,
	actualCalculationStrategy: function (value) {
		if (arguments.length === 1) {
			this._actualCalculationStrategy = value;
			return value;
		} else {
			return this._actualCalculationStrategy;
		}
	}
	,
	calculationStrategy: function () {
	}
	,
	styleKeyType: function () {
	}
	,
	periodOverride: function () {
		return -2147483648;
	}
	,
	shortPeriodOverride: function () {
		return -2147483648;
	}
	,
	longPeriodOverride: function () {
		return -2147483648;
	}
	,
	createPeriodPropertyHelper: function (defaultValue, ownerType, propertyName) {
		var prop = $.ig.DependencyProperty.prototype.register(propertyName, $.ig.Number.prototype.$type, ownerType, new $.ig.PropertyMetadata(2, defaultValue, function (sender, e) {
			($.ig.util.cast($.ig.StrategyBasedIndicator.prototype.$type, sender)).raisePropertyChanged(propertyName, e.oldValue(), e.newValue());
		}));
		$.ig.StrategyBasedIndicator.prototype.invalidatesSeries.add(propertyName);
		return prop;
	}
	,
	createPeriodProperty: function (defaultValue, ownerType) {
		return $.ig.StrategyBasedIndicator.prototype.createPeriodPropertyHelper(defaultValue, ownerType, $.ig.StrategyBasedIndicator.prototype.periodPropertyName);
	}
	,
	createLongPeriodProperty: function (defaultValue, ownerType) {
		return $.ig.StrategyBasedIndicator.prototype.createPeriodPropertyHelper(defaultValue, ownerType, $.ig.StrategyBasedIndicator.prototype.longPeriodPropertyName);
	}
	,
	createShortPeriodProperty: function (defaultValue, ownerType) {
		return $.ig.StrategyBasedIndicator.prototype.createPeriodPropertyHelper(defaultValue, ownerType, $.ig.StrategyBasedIndicator.prototype.shortPeriodPropertyName);
	}
	,
	basedOn: function (position, count) {
		var dataSource = this.provideDataSource(position, count);
		var supportingCalculations = this.provideSupportingCalculations(dataSource);
		return this.actualCalculationStrategy().basedOn(dataSource, supportingCalculations);
	}
	,
	indicatorOverride: function (position, count) {
		var dataSource = this.provideDataSource(position, count);
		if (count == 0) {
			return false;
		}
		if (!this.validateBasedOn(this.basedOn(position, count))) {
			return false;
		}
		var supportingCalculations = this.provideSupportingCalculations(dataSource);
		if (this.indicatorRange() != null) {
			dataSource.minimumValue(this.indicatorRange().minimum());
			dataSource.maximumValue(this.indicatorRange().maximum());
		}
		var retVal = this.actualCalculationStrategy().calculateIndicator(dataSource, supportingCalculations);
		for (var i = 0; i < this.ignoreFirst() && i < dataSource.indicatorColumn().count(); i++) {
			dataSource.indicatorColumn().item(i, NaN);
		}
		if (this.yAxis() != null && this.updateRange(dataSource)) {
			this.yAxis().updateRange();
		}
		return retVal;
	}
	,
	updateRange: function (dataSource) {
		if (!$.ig.util.isNaN(dataSource.minimumValue()) && !$.ig.util.isNaN(dataSource.maximumValue()) && dataSource.specifiesRange()) {
			var pRange = this.indicatorRange();
			this.indicatorRange(new $.ig.AxisRange(dataSource.minimumValue(), dataSource.maximumValue()));
			return this.rangesDiffer(pRange, this.indicatorRange());
		}
		var minimum = 1.7976931348623157E+308;
		var maximum = -1.7976931348623157E+308;
		var en = dataSource.indicatorColumn().getEnumerator();
		while (en.moveNext()) {
			var value = en.current();
			if (!$.ig.util.isNaN(value)) {
				minimum = Math.min(minimum, value);
				maximum = Math.max(maximum, value);
			}
		}
		var prevRange = this.indicatorRange();
		this.indicatorRange(new $.ig.AxisRange(minimum, maximum));
		return this.rangesDiffer(prevRange, this.indicatorRange());
	}
	,
	rangesDiffer: function (prevRange, indicatorRange) {
		if (prevRange == null || indicatorRange == null) {
			return true;
		}
		if (prevRange.minimum() != indicatorRange.minimum()) {
			return true;
		}
		if (prevRange.maximum() != indicatorRange.maximum()) {
			return true;
		}
		return false;
	}
	,
	sanitizePeriod: function (periodValue) {
		if (periodValue == -2147483648) {
			return 0;
		}
		if (periodValue > this.indicatorColumn().count() && this.indicatorColumn().count() > 0) {
			periodValue = this.indicatorColumn().count() - 1;
		}
		if (periodValue < 1) {
			return 1;
		}
		return periodValue;
	}
	,
	provideDataSource: function (position, count) {
		var dataSource = $.ig.FinancialIndicator.prototype.provideDataSource.call(this, position, count);
		dataSource.indicatorColumn(this.indicatorColumn());
		dataSource.period(this.sanitizePeriod(this.periodOverride()));
		dataSource.shortPeriod(this.sanitizePeriod(this.shortPeriodOverride()));
		dataSource.longPeriod(this.sanitizePeriod(this.longPeriodOverride()));
		dataSource.multiplier(this.multiplierOverride());
		return dataSource;
	}
	,
	multiplierOverride: function () {
		return 1;
	}
	,
	createMultiplierProperty: function (defaultValue, ownerType) {
		var prop = $.ig.DependencyProperty.prototype.register($.ig.StrategyBasedIndicator.prototype._multiplerPropertyName, Number, ownerType, new $.ig.PropertyMetadata(2, defaultValue, function (sender, e) {
			($.ig.util.cast($.ig.StrategyBasedIndicator.prototype.$type, sender)).raisePropertyChanged($.ig.StrategyBasedIndicator.prototype._multiplerPropertyName, e.oldValue(), e.newValue());
		}));
		$.ig.StrategyBasedIndicator.prototype.invalidatesSeries.add($.ig.StrategyBasedIndicator.prototype._multiplerPropertyName);
		return prop;
	}
	,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		$.ig.FinancialIndicator.prototype.propertyUpdatedOverride.call(this, sender, propertyName, oldValue, newValue);
		if ($.ig.StrategyBasedIndicator.prototype.invalidatesSeries.contains(propertyName)) {
			if (this.yAxis() != null && !this.yAxis().updateRange()) {
				this.financialView().bucketCalculator().calculateBuckets(this.resolution());
				this.indicatorOverride(0, this.indicatorColumn().count());
				this.indicatorView().trendLineManager().reset();
				this.renderSeries(false);
			}
		}
	}
	,
	$type: new $.ig.Type('StrategyBasedIndicator', $.ig.FinancialIndicator.prototype.$type)
}, true);

$.ig.util.defType('AbsoluteVolumeOscillatorIndicator', 'StrategyBasedIndicator', {
	init: function () {
		$.ig.StrategyBasedIndicator.prototype.init.call(this);
	},
	calculationStrategy: function () {
		return new $.ig.AbsoluteVolumeOscillatorIndicatorStrategy();
	}
	,
	styleKeyType: function () {
		return $.ig.AbsoluteVolumeOscillatorIndicator.prototype.$type;
	}
	,
	shortPeriod: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.AbsoluteVolumeOscillatorIndicator.prototype.shortPeriodProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.AbsoluteVolumeOscillatorIndicator.prototype.shortPeriodProperty));
		}
	}
	,
	shortPeriodOverride: function () {
		return this.shortPeriod();
	}
	,
	longPeriod: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.AbsoluteVolumeOscillatorIndicator.prototype.longPeriodProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.AbsoluteVolumeOscillatorIndicator.prototype.longPeriodProperty));
		}
	}
	,
	longPeriodOverride: function () {
		return this.longPeriod();
	}
	,
	$type: new $.ig.Type('AbsoluteVolumeOscillatorIndicator', $.ig.StrategyBasedIndicator.prototype.$type)
}, true);

$.ig.util.defType('IndicatorCalculationStrategy', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	calculateIndicator: function (dataSource, supportingCalculations) {
	}
	,
	basedOn: function (dataSource, supportingCalculations) {
	}
	,
	$type: new $.ig.Type('IndicatorCalculationStrategy', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('AbsoluteVolumeOscillatorIndicatorStrategy', 'IndicatorCalculationStrategy', {
	init: function () {
		$.ig.IndicatorCalculationStrategy.prototype.init.call(this);
	},
	basedOn: function (dataSource, supportingCalculations) {
		var list = new $.ig.List$1(String, 0);
		list.addRange(supportingCalculations.shortVolumeOscillatorAverage().basedOn());
		list.addRange(supportingCalculations.longVolumeOscillatorAverage().basedOn());
		return list;
	}
	,
	calculateIndicator: function (dataSource, supportingCalculations) {
		var shortEma = supportingCalculations.shortVolumeOscillatorAverage().strategy()(dataSource).getEnumerator();
		var longEma = supportingCalculations.longVolumeOscillatorAverage().strategy()(dataSource).getEnumerator();
		var indicatorColumn = dataSource.indicatorColumn();
		var i = 0;
		while (shortEma.moveNext() && longEma.moveNext()) {
			var pvo = shortEma.current() - longEma.current();
			indicatorColumn.item(i, supportingCalculations.makeSafe()(pvo));
			++i;
		}
		return true;
	}
	,
	$type: new $.ig.Type('AbsoluteVolumeOscillatorIndicatorStrategy', $.ig.IndicatorCalculationStrategy.prototype.$type)
}, true);

$.ig.util.defType('AccumulationDistributionIndicator', 'StrategyBasedIndicator', {
	init: function () {
		$.ig.StrategyBasedIndicator.prototype.init.call(this);
	},
	calculationStrategy: function () {
		return new $.ig.AccumulationDistributionIndicatorStrategy();
	}
	,
	styleKeyType: function () {
		return $.ig.AccumulationDistributionIndicator.prototype.$type;
	}
	,
	$type: new $.ig.Type('AccumulationDistributionIndicator', $.ig.StrategyBasedIndicator.prototype.$type)
}, true);

$.ig.util.defType('StreamingIndicatorCalculationStrategy', 'IndicatorCalculationStrategy', {
	init: function () {
		$.ig.IndicatorCalculationStrategy.prototype.init.call(this);
	},
	provideStream: function (dataSource, supportingCalculations) {
	}
	,
	$type: new $.ig.Type('StreamingIndicatorCalculationStrategy', $.ig.IndicatorCalculationStrategy.prototype.$type)
}, true);

$.ig.util.defType('AccumulationDistributionIndicatorStrategy', 'StreamingIndicatorCalculationStrategy', {
	init: function () {
		$.ig.StreamingIndicatorCalculationStrategy.prototype.init.call(this);
	},
	basedOn: function (dataSource, supportingCalculations) {
		var list = new $.ig.List$1(String, 0);
		list.add($.ig.FinancialSeries.prototype.closeColumnPropertyName);
		list.add($.ig.FinancialSeries.prototype.lowColumnPropertyName);
		list.add($.ig.FinancialSeries.prototype.highColumnPropertyName);
		list.add($.ig.FinancialSeries.prototype.volumeColumnPropertyName);
		return list;
	}
	,
	calculateIndicator: function (dataSource, supportingCalculations) {
		var i = 0;
		var en = this.provideStream(dataSource, supportingCalculations).getEnumerator();
		while (en.moveNext()) {
			var value = en.current();
			dataSource.indicatorColumn().item(i, value);
			i++;
		}
		return true;
	}
	,
	provideStream: function (dataSource, supportingCalculations) {
		var d__ = new $.ig.AccumulationDistributionIndicatorStrategy___ProvideStream__IteratorClass(-2);
		d__.__4__this = this;
		d__.__3__dataSource = dataSource;
		d__.__3__supportingCalculations = supportingCalculations;
		return d__;
	}
	,
	$type: new $.ig.Type('AccumulationDistributionIndicatorStrategy', $.ig.StreamingIndicatorCalculationStrategy.prototype.$type)
}, true);

$.ig.util.defType('AverageDirectionalIndexIndicator', 'StrategyBasedIndicator', {
	init: function () {
		$.ig.StrategyBasedIndicator.prototype.init.call(this);
	},
	calculationStrategy: function () {
		return new $.ig.AverageDirectionalIndexIndicatorStrategy();
	}
	,
	styleKeyType: function () {
		return $.ig.AverageDirectionalIndexIndicator.prototype.$type;
	}
	,
	period: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.AverageDirectionalIndexIndicator.prototype.periodProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.AverageDirectionalIndexIndicator.prototype.periodProperty));
		}
	}
	,
	periodOverride: function () {
		return this.period();
	}
	,
	$type: new $.ig.Type('AverageDirectionalIndexIndicator', $.ig.StrategyBasedIndicator.prototype.$type)
}, true);

$.ig.util.defType('AverageDirectionalIndexIndicatorStrategy', 'IndicatorCalculationStrategy', {
	init: function () {
		$.ig.IndicatorCalculationStrategy.prototype.init.call(this);
	},
	basedOn: function (dataSource, supportingCalculations) {
		var list = new $.ig.List$1(String, 0);
		list.add($.ig.FinancialSeries.prototype.lowColumnPropertyName);
		list.add($.ig.FinancialSeries.prototype.highColumnPropertyName);
		list.addRange(dataSource.trueRange().basedOn());
		list.addRange(supportingCalculations.eMA().basedOn());
		return list;
	}
	,
	upMove: function (index, highColumn, lowColumn) {
		return highColumn.item(index) - highColumn.item(index - 1);
	}
	,
	downMove: function (index, highColumn, lowColumn) {
		return lowColumn.item(index - 1) - lowColumn.item(index);
	}
	,
	plusDM: function (highColumn, lowColumn) {
		var d__ = new $.ig.AverageDirectionalIndexIndicatorStrategy___PlusDM__IteratorClass(-2);
		d__.__4__this = this;
		d__.__3__highColumn = highColumn;
		d__.__3__lowColumn = lowColumn;
		return d__;
	}
	,
	minusDM: function (highColumn, lowColumn) {
		var d__ = new $.ig.AverageDirectionalIndexIndicatorStrategy___MinusDM__IteratorClass(-2);
		d__.__4__this = this;
		d__.__3__highColumn = highColumn;
		d__.__3__lowColumn = lowColumn;
		return d__;
	}
	,
	plusDI: function (dataSource, supportingCalculations) {
		var d__ = new $.ig.AverageDirectionalIndexIndicatorStrategy___PlusDI__IteratorClass(-2);
		d__.__4__this = this;
		d__.__3__dataSource = dataSource;
		d__.__3__supportingCalculations = supportingCalculations;
		return d__;
	}
	,
	minusDI: function (dataSource, supportingCalculations) {
		var d__ = new $.ig.AverageDirectionalIndexIndicatorStrategy___MinusDI__IteratorClass(-2);
		d__.__4__this = this;
		d__.__3__dataSource = dataSource;
		d__.__3__supportingCalculations = supportingCalculations;
		return d__;
	}
	,
	aDXHelper: function (dataSource, supportingCalculations) {
		var d__ = new $.ig.AverageDirectionalIndexIndicatorStrategy___ADXHelper__IteratorClass(-2);
		d__.__4__this = this;
		d__.__3__dataSource = dataSource;
		d__.__3__supportingCalculations = supportingCalculations;
		return d__;
	}
	,
	aDX: function (dataSource, supportingCalculations) {
		var d__ = new $.ig.AverageDirectionalIndexIndicatorStrategy___ADX__IteratorClass(-2);
		d__.__4__this = this;
		d__.__3__dataSource = dataSource;
		d__.__3__supportingCalculations = supportingCalculations;
		return d__;
	}
	,
	calculateIndicator: function (dataSource, supportingCalculations) {
		var adx = this.aDX(dataSource, supportingCalculations).getEnumerator();
		var indicatorColumn = dataSource.indicatorColumn();
		for (var i = 0; i < indicatorColumn.count(); i++) {
			if (adx.moveNext()) {
				indicatorColumn.item(i, adx.current());
			}
		}
		return true;
	}
	,
	$type: new $.ig.Type('AverageDirectionalIndexIndicatorStrategy', $.ig.IndicatorCalculationStrategy.prototype.$type)
}, true);

$.ig.util.defType('AverageTrueRangeIndicator', 'StrategyBasedIndicator', {
	init: function () {
		$.ig.StrategyBasedIndicator.prototype.init.call(this);
	},
	calculationStrategy: function () {
		return new $.ig.AverageTrueRangeIndicatorStrategy();
	}
	,
	styleKeyType: function () {
		return $.ig.AverageTrueRangeIndicator.prototype.$type;
	}
	,
	period: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.AverageTrueRangeIndicator.prototype.periodProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.AverageTrueRangeIndicator.prototype.periodProperty));
		}
	}
	,
	periodOverride: function () {
		return this.period();
	}
	,
	$type: new $.ig.Type('AverageTrueRangeIndicator', $.ig.StrategyBasedIndicator.prototype.$type)
}, true);

$.ig.util.defType('AverageTrueRangeIndicatorStrategy', 'IndicatorCalculationStrategy', {
	init: function () {
		$.ig.IndicatorCalculationStrategy.prototype.init.call(this);
	},
	basedOn: function (dataSource, supportingCalculations) {
		var list = new $.ig.List$1(String, 0);
		list.addRange(dataSource.trueRange().basedOn());
		list.addRange(supportingCalculations.eMA().basedOn());
		return list;
	}
	,
	calculateIndicator: function (dataSource, supportingCalculations) {
		var i = 0;
		var en = supportingCalculations.eMA().strategy()(dataSource.trueRange(), dataSource.period()).getEnumerator();
		while (en.moveNext()) {
			var atr = en.current();
			dataSource.indicatorColumn().item(i, atr);
			++i;
		}
		return true;
	}
	,
	$type: new $.ig.Type('AverageTrueRangeIndicatorStrategy', $.ig.IndicatorCalculationStrategy.prototype.$type)
}, true);

$.ig.util.defType('FinancialOverlay', 'FinancialSeries', {
	init: function () {
		$.ig.FinancialSeries.prototype.init.call(this);
		this.overlayValid(false);
	},
	dataUpdatedOverride: function (action, position, count, propertyName) {
		if (this.xAxis() != null && $.ig.util.cast($.ig.ISortingAxis.prototype.$type, this.xAxis()) !== null) {
			(this.xAxis()).notifyDataChanged();
		}
		this.financialView().bucketCalculator().calculateBuckets(this.resolution());
		this.validateOverlay();
		if (this.yAxis() != null) {
			this.yAxis().updateRange();
		}
		this.renderSeries(true);
	}
	,
	ignoreFirst: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.FinancialOverlay.prototype.ignoreFirstProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.FinancialOverlay.prototype.ignoreFirstProperty));
		}
	}
	,
	usesPresortedValueColumn: function () {
		return true;
	}
	,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		switch (propertyName) {
			case $.ig.Series.prototype.fastItemsSourcePropertyName:
				this.overlayValid(false);
				break;
			case $.ig.FinancialOverlay.prototype.ignoreFirstPropertyName:
				this.overlayValid(false);
				this.renderSeries(false);
				this.notifyThumbnailAppearanceChanged();
				break;
			case $.ig.FinancialSeries.prototype.xAxisPropertyName:
				this.overlayValid(false);
				break;
		}
		$.ig.FinancialSeries.prototype.propertyUpdatedOverride.call(this, sender, propertyName, oldValue, newValue);
	}
	,
	getResolvedHitTestMode: function () {
		if (this.hitTestMode() == $.ig.SeriesHitTestMode.prototype.auto) {
			return $.ig.SeriesHitTestMode.prototype.computational;
		} else {
			return $.ig.FinancialSeries.prototype.getResolvedHitTestMode.call(this);
		}
	}
	,
	testHit: function (position, isFinger) {
		if (this.testHighLowStrokeOver(position, isFinger)) {
			return true;
		}
		return false;
	}
	,
	onApplyTemplate: function () {
		$.ig.FinancialSeries.prototype.onApplyTemplate.call(this);
		this.renderSeries(false);
	}
	,
	scrollIntoView: function (item) {
		var index = this.fastItemsSource() != null ? this.fastItemsSource().indexOf(item) : -1;
		var windowRect = this.view().windowRect();
		var viewportRect = this.view().viewport();
		var unitRect = new $.ig.Rect(0, 0, 0, 1, 1);
		var effectiveViewportRect = this.getEffectiveViewportForUnitViewport(this.view());
		var xParams = new $.ig.ScalerParams(0, unitRect, unitRect, this.xAxis().isInverted(), effectiveViewportRect);
		var yParams = new $.ig.ScalerParams(0, unitRect, unitRect, this.yAxis().isInverted(), effectiveViewportRect);
		if (index >= 0 && $.ig.Rect.prototype.l_op_Inequality(windowRect, null) && $.ig.Rect.prototype.l_op_Inequality(viewportRect, null)) {
			if (this.xAxis() != null) {
				var cx = this.xAxis().getScaledValue(index, xParams);
				if (cx < windowRect.left() + 0.1 * windowRect.width()) {
					cx = cx + 0.4 * windowRect.width();
				}
				if (cx > windowRect.right() - 0.1 * windowRect.width()) {
					cx = cx - 0.4 * windowRect.width();
				}
				windowRect.x(cx - 0.5 * windowRect.width());
			}
			if (this.yAxis() != null && this.lowColumn() != null && this.highColumn() != null && index < this.lowColumn().count() && index < this.highColumn().count()) {
				var low = this.yAxis().getScaledValue(this.lowColumn().item(index), yParams);
				var high = this.yAxis().getScaledValue(this.highColumn().item(index), yParams);
				if (!$.ig.util.isNaN(low) && !$.ig.util.isNaN(high)) {
					var height = Math.abs(low - high);
					if (windowRect.height() < height) {
						windowRect.height(height);
						windowRect.y(Math.min(low, high));
					} else {
						if (low < windowRect.top() + 0.1 * windowRect.height()) {
							low = low + 0.4 * windowRect.height();
						}
						if (low > windowRect.bottom() - 0.1 * windowRect.height()) {
							low = low - 0.4 * windowRect.height();
						}
						windowRect.y(low - 0.5 * windowRect.height());
					}
				}
			}
			this.syncLink().windowNotify(this.seriesViewer(), windowRect);
		}
		return index >= 0;
	}
	,
	getRange: function (axis) {
		if (axis != null && axis == this.yAxis() && this.lowColumn() != null && this.highColumn() != null) {
			return new $.ig.AxisRange(this.lowColumn().minimum(), this.highColumn().maximum());
		}
		return null;
	}
	,
	_overlayValid: false,
	overlayValid: function (value) {
		if (arguments.length === 1) {
			this._overlayValid = value;
			return value;
		} else {
			return this._overlayValid;
		}
	}
	,
	validateOverlay: function () {
	}
	,
	$type: new $.ig.Type('FinancialOverlay', $.ig.FinancialSeries.prototype.$type)
}, true);

$.ig.util.defType('BollingerBandsOverlay', 'FinancialOverlay', {
	init: function () {
		this._averageColumn = new $.ig.List$1(Number, 0);
		this._deviationColumn = new $.ig.List$1(Number, 0);
		$.ig.FinancialOverlay.prototype.init.call(this);
		this.defaultStyleKey($.ig.BollingerBandsOverlay.prototype.$type);
		this._previousFrame = new $.ig.CategoryFrame(4);
		this._transitionFrame = new $.ig.CategoryFrame(4);
		this._currentFrame = new $.ig.CategoryFrame(4);
	},
	period: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.BollingerBandsOverlay.prototype.periodProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.BollingerBandsOverlay.prototype.periodProperty));
		}
	}
	,
	multiplier: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.BollingerBandsOverlay.prototype.multiplierProperty, value);
			return value;
		} else {
			return this.getValue($.ig.BollingerBandsOverlay.prototype.multiplierProperty);
		}
	}
	,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		if (this.getTypicalBasedOn().contains(propertyName)) {
			this.overlayValid(false);
		}
		$.ig.FinancialOverlay.prototype.propertyUpdatedOverride.call(this, sender, propertyName, oldValue, newValue);
		switch (propertyName) {
			case $.ig.BollingerBandsOverlay.prototype.periodPropertyName:
				this.overlayValid(false);
				this.renderSeries(false);
				break;
			case $.ig.BollingerBandsOverlay.prototype.multiplierPropertyName:
				this.renderSeries(false);
				break;
		}
	}
	,
	__maxBandWidth: 0,
	__minBandWidth: 0,
	validateOverlay: function () {
		this._averageColumn.clear();
		this._deviationColumn.clear();
		var sma = $.ig.Series.prototype.sMA(new $.ig.SafeEnumerable(this.typicalColumn()), this.period()).getEnumerator();
		var stdev = $.ig.Series.prototype.sTDEV(new $.ig.SafeEnumerable(this.typicalColumn()), this.period()).getEnumerator();
		this.__minBandWidth = 1.7976931348623157E+308;
		this.__maxBandWidth = -1.7976931348623157E+308;
		var moreSma = true;
		var moreStdev = true;
		var multiplier = this.multiplier();
		while (moreSma || moreStdev) {
			if (sma.moveNext()) {
				this._averageColumn.add(sma.current());
			} else {
				moreSma = false;
			}
			if (stdev.moveNext()) {
				this._deviationColumn.add(stdev.current());
			} else {
				moreStdev = false;
			}
			if (moreSma && moreStdev) {
				this.__minBandWidth = Math.min(this.__minBandWidth, sma.current() - stdev.current() * multiplier);
				this.__maxBandWidth = Math.max(this.__maxBandWidth, sma.current() + stdev.current() * multiplier);
			}
		}
		return true;
	}
	,
	getRange: function (axis) {
		if (this._averageColumn == null || this._deviationColumn == null || axis == null || this.fastItemsSource() == null || axis != this.yAxis()) {
			return null;
		}
		var range = new $.ig.AxisRange(this.__minBandWidth, this.__maxBandWidth);
		return range;
	}
	,
	_averageColumn: null,
	_deviationColumn: null,
	convertToSingle: function (value) {
		return value;
	}
	,
	prepareFrame: function (frame, view) {
		$.ig.FinancialOverlay.prototype.prepareFrame.call(this, frame, view);
		var windowRect = view.windowRect();
		var viewportRect = view.viewport();
		var effectiveViewportRect = this.getEffectiveViewport1(view);
		var xaxis = this.xAxis();
		var yaxis = this.yAxis();
		var xParams = new $.ig.ScalerParams(0, windowRect, viewportRect, xaxis.isInverted(), effectiveViewportRect);
		var yParams = new $.ig.ScalerParams(0, windowRect, viewportRect, yaxis.isInverted(), effectiveViewportRect);
		frame._buckets.clear();
		frame._markers.clear();
		frame._trend.clear();
		var sortingXAxis = $.ig.util.cast($.ig.ISortingAxis.prototype.$type, this.xAxis());
		if (sortingXAxis != null && sortingXAxis.sortedIndices().count() != this.fastItemsSource().count()) {
			return;
		}
		var offset = 0;
		offset = this.getOffset(windowRect, viewportRect, effectiveViewportRect);
		if (!this.overlayValid()) {
			this.overlayValid(this.validateOverlay());
			if (this.yAxis() != null) {
				this.yAxis().updateRange1(true);
			}
		}
		var singlePixelSpan = this.convertToSingle(this.xAxis().getUnscaledValue(2, xParams) - this.xAxis().getUnscaledValue(1, xParams));
		for (var i = view.bucketCalculator().firstBucket(); i <= view.bucketCalculator().lastBucket(); ++i) {
			var bucket;
			if (sortingXAxis == null) {
				bucket = view.bucketCalculator().getBucket(i);
			} else {
				var index = sortingXAxis.sortedIndices().__inner[i];
				var bucketX = sortingXAxis.getUnscaledValueAt(index);
				var bucketAverage = this.convertToSingle(this._averageColumn.__inner[i]);
				var bucketDeviation = this.convertToSingle(this._deviationColumn.__inner[i]);
				var currentAverage = bucketAverage;
				var currentDeviation = bucketDeviation;
				var currentX = bucketX;
				var counter = 1;
				while (i < view.bucketCalculator().lastBucket()) {
					index = sortingXAxis.sortedIndices().__inner[i + 1];
					currentX = sortingXAxis.getUnscaledValueAt(index);
					if (currentX - bucketX > singlePixelSpan) {
						break;
					}
					i++;
					currentAverage += this.convertToSingle(this._averageColumn.__inner[i]);
					currentDeviation += this.convertToSingle(this._deviationColumn.__inner[i]);
					counter++;
				}
				currentAverage /= counter;
				currentDeviation /= counter;
				var param0 = this.convertToSingle(currentAverage - currentDeviation * this.multiplier());
				var param1 = this.convertToSingle(currentAverage);
				var param2 = this.convertToSingle(currentAverage + currentDeviation * this.multiplier());
				var xVal = NaN;
				if (!$.ig.util.isNaN(bucketX)) {
					xVal = this.xAxis().getScaledValue(bucketX, xParams);
				}
				bucket = [ this.convertToSingle(xVal), param0, param1, param2 ];
			}
			var pp = Math.max(1, singlePixelSpan);
			if (!isNaN(bucket[0]) && i * pp >= this.ignoreFirst()) {
				if (this.xAxis() != null && $.ig.util.cast($.ig.ISortingAxis.prototype.$type, this.xAxis()) !== null) {
					bucket[0] = (bucket[0] + offset);
				} else {
					bucket[0] = (xaxis.getScaledValue(bucket[0], xParams) + offset);
				}
				bucket[1] = yaxis.getScaledValue(bucket[1], yParams);
				bucket[2] = yaxis.getScaledValue(bucket[2], yParams);
				bucket[3] = yaxis.getScaledValue(bucket[3], yParams);
				frame._buckets.add(bucket);
			}
		}
	}
	,
	clearRendering: function (wipeClean, view) {
		$.ig.FinancialOverlay.prototype.clearRendering.call(this, wipeClean, view);
		var bollingerView = view;
		if (bollingerView != null) {
			bollingerView.clearRendering();
		}
	}
	,
	getSeriesValue: function (world, useInterpolation, skipUnknowns) {
		if (this._averageColumn == null) {
			return $.ig.FinancialOverlay.prototype.getSeriesValue.call(this, world, useInterpolation, skipUnknowns);
		}
		var effectiveViewportRect = this.getEffectiveViewport1(this.view());
		var offset = this.getOffset(this.view().windowRect(), this.view().viewport(), effectiveViewportRect);
		var xParams = new $.ig.ScalerParams(0, this.view().windowRect(), this.view().viewport(), this.xAxis().isInverted(), effectiveViewportRect);
		xParams._effectiveViewportRect = this.seriesViewer().viewportRect();
		return this.getSeriesValueHelper(this._averageColumn, world, this.xAxis(), xParams, offset, this.getExactUnsortedItemIndex.runOn(this), useInterpolation, skipUnknowns);
	}
	,
	getPreviousOrExactIndex: function (world, skipUnknowns) {
		if (this._averageColumn == null) {
			return $.ig.FinancialOverlay.prototype.getPreviousOrExactIndex.call(this, world, skipUnknowns);
		}
		return this.getPreviousOrExactIndexHelper(world, skipUnknowns, this.xAxis(), this.getExactUnsortedItemIndex.runOn(this), this._averageColumn);
	}
	,
	getNextOrExactIndex: function (world, skipUnknowns) {
		if (this._averageColumn == null) {
			return $.ig.FinancialOverlay.prototype.getNextOrExactIndex.call(this, world, skipUnknowns);
		}
		return this.getNextOrExactIndexHelper(world, skipUnknowns, this.xAxis(), this.getExactUnsortedItemIndex.runOn(this), this._averageColumn);
	}
	,
	validateSeries: function (viewportRect, windowRect, view) {
		var isValid = $.ig.FinancialOverlay.prototype.validateSeries.call(this, viewportRect, windowRect, view);
		if (!this.validateBasedOn(this.getTypicalBasedOn())) {
			isValid = false;
		}
		return isValid;
	}
	,
	renderFrame: function (frame, view) {
		$.ig.FinancialOverlay.prototype.renderFrame.call(this, frame, view);
		var bollingerBandsView = $.ig.util.cast($.ig.BollingerBandsOverlayView.prototype.$type, view);
		var effectiveViewportRect = this.getEffectiveViewport1(view);
		if (view.checkFrameDirty(frame)) {
			bollingerBandsView.clearRendering();
			var count = frame._buckets.count();
			var px = function (i) { return frame._buckets.__inner[i][0]; };
			var nx = function (i) { return frame._buckets.__inner[count - 1 - i][0]; };
			var y0 = function (i) { return frame._buckets.__inner[i][1]; };
			var y1 = function (i) { return frame._buckets.__inner[i][2]; };
			var y2 = function (i) { return frame._buckets.__inner[count - 1 - i][3]; };
			bollingerBandsView.renderBands(count, px, nx, y0, y1, y2);
			view.updateFrameVersion(frame);
		}
		this._renderManager.initCategoryRenderSettings(this, this.shouldOverrideCategoryStyle(), this.xAxis(), this.getCategoryItems.runOn(this), this.getBucketSize(view), this.getFirstBucket(view));
		var areStylesOverriden = false;
		var args = this._renderManager.categoryOverrideArgs();
		var buckets = frame._buckets;
		var valueCount = this.fastItemsSource().count();
		if (args != null) {
			areStylesOverriden = true;
		}
		if (areStylesOverriden) {
			var xParams = new $.ig.ScalerParams(0, view.windowRect(), view.viewport(), this.xAxis().isInverted(), effectiveViewportRect);
			this.performCategoryStyleOverride(buckets, -1, valueCount, this.xAxis(), xParams, view.isThumbnailView());
		}
		var line0 = bollingerBandsView.line0();
		var line1 = bollingerBandsView.line1();
		var line2 = bollingerBandsView.line2();
		var polygon = bollingerBandsView.fillArea();
		this._renderManager.setCategoryShapeAppearance(line1, true, false, true, true);
		this._renderManager._initialRenderDashArray = null;
		this._renderManager._actualRenderDashArray = null;
		this._renderManager.setCategoryShapeAppearance(line0, true, false, false, true);
		this._renderManager.setCategoryShapeAppearance(line2, true, false, false, true);
		this._renderManager.setCategoryShapeAppearance(polygon, false, true, false, false);
		polygon.__opacity = this._renderManager._actualRenderOpacity * this.actualAreaFillOpacity();
	}
	,
	createView: function () {
		return new $.ig.BollingerBandsOverlayView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.FinancialOverlay.prototype.onViewCreated.call(this, view);
		this.bollingerBandsView($.ig.util.cast($.ig.BollingerBandsOverlayView.prototype.$type, view));
	}
	,
	_bollingerBandsView: null,
	bollingerBandsView: function (value) {
		if (arguments.length === 1) {
			this._bollingerBandsView = value;
			return value;
		} else {
			return this._bollingerBandsView;
		}
	}
	,
	$type: new $.ig.Type('BollingerBandsOverlay', $.ig.FinancialOverlay.prototype.$type)
}, true);

$.ig.util.defType('FinancialBucketCalculator', 'Object', {
	_view: null,
	view: function (value) {
		if (arguments.length === 1) {
			this._view = value;
			return value;
		} else {
			return this._view;
		}
	}
	,
	init: function (view) {
		$.ig.Object.prototype.init.call(this);
		if (view == null) {
			throw new $.ig.ArgumentNullException(0, "view");
		}
		this.view(view);
		this.firstBucket(-1);
		this.lastBucket(this.lastBucket());
		this.bucketSize(0);
	},
	getBucket: function (index) {
		return null;
	}
	,
	getErrorBucket: function (index, column) {
		return NaN;
	}
	,
	_firstBucket: 0,
	firstBucket: function (value) {
		if (arguments.length === 1) {
			this._firstBucket = value;
			return value;
		} else {
			return this._firstBucket;
		}
	}
	,
	_lastBucket: 0,
	lastBucket: function (value) {
		if (arguments.length === 1) {
			this._lastBucket = value;
			return value;
		} else {
			return this._lastBucket;
		}
	}
	,
	_bucketSize: 0,
	bucketSize: function (value) {
		if (arguments.length === 1) {
			this._bucketSize = value;
			return value;
		} else {
			return this._bucketSize;
		}
	}
	,
	getBucketInfo: function (firstBucket, lastBucket, bucketSize, resolution) {
		firstBucket = this.firstBucket();
		lastBucket = this.lastBucket();
		bucketSize = this.bucketSize();
		resolution = this.view().financialModel().resolution();
		return {
			p0: firstBucket,
			p1: lastBucket,
			p2: bucketSize,
			p3: resolution
		};
	}
	,
	calculateBuckets: function (resolution) {
		var windowRect = this.view().windowRect();
		var viewportRect = this.view().viewport();
		var effectiveViewportRect = this.view().viewport();
		if (this.view().financialModel() != null) {
			effectiveViewportRect = this.view().financialModel().getEffectiveViewport1(this.view());
		}
		if (windowRect.isEmpty() || viewportRect.isEmpty() || this.view().financialModel().xAxis() == null) {
			this.bucketSize(0);
			return;
		}
		var xIsInverted = (this.view().financialModel().xAxis() != null) ? this.view().financialModel().xAxis().isInverted() : false;
		var xParams = new $.ig.ScalerParams(0, windowRect, viewportRect, xIsInverted, effectiveViewportRect);
		var contentViewport = this.view().financialModel().getContentViewport(this.view());
		var sortingXAxis = $.ig.util.cast($.ig.ISortingAxis.prototype.$type, this.view().financialModel().xAxis());
		if (sortingXAxis == null || sortingXAxis.sortedIndices() == null) {
			var x0 = Math.floor(this.view().financialModel().xAxis().getUnscaledValue(contentViewport.left(), xParams));
			var x1 = Math.ceil(this.view().financialModel().xAxis().getUnscaledValue(contentViewport.right(), xParams));
			if (this.view().financialModel().xAxis().isInverted()) {
				x1 = Math.ceil(this.view().financialModel().xAxis().getUnscaledValue(contentViewport.left(), xParams));
				x0 = Math.floor(this.view().financialModel().xAxis().getUnscaledValue(contentViewport.right(), xParams));
			}
			var c = Math.floor((x1 - x0 + 1) * resolution / contentViewport.width());
			this.bucketSize($.ig.truncate(Math.max(1, c)));
			this.firstBucket($.ig.truncate(Math.floor(x0 / this.bucketSize())));
			this.lastBucket($.ig.truncate(Math.ceil(x1 / this.bucketSize())));
		} else {
			this.firstBucket(sortingXAxis.getFirstVisibleIndex(windowRect, viewportRect, effectiveViewportRect, contentViewport));
			this.lastBucket(sortingXAxis.getLastVisibleIndex(windowRect, viewportRect, effectiveViewportRect, contentViewport));
			this.bucketSize(1);
		}
	}
	,
	cacheValues: function () {
	}
	,
	unCacheValues: function () {
	}
	,
	$type: new $.ig.Type('FinancialBucketCalculator', $.ig.Object.prototype.$type, [$.ig.IBucketizer.prototype.$type])
}, true);

$.ig.util.defType('BollingerBandsBucketCalculator', 'FinancialBucketCalculator', {
	init: function (view) {
		$.ig.FinancialBucketCalculator.prototype.init.call(this, view);
		this.bollingerView(view);
	},
	_bollingerView: null,
	bollingerView: function (value) {
		if (arguments.length === 1) {
			this._bollingerView = value;
			return value;
		} else {
			return this._bollingerView;
		}
	}
	,
	getBucket: function (index) {
		var i0 = index * this.bucketSize();
		var i1 = Math.min(i0 + this.bucketSize() - 1, this.view().financialModel().fastItemsSource().count() - 1);
		if (i0 <= i1) {
			var multiplier = this.bollingerView().bollingerBandsOverlayModel().multiplier();
			var average = 0;
			var deviation = 0;
			var cnt = 0;
			for (var i = i0; i <= i1; ++i) {
				if (!$.ig.util.isNaN(this.bollingerView().bollingerBandsOverlayModel()._averageColumn.__inner[i]) && !$.ig.util.isNaN(this.bollingerView().bollingerBandsOverlayModel()._deviationColumn.__inner[i])) {
					average += this.bollingerView().bollingerBandsOverlayModel()._averageColumn.__inner[i];
					deviation += this.bollingerView().bollingerBandsOverlayModel()._deviationColumn.__inner[i];
					++cnt;
				}
			}
			if (cnt > 0) {
				average = average / cnt;
				deviation = deviation / cnt;
				return [ (0.5 * (i0 + i1)), (average - deviation * multiplier), (average), (average + deviation * multiplier) ];
			}
		}
		return [ NaN, NaN, NaN, NaN ];
	}
	,
	$type: new $.ig.Type('BollingerBandsBucketCalculator', $.ig.FinancialBucketCalculator.prototype.$type)
}, true);

$.ig.util.defType('BollingerBandWidthIndicator', 'StrategyBasedIndicator', {
	init: function () {
		$.ig.StrategyBasedIndicator.prototype.init.call(this);
	},
	calculationStrategy: function () {
		return new $.ig.BollingerBandWidthIndicatorStrategy();
	}
	,
	styleKeyType: function () {
		return $.ig.BollingerBandWidthIndicator.prototype.$type;
	}
	,
	period: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.BollingerBandWidthIndicator.prototype.periodProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.BollingerBandWidthIndicator.prototype.periodProperty));
		}
	}
	,
	periodOverride: function () {
		return this.period();
	}
	,
	multiplier: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.BollingerBandWidthIndicator.prototype.multiplierProperty, value);
			return value;
		} else {
			return this.getValue($.ig.BollingerBandWidthIndicator.prototype.multiplierProperty);
		}
	}
	,
	multiplierOverride: function () {
		return this.multiplier();
	}
	,
	$type: new $.ig.Type('BollingerBandWidthIndicator', $.ig.StrategyBasedIndicator.prototype.$type)
}, true);

$.ig.util.defType('BollingerBandWidthIndicatorStrategy', 'IndicatorCalculationStrategy', {
	init: function () {
		$.ig.IndicatorCalculationStrategy.prototype.init.call(this);
	},
	basedOn: function (dataSource, supportingCalculations) {
		var list = new $.ig.List$1(String, 0);
		list.addRange(dataSource.typicalColumn().basedOn());
		list.addRange(supportingCalculations.sMA().basedOn());
		list.addRange(supportingCalculations.sTDEV().basedOn());
		return list;
	}
	,
	calculateIndicator: function (dataSource, supportingCalculations) {
		var sma = supportingCalculations.sMA().strategy()(dataSource.typicalColumn(), dataSource.period()).getEnumerator();
		var stdev = supportingCalculations.sTDEV().strategy()(dataSource.typicalColumn(), dataSource.period()).getEnumerator();
		var multiplier = dataSource.multiplier();
		var indicatorColumn = dataSource.indicatorColumn();
		var i = 0;
		while (sma.moveNext() && stdev.moveNext()) {
			var offset = stdev.current() * multiplier;
			var upperBand = sma.current() + offset;
			var lowerBand = sma.current() - offset;
			var middleBand = sma.current();
			var bandWidth = supportingCalculations.makeSafe()((upperBand - lowerBand) / middleBand);
			indicatorColumn.item(i, bandWidth);
			i++;
		}
		return true;
	}
	,
	$type: new $.ig.Type('BollingerBandWidthIndicatorStrategy', $.ig.IndicatorCalculationStrategy.prototype.$type)
}, true);

$.ig.util.defType('ChaikinOscillatorIndicator', 'StrategyBasedIndicator', {
	init: function () {
		$.ig.StrategyBasedIndicator.prototype.init.call(this);
	},
	calculationStrategy: function () {
		return new $.ig.ChaikinOscillatorIndicatorStrategy();
	}
	,
	styleKeyType: function () {
		return $.ig.ChaikinOscillatorIndicator.prototype.$type;
	}
	,
	shortPeriod: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.ChaikinOscillatorIndicator.prototype.shortPeriodProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.ChaikinOscillatorIndicator.prototype.shortPeriodProperty));
		}
	}
	,
	shortPeriodOverride: function () {
		return this.shortPeriod();
	}
	,
	longPeriod: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.ChaikinOscillatorIndicator.prototype.longPeriodProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.ChaikinOscillatorIndicator.prototype.longPeriodProperty));
		}
	}
	,
	longPeriodOverride: function () {
		return this.longPeriod();
	}
	,
	$type: new $.ig.Type('ChaikinOscillatorIndicator', $.ig.StrategyBasedIndicator.prototype.$type)
}, true);

$.ig.util.defType('ChaikinOscillatorIndicatorStrategy', 'IndicatorCalculationStrategy', {
	_accumulationDistributionStrategy: null,
	accumulationDistributionStrategy: function (value) {
		if (arguments.length === 1) {
			this._accumulationDistributionStrategy = value;
			return value;
		} else {
			return this._accumulationDistributionStrategy;
		}
	}
	,
	init: function () {
		$.ig.IndicatorCalculationStrategy.prototype.init.call(this);
		this.accumulationDistributionStrategy(new $.ig.AccumulationDistributionIndicatorStrategy());
	},
	basedOn: function (dataSource, supportingCalculations) {
		var list = new $.ig.List$1(String, 0);
		list.addRange(this.accumulationDistributionStrategy().basedOn(dataSource, supportingCalculations));
		list.addRange(supportingCalculations.eMA().basedOn());
		return list;
	}
	,
	calculateIndicator: function (dataSource, supportingCalculations) {
		var indicatorColumn = dataSource.indicatorColumn();
		var accDist = this.accumulationDistributionStrategy().provideStream(dataSource, supportingCalculations);
		var emaShort = supportingCalculations.eMA().strategy()(accDist, dataSource.shortPeriod()).getEnumerator();
		var emaLong = supportingCalculations.eMA().strategy()(accDist, dataSource.longPeriod()).getEnumerator();
		var i = 0;
		while (emaShort.moveNext() && emaLong.moveNext()) {
			var indicatorValue = emaShort.current() - emaLong.current();
			indicatorColumn.item(i, indicatorValue);
			i++;
		}
		return true;
	}
	,
	$type: new $.ig.Type('ChaikinOscillatorIndicatorStrategy', $.ig.IndicatorCalculationStrategy.prototype.$type)
}, true);

$.ig.util.defType('ChaikinVolatilityIndicator', 'StrategyBasedIndicator', {
	init: function () {
		$.ig.StrategyBasedIndicator.prototype.init.call(this);
	},
	calculationStrategy: function () {
		return new $.ig.ChaikinVolatilityIndicatorStrategy();
	}
	,
	styleKeyType: function () {
		return $.ig.ChaikinVolatilityIndicator.prototype.$type;
	}
	,
	period: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.ChaikinVolatilityIndicator.prototype.periodProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.ChaikinVolatilityIndicator.prototype.periodProperty));
		}
	}
	,
	periodOverride: function () {
		return this.period();
	}
	,
	$type: new $.ig.Type('ChaikinVolatilityIndicator', $.ig.StrategyBasedIndicator.prototype.$type)
}, true);

$.ig.util.defType('ChaikinVolatilityIndicatorStrategy', 'IndicatorCalculationStrategy', {
	init: function () {
		$.ig.IndicatorCalculationStrategy.prototype.init.call(this);
	},
	basedOn: function (dataSource, supportingCalculations) {
		var list = new $.ig.List$1(String, 0);
		list.add($.ig.FinancialSeries.prototype.highColumnPropertyName);
		list.add($.ig.FinancialSeries.prototype.lowColumnPropertyName);
		list.addRange(supportingCalculations.eMA().basedOn());
		return list;
	}
	,
	calculateIndicator: function (dataSource, supportingCalculations) {
		var highMinusLow = supportingCalculations.toEnumerable()(function (index) { return dataSource.highColumn().item(index) - dataSource.lowColumn().item(index); }, dataSource.count());
		var emaHighLow = supportingCalculations.eMA().strategy()(highMinusLow, dataSource.period()).getEnumerator();
		var indicatorColumn = dataSource.indicatorColumn();
		var buffer = new Array(dataSource.period());
		for (var j = 0; j < dataSource.period(); j++) {
			buffer[j] = 0;
		}
		var i = 0;
		while (emaHighLow.moveNext()) {
			var cursor = i % dataSource.period();
			var chaikinVolatility = supportingCalculations.makeSafe()((emaHighLow.current() - buffer[cursor]) / (buffer[cursor] * 100));
			if (i < dataSource.period()) {
				indicatorColumn.item(i, 0);
			} else {
				indicatorColumn.item(i, chaikinVolatility);
			}
			buffer[cursor] = emaHighLow.current();
			i++;
		}
		return true;
	}
	,
	$type: new $.ig.Type('ChaikinVolatilityIndicatorStrategy', $.ig.IndicatorCalculationStrategy.prototype.$type)
}, true);

$.ig.util.defType('CommodityChannelIndexIndicator', 'StrategyBasedIndicator', {
	init: function () {
		$.ig.StrategyBasedIndicator.prototype.init.call(this);
	},
	calculationStrategy: function () {
		return new $.ig.CommodityChannelIndexIndicatorStrategy();
	}
	,
	styleKeyType: function () {
		return $.ig.CommodityChannelIndexIndicator.prototype.$type;
	}
	,
	period: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.CommodityChannelIndexIndicator.prototype.periodProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.CommodityChannelIndexIndicator.prototype.periodProperty));
		}
	}
	,
	periodOverride: function () {
		return this.period();
	}
	,
	$type: new $.ig.Type('CommodityChannelIndexIndicator', $.ig.StrategyBasedIndicator.prototype.$type)
}, true);

$.ig.util.defType('CommodityChannelIndexIndicatorStrategy', 'IndicatorCalculationStrategy', {
	init: function () {
		$.ig.IndicatorCalculationStrategy.prototype.init.call(this);
	},
	basedOn: function (dataSource, supportingCalculations) {
		var list = new $.ig.List$1(String, 0);
		list.addRange(dataSource.typicalColumn().basedOn());
		list.addRange(supportingCalculations.sMA().basedOn());
		return list;
	}
	,
	calculateIndicator: function (dataSource, supportingCalculations) {
		var period = dataSource.period();
		var typicalColumn = dataSource.typicalColumn();
		var indicatorColumn = dataSource.indicatorColumn();
		var sma = supportingCalculations.sMA().strategy()(typicalColumn, period).getEnumerator();
		var price = typicalColumn.getEnumerator();
		var buffer = new Array(period);
		for (var j = 0; j < period; j++) {
			buffer[j] = 0;
		}
		var i = 0;
		while (price.moveNext() && sma.moveNext()) {
			buffer[i % period] = price.current();
			var mad = 0;
			for (var j1 = 0; j1 < period; ++j1) {
				mad += Math.abs(sma.current() - buffer[j1]);
			}
			mad /= period;
			indicatorColumn.item(i, supportingCalculations.makeSafe()((price.current() - sma.current()) / (0.015 * mad)));
			++i;
		}
		return true;
	}
	,
	$type: new $.ig.Type('CommodityChannelIndexIndicatorStrategy', $.ig.IndicatorCalculationStrategy.prototype.$type)
}, true);

$.ig.util.defType('CustomIndicator', 'StrategyBasedIndicator', {
	init: function () {
		$.ig.StrategyBasedIndicator.prototype.init.call(this);
	},
	calculationStrategy: function () {
		return new $.ig.CustomIndicatorStrategy();
	}
	,
	styleKeyType: function () {
		return $.ig.CustomIndicator.prototype.$type;
	}
	,
	basedOn: function (position, count) {
		var dataSource = this.provideDataSource(position, count);
		var supportingCalculations = this.provideSupportingCalculations(dataSource);
		var list = new $.ig.List$1(String, 0);
		if (this.__basedOnColumns != null) {
			var args = new $.ig.FinancialEventArgs(dataSource.calculateFrom(), dataSource.calculateCount(), dataSource, supportingCalculations);
			this.__basedOnColumns(this, args);
			if (args.basedOn() != null) {
				var en = args.basedOn().getEnumerator();
				while (en.moveNext()) {
					var propertyName = en.current();
					list.add(propertyName);
				}
			}
		}
		return list;
	}
	,
	__indicator: null,
	indicator: function (value) {
		if (arguments.length === 1) {
			this.__indicator = value;
			this.fullIndicatorRefresh();
			return value;
		} else {
			return this.__indicator;
		}
	}
	,
	__basedOnColumns: null,
	basedOnColumns: function (value) {
		if (arguments.length === 1) {
			this.__basedOnColumns = value;
			return value;
		} else {
			return this.__basedOnColumns;
		}
	}
	,
	indicatorOverride: function (position, count) {
		$.ig.StrategyBasedIndicator.prototype.indicatorOverride.call(this, position, count);
		if (this.__indicator != null) {
			var dataSource = this.provideDataSource(position, count);
			if (count == 0) {
				return false;
			}
			if (!this.validateBasedOn(this.basedOn(position, count))) {
				return false;
			}
			this.__indicator(this, new $.ig.FinancialEventArgs(position, count, dataSource, this.provideSupportingCalculations(dataSource)));
			if (this.updateRange(dataSource) && this.yAxis() != null) {
				this.yAxis().updateRange();
			}
			return true;
		}
		return false;
	}
	,
	$type: new $.ig.Type('CustomIndicator', $.ig.StrategyBasedIndicator.prototype.$type)
}, true);

$.ig.util.defType('CustomIndicatorStrategy', 'IndicatorCalculationStrategy', {
	init: function () {
		$.ig.IndicatorCalculationStrategy.prototype.init.call(this);
	},
	calculateIndicator: function (dataSource, supportingCalculations) {
		return true;
	}
	,
	basedOn: function (dataSource, supportingCalculations) {
		return new $.ig.List$1(String, 0);
	}
	,
	$type: new $.ig.Type('CustomIndicatorStrategy', $.ig.IndicatorCalculationStrategy.prototype.$type)
}, true);

$.ig.util.defType('DetrendedPriceOscillatorIndicator', 'StrategyBasedIndicator', {
	init: function () {
		$.ig.StrategyBasedIndicator.prototype.init.call(this);
	},
	calculationStrategy: function () {
		return new $.ig.DetrendedPriceOscillatorIndicatorStrategy();
	}
	,
	styleKeyType: function () {
		return $.ig.DetrendedPriceOscillatorIndicator.prototype.$type;
	}
	,
	period: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.DetrendedPriceOscillatorIndicator.prototype.periodProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.DetrendedPriceOscillatorIndicator.prototype.periodProperty));
		}
	}
	,
	periodOverride: function () {
		return this.period();
	}
	,
	$type: new $.ig.Type('DetrendedPriceOscillatorIndicator', $.ig.StrategyBasedIndicator.prototype.$type)
}, true);

$.ig.util.defType('DetrendedPriceOscillatorIndicatorStrategy', 'IndicatorCalculationStrategy', {
	init: function () {
		$.ig.IndicatorCalculationStrategy.prototype.init.call(this);
	},
	basedOn: function (dataSource, supportingCalculations) {
		var list = new $.ig.List$1(String, 0);
		list.add($.ig.FinancialSeries.prototype.closeColumnPropertyName);
		list.addRange(supportingCalculations.sMA().basedOn());
		return list;
	}
	,
	calculateIndicator: function (dataSource, supportingCalculations) {
		var period = dataSource.period();
		var closeColumn = dataSource.closeColumn();
		var indicatorColumn = dataSource.indicatorColumn();
		var sma = supportingCalculations.sMA().strategy()(closeColumn, period).getEnumerator();
		var daysAgo = ($.ig.intDivide(period, 2)) + 1;
		var buffer = new Array(daysAgo);
		for (var j = 0; j < daysAgo; j++) {
			buffer[j] = 0;
		}
		for (var i = 1; i < Math.min(daysAgo + 1, indicatorColumn.count()); ++i) {
			var cursor = i % daysAgo;
			indicatorColumn.item(i, 0);
			sma.moveNext();
			buffer[cursor] = sma.current();
		}
		for (var i1 = daysAgo + 1; i1 < indicatorColumn.count(); i1++) {
			var cursor1 = i1 % daysAgo;
			indicatorColumn.item(i1, closeColumn.item(i1) - buffer[cursor1]);
			sma.moveNext();
			buffer[cursor1] = sma.current();
		}
		return true;
	}
	,
	$type: new $.ig.Type('DetrendedPriceOscillatorIndicatorStrategy', $.ig.IndicatorCalculationStrategy.prototype.$type)
}, true);

$.ig.util.defType('EaseOfMovementIndicator', 'StrategyBasedIndicator', {
	init: function () {
		$.ig.StrategyBasedIndicator.prototype.init.call(this);
	},
	calculationStrategy: function () {
		return new $.ig.EaseOfMovementIndicatorStrategy();
	}
	,
	styleKeyType: function () {
		return $.ig.EaseOfMovementIndicator.prototype.$type;
	}
	,
	$type: new $.ig.Type('EaseOfMovementIndicator', $.ig.StrategyBasedIndicator.prototype.$type)
}, true);

$.ig.util.defType('EaseOfMovementIndicatorStrategy', 'IndicatorCalculationStrategy', {
	init: function () {
		$.ig.IndicatorCalculationStrategy.prototype.init.call(this);
	},
	basedOn: function (dataSource, supportingCalculations) {
		var list = new $.ig.List$1(String, 0);
		list.add($.ig.FinancialSeries.prototype.highColumnPropertyName);
		list.add($.ig.FinancialSeries.prototype.lowColumnPropertyName);
		list.add($.ig.FinancialSeries.prototype.volumeColumnPropertyName);
		return list;
	}
	,
	calculateIndicator: function (dataSource, supportingCalculations) {
		var indicatorColumn = dataSource.indicatorColumn();
		var highColumn = dataSource.highColumn();
		var lowColumn = dataSource.lowColumn();
		var volumeColumn = dataSource.volumeColumn();
		var count = dataSource.count();
		if (count > 0) {
			indicatorColumn.item(0, 0);
		}
		for (var i = 1; i < count; i++) {
			var todayMidPoint = (highColumn.item(i) + lowColumn.item(i)) / 2;
			var yesterdayMidPoint = (highColumn.item(i - 1) + lowColumn.item(i - 1)) / 2;
			var midPointMove = todayMidPoint - yesterdayMidPoint;
			var volumeScalingFactor = 10000;
			var boxRatio = supportingCalculations.makeSafe()((volumeColumn.item(i) / volumeScalingFactor) / (highColumn.item(i) - lowColumn.item(i)));
			indicatorColumn.item(i, supportingCalculations.makeSafe()(midPointMove / boxRatio));
		}
		return true;
	}
	,
	$type: new $.ig.Type('EaseOfMovementIndicatorStrategy', $.ig.IndicatorCalculationStrategy.prototype.$type)
}, true);

$.ig.util.defType('FastStochasticOscillatorIndicator', 'StrategyBasedIndicator', {
	init: function () {
		$.ig.StrategyBasedIndicator.prototype.init.call(this);
		this.trendLineBrush((function () {
			var $ret = new $.ig.Brush();
			$ret.fill("Blue");
			return $ret;
		}()));
		this.trendLineType($.ig.TrendLineType.prototype.exponentialAverage);
		this.trendLinePeriod(3);
	},
	calculationStrategy: function () {
		return new $.ig.FastStochasticOscillatorIndicatorStrategy();
	}
	,
	styleKeyType: function () {
		return $.ig.FastStochasticOscillatorIndicator.prototype.$type;
	}
	,
	period: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.FastStochasticOscillatorIndicator.prototype.periodProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.FastStochasticOscillatorIndicator.prototype.periodProperty));
		}
	}
	,
	periodOverride: function () {
		return this.period();
	}
	,
	$type: new $.ig.Type('FastStochasticOscillatorIndicator', $.ig.StrategyBasedIndicator.prototype.$type)
}, true);

$.ig.util.defType('FastStochasticOscillatorIndicatorStrategy', 'IndicatorCalculationStrategy', {
	_percentKStrategy: null,
	percentKStrategy: function (value) {
		if (arguments.length === 1) {
			this._percentKStrategy = value;
			return value;
		} else {
			return this._percentKStrategy;
		}
	}
	,
	init: function () {
		$.ig.IndicatorCalculationStrategy.prototype.init.call(this);
		this.percentKStrategy(new $.ig.PercentKCalculationStrategy());
	},
	basedOn: function (dataSource, supportingCalculations) {
		var list = new $.ig.List$1(String, 0);
		list.addRange(this.percentKStrategy().basedOn(dataSource, supportingCalculations));
		return list;
	}
	,
	calculateIndicator: function (dataSource, supportingCalculations) {
		var percentK = this.percentKStrategy().provideStream(dataSource, supportingCalculations);
		var i = 0;
		var en = percentK.getEnumerator();
		while (en.moveNext()) {
			var value = en.current();
			dataSource.indicatorColumn().item(i, value);
			i++;
		}
		return true;
	}
	,
	$type: new $.ig.Type('FastStochasticOscillatorIndicatorStrategy', $.ig.IndicatorCalculationStrategy.prototype.$type)
}, true);

$.ig.util.defType('FinancialPriceSeries', 'FinancialSeries', {
	createView: function () {
		return new $.ig.FinancialPriceSeriesView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.FinancialSeries.prototype.onViewCreated.call(this, view);
		this.financialPriceView(view);
	}
	,
	_financialPriceView: null,
	financialPriceView: function (value) {
		if (arguments.length === 1) {
			this._financialPriceView = value;
			return value;
		} else {
			return this._financialPriceView;
		}
	}
	,
	init: function () {
		$.ig.FinancialSeries.prototype.init.call(this);
		this.defaultStyleKey($.ig.FinancialPriceSeries.prototype.$type);
		this._previousFrame = new $.ig.CategoryFrame(5);
		this._transitionFrame = new $.ig.CategoryFrame(5);
		this._currentFrame = new $.ig.CategoryFrame(5);
	},
	trendLineType: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.FinancialPriceSeries.prototype.trendLineTypeProperty, $.ig.TrendLineType.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.FinancialPriceSeries.prototype.trendLineTypeProperty));
		}
	}
	,
	trendLineBrush: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.FinancialPriceSeries.prototype.trendLineBrushProperty, value);
			return value;
		} else {
			return this.getValue($.ig.FinancialPriceSeries.prototype.trendLineBrushProperty);
		}
	}
	,
	actualTrendLineBrush: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.FinancialPriceSeries.prototype.actualTrendLineBrushProperty, value);
			return value;
		} else {
			return this.getValue($.ig.FinancialPriceSeries.prototype.actualTrendLineBrushProperty);
		}
	}
	,
	trendLineThickness: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.FinancialPriceSeries.prototype.trendLineThicknessProperty, value);
			return value;
		} else {
			return this.getValue($.ig.FinancialPriceSeries.prototype.trendLineThicknessProperty);
		}
	}
	,
	trendLineDashCap: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.FinancialPriceSeries.prototype.trendLineDashCapProperty, $.ig.PenLineCap.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.FinancialPriceSeries.prototype.trendLineDashCapProperty));
		}
	}
	,
	trendLineDashArray: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.FinancialPriceSeries.prototype.trendLineDashArrayProperty, value);
			return value;
		} else {
			return this.getValue($.ig.FinancialPriceSeries.prototype.trendLineDashArrayProperty);
		}
	}
	,
	trendLinePeriod: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.FinancialPriceSeries.prototype.trendLinePeriodProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.FinancialPriceSeries.prototype.trendLinePeriodProperty));
		}
	}
	,
	trendLineZIndex: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.FinancialPriceSeries.prototype.trendLineZIndexProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.FinancialPriceSeries.prototype.trendLineZIndexProperty));
		}
	}
	,
	dataUpdatedOverride: function (action, position, count, propertyName) {
		if (this.xAxis() != null && $.ig.util.cast($.ig.ISortingAxis.prototype.$type, this.xAxis()) !== null) {
			(this.xAxis()).notifyDataChanged();
		}
		this.financialPriceView().bucketCalculator().calculateBuckets(this.resolution());
		this.financialPriceView().trendLineManager().dataUpdated(action, position, count, propertyName);
		if (this.yAxis() != null) {
			this.yAxis().updateRange();
		}
		this.renderSeries(true);
	}
	,
	displayType: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.FinancialPriceSeries.prototype.displayTypeProperty, $.ig.PriceDisplayType.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.FinancialPriceSeries.prototype.displayTypeProperty));
		}
	}
	,
	getResolvedHitTestMode: function () {
		if (this.hitTestMode() == $.ig.SeriesHitTestMode.prototype.auto) {
			return $.ig.SeriesHitTestMode.prototype.computational;
		} else {
			return $.ig.FinancialSeries.prototype.getResolvedHitTestMode.call(this);
		}
	}
	,
	getMatchingBuckets1: function (world, skipUnknowns) {
		var previousIndex = this.getPreviousOrExactIndex(world, skipUnknowns);
		var nextIndex = this.getNextOrExactIndex(world, skipUnknowns);
		var prevBucket = $.ig.truncate(Math.floor(previousIndex / this.getBucketSize(this.view())));
		var nextBucket = $.ig.truncate(Math.floor(nextIndex / this.getBucketSize(this.view())));
		var prevBucketIndex = prevBucket - this.getFirstBucket(this.view());
		var nextBucketIndex = nextBucket - this.getFirstBucket(this.view());
		if (prevBucketIndex < 0) {
			prevBucketIndex = 0;
		}
		if (prevBucketIndex > this._currentFrame._buckets.count() - 1) {
			prevBucketIndex = this._currentFrame._buckets.count() - 1;
		}
		if (nextBucketIndex < 0) {
			nextBucketIndex = 0;
		}
		if (nextBucketIndex > this._currentFrame._buckets.count() - 1) {
			nextBucketIndex = this._currentFrame._buckets.count() - 1;
		}
		var prevBucketValue = this._currentFrame._buckets.__inner[prevBucketIndex];
		var nextBucketValue = this._currentFrame._buckets.__inner[nextBucketIndex];
		var ret = new Array(2);
		ret[0] = prevBucketValue;
		ret[1] = nextBucketValue;
		return ret;
	}
	,
	getSeriesValueBoundingBox: function (world) {
		var pos = this.fromWorldPosition(world);
		var windowRect = this.view().windowRect();
		var viewportRect = this.view().viewport();
		var effectiveViewportRect = this.getEffectiveViewport1(this.view());
		var matching = this.getMatchingBuckets1(world, true);
		var prevBucketValue = matching[0];
		var nextBucketValue = matching[1];
		var prevDist = Math.abs(pos.__x - prevBucketValue[0]);
		var nextDist = Math.abs(pos.__x - nextBucketValue[0]);
		var width = this.xAxis().getGroupSize(windowRect, viewportRect, effectiveViewportRect) / 2;
		width = Math.max(width, $.ig.FinancialPriceSeries.prototype._mIN_WIDTH);
		if (prevDist < nextDist) {
			var left = prevBucketValue[0] - width;
			var center = prevBucketValue[0];
			var right = prevBucketValue[0] + width;
			var open = prevBucketValue[1];
			var high = prevBucketValue[2];
			var low = prevBucketValue[3];
			var close = prevBucketValue[4];
			var min = Math.min(Math.min(Math.min(open, high), low), close);
			var max = Math.max(Math.max(Math.max(open, high), low), close);
			return new $.ig.Rect(0, left, min, Math.abs(right - left), max - min);
		} else {
			var left1 = nextBucketValue[0] - width;
			var center1 = nextBucketValue[0];
			var right1 = nextBucketValue[0] + width;
			var open1 = nextBucketValue[1];
			var high1 = nextBucketValue[2];
			var low1 = nextBucketValue[3];
			var close1 = nextBucketValue[4];
			var min1 = Math.min(Math.min(Math.min(open1, high1), low1), close1);
			var max1 = Math.max(Math.max(Math.max(open1, high1), low1), close1);
			return new $.ig.Rect(0, left1, min1, Math.abs(right1 - left1), max1 - min1);
		}
	}
	,
	getFineGrainedBoxes: function (bucket, width) {
		var left = bucket[0] - width;
		var center = bucket[0];
		var right = bucket[0] + width;
		var open = bucket[1];
		var high = bucket[2];
		var low = bucket[3];
		var close = bucket[4];
		var min = Math.min(Math.min(Math.min(open, high), low), close);
		var max = Math.max(Math.max(Math.max(open, high), low), close);
		var stickTop = Math.min(high, low);
		var stickBottom = Math.max(high, low);
		var candleTop = Math.min(open, close);
		var candleBottom = Math.max(open, close);
		if (this.displayType() == $.ig.PriceDisplayType.prototype.oHLC) {
			var boxes = new Array(3);
			var openBox = new $.ig.Rect(0, left, open, width, 0);
			var closeBox = new $.ig.Rect(0, center, close, width, 0);
			var stickBox = new $.ig.Rect(0, center, stickTop, 0, stickBottom - stickTop);
			boxes[0] = openBox;
			boxes[1] = closeBox;
			boxes[2] = stickBox;
			return boxes;
		} else {
			var boxes1 = new Array(2);
			var wickBox = new $.ig.Rect(0, center, stickTop, 0, stickBottom - stickTop);
			var candleBox = new $.ig.Rect(0, left, candleTop, width * 2, candleBottom - candleTop);
			boxes1[0] = wickBox;
			boxes1[1] = candleBox;
			return boxes1;
		}
	}
	,
	getSeriesValueFineGrainedBoundingBoxes: function (world) {
		var boxes = new $.ig.List$1($.ig.Rect.prototype.$type, 0);
		var pos = this.fromWorldPosition(world);
		var windowRect = this.view().windowRect();
		var viewportRect = this.view().viewport();
		var effectiveViewportRect = this.getEffectiveViewport1(this.view());
		var matching = this.getMatchingBuckets1(world, true);
		var prevBucketValue = matching[0];
		var nextBucketValue = matching[1];
		var prevDist = Math.abs(pos.__x - prevBucketValue[0]);
		var nextDist = Math.abs(pos.__x - nextBucketValue[0]);
		var width = this.xAxis().getGroupSize(windowRect, viewportRect, effectiveViewportRect) / 2;
		width = Math.max(width, $.ig.FinancialPriceSeries.prototype._mIN_WIDTH);
		if (prevDist < nextDist) {
			return this.getFineGrainedBoxes(prevBucketValue, width);
		} else {
			return this.getFineGrainedBoxes(nextBucketValue, width);
		}
	}
	,
	testHit: function (position, isFinger) {
		if (this.testShapesOver(position, isFinger)) {
			return true;
		}
		return false;
	}
	,
	testShapesOver: function (position, isFinger) {
		var box = this.getSeriesValueBoundingBoxFromSeriesPixel(position);
		if (box.isEmpty()) {
			return false;
		}
		var inflated = $.ig.RectUtil.prototype.inflate(box, (this.thickness() / 2) + this.getHitTestAllowance(isFinger));
		if (inflated.containsPoint(position)) {
			var boxes = this.getSeriesValueFineGrainedBoundingBoxesFromSeriesPixel(position);
			if (boxes != null) {
				for (var i = 0; i < boxes.length; i++) {
					var fineBox = boxes[i];
					var fineInflated = $.ig.RectUtil.prototype.inflate(fineBox, (this.thickness() / 2) + this.getHitTestAllowance(isFinger));
					if (fineInflated.containsPoint(position)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		switch (propertyName) {
			case $.ig.FinancialSeries.prototype.xAxisPropertyName:
				this.financialPriceView().selectTrendlineManager();
				break;
		}
		$.ig.FinancialSeries.prototype.propertyUpdatedOverride.call(this, sender, propertyName, oldValue, newValue);
		if (this.financialPriceView().trendLineManager() != null && this.financialPriceView().trendLineManager().propertyUpdated(sender, propertyName, oldValue, newValue)) {
			this.renderSeries(false);
			this.notifyThumbnailAppearanceChanged();
		}
		switch (propertyName) {
			case $.ig.FinancialPriceSeries.prototype.displayTypePropertyName:
				if (this.rootCanvas() != null) {
					this.updatePathBrushes();
					this._currentFrame.incrementFrameVersion();
					this.renderFrame(this._currentFrame, this.financialPriceView());
				}
				this.notifyThumbnailAppearanceChanged();
				break;
			case $.ig.Series.prototype.trendLineBrushPropertyName:
				this.updateIndexedProperties();
				break;
			case $.ig.Series.prototype.trendLineTypePropertyName:
				this.notifyThumbnailAppearanceChanged();
				break;
		}
	}
	,
	updatePathBrushes: function () {
		this.financialPriceView().updatePathBrushes();
		if (this.thumbnailView() != null) {
			(this.thumbnailView()).updatePathBrushes();
		}
	}
	,
	getRange: function (axis) {
		if (axis != null && axis == this.yAxis() && this.lowColumn() != null && !$.ig.util.isNaN(this.lowColumn().minimum()) && this.highColumn() != null && !$.ig.util.isNaN(this.highColumn().maximum())) {
			return new $.ig.AxisRange(this.lowColumn().minimum(), this.highColumn().maximum());
		}
		return null;
	}
	,
	scrollIntoView: function (item) {
		var index = this.fastItemsSource() != null ? this.fastItemsSource().indexOf(item) : -1;
		var windowRect = this.view().windowRect();
		var viewportRect = this.view().viewport();
		var unitRect = new $.ig.Rect(0, 0, 0, 1, 1);
		var effectiveViewportRect = this.getEffectiveViewportForUnitViewport(this.view());
		if (index >= 0 && $.ig.Rect.prototype.l_op_Inequality(windowRect, null) && $.ig.Rect.prototype.l_op_Inequality(viewportRect, null)) {
			if (this.xAxis() != null) {
				var xParams = new $.ig.ScalerParams(0, unitRect, unitRect, this.xAxis().isInverted(), effectiveViewportRect);
				var cx = this.xAxis().getScaledValue(index, xParams);
				if (cx < windowRect.left() + 0.1 * windowRect.width()) {
					cx = cx + 0.4 * windowRect.width();
				}
				if (cx > windowRect.right() - 0.1 * windowRect.width()) {
					cx = cx - 0.4 * windowRect.width();
				}
				windowRect.x(cx - 0.5 * windowRect.width());
			}
			if (this.yAxis() != null && this.lowColumn() != null && this.highColumn() != null && index < this.lowColumn().count() && index < this.highColumn().count()) {
				var yParams = new $.ig.ScalerParams(0, unitRect, unitRect, this.yAxis().isInverted(), effectiveViewportRect);
				var low = this.yAxis().getScaledValue(this.lowColumn().item(index), yParams);
				var high = this.yAxis().getScaledValue(this.highColumn().item(index), yParams);
				if (!$.ig.util.isNaN(low) && !$.ig.util.isNaN(high)) {
					var height = Math.abs(low - high);
					if (windowRect.height() < height) {
						windowRect.height(height);
						windowRect.y(Math.min(low, high));
					} else {
						if (low < windowRect.top() + 0.1 * windowRect.height()) {
							low = low + 0.4 * windowRect.height();
						}
						if (low > windowRect.bottom() - 0.1 * windowRect.height()) {
							low = low - 0.4 * windowRect.height();
						}
						windowRect.y(low - 0.5 * windowRect.height());
					}
				}
			}
			this.syncLink().windowNotify(this.seriesViewer(), windowRect);
		}
		return index >= 0;
	}
	,
	validateSeries: function (viewportRect, windowRect, view) {
		var isValid = $.ig.FinancialSeries.prototype.validateSeries.call(this, viewportRect, windowRect, view);
		if (this.openColumn() == null || this.closeColumn() == null || this.highColumn() == null || this.lowColumn() == null) {
			isValid = false;
		}
		return isValid;
	}
	,
	convertToSingle: function (value) {
		return value;
	}
	,
	prepareFrame: function (frame, view) {
		var $self = this;
		$.ig.FinancialSeries.prototype.prepareFrame.call(this, frame, view);
		var windowRect = view.windowRect();
		var viewportRect = view.viewport();
		var effectiveViewportRect = this.getEffectiveViewport1(view);
		var xaxis = this.xAxis();
		var yaxis = this.yAxis();
		var xParams = new $.ig.ScalerParams(0, windowRect, viewportRect, xaxis.isInverted(), effectiveViewportRect);
		var yParams = new $.ig.ScalerParams(0, windowRect, viewportRect, yaxis.isInverted(), effectiveViewportRect);
		frame._buckets.clear();
		frame._markers.clear();
		frame._trend.clear();
		var offset = 0;
		var sortingXAxis = $.ig.util.cast($.ig.ISortingAxis.prototype.$type, this.xAxis());
		if (sortingXAxis != null && sortingXAxis.sortedIndices().count() != this.fastItemsSource().count()) {
			return;
		}
		offset = this.getOffset(windowRect, viewportRect, effectiveViewportRect);
		var priceSeriesView = $.ig.util.cast($.ig.FinancialPriceSeriesView.prototype.$type, view);
		if (this.trendLineType() != $.ig.TrendLineType.prototype.none) {
			var typical = new Array(this.fastItemsSource().count());
			var typicalIndex = 0;
			var en = this.typicalColumn().getEnumerator();
			while (en.moveNext()) {
				var typicalPrice = en.current();
				typical[typicalIndex] = typicalPrice;
				typicalIndex++;
			}
			this.financialPriceView().trendLineManager().prepareLine(frame._trend, this.trendLineType(), typical, this.trendLinePeriod(), function (x) { return $self.xAxis().getScaledValue(x, xParams); }, function (y) { return $self.yAxis().getScaledValue(y, yParams); }, (function () {
				var $ret = new $.ig.TrendResolutionParams();
				$ret.bucketSize(view.bucketCalculator().bucketSize());
				$ret.firstBucket(view.bucketCalculator().firstBucket());
				$ret.lastBucket(view.bucketCalculator().lastBucket());
				$ret.offset(offset);
				$ret.resolution($self.resolution());
				$ret.viewport(viewportRect);
				return $ret;
			}()));
		}
		var singlePixelSpan = this.convertToSingle(this.xAxis().getUnscaledValue(2, xParams) - this.xAxis().getUnscaledValue(1, xParams));
		for (var i = view.bucketCalculator().firstBucket(); i <= view.bucketCalculator().lastBucket(); ++i) {
			var bucket;
			if (sortingXAxis == null) {
				bucket = view.bucketCalculator().getBucket(i);
			} else {
				var index = sortingXAxis.sortedIndices().__inner[i];
				var bucketX = sortingXAxis.getUnscaledValueAt(index);
				var bucketOpen = this.convertToSingle(this.openColumn().item(index));
				var bucketHigh = this.convertToSingle(this.highColumn().item(index));
				var bucketLow = this.convertToSingle(this.lowColumn().item(index));
				var bucketClose = this.convertToSingle(this.closeColumn().item(index));
				var currentOpen = bucketOpen;
				var currentHigh = bucketHigh;
				var currentLow = bucketLow;
				var currentClose = bucketClose;
				var currentX = bucketX;
				while (i < view.bucketCalculator().lastBucket()) {
					index = sortingXAxis.sortedIndices().__inner[i + 1];
					currentX = sortingXAxis.getUnscaledValueAt(index);
					if (currentX - bucketX > singlePixelSpan) {
						break;
					}
					i++;
					currentHigh = Math.max(bucketHigh, this.convertToSingle(this.highColumn().item(index)));
					currentLow = Math.min(bucketLow, this.convertToSingle(this.lowColumn().item(index)));
					currentClose = this.convertToSingle(this.closeColumn().item(index));
				}
				var xVal = NaN;
				if (!$.ig.util.isNaN(bucketX)) {
					xVal = this.xAxis().getScaledValue(bucketX, xParams);
				}
				bucket = [ this.convertToSingle(xVal), currentOpen, currentHigh, currentLow, currentClose ];
			}
			if (!isNaN(bucket[0])) {
				if (this.xAxis() != null && $.ig.util.cast($.ig.ISortingAxis.prototype.$type, this.xAxis()) !== null) {
					bucket[0] = (bucket[0] + offset);
				} else {
					bucket[0] = (xaxis.getScaledValue(bucket[0], xParams) + offset);
				}
				bucket[1] = yaxis.getScaledValue(bucket[1], yParams);
				bucket[2] = yaxis.getScaledValue(bucket[2], yParams);
				bucket[3] = yaxis.getScaledValue(bucket[3], yParams);
				bucket[4] = yaxis.getScaledValue(bucket[4], yParams);
				frame._buckets.add(bucket);
			}
		}
		return;
	}
	,
	clearRendering: function (wipeClean, view) {
		$.ig.FinancialSeries.prototype.clearRendering.call(this, wipeClean, view);
		var financialPriceView = $.ig.util.cast($.ig.FinancialPriceSeriesView.prototype.$type, view);
		financialPriceView.clearPriceSymbols();
		if (wipeClean) {
			financialPriceView.shapes().count(0);
		}
		if (financialPriceView.trendLineManager() != null) {
			financialPriceView.trendLineManager().clearPoints();
		}
	}
	,
	hasIndividualElements: function () {
		return true;
	}
	,
	renderFrame: function (frame, view) {
		$.ig.FinancialSeries.prototype.renderFrame.call(this, frame, view);
		if (this.xAxis() == null || this.yAxis() == null) {
			return;
		}
		var isDirty = false;
		if (view.checkFrameDirty(frame)) {
			isDirty = true;
			view.updateFrameVersion(frame);
		}
		this._renderManager.initCategoryRenderSettings(this, this.shouldOverrideCategoryStyle(), this.xAxis(), this.getCategoryItems.runOn(this), this.getBucketSize(view), this.getFirstBucket(view));
		var areStylesOverriden = false;
		var args = this._renderManager.categoryOverrideArgs();
		if (args != null) {
			areStylesOverriden = true;
		}
		var priceSeriesView = $.ig.util.cast($.ig.FinancialPriceSeriesView.prototype.$type, view);
		priceSeriesView.clearGroupedPriceSymbols();
		priceSeriesView.separateMode(false);
		if (priceSeriesView.trendLineManager() != null) {
			priceSeriesView.trendLineManager().clearPoints();
		}
		if (this.trendLineType() != $.ig.TrendLineType.prototype.none) {
			priceSeriesView.trendLineManager().rasterizeTrendLine(frame._trend);
		}
		if (areStylesOverriden) {
			this.renderSeparate(frame, view, isDirty);
		} else {
			priceSeriesView.shapes().count(0);
			this.renderCombinedGrouping(frame, view);
		}
	}
	,
	getItemSpan: function () {
		var width = this.xAxis().getGroupSize(this.view().windowRect(), this.view().viewport(), this.getEffectiveViewport1(this.view())) / 2;
		width = Math.max(width, $.ig.FinancialPriceSeries.prototype._mIN_WIDTH);
		return width;
	}
	,
	renderSeparate: function (frame, view, isDirty) {
		var displayType = this.displayType();
		var effectiveViewportRect = this.getEffectiveViewport1(view);
		var width = this.xAxis().getGroupSize(view.windowRect(), view.viewport(), effectiveViewportRect) / 2;
		width = Math.max(width, $.ig.FinancialPriceSeries.prototype._mIN_WIDTH);
		var priceView = $.ig.util.cast($.ig.FinancialPriceSeriesView.prototype.$type, view);
		priceView.separateMode(true);
		var buckets = frame._buckets;
		var count = 0;
		var valueCount = this.fastItemsSource().count();
		var xAxis = this.xAxis();
		var xParams = new $.ig.ScalerParams(0, view.windowRect(), view.viewport(), xAxis.isInverted(), effectiveViewportRect);
		var actualBrush = this.actualBrush();
		var negativeBrush = this.negativeBrush();
		if (negativeBrush == null) {
			negativeBrush = actualBrush;
		}
		for (var i = 0; i < buckets.count(); ++i) {
			var left = buckets.__inner[i][0] - width;
			var center = buckets.__inner[i][0];
			var right = buckets.__inner[i][0] + width;
			var open = buckets.__inner[i][1];
			var high = buckets.__inner[i][2];
			var low = buckets.__inner[i][3];
			var close = buckets.__inner[i][4];
			if ($.ig.util.isNaN(open) || $.ig.util.isNaN(high) || $.ig.util.isNaN(low) || $.ig.util.isNaN(close)) {
				continue;
			}
			var p = priceView.shapes().item(count);
			count++;
			var negative = open < close;
			var pg = new $.ig.PathGeometry();
			if (isDirty) {
				switch (displayType) {
					case $.ig.PriceDisplayType.prototype.candlestick:
						if (negative) {
							var tmp = open;
							open = close;
							close = tmp;
						}
						var pf1 = new $.ig.PathFigure();
						pf1.__startPoint = { __x: center, __y: low, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
						pf1.__segments.add((function () {
							var $ret = new $.ig.LineSegment(1);
							$ret.point({ __x: center, __y: open, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
							return $ret;
						}()));
						var pf2 = new $.ig.PathFigure();
						pf2.__startPoint = { __x: left, __y: close, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
						pf2.__segments.add((function () {
							var $ret = new $.ig.LineSegment(1);
							$ret.point({ __x: right, __y: close, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
							return $ret;
						}()));
						pf2.__segments.add((function () {
							var $ret = new $.ig.LineSegment(1);
							$ret.point({ __x: right, __y: open, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
							return $ret;
						}()));
						pf2.__segments.add((function () {
							var $ret = new $.ig.LineSegment(1);
							$ret.point({ __x: left, __y: open, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
							return $ret;
						}()));
						pf2.__segments.add((function () {
							var $ret = new $.ig.LineSegment(1);
							$ret.point({ __x: left, __y: close, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
							return $ret;
						}()));
						var pf3 = new $.ig.PathFigure();
						pf3.__startPoint = { __x: center, __y: close, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
						pf3.__segments.add((function () {
							var $ret = new $.ig.LineSegment(1);
							$ret.point({ __x: center, __y: high, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
							return $ret;
						}()));
						pg.figures().add(pf1);
						pg.figures().add(pf2);
						pg.figures().add(pf3);
						break;
					case $.ig.PriceDisplayType.prototype.oHLC:
						var pf4 = new $.ig.PathFigure();
						pf4.__startPoint = { __x: left, __y: open, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
						pf4.__segments.add((function () {
							var $ret = new $.ig.LineSegment(1);
							$ret.point({ __x: center, __y: open, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
							return $ret;
						}()));
						var pf5 = new $.ig.PathFigure();
						pf5.__startPoint = { __x: center, __y: low, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
						pf5.__segments.add((function () {
							var $ret = new $.ig.LineSegment(1);
							$ret.point({ __x: center, __y: high, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
							return $ret;
						}()));
						var pf6 = new $.ig.PathFigure();
						pf6.__startPoint = { __x: center, __y: close, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
						pf6.__segments.add((function () {
							var $ret = new $.ig.LineSegment(1);
							$ret.point({ __x: right, __y: close, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
							return $ret;
						}()));
						pg.figures().add(pf4);
						pg.figures().add(pf5);
						pg.figures().add(pf6);
						break;
				}
			}
			if (negative) {
				this._renderManager._initialRenderFill = negativeBrush;
				this._renderManager._actualRenderFill = negativeBrush;
				this._renderManager._actualNegativeShape = true;
			} else {
				this._renderManager._initialRenderFill = actualBrush;
				this._renderManager._actualRenderFill = actualBrush;
				this._renderManager._actualNegativeShape = false;
			}
			this.performCategoryStyleOverride(buckets, i, valueCount, xAxis, xParams, view.isThumbnailView());
			if (displayType == $.ig.PriceDisplayType.prototype.oHLC) {
				this._renderManager.setCategoryShapeAppearance(p, true, false, false, false);
			} else {
				this._renderManager.setCategoryShapeAppearance(p, false, false, false, false);
			}
			if (isDirty) {
				p.data(pg);
			}
		}
		priceView.shapes().count(count);
		priceView.frameRendered();
	}
	,
	renderCombinedGrouping: function (frame, view) {
		var displayType = this.displayType();
		var effectiveViewportRect = this.getEffectiveViewport1(view);
		var width = this.xAxis().getGroupSize(view.windowRect(), view.viewport(), effectiveViewportRect) / 2;
		width = Math.max(width, $.ig.FinancialPriceSeries.prototype._mIN_WIDTH);
		var priceView = $.ig.util.cast($.ig.FinancialPriceSeriesView.prototype.$type, view);
		var positiveGroup = priceView.getPositiveGroup();
		var negativeGroup = priceView.getNegativeGroup();
		var buckets = frame._buckets;
		for (var i = 0; i < buckets.count(); ++i) {
			var left = buckets.__inner[i][0] - width;
			var center = buckets.__inner[i][0];
			var right = buckets.__inner[i][0] + width;
			var open = buckets.__inner[i][1];
			var high = buckets.__inner[i][2];
			var low = buckets.__inner[i][3];
			var close = buckets.__inner[i][4];
			if ($.ig.util.isNaN(open) || $.ig.util.isNaN(high) || $.ig.util.isNaN(low) || $.ig.util.isNaN(close)) {
				continue;
			}
			var negative = open < close;
			var group = negative ? negativeGroup : positiveGroup;
			switch (displayType) {
				case $.ig.PriceDisplayType.prototype.candlestick:
					if (negative) {
						var tmp = open;
						open = close;
						close = tmp;
					}
					group.children().add((function () {
						var $ret = new $.ig.LineGeometry();
						$ret.startPoint({ __x: center, __y: low, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
						$ret.endPoint({ __x: center, __y: open, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
						return $ret;
					}()));
					group.children().add((function () {
						var $ret = new $.ig.RectangleGeometry();
						$ret.rect(new $.ig.Rect(0, left, close, right - left, open - close));
						return $ret;
					}()));
					group.children().add((function () {
						var $ret = new $.ig.LineGeometry();
						$ret.startPoint({ __x: center, __y: close, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
						$ret.endPoint({ __x: center, __y: high, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
						return $ret;
					}()));
					break;
				case $.ig.PriceDisplayType.prototype.oHLC:
					group.children().add((function () {
						var $ret = new $.ig.LineGeometry();
						$ret.startPoint({ __x: left, __y: open, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
						$ret.endPoint({ __x: center, __y: open, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
						return $ret;
					}()));
					group.children().add((function () {
						var $ret = new $.ig.LineGeometry();
						$ret.startPoint({ __x: center, __y: low, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
						$ret.endPoint({ __x: center, __y: high, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
						return $ret;
					}()));
					group.children().add((function () {
						var $ret = new $.ig.LineGeometry();
						$ret.startPoint({ __x: center, __y: close, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
						$ret.endPoint({ __x: right, __y: close, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
						return $ret;
					}()));
					break;
			}
		}
	}
	,
	item: function (sender, point) {
		if (sender == this.financialPriceView().trendLineManager().trendPolyline()) {
			return null;
		}
		return $.ig.FinancialSeries.prototype.item.call(this, sender, point);
	}
	,
	updateIndexedProperties: function () {
		$.ig.FinancialSeries.prototype.updateIndexedProperties.call(this);
		if (this.index() < 0) {
			return;
		}
		this.financialPriceView().updateTrendlineBrush();
	}
	,
	exportVisualDataOverride: function (svd) {
		$.ig.FinancialSeries.prototype.exportVisualDataOverride.call(this, svd);
		var trendShape = new $.ig.PolyLineVisualData(1, "trendLine", this.financialPriceView().trendLineManager().trendPolyline());
		trendShape.tags().add("Trend");
		svd.shapes().add(trendShape);
	}
	,
	$type: new $.ig.Type('FinancialPriceSeries', $.ig.FinancialSeries.prototype.$type)
}, true);

$.ig.util.defType('FinancialPriceBucketCalculator', 'FinancialBucketCalculator', {
	init: function (view) {
		$.ig.FinancialBucketCalculator.prototype.init.call(this, view);
	},
	getBucket: function (index) {
		var i0 = index * this.bucketSize();
		var i1 = Math.min(i0 + this.bucketSize() - 1, this.view().financialModel().fastItemsSource().count() - 1);
		if (i0 <= i1 && i0 >= 0 && i1 >= 0) {
			var open = this.view().financialModel().openColumn().item(i0);
			var high = Number.NEGATIVE_INFINITY;
			var low = Number.POSITIVE_INFINITY;
			var close = this.view().financialModel().closeColumn().item(i1);
			for (var i = i0; i <= i1; ++i) {
				high = Math.max(high, this.view().financialModel().highColumn().item(i));
				low = Math.min(low, this.view().financialModel().lowColumn().item(i));
			}
			low = Math.min(open, low);
			high = Math.max(close, high);
			return [ (0.5 * (i0 + i1)), open, high, low, close ];
		}
		return [ NaN, NaN, NaN, NaN, NaN ];
	}
	,
	$type: new $.ig.Type('FinancialPriceBucketCalculator', $.ig.FinancialBucketCalculator.prototype.$type)
}, true);

$.ig.util.defType('FinancialValueList', 'Object', {
	__openColumn: null,
	__highColumn: null,
	__closeColumn: null,
	__lowColumn: null,
	__volumeColumn: null,
	init: function (openColumn, highColumn, lowColumn, closeColumn, volumeColumn) {
		$.ig.Object.prototype.init.call(this);
		this.__openColumn = openColumn;
		this.__highColumn = highColumn;
		this.__closeColumn = closeColumn;
		this.__lowColumn = lowColumn;
		this.__volumeColumn = volumeColumn;
	},
	indexOf: function (item) {
		throw new $.ig.NotImplementedException(0);
	}
	,
	insert: function (index, item) {
		throw new $.ig.NotImplementedException(0);
	}
	,
	removeAt: function (index) {
		throw new $.ig.NotImplementedException(0);
	}
	,
	item: function (index, value) {
		if (arguments.length === 2) {
			throw new $.ig.NotImplementedException(0);
			return value;
		} else {
			var open = NaN;
			var high = NaN;
			var low = NaN;
			var close = NaN;
			var volume = NaN;
			if (this.__openColumn != null && index >= 0 && index < this.__openColumn.count()) {
				open = this.__openColumn.item(index);
			}
			if (this.__highColumn != null && index >= 0 && index < this.__highColumn.count()) {
				high = this.__highColumn.item(index);
			}
			if (this.__lowColumn != null && index >= 0 && index < this.__lowColumn.count()) {
				low = this.__lowColumn.item(index);
			}
			if (this.__closeColumn != null && index >= 0 && index < this.__closeColumn.count()) {
				close = this.__closeColumn.item(index);
			}
			if (this.__volumeColumn != null && index >= 0 && index < this.__volumeColumn.count()) {
				volume = this.__volumeColumn.item(index);
			}
			var openIsNaN = $.ig.util.isNaN(open);
			var highIsNaN = $.ig.util.isNaN(high);
			var lowIsNaN = $.ig.util.isNaN(low);
			var closeIsNaN = $.ig.util.isNaN(close);
			var volumeIsNaN = $.ig.util.isNaN(volume);
			if (!highIsNaN && !lowIsNaN && !closeIsNaN) {
				return (high + low + close) / 3;
			}
			if (!highIsNaN && !lowIsNaN) {
				return (high + low) / 2;
			}
			if (!openIsNaN && !closeIsNaN) {
				return (open + close) / 2;
			}
			if (!openIsNaN) {
				return open;
			}
			if (!highIsNaN) {
				return high;
			}
			if (!lowIsNaN) {
				return low;
			}
			if (!closeIsNaN) {
				return close;
			}
			return NaN;
		}
	}
	,
	add: function (item) {
		throw new $.ig.NotImplementedException(0);
	}
	,
	clear: function () {
		throw new $.ig.NotImplementedException(0);
	}
	,
	contains: function (item) {
		throw new $.ig.NotImplementedException(0);
	}
	,
	copyTo: function (array, arrayIndex) {
		throw new $.ig.NotImplementedException(0);
	}
	,
	count: function () {
		var openCount = 0;
		var highCount = 0;
		var lowCount = 0;
		var closeCount = 0;
		var volumeCount = 0;
		if (this.__openColumn != null) {
			openCount = this.__openColumn.count();
		}
		if (this.__highColumn != null) {
			highCount = this.__highColumn.count();
		}
		if (this.__lowColumn != null) {
			lowCount = this.__lowColumn.count();
		}
		if (this.__closeColumn != null) {
			closeCount = this.__closeColumn.count();
		}
		if (this.__volumeColumn != null) {
			volumeCount = this.__volumeColumn.count();
		}
		var count = 0;
		count = Math.max(count, openCount);
		count = Math.max(count, highCount);
		count = Math.max(count, lowCount);
		count = Math.max(count, closeCount);
		count = Math.max(count, volumeCount);
		return count;
	}
	,
	isReadOnly: function () {
		return true;
	}
	,
	remove: function (item) {
		throw new $.ig.NotImplementedException(0);
	}
	,
	getEnumerator: function () {
		throw new $.ig.NotImplementedException(0);
	}
	,
	getEnumerator: function () {
		throw new $.ig.NotImplementedException(0);
	}
	,
	$type: new $.ig.Type('FinancialValueList', $.ig.Object.prototype.$type, [$.ig.IList$1.prototype.$type.specialize(Number)])
}, true);

$.ig.util.defType('FinancialIndicatorBucketCalculator', 'FinancialBucketCalculator', {
	init: function (view) {
		$.ig.FinancialBucketCalculator.prototype.init.call(this, view);
		this.indicatorView(view);
	},
	_indicatorView: null,
	indicatorView: function (value) {
		if (arguments.length === 1) {
			this._indicatorView = value;
			return value;
		} else {
			return this._indicatorView;
		}
	}
	,
	getBucket: function (index) {
		var i0 = index * this.bucketSize();
		var i1 = Math.min(i0 + this.bucketSize() - 1, this.indicatorView().indicatorModel().indicatorColumn().count() - 1);
		var min = NaN;
		var max = NaN;
		for (var i = i0; i <= i1; ++i) {
			var y = this.indicatorView().indicatorModel().indicatorColumn().__inner[i];
			if (!$.ig.util.isNaN(min)) {
				if (!$.ig.util.isNaN(y)) {
					min = Math.min(min, y);
					max = Math.max(max, y);
				}
			} else {
				min = y;
				max = y;
			}
		}
		if (!$.ig.util.isNaN(min)) {
			return [ (0.5 * (i0 + i1)), min, max ];
		}
		return [ NaN, NaN, NaN ];
	}
	,
	$type: new $.ig.Type('FinancialIndicatorBucketCalculator', $.ig.FinancialBucketCalculator.prototype.$type)
}, true);

$.ig.util.defType('ForceIndexIndicator', 'StrategyBasedIndicator', {
	init: function () {
		$.ig.StrategyBasedIndicator.prototype.init.call(this);
		this.displayType($.ig.IndicatorDisplayType.prototype.area);
	},
	calculationStrategy: function () {
		return new $.ig.ForceIndexIndicatorStrategy();
	}
	,
	styleKeyType: function () {
		return $.ig.ForceIndexIndicator.prototype.$type;
	}
	,
	period: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.ForceIndexIndicator.prototype.periodProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.ForceIndexIndicator.prototype.periodProperty));
		}
	}
	,
	periodOverride: function () {
		return this.period();
	}
	,
	$type: new $.ig.Type('ForceIndexIndicator', $.ig.StrategyBasedIndicator.prototype.$type)
}, true);

$.ig.util.defType('ForceIndexIndicatorStrategy', 'IndicatorCalculationStrategy', {
	init: function () {
		$.ig.IndicatorCalculationStrategy.prototype.init.call(this);
	},
	basedOn: function (dataSource, supportingCalculations) {
		var list = new $.ig.List$1(String, 0);
		list.add($.ig.FinancialSeries.prototype.closeColumnPropertyName);
		list.add($.ig.FinancialSeries.prototype.volumeColumnPropertyName);
		list.addRange(supportingCalculations.eMA().basedOn());
		return list;
	}
	,
	fI: function (dataSource) {
		var d__ = new $.ig.ForceIndexIndicatorStrategy___FI__IteratorClass(-2);
		d__.__4__this = this;
		d__.__3__dataSource = dataSource;
		return d__;
	}
	,
	calculateIndicator: function (dataSource, supportingCalculations) {
		var fi = this.fI(dataSource);
		var period = dataSource.period();
		var indicatorColumn = dataSource.indicatorColumn();
		if (period != 0 && !$.ig.util.isNaN(period) && !Number.isInfinity(period)) {
			fi = supportingCalculations.eMA().strategy()(fi, dataSource.period());
		}
		var i = 0;
		var en = fi.getEnumerator();
		while (en.moveNext()) {
			var d = en.current();
			indicatorColumn.item(i, d);
			++i;
		}
		return true;
	}
	,
	$type: new $.ig.Type('ForceIndexIndicatorStrategy', $.ig.IndicatorCalculationStrategy.prototype.$type)
}, true);

$.ig.util.defType('FullStochasticOscillatorIndicator', 'StrategyBasedIndicator', {
	init: function () {
		$.ig.StrategyBasedIndicator.prototype.init.call(this);
		this.trendLineBrush((function () {
			var $ret = new $.ig.Brush();
			$ret.fill("Blue");
			return $ret;
		}()));
		this.trendLineType($.ig.TrendLineType.prototype.exponentialAverage);
		this.trendLinePeriod(3);
	},
	calculationStrategy: function () {
		return new $.ig.FullStochasticOscillatorIndicatorStrategy();
	}
	,
	styleKeyType: function () {
		return $.ig.FullStochasticOscillatorIndicator.prototype.$type;
	}
	,
	period: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.FullStochasticOscillatorIndicator.prototype.periodProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.FullStochasticOscillatorIndicator.prototype.periodProperty));
		}
	}
	,
	periodOverride: function () {
		return this.period();
	}
	,
	smoothingPeriod: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.FullStochasticOscillatorIndicator.prototype.smoothingPeriodProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.FullStochasticOscillatorIndicator.prototype.smoothingPeriodProperty));
		}
	}
	,
	shortPeriodOverride: function () {
		return this.smoothingPeriod();
	}
	,
	triggerPeriod: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.FullStochasticOscillatorIndicator.prototype.triggerPeriodProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.FullStochasticOscillatorIndicator.prototype.triggerPeriodProperty));
		}
	}
	,
	longPeriodOverride: function () {
		return this.triggerPeriod();
	}
	,
	trendPeriodOverride: function () {
		return this.triggerPeriod();
	}
	,
	$type: new $.ig.Type('FullStochasticOscillatorIndicator', $.ig.StrategyBasedIndicator.prototype.$type)
}, true);

$.ig.util.defType('FullStochasticOscillatorIndicatorStrategy', 'IndicatorCalculationStrategy', {
	_percentKStrategy: null,
	percentKStrategy: function (value) {
		if (arguments.length === 1) {
			this._percentKStrategy = value;
			return value;
		} else {
			return this._percentKStrategy;
		}
	}
	,
	init: function () {
		$.ig.IndicatorCalculationStrategy.prototype.init.call(this);
		this.percentKStrategy(new $.ig.PercentKCalculationStrategy());
	},
	basedOn: function (dataSource, supportingCalculations) {
		var list = new $.ig.List$1(String, 0);
		list.addRange(this.percentKStrategy().basedOn(dataSource, supportingCalculations));
		list.addRange(supportingCalculations.eMA().basedOn());
		return list;
	}
	,
	calculateIndicator: function (dataSource, supportingCalculations) {
		var percentK = this.percentKStrategy().provideStream(dataSource, supportingCalculations);
		var fullPercentK = supportingCalculations.eMA().strategy()(percentK, dataSource.shortPeriod());
		var i = 0;
		var en = fullPercentK.getEnumerator();
		while (en.moveNext()) {
			var value = en.current();
			dataSource.indicatorColumn().item(i, value);
			i++;
		}
		return true;
	}
	,
	$type: new $.ig.Type('FullStochasticOscillatorIndicatorStrategy', $.ig.IndicatorCalculationStrategy.prototype.$type)
}, true);

$.ig.util.defType('PercentKCalculationStrategy', 'StreamingIndicatorCalculationStrategy', {
	init: function () {
		$.ig.StreamingIndicatorCalculationStrategy.prototype.init.call(this);
	},
	basedOn: function (dataSource, supportingCalculations) {
		var list = new $.ig.List$1(String, 0);
		list.add($.ig.FinancialSeries.prototype.highColumnPropertyName);
		list.add($.ig.FinancialSeries.prototype.lowColumnPropertyName);
		list.add($.ig.FinancialSeries.prototype.closeColumnPropertyName);
		return list;
	}
	,
	provideStream: function (dataSource, supportingCalculations) {
		var d__ = new $.ig.PercentKCalculationStrategy___ProvideStream__IteratorClass(-2);
		d__.__4__this = this;
		d__.__3__dataSource = dataSource;
		d__.__3__supportingCalculations = supportingCalculations;
		return d__;
	}
	,
	calculateIndicator: function (dataSource, supportingCalculations) {
		var i = 0;
		var en = this.provideStream(dataSource, supportingCalculations).getEnumerator();
		while (en.moveNext()) {
			var value = en.current();
			dataSource.indicatorColumn().item(i, value);
			i++;
		}
		return true;
	}
	,
	$type: new $.ig.Type('PercentKCalculationStrategy', $.ig.StreamingIndicatorCalculationStrategy.prototype.$type)
}, true);

$.ig.util.defType('PositiveVolumeIndexIndicator', 'StrategyBasedIndicator', {
	init: function () {
		$.ig.StrategyBasedIndicator.prototype.init.call(this);
	},
	calculationStrategy: function () {
		return new $.ig.PositiveVolumeIndexIndicatorStrategy();
	}
	,
	styleKeyType: function () {
		return $.ig.PositiveVolumeIndexIndicator.prototype.$type;
	}
	,
	$type: new $.ig.Type('PositiveVolumeIndexIndicator', $.ig.StrategyBasedIndicator.prototype.$type)
}, true);

$.ig.util.defType('PositiveVolumeIndexIndicatorStrategy', 'IndicatorCalculationStrategy', {
	init: function () {
		$.ig.IndicatorCalculationStrategy.prototype.init.call(this);
	},
	basedOn: function (dataSource, supportingCalculations) {
		var list = new $.ig.List$1(String, 0);
		list.add($.ig.FinancialSeries.prototype.closeColumnPropertyName);
		list.add($.ig.FinancialSeries.prototype.volumeColumnPropertyName);
		return list;
	}
	,
	calculateIndicator: function (dataSource, supportingCalculations) {
		var closeColumn = dataSource.closeColumn();
		var volumeColumn = dataSource.volumeColumn();
		var indicatorColumn = dataSource.indicatorColumn();
		var pvi = 0;
		var newValue = 0;
		var yesterdayPVI = 0;
		if (indicatorColumn.count() > 0) {
			indicatorColumn.item(0, pvi);
		}
		for (var i = 1; i < indicatorColumn.count(); i++) {
			if (volumeColumn.item(i) > volumeColumn.item(i - 1)) {
				newValue = supportingCalculations.makeSafe()((closeColumn.item(i) - closeColumn.item(i - 1)) / closeColumn.item(i - 1));
				if (yesterdayPVI != 0) {
					pvi += newValue * yesterdayPVI;
				} else {
					pvi += newValue;
				}
			}
			indicatorColumn.item(i, pvi);
			yesterdayPVI = pvi;
		}
		return true;
	}
	,
	$type: new $.ig.Type('PositiveVolumeIndexIndicatorStrategy', $.ig.IndicatorCalculationStrategy.prototype.$type)
}, true);

$.ig.util.defType('ItemwiseIndicatorCalculationStrategy', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	calculateIndicatorItem: function (dataSource, supportingCalculations, currentIndex) {
	}
	,
	basedOn: function (dataSource, supportingCalculations) {
	}
	,
	$type: new $.ig.Type('ItemwiseIndicatorCalculationStrategy', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('ItemwiseStrategyBasedIndicator', 'StrategyBasedIndicator', {
	_actualItemwiseStrategy: null,
	actualItemwiseStrategy: function (value) {
		if (arguments.length === 1) {
			this._actualItemwiseStrategy = value;
			return value;
		} else {
			return this._actualItemwiseStrategy;
		}
	}
	,
	itemwiseStrategy: function () {
	}
	,
	calculationStrategy: function () {
		return new $.ig.ItemwiseStrategyCalculationStrategy();
	}
	,
	init: function () {
		$.ig.StrategyBasedIndicator.prototype.init.call(this);
		this.actualItemwiseStrategy(this.itemwiseStrategy());
		(this.actualCalculationStrategy()).itemwiseStrategy(this.actualItemwiseStrategy());
	},
	$type: new $.ig.Type('ItemwiseStrategyBasedIndicator', $.ig.StrategyBasedIndicator.prototype.$type)
}, true);

$.ig.util.defType('ItemwiseStrategyCalculationStrategy', 'IndicatorCalculationStrategy', {
	init: function () {
		$.ig.IndicatorCalculationStrategy.prototype.init.call(this);
	},
	_itemwiseStrategy: null,
	itemwiseStrategy: function (value) {
		if (arguments.length === 1) {
			this._itemwiseStrategy = value;
			return value;
		} else {
			return this._itemwiseStrategy;
		}
	}
	,
	basedOn: function (dataSource, supportingCalculations) {
		return this.itemwiseStrategy().basedOn(dataSource, supportingCalculations);
	}
	,
	calculateIndicator: function (dataSource, supportingCalculations) {
		var shouldContinue = true;
		for (var i = dataSource.calculateFrom(); i < dataSource.calculateFrom() + dataSource.calculateCount(); i++) {
			shouldContinue = this.itemwiseStrategy().calculateIndicatorItem(dataSource, supportingCalculations, i);
			if (!shouldContinue) {
				return false;
			}
		}
		return shouldContinue;
	}
	,
	$type: new $.ig.Type('ItemwiseStrategyCalculationStrategy', $.ig.IndicatorCalculationStrategy.prototype.$type)
}, true);

$.ig.util.defType('MassIndexIndicator', 'StrategyBasedIndicator', {
	init: function () {
		$.ig.StrategyBasedIndicator.prototype.init.call(this);
	},
	calculationStrategy: function () {
		return new $.ig.MassIndexIndicatorStrategy();
	}
	,
	styleKeyType: function () {
		return $.ig.MassIndexIndicator.prototype.$type;
	}
	,
	$type: new $.ig.Type('MassIndexIndicator', $.ig.StrategyBasedIndicator.prototype.$type)
}, true);

$.ig.util.defType('MassIndexIndicatorStrategy', 'IndicatorCalculationStrategy', {
	init: function () {
		$.ig.IndicatorCalculationStrategy.prototype.init.call(this);
	},
	basedOn: function (dataSource, supportingCalculations) {
		var list = new $.ig.List$1(String, 0);
		list.add($.ig.FinancialSeries.prototype.highColumnPropertyName);
		list.add($.ig.FinancialSeries.prototype.lowColumnPropertyName);
		list.addRange(supportingCalculations.eMA().basedOn());
		return list;
	}
	,
	highLowRange: function (highColumn, lowColumn) {
		var result = new $.ig.List$1(Number, 0);
		for (var i = 0; i < (Math.min(highColumn.count(), lowColumn.count())); i++) {
			result.add(highColumn.item(i) - lowColumn.item(i));
		}
		return result;
	}
	,
	calculateIndicator: function (dataSource, supportingCalculations) {
		var period = 9;
		var highColumn = dataSource.highColumn();
		var lowColumn = dataSource.lowColumn();
		var indicatorColumn = dataSource.indicatorColumn();
		var ema9 = supportingCalculations.eMA().strategy()(this.highLowRange(highColumn, lowColumn), period).getEnumerator();
		var ema9ema9 = supportingCalculations.eMA().strategy()(supportingCalculations.eMA().strategy()(this.highLowRange(highColumn, lowColumn), period), period).getEnumerator();
		var buffer = new Array(period);
		for (var i = 0; i < period; i++) {
			buffer[i] = 0;
		}
		var mass = 0;
		for (var i1 = 0; i1 < indicatorColumn.count(); i1++) {
			var cursor = i1 % period;
			mass -= buffer[cursor];
			ema9.moveNext();
			ema9ema9.moveNext();
			var newMassValue = supportingCalculations.makeSafe()(ema9.current() / ema9ema9.current());
			mass += newMassValue;
			indicatorColumn.item(i1, mass);
			buffer[cursor] = newMassValue;
		}
		return true;
	}
	,
	$type: new $.ig.Type('MassIndexIndicatorStrategy', $.ig.IndicatorCalculationStrategy.prototype.$type)
}, true);

$.ig.util.defType('MedianPriceIndicator', 'ItemwiseStrategyBasedIndicator', {
	init: function () {
		$.ig.ItemwiseStrategyBasedIndicator.prototype.init.call(this);
	},
	itemwiseStrategy: function () {
		return new $.ig.MedianPriceIndicatorStrategy();
	}
	,
	styleKeyType: function () {
		return $.ig.MedianPriceIndicator.prototype.$type;
	}
	,
	$type: new $.ig.Type('MedianPriceIndicator', $.ig.ItemwiseStrategyBasedIndicator.prototype.$type)
}, true);

$.ig.util.defType('MedianPriceIndicatorStrategy', 'ItemwiseIndicatorCalculationStrategy', {
	init: function () {
		$.ig.ItemwiseIndicatorCalculationStrategy.prototype.init.call(this);
	},
	basedOn: function (dataSource, supportingCalculations) {
		var list = new $.ig.List$1(String, 0);
		list.add($.ig.FinancialSeries.prototype.highColumnPropertyName);
		list.add($.ig.FinancialSeries.prototype.lowColumnPropertyName);
		return list;
	}
	,
	calculateIndicatorItem: function (dataSource, supportingCalculations, currentIndex) {
		dataSource.indicatorColumn().item(currentIndex, (dataSource.highColumn().item(currentIndex) + dataSource.lowColumn().item(currentIndex)) / 2);
		return true;
	}
	,
	$type: new $.ig.Type('MedianPriceIndicatorStrategy', $.ig.ItemwiseIndicatorCalculationStrategy.prototype.$type)
}, true);

$.ig.util.defType('FinancialCalculationDataSource', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	_openColumn: null,
	openColumn: function (value) {
		if (arguments.length === 1) {
			this._openColumn = value;
			return value;
		} else {
			return this._openColumn;
		}
	}
	,
	_closeColumn: null,
	closeColumn: function (value) {
		if (arguments.length === 1) {
			this._closeColumn = value;
			return value;
		} else {
			return this._closeColumn;
		}
	}
	,
	_highColumn: null,
	highColumn: function (value) {
		if (arguments.length === 1) {
			this._highColumn = value;
			return value;
		} else {
			return this._highColumn;
		}
	}
	,
	_lowColumn: null,
	lowColumn: function (value) {
		if (arguments.length === 1) {
			this._lowColumn = value;
			return value;
		} else {
			return this._lowColumn;
		}
	}
	,
	_volumeColumn: null,
	volumeColumn: function (value) {
		if (arguments.length === 1) {
			this._volumeColumn = value;
			return value;
		} else {
			return this._volumeColumn;
		}
	}
	,
	_indicatorColumn: null,
	indicatorColumn: function (value) {
		if (arguments.length === 1) {
			this._indicatorColumn = value;
			return value;
		} else {
			return this._indicatorColumn;
		}
	}
	,
	_typicalColumn: null,
	typicalColumn: function (value) {
		if (arguments.length === 1) {
			this._typicalColumn = value;
			return value;
		} else {
			return this._typicalColumn;
		}
	}
	,
	_trueRange: null,
	trueRange: function (value) {
		if (arguments.length === 1) {
			this._trueRange = value;
			return value;
		} else {
			return this._trueRange;
		}
	}
	,
	_trueLow: null,
	trueLow: function (value) {
		if (arguments.length === 1) {
			this._trueLow = value;
			return value;
		} else {
			return this._trueLow;
		}
	}
	,
	_period: 0,
	period: function (value) {
		if (arguments.length === 1) {
			this._period = value;
			return value;
		} else {
			return this._period;
		}
	}
	,
	_shortPeriod: 0,
	shortPeriod: function (value) {
		if (arguments.length === 1) {
			this._shortPeriod = value;
			return value;
		} else {
			return this._shortPeriod;
		}
	}
	,
	_longPeriod: 0,
	longPeriod: function (value) {
		if (arguments.length === 1) {
			this._longPeriod = value;
			return value;
		} else {
			return this._longPeriod;
		}
	}
	,
	_count: 0,
	count: function (value) {
		if (arguments.length === 1) {
			this._count = value;
			return value;
		} else {
			return this._count;
		}
	}
	,
	_calculateFrom: 0,
	calculateFrom: function (value) {
		if (arguments.length === 1) {
			this._calculateFrom = value;
			return value;
		} else {
			return this._calculateFrom;
		}
	}
	,
	_calculateCount: 0,
	calculateCount: function (value) {
		if (arguments.length === 1) {
			this._calculateCount = value;
			return value;
		} else {
			return this._calculateCount;
		}
	}
	,
	_multiplier: 0,
	multiplier: function (value) {
		if (arguments.length === 1) {
			this._multiplier = value;
			return value;
		} else {
			return this._multiplier;
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
	_specifiesRange: false,
	specifiesRange: function (value) {
		if (arguments.length === 1) {
			this._specifiesRange = value;
			return value;
		} else {
			return this._specifiesRange;
		}
	}
	,
	$type: new $.ig.Type('FinancialCalculationDataSource', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('FinancialCalculationSupportingCalculations', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	_eMA: null,
	eMA: function (value) {
		if (arguments.length === 1) {
			this._eMA = value;
			return value;
		} else {
			return this._eMA;
		}
	}
	,
	_sMA: null,
	sMA: function (value) {
		if (arguments.length === 1) {
			this._sMA = value;
			return value;
		} else {
			return this._sMA;
		}
	}
	,
	_sTDEV: null,
	sTDEV: function (value) {
		if (arguments.length === 1) {
			this._sTDEV = value;
			return value;
		} else {
			return this._sTDEV;
		}
	}
	,
	_movingSum: null,
	movingSum: function (value) {
		if (arguments.length === 1) {
			this._movingSum = value;
			return value;
		} else {
			return this._movingSum;
		}
	}
	,
	_shortVolumeOscillatorAverage: null,
	shortVolumeOscillatorAverage: function (value) {
		if (arguments.length === 1) {
			this._shortVolumeOscillatorAverage = value;
			return value;
		} else {
			return this._shortVolumeOscillatorAverage;
		}
	}
	,
	_longVolumeOscillatorAverage: null,
	longVolumeOscillatorAverage: function (value) {
		if (arguments.length === 1) {
			this._longVolumeOscillatorAverage = value;
			return value;
		} else {
			return this._longVolumeOscillatorAverage;
		}
	}
	,
	_shortPriceOscillatorAverage: null,
	shortPriceOscillatorAverage: function (value) {
		if (arguments.length === 1) {
			this._shortPriceOscillatorAverage = value;
			return value;
		} else {
			return this._shortPriceOscillatorAverage;
		}
	}
	,
	_longPriceOscillatorAverage: null,
	longPriceOscillatorAverage: function (value) {
		if (arguments.length === 1) {
			this._longPriceOscillatorAverage = value;
			return value;
		} else {
			return this._longPriceOscillatorAverage;
		}
	}
	,
	_toEnumerableRange: null,
	toEnumerableRange: function (value) {
		if (arguments.length === 1) {
			this._toEnumerableRange = value;
			return value;
		} else {
			return this._toEnumerableRange;
		}
	}
	,
	_toEnumerable: null,
	toEnumerable: function (value) {
		if (arguments.length === 1) {
			this._toEnumerable = value;
			return value;
		} else {
			return this._toEnumerable;
		}
	}
	,
	_makeSafe: null,
	makeSafe: function (value) {
		if (arguments.length === 1) {
			this._makeSafe = value;
			return value;
		} else {
			return this._makeSafe;
		}
	}
	,
	$type: new $.ig.Type('FinancialCalculationSupportingCalculations', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('SupportingCalculation$1', 'Object', {
	$tCalculationStrategy: null,
	init: function ($tCalculationStrategy, initNumber, strategy) {
		this.$tCalculationStrategy = $tCalculationStrategy;
		this.$type = this.$type.specialize(this.$tCalculationStrategy);
		if (initNumber > 0) {
			switch (initNumber) {
				case 1:
					this.init1.apply(this, arguments);
					break;
			}
			return;
		}
		$.ig.Object.prototype.init.call(this);
		this.__strategy = strategy;
		this.__basedOn = new $.ig.List$1(String, 0);
	},
	init1: function ($tCalculationStrategy, initNumber, strategy, basedOn) {
		this.$tCalculationStrategy = $tCalculationStrategy;
		this.$type = this.$type.specialize(this.$tCalculationStrategy);
		$.ig.Object.prototype.init.call(this);
		this.__strategy = strategy;
		this.__basedOn = new $.ig.List$1(String, 1, basedOn);
	},
	__strategy: null,
	__basedOn: null,
	strategy: function () {
		return this.__strategy;
	}
	,
	basedOn: function () {
		return this.__basedOn;
	}
	,
	$type: new $.ig.Type('SupportingCalculation$1', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('ColumnSupportingCalculation', 'SupportingCalculation$1', {
	init: function (initNumber, strategy) {
		if (initNumber > 0) {
			switch (initNumber) {
				case 1:
					this.init1.apply(this, arguments);
					break;
			}
			return;
		}
		$.ig.SupportingCalculation$1.prototype.init.call(this, $.ig.SupportingCalculationStrategy.prototype.$type, 0, strategy);
	},
	init1: function (initNumber, strategy, basedOn) {
		$.ig.SupportingCalculation$1.prototype.init1.call(this, $.ig.SupportingCalculationStrategy.prototype.$type, 1, strategy, basedOn);
	},
	$type: new $.ig.Type('ColumnSupportingCalculation', $.ig.SupportingCalculation$1.prototype.$type.specialize($.ig.SupportingCalculationStrategy.prototype.$type))
}, true);

$.ig.util.defType('DataSourceSupportingCalculation', 'SupportingCalculation$1', {
	init: function (initNumber, strategy) {
		if (initNumber > 0) {
			switch (initNumber) {
				case 1:
					this.init1.apply(this, arguments);
					break;
			}
			return;
		}
		$.ig.SupportingCalculation$1.prototype.init.call(this, $.ig.ProvideColumnValuesStrategy.prototype.$type, 0, strategy);
	},
	init1: function (initNumber, strategy, basedOn) {
		$.ig.SupportingCalculation$1.prototype.init1.call(this, $.ig.ProvideColumnValuesStrategy.prototype.$type, 1, strategy, basedOn);
	},
	$type: new $.ig.Type('DataSourceSupportingCalculation', $.ig.SupportingCalculation$1.prototype.$type.specialize($.ig.ProvideColumnValuesStrategy.prototype.$type))
}, true);

$.ig.util.defType('CalculatedColumn', 'Object', {
	init: function (initNumber, valuesProvider, basedOn) {
		if (initNumber > 0) {
			switch (initNumber) {
				case 1:
					this.init1.apply(this, arguments);
					break;
			}
			return;
		}
		$.ig.Object.prototype.init.call(this);
		this.__valuesProvider = valuesProvider;
		this.__basedOn = new $.ig.List$1(String, 1, basedOn);
	},
	init1: function (initNumber, valuesProvider, basedOn) {
		$.ig.Object.prototype.init.call(this);
		this.__valuesProvider = valuesProvider;
		this.__basedOn = new $.ig.List$1(String, 1, basedOn);
	},
	__valuesProvider: null,
	__basedOn: null,
	basedOn: function () {
		return this.__basedOn;
	}
	,
	getEnumerator: function () {
		return this.__valuesProvider.getEnumerator();
	}
	,
	getEnumerator: function () {
		return this.__valuesProvider.getEnumerator();
	}
	,
	$type: new $.ig.Type('CalculatedColumn', $.ig.Object.prototype.$type, [$.ig.IEnumerable$1.prototype.$type.specialize(Number)])
}, true);

$.ig.util.defType('MarketFacilitationIndexIndicator', 'ItemwiseStrategyBasedIndicator', {
	init: function () {
		$.ig.ItemwiseStrategyBasedIndicator.prototype.init.call(this);
		this.displayType($.ig.IndicatorDisplayType.prototype.area);
	},
	itemwiseStrategy: function () {
		return new $.ig.MarketFacilitationIndexIndicatorStrategy();
	}
	,
	styleKeyType: function () {
		return $.ig.MarketFacilitationIndexIndicator.prototype.$type;
	}
	,
	$type: new $.ig.Type('MarketFacilitationIndexIndicator', $.ig.ItemwiseStrategyBasedIndicator.prototype.$type)
}, true);

$.ig.util.defType('MarketFacilitationIndexIndicatorStrategy', 'ItemwiseIndicatorCalculationStrategy', {
	init: function () {
		$.ig.ItemwiseIndicatorCalculationStrategy.prototype.init.call(this);
	},
	basedOn: function (dataSource, supportingCalculations) {
		var list = new $.ig.List$1(String, 0);
		list.add($.ig.FinancialSeries.prototype.highColumnPropertyName);
		list.add($.ig.FinancialSeries.prototype.lowColumnPropertyName);
		list.add($.ig.FinancialSeries.prototype.volumeColumnPropertyName);
		return list;
	}
	,
	calculateIndicatorItem: function (dataSource, supportingCalculations, currentIndex) {
		dataSource.indicatorColumn().item(currentIndex, supportingCalculations.makeSafe()((dataSource.highColumn().item(currentIndex) - dataSource.lowColumn().item(currentIndex)) / dataSource.volumeColumn().item(currentIndex)));
		return true;
	}
	,
	$type: new $.ig.Type('MarketFacilitationIndexIndicatorStrategy', $.ig.ItemwiseIndicatorCalculationStrategy.prototype.$type)
}, true);

$.ig.util.defType('MoneyFlowIndexIndicator', 'StrategyBasedIndicator', {
	init: function () {
		$.ig.StrategyBasedIndicator.prototype.init.call(this);
		this.displayType($.ig.IndicatorDisplayType.prototype.area);
	},
	calculationStrategy: function () {
		return new $.ig.MoneyFlowIndexIndicatorStrategy();
	}
	,
	styleKeyType: function () {
		return $.ig.MoneyFlowIndexIndicator.prototype.$type;
	}
	,
	period: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.MoneyFlowIndexIndicator.prototype.periodProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.MoneyFlowIndexIndicator.prototype.periodProperty));
		}
	}
	,
	periodOverride: function () {
		return this.period();
	}
	,
	$type: new $.ig.Type('MoneyFlowIndexIndicator', $.ig.StrategyBasedIndicator.prototype.$type)
}, true);

$.ig.util.defType('MoneyFlowIndexIndicatorStrategy', 'IndicatorCalculationStrategy', {
	init: function () {
		$.ig.IndicatorCalculationStrategy.prototype.init.call(this);
	},
	basedOn: function (dataSource, supportingCalculations) {
		var list = new $.ig.List$1(String, 0);
		list.addRange(dataSource.typicalColumn().basedOn());
		list.add($.ig.FinancialSeries.prototype.volumeColumnPropertyName);
		return list;
	}
	,
	calculateIndicator: function (dataSource, supportingCalculations) {
		var typical = dataSource.typicalColumn().getEnumerator();
		var volume = dataSource.volumeColumn().getEnumerator();
		var period = dataSource.period();
		var i = 0;
		var positiveBuffer = new Array(period);
		for (i = 0; i < period; i++) {
			positiveBuffer[i] = 0;
		}
		var positiveFlow = 0;
		var negativeBuffer = new Array(period);
		for (i = 0; i < period; i++) {
			negativeBuffer[i] = 0;
		}
		var negativeFlow = 0;
		var flowYesterday = 0;
		i = 0;
		while (typical.moveNext() && volume.moveNext()) {
			var cursor = i % period;
			var flowToday = typical.current() * volume.current();
			positiveFlow -= positiveBuffer[cursor];
			negativeFlow -= negativeBuffer[cursor];
			switch (Math.sign(flowToday - flowYesterday)) {
				case -1:
					positiveBuffer[cursor] = 0;
					negativeBuffer[cursor] = flowToday;
					break;
				case 0:
					positiveBuffer[cursor] = 0;
					negativeBuffer[cursor] = 0;
					break;
				case 1:
					positiveBuffer[cursor] = flowToday;
					negativeBuffer[cursor] = 0;
					break;
			}
			positiveFlow += positiveBuffer[cursor];
			negativeFlow += negativeBuffer[cursor];
			var mfi = supportingCalculations.makeSafe()(100 * positiveFlow / (positiveFlow + negativeFlow));
			dataSource.indicatorColumn().item(i, mfi);
			flowYesterday = flowToday;
			++i;
		}
		return true;
	}
	,
	$type: new $.ig.Type('MoneyFlowIndexIndicatorStrategy', $.ig.IndicatorCalculationStrategy.prototype.$type)
}, true);

$.ig.util.defType('MovingAverageConvergenceDivergenceIndicator', 'StrategyBasedIndicator', {
	init: function () {
		$.ig.StrategyBasedIndicator.prototype.init.call(this);
		this.trendLineBrush((function () {
			var $ret = new $.ig.Brush();
			$ret.fill("Blue");
			return $ret;
		}()));
		this.trendLineType($.ig.TrendLineType.prototype.exponentialAverage);
		this.trendLinePeriod(9);
	},
	calculationStrategy: function () {
		return new $.ig.MovingAverageConvergenceDivergenceIndicatorStrategy();
	}
	,
	styleKeyType: function () {
		return $.ig.MovingAverageConvergenceDivergenceIndicator.prototype.$type;
	}
	,
	shortPeriod: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.MovingAverageConvergenceDivergenceIndicator.prototype.shortPeriodProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.MovingAverageConvergenceDivergenceIndicator.prototype.shortPeriodProperty));
		}
	}
	,
	shortPeriodOverride: function () {
		return this.shortPeriod();
	}
	,
	longPeriod: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.MovingAverageConvergenceDivergenceIndicator.prototype.longPeriodProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.MovingAverageConvergenceDivergenceIndicator.prototype.longPeriodProperty));
		}
	}
	,
	longPeriodOverride: function () {
		return this.longPeriod();
	}
	,
	signalPeriod: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.MovingAverageConvergenceDivergenceIndicator.prototype.signalPeriodProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.MovingAverageConvergenceDivergenceIndicator.prototype.signalPeriodProperty));
		}
	}
	,
	trendPeriodOverride: function () {
		return this.signalPeriod();
	}
	,
	$type: new $.ig.Type('MovingAverageConvergenceDivergenceIndicator', $.ig.StrategyBasedIndicator.prototype.$type)
}, true);

$.ig.util.defType('MovingAverageConvergenceDivergenceIndicatorStrategy', 'IndicatorCalculationStrategy', {
	init: function () {
		$.ig.IndicatorCalculationStrategy.prototype.init.call(this);
	},
	basedOn: function (dataSource, supportingCalculations) {
		var list = new $.ig.List$1(String, 0);
		list.addRange(dataSource.typicalColumn().basedOn());
		list.addRange(supportingCalculations.eMA().basedOn());
		return list;
	}
	,
	calculateIndicator: function (dataSource, supportingCalculations) {
		var typicalColumn = dataSource.typicalColumn();
		var indicatorColumn = dataSource.indicatorColumn();
		var shortPeriod = dataSource.shortPeriod();
		var longPeriod = dataSource.longPeriod();
		var shortEma = supportingCalculations.eMA().strategy()(typicalColumn, shortPeriod).getEnumerator();
		var longEma = supportingCalculations.eMA().strategy()(typicalColumn, longPeriod).getEnumerator();
		var i = 0;
		while (shortEma.moveNext() && longEma.moveNext()) {
			var macd = supportingCalculations.makeSafe()(shortEma.current() - longEma.current());
			indicatorColumn.item(i, macd);
			i++;
		}
		return true;
	}
	,
	$type: new $.ig.Type('MovingAverageConvergenceDivergenceIndicatorStrategy', $.ig.IndicatorCalculationStrategy.prototype.$type)
}, true);

$.ig.util.defType('NegativeVolumeIndexIndicator', 'StrategyBasedIndicator', {
	init: function () {
		$.ig.StrategyBasedIndicator.prototype.init.call(this);
	},
	calculationStrategy: function () {
		return new $.ig.NegativeVolumeIndexIndicatorStrategy();
	}
	,
	styleKeyType: function () {
		return $.ig.NegativeVolumeIndexIndicator.prototype.$type;
	}
	,
	$type: new $.ig.Type('NegativeVolumeIndexIndicator', $.ig.StrategyBasedIndicator.prototype.$type)
}, true);

$.ig.util.defType('NegativeVolumeIndexIndicatorStrategy', 'IndicatorCalculationStrategy', {
	init: function () {
		$.ig.IndicatorCalculationStrategy.prototype.init.call(this);
	},
	basedOn: function (dataSource, supportingCalculations) {
		var list = new $.ig.List$1(String, 0);
		list.add($.ig.FinancialSeries.prototype.closeColumnPropertyName);
		list.add($.ig.FinancialSeries.prototype.volumeColumnPropertyName);
		return list;
	}
	,
	calculateIndicator: function (dataSource, supportingCalculations) {
		var closeColumn = dataSource.closeColumn();
		var volumeColumn = dataSource.volumeColumn();
		var indicatorColumn = dataSource.indicatorColumn();
		var nvi = 0;
		var newValue = 0;
		var yesterdayNVI = 0;
		if (indicatorColumn.count() > 0) {
			indicatorColumn.item(0, nvi);
		}
		for (var i = 1; i < indicatorColumn.count(); i++) {
			if (volumeColumn.item(i) < volumeColumn.item(i - 1)) {
				newValue = supportingCalculations.makeSafe()((closeColumn.item(i) - closeColumn.item(i - 1)) / closeColumn.item(i - 1));
				if (yesterdayNVI != 0) {
					nvi += newValue * yesterdayNVI;
				} else {
					nvi += newValue;
				}
			}
			indicatorColumn.item(i, nvi);
			yesterdayNVI = nvi;
		}
		return true;
	}
	,
	$type: new $.ig.Type('NegativeVolumeIndexIndicatorStrategy', $.ig.IndicatorCalculationStrategy.prototype.$type)
}, true);

$.ig.util.defType('OnBalanceVolumeIndicator', 'StrategyBasedIndicator', {
	init: function () {
		$.ig.StrategyBasedIndicator.prototype.init.call(this);
	},
	calculationStrategy: function () {
		return new $.ig.OnBalanceVolumeIndicatorStrategy();
	}
	,
	styleKeyType: function () {
		return $.ig.OnBalanceVolumeIndicator.prototype.$type;
	}
	,
	$type: new $.ig.Type('OnBalanceVolumeIndicator', $.ig.StrategyBasedIndicator.prototype.$type)
}, true);

$.ig.util.defType('OnBalanceVolumeIndicatorStrategy', 'IndicatorCalculationStrategy', {
	init: function () {
		$.ig.IndicatorCalculationStrategy.prototype.init.call(this);
	},
	basedOn: function (dataSource, supportingCalculations) {
		var list = new $.ig.List$1(String, 0);
		list.add($.ig.FinancialSeries.prototype.closeColumnPropertyName);
		list.add($.ig.FinancialSeries.prototype.volumeColumnPropertyName);
		return list;
	}
	,
	calculateIndicator: function (dataSource, supportingCalculations) {
		var closeColumn = dataSource.closeColumn();
		var volumeColumn = dataSource.volumeColumn();
		var indicatorColumn = dataSource.indicatorColumn();
		var n = 0;
		if (closeColumn != null && volumeColumn != null) {
			n = Math.min(closeColumn.count(), volumeColumn.count());
		}
		var obv = 0;
		if (n > 0) {
			obv = volumeColumn.item(0);
			indicatorColumn.item(0, obv);
		}
		for (var i = 1; i < n; ++i) {
			switch (Math.sign(closeColumn.item(i) - closeColumn.item(i - 1))) {
				case -1:
					obv -= volumeColumn.item(i);
					break;
				case 1:
					obv += volumeColumn.item(i);
					break;
			}
			indicatorColumn.item(i, obv);
		}
		return true;
	}
	,
	$type: new $.ig.Type('OnBalanceVolumeIndicatorStrategy', $.ig.IndicatorCalculationStrategy.prototype.$type)
}, true);

$.ig.util.defType('PercentagePriceOscillatorIndicator', 'StrategyBasedIndicator', {
	init: function () {
		$.ig.StrategyBasedIndicator.prototype.init.call(this);
	},
	calculationStrategy: function () {
		return new $.ig.PercentagePriceOscillatorIndicatorStrategy();
	}
	,
	styleKeyType: function () {
		return $.ig.PercentagePriceOscillatorIndicator.prototype.$type;
	}
	,
	shortPeriod: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PercentagePriceOscillatorIndicator.prototype.shortPeriodProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.PercentagePriceOscillatorIndicator.prototype.shortPeriodProperty));
		}
	}
	,
	shortPeriodOverride: function () {
		return this.shortPeriod();
	}
	,
	longPeriod: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PercentagePriceOscillatorIndicator.prototype.longPeriodProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.PercentagePriceOscillatorIndicator.prototype.longPeriodProperty));
		}
	}
	,
	longPeriodOverride: function () {
		return this.longPeriod();
	}
	,
	$type: new $.ig.Type('PercentagePriceOscillatorIndicator', $.ig.StrategyBasedIndicator.prototype.$type)
}, true);

$.ig.util.defType('PercentagePriceOscillatorIndicatorStrategy', 'IndicatorCalculationStrategy', {
	init: function () {
		$.ig.IndicatorCalculationStrategy.prototype.init.call(this);
	},
	basedOn: function (dataSource, supportingCalculations) {
		var list = new $.ig.List$1(String, 0);
		list.addRange(supportingCalculations.shortPriceOscillatorAverage().basedOn());
		list.addRange(supportingCalculations.longPriceOscillatorAverage().basedOn());
		return list;
	}
	,
	calculateIndicator: function (dataSource, supportingCalculations) {
		var indicatorColumn = dataSource.indicatorColumn();
		var shortEma = supportingCalculations.shortPriceOscillatorAverage().strategy()(dataSource).getEnumerator();
		var longEma = supportingCalculations.longPriceOscillatorAverage().strategy()(dataSource).getEnumerator();
		var i = 0;
		while (shortEma.moveNext() && longEma.moveNext()) {
			var ppo = supportingCalculations.makeSafe()(100 * (shortEma.current() - longEma.current()) / longEma.current());
			indicatorColumn.item(i, ppo);
			++i;
		}
		return true;
	}
	,
	$type: new $.ig.Type('PercentagePriceOscillatorIndicatorStrategy', $.ig.IndicatorCalculationStrategy.prototype.$type)
}, true);

$.ig.util.defType('PercentageVolumeOscillatorIndicator', 'StrategyBasedIndicator', {
	init: function () {
		$.ig.StrategyBasedIndicator.prototype.init.call(this);
	},
	calculationStrategy: function () {
		return new $.ig.PercentageVolumeOscillatorIndicatorStrategy();
	}
	,
	styleKeyType: function () {
		return $.ig.PercentageVolumeOscillatorIndicator.prototype.$type;
	}
	,
	shortPeriod: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PercentageVolumeOscillatorIndicator.prototype.shortPeriodProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.PercentageVolumeOscillatorIndicator.prototype.shortPeriodProperty));
		}
	}
	,
	shortPeriodOverride: function () {
		return this.shortPeriod();
	}
	,
	longPeriod: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PercentageVolumeOscillatorIndicator.prototype.longPeriodProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.PercentageVolumeOscillatorIndicator.prototype.longPeriodProperty));
		}
	}
	,
	longPeriodOverride: function () {
		return this.longPeriod();
	}
	,
	$type: new $.ig.Type('PercentageVolumeOscillatorIndicator', $.ig.StrategyBasedIndicator.prototype.$type)
}, true);

$.ig.util.defType('PercentageVolumeOscillatorIndicatorStrategy', 'IndicatorCalculationStrategy', {
	init: function () {
		$.ig.IndicatorCalculationStrategy.prototype.init.call(this);
	},
	basedOn: function (dataSource, supportingCalculations) {
		var list = new $.ig.List$1(String, 0);
		list.addRange(supportingCalculations.shortVolumeOscillatorAverage().basedOn());
		list.addRange(supportingCalculations.longVolumeOscillatorAverage().basedOn());
		return list;
	}
	,
	calculateIndicator: function (dataSource, supportingCalculations) {
		var shortEma = supportingCalculations.shortVolumeOscillatorAverage().strategy()(dataSource).getEnumerator();
		var longEma = supportingCalculations.longVolumeOscillatorAverage().strategy()(dataSource).getEnumerator();
		var indicatorColumn = dataSource.indicatorColumn();
		var i = 0;
		while (shortEma.moveNext() && longEma.moveNext()) {
			var pvo = supportingCalculations.makeSafe()(100 * (shortEma.current() - longEma.current()) / longEma.current());
			indicatorColumn.item(i, pvo);
			++i;
		}
		return true;
	}
	,
	$type: new $.ig.Type('PercentageVolumeOscillatorIndicatorStrategy', $.ig.IndicatorCalculationStrategy.prototype.$type)
}, true);

$.ig.util.defType('PriceChannelOverlay', 'FinancialOverlay', {
	init: function () {
		this._channelTopColumn = new $.ig.List$1(Number, 0);
		this._channelBottomColumn = new $.ig.List$1(Number, 0);
		$.ig.FinancialOverlay.prototype.init.call(this);
		this.defaultStyleKey($.ig.PriceChannelOverlay.prototype.$type);
		this._previousFrame = new $.ig.CategoryFrame(3);
		this._transitionFrame = new $.ig.CategoryFrame(3);
		this._currentFrame = new $.ig.CategoryFrame(3);
	},
	period: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.PriceChannelOverlay.prototype.periodProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.PriceChannelOverlay.prototype.periodProperty));
		}
	}
	,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		switch (propertyName) {
			case $.ig.FinancialSeries.prototype.highColumnPropertyName:
			case $.ig.FinancialSeries.prototype.lowColumnPropertyName:
				this.overlayValid(false);
				break;
		}
		$.ig.FinancialOverlay.prototype.propertyUpdatedOverride.call(this, sender, propertyName, oldValue, newValue);
		switch (propertyName) {
			case $.ig.PriceChannelOverlay.prototype.periodPropertyName:
				this.overlayValid(false);
				this.renderSeries(false);
				break;
		}
	}
	,
	validateSeries: function (viewportRect, windowRect, view) {
		var isValid = $.ig.FinancialOverlay.prototype.validateSeries.call(this, viewportRect, windowRect, view);
		if (this.highColumn() == null || this.lowColumn() == null) {
			isValid = false;
		}
		return isValid;
	}
	,
	validateOverlay: function () {
		this._channelTopColumn.clear();
		this._channelBottomColumn.clear();
		var period = $.ig.truncate($.ig.MathUtil.prototype.clamp(this.period(), 0, this.fastItemsSource().count()));
		var count = Math.min(this.highColumn().count(), this.lowColumn().count());
		var safeHigh = this.makeReadOnlyAndEnsureSorted(this.highColumn());
		var safeLow = this.makeReadOnlyAndEnsureSorted(this.lowColumn());
		for (var i = 0; i < count; i++) {
			var ago = Math.min(period, i);
			var highestHigh = -1.7976931348623157E+308;
			var lowestLow = 1.7976931348623157E+308;
			for (var j = 0; j < ago; j++) {
				if (!$.ig.util.isNaN(safeHigh.item(i - j))) {
					highestHigh = Math.max(highestHigh, safeHigh.item(i - j));
				}
				if (!$.ig.util.isNaN(safeLow.item(i - j))) {
					lowestLow = Math.min(lowestLow, safeLow.item(i - j));
				}
			}
			if (i == 0) {
				lowestLow = safeLow.item(0);
				highestHigh = safeHigh.item(0);
			}
			this._channelTopColumn.add(highestHigh);
			this._channelBottomColumn.add(lowestLow);
		}
		return true;
	}
	,
	convertToSingle: function (value) {
		return value;
	}
	,
	prepareFrame: function (frame, view) {
		$.ig.FinancialOverlay.prototype.prepareFrame.call(this, frame, view);
		var windowRect = view.windowRect();
		var viewportRect = view.viewport();
		var effectiveViewportRect = this.getEffectiveViewport1(view);
		var xParams = new $.ig.ScalerParams(0, windowRect, viewportRect, this.xAxis().isInverted(), effectiveViewportRect);
		var yParams = new $.ig.ScalerParams(0, windowRect, viewportRect, this.yAxis().isInverted(), effectiveViewportRect);
		var xaxis = this.xAxis();
		var yaxis = this.yAxis();
		frame._buckets.clear();
		frame._markers.clear();
		frame._trend.clear();
		var offset = 0;
		var sortingXAxis = $.ig.util.cast($.ig.ISortingAxis.prototype.$type, this.xAxis());
		if (sortingXAxis != null && sortingXAxis.sortedIndices().count() != this.fastItemsSource().count()) {
			return;
		}
		offset = this.getOffset(windowRect, viewportRect, effectiveViewportRect);
		if (!this.overlayValid()) {
			this.overlayValid(this.validateOverlay());
		}
		var singlePixelSpan = this.convertToSingle(this.xAxis().getUnscaledValue(2, xParams) - this.xAxis().getUnscaledValue(1, xParams));
		for (var i = view.bucketCalculator().firstBucket(); i <= view.bucketCalculator().lastBucket(); ++i) {
			var bucket;
			if (sortingXAxis == null) {
				bucket = view.bucketCalculator().getBucket(i);
			} else {
				var index = sortingXAxis.sortedIndices().__inner[i];
				var bucketX = sortingXAxis.getUnscaledValueAt(index);
				var bucketTop = this.convertToSingle(this._channelTopColumn.__inner[i]);
				var bucketBottom = this.convertToSingle(this._channelBottomColumn.__inner[i]);
				var currentTop = bucketTop;
				var currentBottom = bucketBottom;
				var currentX = bucketX;
				while (i < view.bucketCalculator().lastBucket()) {
					index = sortingXAxis.sortedIndices().__inner[i + 1];
					currentX = sortingXAxis.getUnscaledValueAt(index);
					if (currentX - bucketX > singlePixelSpan) {
						break;
					}
					i++;
					currentTop = Math.max(bucketTop, this.convertToSingle(this._channelTopColumn.__inner[i]));
					currentBottom = Math.min(bucketBottom, this.convertToSingle(this._channelBottomColumn.__inner[i]));
				}
				if (!Number.isInfinity(currentBottom) && !Number.isInfinity(currentTop)) {
					var xVal = NaN;
					if (!$.ig.util.isNaN(bucketX)) {
						xVal = this.xAxis().getScaledValue(bucketX, xParams);
					}
					bucket = [ this.convertToSingle(xVal), currentBottom, currentTop ];
				} else {
					bucket = [ NaN, NaN, NaN ];
				}
			}
			var pp = Math.max(1, singlePixelSpan);
			if (!isNaN(bucket[0]) && i * pp >= this.ignoreFirst()) {
				if (this.xAxis() != null && $.ig.util.cast($.ig.ISortingAxis.prototype.$type, this.xAxis()) !== null) {
					bucket[0] = (bucket[0] + offset);
				} else {
					bucket[0] = (xaxis.getScaledValue(bucket[0], xParams) + offset);
				}
				bucket[1] = yaxis.getScaledValue(bucket[1], yParams);
				bucket[2] = yaxis.getScaledValue(bucket[2], yParams);
				frame._buckets.add(bucket);
			}
		}
	}
	,
	clearRendering: function (wipeClean, view) {
		$.ig.FinancialOverlay.prototype.clearRendering.call(this, wipeClean, view);
		var priceChannelView = view;
		if (priceChannelView != null) {
			priceChannelView.clearRendering();
		}
	}
	,
	renderFrame: function (frame, view) {
		$.ig.FinancialOverlay.prototype.renderFrame.call(this, frame, view);
		var priceChannelView = $.ig.util.cast($.ig.PriceChannelOverlayView.prototype.$type, view);
		if (priceChannelView == null) {
			return;
		}
		var effectiveViewportRect = this.getEffectiveViewport1(view);
		if (view.checkFrameDirty(frame)) {
			priceChannelView.clearRendering();
			var count = frame._buckets.count();
			var px = function (i) { return frame._buckets.__inner[i][0]; };
			var nx = function (i) { return frame._buckets.__inner[count - 1 - i][0]; };
			var y0 = function (i) { return frame._buckets.__inner[i][1]; };
			var y1 = function (i) { return frame._buckets.__inner[count - 1 - i][2]; };
			priceChannelView.renderChannel(count, px, nx, y0, y1);
			view.updateFrameVersion(frame);
		}
		this._renderManager.initCategoryRenderSettings(this, this.shouldOverrideCategoryStyle(), this.xAxis(), this.getCategoryItems.runOn(this), this.getBucketSize(view), this.getFirstBucket(view));
		var areStylesOverriden = false;
		var args = this._renderManager.categoryOverrideArgs();
		var buckets = frame._buckets;
		var valueCount = this.fastItemsSource().count();
		if (args != null) {
			areStylesOverriden = true;
		}
		if (areStylesOverriden) {
			var xParams = new $.ig.ScalerParams(0, view.windowRect(), view.viewport(), this.xAxis().isInverted(), effectiveViewportRect);
			this.performCategoryStyleOverride(buckets, -1, valueCount, this.xAxis(), xParams, view.isThumbnailView());
		}
		var line0 = priceChannelView.line0();
		var line1 = priceChannelView.line1();
		var polygon = priceChannelView.fillArea();
		this._renderManager.setCategoryShapeAppearance(line0, true, false, true, true);
		this._renderManager.setCategoryShapeAppearance(line1, true, false, true, true);
		this._renderManager.setCategoryShapeAppearance(polygon, false, true, false, false);
		polygon.__opacity = this._renderManager._actualRenderOpacity * this.actualAreaFillOpacity();
	}
	,
	_channelTopColumn: null,
	_channelBottomColumn: null,
	createView: function () {
		return new $.ig.PriceChannelOverlayView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.FinancialOverlay.prototype.onViewCreated.call(this, view);
		this.priceChannelOverlayView($.ig.util.cast($.ig.PriceChannelOverlayView.prototype.$type, view));
	}
	,
	_priceChannelOverlayView: null,
	priceChannelOverlayView: function (value) {
		if (arguments.length === 1) {
			this._priceChannelOverlayView = value;
			return value;
		} else {
			return this._priceChannelOverlayView;
		}
	}
	,
	getSeriesValue: function (world, useInterpolation, skipUnknowns) {
		if (this.seriesViewer() == null) {
			return NaN;
		}
		var effectiveViewportRect = this.getEffectiveViewport1(this.view());
		var xParams = new $.ig.ScalerParams(0, this.seriesViewer().actualWindowRect(), this.view().viewport(), this.xAxis().isInverted(), effectiveViewportRect);
		var offset = this.getOffset(this.seriesViewer().actualWindowRect(), this.view().viewport(), effectiveViewportRect);
		return this.getSeriesValueHelper(new $.ig.RangeValueList(this._channelTopColumn, this._channelBottomColumn), world, this.xAxis(), xParams, offset, this.getExactUnsortedItemIndex.runOn(this), useInterpolation, skipUnknowns);
	}
	,
	getNextOrExactIndex: function (world, skipUnknowns) {
		if (this._channelTopColumn == null || this._channelBottomColumn == null) {
			return $.ig.FinancialOverlay.prototype.getNextOrExactIndex.call(this, world, skipUnknowns);
		}
		return this.getNextOrExactIndexHelper(world, skipUnknowns, this.xAxis(), this.getExactUnsortedItemIndex.runOn(this), new $.ig.RangeValueList(this._channelTopColumn, this._channelBottomColumn));
	}
	,
	getPreviousOrExactIndex: function (world, skipUnknowns) {
		if (this._channelTopColumn == null || this._channelBottomColumn == null) {
			return $.ig.FinancialOverlay.prototype.getPreviousOrExactIndex.call(this, world, skipUnknowns);
		}
		return this.getPreviousOrExactIndexHelper(world, skipUnknowns, this.xAxis(), this.getExactUnsortedItemIndex.runOn(this), new $.ig.RangeValueList(this._channelTopColumn, this._channelBottomColumn));
	}
	,
	$type: new $.ig.Type('PriceChannelOverlay', $.ig.FinancialOverlay.prototype.$type)
}, true);

$.ig.util.defType('PriceChannelBucketCalculator', 'FinancialBucketCalculator', {
	init: function (view) {
		$.ig.FinancialBucketCalculator.prototype.init.call(this, view);
		this.priceChannelView(view);
	},
	_priceChannelView: null,
	priceChannelView: function (value) {
		if (arguments.length === 1) {
			this._priceChannelView = value;
			return value;
		} else {
			return this._priceChannelView;
		}
	}
	,
	getBucket: function (index) {
		var i0 = index * this.bucketSize();
		var i1 = Math.min(i0 + this.bucketSize() - 1, this.view().financialModel().fastItemsSource().count() - 1);
		if (i0 <= i1) {
			var highestHigh = -1.7976931348623157E+308;
			var lowestLow = 1.7976931348623157E+308;
			var cnt = 0;
			for (var i = i0; i <= i1; ++i) {
				if (!$.ig.util.isNaN(this.priceChannelView().priceChannelOverlayModel()._channelTopColumn.__inner[i]) && !$.ig.util.isNaN(this.priceChannelView().priceChannelOverlayModel()._channelBottomColumn.__inner[i])) {
					highestHigh = Math.max(highestHigh, this.priceChannelView().priceChannelOverlayModel()._channelTopColumn.__inner[i]);
					lowestLow = Math.min(lowestLow, this.priceChannelView().priceChannelOverlayModel()._channelBottomColumn.__inner[i]);
					++cnt;
				}
			}
			if (cnt > 0 && lowestLow != 1.7976931348623157E+308 && highestHigh != -1.7976931348623157E+308) {
				return [ (0.5 * (i0 + i1)), (lowestLow), (highestHigh) ];
			}
		}
		return [ NaN, NaN, NaN ];
	}
	,
	$type: new $.ig.Type('PriceChannelBucketCalculator', $.ig.FinancialBucketCalculator.prototype.$type)
}, true);

$.ig.util.defType('PriceVolumeTrendIndicator', 'StrategyBasedIndicator', {
	init: function () {
		$.ig.StrategyBasedIndicator.prototype.init.call(this);
	},
	calculationStrategy: function () {
		return new $.ig.PriceVolumeTrendIndicatorStrategy();
	}
	,
	styleKeyType: function () {
		return $.ig.PriceVolumeTrendIndicator.prototype.$type;
	}
	,
	$type: new $.ig.Type('PriceVolumeTrendIndicator', $.ig.StrategyBasedIndicator.prototype.$type)
}, true);

$.ig.util.defType('PriceVolumeTrendIndicatorStrategy', 'IndicatorCalculationStrategy', {
	init: function () {
		$.ig.IndicatorCalculationStrategy.prototype.init.call(this);
	},
	basedOn: function (dataSource, supportingCalculations) {
		var list = new $.ig.List$1(String, 0);
		list.add($.ig.FinancialSeries.prototype.volumeColumnPropertyName);
		list.add($.ig.FinancialSeries.prototype.closeColumnPropertyName);
		return list;
	}
	,
	calculateIndicator: function (dataSource, supportingCalculations) {
		var pvt = 0;
		var indicatorColumn = dataSource.indicatorColumn();
		var volumeColumn = dataSource.volumeColumn();
		var closeColumn = dataSource.closeColumn();
		var count = dataSource.count();
		if (count > 0) {
			indicatorColumn.item(0, pvt);
		}
		for (var i = 1; i < count; ++i) {
			pvt = pvt + supportingCalculations.makeSafe()(volumeColumn.item(i) * (closeColumn.item(i) - closeColumn.item(i - 1)) / closeColumn.item(i - 1));
			indicatorColumn.item(i, pvt);
		}
		return true;
	}
	,
	$type: new $.ig.Type('PriceVolumeTrendIndicatorStrategy', $.ig.IndicatorCalculationStrategy.prototype.$type)
}, true);

$.ig.util.defType('RateOfChangeAndMomentumIndicator', 'StrategyBasedIndicator', {
	init: function () {
		$.ig.StrategyBasedIndicator.prototype.init.call(this);
	},
	calculationStrategy: function () {
		return new $.ig.RateOfChangeAndMomentumIndicatorStrategy();
	}
	,
	styleKeyType: function () {
		return $.ig.RateOfChangeAndMomentumIndicator.prototype.$type;
	}
	,
	period: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.RateOfChangeAndMomentumIndicator.prototype.periodProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.RateOfChangeAndMomentumIndicator.prototype.periodProperty));
		}
	}
	,
	periodOverride: function () {
		return this.period();
	}
	,
	$type: new $.ig.Type('RateOfChangeAndMomentumIndicator', $.ig.StrategyBasedIndicator.prototype.$type)
}, true);

$.ig.util.defType('RateOfChangeAndMomentumIndicatorStrategy', 'IndicatorCalculationStrategy', {
	init: function () {
		$.ig.IndicatorCalculationStrategy.prototype.init.call(this);
	},
	basedOn: function (dataSource, supportingCalculations) {
		var list = new $.ig.List$1(String, 0);
		list.add($.ig.FinancialSeries.prototype.closeColumnPropertyName);
		return list;
	}
	,
	calculateIndicator: function (dataSource, supportingCalculations) {
		var closeColumn = dataSource.closeColumn();
		var indicatorColumn = dataSource.indicatorColumn();
		var period = dataSource.period();
		var i = 0;
		var buffer = new Array(period);
		for (i = 0; i < period; i++) {
			buffer[i] = 0;
		}
		i = 0;
		var en = closeColumn.getEnumerator();
		while (en.moveNext()) {
			var close = en.current();
			var cursor = i % period;
			var rcm = supportingCalculations.makeSafe()(100 * (close - buffer[cursor]) / buffer[cursor]);
			indicatorColumn.item(i, rcm);
			buffer[cursor] = close;
			++i;
		}
		return true;
	}
	,
	$type: new $.ig.Type('RateOfChangeAndMomentumIndicatorStrategy', $.ig.IndicatorCalculationStrategy.prototype.$type)
}, true);

$.ig.util.defType('RelativeStrengthIndexIndicator', 'StrategyBasedIndicator', {
	init: function () {
		$.ig.StrategyBasedIndicator.prototype.init.call(this);
		this.displayType($.ig.IndicatorDisplayType.prototype.area);
	},
	calculationStrategy: function () {
		return new $.ig.RelativeStrengthIndexIndicatorStrategy();
	}
	,
	styleKeyType: function () {
		return $.ig.RelativeStrengthIndexIndicator.prototype.$type;
	}
	,
	period: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.RelativeStrengthIndexIndicator.prototype.periodProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.RelativeStrengthIndexIndicator.prototype.periodProperty));
		}
	}
	,
	periodOverride: function () {
		return this.period();
	}
	,
	$type: new $.ig.Type('RelativeStrengthIndexIndicator', $.ig.StrategyBasedIndicator.prototype.$type)
}, true);

$.ig.util.defType('RelativeStrengthIndexIndicatorStrategy', 'StreamingIndicatorCalculationStrategy', {
	init: function () {
		$.ig.StreamingIndicatorCalculationStrategy.prototype.init.call(this);
	},
	basedOn: function (dataSource, supportingCalculations) {
		var list = new $.ig.List$1(String, 0);
		list.add($.ig.FinancialSeries.prototype.closeColumnPropertyName);
		return list;
	}
	,
	calculateIndicator: function (dataSource, supportingCalculations) {
		var i = 0;
		var en = this.provideStream(dataSource, supportingCalculations).getEnumerator();
		while (en.moveNext()) {
			var value = en.current();
			dataSource.indicatorColumn().item(i, value);
			i++;
		}
		return true;
	}
	,
	provideStream: function (dataSource, supportingCalculations) {
		var d__ = new $.ig.RelativeStrengthIndexIndicatorStrategy___ProvideStream__IteratorClass(-2);
		d__.__4__this = this;
		d__.__3__dataSource = dataSource;
		d__.__3__supportingCalculations = supportingCalculations;
		return d__;
	}
	,
	$type: new $.ig.Type('RelativeStrengthIndexIndicatorStrategy', $.ig.StreamingIndicatorCalculationStrategy.prototype.$type)
}, true);

$.ig.util.defType('SlowStochasticOscillatorIndicator', 'StrategyBasedIndicator', {
	init: function () {
		$.ig.StrategyBasedIndicator.prototype.init.call(this);
		this.trendLineBrush((function () {
			var $ret = new $.ig.Brush();
			$ret.fill("Blue");
			return $ret;
		}()));
		this.trendLineType($.ig.TrendLineType.prototype.exponentialAverage);
		this.trendLinePeriod(3);
	},
	calculationStrategy: function () {
		return new $.ig.SlowStochasticOscillatorIndicatorStrategy();
	}
	,
	styleKeyType: function () {
		return $.ig.SlowStochasticOscillatorIndicator.prototype.$type;
	}
	,
	period: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.SlowStochasticOscillatorIndicator.prototype.periodProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.SlowStochasticOscillatorIndicator.prototype.periodProperty));
		}
	}
	,
	periodOverride: function () {
		return this.period();
	}
	,
	$type: new $.ig.Type('SlowStochasticOscillatorIndicator', $.ig.StrategyBasedIndicator.prototype.$type)
}, true);

$.ig.util.defType('SlowStochasticOscillatorIndicatorStrategy', 'IndicatorCalculationStrategy', {
	_percentKStrategy: null,
	percentKStrategy: function (value) {
		if (arguments.length === 1) {
			this._percentKStrategy = value;
			return value;
		} else {
			return this._percentKStrategy;
		}
	}
	,
	init: function () {
		$.ig.IndicatorCalculationStrategy.prototype.init.call(this);
		this.percentKStrategy(new $.ig.PercentKCalculationStrategy());
	},
	basedOn: function (dataSource, supportingCalculations) {
		var list = new $.ig.List$1(String, 0);
		list.addRange(this.percentKStrategy().basedOn(dataSource, supportingCalculations));
		list.addRange(supportingCalculations.eMA().basedOn());
		return list;
	}
	,
	calculateIndicator: function (dataSource, supportingCalculations) {
		var percentK = this.percentKStrategy().provideStream(dataSource, supportingCalculations);
		var slowPercentK = supportingCalculations.eMA().strategy()(percentK, 3);
		var i = 0;
		var en = slowPercentK.getEnumerator();
		while (en.moveNext()) {
			var value = en.current();
			dataSource.indicatorColumn().item(i, value);
			i++;
		}
		return true;
	}
	,
	$type: new $.ig.Type('SlowStochasticOscillatorIndicatorStrategy', $.ig.IndicatorCalculationStrategy.prototype.$type)
}, true);

$.ig.util.defType('StandardDeviationIndicator', 'StrategyBasedIndicator', {
	init: function () {
		$.ig.StrategyBasedIndicator.prototype.init.call(this);
	},
	calculationStrategy: function () {
		return new $.ig.StandardDeviationIndicatorStrategy();
	}
	,
	styleKeyType: function () {
		return $.ig.StandardDeviationIndicator.prototype.$type;
	}
	,
	period: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.StandardDeviationIndicator.prototype.periodProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.StandardDeviationIndicator.prototype.periodProperty));
		}
	}
	,
	periodOverride: function () {
		return this.period();
	}
	,
	$type: new $.ig.Type('StandardDeviationIndicator', $.ig.StrategyBasedIndicator.prototype.$type)
}, true);

$.ig.util.defType('StandardDeviationIndicatorStrategy', 'IndicatorCalculationStrategy', {
	init: function () {
		$.ig.IndicatorCalculationStrategy.prototype.init.call(this);
	},
	basedOn: function (dataSource, supportingCalculations) {
		var list = new $.ig.List$1(String, 0);
		list.addRange(dataSource.typicalColumn().basedOn());
		list.addRange(supportingCalculations.sTDEV().basedOn());
		return list;
	}
	,
	calculateIndicator: function (dataSource, supportingCalculations) {
		var stddev = supportingCalculations.sTDEV().strategy()(dataSource.typicalColumn(), dataSource.period()).getEnumerator();
		var indicatorColumn = dataSource.indicatorColumn();
		var i = 0;
		while (stddev.moveNext()) {
			indicatorColumn.item(i, stddev.current());
			i++;
		}
		return true;
	}
	,
	$type: new $.ig.Type('StandardDeviationIndicatorStrategy', $.ig.IndicatorCalculationStrategy.prototype.$type)
}, true);

$.ig.util.defType('StochRSIIndicator', 'StrategyBasedIndicator', {
	init: function () {
		$.ig.StrategyBasedIndicator.prototype.init.call(this);
	},
	calculationStrategy: function () {
		return new $.ig.StochRSIIndicatorStrategy();
	}
	,
	styleKeyType: function () {
		return $.ig.StochRSIIndicator.prototype.$type;
	}
	,
	period: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.StochRSIIndicator.prototype.periodProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.StochRSIIndicator.prototype.periodProperty));
		}
	}
	,
	periodOverride: function () {
		return this.period();
	}
	,
	$type: new $.ig.Type('StochRSIIndicator', $.ig.StrategyBasedIndicator.prototype.$type)
}, true);

$.ig.util.defType('StochRSIIndicatorStrategy', 'IndicatorCalculationStrategy', {
	_rSIStrategy: null,
	rSIStrategy: function (value) {
		if (arguments.length === 1) {
			this._rSIStrategy = value;
			return value;
		} else {
			return this._rSIStrategy;
		}
	}
	,
	init: function () {
		$.ig.IndicatorCalculationStrategy.prototype.init.call(this);
		this.rSIStrategy(new $.ig.RelativeStrengthIndexIndicatorStrategy());
	},
	basedOn: function (dataSource, supportingCalculations) {
		var list = new $.ig.List$1(String, 0);
		list.addRange(this.rSIStrategy().basedOn(dataSource, supportingCalculations));
		return list;
	}
	,
	calculateIndicator: function (dataSource, supportingCalculations) {
		var RSI = this.rSIStrategy().provideStream(dataSource, supportingCalculations).getEnumerator();
		var period = dataSource.period();
		var i = 0;
		var buffer = new Array(period);
		for (i = 0; i < period; i++) {
			buffer[i] = 0;
		}
		i = 0;
		while (RSI.moveNext()) {
			var ago = Math.min(period, i);
			var highestHigh = -1.7976931348623157E+308;
			var lowestLow = 1.7976931348623157E+308;
			var cursor = i % period;
			for (var j = 0; j < ago; j++) {
				if (!$.ig.util.isNaN(buffer[ago - j - 1])) {
					highestHigh = Math.max(highestHigh, buffer[ago - j - 1]);
				}
				if (!$.ig.util.isNaN(buffer[ago - j - 1])) {
					lowestLow = Math.min(lowestLow, buffer[ago - j - 1]);
				}
			}
			buffer[cursor] = RSI.current();
			var stochRSI = supportingCalculations.makeSafe()((RSI.current() - lowestLow) / (highestHigh - lowestLow));
			dataSource.indicatorColumn().item(i, stochRSI);
			i++;
		}
		return true;
	}
	,
	$type: new $.ig.Type('StochRSIIndicatorStrategy', $.ig.IndicatorCalculationStrategy.prototype.$type)
}, true);

$.ig.util.defType('TRIXIndicator', 'StrategyBasedIndicator', {
	init: function () {
		$.ig.StrategyBasedIndicator.prototype.init.call(this);
		this.displayType($.ig.IndicatorDisplayType.prototype.line);
		this.trendLineBrush((function () {
			var $ret = new $.ig.Brush();
			$ret.fill("Blue");
			return $ret;
		}()));
		this.trendLineType($.ig.TrendLineType.prototype.simpleAverage);
		this.trendLinePeriod(9);
	},
	calculationStrategy: function () {
		return new $.ig.TRIXIndicatorStrategy();
	}
	,
	styleKeyType: function () {
		return $.ig.TRIXIndicator.prototype.$type;
	}
	,
	period: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.TRIXIndicator.prototype.periodProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.TRIXIndicator.prototype.periodProperty));
		}
	}
	,
	periodOverride: function () {
		return this.period();
	}
	,
	$type: new $.ig.Type('TRIXIndicator', $.ig.StrategyBasedIndicator.prototype.$type)
}, true);

$.ig.util.defType('TRIXIndicatorStrategy', 'IndicatorCalculationStrategy', {
	init: function () {
		$.ig.IndicatorCalculationStrategy.prototype.init.call(this);
	},
	basedOn: function (dataSource, supportingCalculations) {
		var list = new $.ig.List$1(String, 0);
		list.add($.ig.FinancialSeries.prototype.closeColumnPropertyName);
		list.addRange(supportingCalculations.eMA().basedOn());
		return list;
	}
	,
	calculateIndicator: function (dataSource, supportingCalculations) {
		var ema1 = supportingCalculations.eMA().strategy()(dataSource.closeColumn(), dataSource.period());
		var ema2 = supportingCalculations.eMA().strategy()(ema1, dataSource.period());
		var ema3 = supportingCalculations.eMA().strategy()(ema2, dataSource.period()).getEnumerator();
		var indicatorColumn = dataSource.indicatorColumn();
		if (indicatorColumn.count() > 0) {
			indicatorColumn.item(0, 0);
		}
		var i = 1;
		ema3.moveNext();
		var lastEMA = ema3.current();
		while (ema3.moveNext()) {
			indicatorColumn.item(i, supportingCalculations.makeSafe()((ema3.current() - lastEMA) / lastEMA));
			lastEMA = ema3.current();
			i++;
		}
		return true;
	}
	,
	$type: new $.ig.Type('TRIXIndicatorStrategy', $.ig.IndicatorCalculationStrategy.prototype.$type)
}, true);

$.ig.util.defType('TypicalPriceIndicator', 'StrategyBasedIndicator', {
	init: function () {
		$.ig.StrategyBasedIndicator.prototype.init.call(this);
	},
	calculationStrategy: function () {
		return new $.ig.TypicalPriceIndicatorStrategy();
	}
	,
	styleKeyType: function () {
		return $.ig.TypicalPriceIndicator.prototype.$type;
	}
	,
	$type: new $.ig.Type('TypicalPriceIndicator', $.ig.StrategyBasedIndicator.prototype.$type)
}, true);

$.ig.util.defType('TypicalPriceIndicatorStrategy', 'IndicatorCalculationStrategy', {
	init: function () {
		$.ig.IndicatorCalculationStrategy.prototype.init.call(this);
	},
	basedOn: function (dataSource, supportingCalculations) {
		var list = new $.ig.List$1(String, 0);
		list.addRange(dataSource.typicalColumn().basedOn());
		return list;
	}
	,
	calculateIndicator: function (dataSource, supportingCalculations) {
		var i = 0;
		var en = dataSource.typicalColumn().getEnumerator();
		while (en.moveNext()) {
			var value = en.current();
			dataSource.indicatorColumn().item(i, value);
			i++;
		}
		return true;
	}
	,
	$type: new $.ig.Type('TypicalPriceIndicatorStrategy', $.ig.IndicatorCalculationStrategy.prototype.$type)
}, true);

$.ig.util.defType('UltimateOscillatorIndicator', 'StrategyBasedIndicator', {
	init: function () {
		$.ig.StrategyBasedIndicator.prototype.init.call(this);
	},
	calculationStrategy: function () {
		return new $.ig.UltimateOscillatorIndicatorCalculationStrategy();
	}
	,
	styleKeyType: function () {
		return $.ig.UltimateOscillatorIndicator.prototype.$type;
	}
	,
	$type: new $.ig.Type('UltimateOscillatorIndicator', $.ig.StrategyBasedIndicator.prototype.$type)
}, true);

$.ig.util.defType('UltimateOscillatorIndicatorCalculationStrategy', 'IndicatorCalculationStrategy', {
	init: function () {
		$.ig.IndicatorCalculationStrategy.prototype.init.call(this);
	},
	basedOn: function (dataSource, supportingCalculations) {
		var list = new $.ig.List$1(String, 0);
		list.addRange(dataSource.trueLow().basedOn());
		list.add($.ig.FinancialSeries.prototype.closeColumnPropertyName);
		list.addRange(dataSource.trueRange().basedOn());
		list.addRange(supportingCalculations.movingSum().basedOn());
		return list;
	}
	,
	buyingPressure: function (dataSource) {
		var i = 0;
		var result = new $.ig.List$1(Number, 0);
		var tl = dataSource.trueLow().getEnumerator();
		while (tl.moveNext()) {
			result.add(dataSource.closeColumn().item(i) - tl.current());
			i++;
		}
		return result;
	}
	,
	calculateIndicator: function (dataSource, supportingCalculations) {
		var buyingPressure = this.buyingPressure(dataSource);
		var trueRange = dataSource.trueRange();
		var bpShort = supportingCalculations.movingSum().strategy()(buyingPressure, 7).getEnumerator();
		var trShort = supportingCalculations.movingSum().strategy()(trueRange, 7).getEnumerator();
		var bpMedium = supportingCalculations.movingSum().strategy()(buyingPressure, 14).getEnumerator();
		var trMedium = supportingCalculations.movingSum().strategy()(trueRange, 14).getEnumerator();
		var bpLong = supportingCalculations.movingSum().strategy()(buyingPressure, 28).getEnumerator();
		var trLong = supportingCalculations.movingSum().strategy()(trueRange, 28).getEnumerator();
		var i = 0;
		while (bpShort.moveNext() && trShort.moveNext() && bpMedium.moveNext() && trMedium.moveNext() && bpLong.moveNext() && trLong.moveNext()) {
			var rawValue = supportingCalculations.makeSafe()(4 * (bpShort.current() / trShort.current()) + 2 * (bpMedium.current() / trMedium.current()) + (bpLong.current() / trLong.current()));
			var uo = (rawValue / (4 + 2 + 1)) * 100;
			dataSource.indicatorColumn().item(i, uo);
			i++;
		}
		return true;
	}
	,
	$type: new $.ig.Type('UltimateOscillatorIndicatorCalculationStrategy', $.ig.IndicatorCalculationStrategy.prototype.$type)
}, true);

$.ig.util.defType('WeightedCloseIndicator', 'ItemwiseStrategyBasedIndicator', {
	init: function () {
		$.ig.ItemwiseStrategyBasedIndicator.prototype.init.call(this);
	},
	itemwiseStrategy: function () {
		return new $.ig.WeightedCloseIndicatorStrategy();
	}
	,
	styleKeyType: function () {
		return $.ig.WeightedCloseIndicator.prototype.$type;
	}
	,
	$type: new $.ig.Type('WeightedCloseIndicator', $.ig.ItemwiseStrategyBasedIndicator.prototype.$type)
}, true);

$.ig.util.defType('WeightedCloseIndicatorStrategy', 'ItemwiseIndicatorCalculationStrategy', {
	init: function () {
		$.ig.ItemwiseIndicatorCalculationStrategy.prototype.init.call(this);
	},
	basedOn: function (dataSource, supportingCalculations) {
		var list = new $.ig.List$1(String, 0);
		list.add($.ig.FinancialSeries.prototype.highColumnPropertyName);
		list.add($.ig.FinancialSeries.prototype.lowColumnPropertyName);
		list.add($.ig.FinancialSeries.prototype.closeColumnPropertyName);
		return list;
	}
	,
	calculateIndicatorItem: function (dataSource, supportingCalculations, currentIndex) {
		dataSource.indicatorColumn().item(currentIndex, (dataSource.highColumn().item(currentIndex) + dataSource.lowColumn().item(currentIndex) + (dataSource.closeColumn().item(currentIndex) * 2)) / 4);
		return true;
	}
	,
	$type: new $.ig.Type('WeightedCloseIndicatorStrategy', $.ig.ItemwiseIndicatorCalculationStrategy.prototype.$type)
}, true);

$.ig.util.defType('WilliamsPercentRIndicator', 'StrategyBasedIndicator', {
	init: function () {
		$.ig.StrategyBasedIndicator.prototype.init.call(this);
	},
	calculationStrategy: function () {
		return new $.ig.WilliamsPercentRIndicatorStrategy();
	}
	,
	styleKeyType: function () {
		return $.ig.WilliamsPercentRIndicator.prototype.$type;
	}
	,
	period: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.WilliamsPercentRIndicator.prototype.periodProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.WilliamsPercentRIndicator.prototype.periodProperty));
		}
	}
	,
	periodOverride: function () {
		return this.period();
	}
	,
	$type: new $.ig.Type('WilliamsPercentRIndicator', $.ig.StrategyBasedIndicator.prototype.$type)
}, true);

$.ig.util.defType('WilliamsPercentRIndicatorStrategy', 'IndicatorCalculationStrategy', {
	init: function () {
		$.ig.IndicatorCalculationStrategy.prototype.init.call(this);
	},
	basedOn: function (dataSource, supportingCalculations) {
		var list = new $.ig.List$1(String, 0);
		list.add($.ig.FinancialSeries.prototype.highColumnPropertyName);
		list.add($.ig.FinancialSeries.prototype.lowColumnPropertyName);
		list.add($.ig.FinancialSeries.prototype.closeColumnPropertyName);
		return list;
	}
	,
	calculateIndicator: function (dataSource, supportingCalculations) {
		var period = dataSource.period();
		var highColumn = dataSource.highColumn();
		var lowColumn = dataSource.lowColumn();
		for (var i = 0; i < dataSource.count(); i++) {
			var ago = Math.min(period, i);
			var highestHigh = -1.7976931348623157E+308;
			var lowestLow = 1.7976931348623157E+308;
			for (var j = 0; j < ago; j++) {
				if (!$.ig.util.isNaN(highColumn.item(i - j))) {
					highestHigh = Math.max(highestHigh, highColumn.item(i - j));
				}
				if (!$.ig.util.isNaN(lowColumn.item(i - j))) {
					lowestLow = Math.min(lowestLow, lowColumn.item(i - j));
				}
			}
			dataSource.indicatorColumn().item(i, supportingCalculations.makeSafe()((dataSource.closeColumn().item(i) - highestHigh) / (highestHigh - lowestLow) * 100));
		}
		return true;
	}
	,
	$type: new $.ig.Type('WilliamsPercentRIndicatorStrategy', $.ig.IndicatorCalculationStrategy.prototype.$type)
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

$.ig.util.defType('FinancialSeriesView', 'SeriesView', {
	_financialModel: null,
	financialModel: function (value) {
		if (arguments.length === 1) {
			this._financialModel = value;
			return value;
		} else {
			return this._financialModel;
		}
	}
	,
	_bucketCalculator: null,
	bucketCalculator: function (value) {
		if (arguments.length === 1) {
			this._bucketCalculator = value;
			return value;
		} else {
			return this._bucketCalculator;
		}
	}
	,
	_frameVersion: 0,
	frameVersion: function (value) {
		if (arguments.length === 1) {
			this._frameVersion = value;
			return value;
		} else {
			return this._frameVersion;
		}
	}
	,
	checkFrameDirty: function (frame) {
		if (this.frameVersion() != frame.frameVersion()) {
			return true;
		}
		return false;
	}
	,
	updateFrameVersion: function (frame) {
		this.frameVersion(frame.frameVersion());
	}
	,
	init: function (model) {
		$.ig.SeriesView.prototype.init.call(this, model);
		this.frameVersion(-1);
		this.financialModel(model);
		this.bucketCalculator(this.createBucketCalculator());
	},
	createBucketCalculator: function () {
	}
	,
	onInit: function () {
		$.ig.SeriesView.prototype.onInit.call(this);
		if (!this.isThumbnailView()) {
			this.model().legendItemBadgeTemplate((function () {
				var $ret = new $.ig.DataTemplate();
				$ret.render($.ig.LegendTemplates.prototype.financialBadgeTemplate);
				$ret.measure($.ig.LegendTemplates.prototype.legendItemBadgeMeasure);
				return $ret;
			}()));
		}
	}
	,
	getDefaultTooltipTemplate: function () {
		var tooltipString = "<div class='ui-chart-default-tooltip-content'>";
		var dateTimeAxis = $.ig.util.cast($.ig.CategoryDateTimeXAxis.prototype.$type, this.financialModel().xAxis());
		if (dateTimeAxis != null) {
			tooltipString += "<span>${item." + dateTimeAxis.dateTimeMemberPath() + "}</span><br/>";
		} else if (this.financialModel().xAxis().label() != null) {
			tooltipString += "<span>${item." + this.financialModel().xAxis().label() + "}</span><br/>";
		}
		tooltipString += "<span class='ui-priority-primary'";
		var nonTransparentOutline = this.model().actualOutline() != null && $.ig.Color.prototype.l_op_Inequality_Lifted($.ig.util.toNullable($.ig.Color.prototype.$type, this.model().actualOutline().color()), $.ig.util.toNullable($.ig.Color.prototype.$type, null)) && this.model().actualOutline().color().a() > 0;
		if (nonTransparentOutline) {
			tooltipString += " style='color:" + this.model().actualOutline().__fill + "'";
		}
		tooltipString += ">" + this.financialModel().title() + "</span><table><tr><td>" + "Open:</td><td>" + "${item." + this.financialModel().openMemberPath() + "}</td></tr><tr><td>" + "High:</td><td>" + "${item." + this.financialModel().highMemberPath() + "}</td></tr><tr><td>" + "Low:</td><td>" + "${item." + this.financialModel().lowMemberPath() + "}</td></tr><tr><td>" + "Close:</td><td>" + "${item." + this.financialModel().closeMemberPath() + "}</td></tr>";
		if (!String.isNullOrEmpty(this.financialModel().volumeMemberPath())) {
			tooltipString += "<tr><td>" + "Volume:</td><td>" + "${item." + this.financialModel().volumeMemberPath() + "}</td></tr>";
		}
		tooltipString += "</table></div>";
		return tooltipString;
	}
	,
	applyDropShadowDefaultSettings: function () {
		var color = new $.ig.Color();
		color.colorString("rgba(95,95,95,0.5)");
		this.model().shadowColor(color);
		this.model().shadowBlur(5);
		this.model().shadowOffsetX(2);
		this.model().shadowOffsetY(2);
	}
	,
	$type: new $.ig.Type('FinancialSeriesView', $.ig.SeriesView.prototype.$type)
}, true);

$.ig.util.defType('BollingerBandsOverlayView', 'FinancialSeriesView', {
	_bollingerBandsOverlayModel: null,
	bollingerBandsOverlayModel: function (value) {
		if (arguments.length === 1) {
			this._bollingerBandsOverlayModel = value;
			return value;
		} else {
			return this._bollingerBandsOverlayModel;
		}
	}
	,
	init: function (model) {
		this._polygon = new $.ig.Polygon();
		this._polyline0 = new $.ig.Polyline();
		this._polyline1 = new $.ig.Polyline();
		this._polyline2 = new $.ig.Polyline();
		this.__hitPolygon0 = new $.ig.Polygon();
		this.__hitPolyline0 = new $.ig.Polyline();
		this.__hitPolyline1 = new $.ig.Polyline();
		this.__hitPolyline2 = new $.ig.Polyline();
		$.ig.FinancialSeriesView.prototype.init.call(this, model);
		this.bollingerBandsOverlayModel(model);
	},
	onInit: function () {
		$.ig.FinancialSeriesView.prototype.onInit.call(this);
		if (!this.isThumbnailView()) {
			this.model().legendItemBadgeTemplate((function () {
				var $ret = new $.ig.DataTemplate();
				$ret.render($.ig.LegendTemplates.prototype.rectBadgeTemplate);
				$ret.measure($.ig.LegendTemplates.prototype.legendItemBadgeMeasure);
				return $ret;
			}()));
		}
	}
	,
	createBucketCalculator: function () {
		return new $.ig.BollingerBandsBucketCalculator(this);
	}
	,
	renderBands: function (count, px, nx, y0, y1, y2) {
		var en = $.ig.Flattener.prototype.flatten3(count, px, y0, this.model().resolution()).getEnumerator();
		while (en.moveNext()) {
			var i = en.current();
			this._polygon.points().add({ __x: px(i), __y: y0(i), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			this._polyline0.points().add({ __x: px(i), __y: y0(i), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		}
		var en1 = $.ig.Flattener.prototype.flatten3(count, px, y1, this.model().resolution()).getEnumerator();
		while (en1.moveNext()) {
			var i1 = en1.current();
			this._polyline1.points().add({ __x: px(i1), __y: y1(i1), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		}
		var en2 = $.ig.Flattener.prototype.flatten3(count, nx, y2, this.model().resolution()).getEnumerator();
		while (en2.moveNext()) {
			var i2 = en2.current();
			this._polygon.points().add({ __x: nx(i2), __y: y2(i2), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			this._polyline2.points().add({ __x: nx(i2), __y: y2(i2), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		}
		this.makeDirty();
	}
	,
	clearRendering: function () {
		this._polygon.points().clear();
		this._polyline0.points().clear();
		this._polyline1.points().clear();
		this._polyline2.points().clear();
		this.makeDirty();
	}
	,
	fillArea: function () {
		return this._polygon;
	}
	,
	line0: function () {
		return this._polyline0;
	}
	,
	line1: function () {
		return this._polyline1;
	}
	,
	line2: function () {
		return this._polyline2;
	}
	,
	_polygon: null,
	_polyline0: null,
	_polyline1: null,
	_polyline2: null,
	__hitPolygon0: null,
	__hitPolyline0: null,
	__hitPolyline1: null,
	__hitPolyline2: null,
	setupHitAppearanceOverride: function () {
		$.ig.FinancialSeriesView.prototype.setupHitAppearanceOverride.call(this);
		this.__hitPolygon0.points(this._polygon.points());
		this.__hitPolyline0.points(this._polyline0.points());
		this.__hitPolyline1.points(this._polyline1.points());
		this.__hitPolyline2.points(this._polyline2.points());
		var hitBrush = this.getHitBrush();
		this.__hitPolygon0.__fill = hitBrush;
		this.__hitPolygon0.__opacity = 1;
		this.__hitPolyline0.__stroke = hitBrush;
		this.__hitPolyline0.strokeThickness(this.model().thickness() + $.ig.SeriesView.prototype.hIT_THICKNESS_AUGMENT);
		this.__hitPolyline1.__stroke = hitBrush;
		this.__hitPolyline1.strokeThickness(this.model().thickness() + $.ig.SeriesView.prototype.hIT_THICKNESS_AUGMENT);
		this.__hitPolyline2.__stroke = hitBrush;
		this.__hitPolyline2.strokeThickness(this.model().thickness() + $.ig.SeriesView.prototype.hIT_THICKNESS_AUGMENT);
	}
	,
	renderOverride: function (context, isHitContext) {
		$.ig.FinancialSeriesView.prototype.renderOverride.call(this, context, isHitContext);
		if (context.shouldRender()) {
			if (isHitContext) {
				context.renderPolygon(this.__hitPolygon0);
				context.renderPolyline(this.__hitPolyline0);
				context.renderPolyline(this.__hitPolyline1);
				context.renderPolyline(this.__hitPolyline2);
			} else {
				context.renderPolygon(this._polygon);
				context.renderPolyline(this._polyline0);
				context.renderPolyline(this._polyline1);
				context.renderPolyline(this._polyline2);
			}
		}
	}
	,
	exportViewShapes: function (svd) {
		$.ig.FinancialSeriesView.prototype.exportViewShapes.call(this, svd);
		var fill = new $.ig.PolygonVisualData(1, "FillShape", this._polygon);
		fill.tags().add("Fill");
		var bottom = new $.ig.PolyLineVisualData(1, "BottomLine", this._polyline0);
		bottom.tags().add("Main");
		bottom.tags().add("Lower");
		var center = new $.ig.PolyLineVisualData(1, "CentralLine", this._polyline1);
		center.tags().add("Central");
		var top = new $.ig.PolyLineVisualData(1, "TopLine", this._polyline2);
		top.tags().add("Upper");
		svd.shapes().add(fill);
		svd.shapes().add(bottom);
		svd.shapes().add(center);
		svd.shapes().add(top);
	}
	,
	$type: new $.ig.Type('BollingerBandsOverlayView', $.ig.FinancialSeriesView.prototype.$type)
}, true);

$.ig.util.defType('FinancialIndicatorView', 'FinancialSeriesView', {
	_indicatorModel: null,
	indicatorModel: function (value) {
		if (arguments.length === 1) {
			this._indicatorModel = value;
			return value;
		} else {
			return this._indicatorModel;
		}
	}
	,
	init: function (model) {
		var $self = this;
		this._positivePath0 = (function () {
			var $ret = new $.ig.Path();
			$ret.data(new $.ig.PathGeometry());
			return $ret;
		}());
		this._negativePath0 = (function () {
			var $ret = new $.ig.Path();
			$ret.data(new $.ig.PathGeometry());
			return $ret;
		}());
		this._positivePath01 = (function () {
			var $ret = new $.ig.Path();
			$ret.data(new $.ig.PathGeometry());
			return $ret;
		}());
		this._negativePath01 = (function () {
			var $ret = new $.ig.Path();
			$ret.data(new $.ig.PathGeometry());
			return $ret;
		}());
		this._positivePath1 = (function () {
			var $ret = new $.ig.Path();
			$ret.data(new $.ig.PathGeometry());
			return $ret;
		}());
		this._negativePath1 = (function () {
			var $ret = new $.ig.Path();
			$ret.data(new $.ig.PathGeometry());
			return $ret;
		}());
		this._positiveColumns = (function () {
			var $ret = new $.ig.Path();
			$ret.data(new $.ig.GeometryGroup());
			return $ret;
		}());
		this._negativeColumns = (function () {
			var $ret = new $.ig.Path();
			$ret.data(new $.ig.GeometryGroup());
			return $ret;
		}());
		this.__hitPositivePath0 = new $.ig.Path();
		this.__hitPositivePath1 = new $.ig.Path();
		this.__hitPositivePath01 = new $.ig.Path();
		this.__hitPositiveColumns = new $.ig.Path();
		this.__hitNegativePath0 = new $.ig.Path();
		this.__hitNegativePath1 = new $.ig.Path();
		this.__hitNegativePath01 = new $.ig.Path();
		this.__hitNegativeColumns = new $.ig.Path();
		$.ig.FinancialSeriesView.prototype.init.call(this, model);
		this.indicatorModel(model);
		this._columns = (function () {
			var $ret = new $.ig.Pool$1($.ig.LineGeometry.prototype.$type);
			$ret.create($self.createColumn.runOn($self));
			$ret.destroy($self.destroyColumn.runOn($self));
			return $ret;
		}());
		this.trendLineManager(new $.ig.CategoryTrendLineManager());
	},
	createBucketCalculator: function () {
		return new $.ig.FinancialIndicatorBucketCalculator(this);
	}
	,
	onInit: function () {
		$.ig.FinancialSeriesView.prototype.onInit.call(this);
		if (!this.isThumbnailView()) {
			this.financialModel().negativeBrush((function () {
				var $ret = new $.ig.Brush();
				$ret.fill("#415460");
				return $ret;
			}()));
		}
	}
	,
	_columns: null,
	createColumn: function () {
		return new $.ig.LineGeometry();
	}
	,
	destroyColumn: function (column) {
	}
	,
	_positivePath0: null,
	_negativePath0: null,
	_positivePath01: null,
	_negativePath01: null,
	_positivePath1: null,
	_negativePath1: null,
	_positiveColumns: null,
	_negativeColumns: null,
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
	clearIndicatorVisual: function (wipeClean) {
		$.ig.Extensions.prototype.reset1((this._positivePath0.data()));
		$.ig.Extensions.prototype.reset1((this._positivePath01.data()));
		$.ig.Extensions.prototype.reset1((this._positivePath1.data()));
		$.ig.Extensions.prototype.reset1((this._negativePath0.data()));
		$.ig.Extensions.prototype.reset1((this._negativePath01.data()));
		$.ig.Extensions.prototype.reset1((this._negativePath1.data()));
		$.ig.Extensions.prototype.reset((this._positiveColumns.data()));
		$.ig.Extensions.prototype.reset((this._negativeColumns.data()));
		if (wipeClean) {
			this._columns.count(0);
		}
		this.makeDirty();
	}
	,
	updateHitTests: function () {
	}
	,
	rasterizeLine: function (count, x0, y0, x1, y1, colorByGradient) {
		$.ig.IndicatorRenderer.prototype.rasterizeLine(count, x0, y0, x1, y1, colorByGradient, this.windowRect(), this.viewport(), this._positivePath0, this._positivePath01, this._positivePath1, this._negativePath0, this._negativePath01, this._negativePath1, this.bucketCalculator().bucketSize(), this.model().resolution());
		this.makeDirty();
	}
	,
	rasterizeArea: function (count, x0, y0, x1, y1, colorByGradient, worldZero) {
		$.ig.IndicatorRenderer.prototype.rasterizeArea(count, x0, y0, x1, y1, colorByGradient, this.windowRect(), this.viewport(), this._positivePath0, this._positivePath01, this._positivePath1, this._negativePath0, this._negativePath01, this._negativePath1, worldZero, this.bucketCalculator().bucketSize(), this.model().resolution());
		this.makeDirty();
	}
	,
	rasterizeColumns: function (count, x0, y0, x1, y1, colorByGradient, worldZero) {
		$.ig.IndicatorRenderer.prototype.rasterizeColumns(count, x0, y0, x1, y1, colorByGradient, worldZero, this._columns, this._positiveColumns, this._negativeColumns);
		this.makeDirty();
	}
	,
	updateTrendlineBrush: function () {
		this.indicatorModel().actualTrendLineBrush(null);
		if (this.indicatorModel().trendLineBrush() != null) {
			this.indicatorModel().actualTrendLineBrush(this.indicatorModel().trendLineBrush());
		} else {
			this.indicatorModel().actualTrendLineBrush(this.indicatorModel().actualBrush());
		}
	}
	,
	__hitPositivePath0: null,
	__hitPositivePath1: null,
	__hitPositivePath01: null,
	__hitPositiveColumns: null,
	__hitNegativePath0: null,
	__hitNegativePath1: null,
	__hitNegativePath01: null,
	__hitNegativeColumns: null,
	setupHitAppearanceOverride: function () {
		$.ig.FinancialSeriesView.prototype.setupHitAppearanceOverride.call(this);
		var hitBrush = this.getHitBrush();
		this.__hitPositivePath0.data(this._positivePath0.data());
		this.__hitPositivePath1.data(this._positivePath1.data());
		this.__hitPositivePath01.data(this._positivePath01.data());
		this.__hitPositiveColumns.data(this._positiveColumns.data());
		this.__hitNegativePath0.data(this._negativePath0.data());
		this.__hitNegativePath1.data(this._negativePath1.data());
		this.__hitNegativePath01.data(this._negativePath01.data());
		this.__hitNegativeColumns.data(this._negativeColumns.data());
		this.__hitPositivePath0.__stroke = hitBrush;
		this.__hitPositivePath1.__stroke = hitBrush;
		this.__hitPositivePath0.strokeThickness(this.model().thickness() + $.ig.SeriesView.prototype.hIT_THICKNESS_AUGMENT);
		this.__hitPositivePath1.strokeThickness(this.model().thickness() + $.ig.SeriesView.prototype.hIT_THICKNESS_AUGMENT);
		this.__hitPositivePath01.__fill = hitBrush;
		this.__hitPositivePath01.__opacity = 1;
		this.__hitPositiveColumns.__stroke = hitBrush;
		this.__hitPositiveColumns.strokeThickness(this.model().thickness() + $.ig.SeriesView.prototype.hIT_THICKNESS_AUGMENT);
		this.__hitNegativePath0.__stroke = hitBrush;
		this.__hitNegativePath1.__stroke = hitBrush;
		this.__hitNegativePath0.strokeThickness(this.model().thickness() + $.ig.SeriesView.prototype.hIT_THICKNESS_AUGMENT);
		this.__hitNegativePath1.strokeThickness(this.model().thickness() + $.ig.SeriesView.prototype.hIT_THICKNESS_AUGMENT);
		this.__hitNegativePath01.__fill = hitBrush;
		this.__hitNegativePath01.__opacity = 1;
		this.__hitNegativeColumns.__stroke = hitBrush;
		this.__hitNegativeColumns.strokeThickness(this.model().thickness() + $.ig.SeriesView.prototype.hIT_THICKNESS_AUGMENT);
	}
	,
	renderOverride: function (context, isHitContext) {
		$.ig.FinancialSeriesView.prototype.renderOverride.call(this, context, isHitContext);
		if (context.shouldRender()) {
			if (isHitContext) {
				if (this.indicatorModel().displayType() == $.ig.IndicatorDisplayType.prototype.column) {
					context.renderPath(this.__hitPositiveColumns);
					context.renderPath(this.__hitNegativeColumns);
				} else {
					context.renderPath(this.__hitPositivePath01);
					context.renderPath(this.__hitPositivePath0);
					context.renderPath(this.__hitPositivePath1);
					context.renderPath(this.__hitNegativePath01);
					context.renderPath(this.__hitNegativePath0);
					context.renderPath(this.__hitNegativePath1);
				}
			} else {
				if (this.indicatorModel().displayType() == $.ig.IndicatorDisplayType.prototype.column) {
					context.renderPath(this._positiveColumns);
					context.renderPath(this._negativeColumns);
				} else {
					context.renderPath(this._positivePath01);
					context.renderPath(this._positivePath0);
					context.renderPath(this._positivePath1);
					context.renderPath(this._negativePath01);
					context.renderPath(this._negativePath0);
					context.renderPath(this._negativePath1);
				}
			}
		}
	}
	,
	renderMarkersOverride: function (context, isHitContext) {
		if (context.shouldRender()) {
			if (this.indicatorModel().trendLineType() != $.ig.TrendLineType.prototype.none && !isHitContext) {
				var polyline = this.trendLineManager().trendPolyline();
				polyline.strokeThickness(this.indicatorModel().trendLineThickness());
				polyline.__stroke = this.indicatorModel().actualTrendLineBrush();
				polyline.strokeDashCap(this.indicatorModel().trendLineDashCap());
				polyline.strokeDashArray(this.indicatorModel().trendLineDashArray());
				context.renderPolyline(polyline);
			}
		}
		$.ig.FinancialSeriesView.prototype.renderMarkersOverride.call(this, context, isHitContext);
	}
	,
	exportViewShapes: function (svd) {
		$.ig.FinancialSeriesView.prototype.exportViewShapes.call(this, svd);
		var positiveColumnsShape = new $.ig.PathVisualData(1, "positiveColumnsShape", this._positiveColumns);
		positiveColumnsShape.tags().add("Positive");
		positiveColumnsShape.tags().add("Main");
		var negativeColumnsShape = new $.ig.PathVisualData(1, "negativeColumnsShape", this._negativeColumns);
		negativeColumnsShape.tags().add("Negative");
		var positive0 = new $.ig.PathVisualData(1, "positive0", this._positivePath0);
		positive0.tags().add("Positive");
		positive0.tags().add("Main");
		var positive1 = new $.ig.PathVisualData(1, "positive1", this._positivePath1);
		positive1.tags().add("Positive");
		var positive01 = new $.ig.PathVisualData(1, "positive01", this._positivePath01);
		positive01.tags().add("PositiveFill");
		var negative0 = new $.ig.PathVisualData(1, "negative0", this._negativePath0);
		negative0.tags().add("Negative");
		negative0.tags().add("Main");
		var negative1 = new $.ig.PathVisualData(1, "negative1", this._negativePath1);
		negative1.tags().add("Negative");
		var negative01 = new $.ig.PathVisualData(1, "negative01", this._negativePath01);
		negative01.tags().add("NegativeFill");
		svd.shapes().add(positiveColumnsShape);
		svd.shapes().add(negativeColumnsShape);
		svd.shapes().add(positive0);
		svd.shapes().add(positive1);
		svd.shapes().add(positive01);
		svd.shapes().add(negative0);
		svd.shapes().add(negative1);
		svd.shapes().add(negative01);
	}
	,
	selectTrendlineManager: function () {
		this.trendLineManager($.ig.CategoryTrendLineManagerBase.prototype.selectManager(this.trendLineManager(), this.indicatorModel().xAxis(), this.model().rootCanvas(), this.indicatorModel()));
	}
	,
	$type: new $.ig.Type('FinancialIndicatorView', $.ig.FinancialSeriesView.prototype.$type)
}, true);

$.ig.util.defType('FinancialPriceSeriesView', 'FinancialSeriesView', {
	_financialPriceModel: null,
	financialPriceModel: function (value) {
		if (arguments.length === 1) {
			this._financialPriceModel = value;
			return value;
		} else {
			return this._financialPriceModel;
		}
	}
	,
	init: function (model) {
		var $self = this;
		this._positivePath = (function () {
			var $ret = new $.ig.Path();
			$ret.data(new $.ig.GeometryGroup());
			return $ret;
		}());
		this._negativePath = (function () {
			var $ret = new $.ig.Path();
			$ret.data(new $.ig.GeometryGroup());
			return $ret;
		}());
		this.__hitItem = new $.ig.Path();
		$.ig.FinancialSeriesView.prototype.init.call(this, model);
		this.financialPriceModel(model);
		this.visibleShapes(new $.ig.List$1($.ig.Path.prototype.$type, 0));
		this.trendLineManager(new $.ig.CategoryTrendLineManager());
		this.shapes((function () {
			var $ret = new $.ig.Pool$1($.ig.Path.prototype.$type);
			$ret.create($self.shapeCreate.runOn($self));
			$ret.activate($self.shapeActivate.runOn($self));
			$ret.disactivate($self.shapeDisactivate.runOn($self));
			$ret.destroy($self.shapeDestroy.runOn($self));
			return $ret;
		}()));
	},
	_shapes: null,
	shapes: function (value) {
		if (arguments.length === 1) {
			this._shapes = value;
			return value;
		} else {
			return this._shapes;
		}
	}
	,
	shapeCreate: function () {
		var shape = new $.ig.Path();
		this.visibleShapes().add(shape);
		shape.__visibility = $.ig.Visibility.prototype.collapsed;
		return shape;
	}
	,
	_visibleShapes: null,
	visibleShapes: function (value) {
		if (arguments.length === 1) {
			this._visibleShapes = value;
			return value;
		} else {
			return this._visibleShapes;
		}
	}
	,
	shapeActivate: function (shape) {
		shape.__visibility = $.ig.Visibility.prototype.visible;
	}
	,
	shapeDisactivate: function (shape) {
		shape.__visibility = $.ig.Visibility.prototype.collapsed;
	}
	,
	shapeDestroy: function (shape) {
		this.visibleShapes().remove(shape);
	}
	,
	createBucketCalculator: function () {
		return new $.ig.FinancialPriceBucketCalculator(this);
	}
	,
	onInit: function () {
		$.ig.FinancialSeriesView.prototype.onInit.call(this);
		if (!this.isThumbnailView()) {
			this.financialModel().negativeBrush((function () {
				var $ret = new $.ig.Brush();
				$ret.fill("#415460");
				return $ret;
			}()));
			this.financialModel().resolution(2);
			this.financialModel().thickness(1);
			this.financialModel().outline((function () {
				var $ret = new $.ig.Brush();
				$ret.fill("#222222");
				return $ret;
			}()));
		}
	}
	,
	_positivePath: null,
	_negativePath: null,
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
	updatePathBrushes: function () {
		if (this.financialPriceModel().displayType() == $.ig.PriceDisplayType.prototype.oHLC) {
			this._positivePath.__stroke = this.model().actualBrush();
		} else {
			this._positivePath.__stroke = this.model().actualOutline();
		}
		if (this.financialPriceModel().displayType() == $.ig.PriceDisplayType.prototype.oHLC) {
			this._negativePath.__stroke = this.financialModel().negativeBrush();
		} else {
			this._negativePath.__stroke = this.model().actualOutline();
		}
	}
	,
	clearPriceSymbols: function () {
		this.makeDirty();
		this.shapes().count(0);
		this.clearGroupedPriceSymbols();
	}
	,
	clearGroupedPriceSymbols: function () {
		var positiveGroup = $.ig.util.cast($.ig.GeometryGroup.prototype.$type, this._positivePath.data());
		var negativeGroup = $.ig.util.cast($.ig.GeometryGroup.prototype.$type, this._negativePath.data());
		$.ig.Extensions.prototype.reset(positiveGroup);
		$.ig.Extensions.prototype.reset(negativeGroup);
	}
	,
	getPositiveGroup: function () {
		this.makeDirty();
		return $.ig.util.cast($.ig.GeometryGroup.prototype.$type, this._positivePath.data());
	}
	,
	getNegativeGroup: function () {
		this.makeDirty();
		return $.ig.util.cast($.ig.GeometryGroup.prototype.$type, this._negativePath.data());
	}
	,
	updateTrendlineBrush: function () {
		this.financialPriceModel().actualTrendLineBrush(null);
		if (this.financialPriceModel().trendLineBrush() != null) {
			this.financialPriceModel().actualTrendLineBrush(this.financialPriceModel().trendLineBrush());
		} else {
			this.financialPriceModel().actualTrendLineBrush(this.financialPriceModel().actualBrush());
		}
	}
	,
	setupAppearanceOverride: function () {
		$.ig.FinancialSeriesView.prototype.setupAppearanceOverride.call(this);
		this._positivePath.strokeThickness(this.model().thickness());
		this._positivePath.strokeDashArray(this.model().dashArray());
		this._positivePath.strokeDashCap(this.model().dashCap());
		if (this.financialPriceModel().displayType() == $.ig.PriceDisplayType.prototype.oHLC) {
			this._positivePath.__stroke = this.model().actualBrush();
		} else {
			this._positivePath.__stroke = this.model().actualOutline();
			this._positivePath.__fill = this.model().actualBrush();
		}
		this._negativePath.strokeThickness(this.model().thickness());
		this._negativePath.strokeDashArray(this.model().dashArray());
		this._negativePath.strokeDashCap(this.model().dashCap());
		if (this.financialPriceModel().displayType() == $.ig.PriceDisplayType.prototype.oHLC) {
			this._negativePath.__stroke = this.financialModel().negativeBrush();
		} else {
			this._negativePath.__stroke = this.model().actualOutline();
			this._negativePath.__fill = this.financialModel().negativeBrush();
		}
	}
	,
	setupHitAppearanceOverride: function () {
		$.ig.FinancialSeriesView.prototype.setupHitAppearanceOverride.call(this);
		var hitBrush = this.getHitBrush();
		this._positivePath.strokeThickness(this.model().thickness() + $.ig.SeriesView.prototype.hIT_THICKNESS_AUGMENT);
		if (this.financialPriceModel().displayType() == $.ig.PriceDisplayType.prototype.oHLC) {
			this._positivePath.__stroke = hitBrush;
		} else {
			this._positivePath.__stroke = hitBrush;
			this._positivePath.__fill = hitBrush;
		}
		this._negativePath.strokeThickness(this.model().thickness() + $.ig.SeriesView.prototype.hIT_THICKNESS_AUGMENT);
		if (this.financialPriceModel().displayType() == $.ig.PriceDisplayType.prototype.oHLC) {
			this._negativePath.__stroke = hitBrush;
		} else {
			this._negativePath.__stroke = hitBrush;
			this._negativePath.__fill = hitBrush;
		}
	}
	,
	getItem: function (index) {
		return this.visibleShapes().__inner[index];
	}
	,
	__hitItem: null,
	getHitItem: function (index) {
		var item = this.visibleShapes().__inner[index];
		this.__hitItem.__visibility = item.__visibility;
		this.__hitItem.data(item.data());
		var hitBrush = this.getHitBrush1(index);
		this.__hitItem.__fill = hitBrush;
		this.__hitItem.__stroke = hitBrush;
		this.__hitItem.strokeThickness(this.model().thickness() + $.ig.SeriesView.prototype.hIT_THICKNESS_AUGMENT);
		return this.__hitItem;
	}
	,
	renderOverride: function (context, isHitContext) {
		$.ig.FinancialSeriesView.prototype.renderOverride.call(this, context, isHitContext);
		if (context.shouldRender()) {
			if (this.separateMode()) {
				for (var i = 0; i < this.visibleShapes().count(); i++) {
					var shape = this.getCurrentItem(i, isHitContext);
					if (shape.__visibility == $.ig.Visibility.prototype.visible) {
						context.renderPath(shape);
					}
				}
			} else {
				context.renderPath(this._positivePath);
				context.renderPath(this._negativePath);
			}
		}
	}
	,
	renderMarkersOverride: function (context, isHitContext) {
		if (context.shouldRender()) {
			if (this.financialPriceModel().trendLineType() != $.ig.TrendLineType.prototype.none && !isHitContext) {
				var polyline = this.trendLineManager().trendPolyline();
				polyline.strokeThickness(this.financialPriceModel().trendLineThickness());
				polyline.__stroke = this.financialPriceModel().actualTrendLineBrush();
				polyline.strokeDashArray(this.financialPriceModel().trendLineDashArray());
				polyline.strokeDashCap(this.financialPriceModel().trendLineDashCap());
				context.renderPolyline(polyline);
			}
		}
		$.ig.FinancialSeriesView.prototype.renderMarkersOverride.call(this, context, isHitContext);
	}
	,
	exportViewShapes: function (svd) {
		$.ig.FinancialSeriesView.prototype.exportViewShapes.call(this, svd);
		var positivePathShape = new $.ig.PathVisualData(1, "positivePathShape", this._positivePath);
		positivePathShape.tags().add("Positive");
		positivePathShape.tags().add("Main");
		var negativePathShape = new $.ig.PathVisualData(1, "negativePathShape", this._negativePath);
		negativePathShape.tags().add("Negative");
		svd.shapes().add(positivePathShape);
		svd.shapes().add(negativePathShape);
	}
	,
	frameRendered: function () {
		this.makeDirty();
	}
	,
	_separateMode: false,
	separateMode: function (value) {
		if (arguments.length === 1) {
			this._separateMode = value;
			return value;
		} else {
			return this._separateMode;
		}
	}
	,
	selectTrendlineManager: function () {
		this.trendLineManager($.ig.CategoryTrendLineManagerBase.prototype.selectManager(this.trendLineManager(), this.financialModel().xAxis(), this.model().rootCanvas(), this.financialModel()));
	}
	,
	$type: new $.ig.Type('FinancialPriceSeriesView', $.ig.FinancialSeriesView.prototype.$type)
}, true);

$.ig.util.defType('PriceChannelOverlayView', 'FinancialSeriesView', {
	_polygon: null,
	_polyline0: null,
	_polyline1: null,
	fillArea: function () {
		return this._polygon;
	}
	,
	line0: function () {
		return this._polyline0;
	}
	,
	line1: function () {
		return this._polyline1;
	}
	,
	_priceChannelOverlayModel: null,
	priceChannelOverlayModel: function (value) {
		if (arguments.length === 1) {
			this._priceChannelOverlayModel = value;
			return value;
		} else {
			return this._priceChannelOverlayModel;
		}
	}
	,
	init: function (model) {
		this._polygon = new $.ig.Polygon();
		this._polyline0 = new $.ig.Polyline();
		this._polyline1 = new $.ig.Polyline();
		this.__hitPolygon0 = new $.ig.Polygon();
		this.__hitPolyline0 = new $.ig.Polyline();
		this.__hitPolyline1 = new $.ig.Polyline();
		$.ig.FinancialSeriesView.prototype.init.call(this, model);
		this.priceChannelOverlayModel(model);
	},
	onInit: function () {
		$.ig.FinancialSeriesView.prototype.onInit.call(this);
		if (!this.isThumbnailView()) {
			this.model().legendItemBadgeTemplate((function () {
				var $ret = new $.ig.DataTemplate();
				$ret.render($.ig.LegendTemplates.prototype.rectBadgeTemplate);
				$ret.measure($.ig.LegendTemplates.prototype.legendItemBadgeMeasure);
				return $ret;
			}()));
		}
	}
	,
	createBucketCalculator: function () {
		return new $.ig.PriceChannelBucketCalculator(this);
	}
	,
	clearRendering: function () {
		this._polygon.points().clear();
		this._polyline0.points().clear();
		this._polyline1.points().clear();
		this.makeDirty();
	}
	,
	renderChannel: function (count, px, nx, y0, y1) {
		var en = $.ig.Flattener.prototype.flatten3(count, px, y0, this.model().resolution()).getEnumerator();
		while (en.moveNext()) {
			var i = en.current();
			this._polygon.points().add({ __x: px(i), __y: y0(i), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			this._polyline0.points().add({ __x: px(i), __y: y0(i), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		}
		var en1 = $.ig.Flattener.prototype.flatten3(count, nx, y1, this.model().resolution()).getEnumerator();
		while (en1.moveNext()) {
			var i1 = en1.current();
			this._polygon.points().add({ __x: nx(i1), __y: y1(i1), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			this._polyline1.points().add({ __x: nx(i1), __y: y1(i1), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		}
		this.makeDirty();
	}
	,
	__hitPolygon0: null,
	__hitPolyline0: null,
	__hitPolyline1: null,
	setupHitAppearanceOverride: function () {
		$.ig.FinancialSeriesView.prototype.setupHitAppearanceOverride.call(this);
		this.__hitPolygon0.points(this._polygon.points());
		this.__hitPolyline0.points(this._polyline0.points());
		this.__hitPolyline1.points(this._polyline1.points());
		var hitBrush = this.getHitBrush();
		this.__hitPolygon0.__fill = hitBrush;
		this.__hitPolygon0.__opacity = 1;
		this.__hitPolyline0.__stroke = hitBrush;
		this.__hitPolyline0.strokeThickness(this.model().thickness() + $.ig.SeriesView.prototype.hIT_THICKNESS_AUGMENT);
		this.__hitPolyline1.__stroke = hitBrush;
		this.__hitPolyline1.strokeThickness(this.model().thickness() + $.ig.SeriesView.prototype.hIT_THICKNESS_AUGMENT);
	}
	,
	renderOverride: function (context, isHitContext) {
		$.ig.FinancialSeriesView.prototype.renderOverride.call(this, context, isHitContext);
		if (context.shouldRender()) {
			if (isHitContext) {
				context.renderPolygon(this.__hitPolygon0);
				context.renderPolyline(this.__hitPolyline0);
				context.renderPolyline(this.__hitPolyline1);
			} else {
				context.renderPolygon(this._polygon);
				context.renderPolyline(this._polyline0);
				context.renderPolyline(this._polyline1);
			}
		}
	}
	,
	exportViewShapes: function (svd) {
		$.ig.FinancialSeriesView.prototype.exportViewShapes.call(this, svd);
		var poly = new $.ig.PolygonVisualData(1, "fill", this._polygon);
		var poly0 = new $.ig.PolyLineVisualData(1, "bottom", this._polyline0);
		var poly1 = new $.ig.PolyLineVisualData(1, "top", this._polyline1);
		poly.tags().add("Fill");
		poly0.tags().add("Lower");
		poly0.tags().add("Main");
		poly1.tags().add("Upper");
		svd.shapes().add(poly);
		svd.shapes().add(poly0);
		svd.shapes().add(poly1);
	}
	,
	$type: new $.ig.Type('PriceChannelOverlayView', $.ig.FinancialSeriesView.prototype.$type)
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

$.ig.util.defType('AverageDirectionalIndexIndicatorStrategy___ADX__IteratorClass', 'Object', {
	__1__state: 0,
	__2__current: 0,
	__adxHelperEMA_5_0: null,
	_dataSource: null,
	__3__dataSource: null,
	_supportingCalculations: null,
	__3__supportingCalculations: null,
	__4__this: null,
	init: function (_1__state) {
		$.ig.Object.prototype.init.call(this);
		this.__1__state = _1__state;
	},
	moveNext: function () {
		switch (this.__1__state) {
			case 0:
				this.__1__state = -1;
				this.__adxHelperEMA_5_0 = this._supportingCalculations.eMA().strategy()(this.__4__this.aDXHelper(this._dataSource, this._supportingCalculations), this._dataSource.period()).getEnumerator();
				this.__2__current = 0;
				this.__1__state = 1;
				return true;
			case 1:
				this.__1__state = -1;
				if (this.__adxHelperEMA_5_0.moveNext()) {
					this.__2__current = this.__adxHelperEMA_5_0.current() * 100;
					this.__1__state = 1;
					return true;
				}
				break;
		}
		return false;
	}
	,
	getEnumerator: function () {
		var d__;
		if (this.__1__state == -2) {
			this.__1__state = 0;
			d__ = this;
		} else {
			d__ = new $.ig.AverageDirectionalIndexIndicatorStrategy___ADX__IteratorClass(0);
			d__.__4__this = this.__4__this;
		}
		d__._dataSource = this.__3__dataSource;
		d__._supportingCalculations = this.__3__supportingCalculations;
		return d__;
	}
	,
	reset: function () {
		throw new $.ig.NotSupportedException(1);
	}
	,
	dispose: function () {
	}
	,
	current: function () {
		return this.__2__current;
	}
	,
	current1: function () {
		return this.__2__current;
	}
	,
	$type: new $.ig.Type('AverageDirectionalIndexIndicatorStrategy___ADX__IteratorClass', $.ig.Object.prototype.$type, [$.ig.IEnumerable$1.prototype.$type.specialize(Number), $.ig.IEnumerable.prototype.$type, $.ig.IEnumerator$1.prototype.$type.specialize(Number), $.ig.IEnumerator.prototype.$type, $.ig.IDisposable.prototype.$type])
}, true);

$.ig.util.defType('FinancialSeries___get_TypicalColumn__IteratorClass', 'Object', {
	__1__state: 0,
	__2__current: 0,
	__userSpecified_5_0: false,
	__dataSource_5_1: null,
	_valueEnumerator: null,
	__value_5_2: 0,
	__count_5_3: 0,
	__sorted_5_4: null,
	__i_5_5: 0,
	__count_5_6: 0,
	__i_5_7: 0,
	__4__this: null,
	init: function (_1__state) {
		$.ig.Object.prototype.init.call(this);
		this.__1__state = _1__state;
	},
	_m_Finally0: function () {
		this.__1__state = -1;
		if (this._valueEnumerator != null) {
			this._valueEnumerator.dispose();
		}
	}
	,
	moveNext: function () {
		var __hasError__ = false;
		try {
			while (this.__1__state >= 0) {
				switch (this.__1__state) {
					case 0:
						this.__1__state = -1;
						this.__userSpecified_5_0 = false;
						if (this.__4__this.hasTypical()) {
							this.__dataSource_5_1 = this.__4__this.provideDataSource(0, this.__4__this.fastItemsSource().count());
							this.__4__this.callTypical(new $.ig.FinancialEventArgs(0, this.__4__this.fastItemsSource().count(), this.__dataSource_5_1, this.__4__this.provideSupportingCalculations(this.__dataSource_5_1)));
							if (this.__dataSource_5_1.typicalColumn() != null) {
								this.__userSpecified_5_0 = true;
								this.__1__state = 1;
								this._valueEnumerator = this.__dataSource_5_1.typicalColumn().getEnumerator();
								this.__1__state = 2;
								break;
							}
						}
						if (!this.__userSpecified_5_0) {
							if (this.__4__this.xAxisSortRequired() && (this.__4__this.xAxis()).sortedIndices() != null) {
								this.__count_5_3 = this.__4__this.fastItemsSource().count();
								this.__sorted_5_4 = (this.__4__this.xAxis()).sortedIndices();
								this.__i_5_5 = 0;
								this.__1__state = 3;
								break;
							}
							this.__count_5_6 = this.__4__this.fastItemsSource().count();
							this.__i_5_7 = 0;
							this.__1__state = 5;
							break;
						}
						break;
					case 2:
						this.__1__state = 1;
						if (this._valueEnumerator.moveNext()) {
							this.__value_5_2 = this._valueEnumerator.current();
							this.__2__current = this.__value_5_2;
							this.__1__state = 2;
							return true;
						}
						this._m_Finally0();
						if (!this.__userSpecified_5_0) {
							if (this.__4__this.xAxisSortRequired() && (this.__4__this.xAxis()).sortedIndices() != null) {
								this.__count_5_3 = this.__4__this.fastItemsSource().count();
								this.__sorted_5_4 = (this.__4__this.xAxis()).sortedIndices();
								this.__i_5_5 = 0;
								this.__1__state = 3;
								break;
							}
							this.__count_5_6 = this.__4__this.fastItemsSource().count();
							this.__i_5_7 = 0;
							this.__1__state = 5;
							break;
						}
						break;
					case 3:
						this.__1__state = -1;
						if (this.__i_5_5 < this.__count_5_3) {
							this.__2__current = (this.__4__this.highColumn().item(this.__sorted_5_4.item(this.__i_5_5)) + this.__4__this.lowColumn().item(this.__sorted_5_4.item(this.__i_5_5)) + this.__4__this.closeColumn().item(this.__sorted_5_4.item(this.__i_5_5))) / 3;
							this.__1__state = 4;
							return true;
						}
						break;
					case 4:
						this.__1__state = -1;
						++this.__i_5_5;
						this.__1__state = 3;
						break;
					case 5:
						this.__1__state = -1;
						if (this.__i_5_7 < this.__count_5_6) {
							this.__2__current = (this.__4__this.highColumn().item(this.__i_5_7) + this.__4__this.lowColumn().item(this.__i_5_7) + this.__4__this.closeColumn().item(this.__i_5_7)) / 3;
							this.__1__state = 6;
							return true;
						}
						break;
					case 6:
						this.__1__state = -1;
						++this.__i_5_7;
						this.__1__state = 5;
						break;
				}
			}
		}
		catch (e) {
			__hasError__ = true;
			throw e;
		}
		finally {
			if (__hasError__) {
				(this).dispose();
			}
		}
		return false;
	}
	,
	getEnumerator: function () {
		var d__;
		if (this.__1__state == -2) {
			this.__1__state = 0;
			d__ = this;
		} else {
			d__ = new $.ig.FinancialSeries___get_TypicalColumn__IteratorClass(0);
			d__.__4__this = this.__4__this;
		}
		return d__;
	}
	,
	reset: function () {
		throw new $.ig.NotSupportedException(1);
	}
	,
	dispose: function () {
		switch (this.__1__state) {
			case 1:
			case 2:
				this._m_Finally0();
				break;
		}
	}
	,
	current: function () {
		return this.__2__current;
	}
	,
	current1: function () {
		return this.__2__current;
	}
	,
	$type: new $.ig.Type('FinancialSeries___get_TypicalColumn__IteratorClass', $.ig.Object.prototype.$type, [$.ig.IEnumerable$1.prototype.$type.specialize(Number), $.ig.IEnumerable.prototype.$type, $.ig.IEnumerator$1.prototype.$type.specialize(Number), $.ig.IEnumerator.prototype.$type, $.ig.IDisposable.prototype.$type])
}, true);

$.ig.util.defType('FinancialSeries___get_TR__IteratorClass', 'Object', {
	__1__state: 0,
	__2__current: 0,
	__count_5_0: 0,
	__sorted_5_1: null,
	__i_5_2: 0,
	__count_5_3: 0,
	__i_5_4: 0,
	__4__this: null,
	init: function (_1__state) {
		$.ig.Object.prototype.init.call(this);
		this.__1__state = _1__state;
	},
	moveNext: function () {
		while (this.__1__state >= 0) {
			switch (this.__1__state) {
				case 0:
					this.__1__state = -1;
					if (this.__4__this.xAxisSortRequired() && (this.__4__this.xAxis()).sortedIndices() != null) {
						this.__count_5_0 = this.__4__this.highColumn().count();
						this.__sorted_5_1 = (this.__4__this.xAxis()).sortedIndices();
						if (this.__count_5_0 > 0) {
							this.__2__current = this.__4__this.makeSafe(this.__4__this.highColumn().item(this.__sorted_5_1.item(0)) - this.__4__this.lowColumn().item(this.__sorted_5_1.item(0)));
							this.__1__state = 1;
							return true;
						}
						this.__1__state = 1;
						break;
					}
					this.__count_5_3 = this.__4__this.highColumn().count();
					if (this.__count_5_3 > 0) {
						this.__2__current = this.__4__this.makeSafe(this.__4__this.highColumn().item(0) - this.__4__this.lowColumn().item(0));
						this.__1__state = 4;
						return true;
					}
					this.__1__state = 4;
					break;
				case 1:
					this.__1__state = -1;
					this.__i_5_2 = 1;
					this.__1__state = 2;
					break;
				case 2:
					this.__1__state = -1;
					if (this.__i_5_2 < this.__count_5_0) {
						this.__2__current = Math.max(this.__4__this.makeSafe(this.__4__this.highColumn().item(this.__sorted_5_1.item(this.__i_5_2)) - this.__4__this.lowColumn().item(this.__sorted_5_1.item(this.__i_5_2))), Math.max(this.__4__this.makeSafe(Math.abs(this.__4__this.highColumn().item(this.__sorted_5_1.item(this.__i_5_2)) - this.__4__this.closeColumn().item(this.__sorted_5_1.item(this.__i_5_2 - 1)))), this.__4__this.makeSafe(Math.abs(this.__4__this.lowColumn().item(this.__sorted_5_1.item(this.__i_5_2)) - this.__4__this.closeColumn().item(this.__sorted_5_1.item(this.__i_5_2 - 1))))));
						this.__1__state = 3;
						return true;
					}
					break;
				case 3:
					this.__1__state = -1;
					++this.__i_5_2;
					this.__1__state = 2;
					break;
				case 4:
					this.__1__state = -1;
					this.__i_5_4 = 1;
					this.__1__state = 5;
					break;
				case 5:
					this.__1__state = -1;
					if (this.__i_5_4 < this.__count_5_3) {
						this.__2__current = Math.max(this.__4__this.makeSafe(this.__4__this.highColumn().item(this.__i_5_4) - this.__4__this.lowColumn().item(this.__i_5_4)), Math.max(this.__4__this.makeSafe(Math.abs(this.__4__this.highColumn().item(this.__i_5_4) - this.__4__this.closeColumn().item(this.__i_5_4 - 1))), this.__4__this.makeSafe(Math.abs(this.__4__this.lowColumn().item(this.__i_5_4) - this.__4__this.closeColumn().item(this.__i_5_4 - 1)))));
						this.__1__state = 6;
						return true;
					}
					break;
				case 6:
					this.__1__state = -1;
					++this.__i_5_4;
					this.__1__state = 5;
					break;
			}
		}
		return false;
	}
	,
	getEnumerator: function () {
		var d__;
		if (this.__1__state == -2) {
			this.__1__state = 0;
			d__ = this;
		} else {
			d__ = new $.ig.FinancialSeries___get_TR__IteratorClass(0);
			d__.__4__this = this.__4__this;
		}
		return d__;
	}
	,
	reset: function () {
		throw new $.ig.NotSupportedException(1);
	}
	,
	dispose: function () {
	}
	,
	current: function () {
		return this.__2__current;
	}
	,
	current1: function () {
		return this.__2__current;
	}
	,
	$type: new $.ig.Type('FinancialSeries___get_TR__IteratorClass', $.ig.Object.prototype.$type, [$.ig.IEnumerable$1.prototype.$type.specialize(Number), $.ig.IEnumerable.prototype.$type, $.ig.IEnumerator$1.prototype.$type.specialize(Number), $.ig.IEnumerator.prototype.$type, $.ig.IDisposable.prototype.$type])
}, true);

$.ig.util.defType('FinancialSeries___get_TL__IteratorClass', 'Object', {
	__1__state: 0,
	__2__current: 0,
	__count_5_0: 0,
	__sorted_5_1: null,
	__i_5_2: 0,
	__count_5_3: 0,
	__i_5_4: 0,
	__4__this: null,
	init: function (_1__state) {
		$.ig.Object.prototype.init.call(this);
		this.__1__state = _1__state;
	},
	moveNext: function () {
		while (this.__1__state >= 0) {
			switch (this.__1__state) {
				case 0:
					this.__1__state = -1;
					if (this.__4__this.xAxisSortRequired() && (this.__4__this.xAxis()).sortedIndices() != null) {
						this.__count_5_0 = this.__4__this.lowColumn().count();
						this.__sorted_5_1 = (this.__4__this.xAxis()).sortedIndices();
						if (this.__count_5_0 > 0) {
							this.__2__current = this.__4__this.makeSafe(this.__4__this.lowColumn().item(this.__sorted_5_1.item(0)));
							this.__1__state = 1;
							return true;
						}
						this.__1__state = 1;
						break;
					}
					this.__count_5_3 = this.__4__this.lowColumn().count();
					if (this.__count_5_3 > 0) {
						this.__2__current = this.__4__this.makeSafe(this.__4__this.lowColumn().item(0));
						this.__1__state = 4;
						return true;
					}
					this.__1__state = 4;
					break;
				case 1:
					this.__1__state = -1;
					this.__i_5_2 = 1;
					this.__1__state = 2;
					break;
				case 2:
					this.__1__state = -1;
					if (this.__i_5_2 < this.__count_5_0) {
						this.__2__current = Math.min(this.__4__this.makeSafe(this.__4__this.lowColumn().item(this.__sorted_5_1.item(this.__i_5_2))), this.__4__this.makeSafe(this.__4__this.closeColumn().item(this.__sorted_5_1.item(this.__i_5_2 - 1))));
						this.__1__state = 3;
						return true;
					}
					break;
				case 3:
					this.__1__state = -1;
					this.__i_5_2++;
					this.__1__state = 2;
					break;
				case 4:
					this.__1__state = -1;
					this.__i_5_4 = 1;
					this.__1__state = 5;
					break;
				case 5:
					this.__1__state = -1;
					if (this.__i_5_4 < this.__count_5_3) {
						this.__2__current = Math.min(this.__4__this.makeSafe(this.__4__this.lowColumn().item(this.__i_5_4)), this.__4__this.makeSafe(this.__4__this.closeColumn().item(this.__i_5_4 - 1)));
						this.__1__state = 6;
						return true;
					}
					break;
				case 6:
					this.__1__state = -1;
					this.__i_5_4++;
					this.__1__state = 5;
					break;
			}
		}
		return false;
	}
	,
	getEnumerator: function () {
		var d__;
		if (this.__1__state == -2) {
			this.__1__state = 0;
			d__ = this;
		} else {
			d__ = new $.ig.FinancialSeries___get_TL__IteratorClass(0);
			d__.__4__this = this.__4__this;
		}
		return d__;
	}
	,
	reset: function () {
		throw new $.ig.NotSupportedException(1);
	}
	,
	dispose: function () {
	}
	,
	current: function () {
		return this.__2__current;
	}
	,
	current1: function () {
		return this.__2__current;
	}
	,
	$type: new $.ig.Type('FinancialSeries___get_TL__IteratorClass', $.ig.Object.prototype.$type, [$.ig.IEnumerable$1.prototype.$type.specialize(Number), $.ig.IEnumerable.prototype.$type, $.ig.IEnumerator$1.prototype.$type.specialize(Number), $.ig.IEnumerator.prototype.$type, $.ig.IDisposable.prototype.$type])
}, true);

$.ig.util.defType('PercentKCalculationStrategy___ProvideStream__IteratorClass', 'Object', {
	__1__state: 0,
	__2__current: 0,
	__period_5_0: 0,
	__highColumn_5_1: null,
	__lowColumn_5_2: null,
	__i_5_3: 0,
	_dataSource: null,
	__3__dataSource: null,
	_supportingCalculations: null,
	__3__supportingCalculations: null,
	__4__this: null,
	init: function (_1__state) {
		$.ig.Object.prototype.init.call(this);
		this.__1__state = _1__state;
	},
	moveNext: function () {
		while (this.__1__state >= 0) {
			switch (this.__1__state) {
				case 0:
					this.__1__state = -1;
					this.__period_5_0 = this._dataSource.period();
					this.__highColumn_5_1 = this._dataSource.highColumn();
					this.__lowColumn_5_2 = this._dataSource.lowColumn();
					this.__i_5_3 = 0;
					this.__1__state = 1;
					break;
				case 1:
					this.__1__state = -1;
					if (this.__i_5_3 < this._dataSource.count()) {
						var ago = Math.min(this.__period_5_0, this.__i_5_3);
						var highestHigh = -1.7976931348623157E+308;
						var lowestLow = 1.7976931348623157E+308;
						for (var j = 0; j < ago; j++) {
							if (!$.ig.util.isNaN(this.__highColumn_5_1.item(this.__i_5_3 - j))) {
								highestHigh = Math.max(highestHigh, this.__highColumn_5_1.item(this.__i_5_3 - j));
							}
							if (!$.ig.util.isNaN(this.__lowColumn_5_2.item(this.__i_5_3 - j))) {
								lowestLow = Math.min(lowestLow, this.__lowColumn_5_2.item(this.__i_5_3 - j));
							}
						}
						this.__2__current = this._supportingCalculations.makeSafe()((this._dataSource.closeColumn().item(this.__i_5_3) - lowestLow) / (highestHigh - lowestLow) * 100);
						this.__1__state = 2;
						return true;
					}
					break;
				case 2:
					this.__1__state = -1;
					this.__i_5_3++;
					this.__1__state = 1;
					break;
			}
		}
		return false;
	}
	,
	getEnumerator: function () {
		var d__;
		if (this.__1__state == -2) {
			this.__1__state = 0;
			d__ = this;
		} else {
			d__ = new $.ig.PercentKCalculationStrategy___ProvideStream__IteratorClass(0);
			d__.__4__this = this.__4__this;
		}
		d__._dataSource = this.__3__dataSource;
		d__._supportingCalculations = this.__3__supportingCalculations;
		return d__;
	}
	,
	reset: function () {
		throw new $.ig.NotSupportedException(1);
	}
	,
	dispose: function () {
	}
	,
	current: function () {
		return this.__2__current;
	}
	,
	current1: function () {
		return this.__2__current;
	}
	,
	$type: new $.ig.Type('PercentKCalculationStrategy___ProvideStream__IteratorClass', $.ig.Object.prototype.$type, [$.ig.IEnumerable$1.prototype.$type.specialize(Number), $.ig.IEnumerable.prototype.$type, $.ig.IEnumerator$1.prototype.$type.specialize(Number), $.ig.IEnumerator.prototype.$type, $.ig.IDisposable.prototype.$type])
}, true);

$.ig.util.defType('AverageDirectionalIndexIndicatorStrategy___PlusDI__IteratorClass', 'Object', {
	__1__state: 0,
	__2__current: 0,
	__emaPlusDM_5_0: null,
	__averageTrueRange_5_1: null,
	_dataSource: null,
	__3__dataSource: null,
	_supportingCalculations: null,
	__3__supportingCalculations: null,
	__4__this: null,
	init: function (_1__state) {
		$.ig.Object.prototype.init.call(this);
		this.__1__state = _1__state;
	},
	moveNext: function () {
		while (this.__1__state >= 0) {
			switch (this.__1__state) {
				case 0:
					this.__1__state = -1;
					this.__emaPlusDM_5_0 = this._supportingCalculations.eMA().strategy()(this.__4__this.plusDM(this._dataSource.highColumn(), this._dataSource.lowColumn()), this._dataSource.period()).getEnumerator();
					this.__averageTrueRange_5_1 = this._supportingCalculations.eMA().strategy()(this._dataSource.trueRange(), this._dataSource.period()).getEnumerator();
					this.__1__state = 1;
					break;
				case 1:
					this.__1__state = -1;
					if (this.__emaPlusDM_5_0.moveNext() && this.__averageTrueRange_5_1.moveNext()) {
						this.__2__current = this._supportingCalculations.makeSafe()(this.__emaPlusDM_5_0.current() / this.__averageTrueRange_5_1.current());
						this.__1__state = 1;
						return true;
					}
					break;
			}
		}
		return false;
	}
	,
	getEnumerator: function () {
		var d__;
		if (this.__1__state == -2) {
			this.__1__state = 0;
			d__ = this;
		} else {
			d__ = new $.ig.AverageDirectionalIndexIndicatorStrategy___PlusDI__IteratorClass(0);
			d__.__4__this = this.__4__this;
		}
		d__._dataSource = this.__3__dataSource;
		d__._supportingCalculations = this.__3__supportingCalculations;
		return d__;
	}
	,
	reset: function () {
		throw new $.ig.NotSupportedException(1);
	}
	,
	dispose: function () {
	}
	,
	current: function () {
		return this.__2__current;
	}
	,
	current1: function () {
		return this.__2__current;
	}
	,
	$type: new $.ig.Type('AverageDirectionalIndexIndicatorStrategy___PlusDI__IteratorClass', $.ig.Object.prototype.$type, [$.ig.IEnumerable$1.prototype.$type.specialize(Number), $.ig.IEnumerable.prototype.$type, $.ig.IEnumerator$1.prototype.$type.specialize(Number), $.ig.IEnumerator.prototype.$type, $.ig.IDisposable.prototype.$type])
}, true);

$.ig.util.defType('AverageDirectionalIndexIndicatorStrategy___MinusDI__IteratorClass', 'Object', {
	__1__state: 0,
	__2__current: 0,
	__emaMinusDM_5_0: null,
	__averageTrueRange_5_1: null,
	_dataSource: null,
	__3__dataSource: null,
	_supportingCalculations: null,
	__3__supportingCalculations: null,
	__4__this: null,
	init: function (_1__state) {
		$.ig.Object.prototype.init.call(this);
		this.__1__state = _1__state;
	},
	moveNext: function () {
		while (this.__1__state >= 0) {
			switch (this.__1__state) {
				case 0:
					this.__1__state = -1;
					this.__emaMinusDM_5_0 = this._supportingCalculations.eMA().strategy()(this.__4__this.minusDM(this._dataSource.highColumn(), this._dataSource.lowColumn()), this._dataSource.period()).getEnumerator();
					this.__averageTrueRange_5_1 = this._supportingCalculations.eMA().strategy()(this._dataSource.trueRange(), this._dataSource.period()).getEnumerator();
					this.__1__state = 1;
					break;
				case 1:
					this.__1__state = -1;
					if (this.__emaMinusDM_5_0.moveNext() && this.__averageTrueRange_5_1.moveNext()) {
						this.__2__current = this._supportingCalculations.makeSafe()(this.__emaMinusDM_5_0.current() / this.__averageTrueRange_5_1.current());
						this.__1__state = 1;
						return true;
					}
					break;
			}
		}
		return false;
	}
	,
	getEnumerator: function () {
		var d__;
		if (this.__1__state == -2) {
			this.__1__state = 0;
			d__ = this;
		} else {
			d__ = new $.ig.AverageDirectionalIndexIndicatorStrategy___MinusDI__IteratorClass(0);
			d__.__4__this = this.__4__this;
		}
		d__._dataSource = this.__3__dataSource;
		d__._supportingCalculations = this.__3__supportingCalculations;
		return d__;
	}
	,
	reset: function () {
		throw new $.ig.NotSupportedException(1);
	}
	,
	dispose: function () {
	}
	,
	current: function () {
		return this.__2__current;
	}
	,
	current1: function () {
		return this.__2__current;
	}
	,
	$type: new $.ig.Type('AverageDirectionalIndexIndicatorStrategy___MinusDI__IteratorClass', $.ig.Object.prototype.$type, [$.ig.IEnumerable$1.prototype.$type.specialize(Number), $.ig.IEnumerable.prototype.$type, $.ig.IEnumerator$1.prototype.$type.specialize(Number), $.ig.IEnumerator.prototype.$type, $.ig.IDisposable.prototype.$type])
}, true);

$.ig.util.defType('AverageDirectionalIndexIndicatorStrategy___ADXHelper__IteratorClass', 'Object', {
	__1__state: 0,
	__2__current: 0,
	__plusDI_5_0: null,
	__minusDI_5_1: null,
	_dataSource: null,
	__3__dataSource: null,
	_supportingCalculations: null,
	__3__supportingCalculations: null,
	__4__this: null,
	init: function (_1__state) {
		$.ig.Object.prototype.init.call(this);
		this.__1__state = _1__state;
	},
	moveNext: function () {
		while (this.__1__state >= 0) {
			switch (this.__1__state) {
				case 0:
					this.__1__state = -1;
					this.__plusDI_5_0 = this.__4__this.plusDI(this._dataSource, this._supportingCalculations).getEnumerator();
					this.__minusDI_5_1 = this.__4__this.minusDI(this._dataSource, this._supportingCalculations).getEnumerator();
					this.__plusDI_5_0.moveNext();
					this.__minusDI_5_1.moveNext();
					this.__1__state = 1;
					break;
				case 1:
					this.__1__state = -1;
					if (this.__plusDI_5_0.moveNext() && this.__minusDI_5_1.moveNext()) {
						this.__2__current = Math.abs(this._supportingCalculations.makeSafe()((this.__plusDI_5_0.current() - this.__minusDI_5_1.current()) / (this.__plusDI_5_0.current() + this.__minusDI_5_1.current())));
						this.__1__state = 1;
						return true;
					}
					break;
			}
		}
		return false;
	}
	,
	getEnumerator: function () {
		var d__;
		if (this.__1__state == -2) {
			this.__1__state = 0;
			d__ = this;
		} else {
			d__ = new $.ig.AverageDirectionalIndexIndicatorStrategy___ADXHelper__IteratorClass(0);
			d__.__4__this = this.__4__this;
		}
		d__._dataSource = this.__3__dataSource;
		d__._supportingCalculations = this.__3__supportingCalculations;
		return d__;
	}
	,
	reset: function () {
		throw new $.ig.NotSupportedException(1);
	}
	,
	dispose: function () {
	}
	,
	current: function () {
		return this.__2__current;
	}
	,
	current1: function () {
		return this.__2__current;
	}
	,
	$type: new $.ig.Type('AverageDirectionalIndexIndicatorStrategy___ADXHelper__IteratorClass', $.ig.Object.prototype.$type, [$.ig.IEnumerable$1.prototype.$type.specialize(Number), $.ig.IEnumerable.prototype.$type, $.ig.IEnumerator$1.prototype.$type.specialize(Number), $.ig.IEnumerator.prototype.$type, $.ig.IDisposable.prototype.$type])
}, true);

$.ig.util.defType('AverageDirectionalIndexIndicatorStrategy___MinusDM__IteratorClass', 'Object', {
	__1__state: 0,
	__2__current: 0,
	__upMove_5_0: 0,
	__downMove_5_1: 0,
	__i_5_2: 0,
	_highColumn: null,
	__3__highColumn: null,
	_lowColumn: null,
	__3__lowColumn: null,
	__4__this: null,
	init: function (_1__state) {
		$.ig.Object.prototype.init.call(this);
		this.__1__state = _1__state;
	},
	moveNext: function () {
		while (this.__1__state >= 0) {
			switch (this.__1__state) {
				case 0:
					this.__1__state = -1;
					this.__2__current = 0;
					this.__1__state = 1;
					return true;
				case 1:
					this.__1__state = -1;
					this.__upMove_5_0 = 0;
					this.__downMove_5_1 = 0;
					this.__i_5_2 = 1;
					this.__1__state = 2;
					break;
				case 2:
					this.__1__state = -1;
					if (this.__i_5_2 < this._highColumn.count()) {
						this.__upMove_5_0 = this.__4__this.upMove(this.__i_5_2, this._highColumn, this._lowColumn);
						this.__downMove_5_1 = this.__4__this.downMove(this.__i_5_2, this._highColumn, this._lowColumn);
						if (this.__downMove_5_1 > this.__upMove_5_0 && this.__downMove_5_1 > 0) {
							this.__2__current = this.__downMove_5_1;
							this.__1__state = 3;
							return true;
						}
						this.__2__current = 0;
						this.__1__state = 3;
						return true;
					}
					break;
				case 3:
					this.__1__state = -1;
					this.__i_5_2++;
					this.__1__state = 2;
					break;
			}
		}
		return false;
	}
	,
	getEnumerator: function () {
		var d__;
		if (this.__1__state == -2) {
			this.__1__state = 0;
			d__ = this;
		} else {
			d__ = new $.ig.AverageDirectionalIndexIndicatorStrategy___MinusDM__IteratorClass(0);
			d__.__4__this = this.__4__this;
		}
		d__._highColumn = this.__3__highColumn;
		d__._lowColumn = this.__3__lowColumn;
		return d__;
	}
	,
	reset: function () {
		throw new $.ig.NotSupportedException(1);
	}
	,
	dispose: function () {
	}
	,
	current: function () {
		return this.__2__current;
	}
	,
	current1: function () {
		return this.__2__current;
	}
	,
	$type: new $.ig.Type('AverageDirectionalIndexIndicatorStrategy___MinusDM__IteratorClass', $.ig.Object.prototype.$type, [$.ig.IEnumerable$1.prototype.$type.specialize(Number), $.ig.IEnumerable.prototype.$type, $.ig.IEnumerator$1.prototype.$type.specialize(Number), $.ig.IEnumerator.prototype.$type, $.ig.IDisposable.prototype.$type])
}, true);

$.ig.util.defType('RelativeStrengthIndexIndicatorStrategy___ProvideStream__IteratorClass', 'Object', {
	__1__state: 0,
	__2__current: 0,
	__period_5_0: 0,
	__uema_5_1: 0,
	__dema_5_2: 0,
	__indicatorColumn_5_3: null,
	__closeColumn_5_4: null,
	__i_5_5: 0,
	__i_5_6: 0,
	_dataSource: null,
	__3__dataSource: null,
	_supportingCalculations: null,
	__3__supportingCalculations: null,
	__4__this: null,
	init: function (_1__state) {
		$.ig.Object.prototype.init.call(this);
		this.__1__state = _1__state;
	},
	moveNext: function () {
		while (this.__1__state >= 0) {
			switch (this.__1__state) {
				case 0:
					{
						this.__1__state = -1;
						this.__period_5_0 = this._dataSource.period();
						var alpha = 2 / (this.__period_5_0 + 1);
						this.__uema_5_1 = 0;
						this.__dema_5_2 = 0;
						this.__indicatorColumn_5_3 = this._dataSource.indicatorColumn();
						this.__closeColumn_5_4 = this._dataSource.closeColumn();
						if (this.__indicatorColumn_5_3.count() > 0) {
							this.__2__current = 0;
							this.__1__state = 1;
							return true;
						}
						this.__1__state = 1;
						break;
					}
				case 1:
					this.__1__state = -1;
					this.__i_5_5 = 1;
					this.__1__state = 2;
					break;
				case 2:
					this.__1__state = -1;
					if (this.__i_5_5 < Math.min(this._dataSource.period(), this.__indicatorColumn_5_3.count())) {
						var C = this.__closeColumn_5_4.item(this.__i_5_5) - this.__closeColumn_5_4.item(this.__i_5_5 - 1);
						var U = C > 0 ? C : 0;
						var D = C > 0 ? 0 : -C;
						this.__uema_5_1 += U / (this.__period_5_0 - 1);
						this.__dema_5_2 += D / (this.__period_5_0 - 1);
						this.__2__current = 0;
						this.__1__state = 3;
						return true;
					}
					this.__i_5_6 = this._dataSource.period();
					this.__1__state = 4;
					break;
				case 3:
					this.__1__state = -1;
					++this.__i_5_5;
					this.__1__state = 2;
					break;
				case 4:
					this.__1__state = -1;
					if (this.__i_5_6 < this.__indicatorColumn_5_3.count()) {
						var C1 = this.__closeColumn_5_4.item(this.__i_5_6) - this.__closeColumn_5_4.item(this.__i_5_6 - 1);
						var U1 = C1 > 0 ? C1 : 0;
						var D1 = C1 > 0 ? 0 : -C1;
						this.__uema_5_1 = (this.__uema_5_1 * (this.__period_5_0 - 1) + U1) / this.__period_5_0;
						this.__dema_5_2 = (this.__dema_5_2 * (this.__period_5_0 - 1) + D1) / this.__period_5_0;
						this.__2__current = this._supportingCalculations.makeSafe()(this.__uema_5_1 != 0 ? 100 * this.__uema_5_1 / (this.__uema_5_1 + this.__dema_5_2) : 0);
						this.__1__state = 5;
						return true;
					}
					break;
				case 5:
					this.__1__state = -1;
					++this.__i_5_6;
					this.__1__state = 4;
					break;
			}
		}
		return false;
	}
	,
	getEnumerator: function () {
		var d__;
		if (this.__1__state == -2) {
			this.__1__state = 0;
			d__ = this;
		} else {
			d__ = new $.ig.RelativeStrengthIndexIndicatorStrategy___ProvideStream__IteratorClass(0);
			d__.__4__this = this.__4__this;
		}
		d__._dataSource = this.__3__dataSource;
		d__._supportingCalculations = this.__3__supportingCalculations;
		return d__;
	}
	,
	reset: function () {
		throw new $.ig.NotSupportedException(1);
	}
	,
	dispose: function () {
	}
	,
	current: function () {
		return this.__2__current;
	}
	,
	current1: function () {
		return this.__2__current;
	}
	,
	$type: new $.ig.Type('RelativeStrengthIndexIndicatorStrategy___ProvideStream__IteratorClass', $.ig.Object.prototype.$type, [$.ig.IEnumerable$1.prototype.$type.specialize(Number), $.ig.IEnumerable.prototype.$type, $.ig.IEnumerator$1.prototype.$type.specialize(Number), $.ig.IEnumerator.prototype.$type, $.ig.IDisposable.prototype.$type])
}, true);

$.ig.util.defType('AverageDirectionalIndexIndicatorStrategy___PlusDM__IteratorClass', 'Object', {
	__1__state: 0,
	__2__current: 0,
	__upMove_5_0: 0,
	__downMove_5_1: 0,
	__i_5_2: 0,
	_highColumn: null,
	__3__highColumn: null,
	_lowColumn: null,
	__3__lowColumn: null,
	__4__this: null,
	init: function (_1__state) {
		$.ig.Object.prototype.init.call(this);
		this.__1__state = _1__state;
	},
	moveNext: function () {
		while (this.__1__state >= 0) {
			switch (this.__1__state) {
				case 0:
					this.__1__state = -1;
					this.__2__current = 0;
					this.__1__state = 1;
					return true;
				case 1:
					this.__1__state = -1;
					this.__upMove_5_0 = 0;
					this.__downMove_5_1 = 0;
					this.__i_5_2 = 1;
					this.__1__state = 2;
					break;
				case 2:
					this.__1__state = -1;
					if (this.__i_5_2 < this._highColumn.count()) {
						this.__upMove_5_0 = this.__4__this.upMove(this.__i_5_2, this._highColumn, this._lowColumn);
						this.__downMove_5_1 = this.__4__this.downMove(this.__i_5_2, this._highColumn, this._lowColumn);
						if (this.__upMove_5_0 > this.__downMove_5_1 && this.__upMove_5_0 > 0) {
							this.__2__current = this.__upMove_5_0;
							this.__1__state = 3;
							return true;
						}
						this.__2__current = 0;
						this.__1__state = 3;
						return true;
					}
					break;
				case 3:
					this.__1__state = -1;
					this.__i_5_2++;
					this.__1__state = 2;
					break;
			}
		}
		return false;
	}
	,
	getEnumerator: function () {
		var d__;
		if (this.__1__state == -2) {
			this.__1__state = 0;
			d__ = this;
		} else {
			d__ = new $.ig.AverageDirectionalIndexIndicatorStrategy___PlusDM__IteratorClass(0);
			d__.__4__this = this.__4__this;
		}
		d__._highColumn = this.__3__highColumn;
		d__._lowColumn = this.__3__lowColumn;
		return d__;
	}
	,
	reset: function () {
		throw new $.ig.NotSupportedException(1);
	}
	,
	dispose: function () {
	}
	,
	current: function () {
		return this.__2__current;
	}
	,
	current1: function () {
		return this.__2__current;
	}
	,
	$type: new $.ig.Type('AverageDirectionalIndexIndicatorStrategy___PlusDM__IteratorClass', $.ig.Object.prototype.$type, [$.ig.IEnumerable$1.prototype.$type.specialize(Number), $.ig.IEnumerable.prototype.$type, $.ig.IEnumerator$1.prototype.$type.specialize(Number), $.ig.IEnumerator.prototype.$type, $.ig.IDisposable.prototype.$type])
}, true);

$.ig.util.defType('AccumulationDistributionIndicatorStrategy___ProvideStream__IteratorClass', 'Object', {
	__1__state: 0,
	__2__current: 0,
	__ad_5_0: 0,
	__min_5_1: 0,
	__max_5_2: 0,
	__count_5_3: 0,
	__i_5_4: 0,
	_dataSource: null,
	__3__dataSource: null,
	_supportingCalculations: null,
	__3__supportingCalculations: null,
	__4__this: null,
	init: function (_1__state) {
		$.ig.Object.prototype.init.call(this);
		this.__1__state = _1__state;
	},
	moveNext: function () {
		while (this.__1__state >= 0) {
			switch (this.__1__state) {
				case 0:
					{
						this.__1__state = -1;
						this.__ad_5_0 = 0;
						this.__min_5_1 = Number.POSITIVE_INFINITY;
						this.__max_5_2 = Number.NEGATIVE_INFINITY;
						var indicatorCount = this._dataSource.indicatorColumn() != null ? this._dataSource.indicatorColumn().count() : 0;
						var closeCount = this._dataSource.closeColumn() != null ? this._dataSource.closeColumn().count() : 0;
						var highCount = this._dataSource.highColumn() != null ? this._dataSource.highColumn().count() : 0;
						var volumeCount = this._dataSource.volumeColumn() != null ? this._dataSource.volumeColumn().count() : 0;
						this.__count_5_3 = Math.min(indicatorCount, Math.min(closeCount, Math.min(highCount, volumeCount)));
						this.__i_5_4 = 0;
						this.__1__state = 1;
						break;
					}
				case 1:
					this.__1__state = -1;
					if (this.__i_5_4 < this.__count_5_3) {
						var C = this._dataSource.closeColumn().item(this.__i_5_4);
						var L = this._dataSource.lowColumn().item(this.__i_5_4);
						var H = this._dataSource.highColumn().item(this.__i_5_4);
						var V = this._dataSource.volumeColumn().item(this.__i_5_4);
						var CLV = ((C - L) - (H - C)) / (H - L);
						this.__ad_5_0 += this._supportingCalculations.makeSafe()(CLV * V);
						this.__min_5_1 = Math.min(this.__min_5_1, this.__ad_5_0);
						this.__max_5_2 = Math.max(this.__max_5_2, this.__ad_5_0);
						this.__2__current = this.__ad_5_0;
						this.__1__state = 2;
						return true;
					}
					break;
				case 2:
					this.__1__state = -1;
					++this.__i_5_4;
					this.__1__state = 1;
					break;
			}
		}
		return false;
	}
	,
	getEnumerator: function () {
		var d__;
		if (this.__1__state == -2) {
			this.__1__state = 0;
			d__ = this;
		} else {
			d__ = new $.ig.AccumulationDistributionIndicatorStrategy___ProvideStream__IteratorClass(0);
			d__.__4__this = this.__4__this;
		}
		d__._dataSource = this.__3__dataSource;
		d__._supportingCalculations = this.__3__supportingCalculations;
		return d__;
	}
	,
	reset: function () {
		throw new $.ig.NotSupportedException(1);
	}
	,
	dispose: function () {
	}
	,
	current: function () {
		return this.__2__current;
	}
	,
	current1: function () {
		return this.__2__current;
	}
	,
	$type: new $.ig.Type('AccumulationDistributionIndicatorStrategy___ProvideStream__IteratorClass', $.ig.Object.prototype.$type, [$.ig.IEnumerable$1.prototype.$type.specialize(Number), $.ig.IEnumerable.prototype.$type, $.ig.IEnumerator$1.prototype.$type.specialize(Number), $.ig.IEnumerator.prototype.$type, $.ig.IDisposable.prototype.$type])
}, true);

$.ig.util.defType('ForceIndexIndicatorStrategy___FI__IteratorClass', 'Object', {
	__1__state: 0,
	__2__current: 0,
	__count_5_0: 0,
	__closeColumn_5_1: null,
	__volumeColumn_5_2: null,
	__i_5_3: 0,
	_dataSource: null,
	__3__dataSource: null,
	__4__this: null,
	init: function (_1__state) {
		$.ig.Object.prototype.init.call(this);
		this.__1__state = _1__state;
	},
	moveNext: function () {
		while (this.__1__state >= 0) {
			switch (this.__1__state) {
				case 0:
					{
						this.__1__state = -1;
						this.__count_5_0 = 0;
						this.__closeColumn_5_1 = this._dataSource.closeColumn();
						this.__volumeColumn_5_2 = this._dataSource.volumeColumn();
						var indicatorColumn = this._dataSource.indicatorColumn();
						if (this.__closeColumn_5_1 != null && this.__volumeColumn_5_2 != null) {
							this.__count_5_0 = Math.min(this.__closeColumn_5_1.count(), this.__volumeColumn_5_2.count());
						}
						if (this.__count_5_0 > 0) {
							this.__2__current = 0;
							this.__1__state = 1;
							return true;
						}
						this.__1__state = 1;
						break;
					}
				case 1:
					this.__1__state = -1;
					this.__i_5_3 = 1;
					this.__1__state = 2;
					break;
				case 2:
					this.__1__state = -1;
					if (this.__i_5_3 < this.__count_5_0) {
						this.__2__current = this.__volumeColumn_5_2.item(this.__i_5_3) * (this.__closeColumn_5_1.item(this.__i_5_3) - this.__closeColumn_5_1.item(this.__i_5_3 - 1));
						this.__1__state = 3;
						return true;
					}
					break;
				case 3:
					this.__1__state = -1;
					++this.__i_5_3;
					this.__1__state = 2;
					break;
			}
		}
		return false;
	}
	,
	getEnumerator: function () {
		var d__;
		if (this.__1__state == -2) {
			this.__1__state = 0;
			d__ = this;
		} else {
			d__ = new $.ig.ForceIndexIndicatorStrategy___FI__IteratorClass(0);
			d__.__4__this = this.__4__this;
		}
		d__._dataSource = this.__3__dataSource;
		return d__;
	}
	,
	reset: function () {
		throw new $.ig.NotSupportedException(1);
	}
	,
	dispose: function () {
	}
	,
	current: function () {
		return this.__2__current;
	}
	,
	current1: function () {
		return this.__2__current;
	}
	,
	$type: new $.ig.Type('ForceIndexIndicatorStrategy___FI__IteratorClass', $.ig.Object.prototype.$type, [$.ig.IEnumerable$1.prototype.$type.specialize(Number), $.ig.IEnumerable.prototype.$type, $.ig.IEnumerator$1.prototype.$type.specialize(Number), $.ig.IEnumerator.prototype.$type, $.ig.IDisposable.prototype.$type])
}, true);

$.ig.PriceDisplayType.prototype.candlestick = 0;
$.ig.PriceDisplayType.prototype.oHLC = 1;

$.ig.IndicatorDisplayType.prototype.line = 0;
$.ig.IndicatorDisplayType.prototype.area = 1;
$.ig.IndicatorDisplayType.prototype.column = 2;

$.ig.FinancialSeries.prototype.negativeBrushPropertyName = "NegativeBrush";
$.ig.FinancialSeries.prototype.xAxisPropertyName = "XAxis";
$.ig.FinancialSeries.prototype.yAxisPropertyName = "YAxis";
$.ig.FinancialSeries.prototype.openMemberPathPropertyName = "OpenMemberPath";
$.ig.FinancialSeries.prototype.openColumnPropertyName = "OpenColumn";
$.ig.FinancialSeries.prototype.highMemberPathPropertyName = "HighMemberPath";
$.ig.FinancialSeries.prototype.highColumnPropertyName = "HighColumn";
$.ig.FinancialSeries.prototype.lowMemberPathPropertyName = "LowMemberPath";
$.ig.FinancialSeries.prototype.lowColumnPropertyName = "LowColumn";
$.ig.FinancialSeries.prototype.closeMemberPathPropertyName = "CloseMemberPath";
$.ig.FinancialSeries.prototype.closeColumnPropertyName = "CloseColumn";
$.ig.FinancialSeries.prototype.volumeMemberPathPropertyName = "VolumeMemberPath";
$.ig.FinancialSeries.prototype.volumeColumnPropertyName = "VolumeColumn";
$.ig.FinancialSeries.prototype.isCustomCategoryStyleAllowedPropertyName = "IsCustomCategoryStyleAllowed";
$.ig.FinancialSeries.prototype.transitionInModePropertyName = "TransitionInMode";
$.ig.FinancialSeries.prototype.isTransitionInEnabledPropertyName = "IsTransitionInEnabled";
$.ig.FinancialSeries.prototype.negativeBrushProperty = $.ig.DependencyProperty.prototype.register($.ig.FinancialSeries.prototype.negativeBrushPropertyName, $.ig.Brush.prototype.$type, $.ig.FinancialSeries.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.FinancialSeries.prototype.negativeBrushPropertyName, e.oldValue(), e.newValue());
}));
$.ig.FinancialSeries.prototype.xAxisProperty = $.ig.DependencyProperty.prototype.register($.ig.FinancialSeries.prototype.xAxisPropertyName, $.ig.CategoryAxisBase.prototype.$type, $.ig.FinancialSeries.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.FinancialSeries.prototype.xAxisPropertyName, e.oldValue(), e.newValue());
}));
$.ig.FinancialSeries.prototype.yAxisProperty = $.ig.DependencyProperty.prototype.register($.ig.FinancialSeries.prototype.yAxisPropertyName, $.ig.NumericYAxis.prototype.$type, $.ig.FinancialSeries.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.FinancialSeries.prototype.yAxisPropertyName, e.oldValue(), e.newValue());
}));
$.ig.FinancialSeries.prototype.openMemberPathProperty = $.ig.DependencyProperty.prototype.register($.ig.FinancialSeries.prototype.openMemberPathPropertyName, String, $.ig.FinancialSeries.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.FinancialSeries.prototype.openMemberPathPropertyName, e.oldValue(), e.newValue());
}));
$.ig.FinancialSeries.prototype.highMemberPathProperty = $.ig.DependencyProperty.prototype.register($.ig.FinancialSeries.prototype.highMemberPathPropertyName, String, $.ig.FinancialSeries.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.FinancialSeries.prototype.highMemberPathPropertyName, e.oldValue(), e.newValue());
}));
$.ig.FinancialSeries.prototype.lowMemberPathProperty = $.ig.DependencyProperty.prototype.register($.ig.FinancialSeries.prototype.lowMemberPathPropertyName, String, $.ig.FinancialSeries.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.FinancialSeries.prototype.lowMemberPathPropertyName, e.oldValue(), e.newValue());
}));
$.ig.FinancialSeries.prototype.closeMemberPathProperty = $.ig.DependencyProperty.prototype.register($.ig.FinancialSeries.prototype.closeMemberPathPropertyName, String, $.ig.FinancialSeries.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.FinancialSeries.prototype.closeMemberPathPropertyName, e.oldValue(), e.newValue());
}));
$.ig.FinancialSeries.prototype.volumeMemberPathProperty = $.ig.DependencyProperty.prototype.register($.ig.FinancialSeries.prototype.volumeMemberPathPropertyName, String, $.ig.FinancialSeries.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.FinancialSeries.prototype.volumeMemberPathPropertyName, e.oldValue(), e.newValue());
}));
$.ig.FinancialSeries.prototype.isCustomCategoryStyleAllowedProperty = $.ig.DependencyProperty.prototype.register($.ig.FinancialSeries.prototype.isCustomCategoryStyleAllowedPropertyName, $.ig.Boolean.prototype.$type, $.ig.FinancialSeries.prototype.$type, new $.ig.PropertyMetadata(2, false, function (sender, e) {
	(sender).raisePropertyChanged($.ig.FinancialSeries.prototype.isCustomCategoryStyleAllowedPropertyName, e.oldValue(), e.newValue());
}));
$.ig.FinancialSeries.prototype.transitionInModeProperty = $.ig.DependencyProperty.prototype.register($.ig.FinancialSeries.prototype.transitionInModePropertyName, $.ig.CategoryTransitionInMode.prototype.$type, $.ig.FinancialSeries.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.CategoryTransitionInMode.prototype.getBox($.ig.CategoryTransitionInMode.prototype.auto), function (sender, e) {
	(sender).raisePropertyChanged($.ig.FinancialSeries.prototype.transitionInModePropertyName, e.oldValue(), e.newValue());
}));
$.ig.FinancialSeries.prototype.isTransitionInEnabledProperty = $.ig.DependencyProperty.prototype.register($.ig.FinancialSeries.prototype.isTransitionInEnabledPropertyName, $.ig.Boolean.prototype.$type, $.ig.FinancialSeries.prototype.$type, new $.ig.PropertyMetadata(2, false, function (sender, e) {
	(sender).raisePropertyChanged($.ig.FinancialSeries.prototype.isTransitionInEnabledPropertyName, e.oldValue(), e.newValue());
}));

$.ig.FinancialIndicator.prototype.displayTypePropertyName = "DisplayType";
$.ig.FinancialIndicator.prototype.ignoreFirstPropertyName = "IgnoreFirst";
$.ig.FinancialIndicator.prototype.displayTypeProperty = $.ig.DependencyProperty.prototype.register($.ig.FinancialIndicator.prototype.displayTypePropertyName, $.ig.IndicatorDisplayType.prototype.$type, $.ig.FinancialIndicator.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.IndicatorDisplayType.prototype.getBox($.ig.IndicatorDisplayType.prototype.line), function (sender, e) {
	(sender).raisePropertyChanged($.ig.FinancialIndicator.prototype.displayTypePropertyName, e.oldValue(), e.newValue());
}));
$.ig.FinancialIndicator.prototype.ignoreFirstProperty = $.ig.DependencyProperty.prototype.register($.ig.FinancialIndicator.prototype.ignoreFirstPropertyName, $.ig.Number.prototype.$type, $.ig.FinancialSeries.prototype.$type, new $.ig.PropertyMetadata(2, 0, function (sender, e) {
	(sender).raisePropertyChanged($.ig.FinancialIndicator.prototype.ignoreFirstPropertyName, e.oldValue(), e.newValue());
}));
$.ig.FinancialIndicator.prototype.trendLineTypeProperty = $.ig.DependencyProperty.prototype.register($.ig.Series.prototype.trendLineTypePropertyName, $.ig.TrendLineType.prototype.$type, $.ig.FinancialIndicator.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.TrendLineType.prototype.getBox($.ig.TrendLineType.prototype.none), function (sender, e) {
	(sender).raisePropertyChanged($.ig.Series.prototype.trendLineTypePropertyName, e.oldValue(), e.newValue());
}));
$.ig.FinancialIndicator.prototype.trendLineBrushProperty = $.ig.DependencyProperty.prototype.register($.ig.Series.prototype.trendLineBrushPropertyName, $.ig.Brush.prototype.$type, $.ig.FinancialIndicator.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.Series.prototype.trendLineBrushPropertyName, e.oldValue(), e.newValue());
}));
$.ig.FinancialIndicator.prototype.actualTrendLineBrushProperty = $.ig.DependencyProperty.prototype.register($.ig.Series.prototype.trendLineActualBrushPropertyName, $.ig.Brush.prototype.$type, $.ig.FinancialIndicator.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.Series.prototype.trendLineActualBrushPropertyName, e.oldValue(), e.newValue());
}));
$.ig.FinancialIndicator.prototype.trendLineThicknessProperty = $.ig.DependencyProperty.prototype.register($.ig.Series.prototype.trendLineThicknessPropertyName, Number, $.ig.FinancialIndicator.prototype.$type, new $.ig.PropertyMetadata(2, 1.5, function (sender, e) {
	(sender).raisePropertyChanged($.ig.Series.prototype.trendLineThicknessPropertyName, e.oldValue(), e.newValue());
}));
$.ig.FinancialIndicator.prototype.trendLineDashCapProperty = $.ig.DependencyProperty.prototype.register($.ig.Series.prototype.trendLineDashCapPropertyName, $.ig.PenLineCap.prototype.$type, $.ig.FinancialIndicator.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.PenLineCap.prototype.getBox($.ig.PenLineCap.prototype.flat), function (sender, e) {
	(sender).raisePropertyChanged($.ig.Series.prototype.trendLineDashCapPropertyName, e.oldValue(), e.newValue());
}));
$.ig.FinancialIndicator.prototype.trendLineDashArrayProperty = $.ig.DependencyProperty.prototype.register($.ig.Series.prototype.trendLineDashArrayPropertyName, $.ig.DoubleCollection.prototype.$type, $.ig.FinancialIndicator.prototype.$type, new $.ig.PropertyMetadata(1, function (sender, e) {
	(sender).raisePropertyChanged($.ig.Series.prototype.trendLineDashArrayPropertyName, e.oldValue(), e.newValue());
}));
$.ig.FinancialIndicator.prototype.trendLinePeriodProperty = $.ig.DependencyProperty.prototype.register($.ig.Series.prototype.trendLinePeriodPropertyName, $.ig.Number.prototype.$type, $.ig.FinancialIndicator.prototype.$type, new $.ig.PropertyMetadata(2, 7, function (sender, e) {
	(sender).raisePropertyChanged($.ig.Series.prototype.trendLinePeriodPropertyName, e.oldValue(), e.newValue());
}));
$.ig.FinancialIndicator.prototype.trendLineZIndexProperty = $.ig.DependencyProperty.prototype.register($.ig.Series.prototype.trendLineZIndexPropertyName, $.ig.Number.prototype.$type, $.ig.FinancialIndicator.prototype.$type, new $.ig.PropertyMetadata(2, 1001, function (sender, e) {
	(sender).raisePropertyChanged($.ig.Series.prototype.trendLineZIndexPropertyName, e.oldValue(), e.newValue());
}));

$.ig.StrategyBasedIndicator.prototype.periodPropertyName = "Period";
$.ig.StrategyBasedIndicator.prototype.longPeriodPropertyName = "LongPeriod";
$.ig.StrategyBasedIndicator.prototype.shortPeriodPropertyName = "ShortPeriod";
$.ig.StrategyBasedIndicator.prototype._multiplerPropertyName = "Multiplier";
$.ig.StrategyBasedIndicator.prototype.invalidatesSeries = new $.ig.List$1(String, 0);

$.ig.AbsoluteVolumeOscillatorIndicator.prototype.shortPeriodProperty = $.ig.StrategyBasedIndicator.prototype.createShortPeriodProperty(10, $.ig.AbsoluteVolumeOscillatorIndicator.prototype.$type);
$.ig.AbsoluteVolumeOscillatorIndicator.prototype.longPeriodProperty = $.ig.StrategyBasedIndicator.prototype.createLongPeriodProperty(30, $.ig.AbsoluteVolumeOscillatorIndicator.prototype.$type);

$.ig.AverageDirectionalIndexIndicator.prototype.periodProperty = $.ig.StrategyBasedIndicator.prototype.createPeriodProperty(14, $.ig.AverageDirectionalIndexIndicator.prototype.$type);

$.ig.AverageTrueRangeIndicator.prototype.periodProperty = $.ig.StrategyBasedIndicator.prototype.createPeriodProperty(14, $.ig.AverageTrueRangeIndicator.prototype.$type);

$.ig.FinancialOverlay.prototype.ignoreFirstPropertyName = "IgnoreFirst";
$.ig.FinancialOverlay.prototype.ignoreFirstProperty = $.ig.DependencyProperty.prototype.register($.ig.FinancialOverlay.prototype.ignoreFirstPropertyName, $.ig.Number.prototype.$type, $.ig.FinancialOverlay.prototype.$type, new $.ig.PropertyMetadata(2, 0, function (sender, e) {
	(sender).raisePropertyChanged($.ig.FinancialOverlay.prototype.ignoreFirstPropertyName, e.oldValue(), e.newValue());
}));

$.ig.BollingerBandsOverlay.prototype.periodPropertyName = "Period";
$.ig.BollingerBandsOverlay.prototype.multiplierPropertyName = "Multiplier";
$.ig.BollingerBandsOverlay.prototype.periodProperty = $.ig.DependencyProperty.prototype.register($.ig.BollingerBandsOverlay.prototype.periodPropertyName, $.ig.Number.prototype.$type, $.ig.BollingerBandsOverlay.prototype.$type, new $.ig.PropertyMetadata(2, 14, function (sender, e) {
	(sender).raisePropertyChanged($.ig.BollingerBandsOverlay.prototype.periodPropertyName, e.oldValue(), e.newValue());
}));
$.ig.BollingerBandsOverlay.prototype.multiplierProperty = $.ig.DependencyProperty.prototype.register($.ig.BollingerBandsOverlay.prototype.multiplierPropertyName, Number, $.ig.BollingerBandsOverlay.prototype.$type, new $.ig.PropertyMetadata(2, 2, function (sender, e) {
	(sender).raisePropertyChanged($.ig.BollingerBandsOverlay.prototype.multiplierPropertyName, e.oldValue(), e.newValue());
}));

$.ig.BollingerBandWidthIndicator.prototype.multiplierPropertyName = "Multiplier";
$.ig.BollingerBandWidthIndicator.prototype.periodProperty = $.ig.StrategyBasedIndicator.prototype.createPeriodProperty(20, $.ig.BollingerBandWidthIndicator.prototype.$type);
$.ig.BollingerBandWidthIndicator.prototype.multiplierProperty = $.ig.StrategyBasedIndicator.prototype.createMultiplierProperty(2, $.ig.BollingerBandWidthIndicator.prototype.$type);

$.ig.ChaikinOscillatorIndicator.prototype.shortPeriodProperty = $.ig.StrategyBasedIndicator.prototype.createShortPeriodProperty(3, $.ig.ChaikinOscillatorIndicator.prototype.$type);
$.ig.ChaikinOscillatorIndicator.prototype.longPeriodProperty = $.ig.StrategyBasedIndicator.prototype.createLongPeriodProperty(10, $.ig.ChaikinOscillatorIndicator.prototype.$type);

$.ig.ChaikinVolatilityIndicator.prototype.periodProperty = $.ig.StrategyBasedIndicator.prototype.createPeriodProperty(10, $.ig.ChaikinVolatilityIndicator.prototype.$type);

$.ig.CommodityChannelIndexIndicator.prototype.periodProperty = $.ig.StrategyBasedIndicator.prototype.createPeriodProperty(20, $.ig.CommodityChannelIndexIndicator.prototype.$type);

$.ig.DetrendedPriceOscillatorIndicator.prototype.periodProperty = $.ig.StrategyBasedIndicator.prototype.createPeriodProperty(20, $.ig.DetrendedPriceOscillatorIndicator.prototype.$type);

$.ig.FastStochasticOscillatorIndicator.prototype.periodProperty = $.ig.StrategyBasedIndicator.prototype.createPeriodProperty(14, $.ig.FastStochasticOscillatorIndicator.prototype.$type);

$.ig.FinancialPriceSeries.prototype.displayTypePropertyName = "DisplayType";
$.ig.FinancialPriceSeries.prototype._mIN_WIDTH = 3;
$.ig.FinancialPriceSeries.prototype.trendLineTypeProperty = $.ig.DependencyProperty.prototype.register($.ig.Series.prototype.trendLineTypePropertyName, $.ig.TrendLineType.prototype.$type, $.ig.FinancialPriceSeries.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.TrendLineType.prototype.getBox($.ig.TrendLineType.prototype.none), function (sender, e) {
	(sender).raisePropertyChanged($.ig.Series.prototype.trendLineTypePropertyName, e.oldValue(), e.newValue());
}));
$.ig.FinancialPriceSeries.prototype.trendLineBrushProperty = $.ig.DependencyProperty.prototype.register($.ig.Series.prototype.trendLineBrushPropertyName, $.ig.Brush.prototype.$type, $.ig.FinancialPriceSeries.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.Series.prototype.trendLineBrushPropertyName, e.oldValue(), e.newValue());
}));
$.ig.FinancialPriceSeries.prototype.actualTrendLineBrushProperty = $.ig.DependencyProperty.prototype.register($.ig.Series.prototype.trendLineActualBrushPropertyName, $.ig.Brush.prototype.$type, $.ig.FinancialPriceSeries.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.Series.prototype.trendLineActualBrushPropertyName, e.oldValue(), e.newValue());
}));
$.ig.FinancialPriceSeries.prototype.trendLineThicknessProperty = $.ig.DependencyProperty.prototype.register($.ig.Series.prototype.trendLineThicknessPropertyName, Number, $.ig.FinancialPriceSeries.prototype.$type, new $.ig.PropertyMetadata(2, 1.5, function (sender, e) {
	(sender).raisePropertyChanged($.ig.Series.prototype.trendLineThicknessPropertyName, e.oldValue(), e.newValue());
}));
$.ig.FinancialPriceSeries.prototype.trendLineDashCapProperty = $.ig.DependencyProperty.prototype.register($.ig.Series.prototype.trendLineDashCapPropertyName, $.ig.PenLineCap.prototype.$type, $.ig.FinancialPriceSeries.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.PenLineCap.prototype.getBox($.ig.PenLineCap.prototype.flat), function (sender, e) {
	(sender).raisePropertyChanged($.ig.Series.prototype.trendLineDashCapPropertyName, e.oldValue(), e.newValue());
}));
$.ig.FinancialPriceSeries.prototype.trendLineDashArrayProperty = $.ig.DependencyProperty.prototype.register($.ig.Series.prototype.trendLineDashArrayPropertyName, $.ig.DoubleCollection.prototype.$type, $.ig.FinancialPriceSeries.prototype.$type, new $.ig.PropertyMetadata(1, function (sender, e) {
	(sender).raisePropertyChanged($.ig.Series.prototype.trendLineDashArrayPropertyName, e.oldValue(), e.newValue());
}));
$.ig.FinancialPriceSeries.prototype.trendLinePeriodProperty = $.ig.DependencyProperty.prototype.register($.ig.Series.prototype.trendLinePeriodPropertyName, $.ig.Number.prototype.$type, $.ig.FinancialPriceSeries.prototype.$type, new $.ig.PropertyMetadata(2, 7, function (sender, e) {
	(sender).raisePropertyChanged($.ig.Series.prototype.trendLinePeriodPropertyName, e.oldValue(), e.newValue());
}));
$.ig.FinancialPriceSeries.prototype.trendLineZIndexProperty = $.ig.DependencyProperty.prototype.register($.ig.Series.prototype.trendLineZIndexPropertyName, $.ig.Number.prototype.$type, $.ig.FinancialPriceSeries.prototype.$type, new $.ig.PropertyMetadata(2, 1001, function (sender, e) {
	(sender).raisePropertyChanged($.ig.Series.prototype.trendLineZIndexPropertyName, e.oldValue(), e.newValue());
}));
$.ig.FinancialPriceSeries.prototype.displayTypeProperty = $.ig.DependencyProperty.prototype.register($.ig.FinancialPriceSeries.prototype.displayTypePropertyName, $.ig.PriceDisplayType.prototype.$type, $.ig.FinancialPriceSeries.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.PriceDisplayType.prototype.getBox($.ig.PriceDisplayType.prototype.candlestick), function (sender, e) {
	(sender).raisePropertyChanged($.ig.FinancialPriceSeries.prototype.displayTypePropertyName, e.oldValue(), e.newValue());
}));

$.ig.ForceIndexIndicator.prototype.periodProperty = $.ig.StrategyBasedIndicator.prototype.createPeriodProperty(0, $.ig.ForceIndexIndicator.prototype.$type);

$.ig.FullStochasticOscillatorIndicator.prototype.smoothingPeriodPropertyName = "SmoothingPeriod";
$.ig.FullStochasticOscillatorIndicator.prototype.triggerPeriodPropertyName = "TriggerPeriod";
$.ig.FullStochasticOscillatorIndicator.prototype.periodProperty = $.ig.StrategyBasedIndicator.prototype.createPeriodProperty(14, $.ig.FullStochasticOscillatorIndicator.prototype.$type);
$.ig.FullStochasticOscillatorIndicator.prototype.smoothingPeriodProperty = $.ig.StrategyBasedIndicator.prototype.createPeriodPropertyHelper(3, $.ig.FullStochasticOscillatorIndicator.prototype.$type, "SmoothingPeriod");
$.ig.FullStochasticOscillatorIndicator.prototype.triggerPeriodProperty = $.ig.StrategyBasedIndicator.prototype.createPeriodPropertyHelper(3, $.ig.FullStochasticOscillatorIndicator.prototype.$type, "TriggerPeriod");

$.ig.MoneyFlowIndexIndicator.prototype.periodProperty = $.ig.StrategyBasedIndicator.prototype.createPeriodProperty(14, $.ig.MoneyFlowIndexIndicator.prototype.$type);

$.ig.MovingAverageConvergenceDivergenceIndicator.prototype.signalPeriodPropertyName = "SignalPeriod";
$.ig.MovingAverageConvergenceDivergenceIndicator.prototype.shortPeriodProperty = $.ig.StrategyBasedIndicator.prototype.createShortPeriodProperty(10, $.ig.MovingAverageConvergenceDivergenceIndicator.prototype.$type);
$.ig.MovingAverageConvergenceDivergenceIndicator.prototype.longPeriodProperty = $.ig.StrategyBasedIndicator.prototype.createLongPeriodProperty(30, $.ig.MovingAverageConvergenceDivergenceIndicator.prototype.$type);
$.ig.MovingAverageConvergenceDivergenceIndicator.prototype.signalPeriodProperty = $.ig.StrategyBasedIndicator.prototype.createPeriodPropertyHelper(9, $.ig.MovingAverageConvergenceDivergenceIndicator.prototype.$type, $.ig.MovingAverageConvergenceDivergenceIndicator.prototype.signalPeriodPropertyName);

$.ig.PercentagePriceOscillatorIndicator.prototype.shortPeriodProperty = $.ig.StrategyBasedIndicator.prototype.createShortPeriodProperty(10, $.ig.PercentagePriceOscillatorIndicator.prototype.$type);
$.ig.PercentagePriceOscillatorIndicator.prototype.longPeriodProperty = $.ig.StrategyBasedIndicator.prototype.createLongPeriodProperty(30, $.ig.PercentagePriceOscillatorIndicator.prototype.$type);

$.ig.PercentageVolumeOscillatorIndicator.prototype.shortPeriodProperty = $.ig.StrategyBasedIndicator.prototype.createShortPeriodProperty(10, $.ig.PercentageVolumeOscillatorIndicator.prototype.$type);
$.ig.PercentageVolumeOscillatorIndicator.prototype.longPeriodProperty = $.ig.StrategyBasedIndicator.prototype.createLongPeriodProperty(30, $.ig.PercentageVolumeOscillatorIndicator.prototype.$type);

$.ig.PriceChannelOverlay.prototype.periodPropertyName = "Period";
$.ig.PriceChannelOverlay.prototype.periodProperty = $.ig.DependencyProperty.prototype.register($.ig.PriceChannelOverlay.prototype.periodPropertyName, $.ig.Number.prototype.$type, $.ig.PriceChannelOverlay.prototype.$type, new $.ig.PropertyMetadata(2, 14, function (sender, e) {
	(sender).raisePropertyChanged($.ig.PriceChannelOverlay.prototype.periodPropertyName, e.oldValue(), e.newValue());
}));

$.ig.RateOfChangeAndMomentumIndicator.prototype.periodProperty = $.ig.StrategyBasedIndicator.prototype.createPeriodProperty(14, $.ig.RateOfChangeAndMomentumIndicator.prototype.$type);

$.ig.RelativeStrengthIndexIndicator.prototype.periodProperty = $.ig.StrategyBasedIndicator.prototype.createPeriodProperty(14, $.ig.RelativeStrengthIndexIndicator.prototype.$type);

$.ig.SlowStochasticOscillatorIndicator.prototype.periodProperty = $.ig.StrategyBasedIndicator.prototype.createPeriodProperty(14, $.ig.SlowStochasticOscillatorIndicator.prototype.$type);

$.ig.StandardDeviationIndicator.prototype.periodProperty = $.ig.StrategyBasedIndicator.prototype.createPeriodProperty(20, $.ig.StandardDeviationIndicator.prototype.$type);

$.ig.StochRSIIndicator.prototype.periodProperty = $.ig.StrategyBasedIndicator.prototype.createPeriodProperty(14, $.ig.StochRSIIndicator.prototype.$type);

$.ig.TRIXIndicator.prototype.periodProperty = $.ig.StrategyBasedIndicator.prototype.createPeriodProperty(15, $.ig.TRIXIndicator.prototype.$type);

$.ig.WilliamsPercentRIndicator.prototype.periodProperty = $.ig.StrategyBasedIndicator.prototype.createPeriodProperty(14, $.ig.WilliamsPercentRIndicator.prototype.$type);

$.ig.Legend.prototype.orientationPropertyName = "Orientation";

} (jQuery));


