MVN=maven/bin/mvn

all: compiler

grammar:
	$(MVN) antlr3:antlr

compiler:
	$(MVN) assembly:assembly

test:
	$(MVN) test

clean:
	$(MVN) clean

devdist:
	zip -r ulc.zip maven pom.xml Makefile ulc README.txt src tests
