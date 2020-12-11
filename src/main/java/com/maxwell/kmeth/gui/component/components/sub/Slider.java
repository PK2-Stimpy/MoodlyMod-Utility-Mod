package com.maxwell.kmeth.gui.component.components.sub;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.ibm.icu.math.BigDecimal;
import com.maxwell.kmeth.gui.component.Component;
import com.maxwell.kmeth.gui.component.components.Button;
import com.maxwell.kmeth.utilities.Wrapper;

import net.minecraft.client.gui.Gui;
import us.np.moodlymod.module.option.OptionDouble;

public class Slider extends Component {
	private boolean hovered;
	public OptionDouble value;
	private Button parent;
	private int offset;
	private int x;
	private int y;

	public Slider(OptionDouble value, Button button, int offset) {
		super(value);
		this.value = value;
		this.parent = button;
		this.x = button.getParent().getX() + button.getParent().getWidth();
		this.y = button.getParent().getY() + button.getOffset();
		this.offset = offset;
	}

	@Override
	public int getOffset() {
		return offset;
	}

	@Override
	public void renderComponent() {
		if(!option.isVisible())
			return;

		this.offset = parent.getParent().optionY;
		parent.getParent().optionY+=12;

		Gui.drawRect(this.parent.getParent().getX() + 2, this.parent.getParent().getY() + this.offset, this.parent.getParent().getX() + this.parent.getParent().getWidth(), this.parent.getParent().getY() + this.offset + 12, this.hovered ? -14540254 : -15658735);
		int drag = (int) (this.value.getValue() / this.value.getMax() * this.parent.getParent().getWidth());
		Gui.drawRect(this.parent.getParent().getX() + 2, this.parent.getParent().getY() + this.offset, this.parent.getParent().getX() + drag, this.parent.getParent().getY() + this.offset + 12, this.hovered ? -11184811 : -12303292);
		Gui.drawRect(this.parent.getParent().getX(), this.parent.getParent().getY() + this.offset, this.parent.getParent().getX() + 2, this.parent.getParent().getY() + this.offset + 12, -15658735);
		GL11.glPushMatrix();
		GL11.glScalef(0.5f, 0.5f, 0.5f);
		Wrapper.getMinecraft().fontRenderer.drawStringWithShadow(String.valueOf(this.value.getName()) + ": " + this.value.getValue(), (this.parent.getParent().getX() * 2 + 15), ((this.parent.getParent().getY() + this.offset + 2) * 2 + 5), -1);
		GL11.glPopMatrix();
	}

	@Override
	public void setOffset(int newOffset) {
		this.offset = newOffset;
	}

	@Override
	public void updateComponent(int mouseX, int mouseY) {
		if(!option.isVisible())
			return;
		this.hovered = (this.isMouseOnButtonD(mouseX, mouseY) || this.isMouseOnButtonI(mouseX, mouseY));
		this.y = this.parent.getParent().getY() + this.offset;
		this.x = this.parent.getParent().getX();
		if (this.hovered && this.parent.isOpen() && Mouse.isButtonDown(0)) {
			double diff = mouseX - this.parent.getParent().getX();
			double value = this.round(diff / (this.parent.getParent().getWidth() - 1) * this.value.getMax(), 1);
			this.value.setValue(value);
		}
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if(!option.isVisible())
			return;
		if (this.isMouseOnButtonD(mouseX, mouseY) && button == 0 && this.parent.isOpen()) {
			OptionDouble numberValue = this.value;
			double value = numberValue.getValue() - 0.1;
			numberValue.setValue(Math.round(value * 10.0) / 10.0);
		}
		if (this.isMouseOnButtonI(mouseX, mouseY) && button == 0 && this.parent.isOpen()) {
			OptionDouble numberValue = this.value;
			double value = numberValue.getValue() + 0.1;
			numberValue.setValue(Math.round(value * 10.0) / 10.0);
		}
		// this.parent.getMod().save();
	}
	
    private double round(double doubleValue, int numOfDecimals)  {
        BigDecimal bigDecimal = new BigDecimal(doubleValue);
        bigDecimal = bigDecimal.setScale(numOfDecimals, BigDecimal.ROUND_HALF_UP);
        return bigDecimal.doubleValue();
    }

	public boolean isMouseOnButtonD(int x, int y) {
		return x > this.x && x < this.x + (this.parent.getParent().getWidth() / 2 + 1) && y > this.y && y < this.y + 12;
	}

	public boolean isMouseOnButtonI(int x, int y) {
		return x > this.x + this.parent.getParent().getWidth() / 2 && x < this.x + this.parent.getParent().getWidth() && y > this.y && y < this.y + 12;
	}
}
