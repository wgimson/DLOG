DLOG.class:	DLOG.java Argument.class Predicate.class Program.class \
	            parser.class Lexer.class IDBNode.class
	javac DLOG.java
Program.class:		Program.java Rule.class IDBPredicate.class
	javac Program.java
Rule.class:		Rule.java Predicate.class
	javac Rule.java
Predicate.class:	Predicate.java Argument.class
	javac Predicate.java
Argument.class:		Argument.java 
	javac Argument.java
IDBPredicate.class:		IDBPredicate.java
	javac IDBPredicate.java
IDBNode.class:		IDBNode.java
	javac IDBNode.java
Lexer.class:	Lexer.java sym.class
	javac Lexer.java
Lexer.java:		DLOG.flex
	jflex DLOG.flex
sym.class:		sym.java
	javac sym.java
sym.java:	DLOG.cup
	java java_cup.Main DLOG.cup
parser.class:	parser.java
	javac parser.java
parser.java:	DLOG.cup
	java java_cup.Main DLOG.cup
clean:
	rm *.class Lexer.java Lexer.java~ sym.java parser.java
