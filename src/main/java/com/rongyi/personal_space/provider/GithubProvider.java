package com.rongyi.personal_space.provider;

import com.alibaba.fastjson2.JSON;
import com.rongyi.personal_space.dto.AccessTokenDto;
import com.rongyi.personal_space.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GithubProvider {
    public String getAccessToken(AccessTokenDto dto) {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(dto));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String str = response.body().string();
            return str.split("&")[0].split("=")[1];
        } catch (IOException e) {
        }
        return null;
    }

    public GithubUser getUser(String accessToken) {
        System.out.println("access token"+accessToken);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user")
                .header("Authorization","Bearer "+accessToken)
                .header("Accept","application/vnd.github+json")
                .header("X-GitHub-Api-Version","2022-11-28")
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            System.out.println(string);
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            return githubUser;
        } catch (IOException e) {
        }
        return null;
    }
}
