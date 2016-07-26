package com.shenghaiyang.dm;

import com.shenghaiyang.dm.entity.Dependency;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by shenghaiyang on 2016/7/26.
 */
public class DependencyDownloader {

    private static final String BASE_URL = "http://jcenter.bintray.com/";
    private Logger logger = LoggerFactory.getLogger(DependencyDownloader.class);

    private int successCount;
    private int failureCount;

    protected void download(String location) {
        Map<String, String> map = path(location);
        if (map.isEmpty()) return;
        File output = new File(location, "build.gradle");
        update(map, output);
    }

    private Map<String, String> path(String location) {
        File file = new File(location, "path.yaml");
        Reader reader;
        try {
            reader = new FileReader(file);
        } catch (FileNotFoundException e) {
            logger.warn("Could not find file 'path.yaml'");
            return Collections.emptyMap();
        }
        Yaml yaml = new Yaml();
        return (Map<String, String>) yaml.load(reader);
    }

    private void update(Map<String, String> map, File file) {
        logger.info("-----------Start------------");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();
        DependencyService service = retrofit.create(DependencyService.class);

        file.deleteOnExit();

        successCount = 0;
        failureCount = 0;
        int count = map.size();
        List<String> list = new ArrayList<>();
        for (String key : map.keySet()) {
            logger.info(key + " start download.");
            String path = map.get(key);
            service.mavenMetaData(path).enqueue(new Callback<Dependency>() {
                @Override
                public void onResponse(Call<Dependency> call, Response<Dependency> response) {
                    String dependency = response.body().toString();
                    logger.info(dependency);
                    list.add(dependency);
                    if (list.size() == count) {
                        synchronized (list) {
                            if (list.size() == count) {
                                Collections.sort(list);
                                StringBuffer buffer = new StringBuffer();
                                for (String str : list) {
                                    buffer.append(str).append("\r\n");
                                }
                                try {
                                    FileUtils.writeStringToFile(file, buffer.toString(),
                                            "UTF-8", false);
                                } catch (IOException e) {
                                    logger.error("Error in save to file.");
                                    e.printStackTrace();
                                } finally {
                                    logger.info("SUCCESS");
                                }
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<Dependency> call, Throwable t) {
                    logger.error(key + " download error.", t);
                }
            });
        }
    }

}
