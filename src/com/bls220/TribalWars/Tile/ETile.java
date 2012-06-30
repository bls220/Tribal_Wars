package com.bls220.TribalWars.Tile;

import android.graphics.Color;

public enum ETile
{
	// id tile passable color
	AIR		(0, 1, 0),
	GRASS	(1, 1, Color.rgb(74, 139, 36)),
	//DIRT	(1, 1, Color.rgb(112, 83, 64));
	DIRT	(1, 1, Color.rgb(0,0,127));
	
	ETile(int Tile, int Passable, int Overlay)
	{
		this.base = (byte)(Tile & 0xff);
		this.passable = (Passable != 0);
		this.overlay = Overlay;
		
		byte ID = 0;
		for( ETile t : ETile.values() ){
			if( t.base == this.base && t.passable == this.passable && t.overlay == this.overlay )
				break;
			ID++;	
		}
		
		this.id = ID;
	}
	
	public static ETile findByID(int ID){ return (ID>0) ? ETile.values()[ID] : AIR; }
	
	public final byte id;
	public final byte base;
	public final boolean passable;
	public final int overlay;
}
