package com.tiankong44.util;

import com.google.gson.Gson;
import com.qiniu.cdn.CdnManager;
import com.qiniu.cdn.CdnResult;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.tiankong44.model.VariableName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * 类作用描述：上传图片到服务器
 *
 * @author 12481
 */
@Slf4j
public class QiniuUpload {
    // 设置好账号的ACCESS_KEY和SECRET_KEY
    private static String ACCESS_KEY = VariableName.accessKey; // 这两个登录七牛 账号里面可以找到
    private static String SECRET_KEY = VariableName.secretKey;

    // 要上传的空间
    private static String bucketname = VariableName.bucket; // 对应要上传到七牛上 你的那个路径（自己建文件夹 注意设置公开）

    // 密钥配置
    private static Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    private static Configuration cfg = new Configuration(Zone.zone0());
    // 创建上传对象

    private static UploadManager uploadManager = new UploadManager(cfg);

    // 简单上传，使用默认策略，只需要设置上传的空间名就可以了
    public static String getUpToken() {
        return auth.uploadToken(bucketname);
    }

    public static String UploadPic(String FilePath, String FileName) {
        Configuration cfg = new Configuration(Zone.zone0());
        UploadManager uploadManager = new UploadManager(cfg);
        String accessKey = VariableName.accessKey; // AccessKey的值
        String secretKey = VariableName.secretKey; // SecretKey的值
        String bucket = VariableName.bucket; // 存储空间名
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(FilePath, FileName, upToken);
            // 解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
            return VariableName.domain + "/" + FileName;
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                // ignore
            }
        }
        return null;
    }

    public static String updateFile(MultipartFile file, String filename) throws Exception {
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        try {
            InputStream inputStream = file.getInputStream();
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            byte[] buff = new byte[600]; //buff用于存放循环读取的临时数据
            int rc = 0;
            while ((rc = inputStream.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }

            byte[] uploadBytes = swapStream.toByteArray();
            try {
                Response response = uploadManager.put(uploadBytes, filename, getUpToken());
                //解析上传成功的结果
                DefaultPutRet putRet;
                putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                log.info(putRet.toString());
                return VariableName.domain + "/" + putRet.key;

            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                }
            }
        } catch (UnsupportedEncodingException ex) {
        }
        return null;

    }

    public static void delete(String filename) throws QiniuException {
        Configuration cfg = new Configuration(Zone.zone0());//设置华南的服务器
        String accessKey = VariableName.accessKey; // AccessKey的值
        String secretKey = VariableName.secretKey; // SecretKey的值
        String bucket = VariableName.bucket; // 存储空间名
        Auth auth = Auth.create(accessKey, secretKey);

        BucketManager bucketManager = new BucketManager(auth, cfg);
        bucketManager.delete(bucket, filename);
    }

    public static void refresh(String url) throws QiniuException {
        String accessKey = VariableName.accessKey; // AccessKey的值
        String secretKey = VariableName.secretKey; // SecretKey的值
        String[] urls = {url};
        Auth auth = Auth.create(accessKey, secretKey);
        CdnManager c = new CdnManager(auth);
        CdnResult.RefreshResult response = c.refreshUrls(urls);
    }


}
