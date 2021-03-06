/* Generated By:JavaCC: Do not edit this line. ExpressionParser.java */
package nts.uk.shr.com.fomula;

import java.math.RoundingMode;
import java.math.MathContext;
import java.math.BigDecimal;
import java.util.Stack;
import java.io.StringReader;

public class ExpressionParser implements ExpressionParserConstants {

	private Stack<BigDecimal> argStack = new Stack<BigDecimal>();
	private String expression;
	private int divineScale = 10;
	private RoundingMode divineRoundMode = RoundingMode.HALF_UP;

	public ExpressionParser() {
		this("");
	}

	public ExpressionParser(String in) {
		this(new StringReader(in));
		this.expression = in;
	}

	public BigDecimal parse() throws ParseException {
		if (this.expression != null && !this.expression.trim().isEmpty()) {
			this.ReInit(new StringReader(this.expression));
			this.expressionParse();
			return argStack.pop();
		}

		return BigDecimal.ZERO;
	}

	public BigDecimal parse(String in) throws ParseException {
		this.expression = in;
		return this.parse();
	}

	public BigDecimal parse(String in, int divineScale, RoundingMode divineRoundMode) throws ParseException {
		this.divineRoundMode = divineRoundMode;
		this.divineScale = divineScale;
		this.expression = in;
		return this.parse();
	}

	final private void expressionParse() throws ParseException {
		switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
		case LBRACKET:
		case PLUS:
		case MINUS:
		case ROUND:
		case FLOOR:
		case CEIL:
		case MAX:
		case MIN:
		case CONDITION:
		case CONSTANT:
			basicOperate();
			jj_consume_token(0);
			break;
		case 0:
			jj_consume_token(0);
			break;
		default:
			jj_la1[0] = jj_gen;
			jj_consume_token(-1);
			throw new ParseException();
		}
	}

	final private void basicOperate() throws ParseException {
		Token x;
		advanceOperate();
		label_1: while (true) {
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case PLUS:
			case MINUS:
				;
				break;
			default:
				jj_la1[1] = jj_gen;
				break label_1;
			}
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case PLUS:
				x = jj_consume_token(PLUS);
				break;
			case MINUS:
				x = jj_consume_token(MINUS);
				break;
			default:
				jj_la1[2] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
			advanceOperate();
			BigDecimal a = argStack.pop();
			BigDecimal b = argStack.pop();
			if (x.kind == PLUS)
				argStack.push(b.add(a));
			else
				argStack.push(b.subtract(a));
		}
	}

	final private void advanceOperate() throws ParseException {
		Token x;
		negativeOrPositive();
		label_2: while (true) {
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case MULTIPLY:
			case DIVIDE:
			case POWER:
				;
				break;
			default:
				jj_la1[3] = jj_gen;
				break label_2;
			}
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case POWER:
				x = jj_consume_token(POWER);
				break;
			case MULTIPLY:
				x = jj_consume_token(MULTIPLY);
				break;
			case DIVIDE:
				x = jj_consume_token(DIVIDE);
				break;
			default:
				jj_la1[4] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
			negativeOrPositive();
			BigDecimal a = argStack.pop();
			BigDecimal b = argStack.pop();
			if (x.kind == MULTIPLY)
				argStack.push(b.multiply(a, getMathContext()));
			else if (x.kind == DIVIDE) {
				argStack.push(b.divide(a, getMathContext()));
			} else if (x.kind == POWER) {
				argStack.push(b.pow(a.intValue(), getMathContext()));
			}
		}
	}

	final private void negativeOrPositive() throws ParseException {
		switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
		case MINUS:
			jj_consume_token(MINUS);
			toNumber();
			BigDecimal a = argStack.pop();
			argStack.push(BigDecimal.ZERO.subtract(a));
			break;
		case LBRACKET:
		case PLUS:
		case ROUND:
		case FLOOR:
		case CEIL:
		case MAX:
		case MIN:
		case CONDITION:
		case CONSTANT:
			toNumber();
			break;
		default:
			jj_la1[5] = jj_gen;
			jj_consume_token(-1);
			throw new ParseException();
		}
	}

	final private void toNumber() throws ParseException {
		switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
		case CONDITION:
			condition();
			break;
		case ROUND:
		case FLOOR:
		case CEIL:
		case MAX:
		case MIN:
			mathOperate();
			break;
		case PLUS:
		case CONSTANT:
			primaryNumber();
			break;
		case LBRACKET:
			expression();
			break;
		default:
			jj_la1[6] = jj_gen;
			jj_consume_token(-1);
			throw new ParseException();
		}
	}

	final private void expression() throws ParseException {
		jj_consume_token(LBRACKET);
		basicOperate();
		jj_consume_token(RBRACKET);
	}

	final private void primaryNumber() throws ParseException {
		BigDecimal a;
		switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
		case CONSTANT:
			jj_consume_token(CONSTANT);
			break;
		case PLUS:
			jj_consume_token(PLUS);
			jj_consume_token(CONSTANT);
			break;
		default:
			jj_la1[7] = jj_gen;
			jj_consume_token(-1);
			throw new ParseException();
		}
		try {
			a = new BigDecimal(token.image);
			argStack.push(a);
		} catch (NumberFormatException ee) {
			argStack.push(BigDecimal.ZERO);
		}
	}

	final private void mathOperate() throws ParseException {
		int i = 0;
		BigDecimal a, b;
		switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
		case ROUND:
			jj_consume_token(ROUND);
			jj_consume_token(LBRACKET);
			basicOperate();
			jj_consume_token(RBRACKET);
			a = argStack.pop();
			BigDecimal rounded = a.setScale(0, RoundingMode.HALF_UP);
			argStack.push(rounded);
			break;
		case FLOOR:
			jj_consume_token(FLOOR);
			jj_consume_token(LBRACKET);
			basicOperate();
			jj_consume_token(RBRACKET);
			a = argStack.pop();
			BigDecimal floored = a.setScale(0, RoundingMode.FLOOR);
			argStack.push(floored);
			break;
		case CEIL:
			jj_consume_token(CEIL);
			jj_consume_token(LBRACKET);
			basicOperate();
			jj_consume_token(RBRACKET);
			a = argStack.pop();
			BigDecimal ceiled = a.setScale(0, RoundingMode.CEILING);
			argStack.push(ceiled);
			break;
		case MAX:
			jj_consume_token(MAX);
			jj_consume_token(LBRACKET);
			basicOperate();
			jj_consume_token(SEPERATOR);
			i++;
			basicOperate();
			label_3: while (true) {
				switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
				case SEPERATOR:
					;
					break;
				default:
					jj_la1[8] = jj_gen;
					break label_3;
				}
				jj_consume_token(SEPERATOR);
				i++;
				basicOperate();
			}
			jj_consume_token(RBRACKET);
			while (i > 0) {
				a = argStack.pop();
				b = argStack.pop();
				if (a.compareTo(b) >= 0) {
					argStack.push(a);
				} else {
					argStack.push(b);
				}
				i--;
			}
			break;
		case MIN:
			jj_consume_token(MIN);
			jj_consume_token(LBRACKET);
			basicOperate();
			jj_consume_token(SEPERATOR);
			i++;
			basicOperate();
			label_4: while (true) {
				switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
				case SEPERATOR:
					;
					break;
				default:
					jj_la1[9] = jj_gen;
					break label_4;
				}
				jj_consume_token(SEPERATOR);
				i++;
				basicOperate();
			}
			jj_consume_token(RBRACKET);
			while (i > 0) {
				a = argStack.pop();
				b = argStack.pop();
				if (a.compareTo(b) <= 0) {
					argStack.push(a);
				} else {
					argStack.push(b);
				}
				i--;
			}
			break;
		default:
			jj_la1[10] = jj_gen;
			jj_consume_token(-1);
			throw new ParseException();
		}
	}

	final private void condition() throws ParseException {
		boolean flag;
		BigDecimal a, b;
		jj_consume_token(CONDITION);
		jj_consume_token(LBRACKET);
		flag = logicExpression();
		jj_consume_token(SEPERATOR);
		basicOperate();
		a = argStack.pop();
		jj_consume_token(SEPERATOR);
		basicOperate();
		b = argStack.pop();
		jj_consume_token(RBRACKET);
		if (flag) {
			argStack.push(a);
		} else {
			argStack.push(b);
		}
	}

	final private boolean logicExpression() throws ParseException {
		boolean flag1, flag2, flag3 = true, flag4 = true;
		BigDecimal a, b;
		boolean result = true;
		switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
		case AND:
			jj_consume_token(AND);
			jj_consume_token(LBRACKET);
			flag1 = logicExpression();
			jj_consume_token(SEPERATOR);
			flag2 = logicExpression();
			label_5: while (true) {
				switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
				case SEPERATOR:
					;
					break;
				default:
					jj_la1[11] = jj_gen;
					break label_5;
				}
				jj_consume_token(SEPERATOR);
				flag3 = logicExpression();
				flag4 = flag4 && flag3;
			}
			jj_consume_token(RBRACKET); {
			if (true)
				result = flag1 && flag2 && flag4;
		}
			break;
		case OR:
			jj_consume_token(OR);
			jj_consume_token(LBRACKET);
			flag1 = logicExpression();
			jj_consume_token(SEPERATOR);
			flag2 = logicExpression();
			label_6: while (true) {
				switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
				case SEPERATOR:
					;
					break;
				default:
					jj_la1[12] = jj_gen;
					break label_6;
				}
				jj_consume_token(SEPERATOR);
				flag3 = logicExpression();
				flag4 = flag4 || flag3;
			}
			jj_consume_token(RBRACKET); {
			if (true)
				result = flag1 || flag2 || flag4;
		}
			break;
		default:
			jj_la1[13] = jj_gen;
			if (jj_2_1(2147483647)) {
				basicOperate();
				a = argStack.pop();
				jj_consume_token(LARGER);
				basicOperate();
				b = argStack.pop();
				{
					if (true)
						result = a.compareTo(b) > 0;
				}
			} else if (jj_2_2(2147483647)) {
				basicOperate();
				a = argStack.pop();
				jj_consume_token(LESSER);
				basicOperate();
				b = argStack.pop();
				{
					if (true)
						result = a.compareTo(b) < 0;
				}
			} else if (jj_2_3(2147483647)) {
				basicOperate();
				a = argStack.pop();
				jj_consume_token(EQUALS);
				basicOperate();
				b = argStack.pop();
				{
					if (true)
						result = a.compareTo(b) == 0;
				}
			} else if (jj_2_4(2147483647)) {
				basicOperate();
				a = argStack.pop();
				jj_consume_token(NOT_EQUALS);
				basicOperate();
				b = argStack.pop();
				{
					if (true)
						result = a.compareTo(b) != 0;
				}
			} else if (jj_2_5(2147483647)) {
				basicOperate();
				a = argStack.pop();
				jj_consume_token(LARGER_OR_EQUALS);
				basicOperate();
				b = argStack.pop();
				{
					if (true)
						result = a.compareTo(b) >= 0;
				}
			} else if (jj_2_6(2147483647)) {
				basicOperate();
				a = argStack.pop();
				jj_consume_token(LESSER_OR_EQUALS);
				basicOperate();
				b = argStack.pop();
				{
					if (true)
						result = a.compareTo(b) <= 0;
				}
			} else {
				jj_consume_token(-1);
				throw new ParseException();
			}
		}
		return result;
	}

	private MathContext getMathContext() throws ParseException {
		return new MathContext(this.divineScale, this.divineRoundMode);
	}

	private boolean jj_2_1(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_1();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(0, xla);
		}
	}

	private boolean jj_2_2(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_2();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(1, xla);
		}
	}

	private boolean jj_2_3(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_3();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(2, xla);
		}
	}

	private boolean jj_2_4(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_4();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(3, xla);
		}
	}

	private boolean jj_2_5(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_5();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(4, xla);
		}
	}

	private boolean jj_2_6(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_6();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(5, xla);
		}
	}

	private boolean jj_3R_32() {
		if (jj_scan_token(SEPERATOR))
			return true;
		if (jj_3R_7())
			return true;
		return false;
	}

	private boolean jj_3_3() {
		if (jj_3R_7())
			return true;
		if (jj_scan_token(EQUALS))
			return true;
		if (jj_3R_7())
			return true;
		return false;
	}

	private boolean jj_3R_21() {
		Token xsp;
		xsp = jj_scanpos;
		if (jj_scan_token(28)) {
			jj_scanpos = xsp;
			if (jj_3R_29())
				return true;
		}
		return false;
	}

	private boolean jj_3_2() {
		if (jj_3R_7())
			return true;
		if (jj_scan_token(LESSER))
			return true;
		if (jj_3R_7())
			return true;
		return false;
	}

	private boolean jj_3R_17() {
		if (jj_3R_21())
			return true;
		return false;
	}

	private boolean jj_3_1() {
		if (jj_3R_7())
			return true;
		if (jj_scan_token(LARGER))
			return true;
		if (jj_3R_7())
			return true;
		return false;
	}

	private boolean jj_3R_16() {
		if (jj_3R_20())
			return true;
		return false;
	}

	private boolean jj_3R_22() {
		if (jj_scan_token(LBRACKET))
			return true;
		if (jj_3R_7())
			return true;
		if (jj_scan_token(RBRACKET))
			return true;
		return false;
	}

	private boolean jj_3R_35() {
		if (jj_scan_token(SEPERATOR))
			return true;
		if (jj_3R_23())
			return true;
		return false;
	}

	private boolean jj_3R_15() {
		if (jj_3R_19())
			return true;
		return false;
	}

	private boolean jj_3R_14() {
		Token xsp;
		xsp = jj_scanpos;
		if (jj_3R_15()) {
			jj_scanpos = xsp;
			if (jj_3R_16()) {
				jj_scanpos = xsp;
				if (jj_3R_17()) {
					jj_scanpos = xsp;
					if (jj_3R_18())
						return true;
				}
			}
		}
		return false;
	}

	private boolean jj_3R_31() {
		if (jj_scan_token(OR))
			return true;
		if (jj_scan_token(LBRACKET))
			return true;
		if (jj_3R_23())
			return true;
		if (jj_scan_token(SEPERATOR))
			return true;
		if (jj_3R_23())
			return true;
		Token xsp;
		while (true) {
			xsp = jj_scanpos;
			if (jj_3R_35()) {
				jj_scanpos = xsp;
				break;
			}
		}
		if (jj_scan_token(RBRACKET))
			return true;
		return false;
	}

	private boolean jj_3R_18() {
		if (jj_3R_22())
			return true;
		return false;
	}

	private boolean jj_3R_34() {
		if (jj_scan_token(SEPERATOR))
			return true;
		if (jj_3R_23())
			return true;
		return false;
	}

	private boolean jj_3R_13() {
		if (jj_3R_14())
			return true;
		return false;
	}

	private boolean jj_3R_23() {
		Token xsp;
		xsp = jj_scanpos;
		if (jj_3R_30()) {
			jj_scanpos = xsp;
			if (jj_3R_31()) {
				jj_scanpos = xsp;
				if (jj_3_1()) {
					jj_scanpos = xsp;
					if (jj_3_2()) {
						jj_scanpos = xsp;
						if (jj_3_3()) {
							jj_scanpos = xsp;
							if (jj_3_4()) {
								jj_scanpos = xsp;
								if (jj_3_5()) {
									jj_scanpos = xsp;
									if (jj_3_6())
										return true;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	private boolean jj_3R_30() {
		if (jj_scan_token(AND))
			return true;
		if (jj_scan_token(LBRACKET))
			return true;
		if (jj_3R_23())
			return true;
		if (jj_scan_token(SEPERATOR))
			return true;
		if (jj_3R_23())
			return true;
		Token xsp;
		while (true) {
			xsp = jj_scanpos;
			if (jj_3R_34()) {
				jj_scanpos = xsp;
				break;
			}
		}
		if (jj_scan_token(RBRACKET))
			return true;
		return false;
	}

	private boolean jj_3R_12() {
		if (jj_scan_token(MINUS))
			return true;
		if (jj_3R_14())
			return true;
		return false;
	}

	private boolean jj_3R_10() {
		Token xsp;
		xsp = jj_scanpos;
		if (jj_3R_12()) {
			jj_scanpos = xsp;
			if (jj_3R_13())
				return true;
		}
		return false;
	}

	private boolean jj_3R_19() {
		if (jj_scan_token(CONDITION))
			return true;
		if (jj_scan_token(LBRACKET))
			return true;
		if (jj_3R_23())
			return true;
		if (jj_scan_token(SEPERATOR))
			return true;
		if (jj_3R_7())
			return true;
		if (jj_scan_token(SEPERATOR))
			return true;
		if (jj_3R_7())
			return true;
		if (jj_scan_token(RBRACKET))
			return true;
		return false;
	}

	private boolean jj_3R_11() {
		Token xsp;
		xsp = jj_scanpos;
		if (jj_scan_token(13)) {
			jj_scanpos = xsp;
			if (jj_scan_token(11)) {
				jj_scanpos = xsp;
				if (jj_scan_token(12))
					return true;
			}
		}
		if (jj_3R_10())
			return true;
		return false;
	}

	private boolean jj_3R_8() {
		if (jj_3R_10())
			return true;
		Token xsp;
		while (true) {
			xsp = jj_scanpos;
			if (jj_3R_11()) {
				jj_scanpos = xsp;
				break;
			}
		}
		return false;
	}

	private boolean jj_3R_28() {
		if (jj_scan_token(MIN))
			return true;
		if (jj_scan_token(LBRACKET))
			return true;
		if (jj_3R_7())
			return true;
		if (jj_scan_token(SEPERATOR))
			return true;
		if (jj_3R_7())
			return true;
		Token xsp;
		while (true) {
			xsp = jj_scanpos;
			if (jj_3R_33()) {
				jj_scanpos = xsp;
				break;
			}
		}
		if (jj_scan_token(RBRACKET))
			return true;
		return false;
	}

	private boolean jj_3R_9() {
		Token xsp;
		xsp = jj_scanpos;
		if (jj_scan_token(9)) {
			jj_scanpos = xsp;
			if (jj_scan_token(10))
				return true;
		}
		if (jj_3R_8())
			return true;
		return false;
	}

	private boolean jj_3R_7() {
		if (jj_3R_8())
			return true;
		Token xsp;
		while (true) {
			xsp = jj_scanpos;
			if (jj_3R_9()) {
				jj_scanpos = xsp;
				break;
			}
		}
		return false;
	}

	private boolean jj_3R_27() {
		if (jj_scan_token(MAX))
			return true;
		if (jj_scan_token(LBRACKET))
			return true;
		if (jj_3R_7())
			return true;
		if (jj_scan_token(SEPERATOR))
			return true;
		if (jj_3R_7())
			return true;
		Token xsp;
		while (true) {
			xsp = jj_scanpos;
			if (jj_3R_32()) {
				jj_scanpos = xsp;
				break;
			}
		}
		if (jj_scan_token(RBRACKET))
			return true;
		return false;
	}

	private boolean jj_3R_26() {
		if (jj_scan_token(CEIL))
			return true;
		if (jj_scan_token(LBRACKET))
			return true;
		if (jj_3R_7())
			return true;
		if (jj_scan_token(RBRACKET))
			return true;
		return false;
	}

	private boolean jj_3R_33() {
		if (jj_scan_token(SEPERATOR))
			return true;
		if (jj_3R_7())
			return true;
		return false;
	}

	private boolean jj_3R_25() {
		if (jj_scan_token(FLOOR))
			return true;
		if (jj_scan_token(LBRACKET))
			return true;
		if (jj_3R_7())
			return true;
		if (jj_scan_token(RBRACKET))
			return true;
		return false;
	}

	private boolean jj_3_6() {
		if (jj_3R_7())
			return true;
		if (jj_scan_token(LESSER_OR_EQUALS))
			return true;
		if (jj_3R_7())
			return true;
		return false;
	}

	private boolean jj_3R_29() {
		if (jj_scan_token(PLUS))
			return true;
		if (jj_scan_token(CONSTANT))
			return true;
		return false;
	}

	private boolean jj_3_5() {
		if (jj_3R_7())
			return true;
		if (jj_scan_token(LARGER_OR_EQUALS))
			return true;
		if (jj_3R_7())
			return true;
		return false;
	}

	private boolean jj_3R_20() {
		Token xsp;
		xsp = jj_scanpos;
		if (jj_3R_24()) {
			jj_scanpos = xsp;
			if (jj_3R_25()) {
				jj_scanpos = xsp;
				if (jj_3R_26()) {
					jj_scanpos = xsp;
					if (jj_3R_27()) {
						jj_scanpos = xsp;
						if (jj_3R_28())
							return true;
					}
				}
			}
		}
		return false;
	}

	private boolean jj_3R_24() {
		if (jj_scan_token(ROUND))
			return true;
		if (jj_scan_token(LBRACKET))
			return true;
		if (jj_3R_7())
			return true;
		if (jj_scan_token(RBRACKET))
			return true;
		return false;
	}

	private boolean jj_3_4() {
		if (jj_3R_7())
			return true;
		if (jj_scan_token(NOT_EQUALS))
			return true;
		if (jj_3R_7())
			return true;
		return false;
	}

	/** Generated Token Manager. */
	public ExpressionParserTokenManager token_source;
	SimpleCharStream jj_input_stream;
	/** Current token. */
	public Token token;
	/** Next token. */
	public Token jj_nt;
	private int jj_ntk;
	private Token jj_scanpos, jj_lastpos;
	private int jj_la;
	private int jj_gen;
	final private int[] jj_la1 = new int[14];
	static private int[] jj_la1_0;
	static {
		jj_la1_init_0();
	}

	private static void jj_la1_init_0() {
		jj_la1_0 = new int[] { 0x100fc681, 0x600, 0x600, 0x3800, 0x3800, 0x100fc680, 0x100fc280, 0x10000200, 0x40, 0x40,
				0x7c000, 0x40, 0x40, 0x300000, };
	}

	final private JJCalls[] jj_2_rtns = new JJCalls[6];
	private boolean jj_rescan = false;
	private int jj_gc = 0;

	/** Constructor with InputStream. */
	public ExpressionParser(java.io.InputStream stream) {
		this(stream, null);
	}

	/** Constructor with InputStream and supplied encoding */
	public ExpressionParser(java.io.InputStream stream, String encoding) {
		try {
			jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1);
		} catch (java.io.UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		token_source = new ExpressionParserTokenManager(jj_input_stream);
		token = new Token();
		jj_ntk = -1;
		jj_gen = 0;
		for (int i = 0; i < 14; i++)
			jj_la1[i] = -1;
		for (int i = 0; i < jj_2_rtns.length; i++)
			jj_2_rtns[i] = new JJCalls();
	}

	/** Reinitialise. */
	public void ReInit(java.io.InputStream stream) {
		ReInit(stream, null);
	}

	/** Reinitialise. */
	public void ReInit(java.io.InputStream stream, String encoding) {
		try {
			jj_input_stream.ReInit(stream, encoding, 1, 1);
		} catch (java.io.UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		token_source.ReInit(jj_input_stream);
		token = new Token();
		jj_ntk = -1;
		jj_gen = 0;
		for (int i = 0; i < 14; i++)
			jj_la1[i] = -1;
		for (int i = 0; i < jj_2_rtns.length; i++)
			jj_2_rtns[i] = new JJCalls();
	}

	/** Constructor. */
	public ExpressionParser(java.io.Reader stream) {
		jj_input_stream = new SimpleCharStream(stream, 1, 1);
		token_source = new ExpressionParserTokenManager(jj_input_stream);
		token = new Token();
		jj_ntk = -1;
		jj_gen = 0;
		for (int i = 0; i < 14; i++)
			jj_la1[i] = -1;
		for (int i = 0; i < jj_2_rtns.length; i++)
			jj_2_rtns[i] = new JJCalls();
	}

	/** Reinitialise. */
	public void ReInit(java.io.Reader stream) {
		jj_input_stream.ReInit(stream, 1, 1);
		token_source.ReInit(jj_input_stream);
		token = new Token();
		jj_ntk = -1;
		jj_gen = 0;
		for (int i = 0; i < 14; i++)
			jj_la1[i] = -1;
		for (int i = 0; i < jj_2_rtns.length; i++)
			jj_2_rtns[i] = new JJCalls();
	}

	/** Constructor with generated Token Manager. */
	public ExpressionParser(ExpressionParserTokenManager tm) {
		token_source = tm;
		token = new Token();
		jj_ntk = -1;
		jj_gen = 0;
		for (int i = 0; i < 14; i++)
			jj_la1[i] = -1;
		for (int i = 0; i < jj_2_rtns.length; i++)
			jj_2_rtns[i] = new JJCalls();
	}

	/** Reinitialise. */
	public void ReInit(ExpressionParserTokenManager tm) {
		token_source = tm;
		token = new Token();
		jj_ntk = -1;
		jj_gen = 0;
		for (int i = 0; i < 14; i++)
			jj_la1[i] = -1;
		for (int i = 0; i < jj_2_rtns.length; i++)
			jj_2_rtns[i] = new JJCalls();
	}

	private Token jj_consume_token(int kind) throws ParseException {
		Token oldToken;
		if ((oldToken = token).next != null)
			token = token.next;
		else
			token = token.next = token_source.getNextToken();
		jj_ntk = -1;
		if (token.kind == kind) {
			jj_gen++;
			if (++jj_gc > 100) {
				jj_gc = 0;
				for (int i = 0; i < jj_2_rtns.length; i++) {
					JJCalls c = jj_2_rtns[i];
					while (c != null) {
						if (c.gen < jj_gen)
							c.first = null;
						c = c.next;
					}
				}
			}
			return token;
		}
		token = oldToken;
		jj_kind = kind;
		throw generateParseException();
	}

	static private final class LookaheadSuccess extends java.lang.Error {
		private static final long serialVersionUID = 1L;
	}

	final private LookaheadSuccess jj_ls = new LookaheadSuccess();

	private boolean jj_scan_token(int kind) {
		if (jj_scanpos == jj_lastpos) {
			jj_la--;
			if (jj_scanpos.next == null) {
				jj_lastpos = jj_scanpos = jj_scanpos.next = token_source.getNextToken();
			} else {
				jj_lastpos = jj_scanpos = jj_scanpos.next;
			}
		} else {
			jj_scanpos = jj_scanpos.next;
		}
		if (jj_rescan) {
			int i = 0;
			Token tok = token;
			while (tok != null && tok != jj_scanpos) {
				i++;
				tok = tok.next;
			}
			if (tok != null)
				jj_add_error_token(kind, i);
		}
		if (jj_scanpos.kind != kind)
			return true;
		if (jj_la == 0 && jj_scanpos == jj_lastpos)
			throw jj_ls;
		return false;
	}

	/** Get the next Token. */
	final public Token getNextToken() {
		if (token.next != null)
			token = token.next;
		else
			token = token.next = token_source.getNextToken();
		jj_ntk = -1;
		jj_gen++;
		return token;
	}

	/** Get the specific Token. */
	final public Token getToken(int index) {
		Token t = token;
		for (int i = 0; i < index; i++) {
			if (t.next != null)
				t = t.next;
			else
				t = t.next = token_source.getNextToken();
		}
		return t;
	}

	private int jj_ntk() {
		if ((jj_nt = token.next) == null)
			return (jj_ntk = (token.next = token_source.getNextToken()).kind);
		else
			return (jj_ntk = jj_nt.kind);
	}

	private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
	private int[] jj_expentry;
	private int jj_kind = -1;
	private int[] jj_lasttokens = new int[100];
	private int jj_endpos;

	private void jj_add_error_token(int kind, int pos) {
		if (pos >= 100)
			return;
		if (pos == jj_endpos + 1) {
			jj_lasttokens[jj_endpos++] = kind;
		} else if (jj_endpos != 0) {
			jj_expentry = new int[jj_endpos];
			for (int i = 0; i < jj_endpos; i++) {
				jj_expentry[i] = jj_lasttokens[i];
			}
			jj_entries_loop: for (java.util.Iterator<?> it = jj_expentries.iterator(); it.hasNext();) {
				int[] oldentry = (int[]) (it.next());
				if (oldentry.length == jj_expentry.length) {
					for (int i = 0; i < jj_expentry.length; i++) {
						if (oldentry[i] != jj_expentry[i]) {
							continue jj_entries_loop;
						}
					}
					jj_expentries.add(jj_expentry);
					break jj_entries_loop;
				}
			}
			if (pos != 0)
				jj_lasttokens[(jj_endpos = pos) - 1] = kind;
		}
	}

	/** Generate ParseException. */
	public ParseException generateParseException() {
		jj_expentries.clear();
		boolean[] la1tokens = new boolean[31];
		if (jj_kind >= 0) {
			la1tokens[jj_kind] = true;
			jj_kind = -1;
		}
		for (int i = 0; i < 14; i++) {
			if (jj_la1[i] == jj_gen) {
				for (int j = 0; j < 32; j++) {
					if ((jj_la1_0[i] & (1 << j)) != 0) {
						la1tokens[j] = true;
					}
				}
			}
		}
		for (int i = 0; i < 31; i++) {
			if (la1tokens[i]) {
				jj_expentry = new int[1];
				jj_expentry[0] = i;
				jj_expentries.add(jj_expentry);
			}
		}
		jj_endpos = 0;
		jj_rescan_token();
		jj_add_error_token(0, 0);
		int[][] exptokseq = new int[jj_expentries.size()][];
		for (int i = 0; i < jj_expentries.size(); i++) {
			exptokseq[i] = jj_expentries.get(i);
		}
		return new ParseException(token, exptokseq, tokenImage);
	}

	/** Enable tracing. */
	final public void enable_tracing() {
	}

	/** Disable tracing. */
	final public void disable_tracing() {
	}

	private void jj_rescan_token() {
		jj_rescan = true;
		for (int i = 0; i < 6; i++) {
			try {
				JJCalls p = jj_2_rtns[i];
				do {
					if (p.gen > jj_gen) {
						jj_la = p.arg;
						jj_lastpos = jj_scanpos = p.first;
						switch (i) {
						case 0:
							jj_3_1();
							break;
						case 1:
							jj_3_2();
							break;
						case 2:
							jj_3_3();
							break;
						case 3:
							jj_3_4();
							break;
						case 4:
							jj_3_5();
							break;
						case 5:
							jj_3_6();
							break;
						}
					}
					p = p.next;
				} while (p != null);
			} catch (LookaheadSuccess ls) {
			}
		}
		jj_rescan = false;
	}

	private void jj_save(int index, int xla) {
		JJCalls p = jj_2_rtns[index];
		while (p.gen > jj_gen) {
			if (p.next == null) {
				p = p.next = new JJCalls();
				break;
			}
			p = p.next;
		}
		p.gen = jj_gen + xla - jj_la;
		p.first = token;
		p.arg = xla;
	}

	static final class JJCalls {
		int gen;
		Token first;
		int arg;
		JJCalls next;
	}

}
