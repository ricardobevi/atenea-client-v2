package org.squadra.atenea.actions;

public abstract class PreloadAction {
	protected String param;
	
	public abstract void execute();
	public PreloadAction setParam(String action)
	{
		param = action;
		return this;
	}
	
}
