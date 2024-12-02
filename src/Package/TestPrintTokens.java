package Package;


import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.*;
import java.text.ListFormat.Style;

import javax.accessibility.AccessibleAttributeSequence;

import org.junit.jupiter.api.Test;

public class TestPrintTokens {

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
    
    @Test
    public void testOpenCharStreamNull() 
    {
        Printtokens instance = new Printtokens();
        BufferedReader br = instance.open_character_stream(null); //call to function being tested
    
    
        assertNotNull(br); 
        assertTrue(br.getClass().getName().equals("java.io.BufferedReader"));
        assertFalse(br.getClass().getName().contains("FileReader"));
    }

    @Test
    public void testOpenCharStreamFileName()
    {
        
        File tempFile = new File("test", ".txt");
        tempFile.deleteOnExit();

        try{
            FileWriter writer = new FileWriter(tempFile);

            writer.write("Hello World"); 
            writer.close();

            Printtokens instance = new Printtokens();
            
            BufferedReader br = instance.open_character_stream(tempFile.getAbsolutePath());

            
            assertNotNull(br);
            assertTrue(br.getClass().getName().equals("java.io.BufferedReader"));
            
            String firstLine = br.readLine();
            assertEquals(firstLine, "Hello World");

        }
        catch(Exception e){};
    }

    /////////////////////////////////////////////////////////////////////////////////////
    @Test
    public void testGetChar() throws IOException 
    {
        Printtokens instance = new Printtokens();
        File tempFile = File.createTempFile("temp", ".txt");
        tempFile.deleteOnExit();

        try (FileWriter writer = new FileWriter(tempFile)) 
        {
            writer.write("12345");
        }

        try (BufferedReader br = instance.open_character_stream(tempFile.getAbsolutePath())) 
        {
            int ret = instance.get_char(br);

            assertNotNull(ret);
            assertEquals('1', ret);
        }
    }

    /////////////////////////////////////////////////////////////////////////////
    @Test
    public void testUngetChar() throws IOException
    {
        Printtokens instance = new Printtokens();
        File tempFile = File.createTempFile("temp", ".txt");
        tempFile.deleteOnExit();

        try(Writer writer = new FileWriter(tempFile);){
            writer.write("1*");
            writer.close();
        }

            try(BufferedReader br = instance.open_character_stream(tempFile.getAbsolutePath());){
            int one = instance.get_char(br);
            int star = instance.get_char(br);

            instance.unget_char(star,br);

            int star2 = instance.get_char(br);

            assertEquals(one, '1');
            assertEquals(star, '*');
            assertEquals(star2, '*');
        }
    }

    /////////////////////////////////////////////////////////////////////////////
    @Test
    public void testOpenTokenStream() throws IOException
    {
        Printtokens instance = new Printtokens();
        File tempFile = File.createTempFile("temp", ".txt");
        tempFile.deleteOnExit();

        try(Writer writer = new FileWriter(tempFile);){
            writer.write("1*");
            writer.close();
        }
        //path one
        BufferedReader one = instance.open_token_stream(null);
        //path2
        BufferedReader two = instance.open_token_stream(tempFile.getAbsolutePath());

        assertNotNull(one);
        assertNotNull(two);

        assertTrue(one.getClass().getName().equals("java.io.BufferedReader"));
        assertFalse(one.getClass().getName().contains("FileReader"));
        
        assertTrue(two.getClass().getName().equals("java.io.BufferedReader")); 
        String content = two.readLine();
        assertEquals("1*", content);

    }

     /////////////////////////////////////////////////////////////////////////////
     @Test
     public void testGetToken() throws IOException
     {
        Printtokens instance = new Printtokens();
        File tempFile = File.createTempFile("temp", ".txt");
        tempFile.deleteOnExit();

        BufferedReader one = null;
        String one1 = instance.get_token(one);
        assertNull(one1);

        //file with no token
        try(Writer writer = new FileWriter(tempFile);){
            writer.write("\n\n");
            writer.close();
        }
        BufferedReader two = instance.open_character_stream(tempFile.getAbsolutePath());
        assertTrue(two.getClass().getName().equals("java.io.BufferedReader")); 
        String two2 = instance.get_token(two);
        assertEquals(two2, null);

        //file with spec_symbol
        try (Writer writer = new FileWriter(tempFile, false)) {
            writer.write("(");
            writer.close();
        }
        BufferedReader three = instance.open_character_stream(tempFile.getAbsolutePath());
        assertTrue(three.getClass().getName().equals("java.io.BufferedReader")); 
        String three3 = three.readLine();
        assertEquals(three3, "(");
        three3 = instance.get_token(three);
        assertEquals(three3, null);

        //single char
        try (Writer writer = new FileWriter(tempFile, false)) {
            writer.write("a");
            writer.close();
        }
        BufferedReader four = instance.open_character_stream(tempFile.getAbsolutePath());
        assertTrue(four.getClass().getName().equals("java.io.BufferedReader"));
        String four4 = instance.get_token(four);
        assertEquals(four4, "a");

        //multiple char
        try (Writer writer = new FileWriter(tempFile, false)) {
            writer.write("abc");
            writer.close();
        }
        BufferedReader five = instance.open_character_stream(tempFile.getAbsolutePath());
        assertTrue(five.getClass().getName().equals("java.io.BufferedReader"));
        String five5 = instance.get_token(five);
        assertEquals(five5, "abc");

        //ends in spec_symbol
        try (Writer writer = new FileWriter(tempFile, false)) {
            writer.write("abc)");
            writer.close();
        }
        BufferedReader six = instance.open_character_stream(tempFile.getAbsolutePath());
        assertTrue(six.getClass().getName().equals("java.io.BufferedReader"));
        String six6 = instance.get_token(six);
        assertEquals(six6, "abc");

        //String
        try (Writer writer = new FileWriter(tempFile, false)) {
            writer.write("\"abc\"");
            writer.close();
        }
        BufferedReader seven = instance.open_character_stream(tempFile.getAbsolutePath());
        assertTrue(seven.getClass().getName().equals("java.io.BufferedReader"));
        String seven7 = instance.get_token(seven);
        assertEquals("\"abc\"", seven7);
        
        //remove semicolon
        try (Writer writer = new FileWriter(tempFile, false)) {
            writer.write("abc;");
            writer.close();
        }
        BufferedReader eight = instance.open_character_stream(tempFile.getAbsolutePath());
        assertTrue(eight.getClass().getName().equals("java.io.BufferedReader"));
        String eight8 = instance.get_token(eight);
        assertEquals(eight8, "abc");

        //normal
        try (Writer writer = new FileWriter(tempFile, false)) {
            writer.write("Hello");
            writer.close();
        }
        BufferedReader nine = instance.open_character_stream(tempFile.getAbsolutePath());
        assertTrue(nine.getClass().getName().equals("java.io.BufferedReader"));
        String nine9 = instance.get_token(nine);
        assertEquals(nine9, "Hello");
     }

      /////////////////////////////////////////////////////////////////////////////
    @Test
    public void testIsTokenEnd() throws IOException
    {
        int x,y;

        x=0;
        y=-1;
        assertTrue(Printtokens.is_token_end(x, y));

        x=1;
        y=34;
        assertTrue(Printtokens.is_token_end(x, y));

        x=1;
        y=97;
        assertFalse(Printtokens.is_token_end(x, y));

        x=2;
        y=97;
        assertFalse(Printtokens.is_token_end(x, y));

        x=2;
        y=10;
        assertTrue(Printtokens.is_token_end(x, y));

        x=0;
        y=40;
        assertTrue(Printtokens.is_token_end(x, y));

        x=0;
        y=32;
        assertTrue(Printtokens.is_token_end(x, y));

        x=0;
        y=97;
        assertFalse(Printtokens.is_token_end(x, y));
    }

     /////////////////////////////////////////////////////////////////////////////
     @Test
     public void testTokenType() throws IOException
     {
        String tok;
        
        tok = "if";
        assertEquals(Printtokens.keyword, Printtokens.token_type(tok));

        tok = "(";
        assertEquals(Printtokens.spec_symbol, Printtokens.token_type(tok));

        tok = "variableName";
        assertEquals(Printtokens.identifier, Printtokens.token_type(tok));

        tok = "123";
        assertEquals(Printtokens.num_constant, Printtokens.token_type(tok));

        tok = "\"Hello\"";
        assertEquals(Printtokens.str_constant, Printtokens.token_type(tok));

        tok = "#a";
        assertEquals(Printtokens.char_constant, Printtokens.token_type(tok));

        tok = ";comment";
        assertEquals(Printtokens.comment, Printtokens.token_type(tok));

        tok = " ";
        assertEquals(Printtokens.error, Printtokens.token_type(tok));


     }

      /////////////////////////////////////////////////////////////////////////////
    @Test
    public void testPrintToken() throws IOException
    {
        Printtokens instance = new Printtokens();
        String tok;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        
        tok = " ";
        instance.print_token(tok);
        assertEquals("error,\" \".\n", outContent.toString());
        System.setOut(originalOut);
        outContent.reset(); 
        System.setOut(new PrintStream(outContent));

        tok = "if";
        instance.print_token(tok);
        assertEquals("keyword,\"if\".\n", outContent.toString());
        System.setOut(originalOut);
        outContent.reset(); 
        System.setOut(new PrintStream(outContent));

        tok = "(";
        instance.print_token(tok);
        assertEquals("lparen.\n", outContent.toString());
        System.setOut(originalOut);
        outContent.reset(); 
        System.setOut(new PrintStream(outContent));

        tok = "variableName";
        instance.print_token(tok);
        assertEquals("identifier,\"variableName\".\n", outContent.toString());
        System.setOut(originalOut);
        outContent.reset(); 
        System.setOut(new PrintStream(outContent));

        tok = "123";
        instance.print_token(tok);
        assertEquals("numeric,123.\n", outContent.toString());
        System.setOut(originalOut);
        outContent.reset(); 
        System.setOut(new PrintStream(outContent));

        tok = "\"Hello\"";
        instance.print_token(tok);
        assertEquals("string,\"Hello\".\n", outContent.toString());
        System.setOut(originalOut);
        outContent.reset(); 
        System.setOut(new PrintStream(outContent));

        tok = "#a";
        instance.print_token(tok);
        assertEquals("character,\'a\'.\n", outContent.toString());
        System.setOut(originalOut);
        outContent.reset(); 
        System.setOut(new PrintStream(outContent));

        tok = ";comment";
        instance.print_token(tok);
        assertEquals("comment,\";comment\".\n", outContent.toString());
        System.setOut(originalOut);
        outContent.reset(); 
        System.setOut(new PrintStream(outContent));
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
                "error,\"112A\".",
                "rparen.");
        assertEquals("Expected output for mixed input", expected, result);
    }

    @Test
    public void testMain13() throws Exception {
        // Write the test.txt file
        String testFileContent = String.join("\n",
            "; this is a comment",
            "if ( x 123 \"hello\" ;another comment",
            "lambda )",
            "#c \"string with spaces\" 456 ` quote , comma ;comment",
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
            "identifier,\"quote\".",
            "comma.",
            "identifier,\"comma\".",
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