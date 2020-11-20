package us.np.moodlymod.module.option;

public class OptionDouble extends Option<Double> {

    protected Double min, max;
    private Double value;
    private Double defaultValue;

    public OptionDouble(String name, Double defaultValue, Double min, Double max) {
        super(name, defaultValue);
        this.min = min;
        this.max = max;
        this.defaultValue = this.value = defaultValue;
    }

    @Override
    public Double getDefaultValue() { super.getDefaultValue(); return defaultValue; }

    @Override
    public Double getValue() { super.getValue(); return value; }

    @Override
    public void setValue(Double value) { super.setValue(value); this.value = value; }

    public Double getMin() {

        return min;
    }

    public Double getMax() {

        return max;
    }
}