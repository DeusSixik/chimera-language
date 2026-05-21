package net.aros.chimera.errors;

import net.aros.chimera.ast.SourcePos;
import net.aros.chimera.errors.diagnostic.ChimeraDiagnosticCollector;
import net.aros.chimera.errors.diagnostic.Diagnostic;
import net.aros.chimera.errors.diagnostic.ErrorCode;
import net.aros.chimera.parsing.SourceFile;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

import java.util.BitSet;

public class ChimeraErrorListener implements ANTLRErrorListener {

    private final SourceFile sourceFile;
    private final ChimeraDiagnosticCollector diagnostics;

    public ChimeraErrorListener(SourceFile sourceFile, ChimeraDiagnosticCollector diagnostics) {
        this.sourceFile = sourceFile;
        this.diagnostics = diagnostics;
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object o, int line, int charPositionInLine, String msg, RecognitionException e) {
        ErrorCode code = ErrorCode.E0001; // По умолчанию
        if (msg.contains("mismatched input")) {
            code = ErrorCode.E0003;
        } else if (msg.contains("no viable alternative") || msg.contains("token recognition error")) {
            code = ErrorCode.E0002;
        }

        diagnostics.report(Diagnostic.error(
                code,
                msg,
                sourceFile,
                new SourcePos(line, charPositionInLine + 1)
        ));
    }

    @Override
    public void reportAmbiguity(Parser parser, DFA dfa, int i, int i1, boolean b, BitSet bitSet, ATNConfigSet atnConfigSet) {

    }

    @Override
    public void reportAttemptingFullContext(Parser parser, DFA dfa, int i, int i1, BitSet bitSet, ATNConfigSet atnConfigSet) {

    }

    @Override
    public void reportContextSensitivity(Parser parser, DFA dfa, int i, int i1, int i2, ATNConfigSet atnConfigSet) {

    }
}
