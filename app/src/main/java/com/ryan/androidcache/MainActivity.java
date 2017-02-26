package com.ryan.androidcache;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ryan.rxcache.AndroidCache;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

@SuppressWarnings("unchecked")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AndroidCache androidCache = new AndroidCache.Builder()
                        .setContext(this)
                        .setCacheSize(AndroidCache.CACHE_SIZE_DEFAULT)
                        .setCacheFolder("folder_name")
                        .build();

        // demo cache  object
        Article article = new Article("1", "title 1", "content 1");

        androidCache.putObjectToFile("object", article);

        androidCache.getObjectFromFile("object", Article.class)
                    .subscribe(new Consumer<Article>() {
                        @Override
                        public void accept(Article article) throws Exception {
                            // do something with article
                        }
                    });

        // demo cache array list
        List<Article> articleList = new ArrayList<>();
        for (int i=0; i<10; i++) {
            Article a = new Article(String.valueOf(i), "title " + String.valueOf(i), "content " + String.valueOf(i));
            articleList.add(a);
        }
        androidCache.putArrayToFile("array", articleList);
        androidCache.getArrayFromFile("array", Article.class)
                    .subscribe(new Consumer<List<Article>>() {
                        @Override
                        public void accept(List<Article> articleList) throws Exception {
                            // do something with article list
                        }
                    });

    }

    public static class Article {
        public String id;
        public String title;
        public String content;

        public Article(String id, String title, String content) {
            this.id = id;
            this.title = title;
            this.content = content;
        }
    }
}
