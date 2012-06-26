package com.bls220.TribalWars.Tile;

import android.graphics.Color;

public enum ETile
{
	// id tile passable color
	AIR		(0,0,1, 0),
	GRASS	(1,1,1, Color.rgb(74, 139, 36)),
	//DIRT	(2,1,1, Color.rgb(112, 83, 64));
	DIRT	(2,1,1, Color.rgb(0,0,127));
	
	ETile(int ID, int Tile, int Passable, int Overlay)
	{
		this.id = (byte) (ID & 0xff);
		this.base = Tile;
		this.passable = (Passable != 0);
		this.overlay = Overlay;
	}
	
	public final byte id;
	public final int base;
	public final boolean passable;
	public final int overlay;
}
