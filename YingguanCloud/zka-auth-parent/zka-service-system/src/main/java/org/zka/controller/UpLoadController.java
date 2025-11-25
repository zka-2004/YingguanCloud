package org.zka.controller;

import org.zka.common.utils.OssTemplate;
import org.zka.common.utils.Result;
import org.zka.common.utils.VodTemplate;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;

// 针对单个控制器 处理跨域  而非全局设置
@CrossOrigin
@RestController
@RequestMapping("/admin/system/upload")
@AllArgsConstructor
public class UpLoadController {
    private OssTemplate ossTemplate;
    private VodTemplate vodTemplate;
    // 上传图片
    @PostMapping("/uploadImage")
    public Result uploadImage(MultipartFile uploadImage) throws Exception{

//        String filename = uploadImage.getOriginalFilename();
//
//        // 上传到本地
//        uploadImage.transferTo(new File("D:\\images\\"+filename));
        //使用uploadImage 获取输入流
        InputStream inputStream = uploadImage.getInputStream();

        String imgUrl = this.ossTemplate.upload(uploadImage.getOriginalFilename(), inputStream);
        System.out.println("---------------------------------------------------");
        System.out.println(imgUrl);
        return Result.ok(imgUrl);
    }

    // 上传视频文件
    // /uploadVideo
    @PostMapping("/uploadVideo")
    public Result uploadVideo(MultipartFile uploadVideo) {
        try {
            InputStream inputStream = uploadVideo.getInputStream();
            String videoId = this.vodTemplate.uploadVideo(uploadVideo.getOriginalFilename(), inputStream);
            System.out.println("视频ID：" + videoId);
            return Result.ok(videoId); // 注意：阿里云VOD返回的是videoId，不是直接的播放URL，需通过videoId获取播放凭证
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("视频上传失败：" + e.getMessage());
        }
    }
}
