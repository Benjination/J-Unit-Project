import org.junit.jupiter.api.*;
import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

public class PrintTokensTest {

    Printtokens pt;

    @BeforeEach
    void setUp() {
        pt = new Printtokens();
    }

    // Individual Method Tests
    // // Test paths for openCharacterStream
    // @Test
    // void testOpenCharacterStream_NullInput() {
    //     assertEquals("br at previous location", pt.openCharacterStream(null)); // Path 1, 2, 4
    // }

    // @Test
    // void testOpenCharacterStream_FileNameInput() {
    //     assertEquals("br at filename line 1", pt.openCharacterStream("fname")); // Path 1, 3, 4
    // }

    // Test paths for getChar
    @Test
    void testGetChar_ValidInput() {
        BufferedReader br = new BufferedReader(new StringReader("test"));
        assertNotNull(pt.getChar(br)); // Path 1, 2
    }

    // Test paths for ungetChar
    @Test
    void testUngetChar_ValidInput() {
        BufferedReader br = new BufferedReader(new StringReader("test"));
        assertEquals(0, pt.ungetChar(br)); // Path 1, 2
    }

    // // Test paths for openTokenStream
    // @Test
    // void testOpenTokenStream_FileNameInput() {
    //     assertEquals("br at file name line 1", pt.openTokenStream("fname")); // Path 1, 2, 4
    // }

    // @Test
    // void testOpenTokenStream_NullInput() {
    //     assertEquals("br at previous location", pt.openTokenStream(null)); // Path 1, 3, 4
    // }

    // Test paths for getToken
    @Test
    void testGetToken_EmptyFile() {
        BufferedReader br = new BufferedReader(new StringReader(""));
        assertNull(pt.getToken(br)); // Path 1
    }

    @Test
    void testGetToken_Newlines() {
        BufferedReader br = new BufferedReader(new StringReader("\n\n"));
        assertNull(pt.getToken(br)); // Path 1, 2, 3, 2, 4
    }

    @Test
    void testGetToken_SingleCharacter() {
        BufferedReader br = new BufferedReader(new StringReader("*"));
        assertEquals("*", pt.getToken(br)); // Path 1, 2, 4, 5
    }

    @Test
    void testGetToken_SimpleAlphaEOF() {
        BufferedReader br = new BufferedReader(new StringReader("a"));
        assertEquals("a", pt.getToken(br)); // Path 1, 2, 4, 5, 6, 7, 8, 9
    }

    @Test
    void testGetToken_AlphaString() {
        BufferedReader br = new BufferedReader(new StringReader("abc"));
        assertEquals("abc", pt.getToken(br)); // Path 1, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12, 11, 13
    }

    @Test
    void testGetToken_AlphaWithSpecialChar() {
        BufferedReader br = new BufferedReader(new StringReader("abc*"));
        assertEquals("abc", pt.getToken(br)); // Path 1, 2, 4, 5, 6, 7, 8, 9, 10, 11, 13, 14, 15
    }

    @Test
    void testGetToken_QuotedString() {
        BufferedReader br = new BufferedReader(new StringReader("\"abc\""));
        assertEquals("\"abc\"", pt.getToken(br)); // Path 1, 2, 4, 5, 6, 7, 8, 9, 10, 11, 13, 14, 15, 16, 17
    }

    @Test
    void testGetToken_Comment() {
        BufferedReader br = new BufferedReader(new StringReader(";abc;"));
        assertEquals(";abc/", pt.getToken(br)); // Path 1, 2, 4, 5, 6, 7, 8, 9, 10, 11, 13, 14, 15, 16, 17, 18, 19
    }

    @Test
    void testGetToken_HelloWorld() {
        BufferedReader br = new BufferedReader(new StringReader("hello world"));
        assertEquals("hello world", pt.getToken(br)); // Path 1, 2, 4, 5, 6, 7, 8, 9, 10, 11, 13, 14, 15, 16, 17, 18, 19, 20, 21
    }

    // Test paths for isTokenEnd
    @Test
    void testIsTokenEnd_Case1() {
        assertTrue(pt.isTokenEnd("0,-1")); // Path 1
    }

    @Test
    void testIsTokenEnd_Case2() {
        assertTrue(pt.isTokenEnd("1,34")); // Path 1, 2, 3, 4
    }

    @Test
    void testIsTokenEnd_Case3() {
        assertFalse(pt.isTokenEnd("1,97")); // Path 1, 2, 3, 5
    }

    @Test
    void testIsTokenEnd_Case4() {
        assertFalse(pt.isTokenEnd("2,97")); // Path 1, 2, 6, 7, 9
    }

    @Test
    void testIsTokenEnd_Case5() {
        assertTrue(pt.isTokenEnd("2,10")); // Path 1, 2, 6, 7, 8
    }

    @Test
    void testIsTokenEnd_Case6() {
        assertTrue(pt.isTokenEnd("0,43")); // Path 1, 2, 6, 10
    }

    @Test
    void testIsTokenEnd_Case7() {
        assertTrue(pt.isTokenEnd("0,32")); // Path 1, 2, 6, 11, 12
    }

    @Test
    void testIsTokenEnd_Case8() {
        assertFalse(pt.isTokenEnd("0,97")); // Path 1, 2, 6, 10, 11, 13
    }

    // Test paths for tokenType
    @Test
    void testTokenType_Keyword() {
        assertEquals("keyword", pt.tokenType("if")); // Path 1, 2
    }

    @Test
    void testTokenType_SpecialSymbol() {
        assertEquals("spec_symbol", pt.tokenType("(")); // Path 1, 3, 4
    }

    @Test
    void testTokenType_Identifier() {
        assertEquals("identifier", pt.tokenType("variableName")); // Path 1, 3, 5, 6
    }

    @Test
    void testTokenType_NumericConstant() {
        assertEquals("num_constant", pt.tokenType("123")); // Path 1, 3, 5, 7, 8
    }

    @Test
    void testTokenType_StringConstant() {
        assertEquals("str_constant", pt.tokenType("\"Hello World\"")); // Path 1, 3, 5, 7, 9, 10
    }

    @Test
    void testTokenType_CharConstant() {
        assertEquals("char_constant", pt.tokenType("#a")); // Path 1, 3, 5, 7, 9, 11, 12
    }

    @Test
    void testTokenType_Comment() {
        assertEquals("comment", pt.tokenType(";comment")); // Path 1, 3, 5, 7, 9, 11, 13, 14
    }

    @Test
    void testTokenType_Error() {
        assertEquals("error", pt.tokenType("*^&")); // Path 1, 3, 5, 7, 9, 11, 13, 15
    }

    // Main Test Paths
    @Test
    void testMain_Path1() throws Exception {
        String inputFile = "file.txt";
        String input = ""; // Simulate empty file
        BufferedReader br = new BufferedReader(new StringReader(input));
        pt.openTokenStream(inputFile); // Simulates open_character_stream[1, 2, 4]
        assertNull(pt.getToken(br));  // Simulates get_token[1]
    }

    @Test
    void testMain_Path2() throws Exception {
        String inputFile = "";
        BufferedReader br = new BufferedReader(new StringReader(""));
        pt.openTokenStream(inputFile); // Simulates open_character_stream[1, 3, 4]
        assertNull(pt.getToken(br));  // Simulates get_token[1]
    }

    @Test
    void testMain_Path3() throws Exception {
        String inputFile = "file.txt";
        BufferedReader br = new BufferedReader(new StringReader("("));
        pt.openTokenStream(inputFile); // Simulates open_character_stream[1, 2, 4]
        assertEquals("lparen", pt.printSpecSymbol(pt.getToken(br))); // Tests get_token and spec_symbol
    }

    @Test
    void testMain_Path4() throws Exception {
        String inputFile = "file.txt";
        BufferedReader br = new BufferedReader(new StringReader("if lambda"));
        pt.openTokenStream(inputFile); // Simulates open_character_stream[1, 2, 4]
        assertEquals("keyword, \"if\".", pt.printToken(pt.getToken(br)));
        assertEquals("keyword, \"lambda\".", pt.printToken(pt.getToken(br)));
    }

    @Test
    void testMain_Path5() throws Exception {
        String inputFile = "";
        BufferedReader br = new BufferedReader(new StringReader("abc;"));
        pt.openTokenStream(inputFile); // Simulates open_character_stream[1, 3, 4]
        assertEquals("abc/", pt.getToken(br)); // Tests handling of semicolon in get_token
    }

    @Test
    void testMain_Path6() throws Exception {
        String inputFile = "file.txt";
        BufferedReader br = new BufferedReader(new StringReader("("));
        pt.openTokenStream(inputFile); // Simulates open_character_stream[1, 2, 4]
        assertEquals("lparen.", pt.printToken(pt.getToken(br))); // Tests tokenType and printToken
    }

    @Test
    void testMain_Path7() throws Exception {
        String inputFile = "file.txt";
        BufferedReader br = new BufferedReader(new StringReader("\"hello world\""));
        pt.openTokenStream(inputFile); // Simulates open_character_stream[1, 2, 4]
        assertEquals("string, \"hello world\".", pt.printToken(pt.getToken(br))); // Tests string constant
    }

    @Test
    void testMain_Path8() throws Exception {
        String inputFile = "";
        BufferedReader br = new BufferedReader(new StringReader("123"));
        pt.openTokenStream(inputFile); // Simulates open_character_stream[1, 2, 4]
        assertEquals("numeric, \"123\".", pt.printToken(pt.getToken(br))); // Tests numeric constant
    }

    @Test
    void testMain_Path9() throws Exception {
        String inputFile = "";
        BufferedReader br = new BufferedReader(new StringReader(";comment"));
        pt.openTokenStream(inputFile); // Simulates open_character_stream[1, 2, 4]
        assertEquals("comment, \";comment\".", pt.printToken(pt.getToken(br))); // Tests comment token
    }

    @Test
    void testMain_Path10() throws Exception {
        String inputFile = "";
        BufferedReader br = new BufferedReader(new StringReader("#a"));
        pt.openTokenStream(inputFile); // Simulates open_character_stream[1, 2, 4]
        assertEquals("character, \"a\".", pt.printToken(pt.getToken(br))); // Tests character constant
    }
}