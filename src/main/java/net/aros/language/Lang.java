package net.aros.language;

import net.aros.language.ast.first.Program;
import net.aros.language.parsing.LangParser;
import net.aros.language.parsing.SourceFile;
import net.aros.language.parsing.test.Ast2PseudoCodeVisitor;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;

// TODO: 10.05.2026 "Lang" -> "Chimera". Cool name I suppose
public class Lang {
    public static void main(String[] args) throws URISyntaxException, IOException {

        Program program = new LangParser().parse(SourceFile.from(Path.of(Lang.class.getResource("/third.l").toURI())));
        Ast2PseudoCodeVisitor visitor = new Ast2PseudoCodeVisitor();

        System.out.println(program);
        System.out.println(visitor.visit(program));
    }
}
