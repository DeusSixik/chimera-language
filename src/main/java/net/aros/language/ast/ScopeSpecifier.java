package net.aros.language.ast;

public enum ScopeSpecifier {
    GLOBAL, LOCAL;

    public static ScopeSpecifier getDefault() {
        return LOCAL;
    }
}
