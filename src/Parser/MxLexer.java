// Generated from C:/Users/hanchong/Desktop/Mx/src/Parser\Mx.g4 by ANTLR 4.9
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
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, IntegerConstant=9, 
		BoolConstant=10, StringConstant=11, NullConstant=12, Int=13, Bool=14, 
		String=15, True=16, False=17, Null=18, Void=19, If=20, Else=21, For=22, 
		While=23, Break=24, Continue=25, Return=26, This=27, New=28, Class=29, 
		LeftParen=30, RightParen=31, LeftBracket=32, RightBracket=33, LeftBrace=34, 
		RightBrace=35, Less=36, LessEqual=37, Greater=38, GreaterEqual=39, LeftShift=40, 
		RightShift=41, Plus=42, Minus=43, And=44, Or=45, AndAnd=46, OrOr=47, Caret=48, 
		Not=49, Tilde=50, Question=51, Colon=52, Semi=53, Comma=54, Assign=55, 
		Equal=56, NotEqual=57, Identifier=58, Whitespace=59, Newline=60, BlockComment=61, 
		LineComment=62;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "IntegerConstant", 
			"BoolConstant", "StringConstant", "NullConstant", "Int", "Bool", "String", 
			"True", "False", "Null", "Void", "If", "Else", "For", "While", "Break", 
			"Continue", "Return", "This", "New", "Class", "LeftParen", "RightParen", 
			"LeftBracket", "RightBracket", "LeftBrace", "RightBrace", "Less", "LessEqual", 
			"Greater", "GreaterEqual", "LeftShift", "RightShift", "Plus", "Minus", 
			"And", "Or", "AndAnd", "OrOr", "Caret", "Not", "Tilde", "Question", "Colon", 
			"Semi", "Comma", "Assign", "Equal", "NotEqual", "Identifier", "Whitespace", 
			"Newline", "BlockComment", "LineComment"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'.'", "'[&]'", "'->'", "'++'", "'--'", "'*'", "'/'", "'%'", null, 
			null, null, null, "'int'", "'bool'", "'string'", "'true'", "'false'", 
			"'null'", "'void'", "'if'", "'else'", "'for'", "'while'", "'break'", 
			"'continue'", "'return'", "'this'", "'new'", "'class'", "'('", "')'", 
			"'['", "']'", "'{'", "'}'", "'<'", "'<='", "'>'", "'>='", "'<<'", "'>>'", 
			"'+'", "'-'", "'&'", "'|'", "'&&'", "'||'", "'^'", "'!'", "'~'", "'?'", 
			"':'", "';'", "','", "'='", "'=='", "'!='"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, "IntegerConstant", 
			"BoolConstant", "StringConstant", "NullConstant", "Int", "Bool", "String", 
			"True", "False", "Null", "Void", "If", "Else", "For", "While", "Break", 
			"Continue", "Return", "This", "New", "Class", "LeftParen", "RightParen", 
			"LeftBracket", "RightBracket", "LeftBrace", "RightBrace", "Less", "LessEqual", 
			"Greater", "GreaterEqual", "LeftShift", "RightShift", "Plus", "Minus", 
			"And", "Or", "AndAnd", "OrOr", "Caret", "Not", "Tilde", "Question", "Colon", 
			"Semi", "Comma", "Assign", "Equal", "NotEqual", "Identifier", "Whitespace", 
			"Newline", "BlockComment", "LineComment"
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2@\u017f\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\3\2\3\2\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\5\3\5\3\5\3\6\3\6\3"+
		"\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\7\n\u0097\n\n\f\n\16\n\u009a\13\n\3"+
		"\n\5\n\u009d\n\n\3\13\3\13\5\13\u00a1\n\13\3\f\3\f\3\f\3\f\3\f\3\f\3\f"+
		"\3\f\7\f\u00ab\n\f\f\f\16\f\u00ae\13\f\3\f\3\f\3\r\3\r\3\16\3\16\3\16"+
		"\3\16\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\21"+
		"\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23"+
		"\3\23\3\24\3\24\3\24\3\24\3\24\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3\26"+
		"\3\27\3\27\3\27\3\27\3\30\3\30\3\30\3\30\3\30\3\30\3\31\3\31\3\31\3\31"+
		"\3\31\3\31\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\33\3\33\3\33"+
		"\3\33\3\33\3\33\3\33\3\34\3\34\3\34\3\34\3\34\3\35\3\35\3\35\3\35\3\36"+
		"\3\36\3\36\3\36\3\36\3\36\3\37\3\37\3 \3 \3!\3!\3\"\3\"\3#\3#\3$\3$\3"+
		"%\3%\3&\3&\3&\3\'\3\'\3(\3(\3(\3)\3)\3)\3*\3*\3*\3+\3+\3,\3,\3-\3-\3."+
		"\3.\3/\3/\3/\3\60\3\60\3\60\3\61\3\61\3\62\3\62\3\63\3\63\3\64\3\64\3"+
		"\65\3\65\3\66\3\66\3\67\3\67\38\38\39\39\39\3:\3:\3:\3;\3;\7;\u0152\n"+
		";\f;\16;\u0155\13;\3<\6<\u0158\n<\r<\16<\u0159\3<\3<\3=\3=\5=\u0160\n"+
		"=\3=\5=\u0163\n=\3=\3=\3>\3>\3>\3>\7>\u016b\n>\f>\16>\u016e\13>\3>\3>"+
		"\3>\3>\3>\3?\3?\3?\3?\7?\u0179\n?\f?\16?\u017c\13?\3?\3?\4\u00ac\u016c"+
		"\2@\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35"+
		"\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67\359\36"+
		";\37= ?!A\"C#E$G%I&K\'M(O)Q*S+U,W-Y.[/]\60_\61a\62c\63e\64g\65i\66k\67"+
		"m8o9q:s;u<w=y>{?}@\3\2\b\3\2\63;\3\2\62;\4\2C\\c|\6\2\62;C\\aac|\4\2\13"+
		"\13\"\"\4\2\f\f\17\17\2\u018b\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t"+
		"\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2"+
		"\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2"+
		"\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2"+
		"+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2"+
		"\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2"+
		"C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3"+
		"\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2"+
		"\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2g\3\2\2\2\2"+
		"i\3\2\2\2\2k\3\2\2\2\2m\3\2\2\2\2o\3\2\2\2\2q\3\2\2\2\2s\3\2\2\2\2u\3"+
		"\2\2\2\2w\3\2\2\2\2y\3\2\2\2\2{\3\2\2\2\2}\3\2\2\2\3\177\3\2\2\2\5\u0081"+
		"\3\2\2\2\7\u0085\3\2\2\2\t\u0088\3\2\2\2\13\u008b\3\2\2\2\r\u008e\3\2"+
		"\2\2\17\u0090\3\2\2\2\21\u0092\3\2\2\2\23\u009c\3\2\2\2\25\u00a0\3\2\2"+
		"\2\27\u00a2\3\2\2\2\31\u00b1\3\2\2\2\33\u00b3\3\2\2\2\35\u00b7\3\2\2\2"+
		"\37\u00bc\3\2\2\2!\u00c3\3\2\2\2#\u00c8\3\2\2\2%\u00ce\3\2\2\2\'\u00d3"+
		"\3\2\2\2)\u00d8\3\2\2\2+\u00db\3\2\2\2-\u00e0\3\2\2\2/\u00e4\3\2\2\2\61"+
		"\u00ea\3\2\2\2\63\u00f0\3\2\2\2\65\u00f9\3\2\2\2\67\u0100\3\2\2\29\u0105"+
		"\3\2\2\2;\u0109\3\2\2\2=\u010f\3\2\2\2?\u0111\3\2\2\2A\u0113\3\2\2\2C"+
		"\u0115\3\2\2\2E\u0117\3\2\2\2G\u0119\3\2\2\2I\u011b\3\2\2\2K\u011d\3\2"+
		"\2\2M\u0120\3\2\2\2O\u0122\3\2\2\2Q\u0125\3\2\2\2S\u0128\3\2\2\2U\u012b"+
		"\3\2\2\2W\u012d\3\2\2\2Y\u012f\3\2\2\2[\u0131\3\2\2\2]\u0133\3\2\2\2_"+
		"\u0136\3\2\2\2a\u0139\3\2\2\2c\u013b\3\2\2\2e\u013d\3\2\2\2g\u013f\3\2"+
		"\2\2i\u0141\3\2\2\2k\u0143\3\2\2\2m\u0145\3\2\2\2o\u0147\3\2\2\2q\u0149"+
		"\3\2\2\2s\u014c\3\2\2\2u\u014f\3\2\2\2w\u0157\3\2\2\2y\u0162\3\2\2\2{"+
		"\u0166\3\2\2\2}\u0174\3\2\2\2\177\u0080\7\60\2\2\u0080\4\3\2\2\2\u0081"+
		"\u0082\7]\2\2\u0082\u0083\7(\2\2\u0083\u0084\7_\2\2\u0084\6\3\2\2\2\u0085"+
		"\u0086\7/\2\2\u0086\u0087\7@\2\2\u0087\b\3\2\2\2\u0088\u0089\7-\2\2\u0089"+
		"\u008a\7-\2\2\u008a\n\3\2\2\2\u008b\u008c\7/\2\2\u008c\u008d\7/\2\2\u008d"+
		"\f\3\2\2\2\u008e\u008f\7,\2\2\u008f\16\3\2\2\2\u0090\u0091\7\61\2\2\u0091"+
		"\20\3\2\2\2\u0092\u0093\7\'\2\2\u0093\22\3\2\2\2\u0094\u0098\t\2\2\2\u0095"+
		"\u0097\t\3\2\2\u0096\u0095\3\2\2\2\u0097\u009a\3\2\2\2\u0098\u0096\3\2"+
		"\2\2\u0098\u0099\3\2\2\2\u0099\u009d\3\2\2\2\u009a\u0098\3\2\2\2\u009b"+
		"\u009d\7\62\2\2\u009c\u0094\3\2\2\2\u009c\u009b\3\2\2\2\u009d\24\3\2\2"+
		"\2\u009e\u00a1\5!\21\2\u009f\u00a1\5#\22\2\u00a0\u009e\3\2\2\2\u00a0\u009f"+
		"\3\2\2\2\u00a1\26\3\2\2\2\u00a2\u00ac\7$\2\2\u00a3\u00a4\7^\2\2\u00a4"+
		"\u00ab\7p\2\2\u00a5\u00a6\7^\2\2\u00a6\u00ab\7^\2\2\u00a7\u00a8\7^\2\2"+
		"\u00a8\u00ab\7$\2\2\u00a9\u00ab\13\2\2\2\u00aa\u00a3\3\2\2\2\u00aa\u00a5"+
		"\3\2\2\2\u00aa\u00a7\3\2\2\2\u00aa\u00a9\3\2\2\2\u00ab\u00ae\3\2\2\2\u00ac"+
		"\u00ad\3\2\2\2\u00ac\u00aa\3\2\2\2\u00ad\u00af\3\2\2\2\u00ae\u00ac\3\2"+
		"\2\2\u00af\u00b0\7$\2\2\u00b0\30\3\2\2\2\u00b1\u00b2\5%\23\2\u00b2\32"+
		"\3\2\2\2\u00b3\u00b4\7k\2\2\u00b4\u00b5\7p\2\2\u00b5\u00b6\7v\2\2\u00b6"+
		"\34\3\2\2\2\u00b7\u00b8\7d\2\2\u00b8\u00b9\7q\2\2\u00b9\u00ba\7q\2\2\u00ba"+
		"\u00bb\7n\2\2\u00bb\36\3\2\2\2\u00bc\u00bd\7u\2\2\u00bd\u00be\7v\2\2\u00be"+
		"\u00bf\7t\2\2\u00bf\u00c0\7k\2\2\u00c0\u00c1\7p\2\2\u00c1\u00c2\7i\2\2"+
		"\u00c2 \3\2\2\2\u00c3\u00c4\7v\2\2\u00c4\u00c5\7t\2\2\u00c5\u00c6\7w\2"+
		"\2\u00c6\u00c7\7g\2\2\u00c7\"\3\2\2\2\u00c8\u00c9\7h\2\2\u00c9\u00ca\7"+
		"c\2\2\u00ca\u00cb\7n\2\2\u00cb\u00cc\7u\2\2\u00cc\u00cd\7g\2\2\u00cd$"+
		"\3\2\2\2\u00ce\u00cf\7p\2\2\u00cf\u00d0\7w\2\2\u00d0\u00d1\7n\2\2\u00d1"+
		"\u00d2\7n\2\2\u00d2&\3\2\2\2\u00d3\u00d4\7x\2\2\u00d4\u00d5\7q\2\2\u00d5"+
		"\u00d6\7k\2\2\u00d6\u00d7\7f\2\2\u00d7(\3\2\2\2\u00d8\u00d9\7k\2\2\u00d9"+
		"\u00da\7h\2\2\u00da*\3\2\2\2\u00db\u00dc\7g\2\2\u00dc\u00dd\7n\2\2\u00dd"+
		"\u00de\7u\2\2\u00de\u00df\7g\2\2\u00df,\3\2\2\2\u00e0\u00e1\7h\2\2\u00e1"+
		"\u00e2\7q\2\2\u00e2\u00e3\7t\2\2\u00e3.\3\2\2\2\u00e4\u00e5\7y\2\2\u00e5"+
		"\u00e6\7j\2\2\u00e6\u00e7\7k\2\2\u00e7\u00e8\7n\2\2\u00e8\u00e9\7g\2\2"+
		"\u00e9\60\3\2\2\2\u00ea\u00eb\7d\2\2\u00eb\u00ec\7t\2\2\u00ec\u00ed\7"+
		"g\2\2\u00ed\u00ee\7c\2\2\u00ee\u00ef\7m\2\2\u00ef\62\3\2\2\2\u00f0\u00f1"+
		"\7e\2\2\u00f1\u00f2\7q\2\2\u00f2\u00f3\7p\2\2\u00f3\u00f4\7v\2\2\u00f4"+
		"\u00f5\7k\2\2\u00f5\u00f6\7p\2\2\u00f6\u00f7\7w\2\2\u00f7\u00f8\7g\2\2"+
		"\u00f8\64\3\2\2\2\u00f9\u00fa\7t\2\2\u00fa\u00fb\7g\2\2\u00fb\u00fc\7"+
		"v\2\2\u00fc\u00fd\7w\2\2\u00fd\u00fe\7t\2\2\u00fe\u00ff\7p\2\2\u00ff\66"+
		"\3\2\2\2\u0100\u0101\7v\2\2\u0101\u0102\7j\2\2\u0102\u0103\7k\2\2\u0103"+
		"\u0104\7u\2\2\u01048\3\2\2\2\u0105\u0106\7p\2\2\u0106\u0107\7g\2\2\u0107"+
		"\u0108\7y\2\2\u0108:\3\2\2\2\u0109\u010a\7e\2\2\u010a\u010b\7n\2\2\u010b"+
		"\u010c\7c\2\2\u010c\u010d\7u\2\2\u010d\u010e\7u\2\2\u010e<\3\2\2\2\u010f"+
		"\u0110\7*\2\2\u0110>\3\2\2\2\u0111\u0112\7+\2\2\u0112@\3\2\2\2\u0113\u0114"+
		"\7]\2\2\u0114B\3\2\2\2\u0115\u0116\7_\2\2\u0116D\3\2\2\2\u0117\u0118\7"+
		"}\2\2\u0118F\3\2\2\2\u0119\u011a\7\177\2\2\u011aH\3\2\2\2\u011b\u011c"+
		"\7>\2\2\u011cJ\3\2\2\2\u011d\u011e\7>\2\2\u011e\u011f\7?\2\2\u011fL\3"+
		"\2\2\2\u0120\u0121\7@\2\2\u0121N\3\2\2\2\u0122\u0123\7@\2\2\u0123\u0124"+
		"\7?\2\2\u0124P\3\2\2\2\u0125\u0126\7>\2\2\u0126\u0127\7>\2\2\u0127R\3"+
		"\2\2\2\u0128\u0129\7@\2\2\u0129\u012a\7@\2\2\u012aT\3\2\2\2\u012b\u012c"+
		"\7-\2\2\u012cV\3\2\2\2\u012d\u012e\7/\2\2\u012eX\3\2\2\2\u012f\u0130\7"+
		"(\2\2\u0130Z\3\2\2\2\u0131\u0132\7~\2\2\u0132\\\3\2\2\2\u0133\u0134\7"+
		"(\2\2\u0134\u0135\7(\2\2\u0135^\3\2\2\2\u0136\u0137\7~\2\2\u0137\u0138"+
		"\7~\2\2\u0138`\3\2\2\2\u0139\u013a\7`\2\2\u013ab\3\2\2\2\u013b\u013c\7"+
		"#\2\2\u013cd\3\2\2\2\u013d\u013e\7\u0080\2\2\u013ef\3\2\2\2\u013f\u0140"+
		"\7A\2\2\u0140h\3\2\2\2\u0141\u0142\7<\2\2\u0142j\3\2\2\2\u0143\u0144\7"+
		"=\2\2\u0144l\3\2\2\2\u0145\u0146\7.\2\2\u0146n\3\2\2\2\u0147\u0148\7?"+
		"\2\2\u0148p\3\2\2\2\u0149\u014a\7?\2\2\u014a\u014b\7?\2\2\u014br\3\2\2"+
		"\2\u014c\u014d\7#\2\2\u014d\u014e\7?\2\2\u014et\3\2\2\2\u014f\u0153\t"+
		"\4\2\2\u0150\u0152\t\5\2\2\u0151\u0150\3\2\2\2\u0152\u0155\3\2\2\2\u0153"+
		"\u0151\3\2\2\2\u0153\u0154\3\2\2\2\u0154v\3\2\2\2\u0155\u0153\3\2\2\2"+
		"\u0156\u0158\t\6\2\2\u0157\u0156\3\2\2\2\u0158\u0159\3\2\2\2\u0159\u0157"+
		"\3\2\2\2\u0159\u015a\3\2\2\2\u015a\u015b\3\2\2\2\u015b\u015c\b<\2\2\u015c"+
		"x\3\2\2\2\u015d\u015f\7\17\2\2\u015e\u0160\7\f\2\2\u015f\u015e\3\2\2\2"+
		"\u015f\u0160\3\2\2\2\u0160\u0163\3\2\2\2\u0161\u0163\7\f\2\2\u0162\u015d"+
		"\3\2\2\2\u0162\u0161\3\2\2\2\u0163\u0164\3\2\2\2\u0164\u0165\b=\2\2\u0165"+
		"z\3\2\2\2\u0166\u0167\7\61\2\2\u0167\u0168\7,\2\2\u0168\u016c\3\2\2\2"+
		"\u0169\u016b\13\2\2\2\u016a\u0169\3\2\2\2\u016b\u016e\3\2\2\2\u016c\u016d"+
		"\3\2\2\2\u016c\u016a\3\2\2\2\u016d\u016f\3\2\2\2\u016e\u016c\3\2\2\2\u016f"+
		"\u0170\7,\2\2\u0170\u0171\7\61\2\2\u0171\u0172\3\2\2\2\u0172\u0173\b>"+
		"\2\2\u0173|\3\2\2\2\u0174\u0175\7\61\2\2\u0175\u0176\7\61\2\2\u0176\u017a"+
		"\3\2\2\2\u0177\u0179\n\7\2\2\u0178\u0177\3\2\2\2\u0179\u017c\3\2\2\2\u017a"+
		"\u0178\3\2\2\2\u017a\u017b\3\2\2\2\u017b\u017d\3\2\2\2\u017c\u017a\3\2"+
		"\2\2\u017d\u017e\b?\2\2\u017e~\3\2\2\2\16\2\u0098\u009c\u00a0\u00aa\u00ac"+
		"\u0153\u0159\u015f\u0162\u016c\u017a\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}