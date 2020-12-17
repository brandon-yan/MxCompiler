// Generated from C:/Users/hanchong/Desktop/MxCompier/src/Parser\Mx.g4 by ANTLR 4.9
package Parser;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MxLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, Int=7, Bool=8, String=9, 
		True=10, False=11, Null=12, If=13, Else=14, For=15, While=16, Break=17, 
		Continue=18, Return=19, This=20, LeftParen=21, RightParen=22, LeftBracket=23, 
		RightBracket=24, LeftBrace=25, RightBrace=26, Less=27, LessEqual=28, Greater=29, 
		GreaterEqual=30, LeftShift=31, RightShift=32, Plus=33, Minus=34, And=35, 
		Or=36, AndAnd=37, OrOr=38, Caret=39, Not=40, Tilde=41, Question=42, Colon=43, 
		Semi=44, Comma=45, Assign=46, Equal=47, NotEqual=48, Identifier=49, IntegerConstant=50, 
		BoolConstant=51, StringConstant=52, NullConstant=53, Whitespace=54, Newline=55, 
		BlockComment=56, LineComment=57;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "Int", "Bool", "String", 
			"True", "False", "Null", "If", "Else", "For", "While", "Break", "Continue", 
			"Return", "This", "LeftParen", "RightParen", "LeftBracket", "RightBracket", 
			"LeftBrace", "RightBrace", "Less", "LessEqual", "Greater", "GreaterEqual", 
			"LeftShift", "RightShift", "Plus", "Minus", "And", "Or", "AndAnd", "OrOr", 
			"Caret", "Not", "Tilde", "Question", "Colon", "Semi", "Comma", "Assign", 
			"Equal", "NotEqual", "Identifier", "IntegerConstant", "BoolConstant", 
			"StringConstant", "NullConstant", "Whitespace", "Newline", "BlockComment", 
			"LineComment"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'int main()'", "'*'", "'/'", "'%'", "'++'", "'--'", "'int'", "'bool'", 
			"'string'", "'true'", "'false'", "'null'", "'if'", "'else'", "'for'", 
			"'while'", "'break'", "'continue'", "'return'", "'this'", "'('", "')'", 
			"'['", "']'", "'{'", "'}'", "'<'", "'<='", "'>'", "'>='", "'<<'", "'>>'", 
			"'+'", "'-'", "'&'", "'|'", "'&&'", "'||'", "'^'", "'!'", "'~'", "'?'", 
			"':'", "';'", "','", "'='", "'=='", "'!='"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, "Int", "Bool", "String", "True", 
			"False", "Null", "If", "Else", "For", "While", "Break", "Continue", "Return", 
			"This", "LeftParen", "RightParen", "LeftBracket", "RightBracket", "LeftBrace", 
			"RightBrace", "Less", "LessEqual", "Greater", "GreaterEqual", "LeftShift", 
			"RightShift", "Plus", "Minus", "And", "Or", "AndAnd", "OrOr", "Caret", 
			"Not", "Tilde", "Question", "Colon", "Semi", "Comma", "Assign", "Equal", 
			"NotEqual", "Identifier", "IntegerConstant", "BoolConstant", "StringConstant", 
			"NullConstant", "Whitespace", "Newline", "BlockComment", "LineComment"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public MxLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Mx.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2;\u0167\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\3\2\3\2\3\2\3\2\3"+
		"\2\3\2\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\6\3\7\3\7"+
		"\3\7\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3"+
		"\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3"+
		"\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\21\3\21\3"+
		"\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3"+
		"\23\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\25\3\25\3"+
		"\25\3\25\3\25\3\26\3\26\3\27\3\27\3\30\3\30\3\31\3\31\3\32\3\32\3\33\3"+
		"\33\3\34\3\34\3\35\3\35\3\35\3\36\3\36\3\37\3\37\3\37\3 \3 \3 \3!\3!\3"+
		"!\3\"\3\"\3#\3#\3$\3$\3%\3%\3&\3&\3&\3\'\3\'\3\'\3(\3(\3)\3)\3*\3*\3+"+
		"\3+\3,\3,\3-\3-\3.\3.\3/\3/\3\60\3\60\3\60\3\61\3\61\3\61\3\62\3\62\7"+
		"\62\u011c\n\62\f\62\16\62\u011f\13\62\3\63\3\63\7\63\u0123\n\63\f\63\16"+
		"\63\u0126\13\63\3\63\5\63\u0129\n\63\3\64\3\64\5\64\u012d\n\64\3\65\3"+
		"\65\3\65\3\65\3\65\3\65\3\65\7\65\u0136\n\65\f\65\16\65\u0139\13\65\3"+
		"\65\3\65\3\66\3\66\3\67\6\67\u0140\n\67\r\67\16\67\u0141\3\67\3\67\38"+
		"\38\58\u0148\n8\38\58\u014b\n8\38\38\39\39\39\39\79\u0153\n9\f9\169\u0156"+
		"\139\39\39\39\39\39\3:\3:\3:\3:\7:\u0161\n:\f:\16:\u0164\13:\3:\3:\4\u0137"+
		"\u0154\2;\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33"+
		"\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67"+
		"\359\36;\37= ?!A\"C#E$G%I&K\'M(O)Q*S+U,W-Y.[/]\60_\61a\62c\63e\64g\65"+
		"i\66k\67m8o9q:s;\3\2\b\4\2C\\c|\6\2\62;C\\aac|\3\2\63;\3\2\62;\4\2\13"+
		"\13\"\"\4\2\f\f\17\17\2\u0173\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t"+
		"\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2"+
		"\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2"+
		"\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2"+
		"+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2"+
		"\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2"+
		"C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3"+
		"\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2"+
		"\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2g\3\2\2\2\2"+
		"i\3\2\2\2\2k\3\2\2\2\2m\3\2\2\2\2o\3\2\2\2\2q\3\2\2\2\2s\3\2\2\2\3u\3"+
		"\2\2\2\5\u0080\3\2\2\2\7\u0082\3\2\2\2\t\u0084\3\2\2\2\13\u0086\3\2\2"+
		"\2\r\u0089\3\2\2\2\17\u008c\3\2\2\2\21\u0090\3\2\2\2\23\u0095\3\2\2\2"+
		"\25\u009c\3\2\2\2\27\u00a1\3\2\2\2\31\u00a7\3\2\2\2\33\u00ac\3\2\2\2\35"+
		"\u00af\3\2\2\2\37\u00b4\3\2\2\2!\u00b8\3\2\2\2#\u00be\3\2\2\2%\u00c4\3"+
		"\2\2\2\'\u00cd\3\2\2\2)\u00d4\3\2\2\2+\u00d9\3\2\2\2-\u00db\3\2\2\2/\u00dd"+
		"\3\2\2\2\61\u00df\3\2\2\2\63\u00e1\3\2\2\2\65\u00e3\3\2\2\2\67\u00e5\3"+
		"\2\2\29\u00e7\3\2\2\2;\u00ea\3\2\2\2=\u00ec\3\2\2\2?\u00ef\3\2\2\2A\u00f2"+
		"\3\2\2\2C\u00f5\3\2\2\2E\u00f7\3\2\2\2G\u00f9\3\2\2\2I\u00fb\3\2\2\2K"+
		"\u00fd\3\2\2\2M\u0100\3\2\2\2O\u0103\3\2\2\2Q\u0105\3\2\2\2S\u0107\3\2"+
		"\2\2U\u0109\3\2\2\2W\u010b\3\2\2\2Y\u010d\3\2\2\2[\u010f\3\2\2\2]\u0111"+
		"\3\2\2\2_\u0113\3\2\2\2a\u0116\3\2\2\2c\u0119\3\2\2\2e\u0128\3\2\2\2g"+
		"\u012c\3\2\2\2i\u0137\3\2\2\2k\u013c\3\2\2\2m\u013f\3\2\2\2o\u014a\3\2"+
		"\2\2q\u014e\3\2\2\2s\u015c\3\2\2\2uv\7k\2\2vw\7p\2\2wx\7v\2\2xy\7\"\2"+
		"\2yz\7o\2\2z{\7c\2\2{|\7k\2\2|}\7p\2\2}~\7*\2\2~\177\7+\2\2\177\4\3\2"+
		"\2\2\u0080\u0081\7,\2\2\u0081\6\3\2\2\2\u0082\u0083\7\61\2\2\u0083\b\3"+
		"\2\2\2\u0084\u0085\7\'\2\2\u0085\n\3\2\2\2\u0086\u0087\7-\2\2\u0087\u0088"+
		"\7-\2\2\u0088\f\3\2\2\2\u0089\u008a\7/\2\2\u008a\u008b\7/\2\2\u008b\16"+
		"\3\2\2\2\u008c\u008d\7k\2\2\u008d\u008e\7p\2\2\u008e\u008f\7v\2\2\u008f"+
		"\20\3\2\2\2\u0090\u0091\7d\2\2\u0091\u0092\7q\2\2\u0092\u0093\7q\2\2\u0093"+
		"\u0094\7n\2\2\u0094\22\3\2\2\2\u0095\u0096\7u\2\2\u0096\u0097\7v\2\2\u0097"+
		"\u0098\7t\2\2\u0098\u0099\7k\2\2\u0099\u009a\7p\2\2\u009a\u009b\7i\2\2"+
		"\u009b\24\3\2\2\2\u009c\u009d\7v\2\2\u009d\u009e\7t\2\2\u009e\u009f\7"+
		"w\2\2\u009f\u00a0\7g\2\2\u00a0\26\3\2\2\2\u00a1\u00a2\7h\2\2\u00a2\u00a3"+
		"\7c\2\2\u00a3\u00a4\7n\2\2\u00a4\u00a5\7u\2\2\u00a5\u00a6\7g\2\2\u00a6"+
		"\30\3\2\2\2\u00a7\u00a8\7p\2\2\u00a8\u00a9\7w\2\2\u00a9\u00aa\7n\2\2\u00aa"+
		"\u00ab\7n\2\2\u00ab\32\3\2\2\2\u00ac\u00ad\7k\2\2\u00ad\u00ae\7h\2\2\u00ae"+
		"\34\3\2\2\2\u00af\u00b0\7g\2\2\u00b0\u00b1\7n\2\2\u00b1\u00b2\7u\2\2\u00b2"+
		"\u00b3\7g\2\2\u00b3\36\3\2\2\2\u00b4\u00b5\7h\2\2\u00b5\u00b6\7q\2\2\u00b6"+
		"\u00b7\7t\2\2\u00b7 \3\2\2\2\u00b8\u00b9\7y\2\2\u00b9\u00ba\7j\2\2\u00ba"+
		"\u00bb\7k\2\2\u00bb\u00bc\7n\2\2\u00bc\u00bd\7g\2\2\u00bd\"\3\2\2\2\u00be"+
		"\u00bf\7d\2\2\u00bf\u00c0\7t\2\2\u00c0\u00c1\7g\2\2\u00c1\u00c2\7c\2\2"+
		"\u00c2\u00c3\7m\2\2\u00c3$\3\2\2\2\u00c4\u00c5\7e\2\2\u00c5\u00c6\7q\2"+
		"\2\u00c6\u00c7\7p\2\2\u00c7\u00c8\7v\2\2\u00c8\u00c9\7k\2\2\u00c9\u00ca"+
		"\7p\2\2\u00ca\u00cb\7w\2\2\u00cb\u00cc\7g\2\2\u00cc&\3\2\2\2\u00cd\u00ce"+
		"\7t\2\2\u00ce\u00cf\7g\2\2\u00cf\u00d0\7v\2\2\u00d0\u00d1\7w\2\2\u00d1"+
		"\u00d2\7t\2\2\u00d2\u00d3\7p\2\2\u00d3(\3\2\2\2\u00d4\u00d5\7v\2\2\u00d5"+
		"\u00d6\7j\2\2\u00d6\u00d7\7k\2\2\u00d7\u00d8\7u\2\2\u00d8*\3\2\2\2\u00d9"+
		"\u00da\7*\2\2\u00da,\3\2\2\2\u00db\u00dc\7+\2\2\u00dc.\3\2\2\2\u00dd\u00de"+
		"\7]\2\2\u00de\60\3\2\2\2\u00df\u00e0\7_\2\2\u00e0\62\3\2\2\2\u00e1\u00e2"+
		"\7}\2\2\u00e2\64\3\2\2\2\u00e3\u00e4\7\177\2\2\u00e4\66\3\2\2\2\u00e5"+
		"\u00e6\7>\2\2\u00e68\3\2\2\2\u00e7\u00e8\7>\2\2\u00e8\u00e9\7?\2\2\u00e9"+
		":\3\2\2\2\u00ea\u00eb\7@\2\2\u00eb<\3\2\2\2\u00ec\u00ed\7@\2\2\u00ed\u00ee"+
		"\7?\2\2\u00ee>\3\2\2\2\u00ef\u00f0\7>\2\2\u00f0\u00f1\7>\2\2\u00f1@\3"+
		"\2\2\2\u00f2\u00f3\7@\2\2\u00f3\u00f4\7@\2\2\u00f4B\3\2\2\2\u00f5\u00f6"+
		"\7-\2\2\u00f6D\3\2\2\2\u00f7\u00f8\7/\2\2\u00f8F\3\2\2\2\u00f9\u00fa\7"+
		"(\2\2\u00faH\3\2\2\2\u00fb\u00fc\7~\2\2\u00fcJ\3\2\2\2\u00fd\u00fe\7("+
		"\2\2\u00fe\u00ff\7(\2\2\u00ffL\3\2\2\2\u0100\u0101\7~\2\2\u0101\u0102"+
		"\7~\2\2\u0102N\3\2\2\2\u0103\u0104\7`\2\2\u0104P\3\2\2\2\u0105\u0106\7"+
		"#\2\2\u0106R\3\2\2\2\u0107\u0108\7\u0080\2\2\u0108T\3\2\2\2\u0109\u010a"+
		"\7A\2\2\u010aV\3\2\2\2\u010b\u010c\7<\2\2\u010cX\3\2\2\2\u010d\u010e\7"+
		"=\2\2\u010eZ\3\2\2\2\u010f\u0110\7.\2\2\u0110\\\3\2\2\2\u0111\u0112\7"+
		"?\2\2\u0112^\3\2\2\2\u0113\u0114\7?\2\2\u0114\u0115\7?\2\2\u0115`\3\2"+
		"\2\2\u0116\u0117\7#\2\2\u0117\u0118\7?\2\2\u0118b\3\2\2\2\u0119\u011d"+
		"\t\2\2\2\u011a\u011c\t\3\2\2\u011b\u011a\3\2\2\2\u011c\u011f\3\2\2\2\u011d"+
		"\u011b\3\2\2\2\u011d\u011e\3\2\2\2\u011ed\3\2\2\2\u011f\u011d\3\2\2\2"+
		"\u0120\u0124\t\4\2\2\u0121\u0123\t\5\2\2\u0122\u0121\3\2\2\2\u0123\u0126"+
		"\3\2\2\2\u0124\u0122\3\2\2\2\u0124\u0125\3\2\2\2\u0125\u0129\3\2\2\2\u0126"+
		"\u0124\3\2\2\2\u0127\u0129\7\62\2\2\u0128\u0120\3\2\2\2\u0128\u0127\3"+
		"\2\2\2\u0129f\3\2\2\2\u012a\u012d\5\25\13\2\u012b\u012d\5\27\f\2\u012c"+
		"\u012a\3\2\2\2\u012c\u012b\3\2\2\2\u012dh\3\2\2\2\u012e\u012f\7^\2\2\u012f"+
		"\u0136\7p\2\2\u0130\u0131\7^\2\2\u0131\u0136\7^\2\2\u0132\u0133\7^\2\2"+
		"\u0133\u0136\7$\2\2\u0134\u0136\13\2\2\2\u0135\u012e\3\2\2\2\u0135\u0130"+
		"\3\2\2\2\u0135\u0132\3\2\2\2\u0135\u0134\3\2\2\2\u0136\u0139\3\2\2\2\u0137"+
		"\u0138\3\2\2\2\u0137\u0135\3\2\2\2\u0138\u013a\3\2\2\2\u0139\u0137\3\2"+
		"\2\2\u013a\u013b\7$\2\2\u013bj\3\2\2\2\u013c\u013d\5\31\r\2\u013dl\3\2"+
		"\2\2\u013e\u0140\t\6\2\2\u013f\u013e\3\2\2\2\u0140\u0141\3\2\2\2\u0141"+
		"\u013f\3\2\2\2\u0141\u0142\3\2\2\2\u0142\u0143\3\2\2\2\u0143\u0144\b\67"+
		"\2\2\u0144n\3\2\2\2\u0145\u0147\7\17\2\2\u0146\u0148\7\f\2\2\u0147\u0146"+
		"\3\2\2\2\u0147\u0148\3\2\2\2\u0148\u014b\3\2\2\2\u0149\u014b\7\f\2\2\u014a"+
		"\u0145\3\2\2\2\u014a\u0149\3\2\2\2\u014b\u014c\3\2\2\2\u014c\u014d\b8"+
		"\2\2\u014dp\3\2\2\2\u014e\u014f\7\61\2\2\u014f\u0150\7,\2\2\u0150\u0154"+
		"\3\2\2\2\u0151\u0153\13\2\2\2\u0152\u0151\3\2\2\2\u0153\u0156\3\2\2\2"+
		"\u0154\u0155\3\2\2\2\u0154\u0152\3\2\2\2\u0155\u0157\3\2\2\2\u0156\u0154"+
		"\3\2\2\2\u0157\u0158\7,\2\2\u0158\u0159\7\61\2\2\u0159\u015a\3\2\2\2\u015a"+
		"\u015b\b9\2\2\u015br\3\2\2\2\u015c\u015d\7\61\2\2\u015d\u015e\7\61\2\2"+
		"\u015e\u0162\3\2\2\2\u015f\u0161\n\7\2\2\u0160\u015f\3\2\2\2\u0161\u0164"+
		"\3\2\2\2\u0162\u0160\3\2\2\2\u0162\u0163\3\2\2\2\u0163\u0165\3\2\2\2\u0164"+
		"\u0162\3\2\2\2\u0165\u0166\b:\2\2\u0166t\3\2\2\2\16\2\u011d\u0124\u0128"+
		"\u012c\u0135\u0137\u0141\u0147\u014a\u0154\u0162\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}