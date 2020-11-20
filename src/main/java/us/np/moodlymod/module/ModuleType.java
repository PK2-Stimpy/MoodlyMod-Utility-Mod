package us.np.moodlymod.module;

public enum ModuleType {
    COMBAT, EXPLOIT, MOVEMENT, RENDER, MISC, WORLD, UI, ALWAYS_ENABLED(true);

    public boolean hiddenCategory = false;
    public String name;
    private ModuleType(String name, boolean hiddenCategory) {
        this.name = name;
        this.hiddenCategory = hiddenCategory;

        if(name == "") name = this.toString();
    }
    private ModuleType(String name) {
        this(name, false);
    }
    private ModuleType(boolean hiddenCategory) {
        this("", hiddenCategory);
    }
    private ModuleType() {
        this("", false);
    }
}
