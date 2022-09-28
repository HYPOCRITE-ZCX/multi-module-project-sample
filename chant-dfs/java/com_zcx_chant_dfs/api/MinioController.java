package com_zcx_chant_dfs.api;

import com_zcx_chant_dfs.config.MinioConfig;
import com_zcx_chant_dfs.vo.R;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;


// minio 简单操作
public class MinioController {

    @Resource
    private MinioClient minioClient;

    @Resource
    private MinioConfig minioConfig;

    @Value("${minio.bucketName}")
    private String bucketName;

    @GetMapping("/list")
    public List<Object> list() throws Exception {
        //获取bucket列表
        Iterable<Result<Item>> myObjects = minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());
        Iterator<Result<Item>> iterator = myObjects.iterator();
        List<Object> items = new ArrayList<>();
        String format = "{'bucketName':'%s','fileName':'%s','fileSize':'%s','path':'%s'}";
        while (iterator.hasNext()) {
            Item item = iterator.next().get();
            String path = minioConfig.getEndpoint() + '/' + bucketName + '/' + item.objectName();
            // 依赖没有引入，暂时注释
            //items.add(JSONUtil.parse(String.format(format, bucketName, item.objectName(), formatFileSize(item.size()), path)));
        }
        return items;
    }

    @PostMapping("/upload")
    public R upload(@RequestParam(name = "file", required = false) MultipartFile[] file) {
        if (file == null || file.length == 0) {
            return R.fail("上传文件不能为空");
        }
        List<Map<String, Object>> orgfileNameList = new ArrayList<>(file.length);
        for (MultipartFile multipartFile : file) {
            String orgfileName = multipartFile.getOriginalFilename();
            //bucket得开启public策略，Manage->Summary->Access Policy:设置成public
            String path = minioConfig.getEndpoint() + '/' + bucketName + '/' + orgfileName;
            Map<String, Object> fileMap = new HashMap<>();
            fileMap.put("fileName", orgfileName);
            fileMap.put("path", path);
            orgfileNameList.add(fileMap);
            try {
                //文件上传
                InputStream in = multipartFile.getInputStream();
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucketName)
                                .object(orgfileName)
                                .stream(in, multipartFile.getSize(), -1)
                                .contentType(multipartFile.getContentType())
                                .build());
                in.close();
            } catch (Exception e) {
                return R.fail("上传失败");
            }
        }
        Map<String, Object> data = new HashMap<>();
        data.put("bucketName", bucketName);
        data.put("fileList", orgfileNameList);
        return R.data(data);
    }

    @GetMapping("/download/{fileName}")
    public void download(HttpServletResponse response, @PathVariable("fileName") String fileName) {
        InputStream in = null;
        try {
            // 获取对象信息
            StatObjectResponse stat = minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build());
            response.setContentType(stat.contentType());
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            //文件下载
            in = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build());
            IOUtils.copy(in, response.getOutputStream());
        } catch (Exception e) {
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
    }

    @DeleteMapping("/delete/{fileName}")
    public R delete(@PathVariable("fileName") String fileName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build());
        } catch (Exception e) {
            return R.fail("删除失败");
        }
        return R.success("删除成功");
    }

    @GetMapping("/getFilePreUrl/{fileName}")
    public R getFilePreUrl(@PathVariable("fileName") String fileName) {
        try {
            String url = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .method(Method.GET)
                    //过期时间1天：60 * 60 * 24
                    //默认最大7天失效:.expiry(7, TimeUnit.DAYS)
                    .expiry(7, TimeUnit.DAYS).build());
            return R.data(url);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("获取文件预览地址失败!");
        }
    }

    private static String formatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + " B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + " KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + " MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + " GB";
        }
        return fileSizeString;
    }
}
