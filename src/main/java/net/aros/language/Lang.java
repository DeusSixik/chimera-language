package net.aros.language;

import net.aros.language.ast.first.Program;
import net.aros.language.parsing.Antlr2LangVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Lang {
    public static void main(String[] args) throws URISyntaxException, IOException {
        String text = Files.readString(Path.of(Lang.class.getResource("/file.l").toURI()));

        net.aros.language.LangLexer lexer = new net.aros.language.LangLexer(CharStreams.fromString(text));
        net.aros.language.LangParser parser = new net.aros.language.LangParser(new CommonTokenStream(lexer));
        Program node = (Program) new Antlr2LangVisitor().visit(parser.program());

        System.out.println(node);
    }
}
