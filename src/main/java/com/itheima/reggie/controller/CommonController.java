package com.itheima.reggie.controller;

import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件上传和下载
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${imgpath}")
    private String basePath;

    /**
     * 文件上传
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        log.info("文件上传：{}", file.getOriginalFilename());
        try {
            // 1. 获取文件后缀
            String originalFilename = file.getOriginalFilename();
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

            // 2. 生成唯一文件名
            String fileName = UUID.randomUUID().toString() + suffix;

            // 3. 创建目录
            File dir = new File(basePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 4. 绝对安全的路径拼接（这是最稳的写法）
            String filePath = basePath + "/" + fileName;
            file.transferTo(new File(filePath));

            log.info("文件保存成功：{}", filePath);
            return R.success(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return R.error("上传失败");
        }
    }

    /**
     * 文件下载（显示图片）
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        try {
            // 拼接正确路径
            String filePath = basePath + "/" + name;
            FileInputStream fis = new FileInputStream(filePath);
            ServletOutputStream sos = response.getOutputStream();

            // 设置响应类型
            response.setContentType("image/jpeg");

            int len;
            byte[] buffer = new byte[1024];
            while ((len = fis.read(buffer)) != -1) {
                sos.write(buffer, 0, len);
                sos.flush();
            }

            sos.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}