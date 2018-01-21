#
ANT=ant/bin/ant
GNAME= UnnamedLanguage
GSRC= $(GNAME).g

all: grammar compiler

grammar:
	$(ANT) antlr

compiler:
	$(ANT) build

test:
	$(ANT) test

clean:
	$(ANT) clean



