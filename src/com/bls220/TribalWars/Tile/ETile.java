package com.bls220.TribalWars.Tile;

import android.graphics.Color;

public enum ETile
{
	AIR		(0,0,0),
	GRASS	(1,1, Color.rgb(74, 139, 36)),
	DIRT	(2,1, Color.rgb(112, 83, 64));
	
	ETile(int ID, int Tile, int Overlay)
	{
		this.id = (byte) (ID & 0xff);
		this.base = Tile;
		this.overlay = Overlay;
	}
	
	public final byte id;
	public final int base;
	public final int overlay;
}
