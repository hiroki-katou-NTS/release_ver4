/*!@license
* Infragistics.Web.ClientUI infragistics.gauge_radialgauge.js 16.1.20161.2145
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
"IArray:bi", 
"List$1:bj", 
"IList$1:bk", 
"ICollection$1:bl", 
"Script:bm", 
"IArrayList:bn", 
"Array:bo", 
"CompareCallback:bp", 
"Func$3:bq", 
"Action$1:br", 
"Comparer$1:bs", 
"IComparer:bt", 
"IComparer$1:bu", 
"DefaultComparer$1:bv", 
"Comparison$1:bw", 
"ReadOnlyCollection$1:bx", 
"Predicate$1:by", 
"NotImplementedException:bz", 
"Dictionary_EnumerableCollection$3:ch", 
"GenericEnumerable$1:cj", 
"GenericEnumerator$1:ck", 
"INotifyCollectionChanged:cm", 
"NotifyCollectionChangedEventHandler:cn", 
"NotifyCollectionChangedEventArgs:co", 
"EventArgs:cp", 
"NotifyCollectionChangedAction:cq", 
"ObservableCollection$1:ct", 
"INotifyPropertyChanged:cu", 
"PropertyChangedEventHandler:cv", 
"PropertyChangedEventArgs:cw", 
"Delegate:cx", 
"Interlocked:cy", 
"Stack$1:c2", 
"ReverseArrayEnumerator$1:c3", 
"DependencyObject:de", 
"Dictionary:df", 
"DependencyProperty:dg", 
"PropertyMetadata:dh", 
"PropertyChangedCallback:di", 
"DependencyPropertyChangedEventArgs:dj", 
"DependencyPropertiesCollection:dk", 
"UnsetValue:dl", 
"Binding:dm", 
"PropertyPath:dn", 
"Point:dt", 
"Environment:d5", 
"StringBuilder:eu", 
"Element:fe", 
"ElementAttributeCollection:ff", 
"ElementCollection:fg", 
"WebStyle:fh", 
"ElementNodeType:fi", 
"Document:fj", 
"EventListener:fk", 
"IElementEventHandler:fl", 
"ElementEventHandler:fm", 
"ElementAttribute:fn", 
"UIElement:gd", 
"Transform:ge", 
"Key:g1", 
"ModifierKeys:g2", 
"MouseEventArgs:g3"]);


$.ig.util.defType('ModifierKeys', 'Enum', {
	init: function (v) {
		this._v = v;
	},
	$getName: function (v) {
		switch (v) {
			case 0: return "None";
			case 1: return "Alt";
			case 2: return "Control";
			case 4: return "Shift";
			case 8: return "Windows";
			case 8: return "Apple";
			default: return v.toString();
		}
	},
	$value: function () {
		return this._v;
	},
	$type: new $.ig.Type('ModifierKeys', $.ig.Enum.prototype.$type)
}, true);

$.ig.util.defType('Key', 'Enum', {
	init: function (v) {
		this._v = v;
	},
	$getName: function (v) {
		switch (v) {
			case 0: return "None";
			case 1: return "Back";
			case 2: return "Tab";
			case 3: return "Enter";
			case 4: return "Shift";
			case 5: return "Ctrl";
			case 6: return "Alt";
			case 7: return "CapsLock";
			case 8: return "Escape";
			case 9: return "Space";
			case 10: return "PageUp";
			case 11: return "PageDown";
			case 12: return "End";
			case 13: return "Home";
			case 14: return "Left";
			case 15: return "Up";
			case 16: return "Right";
			case 17: return "Down";
			case 18: return "Insert";
			case 19: return "Delete";
			case 20: return "D0";
			case 21: return "D1";
			case 22: return "D2";
			case 23: return "D3";
			case 24: return "D4";
			case 25: return "D5";
			case 26: return "D6";
			case 27: return "D7";
			case 28: return "D8";
			case 29: return "D9";
			case 30: return "A";
			case 31: return "B";
			case 32: return "C";
			case 33: return "D";
			case 34: return "E";
			case 35: return "F";
			case 36: return "G";
			case 37: return "H";
			case 38: return "I";
			case 39: return "J";
			case 40: return "K";
			case 41: return "L";
			case 42: return "M";
			case 43: return "N";
			case 44: return "O";
			case 45: return "P";
			case 46: return "Q";
			case 47: return "R";
			case 48: return "S";
			case 49: return "T";
			case 50: return "U";
			case 51: return "V";
			case 52: return "W";
			case 53: return "X";
			case 54: return "Y";
			case 55: return "Z";
			case 56: return "F1";
			case 57: return "F2";
			case 58: return "F3";
			case 59: return "F4";
			case 60: return "F5";
			case 61: return "F6";
			case 62: return "F7";
			case 63: return "F8";
			case 64: return "F9";
			case 65: return "F10";
			case 66: return "F11";
			case 67: return "F12";
			case 68: return "NumPad0";
			case 69: return "NumPad1";
			case 70: return "NumPad2";
			case 71: return "NumPad3";
			case 72: return "NumPad4";
			case 73: return "NumPad5";
			case 74: return "NumPad6";
			case 75: return "NumPad7";
			case 76: return "NumPad8";
			case 77: return "NumPad9";
			case 78: return "Multiply";
			case 79: return "Add";
			case 80: return "Subtract";
			case 81: return "Decimal";
			case 82: return "Divide";
			case 255: return "Unknown";
			default: return v.toString();
		}
	},
	$value: function () {
		return this._v;
	},
	$type: new $.ig.Type('Key', $.ig.Enum.prototype.$type)
}, true);

$.ig.util.defType('INotifyCollectionChanged', 'Object', {
	$type: new $.ig.Type('INotifyCollectionChanged', null)
}, true);

$.ig.util.defType('ObservableCollection$1', 'List$1', {
	$t: null,
	init: function ($t, initNumber) {
		this.$t = $t;
		this.$type = this.$type.specialize(this.$t);
		if (initNumber > 0) {
			switch (initNumber) {
				case 1:
					this.init1.apply(this, arguments);
					break;
				case 2:
					this.init2.apply(this, arguments);
					break;
			}
			return;
		}
		$.ig.List$1.prototype.init.call(this, this.$t, 0);
	},
	init1: function ($t, initNumber, source) {
		this.$t = $t;
		this.$type = this.$type.specialize(this.$t);
		$.ig.List$1.prototype.init1.call(this, this.$t, 1, source);
	},
	init2: function ($t, initNumber, capacity) {
		this.$t = $t;
		this.$type = this.$type.specialize(this.$t);
		$.ig.List$1.prototype.init2.call(this, this.$t, 2, capacity);
	},
	setItem: function (index, newItem) {
		var oldItem = this.__inner[index];
		$.ig.List$1.prototype.setItem.call(this, index, newItem);
		if (this.propertyChanged != null) {
			this.onPropertyChanged(new $.ig.PropertyChangedEventArgs("Item[]"));
		}
		if (this.collectionChanged != null) {
			var args = new $.ig.NotifyCollectionChangedEventArgs(2, $.ig.NotifyCollectionChangedAction.prototype.replace, $.ig.util.getBoxIfEnum(this.$t, newItem), $.ig.util.getBoxIfEnum(this.$t, oldItem), index);
			this.onCollectionChanged(args);
		}
	}
	,
	clearItems: function () {
		$.ig.List$1.prototype.clearItems.call(this);
		if (this.propertyChanged != null) {
			this.onPropertyChanged(new $.ig.PropertyChangedEventArgs("Count"));
			this.onPropertyChanged(new $.ig.PropertyChangedEventArgs("Item[]"));
		}
		if (this.collectionChanged != null) {
			var args = new $.ig.NotifyCollectionChangedEventArgs(0, $.ig.NotifyCollectionChangedAction.prototype.reset);
			this.onCollectionChanged(args);
		}
	}
	,
	insertItem: function (index, newItem) {
		$.ig.List$1.prototype.insertItem.call(this, index, newItem);
		if (this.propertyChanged != null) {
			this.onPropertyChanged(new $.ig.PropertyChangedEventArgs("Count"));
			this.onPropertyChanged(new $.ig.PropertyChangedEventArgs("Item[]"));
		}
		if (this.collectionChanged != null) {
			var args = new $.ig.NotifyCollectionChangedEventArgs(1, $.ig.NotifyCollectionChangedAction.prototype.add, $.ig.util.getBoxIfEnum(this.$t, newItem), index);
			this.onCollectionChanged(args);
		}
	}
	,
	addItem: function (newItem) {
		$.ig.List$1.prototype.addItem.call(this, newItem);
		if (this.propertyChanged != null) {
			this.onPropertyChanged(new $.ig.PropertyChangedEventArgs("Count"));
			this.onPropertyChanged(new $.ig.PropertyChangedEventArgs("Item[]"));
		}
		if (this.collectionChanged != null) {
			var args = new $.ig.NotifyCollectionChangedEventArgs(1, $.ig.NotifyCollectionChangedAction.prototype.add, $.ig.util.getBoxIfEnum(this.$t, newItem), this.count() - 1);
			this.onCollectionChanged(args);
		}
	}
	,
	removeItem: function (index) {
		var oldItem = this.__inner[index];
		$.ig.List$1.prototype.removeItem.call(this, index);
		if (this.propertyChanged != null) {
			this.onPropertyChanged(new $.ig.PropertyChangedEventArgs("Count"));
			this.onPropertyChanged(new $.ig.PropertyChangedEventArgs("Item[]"));
		}
		if (this.collectionChanged != null) {
			var args = new $.ig.NotifyCollectionChangedEventArgs(1, $.ig.NotifyCollectionChangedAction.prototype.remove, $.ig.util.getBoxIfEnum(this.$t, oldItem), index);
			this.onCollectionChanged(args);
		}
	}
	,
	collectionChanged: null,
	propertyChanged: null,
	onPropertyChanged: function (args) {
		if (this.propertyChanged != null) {
			this.propertyChanged(this, args);
		}
	}
	,
	onCollectionChanged: function (args) {
		if (this.collectionChanged != null) {
			this.collectionChanged(this, args);
		}
	}
	,
	$type: new $.ig.Type('ObservableCollection$1', $.ig.List$1.prototype.$type.specialize(0), [$.ig.INotifyCollectionChanged.prototype.$type, $.ig.INotifyPropertyChanged.prototype.$type])
}, true);

$.ig.util.defType('Stack$1', 'Object', {
	$t: null,
	init: function ($t) {
		this.$t = $t;
		this.$type = this.$type.specialize(this.$t);
		this.__inner = new $.ig.Array();
		$.ig.Object.prototype.init.call(this);
	},
	__inner: null,
	push: function (item) {
		this.__inner.add($.ig.util.getBoxIfEnum(this.$t, item));
	}
	,
	peek: function () {
		if (this.__inner.length < 1) {
			return $.ig.util.getDefaultValue(this.$t);
		}
		return $.ig.util.castObjTo$t(this.$t, this.__inner[this.__inner.length - 1]);
	}
	,
	pop: function () {
		var ret = this.__inner[this.__inner.length - 1];
		this.__inner.removeAt(this.__inner.length - 1);
		return $.ig.util.castObjTo$t(this.$t, ret);
	}
	,
	count: function () {
		return this.__inner.length;
	}
	,
	clear: function () {
		this.__inner.clear();
	}
	,
	contains: function (item) {
		return this.__inner.contains($.ig.util.getBoxIfEnum(this.$t, item));
	}
	,
	getEnumerator: function () {
		return new $.ig.ReverseArrayEnumerator$1(this.$t, this.__inner);
	}
	,
	getEnumerator: function () {
		return new $.ig.ReverseArrayEnumerator$1(this.$t, this.__inner);
	}
	,
	$type: new $.ig.Type('Stack$1', $.ig.Object.prototype.$type, [$.ig.IEnumerable$1.prototype.$type.specialize(0)])
}, true);

$.ig.util.defType('ReverseArrayEnumerator$1', 'Object', {
	$t: null,
	__index: 0,
	__array: null,
	init: function ($t, array) {
		this.$t = $t;
		this.$type = this.$type.specialize(this.$t);
		$.ig.Object.prototype.init.call(this);
		this.__array = array;
		this.__index = array.length;
	},
	current: function () {
		return $.ig.util.castObjTo$t(this.$t, this.__array[this.__index]);
	}
	,
	current: function () {
		return this.__array[this.__index];
	}
	,
	moveNext: function () {
		this.__index--;
		return this.__index >= 0;
	}
	,
	reset: function () {
		this.__index = this.__array.length;
	}
	,
	dispose: function () {
		throw new $.ig.NotImplementedException(0);
	}
	,
	$type: new $.ig.Type('ReverseArrayEnumerator$1', $.ig.Object.prototype.$type, [$.ig.IEnumerator$1.prototype.$type.specialize(0)])
}, true);

$.ig.util.defType('Environment', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	newLine: function () {
		return "\n";
	}
	,
	$type: new $.ig.Type('Environment', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('StringBuilder', 'Object', {
	_internal: null,
	internal: function (value) {
		if (arguments.length === 1) {
			this._internal = value;
			return value;
		} else {
			return this._internal;
		}
	}
	,
	init: function (initNumber) {
		if (initNumber > 0) {
			switch (initNumber) {
				case 1:
					this.init1.apply(this, arguments);
					break;
				case 2:
					this.init2.apply(this, arguments);
					break;
			}
			return;
		}
		$.ig.Object.prototype.init.call(this);
		this.internal("");
	},
	init1: function (initNumber, capacity) {
		$.ig.StringBuilder.prototype.init.call(this, 0);
	},
	init2: function (initNumber, value) {
		$.ig.Object.prototype.init.call(this);
		this.internal(value);
	},
	append4: function (obj) {
		if (obj != null) {
			this.append5(obj.toString());
		}
		return this;
	}
	,
	append5: function (str_) {
		if (str_ != null)
        {
            this._internal = this._internal.concat(str_);
        };
		return this;
	}
	,
	append7: function (builder) {
		var str_ = builder.toString();
		this._internal = this._internal.concat(str_);
		return this;
	}
	,
	append1: function (chr_) {
		this._internal = this._internal.concat(chr_);
		return this;
	}
	,
	append2: function (chr_, count_) {
		this._internal = this._internal.concat(chr_.repeat(count_));
		return this;
	}
	,
	append3: function (value_) {
		this._internal = this._internal.concat(value_);
		return this;
	}
	,
	append6: function (value_, startIndex_, count_) {
		this._internal = this._internal.concat(value_.substr(startIndex_, count_));
		return this;
	}
	,
	append: function (value_, startIndex_, charCount_) {
		this._internal = this._internal.concat(value_.slice(startIndex_, startIndex_ + charCount_).join(''));
		return this;
	}
	,
	appendLine: function () {
		return this.appendLine1("");
	}
	,
	appendLine1: function (str_) {
		if (str_ != null)
        {
            this._internal = this._internal.concat(str_);
        }
        this._internal = this._internal.concat($.ig.Environment.prototype.newLine());;
		return this;
	}
	,
	clear: function () {
		this.internal("");
		return this;
	}
	,
	insert: function (index_, chr_) {
		if (index_ == this.length()) {
			this.append1(chr_);
		} else {
			this._internal = this._internal.substring(0, index_).concat(chr_).concat(this._internal.substring(index_, this._internal.length));
		}
		return this;
	}
	,
	insert1: function (index_, str_) {
		if (index_ == this.length()) {
			this.append5(str_);
		} else {
			this._internal = this._internal.substring(0, index_).concat(str_).concat(this._internal.substring(index_, this._internal.length));
		}
		return this;
	}
	,
	remove: function (startIndex_, length_) {
		this._internal = this._internal.substring(0, startIndex_).concat(this._internal.substring(startIndex_ + length_, this._internal.length));
		return this;
	}
	,
	toString: function () {
		return this.internal();
	}
	,
	toString1: function (startIndex, length) {
		return this.internal().substr(startIndex, length);
	}
	,
	length: function (value) {
		if (arguments.length === 1) {
			if (value <= this.length()) {
				this._internal = this._internal.substring(0, value);
			} else {
				throw new $.ig.NotImplementedException(0);
			}
			return value;
		} else {
			return this.internal().length;
		}
	}
	,
	item: function (index_, value) {
		if (arguments.length === 2) {
			this._internal = this._internal.substring(0, index_).concat(value).concat(this._internal.substring(index_ + 1, this._internal.length));
			return value;
		} else {
			return this.internal().charAt(index_);
		}
	}
	,
	appendFormat2: function (format, arg0) {
		return this.append5($.ig.util.stringFormat(format, arg0));
	}
	,
	appendFormat1: function (format, args) {
		return this.append5($.ig.util.stringFormat1(format, args));
	}
	,
	appendFormat: function (provider, format, args) {
		return this.append5($.ig.util.stringFormat2(provider, format, args));
	}
	,
	appendFormat3: function (format, arg0, arg1) {
		return this.append5($.ig.util.stringFormat(format, arg0, arg1));
	}
	,
	appendFormat4: function (format, arg0, arg1, arg2) {
		return this.append5($.ig.util.stringFormat(format, arg0, arg1, arg2));
	}
	,
	_capacity: 0,
	capacity: function (value) {
		if (arguments.length === 1) {
			this._capacity = value;
			return value;
		} else {
			return this._capacity;
		}
	}
	,
	$type: new $.ig.Type('StringBuilder', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('MouseEventArgs', 'EventArgs', {
	init: function () {
		$.ig.EventArgs.prototype.init.call(this);
	},
	_position: null,
	position: function (value) {
		if (arguments.length === 1) {
			this._position = value;
			return value;
		} else {
			return this._position;
		}
	}
	,
	_originalSource: null,
	originalSource: function (value) {
		if (arguments.length === 1) {
			this._originalSource = value;
			return value;
		} else {
			return this._originalSource;
		}
	}
	,
	getPosition: function (relativeTo) {
		return this.position();
	}
	,
	$type: new $.ig.Type('MouseEventArgs', $.ig.EventArgs.prototype.$type)
}, true);

$.ig.ModifierKeys.prototype.none = 0;
$.ig.ModifierKeys.prototype.alt = 1;
$.ig.ModifierKeys.prototype.control = 2;
$.ig.ModifierKeys.prototype.shift = 4;
$.ig.ModifierKeys.prototype.windows = 8;
$.ig.ModifierKeys.prototype.apple = 8;

$.ig.Key.prototype.none = 0;
$.ig.Key.prototype.back = 1;
$.ig.Key.prototype.tab = 2;
$.ig.Key.prototype.enter = 3;
$.ig.Key.prototype.shift = 4;
$.ig.Key.prototype.ctrl = 5;
$.ig.Key.prototype.alt = 6;
$.ig.Key.prototype.capsLock = 7;
$.ig.Key.prototype.escape = 8;
$.ig.Key.prototype.space = 9;
$.ig.Key.prototype.pageUp = 10;
$.ig.Key.prototype.pageDown = 11;
$.ig.Key.prototype.end = 12;
$.ig.Key.prototype.home = 13;
$.ig.Key.prototype.left = 14;
$.ig.Key.prototype.up = 15;
$.ig.Key.prototype.right = 16;
$.ig.Key.prototype.down = 17;
$.ig.Key.prototype.insert = 18;
$.ig.Key.prototype.del = 19;
$.ig.Key.prototype.d0 = 20;
$.ig.Key.prototype.d1 = 21;
$.ig.Key.prototype.d2 = 22;
$.ig.Key.prototype.d3 = 23;
$.ig.Key.prototype.d4 = 24;
$.ig.Key.prototype.d5 = 25;
$.ig.Key.prototype.d6 = 26;
$.ig.Key.prototype.d7 = 27;
$.ig.Key.prototype.d8 = 28;
$.ig.Key.prototype.d9 = 29;
$.ig.Key.prototype.a = 30;
$.ig.Key.prototype.b = 31;
$.ig.Key.prototype.c = 32;
$.ig.Key.prototype.d = 33;
$.ig.Key.prototype.e = 34;
$.ig.Key.prototype.f = 35;
$.ig.Key.prototype.g = 36;
$.ig.Key.prototype.h = 37;
$.ig.Key.prototype.i = 38;
$.ig.Key.prototype.j = 39;
$.ig.Key.prototype.k = 40;
$.ig.Key.prototype.l = 41;
$.ig.Key.prototype.m = 42;
$.ig.Key.prototype.n = 43;
$.ig.Key.prototype.o = 44;
$.ig.Key.prototype.p = 45;
$.ig.Key.prototype.q = 46;
$.ig.Key.prototype.r = 47;
$.ig.Key.prototype.s = 48;
$.ig.Key.prototype.t = 49;
$.ig.Key.prototype.u = 50;
$.ig.Key.prototype.v = 51;
$.ig.Key.prototype.w = 52;
$.ig.Key.prototype.x = 53;
$.ig.Key.prototype.y = 54;
$.ig.Key.prototype.z = 55;
$.ig.Key.prototype.f1 = 56;
$.ig.Key.prototype.f2 = 57;
$.ig.Key.prototype.f3 = 58;
$.ig.Key.prototype.f4 = 59;
$.ig.Key.prototype.f5 = 60;
$.ig.Key.prototype.f6 = 61;
$.ig.Key.prototype.f7 = 62;
$.ig.Key.prototype.f8 = 63;
$.ig.Key.prototype.f9 = 64;
$.ig.Key.prototype.f10 = 65;
$.ig.Key.prototype.f11 = 66;
$.ig.Key.prototype.f12 = 67;
$.ig.Key.prototype.numPad0 = 68;
$.ig.Key.prototype.numPad1 = 69;
$.ig.Key.prototype.numPad2 = 70;
$.ig.Key.prototype.numPad3 = 71;
$.ig.Key.prototype.numPad4 = 72;
$.ig.Key.prototype.numPad5 = 73;
$.ig.Key.prototype.numPad6 = 74;
$.ig.Key.prototype.numPad7 = 75;
$.ig.Key.prototype.numPad8 = 76;
$.ig.Key.prototype.numPad9 = 77;
$.ig.Key.prototype.multiply = 78;
$.ig.Key.prototype.add = 79;
$.ig.Key.prototype.subtract = 80;
$.ig.Key.prototype.decimal = 81;
$.ig.Key.prototype.divide = 82;
$.ig.Key.prototype.unknown = 255;

} (jQuery));



(function ($) {
$.ig = $.ig || {};
var $$t = {}
$.ig.$currDefinitions = $$t;
$.ig.util.bulkDefine(["EventProxy:a", 
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
"Rect:ba", 
"Size:bb", 
"Point:bc", 
"Math:bd", 
"ModifierKeys:be", 
"Func$2:bf", 
"MulticastDelegate:bg", 
"IntPtr:bh", 
"MouseWheelHandler:bi", 
"Delegate:bj", 
"Interlocked:bk", 
"GestureHandler:bl", 
"ZoomGestureHandler:bm", 
"FlingGestureHandler:bn", 
"ContactHandler:bo", 
"TouchHandler:bp", 
"MouseOverHandler:bq", 
"MouseHandler:br", 
"KeyHandler:bs", 
"Key:bt", 
"BaseDOMEventProxy:bu", 
"JQueryObject:bv", 
"Element:bw", 
"ElementAttributeCollection:bx", 
"ElementCollection:by", 
"WebStyle:bz", 
"ElementNodeType:b0", 
"Document:b1", 
"EventListener:b2", 
"IElementEventHandler:b3", 
"ElementEventHandler:b4", 
"ElementAttribute:b5", 
"JQueryPosition:b6", 
"JQueryCallback:b7", 
"JQueryEvent:b8", 
"JQueryUICallback:b9", 
"DOMEventProxy:ca", 
"MSGesture:cb", 
"Script:cc", 
"JQuery:cd", 
"JQueryDeferred:ce", 
"JQueryPromise:cf", 
"Action:cg", 
"Action$1:ch", 
"Callback:ci", 
"window:cj", 
"MouseEventArgs:ck", 
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
"Shape:et", 
"FrameworkElement:eu", 
"Visibility:ev", 
"Style:ew", 
"Brush:ex", 
"Color:ey", 
"DoubleCollection:ez", 
"Path:e0", 
"Geometry:e1", 
"GeometryType:e2", 
"TextBlock:e3", 
"PointCollection:e5", 
"Line:fj", 
"FontInfo:fk", 
"LinearGradientBrush:fv", 
"GradientStop:fw", 
"GeometryGroup:fx", 
"GeometryCollection:fy", 
"FillRule:fz", 
"PathGeometry:f0", 
"PathFigureCollection:f1", 
"LineGeometry:f2", 
"RectangleGeometry:f3", 
"EllipseGeometry:f4", 
"ArcSegment:f5", 
"PathSegment:f6", 
"PathSegmentType:f7", 
"SweepDirection:f8", 
"PathFigure:f9", 
"PathSegmentCollection:ga", 
"LineSegment:gb", 
"PolyLineSegment:gc", 
"BezierSegment:gd", 
"PolyBezierSegment:ge", 
"GeometryUtil:gf", 
"Tuple$2:gg", 
"TransformGroup:gh", 
"TransformCollection:gi", 
"RotateTransform:gk", 
"INotifyPropertyChanged:gn", 
"PropertyChangedEventHandler:go", 
"PropertyChangedEventArgs:gp", 
"DivElement:gy", 
"DoubleAnimator:gz", 
"EasingFunctionHandler:g0", 
"MathUtil:g3", 
"RuntimeHelpers:g4", 
"RuntimeFieldHandle:g5", 
"BrushUtil:g6", 
"ColorUtil:g7", 
"Random:g8", 
"InterpolationMode:g9", 
"BrushCollection:ha", 
"ObservableCollection$1:hb", 
"INotifyCollectionChanged:hc", 
"NotifyCollectionChangedEventHandler:hd", 
"CssHelper:he", 
"CssGradientUtil:hf", 
"Stack$1:hj", 
"ReverseArrayEnumerator$1:hk", 
"StackPool$1:ia", 
"Func$1:ib", 
"StringBuilder:ih", 
"Environment:ii", 
"DoubleValueChangedEventArgs:ir", 
"DoubleValueChangedEventHandler:is", 
"InterpolationUtil:jj", 
"Func$5:jk", 
"PolygonUtil:jo", 
"IVisualData:jt", 
"PrimitiveVisualData:jv", 
"PrimitiveAppearanceData:jw", 
"BrushAppearanceData:jx", 
"AppearanceHelper:jy", 
"LinearGradientBrushAppearanceData:jz", 
"GradientStopAppearanceData:j0", 
"SolidBrushAppearanceData:j1", 
"GeometryData:j2", 
"GetPointsSettings:j3", 
"EllipseGeometryData:j4", 
"RectangleGeometryData:j5", 
"LineGeometryData:j6", 
"PathGeometryData:j7", 
"PathFigureData:j8", 
"SegmentData:j9", 
"LineSegmentData:ka", 
"PolylineSegmentData:kb", 
"ArcSegmentData:kc", 
"PolyBezierSegmentData:kd", 
"BezierSegmentData:ke", 
"LabelAppearanceData:kf", 
"ShapeTags:kg", 
"PathVisualData:ko", 
"AbstractEnumerable:kp", 
"AbstractEnumerator:kq", 
"GenericEnumerable$1:kr", 
"GenericEnumerator$1:ks"]);


$.ig.util.defType('InterpolationMode', 'Enum', {
	init: function (v) {
		this._v = v;
	},
	$getName: function (v) {
		switch (v) {
			case 0: return "RGB";
			case 1: return "HSV";
			default: return v.toString();
		}
	},
	$value: function () {
		return this._v;
	},
	$type: new $.ig.Type('InterpolationMode', $.ig.Enum.prototype.$type)
}, true);

$.ig.util.defType('EventProxy', 'Object', {
	init: function () {
		this.__isInteractionDisabled = false;
		$.ig.Object.prototype.init.call(this);
	},
	onMouseWheel: null,
	onPinchStarted: null,
	onPinchDelta: null,
	onGestureCompleted: null,
	onZoomStarted: null,
	onZoomDelta: null,
	onZoomCompleted: null,
	onFlingStarted: null,
	onContactStarted: null,
	onDragStarted: null,
	onContactMoved: null,
	onDragDelta: null,
	onContactCompleted: null,
	onDragCompleted: null,
	onMouseLeave: null,
	onMouseOver: null,
	onMouseEnter: null,
	onMouseDown: null,
	onMouseUp: null,
	onDoubleTap: null,
	onHold: null,
	onKeyDown: null,
	onKeyUp: null,
	_viewport: null,
	viewport: function (value) {
		if (arguments.length === 1) {
			this._viewport = value;
			return value;
		} else {
			return this._viewport;
		}
	}
	,
	_currentModifiers: 0,
	currentModifiers: function (value) {
		if (arguments.length === 1) {
			this._currentModifiers = value;
			return value;
		} else {
			return this._currentModifiers;
		}
	}
	,
	_rightButton: false,
	rightButton: function (value) {
		if (arguments.length === 1) {
			this._rightButton = value;
			return value;
		} else {
			return this._rightButton;
		}
	}
	,
	_shouldInteract: null,
	shouldInteract: function (value) {
		if (arguments.length === 1) {
			this._shouldInteract = value;
			return value;
		} else {
			return this._shouldInteract;
		}
	}
	,
	clone: function () {
	}
	,
	destroy: function () {
	}
	,
	supportsNonIsometricZoom: function () {
		return false;
	}
	,
	_originalEvent: null,
	originalEvent: function (value) {
		if (arguments.length === 1) {
			this._originalEvent = value;
			return value;
		} else {
			return this._originalEvent;
		}
	}
	,
	raiseOnMouseWheel: function (point, delta) {
		if (this.onMouseWheel != null && !this.isInteractionDisabled()) {
			return this.onMouseWheel(point, delta);
		}
		return false;
	}
	,
	raiseOnPinchStarted: function (point, scale) {
		if (this.onPinchStarted != null && !this.isInteractionDisabled()) {
			this.onPinchStarted(point, scale);
		}
	}
	,
	raiseOnZoomStarted: function (point, scaleX, scaleY, isoScaleDelta) {
		if (this.onZoomStarted != null && !this.isInteractionDisabled()) {
			this.onZoomStarted(point, scaleX, scaleY, isoScaleDelta);
		}
	}
	,
	raiseOnFlingStarted: function (point, velocityX, velocityY) {
		if (this.onFlingStarted != null && !this.isInteractionDisabled()) {
			return this.onFlingStarted(point, velocityX, velocityY);
		}
		return true;
	}
	,
	raiseOnPinchDelta: function (point, scale) {
		if (this.onPinchDelta != null && !this.isInteractionDisabled()) {
			this.onPinchDelta(point, scale);
		}
	}
	,
	raiseOnZoomDelta: function (point, scaleX, scaleY, isoScaleDelta) {
		if (this.onZoomDelta != null && !this.isInteractionDisabled()) {
			this.onZoomDelta(point, scaleX, scaleY, isoScaleDelta);
		}
	}
	,
	raiseOnZoomCompleted: function (point, scaleX, scaleY, isoScaleDelta) {
		if (this.onZoomCompleted != null && !this.isInteractionDisabled()) {
			this.onZoomCompleted(point, scaleX, scaleY, isoScaleDelta);
		}
	}
	,
	raiseOnGestureCompleted: function (point, scale) {
		if (this.onGestureCompleted != null && !this.isInteractionDisabled()) {
			this.onGestureCompleted(point, scale);
		}
	}
	,
	raiseOnContactStarted: function (point, isFinger) {
		if (this.onContactStarted != null && !this.isInteractionDisabled()) {
			this.onContactStarted(point, isFinger);
		}
	}
	,
	raiseOnDragStarted: function (point) {
		if (this.onDragStarted != null && !this.isInteractionDisabled()) {
			this.onDragStarted(point);
		}
	}
	,
	raiseOnContactMoved: function (point, isFinger) {
		if (this.onContactMoved != null && !this.isInteractionDisabled()) {
			this.onContactMoved(point, isFinger);
		}
	}
	,
	raiseOnDragDelta: function (point) {
		if (this.onDragDelta != null && !this.isInteractionDisabled()) {
			this.onDragDelta(point);
		}
	}
	,
	raiseOnContactCompleted: function (point, isFinger) {
		if (this.onContactCompleted != null && !this.isInteractionDisabled()) {
			this.onContactCompleted(point, isFinger);
		}
	}
	,
	raiseOnDragCompleted: function (point) {
		if (this.onDragCompleted != null && !this.isInteractionDisabled()) {
			this.onDragCompleted(point);
		}
	}
	,
	raiseOnMouseLeave: function (point) {
		if (this.onMouseLeave != null && !this.isInteractionDisabled()) {
			this.onMouseLeave(point);
		}
	}
	,
	raiseOnMouseOver: function (point, onMouseMove, isFinger) {
		if (this.onMouseOver != null && !this.isInteractionDisabled()) {
			this.onMouseOver(point, onMouseMove, isFinger);
		}
	}
	,
	raiseOnMouseEnter: function (point) {
		if (this.onMouseEnter != null && !this.isInteractionDisabled()) {
			this.onMouseEnter(point);
		}
	}
	,
	raiseOnMouseDown: function (point) {
		if (this.onMouseDown != null && !this.isInteractionDisabled()) {
			this.onMouseDown(point);
		}
	}
	,
	raiseOnMouseUp: function (point) {
		if (this.onMouseUp != null && !this.isInteractionDisabled()) {
			this.onMouseUp(point);
		}
	}
	,
	raiseOnKeyDown: function (key) {
		if (this.onKeyDown != null && !this.isInteractionDisabled()) {
			return this.onKeyDown(key);
		}
		return false;
	}
	,
	raiseOnKeyUp: function (key) {
		if (this.onKeyUp != null && !this.isInteractionDisabled()) {
			return this.onKeyUp(key);
		}
		return false;
	}
	,
	raiseOnDoubleTap: function (point) {
		if (this.onDoubleTap != null && !this.isInteractionDisabled()) {
			this.onDoubleTap(point);
		}
	}
	,
	raiseOnHold: function (point) {
		if (this.onHold != null && !this.isInteractionDisabled()) {
			this.onHold(point);
		}
	}
	,
	getSourceOffsets: function () {
	}
	,
	__isInteractionDisabled: false,
	isInteractionDisabled: function (value) {
		if (arguments.length === 1) {
			this.__isInteractionDisabled = value;
			this.onIsInteractionDisabledChanged();
			return value;
		} else {
			return this.__isInteractionDisabled;
		}
	}
	,
	_deferredTouchStartMode: false,
	deferredTouchStartMode: function (value) {
		if (arguments.length === 1) {
			this._deferredTouchStartMode = value;
			return value;
		} else {
			return this._deferredTouchStartMode;
		}
	}
	,
	_shouldInteractForDirection: null,
	shouldInteractForDirection: function (value) {
		if (arguments.length === 1) {
			this._shouldInteractForDirection = value;
			return value;
		} else {
			return this._shouldInteractForDirection;
		}
	}
	,
	onIsInteractionDisabledChanged: function () {
	}
	,
	bindToSource: function (source, sourceId) {
	}
	,
	unbindFromSource: function (source, sourceId) {
	}
	,
	$type: new $.ig.Type('EventProxy', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('BaseDOMEventProxy', 'EventProxy', {
	init: function () {
		this.__touchCaptureEnabled = false;
		this.__mouseX = 0;
		this.__mouseY = 0;
		this.__containerMouseX = 0;
		this.__containerMouseY = 0;
		this.__numTouches = 0;
		this.__suppressMouseEvents = false;
		this.__suppressId = $.ig.BaseDOMEventProxy.prototype.nullTimer;
		this.__touchStartDeferred = false;
		this.__mouseCaptured = false;
		$.ig.EventProxy.prototype.init.call(this);
	},
	_eventSource: null,
	eventSource: function (value) {
		if (arguments.length === 1) {
			this._eventSource = value;
			return value;
		} else {
			return this._eventSource;
		}
	}
	,
	clone: function () {
		var ret = new $.ig.DOMEventProxy(this.eventSource());
		ret.deferredTouchStartMode(this.deferredTouchStartMode());
		ret.shouldInteractForDirection(this.shouldInteractForDirection());
		return ret;
	}
	,
	preventDefault: function (e) {
		if (!this.isInteractionDisabled()) {
			if (this.isCancelable(e)) {
				e.preventDefault();
			}
		}
	}
	,
	isCancelable: function (e_) {
		return e_.originalEvent.cancelable || e_.originalEvent.cancelable == undefined;
	}
	,
	__touchCaptureEnabled: false,
	enableTouchCapture: function () {
		var source_ = this.eventSource()[0];
		$.ig.BaseDOMEventProxy.prototype.grabTouches(source_);
	}
	,
	grabTouches: function (source_) {
		if ($.ig.BaseDOMEventProxy.prototype.pointerEnabled) {
			source_.style.touchAction = 'none';
		} else {
			source_.style.msTouchAction = 'none';
		}
		source_.style.msUserSelect = 'none';
		source_.style.webkitTouchCallout = 'none';
		source_.style.webkitUserSelect = 'none';
		source_.style.khtmlUserSelect = 'none';
		source_.style.mozUserSelect = 'none';
		source_.style.userSelect = 'none';
	}
	,
	deferTouches: function (source_) {
		if ($.ig.BaseDOMEventProxy.prototype.pointerEnabled) {
			source_.style.touchAction = 'auto';
		} else {
			source_.style.msTouchAction = 'auto';
		}
		source_.style.msUserSelect = 'auto';
		source_.style.webkitTouchCallout = 'none';
		source_.style.webkitUserSelect = 'auto';
		source_.style.khtmlUserSelect = 'auto';
		source_.style.mozUserSelect = 'auto';
		source_.style.userSelect = 'auto';
	}
	,
	disableTouchCapture: function () {
		var source_ = this.eventSource()[0];
		$.ig.BaseDOMEventProxy.prototype.deferTouches(source_);
	}
	,
	onIsInteractionDisabledChanged: function () {
		$.ig.EventProxy.prototype.onIsInteractionDisabledChanged.call(this);
		if (!this.isInteractionDisabled()) {
			this.enableTouchCapture();
		} else {
			this.disableTouchCapture();
		}
	}
	,
	getTridentVersion: function () {
		var ver_ = -1;
		var matchIE_ = /Trident\/([\d.]+)/;
		if (matchIE_.exec(navigator.userAgent) != null) {
			ver_ = parseFloat(RegExp.$1);
		}
		return ver_;
	}
	,
	getEdgeVersion: function () {
		var ver_ = -1;
		var matchIE_ = /Edge\/([\d.]+)/;
		if (matchIE_.exec(navigator.userAgent) != null) {
			ver_ = parseFloat(RegExp.$1);
		}
		return ver_;
	}
	,
	fixEvent: function (e_) {
		var ieHack = $.ig.BaseDOMEventProxy.prototype.tridentVersion >= 6;
		var oe_ = e_.originalEvent;
		if (((typeof e_.pageX == 'undefined') || ieHack) && oe_.clientX != null) {
			var od_ = e_.target.ownerDocument;
			var ed_ = od_ ? od_ : document;
			var doc_ = ed_.documentElement;
			var body_ = ed_.body;
			var clientX_ = oe_.clientX;
			var clientY_ = oe_.clientY;
			var scrollLeft_ = doc_ && doc_.scrollLeft || body_ && body_.scrollLeft || 0;
			var scrollTop_ = doc_ && doc_.scrollTop || body_ && body_.scrollTop || 0;
			var clientLeft_ = doc_ && doc_.clientLeft || body_ && body_.clientLeft || 0;
			var clientTop_ = doc_ && doc_.clientTop || body_ && body_.clientTop || 0;
			e_.pageX = clientX_ + (scrollLeft_ - clientLeft_);
			e_.pageY = clientY_ + (scrollTop_ - clientTop_);
		}
		if (!e_.pageX) {
			e_.pageX = e_.originalEvent.pageX;
		}
		if (!e_.pageY) {
			e_.pageY = e_.originalEvent.pageY;
		}
	}
	,
	getOffset: function (source) {
		var ieHack = $.ig.BaseDOMEventProxy.prototype.tridentVersion >= 6;
		var source_ = source[0];
		var d_ = source_.ownerDocument;
		var doc_ = d_ ? d_.documentElement : null;
		var z_ = doc_ ? doc_.msContentZoomFactor : null;
		var body_ = doc_.body;
		if (z_ && z_ > 1 || ieHack) {
			var rect_ = source_.getBoundingClientRect();
			var x_ = rect_.left;
			var y_ = rect_.top;
			var scrollLeft_ = doc_ && doc_.scrollLeft || body_ && body_.scrollLeft || 0;
			var scrollTop_ = doc_ && doc_.scrollTop || body_ && body_.scrollTop || 0;
			var clientLeft_ = doc_ && doc_.clientLeft || body_ && body_.clientLeft || 0;
			var clientTop_ = doc_ && doc_.clientTop || body_ && body_.clientTop || 0;
			var left = x_ + scrollLeft_ - clientLeft_;
			var top = y_ + scrollTop_ - clientTop_;
			return new $.ig.Rect(0, left, top, 0, 0);
		} else {
			var offset = source.offset();
			return new $.ig.Rect(0, offset.left, offset.top, 0, 0);
		}
	}
	,
	__mouseX: 0,
	__mouseY: 0,
	__containerMouseX: 0,
	__containerMouseY: 0,
	__numTouches: 0,
	_mousePoint: null,
	mousePoint: function (value) {
		if (arguments.length === 1) {
			this._mousePoint = value;
			return value;
		} else {
			return this._mousePoint;
		}
	}
	,
	_containerMousePoint: null,
	containerMousePoint: function (value) {
		if (arguments.length === 1) {
			this._containerMousePoint = value;
			return value;
		} else {
			return this._containerMousePoint;
		}
	}
	,
	numTouches: function (value) {
		if (arguments.length === 1) {
			this.__numTouches = value;
			return value;
		} else {
			return this.__numTouches;
		}
	}
	,
	updateMousePosition: function (e) {
		this.originalEvent(e);
		$.ig.BaseDOMEventProxy.prototype.fixEvent(e);
		var offset = $.ig.BaseDOMEventProxy.prototype.getOffset(this.eventSource());
		this.__mouseX = e.pageX - offset.left();
		this.__mouseY = e.pageY - offset.top();
		this.__containerMouseX = this.__mouseX - this.viewport().left();
		this.__containerMouseY = this.__mouseY - this.viewport().top();
		this.mousePoint({ __x: this.__mouseX, __y: this.__mouseY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		this.containerMousePoint({ __x: this.__containerMouseX, __y: this.__containerMouseY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
	}
	,
	updateTouchPosition: function (e_) {
		this.originalEvent(e_);
		$.ig.BaseDOMEventProxy.prototype.fixEvent(e_);
		if ($.ig.BaseDOMEventProxy.prototype.mSPointerEnabled || $.ig.BaseDOMEventProxy.prototype.pointerEnabled) {
			var pageX = e_.pageX;
			var pageY = e_.pageY;
			var offset = $.ig.BaseDOMEventProxy.prototype.getOffset(this.eventSource());
			this.__mouseX = pageX - offset.left();
			this.__mouseY = pageY - offset.top();
		} else {
			this.__numTouches = 0;
			if (!e_.originalEvent.targetTouches || e_.originalEvent.targetTouches.length < 1) { return; };
			this.__numTouches = e_.originalEvent.targetTouches.length;
			var pageX1 = e_.originalEvent.targetTouches[0].pageX;
			var pageY1 = e_.originalEvent.targetTouches[0].pageY;
			var offset1 = $.ig.BaseDOMEventProxy.prototype.getOffset(this.eventSource());
			this.__mouseX = pageX1 - offset1.left();
			this.__mouseY = pageY1 - offset1.top();
		}
		this.__containerMouseX = this.__mouseX - this.viewport().left();
		this.__containerMouseY = this.__mouseY - this.viewport().top();
		this.mousePoint({ __x: this.__mouseX, __y: this.__mouseY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		this.containerMousePoint({ __x: this.__containerMouseX, __y: this.__containerMouseY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
	}
	,
	canvasMouseScroll: function (e_) {
		this.updateModifiers(e_);
		if (this.shouldInteract()(this.mousePoint())) {
			var delta_ = 0;
			if (e_.wheelDelta) { delta_ = e_.wheelDelta/120; };
			if (e_.originalEvent && e_.originalEvent.wheelDelta) { delta_ = e_.originalEvent.wheelDelta/120; };
			if (e_.detail) { delta_ = -e_.detail/3; };
			if (e_.originalEvent && e_.originalEvent.detail) { delta_ = -e_.originalEvent.detail/3; };
			delta_ = delta_ / 10;
			var handled = this.raiseOnMouseWheel(this.containerMousePoint(), delta_);
			if (handled) {
				this.preventDefault(e_);
			}
		}
	}
	,
	__suppressMouseEvents: false,
	__suppressId: 0,
	endMouseSuppress: function () {
		if (this.__suppressMouseEvents) {
			if (this.__suppressId == $.ig.BaseDOMEventProxy.prototype.nullTimer) {
				this.__suppressId = window.setTimeout(this.doEndSuppress.runOn(this), 500);
			} else {
				window.clearTimeout(this.__suppressId);
				this.__suppressId = $.ig.BaseDOMEventProxy.prototype.nullTimer;
				this.__suppressId = window.setTimeout(this.doEndSuppress.runOn(this), 500);
			}
		}
	}
	,
	beginMouseSuppress: function () {
		this.__suppressMouseEvents = true;
		if (this.__suppressId != $.ig.BaseDOMEventProxy.prototype.nullTimer) {
			window.clearTimeout(this.__suppressId);
			this.__suppressId = $.ig.BaseDOMEventProxy.prototype.nullTimer;
		}
	}
	,
	doEndSuppress: function () {
		this.__suppressMouseEvents = false;
	}
	,
	_mouseIsOver: false,
	mouseIsOver: function (value) {
		if (arguments.length === 1) {
			this._mouseIsOver = value;
			return value;
		} else {
			return this._mouseIsOver;
		}
	}
	,
	__touchStartDeferred: false,
	touchStartDeferred: function (value) {
		if (arguments.length === 1) {
			this.__touchStartDeferred = value;
			return value;
		} else {
			return this.__touchStartDeferred;
		}
	}
	,
	canvasMouseLeave: function (e) {
		if (this.__touchStartDeferred || this.__suppressMouseEvents) {
			return;
		}
		this.updateMousePosition(e);
		if (this.mouseIsOver()) {
			this.mouseIsOver(false);
			this.raiseOnMouseLeave(this.containerMousePoint());
		}
	}
	,
	canvasMouseMove: function (e) {
		if (this.__touchStartDeferred || this.__suppressMouseEvents) {
			return;
		}
		this.updateMousePosition(e);
		this.updateModifiers(e);
		var me = new $.ig.MouseEventArgs();
		me.position(this.mousePoint());
		if (this.shouldInteract()(this.mousePoint())) {
			if (!this.mouseIsOver()) {
				this.mouseIsOver(true);
				this.raiseOnMouseEnter(this.mousePoint());
			}
			this.raiseOnMouseOver(this.mousePoint(), true, false);
			this.raiseOnContactMoved(this.containerMousePoint(), false);
		} else if (this.mouseIsOver()) {
			this.canvasMouseLeave(e);
		}
	}
	,
	__mouseCaptured: false,
	canvasMouseDown: function (e) {
		if (this.__touchStartDeferred || this.__suppressMouseEvents) {
			return;
		}
		this.eventSource().focus();
		this.updateMousePosition(e);
		this.updateModifiers(e);
		if (this.shouldInteract()(this.mousePoint())) {
			this.__mouseCaptured = true;
			this.raiseOnMouseDown(this.mousePoint());
			this.raiseOnContactStarted(this.containerMousePoint(), false);
			this.preventDefault(e);
		}
	}
	,
	windowMouseUp: function (e) {
		if (this.__touchStartDeferred || this.__suppressMouseEvents) {
			return;
		}
		if (this.__mouseCaptured) {
			this.canvasMouseUp(e);
		}
	}
	,
	canvasMouseUp: function (e) {
		if (this.__touchStartDeferred || this.__suppressMouseEvents) {
			return;
		}
		this.updateModifiers(e);
		this.__mouseCaptured = false;
		this.raiseOnMouseUp(this.mousePoint());
		this.raiseOnContactCompleted(this.containerMousePoint(), false);
		this.preventDefault(e);
	}
	,
	canvasKeyDown: function (e) {
		this.canvasKeyEvent(e, true);
	}
	,
	canvasKeyUp: function (e) {
		this.canvasKeyEvent(e, false);
	}
	,
	getKey: function (e) {
		var downKey = $.ig.Key.prototype.none;
		switch (e.which) {
			case 33:
				downKey = $.ig.Key.prototype.pageUp;
				break;
			case 34:
				downKey = $.ig.Key.prototype.pageDown;
				break;
			case 36:
				downKey = $.ig.Key.prototype.home;
				break;
			case 37:
				downKey = $.ig.Key.prototype.left;
				break;
			case 38:
				downKey = $.ig.Key.prototype.up;
				break;
			case 39:
				downKey = $.ig.Key.prototype.right;
				break;
			case 40:
				downKey = $.ig.Key.prototype.down;
				break;
			case 9:
				downKey = $.ig.Key.prototype.tab;
				break;
			case 32:
				downKey = $.ig.Key.prototype.space;
				break;
			case 13:
				downKey = $.ig.Key.prototype.enter;
				break;
			case 27:
				downKey = $.ig.Key.prototype.escape;
				break;
			case 16:
				downKey = $.ig.Key.prototype.shift;
				break;
			case 17:
				downKey = $.ig.Key.prototype.ctrl;
				break;
			case 18:
				downKey = $.ig.Key.prototype.alt;
				break;
			default:
				if (112 <= e.which && e.which <= 123) {
					downKey = ($.ig.Key.prototype.f1 + e.which - 112);
				}
				break;
		}
		return downKey;
	}
	,
	canvasKeyEvent: function (e, isDown) {
		var downKey = $.ig.BaseDOMEventProxy.prototype.getKey(e);
		var send = downKey != $.ig.Key.prototype.none;
		this.updateModifiers(e);
		if (send && this.shouldInteract()(this.mousePoint())) {
			var handled = false;
			if (isDown) {
				handled = this.raiseOnKeyDown(downKey);
			} else {
				handled = this.raiseOnKeyUp(downKey);
			}
			if (handled) {
				this.preventDefault(e);
			}
		}
	}
	,
	updateModifiers: function (e) {
		this.originalEvent(e);
		this.currentModifiers($.ig.ModifierKeys.prototype.none);
		if (e.shiftKey) {
			this.currentModifiers(this.currentModifiers() | $.ig.ModifierKeys.prototype.shift);
		}
		if (e.altKey) {
			this.currentModifiers(this.currentModifiers() | $.ig.ModifierKeys.prototype.alt);
		}
		if (e.ctrlKey) {
			this.currentModifiers(this.currentModifiers() | $.ig.ModifierKeys.prototype.control);
		}
		this.rightButton(e.button == 2);
	}
	,
	getSourceOffsets: function () {
		var offset = $.ig.BaseDOMEventProxy.prototype.getOffset(this.eventSource());
		var x = offset.left();
		var y = offset.top();
		return { __x: x, __y: y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
	}
	,
	$type: new $.ig.Type('BaseDOMEventProxy', $.ig.EventProxy.prototype.$type)
}, true);

$.ig.util.defType('DOMEventProxy', 'BaseDOMEventProxy', {
	__proxyID: 0,
	__eventNS: null,
	__pinching: false,
	_mGesture: null,
	mGesture: function (value) {
		if (arguments.length === 1) {
			this._mGesture = value;
			return value;
		} else {
			return this._mGesture;
		}
	}
	,
	init: function (DOMEventSource) {
		this.__proxyID = 0;
		this.__eventNS = "";
		this.__pinching = false;
		this.__lastScale = 1;
		this.__holdId = $.ig.BaseDOMEventProxy.prototype.nullTimer;
		this.__tapArea = null;
		this.__holdStartX = 0;
		this.__holdStartY = 0;
		this.__holdCancelDistance = 5;
		this.__dragging = false;
		this.__dragStart = null;
		$.ig.BaseDOMEventProxy.prototype.init.call(this);
		this.shouldInteractForDirection(function (p) { return true; });
		this.deferredTouchStartMode(false);
		$.ig.DOMEventProxy.prototype.__proxyCount++;
		this.__proxyID = $.ig.DOMEventProxy.prototype.__proxyCount;
		this.__eventNS = ".DOMProxy" + this.__proxyID.toString();
		this.currentModifiers($.ig.ModifierKeys.prototype.none);
		this.eventSource(DOMEventSource);
		$.ig.BaseDOMEventProxy.prototype.mSPointerEnabled = false;
		try {
			$.ig.BaseDOMEventProxy.prototype.mSPointerEnabled = window.navigator.msPointerEnabled && MSGesture !== undefined;
		}
		catch (e) {
		}
		$.ig.BaseDOMEventProxy.prototype.pointerEnabled = false;
		try {
			$.ig.BaseDOMEventProxy.prototype.pointerEnabled = window.navigator.pointerEnabled;
		}
		catch (e1) {
		}
		$.ig.BaseDOMEventProxy.prototype.tridentVersion = this.getTridentVersion();
		$.ig.BaseDOMEventProxy.prototype.edgeVersion = this.getEdgeVersion();
		this.bindToSource(this.eventSource(), "");
		this.shouldInteract(function (p) { return true; });
	},
	bindToSource: function (objSource, sourceID) {
		var source = objSource;
		var ns = this.__eventNS + sourceID;
		if ((!$.ig.BaseDOMEventProxy.prototype.mSPointerEnabled && !$.ig.BaseDOMEventProxy.prototype.pointerEnabled)) {
			source.bind("mousemove" + ns, this.canvasMouseMove.runOn(this));
			source.bind("mouseleave" + ns, this.canvasMouseLeave.runOn(this));
			source.bind("mousedown" + ns, this.canvasMouseDown.runOn(this));
			source.bind("mouseup" + ns, this.canvasMouseUp.runOn(this));
			$(window).bind("mouseup" + ns, this.windowMouseUp.runOn(this));
		}
		source.bind("keydown" + ns, this.canvasKeyDown.runOn(this));
		source.bind("keyup" + ns, this.canvasKeyUp.runOn(this));
		var source_ = this.eventSource()[0];
		if (!this.isInteractionDisabled()) {
			$.ig.BaseDOMEventProxy.prototype.grabTouches(source_);
		}
		if ($.ig.BaseDOMEventProxy.prototype.mSPointerEnabled || $.ig.BaseDOMEventProxy.prototype.pointerEnabled) {
			var container = this.eventSource()[0];
			if (window.MSGesture !== undefined) {
				var gesture = new MSGesture();
				gesture.target = container;
				this.mGesture(gesture);
				source.bind("MSGestureStart" + ns, this.canvasGestureStart.runOn(this));
				source.bind("MSGestureChange" + ns, this.canvasGestureChange.runOn(this));
				source.bind("MSGestureEnd" + ns, this.canvasGestureEnd.runOn(this));
			}
			if ($.ig.BaseDOMEventProxy.prototype.pointerEnabled) {
				source.bind("pointerdown" + ns, this.canvasMSPointerDown.runOn(this));
				source.bind("pointerup" + ns, this.canvasMSPointerUp.runOn(this));
				source.bind("pointercancel" + ns, this.canvasMSPointerCancel.runOn(this));
				source.bind("pointermove" + ns, this.canvasMSPointerMove.runOn(this));
				source.bind("pointerout" + ns, this.canvasMSPointerOut.runOn(this));
				source.bind("lostpointercapture" + ns, this.canvasMSLostPointerCapture.runOn(this));
			} else {
				source.bind("MSPointerDown" + ns, this.canvasMSPointerDown.runOn(this));
				source.bind("MSPointerUp" + ns, this.canvasMSPointerUp.runOn(this));
				source.bind("MSPointerCancel" + ns, this.canvasMSPointerCancel.runOn(this));
				source.bind("MSPointerMove" + ns, this.canvasMSPointerMove.runOn(this));
				source.bind("MSPointerOut" + ns, this.canvasMSPointerOut.runOn(this));
				source.bind("MSLostPointerCapture" + ns, this.canvasMSLostPointerCapture.runOn(this));
			}
		}
		source.bind("DOMMouseScroll" + ns, this.canvasMouseScroll.runOn(this));
		source.bind("mousewheel" + ns, this.canvasMouseScroll.runOn(this));
		source.bind("gesturestart" + ns, this.canvasGestureStart.runOn(this));
		source.bind("gesturechange" + ns, this.canvasGestureChange.runOn(this));
		source.bind("gestureend" + ns, this.canvasGestureEnd.runOn(this));
		source.bind("touchstart" + ns, this.canvasTouchStart.runOn(this));
		source.bind("touchmove" + ns, this.canvasTouchMove.runOn(this));
		source.bind("touchend" + ns, this.canvasTouchEnd.runOn(this));
	}
	,
	__lastScale: 0,
	canvasMSPointerUp: function (e_) {
		var isFinger = this.isFinger(e_);
		if (isFinger) {
			this.numTouches(this.numTouches() - 1);
			if (this.numTouches() < 0) {
				this.numTouches(0);
			}
		}
		if (this.numTouches() < 2 && this.__pinching && isFinger) {
			this.__pinching = false;
			var scale = e_.originalEvent.scale;
			this.raiseOnGestureCompleted(this.containerMousePoint(), scale);
		} else {
			if (isFinger) {
				this.canvasTouchEnd(e_);
			} else {
				this.canvasMouseUp(e_);
			}
		}
	}
	,
	canvasMSLostPointerCapture: function (e_) {
		var isFinger = this.isFinger(e_);
		if (isFinger) {
			(function ($obj) { var $value = $obj.numTouches(); $obj.numTouches($value - 1); return $value; }(this));
			if (this.numTouches() < 0) {
				this.numTouches(0);
			}
		}
		if (this.numTouches() < 2 && this.__pinching && isFinger) {
			this.__pinching = false;
			var scale = e_.originalEvent.scale;
			this.raiseOnGestureCompleted(this.containerMousePoint(), scale);
		}
	}
	,
	canvasMSPointerCancel: function (e_) {
		var isFinger = this.isFinger(e_);
		if (isFinger) {
			(function ($obj) { var $value = $obj.numTouches(); $obj.numTouches($value - 1); return $value; }(this));
			if (this.numTouches() < 0) {
				this.numTouches(0);
			}
		}
		if (this.numTouches() < 2 && this.__pinching && isFinger) {
			this.__pinching = false;
			var scale = e_.originalEvent.scale;
			this.raiseOnGestureCompleted(this.containerMousePoint(), scale);
		}
	}
	,
	canvasMSPointerDown: function (e_) {
		var isFinger = this.isFinger(e_);
		if (this.mGesture() != null && isFinger) {
			(function ($obj) { var $value = $obj.numTouches(); $obj.numTouches($value + 1); return $value; }(this));
			this.mGesture().addPointer(e_.originalEvent.pointerId);
		}
		var eventSource_ = this.eventSource()[0];
		if ($.ig.BaseDOMEventProxy.prototype.pointerEnabled) {
			eventSource_.setPointerCapture(e_.originalEvent.pointerId);
		} else {
			eventSource_.msSetPointerCapture(e_.originalEvent.pointerId);
		}
		if (this.numTouches() > 1 && !this.__pinching && isFinger) {
			this.__pinching = true;
			this.updateTouchPosition(e_);
			var scale = e_.originalEvent.scale;
			this.raiseOnPinchStarted(this.containerMousePoint(), scale);
		} else {
			if (isFinger) {
				this.canvasTouchStart(e_);
			} else {
				this.canvasMouseDown(e_);
			}
		}
	}
	,
	isFinger: function (e_) {
		var pointerEvent_ = e_.originalEvent;
		var isFinger = false;
		if ($.ig.BaseDOMEventProxy.prototype.pointerEnabled) {
			isFinger = pointerEvent_.pointerType == 'touch';
		} else {
			isFinger = pointerEvent_.pointerType == pointerEvent_.MSPOINTER_TYPE_TOUCH;
		}
		return isFinger;
	}
	,
	canvasMSPointerMove: function (e_) {
		var isFinger = this.isFinger(e_);
		if (this.__pinching) {
			return;
		}
		if (isFinger) {
			this.canvasTouchMove(e_);
		} else {
			this.canvasMouseMove(e_);
		}
	}
	,
	canvasMSPointerOut: function (e_) {
		var isFinger = this.isFinger(e_);
		if (this.__pinching) {
			return;
		}
		if (isFinger) {
		} else {
			this.canvasMouseLeave(e_);
		}
	}
	,
	canvasGestureStart: function (e_) {
		this.mouseIsOver(true);
		this.updateModifiers(e_);
		this.updateTouchPosition(e_);
		this.dragStopHoldTimer();
		if (this.shouldInteract()(this.mousePoint())) {
			this.preventDefault(e_);
			if ($.ig.BaseDOMEventProxy.prototype.mSPointerEnabled || $.ig.BaseDOMEventProxy.prototype.pointerEnabled) {
				this.__lastScale = 1;
			}
			if (($.ig.BaseDOMEventProxy.prototype.mSPointerEnabled || $.ig.BaseDOMEventProxy.prototype.pointerEnabled) && this.numTouches() < 2) {
				return;
			}
			var scale = e_.originalEvent.scale;
			this.raiseOnPinchStarted(this.containerMousePoint(), scale);
		}
	}
	,
	canvasGestureChange: function (e_) {
		this.mouseIsOver(true);
		this.updateModifiers(e_);
		this.updateTouchPosition(e_);
		if (this.shouldInteract()(this.mousePoint())) {
			this.preventDefault(e_);
			if (($.ig.BaseDOMEventProxy.prototype.mSPointerEnabled || $.ig.BaseDOMEventProxy.prototype.pointerEnabled) && this.numTouches() < 2) {
				return;
			}
			var scale = e_.originalEvent.scale;
			this.raiseOnPinchDelta(this.containerMousePoint(), scale);
		}
	}
	,
	canvasGestureEnd: function (e_) {
		this.preventDefault(e_);
		var scale = e_.originalEvent.scale;
		this.mouseIsOver(false);
		this.updateModifiers(e_);
		this.updateTouchPosition(e_);
		if ((!$.ig.BaseDOMEventProxy.prototype.mSPointerEnabled && !$.ig.BaseDOMEventProxy.prototype.pointerEnabled) || this.__pinching) {
			this.raiseOnGestureCompleted(this.containerMousePoint(), scale);
		}
	}
	,
	__touchContainerStart: null,
	canvasTouchStart: function (e_) {
		this.mouseIsOver(true);
		this.updateModifiers(e_);
		this.updateTouchPosition(e_);
		this.__touchContainerStart = this.containerMousePoint();
		if (this.shouldInteract()(this.mousePoint())) {
			if (!this.deferredTouchStartMode()) {
				this.preventDefault(e_);
			} else {
				this.touchStartDeferred(true);
			}
			this.raiseOnMouseOver(this.mousePoint(), false, true);
			this.raiseOnMouseDown(this.mousePoint());
			this.raiseOnContactStarted(this.containerMousePoint(), true);
			this.startHoldTimer();
		}
	}
	,
	__holdId: 0,
	__tapArea: null,
	__holdStartX: 0,
	__holdStartY: 0,
	__holdCancelDistance: 0,
	startHoldTimer: function () {
		if (this.__holdId == $.ig.BaseDOMEventProxy.prototype.nullTimer) {
			this.__holdStartX = this.mousePoint().__x;
			this.__holdStartY = this.mousePoint().__y;
			this.__holdId = window.setTimeout(this.onHoldTimer.runOn(this), 1500);
		}
	}
	,
	dragStopHoldTimer: function () {
		if (Math.abs(this.__holdStartX - this.mousePoint().__x) > this.__holdCancelDistance || Math.abs(this.__holdStartY - this.mousePoint().__y) > this.__holdCancelDistance) {
			this.stopHoldTimer();
		}
	}
	,
	stopHoldTimer: function () {
		if (this.__holdId != $.ig.BaseDOMEventProxy.prototype.nullTimer) {
			window.clearTimeout(this.__holdId);
			this.__holdId = $.ig.BaseDOMEventProxy.prototype.nullTimer;
		}
	}
	,
	onHoldTimer: function () {
		this.__holdId = $.ig.BaseDOMEventProxy.prototype.nullTimer;
		if (this.touchStartDeferred()) {
			this.touchStartDeferred(false);
			this.beginMouseSuppress();
		}
		this.raiseOnHold(this.containerMousePoint());
	}
	,
	__dragging: false,
	__dragStart: null,
	canvasTouchMove: function (e) {
		this.mouseIsOver(true);
		this.updateModifiers(e);
		this.updateTouchPosition(e);
		if (this.touchStartDeferred()) {
			if (this.shouldInteract()(this.mousePoint()) && this.shouldAllowTouchDrag()) {
				this.touchStartDeferred(false);
				this.beginMouseSuppress();
			}
		}
		this.dragStopHoldTimer();
		this.stopTapTimer();
		if (this.shouldInteract()(this.mousePoint()) && this.numTouches() == 1 && !this.touchStartDeferred()) {
			this.preventDefault(e);
			if (!this.__dragging) {
				this.__dragging = true;
				this.__dragStart = this.containerMousePoint();
				this.raiseOnDragStarted(this.__dragStart);
			} else {
				this.raiseOnMouseOver(this.mousePoint(), true, true);
				this.raiseOnContactMoved(this.containerMousePoint(), true);
				this.raiseOnDragDelta(this.containerMousePoint());
			}
		}
	}
	,
	shouldAllowTouchDrag: function () {
		var direction = { __x: this.__touchContainerStart.__x - this.containerMousePoint().__x, __y: this.__touchContainerStart.__y - this.containerMousePoint().__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		if (Math.abs(direction.__x) < 2 && Math.abs(direction.__y) < 2) {
			return false;
		}
		if (Math.abs(direction.__x) > Math.abs(direction.__y)) {
			direction.__y = 0;
		}
		if (Math.abs(direction.__y) > Math.abs(direction.__x)) {
			direction.__x = 0;
		}
		return this.shouldInteractForDirection()(direction);
	}
	,
	canvasTouchEnd: function (e) {
		this.mouseIsOver(false);
		this.updateModifiers(e);
		this.updateTouchPosition(e);
		this.stopHoldTimer();
		if (this.touchStartDeferred()) {
			this.touchStartDeferred(false);
			this.beginMouseSuppress();
		}
		this.preventDefault(e);
		this.raiseOnMouseOver(this.mousePoint(), false, true);
		this.raiseOnMouseUp(this.mousePoint());
		if (this.numTouches() == 0) {
			this.raiseDoubleTap(this.mousePoint());
			this.endMouseSuppress();
		}
		if (this.__dragging && this.numTouches() == 0) {
			this.__dragging = false;
			this.__dragStart = null;
			this.raiseOnDragCompleted(this.containerMousePoint());
			this.endMouseSuppress();
		}
		this.raiseOnContactCompleted(this.containerMousePoint(), true);
	}
	,
	raiseDoubleTap: function (pt) {
		if ($.ig.Rect.prototype.l_op_Equality(this.__tapArea, null)) {
			this.__tapArea = new $.ig.Rect(0, pt.__x - 50, pt.__y - 50, 100, 100);
			window.setTimeout(this.stopTapTimer.runOn(this), 500);
		} else {
			if (pt.__x >= this.__tapArea.x() && pt.__x <= this.__tapArea.right() && pt.__y >= this.__tapArea.y() && pt.__y <= this.__tapArea.bottom()) {
				this.stopTapTimer();
				this.raiseOnDoubleTap(pt);
			}
		}
	}
	,
	stopTapTimer: function () {
		this.__tapArea = null;
	}
	,
	unbindFromSource: function (objSource, sourceID) {
		var source = objSource;
		var ns = this.__eventNS + sourceID;
		source.unbind(ns);
		if ((!$.ig.BaseDOMEventProxy.prototype.mSPointerEnabled && !$.ig.BaseDOMEventProxy.prototype.pointerEnabled)) {
			$(window).unbind(ns);
		}
	}
	,
	destroy: function () {
		if (this.eventSource() == null) {
			return;
		}
		this.eventSource().unbind(this.__eventNS);
		if ((!$.ig.BaseDOMEventProxy.prototype.mSPointerEnabled && !$.ig.BaseDOMEventProxy.prototype.pointerEnabled)) {
			$(window).unbind(this.__eventNS);
		}
		this.eventSource(null);
	}
	,
	$type: new $.ig.Type('DOMEventProxy', $.ig.BaseDOMEventProxy.prototype.$type)
}, true);

$.ig.util.defType('BrushUtil', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	getLightened: function (brush, interpolation) {
		if (brush == null) {
			return brush;
		}
		if (brush._isGradient) {
			var newBrush = (brush).clone();
			for (var i = 0; i < newBrush._gradientStops.length; i++) {
				var currentStop = newBrush._gradientStops[i];
				currentStop.color($.ig.ColorUtil.prototype.getLightened(currentStop.color(), interpolation));
			}
			return newBrush;
		} else {
			var l = $.ig.ColorUtil.prototype.getLightened(brush.color(), interpolation);
			return (function () {
				var $ret = new $.ig.Brush();
				$ret.color(l);
				return $ret;
			}());
		}
	}
	,
	getInterpolation: function (minimum, interpolation, maximum, interpolationMode) {
		var target = new $.ig.Brush();
		if (minimum == null && maximum == null) {
			target.__fill = "transparent";
			return target;
		}
		var minSolid = null, maxSolid = null;
		var minLinear = null, maxLinear = null;
		if (minimum == null) {
			var c = maximum._isGradient ? (function () {
				var $ret = new $.ig.Color();
				$ret.a(0);
				$ret.r(255);
				$ret.g(255);
				$ret.b(255);
				return $ret;
			}()) : (function () {
				var $ret = new $.ig.Color();
				$ret.a(0);
				$ret.r(maximum.color().r());
				$ret.g(maximum.color().g());
				$ret.b(maximum.color().b());
				return $ret;
			}());
			minSolid = (function () {
				var $ret = new $.ig.Brush();
				$ret.color(c);
				return $ret;
			}());
		} else {
			if (minimum._isGradient) {
				minLinear = minimum;
			} else {
				minSolid = minimum;
			}
		}
		if (maximum == null) {
			var c1 = minimum._isGradient ? (function () {
				var $ret = new $.ig.Color();
				$ret.a(0);
				$ret.r(255);
				$ret.g(255);
				$ret.b(255);
				return $ret;
			}()) : (function () {
				var $ret = new $.ig.Color();
				$ret.a(0);
				$ret.r(minimum.color().r());
				$ret.g(minimum.color().g());
				$ret.b(minimum.color().b());
				return $ret;
			}());
			maxSolid = (function () {
				var $ret = new $.ig.Brush();
				$ret.color(c1);
				return $ret;
			}());
		} else {
			if (maximum._isGradient) {
				maxLinear = maximum;
			} else {
				maxSolid = maximum;
			}
		}
		if (minSolid != null && maxSolid != null) {
			return $.ig.BrushUtil.prototype.solidSolid(minSolid, interpolation, maxSolid, interpolationMode);
		}
		if (minSolid != null && maxLinear != null) {
			return $.ig.BrushUtil.prototype.solidLinear(minSolid, interpolation, maxLinear, interpolationMode);
		}
		if (minLinear != null && maxSolid != null) {
			return $.ig.BrushUtil.prototype.solidLinear(maxSolid, 1 - interpolation, minLinear, interpolationMode);
		}
		if (minLinear != null && maxLinear != null) {
			return $.ig.BrushUtil.prototype.linearLinear(minLinear, interpolation, maxLinear, interpolationMode);
		}
		return target;
	}
	,
	solidSolid: function (min, p, max, interpolationMode) {
		var b = new $.ig.Brush();
		b.color($.ig.ColorUtil.prototype.getInterpolation(min.color(), p, max.color(), interpolationMode));
		return b;
	}
	,
	solidLinear: function (min, p, max, interpolationMode) {
		var b = new $.ig.LinearGradientBrush();
		b._gradientStops = $.ig.BrushUtil.prototype.gradientStops1(min.color(), p, max._gradientStops, interpolationMode);
		if (max._useCustomDirection) {
			b._useCustomDirection = true;
			b._startX = max._startX;
			b._startY = max._startY;
			b._endX = max._endX;
			b._endY = max._endY;
		}
		return b;
	}
	,
	linearLinear: function (min, p, max, interpolationMode) {
		var b = new $.ig.LinearGradientBrush();
		b._gradientStops = $.ig.BrushUtil.prototype.gradientStops(min._gradientStops, p, max._gradientStops, interpolationMode);
		if (min._useCustomDirection || max._useCustomDirection) {
			b._useCustomDirection = true;
			b._startX = min._startX + p * (max._startX - min._startX);
			b._startY = min._startY + p * (max._startY - min._startY);
			b._endX = (1 - p) * min._endX + p * max._endX;
			b._endY = (1 - p) * min._endY + p * max._endY;
		}
		return b;
	}
	,
	gradientStops1: function (min, p, max, interpolationMode) {
		var gradientStopCollection = new Array(max.length);
		for (var i = 0; i < max.length; ++i) {
			gradientStopCollection[i] = (function () {
				var $ret = new $.ig.GradientStop();
				$ret._offset = max[i]._offset;
				$ret.color($.ig.ColorUtil.prototype.getInterpolation(min, p, max[i].color(), interpolationMode));
				return $ret;
			}());
		}
		return gradientStopCollection;
	}
	,
	gradientStops: function (min, p, max, interpolationMode) {
		var minimumCount = Math.min(min.length, max.length);
		var maxCount = Math.max(min.length, max.length);
		var gradientStopCollection = new Array(maxCount);
		var i = 0;
		for (; i < minimumCount; ++i) {
			gradientStopCollection[i] = (function () {
				var $ret = new $.ig.GradientStop();
				$ret._offset = (1 - p) * min[i]._offset + p * max[i]._offset;
				$ret.color($.ig.ColorUtil.prototype.getInterpolation(min[i].color(), p, max[i].color(), interpolationMode));
				return $ret;
			}());
		}
		for (; i < min.length; ++i) {
			gradientStopCollection[i] = (function () {
				var $ret = new $.ig.GradientStop();
				$ret._offset = (1 - p) * min[i]._offset + p * max[max.length - 1]._offset;
				$ret.color($.ig.ColorUtil.prototype.getInterpolation(min[i].color(), p, max[max.length - 1].color(), interpolationMode));
				return $ret;
			}());
		}
		for (; i < max.length; ++i) {
			gradientStopCollection[i] = (function () {
				var $ret = new $.ig.GradientStop();
				$ret._offset = (1 - p) * min[min.length - 1]._offset + p * max[i]._offset;
				$ret.color($.ig.ColorUtil.prototype.getInterpolation(min[min.length - 1].color(), p, max[i].color(), interpolationMode));
				return $ret;
			}());
		}
		return gradientStopCollection;
	}
	,
	isFullyTransparent: function (brush) {
		var linearBrush = $.ig.util.cast($.ig.LinearGradientBrush.prototype.$type, brush);
		if (linearBrush != null) {
			return $.ig.BrushUtil.prototype.isFullyTransparent2(linearBrush);
		}
		if (brush.color().a() == 0) {
			return true;
		}
		return false;
	}
	,
	isFullyTransparent2: function (brush) {
		var $t = brush._gradientStops;
		for (var i = 0; i < $t.length; i++) {
			var stop = $t[i];
			if (!$.ig.BrushUtil.prototype.isFullyTransparent1(stop)) {
				return false;
			}
		}
		return true;
	}
	,
	isFullyTransparent1: function (gradientStop) {
		if (gradientStop.color().a() == 0) {
			return true;
		}
		return false;
	}
	,
	getCssBrushColors: function (className, obj) {
		var brushes = new Array(2);
		obj.addClass(className);
		var fill = new $.ig.Brush();
		fill.__fill = obj.css("background-color");
		var outline = new $.ig.Brush();
		outline.__fill = obj.css("border-top-color");
		obj.removeClass(className);
		brushes[0] = fill;
		brushes[1] = outline;
		return brushes;
	}
	,
	getBrushCollection: function (palleteName_, container_, brushes, outlines, defaultColors) {
		brushes = new $.ig.BrushCollection();
		outlines = new $.ig.BrushCollection();
		var tempBrush;
		var names = new $.ig.List$1(String, 0);
		names.add("background-color");
		names.add("border-top-color");
		var discovery = $.ig.CssHelper.prototype.getDisoveryElement();
		var isNotInDom = false;
		if (container_ != null) {
			container_.append(discovery);
			isNotInDom = !jQuery.contains(document.documentElement, container_[0]);
			if (isNotInDom) {
				$("body").append(container_);
			}
		} else {
			$("body").append(discovery);
		}
		var palette = $.ig.CssHelper.prototype.getValuesForClassCollection(discovery, "ui-" + palleteName_ + "-palette-", names);
		var numPaletteColors = palette.count();
		if (numPaletteColors == 0) {
			if (defaultColors == null) {
				defaultColors = [ "#B1BFC9", "#50a8be", "#798995", "#fc6754", "#4F606C", "#fec33c", "#374650", "#3c6399", "#162C3B", "#91af49" ];
			}
			for (var i = 0; i < defaultColors.length - 1; i += 2) {
				tempBrush = new $.ig.Brush();
				tempBrush.__fill = defaultColors[i];
				outlines.add(tempBrush);
				tempBrush = new $.ig.Brush();
				tempBrush.__fill = defaultColors[i + 1];
				brushes.add(tempBrush);
			}
		}
		for (var i1 = 0; i1 < numPaletteColors; i1++) {
			var fillBrush = new $.ig.Brush();
			fillBrush.__fill = palette.__inner[i1].__inner[0];
			var outlineBrush = new $.ig.Brush();
			outlineBrush.__fill = palette.__inner[i1].__inner[1];
			brushes.add(fillBrush);
			outlines.add(outlineBrush);
		}
		discovery.remove();
		if (isNotInDom) {
			container_.remove();
		}
		return {
			p2: brushes,
			p3: outlines
		};
	}
	,
	getGradientBrushCollection: function (fillGradientPaletteName, outlineGradientPaletteName, paletteName, container_, brushes, outlines, defaultColors) {
		brushes = new $.ig.BrushCollection();
		outlines = new $.ig.BrushCollection();
		if (defaultColors == null) {
			defaultColors = [ "#B1BFC9", "#50a8be", "#798995", "#fc6754", "#4F606C", "#fec33c", "#374650", "#3c6399", "#162C3B", "#91af49" ];
		}
		var discovery = $.ig.CssHelper.prototype.getDisoveryElement();
		var isNotInDom = false;
		if (container_ != null) {
			container_.append(discovery);
			isNotInDom = !jQuery.contains(document.documentElement, container_[0]);
			if (isNotInDom) {
				$("body").append(container_);
			}
		} else {
			$("body").append(discovery);
		}
		var names = new $.ig.List$1(String, 0);
		names.add("background-image");
		var fillsPalette = $.ig.CssHelper.prototype.getValuesForClassCollection(discovery, fillGradientPaletteName, names);
		var numFillsPaletteColors = fillsPalette.count();
		var outlinesPalette = $.ig.CssHelper.prototype.getValuesForClassCollection(discovery, outlineGradientPaletteName, names);
		var numOutlinesPaletteColors = outlinesPalette.count();
		for (var i = 0; i < numFillsPaletteColors; i++) {
			brushes.add($.ig.CssGradientUtil.prototype.brushFromGradientString(fillsPalette.__inner[i].__inner[0]));
		}
		for (var i1 = 0; i1 < numOutlinesPaletteColors; i1++) {
			outlines.add($.ig.CssGradientUtil.prototype.brushFromGradientString(outlinesPalette.__inner[i1].__inner[0]));
		}
		names.clear();
		var fillIndex = 0;
		var outlineIndex = 0;
		var numPaletteColors = Math.min(numFillsPaletteColors, numOutlinesPaletteColors);
		var palette = null;
		if (numFillsPaletteColors == 0) {
			names.add("background-color");
		}
		if (numOutlinesPaletteColors == 0) {
			names.add("border-top-color");
			outlineIndex = numFillsPaletteColors == 0 ? 1 : 0;
		}
		if (names.count() > 0) {
			palette = $.ig.CssHelper.prototype.getValuesForClassCollection(discovery, paletteName, names);
			numPaletteColors = palette.count();
		}
		if (numFillsPaletteColors == 0) {
			if (numPaletteColors > 0) {
				for (var i2 = 0; i2 < numPaletteColors; i2++) {
					var fillBrush = new $.ig.Brush();
					fillBrush.__fill = palette.__inner[i2].__inner[fillIndex];
					brushes.add(fillBrush);
				}
			} else {
				for (var i3 = 0; i3 < defaultColors.length - 1; i3 += 2) {
					var fillBrush1 = new $.ig.Brush();
					fillBrush1 = new $.ig.Brush();
					fillBrush1.__fill = defaultColors[i3 + 1];
					brushes.add(fillBrush1);
				}
			}
		}
		if (numOutlinesPaletteColors == 0) {
			if (numPaletteColors > 0) {
				for (var i4 = 0; i4 < numPaletteColors; i4++) {
					var outlineBrush = new $.ig.Brush();
					outlineBrush.__fill = palette.__inner[i4].__inner[outlineIndex];
					outlines.add(outlineBrush);
				}
			} else {
				for (var i5 = 0; i5 < defaultColors.length - 1; i5 += 2) {
					var outlineBrush1 = new $.ig.Brush();
					outlineBrush1.__fill = defaultColors[i5];
					outlines.add(outlineBrush1);
				}
			}
		}
		discovery.remove();
		if (isNotInDom) {
			container_.remove();
		}
		return {
			p4: brushes,
			p5: outlines
		};
	}
	,
	getGradientBrush: function (gradientClassName, className, cssProp, container_, fallbackColor) {
		var b = null;
		var discovery = $.ig.CssHelper.prototype.getDisoveryElement();
		var isNotInDom = false;
		if (container_ != null) {
			container_.append(discovery);
			isNotInDom = !jQuery.contains(document.documentElement, container_[0]);
			if (isNotInDom) {
				$("body").append(container_);
			}
		} else {
			$("body").append(discovery);
		}
		var gradientString = $.ig.CssHelper.prototype.getPropertyValue(discovery, gradientClassName, "background-image");
		if (gradientString != null) {
			b = $.ig.CssGradientUtil.prototype.brushFromGradientString(gradientString);
		}
		if (b == null) {
			b = new $.ig.Brush();
			var fillColor = $.ig.CssHelper.prototype.getPropertyValue(discovery, className, cssProp);
			b.__fill = fillColor != null ? fillColor : fallbackColor;
		}
		discovery.remove();
		if (isNotInDom) {
			container_.remove();
		}
		return b;
	}
	,
	$type: new $.ig.Type('BrushUtil', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('ColorUtil', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	randomColor: function (alpha) {
		return $.ig.Color.prototype.fromArgb(alpha, $.ig.ColorUtil.prototype._r.next2(0, 255), $.ig.ColorUtil.prototype._r.next2(0, 255), $.ig.ColorUtil.prototype._r.next2(0, 255));
	}
	,
	randomHue: function (color) {
		var ahsv = $.ig.ColorUtil.prototype.getAHSV(color);
		return $.ig.ColorUtil.prototype.fromAHSV(ahsv[0], $.ig.ColorUtil.prototype._r.next2(0, 359), ahsv[2], ahsv[3]);
	}
	,
	getInterpolation: function (minimum, interpolation_, maximum_, interpolationMode) {
		var min_ = minimum;
		switch (interpolationMode) {
			case $.ig.InterpolationMode.prototype.hSV:
				{
					var b = $.ig.ColorUtil.prototype.getAHSV(minimum);
					var e = $.ig.ColorUtil.prototype.getAHSV(maximum_);
					var b1 = b[1] >= 0 ? b[1] : e[1];
					var e1 = e[1] >= 0 ? e[1] : b[1];
					if (b1 >= 0 && e1 >= 0 && Math.abs(e1 - b1) > 180) {
						if (e1 > b1) {
							b1 += 360;
						} else {
							e1 += 360;
						}
					}
					interpolation_ = Math.max(0, Math.min(1, interpolation_));
					return $.ig.ColorUtil.prototype.fromAHSV(b[0] + interpolation_ * (e[0] - b[0]), b1 + interpolation_ * (e1 - b1), b[2] + interpolation_ * (e[2] - b[2]), b[3] + interpolation_ * (e[3] - b[3]));
				}
			case $.ig.InterpolationMode.prototype.rGB: return $.ig.Color.prototype.fromArgb(min_.__a + interpolation_ * (maximum_.__a - min_.__a), min_.__r + interpolation_ * (maximum_.__r - min_.__r), min_.__g + interpolation_ * (maximum_.__g - min_.__g), min_.__b + interpolation_ * (maximum_.__b - min_.__b));
		}
		return minimum;
	}
	,
	getAHSVInterpolation: function (minimum, interpolation, maximum) {
		var b1 = minimum[1] >= 0 ? minimum[1] : maximum[1];
		var e1 = maximum[1] >= 0 ? maximum[1] : minimum[1];
		if (b1 >= 0 && e1 >= 0 && Math.abs(e1 - b1) > 180) {
			if (e1 > b1) {
				b1 += 360;
			} else {
				e1 += 360;
			}
		}
		interpolation = Math.max(0, Math.min(1, interpolation));
		return $.ig.ColorUtil.prototype.fromAHSV(minimum[0] + interpolation * (maximum[0] - minimum[0]), b1 + interpolation * (e1 - b1), minimum[2] + interpolation * (maximum[2] - minimum[2]), minimum[3] + interpolation * (maximum[3] - minimum[3]));
	}
	,
	getLightened: function (color, interpolation) {
		var ahsl = $.ig.ColorUtil.prototype.getAHSL(color);
		if (interpolation < 0) {
			return $.ig.ColorUtil.prototype.fromAHSL(ahsl[0], ahsl[1], ahsl[2], ahsl[3] * (1 - $.ig.MathUtil.prototype.clamp(-interpolation, 0, 1)));
		} else {
			return $.ig.ColorUtil.prototype.fromAHSL(ahsl[0], ahsl[1], ahsl[2], ahsl[3] + $.ig.MathUtil.prototype.clamp(interpolation, 0, 1) * (1 - ahsl[3]));
		}
	}
	,
	getAHSL: function (color) {
		var ahsl = new Array(4);
		var r = color.r() / 255;
		var g = color.g() / 255;
		var b = color.b() / 255;
		var min = Math.min(Math.min(r, g), b);
		var max = Math.max(Math.max(r, g), b);
		var delta = max - min;
		ahsl[0] = color.a() / 255;
		ahsl[3] = (max + min) / 2;
		if (delta == 0) {
			ahsl[1] = -1;
			ahsl[2] = 0;
		} else {
			ahsl[1] = $.ig.ColorUtil.prototype.h(max, delta, r, g, b);
			ahsl[2] = ahsl[3] < 0.5 ? delta / (max + min) : delta / (2 - max - min);
		}
		return ahsl;
	}
	,
	getAHSV: function (color) {
		var a = color.a() / 255;
		var r = color.r() / 255;
		var g = color.g() / 255;
		var b = color.b() / 255;
		var min = Math.min(r, Math.min(g, b));
		var max = Math.max(r, Math.max(g, b));
		var delta = max - min;
		var ahsv = new Array(4);
		ahsv[0] = a;
		ahsv[3] = max;
		if (delta == 0) {
			ahsv[1] = -1;
			ahsv[2] = 0;
		} else {
			ahsv[1] = $.ig.ColorUtil.prototype.h(max, delta, r, g, b);
			ahsv[2] = delta / max;
		}
		return ahsv;
	}
	,
	fromAHSL: function (alpha, hue, saturation, lightness) {
		var r;
		var g;
		var b;
		if (saturation == 0) {
			r = lightness;
			g = lightness;
			b = lightness;
		} else {
			var q = lightness < 0.5 ? lightness * (1 + saturation) : lightness + saturation - (lightness * saturation);
			var p = 2 * lightness - q;
			var hk = hue / 360;
			r = $.ig.ColorUtil.prototype.c(p, q, hk + 1 / 3);
			g = $.ig.ColorUtil.prototype.c(p, q, hk);
			b = $.ig.ColorUtil.prototype.c(p, q, hk - 1 / 3);
		}
		return $.ig.Color.prototype.fromArgb($.ig.truncate((alpha * 255)), $.ig.truncate((r * 255)), $.ig.truncate((g * 255)), $.ig.truncate((b * 255)));
	}
	,
	fromAHSV: function (alpha, hue, saturation, value) {
		var r;
		var g;
		var b;
		while (hue >= 360) {
			hue -= 360;
		}
		if (saturation == 0) {
			r = value;
			g = value;
			b = value;
		} else {
			hue /= 60;
			var i = Math.floor(hue);
			var f = hue - i;
			var p = value * (1 - saturation);
			var q = value * (1 - saturation * f);
			var t = value * (1 - saturation * (1 - f));
			switch ($.ig.truncate(i)) {
				case 0:
					r = value;
					g = t;
					b = p;
					break;
				case 1:
					r = q;
					g = value;
					b = p;
					break;
				case 2:
					r = p;
					g = value;
					b = t;
					break;
				case 3:
					r = p;
					g = q;
					b = value;
					break;
				case 4:
					r = t;
					g = p;
					b = value;
					break;
				default:
					r = value;
					g = p;
					b = q;
					break;
			}
		}
		return $.ig.Color.prototype.fromArgb($.ig.truncate((alpha * 255)), $.ig.truncate((r * 255)), $.ig.truncate((g * 255)), $.ig.truncate((b * 255)));
	}
	,
	h: function (max, delta, r, g, b) {
		var h = r == max ? (g - b) / delta : g == max ? 2 + (b - r) / delta : 4 + (r - g) / delta;
		h *= 60;
		if (h < 0) {
			h += 360;
		}
		return h;
	}
	,
	c: function (p, q, t) {
		t = t < 0 ? t + 1 : t > 1 ? t - 1 : t;
		if (t < 1 / 6) {
			return p + ((q - p) * 6 * t);
		}
		if (t < 1 / 2) {
			return q;
		}
		if (t < 2 / 3) {
			return p + ((q - p) * 6 * (2 / 3 - t));
		}
		return p;
	}
	,
	randomColors: function () {
		if ($.ig.ColorUtil.prototype.__randomColors == null) {
			$.ig.ColorUtil.prototype.__randomColors = new Array(100);
			$.ig.ColorUtil.prototype.__randomColors[0] = $.ig.Color.prototype.fromArgb(255, 70, 130, 180);
			$.ig.ColorUtil.prototype.__randomColors[1] = $.ig.Color.prototype.fromArgb(255, 65, 105, 225);
			$.ig.ColorUtil.prototype.__randomColors[2] = $.ig.Color.prototype.fromArgb(255, 100, 149, 237);
			$.ig.ColorUtil.prototype.__randomColors[3] = $.ig.Color.prototype.fromArgb(255, 176, 196, 222);
			$.ig.ColorUtil.prototype.__randomColors[4] = $.ig.Color.prototype.fromArgb(255, 123, 104, 238);
			$.ig.ColorUtil.prototype.__randomColors[5] = $.ig.Color.prototype.fromArgb(255, 106, 90, 205);
			$.ig.ColorUtil.prototype.__randomColors[6] = $.ig.Color.prototype.fromArgb(255, 72, 61, 139);
			$.ig.ColorUtil.prototype.__randomColors[7] = $.ig.Color.prototype.fromArgb(255, 25, 25, 112);
			for (var colorIndex = 8; colorIndex < 100; colorIndex++) {
				$.ig.ColorUtil.prototype.__randomColors[colorIndex] = $.ig.Color.prototype.fromArgb(255, $.ig.ColorUtil.prototype._r.next1(255), $.ig.ColorUtil.prototype._r.next1(255), $.ig.ColorUtil.prototype._r.next1(255));
			}
		}
		return $.ig.ColorUtil.prototype.__randomColors;
	}
	,
	getRandomColor: function (index) {
		index %= 100;
		return $.ig.ColorUtil.prototype.randomColors()[index];
	}
	,
	colorToInt: function (color) {
		var aa = color.a() / 255;
		var rr = $.ig.truncate((color.r() * aa));
		var gg = $.ig.truncate((color.g() * aa));
		var bb = $.ig.truncate((color.b() * aa));
		return color.a() << 24 | rr << 16 | gg << 8 | bb;
	}
	,
	getColor: function (brush) {
		return brush.color();
	}
	,
	$type: new $.ig.Type('ColorUtil', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('CssHelper', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	getDisoveryElement: function () {
		var discoveryStyle = $("#fakediscoveryelementstyle");
		if (discoveryStyle.length == 0) {
			var styleText = "fakediscoveryelement\n" + "{\n" + "\tdisplay: block;\n" + "   position: absolute;\n" + "   box-sizing: content-box;\n" + "   -moz-box-sizing: content-box;\n" + "\tmargin: " + $.ig.CssHelper.prototype.defaultMarginValue + ";\n" + "\tcolor: " + $.ig.CssHelper.prototype.defaultColorValue + ";\n" + "   border-style: solid;\n" + "   border-color: " + $.ig.CssHelper.prototype.defaultColorValue + ";\n" + "   background-color: " + $.ig.CssHelper.prototype.defaultColorValue + ";\n" + "   background-image: " + $.ig.CssHelper.prototype.defaultBackgroundImageValue + ";\n" + "   border-width: " + $.ig.CssHelper.prototype.defaultWidthHeightValue + ";\n" + "   border-radius: " + $.ig.CssHelper.prototype.defaultWidthHeightValue + ";\n" + "   vertical-align: " + $.ig.CssHelper.prototype.defaultVerticalAlignValue + ";\n" + "   text-align: " + $.ig.CssHelper.prototype.defaultTextAlignValue + ";\n" + "   opacity: " + $.ig.CssHelper.prototype.defaultOpacityValue + ";\n" + "   visibility: " + $.ig.CssHelper.prototype.defaultVisibilityValue + ";\n" + "   width: " + $.ig.CssHelper.prototype.defaultWidthHeightValue + ";\n" + "   height: " + $.ig.CssHelper.prototype.defaultWidthHeightValue + ";\n" + "}\n";
			discoveryStyle = $("<style id='fakediscoveryelementstyle'></style>");
			discoveryStyle.html(styleText);
			$("head").append(discoveryStyle);
		}
		return $("<fakediscoveryelement></fakediscoveryelement>");
	}
	,
	getDefaultValue: function (propertyName) {
		if (propertyName == "color" || propertyName == "border-color" || propertyName == "border-top-color" || propertyName == "border-left-color" || propertyName == "border-right-color" || propertyName == "border-bottom-color" || propertyName == "background-color") {
			return $.ig.CssHelper.prototype.defaultColorValue;
		} else if (propertyName == "margin-left" || propertyName == "margin-top" || propertyName == "margin-right" || propertyName == "margin-bottom") {
			return $.ig.CssHelper.prototype.defaultMarginValue;
		} else if (propertyName == "vertical-align") {
			return $.ig.CssHelper.prototype.defaultVerticalAlignValue;
		} else if (propertyName == "opacity") {
			return $.ig.CssHelper.prototype.defaultOpacityValue;
		} else if (propertyName == "background-image") {
			return $.ig.CssHelper.prototype.defaultBackgroundImageValue;
		} else if (propertyName == "text-align") {
			return $.ig.CssHelper.prototype.defaultTextAlignValue;
		} else if (propertyName == "visibility") {
			return $.ig.CssHelper.prototype.defaultVisibilityValue;
		} else if ($.ig.CssHelper.prototype.isWidthProperty(propertyName)) {
			return $.ig.CssHelper.prototype.defaultWidthHeightValue;
		}
		return "DEFAULT";
	}
	,
	numberOfClasses: function (discoveryElement, classPrefix, propertyName) {
		var defaultValue = $.ig.CssHelper.prototype.getDefaultValue(propertyName);
		var curr = 1;
		var done = false;
		while (!done && curr < $.ig.CssHelper.prototype.maxClasses) {
			var className = classPrefix + curr.toString();
			discoveryElement.addClass(className);
			var propValue = discoveryElement.css(propertyName);
			if (propValue == defaultValue) {
				break;
			}
			curr++;
		}
		return curr;
	}
	,
	getPropertyValue: function (discoveryElement, className, propertyName) {
		discoveryElement.addClass(className);
		var ret_ = discoveryElement.css(propertyName);
		discoveryElement.removeClass(className);
		if (propertyName == "opacity") {
			ret_ = Math.round(1000.0 * ret_) / 1000.0;
		}
		if ($.ig.CssHelper.prototype.isEqualToDefault(ret_, $.ig.CssHelper.prototype.getDefaultValue(propertyName), propertyName)) {
			return null;
		}
		return ret_;
	}
	,
	isEqualToDefault: function (propValue, defaultValue, propertyName) {
		if (propValue == defaultValue) {
			return true;
		}
		if ($.ig.CssHelper.prototype.isWidthProperty(propertyName) && propValue != null && propValue.contains("px")) {
			var num = $.ig.util.replace(propValue, "px", "");
			var val = parseFloat(num);
			if (Math.abs(Math.round(val) - 4321) < 2) {
				return true;
			}
			return false;
		} else if ($.ig.CssHelper.prototype.isMarginProperty(propertyName) && propValue != null && propValue.contains("px")) {
			var num1 = $.ig.util.replace(propValue, "px", "");
			var val1 = parseFloat(num1);
			if (Math.abs(Math.round(val1) + 4321) < 2) {
				return true;
			}
			return false;
		} else {
			return propValue == defaultValue;
		}
	}
	,
	isWidthProperty: function (propertyName) {
		return propertyName == "width" || propertyName == "height" || propertyName == "border-top-width" || propertyName == "border-left-width" || propertyName == "border-right-width" || propertyName == "border-bottom-width" || propertyName == "border-top-left-radius";
	}
	,
	isMarginProperty: function (propertyName) {
		return propertyName == "margin-top" || propertyName == "margin-left" || propertyName == "margin-right" || propertyName == "margin-bottom";
	}
	,
	getValuesForClassCollection: function (discoveryElement, classPrefix, propertyNames) {
		var ret = new $.ig.List$1($.ig.List$1.prototype.$type.specialize(String), 0);
		var curr = 1;
		var done = false;
		while (!done && curr < $.ig.CssHelper.prototype.maxClasses) {
			var className = classPrefix + curr.toString();
			discoveryElement.addClass(className);
			var row = new $.ig.List$1(String, 0);
			for (var i = 0; i < propertyNames.count(); i++) {
				var propertyName = propertyNames.__inner[i];
				var defaultValue = $.ig.CssHelper.prototype.getDefaultValue(propertyName);
				var propValue = discoveryElement.css(propertyName);
				if ($.ig.CssHelper.prototype.isEqualToDefault(propValue, defaultValue, propertyName)) {
					done = true;
					break;
				}
				row.add(propValue);
			}
			discoveryElement.removeClass(className);
			if (!done) {
				ret.add(row);
			}
			curr++;
		}
		return ret;
	}
	,
	$type: new $.ig.Type('CssHelper', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('DoubleAnimator', 'Object', {
	needsFlush: function () {
		return this.transitionProgress() == 0;
	}
	,
	flush: function () {
		this.update(true);
	}
	,
	__transitionProgress: 0,
	transitionProgress: function (value) {
		if (arguments.length === 1) {
			this.__transitionProgress = value;
			if (this.propertyChanged != null) {
				this.propertyChanged(this, new $.ig.PropertyChangedEventArgs("TransitionProgress"));
			}
			return value;
		} else {
			return this.__transitionProgress;
		}
	}
	,
	__intervalMilliseconds: 0,
	intervalMilliseconds: function (value) {
		if (arguments.length === 1) {
			this.__intervalMilliseconds = value;
			return value;
		} else {
			return this.__intervalMilliseconds;
		}
	}
	,
	__easingFunction: null,
	easingFunction: function (value) {
		if (arguments.length === 1) {
			this.__easingFunction = value;
			return value;
		} else {
			return this.__easingFunction;
		}
	}
	,
	__from: 0,
	__to: 0,
	from: function (value) {
		if (arguments.length === 1) {
			this.__from = value;
			return value;
		} else {
			return this.__from;
		}
	}
	,
	to: function (value) {
		if (arguments.length === 1) {
			this.__to = value;
			return value;
		} else {
			return this.__to;
		}
	}
	,
	init: function (from, to, intervalMilliseconds) {
		this.__easingFunction = null;
		this.__from = 0;
		this.__to = 0;
		this.__active = false;
		this.__intervalId = -1;
		this.__lastRender = 0;
		$.ig.Object.prototype.init.call(this);
		this.__from = from;
		this.__to = to;
		this.__intervalMilliseconds = intervalMilliseconds;
		this.requestAnimationFrame(window.requestAnimationFrame ||
            window.webkitRequestAnimationFrame ||
            window.mozRequestAnimationFrame ||
            window.oRequestAnimationFrame ||
            window.msRequestAnimationFrame ||
            function(callback) {
                window.setTimeout(callback, 1000 / 60);
            });
	},
	_requestAnimationFrame: null,
	requestAnimationFrame: function (value) {
		if (arguments.length === 1) {
			this._requestAnimationFrame = value;
			return value;
		} else {
			return this._requestAnimationFrame;
		}
	}
	,
	__active: false,
	active: function (value) {
		if (arguments.length === 1) {
			this.__active = value;
			return value;
		} else {
			return this.__active;
		}
	}
	,
	start: function () {
		this.__transitionProgress = 0;
		this.__lastRender = 0;
		this.__startTime = $.ig.Date.prototype.now();
		if (!this.__active) {
			this.__active = true;
			this.requestAnimationFrame()(this.tick.runOn(this));
		}
	}
	,
	stop: function () {
		this.__active = false;
		this.__transitionProgress = 0;
		this.__lastRender = 0;
	}
	,
	__startTime: new Date(),
	__intervalId: 0,
	__lastRender: 0,
	tick: function () {
		this.update(false);
	}
	,
	update: function (flush) {
		if (!this.__active) {
			this.stop();
			return;
		}
		var currentTime = $.ig.Date.prototype.now();
		var ellapsedMilliseconds = currentTime.getTime() - this.__startTime.getTime();
		if (ellapsedMilliseconds > this.__intervalMilliseconds) {
			ellapsedMilliseconds = this.__intervalMilliseconds;
		}
		if ((ellapsedMilliseconds - this.__lastRender < 16 && ellapsedMilliseconds != this.__intervalMilliseconds) && !flush) {
			this.requestAnimationFrame()(this.tick.runOn(this));
			return;
		}
		this.__lastRender = ellapsedMilliseconds;
		var p = (ellapsedMilliseconds / this.__intervalMilliseconds);
		if (this.__easingFunction != null) {
			p = this.__easingFunction(p);
		}
		var interpolatedValue = this.__from + ((this.__to - this.__from) * p);
		if (!flush) {
			if (ellapsedMilliseconds == this.__intervalMilliseconds) {
				this.stop();
			} else {
				this.requestAnimationFrame()(this.tick.runOn(this));
			}
		}
		this.transitionProgress(interpolatedValue);
	}
	,
	getElapsedMilliseconds: function () {
		return this.__active ? $.ig.Date.prototype.now().getTime() - this.__startTime.getTime() : 0;
	}
	,
	animationActive: function () {
		return this.__active;
	}
	,
	propertyChanged: null,
	$type: new $.ig.Type('DoubleAnimator', $.ig.Object.prototype.$type, [$.ig.INotifyPropertyChanged.prototype.$type])
}, true);

$.ig.util.defType('BrushCollection', 'ObservableCollection$1', {
	init: function () {
		this._interpolationMode = $.ig.InterpolationMode.prototype.rGB;
		$.ig.ObservableCollection$1.prototype.init.call(this, $.ig.Brush.prototype.$type, 0);
	},
	selectRandom: function () {
		return this.item($.ig.BrushCollection.prototype._random.next1(this.count()));
	}
	,
	interpolateRandom: function () {
		return this.getInterpolatedBrush($.ig.BrushCollection.prototype._random.nextDouble() * (this.count() - 1));
	}
	,
	interpolationMode: function (value) {
		if (arguments.length === 1) {
			if (this._interpolationMode != value) {
				this._interpolationMode = value;
				this.onCollectionChanged(new $.ig.NotifyCollectionChangedEventArgs(0, $.ig.NotifyCollectionChangedAction.prototype.reset));
			}
			return value;
		} else {
			return this._interpolationMode;
		}
	}
	,
	_interpolationMode: 0,
	item: function (index, value) {
		if (arguments.length === 2) {
			$.ig.ObservableCollection$1.prototype.item.call(this, index, value);
			return value;
		} else {
			if (index < 0 || index >= this.count()) {
				return null;
			}
			return $.ig.ObservableCollection$1.prototype.item.call(this, index);
		}
	}
	,
	getInterpolatedBrush: function (index) {
		if ($.ig.util.isNaN(index)) {
			return null;
		}
		index = $.ig.MathUtil.prototype.clamp(index, 0, this.count() - 1);
		var i = $.ig.truncate(Math.floor(index));
		if (i == index) {
			return this.item(i);
		}
		return this.interpolateBrushes(index - i, this.item(i), this.item(i + 1), this.interpolationMode());
	}
	,
	interpolateBrushes: function (p, minBrush, maxBrush, InterpolationMode) {
		var minFill = minBrush.color();
		var maxFill = maxBrush.color();
		var interp = $.ig.ColorUtil.prototype.getInterpolation(minFill, p, maxFill, InterpolationMode);
		var b = new $.ig.Brush();
		b.color(interp);
		return b;
	}
	,
	equals: function (obj) {
		if (obj == null) {
			return false;
		}
		var bc = obj;
		if (bc.count() != this.count()) {
			return false;
		}
		for (var i = 0; i < bc.count(); i++) {
			if (!bc.item(i).equals(this.item(i))) {
				return false;
			}
		}
		return true;
	}
	,
	$type: new $.ig.Type('BrushCollection', $.ig.ObservableCollection$1.prototype.$type.specialize($.ig.Brush.prototype.$type))
}, true);

$.ig.util.defType('DoubleValueChangedEventArgs', 'EventArgs', {
	init: function (oldValue, newValue) {
		$.ig.EventArgs.prototype.init.call(this);
		this.oldValue(oldValue);
		this.newValue(newValue);
	},
	_oldValue: 0,
	oldValue: function (value) {
		if (arguments.length === 1) {
			this._oldValue = value;
			return value;
		} else {
			return this._oldValue;
		}
	}
	,
	_newValue: 0,
	newValue: function (value) {
		if (arguments.length === 1) {
			this._newValue = value;
			return value;
		} else {
			return this._newValue;
		}
	}
	,
	$type: new $.ig.Type('DoubleValueChangedEventArgs', $.ig.EventArgs.prototype.$type)
}, true);

$.ig.util.defType('InterpolationUtil', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	interpolatePoints: function (interpolatedPoints, p, minPoints, maxPoints) {
		var q = 1 - p;
		if (interpolatedPoints == null) {
			interpolatedPoints = new $.ig.List$1($.ig.Point.prototype.$type, 0);
		}
		if (minPoints == null) {
			minPoints = new $.ig.List$1($.ig.Point.prototype.$type, 0);
		}
		var minCount = minPoints.count();
		var maxCount = maxPoints.count();
		var count = Math.max(minCount, maxCount);
		var interpolatedCount = interpolatedPoints.count();
		if (interpolatedCount < count) {
			interpolatedPoints.insertRange(interpolatedCount, new Array(count - interpolatedCount));
		}
		if (interpolatedCount > count) {
			interpolatedPoints.removeRange(count, interpolatedCount - count);
		}
		for (var i = 0; i < Math.min(minCount, maxCount); ++i) {
			interpolatedPoints.__inner[i] = { __x: (minPoints.__inner[i].__x * q) + (maxPoints.__inner[i].__x * p), __y: (minPoints.__inner[i].__y * q) + (maxPoints.__inner[i].__y * p), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		}
		if (minCount < maxCount) {
			var mn = minCount > 0 ? minPoints.__inner[minCount - 1] : { __x: 0, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			for (var i1 = minCount; i1 < maxCount; ++i1) {
				interpolatedPoints.__inner[i1] = { __x: (mn.__x * q) + (maxPoints.__inner[i1].__x * p), __y: (mn.__y * q) + (maxPoints.__inner[i1].__y * p), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			}
		}
		if (minCount > maxCount) {
			var mx = maxCount > 0 ? maxPoints.__inner[maxCount - 1] : { __x: 0, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			for (var i2 = maxCount; i2 < minCount; ++i2) {
				interpolatedPoints.__inner[i2] = interpolatedPoints.__inner[i2] = { __x: (minPoints.__inner[i2].__x * q) + (mx.__x * p), __y: (minPoints.__inner[i2].__y * q) + (mx.__y * p), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			}
		}
		return interpolatedPoints;
	}
	,
	interpolateValues$1: function ($t, interpolatedValues, p, minValues, maxValues, createEmpty, interpolate) {
		var q = 1 - p;
		if (interpolatedValues == null) {
			interpolatedValues = new $.ig.List$1($t, 0);
		}
		if (minValues == null) {
			minValues = new $.ig.List$1($t, 0);
		}
		var minCount = minValues.count();
		var maxCount = maxValues.count();
		var count = Math.max(minCount, maxCount);
		if (interpolatedValues.count() < count) {
			var capacity = count - interpolatedValues.count();
			var range = new Array(capacity);
			interpolatedValues.insertRange(interpolatedValues.count(), range);
		}
		if (interpolatedValues.count() > count) {
			interpolatedValues.removeRange(count, interpolatedValues.count() - count);
		}
		for (var i = 0; i < Math.min(minCount, maxCount); ++i) {
			interpolatedValues.__inner[i] = interpolate(p, q, minValues.__inner[i], maxValues.__inner[i]);
		}
		if (minCount < maxCount) {
			var mn = minCount > 0 ? minValues.__inner[minCount - 1] : createEmpty();
			for (var i1 = minCount; i1 < maxCount; ++i1) {
				interpolatedValues.__inner[i1] = interpolate(p, q, mn, maxValues.__inner[i1]);
			}
		}
		if (minCount > maxCount) {
			var mx = maxCount > 0 ? maxValues.__inner[maxCount - 1] : createEmpty();
			for (var i2 = maxCount; i2 < minCount; ++i2) {
				interpolatedValues.__inner[i2] = interpolate(p, q, minValues.__inner[i2], mx);
			}
		}
		return interpolatedValues;
	}
	,
	$type: new $.ig.Type('InterpolationUtil', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('PolygonUtil', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	polygonContainsPoint: function (points, pt) {
		if (points == null) {
			return false;
		}
		var pointsCount = points.count();
		if (pointsCount < 4) {
			return false;
		}
		var c = false;
		for (var i = 0, j = pointsCount - 1; i < pointsCount; j = i++) {
			if (((points.item(i).__y > pt.__y) != (points.item(j).__y > pt.__y)) && (pt.__x < (points.item(j).__x - points.item(i).__x) * (pt.__y - points.item(i).__y) / (points.item(j).__y - points.item(i).__y) + points.item(i).__x)) {
				c = !c;
			}
		}
		return c;
	}
	,
	$type: new $.ig.Type('PolygonUtil', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('StackPool$1', 'Object', {
	$t: null,
	init: function ($t) {
		this.$t = $t;
		this.$type = this.$type.specialize(this.$t);
		this._deferDisactivate = false;
		this._active = new $.ig.Dictionary$2(this.$t, $.ig.Object.prototype.$type, 0);
		this._limbo = new $.ig.Stack$1(this.$t);
		this._inactive = new $.ig.Stack$1(this.$t);
		$.ig.Object.prototype.init.call(this);
	},
	pop: function () {
		var t;
		if (this._limbo.count() != 0) {
			t = this._limbo.pop();
		} else {
			t = this._inactive.count() != 0 ? this._inactive.pop() : this.create()();
			this.activate()(t);
		}
		this._active.add(t, null);
		return t;
	}
	,
	push: function (t) {
		this._active.remove(t);
		if (this.deferDisactivate()) {
			this._limbo.push(t);
		} else {
			this.deactivate()(t);
			var inactiveCount = $.ig.StackPool$1.prototype.roundUp(this.$t, this._active.count());
			if (this._inactive.count() < inactiveCount) {
				this.destroy()(t);
			} else {
				this._inactive.push(t);
			}
		}
	}
	,
	deferDisactivate: function (value) {
		if (arguments.length === 1) {
			if (this._deferDisactivate != value) {
				this._deferDisactivate = value;
				if (!this._deferDisactivate) {
					var inactiveCount = $.ig.StackPool$1.prototype.roundUp(this.$t, this._active.count());
					while (this._limbo.count() > 0 && this._inactive.count() <= inactiveCount) {
						var t = this._limbo.pop();
						this.deactivate()(t);
						this._inactive.push(t);
					}
					while (this._limbo.count() > 0) {
						var t1 = this._limbo.pop();
						this.deactivate()(t1);
						this.destroy()(t1);
					}
					while (this._inactive.count() > inactiveCount) {
						this.destroy()(this._inactive.pop());
					}
				}
			}
			return value;
		} else {
			return this._deferDisactivate;
		}
	}
	,
	_deferDisactivate: false,
	activeCount: function () {
		return this._active.count();
	}
	,
	inactiveCount: function () {
		return this._inactive.count();
	}
	,
	_create: null,
	create: function (value) {
		if (arguments.length === 1) {
			this._create = value;
			return value;
		} else {
			return this._create;
		}
	}
	,
	_deactivate: null,
	deactivate: function (value) {
		if (arguments.length === 1) {
			this._deactivate = value;
			return value;
		} else {
			return this._deactivate;
		}
	}
	,
	_activate: null,
	activate: function (value) {
		if (arguments.length === 1) {
			this._activate = value;
			return value;
		} else {
			return this._activate;
		}
	}
	,
	_destroy: null,
	destroy: function (value) {
		if (arguments.length === 1) {
			this._destroy = value;
			return value;
		} else {
			return this._destroy;
		}
	}
	,
	roundUp: function ($t, a) {
		var p = 2;
		while (a > p) {
			p = p << 1;
		}
		return p;
	}
	,
	_active: null,
	_limbo: null,
	_inactive: null,
	$type: new $.ig.Type('StackPool$1', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('IVisualData', 'Object', {
	$type: new $.ig.Type('IVisualData', null)
}, true);

$.ig.util.defType('PrimitiveVisualData', 'Object', {
	init: function (initNumber) {
		if (initNumber > 0) {
			switch (initNumber) {
				case 1:
					this.init1.apply(this, arguments);
					break;
			}
			return;
		}
		$.ig.Object.prototype.init.call(this);
	},
	init1: function (initNumber, name) {
		$.ig.Object.prototype.init.call(this);
		this.name(name);
		this.tags(new $.ig.ShapeTags());
		this.appearance(new $.ig.PrimitiveAppearanceData());
	},
	_appearance: null,
	appearance: function (value) {
		if (arguments.length === 1) {
			this._appearance = value;
			return value;
		} else {
			return this._appearance;
		}
	}
	,
	_tags: null,
	tags: function (value) {
		if (arguments.length === 1) {
			this._tags = value;
			return value;
		} else {
			return this._tags;
		}
	}
	,
	type: function () {
	}
	,
	_name: null,
	name: function (value) {
		if (arguments.length === 1) {
			this._name = value;
			return value;
		} else {
			return this._name;
		}
	}
	,
	scaleByViewport: function (viewport) {
		this.appearance().scaleByViewport(viewport);
	}
	,
	getPoints: function (settings) {
		var points = new $.ig.List$1($.ig.List$1.prototype.$type.specialize($.ig.Point.prototype.$type), 0);
		this.getPointsOverride(points, settings);
		return points;
	}
	,
	getPointsOverride: function (points, settings) {
	}
	,
	serialize: function () {
		var sb = new $.ig.StringBuilder(0);
		sb.appendLine1("{");
		sb.appendLine1("appearance: " + (this.appearance() != null ? this.appearance().serialize() : "null") + ", ");
		sb.appendLine1("tags: [");
		for (var i = 0; i < this.tags().count(); i++) {
			if (i != 0) {
				sb.append5(", ");
			}
			sb.append5("\"" + this.tags().__inner[i] + "\"");
		}
		sb.appendLine1("],");
		sb.appendLine1("type: \"" + this.type() + "\", ");
		sb.appendLine1("name: \"" + this.name() + "\", ");
		sb.appendLine1(this.serializeOverride());
		sb.appendLine1("}");
		return sb.toString();
	}
	,
	serializeOverride: function () {
		return "";
	}
	,
	$type: new $.ig.Type('PrimitiveVisualData', $.ig.Object.prototype.$type, [$.ig.IVisualData.prototype.$type])
}, true);

$.ig.util.defType('LabelAppearanceData', 'Object', {
	init: function () {
		this._labelBrush = new $.ig.Color();
		$.ig.Object.prototype.init.call(this);
	},
	_text: null,
	text: function (value) {
		if (arguments.length === 1) {
			this._text = value;
			return value;
		} else {
			return this._text;
		}
	}
	,
	_horizontalAlignment: null,
	horizontalAlignment: function (value) {
		if (arguments.length === 1) {
			this._horizontalAlignment = value;
			return value;
		} else {
			return this._horizontalAlignment;
		}
	}
	,
	_verticalAlignment: null,
	verticalAlignment: function (value) {
		if (arguments.length === 1) {
			this._verticalAlignment = value;
			return value;
		} else {
			return this._verticalAlignment;
		}
	}
	,
	_textAlignment: null,
	textAlignment: function (value) {
		if (arguments.length === 1) {
			this._textAlignment = value;
			return value;
		} else {
			return this._textAlignment;
		}
	}
	,
	_textWrapping: null,
	textWrapping: function (value) {
		if (arguments.length === 1) {
			this._textWrapping = value;
			return value;
		} else {
			return this._textWrapping;
		}
	}
	,
	_textPosition: null,
	textPosition: function (value) {
		if (arguments.length === 1) {
			this._textPosition = value;
			return value;
		} else {
			return this._textPosition;
		}
	}
	,
	_labelBrush: null,
	labelBrush: function (value) {
		if (arguments.length === 1) {
			this._labelBrush = value;
			return value;
		} else {
			return this._labelBrush;
		}
	}
	,
	_labelBrushExtended: null,
	labelBrushExtended: function (value) {
		if (arguments.length === 1) {
			this._labelBrushExtended = value;
			return value;
		} else {
			return this._labelBrushExtended;
		}
	}
	,
	_angle: 0,
	angle: function (value) {
		if (arguments.length === 1) {
			this._angle = value;
			return value;
		} else {
			return this._angle;
		}
	}
	,
	_opacity: 0,
	opacity: function (value) {
		if (arguments.length === 1) {
			this._opacity = value;
			return value;
		} else {
			return this._opacity;
		}
	}
	,
	_visibility: false,
	visibility: function (value) {
		if (arguments.length === 1) {
			this._visibility = value;
			return value;
		} else {
			return this._visibility;
		}
	}
	,
	_font: null,
	font: function (value) {
		if (arguments.length === 1) {
			this._font = value;
			return value;
		} else {
			return this._font;
		}
	}
	,
	_fontFamily: null,
	fontFamily: function (value) {
		if (arguments.length === 1) {
			this._fontFamily = value;
			return value;
		} else {
			return this._fontFamily;
		}
	}
	,
	_fontSize: 0,
	fontSize: function (value) {
		if (arguments.length === 1) {
			this._fontSize = value;
			return value;
		} else {
			return this._fontSize;
		}
	}
	,
	_fontWeight: null,
	fontWeight: function (value) {
		if (arguments.length === 1) {
			this._fontWeight = value;
			return value;
		} else {
			return this._fontWeight;
		}
	}
	,
	_fontStyle: null,
	fontStyle: function (value) {
		if (arguments.length === 1) {
			this._fontStyle = value;
			return value;
		} else {
			return this._fontStyle;
		}
	}
	,
	_fontStretch: null,
	fontStretch: function (value) {
		if (arguments.length === 1) {
			this._fontStretch = value;
			return value;
		} else {
			return this._fontStretch;
		}
	}
	,
	_marginLeft: 0,
	marginLeft: function (value) {
		if (arguments.length === 1) {
			this._marginLeft = value;
			return value;
		} else {
			return this._marginLeft;
		}
	}
	,
	_marginRight: 0,
	marginRight: function (value) {
		if (arguments.length === 1) {
			this._marginRight = value;
			return value;
		} else {
			return this._marginRight;
		}
	}
	,
	_marginTop: 0,
	marginTop: function (value) {
		if (arguments.length === 1) {
			this._marginTop = value;
			return value;
		} else {
			return this._marginTop;
		}
	}
	,
	_marginBottom: 0,
	marginBottom: function (value) {
		if (arguments.length === 1) {
			this._marginBottom = value;
			return value;
		} else {
			return this._marginBottom;
		}
	}
	,
	serialize: function () {
		var sb = new $.ig.StringBuilder(0);
		sb.appendLine1("{");
		sb.appendLine1("text: \"" + (this.text() != null ? this.text() : "") + "\", ");
		if (this.textAlignment() != null) {
			sb.appendLine1("textAlignment: \"" + this.textAlignment() + "\", ");
		}
		if (this.textWrapping() != null) {
			sb.appendLine1("textWrapping: \"" + this.textWrapping() + "\", ");
		}
		sb.appendLine1("labelBrush: " + ($.ig.Color.prototype.l_op_Inequality_Lifted($.ig.util.toNullable($.ig.Color.prototype.$type, this.labelBrush()), $.ig.util.toNullable($.ig.Color.prototype.$type, null)) ? $.ig.AppearanceHelper.prototype.serializeColor(this.labelBrush()) : "null") + ", ");
		sb.appendLine1("labelBrushExtended: " + (this.labelBrushExtended() != null ? this.labelBrushExtended().serialize() : "null") + ", ");
		sb.appendLine1("angle: " + this.angle() + ", ");
		sb.appendLine1("marginLeft: " + this.marginLeft() + ", ");
		sb.appendLine1("marginRight: " + this.marginRight() + ", ");
		sb.appendLine1("marginTop: " + this.marginTop() + ", ");
		sb.appendLine1("marginBottom: " + this.marginBottom() + ", ");
		sb.appendLine1("opacity: " + this.opacity() + ", ");
		sb.appendLine1("visibility: " + (this.visibility() ? "true" : "false") + ", ");
		if (this.horizontalAlignment() != null) {
			sb.appendLine1("horizontalAlignment: \"" + this.horizontalAlignment() + "\", ");
		}
		if (this.verticalAlignment() != null) {
			sb.appendLine1("verticalAlignment: \"" + this.verticalAlignment() + "\", ");
		}
		if (this.font() != null) {
			sb.appendLine1("font: \"" + this.font() + "\",");
		}
		if (this.fontFamily() != null) {
			sb.appendLine1("fontFamily: \"" + $.ig.util.replace(this.fontFamily(), "\"", "'") + "\",");
		}
		if (this.fontWeight() != null) {
			sb.appendLine1("fontWeight: \"" + this.fontWeight() + "\",");
		}
		if (this.fontStyle() != null) {
			sb.appendLine1("fontStyle: \"" + this.fontStyle() + "\",");
		}
		if (this.fontStretch() != null) {
			sb.appendLine1("fontStretch: \"" + this.fontStretch() + "\",");
		}
		sb.appendLine1("fontSize: " + this.fontSize());
		sb.appendLine1("}");
		return sb.toString();
	}
	,
	$type: new $.ig.Type('LabelAppearanceData', $.ig.Object.prototype.$type, [$.ig.IVisualData.prototype.$type])
}, true);

$.ig.util.defType('BrushAppearanceData', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	type: function () {
	}
	,
	serialize: function () {
		return "{ type: \"" + this.type() + "\", " + this.serializeOverride() + " }";
	}
	,
	serializeOverride: function () {
		return "";
	}
	,
	$type: new $.ig.Type('BrushAppearanceData', $.ig.Object.prototype.$type, [$.ig.IVisualData.prototype.$type])
}, true);

$.ig.util.defType('SolidBrushAppearanceData', 'BrushAppearanceData', {
	init: function () {
		this._colorValue = new $.ig.Color();
		$.ig.BrushAppearanceData.prototype.init.call(this);
	},
	type: function () {
		return "solid";
	}
	,
	_colorValue: null,
	colorValue: function (value) {
		if (arguments.length === 1) {
			this._colorValue = value;
			return value;
		} else {
			return this._colorValue;
		}
	}
	,
	serializeOverride: function () {
		return "colorValue: " + ($.ig.Color.prototype.l_op_Inequality_Lifted($.ig.util.toNullable($.ig.Color.prototype.$type, this.colorValue()), $.ig.util.toNullable($.ig.Color.prototype.$type, null)) ? $.ig.AppearanceHelper.prototype.serializeColor(this.colorValue()) : "null");
	}
	,
	$type: new $.ig.Type('SolidBrushAppearanceData', $.ig.BrushAppearanceData.prototype.$type)
}, true);

$.ig.util.defType('LinearGradientBrushAppearanceData', 'BrushAppearanceData', {
	init: function () {
		$.ig.BrushAppearanceData.prototype.init.call(this);
		this.stops(new $.ig.List$1($.ig.GradientStopAppearanceData.prototype.$type, 0));
	},
	type: function () {
		return "linear";
	}
	,
	_startX: 0,
	startX: function (value) {
		if (arguments.length === 1) {
			this._startX = value;
			return value;
		} else {
			return this._startX;
		}
	}
	,
	_startY: 0,
	startY: function (value) {
		if (arguments.length === 1) {
			this._startY = value;
			return value;
		} else {
			return this._startY;
		}
	}
	,
	_endX: 0,
	endX: function (value) {
		if (arguments.length === 1) {
			this._endX = value;
			return value;
		} else {
			return this._endX;
		}
	}
	,
	_endY: 0,
	endY: function (value) {
		if (arguments.length === 1) {
			this._endY = value;
			return value;
		} else {
			return this._endY;
		}
	}
	,
	_stops: null,
	stops: function (value) {
		if (arguments.length === 1) {
			this._stops = value;
			return value;
		} else {
			return this._stops;
		}
	}
	,
	serializeOverride: function () {
		var sb = new $.ig.StringBuilder(0);
		sb.append5("startX: " + this.startX() + ", endX: " + this.endX() + ", startY: " + this.startY() + ", endY: " + this.endY());
		sb.append5(", stops: [");
		for (var i = 0; i < this.stops().count(); i++) {
			if (i > 0) {
				sb.append5(", ");
			}
			sb.append5(this.stops().__inner[i].serialize());
		}
		sb.append5("]");
		return sb.toString();
	}
	,
	$type: new $.ig.Type('LinearGradientBrushAppearanceData', $.ig.BrushAppearanceData.prototype.$type)
}, true);

$.ig.util.defType('GradientStopAppearanceData', 'Object', {
	init: function () {
		this._colorValue = new $.ig.Color();
		$.ig.Object.prototype.init.call(this);
	},
	_colorValue: null,
	colorValue: function (value) {
		if (arguments.length === 1) {
			this._colorValue = value;
			return value;
		} else {
			return this._colorValue;
		}
	}
	,
	_offset: 0,
	offset: function (value) {
		if (arguments.length === 1) {
			this._offset = value;
			return value;
		} else {
			return this._offset;
		}
	}
	,
	serialize: function () {
		return "{ " + "colorValue: " + ($.ig.Color.prototype.l_op_Inequality_Lifted($.ig.util.toNullable($.ig.Color.prototype.$type, this.colorValue()), $.ig.util.toNullable($.ig.Color.prototype.$type, null)) ? $.ig.AppearanceHelper.prototype.serializeColor(this.colorValue()) : "null") + ", offset: " + this.offset() + " }";
	}
	,
	$type: new $.ig.Type('GradientStopAppearanceData', $.ig.Object.prototype.$type, [$.ig.IVisualData.prototype.$type])
}, true);

$.ig.util.defType('PrimitiveAppearanceData', 'Object', {
	init: function () {
		this._stroke = new $.ig.Color();
		this._fill = new $.ig.Color();
		$.ig.Object.prototype.init.call(this);
	},
	_stroke: null,
	stroke: function (value) {
		if (arguments.length === 1) {
			this._stroke = value;
			return value;
		} else {
			return this._stroke;
		}
	}
	,
	_strokeExtended: null,
	strokeExtended: function (value) {
		if (arguments.length === 1) {
			this._strokeExtended = value;
			return value;
		} else {
			return this._strokeExtended;
		}
	}
	,
	_fill: null,
	fill: function (value) {
		if (arguments.length === 1) {
			this._fill = value;
			return value;
		} else {
			return this._fill;
		}
	}
	,
	_fillExtended: null,
	fillExtended: function (value) {
		if (arguments.length === 1) {
			this._fillExtended = value;
			return value;
		} else {
			return this._fillExtended;
		}
	}
	,
	_strokeThickness: 0,
	strokeThickness: function (value) {
		if (arguments.length === 1) {
			this._strokeThickness = value;
			return value;
		} else {
			return this._strokeThickness;
		}
	}
	,
	_visibility: 0,
	visibility: function (value) {
		if (arguments.length === 1) {
			this._visibility = value;
			return value;
		} else {
			return this._visibility;
		}
	}
	,
	_opacity: 0,
	opacity: function (value) {
		if (arguments.length === 1) {
			this._opacity = value;
			return value;
		} else {
			return this._opacity;
		}
	}
	,
	_canvasLeft: 0,
	canvasLeft: function (value) {
		if (arguments.length === 1) {
			this._canvasLeft = value;
			return value;
		} else {
			return this._canvasLeft;
		}
	}
	,
	_canvasTop: 0,
	canvasTop: function (value) {
		if (arguments.length === 1) {
			this._canvasTop = value;
			return value;
		} else {
			return this._canvasTop;
		}
	}
	,
	_canvaZIndex: 0,
	canvaZIndex: function (value) {
		if (arguments.length === 1) {
			this._canvaZIndex = value;
			return value;
		} else {
			return this._canvaZIndex;
		}
	}
	,
	_dashArray: null,
	dashArray: function (value) {
		if (arguments.length === 1) {
			this._dashArray = value;
			return value;
		} else {
			return this._dashArray;
		}
	}
	,
	_dashCap: 0,
	dashCap: function (value) {
		if (arguments.length === 1) {
			this._dashCap = value;
			return value;
		} else {
			return this._dashCap;
		}
	}
	,
	scaleByViewport: function (viewport) {
		this.canvasLeft((this.canvasLeft() - viewport.left()) / viewport.width());
		this.canvasTop((this.canvasTop() - viewport.top()) / viewport.height());
	}
	,
	serialize: function () {
		var sb = new $.ig.StringBuilder(0);
		sb.appendLine1("{");
		sb.appendLine1("stroke: " + ($.ig.Color.prototype.l_op_Inequality_Lifted($.ig.util.toNullable($.ig.Color.prototype.$type, this.stroke()), $.ig.util.toNullable($.ig.Color.prototype.$type, null)) ? $.ig.AppearanceHelper.prototype.serializeColor(this.stroke()) : "null") + ", ");
		sb.appendLine1("fill: " + ($.ig.Color.prototype.l_op_Inequality_Lifted($.ig.util.toNullable($.ig.Color.prototype.$type, this.fill()), $.ig.util.toNullable($.ig.Color.prototype.$type, null)) ? $.ig.AppearanceHelper.prototype.serializeColor(this.fill()) : "null") + ", ");
		sb.appendLine1("strokeExtended: " + (this.strokeExtended() != null ? this.strokeExtended().serialize() : "null") + ", ");
		sb.appendLine1("fillExtended: " + (this.fillExtended() != null ? this.fillExtended().serialize() : "null") + ", ");
		sb.appendLine1("strokeThickness: " + this.strokeThickness() + ", ");
		sb.appendLine1("visibility: " + (this.visibility() == $.ig.Visibility.prototype.visible ? "true" : "false") + ", ");
		sb.appendLine1("opacity: " + this.opacity() + ", ");
		sb.appendLine1("canvasLeft: " + this.canvasLeft() + ", ");
		sb.appendLine1("canvasTop: " + this.canvasTop() + ", ");
		sb.appendLine1("canvasZIndex: " + this.canvaZIndex() + ", ");
		sb.appendLine1("dashArray: null, ");
		sb.appendLine1("dashCap: " + this.dashCap());
		sb.appendLine1("}");
		return sb.toString();
	}
	,
	$type: new $.ig.Type('PrimitiveAppearanceData', $.ig.Object.prototype.$type, [$.ig.IVisualData.prototype.$type])
}, true);

$.ig.util.defType('GetPointsSettings', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	_ignoreFigureStartPoint: false,
	ignoreFigureStartPoint: function (value) {
		if (arguments.length === 1) {
			this._ignoreFigureStartPoint = value;
			return value;
		} else {
			return this._ignoreFigureStartPoint;
		}
	}
	,
	$type: new $.ig.Type('GetPointsSettings', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('ShapeTags', 'List$1', {
	init: function () {
		$.ig.List$1.prototype.init.call(this, String, 0);
	},
	$type: new $.ig.Type('ShapeTags', $.ig.List$1.prototype.$type.specialize(String))
}, true);

$.ig.util.defType('PathVisualData', 'PrimitiveVisualData', {
	type: function () {
		return "Path";
	}
	,
	init: function (initNumber) {
		if (initNumber > 0) {
			switch (initNumber) {
				case 1:
					this.init1.apply(this, arguments);
					break;
				case 2:
					this.init2.apply(this, arguments);
					break;
			}
			return;
		}
		$.ig.PrimitiveVisualData.prototype.init1.call(this, 1, "path1");
		this.data(new $.ig.List$1($.ig.GeometryData.prototype.$type, 0));
	},
	init1: function (initNumber, name, path) {
		$.ig.PrimitiveVisualData.prototype.init1.call(this, 1, name);
		this.data($.ig.AppearanceHelper.prototype.fromGeometry(path.data()));
		$.ig.AppearanceHelper.prototype.getShapeAppearance(this.appearance(), path);
	},
	init2: function (initNumber, name, line) {
		$.ig.PrimitiveVisualData.prototype.init1.call(this, 1, name);
		this.data($.ig.AppearanceHelper.prototype.fromLineData(line));
		$.ig.AppearanceHelper.prototype.getShapeAppearance(this.appearance(), line);
	},
	_data: null,
	data: function (value) {
		if (arguments.length === 1) {
			this._data = value;
			return value;
		} else {
			return this._data;
		}
	}
	,
	serializeOverride: function () {
		var sb = new $.ig.StringBuilder(0);
		sb.appendLine1("data: [");
		for (var i = 0; i < this.data().count(); i++) {
			if (i != 0) {
				sb.append5(", ");
			}
			sb.append5((this.data().__inner[i] != null ? this.data().__inner[i].serialize() : "null"));
		}
		sb.appendLine1("]");
		return sb.toString();
	}
	,
	scaleByViewport: function (viewport) {
		$.ig.PrimitiveVisualData.prototype.scaleByViewport.call(this, viewport);
		var en = this.data().getEnumerator();
		while (en.moveNext()) {
			var data = en.current();
			data.scaleByViewport(viewport);
		}
	}
	,
	getPointsOverride: function (points, settings) {
		for (var i = 0; i < this.data().count(); i++) {
			var data = this.data().__inner[i];
			data.getPointsOverride(points, settings);
		}
	}
	,
	$type: new $.ig.Type('PathVisualData', $.ig.PrimitiveVisualData.prototype.$type)
}, true);

$.ig.util.defType('GeometryData', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	type: function () {
	}
	,
	scaleByViewport: function (viewport) {
	}
	,
	getPointsOverride: function (points, settings) {
	}
	,
	serialize: function () {
		return "{ type: \"" + this.type() + "\", " + this.serializeOverride() + "}";
	}
	,
	serializeOverride: function () {
		return "";
	}
	,
	$type: new $.ig.Type('GeometryData', $.ig.Object.prototype.$type, [$.ig.IVisualData.prototype.$type])
}, true);

$.ig.util.defType('PathGeometryData', 'GeometryData', {
	init: function () {
		$.ig.GeometryData.prototype.init.call(this);
		this.figures(new $.ig.List$1($.ig.PathFigureData.prototype.$type, 0));
	},
	type: function () {
		return "Path";
	}
	,
	_figures: null,
	figures: function (value) {
		if (arguments.length === 1) {
			this._figures = value;
			return value;
		} else {
			return this._figures;
		}
	}
	,
	serializeOverride: function () {
		var sb = new $.ig.StringBuilder(0);
		sb.appendLine1("figures: [");
		for (var i = 0; i < this.figures().count(); i++) {
			if (i != 0) {
				sb.append5(", ");
			}
			sb.append5(this.figures().__inner[i].serialize());
		}
		sb.appendLine1("]");
		return sb.toString();
	}
	,
	scaleByViewport: function (viewport) {
		var en = this.figures().getEnumerator();
		while (en.moveNext()) {
			var figure = en.current();
			figure.scaleByViewport(viewport);
		}
	}
	,
	getPointsOverride: function (points, settings) {
		for (var i = 0; i < this.figures().count(); i++) {
			var figure = this.figures().__inner[i];
			figure.getPointsOverride(points, settings);
		}
	}
	,
	$type: new $.ig.Type('PathGeometryData', $.ig.GeometryData.prototype.$type)
}, true);

$.ig.util.defType('LineGeometryData', 'GeometryData', {
	init: function () {
		$.ig.GeometryData.prototype.init.call(this);
	},
	type: function () {
		return "Line";
	}
	,
	_x1: 0,
	x1: function (value) {
		if (arguments.length === 1) {
			this._x1 = value;
			return value;
		} else {
			return this._x1;
		}
	}
	,
	_y1: 0,
	y1: function (value) {
		if (arguments.length === 1) {
			this._y1 = value;
			return value;
		} else {
			return this._y1;
		}
	}
	,
	_x2: 0,
	x2: function (value) {
		if (arguments.length === 1) {
			this._x2 = value;
			return value;
		} else {
			return this._x2;
		}
	}
	,
	_y2: 0,
	y2: function (value) {
		if (arguments.length === 1) {
			this._y2 = value;
			return value;
		} else {
			return this._y2;
		}
	}
	,
	serializeOverride: function () {
		return "x1: " + this.x1() + ", y1: " + this.y1() + ", x2: " + this.x2() + ", y2:" + this.y2();
	}
	,
	scaleByViewport: function (viewport) {
		this.x1((this.x1() - viewport.left()) / viewport.width());
		this.y1((this.y1() - viewport.top()) / viewport.height());
		this.x2((this.x2() - viewport.left()) / viewport.width());
		this.y2((this.y2() - viewport.top()) / viewport.height());
	}
	,
	getPointsOverride: function (points, settings) {
		var current = new $.ig.List$1($.ig.Point.prototype.$type, 0);
		points.add(current);
		current.add({ __x: this.x1(), __y: this.y1(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		current.add({ __x: this.x2(), __y: this.y2(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
	}
	,
	$type: new $.ig.Type('LineGeometryData', $.ig.GeometryData.prototype.$type)
}, true);

$.ig.util.defType('RectangleGeometryData', 'GeometryData', {
	init: function () {
		$.ig.GeometryData.prototype.init.call(this);
	},
	type: function () {
		return "Rectangle";
	}
	,
	_x: 0,
	x: function (value) {
		if (arguments.length === 1) {
			this._x = value;
			return value;
		} else {
			return this._x;
		}
	}
	,
	_y: 0,
	y: function (value) {
		if (arguments.length === 1) {
			this._y = value;
			return value;
		} else {
			return this._y;
		}
	}
	,
	_width: 0,
	width: function (value) {
		if (arguments.length === 1) {
			this._width = value;
			return value;
		} else {
			return this._width;
		}
	}
	,
	_height: 0,
	height: function (value) {
		if (arguments.length === 1) {
			this._height = value;
			return value;
		} else {
			return this._height;
		}
	}
	,
	serializeOverride: function () {
		return "x: " + this.x() + ", y: " + this.y() + ", width: " + this.width() + ", height: " + this.height();
	}
	,
	scaleByViewport: function (viewport) {
		this.x((this.x() - viewport.left()) / viewport.width());
		this.y((this.y() - viewport.top()) / viewport.height());
		this.width(this.width() / viewport.width());
		this.height(this.height() / viewport.height());
	}
	,
	getPointsOverride: function (points, settings) {
		var current = new $.ig.List$1($.ig.Point.prototype.$type, 0);
		points.add(current);
		current.add({ __x: this.x(), __y: this.y(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		current.add({ __x: this.x() + this.width(), __y: this.y(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		current.add({ __x: this.x() + this.width(), __y: this.y() + this.height(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		current.add({ __x: this.x(), __y: this.y() + this.height(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
	}
	,
	$type: new $.ig.Type('RectangleGeometryData', $.ig.GeometryData.prototype.$type)
}, true);

$.ig.util.defType('EllipseGeometryData', 'GeometryData', {
	init: function () {
		$.ig.GeometryData.prototype.init.call(this);
	},
	type: function () {
		return "Ellipse";
	}
	,
	_centerX: 0,
	centerX: function (value) {
		if (arguments.length === 1) {
			this._centerX = value;
			return value;
		} else {
			return this._centerX;
		}
	}
	,
	_centerY: 0,
	centerY: function (value) {
		if (arguments.length === 1) {
			this._centerY = value;
			return value;
		} else {
			return this._centerY;
		}
	}
	,
	_radiusX: 0,
	radiusX: function (value) {
		if (arguments.length === 1) {
			this._radiusX = value;
			return value;
		} else {
			return this._radiusX;
		}
	}
	,
	_radiusY: 0,
	radiusY: function (value) {
		if (arguments.length === 1) {
			this._radiusY = value;
			return value;
		} else {
			return this._radiusY;
		}
	}
	,
	serializeOverride: function () {
		return "centerX: " + this.centerX() + ", centerY: " + this.centerY() + ", radiusX: " + this.radiusX() + ", radiusY: " + this.radiusY();
	}
	,
	scaleByViewport: function (viewport) {
		this.centerX((this.centerX() - viewport.left()) / viewport.width());
		this.centerX((this.centerY() - viewport.top()) / viewport.height());
		this.radiusX(this.radiusX() / viewport.width());
		this.radiusY(this.radiusY() / viewport.height());
	}
	,
	getPointsOverride: function (points, settings) {
		var current = new $.ig.List$1($.ig.Point.prototype.$type, 0);
		points.add(current);
		current.add({ __x: this.centerX(), __y: this.centerY(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
	}
	,
	$type: new $.ig.Type('EllipseGeometryData', $.ig.GeometryData.prototype.$type)
}, true);

$.ig.util.defType('PathFigureData', 'Object', {
	init: function (initNumber) {
		if (initNumber > 0) {
			switch (initNumber) {
				case 1:
					this.init1.apply(this, arguments);
					break;
			}
			return;
		}
		$.ig.Object.prototype.init.call(this);
		this.segments(new $.ig.List$1($.ig.SegmentData.prototype.$type, 0));
		this.startPoint(new $.ig.Point(0));
	},
	init1: function (initNumber, fig) {
		$.ig.Object.prototype.init.call(this);
		this.segments(new $.ig.List$1($.ig.SegmentData.prototype.$type, 0));
		this.startPoint(fig.__startPoint);
		for (var i = 0; i < fig.__segments.count(); i++) {
			var seg = fig.__segments.__inner[i];
			var newData = null;
			switch (seg.type()) {
				case $.ig.PathSegmentType.prototype.line:
					newData = new $.ig.LineSegmentData(1, seg);
					break;
				case $.ig.PathSegmentType.prototype.polyLine:
					newData = new $.ig.PolylineSegmentData(1, seg);
					break;
				case $.ig.PathSegmentType.prototype.arc:
					newData = new $.ig.ArcSegmentData(1, seg);
					break;
				case $.ig.PathSegmentType.prototype.polyBezier:
					newData = new $.ig.PolyBezierSegmentData(1, seg);
					break;
				case $.ig.PathSegmentType.prototype.bezier:
					newData = new $.ig.BezierSegmentData(1, seg);
					break;
			}
			this.segments().add(newData);
		}
	},
	_startPoint: null,
	startPoint: function (value) {
		if (arguments.length === 1) {
			this._startPoint = value;
			return value;
		} else {
			return this._startPoint;
		}
	}
	,
	_segments: null,
	segments: function (value) {
		if (arguments.length === 1) {
			this._segments = value;
			return value;
		} else {
			return this._segments;
		}
	}
	,
	serialize: function () {
		var sb = new $.ig.StringBuilder(0);
		sb.appendLine1("{");
		if ($.ig.Point.prototype.l_op_Inequality(this.startPoint(), null)) {
			sb.appendLine1("startPoint: { x: " + this.startPoint().__x + ", y: " + this.startPoint().__y + "}, ");
		}
		sb.appendLine1("segments: [");
		for (var i = 0; i < this.segments().count(); i++) {
			if (i != 0) {
				sb.append5(", ");
			}
			sb.append5(this.segments().__inner[i].serialize());
		}
		sb.appendLine1("]");
		sb.appendLine1("}");
		return sb.toString();
	}
	,
	scaleByViewport: function (viewport) {
		if ($.ig.Point.prototype.l_op_Inequality(this.startPoint(), null)) {
			this.startPoint({ __x: (this.startPoint().__x - viewport.left()) / viewport.width(), __y: (this.startPoint().__y - viewport.top()) / viewport.height(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		}
		for (var i = 0; i < this.segments().count(); i++) {
			this.segments().__inner[i].scaleByViewport(viewport);
		}
	}
	,
	getPointsOverride: function (points, settings) {
		var current = new $.ig.List$1($.ig.Point.prototype.$type, 0);
		points.add(current);
		if (!settings.ignoreFigureStartPoint()) {
			current.add({ __x: this.startPoint().__x, __y: this.startPoint().__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		}
		for (var i = 0; i < this.segments().count(); i++) {
			this.segments().__inner[i].getPointsOverride(current, settings);
		}
	}
	,
	$type: new $.ig.Type('PathFigureData', $.ig.Object.prototype.$type, [$.ig.IVisualData.prototype.$type])
}, true);

$.ig.util.defType('SegmentData', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	type: function () {
	}
	,
	scaleByViewport: function (viewport) {
	}
	,
	getPointsOverride: function (current, settings) {
	}
	,
	serialize: function () {
		var sb = new $.ig.StringBuilder(0);
		sb.appendLine1("{");
		sb.appendLine1("type: \"" + this.type() + "\", ");
		sb.appendLine1(this.serializeOverride());
		sb.appendLine1("}");
		return sb.toString();
	}
	,
	serializeOverride: function () {
		return "";
	}
	,
	$type: new $.ig.Type('SegmentData', $.ig.Object.prototype.$type, [$.ig.IVisualData.prototype.$type])
}, true);

$.ig.util.defType('LineSegmentData', 'SegmentData', {
	init: function (initNumber) {
		if (initNumber > 0) {
			switch (initNumber) {
				case 1:
					this.init1.apply(this, arguments);
					break;
			}
			return;
		}
		$.ig.SegmentData.prototype.init.call(this);
		this.point(new $.ig.Point(0));
	},
	init1: function (initNumber, seg) {
		$.ig.SegmentData.prototype.init.call(this);
		this.point(seg.point());
	},
	type: function () {
		return "Line";
	}
	,
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
	serializeOverride: function () {
		return "point: { x: " + this.point().__x + ", y: " + this.point().__y + "}";
	}
	,
	scaleByViewport: function (viewport) {
		this.point({ __x: (this.point().__x - viewport.left()) / viewport.width(), __y: (this.point().__y - viewport.top()) / viewport.height(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
	}
	,
	getPointsOverride: function (current, settings) {
		current.add({ __x: this.point().__x, __y: this.point().__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
	}
	,
	$type: new $.ig.Type('LineSegmentData', $.ig.SegmentData.prototype.$type)
}, true);

$.ig.util.defType('PolylineSegmentData', 'SegmentData', {
	init: function (initNumber) {
		if (initNumber > 0) {
			switch (initNumber) {
				case 1:
					this.init1.apply(this, arguments);
					break;
			}
			return;
		}
		$.ig.SegmentData.prototype.init.call(this);
		this.points(new $.ig.List$1($.ig.Point.prototype.$type, 0));
	},
	init1: function (initNumber, poly) {
		$.ig.SegmentData.prototype.init.call(this);
		this.points(new $.ig.List$1($.ig.Point.prototype.$type, 0));
		for (var i = 0; i < poly.__points.count(); i++) {
			this.points().add(poly.__points.__inner[i]);
		}
	},
	type: function () {
		return "Polyline";
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
	serializeOverride: function () {
		var sb = new $.ig.StringBuilder(0);
		sb.appendLine1("points: [");
		for (var i = 0; i < this.points().count(); i++) {
			if (i != 0) {
				sb.append5(", ");
			}
			sb.append5("{ x: " + this.points().__inner[i].__x + ", y: " + this.points().__inner[i].__y + "}");
		}
		sb.appendLine1("]");
		return sb.toString();
	}
	,
	scaleByViewport: function (viewport) {
		for (var i = 0; i < this.points().count(); i++) {
			this.points().__inner[i] = { __x: (this.points().__inner[i].__x - viewport.left()) / viewport.width(), __y: (this.points().__inner[i].__y - viewport.top()) / viewport.height(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		}
	}
	,
	getPointsOverride: function (current, settings) {
		for (var i = 0; i < this.points().count(); i++) {
			current.add({ __x: this.points().__inner[i].__x, __y: this.points().__inner[i].__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		}
	}
	,
	$type: new $.ig.Type('PolylineSegmentData', $.ig.SegmentData.prototype.$type)
}, true);

$.ig.util.defType('BezierSegmentData', 'SegmentData', {
	init: function (initNumber) {
		if (initNumber > 0) {
			switch (initNumber) {
				case 1:
					this.init1.apply(this, arguments);
					break;
			}
			return;
		}
		$.ig.SegmentData.prototype.init.call(this);
		this.points(new $.ig.List$1($.ig.Point.prototype.$type, 0));
	},
	init1: function (initNumber, segment) {
		$.ig.SegmentData.prototype.init.call(this);
		this.points(new $.ig.List$1($.ig.Point.prototype.$type, 0));
		this.points().add(segment.point1());
		this.points().add(segment.point2());
		this.points().add(segment.point3());
	},
	type: function () {
		return "Bezier";
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
	serializeOverride: function () {
		var sb = new $.ig.StringBuilder(0);
		sb.appendLine1("points: [");
		for (var i = 0; i < this.points().count(); i++) {
			if ($.ig.Point.prototype.l_op_Equality(this.points().__inner[i], null)) {
				break;
			}
			if (i != 0) {
				sb.append5(", ");
			}
			sb.append5("{ x: " + this.points().__inner[i].__x + ", y: " + this.points().__inner[i].__y + "}");
		}
		sb.appendLine1("]");
		return sb.toString();
	}
	,
	scaleByViewport: function (viewport) {
		for (var i = 0; i < this.points().count(); i++) {
			if ($.ig.Point.prototype.l_op_Equality(this.points().__inner[i], null)) {
				break;
			}
			this.points().__inner[i] = { __x: (this.points().__inner[i].__x - viewport.left()) / viewport.width(), __y: (this.points().__inner[i].__y - viewport.top()) / viewport.height(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		}
	}
	,
	getPointsOverride: function (current, settings) {
		for (var i = 0; i < this.points().count(); i++) {
			current.add({ __x: this.points().__inner[i].__x, __y: this.points().__inner[i].__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		}
	}
	,
	$type: new $.ig.Type('BezierSegmentData', $.ig.SegmentData.prototype.$type)
}, true);

$.ig.util.defType('PolyBezierSegmentData', 'SegmentData', {
	init: function (initNumber) {
		if (initNumber > 0) {
			switch (initNumber) {
				case 1:
					this.init1.apply(this, arguments);
					break;
			}
			return;
		}
		$.ig.SegmentData.prototype.init.call(this);
		this.points(new $.ig.List$1($.ig.Point.prototype.$type, 0));
	},
	init1: function (initNumber, poly) {
		$.ig.SegmentData.prototype.init.call(this);
		this.points(new $.ig.List$1($.ig.Point.prototype.$type, 0));
		for (var i = 0; i < poly.points().count(); i++) {
			this.points().add(poly.points().__inner[i]);
		}
	},
	type: function () {
		return "PolyBezierSpline";
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
	serializeOverride: function () {
		var sb = new $.ig.StringBuilder(0);
		sb.appendLine1("points: [");
		for (var i = 0; i < this.points().count(); i++) {
			if (i != 0) {
				sb.append5(", ");
			}
			sb.append5("{ x: " + this.points().__inner[i].__x + ", y: " + this.points().__inner[i].__y + "}");
		}
		sb.appendLine1("]");
		return sb.toString();
	}
	,
	scaleByViewport: function (viewport) {
		for (var i = 0; i < this.points().count(); i++) {
			this.points().__inner[i] = { __x: (this.points().__inner[i].__x - viewport.left()) / viewport.width(), __y: (this.points().__inner[i].__y - viewport.top()) / viewport.height(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		}
	}
	,
	getPointsOverride: function (current, settings) {
		for (var i = 0; i < this.points().count(); i++) {
			current.add({ __x: this.points().__inner[i].__x, __y: this.points().__inner[i].__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		}
	}
	,
	$type: new $.ig.Type('PolyBezierSegmentData', $.ig.SegmentData.prototype.$type)
}, true);

$.ig.util.defType('ArcSegmentData', 'SegmentData', {
	init: function (initNumber) {
		if (initNumber > 0) {
			switch (initNumber) {
				case 1:
					this.init1.apply(this, arguments);
					break;
			}
			return;
		}
		$.ig.SegmentData.prototype.init.call(this);
		this.point(new $.ig.Point(0));
		this.isLargeArc(false);
		this.isCounterClockwise(true);
		this.rotationAngle(0);
	},
	init1: function (initNumber, arc) {
		$.ig.SegmentData.prototype.init.call(this);
		this.point(arc.point());
		this.isLargeArc(arc.isLargeArc());
		this.isCounterClockwise(arc.sweepDirection() == $.ig.SweepDirection.prototype.counterclockwise);
		this.sizeX(arc.size().width());
		this.sizeY(arc.size().height());
		this.rotationAngle(arc.rotationAngle());
	},
	type: function () {
		return "Arc";
	}
	,
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
	_isLargeArc: false,
	isLargeArc: function (value) {
		if (arguments.length === 1) {
			this._isLargeArc = value;
			return value;
		} else {
			return this._isLargeArc;
		}
	}
	,
	_isCounterClockwise: false,
	isCounterClockwise: function (value) {
		if (arguments.length === 1) {
			this._isCounterClockwise = value;
			return value;
		} else {
			return this._isCounterClockwise;
		}
	}
	,
	_sizeX: 0,
	sizeX: function (value) {
		if (arguments.length === 1) {
			this._sizeX = value;
			return value;
		} else {
			return this._sizeX;
		}
	}
	,
	_sizeY: 0,
	sizeY: function (value) {
		if (arguments.length === 1) {
			this._sizeY = value;
			return value;
		} else {
			return this._sizeY;
		}
	}
	,
	_rotationAngle: 0,
	rotationAngle: function (value) {
		if (arguments.length === 1) {
			this._rotationAngle = value;
			return value;
		} else {
			return this._rotationAngle;
		}
	}
	,
	serializeOverride: function () {
		return "point: { x: " + this.point().__x + ", y: " + this.point().__y + " }, isLargeArc: " + (this.isLargeArc() ? "true" : "false") + ", isCounterClockwise: " + (this.isCounterClockwise() ? "true" : "false") + ", sizeX: " + this.sizeX() + ", sizeY: " + this.sizeY() + ", rotationAngle: " + this.rotationAngle();
	}
	,
	scaleByViewport: function (viewport) {
		this.point({ __x: (this.point().__x - viewport.left()) / viewport.width(), __y: (this.point().__y - viewport.top()) / viewport.height(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		this.sizeX(this.sizeX() / viewport.width());
		this.sizeY(this.sizeY() / viewport.height());
	}
	,
	getPointsOverride: function (current, settings) {
		current.add({ __x: this.point().__x, __y: this.point().__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
	}
	,
	$type: new $.ig.Type('ArcSegmentData', $.ig.SegmentData.prototype.$type)
}, true);

$.ig.util.defType('AppearanceHelper', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	fromBrush: function (b) {
		if (b == null) {
			return $.ig.Color.prototype.fromArgb(0, 0, 0, 0);
		}
		if ($.ig.Color.prototype.l_op_Equality_Lifted($.ig.util.toNullable($.ig.Color.prototype.$type, b.color()), $.ig.util.toNullable($.ig.Color.prototype.$type, null))) {
			return $.ig.Color.prototype.fromArgb(0, 0, 0, 0);
		}
		return b.color();
	}
	,
	fromBrushExtended: function (b) {
		if (b == null) {
			return null;
		}
		if (b._isGradient) {
			var linear = new $.ig.LinearGradientBrushAppearanceData();
			var inLinear = b;
			linear.startX(inLinear._startX);
			linear.startY(inLinear._startY);
			linear.endX(inLinear._endX);
			linear.endY(inLinear._endY);
			var $t = inLinear._gradientStops;
			for (var i = 0; i < $t.length; i++) {
				var stop = $t[i];
				var newStop = new $.ig.GradientStopAppearanceData();
				newStop.colorValue(stop.color());
				newStop.offset(stop._offset);
				linear.stops().add(newStop);
			}
			return linear;
		} else if (b._isRadialGradient) {
			return null;
		} else {
			var solid = new $.ig.SolidBrushAppearanceData();
			solid.colorValue(b.color());
			return solid;
		}
		return null;
	}
	,
	getCanvasLeft: function (visual) {
		return visual.canvasLeft();
	}
	,
	getCanvasTop: function (visual) {
		return visual.canvasTop();
	}
	,
	getCanvasZIndex: function (line) {
		return line.canvasZIndex();
	}
	,
	fromPathData: function (path) {
		return $.ig.AppearanceHelper.prototype.fromGeometry(path.data());
	}
	,
	fromLineData: function (line) {
		var lineGeometry = new $.ig.LineGeometry();
		lineGeometry.endPoint({ __x: line.x2(), __y: line.y2(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		lineGeometry.startPoint({ __x: line.x1(), __y: line.y1(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		return $.ig.AppearanceHelper.prototype.fromGeometry(lineGeometry);
	}
	,
	fromGeometry: function (data) {
		if (data == null) {
			return new $.ig.List$1($.ig.GeometryData.prototype.$type, 0);
		}
		if ($.ig.util.cast($.ig.GeometryGroup.prototype.$type, data) !== null) {
			var ret = new $.ig.List$1($.ig.GeometryData.prototype.$type, 0);
			var group = data;
			for (var i = 0; i < group.children().count(); i++) {
				var items = $.ig.AppearanceHelper.prototype.fromGeometry(group.children().__inner[i]);
				for (var j = 0; j < items.count(); j++) {
					ret.add(items.__inner[j]);
				}
			}
			return ret;
		} else if ($.ig.util.cast($.ig.PathGeometry.prototype.$type, data) !== null) {
			return $.ig.AppearanceHelper.prototype.fromPathGeometry(data);
		} else if ($.ig.util.cast($.ig.LineGeometry.prototype.$type, data) !== null) {
			return $.ig.AppearanceHelper.prototype.fromLineGeometry(data);
		} else if ($.ig.util.cast($.ig.RectangleGeometry.prototype.$type, data) !== null) {
			return $.ig.AppearanceHelper.prototype.fromRectangleGeometry(data);
		} else if ($.ig.util.cast($.ig.EllipseGeometry.prototype.$type, data) !== null) {
			return $.ig.AppearanceHelper.prototype.fromEllipseGeometry(data);
		} else {
			throw new $.ig.Error(1, "not supported");
		}
	}
	,
	fromEllipseGeometry: function (ellipseGeometry) {
		var ret = new $.ig.List$1($.ig.GeometryData.prototype.$type, 0);
		var newData = new $.ig.EllipseGeometryData();
		ret.add(newData);
		newData.centerX(ellipseGeometry.center().__x);
		newData.centerY(ellipseGeometry.center().__y);
		newData.radiusX(ellipseGeometry.radiusX());
		newData.radiusY(ellipseGeometry.radiusY());
		return ret;
	}
	,
	fromRectangleGeometry: function (rectangleGeometry) {
		var ret = new $.ig.List$1($.ig.GeometryData.prototype.$type, 0);
		var newData = new $.ig.RectangleGeometryData();
		ret.add(newData);
		newData.x(rectangleGeometry.rect().x());
		newData.y(rectangleGeometry.rect().y());
		newData.width(rectangleGeometry.rect().width());
		newData.height(rectangleGeometry.rect().height());
		return ret;
	}
	,
	fromLineGeometry: function (lineGeometry) {
		var ret = new $.ig.List$1($.ig.GeometryData.prototype.$type, 0);
		var newData = new $.ig.LineGeometryData();
		ret.add(newData);
		newData.x1(lineGeometry.startPoint().__x);
		newData.y1(lineGeometry.startPoint().__y);
		newData.x2(lineGeometry.endPoint().__x);
		newData.y2(lineGeometry.endPoint().__y);
		return ret;
	}
	,
	fromPathGeometry: function (pathGeometry) {
		var ret = new $.ig.List$1($.ig.GeometryData.prototype.$type, 0);
		var newData = new $.ig.PathGeometryData();
		ret.add(newData);
		for (var i = 0; i < pathGeometry.figures().count(); i++) {
			var figure = pathGeometry.figures().__inner[i];
			var f = new $.ig.PathFigureData(1, figure);
			newData.figures().add(f);
		}
		return ret;
	}
	,
	getShapeAppearance: function (appearance, path) {
		appearance.stroke($.ig.AppearanceHelper.prototype.fromBrush(path.__stroke));
		appearance.fill($.ig.AppearanceHelper.prototype.fromBrush(path.__fill));
		appearance.strokeExtended($.ig.AppearanceHelper.prototype.fromBrushExtended(path.__stroke));
		appearance.fillExtended($.ig.AppearanceHelper.prototype.fromBrushExtended(path.__fill));
		appearance.strokeThickness(path.strokeThickness());
		appearance.dashArray(null);
		if (path.strokeDashArray() != null) {
			appearance.dashArray(path.strokeDashArray().asArray());
		}
		appearance.dashCap(path.strokeDashCap());
		appearance.visibility(path.__visibility);
		appearance.opacity(path.__opacity);
		appearance.canvasLeft($.ig.AppearanceHelper.prototype.getCanvasLeft(path));
		appearance.canvasTop($.ig.AppearanceHelper.prototype.getCanvasTop(path));
		appearance.canvaZIndex($.ig.AppearanceHelper.prototype.getCanvasZIndex(path));
	}
	,
	fromTextElement: function (frameworkElement, fontInfo) {
		var lad = new $.ig.LabelAppearanceData();
		var tb = frameworkElement;
		lad.text(tb.text());
		lad.labelBrush($.ig.AppearanceHelper.prototype.fromBrush(tb.fill()));
		lad.labelBrushExtended($.ig.AppearanceHelper.prototype.fromBrushExtended(tb.fill()));
		lad.visibility((tb.__visibility == $.ig.Visibility.prototype.visible) ? true : false);
		lad.opacity(tb.__opacity);
		if (fontInfo != null) {
			if (fontInfo.fontFamily() != null) {
				lad.fontFamily(fontInfo.fontFamily());
			}
			if (!$.ig.util.isNaN(fontInfo.fontSize())) {
				lad.fontSize(fontInfo.fontSize());
			}
			if (fontInfo.fontWeight() != null) {
				lad.fontWeight(fontInfo.fontWeight());
			}
			if (fontInfo.fontStyle() != null) {
				lad.fontStyle(fontInfo.fontStyle());
			}
			if (fontInfo.fontStretch() != null) {
				lad.fontStretch(fontInfo.fontStyle());
			}
		}
		var angle = 0;
		var xForm = tb.renderTransform();
		if ($.ig.util.cast($.ig.RotateTransform.prototype.$type, xForm) !== null) {
			var rt = $.ig.util.cast($.ig.RotateTransform.prototype.$type, xForm);
			angle = rt.angle();
		} else if ($.ig.util.cast($.ig.TransformGroup.prototype.$type, xForm) !== null) {
			var tg = $.ig.util.cast($.ig.TransformGroup.prototype.$type, xForm);
			var en = tg.children().getEnumerator();
			while (en.moveNext()) {
				var child = en.current();
				if ($.ig.util.cast($.ig.RotateTransform.prototype.$type, child) !== null) {
					var rt1 = $.ig.util.cast($.ig.RotateTransform.prototype.$type, child);
					angle = rt1.angle();
					break;
				}
			}
		}
		lad.angle(angle);
		return lad;
	}
	,
	serializeColor: function (color) {
		return "{ r: " + color.r() + ", g: " + color.g() + ", b: " + color.b() + ", a: " + color.a() + "}";
	}
	,
	serializeItems: function (sb, name, items, isFirst) {
		if (items != null) {
			if (!isFirst) {
				sb.append5(", ");
			}
			sb.append5(name);
			sb.append5(": [");
			var addedFirst = false;
			var en = items.getEnumerator();
			while (en.moveNext()) {
				var item = en.current();
				if (addedFirst) {
					sb.appendLine1(", ");
				} else {
					addedFirst = true;
				}
				sb.append5(item.serialize());
			}
			sb.appendLine1("]");
			return true;
		}
		return false;
	}
	,
	serializeItem: function (sb, name, item, isFirst) {
		if (item != null) {
			if (!isFirst) {
				sb.append5(", ");
			}
			sb.append5(name);
			sb.append5(": ");
			sb.appendLine1(item.serialize());
			return true;
		}
		return false;
	}
	,
	$type: new $.ig.Type('AppearanceHelper', $.ig.Object.prototype.$type)
}, true);

$.ig.InterpolationMode.prototype.rGB = 0;
$.ig.InterpolationMode.prototype.hSV = 1;

$.ig.BaseDOMEventProxy.prototype.nullTimer = -1;
$.ig.BaseDOMEventProxy.prototype.mSPointerEnabled = false;
$.ig.BaseDOMEventProxy.prototype.pointerEnabled = false;
$.ig.BaseDOMEventProxy.prototype.tridentVersion = 0;
$.ig.BaseDOMEventProxy.prototype.edgeVersion = 0;

$.ig.DOMEventProxy.prototype.__proxyCount = 0;

$.ig.ColorUtil.prototype._r = new $.ig.Random(0);
$.ig.ColorUtil.prototype.__randomColors = null;

$.ig.CssHelper.prototype.defaultMarginValue = "-4321px";
$.ig.CssHelper.prototype.defaultColorValue = "rgb(3, 2, 1)";
$.ig.CssHelper.prototype.defaultBackgroundImageValue = "none";
$.ig.CssHelper.prototype.defaultTextAlignValue = "justify";
$.ig.CssHelper.prototype.defaultVerticalAlignValue = "baseline";
$.ig.CssHelper.prototype.defaultOpacityValue = "0.888";
$.ig.CssHelper.prototype.defaultVisibilityValue = "hidden";
$.ig.CssHelper.prototype.defaultWidthHeightValue = "4321px";
$.ig.CssHelper.prototype.maxClasses = 500;

$.ig.BrushCollection.prototype._random = new $.ig.Random(0);

} (jQuery));



(function ($) {
$.ig = $.ig || {};
var $$t = {}
$.ig.$currDefinitions = $$t;
$.ig.util.bulkDefine(["MorphSegment:a", 
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
"List$1:ba", 
"IList$1:bb", 
"ICollection$1:bc", 
"IArray:bd", 
"Script:be", 
"IArrayList:bf", 
"Array:bg", 
"CompareCallback:bh", 
"MulticastDelegate:bi", 
"IntPtr:bj", 
"Func$3:bk", 
"Action$1:bl", 
"Comparer$1:bm", 
"IComparer:bn", 
"IComparer$1:bo", 
"DefaultComparer$1:bp", 
"Comparison$1:bq", 
"ReadOnlyCollection$1:br", 
"Predicate$1:bs", 
"NotImplementedException:bt", 
"Point:bu", 
"GeometryUtil:bv", 
"Math:bw", 
"Rect:bx", 
"Size:by", 
"Tuple$2:bz", 
"PathFigure:b0", 
"PathSegmentCollection:b1", 
"ArcSegment:b2", 
"PathSegment:b3", 
"PathSegmentType:b4", 
"SweepDirection:b5", 
"PolyLineSegment:b6", 
"PointCollection:b7", 
"InterpolationUtil:b8", 
"Func$1:b9", 
"Func$5:ca", 
"XamRadialGaugeView:cb", 
"XamRadialGauge:cc", 
"Control:cd", 
"FrameworkElement:ce", 
"UIElement:cf", 
"DependencyObject:cg", 
"Dictionary:ch", 
"DependencyProperty:ci", 
"PropertyMetadata:cj", 
"PropertyChangedCallback:ck", 
"DependencyPropertyChangedEventArgs:cl", 
"DependencyPropertiesCollection:cm", 
"UnsetValue:cn", 
"Binding:co", 
"PropertyPath:cp", 
"Transform:cq", 
"Visibility:cr", 
"Style:cs", 
"Thickness:ct", 
"HorizontalAlignment:cu", 
"VerticalAlignment:cv", 
"DoubleAnimator:cw", 
"INotifyPropertyChanged:cx", 
"PropertyChangedEventHandler:cy", 
"PropertyChangedEventArgs:cz", 
"EasingFunctionHandler:c0", 
"Callback:c1", 
"Delegate:c2", 
"Interlocked:c3", 
"BrushCollection:c4", 
"ObservableCollection$1:c5", 
"INotifyCollectionChanged:c6", 
"NotifyCollectionChangedEventHandler:c7", 
"NotifyCollectionChangedEventArgs:c8", 
"EventArgs:c9", 
"NotifyCollectionChangedAction:da", 
"InterpolationMode:db", 
"Brush:dc", 
"Color:dd", 
"ArgumentException:de", 
"Random:df", 
"MathUtil:dg", 
"RuntimeHelpers:dh", 
"RuntimeFieldHandle:di", 
"ColorUtil:dj", 
"RadialGaugeNeedleShape:dk", 
"RadialGaugePivotShape:dl", 
"RadialGaugeScaleOversweepShape:dm", 
"RadialGaugeBackingShape:dn", 
"RadialGaugeRangeCollection:dp", 
"RadialGaugeDuplicateLabelOmissionStrategy:dq", 
"RadialGaugeScaler:dr", 
"RadialGaugeFrame:ds", 
"RadialGaugeNeedleFrame:dt", 
"BrushUtil:du", 
"LinearGradientBrush:dv", 
"GradientStop:dw", 
"JQueryObject:dx", 
"Element:dy", 
"ElementAttributeCollection:dz", 
"ElementCollection:d0", 
"WebStyle:d1", 
"ElementNodeType:d2", 
"Document:d3", 
"EventListener:d4", 
"IElementEventHandler:d5", 
"ElementEventHandler:d6", 
"ElementAttribute:d7", 
"JQueryPosition:d8", 
"JQueryCallback:d9", 
"JQueryEvent:ea", 
"JQueryUICallback:eb", 
"CssHelper:ec", 
"JQuery:ed", 
"JQueryDeferred:ee", 
"JQueryPromise:ef", 
"Action:eg", 
"CssGradientUtil:eh", 
"PathGeometry:ei", 
"Geometry:ej", 
"GeometryType:ek", 
"PathFigureCollection:el", 
"RadialGaugeScaleFrame:em", 
"RadialGaugeRangeFrame:en", 
"LineSegment:eo", 
"BackingFrame:ep", 
"StackPool$1:eq", 
"Dictionary$2:er", 
"IDictionary$2:es", 
"IDictionary:et", 
"Func$2:eu", 
"KeyValuePair$2:ev", 
"Enumerable:ew", 
"Thread:ex", 
"ThreadStart:ey", 
"IOrderedEnumerable$1:ez", 
"SortedList$1:e0", 
"ArgumentNullException:e1", 
"IEqualityComparer$1:e2", 
"EqualityComparer$1:e3", 
"IEqualityComparer:e4", 
"DefaultEqualityComparer$1:e5", 
"InvalidOperationException:e6", 
"Stack$1:e7", 
"ReverseArrayEnumerator$1:e8", 
"Path:e9", 
"Shape:fa", 
"DoubleCollection:fb", 
"TextBlock:fc", 
"FormatRadialGaugeLabelHandler:fd", 
"FormatRadialGaugeLabelEventArgs:fe", 
"AlignRadialGaugeLabelHangler:ff", 
"AlignRadialGaugeLabelEventArgs:fg", 
"DoubleValueChangedEventHandler:fh", 
"DoubleValueChangedEventArgs:fi", 
"XamRadialGaugeRange:fj", 
"PolygonUtil:fk", 
"TransformGroup:fl", 
"TransformCollection:fm", 
"RotateTransform:fn", 
"RadialGaugeNeedlePreparer:fo", 
"RadialGaugeNeedleParameters:fp", 
"GaugeVisualData:fq", 
"PrimitiveVisualData:fr", 
"IVisualData:fs", 
"PrimitiveAppearanceData:ft", 
"BrushAppearanceData:fu", 
"StringBuilder:fv", 
"Environment:fw", 
"AppearanceHelper:fx", 
"LinearGradientBrushAppearanceData:fy", 
"GradientStopAppearanceData:fz", 
"SolidBrushAppearanceData:f0", 
"GeometryData:f1", 
"GetPointsSettings:f2", 
"Line:f3", 
"LineGeometry:f4", 
"GeometryGroup:f5", 
"GeometryCollection:f6", 
"FillRule:f7", 
"RectangleGeometry:f8", 
"EllipseGeometry:f9", 
"EllipseGeometryData:ga", 
"RectangleGeometryData:gb", 
"LineGeometryData:gc", 
"PathGeometryData:gd", 
"PathFigureData:ge", 
"SegmentData:gf", 
"LineSegmentData:gg", 
"PolylineSegmentData:gh", 
"ArcSegmentData:gi", 
"PolyBezierSegment:gj", 
"PolyBezierSegmentData:gk", 
"BezierSegment:gl", 
"BezierSegmentData:gm", 
"LabelAppearanceData:gn", 
"FontInfo:go", 
"ShapeTags:gp", 
"ScaleVisualData:gq", 
"ScaleLabelVisualDataList:gr", 
"ScaleTickmarkVisualDataList:gs", 
"NeedleVisualData:gt", 
"RangeVisualDataList:gu", 
"ScaleLabelVisualData:gv", 
"ScaleTickmarkVisualData:gw", 
"RangeVisualData:gx", 
"FontUtil:gy", 
"RenderingContext:gz", 
"IRenderer:g0", 
"Rectangle:g1", 
"Polygon:g2", 
"Polyline:g3", 
"DataTemplateRenderInfo:g4", 
"DataTemplatePassInfo:g5", 
"ContentControl:g6", 
"DataTemplate:g7", 
"DataTemplateRenderHandler:g8", 
"DataTemplateMeasureHandler:g9", 
"DataTemplateMeasureInfo:ha", 
"DataTemplatePassHandler:hb", 
"EventProxy:hc", 
"ModifierKeys:hd", 
"MouseWheelHandler:he", 
"GestureHandler:hf", 
"ZoomGestureHandler:hg", 
"FlingGestureHandler:hh", 
"ContactHandler:hi", 
"TouchHandler:hj", 
"MouseOverHandler:hk", 
"MouseHandler:hl", 
"KeyHandler:hm", 
"Key:hn", 
"window:ho", 
"DivElement:hp", 
"CanvasElement:hq", 
"CanvasContext:hr", 
"CanvasViewRenderer:hs", 
"CanvasContext2D:ht", 
"TextMetrics:hu", 
"ImageData:hv", 
"Gradient:hw", 
"TranslateTransform:hx", 
"ScaleTransform:hy", 
"DOMEventProxy:hz", 
"BaseDOMEventProxy:h0", 
"MouseEventArgs:h1", 
"MSGesture:h2", 
"PathVisualData:h3", 
"AbstractEnumerable:h5", 
"AbstractEnumerator:h6", 
"GenericEnumerable$1:h7", 
"GenericEnumerator$1:h8"]);


$.ig.util.defType('RadialGaugePivotShape', 'Enum', {
	init: function (v) {
		this._v = v;
	},
	$getName: function (v) {
		switch (v) {
			case 0: return "None";
			case 1: return "Circle";
			case 2: return "CircleWithHole";
			case 3: return "CircleOverlay";
			case 4: return "CircleOverlayWithHole";
			case 5: return "CircleUnderlay";
			case 6: return "CircleUnderlayWithHole";
			default: return v.toString();
		}
	},
	$value: function () {
		return this._v;
	},
	$type: new $.ig.Type('RadialGaugePivotShape', $.ig.Enum.prototype.$type)
}, true);

$.ig.util.defType('RadialGaugeNeedleShape', 'Enum', {
	init: function (v) {
		this._v = v;
	},
	$getName: function (v) {
		switch (v) {
			case 0: return "None";
			case 1: return "Rectangle";
			case 2: return "Triangle";
			case 3: return "Needle";
			case 4: return "Trapezoid";
			case 5: return "RectangleWithBulb";
			case 6: return "TriangleWithBulb";
			case 7: return "NeedleWithBulb";
			case 8: return "TrapezoidWithBulb";
			default: return v.toString();
		}
	},
	$value: function () {
		return this._v;
	},
	$type: new $.ig.Type('RadialGaugeNeedleShape', $.ig.Enum.prototype.$type)
}, true);

$.ig.util.defType('RadialGaugeScaleOversweepShape', 'Enum', {
	init: function (v) {
		this._v = v;
	},
	$getName: function (v) {
		switch (v) {
			case 0: return "Auto";
			case 1: return "Circular";
			case 2: return "Fitted";
			default: return v.toString();
		}
	},
	$value: function () {
		return this._v;
	},
	$type: new $.ig.Type('RadialGaugeScaleOversweepShape', $.ig.Enum.prototype.$type)
}, true);

$.ig.util.defType('RadialGaugeBackingShape', 'Enum', {
	init: function (v) {
		this._v = v;
	},
	$getName: function (v) {
		switch (v) {
			case 0: return "Circular";
			case 1: return "Fitted";
			default: return v.toString();
		}
	},
	$value: function () {
		return this._v;
	},
	$type: new $.ig.Type('RadialGaugeBackingShape', $.ig.Enum.prototype.$type)
}, true);

$.ig.util.defType('RadialGaugeDuplicateLabelOmissionStrategy', 'Enum', {
	init: function (v) {
		this._v = v;
	},
	$getName: function (v) {
		switch (v) {
			case 0: return "OmitLast";
			case 1: return "OmitFirst";
			case 2: return "OmitNeither";
			case 3: return "OmitBoth";
			default: return v.toString();
		}
	},
	$value: function () {
		return this._v;
	},
	$type: new $.ig.Type('RadialGaugeDuplicateLabelOmissionStrategy', $.ig.Enum.prototype.$type)
}, true);

$.ig.util.defType('MorphSegment', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
		this._segmentPoints = new $.ig.List$1($.ig.Point.prototype.$type, 0);
		this._tweenPoints = new $.ig.List$1($.ig.Point.prototype.$type, 0);
	},
	_segmentPoints: null,
	_tweenPoints: null,
	_isArcPhase: false,
	_arcRadius: 0,
	_isTween: false,
	_hasTweenPoints: false,
	_isLargeArc: false,
	_isClockwise: false,
	generateTween: function (otherArc) {
		this._tweenPoints.clear();
		var numPoints = 20;
		if (this._isArcPhase) {
			var p;
			var startPoint;
			var endPoint;
			if (this._segmentPoints.count() == 0) {
				startPoint = { __x: 0, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				endPoint = { __x: 0, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			} else {
				startPoint = this._segmentPoints.__inner[0];
				endPoint = this._segmentPoints.__inner[1];
			}
			var center = $.ig.GeometryUtil.prototype.getCenterFromArcSegment(startPoint, endPoint, this._arcRadius, !this._isClockwise, this._isLargeArc);
			var startAngle = $.ig.GeometryUtil.prototype.getAngleTo(center, startPoint);
			var endAngle = $.ig.GeometryUtil.prototype.getAngleTo(center, endPoint);
			while (this._isClockwise && endAngle < startAngle) {
				endAngle += Math.PI * 2;
			}
			while (!this._isClockwise && endAngle > startAngle) {
				endAngle -= Math.PI * 2;
			}
			var currAngle = 0;
			for (var i = 0; i < numPoints; i++) {
				p = i / numPoints;
				if (this._isClockwise) {
					currAngle = startAngle + (endAngle - startAngle) * p;
				} else {
					currAngle = startAngle - (startAngle - endAngle) * p;
				}
				this._tweenPoints.add({ __x: center.__x + Math.cos(currAngle) * this._arcRadius, __y: center.__y + Math.sin(currAngle) * this._arcRadius, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			}
		} else {
			var p1;
			var startPoint1;
			var endPoint1;
			if (this._segmentPoints.count() == 0) {
				startPoint1 = { __x: 0, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				endPoint1 = { __x: 0, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			} else {
				startPoint1 = this._segmentPoints.__inner[0];
				endPoint1 = this._segmentPoints.__inner[1];
			}
			var startX = startPoint1.__x;
			var startY = startPoint1.__y;
			var endX = endPoint1.__x;
			var endY = endPoint1.__y;
			for (var i1 = 0; i1 < numPoints; i1++) {
				p1 = i1 / numPoints;
				this._tweenPoints.add({ __x: startX + (endX - startX) * p1, __y: startY + (endY - startY) * p1, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			}
		}
		this._hasTweenPoints = true;
	}
	,
	renderSegment: function (lastPoint, pf, radius, centerX, centerY) {
		var segment = this;
		var seg = null;
		var segPoints = segment._segmentPoints;
		var segmentPointsCount = segPoints.count();
		if (segmentPointsCount == 0) {
			return lastPoint;
		}
		if (pf.__segments.count() == 0) {
			var startPoint = segPoints.__inner[0];
			pf.__startPoint = { __x: centerX + radius * startPoint.__x, __y: centerY + radius * startPoint.__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		}
		if (segment._isArcPhase) {
			if (segmentPointsCount > 1) {
				var arc = new $.ig.ArcSegment();
				seg = arc;
				var endPoint = segPoints.__inner[1];
				arc.point({ __x: centerX + radius * endPoint.__x, __y: centerY + radius * endPoint.__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				arc.isLargeArc(segment._isLargeArc);
				arc.sweepDirection(segment._isClockwise ? $.ig.SweepDirection.prototype.clockwise : $.ig.SweepDirection.prototype.counterclockwise);
				arc.size(new $.ig.Size(1, segment._arcRadius * radius, segment._arcRadius * radius));
				lastPoint = arc.point();
			}
		} else {
			var pl = new $.ig.PolyLineSegment();
			seg = pl;
			if (segmentPointsCount > 1) {
				var currPoint;
				var pts = pl.__points;
				for (var i = 1; i < segmentPointsCount; i++) {
					currPoint = segPoints.__inner[i];
					pts.add({ __x: centerX + radius * currPoint.__x, __y: centerY + radius * currPoint.__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				}
				lastPoint = pl.__points.__inner[pl.__points.count() - 1];
			}
		}
		pf.__segments.add(seg);
		return lastPoint;
	}
	,
	interpolateSegment: function (p, previous, next) {
		var target = this;
		var minPoints = previous._segmentPoints;
		var minIsRaster = false;
		var maxPoints = next._segmentPoints;
		var maxIsRaster = false;
		if (minPoints.count() == 0 && maxPoints.count() == 0) {
			return;
		}
		target._isArcPhase = false;
		target._isTween = false;
		target._hasTweenPoints = false;
		target._isLargeArc = false;
		target._isClockwise = false;
		if (previous._isTween) {
			minIsRaster = true;
		}
		if (next._isTween) {
			maxIsRaster = true;
		}
		if (previous._isArcPhase != next._isArcPhase || previous._isTween || next._isTween || previous._isLargeArc != next._isLargeArc || previous._isClockwise != next._isClockwise) {
			minIsRaster = true;
			maxIsRaster = true;
			target._isTween = true;
		}
		if (minIsRaster && !previous._isTween) {
			if (!previous._hasTweenPoints) {
				previous.generateTween(next._arcRadius);
			}
			minPoints = previous._tweenPoints;
		}
		if (maxIsRaster && !next._isTween) {
			if (!next._hasTweenPoints) {
				next.generateTween(previous._arcRadius);
			}
			maxPoints = next._tweenPoints;
		}
		if (previous._isTween) {
			minPoints = previous._tweenPoints;
		}
		if (next._isTween) {
			maxPoints = next._tweenPoints;
		}
		target._tweenPoints.clear();
		if (previous._isArcPhase && next._isArcPhase) {
			target._isArcPhase = true;
		}
		$.ig.InterpolationUtil.prototype.interpolatePoints(target._segmentPoints, p, minPoints, maxPoints);
		if (target._isTween) {
			var point;
			var count = target._segmentPoints.count();
			for (var i = 0; i < count; i++) {
				point = target._segmentPoints.__inner[i];
				target._tweenPoints.add({ __x: point.__x, __y: point.__y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			}
			target._hasTweenPoints = true;
		}
		if (previous._isLargeArc == next._isLargeArc) {
			target._isLargeArc = next._isLargeArc;
		}
		if (previous._isClockwise == next._isClockwise) {
			target._isClockwise = next._isClockwise;
		}
		if (target._isArcPhase) {
			target._arcRadius = previous._arcRadius + (next._arcRadius - previous._arcRadius) * p;
		}
	}
	,
	reset: function () {
		this._segmentPoints.clear();
		this._tweenPoints.clear();
		this._isTween = false;
		this._hasTweenPoints = false;
		this._isArcPhase = false;
	}
	,
	$type: new $.ig.Type('MorphSegment', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('XamRadialGaugeView', 'Object', {
	init: function (gauge) {
		this.__needlePath = new $.ig.Path();
		this.__scalePath = new $.ig.Path();
		this.__backingPath = new $.ig.Path();
		this.__overlayPath = new $.ig.Path();
		this.__underlayPath = new $.ig.Path();
		this.__brushNames = [ "needle", "needlePivot", "backing", "scale", "tick", "minorTick", "label" ];
		this.__ignoreBrushChanged = false;
		this.__scheduled = false;
		this.__font = null;
		this.__fontBrush = new $.ig.Brush();
		this.__labelBrush = (function () {
			var $ret = new $.ig.Brush();
			$ret.fill("black");
			return $ret;
		}());
		this.__currentFontHeight = NaN;
		this.__canvasWidth = -1;
		this.__canvasHeight = -1;
		this.__userRangeBrushes = null;
		this.__userRangeOutlines = null;
		$.ig.Object.prototype.init.call(this);
		this.gauge(gauge);
		this.__actualBrushes = new $.ig.Dictionary$2(String, $.ig.Dictionary$2.prototype.$type.specialize(String, String), 0);
		this.activeLabels(new $.ig.List$1($.ig.TextBlock.prototype.$type, 0));
		this.activePaths(new $.ig.List$1($.ig.Path.prototype.$type, 0));
		this.activeRangePaths(new $.ig.List$1($.ig.Path.prototype.$type, 0));
	},
	_gauge: null,
	gauge: function (value) {
		if (arguments.length === 1) {
			this._gauge = value;
			return value;
		} else {
			return this._gauge;
		}
	}
	,
	_cssSpan: null,
	labelActivate: function (tb) {
		tb.__visibility = $.ig.Visibility.prototype.visible;
	}
	,
	labelDeactivate: function (tb) {
		tb.__visibility = $.ig.Visibility.prototype.collapsed;
	}
	,
	labelDestroy: function (tb) {
		this.activeLabels().remove(tb);
	}
	,
	labelCreate: function () {
		var tb = new $.ig.TextBlock();
		this.activeLabels().add(tb);
		return tb;
	}
	,
	pathActivate: function (p) {
		p.__visibility = $.ig.Visibility.prototype.visible;
	}
	,
	pathDeactivate: function (p) {
		p.__visibility = $.ig.Visibility.prototype.collapsed;
	}
	,
	pathDestroy: function (p) {
		this.activePaths().remove(p);
	}
	,
	pathCreate: function () {
		var p = new $.ig.Path();
		this.activePaths().add(p);
		return p;
	}
	,
	rangePathActivate: function (p) {
		p.__visibility = $.ig.Visibility.prototype.visible;
	}
	,
	rangePathDeactivate: function (p) {
		p.__visibility = $.ig.Visibility.prototype.collapsed;
	}
	,
	rangePathDestroy: function (p) {
		this.activeRangePaths().remove(p);
	}
	,
	rangePathCreate: function () {
		var p = new $.ig.Path();
		this.activeRangePaths().add(p);
		return p;
	}
	,
	_activeRangePaths: null,
	activeRangePaths: function (value) {
		if (arguments.length === 1) {
			this._activeRangePaths = value;
			return value;
		} else {
			return this._activeRangePaths;
		}
	}
	,
	_activePaths: null,
	activePaths: function (value) {
		if (arguments.length === 1) {
			this._activePaths = value;
			return value;
		} else {
			return this._activePaths;
		}
	}
	,
	_activeLabels: null,
	activeLabels: function (value) {
		if (arguments.length === 1) {
			this._activeLabels = value;
			return value;
		} else {
			return this._activeLabels;
		}
	}
	,
	__needlePath: null,
	__scalePath: null,
	__backingPath: null,
	__overlayPath: null,
	__underlayPath: null,
	ready: function () {
		return true;
	}
	,
	getNeedlePath: function () {
		return this.__needlePath;
	}
	,
	getScalePath: function () {
		return this.__scalePath;
	}
	,
	getDesiredWidth: function (element) {
		var tb = $.ig.util.cast($.ig.TextBlock.prototype.$type, element);
		if (tb != null && tb.text() != null) {
			return this.context().measureTextWidth(tb.text()) + $.ig.XamRadialGaugeView.prototype._tEXT_MARGIN;
		}
		return 0;
	}
	,
	getLabelSize: function (tb) {
		if ($.ig.util.isNaN(this.__currentFontHeight)) {
			this.__currentFontHeight = this.getCurrentFontHeight(null);
		}
		var width = this.getDesiredWidth(tb);
		var height = this.__currentFontHeight;
		return new $.ig.Size(1, width, height);
	}
	,
	__actualBrushes: null,
	__brushNames: null,
	getDefaultColors: function () {
		this.__ignoreBrushChanged = true;
		var clrs = [ "#CED3D8", "#CED3D8", "#EBEDEF", "#FFC73C", "#617583", "rgba(100,107,114,.74)", "rgba(100,107,114,.74)" ];
		var outline = [ "#B2B9C0", "#B2B9C0", "#B2B9C0", null, null, null, null ];
		var cssNames = [ "background-color", "border-color" ];
		var defaults = (function () {
			var $ret = new $.ig.List$1(Array, 0);
			$ret.add(clrs);
			$ret.add(outline);
			return $ret;
		}());
		this.__actualBrushes.clear();
		var namesCount = this.__brushNames.length;
		for (var i = 0; i < namesCount; i++) {
			var actualValues = new $.ig.Dictionary$2(String, String, 0);
			var name = this.__brushNames[i];
			var cssValues = new Array(cssNames.length);
			$.ig.util.arrayCopy1(cssNames, 0, cssValues, 0, cssNames.length);
			this.cssValue("ui-radialgauge-" + name, cssValues);
			for (var j = 0; j < cssValues.length; j++) {
				actualValues.item(cssNames[j], (cssValues[j] == null) ? defaults.__inner[j][i] : cssValues[j]);
			}
			this.__actualBrushes.item(name, actualValues);
		}
		this.updateBrushes();
		var brushes;
		var outlines;
		var $ret = $.ig.BrushUtil.prototype.getBrushCollection("radialgauge-range", this.container(), brushes, outlines, null);
		brushes = $ret.p2;
		outlines = $ret.p3;
		this.__rangeBrushes = brushes;
		this.__rangeOutlines = outlines;
		if (this.__userRangeBrushes == null) {
			this.gauge().rangeBrushes(this.__rangeBrushes);
		}
		if (this.__userRangeOutlines == null) {
			this.gauge().rangeOutlines(this.__rangeOutlines);
		}
		this.__ignoreBrushChanged = false;
	}
	,
	__rangeBrushes: null,
	__rangeOutlines: null,
	updateBrushes: function () {
		this.__ignoreBrushChanged = true;
		var namesCount = this.__brushNames.length;
		for (var i = 0; i < namesCount; i++) {
			var name = this.__brushNames[i];
			this.updateBrush(name, this.__actualBrushes.item(name));
		}
		this.__ignoreBrushChanged = false;
	}
	,
	updateBrush: function (name, actualValues) {
		var colorBrush = null;
		var outlineBrush = null;
		var valuesCount = actualValues.count();
		var en = actualValues.keys().getEnumerator();
		while (en.moveNext()) {
			var key = en.current();
			switch (key) {
				case "background-color":
					colorBrush = new $.ig.Brush();
					if (actualValues.item(key) != null) {
						colorBrush.__fill = actualValues.item(key);
					}
					break;
				case "border-color":
					outlineBrush = new $.ig.Brush();
					if (actualValues.item(key) != null) {
						outlineBrush.__fill = actualValues.item(key);
					}
					break;
			}
		}
		switch (name) {
			case "needle":
				if (this.__userNeedleBrush == null) {
					this.gauge().needleBrush(colorBrush);
				}
				if (this.__userNeedleOutline == null) {
					this.gauge().needleOutline(outlineBrush);
				}
				break;
			case "needlePivot":
				if (this.__userNeedlePivotBrush == null) {
					this.gauge().needlePivotBrush(colorBrush);
				}
				if (this.__userNeedlePivotOutline == null) {
					this.gauge().needlePivotOutline(outlineBrush);
				}
				break;
			case "backing":
				if (this.__userBackingBrush == null) {
					this.gauge().backingBrush(colorBrush);
				}
				if (this.__userBackingOutline == null) {
					this.gauge().backingOutline(outlineBrush);
				}
				break;
			case "scale":
				if (this.__userScaleBrush == null) {
					this.gauge().scaleBrush(colorBrush);
				}
				break;
			case "tick":
				if (this.__userTickBrush == null) {
					this.gauge().tickBrush(colorBrush);
				}
				break;
			case "minorTick":
				if (this.__userMinorTickBrush == null) {
					this.gauge().minorTickBrush(colorBrush);
				}
				break;
			case "label":
				this.__labelBrush = colorBrush;
				break;
		}
	}
	,
	cssValue: function (css, prop) {
		var span = this._cssSpan;
		if (span == null) {
			span = this._cssSpan = $("<span style='position:absolute;display:none' />");
			$("body").append(this._cssSpan);
		}
		var i = prop.length, i0 = i;
		var old = new Array(i0);
		while (i0-- > 0) {
			old[i0] = prop[i0] == "font" ? null : span.css(prop[i0]);
		}
		span.addClass(css);
		while (i-- > 0) {
			var p = prop[i];
			p = span.css(p);
			prop[i] = (p == null || p.length == 0 || p == "null" || p == "transparent" || p == old[i]) ? null : p;
		}
		span.removeClass(css);
		return prop;
	}
	,
	toDouble: function (val, def) {
		var len = val == null ? 0 : val.length;
		if (len < 1) {
			return def;
		}
		var s = val.split('.');
		var i = (s[0].length > 0) ? $.ig.Number.prototype.parseInt(s[0]) : 0;
		if (s.length != 2) {
			return i;
		}
		len = s[1].length;
		var d = $.ig.Number.prototype.parseInt(s[1]);
		while (len-- > 0) {
			d /= 10;
		}
		return d + i;
	}
	,
	__ignoreBrushChanged: false,
	onInit: function () {
		this.__ignoreBrushChanged = true;
		this.gauge().needleBrush((function () {
			var $ret = new $.ig.Brush();
			$ret.fill("#CED3D8");
			return $ret;
		}()));
		this.gauge().needleOutline((function () {
			var $ret = new $.ig.Brush();
			$ret.fill("#B2B9C0");
			return $ret;
		}()));
		this.gauge().tickBrush((function () {
			var $ret = new $.ig.Brush();
			$ret.fill("#617583");
			return $ret;
		}()));
		this.gauge().scaleBrush((function () {
			var $ret = new $.ig.Brush();
			$ret.fill("#FFC73C");
			return $ret;
		}()));
		this.gauge().backingBrush((function () {
			var $ret = new $.ig.Brush();
			$ret.fill("#EBEDEF");
			return $ret;
		}()));
		this.gauge().backingOutline((function () {
			var $ret = new $.ig.Brush();
			$ret.fill("#B2B9C0");
			return $ret;
		}()));
		this.gauge().needlePivotBrush((function () {
			var $ret = new $.ig.Brush();
			$ret.fill("#CED3D8");
			return $ret;
		}()));
		this.gauge().needlePivotOutline((function () {
			var $ret = new $.ig.Brush();
			$ret.fill("#B2B9C0");
			return $ret;
		}()));
		this.gauge().minorTickBrush((function () {
			var $ret = new $.ig.Brush();
			$ret.fill("rgba(100,107,114,.74)");
			return $ret;
		}()));
		this.__ignoreBrushChanged = false;
	}
	,
	__scheduled: false,
	scheduleArrange: function () {
		if (!this.__scheduled) {
			this.__scheduled = true;
			window.setTimeout(this.arrange.runOn(this), 0);
		}
	}
	,
	arrange: function () {
		if (this.__scheduled) {
			this.__scheduled = false;
			this.gauge().arrangeGauge();
		}
	}
	,
	flush: function () {
		if (this.__scheduled) {
			this.arrange();
		}
	}
	,
	positionLabel: function (tb, x, y) {
		tb.canvasLeft(x);
		tb.canvasTop(y);
	}
	,
	_eventProxy: null,
	eventProxy: function (value) {
		if (arguments.length === 1) {
			this._eventProxy = value;
			return value;
		} else {
			return this._eventProxy;
		}
	}
	,
	_canvas: null,
	canvas: function (value) {
		if (arguments.length === 1) {
			this._canvas = value;
			return value;
		} else {
			return this._canvas;
		}
	}
	,
	_context: null,
	context: function (value) {
		if (arguments.length === 1) {
			this._context = value;
			return value;
		} else {
			return this._context;
		}
	}
	,
	_container: null,
	container: function (value) {
		if (arguments.length === 1) {
			this._container = value;
			return value;
		} else {
			return this._container;
		}
	}
	,
	__font: null,
	__fontBrush: null,
	onContainerProvided: function (container) {
		if (container == null) {
			this.canvas(this.container(null));
			this.context(null);
			this.cleanUpEventProxy();
			return;
		}
		var cont = $(container);
		this.container(cont);
		var width = Math.round(cont.width());
		var height = Math.round(cont.height());
		this.container().css("position", "relative");
		var canv = $("<canvas style='position:absolute'></canvas>");
		this.container().append(canv);
		this.__canvasWidth = $.ig.truncate(width);
		this.__canvasHeight = $.ig.truncate(height);
		canv.attr("width", width.toString());
		canv.attr("height", height.toString());
		if (this._cssSpan == null) {
			this._cssSpan = $("<span style='position:absolute;display:none' />");
		}
		this.container().append(this._cssSpan);
		this.gauge().viewport(new $.ig.Rect(0, 0, 0, width, height));
		this.canvas(canv);
		var context = (this.canvas()[0]).getContext("2d");
		this.context(new $.ig.RenderingContext(new $.ig.CanvasViewRenderer(), context));
		this.getDefaultColors();
		this.updateStyle();
		this.context().setFontInfo(this.__font);
		this.cleanUpEventProxy();
		this.eventProxy(new $.ig.DOMEventProxy(this.container()));
		var $t = this.eventProxy();
		$t.onContactStarted = $.ig.Delegate.prototype.combine($t.onContactStarted, this.eventProxy_OnContactStarted.runOn(this));
		var $t1 = this.eventProxy();
		$t1.onContactMoved = $.ig.Delegate.prototype.combine($t1.onContactMoved, this.eventProxy_OnContactMoved.runOn(this));
		var $t2 = this.eventProxy();
		$t2.onContactCompleted = $.ig.Delegate.prototype.combine($t2.onContactCompleted, this.eventProxy_OnContactCompleted.runOn(this));
		this.render();
	}
	,
	eventProxy_OnContactCompleted: function (point, isFinger) {
		if (this.gauge() != null) {
			this.gauge().onMouseUp(point);
		}
	}
	,
	eventProxy_OnContactMoved: function (point, isFinger) {
		if (this.gauge() != null) {
			this.gauge().onMouseMove(point, isFinger);
		}
	}
	,
	eventProxy_OnContactStarted: function (point, isFinger) {
		if (this.gauge() != null) {
			this.gauge().onMouseDown(point, false);
		}
	}
	,
	cleanUpEventProxy: function () {
		if (this.eventProxy() != null) {
			var $t = this.eventProxy();
			$t.onContactStarted = $.ig.Delegate.prototype.remove($t.onContactStarted, this.eventProxy_OnContactStarted.runOn(this));
			var $t1 = this.eventProxy();
			$t1.onContactMoved = $.ig.Delegate.prototype.remove($t1.onContactMoved, this.eventProxy_OnContactMoved.runOn(this));
			var $t2 = this.eventProxy();
			$t2.onContactCompleted = $.ig.Delegate.prototype.remove($t2.onContactCompleted, this.eventProxy_OnContactCompleted.runOn(this));
			this.eventProxy().destroy();
		}
	}
	,
	setTextBrush: function (tb, b) {
		if (b != null) {
			tb.fill(b);
		} else {
			tb.fill(this.__labelBrush);
		}
	}
	,
	styleUpdated: function () {
		this.getDefaultColors();
		this.updateStyle();
		this.scheduleArrange();
	}
	,
	updateStyle: function () {
		this.__font = $.ig.FontUtil.prototype.getFont(this.container());
		if (this.gauge().font() != null) {
			this.__font = this.gauge().getFontInfo();
		}
		this.__currentFontHeight = this.getCurrentFontHeight(this.__font);
		this.__fontBrush = new $.ig.Brush();
		this.__fontBrush.__fill = this.container().css("color");
		if (this.context() != null) {
			this.context().setFontInfo(this.__font);
		}
	}
	,
	arrangeComplete: function () {
		this.render();
	}
	,
	__labelBrush: null,
	render: function () {
		if (this.__canvasWidth != this.gauge().viewport().width() || this.__canvasHeight != this.gauge().viewport().height()) {
			this.canvas().attr("width", this.gauge().viewport().width().toString());
			this.canvas().attr("height", this.gauge().viewport().height().toString());
			this.__canvasWidth = $.ig.truncate(Math.round(this.gauge().viewport().width()));
			this.__canvasHeight = $.ig.truncate(Math.round(this.gauge().viewport().height()));
		}
		this.context().clearRectangle(this.gauge().viewport().left(), this.gauge().viewport().top(), this.gauge().viewport().width(), this.gauge().viewport().height());
		this.context().renderPath(this.__backingPath);
		this.context().renderPath(this.__scalePath);
		if (this.__rangesInOrder != null) {
			for (var i = 0; i < this.__rangesInOrder.count(); i++) {
				this.context().renderPath(this.__rangesInOrder.__inner[i]);
			}
		}
		for (var i1 = 0; i1 < this.activeLabels().count(); i1++) {
			this.context().setFontInfo(this.__font);
			this.context().renderTextBlock(this.activeLabels().__inner[i1]);
		}
		for (var i2 = 0; i2 < this.activePaths().count(); i2++) {
			this.context().renderPath(this.activePaths().__inner[i2]);
		}
		this.context().save();
		this.context().applyTransform(this.__needlePath.renderTransform());
		this.context().renderPath(this.__underlayPath);
		this.context().renderPath(this.__needlePath);
		this.context().renderPath(this.__overlayPath);
		this.context().restore();
	}
	,
	__currentFontHeight: 0,
	getCurrentFontHeight: function (font) {
		var tempSpan_ = $("<span>M</span>");
		var doc = $('body');
		doc.append(tempSpan_);
		if (font != null) {
			tempSpan_.css("font", font.fontString());
		}
		var offset_ = tempSpan_.attr("offsetHeight");
		if (isNaN(offset_)) { offset_ = tempSpan_[0].offsetHeight; };
		tempSpan_.remove();
		return parseInt(offset_);
	}
	,
	__rangesInOrder: null,
	setRangeRenderOrder: function (ranges) {
		this.__rangesInOrder = ranges;
	}
	,
	getBackingPath: function () {
		return this.__backingPath;
	}
	,
	getUnderlayPath: function () {
		return this.__underlayPath;
	}
	,
	getOverlayPath: function () {
		return this.__overlayPath;
	}
	,
	__canvasWidth: 0,
	__canvasHeight: 0,
	onContainerResized: function () {
		var width = Math.round(this.container().width());
		var height = Math.round(this.container().height());
		this.gauge().viewport(new $.ig.Rect(0, 0, 0, width, height));
		if (this.eventProxy() != null) {
			this.eventProxy().viewport(this.gauge().viewport());
		}
	}
	,
	exportViewShapes: function (gauge) {
		gauge.scalePath(new $.ig.PathVisualData(1, "Scale", this.__scalePath));
		gauge.needlePath(new $.ig.PathVisualData(1, "Needle", this.__needlePath));
		var needleAngle = ((this.__needlePath.renderTransform()).children().__inner[0]).angle();
		gauge.needle().angle(needleAngle);
		gauge.backingPath(new $.ig.PathVisualData(1, "Backing", this.__backingPath));
		gauge.overlayPath(new $.ig.PathVisualData(1, "Overlay", this.__overlayPath));
		gauge.underlayPath(new $.ig.PathVisualData(1, "Underlay", this.__underlayPath));
		var font = this.__font;
		var en = this.activeLabels().getEnumerator();
		while (en.moveNext()) {
			var label = en.current();
			if (label.__visibility == $.ig.Visibility.prototype.visible) {
				var fontInfo = $.ig.FontUtil.prototype.getFontInfo(label, font);
				var labelVisual = new $.ig.ScaleLabelVisualData();
				labelVisual.labelValue(label.text());
				labelVisual.labelPosition({ __x: label.canvasLeft(), __y: label.canvasTop(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				labelVisual.labelSize(this.getLabelSize(label));
				labelVisual.appearance($.ig.AppearanceHelper.prototype.fromTextElement(label, fontInfo));
				gauge.scaleLabels().add(labelVisual);
			}
		}
		var en1 = this.activePaths().getEnumerator();
		while (en1.moveNext()) {
			var path = en1.current();
			if (path.__visibility == $.ig.Visibility.prototype.visible) {
				var tickVisual = new $.ig.ScaleTickmarkVisualData();
				tickVisual.tickPath(new $.ig.PathVisualData(1, "tickmarks", path));
				gauge.scaleTickmarks().add(tickVisual);
			}
		}
		var en2 = this.activeRangePaths().getEnumerator();
		while (en2.moveNext()) {
			var path1 = en2.current();
			if (path1.__visibility == $.ig.Visibility.prototype.visible) {
				var rangeVisual = new $.ig.RangeVisualData();
				var rangePath = new $.ig.PathVisualData(1, "range", path1);
				rangeVisual.rangePath(rangePath);
				gauge.ranges().add(rangeVisual);
			}
		}
	}
	,
	__userNeedleBrush: null,
	__userNeedleOutline: null,
	__userNeedlePivotBrush: null,
	__userNeedlePivotOutline: null,
	__userBackingBrush: null,
	__userBackingOutline: null,
	__userScaleBrush: null,
	__userTickBrush: null,
	__userMinorTickBrush: null,
	onBrushChanged: function (propertyName, oldValue, newValue) {
		var oldBrush = oldValue;
		var newBrush = newValue;
		if (!this.__ignoreBrushChanged) {
			switch (propertyName) {
				case $.ig.XamRadialGauge.prototype.needleBrushPropertyName:
					this.__userNeedleBrush = newBrush;
					break;
				case $.ig.XamRadialGauge.prototype.needleOutlinePropertyName:
					this.__userNeedleOutline = newBrush;
					break;
				case $.ig.XamRadialGauge.prototype.needlePivotBrushPropertyName:
					this.__userNeedlePivotBrush = newBrush;
					break;
				case $.ig.XamRadialGauge.prototype.needlePivotOutlinePropertyName:
					this.__userNeedlePivotOutline = newBrush;
					break;
				case $.ig.XamRadialGauge.prototype.backingBrushPropertyName:
					this.__userBackingBrush = newBrush;
					break;
				case $.ig.XamRadialGauge.prototype.backingOutlinePropertyName:
					this.__userBackingOutline = newBrush;
					break;
				case $.ig.XamRadialGauge.prototype.scaleBrushPropertyName:
					this.__userScaleBrush = newBrush;
					break;
				case $.ig.XamRadialGauge.prototype.tickBrushPropertyName:
					this.__userTickBrush = newBrush;
					break;
				case $.ig.XamRadialGauge.prototype.minorTickBrushPropertyName:
					this.__userMinorTickBrush = newBrush;
					break;
			}
			if (newBrush == null) {
				this.updateBrushes();
			}
		}
	}
	,
	__userRangeBrushes: null,
	__userRangeOutlines: null,
	onRangeBrushesChanged: function (oldValue, newValue) {
		if (!this.__ignoreBrushChanged) {
			this.__userRangeBrushes = newValue;
			if (this.__userRangeBrushes == null) {
				this.gauge().rangeBrushes(this.__rangeBrushes);
			}
		}
	}
	,
	onRangeOutlinesChanged: function (oldValue, newValue) {
		if (!this.__ignoreBrushChanged) {
			this.__userRangeOutlines = newValue;
			if (this.__userRangeOutlines == null) {
				this.gauge().rangeOutlines(this.__rangeOutlines);
			}
		}
	}
	,
	$type: new $.ig.Type('XamRadialGaugeView', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('GaugeVisualData', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	_scalePath: null,
	scalePath: function (value) {
		if (arguments.length === 1) {
			this._scalePath = value;
			return value;
		} else {
			return this._scalePath;
		}
	}
	,
	_needlePath: null,
	needlePath: function (value) {
		if (arguments.length === 1) {
			this._needlePath = value;
			return value;
		} else {
			return this._needlePath;
		}
	}
	,
	_backingPath: null,
	backingPath: function (value) {
		if (arguments.length === 1) {
			this._backingPath = value;
			return value;
		} else {
			return this._backingPath;
		}
	}
	,
	_underlayPath: null,
	underlayPath: function (value) {
		if (arguments.length === 1) {
			this._underlayPath = value;
			return value;
		} else {
			return this._underlayPath;
		}
	}
	,
	_overlayPath: null,
	overlayPath: function (value) {
		if (arguments.length === 1) {
			this._overlayPath = value;
			return value;
		} else {
			return this._overlayPath;
		}
	}
	,
	_scale: null,
	scale: function (value) {
		if (arguments.length === 1) {
			this._scale = value;
			return value;
		} else {
			return this._scale;
		}
	}
	,
	_scaleLabels: null,
	scaleLabels: function (value) {
		if (arguments.length === 1) {
			this._scaleLabels = value;
			return value;
		} else {
			return this._scaleLabels;
		}
	}
	,
	_scaleTickmarks: null,
	scaleTickmarks: function (value) {
		if (arguments.length === 1) {
			this._scaleTickmarks = value;
			return value;
		} else {
			return this._scaleTickmarks;
		}
	}
	,
	_needle: null,
	needle: function (value) {
		if (arguments.length === 1) {
			this._needle = value;
			return value;
		} else {
			return this._needle;
		}
	}
	,
	_ranges: null,
	ranges: function (value) {
		if (arguments.length === 1) {
			this._ranges = value;
			return value;
		} else {
			return this._ranges;
		}
	}
	,
	_name: null,
	name: function (value) {
		if (arguments.length === 1) {
			this._name = value;
			return value;
		} else {
			return this._name;
		}
	}
	,
	scaleByViewport: function () {
		if (this.scalePath() != null) {
			this.scalePath().scaleByViewport(this.viewport());
		}
		if (this.needlePath() != null) {
			this.needlePath().scaleByViewport(this.viewport());
		}
		if (this.backingPath() != null) {
			this.backingPath().scaleByViewport(this.viewport());
		}
		if (this.underlayPath() != null) {
			this.underlayPath().scaleByViewport(this.viewport());
		}
		if (this.overlayPath() != null) {
			this.overlayPath().scaleByViewport(this.viewport());
		}
		if (this.scaleLabels() != null) {
			var en = this.scaleLabels().getEnumerator();
			while (en.moveNext()) {
				var label = en.current();
				label.scaleByViewport(this.viewport());
			}
		}
		if (this.scaleTickmarks() != null) {
			var en1 = this.scaleTickmarks().getEnumerator();
			while (en1.moveNext()) {
				var tickmark = en1.current();
				if (tickmark.tickPath() != null) {
					tickmark.tickPath().scaleByViewport(this.viewport());
				}
			}
		}
		if (this.ranges() != null) {
			var en2 = this.ranges().getEnumerator();
			while (en2.moveNext()) {
				var range = en2.current();
				if (range.rangePath() != null) {
					range.rangePath().scaleByViewport(this.viewport());
				}
			}
		}
	}
	,
	serialize: function () {
		var sb = new $.ig.StringBuilder(0);
		var first = true;
		sb.appendLine1("{");
		if (this.scalePath() != null) {
			if (first) {
				first = false;
			} else {
				sb.append5(", ");
			}
			sb.append5("scalePath: ");
			sb.appendLine1(this.scalePath().serialize());
		}
		if (this.needlePath() != null) {
			if (first) {
				first = false;
			} else {
				sb.append5(", ");
			}
			sb.append5("needlePath: ");
			sb.appendLine1(this.needlePath().serialize());
		}
		if (this.backingPath() != null) {
			if (first) {
				first = false;
			} else {
				sb.append5(", ");
			}
			sb.append5("backingPath: ");
			sb.appendLine1(this.backingPath().serialize());
		}
		if (this.underlayPath() != null) {
			if (first) {
				first = false;
			} else {
				sb.append5(", ");
			}
			sb.append5("underlayPath: ");
			sb.appendLine1(this.underlayPath().serialize());
		}
		if (this.overlayPath() != null) {
			if (first) {
				first = false;
			} else {
				sb.append5(", ");
			}
			sb.append5("overlayPath: ");
			sb.appendLine1(this.overlayPath().serialize());
		}
		if (this.scaleLabels() != null) {
			if (first) {
				first = false;
			} else {
				sb.append5(", ");
			}
			var subFirst = true;
			sb.append5("scaleLabels: [");
			var en = this.scaleLabels().getEnumerator();
			while (en.moveNext()) {
				var label = en.current();
				if (subFirst) {
					subFirst = false;
				} else {
					sb.append5(", ");
				}
				sb.appendLine1(label.serialize());
			}
			sb.appendLine1("]");
		}
		if (this.scaleTickmarks() != null) {
			if (first) {
				first = false;
			} else {
				sb.append5(", ");
			}
			var subFirst1 = true;
			sb.append5("scaleTickmarks: [");
			var en1 = this.scaleTickmarks().getEnumerator();
			while (en1.moveNext()) {
				var tickmark = en1.current();
				if (subFirst1) {
					subFirst1 = false;
				} else {
					sb.append5(", ");
				}
				sb.appendLine1(tickmark.serialize());
			}
			sb.appendLine1("]");
		}
		if (this.needle() != null) {
			if (first) {
				first = false;
			} else {
				sb.append5(", ");
			}
			sb.append5("needle: ");
			sb.appendLine1(this.needle().serialize());
		}
		if (this.ranges() != null) {
			if (first) {
				first = false;
			} else {
				sb.append5(", ");
			}
			var subFirst2 = true;
			sb.append5("ranges: [");
			var en2 = this.ranges().getEnumerator();
			while (en2.moveNext()) {
				var range = en2.current();
				if (subFirst2) {
					subFirst2 = false;
				} else {
					sb.append5(", ");
				}
				sb.appendLine1(range.serialize());
			}
			sb.appendLine1("]");
		}
		if (this.name() != null) {
			if (first) {
				first = false;
			} else {
				sb.append5(", ");
			}
			sb.append5("name: '");
			sb.append5(this.name());
			sb.appendLine1("'");
		}
		if (first) {
			first = false;
		} else {
			sb.append5(", ");
		}
		sb.append5("viewport: {");
		sb.append5("left: " + this.viewport().left() + ", top: " + this.viewport().top() + ", width: " + this.viewport().width() + ", height: " + this.viewport().height());
		sb.appendLine1("}");
		sb.appendLine1("}");
		return sb.toString();
	}
	,
	_viewport: null,
	viewport: function (value) {
		if (arguments.length === 1) {
			this._viewport = value;
			return value;
		} else {
			return this._viewport;
		}
	}
	,
	$type: new $.ig.Type('GaugeVisualData', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('ScaleVisualData', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	$type: new $.ig.Type('ScaleVisualData', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('ScaleLabelVisualData', 'Object', {
	init: function () {
		this._labelSize = new $.ig.Size();
		$.ig.Object.prototype.init.call(this);
	},
	_labelValue: null,
	labelValue: function (value) {
		if (arguments.length === 1) {
			this._labelValue = value;
			return value;
		} else {
			return this._labelValue;
		}
	}
	,
	_labelSize: null,
	labelSize: function (value) {
		if (arguments.length === 1) {
			this._labelSize = value;
			return value;
		} else {
			return this._labelSize;
		}
	}
	,
	_labelPosition: null,
	labelPosition: function (value) {
		if (arguments.length === 1) {
			this._labelPosition = value;
			return value;
		} else {
			return this._labelPosition;
		}
	}
	,
	_appearance: null,
	appearance: function (value) {
		if (arguments.length === 1) {
			this._appearance = value;
			return value;
		} else {
			return this._appearance;
		}
	}
	,
	scaleByViewport: function (viewport) {
		this.labelPosition({ __x: (this.labelPosition().__x - viewport.left()) / viewport.width(), __y: (this.labelPosition().__y - viewport.top()) / viewport.height(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		this.labelSize(new $.ig.Size(1, (this.labelSize().width()) / viewport.width(), (this.labelSize().height()) / viewport.height()));
	}
	,
	serialize: function () {
		return "{ labelValue: '" + this.labelValue() + "', labelSize: { width: " + this.labelSize().width() + ", height: " + this.labelSize().height() + "}, labelPosition: { x: " + this.labelPosition().__x + ", y: " + this.labelPosition().__y + "}, appearance: " + this.appearance().serialize() + "}";
	}
	,
	$type: new $.ig.Type('ScaleLabelVisualData', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('ScaleLabelVisualDataList', 'List$1', {
	init: function () {
		$.ig.List$1.prototype.init.call(this, $.ig.ScaleLabelVisualData.prototype.$type, 0);
	},
	$type: new $.ig.Type('ScaleLabelVisualDataList', $.ig.List$1.prototype.$type.specialize($.ig.ScaleLabelVisualData.prototype.$type))
}, true);

$.ig.util.defType('ScaleTickmarkVisualData', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	_tickPath: null,
	tickPath: function (value) {
		if (arguments.length === 1) {
			this._tickPath = value;
			return value;
		} else {
			return this._tickPath;
		}
	}
	,
	serialize: function () {
		return "{ tickPath: " + this.tickPath().serialize() + "}";
	}
	,
	$type: new $.ig.Type('ScaleTickmarkVisualData', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('ScaleTickmarkVisualDataList', 'List$1', {
	init: function () {
		$.ig.List$1.prototype.init.call(this, $.ig.ScaleTickmarkVisualData.prototype.$type, 0);
	},
	$type: new $.ig.Type('ScaleTickmarkVisualDataList', $.ig.List$1.prototype.$type.specialize($.ig.ScaleTickmarkVisualData.prototype.$type))
}, true);

$.ig.util.defType('NeedleVisualData', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	_angle: 0,
	angle: function (value) {
		if (arguments.length === 1) {
			this._angle = value;
			return value;
		} else {
			return this._angle;
		}
	}
	,
	serialize: function () {
		return "{ angle: " + this.angle().toString() + "}";
	}
	,
	$type: new $.ig.Type('NeedleVisualData', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('RangeVisualData', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	_rangePath: null,
	rangePath: function (value) {
		if (arguments.length === 1) {
			this._rangePath = value;
			return value;
		} else {
			return this._rangePath;
		}
	}
	,
	serialize: function () {
		return "{ rangePath: " + this.rangePath().serialize() + "}";
	}
	,
	$type: new $.ig.Type('RangeVisualData', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('RangeVisualDataList', 'List$1', {
	init: function () {
		$.ig.List$1.prototype.init.call(this, $.ig.RangeVisualData.prototype.$type, 0);
	},
	$type: new $.ig.Type('RangeVisualDataList', $.ig.List$1.prototype.$type.specialize($.ig.RangeVisualData.prototype.$type))
}, true);

$.ig.util.defType('RadialGaugeFrame', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
		this.needleFrame(new $.ig.RadialGaugeNeedleFrame());
		this.scaleFrame(new $.ig.RadialGaugeScaleFrame());
		this.backingFrame(new $.ig.BackingFrame());
	},
	_needleFrame: null,
	needleFrame: function (value) {
		if (arguments.length === 1) {
			this._needleFrame = value;
			return value;
		} else {
			return this._needleFrame;
		}
	}
	,
	_scaleFrame: null,
	scaleFrame: function (value) {
		if (arguments.length === 1) {
			this._scaleFrame = value;
			return value;
		} else {
			return this._scaleFrame;
		}
	}
	,
	_backingFrame: null,
	backingFrame: function (value) {
		if (arguments.length === 1) {
			this._backingFrame = value;
			return value;
		} else {
			return this._backingFrame;
		}
	}
	,
	_radiusMultiplier: 0,
	radiusMultiplier: function (value) {
		if (arguments.length === 1) {
			this._radiusMultiplier = value;
			return value;
		} else {
			return this._radiusMultiplier;
		}
	}
	,
	interpolate: function (p, previous, next) {
		this.radiusMultiplier(previous.radiusMultiplier() + (next.radiusMultiplier() - previous.radiusMultiplier()) * p);
		this.needleFrame().interpolate(p, previous.needleFrame(), next.needleFrame());
		this.scaleFrame().interpolate(p, previous.scaleFrame(), next.scaleFrame());
		this.backingFrame().interpolate(p, previous.backingFrame(), next.backingFrame());
	}
	,
	$type: new $.ig.Type('RadialGaugeFrame', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('RadialGaugeNeedleFrame', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
		this.baseToFeature(new $.ig.MorphSegment());
		this.baseFeature(new $.ig.MorphSegment());
		this.featureToCap(new $.ig.MorphSegment());
		this.cap(new $.ig.MorphSegment());
		this.capToFeature(new $.ig.MorphSegment());
		this.pointFeature(new $.ig.MorphSegment());
		this.featureToPoint(new $.ig.MorphSegment());
		this.point(new $.ig.MorphSegment());
		this.pointToFeatureUnderside(new $.ig.MorphSegment());
		this.pointFeatureUnderside(new $.ig.MorphSegment());
		this.featureToCapUnderside(new $.ig.MorphSegment());
		this.capUnderside(new $.ig.MorphSegment());
		this.capToFeatureUnderside(new $.ig.MorphSegment());
		this.baseFeatureUnderside(new $.ig.MorphSegment());
		this.featureToBaseUnderside(new $.ig.MorphSegment());
		this.base(new $.ig.MorphSegment());
		this.cutout(new $.ig.MorphSegment());
		this.cutoutUnderside(new $.ig.MorphSegment());
		this.circleOverlay(new $.ig.MorphSegment());
		this.circleOverlayUnderside(new $.ig.MorphSegment());
		this.circleUnderlay(new $.ig.MorphSegment());
		this.circleUnderlayUnderside(new $.ig.MorphSegment());
		this.circleOverlayCutout(new $.ig.MorphSegment());
		this.circleOverlayCutoutUnderside(new $.ig.MorphSegment());
		this.circleUnderlayCutout(new $.ig.MorphSegment());
		this.circleUnderlayCutoutUnderside(new $.ig.MorphSegment());
	},
	_needleStartExtent: 0,
	needleStartExtent: function (value) {
		if (arguments.length === 1) {
			this._needleStartExtent = value;
			return value;
		} else {
			return this._needleStartExtent;
		}
	}
	,
	_needleEndExtent: 0,
	needleEndExtent: function (value) {
		if (arguments.length === 1) {
			this._needleEndExtent = value;
			return value;
		} else {
			return this._needleEndExtent;
		}
	}
	,
	_needleWidth: 0,
	needleWidth: function (value) {
		if (arguments.length === 1) {
			this._needleWidth = value;
			return value;
		} else {
			return this._needleWidth;
		}
	}
	,
	_needleAngle: 0,
	needleAngle: function (value) {
		if (arguments.length === 1) {
			this._needleAngle = value;
			return value;
		} else {
			return this._needleAngle;
		}
	}
	,
	_needleBrush: null,
	needleBrush: function (value) {
		if (arguments.length === 1) {
			this._needleBrush = value;
			return value;
		} else {
			return this._needleBrush;
		}
	}
	,
	_needleOutline: null,
	needleOutline: function (value) {
		if (arguments.length === 1) {
			this._needleOutline = value;
			return value;
		} else {
			return this._needleOutline;
		}
	}
	,
	_capStrokeThickness: 0,
	capStrokeThickness: function (value) {
		if (arguments.length === 1) {
			this._capStrokeThickness = value;
			return value;
		} else {
			return this._capStrokeThickness;
		}
	}
	,
	_needleStrokeThickness: 0,
	needleStrokeThickness: function (value) {
		if (arguments.length === 1) {
			this._needleStrokeThickness = value;
			return value;
		} else {
			return this._needleStrokeThickness;
		}
	}
	,
	_capOutline: null,
	capOutline: function (value) {
		if (arguments.length === 1) {
			this._capOutline = value;
			return value;
		} else {
			return this._capOutline;
		}
	}
	,
	_capBrush: null,
	capBrush: function (value) {
		if (arguments.length === 1) {
			this._capBrush = value;
			return value;
		} else {
			return this._capBrush;
		}
	}
	,
	_baseToFeature: null,
	baseToFeature: function (value) {
		if (arguments.length === 1) {
			this._baseToFeature = value;
			return value;
		} else {
			return this._baseToFeature;
		}
	}
	,
	_baseFeature: null,
	baseFeature: function (value) {
		if (arguments.length === 1) {
			this._baseFeature = value;
			return value;
		} else {
			return this._baseFeature;
		}
	}
	,
	_featureToCap: null,
	featureToCap: function (value) {
		if (arguments.length === 1) {
			this._featureToCap = value;
			return value;
		} else {
			return this._featureToCap;
		}
	}
	,
	_cap: null,
	cap: function (value) {
		if (arguments.length === 1) {
			this._cap = value;
			return value;
		} else {
			return this._cap;
		}
	}
	,
	_capToFeature: null,
	capToFeature: function (value) {
		if (arguments.length === 1) {
			this._capToFeature = value;
			return value;
		} else {
			return this._capToFeature;
		}
	}
	,
	_pointFeature: null,
	pointFeature: function (value) {
		if (arguments.length === 1) {
			this._pointFeature = value;
			return value;
		} else {
			return this._pointFeature;
		}
	}
	,
	_featureToPoint: null,
	featureToPoint: function (value) {
		if (arguments.length === 1) {
			this._featureToPoint = value;
			return value;
		} else {
			return this._featureToPoint;
		}
	}
	,
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
	_pointToFeatureUnderside: null,
	pointToFeatureUnderside: function (value) {
		if (arguments.length === 1) {
			this._pointToFeatureUnderside = value;
			return value;
		} else {
			return this._pointToFeatureUnderside;
		}
	}
	,
	_pointFeatureUnderside: null,
	pointFeatureUnderside: function (value) {
		if (arguments.length === 1) {
			this._pointFeatureUnderside = value;
			return value;
		} else {
			return this._pointFeatureUnderside;
		}
	}
	,
	_featureToCapUnderside: null,
	featureToCapUnderside: function (value) {
		if (arguments.length === 1) {
			this._featureToCapUnderside = value;
			return value;
		} else {
			return this._featureToCapUnderside;
		}
	}
	,
	_capUnderside: null,
	capUnderside: function (value) {
		if (arguments.length === 1) {
			this._capUnderside = value;
			return value;
		} else {
			return this._capUnderside;
		}
	}
	,
	_capToFeatureUnderside: null,
	capToFeatureUnderside: function (value) {
		if (arguments.length === 1) {
			this._capToFeatureUnderside = value;
			return value;
		} else {
			return this._capToFeatureUnderside;
		}
	}
	,
	_baseFeatureUnderside: null,
	baseFeatureUnderside: function (value) {
		if (arguments.length === 1) {
			this._baseFeatureUnderside = value;
			return value;
		} else {
			return this._baseFeatureUnderside;
		}
	}
	,
	_featureToBaseUnderside: null,
	featureToBaseUnderside: function (value) {
		if (arguments.length === 1) {
			this._featureToBaseUnderside = value;
			return value;
		} else {
			return this._featureToBaseUnderside;
		}
	}
	,
	_base: null,
	base: function (value) {
		if (arguments.length === 1) {
			this._base = value;
			return value;
		} else {
			return this._base;
		}
	}
	,
	_cutout: null,
	cutout: function (value) {
		if (arguments.length === 1) {
			this._cutout = value;
			return value;
		} else {
			return this._cutout;
		}
	}
	,
	_cutoutUnderside: null,
	cutoutUnderside: function (value) {
		if (arguments.length === 1) {
			this._cutoutUnderside = value;
			return value;
		} else {
			return this._cutoutUnderside;
		}
	}
	,
	_circleOverlay: null,
	circleOverlay: function (value) {
		if (arguments.length === 1) {
			this._circleOverlay = value;
			return value;
		} else {
			return this._circleOverlay;
		}
	}
	,
	_circleOverlayUnderside: null,
	circleOverlayUnderside: function (value) {
		if (arguments.length === 1) {
			this._circleOverlayUnderside = value;
			return value;
		} else {
			return this._circleOverlayUnderside;
		}
	}
	,
	_circleUnderlay: null,
	circleUnderlay: function (value) {
		if (arguments.length === 1) {
			this._circleUnderlay = value;
			return value;
		} else {
			return this._circleUnderlay;
		}
	}
	,
	_circleUnderlayUnderside: null,
	circleUnderlayUnderside: function (value) {
		if (arguments.length === 1) {
			this._circleUnderlayUnderside = value;
			return value;
		} else {
			return this._circleUnderlayUnderside;
		}
	}
	,
	_circleOverlayCutout: null,
	circleOverlayCutout: function (value) {
		if (arguments.length === 1) {
			this._circleOverlayCutout = value;
			return value;
		} else {
			return this._circleOverlayCutout;
		}
	}
	,
	_circleOverlayCutoutUnderside: null,
	circleOverlayCutoutUnderside: function (value) {
		if (arguments.length === 1) {
			this._circleOverlayCutoutUnderside = value;
			return value;
		} else {
			return this._circleOverlayCutoutUnderside;
		}
	}
	,
	_circleUnderlayCutout: null,
	circleUnderlayCutout: function (value) {
		if (arguments.length === 1) {
			this._circleUnderlayCutout = value;
			return value;
		} else {
			return this._circleUnderlayCutout;
		}
	}
	,
	_circleUnderlayCutoutUnderside: null,
	circleUnderlayCutoutUnderside: function (value) {
		if (arguments.length === 1) {
			this._circleUnderlayCutoutUnderside = value;
			return value;
		} else {
			return this._circleUnderlayCutoutUnderside;
		}
	}
	,
	interpolate: function (p, previous, next) {
		this.needleStartExtent(previous.needleStartExtent() + (next.needleStartExtent() - previous.needleStartExtent()) * p);
		this.needleEndExtent(previous.needleEndExtent() + (next.needleEndExtent() - previous.needleEndExtent()) * p);
		this.needleWidth(previous.needleWidth() + (next.needleWidth() - previous.needleWidth()) * p);
		this.needleAngle(previous.needleAngle() + (next.needleAngle() - previous.needleAngle()) * p);
		this.needleBrush($.ig.BrushUtil.prototype.getInterpolation(previous.needleBrush(), p, next.needleBrush(), $.ig.InterpolationMode.prototype.rGB));
		this.needleOutline($.ig.BrushUtil.prototype.getInterpolation(previous.needleOutline(), p, next.needleOutline(), $.ig.InterpolationMode.prototype.rGB));
		this.needleStrokeThickness(previous.needleStrokeThickness() + (next.needleStrokeThickness() - previous.needleStrokeThickness()) * p);
		this.capStrokeThickness(previous.capStrokeThickness() + (next.capStrokeThickness() - previous.capStrokeThickness()) * p);
		this.capBrush($.ig.BrushUtil.prototype.getInterpolation(previous.capBrush(), p, next.capBrush(), $.ig.InterpolationMode.prototype.rGB));
		this.capOutline($.ig.BrushUtil.prototype.getInterpolation(previous.capOutline(), p, next.capOutline(), $.ig.InterpolationMode.prototype.rGB));
		this.baseToFeature().interpolateSegment(p, previous.baseToFeature(), next.baseToFeature());
		this.baseFeature().interpolateSegment(p, previous.baseFeature(), next.baseFeature());
		this.featureToCap().interpolateSegment(p, previous.featureToCap(), next.featureToCap());
		this.cap().interpolateSegment(p, previous.cap(), next.cap());
		this.capToFeature().interpolateSegment(p, previous.capToFeature(), next.capToFeature());
		this.pointFeature().interpolateSegment(p, previous.pointFeature(), next.pointFeature());
		this.featureToPoint().interpolateSegment(p, previous.featureToPoint(), next.featureToPoint());
		this.point().interpolateSegment(p, previous.point(), next.point());
		this.pointToFeatureUnderside().interpolateSegment(p, previous.pointToFeatureUnderside(), next.pointToFeatureUnderside());
		this.pointFeatureUnderside().interpolateSegment(p, previous.pointFeatureUnderside(), next.pointFeatureUnderside());
		this.featureToCapUnderside().interpolateSegment(p, previous.featureToCapUnderside(), next.featureToCapUnderside());
		this.capUnderside().interpolateSegment(p, previous.capUnderside(), next.capUnderside());
		this.capToFeatureUnderside().interpolateSegment(p, previous.capToFeatureUnderside(), next.capToFeatureUnderside());
		this.baseFeatureUnderside().interpolateSegment(p, previous.baseFeatureUnderside(), next.baseFeatureUnderside());
		this.featureToBaseUnderside().interpolateSegment(p, previous.featureToBaseUnderside(), next.featureToBaseUnderside());
		this.base().interpolateSegment(p, previous.base(), next.base());
		this.cutout().interpolateSegment(p, previous.cutout(), next.cutout());
		this.cutoutUnderside().interpolateSegment(p, previous.cutoutUnderside(), next.cutoutUnderside());
		this.circleOverlay().interpolateSegment(p, previous.circleOverlay(), next.circleOverlay());
		this.circleOverlayUnderside().interpolateSegment(p, previous.circleOverlayUnderside(), next.circleOverlayUnderside());
		this.circleUnderlay().interpolateSegment(p, previous.circleUnderlay(), next.circleUnderlay());
		this.circleUnderlayUnderside().interpolateSegment(p, previous.circleUnderlayUnderside(), next.circleUnderlayUnderside());
		this.circleOverlayCutout().interpolateSegment(p, previous.circleOverlayCutout(), next.circleOverlayCutout());
		this.circleOverlayCutoutUnderside().interpolateSegment(p, previous.circleOverlayCutoutUnderside(), next.circleOverlayCutoutUnderside());
		this.circleUnderlayCutout().interpolateSegment(p, previous.circleUnderlayCutout(), next.circleUnderlayCutout());
		this.circleUnderlayCutoutUnderside().interpolateSegment(p, previous.circleUnderlayCutoutUnderside(), next.circleUnderlayCutoutUnderside());
	}
	,
	getPathGeometry: function (radius, centerX, centerY) {
		var pg = new $.ig.PathGeometry();
		var pf = new $.ig.PathFigure();
		var lastPoint = { __x: NaN, __y: NaN, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		lastPoint = this.baseToFeature().renderSegment(lastPoint, pf, radius, centerX, centerY);
		lastPoint = this.baseFeature().renderSegment(lastPoint, pf, radius, centerX, centerY);
		lastPoint = this.featureToCap().renderSegment(lastPoint, pf, radius, centerX, centerY);
		lastPoint = this.cap().renderSegment(lastPoint, pf, radius, centerX, centerY);
		lastPoint = this.capToFeature().renderSegment(lastPoint, pf, radius, centerX, centerY);
		lastPoint = this.pointFeature().renderSegment(lastPoint, pf, radius, centerX, centerY);
		lastPoint = this.featureToPoint().renderSegment(lastPoint, pf, radius, centerX, centerY);
		lastPoint = this.point().renderSegment(lastPoint, pf, radius, centerX, centerY);
		lastPoint = this.pointToFeatureUnderside().renderSegment(lastPoint, pf, radius, centerX, centerY);
		lastPoint = this.pointFeatureUnderside().renderSegment(lastPoint, pf, radius, centerX, centerY);
		lastPoint = this.featureToCapUnderside().renderSegment(lastPoint, pf, radius, centerX, centerY);
		lastPoint = this.capUnderside().renderSegment(lastPoint, pf, radius, centerX, centerY);
		lastPoint = this.capToFeatureUnderside().renderSegment(lastPoint, pf, radius, centerX, centerY);
		lastPoint = this.baseFeatureUnderside().renderSegment(lastPoint, pf, radius, centerX, centerY);
		lastPoint = this.featureToBaseUnderside().renderSegment(lastPoint, pf, radius, centerX, centerY);
		lastPoint = this.base().renderSegment(lastPoint, pf, radius, centerX, centerY);
		pf.__isClosed = true;
		pg.figures().add(pf);
		var cutoutFigure = new $.ig.PathFigure();
		if (this.cutout()._segmentPoints.count() > 0 || this.cutoutUnderside()._segmentPoints.count() > 0) {
			lastPoint = this.cutout().renderSegment(lastPoint, cutoutFigure, radius, centerX, centerY);
			lastPoint = this.cutoutUnderside().renderSegment(lastPoint, cutoutFigure, radius, centerX, centerY);
			cutoutFigure.__isClosed = true;
			pg.figures().add(cutoutFigure);
		}
		return pg;
	}
	,
	getUnderlayPathGeometry: function (radius, centerX, centerY) {
		var ret = new $.ig.PathGeometry();
		var lastPoint = { __x: NaN, __y: NaN, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		var pf = new $.ig.PathFigure();
		if (this.circleUnderlay()._segmentPoints.count() > 0 || this.circleUnderlayUnderside()._segmentPoints.count() > 0) {
			lastPoint = this.circleUnderlay().renderSegment(lastPoint, pf, radius, centerX, centerY);
			lastPoint = this.circleUnderlayUnderside().renderSegment(lastPoint, pf, radius, centerX, centerY);
			pf.__isClosed = true;
			ret.figures().add(pf);
		}
		var cutoutFigure = new $.ig.PathFigure();
		if (this.circleUnderlayCutout()._segmentPoints.count() > 0 || this.circleUnderlayCutoutUnderside()._segmentPoints.count() > 0) {
			lastPoint = this.circleUnderlayCutout().renderSegment(lastPoint, cutoutFigure, radius, centerX, centerY);
			lastPoint = this.circleUnderlayCutoutUnderside().renderSegment(lastPoint, cutoutFigure, radius, centerX, centerY);
			cutoutFigure.__isClosed = true;
			ret.figures().add(cutoutFigure);
		}
		return ret;
	}
	,
	getOverlayPathGeometry: function (radius, centerX, centerY) {
		var ret = new $.ig.PathGeometry();
		var lastPoint = { __x: NaN, __y: NaN, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		var pf = new $.ig.PathFigure();
		if (this.circleOverlay()._segmentPoints.count() > 0 || this.circleOverlayUnderside()._segmentPoints.count() > 0) {
			lastPoint = this.circleOverlay().renderSegment(lastPoint, pf, radius, centerX, centerY);
			lastPoint = this.circleOverlayUnderside().renderSegment(lastPoint, pf, radius, centerX, centerY);
			pf.__isClosed = true;
			ret.figures().add(pf);
		}
		var cutoutFigure = new $.ig.PathFigure();
		if (this.circleOverlayCutout()._segmentPoints.count() > 0 || this.circleOverlayCutoutUnderside()._segmentPoints.count() > 0) {
			lastPoint = this.circleOverlayCutout().renderSegment(lastPoint, cutoutFigure, radius, centerX, centerY);
			lastPoint = this.circleOverlayCutoutUnderside().renderSegment(lastPoint, cutoutFigure, radius, centerX, centerY);
			cutoutFigure.__isClosed = true;
			ret.figures().add(cutoutFigure);
		}
		return ret;
	}
	,
	$type: new $.ig.Type('RadialGaugeNeedleFrame', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('RadialGaugeRangeFrame', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	_startInnerExtent: 0,
	_startOuterExtent: 0,
	_startAngle: 0,
	_endAngle: 0,
	_endInnerExtent: 0,
	_endOuterExtent: 0,
	_brush: null,
	_outline: null,
	_strokeThickness: 0,
	interpolate: function (p, q, min, max) {
		var ret = new $.ig.RadialGaugeRangeFrame();
		ret._brush = $.ig.BrushUtil.prototype.getInterpolation(min._brush, p, max._brush, $.ig.InterpolationMode.prototype.rGB);
		ret._outline = $.ig.BrushUtil.prototype.getInterpolation(min._outline, p, max._outline, $.ig.InterpolationMode.prototype.rGB);
		ret._strokeThickness = q * min._strokeThickness + p * max._strokeThickness;
		ret._startInnerExtent = q * min._startInnerExtent + p * max._startInnerExtent;
		ret._startOuterExtent = q * min._startOuterExtent + p * max._startOuterExtent;
		ret._startAngle = q * min._startAngle + p * max._startAngle;
		ret._endAngle = q * min._endAngle + p * max._endAngle;
		ret._endInnerExtent = q * min._endInnerExtent + p * max._endInnerExtent;
		ret._endOuterExtent = q * min._endOuterExtent + p * max._endOuterExtent;
		return ret;
	}
	,
	getPathGeometry: function (radius, centerX, centerY) {
		var pg = new $.ig.PathGeometry();
		var pf = new $.ig.PathFigure();
		var resolution = 4;
		var arcThresh = 1 / radius;
		var useOuterArc = false;
		if (Math.abs(this._startOuterExtent - this._endOuterExtent) < arcThresh) {
			useOuterArc = true;
		}
		var useInnerArc = false;
		if (Math.abs(this._startInnerExtent - this._endInnerExtent) < arcThresh) {
			useInnerArc = true;
		}
		var doCircle = false;
		if ((this._endAngle - this._startAngle) >= 2 * Math.PI) {
			doCircle = true;
		}
		if (useInnerArc && useOuterArc && doCircle) {
			var seg1 = this.getArc(0, Math.PI, this._startOuterExtent, radius, centerX, centerY, true);
			pf.__startPoint = seg1.item1();
			var seg2 = this.getArc(Math.PI, 0, this._startOuterExtent, radius, centerX, centerY, true);
			var pf2 = new $.ig.PathFigure();
			var seg3 = this.getArc(0, Math.PI, this._startInnerExtent, radius, centerX, centerY, false);
			pf2.__startPoint = seg3.item1();
			var seg4 = this.getArc(Math.PI, 0, this._startInnerExtent, radius, centerX, centerY, false);
			pf.__segments.add(seg1.item2());
			pf.__segments.add(seg2.item2());
			pf2.__segments.add(seg3.item2());
			pf2.__segments.add(seg4.item2());
			pg.figures().add(pf);
			pg.figures().add(pf2);
			return pg;
		}
		var outerSeg = null;
		if (useOuterArc) {
			var res = this.getArc(this._startAngle, this._endAngle, this._startOuterExtent, radius, centerX, centerY, true);
			pf.__startPoint = res.item1();
			outerSeg = res.item2();
		} else {
			var res1 = this.getRasterArc(this._startAngle, this._endAngle, this._startOuterExtent, this._endOuterExtent, radius, centerX, centerY, true, resolution);
			pf.__startPoint = res1.item1();
			outerSeg = res1.item2();
		}
		var innerSeg = null;
		var lineSeg = new $.ig.LineSegment(1);
		if (useInnerArc) {
			var res2 = this.getArc(this._startAngle, this._endAngle, this._startInnerExtent, radius, centerX, centerY, false);
			innerSeg = res2.item2();
			lineSeg.point(res2.item1());
		} else {
			var res3 = this.getRasterArc(this._startAngle, this._endAngle, this._startInnerExtent, this._endInnerExtent, radius, centerX, centerY, false, resolution);
			innerSeg = res3.item2();
			lineSeg.point(res3.item1());
		}
		pf.__segments.add(outerSeg);
		pf.__segments.add(lineSeg);
		pf.__segments.add(innerSeg);
		pf.__isFilled = true;
		pf.__isClosed = true;
		pg.figures().add(pf);
		return pg;
	}
	,
	getRasterArc: function (startingAngle, endingAngle, startExtent, endExtent, radius, centerX, centerY, isClockwise, resolution) {
		var seg = new $.ig.PolyLineSegment();
		var startExt = radius * startExtent;
		var endExt = radius * endExtent;
		var maxExt = Math.max(startExt, endExt);
		var diameter = maxExt + maxExt;
		var angleStep = Math.asin(resolution / diameter) * 2;
		var p;
		var rad;
		var inter;
		var extentRange = endExt - startExt;
		var angleRange = Math.abs(endingAngle - startingAngle);
		var pts = seg.__points;
		if (isClockwise) {
			var currAngle;
			for (currAngle = startingAngle; currAngle < endingAngle; currAngle += angleStep) {
				inter = (currAngle - startingAngle) / angleRange;
				rad = startExt + inter * extentRange;
				p = { __x: centerX + Math.cos(currAngle) * rad, __y: centerY + Math.sin(currAngle) * rad, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				pts.add(p);
			}
			currAngle = endingAngle;
			inter = (currAngle - startingAngle) / angleRange;
			rad = startExt + inter * extentRange;
			p = { __x: centerX + Math.cos(currAngle) * rad, __y: centerY + Math.sin(currAngle) * rad, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			pts.add(p);
		} else {
			var currAngle1;
			for (currAngle1 = endingAngle; currAngle1 > startingAngle; currAngle1 -= angleStep) {
				inter = (currAngle1 - endingAngle) / angleRange;
				rad = startExt + inter * extentRange;
				p = { __x: centerX + Math.cos(currAngle1) * rad, __y: centerY + Math.sin(currAngle1) * rad, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				pts.add(p);
			}
			currAngle1 = startingAngle;
			inter = (currAngle1 - endingAngle) / angleRange;
			rad = startExt + inter * extentRange;
			p = { __x: centerX + Math.cos(currAngle1) * rad, __y: centerY + Math.sin(currAngle1) * rad, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			pts.add(p);
		}
		return new $.ig.Tuple$2($.ig.Point.prototype.$type, $.ig.PathSegment.prototype.$type, pts.__inner[0], seg);
	}
	,
	getArc: function (startingAngle, endingAngle, extent, radius, centerX, centerY, isClockwise) {
		var ext = extent * radius;
		var arc = new $.ig.ArcSegment();
		arc.sweepDirection(isClockwise ? $.ig.SweepDirection.prototype.clockwise : $.ig.SweepDirection.prototype.counterclockwise);
		arc.isLargeArc(endingAngle - startingAngle > Math.PI);
		arc.size(new $.ig.Size(1, ext, ext));
		var end = isClockwise ? endingAngle : startingAngle;
		var start = isClockwise ? startingAngle : endingAngle;
		arc.point({ __x: centerX + Math.cos(end) * ext, __y: centerY + Math.sin(end) * ext, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		var startPoint = { __x: centerX + Math.cos(start) * ext, __y: centerY + Math.sin(start) * ext, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		return new $.ig.Tuple$2($.ig.Point.prototype.$type, $.ig.PathSegment.prototype.$type, startPoint, arc);
	}
	,
	$type: new $.ig.Type('RadialGaugeRangeFrame', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('BackingFrame', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	_brush: null,
	brush: function (value) {
		if (arguments.length === 1) {
			this._brush = value;
			return value;
		} else {
			return this._brush;
		}
	}
	,
	_outline: null,
	outline: function (value) {
		if (arguments.length === 1) {
			this._outline = value;
			return value;
		} else {
			return this._outline;
		}
	}
	,
	_strokeThickness: 0,
	strokeThickness: function (value) {
		if (arguments.length === 1) {
			this._strokeThickness = value;
			return value;
		} else {
			return this._strokeThickness;
		}
	}
	,
	_startAngle: 0,
	startAngle: function (value) {
		if (arguments.length === 1) {
			this._startAngle = value;
			return value;
		} else {
			return this._startAngle;
		}
	}
	,
	_endAngle: 0,
	endAngle: function (value) {
		if (arguments.length === 1) {
			this._endAngle = value;
			return value;
		} else {
			return this._endAngle;
		}
	}
	,
	_oversweep: 0,
	oversweep: function (value) {
		if (arguments.length === 1) {
			this._oversweep = value;
			return value;
		} else {
			return this._oversweep;
		}
	}
	,
	_outerExtent: 0,
	outerExtent: function (value) {
		if (arguments.length === 1) {
			this._outerExtent = value;
			return value;
		} else {
			return this._outerExtent;
		}
	}
	,
	_innerExtent: 0,
	innerExtent: function (value) {
		if (arguments.length === 1) {
			this._innerExtent = value;
			return value;
		} else {
			return this._innerExtent;
		}
	}
	,
	_cornerRadius: 0,
	cornerRadius: function (value) {
		if (arguments.length === 1) {
			this._cornerRadius = value;
			return value;
		} else {
			return this._cornerRadius;
		}
	}
	,
	interpolate: function (p, previous, next) {
		this.strokeThickness(previous.strokeThickness() + (next.strokeThickness() - previous.strokeThickness()) * p);
		this.brush($.ig.BrushUtil.prototype.getInterpolation(previous.brush(), p, next.brush(), $.ig.InterpolationMode.prototype.rGB));
		this.outline($.ig.BrushUtil.prototype.getInterpolation(previous.outline(), p, next.outline(), $.ig.InterpolationMode.prototype.rGB));
		this.startAngle(previous.startAngle() + (next.startAngle() - previous.startAngle()) * p);
		this.endAngle(previous.endAngle() + (next.endAngle() - previous.endAngle()) * p);
		this.outerExtent(previous.outerExtent() + (next.outerExtent() - previous.outerExtent()) * p);
		this.innerExtent(previous.innerExtent() + (next.innerExtent() - previous.innerExtent()) * p);
		this.cornerRadius(previous.cornerRadius() + (next.cornerRadius() - previous.cornerRadius()) * p);
		this.oversweep(previous.oversweep() + (next.oversweep() - previous.oversweep()) * p);
	}
	,
	doCircle: function (pf, radius, centerX, centerY) {
		var $self = this;
		pf.__startPoint = { __x: radius * this.outerExtent() + centerX, __y: 0 + centerY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		var as1 = (function () {
			var $ret = new $.ig.ArcSegment();
			$ret.point({ __x: -1 * radius * $self.outerExtent() + centerX, __y: 0 + centerY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			$ret.isLargeArc(false);
			$ret.sweepDirection($.ig.SweepDirection.prototype.clockwise);
			$ret.size(new $.ig.Size(1, radius * $self.outerExtent(), radius * $self.outerExtent()));
			return $ret;
		}());
		var as2 = (function () {
			var $ret = new $.ig.ArcSegment();
			$ret.point({ __x: radius * $self.outerExtent() + centerX, __y: 0 + centerY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			$ret.isLargeArc(false);
			$ret.sweepDirection($.ig.SweepDirection.prototype.clockwise);
			$ret.size(new $.ig.Size(1, radius * $self.outerExtent(), radius * $self.outerExtent()));
			return $ret;
		}());
		pf.__segments.add(as1);
		pf.__segments.add(as2);
	}
	,
	getPathGeometry: function (radius, centerX, centerY) {
		var pg = new $.ig.PathGeometry();
		var pf = new $.ig.PathFigure();
		if (this.startAngle() == this.endAngle()) {
			this.doCircle(pf, radius, centerX, centerY);
		} else {
			var outerExtent = this.outerExtent();
			var innerExtent = this.innerExtent();
			var startAngle = this.startAngle();
			var endAngle = this.endAngle();
			var oversweep = this.oversweep();
			var origStartAngle = startAngle;
			var origEndAngle = endAngle;
			startAngle -= oversweep;
			endAngle += oversweep;
			if ((endAngle - startAngle) > 2 * Math.PI) {
				this.doCircle(pf, radius, centerX, centerY);
			} else {
				this.doFitted(pf, radius, centerX, centerY);
			}
		}
		pf.__isClosed = true;
		pg.figures().add(pf);
		return pg;
	}
	,
	closeEnough: function (value1, value2) {
		return Math.abs(value1 - value2) < 1E-05;
	}
	,
	doFitted: function (pf, radius, centerX, centerY) {
		var outerExtent = this.outerExtent();
		var innerExtent = this.innerExtent();
		var startAngle = this.startAngle();
		var endAngle = this.endAngle();
		var oversweep = this.oversweep();
		var origStartAngle = startAngle;
		var origEndAngle = endAngle;
		startAngle -= oversweep;
		endAngle += oversweep;
		var sinStart = Math.sin(startAngle);
		var cosStart = Math.cos(startAngle);
		var sinEnd = Math.sin(endAngle);
		var cosEnd = Math.cos(endAngle);
		var cornerRadius = this.cornerRadius();
		var outerRadius = radius * outerExtent;
		var innerRadius = radius * innerExtent;
		if (cornerRadius * 2 > (outerRadius - innerRadius)) {
			cornerRadius = (outerRadius - innerRadius) / 2;
		}
		var oppositeOverAdjacent = cornerRadius / (radius - cornerRadius);
		var angleDiff = Math.atan(oppositeOverAdjacent);
		var roundedOuterRadius = outerRadius - cornerRadius;
		var roundedInnerRadius = innerRadius + cornerRadius;
		var sinStartMinusDiff = Math.sin(startAngle - angleDiff);
		var sinStartPlusDiff = Math.sin(startAngle + angleDiff);
		var cosStartMinusDiff = Math.cos(startAngle - angleDiff);
		var cosStartPlusDiff = Math.cos(startAngle + angleDiff);
		var sinEndMinusDiff = Math.sin(endAngle - angleDiff);
		var sinEndPlusDiff = Math.sin(endAngle + angleDiff);
		var cosEndMinusDiff = Math.cos(endAngle - angleDiff);
		var cosEndPlusDiff = Math.cos(endAngle + angleDiff);
		var sinOrigStart = Math.sin(origStartAngle);
		var sinOrigEnd = Math.sin(origEndAngle);
		var cosOrigStart = Math.cos(origStartAngle);
		var cosOrigEnd = Math.cos(origEndAngle);
		pf.__startPoint = { __x: cosStart * roundedOuterRadius + centerX, __y: sinStart * roundedOuterRadius + centerY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		var as1 = (function () {
			var $ret = new $.ig.ArcSegment();
			$ret.point({ __x: cosStartPlusDiff * outerRadius + centerX, __y: sinStartPlusDiff * outerRadius + centerY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			$ret.isLargeArc((angleDiff * 2) > Math.PI);
			$ret.sweepDirection($.ig.SweepDirection.prototype.clockwise);
			$ret.size(new $.ig.Size(1, cornerRadius, cornerRadius));
			return $ret;
		}());
		var as2 = (function () {
			var $ret = new $.ig.ArcSegment();
			$ret.point({ __x: cosEndMinusDiff * outerRadius + centerX, __y: sinEndMinusDiff * outerRadius + centerY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			$ret.isLargeArc(((endAngle - angleDiff) - (startAngle + angleDiff)) > Math.PI);
			$ret.sweepDirection($.ig.SweepDirection.prototype.clockwise);
			$ret.size(new $.ig.Size(1, outerRadius, outerRadius));
			return $ret;
		}());
		var as3 = (function () {
			var $ret = new $.ig.ArcSegment();
			$ret.point({ __x: cosEnd * roundedOuterRadius + centerX, __y: sinEnd * roundedOuterRadius + centerY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			$ret.isLargeArc((angleDiff * 2) > Math.PI);
			$ret.sweepDirection($.ig.SweepDirection.prototype.clockwise);
			$ret.size(new $.ig.Size(1, cornerRadius, cornerRadius));
			return $ret;
		}());
		var origOuterPoint = { __x: cosOrigEnd * outerRadius + centerX, __y: sinOrigEnd * outerRadius + centerY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		var origInnerPoint = { __x: cosOrigEnd * innerRadius + centerX, __y: sinOrigEnd * innerRadius + centerY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		var newOuterPoint = { __x: cosEnd * outerRadius + centerX, __y: sinEnd * outerRadius + centerY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		var newInnerPoint = { __x: cosEnd * innerRadius + centerX, __y: sinEnd * innerRadius + centerY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		var matchingInnerY = newOuterPoint.__y;
		var matchingInnerX = newOuterPoint.__x;
		if (origOuterPoint.__x - origInnerPoint.__x != 0) {
			matchingInnerX = centerX;
			var m = (origOuterPoint.__y - origInnerPoint.__y) / (origOuterPoint.__x - origInnerPoint.__x);
			matchingInnerY = m * (centerX - newOuterPoint.__x) + newOuterPoint.__y;
		}
		newInnerPoint = { __x: centerX, __y: matchingInnerY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		if (this.closeEnough(newInnerPoint.__x, newOuterPoint.__x) && this.closeEnough(newInnerPoint.__y, newOuterPoint.__y)) {
			newInnerPoint = origInnerPoint;
		}
		var inter = $.ig.GeometryUtil.prototype.getCircleIntersection(newInnerPoint, newOuterPoint, { __x: centerX, __y: centerY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }, innerRadius);
		var origOuterPoint2 = { __x: cosOrigStart * outerRadius + centerX, __y: sinOrigStart * outerRadius + centerY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		var origInnerPoint2 = { __x: cosOrigStart * innerRadius + centerX, __y: sinOrigStart * innerRadius + centerY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		var newOuterPoint2 = { __x: cosStart * outerRadius + centerX, __y: sinStart * outerRadius + centerY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		var newInnerPoint2 = { __x: cosStart * innerRadius + centerX, __y: sinStart * innerRadius + centerY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		var matchingInnerY2 = newOuterPoint2.__y;
		var matchingInnerX2 = newOuterPoint2.__x;
		if (origOuterPoint2.__x - origInnerPoint2.__x != 0) {
			matchingInnerX2 = centerX;
			var m2 = (origOuterPoint2.__y - origInnerPoint2.__y) / (origOuterPoint2.__x - origInnerPoint2.__x);
			matchingInnerY2 = m2 * (centerX - newOuterPoint2.__x) + newOuterPoint2.__y;
		}
		newInnerPoint2 = { __x: matchingInnerX2, __y: matchingInnerY2, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		if (this.closeEnough(newInnerPoint2.__x, newOuterPoint2.__x) && this.closeEnough(newInnerPoint2.__y, newOuterPoint2.__y)) {
			newInnerPoint2 = origInnerPoint2;
		}
		var inter2 = $.ig.GeometryUtil.prototype.getCircleIntersection(newInnerPoint2, newOuterPoint2, { __x: centerX, __y: centerY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }, innerRadius);
		if ($.ig.util.isNaN(inter.item1().__x) || $.ig.util.isNaN(inter.item1().__y) || $.ig.util.isNaN(inter.item2().__x) || $.ig.util.isNaN(inter.item2().__y) || $.ig.util.isNaN(inter2.item1().__x) || $.ig.util.isNaN(inter2.item1().__y) || $.ig.util.isNaN(inter2.item2().__x) || $.ig.util.isNaN(inter2.item2().__y)) {
			pf.__startPoint = { __x: cosStart * outerRadius + centerX, __y: sinStart * outerRadius + centerY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			pf.__segments.add(as2);
			var ls2 = (function () {
				var $ret = new $.ig.LineSegment(1);
				$ret.point({ __x: cosStart * outerRadius + centerX, __y: sinStart * outerRadius + centerY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				return $ret;
			}());
			pf.__segments.add(ls2);
		} else {
			var p1;
			var p2;
			if ((Math.pow(inter.item1().__x - newOuterPoint.__x, 2) + Math.pow(inter.item1().__y - newOuterPoint.__y, 2)) < (Math.pow(inter.item2().__x - newOuterPoint.__x, 2) + Math.pow(inter.item2().__y - newOuterPoint.__y, 2))) {
				p1 = inter.item1();
			} else {
				p1 = inter.item2();
			}
			if ((Math.pow(inter2.item1().__x - newOuterPoint2.__x, 2) + Math.pow(inter2.item1().__y - newOuterPoint2.__y, 2)) < (Math.pow(inter2.item2().__x - newOuterPoint2.__x, 2) + Math.pow(inter2.item2().__y - newOuterPoint2.__y, 2))) {
				p2 = inter2.item1();
			} else {
				p2 = inter2.item2();
			}
			if ((Math.pow(p2.__x - newOuterPoint.__x, 2) + Math.pow(p2.__y - newOuterPoint.__y, 2)) <= (Math.pow(p1.__x - newOuterPoint.__x, 2) + Math.pow(p1.__y - newOuterPoint.__y, 2))) {
				pf.__startPoint = { __x: cosStart * outerRadius + centerX, __y: sinStart * outerRadius + centerY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
				pf.__segments.add(as2);
				var ls21 = (function () {
					var $ret = new $.ig.LineSegment(1);
					$ret.point({ __x: cosStart * outerRadius + centerX, __y: sinStart * outerRadius + centerY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
					return $ret;
				}());
				pf.__segments.add(ls21);
			} else {
				pf.__segments.add(as1);
				pf.__segments.add(as2);
				pf.__segments.add(as3);
				var ls1 = (function () {
					var $ret = new $.ig.LineSegment(1);
					$ret.point(p1);
					return $ret;
				}());
				pf.__segments.add(ls1);
				var angle1 = $.ig.GeometryUtil.prototype.getAngleTo({ __x: centerX, __y: centerY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }, p1);
				var angle2 = $.ig.GeometryUtil.prototype.getAngleTo({ __x: centerX, __y: centerY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }, p2);
				while (angle2 < angle1) {
					angle2 += 2 * Math.PI;
				}
				var as5 = (function () {
					var $ret = new $.ig.ArcSegment();
					$ret.point(p2);
					$ret.isLargeArc(angle2 - angle1 > Math.PI);
					$ret.sweepDirection($.ig.SweepDirection.prototype.clockwise);
					$ret.size(new $.ig.Size(1, innerRadius, innerRadius));
					return $ret;
				}());
				pf.__segments.add(as5);
				var ls22 = (function () {
					var $ret = new $.ig.LineSegment(1);
					$ret.point({ __x: cosStart * roundedOuterRadius + centerX, __y: sinStart * roundedOuterRadius + centerY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
					return $ret;
				}());
				pf.__segments.add(ls22);
			}
		}
	}
	,
	$type: new $.ig.Type('BackingFrame', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('RadialGaugeScaleFrame', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
		this.ranges(new $.ig.List$1($.ig.RadialGaugeRangeFrame.prototype.$type, 0));
		this.tickAngles(new Array(0));
		this.minorTickAngles(new Array(0));
		this.labelAngles(new Array(0));
		this.labels(new Array(0));
		this.labelWidths(new Array(0));
		this.labelHeights(new Array(0));
		this.labelXOffsets(new Array(0));
		this.labelYOffsets(new Array(0));
	},
	_tickAngles: null,
	tickAngles: function (value) {
		if (arguments.length === 1) {
			this._tickAngles = value;
			return value;
		} else {
			return this._tickAngles;
		}
	}
	,
	_minorTickAngles: null,
	minorTickAngles: function (value) {
		if (arguments.length === 1) {
			this._minorTickAngles = value;
			return value;
		} else {
			return this._minorTickAngles;
		}
	}
	,
	_labelAngles: null,
	labelAngles: function (value) {
		if (arguments.length === 1) {
			this._labelAngles = value;
			return value;
		} else {
			return this._labelAngles;
		}
	}
	,
	_labels: null,
	labels: function (value) {
		if (arguments.length === 1) {
			this._labels = value;
			return value;
		} else {
			return this._labels;
		}
	}
	,
	_labelWidths: null,
	labelWidths: function (value) {
		if (arguments.length === 1) {
			this._labelWidths = value;
			return value;
		} else {
			return this._labelWidths;
		}
	}
	,
	_labelHeights: null,
	labelHeights: function (value) {
		if (arguments.length === 1) {
			this._labelHeights = value;
			return value;
		} else {
			return this._labelHeights;
		}
	}
	,
	_labelXOffsets: null,
	labelXOffsets: function (value) {
		if (arguments.length === 1) {
			this._labelXOffsets = value;
			return value;
		} else {
			return this._labelXOffsets;
		}
	}
	,
	_labelYOffsets: null,
	labelYOffsets: function (value) {
		if (arguments.length === 1) {
			this._labelYOffsets = value;
			return value;
		} else {
			return this._labelYOffsets;
		}
	}
	,
	_scaleStartExtent: 0,
	scaleStartExtent: function (value) {
		if (arguments.length === 1) {
			this._scaleStartExtent = value;
			return value;
		} else {
			return this._scaleStartExtent;
		}
	}
	,
	_scaleEndExtent: 0,
	scaleEndExtent: function (value) {
		if (arguments.length === 1) {
			this._scaleEndExtent = value;
			return value;
		} else {
			return this._scaleEndExtent;
		}
	}
	,
	_labelExtent: 0,
	labelExtent: function (value) {
		if (arguments.length === 1) {
			this._labelExtent = value;
			return value;
		} else {
			return this._labelExtent;
		}
	}
	,
	_tickStartExtent: 0,
	tickStartExtent: function (value) {
		if (arguments.length === 1) {
			this._tickStartExtent = value;
			return value;
		} else {
			return this._tickStartExtent;
		}
	}
	,
	_tickEndExtent: 0,
	tickEndExtent: function (value) {
		if (arguments.length === 1) {
			this._tickEndExtent = value;
			return value;
		} else {
			return this._tickEndExtent;
		}
	}
	,
	_minorTickStartExtent: 0,
	minorTickStartExtent: function (value) {
		if (arguments.length === 1) {
			this._minorTickStartExtent = value;
			return value;
		} else {
			return this._minorTickStartExtent;
		}
	}
	,
	_minorTickEndExtent: 0,
	minorTickEndExtent: function (value) {
		if (arguments.length === 1) {
			this._minorTickEndExtent = value;
			return value;
		} else {
			return this._minorTickEndExtent;
		}
	}
	,
	_scaleStartAngle: 0,
	scaleStartAngle: function (value) {
		if (arguments.length === 1) {
			this._scaleStartAngle = value;
			return value;
		} else {
			return this._scaleStartAngle;
		}
	}
	,
	_scaleEndAngle: 0,
	scaleEndAngle: function (value) {
		if (arguments.length === 1) {
			this._scaleEndAngle = value;
			return value;
		} else {
			return this._scaleEndAngle;
		}
	}
	,
	_tickBrush: null,
	tickBrush: function (value) {
		if (arguments.length === 1) {
			this._tickBrush = value;
			return value;
		} else {
			return this._tickBrush;
		}
	}
	,
	_tickStrokeThickness: 0,
	tickStrokeThickness: function (value) {
		if (arguments.length === 1) {
			this._tickStrokeThickness = value;
			return value;
		} else {
			return this._tickStrokeThickness;
		}
	}
	,
	_minorTickStrokeThickness: 0,
	minorTickStrokeThickness: function (value) {
		if (arguments.length === 1) {
			this._minorTickStrokeThickness = value;
			return value;
		} else {
			return this._minorTickStrokeThickness;
		}
	}
	,
	_minorTickBrush: null,
	minorTickBrush: function (value) {
		if (arguments.length === 1) {
			this._minorTickBrush = value;
			return value;
		} else {
			return this._minorTickBrush;
		}
	}
	,
	_scaleBrush: null,
	scaleBrush: function (value) {
		if (arguments.length === 1) {
			this._scaleBrush = value;
			return value;
		} else {
			return this._scaleBrush;
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
	_scaleOversweep: 0,
	scaleOversweep: function (value) {
		if (arguments.length === 1) {
			this._scaleOversweep = value;
			return value;
		} else {
			return this._scaleOversweep;
		}
	}
	,
	_ranges: null,
	ranges: function (value) {
		if (arguments.length === 1) {
			this._ranges = value;
			return value;
		} else {
			return this._ranges;
		}
	}
	,
	interpolateArray: function (p, dest, prev, next) {
		var maxCount = Math.max(prev.length, next.length);
		var minCount = Math.min(prev.length, next.length);
		if (dest.length != maxCount) {
			dest = new Array(maxCount);
		}
		var lastPrev = 0;
		if (prev.length > 0) {
			lastPrev = prev[prev.length - 1];
		}
		var lastNext = 0;
		if (next.length > 0) {
			lastNext = next[next.length - 1];
		}
		var usePrev = prev.length > next.length;
		var i = 0;
		for (i = 0; i < minCount; i++) {
			dest[i] = prev[i] + (next[i] - prev[i]) * p;
		}
		var prevVal;
		var nextVal;
		for (i = minCount; i < maxCount; i++) {
			if (usePrev) {
				prevVal = prev[i];
				nextVal = lastNext;
			} else {
				prevVal = lastPrev;
				nextVal = next[i];
			}
			dest[i] = prevVal + (nextVal - prevVal) * p;
		}
		return dest;
	}
	,
	interpolateStringArray: function (p, dest, prev, next) {
		var maxCount = Math.max(prev.length, next.length);
		var minCount = Math.min(prev.length, next.length);
		if (dest.length != maxCount) {
			dest = new Array(maxCount);
		}
		var lastPrev = null;
		if (prev.length > 0) {
			lastPrev = prev[prev.length - 1];
		}
		var lastNext = null;
		if (next.length > 0) {
			lastNext = next[next.length - 1];
		}
		var usePrev = prev.length > next.length;
		var i = 0;
		for (i = 0; i < minCount; i++) {
			dest[i] = next[i];
		}
		var prevVal;
		var nextVal;
		for (i = minCount; i < maxCount; i++) {
			if (usePrev) {
				prevVal = prev[i];
				nextVal = lastNext;
			} else {
				prevVal = lastPrev;
				nextVal = next[i];
			}
			if (nextVal != null) {
				dest[i] = nextVal;
			} else {
				dest[i] = prevVal;
			}
		}
		return dest;
	}
	,
	interpolate: function (p, previous, next) {
		this.scaleBrush($.ig.BrushUtil.prototype.getInterpolation(previous.scaleBrush(), p, next.scaleBrush(), $.ig.InterpolationMode.prototype.rGB));
		this.tickBrush($.ig.BrushUtil.prototype.getInterpolation(previous.tickBrush(), p, next.tickBrush(), $.ig.InterpolationMode.prototype.rGB));
		this.minorTickBrush($.ig.BrushUtil.prototype.getInterpolation(previous.minorTickBrush(), p, next.minorTickBrush(), $.ig.InterpolationMode.prototype.rGB));
		if (previous.fontBrush() == null && next.fontBrush() == null) {
			this.fontBrush(null);
		} else {
			this.fontBrush($.ig.BrushUtil.prototype.getInterpolation(previous.fontBrush(), p, next.fontBrush(), $.ig.InterpolationMode.prototype.rGB));
		}
		this.scaleStartExtent(previous.scaleStartExtent() + (next.scaleStartExtent() - previous.scaleStartExtent()) * p);
		this.scaleEndExtent(previous.scaleEndExtent() + (next.scaleEndExtent() - previous.scaleEndExtent()) * p);
		this.scaleStartAngle(previous.scaleStartAngle() + (next.scaleStartAngle() - previous.scaleStartAngle()) * p);
		this.scaleEndAngle(previous.scaleEndAngle() + (next.scaleEndAngle() - previous.scaleEndAngle()) * p);
		this.scaleOversweep(previous.scaleOversweep() + (next.scaleOversweep() - previous.scaleOversweep()) * p);
		this.labelExtent(previous.labelExtent() + (next.labelExtent() - previous.labelExtent()) * p);
		this.tickStartExtent(previous.tickStartExtent() + (next.tickStartExtent() - previous.tickStartExtent()) * p);
		this.tickEndExtent(previous.tickEndExtent() + (next.tickEndExtent() - previous.tickEndExtent()) * p);
		this.minorTickStartExtent(previous.minorTickStartExtent() + (next.minorTickStartExtent() - previous.minorTickStartExtent()) * p);
		this.minorTickEndExtent(previous.minorTickEndExtent() + (next.minorTickEndExtent() - previous.minorTickEndExtent()) * p);
		this.tickStrokeThickness(previous.tickStrokeThickness() + (next.tickStrokeThickness() - previous.tickStrokeThickness()) * p);
		this.minorTickStrokeThickness(previous.minorTickStrokeThickness() + (next.minorTickStrokeThickness() - previous.minorTickStrokeThickness()) * p);
		this.tickAngles(this.interpolateArray(p, this.tickAngles(), previous.tickAngles(), next.tickAngles()));
		this.minorTickAngles(this.interpolateArray(p, this.minorTickAngles(), previous.minorTickAngles(), next.minorTickAngles()));
		this.labelAngles(this.interpolateArray(p, this.labelAngles(), previous.labelAngles(), next.labelAngles()));
		this.labelWidths(this.interpolateArray(p, this.labelWidths(), previous.labelWidths(), next.labelWidths()));
		this.labelHeights(this.interpolateArray(p, this.labelHeights(), previous.labelHeights(), next.labelHeights()));
		this.labelXOffsets(this.interpolateArray(p, this.labelXOffsets(), previous.labelXOffsets(), next.labelXOffsets()));
		this.labelYOffsets(this.interpolateArray(p, this.labelYOffsets(), previous.labelYOffsets(), next.labelYOffsets()));
		this.labels(this.interpolateStringArray(p, this.labels(), previous.labels(), next.labels()));
		this.interpolateRanges(p, this.ranges(), previous.ranges(), next.ranges());
	}
	,
	interpolateRanges: function (p, interpolatedRanges, previousRanges, nextRanges) {
		$.ig.InterpolationUtil.prototype.interpolateValues$1($.ig.RadialGaugeRangeFrame.prototype.$type, interpolatedRanges, p, previousRanges, nextRanges, function () { return new $.ig.RadialGaugeRangeFrame(); }, $.ig.RadialGaugeRangeFrame.prototype.interpolate);
	}
	,
	$type: new $.ig.Type('RadialGaugeScaleFrame', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('RadialGaugeNeedlePreparer', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	_parameters: null,
	parameters: function (value) {
		if (arguments.length === 1) {
			this._parameters = value;
			return value;
		} else {
			return this._parameters;
		}
	}
	,
	prepareNeedle: function (frame) {
		var p = this.parameters();
		frame.needleBrush(p._needleBrush);
		frame.needleOutline(p._needleOutline);
		frame.capBrush(p._capFill);
		frame.capOutline(p._capOutline);
		frame.needleStrokeThickness(p._needleStrokeThickness);
		frame.capStrokeThickness(p._capStrokeThickness);
		switch (p._needleShape) {
			case $.ig.RadialGaugeNeedleShape.prototype.none:
				this.prepareNoneNeedle(frame);
				break;
			case $.ig.RadialGaugeNeedleShape.prototype.rectangle:
				this.prepareRectangleNeedle(frame);
				break;
			case $.ig.RadialGaugeNeedleShape.prototype.triangle:
				this.prepareTriangleNeedle(frame);
				break;
			case $.ig.RadialGaugeNeedleShape.prototype.trapezoid:
				this.prepareTrapezoidNeedle(frame);
				break;
			case $.ig.RadialGaugeNeedleShape.prototype.rectangleWithBulb:
				this.prepareRectangleWithBulbNeedle(frame);
				break;
			case $.ig.RadialGaugeNeedleShape.prototype.needle:
				this.prepareNeedleNeedle(frame);
				break;
			case $.ig.RadialGaugeNeedleShape.prototype.needleWithBulb:
				this.prepareNeedleWithBulbNeedle(frame);
				break;
			case $.ig.RadialGaugeNeedleShape.prototype.trapezoidWithBulb:
				this.prepareTrapezoidWithbulbNeedle(frame);
				break;
			case $.ig.RadialGaugeNeedleShape.prototype.triangleWithBulb:
				this.prepareTriangleWithBulbNeedle(frame);
				break;
		}
		switch (p._capShape) {
			case $.ig.RadialGaugePivotShape.prototype.circleOverlay:
				this.prepareCircle(frame, true, false);
				break;
			case $.ig.RadialGaugePivotShape.prototype.circleUnderlay:
				this.prepareCircle(frame, false, false);
				break;
			case $.ig.RadialGaugePivotShape.prototype.circleOverlayWithHole:
				this.prepareCircle(frame, true, true);
				break;
			case $.ig.RadialGaugePivotShape.prototype.circleUnderlayWithHole:
				this.prepareCircle(frame, false, true);
				break;
		}
	}
	,
	prepareCircle: function (frame, isOverlay, hasCutout) {
		var p = this.getNeedleParameters();
		var capWidth = p._capWidth;
		var capPos = p._capPos;
		var capInnerWidth = p._capInnerWidth;
		var currSeg = frame.circleUnderlay();
		if (isOverlay) {
			currSeg = frame.circleOverlay();
		}
		var currPoints = currSeg._segmentPoints;
		currSeg._isArcPhase = true;
		currSeg._isClockwise = true;
		currSeg._isLargeArc = false;
		currSeg._arcRadius = capWidth;
		currPoints.add({ __x: 0 + capWidth, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currPoints.add({ __x: 0 - capWidth, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currSeg = frame.circleUnderlayUnderside();
		if (isOverlay) {
			currSeg = frame.circleOverlayUnderside();
		}
		currPoints = currSeg._segmentPoints;
		currSeg._isArcPhase = true;
		currSeg._isClockwise = true;
		currSeg._isLargeArc = true;
		currSeg._arcRadius = capWidth;
		currPoints.add({ __x: 0 - capWidth, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currPoints.add({ __x: 0 + capWidth, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		if (hasCutout) {
			currSeg = frame.circleUnderlayCutout();
			if (isOverlay) {
				currSeg = frame.circleOverlayCutout();
			}
			currPoints = currSeg._segmentPoints;
			currSeg._isArcPhase = true;
			currSeg._isClockwise = false;
			currSeg._isLargeArc = false;
			currSeg._arcRadius = capInnerWidth;
			currPoints.add({ __x: 0 + capInnerWidth, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			currPoints.add({ __x: 0 - capInnerWidth, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			currSeg = frame.circleUnderlayCutoutUnderside();
			if (isOverlay) {
				currSeg = frame.circleOverlayCutoutUnderside();
			}
			currPoints = currSeg._segmentPoints;
			currSeg._isArcPhase = true;
			currSeg._isClockwise = false;
			currSeg._isLargeArc = true;
			currSeg._arcRadius = capInnerWidth;
			currPoints.add({ __x: 0 - capInnerWidth, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			currPoints.add({ __x: 0 + capInnerWidth, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		}
	}
	,
	prepareTriangleWithBulbNeedle: function (frame) {
		this.prepareNeedleHelper(frame, false, true, false, true);
	}
	,
	prepareTrapezoidWithbulbNeedle: function (frame) {
		this.prepareNeedleHelper(frame, false, false, true, true);
	}
	,
	prepareTrapezoidNeedle: function (frame) {
		this.prepareNeedleHelper(frame, false, false, true, false);
	}
	,
	prepareTriangleNeedle: function (frame) {
		this.prepareNeedleHelper(frame, false, true, false, false);
	}
	,
	prepareNeedleWithBulbNeedle: function (frame) {
		this.prepareNeedleHelper(frame, false, false, false, true);
	}
	,
	getYValue: function (startX, startY, endX, endY, currX) {
		var p = 0;
		if (endX == startX) {
			p = 0;
		} else {
			p = (currX - startX) / (endX - startX);
		}
		var ret = startY + (endY - startY) * p;
		return ret;
	}
	,
	prepareNeedleHelper: function (frame, isRectangle, isTriangle, isTrapezoid, withBulb) {
		this.resetFrame(frame);
		var p = this.getNeedleParameters();
		frame.needleStartExtent(p._minExtent);
		frame.needleEndExtent(p._maxExtent);
		frame.needleWidth(0);
		if (!$.ig.util.isNaN(p._maxWidth)) {
			frame.needleWidth(Math.max(frame.needleWidth(), p._maxWidth));
		}
		if (!$.ig.util.isNaN(p._capWidth)) {
			frame.needleWidth(Math.max(frame.needleWidth(), p._capWidth));
		}
		if (!$.ig.util.isNaN(p._pointFeatureWidth)) {
			frame.needleWidth(Math.max(frame.needleWidth(), p._pointFeatureWidth));
		}
		if (!$.ig.util.isNaN(p._baseFeatureWidth)) {
			frame.needleWidth(Math.max(frame.needleWidth(), p._baseFeatureWidth));
		}
		if (isTriangle) {
			p._pointFeatureExtent = p._maxExtent;
			p._pointFeatureWidth = 0;
		}
		if (isTrapezoid) {
			p._pointFeatureExtent = p._maxExtent;
			p._pointFeatureWidth = p._maxWidth;
		}
		if (isRectangle) {
			var max = Math.max(p._minWidth, p._maxWidth);
			p._minWidth = max;
			p._maxWidth = max;
			p._pointFeatureExtent = p._maxExtent;
			p._pointFeatureWidth = p._maxWidth;
		}
		if ($.ig.util.isNaN(p._pointFeatureExtent)) {
			p._pointFeatureExtent = p._maxExtent;
		}
		var hasInlineCap = p._capShape == $.ig.RadialGaugePivotShape.prototype.circle || p._capShape == $.ig.RadialGaugePivotShape.prototype.circleWithHole;
		var minWidth = p._minWidth;
		var maxWidth = p._maxWidth;
		if (!isTrapezoid && !isRectangle) {
			maxWidth = 0;
		}
		var pointFeatureWidth = p._pointFeatureWidth;
		var capWidth = p._capWidth;
		var minExtent = p._minExtent;
		var maxExtent = p._maxExtent;
		var baseFeaturePos = p._baseFeatureExtent;
		var pointFeaturePos = p._pointFeatureExtent;
		var baseFeatureWidth = p._baseFeatureWidth;
		var hasPointFeature = true;
		var hasBaseFeature = true;
		if ($.ig.util.isNaN(pointFeaturePos)) {
			hasPointFeature = false;
			pointFeaturePos = maxExtent;
		}
		if ($.ig.util.isNaN(baseFeaturePos)) {
			hasBaseFeature = false;
			baseFeaturePos = minExtent;
		}
		var capInnerWidth = p._capInnerWidth;
		var capPos = p._capPos;
		var intersectAngle = 0;
		var capStartXPos = capPos;
		var capEndXPos = capPos;
		var capStartYPos = this.getYValue(minExtent, minWidth, pointFeaturePos, pointFeatureWidth, capPos);
		var capEndYPos = capStartYPos;
		var hasCapHole = p._capShape == $.ig.RadialGaugePivotShape.prototype.circleWithHole;
		if (hasInlineCap) {
			var res = $.ig.GeometryUtil.prototype.getCircleIntersection({ __x: minExtent, __y: minWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }, { __x: pointFeaturePos, __y: pointFeatureWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }, { __x: 0, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }, capWidth);
			if ($.ig.util.isNaN(res.item1().__x) || $.ig.util.isNaN(res.item1().__y) || $.ig.util.isNaN(res.item2().__x) || $.ig.util.isNaN(res.item2().__y)) {
				hasInlineCap = false;
			} else {
				var interPoint1 = res.item1();
				var interPoint2 = res.item2();
				capStartXPos = interPoint1.__x;
				capEndXPos = interPoint2.__x;
				capStartYPos = interPoint1.__y;
				capEndYPos = interPoint2.__y;
				if (capStartXPos > capEndXPos) {
					var swap = capStartXPos;
					capStartXPos = capEndXPos;
					capEndXPos = swap;
					swap = capStartYPos;
					capStartYPos = capEndYPos;
					capEndYPos = swap;
				}
			}
		}
		if (hasCapHole) {
			var res1 = $.ig.GeometryUtil.prototype.getCircleIntersection({ __x: minExtent, __y: minWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }, { __x: pointFeaturePos, __y: pointFeatureWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }, { __x: 0, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }, capInnerWidth);
			if (($.ig.util.isNaN(res1.item1().__x) || $.ig.util.isNaN(res1.item1().__y) || $.ig.util.isNaN(res1.item2().__x) || $.ig.util.isNaN(res1.item2().__y)) || hasInlineCap) {
				hasCapHole = true;
			} else {
				hasCapHole = false;
			}
		}
		if (minExtent > capEndXPos || maxExtent < capStartXPos) {
			hasInlineCap = false;
			capStartXPos = minExtent;
			capEndXPos = minExtent;
		}
		var hasStartCap = false;
		var hasEndCap = false;
		if (hasInlineCap && minExtent >= capStartXPos) {
			hasStartCap = true;
		}
		if (hasInlineCap && maxExtent <= capEndXPos) {
			hasEndCap = true;
		}
		if ($.ig.util.isNaN(baseFeaturePos) || baseFeaturePos > capStartXPos) {
			baseFeaturePos = capStartXPos;
		}
		if (pointFeaturePos < capEndXPos) {
			pointFeaturePos = capEndXPos;
		}
		var hasBulb = withBulb;
		var startCapWidth = capWidth;
		var endCapWidth = capWidth;
		if (baseFeaturePos + baseFeatureWidth > capStartXPos) {
			hasBulb = false;
		}
		var baseCapStartX = baseFeaturePos;
		var baseCapStartY = minWidth;
		var baseCapEndX = baseFeaturePos;
		var baseCapEndY = minWidth;
		var startCapStartX = capStartXPos;
		var startCapEndX = capEndXPos;
		var startCapStartY = capStartYPos;
		var startCapEndY = capEndYPos;
		var endCapStartX = capStartXPos;
		var endCapEndX = capEndXPos;
		var endCapStartY = capStartYPos;
		var endCapEndY = capEndYPos;
		if (hasBulb) {
			var res2 = $.ig.GeometryUtil.prototype.getCircleIntersection({ __x: minExtent, __y: minWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }, { __x: pointFeaturePos, __y: pointFeatureWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }, { __x: baseFeaturePos, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }, baseFeatureWidth);
			if ((!$.ig.util.isNaN(res2.item1().__x) && !$.ig.util.isNaN(res2.item1().__y) && !$.ig.util.isNaN(res2.item2().__x) && !$.ig.util.isNaN(res2.item2().__y))) {
				hasBulb = true;
				baseCapStartX = res2.item1().__x;
				baseCapStartY = res2.item1().__y;
				baseCapEndX = res2.item2().__x;
				baseCapEndY = res2.item2().__y;
				if (baseCapStartX > baseCapEndX) {
					var swap1 = baseCapStartX;
					baseCapStartX = baseCapEndX;
					baseCapEndX = swap1;
					swap1 = baseCapStartY;
					baseCapStartY = baseCapEndY;
					baseCapEndY = swap1;
				}
			} else {
				hasBulb = false;
			}
		}
		if (hasStartCap) {
			hasBulb = false;
		}
		if (hasBulb && minExtent >= baseCapStartX) {
			hasStartCap = true;
			startCapWidth = baseFeatureWidth;
			startCapStartX = baseCapStartX;
			startCapStartY = baseCapStartY;
			startCapEndX = baseCapEndX;
			startCapEndY = baseCapEndY;
		}
		if (hasBulb && maxExtent <= baseCapEndX) {
			hasEndCap = true;
			endCapWidth = baseFeatureWidth;
			endCapWidth = baseFeatureWidth;
			endCapStartX = baseCapStartX;
			endCapStartY = baseCapStartY;
			endCapEndX = baseCapEndX;
			endCapEndY = baseCapEndY;
		}
		var currX = minExtent;
		var currSeg = frame.base();
		var currPoints = currSeg._segmentPoints;
		var currWidth = minWidth;
		if (hasStartCap) {
			currX = startCapStartX;
			currSeg._isArcPhase = true;
			currSeg._isLargeArc = false;
			currSeg._isClockwise = false;
			currSeg._arcRadius = startCapWidth;
			baseFeaturePos = startCapStartX;
			minExtent = startCapStartX;
			currWidth = startCapStartY;
			minWidth = startCapStartY;
			hasInlineCap = false;
		}
		if (hasEndCap) {
			pointFeaturePos = endCapEndX;
			maxExtent = endCapEndX;
			hasInlineCap = false;
		}
		var hasCap = hasStartCap || hasEndCap || hasInlineCap;
		currPoints.add({ __x: currX, __y: -1 * currWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currPoints.add({ __x: currX, __y: currWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currSeg = frame.baseToFeature();
		currPoints = currSeg._segmentPoints;
		currPoints.add({ __x: currX, __y: currWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currX = baseCapStartX;
		currWidth = this.getYValue(minExtent, minWidth, pointFeaturePos, pointFeatureWidth, currX);
		currPoints.add({ __x: currX, __y: currWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currSeg = frame.baseFeature();
		currPoints = currSeg._segmentPoints;
		if (hasBulb) {
			currSeg._isArcPhase = true;
			currSeg._isLargeArc = false;
			currSeg._isClockwise = false;
			currSeg._arcRadius = baseFeatureWidth;
		}
		currPoints.add({ __x: currX, __y: currWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currX = baseCapEndX;
		currWidth = this.getYValue(minExtent, minWidth, pointFeaturePos, pointFeatureWidth, currX);
		currPoints.add({ __x: currX, __y: currWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currSeg = frame.featureToCap();
		currPoints = currSeg._segmentPoints;
		currPoints.add({ __x: currX, __y: currWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currX = capStartXPos;
		currWidth = this.getYValue(minExtent, minWidth, pointFeaturePos, pointFeatureWidth, currX);
		currPoints.add({ __x: currX, __y: currWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currSeg = frame.cap();
		currPoints = currSeg._segmentPoints;
		if (hasCap) {
			currSeg._isArcPhase = true;
			currSeg._isLargeArc = false;
			currSeg._isClockwise = false;
			currSeg._arcRadius = capWidth;
		}
		currPoints.add({ __x: currX, __y: currWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currX = capEndXPos;
		currWidth = this.getYValue(minExtent, minWidth, pointFeaturePos, pointFeatureWidth, currX);
		currPoints.add({ __x: currX, __y: currWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currSeg = frame.capToFeature();
		currPoints = currSeg._segmentPoints;
		currPoints.add({ __x: currX, __y: currWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currX = pointFeaturePos;
		currWidth = this.getYValue(minExtent, minWidth, pointFeaturePos, pointFeatureWidth, currX);
		currPoints.add({ __x: currX, __y: currWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currSeg = frame.pointFeature();
		currPoints = currSeg._segmentPoints;
		currPoints.add({ __x: currX, __y: currWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currPoints.add({ __x: currX, __y: currWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currSeg = frame.featureToPoint();
		currPoints = currSeg._segmentPoints;
		currPoints.add({ __x: currX, __y: currWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currX = maxExtent;
		currWidth = this.getYValue(pointFeaturePos, pointFeatureWidth, maxExtent, maxWidth, currX);
		currPoints.add({ __x: currX, __y: currWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currSeg = frame.point();
		currPoints = currSeg._segmentPoints;
		if (hasEndCap) {
			currX = endCapEndX;
			currSeg._isArcPhase = true;
			currSeg._isLargeArc = false;
			currSeg._isClockwise = false;
			currSeg._arcRadius = endCapWidth;
		}
		currPoints.add({ __x: currX, __y: currWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currPoints.add({ __x: currX, __y: -1 * currWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currSeg = frame.pointToFeatureUnderside();
		currPoints = currSeg._segmentPoints;
		currPoints.add({ __x: currX, __y: -1 * currWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currX = pointFeaturePos;
		currWidth = this.getYValue(minExtent, minWidth, pointFeaturePos, pointFeatureWidth, currX);
		currPoints.add({ __x: currX, __y: -1 * currWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currSeg = frame.pointFeatureUnderside();
		currPoints = currSeg._segmentPoints;
		currPoints.add({ __x: currX, __y: -1 * currWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currPoints.add({ __x: currX, __y: -1 * currWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currSeg = frame.featureToCapUnderside();
		currPoints = currSeg._segmentPoints;
		currPoints.add({ __x: currX, __y: -1 * currWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currX = capEndXPos;
		currWidth = this.getYValue(minExtent, minWidth, pointFeaturePos, pointFeatureWidth, currX);
		currPoints.add({ __x: currX, __y: -1 * currWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currSeg = frame.capUnderside();
		currPoints = currSeg._segmentPoints;
		if (hasCap) {
			currSeg._isArcPhase = true;
			currSeg._isLargeArc = false;
			currSeg._isClockwise = false;
			currSeg._arcRadius = capWidth;
		}
		currPoints.add({ __x: currX, __y: -1 * currWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currX = capStartXPos;
		currWidth = this.getYValue(minExtent, minWidth, pointFeaturePos, pointFeatureWidth, currX);
		currPoints.add({ __x: currX, __y: -1 * currWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currSeg = frame.capToFeatureUnderside();
		currPoints = currSeg._segmentPoints;
		currPoints.add({ __x: currX, __y: -1 * currWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currX = baseCapEndX;
		currWidth = this.getYValue(minExtent, minWidth, pointFeaturePos, pointFeatureWidth, currX);
		currPoints.add({ __x: currX, __y: -1 * currWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currSeg = frame.baseFeatureUnderside();
		currPoints = currSeg._segmentPoints;
		if (hasBulb) {
			currSeg._isArcPhase = true;
			currSeg._isLargeArc = false;
			currSeg._isClockwise = false;
			currSeg._arcRadius = baseFeatureWidth;
		}
		currPoints.add({ __x: currX, __y: -1 * currWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currX = baseCapStartX;
		currWidth = this.getYValue(minExtent, minWidth, pointFeaturePos, pointFeatureWidth, currX);
		currPoints.add({ __x: currX, __y: -1 * currWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currSeg = frame.featureToBaseUnderside();
		currPoints = currSeg._segmentPoints;
		currPoints.add({ __x: currX, __y: -1 * currWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currX = minExtent;
		currWidth = this.getYValue(minExtent, minWidth, pointFeaturePos, pointFeatureWidth, currX);
		currPoints.add({ __x: currX, __y: -1 * currWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currSeg = frame.cutout();
		currPoints = currSeg._segmentPoints;
		if (hasCapHole && hasCap) {
			currSeg._isArcPhase = true;
			currSeg._isClockwise = true;
			currSeg._isLargeArc = false;
			currSeg._arcRadius = capInnerWidth;
			currPoints.add({ __x: 0 + capInnerWidth, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			currPoints.add({ __x: 0 - capInnerWidth, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		}
		currSeg = frame.cutoutUnderside();
		currPoints = currSeg._segmentPoints;
		if (hasCapHole && hasCap) {
			currSeg._isArcPhase = true;
			currSeg._isClockwise = true;
			currSeg._isLargeArc = true;
			currSeg._arcRadius = capInnerWidth;
			currPoints.add({ __x: 0 - capInnerWidth, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			currPoints.add({ __x: 0 + capInnerWidth, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		}
	}
	,
	prepareNeedleNeedle: function (frame) {
		this.prepareNeedleHelper(frame, false, false, false, false);
	}
	,
	prepareRectangleWithBulbNeedle: function (frame) {
		this.prepareNeedleHelper(frame, true, false, false, true);
	}
	,
	getNeedleParameters: function () {
		var p = this.parameters();
		var o = this.parameters().clone();
		var defaultMinExtent = 0;
		var defaultMaxExtent = 0.48;
		var defaultBaseFeaturePos = NaN;
		var defaultPointFeaturePos = NaN;
		var defaultCapWidth = 0.2;
		var defaultMinWidth = 0.1;
		var defaultMaxWidth = 0.1;
		var defaultCapInnerWidth = 0.1;
		var defaultBaseFeatureWidth = 0.14;
		var defaultPointFeatureWidth = 0.06;
		switch (p._needleShape) {
			case $.ig.RadialGaugeNeedleShape.prototype.needle:
				defaultPointFeaturePos = 0.4;
				break;
			case $.ig.RadialGaugeNeedleShape.prototype.trapezoid:
				defaultMaxWidth = 0.06;
				break;
			case $.ig.RadialGaugeNeedleShape.prototype.needleWithBulb:
				defaultPointFeaturePos = 0.4;
				defaultMinExtent = -0.3;
				defaultBaseFeaturePos = -0.2;
				defaultBaseFeatureWidth = 0.14;
				break;
			case $.ig.RadialGaugeNeedleShape.prototype.rectangleWithBulb:
				defaultMinExtent = -0.3;
				defaultBaseFeaturePos = -0.2;
				defaultBaseFeatureWidth = 0.14;
				break;
			case $.ig.RadialGaugeNeedleShape.prototype.trapezoidWithBulb:
				defaultMaxWidth = 0.06;
				defaultMinExtent = -0.3;
				defaultBaseFeaturePos = -0.2;
				defaultBaseFeatureWidth = 0.14;
				break;
			case $.ig.RadialGaugeNeedleShape.prototype.triangleWithBulb:
				defaultMinExtent = -0.3;
				defaultBaseFeaturePos = -0.2;
				defaultBaseFeatureWidth = 0.14;
				break;
		}
		var minExtent = p._minExtent;
		var maxExtent = p._maxExtent;
		var hasInlineCap = p._capShape == $.ig.RadialGaugePivotShape.prototype.circle || p._capShape == $.ig.RadialGaugePivotShape.prototype.circleWithHole;
		if ($.ig.util.isNaN(minExtent)) {
			minExtent = defaultMinExtent;
		}
		if ($.ig.util.isNaN(maxExtent)) {
			maxExtent = defaultMaxExtent;
		}
		var tmpMin = Math.min(minExtent, maxExtent);
		var tmpMax = Math.max(minExtent, maxExtent);
		minExtent = tmpMin;
		maxExtent = tmpMax;
		var minWidth = p._minWidth;
		if ($.ig.util.isNaN(minWidth)) {
			minWidth = defaultMinWidth;
		}
		var maxWidth = p._maxWidth;
		if ($.ig.util.isNaN(maxWidth)) {
			maxWidth = defaultMaxWidth;
		}
		var capWidth = p._capWidth;
		var capInnerWidth = p._capInnerWidth;
		if ($.ig.util.isNaN(capWidth)) {
			capWidth = defaultCapWidth;
		}
		if ($.ig.util.isNaN(capInnerWidth)) {
			capInnerWidth = defaultCapInnerWidth;
		}
		var tmpCapWidth = Math.max(capWidth, capInnerWidth);
		var tmpCapInnerWidth = Math.min(capWidth, capInnerWidth);
		capWidth = tmpCapWidth;
		capInnerWidth = tmpCapInnerWidth;
		var baseFeaturePos = p._baseFeatureExtent;
		if ($.ig.util.isNaN(baseFeaturePos)) {
			baseFeaturePos = defaultBaseFeaturePos;
		}
		if (!$.ig.util.isNaN(baseFeaturePos)) {
			if (baseFeaturePos < minExtent) {
				baseFeaturePos = NaN;
			}
			if (baseFeaturePos > 0 && minExtent <= 0) {
				baseFeaturePos = NaN;
			}
			if (baseFeaturePos > maxExtent) {
				baseFeaturePos = maxExtent;
			}
		}
		var pointFeaturePos = p._pointFeatureExtent;
		if ($.ig.util.isNaN(pointFeaturePos)) {
			pointFeaturePos = defaultPointFeaturePos;
		}
		if (!$.ig.util.isNaN(pointFeaturePos)) {
			if (pointFeaturePos > maxExtent) {
				pointFeaturePos = NaN;
			}
			if (pointFeaturePos < 0 && maxExtent >= 0) {
				pointFeaturePos = NaN;
			}
			if (pointFeaturePos < minExtent) {
				pointFeaturePos = minExtent;
			}
		}
		var capPos = 0;
		if (minExtent > 0) {
			capPos = minExtent;
		}
		if (maxExtent < 0) {
			capPos = minExtent;
		}
		if (capWidth <= 0) {
			capWidth = 0;
		}
		var baseFeatureWidth = p._baseFeatureWidth;
		var pointFeatureWidth = p._pointFeatureWidth;
		if ($.ig.util.isNaN(baseFeatureWidth)) {
			baseFeatureWidth = defaultBaseFeatureWidth;
		}
		if ($.ig.util.isNaN(pointFeatureWidth)) {
			pointFeatureWidth = defaultPointFeatureWidth;
		}
		o._capWidth = capWidth / 2;
		o._capInnerWidth = capInnerWidth / 2;
		o._baseFeatureExtent = baseFeaturePos;
		o._pointFeatureExtent = pointFeaturePos;
		o._minExtent = minExtent;
		o._maxExtent = maxExtent;
		o._capPos = capPos;
		o._minWidth = minWidth / 2;
		o._maxWidth = maxWidth / 2;
		o._pointFeatureWidth = pointFeatureWidth / 2;
		o._baseFeatureWidth = baseFeatureWidth / 2;
		return o;
	}
	,
	prepareRectangleNeedle: function (frame) {
		this.prepareNeedleHelper(frame, true, false, false, false);
	}
	,
	resetFrame: function (frame) {
		frame.base().reset();
		frame.baseToFeature().reset();
		frame.baseFeature().reset();
		frame.featureToCap().reset();
		frame.cap().reset();
		frame.capToFeature().reset();
		frame.pointFeature().reset();
		frame.featureToPoint().reset();
		frame.point().reset();
		frame.pointToFeatureUnderside().reset();
		frame.pointFeatureUnderside().reset();
		frame.featureToCapUnderside().reset();
		frame.capUnderside().reset();
		frame.capToFeatureUnderside().reset();
		frame.baseFeatureUnderside().reset();
		frame.featureToBaseUnderside().reset();
		frame.cutout().reset();
		frame.cutoutUnderside().reset();
		frame.circleOverlay().reset();
		frame.circleOverlayUnderside().reset();
		frame.circleUnderlay().reset();
		frame.circleUnderlayUnderside().reset();
		frame.circleOverlayCutout().reset();
		frame.circleOverlayCutoutUnderside().reset();
		frame.circleUnderlayCutout().reset();
		frame.circleUnderlayCutoutUnderside().reset();
	}
	,
	prepareNoneNeedle: function (frame) {
		this.resetFrame(frame);
		var minExtent = 0;
		var maxExtent = 0;
		var baseFeaturePos = 0;
		var pointFeaturePos = 0;
		var capPos = 0;
		var width = 0;
		var underWidth = 0;
		var currSeg = frame.base();
		var currPoints = currSeg._segmentPoints;
		currPoints.add({ __x: minExtent, __y: underWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currPoints.add({ __x: minExtent, __y: width, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currSeg = frame.baseToFeature();
		currPoints = currSeg._segmentPoints;
		currPoints.add({ __x: minExtent, __y: width, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currPoints.add({ __x: baseFeaturePos, __y: width, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currSeg = frame.baseFeature();
		currPoints = currSeg._segmentPoints;
		currPoints.add({ __x: baseFeaturePos, __y: width, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currPoints.add({ __x: baseFeaturePos, __y: width, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		var capStartXPos = capPos;
		var capEndXPos = capPos;
		currSeg = frame.featureToCap();
		currPoints = currSeg._segmentPoints;
		currPoints.add({ __x: baseFeaturePos, __y: width, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currPoints.add({ __x: capStartXPos, __y: width, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currSeg = frame.cap();
		currPoints = currSeg._segmentPoints;
		currPoints.add({ __x: capStartXPos, __y: width, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currPoints.add({ __x: capEndXPos, __y: width, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currSeg = frame.capToFeature();
		currPoints = currSeg._segmentPoints;
		currPoints.add({ __x: capEndXPos, __y: width, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currPoints.add({ __x: pointFeaturePos, __y: width, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currSeg = frame.pointFeature();
		currPoints = currSeg._segmentPoints;
		currPoints.add({ __x: pointFeaturePos, __y: width, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currPoints.add({ __x: pointFeaturePos, __y: width, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currSeg = frame.featureToPoint();
		currPoints = currSeg._segmentPoints;
		currPoints.add({ __x: pointFeaturePos, __y: width, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currPoints.add({ __x: maxExtent, __y: width, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currSeg = frame.point();
		currPoints = currSeg._segmentPoints;
		currPoints.add({ __x: maxExtent, __y: width, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currPoints.add({ __x: maxExtent, __y: underWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currSeg = frame.pointToFeatureUnderside();
		currPoints = currSeg._segmentPoints;
		currPoints.add({ __x: maxExtent, __y: underWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currPoints.add({ __x: pointFeaturePos, __y: underWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currSeg = frame.pointFeatureUnderside();
		currPoints = currSeg._segmentPoints;
		currPoints.add({ __x: pointFeaturePos, __y: underWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currPoints.add({ __x: pointFeaturePos, __y: underWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currSeg = frame.featureToCapUnderside();
		currPoints = currSeg._segmentPoints;
		currPoints.add({ __x: pointFeaturePos, __y: underWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currPoints.add({ __x: capEndXPos, __y: underWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currSeg = frame.capUnderside();
		currPoints = currSeg._segmentPoints;
		currPoints.add({ __x: capEndXPos, __y: underWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currPoints.add({ __x: capStartXPos, __y: underWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currSeg = frame.capToFeatureUnderside();
		currPoints = currSeg._segmentPoints;
		currPoints.add({ __x: capStartXPos, __y: underWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currPoints.add({ __x: baseFeaturePos, __y: underWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currSeg = frame.baseFeatureUnderside();
		currPoints = currSeg._segmentPoints;
		currPoints.add({ __x: baseFeaturePos, __y: underWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currPoints.add({ __x: baseFeaturePos, __y: underWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currSeg = frame.featureToBaseUnderside();
		currPoints = currSeg._segmentPoints;
		currPoints.add({ __x: baseFeaturePos, __y: underWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		currPoints.add({ __x: minExtent, __y: underWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
	}
	,
	$type: new $.ig.Type('RadialGaugeNeedlePreparer', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('RadialGaugeNeedleParameters', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	_minExtent: 0,
	_maxExtent: 0,
	_minWidth: 0,
	_maxWidth: 0,
	_baseFeatureWidth: 0,
	_baseFeatureExtent: 0,
	_pointFeatureWidth: 0,
	_pointFeatureExtent: 0,
	_featurePointExtent: 0,
	_needleShape: 0,
	_capShape: 0,
	_capWidth: 0,
	_capFill: null,
	_capOutline: null,
	_capStrokeThickness: 0,
	_needleBrush: null,
	_needleOutline: null,
	_needleStrokeThickness: 0,
	_capPos: 0,
	_capInnerWidth: 0,
	clone: function () {
		var ret = new $.ig.RadialGaugeNeedleParameters();
		ret._minExtent = this._minExtent;
		ret._maxExtent = this._maxExtent;
		ret._minWidth = this._minWidth;
		ret._maxWidth = this._maxWidth;
		ret._baseFeatureWidth = this._baseFeatureWidth;
		ret._baseFeatureExtent = this._baseFeatureExtent;
		ret._pointFeatureWidth = this._pointFeatureWidth;
		ret._pointFeatureExtent = this._pointFeatureExtent;
		ret._needleShape = this._needleShape;
		ret._capShape = this._capShape;
		ret._capWidth = this._capWidth;
		ret._capFill = this._capFill;
		ret._capOutline = this._capOutline;
		ret._capStrokeThickness = this._capStrokeThickness;
		ret._needleBrush = this._needleBrush;
		ret._needleOutline = this._needleOutline;
		ret._needleStrokeThickness = this._needleStrokeThickness;
		ret._capPos = this._capPos;
		ret._capInnerWidth = this._capInnerWidth;
		return ret;
	}
	,
	$type: new $.ig.Type('RadialGaugeNeedleParameters', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('XamRadialGaugeRange', 'DependencyObject', {
	init: function () {
		$.ig.DependencyObject.prototype.init.call(this);
	},
	propertyUpdateOverride: function (propertyName, oldValue, newValue) {
	}
	,
	onPropertyChanged: function (propertyName, oldValue, newValue) {
		this.propertyUpdateOverride(propertyName, oldValue, newValue);
		if (this.propertyChanged != null) {
			this.propertyChanged(this, new $.ig.PropertyChangedEventArgs(propertyName));
		}
	}
	,
	propertyChanged: null,
	name: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGaugeRange.prototype.nameProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGaugeRange.prototype.nameProperty);
		}
	}
	,
	brush: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGaugeRange.prototype.brushProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGaugeRange.prototype.brushProperty);
		}
	}
	,
	outline: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGaugeRange.prototype.outlineProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGaugeRange.prototype.outlineProperty);
		}
	}
	,
	startValue: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGaugeRange.prototype.startValueProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGaugeRange.prototype.startValueProperty);
		}
	}
	,
	endValue: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGaugeRange.prototype.endValueProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGaugeRange.prototype.endValueProperty);
		}
	}
	,
	innerStartExtent: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGaugeRange.prototype.innerStartExtentProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGaugeRange.prototype.innerStartExtentProperty);
		}
	}
	,
	innerEndExtent: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGaugeRange.prototype.innerEndExtentProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGaugeRange.prototype.innerEndExtentProperty);
		}
	}
	,
	outerStartExtent: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGaugeRange.prototype.outerStartExtentProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGaugeRange.prototype.outerStartExtentProperty);
		}
	}
	,
	outerEndExtent: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGaugeRange.prototype.outerEndExtentProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGaugeRange.prototype.outerEndExtentProperty);
		}
	}
	,
	strokeThickness: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGaugeRange.prototype.strokeThicknessProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGaugeRange.prototype.strokeThicknessProperty);
		}
	}
	,
	$type: new $.ig.Type('XamRadialGaugeRange', $.ig.DependencyObject.prototype.$type, [$.ig.INotifyPropertyChanged.prototype.$type])
}, true);

$.ig.util.defType('RadialGaugeRangeCollection', 'ObservableCollection$1', {
	init: function () {
		$.ig.ObservableCollection$1.prototype.init.call(this, $.ig.XamRadialGaugeRange.prototype.$type, 0);
	},
	$type: new $.ig.Type('RadialGaugeRangeCollection', $.ig.ObservableCollection$1.prototype.$type.specialize($.ig.XamRadialGaugeRange.prototype.$type))
}, true);

$.ig.util.defType('XamRadialGauge', 'Control', {
	init: function () {
		var $self = this;
		this.__actualMinimumValue = $.ig.XamRadialGauge.prototype.minimumValueDefaultValue;
		this.__actualMaximumValue = $.ig.XamRadialGauge.prototype.maximumValueDefaultValue;
		this.__isDragging = false;
		this.__lastSize = new $.ig.Size(1, NaN, NaN);
		this.__displayedLabels = new $.ig.List$1($.ig.TextBlock.prototype.$type, 0);
		this.__displayedTicks = new $.ig.List$1($.ig.Path.prototype.$type, 0);
		this.__displayedRanges = new $.ig.List$1($.ig.Path.prototype.$type, 0);
		this.__displayedMinorTicks = new $.ig.List$1($.ig.Path.prototype.$type, 0);
		this.__transitionProgress = 0;
		$.ig.Control.prototype.init.call(this);
		this.view(new $.ig.XamRadialGaugeView(this));
		this.view().onInit();
		this.__viewport = $.ig.Rect.prototype.empty();
		this.previousFrame(new $.ig.RadialGaugeFrame());
		this.nextFrame(new $.ig.RadialGaugeFrame());
		this.currentFrame(new $.ig.RadialGaugeFrame());
		this.ranges(new $.ig.RadialGaugeRangeCollection());
		this.isDirty(true);
		this.animator(new $.ig.DoubleAnimator(0, 1, this.transitionDuration()));
		var $t = this.animator();
		$t.propertyChanged = $.ig.Delegate.prototype.combine($t.propertyChanged, this.animator_PropertyChanged.runOn(this));
		this.defaultStyleKey($.ig.XamRadialGauge.prototype.$type);
		this.labels((function () {
			var $ret = new $.ig.StackPool$1($.ig.TextBlock.prototype.$type);
			$ret.activate($self.view().labelActivate.runOn($self.view()));
			$ret.deactivate($self.view().labelDeactivate.runOn($self.view()));
			$ret.destroy($self.view().labelDestroy.runOn($self.view()));
			$ret.create($self.view().labelCreate.runOn($self.view()));
			return $ret;
		}()));
		this.ticks((function () {
			var $ret = new $.ig.StackPool$1($.ig.Path.prototype.$type);
			$ret.activate($self.view().pathActivate.runOn($self.view()));
			$ret.deactivate($self.view().pathDeactivate.runOn($self.view()));
			$ret.destroy($self.view().pathDestroy.runOn($self.view()));
			$ret.create($self.view().pathCreate.runOn($self.view()));
			return $ret;
		}()));
		this.minorTicks((function () {
			var $ret = new $.ig.StackPool$1($.ig.Path.prototype.$type);
			$ret.activate($self.view().pathActivate.runOn($self.view()));
			$ret.deactivate($self.view().pathDeactivate.runOn($self.view()));
			$ret.destroy($self.view().pathDestroy.runOn($self.view()));
			$ret.create($self.view().pathCreate.runOn($self.view()));
			return $ret;
		}()));
		this.rangeShapes((function () {
			var $ret = new $.ig.StackPool$1($.ig.Path.prototype.$type);
			$ret.activate($self.view().rangePathActivate.runOn($self.view()));
			$ret.deactivate($self.view().rangePathDeactivate.runOn($self.view()));
			$ret.destroy($self.view().rangePathDestroy.runOn($self.view()));
			$ret.create($self.view().rangePathCreate.runOn($self.view()));
			return $ret;
		}()));
		this.scaler(new $.ig.RadialGaugeScaler(this.actualMinimumValue(), this.actualMaximumValue(), this.scaleStartAngle(), this.scaleEndAngle(), this.scaleSweepDirection()));
	},
	scaleValue: function (value) {
		return this.scaler().scale(value);
	}
	,
	unscaleValue: function (angle) {
		return this.scaler().unscale(angle);
	}
	,
	getValueForPoint: function (point) {
		var viewport = this.viewport();
		var radius = Math.min(viewport.width() / 2, viewport.height() / 2) * this.radiusMultiplier();
		var center = this.getGaugeCenter();
		var centerX = center.__x;
		var centerY = center.__y;
		var angle = $.ig.GeometryUtil.prototype.getAngleTo({ __x: centerX, __y: centerY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }, point);
		if (this.scaler() != null && angle < this.scaler().startAngle()) {
			angle += Math.PI * 2;
		}
		if (this.scaler() != null && angle > this.scaler().endAngle()) {
			angle -= Math.PI * 2;
		}
		var value = this.unscaleValue(angle);
		return value;
	}
	,
	getPointForValue: function (value, extent) {
		var angle = this.scaleValue(value);
		var radius = Math.min(this.viewport().width() / 2, this.viewport().height() / 2) * this.radiusMultiplier() * extent;
		var center = this.getGaugeCenter();
		var x = center.__x + Math.cos(angle) * radius;
		var y = center.__y + Math.sin(angle) * radius;
		return { __x: x, __y: y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
	}
	,
	getBrushByIndex: function (index, brushes) {
		if (brushes != null && brushes.count() > 0) {
			return brushes.item(index % brushes.count());
		} else {
			return null;
		}
	}
	,
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
	animator_PropertyChanged: function (sender, e) {
		this.transitionProgress(this.animator().transitionProgress());
	}
	,
	_animator: null,
	animator: function (value) {
		if (arguments.length === 1) {
			this._animator = value;
			return value;
		} else {
			return this._animator;
		}
	}
	,
	rangeBrushes: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.rangeBrushesProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.rangeBrushesProperty);
		}
	}
	,
	rangeOutlines: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.rangeOutlinesProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.rangeOutlinesProperty);
		}
	}
	,
	minimumValue: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.minimumValueProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.minimumValueProperty);
		}
	}
	,
	__actualMinimumValue: 0,
	actualMinimumValue: function (value) {
		if (arguments.length === 1) {
			this.__actualMinimumValue = value;
			return value;
		} else {
			return this.__actualMinimumValue;
		}
	}
	,
	maximumValue: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.maximumValueProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.maximumValueProperty);
		}
	}
	,
	__actualMaximumValue: 0,
	actualMaximumValue: function (value) {
		if (arguments.length === 1) {
			this.__actualMaximumValue = value;
			return value;
		} else {
			return this.__actualMaximumValue;
		}
	}
	,
	interval: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.intervalProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.intervalProperty);
		}
	}
	,
	centerX: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.centerXProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.centerXProperty);
		}
	}
	,
	centerY: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.centerYProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.centerYProperty);
		}
	}
	,
	value: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.valueProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.valueProperty);
		}
	}
	,
	scaleStartAngle: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.scaleStartAngleProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.scaleStartAngleProperty);
		}
	}
	,
	scaleEndAngle: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.scaleEndAngleProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.scaleEndAngleProperty);
		}
	}
	,
	scaleSweepDirection: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.scaleSweepDirectionProperty, $.ig.SweepDirection.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.XamRadialGauge.prototype.scaleSweepDirectionProperty));
		}
	}
	,
	transitionDuration: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.transitionDurationProperty, value);
			return value;
		} else {
			return $.ig.util.getValue(this.getValue($.ig.XamRadialGauge.prototype.transitionDurationProperty));
		}
	}
	,
	transitionEasingFunction: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.transitionEasingFunctionProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.transitionEasingFunctionProperty);
		}
	}
	,
	needleBrush: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.needleBrushProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.needleBrushProperty);
		}
	}
	,
	needleOutline: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.needleOutlineProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.needleOutlineProperty);
		}
	}
	,
	needleStartExtent: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.needleStartExtentProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.needleStartExtentProperty);
		}
	}
	,
	needleEndExtent: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.needleEndExtentProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.needleEndExtentProperty);
		}
	}
	,
	needleShape: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.needleShapeProperty, $.ig.RadialGaugeNeedleShape.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.XamRadialGauge.prototype.needleShapeProperty));
		}
	}
	,
	needleStartWidthRatio: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.needleStartWidthRatioProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.needleStartWidthRatioProperty);
		}
	}
	,
	needleEndWidthRatio: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.needleEndWidthRatioProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.needleEndWidthRatioProperty);
		}
	}
	,
	needleBaseFeatureWidthRatio: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.needleBaseFeatureWidthRatioProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.needleBaseFeatureWidthRatioProperty);
		}
	}
	,
	needleBaseFeatureExtent: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.needleBaseFeatureExtentProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.needleBaseFeatureExtentProperty);
		}
	}
	,
	needlePointFeatureWidthRatio: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.needlePointFeatureWidthRatioProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.needlePointFeatureWidthRatioProperty);
		}
	}
	,
	needlePointFeatureExtent: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.needlePointFeatureExtentProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.needlePointFeatureExtentProperty);
		}
	}
	,
	needlePivotWidthRatio: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.needlePivotWidthRatioProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.needlePivotWidthRatioProperty);
		}
	}
	,
	needlePivotInnerWidthRatio: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.needlePivotInnerWidthRatioProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.needlePivotInnerWidthRatioProperty);
		}
	}
	,
	needlePivotShape: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.needlePivotShapeProperty, $.ig.RadialGaugePivotShape.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.XamRadialGauge.prototype.needlePivotShapeProperty));
		}
	}
	,
	scaleStartExtent: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.scaleStartExtentProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.scaleStartExtentProperty);
		}
	}
	,
	needlePivotBrush: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.needlePivotBrushProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.needlePivotBrushProperty);
		}
	}
	,
	needlePivotOutline: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.needlePivotOutlineProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.needlePivotOutlineProperty);
		}
	}
	,
	needleStrokeThickness: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.needleStrokeThicknessProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.needleStrokeThicknessProperty);
		}
	}
	,
	needlePivotStrokeThickness: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.needlePivotStrokeThicknessProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.needlePivotStrokeThicknessProperty);
		}
	}
	,
	scaleEndExtent: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.scaleEndExtentProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.scaleEndExtentProperty);
		}
	}
	,
	labelExtent: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.labelExtentProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.labelExtentProperty);
		}
	}
	,
	labelInterval: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.labelIntervalProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.labelIntervalProperty);
		}
	}
	,
	tickStartExtent: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.tickStartExtentProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.tickStartExtentProperty);
		}
	}
	,
	tickEndExtent: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.tickEndExtentProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.tickEndExtentProperty);
		}
	}
	,
	tickStrokeThickness: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.tickStrokeThicknessProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.tickStrokeThicknessProperty);
		}
	}
	,
	tickBrush: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.tickBrushProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.tickBrushProperty);
		}
	}
	,
	fontBrush: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.fontBrushProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.fontBrushProperty);
		}
	}
	,
	minorTickStartExtent: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.minorTickStartExtentProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.minorTickStartExtentProperty);
		}
	}
	,
	minorTickEndExtent: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.minorTickEndExtentProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.minorTickEndExtentProperty);
		}
	}
	,
	minorTickStrokeThickness: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.minorTickStrokeThicknessProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.minorTickStrokeThicknessProperty);
		}
	}
	,
	minorTickBrush: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.minorTickBrushProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.minorTickBrushProperty);
		}
	}
	,
	minorTickCount: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.minorTickCountProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.minorTickCountProperty);
		}
	}
	,
	scaleBrush: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.scaleBrushProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.scaleBrushProperty);
		}
	}
	,
	backingBrush: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.backingBrushProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.backingBrushProperty);
		}
	}
	,
	backingOutline: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.backingOutlineProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.backingOutlineProperty);
		}
	}
	,
	backingStrokeThickness: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.backingStrokeThicknessProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.backingStrokeThicknessProperty);
		}
	}
	,
	backingOuterExtent: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.backingOuterExtentProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.backingOuterExtentProperty);
		}
	}
	,
	backingOversweep: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.backingOversweepProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.backingOversweepProperty);
		}
	}
	,
	scaleOversweep: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.scaleOversweepProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.scaleOversweepProperty);
		}
	}
	,
	scaleOversweepShape: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.scaleOversweepShapeProperty, $.ig.RadialGaugeScaleOversweepShape.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.XamRadialGauge.prototype.scaleOversweepShapeProperty));
		}
	}
	,
	backingCornerRadius: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.backingCornerRadiusProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.backingCornerRadiusProperty);
		}
	}
	,
	backingInnerExtent: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.backingInnerExtentProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.backingInnerExtentProperty);
		}
	}
	,
	backingShape: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.backingShapeProperty, $.ig.RadialGaugeBackingShape.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.XamRadialGauge.prototype.backingShapeProperty));
		}
	}
	,
	ranges: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.rangesProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.rangesProperty);
		}
	}
	,
	radiusMultiplier: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.radiusMultiplierProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.radiusMultiplierProperty);
		}
	}
	,
	duplicateLabelOmissionStrategy: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.duplicateLabelOmissionStrategyProperty, $.ig.RadialGaugeDuplicateLabelOmissionStrategy.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.XamRadialGauge.prototype.duplicateLabelOmissionStrategyProperty));
		}
	}
	,
	__isDragging: false,
	isNeedleDraggingEnabled: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.isNeedleDraggingEnabledProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.isNeedleDraggingEnabledProperty);
		}
	}
	,
	isNeedleDraggingConstrained: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.isNeedleDraggingConstrainedProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.isNeedleDraggingConstrainedProperty);
		}
	}
	,
	font: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamRadialGauge.prototype.fontProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamRadialGauge.prototype.fontProperty);
		}
	}
	,
	formatLabel: null,
	alignLabel: null,
	valueChanged: null,
	_isDirty: false,
	isDirty: function (value) {
		if (arguments.length === 1) {
			this._isDirty = value;
			return value;
		} else {
			return this._isDirty;
		}
	}
	,
	_scaler: null,
	scaler: function (value) {
		if (arguments.length === 1) {
			this._scaler = value;
			return value;
		} else {
			return this._scaler;
		}
	}
	,
	refresh: function () {
		this.isDirty(true);
		this.view().scheduleArrange();
	}
	,
	onPropertyChanged: function (propertyName, oldValue, newValue) {
		switch (propertyName) {
			case $.ig.XamRadialGauge.prototype.needleBrushPropertyName:
			case $.ig.XamRadialGauge.prototype.needleOutlinePropertyName:
			case $.ig.XamRadialGauge.prototype.needlePivotBrushPropertyName:
			case $.ig.XamRadialGauge.prototype.needlePivotOutlinePropertyName:
			case $.ig.XamRadialGauge.prototype.backingBrushPropertyName:
			case $.ig.XamRadialGauge.prototype.backingOutlinePropertyName:
			case $.ig.XamRadialGauge.prototype.scaleBrushPropertyName:
			case $.ig.XamRadialGauge.prototype.tickBrushPropertyName:
			case $.ig.XamRadialGauge.prototype.minorTickBrushPropertyName:
				this.view().onBrushChanged(propertyName, oldValue, newValue);
				break;
			case $.ig.XamRadialGauge.prototype.rangeBrushesPropertyName:
				this.view().onRangeBrushesChanged(oldValue, newValue);
				break;
			case $.ig.XamRadialGauge.prototype.rangeOutlinesPropertyName:
				this.view().onRangeOutlinesChanged(oldValue, newValue);
				break;
		}
		switch (propertyName) {
			case $.ig.XamRadialGauge.prototype.valuePropertyName:
				if (this.valueChanged != null) {
					this.valueChanged(this, new $.ig.DoubleValueChangedEventArgs(oldValue, newValue));
				}
				this.isDirty(true);
				this.view().scheduleArrange();
				break;
			case $.ig.XamRadialGauge.prototype.backingBrushPropertyName:
			case $.ig.XamRadialGauge.prototype.backingOutlinePropertyName:
			case $.ig.XamRadialGauge.prototype.backingStrokeThicknessPropertyName:
			case $.ig.XamRadialGauge.prototype.backingShapePropertyName:
			case $.ig.XamRadialGauge.prototype.backingInnerExtentPropertyName:
			case $.ig.XamRadialGauge.prototype.backingOuterExtentPropertyName:
			case $.ig.XamRadialGauge.prototype.backingOversweepPropertyName:
			case $.ig.XamRadialGauge.prototype.backingCornerRadiusPropertyName:
				this.isDirty(true);
				this.view().scheduleArrange();
				break;
			case $.ig.XamRadialGauge.prototype.scaleSweepDirectionPropertyName:
			case $.ig.XamRadialGauge.prototype.scaleStartAnglePropertyName:
			case $.ig.XamRadialGauge.prototype.scaleEndAnglePropertyName:
			case $.ig.XamRadialGauge.prototype.scaleOversweepPropertyName:
			case $.ig.XamRadialGauge.prototype.scaleOversweepShapePropertyName:
				this.scaler(new $.ig.RadialGaugeScaler(this.actualMinimumValue(), this.actualMaximumValue(), this.scaleStartAngle(), this.scaleEndAngle(), this.scaleSweepDirection()));
				this.isDirty(true);
				this.view().scheduleArrange();
				break;
			case $.ig.XamRadialGauge.prototype.minimumValuePropertyName:
			case $.ig.XamRadialGauge.prototype.maximumValuePropertyName:
				var minimum = this.minimumValue();
				var maximum = this.maximumValue();
				if (Number.isInfinity(minimum) || $.ig.util.isNaN(minimum)) {
					minimum = $.ig.XamRadialGauge.prototype.minimumValueDefaultValue;
				}
				if (Number.isInfinity(maximum) || $.ig.util.isNaN(maximum)) {
					maximum = $.ig.XamRadialGauge.prototype.maximumValueDefaultValue;
				}
				this.actualMinimumValue(Math.min(minimum, maximum));
				this.actualMaximumValue(Math.max(minimum, maximum));
				this.scaler(new $.ig.RadialGaugeScaler(this.actualMinimumValue(), this.actualMaximumValue(), this.scaleStartAngle(), this.scaleEndAngle(), this.scaleSweepDirection()));
				this.isDirty(true);
				this.view().scheduleArrange();
				break;
			case $.ig.XamRadialGauge.prototype.intervalPropertyName:
			case $.ig.XamRadialGauge.prototype.scaleStartExtentPropertyName:
			case $.ig.XamRadialGauge.prototype.scaleEndExtentPropertyName:
			case $.ig.XamRadialGauge.prototype.labelExtentPropertyName:
			case $.ig.XamRadialGauge.prototype.tickStartExtentPropertyName:
			case $.ig.XamRadialGauge.prototype.tickEndExtentPropertyName:
			case $.ig.XamRadialGauge.prototype.tickBrushPropertyName:
			case $.ig.XamRadialGauge.prototype.scaleBrushPropertyName:
			case $.ig.XamRadialGauge.prototype.labelIntervalPropertyName:
			case $.ig.XamRadialGauge.prototype.minorTickCountPropertyName:
			case $.ig.XamRadialGauge.prototype.tickStrokeThicknessPropertyName:
			case $.ig.XamRadialGauge.prototype.minorTickStrokeThicknessPropertyName:
			case $.ig.XamRadialGauge.prototype.minorTickBrushPropertyName:
			case $.ig.XamRadialGauge.prototype.minorTickStartExtentPropertyName:
			case $.ig.XamRadialGauge.prototype.minorTickEndExtentPropertyName:
			case $.ig.XamRadialGauge.prototype.centerXPropertyName:
			case $.ig.XamRadialGauge.prototype.centerYPropertyName:
			case $.ig.XamRadialGauge.prototype.radiusMultiplierPropertyName:
			case $.ig.XamRadialGauge.prototype.duplicateLabelOmissionStrategyPropertyName:
				this.scaler(new $.ig.RadialGaugeScaler(this.actualMinimumValue(), this.actualMaximumValue(), this.scaleStartAngle(), this.scaleEndAngle(), this.scaleSweepDirection()));
				this.isDirty(true);
				this.view().scheduleArrange();
				break;
			case $.ig.XamRadialGauge.prototype.needleStartExtentPropertyName:
			case $.ig.XamRadialGauge.prototype.needleEndExtentPropertyName:
			case $.ig.XamRadialGauge.prototype.needleBrushPropertyName:
			case $.ig.XamRadialGauge.prototype.needleOutlinePropertyName:
			case $.ig.XamRadialGauge.prototype.needleStartWidthRatioPropertyName:
			case $.ig.XamRadialGauge.prototype.needleEndWidthRatioPropertyName:
			case $.ig.XamRadialGauge.prototype.needlePivotBrushPropertyName:
			case $.ig.XamRadialGauge.prototype.needlePivotOutlinePropertyName:
			case $.ig.XamRadialGauge.prototype.needleStrokeThicknessPropertyName:
			case $.ig.XamRadialGauge.prototype.needlePivotStrokeThicknessPropertyName:
			case $.ig.XamRadialGauge.prototype.needleBaseFeatureExtentPropertyName:
			case $.ig.XamRadialGauge.prototype.needleBaseFeatureWidthRatioPropertyName:
			case $.ig.XamRadialGauge.prototype.needlePointFeatureExtentPropertyName:
			case $.ig.XamRadialGauge.prototype.needlePointFeatureWidthRatioPropertyName:
			case $.ig.XamRadialGauge.prototype.needlePivotWidthRatioPropertyName:
			case $.ig.XamRadialGauge.prototype.needlePivotInnerWidthRatioPropertyName:
			case $.ig.XamRadialGauge.prototype.needlePivotShapePropertyName:
			case $.ig.XamRadialGauge.prototype.needleShapePropertyName:
				this.isDirty(true);
				this.view().scheduleArrange();
				break;
			case $.ig.XamRadialGauge.prototype.transitionDurationPropertyName:
				this.animator().intervalMilliseconds(this.transitionDuration());
				this.view().scheduleArrange();
				break;
			case $.ig.XamRadialGauge.prototype.transitionEasingFunctionPropertyName:
				this.animator().easingFunction(this.transitionEasingFunction());
				break;
			case $.ig.XamRadialGauge.prototype.transitionProgressPropertyName:
				if (this.transitionProgress() >= 1) {
					this.animator().stop();
					var swap = this.nextFrame();
					this.nextFrame(this.previousFrame());
					this.previousFrame(swap);
					this.prepareBacking();
					this.prepareNeedle();
					this.prepareScale();
					this.renderBacking(this.nextFrame());
					this.renderNeedle(this.nextFrame());
					this.renderScale(this.nextFrame());
					this.view().arrangeComplete();
					return;
				}
				this.currentFrame().interpolate(this.transitionProgress(), this.previousFrame(), this.nextFrame());
				this.renderBacking(this.currentFrame());
				this.renderNeedle(this.currentFrame());
				this.renderScale(this.currentFrame());
				this.view().arrangeComplete();
				break;
			case $.ig.XamRadialGauge.prototype.rangesPropertyName:
				var oldRanges = oldValue;
				var newRanges = newValue;
				if (oldRanges != null) {
					var $t = (oldRanges);
					$t.collectionChanged = $.ig.Delegate.prototype.remove($t.collectionChanged, this.ranges_CollectionChanged.runOn(this));
				}
				if (newRanges != null) {
					var $t1 = (newRanges);
					$t1.collectionChanged = $.ig.Delegate.prototype.combine($t1.collectionChanged, this.ranges_CollectionChanged.runOn(this));
				}
				this.isDirty(true);
				this.view().scheduleArrange();
				break;
			case $.ig.XamRadialGauge.prototype.rangeOutlinesPropertyName:
			case $.ig.XamRadialGauge.prototype.rangeBrushesPropertyName:
				this.isDirty(true);
				this.view().scheduleArrange();
				break;
			case $.ig.XamRadialGauge.prototype.fontBrushPropertyName:
			case $.ig.XamRadialGauge.prototype.fontPropertyName:
				this.isDirty(true);
				this.view().updateStyle();
				this.view().scheduleArrange();
				break;
		}
	}
	,
	styleUpdated: function () {
		this.view().styleUpdated();
	}
	,
	ranges_CollectionChanged: function (sender, e) {
		if (e.oldItems() != null) {
			var en = e.oldItems().getEnumerator();
			while (en.moveNext()) {
				var item = en.current();
				item.propertyChanged = $.ig.Delegate.prototype.remove(item.propertyChanged, this.range_PropertyChanged.runOn(this));
			}
		}
		if (e.newItems() != null) {
			var en1 = e.newItems().getEnumerator();
			while (en1.moveNext()) {
				var item1 = en1.current();
				item1.propertyChanged = $.ig.Delegate.prototype.combine(item1.propertyChanged, this.range_PropertyChanged.runOn(this));
			}
		}
		this.isDirty(true);
		this.view().scheduleArrange();
	}
	,
	range_PropertyChanged: function (sender, e) {
		this.isDirty(true);
		this.view().scheduleArrange();
	}
	,
	__lastSize: null,
	__viewport: null,
	viewport: function (value) {
		if (arguments.length === 1) {
			var oldViewport = this.__viewport;
			this.__viewport = value;
			if (!oldViewport.equals1(this.__viewport)) {
				this.onViewportChanged(oldViewport, this.__viewport);
			}
			return value;
		} else {
			return this.__viewport;
		}
	}
	,
	onViewportChanged: function (oldViewport, newViewport) {
		this.isDirty(true);
		this.view().scheduleArrange();
	}
	,
	isValid: function () {
		return !$.ig.util.isNaN(this.actualMinimumValue()) && !$.ig.util.isNaN(this.actualMaximumValue()) && !$.ig.util.isNaN(this.value()) && !this.viewport().isEmpty() && this.view().ready();
	}
	,
	arrangeGauge: function () {
		if (!this.isValid()) {
			return;
		}
		if (this.isDirty()) {
			if (this.animating()) {
				if (this.animator().needsFlush()) {
					this.animator().flush();
				}
				var previousFrame = this.previousFrame();
				this.previousFrame(this.currentFrame());
				this.currentFrame(previousFrame);
			} else {
				var previousFrame1 = this.previousFrame();
				this.previousFrame(this.nextFrame());
				this.nextFrame(previousFrame1);
			}
		} else if (this.animating()) {
			this.currentFrame().interpolate(this.transitionProgress(), this.previousFrame(), this.nextFrame());
			this.renderBacking(this.currentFrame());
			this.renderNeedle(this.currentFrame());
			this.renderScale(this.currentFrame());
		}
		if (this.isDirty()) {
			this.prepareNeedle();
			this.prepareScale();
			this.prepareBacking();
		}
		if (this.isDirty() && this.shouldAnimate()) {
			this.startAnimation();
			this.currentFrame().interpolate(this.transitionProgress(), this.previousFrame(), this.nextFrame());
			this.renderBacking(this.currentFrame());
			this.renderNeedle(this.currentFrame());
			this.renderScale(this.currentFrame());
		} else {
			if (this.isDirty()) {
				this.renderBacking(this.nextFrame());
				this.renderNeedle(this.nextFrame());
				this.renderScale(this.nextFrame());
			}
		}
		this.isDirty(false);
		this.view().arrangeComplete();
	}
	,
	startAnimation: function () {
		this.__transitionProgress = 0;
		this.animator().stop();
		this.animator().start();
	}
	,
	shouldAnimate: function () {
		return this.transitionDuration() > 0;
	}
	,
	renderBacking: function (frame) {
		var viewport = this.viewport();
		var backingPath = this.view().getBackingPath();
		var backingFrame = frame.backingFrame();
		var radius = Math.min(viewport.width() / 2, viewport.height() / 2) * frame.radiusMultiplier();
		var center = this.getGaugeCenter();
		var centerX = center.__x;
		var centerY = center.__y;
		var geo = backingFrame.getPathGeometry(radius, centerX, centerY);
		backingPath.data(geo);
		backingPath.__fill = backingFrame.brush();
		backingPath.__stroke = backingFrame.outline();
		backingPath.strokeThickness(backingFrame.strokeThickness());
	}
	,
	__needleBoundingPoints: null,
	__needleTouchBoundingPoints: null,
	needleContainsPoint: function (point) {
		if (this.__needleBoundingPoints == null) {
			return false;
		}
		return $.ig.PolygonUtil.prototype.polygonContainsPoint(this.__needleBoundingPoints, point);
	}
	,
	needleContainsPoint1: function (point, isFinger) {
		if (this.__needleBoundingPoints == null) {
			return false;
		}
		return isFinger ? $.ig.PolygonUtil.prototype.polygonContainsPoint(this.__needleTouchBoundingPoints, point) : $.ig.PolygonUtil.prototype.polygonContainsPoint(this.__needleBoundingPoints, point);
	}
	,
	renderNeedle: function (frame) {
		var viewport = this.viewport();
		var needleAngle = frame.needleFrame().needleAngle();
		var needleBrush = frame.needleFrame().needleBrush();
		var needleOutline = frame.needleFrame().needleOutline();
		var needleStrokeThickness = frame.needleFrame().needleStrokeThickness();
		var overlayBrush = frame.needleFrame().capBrush();
		var overlayStroke = frame.needleFrame().capOutline();
		var overlayThickness = frame.needleFrame().capStrokeThickness();
		var needlePath = this.view().getNeedlePath();
		var underlayPath = this.view().getUnderlayPath();
		var overlayPath = this.view().getOverlayPath();
		var needleFrame = frame.needleFrame();
		var radius = Math.min(viewport.width() / 2, viewport.height() / 2) * frame.radiusMultiplier();
		var center = this.getGaugeCenter();
		var centerX = center.__x;
		var centerY = center.__y;
		var geo = needleFrame.getPathGeometry(radius, centerX, centerY);
		var angleRad = needleAngle * 180 / Math.PI;
		needlePath.data(geo);
		var trans = new $.ig.TransformGroup();
		trans.children().add((function () {
			var $ret = new $.ig.RotateTransform();
			$ret.centerX(centerX);
			$ret.centerY(centerY);
			$ret.angle(angleRad);
			return $ret;
		}()));
		needlePath.renderTransform(trans);
		needlePath.__fill = needleBrush;
		needlePath.__stroke = needleOutline;
		needlePath.strokeThickness(needleStrokeThickness);
		underlayPath.data(needleFrame.getUnderlayPathGeometry(radius, centerX, centerY));
		overlayPath.data(needleFrame.getOverlayPathGeometry(radius, centerX, centerY));
		underlayPath.__fill = overlayBrush;
		underlayPath.__stroke = overlayStroke;
		underlayPath.strokeThickness(overlayThickness);
		overlayPath.__fill = overlayBrush;
		overlayPath.__stroke = overlayStroke;
		overlayPath.strokeThickness(overlayThickness);
		var needleBoundingPoints = new $.ig.List$1($.ig.Point.prototype.$type, 0);
		var cosAngle = Math.cos(needleAngle);
		var sinAngle = Math.sin(needleAngle);
		var needleWidth = needleFrame.needleWidth() / 2;
		var p1 = { __x: needleFrame.needleStartExtent() * radius, __y: needleWidth * radius, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		var p2 = { __x: needleFrame.needleEndExtent() * radius, __y: needleWidth * radius, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		var p3 = { __x: needleFrame.needleEndExtent() * radius, __y: -needleWidth * radius, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		var p4 = { __x: needleFrame.needleStartExtent() * radius, __y: -needleWidth * radius, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		p1 = { __x: p1.__x * cosAngle - p1.__y * sinAngle + centerX, __y: p1.__y * cosAngle + p1.__x * sinAngle + centerY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		p2 = { __x: p2.__x * cosAngle - p2.__y * sinAngle + centerX, __y: p2.__y * cosAngle + p2.__x * sinAngle + centerY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		p3 = { __x: p3.__x * cosAngle - p3.__y * sinAngle + centerX, __y: p3.__y * cosAngle + p3.__x * sinAngle + centerY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		p4 = { __x: p4.__x * cosAngle - p4.__y * sinAngle + centerX, __y: p4.__y * cosAngle + p4.__x * sinAngle + centerY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		needleBoundingPoints.add(p1);
		needleBoundingPoints.add(p2);
		needleBoundingPoints.add(p3);
		needleBoundingPoints.add(p4);
		this.__needleBoundingPoints = needleBoundingPoints;
		needleWidth = (needleFrame.needleWidth() / 2) * radius;
		var b1 = { __x: needleFrame.needleStartExtent() * radius, __y: needleWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		var b2 = { __x: needleFrame.needleEndExtent() * radius, __y: needleWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		var b3 = { __x: needleFrame.needleEndExtent() * radius, __y: -needleWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		var b4 = { __x: needleFrame.needleStartExtent() * radius, __y: -needleWidth, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		b1 = { __x: b1.__x * cosAngle - b1.__y * sinAngle + centerX, __y: b1.__y * cosAngle + b1.__x * sinAngle + centerY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		b2 = { __x: b2.__x * cosAngle - b2.__y * sinAngle + centerX, __y: b2.__y * cosAngle + b2.__x * sinAngle + centerY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		b3 = { __x: b3.__x * cosAngle - b3.__y * sinAngle + centerX, __y: b3.__y * cosAngle + b3.__x * sinAngle + centerY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		b4 = { __x: b4.__x * cosAngle - b4.__y * sinAngle + centerX, __y: b4.__y * cosAngle + b4.__x * sinAngle + centerY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		this.__needleTouchBoundingPoints = new $.ig.List$1($.ig.Point.prototype.$type, 0);
		this.__needleTouchBoundingPoints.add(b1);
		this.__needleTouchBoundingPoints.add(b2);
		this.__needleTouchBoundingPoints.add(b3);
		this.__needleTouchBoundingPoints.add(b4);
	}
	,
	getGaugeCenter: function () {
		var viewport = this.viewport();
		var actualCenterX = $.ig.util.isNaN(this.centerX()) ? 0.5 : this.centerX();
		var actualCenterY = $.ig.util.isNaN(this.centerY()) ? 0.5 : this.centerY();
		var centerX = viewport.left() + actualCenterX * viewport.width();
		var centerY = viewport.top() + actualCenterY * viewport.height();
		return { __x: centerX, __y: centerY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
	}
	,
	sanitizeNeedleSetting: function (value) {
		if (Number.isInfinity(value)) {
			return NaN;
		}
		return value;
	}
	,
	prepareNeedle: function () {
		var frame = this.nextFrame();
		frame.radiusMultiplier(this.radiusMultiplier());
		var viewport = this.viewport();
		var scaler = this.scaler();
		var value = this.sanitizeScaleSettingToDefault(this.value(), 0);
		var needleAngle = this.scaler().scale(value);
		var needlePreparer = new $.ig.RadialGaugeNeedlePreparer();
		var p = new $.ig.RadialGaugeNeedleParameters();
		p._needleShape = this.needleShape();
		p._needleStrokeThickness = this.sanitizeNeedleSetting(this.needleStrokeThickness());
		p._needleBrush = this.needleBrush();
		p._needleOutline = this.needleOutline();
		p._minExtent = this.sanitizeNeedleSetting(this.needleStartExtent());
		p._maxExtent = this.sanitizeNeedleSetting(this.needleEndExtent());
		p._baseFeatureExtent = this.sanitizeNeedleSetting(this.needleBaseFeatureExtent());
		p._baseFeatureWidth = this.sanitizeNeedleSetting(this.needleBaseFeatureWidthRatio());
		p._pointFeatureExtent = this.sanitizeNeedleSetting(this.needlePointFeatureExtent());
		p._pointFeatureWidth = this.sanitizeNeedleSetting(this.needlePointFeatureWidthRatio());
		p._capWidth = this.sanitizeNeedleSetting(this.needlePivotWidthRatio());
		p._capInnerWidth = this.sanitizeNeedleSetting(this.needlePivotInnerWidthRatio());
		p._capFill = this.needlePivotBrush();
		p._capOutline = this.needlePivotOutline();
		p._capShape = this.needlePivotShape();
		p._capStrokeThickness = this.sanitizeNeedleSetting(this.needlePivotStrokeThickness());
		p._minWidth = this.sanitizeNeedleSetting(this.needleStartWidthRatio());
		p._maxWidth = this.sanitizeNeedleSetting(this.needleEndWidthRatio());
		needlePreparer.parameters(p);
		needlePreparer.prepareNeedle(frame.needleFrame());
		frame.needleFrame().needleAngle(needleAngle);
	}
	,
	_previousFrame: null,
	previousFrame: function (value) {
		if (arguments.length === 1) {
			this._previousFrame = value;
			return value;
		} else {
			return this._previousFrame;
		}
	}
	,
	_nextFrame: null,
	nextFrame: function (value) {
		if (arguments.length === 1) {
			this._nextFrame = value;
			return value;
		} else {
			return this._nextFrame;
		}
	}
	,
	_currentFrame: null,
	currentFrame: function (value) {
		if (arguments.length === 1) {
			this._currentFrame = value;
			return value;
		} else {
			return this._currentFrame;
		}
	}
	,
	__displayedLabels: null,
	__displayedTicks: null,
	__displayedRanges: null,
	__displayedMinorTicks: null,
	renderScale: function (frame) {
		var viewport = this.viewport();
		var scaleFrame = frame.scaleFrame();
		var labelAngles = scaleFrame.labelAngles();
		var labelStrings = scaleFrame.labels();
		var labelXOffsets = scaleFrame.labelXOffsets();
		var labelYOffsets = scaleFrame.labelYOffsets();
		var labelWidths = scaleFrame.labelWidths();
		var labelHeights = scaleFrame.labelHeights();
		var labelExtent = scaleFrame.labelExtent();
		var scaleStartExtent = scaleFrame.scaleStartExtent();
		var scaleEndExtent = scaleFrame.scaleEndExtent();
		var tickAngles = scaleFrame.tickAngles();
		var minorTickAngles = scaleFrame.minorTickAngles();
		var tickStartExtent = scaleFrame.tickStartExtent();
		var tickEndExtent = scaleFrame.tickEndExtent();
		var scaleStartAngle = scaleFrame.scaleStartAngle();
		var scaleEndAngle = scaleFrame.scaleEndAngle();
		var scaleBrush = scaleFrame.scaleBrush();
		var tickBrush = scaleFrame.tickBrush();
		var tickStrokeThickness = scaleFrame.tickStrokeThickness();
		var minorTickBrush = scaleFrame.minorTickBrush();
		var minorTickStartExtent = scaleFrame.minorTickStartExtent();
		var minorTickEndExtent = scaleFrame.minorTickEndExtent();
		var minorTickStrokeThickness = scaleFrame.minorTickStrokeThickness();
		var scalePath = this.view().getScalePath();
		var overSweep = scaleFrame.scaleOversweep();
		var isParallelSweep = true;
		if (this.scaleOversweepShape() == $.ig.RadialGaugeScaleOversweepShape.prototype.circular || (this.scaleOversweepShape() == $.ig.RadialGaugeScaleOversweepShape.prototype.auto && this.backingShape() == $.ig.RadialGaugeBackingShape.prototype.circular)) {
			isParallelSweep = false;
		}
		var radius = Math.min(viewport.width() / 2, viewport.height() / 2) * frame.radiusMultiplier();
		var center = this.getGaugeCenter();
		var centerX = center.__x;
		var centerY = center.__y;
		var origStartAngle = scaleStartAngle;
		var origEndAngle = scaleEndAngle;
		scaleStartAngle -= overSweep;
		scaleEndAngle += overSweep;
		var doCircle = false;
		if ((scaleEndAngle - scaleStartAngle) >= 2 * Math.PI) {
			doCircle = true;
			scaleStartAngle = 0;
			scaleEndAngle = Math.PI;
		}
		var scaleg = new $.ig.PathGeometry();
		var scalef = new $.ig.PathFigure();
		var cosAngle1 = Math.cos(scaleStartAngle);
		var sinAngle1 = Math.sin(scaleStartAngle);
		var cosAngle2 = Math.cos(scaleEndAngle);
		var sinAngle2 = Math.sin(scaleEndAngle);
		var p1x = centerX + (radius * scaleStartExtent) * cosAngle1;
		var p1y = centerY + (radius * scaleStartExtent) * sinAngle1;
		var p2x = centerX + (radius * scaleEndExtent) * cosAngle1;
		var p2y = centerY + (radius * scaleEndExtent) * sinAngle1;
		var p3x = centerX + (radius * scaleStartExtent) * cosAngle2;
		var p3y = centerY + (radius * scaleStartExtent) * sinAngle2;
		var p4x = centerX + (radius * scaleEndExtent) * cosAngle2;
		var p4y = centerY + (radius * scaleEndExtent) * sinAngle2;
		var beforeDoCircle = doCircle;
		if (overSweep != 0 && !doCircle && isParallelSweep) {
			var cosOrigAngle1 = Math.cos(origStartAngle);
			var sinOrigAngle1 = Math.sin(origStartAngle);
			var cosOrigAngle2 = Math.cos(origEndAngle);
			var sinOrigAngle2 = Math.sin(origEndAngle);
			var origp1x = centerX + (radius * scaleStartExtent) * cosOrigAngle1;
			var origp1y = centerY + (radius * scaleStartExtent) * sinOrigAngle1;
			var origp2x = centerX + (radius * scaleEndExtent) * cosOrigAngle1;
			var origp2y = centerY + (radius * scaleEndExtent) * sinOrigAngle1;
			var origp3x = centerX + (radius * scaleStartExtent) * cosOrigAngle2;
			var origp3y = centerY + (radius * scaleStartExtent) * sinOrigAngle2;
			var origp4x = centerX + (radius * scaleEndExtent) * cosOrigAngle2;
			var origp4y = centerY + (radius * scaleEndExtent) * sinOrigAngle2;
			var matchingInnerY = p1y;
			var matchingInnerX = p2x;
			if (origp2x - origp1x != 0) {
				matchingInnerX = centerX;
				var m = (origp2y - origp1y) / (origp2x - origp1x);
				matchingInnerY = m * (centerX - p2x) + p2y;
			}
			var newInnerPoint = { __x: matchingInnerX, __y: matchingInnerY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			var inter = $.ig.GeometryUtil.prototype.getCircleIntersection(newInnerPoint, { __x: p2x, __y: p2y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }, { __x: centerX, __y: centerY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }, radius * scaleStartExtent);
			var matchingInnerY2 = p3y;
			var matchingInnerX2 = p4x;
			if (origp4x - origp3x != 0) {
				matchingInnerX2 = centerX;
				var m1 = (origp4y - origp3y) / (origp4x - origp3x);
				matchingInnerY2 = m1 * (centerX - p4x) + p4y;
			}
			var newInnerPoint2 = { __x: matchingInnerX2, __y: matchingInnerY2, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			var inter2 = $.ig.GeometryUtil.prototype.getCircleIntersection(newInnerPoint2, { __x: p4x, __y: p4y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }, { __x: centerX, __y: centerY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }, radius * scaleStartExtent);
			if ($.ig.util.isNaN(inter.item1().__x) || $.ig.util.isNaN(inter.item1().__y) || $.ig.util.isNaN(inter.item2().__x) || $.ig.util.isNaN(inter.item2().__y) || $.ig.util.isNaN(inter2.item1().__x) || $.ig.util.isNaN(inter2.item1().__y) || $.ig.util.isNaN(inter2.item2().__x) || $.ig.util.isNaN(inter2.item2().__y)) {
				doCircle = true;
				scaleStartAngle = 0;
				scaleEndAngle = Math.PI;
			} else {
				var p1;
				var p2;
				if ((Math.pow(inter.item1().__x - p2x, 2) + Math.pow(inter.item1().__y - p2y, 2)) < (Math.pow(inter.item2().__x - p2x, 2) + Math.pow(inter.item2().__y - p2y, 2))) {
					p1 = inter.item1();
				} else {
					p1 = inter.item2();
				}
				if ((Math.pow(inter2.item1().__x - p4x, 2) + Math.pow(inter2.item1().__y - p4y, 2)) < (Math.pow(inter2.item2().__x - p4x, 2) + Math.pow(inter2.item2().__y - p4y, 2))) {
					p2 = inter2.item1();
				} else {
					p2 = inter2.item2();
				}
				if ((Math.pow(p2.__x - p2x, 2) + Math.pow(p2.__y - p2y, 2)) <= (Math.pow(p1.__x - p2x, 2) + Math.pow(p1.__y - p2y, 2))) {
					doCircle = true;
					scaleStartAngle = 0;
					scaleEndAngle = Math.PI;
				} else {
					var ang1 = $.ig.GeometryUtil.prototype.getAngleTo({ __x: centerX, __y: centerY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }, p1);
					var ang2 = $.ig.GeometryUtil.prototype.getAngleTo({ __x: centerX, __y: centerY, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName }, p2);
					var tempStart = scaleStartAngle;
					var tempEnd = scaleEndAngle;
					while (tempStart > Math.PI * 2) {
						tempStart -= Math.PI * 2;
					}
					while (tempEnd > Math.PI * 2) {
						tempEnd -= Math.PI * 2;
					}
					var delta1 = Math.min(Math.abs(ang1 - tempStart), Math.abs((Math.PI * 2 - ang1) - tempStart));
					var delta2 = Math.min(Math.abs(ang2 - tempEnd), Math.abs((Math.PI * 2 - ang2) - tempEnd));
					if ((scaleEndAngle - scaleStartAngle) + delta1 + delta2 >= 2 * Math.PI) {
						doCircle = true;
						scaleStartAngle = 0;
						scaleEndAngle = Math.PI;
					} else {
						p1x = p1.__x;
						p1y = p1.__y;
						p3x = p2.__x;
						p3y = p2.__y;
					}
				}
			}
		}
		if (doCircle && !beforeDoCircle) {
			cosAngle1 = Math.cos(scaleStartAngle);
			sinAngle1 = Math.sin(scaleStartAngle);
			cosAngle2 = Math.cos(scaleEndAngle);
			sinAngle2 = Math.sin(scaleEndAngle);
			p1x = centerX + (radius * scaleStartExtent) * cosAngle1;
			p1y = centerY + (radius * scaleStartExtent) * sinAngle1;
			p2x = centerX + (radius * scaleEndExtent) * cosAngle1;
			p2y = centerY + (radius * scaleEndExtent) * sinAngle1;
			p3x = centerX + (radius * scaleStartExtent) * cosAngle2;
			p3y = centerY + (radius * scaleStartExtent) * sinAngle2;
			p4x = centerX + (radius * scaleEndExtent) * cosAngle2;
			p4y = centerY + (radius * scaleEndExtent) * sinAngle2;
		}
		if (doCircle) {
			scalef.__startPoint = { __x: p2x, __y: p2y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			scalef.__segments.add((function () {
				var $ret = new $.ig.ArcSegment();
				$ret.point({ __x: p4x, __y: p4y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				$ret.isLargeArc(false);
				$ret.sweepDirection($.ig.SweepDirection.prototype.clockwise);
				$ret.size(new $.ig.Size(1, radius * scaleEndExtent, radius * scaleEndExtent));
				return $ret;
			}()));
			scalef.__segments.add((function () {
				var $ret = new $.ig.ArcSegment();
				$ret.point({ __x: p2x, __y: p2y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				$ret.isLargeArc(false);
				$ret.sweepDirection($.ig.SweepDirection.prototype.clockwise);
				$ret.size(new $.ig.Size(1, radius * scaleEndExtent, radius * scaleEndExtent));
				return $ret;
			}()));
			var scalef2 = new $.ig.PathFigure();
			scalef2.__startPoint = { __x: p1x, __y: p1y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			scalef2.__segments.add((function () {
				var $ret = new $.ig.ArcSegment();
				$ret.point({ __x: p3x, __y: p3y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				$ret.isLargeArc(false);
				$ret.sweepDirection($.ig.SweepDirection.prototype.counterclockwise);
				$ret.size(new $.ig.Size(1, radius * scaleStartExtent, radius * scaleStartExtent));
				return $ret;
			}()));
			scalef2.__segments.add((function () {
				var $ret = new $.ig.ArcSegment();
				$ret.point({ __x: p1x, __y: p1y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				$ret.isLargeArc(false);
				$ret.sweepDirection($.ig.SweepDirection.prototype.counterclockwise);
				$ret.size(new $.ig.Size(1, radius * scaleStartExtent, radius * scaleStartExtent));
				return $ret;
			}()));
			scaleg.figures().add(scalef);
			scaleg.figures().add(scalef2);
		} else {
			scalef.__startPoint = { __x: p1x, __y: p1y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			scalef.__segments.add((function () {
				var $ret = new $.ig.LineSegment(1);
				$ret.point({ __x: p2x, __y: p2y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				return $ret;
			}()));
			scalef.__segments.add((function () {
				var $ret = new $.ig.ArcSegment();
				$ret.point({ __x: p4x, __y: p4y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				$ret.isLargeArc((scaleEndAngle - scaleStartAngle) > Math.PI);
				$ret.sweepDirection($.ig.SweepDirection.prototype.clockwise);
				$ret.size(new $.ig.Size(1, radius * scaleEndExtent, radius * scaleEndExtent));
				return $ret;
			}()));
			scalef.__segments.add((function () {
				var $ret = new $.ig.LineSegment(1);
				$ret.point({ __x: p3x, __y: p3y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				return $ret;
			}()));
			scalef.__segments.add((function () {
				var $ret = new $.ig.ArcSegment();
				$ret.point({ __x: p1x, __y: p1y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				$ret.isLargeArc((scaleEndAngle - scaleStartAngle) > Math.PI);
				$ret.sweepDirection($.ig.SweepDirection.prototype.counterclockwise);
				$ret.size(new $.ig.Size(1, radius * scaleStartExtent, radius * scaleStartExtent));
				return $ret;
			}()));
			scaleg.figures().add(scalef);
		}
		scalePath.data(scaleg);
		scalePath.__fill = scaleBrush;
		var labels = this.labels();
		labels.deferDisactivate(true);
		for (var i = 0; i < this.__displayedLabels.count(); i++) {
			labels.push(this.__displayedLabels.__inner[i]);
		}
		this.__displayedLabels.clear();
		var fontBrush = scaleFrame.fontBrush();
		for (var i1 = 0; i1 < labelAngles.length; i1++) {
			var lx = centerX + (radius * labelExtent) * Math.cos(labelAngles[i1]);
			var ly = centerY + (radius * labelExtent) * Math.sin(labelAngles[i1]);
			var tb = labels.pop();
			tb.text(labelStrings[i1]);
			this.view().setTextBrush(tb, fontBrush);
			var labelWidth = labelWidths[i1];
			var labelHeight = labelHeights[i1];
			var xOffset = labelXOffsets[i1];
			var yOffset = labelYOffsets[i1];
			this.view().positionLabel(tb, lx + xOffset, ly + yOffset);
			this.__displayedLabels.add(tb);
		}
		labels.deferDisactivate(false);
		var ticks = this.ticks();
		ticks.deferDisactivate(true);
		for (var i2 = 0; i2 < this.__displayedTicks.count(); i2++) {
			ticks.push(this.__displayedTicks.__inner[i2]);
		}
		this.__displayedTicks.clear();
		for (var i3 = 0; i3 < tickAngles.length; i3++) {
			var tx1 = centerX + (radius * tickStartExtent) * Math.cos(tickAngles[i3]);
			var ty1 = centerY + (radius * tickStartExtent) * Math.sin(tickAngles[i3]);
			var tx2 = centerX + (radius * tickEndExtent) * Math.cos(tickAngles[i3]);
			var ty2 = centerY + (radius * tickEndExtent) * Math.sin(tickAngles[i3]);
			var tick = ticks.pop();
			var tpg = new $.ig.PathGeometry();
			var tpf = new $.ig.PathFigure();
			tpf.__startPoint = { __x: tx1, __y: ty1, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			tpf.__segments.add((function () {
				var $ret = new $.ig.LineSegment(1);
				$ret.point({ __x: tx2, __y: ty2, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				return $ret;
			}()));
			tpg.figures().add(tpf);
			tick.data(tpg);
			tick.__stroke = tickBrush;
			tick.strokeThickness(tickStrokeThickness);
			this.__displayedTicks.add(tick);
		}
		ticks.deferDisactivate(false);
		var minorTicks = this.minorTicks();
		minorTicks.deferDisactivate(true);
		for (var i4 = 0; i4 < this.__displayedMinorTicks.count(); i4++) {
			minorTicks.push(this.__displayedMinorTicks.__inner[i4]);
		}
		this.__displayedMinorTicks.clear();
		for (var i5 = 0; i5 < minorTickAngles.length; i5++) {
			var tx11 = centerX + (radius * minorTickStartExtent) * Math.cos(minorTickAngles[i5]);
			var ty11 = centerY + (radius * minorTickStartExtent) * Math.sin(minorTickAngles[i5]);
			var tx21 = centerX + (radius * minorTickEndExtent) * Math.cos(minorTickAngles[i5]);
			var ty21 = centerY + (radius * minorTickEndExtent) * Math.sin(minorTickAngles[i5]);
			var tick1 = minorTicks.pop();
			var tpg1 = new $.ig.PathGeometry();
			var tpf1 = new $.ig.PathFigure();
			tpf1.__startPoint = { __x: tx11, __y: ty11, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
			tpf1.__segments.add((function () {
				var $ret = new $.ig.LineSegment(1);
				$ret.point({ __x: tx21, __y: ty21, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				return $ret;
			}()));
			tpg1.figures().add(tpf1);
			tick1.data(tpg1);
			tick1.__stroke = minorTickBrush;
			tick1.strokeThickness(minorTickStrokeThickness);
			this.__displayedMinorTicks.add(tick1);
		}
		minorTicks.deferDisactivate(false);
		var ranges = this.rangeShapes();
		ranges.deferDisactivate(true);
		for (var i6 = this.__displayedRanges.count() - 1; i6 >= 0; i6--) {
			ranges.push(this.__displayedRanges.__inner[i6]);
		}
		this.__displayedRanges.clear();
		for (var i7 = 0; i7 < scaleFrame.ranges().count(); i7++) {
			var range = scaleFrame.ranges().__inner[i7];
			var currRange = ranges.pop();
			currRange.data(range.getPathGeometry(radius, centerX, centerY));
			currRange.__fill = range._brush;
			currRange.__stroke = range._outline;
			currRange.strokeThickness(range._strokeThickness);
			this.__displayedRanges.add(currRange);
		}
		this.view().setRangeRenderOrder(this.__displayedRanges);
		ranges.deferDisactivate(false);
	}
	,
	valueCloseEnough: function (val1, val2, interval) {
		var delta = interval / 10000;
		if ($.ig.util.isNaN(delta)) {
			delta = 1E-05;
		}
		var diff = Math.abs(val1 - val2);
		if (diff < delta) {
			return true;
		}
		return false;
	}
	,
	sanitizeScaleSetting: function (value) {
		if (Number.isInfinity(value)) {
			return NaN;
		}
		return value;
	}
	,
	sanitizeScaleSettingToDefault: function (value, defaultValue) {
		if (Number.isInfinity(value) || $.ig.util.isNaN(value)) {
			return defaultValue;
		}
		return value;
	}
	,
	prepareScale: function () {
		var frame = this.nextFrame();
		frame.radiusMultiplier(this.sanitizeScaleSetting(this.radiusMultiplier()));
		if ($.ig.util.isNaN(frame.radiusMultiplier())) {
			frame.radiusMultiplier(1);
		}
		var viewport = this.viewport();
		var scaler = this.scaler();
		var interval = this.sanitizeScaleSetting(this.interval());
		var labelInterval = this.sanitizeScaleSetting(this.labelInterval());
		var minorTickCount = this.sanitizeScaleSetting(this.minorTickCount());
		if ($.ig.util.isNaN(interval)) {
			interval = (this.actualMaximumValue() - this.actualMinimumValue()) / 10;
		}
		if ($.ig.util.isNaN(labelInterval)) {
			labelInterval = interval;
		}
		if ($.ig.util.isNaN(minorTickCount)) {
			minorTickCount = 3;
		}
		var labels = new $.ig.List$1($.ig.Tuple$2.prototype.$type.specialize(Number, Number), 0);
		var checkStartAngle = scaler.startAngle();
		var checkEndAngle = scaler.endAngle();
		while (checkStartAngle >= Math.PI * 2) {
			checkStartAngle -= Math.PI * 2;
		}
		while (checkEndAngle >= Math.PI * 2) {
			checkEndAngle -= Math.PI * 2;
		}
		var hasDuplicates = Math.abs(checkEndAngle - checkStartAngle) < 0.0001;
		var omitFirst = this.duplicateLabelOmissionStrategy() == $.ig.RadialGaugeDuplicateLabelOmissionStrategy.prototype.omitFirst || this.duplicateLabelOmissionStrategy() == $.ig.RadialGaugeDuplicateLabelOmissionStrategy.prototype.omitBoth;
		var omitLast = this.duplicateLabelOmissionStrategy() == $.ig.RadialGaugeDuplicateLabelOmissionStrategy.prototype.omitLast || this.duplicateLabelOmissionStrategy() == $.ig.RadialGaugeDuplicateLabelOmissionStrategy.prototype.omitBoth;
		for (var i = this.actualMinimumValue(); i <= this.actualMaximumValue() || this.valueCloseEnough(i, this.actualMaximumValue(), labelInterval); i += labelInterval) {
			if (hasDuplicates && this.valueCloseEnough(i, this.actualMinimumValue(), labelInterval) && omitFirst) {
				continue;
			}
			if (hasDuplicates && this.valueCloseEnough(i, this.actualMaximumValue(), labelInterval) && omitLast) {
				continue;
			}
			labels.add(new $.ig.Tuple$2(Number, Number, i, scaler.scale(i)));
			if (labelInterval == 0) {
				break;
			}
		}
		var ticks = new $.ig.List$1(Number, 0);
		var minorTicks = new $.ig.List$1(Number, 0);
		for (var i1 = this.actualMinimumValue(); i1 <= this.actualMaximumValue() || this.valueCloseEnough(i1, this.actualMaximumValue(), interval); i1 += interval) {
			ticks.add(scaler.scale(i1));
			for (var j = 0; j < minorTickCount; j++) {
				var val = (interval / (minorTickCount + 1)) * (j + 1);
				if (val + i1 > this.actualMaximumValue()) {
					continue;
				}
				minorTicks.add(scaler.scale(val + i1));
			}
			if (interval == 0) {
				break;
			}
		}
		if (frame.scaleFrame().labelAngles().length != labels.count()) {
			frame.scaleFrame().labelAngles(new Array(labels.count()));
			frame.scaleFrame().labels(new Array(labels.count()));
			frame.scaleFrame().labelWidths(new Array(labels.count()));
			frame.scaleFrame().labelHeights(new Array(labels.count()));
			frame.scaleFrame().labelXOffsets(new Array(labels.count()));
			frame.scaleFrame().labelYOffsets(new Array(labels.count()));
		}
		if (frame.scaleFrame().tickAngles().length != ticks.count() || frame.scaleFrame().minorTickAngles().length != minorTicks.count()) {
			frame.scaleFrame().tickAngles(new Array(ticks.count()));
			frame.scaleFrame().minorTickAngles(new Array(minorTicks.count()));
		}
		var args = new $.ig.FormatRadialGaugeLabelEventArgs();
		args.startAngle = this.scaler().startAngle();
		args.endAngle = this.scaler().endAngle();
		args.actualMinimumValue = this.actualMinimumValue();
		args.actualMaximumValue = this.actualMaximumValue();
		var alignArgs = new $.ig.AlignRadialGaugeLabelEventArgs();
		alignArgs.startAngle = args.startAngle;
		alignArgs.endAngle = args.endAngle;
		alignArgs.actualMinimumValue = args.actualMinimumValue;
		alignArgs.actualMaximumValue = args.actualMaximumValue;
		for (var i2 = 0; i2 < labels.count(); i2++) {
			args.angle = labels.__inner[i2].item2();
			alignArgs.angle = args.angle;
			frame.scaleFrame().labelAngles()[i2] = labels.__inner[i2].item2();
			var value = labels.__inner[i2].item1();
			args.value = value;
			var labelValue = Math.round(value * 100) / 100;
			args.label = (labelValue).toString();
			if (this.formatLabel != null) {
				this.formatLabel(this, args);
			}
			alignArgs.value = args.value;
			alignArgs.label = args.label;
			var label = args.label;
			var size = this.view().getLabelSize((function () {
				var $ret = new $.ig.TextBlock();
				$ret.text(label);
				return $ret;
			}()));
			alignArgs.width = size.width();
			alignArgs.height = size.height();
			alignArgs.offsetX = -1 * (size.width() / 2);
			alignArgs.offsetY = -1 * (size.height() / 2);
			if (this.alignLabel != null) {
				this.alignLabel(this, alignArgs);
			}
			frame.scaleFrame().labelWidths()[i2] = alignArgs.width;
			frame.scaleFrame().labelHeights()[i2] = alignArgs.height;
			frame.scaleFrame().labelXOffsets()[i2] = alignArgs.offsetX;
			frame.scaleFrame().labelYOffsets()[i2] = alignArgs.offsetY;
			frame.scaleFrame().labels()[i2] = alignArgs.label;
		}
		for (var i3 = 0; i3 < ticks.count(); i3++) {
			frame.scaleFrame().tickAngles()[i3] = ticks.__inner[i3];
		}
		for (var i4 = 0; i4 < minorTicks.count(); i4++) {
			frame.scaleFrame().minorTickAngles()[i4] = minorTicks.__inner[i4];
		}
		frame.scaleFrame().scaleStartExtent(this.sanitizeScaleSettingToDefault(this.scaleStartExtent(), $.ig.XamRadialGauge.prototype.scaleStartExtentDefaultValue));
		frame.scaleFrame().scaleEndExtent(this.sanitizeScaleSettingToDefault(this.scaleEndExtent(), $.ig.XamRadialGauge.prototype.scaleEndExtentDefaultValue));
		frame.scaleFrame().labelExtent(this.sanitizeScaleSettingToDefault(this.labelExtent(), $.ig.XamRadialGauge.prototype.labelExtentDefaultValue));
		frame.scaleFrame().tickStartExtent(this.sanitizeScaleSettingToDefault(this.tickStartExtent(), $.ig.XamRadialGauge.prototype.tickStartExtentDefaultValue));
		frame.scaleFrame().tickEndExtent(this.sanitizeScaleSettingToDefault(this.tickEndExtent(), $.ig.XamRadialGauge.prototype.tickEndExtentDefaultValue));
		frame.scaleFrame().minorTickStartExtent(this.sanitizeScaleSettingToDefault(this.minorTickStartExtent(), $.ig.XamRadialGauge.prototype.minorTickStartExtentDefaultValue));
		frame.scaleFrame().minorTickEndExtent(this.sanitizeScaleSettingToDefault(this.minorTickEndExtent(), $.ig.XamRadialGauge.prototype.minorTickEndExtentDefaultValue));
		frame.scaleFrame().scaleStartAngle(scaler.startAngle());
		frame.scaleFrame().scaleEndAngle(scaler.endAngle());
		var scaleOverSweep = this.scaleOversweep();
		if ($.ig.util.isNaN((scaleOverSweep))) {
			scaleOverSweep = $.ig.XamRadialGauge.prototype.scaleOversweepDefaultValue;
		}
		var sweepDelta = scaleOverSweep * Math.PI / 180;
		if (sweepDelta < 0) {
			sweepDelta = 0;
		}
		frame.scaleFrame().scaleOversweep(sweepDelta);
		frame.scaleFrame().tickBrush(this.tickBrush());
		frame.scaleFrame().fontBrush(this.fontBrush());
		frame.scaleFrame().minorTickBrush(this.minorTickBrush());
		frame.scaleFrame().scaleBrush(this.scaleBrush());
		frame.scaleFrame().tickStrokeThickness(this.sanitizeScaleSetting(this.tickStrokeThickness()));
		frame.scaleFrame().minorTickStrokeThickness(this.sanitizeScaleSetting(this.minorTickStrokeThickness()));
		if ($.ig.util.isNaN(frame.scaleFrame().tickStrokeThickness())) {
			frame.scaleFrame().tickStrokeThickness($.ig.XamRadialGauge.prototype.tickStrokeThicknessDefaultValue);
		}
		if ($.ig.util.isNaN(frame.scaleFrame().minorTickStrokeThickness())) {
			frame.scaleFrame().minorTickStrokeThickness($.ig.XamRadialGauge.prototype.minorTickStrokeThicknessDefaultValue);
		}
		frame.scaleFrame().ranges().clear();
		for (var i5 = 0; i5 < this.ranges().count(); i5++) {
			var range = this.ranges().__inner[i5];
			var startValue = this.sanitizeScaleSetting(Math.min(range.startValue(), range.endValue()));
			var endValue = this.sanitizeScaleSetting(Math.max(range.startValue(), range.endValue()));
			if ($.ig.util.isNaN(startValue) || $.ig.util.isNaN(endValue)) {
				continue;
			}
			var startAngle = scaler.scale(startValue);
			var endAngle = scaler.scale(endValue);
			var res = $.ig.RadialGaugeScaler.prototype.normalizeAngles(startAngle, endAngle, this.scaleSweepDirection());
			startAngle = res.item1();
			endAngle = res.item2();
			var f = new $.ig.RadialGaugeRangeFrame();
			f._startAngle = startAngle;
			f._endAngle = endAngle;
			f._brush = range.brush() != null ? range.brush() : this.getBrushByIndex(i5, this.rangeBrushes());
			f._outline = range.outline() != null ? range.outline() : this.getBrushByIndex(i5, this.rangeOutlines());
			f._strokeThickness = this.sanitizeScaleSetting(range.strokeThickness());
			f._startInnerExtent = this.sanitizeScaleSetting(range.innerStartExtent());
			if ($.ig.util.isNaN(f._startInnerExtent)) {
				f._startInnerExtent = this.scaleStartExtent();
			}
			f._endInnerExtent = this.sanitizeScaleSetting(range.innerEndExtent());
			if ($.ig.util.isNaN(f._endInnerExtent)) {
				f._endInnerExtent = this.scaleStartExtent();
			}
			f._startOuterExtent = this.sanitizeScaleSetting(range.outerStartExtent());
			if ($.ig.util.isNaN(f._startOuterExtent)) {
				f._startOuterExtent = this.scaleEndExtent();
			}
			f._endOuterExtent = this.sanitizeScaleSetting(range.outerEndExtent());
			if ($.ig.util.isNaN(f._endOuterExtent)) {
				f._endOuterExtent = this.scaleEndExtent();
			}
			if (this.scaleSweepDirection() == $.ig.SweepDirection.prototype.counterclockwise) {
				var swap = f._startInnerExtent;
				f._startInnerExtent = f._endInnerExtent;
				f._endInnerExtent = swap;
				swap = f._startOuterExtent;
				f._startOuterExtent = f._endOuterExtent;
				f._endOuterExtent = swap;
			}
			frame.scaleFrame().ranges().add(f);
		}
	}
	,
	prepareBacking: function () {
		var frame = this.nextFrame();
		frame.radiusMultiplier(this.radiusMultiplier());
		var f = frame.backingFrame();
		var brush = this.backingBrush();
		var outline = this.backingOutline();
		var thickness = this.backingStrokeThickness();
		var shape = this.backingShape();
		var backingOuterExtent = this.backingOuterExtent();
		var backingInnerExtent = this.backingInnerExtent();
		if (Number.isInfinity(backingInnerExtent) || $.ig.util.isNaN(backingInnerExtent)) {
			backingInnerExtent = $.ig.XamRadialGauge.prototype.backingInnerExtentDefaultValue;
		}
		if (Number.isInfinity(backingOuterExtent) || $.ig.util.isNaN(backingOuterExtent)) {
			backingOuterExtent = $.ig.XamRadialGauge.prototype.backingOuterExtentDefaultValue;
		}
		var backingCornerRadius = this.backingCornerRadius();
		if (Number.isInfinity(backingCornerRadius) || $.ig.util.isNaN(backingCornerRadius)) {
			backingCornerRadius = $.ig.XamRadialGauge.prototype.backingCornerRadiusDefaultValue;
		}
		if (backingInnerExtent > backingOuterExtent) {
			var swap = backingOuterExtent;
			backingOuterExtent = backingInnerExtent;
			backingInnerExtent = swap;
		}
		var scaler = this.scaler();
		var scaleStartAngle = scaler.startAngle();
		var scaleEndAngle = scaler.endAngle();
		f.brush(brush);
		f.outline(outline);
		f.strokeThickness(thickness);
		if (shape == $.ig.RadialGaugeBackingShape.prototype.circular) {
			f.cornerRadius(0);
			f.startAngle(0);
			f.endAngle(0);
			f.innerExtent(0);
			f.outerExtent(this.backingOuterExtent());
		} else {
			f.cornerRadius(backingCornerRadius);
			var sweepDelta = this.backingOversweep() * Math.PI / 180;
			if (sweepDelta < 0) {
				sweepDelta = 0;
			}
			if ((Math.PI * 2) - (scaleEndAngle - scaleStartAngle) < sweepDelta) {
				f.oversweep(0);
				f.cornerRadius(0);
				f.startAngle(0);
				f.endAngle(0);
				f.innerExtent(0);
				f.outerExtent(backingOuterExtent);
			} else {
				f.oversweep(sweepDelta);
				f.startAngle(scaleStartAngle);
				f.endAngle(scaleEndAngle);
				f.cornerRadius(backingCornerRadius);
				f.innerExtent(backingInnerExtent);
				f.outerExtent(backingOuterExtent);
			}
		}
	}
	,
	animating: function () {
		return this.animator().animationActive();
	}
	,
	__transitionProgress: 0,
	transitionProgress: function (value) {
		if (arguments.length === 1) {
			var oldValue = this.__transitionProgress;
			this.__transitionProgress = value;
			this.onPropertyChanged($.ig.XamRadialGauge.prototype.transitionProgressPropertyName, oldValue, this.__transitionProgress);
			return value;
		} else {
			return this.__transitionProgress;
		}
	}
	,
	_ticks: null,
	ticks: function (value) {
		if (arguments.length === 1) {
			this._ticks = value;
			return value;
		} else {
			return this._ticks;
		}
	}
	,
	_minorTicks: null,
	minorTicks: function (value) {
		if (arguments.length === 1) {
			this._minorTicks = value;
			return value;
		} else {
			return this._minorTicks;
		}
	}
	,
	_labels: null,
	labels: function (value) {
		if (arguments.length === 1) {
			this._labels = value;
			return value;
		} else {
			return this._labels;
		}
	}
	,
	_rangeShapes: null,
	rangeShapes: function (value) {
		if (arguments.length === 1) {
			this._rangeShapes = value;
			return value;
		} else {
			return this._rangeShapes;
		}
	}
	,
	dragNeedle: function (point, isFinger) {
		var value = this.getValueForPoint(point);
		if (this.isNeedleDraggingConstrained()) {
			var overMax = this.unscaleValue(this.scaler().endAngle() - Math.PI * 2);
			switch (this.scaleSweepDirection()) {
				case $.ig.SweepDirection.prototype.clockwise:
					if (value < this.actualMinimumValue()) {
						value = value < overMax * 0.5 ? this.actualMaximumValue() : this.actualMinimumValue();
					}
					break;
				case $.ig.SweepDirection.prototype.counterclockwise:
					if (value > this.actualMaximumValue()) {
						value = value < (overMax + this.actualMaximumValue()) * 0.5 ? this.actualMaximumValue() : this.actualMinimumValue();
					}
					break;
			}
		}
		if (value != this.value()) {
			this.value(value);
		}
	}
	,
	destroy: function () {
		this.provideContainer(null);
	}
	,
	provideContainer: function (container) {
		this.view().onContainerProvided(container);
	}
	,
	onMouseMove: function (point, isFinger) {
		if (this.__isDragging) {
			this.dragNeedle(point, isFinger);
		}
	}
	,
	onMouseDown: function (point, isFinger) {
		if (this.isNeedleDraggingEnabled() && this.needleContainsPoint1(point, isFinger) && !this.__isDragging) {
			this.__isDragging = true;
		}
	}
	,
	onMouseUp: function (point) {
		if (this.__isDragging) {
			this.__isDragging = false;
		}
	}
	,
	containerResized: function () {
		this.view().onContainerResized();
	}
	,
	flush: function () {
		this.view().flush();
	}
	,
	exportVisualData: function () {
		var gaugeVisualData = new $.ig.GaugeVisualData();
		gaugeVisualData.viewport(new $.ig.Rect(0, this.viewport().left(), this.viewport().top(), this.viewport().width(), this.viewport().height()));
		gaugeVisualData.scaleLabels(new $.ig.ScaleLabelVisualDataList());
		gaugeVisualData.scaleTickmarks(new $.ig.ScaleTickmarkVisualDataList());
		gaugeVisualData.ranges(new $.ig.RangeVisualDataList());
		gaugeVisualData.needle(new $.ig.NeedleVisualData());
		this.view().exportViewShapes(gaugeVisualData);
		return gaugeVisualData;
	}
	,
	getFontInfo: function () {
		return $.ig.FontUtil.prototype.toFontInfo(this.font());
	}
	,
	$type: new $.ig.Type('XamRadialGauge', $.ig.Control.prototype.$type)
}, true);

$.ig.util.defType('RadialGaugeScaler', 'Object', {
	__scaleStartAngle: 0,
	__scaleEndAngle: 0,
	__minimumValue: 0,
	__maximumValue: 0,
	__scaleSweepDirection: 0,
	__startAngle: 0,
	__endAngle: 0,
	startAngle: function () {
		return this.__startAngle;
	}
	,
	endAngle: function () {
		return this.__endAngle;
	}
	,
	init: function (minimumValue, maximumValue, scaleStartAngle, scaleEndAngle, scaleSweepDirection) {
		$.ig.Object.prototype.init.call(this);
		this.__scaleStartAngle = scaleStartAngle;
		this.__scaleEndAngle = scaleEndAngle;
		if ($.ig.util.isNaN(this.__scaleStartAngle) || Number.isInfinity(this.__scaleStartAngle)) {
			this.__scaleStartAngle = 135;
		}
		if ($.ig.util.isNaN(this.__scaleEndAngle) || Number.isInfinity(this.__scaleEndAngle)) {
			this.__scaleEndAngle = 45;
		}
		this.__scaleSweepDirection = scaleSweepDirection;
		this.__minimumValue = minimumValue;
		this.__maximumValue = maximumValue;
		var startAngle = $.ig.MathUtil.prototype.radians(this.__scaleStartAngle);
		var endAngle = $.ig.MathUtil.prototype.radians(this.__scaleEndAngle);
		var res = $.ig.RadialGaugeScaler.prototype.normalizeAngles(startAngle, endAngle, this.__scaleSweepDirection);
		this.__startAngle = res.item1();
		this.__endAngle = res.item2();
	},
	normalizeAngles: function (startAngle, endAngle, sweepDirection) {
		if (sweepDirection == $.ig.SweepDirection.prototype.clockwise && endAngle < startAngle) {
			endAngle += Math.PI * 2;
		}
		if (sweepDirection == $.ig.SweepDirection.prototype.counterclockwise && startAngle < endAngle) {
			startAngle += Math.PI * 2;
		}
		if (sweepDirection == $.ig.SweepDirection.prototype.counterclockwise) {
			var swap = startAngle;
			startAngle = endAngle;
			endAngle = swap;
		}
		return new $.ig.Tuple$2(Number, Number, startAngle, endAngle);
	}
	,
	scale: function (value) {
		var scale = NaN;
		if (this.__minimumValue == this.__maximumValue) {
			scale = 0;
		} else {
			scale = (value - this.__minimumValue) / (this.__maximumValue - this.__minimumValue);
		}
		if (this.__scaleSweepDirection == $.ig.SweepDirection.prototype.counterclockwise) {
			scale = 1 - scale;
		}
		var scaledValue = this.__startAngle + (this.__endAngle - this.__startAngle) * scale;
		return scaledValue;
	}
	,
	unscale: function (angle) {
		var scale = (angle - this.__startAngle) / (this.__endAngle - this.__startAngle);
		if (this.__scaleSweepDirection == $.ig.SweepDirection.prototype.counterclockwise) {
			scale = 1 - scale;
		}
		var unscaled = this.__minimumValue + (this.__maximumValue - this.__minimumValue) * scale;
		return unscaled;
	}
	,
	$type: new $.ig.Type('RadialGaugeScaler', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('FormatRadialGaugeLabelEventArgs', 'EventArgs', {
	init: function () {
		$.ig.EventArgs.prototype.init.call(this);
	},
	actualMinimumValue: 0,
	actualMaximumValue: 0,
	startAngle: 0,
	endAngle: 0,
	angle: 0,
	value: 0,
	label: null,
	$type: new $.ig.Type('FormatRadialGaugeLabelEventArgs', $.ig.EventArgs.prototype.$type)
}, true);

$.ig.util.defType('AlignRadialGaugeLabelEventArgs', 'FormatRadialGaugeLabelEventArgs', {
	init: function () {
		$.ig.FormatRadialGaugeLabelEventArgs.prototype.init.call(this);
	},
	width: 0,
	height: 0,
	offsetX: 0,
	offsetY: 0,
	$type: new $.ig.Type('AlignRadialGaugeLabelEventArgs', $.ig.FormatRadialGaugeLabelEventArgs.prototype.$type)
}, true);

$.ig.RadialGaugePivotShape.prototype.none = 0;
$.ig.RadialGaugePivotShape.prototype.circle = 1;
$.ig.RadialGaugePivotShape.prototype.circleWithHole = 2;
$.ig.RadialGaugePivotShape.prototype.circleOverlay = 3;
$.ig.RadialGaugePivotShape.prototype.circleOverlayWithHole = 4;
$.ig.RadialGaugePivotShape.prototype.circleUnderlay = 5;
$.ig.RadialGaugePivotShape.prototype.circleUnderlayWithHole = 6;

$.ig.RadialGaugeNeedleShape.prototype.none = 0;
$.ig.RadialGaugeNeedleShape.prototype.rectangle = 1;
$.ig.RadialGaugeNeedleShape.prototype.triangle = 2;
$.ig.RadialGaugeNeedleShape.prototype.needle = 3;
$.ig.RadialGaugeNeedleShape.prototype.trapezoid = 4;
$.ig.RadialGaugeNeedleShape.prototype.rectangleWithBulb = 5;
$.ig.RadialGaugeNeedleShape.prototype.triangleWithBulb = 6;
$.ig.RadialGaugeNeedleShape.prototype.needleWithBulb = 7;
$.ig.RadialGaugeNeedleShape.prototype.trapezoidWithBulb = 8;

$.ig.RadialGaugeScaleOversweepShape.prototype.auto = 0;
$.ig.RadialGaugeScaleOversweepShape.prototype.circular = 1;
$.ig.RadialGaugeScaleOversweepShape.prototype.fitted = 2;

$.ig.RadialGaugeBackingShape.prototype.circular = 0;
$.ig.RadialGaugeBackingShape.prototype.fitted = 1;

$.ig.RadialGaugeDuplicateLabelOmissionStrategy.prototype.omitLast = 0;
$.ig.RadialGaugeDuplicateLabelOmissionStrategy.prototype.omitFirst = 1;
$.ig.RadialGaugeDuplicateLabelOmissionStrategy.prototype.omitNeither = 2;
$.ig.RadialGaugeDuplicateLabelOmissionStrategy.prototype.omitBoth = 3;

$.ig.XamRadialGaugeView.prototype._tEXT_MARGIN = 0;

$.ig.XamRadialGaugeRange.prototype.namePropertyName = "Name";
$.ig.XamRadialGaugeRange.prototype.brushPropertyName = "Brush";
$.ig.XamRadialGaugeRange.prototype.outlinePropertyName = "Outline";
$.ig.XamRadialGaugeRange.prototype.startValuePropertyName = "StartValue";
$.ig.XamRadialGaugeRange.prototype.endValuePropertyName = "EndValue";
$.ig.XamRadialGaugeRange.prototype.innerStartExtentPropertyName = "InnerStartExtent";
$.ig.XamRadialGaugeRange.prototype.innerEndExtentPropertyName = "InnerEndExtent";
$.ig.XamRadialGaugeRange.prototype.outerStartExtentPropertyName = "OuterStartExtent";
$.ig.XamRadialGaugeRange.prototype.outerEndExtentPropertyName = "OuterEndExtent";
$.ig.XamRadialGaugeRange.prototype.strokeThicknessPropertyName = "StrokeThickness";
$.ig.XamRadialGaugeRange.prototype.nameProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGaugeRange.prototype.namePropertyName, String, $.ig.XamRadialGaugeRange.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGaugeRange.prototype.namePropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGaugeRange.prototype.brushProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGaugeRange.prototype.brushPropertyName, $.ig.Brush.prototype.$type, $.ig.XamRadialGaugeRange.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGaugeRange.prototype.brushPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGaugeRange.prototype.outlineProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGaugeRange.prototype.outlinePropertyName, $.ig.Brush.prototype.$type, $.ig.XamRadialGaugeRange.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGaugeRange.prototype.outlinePropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGaugeRange.prototype.startValueProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGaugeRange.prototype.startValuePropertyName, Number, $.ig.XamRadialGaugeRange.prototype.$type, new $.ig.PropertyMetadata(2, NaN, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGaugeRange.prototype.startValuePropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGaugeRange.prototype.endValueProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGaugeRange.prototype.endValuePropertyName, Number, $.ig.XamRadialGaugeRange.prototype.$type, new $.ig.PropertyMetadata(2, NaN, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGaugeRange.prototype.endValuePropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGaugeRange.prototype.innerStartExtentProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGaugeRange.prototype.innerStartExtentPropertyName, Number, $.ig.XamRadialGaugeRange.prototype.$type, new $.ig.PropertyMetadata(2, NaN, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGaugeRange.prototype.innerStartExtentPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGaugeRange.prototype.innerEndExtentProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGaugeRange.prototype.innerEndExtentPropertyName, Number, $.ig.XamRadialGaugeRange.prototype.$type, new $.ig.PropertyMetadata(2, NaN, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGaugeRange.prototype.innerEndExtentPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGaugeRange.prototype.outerStartExtentProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGaugeRange.prototype.outerStartExtentPropertyName, Number, $.ig.XamRadialGaugeRange.prototype.$type, new $.ig.PropertyMetadata(2, NaN, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGaugeRange.prototype.outerStartExtentPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGaugeRange.prototype.outerEndExtentProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGaugeRange.prototype.outerEndExtentPropertyName, Number, $.ig.XamRadialGaugeRange.prototype.$type, new $.ig.PropertyMetadata(2, NaN, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGaugeRange.prototype.outerEndExtentPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGaugeRange.prototype.strokeThicknessProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGaugeRange.prototype.strokeThicknessPropertyName, Number, $.ig.XamRadialGaugeRange.prototype.$type, new $.ig.PropertyMetadata(2, 1, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGaugeRange.prototype.strokeThicknessPropertyName, e.oldValue(), e.newValue());
}));

$.ig.XamRadialGauge.prototype.rangeBrushesPropertyName = "RangeBrushes";
$.ig.XamRadialGauge.prototype.rangeOutlinesPropertyName = "RangeOutlines";
$.ig.XamRadialGauge.prototype.minimumValueDefaultValue = 0;
$.ig.XamRadialGauge.prototype.minimumValuePropertyName = "MinimumValue";
$.ig.XamRadialGauge.prototype.maximumValueDefaultValue = 100;
$.ig.XamRadialGauge.prototype.maximumValuePropertyName = "MaximumValue";
$.ig.XamRadialGauge.prototype.intervalPropertyName = "Interval";
$.ig.XamRadialGauge.prototype.centerXPropertyName = "CenterX";
$.ig.XamRadialGauge.prototype.centerYPropertyName = "CenterY";
$.ig.XamRadialGauge.prototype.valuePropertyName = "Value";
$.ig.XamRadialGauge.prototype.scaleStartAnglePropertyName = "ScaleStartAngle";
$.ig.XamRadialGauge.prototype.scaleEndAnglePropertyName = "ScaleEndAngle";
$.ig.XamRadialGauge.prototype.scaleSweepDirectionPropertyName = "ScaleSweepDirection";
$.ig.XamRadialGauge.prototype.transitionDurationPropertyName = "TransitionDuration";
$.ig.XamRadialGauge.prototype.transitionEasingFunctionPropertyName = "TransitionEasingFunction";
$.ig.XamRadialGauge.prototype.needleBrushPropertyName = "NeedleBrush";
$.ig.XamRadialGauge.prototype.needleOutlinePropertyName = "NeedleOutline";
$.ig.XamRadialGauge.prototype.needleStartExtentPropertyName = "NeedleStartExtent";
$.ig.XamRadialGauge.prototype.needleEndExtentPropertyName = "NeedleEndExtent";
$.ig.XamRadialGauge.prototype.needleShapePropertyName = "NeedleShape";
$.ig.XamRadialGauge.prototype.needleStartWidthRatioPropertyName = "NeedleStartWidth";
$.ig.XamRadialGauge.prototype.needleEndWidthRatioPropertyName = "NeedleEndWidthRatio";
$.ig.XamRadialGauge.prototype.needleBaseFeatureWidthRatioPropertyName = "NeedleBaseFeatureWidthRatio";
$.ig.XamRadialGauge.prototype.needleBaseFeatureExtentPropertyName = "NeedleBaseFeatureExtent";
$.ig.XamRadialGauge.prototype.needlePointFeatureWidthRatioPropertyName = "NeedlePointFeatureWidthRatio";
$.ig.XamRadialGauge.prototype.needlePointFeatureExtentPropertyName = "NeedlePointFeatureExtent";
$.ig.XamRadialGauge.prototype.needlePivotWidthRatioPropertyName = "NeedlePivotWidthRatio";
$.ig.XamRadialGauge.prototype.needlePivotInnerWidthRatioPropertyName = "NeedlePivotInnerWidthRatio";
$.ig.XamRadialGauge.prototype.needlePivotShapePropertyName = "NeedlePivotShape";
$.ig.XamRadialGauge.prototype.needlePivotBrushPropertyName = "NeedlePivotBrush";
$.ig.XamRadialGauge.prototype.needlePivotOutlinePropertyName = "NeedlePivotOutline";
$.ig.XamRadialGauge.prototype.needleStrokeThicknessPropertyName = "NeedleStrokeThickness";
$.ig.XamRadialGauge.prototype.needlePivotStrokeThicknessPropertyName = "NeedlePivotStrokeThickness";
$.ig.XamRadialGauge.prototype.scaleStartExtentDefaultValue = 0.5;
$.ig.XamRadialGauge.prototype.scaleStartExtentPropertyName = "ScaleStartExtent";
$.ig.XamRadialGauge.prototype.scaleEndExtentDefaultValue = 0.57;
$.ig.XamRadialGauge.prototype.scaleEndExtentPropertyName = "ScaleEndExtent";
$.ig.XamRadialGauge.prototype.labelExtentDefaultValue = 0.65;
$.ig.XamRadialGauge.prototype.labelExtentPropertyName = "LabelExtent";
$.ig.XamRadialGauge.prototype.labelIntervalPropertyName = "LabelInterval";
$.ig.XamRadialGauge.prototype.tickStartExtentDefaultValue = 0.5;
$.ig.XamRadialGauge.prototype.tickStartExtentPropertyName = "TickStartExtent";
$.ig.XamRadialGauge.prototype.tickEndExtentDefaultValue = 0.57;
$.ig.XamRadialGauge.prototype.tickEndExtentPropertyName = "TickEndExtent";
$.ig.XamRadialGauge.prototype.tickStrokeThicknessDefaultValue = 3;
$.ig.XamRadialGauge.prototype.tickStrokeThicknessPropertyName = "TickStrokeThickness";
$.ig.XamRadialGauge.prototype.tickBrushPropertyName = "TickBrush";
$.ig.XamRadialGauge.prototype.fontBrushPropertyName = "FontBrush";
$.ig.XamRadialGauge.prototype.minorTickStartExtentDefaultValue = 0.54;
$.ig.XamRadialGauge.prototype.minorTickStartExtentPropertyName = "MinorTickStartExtent";
$.ig.XamRadialGauge.prototype.minorTickEndExtentDefaultValue = 0.57;
$.ig.XamRadialGauge.prototype.minorTickEndExtentPropertyName = "MinorTickEndExtent";
$.ig.XamRadialGauge.prototype.minorTickStrokeThicknessDefaultValue = 2;
$.ig.XamRadialGauge.prototype.minorTickStrokeThicknessPropertyName = "MinorTickStrokeThickness";
$.ig.XamRadialGauge.prototype.minorTickBrushPropertyName = "MinorTickBrush";
$.ig.XamRadialGauge.prototype.minorTickCountPropertyName = "MinorTickCount";
$.ig.XamRadialGauge.prototype.scaleBrushPropertyName = "ScaleBrush";
$.ig.XamRadialGauge.prototype.backingBrushPropertyName = "BackingBrush";
$.ig.XamRadialGauge.prototype.backingOutlinePropertyName = "BackingOutline";
$.ig.XamRadialGauge.prototype.backingStrokeThicknessPropertyName = "BackingStrokeThickness";
$.ig.XamRadialGauge.prototype.backingOuterExtentDefaultValue = 0.82;
$.ig.XamRadialGauge.prototype.backingOuterExtentPropertyName = "BackingOuterExtent";
$.ig.XamRadialGauge.prototype.backingOversweepPropertyName = "BackingOversweep";
$.ig.XamRadialGauge.prototype.scaleOversweepDefaultValue = 2.8;
$.ig.XamRadialGauge.prototype.scaleOversweepPropertyName = "ScaleOversweep";
$.ig.XamRadialGauge.prototype.scaleOversweepShapePropertyName = "ScaleOversweepShape";
$.ig.XamRadialGauge.prototype.backingCornerRadiusDefaultValue = 4;
$.ig.XamRadialGauge.prototype.backingCornerRadiusPropertyName = "BackingCornerRadius";
$.ig.XamRadialGauge.prototype.backingInnerExtentDefaultValue = 0.12;
$.ig.XamRadialGauge.prototype.backingInnerExtentPropertyName = "BackingInnerExtent";
$.ig.XamRadialGauge.prototype.backingShapePropertyName = "BackingShape";
$.ig.XamRadialGauge.prototype.rangesPropertyName = "Ranges";
$.ig.XamRadialGauge.prototype.radiusMultiplierPropertyName = "RadiusMultiplier";
$.ig.XamRadialGauge.prototype.duplicateLabelOmissionStrategyPropertyName = "DuplicateLabelOmissionStrategy";
$.ig.XamRadialGauge.prototype.isNeedleDraggingEnabledPropertyName = "IsNeedleDraggingEnabled";
$.ig.XamRadialGauge.prototype.isNeedleDraggingConstrainedPropertyName = "IsNeedleDraggingConstrained";
$.ig.XamRadialGauge.prototype.fontPropertyName = "Font";
$.ig.XamRadialGauge.prototype.transitionProgressPropertyName = "TransitionProgress";
$.ig.XamRadialGauge.prototype.rangeBrushesProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.rangeBrushesPropertyName, $.ig.BrushCollection.prototype.$type, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.rangeBrushesPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.rangeOutlinesProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.rangeOutlinesPropertyName, $.ig.BrushCollection.prototype.$type, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.rangeOutlinesPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.minimumValueProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.minimumValuePropertyName, Number, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.XamRadialGauge.prototype.minimumValueDefaultValue, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.minimumValuePropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.maximumValueProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.maximumValuePropertyName, Number, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.XamRadialGauge.prototype.maximumValueDefaultValue, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.maximumValuePropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.intervalProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.intervalPropertyName, Number, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, NaN, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.intervalPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.centerXProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.centerXPropertyName, Number, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, 0.5, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.centerXPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.centerYProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.centerYPropertyName, Number, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, 0.5, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.centerYPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.valueProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.valuePropertyName, Number, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, 0, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.valuePropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.scaleStartAngleProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.scaleStartAnglePropertyName, Number, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, 135, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.scaleStartAnglePropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.scaleEndAngleProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.scaleEndAnglePropertyName, Number, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, 45, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.scaleEndAnglePropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.scaleSweepDirectionProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.scaleSweepDirectionPropertyName, $.ig.SweepDirection.prototype.$type, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.SweepDirection.prototype.getBox($.ig.SweepDirection.prototype.clockwise), function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.scaleSweepDirectionPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.transitionDurationProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.transitionDurationPropertyName, $.ig.Number.prototype.$type, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, 0, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.transitionDurationPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.transitionEasingFunctionProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.transitionEasingFunctionPropertyName, $.ig.EasingFunctionHandler.prototype.$type, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	($.ig.util.cast($.ig.XamRadialGauge.prototype.$type, sender)).onPropertyChanged($.ig.XamRadialGauge.prototype.transitionEasingFunctionPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.needleBrushProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.needleBrushPropertyName, $.ig.Brush.prototype.$type, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.needleBrushPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.needleOutlineProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.needleOutlinePropertyName, $.ig.Brush.prototype.$type, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.needleOutlinePropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.needleStartExtentProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.needleStartExtentPropertyName, Number, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, NaN, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.needleStartExtentPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.needleEndExtentProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.needleEndExtentPropertyName, Number, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, NaN, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.needleEndExtentPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.needleShapeProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.needleShapePropertyName, $.ig.RadialGaugeNeedleShape.prototype.$type, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.RadialGaugeNeedleShape.prototype.getBox($.ig.RadialGaugeNeedleShape.prototype.needle), function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.needleShapePropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.needleStartWidthRatioProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.needleStartWidthRatioPropertyName, Number, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, NaN, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.needleStartWidthRatioPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.needleEndWidthRatioProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.needleEndWidthRatioPropertyName, Number, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, NaN, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.needleEndWidthRatioPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.needleBaseFeatureWidthRatioProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.needleBaseFeatureWidthRatioPropertyName, Number, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, NaN, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.needleBaseFeatureWidthRatioPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.needleBaseFeatureExtentProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.needleBaseFeatureExtentPropertyName, Number, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, NaN, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.needleBaseFeatureExtentPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.needlePointFeatureWidthRatioProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.needlePointFeatureWidthRatioPropertyName, Number, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, NaN, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.needlePointFeatureWidthRatioPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.needlePointFeatureExtentProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.needlePointFeatureExtentPropertyName, Number, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, NaN, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.needlePointFeatureExtentPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.needlePivotWidthRatioProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.needlePivotWidthRatioPropertyName, Number, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, NaN, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.needlePivotWidthRatioPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.needlePivotInnerWidthRatioProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.needlePivotInnerWidthRatioPropertyName, Number, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, NaN, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.needlePivotInnerWidthRatioPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.needlePivotShapeProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.needlePivotShapePropertyName, $.ig.RadialGaugePivotShape.prototype.$type, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.RadialGaugePivotShape.prototype.getBox($.ig.RadialGaugePivotShape.prototype.circleOverlay), function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.needlePivotShapePropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.needlePivotBrushProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.needlePivotBrushPropertyName, $.ig.Brush.prototype.$type, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.needlePivotBrushPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.needlePivotOutlineProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.needlePivotOutlinePropertyName, $.ig.Brush.prototype.$type, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.needlePivotOutlinePropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.needleStrokeThicknessProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.needleStrokeThicknessPropertyName, Number, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, 1, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.needleStrokeThicknessPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.needlePivotStrokeThicknessProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.needlePivotStrokeThicknessPropertyName, Number, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, 1, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.needlePivotStrokeThicknessPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.scaleStartExtentProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.scaleStartExtentPropertyName, Number, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.XamRadialGauge.prototype.scaleStartExtentDefaultValue, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.scaleStartExtentPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.scaleEndExtentProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.scaleEndExtentPropertyName, Number, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.XamRadialGauge.prototype.scaleEndExtentDefaultValue, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.scaleEndExtentPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.labelExtentProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.labelExtentPropertyName, Number, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.XamRadialGauge.prototype.labelExtentDefaultValue, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.labelExtentPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.labelIntervalProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.labelIntervalPropertyName, Number, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, NaN, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.labelIntervalPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.tickStartExtentProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.tickStartExtentPropertyName, Number, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.XamRadialGauge.prototype.tickStartExtentDefaultValue, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.tickStartExtentPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.tickEndExtentProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.tickEndExtentPropertyName, Number, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.XamRadialGauge.prototype.tickEndExtentDefaultValue, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.tickEndExtentPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.tickStrokeThicknessProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.tickStrokeThicknessPropertyName, Number, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.XamRadialGauge.prototype.tickStrokeThicknessDefaultValue, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.tickStrokeThicknessPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.tickBrushProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.tickBrushPropertyName, $.ig.Brush.prototype.$type, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.tickBrushPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.fontBrushProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.fontBrushPropertyName, $.ig.Brush.prototype.$type, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.fontBrushPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.minorTickStartExtentProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.minorTickStartExtentPropertyName, Number, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.XamRadialGauge.prototype.minorTickStartExtentDefaultValue, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.minorTickStartExtentPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.minorTickEndExtentProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.minorTickEndExtentPropertyName, Number, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.XamRadialGauge.prototype.minorTickEndExtentDefaultValue, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.minorTickEndExtentPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.minorTickStrokeThicknessProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.minorTickStrokeThicknessPropertyName, Number, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.XamRadialGauge.prototype.minorTickStrokeThicknessDefaultValue, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.minorTickStrokeThicknessPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.minorTickBrushProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.minorTickBrushPropertyName, $.ig.Brush.prototype.$type, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.minorTickBrushPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.minorTickCountProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.minorTickCountPropertyName, Number, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, 3, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.minorTickCountPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.scaleBrushProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.scaleBrushPropertyName, $.ig.Brush.prototype.$type, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.scaleBrushPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.backingBrushProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.backingBrushPropertyName, $.ig.Brush.prototype.$type, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.backingBrushPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.backingOutlineProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.backingOutlinePropertyName, $.ig.Brush.prototype.$type, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.backingOutlinePropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.backingStrokeThicknessProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.backingStrokeThicknessPropertyName, Number, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, 12, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.backingStrokeThicknessPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.backingOuterExtentProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.backingOuterExtentPropertyName, Number, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.XamRadialGauge.prototype.backingOuterExtentDefaultValue, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.backingOuterExtentPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.backingOversweepProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.backingOversweepPropertyName, Number, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, 3, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.backingOversweepPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.scaleOversweepProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.scaleOversweepPropertyName, Number, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.XamRadialGauge.prototype.scaleOversweepDefaultValue, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.scaleOversweepPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.scaleOversweepShapeProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.scaleOversweepShapePropertyName, $.ig.RadialGaugeScaleOversweepShape.prototype.$type, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.RadialGaugeScaleOversweepShape.prototype.getBox($.ig.RadialGaugeScaleOversweepShape.prototype.auto), function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.scaleOversweepShapePropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.backingCornerRadiusProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.backingCornerRadiusPropertyName, Number, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.XamRadialGauge.prototype.backingCornerRadiusDefaultValue, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.backingCornerRadiusPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.backingInnerExtentProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.backingInnerExtentPropertyName, Number, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.XamRadialGauge.prototype.backingInnerExtentDefaultValue, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.backingInnerExtentPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.backingShapeProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.backingShapePropertyName, $.ig.RadialGaugeBackingShape.prototype.$type, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.RadialGaugeBackingShape.prototype.getBox($.ig.RadialGaugeBackingShape.prototype.circular), function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.backingShapePropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.rangesProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.rangesPropertyName, $.ig.RadialGaugeRangeCollection.prototype.$type, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.rangesPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.radiusMultiplierProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.radiusMultiplierPropertyName, Number, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, 1, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.radiusMultiplierPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.duplicateLabelOmissionStrategyProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.duplicateLabelOmissionStrategyPropertyName, $.ig.RadialGaugeDuplicateLabelOmissionStrategy.prototype.$type, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.RadialGaugeDuplicateLabelOmissionStrategy.prototype.getBox($.ig.RadialGaugeDuplicateLabelOmissionStrategy.prototype.omitLast), function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.duplicateLabelOmissionStrategyPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.isNeedleDraggingEnabledProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.isNeedleDraggingEnabledPropertyName, $.ig.Boolean.prototype.$type, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, false, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.isNeedleDraggingEnabledPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.isNeedleDraggingConstrainedProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.isNeedleDraggingConstrainedPropertyName, $.ig.Boolean.prototype.$type, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, true, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.isNeedleDraggingConstrainedPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamRadialGauge.prototype.fontProperty = $.ig.DependencyProperty.prototype.register($.ig.XamRadialGauge.prototype.fontPropertyName, String, $.ig.XamRadialGauge.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	(o).onPropertyChanged($.ig.XamRadialGauge.prototype.fontPropertyName, e.oldValue(), e.newValue());
}));

} (jQuery));


