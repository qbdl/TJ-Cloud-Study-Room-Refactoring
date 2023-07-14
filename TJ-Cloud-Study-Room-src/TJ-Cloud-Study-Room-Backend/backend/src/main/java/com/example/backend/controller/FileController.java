package com.example.backend.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.example.backend.common.Result;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {
    @Value("${server.port}")
    private String port;

    private static final String ip = "http://localhost";

    @PostMapping("/upload")  // 上传
    public Result<?> upload(MultipartFile file) throws IOException {
        //获取原文件名称
        String originalFilename = file.getOriginalFilename();
        // 加前缀，作为唯一标识，防止重名
        String flag = IdUtil.fastSimpleUUID();
        //获取上传路径
        // 本地
        String rootFilePath = System.getProperty("user.dir") + "/backend/src/main/resources/files/" + flag + "_" + originalFilename;

        // 云端
//        String rootFilePath = "/var/www/html/dist/userImg/" + flag + "_" + originalFilename;

        //把文件写入到上传路径
        FileUtil.writeBytes(file.getBytes(), rootFilePath);

        // 返回结果 URL
        // 本地
        return Result.success(ip + ":" + port + "/files/" + flag + "_" + originalFilename);
        // 云端
//        return Result.success("http://121.37.184.56/userImg/" + flag + "_" + originalFilename);
    }

    @GetMapping("/{flag}")  // 下载
    public void getFiles(@PathVariable String flag, HttpServletResponse response) {
        // 新建输出流对象
        OutputStream os;
        // 定义文件上传的根路径
        // 本地
        String basePath = System.getProperty("user.dir") + "/backend/src/main/resources/files/";
        // 云端
//        String basePath = "/var/www/html/dist/userImg/";
        // 获取所有文件名称
        List<String> fileNames = FileUtil.listFileNames(basePath);
        // 找到与参数一致的文件
        String fileName = fileNames.stream().filter(name -> name.contains(flag)).findAny().orElse("");
        try {
            if (StrUtil.isAllNotEmpty(fileName)) {
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
                response.setContentType("application/octet-stream");
                // 通过文件路径读取文件字节流
                byte[] bytes = FileUtil.readBytes(basePath + fileName);
                // 通过输出流返回文件
                os = response.getOutputStream();
                os.write(bytes);
                os.flush();
                os.close();
            }
        } catch (Exception e) {
            System.out.println("文件下载失败");
        }
    }
}
