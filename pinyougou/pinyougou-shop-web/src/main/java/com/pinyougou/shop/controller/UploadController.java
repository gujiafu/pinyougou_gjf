package com.pinyougou.shop.controller;

import com.pinyougou.common.util.FastDFSClient;
import com.pinyougou.vo.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author GuJiaFu
 * @date 2018/5/4
 */
@RestController
public class UploadController {

    @PostMapping("/uploadFile")
    public Result uploadFile(MultipartFile file){

        try {
            String ext_fileName = file.getOriginalFilename().
                    substring(file.getOriginalFilename().lastIndexOf("."));
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:fastdfs\\tracker.conf");
            String url = fastDFSClient.uploadFile(file.getBytes(), ext_fileName);
            return Result.ok(url);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("上传失败");
        }
    }

}
