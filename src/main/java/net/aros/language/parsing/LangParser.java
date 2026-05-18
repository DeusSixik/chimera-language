package net.aros.language.parsing;

import net.aros.language.ast.first.Program;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class LangParser {
    public Program parse(SourceFile sourceFile) {
//        DiagnosticCollector diagnostics = new DiagnosticCollector();
//        LangErrorListener errorListener = new LangErrorListener(sourceFile, diagnostics);

        CharStream input = CharStreams.fromString(sourceFile.text());
        net.aros.language.LangLexer lexer = new net.aros.language.LangLexer(input);
        net.aros.language.LangParser parser = new net.aros.language.LangParser(new CommonTokenStream(lexer));

//        lexer.removeErrorListeners();
//        lexer.addErrorListener(errorListener);
//        parser.removeErrorListeners();
//        parser.addErrorListener(errorListener);
//        parser.setErrorHandler(new LangErrorStrategy(new DefaultParseEventListener(sourceFile, diagnostics)));


        ParseTree tree = parser.program();

//        if (diagnostics.hasErrors()) {
//            diagnostics.diagnostics().forEach(DiagnosticsRenderer::renderer);
//            System.exit(1);
//        }

        Antlr2LangVisitor visitor = new Antlr2LangVisitor();
        return (Program) visitor.visit(tree);
    }
}
