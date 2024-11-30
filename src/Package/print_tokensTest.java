package Package;

import org.junit.Test;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.PrintStream;
import java.io.InputStream;

import static org.junit.Assert.*;

public class print_tokensTest {

    Printtokens pt = new Printtokens();

    // Redirect console output for testing
    private String captureOutput(Runnable task) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        PrintStream originalErr = System.err;
        System.setOut(new PrintStream(out));
        System.setErr(new PrintStream(out)); // Capture errors
        try {
            task.run();
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception
        } finally {
            System.setOut(originalOut);
            System.setErr(originalErr);
        }
        return out.toString().trim();
    }
    

    // Test Cases for openCharacterStream
    @Test
    public void testOpenCharacterStreamNull() {
        BufferedReader br = pt.open_character_stream(null);
        assertNotNull("BufferedReader should not be null for null input", br);
    }

    @Test
    public void testOpenCharacterStreamFilename() {
        String filename = "file.txt";
        try {
            BufferedReader br = pt.open_character_stream(filename);
            assertNotNull("BufferedReader should not be null for valid filename", br);
        } catch (Exception e) {
            assertTrue("Expected IOException for non-existing file", e instanceof IOException);
        }
    }

    // Test Cases for getChar
    @Test
    public void testGetChar() {
        String input = "abc";
        BufferedReader br = new BufferedReader(new StringReader(input));
        try {
            int ch = pt.get_char(br);
            assertEquals("Should return first character 'a'", 'a', ch);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    // Test Cases for ungetChar
    @Test
    public void testUngetChar() {
        String input = "abc";
        BufferedReader br = new BufferedReader(new StringReader(input));
        try {
            int ch = pt.get_char(br);
            char ungetResult = pt.unget_char(ch, br);
            assertEquals("Should return 0 after unget", 0, ungetResult);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    // Test Cases for openTokenStream
    @Test
    public void testOpenTokenStreamFilename() {
        BufferedReader br = pt.open_token_stream("test.txt");
        assertNotNull("BufferedReader should not be null for valid filename", br);
    }

    @Test
    public void testOpenTokenStreamNull() {
        BufferedReader br = pt.open_token_stream(null);
        assertNotNull("BufferedReader should not be null for null input", br);
    }

    // Test Cases for getToken
    @Test
    public void testGetTokenEmptyFile() {
        BufferedReader br = new BufferedReader(new StringReader(""));
        String token = pt.get_token(br);
        assertNull("Should return null for empty input", token);
    }

    @Test
    public void testGetTokenNewLines() {
        BufferedReader br = new BufferedReader(new StringReader("\n\n"));
        String token = pt.get_token(br);
        assertNull("Should return null for consecutive newlines", token);
    }

    @Test
    public void testGetTokenSpecialSymbol() {
        BufferedReader br = new BufferedReader(new StringReader("("));
        String token = pt.get_token(br);
        assertEquals("Should return '(' as token", "(", token);
    }

    @Test
    public void testGetTokenAlpha() {
        BufferedReader br = new BufferedReader(new StringReader("a"));
        String token = pt.get_token(br);
        assertEquals("Should return 'a' as token", "a", token);
    }

    @Test
    public void testGetTokenWord() {
        BufferedReader br = new BufferedReader(new StringReader("abc"));
        String token = pt.get_token(br);
        assertEquals("Should return 'abc' as token", "abc", token);
    }

    @Test
    public void testGetTokenWordWithParentheses() {
        BufferedReader br = new BufferedReader(new StringReader("abc)"));
        String token = pt.get_token(br);
        assertEquals("Should return 'abc' as token before ')' character", "abc", token);
    }

    @Test
    public void testGetTokenQuotedString() {
        BufferedReader br = new BufferedReader(new StringReader("\"abc\""));
        String token = pt.get_token(br);
        assertEquals("Should return quoted string", "\"abc\"", token);
    }

    @Test
    public void testGetTokenWithSemicolon() {
        BufferedReader br = new BufferedReader(new StringReader(";abc;"));
        String token = pt.get_token(br);
        assertEquals("Should return token including semicolon", ";abc;", token);
    }

    @Test
    public void testGetTokenHello() {
        BufferedReader br = new BufferedReader(new StringReader("hello"));
        String token = pt.get_token(br);
        assertEquals("Should return 'hello'", "hello", token);
    }

    // Test Cases for isTokenEnd
    @Test
    public void testIsToken() {
        assertTrue("Should return true for '0,-1'", Printtokens.is_token_end(0, -1));
        assertTrue("Should return true for '1,34'", Printtokens.is_token_end(1, 34));
        assertFalse("Should return false for '1,97'", Printtokens.is_token_end(1, 97));
        assertFalse("Should return true for '2,97'", Printtokens.is_token_end(2, 97));
        assertTrue("Should return true for '2,10'", Printtokens.is_token_end(2, 10));
        assertTrue("Should return true for '0,40'", Printtokens.is_token_end(0, 40));
        assertTrue("Should return true for '0,32'", Printtokens.is_token_end(0, 32));
        assertFalse("Should return false for '0,97'", Printtokens.is_token_end(0, 97));
    }

    // Test Cases for tokenType
    @Test
    public void testTokenTypeKeyword() {
        assertEquals("Should return keyword type", 1, Printtokens.token_type("and"));
        assertEquals("Should return keyword type", 1, Printtokens.token_type("or"));
        assertEquals("Should return keyword type", 1, Printtokens.token_type("if"));
        assertEquals("Should return keyword type", 1, Printtokens.token_type("xor"));
        assertEquals("Should return keyword type", 1, Printtokens.token_type("lambda"));
        assertEquals("Should return keyword type", 1, Printtokens.token_type("=>"));
    }

    @Test
    public void testTokenTypeSpecialSymbol() {
        assertEquals("Should return special symbol type", 2, Printtokens.token_type("("));
        assertEquals("Should return special symbol type", 2, Printtokens.token_type(")"));
        assertEquals("Should return special symbol type", 2, Printtokens.token_type("["));
        assertEquals("Should return special symbol type", 2, Printtokens.token_type("]"));
        assertEquals("Should return special symbol type", 2, Printtokens.token_type("'"));
        assertEquals("Should return special symbol type", 2, Printtokens.token_type("`"));
        assertEquals("Should return special symbol type", 2, Printtokens.token_type(","));
    }

    @Test
    public void testTokenTypeIdentifier() {
        assertEquals("Should return identifier type", 3, Printtokens.token_type("variableName"));
        assertEquals("Should return identifier type", 3, Printtokens.token_type("a"));
        assertEquals("Should return identifier type", 3, Printtokens.token_type("aa"));
        assertEquals("Should return identifier type", 3, Printtokens.token_type("a1"));
        assertEquals("Should return identifier type", 3, Printtokens.token_type("a2"));
    }

    @Test
    public void testTokenTypeNumberConstant() {
        assertEquals("Should return number constant type", 41, Printtokens.token_type("123"));
        assertEquals("Should return number constant type", 41, Printtokens.token_type("1"));
        assertEquals("Should return number constant type", 41, Printtokens.token_type("321"));
    }

    @Test
    public void testTokenTypeStringConstant() {
        assertEquals("Should return string constant type", 42, Printtokens.token_type("\"HelloWorld\""));
        assertEquals("Should return string constant type", 42, Printtokens.token_type("\"asd\""));
        assertEquals("Should return string constant type", 42, Printtokens.token_type("\"123\""));
    }

    @Test
    public void testTokenTypeCharConstant() {
        assertEquals("Should return char constant type", 43, Printtokens.token_type("#a"));
        assertEquals("Should return char constant type", 43, Printtokens.token_type("#b"));
    }

    @Test
    public void testTokenTypeComment() {
        assertEquals("Should return comment type", 5, Printtokens.token_type(";comment"));
    }

    @Test
    public void testTokenTypeError() {
        assertEquals("Should return error type", 0, Printtokens.token_type("*^&"));
    }

    // Test Cases for printToken
    @Test
    public void testPrintTokenError() {
        String result = captureOutput(() -> pt.print_token("*^&"));
        assertEquals("error,\"*^&\".", result);
    }

    @Test
    public void testPrintTokenKeyword() {
        String result = captureOutput(() -> pt.print_token("if"));
        assertEquals("keyword,\"if\".", result);
    }

    @Test
    public void testPrintTokenSpecSymbol() {
        String result = captureOutput(() -> pt.print_token("("));
        assertEquals("lparen.", result);
    }

    @Test
    public void testPrintTokenIdentifier() {
        String result = captureOutput(() -> pt.print_token("variableName"));
        assertEquals("identifier,\"variableName\".", result);
    }

    @Test
    public void testPrintTokenNumeric() {
        String result = captureOutput(() -> pt.print_token("123"));
        assertEquals("numeric,123.", result);
    }

    @Test
    public void testPrintTokenString() {
        String result = captureOutput(() -> pt.print_token("\"HelloWorld\""));
        assertEquals("string,\"HelloWorld\".", result);
    }

    @Test
    public void testPrintTokenCharConstant() {
        String result = captureOutput(() -> pt.print_token("#a"));
        assertEquals("character,\'a\'.", result);
    }

    @Test
    public void testPrintTokenComment() {
        String result = captureOutput(() -> pt.print_token(";comment"));
        assertEquals("comment,\";comment\".", result);
    }

    // Test Cases for isComment
    @Test
    public void testIsCommentTrue() {
        assertTrue("Should return true for comment", Printtokens.is_comment(";comment"));
    }

    @Test
    public void testIsCommentFalse() {
        assertFalse("Should return false for non-comment", Printtokens.is_comment("x"));
    }

    // Test Cases for isKeyword
    @Test
    public void testIsKeywordTrue() {
        assertTrue("Should return true for keyword 'and'", Printtokens.is_keyword("and"));
    }

    @Test
    public void testIsKeywordFalse() {
        assertFalse("Should return false for non-keyword", Printtokens.is_keyword("hello"));
    }

    // Test Cases for isCharConstant
    @Test
    public void testIsCharConstantTrue() {
        assertTrue("Should return true for valid char constant", Printtokens.is_char_constant("#a"));
    }

    @Test
    public void testIsCharConstantFalse() {
        assertFalse("Should return false for invalid char constant", Printtokens.is_char_constant("abc"));
    }

    // Test Cases for isNumConstant
    @Test
    public void testIsNumConstantFalseAlpha() {
        assertFalse("Should return false for non-numeric constant", Printtokens.is_num_constant("abc"));
        assertFalse("Should return false for non-numeric constant", Printtokens.is_num_constant("12a"));
    }

    @Test
    public void testIsNumConstantTrueNumeric() {
        assertTrue("Should return true for numeric constant", Printtokens.is_num_constant("123"));
        assertTrue("Should return true for numeric constant", Printtokens.is_num_constant("1"));
    }

    // Test Cases for isStrConstant
    @Test
    public void testIsStrConstantTrue() {
        assertTrue("Should return true for string constant", Printtokens.is_str_constant("\"a\""));
        assertTrue("Should return true for string constant", Printtokens.is_str_constant("\"asd\""));
        assertTrue("Should return true for string constant", Printtokens.is_str_constant("\"123\""));
        assertTrue("Should return true for string constant", Printtokens.is_str_constant("\"HelloWorld\""));
    }

    @Test
    public void testIsStrConstantFalse() {
        assertFalse("Should return false for invalid string", Printtokens.is_str_constant("\""));
        assertFalse("Should return false for invalid string", Printtokens.is_str_constant("abc"));
        assertFalse("Should return false for invalid string", Printtokens.is_str_constant("\"\""));
    }

    // Test Cases for isIdentifier
    @Test
    public void testIsIdentifierTrue() {
        assertTrue("Should return true for identifier", Printtokens.is_identifier("output"));
    }

    @Test
    public void testIsIdentifierFalse() {
        assertFalse("Should return false for invalid identifier", Printtokens.is_identifier("1output"));
    }

    // Test Cases for printSpecSymbol
    @Test
    public void testPrintSpecSymbol() {
        Printtokens.print_spec_symbol("("); // No assert, ensuring it doesn't throw
    }

    // Test Cases for isSpecSymbol
    @Test
    public void testIsSpecSymbolTrue() {
        assertTrue("Should return true for special symbol '('", Printtokens.is_spec_symbol('('));
    }

    @Test
    public void testIsSpecSymbolFalse() {
        assertFalse("Should return false for non-special symbol 'x'", Printtokens.is_spec_symbol('x'));
        // assertFalse("Should return false for non-special symbol \"HelloWorld\"",
        // Printtokens.is_spec_symbol("\"HelloWorld\""));
    }

    // Test cases for entire program
    // Test cases for entire program
    @Test
    public void testMain1() {
        String result = captureOutput(() -> {
            try {
                FileWriter fw = new FileWriter("file.txt");
                fw.write("");
                fw.close(); // Empty file
                Printtokens.main(new String[] { "file.txt" });
            } catch (IOException e) {
                fail("File creation failed.");
            }
        });
        assertEquals("Expected no output for an empty file", "", result);
    }

    @Test
    public void testMain2() {
        String result = captureOutput(() -> {
            InputStream stdin = System.in;
            try {
                System.setIn(new ByteArrayInputStream(new byte[0])); // Simulate EOF for stdin
                Printtokens.main(new String[] { "" });
            } finally {
                System.setIn(stdin);
            }
        });
        assertEquals("Expected no output for empty stdin", "", result);
    }

    @Test
    public void testMain3() {
        String result = captureOutput(() -> {
            try {
                FileWriter fw = new FileWriter("file.txt");
                fw.write("(");
                fw.close();
                Printtokens.main(new String[] { "file.txt" });
            } catch (IOException e) {
                fail("File creation failed.");
            }
        });
        assertEquals("Expected output for '('", "lparen.", result);
    }

    @Test
    public void testMain4() {
        String result = captureOutput(() -> {
            try {
                FileWriter fw = new FileWriter("file.txt");
                fw.write("if lambda");
                fw.close();
                Printtokens.main(new String[] { "file.txt" });
            } catch (IOException e) {
                fail("File creation failed.");
            }
        });
        assertEquals("Expected output for 'if lambda'", "keyword,\"if\".\nkeyword,\"lambda\".", result);
    }

    @Test
public void testMain5() {
    String result = captureOutput(() -> {
        
        InputStream stdin = System.in;
        try {
            System.setIn(new ByteArrayInputStream("abc".getBytes()));
            Printtokens.main(new String[] { "" });
        } finally {
            System.setIn(stdin);
        }
    });
    // System.out.println("Captured Output: " + result); // Debug
    assertEquals("Expected output for 'abc'", "identifier,\"abc\".", result);
}

    @Test
    public void testMain6() {
        String result = captureOutput(() -> {
            try {
                FileWriter fw = new FileWriter("file.txt");
                fw.write("()");
                fw.close();
                Printtokens.main(new String[] { "file.txt" });
            } catch (IOException e) {
                fail("File creation failed.");
            }
        });
        assertEquals("Expected output for '()'", "lparen.\nrparen.", result);
    }

    @Test
    public void testMain7() {
        String result = captureOutput(() -> {
            try {
                FileWriter fw = new FileWriter("file.txt");
                fw.write("\"HelloWorld\"");
                fw.close();
                Printtokens.main(new String[] { "file.txt" });
            } catch (IOException e) {
                fail("File creation failed.");
            }
        });
        assertEquals("Expected output for '\"HelloWorld\"'", "string,\"HelloWorld\".", result);
    }

    @Test
    public void testMain8() {
        String result = captureOutput(() -> {
            InputStream stdin = System.in;
            try {
                System.setIn(new ByteArrayInputStream("123".getBytes()));
                Printtokens.main(new String[] { "" });
            } finally {
                System.setIn(stdin);
            }
        });
        assertEquals("Expected output for '123'", "numeric,123.", result);
    }

    @Test
    public void testMain9() {
        String result = captureOutput(() -> {
            InputStream stdin = System.in;
            try {
                System.setIn(new ByteArrayInputStream(";comment".getBytes()));
                Printtokens.main(new String[] { "" });
            } finally {
                System.setIn(stdin);
            }
        });
        assertEquals("Expected output for ';comment'", "comment,\";comment\".", result);
    }

    @Test
    public void testMain10() {
        String result = captureOutput(() -> {
            InputStream stdin = System.in;
            try {
                System.setIn(new ByteArrayInputStream("#a".getBytes()));
                Printtokens.main(new String[] { "" });
            } finally {
                System.setIn(stdin);
            }
        });
        assertEquals("Expected output for '#a'", "character,'a'.", result);
    }

    @Test
    public void testMain11() {
        String result = captureOutput(() -> {
            InputStream stdin = System.in;
            try {
                String mixedInput = "if ( x 123 \"HelloWorld\" ;comment\nlambda )";
                System.setIn(new ByteArrayInputStream(mixedInput.getBytes()));
                Printtokens.main(new String[] { "" });
            } finally {
                System.setIn(stdin);
            }
        });

        String expected = String.join("\n",
                "keyword,\"if\".",
                "lparen.",
                "identifier,\"x\".",
                "numeric,123.",
                "string,\"HelloWorld\".",
                "comment,\";comment\".",
                "keyword,\"lambda\".",
                "rparen.");
        assertEquals("Expected output for mixed input", expected, result);
    }

    @Test
    public void testMain12() {
        String result = captureOutput(() -> {
            InputStream stdin = System.in;
            try {
                String mixedInput = "and`and\nj\n112A)";
                // System.out.println("Redirecting System.in to: " + mixedInput);
                System.setIn(new ByteArrayInputStream(mixedInput.getBytes()));
                Printtokens.main(new String[] { "" });
            } finally {
                System.setIn(stdin);
            }
        });

        String expected = String.join("\n",
                "keyword,\"and\".",
                "bquote.",
                "keyword,\"and\".",
                "identifier,\"j\".",
                "error,\"112A\".");
        assertEquals("Expected output for mixed input", expected, result);
    }

    @Test
    public void testMain13() throws Exception {
        // Write the test.txt file
        String testFileContent = String.join("\n",
            "; this is a comment",
            "if ( x 123 \"hello\" ;another comment",
            "lambda )",
            "#c \"string\" 456 ` quote , comma ;comment",
            "unknown_token",
            "123abc",
            "\"unclosed_string",
            "and [ ] ( ) ;end_comment"
        );

        String result = captureOutput(() -> {
            try {
                FileWriter fw = new FileWriter("testMain13.txt");
                fw.write(testFileContent);
                fw.close();
                Printtokens.main(new String[] { "testMain13.txt" });
            } catch (IOException e) {
                fail("File creation failed.");
            }
        });
        // Expected output
        String expectedOutput = String.join("\n",
            "comment,\"; this is a comment\".",
            "keyword,\"if\".",
            "lparen.",
            "identifier,\"x\".",
            "numeric,123.",
            "string,\"hello\".",
            "comment,\";another comment\".",
            "keyword,\"lambda\".",
            "rparen.",
            "character,\'c\'.",
            "string,\"string with spaces\".",
            "numeric,456.",
            "bquote.",
            "comma.",
            "comment,\";comment\".",
            "error,\"unknown_token\".",
            "error,\"123abc\".",
            "error,\"\"unclosed_string\".",
            "keyword,\"and\".",
            "lsquare.",
            "rsquare.",
            "lparen.",
            "rparen.",
            "comment,\";end_comment\"."
        );

        // Compare actual and expected output
        assertEquals("The output of Printtokens for test.txt should match the expected output", expectedOutput, result);
    }

}