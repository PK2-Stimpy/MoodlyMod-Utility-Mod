package us.np.moodlymod.module.option;

public class OptionMode extends Option<Mode> {

    private Mode[] modes;
    private String modeName;
    public Mode[] getModes() {
        return modes;
    }

    public OptionMode(String modeName, Mode... modes) {
        super("+ " + modeName, null);
        this.modeName = modeName;
        this.modes = modes;
    }

    public Mode getMode(String name) {
        Mode m = null;
        for(Mode mode : modes) {
            if(mode.getName().equals(name)) {
                m = mode;
            }
        }
        return m;
    }

    public String getModeName() {
        return modeName;
    }
}