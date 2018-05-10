package com.example.abhayaradhya.finalproject;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by abhayaradhya on 4/26/18.
 */

public class Bullet {
    private Drawable sprite;
    private int direction;
    private double speed;
    private Rect hitbox;
    public ImageView bulletView;
    //private RelativeLayout relLayout;

    public Bullet(Drawable img, int direction, double speed, Context c, double screenWidth, double screenHeight, RelativeLayout relLayout){
        sprite = img;
        this.direction = direction;
        this.speed = speed;
        bulletView = new ImageView(c);
        bulletView.setImageDrawable(sprite);
        bulletView.setScaleType(ImageView.ScaleType.CENTER);
        hitbox = new Rect();
        bulletView.getHitRect(hitbox);
        System.out.println("bullet width: " + bulletView.getWidth() + ", bullet height: " + bulletView.getHeight());
        if(direction ==1){
            bulletView.setX(0);
            bulletView.setY((float)(screenHeight/2 - bulletView.getHeight()/2));
        } else if(direction == 2){
            bulletView.setX((float)(screenWidth/2 - bulletView.getWidth()/2));
            bulletView.setY(0);
        } else if(direction == 3){
            bulletView.setX((float)(screenWidth/2 - bulletView.getWidth()/2));
            bulletView.setY((float)(screenHeight - 128));
        } else if(direction == 4){
            bulletView.setX((float)(screenWidth-128));
            bulletView.setY((float)(screenHeight/2 - bulletView.getHeight()/2));
        }
    }

    public Rect getHitbox(){
        return hitbox;
    }

    public void setHitbox(Rect newHitbox) { hitbox = newHitbox; }

    public ImageView getBulletView(){
        return bulletView;
    }

    public int getDirection() {return direction;}

}
