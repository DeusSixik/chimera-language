package net.aros.language.parsing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public record SourceFile(
        String name,
        String text,
        List<String> lines
) {
    public static SourceFile from(Path path) throws IOException {
        String text = Files.readString(path);

        return new SourceFile(
                path.getFileName().toString(),
                text,
                List.of(text.split("\n", -1))
        );
    }

    public static SourceFile fromText(String text) {
        return new SourceFile("<anonymous>", text, List.of(text.split("\n", -1)));
    }
}
