# UTD CS 4390 Math Project Makefile

# Compile all Java source files
all:
	javac *.java

# Run the server
run-server:
	java MathServer

# Run the client
run-client:
	java MathClient

# Remove all compiled class files
clean:
	del /f *.class