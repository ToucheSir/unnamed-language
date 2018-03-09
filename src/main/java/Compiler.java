/*
 * Compiler.java
 *
 * A starting place for the unamed language compiler for CSC 435/535
 *
 */

import ast.ASTNode;
import ast.DotPrinter;
import codegen.JavaCodeGenerator;
import ir.IRGenerator;
import ir.IRPrinter;
import ir.IRProgram;
import org.antlr.runtime.*;
import org.apache.commons.cli.*;
import parser.UnnamedLanguageLexer;
import parser.UnnamedLanguageParser;
import semantic.SemanticException;
import semantic.TypeChecker;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Compiler {
    public static void main(String[] args) throws Exception {
        Options options = new Options();
        options.addOption("astgraph", "Generate a DOT-formatted AST tree for the input file");

        CommandLineParser cliParser = new DefaultParser();
        try {
            CommandLine cmd = cliParser.parse(options, args);
            boolean dumpAst = cmd.hasOption("astgraph");
            List<String> restArgs = cmd.getArgList();

            if (restArgs.size() < 1) {
                printUsage(options);
                return;
            }
            String fileName = restArgs.get(0);
            compileFile(fileName, dumpAst);
        } catch (UnrecognizedOptionException e) {
            System.err.printf("ulc: invalid option: %s%n", e.getOption());
            printUsage(options);
        }
    }

    private static void compileFile(String fileName, boolean dumpAst) throws IOException {
        Path filePath = Paths.get(fileName);
        if (!Files.exists(filePath)) {
            System.err.printf("file %s does not exist%n", fileName);
            return;
        }
        ANTLRInputStream input = new ANTLRInputStream(new FileInputStream(fileName));

        // The name of the grammar here is "UnnamedLanguage",
        // so ANTLR generates UnnamedLanguageLexer and UnnamedLanguageParser
        UnnamedLanguageLexer lexer = new UnnamedLanguageLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        UnnamedLanguageParser parser = new UnnamedLanguageParser(tokens);

        try {
            // TODO don't pretty-print by default after we get proper type-checking and/or evaluation
            ASTNode program = parser.program();
//            PrettyPrinter fmt = new PrettyPrinter(System.out);
//            fmt.process(program);
            TypeChecker typeChecker = new TypeChecker();
            typeChecker.process(program);

            if (dumpAst) {
                File graphFile = new File(filePath.getFileName() + ".dot");
                DotPrinter graph = new DotPrinter(new PrintStream(graphFile));
                graph.process(program);
            }

            IRGenerator irGenerator = new IRGenerator();
            IRProgram irProgram = irGenerator.process(program);
//            new IRPrinter(System.out).print(irProgram);
            new JavaCodeGenerator(System.out).generate(irProgram);
        } catch (RecognitionException e) {
            // A lexical or parsing error occurred.
            // ANTLR will have already printed information on the
            // console due to code added to the grammar,
            // so there is nothing to do here.
        } catch (SemanticException e) {
            System.out.printf("Error:%d:%d:%s%n", e.getLine(), e.getCol(), e.getMessage());
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    private static void printUsage(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("ulc <options> filename.ul", "Options:", options, "");
    }
}
