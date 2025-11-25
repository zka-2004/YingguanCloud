package org.zka.common.utils;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import org.zka.common.config.VodConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

//视频操作
@Component
public class VodTemplate {

    @Autowired
    private VodConfig vodConfig;

    //初始化
    public DefaultAcsClient initVodClient() {
        DefaultProfile profile = DefaultProfile.getProfile(
                vodConfig.getRegionId(),
                vodConfig.getAccessKeyId(),
                vodConfig.getAccessKeySecret());
        return new DefaultAcsClient(profile);
    }

    //上传视频,返回视频id
    // 修改VodTemplate的uploadVideo方法，添加输入流自动关闭
    public String uploadVideo(String fileName, InputStream inputStream) {
        System.out.println("VOD配置 - regionId: " + vodConfig.getRegionId());
        System.out.println("VOD配置 - accessKeyId: " + vodConfig.getAccessKeyId());
        // 使用try-with-resources自动关闭inputStream
        try (InputStream is = inputStream) {
            String title = fileName.substring(0, fileName.lastIndexOf("."));
            UploadStreamRequest request = new UploadStreamRequest(
                    vodConfig.getAccessKeyId(),
                    vodConfig.getAccessKeySecret(),
                    title,
                    fileName,
                    is); // 使用包装后的is

            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);

            if (!response.isSuccess()) {
                String errorMsg = String.format(
                        "视频上传失败：错误码=%s，错误信息=%s",
                        response.getCode(),
                        response.getMessage()
                );
                throw new RuntimeException(errorMsg);
            }

            return response.getVideoId();
        } catch (IOException e) {
            throw new RuntimeException("输入流处理失败", e);
        }
    }

    //获取播放凭证函数
    public GetVideoPlayAuthResponse getVideoPlayAuth(String videoId) throws Exception {
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        request.setVideoId(videoId);
        return initVodClient().getAcsResponse(request);
    }

}
