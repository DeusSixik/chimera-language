package net.aros.chimera.parsing;

import net.aros.chimera.ast.first.Program;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class ChiParser {
    public Program parse(SourceFile sourceFile) {
//        DiagnosticCollector diagnostics = new DiagnosticCollector();
//        ChimeraErrorListener errorListener = new ChimeraErrorListener(sourceFile, diagnostics);

        CharStream input = CharStreams.fromString(sourceFile.text());
        net.aros.chimera.ChimeraLexer lexer = new net.aros.chimera.ChimeraLexer(input);
        net.aros.chimera.ChimeraParser parser = new net.aros.chimera.ChimeraParser(new CommonTokenStream(lexer));

//        lexer.removeErrorListeners();
//        lexer.addErrorListener(errorListener);
//        parser.removeErrorListeners();
//        parser.addErrorListener(errorListener);
//        parser.setErrorHandler(new ChimeraErrorStrategy(new DefaultParseEventListener(sourceFile, diagnostics)));


        ParseTree tree = parser.program();

//        if (diagnostics.hasErrors()) {
//            diagnostics.diagnostics().forEach(DiagnosticsRenderer::renderer);
//            System.exit(1);
//        }

        Antlr2ChiVisitor visitor = new Antlr2ChiVisitor();
        return (Program) visitor.visit(tree);
    }
}
