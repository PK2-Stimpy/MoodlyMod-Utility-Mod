package us.np.moodlymod.util;

public enum ChatColor {
    BLACK("0"),
    DARK_BLUE("1"),
    DARK_GREEN("2"),
    DARK_AQUA("3"),
    DARK_RED("4"),
    DARK_PURPLE("5"),
    GOLD("6"),
    GRAY("7"),
    DARK_GRAY("8"),
    BLUE("9"),
    GREEN("a"),
    AQUA("b"),
    RED("c"),
    LIGHT_PURPLE("d"),
    YELLOW("e"),
    WHITE("f"),
    RESET("r"),
    BOLD("l"),
    UNDERLINE("n"),
    ITALLIC("o"),
    MAGIC("k"),
    STRIKE("r");

    private String cum;
    public static String penis = "\u00a7";
    public String toString() {
        return cum;
    }
    public String s() {
        return cum;
    }
    private ChatColor(String cum) {
        this.cum = "\u00a7" + cum;
    }

    public static String parse(String prefix, String string) {
        return string.replaceAll(prefix, penis);
    }
}