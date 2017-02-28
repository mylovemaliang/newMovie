package cn.fuyoushuo.vipmovie.ext;

import android.graphics.Bitmap;

import java.util.HashMap;

public class BitmapManger {


    private static class BitmapHolder{
        private static BitmapManger INTANCE = new BitmapManger();
    }

    public static BitmapManger getIntance(){
        return BitmapManger.BitmapHolder.INTANCE;
    }

    private HashMap<Integer,Bitmap> bitmapCollection = new HashMap<Integer, Bitmap>();

    public Bitmap getBitmap(Integer fragmentId){
         if(fragmentId == null) return null;
         return bitmapCollection.get(fragmentId);
    }

    public void putBitmap(Integer fragmentId,Bitmap bitmap){
         if(fragmentId == null || bitmap == null) return;
         //如果没有关联图片,添加图片,有则直接更新
         if(!bitmapCollection.containsKey(fragmentId)){
              bitmapCollection.put(fragmentId,bitmap);
         }else{
             removeBitmap(fragmentId);
             bitmapCollection.put(fragmentId,bitmap);
         }
    }

    public void removeBitmap(Integer fragmentId){
         if(fragmentId == null) return;
         if(bitmapCollection.containsKey(fragmentId)){
             Bitmap existBitmap = bitmapCollection.get(fragmentId);
             bitmapCollection.remove(fragmentId);
             existBitmap.recycle();
         }
    }


}