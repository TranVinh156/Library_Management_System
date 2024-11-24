package com.ooops.lms.music;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.mysql.cj.util.LRUCache;
import com.ooops.lms.model.Music;

import java.util.ArrayList;
import java.util.List;

public class MusicInfoFetcher {
    private static final String APPLICATION_NAME = "YouTube Music Player";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String API_KEY = "AIzaSyChNsPo2ZSTE80-0Q5mwpLFysHY4zfDqtM";

    private static final LRUCache<String, List<Music>> musicCache = new LRUCache<>(50);

    /**
     * tìm âm nhạc.
     * @param keyword từ khoá
     * @return danh sách bài hát
     */
    public static List<Music> searchVideos(String keyword) {
        if (musicCache.containsKey(keyword)) {
            return musicCache.get(keyword);
        }
        List<Music> musicList = new ArrayList<>();
        try {
            // Khởi tạo YouTube Service
            YouTube youtubeService = new YouTube.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JSON_FACTORY,
                    request -> {}
            ).setApplicationName(APPLICATION_NAME).build();

            // Tạo truy vấn tìm kiếm
            YouTube.Search.List search = youtubeService.search().list("id,snippet");
            search.setKey(API_KEY);
            search.setQ(keyword);
            search.setType("video");
            search.setMaxResults(5L); // Tùy chỉnh số lượng kết quả
            search.setFields("items(id(videoId),snippet(title,thumbnails(default(url))))");

            // Thực hiện truy vấn
            SearchListResponse response = search.execute();
            List<SearchResult> searchResults = response.getItems();

            // Lấy danh sách videoId
            for (SearchResult result : searchResults) {
                String videoId = result.getId().getVideoId();
                String thumbnailUrl = result.getSnippet().getThumbnails().getDefault().getUrl();
                String title = result.getSnippet().getTitle();
                Music music = new Music(videoId, title, thumbnailUrl);
                musicList.add(music);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        musicCache.put(keyword, musicList);
        return musicList;
    }

    public static void main(String[] args) {
        String keyword = "ghé qua"; // Từ khóa tìm kiếm
        List<Music> musics = searchVideos(keyword);

        if (musics.isEmpty()) {
            System.out.println("No results found.");
        } else {
            for (Music music : musics) {
                System.out.println("Title: " + music.getTitle());
                System.out.println("Video URL: https://www.youtube.com/watch?v=" + music.getMusicId());
                System.out.println("Thumbnail URL: " + music.getThumbnail());
                System.out.println();
            }
        }
    }

}
