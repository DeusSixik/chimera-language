package net.aros.language.ast.ops;

import java.util.List;
import java.util.Optional;

public enum BinaryOp {
    BITWISE_OR("|"),
    BITWISE_AND("&"),
    BITWISE_XOR("^"),
    LOGICAL_AND("&&", "and"),
    LOGICAL_OR("||", "or"),
    LOGICAL_XOR("^^", "xor"),
    SHIFT_LEFT("<<"),
    SHIFT_RIGHT(">>"),
    SHIFT_RIGHT_UNSIGNED(">>>"),
    MULTIPLY("*"),
    DIVIDE("/"),
    MODULO("%"),
    PLUS("+"),
    MINUS("-"),
    LESS("<"),
    LESS_EQUAL("<="),
    GREATER(">"),
    GREATER_EQUAL(">="),
    EQUALS("=="),
    NOT_EQUALS("!=");

    private final List<String> values;

    BinaryOp(String... values) {
        this.values = List.of(values);
    }

    public static Optional<BinaryOp> byValue(String value) {
        for (var op : values()) if (op.values.contains(value)) return Optional.of(op);
        return Optional.empty();
    }
}
