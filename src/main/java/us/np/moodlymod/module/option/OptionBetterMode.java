package us.np.moodlymod.module.option;

public class OptionBetterMode extends Option<BetterMode> {
    private int defaultMode, i;
    private BetterMode[] modes;

    public OptionBetterMode(String name, int defaultMode, BetterMode... modes) {
        super(name, modes[defaultMode]);
        this.defaultMode = this.i = defaultMode;
        this.modes = modes;
    }

    public String getDisplayName() { return modes[i].mode; }
    public String nextMode() { i++; if(i == modes.length) i = 0; return getDisplayName(); }
    public String setMode(int i) {
        this.i = i;
        if(i >= modes.length) this.i = 0;
        return modes[this.i].mode;
    }
    public int getMode() { return i; }
    public BetterMode[] getModes() { return modes; }
    public int getDefaultMode() { return defaultMode; }
}