package com.ooops.lms.Cache;

import com.mysql.cj.util.LRUCache;
import javafx.scene.image.Image;

public class ImageCache {
    private static LRUCache<String, Image> imageLRUCache = new LRUCache(200);

    public static LRUCache<String, Image> getImageLRUCache() {
        return imageLRUCache;
    }
}
