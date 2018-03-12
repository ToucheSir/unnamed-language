#!/usr/bin/env bash
file_name=$(basename $1)
prog_name=${file_name%.*}
../ulc $1 1> "$prog_name.ir" && \
../codegen --file "$prog_name.ir" 1> "$prog_name.j" && \
java -jar ../jasmin.jar "$prog_name.j" && \
java "$prog_name"

