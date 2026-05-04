package net.aros.language.ast.first;

import net.aros.language.ast.SourcePos;

public sealed interface Node permits Expr, Stmt, Program {
    SourcePos pos();
}
