package com.bls220.TribalWars.Tile;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;



public class Tile
{
	private static final int TILE_SIZE = 32;
	
	protected float xPos,yPos,zPos;
	protected boolean isPassable = true;
	
	private Bitmap mSprite;
	
	private ETile mType = ETile.AIR;
	
	public Tile(ETile type)
	{
		mType = type;
	}
	
	public void createTile(Resources res) {
		if( mType != ETile.AIR ) {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inPreferredConfig = Config.ARGB_8888;
			
			Bitmap base = BitmapFactory.decodeResource(res, this.BaseSprite(), opts);
			
			Paint tileP = new Paint();
			tileP.setShader(new BitmapShader(base,TileMode.CLAMP,TileMode.CLAMP));
			tileP.setColorFilter(new PorterDuffColorFilter(this.OverlayColor(),PorterDuff.Mode.MULTIPLY));
			tileP.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SCREEN));
			
			Bitmap tile = Bitmap.createBitmap(base.getWidth(), base.getHeight(), Config.ARGB_8888);
		    tile.eraseColor(Color.TRANSPARENT);
			
			Canvas c = new Canvas(tile);
			c.drawRect(0, 0, tile.getWidth(), tile.getHeight(), tileP);
			this.setSprite(tile);
		}
	}
	
	public void draw(Canvas canvas)
	{
		//Draw tile
		if( mSprite != null ) canvas.drawBitmap(mSprite, 0, 0, null);
	}
	
	public byte ID(){ return this.mType.id; }
	public int BaseSprite(){ return this.mType.base; }
	public int OverlayColor(){ return this.mType.overlay; }
	public void setSprite(Bitmap sprite){ this.mSprite = sprite; }
	
	public boolean IsPassable(){ return this.isPassable; }
}
