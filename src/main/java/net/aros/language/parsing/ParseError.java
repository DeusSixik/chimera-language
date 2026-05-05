package net.aros.language.parsing;

import net.aros.language.ast.SourcePos;
import org.antlr.v4.runtime.RecognitionException;

public record ParseError(
        SourcePos pos,
        String message,
        String offendingToken,
        RecognitionException exception
) {
    @Override
    public String toString() {
        return "[%d:%d] %s".formatted(pos.line(), pos.column(), message);
    }
}
