package net.aros.language.parsing;

import net.aros.language.LangLexer;
import net.aros.language.ast.first.Program;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class LangParser {
    public Program parse(String source) {
        LangErrorListener errorListener = new LangErrorListener();

        CharStream input = CharStreams.fromString(source);
        LangLexer lexer = new LangLexer(input);
        lexer.removeErrorListeners();
        lexer.addErrorListener(errorListener);

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        net.aros.language.LangParser parser = new net.aros.language.LangParser(tokens);

//        parser.setErrorHandler(new BailErrorStrategy());

        try {
            ParseTree tree = parser.program();
            if (errorListener.hasErrors()) {
                throw new ParseException(String.join("\n", errorListener.getErrors().stream().map(Object::toString).toList()));
            }
            Antlr2LangVisitor visitor = new Antlr2LangVisitor();
            return (Program) visitor.visit(tree);
        } catch (Exception e) {
            throw new ParseException("Internal parser error: " + e.getMessage(), e);
        }
    }
}
