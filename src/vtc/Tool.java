/**
 * 
 */
package vtc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author markebbert
 *
 */
public enum Tool {

	SET_OPERATOR("SetOperator", "SO", "Perform set operations on variant sets",
			Arrays.asList(new String[]{"SO", "SetOperator"})),
    VAR_STATS("VarStats", "VS", "Perform various statistical analyses",
            Arrays.asList(new String[]{"VS", "VarStats"})),
    COMPARE("Compare", "comp", "Compare two or variant files", 
    		 Arrays.asList(new String[]{"comp", "cmp", "compare"}));
	
	private String name, shortCommand, briefDescription;
	private List<String> permittedCommands;
	private Tool(String name, String shortCommand, String briefDescription, List<String> permittedCommands){
		this.name = name;
		this.shortCommand = shortCommand;
		this.briefDescription = briefDescription;
		this.permittedCommands = permittedCommands;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getShortCommand(){
		return this.shortCommand;
	}
	
	public String getBriefDescription(){
		return this.briefDescription;
	}
	
	public List<String> getPermittedCommands(){
		return this.permittedCommands;
	}
	
	public boolean permittedCommandsContain(String command){
		for(String s : permittedCommands){
			if(s.equalsIgnoreCase(command)){
				return true;
			}
		}
		return false;
	}
	
	public String toString(){
		return  getName() + " (" +
				getShortCommand() +
				") -- " + getBriefDescription() +
				".";
	}
}
