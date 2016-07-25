package com.shenghaiyang.dm;

import com.shenghaiyang.dm.entity.Dependency;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by shenghaiyang on 2016/7/25.
 */
public interface DependencyService {

    @GET("{path}/maven-metadata.xml")
    Call<Dependency> mavenMetaData(@Path("path") String path);
}
