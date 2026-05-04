package net.aros.language.ast.ops;

import java.util.List;
import java.util.Optional;

public enum UnaryOp {
    LOGIC_NOT("!", "not"),
    BIT_NOT("~"),
    MINUS("-"),
    PLUS("+");

    private final List<String> values;

    UnaryOp(String... args) {
        this.values = List.of(args);
    }

    public List<String> getValues() {
        return values;
    }

    public static Optional<UnaryOp> byValue(String value) {
        for (var op : values()) if (op.values.contains(value)) return Optional.of(op);
        return Optional.empty();
    }
}
