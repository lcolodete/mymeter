package org.mymeter.common;

public enum MTI {
	M_0100,
	M_0110,
	M_0200,
	M_0210,
	M_0320,
	M_0330,
	M_0400,
	M_0410,
	M_0500,
	M_0510,
	M_0800,
	M_0810;
		
	public String toString() {
		return this.name().split("_")[1];
	}
	
}
