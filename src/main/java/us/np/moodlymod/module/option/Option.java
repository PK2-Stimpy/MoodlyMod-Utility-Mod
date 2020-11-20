package us.np.moodlymod.module.option;

public class Option<T> {
    public T value;
    private String name;
    private T defaultValue;
    private OptionBetterMode mode = null;
    private int sOption = 0;

    public Option(String name, T defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }
    public Option(String name, T defaultValue, OptionBetterMode mode, int sOption) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        configShowMode(mode, sOption);
    }

    public void configShowMode(OptionBetterMode mode, int sOption) {
        this.mode = mode;
        this.sOption = sOption;
    }
    public boolean shouldShow() { return (((mode != null) && (mode.getMode() == sOption)) || (mode == null)); }
    public String getName() { return name; }
    public T getDefaultValue() { return defaultValue; }
    public T getValue() { return value; }
    public void setValue(T value) { this.value = value; }
}