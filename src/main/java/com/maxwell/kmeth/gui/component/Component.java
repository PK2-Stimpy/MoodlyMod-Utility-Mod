package com.maxwell.kmeth.gui.component;

import us.np.moodlymod.module.option.Option;

public class Component {
	public Option option;
	public Component(Option option) {
		this.option = option;
	}
	public Component() {
		this.option = null;
	}

	public void renderComponent() {

	}

	public void updateComponent(int mouseX, int mouseY) {

	}

	public void mouseClicked(int mouseX, int mouseY, int button) {

	}

	public void keyTyped(char typedChar, int key) {

	}

	public void setOffset(int newOff) {

	}

	public int getOffset() { return 0; }

	public int getHeight() {
		return 0;
	}
}
