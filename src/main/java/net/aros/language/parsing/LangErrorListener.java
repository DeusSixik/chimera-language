package net.aros.language.parsing;

import net.aros.language.ast.SourcePos;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;

import java.util.ArrayList;
import java.util.List;

public class LangErrorListener extends BaseErrorListener {
    private final List<ParseError> errors = new ArrayList<>();

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        errors.add(new ParseError(
                new SourcePos(line, charPositionInLine + 1),
                msg,
                offendingSymbol instanceof Token token ? token.getText() : "",
                e
        ));
    }

    public List<ParseError> getErrors() {
        return errors;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }
}
