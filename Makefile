#
GNAME= UnnamedLanguage
GSRC= $(GNAME).g

all: grammar compiler

grammar: $(GSRCS)
	java org.antlr.Tool -fo . $(GSRC)

compiler:
	javac *.java

test:
	java org.junit.runner.JUnitCore ParserTestHarness

clean:
	rm *.class $(GNAME)*.java $(GNAME)__.g $(GNAME).tokens


 
