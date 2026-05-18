package net.aros.chimera.ast.ops;

import java.util.Objects;

public enum AssignmentOp {
    BITWISE_OR(BinaryOp.BITWISE_OR, "|="),
    BITWISE_AND(BinaryOp.BITWISE_AND, "&="),
    BITWISE_XOR(BinaryOp.BITWISE_XOR, "^="),
    LOGICAL_AND(BinaryOp.LOGICAL_AND, "&&="),
    LOGICAL_OR(BinaryOp.LOGICAL_OR, "||="),
    LOGICAL_XOR(BinaryOp.LOGICAL_XOR, "^^="),
    SHIFT_LEFT(BinaryOp.SHIFT_LEFT, "<<="),
    SHIFT_RIGHT(BinaryOp.SHIFT_RIGHT, ">>="),
    SHIFT_RIGHT_UNSIGNED(BinaryOp.SHIFT_RIGHT_UNSIGNED, ">>>="),
    MULTIPLY(BinaryOp.MULTIPLY, "*="),
    DIVIDE(BinaryOp.DIVIDE, "/="),
    MODULO(BinaryOp.MODULO, "%="),
    PLUS(BinaryOp.PLUS, "+="),
    MINUS(BinaryOp.MINUS, "-=");

    private final BinaryOp binaryOp;
    private final String value;

    AssignmentOp(BinaryOp binaryOp, String value) {
        this.binaryOp = binaryOp;
        this.value = value;
    }

    public static BinaryOp opByValue(String value) {
        for (AssignmentOp op : values()) if (Objects.equals(op.value, value)) return op.binaryOp;
        return null;
    }
}
