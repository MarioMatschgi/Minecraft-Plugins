package at.mario.luckyploit.modules;

import java.util.ArrayList;
import java.util.List;

import at.mario.luckyploit.Luckyploit;

public class ModuleManager {
	
	public List<Module> modules = new ArrayList<Module>();
	
	public ModuleManager() {
		
		Luckyploit.getLogger().info("Loaded Modules: " + modules.size());
	}
	
	public void addModule(Module module) {
		this.modules.add(module);
		Luckyploit.getLogger().loading("Module: " + module.getName());
	}
	
	public List<Module> getModules() {
		return modules;
	}
	public Module getModuleByName(String moduleName) {
		for (Module module : modules) {
			if (module.getName().trim().equalsIgnoreCase(moduleName) || module.toString().trim().equalsIgnoreCase(moduleName)) {
				return module;
			}
		}
		return null;
	}
	public Module getModule(Class <? extends Module> clazz) {
		for (Module module : modules) {
			if (module.getClass() == clazz) {
				return module;
			}
		}
		return null;
	}
}
