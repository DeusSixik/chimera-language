package net.aros.chimera.ast.first;

import net.aros.chimera.ast.SourcePos;

import java.util.List;

public record Program(List<Stmt> stmts) implements Node {
    @Override
    public SourcePos pos() {
        return new SourcePos(0, 0);
    }
}
