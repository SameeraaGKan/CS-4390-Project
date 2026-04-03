# UTD CS 4390 Math Project Makefile

all:
	javac *.java

run-server:
	java MathServer

run-client:
	java MathClient

clean:
	rm -f *.class