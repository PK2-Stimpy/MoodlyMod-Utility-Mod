package com.maxwell.kmeth.gui;

import java.util.ArrayList;

import com.maxwell.kmeth.gui.component.Component;
import com.maxwell.kmeth.gui.component.Frame;

import com.maxwell.kmeth.gui.component.components.sub.BetterMode;
import com.maxwell.kmeth.gui.component.components.sub.Checkbox;
import com.maxwell.kmeth.gui.component.components.sub.Keybind;
import com.maxwell.kmeth.gui.component.components.sub.Slider;
import net.minecraft.client.gui.GuiScreen;
import us.np.moodlymod.MoodlyMod;
import us.np.moodlymod.module.ModuleType;
import us.np.moodlymod.module.option.Option;

public class GUI extends GuiScreen {
	public ArrayList<Frame> frames;

	public GUI() {
		this.frames = new ArrayList<Frame>();
		int frameX = 5;
		ModuleType[] values;
		for (int length = (values = ModuleType.values()).length, i = 0; i < length; ++i) {
			ModuleType category = values[i];
			if(category.hiddenCategory) continue;
			if(MoodlyMod.moduleManager.getModulesByCategory(category).size() == 0) continue;
 			Frame frame = new Frame(category);
			frame.setX(frameX);
			this.frames.add(frame);
			frameX += frame.getWidth() + 1;
		}
	}

	public Option getOption(Component component) {
		if(component instanceof BetterMode) return ((BetterMode) component).option;
		if(component instanceof Checkbox) return ((Checkbox) component).option;
		if(component instanceof Slider) return ((Slider) component).value;
		return null;
	}

	public boolean shouldRenderOption(Option option) {
		if(option != null)
			return option.shouldShow();
		return false;
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		for (Frame frame : this.frames) {
			frame.renderFrame();
			frame.updatePosition(mouseX, mouseY);
			for (Component comp : frame.getComponents()) {
				if(shouldRenderOption(getOption(comp))) continue;

				comp.updateComponent(mouseX, mouseY);
			}
		}
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		for (Frame frame : this.frames) {
			if (frame.isWithinHeader(mouseX, mouseY) && mouseButton == 0) {
				frame.setDrag(!frame.isDragging);
				frame.dragX = mouseX - frame.getX();
				frame.dragY = mouseY - frame.getY();
			}
			if (frame.isWithinHeader(mouseX, mouseY) && mouseButton == 1) {
				frame.setOpen(!frame.isOpen());
			}
			if (frame.isOpen() && !frame.getComponents().isEmpty()) {
				for (Component component : frame.getComponents()) {
					if(shouldRenderOption(getOption(component))) continue;

					component.mouseClicked(mouseX, mouseY, mouseButton);
				}
			}
		}
	}

	protected void keyTyped(char typedChar, int keyCode) {
		for (Frame frame : this.frames) {
			if (frame.isOpen() && keyCode != 1 && !frame.getComponents().isEmpty()) {
				for (Component component : frame.getComponents()) {
					if(shouldRenderOption(getOption(component))) continue;

					component.keyTyped(typedChar, keyCode);
				}
			}
		}
		if (keyCode == 1) {
			this.mc.displayGuiScreen(null);
		}
	}
	
	protected void mouseMovedOrUp(int mouseX, int mouseY, int state) {
		for (Frame frame : this.frames) {
			frame.setDrag(false);
		}
	}

	public boolean doesGuiPauseGame() {
		return false;
	}
}
