package net.aros.chimera.ast.first;

import net.aros.chimera.ast.SourcePos;

public sealed interface Node permits Expr, Stmt, Program {
    SourcePos pos();
}
