/*
 * Compiler.java
 *
 * A starting place for the unamed language compiler for CSC 435/535
 *
 */

import ast.ASTNode;
import ast.DotPrinter;
import ast.PrettyPrinter;
import org.antlr.runtime.*;

import java.io.*;
import java.util.Scanner;

public class Compiler {
    public static void main(String[] args) throws Exception {
        ANTLRInputStream input;

        if (args.length == 0) {
            System.out.println("Usage: Test filename.ul");
            return;
        }

        String fileName = args[0];
        input = new ANTLRInputStream(new FileInputStream(fileName));

        // The name of the grammar here is "UnnamedLanguage",
        // so ANTLR generates UnnamedLanguageLexer and UnnamedLanguageParser
        UnnamedLanguageLexer lexer = new UnnamedLanguageLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        UnnamedLanguageParser parser = new UnnamedLanguageParser(tokens);

        try {
            ASTNode program = parser.program();
            PrettyPrinter fmt = new PrettyPrinter(System.out);
            fmt.process(program);

            final Process p = Runtime.getRuntime().exec("dot -Tpng -o ast.png");
            try (OutputStream out = p.getOutputStream()) {
                DotPrinter graph = new DotPrinter(new PrintStream(out));
                graph.process(program);
            }
            new Thread(new Runnable() {
                public void run() {
                    InputStreamReader reader = new InputStreamReader(p.getErrorStream());
                    Scanner scan = new Scanner(reader);
                    while (scan.hasNextLine()) {
                        System.err.println(scan.nextLine());
                    }
                }
            }).start();
            p.waitFor();
        } catch (RecognitionException e) {
            // A lexical or parsing error occured.
            // ANTLR will have already printed information on the
            // console due to code added to the grammar.  So there is
            // nothing to do here.
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
}
