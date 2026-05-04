package net.aros.language.ast.first;

import net.aros.language.ast.SourcePos;

import java.util.List;

public record Program(List<Stmt> stmts) implements Node {
    @Override
    public SourcePos pos() {
        return new SourcePos(0, 0);
    }
}
