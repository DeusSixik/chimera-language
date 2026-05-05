package net.aros.language;

import net.aros.language.ast.first.Program;
import net.aros.language.parsing.LangParser;
import net.aros.language.parsing.test.PrintVisitor;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Lang {
    public static void main(String[] args) throws URISyntaxException, IOException {
        String text = Files.readString(Path.of(Lang.class.getResource("/third.l").toURI()));

        Program program = new LangParser().parse(text);
        PrintVisitor visitor = new PrintVisitor();

        System.out.println(program);
        visitor.visit(program);
        System.out.println(visitor);
    }
}
