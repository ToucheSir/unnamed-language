#
ANT=ant/bin/ant
GNAME= UnnamedLanguage
GSRC= $(GNAME).g

all: compiler

grammar:
	$(ANT) antlr

compiler:
	$(ANT) package-jar

test:
	$(ANT) test

clean:
	$(ANT) clean



