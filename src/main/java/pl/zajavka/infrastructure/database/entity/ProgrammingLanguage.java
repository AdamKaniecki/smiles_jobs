package pl.zajavka.infrastructure.database.entity;

public enum ProgrammingLanguage {
    JAVA("Java"),
    PYTHON("Python"),
    JAVASCRIPT("JavaScript"),
    C("C"),
    C_PLUS_PLUS("C++"),
    C_SHARP("C#"),
    PHP("PHP"),
    R("R"),
    SWIFT("Swift"),
    TYPESCRIPT("TypeScript"),
    GO("Go"),
    KOTLIN("Kotlin"),
    RUBY("Ruby"),
    SQL("SQL"),
    HTML("HTML"),
    CSS("CSS"),
    OBJECTIVE_C("Objective-C"),
    MATLAB("MATLAB"),
    SHELL_SCRIPT("Shell Script"),
    SCALA("Scala"),
    PERL("Perl"),
    RUST("Rust"),
    DART("Dart"),
    JULIA("Julia"),
    COBOL("COBOL"),
    FORTRAN("Fortran"),
    VB_NET("VB.NET"),
    LUA("Lua"),
    VHDL("VHDL"),
    OTHER("Other");

    private final String displayName;

    ProgrammingLanguage(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
