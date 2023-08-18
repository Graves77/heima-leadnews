package com.heima.minio;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MinIOTest {
    /**
     * 把list里面的文件上传到minio里面，并且可以在浏览器中访问
     * @param args
     */
    public static void main(String[] args) {

        try {
            FileInputStream fileInputStream = new FileInputStream("要上传的文件路径");
            //1.获取minio的链接信息，创建一个minio的客户端
            MinioClient minioClient = MinioClient.builder().credentials("minioadmin", "minioadmin")
                    .endpoint("http://8.134.120.93:9090").build();

            //2.上传
            PutObjectArgs objectArgs = PutObjectArgs.builder()
                    .object("list.html")//文件名称
                    .contentType("text/html")//文件类型
                    .bucket("leadnews")//创建的桶名称
                    .stream(fileInputStream,fileInputStream.available(),-1).build();
            minioClient.putObject(objectArgs);

            //访问路径
            System.out.println("http://8.134.120.93:9090/leadnews/list.html");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
