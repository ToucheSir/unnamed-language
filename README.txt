The Unnamed Language

# Building:
Run `make` to generate the grammar, compile the source and bundle an executable jar (unnamed_language.jar).
Run `make test` to kick off the automated JUnit test suite.

# Running:
Either run `unnamed-language.jar` or use the provided `ulc` script
e.g. java -jar unnamed-language.jar test.ul
The compiler will print (to stdout) a textual representation of the IR that may be further passed to the "codegen" tool.

## Flags
Passing the `-astgraph` flag will generate a DOT graph representation of the AST that can be passed to graphviz.

# Compiler Internals:
The compiler is written in a mix of Java and Kotlin.
Antlr generates Java source files for the parser and lexer, while the rest of the compiler (AST, etc.) is Kotlin.

The general structure is as follows:
Compiler (ties everything in the pipeline together)
|-> UnnamedLanguageLexer (auto-generated)
|-> UnnamedLanguageParser (auto-generated)
    |-> ASTNode classes
|-> AST Consumers (PrettyPrinter, DOTPrinter, etc.)

## AST Consumers
Using Kotlin's sealed classes and basic pattern matching (see https://kotlinlang.org/docs/reference/sealed-classes.html),
we can traverse the AST without needing a dedicated visitor interface or classes. Thus, both the pretty-printer and
DOT graph printer need only implement a common single-method interface (defined in ASTConsumer.kt) to walk the tree.
It is also no longer necessary to add a `visit` method on each AST node class.

This approach should be considered an alternative to rather than a replacement for the visitor pattern,
especially in a mixed-paradigm language such as Kotlin.
