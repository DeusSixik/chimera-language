package net.aros.chimera;

import net.aros.chimera.ast.first.Program;
import net.aros.chimera.parsing.ChiParser;
import net.aros.chimera.parsing.SourceFile;
import net.aros.chimera.parsing.test.Ast2PseudoCodeVisitor;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;

public class ChimeraMain {
    public static void main(String[] args) throws URISyntaxException, IOException {

        Program program = new ChiParser().parse(SourceFile.from(Path.of(ChimeraMain.class.getResource("/third.chi").toURI())));
        Ast2PseudoCodeVisitor visitor = new Ast2PseudoCodeVisitor();

        System.out.println(program);
        System.out.println(visitor.visit(program));
    }
}
